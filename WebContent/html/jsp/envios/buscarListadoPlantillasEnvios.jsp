<!DOCTYPE html>
<html>
<head>
<!-- buscarListadoPlantillasEnvios.jsp -->
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
	String sDescripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String sIdTipoEnvios = (String)request.getAttribute("idTipoEnvios");
	
	if (sDescripcionPlantilla==null)
	{
		sDescripcionPlantilla="";
	}
	
	if (sIdTipoEnvios==null)
	{
		sIdTipoEnvios="";
	}
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/ENV_DefinirPlantillas.do" method="POST" target="mainWorkArea">
			<html:hidden styleId = "modo"  property = "modo" value = ""/>
			<html:hidden styleId = "hiddenFrame"  property = "hiddenFrame"  value="1"/>
			<html:hidden styleId = "descripcionPlantilla"  property = "descripcionPlantilla"  value="<%=sDescripcionPlantilla%>"/>
			<html:hidden styleId = "idTipoEnvios"  property = "idTipoEnvios"  value="<%=sIdTipoEnvios%>"/>
			<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>

		<siga:Table 
	   	      name="tablaDatos"
	   		  border="1"
	   		  columnNames="envios.plantillas.literal.plantilla,envios.plantillas.literal.tipoenvio,"
	   		  columnSizes="60,25,15">

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
				  		Row htDatos = (Row)vDatos.elementAt(i);
				  		
						String botones = "C";
						
						if(htDatos.getString("FECHABAJA") == null || htDatos.getString("FECHABAJA").equals("")){
							botones += ",E,B";
						}				  		
				  		
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=htDatos.getString(EnvPlantillasEnviosBean.C_IDINSTITUCION)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=htDatos.getString(EnvTipoEnviosBean.C_IDTIPOENVIOS)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=htDatos.getString(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS)%>">
						
						<%=htDatos.getString("NOMBREPLANTILLA")%>
					</td>
					<td>
						<%=htDatos.getString("NOMBRETIPO")%>
					</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>

	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>