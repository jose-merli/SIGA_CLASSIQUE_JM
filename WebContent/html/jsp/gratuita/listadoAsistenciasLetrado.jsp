<!-- listadoAsistenciasLetrado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<% 

	UsrBean usr=(UsrBean)session.getAttribute("USRBEAN");
	String letrado = ((String)request.getAttribute("letrado")==null)?"":(String)request.getAttribute("letrado");
	//String modoPestana = (String)request.getAttribute("MODOPESTANA");
	
	String anio = UtilidadesBDAdm.getYearBD("");
%>

<html>

<!-- HEAD -->
<head>

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script
	src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>"
	type="text/javascript"></script>
<script src="<html:rewrite page="/html/js/validacionStruts.js"/>"
	type="text/javascript"></script>
<script src="<html:rewrite page="/html/js/validation.js"/>"
	type="text/javascript"></script>


<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:TituloExt titulo="censo.fichaCliente.sjcs.asistencia.titulo"
	localizacion="censo.fichaCliente.sjcs.asistencia.localizacion" />

</head>

<body onLoad="ajusteAlto('resultado');buscar();">

	<html:form action="/JGR_AsistenciasLetrado.do" method="POST"
		target="resultado">

		<html:hidden property="modo" />
		<html:hidden property="letrado" value="<%=letrado%>" styleId="letrado"/>
		<table width="100%" border="0">
			<tr>
				<td>
					<siga:ConjCampos>
						<table>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.listadoAsistencias.literal.anio" />
								</td>
								<td>
									<input name="anio" id="anio" type="text" size="4" maxlength="4" class="box" value="<%=anio%>" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>

	<br>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		function buscar () {
			if ( !validarObjetoAnio(document.getElementById("anio")) ){
				alert("<siga:Idioma key="fecha.error.anio"/>");
				return;
			}
			
			sub();
			document.AsistenciasForm.modo.value = "buscarInit";
			document.AsistenciasForm.submit();
		}
		
		function buscarPaginador() {
			document.forms[1].target			= "resultado";
			document.forms[1].modo.value 		= "buscarPor";
			document.forms[1].submit();
		}
	
	    function refrescarLocal() {
			buscar();
		}
	</script>

	<siga:ConjBotonesBusqueda botones="B" titulo="" />
	<iframe align="center"
		src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
		id="resultado" name="resultado" scrolling="no" frameborder="0"
		marginheight="0" marginwidth="0" ;					 
					class="frameGeneral"></iframe>

	<iframe name="submitArea"
		src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
		style="display: none"></iframe>

</body>

</html>