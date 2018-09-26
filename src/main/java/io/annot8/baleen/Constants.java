package io.annot8.baleen;


import uk.gov.dstl.baleen.types.structure.Structure;

public final class Constants {

  public static final String PREFIX = "baleen.";


  // Baleen annotation propreties

  public static final String BALEEN_ID = PREFIX+"id";
  public static final String BALEEN_VALUE = PREFIX+"value";


  // Document annotations related properties

  public static final String DA_PREFIX = PREFIX + ".da.";
  public static final String DA_TYPE = DA_PREFIX + "type";
  public static final String DA_CLASSIFICATION = DA_PREFIX + "classification";
  public static final String DA_CAVEATS = DA_PREFIX + "caveats";
  public static final String DA_RELEASABILITY = DA_PREFIX + "releasability";
  public static final String DA_HASH = DA_PREFIX + "hash";
  public static final String DA_LANGUAGE = DA_PREFIX+ "language";
  public static final String DA_SOURCE = DA_PREFIX+"source";
  public static final String DA_TIMESTAMP = DA_PREFIX+"timestamp";

  // Annotation properties

  public static final String METADATA_PREFIX = PREFIX + "metadata.";
  public static final String METADATA_KEY = METADATA_PREFIX+"key";
  public static final String METADATA_VALUE = METADATA_PREFIX+"value";
  public static final String BALEEN_TYPE = PREFIX + "type";


  public static final String STRUCTURAL_PREFIX = PREFIX + "structural.";
  public static final String STRUCTURAL_DEPTH = STRUCTURAL_PREFIX + "depth";
  public static final String STRUCTURAL_ID = STRUCTURAL_PREFIX + "id";
  public static final String STRUCTURAL_CLASS = STRUCTURAL_PREFIX + "class";

  // Group types

  public static final String GROUP_COREFERENCE = PREFIX + "coreference";
  public static final String GROUP_RELATION = PREFIX + "relation";
  public static final String GROUP_DEPENDENCY = PREFIX + "dependency";

  // Annotation types

  public static final  String TYPE_WORD_TOKEN = PREFIX + "token";
  public static final  String TYPE_SENTENCE = PREFIX + "sentence";

  public static final  String TYPE_METADATA = "metadata";
  public static final String TYPE_LANGUAGE_TEXT = PREFIX + "types.text";
  public static final String TYPE_PUBLISHED_ID = PREFIX + "publishedId";
  public static final String TYPE_LEMMA = PREFIX + "lemma";
  public static final String TYPE_PARAGRAPH = PREFIX + "paragraph";
  public static final String TYPE_CHUNK = PREFIX + "chunk";

  public static final String TYPE_PROTECTIVE_MARKING = PREFIX + "protectiveMarking";


  public static String structualType(Structure s) {
    return String.format(STRUCTURAL_PREFIX+".%s", s.getType().getShortName());
  }

  // Content properties

  public static final String BLOCK_PREFIX = PREFIX + "block.";
  public static final String BLOCK_BEGIN = BLOCK_PREFIX + "begin";
  public static final String BLOCK_END = BLOCK_PREFIX + "end";

  private Constants() {
    // Singleton
  }
}
