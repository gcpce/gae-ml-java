package com.gcpce.demo.gae.ml;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created in gae-ml-java on 10/5/17.
 */
@Component
@Profile("appengine")
public class MLAppEngineConfig {


  @Bean
  public Storage getStorage() {
    return StorageOptions.getDefaultInstance().getService();
  }


}
