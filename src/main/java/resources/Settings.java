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
package resources;

import java.io.File;
import resources.strings.Strings;

/**
 * This class holds global access application settings fields. Do not
 * instantiate it.
 * 
 * @author marcin
 */
public final class Settings {
        public static final int LANGUAGE_ENGLISH = 0, LANGUAGE_POLISH = 1, FILE_TXT = 0, FILE_EPUB = 1, FILE_PDF = 2;

        /**
         * This field holds information about which language is used in interface.
         * Options are in this class as final int fields.
         */
        public static int programLanguage = LANGUAGE_ENGLISH,
                        /**
                         * Type of book: ebook, txt, pdf.
                         */
                        fileType = FILE_TXT;

        /**
         * Path where program files will be saved.
         */
        public static String workingDirectoryPath = System.getProperty("user.dir").concat(File.separator), // there are
                                                                                                           // some
                                                                                                           // problems
                                                                                                           // with
                                                                                                           // property
                                                                                                           // on linux
                        /**
                         * Encoding of website from which chapters will be obtained, it will also be
                         * used to save book.
                         */
                        encoding = "UTF-8",
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
         * Instance of strings resources. You can access fields in it for creating
         * interface.
         */
        public static Strings programStrings;

}
