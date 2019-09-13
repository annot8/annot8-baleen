package io.annot8.baleen.annotators;

import io.annot8.api.capabilities.Capabilities;
import io.annot8.api.context.Context;
import io.annot8.baleen.utils.BaleenSettings;
import io.annot8.common.components.AbstractProcessorDescriptor;
import io.annot8.common.components.capabilities.SimpleCapabilities;
import io.annot8.common.data.bounds.SpanBounds;
import io.annot8.common.data.content.Text;

public class BaleenAnnotator extends AbstractProcessorDescriptor<BaleenAnnotators, BaleenSettings> {


    @Override
    public Capabilities capabilities() {
        return new SimpleCapabilities.Builder()
                .withProcessesContent(Text.class)
                .withCreatesAnnotations("*", SpanBounds.class)
                .build();
    }

    @Override
    protected BaleenAnnotators createComponent(Context context, BaleenSettings settings) {
        return new BaleenAnnotators(settings);
    }
}
