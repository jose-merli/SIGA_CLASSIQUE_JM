<!DOCTYPE html>
<html>
<head>
<!-- operarDocumentacionDesigna.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script src="<html:rewrite page="/html/js/SIGA.js?v=${sessionScope.VERSIONJS}"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>

</head>

<body onload="onLoad();">
	
<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
		<td class="titulitosDatos">	
			<siga:Idioma key="gratuita.insertarDocumentacion.literal.editarDocumentacion"/>
		</td>
	</tr>
</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
<div id="campos" class="posicionModalPeque" align="center">

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>

<html:javascript formName="DefinirDocumentacionDesignaForm" staticJavascript="false" /> 
<html:form action="${path}"  method="POST" enctype="multipart/form-data" target="submitArea">
	<html:hidden styleId="modo" property = "modo" />
	<html:hidden styleId="idInstitucion" property = "idInstitucion" />
	<html:hidden styleId="anio" property = "anio"/>
	<html:hidden styleId="idTurno" property = "idTurno"/>
	<html:hidden styleId="numero" property = "numero"/>
	<html:hidden styleId="idDocumentacion" property = "idDocumentacion"/>
	<html:hidden styleId="extensionArchivo" property = "extensionArchivo"/>
	<html:hidden styleId="fechaArchivo" property = "fechaArchivo"/>
	<html:hidden styleId="nombreArchivo" property = "nombreArchivo"/>
	<html:hidden styleId="descripcionArchivo" property = "descripcionArchivo"/>
	<html:hidden styleId="directorioArchivo" property = "directorioArchivo"/>
	
	<html:hidden styleId="idTipoDocumentoAnterior" property = "idTipoDocumentoAnterior" value ="${DefinirDocumentacionDesignaForm.idTipoDocumento}"/>
	
	<input type="hidden" id="modoEntrada" value="${DefinirDocumentacionDesignaForm.modo}"/> 

		
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="pestana.justiciagratuitaejg.documentacion">
		
	<table class="tablaCampos" align="center" border ="0">

	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.tipoDocumentacion'/>&nbsp;(*)
		</td>
		<td colspan="3">
			<c:choose>
				<c:when test="${DefinirDocumentacionDesignaForm.modo=='insertar'&& DefinirDocumentacionDesignaForm.idActuacion==''}">
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentoDes" required="true"  width="300"   />
				</c:when>
				<c:when test="${DefinirDocumentacionDesignaForm.modo=='ver'}">
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentoDes" selectedIds="${idTipoDocumentoSelected}" required="true" disabled="true"  width="300"   />
				</c:when>
				<c:otherwise>
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentoDes" selectedIds="${idTipoDocumentoSelected}" required="true"  width="300"   />
				</c:otherwise>
			</c:choose>
		
		</td>
	</tr>					

	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.asistencia.documentacion.asociado'/>&nbsp;(*)
		</td>
		<td colspan="3">
			<c:choose>
				
				<c:when test="${DefinirDocumentacionDesignaForm.modo=='insertar' && DefinirDocumentacionDesignaForm.idActuacion==''}">
					<siga:Select id="idActuacion" queryId="getActuacionesDesigna" required="true" width="300" firstLabel="Designacion"  />
				</c:when>
				<c:when test="${DefinirDocumentacionDesignaForm.modo=='ver'}">
					<siga:Select id="idActuacion" queryId="getActuacionesDesigna" params="${paramActuacionesJson}" selectedIds="${idActuacionSelected}" firstLabel="Designacion" required="true"  disabled="true"  width="300"   />
				</c:when>
				<c:otherwise>
					<siga:Select id="idActuacion" queryId="getActuacionesDesigna" params="${paramActuacionesJson}" selectedIds="${idActuacionSelected}" firstLabel="Designacion" required="true"  width="300"   />
				</c:otherwise>
			</c:choose>
		
		</td>
	</tr>	

	<tr>
		<td class="labelText">
			Observaciones<c:out value="${ClsConstants.ACCESS_FULL}"></c:out>
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${accionModo=='ver'}">
					<html:textarea property="observaciones"  rows="6" cols="50" style="width:350" styleClass="boxConsulta" readonly="true"/>
				</c:when>
				<c:otherwise>
					<html:textarea property="observaciones"  onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="6" cols="10" styleClass="box" style="width:100"/>
				</c:otherwise>
			</c:choose>
		</td>	
	</tr>
	</table>
	</siga:ConjCampos>
</div>
<%@ include file="/html/jsp/general/ficheros.jsp"%>
</html:form>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,C" modal="P"  modo="${accionModo}"/>
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	
	<script language="JavaScript">	
	
	
	
	function upload() 
	{	
		jQuery("#idActuacion").removeAttr("disabled");
		document.forms['DefinirDocumentacionDesignaForm'].modo.value = "upload";
		document.forms['DefinirDocumentacionDesignaForm'].submit();
		

	}
	function download()
	{
		jQuery("#idActuacion").removeAttr("disabled");
		document.forms['DefinirDocumentacionDesignaForm'].target="submitArea";
		document.forms['DefinirDocumentacionDesignaForm'].modo.value = "download";
		document.InformeForm.submit();

	}
	function borrar()
	{
		
		if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
			jQuery("#idActuacion").removeAttr("disabled");
			document.forms['DefinirDocumentacionDesignaForm'].target="submitArea";
			document.forms['DefinirDocumentacionDesignaForm'].modo.value = "borrarArchivo";
			document.forms['DefinirDocumentacionDesignaForm'].submit();
		}

	}
	
	
	//Asociada al boton Restablecer Con este metodo se restablecerian los combos anidados del siga:Select -->
	function accionRestablecer() 
	{		
		document.forms['DefinirDocumentacionDesignaForm'].reset();
		jQuery("#idDocumento").data("set-id-value",jQuery("#idDocumento").data("inival"));
		jQuery("#idTipoDocumento").change();
		
	}
		
	//Asociada al boton Guardar y Cerrar -->
	function accionGuardarCerrar() {
		
		sub();
		document.forms['DefinirDocumentacionDesignaForm'].modo.value = document.getElementById("modoEntrada").value;
		jQuery("#idTipoDocumento").removeAttr("disabled");
		if( document.forms['DefinirDocumentacionDesignaForm'].idTipoDocumento.value==''){
			error = "<siga:Idioma key='errors.required' arg0='sjcs.ejg.documentacion.tipoDocumentacion'/>";
			alert(error);
			fin();
			return false;
		}
		jQuery("#idTipoDocumento").attr("disabled",'disabled');
		
		if(!document.forms['DefinirDocumentacionDesignaForm'].idFichero || document.forms['DefinirDocumentacionDesignaForm'].idFichero.value=='' ){
		
			if(!document.forms['DefinirDocumentacionDesignaForm'].theFile || document.forms['DefinirDocumentacionDesignaForm'].theFile.value=='' ){
				error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.archivo'/>";
				alert(error);
				fin();
				return false;
			}
			
			if(document.forms['DefinirDocumentacionDesignaForm'].theFile && document.forms['DefinirDocumentacionDesignaForm'].theFile.value!='' && !TestFileType(document.forms['DefinirDocumentacionDesignaForm'].theFile.value, ['DOC','DOCX','PDF','JPG','PNG','RTF','TXT'])){
				fin();
				return false;
			}
		}
			
		if(document.forms['DefinirDocumentacionDesignaForm'].theFile && document.forms['DefinirDocumentacionDesignaForm'].theFile.value!=''){
			document.forms['DefinirDocumentacionDesignaForm'].descripcionArchivo.value = jQuery("#idTipoDocumento").find("option:selected").text();
		}
			
		/*if(document.forms['DefinirDocumentacionDesignaForm'].idFichero && document.forms['DefinirDocumentacionDesignaForm'].idFichero.value!='' && document.forms['DefinirDocumentacionDesignaForm'].idTipoDocumento.value!=document.forms['DefinirDocumentacionDesignaForm'].idTipoDocumentoAnterior.value) {
			
			if(!confirm("<siga:Idioma key='administracion.informes.mensaje.aviso.archivo.eliminar'/>")){
				fin();
				return false;
			}
			document.forms['DefinirDocumentacionDesignaForm'].borrarFichero.value = 'true';
		}*/
		
		jQuery("#idActuacion").removeAttr("disabled");
		jQuery("#idTipoDocumento").removeAttr("disabled");
		
		
       	document.forms['DefinirDocumentacionDesignaForm'].submit();
		window.top.returnValue="MODIFICADO";		
		
	}		
		
		//Asociada al boton Cerrar -->
		function accionCerrar()
		{	
			top.cierraConParametros("NORMAL");			
		}	
		
		
		function onLoad()
		{
			jQuery("#divFicheros").css("display", "block");
			accionComboActuacion();
			var idActuacion = jQuery("#idActuacion").val();
			if(idActuacion!='' )
				jQuery("#idActuacion").attr("disabled",'disabled');
			
					
		}
		jQuery(function(){
			jQuery("#idDocumento").on("change", function(){
				if(document.forms['DefinirDocumentacionDesignaForm'].modo.value =="insertar"){
					if(document.forms['DefinirDocumentacionDesignaForm'].idDocumento.value=='')
						jQuery("#divFicheros").css("display", "none");
					else
						jQuery("#divFicheros").css("display", "block");
				}
			});
		});
		
		function eliminarFichero()
		{
			jQuery("#idActuacion").removeAttr("disabled");
			if(confirm('<siga:Idioma key="administracion.informes.mensaje.aviso.archivo.eliminar"/>')){
				document.forms['DefinirDocumentacionDesignaForm'].modo.value = "borrarFichero";	
				document.forms['DefinirDocumentacionDesignaForm'].submit();
			}
		}
		function downloadFichero()
		{
			jQuery("#idActuacion").removeAttr("disabled");
			document.forms['DefinirDocumentacionDesignaForm'].modo.value = "downloadFichero";
			document.forms['DefinirDocumentacionDesignaForm'].submit();
		}
		
		//Si selecciona actuacion , dejamos seleccionado el tipo documento justificante(idtipo=1)
		function accionComboActuacion()
		{
			
			var idActuacion = jQuery("#idActuacion").val();
			if(idActuacion!='' ){
				
				jQuery("#idTipoDocumento").val("1");
				jQuery("#idTipoDocumento").attr("disabled",'disabled');
				
			}else{
				jQuery("#idTipoDocumento").removeAttr("disabled");
				
			}
				
			
		}
		
		
		jQuery(function(){
			jQuery("#idActuacion").on("change", function(){
				accionComboActuacion();
			});
			
		});
		
		
		</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>