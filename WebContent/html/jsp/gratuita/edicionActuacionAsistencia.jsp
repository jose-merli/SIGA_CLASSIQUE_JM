<!-- edicionActuacionAsistencia.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>





<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css"
		href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page='/html/js/SIGA.js'/>"
		type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
		type="text/javascript"></script>
	<script
		src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	
	  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
<script>
function preAccionTipoActuacion()
{	
	if(document.ActuacionAsistenciaFormEdicion.idTipoActuacion.value=='-1')
		return 'cancel';
	document.ActuacionAsistenciaForm.idTipoAsistencia.value = document.ActuacionAsistenciaFormEdicion.idTipoAsistencia.value;
}

function postAccionTipoActuacion()
{	
	document.getElementById('idCosteFijoActuacion').value=  document.getElementById('auxIdCosteFijoActuacion').value ;
	document.getElementById('auxIdCosteFijoActuacion').value = '';
	//alert("document.ActuacionAsistenciaForm.modo.value"+document.ActuacionAsistenciaForm.modo.value);
	if(document.ActuacionAsistenciaForm.modo.value =='ver')
		document.getElementById('idCosteFijoActuacion').disabled ="disabled";
	else
		document.getElementById('idCosteFijoActuacion').disabled ="";

}


</script>
	
</head>

<body onload="inicio();">
<bean:define id="usrBean" name="USRBEAN" scope="session"
	type="com.atos.utils.UsrBean"/>
<bean:define id="botones" name="botones" scope="request"/>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="gratuita.mantActuacion.literal.titulo"/>
		</td>
	</tr>
</table>
<html:form action="${path}" >
	<html:hidden property="modo" />
	<html:hidden property="idTipoAsistencia" />
	
	
</html:form>
<html:javascript formName="ActuacionAsistenciaFormEdicion" staticJavascript="true" />
<html:form action="${path}"  name="ActuacionAsistenciaFormEdicion" type="com.siga.gratuita.form.ActuacionAsistenciaForm" method="POST" target="submitArea" enctype="multipart/form-data">
	<html:hidden property="modo" value="${ActuacionAsistenciaFormEdicion.modo}"/>
	<html:hidden property="idInstitucion" value="${ActuacionAsistenciaFormEdicion.idInstitucion}"/>
	<html:hidden property="anio" value="${ActuacionAsistenciaFormEdicion.anio}"/>
	<html:hidden property="numero" value="${ActuacionAsistenciaFormEdicion.numero}"/>
	<html:hidden property="diaDespues" value="${ActuacionAsistenciaFormEdicion.diaDespues}"/>
	<html:hidden property="anulacion" value="${ActuacionAsistenciaFormEdicion.anulacion}"/>
	<html:hidden property="validada" value="${ActuacionAsistenciaFormEdicion.validada}"/>
	<html:hidden property="acuerdoExtrajudicial" value="${ActuacionAsistenciaFormEdicion.acuerdoExtrajudicial}"/>
	<html:hidden property="numeroDiligenciaAsistencia" value="${ActuacionAsistenciaFormEdicion.numeroDiligenciaAsistencia}"/>
	<html:hidden property="numeroProcedimientoAsistencia" value="${ActuacionAsistenciaFormEdicion.numeroProcedimientoAsistencia}"/>
	<html:hidden property="comisariaAsistencia" value="${ActuacionAsistenciaFormEdicion.comisariaAsistencia}"/>
	<html:hidden property="juzgadoAsistencia" value="${ActuacionAsistenciaFormEdicion.juzgadoAsistencia}"/>
	
	
	<input type="hidden" name="validarJustificaciones" value="${asistencia.validarJustificaciones}" />
	<html:hidden property="idTipoAsistencia" value="${ActuacionAsistenciaFormEdicion.idTipoAsistencia}"/>
	
	<input type="hidden" name="auxIdCosteFijoActuacion" value="${ActuacionAsistenciaFormEdicion.idCosteFijoActuacion}" />
	
	<input type="hidden" name="actionModal" />
	
	


<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.dasistencia">
	<table width="100%" style="table-layout:fixed">
		<tr>
			<td width="12%"></td>
			<td width="22%"></td>
			<td width="8%"></td>
			<td width="26%"></td>
			<td width="8%"></td>
			<td width="8%"></td>
			<td width="8%"></td>
			<td width="8%"></td>
			
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.turno'/>
			</td>
			<td class="labelTextValor">	
				<c:out value="${asistencia.turno.nombre}"></c:out>
				
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.guardia'/>
			</td>
			<td class="labelTextValor">	
				<c:out value="${asistencia.guardia.nombreGuardia}"></c:out>
				
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.anio'/>
			</td>
			<td class="labelTextValor">	
					<c:out value="${asistencia.anio}"></c:out>
				
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.numero'/>
			</td>
			<td class="labelTextValor">	
				<c:out value="${asistencia.numero}"></c:out>
				
			</td>
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
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.nif'/>
				</td>
				<td class="labelTextValor">	
					<c:out value="${asistencia.personaJG.NIdentificacion}"></c:out>
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.nombre'/>
				</td>
				<td class="labelTextValor">	
				<c:out value="${asistencia.personaJG.nombre}"></c:out>
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.apellidos1'/>
				</td>
				<td class="labelTextValor">
				<c:out value="${asistencia.personaJG.apellido1}"></c:out>	
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.apellidos2'/>
				</td>
				<td class="labelTextValor">
					<c:out value="${asistencia.personaJG.apellido2}"></c:out>	
					
				</td>
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
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.ncolegiado'/>
				</td>
				<td class="labelTextValor">	
				<c:out value="${asistencia.personaColegiado.colegiado.NColegiado}"></c:out>
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.nombre'/>
				</td>
				<td class="labelTextValor">	
				<c:out value="${asistencia.personaColegiado.nombre}"></c:out>
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.apellidos1'/>
				</td>
				<td class="labelTextValor">	
				<c:out value="${asistencia.personaColegiado.apellido1}"></c:out>
					
				</td>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantActuacion.literal.apellidos2'/>
				</td>
				<td class="labelTextValor">	
				<c:out value="${asistencia.personaColegiado.apellido2}"></c:out>
					
				</td>
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
					<td colspan ="2" class="labelTextValor">
						<c:choose>
							<c:when test="${ActuacionAsistenciaForm.modo=='nuevo'}">
								<html:text name="ActuacionAsistenciaFormEdicion"
									property="idActuacion" size="10" maxlength="10" styleClass="box" />
							</c:when>
							<c:otherwise>
								<c:out value="${ActuacionAsistenciaFormEdicion.idActuacion}"/>
								<html:hidden name="ActuacionAsistenciaFormEdicion" property="idActuacion"/>
							</c:otherwise>	
						</c:choose>
						
						
					</td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.descripcion' />
					</td>
					<td><html:text name="ActuacionAsistenciaFormEdicion"
							property="descripcionBreve" size="40" styleClass="box"></html:text>
					</td>
				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.fecha' />&nbsp;(*)</td>

					<td colspan ="2" class="labelTextValor">
					<c:choose>
							<c:when test="${ActuacionAsistenciaForm.modo=='ver'}">
								<html:text property="fecha" size="10" readonly="true"
									styleClass="box" value="${ActuacionAsistenciaFormEdicion.fecha}" />
							
							</c:when>
							<c:otherwise>
								<html:text property="fecha" size="10" readonly="true"
								styleClass="box" value="${ActuacionAsistenciaFormEdicion.fecha}" />&nbsp;
								<a href='javascript://'
								onClick="showCalendarGeneral(fecha);compruebaDiaDespues(fecha,'${asistencia.fechaHora}');">
								<img src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
								border="0"> </a>
							</c:otherwise>	
						</c:choose>
					
							
							</td>



					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.diadespues' /></td>
					<td><input type="checkbox" id="checkDiaDespues"/></td>

				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantActuacion.literal.tipoActuacion" />&nbsp;(*)</td>
					<td colspan="4"><html:select styleClass="boxCombo"
							style="width:600px;" styleId="tiposActuacion" name="ActuacionAsistenciaFormEdicion"
							property="idTipoActuacion" >
							<bean:define id="tiposActuacion" name="ActuacionAsistenciaForm"
								property="tiposActuacion" type="java.util.Collection" />
							<html:optionsCollection name="tiposActuacion"
								value="idTipoActuacion" label="descripcion" />
						</html:select></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantActuacion.literal.Coste" /></td>
					<td colspan="4"><html:select styleClass="boxCombo"
							style="width:310px;" styleId="tiposCosteFijoActuaciones" name="ActuacionAsistenciaFormEdicion"
							property="idCosteFijoActuacion">
							<bean:define id="tipoCosteFijoActuaciones"
								name="ActuacionAsistenciaForm"
								property="tipoCosteFijoActuaciones" type="java.util.Collection" />
							<html:optionsCollection name="tipoCosteFijoActuaciones"
								value="value" label="key" />
						</html:select></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.nasunto' /></td>

					<td align="left" colspan ="2"><html:text
							name="ActuacionAsistenciaFormEdicion" property="numeroAsunto"
							size="30" maxlength="20" styleClass="box"></html:text></td>

					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.anulacion' /></td>
					<td><input type="checkbox"	id="checkAnulacion"/>
					</td>

				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantenimientoTablasMaestra.literal.comisaria" /></td>
					<td><input type="text" id="codComisaria" class="box" size="8"
						style=" margin-top: 2px;" maxlength="10"
						onBlur="obtenerComisaria();" /></td>
					<td colspan="3"><html:select styleClass="boxCombo"
							style="width:680px;" name="ActuacionAsistenciaFormEdicion"
							property="idComisaria" onchange="cambioComisaria();">
							<bean:define id="comisarias" name="ActuacionAsistenciaForm"
								property="comisarias" type="java.util.Collection" />
							<html:optionsCollection name="comisarias" value="idComisaria"
								label="nombre" />
						</html:select></td>




				</tr>
				<tr>

					<td class="labelText"><siga:Idioma
							key="gratuita.mantenimientoTablasMaestra.literal.juzgado" /></td>

					<td><input type="text" id="codJuzgado" class="box" size="8"
						style=" margin-top: 2px;" maxlength="10"
						onBlur="obtenerJuzgado();" /></td>
					<td colspan="3"><html:select styleClass="boxCombo"
							style="width:680px;" name="ActuacionAsistenciaFormEdicion"
							property="idJuzgado" onchange="cambioJuzgado();">
							<bean:define id="juzgados" name="ActuacionAsistenciaForm"
								property="juzgados" type="java.util.Collection" />
							<html:optionsCollection name="juzgados" value="idJuzgado"
								label="nombre" />
						</html:select></td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.mantenimientoTablasMaestra.literal.prision" /></td>
					<td colspan ="2"><html:select styleClass="boxCombo" style="width:300px;"
							name="ActuacionAsistenciaFormEdicion" property="idPrision">
							<bean:define id="prisiones" name="ActuacionAsistenciaForm"
								property="prisiones" type="java.util.Collection" />
							<html:optionsCollection name="prisiones" value="codigoExt"
								label="nombre" />
						</html:select></td>
					<td class="labelText"><siga:Idioma
							key='gratuita.mantActuacion.literal.observaciones' /></td>

					<td><html:textarea name="ActuacionAsistenciaFormEdicion" property="observaciones" cols="90" rows="3" style="overflow:auto" styleClass="boxCombo"></html:textarea></td>

				</tr>
			</table>
		</siga:ConjCampos>

		<c:choose>
<c:when test="${usrBean.letrado==false}">
	
	
	<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.justificacion">
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
			
				<c:choose>
							<c:when test="${ActuacionAsistenciaForm.modo=='ver'}">
								<html:text property="fechaJustificacion" size="10" readonly="true"
												styleClass="boxConsulta"
												value="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" />
							
							</c:when>
							<c:otherwise>
								<html:text property="fechaJustificacion" size="10" readonly="true"
												styleClass="box"
												value="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" />&nbsp; <a
											name="calendarioTd" 
											href='javascript://'
											onClick="showCalendarGeneral(fechaJustificacion);volverJustificacion();"> <img
												src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
												border="0"> </a>
							</c:otherwise>	
						</c:choose>
			
			
				
			</td>
				

			
			<c:choose>
				<c:when test="${ActuacionAsistenciaForm.modo=='ver'}">
					
					<td colspan="2" class="labelTextValor">&nbsp;
						<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
							<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>
						</c:if>
					</td>
					
					<td class="labelText">	
						<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
					</td>	
					<td class="labelTextValor">
						<html:textarea name="ActuacionAsistenciaFormEdicion" property="observacionesJustificacion" cols="90" rows="3" style="overflow:auto" styleClass="boxCombo"></html:textarea>
					</td>
				</c:when>
				
				<c:when
						test="${ActuacionAsistenciaFormEdicion.facturado==0}">
					<td>
					<input type="button" alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>"
								id="idButton" onclick="validaJustificacion();"
								class="button"
								value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
					</td>
					<td class="labelTextValor" align="left" id="tdValidada">&nbsp;
						<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
							<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>
						</c:if>
					</td>
					<td class="labelText">	
						<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
					</td>	
					<td class="labelTextValor">
						<html:textarea name="ActuacionAsistenciaFormEdicion" property="observacionesJustificacion" cols="90" rows="3" style="overflow:auto" styleClass="boxCombo"></html:textarea>
					</td>
				</c:when>
				
				<c:when
					test="${ActuacionAsistenciaForm.modo!='ver'&&ActuacionAsistenciaFormEdicion.facturado==1}">
					<td colspan = "2" class="labelTextValor">Actuacion facturada</td>
					
				<td class="labelText">	
						<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
					</td>	
					<td class="labelTextValor">
						<c:out	value="${ActuacionAsistenciaFormEdicion.observacionesJustificacion}"></c:out>
					</td>
				</c:when>
			</c:choose>		
			
				
		</tr>
		
	</table>
	</siga:ConjCampos>
</c:when>
<c:otherwise>
	<html:hidden name="ActuacionAsistenciaFormEdicion"
								property="fechaJustificacion" />
	<html:hidden name="ActuacionAsistenciaFormEdicion"
								property="observacionesJustificacion" />
</c:otherwise>
</c:choose>

<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxTipoCosteFijoActuacion"
	source="tiposActuacion" target="tiposCosteFijoActuaciones" parameters="idTipoActuacion={idTipoActuacion},idTipoAsistencia={idTipoAsistencia},idInstitucion={idInstitucion}"
	preFunction="preAccionTipoActuacion"
	postFunction="postAccionTipoActuacion"
	
/>



	<table valign="bottom">
		<!-- tr><td>&nbsp;</td></tr-->
		<tr>
			<td class="labelText">
				<siga:Idioma key='gratuita.mantActuacion.literal.mensajeAsunto'/>
			</td>
		</tr>
	</table>



<siga:ConjBotonesAccion botones="${botones}" modal="P"/>
</html:form>
<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
	<input type="hidden" name="modo"        value="buscarJuzgado">
	<html:hidden property = "codigoExt" value=""/>
</html:form>	
<html:form action = "/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
	<input type="hidden" name="modo"        value="buscarComisaria">
	<html:hidden property = "codigoExtBusqueda" value=""/>
</html:form>


	<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>	
<script type="text/javascript">
<!-- Asociada al boton GuardarCerrar -->

function obtenerComisaria() 
{ 
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
			document.ActuacionAsistenciaFormEdicion.idComisaria.value=idComisaria;
		}else{
			
		}
	}
	
	
}
function cambioComisaria(){
	document.ActuacionAsistenciaFormEdicion.idJuzgado.value="";
}			
function obtenerJuzgado() 
{ 
  	if (document.getElementById("codJuzgado").value!=""){
		document.MantenimientoJuzgadoForm.codigoExt.value=document.getElementById("codJuzgado").value;
	   	document.MantenimientoJuzgadoForm.submit();	
	   	cambioJuzgado();	
 	}
}

function cambioJuzgado(){
	document.ActuacionAsistenciaFormEdicion.idComisaria.value="";
}	
function traspasoDatos(resultado){
	if(resultado && resultado.length > 0){
		var fin = resultado[0].indexOf(',');
		if (fin != -1) { 
			var idJuzgado = resultado[0].substring(0,fin);
			document.ActuacionAsistenciaFormEdicion.idJuzgado.value=idJuzgado;
		}else{
			
		}
	}

}	



function inicio() 
{
	//Para que se rellene el combo de costes
	document.getElementById('idTipoActuacion').onchange();
	document.getElementById("checkDiaDespues").checked = document.ActuacionAsistenciaFormEdicion.diaDespues.value=='S';
	document.getElementById("checkAnulacion").checked = document.ActuacionAsistenciaFormEdicion.anulacion.value=='1';
	if(document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
		document.getElementById('fechaJustificacion').className="boxConsulta";
		if(document.getElementById("tdValidada"))
			document.getElementById("tdValidada").innerText = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
		if(document.getElementById("calendarioTd"))
			document.getElementById("calendarioTd").style.visibility="hidden";
	}else{
		document.getElementById('fechaJustificacion').className="box";
		if(document.getElementById("tdValidada"))
			document.getElementById("tdValidada").innerText = '';
		if(document.getElementById("calendarioTd"))
			document.getElementById("calendarioTd").style.visibility="visible";

	}
	
	
	if(document.ActuacionAsistenciaForm.modo.value=='ver'){
		inputs = document.getElementsByTagName("input");
		for(var i = 0 ; i <inputs.length ; i++) {
			input = inputs[i];
			if(input.type=="checkbox")
				input.disabled =  "disabled";
			else if(input.type!="button"){
				input.className =  "boxConsulta";
				//input.disabled =  "disabled";
			}
		}
		selects = document.getElementsByTagName("select");
		for(var i = 0 ; i <selects.length ; i++) {
			select = selects[i];
			select.disabled =  "disabled"; 
		}
		textareas = document.getElementsByTagName("textarea");
		for(var i = 0 ; i <textareas.length ; i++) {
			textarea = textareas[i];
			textarea.disabled =  "disabled"; 
		}
		
	}
	
	
	
	//Aqui dependiendo del modo, si es consulta deshabiliatremos todo
	//si es nuevo
	
}
function refrescarLocal() {
	document.ActuacionAsistenciaForm.modo.value = 'abrir';
	window.close();
	// document.ActuacionAsistenciaForm.target ='submitArea';
	// document.ActuacionAsistenciaForm.modo.value = 'abrir';
	//document.ActuacionAsistenciaForm.submit();
	
}

function accionGuardarCerrar() 
{
	sub();
	if(document.ActuacionAsistenciaFormEdicion.idTipoActuacion.value== '-1'){
		msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.tipoActuacion'/>";
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
	if (validateActuacionAsistenciaFormEdicion(document.ActuacionAsistenciaFormEdicion)){
		document.ActuacionAsistenciaForm.modo.value = 'abrir';
		document.ActuacionAsistenciaFormEdicion.submit();
	 }else{
	 	fin();
 	}
	
}

function compruebaDiaDespues(fecha1, fecha2){
	 var fechaAct=fecha1.value;
	 var fechaHora=fecha2;
	  if (isAfter(fechaAct,fechaHora)){
		  	
			document.getElementById("checkDiaDespues").checked = true;
	  }else{
	    	if (isEquals(fechaHora,fechaAct)){
	    		document.getElementById("checkDiaDespues").checked = false;
			}else{// cuando la fecha de asistencia es igual que la de la actuacion el check del dia despues no se chequea.
				alert("La fecha de actuación no puede ser anterior a la fecha de la asistencia ("+fechaHora+")");
		  		document.ActuacionAsistenciaFormEdicion.fecha.value = '';
		  		document.getElementById("checkDiaDespues").checked = false;
		
			}// fin del if
	  }// fin del if
}

function validaJustificacion () {
	
	if(document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
		document.getElementById('fechaJustificacion').className="box";
		document.ActuacionAsistenciaFormEdicion.validada.value="0";
		document.getElementById("tdValidada").innerText = '';
		document.getElementById("calendarioTd").style.visibility="visible";
		document.getElementById('fechaJustificacion').value="";
		
		
	}else{
		document.getElementById('fechaJustificacion').className="boxConsulta";
		document.ActuacionAsistenciaFormEdicion.validada.value="1";
		document.getElementById("calendarioTd").style.visibility="hidden";
		document.getElementById("tdValidada").innerText = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
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
			//document.getElementById("tdValidada").innerText = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
		//}
		//else{
			//document.ActuacionAsistenciaFormEdicion.validada.value="0";
			//document.getElementById("tdValidada").innerText = '';
				
		//}
		document.ActuacionAsistenciaFormEdicion.validada.value="1";
		document.getElementById("tdValidada").innerText = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
		
	}else{
		document.getElementById("tdValidada").innerText = '';
		document.ActuacionAsistenciaFormEdicion.validada.value="0";
	}
	
}




<!-- Asociada al boton Cerrar -->
function accionCerrar() 
{		
	document.ActuacionAsistenciaForm.modo.value = 'abrir';
	window.close(); 
	
}

<!-- Asociada al boton Restablecer -->
function accionRestablecer() 
{		
	document.ActuacionAsistenciaFormEdicion.reset();
	inicio();
}

</script>

</body>



</html>
