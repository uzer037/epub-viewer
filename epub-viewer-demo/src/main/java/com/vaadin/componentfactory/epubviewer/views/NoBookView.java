package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "NoBookExample", layout = MainLayout.class)
public class NoBookView extends Div {

    public NoBookView() {
        BookViewer epubViewer = new BookViewer();
        add(epubViewer);
    }
  
}
