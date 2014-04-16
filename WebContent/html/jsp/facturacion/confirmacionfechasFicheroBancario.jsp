<!DOCTYPE html>
<html>
<head>
<!-- confirmacionfechasFicheroBancario.jsp -->

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
	
	String idSerieFacturacion = (String) request.getAttribute("idSerieFacturacion");
	String idProgramacion = (String) request.getAttribute("idProgramacion");
	
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
				if (confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarFicheroRenegociaciones"/>')) {
					document.confirmarFacturacionForm.fechaEntrega.value = jQuery("#fechaPresentacion").val();
					
					if (jQuery("input[name='radioAccion']:checked").val() == "0") { //Checkeado Unica
						document.confirmarFacturacionForm.fechaTipoUnica.value = "1";
						document.confirmarFacturacionForm.fechaUnica.value = jQuery("#fechaCargoUnica").val();	
					} else {
						document.confirmarFacturacionForm.fechaTipoUnica.value = "0";
						document.confirmarFacturacionForm.fechaFRST.value = jQuery("#fechaRecibosPrimeros").val();
						document.confirmarFacturacionForm.fechaRCUR.value = jQuery("#fechaRecibosRecurrentes").val();
						document.confirmarFacturacionForm.fechaCOR1.value = jQuery("#fechaRecibosCOR1").val();
						document.confirmarFacturacionForm.fechaB2B.value = jQuery("#fechaRecibosB2B").val();	
					}
					
					document.confirmarFacturacionForm.modo.value = "confirmarFactura";
					document.confirmarFacturacionForm.facturacionRapida.value = "1";
					document.confirmarFacturacionForm.submit();							
					
				} else {
					fin();
				}
				
			} else {
				fin();
			}		
		}			
		
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.confirmarFacturacionForm.target = "_self";
				document.confirmarFacturacionForm.modo.value = "editarFechas";		
				document.confirmarFacturacionForm.submit();
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
	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">
		<html:hidden name="confirmarFacturacionForm" property="fechaEntrega" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaUnica" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaFRST" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaRCUR" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaCOR1" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaB2B" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="fechaTipoUnica" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="facturacionRapida" value=""/>
		<html:hidden name="confirmarFacturacionForm" property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
		<html:hidden name="confirmarFacturacionForm" property="idProgramacion" value="<%=idProgramacion%>"/>
		<html:hidden name="confirmarFacturacionForm" property="modo" value = ""/>		
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