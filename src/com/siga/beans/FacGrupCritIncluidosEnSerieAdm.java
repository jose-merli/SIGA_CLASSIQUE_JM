/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

public class FacGrupCritIncluidosEnSerieAdm extends MasterBeanAdministrador {
	
	public FacGrupCritIncluidosEnSerieAdm (UsrBean usu) {
		super (FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION, 		
				    		FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION,
							FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS, 	
							FacGrupCritIncluidosEnSerieBean.C_FECHAMODIFICACION,
							FacGrupCritIncluidosEnSerieBean.C_USUMODIFICACION,
							FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION, FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION, FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS,FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacGrupCritIncluidosEnSerieBean bean = null;
		
		try {
			bean = new FacGrupCritIncluidosEnSerieBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION));
			bean.setIdGruposCriterios	(UtilidadesHash.getInteger(hash, FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacGrupCritIncluidosEnSerieBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacGrupCritIncluidosEnSerieBean.C_USUMODIFICACION));
			bean.setIdInstitucionGrup   (UtilidadesHash.getInteger(hash, FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP));
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
			FacGrupCritIncluidosEnSerieBean b = (FacGrupCritIncluidosEnSerieBean) bean;
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS, b.getIdGruposCriterios());
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP, b.getIdInstitucionGrup());			
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
		String[] campos = {"DISTINCT "+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION+ " IDINSTITUCION",
						   FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION+ " IDSERIEFACTURACION",
						   FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS+ " IDGRUPOSCRITERIOS",		   
						   CenGruposCriteriosBean.T_NOMBRETABLA+"."+CenGruposCriteriosBean.C_NOMBRE+ " NOMBREGRUPOCRITERIO",
						   FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_FECHAMODIFICACION+ " FECHAMODIFICACION",
						   FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_USUMODIFICACION+ " USUMODIFICACION",
						   FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP+ " IDINSTITUCION_GRUP"};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		campos += FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+
				  		" INNER JOIN "+ 
				  		CenGruposCriteriosBean.T_NOMBRETABLA+" ON "+
							FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+ FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION_GRUP+"="+CenGruposCriteriosBean.T_NOMBRETABLA+"."+CenGruposCriteriosBean.C_IDINSTITUCION+
							" and "+
							FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+ FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS+"="+CenGruposCriteriosBean.T_NOMBRETABLA+"."+CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS; 
		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { CenGruposCriteriosBean.T_NOMBRETABLA+"."+CenGruposCriteriosBean.C_NOMBRE };
		
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
