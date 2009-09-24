<!-- insertarRetencion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
%>

<html>
<!-- HEAD -->
<head>
	<html:javascript formName="SolicitudRetencionForm" staticJavascript="false"/>	
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.insertarRetencion.literal.insertarRetencion"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/SolicitudRetencioAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	
	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.retenciones.retencion">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->
	<tr>

	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.descripcion"/>&nbsp;(*)
	</td>	
	<td colspan="3" align="left">
		<html:text name="SolicitudRetencionForm" property="descripcion" size="30" styleClass="box" value=""></html:text>
	</td>
	</tr>	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.retencion"/>&nbsp;(*)
	</td>				
	<td align="left">
		<html:text name="SolicitudRetencionForm" property="retencion" size="5" styleClass="boxNumber" value="0.0"></html:text>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.tipoSociedad"/>
	</td>
					
	<td align="left">
		<siga:ComboBD nombre = "comboSociedades" tipo="tipoSociedad" clase="boxCombo"  obligatorioSinTextoSeleccionar="true" seleccionMultiple="true"/>
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
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();			
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			if (validateSolicitudRetencionForm(document.SolicitudRetencionForm)){
	        	document.forms[0].submit();	        
				window.returnValue="MODIFICADO";				
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
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
