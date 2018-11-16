/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

public final class Constants {

  // Baleen annotation propreties

  public static final String BALEEN_ID = "id";
  public static final String BALEEN_VALUE = "value";

  // Document annotations related properties
  public static final String BALEEN_TYPE = "type";
  public static final String GROUP_COREFERENCE = "coreference";
  public static final String GROUP_RELATION = "relation";
  public static final String GROUP_DEPENDENCY = "dependency";
  public static final String TYPE_WORD_TOKEN = "token";
  // Group types
  public static final String TYPE_LANGUAGE_TEXT = "block";
  public static final String TYPE_LEMMA = "lemma";
  public static final String TYPE_PHRASE_CHUNK = "chunk";
  // Special annotation types (othres are baleen.getType().getShortName())
  public static final String TYPE_ENTITY_PREFIX = "entity.";

  // Annotation properties
  public static final String TYPE_STRUCTURAL_PREFIX = "structural.";
  private static final String DA_PREFIX = ".da.";
  public static final String DA_TYPE = DA_PREFIX + "type";
  public static final String DA_CLASSIFICATION = DA_PREFIX + "classification";
  public static final String DA_CAVEATS = DA_PREFIX + "caveats";
  public static final String DA_RELEASABILITY = DA_PREFIX + "releasability";
  public static final String DA_HASH = DA_PREFIX + "hash";
  public static final String DA_LANGUAGE = DA_PREFIX + "language";
  public static final String DA_SOURCE = DA_PREFIX + "source";
  public static final String DA_TIMESTAMP = DA_PREFIX + "timestamp";
  private static final String METADATA_PREFIX = "metadata.";
  public static final String METADATA_KEY = METADATA_PREFIX + "key";
  public static final String METADATA_VALUE = METADATA_PREFIX + "value";
  private static final String BLOCK_PREFIX = "block.";
  public static final String BLOCK_BEGIN = BLOCK_PREFIX + "begin";
  public static final String BLOCK_END = BLOCK_PREFIX + "end";
  private static final String STRUCTURAL_PREFIX = "structural";

  // Content properties
  public static final String STRUCTURAL_DEPTH = STRUCTURAL_PREFIX + "Depth";
  public static final String STRUCTURAL_ID = STRUCTURAL_PREFIX + "Id";
  public static final String STRUCTURAL_CLASS = STRUCTURAL_PREFIX + "Class";

  private Constants() {
    // Singleton
  }
}
