<!-- insertarTelefono.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");	
	
	String idPersona =  miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA).toString();
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.telefonos"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_PestanaSOJBeneficiarios" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idTelefono" value = ""/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>

	<tr>		
	<td>	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.operarDatosBeneficiario.literal.telefonos">
	<table class="tablaCampos" align="center">
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.telefonoUso"/>
	</td>				
	<td>
		<html:text name="DefinirPersonaJGForm" property="nombreTelefono" size="20" styleClass="box" value="" ></html:text>
	</td>
	<tr>
	</tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.numeroTelefono"/>
	</td>				
	<td>
		<html:text name="DefinirPersonaJGForm" property="numeroTelefono" size="20" styleClass="box" value=""></html:text>
	</td>	
	</tr>
	</table>
	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			if (isNaN(document.forms[0].numeroTelefono.value)) {			
				alert('<siga:Idioma key="gratuita.insertarTelefono.literal.errorNumericoTelefono"/>');
			}
			else if (document.forms[0].numeroTelefono.value == "") {
				alert('<siga:Idioma key="gratuita.insertarTelefono.literal.errorTelefono"/>');
			}
			else if (document.forms[0].nombreTelefono.value == "") {
				alert('<siga:Idioma key="gratuita.insertarTelefono.literal.errorUsoTelefono"/>');
			}	
			else {
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "Insertar";
	        	document.forms[0].submit();
			}			
		}				
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>