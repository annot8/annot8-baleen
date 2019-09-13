/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.api.Test;
import uk.gov.dstl.baleen.types.common.Person;
import uk.gov.dstl.baleen.uima.testing.JCasSingleton;

import static org.assertj.core.api.Assertions.assertThat;


class BaleenTypeMapperTest {

  @Test
  void fromBaleenToAnnot8() throws UIMAException {
    BaleenTypeMapper mapper = new BaleenTypeMapper();
    JCas jCas = JCasSingleton.getJCasInstance();
    Person p = new Person(jCas);
    assertThat(mapper.fromBaleenToAnnot8(p).get()).isEqualTo("entity/person");
  }
}
