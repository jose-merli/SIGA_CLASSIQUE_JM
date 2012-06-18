<!-- consultaTurnoInscripcion.jsp -->
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
<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
	<link rel="stylesheet" href="<html:rewrite page="/html/js/themes/base/jquery.ui.all.css"/>" />
		
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

	<script language="JavaScript" type="text/javascript">	
	
			//Asociada al boton Cancelar
			function accionCancelar() {	
				window.top.close();	
			}
			
			//Asociada al boton Cancelar
			function accionCerrar() {		
				window.top.close();
			}
	
		//Asociada al boton Restablecer
			function accionRestablecer() {		
				document.InscripcionTGForm.reset();
			}
			
			function comprobarEstado() {				
				if(document.InscripcionTGForm.estadoPendientes &&document.InscripcionTGForm.estadoPendientes.value!=''){
					if(!confirm(document.InscripcionTGForm.estadoPendientes.value)) {
						accionCerrar();
					}
				}
				return true;		
			}
			//Asociada al boton Siguiente
			function accionSiguiente() {	
				sub();
				document.InscripcionTGForm.target="_self";
				document.InscripcionTGForm.submit();
			}
	
	</script>

</head>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<body onload=comprobarEstado();>
<html:form action="${path}" method="POST"
			target="_top">
<html:hidden property="estadoPendientes"/>

<table width="100%">
	<tr>
		<td>

		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
				<c:if test="${InscripcionTGForm.modo=='sitConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.solicitud.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='sbtConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.solicitudBaja.descripcionTurno" />
				</c:if>
				
				<c:if test="${InscripcionTGForm.modo=='sigConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitud.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='sbgConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitudBaja.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='vitConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.validar.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='vbtConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.validarBaja.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='vigConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.validar.descripcionTurno" />
				</c:if>
				<c:if test="${InscripcionTGForm.modo=='vbgConsultaGuardias'}">
					<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.validarBaja.descripcionTurno" />
				</c:if>
				
				</td>
			</tr>			
		</table>
		</td>
	</tr>

	<tr>
		<td>
			
			<html:hidden property="modo"/>
			<bean:define id="definirTurnosForm" name="InscripcionTGForm"
				property="inscripcionTurno.turno.definirTurnosForm"
				type="com.siga.gratuita.form.DefinirTurnosForm" />

			<siga:ConjCampos
				leyenda="gratuita.maestroTurnos.literal.datosGenerales">

				<table width="100%" border="0">
					<tr>

						<td width="20%"></td>
						<td width="30%"></td>
						<td width="15%"></td>
						<td width="25%"></td>

					</tr>
					<tr>
						<td class="labelText" style="text-align: left;"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.abreviatura" /></td>
						<td class="labelText" style="text-align: left;"><c:out
							value="${definirTurnosForm.abreviatura}"></c:out></td>
						<td class="labelText" style="text-align: left;"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.nombre" /></td>
						<td class="labelText" style="text-align: left;"><c:out
							value="${definirTurnosForm.nombre}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.area" /></td>
						<td class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.area}"></c:out></td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.materia" /></td>
						<td class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.materia}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.zona" /></td>
						<td class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.zona}"></c:out></td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.subzona" /></td>
						<td class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.subzona}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.partidoJudicial" /></td>
						<td colspan="3" class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.partidoJudicial}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.partidaPresupuestaria" />
						</td>
						<td colspan="3" class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.partidaPresupuestaria}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.grupoFacturacion" /></td>
						<td colspan="3" class="labelText" style="text-align: left"><c:out
							value="${definirTurnosForm.grupoFacturacion}"></c:out></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.maestroTurnos.literal.descripcion" /></td>
						<td wrap class="labelText" style="text-align: left"><textarea
							rows="4" cols="50" readonly="readonly" class="boxConsulta"
							style="text-align: left"><c:out
							value="${definirTurnosForm.descripcion}"></c:out>
							</textarea></td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.maestroTurnos.literal.requisitosAcceso" /></td>
						<td class="labelText" style="text-align: left"><textarea
							rows="4" cols="50" readonly="readonly" class="boxConsulta"
							style="text-align: left"><c:out
							value="${definirTurnosForm.requisitos}"></c:out></textarea></td>
					</tr>
				</table>
			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.guardias">
				<table width="100%" border="0" align="center">
					<tr>
						<td class="labelText" style="text-align: center">
							<c:choose>
								<c:when test="${definirTurnosForm.guardias==0}">
									<siga:Idioma
										key="gratuita.maestroTurnos.literal.guardias.obligatorias" />
								</c:when>
								<c:when test="${definirTurnosForm.guardias==1}">
									<siga:Idioma
										key="gratuita.maestroTurnos.literal.guardias.todasNinguna" />
								</c:when>
								<c:otherwise>
									<siga:Idioma
										key="gratuita.maestroTurnos.literal.guardias.elegir" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			<siga:ConjCampos
				leyenda="gratuita.maestroTurnos.literal.configuracion">
				<table border="0" align="center" width="100%">
					<tr>
						<td class="labelText" style="text-align: left">
							<siga:Idioma key="gratuita.maestroTurnos.literal.validarJustificaciones" /> 
							<c:choose>
								<c:when test="${definirTurnosForm.validarJustificaciones=='S'}">
									<input type="checkbox" name="validarJustificaciones" disabled
										checked="checked">
								</c:when>
	
								<c:otherwise>
									<input type="checkbox" name="validarJustificaciones" disabled>
								</c:otherwise>
							</c:choose>
						</td>

						<td class="labelText" style="text-align: left">
							<siga:Idioma key="gratuita.maestroTurnos.literal.validarInscripciones" /> 
							<c:choose>
								<c:when test="${definirTurnosForm.validacionInscripcion=='S'}">
									<input type="checkbox" name="validacionInscripcion" disabled
										checked="checked">
								</c:when>
	
								<c:otherwise>
									<input type="checkbox" name="validacionInscripcion" disabled>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			<siga:ConjCampos
				leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
				<table border="0" width="100%" align="center">
					<tr>
						<td class="labelText" style="text-align:left;width=15%"><siga:Idioma
							key="gratuita.maestroTurnos.literal.primerCriterio" /></td>
						<td class="labelText" style="text-align:left;width=35%"><siga:Idioma
							key="${definirTurnosForm.crit_1}" />
						&nbsp;<siga:Idioma
							key="${definirTurnosForm.ord_1}" />
						</td>
						<td class="labelText" style="text-align: left;width=15%"><siga:Idioma
							key="gratuita.maestroTurnos.literal.segundoCriterio" /></td>
						<td class="labelText" style="text-align: left;width=35%"><siga:Idioma
							key="${definirTurnosForm.crit_2}" />
						&nbsp;<siga:Idioma
							key="${definirTurnosForm.ord_2}" />
						</td>
					</tr>
					<tr>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.maestroTurnos.literal.terceroCriterio" /></td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="${definirTurnosForm.crit_3}" />
						&nbsp;<siga:Idioma
							key="${definirTurnosForm.ord_3}" />
						</td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.maestroTurnos.literal.cuartoCriterio" /></td>
						<td class="labelText" style="text-align: left"><siga:Idioma
							key="${definirTurnosForm.crit_4}" />
						&nbsp;<siga:Idioma
							key="${definirTurnosForm.ord_4}" />
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
		</td>
	</tr>

</table>
</html:form>
<siga:ConjBotonesAccion botones="X,S" ordenar="false" clase="botonesDetalle"  />
</body>

</html>

