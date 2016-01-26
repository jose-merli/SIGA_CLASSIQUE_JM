<!DOCTYPE html>
<html>
<head>
<!-- abrirListadoSolicitudes.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.beans.CerEstadoSoliCertifiBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.certificados.form.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	String concepto = (String) request.getAttribute("concepto");
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	String dato[] = {usr.getLocation()};
	Vector vEstado = (Vector) request.getAttribute("EstadosSolicitud");
	String volver = (String) request.getAttribute("volver");
	SIGASolicitudesCertificadosForm formulario = (SIGASolicitudesCertificadosForm) ses.getAttribute("SolicitudesCertificadosForm");
	String idSolicitudCertif = (String) request.getAttribute("idPeticion");
	if (idSolicitudCertif == null || idSolicitudCertif.equals("")) {
		idSolicitudCertif = formulario.getBusquedaIdSolicitud();
	}

	// datos seleccionados Combos
	ArrayList tipoCert = new ArrayList();
	ArrayList estadoSol = new ArrayList();
	ArrayList colOrigen = new ArrayList();
	ArrayList colDestino = new ArrayList();
	tipoCert.add(formulario.getBusquedaTipoCertificado());
	estadoSol.add(formulario.getBusquedaEstado());
	colOrigen.add(formulario.getBusquedaIdInstitucionOrigen());
	colDestino.add(formulario.getBusquedaIdInstitucionDestino());

	String fBuscar = "";
	String fechaDesde = UtilidadesBDAdm.getFechaBD("");
	String fechaHasta = "";
	String fechaEmisionDesde = "";
	String fechaEmisionHasta = "";
	String fechaSolicitudDesde = "";
	String fechaSolicitudHasta = "";	
	String checkCobrado = "", checkDescardado = "";
	if ((request.getParameter("buscar") != null && request.getParameter("buscar").equalsIgnoreCase("true")) || (request.getAttribute("volver") != null && ((String)request.getAttribute("volver")).equalsIgnoreCase("volver"))) {
		if (formulario != null) {
			if (formulario.getFechaDesde() != null)
				fechaDesde = formulario.getFechaDesde();
			if (formulario.getFechaHasta() != null)
				fechaHasta = formulario.getFechaHasta();
			if (formulario.getFechaEmisionDesde() != null)
				fechaEmisionDesde = formulario.getFechaEmisionDesde();
			if (formulario.getFechaEmisionHasta() != null)
				fechaEmisionHasta = formulario.getFechaEmisionHasta();
			if (formulario.getFechaSolicitudDesde() != null)
				fechaSolicitudDesde = formulario.getFechaSolicitudDesde();
			if (formulario.getFechaSolicitudHasta() != null)
				fechaSolicitudHasta = formulario.getFechaSolicitudHasta();		
			if (formulario.getCobrado() != null)
				checkCobrado = formulario.getCobrado();		
			if (formulario.getDescargado() != null)
				checkDescardado = formulario.getDescargado();					
		}
		fBuscar = "buscar";
	}
	
	String sError= UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "messages.general.error"));
	String sBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.close"));
	String sBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.guardarCerrar")); 
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<!-- Incluido jquery en siga.js -->
	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	
	<siga:Titulo titulo="certificados.solicitudes.titulo" localizacion="menu.certificados" />
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script type="text/javascript">
		function refrescarLocal() {
			buscar();
		}
			
		function buscar() {
			if((validarFecha(SolicitudesCertificadosForm.fechaDesde.value)) && (validarFecha(SolicitudesCertificadosForm.fechaHasta.value))){				
				var nSolicitud = SolicitudesCertificadosForm.busquedaIdSolicitud.value;
				if(isNumero(nSolicitud)){
					sub();
					SolicitudesCertificadosForm.modo.value="buscarInicio";
					SolicitudesCertificadosForm.target="resultado";
					SolicitudesCertificadosForm.submit();
				} else {
					alert("<siga:Idioma key="certificados.solicitudes.literal.numeroSolicitud"/>" +" "+ 
							  "<siga:Idioma key="messages.campoNumerico.error"/>");
				}
			} else {
				setFocusFormularios();
			}
		}
		function seleccionarTodos(pagina) {
			document.forms[0].seleccionarTodos.value = pagina;
			buscar('buscarPor');				
		}		
		function buscarPaginador() {       	
			document.forms[1].target="resultado";
			document.forms[1].modo.value = "buscar";
			document.forms[1].submit();				
		}			


		/// PDFS
		function generarPDFMostrados() {
			
			var oCheck = window.frames.resultado.ObjArray;
				if (oCheck != null && oCheck.length>0) {
					var aDatos = new Array();
					if(confirm('<siga:Idioma key="certificados.gestion.aprobarGenerar"/>')) { 
						
						sub();
						
						var aDatos = new Array();
						var oCheck = window.frames.resultado.ObjArray;
						if(oCheck != null){
							for(var i=0; i<oCheck.length; i++) {
								var elementoSeleccionado  = oCheck[i].split("||");
								var estadoElementoSeleccionado = elementoSeleccionado[9];
								
								var estado="";
								if (estadoElementoSeleccionado!=undefined) {
									estado = estadoElementoSeleccionado;
								}
									// guardo el dato
									aDatos[i] = oCheck[i];	
							}	
							
							SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value="";
						
							for (i=0; i<aDatos.length; i++)	{
								SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value = aDatos[i] + ";" +
								SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value;
								//ordenado al reves para que se generen los certificados en orden
							}
						}
						SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value=SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value.substr(1);
						SolicitudesCertificadosForm.modo.value="aprobarYGenerarVariosCertificados";
						var f = SolicitudesCertificadosForm.name;	
						SolicitudesCertificadosForm.target="submitArea2";
						 window.frames.submitArea2.location = '<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
						// RGG como las meigas no dejaban que funcionara con submitArea, hemos creado submitArea2 y funciona.
					}
					
				} else {
					alert("<siga:Idioma key="certificados.solicitudes.mensaje.noHayCertificados"/>");
					fin();
				}
		}
		/// FINALIZAR
		function accionFinalizarSel() {
			
			var oCheck =  window.frames.resultado.ObjArray;
			
				if (oCheck != null && oCheck.length>0) {
					if(confirm('<siga:Idioma key="certificados.gestion.finalizar"/>')) { 
						sub();
						var aDatos = new Array();
							
						
						if(oCheck != null){
							for(var i=0; i<oCheck.length; i++) {
									aDatos[i] = oCheck[i];	
							}
							document.forms[0].idsParaFinalizar.value="";
							for (i=0; i<aDatos.length; i++) {
								document.forms[0].idsParaFinalizar.value += ";" + aDatos[i];
							}
						}
						document.forms[0].modo.value='finalizarCertificados';
						var f = SolicitudesCertificadosForm.name;	
						SolicitudesCertificadosForm.target="submitArea2";
						window.frames.submitArea2.location = '<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
					}
					
				} else {
					
					alert("<siga:Idioma key="certificados.solicitudes.mensaje.elegirAlgunCertificadoFinalizar"/>");
					fin();
					
				}
			
		}
		
		/// PDFS
		function accionFacturarSel() {
			var oCheck =  window.frames.resultado.ObjArray;
					if (oCheck != null && oCheck.length>0) {
						if(confirm('<siga:Idioma key="certificados.gestion.facturar"/>')) { 
							sub();
							var aDatos = new Array();
						
							if(oCheck != null){	
								
								var oCheck =  window.frames.resultado.ObjArray;
								for(var i=0; i<oCheck.length; i++) {
									var elementoSeleccionado  = oCheck[i].split("||");
									var idSolicitud = elementoSeleccionado[1];
									var idInstitucion = elementoSeleccionado[6];
									var idProceso = elementoSeleccionado[10];
									
									aDatos[i] = "idSolicitud="+idSolicitud+","+"idInstitucion="+idInstitucion+","+"idProceso="+idProceso;	
								}
								document.forms[0].idsParaFacturar.value="";
								for (i=0; i<aDatos.length; i++) {
								
									document.forms[0].idsParaFacturar.value += ";" + aDatos[i];
								}
								
							}
					    jQuery.ajax({ 
							type: "POST",
							url: "/SIGA/CER_GestionSolicitudes.do?modo=getAjaxSeleccionSerieFacturacionFacturacionMasiva",				
							data: "listadoDeSolicitudes=" + document.forms[0].idsParaFacturar.value,
							dataType: "json",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							success: function(json){
								fin(); // finaliza el sub de accionFacturacionRapida()
									
								// Recupera el identificador de la serie de facturacion
								var idSerieFacturacion = json.idSerieFacturacion;		
								if (idSerieFacturacion==null || idSerieFacturacion=='') {
									jQuery("#selectSeleccionSerieFacturacion").find("option").detach();
									jQuery("#selectSeleccionSerieFacturacion").append(json.aOptionsSeriesFacturacion[0]);
									
									jQuery("#divSeleccionSerieFacturacion").dialog(
										{
											height: 220,
											width: 550,
											modal: true,
											resizable: false,
											buttons: {
												"<%=sBotonGuardarCerrar%>": function() {
													idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
													if (idSerieFacturacion==null || idSerieFacturacion=='') {
														alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
														
													} else {
														jQuery(this).dialog("close");
														document.forms[0].target = "submitArea";
														document.forms[0].idSerieSeleccionada.value = idSerieFacturacion;
														document.forms[0].modo.value = "facturarCertificadosSeleccionados";
													   	document.forms[0].submit();	
													   	window.setTimeout("fin()",5000,"Javascript");
														
														
													}
												},
												"<%=sBotonCerrar%>": function() {
													jQuery(this).dialog("close");
												}
											}
										}
									);
									jQuery(".ui-widget-overlay").css("opacity","0");														
									
								} else {
									if(idSerieFacturacion != "-1"){
										sub(); // Inicia el bloqueo hasta que haya realizado la accion
										document.forms[0].target = "submitArea";
										document.forms[0].idSerieSeleccionada.value =idSerieFacturacion;	
										document.forms[0].modo.value = "facturarCertificadosSeleccionados";
									   	document.forms[0].submit();		
									   	window.setTimeout("fin()",5000,"Javascript");
									}else{
										alert("Los certificados seleccionados no se pueden facturar");		
									}
								}													
							},
							
							error: function(e){
								fin(); // finaliza el sub de accionFacturacionRapida()
								alert("<%=sError%>");					
							}
						});		
					}				
				} else {
					alert("<siga:Idioma key="certificados.solicitudes.mensaje.elegirAlgunCertificadoFacturar"/>");
					fin();
				}
		}


		/// ENVIOS
		function accionEnviarSel() {
			sub();
			var oCheck =  window.frames.resultado.ObjArray;
			if (oCheck != null && oCheck.length>0) {
				var aDatos = new Array();
				
				var oCheck =  window.frames.resultado.ObjArray;
				if(oCheck != null){
					for(var i=0; i<oCheck.length; i++) {
							aDatos[i] = oCheck[i];	
					}
					document.forms[1].idsParaEnviar.value="";
					for (i=0; i<aDatos.length; i++) {
						document.forms[1].idsParaEnviar.value += ";" + aDatos[i];
					}
				}
				document.forms[1].modo.value='envioModalCertificado';
			   	var resultado = ventaModalGeneral(document.forms[1].name, "M");
				if (resultado) {
					refrescarLocal();
				} else {
					fin();
				}
			} else {
				alert("<siga:Idioma key="certificados.solicitudes.mensaje.elegirAlgunCertificadoEnvio"/>");
				fin();
			}
		}

		
		function inicio() {			 
			<% if (idSolicitudCertif != null && !idSolicitudCertif.equals("")) { %>			     
				SolicitudesCertificadosForm.busquedaIdSolicitud.value=<%=idSolicitudCertif%>;
			
				<% if (concepto != null && !concepto.equals("")) { %>
					SolicitudesCertificadosForm.busquedaTipoCertificado.value='<%=concepto%>';
				<% } %>
			
			<% } %>
			
			<% if ((request.getParameter("buscar") != null && request.getParameter("buscar").equalsIgnoreCase("true")) || (request.getAttribute("volver") != null && ((String)request.getAttribute("volver")).equalsIgnoreCase("volver"))) { %>
			   	buscar();
			<% } %>		 

		}
			
		function consultas() {		
			document.RecuperarConsultasForm.submit();
		}
		
		function nuevo(){
			SolicitudesCertificadosForm.target = "mainWorkArea";
			SolicitudesCertificadosForm.modo.value="nuevo";
			SolicitudesCertificadosForm.submit();
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onload="ajusteAltoBotones('resultado');inicio();">
	
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
	
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
	<fieldset>
		<table class="tablaCentralCampos" align="center" border="0">
			<html:form action="/CER_GestionSolicitudes.do?noReset=true" method="POST" target="resultado">
				<input type="hidden" name="modo" value="inicio">
				<input type="hidden" name="actionModal" value="">
				<input type="hidden" name="idsParaGenerarFicherosPDF" value="">
				<input type="hidden" name="idsParaFinalizar" value="">
				<input type="hidden" name="idsParaFacturar" value="">
				<input type="hidden" name="idSerieSeleccionada" value="">
				<input type="hidden" name="idsTemp" value="">
				<input type="hidden" name="limpiarFilaSeleccionada" value="">
				<html:hidden property="seleccionarTodos" value=""/>
				
				<tr>
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechadesde" /></td>
					<td><siga:Fecha nombreCampo="fechaDesde" valorInicial="<%=fechaDesde%>" /></td>

					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechahasta" /></td>
					<td><siga:Fecha nombreCampo="fechaHasta" valorInicial="<%=fechaHasta%>" campoCargarFechaDesde="fechaDesde"/></td>
					
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.estadosolicitud" /></td>
					<td>
						<select name="busquedaEstado" class="boxCombo">
							<option value=""></option>
<%
							if (vEstado != null) {
								for (int k = 0; k < vEstado.size(); k++) {
									CerEstadoSoliCertifiBean b = (CerEstadoSoliCertifiBean) vEstado.get(k);
									String seleccionar = "";

									if (b.getIdEstadoSolicitudCertificado().toString().equals(estadoSol.get(0))) {
										seleccionar = "selected";
									}
%>
									<option value="<%=b.getIdEstadoSolicitudCertificado().toString()%>" <%=seleccionar%>><%=b.getDescripcion()%></option>
<%
								}
							}
%>
						</select>
					</td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechaemision.desde" /></td>
					<td><siga:Fecha nombreCampo="fechaEmisionDesde" valorInicial="<%=fechaEmisionDesde%>" /> </td>

					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechaemision.hasta" /></td>
					<td><siga:Fecha nombreCampo="fechaEmisionHasta" valorInicial="<%=fechaEmisionHasta%>" campoCargarFechaDesde="fechaEmisionDesde"/> </td>
					
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.fechaDesde" /></td>
					<td><siga:Fecha nombreCampo="fechaSolicitudDesde" valorInicial="<%=fechaSolicitudDesde%>"/> </td>

					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechaemision.hasta" /></td>
					<td><siga:Fecha nombreCampo="fechaSolicitudHasta" valorInicial="<%=fechaSolicitudHasta%>" campoCargarFechaDesde="fechaSolicitudDesde"/></td>			
				</tr>					
				
				<tr>
					<td class="labelText"><siga:Idioma key="certificados.mantenimiento.literal.certificado" /></td>
					<td><siga:Select queryId="getTiposCertificado" id="busquedaTipoCertificado" selectedIds="<%=tipoCert%>"/></td>
					
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.idPeticion" /></td>
					<td><html:text name="SolicitudesCertificadosForm" styleClass="box" property="busquedaIdSolicitud" maxlength="10" /></td>
					
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.numeroCertificado" /></td>
					<td><html:text name="SolicitudesCertificadosForm" styleClass="box" property="buscarNumCertificadoCompra" maxlength="30" /></td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.colegioOrigen" /></td>
					<td><siga:Select queryId="getInstitucionesAbreviadas" id="busquedaIdInstitucionOrigen" selectedIds="<%=colOrigen%>"/></td>
					
					<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.colegiadoen" /></td>
					<td><siga:Select queryId="getInstitucionesAbreviadas" id="busquedaIdInstitucionDestino" selectedIds="<%=colDestino%>"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.nColegiado" /></td>
					<td><html:text name="SolicitudesCertificadosForm" property="busquedaNumCol" size="20" maxlength="30" styleClass="box" /></td>
				</tr>
								
				<tr>
					<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.nombre" /></td>
					<td><html:text name="SolicitudesCertificadosForm" property="busquedaNombre" size="20" maxlength="30" styleClass="box" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.apellidos" /></td>
					<td><html:text name="SolicitudesCertificadosForm" property="busquedaApellidos" size="20" maxlength="30" styleClass="box" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.nif" /></td>
					<td><html:text name="SolicitudesCertificadosForm" property="busquedaNIF" size="20" maxlength="30" styleClass="box" /></td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.enviado"/></td>
					<td>	
						<html:select property="enviado" styleId="enviado" name="SolicitudesCertificadosForm" style="width:50" styleClass="boxCombo">
							<html:option value=""></html:option>
							<html:option value="1"><siga:Idioma key="general.yes"/></html:option>
							<html:option value="0"><siga:Idioma key="general.no"/></html:option>
						</html:select>	
					</td>
					
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.descargado"/></td>
					<td>	
						<html:select property="descargado" styleId="descargado" name="SolicitudesCertificadosForm" style="width:50" styleClass="boxCombo">
							<html:option value=""></html:option>
							<html:option value="1"><siga:Idioma key="general.yes"/></html:option>
							<html:option value="0"><siga:Idioma key="general.no"/></html:option>
						</html:select>	
					</td>
					<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.cobrado"/></td>
					<td>					
						<html:select property="cobrado" styleId="cobrado" name="SolicitudesCertificadosForm" style="width:50" styleClass="boxCombo">
							<html:option value=""></html:option>
							<html:option value="1"><siga:Idioma key="general.yes"/></html:option>
							<html:option value="0"><siga:Idioma key="general.no"/></html:option>
						</html:select>	
					</td>		
				</tr>
			</html:form>
		</table>
	</fieldset>
	
	

	<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
	<!-- Esta va SearchButtonsConstants -->
	<siga:ConjBotonesBusqueda botones="N,B,CON" titulo="" />

	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"> </iframe>
	
	<!-- Esta va ActionButtonsConstants -->
	<siga:ConjBotonesAccion botones="GAC,ESM,FAC,FSC" clase="botonesDetalle" />

	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
		<html:hidden property="actionModal" value="" />
		<html:hidden property="modo" value="" />
		<html:hidden property="subModo" value="" />
		<html:hidden property="tablaDatosDinamicosD" value="" />
		<html:hidden property="idsParaEnviar" value="" />
		<html:hidden property="idSolicitud" value="" />
		<html:hidden property="idPersona" value="" />
		<html:hidden property="descEnvio" value="" />
	</html:form>
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_CERTIFICADOS%>" />
		<html:hidden property="modo" value="inicio" />
		<html:hidden property="accionAnterior" value="${path}" />
	</html:form>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<iframe name="submitArea2" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>