/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


public class FacTipoCliIncluidoEnSerieFacAdm extends MasterBeanAdministrador {
	
	public FacTipoCliIncluidoEnSerieFacAdm (UsrBean usu) {
		super (FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION,
							FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO,
						    FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION,
						    FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO, 	
						    FacTipoCliIncluidoEnSerieFacBean.C_FECHAMODIFICACION,
						    FacTipoCliIncluidoEnSerieFacBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION, 
							FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO,
							FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION, 
							FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacTipoCliIncluidoEnSerieFacBean bean = null;
		
		try {
			bean = new FacTipoCliIncluidoEnSerieFacBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION));
			bean.setIdInstitucionGrupo  (UtilidadesHash.getInteger(hash, FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION));
			bean.setIdGrupo				(UtilidadesHash.getInteger(hash, FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacTipoCliIncluidoEnSerieFacBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacTipoCliIncluidoEnSerieFacBean.C_USUMODIFICACION));
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
			FacTipoCliIncluidoEnSerieFacBean b = (FacTipoCliIncluidoEnSerieFacBean) bean;
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO, b.getIdInstitucionGrupo());
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO, b.getIdGrupo());
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacTipoCliIncluidoEnSerieFacBean.C_USUMODIFICACION, b.getUsuMod());
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
	
	protected String[] getCamposSelect(String idLenguaje){
		String[] campos = {" DISTINCT "+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION+ " IDINSTITUCION",
						   FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION+ " IDSERIEFACTURACION",
						   FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO+ " IDGRUPO",
						   FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO+ " IDINSTITUCION_GRUPO",
						   "F_SIGA_GETRECURSO("+CenGruposClienteBean.T_NOMBRETABLA+"."+CenGruposClienteBean.C_NOMBRE+","+idLenguaje+") NOMBREGRUPOCLIENTE",
						   FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_FECHAMODIFICACION+ " FECHAMODIFICACION",
						   FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_USUMODIFICACION+ " USUMODIFICACION"};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		campos += FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+
				  		" INNER JOIN "+ 
				  		CenGruposClienteBean.T_NOMBRETABLA+" ON "+
				  		FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+ FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO+"="+CenGruposClienteBean.T_NOMBRETABLA+"."+CenGruposClienteBean.C_IDGRUPO+
						" AND "+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+ FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION_GRUPO+"="+CenGruposClienteBean.T_NOMBRETABLA+"."+CenGruposClienteBean.C_IDINSTITUCION;
		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { "NOMBREGRUPOCLIENTE" };
		
		return campos;
	}
	
	public Vector selectTabla(String where, String idLenguaje){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect(), this.getCamposSelect(idLenguaje));
			
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
