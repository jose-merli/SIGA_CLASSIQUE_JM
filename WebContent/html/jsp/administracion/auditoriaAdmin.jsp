<!DOCTYPE html>
<html>
<head>
<!-- auditoriaAdmin.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.CenVisibilidad"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties" %>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script language="JavaScript">
	
		function buscar() 
		{
			sub();		
			document.getElementById("modo").value="buscar";						
			auditoriaAdminForm.submit();
					
		}
		
		function borrarLog() 
		{
			if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
				document.getElementById("modo").value="borrar";	
				auditoriaAdminForm.submit();
				}
			return false;
		}	

	</script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="administracion.auditoria.configuracion.titulo" localizacion="menu.auditoriaUsuarios.localizacion"/>
	
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAlto('resultado');">
	
	
		<fieldset>
		<table class="tablaCentralCampos" align="center">
			<html:form action="/ADM_AuditoriaAdmin.do" method="POST" target="resultado">
			<input type="hidden" name="modo" value="inicio">
			<tr>				
				<td class="labelText">
					<siga:Idioma key="administracion.auditoria.dirip"/>
				</td>				
				<td>
					<html:text name="auditoriaAdminForm" property="direccionIP" size="15" maxlength="15" styleClass="box"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoria.usuario"/>
				</td>
				<td>
					<html:text name="auditoriaAdminForm" property="usuario" size="40" styleClass="box"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="administracion.auditoria.accion"/>
				</td>
				<td>
					<html:text name="auditoriaAdminForm" property="accion" size="40" styleClass="box"></html:text>
				</td>
			</tr>
		</html:form>
		</table>
		</fieldset>
	
		<!-- BOTONES BUSQUEDA -->
		<table class="tablaTitulo" align="center">
			<tr> 
				<td class="titulitos">
					&nbsp;				
				</td>
				<td class="tdBotones">
				<%-- S�lo muestra el bot�n de borrar si la instituci�n est� en el nivel m�s alto--%>
					<%if (CenVisibilidad.getNivelInstitucion(user.getLocation()).equals("1")){%>
					<html:button property="deleteButton" onclick="return borrarLog();" styleClass="button">
						<siga:Idioma key="administracion.auditoria.borrarlog"/>
					</html:button>
				    <%};%> 
					
				</td>
				<td class="tdBotones">
					<html:button property="idButton" onclick="return buscar();" styleClass="button">
						<siga:Idioma key="general.search"/>
					</html:button>
				</td>
				</tr>	   
		</table>

		<% // Se registra la funcion para admitir la tecla enter en formularios. Se hace asi pq no se usa el tag botonesBusqueda%>
		<script>
			registrarEnterFormularios ();
		</script>
	
		<!-- IFRAME LISTA RESULTADOS -->
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0"					 
					class="frameGeneral">
	</iframe>
				
<!-- SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
</body>
</html>