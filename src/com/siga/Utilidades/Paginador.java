/*
 * Created on 28-mar-2005
 *
 */
package com.siga.Utilidades;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import java.io.Serializable;
import com.siga.general.SIGAException;

@Deprecated
public class Paginador implements IPaginador, Serializable
{
	IPaginador paginador = null;

	protected Paginador() throws ClsExceptions {}

	public Paginador(String query) throws ClsExceptions {
		paginador = new PaginadorSQL(query);
	}

	public Paginador(String query1, String query2) throws ClsExceptions {
		paginador = new PaginadorSQL(query1, query2);
	}

	public Paginador(String query, String[] cabeceras) throws ClsExceptions {
		paginador = new PaginadorSQL(query, cabeceras);
	}
	
	public Paginador(Vector v) throws ClsExceptions {
		paginador = new PaginadorVector(v);
	}

	public Paginador(Vector v, String[] cabeceras) throws ClsExceptions {
		paginador = new PaginadorVector(v, cabeceras);
	}

	public Vector obtenerSiguientes() throws ClsExceptions, SIGAException {
		return paginador.obtenerSiguientes();
	}

	public Vector obtenerAnteriores() throws ClsExceptions, SIGAException {
		return paginador.obtenerAnteriores();
	}

	public int getNumeroTotalRegistros() {
		return paginador.getNumeroTotalRegistros();
	}

	public int getNumeroPaginas() {
		return paginador.getNumeroPaginas();
	}

	public int getNumeroRegistrosPorPagina() {
		return paginador.getNumeroRegistrosPorPagina();
	}

	public int getPaginaActual() {
		return paginador.getPaginaActual();
	}

	public Vector obtenerPagina(int pagina) throws ClsExceptions, SIGAException {
		return paginador.obtenerPagina(pagina);
	}

	/**
	 * @return the paginador
	 */
	public IPaginador getPaginador() {
		return paginador;
	}

	public Hashtable getCodigosInicio() {
		return paginador.getCodigosInicio();
	}

	public String getQueryInicio() {
		return paginador.getQueryInicio();
	}
}