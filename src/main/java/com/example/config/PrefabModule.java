package com.example.config;

import cloud.prefab.client.ConfigClient;
import cloud.prefab.client.FeatureFlagClient;
import cloud.prefab.client.Options;
import cloud.prefab.client.PrefabCloudClient;
import cloud.prefab.client.config.logging.PrefabContextTurboFilter;
import cloud.prefab.client.internal.ConfigClientImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrefabModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(PrefabModule.class);

  protected void configure() {
    // do configuration
  }

  @Provides
  @Singleton
  PrefabCloudClient getPrefabCloudClient(){
    return new PrefabCloudClient(new Options());
  }
  @Singleton
  @Provides
  ConfigClient getConfigClient(PrefabCloudClient prefabCloudClient){
    ConfigClient configClient = new ConfigClientImpl(
        prefabCloudClient
    );
    LOGGER.info("Installing PrefabContextTurboFilter");
    PrefabContextTurboFilter.install(configClient);
    return configClient;
  }

  @Singleton
  @Provides FeatureFlagClient featureFlagClient(PrefabCloudClient prefabCloudClient) {
    return prefabCloudClient.featureFlagClient();
  }

}
