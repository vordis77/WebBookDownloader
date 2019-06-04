package app.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import app.WebBookDownloader;
import app.Settings.FieldDefinition;

import static app.WebBookDownloader.programStrings;

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
        final ArrayList<JComponent> components = new ArrayList<>();
        // first attach note
        components.add(new JLabel(programStrings.settings_note));
        // create gui setting object based on settings definitions.
        // also add them to components (holder)
        final ArrayList<Setting<?>> settings = new ArrayList<>();
        Setting<?> setting;
        Class<?> settingClass;
        for (FieldDefinition fieldDefinition : FieldDefinition.values()) {
            settingClass = null; // NOTE: there should be 0 possibility that null values stay after switch
            switch (fieldDefinition.getType()) {
            case CHOICES:
                settingClass = ChoiceSetting.class;
                break;
            case BOOLEAN:
                settingClass = BooleanSetting.class;
                break;
            }

            try {
                setting = (Setting<?>) settingClass.getConstructor(FieldDefinition.class).newInstance(fieldDefinition);
                settings.add(setting);
                if (setting.usesLabel()) { // add label if setting uses it.
                    components.add(((ChoiceSetting) setting).getLabel());
                }
                components.add(setting.createComponent()); // add component
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
                LOGGER.log(Level.SEVERE, "Failed to create settings components", exception);
            }
        }

        if (JOptionPane.showConfirmDialog(programFrame, components.toArray(), programStrings.menu_program_settings_item,
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // user confirmed choices - update global variables and settings file
            Iterator<Setting<?>> settingsIterator = settings.iterator();
            while (settingsIterator.hasNext()) {
                settingsIterator.next().updateUnderlyingSetting();
            }

            try {
                // save to file
                WebBookDownloader.saveSettings();
            } catch (IOException exception) {
                LOGGER.log(Level.SEVERE, "Failed to save settings", exception);
            }
        }
    }

}