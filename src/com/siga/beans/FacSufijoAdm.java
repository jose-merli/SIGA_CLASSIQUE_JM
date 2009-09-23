package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador de FAC_SUFIJO
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class FacSufijoAdm extends MasterBeanAdmVisible {

	
	/** 
	 * Constructor
	 * @param  usu - Usuario
	 */	
	public FacSufijoAdm(UsrBean usu) {
		super(FacSufijoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 * Devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {FacSufijoBean.C_IDINSTITUCION, FacSufijoBean.C_SUFIJO, FacSufijoBean.C_VARIOS,
							FacSufijoBean.C_CONCEPTO, FacSufijoBean.C_USUMODIFICACION, 
							FacSufijoBean.C_FECHAMODIFICACION};
		return campos;
	}

	/** 
	 * Devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {FacSufijoBean.C_IDINSTITUCION, FacSufijoBean.C_SUFIJO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FacSufijoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacSufijoBean bean = null;
		
		try {
			bean = new FacSufijoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FacSufijoBean.C_IDINSTITUCION));
			bean.setConcepto(UtilidadesHash.getString(hash, FacSufijoBean.C_CONCEPTO));
			bean.setSufijo(UtilidadesHash.getString(hash, FacSufijoBean.C_SUFIJO));
			bean.setVarios(UtilidadesHash.getString(hash, FacSufijoBean.C_VARIOS));
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
			FacSufijoBean b = (FacSufijoBean) bean;
			htData.put(FacSufijoBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(FacSufijoBean.C_CONCEPTO, b.getConcepto());
			htData.put(FacSufijoBean.C_SUFIJO, String.valueOf(b.getSufijo()));
			htData.put(FacSufijoBean.C_VARIOS, String.valueOf(b.getVarios()));
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
	 * Devuelve un vector con el resultado de la busqueda.
	 * @param idInstitucion
	 * @param sufijo
	 * @param concepto
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector consultaBusqueda(String idInstitucion, String sufijo, String concepto) throws ClsExceptions{
		Vector Vsufijos = null;
		try {
			String where = " WHERE "+PysProductosBean.C_IDINSTITUCION+" = "+idInstitucion;
			if(sufijo!=null && !sufijo.trim().equals("")) 
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(sufijo.trim(),FacSufijoBean.C_SUFIJO);
			if(concepto!=null && !concepto.trim().equals(""))
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(concepto.trim(),FacSufijoBean.C_CONCEPTO );
					    				
			Vsufijos = this.selectNLS(where);
		} catch (Exception e){
			throw new ClsExceptions(e,e.getMessage());
		}
		return Vsufijos;
	}

}