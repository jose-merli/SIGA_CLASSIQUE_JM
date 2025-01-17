<!DOCTYPE html>
<html>
<head>
<!-- ventanaFechaEfectiva.jsp -->
<!-- 
	 Permite mostrar/editar datos sobre lols precios asociados a los servicios
	 VERSIONES:
	 miguel.villegas 7-2-2005 
-->

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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String	botones="Y";
	String fecha = UtilidadesBDAdm.getFechaBD("");
%>	

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<!--siga:Titulo titulo="censo.busquedaHistorico.literal.titulo1" localizacion="censo.busquedaHistorico.literal.titulo1"/-->
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="pys.mantenimientoServicios.literal.fechaEfectiva"/>
			</td>
		</tr>
	</table>
			
	<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="submitArea">
		<table class="tablaCampos">	
			<tr><td>&nbsp;</td></tr>
				
			<tr>				
				<td class="labelText" style="width: 30%">
					<siga:Idioma key="pys.mantenimientoServicios.literal.fecha"/>&nbsp;(*)
			    </td>
				<td>		
					<siga:Fecha nombreCampo="fechaEfectiva" valorInicial="<%=fecha%>"></siga:Fecha>
				</td>
			</tr>
		</table>									
	</html:form>
		
	<siga:ConjBotonesAccion botones='<%=botones%>' modo=''  modal="P"/>

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">			
		function accionGuardarCerrar() {	 
		    if (window.top.returnValue=document.forms[0].fechaEfectiva.value=="") {
			  var msg="<siga:Idioma key="messages.servicios.fechaEfectivaObligatoria"/>";
			  alert(msg);
			  fin();
			  return false;
			}else{  
		    	window.top.returnValue=document.forms[0].fechaEfectiva.value;
			
				window.top.close();
			}						
		}				
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
			
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>