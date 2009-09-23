/*
 * Created on 28-mar-2005
 *
 */
package com.siga.Utilidades;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.siga.general.SIGAException;


/**
 * Paginador no sensitivo a mayusculas/minusculas y acentos. Utiliza un Pool configurado
 * con los parametros:
 * alter session set nls_comp=linguistic ;
 * alter session set nls_sort=GENERIC_BASELETTER;
 */

public interface IPaginador  
{
	static int NUM_REGISTROS = 100;
	static int DISTANCIA_PAGINAS_CACHE = 2;
	
	public Vector obtenerSiguientes() throws ClsExceptions, SIGAException ;
	public Vector obtenerAnteriores() throws ClsExceptions, SIGAException;

	public int getNumeroPaginas();
	public int getNumeroTotalRegistros();
	public int getNumeroRegistrosPorPagina();
	public int getPaginaActual();

	public Vector obtenerPagina(int pagina) throws ClsExceptions, SIGAException;
	public String getQueryInicio();
	public Hashtable getCodigosInicio();
}