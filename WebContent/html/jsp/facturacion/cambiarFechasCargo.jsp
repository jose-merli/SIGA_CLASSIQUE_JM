<!DOCTYPE html>
<html>
<head>
<!-- cambiarFechasCargo.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idDisqueteCargo = (String) request.getAttribute("idDisqueteCargo");
	String nombreFichero = (String) request.getAttribute("nombreFichero");

	String modoAction = ""; //NO BORRAR
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
  	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->

	<script LANGUAGE="JavaScript">

		// Asociada al boton Cerrar
		function accionCerrar() { 			
			window.top.close();
		}			
		
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {	
			sub();
			
			if(validarFechasSEPA()){
				document.ficheroBancarioPagosForm.fechaEntrega.value = jQuery("#fechaPresentacion").val();
				
				if (jQuery("input[name='radioAccion']:checked").val() == "0") { //Checkeado Unica
					document.ficheroBancarioPagosForm.fechaTipoUnica.value = "1";
					document.ficheroBancarioPagosForm.fechaUnica.value = jQuery("#fechaCargoUnica").val();	
				} else {
					document.ficheroBancarioPagosForm.fechaTipoUnica.value = "0";
					document.ficheroBancarioPagosForm.fechaFRST.value = jQuery("#fechaRecibosPrimeros").val();
					document.ficheroBancarioPagosForm.fechaRCUR.value = jQuery("#fechaRecibosRecurrentes").val();
					document.ficheroBancarioPagosForm.fechaCOR1.value = jQuery("#fechaRecibosCOR1").val();
					document.ficheroBancarioPagosForm.fechaB2B.value = jQuery("#fechaRecibosB2B").val();	
				}
				
				document.ficheroBancarioPagosForm.modo.value = "cambiarFechasFichero";
				document.ficheroBancarioPagosForm.target = 'submitArea';
				document.ficheroBancarioPagosForm.submit();
			}			
		}			
		
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.ficheroBancarioPagosForm.target = "_self";
				document.ficheroBancarioPagosForm.modo.value = "Editar";		
				document.ficheroBancarioPagosForm.submit();
			}						
		}			
	</script>
</head>

<body>

	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="facturacion.fechasficherobancario.titulo.cambiarFechasCargo"/>
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->	
	<html:form action="/FAC_DisqueteCargos.do" method="POST" target="submitArea">
		<html:hidden name="ficheroBancarioPagosForm" property="idDisqueteCargo" value="<%=idDisqueteCargo%>"/>
		<html:hidden name="ficheroBancarioPagosForm" property="nombreFichero" value="<%=nombreFichero%>"/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaEntrega" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaUnica" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaFRST" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaRCUR" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaCOR1" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaB2B" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaTipoUnica" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="modo" value=""/>
	
		<!-- INICIO: CAMPOS -->
		<siga:ConjCampos leyenda="Fechas" >
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">	
				<tr>
					<td class="labelText" >
						<%@ include file="/html/jsp/facturacion/fechasFicheroBancario.jsp"%>
					</td>
				</tr>	
			</table>
		</siga:ConjCampos>
	</html:form>
			
	<siga:ConjBotonesAccion botones="C,Y,R" modal="M"/>	
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>
</html>			