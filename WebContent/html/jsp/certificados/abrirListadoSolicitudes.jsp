<!-- abrirListadoSolicitudes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

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
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idSolicitudCertif = (String) request
			.getAttribute("idPeticion");
	String concepto = (String) request.getAttribute("concepto");
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");
	String dato[] = {usr.getLocation()};
	Vector vEstado = (Vector) request.getAttribute("EstadosSolicitud");

	SIGASolicitudesCertificadosForm formulario = (SIGASolicitudesCertificadosForm) request
			.getAttribute("SolicitudesCertificadosForm");

	// datos seleccionados Combos
	ArrayList tipoCert = new ArrayList();
	ArrayList estadoSol = new ArrayList();
	ArrayList colOrigen = new ArrayList();
	ArrayList colDestino = new ArrayList();
	if (idSolicitudCertif == null || idSolicitudCertif.equals("")) {
		tipoCert.add(formulario.getTipoCertificado());
		estadoSol.add(formulario.getEstado());
		colOrigen.add(formulario.getIdInstitucionOrigen());
		colDestino.add(formulario.getIdInstitucionDestino());
	} else {
		formulario.resetCamposSolicitud();
		tipoCert.add("");
		estadoSol.add("");
		colOrigen.add("");
		colDestino.add("");
	}

	String fBuscar = "";
	String fechaDesde = UtilidadesBDAdm.getFechaBD("");
	String fechaHasta = "";
	String fechaEmisionDesde = "";
	String fechaEmisionHasta = "";
	if (request.getParameter("buscar") != null
			&& request.getParameter("buscar").equals("true")) {
		if (formulario != null) {
			if (formulario.getFechaDesde() != null)
				fechaDesde = formulario.getFechaDesde();
			if (formulario.getFechaHasta() != null)
				fechaHasta = formulario.getFechaHasta();
			if (formulario.getFechaEmisionDesde() != null)
				fechaEmisionDesde = formulario.getFechaEmisionDesde();
			if (formulario.getFechaEmisionHasta() != null)
				fechaEmisionHasta = formulario.getFechaEmisionHasta();
		}
		fBuscar = "buscar";
	}
%>

<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	
	
	<siga:Titulo titulo="certificados.solicitudes.titulo"
		localizacion="menu.certificados" />
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script type="text/javascript">
		
		function refrescarLocal() {
			buscar();
		}
			
		function buscar() {
			if((validarFecha(SolicitudesCertificadosForm.fechaDesde.value))&&
			 	(validarFecha(SolicitudesCertificadosForm.fechaHasta.value))){
				var nSolicitud = SolicitudesCertificadosForm.buscarIdSolicitudCertif.value;
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
			
		function buscarPaginador() {       	
			document.forms[1].target="resultado";
			document.forms[1].modo.value = "buscar";
			document.forms[1].submit();				
		}			

		<!-- Asociada al boton MarcarTodos -->
		function accionMarcarTodos() {		
			if (document.resultado.document.getElementById("oculto1_1")!=null){
				var checks = document.resultado.document.getElementsByName("chkPDF");
				if (checks.type != 'checkbox') {
					for (i = 0; i < checks.length; i++){
						if (checks[i].disabled==false) {
							checks[i].checked=1;		
						}
					}	
				} else{
					if (checks.disabled==false) {
						checks.checked=1;		
					}
				}
			}	
		}
		
		<!-- Asociada al boton DesmarcarTodos -->
		function accionDesmarcarTodos() {		
			if (document.resultado.document.getElementById("oculto1_1")!=null){
				var checks = document.resultado.document.getElementsByName("chkPDF");
				if (checks.type != 'checkbox') {
					for (i = 0; i < checks.length; i++){
						checks[i].checked=0;		
					}
				} else {
					checks.checked=0;
				}
			}	
		}

		/// PDFS
		function generarPDFMostrados() {
			sub();
			var aDatos = new Array();
				
			var oCheck =  window.frames.resultado.document.getElementsByName("chkPDF");
			var preguntar=false;
				
			for(i=0; i<oCheck.length; i++) {
				// Obtengo el estado
				var estados= window.frames.resultado.document.getElementsByName("oculto"+(i+1)+"_10");
				var estado="";
				if (estados!=undefined && estados[0]!=undefined) {
					estado = estados[0].value;
				}
					
				var dato2 = ""
						
				// solo si no esta anulado y no esta denegado
				if (estado!="" && estado!="<%=CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO%>" && estado!="<%=CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO%>") {  
					preguntar = true;					
					// guardo el dato
					aDatos[i] = oCheck[i].value;
				}
			}	

			SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value="";
				
			for (i=0; i<aDatos.length; i++)	{
				SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value = aDatos[i] + ";" +
				SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value;
				//ordenado al reves para que se generen los certificados en orden
			}

			if (SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value=="") {
				alert("<siga:Idioma key="certificados.solicitudes.mensaje.noHayCertificados"/>");
				fin();
			} else {
				SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value=SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value.substr(1);
				SolicitudesCertificadosForm.modo.value="generarVariosPDF";
				var f = SolicitudesCertificadosForm.name;	
				SolicitudesCertificadosForm.target="submitArea2";
				 window.frames.submitArea2.location = '<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
				// RGG como las meigas no dejaban que funcionara con submitArea, hemos creado submitArea2 y funciona.
			}
		}

		/// ENVIOS
		function accionEnviarSel() {
			sub();
			var aDatos = new Array();
				
			var oCheck =  window.frames.resultado.document.getElementsByName("chkPDF");
			
			for(i=0; i<oCheck.length; i++) {
				if (oCheck[i].checked) {
					var indice=aDatos.length;
					var preguntar=true;

					for (j=0; j<aDatos.length; j++) {
						var dato1 = aDatos[j];
						var dato2 = oCheck[i].value;
					}
					aDatos[j] = oCheck[i].value;
				}
			}
			document.forms[1].idsParaEnviar.value="";
			for (i=0; i<aDatos.length; i++) {
				document.forms[1].idsParaEnviar.value += ";" + aDatos[i];
			}

			if (document.forms[1].idsParaEnviar.value=="") {
				alert("<siga:Idioma key="certificados.solicitudes.mensaje.elegirAlgunCertificadoEnvio"/>");
				fin();
			} else {
				document.forms[1].modo.value='envioModalCertificado';
			   	var resultado = ventaModalGeneral(document.forms[1].name, "P");
				if (resultado) {
					refrescarLocal();
				} else {
					fin();
				}
			}
		}

		/// FINALIZAR
		function accionFinalizarSel() {
			sub();
			var aDatos = new Array();
				
			var oCheck =  window.frames.resultado.document.getElementsByName("chkPDF");
				
			for(i=0; i<oCheck.length; i++) {
				if (oCheck[i].checked) {
					var indice=aDatos.length;
					var preguntar=true;

					for (j=0; j<aDatos.length; j++) {
						var dato1 = aDatos[j];
						var dato2 = oCheck[i].value;
					}						
					aDatos[j] = oCheck[i].value;
				}
			}
			document.forms[0].idsParaFinalizar.value="";
			for (i=0; i<aDatos.length; i++) {
				document.forms[0].idsParaFinalizar.value += ";" + aDatos[i];
			}

			if (document.forms[0].idsParaFinalizar.value=="") {
				alert("<siga:Idioma key="certificados.solicitudes.mensaje.elegirAlgunCertificadoFinalizar"/>");
				fin();
			} else {
				document.forms[0].modo.value='finalizarCertificados';
				var f = SolicitudesCertificadosForm.name;	
				SolicitudesCertificadosForm.target="submitArea2";
				 window.frames.submitArea2.location = '<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
			}
		}

		function inicio() {			 
			<%if (idSolicitudCertif != null && !idSolicitudCertif.equals("")) {%>			     
				SolicitudesCertificadosForm.buscarIdSolicitudCertif.value=<%=idSolicitudCertif%>;
			<%if (concepto != null && !concepto.equals("")) {%>
				SolicitudesCertificadosForm.tipoCertificado.value='<%=concepto%>';
			<%}
			}
			if (request.getParameter("buscar") != null && request.getParameter("buscar").equals("true")) {%>
			    buscar();
			<%}%>		 
		}
			
		function consultas() {		
			document.RecuperarConsultasForm.submit();
		}
		</script>
<!-- FIN: SCRIPTS BOTONES -->

</head>

<body onload="ajusteAltoBotones('resultado');inicio();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"
		property="path" scope="request" />
	<fieldset>
		<table class="tablaCentralCampos" align="center" border="0">
			<html:form action="/CER_GestionSolicitudes.do?noReset=true"
				method="POST" target="resultado">
				<input type="hidden" name="modo" value="inicio">
				<input type="hidden" name="actionModal" value="">
				<input type="hidden" name="idsParaGenerarFicherosPDF" value="">
				<input type="hidden" name="idsParaFinalizar" value="">
				<input type="hidden" name="idsTemp" value="">
				<input type="hidden" name="limpiarFilaSeleccionada" value="">

				<tr>
					<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.fechadesde" /></td>
					<td><siga:Fecha nombreCampo="fechaDesde"
							valorInicial="<%=fechaDesde%>" /></td>



					<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.fechahasta" /></td>
					<td><siga:Fecha nombreCampo="fechaHasta"
							valorInicial="<%=fechaHasta%>" /></td>
					<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.estadosolicitud" /></td>
					<td>
						<!--<siga:ComboBD nombre="estado" tipo="estadoSolicitudCertificado" ancho="140" clase="boxCombo" obligatorio="false"/>-->
						<select name="estado" class="boxCombo">
							<option value=""></option>
							<%
								if (vEstado != null) {
										for (int k = 0; k < vEstado.size(); k++) {
											CerEstadoSoliCertifiBean b = (CerEstadoSoliCertifiBean) vEstado
													.get(k);
											String seleccionar = "";

											if (b.getIdEstadoSolicitudCertificado().toString()
													.equals(estadoSol.get(0))) {
												seleccionar = "selected";
											}
							%>
							<option
								value="<%=b.getIdEstadoSolicitudCertificado()
								.toString()%>"
								<%=seleccionar%>><%=b.getDescripcion()%></option>
							<%
								}
									}
							%>
					</select>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<!-- siga:Idioma key="certificados.solicitudes.literal.fechaemision"/ -->
						Fecha Emision Desde
					</td>
					<td><siga:Fecha nombreCampo="fechaEmisionDesde"
							valorInicial="<%=fechaEmisionDesde%>" /> </td>

					<td class="labelText">
						<!-- siga:Idioma key="certificados.solicitudes.literal.fechaemision"/ -->
						Hasta
					</td>
					<td><siga:Fecha nombreCampo="fechaEmisionHasta"
							valorInicial="<%=fechaEmisionHasta%>" /> </td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="certificados.mantenimiento.literal.certificado" /></td>
					<td><siga:ComboBD nombre="tipoCertificado"
							tipo="cmbTiposCertificados" ancho="170" clase="boxCombo"
							obligatorio="false" parametro="<%=dato%>"
							elementoSel="<%=tipoCert%>" /></td>
					<td class="labelText"><siga:Idioma
							key="censo.busquedaClientes.literal.nColegiado" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							property="numeroCertificado" size="20" maxlength="30"
							styleClass="box" /></td>
					<td class="labelText"><siga:Idioma
							key="censo.busquedaClientes.literal.nif" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							property="CIFNIF" size="20" maxlength="30" styleClass="box" /></td>
				</tr>
				<tr>

					<td class="labelText"><siga:Idioma
							key="censo.busquedaClientes.literal.nombre" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							property="nombre" size="20" maxlength="30" styleClass="box" /></td>
					<td class="labelText"><siga:Idioma
							key="censo.busquedaClientes.literal.apellidos" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							property="apellido1" size="20" maxlength="30" styleClass="box" />
					</td>
					<td class="labelText"><siga:Idioma
							key="pys.gestionSolicitudes.literal.idPeticion" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							styleClass="box" property="buscarIdSolicitudCertif"
							maxlength="10" /></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.colegioOrigen" /></td>
					<td><siga:ComboBD nombre="idInstitucionOrigen"
							tipo="cmbInstitucionesAbreviadas" ancho="160" clase="boxCombo"
							obligatorio="false" elementoSel="<%=colOrigen%>" /></td>
					<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.colegioDestino" /></td>
					<td><siga:ComboBD nombre="idInstitucionDestino"
							tipo="cmbInstitucionesAbreviadas" ancho="160" clase="boxCombo"
							obligatorio="false" elementoSel="<%=colDestino%>" /></td>
					<td class="labelText"><siga:Idioma
							key="pys.gestionSolicitudes.literal.numeroCertificado" /></td>
					<td><html:text name="SolicitudesCertificadosForm"
							styleClass="box" property="buscarNumCertificadoCompra"
							maxlength="30" /></td>
				</tr>
			</html:form>
		</table>
	</fieldset>


	<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
	<siga:ConjBotonesBusqueda botones="GPS,B,CON" titulo="" />

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
		id="resultado" name="resultado" scrolling="no" frameborder="0"
		marginheight="0" marginwidth="0" class="frameGeneral"> </iframe>


	<siga:ConjBotonesAccion botones="MT,DT,ES,FS" clase="botonesDetalle" />

	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST"
		target="mainWorkArea">
		<html:hidden property="actionModal" value="" />
		<html:hidden property="modo" value="" />
		<html:hidden property="subModo" value="" />
		<html:hidden property="tablaDatosDinamicosD" value="" />
		<html:hidden property="idsParaEnviar" value="" />
		<html:hidden property="idSolicitud" value="" />
		<html:hidden property="idPersona" value="" />
		<html:hidden property="descEnvio" value="" />

	</html:form>
	<html:form action="/CON_RecuperarConsultas" method="POST"
		target="mainWorkArea">
		<html:hidden property="idModulo"
			value="<%=ConModuloBean.IDMODULO_CERTIFICADOS%>" />
		<html:hidden property="modo" value="inicio" />
		<html:hidden property="accionAnterior" value="${path}" />

	</html:form>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
</body>
</html>

