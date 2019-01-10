/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

import io.annot8.conventions.PathUtils;

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
  public static final String TYPE_ENTITY_PREFIX = "entity";
  public static final String TYPE_STRUCTURAL_PREFIX = "structural";

  // Annotation properties
  private static final String DA_PREFIX = "da";
  public static final String DA_TYPE = PathUtils.join(DA_PREFIX, "type");
  public static final String DA_CLASSIFICATION = PathUtils.join(DA_PREFIX, "classification");
  public static final String DA_CAVEATS = PathUtils.join(DA_PREFIX, "caveats");
  public static final String DA_RELEASABILITY = PathUtils.join(DA_PREFIX, "releasability");
  public static final String DA_HASH = PathUtils.join(DA_PREFIX, "hash");
  public static final String DA_LANGUAGE = PathUtils.join(DA_PREFIX, "language");
  public static final String DA_SOURCE = PathUtils.join(DA_PREFIX, "source");
  public static final String DA_TIMESTAMP = PathUtils.join(DA_PREFIX, "timestamp");

  private static final String METADATA_PREFIX = "metadata";
  public static final String METADATA_KEY = PathUtils.join(METADATA_PREFIX, "key");
  public static final String METADATA_VALUE = PathUtils.join(METADATA_PREFIX, BALEEN_VALUE);

  private static final String BLOCK_PREFIX = "block";
  public static final String BLOCK_BEGIN = PathUtils.join(BLOCK_PREFIX, "begin");
  public static final String BLOCK_END = PathUtils.join(BLOCK_PREFIX, "end");

  // Content properties
  private static final String STRUCTURAL_PREFIX = "structural";
  public static final String STRUCTURAL_DEPTH = PathUtils.join(STRUCTURAL_PREFIX, "depth");
  public static final String STRUCTURAL_ID = PathUtils.join(STRUCTURAL_PREFIX, "id");
  public static final String STRUCTURAL_CLASS = PathUtils.join(STRUCTURAL_PREFIX, "class");

  private Constants() {
    // Singleton
  }
}
