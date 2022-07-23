/*
 * Copyright 2010-2019 Tim Boudreau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/**
 * Default UI delegate for color choosers. CCBorder handles minor per-look and
 * feel differences so we can use one delegate. Note this delegate is stateless
 * - there is one instance system-wide. State is kept by the listener - see
 * inner class L.
 *
 * @author Tim Boudreau
 */
final class DefaultColorChooserUI extends ColorChooserUI {

    DefaultColorChooserUI() {
    }

    private static DefaultColorChooserUI INSTANCE = null;

    public static ComponentUI createUI(JComponent jc) {
//        assert jc instanceof ColorChooser;
        return getDefault();
    }

    static DefaultColorChooserUI getDefault() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultColorChooserUI();
        }
        return INSTANCE;
    }

    @Override
    protected void init(ColorChooser c) {
        Font f = UIManager.getFont("controlFont");
        if (f == null) {
            f = UIManager.getFont("Label.font");
        }
        if (f == null) {
            f = new JLabel().getFont();
        }
        String uiFontSize = System.getProperty("uiFontSize");
        if (uiFontSize != null) {
            try {
                f = f.deriveFont(Float.parseFloat(uiFontSize));
            } catch (NumberFormatException nfe) {
                // do nothing
            }
        }
        c.setFont(f);
        c.setToolTipText(getDefaultTooltip());
        c.setBorder(new CCBorder());
        c.setFocusable(true);
        c.setOpaque(true);
        ActionMap am = c.getActionMap();
        InputMap im = c.getInputMap();
        KeyStroke copy1 = isMac()
                ? KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK)
                : KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);

        KeyStroke copy2 = KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0);

        KeyStroke paste1 = isMac()
                ? KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK)
                : KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke paste2 = KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0);

        im.put(copy1, "copy");
        im.put(copy2, "copy");
        am.put("copy", new CcCopyAction(c));

        im.put(paste1, "paste");
        im.put(paste2, "paste");
        am.put("paste", new CcPasteAction(c));

        c.addPropertyChangeListener("font", FONT_CHANGE);
    }

    private static final FontChangeListener FONT_CHANGE = new FontChangeListener();

    static final class FontChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof ColorChooser && "font".equals(evt.getPropertyName())) {
                ColorChooser cc = (ColorChooser) evt.getSource();
                cc.invalidate();
                cc.revalidate();
                cc.repaint();
            }
        }
    }

    private static Boolean MAC;

    static boolean isMac() {
        if (MAC != null) {
            return MAC;
        }
        return MAC = System.getProperty("os.name", "x")
                .toLowerCase().contains("darwin")
                || System.getProperty("mrj.version") != null;
    }

    @Override
    protected void uninit(ColorChooser c) {
        if (c.getBorder() instanceof CCBorder) {
            c.setBorder(null);
        }
        if (getDefaultTooltip().equals(c.getToolTipText())) {
            c.setToolTipText(null);
        }
        c.removePropertyChangeListener("font", FONT_CHANGE);
    }

    private static final class CcCopyAction extends AbstractAction {

        private final ColorChooser chooser;

        CcCopyAction(ColorChooser chooser) {
            putValue(NAME, "copy");
            this.chooser = chooser;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Color c = chooser.getColor();
            String txt = ColorParser.toMinimalString(c);
            StringSelection sel = new StringSelection(txt);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
        }
    }

    private static final class CcPasteAction extends AbstractAction {

        private final ColorChooser chooser;

        CcPasteAction(ColorChooser chooser) {
            putValue(NAME, "paste");
            this.chooser = chooser;
        }

        @Override
        public boolean isEnabled() {
            for (DataFlavor df : Toolkit.getDefaultToolkit().getSystemClipboard().getAvailableDataFlavors()) {
                if (DataFlavor.stringFlavor.equals(df)) {
                    try {
                        String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        return ColorParser.canParse(data);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Logger.getLogger(DefaultColorChooserUI.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                }
            }
            return super.isEnabled(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String str = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                Color color = ColorParser.parse(str);
                if (color != null) {
                    chooser.setColor(color);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(DefaultColorChooserUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String getDefaultTooltip() {
        return isMac() ? ColorChooser.getString("tip.mac")
                : //NOI18N
                ColorChooser.getString("tip"); //NOI18N
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        ColorChooser chooser = (ColorChooser) c;
        Color col = chooser.transientColor() != null
                ? chooser.transientColor() : chooser.getColor();

        if (col.getAlpha() != 255) {
            int halfWidth = chooser.getWidth() / 2;
            int halfHeight = chooser.getHeight() / 2;
            Color gray1 = new Color(128, 128, 128);
            Color gray2 = new Color(164, 164, 164);
            g.setColor(gray1);
            g.fillRect(0, 0, halfWidth, halfHeight);
            g.fillRect(halfWidth, halfHeight, halfWidth, halfHeight);
            g.setColor(gray2);
            g.fillRect(halfWidth, 0, halfWidth, halfHeight);
            g.fillRect(0, halfHeight, halfWidth, halfHeight);
        }

        g.setColor(col);
        g.fillRect(0, 0, chooser.getWidth() - 1, chooser.getHeight() - 1);
        if (chooser.hasFocus()) {
            g.setColor(invertColor(col));
            g.drawRect(4, 4, chooser.getWidth() - 8, chooser.getHeight() - 8);
        }
    }

//*****************Some utility methods for manipulating colors***********
    /**
     * Finds a color that will visually contrast with the selected color
     */
    private static final Color invertColor(Color c) {
        int r = checkRange(255 - c.getRed());
        int g = checkRange(255 - c.getGreen());
        int b = checkRange(255 - c.getBlue());
        return new Color(r, g, b);
    }

    /**
     * Checks to make sure the color component passed is not too close to
     * middle-of-the-road, and if so, returns its difference with 128. Used by
     * invertColor to make sure that it doesn't, for example, return 129,129,129
     * as a color to contrast with 127,127,127.
     */
    private static final int checkRange(int i) {
        int result = i;
        if (Math.abs(128 - i) < 24) {
            result = Math.abs(128 - i);
        }
        return result;
    }
}
