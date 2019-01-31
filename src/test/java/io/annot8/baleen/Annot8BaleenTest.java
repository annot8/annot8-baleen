/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.annot8.baleen.annotators.BaleenAnnotatorSettings;
import io.annot8.baleen.annotators.BaleenAnnotators;
import io.annot8.baleen.blocks.TextBlockToContent;
import io.annot8.baleen.reader.BaleenCollectionReader;
import io.annot8.common.data.content.Text;
import io.annot8.common.pipelines.elements.Pipeline;
import io.annot8.common.pipelines.elements.PipelineBuilder;
import io.annot8.common.pipelines.queues.MemoryItemQueue;
import io.annot8.common.pipelines.simple.SimplePipeBuilder;
import io.annot8.common.pipelines.simple.SimplePipelineBuilder;
import io.annot8.components.files.sources.FileSystemSource;
import io.annot8.components.files.sources.FileSystemSourceSettings;
import io.annot8.core.data.Item;
import io.annot8.core.exceptions.BadConfigurationException;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.core.exceptions.MissingResourceException;
import io.annot8.testing.testimpl.TestBaseItemFactory;
import io.annot8.testing.testimpl.TestContext;
import io.annot8.testing.testimpl.components.ItemCollector;

public class Annot8BaleenTest {

  @Test
  public void test()
      throws IncompleteException, BadConfigurationException, MissingResourceException {

    ItemCollector collector = new ItemCollector();

    PipelineBuilder spb = new SimplePipelineBuilder();

    FileSystemSourceSettings settings = new FileSystemSourceSettings(new File("./data").toPath());
    settings.setWatching(false);

    Pipeline pipeline =
        spb.addSource(new FileSystemSource(), settings)
            .addPipe(
                new SimplePipeBuilder()
                    .addProcessor(new BaleenCollectionReader())
                    .addProcessor(new TextBlockToContent())
                    .addProcessor(new BaleenAnnotators(), new BaleenAnnotatorSettings())
                    .addProcessor(collector))
            .withItemFactory(new TestBaseItemFactory())
            .withQueue(new MemoryItemQueue())
            .build();

    pipeline.configure(new TestContext());
    pipeline.run();

    List<Item> items = collector.getItems();

    assertThat(items).hasSize(1);

    Item item = items.get(0);

    Text text = item.getContents(Text.class).findFirst().get();

    assertThat(text.getAnnotations().getAll()).anyMatch(a -> a.getType().equals("entity/email"));
  }
}
