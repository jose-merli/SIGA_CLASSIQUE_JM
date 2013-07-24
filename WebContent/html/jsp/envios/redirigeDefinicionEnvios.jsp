<!DOCTYPE html>
<html>
<head>
<!-- redirigeDefinicionEnvios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
</head>
<body>
<%
String idTipoEnvio = (request.getAttribute("idTipoEnvio")!=null?(String) request.getAttribute("idTipoEnvio"):"");
%>
<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea" scope="session" style="display:none">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = "envioModal"/>
			<html:hidden property = "idTipoEnvio" value = "<%=idTipoEnvio%>"/>
			<input type="hidden" name= "datosEnvios" value="<%=request.getAttribute("datosEnvios")%>"/>
			<input type="hidden" name= "descargar" value="<%=request.getAttribute("descargar")%>"/>
			<input type="hidden" name="subModo" value ="<%=request.getAttribute("subModo")%>"/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			
			
			
</html:form>
<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe> 
</body>
<script language="JavaScript">
function reloadPage() {
  	var resultado = ventaModalGeneral('DefinirEnviosForm','G');
  	if (resultado==undefined||resultado[0]==undefined){
  		if(parent.vueltaEnvio)
  			parent.vueltaEnvio();
  		else
			parent.refrescarLocal();	   		
  	} 
  	else {
  		parent.accionCerrar();
  		var idEnvio = resultado[0];
    	var idTipoEnvio = resultado[1];
    	//alert("idTipoEnvio"+idTipoEnvio);
    	var nombreEnvio = resultado[2];				    
   		document.DefinirEnviosForm.tablaDatosDinamicosD.value= idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio+ "#";;		
   		document.DefinirEnviosForm.modo.value='editar';
   		document.DefinirEnviosForm.submit();
   		
  	}

}
reloadPage();
</script>
</html>
