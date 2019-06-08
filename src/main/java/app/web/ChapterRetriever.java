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

import java.io.IOException;
import org.jsoup.Jsoup;

import app.settings.Settings;

/**
 * This class retrieves chapter text from link.
 *
 * @author marcin
 */
public class ChapterRetriever {

    private Integer crawlingCounter = null;
    private String nextLinkName = null, lastChapterTitle;

    /**
     * Retrieve chapter from website.
     *
     * @param address address of the website.
     * @return string[2] where [0] - chapterTitle, [1] - chapterText;
     * @throws IOException if unable to connect website.
     */
    public String[] retrieveChapter(String address) throws Exception {
        // get html of the chapter site
        final String html = Driver.readSite(address);
        // return chapter in form of table: 0 - title, 1 - text
        return new String[] { getChapterTitle(html), getChapterText(html) };
    }

    /**
     * 
     * @param document
     * @return
     * @throws Exception
     */
    private String getChapterText(String document) throws Exception {
        int size = 0, chapterTextBlockStartIndex, chapterTextBlockEndIndex = 0, divTagCount, index, newSize;
        int[] blockBounds = new int[2];
        // go over blocks with paragraphs, get one with the biggest size of text(parse
        // it).
        while ((chapterTextBlockStartIndex = document.indexOf(Settings.Fields.chapterParagraphContainer,
                chapterTextBlockEndIndex)) != -1) { // search for paragraphs until there is none left, jump over
                                                    // sub-divs.
            // set tag counter as unended
            divTagCount = 1;
            // jump to end of div if there are any sub-divs
            index = chapterTextBlockStartIndex;
            while (divTagCount > 0) { // on default we start with 1 tag count - we are in some div, now we want to
                                      // find its end.
                index = document.indexOf("div", index); // get index of div tag, we can also find normal text , thats
                                                        // why else if
                // validate index, we cant assume that all websites are properly written(or we
                // can find ourselves in script, where we don't know if there are any
                // containers). Error coming from it is not critical
                if (index == -1) {
                    index = chapterTextBlockStartIndex + 2; // create dummy range - 0-1 character, just to pass this
                                                            // useless piece of text
                    break;
                }
                // check if tag is ending or beginning
                if (document.charAt(index - 1) == '<') { // beginning tag
                    divTagCount++;
                } else if (document.charAt(index - 1) == '/') { // ending tag
                    divTagCount--;
                }
                // pass tag length to index, so we will find next, not the same
                index += 4;
            }
            // if chapter paragraph container is <br then we need to use parent index as
            // start
            if (Settings.Fields.chapterParagraphContainer.equals("<br")) {
                chapterTextBlockStartIndex = document.lastIndexOf("<div", chapterTextBlockStartIndex);
            }
            // after loop we should have index of block end
            chapterTextBlockEndIndex = index;
            newSize = Jsoup.parse(document.substring(chapterTextBlockStartIndex, chapterTextBlockEndIndex)).text()
                    .length();
            if (newSize > size) {
                size = newSize;
                blockBounds[0] = chapterTextBlockStartIndex;
                blockBounds[1] = chapterTextBlockEndIndex;
            }
        }

        // after loop we should have indexes of chapter text container, now clean
        // it(remove next chapter, previous chapter and trim).
        String chapterText = Jsoup.parse(document.substring(blockBounds[0], blockBounds[1])).text();
        chapterText = chapterText.replaceAll("Previous.Chapter", "");
        chapterText = chapterText.replaceAll("Next.Chapter", "");
        chapterText = chapterText.trim();
        return chapterText;
    }

    private String getChapterTitle(String document) throws Exception {
        // cut beginning of document - start with title tag
        document = document.substring(document.indexOf("<title>"));
        String title = document.substring(7, document.indexOf("</title>"));
        title = Jsoup.parse(title).text(); // parse escape chars
        // fix chapter name from second onwards
        if (lastChapterTitle == null) { // first chapter - full title, store it as last chapter title
            return lastChapterTitle = title; // if index is still not found just pass full title, index minimally should
                                             // be more than 2 - 1 for space, 1 for char
        } else { // second chapter+ - trim to only chapter text;
            int index;
            // todo: check if most of the sites have useless additions at the end of the
            // title
            /*
             * index = 0; while (title.charAt(index) == lastChapterTitle.charAt(index)) {
             * index++; } title = title.substring(index);
             */
            // find end index
            index = 1;
            while (title.charAt(title.length() - index) == lastChapterTitle.charAt(lastChapterTitle.length() - index)) {
                index++;
            }
            index = title.length() - index + 1; // remember that right parenthesis is exclusive, so we need to add 1
            return title.substring(0, index);
        }

    }

    public void initializeCrawling(Integer numberOfChapters, String nextLinkName) throws Exception {
        if (numberOfChapters != null) {
            crawlingCounter = numberOfChapters - 1;
        } // -1 because we have + 1 because of way of reading - chapter is read than we
          // check for next, and only there is checked condition.
        this.nextLinkName = nextLinkName;
    }

    /**
     * Retrieve chapter values from website by crawling.
     *
     * @param address address of the website.
     * @return [0] - chapter title; [1] - chapter text; [2] - next chapter address
     * @throws java.io.IOException if error in connection
     */
    public String[] retrieveChapterCrawling(String address) throws Exception {
        // get html of the chapter site
        final String html = Driver.readSite(address);
        // return chapter in form of table: 0 - title, 1 - text, 2 - next chapter

        return new String[] { getChapterTitle(html), getChapterText(html), getNextChapterAddress(html, address) };
    }

    private String getNextChapterAddress(String document, String chapterAbsoluteAddress) throws Exception {
        if (crawlingCounter == null || crawlingCounter > 0) { // > 0 - user friendly no 0 index. check if we don't
                                                              // exceed number of chapters specified by user or counter
                                                              // is unspecified meaning that we crawl to the end.
            if (crawlingCounter != null) {
                crawlingCounter--; // if we have restricted amount, then decrement it.
            }
            int indexOfName = document.indexOf(">".concat(nextLinkName));
            if (indexOfName == -1) { // couldn't find next chapter element - return null
                return null;
            }
            int indexOfLinkStart = document.lastIndexOf("href=", indexOfName),
                    // end - find char which is bracket of link than find it counterpart.
                    indexOfLinkEnd = document.indexOf(document.charAt(indexOfLinkStart + 5), indexOfLinkStart + 6);
            if (indexOfLinkStart != -1 && indexOfLinkEnd != -1) { // if indexes ok - all was found properly
                String link = document.substring(indexOfLinkStart + 6, indexOfLinkEnd);
                // check if link is absolute if not fix it.
                if (!link.contains("://")) {
                    // we have full address to previous chapter, but we need only part of it.
                    // new way - find position of local link first address part in absolute, if it
                    // doesn't exist concatenate whole links.
                    int localPartEnd = link.indexOf('/', 1); // get first slash - delimiting first folder in path, if
                                                             // there isn't one just concatenate with absolute - last
                                                             // path
                    int absoluteEnd;
                    if (localPartEnd > -1) {
                        absoluteEnd = chapterAbsoluteAddress.indexOf(link.substring(0, localPartEnd));
                        if (absoluteEnd < 0) { // if index was not found just set index to end of string
                            absoluteEnd = chapterAbsoluteAddress.length() - 1;
                        }
                    } else { // local is minimized - concatenated with absolute - last part
                        absoluteEnd = chapterAbsoluteAddress.lastIndexOf('/') + 1; // slash inclusive
                    }

                    link = chapterAbsoluteAddress.substring(0, absoluteEnd).concat(link);

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
