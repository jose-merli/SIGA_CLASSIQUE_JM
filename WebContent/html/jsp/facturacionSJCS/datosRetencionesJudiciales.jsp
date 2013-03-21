<!-- datosRetencionesJudiciales.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<%
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	String dato[] = {(String) usr.getLocation()};
	String accion = request.getAttribute("accion").toString();

	String fechaInicio = "", fechaFin = "", tipoRetencion = "", importe = "", descDestinatario = "", nombre = "", ncolegiado = "", observaciones = "", obligatorio = "", estilo = "", estiloNum = "", estiloCombo = "", idPersona = "", idRetencion = "", fechaAlta = "", esDeTurno = "";
	ArrayList destinatario = new ArrayList();
	String activar = ClsConstants.DB_FALSE;
	boolean lectura = false;
	String aplicaRetencion = (String) request.getAttribute("APLICADARETENCION");

	try {
		Hashtable miHash = (Hashtable) request.getAttribute("DATABACKUP");
		fechaAlta = (String) miHash.get("FECHAALTA");
		idRetencion = (String) miHash.get("IDRETENCION");
		idPersona = (String) miHash.get("IDPERSONA");
		fechaInicio = GstDate.getFormatedDateShort("", miHash.get("FECHAINICIO").toString());
		fechaFin = GstDate.getFormatedDateShort("", miHash.get("FECHAFIN").toString());
		importe = (String) miHash.get("IMPORTE");
		nombre = (String) miHash.get("NOMBRE");

		ncolegiado = (String) miHash.get("NCOLEGIADO");
		tipoRetencion = (String) miHash.get("TIPORETENCION");
		descDestinatario = (String) miHash.get("DESCDESTINATARIO");
		esDeTurno = (String) miHash.get("ESDETURNO");

		if (miHash.containsKey("OBSERVACIONES"))
			observaciones = (String) miHash.get("OBSERVACIONES");

		destinatario.add((String) miHash.get("IDDESTINATARIO"));
	} catch (Exception e) {
	};

	// La fecha de alta no se modifica, por tanto si entramos en modificación recuperamos la de la base de datos, y si es inserción se pondrá "sysdate"
	if (fechaAlta.equals(""))
		fechaAlta = "sysdate";

	if (accion.equalsIgnoreCase("ver")) {
		estilo = "boxConsulta";
		estiloNum = "boxConsultaNumber";
		estiloCombo = "boxConsulta";
		lectura = true;
		
	} else {
		if (aplicaRetencion != null && aplicaRetencion.equalsIgnoreCase("1")) {
			estilo = "boxConsulta";
			estiloNum = "boxConsultaNumber";
			estiloCombo = "boxConsulta";
			lectura = true;

		} else {
			estilo = "box";
			estiloNum = "boxNumber";
			estiloCombo = "boxCombo";
			lectura = false;
		}
	}
%>

<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

		<html:javascript formName="MantenimientoRetencionesJudicialesForm" staticJavascript="false" />

		<script type="text/javascript">
			function buscarCliente () {
				var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
				var nombre = "";
				// Se recuperan los valores que nos devuelvel a ventana modal (vienen en un array)
				if (resultado[0]!=null) document.forms[1].idPersona.value=resultado[0];
				if (resultado[2]!=null) document.forms[1].ncolegiado.value=resultado[2];
				// Se comprueba si el letrado tiene un nombre, apellido1 y apellido 2 y se va concatenando
				if (resultado[4]!=null) nombre = resultado[4];
				if (resultado[5]!=null) nombre += " " + resultado[5];
				if (resultado[6]!=null) nombre += " " + resultado[6];
				// Ahora se rellena el html:text (que está en modo consulta) con el nombre completo del letrado
				document.forms[1].nombre.value=nombre;
			}
			
			function limpiarPersona() {
				document.getElementById("idPersona").value = "";			
				document.getElementById("numeroNifTagBusquedaPersonas").value = "";
				document.getElementById("nombrePersona").value = "";			
			}			
			
			function activarLetrado(valorCheck) {
				<%if (!lectura) {%>
			 		if (!valorCheck.checked) {
			 			jQuery("#busquedaLetrado").show();
			 		} else {
			 			jQuery("#busquedaLetrado").hide();
			   			limpiarPersona();
			 		}
			 	<%}%>	
			}
			
			function cargarCheck() {		
				var casilla = document.getElementById("checkEsDeTurno");
				
			  	<%if (esDeTurno != null && esDeTurno.equals("1")) {%>			  			      
			  		casilla.checked=true;
				  	<%if (lectura) {%>		
				   		jQuery("#checkEsDeTurno").attr("disabled","true");
				  	<%}%>
				  	
			  	<%} else {%>
			  		casilla.checked=false;
				 	<%if (lectura) {%>
				   		jQuery("#checkEsDeTurno").attr("disabled","true");
				  	<%}%>
			  	<%}%>
			  	
			  	activarLetrado(casilla);
			  	
			  	<%if (!lectura) {%>
			    	document.getElementById("nombrePersona").value = "<%=nombre%>";
			    	document.getElementById("numeroNifTagBusquedaPersonas").value = "<%=ncolegiado%>";			 
			   <%}%>  
			   
			   obtenerCuenta();
			}	
			
			function obtenerCuenta() { 
				if (document.getElementById("idDestinatario").value!=""){
	            	document.MantenimientoDestinatariosRetencionesForm.nombreObjetoDestino.value="";	
				   	document.MantenimientoDestinatariosRetencionesForm.idDestinatario.value=document.getElementById("idDestinatario").value;
				   	document.MantenimientoDestinatariosRetencionesForm.submit();						  
				}
			}
			
			var cuentaContable_bu = "";
			function traspasoDatos(resultado) {
				cuentaContable_bu = resultado[0];
			  	document.getElementById("cuentaContable").value=cuentaContable_bu;
			}		
		</script>
	</head>

<body onload="cargarCheck();">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="FactSJCS.mantRetencionesJ.cabeceraExt" />
			</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalMedia" align="center">

		<!-- INICIO: CAMPOS DEL REGISTRO -->

		<!-- Comienzo del formulario con los campos -->
		<table class="tablaCentralCamposMedia" align="center" cellspacing="0">

			<html:form action="/CEN_BusquedaClientesModal.do" method="POST"	target="mainWorkArea" type="">
				<input type="hidden" name="actionModal" value="" />
				<input type="hidden" name="modo" value="abrirBusquedaModal" />
			</html:form>

			<html:form action="/FCS_RetencionesJudiciales.do" method="POST" target="submitArea">
				<html:hidden property="modo" value="" />
				<html:hidden property="idPersona" value="<%=idPersona%>" />
				<html:hidden property="idRetencion" value="<%=idRetencion%>" />
				<html:hidden property="idInstitucion" value="<%=usr.getLocation()%>" />
				<html:hidden property="fechaAlta" value="<%=fechaAlta%>" />

				<tr>
					<td>
						<!-- SUBCONJUNTO DE DATOS --> 
						<siga:ConjCampos leyenda="FactSJCS.mantRetencionesJ.leyendaRetenciones">
							<table class="tablaCampos" align="center" border="0">
								<tr>
									<td class="labelText" colspan="4">
										<html:checkbox styleid="checkEsDeTurno" property="checkEsDeTurno" onclick="activarLetrado(this);" />
										&nbsp;&nbsp;<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.aplicableLetrados" />
									</td>
								</tr>
								
								<tr>
									<%if (!lectura) {%>
										<td id="busquedaLetrado" colspan="4">
											<siga:BusquedaPersona
												tipo="colegiado"
												titulo="gratuita.seleccionColegiadoJG.literal.titulo"
												idPersona="idPersona">
											</siga:BusquedaPersona>
										</td>
									
									<%} else {%>
										<td class="labelText" width="155">
											<siga:Idioma key="gratuita.busquedaEJG.literal.colegiado" />
										</td>
										
										<td colspan="3">
											<input type="text" name="numeroColegiado"
											style="width: 250px;" class="boxConsulta" readonly="true"
											value="<%=ncolegiado%> - <%=nombre%>" />
										</td>
									<%}%>
								</tr>
								
								<tr>
									<td class="labelText" width="155">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramoLec" />&nbsp;(*)
									</td>

									<td class="labelText">
										<%if (lectura) {%>																				
											<%if (tipoRetencion.equalsIgnoreCase("P")) {%>
												<input type="hidden" name="tipoRetencion" value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>" /> 
												<input type="text" class="boxConsulta" readonly="true" value='<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual" />' />
												
											<%}else if (tipoRetencion.equalsIgnoreCase("F")) {%>
												<input type="hidden" name="tipoRetencion" value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>" /> 
												<input type="text" class="boxConsulta" readonly="true" value='<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo" />' />
																								
											<%}else if (tipoRetencion.equalsIgnoreCase(ClsConstants.TIPO_RETENCION_LEC)) {%>
												<input type="hidden" name="tipoRetencion" value="<%=ClsConstants.TIPO_RETENCION_LEC%>" />
												<input type="text" class="boxConsulta" readonly="true" value='<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramosLEC" />' />												 
											<%}%>
											 
											
										<%} else {%> 
 											<select name="tipoRetencion" class="boxCombo">
												<option value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>"
													<%if (tipoRetencion.equalsIgnoreCase("P")) {%> 
														selected
													<%}%>>
													<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual" />
												</option>
												
												<option value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>"
													<%if (tipoRetencion.equalsIgnoreCase("F")) {%> 
														selected
													<%}%>>
													<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo" />
												</option>
												
												<option value="<%=ClsConstants.TIPO_RETENCION_LEC%>"
													<%if (tipoRetencion.equalsIgnoreCase(ClsConstants.TIPO_RETENCION_LEC)) {%>
														selected 
													<%}%>>
													<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramosLEC" />
												</option>
											</select> 
										<%}%>										
									</td>
										
									<td class="labelText" style="vertical-align: left" colspan="2">
										<html:text name="MantenimientoRetencionesJudicialesForm"
											property="importe" size="10" maxlength="10"
											styleClass="<%=estiloNum%>"
											value="<%=UtilidadesNumero.formatoCampo(importe)%>"
											readonly="<%=lectura%>" />
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaInicioRJ" />&nbsp;(*)
									</td>
									
									<td class="labelText">
										<% // Si se entra en modo consulta, los botones del calendario desaparecen
											if (!accion.equalsIgnoreCase("ver") && !accion.equalsIgnoreCase("modificar")) {
										%>
											<siga:Fecha nombreCampo="fechaInicio" valorInicial="<%=fechaInicio%>" readonly="true" />
										
										<%} else {%>
											<input type="text" name="fechaInicio" class="boxConsulta" readonly="true" value="<%=fechaInicio%>" /> 
										<%}%>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaFin" />
									</td>
									
									<td class="labelText">
										<%if (!accion.equalsIgnoreCase("ver")) {%> 
											<siga:Fecha nombreCampo="fechaFin" valorInicial="<%=fechaFin%>" readonly="true" />
											 
										<%} else {%> 
											<input type="text" name="fechaFin" class="boxConsulta" readonly="true" value="<%=fechaFin%>" /> 
										<%}%>
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.destinatario" />&nbsp;(*)
									</td>

									<td class="labelText">
										<%if (lectura) {%>
											<siga:ComboBD nombre="idDestinatario"
												tipo="destinatariosRetencionesFCS" ancho="150"
												clase="<%=estiloCombo%>" filasMostrar="1"
												seleccionMultiple="false" obligatorio="false"
												parametro="<%=dato%>" ElementoSel="<%=destinatario%>"
												readonly="true" /> 
												
										<%} else {%> 
											<siga:ComboBD
												nombre="idDestinatario" tipo="destinatariosRetencionesFCS"
												ancho="150" clase="<%=estiloCombo%>" filasMostrar="1"
												seleccionMultiple="false" obligatorio="false"
												parametro="<%=dato%>" ElementoSel="<%=destinatario%>"
												accion="obtenerCuenta();" /> 
										<%}%>
									</td>
									
									<td colspan="2">
										<html:text
											name="MantenimientoRetencionesJudicialesForm"
											property="descDestinatario" size="50" maxlength="100"
											styleClass="<%=estilo%>" value="<%=descDestinatario%>"
											readonly="<%=lectura%>" />
									</td>									
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.cuentaContable" />
									</td>
									<td class="labelText">
										<input type="text" name="cuentaContable" class="boxConsulta" readonly="true" />
									</td>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.observaciones" />
									</td>
									<td class="labelText" colspan="3">
										<html:textarea
											cols="95" rows="4" property="observaciones"
											onKeyDown="cuenta(this,400)" onChange="cuenta(this,400)"
											styleclass="<%=estilo%>" value="<%=observaciones%>"
											readonly="<%=lectura%>" 
											style="overflow-y:auto; overflow-x:hidden; resize:none;"/> 
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</html:form>

			<html:form action="/JGR_MantenimientoDestinatariosRetenciones.do" method="POST" target="submitArea">
				<input type="hidden" name="modo" value="buscarCuenta" />
				<html:hidden property="idDestinatario" value="" />
				<html:hidden property="nombreObjetoDestino" value="" />
			</html:form>
		</table>
	</div>

	<%
		String bot = "C";
		if (!accion.equalsIgnoreCase("ver")) {
			bot += ",Y,R";
		}
	%>
		
	<siga:ConjBotonesAccion botones="<%=bot%>" modal="M" />

	<script language="JavaScript">				
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[1].reset();
			cargarCheck();
		}
		
		// Asociada al boton Guardar y Cerrar
		function accionGuardarCerrar() {	
			document.MantenimientoRetencionesJudicialesForm.importe.value=document.MantenimientoRetencionesJudicialesForm.importe.value.replace(/,/,".");
			var res = compararFecha(document.forms[1].fechaFin,document.forms[1].fechaInicio);
			sub();
		  	if (validateMantenimientoRetencionesJudicialesForm(document.MantenimientoRetencionesJudicialesForm)){ 
			
				if (res != 2) {
			 		if (!document.getElementById("checkEsDeTurno").checked && document.getElementById("idPersona").value== "" ){
			  			alert('<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.checkAplicable"/>');
			  			fin();
			  			return false;
			 		}
			 		
			 		if ((MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='P' || MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='F')&&MantenimientoRetencionesJudicialesForm.importe.value==''){
			 			msg = "<siga:Idioma key='errors.required' arg0='FactSJCS.mantRetencionesJ.literal.importe'/>";
						alert(msg);
						fin();
						return false;
			 		}
			 		
			 		if (MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='P' && MantenimientoRetencionesJudicialesForm.importe.value>100){
			  			alert('<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.avisoPorcentaje"/>');
			  			fin();
			  			return false;
			  			
			 		}else{
			 			document.forms[1].checkEsDeTurno.value = document.getElementById("checkEsDeTurno").checked;
						document.forms[1].target = "submitArea";
						document.forms[1].modo.value = '<%=accion%>';
						document.forms[1].submit();				            
			 		}   
			 		
				}else{
			 		alert('<siga:Idioma key="messages.fechas.rangoFechas"/>');
			 		fin();
			 		return false;
				}
				
		  	}else{
		   		fin();
		  	}	
		}
		
	 	// Asociada al boton Cerrar
		function accionCerrar() {
			top.cierraConParametros("NORMAL");
		}
	</script>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>