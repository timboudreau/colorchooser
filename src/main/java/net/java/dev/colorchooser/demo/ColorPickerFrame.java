/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2000-2008 Tim Boudreau. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 */
/*
 * ColorPickerFrame.java
 *
 * Created on August 18, 2006, 1:23 PM
 */
package colorpicker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringWriter;
import java.security.AccessControlException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.dev.colorchooser.ColorChooser;

/**
 * A simple color calculator.
 *
 * @author  Tim Boudreau
 */
public class ColorPickerFrame extends javax.swing.JFrame implements DocumentListener, PropertyChangeListener, ListSelectionListener {
    
    private static Preferences prefs;
    private static boolean cantGetPreferences = false;
    private final DefaultListModel lmdl = new DefaultListModel();
    /** Creates new form ColorPickerFrame */
    public ColorPickerFrame() {
        initComponents();
        hexvalue.getDocument().addDocumentListener(this);
        colorChooser1.addPropertyChangeListener(this);
        colorChooser1.setFocusable(true);
        DL dl = new DL();
        rgbval.getDocument().addDocumentListener(dl);
        hsbval.getDocument().addDocumentListener(dl);
        
        errlbl.setText ("  ");
        
        Font f = bglabel.getFont();
        f = f.deriveFont(Font.BOLD, 15F);
        bglabel.setFont (f);
        fglabel.setFont (f);
        
        list.setModel(lmdl);
        list.setCellRenderer(new LRen());
        list.getSelectionModel().addListSelectionListener(this);
        
        if (!canShowHomeMenu()) {
            helpmenu.remove (webItem);
        }
        if (!restore()) {
            pack();
        }
    }
    
    static Preferences getPreferences() {
        if (prefs == null && !cantGetPreferences) {
            try {
                prefs = Preferences.userNodeForPackage(
                        ColorPickerFrame.class);
            } catch (AccessControlException e) {
                cantGetPreferences = true;
            }
        }
        return prefs;
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        colorChooser1.requestFocus();
    }
    
    @Override
    public void removeNotify() {
        try {
            store();
        } finally {
            super.removeNotify();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        colorChooser1 = new net.java.dev.colorchooser.ColorChooser();
        jLabel1 = new javax.swing.JLabel();
        hexvalue = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        fglabel = new javax.swing.JLabel();
        bglabel = new javax.swing.JLabel();
        errlbl = new javax.swing.JLabel();
        rgblbl = new javax.swing.JLabel();
        rgbval = new javax.swing.JTextField();
        hsbval = new javax.swing.JTextField();
        hsblbl = new javax.swing.JLabel();
        colorlbl = new javax.swing.JLabel();
        listpane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        addToListButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        instructions = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        filemenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        editmenu = new javax.swing.JMenu();
        cutItem = new javax.swing.JMenuItem();
        copyItem = new javax.swing.JMenuItem();
        pasteItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        addToListItem = new javax.swing.JMenuItem();
        clearList = new javax.swing.JMenuItem();
        helpmenu = new javax.swing.JMenu();
        webItem = new javax.swing.JMenuItem();
        aboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Color Calculator");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        colorChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChooser1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        getContentPane().add(colorChooser1, gridBagConstraints);

        jLabel1.setDisplayedMnemonic('a');
        jLabel1.setLabelFor(hexvalue);
        jLabel1.setText("Hex Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        hexvalue.setColumns(6);
        hexvalue.setText("0000FF");
        hexvalue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAddToList(evt);
            }
        });
        hexvalue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hexvalueFocusGained(evt);
            }
        });
        hexvalue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hexvalueKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hexvalueKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        getContentPane().add(hexvalue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(jSeparator1, gridBagConstraints);

        fglabel.setBackground(new java.awt.Color(255, 255, 255));
        fglabel.setForeground(new java.awt.Color(0, 0, 255));
        fglabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fglabel.setText("As Foreground");
        fglabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        getContentPane().add(fglabel, gridBagConstraints);

        bglabel.setBackground(new java.awt.Color(0, 0, 255));
        bglabel.setForeground(new java.awt.Color(255, 255, 255));
        bglabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bglabel.setText("As Background");
        bglabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        getContentPane().add(bglabel, gridBagConstraints);

        errlbl.setForeground(new java.awt.Color(255, 0, 0));
        errlbl.setText("error msg here");
        errlbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(errlbl, gridBagConstraints);

        rgblbl.setDisplayedMnemonic('G');
        rgblbl.setLabelFor(rgbval);
        rgblbl.setText("RGB Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        getContentPane().add(rgblbl, gridBagConstraints);

        rgbval.setText("0, 0, 255");
        rgbval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAddToList(evt);
            }
        });
        rgbval.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rgbvalFocusGained(evt);
            }
        });
        rgbval.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                rgbvalKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        getContentPane().add(rgbval, gridBagConstraints);

        hsbval.setText("0.6666, 1.0, 1.0");
        hsbval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAddToList(evt);
            }
        });
        hsbval.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hsbvalFocusGained(evt);
            }
        });
        hsbval.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hsbvalKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 130;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        getContentPane().add(hsbval, gridBagConstraints);

        hsblbl.setLabelFor(hsbval);
        hsblbl.setText("HSB Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(hsblbl, gridBagConstraints);

        colorlbl.setDisplayedMnemonic('C');
        colorlbl.setText("Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(colorlbl, gridBagConstraints);

        listpane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listpane.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 60;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(listpane, gridBagConstraints);

        addToListButton.setMnemonic('A');
        addToListButton.setText("Add to List");
        addToListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAddToList(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(addToListButton, gridBagConstraints);

        jButton1.setText("Clear List");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        instructions.setText("<html>Click the color swatch or enter text to select a color.  Press combinations of ctrl, shift and alt while dragging to view different color palettes.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        getContentPane().add(instructions, gridBagConstraints);

        filemenu.setMnemonic('F');
        filemenu.setText("File");

        exitItem.setMnemonic('x');
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        filemenu.add(exitItem);

        jMenuBar1.add(filemenu);

        editmenu.setMnemonic('E');
        editmenu.setText("Edit");

        cutItem.setMnemonic('u');
        cutItem.setText("Cut");
        cutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutItemActionPerformed(evt);
            }
        });
        editmenu.add(cutItem);

        copyItem.setMnemonic('C');
        copyItem.setText("Copy");
        copyItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyItemActionPerformed(evt);
            }
        });
        editmenu.add(copyItem);

        pasteItem.setMnemonic('P');
        pasteItem.setText("Paste");
        pasteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteItemActionPerformed(evt);
            }
        });
        editmenu.add(pasteItem);
        editmenu.add(jSeparator2);

        addToListItem.setMnemonic('A');
        addToListItem.setText("Add to List");
        addToListItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAddToList(evt);
            }
        });
        editmenu.add(addToListItem);

        clearList.setMnemonic('l');
        clearList.setText("Clear List");
        clearList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        editmenu.add(clearList);

        jMenuBar1.add(editmenu);

        helpmenu.setMnemonic('H');
        helpmenu.setText("Help");

        webItem.setMnemonic('W');
        webItem.setText("Web Site");
        webItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webItemActionPerformed(evt);
            }
        });
        helpmenu.add(webItem);

        aboutItem.setMnemonic('A');
        aboutItem.setText("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpmenu.add(aboutItem);

        jMenuBar1.add(helpmenu);

        setJMenuBar(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        lmdl.clear();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void performAddToList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_performAddToList
        int index = indexOfColorInList();
        if (index == -1) {
            lmdl.add (0, colorChooser1.getColor());
            list.setSelectedIndex (0);
        } else {
            list.setSelectedIndex(index);
        }
    }//GEN-LAST:event_performAddToList

    private void hsbvalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hsbvalKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != ',' && c != ' ' && c != '.') {
            evt.consume();
        }
    }//GEN-LAST:event_hsbvalKeyTyped

    private void hsbvalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hsbvalFocusGained
        hsbval.selectAll();
    }//GEN-LAST:event_hsbvalFocusGained

    private void rgbvalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rgbvalKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != ',' && c != ' ') {
            evt.consume();
        }
    }//GEN-LAST:event_rgbvalKeyTyped

    private void rgbvalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rgbvalFocusGained
        rgbval.selectAll();
    }//GEN-LAST:event_rgbvalFocusGained

    private void webItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webItemActionPerformed
        launchBrowser();
    }//GEN-LAST:event_webItemActionPerformed

    private void hexvalueFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hexvalueFocusGained
        hexvalue.selectAll();
    }//GEN-LAST:event_hexvalueFocusGained

    private void hexvalueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hexvalueKeyTyped
        char c = Character.toUpperCase(evt.getKeyChar());
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case '\t':
                evt.setKeyChar(Character.toUpperCase(c));
                break;
            default :
                evt.consume();
        }
    }//GEN-LAST:event_hexvalueKeyTyped

    private void hexvalueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hexvalueKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_hexvalueKeyPressed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        if (!isShowing()) {
            return;
        }
        queueStoreNewLocation();
    }//GEN-LAST:event_formComponentMoved

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (!isShowing()) {
            return;
        }
        queueStoreNewLocation();
    }//GEN-LAST:event_formComponentResized

    private void pasteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteItemActionPerformed
        hexvalue.paste();
    }//GEN-LAST:event_pasteItemActionPerformed

    private void copyItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyItemActionPerformed
        //make it clear that it's the hex value being copied
        if (hexvalue.getSelectionStart() == hexvalue.getSelectionEnd()) {
            hexvalue.selectAll();
        }
        if (!hexvalue.hasFocus()) {
            hexvalue.requestFocusInWindow();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                hexvalue.copy();
            }
        });
    }//GEN-LAST:event_copyItemActionPerformed

    private void cutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutItemActionPerformed
        //make it clear that it's the hex value being copied
        if (hexvalue.getSelectionStart() == hexvalue.getSelectionEnd()) {
            hexvalue.selectAll();
        }
        if (!hexvalue.hasFocus()) {
            hexvalue.requestFocusInWindow();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                hexvalue.cut();
            }
        });
    }//GEN-LAST:event_cutItemActionPerformed

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
        
        String s = "<html><center><b>Color Calculator</b> by " +
                "Tim Boudreau (tboudreau@sun.com)<p> " +
                "<a href=\"http://colorchooser.dev.java.net\">http://colorchooser.dev.java.net</a> for updates.";
        JLabel lbl = new JLabel(s);
        lbl.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseReleased (MouseEvent me) {
                JLabel lbl = (JLabel) me.getSource();
                if (lbl.contains (me.getPoint())) {
                    Container c = lbl.getTopLevelAncestor();
                    if (launchBrowser() && c instanceof Dialog) {
                        c.setVisible(false);
                        ((Dialog)c).dispose();
                    }
                }
            }
        });
        JOptionPane.showMessageDialog(this, lbl);
    }//GEN-LAST:event_aboutItemActionPerformed

    private boolean launchBrowser() {
        try {
            if (isMac()) {
                Runtime.getRuntime().exec("open http://colorchooser.dev.java.net");
                return true;
            } else if (isWindows()) {
                Runtime.getRuntime().exec("cmd /c start http://colorchooser.dev.java.net");
                return true;
            }
        } catch (IOException ioe) {
            setError(ioe.getMessage());
        }
        return false;
    }
    
    boolean canShowHomeMenu() {
        return isMac(); //for now
    }
    
    boolean isWindows() {
        String s = System.getProperty ("os.name");
        return s != null && s.toUpperCase().indexOf ("WINDOWS") >= 0;
    }
    
    boolean isMac() {
        return System.getProperty("mrj.version") != null;
    }
    
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    boolean inColorChange = false;
    private void colorChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorChooser1ActionPerformed
        if (changing) {
            return;
        }
        changing = true;
        inColorChange = true;
        try {
            Color c = colorChooser1.getColor();
            setError(setColor (c));
        } finally {
            inColorChange = false;
            changing = false;
        }
        queueStoreNewLocation();
    }//GEN-LAST:event_colorChooser1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        if (args.length == 1 && "reset".equals(args[0])) {
            Preferences prefs = getPreferences();
            if (prefs != null) {
                try {
                    prefs.clear();
                } catch (BackingStoreException e) {
                    e.printStackTrace();
                }
            }
        }
        runIt();
    }
    
    static JFrame runIt() {
        try {
            UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //do nothing
        }
        final JFrame frm = new ColorPickerFrame();
        frm.setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frm.setVisible(true);
            }
        });
        return frm;
    }

    private boolean changing = false;
    private void txtChange() {
        if (changing) {
            return;
        }
        fromTextChange = true;
        try {
            setError(setColor (hexvalue.getText().trim()));
        } finally {
            fromTextChange = false;
        }
    }
    
    public String setColor (Color c) {
        if (c == null) c = Color.BLACK;
        changing = true;
        try {
            fglabel.setForeground(c);
            bglabel.setBackground(c);
            final float[] vals = new float[3];
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), vals);
            if (vals[2] > 0.5) {
                bglabel.setForeground(Color.BLACK);
            } else {
                bglabel.setForeground (Color.WHITE);
            }
            String r = pad (Integer.toString(c.getRed(), 16).toUpperCase());
            String g = pad (Integer.toString(c.getGreen(), 16).toUpperCase());
            String b = pad (Integer.toString(c.getBlue(), 16).toUpperCase());
            if (!inColorChange) {
                colorChooser1.setColor(c);
            }
            if (!fromTextChange) {
                hexvalue.setText(r + g + b);
            }
            if (!inRgbTextChange) {
                String rgb = "" + c.getRed() + ", " + c.getGreen() + ", " 
                        + c.getBlue();
                rgbval.setText(rgb);
            }
            if (!inHsbTextChange) {
                float[] hsb = new float[3];
                hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
                String h = trim (hsb[0]);
                String s = trim (hsb[1]);
                String b1 = trim (hsb[2]);
                hsbval.setText (h + ", " + s + ", " + b1);
            }
            int index = indexOfColorInList();
            if (index >= 0) {
                list.setSelectedIndex(index);
            } else {
                list.clearSelection();
            }

            queueStoreNewLocation();
            return " ";
        } finally {
            changing = false;
        }
    }
    
    private String trim (float f) {
        String s = Float.toString(f);
        if (s.length() > 6) {
            s = s.substring (0, 6);
        }
        return s;
    }
    
    static String pad (String s) {
        while (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }
    
    private boolean fromTextChange = false;
    
    public String setColor (String s) {
        if (s.length() != 6) {
            return "Color must have 6 characters - '" + s + "' has " + s.length();
        }
        String r = s.substring(0, 2);
        String g = s.substring(2, 4);
        String b = s.substring(4);
        try {
            int rval = Integer.parseInt(r, 16);
            int gval = Integer.parseInt(g, 16);
            int bval = Integer.parseInt(b, 16);
            Color c = new Color (rval, gval, bval);
            setColor (c);
        } catch (NumberFormatException nfe) {
            return "Not legal hexadecimal: " + s;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return " ";
    }
    
    public void insertUpdate(DocumentEvent e) {
        txtChange();
    }

    public void removeUpdate(DocumentEvent e) {
        txtChange();
    }

    public void changedUpdate(DocumentEvent e) {
        txtChange();
    }
    
    private void setError (String s) {
        errlbl.setText(s);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        inColorChange = true;
        try {
            if (ColorChooser.PROP_TRANSIENT_COLOR.equals(evt.getPropertyName())) {
                setColor ((Color) evt.getNewValue());
            } else if (ColorChooser.PROP_COLOR.equals (evt.getPropertyName())) {
                setColor ((Color) evt.getNewValue());
            }
        } finally {
            inColorChange = false;
        }
    }
 
    private Timer timer = null;
    private void queueStoreNewLocation() {
        if (timer != null) {
            timer.restart();
            return;
        }
        timer = new Timer (1000, new AL());
        timer.setRepeats(false);
    }
    
    void store() {
        Rectangle r = getBounds();
        Preferences prefs = getPreferences();
        if (prefs == null) return;
        prefs.putInt("x", r.x);
        prefs.putInt("y", r.y);
        prefs.putInt("w", r.width);
        prefs.putInt("h", r.height);
        Color c = colorChooser1.getColor();
        prefs.putInt("r", c.getRed());
        prefs.putInt ("g", c.getGreen());
        prefs.putInt ("b", c.getBlue());
        StringBuffer sb = new StringBuffer();
        int max = lmdl.getSize();
        for (int i=0; i < max; i++) {
            Color color = (Color) lmdl.elementAt(i);
            sb.append (Integer.toString(color.getRGB()));
            if (i != max - 1) {
                sb.append (',');
            }
        }
        prefs.put ("colors", sb.toString());
    }
    
    private class AL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            timer = null;
            store();
        }
    }
    
    private boolean restore() {
        Preferences prefs = getPreferences();
        int x = prefs.getInt("x", -1);
        int y = prefs.getInt("y", -1);
        int w = prefs.getInt("w", -1);
        int h = prefs.getInt("h", -1);
        
        int r = prefs.getInt("r", -1);
        int g = prefs.getInt("g", -1);
        int b = prefs.getInt("b", -1);
        
        if (r != -1 && g != -1 && b != -1 && r <= 255 && g <= 255 && b <= 255) {
            Color c = new Color (r, g, b);
            setColor (c);
        }
        
        boolean result = x != -1 && y != -1 && w != -1 && h != -1;
        if (result) {
            Rectangle rect = new Rectangle(x, y, w, h);
            int ww = getGraphicsConfiguration().getDevice().getDisplayMode().getWidth();
            int hh = getGraphicsConfiguration().getDevice().getDisplayMode().getHeight();
            Rectangle screen = new Rectangle (0, 0, ww, hh);
            result = screen.contains(rect);
            if (result) {
                setBounds (rect);
            }
        }
        
        String colors = prefs.get("colors", null);
        if (colors != null) {
            String[] cc = colors.split(",");
            try {
                for (int i = 0; i < cc.length; i++) {
                    Color color = new Color (Integer.parseInt(cc[i]));
                    lmdl.addElement(color);
                }
            } catch (Exception e) {
                System.err.println("Bad saved color list: " + colors);
            }
        }
        return result;
    }
    
    boolean inRgbTextChange = false;
    private void rgbTextChange() {
        String s = rgbval.getText().trim();
        char[] c = s.toCharArray();
        StringWriter sb = new StringWriter();
        int ix = 0;
        String[] strings = new String[3];
        for (int i = 0; i < c.length; i++) {
            boolean added = false;
            if (Character.isDigit(c[i])) {
                sb.append (c[i]);
                added = true;
            } else {
                added = false;
            }
            if (!added || i == c.length-1) {
                String curr = sb.toString();
                sb = new StringWriter();
                if (curr.length() > 0) {
                    strings[ix] = curr;
                    ix++;
                    if (ix == 3) {
                        break;
                    }
                }
            }
        }
        if (ix < 3) {
            if (ix == 0) {
                setError ("Enter a red, green and blue values for a color");
            } else {
                setError ("Only " + ix + " out of red, green and blue provided");
            }
            return;
        }
        inRgbTextChange = true;
        String curr = null;
        try {
            int[] rgb = new int[3];
            for (int i = 0; i < strings.length; i++) {
                curr = strings[i];
                rgb[i] = Integer.parseInt (curr);
                if (rgb[i] < 0) {
                    setError ("Negative value: " + rgb[i]);
                    return;
                } else if (rgb[i] > 255) {
                    setError ("Color component value must be <= 255");
                    return;
                }
            }
            Color color = new Color (rgb[0], rgb[1], rgb[2]);
            setError(setColor (color));
        } catch (NumberFormatException e) {
            setError ("Not a number: " + curr);
            return;
        } finally {
            inRgbTextChange = false;
        }
    }
    
    boolean inHsbTextChange = false;
    private void hsbTextChange() {
        String s = hsbval.getText().trim();
        char[] c = s.toCharArray();
        StringWriter sb = new StringWriter();
        int ix = 0;
        String[] strings = new String[3];
        for (int i = 0; i < c.length; i++) {
            boolean added = false;
            if (Character.isDigit(c[i]) || c[i] == '.') {
                sb.append (c[i]);
                added = true;
            } else {
                added = false;
            }
            if (!added || i == c.length-1) {
                String curr = sb.toString();
                sb = new StringWriter();
                if (curr.length() > 0) {
                    strings[ix] = curr;
                    ix++;
                    if (ix == 3) {
                        break;
                    }
                }
            }
        }
        if (ix < 3) {
            if (ix == 0) {
                setError ("Enter a hue, saturation and brightness for a color");
            } else {
                setError ("Only " + ix + " out of hue, saturation and brightness provided");
            }
            return;
        }
        inHsbTextChange = true;
        String curr = null;
        try {
            float[] hsb = new float[3];
            for (int i = 0; i < strings.length; i++) {
                curr = strings[i];
                hsb[i] = Float.parseFloat (curr);
                if (hsb[i] < 0) {
                    setError ("HSB values must be between 0 and 1.0" + hsb[i]);
                    return;
                } else if (hsb[i] > 1.0) {
                    setError ("HSB color component value must be <= 1.0 -- " + hsb[i]);
                    return;
                }
            }
            Color color = new Color (Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
            setError(setColor (color));
        } catch (NumberFormatException e) {
            setError ("Not a number: " + curr);
            return;
        } catch (IllegalArgumentException ee) {
            setError (ee.getMessage());
            return;
        } finally {
            inHsbTextChange = false;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        int ix = list.getSelectedIndex();
        if (ix != -1 && !changing) {
            setColor ((Color) list.getSelectedValue());
        }
    }
    
    public int indexOfColorInList() {
        Color color = colorChooser1.getColor();
        int max = lmdl.getSize();
        for (int i = 0; i < max; i++) {
            Color c = (Color) lmdl.getElementAt(i);
            if (color.equals(c)) {
                return i;
            }
        }
        return -1;
    }
    
    private class DL implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            if (e.getDocument() == rgbval.getDocument()) {
                rgbTextChange();
            } else {
                hsbTextChange();
            }
        }

        public void removeUpdate(DocumentEvent e) {
            if (e.getDocument() == rgbval.getDocument()) {
                rgbTextChange();
            } else {
                hsbTextChange();
            }
        }

        public void changedUpdate(DocumentEvent e) {
            if (e.getDocument() == rgbval.getDocument()) {
                rgbTextChange();
            } else {
                hsbTextChange();
            }
        }
    }
    
    private static final ColorIcon CI = new ColorIcon();
    private static class LRen extends DefaultListCellRenderer {
        LRen() {
            setIcon (CI);
            setBorder (BorderFactory.createEmptyBorder(3,3,3,3));
        }
        public void propertyChange (String s, Object a, Object b) {} //performance

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component result = super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            Color c = (Color) value;
            CI.color = c;
            setIcon (CI);
            String r = pad (Integer.toString(c.getRed(), 16).toUpperCase());
            String g = pad (Integer.toString(c.getGreen(), 16).toUpperCase());
            String b = pad (Integer.toString(c.getBlue(), 16).toUpperCase());
            setIconTextGap(5);
            String nm = ColorChooser.getColorName(c);
            if (nm == null) {
                setText (r + g + b);
            } else {
                setText (nm + " - " + r + g + b);
            }
            
            return result;
        }
        
        @Override
        public Dimension getPreferredSize() {
            Dimension result = super.getPreferredSize();
            result.height = Math.max (22, result.height);
            return result;
        }
        
    }
    
    private static class ColorIcon implements Icon {
        Color color = Color.BLACK;
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            Insets ins = ((JComponent)c).getInsets();
            int w = Math.min (getIconWidth(), c.getWidth() - (3 + ins.left));
            int h = Math.min (getIconWidth(), c.getHeight() - (3 + ins.top)) - 1;
            g.setColor (color);
            g.fillRect (ins.left + 1, ins.top + 1, w, h);
            g.setColor (Color.BLACK);
            g.drawRect (ins.left + 1, ins.top + 1, w, h);
        }

        public int getIconWidth() {
            return 20;
        }

        public int getIconHeight() {
            return 14;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JButton addToListButton;
    private javax.swing.JMenuItem addToListItem;
    private javax.swing.JLabel bglabel;
    private javax.swing.JMenuItem clearList;
    private net.java.dev.colorchooser.ColorChooser colorChooser1;
    private javax.swing.JLabel colorlbl;
    private javax.swing.JMenuItem copyItem;
    private javax.swing.JMenuItem cutItem;
    private javax.swing.JMenu editmenu;
    private javax.swing.JLabel errlbl;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JLabel fglabel;
    private javax.swing.JMenu filemenu;
    private javax.swing.JMenu helpmenu;
    private javax.swing.JTextField hexvalue;
    private javax.swing.JLabel hsblbl;
    private javax.swing.JTextField hsbval;
    private javax.swing.JLabel instructions;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JList list;
    private javax.swing.JScrollPane listpane;
    private javax.swing.JMenuItem pasteItem;
    private javax.swing.JLabel rgblbl;
    private javax.swing.JTextField rgbval;
    private javax.swing.JMenuItem webItem;
    // End of variables declaration//GEN-END:variables
    
}
