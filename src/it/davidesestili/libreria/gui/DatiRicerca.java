package it.davidesestili.libreria.gui;

/*
 * Autore: Davide Sestili
 * 
 * Questo software è distribuito sotto licenza GPLv3
 * 
 * http://www.gnu.org/licenses/gpl.html
 * 
 */

import java.util.Vector;

public class DatiRicerca {

	@SuppressWarnings("rawtypes")
	public DatiRicerca(Vector datiRicerca, Vector idLibro)
	{
		this.datiRicerca = datiRicerca;
		this.idLibro = idLibro;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getDatiRicerca() {
		return datiRicerca;
	}
	@SuppressWarnings("rawtypes")
	public Vector getIdLibro() {
		return idLibro;
	}

	@SuppressWarnings({ "rawtypes" })
	private Vector datiRicerca;
	@SuppressWarnings({ "rawtypes" })
	private Vector idLibro;
	
}
