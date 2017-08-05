package it.davidesestili.libreria.gui;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.davidesestili.libreria.utils.GUIUtils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class About extends JFrame {

	private static final long serialVersionUID = 1L;

	public About()
	{
		super("Informazioni sul programma");
		
		try
		{
			setResizable(false);
			setLayout(new GridBagLayout());

			JLabel progLabel = new JLabel("Libreria 0.2");
			JLabel infoLabel = new JLabel("Autore: Davide Sestili");

			JButton chiudiButton = new JButton("Chiudi");
			chiudiButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			GUIUtils.aggiungiComponente(this, progLabel, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0);
			GUIUtils.aggiungiComponente(this, infoLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0);
			GUIUtils.aggiungiComponente(this, chiudiButton, 0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);

			setSize(300, 100);
			GUIUtils.centerScreen(this);

			setVisible(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
