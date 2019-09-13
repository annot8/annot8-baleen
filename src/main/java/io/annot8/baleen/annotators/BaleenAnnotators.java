/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.annotators;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import uk.gov.dstl.baleen.exceptions.BaleenException;

import io.annot8.api.data.Item;
import io.annot8.baleen.utils.AbstractBaleenProcessor;
import io.annot8.baleen.utils.BaleenSettings;
import io.annot8.baleen.utils.JCasPopulator;
import io.annot8.common.data.content.Text;

public class BaleenAnnotators extends AbstractBaleenProcessor {

  public BaleenAnnotators(BaleenSettings settings) {
    super(settings);
  }

  public void processItem(Item item) {
    item.getContents(Text.class).forEach(t -> processText(item, t));
  }

  private void processText(Item item, Text text) {
    InputStream content = IOUtils.toInputStream(text.getData(), StandardCharsets.UTF_8);

    try {
      processWithBaleen(
          text, content, new JCasPopulator(text), new BaleenAnnotatorConsumer(item, text));
    } catch (BaleenException e) {
      log().error("Baleen unable to process text", e);
    }
  }
}
