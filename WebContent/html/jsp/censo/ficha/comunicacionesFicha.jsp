<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
<!-- comunicacionesFicha.jsp-->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<!-- TAGLIBS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script type='text/javascript' src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<title><siga:Idioma key="pestana.justiciagratuitaejg.comunicacion"/></title>
	<siga:Titulo  titulo="pestana.justiciagratuitaejg.comunicacion"  localizacion="censo.fichaCliente.localizacion"/>
</head>

<body onload="inicio();">
	<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean" />
	<bean:define id="busquedaVolver" name="busquedaVolver" type="java.lang.String" />
	
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<div>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo"  class="titulitosDatos" style="font:normal" >
				<siga:Idioma
						key="pestana.fichaCliente.comunicaciones" /> <c:out
						value="${descripcionColegiado}"></c:out>
			</td>
		</tr>
	</table>
</div>


	<html:form action="${path}" target="mainWorkArea">
		<html:hidden styleId = "modo" property="modo"  />
		<html:hidden styleId = "idPersona" property="idPersona"  />
		<html:hidden styleId = "idInstitucion" property="idInstitucion"  />
	</html:form>
	<div id = "divListado">	
	</div>

<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

</body>
<script type="text/javascript">
	function inicio() {
		var idInstitucion = document.ComunicacionesForm.idInstitucion.value;
		var idPersona = document.ComunicacionesForm.idPersona.value;
		var data = "idInstitucion=" + idInstitucion+"&idPersona=" + idPersona;

		sub();
		var accion = document.ComunicacionesForm.action;
		jQuery.ajax({
			type : "POST",
			url : accion + "?modo=listadoComunicacionesAjax",
			data : data,
			success : function(response) {
				jQuery('#divListado').html(response);
				fin();
			},
			error : function(e) {
				fin();
				alert('Error: ' + e);
			}
		});

	}	

</script>
</html>