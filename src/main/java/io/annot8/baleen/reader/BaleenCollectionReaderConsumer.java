/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.reader;

import java.util.function.Consumer;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.dstl.baleen.uima.utils.UimaTypesUtils;

import io.annot8.api.data.Item;
import io.annot8.api.stores.AnnotationStore;
import io.annot8.baleen.Constants;
import io.annot8.baleen.utils.JCasExtractor;
import io.annot8.common.data.content.Text;

public class BaleenCollectionReaderConsumer implements Consumer<JCas> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(BaleenCollectionReaderConsumer.class);

  private final Item item;
  private final String description;

  public BaleenCollectionReaderConsumer(Item item, String description) {
    this.item = item;
    this.description = description;
  }

  @Override
  public void accept(JCas jCas) {
    Text.Builder<Text, String> builder =
        item.createContent(Text.class)
            .withDescription(description)
            .withData(jCas.getDocumentText());
    addDocumentAnnotations(jCas, builder);

    Text text = builder.save();

    AnnotationStore annotations = text.getAnnotations();

    JCasExtractor extractor = new JCasExtractor(jCas, annotations, item.getGroups());
    extractor.extract();
  }

  private void addDocumentAnnotations(JCas jCas, Text.Builder builder) {

    // Add document annotations

    DocumentAnnotation da = (DocumentAnnotation) jCas.getDocumentAnnotationFs();

    builder.withProperty(Constants.DA_TYPE, da.getDocType());
    builder.withProperty(Constants.DA_CLASSIFICATION, da.getDocumentClassification());
    builder.withProperty(Constants.DA_CAVEATS, UimaTypesUtils.toList(da.getDocumentCaveats()));
    builder.withProperty(
        Constants.DA_RELEASABILITY, UimaTypesUtils.toList(da.getDocumentReleasability()));
    builder.withProperty(Constants.DA_HASH, da.getHash());
    builder.withProperty(Constants.DA_LANGUAGE, da.getLanguage());
    builder.withProperty(Constants.DA_SOURCE, da.getSourceUri());
    builder.withProperty(Constants.DA_TIMESTAMP, da.getTimestamp());
  }
}
