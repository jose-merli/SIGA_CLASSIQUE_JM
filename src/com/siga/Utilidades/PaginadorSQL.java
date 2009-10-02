/*
 * Created on 28-mar-2005
 *
 */
package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.atos.utils.ReadProperties;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;


import java.io.Serializable;

/**
 * Paginador no sensitivo a mayusculas/minusculas y acentos. Utiliza un Pool configurado
 * con los parametros:
 * alter session set nls_comp=linguistic ;
 * alter session set nls_sort=GENERIC_BASELETTER;
 * @author Jose.Zulueta
 *
 *  
 */

public class PaginadorSQL extends Paginador implements IPaginador,Serializable 
{
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
	protected String queryInicio = "";
	
	protected PaginadorSQL() throws ClsExceptions {	}

	protected PaginadorSQL(String query) throws ClsExceptions {

		try {
			setQueryInicio(query);
			inicializar(query);

			//Inicializo la query
			String columnas = query.substring(6, query.toUpperCase().indexOf(
					"FROM"));

			/*
			 * this.query = "SELECT "+columnas+" FROM (SELECT a.*, rownum r FROM
			 * ("+ query+") a WHERE rownum <=rowmax) WHERE r>=rowmin";
			 */

			this.query = "SELECT /*+ no_merge */ * FROM (SELECT /*+ no_merge */ a.*, rownum r FROM (" + query
					+ ") a WHERE rownum<=rowmax) WHERE r>=rowmin";

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	protected PaginadorSQL(String query1, String query2) throws ClsExceptions {

		try {
			
			setQueryInicio(query1);
			inicializar(query2);

			//Inicializo la query
			String columnas = query1.substring(6, query1.toUpperCase().indexOf(
					"FROM"));

			/*
			 * this.query = "SELECT "+columnas+" FROM (SELECT a.*, rownum r FROM
			 * ("+ query+") a WHERE rownum <=rowmax) WHERE r>=rowmin";
			 */

			this.query = "SELECT /*+ no_merge */ * FROM (SELECT /*+ no_merge */ a.*, rownum r FROM (" + query1
					+ ") a WHERE rownum<=rowmax) WHERE r>=rowmin";

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	protected PaginadorSQL(String query, String[] cabeceras) throws ClsExceptions {

		try {
			
			setQueryInicio(query);
			inicializar(query);
			this.cabeceras = cabeceras;
			String columnas = "";

			//Inicializo la query
			for (int i = 0; i < cabeceras.length; i++) {
				columnas += "\"" + cabeceras[i] + "\"" + ",";
			}
			columnas = columnas.substring(0, columnas.length() - 1);

			this.query = "SELECT " + columnas
					+ " FROM (SELECT /*+ no_merge */ a.*, rownum r FROM (" + query
					+ ") a WHERE rownum<=rowmax) WHERE r>=rowmin";

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	private void inicializar(String query) throws ClsExceptions {

		try {
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties properties = new ReadProperties("SIGA.properties");
			String numRegistros = properties.returnProperty("paginador.registrosPorPagina", true);
			this.numeroRegistrosPagina = numRegistros != null ? Integer.parseInt(numRegistros) : NUM_REGISTROS;
			String pagCache = properties.returnProperty("paginador.distanciaPaginasCache", true);
			this.distanciaPagCache = pagCache != null ? Integer.parseInt(pagCache) : DISTANCIA_PAGINAS_CACHE;
			this.queryOriginal = query;
///////////////////////////////////////////////////////////////////////////////////////////////////
			//Viendo que el order by consume recursos y que no afecta en nada
			//para el count lo quitamos
			int endIndex = queryOriginal.toLowerCase().lastIndexOf("order by");
			String queryCountSinOrder ;
			if(endIndex!=-1)
				queryCountSinOrder = queryOriginal.substring(0,endIndex);
			else
				queryCountSinOrder = queryOriginal;

			// Inicializar numeroTotalRegistros y ultimaPagina
			// Inicializar numeroTotalRegistros y ultimaPagina
			RowsContainer rc = new RowsContainer();
			ArrayList queryList= UtilidadesBDAdm.quitaCamposQuery(new String(queryCountSinOrder),null);
			String queryCountSinOrderSinCampos = (String)queryList.get(0);
			String count = null;
			//Ha fallado el quitar los ordenes a los campos
			count = "SELECT  /*+ no_merge */ COUNT(*) AS NUMREGISTROS FROM (" + queryCountSinOrderSinCampos + ")";
			rc.queryNLS(count);
			
			Row row = (Row) rc.get(0);
			if (row!=null) {
				this.numeroTotalRegistros = Integer.parseInt(row.getString("NUMREGISTROS"));
	
				if (this.numeroTotalRegistros > 0) {
					if ((this.numeroTotalRegistros / this.numeroRegistrosPagina) > 0) {
						int paginaDePico = (this.numeroTotalRegistros % this.numeroRegistrosPagina) > 0 ? 1
								: 0;
						this.ultimaPagina = (this.numeroTotalRegistros / this.numeroRegistrosPagina)
								+ paginaDePico;
					} else {
						this.ultimaPagina = 1;
					}
				} else {
					this.ultimaPagina = 0;
	
				}
			} else {
			    this.numeroTotalRegistros = 0;
			    this.ultimaPagina = 0;
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Vector obtenerSiguientes() throws ClsExceptions {
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

			// Comprobamos si está en Caché
			if (pagina >= this.paginaInicialCache
					&& pagina <= this.paginaFinalCache) {
				retorno = obtenerPaginaCache(pagina - this.paginaInicialCache
						+ 1, pagina == ultimaPagina);
				paginaActual = pagina;
			} else {
				int rowmax = (pagina + DISTANCIA_PAGINAS_CACHE)
						* this.numeroRegistrosPagina;
				int rowmin = (pagina - DISTANCIA_PAGINAS_CACHE - 1)
						* this.numeroRegistrosPagina + 1;

				if (rowmin < 0) {
					rowmin = 1;
					this.paginaInicialCache = 1;
				} else {
					this.paginaInicialCache = pagina - DISTANCIA_PAGINAS_CACHE;
				}

				if (rowmax > numeroTotalRegistros) {
					rowmax = numeroTotalRegistros;
					this.paginaFinalCache = ultimaPagina;
				} else {
					this.paginaFinalCache = pagina + DISTANCIA_PAGINAS_CACHE;
				}

				//hago la select para actualizar la caché
				setCache(rowmin, rowmax);
				retorno = obtenerPaginaCache(pagina - this.paginaInicialCache
						+ 1, pagina == ultimaPagina);
				paginaActual = pagina;
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}

		return retorno;
	}

	// pagina=1...NUMERO_PAGINAS_CACHE*2+1
	private Vector obtenerPaginaCache(int pagina, boolean ultimaPag) {

		Vector retorno = new Vector();

		if (ultimaPag) {
			int resto = numeroTotalRegistros % numeroRegistrosPagina;
			resto = resto > 0 ? resto : numeroRegistrosPagina;
			for (int i = 0; i < resto; i++) {
				retorno
						.add(cache
								.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		} else {
			for (int i = 0; (i < numeroRegistrosPagina); i++) {
				retorno
						.add(cache
								.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		}

		return retorno;

	}
	
	private void setCache(int rowmin, int rowmax) throws ClsExceptions {

		try {
			String queryTrasSustituir = this.query;
			Pattern pattern = Pattern.compile("rowmin");
			Matcher matcher = pattern.matcher(queryTrasSustituir);
			queryTrasSustituir = matcher.replaceAll(String.valueOf(rowmin));
			pattern = Pattern.compile("rowmax");
			matcher = pattern.matcher(queryTrasSustituir);
			queryTrasSustituir = matcher.replaceAll(String.valueOf(rowmax));

			RowsContainer rc = new RowsContainer();
			rc.queryNLS(queryTrasSustituir);
			if (rc!=null) {
			    this.cache = rc.getAll();
			}

		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
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

	/**
	 * @return the queryOriginal
	 */
	public String getQueryOriginal() {
		return queryOriginal;
	}

	public String getQueryInicio() {
		return queryInicio;
	}

	public void setQueryInicio(String queryInicio) {
		this.queryInicio = queryInicio;
	}
}