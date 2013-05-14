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

<!-- JSP -->
<%
	String app = request.getContextPath(); 
%>

<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

	<script language="JavaScript">
			//Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() {
			sub();
			if (document.all.confirmarFacturacionForm.fechaCargo.value == ''){
				alert('<siga:Idioma key="facturacion.confirmarFacturacion.literal.fechaCargo"/>');
				fin();
				return false;			
			
			}else{
			
				//Mando a la ventana padre la fecha de Cargo y cierro la modal:
				window.top.returnValue = document.all.confirmarFacturacionForm.fechaCargo.value;
				window.top.close();
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
									<siga:Datepicker  nombreCampo="fechaCargo" posicionX="10" posicionY="10"/>
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