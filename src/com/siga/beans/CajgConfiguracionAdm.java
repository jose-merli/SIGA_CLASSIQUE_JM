/*
 * Created on 15/10/2009
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
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author angel.corral
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgConfiguracionAdm extends MasterBeanAdministrador {

	public CajgConfiguracionAdm (UsrBean usu) {
		super (CajgConfiguracionBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgConfiguracionBean.C_IDINSTITUCION
				, CajgConfiguracionBean.C_TIPO_PCAJG
				, CajgConfiguracionBean.C_FTP_IP
				, CajgConfiguracionBean.C_FTP_PUERTO
				, CajgConfiguracionBean.C_FTP_USER
				, CajgConfiguracionBean.C_FTP_PASS
				, CajgConfiguracionBean.C_FTP_DIRECTORIO_IN
				, CajgConfiguracionBean.C_FTP_DIRECTORIO_OUT
				, CajgConfiguracionBean.C_WS_URL
				, CajgConfiguracionBean.C_WS_CLASS
				, CajgConfiguracionBean.C_FECHAMODIFICACION
				, CajgConfiguracionBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgConfiguracionBean.C_IDINSTITUCION };
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgConfiguracionBean bean = null;
		try{
			bean = new CajgConfiguracionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgConfiguracionBean.C_IDINSTITUCION));
			bean.setTipoCAJG(UtilidadesHash.getInteger(hash,CajgConfiguracionBean.C_TIPO_PCAJG));
			bean.setFtpIP(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FTP_IP));
			bean.setFtpPuerto(UtilidadesHash.getInteger(hash,CajgConfiguracionBean.C_FTP_PUERTO));
			bean.setFtpUser(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FTP_USER));
			bean.setFtpPass(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FTP_PASS));
			bean.setFtpDirectorioIN(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FTP_DIRECTORIO_IN));
			bean.setFtpDirectorioOUT(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FTP_DIRECTORIO_OUT));
			bean.setWsURL(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_WS_URL));
			bean.setWsClass(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_WS_CLASS));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgConfiguracionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgConfiguracionBean.C_USUMODIFICACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgConfiguracionBean b = (CajgConfiguracionBean) bean;
			
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_TIPO_PCAJG, b.getTipoCAJG());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_IP, b.getFtpIP());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_PUERTO, b.getFtpPuerto());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_USER, b.getFtpUser());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_PASS, b.getFtpPass());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_DIRECTORIO_IN, b.getFtpDirectorioIN());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FTP_DIRECTORIO_OUT, b.getFtpDirectorioOUT());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_WS_URL, b.getWsURL());
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_WS_CLASS, b.getWsClass());
				
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgConfiguracionBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @return
	 * @throws NumberFormatException
	 * @throws ClsExceptions
	 */
	public static int getTipoCAJG(Integer idInstitucion) throws ClsExceptions {
		int tipoCAJG = -1;
		String sql = "SELECT " + CajgConfiguracionBean.C_TIPO_PCAJG +
				" FROM " + CajgConfiguracionBean.T_NOMBRETABLA + 
				" WHERE " + CajgConfiguracionBean.C_IDINSTITUCION + " = " + idInstitucion;
		
		RowsContainer rc = new RowsContainer();
		
		if (rc.query(sql)) {
			Row fila = (Row) rc.get(0);
			if (fila != null) {
				Hashtable ht = fila.getRow();
				if (ht.get(CajgConfiguracionBean.C_TIPO_PCAJG) != null) {
					String tipo = ht.get(CajgConfiguracionBean.C_TIPO_PCAJG).toString();
					if (tipo != null) {
						tipoCAJG = Integer.parseInt(tipo);
					}
				}
			}
		}
		return tipoCAJG;
	}
}