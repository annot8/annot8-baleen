/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.annotators;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.api.Test;

import uk.gov.dstl.baleen.uima.testing.JCasSingleton;

import io.annot8.testing.testimpl.TestItem;
import io.annot8.testing.testimpl.content.TestStringContent;

class BaleenAnnotatorConsumerTest {

  @Test
  void accept() throws UIMAException {

    TestItem item = new TestItem();
    TestStringContent text = new TestStringContent();
    BaleenAnnotatorConsumer consumer = new BaleenAnnotatorConsumer(item, text);

    JCas jCas = JCasSingleton.getJCasInstance();
    consumer.accept(jCas);

    // This isn't testing much other than it doesn't error
  }
}
