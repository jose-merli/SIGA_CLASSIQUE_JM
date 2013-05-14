<!-- mantAsisResp.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	// Validamos si es una consulta o no.
	boolean consulta = false;
	if(request.getAttribute("accion")!= null) consulta = true;
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.mantAsisResp.literal.titulo" 
		localizacion="gratuita.mantAsisResp.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">	
	
	function buscarCliente ()
	{
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
		var colegiado = document.getElementById('nColegiado');
		if (resultado != null && resultado[2]!=null)
			colegiado.value=resultado[2];
	}
	
	
	</script>
	
	
</head>

<body onLoad="ajusteAlto('resultado');">

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action = "/JGR_Asistencia.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = ""/>	
	<table class="tablaCentralCampos" width="945px" border="0">
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.nif"/>:&nbsp;<html:text name="AsistenciasForm" property="anio" size="4" maxlength="4" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.nombre"/>:&nbsp;<html:text name="AsistenciasForm" property="numero" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.apellido1"/>:&nbsp;<html:text name="AsistenciasForm" property="idTurno" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.apellido2"/>:&nbsp;<html:text name="AsistenciasForm" property="idGuardia" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.cp"/>:&nbsp;<siga:Idioma key="gratuita.mantAsisResp.literal.nif"/><html:text name="AsistenciasForm" property="nif" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText" colspan="3">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.direccion"/>:&nbsp;<html:text name="AsistenciasForm" property="nombre" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.pais"/>:&nbsp;<siga:Idioma key="gratuita.mantAsisResp.literal.nif"/><html:text name="AsistenciasForm" property="nif" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.provincia"/>:&nbsp;<html:text name="AsistenciasForm" property="nombre" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText" colspan="2">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.poblacion"/>:&nbsp;<html:text name="AsistenciasForm" property="apellido1" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText" colspan="2">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.fnacimiento"/>:&nbsp;<html:text name="AsistenciasForm" property="apellido1" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText" colspan="2">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.telefono"/>:&nbsp;<html:text name="AsistenciasForm" property="apellido1" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.mantAsisResp.literal.ecivil"/>:&nbsp;<html:text name="AsistenciasForm" property="nombre" size="10" maxlength="10" styleClass="box" value=""></html:text></br>
				<siga:Idioma key="gratuita.mantAsisResp.literal.rconyugal"/>:&nbsp;<html:text name="AsistenciasForm" property="nombre" size="10" maxlength="10" styleClass="box" value=""></html:text>
			</td>
			<td class="labelText" colspan="3">	
				<table>
					<tr>
						<td><siga:Idioma key="gratuita.mantAsisResp.literal.uso"/></td><td><siga:Idioma key="gratuita.mantAsisResp.literal.telefono"/></td><td>&nbsp;</td>
					</tr>
					<tr>
						<td>1</td><td>1</td><td>1</td>
					</tr>
					<tr>
						<td>1</td><td>1</td><td>1</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
				// obtenemos el idturno y el idguardia
				document.forms[1].idGuardia.value 	= document.forms[1].scsinscripcionguardia.value;
				document.forms[1].idTurno.value		= document.forms[1].turnos.value.substr(document.forms[1].turnos.value.indexOf(",")+1);
				document.forms[1].target			= "resultado";
				document.forms[1].modo.value 		= "buscarPor";
				document.forms[1].submit();
		}		
		
		//Funcion asociada a boton limpiar
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		//Funcion asociada a boton Nuevo
		function nuevo() {		
			 <%
			 	if(usr.isLetrado()){
			 		%>
			 			alert("<siga:Idioma key='gratuita.mantAsisResp.mensaje.alert1'/>");
			 		<%
			 	}
			 %>
			alert('Aun no está definido');
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<div style="position:absolute;top:180;left:18">
		<siga:ConjBotonesBusqueda botones="B,N"  titulo="gratuita.mantAsisResp.literal.titulo"/>
	</div>
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>