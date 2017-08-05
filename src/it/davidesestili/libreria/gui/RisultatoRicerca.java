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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Vector;

import it.davidesestili.libreria.db.SQLite;
import it.davidesestili.libreria.utils.GUIUtils;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class RisultatoRicerca extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable tabella;
	
	private DatiRicerca datiRicerca;
	
	public RisultatoRicerca(DatiRicerca datiRicerca)
	{
		super();
		
		try
		{
			this.datiRicerca = datiRicerca;

			setTitle("Risultati ricerca (" + datiRicerca.getDatiRicerca().size() + " record)");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(true);

			creaGUI();

			setSize(700, 500);
			setVisible(true);
			GUIUtils.centerScreen(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void creaGUI()
	{
		setLayout(new GridBagLayout());
		
		Vector colonne = new Vector();
		colonne.add("Autore");
		colonne.add("Titolo");
		colonne.add("Scaffale");

		tabella = new JTable(datiRicerca.getDatiRicerca(), colonne);
		JScrollPane scrollPane = new JScrollPane(tabella);

		JButton chiudiButton = new JButton("Chiudi");
		chiudiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		JButton cancella = new JButton("Cancella libro selezionato");
		cancella.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancellaLibro();
			}
		});
		
		GUIUtils.aggiungiComponente(this, scrollPane, 0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, chiudiButton, 1, 1, 1, 1, 8, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, cancella, 0, 1, 1, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
	}
	
	@SuppressWarnings("rawtypes")
	private void cancellaLibro()
	{
		int row = tabella.getSelectedRow();
		if(row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selezionare un libro", "Messaggio", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			Vector libro = (Vector)datiRicerca.getDatiRicerca().get(row);
			String autore = (String)libro.get(0);
			String titolo = (String)libro.get(1);
			String scaffale = (String)libro.get(2);
			
			Integer idLibro = (Integer)datiRicerca.getIdLibro().get(row);
			
			String message = "Cancellare il libro '" + titolo + "', autore '" + autore + "', scaffale '" + scaffale + "'?";
			int confirm = JOptionPane.showConfirmDialog(this, message, "Conferma", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(confirm == JOptionPane.YES_OPTION)
			{
				try
				{
					SQLite sqlite = new SQLite();
					sqlite.apriConnessione();
					
					Connection conn = sqlite.getConnection();
					String sql = "delete from libro where id_libro = " + idLibro;
					PreparedStatement stat = conn.prepareStatement(sql);
					stat.execute();
					
					sqlite.chiudiConnessione();
					
					JOptionPane.showMessageDialog(this, "Il libro è stato cancellato", "Cancellazione", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
