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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * Default border for the color chooser, which uses bevels that lighten/darken
 * the selected color, and includes slightly different painting logic for 
 * Metal LAF.
 *
 * @author Tim Boudreau
 */
class CCBorder implements Border {
    @Override
    public Insets getBorderInsets(Component c) {
        Insets result;
        if (UIManager.getLookAndFeel()  instanceof MetalLookAndFeel) {
            result = new Insets(2, 2, 1, 1);
        }  else {
            result = new Insets(1, 1, 1, 1);
        }
        return result;
    }
    
    @Override
    public boolean isBorderOpaque() {
        return true;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        ColorChooser cc = (ColorChooser) c;
        if (!cc.isEnabled()) {
            g.setColor (cc.getColor());
            g.fillRect(x, y, w, h);
            return;
        }
        Color col = cc.transientColor() == null ? 
                cc.getColor() : cc.transientColor();
        
        if (cc == null) {
            col = c.getBackground();
            if (col == null) {
                col = Color.BLACK;
            }
        }
        if (UIManager.getLookAndFeel()  instanceof MetalLookAndFeel) {
            g.setColor(darken(col));
            g.drawLine(x, y, x+w-1, y);
            g.drawLine(x, y, x, y+h-1);
            g.drawLine(x+w-1, y+h-1, x, y+h-1);
            g.drawLine(x+w-1, y+h-1, x+w-1, y);
            g.setColor(brighten(col));
            g.drawLine(x+w-2, y+h-2, x+1, y+h-2);
            g.drawLine(x+w-2, y+h-2, x+w-2, y+1);
        } else {
            g.setColor(darken(col));
            g.drawLine(x+w-1, y+h-1, x, y+h-1);
            g.drawLine(x+w-1, y+h-1, x+w-1, y);
            g.setColor(brighten(col));
            g.drawLine(x, y, x+w-1, y);
            g.drawLine(x, y, x, y+h-1);
        }
    }
    
    /** Slightly more subtle than Color.darker() */
    private static final Color darken(Color c) {
        int amount = 30;
        int r = normalizeToByte(c.getRed() - amount);
        int g = normalizeToByte(c.getGreen() - amount);
        int b = normalizeToByte(c.getGreen() - amount);
        return new Color(r,g,b);
    }
    
    /** Slightly more subtle than Color.brighter() */
    private static final Color brighten(Color c) {
        int amount = 30;
        int r = normalizeToByte(c.getRed() + amount);
        int g = normalizeToByte(c.getGreen() + amount);
        int b = normalizeToByte(c.getGreen() + amount);
        return new Color(r,g,b);
    }
    
    /** Ensure an int is within the possible range for a byte*/
    private static final int normalizeToByte(int i) {
        return Math.min(255, Math.max(0, i));
    }
}