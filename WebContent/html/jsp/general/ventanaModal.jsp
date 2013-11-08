<!DOCTYPE html>
<html>
<head>
<!-- ventanaModal.jsp -->
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
<%@page import="java.util.Properties"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	String msg = request.getParameter("msg");
	
	String tScroll="no";
	//String scroll = (String)ses.getAttribute("ScrollModal");
	String scroll = request.getParameter("scroll");
	if (scroll!=null) {
		tScroll = "auto";
	}
	//ses.removeAttribute("ScrollModal");
	
%>	



	<!-- HEAD -->
	
		<title>
			<siga:Idioma key="general.ventana.cgae"/>
		</title>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.blockUI.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<style type="text/css">
	
	</style>
	
	<script type="text/javascript">
			var bloqueado=false;
			
			function growl(msg,type){
				jQuery('.notice-item-wrapper').remove();
				try{
					jQuery.noticeAdd({
						text: msg,
						type: type
					});
				} catch(e){
					alert(msg);
				}
			}
		
			function cargaContenidoModal() {
				var datos = window.top.dialogArguments;
				
				var formu = document.createElement('form');
				formu.setAttribute('method', 'POST');
				formu.setAttribute('target', 'modal');
				for (var i=0;i<datos.length;i++) {
					if (datos[i].value) {
						
						if (datos[i].name=='actionModal') {
							formu.setAttribute('action', datos[i].value);
							
						} else {						
							var el = document.createElement("input");
							el.setAttribute("type", "hidden");
							el.setAttribute("name", datos[i].name);
							el.setAttribute("id", datos[i].name);
							el.setAttribute("value", datos[i].value);
							formu.appendChild(el);
						}
					}
				}
				document.body.appendChild(formu);
				formu.submit();
			}		
			
			function mainSub(msg){
				if(!bloqueado){
					if (typeof msg == "undefined")
						msg = "";
					try{
 						jQuery.blockUI({
							message: '<div id="barraBloqueante"><span class="labelText">'+msg+'</span><br><img src="<%=app%>/html/imagenes/loadingBar.gif"/></div>', 
							css:{border:0, background:'transparent'},
							overlayCSS: { backgroundColor:'#FFF', opacity: .0} });
					} catch(e){
						jQuery("#divEspera").show();
					}
					bloqueado=true;
				}
			}

			function mainFin(){
				if(bloqueado){
					try{
	 					jQuery.unblockUI();
					} catch(e){
					}
					jQuery("#divEspera").hide();
					bloqueado=false; 
				} 
			}
		</script>
</head>

<body class="tablaCentralCampos" onLoad="cargaContenidoModal();">
	<div id="main_overlay" class="overlay" style="display:none;z-index: 50;"></div>
	<table>
	<tr>
		<td class="labelTextValor"><c:out value="Conectando con la Mutua de la Abogacía..." /> </td>
	</tr>
	</table>
	
	<!-- IFRAME GENERAL -->
	<iframe align="center" src="<%=app%>/html/jsp/general/loadingWindow.jsp?msg=<%=msg%>"
					id="modal"
					name="modal" 
					scrolling="<%=tScroll %>"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					style="position:absolute; width:100%; height:100%; z-index:2; top: 0px; left: 0px">
	</iframe>
	
	<div id="divEspera" title="Espere por favor" style="z-index:100;position:absolute;display:none;top:45%;left:50%">
		<div style="position:relative;left:-50%">
			<br><img src="<%=app%>/html/imagenes/loadingBar.gif"/>
		</div>
	</div>
</body>
</html>
