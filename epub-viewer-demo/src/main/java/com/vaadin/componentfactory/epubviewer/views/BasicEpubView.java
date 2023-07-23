package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.MainLayout;
import com.vaadin.componentfactory.epubviewer.views.enums.BookFormatEnum;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@Route(value = "", layout = MainLayout.class)
public class BasicEpubView extends Div {
  
    public BasicEpubView() {
        BookViewer epubViewer = new BookViewer();
        String bookName = "treasure-island.epub";
        InputStream bookStream = getClass().getClassLoader().getResourceAsStream(bookName);
        epubViewer.setBookSource(bookStream, BookFormatEnum.EPUB);

        add(epubViewer);
    }
  
}
