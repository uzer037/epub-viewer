package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.views.enums.BookFormatEnum;
import com.vaadin.componentfactory.epubviewer.loaders.epub.EpubLoader;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.componentfactory.epubviewer.loaders.BookLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookViewer extends Div {
    private BookLoader bookLoader;
    private int currentPageNumber = 1;
    private boolean showControls = true;
    private HorizontalLayout controlsLayout = new HorizontalLayout();
    private Html viewerFrame = new Html(wrapHtmlWithViewDiv("Loading your book..."));
    private Span pageIndicator = new Span();
    private InputStream bookSource;
    private final List<ViewerPageListener> pageListeners = new ArrayList<>();

    public BookViewer() {
        add(viewerFrame);

        Button prevPageButton = new Button("<");
        Button nextPageButton = new Button(">");

        prevPageButton.addClickListener(e -> gotoPrevPage());
        controlsLayout.add(prevPageButton);

        pageIndicator.setText(String.valueOf("1 / 1"));
        controlsLayout.add(pageIndicator);

        nextPageButton.addClickListener(e -> gotoNextPage());
        controlsLayout.add(nextPageButton);
        add(controlsLayout);
    }


    /**
     * Adds event listener to notify when page was flipped
     * @param listener
     */
    public void addReaderPageListener(ViewerPageListener listener) {
        pageListeners.add(listener);
    }

    /**
     * Tries to go to page with given number and render it in component's view
     * @param number integer from 1 to getPagesCount()
     * @throws IndexOutOfBoundsException if page with given number does not exist
     */
    public void gotoPage(int number) {
        controlsLayout.setEnabled(false);
        currentPageNumber = number;
        showPage(bookLoader.getPage(currentPageNumber));
        pageListeners.stream().forEach(listener->listener.onPageChanged(currentPageNumber));
        if (showControls)
        {
            controlsLayout.setEnabled(true);
        }
    }

    /**
     * Whether to show component's built-in controls
     * @param visible
     */
    public void setShowControls(boolean visible) {
        showControls = visible;
        controlsLayout.setEnabled(showControls);
        controlsLayout.setVisible(showControls);
    }

    /**
     * Tries switch to next page. If there is none - does nothing.
     */
    public void gotoNextPage() {
        if(currentPageNumber < bookLoader.getPagesCount()) {
            gotoPage(currentPageNumber + 1);
        }
    }

    /**
     * Tries switch to previous page. If there is none - does nothing.
     */
    public void gotoPrevPage() {
        if(currentPageNumber > 1) {
            gotoPage(currentPageNumber - 1);
        }
    }

    /**
     * @return current page number (from 1 to lastPageNumber)
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * @return total pages count in current book. If book has no pages or was not loaded will return 0.
     */
    public int getPagesCount() {
        return bookLoader.getPagesCount();
    }

    /**
     * Loads book from source, specified with setBookSource()
     * Automatically called on attaching component (onAttach event)
     * but could be manually called again to load new books
     */
    public void loadBook() {
        if (bookLoader == null) {
            showMessage("Was not given any book.");
        } else {
            try {
                bookLoader.loadFromSource(this.bookSource);
                gotoPage(1);
            } catch (IOException e) {
                showMessage("Requested book is not found.");
            }
        }
    }

    /**
     * Called when component is attached and loads book from source specified with setBookSource().
     * If no book was loaded, displays error message in component's view.
     * @param attachEvent as described in Vaadin docs
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadBook();
    }

    /**
     * Prepa
     * @param bookSource shouldn't be closed until adding component
     * @param bookFormat if specified incorrectly will throw exception or return garbage
     */
    public void setBookSource(InputStream bookSource, BookFormatEnum bookFormat) {
        switch (bookFormat) {
            case EPUB:
                bookLoader = new EpubLoader();
                break;
        }
        this.bookSource = bookSource;
    }

    void showPage(String htmlPage) {
        viewerFrame.setHtmlContent(wrapHtmlWithViewDiv(htmlPage));
        pageIndicator.setText(String.valueOf(currentPageNumber + " / " + bookLoader.getPagesCount()));
    }

    void showMessage(String text) {
        viewerFrame.setHtmlContent(wrapHtmlWithViewDiv(text));
    }

    /**
     * Vaadin's Html component requires root element to always be the same,
     * so for this class to work it has to be wrapped with some element which always stays the same.
     * Using raw html with Vaadin is usually bad idea, but it is what required in this specific case
     * so better using single function for this everywhere rather wrapping html by hand and risking typos
     * @param html
     * @return html wrapped with div
     */
    private String wrapHtmlWithViewDiv(String html) {
        return "<div class='ReaderViewFrame'>" + html + "</div>";
    }
}
