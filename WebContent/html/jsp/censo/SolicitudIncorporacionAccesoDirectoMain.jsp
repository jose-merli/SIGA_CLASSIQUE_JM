<!-- SolicitudIncorporacionAccesoDirectoMain.jsp -->
<!-- EJEMPLO DE VENTANA PRINCIPAL DE LA APLICACION -->
<!-- Contiene la cabecera, el menu principal, el logo y los botones de salir.
	 Ademas contiene la barra de localización y titulo que se actualiza desde
	 cada mantenimiento mostrado en la zona de trabajo.
	 Tiene por lo tanto una zona de trabajo enla que se muestran todos los mantenimientos
-->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "struts-bean.tld" 	prefix="bean"		%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles"	%>
<%@ taglib uri = "struts-html.tld" 	prefix="html" 	%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>


<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>	
	
<!-- JSP -->
<%
String logo=(String)request.getSession().getAttribute(SIGAConstants.PATH_LOGO);
UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
%>

<html>

<!-- HEAD -->
	<head>
		<title><siga:Idioma key="index.title"/></title>
			
		<!-- ESTILOS Y JAVASCRIPT -->
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script language="JavaScript" type="text/javascript">
			var user, psswd, profile, loc;
			user='<%=userBean.getUserName()%>';
			psswd='clavecita';
/*			profile='< % = userBean.getProfile() % > ';   */
			loc='<%=userBean.getLocation()%>';

			function inicio()
			{
				MM_preloadImages('<%=app%>/html/imagenes/botonSession_ilum.gif',
								 '<%=app%>/html/imagenes/botonSession_activo.gif',
								 '<%=app%>/html/imagenes/botonSession.gif',
								 '<%=app%>/html/imagenes/botonCerrar.gif',
								 '<%=app%>/html/imagenes/botonCerrar_ilum.gif',
								 '<%=app%>/html/imagenes/botonCerrar_activo.gif');
			}

			function cerrarSession()
			{
				MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_activo.gif',1);
				
				/*if(psswd!='')
				{
					top.location='<%=app%>/html/jsp/general/login.jsp'; 
				}
				
				else
				{*/
					top.location='<%=app%>';
				//}
				
				return false;
			}
		
			function cerrarAplicacion()
			{
				MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
				
				//if(confirm('¿Está seguro de que desea abandonar la aplicación?'))
				if(confirm('<siga:Idioma key="general.cerrarAplicacion"/>'))
				{
					window.close();
				}
				
				return false;
			}
		</script>
	</head>

	<body class="tableCabecera" onLoad="inicio();SolicitudIncorporacionAccesoDirectoForm.submit();">
		<html:form action = "/CEN_SolicitudesIncorporacionAccesoDirecto.do" target="mainWorkArea" >
				<html:hidden property = "modo" value = "cargaSolicitudIncorporacion"/>
		</html:form>

		<!-- MENU PRINCIPAL -->
		<!-- Esto pinta el menu principal en funcion de los permisos del userBean -->
		<%--script language="JavaScript1.2" src="<%=app%>/html/js/coolmenus4.js"></script--%>
		<script src="<%=app%>/html/js/coolmenus4.jsp" type="text/javascript"></script>

		<div style="position:absolute; z-index:50;">
		</div>

		<!-- CABECERA GENERAL -->
		<div id="img1" style="position:absolute; left:0px; top:0px; text-align:center; 	vertical-align:middle; height:79px; width:170px; z-index:2">
			<img id="logoImg" src="<%=logo%>" style="vertical-align:middle;">
		</div>
		<div id="img2" style="position:absolute; left:0px; top:0px; text-align:center; height:79px;width:170px; z-index:1; background-color:transparent;">
			<img id="logoSIGA" align="bottom" src="<%=app%>/html/imagenes/logoSIGA.png">
		</div>
		<div style="position:absolute; left:630px; top:35px; z-index: 5;"> 
			<table border=0 cellspacing=0 sellpadding=0 width="350">
			<tr>
			<td>
			<a href="javascript://" class="imageLink" onclick="return cerrarAplicacion();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeApp2','','<%=app%>/html/imagenes/botonCerrar_ilum.gif',1)">
				<img src="<%=app%>/html/imagenes/botonCerrar.gif" alt="<siga:Idioma key="general.cerrarAplicacion"/>" align="middle" name="closeApp2"  border="0">
				&nbsp;<siga:Idioma key="general.cerrarAplicacion"/>
			</a>
			</td>
			<td width="50">
			&nbsp;
			</td>
			<td>
			<a href="javascript://" class="imageLink" onclick="return cerrarSession();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_ilum.gif',1)">
				<img src="<%=app%>/html/imagenes/botonSession.gif" alt="<siga:Idioma key="general.cerrarSesion"/>" align="middle" name="closeSession"  border="0">
				&nbsp;<siga:Idioma key="general.cerrarSesion"/>
			</a>
			</td>
			</tr>
			</table>
		</div>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<div class="posicionTitulo">
			<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td class="titulosLeft" width="10%">
				&nbsp;&nbsp;<input id="barraNavegacion" class="boxCabecera" type="text" name="navegacion" value="" readonly>
				</td>
				<td id="titulo" class="titulos"  width="40%">
				&nbsp;
				</td>
			</tr>
			</table>
		</div>

		<!-- INICIO: IFRAME PRINCIPAL -->

		<!-- Iframe donde se ejecutan todos los mantenimientos -->
		<iframe src=""
				id="mainWorkArea"
				name="mainWorkArea"
				frameborder="0"
				class="posicionPrincipal">
		</iframe>

		<!-- FIN: IFRAME PRINCIPAL -->

		<!-- INICIALIZACION DE ESTILOS -->
		<script language="JavaScript" type="text/javascript">
			initStyles();
			mainWorkArea.location='<%=app%>/html/jsp/general/blank.jsp';
		</script>
	</body>
</html>
