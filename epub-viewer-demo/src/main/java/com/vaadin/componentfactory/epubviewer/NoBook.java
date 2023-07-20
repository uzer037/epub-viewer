package com.vaadin.componentfactory.epubviewer;

import com.vaadin.componentfactory.epubviewer.views.BookViewer;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.io.InputStream;

@Route(value = "NoBookExample", layout = MainLayout.class)
public class NoBook extends Div {

  public NoBook() {
      BookViewer epubViewer = new BookViewer();
      add(epubViewer);
  }
  
}
