<!DOCTYPE html>

<!-- alterMutua.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="java.util.ArrayList"%>
<%@ page pageEncoding="ISO-8859-15"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="fmt.tld" prefix="fmt"%>


<html>
<head>

<%String app=request.getContextPath(); %>

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/css/jquery-ui.css"/>">

<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery-ui.js'/>" type="text/javascript"></script>

<style media="screen" type="text/css">
	.ocultar { display: none }
	
	.red { color: "red" }
	
    fieldset{
    	padding-bottom:10px;
    }
    legend{
    	border:1 black;
    	padding-left:10px;
    	padding-right:10px;
    }
    .defaultText {}
    .defaultTextActive { color: #a1a1a1; font-style: italic; }
    .requiredText {}
    .requiredTextActive { color: #8a8a8a; font-weight:bold;}
    .obligatorio {background: #F7D5E3;}
        
</style>

<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:Titulo titulo="censo.alterMutua.titulo" />
<!-- FIN: TITULO Y LOCALIZACION -->

</head>


<body class="tablaCentralCampos">

<div id="datosSolicitud" style="display:none; height:600px; overflow-y:auto">
	<fmt:setLocale value="es_ES"/>
	<html:form action="/CEN_AlterMutua" method="POST">
		<html:hidden property="apellidos"/>
		<html:hidden property="codigoPostal"/>
		<html:hidden property="comunicacionPreferente"/>
		<html:hidden property="domicilio"/>
		<html:hidden property="email"/>
		<html:hidden property="estadoCivil"/>
		<html:hidden property="fax"/>
		<html:hidden property="fechaNacimiento"/>
		<html:hidden property="identificador"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idPaquete"/>
		<html:hidden property="movil"/>
		<html:hidden property="msgRespuesta"/>
		<html:hidden property="nombre"/>
		<html:hidden property="poblacion"/>
		<html:hidden property="sexo"/>
		<html:hidden property="idSexo"/>
		<html:hidden property="telefono1"/>
		<html:hidden property="telefono2"/>
		<html:hidden property="tipoEjercicio"/>
		<html:hidden property="idTipoIdentificacion"/>
		<html:hidden property="idSolicitudalter"/>
		<html:hidden property="identificador"/>
		<html:hidden property="titular"/>
		<html:hidden property="herederos"/>
		<html:hidden property="familiares"/>
		<html:hidden property="requiereBeneficiarios"/>
		<html:hidden property="requiereFamiliares"/>
		<html:hidden property="modo"/>
		<html:hidden property="brevePropuesta"/>
		<html:hidden property="tarifaPropuesta"/>
		<html:hidden property="codigoInstitucion"/>
		<html:hidden property="idSolicitud"/>
		<html:hidden property="idTipoEjercicio"/>
		<html:hidden property="propuesta"/>
		<html:hidden property="numeroPropuestas"/>
		<html:hidden property="idPersona"/>

		<input type="hidden" name="actionModal" value="">
		
		<c:set var="estiloText" value="box" />
		<c:set var="estiloCombo" value="boxCombo" />

		<c:if test="${AlterMutuaForm.idSolicitudalter!=null || AlterMutuaForm.numeroPropuestas <= 0}">
			<c:set var="estiloText" value="boxConsulta" />
			<c:set var="estiloCombo" value="boxComboConsulta" />
		</c:if>

		<fieldset><legend>Selección de propuesta</legend>
		<table>
		<tr>
			<td class="labelText">Modalidad de contratación</td>
		<c:if test="${AlterMutuaForm.numeroPropuestas > 0}">
			<td class="labelTextValue">
			<html:select styleId="propuestaSeleccionada" styleClass="${estiloCombo}" property="propuesta" >
				<html:optionsCollection name="AlterMutuaForm" property="propuestas" label="nombre" value="idPaquete" />
			</html:select>
			</td>
			<td><img id="botonConsulta" src="<%=app%>/html/imagenes/bconsultar_on.gif" style="cursor:hand;"></td>
			<td class="labelText">Tarifa</td>
			<td class="labelTextValue"> <div id="holderTarifa"/> </td>
		</c:if>
		<c:if test="${AlterMutuaForm.numeroPropuestas <= 0}">
			<td class="labelTextValue">No se han recibido datos de Alter Mutua</td>
		</c:if>
		</tr>
		</table>
		<table id="tablaPropuestas" style="display:none">
			<c:forEach items="${AlterMutuaForm.propuestas}" var="p" varStatus="i">
			<tr class="labelTextValue">
				<td class="colorImpar" id="propuesta${i.index}" width="200px"><div class="arrow">${p.nombre}</div></td>
				<td id="seleccionada${i.index}"><input type="radio" name="grupoPropuestas" value="${i.index}"/></td>
				<td id="breve${i.index}"></td>
				<td id="idPaquete${i.index}">${p.idPaquete}</td>
				<td id="familiares${i.index}">${p.familiares}</td>
				<td id="herederos${i.index}">${p.herederos}</td>
				<td id="tarifa${i.index}" colspan="2">
					<fmt:formatNumber type="NUMBER" minFractionDigits="2" maxFractionDigits="2"><c:out value="${p.tarifa}"/></fmt:formatNumber>¤
				</td>
				<td id="descripcion${i.index}">${p.descripcion}</td>
			</tr>
			</c:forEach>
		</table>
	
		</fieldset>

		<!--c:if test="${AlterMutuaForm.idSolicitudalter!=null}"-->
		<fieldset id="estadoSolicitud" ><legend>Estado de la solicitud</legend>
		<table>
		<tr>
			<td class="labelText">Solicitud</td>
			<td class="labelTextValue" id="holderIdSolicitudAlter">${AlterMutuaForm.idSolicitudalter}</td>
			<td><input type="button" alt=""  id="idButton" onclick="return consultarEstado();" class="button" name="idButton" value="Consultar estado"></td>
		</tr>
		</table>
		</fieldset>
		<!--/c:if-->
			
		<fieldset><legend>Datos Personales</legend>
			<table>
				<tr>
					<td class="labelText">Identificador</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoIdentificacion}" />&nbsp;<c:out value="${AlterMutuaForm.identificador}" /></td>
					
					<td class="labelText">Nombre</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.nombre}" /></td>
					
					<td class="labelText">Apellidos</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.apellidos}" /></td>
					
					<td class="labelText">Fecha Nacimiento</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.fechaNacimiento}" /></td>
				</tr>
				<tr>
					
					<td class="labelText">Sexo</td>
					<td class="labelTextValor"> <c:out value="${AlterMutuaForm.sexo}" /> </td>
					
					<c:if test="${AlterMutuaForm.estadoCivil!=''}">
						<td class="labelText">Estado Civil</td>
						<td class="labelTextValor">
							<c:out value="${AlterMutuaForm.estadoCivil}" />
							<html:hidden property="idEstadoCivil"/>
						</td>
					</c:if>
					<c:if test="${AlterMutuaForm.estadoCivil==''}">
						<td class="labelText">Estado Civil (*)</td>
						<td class="labelTextValor">
						<html:select styleClass="${estiloCombo} requiredText" property="idEstadoCivil" >
							<html:optionsCollection name="AlterMutuaForm" property="estadosCiviles" label="value" value="key" />
						</html:select>
						</td>
					</c:if>
					</td>
					
					<td class="labelText">Colegio</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.colegio}" /></td>
					
					<td class="labelText">Situación ejercicio</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoEjercicio}" /></td>
				</tr>
			</table>
		</fieldset>
			

		
		
		<fieldset><legend>Contacto</legend>
			<table>
				<tr>
					<td class="labelText">Medio de Comunicación (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="tipoComunicacion" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposComunicacion" label="value" value="key" />
						</html:select>
					</td>
					<td class="labelText">Idioma (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="idioma" >
						<html:optionsCollection name="AlterMutuaForm" property="idiomas" label="value" value="key" />
						</html:select>
					</td>
					
					<td class="labelText" colspan="2">Acepta publicidad&nbsp;<html:checkbox property="admitePublicidad"></html:checkbox> </td>
				</tr>
				<tr>
					<td class="labelText">Teléfono (*)</td>
					<td class="labelTextValue"><html:text property="telefono1" maxlength="14" styleClass="${estiloText} requiredText"></html:text> </td>

					<td class="labelText">Teléfono 2</td>
					<td class="labelTextValue"><html:text property="telefono2" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>

					<td class="labelText">Móvil</td>
					<td class="labelTextValue"><html:text property="movil" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
				</tr>
				<tr>
					<td class="labelText">Fax</td>
					<td class="labelTextValue"><html:text property="fax" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
					
					<td class="labelText">Correo Electrónico (*)</td>
					<td class="labelTextValue" colspan="3"><html:text property="correoElectronico" maxlength="100" styleClass="${estiloText} requiredText" style="width:100%"></html:text> </td>
				</tr>
			</table>

			<fieldset><legend>Dirección</legend>
				<table>
					<tr>
						<td class="labelText">Domicilio (*)</td>
						<td class="labelTextValue"><html:text property="domicilio" size="30" maxlength="200" styleClass="${estiloText} requiredText"></html:text></td>
						
						<td class="labelText">Código Postal (*)</td>
						<td class="labelTextValue"> <html:text property="codigoPostal" styleClass="${estiloText} requiredText" size="5" maxlength="5"></html:text></td>
					</tr>
					
					<tr>
						<td class="labelText">Pais (*)</td>
						<td class="labelTextValue">
							<html:select styleId="selectPais" styleClass="${estiloCombo} requiredText" property="idPais" >
							<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
							</html:select>
						</td>

						<td class="labelText">Provincia (*)</td>
						<td class="labelTextValue" id="tdProvincia">
							<html:select styleId="selectProvincia" styleClass="${estiloCombo} requiredText" property="idProvincia" >
							<html:optionsCollection name="AlterMutuaForm" property="provincias" label="value" value="key" />
							</html:select>
						</td>
						
					</tr>
					<tr>

						<td class="labelText">Población (*)</td>
						<td class="labelTextValue" id="tdPoblacion"><html:text property="poblacion" style="width:300px" maxlength="50" styleClass="${estiloCombo} requiredText"></html:text></td>
						
						<td class="labelText">Tipo de Dirección </td>
						<td class="labelTextValue">
							<html:select styleClass="${estiloCombo}" property="tipoDireccion" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposDireccion" label="value" value="key" />
							</html:select>
						</td>
					</tr>

				</table>
			</fieldset>
			
		</fieldset>

		<fieldset><legend>Cuenta bancaria</legend>
		<table>
			<tr>
				<td class="labelText" nowrap>Pais cuenta bancaria</td>
				<td class="labelTextValue">
					<html:select styleId="selectPaisCuenta" styleClass="${estiloCombo} requiredText" property="idPaisCuenta" >
						<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
					</html:select>
				</td>
				<td class="cuentaNacional labelText" nowrap>Número de cuenta</td>
				<td class="cuentaNacional">
					<html:text size="4"  maxlength="4" property="cboCodigo" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="4"  maxlength="4" property="codigoSucursal" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="2"  maxlength="2" property="digitoControl" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="10" maxlength="10" property="numeroCuenta" styleClass="${estiloText} requiredText"></html:text>
				</td>

				<td class="cuentaExtranjera labelText" nowrap>IBAN</td>	
				<td class="cuentaExtranjera labelText">
				      <html:text size="30" styleId="IBAN" maxlength="30" property="iban" styleClass="${estiloText} requiredText"></html:text>
				</td>
				
				<td class="cuentaExtranjera labelText" nowrap>SWIFT</td>	
				<td class="cuentaExtranjera labelText">
				      <html:text size="11"  maxlength="11" property="swift" styleClass="${estiloText} requiredText"></html:text>
				</td>
			</tr>
		</table>
		</fieldset>		

		
		<fieldset id="fieldsetFamiliares">
			<legend>Familiares 
				<html:button id="nuevaPersona" property="idButton" onclick="return nuevoFamiliar();" styleClass="button">Añadir</html:button>
			</legend>
			<div class="familiares">
			</div>	
		</fieldset>

		<fieldset id="fieldsetBeneficiarios">
			<legend>Beneficiarios</legend>
			<div class="beneficiarios">
			<table>
				<tr>
					<td class="labelText">
						Seleccione los beneficiarios en caso de fallecimiento
					</td>
					<td class="labelTextValue">
						<html:select styleId="selectBeneficiarios" styleClass="${estiloCombo} requiredText" property="selectBeneficiarios" >
							<html:option value="1">Herederos Legales</html:option>
							<html:option value="3">Otros</html:option>
						</html:select>
					</td>
					<td>
						<html:button styleId="botonBeneficiario" id="nuevoBeneficiario" property="idButton" onclick="return nuevoBeneficiario();" styleClass="button">Añadir</html:button>
					</td>
				</tr>
			</table>
			</div>	
		</fieldset>
		
		<div style="display:none">
		
		<div class="personaFamiliar">
			<span>
				<table>
					<tr>
						<td><html:select styleClass="defaultText" property="parentesco" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposParentesco" label="value" value="key" />
							</html:select> 
						</td>

						<td>
							<html:select styleClass="defaultText" property="sexo" >
							<html:optionsCollection name="AlterMutuaForm" property="sexos" label="value" value="key" />
							</html:select>
						</td>

						<td><html:text styleId="nombre" styleClass="requiredText" style="width:140px" property="nombreWSPersona" title="Nombre (*)" maxlength="100"></html:text></td>

						<td><html:text styleId="apellidos" styleClass="requiredText" style="width:140px" property="apellidosWSPersona" title="Apellidos (*)" maxlength="100"></html:text></td>

						<td><html:select styleClass="defaultText" property="tipoIdentificador" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposIdentificacion" label="value" value="key" />
							</html:select> 
						</td>

						<td><html:text styleId="identificador" styleClass="defaultText" style="width:100px" property="identificadorWSPersona" title="Identificador" maxlength="100"></html:text></td>

						<td><html:text style="width:110px" styleClass="datepicker requiredText" property="fechaNacimientoWSPersona" title="F.Nacimiento (*)" maxlength="10"></html:text></td>

						<td class="labelText"><a href="#" class="eliminar">Eliminar</a></td>
				</table>
				</tr>
			</span>
		</div>
		<div class="personaBeneficiario">
			<span>
				<table>
					<tr>
						<td>
							<html:select styleClass="defaultText" property="parentesco" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposParentesco" label="value" value="key" />
							</html:select> 
						</td>

						<td>
							<html:select styleClass="defaultText" property="sexo" >
							<html:optionsCollection name="AlterMutuaForm" property="sexos" label="value" value="key" />
							</html:select>
						</td>
						
						<td><html:text styleId="nombre" styleClass="requiredText" style="width:140px" property="nombreWSPersona" title="Nombre (*)" maxlength="100"></html:text></td>

						<td><html:text styleId="apellidos" styleClass="requiredText" style="width:140px" property="apellidosWSPersona" title="Apellidos (*)" maxlength="100"></html:text></td>
 
						<td>
							<html:select styleClass="defaultText" property="tipoIdentificador" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposIdentificacion" label="value" value="key" />
							</html:select> 
						</td>

						<td><html:text styleId="identificador" styleClass="defaultText" style="width:100px" property="identificadorWSPersona" title="Identificador" maxlength="100"></html:text></td>

						<td><html:text style="width:110px" styleClass="datepicker requiredText" property="fechaNacimientoWSPersona" title="F.Nacimiento (*)" maxlength="10"></html:text></td>
						
						<td class="labelText"><a href="#" class="eliminar">Eliminar</a></td>
				</table>
				</tr>
			</span>
		</div>
		
		</div>
		
		<fieldset class="labelTextValue" id="fieldsetBeneficiarios">
			<legend>Observaciones</legend>
			<html:textarea styleClass="box" property="observaciones" rows="4" style="width:99%"></html:textarea>
		</fieldset>
			
	</html:form>
</div>


	<table class="botonesDetalle" align="center">
	<tr>
	<td  style="width:900px;">
	&nbsp;
	</td>
	<td class="tdBotones">
	<input type="button" alt="" id="idButtonSolicitar" onclick="return accionGuardar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.solicitarCompra"/>">
	</td>
	<td class="tdBotones">
	<input type="button" alt=""  id="idButtonActualizar" onclick="return accionActualizar();" class="button" name="idButton" value="Actualizar tarifa">
	</td>
	</tr>
	</table>


	<div id="dialog-message" title="SIGA" style="vertical-align: top; "></div>
	
	<script>
	function refrescarLocal(){
		window.location.reload(true);
	}
	
	function accionVolver() {		
		document.forms[0].action="./CEN_SolicitudesIncorporacion.do";	
		document.forms[0].target="mainWorkArea";
		document.forms[0].modo.value="";
		document.forms[0].submit();
	}
	
	
	function accionCerrar(){
		top.cierraConParametros();
	}
	
	function rellenaPersonas(){
			var valido = true;
			var familiares = "";
			// Bucle familiares
			$('.fam').each(function(index) {
				if(isPersonaValida($(this))){
					familiares += addPersona($(this)) + "%%%";
				}else{
					valido=false;
				}
			});
			document.AlterMutuaForm.familiares.value = familiares;
			// Bucle beneficiarios
			var beneficiarios = "";
			$('.ben').each(function(index) {
				if(isPersonaValida($(this))){
					beneficiarios += addPersona($(this)) + "%%%";
				}else{
					valido=false;
				}
			});
			document.AlterMutuaForm.herederos.value = beneficiarios;
			
			return valido;
	}
	
	function validarFormulario(){
		var val = true;
		// Para cada campo obligatorio ...
		$('.requiredText').each(function(index){
			// Si el campo esta vacio
			if($(this).is(':visible') && ( $(this).val()==='' || $(this).val()==='-1')){ 
				val = false;
				$(this).addClass("obligatorio");
			}
			val = val && true;
		});
		$('.ben').find('.requiredText').each(function(index){
			// Si el titulo es igual que el valor (no se ha rellenado)
			if($(this).attr('title') === $(this).val()){ 
				val = false;
				$(this).addClass("obligatorio");
			}
			val = val && true;
		});
		$('.fam').find('.requiredText').each(function(index){
			// Si el titulo es igual que el valor (no se ha rellenado)
			if($(this).attr('title') === $(this).val()){ 
				val = false;
				$(this).addClass("obligatorio");
			}
			val = val && true;
		});
		if($('#selectPais').val()==='00') $('#selectPais').val('724');
		if($('#selectPaisCuenta').val()==='00') $('#selectPaisCuenta').val('724');
		if($('#selectPais').val()==='724' && $('#selectProvincia').val()==='00'){
			$('#selectProvincia').addClas('obligatorio');
			val=false;
		}
		if($('#selectPaisCuenta').val()==='724' && document.AlterMutuaForm.digitoControl.value != obtenerDigitoControl("00" + document.AlterMutuaForm.cboCodigo.value + document.AlterMutuaForm.codigoSucursal.value) + "" + obtenerDigitoControl(document.AlterMutuaForm.numeroCuenta.value)){
			$('#tdCuenta').find('.requiredText').each(function(){
				$(this).addClass("obligatorio");
			});
			val=false;
		}
		return val;
	}
	
	function accionGuardar(){
		sub();
		if(validarFormulario() && rellenaPersonas()){
			$.ajax({
	            type: "POST",
	            url: "/SIGA/CEN_AlterMutua.do?modo=insertar",
	            data: $('form').serialize(),
	            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	            success: function(json){
            		jAlert(json.mensaje,400);
            		document.AlterMutuaForm.idSolicitudalter.value = json.idSolicitud;
            		$("#holderIdSolicitudAlter").text(json.idSolicitud);
            		showEstado();
            		makeReadOnly();
			        fin();
	            },
	            error: function(e){
	                alert('Error de comunicación: ' + e);
			        fin();
	            }
	        });
		}else{
			alert("Rellene correctamente los campos obligatorios");
			fin();
		}
	}
	
	function accionActualizar(){
		sub();
		if(validarFormulario() && rellenaPersonas()){
	        $.ajax({
	            type: "POST",
	            url: "/SIGA/CEN_AlterMutua.do?modo=getTarifa",
	            data: $('form').serialize(),
	            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	            success: function(json){
	            	jAlert(json.breve,600,300);
	            	if(!json.error){
		            	$('#holderTarifa').text(json.tarifa);
	            	};
			        fin();
	            },
	            error: function(e){
	                alert('Error de comunicación: ' + e);
			        fin();
	            }
	        });
		}else{
			alert("Rellene correctamente los campos obligatorios");
			fin();
		}
	}
	
	function consultarEstado(){
		sub();
        $.ajax({
            type: "POST",
            url: "/SIGA/CEN_AlterMutua.do?modo=getEstado",
            data: $('form').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            success: function(json){
           		jAlert(json.mensaje,400,300);
		        fin();
            },
            error: function(e){
                alert('Error de comunicación: ' + e);
		        fin();
            }
        });
	}
	
	function isPersonaValida($per){
		var val = true;
		$per.find('.requiredText').each(function(index){
			if($(this).attr('title') === $(this).val()){ 
				val = false;
				$(this).addClass("obligatorio");
			}
			val = val && true;
		});
		return val;
	}
	
	
	function nuevoBeneficiario(){
		$('.personaBeneficiario').clone().attr("class","ben").appendTo('.beneficiarios');
        $('.eliminar').live('click', function() { 
            $(this).parents('span').remove();
  		});
        setTexts();
	};
	
	function nuevoFamiliar(){
		$('.personaFamiliar').clone().attr("class","fam").appendTo('.familiares');
        $('.eliminar').live('click', function() { 
            $(this).parents('span').remove();
            removeFamiliaresBeneficiarios();
  		});
        addFamiliaresBeneficiarios();
        setTexts();
	};
	
	function addFamiliaresBeneficiarios(){
		if($("#selectBeneficiarios option[value=2]").length <= 0){
			$("#selectBeneficiarios").append('<option value="2">Familiares</option>');
			$("#selectBeneficiarios option[value=2]").insertBefore("#selectBeneficiarios option[value=3]");
		}
	}
	
	function removeFamiliaresBeneficiarios(){
		if($(".fam :visible").length == 0){
			$("#selectBeneficiarios option[value=2]").remove();
		}
	}
	
    function setTexts(){
		$(".defaultText").focus(function(){
			if ($(this).val() == $(this)[0].title){
				$(this).removeClass("defaultTextActive");
				$(this).val("");
			}
		});
		$(".defaultText").blur(function(){
			if ($(this).val() == ""){
				$(this).addClass("defaultTextActive");
				$(this).val($(this)[0].title);
			}
		});
		$(".defaultText").blur();
		$(".requiredText").focus(function(srcc){
			if ($(this).val() == $(this)[0].title){
				$(this).removeClass("requiredTextActive");
				$(this).removeClass("obligatorio");
				$(this).val("");
			}
		});
		$(".requiredText").blur(function(){
			if ($(this).val() == ""){
				$(this).addClass("requiredTextActive");
				$(this).val($(this)[0].title);
			}
		});
		$(".requiredText").click(function(){
			$(this).removeClass("requiredTextActive");
			$(this).removeClass("obligatorio");
		});
		$(".requiredText").blur();
		
		$(".datepicker").live('hover', function() {
			$(this).datepicker({
				yearRange: '-100:+0',
    			changeMonth: true,
    			changeYear: true,
    			regional: "es",
    			beforeShow: function (textbox, instance) {   
                    instance.dpDiv.css({
                        marginTop: (-280) + 'px'
                    });
    			}});
		});       
    	$(function() {
    		$( "#datepicker" ).datepicker({
    			appendText: '(yyyy-mm-dd)',
    			changeMonth: true,
    			changeYear: true
    		});
    	});
	};
	
	// Pasa una persona a un string separado por &&
	// Lo hace en orden de aparicion; 
	//		Parentesco, Sexo, Nombre, Apellidos, Identificador, Fnacimiento
	function addPersona($per){
		var persona = "";
		$per.find('select').each(
			function(ind){
				persona+= $(this).val() + "&&";
			}
		);
		$per.find('input').each(
			function(ind){
				if($(this).attr('title') === $(this).val()){
					persona+= " &&";
				}else{
					persona+= $(this).val() + "&&";
				}
			}
		);
		return persona;
	}
	
   	function showEstado(){
   		if(AlterMutuaForm.idSolicitudalter.value)
      		$("#estadoSolicitud").show();
   		else
      		$("#estadoSolicitud").hide();	
   	}

    
    function makeReadOnly(){
    	$(":input").prop("readonly", true);
    	$(":checkbox").prop("disabled", true);
    	
   		$("select").each(function(){
   			$(this).after($(this).find("option:selected").text());
   			$(this).hide();
   		});
   		$('#fieldsetBeneficiarios').hide();
   		$('#fieldsetFamiliares').hide();
   		$('#idButtonSolicitar').hide();
   		$('#idButtonActualizar').hide();
   		$('.box').addClass("boxConsulta");
   		$('.box').removeClass("box");
   		$('.boxCombo').addClass("boxComboConsulta");
   		$('.boxCombo').removeClass("boxCombo");
   		
    }
    
    $(document).ready(function(){
	      	
    	$('#datosSolicitud').height($(window).height()-25);
    	
  	   $("#botonConsulta").click(
 			function(){
 				var selec = $('#propuestaSeleccionada').prop("selectedIndex");
 				var texto =  $("#descripcion"+selec).html();
 				jAlert(texto, 600, 600);
 			}
 		);
         	
       	$("#propuestaSeleccionada").change(
   			function(){
   				var selec = $(this).prop("selectedIndex");
   				var idPaquete =  $("#idPaquete"+selec).text();
   				var breve =  $("#breve"+selec).text();
           		var familiares =  $("#familiares"+selec).text();
           		var herederos =  $("#herederos"+selec).text();
           		var tarifa =  $("#tarifa"+selec).text();
           		document.AlterMutuaForm.idPaquete.value = idPaquete;
           		$("#holderTarifa").text(tarifa);
           		$("#holderBreve").text(breve);
           		if(herederos==="true"){  
           			$("#fieldsetBeneficiarios").show();
           			AlterMutuaForm.requiereBeneficiarios.value = true;
           		}else{
           			$("#fieldsetBeneficiarios").hide();
           			AlterMutuaForm.requiereBeneficiarios.value = false;
           		}
           		
           		if(familiares==="true"){
           			$("#fieldsetFamiliares").show();
           			AlterMutuaForm.requiereFamiliares.value = true;
           		}else{
           			$("#fieldsetFamiliares").hide();
           			AlterMutuaForm.requiereFamiliares.value = false;
           		}
   			}
       	);
       	$("#propuestaSeleccionada").trigger('change');
       	
       	$("#selectPais").change(function(){
       		if(($(this).val()==="00")||$(this).val()==="724"){
       			$("#tdProvincia").attr('disabled',false);
       		}else{
       			$("#tdProvincia").attr('disabled',true);
       			$("#selectProvincia").val('00');
       		}
       	});
       	$("#selectPais").trigger('change');
       	
       	$("#selectPaisCuenta").change(function(){
       		if(($(this).val()==="00")||$(this).val()==="724"){
       			$(".cuentaExtranjera").hide();
       			$(".cuentaNacional").show();
       		}else{
       			$(".cuentaExtranjera").show();
       			$(".cuentaNacional").hide();
       		}
       	});
       	$("#selectPaisCuenta").trigger('change');
       	
       	$("#selectBeneficiarios").change(function(){
       		if($("#selectBeneficiarios").val()==3){
       			$("#botonBeneficiario").show();
       		}else{
       			$("#botonBeneficiario").hide();
       		}
       	});
       	$("#selectBeneficiarios").trigger('change');
        	
        	
      	showEstado();

       	if(AlterMutuaForm.idSolicitudalter.value || AlterMutuaForm.numeroPropuestas.value<=0){
       		makeReadOnly();
       	}
       		
       	if(AlterMutuaForm.idSolicitudalter.value==='' && AlterMutuaForm.numeroPropuestas.value<=0){
       		jAlert(AlterMutuaForm.msgRespuesta.value,400,300);
       	}
       	
     	setTexts();
     	
     	$('#datosSolicitud').show();

   		
     });
    
	function jAlert(texto, ancho, alto){
		$("#dialog-message").html(texto);
		$("#dialog-message").height(alto);
		$("#dialog-message").dialog({
			modal: true,
			resizable: false,
			width: ancho,
			height: alto,
			buttons: { "Ok": function() { $(this).dialog("close"); $(this).dialog("destroy"); } }
		});
		$("#dialog-message").scrollTop(0);
	}
     	
	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none" />
</body>

</html>



