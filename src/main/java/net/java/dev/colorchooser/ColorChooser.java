/*
 * Copyright 2010-2022 Tim Boudreau
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
 */
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

/**
 * A color chooser which can pop up a pluggable set of palettes. The palette
 * displayed is controlled by combinations of the alt and shift and ctrl
 * (command on macintosh) keys. Will fire an action event when a color is
 * selected. For accessibility, it will show a standard Swing color chooser if
 * focused and either space or enter are pressed.
 * <p>
 * By default, supports two sets of palettes - a set of 4 continuous palettes
 * and a set of 4 tiled, fixed palettes (the SVG/X palette, Swing/AWT palettes
 * and a history of recently selected colors). Whether the tiled or continuous
 * palettes are given precedence depends on the property <code>
 * continuousPalettePreferred</code>.
 * <p>
 * Palettes are pluggable, so it is possible to provide your own
 * implementation(s) of Palette to be displayed when the component is clicked.
 * <p>
 * Typical usage: Attach an ActionListener; it will be notified when the user
 * selects a color.
 * <p>
 * To catch colors as the user selects, listen for PROP_TRANSIENT_COLOR. The
 * component will fire changes in PROP_COLOR along with actin events, when the
 * user selects a color. PROP_COLOR changes are fired both in response to use
 * actions and programmatic changes to the color property.
 * <P>
 * @author Tim Boudreau
 */
public final class ColorChooser extends JComponent {

    /**
     * UI Class ID under which the UI delegate class is stored in UIManager (see
     * UIManager.getUI()). The string value is
     * <code>&quot;nbColorChooserUI&quot;</code>
     */
    public static final String UI_CLASS_ID = "nbColorChooserUI"; //NOI18N

    private transient Palette[] palettes = null;
    private Color color = Color.BLUE;
    private transient Color transientColor = null;
    private transient List actionListenerList;

    /**
     * Property name for property fired when the color property changes.
     */
    public static final String PROP_COLOR = "color"; //NOI18N
    /**
     * Property name for property fired when the transient color property (the
     * color while the user is selecting) changes.
     */
    public static final String PROP_TRANSIENT_COLOR = "transientColor"; //NOI18N
    /**
     * Fired when the value of the continuous palette changes.
     */
    public static final String PROP_CONTINUOUS_PALETTE = "continuousPalette"; //NOI18N

    /**
     * Property indicating the visibility of the popup palette. Code that tracks
     * PROP_TRANSIENT_COLOR can listen for this property with a value of false
     * to do a final update using the value from getColor() to ensure the set
     * color is in sync with the actual value of the color picker - in the case
     * that the mouse was released off the palette, the color may be restored to
     * its previous value.
     */
    public static final String PROP_PICKER_VISIBLE = "pickerVisible";
    private boolean continuousPalette = true;

    /**
     * Create a color chooser
     */
    public ColorChooser() {
        this((java.awt.Color) null);
    }

    /**
     * Create a color chooser initialized to the passed color, defaulted to show
     * a continuous palette on initial click.
     */
    public ColorChooser(Color initialColor) {
        this(null, initialColor);
    }

    /**
     * Create a color chooser with the passed array of 8 palettes and
     * initialized with the passed color.
     */
    public ColorChooser(Palette[] palettes, Color initialColor) {
        setPalettes(palettes);
        if (initialColor != null) {
            color = initialColor;
        }
        updateUI();
    }

    /**
     * Create a color chooser with the passed array of 8 or fewer palettes.
     */
    public ColorChooser(Palette[] palettes) {
        this(palettes, null);
    }

    /**
     * Overridden to return <code>UI_CLASS_ID</code>.
     *
     * @since 1.5 - the previous version mis-capitalized the method name
     */
    @Override
    public String getUIClassID() {
        return UI_CLASS_ID;
    }

    @Override
    public void updateUI() {
        if (UIManager.get(UI_CLASS_ID) != null) {
            setUI((ColorChooserUI) UIManager.getUI(this));
        } else {
            setUI(DefaultColorChooserUI.createUI(this));
        }
    }

    /**
     * Get the color currently represented by this component. If the user is in
     * the process of selecting (the palette or color chooser is open), this
     * will be the last known value, until such time as the user selects a color
     * and an action event is fired.
     */
    public Color getColor() {
        return color;
    }

    public String getColorAsText() {
        return ColorParser.toMinimalString(getColor());
    }

    /**
     * Set the color using a string in one of the formats supported by
     * ColorParser, such as hexadecimal or comma-delimited rgb.
     *
     * @param colorAsText A string describing a color
     * @return true if the string was parsed successfully
     * @since 1.4
     */
    public boolean setAsText(String colorAsText) {
        Color c = ColorParser.parse(colorAsText);
        if (c != null) {
            setColor(c);
            return true;
        }
        return false;
    }

    /**
     * Set the color this color chooser currently represents. Note this will
     * fire a change in <code>PROP_COLOR</code> but will not trigger an action
     * event to be fired.
     *
     * @param c the color
     */
    public void setColor(Color c) {
        if (c.getClass() != Color.class) {
            c = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
        if (!Objects.equals(color, c)) {
            Color old = color;
            color = c;
            if (color != null) {
                // Since via the keyboard, you can adjust a color down to black,
                // adjusting it back upward will get you gray - what we want is to
                // instead substitute in the hue and saturation from the last known
                // non-gray color
                updatePreservedHueAndSaturation(color);
            }
            repaint();
            firePropertyChange(PROP_COLOR, old, c); //NOI18N
        }
    }

    private float preservedHue;
    private float preservedSaturation;
    private final float[] componentsScratch = new float[3];

    private void updatePreservedHueAndSaturation(Color c) {
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        if (Math.abs(red - green) > 2 || Math.abs(red - blue) > 2) {
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), componentsScratch);
            preservedHue = componentsScratch[0];
            preservedSaturation = componentsScratch[1];
        }
    }

    void setTransientColor(Color c) {
        Color old = transientColor;
        transientColor = c;
        if ((c != null && !color.equals(old)) || (old == null && c != null)) {
            firePropertyChange(PROP_TRANSIENT_COLOR, old, getTransientColor());
            repaint();
        } else if (c == null) {
            firePropertyChange(PROP_TRANSIENT_COLOR, old, getColor());
            repaint();
        }
    }

    /**
     * Returns the currently displayed color which may not be the same as the
     * value of <code>getColor()</code> but is the color currently displayed as
     * the user moves the mouse to select the color.
     *
     * @see #PROP_TRANSIENT_COLOR
     * @see #setTransientColor
     * @return the color currently being displayed (not necessarily the one
     * returned by <code>getColor()</code>).
     */
    public Color getTransientColor() {
        return transientColor == null ? null
                : new Color(transientColor.getRed(), transientColor.getGreen(),
                        transientColor.getBlue(), transientColor.getAlpha());
    }

    /**
     * Get a string representation of a color, if possible returning a named,
     * localized constant if the passed color matches one of the SVG constants;
     * else returning a String representing RGB values.
     */
    public static String colorToString(Color c) {
        NamedColor named = RecentColors.findNamedColor(c);
        if (named == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getRed());
            sb.append(',');
            sb.append(c.getGreen());
            sb.append(',');
            sb.append(c.getBlue());
            return sb.toString();
        } else {
            return named.getDisplayName();
        }
    }

    Color transientColor() {
        return transientColor;
    }

    /**
     * Returns the SVG or Swing constant name for the passed color, if the color
     * exactly matches a color in the Swing UIManager constants or the
     * SVG/X-Windows constants.
     */
    public static String getColorName(Color color) {
        return PredefinedPalette.getColorName(color);
    }

    /**
     * Set whether the initial palette shown when clicked with no keys pressed
     * is one showing a continuous (rainbow) palette or a set of tiles with
     * different colors.
     *
     * @param val The value, true to show a continuous palette by default
     */
    public void setContinuousPalettePreferred(boolean val) {
        if (val != continuousPalette) {
            continuousPalette = val;
            setPalettes(null);
            firePropertyChange(PROP_CONTINUOUS_PALETTE, !val, val);
        }
    }

    /**
     * Determine whether the initial palette shown when clicked with no keys
     * pressed is one showing a continuous (rainbow) palette or a set of tiles
     * with different colors. The default is <code>TRUE</code>.
     *
     * @return whether or not to default to a continuous palette
     */
    public boolean isContinuousPalettePreferred() {
        return continuousPalette;
    }

    /**
     * Set the Palette objects this color chooser will display. Can be null to
     * reset to defaults. The passed array length must less than or equal to 8.
     * <p>
     * Which palette is shown to the user depends on what if any control keys
     * are being held when the user initially clicks or presses while dragging
     * the mouse to select. The mapping between key combinations and palette
     * entries is:
     * <ul>
     * <li>No keys held: 0</li>
     * <li>Shift: 1</li>
     * <li>Ctrl (Command on macintosh): 2</li>
     * <li>Shift-Ctrl(Command): 3</li>
     * <li>Alt: 4</li>
     * <li>Alt-Shift: 5</li>
     * <li>Alt-Ctrl(Command): 6</li>
     * <li>Alt-Ctrl(Command)-Shift: 7</li>
     * </ul>
     */
    public void setPalettes(Palette[] palettes) {
        if (palettes != null && palettes.length > 9) {
            throw new IllegalArgumentException("Must be <= 8 palettes"); //NOI18N
        }
        Palette[] old = this.palettes;
        if (palettes == null) {
            palettes = Palette.getDefaultPalettes(continuousPalette);
            palettes = Arrays.copyOf(palettes, palettes.length + 1);
            palettes[palettes.length - 1] = new AlphaPalette(this);
        }
        this.palettes = palettes;
        firePropertyChange("palettes", old, palettes.clone()); //NOI18N
    }

    /**
     * Get the array of palettes that will be displayed when the user clicks
     * this color chooser component and holds down various keys.
     */
    public Palette[] getPalettes() {
        Palette[] result = new Palette[palettes.length];
        System.arraycopy(palettes, 0, result, 0, palettes.length);
        return result;
    }

    static String getString(String key) {
        String BUNDLE = "net.java.dev.colorchooser.resources.Bundle"; //NOI18N
        try {
            return ResourceBundle.getBundle(BUNDLE).getString(key);
        } catch (MissingResourceException mre) {
            mre.printStackTrace();
            return key;
        }
    }

    //****************** Action listener support **************************
    /**
     * Registers ActionListener to receive events. Action events are fired when
     * the user selects a color, either by click-drag-releasing the mouse over
     * the popup palette, or by pressing space or enter and selecting a color
     * from the popup <code>JColorChooser</code>.
     *
     * @param listener The listener to register.
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {
        if (actionListenerList == null) {
            actionListenerList = new ArrayList();
        }
        actionListenerList.add(listener);
    }

    /**
     * Removes ActionListener from the list of listeners. Action events are
     * fired when the user selects a color, either by click-drag-releasing the
     * mouse over the popup palette, or by pressing space or enter and selecting
     * a color from the popup <code>JColorChooser</code> (note they are
     * <i>not</i>
     * fired if you call <code>setColor()</code>).
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {
        if (actionListenerList != null) {
            actionListenerList.remove(listener);
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    void fireActionPerformed(ActionEvent event) {
        List list;
        synchronized (this) {
            if (actionListenerList == null) {
                return;
            }
            list = (List) ((ArrayList) actionListenerList).clone();
        }
        for (int i = 0; i < list.size(); i++) {
            ((java.awt.event.ActionListener) list.get(i)).actionPerformed(event);
        }
    }

    void firePickerVisible(boolean val) {
        firePropertyChange(PROP_PICKER_VISIBLE, !val, val);
    }

    public boolean isDragDropEnabled() {
        return dragDrop;
    }

    private boolean dragDrop;

    public void setDragDropEnabled(boolean val) {
        if (dragDrop != val) {
            dragDrop = val;
            if (val) {
                DropTarget dt = new DropTarget(this, new DTL());
                setDropTarget(dt);
            } else {
                setDropTarget(null);
            }
            firePropertyChange("dragDropEnabled", !val, val);
        }
    }

    private class DTL implements DropTargetListener {

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            for (DataFlavor flavor : dtde.getCurrentDataFlavors()) {
                if (flavor == DataFlavor.stringFlavor) {
                    try {
                        String data = (String) dtde.getTransferable()
                                .getTransferData(DataFlavor.stringFlavor);
                        if (!ColorParser.canParse(data)) {
                            dtde.rejectDrag();
                        } else {
                            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Logger.getLogger(ColorChooser.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            for (DataFlavor flavor : dtde.getCurrentDataFlavors()) {
                if (flavor == DataFlavor.stringFlavor) {
                    try {
                        String data = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                        if (ColorParser.canParse(data)) {
                            Color c = ColorParser.parse(data);
                            if (c != null) {
                                setColor(c);
                                dtde.dropComplete(true);
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Logger.getLogger(ColorChooser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * Adjust the color in HSB color space by the passed delta values,
     * constraining saturation and brightness values to the range 0.0 to 1.0,
     * and cycling the hue continuously through the range 0-1 such that &gt; 1 =
     * value - 1.
     *
     * @param hueBy
     * @param saturationBy
     * @param brightnessBy
     * @return
     */
    public boolean adjustColor(float hueBy, float saturationBy, float brightnessBy) {
        Color color = getColor();
        if (color == null) {
            // have to start somewhere...
            color = new Color(128, 128, 180);
        }

        float[] components = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), components);
        if (isNoSaturation(color)) {
            components[0] = preservedHue;
            components[1] = preservedSaturation;
        }

        float hue = clamp(rotate(components[0] + hueBy));
        float saturation = clamp(components[1] + saturationBy);
        float brightness = clamp(components[2] + brightnessBy);
        Color nue = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        if (color.getAlpha() != 255) {
            nue = new Color(nue.getRed(), nue.getGreen(), nue.getBlue(), color.getAlpha());
        }
        boolean changed = !color.equals(nue);
        if (changed) {
            setColor(nue);
        }
        return changed;
    }

    private static boolean isNoSaturation(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return red == green && red == blue;
    }

    private static float rotate(float value) {
        // XXX won't work for values < or > 2
        if (value < 0) {
            return 1F - value;
        } else if (value > 1) {
            return value - 1F;
        }
        return value;
    }

    private static float clamp(float value) {
        return Math.max(0F, Math.min(1, value));
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "lcd_hrgb");
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel()); //NOI18N
        } catch (UnsupportedLookAndFeelException e) {

        }
        final javax.swing.JLabel jb1 = new javax.swing.JLabel("Choose a color"
                + //NOI18N
                " (combine Shift/Ctrl/Alt to switch palettes)"); //NOI18N

        JButton jb = new JButton("GC");
        final ColorChooser cc = new ColorChooser();

        cc.setFont(new Font("Arial", Font.PLAIN, 48));

        boolean[] updating = new boolean[1];

        JTextField area = new JTextField(ColorParser.toMinimalString(cc.getColor()));
        cc.addActionListener((ActionEvent ae) -> {
            jb1.setForeground(cc.getColor());
            if (!updating[0]) {
                area.setText(ColorParser.toMinimalString(cc.getColor()));
            }
        });

        area.getDocument().addDocumentListener(new DocumentListener() {
            Segment s = new Segment();

            private void updateAsText(Document doc) {
                if (updating[0]) {
                    return;
                }
                updating[0] = true;
                try {
                    doc.render(() -> {
                        try {
                            doc.getText(0, doc.getLength(), s);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    });
                    String txt = s.toString();
                    if (!txt.isEmpty()) {
                        cc.setAsText(txt);
                    }
                } finally {
                    updating[0] = false;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAsText(e.getDocument());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAsText(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // do nothing
            }
        });

        final ColorChooser cc2 = new ColorChooser();
        cc2.addActionListener((ActionEvent e) -> {
            jb.setForeground(cc2.getColor());
        });

        cc.setDragDropEnabled(true);
        cc2.setDragDropEnabled(true);

        area.setDragEnabled(true);

//        cc.setPreferredSize(new Dimension (48, 48));
//        cc.setPalettes (new Palette[] { new ContinuousPalette("Flupper", 
//                200, 200, 0.5F)});
//        cc.setEnabled (false);
        final JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.getContentPane().setLayout(new java.awt.FlowLayout());
        jf.getContentPane().add(cc);
        jb1.setBorder(null);
        jf.getContentPane().add(jb1);
        jf.getContentPane().add(cc2);
        jf.getContentPane().add(area);
        jf.setSize(500, 160);
        jf.setLocation(20, 20);

//        JButton jb = new JButton("Change UI");
//        jb.addActionListener (new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//                    SwingUtilities.updateComponentTreeUI(jf.getContentPane());
//                } catch (Exception e) {}
//            }
//        });
//        jf.getContentPane().add (jb);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    for (int i = 0; i < 5; i++) {
                        System.gc();
                        System.runFinalization();
                        Thread.yield();
                    }
                } catch (Exception e) {
                }
            }
        });
        jf.getContentPane().add(jb);

        jf.setVisible(true);
        cc.requestFocus();
    }
}
