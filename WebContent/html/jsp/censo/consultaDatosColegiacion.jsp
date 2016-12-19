<!DOCTYPE html>
<html>
<head>
<!-- consultaDatosColegiacion.jsp -->
<!-- 
	 Muestra los datos de colegiaci�n generales de un cliente
	 VERSIONES:
	 RGG 15/03/2007 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	
	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String idInstitucionAcceso= user.getLocation();
	String modo = (String) request.getAttribute("ACCION");
	String nombre = (String) request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String nombreSolo = (String) request.getAttribute("NOMBRESOLO"); // Obtengo el nombre completo de la persona
	String apellidos = (String) request.getAttribute("APELLIDOS"); // Obtengo el nombre completo de la persona
	String numero = (String) request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	Long idPersona = (Long) request.getAttribute("IDPERSONA");
	String nif = (String) request.getAttribute("NIF"); // Obtengo el nif de la persona
	String idInstitucion = (String) request
			.getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion	
	String motivo = (String) request.getAttribute("MOTIVO");
	String activar = (String) request.getAttribute("ACTIVAR");
	boolean estadoCertificacion = (Boolean) request
			.getAttribute("ESTADOCERTIFICACION");
	String certificadoCorrecto = (String) request
			.getAttribute("CERTIFICADOCORRECTO");

	if (motivo == null) {
		motivo = "";
	}
	String fecha = (String) request.getAttribute("FECHAESTADO");
	if (fecha == null) {
		fecha = "";
	} else {
		fecha = GstDate.getFormatedDateShort(user.getLanguage(), fecha);
	}

	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute(
			"CenBusquedaClientesTipo");
	String botonesAccion = "";
	if (busquedaVolver == null) {
		busquedaVolver = "volverNo";
	} else {
		botonesAccion = "V,";
	}
	if (!user.isLetrado()) {
		botonesAccion += "BA,";
	}
	// le quito la coma final
	if (botonesAccion.length() > 0) {
		botonesAccion = botonesAccion.substring(0,
				botonesAccion.length() - 1);
	}
	String estilo = "";
	if (modo.equals("ver")) {
		estilo = "boxConsulta";
	} else {
		estilo = "box";
	}
%>	

	<!-- HEAD -->
	
			<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DatosColegiacionForm" staticJavascript="false" />
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->		
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<siga:Titulo titulo="censo.fichaCliente.situacion.cabecera"
			localizacion="censo.fichaLetrado.localizacion" />
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
		
		
		<script type="text/javascript">
			function darBaja(){     
				if (document.getElementById("fechaEstado").value==""){
			   		mensaje = '<siga:Idioma key="censo.consultaDatosColegiacion.error.fechaBaja"/>'
					alert(mensaje);
					return;		
			   	} 		   
		       	jQuery("#activo").hide();
			   	jQuery("#baja").show();			   
			   	jQuery("#botonBaja").hide();
			   	jQuery("#botonAlta").show();
			   	jQuery("#botonFallecido").hide();			   
			   	document.DatosColegiacionForm.modo.value = "darBaja";	
			   	document.DatosColegiacionForm.target="submitArea";
			    document.DatosColegiacionForm.submit();
				jQuery.attr("#fechaEstado","disabled","disabled");
				jQuery.attr("#motivo","disabled","disabled");			
		  	}
			
			function bajaFallecido(){     
			    if (document.getElementById("fechaEstado").value==""){
				    mensaje = '<siga:Idioma key="censo.consultaDatosColegiacion.error.fechaBaja"/>'
					alert(mensaje);
					return;
				}   		   
		       	jQuery("#activo").hide();
			   	jQuery("#baja").show();   
			   	jQuery("#botonBaja").hide();
			   	jQuery("#botonAlta").show();
			   	jQuery("#botonFallecido").hide();	   
			   	document.DatosColegiacionForm.modo.value = "bajaFallecido";	
			    document.getElementById("motivo").value='<siga:Idioma key="censo.consultaDatosColegiacion.literal.motivoFallecido"/>';
			   	document.DatosColegiacionForm.target="submitArea";
			    document.DatosColegiacionForm.submit();
				jQuery.attr("#fechaEstado","disabled","disabled");
				jQuery.attr("#motivo","disabled","disabled");
		  	}
		  
		  	function refrescarLocal() {
		  		document.location = document.location;
		  	}
		  
		  	function darAlta(){   
		       	jQuery("#activo").show();
			   	jQuery("#baja").hide();
			   	jQuery("#botonBaja").show();
			   	jQuery("#botonAlta").hide();
			   	jQuery("#botonFallecido").show();	  
			   	jQuery.removeAttr("#fechaEstado","disabled");
			   	jQuery.removeAttr("#motivo","disabled");
			    document.getElementById("fechaEstado").value="";
				document.getElementById("motivo").value="";
				document.DatosColegiacionForm.target="submitArea";
			   	document.DatosColegiacionForm.modo.value = "activar";	  					
		       	document.DatosColegiacionForm.submit();
			}
		  
		  	function situacionLetrado(){ 
		     	if (document.DatosColegiacionForm.activar.value=="1"){
					jQuery("#activo").hide();
			     	jQuery("#baja").show();				
			   <%if (modo.equals("ver")) {%>
			       	jQuery("#botonBaja").hide();
			       	jQuery("#botonAlta").hide();
				   	jQuery("#botonFallecido").hide();		  
			   <%} else {%>	   
			      <%if (!user.getAccessType().equalsIgnoreCase(SIGAConstants.ACCESS_FULL)) {%>	
			   		jQuery("#botonBaja").hide();
				    jQuery("#botonAlta").hide();
					jQuery("#botonFallecido").hide();
					jQuery.attr("#fechaEstado","disabled","disabled");
			        jQuery.attr("#motivo","disabled","disabled");
				 <%} else {%>	
				    jQuery("#botonBaja").hide();
				    jQuery("#botonAlta").show();
					jQuery("#botonFallecido").hide();
			        jQuery.attr("#fechaEstado","disabled","disabled");
			        jQuery.attr("#motivo","disabled","disabled");			
				<%}
					}%>		   
			 }else{
			     	jQuery("#activo").show();
			     	jQuery("#baja").hide();	     
				 <%if (modo.equals("ver")) {%>
			       	jQuery("#botonBaja").hide();
			       	jQuery("#botonAlta").hide();
				   	jQuery("#botonFallecido").hide();		   
			   <%} else {
						if (!user.getAccessType().equalsIgnoreCase(SIGAConstants.ACCESS_FULL)) {%>	
				    jQuery("#botonBaja").hide();
				    jQuery("#botonAlta").hide();
					jQuery("#botonFallecido").hide();			
			        jQuery.attr("#fechaEstado","disabled","disabled");
			        jQuery.attr("#motivo","disabled","disabled");
				 <%} else {%>
			   	   jQuery("#botonBaja").show();
		     	   jQuery("#botonAlta").hide();
				   jQuery("#botonFallecido").show();
			       jQuery.removeAttr("#fechaEstado","disabled");	  
			       jQuery.removeAttr("#motivo","disabled");
			       document.getElementById("fechaEstado").value="";
				   document.getElementById("motivo").value="";
			   <%}
					}%>	  
			 	}	
			 	document.DatosColegiacionForm.modo.value="buscarDatosColegiacion";
			 	document.DatosColegiacionForm.target="resultadoDatosColegiacion";
			 	document.DatosColegiacionForm.submit();
		  	}
		  	
			function buscar() {
				document.SancionesLetradoForm.submit();
			}	  	
			
			function calcularAltura() {		
				if(document.getElementById("idBotonesAccion")) {
					var tablaBotones = jQuery('#idBotonesAccion')[0];						
					var divResultados = jQuery('#resultados')[0];
					
					var posTablaBotones = tablaBotones.offsetTop;
					var posTablaDatos = divResultados.offsetTop;
					
					jQuery('#resultados').height(posTablaBotones - posTablaDatos);					
				}		
			}	
			
			function accionObtenerDuplicados() 
			{
				// mostrando la tabla de posibles duplicados
				jQuery("#divDescargaDocumentacion").dialog(
					{
						width: 950, height: 300, modal: true, position:['middle',20], resizable: false,
						buttons: { "Cerrar": function() { jQuery(this).dialog("close"); } }
					}
				);
				jQuery(".ui-widget-overlay").css("opacity","0.5");													
			}
			
			function comprobarDuplicados(){
				<% if(idInstitucionAcceso.equals("2000")){ %>
					jQuery.ajax({ 
							type: "POST",
							url: "/SIGA/CEN_MantenimientoDuplicados.do?modo=getAjaxObtenerDuplicados",				
							data: "checkIdentificador="+"1"+"&idPersona="+"<%=idPersona%>"+"&nidSolicitante="+"<%=nif%>"+"&nombre="+"<%=nombreSolo%>"+"&apellidos="+"<%=apellidos%>"+"&idInstitucion="+"<%=idInstitucion%>"+"&nColegiado="+"<%=numero%>",
							dataType: "json",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							success: function(json){	
								// mostrando el icono que avisa de que existen posibles duplicados
								if(json.aOptionsListadoDocumentacion != null && json.aOptionsListadoDocumentacion.length > 0){
									jQuery("#iconoboton_cargando_1").hide();
									jQuery("#iconoboton_aviso_1").show();
								}
								// preparando la tabla de resultados de posibles duplicados
								jQuery("#tablaDocumentacion tr").remove();
								jQuery("#tablaDocumentacion").append(json.aOptionsListadoDocumentacion);
								jQuery("#tablaDocumentacion").append("</table>");	
							}
						});		
				<%}%>
			}
		function informacionLetrado(idPersona,idIntitucion) {
				
			    var idInst = idIntitucion;			          		
			    var idPers = idPersona;		
			  
			    document.forms[3].filaSelD.value = 1;
				
				 
				if(idIntitucion != null && idIntitucion !=""){
					document.forms[3].tablaDatosDinamicosD.value=idPers + ',' + idInst + '%';	
					document.forms[3].modo.value="editar";
				}else{
					//Es no colegiado y el idIntitucion ser� de donde est�s logeado.
					document.forms[3].tablaDatosDinamicosD.value=idPers + ',' + <%=idInstitucion%> + '%';	
					document.forms[3].modo.value="ver";
				}
				
			   	document.forms[3].submit();		   	
			}
			
		function mantenimientoDuplicados(nifcif, numcol, idinstitucion, nombre, apellido1, apellido2) {
			
			document.MantenimientoDuplicadosForm.modo.value = "abrirConParametros";
			document.MantenimientoDuplicadosForm.nifcif.value=nifcif;
			document.MantenimientoDuplicadosForm.numeroColegiado=numcol;
			document.MantenimientoDuplicadosForm.idInstitucion=idinstitucion;
			document.MantenimientoDuplicadosForm.nombre=nombre;
			document.MantenimientoDuplicadosForm.apellido1=apellido1;
			document.MantenimientoDuplicadosForm.apellido2=apellido2;
			document.MantenimientoDuplicadosForm.submit();
		}
		</script>
	</head>

	<body class="tablaCentralCampos" onLoad="situacionLetrado();buscar();calcularAltura();comprobarDuplicados();">
	
		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaDatosColegiacion.literal.titulo1" />&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;&nbsp;
<%
					if (!numero.equalsIgnoreCase("")) {
%> 
						<siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
<%
					} else {
%> 
						<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" />
<%
					}
%>
				</td>
			</tr>
		</table>
	
		<!-- CAMPOS DEL REGISTRO -->
		<html:form action="/CEN_DatosColegiacion.do" method="POST" target="submitArea33">
			<table class="tablaCentralCampos">		
				<html:hidden property="modo" value="" />
				<html:hidden property="idPersona" value="<%=idPersona.toString()%>" />
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>" />
				<html:hidden name="DatosColegiacionForm" property="id" />
				<input type="hidden" name="activar" value="<%=activar%>">
				<input type="hidden" name="nombre" value="<%=nombre%>">
				<input type="hidden" name="numero" value="<%=numero%>">
				<input type="hidden" name="accion" value="<%=(String) request.getAttribute("ACCION")%>">
				<input type="hidden" name="idInstitucionPersona" value="<%=idInstitucion%>">
	
				<tr>
					<td>
						<!-- SUBCONJUNTO DE DATOS --> 
						<siga:ConjCampos leyenda="censo.consultaDatosColegiacion.literal.situacionLetrado">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="80px">
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td id="activo" valign="middle" align="center" class="txGrandeActivo">
													<siga:Idioma key="censo.consultaDatosColegiacion.literal.activo" />
												</td>
												
												<td id="baja" valign="middle" style="display: none" align="center" class="txGrandeBaja">
													<siga:Idioma key="censo.consultaDatosColegiacion.literal.baja" />
												</td>
											</tr>
										</table>
									</td>
									
									<td>
										<table border="0" cellpadding="2" cellspacing="0">
											<tr>
												<td class="labelText">
													<siga:Idioma key="censo.consultaDatosColegiacion.literal.fechaBaja" />&nbsp;(*)
												</td>
												<td align="left" width="120px">
<%
													if (!modo.equals("ver")) {
%> 
														<siga:Fecha nombreCampo="fechaEstado" valorInicial="<%=fecha%>" /> 
<%
												 	} else {
%>
														<siga:Fecha nombreCampo="fechaEstado" valorInicial="<%=fecha%>" disabled="true" /> 
<%
												 	}
%>
												</td>
												
												<td class="labelText">
													<siga:Idioma key="censo.consultaDatosColegiacion.literal.motivoBaja" />
												</td>
												<td>
													<html:text name="DatosColegiacionForm" property="motivo" size="40" maxlength="255" styleId="motivo" styleClass="<%=estilo%>" value="<%=motivo%>" />
												</td>
											</tr>
										</table>
									</td>
									
									<td>
										<table border="0" cellpadding="2" cellspacing="0">
											<tr>
												<td id="botonBaja" align="center" valign="middle" style="display: none">
													<html:button property="botonBaja" onclick="return darBaja();" styleClass="button">
														<siga:Idioma key="censo.consultaDatosColegiacion.literal.darBaja" />
													</html:button>
												</td>
												
												<td id="botonAlta" align="center" valign="middle" style="display: none">
													<html:button property="botonAlta" onclick="return darAlta();" styleClass="button">
														<siga:Idioma key="censo.consultaDatosColegiacion.literal.darAlta" />
													</html:button>
												</td>
												
												<td id="botonFallecido" align="center" valign="middle" style="display: none">
													<html:button property="botonFallecido" onclick="return bajaFallecido();" styleClass="button">
														<siga:Idioma key="censo.consultaDatosColegiacion.literal.bajaFallecido" />
													</html:button>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
		</html:form>
		
		<siga:ConjCampos leyenda="censo.consultaDatosColegiacion.literal.certificacion">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
<%
					if (estadoCertificacion) {
%>
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosColegiacion.literal.certCorrecta" />
						</td>
<%
					} else {
%>
						<td class="labelText">
							<font color=red>
								<b> 
									<siga:Idioma key="censo.consultaDatosColegiacion.literal.certIncorrecta" /> 
								</b>
							</font>
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosColegiacion.literal.certEsperado" />:&nbsp;&nbsp;&nbsp;&nbsp;
							<html:text name="DatosColegiacionForm"
								property="certificadoCorrecto" styleClass="boxConsulta" readonly="true"
								size="30" value="<%=certificadoCorrecto%>" />
						</td>
<%
					}
%>
					<td align="right">
						<img id="iconoboton_cargando_1"	src="/SIGA/html/imagenes/bloading_on_23.gif"	style="cursor: hand" alt="Cargando posibles duplicados"> 
						<img id="iconoboton_aviso_1"	src="/SIGA/html/imagenes/warning.png"			style="cursor: hand; display: none" alt="Duplicidades" onClick="accionObtenerDuplicados();"> 
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<div id="resultados">
			<div style="position:relative;height:50%;width:100%;">		
				<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado" 
					name="resultado" 
					scrolling="no" 
					frameborder="0"
					marginheight="0"
					 marginwidth="0"
					style="position:relative;height:100%;width:100%;"></iframe>
			</div>
			
			<div style="position:relative;height:50%;width:100%;">
				<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoDatosColegiacion" 
					name="resultadoDatosColegiacion"
					scrolling="no" 
					frameborder="0" 
					marginheight="0" 
					marginwidth="0"
					style="position:relative;height:100%;width:100%;"> </iframe>
			</div>
		</div>				
	
		<siga:ConjBotonesAccion botones="<%=botonesAccion %>" modo="<%=modo%>"
			idBoton="4#5" idPersonaBA="<%=idPersona.toString()%>"
			idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle" />
	
		<%@ include file="/html/jsp/censo/includeVolver.jspf"%>
	
		<!-- RGG para buscar las sanciones -->
		<html:form action="/CEN_SancionesLetrado.do?noReset=true" method="POST" target="resultado">
			<html:hidden property="modo" value="buscar" />
			<input type="hidden" name="accionModal" value="">
			<input type="hidden" name="accion" 	value="<%=(String) request.getAttribute("ACCION")%>">
			<html:hidden property="idPersona" value="<%=idPersona.toString() %>" />
			<html:hidden property="idInstitucionAlta" value="<%=user.getLocation() %>" />
		</html:form>
		
		 <%if(!"DUPLICADOS".equalsIgnoreCase(busquedaVolver) && !"MD".equalsIgnoreCase(busquedaVolver)){ %>   
			<html:form  action="/CEN_MantenimientoDuplicados.do" method="POST" target="mainWorkArea">
				<html:hidden property="modo" value="buscarPor"/>
				<html:hidden property="nifcif" />
			</html:form>
		
	   <%} %>
		
	
		<!-- FIN para buscar las sanciones -->	
		<!-- FIN para buscar las colegiaciones -->
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
		<iframe name="submitArea33" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
			<!-- FIN: SUBMIT AREA -->
		<div id="divDescargaDocumentacion" title="Duplicidades" style="display:none">
			<table id='tablaDocumentacion' style='width:100%;table-layout: fixed;'  border='1' align='center' cellspacing='0' cellpadding='0'>	
		</div>
	</body>
</html>
