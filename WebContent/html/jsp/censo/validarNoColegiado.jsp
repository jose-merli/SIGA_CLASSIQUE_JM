<!DOCTYPE html>
<html>
<head>
<!-- validarNoColegiado.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->

<!-- JSP -->
	
<bean:define id="nombre" name="datosGeneralesForm" property="nombre" type="java.lang.String"/>
<bean:define id="apellido1" name="datosGeneralesForm" property="apellido1" type="java.lang.String"/>
<bean:define id="apellido2" name="datosGeneralesForm" property="apellido2" type="java.lang.String"/>
<bean:define id="accion" name="datosGeneralesForm" property="accion" type="java.lang.String"/>
<bean:define id="idPersona" name="datosGeneralesForm" property="idPersona" type="java.lang.String"/>
<bean:define id="idInstitucion" name="datosGeneralesForm" property="idInstitucion" type="java.lang.String"/>
<bean:define id="numColegiado" name="datosGeneralesForm" property="numColegiado" type="java.lang.String"/>
<bean:define id="numIdentificacion" name="datosGeneralesForm" property="numIdentificacion" type="java.lang.String"/>




<!-- HEAD -->


<script>
	function cerrarVentana() {
		var resultado = new Array();
		
		<%if(accion.equals("messages.fichaCliente.mostrarPersonaExistente")){%>
			var type = '<siga:Idioma key="<%=accion%>"/>';
			alert(type);
			
			//Ponemos los datos comunes con otras jsp para que no vayan todas a su aire
			resultado[0]="<%=idPersona %>";
			resultado[1]="<%=idInstitucion %>";
			
			<%

			if(numColegiado==null)
				numColegiado="";%>
			resultado[2]= '<%=numColegiado%>';
				
			resultado[3]="<%=numIdentificacion%>";
			resultado[4]="<%=nombre %>";
			resultado[5]="<%=apellido1 %>";
			resultado[6]="<%=apellido2 %>";
			resultado[7]="";
            window.parent.traspasaDatos(resultado);
		<%}else if(accion.equals("messages.fichaCliente.clienteExiste")){%>
			var type = '<siga:Idioma key="<%=accion%>"/>';
			alert(type);
			//Ponemos los datos comunes con otras jsp para que no vayan todas a su aire
			resultado[0]="<%=idPersona %>";
			resultado[1]="<%=idInstitucion %>";
			<%if(numColegiado==null)
				numColegiado="";%>
			resultado[2]= '<%=numColegiado%>';
			resultado[3]="<%=numIdentificacion%>";
			resultado[4]="<%=nombre %>";
			resultado[5]="<%=apellido1 %>";
			resultado[6]="<%=apellido2 %>";
			resultado[7]="";
			
			window.parent.traspasaDatosCliente(resultado);
		<%}%>
		
		
	
	}
</script>

</head>

<body onLoad="cerrarVentana();">

<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
</body>

</html>
