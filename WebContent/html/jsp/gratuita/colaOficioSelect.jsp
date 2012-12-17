<!-- ColaGuardiasSelect.jsp -->
	 
 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	


<!-- HEAD -->
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
		<siga:Titulo titulo="pestana.justiciagratuitaturno.colaOficio" localizacion="gratuita.turnos.localizacion.colaTurno.manteniento"/>
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
			f.action="JGR_ColaOficio.do";
			f.target="submitArea";
			f.modo.value="imprimir";
			//Se envia la descripcion del turno para que la muestre el informe PDF
			//f.defGuardia.value=f.idGuardia.options[f.idGuardia.selectedIndex].text;
			f.submit();
			// con pantalla de espera
			//submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.wait';
		}

		function cargarOficio() {		
			with(document.forms[0]){	
					target="areaDatos";
					modo.value="ver";
					submit();
			}
		}
		
		function postAccionCalendario() 
		{
			document.ColaOficiosForm.target="areaDatos";
			window.top.focus();
			document.ColaOficiosForm.modo.value = 'ver';
			document.ColaOficiosForm.submit();
		}
		function mostrarFechaActual() {		
			fechaActual = getFechaActualDDMMYYYY();
			document.getElementById("fechaConsulta").value = fechaActual;		
		}
				
		</script>

	</head>

<%		
Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
String[] parametros = new String[3];
parametros[0] = usrbean.getLocation();					// institucion
parametros[1] = (String)turnoElegido.get("IDTURNO");	// turno
%>

<body class="tablaCentralCampos" onload="ajusteAltoBotones('areaDatos');mostrarFechaActual();cargarOficio();">

	 <html:form action="/JGR_ColaOficio" method="get" >
	 	<fieldset>
		<table border="0" style="table-layout:fixed;" cellpadding="2" cellspacing="2" width="100%" align="center">
		  	<tr>			
			  <td class="labelText"><siga:Idioma key="gratuita.gestionInscripciones.fechaConsulta"/></td>
					<td >
					<siga:Fecha nombreCampo="fechaConsulta" postFunction="postAccionCalendario();"></siga:Fecha>

				</tr>
			
			
		  </table>
	 	</fieldset>
	 	
		<html:hidden name="ColaOficiosForm" property="modo"/>
		<html:hidden name="ColaOficiosForm" property="idPersona" value=""/>
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