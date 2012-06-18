<!-- insertarTelefono.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<html:javascript formName="DefinirTelefonosJGForm" staticJavascript="false" />  
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
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
	
	<html:form action="/JGR_TelefonosPersonasJG" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "accion" />
		<html:hidden property = "idInstitucion" />

		<tr>
			<td><!-- SUBCONJUNTO DE DATOS --> <siga:ConjCampos
				leyenda="gratuita.operarDatosBeneficiario.literal.telefonos">
				<table class="tablaCampos" align="center">
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.operarDatosBeneficiario.literal.telefonoUso" />
						</td>
						<td><html:text name="DefinirTelefonosJGForm"
							property="nombreTelefonoJG" size="20" styleClass="box" value=""></html:text>
						</td>
					</tr>
					<tr>

						<td class="labelText"><siga:Idioma
							key="gratuita.operarDatosBeneficiario.literal.numeroTelefono" />&nbsp;(*)
						</td>
						<td><html:text name="DefinirTelefonosJGForm"
							property="numeroTelefonoJG" size="20" styleClass="box" value=""></html:text>
						</td>
					</tr>
				</table>
			</siga:ConjCampos></td>
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
	
		//Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}	
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			sub();
			document.DefinirTelefonosJGForm.numeroTelefonoJG.value=eliminarBlancos(trim(document.DefinirTelefonosJGForm.numeroTelefonoJG.value));
			if (!validateDefinirTelefonosJGForm(document.DefinirTelefonosJGForm)){
				fin();
			  	return false;
			}else{ 
						document.forms[0].target = "submitArea";
						document.forms[0].modo.value = "Insertar";
			        	document.forms[0].submit();
			}			
					
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