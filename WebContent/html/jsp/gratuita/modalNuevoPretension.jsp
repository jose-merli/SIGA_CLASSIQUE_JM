<!DOCTYPE html>
<html>
<head>
<!-- modalNuevoPretension.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>




<!-- JSP -->


	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	
</head>

<body>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.actuacionesDesigna.literal.pretensiones"/>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="submitArea">
		<html:hidden name="MantenimientoProcedimientosForm" property="modo" value="insertarPretensionModal"/>
		<html:hidden name="MantenimientoProcedimientosForm" property="idProcedimiento" />	
		<html:hidden property = "datosMasivos" />
		
	</html:form>
	<siga:Table 
		name="tablaPretensiones"
		border="1"
		
		columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,gratuita.procedimientos.literal.codigo,gratuita.procedimientos.literal.nombre"
		columnSizes="5,12,">
	   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty pretensiones}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			</c:when>
			
			<c:otherwise>
	  			<c:forEach items="${pretensiones}"	var="pretension" varStatus="status">
	  				<siga:FilaConIconosExtExt fila='${status.count}'
  						botones=""
  						pintarEspacio="no"
  						visibleEdicion = "no"
  						visibleBorrado = "no"
  						visibleConsulta = "no"
  						
  						nombreTablaPadre="tablaPretensiones"
  						clase="listaNonEdit"
  						
  						>
						
						<td align="center">
							<input type="hidden" id ="idPretension_${status.count}" value ="${pretension.IDPRETENSION}"/>	
		  					<input type="hidden" id ="idInstitucion_${status.count}" value ="${pretension.IDINSTITUCION}"/>
							<input type="checkbox" id="chkPretension_${status.count}"  name="chkPretension" >
						</td>
						
						<td align="center">
		  					
		  					<c:out value="${pretension.CODIGOEXT}"/>
							
						</td>
						<td align="left">
							<c:out value="${pretension.DESCRIPCION}"/>
						</td>
					</siga:FilaConIconosExtExt>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>





	

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
			 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
	<siga:ConjBotonesAccion botones="Y,C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
		
		
		function cargarChecksTodos(obj){
			
			var pretensiones = document.getElementsByName("chkPretension");
			
			for ( var i = 0; i < pretensiones.length; i++) {
				pretensiones[i].checked = obj.checked; 
			}
				
		}
		
		function accionGuardarCerrar() {
			sub();
			var registrosInsertar = '';
			pretensiones = document.getElementsByName('chkPretension');
			for ( var j = 0; j < pretensiones.length; j++) {
				if(document.getElementById(pretensiones[j].id).checked){
				
					filaModulo = pretensiones[j].id.split("chkPretension_")[1];
					idInstitucion = document.getElementById("idInstitucion_"+filaModulo).value;
					idProcedimiento = document.forms[0].idProcedimiento.value;
					idPretension = document.getElementById("idPretension_"+filaModulo).value;

  					
					
					registrosInsertar = registrosInsertar + 
					idInstitucion + "," + 
					idProcedimiento + "," + 
					idPretension + "#" ; 
				}
			}
			if(registrosInsertar==''){
				alert('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
			}else{
				document.forms[0].datosMasivos.value = registrosInsertar;
				document.forms[0].submit();
				window.top.returnValue="MODIFICADO";
			}
		}
		
		
		
		//Asociada al boton Cerrar
		function accionCerrar() {		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}	
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>