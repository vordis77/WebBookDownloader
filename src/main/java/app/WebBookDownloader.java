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

import static app.settings.Settings.Fields.programLanguage;

import java.io.File;

import app.gui.ChapterSelectingPanel;
import app.gui.Interface;
import app.settings.Reader;
import resources.strings.EnglishStrings;
import resources.strings.PolishStrings;
import resources.strings.Strings;

/**
 * Main class of the program.
 *
 * @author marcin
 */
public class WebBookDownloader {

    /**
     * Instance of strings resources. You can access fields in it for creating
     * interface.
     */
    public static Strings programStrings;

    /**
     * Path where program files will be saved.
     */
    public static final String workingDirectoryPath = System.getProperty("user.dir").concat(File.separator);

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
        Reader settingsReader = new Reader();
        settingsReader.loadSettings(); // ignore result, failure is not critical
        // also it's logged in reader.

        initProgramStrings();

        // initialize gui tool
        gui = new Interface();
        // show frame
        gui.initializeAndShowFrame();
        // start interface - show first panel: range choosing panel
        new ChapterSelectingPanel().showPanel();
    }

    /**
     * Init appropriate String instance.
     */
    private static void initProgramStrings() {
        switch (programLanguage) {
        case POLISH:
            WebBookDownloader.programStrings = new PolishStrings();
            break;
        default:
        case ENGLISH:
            WebBookDownloader.programStrings = new EnglishStrings();
            break;
        }
    }

}
