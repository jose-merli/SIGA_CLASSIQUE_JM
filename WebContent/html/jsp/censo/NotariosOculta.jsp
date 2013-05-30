<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga" %>
<%@ taglib uri = "struts-bean.tld"   prefix="bean" %>
<%@ taglib uri = "struts-html.tld"   prefix="html" %>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Hashtable resul = (Hashtable) request.getAttribute("RESULTADO");
	
%>
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script language="JavaScript">
	<%if (resul!=null){%>
		var idpersona="<%=(resul.get("IDPERSONA")!=null?resul.get("IDPERSONA"):"")%>";
		var tipo="<%=resul.get("TIPOIDENT")%>";
		var nifcif="<%=resul.get("NIFCIF")%>";
		var nombre="<%=resul.get("NOMBRE")%>";
		var apellidos1="<%=resul.get("APELLIDOS1")%>";
		var apellidos2="<%=resul.get("APELLIDOS2")%>";
	<%}%>
	function refrescar() {
			window.parent.parent.DatosRegistralesForm.nombre.disabled=false;
			window.parent.parent.DatosRegistralesForm.apellido1.disabled=false;
			window.parent.parent.DatosRegistralesForm.apellido2.disabled=false;
			window.parent.parent.document.getElementById("tipoIdentificacion").disabled=false;
			if (idpersona != ""){
				if (nombre!=null){
					
					window.parent.parent.DatosRegistralesForm.nombre.value=nombre;
					window.parent.parent.DatosRegistralesForm.nombre.disabled=true;
				
				}
				if (apellidos1!=null){
					window.parent.parent.DatosRegistralesForm.apellido1.value=apellidos1;
					window.parent.parent.DatosRegistralesForm.apellido1.disabled=true;
				}
				if (apellidos2!=null){
					window.parent.parent.DatosRegistralesForm.apellido2.value=apellidos2;
					window.parent.parent.DatosRegistralesForm.apellido2.disabled=true;
				}
				if (nifcif!=null){
					window.parent.parent.DatosRegistralesForm.numIdentificacion.value=nifcif;
				}
				
				lista_tipo = window.parent.parent.document.getElementById("tipoIdentificacion").options;
					for (i = 0; i < lista_tipo.length; i++) {
						if (lista_tipo.options[i].value == tipo ) {
							lista_tipo.options[i].selected = true;
							break;
						}
				
					}
				window.parent.parent.document.getElementById("tipoIdentificacion").disabled=true;
				window.parent.parent.document.getElementById("IdPersonaNotario").value=idpersona
			}else{
				window.parent.parent.DatosRegistralesForm.idPersonaNotario.value="";
				window.parent.parent.DatosRegistralesForm.nombre.value="";
				window.parent.parent.DatosRegistralesForm.apellido1.value="";
				window.parent.parent.DatosRegistralesForm.apellido2.value="";
				window.parent.parent.DatosRegistralesForm.tipoIdentificacion.value="";
			}		
		
	}
	
	
	</script>
</head>

<body onload="refrescar();">
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>