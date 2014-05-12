<!DOCTYPE html>
<html>
<head>
<!-- mandatoCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenMandatosCuentasBancariasBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.atos.utils.ClsConstants"%>

<!-- JSP -->
<% 
	UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String nombrePersona = (String) request.getParameter("nombrePersona");
	String numero = (String) request.getParameter("numero");
	String modoMandato = (String) request.getParameter("modoMandato");
	
	CenMandatosCuentasBancariasBean beanMandato = (CenMandatosCuentasBancariasBean) request.getAttribute("beanMandato");		
	
	boolean modoConsulta = false;
	String claseEdicion = "box";
	if (modoMandato.equals("ver") || (beanMandato.getFirmaFecha()!=null && !beanMandato.getFirmaFecha().equals("")) || (beanMandato.getFechaUso()!=null && !beanMandato.getFechaUso().equals(""))) {
		claseEdicion = "boxConsulta";
		modoConsulta = true;
	}
	
	String iban = beanMandato.getIban();	
	if (modoMandato.equals("ver")) {
		iban = UtilidadesString.mostrarIBANConAsteriscos(iban);
	} else {		
		iban =  UtilidadesString.mostrarDatoMascara(iban, ClsConstants.MASK_IBAN);
	}
	
	// JPT: Gestion de Volver y botones
	String botones = "";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null || usuario.isLetrado()) {
		busquedaVolver = "volverNo";
		
		if (!modoConsulta) {
			botones = "G,R";
		}		
		
	} else {
		if (modoConsulta) {
			botones = "V";
		} else {
			botones = "V,G,R";
		}
	}
	
	ArrayList<String> arrayDeudorTipoId = new ArrayList<String>(), arrayAcreedorTipoId = new ArrayList<String>();
	arrayDeudorTipoId.add(beanMandato.getDeudorTipoId());
	arrayAcreedorTipoId.add(beanMandato.getAcreedorTipoId());
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="MandatosCuentasBancariasForm" staticJavascript="false" />
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>
	
	<siga:TituloExt titulo="pestana.fichaCliente.datosBancarios.mandato" localizacion="censo.fichaCliente.bancos.mandatos.mandato.localizacion" />
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script>			
		jQuery.noConflict();
	
		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		
		jQuery(function(){						
			controlarAutorizacion("<%=beanMandato.getEsquema()%>");
			
			calcularAltura();
			
			// INICIO = Quitar este codigo para habilitar COR1 y B2B
			jQuery("input[id=esquema][value=1]").attr("disabled","disabled");
			jQuery("input[id=esquema][value=2]").attr("disabled","disabled");
			// FIN = Quitar este codigo para habilitar COR1 y B2B
		});	
		
		function controlarAutorizacion(valorEsquema) {
			if (valorEsquema == 2) {
				jQuery("#autorizacionB2BCheck").removeAttr("disabled");
			} else {
				jQuery('#autorizacionB2BCheck').removeAttr("checked");
				jQuery("#autorizacionB2BCheck").attr("disabled","disabled");
			}
		}	    
		
		function refrescarLocal() {
			document.MandatosCuentasBancariasForm.target = "_self";
			document.MandatosCuentasBancariasForm.modo.value = "mandatoCuentaBancaria";		
			document.MandatosCuentasBancariasForm.submit();	
		}				
	
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				refrescarLocal();
			}						
		}		
		
		// Asociada al boton Guardar
		function accionGuardar(){		
			sub();		
			
			// Realizo las validaciones de la pagina
			var errores = "";
			
			if (trim(jQuery("#refMandatoSepa").val()) == "") {
				errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.datosBancarios.referencia'/>"+ '\n';				
			}
			
			if (jQuery("input[id=esquema]:checked").val() == 2 && !jQuery("#autorizacionB2BCheck")[0].checked) {
				errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.datosBancarios.autorizacion.b2b'/>"+ '\n';				 
			}
			
			if (jQuery("#deudorTipoId").val()=="<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>" || jQuery("#deudorTipoId").val()=="<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>") {
				if (!validarNIFCIF(jQuery("#deudorTipoId").val(), jQuery("#deudorId").val())) {
					errores += "<siga:Idioma key='errors.invalid' arg0='censo.fichaCliente.datosBancarios.identificador'/>"+ '\n';
				}
			
			} else if (jQuery("#deudorTipoId").val()=="<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") {
				if (!validaNIE(jQuery("#deudorId").val())) {
					errores += "<siga:Idioma key='errors.invalid' arg0='censo.fichaCliente.datosBancarios.identificador'/>"+ '\n';	
				}
			}				
			
			if (jQuery("#deudorTipoId").val()=="" || jQuery("#deudorId").val() == "") {
				errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.datosBancarios.identificador'/>"+ '\n';				
			}			
			
			if (errores != "") {
				alert(errores);
				fin();
				return false;
			}					
			
		   	if (!validateMandatosCuentasBancariasForm(document.MandatosCuentasBancariasForm)){
				fin();
				return false;
			}
		   	
		 // Elimino los blancos del identificador del mandato SEPA
			document.MandatosCuentasBancariasForm.refMandatoSepa.value = trim(jQuery("#refMandatoSepa").val());
			
			// Transformo el check al formulario
			if (jQuery("#autorizacionB2BCheck")[0].checked) {
				document.MandatosCuentasBancariasForm.autorizacionB2B.value = "1";
			}							
			
			document.MandatosCuentasBancariasForm.target = "submitArea";
			document.MandatosCuentasBancariasForm.modo.value="modificarMandatoCuentaBancaria";		
			document.MandatosCuentasBancariasForm.submit();
		}	
		
	    // Funcion que calcula la altura de la pagina de un div con los botones del final
	    function calcularAltura() {		
			if (document.getElementById("idBotonesAccion")) {
				var tablaBotones = jQuery('#idBotonesAccion')[0];						
				var divDatos = jQuery('#scrollValores')[0];
			
				var posTablaBotones = tablaBotones.offsetTop;
				var posDivDatos = divDatos.offsetTop;
			
				jQuery('#scrollValores').height(posTablaBotones - posDivDatos);			
			}		
		}			
	</script>
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" height="32px">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;

<%
				if ((numero != null) && (!numero.equalsIgnoreCase(""))) {
%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
<%
				} else {
%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<%
				}
%>
			</td>
		</tr>
	</table>	
	
	<html:form action="/CEN_MandatosCuentasBancarias.do" method="POST">
		<html:hidden name="MandatosCuentasBancariasForm" property="modo" styleId="modo" value="" />		
		<html:hidden name="MandatosCuentasBancariasForm" property="idInstitucion" styleId="idInstitucion" value="<%=beanMandato.getIdInstitucion()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idPersona" styleId="idPersona" value="<%=beanMandato.getIdPersona()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idCuenta" styleId="idCuenta" value="<%=beanMandato.getIdCuenta()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idMandato" styleId="idMandato" value="<%=beanMandato.getIdMandato()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="nombrePersona" styleId="nombrePersona" value="<%=nombrePersona%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="numero" styleId="numero" value="<%=numero%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="modoMandato" styleId="modoMandato" value="<%=modoMandato%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="autorizacionB2B" styleId="autorizacionB2B" value="0" />
		
		<div id="scrollValores" style="height:100%; width:100%; overflow-y: auto; overflow-x: hidden; border: white;">
	
			<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.datosGenerales">
				<table cellpadding="0" border="0">
					<tr>
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.referencia"/>&nbsp;(*)</td>
						<td><html:text name="MandatosCuentasBancariasForm" property="refMandatoSepa" styleId="refMandatoSepa" maxlength="35" styleClass="<%=claseEdicion%>" readonly="<%=modoConsulta%>" style='width:350px;' value="<%=beanMandato.getRefMandatoSepa()%>" />
						
						<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.fechaUso"/></td>
						<td><html:text name="MandatosCuentasBancariasForm" property="fechaUso" styleId="fechaUso" styleClass="boxConsulta" readonly="true" value="<%=beanMandato.getFechaUso()%>" />						
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.tipoPago"/></td>	
						<td colspan="3">
							<html:hidden name="MandatosCuentasBancariasForm" property="tipoPago" styleId="tipoPago" value="0" />
							<siga:Idioma key="censo.fichaCliente.datosBancarios.tipoPago.recurrente"/>
						
							<!-- Options de recurrente y unico
								<table cellpadding="0" cellpadding="0" border="0">
									<tr>				
										<td>
											<input name="tipoPago" TYPE="radio" VALUE="0" disabled <%if(beanMandato.getTipoPago().equals("0")) {%> checked <%}%>>
											<siga:Idioma key="censo.fichaCliente.datosBancarios.tipoPago.recurrente"/>
										</td>
										<td>
											<input name="tipoPago" TYPE="radio" VALUE="1" disabled <%if(beanMandato.getTipoPago().equals("1")) {%> checked <%}%>>
											<siga:Idioma key="censo.fichaCliente.datosBancarios.tipoPago.unico"/>
										</td>								
									</tr>
								</table>
							-->
						</td> 											
					</tr>
					
					<tr>
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.esquema"/>&nbsp;(*)</td>
						<td colspan="3">
							<table cellpadding="0" border="0">
								<tr>		
									<td>
										<input id="esquema" name="esquema" TYPE="radio" VALUE="0" <%if(beanMandato.getEsquema().equals("0")) {%> checked <%}%> <%if(modoConsulta) {%> disabled <%}%> onchange="controlarAutorizacion(this.value);">
										<siga:Idioma key="censo.fichaCliente.datosBancarios.esquema.core"/>
									</td>	
								</tr>
		
								<tr>
									<td>							
										<input id="esquema" name="esquema" TYPE="radio" VALUE="1" <%if(beanMandato.getEsquema().equals("1")) {%> checked <%}%> <%if(modoConsulta) {%> disabled <%}%> onchange="controlarAutorizacion(this.value);"> 
										<siga:Idioma key="censo.fichaCliente.datosBancarios.esquema.cor1"/>
									</td>
								</tr>
									
								<tr>
									<td>							
										<input id="esquema" name="esquema" TYPE="radio" VALUE="2" <%if(beanMandato.getEsquema().equals("2")) {%> checked <%}%> <%if(modoConsulta) {%> disabled <%}%> onchange="controlarAutorizacion(this.value);">
										<siga:Idioma key="censo.fichaCliente.datosBancarios.esquema.b2b"/>
									</td>	
									
									<td>									
										&nbsp;&nbsp;
										<input id="autorizacionB2BCheck" name="autorizacionB2BCheck" TYPE="checkbox" style="vertical-align:top" <%if(beanMandato.getAutorizacionB2B().equals("1")) {%> checked <%}%> <%if(modoConsulta) {%> disabled <%}%>>
										<siga:Idioma key="censo.fichaCliente.datosBancarios.autorizacion.b2b"/> 			
										<siga:ToolTip id='ayudaB2B' imagen='/SIGA/html/imagenes/botonAyuda.gif' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usuario,"censo.fichaCliente.datosBancarios.autorizacion.b2b.ayuda"))%>' />						
									</td>
								</tr>
							</table>
						</td> 	
					</tr>						
				</table>	
			</siga:ConjCampos>
			
			<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.acreedor">		
				<table cellpadding="0" border="0">
					<tr>
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.identificador"/></td>
						<td width="350px">
							<siga:Select queryId="getTiposIdentificacion" id="tipoIdentificacion" selectedIds="<%=arrayAcreedorTipoId%>" readOnly="true" width="100px" /> 
							<html:text name="MandatosCuentasBancariasForm" property="acreedorId" styleClass="boxConsulta" readonly="true" style='width:200px;' value="<%=beanMandato.getAcreedorId()%>" />
						</td>				
	
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.nombre"/></td>
						<td width="350px"><html:text name="MandatosCuentasBancariasForm" property="acreedorNombre" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getAcreedorNombre()%>" /></td>				
					</tr>							
				</table>								
			
				<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.direccion">
					<table cellpadding="0" border="0">
						<tr>
							<td class="labelText" width="110px" rowspan="2"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.direccion"/></td>
							<td width="350px" rowspan="2">
								<html:textarea name="MandatosCuentasBancariasForm" property="acreedorDomicilio"
									style="overflow-y:auto; overflow-x:hidden; width:340px; height:50px; resize:none;"
									value="<%=beanMandato.getAcreedorDomicilio()%>" styleClass="boxConsulta"
									readonly="true"></html:textarea>
							</td>
							
							<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.codigoPostal"/></td>
							<td width="350px"><html:text name="MandatosCuentasBancariasForm" property="acreedorCodigoPostal" styleClass="boxConsulta" readonly="true" style='width:50px;' value="<%=beanMandato.getAcreedorCodigoPostal()%>" /></td>				
						</tr>
						
						<tr>
							<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.pais"/></td>
							<td><html:text name="MandatosCuentasBancariasForm" property="acreedorPais" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getAcreedorPais()%>" /></td>																
						</tr>	
						
						<tr>						
							<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.provincia"/></td>
							<td><html:text name="MandatosCuentasBancariasForm" property="acreedorProvincia" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getAcreedorProvincia()%>" /></td>
							
							<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.poblacion"/></td>
							<td><html:text name="MandatosCuentasBancariasForm" property="acreedorPoblacion" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getAcreedorPoblacion()%>" /></td>				
						</tr>											
					</table>			
				</siga:ConjCampos>		
			</siga:ConjCampos>	
			
			<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.deudor">
				<table cellpadding="0" border="0">
					<tr>
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.identificador"/>&nbsp;(*)</td>
						<td width="350px">
							<siga:Select queryId="getTiposIdentificacion" id="deudorTipoId" selectedIds="<%=arrayDeudorTipoId%>" required="true" readOnly="<%=modoConsulta%>" width="100px" /> 		
							<html:text name="MandatosCuentasBancariasForm" property="deudorId" styleId="deudorId" maxlength="20" styleClass="<%=claseEdicion%>" readonly="<%=modoConsulta%>" style='width:200px;' value="<%=beanMandato.getDeudorId()%>" />
						</td>				
	
						<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.nombre"/></td>
						<td width="350px"><html:text name="MandatosCuentasBancariasForm" property="deudorNombre" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getDeudorNombre()%>" /></td>				
					</tr>							
				</table>								
			
				<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.direccion">
					<table cellpadding="0" border="0">
						<tr>
							<td class="labelText" width="110px" rowspan="2"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.direccion"/></td>
							<td width="350px" rowspan="2">
								<html:textarea name="MandatosCuentasBancariasForm" property="deudorDomicilio" styleId="deudorDomicilio"
									onkeydown="cuenta(this,100)" onchange="cuenta(this,100)"
									style="overflow-y:auto; overflow-x:hidden; width:340px; height:50px; resize:none;"
									value="<%=beanMandato.getDeudorDomicilio()%>" styleClass="boxConsulta"
									readonly="true"></html:textarea>
							</td>
							
							<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.codigoPostal"/></td>																	
							<td width="350px"><html:text name="MandatosCuentasBancariasForm" property="deudorCodigoPostal" styleClass="boxConsulta" readonly="true" style='width:50px;' value="<%=beanMandato.getDeudorCodigoPostal()%>" /></td>				
						</tr>
						
						<tr>
							<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.pais"/></td>
							<td><html:text name="MandatosCuentasBancariasForm" property="deudorPais" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getDeudorPais()%>" /></td>		 
						</tr>
						
						<tr>
							<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.provincia"/></td>								
							<td><html:text name="MandatosCuentasBancariasForm" property="deudorProvincia" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getDeudorProvincia()%>" /></td>					
							
							<td class="labelText"><siga:Idioma key="censo.fichaCliente.datosBancarios.direccion.poblacion"/></td>
							<td><html:text name="MandatosCuentasBancariasForm" property="deudorPoblacion" styleClass="boxConsulta" readonly="true" style='width:340px;' value="<%=beanMandato.getDeudorPoblacion()%>" /></td> 											
						</tr>										
					</table>	
				</siga:ConjCampos>	
				
				<siga:ConjCampos leyenda="censo.fichaCliente.datosBancarios.cuentaBancaria">
					<table cellpadding="0" border="0">
						<tr>						
							<td class="labelText" width="110px">
								<siga:Idioma key="censo.fichaCliente.datosBancarios.cuentaBancaria.iban"/>
							</td>						
							<td>
								<html:text name="MandatosCuentasBancariasForm" property="iban" styleClass="boxConsulta" readonly="true" style='width:250px;' value="<%=iban%>" />
							</td>
											
							<td class="labelText">
								<siga:Idioma key="censo.fichaCliente.datosBancarios.cuentaBancaria.bic"/>
							</td>
							<td>
								<html:text name="MandatosCuentasBancariasForm" property="bic" styleClass="boxConsulta" readonly="true" style='width:500px;' value="<%=beanMandato.getBic()%>" />
							</td>					
						</tr>		
						
						<tr>
							<td class="labelText" width="110px"><siga:Idioma key="censo.fichaCliente.datosBancarios.cuentaBancaria.banco"/></td>
							<td colspan="3"><html:text name="MandatosCuentasBancariasForm" property="banco" styleClass="boxConsulta" readonly="true" style='width:850px;' value="<%=beanMandato.getBanco()%>" /></td>				
						</tr>	
					</table>			
				</siga:ConjCampos>				
			</siga:ConjCampos>
		</div>					
	</html:form>			
	
	<siga:ConjBotonesAccion botones='<%=botones%>'/>
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>