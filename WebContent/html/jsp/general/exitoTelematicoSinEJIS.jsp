<!-- exitoTelematicoSinEJIS.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>


<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
<script type="text/javascript">
		
		function onload(){	
			var mensajeDescarga = "${mensajeDescarga}"; 
			if(mensajeDescarga && mensajeDescarga!=''){
				if (confirm(mensajeDescarga)){
					accionSiguiente();
				}
			}
			return true;
		}
		
		function accionSiguiente(){	
			sub();

		    if(${idsInforme}==""){
		    	alert("Debe seleccionar al menos un informe");
		    	fin();
		    	return false;
		    }
		    
			document.InformesGenericosForm.modo.value = "download";
			document.InformesGenericosForm.submit();
		}

</script>

</head>

<body onload=onload();>
<bean:define id="mensajeDescarga" name="mensajeDescarga" scope="request"/>
<bean:define id="idsInforme" name="idsInforme" scope="request"/>
<bean:define id="datosInforme" name="datosInforme" scope="request"/>

<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden styleId ="modo"			property ="modo" />
	<html:hidden styleId ="idInforme"		property ="idInforme" value ="${idsInforme}"/> 				
	<html:hidden styleId ="idTipoInforme"	property ="idTipoInforme" value="OFICI"/>
	<html:hidden styleId ="datosInforme"	property ="datosInforme" value ="${datosInforme}"/>
	<html:hidden styleId ="enviar"			property ="enviar" value ="0"/>
	<html:hidden styleId ="descargar"		property ="descargar" value ="1"/>
</html:form>
<script type="text/javascript">
</script>

</body>

</html>

