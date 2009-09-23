/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;


public class GenClientesTemporalAdm extends MasterBeanAdministrador {
	
	public GenClientesTemporalAdm (UsrBean usu) {
		super (GenClientesTemporalBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {GenClientesTemporalBean.C_IDINSTITUCION, 		
				    	    GenClientesTemporalBean.C_CONTADOR,
							GenClientesTemporalBean.C_POSICION, 	
							GenClientesTemporalBean.C_IDPERSONA,
							GenClientesTemporalBean.C_FECHA,	
							GenClientesTemporalBean.C_SALTO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {GenClientesTemporalBean.C_IDINSTITUCION, GenClientesTemporalBean.C_CONTADOR};
		return claves;
	}
	
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		GenClientesTemporalBean bean = null;
		
		try {
			bean = new GenClientesTemporalBean();
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, GenClientesTemporalBean.C_IDINSTITUCION));
			bean.setContador		(UtilidadesHash.getLong(hash, GenClientesTemporalBean.C_CONTADOR));
			bean.setPosicion		(UtilidadesHash.getInteger(hash, GenClientesTemporalBean.C_POSICION));
			bean.setIdPersona		(UtilidadesHash.getLong(hash, GenClientesTemporalBean.C_IDPERSONA));
			bean.setFecha	 		(UtilidadesHash.getString(hash, GenClientesTemporalBean.C_FECHA));
			bean.setSalto			(UtilidadesHash.getString(hash, GenClientesTemporalBean.C_SALTO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			GenClientesTemporalBean b = (GenClientesTemporalBean) bean;
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_CONTADOR, b.getContador());
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_POSICION, b.getPosicion());
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, GenClientesTemporalBean.C_SALTO, b.getSalto());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param boolean bRW: true si usamos el pool de Lectura/Escritura.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta, boolean bRW) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			//Si uso el pool de lectura/escritura
			if (bRW)
				salida = rc.findForUpdate(consulta);
			else
				salida = rc.query(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en GenClientesTemporalAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		

	public ArrayList obtenerLetradosPosiblesPL (Integer idInstitucion, Integer idTurno, Integer idGuardia) {
		ArrayList arrayLetrados = new ArrayList();
		String posicionTablaTemporal = null; 
		GenClientesTemporalAdm admClientesTmp = new GenClientesTemporalAdm(this.usrbean); 
		ArrayList arrayClientesTmp = null;
		
		try {
			//LLamo al PL que me inserta en una tabla temporal los letrados para hacer las guardias con un indice posicion + idinstitucion
		    String resultado[] = new String[3];
		    //Ejecucion del PL
		    resultado = EjecucionPLs.ejecutarPL_OrdenaColegiadosGuardia(idInstitucion, idTurno, idGuardia);
		
		    //Si el resultado del segundo parametro es 0 todo ha ido bien: guardo la posicion.
		    if (resultado[1]!=null && resultado[1].equals("0")) {
		    	posicionTablaTemporal = resultado[0];
			
			    //Creo un Vector con HashTable en el que guardo ordenadamente el idpersona + salto:
			    String consulta = "SELECT "+GenClientesTemporalBean.C_IDPERSONA+","+GenClientesTemporalBean.C_SALTO+
								  " FROM "+GenClientesTemporalBean.T_NOMBRETABLA+
								  " WHERE "+GenClientesTemporalBean.C_IDINSTITUCION+"="+idInstitucion+
								  " AND "+GenClientesTemporalBean.C_CONTADOR+"="+posicionTablaTemporal+
								  " ORDER BY "+GenClientesTemporalBean.C_POSICION;
			    
			    arrayClientesTmp = new ArrayList();
				RowsContainer rc = new RowsContainer(); 	
				if (rc.query(consulta)) {
					for (int i = 0; i < rc.size(); i++)	{		
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow();
						if (registro != null) {
							Long idPersona = new Long((String)registro.get(GenClientesTemporalBean.C_IDPERSONA));
							String saltoCompensacion  = (String)registro.get(GenClientesTemporalBean.C_SALTO);
							LetradoGuardia letradoGuardia = new LetradoGuardia(idPersona,
																			   idInstitucion,
																			   idTurno,
																			   idGuardia,
																			   saltoCompensacion);  
							arrayClientesTmp.add(letradoGuardia);
						}
					}
				}		    
		    } else
		    	arrayClientesTmp = null;	
		} catch (Exception e){
			arrayClientesTmp = null;
		}
    	return arrayClientesTmp;
	}
	
}
