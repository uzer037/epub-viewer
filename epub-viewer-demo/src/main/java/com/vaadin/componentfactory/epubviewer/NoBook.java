package com.vaadin.componentfactory.epubviewer;

import com.vaadin.componentfactory.epubviewer.views.EpubReaderView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.io.InputStream;

@Route(value = "NoBookExample", layout = MainLayout.class)
public class NoBook extends Div {

  public NoBook() {
      EpubReaderView epubViewer = new EpubReaderView();
      String bookName = "";
      InputStream bookStream = getClass().getClassLoader().getResourceAsStream(bookName);
      try {
          epubViewer.loadBook(bookStream);
      } catch (IOException e) {
          System.out.println("Book not found: " + e);
      }

      add(epubViewer);
  }
  
}
