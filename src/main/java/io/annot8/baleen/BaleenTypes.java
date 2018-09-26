package io.annot8.baleen;

import uk.gov.dstl.baleen.types.BaleenAnnotation;
import uk.gov.dstl.baleen.types.common.Buzzword;
import uk.gov.dstl.baleen.types.common.Chemical;
import uk.gov.dstl.baleen.types.common.CommsIdentifier;
import uk.gov.dstl.baleen.types.common.DocumentReference;
import uk.gov.dstl.baleen.types.common.Frequency;
import uk.gov.dstl.baleen.types.common.Money;
import uk.gov.dstl.baleen.types.common.Nationality;
import uk.gov.dstl.baleen.types.common.Organisation;
import uk.gov.dstl.baleen.types.common.Person;
import uk.gov.dstl.baleen.types.common.Quantity;
import uk.gov.dstl.baleen.types.common.Url;
import uk.gov.dstl.baleen.types.common.Vehicle;
import uk.gov.dstl.baleen.types.geo.Coordinate;
import uk.gov.dstl.baleen.types.language.Dependency;
import uk.gov.dstl.baleen.types.language.Paragraph;
import uk.gov.dstl.baleen.types.language.Pattern;
import uk.gov.dstl.baleen.types.language.PhraseChunk;
import uk.gov.dstl.baleen.types.language.Sentence;
import uk.gov.dstl.baleen.types.language.WordLemma;
import uk.gov.dstl.baleen.types.language.WordToken;
import uk.gov.dstl.baleen.types.metadata.Metadata;
import uk.gov.dstl.baleen.types.metadata.ProtectiveMarking;
import uk.gov.dstl.baleen.types.metadata.PublishedId;
import uk.gov.dstl.baleen.types.military.MilitaryPlatform;
import uk.gov.dstl.baleen.types.military.Weapon;
import uk.gov.dstl.baleen.types.semantic.Entity;
import uk.gov.dstl.baleen.types.semantic.Location;
import uk.gov.dstl.baleen.types.semantic.Temporal;

public enum BaleenTypes {
  // From Base:
  WORD_TOKEN(WordToken.class),
  SENTENCE(Sentence.class),
  PHRASE_CHUNK(PhraseChunk.class),
  PARAGRAPH(Paragraph.class),
  WORD_LEMMA(WordLemma.class),
  DEPENDENCY(Dependency.class),
  PATTERN(Pattern.class),

  // From BaleenAnnotation
  PROTECTIVE_MARKING(ProtectiveMarking.class),
  PUBLISHED_ID(PublishedId.class),
  METADATA(Metadata.class),

  // From Entity
  ENTITY(Entity.class),
  PERSON(Person.class),
  LOCATION(Location.class),
  COORDINATE(Coordinate.class),
  ORGANISATION(Organisation.class),
  VEHICLE(Vehicle.class),
  TEMPORAL(Temporal.class),
  BUZZWORD(Buzzword.class),
  CHEMICAL(Chemical.class),
  COMMS_IDENTIFIER(CommsIdentifier.class),
  DOCUMENT_REFERENCE(DocumentReference.class),
  FREQUENCY(Frequency.class),
  MILITARY_PLATFORM(MilitaryPlatform.class),
  MONEY(Money.class),
  NATIONALITY(Nationality.class),
  QUANTITY(Quantity.class),
  URL(Url.class),
  WEAPONS(Weapon.class);

  private final Class<? extends BaleenAnnotation> clazz;

  BaleenTypes(Class<? extends BaleenAnnotation> clazz) {
    this.clazz = clazz;
  }

  public Class<? extends BaleenAnnotation> getClazz() {
    return clazz;
  }
}
