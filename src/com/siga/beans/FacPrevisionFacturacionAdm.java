/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


public class FacPrevisionFacturacionAdm extends MasterBeanAdministrador {
	
	public FacPrevisionFacturacionAdm (UsrBean usu) {
		super (FacPrevisionFacturacionBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacPrevisionFacturacionBean.C_IDINSTITUCION, 		
						    FacPrevisionFacturacionBean.C_IDSERIEFACTURACION,
							FacPrevisionFacturacionBean.C_IDPREVISION, 	
							FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS,
							FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS,	
							FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS,
							FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS,	
							FacPrevisionFacturacionBean.C_FECHAMODIFICACION,
							FacPrevisionFacturacionBean.C_USUMODIFICACION,
							FacPrevisionFacturacionBean.C_NOMBREFICHERO,
							FacPrevisionFacturacionBean.C_DESCRIPCION,
							FacPrevisionFacturacionBean.C_IDESTADOPREVISION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacPrevisionFacturacionBean.C_IDINSTITUCION, FacPrevisionFacturacionBean.C_IDSERIEFACTURACION, FacPrevisionFacturacionBean.C_IDPREVISION};
		return claves;
	}
	
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacPrevisionFacturacionBean bean = null;
		
		try {
			bean = new FacPrevisionFacturacionBean();
			bean.setIdInstitucion			(UtilidadesHash.getInteger(hash, FacPrevisionFacturacionBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion		(UtilidadesHash.getLong(hash, FacPrevisionFacturacionBean.C_IDSERIEFACTURACION));
			bean.setIdPrevision				(UtilidadesHash.getLong(hash, FacPrevisionFacturacionBean.C_IDPREVISION));
			bean.setFechaInicioProductos	(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS));
			bean.setFechaFinProductos	 	(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS));
			bean.setFechaInicioServicios	(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS));
			bean.setFechaFinServicios	 	(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS));
			bean.setFechaMod				(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod					(UtilidadesHash.getInteger(hash, FacPrevisionFacturacionBean.C_USUMODIFICACION));
			bean.setNombreFichero	 		(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_NOMBREFICHERO));
			bean.setDescripcion				(UtilidadesHash.getString(hash, FacPrevisionFacturacionBean.C_DESCRIPCION));
			bean.setIdEstadoPrevision		(UtilidadesHash.getInteger(hash, FacPrevisionFacturacionBean.C_IDESTADOPREVISION));
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
			FacPrevisionFacturacionBean b = (FacPrevisionFacturacionBean) bean;
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_IDPREVISION, b.getIdPrevision());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS, b.getFechaInicioProductos());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS, b.getFechaFinProductos());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS, b.getFechaInicioServicios());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS, b.getFechaFinServicios());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, FacPrevisionFacturacionBean.C_IDESTADOPREVISION, b.getIdEstadoPrevision());
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
	
	protected String[] getCamposSelect() {
		String [] campos = {FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDINSTITUCION+" IDINSTITUCION", 		
							FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDSERIEFACTURACION+" IDSERIEFACTURACION",
							FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDPREVISION+" IDPREVISION",
							FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDESTADOPREVISION+" IDESTADOPREVISION",
							" (SELECT COUNT(*) FROM FAC_FACTURACIONPROGRAMADA PP WHERE PP.IDINSTITUCION=FAC_PREVISIONFACTURACION.IDINSTITUCION" +
							" AND PP.IDSERIEFACTURACION = FAC_PREVISIONFACTURACION.IDSERIEFACTURACION" +
							" AND PP.IDPREVISION=FAC_PREVISIONFACTURACION.IDPREVISION) EXISTEPROGRAMACION ",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_DESCRIPCION+" DESCRIPCIONFACTURACION",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO+" NOMBREABREVIADO",
							"TO_CHAR("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS+", 'DD/MM/YYYY') FECHAINICIOPRODUCTOS",	
							"TO_CHAR("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS+", 'DD/MM/YYYY') FECHAFINPRODUCTOS",
							"TO_CHAR("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS+", 'DD/MM/YYYY') FECHAINICIOSERVICIOS",
							"TO_CHAR("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS+", 'DD/MM/YYYY') FECHAFINSERVICIOS",
							FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_NOMBREFICHERO+" NOMBREFICHERO",
							FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_DESCRIPCION+ " DESCRIPCION ", 
							"TO_CHAR("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAMODIFICACION + ", 'DD/MM/YYYY') FECHA_EJECUCION ",
						};
		return campos;
	}
	
	protected String getTablasSelect(){
		
		String campos = "";
		
		campos += FacPrevisionFacturacionBean.T_NOMBRETABLA+
				 			" INNER JOIN "+ 
							FacSerieFacturacionBean.T_NOMBRETABLA+" ON "+
								FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+ FacPrevisionFacturacionBean.C_IDINSTITUCION+"="+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDINSTITUCION
								+" and "+
								FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+ FacPrevisionFacturacionBean.C_IDSERIEFACTURACION+"="+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDSERIEFACTURACION;
					 		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		
		String[] campos = { FacPrevisionFacturacionBean.T_NOMBRETABLA + "." + FacPrevisionFacturacionBean.C_FECHAMODIFICACION + " DESC ", 
				"UPPER("+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO+") ASC" };
		
//		String[] campos = { "UPPER("+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO+") ASC",
//				FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDPREVISION + " ASC"};
/* RGG 06/02/2007 cambio de orden INC_2750		String[] campos = { FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS+" DESC",
				FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS+" DESC"};
*/				
		
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
	
	public Vector selectTabla_2(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl(Max("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_IDPREVISION+"), 0) + 1 IDPREVISION";
			sql += " From "+FacPrevisionFacturacionBean.T_NOMBRETABLA;
			sql += where;
			
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
	
	public Vector selectTabla_3(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			// Para solucionar la incidencia-4274 a la fecha se le concatena las 22:00 para cerciorarse que el útimo día se recoge, actualmente se ponian las 00:00 y por tanto el último día no se está teniendo en cuenta.
			//mhg - En vez de comparar las fechas de fin periodo productos/servicios sea el día actual.
			//String sql = "Select to_date((to_char(Greatest("+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS+", "+FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS+"),'dd/mm/yyyy')||' '||'22:00:00'),'dd/mm/yyyy hh24:mi:ss') FECHAPREVISTAGENERACION " +
			String sql = "Select to_date((to_char(sysdate, 'dd/mm/yyyy')||' '||'22:00:00'),'dd/mm/yyyy hh24:mi:ss') FECHAPREVISTAGENERACION " +
								 ", " + FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+FacPrevisionFacturacionBean.C_DESCRIPCION;
			sql += " From "+FacPrevisionFacturacionBean.T_NOMBRETABLA;
			sql += where;
			
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
