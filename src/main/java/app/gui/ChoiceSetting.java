package app.gui;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import app.Settings.FieldDefinition;

/**
 * ChoiceSetting
 */
public class ChoiceSetting extends Setting<JComboBox<String>> {
    private JLabel label;

    public ChoiceSetting(FieldDefinition fieldDefinition, String labelText) {
        super(fieldDefinition, labelText);
    }

    @Override
    protected JComboBox<String> instantiateComponent() {
        this.label = new JLabel(super.labelText);
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

    /**
     * @return the label
     */
    public JLabel getLabel() {
        return label;
    }

}