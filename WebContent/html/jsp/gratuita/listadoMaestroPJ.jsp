<!DOCTYPE html>
<html>
<head>
<!-- listadoMaestroPJ.jsp-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>

	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		//Refresco del iframe 	
	 	function refrescarLocal() {
			parent.buscar()
		}
	</script> 	
</head>

<body>
	<html:form action="/JGR_MantenimientoMaestroPJ.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		<input type="hidden" name="actionModal"  id="actionModal" value="">
	</html:form>	
	
	<siga:Table 		   
	   name="listadoPJ"
	   border="2"
	   columnNames="gratuita.busquedaMaestroPJ.literal.partido,"
	   columnSizes="85,15">	
<% 
		if ((obj!= null) && (obj.size()>0)) { 
			int recordNumber=1;
			while ((recordNumber) <= obj.size()) {	 	
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
%>
				<!-- Campos ocultos por cada fila:      
					1- IDPARTIDO
					2- PARTIDOJUDICIAL
					3- FECHAMODIFICACION
					4- USUMODIFICACION
					5- IDPROVINCIA
					6- PROVINCIA
				-->
				<!-- Campos visibles por cada fila:
					1- PROVINCIA
					2- PARTIDOJUDICIAL
				-->		
				
	       		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDPARTIDO")%>'>
						<%=hash.get(CenPartidoJudicialBean.C_NOMBRE)%>
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
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>	
</html>