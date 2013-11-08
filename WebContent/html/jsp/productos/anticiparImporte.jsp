<!DOCTYPE html>
<html>
<head>
<!-- anticiparImporte.jsp -->

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
		
	
	String totalAnticipado = (String) request.getAttribute("totalAnticipado");
	//String precioSolicitud = UtilidadesNumero.formato(UtilidadesNumero.redondea( (String) request.getAttribute("precioSolicitud"),2));//.replace(',','.');
	String precioSolicitud = (String) request.getAttribute("precioSolicitud");
	String idTipoClave = (String) request.getAttribute("idTipoClave");
	String idClave = (String) request.getAttribute("idClave");
	String idClaveInstitucion = (String) request.getAttribute("idClaveInstitucion");
	String tipo = (String) request.getAttribute("tipo");
	String idPeticion = (String) request.getAttribute("idPeticion");
	String idPersona = (String) request.getAttribute("idPersona");

	
	//Botón de "guardar y cerrar"
	String	botones="Y";
%>	


<%@page import="com.siga.Utilidades.UtilidadesNumero"%>
<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" heigth="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="pys.mantenimientoServicios.literal.tituloNuevoImporteAnticipo"/>
					</td>
				</tr>
			</table>
		
			<table  class="tablaCentralCamposPeque"  align="center">
				<tr>
					<td>
						<html:form action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea">
						<html:hidden property = "modo" value = ""/>
						<html:hidden property = "idTipoClave" value = "<%=idTipoClave%>"/>
						<html:hidden property = "idClave" value = "<%=idClave%>"/>
						<html:hidden property = "idClaveInstitucion" value = "<%=idClaveInstitucion%>"/>
						<html:hidden property = "tipo" value = "<%=tipo%>"/>
						<html:hidden property = "idPeticion" value = "<%=idPeticion%>"/>
						<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
						<html:hidden property = "totalAnticipado" value = "<%=totalAnticipado%>"/>
						<html:hidden property = "precioSolicitud" value = "<%=precioSolicitud%>"/>
							<tr>				
								<td>
									<table align="center">	
										<!-- FILA -->
										<tr>				
											<td class="labelText">
												<siga:Idioma key="pys.mantenimientoServicios.literal.totalAnticipado"/>
											</td>
											<td class="labelTextNum">		
												<%=UtilidadesNumero.formatoCampo(totalAnticipado)%>&nbsp;&euro;
											</td>
										</tr>
										<tr>
											<td class="labelText">
												<siga:Idioma key="pys.mantenimientoServicios.literal.precioSolicitud"/>
											</td>
											<td class="labelTextNum">	
												<%=UtilidadesNumero.formatoCampo(precioSolicitud)%>&nbsp;&euro;
											</td>
										</tr>
										<tr>
											<td class="labelText">
												<siga:Idioma key="pys.mantenimientoServicios.literal.nuevoImporteAnticipado"/>&nbsp;(*)
											</td>
											<td class="labelTextNum">		
												<input type="text" name="nuevoImporteAnticipado"  class="box" style="text-align:right"/>&nbsp;&euro;
											</td>
										</tr>
									</table>	
								</td>
							</tr>
						</html:form>
					</td>
				</tr>		
				<!-- Botones de accion -->						
				<tr>
					<siga:ConjBotonesAccion botones='<%=botones%>' modo=''  modal="P"/>
				</tr>																								
			</table>
		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			registrarEnterFormularios ();
			
			//Asociada a la tecla ENTER mediante la función registrarEnterFormularios()
			function buscar(){
				accionGuardarCerrar();
				window.event.cancelBubble = true;
				return false;
			}	

			function accionGuardarCerrar() 
			{	 
				sub();
				if (!validar()){
					fin();
					return;
				}

				document.forms[0].modo.value = "guardarAnticipos";
				document.forms[0].submit();
			}
			
			//validar que el importe ya anticipado y el importe a anticipar introducido
			//no superan el precio del producto
			function validar(){
				var totalAnticipado =  parseFloat('<%=totalAnticipado%>');
				document.forms[0].nuevoImporteAnticipado.value=document.forms[0].nuevoImporteAnticipado.value.replace(/,/,".");
				var nuevoImporteAnticipado =  parseFloat(document.forms[0].nuevoImporteAnticipado.value);
				var precioSolicitud =  parseFloat('<%=precioSolicitud%>');
			
				//valida que el nuevo importe sea un número positivo
				if (isNaN(nuevoImporteAnticipado) || nuevoImporteAnticipado <= 0){
					alert('<siga:Idioma key="messages.pys.solicitudCompra.errorAnticiparImporteNoValido"/>');
					return false;
				}
				var total = totalAnticipado + nuevoImporteAnticipado;
				if (precioSolicitud < total){
					alert('<siga:Idioma key="messages.pys.solicitudCompra.errorAnticiparImporteSuperior"/>');
					return false;
				}

				return true;
			}
						
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
				
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>