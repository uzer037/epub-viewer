package com.vaadin.componentfactory.epubviewer;

import com.vaadin.componentfactory.epubviewer.views.BasicEpubView;
import com.vaadin.componentfactory.epubviewer.views.CustomControlsView;
import com.vaadin.componentfactory.epubviewer.views.NoBookView;
import com.vaadin.componentfactory.epubviewer.views.PageListenerView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();

        final RouterLink basicExample = new RouterLink("Basic epub book example", BasicEpubView.class);
        final RouterLink noBookExample = new RouterLink("Example when no book was provided", NoBookView.class);
        final RouterLink pageListenerExample = new RouterLink("Page update listener use example", PageListenerView.class);
        final RouterLink customControlsExample = new RouterLink("Custom controls example", CustomControlsView.class);


        final VerticalLayout menuLayout = new VerticalLayout(
                basicExample,
                noBookExample,
                pageListenerExample,
                customControlsExample
        );
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

    @Override
    public void setContent(Component content) {
        super.setContent(content);
        content.getElement().getStyle().set("height", "100%");
        content.getElement().getStyle().set("overflow", "auto");
        content.getElement().getStyle().set("display", "flex");
        content.getElement().getStyle().set("flex-direction", "column");
    }
}
