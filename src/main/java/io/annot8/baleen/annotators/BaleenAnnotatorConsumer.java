package io.annot8.baleen.annotators;

import io.annot8.baleen.utils.JCasExtractor;
import io.annot8.common.data.content.Text;
import io.annot8.core.data.Item;
import java.util.function.Consumer;
import org.apache.uima.jcas.JCas;

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
