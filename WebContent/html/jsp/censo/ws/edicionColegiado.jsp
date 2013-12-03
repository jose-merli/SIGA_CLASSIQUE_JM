<!DOCTYPE html>
<!-- edicionColegiado.jsp -->

<!-- CABECERA JSP -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.ws.form.EdicionColegiadoForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ConModuloBean"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<c:if test="${!EdicionColegiadoForm.historico}">
		<siga:Titulo titulo="censo.ws.gestioncolegiado.titulo" localizacion="censo.ws.gestioncolegiado.localizacion"/>
	</c:if>
	
	<c:if test="${EdicionColegiadoForm.historico}">
		<siga:Titulo titulo="censo.ws.gestioncolegiado.historico.titulo" localizacion="censo.ws.gestioncolegiado.historico.localizacion"/>
	</c:if>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="EdicionColegiadoForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
		
		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		
		jQuery(function(){
			jQuery("#codigopaisextranj").on("change", function(){
				selPais(jQuery(this).val());
			});
			
		});
	
		function buscar() {
			if (!${EdicionColegiadoForm.historico}) {
				sub();
				document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}
		}
		
		function inicio() {
			document.getElementById("codigoprovincia").onchange();
	 		var e = document.getElementById("codigoprovincia");
			var prov = e.options[e.selectedIndex].text;					
			document.getElementById("codigoprovincia2").value=prov;
			selPaisInicio();
		}
		
		function selPaisInicio() {
			var valor = document.getElementById("codigopaisextranj").value;
			if (valor!="" && valor!=idEspana) {
		   		document.getElementById("codigopoblacion").value="";
		   		document.getElementById("codigoprovincia").value="";
			   	//aalg: se quita la marca de obligatoriedad
			   	//document.getElementById("provinciaSinAsterisco").className="labelText";
				//document.getElementById("provinciaConAsterisco").className="ocultar";
				//document.getElementById("poblacionEspanola").className="ocultar";
				//document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		//document.getElementById("poblacionExt").value="";
				//document.getElementById("poblacionEspanola").className="";
				//document.getElementById("poblacionExtranjera").className="ocultar";
				//aalg: se restaura la marca de obligatoriedad si es pertinente
				//comprobarTelefonoAsterico();
	       }
		}
		
		function selPais(valor) {                                                                   
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("codigopoblacion").value="";
		   		document.getElementById("codigoprovincia").value="";
			   	jQuery("#codigoprovincia").attr("disabled","disabled");
			   	//aalg: se quita la marca de obligatoriedad
			   	//document.getElementById("provinciaSinAsterisco").className="labelText";
				//document.getElementById("provinciaConAsterisco").className="ocultar";
				//document.getElementById("poblacionEspanola").className="ocultar";
				//document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("codigopoblacion").value="";
		   		document.getElementById("codigoprovincia").value="";
				jQuery("#codigoprovincia").removeAttr("disabled");
				//document.getElementById("poblacionEspanola").className="";
				//document.getElementById("poblacionExtranjera").className="ocultar";
				//aalg: se restaura la marca de obligatoriedad si es pertinente
				//comprobarTelefonoAsterico();
	       }
		   document.getElementById("codigoprovincia").onchange();
	    }
		
	 	
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>



<body onLoad="ajusteAlto('resultado');buscar();inicio();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		
		<html:form action="/CEN_EdicionColegiado.do?noReset=false" method="POST" target="resultado">	
		<c:set var="htmlTextReadOnly" value="false" />
		<c:set var="htmlTextClass" value="box" />		
		<c:set var="comboBDClass" value="boxCombo" />
		<c:set var="comboProvincia" value="block" />
		<c:set var="textProvincia" value="none" />
		
		<c:if test="${EdicionColegiadoForm.accion!='editar' || EdicionColegiadoForm.historico}">
			<c:set var="htmlTextReadOnly" value="true" />
			<c:set var="htmlTextClass" value="boxConsulta" />
			<c:set var="comboBDClass" value="boxComboConsulta" />
			<c:set var="comboProvincia" value="none" />
			<c:set var="textProvincia" value="block" />
		</c:if>
		
		<html:hidden name="EdicionColegiadoForm" property="incidenciaNumeroColegiadoDuplicadoRevisada" value="false"/>
					
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.datosColegiado">			
				<table class="tablaCampos" align="center">							
					
					<input type="hidden" name="modo" value="<bean:write name="EdicionColegiadoForm" property="modo"/>"/>
					<input type="hidden" name="idcensodatos" value="${EdicionColegiadoForm.idcensodatos}"/>
					
					<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">										
							<siga:Idioma key="censo.ws.literal.publicarInformacion"/>
						</td>
						<td>
							<html:checkbox name="EdicionColegiadoForm" property="publicarcolegiado"  disabled="${htmlTextReadOnly}"/>	
							<html:hidden name="EdicionColegiadoForm" property="publicarcolegiado" value="false"/>						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.numeroSolicitud"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="numsolicitudcolegiacion" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.numColegiado"/>
						</td>
						
						<td>
							<html:text name="EdicionColegiadoForm" property="ncolegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.nombre"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="nombre" styleId="nombreColegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.primerApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido1" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.segundoApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido2" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.sexo"/>
						</td>
						<td>
							<html:select property="sexo" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">								
								<html:optionsCollection name="EdicionColegiadoForm" property="sexos" value="key" label="value"></html:optionsCollection>
							</html:select>													
						</td>						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fechanacimiento"/>
						</td>
						<td>							
							<siga:Fecha  nombreCampo= "fechanacimiento" valorInicial="${EdicionColegiadoForm.fechanacimiento}" disabled="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.tipoIdentificacion"/>
						</td>
						<td>							
							<html:select property="idcensotipoidentificacion" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">								
								<html:optionsCollection name="EdicionColegiadoForm" property="tiposIdentificacion" value="key" label="value"></html:optionsCollection>
							</html:select>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.identificacion"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="numdocumento" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarTelefono"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicartelefono" disabled="${htmlTextReadOnly}"/>
							<html:hidden name="EdicionColegiadoForm" property="publicartelefono" value="false"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.telefono"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="telefono" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarTelefonoMovil"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicartelefonomovil" disabled="${htmlTextReadOnly}"/>
							<html:hidden name="EdicionColegiadoForm" property="publicartelefonomovil" value="false"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.telefonoMovil"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="telefonomovil" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarfax"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicarfax"disabled="${htmlTextReadOnly}"/>	
							<html:hidden name="EdicionColegiadoForm" property="publicarfax" value="false"/>					
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fax"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="fax" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicaremail"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicaremail" disabled="${htmlTextReadOnly}"/>
							<html:hidden name="EdicionColegiadoForm" property="publicaremail" value="false"/>							
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.email"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="email" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.situacionEjer"/>
						</td>
						<td>						
							
							<html:select property="idecomcensosituacionejer" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">								
								<html:optionsCollection name="EdicionColegiadoForm" property="situacionesEjerciente" value="key" label="value"></html:optionsCollection>
							</html:select>							
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fechasituacion"/>
						</td>
						<td>
							<siga:Fecha  nombreCampo= "fechasituacion" valorInicial="${EdicionColegiadoForm.fechasituacion}" disabled="${htmlTextReadOnly}"/>							
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.residente"/>
						</td>
						<td colspan="3">							
							<html:checkbox name="EdicionColegiadoForm" property="residente" disabled="${htmlTextReadOnly}"/>
							<html:hidden name="EdicionColegiadoForm" property="residente" value="false"/>						
						</td>											
					</tr>
					
					
				</table>			
			</siga:ConjCampos>
			
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.direccion">			
				<table class="tablaCampos" align="center">
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicardireccion"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicardireccion" disabled="${htmlTextReadOnly}"/>
							<html:hidden name="EdicionColegiadoForm" property="publicardireccion" value="false"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.tipovia"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="desctipovia" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>		
					</tr>	
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.domicilio"/>
						</td>  
						<td>							
							<html:text name="EdicionColegiadoForm" property="domicilio" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.codigopostal"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="codigopostal" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>		
					</tr>	
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.pais"/>
						</td>  
						<td>							
							<bean:define id="codigopaisextranjList" name="EdicionColegiadoForm" property="codigopaisextranjList" type="java.util.ArrayList"></bean:define>
							
							<siga:ComboBD nombre="codigopaisextranj" tipo="pais"
											clase="${comboBDClass}" obligatorio="false"
											readonly="${htmlTextReadOnly}"											
											elementoSel="<%=codigopaisextranjList%>"
											accion="selPais(this.value);" /> 						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.provincia"/>
						</td>
						<td>
							<bean:define id="codigoprovinciaList" name="EdicionColegiadoForm" property="codigoprovinciaList" type="java.util.ArrayList"></bean:define>
							
							
							<div style="display: ${comboProvincia}">
								<siga:ComboBD nombre="codigoprovincia"
											tipo="provincia" clase="${comboBDClass}" obligatorio="false"
											readonly="false"
											elementoSel="<%=codigoprovinciaList%>"
											accion="Hijo:codigopoblacion" />
							</div>				
							<div  style="display: ${textProvincia}">
								<input type="text" name="codigoprovincia2" size="30" value="" readonly="readonly" class="boxConsulta">
							</div>	
																		
										
						</td>		
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.poblacion"/>
						</td>  
						<td>		
							<bean:define id="codigopoblacionList" name="EdicionColegiadoForm" property="codigopoblacionList" type="java.util.ArrayList"></bean:define>				
							
							<siga:ComboBD nombre="codigopoblacion"
											tipo="poblacion" 
											clase="${comboBDClass}"
											readonly="${htmlTextReadOnly}"
											elementoSel="<%=codigopoblacionList%>"						
											hijo="t" />
											
											
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.poblacionRecibida"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="descripcionpoblacion" size="30" styleClass="boxConsulta" readonly="true"/>
						</td>		
					</tr>
				</table>			
			</siga:ConjCampos>	
			
			<bean:define id="idestadocolegiado" name="EdicionColegiadoForm" property="idestadocolegiado" type="Short"></bean:define>
			
			<siga:ConjCampos leyenda="censo.ws.literal.estado">								
				<table class="tablaCampos" align="center">											
					<tr class="labelTextValue">
						<td>&nbsp;&nbsp;<siga:Idioma key="<%=AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.getDescripcion(idestadocolegiado)%>"/></td>
					</tr>					
				</table>
			</siga:ConjCampos>
			
			<c:choose>
				<c:when test="${EdicionColegiadoForm.incidencias!=null}">				
					<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.incidencias">								
						<table class="tablaCampos" align="center">
							<c:forEach items="${EdicionColegiadoForm.incidencias}" var="incidencia" varStatus="i">							
								<tr class="labelTextValue">									
									<td id="${i.index}">&nbsp;*&nbsp;<siga:Idioma key="${incidencia}"/></td>									
								</tr>
							</c:forEach>
						</table>
					</siga:ConjCampos>
				</c:when>		
			</c:choose>
			
			
					
		</html:form>
		
		<c:choose>
			<c:when test="${!EdicionColegiadoForm.historico && EdicionColegiadoForm.accion=='editar'}">
				<siga:ConjBotonesAccion botones="G, AR" titulo="censo.ws.gestioncolegiado.historicoCambios" clase="botonesSeguido"/>		
			</c:when>
			<c:when test="${!EdicionColegiadoForm.historico && EdicionColegiadoForm.accion!='editar'}">
				<siga:ConjBotonesAccion botones="" titulo="censo.ws.gestioncolegiado.historicoCambios" clase="botonesSeguido"/>
			</c:when>
		</c:choose>
			
		<!-- formulario para el botón volver -->		
		<c:choose>
			<c:when test="${EdicionColegiadoForm.historico}">
				<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<input type="hidden" name="historico"  id="historico"  value="false">
					<input type="hidden" name="accion"  id="accion"  value="${EdicionColegiadoForm.accionPadre}">
					<input type="hidden" name="modo"  id="modo"  value="${EdicionColegiadoForm.accionPadre}">
					<html:hidden property="idcensodatosPadre" value="${EdicionColegiadoForm.idcensodatosPadre }"/>
				</html:form>		
			</c:when>
			<c:otherwise>
				<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<html:hidden property="modo" value="${EdicionRemesaForm.accion}"/>
				</html:form>
			</c:otherwise>
		</c:choose>

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0";					 
				class="frameGeneral">
			<!--style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	</div>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<script type="text/javascript">
			 				 	
			 	function accionVolver() {
					document.forms[1].submit();
				}
			 	
			 	function accionGuardar() {
			 		var guardar = true;
			 		document.forms[0].incidenciaNumeroColegiadoDuplicadoRevisada.value = "false";
			 		if (${EdicionColegiadoForm.incidenciaNumeroColegiadoDuplicado} && '${EdicionColegiadoForm.ncolegiado}' == document.forms[0].ncolegiado.value) {
			 			guardar = confirm('<siga:Idioma key="censo.ws.ncolegiadoduplicado.confirmacion"/>');
			 			if (guardar) {
			 				document.forms[0].incidenciaNumeroColegiadoDuplicadoRevisada.value = "true";
			 			}
			 		}
			 		
			 		if (${EdicionColegiadoForm.incidenciaPoblacionNoEncontrada} && document.forms[0].descripcionpoblacion.value && document.forms[0].codigopoblacion.value) {
			 			guardar = confirm('<siga:Idioma key="censo.ws.poblacion.confirmacion"/>');
			 			if (guardar) {
			 				document.forms[0].incidenciaPoblacionNoEncontradaRevisada.value = "true";
			 			}
			 		}
			 		
			 		if (guardar) {
				 		sub();
						document.forms[0].modo.value="modificar";
						document.forms[0].target="mainWorkArea";	
						document.forms[0].submit();
			 		}
			 	}
			 	
			 	function accionArchivar() {
			 		sub();
					document.forms[0].modo.value="archivar";
					document.forms[0].target="mainWorkArea";	
					document.forms[0].submit();					
			 	}
			 	
							
				
				
			 </script>
			 
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>

</body>
</html>
