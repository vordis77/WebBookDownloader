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
        String[] fontList;
        try {
            fontList = new File(Settings.workingDirectoryPath.concat("fonts")).list();
        } catch (Exception exception) {
            fontList = new String[0];
        }
        final ChoiceSetting languageSetting = new ChoiceSetting(new String[] { "english", "polski" },
                "programLanguage"),
                bookTypeSetting = new ChoiceSetting(new String[] { "TXT", "EPUB", "PDF" }, "bookType"),
                htmlElementSetting = new ChoiceSetting(new String[] { "<p", "<a", "<br", "<span" }, "htmlElement"),
                encodingSetting = new ChoiceSetting(Charset.availableCharsets().keySet().toArray(new String[0]),
                        "encoding"),
                fontSetting = new ChoiceSetting(fontList, "font");

        // show confirm dialog, which allows user to change settings. On ok save
        // settings file.
        // reverse title checkbox
        final JCheckBox titleAtTheEndCheckBox = new JCheckBox(programStrings.settings_title_at_the_end);

        // select current values from global settings
        titleAtTheEndCheckBox.setSelected(Settings.titleAtTheEnd);

        // container
        final JComponent[] components = new JComponent[] { new JLabel(programStrings.settings_note),
                new JLabel(programStrings.settings_language), languageSetting.createComponent(),
                new JLabel(programStrings.settings_format), bookTypeSetting.createComponent(),
                new JLabel(programStrings.settings_html_element), htmlElementSetting.createComponent(),
                new JLabel(programStrings.settings_website_encoding), encodingSetting.createComponent(),
                new JLabel(programStrings.settings_pdf_font), fontSetting.createComponent(), titleAtTheEndCheckBox };

        if (JOptionPane.showConfirmDialog(programFrame, components, programStrings.menu_program_settings_item,
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // user confirmed choices - update global variables and settings file
            languageSetting.updateUnderlyingSetting();
            bookTypeSetting.updateUnderlyingSetting();
            htmlElementSetting.updateUnderlyingSetting();
            encodingSetting.updateUnderlyingSetting();
            fontSetting.updateUnderlyingSetting();
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