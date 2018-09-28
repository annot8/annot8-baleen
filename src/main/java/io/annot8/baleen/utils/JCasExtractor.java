package io.annot8.baleen.utils;

import static io.annot8.baleen.Constants.BALEEN_ID;
import static io.annot8.baleen.Constants.BALEEN_VALUE;
import static io.annot8.baleen.Constants.PREFIX;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.annot8.baleen.Constants;
import io.annot8.common.data.bounds.NoBounds;
import io.annot8.common.data.bounds.SpanBounds;
import io.annot8.core.annotations.Annotation;
import io.annot8.core.annotations.Annotation.Builder;
import io.annot8.core.annotations.Group;
import io.annot8.core.exceptions.Annot8Exception;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.core.stores.AnnotationStore;
import io.annot8.core.stores.GroupStore;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import uk.gov.dstl.baleen.types.language.Dependency;
import uk.gov.dstl.baleen.types.language.Paragraph;
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
import uk.gov.dstl.baleen.types.semantic.ReferenceTarget;
import uk.gov.dstl.baleen.types.semantic.Relation;
import uk.gov.dstl.baleen.types.semantic.Temporal;
import uk.gov.dstl.baleen.types.structure.Structure;
import uk.gov.dstl.baleen.uima.utils.UimaTypesUtils;

public class JCasExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(JCasExtractor.class);
  private static final String ANNOTATION_ERROR = "Unable to create {} annotation";
  private static final String GROUP_ERROR = "Unable to create {} group";

  private final JCas jCas;
  private final AnnotationStore annotations;
  private final GroupStore groups;

  private final Multimap<String, Annotation> referentAnnotations = HashMultimap.create();
  private final Map<String, Annotation> baleenIdToAnnotation = new HashMap<>();
  private final Map<String, Annotation> baleenIdToWordToken = new HashMap<>();
  private final BaleenTypeMapper typeMapper;

  public JCasExtractor(JCas jCas, AnnotationStore annotations, GroupStore groups) {
    this.jCas = jCas;
    this.annotations = annotations;
    this.groups = groups;
    this.typeMapper = new BaleenTypeMapper();
  }


  public void extract() {

    // Structure
    select(Structure.class, this::addStructure);
    select(uk.gov.dstl.baleen.types.language.Text.class, this::addTextBlock);

    // Language
    select(WordToken.class, this::addWordToken);
    select(Sentence.class, this::addSentence);
    select(PhraseChunk.class, this::addPhraseChunk);
    select(Paragraph.class, this::addParagraph);
    select(WordLemma.class, this::addWordLemma);

    // Other baleen
    select(Metadata.class, this::addMetadata);
    select(ProtectiveMarking.class, this::addProtectiveMarking);
    select(PublishedId.class, this::addPublishedId);

    // Entities
    select(Entity.class, this::addEntity);

    // Groups (needs to come after entities for the maps to be populated)
    select(ReferenceTarget.class, this::addCoreferences);
    select(Relation.class, this::addRelations);

    // Dependency is a group in Annot8s
    // Must be after the WordToken
    select(Dependency.class, this::addDependency);

  }

  private <T extends TOP> void select(Class<T> clazz, Consumer<T> consumer) {
    JCasUtil.select(jCas, clazz)
        .forEach(consumer);
  }

  private Annotation.Builder createAnnotation(BaleenAnnotation t) {

    Builder builder = annotations.create()
        .withType(typeMapper.fromBaleenToAnnot8(t).orElse(Constants.PREFIX + ".unknown"))
        .withBounds(new SpanBounds(t.getBegin(), t.getEnd()))
        .withProperty(Constants.BALEEN_ID, t.getExternalId())
        .withProperty(Constants.BALEEN_TYPE, t.getType().getName());

    if (t instanceof Entity) {
      Entity e = (Entity) t;
      builder
          .withProperty(Constants.BALEEN_VALUE, e.getValue());
    }

    return builder;
  }


  private void addWordToken(WordToken t) {
    try {
      Annotation annotation = createAnnotation(t)
          .withProperty("pos", t.getPartOfSpeech())
          .withProperty("sentenceOrder", t.getSentenceOrder())
          .save();

      baleenIdToWordToken.put(t.getExternalId(), annotation);

    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, WordToken.class.getSimpleName(), e);

    }
  }

  private void addSentence(Sentence t) {
    try {
      createAnnotation(t)
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, Sentence.class.getSimpleName(), e);

    }
  }

  private void addPhraseChunk(PhraseChunk t) {
    try {
      Builder builder = createAnnotation(t)
          .withProperty("chunkType", t.getChunkType());
      // TODO: Unclear what to do with head word etc

      if(t.getHeadWord() != null ) {
        builder.withProperty("headWord", t.getHeadWord().getCoveredText());
      }

      builder.save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, PhraseChunk.class.getSimpleName(), e);
    }
  }

  private void addParagraph(Paragraph t) {
    try {
      createAnnotation(t)
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, Paragraph.class.getSimpleName(), e);
    }
  }

  private void addWordLemma(WordLemma t) {
    try {
      createAnnotation(t)
          .withProperty("lemma", t.getLemmaForm())
          .withProperty("pos", t.getPartOfSpeech())
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, WordLemma.class.getSimpleName(), e);
    }
  }

  private void addDependency(Dependency t) {
    try {
      groups.create()
          .withType(Constants.GROUP_DEPENDENCY)
          .withProperty("dependencyType", t.getDependencyType())
          .withAnnotation("governor", baleenIdToWordToken.get(t.getGovernor().getExternalId()))
          .withAnnotation("dependent", baleenIdToWordToken.get(t.getDependent().getExternalId()))
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(GROUP_ERROR, Dependency.class.getSimpleName(), e);
    }
  }

  private void addStructure(Structure m) {
    try {
      createAnnotation(m)
          .withProperty(Constants.STRUCTURAL_DEPTH, m.getDepth())
          .withProperty(Constants.STRUCTURAL_ID, m.getElementId())
          .withProperty(Constants.STRUCTURAL_CLASS, m.getElementClass())
          // TODO: Some structural types have additional info (eg pageNumber, figure reference)
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, Structure.class.getSimpleName(), e);

    }
  }

  private void addPublishedId(PublishedId m) {

    try {
      createAnnotation(m)
          .withProperty("publishedIdType", m.getPublishedIdType())
          .withProperty("publishedId", m.getValue())
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, PublishedId.class.getSimpleName(), e);
    }
  }

  private void addProtectiveMarking(ProtectiveMarking m) {

    try {
      createAnnotation(m)
          .withProperty("caveats", UimaTypesUtils.toList(m.getCaveats()))
          .withProperty("releasability", UimaTypesUtils.toList(m.getReleasability()))
          .withProperty("classification", m.getClassification())
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, ProtectiveMarking.class.getSimpleName(), e);
    }
  }

  private void addMetadata(Metadata m) {
    try {
      annotations.create()
          .withType(typeMapper.fromBaleenToAnnot8(m).orElse(PREFIX + "metadata"))
          .withProperty(Constants.METADATA_KEY, m.getKey())
          .withProperty(Constants.METADATA_VALUE, m.getValue())
          .withBounds(NoBounds.getInstance())
          .save();
    } catch (Annot8Exception e) {
      LOGGER.error(ANNOTATION_ERROR, Metadata.class.getSimpleName(), e);
    }
  }

  private void addTextBlock(uk.gov.dstl.baleen.types.language.Text block) {
    try {
      createAnnotation(block)
          .save();
    } catch (Annot8Exception e) {
      LOGGER
          .error(ANNOTATION_ERROR, uk.gov.dstl.baleen.types.language.Text.class.getSimpleName(), e);
    }
  }

  private void addCoreferences(ReferenceTarget rt) {
    try {

      Group.Builder builder = groups.create()
          .withType(Constants.GROUP_COREFERENCE)
          .withProperty(BALEEN_ID, rt.getExternalId());

      referentAnnotations.get(rt.getExternalId())
          .forEach(a -> builder.withAnnotation("mention", a));

      builder.save();
    } catch (IncompleteException e) {
      LOGGER.error(GROUP_ERROR, ReferenceTarget.class.getSimpleName(), e);
    }
  }

  private void addRelations(Relation r) {
    try {

      Group.Builder builder = groups.create()
          .withType(Constants.GROUP_RELATION)
          .withProperty(BALEEN_ID, r.getExternalId())
          .withProperty(BALEEN_VALUE, r.getValue())
          .withProperty("relationshipType", r.getRelationshipType())
          .withProperty("relationshipSubtype", r.getRelationSubType())
          .withProperty("sentenceDistance", r.getSentenceDistance())
          .withProperty("dependencyDistance", r.getDependencyDistance())
          .withProperty("wordDistance", r.getWordDistance())
          .withAnnotation("from", baleenIdToAnnotation.get(r.getSource().getExternalId()))
          .withAnnotation("to", baleenIdToAnnotation.get(r.getTarget().getExternalId()));

      builder.save();
    } catch (IncompleteException e) {
      LOGGER.error(GROUP_ERROR, Relation.class.getSimpleName(), e);
    }
  }

  private void addEntity(Entity entity) {

    try {

      Builder builder = createAnnotation(entity);

      if (entity instanceof Person) {
        Person e = (Person) entity;

        builder
            .withProperty("gender", e.getGender())
            .withProperty("title", e.getTitle());

      } else if (entity instanceof Location) {
        Location e = (Location) entity;
        builder
            .withProperty("geoJson", e.getGeoJson());

      } else if (entity instanceof Organisation) {
        // No props
      } else if (entity instanceof Vehicle) {
        Vehicle e = (Vehicle) entity;
        builder
            .withProperty("vehicleId", e.getVehicleIdentifier());
      } else if (entity instanceof Temporal) {
        Temporal e = (Temporal) entity;
        builder
            .withProperty("precision", e.getPrecision())
            .withProperty("scope", e.getScope())
            .withProperty("temporalType", e.getTemporalType())
            .withProperty("temporalStart", e.getTimestampStart())
            .withProperty("temporalEnd", e.getTimestampStop());
      } else if (entity instanceof Buzzword) {
        Buzzword e = (Buzzword) entity;
        builder
            .withProperty("tags", UimaTypesUtils.toList(e.getTags()));
      } else if (entity instanceof Chemical) {
        // No props
      } else if (entity instanceof CommsIdentifier) {
        // No props
      } else if (entity instanceof DocumentReference) {
        // No props
      } else if (entity instanceof Frequency) {
        // No props
      } else if (entity instanceof MilitaryPlatform) {
        // No props
      } else if (entity instanceof Money) {
        Money e = (Money) entity;
        builder
            .withProperty("amount", e.getAmount())
            .withProperty("currency", e.getCurrency());
      } else if (entity instanceof Nationality) {
        Nationality e = (Nationality) entity;
        builder
            .withProperty("countryCode", e.getCountryCode());
      } else if (entity instanceof Quantity) {
        Quantity e = (Quantity) entity;
        builder
            .withProperty("normalizedQuantity", e.getNormalizedQuantity())
            .withProperty("normalizedUnit", e.getNormalizedUnit())
            .withProperty("quantity", e.getQuantity())
            .withProperty("unit", e.getUnit());
      } else if (entity instanceof Url) {
        // No props
      } else if (entity instanceof Weapon) {
        // No props
      }

      Annotation annotation = builder.save();

      if (entity.getReferent() != null) {
        referentAnnotations.put(entity.getReferent().getExternalId(), annotation);
      }
      baleenIdToAnnotation.put(entity.getExternalId(), annotation);

    } catch (IncompleteException e) {
      LOGGER.error(ANNOTATION_ERROR, e.getClass().getSimpleName(), e);
    }
  }
}
