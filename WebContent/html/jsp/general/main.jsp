<!DOCTYPE html>
<html class="blueback">
<head>
<!-- main.jsp -->
<!-- EJEMPLO DE VENTANA PRINCIPAL DE LA APLICACION -->
<!-- Contiene la cabecera, el menu principal, el logo y los botones de salir.
	 Ademas contiene la barra de localización y titulo que se actualiza desde
	 cada mantenimiento mostrado en la zona de trabajo.
	 Tiene por lo tanto una zona de trabajo enla que se muestran todos los mantenimientos
-->

<!-- CABECERA JSP -->
<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>
<!-- CABECERA JSP -->

<!-- TAGLIBS -->
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri ="struts-html.tld" prefix="html"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<%@ page import="com.atos.utils.BotonesMenu"%>
<%@ page import="java.util.Properties"%>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();	
	String versionSiga = (String) request.getAttribute("versionSiga");
	UsrBean userBean = (UsrBean) ses.getAttribute("USRBEAN");
	
	if (userBean == null)
		response.sendError(response.SC_UNAUTHORIZED);
	
	String pathInicio = BotonesMenu.getPathCerrarSesion(userBean.getLocation());
	String pathAyuda = BotonesMenu.getPathAyuda(userBean.getLocation());
	String pathVersiones = BotonesMenu.getPathVersiones(userBean.getLocation());
	
	
	ReadProperties rproperties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	
	//Parte para el Applet:
	//LMS 19/09/2006
	//Se detecta si es petición HTTP ó HTTPS.
	String sProtocolo = request.getRequestURL().toString()
			.toLowerCase().indexOf("https") > -1 ? "https" : "http";
	String queryString = sProtocolo + "://" + request.getServerName()
			+ ":" + request.getServerPort();
	//     String scodebase = queryString + app + "/html/jsp/general";
	String scodebase = queryString;// + app + "/html/jsp/general";

	String logo = (String) ses.getAttribute(SIGAConstants.PATH_LOGO);	

	CenInstitucionAdm admInstitucion = new CenInstitucionAdm(userBean);
	String nombreInstitucion = admInstitucion
			.getNombreInstitucion(userBean.getLocation());

	String interfaz = (String) ses.getAttribute(
			"InterfazInicio");
	String onLoad = "";
	if (interfaz != null) {
		onLoad = "interfaz();";
	} else {
		onLoad = "";
	}
	ses.removeAttribute("InterfazInicio");
	String idAnalytics = rproperties.returnProperty(SIGAConstants.ID_ANALYTICS);
%>






<!-- HEAD -->

	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title><siga:Idioma key="index.title" /></title>


<!-- ESTILOS Y JAVASCRIPT -->	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.blockUI.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/dropdownchecklist/ui.dropdownchecklist-1.4-min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<style type="text/css">
	.notice-wrap {
		position: absolute;
		top: 40px;
		z-index: 9999;
		left: 175px;
		width: 825px;
	}
	.notice-item{
		left:0px;
		margin: 0px;
	}
	</style>
	
	<script type="text/javascript">
	jQuery.noConflict();
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', '<%=idAnalytics%>']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();	

			var user, psswd, profile, loc, bloqueado=false;
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
			
					jQuery.ajax({ //Comunicación jQuery hacia JSP  
							type: "POST",
					url: "/SIGA/GEN_InformacionUsuario.do?modo=getAjaxObtenerInfoUsuario",
					data: "idCombo=",
					dataType: "json",
					success: function(json){	
						if(json.Encontrado =="SI"){
							$("#dialogoInsercion").html("");
							$("#dialogoInsercion").append("<b>Nombre: </b>");
							$("#dialogoInsercion").append(json.Nombre + "</br>");
							$("#dialogoInsercion").append("<b>DNI: </b>");
							$("#dialogoInsercion").append(json.DNI + "</br>");
							$("#dialogoInsercion").append("<b>Grupo: </b>");
							$("#dialogoInsercion").append(json.Grupo+ "</br>");
							$("#dialogoInsercion").append("<b>Institución: </b>");
							$("#dialogoInsercion").append(json.Institucion+ "</br>");
							$("#dialogoInsercion").append("<b>Fecha último acceso: </b>");
							$("#dialogoInsercion").append(json.FechaAcceso);
							$("#dialogoInsercion").dialog(
									{
									      height: 270,
									      width: 525,
									      modal: true,
									      resizable: false,
									      buttons: {
									    	  "Cerrar": function() {
													$(this).dialog("close");
												}
									      }
									}
								);
						}else{
							alert('No existe Usuario de acceso en el sistema ');
						}
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});				
				$(".ui-widget-overlay").css("opacity","0");
				window.top.focus();
				return false;
			}
			
			
			function ayuda()
			{
				MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_activo.gif',1);
				window.open('<%=pathAyuda%>', 'Ayuda', 'width=800px,height=600px,scrollbars=1;resizable:no;top=100px;left=100px;Directories=no;Location=no;Menubar=no;Status=yes;Toolbar=no;');
				window.top.focus();
				return false;
			}

			function version()
			{
				MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_activo.gif',1);
				window.open('<%=pathVersiones%>', 'Versiones');
				window.top.focus();				
				return false;
			}
			
			function cerrarAplicacion()
			{
				MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
				
				if(confirm('<siga:Idioma key="general.cerrarAplicacion"/>')){
					window.top.close();
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

			function mainSub(msg){
				if(!bloqueado){
					if (typeof msg == "undefined")
						msg = "";
					if (jQueryTop("#mainWorkArea").length > 0 && 
							typeof jQueryTop("#mainWorkArea")[0].contentWindow != "undefined" && 
							typeof jQueryTop("#mainWorkArea")[0].contentWindow.jQuery != "undefined"){
						var mainWorkAreaJquery = jQueryTop("#mainWorkArea")[0].contentWindow.jQuery;
						try{
							mainWorkAreaJquery.blockUI({
								message: '<div id="barraBloqueante"><span class="labelText">'+msg+'</span><br><img src="<%=app%>/html/imagenes/loadingBar.gif"/></div>', 
								css:{border:0, background:'transparent'},
								overlayCSS: { backgroundColor:'#FFF', opacity: .0} });
						}catch (e) {
							console.debug("[mainSub] blockUI");
							jQuery("#divEspera").show();
						}
						
					} else{
						jQuery("#divEspera").show();
					}
					bloqueado=true;
				}
			}

			function mainFin(){
				if(bloqueado){
					if (jQueryTop("#mainWorkArea").length > 0 && 
							typeof jQueryTop("#mainWorkArea")[0].contentWindow != "undefined" && 
							typeof jQueryTop("#mainWorkArea")[0].contentWindow.jQuery != "undefined"){
						var mainWorkAreaJquery = jQueryTop("#mainWorkArea")[0].contentWindow.jQuery;
						try {
							mainWorkAreaJquery.unblockUI();
						} catch (e) {
							console.debug("[mainFin] unblockUI");
						}
					}
					jQuery("#divEspera").hide();
					bloqueado=false; 
				} 
			}
			
			function growl(msg,type){
				jQuery('.notice-item-wrapper').remove();
				jQuery.noticeAdd({
					text: msg,
					type: type
				});
			}
			
		</script>
</head>

<body class="tableCabecera"
	onLoad="inicio();<%=onLoad%>;ajusteAlto('mainWorkArea');">
	
	<!-- MENU PRINCIPAL -->
	
	
<div class="cabeceraGnal"> <%--/*h*/ Defino una capa que englobe la cabecera --%> 

	<!-- Esto pinta el menu principal en funcion de los permisos del userBean -->
	<script src="<%=app%>/html/js/coolmenus4.jsp" type="text/javascript"></script>

		<div style="position: absolute; top: 1px; z-index: 50;">
			<tiles:insert page="/menu.do" flush="true" />
		</div>

	<!-- CABECERA GENERAL -->
		<div id="img1" style="position: absolute;z-index:2; width:168px;text-align: center">
			<img id="logoImg" src="<%=logo%>" style="vertical-align: middle;">
		</div>
		<div id="img2" style="position: absolute; top: 1px;"><img id="logoSIGA" align="top"src="<%=app%>/html/imagenes/logoSIGA.png"></div>
	
		<div style="position: absolute; left: 180px; top: 48px; z-index: 0;">
			<table border=0 cellspacing=0 cellpadding=0>
				<tr>
				<td width="300px">
				<a href="javascript://"  class="imageLink" onclick="return version();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('AbrirVersiones','','<%=app%>/html/imagenes/botonVersion.gif',1)" >
					<img src="<%=app%>/html/imagenes/botonVersion.gif" alt="<%= versionSiga %>" align="middle" name="AbrirVersiones" border="0">
<% if (versionSiga == null || "".equals(versionSiga.trim())) {
%>
					&nbsp;<siga:Idioma key="general.icono.version"/>
<%
} else {
%>
					&nbsp;<%= versionSiga %>
<%
}
%>
				</a>
				</td>
				<td width="175px">
					<a href="javascript://" class="imageLink" onclick="return usuario();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('AbrirUsuario','','<%=app%>/html/imagenes/botonUsuario_ilum.gif',1)">
					<img src="<%=app%>/html/imagenes/botonUsuario_ilum.gif" alt="<siga:Idioma key="general.boton.usuario"/>" align="middle" name="AbrirUsuario"  border="0">
					&nbsp;<siga:Idioma key="general.boton.usuario"/>
				</a>
				</td>
				<td width="175px">
				<a href="javascript://" class="imageLink" onclick="return ayuda();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('AbrirAyuda','','<%=app%>/html/imagenes/botonAyuda_activo.gif',1)">
					<img src="<%=app%>/html/imagenes/botonAyuda_activo.gif" alt="<siga:Idioma key="general.boton.ayuda"/>" align="middle" name="AbrirAyuda"  border="0">
					&nbsp;<siga:Idioma key="general.boton.ayuda"/>
				</a>
				</td>
				<td width="175px">
				<a href="javascript://" class="imageLink" onclick="return cerrarSession();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession.gif',1)">
					<img src="<%=app%>/html/imagenes/botonCerrar_activo.gif" alt="<siga:Idioma key="general.cerrarSesion"/>" align="middle" name="closeSession"  border="0">
					&nbsp;<siga:Idioma key="general.cerrarSesion"/>
				</a>
				</td>
				</tr>
				</table>
		</div>
	
			<!-- TITULO -->

			<!-- Barra de titulo actualizable desde los mantenimientos -->

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->

</div>
		<div id="posicionTitulo" class="posicionTitulo">
			<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td class="titulos" width="60%"><input id="barraNavegacion" class="boxCabecera" type="text" name="navegacion" value="" readonly onchange="pushGA()"></td>	
				<td id="titulo" class="titulos titGrande" width="40%">				
				&nbsp;
				</td>
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


	<form name="formularioOcultoCerrarSesion" target="submitApplet"
		action="<%=app%>/html/jsp/general/cerrarSesion.jsp"></form>


	<iframe name="submitApplet" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"> </iframe>



	<div id="dialog-message" title="SIGA" style="vertical-align: center;"></div>
	<script>

	function jAlert(texto, ancho, alto){
		jQuery.noConflict();
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
<div id="main_overlay" class="overlay" style="display:none;z-index: 50;"></div>
	<div id="divEspera" title="Espere por favor" style="z-index:100; position:absolute;vertical-align: center;display:none; top:45%; left:450px">
		<span class="labelText"></span><br><img src="<%=app%>/html/imagenes/loadingBar.gif"/><span id="barraBloqueante">&nbsp;</span>
	</div>
	<div id="dialogoInsercion"  title='Datos Usuario' style="display: none">
	</div>
</body>
</html>