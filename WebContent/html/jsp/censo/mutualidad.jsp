<!DOCTYPE html>
<html>
<head>
<!-- mutualidad.jsp -->
<!-- CABECERA JSP -->
<%@page import="java.util.ArrayList"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>



	<style>
		.ocultar {display:none}
		.seguido {float:left}
		.primeraCol {width:150px}
	</style>	


<script>
function habilitarCampos(isHabilitar) {
		
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

</script>


<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
				 
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en SIGA.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>

	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	
	<script>
	
	jQuery.noConflict();
	
	function refrescarLocal(){
		window.location.reload( true );
	}
	
	function preAccionPoblacion(){
		//document.getElementById("idPoblacion").value=document.MutualidadForm.idPoblacion.value;
		//alert("document.MutualidadForm.idPoblacion.value"+document.MutualidadForm.idPoblacion.value);
	}
	function postAccionPoblacion(){
		//alert("document.MutualidadForm.idPoblacion.value"+document.MutualidadForm.idPoblacion.value);
		//alert("idpoblacion"+document.getElementById("idPoblacion").value);
		//if(document.MutualidadForm.idPoblacion.value!=''){
		//	 document.getElementById("idPoblacion").value=document.MutualidadForm.idPoblacion.value;
		//}
	}	
	
	function postEuros(){
		jQuery("#capitalCobertura").val(jQuery("#capitalCobertura").val()+"�");
		jQuery("#cuotaCobertura").val(jQuery("#cuotaCobertura").val()+"�");
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
		if(document.getElementById("IBAN")){
			var iban = document.getElementById("IBAN").value;
			var codigoBanco = "${MutualidadForm.cboCodigo}";
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
	}
	
	
	function rpad() {
		if (document.getElementById("SWIFT").value.length == 8){
	    	while (document.getElementById("SWIFT").value.length < 11)
	    		document.getElementById("SWIFT").value = document.getElementById("SWIFT").value + 'X';
		}
	}	
	
	</script>	
	
	
	<siga:Titulo titulo="censo.mutualidad.titulo" localizacion="censo.solicitudIncorporacion.localizacion"/>
	<%if(path.toString().contains("Ficha")){ %>
		<siga:Titulo titulo="censo.mutualidad.titulo" localizacion="censo.fichaCliente.localizacion"/>
	<%}%>
	
</head>


<body class="tablaCentralCampos" onload="inicioCargarBancoBIC();"> 

<%  
		ArrayList idPaisSeleccionado = (ArrayList)request.getAttribute("idPaisSeleccionado");
		ArrayList idPaisNacionalidad = new ArrayList();
		idPaisNacionalidad.add("191");
		ArrayList idBancoSeleccionado = (ArrayList)request.getAttribute("idBancoSeleccionado");
		String accion = (String)request.getParameter("accion");
	%>
	

<html:javascript formName="MutualidadForm" staticJavascript="true" />
<html:form action="${path}"  method="POST" target="submitArea">
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
<html:hidden property="idSolicitud"/>
<html:hidden property="idSolicitudAceptada"/>
<html:hidden property="pais"/>
<html:hidden property="provincia"/>
<html:hidden property="poblacion"/>
<html:hidden property="idEstado"/>
<html:hidden property="estado"/>
<html:hidden property="colegio"/>
<html:hidden property="nacionalidad"/>
<html:hidden property="estadoMutualista"/>

<input type="hidden" name="actionModal" value="">

<c:set var="estiloText" value="box" />
<c:set var="estiloCombo" value="box" />
<c:if test="${MutualidadForm.modo=='consulta'}">
	<c:set var="estiloCombo" value="boxConsulta" />
	<c:set var="estiloText" value="boxConsulta" />
</c:if>

<fieldset id="dialogoSolicitar">
		<table width="99%">
		
			<tr>
				<td class="labelText" width="80%">
				
					<c:choose>
					<c:when test="${MutualidadForm.idTipoSolicitud=='S'}">
						<siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.seguroAccidentes"/>
					</c:when>
					<c:otherwise>
						<siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.planProfesional"/>
					</c:otherwise>
					</c:choose>

				<td>
					<html:button styleId="botonSolicitarAltaSeguro" property="idButton"onclick="return accionMostrarSolicitud();" styleClass="button">
						<siga:Idioma key="general.boton.solicitarCompra" />
					</html:button>
				</td>
			<tr>
			<tr>
				<td class="labelText" colspan="2">
					<siga:Idioma key="censo.mutualidad.literal.masInfo"/>
					<a href="http://delegaciones.mutualidadabogacia.com/MicroSiteContacto/pages/contacto.aspx" target="new"><siga:Idioma key="censo.mutualidad.literal.aqui"/></a>
				</td>
			</tr>
		</table>
</fieldset>
<div id="divSolicitud" style="display:none;" >
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
					<td class="labelText"><siga:Idioma key="censo.mutualidad.literal.solicitudAlta"/></td>
					<c:choose>
							
						<c:when test="${MutualidadForm.idSolicitud==null}">
								<td class="labelTextValue"><siga:Idioma key="censo.mutualidad.literal.altaNoSolicitada"/></td>
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
								<td>
									<html:button property="idButton"
											onclick="return accionComprobarEstadoMutualidad();"
											styleClass="button">
										<siga:Idioma key="censo.mutualidad.literal.estado" />
									</html:button>
									
								</td>
								<td id="tdEstadoSolicitud" class="labelTextValor" style="color: blue;"><c:out
										value="${MutualidadForm.estado}" />
								</td>
							</c:when>
					</c:choose>
					<c:choose>
						<c:when test="${MutualidadForm.idTipoSolicitud=='P'}">
						<td>
							<html:button property="idButton"
								onclick="return accionComprobarEstadoMutualista();"
								styleClass="button">
								<siga:Idioma key="censo.mutualidad.literal.estadoMutualista" />
							</html:button>
						</td>
						<td id="tdEstadoMutualista" class="labelTextValor" style="color: blue;" colspan="3" width="300px">
							&nbsp;<c:out value="${MutualidadForm.estadoMutualista}" />
						</td>
						</c:when>
					</c:choose>

					</tr>
					<tr>
						<td class="labelText" colspan="8">
							<siga:Idioma key="censo.mutualidad.literal.masInfo"/>
							<a href="http://delegaciones.mutualidadabogacia.com/MicroSiteContacto/pages/contacto.aspx" target="new"><siga:Idioma key="censo.mutualidad.literal.aqui"/></a>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			<c:choose>
			<c:when test="${MutualidadForm.idSolicitud==null && MutualidadForm.idTipoSolicitud=='P'}">
				<p class="labelText">
				La persona no puede solicitar el alta en la Mutualidad de la Abogac�a. Puede que ya sea mutualista.<br> 
				Compruebe que ha introducido un NIF v�lido y que su edad est� comprendida entre 18 y 50 a�os.
				</p>
			</c:when>
			</c:choose>
		</c:if>

		<c:if test="${MutualidadForm.modo!='consulta' || MutualidadForm.idSolicitud!=null}">

		<siga:ConjCampos>
			<table width="90%">

				<tr>
					<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif" /></td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.tipoIdentificacion}" />&nbsp;<c:out value="${MutualidadForm.numeroIdentificacion}" /></td>

					<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/></td>
					<td class="labelTextValor" colspan="3">
						<c:out value="${MutualidadForm.tratamiento}" />
						<c:out value="${MutualidadForm.nombre}" />
						<c:out value="${MutualidadForm.apellido1}" />
						<c:out value="${MutualidadForm.apellido2}" />
					</td>
				</tr>

				<tr>
					<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.estadoCivil" /></td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.estadoCivil}" />
					
					<td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.sexo" /> </td>
					<td class="labelTextValor" width="15%">
					<c:choose>
					<c:when test="${MutualidadForm.sexo=='M'}">
						<siga:Idioma key="censo.sexo.mujer" />
					</c:when>
					<c:otherwise>
						<siga:Idioma key="censo.sexo.hombre" />
					</c:otherwise>
					</c:choose>
					</td>
					
					<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaNacimiento" /></td>
					<td class="labelTextValor"><c:out value="${MutualidadForm.fechaNacimiento}" /></td>
				</tr>

			</table>
		</siga:ConjCampos>
		<c:if test="${MutualidadForm.idTipoSolicitud=='S'}"> <!-- Seguro Accidentes -->
		<table>
			<tr>
				<td class="labelText" >
					<siga:Idioma key="censo.mutualidad.seguroAccidentes.descripcion"/>
				</td>
		</tr>
		</table>
	</c:if>
	<div id="datosSolicitud">		
	<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.nacionalidad" /></td>
			
			<td colspan="2">
			<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
						<siga:ComboBD nombre="comboNacionalidad" tipo="pais" ancho="300" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" obligatorio="false" elementoSel='<%=idPaisNacionalidad%>' accion="cargaCombos(this.value);" />

					</c:when>
					<c:otherwise>
						<html:text property="nacionalidad" maxlength="100" style="width:300" styleClass="${estiloText}" ></html:text>
					</c:otherwise>
				</c:choose>
			
				
			</td>
			
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.email" />&nbsp;(*)</td>
			<td colspan="3"><html:text property="correoElectronico" maxlength="100" style="width:400" styleClass="${estiloText}" ></html:text></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma
							key="censo.mutualidad.literal.pais" /></td>
			<td colspan="2">
			<c:choose>
						<c:when test="${MutualidadForm.modo=='insertar'}">
							<siga:ComboBD nombre="idPais" tipo="pais" ancho="300" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" obligatorio="false" elementoSel='<%=idPaisSeleccionado%>' accion="cargaCombos(this.value);" />

						</c:when>
						<c:otherwise>
							<siga:ComboBD  nombre="idPais" tipo="pais" ancho="300" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" obligatorio="false"  elementoSel='<%=idPaisSeleccionado%>' pestana='2' accion="cargaCombos(this.value);" />
						</c:otherwise>
					</c:choose>
			
				
			</td>
			<td class="labelText" ><siga:Idioma
							key="censo.mutualidad.literal.provincia" />&nbsp;(*)</td>
			<td>
			<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
				<html:select styleId="provincias" styleClass="${estiloCombo}"  
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
				<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.codigoPostal" />&nbsp;(*)
				<html:text property="codigoPostal" styleClass="${estiloText}" size="5" maxlength="5"  ></html:text>
				</td>
		
		</tr>
		<tr>

			<td class="labelText"><siga:Idioma
							key="censo.mutualidad.literal.poblacion" />&nbsp;(*)</td>
			<td id="tdPoblacionEspanola" colspan="2">
			<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
			
				<html:select styleId="poblaciones" styleClass="boxCombo" style="width:320px;" property="idPoblacion" >
					<bean:define id="poblaciones" name="MutualidadForm" property="poblaciones" type="java.util.Collection" />
					<html:optionsCollection name="poblaciones" value="idPoblacion" label="nombre" />
				</html:select>
				</c:when>
				<c:otherwise>
					<html:text property="poblacion"  styleClass="${estiloText}" ></html:text>
					<html:hidden property="idPoblacion"/>
				</c:otherwise>
				
			</c:choose>
			</td> 
			
			<td class="ocultar" colspan="2" id="tdPoblacionExtranjera">
				<html:text property="poblacionExtranjera" style="width:300" maxlength="100" styleClass="${estiloText}" ></html:text>
			</td>
				
			<td class="labelText" ><siga:Idioma
							key="censo.mutualidad.literal.domicilio" />&nbsp;(*)</td>
			<td colspan="2"><html:text property="domicilio" style="width:300" maxlength="100" styleClass="${estiloText}" ></html:text></td>
			
			
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.telefono" />&nbsp;(*)</td>
			<td><html:text property="telef1" maxlength="9" styleClass="${estiloText}" ></html:text></td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.movil" /></td>
			<td><html:text property="movil" maxlength="9" styleClass="${estiloText}"  ></html:text></td>
			
			
		</tr>
	</table>
	
	</siga:ConjCampos>




		<div>

		<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera" clase="planProfesional">
			<!-- FILA -->
			<table class="tablaCampos" align="left" border="0" style="width:100%">	
				<tr align="left">		
					<td style="display:none">
					      <html:text size="4"  maxlength="4" property="cboCodigo"  styleClass="${estiloText}"  onChange="document.MutualidadForm.idBanco.value=document.MutualidadForm.cboCodigo.value"></html:text>
						- <html:text size="4"  maxlength="4" property="codigoSucursal" 	styleClass="${estiloText}" ></html:text>
						- <html:text size="2"  maxlength="2" property="digitoControl"  	styleClass="${estiloText}" ></html:text>
						- <html:text size="10" maxlength="10" property="numeroCuenta"  styleClass="${estiloText}" ></html:text></td>
					<td class="labelText" nowrap><siga:Idioma
							key="censo.mutualidad.literal.iban" /></td>
					<td class="labelText">
						<html:text property="iban" size="36" styleClass="${estiloText}" maxlength="34" onblur="cargarBancoPorIBAN();"/>
					</td>
					<td class="labelText" nowrap><siga:Idioma
							key="censo.mutualidad.literal.swift" /></td>
					<td class="labelText">
						<html:text property="swift" size="15" maxlength="11" styleClass="boxConsulta" readonly="true" onblur="rpad();"/>
					</td>
				<tr>
	
			
				<!-- FILA -->
				<tr style="display:none">					
					<td class="labelText" colspan="3"><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
					<td class="labelText" colspan="3">
						<siga:ComboBD nombre="idBanco" ancho="450" tipo="cmbBancos" clase="${estiloCombo}" readonly="${MutualidadForm.modo=='consulta'}" elementoSel='<%=idBancoSeleccionado%>' accion="document.MutualidadForm.cboCodigo.value=document.MutualidadForm.idBanco.value"/>
					</td>
				</tr>
				
				<!-- FILA -->
				<tr align="left">		
					<td class="labelText"><siga:Idioma
							key="censo.mutualidad.literal.periodicidadPago" /></td>
					<td class="labelTextValor" colspan="5">
						<c:choose>
							<c:when test="${MutualidadForm.modo=='insertar'}">
								<html:select styleClass="${estiloCombo}" style="width:200px;" name="MutualidadForm" property="idPeriodicidadPago"  >
									<bean:define id="periodicidadesPago" name="MutualidadForm" property="periodicidadesPago" type="java.util.List" />
									<html:optionsCollection name="periodicidadesPago" value="key" label="value"  />
								</html:select>	
								<html:hidden property="periodicidadPago"/>
	
							</c:when>
							<c:otherwise>
								<c:out value="${MutualidadForm.periodicidadPago}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>



		<siga:ConjCampos leyenda="censo.mutualidad.leyenda.poliza">

		<div class="planProfesional">
				<div class="labelText seguido primeraCol"><siga:Idioma key="censo.mutualidad.literal.opcionCobertura" /></div>
				<div class="seguido">
				<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
						<html:select styleClass="${estiloCombo}" styleId="opcionesCobertura" name="MutualidadForm" property="idCobertura" style="width:250px;">
						<bean:define id="opcionesCobertura" name="MutualidadForm" property="opcionesCobertura" type="java.util.List" />
						<html:optionsCollection name="opcionesCobertura" value="key" label="value" />
					</html:select>
					<html:hidden property="cobertura"/>
					</c:when>
					<c:otherwise>
					<c:out value="${MutualidadForm.cobertura}"></c:out>
					</c:otherwise>
				</c:choose>

				</div>
				<div class="labelText seguido"><siga:Idioma key="censo.mutualidad.literal.cuotaMensual" /></div>
				<div class="labelTextValor seguido"><html:text property="cuotaCobertura" styleId="cuotaCobertura" size="6" styleClass="boxNumber boxConsulta" style="text-align:right"/></div>
				<div class="labelText seguido"><siga:Idioma key="censo.mutualidad.literal.capitalObjetivo" /></div>
				<div class="labelTextValor"><html:text property="capitalCobertura" styleId="capitalCobertura" size="8" styleClass="boxNumber boxConsulta" style="text-align:right"/></div>
				
				<div class="labelText" style="padding-bottom:5px">
					<a href="http://www.mutualidadabogacia.com/Home/Alternativa-al-RETA/Elige-el-nivel-de-cobertura-que-deseas.aspx" style="align:left" target="new">Conoce las distintas opciones de cobertura disponibles</a>
				</div>
		</div>
		

		<div>
			<div class="labelText seguido primeraCol"><siga:Idioma key="censo.mutualidad.literal.beneficiarios" /></div>
			<div class="seguido">
				<c:choose>
					<c:when test="${MutualidadForm.modo=='insertar'}">
						<div class="seguido">
						<html:select styleClass="${estiloCombo}" name="MutualidadForm" property="idBeneficiario" styleId="idBeneficiario" style="width:700px;" onchange="onchangeBeneficiario();">
							<bean:define id="beneficiarios" name="MutualidadForm" property="beneficiarios" type="java.util.List" />
							<html:optionsCollection name="beneficiarios" value="key" label="value" />
						</html:select>
						<html:hidden property="beneficiario"/>
						</div>
						<div>
							<html:text property="otrosBeneficiarios"  size="40" styleClass="${estiloText}" style="width:700px;display:none"/>
						</div>
					</c:when>
					<c:otherwise>
						<div class="labelTextValor">
							<c:out value="${MutualidadForm.beneficiario}"></c:out>
						</div>
						<div class="labelTextValor">
							<c:out value="${MutualidadForm.otrosBeneficiarios}"></c:out>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<div class="planProfesional">
			<div>
					<div class="labelText seguido primeraCol"><siga:Idioma key="censo.mutualidad.literal.asistenciaSanitaria" /></div>
					<div class="labelTextValor">
						<c:choose>
							<c:when test="${MutualidadForm.modo=='insertar'}">
								<html:select styleClass="${estiloCombo}" name="MutualidadForm" property="idAsistenciaSanitaria"  style="width:250px;">
									<bean:define id="asistenciasSanitarias" name="MutualidadForm" property="asistenciasSanitarias" type="java.util.List" />
								<html:optionsCollection name="asistenciasSanitarias" value="key" label="value" />
								</html:select>
								<html:hidden property="asistenciaSanitaria"/>
							</c:when>
							<c:otherwise>
								<c:out value="${MutualidadForm.asistenciaSanitaria}"></c:out>
							</c:otherwise>
						</c:choose>
					</div>
			</div>
			<div class="labelText">
					Las cuotas aportadas durante los tres primeros a�os al Sistema de Previsi�n Profesional para las garant�as de Ahorro-Jubilaci�n, Fallecimiento, Incapacidad Permanente, Incapacidad Temporal Profesional y Dependencia, tendr�n una reducci�n del 50% para los menores de 50 a�os.
			</div>
		</div>
		</siga:ConjCampos>



		<siga:ConjCampos leyenda="censo.mutualidad.leyenda.unidadFamiliar" clase="planProfesional">
		<table>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.fechaNacimientoConyuge" /></td>
			<td>
				<siga:Fecha nombreCampo="fechaNacimientoConyuge" valorInicial="${MutualidadForm.fechaNacimientoConyuge}" disabled="${MutualidadForm.modo=='consulta'}"></siga:Fecha>				
			</td>
			<td class="labelText" ><siga:Idioma
							key="censo.mutualidad.literal.numeroHijos" /></td>
			<td><html:text
				 size="4"  maxlength="2" property="numeroHijos" styleId="numeroHijos" styleClass="${estiloText}" onchange="consultarNHijos();"/>
			</td>
			</tr>
			</table>
		<siga:ConjCampos leyenda="censo.mutualidad.leyenda.edadHijos">
		<table width="100%">
		
			<tr>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;1</td>
			<td >
				<html:text property="edadHijo1" styleId="edadHijo1" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;2</td>
			<td >
				<html:text property="edadHijo2" styleId="edadHijo2" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;3</td>
			<td >
				<html:text property="edadHijo3" styleId="edadHijo3" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;4</td>
			<td >
				<html:text property="edadHijo4" styleId="edadHijo4" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;5</td>
			<td >
				<html:text property="edadHijo5" styleId="edadHijo5" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			
			</tr>
			
			<tr style="display:none" id="masHijos">
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;6</td>
			<td>
				<html:text property="edadHijo6" styleId="edadHijo6" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;7</td>
			<td>
				<html:text property="edadHijo7" styleId="edadHijo7" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;8</td>
			<td>
				<html:text property="edadHijo8" styleId="edadHijo8" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;9</td>
			<td>
				<html:text property="edadHijo9" styleId="edadHijo9" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			<td class="labelText" ><siga:Idioma key="censo.mutualidad.literal.hijo" />&nbsp;10</td>
			<td>
				<html:text property="edadHijo10" styleId="edadHijo10" size="4" styleClass="${estiloText}" maxlength="2"/>
			</td>
			
			</tr>
			</table>
			</siga:ConjCampos>
	</siga:ConjCampos>
	</div>
	
	<div class="labelText">
		<c:choose>
		<c:when test="${MutualidadForm.modo=='insertar'}">
			<input type="checkbox" id="checkCesionDatos">
		</c:when>
		<c:otherwise>
			<input type="checkbox" id="checkCesionDatos" checked="checked" disabled="disabled">
		</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${MutualidadForm.idTipoSolicitud=='S' || path!='/CEN_Mutualidad'}">
			<label for="checkCesionDatos"><siga:Idioma key="censo.mutualidad.cesionDatos"/></label>
		</c:when>
		<c:otherwise>
			<label for="checkCesionDatos"><siga:Idioma key="censo.mutualidad.cesionDatosPlan"/></label>
		</c:otherwise>
		</c:choose>
	</div>
	
</div>
	
<ajax:updateFieldFromSelect  
	baseUrl="/SIGA${path}.do?modo=getAjaxCuotaCapitalCobertura"
	source="opcionesCobertura" target="cuotaCobertura,capitalCobertura" parameters="idCobertura={idCobertura},idSexo={idSexo},fechaNacimiento={fechaNacimiento}"
	postFunction="postEuros"
	/>
<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxPoblaciones"
	source="provincias" target="poblaciones" parameters="idProvincia={idProvincia}"
	preFunction="preAccionPoblacion"
	postFunction="postAccionPoblacion" />
</c:if>

</div>
<div id="botonera">
<c:if test="${MutualidadForm.modo=='insertar'}">
	<siga:ConjBotonesAccion botones="GS,R" clase="botonesDetalle" />
</c:if>
</div>
</html:form>

<c:if test="${MutualidadForm.modo=='ver'}">
		<script>habilitarCampos('false'); </script>
	</c:if>
	

					
						
			
		
	<!-- FIN: BOTONES REGISTRO -->
	

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	

	function accionVolver() {		
		document.forms[0].action="./CEN_SolicitudesIncorporacion.do";	
		document.forms[0].target="mainWorkArea";
		document.forms[0].modo.value="";
		document.forms[0].submit();
	}

 
	function accionRestablecer(){
		document.MutualidadForm.reset();
		cargaCombos();
	}
	
	function consultarNHijos(){
		if(document.MutualidadForm.numeroHijos){
			var hijos=document.MutualidadForm.numeroHijos.value;
			if (hijos>5){
				document.getElementById("masHijos").style.display="inline";
			}else{
				document.getElementById("masHijos").style.display="none";
			}
		}
	}

	function accionGuardar(){
		sub();
		if(!document.getElementById("checkCesionDatos").checked){
			
			msg = "<siga:Idioma key='errors.checkRequired' arg0='censo.mutualidad.cesionDatos'/>";
			alert(msg);
			fin();
			return false;
			
		}
		
		document.MutualidadForm.capitalCobertura.value=document.MutualidadForm.capitalCobertura.value.slice(0,-1)
		document.MutualidadForm.cuotaCobertura.value= document.MutualidadForm.cuotaCobertura.value.slice(0,-1)
		
		document.MutualidadForm.periodicidadPago.value = document.MutualidadForm.idPeriodicidadPago.options[document.MutualidadForm.idPeriodicidadPago.selectedIndex].text;
		document.MutualidadForm.cobertura.value = document.MutualidadForm.idCobertura.options[document.MutualidadForm.idCobertura.selectedIndex].text;
		document.MutualidadForm.beneficiario.value = document.MutualidadForm.idBeneficiario.options[document.MutualidadForm.idBeneficiario.selectedIndex].text;
		document.MutualidadForm.asistenciaSanitaria.value = document.MutualidadForm.idAsistenciaSanitaria.options[document.MutualidadForm.idAsistenciaSanitaria.selectedIndex].text;
		if(document.MutualidadForm.idPais.value == ""){
			document.getElementById("idPais").value = "191";
		}
		document.MutualidadForm.pais.value = document.MutualidadForm.idPais.options[document.MutualidadForm.idPais.selectedIndex].text;
		document.MutualidadForm.nacionalidad.value = document.MutualidadForm.comboNacionalidad.options[document.MutualidadForm.comboNacionalidad.selectedIndex].text

		if(document.MutualidadForm.idProvincia&&document.MutualidadForm.idProvincia.selectedIndex!=-1){
			document.MutualidadForm.provincia.value = document.MutualidadForm.idProvincia.options[document.MutualidadForm.idProvincia.selectedIndex].text;
		}
		if(document.MutualidadForm.idPoblacion&&document.MutualidadForm.idPoblacion.selectedIndex!=-1){
			document.MutualidadForm.poblacion.value = document.MutualidadForm.idPoblacion.options[document.MutualidadForm.idPoblacion.selectedIndex].text;
		}
		
		if (validateMutualidadForm(document.MutualidadForm)){
			//validamos los hijos grabados
			var numHijosGrabados = 0; 
			if(document.MutualidadForm.numeroHijos.value!=''&&document.MutualidadForm.numeroHijos.value>0){
				if(document.getElementById('edadHijo1') && document.getElementById('edadHijo1').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo2') && document.getElementById('edadHijo2').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo3') && document.getElementById('edadHijo3').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo4') && document.getElementById('edadHijo4').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo5') && document.getElementById('edadHijo5').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo6') && document.getElementById('edadHijo6').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo7') && document.getElementById('edadHijo7').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo8') && document.getElementById('edadHijo8').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo9') && document.getElementById('edadHijo9').value!='')
					numHijosGrabados = numHijosGrabados+1;
				if(document.getElementById('edadHijo10') && document.getElementById('edadHijo10').value!='')
					numHijosGrabados = numHijosGrabados+1;
				
				if(numHijosGrabados!=document.MutualidadForm.numeroHijos.value){
					
					msg = "<siga:Idioma key='censo.mutualidad.aviso.edadHijos' />";
					alert(msg);
					fin();
					return false;
				}
				
			}
			
			//validamos el numero de cuenta (IBAN)
			if(document.MutualidadForm.idTipoSolicitud.value=='P'){				
				iban = document.MutualidadForm.iban.value;
				bic = document.MutualidadForm.swift.value;
				
				if(!validarCuentaBancaria(iban,bic,"banco")){//Como en el formulario no existe el banco, ponemos ese literal para pasar la validacion
					fin();
					return false;
				} 	
			}
			
			deshabilitaCampos();
			if(!confirm("<siga:Idioma key='censo.mutualidad.literal.confirmacion'/>")){
				// Si no aceptan el disclaimer salimos de la aplicacion
				fin();
				habilitaCampos();
				return false;		
			}
			// Quitamos los botones //
			jQuery(".botonesDetalle").hide();
			document.MutualidadForm.submit();
		}else{
			fin();
		}
		
	}
	function cargaCombos() {      
		
		if(document.getElementById("idPais")){
			var pais = document.MutualidadForm.idPais.value;
		   if (pais!=null && pais!="" && pais!='191'){
			    document.getElementById("idPoblacion").value="";
		   		document.getElementById("idProvincia").value="";
			   	jQuery("#idProvincia").attr("disabled","disabled");
				document.getElementById("tdPoblacionEspanola").className="ocultar";
				document.getElementById("tdPoblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExtranjera").value="";
				jQuery("#idProvincia").removeAttr("disabled");		   		
				document.getElementById("tdPoblacionEspanola").className="";
				document.getElementById("tdPoblacionExtranjera").className="ocultar";
				//if(document.getElementById("idProvincia").onchange)
					//var cmbProvincia = document.getElementById("idProvincia").onchange();
				
				
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
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'<siga:Idioma	key="censo.mutualidad.aviso.espera" />');
		if(resultado){
			document.MutualidadForm.idEstado.value = resultado[0];
			document.MutualidadForm.estado.value = resultado[1];	
	    	document.getElementById("tdEstadoSolicitud").innerText = resultado[1];
	    	var ruta = resultado[2];
			if(ruta.length>0 && confirm('�Desea descargar el documento asociado a la solicitud?')){

				var formu = document.createElement('form');
             	formu.setAttribute('name', 'descargar');
                formu.setAttribute('method', 'POST');
                formu.setAttribute('action', '/SIGA/ServletDescargaFichero.svrl');
                formu.setAttribute('target', 'submitArea');
                formu.setAttribute('method', 'POST');

                var myinput = document.createElement('input');
                myinput.setAttribute('type', 'hidden');
                myinput.setAttribute('name', 'rutaFichero');
                myinput.setAttribute('value', ruta);
                formu.appendChild(myinput);

                var myinput2 = document.createElement('input');
                myinput2.setAttribute('type', 'hidden');
                myinput2.setAttribute('name', 'nombreFichero');
                myinput2.setAttribute('value', 'Solicitud.pdf');
                formu.appendChild(myinput2);

                var myinput3 = document.createElement('input');
                myinput3.setAttribute('type', 'hidden');
                myinput3.setAttribute('name', 'accion');
                myinput3.setAttribute('value', '');
                formu.appendChild(myinput3);
                
				document.body.appendChild(formu);
				formu.submit();
				
			}
		}
		
	}
	

	
	function accionComprobarEstadoMutualista()
	{
		document.MutualidadForm.modo.value = "actualizaEstadoMutualista";
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'<siga:Idioma key="censo.mutualidad.aviso.espera" />');
		if(resultado && resultado[0]!='1'){
			var ruta = resultado[1];
			document.MutualidadForm.estadoMutualista.value = resultado[0];	
	    	document.getElementById("tdEstadoMutualista").innerText = resultado[0];
			if(ruta.length>0 && confirm('�Desea descargar el documento asociado a la solicitud?')){

				var formu = document.createElement('form');
             	formu.setAttribute('name', 'descargar');
                formu.setAttribute('method', 'POST');
                formu.setAttribute('action', '/SIGA/ServletDescargaFichero.svrl');
                formu.setAttribute('target', 'submitArea');
                formu.setAttribute('method', 'POST');

                var myinput = document.createElement('input');
                myinput.setAttribute('type', 'hidden');
                myinput.setAttribute('name', 'rutaFichero');
                myinput.setAttribute('value', ruta);
                formu.appendChild(myinput);

                var myinput2 = document.createElement('input');
                myinput2.setAttribute('type', 'hidden');
                myinput2.setAttribute('name', 'nombreFichero');
                myinput2.setAttribute('value', 'Solicitud.pdf');
                formu.appendChild(myinput2);

                var myinput3 = document.createElement('input');
                myinput3.setAttribute('type', 'hidden');
                myinput3.setAttribute('name', 'accion');
                myinput3.setAttribute('value', '');
                formu.appendChild(myinput3);
                
				document.body.appendChild(formu);
				formu.submit();
			}
		}else{
			document.getElementById("tdEstadoMutualista").innerText = "No se han recibido datos.";
		}
		if(resultado && resultado[0]==''){
			document.getElementById("tdEstadoMutualista").innerText = "No se han recibido datos.";
		}
	}
	
	function accionActualizaEstados()
	{
		if(document.MutualidadForm.modo.value=="consulta" && document.MutualidadForm.idSolicitud && document.MutualidadForm.idSolicitud.value!=""){
			fin();
			document.MutualidadForm.modo.value = "actualizaEstados";
			var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'<siga:Idioma key="censo.mutualidad.aviso.espera" />');
			if(resultado){
				var estadoMut = resultado[0];
				var estadoSol = resultado[1];
				var ruta = resultado[2];
				if(resultado[0]=='') estadoMut = "No se han recibido datos.";
		    	if(document.getElementById("tdEstadoMutualista")) document.getElementById("tdEstadoMutualista").innerText = estadoMut;
		    	document.getElementById("tdEstadoSolicitud").innerText = estadoSol;
		    	
				if(ruta.length>0 && confirm('�Desea descargar el documento asociado a la solicitud?')){
	
					var formu = document.createElement('form');
	             	formu.setAttribute('name', 'descargar');
	                formu.setAttribute('method', 'POST');
	                formu.setAttribute('action', '/SIGA/ServletDescargaFichero.svrl');
	                formu.setAttribute('target', 'submitArea');
	                formu.setAttribute('method', 'POST');
	
	                var myinput = document.createElement('input');
	                myinput.setAttribute('type', 'hidden');
	                myinput.setAttribute('name', 'rutaFichero');
	                myinput.setAttribute('value', ruta);
	                formu.appendChild(myinput);
	
	                var myinput2 = document.createElement('input');
	                myinput2.setAttribute('type', 'hidden');
	                myinput2.setAttribute('name', 'nombreFichero');
	                myinput2.setAttribute('value', 'Solicitud.pdf');
	                formu.appendChild(myinput2);
	
	                var myinput3 = document.createElement('input');
	                myinput3.setAttribute('type', 'hidden');
	                myinput3.setAttribute('name', 'accion');
	                myinput3.setAttribute('value', '');
	                formu.appendChild(myinput3);
	
					document.body.appendChild(formu);
					formu.submit();
				}
			}else{
				document.getElementById("tdEstadoMutualista").innerText = "No se han recibido datos.";
			}
		}
	}
	
	
	function onchangeBeneficiario()
	{
		
		var textoBeneficiario = jQuery("#idBeneficiario option:selected").text().toLowerCase();
		if(textoBeneficiario.indexOf("otros")>=0){
			document.getElementById("otrosBeneficiarios").style.display = "block";
		}else{
			document.getElementById("otrosBeneficiarios").style.display = "none";
		}
		
	}
	
	consultarNHijos();
	
	function deshabilitaCampos(){
		jQuery('#datosSolicitud :input').attr('readOnly', 'readOnly');
		jQuery('#datosSolicitud :input').addClass('boxConsulta');
		jQuery('#datosSolicitud :input').removeClass('boxCombo');
		jQuery('#datosSolicitud :input').removeClass('box');
	}
	

	
	function habilitaCampos(){
		jQuery('#datosSolicitud :input').removeAttr('readOnly');
		jQuery('#datosSolicitud :input').removeClass('boxConsulta');
		jQuery('#datosSolicitud :input').addClass('box');
		jQuery('#capitalCobertura').addClass('boxConsulta');
		jQuery('#cuotaCobertura').addClass('boxConsulta');
	}
	
	function accionMostrarSolicitud(){
		jQuery('#dialogoSolicitar').hide();
		jQuery('#divSolicitud').show();
		jQuery('#botonera').show();
		if("${MutualidadForm.idTipoSolicitud}"=="S" ){
			jQuery(".planProfesional").hide();
			jQuery(".seguroGratuito").show();
		}else{
			jQuery(".planProfesional").show();
		}
		jQuery(".seguroGratuito").show();
	}
	
	jQuery(document).ready(function() {
		if(("${path}"=="/CEN_Mutualidad")||"${MutualidadForm.modo}"=="consulta"){
			accionMostrarSolicitud();
		}else{
			jQuery('#dialogoSolicitar').show();
			jQuery('#divSolicitud').hide();
			jQuery('#botonera').hide();
			jQuery(".planProfesional").hide();
		}
		cargaCombos();
		if("${MutualidadForm.idTipoSolicitud}"=="P" ){
			jQuery('#divSolicitud').css("overflow-y","auto");
		}
		if("<%=accion%>"=="ver"){
			jQuery('#botonSolicitarAltaSeguro').attr("disabled", "disabled");
		}
		accionActualizaEstados();
		postEuros();
	});
	
	
</script>
	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"/>
</body>
	
</html>



