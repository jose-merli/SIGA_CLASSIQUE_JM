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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	Row registro=(Row)request.getAttribute("resultado");
	
	
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
			function enviaRespuesta() {
				<%if(registro==null){%>
				alert("<siga:Idioma key="gratuita.busquedaSJCS.literal.noLetradosCola"/>");
				parent.manejaRespuestaFormAutomatico(null);
				
				<%}else{%>		
				var res = new Array();
				res[0]=document.forms[0].idPersona.value;
				res[1]=document.forms[0].ncolegiado.value;
				res[2]=document.forms[0].nombre.value;
				res[3]=document.forms[0].apellido1.value;
				res[4]=document.forms[0].apellido2.value;
				res[5]=document.forms[0].salto.value;
				res[6]=document.forms[0].compensacion.value;
				alert("<siga:Idioma key="gratuita.busquedaSJCS.literal.seleccionadoA"/> "+
						document.forms[0].nombre.value+" "+
						document.forms[0].apellido1.value+" "+
						document.forms[0].apellido2.value	);
				parent.manejaRespuestaFormAutomatico(res);
				<%}%>
				
			}
		</script>

	</head>


<body onLoad="enviaRespuesta();" >
	<%if(registro!=null){
			String idPersona = (registro.getString(CenColegiadoBean.C_IDPERSONA)==null||((String)registro.getString(CenColegiadoBean.C_IDPERSONA)).equals(""))?"&nbsp;":(String)registro.getString(CenColegiadoBean.C_IDPERSONA);
			String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
			String salto = UtilidadesString.mostrarDatoJSP(registro.getString("SALTO"));
			String compensacion = UtilidadesString.mostrarDatoJSP(registro.getString("COMPENSACION"));%>
	<html:form action="/JGR_BusquedaClientesFiltros.do">
		<input type="hidden" name="idPersona" value="<%=idPersona%>">
		<input type="hidden" name="ncolegiado" value="<%=ncolegiado%>">
		<input type="hidden" name="nombre" value="<%=nombre%>">
		<input type="hidden" name="apellido1" value="<%=apellido1%>">
		<input type="hidden" name="apellido2" value="<%=apellido2%>">
		<input type="hidden" name="salto" value="<%=salto%>">
		<input type="hidden" name="compensacion" value="<%=compensacion%>">
	</html:form>
	<%}%>

</body>
</html>
