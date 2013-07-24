<!DOCTYPE html>
<html>
<head>
<!-- busquedaIRPF.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 	
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
%>



	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo titulo="gratuita.insertarRetencion.literal.retencionesIrpf" localizacion="sjcs.maestros.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->	
	</head>

	<body onload="ajusteAlto('resultado');">

		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/SolicitudRetencioAction.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="actionModal" value="">
	  		
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.retenciones.descripcion"/>
						</td>
						<td class="labelText">
							<html:text name="SolicitudRetencionForm" property="descripcion" size="50" maxlength="60" styleClass="box" value="" />
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.retenciones.retencion"/>
						</td>
						<td class="labelText">
							<html:text name="SolicitudRetencionForm" property="retencion" size="5" maxlength="5" styleClass="boxNumber" value="" />
						</td>
						
						<td class="labelText">
							<!--siga:Idioma key="gratuita.retenciones.letraNifSociedad"/-->
							<siga:Idioma key="gratuita.retenciones.tipoSociedad"/>
						</td>
						<td class="labelText">
							<siga:ComboBD nombre="letraNifSociedad" tipo="cmbTipoSociedadAlta" clase="boxCombo" ancho="250"/>
						</td>
					</tr>	
				</html:form>	
			</table>
		</fieldset>	
		<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
		<!-- INICIO: BOTONES BUSQUEDA -->		
		<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.retenciones.consultaRetenciones" />	
		<!-- FIN: BOTONES BUSQUEDA -->
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			// Funcion asociada a boton buscar
			function buscar() {
				document.forms[0].descripcion.value = trim(document.forms[0].descripcion.value);
				document.forms[0].retencion.value = trim(document.forms[0].retencion.value);
			
				var nif = document.forms[0].letraNifSociedad.value;
				var retencion = document.forms[0].retencion.value;
				if (isNaN(nif) || (nif == 0)) {
					if (isNaN(retencion)) {
						alert('<siga:Idioma key="gratuita.formularioInicial.message.errorIRPF"/>');
						
					} else {
						document.forms[0].modo.value = "buscar";
						document.forms[0].submit();
					}
					
				} else { 
					alert("Error nif");
				}
			}
		
			// Funcion asociada a boton Nuevo
			function nuevo() {		
				document.forms[0].modo.value = "nuevo";
				var resultado=ventaModalGeneral(document.forms[0].name,"P");
				if(resultado=='MODIFICADO') {
					buscar();
				}
			}			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->		
		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
			id="resultado"
			name="resultado" 
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0";					 
			class="frameGeneral">
		</iframe>
		<!-- FIN: IFRAME LISTA RESULTADOS -->
			
		<!-- INICIO: SUBMIT AREA -->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>