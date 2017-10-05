package com.gcpce.demo.gae.ml;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created in gae-ml-java on 10/5/17.
 */
@Component
@Profile("!appengine")
public class MLLocalConfig {

  @Value("${PROJECT_ID}")
  private String projectId;

  @Value("${GOOGLE_CREDENTIALS}")
  String GOOGLE_CREDENTIALS;

  public GoogleCredentials getCredentials() {
    try {
      return GoogleCredentials.fromStream(new ByteArrayInputStream(GOOGLE_CREDENTIALS.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Bean
  public Storage getStorage() {
    return StorageOptions.newBuilder().setProjectId(projectId).setCredentials(getCredentials()).build().getService();
  }


}
