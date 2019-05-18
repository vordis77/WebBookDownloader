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

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

import app.gui.components.Button;
import app.gui.components.Label;
import app.gui.components.Panel;
import app.gui.components.TextField;
import resources.Dimensions;
import resources.Settings;
import resources.strings.Strings;
import app.web.Chapter;
import app.web.LinksRetriever;
import app.WebBookDownloader;

/**
 * This panel allows user to choose range of chapters to create book.
 *
 * @author marcin
 */
public class ChapterSelectingPanel extends Panel {

    private final LinksRetriever wlr;
    private final Strings strings;

    public ChapterSelectingPanel() {
        this.wlr = new LinksRetriever();
        strings = Settings.programStrings;
    }

    @Override
    public void initializePanel() {
        // components
        final JTabbedPane tabbedPane = new JTabbedPane();
        final TextField indexAddressText = new TextField(),
                rangeStartAddressText = new TextField(),
                rangeEndAddressText = new TextField(),
                startAddressText = new TextField(),
                numberOfChaptersText = new TextField(),
                nextChapterLinkNameText = new TextField("Next Chapter");
        tabbedPane.addTab(strings.range_choosing_tab1, createHorizontallyCenteredComponent(new JComponent[]{
            new Label(strings.range_choosing_index_adress),
            indexAddressText
        }));
        tabbedPane.addTab(strings.range_choosing_tab2, createVerticallyCenteredComponent(new JComponent[]{
            createHorizontallyCenteredComponent(new JComponent[]{
                new Label(strings.range_choosing_adress_start),
                rangeStartAddressText
            }),
            createHorizontallyCenteredComponent(new JComponent[]{
                new Label(strings.range_choosing_adress_end),
                rangeEndAddressText
            }),})
        );
        tabbedPane.addTab(strings.range_choosing_tab3, createVerticallyCenteredComponent(new JComponent[]{
            createHorizontallyCenteredComponent(new JComponent[]{
                new Label(strings.range_choosing_adress_start),
                startAddressText
            }),
            createHorizontallyCenteredComponent(new JComponent[]{
                new Label(strings.range_choosing_number_of_chapters),
                numberOfChaptersText
            }),
            createHorizontallyCenteredComponent(new JComponent[]{
                new Label(strings.range_choosing_next_chapter_link_name),
                nextChapterLinkNameText
            }),
        })
        );
        final Button confirmButton = new Button(strings.range_choosing_confirm_button);
        // listeners
        confirmButton.addActionListener((ActionEvent e) -> {
            if (tabbedPane.getSelectedIndex() == 2) { // if crawling go straight to next panel
                askForFileNameAndProceedToBookCreatingPane(null, new String[]{
                    nextChapterLinkNameText.getText(),
                    numberOfChaptersText.getText(),
                    startAddressText.getText()
                }); 
            } else { // craete progress bar when loading chapters, allow selection of chapters after loading commenced
                final JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                progressBar.setPreferredSize(new Dimension(Dimensions.PROGRESS_BAR_WIDTH, Dimensions.PROGRESS_BAR_HEIGHT));
                final JDialog dialog = new JDialog(WebBookDownloader.gui.getProgramFrame(), "Doing Work...", false);
                // prevent user from closing manually
                enableComponents(this, false);
                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                dialog.setContentPane(progressBar);
                dialog.pack();
                dialog.setVisible(true);

                new SwingWorker<ArrayList<Chapter>, Object>() {

                    @Override
                    protected ArrayList<Chapter> doInBackground() throws Exception {
                        switch (tabbedPane.getSelectedIndex()) {
                            case 0: {// table of contents
                                try {
                                    return wlr.getChaptersFromTOC(indexAddressText.getText());
                                } catch (IOException ex) {
                                    if (ex instanceof MalformedURLException) { // bad url error
                                        WebBookDownloader.gui.showInformationDialog(strings.errorDialogTitle, strings.dialog_invalid_address_message + "\n" + ex.getMessage());
                                    } else { // io error
                                        WebBookDownloader.gui.showInformationDialog(strings.errorDialogTitle, strings.dialog_io_error_message + "\n" + ex.getMessage());
                                    }
                                    return null;
                                }
                            }
                            case 1: // range
                                return wlr.getChaptersFromRange(rangeStartAddressText.getText(), rangeEndAddressText.getText());
                        }
                        // should not occur
                        return null;
                    }

                    @Override
                    protected void done() {
                        dialog.setVisible(false);
                        enableComponents(ChapterSelectingPanel.this, true);
                        try {
                            confirmChaptersAndGoToCreatingBookPanel(get());
                        } catch (InterruptedException | ExecutionException ex) {
                            // should not occur
                        }
                    }

                }.execute();
            }

        });
        // add to panel
        add(tabbedPane);
        add(createHorizontallyCenteredComponent(confirmButton));
    }

    private void confirmChaptersAndGoToCreatingBookPanel(ArrayList<Chapter> chapters) {
        // check if chapters are not empty and are not null -- empty if not found links on site, null if any error occured while loading links
        if (chapters == null || chapters.isEmpty()) {
            return; // end prematurely
        }
        // components
        final Vector<Chapter> listContent = new Vector<>(chapters); // data
        final JList chapterList = new JList(listContent);
        final JScrollPane listContainer = new JScrollPane(chapterList);
        listContainer.setPreferredSize(new Dimension(Dimensions.FRAME_WIDTH, Dimensions.FRAME_HEIGHT));
        // show list in dialog
        if (JOptionPane.showConfirmDialog(WebBookDownloader.gui.getProgramFrame(), listContainer, strings.dialog_chapter_selection_title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // user confirmed - get selected chapters and open chapter crawlingPanel with them
            final ArrayList<Chapter> selectedChapters = new ArrayList<>(chapterList.getSelectedValuesList());
            // check if user selected chapters
            if (selectedChapters.isEmpty()) {
                WebBookDownloader.gui.showInformationDialog(strings.errorDialogTitle, strings.dialog_no_chapter_selected_message);
                return;
            }

            // ask for filename and go to next panel
            askForFileNameAndProceedToBookCreatingPane(selectedChapters, null);

        }
    }

    private void askForFileNameAndProceedToBookCreatingPane(ArrayList<Chapter> selectedChapters, String[] linkNames) {
        // ask for fileName and Path with extension depending on fileType
        final String extension;
        switch (Settings.fileType) {
            case Settings.FILE_EPUB:
                extension = ".epub";
                break;
            case Settings.FILE_TXT:
                extension = ".txt";
                break;
            case Settings.FILE_PDF:
                extension = ".pdf";
                break;
            default:
                extension = ".txt";
                break;
        }
        final FileDialog fd = new FileDialog(WebBookDownloader.gui.getProgramFrame(), strings.dialog_file_create, FileDialog.SAVE);
        fd.setFilenameFilter((File dir, String name) -> {
            return new File(dir.getAbsolutePath().concat(File.separator).concat(name)).isDirectory() || name.contains(extension); // show only folders or files with the same extension.
        });
        // set predefined name to book title+.extension
        fd.setFile(wlr.getBookTitle().concat(extension));
        fd.setVisible(true);
        String fileName;
        if ((fileName = fd.getFile()) != null && fd.getDirectory() != null) { // check if user selected and if yes if directory isn't corrupted.
            // check if user entered extension, probably not, so we need to add extension to name
            fileName = (fileName.contains(extension)) ? fileName : fileName.concat(extension);
            final String filePath = fd.getDirectory().concat(File.separator).concat(fileName);
            // open panel where book will be created.
            new BookCreatingPanel(selectedChapters, filePath, this, linkNames).showPanel();
        }
    }

    private void enableComponents(Object component, boolean enable) {
        final JComponent jComponent = (JComponent) component;
        if (jComponent.getComponentCount() > 0) {
            for (int i = 0; i < jComponent.getComponentCount(); i++) {
                enableComponents(jComponent.getComponent(i), enable);
            }
        } else {
            jComponent.setEnabled(enable);
        }
    }

}
