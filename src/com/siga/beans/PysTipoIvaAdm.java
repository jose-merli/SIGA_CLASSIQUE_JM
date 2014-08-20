/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 16-03-2004 - Creación
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
* Clase que gestiona la tabla PYS_TIPOIVA de la BBDD
*/
public class PysTipoIvaAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public PysTipoIvaAdm(UsrBean usu)	{
		super (PysTipoIvaBean.T_NOMBRETABLA, usu);
	
	}
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysTipoIvaBean.C_IDTIPOIVA,
				PysTipoIvaBean.C_DESCRIPCION,
				PysTipoIvaBean.C_CODIGOEXT,
				PysTipoIvaBean.C_VALOR,
				PysTipoIvaBean.C_SUBCTATIPO,
				PysTipoIvaBean.C_DESCRIPCIONTIPO};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysTipoIvaBean.C_IDTIPOIVA};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysTipoIvaBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysTipoIvaBean bean = null;
		
		try {
			bean = new PysTipoIvaBean();
			bean.setIdTipoIva(UtilidadesHash.getInteger(hash,PysTipoIvaBean.C_IDTIPOIVA));
			bean.setDescripcion(UtilidadesHash.getString(hash,PysTipoIvaBean.C_DESCRIPCION));			
			bean.setCodigoExt(UtilidadesHash.getString(hash,PysTipoIvaBean.C_CODIGOEXT));
			bean.setValor(UtilidadesHash.getString(hash,PysTipoIvaBean.C_VALOR));
			bean.setSubCtaTipo(UtilidadesHash.getString(hash,PysTipoIvaBean.C_SUBCTATIPO));	
			bean.setDescripciontipo(UtilidadesHash.getString(hash,PysTipoIvaBean.C_DESCRIPCIONTIPO));	
			
		} catch (Exception e) { 
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
			PysTipoIvaBean b = (PysTipoIvaBean) bean; 
			UtilidadesHash.set(htData,PysTipoIvaBean.C_IDTIPOIVA, b.getIdTipoIva());
			UtilidadesHash.set(htData,PysTipoIvaBean.C_DESCRIPCION, b.getDescripcion());			
			UtilidadesHash.set(htData,PysTipoIvaBean.C_CODIGOEXT,b.getCodigoExt());
			UtilidadesHash.set(htData,PysTipoIvaBean.C_VALOR, b.getValor());
			UtilidadesHash.set(htData,PysTipoIvaBean.C_SUBCTATIPO, b.getSubCtaTipo());
			UtilidadesHash.set(htData,PysTipoIvaBean.C_DESCRIPCIONTIPO, b.getDescripciontipo());
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
		String [] claves = {PysTipoIvaBean.C_IDTIPOIVA};		
		return claves;
	}
	
	/**
	 * Obtener el valor para FAC_LINEAFACTURA.CTAIVA
	 * @param idInstitucion
	 * @param idTipoIva
	 * @return
	 * @throws ClsExceptions
	 */
	public String obtenerCTAIVA (String idInstitucion, String idTipoIva) throws ClsExceptions {
		   String sCTAIVA = "";
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql = "SELECT F_SIGA_GETPARAMETRO('FAC', 'CONTABILIDAD_IVA', " + idInstitucion + ") || " + 
	            				PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_SUBCTATIPO + " AS " + FacLineaFacturaBean.C_CTAIVA +
							" FROM " + PysTipoIvaBean.T_NOMBRETABLA + 
							" WHERE " + PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_IDTIPOIVA + " = " + idTipoIva;
														
	            if (rc.find(sql) && rc.size()==1) {
	            	Row fila = (Row) rc.get(0);
	            	sCTAIVA = fila.getString(FacLineaFacturaBean.C_CTAIVA);
	            }
	            
	       } catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el valor de CTAIVA");
	       }
	       
	       return sCTAIVA;                        
	    }		
}
