<!-- busquedaPago.jsp -->
<!-- Patalla con los campos de busqueda de los Pagos y un iframe para los resultados
	 VERSIONES:
	 david.sanchez 17-03-2005 creacion
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
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
	//Obtengo el formulario de sesion:
	MantenimientoPagoForm formulario = (MantenimientoPagoForm)request.getSession().getAttribute("mantenimientoPagoForm");
		
	//Miro si tengo que buscar tras mostrar la pantalla
	String buscar = formulario.getBuscar();
	String funcionBuscar = "";
	if (buscar!=null && buscar.equals("SI")) {
		funcionBuscar = "buscar()";
	}
	
	String sTieneHijos = (String)request.getAttribute("SJCSTieneHijos");
	boolean tieneHijos = false;
	ArrayList idInstitucionSel = new ArrayList();
	ArrayList idEstadoSel = new ArrayList();
	if (sTieneHijos!=null && sTieneHijos.equals("S")) {
		tieneHijos =  true;
		//Datos seleccionados Combo:
		idInstitucionSel.add(formulario.getIdInstitucion());
	} else {
		//Datos seleccionados Combo:
		idEstadoSel.add(formulario.getIdEstado());
	}

	//InstitucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("SJCSInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	/*String institucionParam[] = new String[1];
	institucionParam[0] = institucionesVisibles;*/
	
	String institucionParam[] = new String[1];
	institucionParam[0] = usrbean.getLocation();

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.datosPagos.cabecera" 
		localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');<%=funcionBuscar %>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<div id="camposRegistro" class="posicionBusquedaSolo" align="center">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.datosPagos.leyenda1">

	<table class="tablaCampos" align="center" border="0">

	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="resultado">
	<html:hidden name="mantenimientoPagoForm" property = "modo" value = ""/>
	<input type="hidden" name="limpiarFilaSeleccionada" value="">

	<!-- FILA -->
	<tr>				

<% if (tieneHijos) {  %>

	<html:hidden name="mantenimientoPagoForm" property="idEstado" value='<%=ClsConstants.ESTADO_PAGO_CERRADO%>' />

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.institucion"/>
	</td>				
	<td >
		<siga:ComboBD nombre="idInstitucion" tipo="cmbNombreColegiosProduccion" parametro="<%=institucionParam %>" clase="boxCombo" obligatorio="false" elementoSel="<%=idInstitucionSel %>" />
	</td>
	
<% } else {  %>

	<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=usrbean.getLocation() %>" />

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.estado"/>
	</td>
	<td >
		<siga:ComboBD nombre="idEstado" tipo="cmbEstadosPagos" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoSel %>" />
	</td>
	
<% } %>

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.nombre"/>
	</td>
	<td>
		<html:text name="mantenimientoPagoForm" property="nombre" size="30" maxlength="100" styleClass="box"></html:text>
	</td>


	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.fechaInicio"/>
	</td>
	<td >
		<siga:Fecha nombreCampo="fechaIni" readonly="true"></siga:Fecha>
	</td>

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.fechaFin"/>
	</td>				
	<td>
		<siga:Fecha nombreCampo="fechaFin" readonly="true"></siga:Fecha>
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

		<siga:ConjBotonesBusqueda botones="B,N"  titulo="factSJCS.datosPagos.cabecera" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			sub();		
			if (compararFecha(document.forms[0].fechaIni,document.forms[0].fechaFin)==1) {
				mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
				alert(mensaje);
			} else {
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
		}

		//Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainPestanas";	
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
