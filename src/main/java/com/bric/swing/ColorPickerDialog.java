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
package com.bric.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/** This wraps a <code>ColorPicker</code> in a simple dialog with "OK" and "Cancel" options.
 * <P>(This object is used by the static calls in <code>ColorPicker</code> to show a dialog.)
 *
 */
class ColorPickerDialog extends JDialog {
    
	private static final long serialVersionUID = 1L;
	
	ColorPicker cp;
	int alpha;
	JButton ok = new JButton(ColorPicker.strings.getObject("OK").toString());
	JButton cancel = new JButton(ColorPicker.strings.getObject("Cancel").toString());
	Color returnValue = null;
	ActionListener buttonListener = new ActionListener() {
        @Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if(src==ok) {
				returnValue = cp.getColor();
			}
			setVisible(false);
		}
	};
	
	ColorPickerDialog(Frame owner, Color color,boolean includeOpacity) {
		super(owner);
		initialize(owner,color,includeOpacity);
                initActions();
	}

	ColorPickerDialog(Dialog owner, Color color,boolean includeOpacity) {
		super(owner);
		initialize(owner,color,includeOpacity);
                initActions();
	}
        
        private void initActions() {
                getRootPane().setDefaultButton(ok);
                getRootPane().getActionMap()
                        .put("cancel", new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cancel.doClick();
                            }

                        });
                getRootPane().getInputMap(WHEN_IN_FOCUSED_WINDOW)
                        .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "cancel");
        }

	private void initialize(Component owner,Color color,boolean includeOpacity) {
		cp = new ColorPicker(true,includeOpacity);
		setModal(true);
		setResizable(false);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;
		c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10,10,10,10);
		getContentPane().add(cp,c);
		c.gridy++; c.gridwidth = 1;
		getContentPane().add(new JPanel(),c);
		c.gridx++; c.weightx = 0;
		getContentPane().add(cancel,c);
		c.gridx++; c.weightx = 0;
		getContentPane().add(ok,c);
		cp.setRGB(color.getRed(), color.getGreen(), color.getBlue());
		cp.setOpacity( ((float)color.getAlpha())/255f );
		alpha = color.getAlpha();
		pack();
        setLocationRelativeTo(owner);
		
		ok.addActionListener(buttonListener);
		cancel.addActionListener(buttonListener);
		
		getRootPane().setDefaultButton(ok);
	}
	
	/** @return the color committed when the user clicked 'OK'.  Note this returns <code>null</code>
	 * if the user canceled this dialog, or exited via the close decoration.
	 */
	public Color getColor() {
		return returnValue;
	}
}

