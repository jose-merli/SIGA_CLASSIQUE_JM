<!DOCTYPE html>
<html>
<head>
<!-- listadoActuacionesAsistencia.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.form.*"%>
<%@ page import="java.util.List"%>
<%@ page import="com.siga.gratuita.form.ActuacionAsistenciaForm"%>




<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	

	String anio ="", numero ="", idInstitucion="";
	List <ActuacionAsistenciaForm> listadoActuaciones =(List <ActuacionAsistenciaForm>)request.getAttribute("actuacionesAsistenciaList"); 

	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if (sEsFichaColegial != null
		&& (sEsFichaColegial.equalsIgnoreCase("1") || sEsFichaColegial.equalsIgnoreCase("true"))) {
		esFichaColegial = true;
	}
	if(listadoActuaciones != null && listadoActuaciones.size() >0){
		 anio = listadoActuaciones.get(0).getAnio();
		 numero =listadoActuaciones.get(0).getNumero();
		 idInstitucion =listadoActuaciones.get(0).getIdInstitucion();
	}

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body onload="ponerTitulo()">
	<bean:define id="asistencia" name="asistencia" scope="request" type="com.siga.gratuita.form.AsistenciaForm" />
	<bean:define id="error" name="error" scope="request" />
	<bean:define id="actuacionesAsistenciaList" name="actuacionesAsistenciaList" type="java.util.Collection" scope="request" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
		
	<html:form action="${path}" style="display: none">
		<html:hidden property="modo" value="abrir" />
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
	</html:form>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td class="titulitosDatos">
				<c:out value="${asistencia.descripcion}" />
			</td>
		</tr>
	</table>
	
	<siga:Table 		   
	   name="listadoActuacionesAsistencia"
	   border="2"
	   columnNames="gratuita.listadoActuacionesAsistencia.literal.nactuacion,
	   				gratuita.listadoActuacionesAsistencia.literal.fecha,
	   				general.literal.desde,
	   				informes.cartaOficio.numAsuntoLista,
	   				gratuita.mantActuacion.literal.tipoActuacion,
	   				gratuita.listadoActuacionesAsistencia.literal.justificado,
	   				gratuita.actuacionesDesigna.literal.validada,
	   				gratuita.procedimientos.literal.anulada,
	   				gratuita.actuacionesDesigna.literal.facturacion,"
	   columnSizes="6,8,8,12,14,8,5,5,22,12"
	   modal="P">	
	
		<c:choose>
			<c:when test="${empty actuacionesAsistenciaList}">
		 		<tr class="notFound">
					<td class="titulitos">
						<siga:Idioma key="messages.noRecordFound"/>
					</td>
				</tr>			
			</c:when>
			
			<c:otherwise>
				<c:forEach items="${actuacionesAsistenciaList}" var="actuacionAsistencia" varStatus="status">
					<%
						 	FilaExtElement[] elems = null;
						 	elems = new FilaExtElement[1];
					%>
					<c:if test="${not empty actuacionAsistencia.idFacturacion}">
					<c:if test="${not empty actuacionAsistencia.facturacionCerrada}">
						<%
					 		elems[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", "movimientosVarios.icono.alt", SIGAConstants.ACCESS_FULL);
				 		%>
				 	</c:if>
				 	</c:if>
			 
					<siga:FilaConIconos fila='${status.count}' id="trtabla_${status.count}" botones="${actuacionAsistencia.botones}" clase="listaNonEdit" elementos="<%=elems%>">							
						<td align="center">
							<input type="hidden" id="idInstitucion_${status.count}" value="${actuacionAsistencia.idInstitucion}">
							<input type="hidden" id="anio_${status.count}" value="${actuacionAsistencia.anio}">
							<input type="hidden" id="numero_${status.count}" value="${actuacionAsistencia.numero}">
							<input type="hidden" id="idActuacion_${status.count}" value="${actuacionAsistencia.idActuacion}">
							<c:out value="${actuacionAsistencia.idActuacion}" />
						</td>
						<td align="center"> <c:out value="${actuacionAsistencia.fecha}" /> </td>
						<td align="center"> <c:out value="${actuacionAsistencia.fechaHoraAsistencia}" /> </td>
						<td align="center"> &nbsp;<c:out value="${actuacionAsistencia.numeroAsunto}" /> </td>
						<td align="center"> &nbsp;<c:out value="${actuacionAsistencia.descripcionActuacion}" /> </td>							
						<td align="center">
							<c:choose>
								<c:when test="${actuacionAsistencia.fechaJustificacion!=''}">
									<c:out value="${actuacionAsistencia.fechaJustificacion}" />
								</c:when>
								<c:otherwise>
			  						&nbsp;
			  					</c:otherwise>
							</c:choose>
						</td>
						<td align="center">
							<c:choose>
								<c:when test="${actuacionAsistencia.validada=='1'}">
									<siga:Idioma key="general.yes" />
								</c:when>
								<c:otherwise>
									<siga:Idioma key="general.no" />
								</c:otherwise>
							</c:choose>
						</td>
						<td align="center">
							<c:choose>
								<c:when test="${actuacionAsistencia.anulacion=='1'}">
									<siga:Idioma key="general.yes" />
								</c:when>
								<c:otherwise>
									<siga:Idioma key="general.no" />
								</c:otherwise>
							</c:choose>
						</td>
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
	</siga:Table>

	<c:if test="${asistencia.botonesDetalle!=''}">
		<siga:ConjBotonesAccion botones="${asistencia.botonesDetalle}"
			clase="botonesDetalle" />
	</c:if>

	<html:form action="${path}" method="POST" name="ActuacionAsistenciaFormEdicion" styleId="ActuacionAsistenciaFormEdicion" type="com.siga.gratuita.form.ActuacionAsistenciaForm">
		<html:hidden property="modo" value="" name="ActuacionAsistenciaFormEdicion" />
		<html:hidden property="idInstitucion" styleId="idInstitucion" name="ActuacionAsistenciaFormEdicion" value="${asistencia.idInstitucion }" />
		<html:hidden property="anio" name="ActuacionAsistenciaFormEdicion" styleId="anio" value="${asistencia.anio}" />
		<html:hidden property="numero" name="ActuacionAsistenciaFormEdicion" styleId="numero" value="${asistencia.numero}" />
		<html:hidden property="fichaColegial" styleId="fichaColegial" name="ActuacionAsistenciaFormEdicion" value="${asistencia.fichaColegial}" />
		<html:hidden property="idTipoAsistencia" styleId="idTipoAsistencia" name="ActuacionAsistenciaFormEdicion" value="${asistencia.idTipoAsistenciaColegio}" />
		<html:hidden property="idActuacion" styleId="idActuacion" name="ActuacionAsistenciaFormEdicion" />
		<input type="hidden" name="actionModal" />
		
	</html:form>
	<html:form action="/JGR_GestionSolicitudesAceptadasCentralita.do"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idSolicitudAceptada"/>
	</html:form>
	
	<html:form action="/JGR_MovimientosVariosLetrado?noReset=true" method="POST" target="submitArea" styleId="movimientosVarios">
			<html:hidden name="MantenimientoMovimientosForm" property = "modo" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property = "actionModal" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property="checkHistorico" value=""/>
			<html:hidden name="MantenimientoMovimientosForm" property="idPersona" value=""/>
			<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<input type="hidden" name="botonBuscarPulsado" value="">
			<input type="hidden" name="mostrarMovimientos" value="">
			
			<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden property = "anio" value="<%=anio%>" />	
			<html:hidden property = "numero" value="<%=numero%>" />
			<html:hidden property = "nactuacion"  />
			<html:hidden property = "origen" value="ACTUACIONESASISTENCIAS"  />
			
	</html:form>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	
	<script type="text/javascript">
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
			document.ActuacionAsistenciaFormEdicion.submit();
		}

		function accionVolver() {
			if(document.forms[0].jsonVolver && document.forms[0].jsonVolver.value!=''){
				jSonVolverValue = document.forms[0].jsonVolver.value;
				jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
				var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
				nombreFormulario = jSonVolverObject.nombreformulario; 
				if(nombreFormulario == 'SolicitudAceptadaCentralitaForm'){
					document.forms['SolicitudAceptadaCentralitaForm'].idSolicitudAceptada.value =  jSonVolverObject.idsolicitudaceptada;
					document.forms['SolicitudAceptadaCentralitaForm'].idInstitucion.value = jSonVolverObject.idinstitucion;
					document.forms['SolicitudAceptadaCentralitaForm'].modo.value="consultarSolicitudAceptada";
					document.forms['SolicitudAceptadaCentralitaForm'].target = "mainWorkArea";
					document.forms['SolicitudAceptadaCentralitaForm'].submit();
				}
			}else{
				document.ActuacionAsistenciaForm.target="mainWorkArea";
				document.ActuacionAsistenciaForm.action = "/SIGA/JGR_Asistencia.do";
				document.ActuacionAsistenciaForm.modo.value= "abrir";
				document.ActuacionAsistenciaForm.submit();
			}
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
			document.ActuacionAsistenciaFormEdicion.submit();
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
			document.ActuacionAsistenciaFormEdicion.submit();
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

		//Funcion asociada a boton nuevo
		function anticiparImporte(fila, id) 
		{	
			document.movimientosVarios.modo.value="nuevo";
			document.movimientosVarios.target="submitArea";
			document.movimientosVarios.nactuacion.value=jQuery("#idActuacion_"+fila).val(); 
			var resultado=ventaModalGeneral(document.movimientosVarios.name,"M");
			//if (resultado=="MODIFICADO")buscar2();
		}
		
		var messageError='${error}';
		if (messageError)
			alert(messageError);

	</script>
</body>
</html>