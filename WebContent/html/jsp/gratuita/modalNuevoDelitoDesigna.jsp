<!-- modalNuevoDelitoEJG.jsp-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri ="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoDesignaForm"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	PestanaDelitoDesignaForm formulario = (PestanaDelitoDesignaForm)request.getAttribute("pestanaDelitoDesignaForm");
	String anio = formulario.getAnio().toString();
	String numero = formulario.getNumero().toString();
	String idTurno = formulario.getIdTurno().toString();
	String parametro[] = {usr.getLocation(), usr.getLocation(), numero, anio, idTurno};
%>

<html>

<!-- HEAD -->
<head>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="pestanaDelitoDesignaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.delito"/>
	</td>
</tr>
</table>
	
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DelitosDesignas.do" method="post">
		<html:hidden property = "modo" value = ""/>
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoDesignaForm" property="anio" />
		<html:hidden name="pestanaDelitoDesignaForm" property="numero" />
		<html:hidden name="pestanaDelitoDesignaForm" property="idTurno" />

	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
		<td>		
		<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.delito">
			<table class="tablaCampos" align="center">
			<tr>
				<td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.delito"/>&nbsp;(*)
				</td>
				<td>
						<siga:ComboBD nombre="idDelito" tipo="comboDelitosDesignas" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=parametro%>" />
				</td>
			</tr>
			</table>
		</siga:ConjCampos>		
		</td>
	</tr>
	</table>
	</html:form>			
	
		<siga:ConjBotonesAccion botones="Y,C" modal="P"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
				sub();
				if (validatePestanaDelitoDesignaForm(document.pestanaDelitoDesignaForm)) {
						document.forms[0].modo.value = "insertar";
						document.forms[0].target = "submitArea";							
						document.forms[0].submit();	
						window.top.returnValue="MODIFICADO";
				}else{
				
					fin();
					return false;
				
				}
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>