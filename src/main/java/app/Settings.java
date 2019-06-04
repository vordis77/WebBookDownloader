package app;

import java.nio.charset.Charset;
import java.io.File;

import static app.WebBookDownloader.programStrings;

/**
 * Settings
 */
public class Settings {

    /**
     * Possible languages.
     */
    public enum Language {
        ENGLISH, POLISH;
    }

    /**
     * Possible book types.
     */
    public enum Book {
        TXT, EPUB, PDF;
    }

    /**
     * Setting field definition.
     */
    public enum FieldDefinition {
        // TODO: {Vordis 2019-06-03 19:54:51} names could be read dynamically via
        // reflection or generated based on const name (but const would have to match
        // static field name). Also it's fine as it is, since we have to define those
        // consts either way.
        LANGUAGE(Type.CHOICES, "programLanguage", programStrings.settings_language,
                new String[] { "english", "polski" }),
        BOOK_TYPE(Type.CHOICES, "bookType", programStrings.settings_format, new String[] { "TXT", "EPUB", "PDF" }),
        ENCODING(Type.CHOICES, "encoding", programStrings.settings_website_encoding,
                Charset.availableCharsets().keySet().toArray(new String[0])),
        FONT(Type.CHOICES, "pdfFontFile", programStrings.settings_pdf_font, getFontList()),
        HTML_ELEMENT(Type.CHOICES, "chapterParagraphContainer", programStrings.settings_html_element,
                new String[] { "<p", "<a", "<br", "<span" }),
        TITLE_AT_THE_END(Type.BOOLEAN, "titleAtTheEnd", programStrings.settings_title_at_the_end);

        private static String[] getFontList() {
            String[] fontList;
            try {
                fontList = new File(WebBookDownloader.workingDirectoryPath.concat("fonts")).list();
            } catch (Exception exception) {
                fontList = new String[0];
            }
            return fontList;
        }

        private final Type type;
        private final String name;
        private final String[] choices;
        private final String label;

        private FieldDefinition(Type type, String name, String label, String[] choices) {
            this.type = type;
            this.name = name;
            this.label = label;
            this.choices = choices;
        }

        private FieldDefinition(Type type, String name, String label) {
            this.type = type;
            this.name = name;
            this.label = label;
            this.choices = null;
        }

        /**
         * @return the type
         */
        public Type getType() {
            return type;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @return the choices
         */
        public String[] getChoices() {
            return choices;
        }

        public enum Type {
            CHOICES, BOOLEAN, RAW_VALUE;
        }
    }

    /**
     * Settings fields.
     */
    public static class Fields {
        /**
         * Encoding of website from which chapters will be obtained, it will also be
         * used to save book.
         */
        public static Language programLanguage = Language.ENGLISH;
        /**
         * Type of book: ebook, txt, pdf.
         */
        public static Book bookType = Book.TXT;

        /**
         * Encoding of website from which chapters will be obtained, it will also be
         * used to save book.
         */
        public static String encoding = "UTF-8",
                /**
                 * Font used by pdf. Note that wrong font can spell exceptions in creating book.
                 * For example if website has any chinese characters, then you have to use
                 * chinese font.
                 */
                pdfFontFile = "LatinExtended.ttf",
                /**
                 * Html element, which contains paragraphs of chapter. Block(div) with most of
                 * this elements will be assumed as block with chapter text.
                 */
                chapterParagraphContainer = "<p";

        /**
         * True if title should be set at the of the chapter.
         */
        public static boolean titleAtTheEnd = false;
    }

}