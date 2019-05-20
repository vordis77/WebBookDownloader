/*
 * Copyright (c) 2017, marcin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package app.gui.components;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import app.WebBookDownloader;

/**
 * Panel based on box layout.
 *
 * @author marcin
 */
public abstract class Panel extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(Panel.class.getName()); // TODO: {Vordis 2019-05-20 20:15:49}
                                                                                  // ugly, think about some injection

    public Panel() {
        super();
    }

    @Override
    public void doLayout() {
        super.setBackground(Color.DARK_GRAY);
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        super.doLayout(); // To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Add components to panel in this method.
     * 
     * @throws java.lang.Exception exception if fatal error occured while building
     *                             panel.
     */
    public abstract void initializePanel() throws Exception;

    /**
     * Show panel in main frame of the program.
     */
    public void showPanel() {
        try {
            initializePanel();
            WebBookDownloader.gui.changePanel(this);
        } catch (Exception e) {
            // do not show panel
            LOGGER.log(Level.SEVERE, "Failed to show panel.", e);
        }
    }

    /**
     * This method row with one component in center of it.
     *
     * @param component component.
     * @return row.
     */
    protected Box createHorizontallyCenteredComponent(JComponent component) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    /**
     * This method row with many components in center of it.
     *
     * @param components components.
     * @return row.
     */
    protected Box createHorizontallyCenteredComponent(JComponent[] components) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        for (JComponent component : components) {
            box.add(component);
        }
        box.add(Box.createHorizontalGlue());
        return box;
    }

    /**
     * This method row with one component in center of it.
     *
     * @param component component.
     * @return row.
     */
    protected Box createVerticallyCenteredComponent(JComponent component) {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue());
        box.add(component);
        box.add(Box.createVerticalGlue());
        return box;
    }

    /**
     * This method row with many components in center of it.
     *
     * @param components components.
     * @return row.
     */
    protected Box createVerticallyCenteredComponent(JComponent[] components) {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue());
        for (JComponent component : components) {
            box.add(component);
        }
        box.add(Box.createVerticalGlue());
        return box;
    }

}
