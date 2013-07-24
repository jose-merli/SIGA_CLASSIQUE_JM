<!DOCTYPE html>
<html>
<head>
<!-- operarPartida.jsp -->
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
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Hashtable miHash = (Hashtable) request.getSession().getAttribute("elegido");	
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("elegido");
	ses.removeAttribute("accion");	
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
	<html:javascript formName="DefinirPartidaPresupuestariaForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<%if (accion.equalsIgnoreCase("ver")){%>					  		   
			<siga:Idioma key="gratuita.operarPartida.literal.consultarPartidaPresupuestaria"/>
		<%} else {%>
			<siga:Idioma key="gratuita.operarPartida.literal.modificarPartidaPresupuestaria"/>
		<%}%>			
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/PartidaPresupuestariaAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "idPartidaPresupuestaria" value = "<%=(String)miHash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA)%>"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>

	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria">
	<table class="tablaCampos"  align="center">
	
	<!-- FILA -->
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.partidaPresupuestaria.literal.nombre"/>&nbsp;(*)
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirPartidaPresupuestariaForm" property="nombrePartida" size="30" styleClass="boxConsulta" value="<%=(String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirPartidaPresupuestariaForm" property="nombrePartida" size="30" styleClass="box" value="<%=(String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)%>"></html:text>
		<%}%>
	</td>
	</tr>
	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.importePartida"/>&nbsp;(*)
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirPartidaPresupuestariaForm" property="importePartida" size="12" maxlength="13" styleClass="boxConsultaNumber" value="<%=UtilidadesNumero.formatoCampo((String)miHash.get(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA))%>" readOnly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirPartidaPresupuestariaForm" property="importePartida" size="12" maxlength="13" styleClass="boxNumber" value="<%=UtilidadesNumero.formatoCampo((String)miHash.get(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA))%>"></html:text>
		<%}%>
	</td>	
	</tr>	
	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.partidaPresupuestaria.literal.descripcion"/>&nbsp;(*)
	</td>

	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<textarea name="descripcion" rows="4" cols="62" readOnly="true" class="boxConsulta"><%=(String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)%></textarea>
		<%} else {%>
			<textarea name="descripcion" rows="4" cols="62" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)"
			 class="box"><%=(String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)%></textarea>		
		<%}%>
	</td>
	</tr>

	</table>

	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
		<%if (accion.equalsIgnoreCase("ver")){%>		
		<siga:ConjBotonesAccion botones="C" modal="P"  />
		<%} else { %>
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
		<%}%>

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}		
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			if (validateDefinirPartidaPresupuestariaForm(document.DefinirPartidaPresupuestariaForm)){
				document.forms[0].importePartida.value=document.forms[0].importePartida.value.replace(/,/,".");
	        	document.forms[0].submit();	        
				window.top.returnValue="MODIFICADO";
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>