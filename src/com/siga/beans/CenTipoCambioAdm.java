/*
 * VERSIONES:
 * 
 * miguel.villegas - 23-12-2004 - Funciones de accesos a BBDDs y relacionadas con el Tipo de Cambio
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla PYS_PRODUCTOSINSTITUCION de la BBDD
* 
*/
public class CenTipoCambioAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenTipoCambioAdm(UsrBean usu) {
		super(CenTipoCambioBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenTipoCambioBean.C_IDTIPOCAMBIO,
							CenTipoCambioBean.C_DESCRIPCION,
							CenTipoCambioBean.C_FECHAMODIFICACION,
							CenTipoCambioBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenHistoricoBean.C_IDTIPOCAMBIO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenTipoCambioBean bean = null;
		
		try {
			bean = new CenTipoCambioBean();
			bean.setIdTipoCambio (UtilidadesHash.getInteger(hash,CenTipoCambioBean.C_IDTIPOCAMBIO));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTipoCambioBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenTipoCambioBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTipoCambioBean.C_USUMODIFICACION));			
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
			CenTipoCambioBean b = (CenTipoCambioBean) bean;
			UtilidadesHash.set(htData,CenTipoCambioBean.C_IDTIPOCAMBIO ,b.getIdTipoCambio());
			UtilidadesHash.set(htData,CenTipoCambioBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenTipoCambioBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,CenTipoCambioBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
		
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idPers - identificador de la persona 
	 * @param  idInst - identificador de la institucion
	 * @param  idHistor - identificador del historico	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerDescripcion (String idTCamb) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_IDTIPOCAMBIO  + "," +
							UtilidadesMultidioma.getCampoMultidioma(CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_DESCRIPCION, this.usrbean.getLanguage())  + 
							" FROM " + CenTipoCambioBean.T_NOMBRETABLA + 
							" WHERE " +
							CenTipoCambioBean.T_NOMBRETABLA +"."+ CenTipoCambioBean.C_IDTIPOCAMBIO + "=" + idTCamb;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}

	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la descripcion sobre una entrada de la tabla TIPOCAMBIO.");
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
