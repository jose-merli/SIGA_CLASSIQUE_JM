<!DOCTYPE html>
<html>
<head>
<!-- listadoRetenciones.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.beans.ScsRetencionesAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- TAGLIBS -->
<%@taglib uri="struts-html.tld" 	prefix="html"%>
<%@taglib uri="libreria_SIGA.tld" 	prefix="siga"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	ScsRetencionesAdm retencionesAdm = new ScsRetencionesAdm(usr);
	Hashtable sociedades = (Hashtable)request.getAttribute("sociedades");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<title><siga:Idioma key="gratuita.retenciones.listadoRetenciones"/></title>

	<script language="JavaScript">	
		function refrescarLocal() {
			parent.buscar();
		}		
	</script>
</head>

<body>
		
	<html:form action="/SolicitudRetencioAction.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" id="modo" value="">
		<input type="hidden" name="hiddenframe" id="hiddenframe" value="1">
	</html:form>	
	
	<siga:Table 		   
	   	name="listadoRetenciones"
	   	border="2"
	   	columnNames="gratuita.retenciones.descripcion,gratuita.retenciones.retencion,gratuita.retenciones.tipoSociedad,"
	   	columnSizes="60,8,20,12"
	   	modal="P" >
				
<%
		if (obj.size()>0) {   
			int recordNumber=1;
			while (recordNumber-1 < obj.size()) {			
				Hashtable registro = (Hashtable)obj.get(recordNumber-1);
				ScsRetencionesBean fila = (ScsRetencionesBean)retencionesAdm.hashTableToBean(registro);
				String nomSociedad=sociedades.get(fila.getLetraNifSociedad()) == null?"":(String)sociedades.get(fila.getLetraNifSociedad());
%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdRetencion()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getUsuMod()%>">										   
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getFechaMod()%>">
						
						<%=fila.getDescripcion()%>
					</td>
					<td align="center"><%=fila.getRetencion()%></td>							
					<td align="left"><%=nomSociedad%>&nbsp;</td>
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
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->		
</body>	
</html>	