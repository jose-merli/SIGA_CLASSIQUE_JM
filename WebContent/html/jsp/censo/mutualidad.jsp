<!-- mutualidad.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

	<style>
		.ocultar {display:none}
	</style>	
<html>
<head>


<link id="default" rel="stylesheet" type="text/css" href='<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>'>
<script src="<html:rewrite page='/html/js/SIGA.js'/>"type="text/javascript"></script>
<script	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>"type="text/javascript"></script>

<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />	
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
  
	
<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="Alta de Mutalidad de Abogacia" />
	<!-- FIN: TITULO Y LOCALIZACION -->
	
		
	<script>
	function preAccionPoblacion(){
	}
	function postAccionPoblacion(){
	}	

	</script>	

</head>


<body  class="tablaCentralCampos" onload="cargaCombos();">
<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<bean:define id="idPaisSeleccionado" name="idPaisSeleccionado"  scope="request" type="java.util.ArrayList"/>
<bean:define id="idBancoSeleccionado" name="idBancoSeleccionado"  scope="request" type="java.util.ArrayList"/>




<html:form action="${path}"  method="POST" >
<html:hidden property="modo"/>
<html:hidden property="idTipoIdentificacion"/>
<html:hidden property="idSexo"/>
<html:hidden property="idTratamiento"/>
<html:hidden property="idEstadoCivil"/>
<html:hidden property="idTipoSolicitud"/>
<html:hidden property="idSolicitudIncorporacion"/>
<html:hidden property="tipoIdentificacion"/>
<html:hidden property="numeroIdentificacion"/>
<html:hidden property="nombre"/>
<html:hidden property="apellido1"/>
<html:hidden property="apellido2"/>
<html:hidden property="fechaNacimiento"/>
<html:hidden property="naturalDe"/>
<html:hidden property="idSolicitud"/>
<html:hidden property="idSolicitudAceptada"/>
<html:hidden property="pais"/>
<html:hidden property="provincia"/>
<html:hidden property="poblacion"/>
<html:hidden property="idEstado"/>
<html:hidden property="estado"/>
<html:hidden property="estadoMutualista"/>

<input type="hidden" name="actionModal" value="">

<c:set var="estiloText" value="box" />
<c:set var="estiloCombo" value="boxCombo" />
<c:if test="${MutualidadForm.modo=='consulta'}">
	<c:set var="estiloCombo" value="boxComboConsulta" />
	<c:set var="estiloText" value="boxConsulta" />
</c:if>

		<c:if test="${MutualidadForm.modo=='consulta'}">
			<siga:ConjCampos>
				<table>
					<tr>
						<td width=""></td>
						<td width=""></td>
						<td width=""></td>
						<td width=""></td>
						<td width=""></td>
						<td width=""></td>
						<td width=""></td>
					</tr>
					<tr>
					<td class="labelText">Solicitud de Alta</td>
					<c:choose>
							
						<c:when test="${MutualidadForm.idSolicitud==null}">
								<td>No Solicitada</td>
								<td colspan="6">&nbsp;</td>
						</c:when>
						<c:when test="${MutualidadForm.idSolicitud!=null}">
								<td>&nbsp;</td>
								<td class="labelTextValor">
									<c:choose>
										<c:when test="${MutualidadForm.idSolicitudAceptada!=null}">
											<c:out	value="${MutualidadForm.idSolicitudAceptada}" />
										</c:when>
										<c:otherwise>
											<c:out	value="${MutualidadForm.idSolicitud}" />
										</c:otherwise>
									</c:choose>
								</td>
								<td id="tdEstadoSolicitud" class="labelTextValor" style="color: blue;"><c:out
										value="${MutualidadForm.estado}" />
								</td>
								<td>
									<html:button property="idButton"
											onclick="return accionComprobarEstadoMutualidad();"
											styleClass="button">
										Estado
											</html:button>
									
								</td>
								<c:choose>
										<c:when test="${MutualidadForm.idTipoSolicitud=='P'}">
										<td id="tdEstadoMutualista" class="labelTextValor" style="color: blue;"><c:out
											value="${MutualidadForm.estadoMutualista}" />
										</td>
										<td>
											<html:button property="idButton"
												onclick="return accionComprobarEstadoMutualista();"
												styleClass="button">
											Estado Mutualista
												</html:button>
									
											</td>
										
										</c:when>
										<c:otherwise>
										<td colspan="2"></td>
										</c:otherwise>
									</c:choose>
								
								
							</c:when>
						
						
						
					</c:choose>
					
					
					</tr>
					
					

				</table>
			</siga:ConjCampos>
		</c:if>

		<c:if test="${MutualidadForm.modo!='consulta' || MutualidadForm.idSolicitud!=null}">

		<siga:ConjCampos>
			<table>
				<tr>
					<td width="20%"></td>
					<td width="15%"></td>
					<td width="15%"></td>
					<td width="15%"></td>
					<td width="20%"></td>
					<td width="15%"></td>
				</tr>

				<tr>
					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.nifcif" />
					</td>
					<td class="labelTextValor"><c:out
							value="${MutualidadForm.tipoIdentificacion}" />&nbsp;<c:out
							value="${MutualidadForm.numeroIdentificacion}" /></td>


					<td class="labelText"><siga:Idioma
							key="censo.consultaDatosGenerales.literal.sexo" />
					</td>
					<td class="labelTextValor"><c:out
							value="${MutualidadForm.sexo}" /></td>
					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.tratamiento" />
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.tratamiento}" />
					</td>
				</tr>
				<tr>

					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.nombre"/>
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.nombre}" />
					</td>

					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.apellido1" />
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.apellido1}" />
					</td>

					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.apellido2" />
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.apellido2}" />
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.fechaNacimiento" />
					</td>
					<td class="labelTextValor">

					<c:out value="${MutualidadForm.fechaNacimiento}" />
					</td>


					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.naturalDe" />
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.naturalDe}" />
					</td>

					<td class="labelText"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.estadoCivil" />
					</td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.estadoCivil}" />
					</td>
				</tr>

			</table>
		</siga:ConjCampos>
		<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.datosDireccion.literal.pais2"/></td>
			<td colspan="2">
			<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
							<siga:ComboBD nombre="idPais" tipo="pais" ancho="300" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" obligatorio="false" elementoSel='${idPaisSeleccionado}' accion="cargaCombos(this.value);" />

						</c:when>
						<c:otherwise>
							<siga:ComboBD  nombre="idPais" tipo="pais" ancho="300" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" obligatorio="false"  elementoSel='${idPaisSeleccionado}' pestana='2' accion="cargaCombos(this.value);" />
						</c:otherwise>
					</c:choose>
			
				
			</td>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>&nbsp;(*)</td>
			<td colspan="2">
			<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
				<html:select styleId="provincias" styleClass="${boxComboConsulta}"  
					property="idProvincia" >
					<bean:define id="provincias" name="MutualidadForm"
						property="provincias" type="java.util.Collection" />
					<html:optionsCollection name="provincias" value="idProvincia"
						label="nombre" />
				</html:select>
				</c:when>
				<c:otherwise>
				<html:hidden property="idProvincia" />
				<html:text property="provincia"  styleClass="${estiloText}" ></html:text>
				</c:otherwise>
				</c:choose>
				</td>
		
		</tr>
		<tr>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>&nbsp;(*)</td>
			<td id="tdPoblacionEspanola" colspan="2">
			<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
			
				<html:select styleId="poblaciones" styleClass="boxCombo" style="width:320px;"
					property="idPoblacion" >
					<bean:define id="poblaciones" name="MutualidadForm"
						property="poblaciones" type="java.util.Collection" />
					<html:optionsCollection name="poblaciones" value="idPoblacion"
						label="nombre" />
				</html:select>
				</c:when>
				<c:otherwise>
					<html:text property="poblacion"  styleClass="${estiloText}" ></html:text>
				</c:otherwise>
				
			</c:choose>
			
								
				
			</td> 
			
			<td class="ocultar" colspan="2" id="tdPoblacionExtranjera">
				<html:text property="poblacionExtranjera" style="width:300" maxlength="100" styleClass="${estiloText}" ></html:text>
			</td>
				
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.domicilio"/>&nbsp;(*)</td>
			<td><html:text property="domicilio" size="30" maxlength="100" styleClass="${estiloText}" ></html:text></td>
			
			<td class="labelText" >CP&nbsp;(*)
				<html:text property="codigoPostal" styleClass="${estiloText}" size="5" maxlength="5"  ></html:text></td>
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono1"/>&nbsp;(*)</td>
			<td><html:text property="telef1" maxlength="20" styleClass="${estiloText}" ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono2"/></td>
			<td><html:text property="telef2" maxlength="20" styleClass="${estiloText}" ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono3"/></td>
			<td><html:text property="movil" maxlength="20" styleClass="${estiloText}"  ></html:text></td>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax1"/></td>
			<td><html:text property="fax1" maxlength="20" styleClass="${estiloText}" ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax2"/></td>
			<td><html:text property="fax2" maxlength="20" styleClass="${estiloText}" ></html:text></td>
					
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>&nbsp;(*)</td>
			<td><html:text property="correoElectronico" maxlength="100" styleClass="${estiloText}" ></html:text></td>
		</tr>
		
		
			
			

	</table>
	
	</siga:ConjCampos>
		
	<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera">
		<table class="tablaCampos" align="left" border="0" style="width:80%">	
			<tr align="left">		
				<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/></td>
				<td class="labelText">
					<html:text property="titular"  size="50" styleClass="${estiloText}" maxlength="100"/>
				</td>
				<td class="labelText">IBAN</td>
				<td class="labelText">
					<html:text property="iban"  size="20" styleClass="${estiloText}" maxlength="24"/>
				</td>
				<td class="labelText">SWIFT</td>
				<td class="labelText">
					<html:text property="swift"  size="15" styleClass="${estiloText}" maxlength="12"/>
				</td>
			<tr>

		
			<!-- FILA -->
			<tr>					
				<td class="labelText" nowrap>Cuenta</td>	
				<td class="labelText">
				      <html:text size="4"  maxlength="4" property="cboCodigo"  styleClass="${estiloText}"  onChange="document.MutualidadForm.idBanco.value=document.MutualidadForm.cboCodigo.value"></html:text>
					- <html:text size="4"  maxlength="4" property="codigoSucursal" 	styleClass="${estiloText}" ></html:text>
					- <html:text size="2"  maxlength="2" property="digitoControl"  	styleClass="${estiloText}" ></html:text>
					- <html:text size="10" maxlength="10" property="numeroCuenta"  styleClass="${estiloText}" ></html:text></td>
				
				<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
				<td class="labelText" colspan="3">
					<siga:ComboBD nombre="idBanco" ancho="450" tipo="cmbBancos" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" elementoSel='${idBancoSeleccionado}' accion="document.MutualidadForm.cboCodigo.value=document.MutualidadForm.idBanco.value"/>
				</td>
			</tr>
			<tr align="left">		
				<td class="labelText">Periodicidad de Pago</td>
				<td class="labelTextValor">
					<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
							<html:select styleClass="${estiloCombo}" style="width:200px;" name="MutualidadForm" property="idPeriodicidadPago"  >
								<bean:define id="periodicidadesPago" name="MutualidadForm" property="periodicidadesPago" type="java.util.Map" />
								<html:optionsCollection name="periodicidadesPago" value="key" label="value" />
							</html:select>	
							<html:hidden property="periodicidadPago"/>

						</c:when>
						<c:otherwise>
							<c:out value="${MutualidadForm.periodicidadPago}"></c:out>
						</c:otherwise>
					</c:choose>
										
					
				</td>
				<td colspan="4"></td>
				
			</tr>
			
			
		</table>
	</siga:ConjCampos>
		<siga:ConjCampos>
			<table class="tablaCampos" align="left" border="0">
					<tr>
					<td width="20%"></td>
					<td width="20%"></td>
					<td width="5%"></td>
					<td width="35%"></td>
					<td width="20%"></td>
					</tr>

				<tr>
					<td class="labelText">Opcion de Cobertura</td>
					<td colspan="2" class="labelTextValor">
					<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
					<html:select styleClass="${estiloCombo}"
							styleId="opcionesCobertura" name="MutualidadForm"
							property="idCobertura" style="width:200px;">
							<bean:define id="opcionesCobertura" name="MutualidadForm"
								property="opcionesCobertura" type="java.util.Map" />
							<html:optionsCollection name="opcionesCobertura" value="key"
								label="value" />
						</html:select>
						<html:hidden property="cobertura"/>
						</c:when>
						<c:otherwise>
						<c:out value="${MutualidadForm.cobertura}"></c:out>
						</c:otherwise>
					</c:choose>

					</td>
					
					<td></td>
					<td></td>
				</tr>
				<tr>
				<td colspan="5">
					<table>
					<tr>
						<td class="labelText" style="vertical-align: right">Cuota Mensual</td>
						<td class="labelTextValor"><html:text property="cuotaCobertura" styleClass="boxConsulta" style="text-align:right"/>&euro;</td>
						<td width="10%"></td>
						<td class="labelText">Capital objetivo estimado a los 65 a�os</td>
						<td class="labelTextValor"><html:text property="capitalCobertura" styleClass="boxConsulta" style="text-align:right"/>&euro;</td>
					</tr>
					</table>
				</td>
				
				<tr align="left">

					<td class="labelText">Beneficiarios</td>
					
					<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
					<td colspan = "2" class="labelTextValor">
					<html:select styleClass="${estiloCombo}" 
							name="MutualidadForm" property="idBeneficiario" style="width:500px;" onchange="onchangeBeneficiario();">
							<bean:define id="beneficiarios" name="MutualidadForm"
								property="beneficiarios" type="java.util.Map" />
							<html:optionsCollection name="beneficiarios" value="key"
								label="value" />
						</html:select>
						<html:hidden property="beneficiario"/>
						</td>
						<td colspan = "2" class="labelText" >
						<html:text property="otrosBeneficiarios"  size="40" styleClass="${estiloText}" style="display:none" />
						</td>
						</c:when>
						<c:otherwise>
						<td colspan = "2" class="labelTextValor">
							<c:out value="${MutualidadForm.beneficiario}"></c:out>
						</td>
						<td colspan = "2" class="labelTextValor">
							<c:out value="${MutualidadForm.otrosBeneficiarios}"></c:out>
						</td>
						</c:otherwise>
					</c:choose>

					
					
					</tr>
					<tr align="left">
					<td class="labelText">Asistencia Sanitaria</td>
					<td colspan= "4" class="labelTextValor">
					<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
					<html:select styleClass="${estiloCombo}" 
							name="MutualidadForm" property="idAsistenciaSanitaria">
							<bean:define id="asistenciasSanitarias" name="MutualidadForm"
								property="asistenciasSanitarias" type="java.util.Map" />
							<html:optionsCollection name="asistenciasSanitarias" value="key"
								label="value" />
						</html:select>
						<html:hidden property="asistenciaSanitaria"/>
						</c:when>
						<c:otherwise>
						<c:out value="${MutualidadForm.asistenciaSanitaria}"></c:out>
						</c:otherwise>
					</c:choose>
					</td>

				</tr>


			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="Unidad Familiar">
		<table>
		<tr>
			<td class="labelText" >Fecha Nacimiento conyuge</td>
			<td>
				
				
				<siga:Fecha nombreCampo="fechaNacimientoConyuge" disabled="${MutualidadForm.modo=='consulta'}"></siga:Fecha>
				<c:if test="${MutualidadForm.modo=='insertar'}">
					<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimientoConyuge);"><img src="<html:rewrite page='/html/imagenes/calendar.gif'/>" border="0"> </a>
				</c:if>
			</td>
			<td class="labelText" >Numero de hijos</td>
			<td><html:text
				 size="4"  maxlength="2" property="numeroHijos" 	styleClass="${estiloText}"/>
			</td>
			</tr>
			</table>
			<siga:ConjCampos leyenda="Fecha Naciemineto Hijos">
		<table width="100%">
		
			<tr>
			<td class="labelText" >Edad Hijo 1</td>
			<td >
				<html:text property="edadHijo1"  size="4" styleClass="${estiloText}" maxlength="2"/>
				
			</td>
			<td class="labelText" >Edad Hijo 2</td>
			<td >
				<html:text property="edadHijo2"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 3</td>
			<td >
				<html:text property="edadHijo3"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 4</td>
			<td >
				<html:text property="edadHijo4"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 5</td>
			<td >
				<html:text property="edadHijo5"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			
			</tr>
			<tr>
			<td class="labelText" >Edad Hijo 6</td>
			<td>
				<html:text property="edadHijo6"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 7</td>
			<td>
				<html:text property="edadHijo7"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 8</td>
			<td>
				<html:text property="edadHijo8"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 9</td>
			<td>
				<html:text property="edadHijo9"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" >Edad Hijo 10</td>
			<td>
				<html:text property="edadHijo10"  size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			
			</tr>
			</table>
			</siga:ConjCampos>
	</siga:ConjCampos>
<ajax:updateFieldFromSelect  
	baseUrl="/SIGA${path}.do?modo=getAjaxCuotaCapitalCobertura"
	source="opcionesCobertura" target="cuotaCobertura,capitalCobertura" parameters="idCobertura={idCobertura},idSexo={idSexo},fechaNacimiento={fechaNacimiento}"
	/>
<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxPoblaciones"
	source="provincias" target="poblaciones" parameters="idPais={idPais},idProvincia={idProvincia}"
	preFunction="preAccionPoblacion"
	postFunction="postAccionPoblacion" />
</c:if>


<c:if test="${MutualidadForm.modo=='insertar'}">
		<siga:ConjBotonesAccion botones="G,R,C" clase="botonesDetalle" />
</c:if>
</html:form>

					
						
			
		
	<!-- FIN: BOTONES REGISTRO -->
	

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	

	function accionVolver() {		
	}

 
	function accionRestablecer(){
	
	}

	function accionGuardar(){
		
		sub();
		
		document.MutualidadForm.periodicidadPago.value = document.MutualidadForm.idPeriodicidadPago.options[document.MutualidadForm.idPeriodicidadPago.selectedIndex].text;
		document.MutualidadForm.cobertura.value = document.MutualidadForm.idCobertura.options[document.MutualidadForm.idCobertura.selectedIndex].text;
		document.MutualidadForm.beneficiario.value = document.MutualidadForm.idBeneficiario.options[document.MutualidadForm.idBeneficiario.selectedIndex].text;
		document.MutualidadForm.asistenciaSanitaria.value = document.MutualidadForm.idAsistenciaSanitaria.options[document.MutualidadForm.idAsistenciaSanitaria.selectedIndex].text;
		if(document.MutualidadForm.idPais.value == ""){
			document.getElementById("idPais").value = "191";
		}
		document.MutualidadForm.pais.value = document.MutualidadForm.idPais.options[document.MutualidadForm.idPais.selectedIndex].text;
		document.MutualidadForm.provincia.value = document.MutualidadForm.idProvincia.options[document.MutualidadForm.idProvincia.selectedIndex].text;
		document.MutualidadForm.poblacion.value = document.MutualidadForm.idPoblacion.options[document.MutualidadForm.idPoblacion.selectedIndex].text;
		
		document.MutualidadForm.submit();
		//var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'Esperando respuesta de la mutualidad. Espere por favor...');
		
	}
	function cargaCombos() {      
		
		if(document.getElementById("idPais")){
			var pais = document.MutualidadForm.idPais.value;
		   if (pais!=null && pais!="" && pais!='191'){
			    document.getElementById("idPoblacion").value="";
		   		document.getElementById("idProvincia").value="";
			   	document.getElementById("idProvincia").disabled=true;
				document.getElementById("tdPoblacionEspanola").className="ocultar";
				document.getElementById("tdPoblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExtranjera").value="";
				document.getElementById("idProvincia").disabled=false;
				document.getElementById("tdPoblacionEspanola").className="";
				document.getElementById("tdPoblacionExtranjera").className="ocultar";
				if(document.getElementById("idProvincia").onchange)
					var cmbProvincia = document.getElementById("idProvincia").onchange();
				
				
	       }
		}
	}
	
	
	
	function accionCerrar(){	
		//window.close();
		top.cierraConParametros("");
	}
	
	function accionComprobarEstadoMutualidad()
	{
		document.MutualidadForm.modo.value = "actualizaEstado";
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'Esperando respuesta de la mutualidad. Espere por favor...');
		if(resultado){
			document.MutualidadForm.idEstado.value = resultado[0];
			document.MutualidadForm.estado.value = resultado[1];	
	    	document.getElementById("tdEstadoSolicitud").innerText = resultado[1];

			
		}
		
	}
	function accionComprobarEstadoMutualista()
	{
		document.MutualidadForm.modo.value = "actualizaEstadoMutualista";
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'Esperando respuesta de la mutualidad. Espere por favor...');
		if(resultado){
			document.MutualidadForm.estadoMutualista.value = resultado[0];	
	    	document.getElementById("tdEstadoMutualista").innerText = resultado[0];
		}
	
	}
	function onchangeBeneficiario()
	{
		
		
		var textoBeneficiario = document.MutualidadForm.idBeneficiario.options[document.MutualidadForm.idBeneficiario.selectedIndex].text.toLowerCase();
		if(textoBeneficiario.indexOf("otros")>=0){
			document.getElementById("otrosBeneficiarios").style.display = "block";
		}else{
			document.getElementById("otrosBeneficiarios").style.display = "none";
			
		}
		
	}
	
	
</script>
	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"/>
</body>
	
</html>



