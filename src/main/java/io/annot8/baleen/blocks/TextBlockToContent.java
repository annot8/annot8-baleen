/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.baleen.blocks;

import io.annot8.api.annotations.Annotation;
import io.annot8.api.components.responses.ProcessorResponse;
import io.annot8.api.data.Item;
import io.annot8.api.stores.AnnotationStore;
import io.annot8.baleen.Constants;
import io.annot8.common.components.AbstractProcessor;
import io.annot8.common.data.bounds.SpanBounds;
import io.annot8.common.data.content.Text;
import java.util.Optional;

public class TextBlockToContent extends AbstractProcessor {

  @Override
  public ProcessorResponse process(Item item) {
    item.getContents(Text.class).forEach(t -> process(item, t));

    return ProcessorResponse.ok();
  }

  private void process(Item item, Text text) {
    text.getAnnotations()
        .getByType(Constants.TYPE_LANGUAGE_TEXT)
        .forEach(b -> process(item, text, b));
  }

  private void process(Item item, Text text, Annotation block) {

    Optional<SpanBounds> blockBounds = block.getBounds(SpanBounds.class);

    // Cant do anything without bounds.
    if (!blockBounds.isPresent()) {
      return;
    }

    Optional<String> dataInBlock = text.getText(block);

    // Cant do anything is we have no data
    if (!dataInBlock.isPresent()) {
      return;
    }

    SpanBounds bounds = blockBounds.get();

    Text subText =
        item.createContent(Text.class)
            .withDescription(makeDescription(text, bounds))
            .withProperty(Constants.BLOCK_BEGIN, bounds.getBegin())
            .withProperty(Constants.BLOCK_END, bounds.getEnd())
            .withData(dataInBlock.get())
            .save();

    // Move the annotations from the old block to this
    moveAnnotations(text, bounds, subText);

    // Now delete the block, we'll process the subText from on
    text.getAnnotations().delete(block);
  }

  private void moveAnnotations(Text source, SpanBounds targetBounds, Text target) {
    AnnotationStore targetAnnotations = target.getAnnotations();

    source
        .getBetween(targetBounds.getBegin(), targetBounds.getEnd())
        // Don't copy the text blocks over
        .filter(a -> !a.getType().equals(Constants.TYPE_LANGUAGE_TEXT))
        .forEach(
            a -> {
              // Adjust the bounds
              SpanBounds boundsInSource = a.getBounds(SpanBounds.class).get();
              SpanBounds boundsInTarget =
                  new SpanBounds(
                      Math.max(0, boundsInSource.getBegin() - targetBounds.getBegin()),
                      Math.min(
                          targetBounds.getLength(),
                          boundsInSource.getEnd() - targetBounds.getBegin()));

              // Add the annotation to the target content
              targetAnnotations.copy(a).withBounds(boundsInTarget).save();
            });
  }

  private String makeDescription(Text text, SpanBounds bounds) {
    return String.format("%s-block[%d,%d]", text.getId(), bounds.getBegin(), bounds.getEnd());
  }
}
