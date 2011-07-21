<!-- busquedaSolicitudesIncorporacion.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.ArrayList" %>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	ArrayList estadoSolicitud = new ArrayList();
    estadoSolicitud.add(String.valueOf(ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC));
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.busquedaSolicitudesIncorporacion.literal.cabecera" 
		localizacion="censo.busquedaSolicitudesIncorporacion.literal.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->


	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
</head>

<body onLoad="ajusteAlto('resultado');">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<siga:ConjCampos leyenda="censo.busquedaSolicitudesIncorporacion.literal.cabecera">

	<table align="center">

	<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

		<tr>				
			<td class="labelText"><siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.tipoSolicitud"/></td>				
			<td><siga:ComboBD nombre = "buscarTipoSolicitud" tipo="solicitud" clase="boxCombo" /></td>
	
			<td class="labelText"><siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.estado"/></td>				
			<td><siga:ComboBD nombre = "buscarEstadoSolicitud" tipo="estadoSolicitud1" elementoSel="<%=estadoSolicitud%>" clase="boxCombo"/></td>
	
			<td class="labelText"><siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.verAlarmas"/>&nbsp;&nbsp;<input type="checkbox" name="buscarVerAlarmas" onclick="deshabilitarCombo();"></td>
		</tr>
		<tr>				
			<td class="labelText"><siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.fechaDesde"/></td>
			<td>
				<!--input class="box" type="text" name="buscarFechaDesde"  style="width:75px" maxlength="10" class="box" readonly="true"-->
				<siga:Fecha nombreCampo="buscarFechaDesde" />
				<a href='javascript://'onClick="return showCalendarGeneral(buscarFechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
			</td>

			<td class="labelText"><siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.fechaHasta"/></td>
			<td>
				<!--input class="box" type="text" name="buscarFechaHasta" style="width:75px" maxlength="10" class="box" readonly="true"-->
				<siga:Fecha nombreCampo="buscarFechaHasta" campoCargarFechaDesde="buscarFechaDesde"/>
				<a href='javascript://'onClick="document.forms[0].buscarFechaHasta.value=document.forms[0].buscarFechaDesde.value; return showCalendarGeneral(buscarFechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
			</td>
		</tr>
	</html:form>
	
	</table>
	</siga:ConjCampos>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			if((validarFecha(document.SolicitudIncorporacionForm.buscarFechaDesde.value))&&
				(validarFecha(document.SolicitudIncorporacionForm.buscarFechaHasta.value))){
				sub();	
				if (compararFecha (document.SolicitudIncorporacionForm.buscarFechaDesde, document.SolicitudIncorporacionForm.buscarFechaHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					return;
				}
				document.SolicitudIncorporacionForm.modo.value = "buscarPor";
				document.SolicitudIncorporacionForm.submit();
			}else{
				setFocusFormularios();
			}
		}
		function deshabilitarCombo(){
		  if (document.SolicitudIncorporacionForm.buscarVerAlarmas.checked){
		  document.SolicitudIncorporacionForm.buscarEstadoSolicitud.value="";
		    document.SolicitudIncorporacionForm.buscarEstadoSolicitud.disabled=true;
		  }	else{
     		  document.SolicitudIncorporacionForm.buscarEstadoSolicitud.disabled=false;
		  }
		}


		function deshabilitarCombo(a){
			  if (document.SolicitudIncorporacionForm.buscarVerAlarmas.checked){
			  document.SolicitudIncorporacionForm.buscarEstadoSolicitud.value="";
			    document.SolicitudIncorporacionForm.buscarEstadoSolicitud.disabled=true;
			  }	else{
	     		  document.SolicitudIncorporacionForm.buscarEstadoSolicitud.disabled=false;
			  }
			}
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
					
	</iframe>
	
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
