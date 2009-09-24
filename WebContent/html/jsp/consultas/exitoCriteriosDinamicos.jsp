<!-- exitoCriteriosDinamicos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.consultas.CriterioDinamico"%>

<html>
<head>
<%
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	
	
	// ATRIBUTOS
	CriterioDinamico[] criterios = (CriterioDinamico[])request.getAttribute("criterios");
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

</head>

<body onload="reloadPage();">
	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="submitArea">
		<html:hidden property = "hiddenFrame" value = "1"/>		
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property="modo" value="ejecutarConsulta"/>	
	</html:form>
		
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe> 		
</body>

	<script>
	function reloadPage() {	
		<%for (int i=0;i<criterios.length && criterios[i]!=null;i++){%>	
			var input = document.createElement('INPUT');
			input.type = "hidden";
			input.name = "criteriosDinamicos["+<%=i%>+"].idC";
			input.value = "<%=criterios[i].getIdC()%>";
			document.forms[0].appendChild(input);
			
			var input2 = document.createElement('INPUT');
			input2.type = "hidden";
			input2.name = "criteriosDinamicos["+<%=i%>+"].tc";
			input2.value = "<%=criterios[i].getTc()%>";
			document.forms[0].appendChild(input2);
			
			var input3 = document.createElement('INPUT');
			input3.type = "hidden";
			input3.name = "criteriosDinamicos["+<%=i%>+"].val";
			input3.value = "<%=criterios[i].getVal()%>";
			document.forms[0].appendChild(input3);
			
			var input4 = document.createElement('INPUT');
			input4.type = "hidden";
			input4.name = "criteriosDinamicos["+<%=i%>+"].op";
			input4.value = "<%=criterios[i].getOp()%>";
			document.forms[0].appendChild(input4);
			
			var input5 = document.createElement('INPUT');
			input5.type = "hidden";
			input5.name = "criteriosDinamicos["+<%=i%>+"].st";
			input5.value = "<%=criterios[i].getSt()%>";
			document.forms[0].appendChild(input5);
			
			var input6 = document.createElement('INPUT');
			input6.type = "hidden";
			input6.name = "criteriosDinamicos["+<%=i%>+"].hp";
			input6.value = "<%=criterios[i].getHp()%>";
			document.forms[0].appendChild(input6);
		<%}%>		
		var salida = ventaModalGeneral(document.forms[0].name,"G");
		if (salida == undefined || salida == "VACIO"){
			top.cierraConParametros("VACIO");	
		}
	}
	</script>
</html>