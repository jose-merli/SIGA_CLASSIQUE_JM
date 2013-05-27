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
<%@ page import="com.atos.utils.*"%>


<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.siga.beans.CenPersonaBean"%>
<%@page import="com.siga.beans.CenClienteBean"%>
<%@page import="java.io.File"%><html:html>
<head>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");	
	String mensaje=(String)request.getAttribute("msj");
	Vector resultado = null;
	resultado = (Vector) request.getAttribute("CenResultadoDatosGenerales");
	String idInstitucion = "";
	String idPersona = "";
	String fotografia = "";
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	String nIdentificacion = "";
	String abono ="";
	String cargo ="";
	String cuentaContable="";
	String idioma="";
	String guiaJudicial = "";
	String publicidad = "";
	String comisiones ="";
	String noRedAbogacia ="";
	String tipoi="";
	Hashtable registro = (Hashtable) resultado.get(0);

		
			idPersona = (String) registro.get(CenPersonaBean.C_IDPERSONA);
			if (idPersona==null) idPersona=""; 
			// fotografia con path virtual
			fotografia = (String) registro.get(CenClienteBean.C_FOTOGRAFIA);
			if (fotografia!=null && !fotografia.equals("")) {
				//fotografia = "/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_FOTOS+"/"+idInstitucion+"/"+fotografia;
				fotografia = File.separatorChar+ClsConstants.PATH_DOMAIN+
								  File.separatorChar+ClsConstants.RELATIVE_PATH_FOTOS+
								  File.separatorChar+idInstitucion+
								  File.separatorChar+fotografia;
			}
			nombre = (String) registro.get(CenPersonaBean.C_NOMBRE);
			if (nombre==null) nombre=""; 
			apellido1 = (String) registro.get(CenPersonaBean.C_APELLIDOS1);
			if (apellido1==null) apellido1=""; 
			apellido2 = (String) registro.get(CenPersonaBean.C_APELLIDOS2);
			if (apellido2==null) apellido2=""; 
			nIdentificacion = (String) registro.get(CenPersonaBean.C_NIFCIF);
			if (nIdentificacion==null) nIdentificacion=""; 
			 
			abono = (String) registro.get(CenClienteBean.C_ABONOSBANCO);
			if (abono==null) abono=""; 
			cargo = (String) registro.get(CenClienteBean.C_CARGOSBANCO);
			if (cargo==null) cargo=""; 
			cuentaContable = (String) registro.get(CenClienteBean.C_ASIENTOCONTABLE);
			if (cuentaContable==null) cuentaContable=""; 
			idioma = (String) registro.get(CenClienteBean.C_IDLENGUAJE);
			if (idioma==null) idioma=""; 
			guiaJudicial = (String) registro.get(CenClienteBean.C_GUIAJUDICIAL);
			if (guiaJudicial==null) guiaJudicial=""; 
			publicidad = (String) registro.get(CenClienteBean.C_PUBLICIDAD);
			if (publicidad==null) publicidad=""; 
			comisiones = (String) registro.get(CenClienteBean.C_COMISIONES);
			if (comisiones==null) comisiones="";			
			noRedAbogacia = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
			if (noRedAbogacia==null) noRedAbogacia="";
			tipoi = (String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION);
			if (tipoi==null) tipoi="";
%>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<%=mensaje%>';
			if (confirm(type)) {			
				document.forms[0].continuarAprobacion.value="1";			    
				document.forms[0].submit();
			}
		}
	</script>
</head>

<body onload="reloadPage();">
<html:form action="/CEN_DatosGenerales.do" method="POST" target="_self">
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden name="datosGeneralesForm" property = "continuarAprobacion" value = ""/>
		<html:hidden name="datosGeneralesForm" property="idInstitucion"/>
		<html:hidden name="datosGeneralesForm" property="numIdentificacion"  value="<%=nIdentificacion%>" />
		<html:hidden name="datosGeneralesForm" property="idPersona" value="<%=idPersona%>" />
		<html:hidden name="datosGeneralesForm" property="nombre" value="<%=nombre%>" />
		<html:hidden name="datosGeneralesForm" property="apellido1" value="<%=apellido1%>" />
		<html:hidden name="datosGeneralesForm" property="apellido2" value="<%=apellido2%>" />
		<html:hidden name="datosGeneralesForm" property="abono" value="<%=abono%>"/>
		<html:hidden name="datosGeneralesForm" property="cargo"  value="<%=cargo%>"/>
		<html:hidden name="datosGeneralesForm" property="cuentaContable" value="<%=cuentaContable %>"></html:hidden>
		<html:hidden name="datosGeneralesForm" property="idioma" value="<%=idioma%>"></html:hidden>
		
		<% if (guiaJudicial.equals(ClsConstants.DB_TRUE)) { %>
			<html:hidden  name="datosGeneralesForm" property="guiaJudicial"  value="<%=ClsConstants.DB_TRUE%>"></html:hidden>		
		<%}%>
		
		<%  if (publicidad.equals(ClsConstants.DB_TRUE)) { 	%>
		    <html:hidden  name="datosGeneralesForm" property="publicidad"  value="<%=ClsConstants.DB_TRUE%>"></html:hidden>
		<%}%>
		<% if (comisiones.equals(ClsConstants.DB_TRUE)) { %>
		    <html:hidden  name="datosGeneralesForm" property="comisiones"  value="<%=ClsConstants.DB_TRUE%>"></html:hidden>
		<%}%>
		
		<%  if (noRedAbogacia.equals(ClsConstants.DB_TRUE)) {%>
			<html:hidden  name="datosGeneralesForm" property="noRedAbogacia"  value="<%=ClsConstants.DB_TRUE%>"></html:hidden>
		<%} %>
		
		<html:hidden name="datosGeneralesForm" property="tipoIdentificacion" value="<%=tipoi%>"></html:hidden>
		
		
</html:form>


		
	

</body>
</html:html>