<!-- tablasMaestras.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>

<% String app=request.getContextPath(); %>

<html>   
	<head>
		<title><siga:Idioma key="index.title"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script language="JavaScript">
			function irA(menu)
			{
				//comprueba que el iframe EDITAR no este bloqueado
				//limpia el IFRAME "editar"
				//if(menu.options[menu.selectedIndex].action!='')
				//{
				//alert("Espera");
				  	document.all.editar.src='<%=app%>/html/jsp/general/blank.jsp';
				  	document.frmGestion.action="/SIGA" + menu.options[menu.selectedIndex].action;
				  	//alert(document.frmGestion.action);
				  	document.frmGestion.submit();
			  	//}
			  	
				return false;
			}
					<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->

			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{		
				irA(document.frmGestion.tableName);
						
			}


		
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
		</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="administracion.catalogos.titulo" 
		localizacion="menu.administracion"/>
	
	<!-- FIN: TITULO Y LOCALIZACION -->
	</head>
	
	<body>
		<div id="camposRegistro" class="posicionBusquedaSolo">
	
		<%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr><td class="rayita"></td></tr>
		</table>

		 <p class="titulos" style="text-align:right">GESTIÓN CATÁLOGOS MAESTROS</p> 

		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr><td class="rayita"></td></tr>
		</table>
		--%>

		<form name="frmGestion" target="registros" method="post" action="">
  			<input type="hidden" name="mode" value="listing">
  			
  			
  			  			
  			<!--<table width="100%" border="0" cellspacing="0" cellpadding="0">-->
  			 <table class="tablaCentralCampos" align="center">
	 			<tr> 
    				<td class="labelText" align="right" valign="middle">
    					Seleccionar Catálogo
    				</td>
        			<td class="labelText" align="left" valign="middle">
        				<tiles:insert page="/selectorTablasMaestras.do" flush="true"/>
					</td>
        			<td align="center">
						<%--<html:button property="searchButton" onclick="irA(document.frmGestion.tableName);return false;" styleClass="button">
							<siga:Idioma key="general.search"/>
						</html:button>--%>
					</td>
	 			</tr>
  			</table>
  					<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->

        <siga:ConjBotonesBusqueda botones="B" titulo="" />
		

		<!-- FIN: BOTONES BUSQUEDA -->
  			
			<IFRAME	src	= "<%=app%>/html/jsp/general/blank.jsp"
					width		= "100%"
					height		= "50%"
					scrolling	= "no"
					frameborder	= "1"
			        name= "registros"
					> [Your user agent does not support inline frames or is currently 
			              configured not to display frames. </IFRAME> 
			<IFRAME	src	= "<%=app%>/html/jsp/general/blank.jsp"
					width		= "100%"
					height		= "43%"
					scrolling	= "no"
					frameborder	= "1"
			      	name= "editar"
					> [Your user agent does not support inline frames or is currently 
			        configured not to display frames. </IFRAME> 
		</form>
	</div>	
	</body>
</html>