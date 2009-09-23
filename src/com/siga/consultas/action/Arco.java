
package com.siga.consultas.action;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/*
 * Created on 08-abr-2005
 *
 */

/**
 * @author daniel.campos
 * Clase que representa la relacion entre dos tablas de la B.D.
 *
 */
public class Arco {

	String nodoOrigen = "";
	String nodoDestino = "";
	Vector vClavesOrigen = null;
	Vector vClavesDestino = null;
	int peso = 0;
	private boolean unidireccional = false;

	/**
	 * Arco
	 * Constructor
	 */
	private Arco () {
		this.nodoOrigen = "";
		this.nodoDestino = "";
		this.vClavesOrigen = null;
		this.vClavesDestino = null;
		this.peso = 0;
		this.unidireccional = false;
	}

	/**
	 * Arco
	 * Constructor
	 * @param origen
	 * @param destino
	 * @param leeBD
	 * @throws SIGAException
	 */
	public Arco (String o, String d, boolean leeBD, boolean uni) throws SIGAException {
		try {
			this.nodoOrigen = o;
			this.nodoDestino = d;
			this.vClavesOrigen = new Vector ();
			this.vClavesDestino = new Vector ();
			this.peso = 1;
			this.unidireccional = uni;
			if (leeBD)
				this.getClaves();
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
	/**
	 * Clone
	 * Crea una copia del objeto
	 * return el objeto copia
	 */
	public Object clone(){
		try {
			Arco a = new Arco();
			a.nodoOrigen = this.nodoOrigen;
			a.nodoDestino = this.nodoDestino;
			a.vClavesOrigen = this.vClavesOrigen;
			a.vClavesDestino = this.vClavesDestino;
			a.peso = this.peso;
			return a;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * getClaves
	 * Obtiene los campos que comforman las claves
	 * @return true si ok
	 * @throws SIGAException
	 */
	private boolean getClaves() throws SIGAException {
		try {
			String fk_origen  = "";
			String fk_destino = "";

			
			// 0 Obtenermos las fk y rfk que estan relaccionadas				
			RowsContainer rc0 = null;
			rc0 = new RowsContainer(); 

			String sql0 = " SELECT CONSTRAINT_NAME, R_CONSTRAINT_NAME FROM USER_CONSTRAINTS " + 
						  " WHERE CONSTRAINT_TYPE = 'R' AND TABLE_NAME = '" + ((this.unidireccional)?this.nodoDestino:this.nodoOrigen) + "' AND R_CONSTRAINT_NAME IN " + 
						  "   (SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS " +
						  "    WHERE (CONSTRAINT_TYPE = 'P' OR CONSTRAINT_TYPE = 'U') AND TABLE_NAME = '" + ((this.unidireccional)?this.nodoOrigen:this.nodoDestino) + "' )";
 			if (rc0.query(sql0)) {
				for (int i = 0; i < rc0.size(); i++)	{
					Row fila = (Row) rc0.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) {
						fk_origen  = UtilidadesHash.getString(registro, "CONSTRAINT_NAME");
						fk_destino = UtilidadesHash.getString(registro, "R_CONSTRAINT_NAME");
					}
				}
			}
 			else 
 				throw new SIGAException ("Error al obtener el nombre de los campos de la clave extrajera");

			// 1 Obtenemos las columanas PK del origen
			RowsContainer rc1 = null;
			rc1 = new RowsContainer(); 
			
			String sql1 = " SELECT COLUMN_NAME FROM USER_CONS_COLUMNS WHERE CONSTRAINT_NAME = '" + fk_origen + "' ORDER BY POSITION ";
 			if (rc1.query(sql1)) {
				for (int i = 0; i < rc1.size(); i++)	{
					Row fila = (Row) rc1.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) {
						this.vClavesOrigen.add(UtilidadesHash.getString(registro, "COLUMN_NAME"));
					}
				}
			}
 			else 
 				throw new SIGAException ("Error al obtener el nombre de los campos de la clave extrajera ");

			// 2 Obtenemos los nombres de las columnas destino 
			RowsContainer rc2 = null;
			rc2 = new RowsContainer();
			
			String sql2 = " SELECT COLUMN_NAME FROM USER_CONS_COLUMNS WHERE CONSTRAINT_NAME = '" + fk_destino + "' ORDER BY POSITION ";
			if (rc2.query(sql2)) {
				for (int i = 0; i < rc2.size(); i++)	{
					Row fila = (Row) rc2.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) {
						this.vClavesDestino.add(UtilidadesHash.getString(registro, "COLUMN_NAME"));
					}
				}
			}
 			else 
 				throw new SIGAException ("Error al obtener el nombre de los campos de la clave extrajera " + fk_destino) ;
			
	        return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al obtener la relacion entre tablas");
		}
	}
	
	/**
	 * @return Returns the clavesOrigen.
	 */
	public Vector getClavesOrigen() {
		return this.vClavesOrigen;
	}
	/**
	 * 
	 * @return Returns the clavesDestino.
	 */
	public Vector getClavesDestino() {
		return this.vClavesDestino;
	}

	/**
	 * @return Returns the nodoDestino.
	 */
	public String getNodoDestino() {
		return nodoDestino;
	}
	/**
	 * @return Returns the nodoOrigen.
	 */
	public String getNodoOrigen() {
		return nodoOrigen;
	}
	/**
	 * @return Returns the peso.
	 */
	public int getPeso() {
		return peso;
	}
}
