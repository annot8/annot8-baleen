package io.annot8.baleen;

import static org.assertj.core.api.Assertions.assertThat;

import io.annot8.baleen.annotators.BaleenAnnotatorSettings;
import io.annot8.baleen.annotators.BaleenAnnotators;
import io.annot8.baleen.blocks.TextBlockToContent;
import io.annot8.baleen.reader.BaleenCollectionReader;
import io.annot8.common.data.content.Text;
import io.annot8.common.implementations.pipelines.Pipeline;
import io.annot8.common.implementations.pipelines.SimplePipelineBuilder;
import io.annot8.components.files.sources.FileSystemSource;
import io.annot8.components.files.sources.FileSystemSourceSettings;
import io.annot8.core.data.Item;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.testing.testimpl.TestItemFactory;
import io.annot8.testing.testimpl.components.ItemCollector;
import java.io.File;
import java.util.List;
import org.junit.Test;

public class BaleenIT {

  @Test
  public void test() throws IncompleteException {

    ItemCollector collector = new ItemCollector();

    SimplePipelineBuilder spb = new SimplePipelineBuilder();

    FileSystemSourceSettings settings = new FileSystemSourceSettings(
        new File("./data").toPath());
    settings.setWatching(false);

    Pipeline pipeline = new SimplePipelineBuilder()
        .withItemFactory(new TestItemFactory())
        .addSource(new FileSystemSource(), settings)
        .addProcessor(new BaleenCollectionReader())
        .addProcessor(new TextBlockToContent())
        .addProcessor(new BaleenAnnotators(), new BaleenAnnotatorSettings())
        .addProcessor(collector)
        .build();

    pipeline.run();

    List<Item> items = collector.getItems();

    assertThat(items).hasSize(1);

    Item item = items.get(0);

    Text text = item.getContents(Text.class).findFirst().get();

    assertThat(text.getAnnotations().getAll())
        .anyMatch(a -> a.getType().equals("baleen.entity.commsIdentifier"));
  }

}
