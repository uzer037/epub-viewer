package com.vaadin.componentfactory.epubviewer.loaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EpubLoaderTest {
    String bookWithImagesFileName = "treasure-island.epub";
    String simpleBookFileName = "treasure-island.epub";
    String corruptedBookFileName = "corruptedBook.epub";
    String xhtmlExampleFileName = "spine_example.xhtml";
    @Test
    void testThatPageSlicerWorksAtAll() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xhtmlExampleFileName);
        final Document document = new Document(inputStream.readAllBytes().toString());
        inputStream.close();

        assertThatCode(()->loader.sliceDocumentToPages(document)).doesNotThrowAnyException();
    }
    @Test
    void testThatLoadingWorks() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(simpleBookFileName);
        assertThatCode(()->loader.loadFromSource(inputStream)).doesNotThrowAnyException();
        inputStream.close();
    }

    @Test
    void testPageCount() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(simpleBookFileName);
        loader.loadFromSource(inputStream);
        inputStream.close();

        // actual page count is around 200 if result page count deviates significantly then something is VERY wrong
        assertThat(loader.getPagesCount()).isGreaterThan(150);
        assertThat(loader.getPagesCount()).isLessThan(250);
    }

    @Test
    void testPageSlicer() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(simpleBookFileName);
        loader.loadFromSource(inputStream);
        inputStream.close();

        assertThat(loader.getPagesCount()).isGreaterThan(150);
        assertThat(loader.getPagesCount()).isLessThan(250);
    }

    @Test
    void testImageNameExtractor() {
        EpubLoader loader = new EpubLoader();
        Map<String,String> testData = new HashMap<>();

        // if name is already extracted - method should leave it as is
        testData.put("test_no_nesting.png", "test_no_nesting.png");

        // other test cases
        testData.put("./test_one_layer.png", "test_one_layer.png");
        testData.put("./images/chapter_01/test_multilayer_nesting.png", "test_multilayer_nesting.png");
        testData.put("./././test_multiple_dots.png", "test_multiple_dots.png");

        for (String input : testData.keySet()) {
            String output = testData.get(input);
            assertThat(loader.extractResourceFileName(input)).isEqualTo(output);
        }
        assertThat(loader.extractResourceFileName("./path/to/nothing/")).isEmpty();
    }

    @Test
    void testImageLoading() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(bookWithImagesFileName);
        loader.loadFromSource(inputStream);
        inputStream.close();

        Map<String, String> imagesMap = loader.getImagesMap();
        assertThat(imagesMap).isNotEmpty();
        for (String imageName : imagesMap.keySet()) {
            /* image name should be processed by extractResourceFileName
                (if processed second time method should leave it as is) */
            assertThat(imageName).isEqualTo(loader.extractResourceFileName(imageName));
            // image should be non-empty base64 string
            assertThat(imagesMap.get(imageName)).isNotEmpty();
            assertThat(imagesMap.get(imageName)).isBase64();
        }
    }

    @Test
    void testGetPage() throws IOException{
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(simpleBookFileName);
        loader.loadFromSource(inputStream);
        inputStream.close();

        int pagesCount = loader.getPagesCount();

        assertThatCode(()->loader.getPage(1)).doesNotThrowAnyException();

        // page number should be between 1 and loader.getPagesCount()
        assertThrows(IndexOutOfBoundsException.class, ()->loader.getPage(0));
        assertThrows(IndexOutOfBoundsException.class, ()->loader.getPage(pagesCount+1));

        /*
         *  JSoup internally makes sure that string will be interpreted as valid html
         *  so at least let's make sure that html string is not empty
         */
        for(int i = 1; i <= pagesCount; i++) {
            String pageHtml = loader.getPage(i);
            assertThat(pageHtml).isNotEmpty();
        }
    }

    @Test
    void testLoadingInvalidImage() throws IOException {
        EpubLoader loader = new EpubLoader();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(corruptedBookFileName);
        assertThatCode(()->loader.loadFromSource(inputStream)).doesNotThrowAnyException();
        inputStream.close();
    }
}
