package app.gui;

import javax.swing.JComboBox;

import app.Settings.FieldDefinition;

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
        component.setSelectedItem((String) value);
    }

    @Override
    protected void setDefaultValueOnException(JComboBox<String> component) {
        component.setSelectedIndex(0); // TODO: {Vordis 2019-06-03 20:30:18} possible that it's unnecessary
    }

    @Override
    protected Object getValue(JComboBox<String> component) {
        return component.getSelectedItem();
    }

}