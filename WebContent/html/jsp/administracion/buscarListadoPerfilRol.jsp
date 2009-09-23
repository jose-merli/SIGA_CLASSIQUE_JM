<!-- buscarListadoPerfilRol.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	request.removeAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/ADM_GestionarPerfilRol.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "modal" value = "true"/>
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="administracion.perfilRol.literal.Rol,administracion.perfilRol.literal.grupoPorDefecto,"
		   		  tamanoCol="45,45,10"
		   		  alto="100%"
		   		  modal="P"
		   		  activarFilaSel="true">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Hashtable htDatos = (Hashtable)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="E" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value=" <%=htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value=" <%=htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value=" <%=htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value=" <%=htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value=" <%=htDatos.get(AdmPerfilRolBean.T_NOMBRETABLA + "_" + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO)%>">
					<%=htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION)%>
					</td>
					<td>&nbsp;<%=htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION)%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>

		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>