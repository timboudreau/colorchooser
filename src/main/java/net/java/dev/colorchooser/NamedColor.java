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

/** An abstract class representing a color which has a name and may provide
 * custom code for instantiation. Implements comparable in order to appear
 * in an ordered way in palettes.  Note that this class is internal to the color
 * chooser.  It is not acceptable
 * for the color chooser to provide instances of NamedColor from its getColor
 * method, since they may be serialized and will not be deserializable if their
 * implementation is not on the classpath.
 *
 * @author  Tim Boudreau
 */
abstract class NamedColor extends Color implements Comparable {
    /**
     * Creates a new instance of NamedColor
     * @param name
     * @param r red
     * @param g green
     * @param b blue
     */
    protected NamedColor(String name, int r, int g, int b) {
        super (r, g, b);
    }
    /**
     * Get a localized display name for this color if possible.  For
     * some colors, such as named system colors, a localized variant is not
     * a reasonable option.
     * @return the localized (or not) display name
     */
    public abstract String getDisplayName();
    /** Get the programmatic name, if any, for this color, such as a 
     * Swing UIDefaults key or an SVG constant name.*/
    public abstract String getName();
    /**
     * Fetch a java code snippet for instantiating this color.  For cases such
     * as named defaults from the Swing UIManager, this method might return
     * something such as <code>UIManager.getColor(&quot;control&quot;)</code>.
     * Useful when implementing a property editor.
     * @return a string that could be pasted into Java code to instantiate a
     * color with these rgb values
     */
    public String getInstantiationCode() {
        return toString();
    }
    
    static NamedColor create (Color c, String name) {
        return new DefaultNamedColor(c, name);
    }
    
    private static final class DefaultNamedColor extends NamedColor {
        private final String name;
        DefaultNamedColor(Color c, String name) {
            super (name, c.getRed(), c.getGreen(), c.getBlue());
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof NamedColor) {
                NamedColor nc = (NamedColor) o;
                String nm = nc.getDisplayName();
                if (nm == null && getDisplayName() == null) {
                    return 0;
                } else {
                    return nm != null && getDisplayName() != null ?
                        getDisplayName().compareTo(nm) : -1;
                }
            } else {
                return -1;
            }
        }
    }
}
