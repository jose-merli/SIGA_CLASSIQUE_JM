<!-- listadoTelefonosJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.gratuita.form.DefinirTelefonosJGForm"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Accion de la que venimos:
	DefinirTelefonosJGForm formulario = (DefinirTelefonosJGForm) request
			.getAttribute("DefinirTelefonosJGForm");
	String accion = formulario.getAccion();

	//Calculo los telefonos si la accion no es nuevo:
	Vector vTelefonosJG = (Vector) request.getAttribute("VTELEFONOS");
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<style type="text/css">
.tdBotones {
	text-align: center;
	padding-left: 0px;
	padding-right: 0px;
	padding-top: 0px;
	padding-bottom: 0px;
}
</style>

<script type="text/javascript">	
		function refrescarLocal(){
			buscarTelefonos();
		}
		
		function accionInsertarTelefonoTabla () {
			var validado = validarDatosMinimos (); 
			if(!validado){
				return;
			}			
			crearFila();
		}		
	</script>
</head>

<body>
	<!-- CAMPOS DEL REGISTRO -->
	<html:form action="/JGR_TelefonosPersonasJG" method="POST" target="submitArea" style="display:none">
		<html:hidden property="modo" value="" />
		<html:hidden property="idPersona" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="accion" />
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>

	<div style="top: 0px; z-index: 3; left: 0px; width: 380px; height: 60px;">

		<table width="350px" border="0" height="60px" cellpadding="0"cellspacing="0">
			<tr>
				<td class='labelText'>
					<div>
						<table id='tabTelefonosCabeceras' border='1' width='350px'
							cellspacing='0' cellpadding='0' style='table-layout: fixed'>
							<tr class='tableTitle'>
								<td style="width:120px; text-align: center; margin:2px" >
									<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.telefonoUso" />
								</td>
								<td style="width:120px; text-align: center; margin:2px" >
									<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.numeroTelefono" />
								</td>
								<td style="width:45px; text-align: center; margin:2px">
									<siga:Idioma key="censo.preferente.sms" />
								</td>
								<%
									if (!accion.equalsIgnoreCase("ver")) {
								%>
								<td style="width:80px; text-align: center;">
									<input type='button'
											id='idInsertarTelefonos' class="button" name='idButtoninsertar'
											value='<siga:Idioma key="general.boton.insertar"/>'
											alt='<siga:Idioma key="general.boton.insertar"/>'
											onclick="accioninsertar();">
								</td>
								<%
									}
								%>
							</tr>
						</table>
					</div>
					<div id="divtelefono" style='height: 60px; overflow-y: auto'>
						<table id='tablaTelefono' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='overflow-y: auto'>
							<logic:notEmpty name="DefinirTelefonosJGForm" property="telefonos">
								<logic:iterate name="DefinirTelefonosJGForm" property="telefonos" id="telefono" indexId="index">
									<bean:define id="telefonosJGForm" name="telefono" property="definirTelefonosJGForm"></bean:define>

									<tr id="fila_<bean:write name='index'/>">
										<%
											if (!accion.equalsIgnoreCase("ver")) {
										%>
										<td width="120px">
											<input type="text"
												id="nombreTelefonoJG_<bean:write name='index'/>" class="box"
												maxLength="20" style="width:110px"
												value="<bean:write name="telefonosJGForm" property="nombreTelefonoJG" />" />
										</td>
										<td width="120px">
											<input type="text"
												id="numeroTelefonoJG_<bean:write name='index'/>" class="box"
												maxLength="20" style="width:110px"
												value="<bean:write name="telefonosJGForm" property="numeroTelefonoJG" />" />
										</td>
										<bean:define id="preferenteSms" name="telefono" property="preferenteSms"></bean:define>
										<td width="45px">
											<input type="checkbox"
												id="preferenteSms_<bean:write name='index'/>"
												name="preferenteSms_<bean:write name='index'/>"
												class="preferenteSms"
												value="<bean:write name="telefonosJGForm" property="preferenteSms"/>"
												onClick="checkSms()"
												<%=(preferenteSms.equals("1")) ? "checked" : ""%> />
										</td>
										<td width="80px">
											<img src="/SIGA/html/imagenes/bborrar_off.gif"
												style="cursor: hand;"
												alt="<siga:Idioma key="general.borrar"/>" name="" border="0"
												onclick="borrarFila('fila_<bean:write name='index'/>')" />
										</td>
										<%
											} else {
										%>
										<td width="120px">
											<input type="text" readonly="readonly"
												id="nombreTelefonoJG_<bean:write name='index'/>"
												class="boxConsulta"
												maxLength="20"
												value="<bean:write name="telefonosJGForm" property="nombreTelefonoJG" />" />
										</td>
										<td width="120px">
											<input type="text" readonly="readonly"
												id="numeroTelefonoJG_<bean:write name='index'/>"
												class="boxConsulta"
												maxLength="20" style="width:110px"
												value="<bean:write name="telefonosJGForm" property="numeroTelefonoJG" />" />
										</td>
										<bean:define id="preferenteSms" name="telefono" property="preferenteSms"></bean:define>
										<td width="120px">
											<input type="checkbox" disabled="disabled"
												class="preferenteSms" style="width:110px"
												id="preferenteSms_<bean:write name='index'/>" name="preferenteSms_<bean:write name='index'/>"
												value="<bean:write name="telefonosJGForm" property="preferenteSms"/>"
												onClick="checkSms()"
												<%=(preferenteSms.equals("1")) ? "checked" : ""%> />
										</td>
										<td width="80px" style="text-align: center;" />
										<%
											}
										%>
									</tr>
								</logic:iterate>
							</logic:notEmpty>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<script language="javascript">
	
		function buscarTelefonos() {		
			document.forms[0].target = "_self";
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}


		//función que inserta una fila para introducir un numero de telefono
		function accioninsertar()	{			
			table = document.getElementById("tablaTelefono");		
			numFila = table.rows.length;
			tr = table.insertRow(numFila);
			tr.id = "fila_" + numFila;
			td = tr.insertCell(0);
			td.setAttribute("width", "120px");
			td.className = "";
			td.innerHTML = '<input type="text" id="nombreTelefonoJG_'   + numFila + '" class="box" style="width:110px;padding:2px;text-align:center;" maxLength="20" value="" />';

			// numero de telefono
			td = tr.insertCell(1); 
			td.setAttribute("width", "120px");
			td.className = "";
			td.innerHTML ='<input type="text" id="numeroTelefonoJG_' + numFila + '" class="box" style="width:110px;padding:2px;text-align:center;" maxLength="20" value="" />';

			//sms				
			td = tr.insertCell(2); 
			td.setAttribute("width", "45px");
			td.className = "";
			td.innerHTML ='<input type="checkbox" id="preferenteSms_' + numFila + '"  name="preferenteSms_' + numFila + '" value=""  onClick="checkSms()"/>';					
				

			//imagen de borrar
			td = tr.insertCell(3); 
			td.setAttribute("width", "80px");
			td.className = "";				
			td.innerHTML = '<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" onclick="borrarFila(\''+ tr.id +'\')">';
			tr.scrollIntoView(true);

			validarAnchoTabla ();			
		}

		function validarAnchoTabla () {

		if ( $.browser.msie ) {
				if (document.getElementById("tablaTelefono").clientHeight < document.getElementById("divtelefono").clientHeight) {
					document.getElementById("tabTelefonosCabeceras").width='80%';				
				} else {
					document.getElementById("tabTelefonosCabeceras").width='76.30%';	
										
				}
			}
		}
		
		//funcion de marcar un telefono para enviar sms
		function checkSms(){
			var boxes = $("input:checkbox").click(function(){
			    boxes.val('1').not(this).attr('checked', false).val('0');
			});
	   	}
	
		//borrar un telefono se borra la fila.		
		function  borrarFila (idFila) 			{ 				
			t = document.getElementById("tablaTelefono");
			for (i = 0; i < t.rows.length; i++) {					
				if (t.rows[i].id == idFila) {	
					fila = idFila.split("_")[1];			
					t.deleteRow (i);
					validarAnchoTabla ();	
					return;
				}
			}
		}			
	</script>
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>