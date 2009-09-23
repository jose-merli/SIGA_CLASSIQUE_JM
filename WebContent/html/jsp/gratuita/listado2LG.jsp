<!-- Definir Listas de Guardias: Modal: listado2LG.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaLG.literal.titulo" 
		localizacion="gratuita.busquedaLG.literal.titulo"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<script>
		function activarOrden(turno,guardia,orden){
			parent.document.forms[0].idTurno.value = turno;
			parent.document.forms[0].idGuardia.value = guardia;
			parent.document.forms[0].ordenOriginal.value = orden;
			parent.document.getElementById('ordenAux').disabled = false;
		}
	</script>
</head>

<body>

	<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = "buscar"/>
		<input type="hidden" name="modo" />
		<input type="hidden" name="idPersonaSociedadInicial" value="">
	</html:form>	

		<siga:TablaCabecerasFijas 		   
			   nombre="listadoLG"
			   borde="2"
			   clase="tableTitle"		   
			   nombreCol=" ,gratuita.modalLG.literal.turno,gratuita.modalLG.literal.guardia"
			   tamanoCol="5,35,40"
		   			alto="100%"
			>
		
		<!-- INICIO: RESULTADO -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
				<%
				int recordNumber=1;
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDTURNO
				2- IDGUARDIA
				3- ORDEN
			-->
			<!-- Campos visibles por cada fila:
				1- TURNO
				2- GUARDIA    
			-->
			<tr class="listaNonEdit">
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' id='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDTURNO")%>'>
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' id='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGUARDIA")%>'>
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("ORDEN")%>'>
				<td align="center"><input type="radio" name="guardiaEscogida" value="<%=String.valueOf(recordNumber)%>" onclick="activarOrden(document.getElementById('oculto<%=String.valueOf(recordNumber)%>_1').value,document.getElementById('oculto<%=String.valueOf(recordNumber)%>_2').value,document.getElementById('oculto<%=String.valueOf(recordNumber)%>_3').value)"></td>
				<td><%=hash.get("TURNO")%></td>
				<td><%=hash.get("GUARDIA")%></td>
				
			</tr>
				<% 		recordNumber++; %>
				<% } %>
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