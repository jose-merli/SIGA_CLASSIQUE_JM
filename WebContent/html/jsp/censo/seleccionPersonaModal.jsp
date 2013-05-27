<!-- seleccionPersonaModal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	String	idPersona="";
	String	idInstitucion="";
	String	nifCif="";
	String	nombre="";
	String	apellido1="";
	String	apellido2="";

	Hashtable datosPersona = (Hashtable) request.getAttribute("datosPersona");
	if (datosPersona!=null) {
		idPersona = (String) datosPersona.get("idPersona");
		if (idPersona==null) idPersona="";
		idInstitucion = (String)datosPersona.get("idInstitucion");
		if (idInstitucion==null) idInstitucion="";
		nifCif = (String) datosPersona.get("nifCif");
		if (nifCif==null) nifCif="";
		nombre = (String) datosPersona.get("nombre");
		if (nombre==null) nombre="";
		apellido1 = (String) datosPersona.get("apellido1");
		if (apellido1==null) apellido1="";
		apellido2 = (String) datosPersona.get("apellido2");
		if (apellido2==null) apellido2="";
	}


%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script>
			function cerrarVentana() {
			
				var aux = new Array();
				aux[0]="<%=idPersona %>";
				aux[1]="<%=idInstitucion %>";
				aux[2]="<%=nifCif %>";
				aux[3]="<%=UtilidadesString.cambiarDoblesComillas(nombre) %>";
				aux[4]="<%=UtilidadesString.cambiarDoblesComillas(apellido1) %>";
				aux[5]="<%=UtilidadesString.cambiarDoblesComillas(apellido2) %>";
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
