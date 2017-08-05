package it.davidesestili.libreria.db;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLite {

	private Connection connection;
	
	public SQLite() throws Exception
	{
		Class.forName("org.sqlite.JDBC");
	}

	public Connection getConnection() 
	{
		return connection;
	}

	public void apriConnessione() throws Exception
	{
		connection = DriverManager.getConnection("jdbc:sqlite:database/libreria");
	}

	public void eseguiInsert(String sql) throws Exception
	{
		Statement stat = connection.createStatement();
		stat.executeUpdate(sql);
		stat.close();
	}
	
	public ResultSet eseguiQuery(String sql) throws Exception
	{
		Statement stat = connection.createStatement();
		return stat.executeQuery(sql);
	}

	public void chiudiConnessione() throws Exception
	{
		connection.close();
	}
	
}
