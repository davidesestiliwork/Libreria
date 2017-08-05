package it.davidesestili.libreria.utils;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import it.davidesestili.libreria.db.SQLite;

public class InserimentoProva {
	public static void main(String[] args) {
		try
		{
			SQLite sqlite = new SQLite();
			sqlite.apriConnessione();
			
			for(int i = 0; i < 1000; i++)
			{
				String sql = "insert into libro (id_autore, scaffale, titolo) values (";
				sql += "1, 'A1', 'prova " + (i + 1) + "')";
				sqlite.eseguiInsert(sql);
			}

			sqlite.chiudiConnessione();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
