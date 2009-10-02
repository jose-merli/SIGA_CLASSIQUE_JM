<!-- busquedaPrevisiones.jsp -->
<% 
//	 VERSIONES:
//	 raul.ggonzalez 20-04-2005 creacion
%>

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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPrevisionesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	// locales
	MantenimientoPrevisionesForm formulario = (MantenimientoPrevisionesForm)request.getSession().getAttribute("mantenimientoPrevisionesForm");
		
	// datos seleccionados Combo
	ArrayList hitoSel = new ArrayList();
	hitoSel.add(formulario.getHitos());

	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscar()";
	}
	
	String sTieneHijos = (String)request.getAttribute("SJCSTieneHijos");
	boolean tieneHijos = false;
	if (sTieneHijos!=null && sTieneHijos.equals("S")) {
		tieneHijos =  true;
	}

	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("SJCSInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	String institucionParam[] = new String[1];
	institucionParam[0] = institucionesVisibles;

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.previsiones.cabecera" 
		localizacion="factSJCS.previsiones.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoPrevisionesForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAlto('resultado');<%=funcionBuscar %>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<div id="camposRegistro" class="posicionBusquedaSolo" align="center">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.datosFacturacion.leyenda1">

	<table class="tablaCampos" align="center">

<html:form action="/FCS_MantenimientoPrevisiones.do?noReset=true" method="POST" target="resultado">
	<html:hidden name="mantenimientoPrevisionesForm" property = "modo" value = ""/>
	<html:hidden name="mantenimientoPrevisionesForm" property="idInstitucion" value="" />
	<input type="hidden" name="actionModal">
	<input type="hidden" name="limpiarFilaSeleccionada" value="">

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaInicio"/>
	</td>
	<td>
		<html:text name="mantenimientoPrevisionesForm" property="fechaDesdeB" maxlength="10" styleClass="box" readonly="true"/><a href="javascript://" onClick="return showCalendarGeneral(fechaDesdeB);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
	</td>

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaFin"/>
	</td>				
	<td>
		<html:text name="mantenimientoPrevisionesForm" property="fechaHastaB" maxlength="10" styleClass="box" readonly="true"/><a href="javascript://" onClick="return showCalendarGeneral(fechaHastaB);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
	</td>

	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.nombre"/>
	</td>
	<td>
		<html:text name="mantenimientoPrevisionesForm" property="nombreB" size="30" styleClass="box"></html:text>
		<html:hidden name="mantenimientoPrevisionesForm" property="nombre"></html:hidden>
	</td>


	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/>
	</td>
	<td>
		<siga:ComboBD nombre = "hitosB" tipo="cmbHitoGeneral" clase="boxCombo" obligatorio="false"  elementoSel="<%=hitoSel %>"/>						
	</td>


	</tr>

	</html:form>

	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
	<%
		String tipoAcceso = usrbean.getAccessType();
		String botonesBusqueda = "B,N";
	%>

		<siga:ConjBotonesBusqueda botones="<%=botonesBusqueda %>"  titulo="factSJCS.previsiones.cabecera" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();		
			if (validateMantenimientoPrevisionesForm(document.forms[0])) 
			{
				if (compararFecha(document.forms[0].fechaDesdeB,document.forms[0].fechaHastaB)==1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
				} else {
					document.forms[0].action="<%=app%>/FCS_MantenimientoPrevisiones.do?noReset=true";
					document.forms[0].modo.value="buscarPor";
					document.forms[0].target="resultado";	
					document.forms[0].submit();	
				}
			}
		}

		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.forms[0].action="<%=app%>/FCS_MantenimientoPrevisiones.do?noReset=true";
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
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



</div>	

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
