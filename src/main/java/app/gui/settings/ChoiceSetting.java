package app.gui.settings;

import javax.swing.JComboBox;

import app.settings.Settings.FieldDefinition;

/**
 * ChoiceSetting
 */
public class ChoiceSetting extends Setting<JComboBox<String>> {

    public ChoiceSetting(FieldDefinition fieldDefinition) {
        super(fieldDefinition, true);
    }

    @Override
    protected JComboBox<String> instantiateComponent() {
        return new JComboBox<>(super.fieldDefinition.getChoices());
    }

    @Override
    protected void setDefaultValue(JComboBox<String> component, Object value) {
        component.setSelectedItem(value.toString());
    }

    @Override
    protected Object getValue(JComboBox<String> component) {
        return component.getSelectedItem();
    }

}