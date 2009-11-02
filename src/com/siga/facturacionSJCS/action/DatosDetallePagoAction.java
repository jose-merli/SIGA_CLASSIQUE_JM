/* VERSIONES:
 * ruben.fernandez - 09/03/2005 - Creacion
 * Modificado por david.sanchezp el 05/05/2005 - Modificaciones varias.
 */

/**
 * Clase action para la visualizacion y mantenimiento de los datos de facturación para una Institución.<br/>
 */

package com.siga.facturacionSJCS.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsEstadosPagosBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.form.DatosDetallePagoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class DatosDetallePagoAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		//recogemos el formulario
		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		
		//recogemos el modo de acceso desde el parametro de las pestanhas
		String accion = (String)request.getParameter("accion");
		try{
			//modos normales de acceso, se redirigen a abrir
			if ((accion != null)&&(((accion.equalsIgnoreCase("nuevo"))||accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("consulta")))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}//en caso contrario (consulta) se llama al executeInternal de MasterForm
//			else if ((miForm.getModo()!=null)&&(miForm.getModo().equalsIgnoreCase("modificarPrecioPago")))
//				return mapping.findForward(modificarPrecioPago (mapping, miForm, request, response));
			else if ((miForm.getModo()!=null)&&(miForm.getModo().equalsIgnoreCase("abrirVolver")))
				return mapping.findForward(abrirVolver (mapping, miForm, request, response));
//			else if ((miForm.getModo()!=null)&&(miForm.getModo().equalsIgnoreCase("verDetalle")))
//				return mapping.findForward(verDetalle (mapping, miForm, request, response));
			else if ((miForm.getModo()!=null) && (miForm.getModo().equalsIgnoreCase("detalleLetrado")))
				return mapping.findForward(detalleLetrado (mapping, miForm, request, response));
			else if ((miForm.getModo()!=null) && (miForm.getModo().equalsIgnoreCase("detalleConcepto")))
				return mapping.findForward(detalleConcepto (mapping, miForm, request, response));
			else
				return super.executeInternal(mapping, formulario, request, response);
		}
		catch(SIGAException e){
			throw e;
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
	}
	/** 
	 *  Funcion que atiende la accion abrirVolver.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirVolver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward="inicio";
		//Recuperamos el USRBEAN
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
		
		//Recogemos los parámetros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idPago = (String)miform.getIdPago();
		String accion = "editar";
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//vector que pasaremos como resultado a la jsp
		Vector resultado = new Vector();
		
		//variable para recoger el nombre del colegio
		String nombreInstitucion = "";
		
		
		//comprobamos que el estado no sea ABIERTO
		boolean abierta = false;
		try{
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Integer idInstitucionInt = new Integer (idInstitucion);
			Integer idPagoInt = new Integer (idPago);
			Hashtable hash = (Hashtable)pagoAdm.getEstadoPago(idInstitucionInt,idPagoInt);
			if (((String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG)).equalsIgnoreCase(ClsConstants.ESTADO_PAGO_ABIERTO))
				abierta = true;
		}catch(Exception e){}
		
		if (!abierta){
			//Creamos una clausula where que nos servirá para consultar por idPago e idInstitucion
			String consultaPago = " where idInstitucion = " + idInstitucion +" and idPago =" + idPago + " ";
			
			try 
			{
				//Recuperamos los importes del pago, por cada persona
				resultado = this.getDetallePago(usr.getLocation(),idPago , request);
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				resultado = new Vector();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				throwExcp("Error: DatosDetallePagoAction",e,null);
			}
			//pasamos el resultado por la request
			request.setAttribute("resultado",resultado);
		}else{
			//pasamos por la request una variables para indicar que el estado es abierta
			request.setAttribute("estado","abierta");
		}
		
		try{
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
		}catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
		}
		
		
		//pasamos el nombre de la institución, y los identificadores del pago y la institucion
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		request.setAttribute("idPagosJG",idPago);
		request.setAttribute("idInstitucion",idInstitucion);
		
		return forward;
	}
	
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
//	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		
//		//Recogemos el formulario
//		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
//		
//		//recuperamos el UsrBean
//		HttpSession ses = request.getSession();
//		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
//		
//		//recuperamos la fila seleccionada y el campo oculto(idPersona)
//		String idPersona = (String)miform.getIdPersona();
//		//String nColegiado = (String)visibles.get(0);
//		String nombreColegiado = (String)miform.getNombreColegiado();
//		//String importeTotal = (String)miform.getImporteTotal();
//		
//		//recuperamos el identificador del pago y de la institucion
//		String idInstitucion = (String)miform.getIdInstitucion();
//		if(idInstitucion==null){
//			idInstitucion = (String)usr.getLocation();
//			//idPersona = (String)request.getSession().getAttribute("idPersona");
//			request.getSession().removeAttribute("idPersona");
//		}
//		String idPago = (String)miform.getIdPago();
//		
//		//variable para recoger el nombre del colegio
//		String nombreInstitucion = "";
//		
//		//variable para consultar el estado
//		Hashtable hash = new Hashtable();
//		
//		try{
//			//Consultamos el nombre de la institucion
//			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
//			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
//		}catch(ClsExceptions e){
//			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
//		}
//		
//		//consultamos el estado del pago
//		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm (this.getUserBean(request));
//		try{
//			Integer idInst = new Integer (idInstitucion);
//			Integer idPag = new Integer (idPago);
//			hash = (Hashtable)pagosAdm.getEstadoPago(idInst, idPag);
//		}catch(Exception e){}
//		
//		//consultamos los detalles del pago para esa persona
//		FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
//		Vector resultado = (Vector)pagoAdm.getDetallePorColegiado(idInstitucion,idPago,idPersona);
//		
//		//devolvemos el vector con los resultados y el nombre del cliente
//		request.setAttribute("estado",(String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG));
//		request.setAttribute("resultado",resultado);
//		request.setAttribute("idPago",idPago);
//		request.setAttribute("nombreColegiado",nombreColegiado);
//		request.setAttribute("nombreInstitucion",nombreInstitucion);
//		request.setAttribute("modoOriginal",miform.getModoOriginal());
//		return "consulta";
//	}
	
	/** 
	 * Método que atiende la accion abrir al pulsar sobre la pestanha de Detalle en el Mantenimiento del Pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String forward="inicio";
		
		//Recogemos los parámetros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idPago = (String)request.getParameter("idPagosJG");
		String accion = (String)request.getParameter("accion");
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//Estado del Pago:
		int estado = 0;
		
		//comprobamos que el estado no sea ABIERTO
		boolean abierta = false;
		
		//Si es una nueva no tenemos idpago:
		if (idPago==null || idPago.equals(""))
			request.setAttribute("NUEVO","SI");
		
		try{
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Integer idInstitucionInt = new Integer (idInstitucion);
			Integer idPagoInt = new Integer (idPago);
			Hashtable hash = (Hashtable)pagoAdm.getEstadoPago(idInstitucionInt,idPagoInt);
			estado = Integer.parseInt((String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG));
			if (estado == Integer.parseInt(ClsConstants.ESTADO_PAGO_ABIERTO))
				abierta = true;
		}
		catch(Exception e){abierta = false;}
		
		if (!abierta){
			
			//	Recuperamos los importes del pago, por cada persona
			Vector resultado = new Vector();
			
			try 
			{	
				resultado = (Vector)this.getDetallePago(idInstitucion, idPago, request);
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction " + e.getMessage(), e,3);
				resultado = new Vector();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				throwExcp("Error: DatosDetallePagoAction",e,null);
			}
			//pasamos el resultado por la request
			request.setAttribute("resultado",resultado);
			
			//pasamos por la request una variables para indicar que el estado NO es abierta
			request.setAttribute("estado","NOabierta");
		}
		else {
			//pasamos por la request una variables para indicar que el estado es abierta
			request.setAttribute("estado","abierta");
		}
		
		String nombreInstitucion = "";
		try {
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion);
		}
		catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
		}
		
		//pasamos el nombre de la institución, y los identificadores del pago y la institucion
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		request.setAttribute("idPagosJG",idPago);
		request.setAttribute("idInstitucion",idInstitucion);
		request.setAttribute("estadoPago",Integer.toString(estado));
		request.setAttribute("modoOriginal",accion);
		return forward;
	}
	
	
	
	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
//	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		
//		//recogemos el formulario
//		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
//		//recogemos el campo oculto para ver que registro hemos editado
//		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
//		String tipo = (String)ocultos.get(0);
//		//recogemos los campos de la tabla seleccionados
//		//Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
//		//recogemos el idPAgo
//		String idPago = (String)miform.getIdPago();
//		String idPersona = (String)miform.getIdPersona();
//		
//		String importeIrpf = "0"; 
//		String importe = "0";
//		String porcentajeIrpf = "0";
//		
//		if ((tipo!=null)&&(tipo.equalsIgnoreCase("TURNO"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String idTurno = (String)ocultos.get(3);
//			String anio = (String)ocultos.get(4);
//			String numero = (String)ocultos.get(5);
//			String numeroAsunto = (String)ocultos.get(6);			
//			String consulta = " where " + FcsPagoActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion +
//			" and " + FcsPagoActuacionDesignaBean.C_IDPAGOSJG + "=" + idPago + " "+
//			" and " + FcsPagoActuacionDesignaBean.C_IDTURNO + "=" + idTurno + " " +
//			" and " + FcsPagoActuacionDesignaBean.C_ANIO + "=" + anio + " " +
//			" and " + FcsPagoActuacionDesignaBean.C_NUMERO + "=" + numero+ " " +
//			" and " + FcsPagoActuacionDesignaBean.C_NUMEROASUNTO + "=" + numeroAsunto + " ";
//			FcsPagoActuacionDesignaAdm admin = new FcsPagoActuacionDesignaAdm(this.getUserBean(request));
//			try{
//				FcsPagoActuacionDesignaBean beanReg = (FcsPagoActuacionDesignaBean)((Vector)admin.select(consulta)).get(0);
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","turno");
//				importeIrpf = (String)ocultos.get(7);
//				importe = (String)ocultos.get(8);
//				porcentajeIrpf = (String)ocultos.get(9);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("GUARDIA"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String idTurno = (String)ocultos.get(3);
//			String idGuardia = (String)ocultos.get(4);
//			String idCalendario = (String)ocultos.get(5);
//			String idPers = (String)ocultos.get(6);
//			String fecha = (String)ocultos.get(7);
//			String consulta = " where " + FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion + 
//			" and " + FcsPagoGuardiasColegiadoBean.C_IDTURNO + "=" + idTurno +
//			" and " + FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG + "=" + idPago +
//			" and " + FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS + "=" + idCalendario +
//			" and " + FcsPagoGuardiasColegiadoBean.C_IDPERSONA + "=" + idPers +
//			" and " + FcsPagoGuardiasColegiadoBean.C_FECHAINICIO + "= TO_DATE('"+fecha+"','YYYY/MM/DD HH24:MI:SS')"+
//			" and " + FcsPagoGuardiasColegiadoBean.C_IDGUARDIA + "=" + idGuardia + " ";
//			FcsPagoGuardiasColegiadoAdm admin = new FcsPagoGuardiasColegiadoAdm(this.getUserBean(request));
//			try{
//				FcsPagoGuardiasColegiadoBean beanReg = (FcsPagoGuardiasColegiadoBean)((Vector)admin.select(consulta)).get(0);
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","guardia");
//				importeIrpf = (String)ocultos.get(8);
//				importe = (String)ocultos.get(9);
//				porcentajeIrpf = (String)ocultos.get(10);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("ACTUACION"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String anio = (String)ocultos.get(3);
//			String numero = (String)ocultos.get(4);
//			String idActuacion = (String)ocultos.get(5);
//			String consulta =  " where " + FcsPagoActuacionAsistBean.C_IDINSTITUCION +"="+idInstitucion+
//			" and " + FcsPagoActuacionAsistBean.C_IDPAGOSJG + "=" + idPago +
//			" and " + FcsPagoActuacionAsistBean.C_NUMERO + "=" + numero + 
//			" and " + FcsPagoActuacionAsistBean.C_IDACTUACION + "=" + idActuacion + 
//			" and " + FcsPagoActuacionAsistBean.C_ANIO + "=" + anio +
//			" and " + FcsPagoActuacionAsistBean.C_IDACTUACION + "=" + idActuacion + " ";
//			FcsPagoActuacionAsistAdm admin = new FcsPagoActuacionAsistAdm(this.getUserBean(request));
//			try{
//				FcsPagoActuacionAsistBean beanReg = (FcsPagoActuacionAsistBean)((Vector)admin.select(consulta)).get(0);
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoActuacionAsistBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","actuacion");
//				importeIrpf = (String)ocultos.get(6);
//				importe = (String)ocultos.get(7);
//				porcentajeIrpf = (String)ocultos.get(8);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("ASISTENCIA"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String anio = (String)ocultos.get(3);
//			String numero = (String)ocultos.get(4);
//			FcsPagoAsistenciaAdm admin = new FcsPagoAsistenciaAdm(this.getUserBean(request));
//			String consulta = " where " + FcsPagoAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
//			" and " +FcsPagoAsistenciaBean.C_IDPAGOSJG + "=" + idPago +
//			" and " +FcsPagoAsistenciaBean.C_ANIO + "=" + anio +
//			" and " +FcsPagoAsistenciaBean.C_NUMERO + "=" + numero + " ";
//			try{
//				FcsPagoAsistenciaBean beanReg = (FcsPagoAsistenciaBean)((Vector)admin.select(consulta)).get(0);
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoAsistenciaBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","asistencia");
//				importeIrpf = (String)ocultos.get(5);
//				importe = (String)ocultos.get(6);
//				porcentajeIrpf = (String)ocultos.get(7);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("SOJ"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String idTipoSoj = (String)ocultos.get(3);
//			String anio = (String)ocultos.get(4);
//			String numero = (String)ocultos.get(5);
//			FcsPagoSojAdm admin = new FcsPagoSojAdm(this.getUserBean(request));
//			String consulta = " where " + FcsPagoSojBean.C_IDINSTITUCION + "=" + idInstitucion +
//			" and " + FcsPagoSojBean.C_IDTIPOSOJ + "=" + idTipoSoj +
//			" and " + FcsPagoSojBean.C_IDPAGOSJG + "=" + idPago +
//			" and " + FcsPagoSojBean.C_ANIO + "=" + anio +
//			" and " + FcsPagoSojBean.C_NUMERO + "=" + numero + " ";
//			try{
//				FcsPagoSojBean beanReg = (FcsPagoSojBean)((Vector)admin.select(consulta)).get(0);
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoSojBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","soj");
//				importeIrpf = (String)ocultos.get(6);
//				importe = (String)ocultos.get(7);
//				porcentajeIrpf = (String)ocultos.get(8);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("EJG"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String idTipoEjg = (String)ocultos.get(3);
//			String anio = (String)ocultos.get(4);
//			String numero = (String)ocultos.get(5);
//			FcsPagoEjgAdm admin = new FcsPagoEjgAdm(this.getUserBean(request));
//			String consulta = " where " + FcsPagoEjgBean.C_IDINSTITUCION + "=" + idInstitucion +
//			" and " + FcsPagoEjgBean.C_IDTIPOEJG + "=" + idTipoEjg +
//			" and " + FcsPagoEjgBean.C_ANIO + "=" + anio +
//			" and " + FcsPagoEjgBean.C_IDPAGOSJG + "=" + idPago +
//			" and " + FcsPagoEjgBean.C_NUMERO + "=" + numero + " ";
//			try{
//				FcsPagoEjgBean beanReg = (FcsPagoEjgBean)((Vector)admin.select(consulta)).get(0); 
//				Hashtable bean = (Hashtable)beanReg.getOriginalHash().clone();
//				bean.put("IMPORTE",(String)bean.get(FcsPagoEjgBean.C_IMPORTEPAGADO));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","ejg");
//				importeIrpf = (String)ocultos.get(6);
//				importe = (String)ocultos.get(7);
//				porcentajeIrpf = (String)ocultos.get(8);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//		}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("MOVIMIENTO"))){
//			String idInstitucion = (String)ocultos.get(1);
//			String idMovimiento =(String)ocultos.get(2);
//			FcsMovimientosVariosAdm admin = new FcsMovimientosVariosAdm(this.getUserBean(request));
//			String consulta = "select * from "+ FcsMovimientosVariosBean.T_NOMBRETABLA+" where " + FcsMovimientosVariosBean.C_IDINSTITUCION +"="+idInstitucion+
//			" and " + FcsMovimientosVariosBean.C_IDMOVIMIENTO+"="+idMovimiento+" ";
//			try{
//				Hashtable bean = (Hashtable)((Vector)admin.selectGenerico(consulta)).get(0);
//				bean.put("IMPORTE",(String)bean.get(FcsMovimientosVariosBean.C_CANTIDAD));
//				request.getSession().setAttribute("DATABACKUP", bean);
//				request.setAttribute("tipo","movimiento");
//				importeIrpf = (String)ocultos.get(3);
//				importe = (String)ocultos.get(4);
//				porcentajeIrpf = (String)ocultos.get(5);
//			}catch(Exception e){
//				throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			}
//			
//			/*}else if ((tipo!=null)&&(tipo.equalsIgnoreCase("RETENCION"))){
//			 String idInstitucion = (String)ocultos.get(1);
//			 String idPersona = (String)ocultos.get(2);
//			 String idRetencion = (String)ocultos.get(3);
//			 FcsRetencionesJudicialesAdm admin = new FcsRetencionesJudicialesAdm(this.getUserBean(request));
//			 String consulta = "select "+ FcsRetencionesJudicialesBean.C_IMPORTE+" as IMPORTE from "+ FcsRetencionesJudicialesBean.T_NOMBRETABLA+" where " + FcsRetencionesJudicialesBean.C_IDINSTITUCION+"="+idInstitucion+
//			 " and "+FcsRetencionesJudicialesBean.C_IDRETENCION+"="+idRetencion+" ";
//			 try{
//			 Hashtable bean = (Hashtable)((Vector)admin.selectGenerico(consulta)).get(0);
//			 request.getSession().setAttribute("DATABACKUP", bean);
//			 //request.getSession().setAttribute("fila",ocultos);
//			  request.setAttribute("tipo","retencion");
//			  }catch(Exception e){
//			  throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
//			  }*/
//			
//		}
//		
//		// para el scroll de la ventana modal
//		//request.getSession().setAttribute("ScrollModal","S");
//		
//		request.setAttribute("idPago",idPago);
//		request.setAttribute("idPersona",idPersona);
//		request.setAttribute("importeIrpf",importeIrpf);
//		request.setAttribute("importe",importe);
//		request.setAttribute("porcentajeIrpf",porcentajeIrpf);
//		return "precioPago";
//	}
//	
	
	/** 
	 * Abre la modal de detalle de pago de cada colegiado.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
//	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		
//		//Recogemos el formulario
//		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
//		
//		//recuperamos el UsrBean
//		HttpSession ses = request.getSession();
//		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
//		
//		//recuperamos la fila seleccionada y el campo oculto(idPersona)
//		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
//		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
//		String idPersona = (String)ocultos.get(0);
//		String nColegiado = (String)visibles.get(0);
//		String nombreColegiado = (String)visibles.get(1);
//		//String importeTotal = (String)visibles.get(2);
//		
//		//Recuperamos el modo original:
//		String modoOriginal = miform.getModoOriginal();
//		
//		//recuperamos el identificador del pago y de la institucion
//		String idInstitucion = (String)miform.getIdInstitucion();
//		if(idInstitucion==null){
//			idInstitucion = (String)usr.getLocation();
//			idPersona = (String)request.getSession().getAttribute("idPersona");
//			request.getSession().removeAttribute("idPersona");
//		}
//		String idPago = (String)miform.getIdPago();
//		
//		//variable para recoger el nombre del colegio
//		String nombreInstitucion = "";
//		
//		//variable para consultar el estado
//		Hashtable hash = new Hashtable();
//		
//		try{
//			//Consultamos el nombre de la institucion
//			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
//			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
//		}catch(ClsExceptions e){
//			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
//		}
//		
//		//consultamos el estado del pago
//		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm (this.getUserBean(request));
//		try{
//			Integer idInst = new Integer (idInstitucion);
//			Integer idPag = new Integer (idPago);
//			hash = (Hashtable)pagosAdm.getEstadoPago(idInst, idPag);
//		}catch(Exception e){}
//		
//		//consultamos los detalles del pago para esa persona
//		FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
//		Vector resultado = (Vector)pagoAdm.getDetallePorColegiado(idInstitucion,idPago,idPersona);
//		
//		// para el scroll de la ventana modal
//		//request.getSession().setAttribute("ScrollModal","S");
//		
//		//devolvemos el vector con los resultados y el nombre del cliente
//		request.setAttribute("estado",(String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG));
//		request.setAttribute("resultado",resultado);
//		request.setAttribute("idPago",idPago);
//		request.setAttribute("idLetrado",idPersona);
//		request.setAttribute("nombreColegiado",nombreColegiado);
//		request.setAttribute("numeroColegiado",nColegiado);
//		request.setAttribute("nombreInstitucion",nombreInstitucion);
//		request.setAttribute("modoOriginal",modoOriginal);
//		return "consulta";
//	}
	
	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return mapSinDesarrollar;					
	}
	
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return mapSinDesarrollar;
	}
	
	/**
	 * Funcion que obtiene los idPersona de los colegiados que intervienen en un pago,
	 * y consulta los detalles de pago para cada persona,
	 * y por cada persona, obtiene:
	 * 
	 * -IDPERSONA
	 * -NCOLEGIADO
	 * -NOMBRECOLEGIADO
	 * -IMPORTETOTAL
	 * -IMPORTERETENCION
	 * -IMPORTESJCS
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	protected Vector getDetallePago (String idInstitucion, String idPago, HttpServletRequest request) throws ClsExceptions
	{
		try {
			
			// Obtenemos los datos de los detalles de los pagos
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Vector pagos = pagoAdm.getDetallePago(new Integer(idInstitucion), new Integer(idPago));
			
			// Obtenemos el nombre de las personas del pago
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			if (pagos != null) {
				for (int i = 0; i < pagos.size(); i++) {
					Hashtable a = (Hashtable) pagos.get(i);
					String idPersona = UtilidadesHash.getString(a, "IDPERSONASJCS");
					if (idPersona != null) {
						Hashtable p = facturacionAdm.getDatosPersona(idPersona);
						if (p != null) {
							
							CenColegiadoAdm cAdm = new CenColegiadoAdm (this.getUserBean(request));
							CenColegiadoBean cBean = cAdm.getDatosColegiales(new Long(idPersona), new Integer(idInstitucion));
							String numColegiado = cAdm.getIdentificadorColegiado(cBean);
							
							UtilidadesHash.set(a, "NCOLEGIADO", numColegiado);
							UtilidadesHash.set(a, "NOMBREPERSONA", UtilidadesHash.getString(p, "NOMBREPERSONA"));
							pagos.setElementAt(a, i);
						}
					}
				}
			}
			return pagos;
		}
		catch (Exception e) {
			return null;
		}
		
		/*
		 
		 //resultado final, vector de Hashtables
		  Vector resultado = new Vector();
		  
		  //vector con todos los colegiados que intervendrán en el pago
		   Vector colegiados = new Vector();
		   
		   String importeRetenciones="", importeTotalSJCS="";
		   
		   //recogemos todos los idPersona
		    try{
		    FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
		    if (!idPago.equals(""))				
		    colegiados = (Vector)pagoAdm.getColegiadosAPagar(idInstitucion, idPago);
		    else
		    colegiados = new Vector();	
		    }catch(Exception e){
		    colegiados = new Vector();
		    com.atos.utils.ClsLogging.writeFileLogError("Consulta en DatosDetallePagoAction.getDetallePago SQL:"+e.getMessage(), e,3);
		    }
		    
		    //variables para recorrer el vector de los idPersonas
		     Hashtable Persona = new Hashtable();
		     String idPersona = "";
		     
		     //variables para los importes en detalle del pago
		      String  importeActuacionDesigna="0", importeGuardiasPresenciales="0", importeAsistencias="0", importeActuaciones="0", importeSoj="0", importeEjg="0", importeMov="0";
		      String  irpfActuacionDesigna="0", irpfGuardiasPresenciales="0", irpfAsistencias="0", irpfActuaciones="0", irpfSoj="0", irpfEjg="0", importeRet="0", irpfMov="0";
		      
		      //varibles hashtables para ejecutar los getImportePagado
		       Hashtable htActuacionDesigna=new Hashtable(), htGuardiasPresenciales=new Hashtable(), htAsistencias=new Hashtable(), htActuaciones=new Hashtable(), htSoj=new Hashtable(), htEjg=new Hashtable(), htRet=new Hashtable();
		       Hashtable htMov = new Hashtable();
		       
		       //variables resultado para cada colegiado
		        String importeTotal="", nombreColegiado="", nColegiado="", importeTotalIrpf="";
		        
		        //consultamos el estado del pago
		         FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm(this.getUserBean(request));
		         boolean cerrado = false;
		         try{
		         Integer idInst = new Integer(idInstitucion);
		         Integer idPag = new Integer(idPago);
		         Hashtable hash = (Hashtable)pagoAdm.getEstadoPago(idInst, idPag);
		         int estado = Integer.parseInt((String)hash.get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG));
		         cerrado = (estado >= Integer.parseInt(ClsConstants.ESTADO_PAGO_CERRADO));
		         }catch(Exception e){cerrado=false;}
		         
		         //recorrer el vector con todos los colegiados
		          for (int cont=0; cont<colegiados.size(); cont++)
		          {
		          //cogemos el idPersona de la Persona
		           Persona = (Hashtable)colegiados.get(cont);
		           idPersona = (String)Persona.get("IDPERSONA");
		           
		           //calculamos los importes parciales
		            //para ActuacionesDesignas
		             try{
		             FcsPagoActuacionDesignaAdm actDesAdm = new FcsPagoActuacionDesignaAdm (this.getUserBean(request));
		             htActuacionDesigna = (Hashtable)actDesAdm.getImportePagado(idInstitucion,idPago,idPersona);
		             importeActuacionDesigna = (String)htActuacionDesigna.get("IMPORTE");
		             irpfActuacionDesigna = (String)htActuacionDesigna.get("IRPF");
		             }catch(Exception e){
		             importeActuacionDesigna = "0";
		             irpfActuacionDesigna = "0";
		             }
		             //para guardias presenciales
		              try{
		              FcsPagoGuardiasColegiadoAdm guardAdm = new FcsPagoGuardiasColegiadoAdm (this.getUserBean(request));
		              htGuardiasPresenciales = (Hashtable)guardAdm.getImportePagado(idInstitucion,idPago,idPersona);
		              importeGuardiasPresenciales = (String)htGuardiasPresenciales.get("IMPORTE");
		              irpfGuardiasPresenciales = (String)htGuardiasPresenciales.get("IRPF");
		              }catch(Exception e){
		              importeGuardiasPresenciales = "0";
		              irpfGuardiasPresenciales = "0";
		              }
		              //para asistencias
		               try{
		               FcsPagoAsistenciaAdm asisAdm = new FcsPagoAsistenciaAdm (this.getUserBean(request));
		               htAsistencias = (Hashtable)asisAdm.getImportePagado(idInstitucion,idPago,idPersona);
		               importeAsistencias = (String)htAsistencias.get("IMPORTE");
		               irpfAsistencias = (String)htAsistencias.get("IRPF");
		               }catch(Exception e){
		               importeAsistencias = "0";
		               irpfAsistencias = "0";
		               }
		               //para actuaciones
		                try{
		                FcsPagoActuacionAsistAdm actAdm = new FcsPagoActuacionAsistAdm (this.getUserBean(request));
		                htActuaciones = (Hashtable)actAdm.getImportePagado(idInstitucion,idPago,idPersona);
		                importeActuaciones = (String)htActuaciones.get("IMPORTE");
		                irpfActuaciones = (String)htActuaciones.get("IRPF");
		                }catch(Exception e){
		                importeActuaciones= "0";
		                irpfActuaciones= "0";
		                }
		                //para SOJ
		                 try{
		                 FcsPagoSojAdm sojAdm = new FcsPagoSojAdm (this.getUserBean(request));
		                 htSoj = (Hashtable)sojAdm.getImportePagado(idInstitucion,idPago,idPersona);
		                 importeSoj = (String)htSoj.get("IMPORTE");
		                 irpfSoj = (String)htSoj.get("IRPF");
		                 }catch(Exception e){
		                 importeSoj = "0";
		                 irpfSoj = "0";
		                 }
		                 //para EJG
		                  try{
		                  FcsPagoEjgAdm ejgAdm = new FcsPagoEjgAdm (this.getUserBean(request));
		                  htEjg = (Hashtable)ejgAdm.getImportePagado(idInstitucion,idPago,idPersona);
		                  importeEjg = (String)htEjg.get("IMPORTE");
		                  irpfEjg = (String)htEjg.get("IRPF");
		                  }catch(Exception e){
		                  importeEjg = "0";
		                  irpfEjg = "0";
		                  }
		                  //para Cobros Retenciones Judiciales
		                   if (cerrado){
		                   try{
		                   FcsCobrosRetencionJudicialAdm cobrosRetAdm = new FcsCobrosRetencionJudicialAdm (this.getUserBean(request));
		                   htRet = (Hashtable)cobrosRetAdm.getImporteRetenido(idInstitucion, idPago, idPersona);
		                   importeRet = (String)htRet.get("IMPORTE");
		                   }catch(Exception e){
		                   importeRet = "0";
		                   }
		                   //Si es cerrado no las tengo en cuenta:
		                    } else 
		                    importeRet = "0";
		                    //para Movimientos Varios
		                     try {
		                     FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		                     htMov = (Hashtable)movimientosAdm.getImporteMovimientos(idInstitucion, idPago, idPersona);
		                     importeMov = (String)htMov.get("IMPORTE");
		                     irpfMov = (String)htMov.get("IRPF");
		                     } catch(Exception e) {
		                     importeMov = "0";
		                     irpfMov = "0";
		                     }
		                     
		                     //calculamos el importe Total
		                      double contTot = 0;
		                      Double contActDes = new Double(importeActuacionDesigna);
		                      Double contGuar = new Double(importeGuardiasPresenciales);
		                      Double contAsis = new Double(importeAsistencias);
		                      Double contAct = new Double(importeActuaciones);
		                      Double contSoj = new Double(importeSoj);
		                      Double contEjg = new Double(importeEjg);
		                      Double contRet = new Double(importeRet);
		                      Double contMov = new Double(importeMov);
		                      
		                      contTot += contActDes.doubleValue();
		                      contTot += contGuar.doubleValue();
		                      contTot += contAsis.doubleValue();
		                      contTot += contAct.doubleValue();
		                      contTot += contSoj.doubleValue();
		                      contTot += contEjg.doubleValue();
		                      contTot += contMov.doubleValue();
		                      
		                      //Importe SJCS:
		                       importeTotalSJCS = String.valueOf(contTot);
		                       
		                       //Importe Retenciones:
		                        importeRetenciones = String.valueOf(importeRet);
		                        
		                        //Si el estado es >= CERRADO sumo las retenciones al total:
		                         if (cerrado)contTot += contRet.doubleValue();
		                         importeTotal = String.valueOf(contTot);
		                         
		                         //calculamos el IRPF Total
		                          double contTotIrpf = 0;
		                          Double contActDesIrpf = new Double(irpfActuacionDesigna);
		                          Double contGuarIrpf = new Double(irpfGuardiasPresenciales);
		                          Double contAsisIrpf = new Double(irpfAsistencias);
		                          Double contActIrpf = new Double(irpfActuaciones);
		                          Double contSojIrpf = new Double(irpfSoj);
		                          Double contEjgIrpf = new Double(irpfEjg);
		                          Double contMovIrpf = new Double(irpfMov);
		                          
		                          contTotIrpf += contActDesIrpf.doubleValue();
		                          contTotIrpf += contGuarIrpf.doubleValue();
		                          contTotIrpf += contAsisIrpf.doubleValue();
		                          contTotIrpf += contActIrpf.doubleValue();
		                          contTotIrpf += contSojIrpf.doubleValue();
		                          contTotIrpf += contEjgIrpf.doubleValue();
		                          contTotIrpf += contMovIrpf.doubleValue();
		                          importeTotalIrpf = String.valueOf(contTotIrpf);
		                          
		                          //recuperamos el nombre del colegiado
		                           CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
		                           try{
		                           nombreColegiado = (String)personaAdm.obtenerNombreApellidos(idPersona);
		                           }catch(Exception e){
		                           nombreColegiado = "";
		                           com.atos.utils.ClsLogging.writeFileLogError("DatosDetalleFacturacionAction: Error al intentar recuperar el nombre del usuario con idPersona"+idPersona+"."+e.getMessage(), e,3);
		                           }
		                           
		                           //recuperamos el nColegiado
		                            CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
		                            try{
		                            Long idPersonaLong = new Long(idPersona);
		                            Integer idInsititucionInteger = new Integer(idInstitucion);
		                            CenColegiadoBean colegiadoBean = (CenColegiadoBean)colegiadoAdm.getDatosColegiales(idPersonaLong, idInsititucionInteger);
		                            nColegiado = colegiadoAdm.getIdentificadorColegiado(colegiadoBean);
		                            }catch(Exception e){
		                            nColegiado = "";
		                            com.atos.utils.ClsLogging.writeFileLogError("DatosDetalleFacturacionAction: Error al intentar recuperar el nombre del usuario con idPersona"+idPersona+"."+e.getMessage(), e,3);
		                            }
		                            
		                            //nuevo hashtable para meterlo en el Vector resultado
		                             Hashtable personaActual = new Hashtable ();
		                             personaActual.put("IDPERSONA",idPersona);
		                             personaActual.put("IMPORTETOTAL",importeTotal);
		                             personaActual.put("IMPORTETOTALIRPF",importeTotalIrpf);
		                             personaActual.put("NCOLEGIADO",nColegiado);
		                             personaActual.put("NOMBRECOLEGIADO",nombreColegiado);			
		                             personaActual.put("IMPORTERETENCIONES",importeRetenciones);
		                             personaActual.put("IMPORTETOTALJCS",importeTotalSJCS);
		                             
		                             //anhadimos el registro
		                              resultado.add(cont, personaActual);
		                              }
		                              return resultado;
		                              
		                              */
	}
	/** 
	 *  Funcion que atiende la accion modificarPrecioPago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
//	protected String modificarPrecioPago(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		
//		boolean resultado = false;
//		//recogemos el registro seleccionado al principio en la tabla de detalles
//		Hashtable elegido;
//		//recogemos el formulario
//		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
//		//recogemos el tipo de modificacion que estamos haciendo, el apunte
//		String tipo=null, idPago=null;
//		UserTransaction tx = null;
//		UsrBean usr;
//		double diferenciaImportePagado=0, importeAnterior=0;
//		String resultadoPL[];
//		double importeIrpf=0;
//		
//		try {
//			double importeActual = (new Double((String)miform.getImporteTotal())).doubleValue();
//			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
//			tipo = (String)miform.getTipo();
//			//recogemos el idPago, para luego actualizar en FcsPagosJG y el idPErsona
//			idPago = (String)miform.getIdPago();
//			
//			//Recuperamos de Sesion:
//			elegido = (Hashtable)request.getSession().getAttribute("DATABACKUP");
//			
//			if (tipo.equalsIgnoreCase("turno")){
//				//creamos el nuevo apunte, modificado
//				FcsPagoActuacionDesignaAdm admin = new FcsPagoActuacionDesignaAdm(this.getUserBean(request));
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO,(String)miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoActuacionDesignaBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoActuacionDesignaBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("guardia")){
//				FcsPagoGuardiasColegiadoAdm admin = new FcsPagoGuardiasColegiadoAdm(this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO,miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("actuacion")){
//				FcsPagoActuacionAsistAdm admin = new FcsPagoActuacionAsistAdm(this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoActuacionAsistBean.C_IMPORTEPAGADO,(String)miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoActuacionAsistBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoActuacionAsistBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoActuacionAsistBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("asistencia")){
//				FcsPagoAsistenciaAdm admin = new FcsPagoAsistenciaAdm(this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoAsistenciaBean.C_IMPORTEPAGADO,(String)miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoAsistenciaBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoAsistenciaBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoAsistenciaBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("soj")){
//				FcsPagoSojAdm admin = new FcsPagoSojAdm(this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoSojBean.C_IMPORTEPAGADO,(String)miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoSojBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoSojBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoSojBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("ejg")){
//				FcsPagoEjgAdm admin = new FcsPagoEjgAdm(this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsPagoEjgBean.C_IMPORTEPAGADO, (String)miform.getImporteTotal());
//				nuevoApunte.put(FcsPagoEjgBean.C_IMPORTEIRPF,miform.getImporteIrpf());
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsPagoEjgBean.C_IMPORTEPAGADO))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsPagoEjgBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			if (tipo.equalsIgnoreCase("movimiento")){
//				FcsMovimientosVariosAdm admin = new FcsMovimientosVariosAdm (this.getUserBean(request));
//				//creamos el nuevo apunte, modificado
//				Hashtable nuevoApunte = (Hashtable)elegido.clone();
//				nuevoApunte.put(FcsMovimientosVariosBean.C_CANTIDAD,(String)miform.getImporteTotal());
//				nuevoApunte.put(FcsMovimientosVariosBean.C_IMPORTEIRPF,String.valueOf(importeIrpf));
//				
//				//calculamos la diferencia entre el importepagado anterior y el actual
//				importeAnterior = (new Double((String)elegido.get(FcsMovimientosVariosBean.C_CANTIDAD))).doubleValue();
//				diferenciaImportePagado = importeAnterior - importeActual;
//				
//				//Calculo del IRPF:
//				resultadoPL = new String[3];
//				resultadoPL = EjecucionPLs.ejecutarPLCalcularIRPF(usr.getLocation(),miform.getIdPersona(),miform.getImporteTotal(),miform.getPorcentajeIrpf());
//				//Si el PL se ha ejecutado correctamente anhado en la hash el porcentajeIRPF calculado:
//				if (resultadoPL[2].equals("0")) {
//					nuevoApunte.put(FcsMovimientosVariosBean.C_IMPORTEIRPF,resultadoPL[0]);
//				} else {
//					return exitoModal("messages.updated.error",request);
//				}
//				
//				//iniciamos la transaccion
//				tx = usr.getTransaction();
//				tx.begin();
//				resultado = admin.update(nuevoApunte, elegido);
//			}
//			
//			//Ahora modificamos en FcsPAgosJG
//			FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(this.getUserBean(request));
//			String consulta = " where " + FcsPagosJGBean.C_IDINSTITUCION + "=" + usr.getLocation() +
//			" and " + FcsPagosJGBean.C_IDPAGOSJG + "=" +idPago+" ";
//			FcsPagosJGBean pago = (FcsPagosJGBean)((Vector)pagosAdm.select(consulta)).get(0);
//			Hashtable pagoOld = (Hashtable)pago.getOriginalHash().clone();
//			Hashtable pagoNew = (Hashtable)pagoOld.clone();
//			
//			//calculamos el nuevo importe pagado
//			//que será el valor anterior mas la suma de la diferencia de importes del apunte modificado
//			double importePagoAnterior = ((Double)pago.getImportePagado()).doubleValue();
//			double importePagoActual = importePagoAnterior + diferenciaImportePagado;
//			pagoNew.put(FcsPagosJGBean.C_IMPORTEPAGADO,String.valueOf(importePagoActual));
//			resultado = pagosAdm.update(pagoNew, pagoOld);
//			tx.commit();
//			
//			//devolvemos el mapping
//			//como vamos a volver a cargar el detalle para la persona sin pasar por seleccionar una fila
//			//pasamos el idPersona por la session, se borrará al editar los detalles
//			request.getSession().setAttribute("idPersona",(String)elegido.get(FcsMovimientosVariosBean.C_IDPERSONA));
//		} catch(Exception e) {
//			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
//		}
//		return exitoModal("messages.updated.success",request);
//	}
	
	/** 
	 * Funcion que implementa la accion verDetalle
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
//	protected String verDetalle (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		
//		String salida = "", idPago="", idInstitucion="", estadoPago="";
//		
//		try 
//		{ 
//			
//			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
//			
//			double totalTurnos = 0;
//			double totalGuardias = 0;
//			double totalEJG = 0;
//			double totalSOJ = 0;
//			double totalMovimientos = 0;
//			double totalRetenciones = 0;
//			double totalIRPFTurnos = 0;
//			double totalIRPFGuardias = 0;
//			double totalIRPFEJG = 0;
//			double totalIRPFSOJ = 0;
//			double totalIRPFMovimientos = 0;			
//			double total = 0;
//			double totalIRPF = 0;
//			double totalARepartir = 0;
//			
//			
//			DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
//			idPago = miform.getIdPago();
//			idInstitucion = miform.getIdInstitucion(); 
//			estadoPago = miform.getEstadoPago();
//			
//			
//			//////////////////////////////////
//			// MOVIMIENTOS VARIOS rgg 10-05-2005
//			
//			Object[] param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			String resultado[] = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_MOVIM_VARIOS(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			String codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalMovimientos = aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFMovimientos = aux.doubleValue();
//			}
//			
//			//////////////////////////////////
//			// TURNOS DE OFICIO rgg 10-05-2005
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_TURNOS(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalTurnos = aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFTurnos = aux.doubleValue();
//			}
//			
//			//////////////////////////////////
//			// TURNOS GUARDIAS rgg 10-05-2005
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_GUA_COLEG(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalGuardias += aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFGuardias += aux.doubleValue();
//			}
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_ACT_ASIST(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalGuardias += aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFGuardias += aux.doubleValue();
//			}
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_ASISTENCIA(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalGuardias += aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFGuardias += aux.doubleValue();
//			}
//			
//			//////////////////////////////////
//			// EJG rgg 10-05-2005
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[6];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_EJG(?,?,?,?,?,?,?,?)}", 6, param_in_facturacion);
//			codretorno = (String)resultado[4];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalEJG = aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFEJG += aux.doubleValue();
//				aux = new Double((String)resultado[2]);
//				totalEJG += aux.doubleValue();
//				aux = new Double((String)resultado[3]);
//				totalIRPFEJG += aux.doubleValue();
//			}
//			
//			//////////////////////////////////
//			// SOJ rgg 10-05-2005
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[6];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_SOJ(?,?,?,?,?,?,?,?)}", 6, param_in_facturacion);
//			codretorno = (String)resultado[4];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[0]);
//				totalSOJ = aux.doubleValue();
//				aux = new Double((String)resultado[1]);
//				totalIRPFSOJ += aux.doubleValue();
//				aux = new Double((String)resultado[2]);
//				totalSOJ += aux.doubleValue();
//				aux = new Double((String)resultado[3]);
//				totalIRPFSOJ += aux.doubleValue();
//			}
//			
//			//////////////////////////////////
//			// RETENCIONES JUDICIALES:
//			if (Integer.parseInt(miform.getEstadoPago()) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_CERRADO)) {
//				param_in_facturacion = new Object[2];
//				// parametros de entrada
//				param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//				param_in_facturacion[1] = idPago; // idPago
//				
//				resultado = new String[3];
//				resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTE_RETENC(?,?,?,?,?)}", 3, param_in_facturacion);
//				codretorno = (String)resultado[1];
//				if (!codretorno.equals("0")){
//					//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//	        		throw new ClsExceptions ("Error al mostrar el detalle."+
//	        				"\nError en PL = "+(String)resultado[2]);
//				} else {		        		
//					Double aux = new Double((String)resultado[0]);
//					totalRetenciones += aux.doubleValue();        		
//				}
//			}
//			
//			//////////////////////////////////
//			// TOTALES rgg 10-05-2005
//			
//			param_in_facturacion = new Object[2];
//			// parametros de entrada
//			param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
//			param_in_facturacion[1] = idPago; // idPago
//			
//			resultado = new String[4];
//			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_IMPORTES_PAGOS(?,?,?,?,?,?)}", 4, param_in_facturacion);
//			codretorno = (String)resultado[2];
//			if (!codretorno.equals("0")){
//				//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
//        		throw new ClsExceptions ("Error al mostrar el detalle."+
//        				"\nError en PL = "+(String)resultado[2]);
//			} else {		        		
//				Double aux = new Double((String)resultado[1]);
//				totalARepartir = aux.doubleValue();
//			}
//			
//			total = totalTurnos + totalGuardias + totalEJG + totalSOJ + totalMovimientos + totalRetenciones; 
//			totalIRPF = totalIRPFTurnos + totalIRPFGuardias + totalIRPFEJG + totalIRPFSOJ + totalIRPFMovimientos; 
//			
//			Hashtable valoresTotales = new Hashtable();
//			valoresTotales.put("turnos", String.valueOf(UtilidadesNumero.redondea(totalTurnos,2)));
//			valoresTotales.put("guardias", String.valueOf(UtilidadesNumero.redondea(totalGuardias,2)));
//			valoresTotales.put("ejg", String.valueOf(UtilidadesNumero.redondea(totalEJG,2)));
//			valoresTotales.put("soj", String.valueOf(UtilidadesNumero.redondea(totalSOJ,2)));
//			valoresTotales.put("movimientos", String.valueOf(UtilidadesNumero.redondea(totalMovimientos,2)));
//			valoresTotales.put("total", String.valueOf(UtilidadesNumero.redondea(total,2)));
//			valoresTotales.put("totalRepartir", String.valueOf(UtilidadesNumero.redondea(totalARepartir,2)));
//			valoresTotales.put("totalIRPF", String.valueOf(UtilidadesNumero.redondea(totalIRPF,2)));
//			valoresTotales.put("retenciones", String.valueOf(UtilidadesNumero.redondea(totalRetenciones,2)));
//			request.setAttribute("valoresTotalPagos",valoresTotales);
//			request.setAttribute("estadoPago",estadoPago);
//			
//			String nombreInstitucion = "";
//			try{
//				//Consultamos el nombre de la institucion
//				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
//				nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
//			}catch(ClsExceptions e){
//				ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
//			}
//			
//			//pasamos el nombre de la institución, y los identificadores del pago y la institucion
//			request.setAttribute("nombreInstitucion",nombreInstitucion);
//			
//			salida = "verDetalle";
//			
//		} 
//		catch (Exception e) { 
//			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
//		}					
//		return salida;
//	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String detalleLetrado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			String idPago = "", idInstitucion = "", idFacturacion = "";
			
			UsrBean usuario = this.getUserBean(request);
			
			DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
			idPago        = miform.getIdPago();
			idInstitucion = miform.getIdInstitucion();
			
			// Recuperamos el idFacturacion asociado
			FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = pagosJGAdm.selectByPK(claves);
			if ((v != null) && (v.size() == 1)) {
				idFacturacion = "" + ((FcsPagosJGBean)v.get(0)).getIdFacturacion();
			}
			
			// Recuperamos los datos de importes del pago a mostrar
			Vector resultado = new Vector();
			resultado = (Vector)this.getDetallePago(idInstitucion, idPago, request);
			
			// Generarmos el fichero XSL
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String nombreFichero = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES", null);
			nombreFichero += File.separator + "PAGOS_LETRADO_" + idInstitucion + "_" + idFacturacion + "_" + idPago + ".XLS";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
			
			String cadena = UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.nColegiado") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.colegiado") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeSJCS") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeMovimientosVarios") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosFacturacion.literal.importeBruto") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.importeIRPF") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeRetenciones") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.importe");
			
			// cambio a formato DOS
			cadena += "\r\n";
			
			bw.write(cadena);
			
			if (resultado==null || resultado.size()==0) {			
//				cadena = UtilidadesString.getMensajeIdioma(usuario, "messages.noRecordFound");
//				bw.write(cadena);
			} 
			else { 
				for (int i = 0; i < resultado.size(); i++) {
					Hashtable fila = (Hashtable) resultado.get(i);
					String nombreColegiado = UtilidadesHash.getString(fila, "NOMBREPERSONA");
					String ncolegiado      = UtilidadesHash.getString(fila, "NCOLEGIADO");
					String irpf            = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"),2);
					String importeRetenciones = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"),2);
					String importeTotalSJCS   = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS"),2);
					String importeTotalMovimientoVarios = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS"),2);
					float totalBrutos = Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios);
					if (totalBrutos<0) totalBrutos=0; 
					float totalTotal = Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios)+Float.parseFloat(irpf)+Float.parseFloat(importeRetenciones);
					if (totalTotal<0) totalTotal=0; 
					
					
					
					/*cadena = ncolegiado + "\t" + nombreColegiado + "\t" + importeTotalSJCS + " € \t" + importeTotalMovimientoVarios + " € \t" + importeRetenciones + " € \t" + totalBrutos + " € \t" + irpf + " € \n";*/
					cadena = ncolegiado + "\t" + nombreColegiado + "\t" + importeTotalSJCS.replace('.',',') + "\t" + importeTotalMovimientoVarios.replace('.',',') + "\t" + UtilidadesNumero.redondea(String.valueOf(totalBrutos),2).replace('.',',') + "\t" + irpf.replace('.',',') + "\t" + importeRetenciones.replace('.',',') + "\t" + UtilidadesNumero.redondea(String.valueOf(totalTotal),2).replace('.',',') + "\t" ;
					// cambio a formato DOS
					cadena += "\r\n";
					
					bw.write(cadena);
				}
			}
			bw.close();
			
			// Descargamos el fichero
			File fichero = new File(nombreFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";		
	}
	
	protected String detalleConcepto (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			String idPago = "", idInstitucion = "", idFacturacion = "";
			
			DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
			idPago        = miform.getIdPago();
			idInstitucion = miform.getIdInstitucion();
			
			// Recuperamos el idFacturacion asociado
			FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = pagosJGAdm.selectByPK(claves);
			if ((v != null) && (v.size() == 1)) {
				idFacturacion = "" + ((FcsPagosJGBean)v.get(0)).getIdFacturacion();
			}
			
			request.setAttribute("idInstitucion", idInstitucion);
			request.setAttribute("idFacturacion", idFacturacion);
			request.setAttribute("idPago", idPago);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "detalleConceptoDescargas";		
	}
	
}
