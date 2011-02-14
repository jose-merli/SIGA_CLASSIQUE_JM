<!-- listadoInformes.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript"
	src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
  
</head>

<body onload="ajustarCabeceraTabla();ajusteAlto('listadoInformesDiv');">
<bean:define id="informes" name="listadoInformes" scope="request"/>
<div>		
	<table id='listadoInformesCab' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='8%'><siga:Idioma key="administracion.informes.literal.colegio"/></td>
			<td align='center' width='10%'><siga:Idioma key="administracion.informes.literal.tipoInforme"/></td>
			<td align='center' width='5%'><siga:Idioma key="administracion.informes.literal.orden"/></td>
			<td align='center' width='19%'><siga:Idioma key="administracion.informes.literal.nombre"/></td>
			<td align='center' width='19%'><siga:Idioma key="administracion.informes.literal.descripcion"/></td>
			<td align='center' width='6%'><siga:Idioma key="administracion.informes.literal.visible"/></td>
			<td align='center' width='6%'><siga:Idioma key="administracion.informes.literal.solicitantes"/></td>
			<td align='center' width='6%'><siga:Idioma key="administracion.informes.literal.destinatarios.enviarA"/></td>
			<td align='center' width='12%'>&nbsp;</td>
		</tr>
	</table>
</div>
<div id='listadoInformesDiv' style='height:400;width:100%; overflow-y:auto'>
	<table class="tablaCampos" id='listadoInformes' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		
		
		<c:choose>
   			<c:when test="${empty informes}">
				<br>
	   		 		<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 			<br>
   			</c:when>
   			<c:otherwise>
   			<tr>
     			<td width='8%'></td>     		
				<td width='10%'></td>
				<td width='5%'></td>
				<td width='19%'></td>
				<td width='19%'></td>
				<td width='6%'></td>
				<td width='6%'></td>
				<td width='6%'></td>
				<td width='12%'></td>
			</tr>
   
				<c:forEach items="${informes}" var="informe" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'				    
	  				botones="${informe.botones}" 
	  				elementos="${informe.elementosFila}"
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				clase="listaNonEdit">
				
				<input type="hidden" name="idPlantilla_${status.count}" value="${informe.idPlantilla}">
				<input type="hidden" name="idInstitucion_${status.count}" value="${informe.idInstitucion}">
				<input type="hidden" name="claseTipoInforme_${status.count}" value="${informe.claseTipoInforme}">
				 
				<td align='left'><c:out value="${informe.descripcionInstitucion}"></c:out></td>
				<td align='left'><c:out value="${informe.descripcionTipoInforme}"></c:out></td>
				<td align='center'><c:choose>
					<c:when test="${informe.orden!=''}">
						<c:out value="${informe.orden}"/>
					</c:when>
					<c:otherwise>
						&nbsp;
						</c:otherwise>
					</c:choose>
				
				</td>
				<td align='left'><c:out value="${informe.alias}"></c:out></td>
				<td align='left'><c:out value="${informe.descripcion}"></c:out></td>
				<td align='center'><c:out value="${informe.visible}"></c:out></td>
				<td align='center'><c:out value="${informe.ASolicitantes}"></c:out></td>
				<td align='center'><c:out value="${informe.destinatarios}"></c:out></td>

				
			</siga:FilaConIconos>
		</c:forEach>
	</c:otherwise>
</c:choose>
</table>

<html:form action="/ADM_GestionInformes"  name="InformeFormEdicion" type="com.siga.administracion.form.InformeForm">
	<html:hidden property="modo" />
	<html:hidden property="idPlantilla" />
	<html:hidden property="idInstitucion" />
	<html:hidden property="claseTipoInforme" />
	<input type="hidden" name="actionModal" />
</html:form>

</div>

<iframe name="submitArea"	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>	
				
<script type="text/javascript">
function ajustarCabeceraTabla(){
	
	if (document.all.listadoInformes.clientHeight < document.all.listadoInformesDiv.clientHeight) {
		   document.all.listadoInformesCab.width='100%';
		   
	  } else {
		   document.all.listadoInformesCab.width='98.43%';
		   
	  }
}
function refrescarLocal(){
	parent.refrescarLocal();
}
function consultar(fila){
	document.InformeFormEdicion.modo.value = "consultar";
	var idPlantillaFila = 'idPlantilla_'+fila;
	var idInstitucionFila = 'idInstitucion_'+fila;
	var claseTipoInformeFila = 'claseTipoInforme_'+fila;
	document.InformeFormEdicion.idPlantilla.value = document.getElementById(idPlantillaFila).value;
	document.InformeFormEdicion.idInstitucion.value = document.getElementById(idInstitucionFila).value;
	document.InformeFormEdicion.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
	var resultado = ventaModalGeneral(document.InformeFormEdicion.name,"G");
	
}
function editar(fila){
	var idPlantillaFila = 'idPlantilla_'+fila;
	var idInstitucionFila = 'idInstitucion_'+fila;
	var claseTipoInformeFila = 'claseTipoInforme_'+fila;
	document.InformeFormEdicion.idPlantilla.value = document.getElementById(idPlantillaFila).value;
	document.InformeFormEdicion.idInstitucion.value = document.getElementById(idInstitucionFila).value;
	document.InformeFormEdicion.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
	document.InformeFormEdicion.modo.value = "editar";
	
	var resultado = ventaModalGeneral(document.InformeFormEdicion.name,"G");
	parent.refrescarLocal();
	 

}
function borrar(fila){
	if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')) { 
		document.InformeFormEdicion.modo.value = "borrar";
		var idPlantillaFila = 'idPlantilla_'+fila;
		var idInstitucionFila = 'idInstitucion_'+fila;
		var claseTipoInformeFila = 'claseTipoInforme_'+fila;
		document.InformeFormEdicion.idPlantilla.value = document.getElementById(idPlantillaFila).value;
		document.InformeFormEdicion.idInstitucion.value = document.getElementById(idInstitucionFila).value;
		document.InformeFormEdicion.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
		document.InformeFormEdicion.target = "submitArea";
		document.InformeFormEdicion.submit();	
	}
}

function duplicar(fila)
{
	var idPlantillaFila = 'idPlantilla_'+fila;		
	var claseTipoInformeFila = 'claseTipoInforme_'+fila;	
	document.InformeFormEdicion.idPlantilla.value = document.getElementById(idPlantillaFila).value;
	document.InformeFormEdicion.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
	document.InformeFormEdicion.modo.value = "duplicar";
	document.InformeFormEdicion.submit();	
}


var messageError='${InformeForm.msgError}';
var messageAviso='${InformeForm.msgAviso}';
if (messageAviso)
	alert(messageAviso);
if (messageError)
	alert(messageError);
		
	</script>
</body>

</html>