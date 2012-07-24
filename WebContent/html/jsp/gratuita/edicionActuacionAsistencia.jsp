<!-- edicionActuacionAsistencia.jsp -->

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
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />

<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>"></script>	
<script type="text/javascript" src="<html:rewrite page="/html/js/jquery.custom.js"/>"></script>		
<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>

<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>

<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="<html:rewrite page="/html/css/ajaxtags.css"/>" />

	<script type="text/javascript">
		function preAccionTipoActuacion() {
			//alert("preAccionTipoActuacion");
			if(document.getElementById("tiposActuacion").value=='-1'){
				document.getElementById('tiposCosteFijoActuaciones').options.length = 0;
				return 'cancel';
			}
			document.ActuacionAsistenciaForm.idTipoAsistencia.value = document.ActuacionAsistenciaFormEdicion.idTipoAsistencia.value;
		}
		
		function postAccionTipoActuacion() {	
			//alert("postAccionTipoActuacion");
			document.getElementById('idCosteFijoActuacion').value = document.getElementById('auxIdCosteFijoActuacion').value ;
			document.getElementById('auxIdCosteFijoActuacion').value = '';
			if(document.ActuacionAsistenciaForm.modo.value =='ver' || document.ActuacionAsistenciaFormEdicion.validada.value==1 ||  document.ActuacionAsistenciaFormEdicion.anulacion.value==1){
				document.getElementById('idCosteFijoActuacion').disabled ="disabled";
			} else {
				document.getElementById('idCosteFijoActuacion').disabled ="";
			}
			if(document.ActuacionAsistenciaForm.modo.value=='ver' ){
				tdTiposCosteFijoAct = document.getElementById('tdSelectTiposCosteFijo');
				index=document.ActuacionAsistenciaFormEdicion.idCosteFijoActuacion.selectedIndex;
				idCosteFijoActuacion="";
				if(index!=-1){
					descripcionCosteFijo = document.ActuacionAsistenciaFormEdicion.idCosteFijoActuacion.options[index].text;
					idCosteFijoActuacion=document.ActuacionAsistenciaFormEdicion.idCosteFijoActuacion.options[index].value;
				} else {
					descripcionCosteFijo ="";
				}
				tdTiposCosteFijoAct.innerHTML = '<input type="hidden" value="'+idCosteFijoActuacion+'" id="idCosteFijoActuacion"/><input type="text" readonly class="boxConsulta" value="'+descripcionCosteFijo+'" style="width:600px;" />';
			}	
		}	
	</script>

</head>

<body onload="inicio();">
	<bean:define id="usrBean" name="USRBEAN" scope="session"
		type="com.atos.utils.UsrBean" />
	<bean:define id="botones" name="botones" scope="request" />
	<bean:define id="tipoPcajg" name="tipoPcajg" scope="request" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"
		property="path" scope="request" />

	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma
					key="gratuita.mantActuacion.literal.titulo" /></td>
		</tr>
	</table>
	<html:form action="${path}">
		<html:hidden property="modo" />
		<html:hidden property="idTipoAsistencia" />
	</html:form>

	<html:javascript formName="ActuacionAsistenciaFormEdicion"
		staticJavascript="true" />
		
	<html:form action="${path}" name="ActuacionAsistenciaFormEdicion"
		type="com.siga.gratuita.form.ActuacionAsistenciaForm" method="POST"
		target="submitArea" enctype="multipart/form-data">
		<html:hidden property="modo"
			styleId="modo"
			value="${ActuacionAsistenciaFormEdicion.modo}" />
		<html:hidden property="idInstitucion"
			styleId="idInstitucion"
			value="${ActuacionAsistenciaFormEdicion.idInstitucion}" />
		<html:hidden property="anio"
			styleId="anio"
			value="${ActuacionAsistenciaFormEdicion.anio}" />
		<html:hidden property="numero"
			styleId="numero"
			value="${ActuacionAsistenciaFormEdicion.numero}" />
		<html:hidden property="diaDespues"
			styleId="diaDespues"
			value="${ActuacionAsistenciaFormEdicion.diaDespues}" />
		<html:hidden property="anulacion"
			styleId="anulacion"
			value="${ActuacionAsistenciaFormEdicion.anulacion}" />
		<html:hidden property="validada"
			styleId="validada"
			value="${ActuacionAsistenciaFormEdicion.validada}" />
		<html:hidden property="acuerdoExtrajudicial"
			styleId="acuerdoExtrajudicial"
			value="${ActuacionAsistenciaFormEdicion.acuerdoExtrajudicial}" />
		<html:hidden property="numeroDiligenciaAsistencia"
			styleId="numeroDiligenciaAsistencia"
			value="${ActuacionAsistenciaFormEdicion.numeroDiligenciaAsistencia}" />
		<html:hidden property="numeroProcedimientoAsistencia"
			styleId="numeroProcedimientoAsistencia"
			value="${ActuacionAsistenciaFormEdicion.numeroProcedimientoAsistencia}" />
		<html:hidden property="comisariaAsistencia"
			styleId="comisariaAsistencia"
			value="${ActuacionAsistenciaFormEdicion.comisariaAsistencia}" />
		<html:hidden property="juzgadoAsistencia"
			styleId="juzgadoAsistencia"
			value="${ActuacionAsistenciaFormEdicion.juzgadoAsistencia}" />
		<html:hidden property="tipoPcajg"
			styleId="tipoPcajg"
			value="${ActuacionAsistenciaFormEdicion.tipoPcajg}" />
		<input type="hidden" name="validarJustificaciones" 
			id="validarJustificaciones"
			value="${asistencia.validarJustificaciones}" />
		<input type="hidden" 
			id="isLetrado" 
			name="isLetrado" value="${usrBean.letrado}" />
		<html:hidden property="idTipoAsistencia"
			styleId="idTipoAsistencia"
			value="${ActuacionAsistenciaFormEdicion.idTipoAsistencia}" />
		<input type="hidden" name="auxIdCosteFijoActuacion"
			id="auxIdCosteFijoActuacion"
			value="${ActuacionAsistenciaFormEdicion.idCosteFijoActuacion}" />
		<input type="hidden" name="actionModal" 
			id="actionModal"/>


		<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.dasistencia">
			<table width="100%" style="table-layout: fixed">
				<tr>
					<td width="6%"></td>
					<td width="22%"></td>
					<td width="6%"></td>
					<td width="22%"></td>
					<td width="4%"></td>
					<td width="4%"></td>
					<td width="6%"></td>
					<td width="4%"></td>
					<td width="6%"></td>
					<td width="8%"></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.turno' /></td>
					<td class="labelTextValor"><c:out
							value="${asistencia.turno.nombre}"></c:out></td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.guardia' /></td>
					<td class="labelTextValor"><c:out
							value="${asistencia.guardia.nombreGuardia}"></c:out></td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.anio' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.anio}"></c:out></td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.numero' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.numero}"></c:out>

					</td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.fecha' /></td>
					<td class="labelTextValor"><c:out
							value="${asistencia.fechaHora}"></c:out></td>
				</tr>
			</table>
			<siga:ConjCampos leyenda='gratuita.mantActuacion.literal.asistido'>
				<table width="100%">
					<tr>
						<td width="12%"></td>
						<td width="8%"></td>
						<td width="8%"></td>
						<td width="12%"></td>
						<td width="8%"></td>
						<td width="22%"></td>
						<td width="8%"></td>
						<td width="22%"></td>
					</tr>

					<tr>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.nif' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaJG.NIdentificacion}"></c:out></td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.nombre' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaJG.nombre}"></c:out></td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.apellidos1' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaJG.apellido1}"></c:out></td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.apellidos2' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaJG.apellido2}"></c:out></td>
					</tr>
				</table>
			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.letrado">

				<table width="100%">
					<tr>
						<td width="12%"></td>
						<td width="8%"></td>
						<td width="8%"></td>
						<td width="12%"></td>
						<td width="8%"></td>
						<td width="22%"></td>
						<td width="8%"></td>
						<td width="22%"></td>

					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.ncolegiado' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaColegiado.colegiado.NColegiado}"></c:out>
						</td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.nombre' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaColegiado.nombre}"></c:out></td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.apellidos1' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaColegiado.apellido1}"></c:out></td>
						<td class="labelText"><siga:Idioma
								key='gratuita.mantActuacion.literal.apellidos2' /></td>
						<td class="labelTextValor"><c:out
								value="${asistencia.personaColegiado.apellido2}"></c:out></td>
					</tr>
				</table>
			</siga:ConjCampos>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.actuacion">
			<table width="100%" border="0">
				<tr>
					<td width="15%"></td>
					<td width="5%"></td>
					<td width="25%"></td>
					<td width="25%"></td>
					<td width="35%"></td>
				</tr>

				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.nactuacion' />&nbsp;(*)</td>
					<td class="labelTextValor"><c:choose>
							<c:when test="${ActuacionAsistenciaForm.modo=='nuevo'}">
								<html:text name="ActuacionAsistenciaFormEdicion" styleId="idActuacion"
									property="idActuacion" size="10" maxlength="10"
									styleClass="box" />
							</c:when>
							<c:otherwise>
								<c:out value="${ActuacionAsistenciaFormEdicion.idActuacion}" />
								<html:hidden name="ActuacionAsistenciaFormEdicion"
									property="idActuacion" styleId="idActuacion"/>
							</c:otherwise>
						</c:choose></td>

					<td class="labelText" style="text-align: center;"><siga:Idioma
							key='gratuita.mantActuacion.literal.anulacion' /> <input
						type="checkbox" id="checkAnulacion"
						onclick="onclickCheckAnulacion();" /></td>

					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.descripcion' /></td>
					<td><html:text name="ActuacionAsistenciaFormEdicion" styleId="descripcionBreve"
							property="descripcionBreve" size="40" styleClass="box"></html:text>
					</td>
				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.fecha' />&nbsp;(*)</td>

					<td colspan="2">
						<c:choose>
							<c:when
								test="${ActuacionAsistenciaForm.modo=='ver' ||  ActuacionAsistenciaFormEdicion.validada==1 ||  ActuacionAsistenciaFormEdicion.anulacion==1}">
								<html:text property="fecha" size="10" readonly="true"
									styleClass="box" styleId="fecha"
									value="${ActuacionAsistenciaFormEdicion.fecha}" />

							</c:when>
							<c:otherwise>
								<siga:Fecha  nombreCampo= "fecha" valorInicial="${ActuacionAsistenciaFormEdicion.fecha}" readonly="true" postFunction="compruebaDiaDespues('${asistencia.fechaHora}');"/>
							</c:otherwise>
						</c:choose>
					</td>

					<td class="labelText">
						<siga:Idioma key='gratuita.mantActuacion.literal.diadespues' />
					</td>
					<td>
						<input type="checkbox" id="checkDiaDespues" />
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantActuacion.literal.tipoActuacion" />&nbsp;(*)</td>
					<td colspan="4" id="tdSelectTiposActuacion">
						<html:select styleClass="boxCombo" style="width:600px;"
							styleId="tiposActuacion" name="ActuacionAsistenciaFormEdicion"							
							property="idTipoActuacion">
							<bean:define id="tiposActuacion" name="ActuacionAsistenciaForm"
								property="tiposActuacion" type="java.util.Collection" />
							<html:optionsCollection name="tiposActuacion"
								value="idTipoActuacion" label="descripcion" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantActuacion.literal.Coste" /></td>
					<td colspan="4" id="tdSelectTiposCosteFijo">
						<html:select styleClass="boxCombo" style="width:600px;"
							styleId="tiposCosteFijoActuaciones"
							name="ActuacionAsistenciaFormEdicion"
							property="idCosteFijoActuacion">
							<bean:define id="tipoCosteFijoActuaciones"
								name="ActuacionAsistenciaForm"
								property="tipoCosteFijoActuaciones" type="java.util.Collection" />
							<html:optionsCollection name="tipoCosteFijoActuaciones"
								value="value" label="key" />
						</html:select>
					</td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${tipoPcajg=='2'}">
							<td class="labelText"><siga:Idioma
									key='gratuita.mantActuacion.literal.nasunto' />(*)</td>
							<td align="left" colspan="2"><html:text
									name="ActuacionAsistenciaFormEdicion" property="numeroAsunto" styleId="numeroAsunto"
									size="30" maxlength="15" styleClass="box"></html:text></td>
						</c:when>
						<c:otherwise>
							<td class="labelText"><siga:Idioma
									key='gratuita.mantActuacion.literal.nasunto' /></td>
							<td align="left" colspan="2"><html:text
									name="ActuacionAsistenciaFormEdicion" property="numeroAsunto" styleId="numeroAsunto"
									size="30" maxlength="20" styleClass="box"></html:text></td>
						</c:otherwise>
					</c:choose>
					<td class="labelText" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="5">
						<table>
							<tr>
								<td width="20%"></td>
								<td width="5%"></td>
								<td width="5%"></td>
								<td width="70%"></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td class="labelText"><siga:Idioma
										key="gratuita.mantenimientoTablasMaestra.literal.comisaria" /></td>
								<td>
									<input type="text" id="codComisaria" class="box"
										size="8" style="margin-top: 2px;" maxlength="10"
										onBlur="obtenerComisaria();" />
								</td>
								<td id="tdSelectComisaria">
									<html:select styleClass="boxCombo" style="width:680px;"
										name="ActuacionAsistenciaFormEdicion" property="idComisaria"
										styleId="idComisaria"
										onchange="cambioComisaria();">
										<bean:define id="comisarias" name="ActuacionAsistenciaForm"
											property="comisarias" type="java.util.Collection" />
										<html:optionsCollection name="comisarias" value="idComisaria"
											label="nombre" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td></td>
								<td class="labelText" style="text-align: center;">�</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
								</td>
								<td>
									<input type="text" id="codJuzgado" class="box" size="8"
										style="margin-top: 2px;" maxlength="10"
										onBlur="obtenerJuzgado();" />
								</td>
								<td id="tdSelectJuzgado">
									<html:select styleClass="boxCombo"
										style="width:680px;" name="ActuacionAsistenciaFormEdicion"
										styleId="idJuzgado"
										property="idJuzgado" onchange="cambioJuzgado();">
										<bean:define id="juzgados" name="ActuacionAsistenciaForm"
											property="juzgados" type="java.util.Collection" />
										<html:optionsCollection name="juzgados" value="idJuzgado"
											label="nombre" />
									</html:select>
								</td>
							</tr>
						</table>
					</td>
				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantAsistencias.literal.NIG' /></td>
					<td colspan="5">
						<html:text name="ActuacionAsistenciaFormEdicion" property="nig" size="30" styleId="nig"
							maxlength="50" styleClass="box"></html:text></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantenimientoTablasMaestra.literal.prision" /></td>
					<td colspan="2" id="tdSelectPrision">
						<html:select styleClass="boxCombo" style="width:300px;"
							name="ActuacionAsistenciaFormEdicion" property="idPrision" styleId="idPrision">
							<bean:define id="prisiones" name="ActuacionAsistenciaForm"
								property="prisiones" type="java.util.Collection" />
							<html:optionsCollection name="prisiones" value="codigoExt"
								label="nombre" />
						</html:select></td>
					<td class="labelText">
						<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
					</td>
					<td> 
						<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observaciones"
							property="observaciones" cols="90" rows="3" style="overflow:auto"
							styleClass="boxCombo"></html:textarea>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<c:choose>
			<c:when
				test="${ActuacionAsistenciaFormEdicion.facturado=='1' ||ActuacionAsistenciaForm.modo=='ver'}">
				<siga:ConjCampos
					leyenda="gratuita.mantActuacion.literal.justificacion">
					<table width="100%" border="0">
						<tr>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="10%"></td>
							<td width="20%"></td>
							<td width="15%"></td>
							<td width="30%"></td>
						</tr>

						<tr>
							<td class="labelText" valign="top"><siga:Idioma
									key='gratuita.mantActuacion.literal.fecha' /></td>
							<td class="labelTextValor"><html:text
									property="fechaJustificacion" size="10" readonly="true"
									styleId="fechaJustificacion"
									styleClass="boxConsulta"
									value="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" />
							</td>
							<td colspan="2" class="labelTextValor">&nbsp; 
								<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
									<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada' />
								</c:if>
							</td>

							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
							</td>
							<td class="labelTextValor">
								<html:textarea
									name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion"
									property="observacionesJustificacion" cols="90" rows="3"
									style="overflow:auto" styleClass="boxCombo"></html:textarea>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</c:when>

			<c:when test="${usrBean.letrado==false}">
				<siga:ConjCampos
					leyenda="gratuita.mantActuacion.literal.justificacion">
					<table width="100%" border="0">
						<tr>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="10%"></td>
							<td width="20%"></td>
							<td width="15%"></td>
							<td width="30%"></td>
						</tr>
						<tr>
							<td class="labelText" valign="top">
								<siga:Idioma key='gratuita.mantActuacion.literal.fecha' />
							</td>
							<td class="labelTextValor">
								<siga:Fecha nombreCampo="fechaJustificacion" valorInicial="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" readOnly="true" postFunction="volverJustificacion();"></siga:Fecha>
							</td>

							<td>
								<input type="button"
									alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>"
									id="idButton" onclick="validaJustificacion();" class="button"
									value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
							</td>
							<td class="labelTextValor" align="left" id="tdValidada">&nbsp;
								<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
									<siga:Idioma
										key='gratuita.mantActuacion.literal.actuacionValidada' />
								</c:if>
							</td>
							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
							</td>
							<td class="labelTextValor">
								<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion"
									property="observacionesJustificacion" cols="90" rows="3"
									style="overflow:auto" styleClass="boxCombo"></html:textarea>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</c:when>
			<c:when
				test="${usrBean.letrado==true && asistencia.validarJustificaciones=='N' }">
				<html:hidden name="ActuacionAsistenciaFormEdicion"
					styleId="fechaJustificacion"
					property="fechaJustificacion" value="sysdate" />
				<html:hidden name="ActuacionAsistenciaFormEdicion"
					property="observacionesJustificacion"
					styleId="observacionesJustificacion"
					value="Validado autom�ticamente por configuraci�n del turno" />
			</c:when>
			<c:otherwise>
				<html:hidden name="ActuacionAsistenciaFormEdicion"
					styleId="fechaJustificacion"
					property="fechaJustificacion" />
				<html:hidden name="ActuacionAsistenciaFormEdicion"
					property="observacionesJustificacion" />
			</c:otherwise>
		</c:choose>

		<table valign="bottom">
			<!-- tr><td>&nbsp;</td></tr-->
			<tr>
				<td class="labelText"><siga:Idioma
						key='gratuita.mantActuacion.literal.mensajeAsunto' /></td>
			</tr>
		</table>
		<siga:ConjBotonesAccion botones="${botones}" modal="P" />
	</html:form>
	
	<c:if test="${ActuacionAsistenciaForm.modo != 'ver'}">
		<ajax:select
				baseUrl="/SIGA${path}.do?modo=getAjaxTipoCosteFijoActuacion"
				source="tiposActuacion" target="tiposCosteFijoActuaciones"
				parameters="idTipoActuacion={tiposActuacion},idTipoAsistencia={idTipoAsistencia},idInstitucion={idInstitucion}"
				preFunction="preAccionTipoActuacion"
				postFunction="postAccionTipoActuacion" />
	</c:if>
	
	<html:form action="/JGR_MantenimientoJuzgados.do" method="POST"
		target="submitArea">
		<input type="hidden" name="modo" value="buscarJuzgado">
		<html:hidden property="codigoExt2" styleId="codigoExt2" value="" />
	</html:form>
	
	<html:form action="/JGR_MantenimientoComisarias.do" method="POST"
		target="submitArea">
		<input type="hidden" name="modo" value="buscarComisaria">
		<html:hidden property="codigoExtBusqueda" styleId="codigoExtBusqueda" value="" />
	</html:form>

	<iframe name="submitArea"
		src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		style="display: none"></iframe>
		
	<script type="text/javascript">
	
	function obtenerComisaria() { 
		if (document.getElementById("codComisaria").value!=""){
			document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.getElementById("codComisaria").value;
			document.MantenimientoComisariaForm.submit();
			cambioComisaria();		
		 }
	}
	//			
	function traspasoDatosComisaria(resultado){
		if(resultado && resultado.length > 0){
			var fin = resultado[0].indexOf(',');
			if (fin != -1) { 
				var idComisaria = resultado[0].substring(0,fin);
				document.getElementById("idComisaria").value=idComisaria;
			} else {
				
			}
		}		
	}
	
	function cambioComisaria(){
		document.getElementById("idJuzgado").value="";
	}
	
	function obtenerJuzgado() { 
	  	if (document.getElementById("codJuzgado").value!=""){
			document.MantenimientoJuzgadoForm.codigoExt2.value=document.getElementById("codJuzgado").value;
		   	document.MantenimientoJuzgadoForm.submit();	
		   	cambioJuzgado();	
	 	}
	}
	
	function cambioJuzgado(){
		document.getElementById("idComisaria").value="";
	}
	
	function traspasoDatos(resultado){
		if(resultado && resultado.length > 0){
			var fin = resultado[0].indexOf(',');
			if (fin != -1) { 
				var idJuzgado = resultado[0].substring(0,fin);
				document.geElementById("idJuzgado").value=idJuzgado;
			}else{
				
			}
		}	
	}	
	
	function inicio() {
		//Para que se rellene el combo de costes
		if( document.getElementsByName("ActuacionAsistenciaForm")[0].modo.value != "ver" ) {
		// if(document.ActuacionAsistenciaForm.modo.value=='ver' && document.ActuacionAsistenciaFormEdicion.validada.value != "1"){	
			document.getElementById("tiposActuacion").onchange();
		}
		document.getElementById("checkDiaDespues").checked = document.ActuacionAsistenciaFormEdicion.diaDespues.value=='S';
		document.getElementById("checkAnulacion").checked = document.ActuacionAsistenciaFormEdicion.anulacion.value=='1';
		if(document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
			document.getElementById('fechaJustificacion').className="boxConsulta";
			if(document.getElementById("tdValidada"))
				document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
			if(document.getElementById("invokefechaJustificacion"))
				document.getElementById("invokefechaJustificacion").style.visibility="hidden";
		} else {
			document.getElementById('fechaJustificacion').className="box";
			if(document.getElementById("tdValidada")) {
				document.getElementById("tdValidada").innerHTML = "";
			}
			if(document.getElementById("invokefechaJustificacion")) {
				document.getElementById("invokefechaJustificacion").style.visibility="visible";
			}
		}
		
		// if(document.ActuacionAsistenciaForm.modo.value=='ver' || document.ActuacionAsistenciaFormEdicion.validada.value=="1" || document.ActuacionAsistenciaFormEdicion.anulacion.value=='1'){
		if(document.getElementsByName("ActuacionAsistenciaForm")[0].modo.value == 'ver' ){
			habilitarCampos(false);			
		} else {			
			if(document.getElementById('isLetrado').value=='false') {							
				if(document.ActuacionAsistenciaFormEdicion.anulacion.value=="1") {
					habilitarCampos(false);
					document.getElementById('fechaJustificacion').className="boxConsulta";
					if(document.getElementById("invokefechaJustificacion")) 
						document.getElementById("invokefechaJustificacion").style.visibility="hidden";
					document.getElementById("idValidacion").style.visibility="hidden";
					
					document.getElementById("checkAnulacion").disabled = "";
				} else if(document.ActuacionAsistenciaFormEdicion.validada.value=="1") {
					habilitarCampos(false);						
				}				
			} else {
				if(document.ActuacionAsistenciaFormEdicion.anulacion.value=="1" ||document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
					habilitarCampos(false);
				}
			}
		}		
	}
		
	function habilitarCampos(isHabilitar) {
		//alert('Habilitar campos : '+isHabilitar);
		
		if(isHabilitar==true){
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="checkbox") {
					jQuery.removeAttr(input,"disabled");
					//$(input).removeAttr("disabled");
				} else if(input.type!="button") {
					input.className = "box";
					jQuery.removeAttr(input,"readonly");
					//$(input).removeAttr("readonly");
				}
			}
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i <selects.length ; i++) {
				select = selects[i];
				jQuery.removeAttr(select,"disabled");
				//$(select).removeAttr("disabled"); 
			}
			var textareas = document.getElementsByTagName("textarea");
			for(var i = 0 ; i <textareas.length ; i++) {
				textarea = textareas[i];
				jQuery.removeAttr(textarea,"disabled");
				//$(select).removeAttr("disabled"); 
			}				
		} else {
			var tdTiposActuacion = document.getElementById('tdSelectTiposActuacion');
			var index = document.getElementById("tiposActuacion").selectedIndex;
			var descripcionTipoActuacion;
			if(index && index != -1){
				descripcionTipoActuacion = document.getElementById("tiposActuacion").options[index].text;
			} else {
				descripcionTipoActuacion ="";
			}
			document.getElementById("tiposActuacion").style.display="none";
			var inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionTipoActuacion+'" style="width:600px;" />';
			tdTiposActuacion.innerHTML = tdTiposActuacion.innerHTML + inputStr;
			
			var tdSelectTiposCosteFijo = document.getElementById('tdSelectTiposCosteFijo');
			index = document.getElementById("tiposCosteFijoActuaciones").selectedIndex;
			var descripcionCosteFijo;
			if(index && index != -1){
				descripcionCosteFijo = document.getElementById("tiposCosteFijoActuaciones").options[index].text;
			} else {
				descripcionCosteFijo ="";
			}
			document.getElementById("tiposCosteFijoActuaciones").style.display="none";
			inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionCosteFijo+'" style="width:680px;" />'
			tdSelectTiposCosteFijo.innerHTML = tdSelectTiposCosteFijo.innerHTML + inputStr;
	
			var tdSelectComisaria = document.getElementById('tdSelectComisaria');
			index = document.getElementById("idComisaria").selectedIndex;
			var descripcionComisaria;
			if(index && index != -1){
				descripcionComisaria = document.getElementById("idComisaria").options[index].text;
			} else {
				descripcionComisaria ="";
			}
			document.getElementById("idComisaria").style.display="none";
			inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionComisaria+'" style="width:680px;" />'
			tdSelectComisaria.innerHTML = tdSelectComisaria.innerHTML + inputStr;
			
			var tdSelectJuzgado = document.getElementById('tdSelectJuzgado');
			index = document.getElementById("idJuzgado").selectedIndex;
			var descripcionJuzgado;
			if(index && index != -1){
				descripcionJuzgado =document.getElementById("idJuzgado").options[index].text;
			} else {
				descripcionJuzgado ="";
			}
			document.getElementById("idJuzgado").style.display="none";
			inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionJuzgado+'" style="width:680px;" />';
			tdSelectJuzgado.innerHTML = tdSelectJuzgado.innerHTML + inputStr;
			
			var tdSelectPrision = document.getElementById('tdSelectPrision');
			index = document.ActuacionAsistenciaFormEdicion.idPrision.selectedIndex;
			var descripcionPrision;
			if(index && index != -1) {
				descripcionPrision = document.ActuacionAsistenciaFormEdicion.idPrision.options[index].text;
			} else {
				descripcionPrision ="";
			}
			document.getElementById("idPrision").style.display="none";
			inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionPrision+'" style="width:300px;"/>';
			tdSelectPrision.innerHTML = tdSelectPrision.innerHTML + inputStr;
			
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i < inputs.length ; i++) {
				var input = inputs[i];
				if(input.type=="checkbox"){
					jQuery.attr(input,"disabled","disabled");
					//$(input).attr("disabled","disabled");
				} else if(input.type!="button"){
					input.className = "boxConsulta";
					//$(input).attr("readonly","readonly");
					jQuery.attr(input,"readonly","readonly");
				}
			}
			
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i < selects.length ; i++) {
				var select = selects[i];
				jQuery.attr(select,"disabled","disabled");
				//$(select).attr("disabled","disabled");
			}
			var textareas = document.getElementsByTagName("textarea");
			for(var i = 0 ; i <textareas.length ; i++) {
				var textarea = textareas[i];
				jQuery.attr(textarea,"disabled","disabled")
				//$(textarea).attr("disabled","disabled"); 
			}			
			
		}
	
	}
	
	function refrescarLocal() {
		document.ActuacionAsistenciaForm.modo.value = 'abrir';
	
		window.top.close();		
	}
	
	function accionGuardarCerrar() {
		sub();
		if(document.getElementById("tiposActuacion").value== '-1'){
			msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.tipoActuacion'/>";
			alert(msg);
			fin();
			return false;
		}
		if(document.getElementById("tipoPcajg").value=='2' &&  document.ActuacionAsistenciaFormEdicion.numeroAsunto.value== ''){
			msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.nasunto'/>";
			alert(msg);
			fin();
			return false;
		}
		if(document.getElementById("checkDiaDespues").checked){
			document.ActuacionAsistenciaFormEdicion.diaDespues.value = 'S';
		}else{
			document.ActuacionAsistenciaFormEdicion.diaDespues.value = 'N';
		}
		if(document.getElementById("checkAnulacion").checked){
			document.ActuacionAsistenciaFormEdicion.anulacion.value = '1';
		}else{
			document.ActuacionAsistenciaFormEdicion.anulacion.value = '0';
		}
		if(document.getElementById("validarJustificaciones").value=='N' &&document.getElementById("isLetrado").value=='true' ){
			document.ActuacionAsistenciaFormEdicion.validada.value = "1";
		}
		habilitarCampos(true);
		if (validateActuacionAsistenciaFormEdicion(document.ActuacionAsistenciaFormEdicion)){
			document.ActuacionAsistenciaForm.modo.value = 'abrir';
			document.ActuacionAsistenciaFormEdicion.submit();
		 } else {
			 fin();
	 	}		
	}
	
	function compruebaDiaDespues(fecha2){
		 var fechaAct=document.getElementById("fecha").value;
		 var fechaHora=fecha2;
		  if (isAfter(fechaAct,fechaHora)){
			  	
				document.getElementById("checkDiaDespues").checked = true;
		  }else{
		    	if (isEquals(fechaHora,fechaAct)){
		    		document.getElementById("checkDiaDespues").checked = false;
				}else{// cuando la fecha de asistencia es igual que la de la actuacion el check del dia despues no se chequea.
					alert("La fecha de actuaci�n no puede ser anterior a la fecha de la asistencia ("+fechaHora+")");
			  		document.ActuacionAsistenciaFormEdicion.fecha.value = '';
			  		document.getElementById("checkDiaDespues").checked = false;
			
				}// fin del if
		  }// fin del if
	}
	
	function onclickCheckAnulacion () {		
	}
	
	function validaJustificacion () {		
		if(document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
			document.getElementById('fechaJustificacion').className="box";
			document.ActuacionAsistenciaFormEdicion.validada.value="0";
			document.getElementById("tdValidada").innerHTML = '';
			if(document.getElementById("invokefechaJustificacion")) 
				document.getElementById("invokefechaJustificacion").style.visibility="visible";
			document.getElementById('fechaJustificacion').value="";			
		} else {
			document.getElementById('fechaJustificacion').className="boxConsulta";
			document.ActuacionAsistenciaFormEdicion.validada.value="1";
			if(document.getElementById("invokefechaJustificacion")) 
				document.getElementById("invokefechaJustificacion").style.visibility="hidden";
			document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
			if(document.ActuacionAsistenciaFormEdicion.fechaJustificacion.value==''){	
				document.getElementById('fechaJustificacion').value=getFechaActualDDMMYYYY();
				
			}			
		}	
	}
	
	function volverJustificacion () {
		if(document.ActuacionAsistenciaFormEdicion.fechaJustificacion.value!=''){
			//Si alguna vez se abre justificaciones para colegiados, la lineas comentadas de abajo de abajo serviran cuando sea letrado
			//if(document.getElementById('validarJustificaciones').value=='N'){
				//document.ActuacionAsistenciaFormEdicion.validada.value="1";
				//document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
			//}
			//else{
				//document.ActuacionAsistenciaFormEdicion.validada.value="0";
				//document.getElementById("tdValidada").innerHTML = '';
					
			//}
			document.ActuacionAsistenciaFormEdicion.validada.value="1";
			document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
			
		}else{
			document.getElementById("tdValidada").innerHTML = '';
			document.ActuacionAsistenciaFormEdicion.validada.value="0";
		}
		
	}	
	
	<!-- Asociada al boton Cerrar -->
	function accionCerrar() {		
		document.ActuacionAsistenciaForm.modo.value = 'abrir';
		window.top.close(); 
		
	}
	
	<!-- Asociada al boton Restablecer -->
	function accionRestablecer() {		
		document.ActuacionAsistenciaFormEdicion.reset();
		if(document.ActuacionAsistenciaFormEdicion.validada.value != "1"){ 
			inicio();
		}
	}
</script>

</body>



</html>
