<!-- selecTipoNuevoExp.jsp -->
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	String	idInstitucion_TipoExpediente="";
	String	idTipoExpediente="";
	
	
	Hashtable datosTipoExpediente = (Hashtable) request.getAttribute("datosTipoExpediente");
	if (datosTipoExpediente!=null) {
		idInstitucion_TipoExpediente = (String) datosTipoExpediente.get("idInstitucion_TipoExpediente");
		if (idInstitucion_TipoExpediente==null) idInstitucion_TipoExpediente="";
		idTipoExpediente = (String) datosTipoExpediente.get("idTipoExpediente");
		if (idTipoExpediente==null) idTipoExpediente="";			
	}
	String	ANIO=(String)datosTipoExpediente.get("anioEjg");
	String	NUMERO=(String)datosTipoExpediente.get("numeroEjg");
	String	IDTIPOEJG=(String)datosTipoExpediente.get("idTipoEjg");
	String	asunto=(String)datosTipoExpediente.get("asunto");			
	String	observaciones=(String)datosTipoExpediente.get("observaciones");
	String	numeroProcedimientoAsi=(String)datosTipoExpediente.get("numeroProcedimiento");	
	String	juzgadoAsi=(String)datosTipoExpediente.get("juzgado");
	String	juzgadoInstitucionAsi=(String)datosTipoExpediente.get("juzgadoInstitucion");
	String	idPretension=(String)datosTipoExpediente.get("pretension");
	String	idInstitucion=(String)datosTipoExpediente.get("idInstitucion");
	String	idturnoDesignado=(String)datosTipoExpediente.get("idturnoDesignado");
	String	nombreDesignado=(String)datosTipoExpediente.get("nombreDesignado");
	String  numColDesignado=(String)datosTipoExpediente.get("numColDesignado");
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

		<script>
			function cerrarVentana() {
				var aux = new Array();
				aux[0]="<%=idInstitucion_TipoExpediente %>";
				aux[1]="<%=idTipoExpediente %>";	
				document.ExpDatosGeneralesForm.modo.value="";
				document.ExpDatosGeneralesForm.idTipoExpediente.value="<%=idTipoExpediente %>";	
				document.ExpDatosGeneralesForm.idInstitucion_TipoExpediente.value="<%=idInstitucion_TipoExpediente %>";
				document.ExpDatosGeneralesForm.submit();		
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
	<html:form action="/EXP_Auditoria_DatosGenerales.do"  method="POST" target="mainWorkArea">
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = "nuevo"/>
		<html:hidden property = "metodo" value = "nuevoejg"/>	
		<html:hidden property ="idTipoExpediente"   value = ""/>	
		<html:hidden property ="numeroEjg"   value = "<%=NUMERO%>"/>
		<html:hidden property ="idTipoEjg"   value = "<%=IDTIPOEJG%>"/>
		<html:hidden property ="anioEjg"     value = "<%=ANIO%>"/>
		<html:hidden property ="observaciones"     value = "<%=observaciones%>" />
		<html:hidden property ="idInstitucion_TipoExpediente" value= ""/>	
		<html:hidden property ="numeroProcedimiento"   value = "<%=numeroProcedimientoAsi%>"/>
		<html:hidden property ="asunto"     value = "<%=asunto%>"/>
		<html:hidden property ="juzgado"   value = "<%=juzgadoAsi%>"/>
		<html:hidden property ="juzgadoInstitucion"   value = "<%= juzgadoInstitucionAsi%>"/>
		<html:hidden property ="pretension"     value = "<%=idPretension%>" />
		<html:hidden property ="pretensionInstitucion"     value = "<%=idInstitucion%>" />
		<html:hidden property ="idturnoDesignado"     value = "<%=idturnoDesignado %>" />
		<html:hidden property ="nombreDesignado" value= "<%=nombreDesignado%>"/>	
		<html:hidden property ="soloSeguimiento"     value = "false" />
		<html:hidden property ="editable" value= "1"/>
		<html:hidden property ="ncolegiadoDesignado" value= "<%=numColDesignado %>"/>
		<html:hidden property ="idclasificacion" value= "1"/>
			
					
		

		
	</html:form>
</body>

</html>

