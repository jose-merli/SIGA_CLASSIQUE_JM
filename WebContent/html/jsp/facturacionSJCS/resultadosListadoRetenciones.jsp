<!-- resultadosListadoRetenciones.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


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
	<title><siga:Idioma key="FactSJCS.listadoRetencionesJ.cabecera"/></title>
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
		   columnNames="FactSJCS.listadoRetencionesJ.literal.destinatario,FactSJCS.listadoRetencionesJ.literal.nColegiado,FactSJCS.listadoRetencionesJ.literal.nombreColegiado,FactSJCS.listadoRetencionesJ.literal.pago,FactSJCS.listadoRetencionesJ.literal.importeRetenido,FactSJCS.listadoRetencionesJ.literal.fechaRetencion"
		   columnSizes="25,10,25,20,10,10">
		  <%if (obj.size()>0){%>
  			<%
	    	int recordNumber=0;
			while (recordNumber < obj.size())
			{			
				fila = (Hashtable)obj.get(recordNumber);
			%>				
				<tr  class="listaNonEdit">
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NOMBREDESTINATARIO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NCOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NOMBRECOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NOMBREPAGO"))%></td>
					<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesHash.getString(fila, "IMPORTERETENIDO")))%></td>
					<td><%=GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "FECHARETENCION")))%></td>
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
	