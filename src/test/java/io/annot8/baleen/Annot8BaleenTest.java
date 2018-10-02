/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

import static org.assertj.core.api.Assertions.assertThat;

import io.annot8.common.implementations.pipelines.runnable.RunnablePipeline;
import io.annot8.common.implementations.pipelines.runnable.RunnablePipelineBuilder;
import io.annot8.common.implementations.pipelines.runnable.SimpleRunnablePipelineBuilder;
import io.annot8.core.exceptions.BadConfigurationException;
import io.annot8.core.exceptions.MissingResourceException;
import io.annot8.testing.testimpl.TestContext;
import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.annot8.baleen.annotators.BaleenAnnotatorSettings;
import io.annot8.baleen.annotators.BaleenAnnotators;
import io.annot8.baleen.blocks.TextBlockToContent;
import io.annot8.baleen.reader.BaleenCollectionReader;
import io.annot8.common.data.content.Text;
import io.annot8.common.implementations.pipelines.Pipeline;
import io.annot8.components.files.sources.FileSystemSource;
import io.annot8.components.files.sources.FileSystemSourceSettings;
import io.annot8.core.data.Item;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.testing.testimpl.components.ItemCollector;

public class Annot8BaleenTest {

  @Test
  public void test() throws IncompleteException, BadConfigurationException, MissingResourceException {

    ItemCollector collector = new ItemCollector();

    RunnablePipelineBuilder spb = new SimpleRunnablePipelineBuilder();

    FileSystemSourceSettings settings = new FileSystemSourceSettings(new File("./data").toPath());
    settings.setWatching(false);

    RunnablePipeline pipeline =
        spb
            .addSource(new FileSystemSource(), settings)
            .addProcessor(new BaleenCollectionReader())
            .addProcessor(new TextBlockToContent())
            .addProcessor(new BaleenAnnotators(), new BaleenAnnotatorSettings())
            .addProcessor(collector)
            .build();


    pipeline.configure(new TestContext());
    pipeline.run();

    List<Item> items = collector.getItems();

    assertThat(items).hasSize(1);

    Item item = items.get(0);

    Text text = item.getContents(Text.class).findFirst().get();

    assertThat(text.getAnnotations().getAll())
        .anyMatch(a -> a.getType().equals("baleen.entity.commsIdentifier"));
  }
}
