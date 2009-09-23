/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


public class FacTiposServInclsEnFactAdm extends MasterBeanAdministrador {
	
	public FacTiposServInclsEnFactAdm (UsrBean usu) {
		super (FacTiposServInclsEnFactBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacTiposServInclsEnFactBean.C_IDINSTITUCION, 		
						    FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION,
							FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS,
							FacTiposServInclsEnFactBean.C_IDSERVICIO,
							FacTiposServInclsEnFactBean.C_FECHAMODIFICACION,
							FacTiposServInclsEnFactBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacTiposServInclsEnFactBean.C_IDINSTITUCION, FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION, FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS, FacTiposServInclsEnFactBean.C_IDSERVICIO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacTiposServInclsEnFactBean bean = null;
		
		try {
			bean = new FacTiposServInclsEnFactBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacTiposServInclsEnFactBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION));
			bean.setIdTipoServicios		(UtilidadesHash.getInteger(hash, FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio			(UtilidadesHash.getInteger(hash, FacTiposServInclsEnFactBean.C_IDSERVICIO));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacTiposServInclsEnFactBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacTiposServInclsEnFactBean.C_USUMODIFICACION));
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
			FacTiposServInclsEnFactBean b = (FacTiposServInclsEnFactBean) bean;
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_IDSERVICIO, b.getIdServicio());
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacTiposServInclsEnFactBean.C_USUMODIFICACION, b.getUsuMod());
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
		String[] campos = {"DISTINCT "+FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDINSTITUCION+ " IDINSTITUCION",
						   FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION+ " IDSERIEFACTURACION",
						   FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS+ " IDTIPOSERVICIOS",
						   FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDSERVICIO+ " IDSERVICIO",
						   PysServiciosBean.T_NOMBRETABLA+"."+PysServiciosBean.C_DESCRIPCION+ " DESCRIPCIONSERVICIO",
						   FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_FECHAMODIFICACION+ " FECHAMODIFICACION",
						   FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_USUMODIFICACION+ " USUMODIFICACION"};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		campos += FacTiposServInclsEnFactBean.T_NOMBRETABLA+
				  		" INNER JOIN "+ 
				  		PysServiciosBean.T_NOMBRETABLA+" ON "+
			  			FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+ FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS+"="+PysServiciosBean.T_NOMBRETABLA+"."+PysServiciosBean.C_IDTIPOSERVICIOS + 
						" AND "+
						FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+ FacTiposServInclsEnFactBean.C_IDSERVICIO+"="+PysServiciosBean.T_NOMBRETABLA+"."+PysServiciosBean.C_IDSERVICIO + 
						" AND "+
						FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+ FacTiposServInclsEnFactBean.C_IDINSTITUCION+"="+PysServiciosBean.T_NOMBRETABLA+"."+PysServiciosBean.C_IDINSTITUCION; 
		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { PysServiciosBean.T_NOMBRETABLA+"."+PysServiciosBean.C_DESCRIPCION };
		
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
