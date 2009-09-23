
package com.siga.consultas.action;

import java.util.Hashtable;

import com.siga.general.SIGAException;

/*
 * Created on 09-jun-2005
 *
 */

/**
 * @author daniel.campos
 *
 */
public interface Grafo {

    static public final String ERROR_NO_HAY_CAMINO = "NO SE HA LOCALIZADO UN CAMINO PARA RELACCIONAR LAS TABLAS";
    static public final String OK_HAY_CAMINO = "OK";
    static public final String ESTADO 	= "ESTADO";
    static public final String FROM 	= "FROM";
    static public final String WHERE 	= "WHERE";

    /**
	 * getFromWhereConsulta
	 * Obtiene el From y el Where necesarios que relaciona el conjunto de tablas que recibe
	 * @param Conjuento de tablas a obtener como consulta
	 * @return Hashtable con los datos ESTADO, FROM y WHERE
	 * @throws SIGAException
	 */
    public Hashtable getFromWhereConsulta (String sTablas[]) throws SIGAException;
}
