# Vaadin epub viewer plugin
## What is this
Vaadin epub book viewer plugin, that ensures same page slicing between sessions for easier indexation.

Main class is `EpubReaderView` which is derived from Vaadin's `HorizontalLayout`.

It implements `BookReader` interface which might be used to control flipping pages with custom controls or events

# Content
- [What is this](#what-is-this)
- [Usage example](#usage-example)
  - [Basic example](#basic-example)
  - [Page flip event listener example](#onpagelistener-usage)
- [Core dependencies](#core-dependencies)

## Usage examples
### Basic example:
```java
// your root view or component
VerticalLayout layout = new VerticalLayout();

BookViewer readerView = new BookViewer();
File bookFile = new File("/path/to/book.epub");
// make sure that file exists and is in fact epub
// ...

// Sets book source, but doesn't load from it to conserve resources
// Currently supports only Epub book format
InputStream targetStream = new FileInputStream(initialFile);
readerView.setBookSource(bookStream, BookFormatEnum.EPUB);

// Book will be loaded only when component is added somewhere
layout.add(readerView);

// other code
// ...
        
// close stream only after component is added to page
bookStream.close();
```


### Page flip event listener example:
```java
class MyClass extends VerticalLayout implements ViewerPageListener {
    private BookViewer epubViewer = new BookViewer();
    private Span pageNumberIndicator = new Span();
    
    public MyClass() {
        // registering listener
        epubViewer.addReaderPageListener(this::onPageChanged);
        
        // attaching component
        add(epubViewer);
        
        // rest of constructor
        // ...
    }
    
    @Override
    public void onPageChanged(int pageNumber) {
        // your code here
        pageNumberIndicator.setText("Page number: " + pageNumber);
    }
}
```

## Core dependencies
- vaadin-core
- epublib-core (from [nl.siegmann.epublib](https://github.com/psiegman/epublib))
- jsoup