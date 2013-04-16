<!-- consultaTramosRetencion.jsp -->

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
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:TituloExt titulo="menu.fcs.tramosLEC"	localizacion="fcs.tramosLEC.gestion.localizacion" />

<script type="text/javascript">
//aalg: INC_10652_SIGA. Se añaden validaciones de números en los campos configurables
	function postAccionAnio(){
		if(TramosRetencionForm.smi.value=='0')
			alert('<siga:Idioma key="fcs.tramosLEC.mensaje.error.AñoSinSmi"/>');
		document.getElementById('idImporte').onchange();
		document.getElementById('idBuscarTramosRetencion').onclick();
		if (TramosRetencionForm.smi.value=='')
			TramosRetencionForm.anio.value = "";
		
	}
	function onChangeNumeroMeses(){
		if (isNumero(TramosRetencionForm.numeroMeses.value)){
			if(TramosRetencionForm.numeroMeses.value==0||TramosRetencionForm.numeroMeses.value>12){
				// TramosRetencionForm.numeroMeses.value = '1';
				alert('<siga:Idioma key="fcs.tramosLEC.mensaje.aviso.numMesesErroneo"/>');
				return;
			}
			document.getElementById('idImporte').onchange();
			document.getElementById('idBuscarTramosRetencion').onclick();
		}
		else{
			alert('<siga:Idioma key="fcs.tramosLEC.mensaje.aviso.numMesesErroneo"/>');
			TramosRetencionForm.numeroMeses.value = "1";
			onChangeNumeroMeses();
		}
	}
	function onChangeSmi(){
		TramosRetencionForm.smi.value=TramosRetencionForm.smi.value.replace(".",",");
		if (validaFloat(TramosRetencionForm.smi.value)){
			document.getElementById('idImporte').onchange();
			document.getElementById('idBuscarTramosRetencion').onclick();
		}
		else{
			TramosRetencionForm.smi.value="";
			TramosRetencionForm.importeRetencion.value="";
			document.getElementById('idBuscarTramosRetencion').onclick();
		}	
	}
	function onChangeImporte(){
		TramosRetencionForm.importe.value=TramosRetencionForm.importe.value.replace(".",",");
		if (validaFloat(TramosRetencionForm.importe.value)){
			document.getElementById('idImporte').onchange();
			document.getElementById('idBuscarTramosRetencion').onclick();
		}
	}
	function preAccionImporte(){
		TramosRetencionForm.importe.value=TramosRetencionForm.importe.value.replace(/,/,".");
		if(TramosRetencionForm.importe.value==''){
			return 'cancel';
		}
		
		
	}
	
	function preAccionBuscarTramosRetencion(){
		
	}
	function postAccionImporte(){
		if (TramosRetencionForm.importeRetencion.value=="")
			TramosRetencionForm.importe.value="";
	}
	function postAccionBuscarTramosRetencion(){
		
	}
	function onload(){
		document.getElementById('idBuscarTramosRetencion').onclick();
	}
	
</script>

</head>

<body onload="onload();" >

<!-- INICIO: CAMPOS DE BUSQUEDA-->
<html:form action="/FCS_GestionTramosLEC" method="POST" target="mainWorkArea">
<html:hidden property="modo"/>
<html:hidden property="idInstitucion"/>
<siga:ConjCampos leyenda="general.criterios">
	<table width="100%" class="tablaCampos" border="0" >
		<tr>
			<td width="20%"></td>
			<td width="10%"></td>
			<td width="25%"></td>
			<td></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="fcs.tramosLEC.literal.año" /></td>
			<td><html:text styleId="idAnio" property="anio" size="4" maxlength="4" styleClass="box" style="width:50"></html:text></td>
			<td class="labelText"><siga:Idioma key="fcs.tramosLEC.literal.smi" /></td>
			<td><html:text	property="smi" styleClass="box" onchange="onChangeSmi();"></html:text></td>
		</tr>
	</table>
</siga:ConjCampos>
<siga:ConjCampos leyenda="fcs.tramosLEC.leyenda.simulacionRetencion">
	<table width="100%" class="tablaCampos" border="0" >
		<tr>
			<td width="20%"></td>
			<td width="10%"></td>
			<td width="10%"></td>
			<td width="10%"></td>
			<td width="25%"></td>
			<td></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="fcs.tramosLEC.literal.numeroMeses" /></td>
			<td><html:text property="numeroMeses" styleClass="box" maxlength="2" style="width:30" onchange="onChangeNumeroMeses();"></html:text></td>
			<td class="labelText"><siga:Idioma key="fcs.tramosLEC.literal.importe" /></td>
			<td><html:text property="importe" styleId="idImporte" styleClass="box" style="width:70;text-align=right" value="" /></td>
			<td>&nbsp;</td>
			<td class="labelText"><siga:Idioma key="fcs.tramosLEC.literal.importeRetencion" /></td>
			<td><html:text property="importeRetencion" styleClass="boxConsulta" readonly="true" style="width:70;text-align=right" value="" /></td>
		</tr>
	</table>
</siga:ConjCampos>
<input type='button'  id = 'idBuscarTramosRetencion' name='idButton' style="display:none" value='Buscar' alt='Buscar' class='busquedaTramosRetencion'>

<ajax:updateFieldFromField 
	baseUrl="/SIGA/FCS_GestionTramosLEC.do?modo=getAjaxSmi"
    source="anio" target="smi"
	parameters="anio={anio}" 
	postFunction="postAccionAnio"
	
/>
<ajax:updateFieldFromField 
	baseUrl="/SIGA/FCS_GestionTramosLEC.do?modo=getAjaxImporteRetencion"
    source="importe" target="importeRetencion"
	parameters="importe={importe},anio={anio},numeroMeses={numeroMeses}"
	preFunction ="preAccionImporte" 
	postFunction="postAccionImporte"
/>

<div id="divTramosRetencion" style='height:480px;position:absolute;width:100%; overflow-y:auto'>
	<table id='tramosRetencion' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
	</table>
</div>	
</html:form>

<ajax:htmlContent
	baseUrl="/SIGA/FCS_GestionTramosLEC.do?modo=getAjaxBusquedaTramosRetencion"
	source="idBuscarTramosRetencion"
	target="divTramosRetencion"
	preFunction="preAccionBuscarTramosRetencion"
	postFunction="postAccionBuscarTramosRetencion"
	parameters="anio={anio},numeroMeses={numeroMeses},idInstitucion={idInstitucion},smi={smi}"/>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
</body>
</html>