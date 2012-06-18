<!-- Definir Listas de Guardias: Modal: listado3LG.jsp-->
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

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");	
	String accion = request.getAttribute("accion")==null?"":(String)request.getAttribute("accion");	
	String botones = "B";
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
			parent.buscarGuardias()
		}
	</script>
 	
</head>

<body class="tablaCentralCampos">

	<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" value = "<%=modo%>"/>
		<html:hidden property = "accion" value = "<%=accion%>"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		

	<siga:TablaCabecerasFijas
			   nombre="listado3LG"
			   borde="2"
			   clase="tableTitle"
			   nombreCol="gratuita.mantenimientoLG.literal.orden,gratuita.mantenimientoLG.literal.turno,gratuita.mantenimientoLG.literal.guardia,gratuita.mantenimientoLG.literal.tipodia,"
			   tamanoCol="8,32,20,21,9"
		   			alto="100%"
	>

	<!-- INICIO: RESULTADO -->
	<% if ((obj != null) && (obj.size()>0)) { %>
			<%
					int recordNumber=1;
					while ((recordNumber) <= obj.size()) {	 
						Hashtable miHash = (Hashtable)obj.get(recordNumber-1);
			%>
			<!-- Campos ocultos por cada fila:      
					1- IDINSTITUCION
					2- IDLISTA
					3- IDGUARDIA
					4- IDTURNO
			-->
			<!-- Campos visibles por cada fila:
					1- ORDEN
					2- TURNO
					3- GUARDIA
					4- TIPODIAS
			-->				
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" modo="<%=accion%>">
				<td>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=miHash.get("IDINSTITUCION")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=miHash.get("IDLISTA")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=miHash.get("IDGUARDIA")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=miHash.get("IDTURNO")%>'>
					<%=miHash.get("ORDEN")%></td>
				<td><%=miHash.get("TURNO")%></td>
				<td><%=miHash.get("GUARDIA")%></td>
				<td><%=miHash.get("TIPODIASPARSEADO")%></td>								
			</siga:FilaConIconos>
			<% 			recordNumber++; %>
			<%  	} %>

	<!-- FIN: RESULTADO -->
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } %>	

	</siga:TablaCabecerasFijas>


	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>