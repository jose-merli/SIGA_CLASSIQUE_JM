/*
 * VERSIONES:
 * yolanda.garcia - 13-12-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


public class FacClienIncluidoEnSerieFacturAdm extends MasterBeanAdministrador {
	
	public FacClienIncluidoEnSerieFacturAdm (UsrBean usu) {
		super (FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION, 		
						    FacClienIncluidoEnSerieFacturBean.C_IDPERSONA,
							FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION,
							FacClienIncluidoEnSerieFacturBean.C_FECHAMODIFICACION,
							FacClienIncluidoEnSerieFacturBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION, FacClienIncluidoEnSerieFacturBean.C_IDPERSONA, FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacClienIncluidoEnSerieFacturBean bean = null;
		
		try {
			bean = new FacClienIncluidoEnSerieFacturBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash, FacClienIncluidoEnSerieFacturBean.C_IDPERSONA));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacClienIncluidoEnSerieFacturBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacClienIncluidoEnSerieFacturBean.C_USUMODIFICACION));
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
			FacClienIncluidoEnSerieFacturBean b = (FacClienIncluidoEnSerieFacturBean) bean;
			UtilidadesHash.set(htData, FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacClienIncluidoEnSerieFacturBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacClienIncluidoEnSerieFacturBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacClienIncluidoEnSerieFacturBean.C_USUMODIFICACION, b.getUsuMod());
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
	
	protected String[] getCamposSelect(){
		String[] campos = {"DISTINCT "+FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+FacClienIncluidoEnSerieFacturBean.C_IDINSTITUCION+ " IDINSTITUCION",
						   FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+FacClienIncluidoEnSerieFacturBean.C_IDPERSONA+ " IDPERSONA",		   
						   FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+FacClienIncluidoEnSerieFacturBean.C_IDSERIEFACTURACION+ " IDSERIEFACTURACION",	
						   CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+ " NOMBRECLIENTE",
						   CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+ " APELLIDOS1",
						   CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+ " APELLIDOS2",
						   FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+FacClienIncluidoEnSerieFacturBean.C_FECHAMODIFICACION+ " FECHAMODIFICACION",
						   FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+FacClienIncluidoEnSerieFacturBean.C_USUMODIFICACION+ " USUMODIFICACION"};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		campos += FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+
				  		" INNER JOIN "+ 
				  		CenPersonaBean.T_NOMBRETABLA+" ON "+
				  			FacClienIncluidoEnSerieFacturBean.T_NOMBRETABLA+"."+ FacClienIncluidoEnSerieFacturBean.C_IDPERSONA+"="+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA; 
		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,
							CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2};
		
		return campos;
	}
	
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect(), this.getCamposSelect());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
}
