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
* Clase que gestiona la tabla CEN_TIPOSMODIFICACIONES de la BBDD
* 
*/
public class CenTiposModificacionesAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenTiposModificacionesAdm(UsrBean usu) {
		super(CenTiposModificacionesBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenTiposModificacionesBean.C_IDTIPOMODIFICACION,
							CenTiposModificacionesBean.C_DESCRIPCION,							
							CenTiposModificacionesBean.C_FECHAMODIFICACION,
							CenTiposModificacionesBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenTiposModificacionesBean.C_IDTIPOMODIFICACION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  MasterBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenTiposModificacionesBean bean = null;
		
		try {
			bean = new CenTiposModificacionesBean();
			bean.setIdTipoModificacion (UtilidadesHash.getInteger(hash,CenTiposModificacionesBean.C_IDTIPOMODIFICACION));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTiposModificacionesBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenTiposModificacionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTiposModificacionesBean.C_USUMODIFICACION));			
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
			CenTiposModificacionesBean b = (CenTiposModificacionesBean) bean;
			UtilidadesHash.set(htData,CenTiposModificacionesBean.C_IDTIPOMODIFICACION,b.getIdTipoModificacion());
			UtilidadesHash.set(htData,CenTiposModificacionesBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenTiposModificacionesBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,CenTiposModificacionesBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
			
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  id - identificador del tipo de modificacion 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public MasterBean obtenerEntradaTiposModificaciones (String id) throws ClsExceptions, SIGAException {
		
		   CenTiposModificacionesBean datos=new CenTiposModificacionesBean();
		
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_IDTIPOMODIFICACION + "," +							
							UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage()) + "," +
							CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_FECHAMODIFICACION + "," +
	            			CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_USUMODIFICACION +
							" FROM " + CenTiposModificacionesBean.T_NOMBRETABLA + 
							" WHERE " +
							CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenTiposModificacionesBean.C_IDTIPOMODIFICACION + "=" + id;
														
	            if (rc.find(sql)) {
		               for (int i = 0; i < rc.size(); i++){
		                  Row fila = (Row) rc.get(i);
		                  datos.setIdTipoModificacion(Integer.valueOf(fila.getString(CenTiposModificacionesBean.C_IDTIPOMODIFICACION)));
		                  datos.setDescripcion(fila.getString(CenTiposModificacionesBean.C_DESCRIPCION));
		                  datos.setUsuMod(Integer.valueOf(fila.getString(CenTiposModificacionesBean.C_USUMODIFICACION)));
		                  datos.setFechaMod(fila.getString(CenTiposModificacionesBean.C_FECHAMODIFICACION));	                  
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
