package app.gui;

import javax.swing.JCheckBox;

import app.Settings.FieldDefinition;

/**
 * ChoiceSetting
 */
public class BooleanSetting extends Setting<JCheckBox> {

    public BooleanSetting(FieldDefinition fieldDefinition) {
        super(fieldDefinition, false);
    }

    @Override
    protected JCheckBox instantiateComponent() {
        return new JCheckBox(super.fieldDefinition.getLabel());
    }

    @Override
    protected void setDefaultValue(JCheckBox component, Object value) {
        component.setSelected((Boolean) value);
    }

    @Override
    protected void setDefaultValueOnException(JCheckBox component) {
        component.setSelected(false); // TODO: {Vordis 2019-06-03 20:30:18} possible that it's unnecessary
    }

    @Override
    protected Object getValue(JCheckBox component) {
        return component.isSelected();
    }

}