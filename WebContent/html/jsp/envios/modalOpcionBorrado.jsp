<!DOCTYPE html>
<html>
<head>
<!-- modalOpcionBorrado.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- JSP -->



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
  	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body>
		<bean:define id="mensajeradio" name="mensajeradio" scope="request"/>
		
		<html:form action="/ENV_EntradaEnvios.do" method="POST" target="_self">
			<html:hidden property="modo"/>
			<table>
				<tr>
					<td class="labelText" style="width:490px">
						<bean:message key="comunicaciones.etiqueta.confirmarAccionEliminar"/>
					</td>
				</tr>
				<tr>
					<td class="labelText" width="470px">
						<input type="radio" id="act" name="accionG" value="1" checked><label for="act"><bean:message key="comunicaciones.etiqueta.borrarSoloAsignacion"/></input></label>
						<br>
						<input type="radio" id="cre" name="accionG" value="0"><label for="cre">${mensajeradio}</input></label>
						<br>
					</td>
				</tr> 	
			</table>
		</html:form>
		<siga:ConjBotonesAccion botones="A,C" modal="P"/>
		
		<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("");
		}

		//Asociada al boton Cerrar -->
		function accionAceptar() {
			var result = '';
			if (document.forms[0].accionG[0].checked){
				result = 'BORRAR_RELACION';
			}else if(document.forms[0].accionG[1].checked){
				result = 'BORRAR_TODO';
			}
			window.top.returnValue=result;
			window.top.close();
		}
	</script>
</body>
</html>
