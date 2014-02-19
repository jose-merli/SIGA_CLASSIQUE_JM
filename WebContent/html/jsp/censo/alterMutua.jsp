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
<script>
function habilitarCampos(isHabilitar) {
		
		if(isHabilitar==true){
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="checkbox") {
					jQuery.removeAttr(input,"disabled");
					//$(input).removeAttr("disabled");
				}else if(input.type=="button") {
					jQuery.removeAttr(input,"disabled");
					
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


			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i < inputs.length ; i++) {
				var input = inputs[i];
				if(input.type=="checkbox"){
					jQuery.attr(input,"disabled","disabled");
					//$(input).attr("disabled","disabled");
				}else if(input.type=="button") {
					//jQuery.attr(input,"disabled","disabled");
					$('#botonNuevoFamiliar').hide();
					
				}else if(input.type!="button"){
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
	
	function cargarBancoPorIBAN(){		
		mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";	
		var iban = formateaMask(document.getElementById("IBAN").value);	
		if (iban!=undefined && iban!="") {
			jQuery.ajax({ //Comunicacion jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBancoBIC",
				data: "iban="+iban,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){	
					if(json!=null && json.pais != null){
						if(json.pais == "ES"){								
							//Se comprueba si el banco existe
							if(json.banco != null){
								var bic = json.banco.bic;
								document.getElementById("SWIFT").value=bic;
								document.getElementById("SWIFT").readOnly = true;
								document.getElementById("SWIFT").className = "boxConsulta";
							
							} else {
								alert(mensaje);
								document.getElementById("SWIFT").value="";
								document.getElementById("SWIFT").readOnly = true;
								document.getElementById("SWIFT").className = "boxConsulta";
								fin();
							}
							
						}else{
							document.getElementById("SWIFT").readOnly = false;
							document.getElementById("SWIFT").className = "box";
							document.getElementById("SWIFT").value="";
							alert("Rellene el SWIFT para el banco extranjero");
						}
						
					}else{
						alert(mensaje);
						document.getElementById("SWIFT").value="";
						document.getElementById("SWIFT").readOnly = true;
						document.getElementById("SWIFT").className = "boxConsulta";
					}
					fin();
				},
				error: function(e){
					alert(mensaje);
					document.getElementById("SWIFT").value="";
					document.getElementById("SWIFT").readOnly = true;
					document.getElementById("SWIFT").className = "boxConsulta";
					fin();
				}
			});
			
		} else {
			document.getElementById("IBAN").value="";
			document.getElementById("SWIFT").value="";
			document.getElementById("SWIFT").readOnly = true;
			document.getElementById("SWIFT").className = "boxConsulta";
		}
	}	
	
	function inicioCargarBancoBIC(){
		var iban = formateaMask(document.getElementById("IBAN").value);
		var codigoBanco = "${AlterMutuaForm.cboCodigo}";
		if (iban!=undefined && iban!="") {			
			jQuery.ajax({ //Comunicacion jQuery hacia JSP  
					type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxCargaInicialBancoBIC",
				data: "iban="+iban+"&codigo="+codigoBanco,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){	
					if(json.banco!=null && json.banco!=""){
						document.getElementById("SWIFT").value=json.banco.bic;
					}
					fin();
				},
				error: function(e){
					alert(mensaje);
					fin();
				}
			});
		}
	}
	
	
	function rpad() {
		if (document.getElementById("SWIFT").value.length == 8){
	    	while (document.getElementById("SWIFT").value.length < 11)
	    		document.getElementById("SWIFT").value = document.getElementById("SWIFT").value + 'X';
		}
	}

</script>

<%String app=request.getContextPath(); %>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/css/jquery-ui.css"/>">

<script src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery-1.7.1.js'/>" type="text/javascript"></script>
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
	<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.solicitudIncorporacion.localizacion"/>
	<%if(path.toString().contains("Ficha")){ %>
		<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.fichaCliente.localizacion"/>
	<%}%>
	
<!-- FIN: TITULO Y LOCALIZACION -->

</head>


<body class="tablaCentralCampos" onload="inicioCargarBancoBIC();">

<div id="datosSolicitud" style="display:none; height:600px; overflow-y:auto">
	<fmt:setLocale value="es_ES"/>
	<html:form action="/CEN_AlterMutua" method="POST">
		<html:hidden property="apellidos"/>
		<html:hidden property="codigoPostal"/>
		<html:hidden property="comunicacionPreferente"/>
		<html:hidden property="domicilio"/>
		<html:hidden property="email"/>
		<html:hidden property="estadoCivil"/>
		<html:hidden property="fechaNacimiento"/>
		<html:hidden property="identificador"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="msgRespuesta"/>
		<html:hidden property="nombre"/>
		<html:hidden property="sexo"/>
		<html:hidden property="idSexo"/>
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

		<html:hidden property="tarifaPaquete"/>
		<html:hidden property="brevePaquete"/>
		<html:hidden property="descripcionPaquete"/>
		<html:hidden property="nombrePaquete"/>
		
		<input type="hidden" name="actionModal" value="">
		
		<c:set var="estiloText" value="box" />
		<c:set var="estiloCombo" value="boxCombo" />
		<c:set var="modoConsulta" value="false" />

		<c:if test="${AlterMutuaForm.idSolicitudalter!=null || AlterMutuaForm.numeroPropuestas <= 0}">
			<c:set var="estiloText" value="boxConsulta" />
			<c:set var="estiloCombo" value="boxComboConsulta" />
			<c:set var="modoConsulta" value="true" />
		</c:if>

		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.seleccionPropuesta"/></legend>

			<c:if test="${AlterMutuaForm.nombrePaquete!=null && AlterMutuaForm.nombrePaquete!=''}">
			<table>
			<tr>
				<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.modalidadContratacion"/></td>
				<td class="labelTextValue">${AlterMutuaForm.nombrePaquete}</td>
				<td><img id="botonConsultaPaquete" src="<%=app%>/html/imagenes/bconsultar_on.gif" style="cursor:hand;"></td>
				<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.tarifa"/></td>
				<td class="labelTextValue" align="right">${AlterMutuaForm.tarifaPaquete}</td>
				<td class="labelTextValue" align="left">¤</td>
			</tr>
			</table>
			<table id="tablaPropuestas" style="display:none">
				<tr class="labelTextValue">
					<td id="propuesta0" width="200px">${AlterMutuaForm.nombrePaquete}</td>
					<td id="breve0">${AlterMutuaForm.brevePaquete}</td>
					<td id="tarifa0">${AlterMutuaForm.tarifaPaquete}</td>
					<td id="descripcion0">${AlterMutuaForm.descripcionPaquete}</td>
				</tr>
			</table>
			</c:if>
			
			<c:if test="${AlterMutuaForm.nombrePaquete==null || AlterMutuaForm.nombrePaquete==''}">
				<c:if test="${AlterMutuaForm.numeroPropuestas > 0}">
				<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.modalidadContratacion"/></td>
					<td class="labelTextValue">
					<html:select styleId="propuestaSeleccionada" styleClass="${estiloCombo}" property="idPaquete" >
						<html:optionsCollection name="AlterMutuaForm" property="propuestas" label="nombre" value="idPaquete" />
					</html:select>
					</td>
					<td><img id="botonConsulta" src="<%=app%>/html/imagenes/bconsultar_on.gif" style="cursor:hand;"></td>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.tarifa"/></td>
					<td class="labelTextValue" style="text-align:right"><div id="holderTarifa"/></td>
					<td class="labelTextValue" style="text-align:left">¤</td>
				</c:if>
				<c:if test="${AlterMutuaForm.numeroPropuestas <= 0}">
				<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.noHayDatos"/></td>
				</tr>
				</table>
				</c:if>
				</tr>
				</table>
				<table id="tablaPropuestas" style="display:none">
					<c:forEach items="${AlterMutuaForm.propuestas}" var="p" varStatus="i">
					<tr class="labelTextValue">
						<td id="propuesta${i.index}" width="200px">${p.nombre}</td>
						<td id="seleccionada${i.index}"><input type="radio" name="grupoPropuestas" value="${i.index}"/></td>
						<td id="breve${i.index}">${p.breve}</td>
						<td id="idPaquete${i.index}">${p.idPaquete}</td>
						<td id="familiares${i.index}">${p.familiares}</td>
						<td id="herederos${i.index}">${p.herederos}</td>
						<td id="tarifa${i.index}" colspan="2"><fmt:formatNumber type="NUMBER" minFractionDigits="2" maxFractionDigits="2"><c:out value="${p.tarifa}"/></fmt:formatNumber></td>
						<td id="descripcion${i.index}">${p.descripcion}</td>
					</tr>
					</c:forEach>
				</table>
			</c:if>
		</fieldset>

		<fieldset id="estadoSolicitud" ><legend><siga:Idioma key="censo.alterMutua.literal.estadoSolicitud"/></legend>
		<table>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.solicitud"/></td>
			<td class="labelTextValue" id="holderIdSolicitudAlter">${AlterMutuaForm.idSolicitudalter}</td>
			<td><input type="button" alt=""  id="idButton" onclick="return consultarEstado();" class="button" name="idButton" value="<siga:Idioma key='censo.alterMutua.literal.consultarEstado'/>"></td>
			<td class="labelText">&nbsp;</td>
			<td class="tdBotonDescarga" style="display:none"><siga:Idioma key="censo.alterMutua.literal.descargarDocumento"/></td>
			<td class="tdBotonDescarga" style="display:none"><img id="botonDescarga" src="<%=app%>/html/imagenes/bdownload_on.gif" style="cursor:hand;"></td>
		</tr>
		</table>
		</fieldset>
		
		
		<!--/c:if-->
			
		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.datosPersonales"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.identificador"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.tipoIdentificacion}"/>&nbsp;<c:out value="${AlterMutuaForm.identificador}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.nombre"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.nombre}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.apellidos"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.apellidos}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.fechaNacimiento"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.fechaNacimiento}"/></td>
				</tr>
				<tr>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.sexo"/></td>
					<td class="labelTextValor"> <c:out value="${AlterMutuaForm.sexo}" /> </td>
					
					<c:if test="${AlterMutuaForm.estadoCivil!=''}">
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.estadoCivil"/></td>
						<td class="labelTextValor">
							<c:out value="${AlterMutuaForm.estadoCivil}" />
							<html:hidden property="idEstadoCivil"/>
						</td>
					</c:if>
					<c:if test="${AlterMutuaForm.estadoCivil==''}">
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.estadoCivil"/> (*)</td>
						<td class="labelTextValor">
						<html:select styleClass="${estiloCombo} requiredText" property="idEstadoCivil" >
							<html:optionsCollection name="AlterMutuaForm" property="estadosCiviles" label="value" value="key" />
						</html:select>
						</td>
					</c:if>
					</td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.colegio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.colegio}" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.situacionEjercicio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoEjercicio}" /></td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset id="estadoColegiado" ><legend><siga:Idioma key="censo.alterMutua.literal.estadoColegiado"/></legend>
		<table>
		<tr>
			<td style="width:120px">&nbsp;</td>
			<td><input type="button" alt=""  id="idButton" onclick="return consultarEstadoColegiado();" class="button" name="idButton" value="<siga:Idioma key='censo.alterMutua.literal.consultarEstado'/>"></td>
		</tr>
		</table>
		</fieldset>
		
		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.contacto"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.medioComunicacion"/> (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="tipoComunicacion" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposComunicacion" label="value" value="key" />
						</html:select>
					</td>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.idioma"/> (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="idioma" >
						<html:optionsCollection name="AlterMutuaForm" property="idiomas" label="value" value="key" />
						</html:select>
					</td>
					
					<td class="labelText" colspan="2">
						<label for="admitePublicidad"><siga:Idioma key="censo.alterMutua.literal.aceptaPublicidad"/>&nbsp;</label>
						<input type="checkbox" id="admitePublicidad" name="admitePublicidad" value="true" checked/>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.telefono"/> (*)</td>
					<td class="labelTextValue"><html:text property="telefono1" maxlength="14" styleClass="${estiloText} requiredText"></html:text> </td>

					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.telefono"/> 2</td>
					<td class="labelTextValue"><html:text property="telefono2" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>

					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.movil"/></td>
					<td class="labelTextValue"><html:text property="movil" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.fax"/></td>
					<td class="labelTextValue"><html:text property="fax" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.correoElectronico"/> (*)</td>
					<td class="labelTextValue" colspan="3"><html:text property="correoElectronico" maxlength="100" styleClass="${estiloText} requiredText" style="width:100%"></html:text> </td>
				</tr>
			</table>

			<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.direccion"/></legend>
				<table>
					<tr>
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.domicilio"/> (*)</td>
						<td class="labelTextValue"><html:text property="domicilio" size="30" maxlength="200" styleClass="${estiloText} requiredText"></html:text></td>
						
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.codigoPostal"/> (*)</td>
						<td class="labelTextValue"> <html:text property="codigoPostal" styleClass="${estiloText} requiredText" size="5" maxlength="5"></html:text></td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.pais"/> (*)</td>
						<td class="labelTextValue">
							<html:select styleId="selectPais" styleClass="${estiloCombo} requiredText" property="idPais" >
							<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
							</html:select>
						</td>

						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.provincia"/> (*)</td>
						<td class="labelTextValue" id="tdProvincia">
							<html:select styleId="selectProvincia" styleClass="${estiloCombo} requiredText" property="idProvincia" >
							<html:optionsCollection name="AlterMutuaForm" property="provincias" label="value" value="key" />
							</html:select>
						</td>
						
					</tr>
					<tr>

						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.poblacion"/> (*)</td>
						<td class="labelTextValue" id="tdPoblacion"><html:text property="poblacion" style="width:300px" maxlength="50" styleClass="${estiloCombo} requiredText"></html:text></td>
						
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.tipoDireccion"/></td>
						<td class="labelTextValue">
							<html:select styleClass="${estiloCombo}" property="tipoDireccion" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposDireccion" label="value" value="key" />
							</html:select>
						</td>
					</tr>

				</table>
			</fieldset>
			
		</fieldset>

		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.cuentaBancaria"/></legend>
		<table>
			<tr>
				<td class="labelTextValue" style="display:none">
					<html:select styleId="selectPaisCuenta" styleClass="${estiloCombo} requiredText" property="idPaisCuenta" >
						<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
					</html:select>
				</td>
				<td style="display:none">
					<html:text size="4"  maxlength="4" property="cboCodigo" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="4"  maxlength="4" property="codigoSucursal" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="2"  maxlength="2" property="digitoControl" styleClass="${estiloText} requiredText"></html:text>
					- <html:text size="10" maxlength="10" property="numeroCuenta" styleClass="${estiloText} requiredText"></html:text>
				</td>

				<td class="labelText" nowrap><siga:Idioma key="censo.alterMutua.literal.iban"/>&nbsp;(*)</td>	
				<td class="labelText">
				      <html:text styleId="IBAN" size="34"  maxlength="34" property="iban" styleClass="${estiloText} requiredText" onblur="cargarBancoPorIBAN();"></html:text>
				</td>
				
				<td class="labelText" nowrap><siga:Idioma key="censo.alterMutua.literal.swift"/></td>	
				<td class="labelText">
				      <html:text styleId="SWIFT"size="14"  maxlength="11" property="swift" styleClass="boxConsulta" readonly="true" onblur="rpad();"></html:text>
				</td>
			</tr>
		</table>
		</fieldset>		

		
		<fieldset id="fieldsetFamiliares">
			<legend><siga:Idioma key="censo.alterMutua.literal.familiares"/> 
				<html:button styleId="botonNuevoFamiliar" property="idButton" onclick="return nuevoFamiliar();" styleClass="button" ><siga:Idioma key="censo.alterMutua.literal.anadir"/></html:button>
			</legend>
			<div>
				<table  class="familiares" style="width:100%">
				</table>
			</div>	
		</fieldset>

		<fieldset id="fieldsetBeneficiarios">
			<legend><siga:Idioma key="censo.alterMutua.literal.beneficiarios"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.seleccionBeneficiarios"/></td>
					<td class="labelTextValue">
						<html:select styleId="selectBeneficiarios" styleClass="${estiloCombo} requiredText" property="selectBeneficiarios" >
							<html:option value="1"><siga:Idioma key="censo.alterMutua.literal.herederosLegales"/></html:option>
							<html:option value="3"><siga:Idioma key="censo.alterMutua.literal.otros"/></html:option>
						</html:select>
					</td>
					<td id="botonBeneficiario">
						<html:button styleId="botonNuevoBeneficiario" property="idButton" onclick="return nuevoBeneficiario();" styleClass="button"><siga:Idioma key="censo.alterMutua.literal.anadir"/></html:button>
						<html:button styleId="botonCopiarFamiliares" property="idButton" onclick="return copiarFamiliares();" styleClass="button"><siga:Idioma key="censo.alterMutua.literal.copiarFamiliares"/></html:button>
					</td>
				</tr>
			</table>
			<table class="beneficiarios" style="width:100%">
			</table>	
		</fieldset>
		
		<div style="display:none">
		
			<table style="width:100%">
				<tr class="personaFamiliar">
					<td style="width:105px"><html:select styleClass="defaultText" property="parentesco" styleId="tipoParentesco">
						<html:optionsCollection name="AlterMutuaForm" property="tiposParentesco" label="value" value="key" />
						</html:select> 
					</td>

					<td style="width:85px">
						<html:select styleClass="defaultText" property="sexo" styleId="tipoSexo">
						<html:optionsCollection name="AlterMutuaForm" property="sexos" label="value" value="key" />
						</html:select>
					</td>

					<td style="width:165px"><html:text style="width:150px" styleId="nombre" styleClass="requiredText" property="nombreWSPersona" title="Nombre (*)" maxlength="100"></html:text></td>

					<td style="width:165px"><html:text style="width:150px" styleId="apellidos" styleClass="requiredText" property="apellidosWSPersona" title="Apellidos (*)" maxlength="100"></html:text></td>

					<td style="width:165px">
						<html:select styleClass="defaultText tipoIdent" property="tipoIdentificador"  styleId="tipoIdentificador">
						<html:optionsCollection name="AlterMutuaForm" property="tiposIdentificacion" label="value" value="key" />
						</html:select> 
					</td>

					<td style="width:115px"><html:text styleId="identificador" style="width:90px" styleClass="defaultText ident" property="identificadorWSPersona" title="Identificador" maxlength="100"></html:text></td>

					<td style="width:115px"><html:text styleClass="datepicker requiredText" style="width:95px" property="fechaNacimientoWSPersona" title="F.Nacimiento (*)" maxlength="10"></html:text></td>

					<td class="labelText"><a href="#" class="eliminar"><siga:Idioma key='censo.alterMutua.literal.eliminar'/></a></td>
				</tr>

				<tr class="personaBeneficiario">
					<td style="width:105px">
						<html:select styleClass="defaultText" property="parentesco" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposParentesco" label="value" value="key" />
						</html:select> 
					</td>

					<td style="width:85px">
						<html:select styleClass="defaultText" property="sexo" >
						<html:optionsCollection name="AlterMutuaForm" property="sexos" label="value" value="key" />
						</html:select>
					</td>
					
					<td style="width:165px"><html:text style="width:150px" styleId="nombre" styleClass="requiredText" property="nombreWSPersona" title="Nombre (*)" maxlength="100"></html:text></td>

					<td style="width:165px"><html:text style="width:150px" styleId="apellidos" styleClass="requiredText" property="apellidosWSPersona" title="Apellidos (*)" maxlength="100"></html:text></td>

					<td style="width:165px">
						<html:select styleClass="defaultText tipoIdent" property="tipoIdentificador" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposIdentificacion" label="value" value="key" />
						</html:select> 
					</td>

					<td style="width:115px"><html:text styleId="identificador" styleClass="defaultText ident" style="width:90px" property="identificadorWSPersona" title="Identificador" maxlength="100"></html:text></td>

					<td style="width:115px"><html:text style="width:95px" styleClass="datepicker defaultText" property="fechaNacimientoWSPersona" title="F.Nacimiento" maxlength="10"></html:text></td>
					
					<td class="labelText"><a href="#" class="eliminar"><siga:Idioma key='censo.alterMutua.literal.eliminar'/></a></td>
				</tr>
			</table>
		</div>
		
		<fieldset class="labelTextValue" id="fieldsetObservaciones">
			<legend><siga:Idioma key='censo.alterMutua.literal.observaciones'/></legend>
			<html:textarea styleClass="box" property="observaciones" styleId="textoObservaciones" rows="4" style="width:99%" maxlength="40"></html:textarea>
		</fieldset>
		
		</div>
			
	</html:form>
</div>

<siga:ConjBotonesAccion botones="V,SC,AT" modo="${modoAccion}" clase="botonesDetalle"/>




	<div id="dialog-message" title="SIGA" style="vertical-align: top; max-height: 400px"></div>
	<form name="busquedaClientesForm" method="POST" action="/SIGA/CEN_BusquedaClientes.do" target="mainWorkArea">
			<input type="hidden" name="modo" value="Editar">
			<input type="hidden" name="avanzada" value="">
	</form>
	<form name="SolicitudIncorporacionForm" method="POST" action="/SIGA/CEN_SolicitudesIncorporacion.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
	</form>	
	<c:if test="${modoAccion=='ver'}">
		<script>habilitarCampos('false'); </script>
	</c:if>
	
	<script>
	function refrescarLocal(){
		window.location.reload(true);
	}
	<%if(path.toString().contains("Ficha")){ %>
		function accionVolver() 
		{
			document.busquedaClientesForm.action = "/SIGA/CEN_BusquedaClientes.do" + "?noReset=true&buscar=true";
			document.busquedaClientesForm.modo.value="abrirConParametros";
			document.busquedaClientesForm.submit();	
		}
	<%}else{%>
		function accionVolver() {		
			document.SolicitudIncorporacionForm.action="./CEN_SolicitudesIncorporacion.do";	
			document.SolicitudIncorporacionForm.target="mainWorkArea";
			document.SolicitudIncorporacionForm.submit();
		}
	<%}%>
	
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
			document.forms["AlterMutuaForm"].familiares.value = familiares;
			// Bucle beneficiarios
			var beneficiarios = "";
			$('.ben').each(function(index) {
				if(isPersonaValida($(this))){
					beneficiarios += addPersona($(this)) + "%%%";
				}else{
					valido=false;
				}
			});
			document.forms["AlterMutuaForm"].herederos.value = beneficiarios;
			
			return valido;
	}
	
	function validarFormulario(){
		$('.obligatorio').removeClass('obligatorio');
		sincronizaFamiliares();
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

		if($("#selectBeneficiarios").val()==3 && 
			$(".ben :visible").length<=0){
			$("#botonNuevoBeneficiario").addClass("obligatorio");
			val = false;
		}
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

		//validamos el numero de cuenta (IBAN)
		document.forms["AlterMutuaForm"].IBAN.value = formateaMask(document.getElementById("IBAN").value);
		iban = document.forms["AlterMutuaForm"].IBAN.value;
		bic = document.forms["AlterMutuaForm"].SWIFT.value;		
		
		//SE VALIDA SI SE HA INTODUCIDO IBAN Y BIC
		if (iban == ""  && bic == ""){ 
			mensaje = "<siga:Idioma key='messages.censo.cuentasBancarias.errorCuentaBancaria'/>";
			alert(mensaje);
			fin();
			return false;
		} 
		
		if(!validarCuentaBancaria(iban,bic,"banco")){//Como en el formulario no existe el banco, ponemos ese literal para pasar la validacion
			fin();
			return false;
		}	
		
		$('.ident').each(function(){
			var tipoIdent = $(":input:eq(" + ($(":input").index(this) - 1) + ")").val();
			if( $(this).val()!=$(this).attr('title')
				&& (tipoIdent==="0"
					|| tipoIdent==="2")
				&& isNumeroIdentificacionValido($(this).val())<=0){
				val = false;
				$(this).addClass("obligatorio");
			}
		});

		return val;
	}
	
	function accionGuardar(){
		sub();
		var mensaje="<siga:Idioma key='censo.alterMutua.literal.mensajeConfirmacion'/>";
		if(confirm(mensaje)){
			if(accionActualizar()){
				$.ajax({
		            type: "POST",
		            url: "/SIGA/CEN_AlterMutua.do?modo=insertar",
		            data: $('form').serialize(),
		            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		            success: function(json){
	            		jAlert(json.mensaje,400);
	            		document.forms["AlterMutuaForm"].idSolicitudalter.value = json.idSolicitud;
	            		$("#holderIdSolicitudAlter").text(json.idSolicitud);
	            		showEstado();
	            		makeReadOnly();
						fin();
		            },
		            error: function(e){
						fin();
		                alert('Error de comunicación',"error");
		            }
		        });
			}else{
				fin();
			}
		}else{
			fin();
		}
	}
	
	function consultarEstadoColegiado(){
		sub();
        $.ajax({
            type: "POST",
            url: "/SIGA/CEN_AlterMutua.do?modo=getEstadoColegiado",
            data: $('form').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            success: function(json){
           		jAlert(json.mensaje,400,300);
		        fin();
            },
            error: function(e){
                alert('Error de comunicación',"error");
		        fin();
            }
        });
	}
	
	function accionActualizar(){
		var retVal=false;
		if(validarFormulario() && rellenaPersonas()){
	        $.ajax({
	            type: "POST",
	            url: "/SIGA/CEN_AlterMutua.do?modo=getTarifa",
	            data: $('form').serialize(),
	            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	            async: false,
	            success: function(json){
	            	if(!json.error){
		            	$('#holderTarifa').text(json.tarifa);
		            	retVal=true;
	            	};
	            },
	            error: function(e){
	                alert('Error de comunicación',"error");
	            }
	        });
		}else{
			alert("<siga:Idioma key='censo.alterMutua.literal.relleneCampos'/>","error");
		}
		return retVal;
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
           		if(json.ruta!=""){
					$(".tdBotonDescarga").show();
					$("#botonDescarga").live('click',
						function(){
						descargaFichero(json.ruta);
           	 			}
           	 		);
           		}
		        fin();
            },
            error: function(e){
                alert('Error de comunicación',"error");
		        fin();
            }
        });
	}
	
	function descargaFichero(ruta){
		var formu=document.createElement("<form method='POST' name='descargar'  action='/SIGA/ServletDescargaFichero.svrl' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='rutaFichero'   value=''/>"));
		formu.appendChild(document.createElement("<input type='hidden' name='nombreFichero'   value=''/>"));
		formu.appendChild(document.createElement("<input type='hidden' name='accion'   value=''/>"));
		document.appendChild(formu);
		formu.rutaFichero.value=ruta;
		formu.nombreFichero.value='Solicitud.pdf';
		formu.accion.value = "";
		formu.submit();
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
		$("#botonNuevoBeneficiario").removeClass("obligatorio");
        $('.eliminar').live('click', function() { 
            $(this).parents('tr').remove();
  		});
        setTexts();
	};
	
	function nuevoFamiliar(){
		$('.personaFamiliar').clone().attr("class","fam").attr("id","fam"+($('.fam').length+1)).appendTo('.familiares');
        $('.eliminar').live('click', function() { 
            $(this).parents('tr').remove();
            removeFamiliaresBeneficiarios();
  		});
        addFamiliaresBeneficiarios();
        setTexts();
	};
	
	
	function addFamiliaresBeneficiarios(){
		if($("#selectBeneficiarios option[value=2]").length <= 0){
			$("#selectBeneficiarios").append('<option value="2"><siga:Idioma key="censo.alterMutua.literal.familiares"/></option>');
			$("#selectBeneficiarios option[value=2]").insertBefore("#selectBeneficiarios option[value=3]");
			$("#botonCopiarFamiliares").show();
		}
	}
	
	function removeFamiliaresBeneficiarios(){
		if($(".fam :visible").length == 0){
			$("#selectBeneficiarios option[value=2]").remove();
			$("#botonCopiarFamiliares").hide();
		}
	}
	
    function setTexts(){
		$(".defaultText").focus(function(){
			if ($(this).val() == $(this)[0].title){
				$(this).removeClass("defaultTextActive");
				$(this).val("");
			}
		});
		$(".obligatorio").on({focus:function(){
			$(this).removeClass("obligatorio");
		}});
		$(".defaultText").blur(function(){
			if ($(this).val() == ""){
				$(this).addClass("defaultTextActive");
				$(this).val($(this)[0].title);
			}
		});
		$(".defaultText").blur();
		$(".requiredText").focus(function(){
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
		$(".requiredText").focus(function(){
			$(this).removeClass("requiredTextActive");
			$(this).removeClass("obligatorio");
		});
		$(".datepicker").on({change: function(){
			$(this).removeClass("requiredTextActive");
			$(this).removeClass("defaultTextActive");
			$(this).removeClass("obligatorio");
		}});
		$(".requiredText").blur();
			
		$(".datepicker").on({hover: function() {
			$(this).datepicker({
				yearRange: '-100:+0',
    			changeMonth: true,
    			changeYear: true,
    			regional: "es",
    			beforeShow: function (textbox, instance) {   
//                    instance.dpDiv.css({marginTop: textbox.offsetHeight + 'px', marginLeft: -textbox.offsetWidth-100 + 'px'});
                    instance.dpDiv.css({marginTop: '0px', marginLeft: '0px'});
    			}});
		}});       
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
   		if(document.forms["AlterMutuaForm"].idSolicitudalter.value){
      		$("#estadoSolicitud").show();
      		$("#estadoColegiado").show();
   		}else{
      		$("#estadoSolicitud").hide();	
      		$("#estadoColegiado").hide();
   		}
   	}

    
    function makeReadOnly(){
    	$(":input").prop("readonly", true);
    	$(":checkbox").prop("disabled", true);
    	
   		$("select").each(function(){
   			$(this).after($(this).find("option:selected").text());
   			$(this).hide();
   		});
   		$('#idButtonSolicitar').hide();
   		$('#botonNuevoFamiliar').hide();
   		$('#idButtonActualizar').hide();
   		$('.box').addClass("boxConsulta");
   		$('.box').removeClass("box");
   		$('.boxCombo').addClass("boxComboConsulta");
   		$('.boxCombo').removeClass("boxCombo");
   		$('.persona').remove();
   		mostrarFamiliares();
   		mostrarHerederos();
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
  	   
  	   	$("#botonConsultaPaquete").click(
  	 			function(){
  	 				var texto =  $("#descripcion0").html();
  	 				jAlert(texto, 600, 600);
  	 			}
  	 		);
  	           	
       	$("#propuestaSeleccionada").change(
   			function(){
   				var selec = $(this).prop("selectedIndex");
   				var idPaquete =  $("#idPaquete"+selec).text();
           		var familiares =  $("#familiares"+selec).text();
           		var herederos =  $("#herederos"+selec).text();
           		
   				var breve =  $("#breve"+selec).html();
   				var nombre = $("#propuesta"+selec).text();
           		var tarifa =  $("#tarifa"+selec).text();
           		var descripcion =  $("#descripcion"+selec).html();

           		document.forms["AlterMutuaForm"].nombrePaquete.value = nombre;
           		document.forms["AlterMutuaForm"].brevePaquete.value = breve;
           		document.forms["AlterMutuaForm"].descripcionPaquete.value = descripcion;
           		document.forms["AlterMutuaForm"].tarifaPaquete.value = tarifa;
           		
           		$("#holderTarifa").text(tarifa);
           		$("#holderBreve").text(breve);
           		if(herederos==="true"){  
           			$("#fieldsetBeneficiarios").show();
           			document.forms["AlterMutuaForm"].requiereBeneficiarios.value = true;
           		}else{
           			$("#fieldsetBeneficiarios").hide();
           			document.forms["AlterMutuaForm"].requiereBeneficiarios.value = false;
           		}
           		
           		if(familiares==="true"){
           			$("#fieldsetFamiliares").show();
           			document.forms["AlterMutuaForm"].requiereFamiliares.value = true;
           		}else{
           			$("#fieldsetFamiliares").hide();
           			document.forms["AlterMutuaForm"].requiereFamiliares.value = false;
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
       	
       	
       	$("#selectBeneficiarios").change(function(){
       		if($("#selectBeneficiarios").val()==3){
       			$("#botonBeneficiario").show();
       			addFamiliaresBeneficiarios();
       		}else{
       			$("#botonBeneficiario").hide();
       			$("#botonCopiarFamiliares").hide();
       			$('.ben').each(function(index){
       				// Si el titulo es igual que el valor (no se ha rellenado)
       				$(this).remove();
       			});
       		}
       	});
       	$("#selectBeneficiarios").trigger('change');
        	
        	
      	showEstado();

       	if(document.forms["AlterMutuaForm"].idSolicitudalter.value || document.forms["AlterMutuaForm"].numeroPropuestas.value<=0){
       		makeReadOnly();
       	}
       		
       	if(document.forms["AlterMutuaForm"].idSolicitudalter.value==='' && document.forms["AlterMutuaForm"].numeroPropuestas.value<=0){
       		jAlert(document.forms["AlterMutuaForm"].msgRespuesta.value,400,300);
       	}
       	
     	setTexts();
     	
     	$('#datosSolicitud').show();
     	$('.botonesDetalle').show();
     	$('#botonCopiarFamiliares').hide();

     });
       $('#textoObservaciones').on('keyup keydown blur', function() {
           var maxlength = 4000;
           var val = $(this).val();
           if (val.length > maxlength) {
               $(this).val(val.slice(0, maxlength));
           }
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
	
	function copiarFamiliares(){
		$('.famBen').remove();
		$('.fam').clone().attr("class","ben").addClass("famBen").appendTo('.beneficiarios');
        $('.eliminar').on({click: function() { 
            $(this).parents('span').remove();
        }});
		$('.famBen').each(function(){
			$(this).find('input').each(function(){
				if($(this).attr('title') != $(this).val()){
					$(this).prop("readonly", true);
			    	$(this).addClass("boxConsulta");
			    	$(this).removeClass("box");
			   		$(this).addClass("boxComboConsulta");
			   		$(this).removeClass("boxCombo");
			   		$(this).removeClass("requiredText");
			   		$(this).removeClass("requiredTextActive");
				}
			});
			$(this).find('select').each(function(){
				$(this).after($(this).find("option:selected").text());
	   			$(this).hide();
			});
		});
        setTexts();
	}
	
	function mostrarFamiliares(){
		var st = document.forms["AlterMutuaForm"].familiares.value;
		var salida="";
		if(st && st!=""){
			var familiares = st.split("%%%");
			salida += "<tr>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.parentesco'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.sexo'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.nombre'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.apellidos'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.identificador'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.fechaNacimiento'/></td>";
			salida += "</tr>";
			
			for ( var i = 0; i < familiares.length-1; i++) {
				salida+=printPersona(familiares[i]);		
			}

			$(".familiares").append(salida);
			
			$("#fieldsetFamiliares").show();
		}else{
			$("#fieldsetFamiliares").hide();
		}
	}
	function mostrarHerederos(){
		var st = document.forms["AlterMutuaForm"].herederos.value;
		var salida="";
		if(st && st!=""){
			var herederos = st.split("%%%");
			salida += "<tr>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.parentesco'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.sexo'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.nombre'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.apellidos'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.identificador'/></td>";
			salida += "<td class='labelText'><siga:Idioma key='censo.alterMutua.literal.fechaNacimiento'/></td>";
			salida += "</tr>";
			
			for ( var i = 0; i < herederos.length-1; i++) {
				salida+=printPersona(herederos[i]);		
			}
			$(".beneficiarios").append(salida);
			$("#fieldsetBeneficiarios").show();
		}else{
			$("#fieldsetBeneficiarios").hide();
		}
	}
	
	function printPersona(persona){
		var html="";
		var campos = persona.split("&&");
		
		var parentesco = $("#tipoParentesco option:[value="+campos[0]+"]").text();
		var sexo = $("#tipoSexo option:[value="+campos[1]+"]").text();
		var tipoIdent = $("#tipoIdentificador option:[value="+campos[2]+"]").text();
		var nombre = campos[3];
		var apellidos = campos[4];
		var identificador = campos[5];
		var fechaNacimiento = campos[6];
		html += "<tr>";
		html += "<td class='labelTextValue'>"+parentesco+"</td>";
		html += "<td class='labelTextValue'>"+sexo+"</td>";
		html += "<td class='labelTextValue'>"+nombre+"</td>";
		html += "<td class='labelTextValue'>"+apellidos+"</td>";
		if(identificador!=""){
			html += "<td class='labelTextValue'>"+tipoIdent+" "+identificador+"</td>";
		}else{
			html += "<td class='labelTextValue'>&nbsp;</td>";
		}
		html += "<td class='labelTextValue'>"+fechaNacimiento+"</td>";
		html += "</tr>";

		return html;
	}
	
	function sincronizaFamiliares(){
		$('.famBen').each(function(){
			var idFam = $(this).attr("id");
			var idBen = $(this).attr("id")+"ben";
			$(this).attr("id",idBen);
			
			var camposBen = $("#"+idBen).find('select');
			var a=0;
			$("#"+idFam).find('select').each(function(){
				if($(this).val()!=camposBen[a].value && $(this).val()!=$(this).attr('title')){
					$(this).val(camposBen[a].value);
				}
				a++;
			});
			
			var inputBen = $("#"+idBen).find('input');
			a=0;
			$("#"+idFam).find('input').each(function(){
				if($(this).val()!=inputBen[a].value){
					$(this).val(inputBen[a].value);
				}
				a++;
			});
			$(this).attr("id",idFam);
		});
	}
	
	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none" />
</body>

</html>



