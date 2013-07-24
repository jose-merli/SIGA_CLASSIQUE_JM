<!DOCTYPE html>
<html>
<head>
<!-- consultaCriteriosFacturacion.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getSession().getAttribute("vHito");
	request.getSession().removeAttribute("vHito");
	
	String sRegularizacion = (String) request.getParameter("regularizacion");
	String modo = (String) request.getParameter("modo");
	String idFacturacion = (String) request.getParameter("idFacturacion");
	String idInstitucion = (String) request.getParameter("idInstitucion");
	String estado = (String)ses.getAttribute("estado");

	String prevision = request.getParameter("prevision");
	boolean bPrevision = false;
	if (prevision!=null && prevision.equals("S")) {
		bPrevision = true;
		request.getSession().setAttribute("prevision","S");
	} else {
		prevision = (String) request.getSession().getAttribute("prevision");
		if (prevision!=null && prevision.equals("S")) {
			bPrevision = true;
		}
	}
	String alto = "192";
	if (bPrevision) {
		alto = "263";
	}
		
	ses.removeAttribute("Modo");
	ses.removeAttribute("idFacturacion");
	ses.removeAttribute("estado");
	if (modo==null)modo="Consulta";
	int estadoInt = 0;
	if (estado!=null){
		estadoInt = Integer.parseInt(estado);
	}
	String botones = "";
	if (estadoInt < 20) botones = "B";
	
	if ((sRegularizacion != null) && (sRegularizacion.equalsIgnoreCase("true"))) botones = "";
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

		function refrescarLocal(){ 
			parent.buscarCriterios();
		}
		
	
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		</html:form>	
		
		
		<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="factSJCS.datosFacturacion.literal.gruposFacturacion,factSJCS.datosFacturacion.literal.hitos,"
			   columnSizes="35,45,10"
			   modal="P">
		<% if (obj==null || obj.size()==0){%>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		<%}else{%>
		
			<!-- Campo obligatorio -->
			  <%
		    	int recordNumber=1;
				while ((recordNumber) <= obj.size()){	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			 	%>	
				  	<siga:FilaConIconos visibleEdicion='no' visibleConsulta='no' fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" modo="<%=modo%>">
						<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idFacturacion%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGRUPOFACTURACION")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDHITOGENERAL")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idInstitucion%>'><%=UtilidadesString.mostrarDatoJSP((String)hash.get("NOMBRE"))%></td>
						<td><siga:Idioma key='<%=(String)hash.get("DESCRIPCION")%>'/></td>
					</siga:FilaConIconos>	
				<%recordNumber++;%>
				<%}%>	
		<%}%>
		</siga:Table>
		

<!-- FIN: LISTA DE VALORES -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
		
	
	</body>
</html>
		  
		
