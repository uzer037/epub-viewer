package com.vaadin.componentfactory.epubviewer;

import com.vaadin.componentfactory.epubviewer.views.EpubReaderView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.io.IOException;

@Route(value = "NoBookExample", layout = MainLayout.class)
public class NoBook extends Div {

  public NoBook() {
      EpubReaderView epubViewer = new EpubReaderView();
      String bookName = "";
      try {
          epubViewer.loadBook(bookName);
      } catch (IOException e) {
          System.out.println("Book not found: " + e);
      }

      add(epubViewer);
  }
  
}
