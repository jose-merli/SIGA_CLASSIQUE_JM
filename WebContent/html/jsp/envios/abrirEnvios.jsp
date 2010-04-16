<!-- abrirEnvios.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*, com.siga.envios.form.DefinirEnviosForm, java.util.*"%>
<%@ page import="com.siga.beans.EnvEnviosAdm"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = (UsrBean)ses.getAttribute(("USRBEAN"));

//	String buscar = (String) request.getAttribute("buscarEnvios");
	String buscar = (String) request.getAttribute("buscar");
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String funcionBuscar = "";
	if (buscar!=null && buscar.equals("true")) {
		funcionBuscar = "buscarPaginador()";
	}
	 
	ArrayList aEstado = new ArrayList();
	ArrayList aTipoEnvio = new ArrayList();
	
	try {
// 		DefinirEnviosForm	form = (DefinirEnviosForm) ses.getAttribute("DATABACKUP"); 
 		DefinirEnviosForm	form = (DefinirEnviosForm) ses.getAttribute("DefinirEnviosForm"); 
		if(form==null){
			aEstado.add("");	
			aTipoEnvio.add("");
		}else
		{
			aEstado.add(form.getIdEstado());
			aTipoEnvio.add(form.getIdTipoEnvio());
		}

	} catch (Exception e) {
		aEstado.add("");
		aTipoEnvio.add("");
	}
	
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.definir.literal.titulo" 
		localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
</head>

<body onload="ajusteAltoBotones('resultado');<%=funcionBuscar%>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<%--<html:form action="/EXP_Consultas.do?noReset=true" method="POST" target="resultado">--%>
	<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="resultado">	
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "tablaDatosDinamicosD" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

	<tr>				
		<td>
	
	<siga:ConjCampos>
	
	<table class="tablaCampos" align="center">
		<tr>
			<td width="18%" class="labelText">
				<html:radio  name="DefinirEnviosForm" property="tipoFecha" value="<%=EnvEnviosAdm.FECHA_CREACION%>"/>			
				<siga:Idioma key="envios.definir.literal.fechacreacion"/>			    
			</td>
			<td width="18%" class="labelText">
				<html:radio  name="DefinirEnviosForm" property="tipoFecha" value="<%=EnvEnviosAdm.FECHA_PROGRAMADA%>"/>			
				<siga:Idioma key="envios.definir.literal.fechaprogramada"/>			    
			</td>
			<td>&nbsp;</td>

			<td class="labelText" width="12%">
				<siga:Idioma key="envios.definir.literal.fechadesde"/>
			</td>				
			<td width="20%">
				<html:text name="DefinirEnviosForm" property="fechaDesde" size="9" maxlength="10" styleClass="box" readonly="true"></html:text>
				<a href='javascript://'onClick="return showCalendarGeneral(fechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
			</td>
			<td class="labelText" width="12%">
				<siga:Idioma key="envios.definir.literal.fechahasta"/>
			</td>
			<td width="15%">
				<html:text name="DefinirEnviosForm" property="fechaHasta" size="9" maxlength="10" styleClass="box" readonly="true"></html:text>
				<a href='javascript://'onClick="return showCalendarGeneral(fechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
			</td>
		</tr>	
	</table>
	</siga:ConjCampos>
	
	<siga:ConjCampos>
	<table>
		<tr>	
			<td class="labelText" width="10%">
				<siga:Idioma key="envios.definir.literal.identificador"/>
			</td>
			<td width="7%">
				<html:text name="DefinirEnviosForm" property="idEnvioBuscar" size="8" maxlength="12" styleClass="box"></html:text>
			</td>
			
			<td class="labelText" width="10%">
				<siga:Idioma key="envios.definir.literal.nombre"/>
			</td>
			<td width="10%">
				<html:text name="DefinirEnviosForm" property="nombre" size="20" maxlength="100" styleClass="box"></html:text>
			</td>
			
			<td class="labelText" width="8%">
				<siga:Idioma key="envios.definir.literal.estado"/>
			</td>
			<td width="20%">	
				<siga:ComboBD nombre = "idEstado" tipo="cmbEstadoEnvio" ElementoSel="<%=aEstado%>"  clase="boxCombo" obligatorio="true"/>
			</td>		
			
			<td class="labelText">
					<siga:Idioma key="envios.definir.literal.tipoenvio"/>
			</td>
			<td>	
				<siga:ComboBD nombre = "idTipoEnvio" tipo="cmbTipoEnvios" ElementoSel="<%=aTipoEnvio%>"  clase="boxCombo" obligatorio="true"/>				
			</td>	
		</tr>
	
	</table>
	</siga:ConjCampos>
			
	</td>
	</tr>
	</html:form>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B,N"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		function accionInformeEnvios() 
		{		
			document.forms[0].modo.value="descargarEstadistica";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		
		
		function buscarPaginador() 
		{		
//			if (validateBusquedaClientesForm(document.forms[0])) 
			{  
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
		}
		function buscar() 
		{
			sub();	
			document.forms[0].modo.value="buscarInicio";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}

		<!-- Asociada al boton Nuevo -->
			function nuevo() 
			{		
				document.forms[0].modo.value = "nuevo";
   				var datosTabla = ventaModalGeneral(document.forms[0].name,"P");
   				
   				if(datosTabla!=null&&datosTabla.indexOf('%')!=-1) {
   					DefinirEnviosForm.tablaDatosDinamicosD.value=datosTabla;   				
   					DefinirEnviosForm.modo.value="editar";
   					DefinirEnviosForm.target="mainWorkArea";
					DefinirEnviosForm.submit();
					
					//var auxTarget = DefinirEnviosForm.target;
				   	/*DefinirEnviosForm.target="submitArea";
				   	DefinirEnviosForm.submit();
				   	DefinirEnviosForm.target="mainWorkArea";*/
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

		<siga:ConjBotonesAccion botones="IE" clase="botonesDetalle"/> 

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
