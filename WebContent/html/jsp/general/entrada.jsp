<!DOCTYPE html>
<html>
<head>
<!-- entrada.jsp -->
 <%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.atos.utils.UsrBean"%>
<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	if (usr==null) usr=UsrBean.UsrBeanAutomatico("2000");

	String lenguaje = usr.getLanguage();
%>


	
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	    <title><siga:Idioma key="index.title"/></title>

<style>
.labelEntrada{
	text-align: left;
	font-family: Arial;
	color:#000000;
	font-size: 12px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
} 

.botonAcuerdo{
  background: #597db1;
  border: 1px solid #3b639b;
  border-radius: 6px;
  color: white;
  position: relative;
  margin-left: 20px;
  padding: 2px;
  cursor: hand;
}

</style>	    
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<style>
	.divCookies{
		background
	}
	</style>
	</head>
	<body style="background-color:#ffffff;">
	
	<div style="position:absolute;width:100%;">
		<% if (lenguaje.equals("2")) { // en catalan %>	
			<img src="<%=app%>/html/imagenes/logoSIGAblanco_CA.JPG" style="display:block;margin-left:auto;margin-right:auto;" />
		<% } else { %>	
			<img src="<%=app%>/html/imagenes/logoSIGAblanco.JPG" style="display:block;margin-left:auto;margin-right:auto;" />
		<% } %>	
	</div>
	
	<div class="patrocinador" style="position:absolute;left:20px;bottom:20px;">
	<siga:Idioma key="messages.entrada.portada"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoRedAbogacia.jpg" aling="center" valign="bottom" />
	</div>
	
	<div class="patrocinador" style="position:absolute;right:20px;bottom:20px;">
	<siga:Idioma key="messages.entrada.escrito"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoMinisterio.jpg"  aling="center" valign="bottom"/>
	<img src="<%=app%>/html/imagenes/logoAvanza.jpg"  aling="center" valign="bottom"/>
	</div>
	
	<div id="cookie" style="position: absolute; left: 0px; bottom: -50px; width: 100%; background-color:#01446E; z-index: 9999; height:50px;">
		
		<div style="max-width:900px;margin-left:auto;margin-right:auto; padding:8px; vertical-align:center">
			<h3 style="letter-spacing:-0.5px; font-weight:normal;margin:0;padding:0;color:#efefef;display: block;float: left;height:40px;line-height: 20px;text-align: right;max-width:140px;font-size:15px;vertical-align:center">Uso de cookies en Abogacía Española</h3>
			<p style="color:#BEBEBE;display: block;float:left;margin:2px 0 0 30px;padding:0;max-width:550px;font-size:11px;">
			Utilizamos cookies propias y de analítica para mejorar tu experiencia de usuario. Si continúas navegando, consideramos que aceptas su uso. <a href="http://www.abogacia.es/informacion-sobre-cookies/" target="new" style="color:#ffffff;">Más información</a>.
			</p>
			<input type="button" id="estoyDeAcuerdo" class="botonAcuerdo" value="Estoy de acuerdo" onClick="ocultarAcuerdo()" title="Acepto el uso de cookies">
		</div>
		
	</div>
	
	<script>
		 jQuery(document).ready(function(){
			 checkCookie();
		 });
		 
	 	function setCookie(c_name, value, exdays) {
			var exdate = new Date();
			exdate.setDate(exdate.getDate() + exdays);
			var c_value = escape(value)
					+ ((exdays == null) ? "" : "; expires="
							+ exdate.toUTCString());
			document.cookie = c_name + "=" + c_value;
		}

		function getCookie(c_name) {
			var c_value = document.cookie;
			var c_start = c_value.indexOf(" " + c_name + "=");
			if (c_start == -1) {
				c_start = c_value.indexOf(c_name + "=");
			}
			if (c_start == -1) {
				c_value = null;
			} else {
				c_start = c_value.indexOf("=", c_start) + 1;
				var c_end = c_value.indexOf(";", c_start);
				if (c_end == -1) {
					c_end = c_value.length;
				}
				c_value = unescape(c_value.substring(c_start, c_end));
			}
			return c_value;
		}
			
	 	function mostrarAcuerdo() {
			jQuery("#cookie").animate({bottom : "+=50"}, 500, function() {});
			jQuery(".patrocinador").animate({bottom : "+=50"}, 500, function() {});
		}

		function ocultarAcuerdo() {
			setCookie("aceptaAcuerdo",1,3000);
			jQuery("#cookie").animate({bottom : "-=50"}, 500, function() {});
			jQuery(".patrocinador").animate({bottom : "-=50"}, 500, function() {});
		}

		function checkCookie() {
			var aceptaAcuerdo = getCookie("aceptaAcuerdo");
			if (aceptaAcuerdo == null) mostrarAcuerdo();
		}
	</script>
	</body>
</html>