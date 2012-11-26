package com.siga.beans;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.SIGAReferences.RESOURCE_FILES;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author  * @author jose.barrientos
 */

public class GenPropertiesAdm extends MasterBeanAdministrador {

	public GenPropertiesAdm(UsrBean usuario) {
		super(GenPropertiesBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {GenPropertiesBean.C_FICHERO,
							GenPropertiesBean.C_PARAMETRO,
							GenPropertiesBean.C_VALOR};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {GenPropertiesBean.C_PARAMETRO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenPropertiesBean bean = null;
		
		try {
			bean = new GenPropertiesBean();
			bean.setFichero(UtilidadesHash.getString(hash, GenPropertiesBean.C_FICHERO));
			bean.setParametro(UtilidadesHash.getString(hash, GenPropertiesBean.C_PARAMETRO));
			bean.setValor(UtilidadesHash.getString(hash, GenPropertiesBean.C_VALOR));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			GenPropertiesBean b = (GenPropertiesBean) bean;
			UtilidadesHash.set(htData, GenPropertiesBean.C_FICHERO, b.getFichero());
			UtilidadesHash.set(htData, GenPropertiesBean.C_PARAMETRO, b.getParametro());
			UtilidadesHash.set(htData, GenPropertiesBean.C_VALOR, b.getValor());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public String getValor(String fichero, String parametro) throws ClsExceptions	{
		
		String valor="";
		
		try {
			Hashtable hashParams = new Hashtable();
			UtilidadesHash.set(hashParams, GenPropertiesBean.C_FICHERO, fichero);
			UtilidadesHash.set(hashParams, GenPropertiesBean.C_PARAMETRO, parametro);
			Vector auxV = this.select(hashParams);
			
			if (auxV != null && auxV.size() > 0) {
				for (int i = 0; i < auxV.size(); i++) {
					GenPropertiesBean registro = (GenPropertiesBean) auxV.get(i);
					valor = registro.getValor();
				}
			}
			return valor;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener los valores del parámetro " + parametro);
		}
	}
	
	public Properties getProperties(RESOURCE_FILES resource) throws ClsExceptions	{
		
		Properties prop = new Properties();
		
		try {
			
			Vector auxV = this.select("WHERE FICHERO = '"+resource.name() +"'");
			
			if (auxV != null && auxV.size() > 0) {
				for (int i = 0; i < auxV.size(); i++) {
					GenPropertiesBean registro = (GenPropertiesBean) auxV.get(i);
					prop.setProperty(registro.getParametro(),registro.getValor());
				}
			}
			return prop;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener los valores del parámetro " + resource);
		}
	}
	
}
