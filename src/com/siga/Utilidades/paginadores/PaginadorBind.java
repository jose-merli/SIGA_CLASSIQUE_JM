package com.siga.Utilidades.paginadores;

import java.util.Hashtable;
import java.util.regex.Matcher;

import com.atos.utils.ClsExceptions;
import com.atos.utils.RowsContainer;

public class PaginadorBind<E> extends PaginadorAdapter<E> {
	private static final long serialVersionUID = 7248138824807743593L;

	public PaginadorBind(String query, Hashtable<Integer, Object> codigos) throws ClsExceptions {
		this(query, query, codigos);
	}

	public PaginadorBind(String query1, String query2, Hashtable<Integer, Object> codigos) throws ClsExceptions {
		try {
			setCodigosInicio(codigos);
			setQueryInicio(query1);
			inicializar(query2, codigos);

			this.query = "SELECT /*+ no_merge */ * FROM (SELECT /*+ no_merge */ a.*, rownum r FROM (" + query1
					+ ") a WHERE rownum<=rowmax) WHERE r>=rowmin";

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}
	
	public PaginadorBind(String query, String[] cabeceras, Hashtable<Integer,Object> codigos) throws ClsExceptions {
		try {
			setCodigosInicio(codigos);
			setQueryInicio(query);
			inicializar(query, codigos);
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
	
	protected RowsContainer ejecutarConsulta(RowsContainer rc, String query, Hashtable<?,?> codigos) throws ClsExceptions {
		rc.queryNLSBind(query, codigos);
		
		return rc;
	}

	protected String sustituirMax(Matcher matcher, int rowmax, int posicion) {
		return matcher.replaceAll(":"+posicion);
	}

	protected String sustituirMin(Matcher matcher, int rowmin, int posicion) {
		return matcher.replaceAll(":"+posicion);
	}
	
	public Hashtable<Integer,Object> getCodigosInicio() {
		return codigosInicio;
	}

	private void setCodigosInicio(Hashtable<Integer,Object> codigosInicio) {
		this.codigosInicio = codigosInicio;
	}
}
