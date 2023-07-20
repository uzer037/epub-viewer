package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.bookloaders.book.Book;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface BookReader {
    public void gotoPage(int number);

    /** Flips to the next page. If on last page - does nothing */
    public void nextPage();

    /** Flips to the previous page. If on first page - does nothing */
    public void prevPage();
    public void loadBook(File file) throws IOException;
    public void loadBook(InputStream inputStream) throws IOException;
}
