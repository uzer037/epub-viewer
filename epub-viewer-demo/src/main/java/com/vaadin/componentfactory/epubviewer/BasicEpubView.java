package com.vaadin.componentfactory.epubviewer;

import com.vaadin.componentfactory.epubviewer.views.BookViewer;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@Route(value = "", layout = MainLayout.class)
public class BasicEpubView extends Div {
  
  public BasicEpubView() {
      BookViewer epubViewer = new BookViewer();
      String bookName = "Spring REST 2022.epub";
      InputStream bookStream = getClass().getClassLoader().getResourceAsStream(bookName);
      epubViewer.setBookSource(bookStream, BookFormatEnum.EPUB);

      add(epubViewer);
  }
  
}
