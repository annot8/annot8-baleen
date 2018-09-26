package io.annot8.baleen.annotators;

import io.annot8.baleen.utils.AbstractBaleenProcessor;
import io.annot8.baleen.utils.JCasPopulator;
import io.annot8.common.data.content.Text;
import io.annot8.core.context.Context;
import io.annot8.core.data.Item;
import io.annot8.core.settings.SettingsClass;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import uk.gov.dstl.baleen.exceptions.BaleenException;

@SettingsClass(BaleenAnnotatorSettings.class)
public class BaleenAnnotators extends AbstractBaleenProcessor {


  @Override
  protected String getYaml(Context context) {
    Optional<BaleenAnnotatorSettings> settings = context.getSettings(BaleenAnnotatorSettings.class);
    return settings.map(BaleenAnnotatorSettings::getYaml).orElse("");
  }

  public void processItem(Item item) {
    item.getContents(Text.class)
        .forEach(t -> processText(item, t));
  }

  private void processText(Item item, Text text) {
    InputStream content = IOUtils.toInputStream(text.getData(), StandardCharsets.UTF_8);

    try {
      processWithBaleen(text, content, new JCasPopulator(text),
          new BaleenAnnotatorConsumer(item, text));
    } catch (BaleenException e) {
      log().error("Baleen unable to process text", e);
    }
  }
}
