<!DOCTYPE html>
<html>
<head>
<!-- operarDocumentacionEJG.jsp -->
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
<% 


%>
<html:javascript formName="DefinirDocumentacionEJGForm" staticJavascript="false" /> 
<html:form action="${path}"  method="POST" enctype="multipart/form-data" target="submitArea">
	<html:hidden styleId="modo" property = "modo" />
	<html:hidden styleId="idInstitucion" property = "idInstitucion" />
	<html:hidden styleId="idTipoEJG" property = "idTipoEJG"/>
	<html:hidden styleId="anio" property = "anio"/>
	<html:hidden styleId="numero" property = "numero"/>
	<html:hidden styleId="idDocumentacion" property = "idDocumentacion"/>
	<html:hidden styleId="extensionArchivo" property = "extensionArchivo"/>
	<html:hidden styleId="fechaArchivo" property = "fechaArchivo"/>
	<html:hidden styleId="nombreArchivo" property = "nombreArchivo"/>
	<html:hidden styleId="descripcionArchivo" property = "descripcionArchivo"/>
	<html:hidden styleId="directorioArchivo" property = "directorioArchivo"/>
	<html:hidden styleId="numEjg" property = "numEjg" />
	<html:hidden styleId="solicitante" property = "solicitante" value="${DefinirDocumentacionEJGForm.solicitante}"/>
	<html:hidden styleId="borrarFichero" property = "borrarFichero" />
	<html:hidden styleId="idFichero" property = "idFichero" />
	
	<html:hidden styleId="idPresentadorAnterior" property = "idPresentadorAnterior" value ="${DefinirDocumentacionEJGForm.idPresentador}"/>
	<html:hidden styleId="idTipoDocumentoAnterior" property = "idTipoDocumentoAnterior" value ="${DefinirDocumentacionEJGForm.idTipoDocumento}"/>
	<html:hidden styleId="idDocumentoAnterior" property = "idDocumentoAnterior" value ="${DefinirDocumentacionEJGForm.idDocumento}"/>
	
	<input type="hidden" id="modoEntrada" value="${DefinirDocumentacionEJGForm.modo}"/> 

	<bean:define id="presentadorSelected" name="presentadorSelected" type="java.util.List"	 scope="request" />
	<bean:define id="idTipoDocumentoSelected" name="idTipoDocumentoSelected" type="java.util.List"	 scope="request" />
	<bean:define id="idDocumentoSelected" name="idDocumentoSelected" type="java.util.List"	 scope="request" />
	

		
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="pestana.justiciagratuitaejg.documentacion">
		
	<table class="tablaCampos" align="center" border ="0">
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaLimitePresentacion"/>
		
	</td>
	<td>
		<c:choose>
			<c:when test="${accionModo=='ver'}">
				<html:text name="DefinirDocumentacionEJGForm" property="fechaLimite" size="10" styleClass="boxConsulta"readonly="true"></html:text>
			</c:when>
			<c:otherwise>
				<siga:Fecha nombreCampo="fechaLimite"  valorInicial="${DefinirDocumentacionEJGForm.fechaLimite}"></siga:Fecha>
			</c:otherwise>
		</c:choose>
				
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaPresentacion"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${accionModo=='ver'}">
				<html:text name="DefinirDocumentacionEJGForm" property="fechaEntrega" size="10" styleClass="boxConsulta" readonly="true"></html:text>
			</c:when>
			<c:otherwise>
				<siga:Fecha nombreCampo="fechaEntrega" valorInicial="${DefinirDocumentacionEJGForm.fechaEntrega}"></siga:Fecha>
			</c:otherwise>
		</c:choose>
		
			
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionEJG.regentrada"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${accionModo=='ver'}">
				<html:text name="DefinirDocumentacionEJGForm" property="regEntrada"  size="20" styleClass="boxConsulta" readonly="true"/>
			</c:when>
			<c:otherwise>
				<html:text name="DefinirDocumentacionEJGForm" property="regEntrada"  size="20" styleClass="box"  readonly="false"/>
			</c:otherwise>
		</c:choose>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionEJG.regsalida"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${accionModo=='ver'}">
				<html:text name="DefinirDocumentacionEJGForm" property="regSalida" size="20" styleClass="boxConsulta"  readonly="true"></html:text>&nbsp;&nbsp;
			</c:when>
			<c:otherwise>
				<html:text name="DefinirDocumentacionEJGForm" property="regSalida" size="20" styleClass="box" readonly="false"></html:text>&nbsp;&nbsp;
			</c:otherwise>
		</c:choose>
	</td>
	</tr>	
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.presentador'/>&nbsp;(*)
		</td>
		<td colspan="3">
			<c:choose>
				<c:when test="${DefinirDocumentacionEJGForm.modo=='insertar'}">
					<siga:Select id="idPresentador" queryId="getPresentador"   required="true" width="300"   />
				</c:when>
				<c:when test="${accionModo=='ver'}">
					<siga:Select id="idPresentador" queryId="getPresentador"  selectedIds="${presentadorSelected}"  required="true" disabled="true"  width="300"   />
				</c:when>
				<c:otherwise>
					<siga:Select id="idPresentador" queryId="getPresentador"  selectedIds="${presentadorSelected}"   required="true" width="300"  />
				</c:otherwise>
			</c:choose>
				
		</td>
	</tr>
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.tipoDocumentacion'/>&nbsp;(*)
		</td>
		<td colspan="3">
			<c:choose>
				<c:when test="${DefinirDocumentacionEJGForm.modo=='insertar'}">
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentacionEjg" queryParamId="idtipodocumento" childrenIds="idDocumento"   required="true"  width="300"   />
				</c:when>
				<c:when test="${accionModo=='ver'}">
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentacionEjg" queryParamId="idtipodocumento" childrenIds="idDocumento" selectedIds="${idTipoDocumentoSelected}" required="true" disabled="true"  width="300"   />
				</c:when>
				<c:otherwise>
					<siga:Select id="idTipoDocumento" queryId="getTipoDocumentacionEjg" queryParamId="idtipodocumento" childrenIds="idDocumento"  selectedIds="${idTipoDocumentoSelected}" required="true"  width="300"   />
				</c:otherwise>
			</c:choose>
		
		</td>
	</tr>					
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.documentacion'/>&nbsp;(*)
		</td>
		<td colspan="3">
			<c:choose>
				<c:when test="${DefinirDocumentacionEJGForm.modo=='insertar'}">
					<siga:Select  id="idDocumento" queryId="getTipoDocumentoEjg" firstLabel="general.combo.todos" parentQueryParamIds="idtipodocumento"  required="true"  width="300"   />
				</c:when>
				<c:when test="${accionModo=='ver'}">
					<siga:Select id="idDocumento" queryId="getTipoDocumentoEjg" parentQueryParamIds="idtipodocumento" params="${idTipoDocumentoJson}"  selectedIds="${idDocumentoSelected}" required="true" disabled="true"  width="300"   />
				</c:when>
				<c:otherwise>
				
				<siga:Select id="idDocumento" queryId="getTipoDocumentoEjg"  parentQueryParamIds="idtipodocumento"  params="${idTipoDocumentoJson}" selectedIds="${idDocumentoSelected}"  required="true"  width="300"   />
				
				</c:otherwise>
			</c:choose>
		
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="pestana.justiciagratuitaejg.documentacion"/><c:out value="${ClsConstants.ACCESS_FULL}"></c:out>
		</td>
		<td class="labelText" colspan="3">
			<c:choose>
				<c:when test="${accionModo=='ver'}">
				<html:textarea property="documentacion"  rows="6" cols="60" style="width:600" styleClass="boxConsulta" readonly="true"/>
				</c:when>
				<c:otherwise>
				<html:textarea property="documentacion"  onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="6" cols="60" styleClass="box" style="width:600"/>
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
		
		document.forms['DefinirDocumentacionEJGForm'].modo.value = "upload";
		document.forms['DefinirDocumentacionEJGForm'].submit();
		

	}
	function download()
	{
		document.forms['DefinirDocumentacionEJGForm'].target="submitArea";
		document.forms['DefinirDocumentacionEJGForm'].modo.value = "download";
		document.InformeForm.submit();

	}
	function borrar()
	{
		if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
			document.forms['DefinirDocumentacionEJGForm'].target="submitArea";
			document.forms['DefinirDocumentacionEJGForm'].modo.value = "borrarArchivo";
			document.forms['DefinirDocumentacionEJGForm'].submit();
		}

	}
	
	
	//Asociada al boton Restablecer Con este metodo se restablecerian los combos anidados del siga:Select -->
	function accionRestablecer() 
	{		
		document.forms['DefinirDocumentacionEJGForm'].reset();
		jQuery("#idDocumento").data("set-id-value",jQuery("#idDocumento").data("inival"));
		jQuery("#idTipoDocumento").change();
		
	}
		
	//Asociada al boton Guardar y Cerrar -->
	function accionGuardarCerrar() 
	{
		
		sub();
		document.forms['DefinirDocumentacionEJGForm'].modo.value = document.getElementById("modoEntrada").value;
		
		if(document.forms['DefinirDocumentacionEJGForm'].modo.value =='modificar' && document.forms['DefinirDocumentacionEJGForm'].idDocumento.value==''){
			error = "<siga:Idioma key='errors.required' arg0='sjcs.ejg.documentacion.documentacion'/>";
			alert(error);
			fin();
			return false;
		}
		
		if(document.forms['DefinirDocumentacionEJGForm'].theFile && document.forms['DefinirDocumentacionEJGForm'].theFile.value!='' && !TestFileType(document.forms['DefinirDocumentacionEJGForm'].theFile.value, ['DOC','DOCX','PDF','JPG','PNG','RTF','TXT'])){
			fin();
			return false;
		}
		if (validateDefinirDocumentacionEJGForm(document.forms['DefinirDocumentacionEJGForm'])){
				//si ya se habia seleccionado fichero y ahora nom cambia el docuemnto le avisamos que se va a borrar el anterior
				
				
			if(document.forms['DefinirDocumentacionEJGForm'].theFile && document.forms['DefinirDocumentacionEJGForm'].theFile.value!=''){
				document.forms['DefinirDocumentacionEJGForm'].descripcionArchivo.value = jQuery("#idDocumento").find("option:selected").text();
			}
			
			if(document.forms['DefinirDocumentacionEJGForm'].idFichero && document.forms['DefinirDocumentacionEJGForm'].idFichero.value!='' && (document.forms['DefinirDocumentacionEJGForm'].idDocumento.value!=document.forms['DefinirDocumentacionEJGForm'].idDocumentoAnterior.value ||
					document.forms['DefinirDocumentacionEJGForm'].idPresentador.value!=document.forms['DefinirDocumentacionEJGForm'].idPresentadorAnterior.value ||
					document.forms['DefinirDocumentacionEJGForm'].idTipoDocumento.value!=document.forms['DefinirDocumentacionEJGForm'].idTipoDocumentoAnterior.value) ){
				
				if(!confirm("<siga:Idioma key='administracion.informes.mensaje.aviso.archivo.eliminar'/>")){
					fin();
					return false;
				}
				document.forms['DefinirDocumentacionEJGForm'].borrarFichero.value = 'true';
			}
	       	document.forms['DefinirDocumentacionEJGForm'].submit();
			window.top.returnValue="MODIFICADO";		
		}else{
			fin();
			return false;
		}
	}		
		
		//Asociada al boton Cerrar -->
		function accionCerrar()
		{	
			top.cierraConParametros("NORMAL");			
		}	
		
		
		function onLoad()
		{
			if(document.forms['DefinirDocumentacionEJGForm'].modo.value =="insertar"){
				jQuery("#divFicheros").css("display", "none");
			}else{
				jQuery("#divFicheros").css("display", "block");
			}
			
		}
		jQuery(function(){
			jQuery("#idDocumento").on("change", function(){
				if(document.forms['DefinirDocumentacionEJGForm'].modo.value =="insertar"){
					if(document.forms['DefinirDocumentacionEJGForm'].idDocumento.value=='')
						jQuery("#divFicheros").css("display", "none");
					else
						jQuery("#divFicheros").css("display", "block");
				}
			});
			
			
			
		});
		
		function eliminarFichero()
		{
			if(confirm('<siga:Idioma key="administracion.informes.mensaje.aviso.archivo.eliminar"/>')){
				document.forms['DefinirDocumentacionEJGForm'].modo.value = "borrarFichero";	
				document.forms['DefinirDocumentacionEJGForm'].submit();
			}
		}
		function downloadFichero()
		{
			document.forms['DefinirDocumentacionEJGForm'].modo.value = "downloadFichero";
			document.forms['DefinirDocumentacionEJGForm'].submit();
		}
		
		
		</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>