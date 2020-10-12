/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import static org.assertj.core.api.Assertions.assertThat;

import io.annot8.common.data.content.Text;
import io.annot8.testing.testimpl.content.TestStringContent;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.api.Test;
import uk.gov.dstl.baleen.types.metadata.Metadata;
import uk.gov.dstl.baleen.uima.testing.JCasSingleton;

class JCasPopulatorTest {

  @Test
  void accept() throws UIMAException {
    JCas jCas = JCasSingleton.getJCasInstance();
    Text text = new TestStringContent();
    JCasPopulator populator = new JCasPopulator(text);

    text.getAnnotations()
        .create()
        .withType("metadata")
        .withProperty("key", "1")
        .withProperty("value", "2")
        .save();

    populator.accept(jCas);

    Metadata metadata = JCasUtil.select(jCas, Metadata.class).stream().findFirst().get();

    assertThat(metadata.getKey()).isEqualTo("1");
    assertThat(metadata.getValue()).isEqualTo("2");
  }
}
