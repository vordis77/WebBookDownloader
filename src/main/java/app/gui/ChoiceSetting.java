package app.gui;

import java.lang.reflect.Field;

import javax.swing.JComboBox;

import resources.Settings;

/**
 * Choice setting object.
 */
public class ChoiceSetting {

    private final String[] options;
    private final String name;
    private JComboBox<String> component;

    public ChoiceSetting(String[] options, String name) {
        this.options = options;
        this.name = name;
    }

    /**
     * Create gui component for this setting.
     * 
     * @return gui component.
     */
    public JComboBox<String> createComponent() {
        this.component = new JComboBox<String>(this.options);
        // dynamically load setting value and select it
        try { 
            Field field = Settings.class.getField(this.name);
            Integer value = (Integer)field.get(null);
            component.setSelectedIndex(value);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            component.setSelectedIndex(0); // set first item on error.
        }

        return this.component;
    }

    /**
     * Update setting value, based on value in component.
     * @return true on successful update. (False is possible only on bad configuration - invalid name of field etc.)
     */
    public boolean updateUnderlyingSetting() {
        try { 
            Field field = Settings.class.getField(this.name);
            field.set(null, component.getSelectedIndex());
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            return false;
        }
    }
    

}