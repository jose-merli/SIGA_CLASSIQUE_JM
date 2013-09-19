<!DOCTYPE html>
<html>
<head>
<!-- listadoSubzonas.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-bean.tld"	prefix="bean"%>
<%@taglib uri = "struts-html.tld" 	prefix="html"%>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@taglib uri = "struts-logic.tld" 	prefix="logic"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("accion");
	ScsSubzonaBean fila = new ScsSubzonaBean();
	
	String botones="";
	
	if (accion.equalsIgnoreCase("ver")){
		botones = "C";
	} else {
		botones = "C,E,B";
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<title><siga:Idioma key="gratuita.listadoSubzonas.literal.listadoSubzonas"/></title>
	
	<script type="text/javascript">
		function refrescarLocal() {
			parent.buscar();
		}
	</script>
</head>

<body>	
	<html:form action="/JGR_DefinirZonasSubzonas.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="accion" value="subzona">
	</html:form>	
		
	<siga:Table 		   
	   name="listadoSubzonas"
	   border="1"
	   columnNames="gratuita.busquedaZonas.literal.subzona,"
	   columnSizes="90,10"
	   modal="P">

<%
		if (obj.size()>0) {
		   	int recordNumber=1;
			while (recordNumber-1 < obj.size()) {			
				fila = (ScsSubzonaBean)obj.get(recordNumber-1);
%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdZona()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getIdSubzona()%>">
						
						<%=fila.getNombre()%>&nbsp;
					</td>
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