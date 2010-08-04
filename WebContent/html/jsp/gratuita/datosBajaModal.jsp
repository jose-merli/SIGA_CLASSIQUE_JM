<!-- busquedaClientesModal.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.atos.utils.GstDate"%>
<%@page import="com.atos.utils.UsrBean"%>
<%@page import="com.siga.gratuita.form.BajaTurnosForm"%>
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<!-- JSP -->

<%  
	String app = request.getContextPath();
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
  	BajaTurnosForm form = (BajaTurnosForm)request.getAttribute("BajaTurnosForm");
  	
  	String fechaBaja = form.getFechaBaja()==null?"":GstDate.getFormatedDateShort(usr.getLanguage(),form.getFechaBaja());
  	String fechaSolBaja =  form.getFechaSolicitudBaja()==null?"":GstDate.getFormatedDateShort(usr.getLanguage(),form.getFechaSolicitudBaja());
  	String motivo = form.getObservacionesBaja()==null?"":form.getObservacionesBaja();
%>

<%@page import="com.siga.gratuita.form.PersonaJGForm"%><html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script>
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 		
			window.close();
		}	
	</script>
		
</head>

<body>
		<!-- TITULO -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.datosBajaTurno.titulo"/>
				</td>
			</tr>
		</table>
		<table>
		<br> <!-- Para espaciar -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="censo.datosBajaTurno.fechaSolicitud"/>
			</td>
			<td class="boxConsulta">
				<%=fechaSolBaja%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="censo.datosBajaTurno.fechaValidacion"/>
			</td>
			<td class="boxConsulta">
				<%=fechaBaja%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="censo.datosBajaTurno.motivoBaja"/>
			</td>
			<td>
				<textarea class="boxConsulta" style="scroll:auto" cols="45" rows="10" readonly="true"><%=motivo %></textarea>
			</td>
		</tr>
		</table>
	<siga:ConjBotonesAccion botones="C"/>
	
</body>

</html>
