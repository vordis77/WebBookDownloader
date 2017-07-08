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
package web;

import java.io.IOException;
import org.jsoup.Jsoup;
import resources.Settings;

/**
 * This class retrieves chapter text from link.
 *
 * @author marcin
 */
public class WebChapterRetriever {

    private Integer crawlingCounter = null;
    private String nextLinkName = null;

    /**
     * Retrieve chapter from website.
     *
     * @param address address of the website.
     * @return string[2] where [0] - chapterTitle, [1] - chapterText;
     * @throws IOException if unable to connect website.
     */
    public String[] retrieveChapter(String address) throws IOException {
        // get html of the chapter site
        final String html = WebDriver.readSite(address);
        // return chapter in form of table: 0 - title, 1 - text
        return new String[]{
            getChapterTitle(html),
            getChapterText(html)
        };
    }

    private String getChapterText(String document) {
        int size = 0, chapterTextBlockStartIndex, chapterTextBlockEndIndex = 0, newSize;
        int[] blockBounds = new int[2];
        // go over blocks with paragraphs, get one with the biggest size of text(parse it).
        while ((chapterTextBlockStartIndex = document.indexOf(Settings.chapterParagraphContainer, chapterTextBlockEndIndex)) != -1 && (chapterTextBlockEndIndex = document.indexOf("<div", chapterTextBlockStartIndex)) != -1) { // search for paragraphs until there is none left, assume that block ends with the begining of new block, so we assume that block cannot be last in document.
            newSize = Jsoup.parse(document.substring(chapterTextBlockStartIndex, chapterTextBlockEndIndex)).text().length();
            if (newSize > size) {
                size = newSize;
                blockBounds[0] = chapterTextBlockStartIndex;
                blockBounds[1] = chapterTextBlockEndIndex;
            }
        }
        // after loop we should have indexes of chapter text container, now clean it(remove next chapter, previous chapter and trim).
        String chapterText = Jsoup.parse(document.substring(blockBounds[0], blockBounds[1])).text();
        chapterText = chapterText.replaceAll("Previous.Chapter", "");
        chapterText = chapterText.replaceAll("Next.Chapter", "");
        chapterText = chapterText.trim();
        return chapterText;
    }

    private String getChapterTitle(String document) {
        // cut beginning of document - start with title tag
        document = document.substring(document.indexOf("<title>"));
        String title = document.substring(7, document.indexOf("</title>"));
        title = Jsoup.parse(title).text(); // parse escape chars
        // we have title of the website, now cut out website name, leave chapter title
        int index = title.lastIndexOf("–"); // find last long minus
        if (index == -1) { // if there is no such character find normal minus
            index = title.lastIndexOf("-");
        }
        index--; // take into account space 
        return (index > 2) ? title.substring(0, index) : title; // if index is still not found just pass full title, index minimally should be more than 2 - 1 for space, 1 for char
    }

    public void initializeCrawling(Integer numberOfChapters, String nextLinkName) {
        if (numberOfChapters != null) {
            crawlingCounter = numberOfChapters - 1;
        } // -1 because we have + 1 because of way of reading - chapter is read than we check for next, and only there is checked condition.
        this.nextLinkName = nextLinkName;
    }

    /**
     * Retrieve chapter valeus from website by crawling.
     *
     * @param address address of the website.
     * @return [0] - chapter title; [1] - chapter text; [2] - next chapter
     * address
     * @throws java.io.IOException if error in conection
     */
    public String[] retrieveChapterCrawling(String address) throws IOException {
        // get html of the chapter site
        final String html = WebDriver.readSite(address);
        // return chapter in form of table: 0 - title, 1 - text, 2 - next chapter

        return new String[]{
            getChapterTitle(html),
            getChapterText(html),
            getNextChapterAddress(html, address)
        };
    }

    private String getNextChapterAddress(String document, String chapterAbsoluteAddress) {
        if (crawlingCounter == null || crawlingCounter > 0) { // > 0 - user friendly no 0 index. check if we don't exceed number of chapters specified by user or counter is unspecified meaning that we crawl to the end.
            if (crawlingCounter != null) {
                crawlingCounter--; // if we have restricted amount, then decrement it.
            }
            int indexOfName = document.indexOf(">".concat(nextLinkName));
            if (indexOfName == -1) { // couldn't find next chapter element - return null
                return null;
            }
            int indexOfLinkStart = document.lastIndexOf("href=", indexOfName),
                    indexOfLinkEnd = document.indexOf(document.charAt(indexOfLinkStart + 5), indexOfLinkStart + 6); // end - find char which is bracket of link than find it counterpart.
            if (indexOfLinkStart != -1 && indexOfLinkEnd != -1) { // if indexes ok - all was found properly
                String link = document.substring(indexOfLinkStart + 6, indexOfLinkEnd);
                // check if link is absolute if not fix it.
                if (!link.contains("://")) {
                    // we have full address to previous chapter, but we need only part of it.
                    // assuming that last chapter and next chapter are in the same folder, we can just cut out last part of chapter and get last part of link
                    int chapterIndex = chapterAbsoluteAddress.lastIndexOf('/', chapterAbsoluteAddress.length()-2); // -2 because we want to ommit last slash if it exists
                    int linkIndex = link.lastIndexOf('/', link.length()-2); // same
                    
                    link = chapterAbsoluteAddress.substring(0, chapterIndex).concat(link.substring(linkIndex)); 
                }
                return link;
            } else { // if somehow indexes broke, it shouldnt occur.
                return null;
            }
        } else { // we exceeded number of chapters - return null as next address
            return null; // there is no next chapter
        }
    }
}
