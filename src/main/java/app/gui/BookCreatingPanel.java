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
package app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import resources.Settings;
import resources.strings.Strings;
import app.file.Saver;
import app.gui.components.Button;
import app.gui.components.Label;
import app.gui.components.Panel;
import app.web.Chapter;
import app.web.ChapterRetriever;
import app.WebBookDownloader;

/**
 * This class is responsible for panel in which user can see progress of
 * creating book, can cancel it or (after operation ended) go back to
 * chapterSelecting.
 *
 * @author marcin
 */
public class BookCreatingPanel extends Panel {

    private static final long serialVersionUID = -4681243607663660435L;
    private final ArrayList<Chapter> chapters;
    private final String fileName;
    private final Strings strings;
    private final ChapterSelectingPanel parentPanel;
    private boolean taskCancelled = false;
    private final String[] crawlingValues;
    private static final Logger LOGGER = Logger.getLogger(BookCreatingPanel.class.getName());

    /**
     * Create new instance of book creating panel.
     *
     * @param chapters       list of chapters from which book will be created, pass
     *                       null if you want to crawl through next links.
     * @param fileName       path to file in which book will be saved.
     * @param parentPanel    parent instance, thanks to this reference we can
     *                       recreate panel with values in fields.
     * @param crawlingValues (string array where 0 - next chapter link name 1 -
     *                       number of chapters 2 - first chapter address) or null
     *                       if we are using selected chapters.
     */
    public BookCreatingPanel(ArrayList<Chapter> chapters, String fileName, ChapterSelectingPanel parentPanel,
            String[] crawlingValues) {
        this.chapters = chapters;
        this.fileName = fileName;
        this.strings = Settings.programStrings;
        this.parentPanel = parentPanel;
        this.crawlingValues = crawlingValues;
    }

    @Override
    public void initializePanel() throws Exception {
        // first of all try to create file, if failure back to parent panel
        final Saver fs = new Saver(fileName, Settings.encoding);
        try {
            fs.createFile();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) { // it should not occur with fileDialog but
                                                                            // who knows.
            // inform user, throw exception so this panel will not be shown and rest
            // operations will not be done.
            WebBookDownloader.gui.showInformationDialog(strings.errorDialogTitle,
                    strings.dialog_invalid_filename_message);
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Undefined failure in file creation.", ex);
        }
        // number of chapters with taking into account crawlingValues
        int numberOfChapters = (chapters != null) ? chapters.size()
                : (!crawlingValues[1].isEmpty()) ? Integer.valueOf(crawlingValues[1]) : 0; // if empty than user wants
                                                                                           // all
        // chapters in crawling

        // components
        final Label chaptersSuccessTextLabel = new Label(strings.book_creating_chapters_success),
                chaptersSuccessValueLabel = new Label("0"),
                chaptersFailureTextLabel = new Label(strings.book_creating_chapters_failure),
                chaptersFailureValueLabel = new Label("0"),
                chaptersTotalTextLabel = new Label(strings.book_creating_chapters_total),
                chaptersTotalValueLabel = new Label(String.valueOf(numberOfChapters));
        final JProgressBar progressBar = new JProgressBar(0, numberOfChapters);
        // if crawling with unspecified number use ambiguous progress bar
        if (numberOfChapters == 0) {
            progressBar.setIndeterminate(true);
        }
        progressBar.setStringPainted(true);
        final Button cancelButton = new Button(strings.book_creating_button_cancel),
                backButton = new Button(strings.book_creating_button_back);
        final JList<Object> progressDetails = new JList<Object>(new DefaultListModel<Object>());
        // cell renderer - set color of font for success and error, based on assumption
        // that chapter name doesn't contain :// character
        progressDetails.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = -8147757033069789714L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if (value.toString().contains("://")) { // it's failure - contains address
                    label.setForeground(Color.red);
                } else {
                    label.setForeground(Color.green);
                }
                return label;
            }

        });
        final JScrollPane progressDetailsContainer = new JScrollPane(progressDetails);
        // disable back button on default - before operation ended or user cancelled
        // back button shall be unavailable
        backButton.setEnabled(false);

        // swing worker - async task that will create book in background
        // todo: change to multitasking, especially networking - it will give huge speed
        // upgrade
        // or make it an option in settings.
        new SwingWorker<Object, Object>() {
            long bookSizeInCharacters = 0;

            @Override
            protected Object doInBackground() throws Exception {
                // go over all chapters
                String[] chapterValues;
                final ChapterRetriever wcr = new ChapterRetriever();
                int successCounter = 0, failureCounter = 0;
                String resultDescription;
                // here we have decisions: for each selected chapter or crawl through chapters
                // until there is no next chapter or maximum amount of chapters received.
                if (chapters != null) { // toc or range
                    for (Chapter wc : chapters) {
                        try {
                            // get chapterText and title
                            chapterValues = wcr.retrieveChapter(wc.getAddress());
                            // save them into file depending on user choice(txt, epub or pdf)
                            fs.saveToFile(chapterValues);
                            // if all ok counter of success +1
                            successCounter++;
                            resultDescription = chapterValues[0] + strings.book_creating_list_success_entry
                                    + chapterValues[1].length();
                            bookSizeInCharacters += chapterValues[1].length() + chapterValues[0].length();
                        } catch (Exception e) {
                            failureCounter++;
                            resultDescription = strings.book_creating_list_failure_entry + wc.getAddress() + " -> "
                                    + e.getMessage();
                            LOGGER.log(Level.SEVERE, "Chapter failure.", e);
                        }
                        // publish results
                        publish(successCounter, failureCounter, resultDescription);
                        // check if task was cancelled if yes break loop
                        if (taskCancelled) {
                            break;
                        }
                    }
                } else { // crawling
                    String nextChapter = crawlingValues[2];
                    // initialize crawling
                    wcr.initializeCrawling(crawlingValues[1].isEmpty() ? null : Integer.valueOf(crawlingValues[1]),
                            crawlingValues[0]);
                    do {
                        try { // TODO: {Vordis 2019-05-20 21:00:15} repetition
                              // get chapterText and title
                            chapterValues = wcr.retrieveChapterCrawling(nextChapter);
                            try {
                                // save them into file depending on user choice(txt, epub or pdf)
                                fs.saveToFile(chapterValues);
                                // if all ok counter of success +1
                                successCounter++;
                                resultDescription = chapterValues[0] + strings.book_creating_list_success_entry
                                        + chapterValues[1].length();
                                bookSizeInCharacters += chapterValues[1].length() + chapterValues[0].length();
                            } catch (Throwable ex) { // soft crash - unable to save one of chapters it does not mean
                                                     // that we have to abort operation.
                                failureCounter++;
                                resultDescription = strings.book_creating_list_failure_entry + nextChapter + " -> "
                                        + ex.getMessage();
                                LOGGER.log(Level.SEVERE, "Chapter failure.", ex);
                            }
                        } catch (Throwable e) { // hard crash - unable to get values for chapter from web, on crawling
                                                // it means that we have to abort operation.
                            resultDescription = strings.book_creating_list_failure_entry + nextChapter + " -> "
                                    + e.getMessage();
                            // make sure that loop will break after publishing results
                            chapterValues = new String[] { null, null, null };
                            LOGGER.log(Level.SEVERE, "Chapter failure.", e);
                        }
                        // publish results
                        publish(successCounter, failureCounter, resultDescription);
                        // check if task was cancelled if yes break loop
                        if (taskCancelled) {
                            break;
                        }
                    } while ((nextChapter = chapterValues[2]) != null);
                }

                return null;
            }

            @Override
            protected void process(List<Object> chunks) {
                // update progressBar
                progressBar.setValue((int) chunks.get(0) + (int) chunks.get(1)); // progress bar = success + failure
                // update labels
                chaptersSuccessValueLabel.setText(chunks.get(0).toString());
                chaptersFailureValueLabel.setText(chunks.get(1).toString());
                // update list on success chapter title + number of characters, on failure
                // chapter address and exception message.
                ((DefaultListModel<Object>) progressDetails.getModel()).addElement(chunks.get(2));
            }

            @Override
            protected void done() {
                // show finish message and report
                WebBookDownloader.gui.showInformationDialog(strings.dialog_book_creating_report_title,
                        strings.dialog_book_creating_report_message + bookSizeInCharacters);
                // disable cancel button, enable back button
                backButton.setEnabled(true);
                cancelButton.setEnabled(false);
                // close file
                try {
                    fs.closeFile();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to close book file.", ex);
                    // ignore, we dont care that much about this operation success.
                }

            }

        }.execute();

        // listeners
        cancelButton.addActionListener((ActionEvent e) -> {
            // cancel task operation
            taskCancelled = true;
            // enable back button
            backButton.setEnabled(true);
        });
        backButton.addActionListener((ActionEvent e) -> {
            // back to select panel
            WebBookDownloader.gui.changePanel(parentPanel);
        });

        // add to panel
        // first row, components layed out with same gaps
        final Box b = Box.createHorizontalBox();
        b.add(chaptersSuccessTextLabel);
        b.add(chaptersSuccessValueLabel);
        b.add(Box.createHorizontalGlue());
        b.add(chaptersFailureTextLabel);
        b.add(chaptersFailureValueLabel);
        b.add(Box.createHorizontalGlue());
        b.add(chaptersTotalTextLabel);
        b.add(chaptersTotalValueLabel);
        add(b);
        add(progressBar);
        add(progressDetailsContainer);
        add(createHorizontallyCenteredComponent(new JComponent[] { cancelButton, backButton }));

    }

}
