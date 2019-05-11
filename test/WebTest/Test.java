/*
 * Copyright (c) 2017, marcin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package WebTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubWriter;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import resources.Settings;
import tools.FileSaver;
import tools.WebChapter;
import web.WebChapterRetriever;
import web.WebLinksRetriever;

/**
 *
 * @author marcin
 */
public class Test { // TODO: transform into real true tests

    public Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    public void testChapterRetriever() throws IOException {
        final WebChapterRetriever wcr = new WebChapterRetriever();
        final String[] result = wcr.retrieveChapter("http://gravitytales.com/Novel/master-of-the-stars/ms-chapter-1");
        System.out.println(result[0] + "\n" + result[1]);
    }

    public void testChaptersFromRange() {
        final WebLinksRetriever wlr = new WebLinksRetriever();
        final ArrayList<WebChapter> chapterList = wlr.getChaptersFromRange("http://www.wuxiaworld.com/ldk-index/ldk-chapter-001/", "http://www.wuxiaworld.com/ldk-index/ldk-chapter-100/");
    }

    public void testCrawling() throws IOException {
        final WebChapterRetriever wcr = new WebChapterRetriever();
        wcr.initializeCrawling(10, "Next Chapter");
        String[] result;
        String nextChapterAddress = "http://gravitytales.com/Novel/master-of-the-stars/ms-chapter-1";
        do {
            result = wcr.retrieveChapterCrawling(nextChapterAddress);
            System.out.println(result[0] + "\n" + result[1] + "\n" + result[2]);
            nextChapterAddress = result[2];
        } while (result[2] != null); // next address exists

    }

    public void testEpubCreating() throws IOException {
        // Create new Book
        Book book = new Book();
// Set the title
        book.getMetadata().addTitle("Epublib test book 1");

// Add an Author
        book.getMetadata().addAuthor(new Author("Joe", "Tester"));

// Set cover image
        book.setCoverImage(new Resource(new FileInputStream("book1/cover.png"), "cover.png"));

// Add Chapter 1
        book.addSection("Introduction", new Resource(new FileInputStream("book1/chapter1.html"), "chapter1.html"));

// Add css file
        book.getResources().add(new Resource(new FileInputStream("book1/book1.css"), "book1.css"));

// Add Chapter 2
        TOCReference chapter2 = book.addSection("Second Chapter", new Resource(new FileInputStream("book1/chapter2.html"), "chapter2.html"));

// Add image used by Chapter 2
        book.getResources().add(new Resource(new FileInputStream("book1/flowers.jpg"), "flowers.jpg"));

// Add Chapter2, Section 1
        book.addSection(chapter2, "Chapter 2, section 1", new Resource(new FileInputStream("book1/chapter2_1.html"), "chapter2_1.html"));

// Add Chapter 3
        book.addSection("Conclusion", new Resource(new FileInputStream("book1/chapter3.html"), "chapter3.html"));

// Create EpubWriter
        EpubWriter epubWriter = new EpubWriter();

// Write the Book as Epub
        epubWriter.write(book, new FileOutputStream("test1_book1.epub"));

    }

    public void testCustomEpub() throws IOException {
        // Create new Book
        Book book = new Book();
// Set the title
        book.getMetadata().addTitle("My book");

// Add an Author
        book.getMetadata().addAuthor(new Author("WebBookDownloader"));

        String chapter1, chapter2;
        chapter1 = "<html>\n"
                + "<head>\n"
                + "	<title>Chapter 1</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<h1>Introduction</h1>\n"
                + "<p>\n"
                + "Welcome to Chapter 1 of the epublib book1 test book.<br/>\n"
                + "We hope you enjoy the test.\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";
        chapter2 = "SDfsadgoiasdkfas doloroes, safdsafjsdi fdsakfsdakfajasdkfjsdakfjksda fksd jdskafjasdkjf ksdajfksdaj fksdafjk asjf";

// Add Chapter 1
        book.addSection("Chapter 1", new Resource(chapter1.getBytes(), "chapter1.html"));

// Add css file
        //book.getResources().add(new Resource(new FileInputStream("book1/book1.css"), "book1.css"));
// Add Chapter 2
        book.addSection("Chapter 2", new Resource(chapter2.getBytes(), "chapter2.html"));

// Create EpubWriter
        EpubWriter epubWriter = new EpubWriter();

// Write the Book as Epub
        epubWriter.write(book, new FileOutputStream("test1_book1.epub"));
    }

    public void testCharacterFromUnicode() {
        final String hexCode = "U+00f3";
        final int code = Integer.parseInt(hexCode.substring(2), 16);
        final char c = (char) code;
    }

    @org.junit.Test
    public void testPDFFont() throws FileNotFoundException, IOException {

        // create document
        PDDocument document = new PDDocument();
        //Setting the author of the document
        document.getDocumentInformation().setAuthor("WebBookDownloader");
        PDFont font = PDType0Font.load(document, new FileInputStream(Settings.workingDirectoryPath.concat("fonts").concat(File.separator).concat("Chinese.ttf")), true);
        final String s = "M M M M ";
        final float mWidth = font.getStringWidth(s); // 889 for serif 824 for chinese
        // 4556 serif, 5296 chinese
    }

}
