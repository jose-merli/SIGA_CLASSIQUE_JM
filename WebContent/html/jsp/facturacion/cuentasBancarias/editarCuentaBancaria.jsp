<!DOCTYPE html>
<html>
<head>
<!--editarCuentaBancaria.jsp -->

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

<%@ page import="org.redabogacia.sigaservices.app.autogen.model.FacSeriefacturacion"%>
<%@ page import="java.util.List"%>
<%@ page import="com.siga.facturacion.form.CuentasBancariasForm"%>
<%@ page import="java.util.ArrayList"%>

<!-- HEAD -->

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script src="<html:rewrite page="/html/js/SIGA.js?v=${sessionScope.VERSIONJS}"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>

<% 
	List<FacSeriefacturacion> lista = (List<FacSeriefacturacion>) request.getAttribute("seriesFacturacion");

	CuentasBancariasForm formulario = (CuentasBancariasForm) request.getAttribute("CuentasBancariasForm");

	ArrayList vIva = new ArrayList(); // valores originales iva
	// Cargo valor IVA
    vIva.add(formulario.getComisioniva());
%>

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
				
				//EN ESTE MODULO NO ACEPTAN IBAN EXTRANJEROS POR PROBELMAS CON LA SUCURSALES PARA EL FICHERO BANCARIO
				if(iban.length > 2 && iban.substring(0,2) != 'ES'){
					alert(mensaje+". Sólo se admiten códigos IBAN españoles");
					document.getElementById("IBAN").value="";
					document.getElementById("BIC").value="";
					document.getElementById("bancoNombre").value="";
					document.getElementById("BIC").readOnly = true;
					document.getElementById("BIC").className = "boxConsulta";						
				
				} else {			
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
										document.getElementById("BIC").value=bic;
										document.getElementById("BIC").readOnly = true;
										document.getElementById("BIC").className = "boxConsulta";
									
										//Se rellena el banco
										var txtBanco = json.banco.nombre;
										document.getElementById("bancoNombre").value=txtBanco;
									} else {
										alert(mensaje);
										document.getElementById("BIC").value="";
										document.getElementById("bancoNombre").value="";
										document.getElementById("BIC").readOnly = true;
										document.getElementById("BIC").className = "boxConsulta";
										fin();
									}
									
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
							alert(mensaje);
							document.getElementById("BIC").value="";
							document.getElementById("bancoNombre").value="";
							document.getElementById("BIC").readOnly = true;
							document.getElementById("BIC").className = "boxConsulta";
							fin();
						}
					});
				}
				
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
					alert(mensaje);
					fin();
				}
			});
		}
	}
	
	jQuery(function($){		
		if(document.CuentasBancariasForm.modo.value == 'modificar'){
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
	
	function rpad() {
		if (document.getElementById("BIC").value.length == 8){
	    	while (document.getElementById("BIC").value.length < 11)
	    		document.getElementById("BIC").value = document.getElementById("BIC").value + 'X';
		}
	}
	
	function habilitarDeshabCombo()
	{ 
		if(document.CuentasBancariasForm.modo.value != 'abrir'){
			
			if(document.getElementById("sjcs").checked) {
				document.getElementById("comboSufijossjcs").disabled =false;
				
			}else {
				document.getElementById("comboSufijossjcs").disabled=true;
				document.getElementById("comboSufijossjcs").value="";
			}
			
		}else{
			document.getElementById("comboSufijossjcs").disabled=true;
			document.getElementById("comboSufijos").disabled=true;
			
		}
	}
	</script>
</head>

<body onload="inicio(); inicioCargarBancoBIC(); habilitarDeshabCombo();">

	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="menu.facturacion.gestionCuentasBancarias"/>
			</td>
		</tr>
	</table>
	
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:javascript formName="CuentasBancariasForm" staticJavascript="false" />
	 
	<html:form action="${path}" target="submitArea">
		<html:hidden property="modo" value ="${CuentasBancariasForm.modo}"  />
		<html:hidden property="idInstitucion" value ="${CuentasBancariasForm.idInstitucion}"/>
		<html:hidden property="idCuentaBancaria" value ="${CuentasBancariasForm.idCuentaBancaria}"/>
		<html:hidden property="idSerieFacturacion" value =""/>
		<html:hidden property="bancosCodigo" value ="${CuentasBancariasForm.bancosCodigo}"/>
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
			<html:hidden property="listaSeries"/>
		</c:if>
	
		<c:set var="disabledFecha" value="false" />
		<c:if test="${CuentasBancariasForm.modo=='abrir'}">
			<c:set var="disabledFecha" value="true" />
		</c:if>
	
		<table width="100%" border="0">
			<!-- FILA -->
			<tr>						
				<td class="labelText" nowrap>
					<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoIBAN"/>&nbsp;(*)
				</td>
				<td>
					<html:text size="34"  maxlength="34" name="CuentasBancariasForm" styleId="IBAN" property="IBAN" styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}" onblur="cargarBancoPorIBAN();" />
				</td>

				<td class="labelText" nowrap>
					<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBIC"/>
				</td>
				<td>
					<html:text size="14"  maxlength="11" name="CuentasBancariasForm" styleId="BIC" property="BIC" styleClass="boxConsulta" readonly="true" onblur="rpad();" />
				</td>
				
				<c:if test="${CuentasBancariasForm.modo != 'insertar'}">	
					<td class="labelText">
						<siga:Idioma key="facturacion.cuentasBancarias.tieneBaja"/> 
					</td>
					<td>
						<siga:Fecha nombreCampo="fechaBaja" valorInicial="${CuentasBancariasForm.fechaBaja}" disabled="${disabledFecha}" readonly="${disabledFecha}"/>
					</td>		
				</c:if>	
			</tr>
			
			<!-- FILA -->
			<tr>	
				<td class="labelText" nowrap>
					<siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/>
				</td>
				<td colspan="5">
					<html:text style="width:500px;" name="CuentasBancariasForm" property="bancoNombre" styleClass="boxConsulta" readonly="true"/>
				</td>
			</tr>
						
			<tr>
				<td class="labelText">
					<siga:Idioma key="facturacion.cuentasBancarias.asientoContable"/>
				</td> 
				<td>
					<html:text styleId="asientoContable" property="asientoContable"	name="CuentasBancariasForm" size="20" maxlength="20" styleClass="box" />
				</td>
				
				<td class="labelText">
					<siga:Idioma key="facturacion.cuentasBancarias.cuentaContableTarjeta"/>
				</td>				
				<td>
					<html:text styleId="cuentaContableTarjeta" property="cuentaContableTarjeta"	name="CuentasBancariasForm" size="20" maxlength="20" styleClass="box" />
				</td>

				<c:if test="${CuentasBancariasForm.modo != 'insertar'}">						
					<td class="labelText">
						<siga:Idioma key="general.code"/>
					</td> 
					<td>
						<html:text styleId="codigo" property="idCuentaBancaria"	name="CuentasBancariasForm" size="20" maxlength="20" styleClass="boxConsulta" readonly="true" />
					</td>
				</c:if>									
			</tr>
			
			<c:if test="${CuentasBancariasForm.modo != 'insertar' && CuentasBancariasForm.cuentaBanco != null &&  CuentasBancariasForm.cuentaBanco != ''}">	
				<tr>
					<td colspan="6">
						<siga:ConjCampos leyenda="datosCuentaBancaria.literal.formatoAntiguo">
							<table>
								<tr>						
									<td class="labelText" nowrap>C.C.C.&nbsp;(*)</td>
									<td class="labelText">
										<html:text size="4"  maxlength="4"  name="CuentasBancariasForm" property="codigoBanco"   	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>-
										<html:text size="4"  maxlength="4"  name="CuentasBancariasForm" property="sucursalBanco" 	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>-
										<html:text size="2"  maxlength="2"  name="CuentasBancariasForm" property="digControlBanco" 	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}" ></html:text>-
										<html:text size="10" maxlength="10" name="CuentasBancariasForm" property="cuentaBanco"  	styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"></html:text>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>				
			</c:if>
				
			<tr>
				<td colspan="6">
					<siga:ConjCampos leyenda="facturacion.cuentasBancarias.comision">
						<table width="100%" border="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.cuentasBancarias.comision.importe"/>&nbsp;(*)
								</td>
								<td>
									<html:text styleClass="boxNumber" styleId="comisionimporte" property="comisionimporte" name ="CuentasBancariasForm" size="13" maxlength="13" />&nbsp;&euro;
								</td>
								
								<td class="labelText">
									<siga:Idioma key="facturacion.cuentasBancarias.comision.IVA"/>&nbsp;(*)
								</td>
								<td>
									<c:choose>
										<c:when test="${CuentasBancariasForm.modo=='insertar'}">
											<siga:Select queryId="getPorcentajesIva" id="comisioniva" required="true"/>
										</c:when>
										
										<c:when test="${CuentasBancariasForm.modo=='modificar'}">
											<siga:Select queryId="getPorcentajesIva" id="comisioniva" selectedIds="<%=vIva%>" required="true"/>
										</c:when>	
										
										<c:otherwise>
											<siga:Select queryId="getPorcentajesIva" id="comisioniva" selectedIds="<%=vIva%>" required="true" disabled="true" />
										</c:otherwise>
									</c:choose>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="facturacion.cuentasBancarias.comision.cuentaContable"/>
								</td>				
								<td>
									<html:text styleId="comisioncuentacontable" property="comisioncuentacontable" name="CuentasBancariasForm" size="20" maxlength="20" styleClass="box" />
								</td>																
							</tr>
					
							<tr>	
								<td class="labelText">
									<siga:Idioma key="facturacion.cuentasBancarias.comision.descripcion"/>&nbsp;(*)
								</td>
								<td colspan="5">
									<html:text styleClass="box" styleId="comisiondescripcion" property="comisiondescripcion" name ="CuentasBancariasForm" maxlength="255" style="width:850px;" />
								</td>			
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td colspan="6">
					<siga:ConjCampos leyenda="Uso de Cuenta">
						<table>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.cuentasBancarias.uso.sjcs"/>
								</td>								
								<td> 
									<html:checkbox name="CuentasBancariasForm" id = "sjcs" property="sjcs" value="1" onclick="habilitarDeshabCombo()"></html:checkbox>
								</td>
								<td>
									<html:select styleId="comboSufijossjcs" property="idSufijosjcs" value="${CuentasBancariasForm.idSufijosjcs}" styleClass="boxCombo" style="width:200px;">
										<s:if test="${empty CuentasBancariasForm.idSufijosjcs}">
											<html:option value=""><c:out value=""/></html:option>
										</s:if>
						
										<c:forEach items="${listaSufijos}" var="sufijoCmb">
											<html:option value="${sufijoCmb.idSufijo}">
											<c:if	test="${sufijoCmb.sufijo.trim().length()>0}">
												<c:out value="${sufijoCmb.sufijo} ${sufijoCmb.concepto}"/>
											</c:if>
											<c:if	test="${sufijoCmb.sufijo.trim().length()==0}">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoCmb.concepto}"/>
											</c:if>
											</html:option>
										</c:forEach>
									</html:select>	
								</td>
							</tr> 		
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	
		<c:if test="${CuentasBancariasForm.modo!='insertar'}">
			
  			<c:set var="botonesList" value=""></c:set>		
 			<div id="divListadoCuentasBancarias" style='height: 100%; position: absolute; width: 100%; overflow-y: auto'> 
					<siga:Table 
						name="listado" 
						border="1" 
						columnNames="Serie,Descripción,Sufijo," 
						columnSizes="29,45,21,5">
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
						  			botones="${botonesList}"
						  			visibleBorrado="N"
						  			visibleEdicion="N"
						  			visibleConsulta="N"
						  			clase="listaNonEdit">
										<td align='left'>
											<input type="hidden" name="idseriefacturacion_${status.count}" id="idseriefacturacion_${status.count}" value="${serieFacturacion.idseriefacturacion}">
											<input type="hidden" name="bancos_codigo_${status.count}" id="bancos_codigo_${status.count}" value="${serieFacturacion.bancos_codigo}">
											<c:out	value="${serieFacturacion.nombreabreviado}" />
										</td>
										<td align='left'>
											<c:out value="${serieFacturacion.descripcion}" />
										</td>
										<td align='left'>	
											<html:select styleId="comboSufijosSerie_${status.count}" property="idSufijoSerie" value="${serieFacturacion.idsufijo}" styleClass="boxCombo" style="width:200px;">
												<s:if test="${empty serieFacturacion.idsufijo}">
													<html:option value=""><c:out value=""/></html:option>
												</s:if>
												<c:forEach items="${listaSufijos}" var="sufijoSerieCmb">											
													<html:option value="${sufijoSerieCmb.idSufijo}">
													<c:if	test="${sufijoSerieCmb.sufijo.trim().length()>0}">
														<c:out value="${sufijoSerieCmb.sufijo} ${sufijoSerieCmb.concepto}"/>
													</c:if>
													<c:if	test="${sufijoSerieCmb.sufijo.trim().length()==0}">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoSerieCmb.concepto}"/>
													</c:if>
													</html:option>
												</c:forEach>
											</html:select>	
										</td>
								</siga:FilaConIconos>
							</c:forEach>
						</c:otherwise>
					</c:choose>
	
				</siga:Table>
 			</div> 
		</c:if>		
	</html:form>

	<c:choose>
		<c:when test="${CuentasBancariasForm.modo=='insertar'}">
			<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
		</c:when>
		
		<c:when test="${CuentasBancariasForm.modo=='modificar'}">
			<c:choose>
				<c:when test="${CuentasBancariasForm.fechaBaja.length()>0}">
					<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
				</c:when>

				<c:otherwise>				
					<siga:ConjBotonesAccion botones="RA,Y,R,C" modal="P" />
				</c:otherwise>
			</c:choose>
		</c:when>	
		
		<c:otherwise>
			<siga:ConjBotonesAccion botones="C" modal="P" />
		</c:otherwise>
	</c:choose>
	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

<script type="text/javascript">	
	function accionGuardarCerrar() {
		sub();

		//Se quita la mascara al guardar 
		document.CuentasBancariasForm.IBAN.value = formateaMask(document.getElementById("IBAN").value);
		
		if(document.CuentasBancariasForm.modo.value=='insertar'){
			iban = document.CuentasBancariasForm.IBAN.value;
			bic = document.CuentasBancariasForm.BIC.value;
			banco = document.CuentasBancariasForm.bancoNombre.value;

			//SE VALIDA SI SE HA INTODUCIDO IBAN Y BIC
			if (iban == ""  && bic == ""){ 
				mensaje = "<siga:Idioma key='messages.censo.cuentasBancarias.errorCuentaBancaria'/>";
				alert(mensaje);
				fin();
				return false;
			} 
			
			if(!validarCuentaBancaria(iban,bic,banco)){
				fin();
				return false;
			}

		}
		
		if(validateCuentasBancariasForm(document.CuentasBancariasForm)){
			
			//Si está marcado el check de SJCS tiene que asignarse un sufijo
			if(document.getElementById("sjcs").checked) {
				
				if(document.CuentasBancariasForm.idSufijosjcs.value<1) {
					mensaje = "<siga:Idioma key='facturacion.sufijos.message.errorCuentaBancariaSufijoSJCS'/>";
					alert(mensaje);
					fin();
					return false;
				}
					
					
			}
			
			//Se pasan los valores del idserie y el sufijo de las series de la tabla para actualizar el sufijo en bbdd
<%
			if (lista!=null) {
%>			

				var contadorLista = <%=lista.size()%>;
				var datos = "";
				
				for(i=1; i<=contadorLista; i++) {
					var datosFila = jQuery("#comboSufijosSerie_" + i);				
					var datosSerie = jQuery("#idseriefacturacion_" + i);
					var datosBanco = jQuery("#bancos_codigo_" + i);

						datos = datos + datosSerie.val() + "," + datosBanco.val()+","+ datosFila.val() + ";" ;				
						
// 						Todas las series de la lista deben tener un sufijo asociado
						if(!datosFila.val()){
							mensaje = "<siga:Idioma key='facturacion.sufijos.message.error.cuenta.serie.sufijo'/>";
							alert(mensaje);
							fin();
							return false;
						}
							
						
				}
				
				document.CuentasBancariasForm.listaSeries.value=datos;
<% 
			}
%>			
				
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
			if (document.getElementById("seriesFacturacion").clientHeight < document.getElementById("divListadoSeriesFacturacion").clientHeight) 
			{
				document.getElementById("tabCuentasBancarias").width='100%';				   
			} else {
				  document.getElementById("tabCuentasBancarias").width='98.43%';				   
			  }
		}
	}
	
	function accionRelacionarSerieFact(){
 		document.CuentasBancariasForm.modo.value = "seriesDisponibles";
		var resultado=ventaModalGeneral(document.CuentasBancariasForm.name,"P");
		refrescarLocal();	
	}
	
	function refrescarLocal(){
		parent.close();
		document.CuentasBancariasForm.modo.value="editar";
		var resultado=ventaModalGeneral(document.CuentasBancariasForm.name,"G");
	}

	
</script>

</body>

</html>