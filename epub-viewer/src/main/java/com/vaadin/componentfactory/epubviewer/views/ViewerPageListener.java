package com.vaadin.componentfactory.epubviewer.views;

public interface ViewerPageListener {
    /**
     * Listener function for page flip event
     * @param pageNumber
     */
    void onPageChanged(int pageNumber);
}
