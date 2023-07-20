package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.componentfactory.epubviewer.bookloaders.book.Book;
import com.vaadin.componentfactory.epubviewer.bookloaders.loader.BookLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class BookReaderView extends Div implements BookReader {
    private int currentPage = 1;
    private HorizontalLayout controlsLayout = new HorizontalLayout();
    private Html viewerFrame = new Html("<div class=ReaderViewFrame'>Loading your book...</div>");
    private Span pageIndicator = new Span();
    private Book book;
    protected BookLoader bookLoader;
    protected BookReaderView() {
        // functional stuff
        add(viewerFrame);

        Button prevPageButton = new Button("<");
        Button nextPageButton = new Button(">");

        prevPageButton.addClickListener(e -> prevPage());
        controlsLayout.add(prevPageButton);

        pageIndicator.setText(String.valueOf("1 / 1"));
        controlsLayout.add(pageIndicator);

        nextPageButton.addClickListener(e -> nextPage());
        controlsLayout.add(nextPageButton);
        add(controlsLayout);
    }

    // region Page flip notifier event system
    private List<ReaderPageListener> pageListeners = new ArrayList<>();
    public void addReaderPageListener(ReaderPageListener listener) {
        pageListeners.add(listener);
    }

    protected void redrawView() {
        if (book == null) {
            replaceText("Book could not be loaded.");
            controlsLayout.setEnabled(false);
        } else {
            viewerFrame.setHtmlContent("<div class=ReaderViewFrame'>" + book.getPage(currentPage).getHtml() + "</div>");
            pageIndicator.setText(String.valueOf(currentPage + " / " + book.getLastPageNumber()));
            controlsLayout.setEnabled(true);
        }
    }
    // endregion

    private void replaceText(String text) {
        viewerFrame.setHtmlContent(
                "<div class=ReaderViewFrame'>" + text + "</div>"
        );
    }
    @Override
    public void gotoPage(int number) throws IndexOutOfBoundsException{
        if (number <= 0 || book.getLastPageNumber() < number) {
            throw new IndexOutOfBoundsException("Page with number " + number + " does not exist.\n" +
                    "Can only go to (1 .. " + book.getLastPageNumber() + ")");
        } else {
            currentPage = number;
            for (ReaderPageListener listener : pageListeners) {
                listener.pageNotifyListener(number);
            }
            redrawView();
        }
    }

    @Override
    public void nextPage() {
        if(currentPage < book.getLastPageNumber()) {
            gotoPage(currentPage + 1);
        }
    }

    @Override
    public void prevPage() {
        if(currentPage > 1) {
            gotoPage(currentPage - 1);
        }
    }

    void loadBook(Book book) {
        this.book = book;
        currentPage = 1;
        redrawView();
    }

    @Override
    public void loadBook(InputStream inputStream) throws IOException {
        Book newBook = null;
        try {
            newBook = bookLoader.loadBook(inputStream);

            // removing empty book
            if(newBook != null && newBook.getLastPageNumber() == 0) {
                newBook = null;
            }
        } finally {
            this.loadBook(newBook);
        }
    }

    @Override
    public void loadBook(File file) throws IOException {
        InputStream bookStream = new FileInputStream(file);
        loadBook(bookStream);
        bookStream.close();
    }
}
