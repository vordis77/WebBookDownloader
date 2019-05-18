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
package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import app.gui.ChapterSelectingPanel;
import app.gui.Interface;
import resources.Settings;
import static resources.Settings.LANGUAGE_ENGLISH;
import static resources.Settings.LANGUAGE_POLISH;
import static resources.Settings.programLanguage;
import static resources.Settings.programStrings;
import resources.strings.EnglishStrings;
import resources.strings.PolishStrings;

/**
 * Main class of the program.
 *
 * @author marcin
 */
public class WebBookDownloader {

    /**
     * Use this field to handle basic interaction between user and interface.
     */
    public static Interface gui;

    /**
     * Main method of the program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // load settings
        try {
            loadSettings();
        } catch (FileNotFoundException ex) {
        }

        // create corresponding to language Strings instance
        switch (programLanguage) {
        case LANGUAGE_POLISH:
            programStrings = new PolishStrings();
            break;
        case LANGUAGE_ENGLISH:
            programStrings = new EnglishStrings();
            break;
        default:
            programStrings = new EnglishStrings();
            break;
        }
        // initialize gui tool
        gui = new Interface();
        // show frame
        gui.initializeAndShowFrame();
        // start interface - show first panel: range choosing panel
        new ChapterSelectingPanel().showPanel();
    }

    /**
     * This method loads data from settings file into settings global fields, on
     * assumption that settings file exists and values lays in strict order, divided
     * by new line.
     */
    private static void loadSettings() throws FileNotFoundException {
        try (Scanner scan = new Scanner(new File(Settings.workingDirectoryPath.concat("settings.data")))) { // open file
            scan.useDelimiter("\n");
            try {
                String line;
                // language
                Settings.programLanguage = scan.nextInt();
                // book type
                Settings.fileType = scan.nextInt();
                // html element
                Settings.chapterParagraphContainer = scan.next();
                // get encoding, check if they are supported
                // encoding
                line = scan.next();
                if (Charset.isSupported(line)) {
                    Settings.encoding = line;
                }
                // pdf font name
                Settings.pdfFontFile = scan.next();
            } catch (Exception e) { // if data invalid do nothing, settings have default values
                System.err.println(e.toString());
            }
        }
    }

    public static void saveSettings() throws IOException {
        try (FileWriter fw = new FileWriter(Settings.workingDirectoryPath.concat("settings.data"))) {
            fw.append(Settings.programLanguage + "\n" + Settings.fileType + "\n" + Settings.chapterParagraphContainer
                    + "\n" + Settings.encoding + "\n" + Settings.pdfFontFile);
            fw.flush();
        }
    }

}
