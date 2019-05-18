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
package app.web;

import app.web.Chapter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;

/**
 * This class analyzes and converts results from Driver(pure html) into user
 * friendly(pure string) results.
 *
 * @author marcin
 */
public class LinksRetriever {

    private final String encoding = "UTF-8";
    private String bookTitle = "";

    /**
     * Obtain chapters list from table of contents website.
     *
     * @param tocAddress table of contents address.
     * @return list of chapters.
     * @throws java.io.IOException if problem occured, when reading from site or
     *                             address is invalid - check if its instance of
     *                             MalformedURLException.
     */
    public ArrayList<Chapter> getChaptersFromTOC(String tocAddress) throws IOException {
        final ArrayList<Chapter> chapterList = new ArrayList<>();
        // get toc code
        String siteCode = Driver.readSite(tocAddress);
        // get bookTitle while we are at scanning index website
        bookTitle = retrieveBookTitle(siteCode);
        // scan over siteCode and get links from it
        int index, caret = 0;
        String chapterLink, chapterTitle;
        char linkChar;
        while ((index = siteCode.indexOf("href=", caret)) > -1) { // do until there is no more links
            // stop on link
            // get link char
            linkChar = siteCode.charAt(index + 5); // first character after '='
            // put caret on linkChar
            caret = index + 6; // remember that index is on the begining on word(href)
            // get chapterLink
            chapterLink = siteCode.substring(caret, siteCode.indexOf(linkChar, caret)); // -1 because caret is on first
                                                                                        // char after linkChar
            // check if link is local if yes convert it to absolute - use tocAddress to
            // create link
            if (!chapterLink.contains("://")) {
                chapterLink = tocAddress.substring(0, tocAddress.lastIndexOf('/') + 1).concat(chapterLink); // + 1
                                                                                                            // because
                                                                                                            // index is
                                                                                                            // exclusive,
                                                                                                            // and we
                                                                                                            // want
                                                                                                            // slash
            }

            // get chapter title
            caret = siteCode.indexOf(">", caret); // put caret on end of html element
            index = siteCode.indexOf("<", caret); // put index on begining of next html element, if we have link with
                                                  // description between those two elements lays description.
            if (index == -1) {
                break; // end of html, also html is probably broken, it should not end with link
            }
            chapterTitle = siteCode.substring(caret + 1, index); // get from first char after end of link element
                                                                 // inclusive to first char of next element exclusive
            if (chapterTitle.length() > 1) { // empty doesnt work, changed to length > 1 char.
                // add chapter if description is not empty, if description is empty we dont want
                // this link probably.
                // parse chapter title for any html codes
                chapterTitle = Jsoup.parse(chapterTitle).text();
                chapterList.add(new Chapter(chapterLink, chapterTitle));
            }
            // caret is on end of html block, should be ok for scanning for next link
            // element.

        }
        return chapterList;
    }

    /**
     * Get book title, only applicable to links from table of contents.
     * 
     * @return book title.
     */
    public String getBookTitle() {
        return bookTitle;
    }

    private String retrieveBookTitle(String document) {
        // cut beginning of document - start with title tag
        document = document.substring(document.indexOf("<title>"));
        String title = document.substring(7, document.indexOf("</title>"));
        // we have title of the website, now cut out website name, leave chapter title
        int index = title.indexOf("&#8211;"); // find first long minus
        if (index == -1) { // if there is no such character find normal minus
            index = title.indexOf("-");
        }
        index--; // take into account space
        return (index > 2) ? title.substring(0, index) : title; // if index is still not found just pass full title,
                                                                // index minimally should be more than 2 - 1 for space,
                                                                // 1 for char
    }

    /**
     * Obtain chapters from range specified by first and last chapter.
     *
     * @param startAddress address of first chapter.
     * @param endAddress   address of last chapter.
     * @return list of chapters.
     */
    public ArrayList<Chapter> getChaptersFromRange(String startAddress, String endAddress) {
        final ArrayList<Chapter> chapterList = new ArrayList<>();
        // find numbers in both adresses, then increment last number from first until it
        // gets the same value as number from last.
        String tempString = startAddress;
        tempString = tempString.replaceAll("[^0-9]+", " ");
        String[] startNumbers = tempString.trim().split(" ");
        tempString = endAddress;
        tempString = tempString.replaceAll("[^0-9]+", " ");
        String[] endNumbers = tempString.trim().split(" ");
        String numberString;
        // for convenience store startNumber in tempSTring
        tempString = startNumbers[startNumbers.length - 1];
        int indexOfNumber = startAddress.lastIndexOf(tempString);
        for (int i = new Integer(tempString); i <= new Integer(endNumbers[endNumbers.length - 1]); i++) { // go over all
                                                                                                          // elements
            numberString = Integer.toString(i);
            for (int j = 0; j < tempString.length() - Integer.toString(i).length(); j++) { // new number is lower than
                                                                                           // basic, it means basic
                                                                                           // number has leading zeros,
                                                                                           // so we just will add
                                                                                           // preceding 0 for every
                                                                                           // length number that number
                                                                                           // lacks, do not use
                                                                                           // numberString we need
                                                                                           // constant in condition
                numberString = "0" + numberString;
            }
            chapterList.add(new Chapter(startAddress.substring(0, indexOfNumber) + numberString
                    + startAddress.substring(indexOfNumber + tempString.length()), "Chapter " + i));
        }
        return chapterList;
    }

}
