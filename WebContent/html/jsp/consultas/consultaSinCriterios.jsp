<!DOCTYPE html>
<html>
<head>
<!-- consultaSinCriterios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.ConCampoConsultaBean"%>
<%@ page import="com.siga.beans.ConCriteriosDinamicosBean"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

%>

    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	</head>

	<body onload="ajusteAlto('resultado');accionAceptar();" style='height:100%'>

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action = "${path}" method="POST" target="submitArea">			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>		
			<html:hidden property = "actionModal" value = ""/>

	<div id='frameResultado'>
		<iframe name="resultado" id="resultado" src="<%=app%>/html/jsp/general/blank.jsp" style="width:100%; border:0" frameborder="0"></iframe>
	</div>
	
	<script language="JavaScript">
	
	<!-- Asociada al boton Aceptar -->
		function accionAceptar() 
		{			
			document.forms[0].target = "resultado";	
			document.forms[0].modo.value = "ejecutarConsulta";
			document.forms[0].submit();
			
		}	

		function accionDownload() 
		{
			sub();
			document.forms[0].modo.value = "download";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
			fin();
		}
		
		function accionImprimir() 
		{			
			
			window.print();
		}
		
		function accionVolver() 
		{		
			var formu=document.RecuperarConsultasForm;
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

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	</html:form>	
	
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.volver"/>' name='idButton' id="idButton" onclick="return accionVolver();" class="button" value='<siga:Idioma key="general.boton.volver"/>'>
			</td>
			<td  style="width:900px;">
			&nbsp;
			</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.imprimir"/>' name='idButton' id="idButtonImprimir" onclick="return accionImprimir();" class="button" value='<siga:Idioma key="general.boton.imprimir"/>'>
			</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.download"/>' name='idButton' id="idButtonDescargar" onclick="return accionDownload();" class="button" value='<siga:Idioma key="general.boton.download"/>'>
			</td>
		</tr>
	</table>	
		
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
		
	</body>
</html>