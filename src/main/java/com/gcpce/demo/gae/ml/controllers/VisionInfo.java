package com.gcpce.demo.gae.ml.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created in gae-ml-java on 10/5/17.
 */
@RestController
@RequestMapping(value = "/vision", produces = "application/json")
public class VisionInfo {

  @Autowired
  Storage storage;


  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public String getMetadata(@RequestParam String bucket,
                                          @RequestParam String file) {
    try {
      ImageAnnotatorClient vision = ImageAnnotatorClient.create();
      Blob blob = storage.get(BlobId.of(bucket, file));
      byte[] imageBytes = blob.getContent();
      AnnotateImageRequest request =
              AnnotateImageRequest
                      .newBuilder()
                      .setImage(Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build())
                      .addFeatures(Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build())
                      .addFeatures(Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build())
                      .addFeatures(Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build())
                      .build();
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Collections.singletonList(request));
      List<AnnotateImageResponse> responsesList = response.getResponsesList();
      AnnotateImageResponse imageResponse = responsesList.get(0);

      Gson gson = new Gson();
      JsonElement safeSearch = gson.toJsonTree(imageResponse.getSafeSearchAnnotation());

      JsonArray faceAnnotations = new JsonArray();
      for (FaceAnnotation faceAnnotation : imageResponse.getFaceAnnotationsList()) {
        faceAnnotations.add(gson.toJsonTree(faceAnnotation, FaceAnnotation.class));
      }

      JsonArray labelAnnotations = new JsonArray();
      for (EntityAnnotation entityAnnotation : imageResponse.getLabelAnnotationsList()) {
        labelAnnotations.add(gson.toJsonTree(entityAnnotation, EntityAnnotation.class));
      }

      JsonObject info = new JsonObject();
      info.add("safeSearch", safeSearch);
      info.add("faces", faceAnnotations);
      info.add("labels", labelAnnotations);

      return gson.toJson(info);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
