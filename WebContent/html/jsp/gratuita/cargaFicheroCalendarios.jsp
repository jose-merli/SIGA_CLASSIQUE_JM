<!DOCTYPE html>
<html>
<head>
<!-- cargaFicheroCalendarios.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>
 
<!-- IMPORTS -->
<!-- JSP -->
<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="sjcs.guardias.programacionCalendarios.cargaFicheroCalendarios" localizacion="sjcs.guardias.programacionCalendarios.localizacion"/>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>
 
<body>
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="/JGR_DefinirCalendarioGuardia.do"   method="POST" target="submitArea" enctype="multipart/form-data">
		<siga:ConjCampos leyenda="Datos Plantilla Fichero Modelo">
			<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaEJG.literal.turno" />&nbsp;(*)
					</td>					
					<td>
						<siga:Select id="idTurno" queryParamId="idturno" queryId="getTurnos" width="355" childrenIds="idGuardia" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaEJG.literal.guardia" />&nbsp;(*)
					</td>
					<td>
						<siga:Select id="idGuardia" queryId="getGuardiasDeTurno" width="355" parentQueryParamIds="idturno"/>
					</td>
				</tr>
				
				<tr>		
					<td class="tdBotones" colspan="4" align="center">
						<input type="button" alt="<siga:Idioma key="general.boton.descargaFicheroModelo"/>" id="idButton" onclick="return descargarModelo();" class="button" value="<siga:Idioma key="general.boton.descargaFicheroModelo"/>">
					</td>					
				</tr>
			</table>
		</siga:ConjCampos>
		
		 <siga:ConjCampos leyenda="Carga Fichero">
			<table align="left" cellpadding="0" cellpadding="0">
				<html:hidden property = "modo" value = ""/>
					<tr>
						<td class="labelText" colspan="2">
							<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.observaciones"/>:
						</td>
					</tr>
					
					<tr>
						<td class="labelText" colspan="5">
							<html:textarea name="DefinirCalendarioGuardiaForm" onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)" property="observaciones" cols="200" rows="5" style="overflow:auto" styleClass="boxCombo" value="" readonly="false" ></html:textarea>
						</td>
					</tr>						
					<tr>
						<td class="labelText" >
							<siga:Idioma key="pys.cargaProductos.literal.fichero"/>&nbsp;(*)
						</td>				
						<td colspan="3">
							<html:file name="DefinirCalendarioGuardiaForm"  property="ficheroCalendario" size="90" styleClass="box"></html:file>
						</td>
						
						<td class="tdBotones">
							<input type="button" alt="<siga:Idioma key="general.boton.cargarFichero"/>" id="idButton" onclick="return upload();" class="button" value="<siga:Idioma key="general.boton.cargarFichero"/>">
						</td>
					</tr>
			</table>
		</siga:ConjCampos>	
	</html:form>	
	
	<script language="JavaScript">	
		function upload() {
			sub();
			
			if(document.DefinirCalendarioGuardiaForm.ficheroCalendario.value==''){
				error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.archivo'/>";
				alert(error);
				fin();
				return;
			}
			
			if (!TestFileType(document.DefinirCalendarioGuardiaForm.ficheroCalendario.value, ['XLS'])){
				fin();
				return false;
			}
			
			document.DefinirCalendarioGuardiaForm.modo.value = "uploadFicheroCalendarios";
			document.DefinirCalendarioGuardiaForm.target = "mainWorkArea";
			document.DefinirCalendarioGuardiaForm.submit();
		}	
		
		function descargarModelo() {	
			if(document.DefinirCalendarioGuardiaForm.idTurno.selectedIndex < 1){
				error = "<siga:Idioma key='errors.required' arg0='gratuita.busquedaEJG.literal.turno'/>"+ '\n';
				alert(error);
				fin();
				return;
			}

			if(document.DefinirCalendarioGuardiaForm.idGuardia.selectedIndex < 1){
				error = "<siga:Idioma key='errors.required' arg0='gratuita.busquedaEJG.literal.guardia'/>"+ '\n';
				alert(error);
				fin();
				return;
			}			
			document.DefinirCalendarioGuardiaForm.modo.value = "descargaFicheroModelo";
			document.DefinirCalendarioGuardiaForm.submit();
		}	
		
		function accionVolver() {	
			document.ProgrCalendariosForm.modo.value = "abrirVolver";
			document.ProgrCalendariosForm.submit();
		}			
		
	</script>	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	<!-- Formularios auxiliares -->
	<html:form action="/JGR_ProgramacionCalendarios.do"  method="POST" target="mainWorkArea">
		<html:hidden property="modo" value="modo"/>
	</html:form>	
			
			
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />			
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>