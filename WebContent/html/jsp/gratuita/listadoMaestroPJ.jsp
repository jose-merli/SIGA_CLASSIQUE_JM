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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<script>
		//Refresco del iframe 	
	 	function refrescarLocal()
		{
			parent.buscar()
		}
	</script>
 	
</head>

<body>

	<html:form action="/JGR_MantenimientoMaestroPJ.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		<input type="hidden" name="actionModal"  id="actionModal" value="">
	</html:form>	
		
	<!-- INICIO: RESULTADO -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
		<siga:TablaCabecerasFijas 		   
			   nombre="listadoPJ"
			   borde="2"
			   clase="tableTitle"		   
			   nombreCol="gratuita.busquedaMaestroPJ.literal.partido,"
			   tamanoCol="90,10"
			   alto="100%" >
			   
		<%
				int recordNumber=1;
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
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
				<% 		recordNumber++; %>
				<% } %>
			</siga:TablaCabecerasFijas>
		<!-- FIN: RESULTADO -->
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } %>

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>