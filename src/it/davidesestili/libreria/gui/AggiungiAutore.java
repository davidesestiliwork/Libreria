package it.davidesestili.libreria.gui;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import it.davidesestili.libreria.db.SQLite;
import it.davidesestili.libreria.utils.GUIUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AggiungiAutore extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField cognomeField;
	private JTextField nomeField;
	private JPanel listaPanel;
	
	public AggiungiAutore()
	{
		super("Aggiungi autore");
		
		try
		{
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			
			creaGUI();
			pack();
			
			setVisible(true);
			GUIUtils.centerScreen(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void creaGUI()
	{
		setLayout(new GridBagLayout());
		
		JLabel cognomeLabel = new JLabel("Cognome");
		cognomeField = new JTextField(20);
		
		JLabel nomeLabel = new JLabel("Nome");
		nomeField = new JTextField(20);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());

		JButton annullaButton = new JButton("Annulla");
		annullaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JButton aggiungiButton = new JButton("Aggiungi autore");
		aggiungiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aggiungiAutore();
			}
		});
		
		listaPanel = new JPanel();
		listaPanel.setLayout(new GridBagLayout());
		
		GUIUtils.aggiungiComponente(this, cognomeLabel, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, cognomeField, 1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, nomeLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, nomeField, 1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, buttonsPanel, 0, 2, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, listaPanel, 0, 3, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		
		GUIUtils.aggiungiComponente(buttonsPanel, aggiungiButton, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(buttonsPanel, annullaButton, 1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
	}

	public void aggiungiAutore()
	{
		try
		{
			if(cognomeField.getText().trim().equals("") || nomeField.getText().trim().equals(""))
			{
				JOptionPane.showMessageDialog(this, "Digita cognome e nome");
				return;
			}

			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();
			
			StringBuilder builder = new StringBuilder();
			builder.append("select count(*) as q from autore where cognome = '");
			builder.append(cognomeField.getText().replace("'", "''"));
			builder.append("' and nome = '");
			builder.append(nomeField.getText().replace("'", "''"));
			builder.append("'");
			ResultSet rset = sqlite.eseguiQuery(builder.toString());
			int q = rset.getInt("q");
			rset.close();
			if(q > 0)
			{
				sqlite.chiudiConnessione();
				JOptionPane.showMessageDialog(this, "L'autore è già stato inserito");
				return;
			}
			
			builder = new StringBuilder();
			builder.append("insert into autore (cognome, nome) values ('");
			builder.append(cognomeField.getText().replace("'", "''"));
			builder.append("', '");
			builder.append(nomeField.getText().replace("'", "''"));
			builder.append("')");
			sqlite.eseguiInsert(builder.toString());
			
			sqlite.chiudiConnessione();
			JOptionPane.showMessageDialog(this, "Autore inserito correttamente");
			
			dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
