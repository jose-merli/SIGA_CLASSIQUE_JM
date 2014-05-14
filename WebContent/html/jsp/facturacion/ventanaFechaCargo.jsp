<!DOCTYPE html>
<html>
<head>
<!-- ventanaFechaCargo.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-html.tld" 	prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"	%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.FacSufijoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String modoAction = ""; //NO BORRAR
%>



	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script language="JavaScript">
		// Asociada al boton Cerrar
		function accionCerrar() { 			
			window.top.close();
		}			
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub();
		
			if(validarFechasSEPA()){		
				if (confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarFicheroRenegociaciones"/>')) {
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
					
					document.ficheroBancarioPagosForm.modo.value = "generarFichero";
					document.ficheroBancarioPagosForm.target = 'submitArea';					
					
					var nombreFormulario = document.ficheroBancarioPagosForm.name;	
					window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + nombreFormulario + '&msg=facturacion.ficheroBancarioPagos.mensaje.generandoFicheros';
					//document.ficheroBancarioPagosForm.submit();
					
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
				document.confirmarFacturacionForm.modo.value = "nuevo";		
				document.confirmarFacturacionForm.submit();
			}						
		}			
	</script>	
</head>

<body>
	<table class="tablaTitulo">		
		<!-- Campo obligatorio -->
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.ficheroBancarioPagos.boton.renegociacion"/>				    
			</td>				
		</tr>
	</table>	
	
	<html:form action="/FAC_DisqueteCargos.do" method="POST" target="submitArea">
		<html:hidden name="ficheroBancarioPagosForm" property="fechaEntrega" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaUnica" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaFRST" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaRCUR" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaCOR1" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaB2B" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaTipoUnica" value=""/>
		<html:hidden name="ficheroBancarioPagosForm" property="modo" value=""/>	
	</html:form>		
		
	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">
		<html:hidden name="confirmarFacturacionForm" property="modo" value = ""/>
		
		<siga:ConjCampos leyenda="facturacion.fechasficherobancario.fechas">
			<%@ include file="/html/jsp/facturacion/fechasFicheroBancario.jsp"%>
		</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion botones='C,Y,R' modo='' modal="P" />
	

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>