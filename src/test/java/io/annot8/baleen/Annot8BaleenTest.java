/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

import static org.assertj.core.api.Assertions.assertThat;

import io.annot8.api.annotations.Annotation;
import io.annot8.api.capabilities.Capabilities;
import io.annot8.api.components.*;
import io.annot8.api.context.Context;
import io.annot8.api.data.Item;
import io.annot8.api.settings.NoSettings;
import io.annot8.baleen.annotators.BaleenAnnotator;
import io.annot8.baleen.blocks.TextBlockToContent;
import io.annot8.baleen.reader.BaleenCollectionReader;
import io.annot8.baleen.utils.BaleenSettings;
import io.annot8.common.components.AbstractAnnot8ComponentDescriptor;
import io.annot8.common.components.capabilities.SimpleCapabilities;
import io.annot8.common.data.content.Text;
import io.annot8.components.files.sources.FileSystemSource;
import io.annot8.components.files.sources.FileSystemSourceSettings;
import io.annot8.implementations.pipeline.InMemoryPipelineRunner;
import io.annot8.implementations.pipeline.SimplePipelineDescriptor;
import io.annot8.testing.testimpl.TestItemFactory;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class Annot8BaleenTest {

  public static class PassthroughDescriptor<T extends Annot8Component>
      extends AbstractAnnot8ComponentDescriptor<T, NoSettings> {

    private final T instance;

    public PassthroughDescriptor(T instance) {
      this.instance = instance;
      setName(instance.getClass().getSimpleName());
      setSettings(NoSettings.getInstance());
    }

    @Override
    public T create(Context context) {
      return instance;
    }

    @Override
    public Capabilities capabilities() {
      return new SimpleCapabilities.Builder().build();
    }
  }

  public static class PassthroughSourceDescriptor<T extends Source> extends PassthroughDescriptor<T>
      implements SourceDescriptor<T, NoSettings> {

    public PassthroughSourceDescriptor(T instance) {
      super(instance);
    }
  }

  public static class PassthroughProcessorDescriptor<T extends Processor>
      extends PassthroughDescriptor<T> implements ProcessorDescriptor<T, NoSettings> {

    public PassthroughProcessorDescriptor(T instance) {
      super(instance);
    }
  }

  @Test
  public void test() {
    FileSystemSourceSettings settings = new FileSystemSourceSettings(new File("./data").toPath());
    settings.setWatching(false);

    SimplePipelineDescriptor spd =
        new SimplePipelineDescriptor.Builder()
            .withName("Test")
            .withSource(new PassthroughSourceDescriptor<>(new FileSystemSource.Source(settings)))
            .withProcessor(new PassthroughProcessorDescriptor(new BaleenCollectionReader()))
            .withProcessor(new PassthroughProcessorDescriptor(new TextBlockToContent()))
            .withProcessor(new BaleenAnnotator(new BaleenSettings("annotators:\n  - regex.Email")))
            .withDescription("Test")
            .build();

    TestItemFactory itemFactory = new TestItemFactory();
    InMemoryPipelineRunner runner = new InMemoryPipelineRunner(spd, itemFactory);
    runner.run();

    List<Item> items = itemFactory.getCreatedItems();

    assertThat(items).hasSize(1);

    Item item = items.get(0);

    Text text = item.getContents(Text.class).findFirst().get();

    List<Annotation> annotations = text.getAnnotations().getAll().collect(Collectors.toList());

    assertThat(annotations).anyMatch(a -> a.getType().equals("entity/email"));
  }
}
