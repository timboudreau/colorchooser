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
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

/** Palette implementation that can have recent colors added to it.
 *
 * @author  Tim Boudreau
 */
class RecentColors extends Palette {
    private Palette palette; 
    private boolean changed=true;
    /** Creates a new instance of RecentColors */
    private RecentColors() {
    }
    
    private Palette getWrapped() {
        if (changed || palette == null) {
            palette = createPalette();
            changed = false;
        }
        return palette;
    }
    
    @Override
    public java.awt.Color getColorAt(int x, int y) {
        return getWrapped().getColorAt(x,y);
    }
    
    @Override
    public String getDisplayName() {
        try {
            return ResourceBundle.getBundle(
                "org.netbeans.swing.colorchooser.Bundle").getString("recent"); //NOI18N
        } catch (MissingResourceException mre) {
//            mre.printStackTrace();
            return "Recent colors";
        }
    }
    
    @Override
    public Dimension getSize() {
        Dimension result = ((PredefinedPalette)getWrapped()).calcSize();
        return result;
    }
    
    @Override
    public void paintTo(java.awt.Graphics g) {
        getWrapped().paintTo(g);
    }
    
    @Override
    public String getNameAt(int x, int y) {
        return getWrapped().getNameAt(x,y);
    }
    
    Stack stack = new Stack();
    void add(Color c) {
        if (c instanceof RecentColor) {
            return;
        }
        if (stack.indexOf(c) == -1) {
            String name = c instanceof PredefinedPalette.BasicNamedColor ? 
                ((PredefinedPalette.BasicNamedColor) c).getDisplayName() : null;
            String toString = c instanceof PredefinedPalette.BasicNamedColor ?
                ((PredefinedPalette.BasicNamedColor)c).toString() : null;
            Color col = new RecentColor(name, c.getRed(), c.getGreen(), c.getBlue(), toString);
            stack.push(col);
            changed = true;
            palette = null;
            if (c instanceof NamedColor) {
                addToNameCache((NamedColor)c);
            }
            saveToPrefs();
        }
    }
    public static final String INNER_DELIMITER="^$";
    public static final String OUTER_DELIMITER="!*";
    public void saveToPrefs() {
        Preferences prefs = getPreferences();
        if (prefs == null) return;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        Stack stack = new Stack();
        stack.addAll(this.stack);
        while (!stack.isEmpty() && count < 64) {
            count++;
            Color c = (Color) stack.pop();
            if (c instanceof DummyColor) {
                break;
            }
            String name="null";
            if (c instanceof PredefinedPalette.BasicNamedColor) {
                PredefinedPalette.BasicNamedColor nc = (PredefinedPalette.BasicNamedColor) c;
                name = nc.getDisplayName();
            }
            if (name == "null") { //NOI18N
                name = null;
            }
            sb.append (name);
            sb.append(INNER_DELIMITER);
            sb.append (c.getRed());
            sb.append (INNER_DELIMITER);
            sb.append(c.getGreen());
            sb.append (INNER_DELIMITER);
            sb.append(c.getBlue());
            sb.append (INNER_DELIMITER);
            if (c instanceof PredefinedPalette.BasicNamedColor) {
                sb.append(c.toString());
            } else {
                sb.append('x');
            }
            sb.append (OUTER_DELIMITER); //NOI18N
        }
        prefs.put("recentColors", sb.toString()); //NOI18N
    }
    
    static Map<Integer, NamedColor> namedMap;
    static NamedColor findNamedColor (Color color) {
        if (namedMap == null) {
            return null;
        }
        NamedColor result = (NamedColor)namedMap.get(color.getRGB());
        return result;
    }
    
    static void addToNameCache (NamedColor color) {
        Map<Integer, NamedColor> map = namedMap;
        if (map == null) {
            synchronized(RecentColors.class) {
                map = namedMap;
                if (map == null) {
                    map = namedMap = new HashMap<>(40);
                }
            }
        }
        namedMap.put (color.getRGB(), color);
    }
    
    private Preferences getPreferences() {
        try {
            Preferences base = Preferences.userNodeForPackage(getClass());
            return base.node("1.5"); //NOI18N
        } catch (Exception ace) {
            return null;
        }
    }
    
    public void loadFromPrefs() {
        Preferences prefs = getPreferences();
        if (prefs == null) return;
        String s = prefs.get("recentColors", null); //NOI18N
        stack = new Stack();
        Color[] col = new Color[64];
        Arrays.fill(col, new DummyColor());
        int count = 63;
        try {
            if (s != null) {
                //a weird but highly unlikely delimiter
                StringTokenizer tok = new StringTokenizer(s,OUTER_DELIMITER); //NOI18N
                while (tok.hasMoreTokens() && count >= 0) {
                    String curr = tok.nextToken();
                    //another weird but highly unlikely delimiter
                    StringTokenizer tk2 = new StringTokenizer(curr, INNER_DELIMITER); //NOI18N
                    while (tk2.hasMoreTokens()) {
                        String name = tk2.nextToken();
                        if ("null".equals(name)) {
                            name = null;
                        }
                        int r = Integer.parseInt(tk2.nextToken());
                        int g = Integer.parseInt(tk2.nextToken());
                        int b = Integer.parseInt(tk2.nextToken());
                        String toString = tk2.nextToken();
                        if ("x".equals(toString)) { //NOI18N
                            col[count] = new RecentColor(name,r,g,b);
                        } else {
                            col[count] = new RecentColor(name,r,g,b,toString);
                        }
                        addToNameCache((NamedColor)col[count]);
                    }
                    count--;
                }
            }
            stack.addAll(Arrays.asList(col));
        } catch (NumberFormatException e) {
            System.err.println("Error loading color preferences"); //NOI18N
            e.printStackTrace();
        }
    }
    
    private Palette createPalette() {
        PredefinedPalette.BasicNamedColor[] nc = (PredefinedPalette.BasicNamedColor[]) stack.toArray(new PredefinedPalette.BasicNamedColor[0]);
        return new PredefinedPalette("", nc); //NOI18N
    }
    
    private class RecentColor extends PredefinedPalette.BasicNamedColor {
        String displayName;
        String toString = null;
        RecentColor (String name, int r, int g, int b) {
            super(name, r, g, b);
            displayName = name;
        }

        RecentColor(String name, int r, int g, int b, String toString) {
            this(name, r, g, b);
            displayName = name;
            this.toString = toString;
        }
        
        @Override
        public int compareTo(Object o) {
            return stack.indexOf(o) - stack.indexOf(this);
        }
        
        @Override
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof Color) {
                Color c = (Color) o;
                return c.getRGB() == getRGB();
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return getRGB();
        }
        
        @Override
        public String toString() {
            if (toString != null) {
                return toString;
            } else {
                return "new java.awt.Color(" + getRed() + "," + getGreen() 
                    + "," + getBlue()+ ")"; //NOI18N
            }
        }
    }
    
    private static RecentColors defaultInstance = null;
    public static final RecentColors getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new RecentColors();
            ((RecentColors)defaultInstance).loadFromPrefs();
        }
        return defaultInstance;
    }
    
    /** A stand in for colors to fill up the array of recent colors until
     * we really have something to put there. */
    private class DummyColor extends RecentColor {
        DummyColor() {
            super(null, 0,0,0);
        }
        @Override
        public String getDisplayName() {
            return null;
        }
        //Ensure that no color will match this, so black swing colors can
        //be put into the recent colors array
        @Override
        public boolean equals(Object o) {
            return o == this;
        }
        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }
    }
    
}
