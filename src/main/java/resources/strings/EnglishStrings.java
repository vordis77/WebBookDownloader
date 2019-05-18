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
package resources.strings;

/**
 * This class holds strings for program in polish language.
 *
 * @author marcin
 */
public class EnglishStrings extends Strings {

    public EnglishStrings() {
        super("Error",
                "WebBookDownloader",
                "Program",
                "Help",
                "Close",
                "About program",
                "WebBookDownloader v 1.0\n"
                        + "This program allows you to browse through website and download it's content as book\n"
                        + "Copyright (c) 2017\n"
                        + "Author Marcin Klimek",
                "Index",
                "Range",
                "Crawl",
                "Table of contents address: ",
                "First chapter address: ",
                "Last chapter address: ",
                "Number of chapters(leave empty for crawling to the end): ",
                "Confirm",
                "Adress, that you provided is invalid.",
                "Unable to establish connection with site that you provided.",
                "Select chapters",
                "Please wait...",
                "Create file.",
                "Succeed: ",
                "Failed: ",
                "Total: ",
                "Cancel",
                "Back",
                "You have to select at least one chapter!",
                "Invalid filename!",
                " downloaded and saved successfully, length of chapter(in characters): ",
                "Couldn't download or save chapter. Address of failed chapter and cause: ",
                "Raport",
                "Operation finished, size of the book in characters: ",
                "Name of the link to next chapter: ",
                "Settings",
                "Note that some changes will be only reflected after restart of program.",
                "Language: ",
                "Format of book: ",
                "Html element, which contains paragraph in chapter website: ",
                "Source website encoding: ",
                "Font file which will be used as font in pdf: ");
    }

}
