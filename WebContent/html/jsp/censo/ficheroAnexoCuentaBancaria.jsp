<!DOCTYPE html>
<html>
<head>
<!-- ficheroAnexoCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean" %>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.CenAnexosCuentasBancariasBean"%>

<!-- JSP -->
<% 
	String modoFichero = (String) request.getAttribute("modoFichero");
		
	String sClaseConsulta = "box";
	boolean bConsulta = false;
	if (modoFichero.equals("verFicheroAnexoCuentaBancaria")) {
		sClaseConsulta = "boxConsulta";
		bConsulta = true;
	}
	
	CenAnexosCuentasBancariasBean beanAnexo = (CenAnexosCuentasBancariasBean) request.getAttribute("beanAnexo");
	
	String sConsultaFirma = "false";
	boolean bConsultaFirma = false;
	String sClaseConsultaFirma = "box";
	if (bConsulta || (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals(""))) {
		sConsultaFirma = "true";
		bConsultaFirma = true;
		sClaseConsultaFirma = "boxConsulta";
	}
	
	// JPT: Gestion de Volver y botones
	String botones = "C";	
	if (!bConsulta) {
		botones += ",Y,R";
	}		
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>
	
	<siga:TituloExt titulo="pestana.fichaCliente.datosBancarios.ficherosAnexos" localizacion="censo.fichaCliente.bancos.mandatos.ficheros.localizacion" />
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script>			
		jQuery.noConflict();
		
		jQuery(function(){
			jQuery("#firmado").on("change", function(){
				controlFirma();
			});					
			
			if (!<%=bConsultaFirma%>) {
				controlFirma();
			}else{
				if (jQuery("#firmado")[0].checked) {
					jQuery("#divFicheros").css("display", "block");
				}else{
					jQuery("#divFicheros").css("display", "none");	
				}
			}
		});			
		
		
		function controlFirma() {
			if (jQuery("#firmado")[0].checked) {
			jQuery("#divFicheros").css("display", "block");
				jQuery("#firmaFecha").removeAttr("disabled");	
				jQuery("#firmaFecha-datepicker-trigger").show();
				jQuery("#firmaFechaHora").removeAttr("disabled");
				jQuery("#firmaFechaMinutos").removeAttr("disabled");
				jQuery("#firmaLugar").removeAttr("disabled");
				jQuery("#origen").removeAttr("disabled");
				jQuery("#descripcion").removeAttr("disabled");
				
			} else {
				jQuery("#divFicheros").css("display", "none");
				jQuery("#firmaFecha").attr("disabled","disabled");				
				jQuery("#firmaFecha-datepicker-trigger").hide();	
				jQuery("#firmaFechaHora").attr("disabled","disabled");
				jQuery("#firmaFechaMinutos").attr("disabled","disabled");
				jQuery("#firmaLugar").attr("disabled","disabled");
				jQuery("#origen").attr("disabled","disabled");
				jQuery("#descripcion").attr("disabled","disabled");							
			}
		}
		
		
		
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.AnexosCuentasBancariasForm.target = "_self";
				document.AnexosCuentasBancariasForm.modo.value="<%=modoFichero%>";		
				document.AnexosCuentasBancariasForm.submit();
			}						
		}			
	
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar(){		
			sub();
			
			// Realizo las validaciones de la pagina
			var errores = "";			
			
			if (jQuery("#firmado")[0].checked) {
				if (jQuery("#firmaFecha").val()=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.fechaFirmada'/>"+ '\n';				
				}
				
				if (trim(jQuery("#firmaFechaHora").val())=="" || trim(jQuery("#firmaFechaMinutos").val())=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.horaFirmada'/>"+ '\n';	
					
				} else {
					if (isNaN(jQuery("#firmaFechaHora").val()) || isNaN(jQuery("#firmaFechaMinutos").val())) {
						errores += "<siga:Idioma key='errors.invalid' arg0='censo.fichaCliente.bancos.mandatos.anexos.horaFirmada'/>"+ '\n';
						
					} else {
						var hora = trim(jQuery("#firmaFechaHora").val());
						var minutos = trim(jQuery("#firmaFechaMinutos").val());
						
						if (hora.length==1) {
							jQuery("#firmaFechaHora").val("0" + hora);
						}
						
						if (minutos.length==1) {
							jQuery("#firmaFechaMinutos").val("0" + minutos);
						}
						
						if (hora<0 || hora>23 || minutos<0 || minutos>59) {
							errores += "<siga:Idioma key='errors.invalid' arg0='censo.fichaCliente.bancos.mandatos.anexos.horaFirmada'/>"+ '\n';	
						}
					}
				}
				
				if (trim(jQuery("#firmaLugar").val())=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.lugarFirma'/>"+ '\n';				
				}
				
				if (trim(jQuery("#origen").val())=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.origenFirma'/>"+ '\n';				
				}

				if (trim(jQuery("#descripcion").val())=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.descripcionFirma'/>"+ '\n';				
				}	
				if(document.forms['AnexosCuentasBancariasForm'].theFile && document.forms['AnexosCuentasBancariasForm'].theFile.value && !TestFileType(document.forms['AnexosCuentasBancariasForm'].theFile.value, ['DOC','DOCX','PDF'])){
					fin();
					return false;
				}	
			} else {
				errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.firmado'/>"+ '\n';	
			} 
			
			if (errores != "") {
				alert(errores);
				fin();
				return false;
			}
			
			if (<%=modoFichero.equals("editarFicheroAnexoCuentaBancaria")%>) {
				document.AnexosCuentasBancariasForm.modo.value = "modificarFirmaAnexoCuentaBancaria";
			} else {
				document.AnexosCuentasBancariasForm.modo.value = "insertarFirmaAnexoCuentaBancaria";
			}
			document.AnexosCuentasBancariasForm.submit();
		}			
		
		// Asociada al boton Cerrar
		function accionCerrar() {		
			window.top.close();
		}			
		function eliminarFichero()
		{
			if(confirm('<siga:Idioma key="administracion.informes.mensaje.aviso.archivo.eliminar"/>')){
				document.forms['AnexosCuentasBancariasForm'].modo.value = "borrarFichero";	
				document.forms['AnexosCuentasBancariasForm'].submit();
			}
		}
		function downloadFichero()
		{
			document.forms['AnexosCuentasBancariasForm'].modo.value = "downloadFichero";
			document.forms['AnexosCuentasBancariasForm'].submit();
		}
		
	</script>
</head>

<body>
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.firmaAnexo"/>
				</td>
			</tr>
		</table>

	<html:form action="/CEN_AnexosCuentasBancarias.do" method="POST" enctype="multipart/form-data" target="submitArea">
		<html:hidden name="AnexosCuentasBancariasForm" property="modo" styleId="modo" value="" />		
		<html:hidden name="AnexosCuentasBancariasForm" property="idInstitucion" styleId="idInstitucion" value="<%=beanAnexo.getIdInstitucion()%>" />
		<html:hidden name="AnexosCuentasBancariasForm" property="idPersona" styleId="idPersona" value="<%=beanAnexo.getIdPersona()%>" />
		<html:hidden name="AnexosCuentasBancariasForm" property="idCuenta" styleId="idCuenta" value="<%=beanAnexo.getIdCuenta()%>" />
		<html:hidden name="AnexosCuentasBancariasForm" property="idMandato" styleId="idMandato" value="<%=beanAnexo.getIdMandato()%>" />
		<html:hidden name="AnexosCuentasBancariasForm" property="idAnexo" styleId="idAnexo" value="<%=beanAnexo.getIdAnexo()%>" />
		
		<table cellpadding="5" border="0">
			<tr>
				<td>
					<input type="checkbox" id="firmado" name="firmado" 
						<%if (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals("")) {%> checked <%}%> 
						<%if (bConsultaFirma) {%> disabled <%}%>>
				</td>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.firmado"/>&nbsp;(*)</td>
			</tr>
			
			<tr>
				<td rowspan="5">&nbsp;</td>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.fechaFirmada"/>&nbsp;(*)</td>
				<td style="padding:0px">
					<table cellpadding="5" border="0">
						<tr>
							<td>				
								<siga:Fecha nombreCampo="firmaFecha" styleId="firmaFecha" 
									valorInicial="<%=beanAnexo.getFirmaFecha()%>" 
									disabled="<%=sConsultaFirma%>" />
							</td>
							
							<td class="labelText" width="50px"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.horaFirmada"/>&nbsp;(*)</td>
							<td>
								<html:text name="AnexosCuentasBancariasForm" property="firmaFechaHora" styleId="firmaFechaHora" 
									onkeypress="return soloDigitos(event);" value="<%=beanAnexo.getFirmaFechaHora()%>" 
									style='width:20px;' maxlength="2" 
									styleClass="<%=sClaseConsultaFirma%>" readonly="<%=bConsultaFirma%>" />					
								:
								<html:text name="AnexosCuentasBancariasForm" property="firmaFechaMinutos" styleId="firmaFechaMinutos" 
									onkeypress="return soloDigitos(event);" value="<%=beanAnexo.getFirmaFechaMinutos()%>" 
									style='width:20px;' maxlength="2" 
									styleClass="<%=sClaseConsultaFirma%>" readonly="<%=bConsultaFirma%>" />	
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.lugarFirma"/>&nbsp;(*)</td>
				<td>
					<html:text name="AnexosCuentasBancariasForm" property="firmaLugar" styleId="firmaLugar" 
						value="<%=beanAnexo.getFirmaLugar()%>" style='width:500px;' maxlength="100" 
						styleClass="<%=sClaseConsultaFirma%>" readonly="<%=bConsultaFirma%>" />
				</td>				
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.origenFirma"/>&nbsp;(*)</td>
				<td>
					<html:textarea name="AnexosCuentasBancariasForm" property="origen" styleId="origen"
						onkeydown="cuenta(this,1000)" onchange="cuenta(this,1000)"
						style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;"
						value="<%=beanAnexo.getOrigen()%>" styleClass="<%=sClaseConsultaFirma%>"
						readonly="<%=bConsultaFirma%>"></html:textarea>
				</td>				
			</tr>			
			
			<tr>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.descripcionFirma"/>&nbsp;(*)</td>
				<td>
					<html:textarea name="AnexosCuentasBancariasForm" property="descripcion" styleId="descripcion"
						onkeydown="cuenta(this,1000)" onchange="cuenta(this,1000)"
						style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;"
						value="<%=beanAnexo.getDescripcion()%>" styleClass="<%=sClaseConsultaFirma%>"
						readonly="<%=bConsultaFirma%>"></html:textarea>
				</td>				
			</tr>				
			
			<tr>
				
				<td colspan="2">
					
					<%@ include file="/html/jsp/general/ficheros.jsp"%>												
				</td>				
			</tr>		
		</table>			
	</html:form>		
	
	<siga:ConjBotonesAccion botones='<%=botones%>'/>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>