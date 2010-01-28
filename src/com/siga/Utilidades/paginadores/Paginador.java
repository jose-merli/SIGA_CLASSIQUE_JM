package com.siga.Utilidades.paginadores;

import java.util.Hashtable;
import java.util.regex.Matcher;

import com.atos.utils.ClsExceptions;
import com.atos.utils.RowsContainer;

public class Paginador<E> extends PaginadorAdapter<E> {
	private static final long serialVersionUID = 7474782809973965145L;

	public Paginador(String query) throws ClsExceptions {
		this(query, query);
	}

	public Paginador(String query1, String query2) throws ClsExceptions {
		try {
			setQueryInicio(query1);
			inicializar(query2, null);

			this.query = "SELECT /*+ no_merge */ * FROM (SELECT /*+ no_merge */ a.*, rownum r FROM (" + query1
					+ ") a WHERE rownum<=rowmax) WHERE r>=rowmin";

		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	public Paginador(String query, String[] cabeceras) throws ClsExceptions {
		try {
			setQueryInicio(query);
			inicializar(query, null);
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
		rc.queryNLS(query);
		
		return rc;
	}

	protected String sustituirMax(Matcher matcher, int rowmax, int posicion) {
		return matcher.replaceAll(String.valueOf(rowmax));
	}

	protected String sustituirMin(Matcher matcher, int rowmin, int posicion) {
		return matcher.replaceAll(String.valueOf(rowmin));
	}
}
