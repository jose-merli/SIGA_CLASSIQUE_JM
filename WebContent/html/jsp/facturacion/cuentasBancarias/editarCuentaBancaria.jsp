<!DOCTYPE html>
<html>
<head>
<!--editarCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- HEAD -->

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script src="<html:rewrite page="/html/js/SIGA.js?v=${sessionScope.VERSIONJS}"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script type="text/javascript">
	function inicio() {
		ajustarCabeceraTabla();
		if(document.CuentasBancariasForm.modo.value=='abrir'){
			habilitar(false);
		}
	}
	
	function habilitar(isHabilitar){
		
		if(isHabilitar==true){
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="checkbox") {
					jQuery.removeAttr(input,"disabled");
				} else if(input.type!="button") {
					input.className = "box";
					jQuery.removeAttr(input,"readonly");
				}
			}
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i <selects.length ; i++) {
				select = selects[i];
				jQuery.removeAttr(select,"disabled");
			}
			
		}else{
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i < inputs.length ; i++) {
				var input = inputs[i];
				if(input.type=="checkbox"){
					jQuery.attr(input,"disabled","disabled");
				} else if(input.type!="button"){
					input.className = "boxConsulta";
					jQuery.attr(input,"readonly","readonly");
				}
			}
			
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i < selects.length ; i++) {
				var select = selects[i];
				jQuery.attr(select,"disabled","disabled");
			}
		}
	}
	
	function cargarBancoPorIBAN(){		
		if(document.CuentasBancariasForm.modo.value =='insertar'){
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
								var bic = json.banco.bic;
								document.getElementById("BIC").value=bic;
								document.getElementById("BIC").readOnly = true;
								document.getElementById("BIC").className = "boxConsulta";
								
								//Se rellena el banco
								var txtBanco = json.banco.nombre;
								document.getElementById("bancoNombre").value=txtBanco;
							}else{
								document.getElementById("BIC").readOnly = false;
								document.getElementById("BIC").className = "box";
								document.getElementById("bancoNombre").value="";
								document.getElementById("BIC").value="";
								alert("Rellene el BIC para el banco extranjero");
							}
							
						}else{
							alert(mensaje);
							document.getElementById("BIC").value="";
							document.getElementById("bancoNombre").value="";
							document.getElementById("BIC").readOnly = true;
							document.getElementById("BIC").className = "boxConsulta";
						}
						fin();
					},
					error: function(e){
						alert(mensajeGeneralError);
						document.getElementById("BIC").value="";
						document.getElementById("bancoNombre").value="";
						document.getElementById("BIC").readOnly = true;
						document.getElementById("BIC").className = "boxConsulta";
						fin();
					}
				});
				
			} else {
				document.getElementById("IBAN").value="";
				document.getElementById("BIC").value="";
				document.getElementById("bancoNombre").value="";
				document.getElementById("BIC").readOnly = true;
				document.getElementById("BIC").className = "boxConsulta";
			}
		}
	}	
	
	function inicioCargarBancoBIC(){
		var iban = formateaMask(document.getElementById("IBAN").value);
		var codigoBanco = "${CuentasBancariasForm.codigoBanco}";
		if (iban!=undefined && iban!="") {			
			jQuery.ajax({ //Comunicacion jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxCargaInicialBancoBIC",
				data: "iban="+iban+"&codigo="+codigoBanco,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){	
					if(json.banco!=null && json.banco!=""){
						document.getElementById("BIC").value=json.banco.bic;
						document.getElementById("bancoNombre").value=json.banco.nombre;
					}
					fin();
				},
				error: function(e){
					alert(mensajeGeneralError);
					fin();
				}
			});
		}
	}
	
	jQuery(function($){		
		if(document.CuentasBancariasForm.modo.value !='insertar'){
			var defaultValue = jQuery("#IBAN").val();
			if(defaultValue.length <= 34){
				jQuery('#IBAN').show();
			}else{
				jQuery('#IBAN').hide();
				
			}
			jQuery("#IBAN").mask("AA AA AAAA AAAA AAAA AAAA AAAA AAAA AAAA AA");
			jQuery("#IBAN").keyup();	
		}
	});				
	
	</script>
</head>

<body onload="inicio();inicioCargarBancoBIC();">

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="menu.facturacion.gestionCuentasBancarias"/>
		</td>
	</tr>
</table>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:javascript formName="CuentasBancariasForm" staticJavascript="false" /> 
<html:form action="${path}" target="submitArea">
	<html:hidden property="modo" value ="${CuentasBancariasForm.modo}"  />
	<html:hidden property="idInstitucion" value ="${CuentasBancariasForm.idInstitucion}"/>
	<html:hidden property="idCuentaBancaria" value ="${CuentasBancariasForm.idCuentaBancaria}"/>
    <input type="hidden" id="actionModal" name="actionModal" />
    <c:set var="clasePorEdicion" value="box" />
    <c:set var="disabledPorEdicion" value="false" />
	<c:if	test="${CuentasBancariasForm.modo=='modificar'}">
		<c:set var="clasePorEdicion" value="boxConsulta" />
		<c:set var="disabledPorEdicion" value="true" />
		<html:hidden property="codigoBanco" value ="${CuentasBancariasForm.codigoBanco}"/>
		<html:hidden property="sucursalBanco" value ="${CuentasBancariasForm.sucursalBanco}"/>
		<html:hidden property="cuentaBanco" value ="${CuentasBancariasForm.cuentaBanco}"/>
		<html:hidden property="digControlBanco" value ="${CuentasBancariasForm.digControlBanco}"/>
	</c:if>
	
	<c:set var="disabledFecha" value="false" />
	<c:if test="${CuentasBancariasForm.modo=='abrir'}">
		<c:set var="disabledFecha" value="true" />
	</c:if>
	
	<table width="100%" border="0">
			<!-- FILA -->
			<tr>						
				<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoIBAN"/>&nbsp;(*)</td>
				<td class="labelText"><html:text size="34"  maxlength="34" name="CuentasBancariasForm" styleId="IBAN" property="IBAN" styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}" onblur="cargarBancoPorIBAN();"></html:text></td>

				<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBIC"/>&nbsp;(*)</td>
				<td class="labelText"><html:text size="14"  maxlength="11" name="CuentasBancariasForm" styleId="BIC" property="BIC"   styleClass="boxConsulta" readonly="true" ></html:text></td>
				
				<td class="labelText"><bean:message key="facturacion.cuentasBancarias.sjcs" /> </td>
				<td align="left"><html:checkbox name="CuentasBancariasForm" property="sjcs" value="1" ></html:checkbox></td>
				
				<c:if test="${CuentasBancariasForm.modo != 'insertar'}">	
					<td class="labelText"><bean:message key="facturacion.cuentasBancarias.baja" /> </td>
					<td><siga:Fecha nombreCampo="fechaBaja" valorInicial="${CuentasBancariasForm.fechaBaja}" disabled="${disabledFecha}" readonly="${disabledFecha}"/></td>		
				</c:if>	
			</tr>
			
			<!-- FILA -->
			<tr>	
				<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
				<td class="labelText" COLSPAN="3">
					<html:text style="width:500px;" name="CuentasBancariasForm" property="bancoNombre" styleClass="boxConsulta"  readonly="true"/>
				</td>
			</tr>
						
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.asientoContable" /> </td>
			<td><html:text styleId="asientoContable" property="asientoContable"	name ="CuentasBancariasForm"	size="20" maxlength="20" styleClass="box" /></td>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.cuentaContableTarjeta" /> </td>
			<td><html:text styleId="cuentaContableTarjeta" property="cuentaContableTarjeta"	name ="CuentasBancariasForm"	size="20" maxlength="20" styleClass="box" /></td>
	
		</tr>
		<c:if test="${CuentasBancariasForm.modo != 'insertar' && CuentasBancariasForm.cuentaBanco != null &&  CuentasBancariasForm.cuentaBanco != null}">	
			<!-- FILA -->
			<tr><td COLSPAN="8">
				<siga:ConjCampos leyenda="Cuenta Antigua">
					<table>
						<tr>						
							<td class="labelText" nowrap colspan="2">C.C.C.&nbsp;(*)</td>
							<td class="labelText">
								<html:text size="4"  maxlength="4"  name="CuentasBancariasForm" property="codigoBanco"   	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>-
								<html:text size="4"  maxlength="4"  name="CuentasBancariasForm" property="sucursalBanco" 	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>-
								<html:text size="2"  maxlength="2"  name="CuentasBancariasForm" property="digControlBanco" 	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}" ></html:text>-
								<html:text size="10" maxlength="10" name="CuentasBancariasForm" property="cuentaBanco"  	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td></tr>				
		</c:if>	
		<!-- FILA -->
		<tr><td COLSPAN="8">
			<siga:ConjCampos leyenda="Importes Comision">
				<table align="center" width="100%">
					<tr>
						<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionPropiaCargo" />&nbsp;(*)</td>
						<td><html:text styleId="impComisionPropiaCargo" property="impComisionPropiaCargo" name ="CuentasBancariasForm" 	size="13" maxlength="13" styleClass="box" /></td>
						<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionPropiaAbono" />&nbsp;(*)</td>
						<td><html:text styleId="impComisionPropiaAbono" property="impComisionPropiaAbono" name ="CuentasBancariasForm"	size="13" maxlength="13" styleClass="box" /></td>
					</tr>
					
					<tr>
						<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionAjenaCargo" />&nbsp;(*)</td>
						<td><html:text styleId="impComisionAjenaCargo" property="impComisionAjenaCargo" name ="CuentasBancariasForm" size="13" maxlength="13" styleClass="box" /></td>
						<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionAjenaAbono" />&nbsp;(*)</td>
						<td><html:text styleId="impComisionAjenaAbono" property="impComisionAjenaAbono"	name ="CuentasBancariasForm" size="13" maxlength="13" styleClass="box" /></td>
				
					</tr>
				</table>
			</siga:ConjCampos>
		</td></tr>	
	</table>
	
		<c:if test="${CuentasBancariasForm.modo!='insertar'}">
						
			<div id="divListadoCuentasBancarias" style='height: 100%; position: absolute; width: 100%; overflow-y: auto'>
					<siga:Table 
						name="listado" 
						border="1" 
						columnNames="Serie,Descripción" 
						columnSizes="50,50">
	
					<c:choose>
		   				<c:when test="${empty seriesFacturacion}">
			   				<tr class="notFound">
				   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr>	 		
				 			
			   			</c:when>
			   			<c:otherwise>
				   			
							<c:forEach items="${seriesFacturacion}" var="serieFacturacion" varStatus="status">									
								<siga:FilaConIconos	fila='${status.count}'				    
						  			pintarEspacio="no"
						  			botones=""
						  			visibleBorrado="N"
						  			visibleEdicion="N"
						  			visibleConsulta="N"
						  			clase="listaNonEdit">
										<td align='left'><c:out	value="${serieFacturacion.nombreabreviado}" /></td>
										<td align='left' style="white-space: pre-line;"><c:out value="${serieFacturacion.descripcion}" /></td>
								</siga:FilaConIconos>
							</c:forEach>
							
						</c:otherwise>
					</c:choose>
	
				</siga:Table>
			</div>
		</c:if>
		

	</html:form>
<c:choose>
	<c:when test="${CuentasBancariasForm.modo!='abrir'}">
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	</c:when>
	<c:otherwise>
		<siga:ConjBotonesAccion botones="C" modal="P" />
	</c:otherwise>
</c:choose>

	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

<script type="text/javascript">

	function validarDigControl(){
		mensaje = "<siga:Idioma key='messages.censo.cuentasBancarias.errorCuentaBancaria'/>";
		iban = document.CuentasBancariasForm.IBAN.value;
		bic = document.CuentasBancariasForm.BIC.value;
		banco = document.CuentasBancariasForm.bancoNombre.value;
		
		if (iban == ""  && bic == ""){ 
			alert(mensaje);
			return false;
		
		} else {
			if(iban.substring(0,2) == 'ES' && banco==""){
				alert(mensaje);
				return false;
			}
			if(iban.length < 4 || (iban.substring(0,2) != 'ES' && bic.length != 11)){
				alert(mensaje);
				return false;
			}else{
				//Si el IBAN es español se valida el digito de contro de la cuenta bancaria como se hacía antiguamente
				if(iban.substring(0,2) == 'ES' && iban.length == 24){
					if(!calcularDigitoCuentaBancariaEspañola(iban.substring(4))){
						return false;
					}
				}
				//VALIDACION DEL DIGITO DE CONTROL DEL IBAN
				if(!validarIBAN(iban)){
					alert(mensaje);
					return false;
				}
			}
		}
		
		return true;  
	}	
	
	
	function accionGuardarCerrar() {
		sub();
		
		//Se quita la mascara al guardar 
		document.CuentasBancariasForm.IBAN.value = formateaMask(document.getElementById("IBAN").value);
		
		if(document.CuentasBancariasForm.modo.value=='insertar'){
			if(!validarDigControl()){
				fin();
				return false;
			}
		}
		
		if(validateCuentasBancariasForm(document.CuentasBancariasForm)){
			document.CuentasBancariasForm.submit();
		}else{
			fin();
			return false; 
		}
	}

	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}

	function accionRestablecer() 
	{		
		document.CuentasBancariasForm.reset();
		document.getElementById("BIC").readOnly = true;
		document.getElementById("BIC").className = "boxConsulta";
		inicioCargarBancoBIC();
	}
	
	function ajustarCabeceraTabla(){
		if(document.getElementById("seriesFacturacion")){
			if (document.getElementById("seriesFacturacion").clientHeight < document.getElementById("divListadoSeriesFacturacion").clientHeight) {
				document.getElementById("tabCuentasBancarias").width='100%';				   
			  } else {
				  document.getElementById("tabCuentasBancarias").width='98.43%';				   
			  }
		}
	}
	
</script>

</body>

</html>