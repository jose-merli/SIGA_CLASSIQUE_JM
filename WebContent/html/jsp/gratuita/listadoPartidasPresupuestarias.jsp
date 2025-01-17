<!DOCTYPE html>
<html>
<head>
<!-- listadoPartidasPresupuestarias.jsp -->

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
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) ses.getAttribute("resultado");
%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<title><siga:Idioma key="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria"/></title>
		
	<script language="JavaScript">	
		function refrescarLocal() {
			parent.buscar();
		}		
		
		function refrescarLocalArray(datos) {
		}	
	</script>
</head>

<body>
	<html:form action="/PartidaPresupuestariaAction.do" method="post" target="submitArea"	 style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
	</html:form>	
	
	<siga:Table 		   
	   name="listadoPartidas"
	   border="2"
	   columnNames="gratuita.partidaPresupuestaria.literal.nombre,gratuita.partidaPresupuestaria.literal.descripcion,factSJCS.datosFacturacion.literal.importePartida,"
	   columnSizes="28,45,15,12"
	   modal="P">
		   
<%
		if (obj.size()>0) {
	    	int recordNumber=1;
	    	Hashtable registro = new Hashtable();
			while (recordNumber-1 < obj.size()) {
				ScsPartidaPresupuestariaBean fila = (ScsPartidaPresupuestariaBean)obj.get(recordNumber-1);
				registro = fila.getOriginalHash();
%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdPartidaPresupuestaria()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getFechaMod()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getUsuMod()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=fila.getIdInstitucion()%>">
						
						<%=registro.get("NOMBREPARTIDA")%>
					</td>
					<td><%=registro.get("DESCRIPCION")%>&nbsp;</td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(registro.get("IMPORTEPARTIDA").toString())%>&nbsp;&euro;</td>
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

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>	
</html>