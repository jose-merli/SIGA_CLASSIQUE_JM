<!DOCTYPE html>
<html>
<head>
<!--editarProgrCalendarios.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<%
	String modo = (String) request.getSession().getAttribute("Modo");
%>

<!-- HEAD -->
	<c:if test="${ProgrCalendariosFormEdicion.modo=='consultarProgrCalendarios'}">
		<script type="text/javascript">
			var modo = "VER";
		</script>
	</c:if>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	
	<script type="text/javascript">
		function onChangeConjuntoGuardia (){
			document.getElementById('idBuscarConfConjuntoGuardias').onclick();
		}
		
		function preAccionBuscarConfConjuntoGuardias (){
			sub();
		}
		
		function postAccionBuscarConfConjuntoGuardias (){
			fin();
		}
		
		function preAccionBuscarHcoProgramacion (){
			sub();
		}
		
		function postAccionBuscarHcoProgramacion (){
			fin();
		}
		
		function inicioHistorico (){
			document.getElementById('idBuscarHcoProgramacion').onclick();
		}
		
		function refrescarLocal() {			
			inicioHistorico();						
		}
		
		function descargaLog(fila) {
		    var idTurno = document.getElementById("idTurno_"+fila).value;
			var idGuardia = document.getElementById("idGuardia_"+fila).value;
			var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
			document.CalendarioGuardiasForm.idTurno.value = idTurno;
			document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
			document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
			document.CalendarioGuardiasForm.accion.value = 'descargaDesdeProgramacion';
			document.CalendarioGuardiasForm.target="submitArea";
			document.CalendarioGuardiasForm.modo.value="descargarLog";
			document.CalendarioGuardiasForm.submit();
		}
	</script>
</head>

<body onload="inicio();">

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma key="gratuita.calendarios.programacion.modal.edicion"/>
			</td>
		</tr>
	</table>

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	
	<html:form action="${path}" >
		<html:hidden property="modo" />
	</html:form>

	<html:form action="${path}" name="ProgrCalendariosFormEdicion" type="com.siga.gratuita.form.ProgrCalendariosForm" target="submitArea">
		<html:hidden property="modo" value="${ProgrCalendariosFormEdicion.modo}"/>
		<html:hidden property="idProgrCalendario" value="${ProgrCalendariosFormEdicion.idProgrCalendario}"/>
		<input type="hidden" name="actionModal" />

		<table width="100%" border="0">
			<tr>
				<td width="25%"></td>
				<td width="25%"></td>
				<td width="25%"></td>
				<td width="25%"></td>			
			</tr>
	
			<tr>
				<c:choose>
					<c:when test="${ProgrCalendariosFormEdicion.modo!='consultarProgrCalendarios'}">
						<td class="labelText"  style="align:left"><siga:Idioma key='gratuita.calendarios.programacion.fechaProgramada'/></td>
						<td>
							<siga:Fecha nombreCampo="fechaProgramacion" valorInicial="${ProgrCalendariosFormEdicion.fechaProgramacion}"/>						
							<html:text property="horaProgramacion" size="2" maxlength="2" styleClass="box" value="${ProgrCalendariosFormEdicion.horaProgramacion}" style="text-align:center"/>					
							:
							<html:text property="minutoProgramacion"  size="2" maxlength="2" styleClass="box" value="${ProgrCalendariosFormEdicion.minutoProgramacion}" style="text-align:center"/>	
						</td>
					</c:when>
					
					<c:otherwise>
						<td class="labelText"><siga:Idioma key='gratuita.calendarios.programacion.fechaProgramada'/></td>
						<td>
							<siga:Fecha nombreCampo="fechaProgramacion" valorInicial="${ProgrCalendariosFormEdicion.fechaProgramacion}" readOnly="true" disabled="true"/>
							<html:text property="horaProgramacion" size="2" maxlength="2" styleClass="boxConsulta" readonly="true" value="${ProgrCalendariosFormEdicion.horaProgramacion}" style="text-align:center"/>					
							:
							<html:text property="minutoProgramacion" size="2" maxlength="2" styleClass="boxConsulta" readonly="true" value="${ProgrCalendariosFormEdicion.minutoProgramacion}" style="text-align:center"/>
						</td>
						<td></td>
						<td ></td>
					</c:otherwise>
				</c:choose>
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key='gratuita.calendarios.programacion.conjuntoGuardias'/></td>
				<td>
					<c:choose>
						<c:when test="${ProgrCalendariosFormEdicion.modo=='consultarProgrCalendarios'}">					
							<html:select styleClass="boxConsulta" style="width:320px;" readOnly="true" 
								property="idConjuntoGuardia" id="idConjuntoGuardia"  onchange="onChangeConjuntoGuardia();" value="${ProgrCalendariosFormEdicion.idConjuntoGuardia}" disabled="true">
								<html:option value=""><siga:Idioma key="general.combo.seleccionar"/></html:option>
								<html:optionsCollection name="ConjuntoGuardiasForms" value="idConjuntoGuardia" label="descripcion" />
							</html:select>
						</c:when>
						
						<c:otherwise>
							<html:select styleClass="boxCombo" style="width:320px;" 
								property="idConjuntoGuardia" id="idConjuntoGuardia"  onchange="onChangeConjuntoGuardia();" value="${ProgrCalendariosFormEdicion.idConjuntoGuardia}" >
								<html:option value=""><siga:Idioma key="general.combo.seleccionar"/></html:option>
								<html:optionsCollection name="ConjuntoGuardiasForms" value="idConjuntoGuardia" label="descripcion" />
							</html:select>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
			<tr>	
				<c:choose>
					<c:when test="${ProgrCalendariosFormEdicion.modo!='consultarProgrCalendarios'}">
						<td class="labelText"  style="align:left">
							<siga:Idioma key="gratuita.calendarios.programacion.fechaCalendario"/>
							<siga:Idioma key='general.literal.desde'/>
						</td>
						<td>
							<table>
								<tr>															
									<td><siga:Fecha nombreCampo="fechaCalInicio" valorInicial="${ProgrCalendariosFormEdicion.fechaCalInicio}"/></td>
									<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>
									<td><siga:Fecha nombreCampo="fechaCalFin" valorInicial="${ProgrCalendariosFormEdicion.fechaCalFin}"/></td>
								</tr>
							</table>
						</td>
					</c:when>
					
					<c:otherwise>
						<td class="labelText">
							<siga:Idioma key="gratuita.calendarios.programacion.fechaCalendario"/>
							<siga:Idioma key='general.literal.desde'/>
						</td>
						<td>
							<table>
								<tr>
									<td><html:text property="fechaCalInicio" size="10" readonly="true" styleClass="boxConsulta" value="${ProgrCalendariosFormEdicion.fechaCalInicio}"/></td>
									<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>
									<td><html:text property="fechaCalFin" size="10" readonly="true" styleClass="boxConsulta" value="${ProgrCalendariosFormEdicion.fechaCalFin}"/></td>
								</tr>
							</table>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>		
		</table>
	
		<c:choose>
			<c:when test="${ProgrCalendariosFormEdicion.modo=='consultarProgrCalendarios'}">
				<siga:ConjBotonesAccion botones="C" modal="P" />
				<div id="divHcoProgramacion" style='position:relative;height:100%;width:100%;'>
					<table id='hcoProgramacion' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
					</table>
				</div>
			</c:when>
			
			<c:otherwise>
				<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
				<div id="divConfConjuntoGuardias" style='position:relative;height:100%;width:100%;'>
					<table id='confConjuntoGuardias' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</html:form>
		
	<html:form action="/JGR_ProgrCalendariosConf" method="POST" target="_self"  style="display:none">
		<html:hidden property="modo"/>
		<html:hidden property="idConjuntoGuardia"/>
		<html:hidden property="mostrarSoloGuardiasConfiguradas" value="true"/>	

		<table class="botonesSeguido" align="center">
			<tr>
				<td class="titulitos"></td>
				<td class="tdBotones"><input type='button' id='idBuscarConfConjuntoGuardias' name='idButton' style="display:none" value='Buscar' alt='Buscar' class='busquedaConfConjuntoGuardias'></td>
			</tr>
		</table>
	</html:form>

	<ajax:htmlContent
		baseUrl="/SIGA/JGR_ProgrCalendariosConf.do?modo=getAjaxBusquedaConfConjuntoGuardiasProgr"
		source="idBuscarConfConjuntoGuardias"
		target="divConfConjuntoGuardias"
		preFunction="preAccionBuscarConfConjuntoGuardias"
		postFunction="postAccionBuscarConfConjuntoGuardias"
		parameters="idConjuntoGuardia={idConjuntoGuardia},mostrarSoloGuardiasConfiguradas={mostrarSoloGuardiasConfiguradas}"/>
		
	<html:form action="/JGR_ProgrCalendariosProgr" method="POST" target="submitArea" type="" style="display:none">
		<html:hidden property="modo"/>
		<html:hidden property="idProgrCalendario"/>

		<table class="botonesSeguido" align="center">
			<tr>
				<td class="titulitos"></td>
				<td class="tdBotones"><input type='button'  id = 'idBuscarHcoProgramacion' name='idButton' style="display:none" value='Buscar' alt='Buscar' class='busquedaHcoProgramacion'></td>
			</tr>
		</table>
	</html:form>

	<ajax:htmlContent
		baseUrl="/SIGA/JGR_ProgrCalendariosProgr.do?modo=getAjaxBusquedaHcoProgramacion"
		source="idBuscarHcoProgramacion"
		target="divHcoProgramacion"
		preFunction="preAccionBuscarHcoProgramacion"
		postFunction="postAccionBuscarHcoProgramacion"
		parameters="idProgrCalendario={idProgrCalendario}"/>

	<html:form action="/JGR_HcoProgrCalendarios" method="POST" target="submitArea" type="" style="display:none">
		<html:hidden property="modo"/>
		<html:hidden property="idProgrCalendario"/>
		<html:hidden property="idConjuntoGuardia"/>
		<html:hidden property="idTurno"/>
		<html:hidden property="idGuardia"/>
		<html:hidden property="idCalendarioGuardias"/>
	</html:form>
	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" name="CalendarioGuardiasForm" type="com.siga.gratuita.form.DefinirCalendarioGuardiaForm" method="post" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idTurno" value = ""/>
		<html:hidden property = "idGuardia" value = ""/>
		<html:hidden property = "idCalendarioGuardias" value = ""/>
		<html:hidden property = "accion" value = ""/>
		<html:hidden property = "modoPestanha" value = ""/>
		<input type="hidden" name="actionModal" value="">
	</html:form>
		
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

	<script type="text/javascript">

		function inicio() {
			if(ProgrCalendariosFormEdicion.modo.value == 'consultarProgrCalendarios'){
				document.getElementById("titulo").innerHTML = '<siga:Idioma key="gratuita.calendarios.programacion.modal.consulta"/>';
				jQuery("#idConjuntoGuardia").attr("disabled","disabled");
	
				inicioHistorico();
				
			}else{
				if(ProgrCalendariosFormEdicion.modo.value == 'editarProgrCalendarios'){
					document.getElementById("titulo").innerHTML = '<siga:Idioma key="gratuita.calendarios.programacion.modal.edicion"/>';
				}else if(ProgrCalendariosFormEdicion.modo.value == 'insertarProgrCalendarios'){
					document.getElementById("titulo").innerHTML = '<siga:Idioma key="gratuita.calendarios.programacion.modal.nueva"/>';
	
				}
				onChangeConjuntoGuardia();
			}
		}
		
		function accionGuardarCerrar() {
			var error = '';
			if(document.ProgrCalendariosFormEdicion.fechaProgramacion.value=='')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.fechaProgramada'/>"+ '\n';
			if(document.ProgrCalendariosFormEdicion.horaProgramacion.value=='')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.horaProgramada'/>"+ '\n';
			if(document.ProgrCalendariosFormEdicion.minutoProgramacion.value=='')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.minutiProgramada'/>"+ '\n';
			if(document.ProgrCalendariosFormEdicion.idConjuntoGuardia.value=='')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.conjuntoGuardias'/>"+ '\n';
			if(document.ProgrCalendariosFormEdicion.fechaCalInicio.value=='' ||document.ProgrCalendariosFormEdicion.fechaCalFin.value=='')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.fechaCalendario'/>"+ '\n';
			if(error !=''){
				alert(error);
			 	return false;
			}
			
			//modificaProgrCalendarios
			//insertarProgrCalendarios
			sub();
			document.ProgrCalendariosFormEdicion.submit();
		}
	
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}
	
		function accionRestablecer() {
			document.ProgrCalendariosFormEdicion.reset();
			onChangeConjuntoGuardia();
		}
		
		//Este es el borrar del hijo
		function borrar(fila) {
			borrarHcoProgramacion(fila);
		}
		
		function borrarHcoProgramacion(fila) {
			sub();
			var idTurno = document.getElementById("idTurno_"+fila).value;
			var idGuardia = document.getElementById("idGuardia_"+fila).value;
			var idConjuntoGuardia = document.getElementById("idConjuntoGuardia_"+fila).value;
			document.HcoConfProgrCalendarioForm.idProgrCalendario.value = document.ProgrCalendariosFormEdicion.idProgrCalendario.value;
			document.HcoConfProgrCalendarioForm.idTurno.value = idTurno;
			document.HcoConfProgrCalendarioForm.idGuardia.value = idGuardia;
			document.HcoConfProgrCalendarioForm.idConjuntoGuardia.value = idConjuntoGuardia;
			document.HcoConfProgrCalendarioForm.modo.value="borrarHcoProgramacion";
			document.HcoConfProgrCalendarioForm.submit();
		} 
		
		function descargaLog(fila) {
		    var idTurno = document.getElementById("idTurno_"+fila).value;
			var idGuardia = document.getElementById("idGuardia_"+fila).value;
			var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
			document.CalendarioGuardiasForm.idTurno.value = idTurno;
			document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
			document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
			document.CalendarioGuardiasForm.accion.value = 'descargaDesdeProgramacion';
			document.CalendarioGuardiasForm.target="submitArea";
			document.CalendarioGuardiasForm.modo.value="descargarLog";
			document.CalendarioGuardiasForm.submit();
		}
	</script>
</body>
</html>