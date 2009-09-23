<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="factSJCS.historicoPrecios.cabecera"/></title>
	<script type="text/javascript">
		function refrescarLocal(){
			parent.buscar();
		}		
	</script>
</head>

<body>
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoRetenciones"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="factSJCS.historicoPrecios.literal.turno,factSJCS.historicoPrecios.literal.guardia,factSJCS.historicoPrecios.literal.hitoFacturable,factSJCS.historicoPrecios.literal.importe"
		   tamanoCol="25,25,25,25"
		   alto="100%"
		  >
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
			<tr>
			<td colspan="4">
			<br>
			<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br>
			</td>
			</tr>
		<%}%>
		</siga:TablaCabecerasFijas>	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>	
</html>
	