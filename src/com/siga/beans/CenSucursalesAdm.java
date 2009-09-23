/*
 * VERSIONES:
 * 
 * miguel.villegas - 04-01-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CENSUCURSALES de la BBDD
* 
*/
public class CenSucursalesAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenSucursalesAdm(UsrBean usu) {
		super(CenSucursalesBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenSucursalesBean.C_BANCOS_CODIGO,
							CenSucursalesBean.C_COD_SUCURSAL,
							CenSucursalesBean.C_NOMBRE,
							CenSucursalesBean.C_DOMICILIO,							
							CenSucursalesBean.C_CODIGOPOSTAL,
							CenSucursalesBean.C_COD_PLAZA};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenSucursalesBean.C_BANCOS_CODIGO,
							CenSucursalesBean.C_COD_SUCURSAL};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSucursalesBean bean = null;
		
		try {
			bean = new CenSucursalesBean();
			bean.setBancosCodigo (UtilidadesHash.getString(hash,CenSucursalesBean.C_BANCOS_CODIGO));
			bean.setCodSucursal(UtilidadesHash.getString(hash,CenSucursalesBean.C_COD_SUCURSAL));
			bean.setNombre (UtilidadesHash.getString(hash,CenSucursalesBean.C_NOMBRE));
			bean.setDomicilio (UtilidadesHash.getString(hash,CenSucursalesBean.C_DOMICILIO));			
			bean.setCodigoPostal (UtilidadesHash.getString(hash,CenSucursalesBean.C_CODIGOPOSTAL));
			bean.setCodPlaza(UtilidadesHash.getString(hash,CenSucursalesBean.C_COD_PLAZA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSucursalesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSucursalesBean.C_USUMODIFICACION));			
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
			CenSucursalesBean b = (CenSucursalesBean) bean;
			UtilidadesHash.set(htData,CenSucursalesBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData,CenSucursalesBean.C_COD_SUCURSAL,b.getCodSucursal());
			UtilidadesHash.set(htData,CenSucursalesBean.C_NOMBRE,b.getNombre());
			UtilidadesHash.set(htData,CenSucursalesBean.C_DOMICILIO,b.getDomicilio());			
			UtilidadesHash.set(htData,CenSucursalesBean.C_CODIGOPOSTAL,b.getCodigoPostal());
			UtilidadesHash.set(htData,CenSucursalesBean.C_COD_PLAZA,b.getCodPlaza());
			UtilidadesHash.set(htData,CenSucursalesBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,CenSucursalesBean.C_USUMODIFICACION,b.getUsuMod());
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
	 * Recoge los datos de una sucursal especifica
	 * @param  codigoBanco - identificador del banco
	 * @param  codSucursal - identificador de la sucursal
	 * @return  Hashtable - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable getSucursal(String codigoBanco, String codigoSucursal) throws ClsExceptions,SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_BANCOS_CODIGO + "," +
	            			CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_COD_SUCURSAL + "," +
	            			CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_NOMBRE + "," +
	            			CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_DOMICILIO + "," +
	            			CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_CODIGOPOSTAL + "," +
	            			CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_COD_PLAZA + "," +
	            			CenPlazasBean.T_NOMBRETABLA + "." + CenPlazasBean.C_NOMBRE + " as PLAZA " + 							
							" FROM " + 
							CenSucursalesBean.T_NOMBRETABLA +
								" INNER JOIN " + CenPlazasBean.T_NOMBRETABLA +
									" ON " + CenPlazasBean.T_NOMBRETABLA + "." + CenPlazasBean.C_COD_PLAZA + "=" + 
											 CenSucursalesBean.T_NOMBRETABLA + "." + CenSucursalesBean.C_COD_PLAZA +
	            			" WHERE " + 
							CenSucursalesBean.T_NOMBRETABLA +"."+ CenSucursalesBean.C_BANCOS_CODIGO + "=" + codigoBanco +
							" AND " +
							CenSucursalesBean.T_NOMBRETABLA +"."+ CenSucursalesBean.C_COD_SUCURSAL + "=" + codigoSucursal;
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            }
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener datos de la sucursal");
	       }
	       return datos;                        
	    }
	
}
