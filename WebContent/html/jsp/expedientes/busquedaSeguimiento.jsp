
<!-- busquedaSeguimiento.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.expedientes.form.ExpSeguimientoForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	ExpSeguimientoForm form = (ExpSeguimientoForm)request.getAttribute("ExpSeguimientoForm");
	
	String[] parametro = new String[2];
	parametro[0] = form.getIdInstitucionTipoExpediente();
	parametro[1] = form.getIdTipoExpediente();
	
	String botones2 = form.getEditable().booleanValue()? "V,N" : "V";
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo titulo="expedientes.auditoria.seguimiento.cabecera" localizacion="expedientes.auditoria.localizacion" />
		<!-- FIN: TITULO Y LOCALIZACION -->
	</head>

	<body onLoad="ajusteAlto('resultado');buscar();">
		<html:form action="/EXP_Auditoria_Seguimiento.do?noReset=true" method="POST" target="submitArea">
			<html:hidden property="modo" value="" />
			<html:hidden property="hiddenFrame" value="1" />
			<html:hidden property="idInstitucion" />
			<html:hidden property="idInstitucionTipoExpediente" />
			<html:hidden property="idTipoExpediente" />
			<html:hidden property="numeroExpediente" />
			<html:hidden property="anioExpediente" />
			<html:hidden property="editable" />
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<table class="tablaTitulo" align="center" cellspacing="0">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<% ExpSeguimientoForm f = (ExpSeguimientoForm)request.getAttribute("ExpSeguimientoForm"); %>
						<%=f.getTituloVentana()%>
					</td>
				</tr>
			</table>
			<fieldset>
				<table class="tablaCampos" align="center">
					<!-- FILA -->
					<tr>
						<td class="labelText" width="100">
							<siga:Idioma key="expedientes.auditoria.literal.fase" />
						</td>
						<td>
							<siga:ComboBD nombre="comboFases" tipo="cmbFases" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" accion="Hijo:comboEstados" hijo="t" pestana="t" />
						</td>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.estado" />
						</td>
						<td>
							<siga:ComboBD nombre="comboEstados" tipo="cmbEstados" clase="boxCombo" obligatorio="true" hijo="t" pestana="t" />
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.tipoanotacion" />
						</td>
						<td>
							<siga:ComboBD nombre="idTipoAnotacion" tipo="cmbTipoAnotacion" clase="boxCombo" parametro="<%=parametro%>" obligatorio="false" />
						</td>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.usuario" />
						</td>
						<td>
							<siga:ComboBD nombre="idUsuario" tipo="cmbUsuariosAnotacion" clase="boxCombo" parametro="<%=parametro%>" obligatorio="false" />
						</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma key="expedientes.auditoria.literal.fechainicial" />
						</td>
						<td>
							<siga:Fecha nombreCampo="fechaDesde" valorInicial="<%=form.getFechaDesde()%>" />
						</td>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.fechafinal" />
						</td>
						<td>
							<siga:Fecha nombreCampo="fechaHasta" valorInicial="<%=form.getFechaHasta()%>" />
						</td>
					</tr>
				</table>
			</fieldset>
		</html:form>

		<siga:ConjBotonesBusqueda botones="B" />

		<iframe align="middle" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0"; class="frameGeneral">
		</iframe>
		
		<!-- FIN: LISTA DE VALORES -->
		
		<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="<%=botones2%>" clase="botonesDetalle" />
		<!-- FIN: BOTONES REGISTRO -->

		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value="" />
			<html:hidden property="avanzada" value="" />
		</html:form>

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			function refrescarLocal() {	
				document.location.reload();
			}

			<!-- Asociada al boton Volver -->
			function accionVolver() {		
				<% if (busquedaVolver.equals("AB")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% } else if (busquedaVolver.equals("NB")){ %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } else if(busquedaVolver.equals("AV")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% }  else if (busquedaVolver.equals("Al")){%>
					document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } %>
				document.forms[1].submit();	
			}
			
			function buscar() {
				sub();	
				document.ExpSeguimientoForm.modo.value="buscar";
				document.ExpSeguimientoForm.target="resultado";	
				document.ExpSeguimientoForm.submit();
			}

			<!-- Asociada al boton Nuevo -->
			function accionNuevo() {		
				document.forms[0].modo.value = "nuevo";
				var resultado=ventaModalGeneral(document.forms[0].name,"M");
				if(resultado=='MODIFICADO'){
					refrescarLocal();
				}
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none">
		</iframe>
	</body>
</html>
