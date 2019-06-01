package app.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import app.WebBookDownloader;
import resources.Settings;
import static resources.Settings.programStrings;

/**
 * SettingsItemAction
 */
public class SettingsItemActionListener implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(SettingsItemActionListener.class.getName());
    private final JFrame programFrame;

    /**
     * Create listener for settings menu item.
     * 
     * @param programFrame main frame of the program graphic interface.
     */
    public SettingsItemActionListener(JFrame programFrame) {
        this.programFrame = programFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // show confirm dialog, which allows user to change settings. On ok save
        // settings file.
        // language label + comboBox
        final JComboBox<String> languageBox = new JComboBox<>(new String[] { "english", "polski" });
        // book type
        final JComboBox<String> bookTypeBox = new JComboBox<>(new String[] { "TXT", "EPUB", "PDF" });
        // paragraph element
        final JComboBox<String> htmlElementBox = new JComboBox<>(new String[] { "<p", "<a", "<br", "<span", });
        // encoding label + comboBox
        final JComboBox<Object> encodingBox = new JComboBox<>(Charset.availableCharsets().keySet().toArray());
        // font label + comboBox
        String[] fontList;
        try {
            fontList = new File(Settings.workingDirectoryPath.concat("fonts")).list();
        } catch (Exception exception) {
            fontList = new String[0];
        }
        final JComboBox<String> fontBox = new JComboBox<>(fontList);
        // reverse title checkbox
        final JCheckBox titleAtTheEndCheckBox = new JCheckBox(programStrings.settings_title_at_the_end);

        // select current values from global settings
        languageBox.setSelectedIndex(Settings.programLanguage);
        bookTypeBox.setSelectedIndex(Settings.fileType);
        htmlElementBox.setSelectedItem(Settings.chapterParagraphContainer);
        encodingBox.setSelectedItem(Settings.encoding);
        fontBox.setSelectedItem(Settings.pdfFontFile);
        titleAtTheEndCheckBox.setSelected(Settings.titleAtTheEnd);

        // container
        final JComponent[] components = new JComponent[] { new JLabel(programStrings.settings_note),
                new JLabel(programStrings.settings_language), languageBox, new JLabel(programStrings.settings_format),
                bookTypeBox, new JLabel(programStrings.settings_html_element), htmlElementBox,
                new JLabel(programStrings.settings_website_encoding), encodingBox,
                new JLabel(programStrings.settings_pdf_font), fontBox, titleAtTheEndCheckBox };

        if (JOptionPane.showConfirmDialog(programFrame, components, programStrings.menu_program_settings_item,
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // user confirmed choices - update global variables and settings file
            Settings.programLanguage = languageBox.getSelectedIndex();
            Settings.fileType = bookTypeBox.getSelectedIndex();
            Settings.chapterParagraphContainer = htmlElementBox.getSelectedItem().toString();
            Settings.encoding = encodingBox.getSelectedItem().toString();
            Settings.pdfFontFile = fontBox.getSelectedItem().toString();
            Settings.titleAtTheEnd = titleAtTheEndCheckBox.isSelected();
            try {
                // save to file
                WebBookDownloader.saveSettings();
            } catch (IOException exception) {
                LOGGER.log(Level.SEVERE, "Failed to save settings", exception);
            }
        }
    }

}