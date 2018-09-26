package io.annot8.baleen.annotators;

import io.annot8.core.settings.Settings;

public class BaleenAnnotatorSettings implements Settings {

  private String yaml = "annotators:\n  - regex.Email";

  public String getYaml() {
    return yaml;
  }

  public void setYaml(String yaml) {
    this.yaml = yaml;
  }

  @Override
  public boolean validate() {
    return yaml != null;
  }
}
