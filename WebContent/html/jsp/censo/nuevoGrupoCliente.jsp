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


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	GruposClienteClienteForm miform = (GruposClienteClienteForm)request.getAttribute("GruposClienteClienteForm");
	String[] param = {miform.getIdPersona(), miform.getIdInstitucion(), miform.getIdInstitucion()};
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>	
	
</head>

<body>
	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
		<td class="titulitosDatos">	
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.grupoCliente"/>
		</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->




		
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.grupoCliente">
	<table class="tablaCampos" align="center" border="0">
	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "modoAnterior" />
	<tr>
		<!-- GRUPO CLIENTE -->
		<td width="80" class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.grupoCliente"/>
		</td>
		<td align="left">
			<siga:ComboBD nombre="grupos" tipo="cmbGruposCliente_2" clase="boxCombo" obligatorioSinTextoSeleccionar="true" seleccionMultiple="false" readonly="false" parametro="<%=param%>" ancho="360" />
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
			}else{
				
				alert("<siga:Idioma key='messages.campoObligatorio.error'/>  <siga:Idioma key='censo.busquedaClientesAvanzada.literal.grupoCliente'/>");
				fin();
				return false;
			} 
	   }	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>