<!-- ventanaFechaCargo.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

<!-- JSP -->
<%
	String app = request.getContextPath(); 
%>

<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
	<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() {
			sub();
			if (document.all.confirmarFacturacionForm.fechaCargo.value == ''){
				alert('<siga:Idioma key="facturacion.confirmarFacturacion.literal.fechaCargo"/>');
				fin();
				return false;			
			
			}else{
			
				//Mando a la ventana padre la fecha de Cargo y cierro la modal:
				window.returnValue = document.all.confirmarFacturacionForm.fechaCargo.value;
				window.close();
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

		<table class="tablaCentralCamposPeque" align="center">
		
<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">
<html:hidden name="confirmarFacturacionForm" property="modo" value = ""/>
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.consultaComponentesJuridicos.literal.fechaCargo">
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaCargo"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<html:text name="confirmarFacturacionForm" property="fechaCargo" cols="8" styleClass="box" readonly="true"></html:text>
									<a href='javascript://'onClick="return showCalendarGeneral(fechaCargo);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
								</td>
							</tr>
					</table>
				</siga:ConjCampos>	
				</td>
			</tr>
</html:form>
		</table>

	<siga:ConjBotonesAccion botones='Y' modo='' modal="P" />
	

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>