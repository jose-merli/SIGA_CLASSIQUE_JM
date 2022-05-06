<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
<!-- listadoIntercambiosJG.jsp-->

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
	<title><siga:Idioma key="pestana.justiciagratuitaejg.intercambiosJG"/></title>
	<siga:Titulo  titulo="pestana.justiciagratuitaejg.intercambiosJG"  localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body>
	<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>			
<div>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo"  class="titulitosDatos" style="font:normal" >
				<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<c:out value="${DefinirEJGForm.anio}"/>/<c:out value="${DefinirEJGForm.numEJG}"/>
				&nbsp;-&nbsp;
				<c:out value="${DefinirEJGForm.solicitante}"/>
			</td>
		</tr>
	</table>
</div>
	<html:form action="${path}" target="mainWorkArea">
		<html:hidden styleId = "modo" property="modo"  />
		<html:hidden styleId = "solicitante" property="solicitante" />
		<html:hidden styleId = "idInstitucion" property="idInstitucion" />
		<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
		<html:hidden styleId = "anio" property="anio" />
		<html:hidden styleId = "numero" property="numero" />
		<html:hidden styleId = "origen" property="origen" value ="${path}"/>
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
	</html:form>

	<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
		<html:hidden styleId = "modo" property="modo" value="editar"/>
		<html:hidden styleId = "idInstitucion" property="idInstitucion" value="${DefinirEJGForm.idInstitucion}"/>
		<html:hidden styleId = "anio" property="anio" value="${DefinirEJGForm.anio}"/>
		<html:hidden styleId = "idTipoEJG" property="idTipoEJG" value="${DefinirEJGForm.idTipoEJG}"/>
		<html:hidden styleId = "numero" property="numero" value="${DefinirEJGForm.numero}"/>

		<html:hidden styleId = "origen" property="origen" value ="/JGR_EJG"/>
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
	</html:form>

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				Listado de envío de alta de expedientes
			</td>
		</tr>
	</table> 

	<c:set var="columnNames" value="Intercambio,Fecha Envio, Estado, Fecha Respuesta,Respuesta" />
	<c:set var="columnSizes" value="10,10,10,10,40" />
	
	<siga:Table	
			name="listadoAltaExpediente"
			border="1"
			columnNames="${columnNames}"
			columnSizes="${columnSizes}"
			fixedHeight="50%">

		<c:choose>
			
			<c:when test="${empty listadoAltaExpediente}">
				<tr class="notFound">
		   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>				
			</c:when>
			<c:otherwise>
					<c:forEach items="${listadoAltaExpediente}" var="intercambioAltaExpediente" varStatus="status">
						
							<siga:FilaConIconosExtExt fila='${status.count}' botones = "" pintarEspacio="no" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false"   nombreTablaPadre="comunicacionesSalida"  clase="listaNonEdit">
								<td><c:out value="${intercambioAltaExpediente.descripcion}"/></td>
								<td align="center"><c:out value="${intercambioAltaExpediente.fechaEnvioTxt}"/></td>
								<td ><c:out value="${intercambioAltaExpediente.descEstado}"/></td>
								<td align="center"><c:out value="${intercambioAltaExpediente.fechaRespuestaTxt}"/></td>
								<td ><siga:Idioma key="${intercambioAltaExpediente.respuesta}" /></td>
							
							</siga:FilaConIconosExtExt>
					

					</c:forEach>
				
			</c:otherwise>
		</c:choose>
	</siga:Table>	    
	

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				Listado de envío de documentación
			</td>
		</tr>
	</table> 
	
	<siga:Table 
		name="listadoEnvioDocumento"
		border="1"
		columnNames='Intercambio,Fecha Envio, Estado,Fecha Respuesta,Respuesta'
		columnSizes="10,10,10,10,40">
	   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty listadoEnvioDocumento}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			</c:when>
			
			<c:otherwise>
	  			<c:forEach items="${listadoEnvioDocumento}" var="intercambioDocumento" varStatus="status">
						
						<siga:FilaConIconosExtExt fila='${status.count}' botones = "" pintarEspacio="no" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false"   nombreTablaPadre="listadoAltaExpediente"  clase="listaNonEdit">
							<td><c:out value="${intercambioDocumento.descripcion}"/></td>
							<td align="center"><c:out value="${intercambioDocumento.fechaEnvioTxt}"/></td>
							<td ><c:out value="${intercambioDocumento.descEstado}"/></td>
							<td align="center"><c:out value="${intercambioDocumento.fechaRespuestaTxt}"/></td>
							<td ><siga:Idioma key="${intercambioDocumento.respuesta}" /></td>
							
						</siga:FilaConIconosExtExt>
					

					</c:forEach>
			</c:otherwise>
		</c:choose>
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="V" modo="${DefinirEJGForm.modo}" clase="botonesDetalle" />

	<bean:define id="busquedaVolver" value="S"/>

	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
</body>	

	
<script type="text/javascript">
	
	function reenviar(fila) {
		if(!confirm('<siga:Idioma key="envios.confirmar.reenviar"/>')){
			return false;
		}
		
		sub();
		subicono('iconoboton_reeenviar'+fila);
		document.forms['FormDefinirEnvio'].target="mainWorkArea";
		document.forms['FormDefinirEnvio'].idEnvio.value = document.getElementById("idEnvio_"+fila).value;
		document.forms['FormDefinirEnvio'].tipoEnvio.value = document.getElementById("idTipoEnvio_"+fila).value;
		document.forms['FormDefinirEnvio'].estado.value = document.getElementById("idEstadoEnvio_"+fila).value;
		
		document.forms['FormDefinirEnvio'].modo.value='reenviar';
		document.forms['FormDefinirEnvio'].submit();
		alert('<siga:Idioma key="envios.aviso.reenviar.ok"/>');
		
	}
	
	function refrescarLocal() {
		if(document.forms['DefinirEJGForm'].ejgIdInstitucion.value!=''){
			document.forms['DefinirEJGForm'].submit();
		}else{
			document.forms['MaestroDesignasForm'].submit();
		}		
	}
	
	function descargaLog(fila,id) {
		selectRow(fila,id);
		if (typeof id == 'undefined')
			id='comunicacionesSalida';		
		
		if(id=='comunicacionesSalida'){
			if(document.forms['DefinirEJGForm'].ejgIdInstitucion.value!='')
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['DefinirEJGForm'].ejgIdInstitucion.value;
			else
				document.forms['FormDefinirEnvio'].idInstitucion.value = document.forms['DefinirEJGForm'].designaIdInstitucion.value;
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
			if(document.forms['DefinirEJGForm'].ejgIdInstitucion.value!='')
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['DefinirEJGForm'].ejgIdInstitucion.value;
			else
				document.forms['EntradaEnviosForm'].idInstitucion.value = document.forms['DefinirEJGForm'].designaIdInstitucion.value;
			document.forms['EntradaEnviosForm'].idEnvio.value = document.getElementById("idEntradaEnvio_"+fila).value;
			document.forms['EntradaEnviosForm'].target="submitArea";
			document.forms['EntradaEnviosForm'].modo.value="comunicar";
			document.forms['EntradaEnviosForm'].submit();
		}
	}
	
	
	
</script>
</html>