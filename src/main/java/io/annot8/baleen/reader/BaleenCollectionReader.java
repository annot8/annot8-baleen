/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.reader;

import io.annot8.api.data.Item;
import io.annot8.baleen.utils.AbstractBaleenProcessor;
import io.annot8.baleen.utils.BaleenSettings;
import io.annot8.common.data.content.FileContent;
import uk.gov.dstl.baleen.exceptions.BaleenException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaleenCollectionReader extends AbstractBaleenProcessor {

  public BaleenCollectionReader() {
    super(new BaleenSettings(""));
  }

  public void processItem(Item item) {
    item.getContents(FileContent.class).forEach(f -> processFile(item, f));
  }

  private void processFile(Item item, FileContent file) {
    try (InputStream is = new FileInputStream(file.getData())) {
      processWithBaleen(
          file, is, new BaleenCollectionReaderConsumer(item, createDescription(file)));
    } catch (BaleenException e) {
      log().error("Baleen unable to process", e);
    } catch (IOException e) {
      log().error("Unable to process", e);
    }
  }

  private String createDescription(FileContent file) {
    return String.format("Extracted from FileContent[%s]", file.getId());
  }
}
