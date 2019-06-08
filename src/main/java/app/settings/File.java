package app.settings;

import app.WebBookDownloader;

/**
 * File
 */
public interface File {

    /**
     * Path where program settings will be saved.
     */
    String settingsFilePath = WebBookDownloader.workingDirectoryPath.concat("settings.data");
    /**
     * Separator for settings label and values in file.
     */
    String settingSeparator = "=";
    
}