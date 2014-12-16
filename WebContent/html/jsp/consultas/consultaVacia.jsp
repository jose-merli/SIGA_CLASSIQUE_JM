<!DOCTYPE html>
<html>
<head>
<!-- consultaVacia.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
</head>

<body>


	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" align="center">

	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>	
	
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.consulta">
			<table class="tablaCampos" align="center">
			<tr>
				<td class="labelText" align="center">
					<br>
					<siga:Idioma key="consultas.recuperarconsulta.error.consultaVacia"/>
				</td>				
			</tr>				
			</table>
		</siga:ConjCampos>
		</td>
	</tr>
	</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

<table id="tablaBotonesDetalle" class="botonesDetalle" align="center" style="bottom: 26px;">
	<tr>
		<tr>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.volver"/>' name='idButton' id="idButton" onclick="return accionVolver();" class="button" value='<siga:Idioma key="general.boton.volver"/>'>
			</td>
			<td  style="width:900px;">
			&nbsp;
			</td>
		</tr>
	</tr>
</table>

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	function accionVolver() 
	{		
		var formu=document.forms[0];
		formu.action=formu.action+"?noReset=true&buscar=true";
		if(parent.document.getElementById("accionAnterior")&&parent.document.getElementById("accionAnterior").value!=""){
			formu.accionAnterior.value=parent.document.getElementById("accionAnterior").value;
			formu.idModulo.value=parent.document.getElementById("idModulo").value;
			formu.modo.value="inicio";
		}else{
			formu.modo.value="abrir";
		}
		formu.target='mainWorkArea';
		formu.submit();				
	}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

		<html:form action="/CON_RecuperarConsultasDinamicas.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior" value = "ejecucion"/>
			<html:hidden property = "idModulo"/>
		</html:form>
</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
