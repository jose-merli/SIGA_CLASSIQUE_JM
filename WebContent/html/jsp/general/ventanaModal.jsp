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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	String msg = request.getParameter("msg");
	
	String tScroll="no";
	//String scroll = (String)ses.getAttribute("ScrollModal");
	String scroll = request.getParameter("scroll");
	if (scroll!=null) {
		tScroll = "auto";
	}
	//ses.removeAttribute("ScrollModal");
	
%>	


<html>
	<!-- HEAD -->
	<head>
		<title>
			<siga:Idioma key="general.ventana.cgae"/>
		</title>

		
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<%=app%>/html/js/jquery.blockUI.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/jquery.notice.js" type="text/javascript"></script>
		
		<script type="text/javascript">
		
			jQuery.noConflict();
			
			function cargaContenidoModal() {
				var datos = window.dialogArguments;
				var formu = document.createElement('form');
				formu.setAttribute('method', 'POST');
				formu.setAttribute('target', 'modal');
				for (var i=0;i<datos.length;i++) {
					if (datos[i].value) {
						if (datos[i].name=='actionModal') {
							formu.setAttribute('action', datos[i].value);
						} else {						
							var el = document.createElement("input");
							el.setAttribute("type", "hidden")
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
				jQuery(document).ready(
					function() { 
						jQuery.blockUI({
							message: '<span class="labelText">'+msg+'</span><br><img src="<%=app%>/html/imagenes/loadingBar.gif"/><span id="barraBloqueante">&nbsp;</span>', 
							css:{border:0,left:'300px', background:'transparent'},
							overlayCSS: { backgroundColor:'#000', opacity: .0} }); 
						jQuery("#barraBloqueante").click(function() { 
							jQuery.unblockUI(); 
						});
					}
				)
				bloqueado=true; 
			}

			function mainFin(){
				if(bloqueado){
					jQuery(document).ready(
						function() { 
					    	jQuery.unblockUI(); 
						}
					)
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

	<body class="tablaCentralCampos" onLoad="cargaContenidoModal();">
	<table>
	<tr>
		<td class="labelTextValor"><c:out value="Conectando con la Mutua de la Abogac�a..." /> </td>
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

	</body>
</html>
