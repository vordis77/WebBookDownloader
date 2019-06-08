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
package app.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import app.WebBookDownloader;
import app.settings.Settings;
import app.settings.Settings.Fields;

/**
 * This class allows to save chapters into file, depending on user settings -
 * epub, txt, pdf.
 *
 * @author marcin
 */
public class Saver { // TODO: {Vordis 2019-05-18 17:33:00} reshape into concrete savers

    private final String filePath, encoding;
    private PrintWriter pw;
    private Book ebook;
    private PDDocument document;
    private PDFPageWriter ppw;
    private int pdfTextWidth = 80, pdfTitleWidth = 47;

    /**
     * Create new instance of fileSaver.
     *
     * @param fileName absolute path of the file.
     * @param encoding encoding of the file.
     */
    public Saver(String fileName, String encoding) {
        this.filePath = fileName;
        this.encoding = encoding;
    }

    /**
     * Get title for the ebook.
     * 
     * @return string ebook title.
     */
    private String getBookTitle() {
        // generate ebook title based on file name - find text between last slash and
        // dot.
        final int rangeStart = filePath.lastIndexOf(File.separatorChar), rangeEnd = filePath.lastIndexOf('.');

        // return only if ranges are valid (were found)
        if (rangeStart != -1 && rangeEnd != -1) {
            return filePath.substring(rangeStart + 1, rangeEnd); // + 1 for exclusion of slash.
        }

        return "Untitled";
    }

    /**
     * Creates file. Epub have default encoding - utf-8, but it should be ok.
     *
     * @throws FileNotFoundException        if path is invalid.
     * @throws UnsupportedEncodingException if encoding is invalid.
     */
    public void createFile() throws UnsupportedEncodingException, IOException {
        switch (Fields.bookType) {
        case EPUB:
            ebook = new Book();
            // Set the title
            ebook.getMetadata().addTitle(getBookTitle());
            // Add an Author
            ebook.getMetadata().addAuthor(new Author("WebBookDownloader"));
            break;
        case PDF:
            // create document
            document = new PDDocument();
            // Setting the author of the document
            document.getDocumentInformation().setAuthor("WebBookDownloader");
            // Setting the title of the document
            document.getDocumentInformation().setTitle(getBookTitle());
            // content handler
            ppw = new PDFPageWriter(document);
            // compute true text width based on used font
            final float fontFactor = ppw.getFontFactor();
            pdfTextWidth *= fontFactor;
            pdfTitleWidth *= fontFactor;
            break;
        case TXT:
            pw = new PrintWriter(filePath, encoding);
            break;
        }
    }

    /**
     * Save chapter into file.
     *
     * @param values string array where [0] - chapter title, [1] - chapter text
     * @throws IOException exception if saving failed
     */
    public void saveToFile(String[] values) throws IOException {
        switch (Fields.bookType) {
        case EPUB:
            ebook.addSection(values[0], new Resource(getEbookChapterValue(values), values[0].concat(".html")));
            break;
        case PDF:
            ppw.createPage(values[0]);
            for (Object object : explodeStringIntoPdfLines(values[1], pdfTextWidth)) {
                // check if we have free lines on page, if not then add last page and create new
                // one.
                if (!ppw.hasFreeLines()) {
                    ppw.addPageToDocument();
                    ppw.createPage(null);
                }
                // add line to page
                ppw.addLine((String) object);
            }
            // remember that last page could have not full lines, so it wasn't saved.
            if (ppw.hasUnsavedProgress()) {
                ppw.addPageToDocument();
            }
            break;
        case TXT:
            pw.print(values[0] + "\n" + values[1] + "\n");
            break;
        }
    }

    public void closeFile() throws IOException {
        switch (Fields.bookType) {
        case EPUB:
            // Create EpubWriter
            EpubWriter epubWriter = new EpubWriter();
            // Write the Book as Epub
            epubWriter.write(ebook, new FileOutputStream(filePath));
            break;
        case PDF:
            // save document
            document.save(filePath);
            // close document
            document.close();
            break;
        case TXT:
            pw.flush(); // flush to be sure that everything was saved.
            pw.close();
            pw = null;
            break;
        }
    }

    private byte[] getEbookChapterValue(String[] values) throws UnsupportedEncodingException {
        String htmlString = "<html>\n" + "<head>\n" + "	<title>" + values[0] + "</title>\n" + "</head>\n" + "<body>\n"
                + "<h1>" + values[0] + "</h1>\n" + "<p>\n" + values[1] + "</p>\n" + "</body>\n" + "</html>";

        return htmlString.getBytes(encoding);
    }

    /**
     * This class allows easy creating of pdf documents.
     */
    private class PDFPageWriter {

        private PDPage page;
        private PDPageContentStream contentStream;
        private final PDDocument document;
        private int lineCounter = 0;
        private final int MAX_LINES = 47;
        private final PDFont font;

        public PDFPageWriter(PDDocument document) throws IOException {
            this.document = document;
            font = PDType0Font.load(document, new FileInputStream(
                    WebBookDownloader.workingDirectoryPath.concat("fonts").concat(File.separator).concat(Fields.pdfFontFile)),
                    true);

        }

        /**
         * Compute font factor - difference between desirable and font used.
         * 
         * @return font factor, multiply textWidth by it to get desirable textWidth.
         * @throws IOException exception if error occurred.
         */
        public float getFontFactor() throws IOException {
            final float desirableWidth = 4700f;
            final String testString = "M M M M "; // longest characters
            final float testWidth = font.getStringWidth(testString);
            return desirableWidth / testWidth;
        }

        /**
         * This method creates new page, if you are reusing it remember to call
         * addPageToDocument first.
         *
         * @param title    title of document or null if non titled page.
         * @param document document.
         * @throws IOException exception if unable to save.
         */
        public void createPage(String title) throws IOException {
            // tools
            page = new PDPage();
            // stream
            contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 730); // caret start
            if (title != null) {
                contentStream.setLeading(22.5f); // vertical space between lines for title it needs to be a little
                                                 // bigger
                contentStream.setFont(font, 22); // title format
                // add title, take into account that it can span multi lines, probably only 2
                // but better safe than sorry
                for (Object o : explodeStringIntoPdfLines(title, pdfTitleWidth)) {
                    addLine((String) o);
                }
                contentStream.newLine();
                lineCounter++; // take into account new line
                lineCounter *= 1.55; // take into account difference between fonts.
            }
            contentStream.setLeading(14.5f); // vertical space between lines
            contentStream.setFont(font, 14); // text format
        }

        /**
         * Check if page has anymore free lines.
         *
         * @return true if page has free lines.
         */
        public boolean hasFreeLines() {
            return MAX_LINES > lineCounter;
        }

        /**
         * Check if page was saved.
         *
         * @return true if page was saved.
         */
        public boolean hasUnsavedProgress() {
            return lineCounter > 0;
        }

        /**
         * Add single line to page.
         *
         * @param line line.
         * @throws IOException exception if operation failed.
         */
        public void addLine(String line) throws IOException {
            // pdfBox has stupid errors with fonts - encoding characters, so we will just
            // catch exceptions and replace recursively.
            try {
                contentStream.showText(line = parseString(line));
            } catch (Exception ex) {
                if (ex.getMessage().contains("No glyph for")) { // check if font failed
                    int index = ex.getMessage().indexOf("U+") + 2;
                    addLine(line.replace((char) Integer.parseInt(ex.getMessage().substring(index, index + 4), 16),
                            '?'));
                } else { // other error, pass exception up
                    throw ex;
                }
            }
            contentStream.newLine();
            lineCounter++;
        }

        /**
         * Replace extended unicode with normal chars.
         *
         * @param string string to be parsed
         * @return parsed string.
         */
        private String parseString(String string) {
            return string.replace((char) 0x00a0, ' ').replace((char) 0x2600, '*'); // replace two chars, which can make
                                                                                   // problems in multiple fonts.
        }

        /**
         * Add page to document, also close page stream.
         *
         * @throws IOException exception if operation failed.
         */
        public void addPageToDocument() throws IOException {
            contentStream.endText();
            contentStream.close();
            lineCounter = 0;
            document.addPage(page);
        }

    }

    private Object[] explodeStringIntoPdfLines(String string, int lineWidth) {
        ArrayList<String> lineList = new ArrayList<>();
        int lastIndex = 0, rangeStart;
        while ((rangeStart = lastIndex) < string.length()) { // go until
            lastIndex = string.indexOf(' ', rangeStart + lineWidth) + 1; // space last in line, not in the next
            if (lastIndex == 0) { // if last index is not found, then it means we are at the end of the string
                                  // with no more spaces left
                lastIndex = string.length();
            }
            lineList.add(string.substring(rangeStart, lastIndex));
        }
        return lineList.toArray();
    }

}
