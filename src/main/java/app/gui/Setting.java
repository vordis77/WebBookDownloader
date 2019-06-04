package app.gui;

import java.lang.reflect.Field;

import javax.swing.JComponent;

import app.Settings;
import app.Settings.FieldDefinition;

/**
 * Setting object - archetype.
 */
public abstract class Setting<Component extends JComponent> {

    protected FieldDefinition fieldDefinition;
    private Component component;
    protected final String labelText;

    public Setting(FieldDefinition fieldDefinition, String labelText) {
        this.fieldDefinition = fieldDefinition;
        this.labelText = labelText;
    }

    protected abstract Component instantiateComponent();

    protected abstract void setDefaultValue(Component component, Object value);

    protected abstract void setDefaultValueOnException(Component component);

    protected abstract Object getValue(Component component);

    /**
     * Create gui component for this setting.
     * 
     * @return gui component.
     */
    public Component createComponent() {
        this.component = instantiateComponent();
        // dynamically load setting value and select it
        try {
            Field field = Settings.Fields.class.getField(this.fieldDefinition.getName());
            Object value = field.get(null);
            setDefaultValue(component, value);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            setDefaultValueOnException(component);
        }

        return this.component;
    }

    /**
     * Update setting value, based on value in component.
     * 
     * @return true on successful update. (False is possible only on bad
     *         configuration - invalid name of field etc.)
     */
    public boolean updateUnderlyingSetting() {
        try {
            Field field = Settings.Fields.class.getField(this.fieldDefinition.getName());
            field.set(null, getValue(component));
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            return false;
        }
    }

}