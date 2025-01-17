<!DOCTYPE html>
<html>
<head>
<!-- ventanaPrecioPorDefecto.jsp -->
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

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	String	botones="Y,R,C";
%>	


<!-- HEAD -->
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			
		<script src="<%=app%>/html/js/jquery.notice.js" type="text/javascript"></script>
		
		<script language="JavaScript">
		
			function validacionPosterior(){

				var envio=true;	
				var i;
				var mensaje="";

				if (document.forms[0].precio.value==""){
					mensaje='<siga:Idioma key="pys.mantenimientoServicios.literal.precio"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					envio=false;
				}
				else{				
					if ((document.forms[0].precio.value<0) || (document.forms[0].precio.value>99999999) || (isNaN(document.forms[0].precio.value))){
						mensaje='<siga:Idioma key="messages.pys.mantenimientoProductos.errorPrecio"/>';
						alert(mensaje);
						envio=false;
					}
					else{
						//Comprobar periodicidad
						if (document.forms[0].periodicidad.value==""){
							mensaje='<siga:Idioma key="pys.mantenimientoServicios.literal.periodicidad"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
							alert(mensaje);										
							envio=false;
						}
					}
				}					
				return envio;				
			}	
			
			function growl(msg,type){
				jQuery('.notice-item-wrapper').remove();
				jQuery.noticeAdd({
					text: msg,
					type: type
				});
			}
				
		</script>		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="pys.mantenimientoServicios.literal.titulo1"/>
					</td>
				</tr>
			</table>
		
			<table  class="tablaCentralCamposPeque"  align="center">
				<tr>
					<td>
						<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="submitArea">
							<tr>				
								<td>
								<siga:ConjCampos leyenda="pys.mantenimientoServicios.leyenda">
									<table class="tablaCampos">	
										<!-- FILA -->
										<tr>				
											<td class="labelText">
												<siga:Idioma key="pys.mantenimientoServicios.literal.precio"/>&nbsp;(*)
											</td>				
											<td class="labelText">
									  			<html:text property="precio" styleClass="box" size="10" value=""></html:text>&nbsp;&euro;
											</td>
											<td class="labelText">
												<siga:Idioma key="pys.mantenimientoServicios.literal.periodicidad"/>&nbsp;(*)
											</td>				
											<td class="labelText">
									  			<siga:ComboBD nombre = "periodicidad" tipo="cmbPeriodicidad" clase="boxCombo" obligatorio="true"/>
											</td>
										</tr>
									</table>	
								</siga:ConjCampos>
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


			<!-- Asociada al boton GuardarYCerrar -->
			function accionGuardarCerrar() 
			{	
			    		
				document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
				if (validacionPosterior()){				
					var datos = new Array();
					datos[0] = 1;
					datos[1] = document.forms[0].precio.value.replace(/,/,".");
					datos[2] = document.forms[0].periodicidad.value;
					window.top.returnValue = datos;
					window.top.close();
				}	
			}
	
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
				
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{			
				var datos = new Array();
				datos[0] = 0;
				datos[1] = "";
				datos[2] = "";
				window.top.returnValue=datos;
				window.top.close();
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