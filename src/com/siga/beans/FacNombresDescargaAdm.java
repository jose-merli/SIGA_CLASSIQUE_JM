/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacNombresDescargaAdm extends MasterBeanAdministrador {
	
	public FacNombresDescargaAdm (UsrBean usu) {
		super (FacNombresDescargaBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacNombresDescargaBean.C_ID, 		
							FacNombresDescargaBean.C_NOMBRE};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacNombresDescargaBean.C_ID};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacNombresDescargaBean bean = null;
		
		try {
			bean = new FacNombresDescargaBean();
			bean.setId	(UtilidadesHash.getInteger(hash, FacNombresDescargaBean.C_ID));
			bean.setNombre(UtilidadesHash.getString(hash, FacNombresDescargaBean.C_NOMBRE));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacNombresDescargaBean b = (FacNombresDescargaBean) bean;
			UtilidadesHash.set(htData, FacNombresDescargaBean.C_ID, b.getId());
			UtilidadesHash.set(htData, FacNombresDescargaBean.C_NOMBRE, b.getNombre());
			
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
	
	
	
	/** 
	 * Obtenemos el formato del que debe de llevar el nombre de los ficheros de facturación al descargarlos
	 * @param  idNombreDescargaPDF - identificador del nombre
	 * @return  String - Formato del nombre de descarga
	 *
	 */	
	public String getNombreDescargarPDF(Integer idNombreDescargaPDF) throws ClsExceptions,SIGAException {
		
		  String nombre = null;
          RowsContainer rc = new RowsContainer(); 
          Hashtable codigos = new Hashtable();
	       try {
	            codigos.put(new Integer(1), idNombreDescargaPDF);
	            String sql ="SELECT " +
	            		FacNombresDescargaBean.T_NOMBRETABLA + "." + FacNombresDescargaBean.C_ID + "," +
	            		FacNombresDescargaBean.T_NOMBRETABLA + "." + FacNombresDescargaBean.C_NOMBRE +
							" FROM " + FacNombresDescargaBean.T_NOMBRETABLA +
								" WHERE " +FacNombresDescargaBean.T_NOMBRETABLA +"."+ FacNombresDescargaBean.C_ID + "= :1" ;
	            if (rc.findBind(sql,codigos)) {
	               if(rc != null && rc.size()>0){
	            	   Row fila = (Row) rc.get(0);
	            	   nombre = (String)fila.getRow().get(FacNombresDescargaBean.C_NOMBRE);
	               }
	            }
	       }
	       catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al obtener el nombre");
	       } 
	       return nombre;
	    }
	
}
