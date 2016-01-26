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
import com.siga.Utilidades.UtilidadesNumero;
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
	 * 
	 * JPT - Accesos:
	 * 1. PysPeticionCompraSuscripcionAdm.getPeticionDetalle()
	 * - datos => PysPeticionCompraSuscripcionBean.C_IDPETICION	
	 * 
	 * 2. DatosFacturacionAction.abrirServicios()
	 * - datos => PysServiciosSolicitadosBean.C_IDPERSONA
	 * - datos => PysPeticionCompraSuscripcionBean.C_TIPOPETICION
	 * - datos => PysServiciosSolicitadosBean.C_ACEPTADO
	 * 
	 * 3. SolicitudesModificacionEspecificasAction.ver()
	 * - datos => PysServiciosSolicitadosBean.C_IDINSTITUCION
	 * - datos => PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS
	 * - datos => PysServiciosSolicitadosBean.C_IDSERVICIO
	 * - datos => PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION
	 * - datos => PysServiciosSolicitadosBean.C_IDPETICION
	 * 	 
	 * 4. SolicitudBajaAction.getProductosServicios()
	 * - datos => PysPeticionCompraSuscripcionBean.C_IDPERSONA
	 * - datos => PysPeticionCompraSuscripcionBean.C_TIPOPETICION
	 * - datos => "ES_SOLICITUD_BAJA"
	 * 
	 * @param Hashtable con los datos necesarios para la consulta
	 * @param idInstitucion
	 * 
	 * @return vector con todos los registros recuperados
	 * @throws SIGAException, ClsExceptions
	 */
	public Vector getServiciosSolicitados (Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {

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
			
			StringBuilder consulta = new StringBuilder();
			consulta.append("SELECT 'SERVICIO' AS CONSULTA, "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDCUENTA);
			consulta.append(", "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_CANTIDAD);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
			consulta.append(", ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDFORMAPAGO);
			consulta.append(", ");									
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			consulta.append(", ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
			consulta.append(", ");
			
			if (isSolicitudBaja) {
				consulta.append("( SELECT COUNT(*) ");
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PCSB, ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" SSB ");
				consulta.append(" WHERE PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = '");
				consulta.append(ClsConstants.TIPO_PETICION_COMPRA_BAJA);
				consulta.append("' AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" = ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND SSB.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(") AS ESTADO_BAJA, ");
				
			} else {
				consulta.append(" CASE WHEN (");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = 'B') THEN ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" ELSE ( ");
				consulta.append(" SELECT PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION); 
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PC, ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" PS "); 
				consulta.append(" WHERE PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION); 
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);                                   
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" ) ");
				consulta.append(" END AS IDPETICIONRELACIONADA, ");
				
				consulta.append(" F_SIGA_COMPROBAR_ANTICIPAR( "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(", "); 
				consulta.append("'S', "); 
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(", ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(", ");
				consulta.append(" F_SIGA_CALCULOPRECIOSERVICIO( "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(", "); 
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(", "); 
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(", ");
				consulta.append(this.usrbean.getLanguageInstitucion()); 
				consulta.append(" ) ");
				consulta.append(" ) AS ANTICIPAR, "); 		
				
				// Fecha Efectiva:									
				consulta.append(" NVL( ");
				consulta.append(" ( ");
				consulta.append(" SELECT MIN(");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_FECHASUSCRIPCION);
				consulta.append(")"); 
				consulta.append(" FROM ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);										  
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" ), NVL( "); 
				consulta.append(" ( ");
				consulta.append(" SELECT MAX(");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_FECHABAJAFACTURACION);
				consulta.append(")");
				consulta.append(" FROM ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);										  
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" ), ( ");
				consulta.append(" SELECT PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA); 
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PC, ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" PS ");
				consulta.append(" WHERE PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION); 
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" <> ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE);
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = 'B' ");
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" IS NOT NULL ");
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
				consulta.append(" = 'B' ");	
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA);
				consulta.append(" ) ");
				consulta.append(" ) ");
				consulta.append(" ) AS FECHAEFEC, ");		
				
				// Esta facturado
				consulta.append(" ( ");
				consulta.append("SELECT 1 "); 
				consulta.append(" FROM ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(", "); 
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDINSTITUCION); 
				consulta.append(" AND ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS); 
				consulta.append(" AND ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIO); 
				consulta.append(" AND ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION); 
				consulta.append(" AND ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_IDSUSCRIPCION);
				consulta.append(" = ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDSUSCRIPCION); 
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION); 
				consulta.append(" AND ");
				consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysSuscripcionBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION); 
				consulta.append(" AND ");
				consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(FacFacturacionSuscripcionBean.C_NUMEROLINEA);
				consulta.append(" = 1 "); 
				consulta.append(" AND ROWNUM < 2) AS ESTAFACTURADO, ");		
			}
									
			consulta.append(" F_SIGA_CALCULOPRECIOSERVICIO( "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(", "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
			consulta.append(", "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
			consulta.append(", "); 
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(", "); 
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
			consulta.append(", ");
			consulta.append(this.usrbean.getLanguageInstitucion());
			consulta.append(") AS PRECIO_SERVICIO, ");
									
			// Forma Pago
			consulta.append(" ( ");
			consulta.append("SELECT ");
			consulta.append(UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()));
			consulta.append(" FROM ");
			consulta.append(PysFormaPagoBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(PysFormaPagoBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysFormaPagoBean.C_IDFORMAPAGO);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDFORMAPAGO);
			consulta.append(" ) AS FORMAPAGO, ");
									
			// Concepto
			consulta.append(" ( ");
			consulta.append("SELECT ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_DESCRIPCION); 
			consulta.append(" FROM ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDSERVICIO);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(" ) AS CONCEPTO, ");
									
			// Solicitar Baja
			consulta.append(" ( ");
			consulta.append("SELECT ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_SOLICITARBAJA); 
			consulta.append(" FROM ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDSERVICIO);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
			consulta.append(" AND ");
			consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
			consulta.append(" ) AS SOLICITARBAJA, ");

			// Cuenta
			consulta.append(" NVL( ");
			consulta.append(" ( ");
			consulta.append(" SELECT ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IBAN);
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDCUENTA);
			consulta.append(" AND ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDPERSONA); 
			consulta.append(" ) "); 
			consulta.append(" ,'-') AS NCUENTA ");
			
			consulta.append(" FROM ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(", ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			
			consulta.append(" WHERE ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
			consulta.append(" = ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION); 
			consulta.append(" AND ");
			consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);	
			
			if (idPeticion != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(idPeticion);
			}
			
			if (idPersona != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(idPersona);
			}
			
			if (tipoPeticion != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = '");
				consulta.append(tipoPeticion);
				consulta.append("' ");
			}
			
			if (distintoCampoAceptado != null) {
				consulta.append(" AND ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
				consulta.append(" <> '");
				consulta.append(distintoCampoAceptado);
				consulta.append("' ");
			}
		
			// MODIFICADO POR MAV PARA INCLUIR NUEVOS FILTROS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (idTipoServicios != null) {
				consulta.append(" AND ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
				consulta.append(" = ");
				consulta.append(idTipoServicios);
			}			

			if (idServicio != null) {
				consulta.append(" AND ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO);
				consulta.append(" = ");
				consulta.append(idServicio);
			}
			
			if (idServiciosInstitucion != null) {
				consulta.append(" AND ");
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
				consulta.append(" = ");
				consulta.append(idServiciosInstitucion);
			}
			// FIN MODIFICACION //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			// Fechas
			if (fechaDesde != null) {
				if (fechaDesde.indexOf("SYSDATE")!=-1) {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" >= ");
					consulta.append(fechaDesde);
				} else {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" >= TO_DATE('");
					consulta.append(fechaDesde);
					consulta.append("', '");
					consulta.append(ClsConstants.DATE_FORMAT_SQL);
					consulta.append("') ");
				}
			}
			
			if (fechaHasta != null) {
				if (fechaHasta.indexOf("SYSDATE")!=-1) {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" <= ");
					consulta.append(fechaHasta);
				} else {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" <= TO_DATE('");
					consulta.append(fechaHasta);
					consulta.append("', '");
					consulta.append(ClsConstants.DATE_FORMAT_SQL);
					consulta.append("') ");
				}
			}
			
			if (isSolicitudBaja){
				consulta.append(" AND "); // Debe tener la peticion de suscripcion procesada (como tiene tipoPeticion ya comprueba que es una peticion de alta)
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" = ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA);
				consulta.append(" AND "); // No debe ser una solicitud denegada o de baja
				consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
				consulta.append(" NOT IN ('");
				consulta.append(ClsConstants.PRODUCTO_DENEGADO);
				consulta.append("',' ");
				consulta.append(ClsConstants.PRODUCTO_BAJA);
				consulta.append("')");
			}
			
			consulta.append(" ORDER BY ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
			consulta.append(" DESC ");
			
			RowsContainer rc = new RowsContainer();
			if (rc.query(consulta.toString())) {
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					
					Hashtable a = (Hashtable)((Row) rc.get(i)).getRow();
					
					String idTipoServicio = (String)a.get(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
					String idServicios = (String)a.get(PysServiciosSolicitadosBean.C_IDSERVICIO);
					String idServicioInstitucion = (String)a.get(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
					String idPeticionConsulta = (String)a.get(PysServiciosSolicitadosBean.C_IDPETICION);
					
					String estadoPago = this.getEstadoSuscripcion(idInstitucion.toString(), idTipoServicio, idServicios, idServicioInstitucion, idPeticionConsulta);
					UtilidadesHash.set(a, "ESTADOPAGO", estadoPago);					
					
					// Tratamos los datos de la funcion 'F_SIGA_CALCULOPRECIOSERVICIO'
					String valor = UtilidadesHash.getString(a, "PRECIO_SERVICIO");

					// "-1" --> Error no existen datos en la tabla Pys_ServicioInstitucion
					if (!valor.equalsIgnoreCase("-1")){
						
						String datosPrecio[] =  UtilidadesString.splitIgual(valor, "#");
						
						// RGG cambio para 10g
						String diezg = datosPrecio[0];
						diezg = diezg.replaceAll(",",".");
						
						UtilidadesHash.set(a, "VALOR", new Double(diezg));
				
						// RGG cambio para 10g
						String diezgg = datosPrecio[1];
						diezgg = diezgg.replaceAll(",",".");
						
						UtilidadesHash.set(a, "VALORIVA", new Float(diezgg));
						
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
					resultados.add(a);
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
					iva = b.getIdTipoIva();
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
					double importeAnticipadoTarjeta = UtilidadesNumero.redondea(servicioBean.getCantidad().doubleValue() * precio.doubleValue() * (1 + (iva.floatValue() / 100)),2);
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
				    
				    if (fechaEfectiva.equals("0")) {
				    	suscripcionBean.setFechaBaja("sysdate");
				    	suscripcionBean.setFechaBajaFacturacion("sysdate");						
					} else {
						suscripcionBean.setFechaBaja(fechaEfectiva);
						suscripcionBean.setFechaBajaFacturacion(fechaEfectiva);
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
				UtilidadesHash.set(hash, "IVA" , a.getIdTipoIva());		
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

		Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
		String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);		
		String distintoCampoAceptado = UtilidadesHash.getString(datos, PysServiciosSolicitadosBean.C_ACEPTADO);
		
		StringBuilder consulta = new StringBuilder();  
		consulta.append("SELECT 'SERVICIO' AS CONSULTA, "); 
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDPETICION);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIO);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(", "); 
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDCUENTA);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_CANTIDAD);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDFORMAPAGO);
		consulta.append(", ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_FECHASUSCRIPCION);
		consulta.append(", "); 
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_FECHABAJAFACTURACION);
		consulta.append(", ");						
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		consulta.append(", ");						
		consulta.append(" CASE ");
		consulta.append(" WHEN TRUNC(NVL(");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_FECHASUSCRIPCION);
		consulta.append(", SYSDATE)) > TRUNC(SYSDATE) THEN 'A' "); 
		consulta.append(" ELSE ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO); 
		consulta.append(" END AS ACEPTADO, ");						
		consulta.append(" ( ");
		consulta.append(" SELECT ");
		consulta.append(UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()));
		consulta.append(" FROM ");
		consulta.append(PysFormaPagoBean.T_NOMBRETABLA); 
		consulta.append(" WHERE ");
		consulta.append(PysFormaPagoBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysFormaPagoBean.C_IDFORMAPAGO);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDFORMAPAGO);
		consulta.append(" ) AS FORMAPAGO, "); 							
		consulta.append(" ( ");
		consulta.append(" SELECT ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_DESCRIPCION);
		consulta.append(" FROM ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS);
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDSERVICIO);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIO); 
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" = ");		
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" ) AS CONCEPTO, " );								
		consulta.append(" ( ");
		consulta.append("SELECT TO_CHAR(MAX(");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_FECHAFIN);
		consulta.append("), '");
		consulta.append(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		consulta.append("') ");
		consulta.append(" FROM ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION); 
		consulta.append(" AND ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS); 
		consulta.append(" AND ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_IDSERVICIO);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIO); 
		consulta.append(" AND ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION); 
		consulta.append(" AND ");
		consulta.append(FacFacturacionSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionSuscripcionBean.C_IDSUSCRIPCION);
		consulta.append(" = ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSUSCRIPCION);  
		consulta.append(" ) AS ULTIMAFECHAFACTURADA, ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_AUTOMATICO);  
		consulta.append(" FROM ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(", "); 
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(", "); 
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(", "); 
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDPETICION);
		consulta.append(" = ");
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDPETICION);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDPETICION);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDTIPOSERVICIOS);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIO);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO); 
		consulta.append(" AND ");
		consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDINSTITUCION);
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDSERVICIO);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIO); 
		consulta.append(" AND ");
		consulta.append(PysServiciosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysServiciosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
		
		if (idPersona != null) {
			consulta.append(" AND ");
			consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysSuscripcionBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
		}
		
		if (tipoPeticion != null) {
			consulta.append(" AND ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			consulta.append(" = '");
			consulta.append(tipoPeticion);
			consulta.append("' ");
		}

		if (distintoCampoAceptado != null) {
			consulta.append(" AND (");
			consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
			consulta.append(" <> '");
			consulta.append(distintoCampoAceptado);
			consulta.append("' "); 
			consulta.append(" OR ( ");
			consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
			consulta.append(" = '");
			consulta.append(distintoCampoAceptado);
			consulta.append("' AND ");
			consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysSuscripcionBean.C_FECHASUSCRIPCION);
			consulta.append(" > SYSDATE ) "); 
			consulta.append(" OR ( ");
			consulta.append(PysServiciosSolicitadosBean.C_ACEPTADO);
			consulta.append(" = '");
			consulta.append(distintoCampoAceptado);
			consulta.append("' AND ");
			consulta.append(PysSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysSuscripcionBean.C_FECHABAJAFACTURACION);
			consulta.append(" > SYSDATE ) "); 
			consulta.append(" ) ");
		}
		
		consulta.append(" ORDER BY ");
		consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		consulta.append(" DESC ");
				 
		return consulta.toString();
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