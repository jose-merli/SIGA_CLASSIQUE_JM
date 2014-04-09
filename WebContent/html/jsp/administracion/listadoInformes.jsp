<!DOCTYPE html>
<html>
<head>
	<!-- listadoInformes.jsp -->
	
	<!-- CABECERA JSP -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<%@ page pageEncoding="ISO-8859-15"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
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
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>
	<bean:define id="informes" name="listadoInformes" scope="request"/>

	<siga:Table 		   
	   name="listadoInformesTable"
	   border="1"
	   columnNames="administracion.informes.literal.colegio,
	   				administracion.informes.literal.tipoInforme,
	   				administracion.informes.literal.orden,
	   				administracion.informes.literal.nombre,
	   				administracion.informes.literal.descripcion,
	   				administracion.informes.literal.visible,
	   				administracion.informes.literal.solicitantes,
	   				administracion.informes.literal.destinatarios.enviarA,"
	   columnSizes="8,10,5,19,19,6,6,6,12" >
		
		<c:choose>
   			<c:when test="${empty informes}">
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
   			</c:when>
   			
   			<c:otherwise>   
				<c:forEach items="${informes}" var="informe" varStatus="status">
					<siga:FilaConIconos	fila='${status.count}'				    
		  				botones="${informe.botones}" 
		  				elementos="${informe.elementosFila}"
		  				pintarEspacio="no"
		  				visibleConsulta="no"
		  				visibleEdicion = "no"
		  				visibleBorrado = "no"
		  				clase="listaNonEdit">
								 
						<td align='left'>
							<input type="hidden" name="idPlantilla_${status.count}" id="idPlantilla_${status.count}"  value="${informe.idPlantilla}">
							<input type="hidden" name="idInstitucion_${status.count}" id="idInstitucion_${status.count}" value="${informe.idInstitucion}">
							<input type="hidden" name="claseTipoInforme_${status.count}" id="claseTipoInforme_${status.count}" value="${informe.claseTipoInforme}">
												
							<c:out value="${informe.descripcionInstitucion}"></c:out>
						</td>
						<td align='left'><c:out value="${informe.descripcionTipoInforme}"></c:out></td>
						<td align='center'>
							<c:choose>
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
	</siga:Table>

	<html:form action="/ADM_GestionInformes"  name="InformeFormEdicion" type="com.siga.administracion.form.InformeForm">
		<html:hidden property="modo" />
		<html:hidden property="idPlantilla" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="claseTipoInforme" />
		<input type="hidden" name="actionModal" />
	</html:form>

	<iframe name="submitArea"	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>	
				
	<script type="text/javascript">

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
		document.InformeFormEdicion.target = "mainWorkArea";
		document.InformeFormEdicion.submit();
	}

	function editar(fila){
		var idPlantillaFila = 'idPlantilla_'+fila;
		var idInstitucionFila = 'idInstitucion_'+fila;
		var claseTipoInformeFila = 'claseTipoInforme_'+fila;
		document.InformeFormEdicion.idPlantilla.value = document.getElementById(idPlantillaFila).value;
		document.InformeFormEdicion.idInstitucion.value = document.getElementById(idInstitucionFila).value;
		document.InformeFormEdicion.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
		document.InformeFormEdicion.modo.value = "editar";
		document.InformeFormEdicion.target = "mainWorkArea";
		document.InformeFormEdicion.submit();
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

	function duplicar(fila) {
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