# annot8-baleen

Wrap [Baleen](http://github.com/dstl/baleen) to run inside Annot8.

This is proof of concept - whilst it should work functionally using the output might be tricky!

## Usage

You can use this in two ways: 

* Use `BaleenCollectionReader` to convert an Item's `FileContent` into `Text`. This will run through the Baleen's Structured Content extractors producing plain text, metadata, and structural annotations.
* Use `TextBlocksToContent` to create individual Annot8 text content for each annotation TextBlock.
* Use `BaleenAnnotators` to process Annot8 Text content through a Baleen annotators pipeline (providing your own YAML).

See the `Annot8BaleenTest` test for an example pipeline.



