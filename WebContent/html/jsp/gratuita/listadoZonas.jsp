<!DOCTYPE html>
<html>
<head>
<!-- listadoZonas.jsp -->
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
<%@ page import="com.siga.beans.ScsZonaBean"%>
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ScsZonaBean fila = new ScsZonaBean();
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.listadoZonas.literal.listadoZonas"/></title>
</head>

<body>
	<%if (obj.size()>0){%>
	<html:form action="/JGR_DefinirZonasSubzonas.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		<html:hidden property = "accion"  styleId = "accion" value = "zona"/>
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
	</html:form>	
		
		<siga:Table 		   
		   name="listadoZonas"
		   border="2"
		   columnNames="gratuita.busquedaZonas.literal.zona,"
		   columnSizes="90,10">

  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size()) {
				fila = (ScsZonaBean)obj.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdInstitucion()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdZona()%>"><%=fila.getNombre()%>&nbsp;</td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}
			%>
		</siga:Table>
	<%
	}else {
	%>
	<siga:Table 		   
		   name="listadoZonas"
		   border="2"
		   columnNames="gratuita.busquedaZonas.literal.zona,"
		   columnSizes="90,10">
  	    </siga:Table>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<%
	}
	%>
	
		<script language="JavaScript">

		//Funcion asociada al refresco
		function refrescarLocal() 
		{		
			parent.buscar();
		}		
		
		</script>
	
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>	
</html>