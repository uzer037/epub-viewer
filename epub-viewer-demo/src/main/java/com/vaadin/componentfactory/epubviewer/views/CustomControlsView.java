package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.MainLayout;
import com.vaadin.componentfactory.epubviewer.views.enums.BookFormatEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@Route(value = "CustomControlsExample", layout = MainLayout.class)
public class CustomControlsView extends Div implements ViewerPageListener {

    private Span pageIndicator = new Span("1 / 1");
    private BookViewer epubViewer = new BookViewer();
    private Div controlsLayout = new Div();
    public CustomControlsView() {
        // custom layout
        Button button1 = new Button("1");
        button1.addClickListener(e -> epubViewer.gotoPage(1));
        controlsLayout.add(button1);
        Button button2 = new Button("2");
        button2.addClickListener(e -> epubViewer.gotoPage(2));
        controlsLayout.add(button2);
        Button button3 = new Button("3");
        button3.addClickListener(e -> epubViewer.gotoPage(3));
        controlsLayout.add(button3);
        add(controlsLayout);

        // adding book viewer
        String bookName = "treasure-island.epub";
        InputStream bookStream = getClass().getClassLoader().getResourceAsStream(bookName);
        epubViewer.setBookSource(bookStream, BookFormatEnum.EPUB);

        // registering page listener
        epubViewer.addReaderPageListener(this::onPageChanged);

        // disabling built-in controls
        epubViewer.setShowControls(false);

        // adding component
        add(epubViewer);
    }

    @Override
    public void onPageChanged(int pageNumber) {
        pageIndicator.setText(epubViewer.getCurrentPageNumber() + " / " + epubViewer.getPagesCount());
    }
}
