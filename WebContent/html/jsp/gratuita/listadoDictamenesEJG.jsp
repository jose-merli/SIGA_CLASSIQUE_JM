<!DOCTYPE html>
<html>
<head>
 <!-- listadoDictamenesEJG.jsp -->
 
<!-- CABECERA JSP -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.beans.ScsAreaAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultado");
	Hashtable fila = new Hashtable();
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script type="text/javascript">
		function refrescarLocal() {
			parent.buscar();
		}
	</script>	
</head>

<body>
	<html:form action="/JGR_MantenimientoDictamenesEJG.do" method="POST" target="mainWorkArea">
		<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		<input type="hidden" id="actionModal"  name="actionModal" value="">
	</html:form>
	
	<siga:Table 		   
	   name="listadoAreas"
	   border="2"
	   columnNames="general.codeext,
	   				censo.fichaCliente.literal.abreviatura,
	   				general.description,"
	   columnSizes="15,35,40,10">
<%
		if (obj.size()>0) {
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size()) {
				fila = (Hashtable)obj.get(recordNumber-1);
%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDDICTAMEN")%>"><%=fila.get("CODIGOEXT")%>&nbsp;</td>
					<td><%=fila.get("ABREVIATURA")%>&nbsp;</td>
					<td><%=fila.get("DESCRIPCION")%>&nbsp;</td>
					
				</siga:FilaConIconos>		
<%  
				recordNumber++; 
			}
		} else {
%>
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	 
<%
		}
%>
	</siga:Table>
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->		
</body>	
</html>