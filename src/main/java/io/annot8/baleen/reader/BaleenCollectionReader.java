/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import uk.gov.dstl.baleen.exceptions.BaleenException;

import io.annot8.baleen.utils.AbstractBaleenProcessor;
import io.annot8.common.data.content.FileContent;
import io.annot8.core.context.Context;
import io.annot8.core.data.Item;

public class BaleenCollectionReader extends AbstractBaleenProcessor {

  private static final String CONTENT_NAME_POSTFIX = ".text";

  public String getYaml(Context context) {
    // Just the text conversion
    return "";
  }

  public void processItem(Item item) {
    item.getContents(FileContent.class).forEach(f -> processFile(item, f));
  }

  private void processFile(Item item, FileContent file) {
    try (InputStream is = new FileInputStream(file.getData())) {
      processWithBaleen(
          file, is, new BaleenCollectionReaderConsumer(item, createContentName(file)));
    } catch (BaleenException e) {
      log().error("Baleen unable to process", e);
    } catch (IOException e) {
      log().error("Unable to process", e);
    }
  }

  private String createContentName(FileContent file) {
    return file.getName() + CONTENT_NAME_POSTFIX;
  }
}
