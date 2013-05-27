<!-- modalAnularValidacionCabeceraGuardia.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

</head>

<body >

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				Anulacíon Validacion Cabecera Guardia
			</td>
		</tr>
	</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:javascript formName="DefinirCalendarioGuardiaForm" staticJavascript="false" />  
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" styleId="DefinirCalendarioGuardiaForm">
		<html:hidden property = "modo" 					styleId = "modo" />
		<html:hidden property = "idInstitucion"			styleId = "idInstitucion" />
		<html:hidden property = "idTurno"				styleId = "idTurno" />
		<html:hidden property = "idGuardia"				styleId = "idGuardia" />
		<html:hidden property = "idCalendarioGuardias"	styleId = "idCalendarioGuardias" />		
		<html:hidden property = "fechaInicio" 			styleId = "fechaInicio" />
		<html:hidden property = "idPersona"				styleId = "idPersona" />
			
		<!-- INICIO: CAMPOS DEL REGISTRO -->
		<table class="tablaCentralCamposPeque" align="center" border="0">
		<tr>
			<td>
				<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.motivosSolicitud">
					<table class="tablaCampos" align="left" border="0" width="100%">
						<tr>
							<td class="labelText">
				 				<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos"/> 			
							</td>
							<td >
								<html:textarea name="DefinirCalendarioGuardiaForm" property="comenAnulacion" styleId="comenAnulacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="50" rows="12" style="width:350"  styleClass="box" readOnly="false" ></html:textarea>
							</td>	
						</tr>
					</table>
				</siga:ConjCampos>	
			</td>		
		  </tr>								
		</table>	
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="Y,C" modal="P"/>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->

	<script language="JavaScript">
		
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {		

			document.DefinirCalendarioGuardiaForm.comenAnulacion.value = document.getElementById("comenAnulacion").value;
			document.DefinirCalendarioGuardiaForm.modo.value = "realizarAnulacion";
			document.DefinirCalendarioGuardiaForm.target = "submitArea";							
			document.DefinirCalendarioGuardiaForm.submit();	
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}
		
		function refrescarLocal() {		
			top.cierraConParametros("NORMAL");
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>	
	<!-- FIN: SUBMIT AREA -->

</body>
</html>