/*
 * Created on 03-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.Articulo;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysServiciosSolicitadosAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public PysServiciosSolicitadosAdm(UsrBean usuario) {
		super(PysServiciosSolicitadosBean.T_NOMBRETABLA, usuario);
	}

	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysServiciosSolicitadosBean.C_ACEPTADO, PysServiciosSolicitadosBean.C_CANTIDAD,
				PysServiciosSolicitadosBean.C_FECHAMODIFICACION, PysServiciosSolicitadosBean.C_IDCUENTA,
				PysServiciosSolicitadosBean.C_IDFORMAPAGO, PysServiciosSolicitadosBean.C_IDINSTITUCION,
				PysServiciosSolicitadosBean.C_IDPERIODICIDAD, PysServiciosSolicitadosBean.C_IDPERSONA,
				PysServiciosSolicitadosBean.C_IDPETICION, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS, 
				PysServiciosSolicitadosBean.C_IDSERVICIO, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION,
				PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, PysServiciosSolicitadosBean.C_USUMODIFICACION,
				PysServiciosSolicitadosBean.C_ORDEN};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] campos = {				
				PysServiciosSolicitadosBean.C_IDINSTITUCION,
				PysServiciosSolicitadosBean.C_IDPETICION,  
				PysServiciosSolicitadosBean.C_IDSERVICIO, 
				PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION,
				PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysServiciosSolicitadosBean bean = null;
		
		try {
			bean = new PysServiciosSolicitadosBean();
			bean.setAceptado(UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_ACEPTADO));
			bean.setCantidad(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_CANTIDAD));
			bean.setFechaMod(UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_FECHAMODIFICACION));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDCUENTA));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION));
			bean.setIdPeriodicidad(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDPERIODICIDAD));
			bean.setIdPersona(UtilidadesHash.getLong(hash, PysServiciosSolicitadosBean.C_IDPERSONA));
			bean.setIdPeticion(UtilidadesHash.getLong(hash, PysServiciosSolicitadosBean.C_IDPETICION));
			bean.setIdPreciosServicios(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS));
			bean.setIdServicio(UtilidadesHash.getLong(hash, PysServiciosSolicitadosBean.C_IDSERVICIO));
			bean.setIdServicioInstitucion(UtilidadesHash.getLong(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdTipoServicios(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_USUMODIFICACION));
			bean.setOrden(UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_ORDEN));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysServiciosSolicitadosBean b = (PysServiciosSolicitadosBean) bean;
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_ACEPTADO , b.getAceptado());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_CANTIDAD , b.getCantidad());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_FECHAMODIFICACION , b.getFechaMod());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDCUENTA , b.getIdCuenta());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDFORMAPAGO , b.getIdFormaPago());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDINSTITUCION , b.getIdInstitucion());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDPERIODICIDAD , b.getIdPeriodicidad());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDPERSONA , b.getIdPersona());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDPETICION , b.getIdPeticion());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS , b.getIdPreciosServicios());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDSERVICIO , b.getIdServicio());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION , b.getIdServicioInstitucion());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS , b.getIdTipoServicios());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_USUMODIFICACION , b.getUsuMod());
			UtilidadesHash.set(htData, PysServiciosSolicitadosBean.C_ORDEN , b.getOrden());

		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/**
	 * Funcion getServiciosSolicitados
	 * Obtiene los datos de los servicios solicitados a partir de unos criterios
	 * @param Hashtable con los datos necesarios para la consulta
	 * @param idInstitucion
	 * @param peticionBaja en funcion de este parametro se obtienen o no los servicios pendientes de confirmar su solicitud.
	 * @return vector con todos los registros recuperados
	 * @throws SIGAException, ClsExceptions
	 */
	public Vector getServiciosSolicitados (Hashtable datos, Integer idInstitucion, boolean peticionBaja) throws ClsExceptions, SIGAException {

		try { 
			Long idPeticion = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDPETICION);
			Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
			String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			
			String distintoCampoAceptado = UtilidadesHash.getString(datos, PysServiciosSolicitadosBean.C_ACEPTADO);
			
			
			// MODIFICADO POR MAV PARA INCLUIR NUEVOS FILTROS /////////////////////////////////////////////////////////////////
			Integer idTipoServicios = UtilidadesHash.getInteger(datos, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);		 //
			Long idServicio = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDSERVICIO);						 //
			Long idServiciosInstitucion = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);//
			// FIN MODIFICACION ///////////////////////////////////////////////////////////////////////////////////////////////
			
			//Si viene con este parametro es que se hace el select desde Solicitudbajaaction.
			//No lo metemos como parametro en el metodo porque se llama desde distinto sitios
			boolean isSolicitudBaja = UtilidadesHash.getString(datos, "ES_SOLICITUD_BAJA")!=null;
			
			String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE");
			String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA");
			
			String select = "SELECT 'SERVICIO' AS CONSULTA, " + 
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION+ ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDCUENTA + ", " + 
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_CANTIDAD + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_ACEPTADO + ", " +
									PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDFORMAPAGO + ", " +
									
									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + ", " +
									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ", " +
									
         							" CASE WHEN (" + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = 'B') THEN " +
		 									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA +
		 								" ELSE ( " +
		 									" SELECT PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
		 									" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + " PC, " +
		 										PysServiciosSolicitadosBean.T_NOMBRETABLA + " PS " + 
											" WHERE PS." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
												" AND PS." + PysServiciosSolicitadosBean.C_IDPETICION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION +                                   
												" AND PC." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION +
												" AND PS." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION +
												" AND PS." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
												" AND PS." + PysServiciosSolicitadosBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
												" AND PS." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
												" AND PS." + PysServiciosSolicitadosBean.C_IDPERSONA + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPERSONA +
										" ) " +
									" END AS IDPETICIONRELACIONADA, " +   									
									
									" F_SIGA_CALCULOPRECIOSERVICIO( " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + ", " + 
										PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + ", " +
										this.usrbean.getLanguageInstitucion() + ") AS PRECIO_SERVICIO, " +
									
									" F_SIGA_COMPROBAR_ANTICIPAR( " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " + 
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + ", " + 
										"'S', " + 
										PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + ", " +
										PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + ", " +
										" F_SIGA_CALCULOPRECIOSERVICIO( " + 
											PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " + 
											PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " + 
											PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " + 
											PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + ", " + 
											PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + ", " +
											this.usrbean.getLanguageInstitucion() + 
										" ) " +
									" ) AS ANTICIPAR, " + 
									
									// Forma Pago
									" ( " +
										"SELECT " + UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()) +
										" FROM " + PysFormaPagoBean.T_NOMBRETABLA + 
										" WHERE " + PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_IDFORMAPAGO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDFORMAPAGO +
									" ) AS FORMAPAGO, " +

									
									// Fecha Efectiva:									
									" NVL( " +
										" ( " +
											" SELECT " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHASUSCRIPCION + 
											" FROM " + PysSuscripcionBean.T_NOMBRETABLA +
											" WHERE " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION +										  
										  		" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
										  		" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
										  		" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
										  		" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION +
										  		" AND ROWNUM = 1 " +
										  " ), NVL( " + 
									  		" ( " +
									  			" SELECT " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHABAJA + 
									  			" FROM " + PysSuscripcionBean.T_NOMBRETABLA +
									  			" WHERE " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION +										  
									  				" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
								  					" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
								  					" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
								  					" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA +
								  					" AND ROWNUM = 1 " +
								  			" ), ( " +
												" SELECT PC." + PysPeticionCompraSuscripcionBean.C_FECHA + 
												" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + " PC, " +
												PysServiciosSolicitadosBean.T_NOMBRETABLA + " PS " +
												" WHERE PS." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDPETICION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDPERSONA + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPERSONA +
													" AND PC." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " <> " + ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE +
													" AND PC." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = 'B' " +
													" AND PC." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + " IS NOT NULL " +
													" AND PS." + PysServiciosSolicitadosBean.C_ACEPTADO + " = 'B' " +	
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDPETICION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION +
	     											" AND PS." + PysServiciosSolicitadosBean.C_IDPERSONA + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPERSONA +
											" ) " +
	     								" ) " +
									" ) AS FECHAEFEC, " +
									
									// Concepto
									" ( " +
										"SELECT " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION + 
										" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA + 
										" WHERE " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + 
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
									" ) AS CONCEPTO, " +
									
									// Solicitar Baja
									" ( " +
										"SELECT " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_SOLICITARBAJA + 
										" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA + 
										" WHERE " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + 
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO +
											" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
									" ) AS SOLICITARBAJA, " +

									// Está facturado
									" ( " +
										"SELECT 1 " + 
										" FROM " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + ", " + 
											PysSuscripcionBean.T_NOMBRETABLA +
										" WHERE " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + 
											" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + 
											" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSERVICIO + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + 
											" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + 
											" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSUSCRIPCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSUSCRIPCION + 
											" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
											" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
											" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_NUMEROLINEA + " = 1 " + 
											" AND ROWNUM < 2) AS ESTAFACTURADO, " +

									// Cuenta
									" NVL( " +
										" ( " +
											" SELECT "+ CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN +
											" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
											" WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDCUENTA +
												" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + 
												" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPERSONA + 
										" ) " + 
									" ,'-') AS NCUENTA, " +

									" F_SIGA_ESTADOSUSCRIPCION( " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION +
									" ) AS ESTADOPAGO ";
			
			String from  =  " FROM " + PysServiciosSolicitadosBean.T_NOMBRETABLA + ", " + 
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA;
			
			String where =  " WHERE " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
								" AND " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION;	
			
			if (idPeticion != null) {
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + " = " + idPeticion;
			}
			
			if (idPersona != null) {
				where += " AND " +  PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + " = " + idPersona;
			}
			
			if (tipoPeticion != null) {
				where += " AND " +  PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = '" + tipoPeticion + "' ";
			}
			
			if (distintoCampoAceptado != null) {
				where += " AND " +  PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_ACEPTADO + " <> '" + distintoCampoAceptado + "' ";
			}
		
			// MODIFICADO POR MAV PARA INCLUIR NUEVOS FILTROS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (idTipoServicios != null) {																																	//
				where += " AND " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + " = " + idTipoServicios;				//
			}			
			//
			if (idServicio != null) {																																		//
				where += " AND " +  PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + " = " + idServicio;						//
			}																																								//
			
			if (idServiciosInstitucion != null) {																															//
				where += " AND " +  PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + " = " + idServiciosInstitucion;	//
			}																																								//
			// FIN MODIFICACION //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			// Fechas
			if (fechaDesde != null) {
				if (fechaDesde.indexOf("SYSDATE")!=-1) {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + fechaDesde ;
				} else {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + " TO_DATE('" + fechaDesde + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
				}
			}
			
			if (fechaHasta != null) {
				if (fechaHasta.indexOf("SYSDATE")!=-1) {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + fechaHasta;
				} else {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + " TO_DATE('" + fechaHasta + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
				}
			}
			
			if (peticionBaja){
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " = " + ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA;
			}
			
			String orderBy = " ORDER BY " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " DESC ";
			
			String consulta = select + from + where + orderBy; 
			
			RowsContainer rc = new RowsContainer();
			if (rc.query(consulta)) {
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					
					Hashtable a = (Hashtable)((Row) rc.get(i)).getRow();
					
					// Tratamos los datos de la funcion 'F_SIGA_CALCULOPRECIOSERVICIO'
					String valor = UtilidadesHash.getString(a, "PRECIO_SERVICIO");
					
					//ClsLogging.writeFileLogWithoutSession("Valor recuperado de la consulta  "+valor, 10);
					

					// "-1" --> Error no existen datos en la tabla Pys_ServicioInstitucion
					//if (!valor.equalsIgnoreCase("0#0#0#0#")){
					if (!valor.equalsIgnoreCase("-1")){
						
						String datosPrecio[] =  UtilidadesString.splitIgual(valor, "#");
						
						// RGG cambio para 10g
						String diezg = datosPrecio[0];
						diezg = diezg.replaceAll(",",".");
						
						UtilidadesHash.set(a, "VALOR", new Double(diezg));
						
						//ClsLogging.writeFileLogWithoutSession("Valor del importe= "+datosPrecio[0], 10);
						//ClsLogging.writeFileLogWithoutSession("Valor del importe tras cambio 10g= "+diezg, 10);
				
						// RGG cambio para 10g
						String diezgg = datosPrecio[1];
						diezgg = diezgg.replaceAll(",",".");
						
						UtilidadesHash.set(a, "PORCENTAJEIVA", new Float(diezgg));
						
						UtilidadesHash.set(a, "SERVICIO_IDPRECIOSSERVICIOS", new Integer(datosPrecio[2]));
						UtilidadesHash.set(a, "SERVICIO_IDPERIODICIDAD", new Integer(datosPrecio[3]));
						if (datosPrecio.length == 5) {
							UtilidadesHash.set(a, "SERVICIO_DESCRIPCION_PERIODICIDAD", datosPrecio[4]);
						}
						
						if (datosPrecio.length == 6) {
							datosPrecio[5] = UtilidadesHash.getString(a, "CONCEPTO") + " " + datosPrecio[5];
							UtilidadesHash.set(a, "CONCEPTO", datosPrecio[5]);
							UtilidadesHash.set(a, "SERVICIO_DESCRIPCION_PRECIO", datosPrecio[5]);
							UtilidadesHash.set(a, "SERVICIO_DESCRIPCION_PERIODICIDAD", datosPrecio[4]);
						}
					}

					//Consulto si la baja ha sido solicitada:
					String idTipoServicio = (String)a.get(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
					String idServicios = (String)a.get(PysServiciosSolicitadosBean.C_IDSERVICIO);
					String idServicioInstitucion = (String)a.get(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
					String idPeticionConsulta = (String)a.get(PysServiciosSolicitadosBean.C_IDPETICION);
					if(isSolicitudBaja){
						if (this.getTipoPeticion(idInstitucion,idPersona,idTipoServicio,idServicios,idServicioInstitucion,idPeticionConsulta))
							a.put("ESTADO_BAJA","SI");
						else
							a.put("ESTADO_BAJA","NO");
						String estadoPago = (String)a.get("ESTADOPAGO");
						// CRM--> SI SE PUEDE ANULAR LOS SERVICIOS FACTURADOS
						resultados.add(a);
					}else{
						resultados.add(a);
					}
				}
				return resultados;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e,"Error al obetener los servicios solicitados.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 * confirmarServicio
	 * Confirma la solicitud (de alta o de baja) de un servicio.
	 * @param idInstitucion
	 * @param idPeticion 
	 * @param idTipoServicio
	 * @param idServicio
	 * @param idServicioInstitucion
	 * @return true si todo va bien, false si error
	 * @throws SIGAException, ClsExceptions
	 */
	public boolean confirmarServicio (Integer idInstitucion, Long idPeticion, Integer idTipoServicio, Long idServicio, Long idServicioInstitucion, Integer idCuenta, Double importeAnticipado, String fechaEfectiva) throws SIGAException, ClsExceptions {

		try {

			PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			PysPeticionCompraSuscripcionBean peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);
			if(fechaEfectiva==null)
				fechaEfectiva="0";
			// Peticion de ALTA
			if (peticionBean.getTipoPeticion().equals(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {

				// 1. Marcamos el servicio como ACEPTADO
				claves.clear();
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				PysServiciosSolicitadosAdm servicioAdm = new PysServiciosSolicitadosAdm(this.usrbean);
				PysServiciosSolicitadosBean servicioBean = (PysServiciosSolicitadosBean) servicioAdm.select(claves).get(0);

				servicioBean.setAceptado(ClsConstants.PRODUCTO_ACEPTADO);
				if (servicioBean.getIdFormaPago()!=null && (servicioBean.getIdFormaPago().intValue() != ClsConstants.TIPO_FORMAPAGO_METALICO)){
					if (idCuenta != null) servicioBean.setIdCuenta(idCuenta);
				}	
					if (!servicioAdm.update(servicioBean)) {
						return false;
					}
				
				// 2. Creamos el apunte -> Insertamos en la tabla PysSuscripcion el registro
				PysSuscripcionAdm suscripcionAdm = new PysSuscripcionAdm (this.usrbean);
				PysSuscripcionBean suscripcionBean = new PysSuscripcionBean ();
				suscripcionBean.setCantidad(servicioBean.getCantidad());
				suscripcionBean.setFechaSuscripcion("sysdate");
				suscripcionBean.setIdFormaPago(servicioBean.getIdFormaPago());
				suscripcionBean.setIdInstitucion(servicioBean.getIdInstitucion());
				suscripcionBean.setIdPeticion(servicioBean.getIdPeticion());
				suscripcionBean.setIdPersona(servicioBean.getIdPersona());
				suscripcionBean.setIdServicio(servicioBean.getIdServicio());
				suscripcionBean.setIdServicioInstitucion(servicioBean.getIdServicioInstitucion());
				suscripcionBean.setIdTipoServicios(servicioBean.getIdTipoServicios());
				suscripcionBean.setIdCuenta(servicioBean.getIdCuenta());
				suscripcionBean.setIdSuscripcion(suscripcionAdm.getNuevoID(suscripcionBean));
				
				//suscripcionBean.setImporteUnitario(servicioBean.get)

				// RGG 29-04-2005 cambio para insertar la descripcion
				// buscamos la descripcion
				PysServiciosInstitucionAdm pyssiAdm = new PysServiciosInstitucionAdm(this.usrbean);
				Hashtable claves2 = new Hashtable();
				claves2.put(PysServiciosInstitucionBean.C_IDINSTITUCION,servicioBean.getIdInstitucion());
				claves2.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,servicioBean.getIdTipoServicios());
				claves2.put(PysServiciosInstitucionBean.C_IDSERVICIO,servicioBean.getIdServicio());
				claves2.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,servicioBean.getIdServicioInstitucion());
				Vector vpi = pyssiAdm.selectByPK(claves2);
				String descripcion = "";
				Float iva = new Float(0);
				if (vpi!=null && vpi.size()>0) {
					PysServiciosInstitucionBean b = (PysServiciosInstitucionBean) vpi.get(0);
					descripcion = b.getDescripcion();
					iva = b.getPorcentajeIva();
				}
				suscripcionBean.setDescripcion(descripcion);
				
				//LMS 29/08/2006
				//Se copia tal cual está en Productos, salvo el pago con tarjeta.
				if (servicioBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA) {
					//En el caso de pago por tarjeta no se hace nada, pues ya se calcula en otro sitio.
					//JASU 04/06/2009 pues ahora hay que volver a calcular esto aquí para poder mostrar 
					//el importeanticipado en la ventana final del proceso de compra.

					Vector precioAux = getPrecioServicio(idInstitucion.toString(), idTipoServicio.toString(), idServicio.toString(), idServicioInstitucion.toString(), servicioBean.getIdPersona().toString(), usrbean.getLocation());
					Double precio = new Double(0);
					if (precioAux != null && precioAux.size()>0){
						String aux = (String)((Hashtable) precioAux.get(0)).get("PRECIO_SERVICIO");
						if (aux != null && !"-1".equals(precioAux)){
							precio = new Double(aux.split("#")[0]);
						}
					}
					double importeAnticipadoTarjeta = new Double(""+servicioBean.getCantidad()).doubleValue() * (precio.doubleValue() + ( (precio.doubleValue()*iva.floatValue()) / 100 ));
					suscripcionBean.setImporteAnticipado(new Double(importeAnticipadoTarjeta));	
					
					//suscripcionBean.setD
				}
				else {
					if (servicioBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_METALICO)
						suscripcionBean.setImporteAnticipado(importeAnticipado);
					else
						suscripcionBean.setImporteAnticipado(new Double(0));
				}

				if(fechaEfectiva.equals("0")){
					suscripcionBean.setFechaSuscripcion("sysdate");
					
				}
				else{
					String fecha=GstDate.getApplicationFormatDate("EN",fechaEfectiva);
					suscripcionBean.setFechaSuscripcion(fecha);
				}	
				
				if (!suscripcionAdm.insert(suscripcionBean)) {
					return false;
				}
				
				// 3. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes(idInstitucion, idPeticion);
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				// 3.1 no hay articulos pendientes, cambiamos el estado a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				return ppcsa.update(peticionBean);
			}
			
			// Peticion de BAJA
			else {
				//mhg - Se quita la actualizacion de la fecha solicitud.
				if(!fechaEfectiva.equals("0")){
					fechaEfectiva=GstDate.getApplicationFormatDate("ES",fechaEfectiva);
				}
				// 1. Cambiamos el estado de peticion de baja a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				//peticionBean.setFecha(GstDate.getApplicationFormatDate("EN",fechaEfectiva));
				if (!ppcsa.update(peticionBean)) {
					return false;
				}
				
				// 2. Cambiamos el estado de servicio de peticion de baja a PRODUCTO_BAJA
				claves.clear();
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				PysServiciosSolicitadosAdm servicioAdm = new PysServiciosSolicitadosAdm(this.usrbean);
				PysServiciosSolicitadosBean servicioBean = (PysServiciosSolicitadosBean) servicioAdm.select(claves).get(0);
				servicioBean.setAceptado(ClsConstants.PRODUCTO_BAJA);
				
				if (!servicioAdm.update(servicioBean)) {
					return false;
				}
				
				// 3. Cambiamos el estado de servicio de peticion de alta a PRODUCTO_BAJA
				claves.clear();
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, peticionBean.getIdPeticionAlta());
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				servicioBean = (PysServiciosSolicitadosBean) servicioAdm.select(claves).get(0);
				servicioBean.setAceptado(ClsConstants.PRODUCTO_BAJA);
				if (!servicioAdm.update(servicioBean)) {
					return false;
				}

				// 4. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes(idInstitucion, peticionBean.getIdPeticionAlta());
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				// 4.1 No hay articulos pendientes, cambiamos el estado a PROCESADA
				claves.clear();
				UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, peticionBean.getIdPeticionAlta());
				Long idPeticionAlta=peticionBean.getIdPeticionAlta();
				peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				//peticionBean.setFecha(GstDate.getApplicationFormatDate("EN",fechaEfectiva));
				
				if (!ppcsa.update(peticionBean)) {
				    return false;
				}
				
				PysSuscripcionAdm suscripcionAdm = new PysSuscripcionAdm(this.usrbean);
				
				claves.clear();
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDINSTITUCION, idInstitucion);
				if(!fechaEfectiva.equals("0")){
				  UtilidadesHash.set(claves, PysSuscripcionBean.C_IDPETICION, peticionBean.getIdPeticion());
				}else{
				  UtilidadesHash.set(claves, PysSuscripcionBean.C_IDPETICION, idPeticionAlta);	
				}
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				
				Vector v = suscripcionAdm.select(claves);
				if (v!=null && v.size()>0) {
				    PysSuscripcionBean suscripcionBean = (PysSuscripcionBean) v.get(0);
				    if (!fechaEfectiva.equals("0")){// Solo comparamos la fecha efectiva con la de suscripcion cuando
				    	                            // la fecha efectiva se ha introducido por pantalla.
					    PysServiciosAdm psa = new PysServiciosAdm(this.usrbean);
					    // jbd Usamos obtenerFechaMayorServicioFacturadoPersona en vez de obtenerFechaMayorServicioFacturado
					    //     para que no deniegue la fecha de baja a la persona si no esta en las facturaciones del servicio
					    String sFechaFacMayor = psa.obtenerFechaMayorServicioFacturadoPersona(
					    		idInstitucion.toString(), idTipoServicio.toString(), idServicio.toString(), idServicioInstitucion.toString(), suscripcionBean.getIdPersona().toString() );
					    SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
					    if (sFechaFacMayor!=null && !sFechaFacMayor.equals("")){
						    Date dFechaFacMayor = formato.parse(sFechaFacMayor);
						    Date dFecha = formato.parse(fechaEfectiva);
						    if (dFecha.compareTo(dFechaFacMayor)<=0) {
						    	// la fecha de baja es menor o igual que la fecha de facturacion mayor
						    	formato.applyPattern("dd/MM/yyyy");
						    	String [] datos = {formato.format(dFechaFacMayor)};
						    	throw new SIGAException(UtilidadesString.getMensaje( "messages.Servicios.GestionSolicitudes.FechaEfectivaMenorFacturacion", datos, this.usrbean.getLanguage()));
						    }
					    }
					    else{
					        // Si no existen facturaciones se comprueba la fecha de suscripcion
						     Date dFechaSus = new Date(suscripcionBean.getFechaSuscripcion());
						     String fechaBaja=fechaEfectiva;
						     Date dFechaBaj = new Date(fechaBaja);
						     if (dFechaSus.compareTo(dFechaBaj)>0) {
						        // la fecha de baja es menor que la fecha de suscripcion
						        throw new SIGAException("messages.Servicios.GestionSolicitudes.FechaEfectivaMal");
						     }
					    }
				    }
				    if(fechaEfectiva.equals("0")){
				    	suscripcionBean.setFechaBaja("sysdate");
						
					}
					else{
						suscripcionBean.setFechaBaja(fechaEfectiva);
					}	
				    
				    if (!suscripcionAdm.updateDirect(suscripcionBean)) {
					    return false;
				    }
				}
				
				return true;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e,"Error al confirmar la solicitud del servicio.");
	   			}
	   		}	
	    }
	}
	
	
	/**
	 * denegarServicio
	 * Deniega la solicitud (de alta o de baja) de un servicio.
	 * @param idInstitucion
	 * @param idPeticion
	 * @param idTipoServicio
	 * @param idServicio
	 * @param idServicioInstitucion
	 * @param mensaje mesaje de salida que se mostrara
	 * @return
	 * @throws SIGAException, ClsExceptions
	 */
	public boolean denegarServicio (Integer idInstitucion, Long idPeticion, Integer idTipoServicio, Long idServicio, Long idServicioInstitucion, String []mensaje) throws SIGAException, ClsExceptions {

		try {
			PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			PysPeticionCompraSuscripcionBean peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);

			// Peticion de ALTA
			if (peticionBean.getTipoPeticion().equals(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {
				// 1. Marcamos el servicio como DENEGADO
				claves.clear();
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				PysServiciosSolicitadosAdm servicioAdm = new PysServiciosSolicitadosAdm(this.usrbean);
				PysServiciosSolicitadosBean servicioBean = (PysServiciosSolicitadosBean) servicioAdm.select(claves).get(0);
				servicioBean.setAceptado(ClsConstants.PRODUCTO_DENEGADO);
				if (!servicioAdm.update(servicioBean)) {
					return false;
				}

				// 2 Si la forma de pado es TARJETA -> mensaje especial, sino OK
				if (servicioBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA) {
					mensaje[0] = new String("messages.pys.gestionSolicitudes.abono");					
				}
				else {
					mensaje[0] = new String("messages.updated.success");
				}
				
				// 3. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes(idInstitucion, idPeticion);
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				// 3.1 no hay articulos pendientes, cambiamos el estado a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				if(!ppcsa.update(peticionBean)) {
					return false;
				}
				return true;
			}

			// Peticion de BAJA
			else {
				// 1. Cambiamos el estado de peticion de baja a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				if (!ppcsa.update(peticionBean)) {
					return false;
				}

				// 2. Cambiamos el estado de servicio de peticion de baja a PRODUCTO_DENEGADO
				claves.clear();
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS, idTipoServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIO, idServicio);
				UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				PysServiciosSolicitadosAdm servicioAdm = new PysServiciosSolicitadosAdm(this.usrbean);
				PysServiciosSolicitadosBean servicioBean = (PysServiciosSolicitadosBean) servicioAdm.select(claves).get(0);
				servicioBean.setAceptado(ClsConstants.PRODUCTO_DENEGADO);
				return servicioAdm.update(servicioBean);
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e,"Error al denegar la solicitud del servicio.");
	   			}
	   		}	
	    }
	}
	
	/**
	 * Actualiza los datos de un C.V. y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param BeanCV datos del CV.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (PysServiciosSolicitadosBean beanFact, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanFact), beanFact.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanFact, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e,"Error al insertar datos en B.D.");
	   			}
	   		}	
	    }
	}
	
		
	/**
	 * Inserta el servicio del carrito en la tabla Pys_ServiciosSolicitados	 
	 * @param a - Servicio que vamos a insertar 
	 * @param idPeticion - identificador de la petición de compra a la que está asociado  
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @return  Hashtable  contiene todos los datos sobre el servicio que se ha insertado
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Hashtable insertServicio(Articulo a, Long idPeticion, Long idPersona) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		PysServiciosSolicitadosAdm adm = new PysServiciosSolicitadosAdm(this.usrbean);
		
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION , a.getIdInstitucion());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS , a.getIdTipo());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIO , a.getIdArticulo());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, a.getIdArticuloInstitucion());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERSONA, idPersona);
		if (a.getIdFormaPago()!=null && a.getIdFormaPago().intValue()==ClsConstants.TIPO_FORMAPAGO_METALICO){
			   
			    UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDCUENTA, "");
			}else{
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDCUENTA, a.getIdCuenta());
			}
		
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO, a.getIdFormaPago());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_CANTIDAD , new Integer(a.getCantidad()));
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_ACEPTADO , ClsConstants.PRODUCTO_PENDIENTE);
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERIODICIDAD , a.getIdPeriodicidad());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS , a.getIdPrecios());
		UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_ORDEN, a.getOrden());
		
		try {
			if(adm.insert(hash)){
				UtilidadesHash.set(hash, "DESCRIPCION_ARTICULO", a.getIdArticuloInstitucionDescripcion());	
				UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", a.getFormaPago());	
				UtilidadesHash.set(hash, "DESCRIPCION_CUENTA", a.getNumeroCuenta());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS, a.getPrecio());
				UtilidadesHash.set(hash, "IVA" , a.getIdIva());		
				UtilidadesHash.set(hash, "PERIODICIDAD" , a.getPeriodicidad());
				UtilidadesHash.set(hash, "SERVICIO_DESCRIPCION_PRECIO" , a.getDescripcionPrecio());
			} else {
				return null;
			}			
		} catch (Exception e) { 
				throw new ClsExceptions("Error al insertar Servicios");
			   //throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null);
		}
		return hash;
	}

	/**
	 * Obtiene si hay una peticion de baja.
	 * @param idinstitucion
	 * @param idpeticion
	 * @return boolean
	 */
	public boolean getTipoPeticion(Integer idinstitucion, Long idpersona, String idTipoServicios, String idServicio, String idServiciosInstitucion, String idPeticion){
		boolean hay = false;

		try {
			String where = " SELECT 1 "+
						   " FROM "+
						   PysPeticionCompraSuscripcionBean.T_NOMBRETABLA+" pet,"+
						   PysServiciosSolicitadosBean.T_NOMBRETABLA+" serv"+
						   " WHERE " +
						   " pet."+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+"="+idinstitucion.toString()+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDPERSONA+"="+idpersona.toString()+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_TIPOPETICION+"='"+ClsConstants.TIPO_PETICION_COMPRA_BAJA+"'"+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION+"="+ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA+"="+idPeticion+
						   //SERVICIO:
						   " AND serv."+PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS+"="+idTipoServicios+
						   " AND serv."+PysServiciosSolicitadosBean.C_IDSERVICIO+"="+idServicio+
						   " AND serv."+PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION+"="+idServiciosInstitucion+
						   //JOIN:
						   " AND serv."+PysServiciosSolicitadosBean.C_IDINSTITUCION+"=pet."+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+
						   " AND serv."+PysServiciosSolicitadosBean.C_IDPETICION+"=pet."+PysPeticionCompraSuscripcionBean.C_IDPETICION+
						   " AND serv."+PysServiciosSolicitadosBean.C_IDPERSONA+"=pet."+PysPeticionCompraSuscripcionBean.C_IDPERSONA;
			Vector v = this.selectGenerico(where);
			if (!v.isEmpty())
				hay = true;
		} catch(Exception e){
			hay = false;
		}
		return hay;
	}
	
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en PysServiciosSolicitadosAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

	public Vector obtenerServiciosSolicitados(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
		Vector salida = new Vector();
		try {
			salida = this.select("where idinstitucion="+beanPeticion.getIdInstitucion().toString()+ " and idpeticion="+beanPeticion.getIdPeticion().toString());
		} catch(Exception e){
			throw new ClsExceptions(e, "Error al obtener servicios solicitados.");
		}
		return salida;
	}		

	public PaginadorBind getServiciosSolicitadosPaginador (Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			String select = getQueryServiciosSolicitadosBind(datos, idInstitucion);
			paginador = new PaginadorBind(select, new Hashtable());


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getServiciosSolicitadosPaginador.");
		}
		
		return paginador;  
	}
	
	/**
	 * 
	 * @param datos
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private String getQueryServiciosSolicitadosBind(Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {

		Long idPeticion = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDPETICION);
		Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
		String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);		
		String distintoCampoAceptado = UtilidadesHash.getString(datos, PysServiciosSolicitadosBean.C_ACEPTADO);
		Integer idTipoServicios = UtilidadesHash.getInteger(datos, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);		 
		Long idServicio = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDSERVICIO);						 
		Long idServiciosInstitucion = UtilidadesHash.getLong(datos, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
		String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE"); // JPT: Segun la incidencia INC-6529 estos campos para servicios no deberian estar 
		String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA"); // JPT: Segun la incidencia INC-6529 estos campos para servicios no deberian estar 
		
		String sql = "SELECT 'SERVICIO' AS CONSULTA, " + 
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + ", " + 
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDCUENTA + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_CANTIDAD + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDFORMAPAGO + ", " +
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHASUSCRIPCION + ", " + 
						PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHABAJA + ", " +
						
						// PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + ", " +  => JPT: No lo utiliza la jsp
						PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ", " +
						
						" CASE " +
							" WHEN TRUNC(NVL(" + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHASUSCRIPCION + ", SYSDATE)) > TRUNC(SYSDATE) THEN 'A' " + 
							" ELSE " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_ACEPTADO + 
						" END AS ACEPTADO, " +
						
						" ( " +
							" SELECT " + UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()) +
							" FROM " + PysFormaPagoBean.T_NOMBRETABLA + 
							" WHERE " + PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_IDFORMAPAGO  +  " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDFORMAPAGO +
						" ) AS FORMAPAGO, " + 	
						
						" ( " + 
							" SELECT " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION +
							" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA +
							" WHERE " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION +
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS +
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + 
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION +
						" ) AS CONCEPTO, "  +
								
						/* => JPT: No lo utiliza la jsp
						" ( " +
							" SELECT " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_SOLICITARBAJA +
							" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA + 
							" WHERE " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + 
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS +
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + 
								" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION +
						" ) AS SOLICITARBAJA, " +*/
								
						" ( " +
							"SELECT 1 " + 
							" FROM " + FacFacturacionSuscripcionBean.T_NOMBRETABLA +
							" WHERE " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + 
								" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + 
								" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSERVICIO + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + 
								" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + 
								" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_IDSUSCRIPCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSUSCRIPCION + 
								" AND " + FacFacturacionSuscripcionBean.T_NOMBRETABLA + "." + FacFacturacionSuscripcionBean.C_NUMEROLINEA + " = 1 " + 
								" AND ROWNUM < 2 " +
						" ) AS ESTAFACTURADO " + 
								
						/* => JPT: No lo utiliza la jsp
						" NVL( " + 
							" ( " +
								" SELECT " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
								" WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + 
									" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPERSONA +
									" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDCUENTA +
							" ) " +
						" ,'-') AS NCUENTA " +*/ 
							
					" FROM " + PysServiciosSolicitadosBean.T_NOMBRETABLA + ", " + 
						PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + ", " + 
						PysSuscripcionBean.T_NOMBRETABLA +
					" WHERE " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS +
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + 
						" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION;
		
		if (idPeticion != null) {
			sql += " AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPETICION + " = " + idPeticion;
		}
		
		if (idPersona != null) {
			sql += " AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPERSONA + " = " + idPersona;
		}
		
		if (tipoPeticion != null) {
			sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = '" + tipoPeticion + "' ";
		}

		if (idTipoServicios != null) {																																	
			sql += " AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDTIPOSERVICIOS + " = " + idTipoServicios;
		}																																								
		
		if (idServicio != null) {																																		
			sql += " AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIO + " = " + idServicio;
		}																																								
		
		if (idServiciosInstitucion != null) {																															
			sql += " AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDSERVICIOSINSTITUCION + " = " + idServiciosInstitucion;
		}																																								
		
		if (fechaDesde != null) { // JPT: Segun la incidencia INC-6529 estos campos para servicios no deberian estar 
			if (fechaDesde.indexOf("SYSDATE")!=-1) {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + fechaDesde;
				
			} else {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + " TO_DATE('" + fechaDesde + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
			}
		}
		
		if (fechaHasta != null) { // JPT: Segun la incidencia INC-6529 estos campos para servicios no deberian estar 
			if (fechaHasta.indexOf("SYSDATE")!=-1) {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + fechaHasta;
				
			} else {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + " TO_DATE('" + fechaHasta + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
			}
		}		
		
		if (distintoCampoAceptado != null) {
			sql += " AND (" + 
						PysServiciosSolicitadosBean.C_ACEPTADO + " <> '" + distintoCampoAceptado + "' " + 
						" OR ( " + PysServiciosSolicitadosBean.C_ACEPTADO + " = '" + distintoCampoAceptado + "' AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHASUSCRIPCION + " > SYSDATE ) " + 
						" OR ( " + PysServiciosSolicitadosBean.C_ACEPTADO + " = '" + distintoCampoAceptado + "' AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHABAJA + " > SYSDATE ) " + 
					" ) ";
		}
		
		sql += " ORDER BY " + PysPeticionCompraSuscripcionBean.C_FECHA + " DESC ";
				 
		return sql.toString();
	}
	
	
	/**
	 * getEstadoCompraProducto Este metodo es parte de la consulta de productos que relentiza la query.
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idPeticionConsulta
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getPrecioServicio(String idInstitucion, String idTipoServicio, String idServicio, String idServicioInstitucion, String idPersona, String idioma) throws ClsExceptions {
		try {
			String sql = " SELECT F_SIGA_CALCULOPRECIOSERVICIO(" + idInstitucion + ", " + idTipoServicio + ", " + idServicio + ", " + idServicioInstitucion + ", " + idPersona + ", " + idioma + ") AS PRECIO_SERVICIO FROM DUAL";
				
			return this.selectGenerico(sql);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getPrecioServicio");
		}
	}
	
	/**
	 * Este metodo devuelve el estado de la suscripcion
	 * @param idInstitucion
	 * @param idTipoServicio
	 * @param idServicio
	 * @param idServiciosInstitucion
	 * @param idPeticionConsulta
	 * @return
	 * @throws ClsExceptions
	 */
	public String getEstadoSuscripcion(String idInstitucion, String idTipoServicio, String idServicio, String idServiciosInstitucion, String idPeticionConsulta) throws ClsExceptions {
		String estadoSuscripcion = "";
		try {
			String sql = " SELECT F_SIGA_ESTADOSUSCRIPCION(" + idInstitucion + ", " + idPeticionConsulta + ", " + idServicio + ", " + idTipoServicio + ", " + idServiciosInstitucion + ") AS ESTADOPAGO FROM DUAL";
			
			Vector vEstados = this.selectGenerico(sql);
			if (vEstados!=null && vEstados.size()>0) {
				String estadoPago = (String) ((Hashtable) vEstados.get(0)).get("ESTADOPAGO");
				estadoSuscripcion = UtilidadesString.getMensajeIdioma(this.usrbean, estadoPago); 
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getEstadoSuscripcion");
		}	
		
		return estadoSuscripcion;
	}
	
}