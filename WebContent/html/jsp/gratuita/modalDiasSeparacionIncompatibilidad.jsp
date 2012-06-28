<!-- modalDiasSeparacionIncompatibilidad.jsp -->

<!---------- CABECERA JSP ---------->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!---------- TAGLIBS ---------->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>


<!---------- IMPORTS ---------->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.beans.GenParametrosAdm"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.beans.*"%>


<!---------- JSP ---------->
<% 
	String app=request.getContextPath();
%>

<html>

<!---------- HEAD ---------->
<head>
<title><siga:Idioma key="general.ventana.cgae"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/jquery.blockUI.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/jquery.notice.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
    
	<script language="JavaScript">

		//Asociada al evento onload
		function setDefault () {
			var datos = new Array ();
			datos[0] = 0;
			datos[1] = "";
			datos[2] = "";
			window.top.returnValue=datos;
		}	
		
		//Asociada al boton Cerrar
		function accionCerrar () {
			setDefault ();
			window.close ();
		}	
		
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar () {
            var dias = document.forms[0].diasseparacion.value;
            var moti = document.forms[0].motivos.value;
            sub();
            //validando que se haya introducido un numero
			if (dias.length < 1 || isNaN (dias)) {
				var mensaje = "<siga:Idioma key="messages.gratuita.incompatibilidadesGuardias.recuerdaDiasSeparacion"/>";
				alert (mensaje);
				fin();
				return false;
			}
			
			//devolviendo los datos a la pantalla anterior
			var datos = new Array();
			datos[0] = 1;
			datos[1] = dias;
			datos[2] = moti;
			window.top.returnValue = datos;
			window.top.close();
		}
		
		function mainSub(){
				$(document).ready(
				function() { 
					$.blockUI({
						message: '<img src="<%=app%>/html/imagenes/loadingBar.gif">', 
						css:{border:0, background:'transparent'},
						overlayCSS: { backgroundColor:'#000', opacity: .0} }); 
				}
			)
			bloqueado=true; 
		}

		function mainFin(){
			if(bloqueado){
				$(document).ready(
					function() { 
				    	$.unblockUI(); 
					}
				)
				bloqueado=false;
			} 
		}
		
		function growl(msg,type){
			//$.noticeRemove($('.notice-item-wrapper'), 400);
			$.noticeAdd({
				text: msg,
				type: type
			});
		}

	</script>	
</head>


<!---------- BODY ---------->
<body onload="setDefault();">

	<!-- INICIO: Titulo -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr><td id="titulo" class="titulosPeq">
		    <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.diasSeparacionGuardias" />
		</td></tr>
	</table>
	<!-- FIN: Titulo -->
	
	<!-- INICIO: Campos -->
	<fieldset>
	<html:form action="/JGR_MantenimientoIncompatibilidadesGuardias.do"
	           method="POST">
		<table class="tablaCampos" align="center">	
			<tr>
				<td class="labelText">
				    <siga:Idioma key="messages.gratuita.incompatibilidadesGuardias.pideDiasSeparacion" />
				</td>
				<td style="text-align: center;">
				    <input type="text" maxlength="2" size="1"
				           style="text-align: right;"
				           name="diasseparacion" />
				</td>
			</tr>
		</table>
		<table class="tablaCampos" align="center">	
			<tr>
			    <td class="labelText">
			    	<br><siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.motivos" />
			    </td>
			    <td>
				    <textarea class="box" type="text" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" maxlength="1024" rows="3" name="motivos">
				    </textarea>
				</td>
			</tr>
		</table>
	</html:form>
	</fieldset>
	<!-- FIN: Campos -->
    
    <!-- INICIO: Botones -->
	<siga:ConjBotonesAccion botones='C,Y' modo='' modal="P" />
	<!-- FIN: Botones -->

</body>

</html>