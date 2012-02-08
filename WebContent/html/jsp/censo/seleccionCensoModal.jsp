<!-- seleccionCensoModal.jsp -->
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
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	String	idPersona="";
	String	idInstitucion="";
	String	nColegiado="";
	String	nifCif="";
	String	nombre="";
	String	apellido1="";
	String	apellido2="";

	Hashtable datosCliente = (Hashtable) request.getAttribute("datosCensoModal");
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

	String  dni="", mail="", numcolegiado="", telefono="", descripcion= "", nacido = "", fechaNac = "";
 	String  codPostal= "", direcion="",provincia="", poblacion= "", residencia="", ejerciente="", sexo = "", tratamiento= "";
	String pais = "",  cPostal = "", telefono1 = "", movil = "", idDireccion = "", direccion = "", idPais = "", poblacionExtranjera = "", fax="";
	Hashtable h = (Hashtable) request.getAttribute("unicaDireccion");

		pais        = (String) datosCliente.get("pais");				 if (pais        == null) pais        = new String("");
		poblacion   = (String) datosCliente.get("poblacion");	         if (poblacion   == null) poblacion   = new String("");
		provincia   = (String) datosCliente.get("provincia");	         if (provincia   == null) provincia   = new String("");
		direccion   = (String) datosCliente.get("direcion");	         if (direccion   == null) direccion   = new String("");
		cPostal     = (String) datosCliente.get("codPostal");            if (cPostal     == null) cPostal     = new String("");
		telefono1   = (String) datosCliente.get("telefono");     	     if (telefono1   == null) telefono1   = new String("");
		idDireccion = (String) datosCliente.get("idDireccion");     	 if (idDireccion == null) idDireccion = new String("");
		idPais      = (String) datosCliente.get("idPais");               if (idPais      == null) idPais      = new String("");
		mail        = (String) datosCliente.get("mail");                 if (mail      	 == null) mail        = new String("");
		sexo        = (String) datosCliente.get("sexo");                 if (sexo      	 == null) sexo        = new String("");
		tratamiento = (String) datosCliente.get("tratamiento");          if (tratamiento == null) tratamiento = new String("");
		fax         = (String) datosCliente.get("fax1");                 if (fax      	 == null) fax      	  = new String("");
		nacido      = (String) datosCliente.get("LugarNacimiento");      if (nacido    	 == null) nacido      = new String("");
		fechaNac    = (String) datosCliente.get("FechaNacimiento");      if (fechaNac  	 == null) fechaNac    = new String("");
		poblacionExtranjera = null; /*(String) h.get(CenDireccionesBean.C_POBLACIONEXTRANJERA);*/	  if (poblacionExtranjera == null) poblacionExtranjera = new String("");
	
		if (!fechaNac.equals("")){
			fechaNac = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(user.getLanguage(),fechaNac));
		}

%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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

					aux[7]  = "<%=UtilidadesString.cambiarDoblesComillas(direccion) %>";
					aux[8]  = "<%=poblacion%>";
					aux[9]  = "<%=provincia%>";
					aux[10] = "<%=pais%>";
					aux[11] = "<%=cPostal%>";
					aux[12] = "<%=telefono1%>";
					aux[13] = "<%=mail%>";
					aux[14] = "<%=sexo%>";
					aux[15] = "<%=tratamiento%>";
					aux[16] = "<%=fax%>";
					aux[17] = "<%=pais%>";
					aux[18] = "<%=nacido%>";
					aux[19] = "<%=fechaNac%>";

				
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
