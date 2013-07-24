<!DOCTYPE html>
<html>
<head>
<!-- buscarListadoUsuarios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	request.removeAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script>
			function refrescarLocal() {
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="submitArea">
			<html:hidden property = "modo" styleId = "modo"  value = ""/>
		</html:form>	
		
		
			<siga:Table 
					name="tablaDatos"
					border="1"  
					modal="M" 
		   		  	columnNames="administracion.usuarios.literal.nombre,administracion.usuarios.literal.NIF,administracion.usuarios.literal.fechaAlta,administracion.usuarios.literal.activo,administracion.certificados.literal.roles,&nbsp;"
		   		  	columnSizes="30,15,8,7,30,10">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		AdmUsuariosBean bean = (AdmUsuariosBean)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdUsuario()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getDescripcion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdLenguaje()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getNIF()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getActivo()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value=" <%=bean.getGrupos()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=bean.getFechaAlta()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_9" value="<%=bean.getUsuMod()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=bean.getFechaMod()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=bean.getCodigoExt()%>">
						
						<%=bean.getDescripcion()%>
					</td>
					<td><%=bean.getNIF()%></td>
					<td><%=GstDate.getFormatedDateShort(userBean.getLanguage(), bean.getFechaAlta())%></td>
					<td><%if (bean.getActivo().equals("S")) {%><siga:Idioma key="general.boton.yes"/><%} else {%><siga:Idioma key="general.boton.no"/><%}%></td>
					<td>&nbsp;<%=bean.getGrupos()%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>

	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>