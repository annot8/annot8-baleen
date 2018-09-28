/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.utils;

import static io.annot8.conventions.PropertyKeys.PROPERTY_KEY_SOURCE;

import java.io.InputStream;
import java.util.function.Consumer;

import org.apache.uima.jcas.JCas;

import uk.gov.dstl.baleen.exceptions.BaleenException;

import io.annot8.components.base.components.AbstractComponent;
import io.annot8.core.components.Processor;
import io.annot8.core.components.responses.ProcessorResponse;
import io.annot8.core.context.Context;
import io.annot8.core.data.Content;
import io.annot8.core.data.Item;
import io.annot8.core.exceptions.Annot8Exception;
import io.annot8.core.exceptions.BadConfigurationException;

import io.committed.baleen.embedded.ConsumerOutputConverter;
import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.EmbeddedBaleenFactory;

public abstract class AbstractBaleenProcessor extends AbstractComponent implements Processor {

  private static final String DEFAULT_SOURCE = "annot8";
  private static final int DEFAULT_POOL_SIZE = 1;

  private EmbeddableBaleen baleen;

  @Override
  public void configure(Context context) throws BadConfigurationException {

    try {
      baleen =
          EmbeddedBaleenFactory.createAndSetup(
              this.getClass().getSimpleName(), getYaml(context), getPoolSize(context));
    } catch (BaleenException e) {
      throw new BadConfigurationException("Baleen can not be configured", e);
    }
  }

  @Override
  public void close() {
    if (baleen != null) {
      baleen.shutdown();
      baleen = null;
    }
  }

  protected abstract String getYaml(Context context);

  protected int getPoolSize(Context context) {
    return DEFAULT_POOL_SIZE;
  }

  @Override
  public ProcessorResponse process(Item item) throws Annot8Exception {
    if (baleen == null) {
      return ProcessorResponse.processingError();
    }

    try {
      processItem(item);
      return ProcessorResponse.ok();
    } catch (BaleenException e) {
      log().error("Unable to process item with Baleen", e);
      return ProcessorResponse.itemError();
    }
  }

  protected abstract void processItem(Item item) throws BaleenException, Annot8Exception;

  protected void processWithBaleen(
      Content<?> content, InputStream is, Consumer<JCas> annotatorCreator, Consumer<JCas> consumer)
      throws BaleenException {
    String source = getSource(content);
    baleen.process(source, is, annotatorCreator, new ConsumerOutputConverter(consumer));
  }

  protected void processWithBaleen(Content<?> content, InputStream is, Consumer<JCas> consumer)
      throws BaleenException {
    String source = getSource(content);
    baleen.process(source, is, new ConsumerOutputConverter(consumer));
  }

  private String getSource(Content<?> content) {
    // We don't have the same notion of source as Baleen... so we just make sure we have
    // something non-null

    return content.getProperties().getOrDefault(PROPERTY_KEY_SOURCE, DEFAULT_SOURCE);
  }
}
