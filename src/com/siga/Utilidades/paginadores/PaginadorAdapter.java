package com.siga.Utilidades.paginadores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;

public abstract class PaginadorAdapter<E> implements IPaginador<E>, Serializable{
	private static final long serialVersionUID = -1806403868443554796L;
	
	protected Hashtable<Integer,Object> codigosInicio = null;
	protected int numeroTotalRegistros = 0;
	protected int numeroRegistrosPagina = 0;
	protected int paginaActual = 0;
	protected int paginaInicialCache = 0;
	protected int paginaFinalCache = 0;
	protected int ultimaPagina = 0;
	protected String query = "";
	protected List<E> cache = null;
	protected String queryInicio = "";

	protected void inicializar(String query, Map<Integer, Object> codigos) throws ClsExceptions {
		try {
			String queryOriginal = query;
			
			int endIndex = queryOriginal.toLowerCase().lastIndexOf("order by");
			String queryCountSinOrder ;
			if(endIndex!=-1)
				queryCountSinOrder = queryOriginal.substring(0,endIndex);
			else
				queryCountSinOrder = queryOriginal;

			RowsContainer rc = new RowsContainer();
			ArrayList<?> queryList= UtilidadesBDAdm.quitaCamposQuery(new String(queryCountSinOrder),(Hashtable<?,?>)codigos);
			String queryCountSinOrderSinCampos = (String)queryList.get(0);
			Hashtable<?,?> codigosReducido = (Hashtable<?,?>)queryList.get(1);
			String count = null;
			//Ha fallado el quitar los ordenes a los campos
			count = "SELECT  /*+ no_merge */ COUNT(*)  AS NUMREGISTROS FROM (" + queryCountSinOrderSinCampos + ")";
			rc=ejecutarConsulta(rc, count, codigosReducido);
			
			if (rc!=null){
				Row row = (Row) rc.get(0);
				if (row!=null) {
					int numeroTotalRegistros = Integer.parseInt(row.getString("NUMREGISTROS"));
					calculaPaginas(numeroTotalRegistros);
				} else {
					calculaPaginas(0);
				}
			} else {
				calculaPaginas(0);
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}
	
	protected void calculaPaginas(int numeroTotalRegistros){
	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String numRegistros = properties.returnProperty("paginador.registrosPorPagina", true);
		numeroRegistrosPagina = numRegistros != null ? Integer.parseInt(numRegistros) : NUM_REGISTROS;

		this.numeroTotalRegistros = numeroTotalRegistros;
		if (this.numeroTotalRegistros > 0 && this.numeroRegistrosPagina > 0) {
			if ((this.numeroTotalRegistros / this.numeroRegistrosPagina) > 0) {
				int paginaDePico = (this.numeroTotalRegistros % this.numeroRegistrosPagina) > 0 ? 1 : 0;
				this.ultimaPagina = (this.numeroTotalRegistros / this.numeroRegistrosPagina) + paginaDePico;
			} else {
				this.ultimaPagina = 1;
			}
		} else {
			this.ultimaPagina = 0;
		}
	}
	
	public Vector<E> obtenerSiguientes() throws ClsExceptions {
		paginaActual++;
		
		try {
			return obtenerPagina(paginaActual);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Vector<E> obtenerAnteriores() throws ClsExceptions {
		paginaActual--;
		paginaActual = paginaActual == 0 ? 1 : paginaActual;
		
		try {
			return obtenerPagina(paginaActual);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Vector<E> obtenerPagina(int pagina) throws ClsExceptions {
		Vector<E> retorno = null;

		try {
			// Comprobamos si está en Caché
			if (pagina >= this.paginaInicialCache && pagina <= this.paginaFinalCache) {
				retorno = obtenerPaginaCache(pagina - this.paginaInicialCache + 1, pagina == ultimaPagina);
				paginaActual = pagina;
			} else {
				int rowmax = (pagina + DISTANCIA_PAGINAS_CACHE) * this.numeroRegistrosPagina;
				int rowmin = (pagina - DISTANCIA_PAGINAS_CACHE - 1) * this.numeroRegistrosPagina + 1;

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
				retorno = obtenerPaginaCache(pagina - this.paginaInicialCache + 1, pagina == ultimaPagina);
				paginaActual = pagina;
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}

		return retorno;
	}

	private Vector<E> obtenerPaginaCache(int pagina, boolean ultimaPag) {
		Vector<E> retorno = new Vector<E>();

		if (cache == null || cache.size() < 1)
			return retorno;
		
		if (pagina<0 || pagina>ultimaPagina)
			return retorno;
		
		if (ultimaPag) {
			int resto = numeroTotalRegistros % numeroRegistrosPagina;
			resto = resto > 0 ? resto : numeroRegistrosPagina;
			for (int i = 0; (i < resto) && ((numeroRegistrosPagina * (pagina - 1) + i)>=0) && (cache.size()>(numeroRegistrosPagina * (pagina - 1) + i)); i++) {
				retorno.add(cache.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		} else {
			for (int i = 0; (i < numeroRegistrosPagina) && ((numeroRegistrosPagina * (pagina - 1) + i)>=0) && (cache.size()>(numeroRegistrosPagina * (pagina - 1) + i))
				; i++) {
				retorno.add(cache.get(numeroRegistrosPagina * (pagina - 1) + i));
			}
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public void setCache(int rowmin, int rowmax) throws ClsExceptions {
		String queryTrasSustituir = this.query;
		Hashtable<Integer,Object> codigosAux = null;
		
		if (codigosInicio!=null && codigosInicio.size()>0){
			codigosAux=(Hashtable<Integer,Object>)codigosInicio.clone();
		} else {
			codigosAux=new Hashtable<Integer,Object>();
		}

		try {
			Pattern pattern = Pattern.compile("rowmax");
			Matcher matcher = pattern.matcher(queryTrasSustituir);
			int contador=codigosAux.size()+1;
			codigosAux.put(contador,String.valueOf(rowmax));
			queryTrasSustituir = sustituirMax(matcher, rowmax, contador);
	
			pattern = Pattern.compile("rowmin");
			matcher = pattern.matcher(queryTrasSustituir);
			contador=codigosAux.size()+1;
			codigosAux.put(contador,String.valueOf(rowmin));
			queryTrasSustituir = sustituirMin(matcher, rowmin, contador);
	
			RowsContainer rc = new RowsContainer();
			rc=ejecutarConsulta(rc, queryTrasSustituir, codigosAux);
			
			if (rc!=null) {
				this.cache = (List<E>)rc.getAll();
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	protected abstract String sustituirMax(Matcher matcher, int rowmax, int posicion);
	protected abstract String sustituirMin(Matcher matcher, int rowmin, int posicion);
	protected abstract RowsContainer ejecutarConsulta(RowsContainer rc, String query, Hashtable<?,?> codigos) throws ClsExceptions;
	
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

	public String getQueryInicio() {
		return queryInicio;
	}

	public void setQueryInicio(String query) {
		queryInicio=query;
	}
}
