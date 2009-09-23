
package com.siga.consultas.action;

import java.util.*;

import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


/*
 * Created on 11-abr-2005
 *
 */

/**
 * @author daniel.campos
 *
 */
public class ConsultasBD {

	private GrafoUni bdUni  = null;
	private GrafoBi bdBi = null;
	public final String FROM = Grafo.FROM;
	public final String WHERE = Grafo.WHERE;
	public final String ESTADO = Grafo.ESTADO;
	
	/**
	 * Crea un objeto consulta, reproduciendo en memoria la BD
	 * @throws SIGAException
	 */
	public ConsultasBD() throws SIGAException {
		try {
			this.bdUni = new GrafoUni();
			this.bdBi = new GrafoBi();
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
	/**
	 * getFromWhere
	 * Obtiene el From y el Where necesario para relacionar las tabas que recibe
	 * @param Tablas a relacionar 
	 * @return <br> 
	 * 1. En caso de no encontrar relacion entre las tablas: null <br>
	 * 2. En caso de obtener la relacion un Hash con las claves: 
	 * <br> ESTADO: String con "OK" si se ha encontrado relacion
	 * <br> FROM: String con las tablas que utilizaremos en la consulta
	 * <br> WHERE: String con las uniones necesarias para utilizar la consulta
	 * @throws SIGAException
	 */
	public Hashtable getFromWhere (String tablas[]) throws SIGAException {
		try {
			Hashtable h = null; 
			if (tablas.length < 1) {
				return h;
			}
			if (tablas.length == 1) {
				h = new Hashtable ();
				UtilidadesHash.set(h, this.FROM, tablas[0]);
				UtilidadesHash.set(h, this.WHERE, "");
				UtilidadesHash.set(h, this.ESTADO, Grafo.OK_HAY_CAMINO);
				return h;
			}
			
			h = this.bdUni.getFromWhereConsulta(tablas);
			if (h!=null && ((String)h.get(this.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){
			    this.printResultadoEnLog(h);
				return h;
			}

			h = this.bdBi.getFromWhereConsulta(tablas);
			if (h!=null && ((String)h.get(this.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){
			    this.printResultadoEnLog(h);
				return h;
			}
			
			return null;
		}
		catch (SIGAException e)	{
			throw e;
		}
	}
	
	private void printResultadoEnLog (Hashtable h) 
	{
	    if (h != null) {
			ClsLogging.writeFileLog("-------------------------------------------------", 10); 
			ClsLogging.writeFileLog("Resultado de la deduccion de la consulta: ", 10); 
			ClsLogging.writeFileLog("   FROM: " + h.get(Grafo.FROM),  10); 
			ClsLogging.writeFileLog("  WHERE: " + h.get(Grafo.WHERE), 10); 
			ClsLogging.writeFileLog("-------------------------------------------------", 10);
	    }
	}
	
}
