<!-- listadoLG.jsp-->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>

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

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	<script>
	 	//Refresco del iframe 	
	 	function refrescarLocal()
		{
			parent.buscar()
		}
 	</script>
 	
</head>

<body>

	<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" styleId = "modo" value = ""/>
		<html:hidden property = "accion" styleId = "accion" value = "buscarPor"/>
			<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>	
		
	<!-- INICIO: RESULTADO -->
		<siga:Table 		   
			   name="listadoLG"
			   border="1"
			   columnNames="gratuita.listadoLG.literal.nombre,gratuita.listadoLG.literal.lugar,"
			   columnSizes="35,35,10">
				
	<% if ((obj!= null) && (obj.size()>0)) { %>
		
				<%
				int recordNumber=1;
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDINSTITUCION
				2- IDLISTA
				3- OBSERVACIONES
			-->
			<!-- Campos visibles por cada fila:
				1- NOMBRE LISTAGUARDIAS
				2- LUGAR
			-->		
			<%
			String lugar = ((String)hash.get("LUGAR")).equals("")?"":(String)hash.get("LUGAR");
			String observaciones = ((String)hash.get("OBSERVACIONES")).equals("")?"":(String)hash.get("OBSERVACIONES");
			%>
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
				<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDINSTITUCION")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDLISTA")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=observaciones%>'><%=hash.get("NOMBRE")%></td>			
				<td><%=UtilidadesString.mostrarDatoJSP(lugar)%></td>			
			</siga:FilaConIconos>		
				<% 		recordNumber++; %>
				<% } %>
		<!-- FIN: RESULTADO -->
	<% } else { %>
	 		<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
	<% } %>

	</siga:Table>

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>