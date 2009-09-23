<!-- inicio_SaltosYCompensaciones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	//Para el Combo de Turnos
	String dato[] = {(String)usr.getLocation()};
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.inicio_SaltosYCompensaciones.literal.titulo" 
		localizacion="gratuita.inicio_SaltosYCompensaciones.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');">
	
	<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_SaltosYCompensaciones.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "idPersona" value = ""/>
		<html:hidden property = "actionModal" value = "M"/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<tr>	
	<td>	

<siga:ConjCampos leyenda="gratuita.inicio_SaltosYCompensaciones.literal.titulo">

	<table width="100%" border=0>
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.turno"/>
		</td>
		<td>
			<siga:ComboBD nombre = "idTurno" tipo="turnos" clase="boxCombo" ancho="350" obligatorio="false" accion="Hijo:idGuardia" parametro="<%=dato%>"/>		
		</td>	
		<td class="labelText">	
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.guardia"/>
		</td>	
		<td>	
			<siga:ComboBD nombre = "idGuardia" tipo="guardias" clase="boxCombo" ancho="290" obligatorio="false" hijo="t"/> 
		</td>		
	</tr>
	<tr>	
		<td class="labelText">	
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.numero"/>
		</td>
		<td class="labelText" colspan="2">	

				<!-- RGG - SELECCION DE COLEGIADO -->
				<script language="JavaScript">	
					function buscarCliente () 
					{
						var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
						if (resultado != null && resultado[2]!=null)
						{
							document.getElementById('numeroLetrado').value = resultado[2];
							document.getElementById('idPersona').value = resultado[0];
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
						document.getElementById('numeroLetrado').value = "";
						document.getElementById('idPersona').value = "";
						document.getElementById('nombreMostrado').value = "";
					}		
				</script>
				<!-- Si la busqueda se realiza por idPersona, el campo numeroLetrado no puede modificarse, en cambio
					 si la busqueda se realiza mediante el campo numeroLetrado se podría modificar por pantalla sin
					 necesidad de seleccionarlo por el botón -->
				<html:text name="SaltosYCompensacionesForm" property="numeroLetrado" size="10" maxlength="100" styleClass="boxConsulta" value="" readOnly="true"></html:text>
				<html:text property="nombreMostrado" size="30" maxlength="150" styleClass="boxConsulta" value="" readOnly="true"></html:text>
				&nbsp;
				<!-- Boton para buscar un Colegiado -->
				<input type="button" class="button" id="idButton" name="buscarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>' onClick="buscarCliente();">
				<!-- Si el campo numeroLetrado es de solo lectuta hace falta este botón para limpiar -->
				&nbsp;<input type="button" id="idButton" class="button" name="limpiarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.limpiar"/>' onClick="limpiarCliente();">
				<!-- FIN - RGG - SELECCION DE COLEGIADO -->
		</td>
		<td class="labelText">	
				<html:radio name="SaltosYCompensacionesForm" property="salto" value="S"></html:radio>				
				&nbsp;
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.salto"/>	
				<html:radio name="SaltosYCompensacionesForm" property="salto" value="C"></html:radio>		
				&nbsp;
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacion"/>
		</td>	
	</tr>
	<tr>	
		<td class="labelText">	
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.fecha"/>
			&nbsp;
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.desde"/>
		</td>
		<td class="labelText">	
			<html:text name="SaltosYCompensacionesForm" property="fechaDesde" size="12" styleClass="box" value="" readOnly="true"></html:text>		
			&nbsp;
			<a onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="general.literal.seleccionarFecha"/>'  border="0"></a>
			&nbsp;
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta"/>
			&nbsp;		
			<html:text name="SaltosYCompensacionesForm" property="fechaHasta" size="12" styleClass="box" value="" readOnly="true"></html:text>		
			&nbsp;
			<a onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="general.literal.seleccionarFecha"/>'  border="0"></a>
		</td>
		<td class="labelText">	
			<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensado"/>
			&nbsp;
			<html:checkbox name="SaltosYCompensacionesForm" property="compensado" value=""></html:checkbox>
		</td>	
	</tr>
	</table>

</siga:ConjCampos>

		</td>	
	</tr>
	</html:form>	

	<!-- Formulario para rellenar el nColegiado desde el action de censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	
	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesBusqueda botones="N,B" />
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
			
		}		
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"P");	
			if (salida == "MODIFICADO") 
				buscar();
		}
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
			document.getElementsByName("idTurno")[0].onchange();
			
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

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
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>