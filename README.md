# Vaadin epub viewer plugin
## What is this
Vaadin epub book viewer plugin, that ensures same page slicing between sessions for easier indexation.

Main class is `EpubReaderView` which is derived from Vaadin's `HorizontalLayout`.

It implements `BookReader` interface which might be used to control flipping pages with custom controls or events

# Content
- [What is this](#what-is-this)
- [Usage example](#usage-example)
- [Core dependencies](#core-dependencies)

## Usage example:
```java
// your root view or component
VerticalLayout layout = new VerticalLayout();

EpubReaderView readerView = new EpubReaderView();
File bookFile = new File("/path/to/book.epub");

// Alternatively, book could be loaded as InputStream or directly as Book object

// if book doesn't exist or unaccessible will throw IOException
try {
    readerView.loadBook(bookFile);
} catch (IOException) {
    // your exception handling
}

layout.add(readerView);
```

## Core dependencies
- vaadin-core
- epublib-core (from [nl.siegmann.epublib](https://github.com/psiegman/epublib))
- jsoup