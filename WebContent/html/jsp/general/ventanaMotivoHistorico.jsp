<!-- ventanaMotivoHistorico.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
	 	18-01-2005 Validacion empleando javascript
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.beans.GenParametrosAdm"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.beans.*"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
%>
<html>

<!-- HEAD -->
<head>
<title><siga:Idioma key="general.ventana.cgae"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script type="text/javascript">
		
		jQuery.noConflict();

		<!-- Asociada al evento onload -->
		function setDefault(){ 
			var datos = new Array();
			datos[0] = 0;
			datos[1] = "";
			window.top.returnValue=datos;
		}	
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar(){ 
			setDefault();
			window.top.close();
		}	
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			if (document.datosCVForm.motivo.value.length < 1) {
				var mensaje = "<siga:Idioma key="messages.censo.historico.motivo"/>";
				alert (mensaje);
				return;
			}
			if (document.datosCVForm.motivo.value.length > 255) {
				var mensaje = "<siga:Idioma key="messages.censo.historico.motivoLargo"/>";
				alert (mensaje);
				return;
			}
			
			var datos = new Array();
			datos[0] = 1;
			datos[1] = document.datosCVForm.motivo.value;
			window.top.returnValue = datos;
			window.top.close();
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

<body onload="setDefault();">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma key="censo.consultaHistorico.cabecera"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<fieldset>
	<html:form action="/CEN_DatosCV.do" method="POST">
		<table class="tablaCampos" align="center">	
			<tr>
				<td class="labelText" width="80px"><siga:Idioma key="censo.datosCV.literal.motivo"/></td>
				<td><textarea cols="80" rows="4" name="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" class="box" style="width:100%; overflow:hidden"></textarea></td>			   	
			</tr>
		</table>
	</html:form>
	</fieldset>

		<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo=''  modal="P"/>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
