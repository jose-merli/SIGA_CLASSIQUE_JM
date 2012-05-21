/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
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
					  ", "+PysTiposProductosBean.T_NOMBRETABLA;
		}
		
		if (tipoServicio!=null && !tipoServicio.trim().equals("")) {
			campos += ", "+FacTiposServInclsEnFactBean.T_NOMBRETABLA+
					  ", "+PysTipoServiciosBean.T_NOMBRETABLA;
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
	
	public Vector selectTabla(String tipoProducto, String tipoServicio, String grupoClienteFijo, String grupoClientesDinamico, String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect(tipoProducto, tipoServicio, grupoClienteFijo, grupoClientesDinamico), this.getCamposSelect());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect());
			if (rc.queryNLS(sql)) {
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
			String sql = "Select Nvl(Max("+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDSERIEFACTURACION+"), 0) + 1 IDSERIEFACTURACION";
			sql += " From "+FacSerieFacturacionBean.T_NOMBRETABLA;
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
	
	public AdmContadorBean obtenerContador(Hashtable miHash) throws ClsExceptions {
		AdmContadorBean salida = null;
		Vector v = null;
		Vector v2 = null;
		v = this.selectByPK(miHash);
		if (v!=null && v.size()>0) {
			FacSerieFacturacionBean b = (FacSerieFacturacionBean) v.get(0);
			Hashtable miHash2 = new Hashtable();
			miHash2.put(AdmContadorBean.C_IDINSTITUCION,b.getIdInstitucion());
			miHash2.put(AdmContadorBean.C_IDCONTADOR,b.getIdContador());
			AdmContadorAdm admC = new AdmContadorAdm(this.usrbean);
			v2 = admC.selectByPK(miHash2);
			if (v2!=null && v2.size()>0) {
				salida = (AdmContadorBean) v2.get(0);
			}
		} else {
			throw new ClsExceptions("No se encuentra la serie de facturación.");
		}
			
		return salida;
	}
	
    public FacSerieFacturacionBean obtenerSerieGenerica(String idInstitucion) throws ClsExceptions {
        FacSerieFacturacionBean salida=null;
    	try {
    		Hashtable ht = new Hashtable();
    		ht.put(FacSerieFacturacionBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(FacSerieFacturacionBean.C_TIPOSERIE,"G");
    		Vector v = this.select(ht);
    		if (v!=null && v.size()>0) {
    			salida = (FacSerieFacturacionBean) v.get(0);
    		} else {
    		    throw new ClsExceptions("No esta creada la serie de facturacion genérica para facturaciones rápidas.");
    		}
			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener id SOlicitud desde la compra.");
    	}
		return salida;
    }
    
    public FacSerieFacturacionBean obtenerSerieTemporalDesdeGenerica(String idInstitucion, PysCompraBean compra) throws ClsExceptions {
        FacSerieFacturacionBean salida=null;
    	try {
    	    FacSerieFacturacionBean general = this.obtenerSerieGenerica(idInstitucion);
    	    CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);

    	    String nuevoId = this.getNuevoId(idInstitucion);
    	    
    	    // En lugar de clonarlo lo obtengo de nuevo y lo modifico
    	    salida = this.obtenerSerieGenerica(idInstitucion);
    	    salida.setTipoSerie("T");
    	    salida.setIdSerieFacturacion(new Long(nuevoId));
    	    salida.setDescripcion("Fact. Autom. "+admPer.obtenerNombreApellidos(compra.getIdPersona().toString()));
    	    salida.setNombreAbreviado("AUTOM_"+compra.getIdPersona().toString()+"_"+salida.getIdSerieFacturacion().toString());
    	    if (!this.insert(salida)) {
    	        throw new ClsExceptions("Error al crear la serie de facturacion temporal");
    	    }

                	    
    	    // crear fac_seriefacturacion_bancos
			FacBancoInstitucionAdm admBancos= new FacBancoInstitucionAdm(this.usrbean);
			Vector v = admBancos.obtenerSerieFacturacionBanco(idInstitucion,general.getIdSerieFacturacion().toString());
			for (int i=0;v!=null && i<v.size();i++) {
			    Row h3 = (Row) v.get(i);
                admBancos.insertaBancosSerieFacturacion(general.getIdInstitucion().toString(),nuevoId,h3.getString("BANCOS_CODIGO"));
            }

			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener id SOlicitud desde la compra.");
    	}
		return salida;
    }

    public FacSerieFacturacionBean obtenerSerieTemporalDesdeOtra(FacSerieFacturacionBean beanSerie, PysPeticionCompraSuscripcionBean peticion) throws ClsExceptions {
        FacSerieFacturacionBean salida=new FacSerieFacturacionBean();
    	try {
    	    CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);

    	    String nuevoId = this.getNuevoId(beanSerie.getIdInstitucion().toString());
    	    
    	    // En lugar de clonarlo lo obtengo de nuevo y lo modifico
    	    
    	    // campos comunes
    	    salida.setConfigDeudor(beanSerie.getConfigDeudor());
    	    salida.setConfigIngresos(beanSerie.getConfigIngresos());
    	    salida.setCuentaClientes(beanSerie.getCuentaClientes());
    	    salida.setCuentaIngresos(beanSerie.getCuentaIngresos());
    	    salida.setEnvioFactura(beanSerie.getEnvioFactura());
    	    salida.setGenerarPDF(beanSerie.getGenerarPDF());
    	    salida.setIdContador(beanSerie.getIdContador());
    	    salida.setIdInstitucion(beanSerie.getIdInstitucion());
    	    salida.setIdPlantilla(beanSerie.getIdPlantilla());
    	    salida.setObservaciones(beanSerie.getObservaciones());
    	    
    	    // campos diferentes
    	    salida.setTipoSerie("T");
    	    salida.setIdSerieFacturacion(new Long(nuevoId));
    	    salida.setDescripcion("Fact. Autom. "+admPer.obtenerNombreApellidos(peticion.getIdPersona().toString()));
    	    salida.setNombreAbreviado("AUTOM_"+peticion.getIdPersona().toString()+"_"+salida.getIdSerieFacturacion().toString());
    	    if (!this.insert(salida)) {
    	        throw new ClsExceptions("Error al crear la serie de facturacion temporal");
    	    }

                	    
    	    // crear fac_seriefacturacion_bancos
			FacBancoInstitucionAdm admBancos= new FacBancoInstitucionAdm(this.usrbean);
			Vector v = admBancos.obtenerSerieFacturacionBanco(beanSerie.getIdInstitucion().toString(),beanSerie.getIdSerieFacturacion().toString());
			for (int i=0;v!=null && i<v.size();i++) {
			    Row h3 = (Row) v.get(i);
                admBancos.insertaBancosSerieFacturacion(beanSerie.getIdInstitucion().toString(),nuevoId,h3.getString("BANCOS_CODIGO"));
            }

			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener id SOlicitud desde la compra.");
    	}
		return salida;
    }

	public String getNuevoId(String IdInstitucion){
		RowsContainer rc = null;
		String salida = "";
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl(Max("+FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_IDSERIEFACTURACION+"), 0) + 1 IDSERIEFACTURACION";
			sql += " From "+FacSerieFacturacionBean.T_NOMBRETABLA;
			sql += " where idinstitucion="+IdInstitucion;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					salida = (String)registro.get("IDSERIEFACTURACION");
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
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
	        
	        int contador = 0;
	        String aux = "";
	        String idInstitucion="";
	        
	        int tipoProductoAux=0;
	        for (int i=0;i<compras.size();i++) {
	            PysCompraBean b = (PysCompraBean) compras.get(i);
	            contador++;
	            
	            aux+="'"+b.getIdProducto().toString()+"__"+b.getIdTipoProducto().toString()+"',";
	            idInstitucion= b.getIdInstitucion().toString();
	            if (tipoProductoAux==b.getIdTipoProducto().intValue()){
	            	contador--;
	            	
	            }
	            tipoProductoAux=b.getIdTipoProducto().intValue();
	            
	        }
	        aux = aux.substring(0,aux.length()-1);
	        
	        String where = "where (idinstitucion, idseriefacturacion) in ( " +
	                " select idinstitucion, idseriefacturacion " +
	            	" from fac_tiposproduincluenfactu  " +
	            	" where idproducto||'__'||idtipoproducto in ("+aux+") " +
	            	" and idinstitucion=" +idInstitucion +")";
	        
	        salida = this.select(where);
	        
		}
		catch(Exception e){
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
	public Vector obtenerFormasPago (String idInstitucion, String idSerieFacturacion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
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
	public Vector obtenerIdPago (String idInstitucion, String idSerieFacturacion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
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
}
