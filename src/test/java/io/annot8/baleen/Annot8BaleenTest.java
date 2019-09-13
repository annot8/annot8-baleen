/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen;

public class Annot8BaleenTest {

//  @Test
//  public void test() {
//
//    ItemCollector collector = new ItemCollector();
//
//    PipelineBuilder spb = new SimplePipelineBuilder();
//
//    FileSystemSourceSettings settings = new FileSystemSourceSettings(new File("./data").toPath());
//    settings.setWatching(false);
//
//    Pipeline pipeline =
//        spb.addSource(new FileSystemSource(), settings)
//            .addPipe(
//                new SimplePipeBuilder()
//                    .addProcessor(new BaleenCollectionReader())
//                    .addProcessor(new TextBlockToContent())
//                    .addProcessor(new BaleenAnnotators(), new BaleenAnnotatorSettings())
//                    .addProcessor(collector))
//            .withItemFactory(new TestBaseItemFactory())
//            .withQueue(new MemoryItemQueue())
//            .build();
//
//    pipeline.configure(new TestContext());
//    pipeline.run();
//
//    List<Item> items = collector.getItems();
//
//    assertThat(items).hasSize(1);
//
//    Item item = items.get(0);
//
//    Text text = item.getContents(Text.class).findFirst().get();
//
//    assertThat(text.getAnnotations().getAll()).anyMatch(a -> a.getType().equals("entity/email"));
//  }
}
