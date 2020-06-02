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


public class FacSerieFacturacionAdm extends MasterBeanAdministrador {
	
	public FacSerieFacturacionAdm (UsrBean usu) {
		super (FacSerieFacturacionBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacSerieFacturacionBean.C_IDINSTITUCION, 		
							FacSerieFacturacionBean.C_IDSERIEFACTURACION,
							FacSerieFacturacionBean.C_IDPLANTILLA,	
							FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF,
							FacSerieFacturacionBean.C_DESCRIPCION,
							FacSerieFacturacionBean.C_NOMBREABREVIADO,
							FacSerieFacturacionBean.C_ENVIOFACTURA,	
							FacSerieFacturacionBean.C_GENERARPDF,
							FacSerieFacturacionBean.C_TRASPASOFACTURAS,
							FacSerieFacturacionBean.C_TRASPASOPLANTILLA,
							FacSerieFacturacionBean.C_TRASPASOCODAUDITORIADEF,
							FacSerieFacturacionBean.C_IDCONTADOR,
							FacSerieFacturacionBean.C_IDCONTADOR_ABONOS,
							FacSerieFacturacionBean.C_CONFDEUDOR,
							FacSerieFacturacionBean.C_CONFINGRESOS,
							FacSerieFacturacionBean.C_CTACLIENTES,
							FacSerieFacturacionBean.C_CTAINGRESOS,
							FacSerieFacturacionBean.C_OBSERVACIONES,
							FacSerieFacturacionBean.C_TIPOSERIE,							
							FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL,
							FacSerieFacturacionBean.C_IDTIPOENVIOS,
							FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA,
							FacSerieFacturacionBean.C_FECHABAJA, 
							FacSerieFacturacionBean.C_FECHAMODIFICACION,
							FacSerieFacturacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacSerieFacturacionBean.C_IDINSTITUCION, FacSerieFacturacionBean.C_IDSERIEFACTURACION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FacSerieFacturacionBean bean = null;
		try {
			bean = new FacSerieFacturacionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FacSerieFacturacionBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion(UtilidadesHash.getLong(hash, FacSerieFacturacionBean.C_IDSERIEFACTURACION));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDPLANTILLA));
			bean.setIdNombreDescargaPDF(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF));
			bean.setDescripcion(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_DESCRIPCION));
			bean.setEnvioFactura(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_ENVIOFACTURA));
			bean.setGenerarPDF(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_GENERARPDF));
			bean.setTraspasoFacturas(UtilidadesHash.getString(hash,FacSerieFacturacionBean.C_TRASPASOFACTURAS));
			bean.setTraspasoPlantilla(UtilidadesHash.getString(hash,FacSerieFacturacionBean.C_TRASPASOPLANTILLA));
			bean.setTraspasoCodAuditoriaDef(UtilidadesHash.getString(hash,FacSerieFacturacionBean.C_TRASPASOCODAUDITORIADEF));
			bean.setNombreAbreviado(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_NOMBREABREVIADO));
			bean.setIdContador(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_IDCONTADOR));
			bean.setIdContadorAbonos(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_IDCONTADOR_ABONOS));
			bean.setConfigDeudor(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CONFDEUDOR));
			bean.setConfigIngresos(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CONFINGRESOS));
			bean.setCuentaClientes(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CTACLIENTES));
			bean.setCuentaIngresos(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_CTAINGRESOS));
			bean.setObservaciones(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_OBSERVACIONES));
			bean.setTipoSerie(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_TIPOSERIE));			
			bean.setIdTipoPlantillaMail(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_IDTIPOENVIOS));
			bean.setIdSerieFacturacionPrevia(UtilidadesHash.getLong(hash, FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA));
			bean.setFechaBaja(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_FECHABAJA));
			bean.setFechaMod(UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacSerieFacturacionBean.C_USUMODIFICACION));
		} catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable<String,Object> htData = null;
		try {
			htData = new Hashtable<String,Object>();
			FacSerieFacturacionBean b = (FacSerieFacturacionBean) bean;
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF, b.getIdNombreDescargaPDF());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_ENVIOFACTURA, b.getEnvioFactura());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_GENERARPDF, b.getGenerarPDF());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_TRASPASOFACTURAS, b.getTraspasoFacturas());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_TRASPASOPLANTILLA, b.getTraspasoPlantilla());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_TRASPASOCODAUDITORIADEF, b.getTraspasoCodAuditoriaDef());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_NOMBREABREVIADO, b.getNombreAbreviado());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDCONTADOR, b.getIdContador());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDCONTADOR_ABONOS, b.getIdContadorAbonos());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CONFDEUDOR, b.getConfigDeudor());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CONFINGRESOS, b.getConfigIngresos());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CTACLIENTES, b.getCuentaClientes());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_CTAINGRESOS, b.getCuentaIngresos());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_TIPOSERIE, b.getTipoSerie());					
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL, b.getIdTipoPlantillaMail());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA, b.getIdSerieFacturacionPrevia());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacSerieFacturacionBean.C_USUMODIFICACION, b.getUsuMod());
		} catch (Exception e) {
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

	private String[] getCamposSelect() {
		String [] campos = {"DISTINCT " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " AS IDINSTITUCION", 		
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " AS IDSERIEFACTURACION",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA + " AS IDSERIEFACTURACIONPREVIA",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDPLANTILLA + " AS IDPLANTILLA",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL + " AS IDTIPOPLANTILLAMAIL", 	
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_DESCRIPCION + " AS DESCRIPCION",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + " AS NOMBREABREVIADO",	
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_TIPOSERIE + " AS TIPOSERIE",	
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDCONTADOR + " AS IDCONTADOR",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDCONTADOR_ABONOS + " AS IDCONTADOR_ABONOS",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_FECHABAJA + " AS FECHABAJA",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_FECHAMODIFICACION + " AS FECHAMODIFICACION",
										  FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_USUMODIFICACION + " AS USUMODIFICACION"};
		return campos;
	}
	
	private String getTablasSelect(String tipoProducto, String tipoServicio, String grupoClienteFijo, String grupoClientesDinamico) {
		StringBuilder campos = new StringBuilder();  
		campos.append(FacSerieFacturacionBean.T_NOMBRETABLA);
		
		if (tipoProducto!=null && !tipoProducto.trim().equals("")) {
			campos.append(", ");
			campos.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(PysTiposProductosBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(PysProductosBean.T_NOMBRETABLA);
		}
		
		if (tipoServicio!=null && !tipoServicio.trim().equals("")) {
			campos.append(", ");
			campos.append(FacTiposServInclsEnFactBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(PysTipoServiciosBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(PysServiciosBean.T_NOMBRETABLA);
		}
				 		
		if (grupoClienteFijo!=null && !grupoClienteFijo.trim().equals("")) {
			campos.append(", ");
			campos.append(FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(CenGruposClienteBean.T_NOMBRETABLA);
		}
		
		if (grupoClientesDinamico!=null && !grupoClientesDinamico.trim().equals("")) {
			campos.append(", ");
			campos.append(FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA);
			campos.append(", ");
			campos.append(CenGruposCriteriosBean.T_NOMBRETABLA);
		}		 		
				 		
		return campos.toString();
	}
	
	private String[] getOrdenSelect(){
		String[] campos = { FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO,
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_DESCRIPCION};
		return campos;
	}
	
	public Vector<Hashtable<String, Object>> selectTabla(String tipoProducto, String tipoServicio, String grupoClienteFijo, String grupoClientesDinamico, String where) {
		Vector<Hashtable<String, Object>> v = new Vector<Hashtable<String, Object>>();
		RowsContainer rc = null;
		try {
			rc = new RowsContainer(); 
			StringBuilder sql = new StringBuilder();
			sql.append(UtilidadesBDAdm.sqlSelect(this.getTablasSelect(tipoProducto, tipoServicio, grupoClienteFijo, grupoClientesDinamico), this.getCamposSelect()));			
			sql.append(where);
			sql.append(UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect()));
			if (rc.queryNLS(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> registro = (Hashtable<String, Object>)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
			
		} catch (ClsExceptions e){
			e.printStackTrace();
		}
		
		return v;
	}

    /**
     * Notas Jorge PT 118: Obtiene un nuevo identidicador de serie de facturacion
     * @param IdInstitucion
     * @return
     */
	public String getNuevoId(String idInstitucion) throws ClsExceptions {
		String salida = "";
		try {
			RowsContainer rc = new RowsContainer(); 
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT NVL(MAX(");
			sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
			sql.append("), 0) + 1 AS ");
			sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION); 
			sql.append(" FROM ");
			sql.append(FacSerieFacturacionBean.T_NOMBRETABLA); 
			sql.append(" WHERE ");
			sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(idInstitucion);
			
			if (rc.query(sql.toString()) && rc.size()>0)	{
				Row fila = (Row) rc.get(0);
				salida = fila.getString(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
			}
			
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al obtener un nuevo identificador de serie de facturación");		
		}	
		
		return salida;
	}
	
	/** 
	 * Recoge las formas de pago relacionadas con un determinado registro por sus claves y su atributo Internet
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @param  internet - descripcion de la forma de pago 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector<Hashtable<String, Object>> obtenerFormasPago (String idInstitucion, String idSerieFacturacion) throws ClsExceptions {
		Vector<Hashtable<String, Object>> datos = new Vector<Hashtable<String, Object>>();
		try {
    	   StringBuilder sql = new StringBuilder();
    	   sql.append("SELECT ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
    	   sql.append(", ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
    	   sql.append(", ");
    	   sql.append(FacFormaPagoSerieBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacFormaPagoSerieBean.C_IDFORMAPAGO); 				
    	   sql.append(" FROM ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(", ");
    	   sql.append(FacFormaPagoSerieBean.T_NOMBRETABLA); 
    	   sql.append(" WHERE ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
    	   sql.append(" = ");
    	   sql.append(idInstitucion);
    	   sql.append(" AND ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
    	   sql.append(" = ");
    	   sql.append(idSerieFacturacion);
    	   sql.append(" AND ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
    	   sql.append(" = ");
    	   sql.append(FacFormaPagoSerieBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(PysFormaPagoProductoBean.C_IDINSTITUCION);
    	   sql.append(" AND ");
    	   sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
    	   sql.append(" = ");
    	   sql.append(FacFormaPagoSerieBean.T_NOMBRETABLA);
    	   sql.append(".");
    	   sql.append(FacFormaPagoSerieBean.C_IDSERIEFACTURACION);
            
			datos = this.getHashSQL(sql.toString());
            
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al obtener las formas de pago relacionadas.");	
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
		try {		
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT NVL(F_SIGA_CALCULONCOLEGIADO(PoblSF.IDINSTITUCION, PoblSF.IDPERSONA), ' ') AS NCOLEGIADO, ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_NOMBRE);
			sql.append(" AS NOMBRE, ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS1);
			sql.append(" AS APELLIDOS1, ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS2);
			sql.append(" AS APELLIDOS2 FROM ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(", TABLE(PKG_SIGA_FACTURACION.OBTENCIONPOBLACIONCLIENTES(");
			sql.append(idInstitucion);
			sql.append(", ");
			sql.append(idSerieFacturacion);
			sql.append(")) PoblSF WHERE PoblSF.IDPERSONA=");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_IDPERSONA);
			sql.append(" ORDER BY NCOLEGIADO, APELLIDOS1, APELLIDOS2, NOMBRE");
			
			RowsContainer rc = new RowsContainer();
			if (rc.query(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					resultado.add((Hashtable<String,Object>) fila.getRow()); 
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
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<FacSerieFacturacionBean> obtenerSeriesFacturacionProducto(String idInstitucion, String idTipoProducto, String idProducto, String idPersona) throws ClsExceptions {
	    Vector<FacSerieFacturacionBean> salida = new Vector<FacSerieFacturacionBean>();
	    try{	        
	    	StringBuilder sql = new StringBuilder();
	    	sql = new StringBuilder();
	    	sql.append(UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()));
	    	sql.append(" WHERE ");
	    	sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
	    	sql.append("=");
	    	sql.append(idInstitucion);
	    	sql.append(" AND ");
	    	sql.append(FacSerieFacturacionBean.C_TIPOSERIE);
	    	sql.append("='G' AND ");
	    	sql.append(FacSerieFacturacionBean.C_FECHABAJA);
	    	sql.append(" IS NULL AND EXISTS (SELECT 1 FROM ");
	    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	    	sql.append(" WHERE ");
	    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
	    	sql.append(" = ");
	    	sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
	    	sql.append(" AND ");
	    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
	    	sql.append(" = ");
	    	sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
	    	sql.append(" AND ");
	    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO);
	    	sql.append(" = ");
	    	sql.append(idTipoProducto);
	    	sql.append(" AND ");
	    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	    	sql.append(".");
	    	sql.append(FacTiposProduIncluEnFactuBean.C_IDPRODUCTO);
	    	sql.append(" = ");
	    	sql.append(idProducto);
	    	sql.append(") ORDER BY ");
	    	sql.append(FacSerieFacturacionBean.C_DESCRIPCION);	        
	        salida = this.selectSQL(sql.toString());
	        
	        if (salida==null || salida.size()==0) {	    		    	
		    	sql = new StringBuilder();
		    	sql.append(UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()));
		    	sql.append(" WHERE ");
		    	sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
		    	sql.append("=");
		    	sql.append(idInstitucion);
		    	sql.append(" AND ");
		    	sql.append(FacSerieFacturacionBean.C_FECHABAJA);
		    	sql.append(" IS NULL AND EXISTS (SELECT 1 FROM ");
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(" WHERE ");
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
		    	sql.append(" = ");
		    	sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
		    	sql.append(" AND ");
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
		    	sql.append(" = ");
		    	sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
		    	sql.append(" AND ");
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO);
		    	sql.append(" = ");
		    	sql.append(idTipoProducto);
		    	sql.append(" AND ");
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDPRODUCTO);
		    	sql.append(" = ");
		    	sql.append(idProducto);
		    	sql.append(" AND EXISTS (SELECT 1 FROM TABLE(PKG_SIGA_FACTURACION.OBTENCIONPOBLACIONCLIENTES("); 
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
		    	sql.append(", "); 
		    	sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		    	sql.append(".");
		    	sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
		    	sql.append(")) PoblSF WHERE PoblSF.IDPERSONA=");
		    	sql.append(idPersona);
		    	sql.append(")) ORDER BY ");
		    	sql.append(FacSerieFacturacionBean.C_DESCRIPCION);	        
		        salida = this.selectSQL(sql.toString());
	        }
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion del producto.");
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
	public Vector<FacSerieFacturacionBean> obtenerSeriesAdecuadas(Vector<PysCompraBean> compras) throws ClsExceptions {
	    Vector<FacSerieFacturacionBean> salida = new Vector<FacSerieFacturacionBean>();
	    try{
	        if (compras==null || compras.size()==0) {
	            throw new ClsExceptions("Error: No se han recibido compras.");
	        }
	        
	        String idInstitucion="", idPersona="";
	        StringBuilder listadoProductos = new StringBuilder();
	        int numeroTipos=0;
	        
	        // Obtiene un listado de los tipos de productos y un contador
	        for (int i=0; i<compras.size(); i++) {
	            PysCompraBean b = (PysCompraBean) compras.get(i);
	            String sTipoProducto = "('" + b.getIdProducto() + "','" + b.getIdTipoProducto() + "')";
	            
	            if (listadoProductos.length()>0) {
	            	if (listadoProductos.indexOf(sTipoProducto) == -1) {
	            		numeroTipos++;
	            		listadoProductos.append(",");
	            		listadoProductos.append(sTipoProducto);
	            	}

	            } else {
	            	numeroTipos++;
	            	listadoProductos.append(sTipoProducto);
	            	idInstitucion = b.getIdInstitucion().toString();
	            	idPersona = (b.getIdPersonaDeudor()!=null && !b.getIdPersonaDeudor().toString().equals("") ? b.getIdPersonaDeudor().toString() : b.getIdPersona().toString());
	            }
	        }
	        
	        StringBuilder sql = new StringBuilder();
	        sql.append(UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean())); 
	        sql.append(" WHERE ");
	        sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
	        sql.append("=");
	        sql.append(idInstitucion);
	        sql.append(" AND ");
	    	sql.append(FacSerieFacturacionBean.C_TIPOSERIE);
	    	sql.append("='G' AND ");
	        sql.append(FacSerieFacturacionBean.C_FECHABAJA);
	        sql.append(" IS NULL AND ");
	        sql.append(numeroTipos);
	        sql.append("=(SELECT COUNT(*) FROM ");
	        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	        sql.append(" WHERE ("); 
	        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacTiposProduIncluEnFactuBean.C_IDPRODUCTO);
	        sql.append(", "); 
	        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO); 
	        sql.append(") IN (");
	        sql.append(listadoProductos);
	        sql.append(") AND ");
	        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
	        sql.append(" = ");
	        sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
	        sql.append(" AND ");
	        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
	        sql.append(" = ");
	        sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
	        sql.append(".");
	        sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
	        sql.append(") ORDER BY ");
	        sql.append(FacSerieFacturacionBean.C_DESCRIPCION);	        
	        salida = this.selectSQL(sql.toString());
	        
	        if (salida==null || salida.size()==0) {	 
		        sql = new StringBuilder();
		        sql.append(UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean())); 
		        sql.append(" WHERE ");
		        sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
		        sql.append("=");
		        sql.append(idInstitucion);
		        sql.append(" AND ");
		        sql.append(FacSerieFacturacionBean.C_FECHABAJA);
		        sql.append(" IS NULL AND ");
		        sql.append(numeroTipos);
		        sql.append("=(SELECT COUNT(*) FROM ");
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(" WHERE ("); 
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDPRODUCTO);
		        sql.append(", "); 
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO); 
		        sql.append(") IN (");
		        sql.append(listadoProductos);
		        sql.append(") AND ");
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
		        sql.append(" = ");
		        sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacSerieFacturacionBean.C_IDINSTITUCION);
		        sql.append(" AND ");
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
		        sql.append(" = ");
		        sql.append(FacSerieFacturacionBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacSerieFacturacionBean.C_IDSERIEFACTURACION);
		        sql.append(" AND EXISTS (SELECT 1 FROM TABLE(PKG_SIGA_FACTURACION.OBTENCIONPOBLACIONCLIENTES("); 
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDINSTITUCION);
		        sql.append(", "); 
		        sql.append(FacTiposProduIncluEnFactuBean.T_NOMBRETABLA);
		        sql.append(".");
		        sql.append(FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION);
		        sql.append(")) PoblSF WHERE PoblSF.IDPERSONA=");
		        sql.append(idPersona);
		        sql.append(")) ORDER BY ");
		        sql.append(FacSerieFacturacionBean.C_DESCRIPCION);	        
		        salida = this.selectSQL(sql.toString());	        
	        }
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
	    
		return salida;
	}		
}