<!-- abrirPrevisionesFacturacion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="facturacion.previsionesFacturacion.literal.titulo" 
			localizacion="facturacion.previsionesFacturacion.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			//Funcion asociada a boton buscar -->
			function buscar() 
			{
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";
				document.forms[0].submit();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	</head>

	<body onLoad="ajusteAltoBotones('resultado');buscar();">
	
<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.previsionesFacturacion.literal.titulo"/>				    
			</td>				
		</tr>
	</table>		
		
			<html:form action="/FAC_PrevisionesFacturacion.do" method="POST" target="resultado"  style="display:none">
				<html:hidden property="modo" value=""/>
				<html:hidden property="actionModal" value=""/>
			</html:form>

			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="5"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
		
			<!-- FIN: LISTA DE VALORES -->
			
			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->
		
			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 	boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 	son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 	LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 	PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
			-->

			<siga:ConjBotonesAccion botones="N" clase="botonesDetalle"/>
	
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">
		
				//Asociada al boton Nuevo -->
				function accionNuevo() 
				{		
					document.forms[0].modo.value="nuevo";
					document.forms[0].target='';						
					var salida = ventaModalGeneral(document.forms[0].name,"M");
					if (salida == "MODIFICADO") buscar();
				}
	
			</script>
			<!-- FIN: SCRIPTS BOTONES -->

			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
