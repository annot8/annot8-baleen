/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import io.annot8.api.settings.Settings;

public class BaleenSettings implements Settings {
  private static final int DEFAULT_POOL_SIZE = 1;

  private final String yaml;

  private final int poolSize;

  public BaleenSettings(String yaml) {
    this(yaml, DEFAULT_POOL_SIZE);
  }

  public BaleenSettings(String yaml, int poolSize) {
    this.yaml = yaml;
    this.poolSize = 1;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public String getYaml() {
    return yaml;
  }

  @Override
  public boolean validate() {
    return yaml != null && !yaml.isEmpty();
  }
}
