package it.davidesestili.libreria.utils;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

public class GUIUtils {

	public static void aggiungiComponente(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.anchor = anchor;
		constraints.fill = fill;
		constraints.insets = insets;
		constraints.ipadx = ipadx;
		constraints.ipady = ipady;
		
		container.add(component, constraints);
	}
	
	
	public static void centerScreen(Component finestra)
	{
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    int w = finestra.getSize().width;
	    int h = finestra.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    finestra.setLocation(x, y);	
	}
	
}
