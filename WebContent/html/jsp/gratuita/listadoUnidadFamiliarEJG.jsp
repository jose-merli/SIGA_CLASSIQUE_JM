<!DOCTYPE html>
<html>
<head>
<!-- listadoUnidadFamiliarEJG.jsp-->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-bean.tld" 	prefix="bean"%>
<%@taglib uri = "struts-html.tld" 	prefix="html"%>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@taglib uri = "c.tld" 			prefix="c"%>

<%@page import="com.atos.utils.ClsConstants"%>

<!-- JSP -->
	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<title><siga:Idioma key="gratuita.operarUnidadFamiliar.literal.unidadFamiliar"/></title>
	<siga:Titulo titulo="gratuita.busquedaEJG.unidadFamiliar" localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body class="tablaCentralCampos">	
	<bean:define id="modo" name="DefinirUnidadFamiliarEJGForm" property="modo" type="java.lang.String"/>
	<bean:define id="conceptoEJG" scope="request" name="EJG_UNIDADFAMILIAR" />
	<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean"/>
	
	<html:form action="/JGR_UnidadFamiliarPerJG" method="post" target="submitArea" >
		<html:hidden property="modo" value="abrirPestana"/>
		<html:hidden property="idInstitucionJG" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<html:hidden property="idPersonaJG" value=""/>
		<html:hidden property="idInstitucionEJG" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anioEJG" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numeroEJG" value="${DefinirUnidadFamiliarEJGForm.numero}"/>
		<html:hidden property="conceptoE" value="${conceptoEJG}"/>
		<html:hidden property="tituloE" value="gratuita.personaJG.literal.unidadFamiliar"/>
		<html:hidden property="localizacionE" value=""/>
		<html:hidden property="accionE" value="nuevo"/>
		<html:hidden property="actionE" value="/JGR_UnidadFamiliarPerJG.do"/>
		<html:hidden property="pantallaE" value="M"/>
	</html:form>
		
	<html:form action="/JGR_UnidadFamiliarEJG"  method="post" target="submitArea">
		<html:hidden property="modo"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anio" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numero" value="${DefinirUnidadFamiliarEJGForm.numero}"/>
		<html:hidden property="idInstitucion" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<input type="hidden" name="tablaDatosDinamicosD"/>		
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
	</html:form>

	<html:form action="/JGR_UnidadFamiliarEEJG" name="EEJG" method="post" target="submitArea" type ="com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm">
		<html:hidden property="modo"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anio" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numero" value="${DefinirUnidadFamiliarEJGForm.numero}"/>
		<html:hidden property="idInstitucion" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<input type="hidden" name="tablaDatosDinamicosD"/>
		
	</html:form>

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.ejg.anio}"></c:out>/<c:out value="${DefinirUnidadFamiliarEJGForm.ejg.numEJG}"></c:out>&nbsp;-&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.nombre}"></c:out>&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.apellido1}"></c:out>&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.apellido2}"></c:out>
			</td>
		</tr>
	</table>

	<bean:define id="tienePermisoEejg" name="DefinirUnidadFamiliarEJGForm" property="permisoEejg" type="java.lang.Boolean"/>
	<% 
		String alto = "100%";
		if (tienePermisoEejg)
			alto = "49%";
	%>

	<siga:Table 		   
		name="listadoUnidadFamiliar"
		border="2"
		columnNames="gratuita.personaJG.literal.parentescoNormalizado,
			gratuita.busquedaEJG.literal.nif,gratuita.busquedaEJG.literal.nombre,
			gratuita.operarInteresado.literal.ingresosAnuales,
			gratuita.operarInteresado.literal.bienesMobiliarios,
			gratuita.operarInteresado.literal.bienesInmuebles,
			gratuita.operarInteresado.literal.otrosBienes,"
		columnSizes="9,8,25,8,8,8,8,28"
		modal="G"
		mensajeBorrado="gratuita.ejg.unidadFamiliar.borrado"
		fixedHeight="<%=alto%>">
			  
		<logic:empty name="DefinirUnidadFamiliarEJGForm" property="unidadFamiliar">
			<tr class="notFound">
			 	<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</logic:empty>
		
		<logic:notEmpty name="DefinirUnidadFamiliarEJGForm"	property="unidadFamiliar">
			<logic:iterate name="DefinirUnidadFamiliarEJGForm"	property="unidadFamiliar" id="solicitante" indexId="index" type="com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm">
				<%index = index.intValue()+1; %>
						
				<c:if	test="${solicitante.idPersona!=DefinirUnidadFamiliarEJGForm.personaJG.idPersona}">
					<bean:define id="elementosFila" name="solicitante" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
					<bean:define id="botones" name="solicitante" property="botones" type="java.lang.String"/>	
			
					<siga:FilaConIconosExtExt 
						fila="<%=String.valueOf(index.intValue())%>" 
						botones="<%=botones%>" 
						elementos="<%=elementosFila%>" 
						clase="listaNonEdit" 
						modo="<%=modo%>" 
						nombreTablaPadre="listadoUnidadFamiliar">
						
						<td>
							<input type="hidden" id="idPersonaJG_<%=index%>" value="<bean:write name="solicitante" property="idPersona" />">
							<input type="hidden" name="oculto<%=index%>_1" value="EJGUnidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_2" value="gratuita.personaJG.literal.unidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_3" value="gratuita.personaJG.literal.unidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_4" value="editar">
							<input type="hidden" name="oculto<%=index%>_5" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
							<input type="hidden" name="oculto<%=index%>_6" value="${solicitante.personaJG.idPersona}">
							<input type="hidden" name="oculto<%=index%>_7" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
							<input type="hidden" name="oculto<%=index%>_8" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}">
							<input type="hidden" name="oculto<%=index%>_9" value="${DefinirUnidadFamiliarEJGForm.anio}">
							<input type="hidden" name="oculto<%=index%>_10" value="${DefinirUnidadFamiliarEJGForm.numero}">
							<input type="hidden" name="oculto<%=index%>_11" value="${solicitante.peticionEejg.idPeticion}">
							<input type="hidden" name="oculto<%=index%>_12" value="${DefinirUnidadFamiliarEJGForm.esComision}">
							<input type="hidden" name="oculto<%=index%>_13" value="${solicitante.solicitante}">
							<input type="hidden" name="oculto<%=index%>_14" value="${solicitante.personaJG.nif}">
							<input type="hidden" name="oculto<%=index%>_15" value="${solicitante.personaJG.tipoIdentificacion}">
							<input type="hidden" name="oculto<%=index%>_16" value="${solicitante.personaJG.asistidoAutorizaEEJG}">
														
							<c:out value="${solicitante.parentesco.descripcion}"></c:out>						
						</td>
						
						<td>
							<c:choose>
								<c:when test="${solicitante.personaJG.nif!=''}">
									<c:out value="${solicitante.personaJG.nif}"></c:out>
								</c:when>
								
								<c:otherwise>
								  &nbsp;
								</c:otherwise>
							</c:choose>
						</td>
						
						<td>
							<c:out value="${solicitante.personaJG.nombre}"></c:out>&nbsp;
							<c:out value="${solicitante.personaJG.apellido1}"></c:out>&nbsp;
							<c:out value="${solicitante.personaJG.apellido2}"></c:out>
						</td>

						<td align="right">
							<c:if test="${solicitante.importeIngresosAnuales!=''}">
								<c:out value="${solicitante.importeIngresosAnuales}"></c:out>&euro;
							</c:if>
							<c:if test="${solicitante.importeIngresosAnuales==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeBienesMuebles!=''}">
								<c:out value="${solicitante.importeBienesMuebles}"></c:out>&euro;
							</c:if>
							<c:if test="${solicitante.importeBienesMuebles==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeBienesInmuebles!=''}">
								<c:out value="${solicitante.importeBienesInmuebles}"></c:out>&euro;
							</c:if>
							<c:if test="${solicitante.importeBienesInmuebles==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeOtrosBienes!=''}">
								<c:out value="${solicitante.importeOtrosBienes}"></c:out>&euro;
							</c:if>
							<c:if test="${solicitante.importeOtrosBienes==''}">
								&nbsp;						
							</c:if>
						</td>
					</siga:FilaConIconosExtExt>
				</c:if>
				
				<c:if test="${solicitante.idPersona==DefinirUnidadFamiliarEJGForm.personaJG.idPersona}">
					<bean:define id="elementosFila" name="solicitante" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
					<siga:FilaConIconosExtExt 
						fila="<%=String.valueOf(index.intValue())%>" 
						botones="" 
						elementos="<%=elementosFila%>" 
						clase="listaNonEdit" 
						modo="<%=modo%>"  
						visibleBorrado="false" 
						visibleEdicion="false"	
						visibleConsulta="false" 
						nombreTablaPadre="listadoUnidadFamiliar">
					
						<td>
							<input type="hidden" id="idPersonaJG_<%=index%>" value="<bean:write name="solicitante" property="idPersona" />">
							<input type="hidden" name="oculto<%=index%>_1" value="EJGUnidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_2" value="gratuita.personaJG.literal.unidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_3" value="gratuita.personaJG.literal.unidadFamiliar">
							<input type="hidden" name="oculto<%=index%>_4" value="editar">
							<input type="hidden" name="oculto<%=index%>_5" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
							<input type="hidden" name="oculto<%=index%>_6" value="${solicitante.personaJG.idPersona}">
							<input type="hidden" name="oculto<%=index%>_7" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
							<input type="hidden" name="oculto<%=index%>_8" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}">
							<input type="hidden" name="oculto<%=index%>_9" value="${DefinirUnidadFamiliarEJGForm.anio}">
							<input type="hidden" name="oculto<%=index%>_10" value="${DefinirUnidadFamiliarEJGForm.numero}">
							<input type="hidden" name="oculto<%=index%>_12" value="${DefinirUnidadFamiliarEJGForm.esComision}">
							<input type="hidden" name="oculto<%=index%>_13" value="${solicitante.solicitante}">
							<input type="hidden" name="oculto<%=index%>_14" value="${solicitante.personaJG.nif}">
							<input type="hidden" name="oculto<%=index%>_15" value="${solicitante.personaJG.tipoIdentificacion}">
							<input type="hidden" name="oculto<%=index%>_16" value="${solicitante.personaJG.asistidoAutorizaEEJG}">
							
							<c:choose>
								<c:when test="${solicitante.peticionEejg.idPeticion!=null}">
									<input type="hidden" name="oculto<%=index%>_11" value="${solicitante.peticionEejg.idPeticion}">
								</c:when>
								
								<c:otherwise>
								  	<input type="hidden" name="oculto<%=index%>_11" value=" ">
								</c:otherwise>
							</c:choose>
	
							<c:out value="${solicitante.parentesco.descripcion}"></c:out>					
						</td>
						<td>
							<c:out value="${solicitante.personaJG.nif}"></c:out>&nbsp;
						</td>
						<td>
							<c:out value="${solicitante.personaJG.nombre}"></c:out>
							&nbsp;
							<c:out value="${solicitante.personaJG.apellido1}"></c:out>
							&nbsp;
							<c:out value="${solicitante.personaJG.apellido2}"></c:out>
						</td>
	
						<td align="right">
							<c:if	test="${solicitante.importeIngresosAnuales!=''}">
								<c:out value="${solicitante.importeIngresosAnuales}"></c:out>&euro;
							</c:if>
							<c:if test="${solicitante.importeIngresosAnuales==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeBienesMuebles!=''}">
								<c:out value="${solicitante.importeBienesMuebles}"></c:out>&euro;
							</c:if>
							<c:if	test="${solicitante.importeBienesMuebles==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeBienesInmuebles!=''}">
								<c:out value="${solicitante.importeBienesInmuebles}"></c:out>&euro;
							</c:if>
							<c:if	test="${solicitante.importeBienesInmuebles==''}">
								&nbsp;
							</c:if>
						</td>
						
						<td align="right">
							<c:if test="${solicitante.importeOtrosBienes!=''}">
								<c:out value="${solicitante.importeOtrosBienes}"></c:out>&euro;
							</c:if>
							<c:if	test="${solicitante.importeOtrosBienes==''}">
								&nbsp;						
							</c:if>
						</td>
					</siga:FilaConIconosExtExt>								
				</c:if>
			</logic:iterate>
		
			<siga:FilaConIconosExtExt 
				fila="200" 
				botones ="" 
				visibleBorrado="false" 
				visibleEdicion="false" 
				visibleConsulta="false"  
				clase="listaNonEdit" 
				nombreTablaPadre="listadoUnidadFamiliar">
						
				<td colspan="3" align="right"><b><siga:Idioma key="gratuita.listadoUnidadFamiliar.literal.totalUnidadFamiliar"/></b></td>
				<td align="right"><c:out value="${DefinirUnidadFamiliarEJGForm.importeIngresosAnuales}"></c:out>&euro;</td>
				<td align="right"><c:out value="${DefinirUnidadFamiliarEJGForm.importeBienesMuebles}"></c:out>&euro;</td>
				<td align="right"><c:out value="${DefinirUnidadFamiliarEJGForm.importeBienesInmuebles}"></c:out>&euro;</td>
				<td align="right"><c:out value="${DefinirUnidadFamiliarEJGForm.importeOtrosBienes}"></c:out>&euro;</td>		
			</siga:FilaConIconosExtExt>
		</logic:notEmpty>
	</siga:Table>

	<c:if test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">	
					<siga:Idioma key="gratuita.eejg.peticiones.titulo"/>
				</td>
			</tr>
		</table>

		<siga:Table 	
		   	name="listadoPeticiones"
		   	border="2"
		   	columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='checkTodos()'/>,
		   		gratuita.busquedaEJG.literal.nif,
		   		gratuita.busquedaEJG.literal.nombre,
		   		gratuita.eejg.peticiones.usuarioPeticion,
		   		gratuita.eejg.peticiones.fechaPeticion,"
		   	columnSizes="5,10,30,30,10,">
		   	
			<c:set var="disabledPorCambioLetrado" value="" />
		  
			<logic:empty name="DefinirUnidadFamiliarEJGForm" property="peticionesEejg">
				<tr class="notFound">
   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			</logic:empty>
			
			<logic:notEmpty name="DefinirUnidadFamiliarEJGForm"	property="peticionesEejg">
				<logic:iterate name="DefinirUnidadFamiliarEJGForm"	property="peticionesEejg" id="peticion" indexId="indice" type="com.siga.beans.eejg.ScsEejgPeticionesBean">
					<%indice = indice.intValue()+1; %>

					<bean:define id="elementosFila" name="peticion" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
					<siga:FilaConIconosExtExt 
						fila="<%=String.valueOf(indice.intValue())%>" 
						botones="" 
						elementos="<%=elementosFila%>" 
						clase="listaNonEdit" 
						modo="<%=modo%>" 
						visibleBorrado="false" 
						visibleEdicion="false"	
						visibleConsulta="false" 
						nombreTablaPadre="listadoPeticiones">
						
						<td align="center">
							<input type="hidden" name="peticion<%=indice%>_1" value="${peticion.idPersona}">
							<input type="hidden" name="peticion<%=indice%>_2" value="${peticion.idInstitucion}">
							<input type="hidden" name="peticion<%=indice%>_3" value="${peticion.idTipoEjg}">
							<input type="hidden" name="peticion<%=indice%>_4" value="${peticion.anio}">
							<input type="hidden" name="peticion<%=indice%>_5" value="${peticion.numero}">
							<input type="hidden" name="peticion<%=indice%>_10" value="${peticion.nif}">
					
							<c:choose>
								<c:when test="${peticion.idPeticion!=null}">
									<input type="hidden" name="peticion<%=indice%>_6" value="${peticion.idPeticion}">
								</c:when>
								<c:otherwise>
						  			<input type="hidden" name="peticion<%=indice%>_6" value=" ">
								</c:otherwise>
							</c:choose>						
						
							<c:choose>
								<c:when test="${peticion.idXml!=null && peticion.personaUnidadFamiliar==true && peticion.estado == 30}">
									<input type="checkbox" value="${peticion.idXml}" name="chkPersona">
									<input type="hidden" name="peticion<%=indice%>_7" value=" ">
								</c:when>
				
								<c:otherwise>
				  					<input type="checkbox"  value="<bean:write name="indice"/>" disabled name="chkPersona">
				  					<input type="hidden" name="peticion<%=indice%>_7" value="${peticion.msgError}">
								</c:otherwise>
							</c:choose>											
						</td>
		
						<td><c:out value="${peticion.nif}"></c:out>&nbsp;</td>
						<td>
							<c:out value="${peticion.nombre}"></c:out>
							&nbsp;
							<c:out value="${peticion.apellido1}"></c:out>
							&nbsp;
							<c:out value="${peticion.apellido2}"></c:out>
						</td>
						<td>
							<c:out value="${peticion.usuarioPeticion.NIF}"></c:out>
							&nbsp;-&nbsp;
							<c:out value="${peticion.usuarioPeticion.descripcion}"/>
						</td>
						<td><c:out value="${peticion.fechaPeticion}"/></td>					
					</siga:FilaConIconosExtExt>
				</logic:iterate>
			</logic:notEmpty>
		</siga:Table>
	</c:if>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<c:choose>
		<c:when test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">
			<siga:ConjBotonesAccion botones="V,N,DEE" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
		</c:when>
		
		<c:otherwise>
		 	<siga:ConjBotonesAccion botones="V,N" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
		</c:otherwise>
	</c:choose>
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
		<html:hidden property="idInstitucion" value = "<%=usrBean.getLocation()%>"/>
		<html:hidden property="idTipoInforme" value='<%=usrBean.isComision() ?"CAJG":"EJG"%>'/>
		<html:hidden property="enviar" value="0" />
		<html:hidden property="descargar" value="1"/>
		<html:hidden property="datosInforme"/>
		<html:hidden property="destinatarios" value="S"/>
		<html:hidden property="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal'>
	</html:form>	
	
	<!-- Formulario para la edicion del envio -->
	<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="tablaDatosDinamicosD" value="">
	</form>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>	
</body>	
	
<script type="text/javascript">
				
	//Asociada al boton Cerrar
	function accionVolver() {
		
		if(document.DefinirUnidadFamiliarEJGForm.jsonVolver && document.DefinirUnidadFamiliarEJGForm.jsonVolver.value!=''){
			
			jSonVolverValue = document.DefinirUnidadFamiliarEJGForm.jsonVolver.value;
			jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
			var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
			nombreFormulario = jSonVolverObject.nombreformulario;
			if(nombreFormulario != ''){
				parent.document.forms[nombreFormulario].idRemesa.value =  jSonVolverObject.idremesa;
				parent.document.forms[nombreFormulario].idinstitucion.value = jSonVolverObject.idinstitucion;
				parent.document.forms[nombreFormulario].modo.value="editar";
				parent.document.forms[nombreFormulario].target = "mainWorkArea";
				parent.document.forms[nombreFormulario].submit();
				
			}
		}else{
			document.DefinirUnidadFamiliarEJGForm.idInstitucion.value = "<%=usrBean.getLocation()%>";
			document.DefinirUnidadFamiliarEJGForm.action="./JGR_EJG.do";	
			document.DefinirUnidadFamiliarEJGForm.modo.value="buscar";
			document.DefinirUnidadFamiliarEJGForm.target="mainWorkArea"; 
			document.DefinirUnidadFamiliarEJGForm.submit(); 
		}
	}
	
	function accionNuevo() {		
		var idPersonaJG = document.getElementById( 'oculto' + '1' + '_6');
		if (idPersonaJG==null)			{
		    alert("<siga:Idioma key='gratuita.listadoUnidadFamiliarEJG.solicitante'/>");
			return;
		}

		document.PersonaJGForm.target = "submitArea";
		document.PersonaJGForm.modo.value = "abrirPestana";
		document.PersonaJGForm.accionE.value = "nuevo";
		var resultado=ventaModalGeneral(document.forms[0].name,"G");
		if (resultado=="MODIFICADO") 
				buscar();		
	}
	
	function buscar() {		
		document.DefinirUnidadFamiliarEJGForm.modo.value = "abrir";
		document.DefinirUnidadFamiliarEJGForm.target = "mainPestanas";
		document.DefinirUnidadFamiliarEJGForm.submit();
	}
	
	function comunicar(fila) {
		
		
		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
		var esComision = document.getElementById( 'oculto' + fila + '_12');
		var solicitante = document.getElementById( 'oculto' + fila + '_13');
		
		
		
		
		
		var datos = "idinstitucion=="+idInstitucionEJG.value + "##idtipo==" +idTipoEJG.value+"##anio=="+anio.value +"##numero==" +numero.value+"##idPersonaJG==" +idPersonaJG.value+"##idTipoInforme==EJG%%%";
		
		if(solicitante.value==1) {
			document.InformesGenericosForm.enviar.value = '1';
			
		} else {
			document.InformesGenericosForm.enviar.value = '0';
		}
		
		document.InformesGenericosForm.datosInforme.value=datos;
		var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		if (arrayResultado==undefined||arrayResultado[0]==undefined|| typeof(arrayResultado) == "string"){
		   		
	   	} else {
	   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
	   		if (confirmar){
	   			var idEnvio = arrayResultado[0];
			    var idTipoEnvio = arrayResultado[1];
			    var nombreEnvio = arrayResultado[2];				    
			    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
	   		}
	   	}
	} 
	
	function validaNumeroIdentificacion (nif,idTipoIdentificacion){
		var errorNIE = false;
		var errorNIF = false;
		var errorDatos = false;
		var valido = true;

		if(idTipoIdentificacion== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			var numero = nif;
			if(numero.length==9){
				letIn = numero.substring(8,9);
				num = numero.substring(0,8);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letIn) {
					errorNIF=true;
				}
			}else{
				errorNIF=true;
			}
		}else if(idTipoIdentificacion== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
			var dnie = nif;
			if(dnie.length==9){
				letIni = dnie.substring(0,1);
				primera=letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni + dnie.substring(1,8);
				letFin = dnie.substring(8,9);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (!primera.match('[X|Y|Z]')||letra!=letFin) {
					errorNIE=true;
				}
			}else{
				errorNIE=true;
			}
		}
		if (errorNIF){
			valido = false;
			alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
		}
		
		return valido;
	}
	
	function solicitarEejg(fila) {
		var nif = document.getElementById( 'oculto' + fila + '_14').value;
		var idTipoIdentificacion = document.getElementById( 'oculto' + fila + '_15').value;
		
		
		
		if (!validaNumeroIdentificacion(nif,idTipoIdentificacion)) {
			fin();
			return false;
		}
		var autoriza = document.getElementById( 'oculto' + fila + '_16');
		
		if(autoriza){
			if(autoriza.value =='0'){
				alert("<siga:Idioma key='gratuita.eejg.error.solicitantenoautoriza'/>","warning");
				return false;
			}else if(autoriza.value =='1'){
				autoriza = 2;
			}else{
				autoriza = 1;
			}
		}
		else{
			autoriza = 1;
		}
		var isConfirmado = confirm('<siga:Idioma key="gratuita.eejg.message.confirmaSolicitud"/>');
		if(!isConfirmado)
			return;
		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
			datos = idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + 	','
	   			+ autoriza +'#';
   		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "solicitarEejg";
		document.EEJG.submit();
	
	}
	
	function accionCerrar() {

	}
	
	function descargarEejg(fila) {
		selectRowPeticiones(fila);
		
		var idPersonaJG = document.getElementById( 'peticion' + fila + '_1');
		var idInstitucionEJG = document.getElementById( 'peticion' + fila + '_2');
		var idTipoEJG = document.getElementById( 'peticion' + fila + '_3');
		var anio = document.getElementById( 'peticion' + fila + '_4');
		var numero = document.getElementById( 'peticion' + fila + '_5');
		var idPeticion = document.getElementById( 'peticion' + fila + '_6');
		var nif = document.getElementById( 'peticion' + fila + '_10');
		datos = idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + ','
				+idPeticion.value + ','
				+nif.value + ''
				'#';
		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "descargaEejg";
		document.EEJG.submit();	
	}
	
	function descargaEejg() {
		var chkPersonas = document.getElementsByName("chkPersona");
		datos = '';
		sub();
		for (i = 0; i < chkPersonas.length; i++) {
			if(chkPersonas[i].checked){
				var idPersonaJG = document.getElementById( 'peticion' + (i+1) + '_1');
				var idInstitucionEJG = document.getElementById( 'peticion' + (i+1) + '_2');
				var idTipoEJG = document.getElementById( 'peticion' + (i+1) + '_3');
				var anio = document.getElementById( 'peticion' + (i+1) + '_4');
				var numero = document.getElementById( 'peticion' + (i+1) + '_5');
				
				var idPeticion = document.getElementById( 'peticion' + (i+1) + '_6');
				var nif = document.getElementById( 'peticion' + (i+1) + '_10');
				datos = datos + idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + ','
	   			
	   			+idPeticion.value + ','
				+nif.value + ''
				'#';
	 		   
 		   	}
			
		}
		if(datos==''){
			alert("<siga:Idioma key="general.message.seleccionar"/>");
			fin();
			return;
		}
		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "descargaEejgMasivo";
		document.EEJG.submit();
	
	}	
		
	function checkTodos(){
		var chkGeneral = document.getElementById("chkGeneral");
	 	var chkPersonas = document.getElementsByName("chkPersona");
	  	for (i = 0; i < chkPersonas.length; i++) {
	  		if(chkGeneral.checked){
	   			if(!chkPersonas[i].disabled){
	   				chkPersonas[i].checked = chkGeneral.checked;
	   			} 
   			}else{
   				chkPersonas[i].checked = chkGeneral.checked; 
   			}
   		}
   	}
   	
   	function esperaEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaEejg"/>");
   	}
   	
   	function errorEejg(fila){
   		selectRowPeticiones(fila);
   		var msgError = document.getElementById( 'peticion' + fila + '_7').value;
   		if(msgError!=null && msgError!= ''){
   			alert("<siga:Idioma key="general.boton.errorEejg"/>" + "\n C�digo de error " + msgError);
   		}else{
   			alert("<siga:Idioma key="general.boton.errorEejg"/>");
   		}
   	}
   	
   	function avisoDNICambiado(fila){
   		selectRowPeticiones(fila);
   		var msgError = document.getElementById( 'peticion' + fila + '_7').value;
   		alert("<siga:Idioma key='gratuita.personaJG.tooltip.noPerteneceUnidadFam'/>");
   		
   	}
   	
   	function esperaInfoEejg(fila){
   		selectRowPeticiones(fila);
   		var confirmar = confirm("<siga:Idioma key='gratuita.eejg.message.avisoEsperaInfo'/>");
   		if(confirmar){
   			return descargarEejg(fila);
   		}
   		
   	}
   	function avisoEsperaInfoEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaInfoEejg"/>");
   		
   		
   	}
   	
   	function esperaAdministracionesEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaAdministracionesEejg"/>");
   	}
	
	function refrescarLocal()
	{	
		buscar();
	}
	function accionNuevaRegularizacion(){
		document.DefinirUnidadFamiliarEJGForm.modo.value = "simulaWebService";
		document.DefinirUnidadFamiliarEJGForm.submit();
	
   	}
	function enviaDocumentoCAJG(fila) {
		selectRowPeticiones(fila);
		
		var idInstitucionEJG = document.getElementById( 'peticion' + fila + '_2');
		var idTipoEJG = document.getElementById( 'peticion' + fila + '_3');
		var anio = document.getElementById( 'peticion' + fila + '_4');
		var numero = document.getElementById( 'peticion' + fila + '_5');
		var nif = document.getElementById( 'peticion' + fila + '_10');
		datos = idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + ','
				+nif.value + ''
				'#';
		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "enviaDocumentoCAJG";
		document.EEJG.submit();	
	}
	
	
	function consultar(fila, id) {
		if (typeof id == 'undefined')
			id='listadoPeticiones';
		preparaDatos(fila,id);
	   document.forms[0].modo.value = "Ver";
	   if (id=='listadoUnidadFamiliar'){
		   ventaModalGeneral(document.forms[0].name,"G");
	   } else
	   		document.forms[0].submit();
	 }

	 function editar(fila, id) {
		if (typeof id == 'undefined')
			id='listadoPeticiones';
		preparaDatos(fila, id);
	   document.forms[0].modo.value = "Editar";
	   if (id == 'listadoUnidadFamiliar'){
		   var resultado = ventaModalGeneral(document.forms[0].name,"G");
		   if (resultado) {
		  	 	if (resultado[0]) {
		   		refrescarLocalArray(resultado);
		   	} else 
		   	if (resultado=="MODIFICADO")
		   	{
		      		refrescarLocal();
		   	}
		   }
	   } else
	   	document.forms[0].submit();
	 }
	 
	 function selectRowPeticiones(fila){
		 selectRow(fila, 'listadoPeticiones');
	 }
</script>
</html>