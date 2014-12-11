/*
 * Created on 28-mar-2005
 *
 */
package com.siga.Utilidades;

import java.util.Vector;
import com.atos.utils.ClsExceptions;


import java.io.Serializable;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

/**
 * Paginador no sensitivo a mayusculas/minusculas y acentos. Utiliza un Pool configurado
 * con los parametros:
 * alter session set nls_comp=linguistic ;
 * alter session set nls_sort=GENERIC_BASELETTER;
 */
@Deprecated
public class PaginadorVector extends Paginador implements IPaginador, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 691786969393430047L;
	protected int numeroTotalRegistros = 0;
	protected int numeroRegistrosPagina = 0;
	protected int paginaActual = 0;
	protected int paginaInicialCache = 0;
	protected int paginaFinalCache = 0;
	protected int ultimaPagina = 0;
	protected String query = "";
	protected Vector cache = null;
	protected int distanciaPagCache = 0;
	protected String[] cabeceras = null;
	protected String queryOriginal = "";

	private Vector vDatos = null;
	
	protected PaginadorVector(Vector v) throws ClsExceptions 
	{
		try {
			if (v != null) {
				vDatos = v;
			}
			else { 
				vDatos = new Vector();
			}
			inicializar();
		} 
		catch (Exception e) { e.printStackTrace();}
	}

	protected PaginadorVector(Vector vDatos, String[] cabeceras) throws ClsExceptions 
	{
		this (vDatos);
		this.cabeceras = cabeceras;
	}

	private void inicializar() throws ClsExceptions 
	{
		try {
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties properties = new ReadProperties("SIGA.properties");
			String numRegistros = properties.returnProperty("paginador.registrosPorPagina", true);

			this.numeroRegistrosPagina = numRegistros != null ? Integer.parseInt(numRegistros) : NUM_REGISTROS;
			String pagCache = properties.returnProperty("paginador.distanciaPaginasCache", true);
			this.distanciaPagCache = pagCache != null ? Integer.parseInt(pagCache) : DISTANCIA_PAGINAS_CACHE;

			if (vDatos != null) {
				this.numeroTotalRegistros = vDatos.size();
			}
			else {
				this.numeroTotalRegistros = 0;
			}

			if (this.numeroTotalRegistros > 0) {
				if ((this.numeroTotalRegistros / this.numeroRegistrosPagina) > 0) {
					int paginaDePico = (this.numeroTotalRegistros % this.numeroRegistrosPagina) > 0 ? 1 : 0;
					this.ultimaPagina = (this.numeroTotalRegistros / this.numeroRegistrosPagina) + paginaDePico;
				} 
				else {
					this.ultimaPagina = 1;
				}
			} 
			else {
				this.ultimaPagina = 0;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vector obtenerSiguientes() throws ClsExceptions 
	{
		paginaActual++;
		try {
			return obtenerPagina(paginaActual);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Vector obtenerAnteriores() throws ClsExceptions {
		paginaActual--;
		paginaActual = paginaActual == 0 ? 1 : paginaActual;
		try {
			return obtenerPagina(paginaActual);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Vector obtenerPagina(int pagina) throws ClsExceptions {

		Vector retorno = null;
		try {
			if(true){
				this.cache=this.vDatos;
				this.paginaFinalCache = this.ultimaPagina;
			}
				
			retorno = obtenerPaginaCache(pagina , pagina == ultimaPagina);
			paginaActual = pagina;
			
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}

		return retorno;
	}
	public Vector obtenerPaginaNoCache(int pagina) throws ClsExceptions {

		Vector retorno = null;
		try {
				this.cache=this.vDatos;
				this.paginaFinalCache = this.ultimaPagina;
				retorno = obtenerPaginaCache(pagina , pagina == ultimaPagina);
				paginaActual = pagina;
				
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}

		return retorno;
	}
	
	

	// pagina=1...NUMERO_PAGINAS_CACHE*2+1
	private Vector obtenerPaginaCache(int pagina, boolean ultimaPag) 
	{
		Vector retorno = new Vector();
		if (cache == null || cache.size() < 1)
			return retorno;
		
		if (ultimaPag) {
			int resto = numeroTotalRegistros % numeroRegistrosPagina;
			resto = resto > 0 ? resto : numeroRegistrosPagina;
			for (int i = 0; i < resto; i++) {
				retorno.add(cache.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		} 
		else {
			for (int i = 0; (i < numeroRegistrosPagina); i++) {
				retorno.add(cache.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		}
		return retorno;
	}
	

	public void setCache(int rowmin, int rowmax) throws ClsExceptions {
		this.cache = this.vDatos;
	}
	public int getNumeroPaginas() {
		return this.ultimaPagina;
	}

	public int getNumeroTotalRegistros() {
		return this.numeroTotalRegistros;
	}

	public int getNumeroRegistrosPorPagina() {
		return this.numeroRegistrosPagina;
	}

	public int getPaginaActual() {
		return this.paginaActual;
	}

}