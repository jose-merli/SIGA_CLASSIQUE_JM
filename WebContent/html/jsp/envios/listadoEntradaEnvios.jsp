<!DOCTYPE html>
<html>
<head>
<!--- listadoEntradaEnvios.jsp -->
<!-- 
	 VERSIONES:
	 Carlos Ruano Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 	prefix="html"%>
<%@ taglib uri = "c.tld" 			prefix="c"%>

<!-- JSP -->




<!-- HEAD -->

	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="envios.bandejaentrada.titulo"	localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body class="tablaCentralCampos">

	<bean:define id="listEntradaEnviosForm" name="listEntradaEnviosForm"  scope="request"/>

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
		 de cabeceras fijas -->
		 
	<html:form action="/ENV_EntradaEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">		
	    <html:hidden styleId = "modo" 			property = "modo"/>
	    <html:hidden styleId = "idEnvio" 		property = "idEnvio"/>
	    <html:hidden styleId = "idInstitucion" 	property = "idInstitucion"/>
	    <html:hidden styleId = "idEstado" 		property = "idEstado"/>
	    <html:hidden styleId = "anioDesignaSel" 		property = "anioDesignaSel"/>
	    <html:hidden styleId = "idTurnoDesignaSel" 		property = "idTurnoDesignaSel"/>
	    <html:hidden styleId = "numeroDesignaSel" 		property = "numeroDesignaSel"/>
	    
		<input type="hidden" id="actionModal"  name="actionModal" value="">
	</html:form>

		<siga:Table 
	   	      name="tablaDatos"
	   		  border="1"
	   		  columnNames='general.etiqueta.fecha,
	   		  comunicaciones.etiqueta.remitente,
	   		  comunicaciones.etiqueta.asunto,
	   		  comunicaciones.etiqueta.respuesta,
	   		  comunicaciones.etiqueta.estado,'
	   		  columnSizes="10,20,35,10,13,12">
	   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty listEntradaEnviosForm}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
			</c:when>
			
			<c:otherwise>
	  			<c:forEach items="${listEntradaEnviosForm}"	var="listEntradaEnviosForm" varStatus="status">
	  				<siga:FilaConIconos fila='${status.count}'
	  						botones="${listEntradaEnviosForm.botonesFila}"
	  						elementos="${listEntradaEnviosForm.elementosFila}" 
	  						pintarEspacio="no"
	  						visibleEdicion = "no"
	  						visibleBorrado = "no"
	  						clase="listaNonEdit" >

						<td align="center">
							<input type="hidden" id ="idEnvio_${status.count}" value ="${listEntradaEnviosForm.idEnvio}"/>	
		  					<input type="hidden" id ="idInstitucion_${status.count}" value ="${listEntradaEnviosForm.idInstitucion}"/>
		  					<input type="hidden" id ="idEstado_${status.count}" value ="${listEntradaEnviosForm.idEstado}"/>
		  					<input type="hidden" id ="anio_${status.count}" value ="${listEntradaEnviosForm.anioDesignaSel}"/>
		  					<input type="hidden" id ="numero_${status.count}" value ="${listEntradaEnviosForm.numeroDesignaSel}"/>
		  					<input type="hidden" id ="idTurno_${status.count}" value ="${listEntradaEnviosForm.idTurnoDesignaSel}"/>
							<c:out value="${listEntradaEnviosForm.fechaPeticion}"/>
						</td>
						<td align="center">
							<c:choose>
								<c:when test="${listEntradaEnviosForm.descripcionRemitente != null && listEntradaEnviosForm.descripcionRemitente != ''}">
									<c:out value="${listEntradaEnviosForm.descripcionRemitente}"/>
								</c:when>
								<c:otherwise>
									<bean:message	key="comunicaciones.aviso.remitente.pendiente" />
								</c:otherwise>
							</c:choose>	
						</td>
						<td align="center">
							<c:out value="${listEntradaEnviosForm.asunto}"/>
						</td>
							
						<td align="center">
							<c:choose>
								<c:when test="${listEntradaEnviosForm.fechaRespuesta != null && listEntradaEnviosForm.fechaRespuesta != ''}">
									<c:out value="${listEntradaEnviosForm.fechaRespuesta}" />
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>	
						</td>
						<td align="center">
							<c:out value="${listEntradaEnviosForm.descripcionEstado}"/>
						</td>
					</siga:FilaConIconos>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<!-- FIN: ZONA DE REGISTROS -->
		</siga:Table>
		
	<!-- formulario para consultar/editar información del envío relacionado -->
	<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
	    <html:hidden styleId = "modo" 			property = "modo"/>
		<html:hidden styleId = "hiddenFrame"  	property = "hiddenFrame"	value = "1"/>
		<html:hidden styleId = "idEnvio"		property = "idEnvio" />
		<html:hidden styleId = "idTipoEnvio"  	property = "idTipoEnvio"/>
		<html:hidden styleId = "datosInforme"  	property = "datosInforme" />
		<html:hidden styleId = "idEstadoEnvio" 	property = "idEstado"/>
		<input type="hidden" id="actionModal"  name="actionModal" value="">
	</html:form>		

			<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		
		<script language="JavaScript">		
			function refrescarLocal(){		
				parent.buscar();			
			}

			function consultar(fila){
				var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
				var idEnvio = document.getElementById("idEnvio_"+fila).value;
				var idEstado = document.getElementById("idEstado_"+fila).value;
				document.forms['EntradaEnviosForm'].idInstitucion.value = idInstitucion;
				document.forms['EntradaEnviosForm'].idEnvio.value = idEnvio;
				document.forms['EntradaEnviosForm'].idEstado.value = idEstado;
				document.forms['EntradaEnviosForm'].modo.value = 'ver';
				document.forms['EntradaEnviosForm'].submit();
			}		

			function comunicar(fila){
				var idEnvio = document.getElementById("idEnvio_"+fila).value;
				var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
				var anio = document.getElementById("anio_"+fila).value;
				var idTurno = document.getElementById("idTurno_"+fila).value;
				var numero = document.getElementById("numero_"+fila).value;
				document.forms['EntradaEnviosForm'].idEnvio.value = idEnvio;
				document.forms['EntradaEnviosForm'].idInstitucion.value=idInstitucion;
				document.forms['EntradaEnviosForm'].anioDesignaSel.value = anio;
				document.forms['EntradaEnviosForm'].idTurnoDesignaSel.value = idTurno;
				document.forms['EntradaEnviosForm'].numeroDesignaSel.value = numero;
				document.forms['EntradaEnviosForm'].target="submitArea";	
				document.forms['EntradaEnviosForm'].modo.value='comunicar';
				document.forms['EntradaEnviosForm'].submit();
			
			}
			
			

			function download(fila){
				var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
				var idEnvio = document.getElementById("idEnvio_"+fila).value;
				document.forms['EntradaEnviosForm'].idInstitucion.value = idInstitucion;
				document.forms['EntradaEnviosForm'].idEnvio.value = idEnvio;
				document.forms['EntradaEnviosForm'].modo.value = 'descargar';
				document.forms['EntradaEnviosForm'].submit();
			}		
			
			
		</script>
		
	</body>
</html>

