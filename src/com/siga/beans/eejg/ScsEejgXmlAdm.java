package com.siga.beans.eejg;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CerSolicitudCertificadosTextoBean;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.beans.ScsTurnoBean;
/**
 * 
 * @author jorgeta
 *
 */

public class ScsEejgXmlAdm extends MasterBeanAdministrador {
  

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsEejgXmlAdm (UsrBean usuario) {
		super( ScsEejgXmlBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {
				ScsEejgXmlBean.C_IDPETICION,
				ScsEejgXmlBean.C_ESTADO,
				ScsEejgXmlBean.C_IDXML,
				ScsEejgXmlBean.C_ENVIORESPUESTA,
				ScsEejgXmlBean.C_XML,
				
				ScsEejgXmlBean.C_USUMODIFICACION,
				ScsEejgXmlBean.C_FECHAMODIFICACION
				};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsEejgXmlBean.C_IDXML};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsEejgXmlBean bean = null;
		try{
			bean = new ScsEejgXmlBean();
			bean.setIdPeticion(UtilidadesHash.getLong(hash,ScsEejgXmlBean.C_IDPETICION));
			bean.setEnvioRespuesta(UtilidadesHash.getString(hash,ScsEejgXmlBean.C_ENVIORESPUESTA));
			bean.setXml(UtilidadesHash.getString(hash,ScsEejgXmlBean.C_XML));
			bean.setEstado(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_ESTADO));
			bean.setIdXml(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_IDXML));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsEejgXmlBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_USUMODIFICACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable<String, Object> beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable<String, Object> hash = null;
		try{
			hash = new Hashtable<String, Object>();
			ScsEejgXmlBean b = (ScsEejgXmlBean) bean;
			
			hash.put(ScsEejgXmlBean.C_IDPETICION, b.getIdPeticion());
			hash.put(ScsEejgXmlBean.C_ENVIORESPUESTA, b.getEnvioRespuesta());
			hash.put(ScsEejgXmlBean.C_XML, b.getXml());
			hash.put(ScsEejgXmlBean.C_ESTADO, b.getEstado());
			hash.put(ScsEejgXmlBean.C_IDXML, b.getIdXml());
			hash.put(ScsEejgXmlBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsEejgXmlBean.C_FECHAMODIFICACION, b.getFechaMod());
			
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	public String[] getOrdenCampos() {
		String[] vector = {ScsEejgXmlBean.C_IDPETICION};
		return vector;
	}
	

	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public Integer getNuevoIdXml() throws ClsExceptions {
		Integer nuevoId = null;		
		try {
			String select  = "SELECT NVL(MAX("+ScsEejgXmlBean.C_IDXML+"), 0) + 1 AS ID FROM "+ScsEejgXmlBean.T_NOMBRETABLA;
			Vector datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
			nuevoId = Integer.valueOf(id);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}
	public String getEejgXml(Integer idXml) throws ClsExceptions, UnsupportedEncodingException{
		RowsContainer rows=new RowsContainer();
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(ScsEejgXmlBean.C_XML);
		query.append(" FROM ");
		query.append(ScsEejgXmlBean.T_NOMBRETABLA);
		query.append(" WHERE ");
		query.append(ScsEejgXmlBean.C_IDXML);
		query.append(" = ");
		query.append(idXml);
		
		
		
		String xml = rows.getClob(ScsEejgXmlBean.T_NOMBRETABLA,ScsEejgXmlBean.C_XML,query.toString());
		return xml;
	}
	
	

	
}