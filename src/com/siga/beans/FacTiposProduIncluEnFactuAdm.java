/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


public class FacTiposProduIncluEnFactuAdm extends MasterBeanAdministrador {
	
	public FacTiposProduIncluEnFactuAdm (UsrBean usu) {
		super (FacTiposProduIncluEnFactuBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacTiposProduIncluEnFactuBean.C_IDINSTITUCION, 		
				    FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION,
				    FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO, 	
				    FacTiposProduIncluEnFactuBean.C_IDPRODUCTO, 	
				    FacTiposProduIncluEnFactuBean.C_FECHAMODIFICACION,
				    FacTiposProduIncluEnFactuBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacTiposProduIncluEnFactuBean.C_IDINSTITUCION, FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION, FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO, FacTiposProduIncluEnFactuBean.C_IDPRODUCTO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacTiposProduIncluEnFactuBean bean = null;
		
		try {
			bean = new FacTiposProduIncluEnFactuBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacTiposProduIncluEnFactuBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION));
			bean.setIdTipoProducto		(UtilidadesHash.getInteger(hash, FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto		(UtilidadesHash.getInteger(hash, FacTiposProduIncluEnFactuBean.C_IDPRODUCTO));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacTiposProduIncluEnFactuBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacTiposProduIncluEnFactuBean.C_USUMODIFICACION));
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
			FacTiposProduIncluEnFactuBean b = (FacTiposProduIncluEnFactuBean) bean;
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_IDPRODUCTO, b.getIdProducto());
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacTiposProduIncluEnFactuBean.C_USUMODIFICACION, b.getUsuMod());
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
		String[] campos = {"DISTINCT "+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDINSTITUCION+ " IDINSTITUCION",
						   FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION+ " IDSERIEFACTURACION",
						   FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO+ " IDTIPOPRODUCTO",
						   FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDPRODUCTO+ " IDPRODUCTO",
						   PysProductosBean.T_NOMBRETABLA+"."+PysProductosBean.C_DESCRIPCION+ " DESCRIPCIONPRODUCTO",
						   FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_FECHAMODIFICACION+ " FECHAMODIFICACION",
						   FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_USUMODIFICACION+ " USUMODIFICACION"};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		campos += FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+
				  		" INNER JOIN "+ 
						PysProductosBean.T_NOMBRETABLA+" ON "+
						FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+ FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO+"="+PysProductosBean.T_NOMBRETABLA+"."+PysProductosBean.C_IDTIPOPRODUCTO + 
						" AND "+
						FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+ FacTiposProduIncluEnFactuBean.C_IDPRODUCTO+"="+PysProductosBean.T_NOMBRETABLA+"."+PysProductosBean.C_IDPRODUCTO + 
						" AND "+
						FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+ FacTiposProduIncluEnFactuBean.C_IDINSTITUCION+"="+PysProductosBean.T_NOMBRETABLA+"."+PysProductosBean.C_IDINSTITUCION; 
		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { PysProductosBean.T_NOMBRETABLA+"."+PysProductosBean.C_DESCRIPCION };
		
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
