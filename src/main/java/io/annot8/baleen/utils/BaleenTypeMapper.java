/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import static uk.gov.dstl.baleen.consumers.utils.ConsumerUtils.toCamelCase;

import java.util.Optional;

import org.apache.uima.jcas.cas.TOP;

import uk.gov.dstl.baleen.types.language.PhraseChunk;
import uk.gov.dstl.baleen.types.language.Text;
import uk.gov.dstl.baleen.types.language.WordLemma;
import uk.gov.dstl.baleen.types.language.WordToken;
import uk.gov.dstl.baleen.types.semantic.Entity;
import uk.gov.dstl.baleen.types.structure.Structure;

import io.annot8.baleen.Constants;
import io.annot8.conventions.PathUtils;

public class BaleenTypeMapper {

  public Optional<String> fromBaleenToAnnot8(TOP annotation) {

    if (annotation == null) {
      return Optional.empty();
    }

    String annot8;

    String name = toCamelCase(annotation.getType().getShortName());

    if (annotation instanceof Structure) {
      annot8 = PathUtils.join(Constants.TYPE_STRUCTURAL_PREFIX, name);
    } else if (annotation instanceof WordToken) {
      annot8 = Constants.TYPE_WORD_TOKEN;
    } else if (annotation instanceof WordLemma) {
      annot8 = Constants.TYPE_LEMMA;
    } else if (annotation instanceof PhraseChunk) {
      annot8 = Constants.TYPE_PHRASE_CHUNK;
    } else if (annotation instanceof Text) {
      annot8 = Constants.TYPE_LANGUAGE_TEXT;
    } else if (annotation instanceof Entity) {
      annot8 = PathUtils.join(Constants.TYPE_ENTITY_PREFIX, name);
    } else {
      annot8 = name;
    }

    return Optional.ofNullable(annot8);
  }
}
