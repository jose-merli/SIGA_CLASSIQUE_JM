<!DOCTYPE html>
<html>
<head>
<!-- busquedaFacturacion.jsp -->
<% 
//	 VERSIONES:
//	 raul.ggonzalez 07-03-2005 creacion
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
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoFacturacionForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Properties" %>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	// locales
	MantenimientoFacturacionForm formulario = (MantenimientoFacturacionForm)request.getSession().getAttribute("mantenimientoFacturacionForm");
		
	// datos seleccionados Combo
	ArrayList institucionSel = new ArrayList();
	institucionSel.add(formulario.getNombreInstitucion());
	ArrayList hitoSel = new ArrayList();
	hitoSel.add(formulario.getHitos());
	ArrayList estadoSel = new ArrayList();
	estadoSel.add(formulario.getEstado());

	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscarPaginador()";
	}
	
	String sTieneHijos = (String)request.getAttribute("SJCSTieneHijos");
	String strutTrans = (String)request.getAttribute("strutTrans");
	boolean tieneHijos = false;
	if (sTieneHijos!=null && sTieneHijos.equals("S")) {
		tieneHijos =  true;
	}

	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("SJCSInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	/*String institucionParam[] = new String[1];
	institucionParam[0] = institucionesVisibles;*/
	String institucionParam[] = new String[1];
	institucionParam[0] = usrbean.getLocation();

%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<% if(strutTrans.equalsIgnoreCase("FCS_MantenimientoPrevisiones")){  %>
		<siga:Titulo titulo="factSJCS.previsiones.cabecera"	localizacion="factSJCS.previsiones.ruta"/>	
	<% } else if(strutTrans.equalsIgnoreCase("CEN_MantenimientoFacturacion")) { %>
		<siga:Titulo titulo="factSJCS.datosFacturacion.cabecera" localizacion="factSJCS.mantenimientoFacturacion.localizacion"/>
	<% }  %>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoFacturacionForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAlto('resultado');<%=funcionBuscar %>">



	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.datosFacturacion.leyenda1">

	<table class="tablaCampos" align="center">

	<html:form action="/CEN_MantenimientoFacturacion.do?noReset=true" method="POST" target="resultado">
		<html:hidden name="mantenimientoFacturacionForm" property = "modo" value = ""/>
		<input type="hidden" name="actionModal">
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

	<!-- FILA -->
	<tr>				

<% if (tieneHijos) {  %>

	<html:hidden name="mantenimientoFacturacionForm" property="estado" value='<%=new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_LISTA_CONSEJO.getCodigo()).toString() %>' />

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.institucion"/>
	</td>				
	<td colspan="3">
		<siga:ComboBD nombre = "nombreInstitucion" tipo="cmbNombreColegiosProduccion" parametro="<%=institucionParam %>" clase="boxCombo" obligatorio="false"  elementoSel="<%=institucionSel %>" />						
	</td>

<% } else {  %>

	<html:hidden name="mantenimientoFacturacionForm" property="nombreInstitucion" value="<%=usrbean.getLocation() %>" />

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.estado"/>
	</td>
	<td colspan="3">
		<siga:ComboBD nombre = "estado" tipo="cmbEstadosFacturacion" clase="boxCombo" obligatorio="false"  elementoSel="<%=estadoSel %>"/>						
	</td>

<% }  %>

	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaInicio"/>
	</td>
	<td>
		<siga:Fecha nombreCampo="fechaIni"></siga:Fecha>
	</td>

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaFin"/>
	</td>				
	<td>
	<siga:Fecha nombreCampo="fechaFin" campoCargarFechaDesde="fechaIni"></siga:Fecha>
	</td>

	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.nombre"/>
	</td>
	<td>
		<html:text name="mantenimientoFacturacionForm" property="nombre" size="30" styleClass="box"></html:text>
	</td>


	<td class="labelText">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/>
	</td>
	<td>
		<siga:ComboBD nombre = "hitos" tipo="cmbHitoGeneral" clase="boxCombo" obligatorio="false"  elementoSel="<%=hitoSel %>"/>						
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
		String botonesBusqueda = "B,";
		if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) ) {
			if(strutTrans.equalsIgnoreCase("FCS_MantenimientoPrevisiones")){
				botonesBusqueda += "N";
			}else if(strutTrans.equalsIgnoreCase("CEN_MantenimientoFacturacion")){
				botonesBusqueda += "N,NR";
			}
			
		}
	%>

		<siga:ConjBotonesBusqueda botones="<%=botonesBusqueda %>"  titulo="factSJCS.datosFacturacion.cabecera" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();		
			if (validateMantenimientoFacturacionForm(document.forms[0])) 
			{
				if (compararFecha(document.forms[0].fechaIni,document.forms[0].fechaFin)==1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
				} else {
					document.forms[0].action="<%=app%>/CEN_MantenimientoFacturacion.do?noReset=true";
					document.forms[0].modo.value="buscarInit";
					document.forms[0].target="resultado";	
					document.forms[0].submit();	
				}
			}
		}
		function buscarPaginador() 
		{		
//			if (validateMantenimientoFacturacionForm(document.forms[0])) 
			{
				if (compararFecha(document.forms[0].fechaIni,document.forms[0].fechaFin)==1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
				} else {
					document.forms[0].action="<%=app%>/CEN_MantenimientoFacturacion.do?noReset=true";
					document.forms[0].modo.value="buscarPor";
					document.forms[0].target="resultado";	
					document.forms[0].submit();	
				}
			}
		}
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.forms[0].action="<%=app%>/CEN_MantenimientoFacturacion.do?noReset=true";
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
		
		<!-- Funcion asociada a boton nuevaRegularizacion -->
		function nuevaRegularizacion() 
		{		
			document.forms[0].action="<%=app%>/FCS_RegularizacionFacturacion.do";
			document.forms[0].modo.value="nuevo";

			var rc = ventaModalGeneral(document.forms[0].name,"P");
			// Abrimos la ventana de detalle de la factura
			if (rc) {
			  if (rc[0] == "CERRAR_SIN_GUARDAR"){// no hay refresco
			   return;
			   
			  }else{
				document.forms[1].idInstitucion.value = rc[0];
				document.forms[1].idFacturacion.value = rc[1];
				document.forms[1].submit();
			  }	
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


<html:form action="/CEN_MantenimientoFacturacion.do?noReset=true" method="POST" target="mainWorkArea">
	<html:hidden name="mantenimientoFacturacionForm" property = "modo" 					value= "Editar"/>
	<html:hidden name="mantenimientoFacturacionForm" property = "idFacturacion" value= ""/>
	<html:hidden name="mantenimientoFacturacionForm" property = "idInstitucion" value= ""/>
	<html:hidden name="mantenimientoFacturacionForm" property = "desdeRegularizacion" value= "si"/>
</html:form>



<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
