<!DOCTYPE html>
<html>
<head>
<!-- altaRetencionIRPF.jsp -->
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

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>


<!-- JSP -->
<%  
	String botones = "y,X";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	// Obtengo los datos para validar las fechas.
	Vector obj = (Vector) request.getSession().getAttribute("fechas");
	request.getSession().removeAttribute("fechas");
%>	

<!-- HEAD -->

	<title><siga:Idioma key="gratuita.altaRetencionIRPF.literal.aRetencionIRPF"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.altaRetencionIRPF.literal.aRetencionIRPF"/>
			</td>
		</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<fieldset>
		<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCampos"  align="center">
			<html:form action="JGR_PestanaRetencionesIRPF.do" method="post" target="_self">
				<input type="hidden" name="modo" value="insertar">
				<tr>
					<td>
		 				<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText" width="100px">
									<siga:Idioma key="gratuita.altaRetencionIRPF.literal.fDesde"/>
								</td>
								<td>
									<siga:Fecha  nombreCampo= "fechaInicio" posicionX="10" posicionY="10"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.altaRetencionIRPF.literal.retencion"/>
								</td>
								<td>
									<siga:ComboBD nombre="idRetencion" tipo="tiposirpf" estilo="true" clase="boxCombo" obligatorio="false" ancho="350"/>
								</td>
							</tr>
						</table>
	   				</td>
	   			</tr>
   			</html:form>
		</table>
	</fieldset>

	<siga:ConjBotonesAccion botones="<%=botones%>"  modal="P"/>	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() {
		}

		function accionCancelar() {		
			window.top.close();
		}
		
		function accionGuardarCerrar() {
			sub();
			if(document.forms[0].idRetencion.value == "") {
				alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert3'/>");
				fin();
				return false;
			} else if(document.forms[0].fechaInicio.value == "") {
				alert("Debe introducir una fecha de inicio");
				fin();
				return false;
			} else {
				document.forms[0].setAttribute("action","<%=app%>/JGR_PestanaRetencionesIRPF.do");
				document.forms[0].submit();
			}
		}		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>