/*
 * Created on Dec 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsDictamenEJGAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public ScsDictamenEJGAdm(UsrBean usu){
		super(ScsDictamenEJGBean.T_NOMBRETABLA, usu);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {ScsDictamenEJGBean.C_ABREVIATURA,ScsDictamenEJGBean.C_DESCRIPCION,ScsDictamenEJGBean.C_IDINSTITUCION,ScsDictamenEJGBean.C_IDDICTAMEN,ScsDictamenEJGBean.C_IDTIPODICTAMEN,ScsDictamenEJGBean.C_IDFUNDAMENTO,ScsDictamenEJGBean.C_CODIGOEXT};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {ScsDictamenEJGBean.C_IDINSTITUCION,ScsDictamenEJGBean.C_IDDICTAMEN};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ScsDictamenEJGBean bean = null;
		
		try {
			bean = new ScsDictamenEJGBean();
			bean.setAbreviatura((String)hash.get(ScsDictamenEJGBean.C_ABREVIATURA));			
			bean.setDescripcion((String)hash.get(ScsDictamenEJGBean.C_DESCRIPCION));		
			bean.setIdInstitucion((String)hash.get(ScsDictamenEJGBean.C_IDINSTITUCION));		
			bean.setIdDictamen((String)hash.get(ScsDictamenEJGBean.C_IDDICTAMEN));
			bean.setIdTipoDictamen((String)hash.get(ScsDictamenEJGBean.C_IDTIPODICTAMEN));
			bean.setIdFundamento((String)hash.get(ScsDictamenEJGBean.C_IDFUNDAMENTO));
			bean.setCodigoExt((String)hash.get(ScsDictamenEJGBean.C_CODIGOEXT));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {

		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsDictamenEJGBean b = (ScsDictamenEJGBean) bean;
			htData.put(ScsDictamenEJGBean.C_ABREVIATURA, b.getAbreviatura());		
			htData.put(ScsDictamenEJGBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(ScsDictamenEJGBean.C_IDDICTAMEN, b.getIdDictamen());
			htData.put(ScsDictamenEJGBean.C_IDTIPODICTAMEN, b.getIdTipoDictamen());
			htData.put(ScsDictamenEJGBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(ScsDictamenEJGBean.C_IDFUNDAMENTO, b.getIdFundamento());
			htData.put(ScsDictamenEJGBean.C_CODIGOEXT, b.getCodigoExt());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
  			String sql ="SELECT (MAX("+ScsDictamenEJGBean.C_IDDICTAMEN+") + 1) AS IDDICTAMENEJG" +
  					" FROM "+ScsDictamenEJGBean.T_NOMBRETABLA+" WHERE " +
  					ScsDictamenEJGBean.C_IDINSTITUCION+" = "+ entrada.get(ScsDictamenEJGBean.C_IDINSTITUCION);
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDDICTAMENEJG").equals("")) {
					entrada.put(ScsDictamenEJGBean.C_IDDICTAMEN,"1");
				}
				else entrada.put(ScsDictamenEJGBean.C_IDDICTAMEN,(String)prueba.get("IDDICTAMENEJG"));				
			}
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE DICTAMEN");
		};
		
		return entrada;
	}		
	
}
