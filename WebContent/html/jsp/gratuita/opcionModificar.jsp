<!-- opcionModificar.jsp -->
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
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Hashtable miHash = (Hashtable) ses.getAttribute("elegido");
	ses.removeAttribute("elegido");
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("accion");
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	
	<html:javascript formName="SolicitudRetencionForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:Idioma key="gratuita.retenciones.consultaRetencion"/>
		<%} else {%>
			<siga:Idioma key="gratuita.retenciones.modificarRetencion"/>
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
	
	<html:form action="/SolicitudRetencioAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idRetencion" value = "<%=(String)miHash.get(ScsRetencionesBean.C_IDRETENCION)%>"/>
	
	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="gratuita.retenciones.retencion">
	<table class="tablaCampos"  align="center">
	
	<!-- FILA -->
	<tr>

	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.descripcion"/>&nbsp;(*)
	</td>				
	<td colspan="3">
		<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="SolicitudRetencionForm" property="descripcion" size="20" maxlength="60" styleClass="boxConsulta" value="<%=(String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)%>" readonly="true"></html:text>
		<%} else {%>
		<html:text name="SolicitudRetencionForm" property="descripcion" size="20" maxlength="60" styleClass="box" value="<%=(String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)%>"></html:text>
		<%}%>
	</td>
	
	</tr>	
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.retencion"/>&nbsp;(*)
	</td>				
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="SolicitudRetencionForm" property="retencion" size="5" maxlength="5" styleClass="boxConsultaNumber" value="<%=(String)miHash.get(ScsRetencionesBean.C_RETENCION)%>" readonly="true"></html:text>
		<%} else {%>
		<html:text name="SolicitudRetencionForm" property="retencion" size="5" maxlength="5" styleClass="boxNumber" value="<%=(String)miHash.get(ScsRetencionesBean.C_RETENCION)%>"></html:text>
		<%}%>
	</td>
	
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.tipoSociedad"/>
	</td>				
	
<%	ArrayList alSociedades=(ArrayList)request.getAttribute("sociedades");
	String sociedad[]=new String[1];
	boolean noHayElementos = false;
	if(alSociedades!=null && alSociedades.size()>0){
		sociedad[0]=(String) alSociedades.get(0);
		if (((String)alSociedades.get(0)).equals(""))
			noHayElementos = true;
	} else
		noHayElementos = true;
	%>
	<td>
		<% if (accion.equalsIgnoreCase("ver")) { %>
		<siga:ComboBD nombre = "comboSociedades" tipo="tipoSociedadRO" clase="boxConsulta"  obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" elementoSel="<%=alSociedades%>" readonly="true" parametro="<%=sociedad%>"/>
		<% } else {
			if (accion.equalsIgnoreCase("editar") && !noHayElementos) { %>		
			<siga:ComboBD nombre = "comboSociedades" tipo="tipoSociedadEdit" clase="boxCombo"  obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" elementoSel="<%=alSociedades%>" parametro="<%=sociedad%>" />
			<% } else { %>
			<siga:ComboBD nombre = "comboSociedades" tipo="tipoSociedad" clase="boxCombo"  obligatorioSinTextoSeleccionar="true" seleccionMultiple="true"/>				
			<% } %>
		<% } %>
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
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			if (validateSolicitudRetencionForm(document.SolicitudRetencionForm)){
	        	document.forms[0].submit();				
			}
		}		
		
		//Asociada al boton Cerrar -->
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
