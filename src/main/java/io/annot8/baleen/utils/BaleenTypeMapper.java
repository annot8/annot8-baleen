/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import static uk.gov.dstl.baleen.consumers.utils.ConsumerUtils.toCamelCase;

import io.annot8.baleen.Constants;
import io.annot8.conventions.AnnotationTypes;
import io.annot8.conventions.PathUtils;
import java.util.Optional;
import org.apache.uima.jcas.cas.TOP;
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
import uk.gov.dstl.baleen.types.common.Vulnerability;
import uk.gov.dstl.baleen.types.geo.Coordinate;
import uk.gov.dstl.baleen.types.language.PhraseChunk;
import uk.gov.dstl.baleen.types.language.Text;
import uk.gov.dstl.baleen.types.language.WordLemma;
import uk.gov.dstl.baleen.types.language.WordToken;
import uk.gov.dstl.baleen.types.military.MilitaryPlatform;
import uk.gov.dstl.baleen.types.military.Weapon;
import uk.gov.dstl.baleen.types.semantic.Entity;
import uk.gov.dstl.baleen.types.semantic.Location;
import uk.gov.dstl.baleen.types.semantic.Temporal;
import uk.gov.dstl.baleen.types.structure.Structure;

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
      annot8 = mapEntity((Entity) annotation);
    } else {
      annot8 = name;
    }

    return Optional.ofNullable(annot8);
  }

  private String mapEntity(Entity entity) {

    String type = entity.getType().getShortName();
    String subType = entity.getSubType();

    if (entity instanceof Buzzword) {
      // Could use tags here.. but you can have multiple tags
      type = AnnotationTypes.ANNOTATION_TYPE_CONCEPT;
    } else if (entity instanceof Chemical) {
      type = AnnotationTypes.ANNOTATION_TYPE_CHEMICAL;
    } else if (entity instanceof CommsIdentifier) {
      if (subType != null) {
        switch (subType) {
          case "email":
            type = AnnotationTypes.ANNOTATION_TYPE_EMAIL;
            subType = null;
            break;
          case "domain":
            type = AnnotationTypes.ANNOTATION_TYPE_DOMAIN;
            subType = null;
            break;
          case "ipv4address":
            type = AnnotationTypes.ANNOTATION_TYPE_IPADDRESS;
            subType = null;
            break;
          case "ipv6address":
            type = AnnotationTypes.ANNOTATION_TYPE_IPADDRESS;
            subType = null;
            break;
          case "telephone":
            type = AnnotationTypes.ANNOTATION_TYPE_PHONENUMBER;
            subType = null;
            break;
          case "username":
            type = AnnotationTypes.ANNOTATION_TYPE_USERNAME;
            subType = null;
            break;
        }
      }
    } else if (entity instanceof DocumentReference) {
      type = AnnotationTypes.ANNOTATION_TYPE_REFERENCE;
    } else if (entity instanceof Frequency) {
      type = AnnotationTypes.ANNOTATION_TYPE_FREQUENCY;
    } else if (entity instanceof Money) {
      type = AnnotationTypes.ANNOTATION_TYPE_MONEY;
    } else if (entity instanceof Nationality) {
      type = AnnotationTypes.ANNOTATION_TYPE_NATIONALITY;
    } else if (entity instanceof Organisation) {
      type = AnnotationTypes.ANNOTATION_TYPE_GEOPOLITICALENTITY;
    } else if (entity instanceof Person) {
      type = AnnotationTypes.ANNOTATION_TYPE_PERSON;
    } else if (entity instanceof Quantity) {
      Quantity q = (Quantity) entity;
      type = AnnotationTypes.ANNOTATION_TYPE_QUANTITY;

      if (subType != null) {
        switch (subType) {
          default:
          case "":
            type = AnnotationTypes.ANNOTATION_TYPE_QUANTITY;
            break;
          case "weight":
            type = AnnotationTypes.ANNOTATION_TYPE_MASS;
            subType = null;
            break;
          case "distance":
            type = AnnotationTypes.ANNOTATION_TYPE_DISTANCE;
            subType = null;
            break;
          case "area":
            type = AnnotationTypes.ANNOTATION_TYPE_AREA;
            subType = null;
            break;
          case "volume":
            type = AnnotationTypes.ANNOTATION_TYPE_VOLUME;
            subType = null;
            break;
        }
      }

    } else if (entity instanceof Url) {
      type = AnnotationTypes.ANNOTATION_TYPE_URL;
    } else if (entity instanceof Vehicle) {
      type = AnnotationTypes.ANNOTATION_TYPE_VEHICLE;
    } else if (entity instanceof Vulnerability) {
      type = AnnotationTypes.ANNOTATION_TYPE_VULNERABILITY;
    } else if (entity instanceof Coordinate) {
      type = AnnotationTypes.ANNOTATION_TYPE_COORDINATE;
    } else if (entity instanceof MilitaryPlatform) {
      type = AnnotationTypes.ANNOTATION_TYPE_VEHICLE;
    } else if (entity instanceof Weapon) {
      type = AnnotationTypes.ANNOTATION_TYPE_WEAPON;
    } else if (entity instanceof Location) {
      type = AnnotationTypes.ANNOTATION_TYPE_LOCATION;
    } else if (entity instanceof Temporal) {
      type = AnnotationTypes.ANNOTATION_TYPE_TEMPORAL;
    } else {
      type = PathUtils.join(Constants.TYPE_ENTITY_PREFIX, type);
    }

    return PathUtils.join(sanitiseType(type), sanitiseType(subType));
  }

  private String sanitiseType(String type) {
    if (type == null) {
      return null;
    }

    String trimmed = type.trim();

    if (trimmed.isEmpty()) {
      return null;
    }

    return toCamelCase(trimmed);
  }
}
