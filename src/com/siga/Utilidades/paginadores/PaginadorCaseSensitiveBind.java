package com.siga.Utilidades.paginadores;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.RowsContainer;

public class PaginadorCaseSensitiveBind<E> extends PaginadorBind<E> {
	private static final long serialVersionUID = 7263566353499346240L;

	public PaginadorCaseSensitiveBind(String query, Hashtable<Integer, Object> codigos) throws ClsExceptions {
		super(query, codigos);
	}

	public PaginadorCaseSensitiveBind(String query1, String query2, Hashtable<Integer, Object> codigos) throws ClsExceptions {
		super(query1, query2, codigos);
	}

	public PaginadorCaseSensitiveBind(String query, String[] cabeceras, Hashtable<Integer, Object> codigos) throws ClsExceptions {
		super(query, cabeceras, codigos);
	}

	protected RowsContainer ejecutarConsulta(RowsContainer rc, String query, Hashtable<?,?> codigos) throws ClsExceptions {
		rc.queryBind(query, codigos);
		
		return rc;
	}
}
