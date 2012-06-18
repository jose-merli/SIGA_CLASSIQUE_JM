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
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
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
		function accionCalendario() 
		{
			// Abrimos el calendario 
			document.ColaGuardiasForm.target="areaDatos";
			var resultado = showModalDialog("<html:rewrite page='/html/jsp/general/calendarGeneral.jsp'/>?valor="+ document.ColaGuardiasForm.fechaConsulta.value, document.ColaGuardiasForm.fechaConsulta,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");
			window.top.focus();
			if (resultado) {				 
				 document.ColaGuardiasForm.fechaConsulta.value = resultado;
				 document.getElementById('fechaConsulta').value = resultado;
				 document.ColaGuardiasForm.modo.value = 'ver';				 
				 document.ColaGuardiasForm.submit();				
		 	} else {
					if(document.ColaGuardiasForm.fechaConsulta.value==''){
						document.getElementById('fechaConsulta').value = '';
						document.ColaGuardiasForm.fechaConsulta.value = '';
						document.ColaGuardiasForm.modo.value = 'ver';
						document.ColaGuardiasForm.submit();
					}
			} 
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

<body class="tablaCentralCampos" onload="ajusteAltoBotones('areaDatos');mostrarFechaActual();cargarGuardia();">

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
			
					<td class="labelText"><siga:Idioma key="gratuita.gestionInscripciones.fechaConsulta"/></td>
					<td >
					<html:text id="fechaConsulta" name="ColaGuardiasForm" property="fechaConsulta" size="10" maxlength="10" styleClass="box" ></html:text>
					&nbsp;&nbsp;<a
						id="calendarioTd" 
						onClick="accionCalendario();"
						onMouseOut="MM_swapImgRestore();"
						onMouseOver="MM_swapImage('Calendario','','<html:rewrite page='/html/imagenes/calendar.gif'/>',1);"><img
						src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
						alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
						border="0"></a>
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