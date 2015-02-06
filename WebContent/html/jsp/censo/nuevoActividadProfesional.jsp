<!DOCTYPE html>
<html>
<head>
<!-- nuevoGrupoCliente.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.GruposClienteClienteForm"%>
<%@ page import="com.siga.censo.form.ActividadProfesionalForm"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	ActividadProfesionalForm miform = (ActividadProfesionalForm)request.getAttribute("ActividadProfesionalForm");
	//String[] param = { miform.getIdInstitucion(), miform.getIdInstitucion()}; 
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
</head>

<body>
	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
		<td class="titulitosDatos">	
			<siga:Idioma key="censo.fichaCliente.literal.actprofesional"/>
		</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->




		
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="censo.fichaCliente.literal.actprofesional">
	<table class="tablaCampos" align="center" border="0">
	<html:form action="/CEN_ActividadProfesional.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "modoAnterior" />
	<tr>
		<!-- GRUPO CLIENTE -->
		<td width="80" class="labelText">
			<siga:Idioma key="censo.fichaCliente.literal.actprofesional"/>
		</td>
		<td align="left">
			
			<siga:ComboBD nombre="grupos" tipo="cmbActividadProfesional" clase="boxCombo" obligatorioSinTextoSeleccionar="true" seleccionMultiple="false" readonly="false" />
			
		</td>
	</tr>
	</html:form>
	</table>
	</siga:ConjCampos>
	<!-- FIN: CAMPOS DEL REGISTRO -->


	
	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->
	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() {	
			sub();
			if (document.forms[0].grupos.value!='') {			
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "Insertar";
	        	document.forms[0].submit();
			} else{
				alert('<siga:Idioma key="messages.campoObligatorio.error"/> <siga:Idioma key="censo.fichaCliente.literal.actprofesional"/>');
				fin();
				return false;
			}
	   }	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>