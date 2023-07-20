package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.BookFormatEnum;
import com.vaadin.componentfactory.epubviewer.loaders.EpubLoader;
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
    BookLoader bookLoader;
    private int currentPage = 1;
    private HorizontalLayout controlsLayout = new HorizontalLayout();
    private Html viewerFrame = new Html("<div class=ReaderViewFrame'>Loading your book...</div>");
    private Span pageIndicator = new Span();
    private InputStream bookSource;

    public BookViewer() {
        add(viewerFrame);

        // Controls
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

    // region Page flip notifier event system
    private List<ViewerPageListener> pageListeners = new ArrayList<>();
    public void addReaderPageListener(ViewerPageListener listener) {
        pageListeners.add(listener);
    }
    // endregion
    public void gotoPage(int number) throws IndexOutOfBoundsException{
        try {
            currentPage = number;
            showPage(bookLoader.getPage(currentPage));
            controlsLayout.setEnabled(true);
        } catch (IndexOutOfBoundsException e) {
            showMessage("Book could not be loaded.");
            controlsLayout.setEnabled(false);
        }
    }

    public void gotoNextPage(){
        if(currentPage < bookLoader.getPagesCount()) {
            gotoPage(currentPage + 1);
        }
    }

    public void gotoPrevPage() {
        if(currentPage > 1) {
            gotoPage(currentPage - 1);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
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
     * Prepa
     * @param bookSource shouldn't be closed until adding component
     * @param bookFormat if specified incorrectly will throw exception or return garbage
     */
    public void setBookSource(InputStream bookSource, BookFormatEnum bookFormat) {
        // TODO: add book format check
        switch (bookFormat) {
            case EPUB:
                bookLoader = new EpubLoader();
                break;
        }
        this.bookSource = bookSource;
    }

    void showPage(String htmlPage) {
        viewerFrame.setHtmlContent("<div class=ReaderViewFrame'>" + htmlPage + "</div>");
        pageIndicator.setText(String.valueOf(currentPage + " / " + bookLoader.getPagesCount()));
    }

    void showMessage(String text) {
        viewerFrame.setHtmlContent(
                "<div class=ReaderViewFrame'>" + text + "</div>"
        );
    }
}
