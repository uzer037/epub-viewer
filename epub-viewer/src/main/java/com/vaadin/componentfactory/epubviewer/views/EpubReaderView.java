package com.vaadin.componentfactory.epubviewer.views;

import com.vaadin.componentfactory.epubviewer.bookloaders.loader.EpubLoader;

public class EpubReaderView extends BookReaderView{
    public EpubReaderView(){
        super();
        bookLoader = new EpubLoader();
    }
}
