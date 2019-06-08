package app.settings;

import java.nio.charset.Charset;

import app.WebBookDownloader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        LANGUAGE(Type.CHOICES, 0, programStrings.settings_language,
                new String[] { "english", "polski" }),
        BOOK_TYPE(Type.CHOICES, 1, programStrings.settings_format,
                new String[] { "TXT", "EPUB", "PDF" }),
        ENCODING(Type.CHOICES, 2, programStrings.settings_website_encoding,
                Charset.availableCharsets().keySet().toArray(new String[0])),
        FONT(Type.CHOICES, 3, programStrings.settings_pdf_font, getFontList()),
        HTML_ELEMENT(Type.CHOICES, 4, programStrings.settings_html_element,
                new String[] { "<p", "<a", "<br", "<span" }),
        TITLE_AT_THE_END(Type.BOOLEAN, 5, programStrings.settings_title_at_the_end);

        private static String[] getFontList() {
            String[] fontList;
            try {
                fontList = new File(WebBookDownloader.workingDirectoryPath.concat("fonts")).list();
            } catch (Exception exception) {
                fontList = new String[0];
            }
            return fontList;
        }

        /**
         * Fields cache.
         */
        private static Field[] fieldNames = null;

        /**
         * Get all setting fields via reflection. Note: function is cached.
         * 
         * @return
         */
        private static Field[] getFields() {
            if (fieldNames != null) { // cached result
                return fieldNames;
            }
            return Fields.class.getFields();
        }

        private final Type type;
        private final String name;
        private final String[] choices;
        private final String label;

        private FieldDefinition(Type type, int index, String label, String[] choices) {
            this.type = type;
            this.name = getFields()[index].getName();
            this.label = label;
            this.choices = choices;
        }

        private FieldDefinition(Type type, int index, String label) {
            this(type, index, label, null);
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
     * Set field via reflection.
     * 
     * @param name
     * @param value
     * @return false if unable to set.
     */
    public static boolean setFieldValue(String name, Object value) {
        Field field = null;
        try {
            field = Settings.Fields.class.getField(name);
            field.set(null, value);
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException exception) {
            return false;
        } catch (IllegalArgumentException exception) { // parameter different than string 
            try { // try to set it via valueOf method of it's type.
                Method valueOf = field.getType().getDeclaredMethod("valueOf", String.class);
                field.set(null, valueOf.invoke(null, value));
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException
                    | InvocationTargetException e) {
                return false;
            }
        }
    }

    /**
     * Get field value via reflection.
     * 
     * @param name
     * @param defaultValue default value, which will be returned on error.
     * @return
     */
    public static Object getFieldValue(String name, Object defaultValue) {
        try {
            Field field = Settings.Fields.class.getField(name);
            return field.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            return defaultValue;
        }
    }

    /**
     * 
     * Get field value via reflection.
     * 
     * @param name
     * @return value or null if unable to get.
     */
    public static Object getFieldValue(String name) {
        return Settings.getFieldValue(name, null);
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
        public static Boolean titleAtTheEnd = false;
    }

}