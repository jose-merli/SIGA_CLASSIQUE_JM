/*
 * VERSIONES:
 * 
 * miguel.villegas - 20-01-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CEN_ESTAOSOLICITUDMODIF de la BBDD
* 
*/
public class CenEstadoSolicitudModifAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenEstadoSolicitudModifAdm(UsrBean usu) {
		super(CenEstadoSolicitudModifBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenEstadoSolicitudModifBean.C_IDESTADOSOLIC,
							CenEstadoSolicitudModifBean.C_DESCRIPCION,							
							CenEstadoSolicitudModifBean.C_FECHAMODIFICACION,
							CenEstadoSolicitudModifBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenEstadoSolicitudModifBean.C_IDESTADOSOLIC};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  MasterBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenEstadoSolicitudModifBean bean = null;
		
		try {
			bean = new CenEstadoSolicitudModifBean();
			bean.setIdEstadoSolic (UtilidadesHash.getInteger(hash,CenEstadoSolicitudModifBean.C_IDESTADOSOLIC));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenEstadoSolicitudModifBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenEstadoSolicitudModifBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenEstadoSolicitudModifBean.C_USUMODIFICACION));			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenEstadoSolicitudModifBean b = (CenEstadoSolicitudModifBean) bean;
			UtilidadesHash.set(htData,CenEstadoSolicitudModifBean.C_IDESTADOSOLIC,b.getIdEstadoSolic());
			UtilidadesHash.set(htData,CenEstadoSolicitudModifBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenEstadoSolicitudModifBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,CenEstadoSolicitudModifBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
			
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  id - identificador del tipo de estado 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public MasterBean obtenerEntradaEstadoSolicitudModif (String id) throws ClsExceptions, SIGAException {
		
		   CenEstadoSolicitudModifBean datos=new CenEstadoSolicitudModifBean();
		
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_IDESTADOSOLIC + "," +							
    						UtilidadesMultidioma.getCampoMultidioma(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage())  + "," +
							CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_FECHAMODIFICACION + "," +
	            			CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_USUMODIFICACION +
							" FROM " + CenEstadoSolicitudModifBean.T_NOMBRETABLA + 
							" WHERE " +
							CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC + "=" + id;
														
	            if (rc.find(sql)) {
		               for (int i = 0; i < rc.size(); i++){
		                  Row fila = (Row) rc.get(i);
		                  datos.setIdEstadoSolic(Integer.valueOf(fila.getString(CenEstadoSolicitudModifBean.C_IDESTADOSOLIC)));
		                  datos.setDescripcion(fila.getString(CenEstadoSolicitudModifBean.C_DESCRIPCION));
		                  datos.setUsuMod(Integer.valueOf(fila.getString(CenEstadoSolicitudModifBean.C_USUMODIFICACION)));
		                  datos.setFechaMod(fila.getString(CenEstadoSolicitudModifBean.C_FECHAMODIFICACION));	                  
		               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla.");
	       }
	       return datos;                        
	    }
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	
}
