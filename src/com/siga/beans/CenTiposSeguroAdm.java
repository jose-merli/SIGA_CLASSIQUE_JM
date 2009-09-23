/*
 * Created on 25-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CEN_TIPOSSEGURO de la BBDD
* 
*/
public class CenTiposSeguroAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenTiposSeguroAdm(UsrBean usu) {
		super(CenTiposSeguroBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {CenTiposSeguroBean.C_IDTIPOSSEGURO, 	CenTiposSeguroBean.C_FECHAMODIFICACION,CenTiposSeguroBean.C_CODIGOEXT,
				CenTiposSeguroBean.C_NOMBRE, CenTiposSeguroBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {CenTiposSeguroBean.C_IDTIPOSSEGURO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenTiposSeguroBean bean = null;
		
		try {
			bean = new CenTiposSeguroBean();
			bean.setNombre((String)hash.get(CenTiposSeguroBean.C_NOMBRE));
			bean.setCodigoExt((String)hash.get(CenTiposSeguroBean.C_CODIGOEXT));
			bean.setFechaMod((String)hash.get(CenTiposSeguroBean.C_FECHAMODIFICACION));
			bean.setIdTiposSeguro(UtilidadesHash.getInteger(hash, CenTiposSeguroBean.C_IDTIPOSSEGURO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTiposSeguroBean.C_USUMODIFICACION));
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
			CenTiposSeguroBean b = (CenTiposSeguroBean) bean;
			htData.put(CenTiposSeguroBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenTiposSeguroBean.C_NOMBRE, b.getNombre());
			htData.put(CenTiposSeguroBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTiposSeguroBean.C_IDTIPOSSEGURO, String.valueOf(b.getIdTiposSeguro()));
			htData.put(CenTiposSeguroBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idSeguro - identificador del tipo seguro 
	 * @return  Vector - Vector con las filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaTiposSeguro (String idSeguro) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			CenTiposSeguroBean.T_NOMBRETABLA + "." + CenTiposSeguroBean.C_IDTIPOSSEGURO  + "," +
	            			UtilidadesMultidioma.getCampoMultidioma(CenTiposSeguroBean.T_NOMBRETABLA + "." + CenTiposSeguroBean.C_NOMBRE,this.usrbean.getLanguage()) +
							" FROM " + CenTiposSeguroBean.T_NOMBRETABLA + 
							" WHERE " +
							CenTiposSeguroBean.T_NOMBRETABLA +"."+ CenTiposSeguroBean.C_IDTIPOSSEGURO + "=" + idSeguro;
														
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de los tipos de seguro.");
	       }
	       return datos;                        
	    }
	

}
