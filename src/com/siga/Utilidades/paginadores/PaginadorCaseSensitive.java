package com.siga.Utilidades.paginadores;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.RowsContainer;

public class PaginadorCaseSensitive<E> extends Paginador<E> {
	private static final long serialVersionUID = 1342806826541613978L;

	public PaginadorCaseSensitive(String query) throws ClsExceptions {
		super(query);
	}

	public PaginadorCaseSensitive(String query1, String query2) throws ClsExceptions {
		super(query1, query2);
	}

	public PaginadorCaseSensitive(String query, String[] cabeceras) throws ClsExceptions {
		super(query, cabeceras);
	}

	protected RowsContainer ejecutarConsulta(RowsContainer rc, String query, Hashtable<?,?> codigos) throws ClsExceptions {
		rc.query(query);
		
		return rc;
	}
}
