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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FinestraPrincipale extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField autoreField;
	private JTextField titoloField;
	private JTextField scaffaleField;
	private JButton ricercaButton;
	
	public FinestraPrincipale()
	{
		super("Libreria 0.2");
		
		try
		{
			aggiungiMenu();
			creaGUI();
			pack();

		    setDefaultCloseOperation(EXIT_ON_CLOSE);
		    setResizable(false);
		    GUIUtils.centerScreen(this);
		    setVisible(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void creaGUI()
	{
		setLayout(new GridBagLayout());
		
		/*
		ImageIcon icon = new ImageIcon("scaffale_libri.jpg");
		JLabel iconLabel = new JLabel(icon);
		*/

		JLabel autoreLabel = new JLabel("Autore:");
		autoreField = new JTextField(20);
		
		JLabel titoloLabel = new JLabel("Titolo:");
		titoloField = new JTextField(20);
		
		JLabel scaffaleLabel = new JLabel("Scaffale:");
		scaffaleField = new JTextField(20);
		
		ricercaButton = new JButton("Ricerca");
		ricercaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				apriRisultatoRicerca();
			}
		});
		
//		GUIUtils.aggiungiComponente(this, iconLabel, 0, 0, 2, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, autoreLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		GUIUtils.aggiungiComponente(this, autoreField, 1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, titoloLabel, 0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, titoloField, 1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, scaffaleLabel, 0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, scaffaleField, 1, 3, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
		GUIUtils.aggiungiComponente(this, ricercaButton, 0, 4, 2, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1);
	}

	private void apriRisultatoRicerca()
	{
		ricercaButton.setText("Ricerca in corso, attendere...");
		ricercaButton.setEnabled(false);

		new Thread(new Runnable() {
			@Override
			public void run() {
				DatiRicerca datiRicerca = ricerca();
				if(datiRicerca != null)
				{
					new RisultatoRicerca(datiRicerca);
				}

				ricercaButton.setText("Ricerca");
				ricercaButton.setEnabled(true);
			}
		}).start();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DatiRicerca ricerca()
	{
		DatiRicerca datiRicerca = null;
		Vector dati = null;
		Vector idLibro = null;

		try
		{
			String autore = autoreField.getText().trim();
			String titolo = titoloField.getText().trim();
			String scaffale = scaffaleField.getText().trim();
	
			if(autore.equals("") && titolo.equals("") && scaffale.equals(""))
			{
				JOptionPane.showMessageDialog(this, "Eseguire una ricerca per autore, titolo o scaffale");
				return datiRicerca;
			}
			
			dati = new Vector<String[]>();
			idLibro = new Vector<Integer>();
			
			StringBuilder builder = new StringBuilder("select * from libro inner join autore on libro.id_autore = autore.id_autore where");
			if(autore.length() > 0)
			{
				builder.append(" (autore.cognome || ' ' || autore.nome) like '%" + autore + "%' and ");
			}
			if(titolo.length() > 0)
			{
				builder.append(" libro.titolo like '%" + titolo + "%' and ");
			}
			if(scaffale.length() > 0)
			{
				builder.append(" libro.scaffale like '%" + scaffale + "%' and ");
			}
			int len = builder.toString().length();
			String sql = builder.toString().substring(0, len - 5);
			
			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();

			ResultSet rset = sqlite.eseguiQuery(sql);

			while(rset.next())
			{
				String cognome = rset.getString("cognome");
				String nome = rset.getString("nome");
				titolo = rset.getString("titolo");
				scaffale = rset.getString("scaffale");
				Integer id = rset.getInt("id_libro");

				idLibro.add(id);
				
				Vector riga = new Vector();
				riga.add(cognome + " " + nome);
				riga.add(titolo);
				riga.add(scaffale);
				riga.add(idLibro);
				dati.add(riga);
			}
			
			rset.close();
			
			sqlite.chiudiConnessione();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return new DatiRicerca(dati, idLibro);
	}

	private void aggiungiMenu()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);

		JMenuItem aggiungiAutore = new JMenuItem("Aggiungi autore");
		aggiungiAutore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AggiungiAutore();
			}
		});
		menuFile.add(aggiungiAutore);
		
		JMenuItem aggiungiLibro = new JMenuItem("Aggiungi libro");
		aggiungiLibro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AggiungiLibro();
			}
		});
		menuFile.add(aggiungiLibro);
		
		JMenuItem statistiche = new JMenuItem("Statistiche");
		statistiche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostraStatistiche();
			}
		});
		menuFile.add(statistiche);
		
		JMenuItem cancellaDB = new JMenuItem("Cancella DB");
		cancellaDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancellaDB();
			}
		});
		menuFile.add(cancellaDB);
		
		JMenuItem esci = new JMenuItem("Esci");
		esci.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuFile.add(esci);
		
		JMenu menuInfo = new JMenu("?");
		menuBar.add(menuInfo);
		
		JMenuItem aboutItem = new JMenuItem("Informazioni");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});
		menuInfo.add(aboutItem);
		
		setJMenuBar(menuBar);
	}

	private void cancellaDB()
	{
		String input = JOptionPane.showInputDialog(this, "Sei sicuro di voler cancellare l'intero database? Digita 'cancellare' per proseguire.", "Cancellazione DB", JOptionPane.WARNING_MESSAGE);
		
		if(input != null && input.equals("cancellare"))
		{
			try
			{
				SQLite sqlite = new SQLite();
				sqlite.apriConnessione();
				
				Connection conn = sqlite.getConnection();
				String sql = "delete from libro";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.execute();
				
				sql = "delete from autore";
				stat = conn.prepareStatement(sql);
				stat.execute();
				
				sqlite.chiudiConnessione();
				
				JOptionPane.showMessageDialog(this, "L'intero database è stato cancellato", "Database", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void mostraStatistiche()
	{
		try
		{
			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();
			
			String sql = "select count(*) as autori from autore";
			ResultSet rset = sqlite.eseguiQuery(sql);
			int autori = rset.getInt("autori");
			rset.close();
			
			sql = "select count(*) as libri from libro";
			rset = sqlite.eseguiQuery(sql);
			int libri = rset.getInt("libri");
			rset.close();
			
			sql = "select count(*) as scaffali from (select distinct scaffale from libro)";
			rset = sqlite.eseguiQuery(sql);
			int scaffali = rset.getInt("scaffali");
			rset.close();
			
			sqlite.chiudiConnessione();
			
			StringBuilder builder = new StringBuilder("Numero di autori: " + autori);
			builder.append("\nNumero di libri: " + libri);
			builder.append("\nNumero di scaffali: " + scaffali);
			JOptionPane.showMessageDialog(this, builder.toString(), "Statistiche", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new FinestraPrincipale();
	}

}
