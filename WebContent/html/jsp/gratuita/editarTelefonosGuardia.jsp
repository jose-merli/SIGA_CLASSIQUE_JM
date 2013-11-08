<!DOCTYPE html>
<html>
<head>
<!-- editarTelefonosGuardia.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>





<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>"
	type="text/javascript"></script>
<html:javascript formName="InscripcionTGForm" staticJavascript="false" />

</head>

<body>
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<c:if test="${InscripcionTGForm.modo=='sitInsertar'}">
					<siga:Idioma
						key="gratuita.gestionInscripciones.paso.inscripcion.turno.contacto" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='smitInsertar'}">
					<siga:Idioma
						key="gratuita.gestionInscripciones.paso.inscripcion.turno.masiva.contacto" />
				</c:if>
			</td>
		</tr>
	</table>

	<html:form action="/JGR_AltaTurnosGuardias.do" method="post"
		target="_self">
		<html:hidden property="modo" />
		<html:hidden property="idDireccion" />

		<table class="tablaCentralCamposGrande" align="center" border="0">
			<tr>
				<td>
					<siga:ConjCampos
						leyenda="gratuita.editartelefonosGuardia.literal.telefonosGuardia">
						<table class="tablaCampos" align="center">

							<tr>
								<td class="labelText"><siga:Idioma
										key="censo.datosDireccion.literal.telefono1" />&nbsp;(*)</td>
								<td><html:text name="InscripcionTGForm"
										property="telefono1" maxlength="20" size="10" styleClass="box"></html:text></td>

								<td class="labelText"><siga:Idioma
										key="censo.datosDireccion.literal.telefono2" />&nbsp;</td>
								<td><html:text name="InscripcionTGForm"
										property="telefono2" maxlength="20" size="10" styleClass="box"></html:text></td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma
										key="censo.datosDireccion.literal.movil" />&nbsp;</td>
								<td><html:text name="InscripcionTGForm" property="movil"
										maxlength="20" size="10" styleClass="box"></html:text></td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma
										key="censo.datosDireccion.literal.fax1" />&nbsp;</td>
								<td><html:text name="InscripcionTGForm" property="fax1"
										maxlength="20" size="10" styleClass="box"></html:text></td>

								<td class="labelText"><siga:Idioma
										key="censo.datosDireccion.literal.fax2" />&nbsp;</td>
								<td><html:text name="InscripcionTGForm" property="fax2"
										maxlength="20" size="10" styleClass="box"></html:text></td>
							</tr>
							
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>

	<siga:ConjBotonesAccion botones="X,F" ordenar="false" />

	<!-- ******* BOTONES DE ACCIONES ****** -->

	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() {		
			top.volver();
		}

		function accionCancelar() {		
			window.top.close();
		}
		
		//Asociada al boton Finalizar -->
		function accionFinalizar() {		
			sub();
			
			document.InscripcionTGForm.telefono1.value=eliminarBlancos(trim(document.InscripcionTGForm.telefono1.value));
		  	document.InscripcionTGForm.telefono2.value=eliminarBlancos(trim(document.InscripcionTGForm.telefono2.value));
		  	document.InscripcionTGForm.fax1.value=eliminarBlancos(trim(document.InscripcionTGForm.fax1.value));
		  	document.InscripcionTGForm.fax2.value=eliminarBlancos(trim(document.InscripcionTGForm.fax2.value));
		  	document.InscripcionTGForm.movil.value=eliminarBlancos(trim(document.InscripcionTGForm.movil.value));
			if (!validateInscripcionTGForm(document.InscripcionTGForm)) { 
			  	fin();
				return false;
			}else{  
				document.InscripcionTGForm.target="submitArea";
				document.InscripcionTGForm.submit();
				window.top.returnValue="INSERTADO";			
			}
		}
		
		function refrescarLocal(){
			fin();
		}

	</script>

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea"
		src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
