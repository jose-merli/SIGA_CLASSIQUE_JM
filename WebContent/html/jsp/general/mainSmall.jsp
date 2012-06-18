<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- mainSmall.jsp -->
<html>
<% String app=request.getContextPath(); %>
<head>
	<title>Sistema de Gestión de la Abogacía</title>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
	<script type="text/javascript">
		var user, psswd, profile, loc;
		user=getParameterValue('user');
		psswd=getParameterValue('psswd');		
		profile=getParameterValue('profile');
		loc=getParameterValue('location');
				
		function cerrarSession() {
			MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_activo.gif',1);
			if(psswd!='')
				top.location='<%=app%>/html/jsp/general/login.jsp'; 
			else top.location='<%=app%>/html/jsp/index.jsp'; 
			return false;
		}
	
		function cerrarAplicacion() {
			MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
			if(confirm('¿Está seguro de que desea abandonar la aplicación?'))
				window.top.close(); 
			return false;
		}
		function inicio() {
			MM_preloadImages('<%=app%>/html/imagenes/botonSession_ilum.gif',
							'<%=app%>/html/imagenes/botonSession_activo.gif',
							'<%=app%>/html/imagenes/botonCerrar_ilum.gif',
							'<%=app%>/html/imagenes/botonCerrar_activo.gif'
							);
		}
	</script>
	
</head>

<body onLoad="inicio();">
		<%--script language="JavaScript1.2" src="<%=app%>/html/js/coolmenus4.js"></script--%>
		<script src="<%=app%>/html/js/coolmenus4.jsp" type="text/javascript"></script>

/*****************************************************************************
Copyright (c) 2001 Thomas Brattli (webmaster@dhtmlcentral.com)

DHTML coolMenus - Get it at coolmenus.dhtmlcentral.com
Version 4.0_beta
This script can be used freely as long as all copyright messages are
intact.

Extra info - Coolmenus reference/help - Extra links to help files **** 
CSS help: http://coolmenus.dhtmlcentral.com/projects/coolmenus/reference.asp?m=37
General: http://coolmenus.dhtmlcentral.com/reference.asp?m=35
Menu properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=47
Level properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=48
Background bar properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=49
Item properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=50
******************************************************************************/
/* Permission granted to SimplytheBest.net to feature script in its 
DHTML script collection at http://simplythebest.net/info/dhtml_scripts.html */ 
</script>
<script language="JavaScript1.2" src="javascript/menuSmall.js"></script>
<div id="img1" style="position:absolute; left:0px; top:0px; text-align:center; 	vertical-align:middle; height:79px; width:170px; z-index:2"> 
  <img id="logoImg" src="<%=app%>/html/imagenes/logoconsejo2.gif" style="vertical-align:middle;">
 </div>
<div id="img2" style="position:absolute; left:0px; top:0px; text-align:center; height:79px;width:170px; z-index:1; background-color:transparent;">
<img id="logoSIGA" align="bottom" src="<%=app%>/html/imagenes/logoSIGA.png">
</div>
<div style="position:absolute; left:500px; top:36px; z-index: 5;"> 
  <!--<a href="javascript://" class="imageLink" onclick="if(confirm('¿Está seguro de que desea abandonar la aplicación?'))window.top.close(); else return false;">
<img src="<%=app%>/html/imagenes/botonCerrar.gif" align="middle" border="0">&nbsp;Cerrar Aplicación</a>-->
  <a href="javascript://" class="imageLink" onclick="return cerrarAplicacion();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_ilum.gif',1)"> 
  <img src="<%=app%>/html/imagenes/botonCerrar.gif" alt="Cerrar Aplicacion" align="middle" name="closeApp" width="27" height="27" border="0"> 
  &nbsp;Cerrar Aplicaci&oacute;n </a> &nbsp;&nbsp; <a href="javascript://" class="imageLink" onclick="return cerrarSession();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_ilum.gif',1)"> 
  <img src="<%=app%>/html/imagenes/botonSession.gif" alt="Cerrar Sesion" align="middle" name="closeSession" width="27" height="27" border="0"> 
  &nbsp;Cerrar Sesi&oacute;n </a> 
  <!--<a href="javascript://" class="imageLink" onClick="if(psswd!='')top.location='../login.html'; else top.location='../index.html'; return false;" target="_top">
<img src="<%=app%>/html/imagenes/botonSession.gif" align="middle" border="0">&nbsp;Cerrar Sesión</a>-->
</div>
<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainWorkArea"
			name="mainWorkArea"
			scrolling="no"
			frameborder="0"
			style="position:absolute; width:100%; height:86%; z-index:3; top: 80px; left: 0px"> 
</iframe>
<script language="JavaScript" type="text/javascript">
	initStyles();
	if(profile=='NCL') {
		mainWorkArea.location='<%=app%>/html/jsp/censo/solIncorporacion.jsp';
	} else {
		mainWorkArea.location='<%=app%>/html/jsp/censo/inicioCenso.jsp';
	} 
</script>
</body>
</html>
