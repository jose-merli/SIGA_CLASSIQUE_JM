<!DOCTYPE html>
<html>
<head>
<!-- cargaProductos.jsp -->
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


	<!-- HEAD -->
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
  	
	<siga:Titulo titulo="pys.cargaProductos.titulo"	localizacion="pys.cargaProductos.localizacion"/>		

<script type="text/javascript">
	jQuery.noConflict();
	
	function refrescarLocal(){
		buscarCargasMasivas();
	}
	
	
	function buscar() {
		return buscarCargasMasivas();
	}
	
	
	function buscarCargasMasivas() {
		sub();
		var idInstitucion = document.CargaProductosForm.idInstitucion.value;
		var fechaCarga = document.CargaProductosForm.fechaCarga.value;
		
		var data = "idInstitucion="+idInstitucion;
		if(fechaCarga!='')
			data += "&fechaCarga="+fechaCarga;
		
	    
	   	jQuery.ajax({
        	type: "POST",
            url: "/SIGA/PYS_CargaProductos.do?modo=getAjaxBusqueda",
            data: data,
            success: function(response){
                fin();
            	jQuery('#divListado').html(response);
                
            },
            error: function(e){
            	fin();
                alert('Error: ' + e);
            }
        });
    }
	function inicio() {
		if(document.forms['CargaProductosForm'].modo.value == 'vuelta' && document.forms['CargaProductosForm'].idInstitucion.value!=''){
			buscarCargasMasivas();
			document.forms['CargaProductosForm'].modo.value = '';
		}
	}
	
	function downloadExample() {
		document.forms['CargaProductosForm'].modo.value = 'downloadExample';
		document.forms['CargaProductosForm'].submit();
		
	}
		
	function parseExcelFile() {
		if(!document.forms['CargaProductosForm'].theFile || document.forms['CargaProductosForm'].theFile.value==''){
			error = "<siga:Idioma key='errors.required' arg0='administracion.confInterfaz.fichero'/>";
			alert(error);
			fin();
			return false;
		}
		
		if(document.forms['CargaProductosForm'].theFile && document.forms['CargaProductosForm'].theFile.value!='' && !TestFileType(document.forms['CargaProductosForm'].theFile.value, ['XLS'])){
			fin();
			return false;
		}
		sub();
		document.forms['CargaProductosForm'].nombreFichero.value = document.forms['CargaProductosForm'].theFile.value;
		document.forms['CargaProductosForm'].modo.value = 'parseExcelFile';
		document.forms['CargaProductosForm'].submit();
		
	}
	function download(fila) {
		var idFichero = document.getElementById("idFichero_"+fila).value;
		var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
		document.forms['CargaProductosForm'].idInstitucion.value = idInstitucion;
		document.forms['CargaProductosForm'].idFichero.value = idFichero;
		document.forms['CargaProductosForm'].modo.value = 'downloadExcelProcessed';
		document.forms['CargaProductosForm'].submit();
		
	}
	function descargaLog(fila) {
		var idFicheroLog = document.getElementById("idFicheroLog_"+fila).value;
		var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
		document.forms['CargaProductosForm'].idInstitucion.value = idInstitucion;
		document.forms['CargaProductosForm'].idFicheroLog.value = idFicheroLog;
		document.forms['CargaProductosForm'].modo.value = 'downloadExcelLog';
		document.forms['CargaProductosForm'].submit();
		
	}
	
	
</script>
</head>
<body onload="inicio();">
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" enctype="multipart/form-data" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="nombreFichero"/>
		<html:hidden property="idFichero"/>
		<html:hidden property="idFicheroLog"/>
		<siga:ConjCampos leyenda="censo.cargaProductos.cabecera">
		
			<table width="100%" border="0">
				<tr>
					<td width="15%"></td>
					<td width="15%"></td>
					<td width="15%"></td>
					<td width="55%"></td>
					
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma  key="cargaMasivaDatosCurriculares.fechaCarga.literal"/>
					</td>
					<td>
						<siga:Fecha nombreCampo="fechaCarga" valorInicial="${CargaProductosForm.fechaCarga}"/>
					</td>
					<td ></td>
					<td ></td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="administracion.confInterfaz.fichero" />&nbsp;
					</td>
					<td><html:file  property="theFile" styleClass="boxCombo"  style="width:400px;" />
					</td>
					<td >
						<input  type="button" alt="<siga:Idioma key="general.boton.procesaFichero" />"
							 onclick="return parseExcelFile();"
							class="button" name="idButton" value="<siga:Idioma key="general.boton.procesaFichero" />"></input>
					</td>
					<td >
						<input type="button" alt='<siga:Idioma key="general.boton.descargaFicheroModelo" />'  onClick="downloadExample()" class="button" name="idButton" value="<siga:Idioma key="general.boton.descargaFicheroModelo" />"/>
					</td>
				</tr>

				
			</table>
			</siga:ConjCampos>
		<siga:ConjBotonesBusqueda botones="B"  titulo="cargaMasivaProductos.busqueda"/>
		<div id="divListado"></div>	
	</html:form>

<html:form action="${path}"  name="FormularioCarga" type ="com.siga.productos.form.CargaProductosForm" target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion" value='${CargaProductosForm.idInstitucion}' />
</html:form>

<script>

</script>

<!-- FIN: BOTONES BUSQUEDA -->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
</body>
</html>