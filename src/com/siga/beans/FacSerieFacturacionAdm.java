/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacSerieFacturacionAdm extends MasterBeanAdministrador {
	
	public FacSerieFacturacionAdm (UsrBean usu) {
		super (FacSerieFacturacionBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacSerieFacturacionBean.C_IDINSTITUCION, 		
							FacSerieFacturacionBean.C_IDSERIEFACTURACION,
							FacSerieFacturacionBean.C_IDPLANTILLA, 	
							FacSerieFacturacionBean.C_DESCRIPCION,
							FacSerieFacturacionBean.C_NOMBREABREVIADO,
							FacSerieFacturacionBean.C_ENVIOFACTURA,	
							FacSerieFacturacionBean.C_GENERARPDF,	
							FacSerieFacturacionBean.C_IDCONTADOR,
							FacSerieFacturacionBean.C_CONFDEUDOR,
							FacSerieFacturacionBean.C_CONFINGRESOS,
							FacSerieFacturacionBean.C_CTACLIENTES,
							FacSerieFacturacionBean.C_CTAINGRESOS,
							FacSerieFacturacionBean.C_OBSERVACIONES,
							FacSerieFacturacionBean.C_TIPOSERIE,
							FacSerieFacturacionBean.C_FECHAMODIFICACION,
							FacSerieFacturacionBean.C_USUMODIFICACION,
							FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL,
							FacSerieFacturacionBean.C_IDTIPOENVIOS};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacSerieFacturacionBean.C_IDINSTITUCION, FacSerieFacturacionBean.C_IDSERIEFACTURACION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		FacSerieFacturacionBean bean = null;
		
		try {
			bean = new FacSerieFacturacionBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacSerieFacturacionBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacSerieFacturacionBean.C_IDSERIEFACTURACION));
			bean.setIdPlantilla			(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDPLANTILLA));
			bean.setDescripcion			(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_DESCRIPCION));
			bean.setEnvioFactura		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_ENVIOFACTURA));
			bean.setGenerarPDF			(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_GENERARPDF));
			bean.setNombreAbreviado	 	(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_NOMBREABREVIADO));
			bean.setIdContador	 		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_IDCONTADOR));
			bean.setConfigDeudor		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CONFDEUDOR));
			bean.setConfigIngresos		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CONFINGRESOS));
			bean.setCuentaClientes		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CTACLIENTES));
			bean.setCuentaIngresos		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CTAINGRESOS));
			bean.setObservaciones		(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_OBSERVACIONES));
			bean.setTipoSerie			(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_TIPOSERIE));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_USUMODIFICACION));
			bean.setIdTipoPlantillaMail	(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL));
			bean.setIdTipoEnvios		(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDTIPOENVIOS));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions 
	{
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacSerieFacturacionBean b = (FacSerieFacturacionBean) bean;
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_ENVIOFACTURA, b.getEnvioFactura());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_GENERARPDF, b.getGenerarPDF());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_NOMBREABREVIADO, b.getNombreAbreviado());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDCONTADOR, b.getIdContador());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CONFDEUDOR, b.getConfigDeudor());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CONFINGRESOS, b.getConfigIngresos());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CTACLIENTES, b.getCuentaClientes());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CTAINGRESOS, b.getCuentaIngresos());

			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_OBSERVACIONES, b.getObservaciones());

			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_TIPOSERIE, b.getTipoSerie());

			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_USUMODIFICACION, b.getUsuMod());
			
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL, b.getIdTipoPlantillaMail());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
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
		String [] campos = {"DISTINCT "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDINSTITUCION+" IDINSTITUCION", 		
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDSERIEFACTURACION+" IDSERIEFACTURACION",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDPLANTILLA+" IDPLANTILLA",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL+" IDTIPOPLANTILLAMAIL", 	
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_DESCRIPCION+" DESCRIPCION",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO+" NOMBREABREVIADO",	
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_TIPOSERIE+" TIPOSERIE",	
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDCONTADOR+" IDCONTADOR",	
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_FECHAMODIFICACION+" FECHAMODIFICACION",
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_USUMODIFICACION+" USUMODIFICACION"};
		return campos;
	}
	
	protected String getTablasSelect(String tipoProducto, String tipoServicio, String grupoClienteFijo, String grupoClientesDinamico){
		
		String campos = FacSerieFacturacionBean.T_NOMBRETABLA;
		
		if (tipoProducto!=null && !tipoProducto.trim().equals("")) {
			campos += ", "+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+
					  ", "+PysTiposProductosBean.T_NOMBRETABLA+
					  ", "+PysProductosBean.T_NOMBRETABLA;
		}
		
		if (tipoServicio!=null && !tipoServicio.trim().equals("")) {
			campos += ", "+FacTiposServInclsEnFactBean.T_NOMBRETABLA+
					  ", "+PysTipoServiciosBean.T_NOMBRETABLA+
					  ", "+PysServiciosBean.T_NOMBRETABLA;
		}
				 		
		if (grupoClienteFijo!=null && !grupoClienteFijo.trim().equals("")) {
			campos += ", "+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+
					  ", "+CenGruposClienteBean.T_NOMBRETABLA;
		}
		
		if (grupoClientesDinamico!=null && !grupoClientesDinamico.trim().equals("")) {
			campos += ", "+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+
					  ", "+CenGruposCriteriosBean.T_NOMBRETABLA;
		}		 		
				 		
		return campos;
	}
	
	protected String[] getOrdenSelect(){
		String[] campos = { FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO,
							FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_DESCRIPCION};
		return campos;
	}
	
	public Vector<Hashtable<String, Object>> selectTabla(String tipoProducto, String tipoServicio, String grupoClienteFijo, String grupoClientesDinamico, String where){
		Vector<Hashtable<String, Object>> v = new Vector<Hashtable<String, Object>>();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect(tipoProducto, tipoServicio, grupoClienteFijo, grupoClientesDinamico), this.getCamposSelect());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect());
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> registro = (Hashtable<String, Object>)fila.getRow(); 
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

    /**
     * Notas Jorge PT 118: Obtiene un nuevo identidicador de serie de facturacion
     * @param IdInstitucion
     * @return
     */
	public String getNuevoId(String IdInstitucion) throws ClsExceptions {
		String salida = "";
		try {
			RowsContainer rc = new RowsContainer(); 
			String sql = "SELECT NVL(MAX(" + FacSerieFacturacionBean.C_IDSERIEFACTURACION + "), 0) + 1 AS " + FacSerieFacturacionBean.C_IDSERIEFACTURACION + 
						" FROM " + FacSerieFacturacionBean.T_NOMBRETABLA + 
						" WHERE " + FacSerieFacturacionBean.C_IDINSTITUCION + " = " +IdInstitucion;
			
			if (rc.query(sql) && rc.size()>0)	{
				Row fila = (Row) rc.get(0);
				salida = fila.getString(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
			}
			
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al obtener un nuevo identificador de serie de facturación");		
		}			
		
		return salida;
	}
	
	/**
	 * Busca series de facturacion que tengan todos los tipos de productos 
	 * de los productos de cada compra.
	 * @param compras
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector obtenerSeriesAdecuadas(Vector compras) throws ClsExceptions {
	    Vector salida = new Vector();
	    try{
	        if (compras==null || compras.size()==0) {
	            throw new ClsExceptions("Error: No se han recibido compras.");
	        }
	        
	        String aux = "";
	        String idInstitucion="";
	        
	        for (int i=0;i<compras.size();i++) {
	            PysCompraBean b = (PysCompraBean) compras.get(i);
	            
	            aux += "'" + b.getIdProducto().toString() + "_" + b.getIdTipoProducto().toString() + "',";
	            idInstitucion = b.getIdInstitucion().toString();	            
	        }
	        aux = aux.substring(0,aux.length()-1);
	        
	        String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()) + 
	        		" WHERE (" + FacSerieFacturacionBean.C_IDINSTITUCION + ", " + FacSerieFacturacionBean.C_IDSERIEFACTURACION + ") IN (" +
	                	" SELECT " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDINSTITUCION + ", " +
                			FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION +
	                	" FROM " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA +
	                	" WHERE " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDPRODUCTO + 
	                			" || '_' || " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO + " IN (" + aux + ") " +
	            			" AND " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDINSTITUCION + " = " + idInstitucion + 
	            	" ) " + 
	            	" ORDER BY " + FacSerieFacturacionBean.C_DESCRIPCION;
	        
	        salida = this.selectSQL(sql);
	        
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
	    
		return salida;
	}	
	
	
	/** 
	 * Recoge las formas de pago relacionadas con un determinado registro por sus claves <br/>
	 * y su atributo Internet
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @param  internet - descripcion de la forma de pago 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector<Row> obtenerFormasPago (String idInstitucion, String idSerieFacturacion) throws ClsExceptions, SIGAException {
		   Vector<Row> datos=new Vector<Row>();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION  + "," +
	            			FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + "," +
	            			FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_IDFORMAPAGO + "," +
	            			FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_FECHAMODIFICACION + ", " +
	            			FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_USUMODIFICACION +
							" FROM " + FacSerieFacturacionBean.T_NOMBRETABLA + ", " + FacFormaPagoSerieBean.T_NOMBRETABLA + 
							" WHERE " +
							FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION + "=" + idSerieFacturacion +
	            			" AND " +							
	            			FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION + "=" + FacFormaPagoSerieBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDINSTITUCION + "(+)" +
							" AND " +
							FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION + "=" + FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_IDSERIEFACTURACION  + "(+)";
					
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener las formas de pago relacionadas.");
	       		}	
		   }
	       return datos;                        
	    }	
	
	/** 
	 * Recoge las formas de pago relacionadas con un determinado registro por sus claves <br/>
	 * y su atributo Internet
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @param  internet - descripcion de la forma de pago 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector<Row> obtenerIdPago (String idInstitucion, String idSerieFacturacion) throws ClsExceptions, SIGAException {
	   Vector<Row> datos=new Vector<Row>();
       try {
            RowsContainer rc = new RowsContainer(); 
            String sql ="SELECT " +
	    			FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION  + "," +
	    			FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + "," +
	    			FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_IDFORMAPAGO + 				
						" FROM " + FacSerieFacturacionBean.T_NOMBRETABLA + ", " + FacFormaPagoSerieBean.T_NOMBRETABLA + 
						" WHERE " +
						FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION + "=" + idInstitucion +
						" AND " +
						FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION + "=" + idSerieFacturacion +
            			" AND " +							
            			FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION + "=" + FacFormaPagoSerieBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDINSTITUCION + "(+)" +
						" AND " +
						FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION + "=" + FacFormaPagoSerieBean.T_NOMBRETABLA + "." + FacFormaPagoSerieBean.C_IDSERIEFACTURACION  + "(+)";
				
						
            if (rc.find(sql)) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  datos.add(fila);
               }
            } 
       }
	   catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error al obtener las formas de pago relacionadas.");
       		}	
	   }
       return datos;                        
    }
	
	/**
	 * Obtiene la poblacion de la serie de facturacion
	 * @param idInstitucion
	 * @param idSerieFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerPoblacionSerieFacturacion(String idInstitucion, String idSerieFacturacion) throws ClsExceptions {
		Vector<Hashtable<String,Object>> resultado = new Vector<Hashtable<String,Object>>();
		try{			
			String sql = "SELECT NVL(F_SIGA_CALCULONCOLEGIADO(PoblSF.IDINSTITUCION, PoblSF.IDPERSONA), ' ') AS NCOLEGIADO, " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " AS NOMBRE, " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " AS APELLIDOS1, " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " AS APELLIDOS2 " + 
						" FROM " + CenPersonaBean.T_NOMBRETABLA + ", " +
							" TABLE(PKG_SIGA_FACTURACION.OBTENCIONPOBLACIONCLIENTES(" + idInstitucion + ", " + idSerieFacturacion + ")) PoblSF " +
						" WHERE " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = PoblSF.IDPERSONA "+
						" ORDER BY NCOLEGIADO, APELLIDOS1, APELLIDOS2, NOMBRE";
			
			RowsContainer rc = new RowsContainer();
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					resultado.add(fila.getRow()); 
				}
			}
		
		} catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al obtener la población de la serie de facturación.");
		}
		
		return resultado;
	}	
	
	/**
	 * Busca las series de facturacion del producto
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<FacSerieFacturacionBean> obtenerSeriesFacturacionProducto(String idInstitucion, String idTipoProducto, String idProducto) throws ClsExceptions {
	    Vector<FacSerieFacturacionBean> salida = new Vector<FacSerieFacturacionBean>();
	    try{	        
	        String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()) + 
	        		" WHERE (" + FacSerieFacturacionBean.C_IDINSTITUCION + ", " + FacSerieFacturacionBean.C_IDSERIEFACTURACION + ") IN (" +
	                	" SELECT " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDINSTITUCION + ", " +
                			FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION +
	                	" FROM " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA +
	                	" WHERE " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDINSTITUCION + " = " + idInstitucion +
	                		" AND " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO + " = " + idTipoProducto +
	                		" AND " + FacTiposProduIncluEnFactuBean.T_NOMBRETABLA + "." + FacTiposProduIncluEnFactuBean.C_IDPRODUCTO + " = " + idProducto +	                		
	            	" ) " + 
	            	" ORDER BY " + FacSerieFacturacionBean.C_DESCRIPCION;
	        
	        salida = this.selectSQL(sql);
	        
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion del producto.");
		}
	    
		return salida;
	}	
		
}