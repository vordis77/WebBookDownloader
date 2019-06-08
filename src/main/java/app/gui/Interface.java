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
package app.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import app.gui.settings.SettingsItemActionListener;
import resources.Dimensions;
import static app.WebBookDownloader.programStrings;

/**
 * This class handles basic user- interface interactions, it also allows to
 * create program window and change it's content.
 *
 * @author marcin
 */
public class Interface {

    private final JFrame programFrame;

    /**
     * Creates instance of graphic user interface, creates frame.
     */
    public Interface() {
        this.programFrame = new JFrame(programStrings.programTitle);
    }

    /**
     * This method initialize frame and shows it.
     */
    public void initializeAndShowFrame() {
        programFrame.setPreferredSize(new Dimension(Dimensions.FRAME_WIDTH, Dimensions.FRAME_HEIGHT));
        programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // icon
        programFrame.setIconImage(
                new ImageIcon(System.getProperty("user.dir").concat(File.separator).concat("icon.jpg")).getImage());
        programFrame.setResizable(false);
        // menu bar
        final JMenuBar menuBar = new JMenuBar();
        // menus
        final JMenu programMenu = new JMenu(programStrings.menu_program_title),
                helpMenu = new JMenu(programStrings.menu_help_title);
        // menu items
        final JMenuItem closeItem = new JMenuItem(programStrings.menu_program_close_item),
                settingsItem = new JMenuItem(programStrings.menu_program_settings_item),
                aboutProgramItem = new JMenuItem(programStrings.menu_help_about_program_item);
        // menu items listeners
        closeItem.addActionListener((ActionEvent e) -> {
            // close program
            System.exit(0);
        });
        settingsItem.addActionListener(new SettingsItemActionListener(programFrame));
        aboutProgramItem.addActionListener((ActionEvent e) -> {
            // show dialog with information about program
            showInformationDialog(programStrings.menu_help_about_program_item, programStrings.about_program_message);
        });

        // add items into menus, menus into menubar, menubar into frame
        programMenu.add(settingsItem);
        programMenu.add(closeItem);
        helpMenu.add(aboutProgramItem);
        menuBar.add(programMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        programFrame.setJMenuBar(menuBar);
        // show frame
        programFrame.pack();
        programFrame.setVisible(true);
    }

    /**
     * This method allows you to show small information dialog to user.
     *
     * @param title   title of the dialog.
     * @param message message of the dialog, string or component.
     */
    public void showInformationDialog(String title, Object message) {
        JOptionPane.showMessageDialog(programFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Display new panel in frame.
     *
     * @param container panel.
     */
    public void changePanel(Container container) {
        programFrame.setContentPane(container);
        programFrame.pack();
    }

    /**
     * Get main frame of the program.
     *
     * @return main frame.
     */
    public JFrame getProgramFrame() {
        return programFrame;
    }

}
