<!-- listadoActuacionesAsistencia.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%
boolean esFichaColegial = false;

String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
if ((sEsFichaColegial != null)
		&& ((sEsFichaColegial.equalsIgnoreCase("1"))||(sEsFichaColegial.equalsIgnoreCase("true"))  )) {
	esFichaColegial = true;
}

%>

<html>

<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
</head>

<body onload="ajustarCabeceraTabla();ponerTitulo()">

	<bean:define id="asistencia" name="asistencia" scope="request"
		type="com.siga.gratuita.form.AsistenciaForm" />

	<bean:define id="error" name="error" scope="request" />
	<bean:define id="actuacionesAsistenciaList"
		name="actuacionesAsistenciaList" type="java.util.Collection"
		scope="request" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"
		property="path" scope="request" />
		
	<html:form action="${path}" style="display: none">
		<html:hidden property="modo" value="abrir" />
	</html:form>
	
	<table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos"><c:out
					value="${asistencia.descripcion}" /></td>
		</tr>
	</table>
	<div>
		<table id='listadoActuacionesAsistenciaCab' border='1' width='100%'
			cellspacing='0' cellpadding='0' style="table-layout: fixed;">
			<tr class='tableTitle'>
				<td align='center' width='8%'><siga:Idioma
						key="gratuita.listadoActuacionesAsistencia.literal.nactuacion" /></td>
				<td align='center' width='8%'><siga:Idioma
						key="gratuita.listadoActuacionesAsistencia.literal.fecha" /></td>
				<td align='center' width='11%'><siga:Idioma
						key="gratuita.busquedaAsistencias.literal.fechaAsistencia" />&nbsp;<siga:Idioma
						key='general.literal.desde' /></td>
				<td align='center' width='10%'><siga:Idioma
						key='informes.cartaOficio.numAsuntoLista' /></td>
				<td align='center' width='14%'><siga:Idioma
						key="gratuita.mantActuacion.literal.tipoActuacion" /></td>
				<td align='center' width='7%'><siga:Idioma
						key="gratuita.listadoActuacionesAsistencia.literal.justificado" /></td>
				<td align='center' width='6%'><siga:Idioma
						key="gratuita.actuacionesDesigna.literal.validada" /></td>
				<td align='center' width='6%'><siga:Idioma
						key="gratuita.procedimientos.literal.anulada" /></td>
				<td align='center' width='20%'><siga:Idioma
						key="gratuita.actuacionesDesigna.literal.facturacion" /></td>
				<td align='center' width='10%'></td>
			</tr>
		</table>
	</div>
	<div id='listadoActuacionesAsistenciaDiv'
		style='height: 400; width: 100%; overflow-y: auto'>

		<table class="tablaCampos" id='listadoActuacionesAsistencia'
			border='1' align='center' width='100%' cellspacing='0'
			cellpadding='0' style='table-layout: fixed'>
			<c:choose>

				<c:when test="${empty actuacionesAsistenciaList}">
					<tr>
						<td align='center' width='8%'></td>
						<td align='center' width='8%'></td>
						<td align='center' width='11%'></td>
						<td align='center' width='10%'></td>
						<td align='center' width='14%'></td>
						<td align='center' width='7%'></td>
						<td align='center' width='6%'></td>
						<td align='center' width='6%'></td>
						<td align='center' width='20%'></td>
						<td align='center' width='10%'></td>
					</tr>

					<tr class='titulitos' id="noRecordFound">
						<td class="titulitos" style="text-align: center" colspan="10">
							<siga:Idioma key="messages.noRecordFound" />
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td align='center' width='8%'></td>
						<td align='center' width='8%'></td>
						<td align='center' width='11%'></td>
						<td align='center' width='10%'></td>
						<td align='center' width='14%'></td>
						<td align='center' width='7%'></td>
						<td align='center' width='6%'></td>
						<td align='center' width='6%'></td>
						<td align='center' width='20%'></td>
						<td align='center' width='10%'></td>
					</tr>
					<c:forEach items="${actuacionesAsistenciaList}"
						var="actuacionAsistencia" varStatus="status">


						<siga:FilaConIconos fila='${status.count}'
							id="trtabla_${status.count}"
							botones="${actuacionAsistencia.botones}" clase="listaNonEdit">

							<input type="hidden" id="idInstitucion_${status.count}"
								value="${actuacionAsistencia.idInstitucion}">
							<input type="hidden" id="anio_${status.count}"
								value="${actuacionAsistencia.anio}">
							<input type="hidden" id="numero_${status.count}"
								value="${actuacionAsistencia.numero}">
							<input type="hidden" id="idActuacion_${status.count}"
								value="${actuacionAsistencia.idActuacion}">

							<td align="center"><c:out
									value="${actuacionAsistencia.idActuacion}" /></td>
							<td align="center"><c:out
									value="${actuacionAsistencia.fecha}" /></td>
							<td align="center"><c:out
									value="${actuacionAsistencia.fechaHoraAsistencia}" /></td>
							<td align="center">&nbsp; <c:out
									value="${actuacionAsistencia.numeroAsunto}" />
							</td>

							<td align="center">&nbsp; <c:out
									value="${actuacionAsistencia.descripcionActuacion}" />
							</td>
							<td align="center"><c:choose>
									<c:when test="${actuacionAsistencia.fechaJustificacion!=''}">
										<c:out value="${actuacionAsistencia.fechaJustificacion}" />
									</c:when>
									<c:otherwise>
				  				&nbsp;
				  			</c:otherwise>
								</c:choose></td>

							<td align="center"><c:choose>
									<c:when test="${actuacionAsistencia.validada=='1'}">
										<siga:Idioma key="general.yes" />
									</c:when>
									<c:otherwise>
										<siga:Idioma key="general.no" />
									</c:otherwise>
								</c:choose></td>
							<td align="center"><c:choose>
									<c:when test="${actuacionAsistencia.anulacion=='1'}">
										<siga:Idioma key="general.yes" />
									</c:when>
									<c:otherwise>
										<siga:Idioma key="general.no" />
									</c:otherwise>
								</c:choose></td>
							<td align="center">
								<c:choose>
									<c:when test="${actuacionAsistencia.nombreFacturacion!=''}">
										<c:out value="${actuacionAsistencia.nombreFacturacion}" />
									</c:when>
									<c:otherwise>
				  					&nbsp;
				  					</c:otherwise>
								</c:choose>
							</td>
						</siga:FilaConIconos>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</div>

	<c:if test="${asistencia.botonesDetalle!=''}">
		<siga:ConjBotonesAccion botones="${asistencia.botonesDetalle}"
			clase="botonesDetalle" />
	</c:if>

	<html:form action="${path}" method="POST"
		name="ActuacionAsistenciaFormEdicion"
		styleId="ActuacionAsistenciaFormEdicion"
		type="com.siga.gratuita.form.ActuacionAsistenciaForm">

		<html:hidden property="modo" value=""
			name="ActuacionAsistenciaFormEdicion" />
		<html:hidden property="idInstitucion"
			styleId="idInstitucion"
			name="ActuacionAsistenciaFormEdicion"
			value="${asistencia.idInstitucion }" />
		<html:hidden property="anio" name="ActuacionAsistenciaFormEdicion"
			styleId="anio"
			value="${asistencia.anio}" />
		<html:hidden property="numero" name="ActuacionAsistenciaFormEdicion"
			styleId="numero"
			value="${asistencia.numero}" />
		<html:hidden property="fichaColegial"
			styleId="fichaColegial"
			name="ActuacionAsistenciaFormEdicion"
			value="${asistencia.fichaColegial}" />
		<html:hidden property="idTipoAsistencia"
			styleId="idTipoAsistencia"
			name="ActuacionAsistenciaFormEdicion"
			value="${asistencia.idTipoAsistenciaColegio}" />
		<html:hidden property="idActuacion"
			styleId="idActuacion"
			name="ActuacionAsistenciaFormEdicion" />
		<input type="hidden" name="actionModal" />
	</html:form>

	<iframe name="submitArea"
		src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		style="display: none"></iframe>
	<script type="text/javascript">


function refrescarLocal() {	
}

function ajustarCabeceraTabla(){
	if (document.getElementById("listadoActuacionesAsistencia").clientHeight < document.getElementById("listadoActuacionesAsistenciaDiv").clientHeight) {
	   document.getElementById("listadoActuacionesAsistenciaCab").width='100%';	   
	} else {
	   document.getElementById("listadoActuacionesAsistenciaCab").width='98.43%';
	}
}

function ponerTitulo(){
	var siga ="SIGA";
	var tit ="<siga:Idioma key="gratuita.listadoActuacionesAsistencia.literal.titulo"/>";
	setTitulo(siga, tit);
	var esFichaColegial = document.getElementById("fichaColegial").value;
	var loc = "";
	if (esFichaColegial == "1")
		loc ="<siga:Idioma key="censo.gratuita.asistencias.actuaciones.literal.localizacion"/>";
	else
		loc = "<siga:Idioma key="gratuita.asistencias.actuaciones.localizacion"/>";
	setLocalizacion(loc);
}

function accionNuevo()	{
    document.ActuacionAsistenciaFormEdicion.modo.value = "nuevo";
    resultadoVentanaCondicion = ventaModalGeneral(document.ActuacionAsistenciaFormEdicion.name, "G");
	if (resultadoVentanaCondicion == "MODIFICADO") 
		refrescarLocal();
	else
		refrescarLocal();
}

function accionVolver() {
	document.ActuacionAsistenciaForm.target="mainWorkArea";
	document.ActuacionAsistenciaForm.action = "/SIGA/JGR_Asistencia.do";
	document.ActuacionAsistenciaForm.modo.value= "abrir";
	document.ActuacionAsistenciaForm.submit();
}

function refrescarLocal() {
	document.ActuacionAsistenciaForm.target="_self";
	document.ActuacionAsistenciaForm.modo.value = "abrir";
	document.ActuacionAsistenciaForm.submit();		
}
function selectRow(fila) {
    document.getElementById('trtabla_'+fila).className = 'listaNonEditSelected';
}
	 
	 function consultar(fila) {
		 
		  
		var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
		var anio = document.getElementById("anio_"+fila).value;
		var numero = document.getElementById("numero_"+fila).value;
		var idActuacion = document.getElementById("idActuacion_"+fila).value;
		document.ActuacionAsistenciaFormEdicion.idInstitucion.value = idInstitucion;
		document.ActuacionAsistenciaFormEdicion.anio.value = anio;
		document.ActuacionAsistenciaFormEdicion.numero.value = numero;
		document.ActuacionAsistenciaFormEdicion.idActuacion.value = idActuacion;
		document.ActuacionAsistenciaFormEdicion.modo.value="ver";
		var resultado = ventaModalGeneral(document.ActuacionAsistenciaFormEdicion.name,"G");
		if(resultado)
			refrescarLocal();
		else
			refrescarLocal();
		
		
	 }
	 
	 function editar(fila) {
		 var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
			var anio = document.getElementById("anio_"+fila).value;
			var numero = document.getElementById("numero_"+fila).value;
			var idActuacion = document.getElementById("idActuacion_"+fila).value;
			document.ActuacionAsistenciaFormEdicion.idInstitucion.value = idInstitucion;
			document.ActuacionAsistenciaFormEdicion.anio.value = anio;
			document.ActuacionAsistenciaFormEdicion.numero.value = numero;
			document.ActuacionAsistenciaFormEdicion.idActuacion.value = idActuacion;
			document.ActuacionAsistenciaFormEdicion.modo.value="editar";
	   		var resultado = ventaModalGeneral(document.ActuacionAsistenciaFormEdicion.name,"G");
	   		
	   		if(resultado)
				refrescarLocal();
			else
				refrescarLocal();
	   		//document.ActuacionAsistenciaForm.modo.value="";
	   		// document.ActuacionAsistenciaFormEdicion.modo.value="";
	 }
	 
	 function borrar(fila) {
	   var datos;
	   if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
		   
		   var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
			var anio = document.getElementById("anio_"+fila).value;
			var numero = document.getElementById("numero_"+fila).value;
			var idActuacion = document.getElementById("idActuacion_"+fila).value;
			document.ActuacionAsistenciaFormEdicion.idInstitucion.value = idInstitucion;
			document.ActuacionAsistenciaFormEdicion.anio.value = anio;
			document.ActuacionAsistenciaFormEdicion.numero.value = numero;
			document.ActuacionAsistenciaFormEdicion.idActuacion.value = idActuacion;
			
			var auxTarget = document.ActuacionAsistenciaFormEdicion.target;
		   	document.ActuacionAsistenciaFormEdicion.target="submitArea";
		   	document.ActuacionAsistenciaFormEdicion.modo.value = "Borrar";
		   	document.ActuacionAsistenciaFormEdicion.submit();
		   	document.ActuacionAsistenciaFormEdicion.target=auxTarget;
		   	
	   	
	 	}
	 }




var messageError='${error}';

if (messageError)
	alert(messageError);

</script>
</body>

</html>