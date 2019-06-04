package app.gui;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;

import app.Settings;
import app.Settings.FieldDefinition;

/**
 * Setting object - archetype.
 */
public abstract class Setting<Component extends JComponent> {

    protected FieldDefinition fieldDefinition;
    private Component component;
    private JLabel label;
    private final Boolean usesLabel;

    public Setting(FieldDefinition fieldDefinition, Boolean usesLabel) {
        this.fieldDefinition = fieldDefinition;
        this.usesLabel = usesLabel;
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

    /**
     * @return the usesLabel
     */
    public Boolean usesLabel() {
        return usesLabel;
    }

    /**
     * Create new or return existing label.
     * @return the label
     */
    public JLabel getLabel() {
        if (this.label != null) {
            return this.label;
        }
        
        return this.label = new JLabel(this.fieldDefinition.getLabel());
    }

}