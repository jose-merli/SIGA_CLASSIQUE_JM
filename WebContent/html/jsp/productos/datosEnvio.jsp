<!DOCTYPE html>
<html>
<head>
<!-- datosEnvio.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 21-04-2005 - Inicio
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
<%@ page import = "com.siga.beans.EnvDestinatariosBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	
	String tipoEnvio				 = "";
	String domicilio 				 = "";
	String poblacion 				 = "";
	String provincia 				 = "";
	String pais 						 = "";
	String cp 							 = "";
	String fax1 						 = "";
	String fax2 						 = "";
	String correoElectronico = "";
	
	Hashtable htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	if(!htData.isEmpty()){
		tipoEnvio				 	= (String)request.getAttribute("idTipoEnvio");
		domicilio 				= UtilidadesString.mostrarDatoJSP((String) (htData.get(EnvDestinatariosBean.C_DOMICILIO)));
		poblacion 				= UtilidadesString.mostrarDatoJSP((String) (htData.get("POBLACION")));
		provincia 				= UtilidadesString.mostrarDatoJSP((String) (htData.get("PROVINCIA")));
		pais 						 	= UtilidadesString.mostrarDatoJSP((String) (htData.get("PAIS")));
		cp 							 	= UtilidadesString.mostrarDatoJSP((String) (htData.get(EnvDestinatariosBean.C_CODIGOPOSTAL)));
		fax1 						 	= UtilidadesString.mostrarDatoJSP((String) (htData.get(EnvDestinatariosBean.C_FAX1)));
		fax2 						 	= UtilidadesString.mostrarDatoJSP((String) (htData.get(EnvDestinatariosBean.C_FAX2)));
		correoElectronico = UtilidadesString.mostrarDatoJSP((String) (htData.get(EnvDestinatariosBean.C_CORREOELECTRONICO)));
		}
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->	
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 			
			window.parent.close();
		}		
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body>
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="pys.solicitudCompra.literal.certificado"/> 	   
				</td>
			</tr>
		</table>
	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">
	<!-- INICIO: CAMPOS -->	
	<table  class="tablaCentralCamposMedia"  align="center">		
		<tr>				
			<td>
				<siga:ConjCampos leyenda="pys.solicitudCompra.literal.certificado">
					<table class="tablaCampos" align="center">							
						<!-- FILA -->
						<tr><td>&nbsp</td></tr>	
						<tr>		
							<td class="labelText">
								<siga:Idioma key="envios.definir.literal.tipoenvio"/>
							</td>
							<td class="labelText">
								<%=tipoEnvio%>
							</td>
						</tr>				
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.direccion"/>
							</td>
							<td class="labelText">
								<%=domicilio%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>
							</td>
							<td class="labelText">
								<%=poblacion%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.provincia"/>
							</td>
							<td class="labelText">
								<%=provincia%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.pais"/>
							</td>
							<td class="labelText">
								<%=pais%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.cp"/>
							</td>
							<td class="labelText">
								<%=cp%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.fax1"/>
							</td>
							<td class="labelText">
								<%=fax1%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.fax2"/>
							</td>
							<td class="labelText">
								<%=fax2%>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.correo"/>
							</td>
							<td class="labelText">
								<%=correoElectronico%>
							</td>
						</tr>						
		   		</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</table>
		
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="C" modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
