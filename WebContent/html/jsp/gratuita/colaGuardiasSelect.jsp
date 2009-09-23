<!-- ColaGuardiasSelect.jsp -->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	


<html>
<!-- HEAD -->
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<siga:Titulo titulo="pestana.justiciagratuitaturno.colaGuardia" localizacion="gratuita.turnos.localizacion.colaTurno"/>
		<script>
		<!-- Asociada al boton Volver -->
		function accionVolver() {	
			with(document.forms[1]){	
		   <% String entrada = (String)ses.getAttribute("entrada");
			  if (entrada.equalsIgnoreCase("1")){%>
				action="JGR_DefinirTurnos.do";
				target="mainWorkArea";
				modo.value="abrirAvanzada";
				submit();
			<%}else{%>
				action="JGR_DefinirTurnosLetrado.do";
				target="mainPestanas";
				modo.value="abrir";
				submit();
			<%}%>
			}
		}

		<!-- Asociada al boton Imprimir -->
		function accionImprimir() {	
			sub();
			var f=document.forms[0];
			f.action="JGR_ColaGuardias.do";
			f.target="submitArea";
			f.modo.value="imprimir";
			//Se envia la descripcion de la guardia para que la muestre el informe PDF
			f.defGuardia.value=f.idGuardia.options[f.idGuardia.selectedIndex].text;
			f.submit();
			// con pantalla de espera
			//submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.wait';
		}
		
		function cargarGuardia() {		
			with(document.forms[0]){	
				if(idGuardia.value!=""){
					target="areaDatos";
					modo.value="ver";
					submit();
				}
			}
		}
		
		</script>

	</head>

<%		
Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
String[] parametros = new String[3];
parametros[0] = usrbean.getLocation();					// institucion
parametros[1] = (String)turnoElegido.get("IDTURNO");	// turno
%>

<body class="tablaCentralCampos" onload="ajusteAltoBotones('areaDatos');cargarGuardia();">

	 <html:form action="/JGR_ColaGuardias" method="get" >
	 	<fieldset>
		<table border="0" style="table-layout:fixed;" cellpadding="2" cellspacing="2" width="100%" align="center">
		  	<tr>
			  <td class="labelText" width="200">
			  	<siga:Idioma key="gratuita.turnos.literal.seleccionarGuardia"/>: 
			  </td>
			  <td>
				<siga:ComboBD nombre="idGuardia" tipo="guardias" obligatorioSinTextoSeleccionar="true" parametro="<%=parametros%>" clase="boxCombo" accion="cargarGuardia();"/>
			  </td>
			</tr>
		  </table>
	 	</fieldset>
	 	
		<html:hidden name="ColaGuardiasForm" property="modo"/>
		<html:hidden name="ColaGuardiasForm" property="defGuardia"/>
		<html:hidden name="ColaGuardiasForm" property="idPersona" value=""/>
	</html:form>
	
	<iframe name="areaDatos" src="<%=app%>/html/jsp/general/blank.jsp"
		width="100%" height="360px" 
		scrolling="no"
		frameborder="no">
	</iframe>

	<siga:ConjBotonesAccion botones="V,I"   />

	<form name="auxForm" action="aux" target="aux" method="post">
		<input type=hidden name="modo">
	</form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>

</html>