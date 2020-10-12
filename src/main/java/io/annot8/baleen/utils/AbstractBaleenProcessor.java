/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import static io.annot8.conventions.PropertyKeys.PROPERTY_KEY_SOURCE;

import io.annot8.api.components.responses.ProcessorResponse;
import io.annot8.api.data.Content;
import io.annot8.api.data.Item;
import io.annot8.api.exceptions.BadConfigurationException;
import io.annot8.common.components.AbstractProcessor;
import io.committed.baleen.embedded.ConsumerOutputConverter;
import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.EmbeddedBaleenFactory;
import java.io.InputStream;
import java.util.function.Consumer;
import org.apache.uima.jcas.JCas;
import uk.gov.dstl.baleen.exceptions.BaleenException;

public abstract class AbstractBaleenProcessor extends AbstractProcessor {

  private static final String DEFAULT_SOURCE = "annot8";

  private EmbeddableBaleen baleen;
  private BaleenSettings settings;

  public AbstractBaleenProcessor(BaleenSettings settings) {
    this.settings = settings;
  }

  protected EmbeddableBaleen getBaleen() {
    if (baleen == null) {
      try {
        baleen =
            EmbeddedBaleenFactory.createAndSetup(
                this.getClass().getSimpleName(), settings.getYaml(), settings.getPoolSize());
      } catch (BaleenException e) {
        throw new BadConfigurationException("Baleen can not be configured", e);
      }
    }

    return baleen;
  }

  @Override
  public void close() {
    if (baleen != null) {
      baleen.shutdown();
      baleen = null;
    }
  }

  @Override
  public ProcessorResponse process(Item item) {
    try {
      processItem(item);
      return ProcessorResponse.ok();
    } catch (BaleenException e) {
      log().error("Unable to process item with Baleen", e);
      return ProcessorResponse.itemError();
    }
  }

  protected abstract void processItem(Item item) throws BaleenException;

  protected void processWithBaleen(
      Content<?> content, InputStream is, Consumer<JCas> annotatorCreator, Consumer<JCas> consumer)
      throws BaleenException {
    String source = getSource(content);
    getBaleen().process(source, is, annotatorCreator, new ConsumerOutputConverter(consumer));
  }

  protected void processWithBaleen(Content<?> content, InputStream is, Consumer<JCas> consumer)
      throws BaleenException {
    String source = getSource(content);
    getBaleen().process(source, is, new ConsumerOutputConverter(consumer));
  }

  private String getSource(Content<?> content) {
    // We don't have the same notion of source as Baleen... so we just make sure we have
    // something non-null

    return content.getProperties().getOrDefault(PROPERTY_KEY_SOURCE, DEFAULT_SOURCE);
  }
}
