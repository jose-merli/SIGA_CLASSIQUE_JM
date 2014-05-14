<!--fechasFicheroBancario.jsp-->

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

<!-- JSP -->
<% 
	/** CR7 - Control de fechas de presentación y cargo en ficheros SEPA **/
	String fechaPresentacion = (String) request.getAttribute("fechaPresentacion");
	String fechaUnicaCargos = (String) request.getAttribute("fechaUnicaCargos");
	String fechaPrimerosRecibos = (String) request.getAttribute("fechaPrimerosRecibos");
	String fechaRecibosRecurrentes = (String) request.getAttribute("fechaRecibosRecurrentes");
	String fechaRecibosCOR1 = (String) request.getAttribute("fechaRecibosCOR1");
	String fechaRecibosB2B = (String) request.getAttribute("fechaRecibosB2B");
	String radio = (String) request.getAttribute("radio");
	String habilesUnicaCargos = (String) request.getAttribute("habilesUnicaCargos");
	String habilesPrimerosRecibos = (String) request.getAttribute("habilesPrimerosRecibos");
	String habilesRecibosRecurrentes = (String) request.getAttribute("habilesRecibosRecurrentes");
	String habilesRecibosCOR1 = (String) request.getAttribute("habilesRecibosCOR1");
	String habilesRecibosB2B = (String) request.getAttribute("habilesRecibosB2B");	
%>

<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="labelText" width="210px"><siga:Idioma key="facturacion.fechasficherobancario.fechapresentacion"/>&nbsp;(*)</td>
		<td width="120px"><siga:Fecha nombreCampo="fechaPresentacion"	posicionX="10" posicionY="10" valorInicial="<%=fechaPresentacion%>"/></td>
		<td><siga:ToolTip id='ayudaFechaPresentacion' imagen='/SIGA/html/imagenes/botonAyuda.gif' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "facturacion.fechasficherobancario.ayudaFechaPresentacion"))%>'/></td>
	</tr>
</table>	
	
<siga:ConjCampos leyenda="facturacion.fechasficherobancario.fechacargo">
	<table class="tablaCampos" border="0">
		<tr>
			<td class="labelText" colspan="5">
				<input type="radio" id="radiofechaCargo" name='radioAccion' value="0" onclick="accionRadio()"/>
				<label for="radiofechaCargo"><siga:Idioma key="facturacion.fechasficherobancario.unica"/></label>
			</td>
		</tr>
		
		<tr>
			<td width="30px">&nbsp;</td>

			<td class="labelText" width="250px" id="fechaCargoUnicaSinAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechascargounica"/></td>
			<td class="labelText" width="250px" id="fechaCargoUnicaConAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechascargounica"/>&nbsp;(*)</td>
			
			<td><siga:Fecha nombreCampo="fechaCargoUnica" posicionX="10" posicionY="10" valorInicial="<%=fechaUnicaCargos%>"/></td>						
<%
			if (habilesUnicaCargos!=null && !habilesUnicaCargos.equals("")) {
%>						
				<td>(<siga:Idioma key='facturacion.fechasficherobancario.fechaMinimoDiasHabiles' arg0='<%=habilesUnicaCargos%>'/>)</td>
<%
			}
%>							
		</tr>
		
		<tr>
			<td class="labelText" colspan="5">
				<input type="radio" id="radioMinimas" name='radioAccion' value="1" onclick="accionRadio()" />
				<label for="radioMinimas"><siga:Idioma key="facturacion.fechasficherobancario.minimas"/></label>
			</td>
		</tr>

		<tr>
			<td width="30px" rowspan="4">&nbsp;</td>
			
			<td class="labelText" width="250px" id="fechaRecibosPrimerosSinAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechaprimerosrecibos"/></td>
			<td class="labelText" width="250px" id="fechaRecibosPrimerosConAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechaprimerosrecibos"/>&nbsp;(*)</td>
			
			<td><siga:Fecha nombreCampo="fechaRecibosPrimeros" posicionX="10" posicionY="10" valorInicial="<%=fechaPrimerosRecibos%>"/></td>
<%
			if (habilesPrimerosRecibos!=null && !habilesPrimerosRecibos.equals("")) {
%>														
				<td>(<siga:Idioma key='facturacion.fechasficherobancario.fechaMinimoDiasHabiles' arg0='<%=habilesPrimerosRecibos%>'/>)</td>
<%
			}
%>								
		</tr>
		
		<tr>
			<td class="labelText" width="250px" id="fechaRecibosRecurrentesSinAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fecharecibosrecurrentes"/></td>
			<td class="labelText" width="250px" id="fechaRecibosRecurrentesConAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fecharecibosrecurrentes"/>&nbsp;(*)</td>

			<td><siga:Fecha nombreCampo="fechaRecibosRecurrentes" posicionX="10" posicionY="10" valorInicial="<%=fechaRecibosRecurrentes%>"/></td>
<%
			if (habilesRecibosRecurrentes!=null && !habilesRecibosRecurrentes.equals("")) {
%>						
				<td>(<siga:Idioma key='facturacion.fechasficherobancario.fechaMinimoDiasHabiles' arg0='<%=habilesRecibosRecurrentes%>'/>)</td>
<%
			}
%>								
		</tr>
		
		<tr>
			<td class="labelText" width="250px" id="fechaRecibosCOR1SinAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechareciboscor1"/></td>
			<td class="labelText" width="250px" id="fechaRecibosCOR1ConAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fechareciboscor1"/>&nbsp;(*)</td>

			<td><siga:Fecha nombreCampo="fechaRecibosCOR1" posicionX="10" posicionY="10" valorInicial="<%=fechaRecibosCOR1%>"/></td>
<%
			if (habilesRecibosCOR1!=null && !habilesRecibosCOR1.equals("")) {
%>							
				<td>(<siga:Idioma key='facturacion.fechasficherobancario.fechaMinimoDiasHabiles' arg0='<%=habilesRecibosCOR1%>'/>)</td>
<%
			}
%>								
		</tr>

		<tr>
			<td class="labelText" width="250px" id="fechaRecibosB2BSinAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fecharecibosb2b"/></td>
			<td class="labelText" width="250px" id="fechaRecibosB2BConAsterisco"><siga:Idioma key="facturacion.fechasficherobancario.fecharecibosb2b"/>&nbsp;(*)</td>

			<td><siga:Fecha nombreCampo="fechaRecibosB2B" posicionX="10" posicionY="10" valorInicial="<%=fechaRecibosB2B%>"/></td>
<%
			if (habilesRecibosB2B!=null && !habilesRecibosB2B.equals("")) {
%>								
				<td>(<siga:Idioma key='facturacion.fechasficherobancario.fechaMinimoDiasHabiles' arg0='<%=habilesRecibosB2B%>'/>)</td>
<%
			}
%>								
		</tr>
	</table>
</siga:ConjCampos>

<script type="text/javascript">

	jQuery(document).ready(function () {		
<%
		if(radio!= null && radio.equals("0")) { 
%>
			jQuery('#radioMinimas').attr("checked","checked");	
<%
		} else {
%>
			jQuery('#radiofechaCargo').attr("checked","checked");			
<%
		}
%>
		
<%
		if(modoAction!= null && modoAction.trim().equals("ver")) { 
%>
			deshabilitar()
<%
		} else {
%>
			accionRadio();			
<%
		}
%>		
	});	

	function accionRadio(){		
		if (jQuery("input[name='radioAccion']:checked").val() == "0") {
			jQuery('#fechaRecibosPrimeros').addClass("boxConsulta").removeClass("box");
			jQuery('#fechaRecibosRecurrentes').addClass("boxConsulta").removeClass("box");
			jQuery('#fechaRecibosCOR1').addClass("boxConsulta").removeClass("box");
			jQuery('#fechaRecibosB2B').addClass("boxConsulta").removeClass("box");
			jQuery("#fechaRecibosPrimeros-datepicker-trigger").hide();	
			jQuery("#fechaRecibosRecurrentes-datepicker-trigger").hide();
			jQuery("#fechaRecibosCOR1-datepicker-trigger").hide();
			jQuery("#fechaRecibosB2B-datepicker-trigger").hide();
			jQuery("#fechaCargoUnica-datepicker-trigger").show();
			jQuery('#fechaCargoUnica').addClass("box").removeClass("boxConsulta");				
			jQuery('#fechaCargoUnicaConAsterisco').show();
			jQuery('#fechaCargoUnicaSinAsterisco').hide();	
			jQuery('#fechaRecibosPrimerosConAsterisco').hide();
			jQuery('#fechaRecibosPrimerosSinAsterisco').show();	
			jQuery('#fechaRecibosRecurrentesConAsterisco').hide();
			jQuery('#fechaRecibosRecurrentesSinAsterisco').show();	
			jQuery('#fechaRecibosCOR1ConAsterisco').hide();
			jQuery('#fechaRecibosCOR1SinAsterisco').show();	
			jQuery('#fechaRecibosB2BConAsterisco').hide();
			jQuery('#fechaRecibosB2BSinAsterisco').show();				
			
		} else {
			jQuery('#fechaCargoUnica').addClass("boxConsulta").removeClass("box");	
			jQuery('#fechaRecibosPrimeros').addClass("box").removeClass("boxConsulta");	
			jQuery('#fechaRecibosRecurrentes').addClass("box").removeClass("boxConsulta");	
			jQuery('#fechaRecibosCOR1').addClass("box").removeClass("boxConsulta");	
			jQuery('#fechaRecibosB2B').addClass("box").removeClass("boxConsulta");	
			jQuery("#fechaCargoUnica-datepicker-trigger").hide();
			jQuery("#fechaRecibosPrimeros-datepicker-trigger").show();
			jQuery("#fechaRecibosRecurrentes-datepicker-trigger").show();
			jQuery("#fechaRecibosCOR1-datepicker-trigger").show();
			jQuery("#fechaRecibosB2B-datepicker-trigger").show();	
			
			jQuery('#fechaCargoUnicaConAsterisco').hide();
			jQuery('#fechaCargoUnicaSinAsterisco').show();	
			jQuery('#fechaRecibosPrimerosConAsterisco').show();
			jQuery('#fechaRecibosPrimerosSinAsterisco').hide();
			jQuery('#fechaRecibosRecurrentesConAsterisco').show();
			jQuery('#fechaRecibosRecurrentesSinAsterisco').hide();
			jQuery('#fechaRecibosCOR1ConAsterisco').show();
			jQuery('#fechaRecibosCOR1SinAsterisco').hide();
			jQuery('#fechaRecibosB2BConAsterisco').show();
			jQuery('#fechaRecibosB2BSinAsterisco').hide();	
		}
	}
	
	function deshabilitar(){
		//input fecha
		jQuery('#fechaPresentacion').addClass("boxConsulta").removeClass("box");	
		jQuery('#fechaCargoUnica').addClass("boxConsulta").removeClass("box");	
		jQuery('#fechaRecibosPrimeros').addClass("boxConsulta").removeClass("box");
		jQuery('#fechaRecibosRecurrentes').addClass("boxConsulta").removeClass("box");
		jQuery('#fechaRecibosCOR1').addClass("boxConsulta").removeClass("box");
		jQuery('#fechaRecibosB2B').addClass("boxConsulta").removeClass("box");
		
		//icono fecha
		jQuery("#fechaPresentacion-datepicker-trigger").hide();
		jQuery("#fechaCargoUnica-datepicker-trigger").hide();
		jQuery("#fechaRecibosPrimeros-datepicker-trigger").hide();	
		jQuery("#fechaRecibosRecurrentes-datepicker-trigger").hide();
		jQuery("#fechaRecibosCOR1-datepicker-trigger").hide();
		jQuery("#fechaRecibosB2B-datepicker-trigger").hide();
		
		//Asteriscos (todos fuera en modo ver)
		jQuery('#fechaCargoUnicaConAsterisco').hide();
		jQuery('#fechaCargoUnicaSinAsterisco').show();	
		jQuery('#fechaRecibosPrimerosConAsterisco').hide();
		jQuery('#fechaRecibosPrimerosSinAsterisco').show();	
		jQuery('#fechaRecibosRecurrentesConAsterisco').hide();
		jQuery('#fechaRecibosRecurrentesSinAsterisco').show();	
		jQuery('#fechaRecibosCOR1ConAsterisco').hide();
		jQuery('#fechaRecibosCOR1SinAsterisco').show();	
		jQuery('#fechaRecibosB2BConAsterisco').hide();
		jQuery('#fechaRecibosB2BSinAsterisco').show();
		
		//radiobuttons
		jQuery("input[name='radioAccion']").attr("disabled","disabled");	
	}
	
	function validarFechasSEPA(){		
		// La fecha de presentacion es obligatoria
		if(jQuery('#fechaPresentacion').val() == ""){
			alert('<siga:Idioma key="facturacion.fechasficherobancario.msgerror.fechapresentacion"/>');
			return false;
		}
		
		if (jQuery("input[name='radioAccion']:checked").val() == "0") { //Checkeado Unica
			if(jQuery('#fechaCargoUnica').val() == ""){
				alert('<siga:Idioma key="facturacion.fechasficherobancario.msgerror.fechacargounica"/>');
				return false;
			}			
			
		} else{ //Checkeado Minimas
			
			if(jQuery('#fechaRecibosRecurrentes').val() == "" || jQuery('#fechaRecibosPrimeros').val() == "" || jQuery('#fechaRecibosCOR1').val() == "" || jQuery('#fechaRecibosB2B').val() == ""){
				alert('<siga:Idioma key="facturacion.fechasficherobancario.msgerror.fechasminimas"/>');
				return false;
			}
		}	
		
		return true;
	}
</script>