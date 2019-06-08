package app.gui.settings;

import javax.swing.JCheckBox;

import app.settings.Settings.FieldDefinition;

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
    protected Object getValue(JCheckBox component) {
        return component.isSelected();
    }

}