<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.*"%>


<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) request.getAttribute("resultado");		
	Hashtable fila = new Hashtable();
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="factSJCS.historicoPrecios.cabecera"/></title>
	<script type="text/javascript">
		function refrescarLocal(){
			parent.buscar();
		}		
	</script>
</head>

<body>
	<siga:Table 		   
		   name="listadoRetenciones"
		   border="2"
		   columnNames="factSJCS.historicoPrecios.literal.turno,factSJCS.historicoPrecios.literal.guardia,factSJCS.historicoPrecios.literal.hitoFacturable,factSJCS.historicoPrecios.literal.importe"
		   columnSizes="25,25,25,25">
		  <%if (obj.size()>0){%>
  			<%
	    	int recordNumber=0;
			while (recordNumber < obj.size())
			{			
				fila = (Hashtable)obj.get(recordNumber);
			%>				
				<tr>
					<td class="listaNonEdit"><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "TURNO"))%></td>
					<td class="listaNonEdit"><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "GUARDIA"))%></td>
					<td class="listaNonEdit"><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "HITO"))%></td>
					<td class="listaNonEdit"><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "IMPORTE"))%>&nbsp;&nbsp;&euro;</td>
				</tr>
			<% recordNumber++;} %>
		<%}else {%>
			<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
		<%}%>
		</siga:Table>	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>	
</html>
	