<!DOCTYPE html>
<html>
<head>
<!-- seleccionClienteModal.jsp -->
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
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	String	idPersona="";
	String	idInstitucion="";
	String	nColegiado="";
	String	nifCif="";
	String	nombre="";
	String	apellido1="";
	String	apellido2="";

	Hashtable datosCliente = (Hashtable) request.getAttribute("datosClienteModal");
	if (datosCliente!=null) {
		idPersona = (String) datosCliente.get("idPersona");
		if (idPersona==null) idPersona="";
		idInstitucion = (String) datosCliente.get("idInstitucion");
		if (idInstitucion==null) idInstitucion="";
		nColegiado = (String) datosCliente.get("nColegiado");
		if (nColegiado==null) nColegiado="";
		nifCif = (String) datosCliente.get("nifCif");
		if (nifCif==null) nifCif="";
		nombre = (String) datosCliente.get("nombre");
		if (nombre==null) nombre="";
		apellido1 = (String) datosCliente.get("apellido1");
		if (apellido1==null) apellido1="";
		apellido2 = (String) datosCliente.get("apellido2");
		if (apellido2==null) apellido2="";
	}	

	String pais = "", direccion = "", poblacion = "", provincia = "", cPostal = "", telefono1 = "", movil = "", idDireccion = "", idPais = "", poblacionExtranjera = "";
	Hashtable h = (Hashtable) request.getAttribute("unicaDireccion");
	if (h != null) {
		pais        = (String) h.get("PAIS"); 		        			 if (pais        == null) pais        = new String("");
		poblacion   = (String) h.get("POBLACION");	        			 if (poblacion   == null) poblacion   = new String("");
		provincia   = (String) h.get("PROVINCIA");	        			 if (provincia   == null) provincia   = new String("");
		direccion   = (String) h.get(CenDireccionesBean.C_DOMICILIO);	 if (direccion   == null) direccion   = new String("");
		cPostal     = (String) h.get(CenDireccionesBean.C_CODIGOPOSTAL); if (cPostal     == null) cPostal     = new String("");
		telefono1   = (String) h.get(CenDireccionesBean.C_TELEFONO1);	 if (telefono1   == null) telefono1   = new String("");
		movil       = (String) h.get(CenDireccionesBean.C_MOVIL);		 if (movil       == null) movil       = new String("");
		idDireccion = (String) h.get(CenDireccionesBean.C_IDDIRECCION);	 if (idDireccion == null) idDireccion = new String("");
		idPais      = (String) h.get(CenDireccionesBean.C_IDPAIS);       if (idPais      == null) idPais      = new String("");
		poblacionExtranjera = (String) h.get(CenDireccionesBean.C_POBLACIONEXTRANJERA);	  if (poblacionExtranjera == null) poblacionExtranjera = new String("");
	}
	

%>	


	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script>
			function cerrarVentana() {
				var aux = new Array();
				aux[0]="<%=idPersona %>";
				aux[1]="<%=idInstitucion%>";
				
				aux[2]="<%=nColegiado %>";
				aux[3]="<%=nifCif %>";
				aux[4]="<%=UtilidadesString.cambiarDoblesComillas(nombre) %>";
				aux[5]="<%=UtilidadesString.cambiarDoblesComillas(apellido1) %>";
				aux[6]="<%=UtilidadesString.cambiarDoblesComillas(apellido2) %>";
				
				// Devolvemos la direccion si es unica
				<% if (h != null) { 
				
					if (idPais !=null && !idPais.equals("") && !idPais.equals(ClsConstants.ID_PAIS_ESPANA)) {
						poblacion = poblacionExtranjera;
					}
				%>
					aux[7]  = "<%=UtilidadesString.escape(direccion)%>";
					aux[8]  = "<%=UtilidadesString.escape(poblacion)%>";
					aux[9]  = "<%=UtilidadesString.escape(provincia)%>";
					aux[10] = "<%=UtilidadesString.escape(pais)%>";
					aux[11] = "<%=cPostal%>";
					aux[12] = "<%=idDireccion%>";
					aux[13] = "<%=telefono1%>";
					aux[14] = "<%=movil%>";

				<% } %>
				
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
