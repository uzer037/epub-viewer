package com.vaadin.componentfactory.epubviewer.loaders;

import com.vaadin.flow.server.NoInputStreamException;

import java.io.IOException;
import java.io.InputStream;

public interface BookLoader {
    // Planning Epub, fb2 and pdf support, else is optional

    /**
     * Loads book data from stream provided
     * @param stream book file stream
     * @throws IOException if InputStream or underlying file does not exist or inaccessible
     */
    void loadFromSource(InputStream stream) throws IOException;

    /**
     * @param pageNumber
     * @return Html string for given page
     * @throws IndexOutOfBoundsException if there is no page with such index
     */
    String getPage(int pageNumber) throws IndexOutOfBoundsException;

    /**
     * @return total pages number. If book isn't loaded or empty - returns 0
     */
    int getPagesCount();
}
