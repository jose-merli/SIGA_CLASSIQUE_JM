<!DOCTYPE html>
<html>
<head>
<!-- seleccionDenunciado.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<bean:define id="idPersona" name="ExpDenunciadoForm" property="idPersona" type="java.lang.String"/>
<bean:define id="idDireccion" name="ExpDenunciadoForm" property="idDireccion" type="java.lang.String"/>
<bean:define id="nombre" name="ExpDenunciadoForm" property="nombre" type="java.lang.String"/>
<bean:define id="primerApellido" name="ExpDenunciadoForm" property="primerApellido" type="java.lang.String"/>
<bean:define id="segundoApellido" name="ExpDenunciadoForm" property="segundoApellido" type="java.lang.String"/>
<bean:define id="nif" name="ExpDenunciadoForm" property="nif" type="java.lang.String"/>
<bean:define id="numColegiado" name="ExpDenunciadoForm" property="numColegiado" type="java.lang.String"/>
<bean:define id="idInstitucion" name="ExpDenunciadoForm" property="idInstitucion" type="java.lang.String"/>





	<!-- HEAD -->
	

		<script>
			function cerrarVentana() {
				
				var resultado = new Array();
				resultado[0]="<%=idPersona %>";
				resultado[1]="<%=idInstitucion %>";
				<%if(numColegiado==null)
					numColegiado="";%>
				resultado[2]= '<%=numColegiado%>';
				resultado[3]="<%=nif%>";
				resultado[4]="<%=nombre %>";
				resultado[5]="<%=primerApellido %>";
				resultado[6]="<%=segundoApellido %>";
				resultado[7]="<%=idDireccion%>";
				
				

				
			
				
				top.cierraConParametros(resultado);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
