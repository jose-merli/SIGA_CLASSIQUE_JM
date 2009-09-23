<!-- auditoriaUsuariosDatos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import = "java.util.*"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.siga.administracion.*"%>
<%@ page import = "com.siga.beans.CenHistoricoBean"%>

<% 
	String app = request.getContextPath();
	Properties src = (Properties)request.getSession().getAttribute(SIGAConstants.STYLESHEET_REF);
	
	Hashtable h = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	String botones = "C", estilo = "boxConsulta";
	String modo = (String)request.getAttribute("MODO_MOSTRAR");
	if (modo.equalsIgnoreCase("editar")) {
		botones = "Y," + botones;
		estilo = "box";
	}
	
%>	

	<!-- Validaciones en Cliente -->
	<html:javascript formName="auditoriaUsuariosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
<html>

	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">

			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				if (!validateAuditoriaUsuariosForm(document.auditoriaUsuariosForm))
					fin();
					return;
				
				auditoriaUsuariosForm.modo.value = "modificar";
				auditoriaUsuariosForm.submit();
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.close();
			}

		</script>
	</head>

	<body>
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="administracion.auditoriaUsuarios.datos.titulo"/>
				</td>
			</tr>
		</table>
		
		<html:form action="/ADM_AuditoriaUsuarios.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = " "/>

			<table class="tablaCentralCamposMedia" align="center" border="0" >
			<tr>
				<td class="labelText" width="">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.usuMod"/>
				</td>
				<td class="labelTextValue">
					<% if (UtilidadesHash.getString(h, CenHistoricoBean.C_USUMODIFICACION).equalsIgnoreCase(Integer.toString(ClsConstants.USUMODIFICACION_AUTOMATICO))) { %>
						<siga:Idioma key="censo.tipoApunteHistorico.automatico"/>
					<% } else { %>
						<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(h, "NOMBRE_USUARIO"))%>						
					<% } %>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.tipoAccion"/>
				</td>
				<td class="labelTextValue">
					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(h, "DESCRIPCION_TIPO_ACCESO"))%>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaEntrada"/>
					<% if (estilo.equals("box")) {%>
						&nbsp;(*)
					<% } %>
				</td>
				<td class="labelTextValue">
					<% String fechaEntrada = GstDate.getFormatedDateShort("", UtilidadesHash.getString(h, CenHistoricoBean.C_FECHAENTRADA)); %>
					<html:text styleClass="<%=estilo%>" property="fechaEntrada" size="8" maxlength="10" readonly="true" value="<%=fechaEntrada%>" />
					<% if (estilo.equals("box")) {%>
						<a href='javascript://'onClick="return showCalendarGeneral(fechaEntrada);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
					<% } %>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaEfectiva"/>
					<% if (estilo.equals("box")) {%>
						&nbsp;(*)
					<% } %>
				</td>
				<td class="labelTextValue">
					<% String fechaEfectiva = GstDate.getFormatedDateShort("", UtilidadesHash.getString(h, CenHistoricoBean.C_FECHAEFECTIVA)); %>
					<html:text styleClass="<%=estilo%>" property="fechaEfectiva" size="8" maxlength="10" readonly="true" value="<%=fechaEfectiva%>" />
					<% if (estilo.equals("box")) {%>
						<a href='javascript://'onClick="return showCalendarGeneral(fechaEfectiva);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
					<% } %>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.motivo"/>
					<% if (estilo.equals("box")) {%>
						&nbsp;(*)
					<% } %>
				</td>
				<td class="labelTextValue">
					<% String motivo = UtilidadesHash.getString(h, CenHistoricoBean.C_MOTIVO); %>
					<html:textarea property="motivo" name="auditoriaUsuariosForm" cols="50" rows="3" styleClass="<%=estilo%>" style="width:500px" value="<%=motivo%>" ></html:textarea>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoriaUsuarios.literal.descripcion"/>
				</td>
				<td class="labelTextValue">
					<textArea cols="10" rows="8" class="boxConsulta" readonly="" style="width:500px"><%=UtilidadesHash.getString(h, CenHistoricoBean.C_DESCRIPCION)%></textarea>
				</td>
			</tr>
				
			</table>
		</html:form>



		<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
		<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>