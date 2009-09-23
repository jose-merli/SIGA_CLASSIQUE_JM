<!-- listadoModal_IncompatibilidadesGuardia.jsp -->
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
 	
	<script>
		function activarMotivo(idturno_incompatible, idguardia_incompatible){
			parent.document.forms[0].idTurnoIncompatible.value = idturno_incompatible;
			parent.document.forms[0].idGuardiaIncompatible.value = idguardia_incompatible;			
			parent.document.getElementById('motivosAux').disabled = false;
		}
		function marcarDesmarcarTodos(o) 
			{
				var ele = document.getElementsByName("chkGuardia");
				for (i = 0; i < ele.length; i++) {
					ele[i].checked = o.checked;
					
				}
				parent.document.getElementById('motivosAux').disabled = false;
			}
			
	</script>
</head>

<body>

	<html:form action="/JGR_IncompatibilidadesGuardia.do" method="post" target="mainWorkArea">
		<html:hidden property = "modo" value = ""/>
	</html:form>

	<!-- INICIO: RESULTADO -->
		<siga:TablaCabecerasFijas 		   
			   nombre="listadoModal"
			   borde="1"
			   clase="tableTitle"		   
			   nombreCol="<input type='checkbox' name='checkGeneral' onclick='marcarDesmarcarTodos(this)'/>,gratuita.listado_IncompatibilidadesGuardia.literal.turno,gratuita.listado_IncompatibilidadesGuardia.literal.guardia,gratuita.modal_IncompatibilidadesGuardia.literal.tipodias"
			   tamanoCol="5,25,25,25"
		   			alto="100%"

		>

	<% if ((obj!= null) && (obj.size()>0)) { %>
				<%
				int recordNumber=1;
				String turno="", guardia="", tipodias="", idturno_incompatible="", idguardia_incompatible="";
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
						turno = ((String)hash.get("TURNO")).equals("")?"&nbsp;":(String)hash.get("TURNO");
						guardia = ((String)hash.get("GUARDIA")).equals("")?"&nbsp;":(String)hash.get("GUARDIA");
						tipodias = ((String)hash.get("TIPODIAS")).equals("")?"&nbsp;":(String)hash.get("TIPODIAS");
						idturno_incompatible= ((String)hash.get("IDTURNO_INCOMPATIBLE")).equals("")?"&nbsp;":(String)hash.get("IDTURNO_INCOMPATIBLE");
						idguardia_incompatible= ((String)hash.get("IDGUARDIA_INCOMPATIBLE")).equals("")?"&nbsp;":(String)hash.get("IDGUARDIA_INCOMPATIBLE");
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDTURNO_INCOMPATIBLE
				2- IDGUARDIA_INCOMPATIBLE
			-->
			<!-- Campos visibles por cada fila:
				1- TURNO
				2- GUARDIA
				3- TIPODIAS
			-->
			<tr class="listaNonEdit">
			   <td align="center">
					<!--<input type="radio" name="guardiaEscogida" value="< %=String.valueOf(recordNumber)%>" onclick="activarMotivo(document.getElementById('oculto< %=String.valueOf(recordNumber)%>_1').value,document.getElementById('oculto< %=String.valueOf(recordNumber)%>_2').value)" />-->
					<input type="checkbox" value="<%=String.valueOf(recordNumber)%>" name="chkGuardia" onclick="activarMotivo(document.getElementById('oculto<%=String.valueOf(recordNumber)%>_1').value,document.getElementById('oculto<%=String.valueOf(recordNumber)%>_2').value)" >
					
				</td>
				<td>
					<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idturno_incompatible%>' />
					<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idguardia_incompatible%>' />
					<%=turno%>
				</td>
				<td><%=guardia%></td>
				<td><%=tipodias%></td>				
				
			</tr>
				<% 		recordNumber++; %>
				<% } %>
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<br>
		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
		<br>
	<% } %>
			</siga:TablaCabecerasFijas>
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>