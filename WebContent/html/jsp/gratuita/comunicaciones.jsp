<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
<!-- comunicaciones.jsp-->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<!-- TAGLIBS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script type='text/javascript' src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<title><siga:Idioma key="pestana.justiciagratuitaejg.comunicacion"/></title>
	<siga:Titulo titulo="pestana.justiciagratuitaejg.comunicacion" localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body>

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>			

	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td class="titulitosDatos">
				<c:out value="${ComunicacionesForm.anio}"/>
				/
				<c:out value="${ComunicacionesForm.codigoDesignaNumEJG}"/>
				&nbsp;-&nbsp;
				<c:out value="${ComunicacionesForm.solicitante}"/>
			</td>
		</tr>
	</table>

	<html:form action="${path}" target="mainWorkArea">
		<html:hidden styleId = "modo" property="modo"  />
		<html:hidden styleId = "solicitante" property="solicitante" />
		<html:hidden styleId = "codigoDesignaNumEJG" property="codigoDesignaNumEJG" />
		
		<html:hidden styleId = "ejgIdInstitucion" property="ejgIdInstitucion" />
		<html:hidden styleId = "ejgIdTipo" property="ejgIdTipo" />
		<html:hidden styleId = "ejgAnio" property="ejgAnio" />
		<html:hidden styleId = "ejgNumero" property="ejgNumero" />
		
		<html:hidden styleId = "designaIdInstitucion" property="designaIdInstitucion" />
		<html:hidden styleId = "designaAnio" property="designaAnio" />
		<html:hidden styleId = "designaIdTurno" property="designaIdTurno" />
		<html:hidden styleId = "designaNumero" property="designaNumero" />
		<html:hidden styleId = "designaCodigo" property="designaCodigo" />
	</html:form>

	<html:form action="/ENV_DefinirEnvios.do?noReset=true" name="FormDefinirEnvio" type= "com.siga.envios.form.DefinirEnviosForm" method="POST" target="mainWorkArea" style="display:none">
	    <html:hidden styleId = "modo" property = "modo"/>
		<html:hidden styleId = "idEnvio" property = "idEnvio"/>
		<html:hidden styleId = "idTipoEnvio" property = "idTipoEnvio"/>
		<html:hidden styleId = "idIntercambio" property = "idIntercambio"/>
		<html:hidden styleId = "idInstitucion" property = "idInstitucion"/>
		<html:hidden styleId = "datosEnvios" property = "datosEnvios"/>
		<html:hidden styleId = "origen" property = "origen" value ="${path}"/>
	</html:form>

	<html:form action="/ENV_EntradaEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">		
	    <html:hidden styleId = "modo" 			property = "modo"/>
	    <html:hidden styleId = "idEnvio" 		property = "idEnvio"/>
	    <html:hidden styleId = "idInstitucion" 	property = "idInstitucion"/>
	    <html:hidden styleId = "anioEJGSel" 	property = "anioEJGSel" value="${ComunicacionesForm.ejgAnio}" />
	    <html:hidden styleId = "idTipoEJGSel" 	property = "idTipoEJGSel" value="${ComunicacionesForm.ejgIdTipo}"/>
	    <html:hidden styleId = "numeroEJGSel" 	property = "numeroEJGSel" value="${ComunicacionesForm.ejgNumero}"/>
	    
	    <html:hidden styleId = "anioDesignaSel" property = "anioDesignaSel" value="${ComunicacionesForm.designaAnio}"/>
	    <html:hidden styleId = "idTurnoDesignaSel"	property = "idTurnoDesignaSel" value="${ComunicacionesForm.designaIdTurno}"/>
	    <html:hidden styleId = "numeroDesignaSel" property = "numeroDesignaSel" value="${ComunicacionesForm.designaNumero}"/>
	    
	    <html:hidden styleId = "origen" property="origen" value ="${path}"/>	    
	</html:form>

	<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
		<html:hidden styleId = "modo" property="modo" value="editar"/>
		<html:hidden styleId = "idInstitucion" property="idInstitucion" value="${ComunicacionesForm.ejgIdInstitucion}"/>
		<html:hidden styleId = "anio" property="anio" value="${ComunicacionesForm.ejgAnio}"/>
		<html:hidden styleId = "idTipoEJG" property="idTipoEJG" value="${ComunicacionesForm.ejgIdTipo}"/>
		<html:hidden styleId = "numero" property="numero" value="${ComunicacionesForm.ejgNumero}"/>
		
		<html:hidden styleId = "origen" property="origen" value ="${path}"/>
	</html:form>

	<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
		<html:hidden styleId = "modo" property = "modo"  value="editar"/>
		<html:hidden styleId = "idInstitucion" property="idInstitucion" value="${ComunicacionesForm.designaIdInstitucion}"/>
		<html:hidden styleId = "anio" property="anio" value="${ComunicacionesForm.designaAnio}"/>
		<html:hidden styleId = "idTurno" property="idTurno" value="${ComunicacionesForm.designaIdTurno}"/>
		<html:hidden styleId = "numero" property="numero" value="${ComunicacionesForm.designaNumero}"/>
		<html:hidden styleId = "origen" property="origen" value ="${path}"/>
	</html:form>

	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.definir.literal.titulo"/>
			</td>
		</tr>
	</table> 

	<siga:Table	
		name="comunicacionesSalida"
		border="1"
		columnNames="envios.definir.literal.identificador, 
					envios.definir.literal.nombre,
				  	envios.definir.literal.fechacreacion,
				  	envios.definir.literal.fechaprogramada,
				  	envios.definir.literal.estado,
				  	envios.definir.literal.tipoenvio,"
		columnSizes="10,28,10,10,12,12,18"
		fixedHeight="50%">

		<c:choose>
			<c:when test="${empty comunicacionesSalida}">
				<tr class="notFound">
		   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>				
			</c:when>
			
			<c:otherwise>
					<c:forEach items="${comunicacionesSalida}" var="comunicacionSalida" varStatus="status">
						<siga:FilaConIconosExtExt fila='${status.count}' botones="${comunicacionSalida.botones}" elementos="${comunicacionSalida.elementosFila}" nombreTablaPadre="comunicacionesSalida"  clase="listaNonEdit">
							<td>
								<input type="hidden" id="idEnvio_${status.count}" value = "${comunicacionSalida.idEnvio}"/> 
								<input type="hidden" id="idTipoEnvio_${status.count}" value = "${comunicacionSalida.idTipoEnvio}"/>
								<input type="hidden" id="idIntercambio_${status.count}" value = "${comunicacionSalida.idIntercambio}"/>
								<c:out value="${comunicacionSalida.idEnvio}"/>
							</td>
							<td><c:out value="${comunicacionSalida.nombre}"/></td>
							<td align="center"><c:out value="${comunicacionSalida.fecha}"/></td>
							<td align="center"><c:out value="${comunicacionSalida.fechaProgramada}"/></td>
							<td ><siga:Idioma key="${comunicacionSalida.estado}" /></td>
							<td><siga:Idioma key="${comunicacionSalida.tipoEnvio}" /></td>
						</siga:FilaConIconosExtExt>
					</c:forEach>
			</c:otherwise>
		</c:choose>	    
	</siga:Table>

	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.bandejaentrada.titulo"/>
			</td>
		</tr>
	</table> 
	
	<siga:Table 
		name="comunicacionesEntrada"
		border="1"
		columnNames='general.etiqueta.fecha,
					comunicaciones.etiqueta.remitente,
					comunicaciones.etiqueta.asunto,
					comunicaciones.etiqueta.respuesta,
					comunicaciones.etiqueta.estado,'
		columnSizes="10,20,35,10,13,12">
	   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty comunicacionesEntrada}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			</c:when>
			
			<c:otherwise>
	  			<c:forEach items="${comunicacionesEntrada}"	var="comunicacionEntrada" varStatus="status">
	  				<siga:FilaConIconosExtExt fila='${status.count}'
  						botones="${comunicacionEntrada.botonesFila}"
  						elementos="${comunicacionEntrada.elementosFila}" 
  						pintarEspacio="no"
  						visibleEdicion = "no"
  						visibleBorrado = "no"
  						nombreTablaPadre="comunicacionesEntrada"
  						clase="listaNonEdit">

						<td align="center">
		  					<input type="hidden" id ="idEntradaEnvio_${status.count}" value ="${comunicacionEntrada.idEnvio}"/>	
		  					<input type="hidden" id ="idEntradaEstado_${status.count}" value ="${comunicacionEntrada.idEstado}"/>
		  					<input type="hidden" id ="anio_${status.count}" value ="${comunicacionEntrada.anioDesignaSel}"/>
		  					<input type="hidden" id ="numero_${status.count}" value ="${comunicacionEntrada.numeroDesignaSel}"/>
		  					<input type="hidden" id ="idTurno_${status.count}" value ="${comunicacionEntrada.idTurnoDesignaSel}"/>
							<c:out value="${comunicacionEntrada.fechaPeticion}"/>
						</td>
						
						<td align="center">
							<c:choose>
								<c:when test="${comunicacionEntrada.descripcionRemitente != null && comunicacionEntrada.descripcionRemitente != ''}">
									<c:out value="${comunicacionEntrada.descripcionRemitente}"/>
								</c:when>
								<c:otherwise>
									<bean:message key="comunicaciones.aviso.remitente.pendiente" />
								</c:otherwise>
							</c:choose>	
						</td>
						
						<td align="center">
							<c:out value="${comunicacionEntrada.asunto}"/>
						</td>
							
						<td align="center">
							<c:choose>
								<c:when test="${comunicacionEntrada.fechaRespuesta != null && comunicacionEntrada.fechaRespuesta != ''}">
									<c:out value="${comunicacionEntrada.fechaRespuesta}" />
								</c:when>
								
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>	
						</td>
						
						<td align="center">
							<c:out value="${comunicacionEntrada.descripcionEstado}"/>
						</td>
					</siga:FilaConIconosExtExt>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="V" modo="${ComunicacionesForm.modo}" clase="botonesDetalle" />

	<bean:define id="busquedaVolver" value="S"/>

	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
</body>	

	
<script type="text/javascript">
				
	function editar(fila, id) {
		if (typeof id == 'undefined')
			id='comunicacionesSalida';
			   
	   	if (id=='comunicacionesSalida') {
		   	document.forms['FormDefinirEnvio'].idInstitucion.value = document.ComunicacionesForm.ejgIdInstitucion.value;
		   	document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
		   	document.forms['FormDefinirEnvio'].idTipoEnvio.value = document.getElementById("idTipoEnvio_"+fila).value;
		   	document.forms['FormDefinirEnvio'].idIntercambio.value = document.getElementById("idIntercambio_"+fila).value;
		   	literalDatosEnvio = document.forms['ComunicacionesForm'].ejgIdInstitucion.value+","+document.forms['ComunicacionesForm'].ejgAnio.value+","+
	   		document.forms['ComunicacionesForm'].ejgIdTipo.value+","+document.forms['ComunicacionesForm'].ejgNumero.value+","+
	   		document.forms['ComunicacionesForm'].designaIdInstitucion.value+","+document.forms['ComunicacionesForm'].designaAnio.value+","+
	   		document.forms['ComunicacionesForm'].designaIdTurno.value+","+document.forms['ComunicacionesForm'].designaNumero.value;
	   		document.forms['FormDefinirEnvio'].datosEnvios.value = literalDatosEnvio;
	   		document.forms['FormDefinirEnvio'].modo.value = "editarComunicacion";
	   		document.forms['FormDefinirEnvio'].submit();
		   
	   	} else if(id=='comunicacionesEntrada'){
	   	}
	 }
	
	function consultar(fila, id) {
		if (typeof id == 'undefined')
			id='comunicacionesSalida';	
	   
	   	if (id=='comunicacionesSalida') {
		   	document.forms['FormDefinirEnvio'].idInstitucion.value = document.ComunicacionesForm.ejgIdInstitucion.value;
		   	document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
		   	document.forms['FormDefinirEnvio'].idTipoEnvio.value = document.getElementById("idTipoEnvio_"+fila).value;
		   	document.forms['FormDefinirEnvio'].idIntercambio.value = document.getElementById("idIntercambio_"+fila).value;
		   	literalDatosEnvio = document.forms['ComunicacionesForm'].ejgIdInstitucion.value+","+document.forms['ComunicacionesForm'].ejgAnio.value+","+
	   		document.forms['ComunicacionesForm'].ejgIdTipo.value+","+document.forms['ComunicacionesForm'].ejgNumero.value+","+
	   		document.forms['ComunicacionesForm'].designaIdInstitucion.value+","+document.forms['ComunicacionesForm'].designaAnio.value+","+
	   		document.forms['ComunicacionesForm'].designaIdTurno.value+","+document.forms['ComunicacionesForm'].designaNumero.value;
	   		document.forms['FormDefinirEnvio'].datosEnvios.value = literalDatosEnvio;
	   		document.forms['FormDefinirEnvio'].modo.value = "consultarComunicacion";
	   		document.forms['FormDefinirEnvio'].submit();
		   
	   } else if(id=='comunicacionesEntrada') {
		   if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
		   
			document.forms['EntradaEnviosForm'].idEnvio.value = document.getElementById("idEntradaEnvio_"+fila).value;
			document.forms['EntradaEnviosForm'].modo.value = 'consultarComunicacion';
			document.forms['EntradaEnviosForm'].submit();
	   	}
	 }
	
	//Ponemos las acciones de los botones de la bandeja de salida
	function enviar(fila,id) {
		sub();
		subicono('iconoboton_enviar'+fila);
		selectRow(fila,id);
		if (typeof id == 'undefined')
			id='comunicacionesSalida';
		
		if (id=='comunicacionesSalida') {
			if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
			document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
			document.forms['FormDefinirEnvio'].idTipoEnvio.value = document.getElementById("idTipoEnvio_"+fila).value;
			document.forms['FormDefinirEnvio'].target="submitArea";	   	
			document.forms['FormDefinirEnvio'].modo.value='procesarEnvio';
			document.forms['FormDefinirEnvio'].submit();
			document.forms['FormDefinirEnvio'].target="mainWorkArea";
		}		
	}
	
	function refrescarLocal() {
		if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!=''){
			document.forms['DefinirEJGForm'].submit();
		}else{
			document.forms['MaestroDesignasForm'].submit();
		}		
	}

	function borrar(fila, id) {
		selectRow(fila,id);
		if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')) {
		
			if (typeof id == 'undefined')
				id='comunicacionesSalida';			
		   
		   	if (id=='comunicacionesSalida') {
			   if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
					document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
				else
					document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
			   	document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
			   	document.forms['FormDefinirEnvio'].idTipoEnvio.value = document.getElementById("idTipoEnvio_"+fila).value;
			   	document.forms['FormDefinirEnvio'].idIntercambio.value = document.getElementById("idIntercambio_"+fila).value;
		   		document.forms['FormDefinirEnvio'].target="submitArea";
		   		document.forms['FormDefinirEnvio'].modo.value = "borrarComunicacion";
		   		document.forms['FormDefinirEnvio'].submit();
		   		document.forms['FormDefinirEnvio'].target="mainWorkArea";
			   
		   } else if(id=='comunicacionesEntrada'){
		   }
		}
	 }

	function download(fila,id) {
		selectRow(fila,id);
		if (typeof id == 'undefined')
			id='comunicacionesSalida';
		
		if(id=='comunicacionesSalida'){
			var auxEnv = 'oculto' + fila + '_3';
			var pathEnv = document.getElementById(auxEnv);			          		
		   	
		   	// var urlFTP='pathFTP %>' + pathEnv.value;
		   	window.open(urlFTP,'FTPdownload','height=600,width=800,status=yes,toolbar=yes,menubar=no,location=no');
		   	
		} else {
			if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
			document.forms['EntradaEnviosForm'].idEnvio.value = document.getElementById("idEntradaEnvio_"+fila).value;
			document.forms['EntradaEnviosForm'].modo.value = 'descargar';
			document.forms['EntradaEnviosForm'].submit();			
		}	   	
	}
	
	function descargaLog(fila,id) {
		selectRow(fila,id);
		if (typeof id == 'undefined')
			id='comunicacionesSalida';		
		
		if(id=='comunicacionesSalida'){
			if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
		   	document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
			document.forms['FormDefinirEnvio'].target="submitArea";
	   		document.forms['FormDefinirEnvio'].modo.value = "descargarLogComunicacion";
	   		document.forms['FormDefinirEnvio'].submit();
	   		document.forms['FormDefinirEnvio'].target="mainWorkArea";
	   		
		} else if(id=='comunicacionesEntrada') {		
		}
	}

	function comunicar(fila,id) {		
		selectRow(fila,id);
		if (typeof id == 'undefined')
			id='comunicacionesSalida';
		
		if (id=='comunicacionesSalida') {
			
		} else {
			if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
			document.forms['EntradaEnviosForm'].idEnvio.value = document.getElementById("idEntradaEnvio_"+fila).value;
			document.forms['EntradaEnviosForm'].target="submitArea";
			document.forms['EntradaEnviosForm'].modo.value="comunicar";
			document.forms['EntradaEnviosForm'].submit();
		}
	}
	
	function procesar(){	
		selectRow(fila,id);
		
		if (typeof id == 'undefined')
			id='comunicacionesSalida';
		
		if (id=='comunicacionesSalida') {
			
		} else {
			if(document.forms['ComunicacionesForm'].ejgIdInstitucion.value!='')
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].ejgIdInstitucion.value;
			else
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['ComunicacionesForm'].designaIdInstitucion.value;
			document.forms['EntradaEnviosForm'].idEnvio.value = document.getElementById("idEntradaEnvio_"+fila).value;
			document.forms['EntradaEnviosForm'].target="submitArea";
			document.forms['EntradaEnviosForm'].modo.value="procesar";
			document.forms['EntradaEnviosForm'].submit();			
		}				
	}				
</script>
</html>