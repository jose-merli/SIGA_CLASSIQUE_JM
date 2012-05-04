<!-- main.jsp -->
<!-- EJEMPLO DE VENTANA PRINCIPAL DE LA APLICACION -->
<!-- Contiene la cabecera, el menu principal, el logo y los botones de salir.
	 Ademas contiene la barra de localización y titulo que se actualiza desde
	 cada mantenimiento mostrado en la zona de trabajo.
	 Tiene por lo tanto una zona de trabajo enla que se muestran todos los mantenimientos
-->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>
<!-- CABECERA JSP -->

<!-- TAGLIBS -->
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ReadProperties"%>
<%@ page import="com.atos.utils.BotonesMenu"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.SIGAReferences"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	ReadProperties rproperties = new ReadProperties(
			SIGAReferences.RESOURCE_FILES.SIGA);
	//Parte para el Applet:
	//LMS 19/09/2006
	//Se detecta si es petición HTTP ó HTTPS.
	String sProtocolo = request.getRequestURL().toString()
			.toLowerCase().indexOf("https") > -1 ? "https" : "http";
	String queryString = sProtocolo + "://" + request.getServerName()
			+ ":" + request.getServerPort();
	//     String scodebase = queryString + app + "/html/jsp/general";
	String scodebase = queryString;// + app + "/html/jsp/general";
%>

<!-- JSP -->
<%
	String logo = (String) request.getSession().getAttribute(
			SIGAConstants.PATH_LOGO);
	UsrBean userBean = (UsrBean) request.getSession().getAttribute(
			"USRBEAN");

	String pathInicio = BotonesMenu.getPathCerrarSesion(userBean
			.getLocation());
	String pathAyuda = BotonesMenu.getPathAyuda(userBean.getLocation());
	String pathVersiones = BotonesMenu.getPathVersiones(userBean
			.getLocation());

	CenInstitucionAdm admInstitucion = new CenInstitucionAdm(userBean);
	String nombreInstitucion = admInstitucion
			.getNombreInstitucion(userBean.getLocation());

	String interfaz = (String) request.getSession().getAttribute(
			"InterfazInicio");
	String onLoad = "";
	if (interfaz != null) {
		onLoad = "interfaz();";
	} else {
		onLoad = "";
	}
	request.getSession().removeAttribute("InterfazInicio");
%>

<html>

<!-- HEAD -->
<head>
<title><siga:Idioma key="index.title" />
</title>

<!-- ESTILOS Y JAVASCRIPT -->
<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/css/jquery-ui.css"> 
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script src="<%=app%>/html/js/jquery.js" type="text/javascript"></script>
<script src="<%=app%>/html/js/jquery-ui.js" type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
			var user, psswd, profile, loc;
			user='<%=userBean.getUserName()%>';
			psswd='clavecita';
			loc='<%=userBean.getLocation()%>';

			function inicio()
			{
				MM_preloadImages('<%=app%>/html/imagenes/botonSession_ilum.gif',
								 '<%=app%>/html/imagenes/botonSession_activo.gif',
								 '<%=app%>/html/imagenes/botonSession.gif',
								 '<%=app%>/html/imagenes/botonAyuda.gif',
								 '<%=app%>/html/imagenes/botonAyuda_ilum.gif',
								 '<%=app%>/html/imagenes/botonAyuda_activo.gif');
			}

			function cerrarSession()
			{
				MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_activo.gif',1);
				
				document.formularioOcultoCerrarSesion.submit();
				
				return false;
			}
			
			function usuario()
			{
				MM_swapImage('AbrirUsuario','','<%=app%>/html/imagenes/botonUsuario_activo.gif',1);

				f = "dialogHeight:250px;dialogWidth:450px;status:no;unadorned:no;scroll:no"
//				"width=750,height=200,scrollbars=no;resizable:no;top=100;left=100;Directories=no;Location=no;Menubar=no;Status=yes;Toolbar=no;"
				return showModalDialog("<%=app%>/html/jsp/general/ventanaUsuario.jsp", "", f);
			}
			
			
			function ayuda()
			{
				MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_activo.gif',1);
				window.open('<%=pathAyuda%>', 'Ayuda', 'width=800px,height=600px,scrollbars=1;resizable:no;top=100px;left=100px;Directories=no;Location=no;Menubar=no;Status=yes;Toolbar=no;');
								
				return false;
			}

			function version()
			{
				MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_activo.gif',1);
				window.open('<%=pathVersiones%>', 'Versiones', 'width=800px,height=600px,scrollbars=1;resizable:no;top=100px;left=100px;Directories=no;Location=no;Menubar=no;Status=yes;Toolbar=no;');
								
				return false;
			}
			
			function cerrarAplicacion()
			{
				MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
				
				if(confirm('<siga:Idioma key="general.cerrarAplicacion"/>')){
					window.close();
				}
				
				return false;
			}
			function interfaz()
			{
				mainWorkArea.location='<%=app%>/Dispatcher.do?proceso=80';				
				return false;
			}	
			function establecerIP(dirIP) {		
			    document.formularioOcultoParaIP.IPCLIENTE.value=dirIP;		
		    	document.formularioOcultoParaIP.submit();     
		    }						

			function comprobarPops()
			{
				showModelessDialog('<%=app%>/html/jsp/general/loadingWindowPru.jsp','', 'dialogLeft:2000;dialogTop:2000;dialogWidth:2000px;dialogHeight:2000px;resizable:yes;help:no;center:no;');
			}
			   
		</script>
</head>

<body class="tableCabecera"
	onLoad="inicio();<%=onLoad%>;ajusteAlto('mainWorkArea');comprobarPops();">

	<!-- MENU PRINCIPAL -->
	<!-- Esto pinta el menu principal en funcion de los permisos del userBean -->
	<script src="<%=app%>/html/js/coolmenus4.jsp" type="text/javascript"></script>


	<div style="position: absolute; top: 1px; z-index: 50;">
		<tiles:insert page="/menu.do" flush="true" />
	</div>

	<!-- CABECERA GENERAL -->
	<div id="img1"
		style="position: absolute; left: 0px; top: 0px; text-align: center; vertical-align: middle; height: 79px; width: 170px; z-index: 2">
		<img id="logoImg" src="<%=logo%>" style="vertical-align: middle;">
	</div>
	<div id="img2"
		style="position: absolute; left: 0px; top: 0px; text-align: center; height: 79px; width: 170px; z-index: 1; background-color: transparent;">
		<img id="logoSIGA" align="bottom"
			src="<%=app%>/html/imagenes/logoSIGA.png">
	</div>

	<div style="position: absolute; left: 180px; top: 50px; z-index: 5;">
		<table border=0 cellspacing=0 cellpadding=0>
			<tr>
				<td width="300px"><a href="javascript://" class="imageLink"
					onclick="return version();"> <img
						src="<%=app%>/html/imagenes/botonVersion.gif"
						alt="<siga:Idioma key="general.icono.version"/>" align="middle"
						border="0"> &nbsp;<siga:Idioma key="general.icono.version" />
				</a></td>
				<td width="175px"><a href="javascript://" class="imageLink"
					onclick="return usuario();" onMouseOut="MM_swapImgRestore()"
					onMouseOver="MM_swapImage('AbrirUsuario','','<%=app%>/html/imagenes/botonUsuario_ilum.gif',1)">
						<img src="<%=app%>/html/imagenes/botonUsuario.gif"
						alt="<siga:Idioma key="general.boton.usuario"/>" align="middle"
						name="AbrirUsuario" border="0"> &nbsp;<siga:Idioma
							key="general.boton.usuario" /> </a></td>
				<td width="175px"><a href="javascript://" class="imageLink"
					onclick="return ayuda();" onMouseOut="MM_swapImgRestore()"
					onMouseOver="MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_ilum.gif',1)">
						<img src="<%=app%>/html/imagenes/botonAyuda.gif"
						alt="<siga:Idioma key="general.boton.ayuda"/>" align="middle"
						name="AbrirAyuda" border="0"> &nbsp;<siga:Idioma
							key="general.boton.ayuda" /> </a></td>
				<td width="175px"><a href="javascript://" class="imageLink"
					onclick="return cerrarSession();" onMouseOut="MM_swapImgRestore()"
					onMouseOver="MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_ilum.gif',1)">
						<img src="<%=app%>/html/imagenes/botonSession.gif"
						alt="<siga:Idioma key="general.cerrarSesion"/>" align="middle"
						name="closeSession" border="0"> &nbsp;<siga:Idioma
							key="general.cerrarSesion" /> </a></td>
			</tr>
		</table>
	</div>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<div class="posicionTitulo"  style="width:1000px; height:18px">
		<table class="tablaTitulo" cellspacing="0">
			<tr  style="height:18px">
				<td class="titulosLeft" width="50%">&nbsp;&nbsp;<input
					id="barraNavegacion" class="boxCabecera" type="text"
					name="navegacion" value="" readonly></td>
				<td id="titulo" class="titulos" width="50%">&nbsp;</td>
			</tr>
		</table>
	</div>

	<!-- INICIO: IFRAME PRINCIPAL -->

	<!-- Iframe donde se ejecutan todos los mantenimientos -->
	<iframe src="<%=app%>/html/jsp/general/entrada.jsp" id="mainWorkArea"
		name="mainWorkArea" frameborder="0" scrolling="no"
		class="posicionPrincipal"> </iframe>

	<!-- FIN: IFRAME PRINCIPAL -->

	<!-- INICIALIZACION DE ESTILOS -->
	<script language="JavaScript" type="text/javascript">
			initStyles();
			mainWorkArea.location='<%=app%>/html/jsp/general/entrada.jsp';
		</script>



	<div id="velo">
		<table class="tvelo">
			<tr>
				<td><siga:Idioma key="messages.enProceso" />
				</td>
			</tr>
		</table>
	</div>

	<form name="formularioOcultoCerrarSesion" target="submitApplet"
		action="<%=app%>/html/jsp/general/cerrarSesion.jsp"></form>

	<iframe name="frmAppletIP" src="<%=app%>/html/jsp/general/appletIP.jsp"
		style="display: none"></iframe>

	<form name="formularioOcultoParaIP" target="submitApplet"
		action="/SIGA/SIGADireccionIP.svrl">
		<input type="hidden" name="IPCLIENTE" value="">
	</form>


	<iframe name="submitApplet" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"> </iframe>

	<!-- APPLET IP -->
	<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" width="0"
		height="0" codebase="<%=scodebase%>">
		<param name="archive" value="AppletDireccionIP.jar">
		<param name="code" value="com.siga.Utilidades.AppletDireccionIP.class">
		<param name="codebase" value="<%=scodebase%>">
		<param name="mayscript" value="true">
	</object>
	<!-- APPLET IP -->


	<div id="dialog-message" title="SIGA" style="vertical-align: center;"></div>
	<script>

	function jAlert(texto, ancho, alto){
		$("#dialog-message").html(texto);
		$("#dialog-message").height(alto);
		$("#dialog-message").dialog({
			modal: true,
			resizable: false,
			width: ancho,
			height: alto,
			buttons: { "Ok": function() { $(this).dialog("close"); $(this).dialog("destroy"); } }
		});
		$("#dialog-message").scrollTop(0);
	}
	
</script>
</body>
</html>