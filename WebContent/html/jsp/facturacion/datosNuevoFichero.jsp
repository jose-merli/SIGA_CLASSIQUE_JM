<!-- datosNuevoFichero.jsp -->
<!-- 
	 Muestra el formulario de incorporacion de nuevo fichero de devoluciones
	 VERSIONES:
		 miguel.villegas 22/03/2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// Datos del cliente a visualizar
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	
	String importePendiente=(String)request.getAttribute("PAGOPENDIENTE"); // Obtengo el importe pendiente	

	// Si tiene producto comision
	String tieneProductoComision=(String)request.getAttribute("TIENEPRODUCTOCOMISION");

%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DevolucionesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		var tieneProductoComision = <%=tieneProductoComision%>;
		var msgNoTieneProductoComision = "<siga:Idioma key="messages.facturacion.devoluciones.noProductoComision"/>";
		
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.close();
		}	
		
		<!-- Asociada al boton Volver -->
		function accionGuardarCerrar(){ 
			
			if(document.getElementById('comision').checked)
				document.forms[0].comisiones.value="1";
			else
				document.forms[0].comisiones.value="0";
			
				
			if (document.getElementById('comision').checked && !tieneProductoComision) {
				alert(msgNoTieneProductoComision);	
			} else {
				if (validateDevolucionesForm(document.DevolucionesForm)){
					document.forms[0].modo.value="insertar";
					document.frames.submitArea.location="<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName="+
														document.forms[0].name+
														"&msg=facturacion.nuevoFichero.literal.generandoDevoluciones";
					//document.forms[0].submit();
				}	
			}
		}	
		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.nuevoFichero.cabecera"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_Devoluciones.do" method="POST" target="submitArea"  enctype="multipart/form-data">
		<html:hidden property ="modo" value = ""/>
		<html:hidden property ="comisiones" />
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<table  class="tablaCentralCamposPeque"  align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="facturacion.nuevoFichero.literal.datosFichero">
						<table class="tablaCampos" align="center">	
							<tr>
								<td class="labelText" width="90%">
									<siga:Idioma key="facturacion.nuevoFichero.literal.seleccion"/>&nbsp;(*)
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<html:file property="ruta" size="50" styleClass="box" readOnly="false" onChange="rutaVisible.value=ruta.value"></html:file>
									<div style="display:none;position:absolute; width:18; height:35; z-index:2; top: 60px; left: 25px">
										<html:text styleClass="box" property="rutaVisible" size="50" value="" readOnly="true"></html:text>
									</div>
								</td>
							</tr>
							
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.Titulo">
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText" colspan="2">
								<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" checked="checked" value="noRenegociarAutomaticamente" >
									No renegociar automáticamente.
								<br>
								<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" >
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table class="tablaCampos" border="0">
									<tr>
										<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.Observaciones"/></td>
										<td class="labelText"><html:textarea property="datosPagosRenegociarObservaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" cols="80" rows="3" styleClass="box" value=""/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</siga:ConjCampos>
				</td>
			</tr>
		
			<tr>
				<td>
					<siga:ConjCampos leyenda="Comisiones">
						<table class="tablaCampos" border="0">
							<tr>
									<td class="labelText" width="90%">
										<siga:Idioma key="facturacion.nuevoFichero.literal.cargaComisiones"/>&nbsp;&nbsp;
										<input type = "checkbox"  name="comision"  />
									</td>
								</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			
		</table>
	</html:form>
	<siga:ConjBotonesAccion botones='Y,C' modo=''  modal="P"/>
	<!-- FIN: CAMPOS -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
