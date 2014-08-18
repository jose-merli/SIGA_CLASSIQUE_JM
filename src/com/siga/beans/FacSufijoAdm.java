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
		String [] campos = {FacSufijoBean.C_IDSUFIJO, FacSufijoBean.C_IDINSTITUCION, FacSufijoBean.C_SUFIJO,
							FacSufijoBean.C_CONCEPTO, FacSufijoBean.C_USUMODIFICACION, 
							FacSufijoBean.C_FECHAMODIFICACION};
		return campos;
	}

	/** 
	 * Devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {FacSufijoBean.C_IDINSTITUCION, FacSufijoBean.C_IDSUFIJO};
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
			bean.setIdSufijo(UtilidadesHash.getInteger(hash, FacSufijoBean.C_IDSUFIJO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FacSufijoBean.C_IDINSTITUCION));
			bean.setConcepto(UtilidadesHash.getString(hash, FacSufijoBean.C_CONCEPTO));
			bean.setSufijo(UtilidadesHash.getString(hash, FacSufijoBean.C_SUFIJO));
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
			htData.put(FacSufijoBean.C_IDSUFIJO,b.getIdSufijo());
			htData.put(FacSufijoBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(FacSufijoBean.C_CONCEPTO, b.getConcepto());
			htData.put(FacSufijoBean.C_SUFIJO, String.valueOf(b.getSufijo()));
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
	public Vector consultaBusqueda(FacSufijoBean datos) throws ClsExceptions{
		Vector Vsufijos = null;
		try {
			String where = " WHERE "+FacSufijoBean.C_IDINSTITUCION+" = "+datos.getIdInstitucion();
			if(datos.getIdSufijo()!=null&&(datos.getIdSufijo()>0)) 
				where += " AND "+FacSufijoBean.C_IDSUFIJO+" = "+datos.getIdSufijo();
			if(datos.getSufijo()!=null&&(!datos.getSufijo().isEmpty())) 
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(datos.getSufijo().trim(),FacSufijoBean.C_SUFIJO);
			if((datos.getConcepto()!=null)&&(!datos.getConcepto().isEmpty()))
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(datos.getConcepto().trim(),FacSufijoBean.C_CONCEPTO );    				
			Vsufijos = this.selectNLS(where);
		} catch (Exception e){
			throw new ClsExceptions(e,e.getMessage());
		}
		return Vsufijos;
	}
	
	
	/**
	 * Devuelve el máximo idSufijo para una institución
	 * @param idInstitucion
	 * @return idSufijo
	 * @throws ClsExceptions
	 */
	public Integer idSufijoMaxInstitucion(Integer idInstitucion)throws ClsExceptions{
		
		String select = null;
		Integer nuevoIdSufijo;
		
		try {
			select  = "SELECT MAX("+FacSufijoBean.C_IDSUFIJO+")+1 AS ID FROM "+FacSufijoBean.T_NOMBRETABLA+
					  " WHERE IDINSTITUCION="+idInstitucion;

			Vector datos = this.selectGenerico(select);
			String id = (String)((Hashtable<String , Object>)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoIdSufijo = 1;
			else
				nuevoIdSufijo = Integer.parseInt(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoIdSufijo;
	}

}