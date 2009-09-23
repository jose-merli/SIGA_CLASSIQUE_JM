/**
 * Clase action para la visualizacion y mantenimiento de los datos de facturación para una Institución.<br/>
 * Gestiona la edicion, consulta y mantenimiento de la ejecución de facturaciones.  
 */

package com.siga.facturacionSJCS.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsEstadosFacturacionBean;
import com.siga.beans.FcsFactEstadosFacturacionAdm;
import com.siga.beans.FcsFactEstadosFacturacionBean;
import com.siga.beans.FcsFactGrupoFactHitoAdm;
import com.siga.beans.FcsFactGrupoFactHitoBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.DatosGeneralesFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;


public class DatosGeneralesFacturacionAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		String accion = (String)request.getParameter("accion");
		try{
			if((accion != null)&&(accion.equalsIgnoreCase("nuevo"))){
				return mapping.findForward(this.nuevo(mapping, miForm, request, response));
			}
			if ((accion != null)&&((accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("consulta")))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("insertarCriterio"))){
				return mapping.findForward(this.insertarCriterio(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("nuevoCriterio"))){
				return mapping.findForward(this.nuevoCriterio(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("listaConsejo"))){
				return mapping.findForward(this.listaParaConsejo(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("ejecutarFacturacion"))){
				return mapping.findForward(this.ejecutarFacturacion(mapping, miForm, request, response));
			}
			if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("ejecutarRegularizacion"))){
				return mapping.findForward(this.ejecutarRegularizacion(mapping, miForm, request, response));
			}
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("download"))){
				return mapping.findForward(this.download(mapping, miForm, request, response));
			}
			if ((miForm.getModo() != null) && (miForm.getModo().equalsIgnoreCase("descargarLog"))){
				return mapping.findForward(this.descargarLog(mapping, miForm, request, response));
			}
			if ((accion != null)&&(accion.equalsIgnoreCase("downloadMultiple"))){
				return mapping.findForward(this.generarDescargarExcelFacturacionMultiple(mapping, miForm, request, response));
			}
			if ((accion != null)&&(accion.equalsIgnoreCase("descargarFichero"))){
				return mapping.findForward(this.descargarFichero(mapping, miForm, request, response));
			}
			

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
	 *  Funcion que atiende la accion listaParaConsejo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String listaParaConsejo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos del formulario el idFacturacion
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		String idFacturacion = (String)miform.getIdFacturacion();
		UserTransaction  tx = null;
		String forward = null;
		String[] resultadoPL = null;
		
		try {
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			//Ejecutamos el PL PROC_FCS_PREPARAR_PARA_CONSEJO para modificar el idfacturacion de las tablas de SJCS y el estado de la
			// facturacion a lista para consejo:
			tx.begin();
			resultadoPL = this.ejecutarPLPrepararParaConsejo(miform.getIdInstitucion(), idFacturacion, this.getUserName(request).toString());
			if (!resultadoPL[0].equals("0")) {
				throw new ClsExceptions("Ha ocurrido un error al cerrar la facturación.");
				//tx.rollback();
				//forward = exito("messages.factSJCS.error.prepararConsejo",request);
			} else {
				//Si todo ha ido bien:
				
				forward = exitoRefresco("messages.updated.success",request);//"refrescar";
				//le pasamos los parametros a las pestanhas
				Hashtable datosFacturacion = new Hashtable();
				datosFacturacion.put("idFacturacion", idFacturacion);
				datosFacturacion.put("idInstitucion", (String)usr.getLocation());
				datosFacturacion.put("accion", "Edicion");
				request.setAttribute("datosFacturacion",datosFacturacion);
			}
			tx.commit();
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
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
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
		
		Vector vHitos = new Vector();
		
		try{
			vHitos = (Vector)facturacionAdm.getCriteriosFacturacion(usr.getLocation(), miform.getIdFacturacion());
		}
		catch(Exception e) { }
		
		request.getSession().setAttribute("vHito", vHitos);
		request.setAttribute("modo","Edicion");
		return "refrescarCriterios";
	}
	
	/** 
	 *  Funcion que atiende la accion nuevoCriterio.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevoCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//Recuperamos el USRBEAN
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		try{
			//		Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			request.setAttribute("nombreInstitucion", nombreInstitucion);
			request.setAttribute("idFacturacion", miform.getIdFacturacion());
			String aux = request.getParameter("prevision");
			if (aux!=null) {
				request.setAttribute("prevision", "S");
			}
		}
		catch(Exception e){}
		return "nuevoCriterio";
	}
	/** 
	 *  Funcion que atiende la accion abrir.
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
		String idFacturacion = (String)request.getParameter("idFacturacion");
		String accion = (String)request.getParameter("accion");
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//Creamos una clausula where que nos servirá para consultar por idFacturacion e idInstitucion
		String consultaFact = " where idInstitucion = " + idInstitucion +" and idFacturacion =" + idFacturacion + " ";
		
		try 
		{
			//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
			FcsFacturacionJGAdm facturaAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			FcsFacturacionJGBean facturaBean = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact)).get(0);
			
			if (facturaBean.getIdFacturacion_regulariza()!=null) {
				String consultaFact2 = " where idInstitucion = " + idInstitucion +" and idFacturacion =" + facturaBean.getIdFacturacion_regulariza() + " "; 
				FcsFacturacionJGBean facturaBeanOriginal = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact2)).get(0);
				request.setAttribute("NOMBREFACTURACION", facturaBeanOriginal.getNombre());
				
			}
			
			if (facturaBean.getRegularizacion().equals(ClsConstants.DB_TRUE)) {
				request.setAttribute("FcsRegularizacion","1");
			}
			
			//Recuperamos el Estado de la Factura
			Hashtable nombreEstadoBean = (Hashtable)facturaAdm.getEstadoFacturacion(idInstitucion, idFacturacion);
			
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion);
			
			//Consultamos los criterios de facturacion
			Vector vHito = (Vector)facturaAdm.getCriteriosFacturacion(idInstitucion, idFacturacion);
			
			//Pasamos la factura seleccionada ,el modo, el nombre de la institucion ,
			//la descripcion del estado , el identificador de la partida presupuestaria,
			//el vector con los hitos a facturar y el vector de los grupos de facturacion
			request.getSession().setAttribute("DATABACKUP",facturaBean); 
			request.setAttribute("modo",accion);
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			request.setAttribute("estado",(String)nombreEstadoBean.get(FcsEstadosFacturacionBean.C_DESCRIPCION));
			request.setAttribute("fechaEstado",(String)nombreEstadoBean.get(FcsFactEstadosFacturacionBean.C_FECHAESTADO));
//			request.setAttribute("importe",partidaBean.getImportePartida().toString());
			//request.getSession().setAttribute("idFacturacion",((Integer)facturaBean.getIdFacturacion()).toString());
			//el idFacturacion, idInstitucion y modo
			request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idFacturacion",idFacturacion);
			//esta variable es para mostrar en los criterios el boton de borrado
			request.getSession().setAttribute("estado",(String)nombreEstadoBean.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION));
			request.setAttribute("idEstado",UtilidadesHash.getInteger(nombreEstadoBean, FcsEstadosFacturacionBean.C_IDESTADOFACTURACION));
			// para ver si ya se ha ejecutado
			request.setAttribute("yaHaSidoEjecutada",new Boolean(facturaAdm.yaHaSidoEjecutada(idInstitucion,idFacturacion)));
			
			//para el iframe de abajo pasamos el modo por session, se borrará en la jsp
			if (accion!=null)request.getSession().setAttribute("Modo",accion);
			request.getSession().setAttribute("vHito",vHito);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		} 									
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 * Funcion que implementa la accion ejecutarFacturacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutarFacturacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		String salida = "";
		
		try 
		{ 
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = usr.getTransactionPesada();
			
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = usr.getLocation(); 
			
			tx.begin();
			// admFac.ejecutarFacturacion(idInstitucion,idFacturacion,tx);
			FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(usr);
			FcsFactEstadosFacturacionBean beanEstado = null;
			beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setIdFacturacion(new Integer(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ClsConstants.ESTADO_FACTURACION_PROGRAMADA));
			beanEstado.setFechaEstado("SYSDATE");
			admEstado.insert(beanEstado);
			tx.commit();
		    // Notificación
			
			SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapido);
			
			salida = this.exitoRefresco("messages.facturacionSJCS.programada",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}					
		return salida;
	}
	
	/** 
	 *  Funcion que implementa la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
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
		
		String forward="inicio";
		String nombreInstitucion = "";
		try{
			//Recogemos el UsrBean
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//pasamos por el request el modo y el nombre De la institucion
		request.setAttribute("modo","nuevo");
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		return forward;
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
		
		UserTransaction tx = null;
		DatosGeneralesFacturacionForm miform = null;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		FcsFactEstadosFacturacionAdm estadosAdm = new FcsFactEstadosFacturacionAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable datos = new Hashtable(); 
		String forward="error";
		try
		{
			miform = (DatosGeneralesFacturacionForm)formulario;
			datos = (Hashtable)miform.getDatos();

			tx = usr.getTransaction();
			tx.begin();

			//calculamos el nuevo idFacturacion
			datos.put("IDINSTITUCION", (String)usr.getLocation());
			datos.put("PREVISION",ClsConstants.DB_FALSE);
			facturacionAdm.prepararInsert(datos);
			
			//ponemos el campo regularizacion a false			
			datos.put(FcsFacturacionJGBean.C_REGULARIZACION,ClsConstants.DB_FALSE);
			
			//comprobamos que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			facturaPrueba.setFechaDesde((String)miform.getFechaInicio());
			facturaPrueba.setFechaHasta((String)miform.getFechaFin());
			facturaPrueba.setIdInstitucion(Integer.valueOf((String)usr.getLocation()));
			facturaPrueba.setIdFacturacion(Integer.valueOf((String)datos.get("IDFACTURACION")));

			//preparamos la insercion en estados fact
			Hashtable estado = new Hashtable();
			estado.put(FcsFactEstadosFacturacionBean.C_IDFACTURACION , datos.get("IDFACTURACION"));
			estado.put(FcsFactEstadosFacturacionBean.C_FECHAESTADO , "sysdate");
			estado.put(FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION, "sysdate");
			estado.put(FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION, String.valueOf(ClsConstants.ESTADO_FACTURACION_ABIERTA));
			estado.put(FcsFactEstadosFacturacionBean.C_IDINSTITUCION, usr.getLocation());
			estado.put(FcsFactEstadosFacturacionBean.C_USUMODIFICACION , usr.getUserName());
			
			if(!facturacionAdm.insert(datos))
				throw new SIGAException(facturacionAdm.getError());
			
			if(!estadosAdm.insert(estado))
				throw new SIGAException(estadosAdm.getError());
			
			tx.commit();
			
			//pasamos los datos a la pestanha
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("idFacturacion", (String)datos.get("IDFACTURACION"));
			datosFacturacion.put("idInstitucion", (String)usr.getLocation());
			datosFacturacion.put("accion", "Edicion");
			request.setAttribute("datosFacturacion",datosFacturacion);
			forward= "exitoFacturacion";
			request.setAttribute("mensaje","messages.inserted.success");
			
		}
		catch (Exception e)
		{
			throwExcp("messages.general.error", new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
		//return "refrescar";
		return forward;
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
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		Hashtable datos = (Hashtable)miform.getDatos();
		String forward = null;
		UserTransaction tx = null;
		try 
		{
		    tx = usr.getTransaction();
		    
			FcsFacturacionJGBean facturacionOldBean = (FcsFacturacionJGBean)request.getSession().getAttribute("DATABACKUP");
			
			tx.begin();
			
			//preparamos la nueva facturacion
			Hashtable datosEntrada = (Hashtable)miform.getDatos();
			
			facturacionOldBean.setNombre((String)datosEntrada.get("NOMBRE"));
			facturacionOldBean.setFechaDesde(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHADESDE")));
			facturacionOldBean.setFechaHasta(GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHAHASTA")));
			
			//comprobamos que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			facturaPrueba.setFechaDesde((String)miform.getFechaInicio());
			facturaPrueba.setFechaHasta((String)miform.getFechaFin());
			facturaPrueba.setIdInstitucion(Integer.valueOf((String)usr.getLocation()));
			facturaPrueba.setIdFacturacion(Integer.valueOf((String)datos.get("IDFACTURACION")));
			
			boolean solapamiento = facturacionAdm.existeFacturacionMismoPerdiodo(facturaPrueba,null);
			
			//Si se solapan, paramos la insercion y volvemos avisando del error sin refrescar:
			if (solapamiento) 
			    forward = exito("messages.factSJCS.facturacionMismoPeriodo",request);
			//Si no hay solapamiento modificamos:
			else {	
				//actualizamos
//				facturacionAdm.update(facturacionNew, facturacionOld);
				facturacionAdm.update(facturacionOldBean);
				
				//para despues de modificar, sino no se recoge desde la pestanha el modo, idFacturacion ni idInstitucion (de las pestanhas)
				request.getSession().setAttribute("Modo","edicion");
				request.getSession().setAttribute("idFacturacion",(String)miform.getIdFacturacion());
				request.getSession().setAttribute("idInstitucion",(String)miform.getIdInstitucion());
				forward = exitoRefresco("messages.updated.success",request);
			}
			tx.commit();
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
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
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		try 
		{
			if (ocultos!=null)
			{
				return this.borrarCriterio(mapping, formulario, request, response);
			}
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}					
		return exitoRefresco("messages.deleted.error", request);					
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
		
		String forward="listar";
		
		try 
		{
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}
		return (forward);
	}
	
	
	/** 
	 *  Funcion que implementa la accion borrarCriterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrarCriterio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable aBorrar = new Hashtable();
		UserTransaction tx = null;
		try{
		    tx = usr.getTransaction();
		    tx.begin();
			//preparamos el registro a borrar
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDINSTITUCION, usr.getLocation());
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDFACTURACION, (String)ocultos.get(0));
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION, (String)ocultos.get(1));
			aBorrar.put(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL, (String)ocultos.get(2));
			
			//borramos
			FcsFactGrupoFactHitoAdm criterioAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
			criterioAdm.delete(aBorrar);
			tx.commit();
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	}
	
	/** 
	 *  Funcion que implementa la accion insertarCriterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarCriterio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		FcsFactGrupoFactHitoAdm criterioAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
		
		UserTransaction tx = null;
		try{
		    tx = usr.getTransaction();
		    tx.begin();
		    
			// vemos si venimos de previsiones
			String prevision = request.getParameter("prevision");
			
			//preparamos el nuevo registro
			FcsFactGrupoFactHitoBean beanCriterio = new FcsFactGrupoFactHitoBean();
			beanCriterio.setFechaMod("sysdate");
			beanCriterio.setIdFacturacion(new Integer(miform.getIdFacturacion()));
			beanCriterio.setIdGrupoFacturacion(new Integer(miform.getGrupoF()));
			beanCriterio.setIdHitoGeneral(new Integer(miform.getHito()));
			beanCriterio.setIdInstitucion(new Integer(usr.getLocation()));
			beanCriterio.setUsuMod(new Integer(usr.getUserName()));
			
			Hashtable nuevoCriterio = new Hashtable();
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_FECHAMODIFICACION, "sysdate");
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDFACTURACION, (String)miform.getIdFacturacion());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION, (String) miform.getGrupoF());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL, (String) miform.getHito());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_IDINSTITUCION, (String)usr.getLocation());
			nuevoCriterio.put(FcsFactGrupoFactHitoBean.C_USUMODIFICACION, (String)usr.getUserName());
			
			// RGG 25/04/2005 Si venimos de prevision no comprobamos esto
			if (prevision==null || !prevision.equals("S")) {
				
				//comprobamos que no exista un criterio igual
				String consulta = " where " + FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION + "=" + (String) miform.getGrupoF() +
				" and " + FcsFactGrupoFactHitoBean.C_IDHITOGENERAL + "=" + (String) miform.getHito() + 
				" and " + FcsFactGrupoFactHitoBean.C_IDFACTURACION + "=" + (String) miform.getIdFacturacion() +
				" and " + FcsFactGrupoFactHitoBean.C_IDINSTITUCION + "=" + (String) usr.getLocation();
				
				Vector resultadoConsulta = criterioAdm.select(consulta);
				if (resultadoConsulta.size()>0)
					return exitoModalSinRefresco("messages.factSJCS.error.insertarCriterioFacturacion", request);
			}
			
			//comprobamos que la facturacion no se solape con otra ya existente
			FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			
			//consultamos la facturacion
			String consultaFact = " where idinstitucion=" + (String)usr.getLocation() + " and idfacturacion = " + (String) miform.getIdFacturacion() + " ";
			facturaPrueba = (FcsFacturacionJGBean)((Vector)facturacionAdm.select(consultaFact)).get(0);
			
			// RGG 25/04/2005 Si venimos de prevision no comprobamos esto
			if (prevision==null || !prevision.equals("S")) {
				boolean solapamiento = facturacionAdm.existeFacturacionMismoPerdiodo(facturaPrueba,beanCriterio);
				
				//si se solapan, paramos la insercion
				if (solapamiento) 
					return exitoModalSinRefresco("messages.factSJCS.facturacionMismoPeriodo",request);
			}
			
			//insertamos
			criterioAdm.insert(nuevoCriterio);
			
			tx.commit();
			
			//pasamos el modo que se borrará en la página de consultas criterios
			request.getSession().setAttribute("Modo","edicion");
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return exitoModal("messages.inserted.success",request);
	}
	
	
	/** 
	 * Funcion que implementa la accion ejecutarRegularizacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutarRegularizacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		String salida = "";
		
		try 
		{ 
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = usr.getTransactionPesada();
			
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = usr.getLocation(); 
			
			tx.begin();
			// admFac.ejecutarFacturacion(idInstitucion,idFacturacion,tx);
			FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(usr);
			FcsFactEstadosFacturacionBean beanEstado = null;
			beanEstado = new FcsFactEstadosFacturacionBean();
			beanEstado.setIdInstitucion(new Integer(idInstitucion));
			beanEstado.setIdFacturacion(new Integer(idFacturacion));
			beanEstado.setIdEstadoFacturacion(new Integer(ClsConstants.ESTADO_FACTURACION_PROGRAMADA));
			beanEstado.setFechaEstado("SYSDATE");
			admEstado.insert(beanEstado);
			tx.commit();
		    // Notificación
			
			SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapido);
			
			salida = this.exitoRefresco("messages.facturacionSJCS.programadaRegularizacion",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}					
		return salida;
	}
	
	
	/**
	 * Metodo que implementa el modo download
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String download (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		File fichero = null;
		String sNombreFichero = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// borro el formulario en session de Avanzada
			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
			
			String idFacturacion = "" + miform.getIdFacturacionDownload();
			String idInstitucion = "" + miform.getIdInstitucionDownload();
			String idPago 		 = "" + miform.getIdPagoDownload();
			String idPersona     = "" + miform.getIdPersonaDownload();
			
			String tipoConcepto = miform.getTipoDownload();
			if (tipoConcepto == null) {
				throw new SIGAException("Parámetro obligatorio");
			}
			
			Hashtable nombresFichero = null;
			
			if((idPago == null || idPago.equals("")) && (idPersona == null || idPersona.equals(""))) { 
				nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(new Integer(idInstitucion), new Integer(idFacturacion), user);
			}
			else {
				if((idPago != null || !idPago.equals("")) && (idPersona == null || idPersona.equals(""))) {
					nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosPago(new Integer(idInstitucion), new Integer(idFacturacion), new Integer(idPago), null, user);
				}
				else {
					nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosPago(new Integer(idInstitucion), new Integer(idFacturacion), new Integer(idPago), new Long(idPersona), user);
				}
			}
			
			
			if (tipoConcepto.equalsIgnoreCase("O")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_TURNO); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("G")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_GUARDIA); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("E")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_EJG); 	
			} 
			else if (tipoConcepto.equalsIgnoreCase("S")) {
				sNombreFichero += UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_SOJ); 	
			}
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String sDirectorio = paramAdm.getValor(user.getLocation(),"FCS","PATH_PREVISIONES",null);
			
			String sNombreCompletoFichero = sDirectorio + File.separator + sNombreFichero;
			fichero = new File(sNombreCompletoFichero);
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
	
	
	//
	//PL que inserta los precios en el historico para una institucion y una facturacion.
	//	
	private String[] ejecutarPLGuardarHistorico(DatosGeneralesFacturacionForm miform, String usuModificacion) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[]; //Parametros de salida del PL
		
		//EXEC PROC_FCS_GUARDAR_PRECIOS_HIST
		//Parametros de entrada del PL
		param_in = new Object[3];
		param_in[0] = miform.getIdInstitucion();
		param_in[1] = miform.getIdFacturacion();
		param_in[2] = usuModificacion;
		resultado = new String[2];
		//Ejecucion del PL
		resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_GUARDAR_PRECIOS_HIST (?,?,?,?,?)}", 2, param_in);
		//Resultado del PL        
		return resultado;
	}
	
	//
	//PL que modifica el idfacturacion de las tablas de SJCS y el estado de la facturacion a lista para consejo
	//	
	private String[] ejecutarPLPrepararParaConsejo(String idInstitucion, String idFacturacion, String usumodificacion) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[]; //Parametros de salida del PL
		
		//EXEC PROC_FCS_PREPARAR_PARA_CONSEJO
		//Parametros de entrada del PL
		param_in = new Object[3];
		param_in[0] = idInstitucion;
		param_in[1] = idFacturacion;
		param_in[2] = usumodificacion;
		resultado = new String[2];
		//Ejecucion del PL
		resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_PREPARAR_PARA_CONSEJO (?,?,?,?,?)}", 2, param_in);
		//Resultado del PL        
		return resultado;
	}

	
	/**
	 * Genera los ficheros excel resultado de la union de los datos de las facturaciones cuyas fechas desde/hasta
	 * están coprendidas en las fechas desde/hasta de las facturaciones inicial y final.
	 * Muestra la pantalla de descarga de esos ficheros
	 *
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String descargarLog (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{

		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;
			String idInstitucion =  miform.getIdInstitucion();
			String idFacturacion =  miform.getIdFacturacion();

			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String pathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);

			String sNombreFichero = pathFicheros + File.separator + "LOG_ERROR_" + idInstitucion + "_" + idFacturacion + ".log";

			//Genera el zip de los ficheros generados enteriormente
			File fichero=new File(sNombreFichero);
			
			//Se pasan los parametros necesarios al servlet de descarga
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);	
		}
		return "descargaFichero";
	}

	protected String generarDescargarExcelFacturacionMultiple (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{

		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DatosGeneralesFacturacionForm miform = (DatosGeneralesFacturacionForm) formulario;

		String idInstitucion = user.getLocation(); 
		String idFacturacionIni = miform.getIdFacturacionIniDownload().toString(); 
		String idFacturacionFin = miform.getIdFacturacionFinDownload().toString();

		try {
			//Genera los ficheros excel para el intervalo de facturaciones
			UtilidadesFacturacionSJCS.generarFicherosFacturacionMultiple(new Integer(idInstitucion), new Integer(idFacturacionIni), new Integer(idFacturacionFin), user);
			
			//Genera el zip de los ficheros generados enteriormente
			File fichero = creaZipFacturacionMultiple(idInstitucion, idFacturacionIni, idFacturacionFin, user);
			
			//Se pasan los parametros necesarios al servlet de descarga
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");
			
		} catch (ClsExceptions e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);	}
		return "descargaFicheroPrevio";
	}

	
	/**
	 * Crea el zip de los ficheros de facturación generados para las facturaciones entre
	 * <code>idFacturacionIni</code> y <code>idFacturacionFin</code>
	 * 
	 * @param idInstitucion
	 * @param idFacturacionIni
	 * @param idFacturacionFin
	 * @throws ClsExceptions
	 */
	private File creaZipFacturacionMultiple(String idInstitucion, String idFacturacionIni, String idFacturacionFin, UsrBean user) throws ClsExceptions	{
		ClsLogging.writeFileLog("DatosGeneralesFacturacionAction.creaZipFacturacionMultiple",10);
		
		File ficZip = null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;

		GenParametrosAdm paramAdm = new GenParametrosAdm(user);
		String sPathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);

		try {
			File pathFicheros = new File(sPathFicheros);
			//Se recuperan sólo los ficheros del intervalo de facturación
			String aux = "_" + idInstitucion + "_" + idFacturacionIni + "-" + idFacturacionFin;
			final String aux2 = aux + ".XLS";
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.matches(".*"+aux2);
				}
			};
			File [] ficherosPDF = pathFicheros.listFiles(filter);
			ficZip = new File(sPathFicheros +  File.separator +"FACTURACION_MULTIPLE" + aux + ".zip");
			if (ficherosPDF != null && ficherosPDF.length > 0) {
	
				if (ficZip.exists()) {
				    ficZip.delete();
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: el fichero zip ya existia. Se elimina",10);
				}
				
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.length; i++)
				{
				    File auxFile = (File)ficherosPDF[i];
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: fichero numero "+i+" longitud="+auxFile.length(),10);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						
						fis.close();
						outTemp.closeEntry();
					}
					//elimina el fichero temporal con las facturaciones concatenadas
					auxFile.delete();
				}
			    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: ok ",10);
				
				outTemp.close();
			}
			else{
				return null;
			}
			
		} catch (FileNotFoundException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		}
		finally {
			try {
				if (outTemp != null)
					outTemp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ficZip;
	}

	/**
	 * Metodo que permite la descarga de un fichero.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String descargarFichero (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		
		try {
			//Obtenemos el formulario y sus datos:
			String nombreFichero = (String)request.getParameter("nombreFichero");
			String rutaFichero = (String)request.getParameter("rutaFichero");
			File fichero = new File(rutaFichero);
			
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", rutaFichero);
			
			
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";	
	}


}