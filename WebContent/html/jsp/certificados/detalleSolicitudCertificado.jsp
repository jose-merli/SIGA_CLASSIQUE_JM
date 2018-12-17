<!DOCTYPE html>
<html>
<head>
<!-- detalleSolicitudCertificado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	String idLenguaje = userBean.getLanguage();
	int institucion = Integer.parseInt(idInstitucion);

	String modo = (String) request.getAttribute("modo");

	CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) request.getAttribute("solicitud");
	CenInstitucionBean beanInstitucionOrigen = (CenInstitucionBean) request.getAttribute("institucionOrigen");
	CenInstitucionBean beanInstitucionDestino = (CenInstitucionBean) request.getAttribute("institucionDestino");
	CenInstitucionBean beanInstitucionColegiacion = (CenInstitucionBean) request.getAttribute("institucionColegiacion");
	String modificarSolicitud = (String) request.getAttribute("modificarSolicitud");
	String idEstadoSolicitud = (String) request.getAttribute("idEstadoSolicitud");
	String strEstadoSolicitud = (String) request.getAttribute("strEstadoSolicitud");
	String strSiguienteEstado = (String) request.getAttribute("strSiguienteEstado");
	String tipoCertificado = (String) request.getAttribute("tipoCertificado");
	String descargaPDF = (String) request.getAttribute("descarga");

	String comboInstituciones = "getInstitucionesAbreviadas";
	String comboInstitucionesDest = "getInstitucionesAbreviadas";
	String consultaOrigen, consultaDestino = "";

	boolean pintarCheckMutualidad = (Boolean) request.getAttribute("pintarCheckMutualidad");
	boolean bSolicitudTelematica = false;
	
	boolean esCompatibleConCertificadosExistentes = (Boolean) request.getAttribute("esCompatibleConCertificadosExistentes");
	boolean facturable = (Boolean) request.getAttribute("facturable");

	if (institucion == 2000) { // General
		consultaOrigen = "getColegiosAbreviados";
		consultaDestino = "getColegiosAbreviados";
	} else if (institucion > 3000) { // Consejo
		consultaOrigen = "getColegiosDeConsejo";
		consultaDestino = "getColegiosDeConsejo";
	} else { // Colegio
		consultaOrigen = "getColegiosDeConsejo";
		consultaDestino = "getColegiosAbreviados";
	}

	ArrayList idInstitucionPresentador = new ArrayList();
	if (modo.equalsIgnoreCase("nuevo") && ClsConstants.esColegio(idInstitucion)) {
		idInstitucionPresentador.add(idInstitucion);
	} else if (beanInstitucionOrigen != null) {
		idInstitucionPresentador.add(beanInstitucionOrigen.getIdInstitucion().toString());
	}

	ArrayList idInstitucionColegiacion = new ArrayList();
	if (beanInstitucionColegiacion != null) {
		idInstitucionColegiacion.add(beanInstitucionColegiacion.getIdInstitucion().toString());
	}

	ArrayList idInstitucionDestino = new ArrayList();
	if (beanInstitucionDestino != null) {
		idInstitucionDestino.add(beanInstitucionDestino.getIdInstitucion().toString());
	}

	String codigo = (String) request.getAttribute("codigo");
	String sanciones = (String) request.getAttribute("sanciones");

	String numSolicitud = "", idProducto = "", idProductoInstitucion = "", idTipoProducto = "";
	String idInstitucionCertificado = "", institucionFinal = "", idsTemp = "", idPeticion = "";
	String idPersona = "", nombreSolicitante = "", nombreSoloSolicitante = "", apellidosSolicitante = "", nidSolicitante = "", ncolSolicitante = "";
	String sIdCompra = "";
	String estadoInc = "", residente = "", idInstitucionRes = "";
	boolean isSolicitudColegio = false;
	String[] parametros = null;
	String aceptaCesion = "", aceptMut = "";
	ArrayList aMetodoSol = new ArrayList();
	String fechaSolicitud = "", idInstitucionSolicitud = "";
	ArrayList tipoCertSel = new ArrayList();
	ArrayList motivoSolicitudSel = new ArrayList();
	ArrayList motivoAnulacionSel = new ArrayList();
	Integer idMotivoSolicitud = -1;
	Integer idMotivoAnulacion = -1;
	String paramidMotivoSolicitud = null;
	String paramidMotivoAnulacion = null;
	ArrayList idInstitucionResidencia = new ArrayList();

	if (beanSolicitud != null) {
		if (beanSolicitud.getIdPeticionProducto() != null) {
			sIdCompra = beanSolicitud.getIdPeticionProducto().toString();
		}

		if (beanSolicitud.getIdSolicitud() != null) {
			numSolicitud = beanSolicitud.getIdSolicitud().toString();
		}

		isSolicitudColegio = beanSolicitud.getIdInstitucion_Sol().intValue() != 2000
				&& !String.valueOf(beanSolicitud.getIdInstitucion_Sol()).substring(0, 2).equals("30");
		parametros = new String[] { beanSolicitud.getIdInstitucion_Sol().toString(), beanSolicitud.getIdInstitucion_Sol().toString() };
		aceptaCesion = beanSolicitud.getAceptaCesionMutualidad().equals("1") ? "checked" : "";
		aceptMut = beanSolicitud.getAceptaCesionMutualidad();
		aMetodoSol.add(beanSolicitud.getMetodoSolicitud());
		if(beanSolicitud.getMetodoSolicitud().equals("5")){
			bSolicitudTelematica = true;
			estadoInc = (String) request.getAttribute("estadoInc");
			residente = (String) request.getAttribute("residenteInc");
			idInstitucionRes = (String) request.getAttribute("idInstitucionRes");
			idInstitucionResidencia.add(idInstitucionRes);
		}
			
		fechaSolicitud = beanSolicitud.getFechaSolicitud();
		idInstitucionSolicitud = beanSolicitud.getIdInstitucion_Sol().toString();
		idTipoProducto = beanSolicitud.getPpn_IdTipoProducto().toString();
		idProducto = beanSolicitud.getPpn_IdProducto().toString();
		idProductoInstitucion = beanSolicitud.getPpn_IdProductoInstitucion().toString();
		String idtipoCertSel = idInstitucion + "_" + idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion;
		tipoCertSel.add(idtipoCertSel);
		
		idMotivoSolicitud = beanSolicitud.getIdMotivoSolicitud();
		if (idMotivoSolicitud != null) {
			String idMotivoSolicitudSel = idInstitucion + "_" + idMotivoSolicitud.toString();
			motivoSolicitudSel.add(idMotivoSolicitudSel);
		} else {
			idMotivoSolicitud = -1;
		}
		paramidMotivoSolicitud = "{\"idmotivosolicitud\":\""+idMotivoSolicitud+"\"}";
		
		idMotivoAnulacion = beanSolicitud.getIdMotivoAnulacion();
		if (idMotivoAnulacion != null) {
			String idMotivoAnulacionSel = idInstitucion + "_" + idMotivoAnulacion.toString();
			motivoAnulacionSel.add(idMotivoAnulacionSel);
		} else {
			idMotivoAnulacion = -1;
		}
		paramidMotivoAnulacion = "{\"idmotivoanulacion\":\""+idMotivoAnulacion+"\"}";

		//Datos solicitante
		idPersona = beanSolicitud.getIdPersona_Des().toString();
		if (idPersona != null) {
			nidSolicitante = (String) request.getAttribute("nidSolicitante");
			nombreSolicitante = (String) request.getAttribute("nombreSolicitante");
			nombreSoloSolicitante = (String) request.getAttribute("nombreSoloSolicitante");
			apellidosSolicitante = (String) request.getAttribute("apellidosSolicitante");
			ncolSolicitante = (String) request.getAttribute("ncolSolicitante");
		}

		if (beanSolicitud.getIdPeticionProducto() != null)
			idPeticion = beanSolicitud.getIdPeticionProducto().toString();

		if (beanSolicitud.getIdInstitucion() != null)
			idInstitucionCertificado = beanSolicitud.getIdInstitucion().toString();

		institucionFinal = idInstitucionCertificado; //Revisar
		idsTemp = fechaSolicitud + "||" + numSolicitud + "||" + tipoCertificado + "||" + idTipoProducto + "||" + idProducto + "||"
				+ idProductoInstitucion + "||" + idInstitucionCertificado + "||" + idPersona + "||" + institucionFinal;
	}

	String sAbreviaturaInstitucionOrigen = "";
	String sAbreviaturaInstitucionDestino = "";

	if (beanInstitucionOrigen != null) {
		sAbreviaturaInstitucionOrigen = beanInstitucionOrigen.getAbreviatura();
	}

	if (beanInstitucionDestino != null) {
		sAbreviaturaInstitucionDestino = beanInstitucionDestino.getAbreviatura();
	}

	// Las siguientes variables controlaran que se pueda o no Facturar y Anular. Ademas, cuando el control de facturas este activo, el campo 'Siguiente estado' para 'Aprobado' siempre sera 'Finalizado'
	String controlFacturasSII = (String) request.getAttribute("controlFacturasSII");
	String hayFacturacionHoy = (String) request.getAttribute("hayFacturacionHoy");
	String facturado = (String) request.getAttribute("facturado");

	String botones = "";
	if (modo.equalsIgnoreCase("ver")) {
		//Modo consulta
		botones = "V";
	} else if (modo.equalsIgnoreCase("nuevo")) {
		//Nuevo certificado
		botones = "V,G";
	} else {
		//Modo edicion, se filtra por estado
		if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND)) {
			//Pendiente de aprobar
			botones = "V,DSOL,AG,G";
		} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO)) {
			//Aprobado
			botones = "V,RG,AN,F,G";
		} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR)) {
			//Pendiente de facturar
			if (controlFacturasSII.equalsIgnoreCase("1") && !hayFacturacionHoy.equalsIgnoreCase("1")) {
				botones = "V,RG,AN,G"; //Si hay control de facturas por SII y hoy no toca facturar (tipicamente el ultimo dia del mes), entonces no se puede facturar
			} else {
				botones = "V,RG,AN,FAC,G";
			}
		} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO)) {
			//Finalizado
			if (controlFacturasSII.equalsIgnoreCase("1") && !hayFacturacionHoy.equalsIgnoreCase("1") && facturado.equalsIgnoreCase("1")) { //Si tiene factura
				botones = "V,RG,G"; //Si hay control de facturas por SII y hoy no toca facturar (tipicamente el ultimo dia del mes), entonces no se puede anular (ya que en este caso, la anulacion implica la emision de una factura rectificativa)
			} else {
				botones = "V,RG,AN,G";
			}
		} else {
			botones = "V";
		}
	}

	String tipoBox = "box";
	String tipoCombo = "boxConsulta";
	String stLectura = "false";
	String readOnlyCertificado = "true";
	boolean modoEditar = true;
	String deshabilitaInfo = "";
	String deshabilitaCobro = "", deshabilitaMutualidad = "";
	boolean camposDeshabilitaCobro = false;
	String deshabilitaDescarga = "";

	if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_DENEGADO)
			|| idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_ANULADO) || modo.equalsIgnoreCase("ver")) {
		tipoBox = "boxConsulta";
		stLectura = "true";
		modoEditar = false;
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "disabled";
		deshabilitaInfo = "disabled";
		deshabilitaMutualidad = "disabled";
		modificarSolicitud = "0";

	} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND)) {
		deshabilitaDescarga = "disabled";
		if (!facturable)
			deshabilitaCobro = "disabled";

	} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO)) {
		deshabilitaDescarga = "disabled";
		if (!facturable)
			deshabilitaCobro = "disabled";

	} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR)) {
		tipoBox = "boxConsulta";
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "disabled";
		deshabilitaMutualidad = "disabled";

	} else if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO)) {
		tipoBox = "boxConsulta";
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "disabled";
		deshabilitaMutualidad = "disabled";
	}
	if(bSolicitudTelematica){
		deshabilitaCobro = "disabled";
	}

	if (modo.equalsIgnoreCase("nuevo")) {
		readOnlyCertificado = "false";
		tipoCombo = "boxCombo";
	}

	String idInstitucionSol = UtilidadesString.createJsonString("idinstitucion", idInstitucionSolicitud);
	String sReadOnly = stLectura;
	String paramInstitucion[] = { idInstitucion };

	String nombreUltimoUsuMod = (String) request.getAttribute("nombreUltimoUsuMod");
	if (nombreUltimoUsuMod == null)
		nombreUltimoUsuMod = new String("");

	String nombreUsuCreacion = (String) request.getAttribute("nombreUsuCreacion");
	if (nombreUsuCreacion == null)
		nombreUsuCreacion = new String("");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<!-- Incluido jquery en siga.js -->
	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

	<style type="text/css">
		.ui-dialog-titlebar-close {
			  visibility: hidden;
		}
		td{
			padding-top: .3em;
			height: 27px;
		}
	</style>
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	jQuery.noConflict();
		function refrescarLocal() {
			sub();
			CerDetalleSolicitudForm.target="mainPestanas";
			CerDetalleSolicitudForm.modo.value="editar";
			CerDetalleSolicitudForm.submit();
		}
		
		function refrescarLocalDescarga() {
			sub();
			CerDetalleSolicitudForm.target="mainPestanas";
			CerDetalleSolicitudForm.modo.value="editarDescarga";
			CerDetalleSolicitudForm.submit();
		}
	
		// Asociada al boton Cerrar
		function accionVolver() 
		{		
			sub();
			CerDetalleSolicitudForm.action = "/SIGA/CER_GestionSolicitudes.do";
			CerDetalleSolicitudForm.target = "mainWorkArea";
			CerDetalleSolicitudForm.modo.value="volver";
			CerDetalleSolicitudForm.submit();
		}
		
		// Asociada al boton Cerrar
		function accionDenegarSolicitud() 
		{		
			sub();
			if(confirm('<siga:Idioma key="facturacion.seleccionSerie.literal.denegarCertificado"/>')) { 
				CerDetalleSolicitudForm.modo.value="denegar";
				CerDetalleSolicitudForm.submit();
		   		fin();
			} else {
				fin();
				return false;
			}	
					
		}		
		
		// Asociada al boton Cerrar
		function accionAprobarGenerar() 
		{	
			sub();
			CerDetalleSolicitudForm.modo.value="comprobarNumPlantillas";
			CerDetalleSolicitudForm.submit();
		}			
		
		// Asociada al boton Cerrar
		function accionRegenerar() 
		{		
			sub();
			CerDetalleSolicitudForm.modo.value="comprobarNumPlantillas";
			CerDetalleSolicitudForm.regenerar.value="1";
			CerDetalleSolicitudForm.submit();
		}			
		
		function accionAnular() {
			jQuery("#divAnulacion").dialog(
			{
			  minHeight: 0,
		      width: 700,
		      modal: true,
		      resizable: false,
		      position: ['center',20] ,
		      scroll: true,
		      open: function(event, ui) { 
						jQuery(".liTab:visible").first().find('a').trigger('click');
						jQuery(".liTab:visible").blur();
						jQuery(ventanaDialogo).css('top','2px');
				},
		      buttons :  { 
		    	     "btGuardarCerrar" : {
		    	         text: "Guardar y cerrar",
		    	         id: "btGuardarYCerrar",
		    	         click: function(){
			 			   		CerDetalleSolicitudForm.modo.value = "anular";
			 			   		CerDetalleSolicitudForm.idMotivoAnulacion.value = jQuery("select#idMotivoAnulacionNuevo option:selected").val();
						   		CerDetalleSolicitudForm.submit();
								jQuery( this ).dialog( "close" );
		    	        	 }   
		    	      } ,
		    	      "btCerrar" : {
		    	         text: "Cerrar",
		    	         id: "btCerrar",
		    	         click: function(){
		    	        	jQuery( this ).dialog( "close" );
		    	         }   
			    	  } 
		    	   }		      
		    });
			
	 		jQuery(".ui-widget-overlay").css("opacity","0");
	 		jQuery(".ui-tabs .ui-tabs-panel").css("padding",'0 2 0 2');
	 		
	 		if(guardar==0){
	 			jQuery('#btGuardarYCerrar').hide() ;
	 		}
	 		if((jQuery(window).height()-150)<jQuery(ventanaDialogo).height()){
	 			jQuery(ventanaDialogo).height(jQuery(window).height()-150);
	 		}
		}
		
		function accionFinalizar() { 
			sub();
			
			if(confirm('<siga:Idioma key="facturacion.seleccionSerie.literal.finalizarCertificado"/>')) { 
			   	CerDetalleSolicitudForm.modo.value = "finalizar";
			   	CerDetalleSolicitudForm.submit();
			} else {
				fin();
				return false;
			}	
		}	
		
		function accionFacturarSel() { 
			if(confirm('<siga:Idioma key="facturacion.seleccionSerie.literal.facturacionRapidaCertificado"/>')) {
				sub();
				
			    var idInstitucion = CerDetalleSolicitudForm.idInstitucion;		          		
			    var idSolicitud = CerDetalleSolicitudForm.idSolicitud;
			    var idPersona = CerDetalleSolicitudForm.idPersonaSolicitante;
			    var idProducto = CerDetalleSolicitudForm.idProducto;
			    var idTipoProducto = CerDetalleSolicitudForm.idTipoProducto;
			    
			    jQuery.ajax({ 
					type: "POST",
					url: "/SIGA/CER_DetalleSolicitud.do?modo=getAjaxSeleccionSerieFacturacion",				
					data: "idInstitucion=" + idInstitucion.value + "&idTipoProducto=" + idTipoProducto.value + "&idProducto=" + idProducto.value + "&idSolicitud=" + idSolicitud.value + "&idPersona=" + idPersona.value,
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){							
							
						// Recupera el identificador de la serie de facturacion
						var idSerieFacturacion = json.idSerieFacturacion;		
						
						if (idSerieFacturacion==null || idSerieFacturacion=='') {
							//jQuery("#selectSeleccionSerieFacturacion")[0].innerHTML = json.aOptionsSeriesFacturacion[0];
							jQuery("#selectSeleccionSerieFacturacion").find("option").detach();
							jQuery("#selectSeleccionSerieFacturacion").append(json.aOptionsSeriesFacturacion[0]);
							
							fin();
							jQuery("#divSeleccionSerieFacturacion").dialog(
								{
									height: 220,
									width: 550,
									modal: true,
									resizable: false,
									buttons: {
										"Guardar": function() {
											sub();
											idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
											if (idSerieFacturacion==null || idSerieFacturacion=='') {
												alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
												fin();
												
											} else {
												jQuery(this).dialog("close");
												CerDetalleSolicitudForm.idSerieSeleccionada.value =  idSerieFacturacion;
												CerDetalleSolicitudForm.modo.value = "facturacionRapida";
											   	CerDetalleSolicitudForm.submit();	
											   	window.setTimeout("fin()",5000,"Javascript");
											}
										},
										"Cerrar": function() {
											jQuery(this).dialog("close");
										}
									}
								}
							);
							jQuery(".ui-widget-overlay").css("opacity","0");														
							
						} else {
							
							CerDetalleSolicitudForm.idSerieSeleccionada.value =  idSerieFacturacion;	
							CerDetalleSolicitudForm.modo.value = "facturacionRapida";
						   	CerDetalleSolicitudForm.submit();		
						   	window.setTimeout("fin()",5000,"Javascript");
						}							
					},
					
					error: function(e){
						alert("Error");
						fin();
					}
				});
			}			   
		}
		
		function copiarSanciones() 
		{		
			CerDetalleSolicitudForm.modo.value="copiarSanciones";
			CerDetalleSolicitudForm.submit();
		}
		
		function copiarHistorico() 
		{		
			CerDetalleSolicitudForm.modo.value="copiarHistorico";
			CerDetalleSolicitudForm.submit();
		}
		
		function historicoObservaciones()
		{
			CerDetalleSolicitudForm.modo.value="historicoObservaciones";
			CerDetalleSolicitudForm.submit();
		} 
		
		function accionGuardar() 
		{	
			sub();
			
			if (CerDetalleSolicitudForm.fechaSolicitud.value==""){
			      alert("Debe introducir una fecha de solicitud");
			      fin();
				  return false;
			    }
			var idInstitucion = CerDetalleSolicitudForm.idInstitucion.value;
			if (CerDetalleSolicitudForm.idInstitucionOrigen.value==""){
		      alert("<siga:Idioma key="messages.certificado.error.noExisteColegioOrigen"/>");
		      fin();
			  return false;
		    }
		  if (CerDetalleSolicitudForm.idInstitucionDestino){	
		  <%if (tipoCertificado != null && tipoCertificado.equals("C")) {%> 
		       if (CerDetalleSolicitudForm.checkCobro.checked){
			         CerDetalleSolicitudForm.idInstitucionDestino.value="";
			  }else{
			  	if(idInstitucion==2000 || idInstitucion.substring(0,2)==30){
				    if (CerDetalleSolicitudForm.idInstitucionDestino.value==""){
				      alert("<siga:Idioma key="messages.certificado.error.noExisteColegioFacturable"/>");
					  fin();
				 	  return false;
				    }
			    }
			  }
		  <%}%>	    	
		  }
		  
		  <%if (modo.equals("nuevo")) {%> 
				// NUEVO
				if(CerDetalleSolicitudForm.idProductoCertificado.value==""){
					var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);
					fin();
					return false;
				}
				
				if(CerDetalleSolicitudForm.idInstitucionOrigen.value==""){
					var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.presentador"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);
					fin();
					return false;
				}
				
				if(CerDetalleSolicitudForm.idPersonaSolicitante.value==""){
					var mensaje = "<siga:Idioma key="gratuita.busquedaEJG.interesado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);
					fin();
					return false;
				}
				
				DummyForm.idProductoCertificado.value = CerDetalleSolicitudForm.idProductoCertificado.value;
				DummyForm.idInstitucionPresentador.value = CerDetalleSolicitudForm.idInstitucionOrigen.value;
				DummyForm.idInstitucion.value = CerDetalleSolicitudForm.idInstitucion.value;
				DummyForm.idPersona.value = CerDetalleSolicitudForm.idPersonaSolicitante.value;
				DummyForm.metodoSolicitud.value = CerDetalleSolicitudForm.metodoSolicitud.value;
				DummyForm.fechaSolicitud.value = CerDetalleSolicitudForm.fechaSolicitud.value;
				DummyForm.idInstitucionColegiacion.value =CerDetalleSolicitudForm.idInstitucionColegiacion.value;
				if(CerDetalleSolicitudForm.aceptaCesionMutualidadCheck != null && CerDetalleSolicitudForm.aceptaCesionMutualidadCheck.checked){
					DummyForm.aceptaCesionMutualidad.value = "1";
				} else {
					DummyForm.aceptaCesionMutualidad.value = "0";
				}
				DummyForm.modo.value="modificar";
				DummyForm.submit();
						  
			
		<%} else {%> 	
			  
			  // MOFIFICAR				  
			  if (CerDetalleSolicitudForm.checkCobro.checked){
				  
				  // LA FECHA COBRO ES OBLIGATORIA SI ESTA MARCADO EL CHECK DE COBRO
				  if(jQuery("#fechaCobro").val() == null || jQuery("#fechaCobro").val() == ""){
				      alert("<siga:Idioma key="messages.certificado.error.fechacobro.obligatorio"/>");
					  fin();
				 	  return false;
				  }
				  
				  fechaActual = getFechaActualDDMMYYYY();
				  
				  // LA FECHA COBRO ES OBLIGATORIA SI ESTA MARCADO EL CHECK DE COBRO
				  if(compararFecha(jQuery("#fechaCobro").val(),fechaActual) == 1){
					  alert("<siga:Idioma key="messages.certificado.error.fechacobro.futuro"/>");
					  fin();
				 	  return false;
				  }				  
				  
				  //SI NO EXISTE EL BANCO PARA EL CODIGO INTRODUCIDO 
				  if(jQuery("#codigoBanco").val()!= "" && (jQuery("#bancoNombre").val() == null || jQuery("#bancoNombre").val() == "")){
					  alert("<siga:Idioma key="messages.certificado.error.banco.obligatorio"/>");
					  fin();
				 	  return false;
				  }					 

				  //NO SE PUEDE RELLENAR EL CAMPO SUCURSAL SIN HABER RELLENADO LA ENTIDAD
				  if(jQuery("#sucursalBanco").val()!= "" && (jQuery("#bancoNombre").val() == null || jQuery("#bancoNombre").val() == "")){
					  alert("<siga:Idioma key="messages.certificado.error.sucursal.nobanco"/>");
					  fin();
				 	  return false;
				  }				 
				  
				  //EL CAMPO SUCURSAL DEBE TENER UN TAMAÑO DE 4
				  if(jQuery("#sucursalBanco").val()!= "" && jQuery("#sucursalBanco").val().length != 4){
					  alert("<siga:Idioma key="messages.certificado.error.sucursal.longitud"/>");
					  fin();
				 	  return false;
				  }	
				  
					
					if(CerDetalleSolicitudForm.idPersonaSolicitante.value==""){
						var mensaje = "<siga:Idioma key="gratuita.busquedaEJG.interesado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
						alert (mensaje);
						fin();
						return false;
					}				  
			  }
				
			  <%if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO)
						|| idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR)
						|| idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO)) {%>
			 	 alertStop("<siga:Idioma key="certificados.solicitudes.literal.msgRegenerar"/>");
			  <%}%>
				CerDetalleSolicitudForm.modo.value="modificar";
				CerDetalleSolicitudForm.submit();
		 <%}%>
		}
		
		function validarCheckDescarga()
		{
			if (!CerDetalleSolicitudForm.checkDescarga.checked)	
			{
				CerDetalleSolicitudForm.fechaDescarga.value="";
				
			}
			else
			{
				var fFecha = new Date();
				var dia=fFecha.getDate();
				var mes=fFecha.getMonth()+1;
				var yea=fFecha.getYear();
				if (dia<10) dia="0"+dia;
				if (mes<10) mes="0"+mes;
								
				CerDetalleSolicitudForm.fechaDescarga.value=dia+"/"+mes+"/"+yea;
			}
		}
		
		function validarCheckCobro()
		{
			if (!CerDetalleSolicitudForm.checkCobro.checked) {
				
				CerDetalleSolicitudForm.fechaCobro.value="";
				<%if (tipoCertificado != null && tipoCertificado.equals("C")) {%> 
					jQuery("#idInstitucionDestino").removeAttr("disabled");
 				<%}%>
				jQuery("#fechaCobro").addClass("boxConsulta").removeClass("box");	
				jQuery("#fechaCobro-datepicker-trigger").hide();
				jQuery("#td_1").hide();
				jQuery("#td_2").hide();
				jQuery("#td_3").hide();
				jQuery("#td_4").hide();
				jQuery("#td_5").hide();
				jQuery("#td_gcob1").show();
				jQuery("#td_gcob2").show();					
				
				<%if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO)) {%>
					jQuery("#siguienteEstado").val("Finalizado");
				<%}%>
			} else {
				<%if (tipoCertificado != null && tipoCertificado.equals("C") && !bSolicitudTelematica) {%> 
					CerDetalleSolicitudForm.idInstitucionDestino.value="";
					jQuery("#idInstitucionDestino").attr("disabled","disabled");
 				<%}%>	  

				jQuery("#td_1").show();
				jQuery("#td_2").show();
				jQuery("#td_3").show();
				jQuery("#td_4").show();
				jQuery("#td_5").show();
				jQuery("#fechaCobro").addClass("box").removeClass("boxConsulta");	
				jQuery("#fechaCobro-datepicker-trigger").show();
				jQuery("#td_gcob1").hide();
				jQuery("#td_gcob2").hide();					
				CerDetalleSolicitudForm.fechaCobro.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
				
				<%if (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO)) {%>
					<%if (controlFacturasSII.equals("1")) {%>
						jQuery("#siguienteEstado").val("Finalizado");
					<%} else {%>
						jQuery("#siguienteEstado").val("Pendiente de Facturar");
					<%}%>
				<%}%>
			}
		}
		
		function cargarBanco(){
			var codigoBanco = jQuery("#codigoBanco").val();
			if (codigoBanco!=undefined && codigoBanco!="") {			
				jQuery.ajax({ //Comunicacion jQuery hacia JSP  
	   				type: "POST",
					url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBanco",
					data: "idBanco="+codigoBanco,
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){	
						if(json.banco!=null && json.banco!=""){
							document.getElementById("bancoNombre").value=json.banco.nombre;
						}
						fin();
					},
					error: function(e){
						alert(mensaje);
						fin();
					}
				});
			} else {
				document.getElementById("bancoNombre").value="";
			}
		}			
		
		function fijarFechaEntregaInfo () 
		{
			if (CerDetalleSolicitudForm.checkInfoAdjunta.checked) {
				CerDetalleSolicitudForm.fechaEntregaInfo.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
			}
			else {
				CerDetalleSolicitudForm.fechaEntregaInfo.value="";
			}
		}
		
		function checkMutualidad () 
		{
			if(CerDetalleSolicitudForm.aceptaCesionMutualidadCheck.checked){
				CerDetalleSolicitudForm.aceptaCesionMutualidad.value = "1";
			} else {
				CerDetalleSolicitudForm.aceptaCesionMutualidad.value = "0";
			}
		}		
		
		function revisarCheck() {
			<%if (!modo.equals("nuevo")) {%> 
				if (CerDetalleSolicitudForm.idInstitucionDestino)
					if (!CerDetalleSolicitudForm.checkCobro.checked){
				     <%if (tipoCertificado != null && tipoCertificado.equals("C")) {%> 
				       jQuery("#idInstitucionDestino").removeAttr("disabled");
 				    <%}%>	  
				} else {
				     <%if (tipoCertificado != null && tipoCertificado.equals("C")) {%> 
					   jQuery("#idInstitucionDestino").attr("disabled","disabled");
  				    <%}%>	  
				}
			<%}%>
		}
		
		function nuevoSolicitante() {
			var resultado=ventaModalGeneral("busquedaCensoModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ) {
				var idInstitucion = <%=idInstitucion%>;
				var idPersona;
				var idInstitucionOrigen;
				var nid;
				var nombre;
				var apellidos;
				
				if (resultado[8]!=undefined && resultado[8] == 'Nuevo') {
					//Se cera desde cero
					idPersona = resultado[0];
					idInstitucionOrigen = resultado[1];
					nid = resultado[3];
					nombre = resultado[4];
					apellidos = resultado[5] + " " +resultado[6];
				} else {
					//Selecciona uno existente
					idPersona = resultado[0];
					idInstitucionOrigen = resultado[5];
					nid = resultado[6];
					nombre = resultado[2];
					apellidos = resultado[3] + " " +resultado[4];
				}				
				CerDetalleSolicitudForm.idPersonaSolicitante.value=idPersona;
				CerDetalleSolicitudForm.nombre.value=nombre + " " + apellidos;
				CerDetalleSolicitudForm.idInstitucionOrigenSolicitante.value=idInstitucionOrigen;
				CerDetalleSolicitudForm.nidSolicitante.value=nid;
				
				comprobarDuplicados(idInstitucion, idPersona, nid, nombre, apellidos, '', '', '');
				// NOTA: es mejor no pasar el numcol, para que asi busque duplicados por todas las colegiaciones
			}
		}
		
		function editarSolicitante() {
		    var idInst = CerDetalleSolicitudForm.idInstitucionOrigenSolicitante.value;		
		    if(idInst == null || idInst ==''){
		    	idInst = CerDetalleSolicitudForm.idInstitucion.value;
		    }
		    var idPers = CerDetalleSolicitudForm.idPersonaSolicitante.value;			    
		    var idLetrado = "1";			
		    
			if (idLetrado.value=='1') {				    
				CerDetalleSolicitudForm.tablaDatosDinamicosD.value=idPers + ',' + idInst + ',LETRADO' + '%';		
			 } else {
				CerDetalleSolicitudForm.tablaDatosDinamicosD.value=idPers + ',' + idInst + '%';		
			 }
		   	CerDetalleSolicitudForm.submit();			   	
		}
		
		function descargarPDF() {
			<%if (descargaPDF != null && descargaPDF.equalsIgnoreCase("1")) {%>
				sub();
				CerDetalleSolicitudForm.descargarCertificado.value="1";
				CerDetalleSolicitudForm.modo.value="descargar";
				CerDetalleSolicitudForm.submit();
			<%}%>
		}		
	</script>
		
	<style>
        .colizq{clear:left;float:left;}
       
        .colsiguientes{float:left;display:block;}
	</style>	
		
		
<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onLoad="ajusteAltoBotones('mainWorkArea');revisarCheck();descargarPDF();comprobarDuplicados('<%=idInstitucion%>', '<%=idPersona%>', '<%=nidSolicitante%>', '<%=nombreSoloSolicitante%>', '<%=apellidosSolicitante%>', '', '', '');" >

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma
				key="certificados.solicitudes.ventanaEdicion.titulo" /></td>
		</tr>
	</table>
		
	<div id="camposRegistro" style="overflow-x:hidden; overflow-y:auto">
	
	<div id="divSeleccionSerieFacturacion" title="<siga:Idioma key='facturacion.seleccionSerie.titulo'/>" style="display:none">
		<table align="left">
			<tr>		
				<td class="labelText" nowrap>
					<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.serieFacturacion"/>&nbsp;(*)
				</td>
				<td>
					<select class='box' style='width:270px' id='selectSeleccionSerieFacturacion'>
					</select>
				</td>
			</tr>				
							
			<tr>
				<td class="labelTextValue" colspan="2">
					<siga:Idioma key="pys.gestionSolicitudes.aviso.seriesFacturacionMostradas"/>
				</td>
			</tr>					
		</table>			
	</div>	
		
	<html:form action="/CER_DetalleSolicitud.do" method="POST" target="submitArea">
		<html:hidden property="modo" value="" />
		<html:hidden property="idInstitucion" styleId="idInstitucion" value="<%=idInstitucion%>" />
		<html:hidden property="idInstitucionSolicitud" value="<%=idInstitucionSolicitud%>" />
		<html:hidden property="buscarIdPeticionCompra" value="<%=sIdCompra%>" />
			
		<html:hidden property="idSolicitud" styleId="idSolicitud" value="<%=numSolicitud%>" />
		<html:hidden property="idTipoProducto" styleId="idTipoProducto" value="<%=idTipoProducto%>" />
		<html:hidden property="idProducto" styleId="idProducto" value="<%=idProducto%>" />
		<html:hidden property="idProductoInstitucion" styleId="idProductoInstitucion" value="<%=idProductoInstitucion%>" />
		<html:hidden property="tipoCertificado" value="<%=tipoCertificado%>" />
		<html:hidden property="idMotivoAnulacion" value="" />
		<html:hidden property="idsTemp" value="<%=idsTemp%>"/>
		<html:hidden property="idSerieSeleccionada" styleId="idSerieSeleccionada" />
		<html:hidden property="regenerar" value="" />
		<input type="hidden" id="idPeticion" name="idPeticion" value="<%=idPeticion%>">	
		<input type="hidden" id="descargarCertificado" name="descargarCertificado" value="">	
		
		<html:hidden property="aceptaCesionMutualidad" value="<%=aceptMut%>" />
		<tr>
			<td><siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.datosSolicitud" desplegable="true">
				<table class="tablaCampos" align="center">
					<tr>
						<td width="15%" class="labelText"><siga:Idioma key="certificados.mantenimiento.literal.productoCertificado" />&nbsp;(*)</td>
						<td width="17%"><siga:ComboBD nombre="idProductoCertificado" tipo="cmbCertificadosOrdinadios" clase="<%=tipoCombo%>" parametro="<%=paramInstitucion%>" obligatorio="true" elementoSel="<%=tipoCertSel%>" readonly="<%=readOnlyCertificado%>"/></td>				
					
						<td width="15%" class="labelText">
							<siga:Idioma key="certificados.solicitudes.literal.numeroSolicitud"/> / 
							<siga:Idioma key="certificados.solicitudes.literal.idSolicitudCompra"/>
						</td>
						<td width="17%" class="labelTextvalue">
							<b><%=UtilidadesString.mostrarDatoJSP(numSolicitud)%></b> / 
							<%=UtilidadesString.mostrarDatoJSP(sIdCompra)%>
						</td>
						
						<td width="15%" class="labelText"><siga:Idioma key="certificados.solicitudes.literal.numeroCertificado" /></td>
						<td width="17%"><b><%=UtilidadesString.mostrarDatoJSP(codigo)%></b></td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechaSolicitud" />&nbsp;(*)</td>
						<td>
						<%
							if (modificarSolicitud.equals("1") && !bSolicitudTelematica
								&& (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND) || 
									idEstadoSolicitud.equals(""	+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_APROBADO))) {
						%> 
						<%
 								if (modo.equals("nuevo")) {
 									SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
 									Date date = new Date();
 									String fechaSol = sdf.format(date);
 						%>
							<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSol%>" />&nbsp;

						<%
								} else if (modoEditar) {
									SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
									Date date = new Date(fechaSolicitud);
									String fechaSol = sdf.format(date);
						%>
							<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSol%>" />&nbsp;

						<%
								} else {
						%>
							<html:text	name="SolicitudesCertificadosForm" style="width:80px"
										property="fechaSolicitud" styleClass="boxConsulta" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), fechaSolicitud)%>">
							</html:text>
						<%
								}
						%>
						<%
							} else {
						%>
							<html:text  name="SolicitudesCertificadosForm" style="width:80px"
										property="fechaSolicitud" styleClass="boxConsulta" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), fechaSolicitud)%>">
							</html:text>
						<%
							}
						%>
						</td>
						
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.colegioOrigen" />&nbsp;(*) </td>
						<td>
							<%
								if (!modificarSolicitud.equals("1") || bSolicitudTelematica)
											sReadOnly = "true";
							%>
							<siga:Select id="idInstitucionOrigen" 
									queryId="<%=consultaOrigen%>" 
									selectedIds="<%=idInstitucionPresentador%>"
									params="<%=idInstitucionSol%>"
									readonly="<%=sReadOnly%>"
									required="true" />					
						</td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.metodoSolicitud"/></td>
						
						<%if (!bSolicitudTelematica) {%> 
							<td>
								<siga:Select id="metodoSolicitud" queryId="getMetodosSolicitudNew" selectedIds="<%=aMetodoSol%>" readonly="<%=stLectura%>"/>
							</td>	
						<%} else {%>
							<td>
								<siga:Select id="metodoSolicitud" queryId="getMetodosSolicitud" selectedIds="<%=aMetodoSol%>" readonly="true"/>
							</td>
						<%}%>
										
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.colegiadoen"/></td>	
						<td >
							<%
								sReadOnly = stLectura;
								if (!modificarSolicitud.equals("1") || bSolicitudTelematica)
									sReadOnly = "true";
							%>
							<siga:Select id="idInstitucionColegiacion" 
									queryId="<%=consultaOrigen%>" 
									selectedIds="<%=idInstitucionColegiacion%>"
									params="<%=idInstitucionSol%>"
									readonly="<%=sReadOnly%>"/>
						</td>
						
						<td colspan="2" rowspan="2">
							<%
								if (modificarSolicitud.equals("1")) {
							%> 
								<i><siga:Idioma key="pys.solicitudCompra.literal.indicacion"/></i>
							<%
								} else {
							%> 						
								&nbsp;
							<%
								}
							%>							
						</td>
					</tr>
					

					<%
						if (!esCompatibleConCertificadosExistentes) {
					%>					
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.motivo" />&nbsp;(*)</td>
						<td colspan="3"><siga:Select id="idMotivoSolicitud" queryId="getMotivosSolicitud" queryParamId="idmotivosolicitud" params="<%=paramidMotivoSolicitud%>" selectedIds="<%=motivoSolicitudSel%>" readonly="<%=stLectura%>" width="100%"/></td>
					</tr>
					<%
						}
					%>
					
					<%if(bSolicitudTelematica){ %>					
						<tr>
							<td class="labelText" style="width:150px"><siga:Idioma key="certificados.solicitudes.literal.estadoIncorporacion" /></td>
							<td class="labelTextvalue" style="width:80px"><%=UtilidadesString.mostrarDatoJSP(estadoInc)%></td>

					<%
						if (pintarCheckMutualidad) {
					%>					
					<tr>
						<td class="labelText" colspan="6">
							<% if (bSolicitudTelematica) { %>
								<div style="float:left"><input disabled="disabled" type=checkbox name="aceptaCesionMutualidadCheck" <%=aceptaCesion %> <%=deshabilitaMutualidad%> onclick="checkMutualidad()" /></div>&nbsp;		
							<%}else{%>
								<div style="float:left"><input type=checkbox name="aceptaCesionMutualidadCheck" <%=aceptaCesion %> <%=deshabilitaMutualidad%> onclick="checkMutualidad()" /></div>&nbsp;
							<% } %> 
							<siga:Idioma key="certificados.solicitudes.literal.textoConformidad" />
						</td>					
					</tr>
					<%
						}
					%>
				</table>
			</siga:ConjCampos> 
			
			<siga:ConjCampos leyenda="gratuita.busquedaEJG.interesado">
				<table class="tablaCampos" align="center" border="0">
					<tr>
						<td class="labelText" width="60px">
							<siga:Idioma key="expedientes.auditoria.literal.nid"/>
						</td>				
						<td>				
							<html:text name="SolicitudesCertificadosForm" size="12" property="nidSolicitante" styleId="nidSolicitante" styleClass="boxConsulta" readonly="true" style="width:80px" value="<%=nidSolicitante%>"/>
						</td>
						<td class="labelText" width="60px">
							<html:hidden name="SolicitudesCertificadosForm" property = "idPersonaSolicitante" styleId="idPersonaSolicitante" value="<%=idPersona%>"/>
							<html:hidden name="SolicitudesCertificadosForm" property = "idInstitucionOrigenSolicitante" styleId="idInstitucionOrigenSolicitante"/>
							
							<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
						</td>				

						<td>
							<html:text name="SolicitudesCertificadosForm" property="nombre" styleId="nombre" size="20" styleClass="boxConsulta" readonly="true" style="width:500px" value="<%=nombreSolicitante%>"/>																								 				 																										
						</td>		

						<td align="right">	
						<%
							if (modo.equals("nuevo")) {
						%>			
							<img id="iconoboton_newSol_1"  src="/SIGA/html/imagenes/icono+.gif" style="cursor: hand;" alt="Nuevo Solicitante"  name="newSol_1"  border="0" onClick="nuevoSolicitante();"> 
						<%
						 	} else if (modoEditar 
						 			&& !idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO)
 									&& !idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR)) {
						%>			
							<img id="iconoboton_editSol_1" src="/SIGA/html/imagenes/bseleccionar_on.gif" style="cursor: hand;" alt="Editar Solicitante" name="editSol_1" border="0" onClick="editarSolicitante();">			
						<%
							}
						%>
							<img id="iconoboton_cargando_1"	src="/SIGA/html/imagenes/bloading_on_23.gif"	style="cursor: hand; display: none" alt="Cargando posibles duplicados"> 
							<img id="iconoboton_aviso_1"	src="/SIGA/html/imagenes/warning.png"			style="cursor: hand; display: none" alt="Duplicidades" onClick="accionObtenerDuplicados();"> 
						</td>
					</tr>
				</table>		
			</siga:ConjCampos>			
			
		<%
			if (!modo.equals("nuevo")) {
		%> 
		
			<siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.sanciones" desplegable="true">
				<table class="tablaCampos" align="center" border="0" cellspacing="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="certificados.solicitudes.literal.fechaEmision" />&nbsp;&nbsp;
		<%
				if (modificarSolicitud.equals("1")) {
		%> 
		<%
						if (modoEditar) {
							String fechaEm = "";
							if (beanSolicitud != null && !beanSolicitud.getFechaEmisionCertificado().equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
								Date date = new Date(beanSolicitud.getFechaEmisionCertificado());
								fechaEm = sdf.format(date);
							}
		%>
							<siga:Fecha nombreCampo="fechaEmision" valorInicial="<%=fechaEm%>" />&nbsp;
		<%
					} else {
		%>
							<html:text	name="SolicitudesCertificadosForm" style="width:80px"
										property="fechaEmision" styleClass="<%=tipoBox%>" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEmisionCertificado())%>">
							</html:text>
		<%
					}
		%>
		<%
				} else {
		%>
							<html:text  name="SolicitudesCertificadosForm" style="width:80px" 
										property="fechaEmision" styleClass="boxConsulta" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEmisionCertificado())%>">
							</html:text> 
		<%
					}
		%>
						</td>	
						
						<td>&nbsp;</td>
						
						<td class="labelText" nowrap>
							<siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirDeudas" />&nbsp;&nbsp;
							<html:checkbox property="incluirDeudas" disabled="<%=!modoEditar%>"/>
						</td>

						<td class="labelText" nowrap>
							<siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirSanciones" />&nbsp;&nbsp;
							<html:checkbox property="incluirSanciones" disabled="<%=!modoEditar%>"/>
						</td>
					</tr>
					
		<%
				if (isSolicitudColegio) {
		%>
						
					<tr>
						<td width="25%">&nbsp;</td>
						<td width="25">&nbsp;</td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.textoSanciones" /></td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.comentario" /></td>
					</tr>
					
					<tr>							
						<td>
							<siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.estadosColegiales">
								<table class="tablaCampos" align="center" border="0" cellspacing="0">
									<tr>
										<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirLiteratura" />
										<html:checkbox property="incluirLiteratura" disabled="<%=!modoEditar%>"/></td>
										<td></td>
									</tr>
									<tr>
										<td class="labelText">
		<%
					if (modificarSolicitud.equals("1")) {
		%> 
											<input  type="button" width="200"
													alt="<siga:Idioma key="certificados.solicitudes.literal.copiarHistorico"/>"
													id="enviarSel" onclick="return copiarHistorico();"
													name="idButton" class="button"
													value="<siga:Idioma key="certificados.solicitudes.literal.copiarHistorico"/>">
		<%
					} else {
		%> 
											&nbsp; 
		<%
					}
		%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
		<%
					if (modificarSolicitud.equals("1")) {
		%>
											<input  type="button" alt="<siga:Idioma key="certificados.solicitudes.literal.historicoObservaciones"/>"
													id="enviarSel" onclick="return historicoObservaciones();"
													name="idButton" class="button" width="100"
													value="<siga:Idioma key="certificados.solicitudes.literal.CopiarHistObserv"/>">
		<%
					} else {
		%>
											&nbsp;
		<%
					}
		%>
										</td>
	
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
						
						<td>&nbsp;</td>

						<td rowspan="2">
		<%
					if (modificarSolicitud.equals("1")) {
		%> 
							<html:textarea  property="textoSanciones" value="" styleClass="box"
								onKeyDown="cuenta(this,3500)" cols="100" rows="7" style="width:350px" 
								value="<%=sanciones%>" readonly="false" /> 
							
		<%
					} else {
		%> 
						 	<html:textarea	property="textoSanciones" value="" styleClass="boxConsulta"
								onKeyDown="cuenta(this,3500)" cols="100" rows="7" style="width:350px" 
								value="<%=sanciones%>" readonly="true" /> <%
					}
		%>
						</td>
					
						<td rowspan="2">
		<%
					if (modificarSolicitud.equals("1")) {
		%>
							<html:textarea	property="comentario" value="" styleClass="box" style="width:350px" 
								onKeyDown="cuenta(this,4000)" cols="100" rows="7"
								value="<%=beanSolicitud.getComentario()%>" readonly="false" /> <%
					} else {
		%>
							<html:textarea property="comentario" value="" style="width:350px" 
								styleClass="boxConsulta" onKeyDown="cuenta(this,4000)" cols="100"
								rows="7" value="<%=beanSolicitud.getComentario()%>"
								readonly="true" />
		<%
					}
		%>
						</td>

					</tr>
				
		<%
				} else { //no es solicitud del colegio
		%> 
				
					<tr>
						<td class="labelText" colspan="4"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.textoSanciones" /></td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.comentario" /></td>
					</tr>						

					<tr>
						<td colspan="4">
		<%
					if (modificarSolicitud.equals("1")) {
		%> 
							<html:textarea property="textoSanciones" value="" styleClass="box"
								onKeyDown="cuenta(this,3500)" cols="100" rows="4" style="width:700px" 
								value="<%=sanciones%>" readonly="false" /> 
							
		<%
					} else {
		%> 
						 	<html:textarea	property="textoSanciones" value="" styleClass="boxConsulta"
								onKeyDown="cuenta(this,3500)" cols="100" rows="4" style="width:700px" 
								value="<%=sanciones%>" readonly="true" />
		<%
					}
		%>
						</td>			
										
						<td>
		<%
					if (modificarSolicitud.equals("1")) {
		%> 
							<html:textarea	property="comentario" value="" styleClass="box" style="width:250px" 
								onKeyDown="cuenta(this,4000)" cols="100" rows="4"
								value="<%=beanSolicitud.getComentario()%>" readonly="false" /> 
						
		<%
					} else {
		%>
							<html:textarea property="comentario" value="" style="width:250px" 
								styleClass="boxConsulta" onKeyDown="cuenta(this,4000)" cols="100"
								rows="4" value="<%=beanSolicitud.getComentario()%>"
								readonly="true" /> 
		<%
					}
		%>
						</td>
					</tr>
		<%
				}
		%>
				</table>
			</siga:ConjCampos>
				
			<siga:ConjCampos leyenda="certificados.solicitudes.literal.cobro">
				<table class="tablaCampos" align="center" border="0">		
					<tr>
						<td class="labelText" width="180px">
							<siga:Idioma key="certificados.solicitudes.literal.cobrado"/>&nbsp; &nbsp; 
							
							<input type=checkbox name="checkCobro" onclick="validarCheckCobro();" <%=deshabilitaCobro%>
								<%=(beanSolicitud.getFechaCobro() != null && !beanSolicitud.getFechaCobro().trim().equals("")) ? "checked" : ""%>>
						</td>
						<td>	
							<%
									if (modoEditar && !idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO) && !bSolicitudTelematica) {
													String fechaCo = "";
													if (!beanSolicitud.getFechaCobro().equals("")) {
														SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
														Date date = new Date(beanSolicitud.getFechaCobro());
														fechaCo = sdf.format(date);
													}
								%>		
								<siga:Fecha nombreCampo="fechaCobro" valorInicial="<%=fechaCo%>" />
							
							<%
																} else {
															%>
								<html:text name="SolicitudesCertificadosForm" styleId="fechaCobro"
									property="fechaCobro" styleClass="boxConsulta" readonly="true"
									value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaCobro())%>"
									size="10"></html:text>
							<%
								}
							%>	
								
							<script type="text/javascript">	
								jQuery(document).ready(function () {		
									if (!CerDetalleSolicitudForm.checkCobro.checked){
										jQuery("#fechaCobro").addClass("boxConsulta").removeClass("box");	
										jQuery("#fechaCobro-datepicker-trigger").hide();
										jQuery("#td_1").hide();
										jQuery("#td_2").hide();
										jQuery("#td_3").hide();
										jQuery("#td_4").hide();
										jQuery("#td_5").hide();
										jQuery("#td_gcob1").show();
										jQuery("#td_gcob2").show();												
									} else{
										jQuery("#td_1").show();
										jQuery("#td_2").show();
										jQuery("#td_3").show();
										jQuery("#td_4").show();
										jQuery("#td_5").show();
										jQuery("#td_gcob1").hide();
										jQuery("#td_gcob2").hide();												
									}
								});
							</script>	
						</td>
						
						<td class="labelText"  id="td_1">
							<siga:Idioma key="administracion.multidioma.catalogosMaestros.literal.entidad" />
						</td>
						
						<td  id="td_2">
							<html:text size="2"  maxlength="4"  name="SolicitudesCertificadosForm" property="codigoBanco" styleId="codigoBanco" value="<%=beanSolicitud.getCbo_codigo()%>"  readonly="<%=camposDeshabilitaCobro%>" styleClass="<%=tipoBox%>" onblur="cargarBanco();"></html:text> 
						</td>
							<script type="text/javascript">
								jQuery("#codigoBanco").keypress(function (e) {
										if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				           				return false;
									});
							</script>

						<td class="labelText"  id="td_3">
							<html:text style="width:250px;" name="SolicitudesCertificadosForm" property="bancoNombre"  styleId="bancoNombre" styleClass="boxConsulta" readonly="true"/>
						</td>
							<script type="text/javascript">	
								cargarBanco();
							</script>

						<td class="labelText"  id="td_4">
							<siga:Idioma key="facturacion.cuentasBancarias.sucursalBanco" />
						</td>
						
						<td  id="td_5"> 
							<html:text size="2"  maxlength="4"  name="SolicitudesCertificadosForm" property="sucursalBanco" styleId="sucursalBanco" value="<%=beanSolicitud.getCodigo_sucursal()%>"  readonly="<%=camposDeshabilitaCobro%>" styleClass="<%=tipoBox%>"></html:text> 
						</td>
						<script type="text/javascript">
							jQuery("#sucursalBanco").keypress(function (e) {
									if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
			           				return false;
								});
						</script>							


						<td class="labelText" id="td_gcob1">
						<%
							if (tipoCertificado != null && tipoCertificado.equals("C")) {
						%> 
							<siga:Idioma key="certificados.solicitudes.literal.facturableA" /> 
						<%
								} else {
							%> 
							<siga:Idioma key="certificados.solicitudes.literal.colegioDestino" /> 
						<%
								}
							%>
						</td>
						<td id="td_gcob2">
							<%
								if (!modificarSolicitud.equals("1") || (idEstadoSolicitud.equals("" + CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO)))
												sReadOnly = "true";
							%>
							<siga:Select id="idInstitucionDestino"
									queryId="<%=consultaDestino%>"
									selectedIds="<%=idInstitucionDestino%>"
									params="<%=idInstitucionSol%>"
									readonly="<%=sReadOnly%>"/>
						</td>
					</tr>
				</table>
			</siga:ConjCampos> 					
			
			<siga:ConjCampos leyenda="certificados.solicitudes.literal.otrasAcciones">
				<table class="tablaCampos" align="center" border="0" width="100%">
					<tr>
						<td width="10%" class="labelText"><siga:Idioma key="certificados.solicitudes.literal.descargado" /></td>
						<td width="3%">
							<input type=checkbox name="checkDescarga" onclick="validarCheckDescarga();" <%=deshabilitaDescarga%>
							<%=(beanSolicitud.getFechaDescarga() != null && !beanSolicitud.getFechaDescarga().trim().equals("")) ? "checked" : ""%>>
						</td>
						<td width="15%">
							<html:text name="SolicitudesCertificadosForm" property="fechaDescarga" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaDescarga())%>" size="7"></html:text>
						</td>
						
						<td width="10%" class="labelText"><siga:Idioma key="certificados.solicitudes.literal.enviado" /></td>
						<td width="3%">
							<input type=checkbox name="checkEnvio" disabled
								<%=(beanSolicitud.getFechaEnvio() != null && !beanSolicitud.getFechaEnvio().trim().equals("")) ? "checked" : ""%>> 
						</td>
						<td width="15%">
							<html:text name="SolicitudesCertificadosForm" property="fechaEnvio" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEnvio())%>" size="10"></html:text>
						</td>
						
						<td width="15%" class="labelText"><siga:Idioma key="certificados.solicitudes.literal.entregadaInfoAdjunta" /> </td>
						<td width="3%">
							<input type=checkbox name="checkInfoAdjunta" onclick="fijarFechaEntregaInfo();" <%=deshabilitaInfo%>
								<%=(beanSolicitud.getFechaEntregaInfo() != null && !beanSolicitud.getFechaEntregaInfo().trim().equals("")) ? "checked" : ""%>>
						</td>
						<td width="15%">
							<html:text name="SolicitudesCertificadosForm" property="fechaEntregaInfo" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEntregaInfo())%>" size="10"></html:text>
						</td>
						<td width="11%">
						</td>
					</tr>
				</table>
			</siga:ConjCampos> 
			
			<siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.gestionSolicitud" desplegable="true">
				<table class="tablaCampos" align="center" border="0" width="100%">	
					<tr>
						<td class="labelText" width="20%">
							<siga:Idioma key="certificados.solicitudes.literal.estadoActual" />
						</td>				
						<td class="labelText" width="20%" colspan="2">
							<b><%=strEstadoSolicitud%></b>
						</td>
						<td class="labelText" width="35%">
							&nbsp;
						</td>
						<td align="right" width="25%">
							<i>[
							<siga:Idioma key="certificados.solicitudes.literal.estadoSiguiente" />:
							<%=strSiguienteEstado%>
							]</i>
						</td>
					</tr>
					
					<%
						if (idMotivoAnulacion != null && !idMotivoAnulacion.equals(new Integer(-1))) {
					%>					
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.motivo.anulacion" /></td>
						<td class="labelText" colspan="3"><siga:Select id="idMotivoAnulacion" width="100%" queryId="getMotivosAnulacion" queryParamId="idmotivoanulacion" params="<%=paramidMotivoAnulacion%>" selectedIds="<%=motivoAnulacionSel%>" readOnly="true"/></td>
					</tr>
					<%
						}
					%>
					
					<tr>
						<td class="labelText" width="20%">
							<siga:Idioma key="certificados.solicitudes.literal.fechaCreacion" />
						</td>				
						<td class="labelText" width="15%">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateLong(userBean.getLanguage(), beanSolicitud.getFechaCreacion()))%>
						</td>							
						<td class="labelText" width="5%">
							-
						</td>
						<td class="labelText" width="35%">
							<%=nombreUsuCreacion%>
						</td>
						<td class="labelText" width="25%">
							&nbsp;
						</td>
					</tr>
																	
					<tr>
						<td class="labelText" >
							<siga:Idioma key="certificados.solicitudes.literal.ultimaFechaMod" />
						</td>				
						<td class="labelText">
							<span class="boxConsulta"> <%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateLong(userBean.getLanguage(), beanSolicitud.getFechaMod()))%></span>
						</td>							
						<td class="labelText">
							-
						</td>
						<td class="labelText">
							<span class="boxConsulta"><%=nombreUltimoUsuMod%></span>
						</td>
						<td class="labelText">
							&nbsp;
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			<%
				}
			%>
			</td>							
		</tr>
		
	</html:form>
	</div>

	<div id="divAnulacion" class="divModal" style="display:none" title="Anulación">
		<fieldset>
		<div style='width:98%;'>
			<div class="labelText editLabel">
				<siga:Idioma key="certificados.solicitudes.literal.motivo.anulacion"/>
				<siga:Select id="idMotivoAnulacionNuevo" width="80%" queryId="getMotivosAnulacion" queryParamId="idmotivoanulacion" params="<%=paramidMotivoAnulacion%>"/>
				<p>&nbsp;</p>
			</div>
		</div>
		</fieldset>
	</div>
	
	<siga:ConjBotonesAccion botones="<%=botones%>" ordenar="false"/>

	<!-- Formulario para la búsqueda de clientes -->
	<html:form action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea">
		<html:hidden styleId="modo" property="modo" value="editar"/>
		<html:hidden styleId="filaSelD" property="filaSelD"/>
		<html:hidden styleId="tablaDatosDinamicosD" property="tablaDatosDinamicosD" value="ver"/>
	</html:form>
	
	<html:form action="/CEN_BusquedaCensoModal" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="1">
		<input type="hidden" name="modo" value="designarArt27">
		<input type="hidden" name="numIdentificacion">
		<input type="hidden" name="colegiadoen">
		<input type="hidden" name="idDireccion">
	</html:form>	
	
	<html:form action="/PYS_CompraPredefinida" method="POST" target="mainWorkArea">
		<input type="hidden" name="modo">
		<input type="hidden" name="idPersona" value ="${CerDetalleSolicitudForm.idPersonaSolicitante}">
		<input type="hidden" name="idInstitucion" value ="${CerDetalleSolicitudForm.idInstitucion}">
		<input type="hidden" name="idInstitucionPresentador" value="${CerDetalleSolicitudForm.idInstitucionOrigen}">
		<input type="hidden" name="idProductoCertificado" value="${CerDetalleSolicitudForm.idProductoCertificado}">
		<input type="hidden" name="metodoSolicitud" value="${CerDetalleSolicitudForm.metodoSolicitud}">
		<input type="hidden" name="fechaSolicitud" value="${CerDetalleSolicitudForm.fechaSolicitud}">
		<input type="hidden" name="idInstitucionColegiacion" value="${CerDetalleSolicitudForm.idInstitucionColegiacion}">
		<input type="hidden" name="aceptaCesionMutualidad" value="${CerDetalleSolicitudForm.aceptaCesionMutualidad}">
		<input type="hidden" name="idBoton" value="2">
	</html:form>
	
	<%
			String busquedaVolver = null; /* la vuelta no se trata de forma generica */
		%>
	<%@ include file="/html/jsp/censo/includeMantenimientoDuplicados.jspf"%>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>

</body>
</html>