<!DOCTYPE html>
<html>
<head>
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
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	String [] institucionParam = new String[1];
	institucionParam[0] = user.getLocation();

	// Si tiene producto comision
	String tieneProductoComision=(String)request.getAttribute("TIENEPRODUCTOCOMISION");

%>


<!-- HEAD -->


		
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DevolucionesManualesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		var tieneProductoComision = <%=tieneProductoComision%>;
		var msgNoTieneProductoComision = "<siga:Idioma key="messages.facturacion.devoluciones.noProductoComision"/>";

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	
		
		<!-- Asociada al boton Volver -->
		function accionGuardarCerrar(){ 
			if (document.forms[0].aplicarComisiones.checked && !tieneProductoComision) {
				alert(msgNoTieneProductoComision);	
			} else {
				if (validateDevolucionesManualesForm(document.DevolucionesManualesForm)){
					document.forms[0].modo.value="insertar";
					window.frames.submitArea.location="<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName="+
														document.forms[0].name+
														"&msg=facturacion.nuevoFichero.literal.generandoDevoluciones";
				}	
			}
		}	
		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.devolucionManual.datosDevolucion"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_DevolucionesManual.do" method="POST" target="submitArea">
		<html:hidden property ="modo" value = ""/>
		<html:hidden property ="recibos"/>
		<html:hidden property ="banco"/>
		<html:hidden property ="numeroRecibo"/>

		<table  class="tablaCentralCamposPeque"  align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="facturacion.devolucionManual.datosDevolucion">
						<table class="tablaCampos" align="center">	
							<tr>
								<td class="labelText" >
									<siga:Idioma key="facturacion.devolucionManual.fechaDevolucion"/>&nbsp;(*)
								</td>
								<td>
									<siga:Fecha nombreCampo="fechaDevolucion" readOnly="true" posicionX="10" posicionY="10"></siga:Fecha>
									
								</td>
							</tr>
							<tr>
								<td class="labelText" >
									<siga:Idioma key="facturacion.devolucionManual.aplicarComisiones"/>
								</td>
								<td>
									<input type="checkbox" name="aplicarComisiones" value="<%=ClsConstants.DB_TRUE%>">
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>

	<siga:ConjBotonesAccion botones='Y,C' modo=''  modal="P"/>

			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
