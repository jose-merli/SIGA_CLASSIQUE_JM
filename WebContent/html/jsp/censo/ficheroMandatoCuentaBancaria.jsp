<!DOCTYPE html>
<html>
<head>
<!-- ficheroMandatoCuentaBancaria.jsp -->

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
<%@ page import="com.siga.beans.CenMandatosCuentasBancariasBean"%>

<!-- JSP -->
<% 
	String modoFichero = (String) request.getAttribute("modoFichero");
		
	String sClaseConsulta = "box";
	boolean bConsulta = false;
	if (modoFichero.equals("verFicheroMandatoCuentaBancaria")) {
		sClaseConsulta = "boxConsulta";
		bConsulta = true;
	}
	
	CenMandatosCuentasBancariasBean beanMandato = (CenMandatosCuentasBancariasBean) request.getAttribute("beanMandato");
	
	String sConsultaFirma = "false";
	boolean bConsultaFirma = false;
	String sClaseConsultaFirma = "box";
	if (bConsulta || (beanMandato.getFirmaFecha()!=null && !beanMandato.getFirmaFecha().equals(""))) {
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
				
			} else {
				controlFirmado();
			}
		});			
		
		function controlFirma() {
			if (jQuery("#firmado")[0].checked) {
				jQuery("#firmaFecha").removeAttr("disabled");	
				jQuery("#firmaFecha-datepicker-trigger").show();
				jQuery("#firmaLugar").removeAttr("disabled");
				
			} else {
				jQuery("#firmaFecha").val("");
				jQuery("#firmaFecha").attr("disabled","disabled");
				jQuery("#firmaFecha-datepicker-trigger").hide();
				jQuery("#firmaLugar").val("");
				jQuery("#firmaLugar").attr("disabled","disabled");			
			}
			
			controlFirmado();
		}
		
		function controlFirmado() {
			if (jQuery("#firmado")[0].checked) {				
				jQuery("#divFicheros").show();
				
			} else {
				jQuery("#divFicheros").hide();
			}
		}			
		
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.MandatosCuentasBancariasForm.target = "_self";
				document.MandatosCuentasBancariasForm.modo.value="<%=modoFichero%>";		
				document.MandatosCuentasBancariasForm.submit();
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
				
				if (trim(jQuery("#firmaLugar").val())=="") {
					errores += "<siga:Idioma key='errors.required' arg0='censo.fichaCliente.bancos.mandatos.anexos.lugarFirma'/>"+ '\n';				
				}
				
				if(document.forms['MandatosCuentasBancariasForm'].theFile && document.forms['MandatosCuentasBancariasForm'].theFile.value && !TestFileType(document.forms['MandatosCuentasBancariasForm'].theFile.value, ['DOC','DOCX','PDF'])){
					fin();
					return false;
				}				
			}
			
			if (errores != "") {
				alert(errores);
				fin();
				return false;
			}
			
			document.MandatosCuentasBancariasForm.modo.value="modificarFirmaMandatoCuentaBancaria";		
			document.MandatosCuentasBancariasForm.submit();
		}			
		
		// Asociada al boton Cerrar
		function accionCerrar() {		
			window.top.close();
		}		
		
		function eliminarFichero()
		{
			if(confirm('<siga:Idioma key="administracion.informes.mensaje.aviso.archivo.eliminar"/>')){
				document.forms['MandatosCuentasBancariasForm'].modo.value = "borrarFichero";	
				document.forms['MandatosCuentasBancariasForm'].submit();
			}
		}
		function downloadFichero()
		{
			document.forms['MandatosCuentasBancariasForm'].modo.value = "downloadFichero";
			document.forms['MandatosCuentasBancariasForm'].submit();
		}
		
	</script>
</head>

<body>
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.firmaMandato"/>
				</td>
			</tr>
		</table>

	<html:form action="/CEN_MandatosCuentasBancarias.do" enctype="multipart/form-data" method="POST" target="submitArea">
		<html:hidden name="MandatosCuentasBancariasForm" property="modo" styleId="modo" value="" />		
		<html:hidden name="MandatosCuentasBancariasForm" property="idInstitucion" styleId="idInstitucion" value="<%=beanMandato.getIdInstitucion()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idPersona" styleId="idPersona" value="<%=beanMandato.getIdPersona()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idCuenta" styleId="idCuenta" value="<%=beanMandato.getIdCuenta()%>" />
		<html:hidden name="MandatosCuentasBancariasForm" property="idMandato" styleId="idMandato" value="<%=beanMandato.getIdMandato()%>" />
		
		<table cellpadding="5" border="0">
			<tr>
				<td>
					<input type="checkbox" id="firmado" name="firmado" 
						<%if (beanMandato.getFirmaFecha()!=null && !beanMandato.getFirmaFecha().equals("")) {%> checked <%}%> 
						<%if (bConsultaFirma) {%> disabled <%}%>>
				</td>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.firmado"/>&nbsp;(*)</td>
			</tr>
			
			<tr>
				<td rowspan="3">&nbsp;</td>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.fechaFirmada"/>&nbsp;(*)</td>
				<td>
					<siga:Fecha nombreCampo="firmaFecha" styleId="firmaFecha" valorInicial="<%=beanMandato.getFirmaFecha()%>" disabled="<%=sConsultaFirma%>" />
				</td>
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.anexos.lugarFirma"/>&nbsp;(*)</td>
				<td>
					<html:text name="MandatosCuentasBancariasForm" property="firmaLugar" styleId="firmaLugar" 
						value="<%=beanMandato.getFirmaLugar()%>" style='width:500px;' maxlength="100" 
						styleClass="<%=sClaseConsultaFirma%>" readonly="<%=bConsultaFirma%>" />
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