package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.MainLayout;
import com.vaadin.componentfactory.epubviewer.views.enums.BookFormatEnum;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@Route(value = "PageListenerExample", layout = MainLayout.class)
public class PageListenerView extends Div implements ViewerPageListener {

    private Span pageIndicator = new Span();
    private BookViewer epubViewer = new BookViewer();
    public PageListenerView() {
        String bookName = "treasure-island.epub";
        InputStream bookStream = getClass().getClassLoader().getResourceAsStream(bookName);
        epubViewer.setBookSource(bookStream, BookFormatEnum.EPUB);

        // registering page listener
        epubViewer.addReaderPageListener(this::onPageChanged);

        add(pageIndicator);
        add(epubViewer);
    }

    @Override
    public void onPageChanged(int pageNumber) {
        pageIndicator.setText("Viewing page " + pageNumber + " of " + epubViewer.getPagesCount());
    }
}
