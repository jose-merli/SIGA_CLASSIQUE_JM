<!-- gestionBajasTemporales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>
<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>

<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

<!-- Step 4 -->
 <!-- Importar el js propio de la pagina-->
<script src="<html:rewrite page='/html/js/censo/bajasTemporales.js'/>" type="text/javascript"></script>

<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:TituloExt titulo="menu.sjcs.bajasTemporales"	localizacion="censo.bajastemporales.gestion.localizacion" />

<script type="text/javascript">
jQuery.noConflict();
	function preAccionBusqueda(){
		if (document.BajasTemporalesForm.fechaDesde.value=='') {
				var campo = '<siga:Idioma key="censo.bajastemporales.filtro.fechaDesde"/>';
 				var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
 				alert (msg);
 				fin();				
				return 'cancel';
		}
		 sub();
	}
	function borrar(fila){
		if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
			return borrarFila(fila);
		}
		
	}
	
</script>

</head>

<body  >


<!-- INICIO: CAMPOS DE BUSQUEDA-->
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}"  method="POST" target="mainWorkArea">
<html:hidden property="modo"/>
<html:hidden property="idPersona"/>
<html:hidden property="fichaColegial"/>


		

	<siga:ConjCampos leyenda="censo.bajastemporales.leyenda.busqueda">
		<table width="100%" border="0">
			<tr>
				<td width="20%"></td>
				<td width="30%"></td>
				<td width="15%"></td>
				<td width="35%"></td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma
					key="censo.bajastemporales.situacion" />
				</td>
				<td  align="left">
				<!-- aalg: INC_07989. Que aparezca por defecto todos -->
				<html:select styleId="situacion" property="situacion" onclick="accionComboSituacion();" styleClass="boxCombo" >
					
						<html:option value="T"><siga:Idioma
					key="censo.bajastemporales.situacion.todos" /></html:option>
						<html:option value="B"><siga:Idioma
					key="censo.bajastemporales.situacion.bajaTemporal" />
						</html:option>
						
						<html:option value="A"><siga:Idioma
					key="censo.bajastemporales.situacion.activos" /></html:option>
					
				</html:select>
				</td>
				
				
				<td class="labelText">
					<siga:Idioma
					key="censo.bajastemporales.estadoBaja" />
				</td>
				<td align="left">
				<html:select styleId="estadoBaja" property="estadoBaja"   styleClass="boxCombo" >
					<html:option value="-1"><siga:Idioma key="general.combo.seleccionar"/>
						</html:option>
					
					
						<html:option value="P"><siga:Idioma
					key="censo.bajastemporales.estadoBaja.pendiente" />
						</html:option>
						<html:option value="V"><siga:Idioma
					key="censo.bajastemporales.estadoBaja.aceptado" /></html:option>
						<html:option value="D"><siga:Idioma
					key="censo.bajastemporales.estadoBaja.denegado" /></html:option>
						</html:select>
				</td>
			</tr>
			
			
			<tr>
				<td class="labelText"><siga:Idioma
					key="censo.bajastemporales.turno" /></td>
				<td><html:select styleId="turnos" styleClass="boxCombo" style="width:320px;"
					property="idTurno" >
					<bean:define id="turnos" name="BajasTemporalesForm"
						property="turnos" type="java.util.Collection" />
					<html:optionsCollection name="turnos" value="idTurno"
						label="nombre" />
					</html:select>
				</td>
				<td class="labelText"><siga:Idioma
					key="censo.bajastemporales.guardia" /></td>
				<td><html:select styleId="guardias" styleClass="boxCombo" style="width:320px;"
					property="idGuardia" >
					<bean:define id="guardias" name="BajasTemporalesForm"
						property="guardias" type="java.util.Collection" />
					<html:optionsCollection name="guardias" value="idGuardia"
						label="nombre" />
				</html:select></td>
			</tr>
			
			
			<tr>

				<td colspan="6"><siga:ConjCampos
					leyenda="censo.bajastemporales.colegiado.leyenda">
					<table width="100%">
						<tr>
							<td>


							<table align="left">
								<tr>
									<td class="labelText"><siga:Idioma
										key="censo.bajastemporales.colegiado" /> </td>
									<td><html:text styleId="colegiadoNumero"
										property="colegiadoNumero" size="4" maxlength="9"
										styleClass="box"></html:text></td>
									<td><html:text styleId="colegiadoNombre"
										property="colegiadoNombre" size="50" maxlength="50"
										styleClass="box" readonly="true"></html:text></td>
									<td><!-- Boton buscar --> <input type="button"
										class="button" id="idButton" name="Buscar"
										value="<siga:Idioma
												key="general.boton.search" />"
										onClick="buscarColegiado();"> <!-- Boton limpiar -->
									&nbsp;<input type="button" class="button" id="idButton"
										name="Limpiar"
										value="<siga:Idioma
												key="general.boton.clear" />"
										onClick="limpiarColegiado();"></td>

									<td class="labelText" width="75px" style="text-align: right;">
									
									</td>
									<td>
									</td>
									<td> 
									</td>
								</tr>

							</table>


							</td>


						</tr>
					</table>
					
				</siga:ConjCampos>
			</td>
			</tr>
			<tr>	
				<td colspan= "6">
					<table>
						<tr>		
							<td class="labelText" style="width:15%"><siga:Idioma key="censo.bajastemporales.tipo"/></td>
							<td class="labelText" style="width:30%">
							<html:select property="tipo" styleClass="boxCombo">
								<html:option value=""><siga:Idioma key="general.combo.seleccionar"/></html:option>
								<html:option value="V"><siga:Idioma key="censo.bajastemporales.tipo.vacaciones"/></html:option>
								<html:option value="B"><siga:Idioma key="censo.bajastemporales.tipo.baja"/></html:option>
								<html:option value="M"><siga:Idioma key="censo.bajastemporales.tipo.maternidad"/></html:option>
								<html:option value="S"><siga:Idioma key="censo.bajastemporales.tipo.suspension"/></html:option>
							</html:select>
							</td>
							<td class="labelText" style="width:18%"><siga:Idioma key="censo.bajastemporales.filtro.fechaDesde"/></td>
							<td class="labelText" >
								<siga:Datepicker  nombreCampo= "fechaDesde" campoCargarFechaDesde="fechaHasta"/>
							</td>
				
							<td class="labelText"><siga:Idioma key="censo.bajastemporales.filtro.fechaHasta"/></td>
							<td class="labelText" >
							<siga:Datepicker  nombreCampo= "fechaHasta"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	<table class="botonesSeguido" align="center">
		<tr>
			<td class="titulitos"></td>
				
				<td class="tdBotones">
					
					<input type='button'  id = 'idBusqueda' name='idButton' value='Buscar' alt='Buscar' class='button'>
				</td>
			</tr>
	</table>



<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxGuardias"
	source="turnos" target="guardias" parameters="idTurno={idTurno}"
	/>
<ajax:updateFieldFromField 
	baseUrl="/SIGA${path}.do?modo=getAjaxColegiado"
    source="colegiadoNumero" target="idPersona,colegiadoNumero,colegiadoNombre"
	parameters="colegiadoNumero={colegiadoNumero}" 
	preFunction="preAccionColegiado"
	postFunction="postAccionColegiado"
	/>

<div id="divBajasTemporales" style='height:480px;position:absolute;width:100%; overflow-y:auto'>
	<table id='bajasTemporales' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
	</table>
</div>	
</html:form>

<table class="botonesDetalle" align="center">
<tr>
<td  style="width:900px;">
&nbsp;
</td>
<td class="tdBotones">
<input type="button" alt="<siga:Idioma key="general.boton.new"/>"  id="idButton" onclick="return accionNuevo(false,'<siga:Idioma key="general.message.seleccionar"/>');" class="button" name="idButton" value="<siga:Idioma key="general.boton.new"/>">
</td>
<td class="tdBotones">
<input type="button" alt="<siga:Idioma key="censo.bajastemporales.boton.aceptarSolicitud"/>"  id="idValidarSolicitud" onclick="return accionValidarSolicitud('<siga:Idioma key="general.message.seleccionar"/>');" class="button" name="idValidarSolicitud"  value="<siga:Idioma key="censo.bajastemporales.boton.aceptarSolicitud"/>">
</td>
<td class="tdBotones">
<input type="button" alt="<siga:Idioma key="censo.bajastemporales.boton.denegarSolicitud"/>"  id="idDenegarSolicitud" onclick="return accionDenegarSolicitud('<siga:Idioma key="general.message.seleccionar"/>');" class="button" name="idDenegarSolicitud"  value="<siga:Idioma key="censo.bajastemporales.boton.denegarSolicitud"/>">
</td>
</tr>
</table>


<ajax:htmlContent
	baseUrl="/SIGA${path}.do?modo=getAjaxBusqueda"
	source="idBusqueda"
	target="divBajasTemporales"
	preFunction="preAccionBusqueda"
	postFunction="postAccionBusqueda"
	parameters="idTurno={idTurno},idGuardia={idGuardia},idPersona={idPersona},fechaDesde={fechaDesde},fechaHasta={fechaHasta},tipo={tipo},situacion={situacion},estadoBaja={estadoBaja}"/>


<!-- FIN: CAMPOS DE BUSQUEDA-->
<!-- Formularios auxiliares -->
<html:form action="/CEN_BusquedaClientesModal" method="POST"
	target="mainWorkArea" type="" style="display:none">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
</html:form>
<html:form action="${path}"  name="FormBajasTemporales" type ="com.siga.censo.form.BajasTemporalesForm">
	<html:hidden property="modo"/>
	<html:hidden property="idInstitucion" />
	<html:hidden property="idPersona" />
	<html:hidden property="colegiadoNombre" />
	<html:hidden property="colegiadoNumero" />
	<html:hidden property="fechaAlta" />
	<html:hidden property="fechaDesde" />
	<html:hidden property="fechaHasta" />
	<html:hidden property="tipo" />
	<html:hidden property="datosSeleccionados" />
	<input type="hidden" name="actionModal" />
</html:form>

			
<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"

				style="display: none"></iframe>



</body>


</html>