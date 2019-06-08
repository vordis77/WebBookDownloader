package app.gui.settings;

import javax.swing.JComponent;
import javax.swing.JLabel;

import app.settings.Settings;
import app.settings.Settings.FieldDefinition;

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

    protected abstract Object getValue(Component component);

    /**
     * Create gui component for this setting.
     * 
     * @return gui component.
     */
    public Component createComponent() {
        this.component = instantiateComponent();
        // dynamically load setting value and select it
        Object fieldValue = Settings.getFieldValue(this.fieldDefinition.getName());
        if (fieldValue != null) { // TODO: {Vordis 2019-06-05 20:00:42} we can react differently
            // I assume that this should hold true always
            // unless provided names are invalid, which shouldn't occur
            setDefaultValue(component, fieldValue);
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
        return Settings.setFieldValue(this.fieldDefinition.getName(), getValue(component));
    }

    /**
     * @return the usesLabel
     */
    public Boolean usesLabel() {
        return usesLabel;
    }

    /**
     * Create new or return existing label.
     * 
     * @return the label
     */
    public JLabel getLabel() {
        if (this.label != null) {
            return this.label;
        }

        return this.label = new JLabel(this.fieldDefinition.getLabel());
    }

}