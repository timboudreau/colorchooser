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

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class ImageTransferable implements Transferable {
	Image img;
	
	ImageTransferable(Image i) {
		img = i;
	}

        @Override
	public Object getTransferData(DataFlavor f)
			throws UnsupportedFlavorException, IOException {
		if(f.equals(DataFlavor.imageFlavor)==false)
			throw new UnsupportedFlavorException(f);
		return img;
	}

        @Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {DataFlavor.imageFlavor};
	}

        @Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return(flavor.equals(DataFlavor.imageFlavor));
	}
	
}
