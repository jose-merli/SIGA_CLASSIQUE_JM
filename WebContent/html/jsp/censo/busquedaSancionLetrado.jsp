<!-- busquedaSancionLetrado.jsp -->
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
<%@ page import="com.siga.censo.form.SancionesLetradoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
 <%@ page import="java.util.Properties"%>
<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	
<%  
	// locales
	SancionesLetradoForm formulario = (SancionesLetradoForm)request.getSession().getAttribute("SancionesLetradoForm");

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.busquedaSancionesLetrado.titulo" 
		localizacion="censo.busquedaSancionesLetrado.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="SancionesLetradoForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>

<body onLoad="ajusteAlto('resultado');">



	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center" width="100%">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.busquedaSancionesLetrado.literal.titulo1">

	<table class="tablaCampos" align="center"  width="100%" border="0">

	<html:form action="/CEN_SancionesLetrado.do?noReset=true" method="POST" target="resultado">
		<html:hidden name="SancionesLetradoForm" property = "modo" value = ""/>
		<input type="hidden" name= "actionModal" value = "">
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		
	
	
	<!-- FILA -->
	<tr>				

		<td class="labelText" width="140">
			<siga:Idioma key="censo.busquedaSancionesLetrado.literal.colegio"/>
		</td>				
		<td width="400">
			<siga:ComboBD nombre = "nombreInstitucionBuscar" tipo="cmbInstitucionesAbreviadas" clase="boxCombo" obligatorio="false"/>
		</td>

		<td class="labelText">	
		<siga:Idioma key="censo.busquedaSancionesLetrado.literal.ncolegiado"/>
		</td>
		<td >
			<!-- RGG - SELECCION DE COLEGIADO -->
			<script language="JavaScript">	
				function buscarCliente () 
				{    
					var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
					if (resultado != null && resultado[0]!=null)
					{
						document.getElementById('colegiadoBuscar').value = resultado[0];
					}
					if (resultado != null && resultado[4]!=null && resultado[5]!=null && resultado[6]!=null)
					{
						document.getElementById('nombreMostrado').value = resultado[4] + " " + resultado[5] + " " + resultado[6];
					}
				}		
			</script>
			<script language="JavaScript">	
				function limpiarCliente () 
				{
					document.getElementById('colegiadoBuscar').value = "";
					document.getElementById('nombreMostrado').value = "";
				}		



			</script>
			<!-- Boton para buscar un Colegiado -->
			<input type="button" class="button" name="buscarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>' onClick="buscarCliente();">
			<!-- Si el campo numeroLetrado es de solo lectuta hace falta este botón para limpiar -->
			&nbsp;<input type="button" class="button" name="limpiarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.limpiar"/>' onClick="limpiarCliente();">
			&nbsp;
			<!-- Si la busqueda se realiza por idPersona, el campo numeroLetrado no puede modificarse, en cambio
				 si la busqueda se realiza mediante el campo numeroLetrado se podría modificar por pantalla sin
				 necesidad de seleccionarlo por el botón -->
			<html:hidden name="SancionesLetradoForm" property="colegiadoBuscar" size="8" maxlength="80" styleClass="boxConsulta" value="" readOnly="true"></html:hidden>
			<html:text property="nombreMostrado" size="50" styleClass="boxConsulta" value="" readOnly="true"></html:text>
			<!-- FIN - RGG - SELECCION DE COLEGIADO -->
		</td>	

	</tr>
	<!-- FILA -->
	<tr>				

		<td class="labelText">
			<siga:Idioma key="censo.busquedaSancionesLetrado.literal.tipoSancion"/>
		</td>				
		<td >
			<siga:ComboBD nombre = "tipoSancionBuscar" tipo="cmbTipoSancion"  clase="boxCombo" obligatorio="false"/>
		</td>
		<td class="labelText">
			<siga:Idioma key="censo.BusquedaSancionesLetrado.literal.refCGAE"/>
		</td>				
		<td >
			<html:text  property="refCGAE" size="20" maxlength="50" styleClass="box" readOnly="false"></html:text>
		</td>

	</tr>
	<!-- FILA -->
	<tr>
		<td class="labelText" width="125">
			<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaInicio"/>
		</td>
		<td >
			<html:text name="SancionesLetradoForm" property="fechaInicioBuscar" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaInicioBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
		<td class="labelText" width="125">
			<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaFin"/>
		</td>
		<td>	
			<html:text name="SancionesLetradoForm" property="fechaFinBuscar" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaFinBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
	</tr>
	<tr>
		<td class="labelText" width="125">
			<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaImposicionDesde"/>
		</td>
		<td >
			<html:text name="SancionesLetradoForm" property="fechaImposicionDesdeBuscar" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaImposicionDesdeBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
		<td class="labelText" width="125">
			<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaImposicionHasta"/>
		</td>
		<td>	
			<html:text name="SancionesLetradoForm" property="fechaImposicionHastaBuscar" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaImposicionHastaBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
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

		<siga:ConjBotonesBusqueda botones="L,B,N"  titulo="" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function buscar() 
		{		
			document.forms[0].modo.value="buscarInit";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}

		function nuevo() 
		{		
			document.forms[0].modo.value="nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"G");
			if (resultado!=undefined && resultado=="MODIFICADO")
			{
				buscar();
			}
		}
				
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
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
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE LETRADOS -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
		<input type="hidden" name="busquedaSancion" value="1">
	</html:form>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>