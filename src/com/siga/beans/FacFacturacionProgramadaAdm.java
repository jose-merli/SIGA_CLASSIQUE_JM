/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 * nuria.rgonzalez - 08-03-05 - incorporación: getCamposFacturacion(), getTablasFacturacion(), selectDatosFacturacion(String sWhere, String[] orden), getNuevoID(FacFacturacionProgramadaBean bean).
 */

package com.siga.beans;

import java.io.File;
import java.util.*;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.certificados.Plantilla;
import com.siga.envios.Envio;
import com.siga.general.SIGAException;



public class FacFacturacionProgramadaAdm extends MasterBeanAdministrador {
	
	public FacFacturacionProgramadaAdm (UsrBean usu) {
		super (FacFacturacionProgramadaBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacFacturacionProgramadaBean.C_IDINSTITUCION, 		
							FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,
							FacFacturacionProgramadaBean.C_IDPROGRAMACION, 	
							FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS,
							FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS,	
							FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS,
							FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS,
							FacFacturacionProgramadaBean.C_FECHAREALGENERACION,
							FacFacturacionProgramadaBean.C_FECHACONFIRMACION,
							FacFacturacionProgramadaBean.C_FECHAPROGRAMACION,
							FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION,
							FacFacturacionProgramadaBean.C_IDPREVISION,
							FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM ,
							//FacFacturacionProgramadaBean.C_FECHAREALCONFIRM ,
							FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION ,
							FacFacturacionProgramadaBean.C_IDESTADOPDF ,
							FacFacturacionProgramadaBean.C_IDESTADOENVIO ,
							FacFacturacionProgramadaBean.C_GENERAPDF ,
							FacFacturacionProgramadaBean.C_ENVIO ,
							FacFacturacionProgramadaBean.C_ARCHIVARFACT ,
							FacFacturacionProgramadaBean.C_LOCKED ,
							FacFacturacionProgramadaBean.C_FECHACARGO,
							FacFacturacionProgramadaBean.C_CONFDEUDOR,
							FacFacturacionProgramadaBean.C_CONFINGRESOS,
							FacFacturacionProgramadaBean.C_CTACLIENTES,
							FacFacturacionProgramadaBean.C_CTAINGRESOS,
							FacFacturacionProgramadaBean.C_VISIBLE,
							FacFacturacionProgramadaBean.C_DESCRIPCION,
							FacFacturacionProgramadaBean.C_IDTIPOENVIOS,
							FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL,
							FacFacturacionProgramadaBean.C_FECHAMODIFICACION,
							FacFacturacionProgramadaBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, FacFacturacionProgramadaBean.C_IDPROGRAMACION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFacturacionProgramadaBean bean = null;
		
		try {
			bean = new FacFacturacionProgramadaBean();
			bean.setIdInstitucion			(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion		(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION));
			bean.setIdProgramacion			(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION));
			bean.setFechaInicioProductos	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS));
			bean.setFechaFinProductos	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS));
			bean.setFechaInicioServicios	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS));
			bean.setFechaFinServicios	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS));
			bean.setFechaRealGeneracion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAREALGENERACION));
			bean.setFechaConfirmacion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACONFIRMACION));
			bean.setFechaProgramacion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION));
			bean.setFechaPrevistaGeneracion	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION));
			bean.setIdPrevision				(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPREVISION));

			bean.setFechaPrevistaConfirmacion(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM));
			//bean.setFechaRealConfirmacion(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAREALCONFIRM));
			bean.setIdEstadoConfirmacion(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION));
			bean.setIdEstadoPDF(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOPDF));
			bean.setIdEstadoEnvio(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOENVIO));
			bean.setGenerarPDF(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_GENERAPDF));
			bean.setEnvio(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_ENVIO));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDTIPOENVIOS));
			bean.setIdTipoPlantillaMail(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL));
			bean.setArchivarFact(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_ARCHIVARFACT));
			bean.setLocked(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_LOCKED));

			bean.setConfDeudor(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CONFDEUDOR));
			bean.setConfIngresos(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CONFINGRESOS));
			bean.setCtaClientes(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CTACLIENTES));
			bean.setCtaIngresos(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CTAINGRESOS));
			bean.setVisible(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_VISIBLE));
			bean.setDescripcion(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_DESCRIPCION));
			bean.setFechaCargo				(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACARGO));
			bean.setFechaMod				(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAMODIFICACION));
			bean.setUsuMod					(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_USUMODIFICACION));

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
			FacFacturacionProgramadaBean b = (FacFacturacionProgramadaBean) bean;
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDPROGRAMACION, b.getIdProgramacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS, b.getFechaInicioProductos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS, b.getFechaFinProductos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS, b.getFechaInicioServicios());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS, b.getFechaFinServicios());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAREALGENERACION, b.getFechaRealGeneracion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, b.getFechaConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION, b.getFechaProgramacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION, b.getFechaPrevistaGeneracion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDPREVISION, b.getIdPrevision());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, b.getFechaPrevistaConfirmacion());
			//UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAREALCONFIRM, b.getFechaRealConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, b.getIdEstadoConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOPDF, b.getIdEstadoPDF());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOENVIO, b.getIdEstadoEnvio());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_GENERAPDF, b.getGenerarPDF());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_ENVIO, b.getEnvio());
			if(b.getIdTipoPlantillaMail() != null){
				UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL, b.getIdTipoPlantillaMail());
			}else{
				UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL, "");
			}
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_ARCHIVARFACT, b.getArchivarFact());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_LOCKED, b.getLocked());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHACARGO, b.getFechaCargo());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CONFDEUDOR, b.getConfDeudor());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CONFINGRESOS, b.getConfIngresos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CTACLIENTES, b.getCtaClientes());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CTAINGRESOS, b.getCtaIngresos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_VISIBLE, b.getVisible());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_DESCRIPCION, b.getDescripcion());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_USUMODIFICACION, b.getUsuMod());
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
	
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl(Max("+FacFacturacionProgramadaBean.T_NOMBRETABLA+"."+FacFacturacionProgramadaBean.C_IDPROGRAMACION+"), 0) + 1 IDPROGRAMACION";
			sql += " From "+FacFacturacionProgramadaBean.T_NOMBRETABLA;
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
	/**
	 * Devuelve un array con los nombres de los campos del bean FAC_FacturacionProgramada + el campo NombreAbreviado de FAC_SereiFacturacion necesarios para construir la select en 'PROGRAMAR FACTURACION'.
	 * @author nuria.rgonzalez 10-03-05
	 */
	protected String[] getCamposFacturacion() {
		int num = getCamposBean().length;
		String[] aux = new String [num+1];
		String[] campos = getCamposBean();
		for(int i = 0; i < num; i++){
			aux[i] = FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + campos[i];
		}
		aux[num] = FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO;		
				
		return aux;
	}
	
	/**
	 * Devuelve un string con el nombre de las tablas FAC_FACTURACIONPROGRAMADA, FAC_SERIEFACTURACION y las relaciones entre ellas para construir la Query.
	 * @author nuria.rgonzalez 10-03-05
	 */
	protected String getTablasFacturacion(){
		
		String campos = FacFacturacionProgramadaBean.T_NOMBRETABLA 
			+ " LEFT JOIN "+ 
			 FacSerieFacturacionBean.T_NOMBRETABLA +
			 " ON "+
			 FacFacturacionProgramadaBean.T_NOMBRETABLA +"."+ FacFacturacionProgramadaBean.C_IDINSTITUCION + "=" +
			 FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
			 " AND "+
			 FacFacturacionProgramadaBean.T_NOMBRETABLA +"."+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + "=" +
			 FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION;
		 		
		return campos;
	}
	
	/**
	 * Devuelve un Vector con los datos de la Facturacion Programada  del cliente pasado como parámetro.
	 * @author nuria.rgonzalez 10-03-05
	 * @version 1	 
	 * @param sWhere, string que contiene la sentencia where
	 * @param orden, array que contiene los campos por los que queremos ordenar los resultados
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Vector selectDatosFacturacion(String sWhere, String[] orden) throws ClsExceptions, SIGAException{
		
		Vector v = null;		
		RowsContainer rc = null;
		String where = sWhere;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasFacturacion(), this.getCamposFacturacion());
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(orden);  
			sql += "desc";
            rc = this.find(sql);
            if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosFacturacion");
		}
		return v;
	}

	
	/**
	 * Devuelve un Vector con los datos de la Facturacion Programada  del cliente pasado como parámetro.
	 * @author nuria.rgonzalez 10-03-05
	 * @version 1	 
	 * @param sWhere, string que contiene la sentencia where
	 * @param orden, array que contiene los campos por los que queremos ordenar los resultados
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Vector selectDatosFacturacionBean(String sWhereBind,Hashtable codigos, String[] orden) throws ClsExceptions, SIGAException{
		
		Vector v = null;		
		RowsContainer rc = null;
		String where = sWhereBind;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasFacturacion(), this.getCamposFacturacion());
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(orden);  
			sql += "desc";
            rc = this.findBind(sql,codigos);
            if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					MasterBean registro1=(MasterBean)this.hashTableToBean(registro); 
					if (registro1 != null) 
						v.add(registro1);
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosFacturacion");
		}
		return v;
	}

	/**
	 * Obtiene un nuevo ID de Fac_FacturacionProgramada de una institucion  e idSerieFacturacion determinada
	 * @author nuria.rgonzalez 10-03-05
	 * @version 1	 
	 * @param Bean datos de la factura.
	 * @return nuevo ID.
	 * @exception ClsExceptions En cualquier caso de error
	 */
	public Long getNuevoID(FacFacturacionProgramadaBean bean) throws SIGAException, ClsExceptions 
	{
		return getNuevoID(bean.getIdInstitucion().toString(),bean.getIdSerieFacturacion().toString());
	}	

	public Long getNuevoID(String idInstitucion, String idSerieFacturacion) throws SIGAException, ClsExceptions 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT NVL((MAX(" + FacFacturacionProgramadaBean.C_IDPROGRAMACION + ") + 1),1) AS " + FacFacturacionProgramadaBean.C_IDPROGRAMACION + 
			  			 " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA +
						 " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion +
						 " AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;

			rc = this.findForUpdate(sql);
			if (rc!=null && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Long idProgramacion = UtilidadesHash.getLong(prueba, FacFacturacionProgramadaBean.C_IDPROGRAMACION);
				if (idProgramacion == null) {
					return new Long(1);
				}
				else return idProgramacion;								
			}
		}
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	

	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}

	
	/**
	 * Realiza el tratamiento de una facturación para ponerle los estados de confirmacion, generacion de pdf y envio de facturas adecuados segun sus datos.
	 * @param bean Obtendremos de el los datos
	 * @return bean con los estados actualizados
	 * @throws ClsExceptions
	 */
	public FacFacturacionProgramadaBean tratamientoEstadosProgramacion(FacFacturacionProgramadaBean bean) throws ClsExceptions {
		
		FacFacturacionProgramadaBean aux = bean;
		try { 
			// confirmacion
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA);
			} else {
				bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PENDIENTE);
			}
			// PDF
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				if (bean.getGenerarPDF().equals("1")) {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PROGRAMADA);
				} else {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
				}
			} else { 
				if (bean.getGenerarPDF().equals("1")) {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PENDIENTE);
				} else {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
				}
			}
			// Envio
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				if (bean.getEnvio().equals("1")) {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_PROGRAMADA);
				} else {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
				}
			} else { 
				if (bean.getEnvio().equals("1")) {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_PENDIENTE);
				} else {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
				}
			}
			
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.tratamientoEstadosProgramacion().");
		}
		return aux;	
	}

	/**
	 * Realiza las comprobaciones oportunas de plantillas necesarias para que la confirmación de la facturación funcione sin problemas
	 * @return Vector con la lista de mensajes tipo idrecurso
	 * @throws ClsExceptions
	 */
	public Vector comprobarRecursosProgramacion(FacFacturacionProgramadaBean bean) throws ClsExceptions {
		
		Vector v = new Vector();
		try { 
			String idserieidprogramacion = bean.getIdSerieFacturacion().toString()+"_" + bean.getIdProgramacion().toString();
			String institucion = bean.getIdInstitucion().toString();
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			//ReadProperties p = new ReadProperties ("SIGA.properties");
			
			// directorio de fichero bancario 
			String pathFichero = p.returnProperty("facturacion.directorioBancosOracle");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
    		pathFichero += sBarra+institucion;
    		File aux = new File(pathFichero);
    		if (!aux.exists()) {
    			v.add("messages.facturacion.comprueba.noPathFicheroBancario"); // No existe el path de generacion de fichero bancario
    		} else 
    		if (!aux.canWrite()) {
    			v.add("messages.facturacion.comprueba.noPermisosPathFicheroBancario"); // EL path de generación de fichero bancarios no tiene los permisos adecuados.
    		} 
    		
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.usrbean);
			Vector plantillas=plantillaAdm.getPlantillaSerieFacturacion(institucion,bean.getIdSerieFacturacion().toString());
			if (plantillas==null || plantillas.size()==0) {
				v.add("messages.facturacion.comprueba.plantillaFacturaNoConfigurada"); // No esta configurada una plantilla de factura.
			} else { 
				String plantilla=plantillas.firstElement().toString();
				Plantilla plantillaMng = new Plantilla(this.usrbean);

				// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
			    String rutaTemporal = p.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+p.returnProperty("facturacion.directorioTemporalFacturasJava");
	    		String barraTemporal = "";
	    		String nombreFicheroTemporal = "";
	    		if (rutaTemporal.indexOf("/") > -1){ 
	    			barraTemporal = "/";
	    		}
	    		if (rutaTemporal.indexOf("\\") > -1){ 
	    			barraTemporal = "\\";
	    		}    		
	    		rutaTemporal += barraTemporal+institucion.toString();
				File rutaFOP=new File(rutaTemporal);
				if (!rutaFOP.exists()) {
	    			v.add("messages.facturacion.comprueba.noPathTemporalFacturas"); // No existe el path temporal de facturas
	    		} else 
	    		if (!rutaFOP.canWrite()) {
	    			v.add("messages.facturacion.comprueba.noPermisosPathFicheroBancario"); // El path temporal de facturas no tiene los permisos adecuados.
	    		} 
			
	    		
	    		
				// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
//			    String rutaAlmacen = p.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+p.returnProperty("facturacion.directorioFacturaPDFJava");
//	    		String barraAlmacen = "";
//	    		String nombreFicheroAlmacen = "";
//	    		if (rutaAlmacen.indexOf("/") > -1){ 
//	    			barraAlmacen = "/";
//	    		}
//	    		if (rutaAlmacen.indexOf("\\") > -1){ 
//	    			barraAlmacen = "\\";
//	    		}    		
	/**/   		
//	    		rutaAlmacen += barraAlmacen+institucion.toString()+barraAlmacen+idserieidprogramacion;
				
				/*File rutaPDF=new File(rutaAlmacen);
				rutaPDF.mkdirs();
				if (!rutaPDF.exists()) {
	    			v.add("messages.facturacion.comprueba.noPathFacturas") ; // No existe el path de las facturas
	    		} else 
	    		if (!rutaPDF.canWrite()) {
	    			v.add("messages.facturacion.comprueba.noPermisosPathFacturas"); // El path de facturas no tiene los permisos adecuados.
	    		} */

				// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
			    String rutaPlantilla = p.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+p.returnProperty("facturacion.directorioPlantillaFacturaJava");
			    String barraPlantilla="";
	    		if (rutaPlantilla.indexOf("/") > -1){
	    			barraPlantilla = "/";
	    		}
	    		if (rutaPlantilla.indexOf("\\") > -1){
	    			barraPlantilla = "\\";
	    		}
	    		rutaPlantilla += barraPlantilla+institucion.toString()+barraPlantilla+plantilla;
				File rutaModelo=new File(rutaPlantilla);
				//Comprobamos que exista la ruta y sino la creamos
				if (!rutaModelo.exists()){
					v.add("messages.facturacion.comprueba.noPlantillaFacturacion"); // No existe la plantilla de facturación					
				}
			}
			
    		if (bean.getEnvio().equals("1")) {
				// plantilla de envios
				GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
				String preferencia = paramAdm.getValor(institucion.toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");
				Integer tipoEnvio = Envio.calculaTipoEnvio(preferencia);
	
				// Recojo una plantilla valida cualquiera:
				EnvPlantillasEnviosAdm plantillasEnviosAdm = new EnvPlantillasEnviosAdm(this.usrbean);
				Vector plantillasValidas=plantillasEnviosAdm.getIdPlantillasValidos(institucion,tipoEnvio.toString());
				//Si no hay plantillas:
				if (plantillasValidas==null || plantillasValidas.size()==0) {
					v.add("messages.facturacion.comprueba.noConfigPlantillaEnvios"); // No esta configurada ninguna plantilla de envios.
				} else { 
					// RGG Damos por supuesto que el proceso de crear envios automaticamwnte obtiene la primera plantilla que encuentra
					String idplantillaenvios=plantillasValidas.firstElement().toString();
					EnvPlantillaGeneracionAdm admPlantilla= new EnvPlantillaGeneracionAdm(this.usrbean);  
					Vector plantillasEnvio = admPlantilla.obtenerListaPlantillas(institucion,tipoEnvio.toString(),idplantillaenvios);
					if (tipoEnvio.intValue()!=1 && (plantillasEnvio==null || plantillasEnvio.size()==0)) {
						v.add("messages.facturacion.comprueba.noConfigPlantillaGeneracionEnvios"); // No estan configuradas plantillas de generacion para la plantilla seleccionada
					} else {
			   	        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_PLANTILLAS' AND "+
	                    GenParametrosBean.C_MODULO + "='CER' AND " + 
	                    GenParametrosBean.C_IDINSTITUCION + "=0";
						GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
						Vector vParametros = admParametros.select(sWhere);
						if (vParametros==null || vParametros.size()==0)
						{
							v.add("messages.facturacion.comprueba.pathPlantillasMalConfiguradoParametros"); // El path de plantillas esta mal configurado
						}

						GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
						String sPath = beanParametros.getValor();

	
			   	        if (tipoEnvio.intValue()!=1) {
							// RGG Damos por supuesto que el proceso de crear envios automaticamwnte obtiene la primera plantilla de generacion que encuentra en la plantilla
							EnvPlantillaGeneracionBean beanPL = (EnvPlantillaGeneracionBean) plantillasEnvio.get(0); 	
				   	        String sCompuesto = beanPL.getIdTipoEnvios() + "_" + beanPL.getIdPlantillaEnvios() + "_" + beanPL.getIdPlantilla();

				   	        String sNombreFinal = sPath + File.separator + institucion + File.separator + sCompuesto;
				   	        File f = new File(sNombreFinal);
							if (!f.exists()) {
								v.add("messages.facturacion.comprueba.noFicheroPlantillaEnvios"); // No existe la plantilla configurada de envios
				    		} else 
				    		if (!f.canRead()) {
				    			v.add("messages.facturacion.comprueba.noPermisosFicheroPlantillaEnvios"); // La plantilla de envios configurada no tiene los permisos adecuados.
				    		} 
			   	        }
					}
				}
    		}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.comprobarRecursosProgramacion().");
		}
		return v;	
	}
	
	
    public FacFacturacionProgramadaBean getFacturacionProgramadaDesdeCompra (PysCompraBean beanCompra)
    {	
		try {
	    	FacFacturaAdm facturaAdm = new FacFacturaAdm (this.usrbean);
	    	Hashtable h = new Hashtable();
	    	UtilidadesHash.set(h, FacFacturaBean.C_IDFACTURA, beanCompra.getIdFactura());
	    	UtilidadesHash.set(h, FacFacturaBean.C_IDINSTITUCION, beanCompra.getIdInstitucion());
	    	Vector v = facturaAdm.selectByPK(h);
			if (v != null && v.size() == 1) {
	    		FacFacturaBean b = (FacFacturaBean) v.get(0);
	    		h.clear();
		    	UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, b.getIdInstitucion());
		    	UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
		    	UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDPROGRAMACION, b.getIdProgramacion());
		    	Vector v1 = this.selectByPK(h);
		    	if (v1 != null && v1.size() == 1) {
		    		return (FacFacturacionProgramadaBean) v1.get(0);
		    	}
	    	}
		} 
		catch (ClsExceptions e) {}
    	return null;
    }
	
    public String getDescripcionPorDefecto (FacFacturacionProgramadaBean b) 
    { 
    	try {
	    	FacSerieFacturacionAdm adm = new FacSerieFacturacionAdm (this.usrbean);
	    	Hashtable h = new Hashtable ();
	    	UtilidadesHash.set (h, FacSerieFacturacionBean.C_IDINSTITUCION, b.getIdInstitucion());
	    	UtilidadesHash.set (h, FacSerieFacturacionBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
	    	
	    	Vector v = adm.select(h);
	    	if (v != null && v.size() == 1) {
	    		FacSerieFacturacionBean bSerie = (FacSerieFacturacionBean)v.get(0);
	    		String nombre = bSerie.getNombreAbreviado() + " [" + b.getIdProgramacion() + "]";
	    		return nombre;
	    	}
    		return "";
    	}
    	catch (Exception e) {
    		return "";
    	}
    }
}
