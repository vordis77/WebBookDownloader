package app.settings;

import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.settings.Settings.FieldDefinition;

/**
 * Saver
 */
public class Saver implements File {

    private static final Logger LOGGER = Logger.getLogger(Saver.class.getName());
    
    /**
     * Save settings in file
     * @return true on success.
     */
    public boolean storeInFile() {
        try (FileWriter fw = new FileWriter(settingsFilePath)) {
            for (FieldDefinition fieldDefinition : FieldDefinition.values()) {
                fw.append(fieldDefinition.getName() + settingSeparator + Settings.getFieldValue(fieldDefinition.getName()) + "\r\n");
            }
            fw.flush();
            return true;
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Failed to save settings.", e);
            return false;
        }
    }
}