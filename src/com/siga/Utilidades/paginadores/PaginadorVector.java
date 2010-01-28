package com.siga.Utilidades.paginadores;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;

import com.atos.utils.ClsExceptions;
import com.atos.utils.RowsContainer;

public class PaginadorVector<E> extends PaginadorAdapter<E> {
	private static final long serialVersionUID = 6287903263299224048L;
	private List<E> datos=null;

	public PaginadorVector(List<E> v) throws ClsExceptions {
		try {
			if (v != null) {
				datos = v;
			} else { 
				datos = new ArrayList<E>();
			}
			inicializar();
		} 
		catch (Exception e) {
		}
	}

	private void inicializar() throws ClsExceptions {
		if (datos != null) {
			calculaPaginas(datos.size());
		} else {
			calculaPaginas(0);
		}
	}
	
	public PaginadorVector(List<E> vDatos, String[] cabeceras) throws ClsExceptions {
		this(vDatos);
	}

	public void setCache(int rowmin, int rowmax) throws ClsExceptions {
		this.cache = this.datos;
	}

	protected RowsContainer ejecutarConsulta(RowsContainer rc, String query, Hashtable<?, ?> codigos) throws ClsExceptions {
		return null;
	}

	protected String sustituirMax(Matcher matcher, int rowmax, int posicion) {
		return null;
	}

	protected String sustituirMin(Matcher matcher, int rowmin, int posicion) {
		return null;
	}
}
