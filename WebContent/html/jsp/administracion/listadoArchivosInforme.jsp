<!-- listadoArchivosInforme.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>

	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	
	<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
	<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
	
</head>

<body onload="ajustarCabeceraTabla();ajusteAlto('listadoInformesDiv');inicio();">


	
<html:form action="/ADM_GestionInformes" method="POST" target="_self" enctype="multipart/form-data">
	<html:hidden property="modo" />
	<html:hidden property="modoInterno" />
	<html:hidden property="destinatarios" />
	<html:hidden property="filaInformeSeleccionada" />
	

<c:if test="${InformeForm.idInstitucion!='0' || InformeForm.usrBean.location=='2000'}">
<div id="divUpload">
<table>

		<tr>
			<td width="20%"></td>
			<td width="40%"></td>
			<td width="20%"></td>
			<td width="20%"></td>
			
		</tr>
	
	<tr>				
			<td class="labelText" >
				<siga:Idioma key="administracion.informes.literal.archivo"/>
			</td>				
			<td >
				<html:file  name="InformeForm" property="theFile" styleClass="boxCombo" style="width:500px;"/>
			</td>
			<td class="labelText">
				<html:select styleClass="boxCombo" style="width:200px;"
						name="InformeForm" property="lenguaje" >
						<bean:define id="lenguajes" name="InformeForm"
							property="lenguajes" type="java.util.Collection" />
							<html:optionsCollection name="lenguajes" value="codigoExt"
									label="descripcion" />
					</html:select>
			
			</td>
							
			<td class="tdBotones">

				<input type="button" alt="<siga:Idioma key="administracion.informes.boton.archivo.anadir"/>" id="idButton" onclick="return upload();" class="button" value="<siga:Idioma key="administracion.informes.boton.archivo.anadir"/>">

			</td>
		</tr>
		<tr>
			<td colspan = "4" >&nbsp;</td>
			
		</tr>
		
</table>
</div>
</c:if>
<div>		
	<table id='listadoArchivosCab' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class ='tableTitle'>
			<td align='center' width='30%'><b><siga:Idioma key="administracion.informes.literal.archivo.nombre"/></b></td>
			<td align='center' width='30%'><b><siga:Idioma key="administracion.informes.literal.archivo.permisos"/></b></td>
			<td align='center' width='30%'><b><siga:Idioma key="administracion.informes.literal.archivo.fecha"/></b></td>
			<td align='center' width='10%'>&nbsp;</td>
		</tr>
	</table>
</div>
<div id='listadoArchivosDiv' style='height:400;width:100%; overflow-y:auto'>

<table class="tablaCampos" id='listadoArchivos' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
	
		<c:choose>
			<c:when test="${InformeForm.directorioFile==null}">
			<tr>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='10%'></td>
				</tr>
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="background-color:transparent; text-align:center;" colspan = "4">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		</td>
	 			</tr>
				
   			</c:when>
   			<c:when test="${empty InformeForm.directorioFile.files}">
   				<tr>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='10%'></td>
				</tr>
   				
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="background-color:transparent; text-align:center;" colspan = "4">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		</td>
	 			</tr>
   			</c:when>
   			<c:otherwise>
		
				<tr>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='10%'></td>
				</tr>
				<c:forEach items="${InformeForm.directorioFile.files}" var="fileInforme" varStatus="status">                 
					<siga:FilaConIconos	fila='${status.count}'
		  				botones="${fileInforme.botones}" 
		  				elementos="${fileInforme.elementosFila}"
		  				pintarEspacio="no"
		  				visibleConsulta="no"
		  				visibleEdicion = "no"
		  				visibleBorrado = "si"
		  				clase="listaNonEdit"
		  				id="filaInforme_${status.count}"
		  				>
						<td align='left'><c:out value="${fileInforme.nombre}"></c:out></td>
						<td align='left'><c:out value="${fileInforme.permisos}"></c:out> </td>
						<td align='left'><c:out value="${fileInforme.fecha}"></c:out></td>
					</siga:FilaConIconos>
				</c:forEach>
				
				
			</c:otherwise>
	</c:choose>

</table>

</div>
</html:form>

<script language="JavaScript">
function download(fila)
{
	document.InformeForm.filaInformeSeleccionada.value=fila-1;
	document.InformeForm.target="submitArea";
	document.InformeForm.modo.value = "download";
	document.InformeForm.submit();

}
function borrar(fila)
{
	if(confirm('<siga:Idioma key="administracion.informes.mensaje.aviso.archivo.eliminar"/>')){
		var idFila = "filaInforme_"+fila;
		document.InformeForm.filaInformeSeleccionada.value=fila-1;
		document.InformeForm.target="submitArea";
		document.InformeForm.modo.value = "comprobarBorrarInformeFile";
		document.InformeForm.submit();
	}

}
function upload() 
{	
	if(document.InformeForm.theFile.value==''){
		error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.archivo'/>";
		alert(error);
		return;
	}
	
	if (!TestFileType(document.InformeForm.theFile.value, ['DOC','XSL'])){
		fin();
		return false;
	}
	document.InformeForm.modo.value = "upload";
	document.InformeForm.submit();
	

}
function refrescarLocal()
{
	
	
}

function ajustarCabeceraTabla(){
	
		if (document.getElementById("listadoArchivos").clientHeight < document.all.listadoArchivosDiv.clientHeight) {
			document.getElementById("listadoArchivosCab").width='100%';
		   
	  } else {
		  document.getElementById("listadoArchivosCab").width='98.43%';
		   
	  }
}
function inicio() 
{	
	if(document.getElementById("divUpload")){
		if(document.InformeForm.modoInterno.value=='consultar'||document.InformeForm.modo.value=='nuevoListadoArchivosInforme'){
			document.InformeForm.modoInterno.value = '';
			document.getElementById("divUpload").style.display = "none";
			
		}
	}
}

</script>
</body>

</html>