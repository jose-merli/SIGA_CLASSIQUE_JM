package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class AdmEnvioInformeAdm extends MasterBeanAdministrador
{
	//
	//CONSTRUCTORES
	//
	public AdmEnvioInformeAdm (UsrBean usuario) 
	{
		super (AdmEnvioInformeBean.T_NOMBRETABLA, usuario);
	}
	
	
	//
	//METODOS DE ADM
	//
	protected String[] getCamposBean()
	{
		String [] campos = 
		{
			
			
				
				AdmEnvioInformeBean.C_IDINSTITUCION,
				AdmEnvioInformeBean.C_IDPLANTILLA,
				AdmEnvioInformeBean.C_IDTIPOENVIOS,
				AdmEnvioInformeBean.C_IDPLANTILLAENVIODEF,
				AdmEnvioInformeBean.C_DEFECTO,AdmEnvioInformeBean.C_FECHAMODIFICACION,
				AdmEnvioInformeBean.C_USUMODIFICACION
				
				
		};
		return campos;
	} //getCamposBean()
	
	protected String[] getClavesBean()
	{
		String [] claves = 
		{
				AdmEnvioInformeBean.C_IDPLANTILLA,
				AdmEnvioInformeBean.C_IDINSTITUCION
		};
		return claves;
	} //getClavesBean()
	
	protected String[] getOrdenCampos()
	{
		String [] orden = 
		{
				AdmEnvioInformeBean.C_IDPLANTILLA,AdmEnvioInformeBean.C_IDINSTITUCION
		};
		return orden;
	} //getOrdenCampos()
	
	protected MasterBean hashTableToBean (Hashtable hash) throws ClsExceptions
	{
		AdmEnvioInformeBean bean = null;
		
		try {
			bean = new AdmEnvioInformeBean();
			
			
			bean.setFechaMod		(UtilidadesHash.getString(hash, AdmEnvioInformeBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, AdmEnvioInformeBean.C_IDINSTITUCION));
			bean.setIdPlantilla		(UtilidadesHash.getString(hash, AdmEnvioInformeBean.C_IDPLANTILLA));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, AdmEnvioInformeBean.C_USUMODIFICACION));
			bean.setIdTipoEnvios(UtilidadesHash.getString(hash, AdmEnvioInformeBean.C_IDTIPOENVIOS));
			bean.setDefecto(UtilidadesHash.getString(hash, AdmEnvioInformeBean.C_DEFECTO));
			bean.setIdPlantillaEnvioDef(	UtilidadesHash.getString(hash, AdmEnvioInformeBean.C_IDPLANTILLAENVIODEF));

		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean()
	
	protected Hashtable beanToHashTable (MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			AdmEnvioInformeBean b = (AdmEnvioInformeBean) bean;
			
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_IDPLANTILLA, 		b.getIdPlantilla());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_IDTIPOENVIOS, 		b.getIdTipoEnvios());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_DEFECTO, 		b.getDefecto());
			UtilidadesHash.set(htData, AdmEnvioInformeBean.C_IDPLANTILLAENVIODEF, 		b.getIdPlantillaEnvioDef());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	} //beanToHashTable()
	
	
	
	
	
	
	
	
}
