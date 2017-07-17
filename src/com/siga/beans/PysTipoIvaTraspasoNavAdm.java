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
* Clase que gestiona la tabla PYS_TIPOIVA_TRASPASONAV de la BBDD
*/
public class PysTipoIvaTraspasoNavAdm extends MasterBeanAdministrador {

	public PysTipoIvaTraspasoNavAdm(UsrBean usu)	{
		super (PysTipoIvaTraspasoNavBean.T_NOMBRETABLA, usu);
	
	}
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysTipoIvaTraspasoNavBean.C_IDTIPOIVA,
				PysTipoIvaTraspasoNavBean.C_IDINSTITUCION, 
				PysTipoIvaTraspasoNavBean.C_CODIGO_TRASPASONAV};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysTipoIvaTraspasoNavBean.C_IDTIPOIVA};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysTipoIvaBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysTipoIvaTraspasoNavBean bean = null;
		
		try {
			bean = new PysTipoIvaTraspasoNavBean();
			bean.setIdTipoIva(UtilidadesHash.getInteger(hash,PysTipoIvaTraspasoNavBean.C_IDTIPOIVA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysTipoIvaTraspasoNavBean.C_IDINSTITUCION));
			bean.setCodigoTraspasoNav(UtilidadesHash.getString(hash,PysTipoIvaTraspasoNavBean.C_CODIGO_TRASPASONAV));
			
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
			PysTipoIvaTraspasoNavBean b = (PysTipoIvaTraspasoNavBean) bean; 
			UtilidadesHash.set(htData, PysTipoIvaTraspasoNavBean.C_IDTIPOIVA, b.getIdTipoIva());
			UtilidadesHash.set(htData, PysTipoIvaTraspasoNavBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, PysTipoIvaTraspasoNavBean.C_CODIGO_TRASPASONAV, b.getCodigoTraspasoNav());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		String [] claves = {PysTipoIvaTraspasoNavBean.C_IDTIPOIVA};		
		return claves;
	}
	
	/**
	 * Obtener el valor para FAC_LINEAFACTURA.CTAIVA
	 * @param idInstitucion
	 * @param idTipoIva
	 * @return
	 * @throws ClsExceptions
	 */
	/*public String obtenerCTAIVA (String idInstitucion, String idTipoIva) throws ClsExceptions {
		   String sCTAIVA = "";
	       try {
	            RowsContainer rc = new RowsContainer();
	            String sql = "SELECT F_SIGA_GETPARAMETRO('FAC', 'CONTABILIDAD_IVA', " + idInstitucion + ") || " + 
	            				PysTipoIvaTraspasoNavBean.T_NOMBRETABLA + "." + PysTipoIvaTraspasoNavBean.C_SUBCTATIPO + " AS " + FacLineaFacturaBean.C_CTAIVA +
							" FROM " + PysTipoIvaTraspasoNavBean.T_NOMBRETABLA + 
							" WHERE " + PysTipoIvaTraspasoNavBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_IDTIPOIVA + " = " + idTipoIva;
	            
	            if (rc.find(sql) && rc.size()==1) {
	            	Row fila = (Row) rc.get(0);
	            	sCTAIVA = fila.getString(FacLineaFacturaBean.C_CTAIVA);
	            }
	            
	       } catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el valor de CTAIVA");
	       }
	       
	       return sCTAIVA;
	    }*/
}
