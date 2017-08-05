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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class AggiungiLibro extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField titoloField;
	private JTextField scaffaleField;
	private JList autoriList;

	public AggiungiLibro()
	{
		super("Aggiungi libro");
		
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
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		JLabel titoloLabel = new JLabel("Titolo libro:");
		titoloField = new JTextField(20);
		
		JLabel scaffaleLabel = new JLabel("Scaffale:");
		scaffaleField = new JTextField(20);
		
		JLabel autoriLabel = new JLabel("Elenco autori:");
		autoriList = new JList(caricaAutori());
		JScrollPane scrollPane = new JScrollPane(autoriList);
		scrollPane.setPreferredSize(new Dimension(400, 500));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());
		
		JButton insertButton = new JButton("Aggiungi libro");
		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aggiungiLibro();
			}
		});

		JButton annullaButton = new JButton("Annulla");
		annullaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		GUIUtils.aggiungiComponente(this, titoloLabel, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, titoloField, 1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, scaffaleLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, scaffaleField, 1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, autoriLabel, 0, 2, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, scrollPane, 0, 3, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, buttonsPanel, 0, 4, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);

		GUIUtils.aggiungiComponente(buttonsPanel, insertButton, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(buttonsPanel, annullaButton, 1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);		
	}

	private void aggiungiLibro()
	{
		try
		{
			if(titoloField.getText().trim().equals("") || scaffaleField.getText().trim().equals(""))
			{
				JOptionPane.showMessageDialog(this, "Digita il titolo del libro e lo scaffale");
				return;
			}
			
			String titolo = titoloField.getText().trim().replace("'", "''");
			String scaffale = scaffaleField.getText().trim().replace("'", "''");

			DatiLista datiLista = (DatiLista)autoriList.getSelectedValue();
			if(datiLista == null)
			{
				JOptionPane.showMessageDialog(this, "Seleziona un autore");
				return;
			}
			
			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();

			StringBuilder builder = new StringBuilder("select count(*) as q from libro where titolo = '");
			builder.append(titolo + "' and scaffale = '");
			builder.append(scaffale + "' and id_autore = ");
			builder.append(datiLista.getIdAutore());
			ResultSet rset = sqlite.eseguiQuery(builder.toString());
			int q = rset.getInt("q");
			rset.close();
			if(q > 0)
			{
				sqlite.chiudiConnessione();
				JOptionPane.showMessageDialog(this, "Il libro è già stato inserito");
				return;
			}

			builder = new StringBuilder("insert into libro (titolo, scaffale, id_autore) values ('");
			builder.append(titolo + "','");
			builder.append(scaffale + "',");
			builder.append(datiLista.getIdAutore() + ")");
			sqlite.eseguiInsert(builder.toString());
			
			sqlite.chiudiConnessione();
			JOptionPane.showMessageDialog(this, "Libro inserito correttamente");
			
			dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private Vector<DatiLista> caricaAutori()
	{
		Vector<DatiLista> datiLista = new Vector<DatiLista>();
		
		try
		{
			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();
			
			ResultSet rset = sqlite.eseguiQuery("select count(*) as q from autore");
			int q = rset.getInt("q");
			rset.close();
			
			if(q == 0)
			{
				sqlite.chiudiConnessione();

				return datiLista;
			}
			
			rset = sqlite.eseguiQuery("select * from autore order by cognome, nome");
			while(rset.next())
			{
				int idAutore = rset.getInt("id_autore");
				String cognome = rset.getString("cognome");
				String nome = rset.getString("nome");
				
				DatiLista riga = new DatiLista(idAutore, cognome, nome);
				datiLista.add(riga);
			}
			
			rset.close();
			
			sqlite.chiudiConnessione();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return datiLista;
	}

}

class DatiLista
{

	private int idAutore;
	private String cognome, nome;
	
	public DatiLista(int idAutore, String cognome, String nome)
	{
		this.idAutore = idAutore;
		this.cognome = cognome;
		this.nome = nome;
	}

	public int getIdAutore() {
		return idAutore;
	}

	public String getCognome() {
		return cognome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return cognome + " " + nome;
	}
	
}
