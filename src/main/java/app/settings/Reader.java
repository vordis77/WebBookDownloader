package app.settings;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reader
 */
public class Reader implements app.settings.File {

    private static final Logger LOGGER = Logger.getLogger(Saver.class.getName());
    
    /**
     * This method loads data from settings file into settings (global fields).
     * @return true on success.
     */
    public boolean loadSettings() {
        try (Scanner scan = new Scanner(new File(settingsFilePath))) { // open file
            scan.useDelimiter("\r\n");
            String lineSplit[]; // 0 - name, 1 - value
            while (scan.hasNextLine()) {
                try {
                    lineSplit = scan.next().split(settingSeparator);
                    Settings.setFieldValue(lineSplit[0], lineSplit[1]);
                } catch (Throwable e) {
                    LOGGER.log(Level.SEVERE, "Failed to load one of the fields in settings.", e);
                }
            }
            return true;
        } catch (Throwable e) { // if data invalid do nothing, settings have default values
            LOGGER.log(Level.SEVERE, "Failed to load settings.", e);
            return false;
        }
    }
}