<!-- seleccionDireccionModal.jsp -->
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
<%@ page import="com.siga.beans.EnvDestinatariosBean"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	
		String domicilio = "";
		String poblacion = "";
		String poblacionExt = "";
		String provincia = "";
		String pais = "";
		String cp = "";
		String fax1 = "";
		String fax2 = "";
		String correoElectronico = "";	
		String idPoblacion = "";
		String idProvincia = "";
		String idPais = "";
		String idDireccion = "";
		String idTipoEnvio = "";
		String telefono1 = "";
		String movil = "";
		
		
	Hashtable direccion = (Hashtable) request.getAttribute("direccion");
	if (direccion!=null) {
		domicilio = (String) direccion.get(EnvDestinatariosBean.C_DOMICILIO);
		if (domicilio == null) domicilio = "";
		idPais = (String) direccion.get(EnvDestinatariosBean.C_IDPAIS);
		if (idPais == null) idPais = "&nbsp;";
		poblacion = (String) direccion.get("POBLACION");
		if (poblacion == null) poblacion = "";
		poblacionExt = (String) direccion.get("POBLACIONEXTRANJERA");
		String pob =poblacion;
		if (!idPais.equals(ClsConstants.ID_PAIS_ESPANA)) {
			pob = poblacionExt;
			if (pob == null) pob = "";
		}		
		
		provincia = (String) direccion.get("PROVINCIA");
		if (provincia == null) provincia = "";
		pais = (String) direccion.get("PAIS");
		if (pais == null) pais = "";
		cp = (String) direccion.get(EnvDestinatariosBean.C_CODIGOPOSTAL);
		if (cp == null) cp = "";
		fax1 = (String) direccion.get(EnvDestinatariosBean.C_FAX1);
		if (fax1 == null) fax1 = "";
		fax2 = (String) direccion.get(EnvDestinatariosBean.C_FAX2);
		if (fax2 == null) fax2 = "";
		correoElectronico = (String) direccion.get(EnvDestinatariosBean.C_CORREOELECTRONICO);
		if (correoElectronico == null) correoElectronico = "";
		idPoblacion = (String) direccion.get(EnvDestinatariosBean.C_IDPOBLACION);
		if (idPoblacion == null) idPoblacion = "&nbsp;";
		idProvincia = (String) direccion.get(EnvDestinatariosBean.C_IDPROVINCIA);
		if (idProvincia == null) idProvincia = "&nbsp;";
		idDireccion = (String) direccion.get(CenDireccionesBean.C_IDDIRECCION);
		if (idDireccion == null) idDireccion = "&nbsp;";
		idTipoEnvio = (String) direccion.get(EnvEnviosBean.C_IDTIPOENVIOS);
		if (idTipoEnvio == null) idTipoEnvio = "&nbsp;";
		telefono1 = (String) direccion.get(CenDireccionesBean.C_TELEFONO1);
		if (telefono1 == null) telefono1 = "&nbsp;";
		movil = (String) direccion.get(CenDireccionesBean.C_MOVIL);
		if (movil == null) movil = "&nbsp;";
		
	}	

%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<script>
			function cerrarVentana() {
				var aux = new Array();
				aux[0]=unescape("<%=UtilidadesString.cambiarDoblesComillas(domicilio) %>");
				aux[1]=unescape("<%=poblacion %>");
				aux[2]=unescape("<%=provincia %>");
				aux[3]=unescape("<%=pais %>");
				aux[4]=unescape("<%=cp %>");
				aux[5]=unescape("<%=fax1 %>");
				aux[6]=unescape("<%=fax2 %>");
				aux[7]=unescape("<%=correoElectronico %>");
				aux[8]=unescape("<%=idPoblacion %>");
				aux[9]=unescape("<%=idProvincia %>");
				aux[10]=unescape("<%=idPais %>");
				aux[11]=unescape("<%=idDireccion %>");
				aux[12]=unescape("<%=idTipoEnvio %>");
				aux[13]=unescape("<%=telefono1%>");
				aux[14]=unescape("<%=movil%>");
				aux[15]=unescape("<%=poblacionExt%>");
				
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
