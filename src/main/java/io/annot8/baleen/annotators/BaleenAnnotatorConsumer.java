/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.annotators;

import java.util.function.Consumer;

import org.apache.uima.jcas.JCas;

import io.annot8.api.data.Item;
import io.annot8.baleen.utils.JCasExtractor;
import io.annot8.common.data.content.Text;

public class BaleenAnnotatorConsumer implements Consumer<JCas> {

  private final Item item;
  private final Text text;

  public BaleenAnnotatorConsumer(Item item, Text text) {
    this.item = item;
    this.text = text;
  }

  public void accept(JCas jCas) {
    JCasExtractor extractor = new JCasExtractor(jCas, text.getAnnotations(), item.getGroups());
    extractor.extract();
  }
}
