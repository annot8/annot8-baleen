/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import java.util.function.Consumer;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;

import uk.gov.dstl.baleen.types.metadata.Metadata;

import io.annot8.common.data.content.Text;

public class JCasPopulator implements Consumer<JCas> {

  private final Text text;

  public JCasPopulator(Text text) {
    this.text = text;
  }

  @Override
  public void accept(JCas jCas) {

    populateDocumentAnnotation((DocumentAnnotation) jCas.getDocumentAnnotationFs());

    populateMetadata(jCas);

    // TODO: Populate POS
    // TODO: Populate structural
    // TODO: Populate annotations
    // TODO: populate entities, relations from groups
  }

  private void populateMetadata(JCas jCas) {
    text.getAnnotations()
        .getByType("metadata")
        .forEach(
            a -> {
              Metadata m = new Metadata(jCas);
              a.getProperties().get("key", String.class).ifPresent(m::setKey);
              a.getProperties().get("value", String.class).ifPresent(m::setValue);

              m.addToIndexes();
            });
  }

  private void populateDocumentAnnotation(DocumentAnnotation da) {
    // TODO from properties...
  }
}
