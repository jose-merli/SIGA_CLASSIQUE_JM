//VERSIONES:
//ruben.fernandez Creacion: 21/03/2005 
//

package com.siga.facturacionSJCS.action;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.ScsCabeceraguardias;
import org.redabogacia.sigaservices.app.services.scs.ScsCabeceraGuardiasService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FcsAplicaMovimientosVariosBean;
import com.siga.beans.FcsFactActuacionAsistenciaAdm;
import com.siga.beans.FcsFactActuacionDesignaAdm;
import com.siga.beans.FcsFactApunteAdm;
import com.siga.beans.FcsFactAsistenciaAdm;
import com.siga.beans.FcsMovimientosVariosAdm;
import com.siga.beans.FcsMovimientosVariosBean;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.facturacionSJCS.form.MantenimientoMovimientosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;

import es.satec.businessManager.BusinessManager;



public class MantenimientoMovimientosAction extends MasterAction {
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#executeInternal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
				
			String accion = miForm.getModo();
			
			if (accion!= null && accion.equalsIgnoreCase("buscarPaginador")) {
				request.setAttribute("noFicha","0");
				mapDestino = buscarPor(mapping, miForm, request,response);
			} else if (accion!= null && accion.equalsIgnoreCase("buscarPor")) {
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscarPor(mapping, miForm, request,response);
			} else {
				request.getSession().removeAttribute("DATAPAGINADOR");
				return super.executeInternal(mapping, formulario,request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			
			return mapping.findForward(mapDestino);
			
		} catch (SIGAException es) { 
			throw es;
		} catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
	}

	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// Si vengo desde la ficha colegial
			
			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
				return this.buscarPor(mapping, formulario, request,response);
			}
			
			//Recogemos de sesion el UsrBean
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//mapping normal
		return "inicio";
	}


	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String destino = "error";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		try {
			
			Hashtable resultado = movimAdm.getMovimientoVario(usr.getLocation(),(String)ocultos.get(0),this.getUserBean(request).getLanguage());
			request.getSession().setAttribute("ORIGEN", resultado.get("ASUNTO"));
			request.getSession().setAttribute("resultado",resultado);
			request.getSession().setAttribute("modo","edicion");
			destino = "nuevo";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		return destino;
	}

	/**
	 * Metodo que implementa el modo insertar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String destino = "error";
		boolean ok = true;
		
		//recogemos el formulario
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = (UserTransaction)usr.getTransaction();
		
		//preparamos el nuevo registro
		Hashtable movimientoNew = new Hashtable();
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDINSTITUCION, (String)usr.getLocation());
		//UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDMOVIMIENTO, );
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDPERSONA, (String)miform.getIdPersona());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_DESCRIPCION, (String)miform.getDescripcion());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_MOTIVO, (String)miform.getMotivo());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_FECHAALTA, "sysdate");
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_CANTIDAD, (String)miform.getCantidad());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_FECHAMODIFICACION, "sysdate");
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_USUMODIFICACION, (String)usr.getUserName());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_CONTABILIZADO, "0");
		if(!miform.getIdFacturacion().trim().equals(""))
			UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDFACTURACION, miform.getIdFacturacion());
		if(!miform.getIdGrupoFacturacion().trim().equals(""))
			UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDGRUPOFACTURACION, miform.getIdGrupoFacturacion());
		try {
			//consultamos el valor que falta, el idMovimiento
			FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
			tx.begin();
			movimientoNew = movimientosAdm.prepararInsert(movimientoNew);
			
			//insertar el nuevo registro
			ok = movimientosAdm.insert(movimientoNew);
	
			if (ok) {
				String origen = (String)request.getSession().getAttribute("ORIGEN");
				if(origen != null && !"".equalsIgnoreCase(origen) && "ASUNTO".equalsIgnoreCase(origen)){
					//Insertamos en la tabla scs_actuacionesDesignas el idmovimiento
					ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (this.getUserBean(request));	
					
					Hashtable hashDatosDesigna= new Hashtable();			
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_IDINSTITUCION, miform.getIdInstitucion());
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_ANIO, miform.getAnio());
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_NUMERO, miform.getNumero());
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_IDTURNO, miform.getIdTurno());
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_NUMEROASUNTO,miform.getNactuacion());		
					UtilidadesHash.set(hashDatosDesigna,ScsActuacionDesignaBean.C_IDMOVIMIENTO,Integer.valueOf((String)movimientoNew.get("IDMOVIMIENTO")));		
					
					designaAdm.actualizarActuacionesMovimientosVarios(hashDatosDesigna);
					
				}else if(origen != null && !"".equalsIgnoreCase(origen) && "ACTUACIONESASISTENCIAS".equalsIgnoreCase(origen)){
					
					ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm (this.getUserBean(request));
					
					Hashtable hashDatosActuacionAsistencia= new Hashtable();
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_IDINSTITUCION, miform.getIdInstitucion());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_ANIO, miform.getAnio());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_NUMERO, miform.getNumero());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_IDACTUACION,miform.getNactuacion());	
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_IDMOVIMIENTO,Integer.valueOf((String)movimientoNew.get("IDMOVIMIENTO")));		
					
					actuacionAsistenciaAdm.actualizarActuacionesMovimientosVarios(hashDatosActuacionAsistencia);
					
				}else if(origen != null && !"".equalsIgnoreCase(origen) && "ASISTENCIAS".equalsIgnoreCase(origen)){
					
					ScsAsistenciasAdm asistenciasAdm = new ScsAsistenciasAdm (this.getUserBean(request));
					
					Hashtable hashDatosActuacionAsistencia= new Hashtable();
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_IDINSTITUCION, miform.getIdInstitucion());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_ANIO, miform.getAnio());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_NUMERO, miform.getNumero());
					UtilidadesHash.set(hashDatosActuacionAsistencia,ScsActuacionAsistenciaBean.C_IDMOVIMIENTO,Integer.valueOf((String)movimientoNew.get("IDMOVIMIENTO")));		
					
					asistenciasAdm.actualizarAsistenciaMovimientosVarios(hashDatosActuacionAsistencia);
					
				}else if(origen != null && !"".equalsIgnoreCase(origen) && "GUARDIAS".equalsIgnoreCase(origen)){
					
					
					ScsCabeceraGuardiasAdm scsCabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
					
					scsCabeceraGuardiasAdm.actualizarGuardiasMovimientosVarios(miform.getIdPersona(), miform.getIdTurno(), 
						miform.getIdGuardia(), miform.getIdInstitucion(), miform.getFechaInicio().toString(),Integer.valueOf((String)movimientoNew.get("IDMOVIMIENTO")));	
				}
				tx.commit();
			} else {
				tx.rollback();
			}
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		if (ok) return exitoModal("messages.inserted.success",request);
		else return exitoModal("messages.inserted.error",request);
	}

		
	/**
	 * Metodo que implementa el modo modificar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		//variables
		String destino = "error";
		boolean ok = false;
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm ( this.getUserBean(request));
		
		//recoger formulario
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;

		//recoger el UsrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = (UserTransaction)usr.getTransaction();
		try {
			//recogemos el movimiento que está en BBDD
			String where = " where " + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + usr.getLocation() +
							" and " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +  (String)miform.getIdMovimiento() + " ";
			FcsMovimientosVariosBean movimBd = (FcsMovimientosVariosBean)((Vector)movimAdm.select (where)).get(0);
			
			//lo pasamos a HAshtable
			Hashtable movimOld = (Hashtable)movimBd.getOriginalHash().clone();
			
			//preparamos el nuevo HAshtable
			Hashtable movimNew = (Hashtable)movimOld.clone();
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_CANTIDAD, (String)miform.getCantidad());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_DESCRIPCION, (String)miform.getDescripcion());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_MOTIVO, (String)miform.getMotivo());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_IDPERSONA, (String)miform.getIdPersona());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_USUMODIFICACION, (String)usr.getUserName());
			UtilidadesHash.set( movimNew, FcsAplicaMovimientosVariosBean.C_IDPAGOSJG, (String)miform.getIdPagoJg());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_FECHAALTA, (String)miform.getFechaAlta());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_FECHAMODIFICACION, "sysdate");
			//if(!miform.getIdFacturacion().trim().equals(""))
				UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_IDFACTURACION, miform.getIdFacturacion());
			//if(!miform.getIdGrupoFacturacion().trim().equals(""))
				UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_IDGRUPOFACTURACION, miform.getIdGrupoFacturacion());

			//modificamos
			tx.begin();
			
			//insertar el nuevo registro
			ok = movimAdm.update(movimNew, movimOld);
	
			if (ok) {
				tx.commit();
			} else {
				tx.rollback();
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		if (ok) return exitoModal("messages.updated.success", request);
		else return exitoModalSinRefresco("messages.updated.error", request);

	}


	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String destino = "error";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		
		
		try {
			
			Hashtable resultado = movimAdm.getMovimientoVario(usr.getLocation(),(String)ocultos.get(0),this.getUserBean(request).getLanguage());
			request.getSession().setAttribute("resultado",resultado);
			request.getSession().setAttribute("modo","consulta");
			destino = "nuevo";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		
		
		
		
		
		
		return destino;
	}



	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles
		HttpSession ses = (HttpSession) request.getSession();
		UsrBean usr = this.getUserBean(request);
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm) formulario;
		CenPersonaAdm persAdm = new CenPersonaAdm(usr);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// eliminando el formulario. Si no lo hicieramos, desde MVs obtendriamos el de session, lo cual no es correcto
		request.getSession().removeAttribute("MantenimientoMovimientosForm");
		request.getSession().removeAttribute("ORIGEN");
		request.getSession().setAttribute("modo", "nuevo");

		// si no tenemos origen, que siga normal
		if (miform.getOrigen() == null || "".equalsIgnoreCase(miform.getOrigen())) {
			return "nuevo";
		}

		try {
			if ("ACTUACIONESDESIGNAS".equalsIgnoreCase(miform.getOrigen())) {

				ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm(usr);
				Hashtable hashDatosDesigna = new Hashtable();
				UtilidadesHash.set(hashDatosDesigna, ScsDesignaBean.C_IDINSTITUCION, miform.getIdInstitucion());
				UtilidadesHash.set(hashDatosDesigna, ScsDesignaBean.C_ANIO, miform.getAnio());
				UtilidadesHash.set(hashDatosDesigna, ScsDesignaBean.C_NUMERO, miform.getNumero());
				UtilidadesHash.set(hashDatosDesigna, ScsDesignaBean.C_IDTURNO, miform.getIdTurno());
				UtilidadesHash.set(hashDatosDesigna, "VISIBLE", miform.getNactuacion());
				Vector vAct = designaAdm.getConsultaActuacion(hashDatosDesigna, usr);

				Hashtable hashActuacion = (Hashtable) (vAct).get(0);
				if ((String) hashActuacion.get("IDMOVIMIENTO") != null && !"".equalsIgnoreCase((String) hashActuacion.get("IDMOVIMIENTO"))) {
					// Desde Asunto y ya existe movimiento
					FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm(usr);
					Hashtable resultado = movimAdm.getMovimientoVario((String) hashActuacion.get("IDINSTITUCION"),
							(String) hashActuacion.get("IDMOVIMIENTO"), usr.getLanguage());
					ses.setAttribute("resultado", resultado);
					ses.setAttribute("modo", "edicion");
					ses.setAttribute("ORIGEN", "ASUNTO");

				} else {// Desde Asunto y es nuevo
					FcsFactActuacionDesignaAdm factura = new FcsFactActuacionDesignaAdm(usr);
					ScsTurnoAdm scsTurnoAdm = new ScsTurnoAdm(usr);
					Hashtable infoTurno = scsTurnoAdm.getDatosTurno(miform.getIdInstitucion(), miform.getIdTurno());

					String nombre_dest = persAdm.obtenerNombreApellidos((String) hashActuacion.get("IDPERSONACOLEGIADO"));
					String nif = persAdm.obtenerNIF((String) hashActuacion.get("IDPERSONACOLEGIADO"));
					ses.setAttribute("IDPERSONACOLEGIADO", (String) hashActuacion.get("IDPERSONACOLEGIADO"));
					ses.setAttribute("NOMBRE", nombre_dest);
					ses.setAttribute("NIF", nif);
					ses.setAttribute("NCOLEGIADO", (String) hashActuacion.get("NCOLEGIADO"));
					ses.setAttribute("IDFACTURACION", (String) hashActuacion.get("IDFACTURACION"));
					ses.setAttribute("IDGRUPOFACTURACION", String.valueOf(infoTurno.get("IDGRUPOFACTURACION")));
					ses.setAttribute("ORIGEN", "ASUNTO");
					String descripcionAsunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "movimientosVarios.ActuacionDesigna.titulo") + " " + hashActuacion.get(ScsActuacionDesignaBean.C_ANIO) + "/" + hashActuacion.get("CODIGODESIGNA") + "/" + hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO);
					ses.setAttribute("ASUNTO", descripcionAsunto);
					ses.setAttribute("DESCRIPCION", descripcionAsunto);

					ses.setAttribute("hashActuacion", hashActuacion);

					// Cogemos el importe de la facturación
					String importeFactura = factura.getImporteFacturado(hashActuacion);
					ses.setAttribute("CANTIDAD", formatearImporteFacturadoParaMV(importeFactura));
				}

			} else if ("ACTUACIONESASISTENCIAS".equalsIgnoreCase(miform.getOrigen())) {

				ScsActuacionAsistenciaAdm asistenciaAdm = new ScsActuacionAsistenciaAdm(usr);
				ScsAsistenciasAdm asistenciasAdm = new ScsAsistenciasAdm(usr);
				ActuacionAsistenciaForm actuacionAsistenciaForm = new ActuacionAsistenciaForm();
				actuacionAsistenciaForm.setIdInstitucion(miform.getIdInstitucion());
				actuacionAsistenciaForm.setAnio(miform.getAnio());
				actuacionAsistenciaForm.setNumero(miform.getNumero());
				actuacionAsistenciaForm.setIdActuacion(miform.getNactuacion());
				ScsActuacionAsistenciaBean actuacionBean = asistenciaAdm.getActuacionAsistencia(actuacionAsistenciaForm);

				if (actuacionBean.getIdMovimiento() != null) {
					// Desde AsuntoAsistencias y ya existe movimiento
					FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm(usr);
					Hashtable resultado = movimAdm.getMovimientoVario(String.valueOf(actuacionBean.getIdInstitucion()),
							String.valueOf(actuacionBean.getIdMovimiento()), usr.getLanguage());
					ses.setAttribute("resultado", resultado);
					ses.setAttribute("modo", "edicion");
					ses.setAttribute("ORIGEN", "ACTUACIONESASISTENCIAS");

				} else {// Desde AsuntoAsistencias y es nuevo
					FcsFactActuacionAsistenciaAdm factura = new FcsFactActuacionAsistenciaAdm(usr);
					ScsTurnoAdm scsTurnoAdm = new ScsTurnoAdm(usr);

					// Necesitamos información de la asistencia por ejemplo para el idPersonaColegiado
					ScsAsistenciasBean scsAsistenciasBean = new ScsAsistenciasBean();

					scsAsistenciasBean.setIdInstitucion(Integer.parseInt(miform.getIdInstitucion()));
					scsAsistenciasBean.setAnio(Integer.parseInt(miform.getAnio()));
					scsAsistenciasBean.setNumero(Integer.parseInt(miform.getNumero()));

					// Obtenemos los datos de las asistencia
					AsistenciaForm asistenciaForm = asistenciasAdm.getDatosAsistencia(scsAsistenciasBean);
					Hashtable infoTurno = scsTurnoAdm.getDatosTurno(asistenciaForm.getIdInstitucion(), asistenciaForm.getIdTurno());

					String nombre_dest = persAdm.obtenerNombreApellidos(asistenciaForm.getIdPersonaColegiado());
					String nif = persAdm.obtenerNIF(asistenciaForm.getIdPersonaColegiado());

					ses.setAttribute("IDPERSONACOLEGIADO", asistenciaForm.getIdPersonaColegiado());
					ses.setAttribute("NOMBRE", nombre_dest);
					ses.setAttribute("NIF", nif);
					ses.setAttribute("NCOLEGIADO", asistenciaForm.getPersonaColegiado().getColegiado().getNColegiado());
					ses.setAttribute("IDFACTURACION", String.valueOf(actuacionBean.getIdFacturacion()));
					ses.setAttribute("IDGRUPOFACTURACION", String.valueOf(infoTurno.get("IDGRUPOFACTURACION")));
					ses.setAttribute("ORIGEN", "ACTUACIONESASISTENCIAS");
					String descripcionAsunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "movimientosVarios.ActuacionAsistencias.titulo") + " " + actuacionBean.getAnio() + "/" + actuacionBean.getNumero() + "/" + actuacionBean.getIdActuacion();
					ses.setAttribute("ASUNTO", descripcionAsunto);
					ses.setAttribute("DESCRIPCION", descripcionAsunto);

					// Cogemos el importe de la facturación
					String importeFactura = factura.getImporteTotalFacturado(actuacionBean);
					ses.setAttribute("CANTIDAD", formatearImporteFacturadoParaMV(importeFactura));
					
					Hashtable hashActuacion = new Hashtable();
					hashActuacion.put("IDTURNO", asistenciaForm.getIdTurno());
					hashActuacion.put("IDINSTITUCION", asistenciaForm.getIdInstitucion());
					hashActuacion.put("ANIO", asistenciaForm.getAnio());
					hashActuacion.put("NUMERO", asistenciaForm.getNumero());
					hashActuacion.put("NUMEROASUNTO", miform.getNactuacion());
					ses.setAttribute("hashActuacion", hashActuacion);
				}

			} else if ("ASISTENCIAS".equalsIgnoreCase(miform.getOrigen())) {

				ScsAsistenciasAdm asistenciasAdm = new ScsAsistenciasAdm(usr);

				Hashtable hashAsistencia = asistenciasAdm.getAsistencia(miform.getAnio(), miform.getNumero(), miform.getIdInstitucion());

				if ((String) hashAsistencia.get("IDMOVIMIENTO") != null && !"".equalsIgnoreCase((String) hashAsistencia.get("IDMOVIMIENTO"))) {
					// Desde Asunto y ya existe movimiento
					FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm(usr);
					Hashtable resultado = movimAdm.getMovimientoVario((String) hashAsistencia.get("IDINSTITUCION"),
							(String) hashAsistencia.get("IDMOVIMIENTO"), usr.getLanguage());
					ses.setAttribute("resultado", resultado);
					ses.setAttribute("modo", "edicion");
					ses.setAttribute("ORIGEN", "ASISTENCIAS");

				} else {// Desde Asunto y es nuevo
					FcsFactAsistenciaAdm factura = new FcsFactAsistenciaAdm(usr);
					ScsTurnoAdm scsTurnoAdm = new ScsTurnoAdm(usr);

					String nombre_dest = persAdm.obtenerNombreApellidos((String) hashAsistencia.get("IDPERSONACOLEGIADO"));
					String nif = persAdm.obtenerNIF((String) hashAsistencia.get("IDPERSONACOLEGIADO"));
					ses.setAttribute("IDPERSONACOLEGIADO", (String) hashAsistencia.get("IDPERSONACOLEGIADO"));
					ses.setAttribute("NOMBRE", nombre_dest);
					ses.setAttribute("NIF", nif);
					ses.setAttribute("NCOLEGIADO", (String) hashAsistencia.get("NCOLEGIADO"));
					ses.setAttribute("ORIGEN", "ASISTENCIAS");
					String descripcionAsunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "movimientosVarios.asistencia.titulo") + " " + hashAsistencia.get("ANIO") + "/" + hashAsistencia.get("NUMERO");
					ses.setAttribute("ASUNTO", descripcionAsunto);
					ses.setAttribute("DESCRIPCION", descripcionAsunto);

					Hashtable infoTurno = scsTurnoAdm.getDatosTurno((String) hashAsistencia.get("IDINSTITUCION"), (String) hashAsistencia.get("IDTURNO"));
					ses.setAttribute("IDFACTURACION", String.valueOf((String) hashAsistencia.get("IDFACTURACION")));
					ses.setAttribute("IDGRUPOFACTURACION", String.valueOf(infoTurno.get("IDGRUPOFACTURACION")));

					ses.setAttribute("hashActuacion", hashAsistencia);

					// Cogemos el importe de la facturación
					String importeFactura = factura.getImporteTotalFacturado(hashAsistencia);
					ses.setAttribute("CANTIDAD", formatearImporteFacturadoParaMV(importeFactura));
				}

			} else if ("GUARDIAS".equalsIgnoreCase(miform.getOrigen())) {

				ScsCabeceraGuardiasService scsCabeceraGuardias = (ScsCabeceraGuardiasService) BusinessManager.getInstance().getService(
						ScsCabeceraGuardiasService.class);

				ScsCabeceraguardias scsCabeceraGuadiasBean = new ScsCabeceraguardias();

				scsCabeceraGuadiasBean.setIdinstitucion(Short.valueOf(miform.getIdInstitucion()));
				scsCabeceraGuadiasBean.setIdturno(Integer.valueOf(miform.getIdTurno()));
				scsCabeceraGuadiasBean.setIdguardia(Integer.valueOf(miform.getIdGuardia()));
				scsCabeceraGuadiasBean.setIdpersona(Long.valueOf(miform.getIdPersonaMovimiento()));
				scsCabeceraGuadiasBean.setFechainicio(UtilidadesFecha.getDate(miform.getFechaInicio(), "yyyy/MM/dd 00:00:00"));
				ScsCabeceraguardias cabeceraGuardias = scsCabeceraGuardias.getCabeceraGuardia(scsCabeceraGuadiasBean.getIdpersona(),
						scsCabeceraGuadiasBean.getIdturno(), scsCabeceraGuadiasBean.getIdguardia(), scsCabeceraGuadiasBean.getIdinstitucion(),
						scsCabeceraGuadiasBean.getFechainicio());

				if (cabeceraGuardias.getIdmovimiento() != null) {
					// Desde Asunto y ya existe movimiento
					FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm(usr);

					Hashtable resultado = movimAdm.getMovimientoVario(String.valueOf(cabeceraGuardias.getIdinstitucion()),
							String.valueOf(cabeceraGuardias.getIdmovimiento()), usr.getLanguage());
					ses.setAttribute("resultado", resultado);
					ses.setAttribute("modo", "edicion");
					ses.setAttribute("ORIGEN", "GUARDIAS");

				} else {// Desde Asunto y es nuevo
					FcsFactApunteAdm fcsFactApunteAdm = new FcsFactApunteAdm(usr);
					ScsTurnoAdm scsTurnoAdm = new ScsTurnoAdm(usr);
					Hashtable infoTurno = scsTurnoAdm.getDatosTurno(String.valueOf(cabeceraGuardias.getIdinstitucion()), String.valueOf(cabeceraGuardias.getIdturno()));
					ScsGuardiasTurnoAdm scsGuardiaAdm = new ScsGuardiasTurnoAdm(usr);
					ScsGuardiasTurnoBean infoGuardia = scsGuardiaAdm.getGuardiaTurno(String.valueOf(cabeceraGuardias.getIdinstitucion()), String.valueOf(cabeceraGuardias.getIdturno()), String.valueOf(cabeceraGuardias.getIdguardia()));

					String nombre_dest = persAdm.obtenerNombreApellidos(String.valueOf(cabeceraGuardias.getIdpersona()));
					String nif = persAdm.obtenerNIF(String.valueOf(cabeceraGuardias.getIdpersona()));
					ses.setAttribute("IDPERSONACOLEGIADO", String.valueOf(cabeceraGuardias.getIdpersona()));
					ses.setAttribute("NOMBRE", nombre_dest);
					ses.setAttribute("NIF", nif);
					ses.setAttribute("NCOLEGIADO", miform.getNcolegiado());
					ses.setAttribute("ORIGEN", "GUARDIAS");
					String descripcionAsunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "movimientosVarios.guardia.titulo") + " " + df.format(scsCabeceraGuadiasBean.getFechainicio()) + " en " + infoGuardia.getNombre() + " (" + infoTurno.get("ABREVIATURA") + ")";
					ses.setAttribute("ASUNTO", descripcionAsunto);
					ses.setAttribute("DESCRIPCION", descripcionAsunto);

					ses.setAttribute("IDFACTURACION", String.valueOf(cabeceraGuardias.getIdfacturacion()));
					ses.setAttribute("IDGRUPOFACTURACION", String.valueOf(infoTurno.get("IDGRUPOFACTURACION")));

					Hashtable hashActuacion = new Hashtable();
					hashActuacion.put("IDTURNO", scsCabeceraGuadiasBean.getIdturno().toString());
					hashActuacion.put("IDINSTITUCION", scsCabeceraGuadiasBean.getIdinstitucion().toString());
					hashActuacion.put("IDGUARDIA", scsCabeceraGuadiasBean.getIdguardia().toString());
					hashActuacion.put("IDPERSONA", scsCabeceraGuadiasBean.getIdpersona().toString());
					hashActuacion.put("FECHAINICIO", df.format(scsCabeceraGuadiasBean.getFechainicio()));

					ses.setAttribute("hashActuacion", hashActuacion);

					// Cogemos el importe de la facturación
					String importeFactura = fcsFactApunteAdm.getImporteTotalFacturado(scsCabeceraGuadiasBean);
					ses.setAttribute("CANTIDAD", formatearImporteFacturadoParaMV(importeFactura));
				}

			}
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, null);
		}

		return "nuevo";
	}

	/**
	 * 
	 * @param importeFactura
	 */
	private String formatearImporteFacturadoParaMV(String importeFactura)
	{
		String primerCaracter = importeFactura.substring(0, 1);
		if (primerCaracter.equalsIgnoreCase("-")) { // La cantidad es negativa luego el importe para contrarestar debe ser positivo
			importeFactura = importeFactura.replace("-", "");
		} else if (!"0".equalsIgnoreCase(importeFactura)) {
			importeFactura = "-" + importeFactura;
		}
		
		return importeFactura;
	}
	
	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "error";

		try {		
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
			MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
			Hashtable datos = (Hashtable)miform.getDatos();
			Vector resultado = null;
			//falta la institucion
			datos.put("IDINSTITUCION",(String)usr.getLocation());
			
			String checkHistoricoMovimiento = miform.getCheckHistorico();	
			String check = (String) miform.getCheckHistoricoMovimiento();
			if (check != null) {
				datos.put("CHECKHISTORICO",check);
			}
			
			//String prueba =(String) mapping.getParameter().toUpperCase();
			
			//String prueba2 = (String) ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase(); 			

			/**********  PAGINADOR ************/
			HashMap databackup = new HashMap();
			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				resultado = new Vector();

				// Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String) request.getParameter("pagina");
				if (paginador != null) {
					if (pagina != null) {
						resultado = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						resultado = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", resultado);

			} else {
				databackup = new HashMap();
				// obtengo datos de la consulta
				Paginador movimientos = movimAdm.consultaBusqueda(datos);
				databackup.put("paginador", movimientos);
				if (movimientos != null) {
					resultado = movimientos.obtenerPagina(1);
					databackup.put("datos", resultado);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				}
			}	

			// Si vengo desde la ficha colegial
			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
				String idPersona = (String) request.getSession().getAttribute("idPersonaPestanha");
				UtilidadesHash.set(datos, "IDPERSONA", idPersona);
			
			} else if ( "si".equals(request.getParameter("botonBuscarPulsado")) ){//Si no se viene de ficha colegial y se ha pulsado el botón buscar, no se tiene en cuenta el IDPERSONA
				UtilidadesHash.set(datos, "IDPERSONA", "");
			}

			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {			
				request.getSession().setAttribute("entrada","2");
			}
			
			request.getSession().setAttribute("checkHistoricoMovimiento", check);
			request.getSession().setAttribute("checkHistorico", checkHistoricoMovimiento);
			request.getSession().setAttribute("MOSTRARMOVIMIENTOS", (String)datos.get("MOSTRARMOVIMIENTOS"));
			
			//el mapping correcto
			destino = "resultado";
			
			//para que cuando vuelva a la página de busqueda sepa que tiene que hacer la busqueda de nuevo
			miform.setBuscar("si");
			request.getSession().setAttribute("MantenimientoMovimientosForm",miform);
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	}
		
		return destino;
	}

	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String consultaAplicacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "error";
		try {
			// obtener institucion
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
			Hashtable datos = (Hashtable)miform.getDatos();
			
			//falta la institucion
			datos.put("IDINSTITUCION",(String)usr.getLocation());
			
			UtilidadesHash.set(datos, "IDPERSONA", miform.getIdPersona());
			
			//consulta a BBDD
			FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
			
			Vector resultado = movimAdm.consultaAplicacion(datos);
			
			request.setAttribute("resultado",resultado);
			
			//el mapping correcto
			destino = "resultadoAplicacion";
			
			//para que cuando vuelva a la página de busqueda sepa que tiene que hacer la busqueda de nuevo
			request.getSession().setAttribute("MantenimientoMovimientosForm",miform);			
			

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}	
	
	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm(this.getUserBean(request));
		UsrBean usr = this.getUserBean(request);
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm) formulario;
		Vector ocultos = (Vector) miform.getDatosTablaOcultos(0);
		UserTransaction tx = (UserTransaction) usr.getTransaction();

		boolean borradoCorrecto = false;

		try {
			// preparando la hashtable a borrar
			Hashtable aBorrar = new Hashtable();
			UtilidadesHash.set(aBorrar, FcsMovimientosVariosBean.C_IDINSTITUCION, usr.getLocation());
			UtilidadesHash.set(aBorrar, FcsMovimientosVariosBean.C_IDMOVIMIENTO, (String) ocultos.get(0));

			// obteniendo el origen del movimiento
			Hashtable<Integer, Object> pkMovimiento = new Hashtable<Integer, Object>();
			pkMovimiento.put(1, aBorrar.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO));
			pkMovimiento.put(2, aBorrar.get(FcsMovimientosVariosBean.C_IDINSTITUCION));
			String origen = EjecucionPLs.ejecutarFuncion(pkMovimiento, "f_Siga_Asuntoorigen_MV");

			tx.begin();

			if (origen != null && !"".equalsIgnoreCase(origen)) {
				Hashtable hashDatosOrigen = new Hashtable();

				if (ClsConstants.ORIGEN_MV_RECURSO_ACTUACIONDESIGNA.equalsIgnoreCase(origen)) {

					UtilidadesHash.set(hashDatosOrigen, ScsActuacionDesignaBean.C_IDINSTITUCION, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDINSTITUCION));
					UtilidadesHash.set(hashDatosOrigen, ScsActuacionDesignaBean.C_IDMOVIMIENTO, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO));

					ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm(usr);
					designaAdm.quitaMovimientoVarioAsociado(hashDatosOrigen);

				} else if (ClsConstants.ORIGEN_MV_RECURSO_ACTUACIONASISTENCIA.equalsIgnoreCase(origen)) {

					UtilidadesHash.set(hashDatosOrigen, ScsActuacionAsistenciaBean.C_IDINSTITUCION, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDINSTITUCION));
					UtilidadesHash.set(hashDatosOrigen, ScsActuacionAsistenciaBean.C_IDMOVIMIENTO, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO));

					ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usr);
					actuacionAsistenciaAdm.quitaMovimientoVarioAsociado(hashDatosOrigen);

				} else if (ClsConstants.ORIGEN_MV_RECURSO_ASISTENCIA.equalsIgnoreCase(origen)) {

					UtilidadesHash.set(hashDatosOrigen, ScsAsistenciasBean.C_IDINSTITUCION, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDINSTITUCION));
					UtilidadesHash.set(hashDatosOrigen, ScsAsistenciasBean.C_IDMOVIMIENTO, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO));

					ScsAsistenciasAdm asistenciasAdm = new ScsAsistenciasAdm(usr);
					asistenciasAdm.quitaMovimientoVarioAsociado(hashDatosOrigen);

				} else if (ClsConstants.ORIGEN_MV_RECURSO_CABECERAGUARDIA.equalsIgnoreCase(origen)) {

					UtilidadesHash .set(hashDatosOrigen, ScsCabeceraGuardiasBean.C_IDINSTITUCION, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDINSTITUCION));
					UtilidadesHash.set(hashDatosOrigen, ScsCabeceraGuardiasBean.C_IDMOVIMIENTO, (String) aBorrar.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO));

					ScsCabeceraGuardiasAdm scsCabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usr);
					scsCabeceraGuardiasAdm.quitaMovimientoVarioAsociado(hashDatosOrigen);
				}
			}

			// borrando el registro
			borradoCorrecto = movimAdm.delete(aBorrar);
			if (borradoCorrecto) {
				tx.commit();
			} else {
				tx.rollback();
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, tx);
		}
		
		if (borradoCorrecto) {
			return exitoRefresco("messages.deleted.success", request);
		} else {
			return exitoRefresco("messages.deleted.error", request);
		}
	}

}
