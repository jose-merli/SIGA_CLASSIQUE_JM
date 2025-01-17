<!DOCTYPE html>
<html>
<head>
<!-- listadoModal_IncompatibilidadesGuardia.jsp -->

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
	
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<script>
		function activarMotivo(idturno_incompatible, idguardia_incompatible){
			parent.document.forms[0].idTurnoIncompatible.value = idturno_incompatible;
			parent.document.forms[0].idGuardiaIncompatible.value = idguardia_incompatible;			
			parent.document.getElementById('motivosAux').disabled = false;
		}
		
		function marcarDesmarcarTodos(o) {
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
	<siga:Table 		   
		name="listadoModal"
		border="1"
		columnNames="<input type='checkbox' name='checkGeneral' onclick='marcarDesmarcarTodos(this)'/>,
		   			gratuita.listado_IncompatibilidadesGuardia.literal.turno,
		   			gratuita.listado_IncompatibilidadesGuardia.literal.guardia,
		   			gratuita.modal_IncompatibilidadesGuardia.literal.tipodias"
		columnSizes="5,25,25,25">

<% 
		if ((obj!= null) && (obj.size()>0)) {
			int recordNumber=1;
			String turno="", guardia="", tipodias="", idturno_incompatible="", idguardia_incompatible="";
			while ((recordNumber) <= obj.size()) {	 	
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
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