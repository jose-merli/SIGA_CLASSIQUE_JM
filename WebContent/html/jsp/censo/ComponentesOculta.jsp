<!DOCTYPE html>
<html>
<head>
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
	
	Hashtable resul = (Hashtable) request.getAttribute("RESULTADO");
	
	String nombre = null; 
	String apellidos1 = null;
	String apellidos2 = null;
	String mensajeTipo4 = null;
	String sEstadoColegial = "true";
	if (resul!=null){
		nombre = (String) resul.get("NOMBRE");
		apellidos1 = (String) resul.get("APELLIDOS1");
		apellidos2 = (String) resul.get("APELLIDOS2");	
		String[] datos = {nombre,apellidos1,apellidos2};
		mensajeTipo4 = UtilidadesString.getMensajeIdioma(usr, "messages.censo.componentes.insertaNocolegiadoOtroColegio", datos);
	    sEstadoColegial=(String) resul.get("ESTADO");
		if (Integer.toString(ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL).equals(sEstadoColegial)  ){ 
			sEstadoColegial="false";
		}
		else{
			sEstadoColegial="true";
		}
	}
	
%>



<!-- HEAD -->

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script language="JavaScript">
	<%if (resul!=null){%>
		var idpersona="<%=(resul.get("IDPERSONA")!=null?resul.get("IDPERSONA"):"")%>";
		var tipo="<%=resul.get("TIPO")%>";
		var nifcif="<%=resul.get("NIFCIF")%>";
		var idinstitucion="<%=resul.get("IDINSTITUCION")%>";
		var nombre="<%=nombre%>";
		var apellidos1="<%=apellidos1%>";
		var apellidos2="<%=apellidos2%>";
		var ncolegiado="<%=resul.get("NCOLEGIADO")%>";	
	<%} else{%>
		var idpersona="";
	<%}%>
	function refrescar() {

			window.parent.document.getElementById("colegiado").style.display="none";
			window.parent.document.getElementById("sjcs").style.display="none";
			window.parent.document.getElementById("colegio1").style.display="none";
			window.parent.document.getElementById("colegio2").style.display="none";
			window.parent.document.getElementById("profesional").checked=false;
			window.parent.componentesJuridicosForm.idTipoColegio.value="";
			window.parent.document.getElementById("numColegiado").value="";
			window.parent.componentesJuridicosForm.nombre.disabled=false;
			window.parent.componentesJuridicosForm.apellidos1.disabled=false;
			window.parent.componentesJuridicosForm.apellidos2.disabled=false;	
			
			<%if (sEstadoColegial.equals("false")){%>
				if ("0" == confirm("<siga:Idioma key="messages.censo.componentes.colegiadoBaja"/>") ){
					window.parent.componentesJuridicosForm.nombre.value=""
					window.parent.componentesJuridicosForm.apellidos1.value=""
					window.parent.componentesJuridicosForm.apellidos2.value=""
					return;
				}
			<%}%>
					
			if (idpersona != ""){
				
				if (tipo=="4"){
					window.parent.componentesJuridicosForm.nombre.disabled=true;
					window.parent.componentesJuridicosForm.apellidos1.disabled=true;
					window.parent.componentesJuridicosForm.apellidos2.disabled=true;
					alert("<%=mensajeTipo4%>")
				}

				window.parent.componentesJuridicosForm.clienteIdPersona.value=idpersona;
				
				if (nombre!=null){
					window.parent.componentesJuridicosForm.nombre.value=nombre;
				}
				if (apellidos1!=null){
					window.parent.componentesJuridicosForm.apellidos1.value=apellidos1;
				}
				if (apellidos2!=null){
					window.parent.componentesJuridicosForm.apellidos2.value=apellidos2;
				}
				if (nifcif!=null){
					window.parent.componentesJuridicosForm.nifcif.value=nifcif;
				}
				if (tipo=="1" || tipo=="2"){
					window.parent.componentesJuridicosForm.idProvincia.value="";
					window.parent.document.getElementById("provincia1").style.display="none";
					window.parent.document.getElementById("provincia2").style.display="none";
					window.parent.document.getElementById("colegiado").style.display="block";
					window.parent.document.getElementById("sjcs").style.display="block";
					window.parent.document.getElementById("colegio1").style.display="block";
					window.parent.document.getElementById("colegio2").style.display="block";
					window.parent.document.getElementById("profesional").checked=true;
					window.parent.document.getElementById("profesional").disabled=true;

					lista_tipo = window.parent.document.getElementById("idTipoColegio1").options;
					lista_tipo.options[0].selected = true;
					window.parent.componentesJuridicosForm.idTipoColegio.value=lista_tipo.options[0].value;

					lista_cole = window.parent.document.getElementById("clienteIdInstitucion").options;
					for (i = 0; i < lista_cole.length; i++) {
						if (lista_cole.options[i].value == idinstitucion ) {
							lista_cole.options[i].selected = true;
							break;
						}
				
					}
					
					window.parent.document.getElementById("numColegiado").value=ncolegiado;
					window.parent.document.getElementById("colegiadoabogacia").style.display="block";
					window.parent.document.getElementById("colegiadonoabogacia").style.display="none";
					window.parent.document.getElementById("asteriscoCuenta").style.display="none";
					window.parent.document.getElementById("sinasteriscoCuenta").style.display="block";
				}else{
					window.parent.document.getElementById("clienteIdInstitucion").value=idinstitucion;
				}
				
			}else{
				window.parent.componentesJuridicosForm.nombre.value="";
				window.parent.componentesJuridicosForm.apellidos1.value="";
				window.parent.componentesJuridicosForm.apellidos2.value="";
				window.parent.componentesJuridicosForm.clienteIdPersona.value="";
				window.parent.componentesJuridicosForm.idTipoColegio1.value="";
				window.parent.componentesJuridicosForm.idTipoColegio2.value="";
				window.parent.componentesJuridicosForm.idProvincia.value="";
				window.parent.document.getElementById("numColegiado").value="";
				
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