<%@page import="com.atos.utils.ClsConstants"%>
<html>
<head>
<!-- editarSolicitudAceptadaCentralita.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>" ></script>
		

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<siga:TituloExt titulo="menu.sjcs.solicitudesAceptadasCentralita" localizacion="sjcs.solicitudaceptadacentralita.localizacion"/>

<script type="text/javascript">
	
	function refrescarLocal(){
		buscarSolicitudesAceptadas();
	}

	jQuery(function(){
		jQuery("#solicitantePais").on("change", function(){
			return onChangePais(jQuery(this).val());
		});
	});
	jQuery(function(){
		jQuery("#solicitanteIdTipoIdentificacion").on("change", function(){
			return onChangeTipoIdentificacion(jQuery(this).val());
		});
	});
	
	jQuery(function(){
		jQuery("#idJuzgado").on("change", function(){
			return onChangeJuzgado(jQuery(this).val());
		});
	});
	
	jQuery(function(){
		jQuery("#idComisaria").on("change", function(){
			return onChangeComisaria(jQuery(this).val());
		});
	});
	jQuery(function(){
		jQuery("#solicitanteProvincia").on("change", function(){
			document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCodPostal.value="";
		});
		jQuery("#solicitantePoblacion").on("change", function(){
			document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCodPostal.value="";
		});
	});
	function onChangeComisaria(valor){
		jQuery("#idJuzgado").val("");
		jQuery("#idJuzgado_searchBox").val("");
	}
	function onChangeJuzgado(valor){
		jQuery("#idComisaria").val("");
		jQuery("#idComisaria_searchBox").val("");
		
	}
	function onChangePais(valor) {
	   if (valor!="" && valor!="191") {
		   	jQuery("#solicitantePoblacion").val("");
		   	jQuery("#solicitanteProvincia").val("");
		   	document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCodPostal.value="";
	   		jQuery("#solicitanteProvincia").attr("disabled","disabled");
	   		jQuery("#poblacionEspanola").hide();
        	jQuery("#poblacionExtranjera").show();
			
       } else {
    		jQuery("#solicitantePoblacionExt").val("");
			jQuery("#solicitanteProvincia").removeAttr("disabled");
			document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCodPostal.value="";
			jQuery("#poblacionEspanola").show();
        	jQuery("#poblacionExtranjera").hide();
       }
    }
		
	function inicio() {
		if(document.getElementById("solicitantePais"))
			onChangePais('');
	}	    
	
	 function onChangeTipoIdentificacion(valor){
						
		// Solo se genera el NIF o CIF de la persona
		if(valor== "10"){
			document.getElementById("textoInformativo").style.display="none";
			onChangePais("191")
		} else if ((valor== "20") || (valor== "40")){
			document.getElementById("textoInformativo").style.display="none";

		} else{
			document.getElementById("textoInformativo").style.display="block";
		}			
	}	
	function accionVolver() 
	{		
		document.forms['SolicitudAceptadaCentralitaForm'].modo.value="volver";
		document.forms['SolicitudAceptadaCentralitaForm'].target = "mainWorkArea";
		document.forms['SolicitudAceptadaCentralitaForm'].submit();
	}
	function accionGuardar() 
	{
		sub();
		if (!validateSolicitudAceptadaCentralitaForm(document.SolicitudAceptadaCentralitaForm)){
			fin();
			return false;
		}
		if(document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNombre.value!=""){
			
			if(document.forms['SolicitudAceptadaCentralitaForm'].solicitanteIdTipoIdentificacion.value==''){
				alert("<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>");
				fin();
				return false;
			}
			if(!validaNumeroIdentificacion()){
				fin();
				return false;
			}
			
		}
		document.forms['SolicitudAceptadaCentralitaForm'].target.value="submitArea";
		document.forms['SolicitudAceptadaCentralitaForm'].modo.value="guardarSolicitudAceptada";
		document.forms['SolicitudAceptadaCentralitaForm'].submit();
		
	}
	
	function validaNumeroIdentificacion (idTipoIdentificacion, numeroIdentificacion){
		
		var errorNIE = false;
		var errorNIF = false;
		var errorDatos = false;
		var valido = true;

		if(document.forms['SolicitudAceptadaCentralitaForm'].solicitanteIdTipoIdentificacion.value== "10"){			
			var nif = document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNumeroIdentificacion.value;
			
			while (nif.indexOf(" ") != -1) {
				nif = nif.replace(" ","");
			}

			while (nif.length<9){
				nif="0"+nif;
			}						

			if (new RegExp('^[0-9]{8}[A-Za-z]$').test(nif)) {
				var letraNif = nif.substring(8,9).toUpperCase();								
				
				var numero = nif.substring(0,8);
				var posicion = numero % 23;
				var letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letraNif) {
					errorNIF=true;
				} else {
					document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNumeroIdentificacion.value = nif;
				}				
			}else{
				errorNIF=true;
			}
		}else if(document.forms['SolicitudAceptadaCentralitaForm'].solicitanteIdTipoIdentificacion.value== "40"){
			var dnie = document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNumeroIdentificacion.value;
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
		}else{
			var numId = document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNumeroIdentificacion.value;
			var tipoId = document.forms['SolicitudAceptadaCentralitaForm'].solicitanteIdTipoIdentificacion.value;
			if(numId!="" && tipoId==""){
				errorDatos=true;
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
		if (errorDatos){
			valido = false;
			alert("<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>");;
		}
		return valido;
	}
</script>
</head>

<body onload="inicio();" >
	<c:set var="parametrosComboComisaria" value="{\"idcomisaria\":\"-1\"}"/>
		<c:set var="readonlyText" value="true" />
		<c:set var="disabledSelect" value="true" />
		<c:set var="estiloText" value="boxConsulta" />
		<c:set var="estiloSelect" value="boxComboConsulta" />
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:javascript formName="SolicitudAceptadaCentralitaForm" staticJavascript="false" />
	<html:form action="${path}"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idSolicitudAceptada"/>
		<html:hidden property="idLlamada"/>
		<html:hidden property="jsonVolver"/>
		<c:if	test="${(SolicitudAceptadaCentralitaForm.idEstado=='0' && SolicitudAceptadaCentralitaForm.modo=='editarSolicitudAceptada')
			||(SolicitudAceptadaCentralitaForm.idEstado=='1' && SolicitudAceptadaCentralitaForm.modo=='editarSolicitudAceptada')}">
			<c:set var="estiloText" value="box" />
			<c:set var="estiloSelect" value="boxCombo" />
			<c:set var="readonlyText" value="" />
			<c:set var="disabledSelect" value="" />
					
		</c:if>
		<table class="tablaTitulo" cellspacing="0" height="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="${titulo}"/>
					
				</td>
			</tr>
		</table>

		<siga:ConjCampos leyenda="gratuita.gestionInscripciones.datosSolicitud.leyenda">
			<table width="100%" border="0">
				<tr>
					<td width="10"></td>
					<td width="25"></td>
					<td width="10"></td>
					<td width="25"></td>
					<td width="10"></td>
					<td width="20"></td>
					
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.idAvisoCentralita"/>
					</td>
					<td class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.idLlamada}"/>
							
					</td>
					<td class="labelText" >
						<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.fechaLlamada"/>
					</td>
					<td class="labelTextValor"> 
						<c:out value="${SolicitudAceptadaCentralitaForm.fechaLlamadaHoras}"/>
						<html:hidden property="fechaGuardia"/>
							
					</td>
					
					<td class="labelText">
						<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.fechaRecepcion"/>
					
					</td>
					<td class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.fechaRecepcion}"/>
					</td>
					
				</tr>
				<tr>
	
					<td class="labelText">
						<siga:Idioma key="gratuita.seleccionColegiadoJG.literal.colegiado" />
					</td>
					<td class="labelTextValor" colspan="2" >
						<c:out value="${SolicitudAceptadaCentralitaForm.colegiadoNumero}"/>&nbsp;<c:out value="${SolicitudAceptadaCentralitaForm.colegiadoNombre}"/>
						
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia" />
					</td >
					<td colspan="2" class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.nombreGuardia}"/>
						
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.centroDetencion" />
					</td>
											
					<td colspan="5" class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.nombreCentroDetencion}"/>
					</td>
					
				</tr>
				
			</table>				
		</siga:ConjCampos>
	
	
	<c:choose>
		<c:when test="${(SolicitudAceptadaCentralitaForm.idEstado=='0' && SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada')||
		(SolicitudAceptadaCentralitaForm.idEstado=='1' && SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada')}">
			<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.dasistencia">
				<table width="100%" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno" />&nbsp;(*)
						</td>
						<td >
							<siga:Select queryId="getTurnos" id="idTurno" queryParamId="idturno" childrenIds="idGuardia" selectedIds="${idTurnoSelected}"  required="true" width="300" cssClass="${estiloSelect}" disabled="${disabledSelect}" />
							
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia" />&nbsp;(*)
						</td>
						<td>
							<siga:Select queryId="getGuardiasDeTurno" id="idGuardia"  parentQueryParamIds="idturno" queryParamId="idGuardia" params="${paramsGuardiasDeTurno}" selectedIds="${idGuardiaSelected}" required="true" width="300" childrenIds="idPersona" cssClass="${estiloSelect}" disabled="${disabledSelect}"/>
						</td>
					</tr>
					<c:if	test="${SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada'}">
						<tr>
						
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaAsistencias.literal.tipoAsistenciaColegio"/>&nbsp;(*)
							</td>
													
							<td colspan="3">
								
								<siga:Select queryId="getTiposAsistenciaDeColegio" id="idTipoAsistenciaColegio" selectedIds="${idTipoAsitenciaSelected}" width="498" cssClass="${estiloSelect}" disabled="${disabledSelect}"/>
							
								
							</td>
						</tr>
					</c:if>
					<tr>
					
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria" />
						</td>
												
						<td id="tdSelectComisaria" colspan="3">
							<siga:Select id="idComisaria" 
										queryId="getComisariasDeInstitucion" 
										params="${parametrosComboComisaria}"
										showSearchBox="true"
										searchkey="CODIGOEXT"
										searchBoxMaxLength="10"
										searchBoxWidth="8"
										width="420"
										selectedIds="${idComisariaSelected}"
										cssClass="${estiloSelect}"
										disabled="${disabledSelect}"
										/>
						
						
							
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
						</td>
											
						<td id="tdSelectJuzgado" colspan = "3">
						
							<siga:Select id="idJuzgado" 
										queryParamId="idjuzgado"
										queryId="getJuzgadosTurnos"
										params="${paramJuzgadosTurnos}"
										showSearchBox="true"
										searchkey="CODIGOEXT2"
										searchBoxMaxLength="10"
										searchBoxWidth="8"
										width="420"
										selectedIds="${idJuzgadoSelected}"
										cssClass="${estiloSelect}"
										disabled="${disabledSelect}"
										/>
						</td>
					</tr>
					

					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.seleccionColegiadoJG.literal.colegiado" />(*)&nbsp;
						</td>
						<td colspan = "3">
							<siga:Select queryId="getColegiadosGuardia" id="idPersona" parentQueryParamIds="idGuardia"  params="${parametrosComboColegiadosGuardia}" selectedIds="${idColegiadoGuardiaSelected}" required="true" />
						</td>
					</tr>
				</table>
			</siga:ConjCampos>

			<siga:ConjCampos leyenda="sjcs.solicitudaceptadacentralita.leyenda.datosSolicitante">
				<table width="100%" cellpadding="2" cellspacing="0" border="0">
					<tr>
						<html:hidden property="solicitanteIdTipoPersona" value = 'F'/>
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.tipoIdentificacion"/>
						</td>	
						
						

						<td>			
							<siga:Select queryId="getTiposIdentificacion" id="solicitanteIdTipoIdentificacion"  width="160px" />
						</td>
						<td class="labelText">
							<html:text property="solicitanteNumeroIdentificacion" size="10" maxlength="20" styleClass="box" />
						</td>

						<td class="labelText" colspan = "3">
							<div class="labelText" id="textoInformativo">
								<siga:Idioma key="gratuita.personaJG.literal.requiereNifCif"/>
							</div>
						</td>
					</tr>

					<tr>
						<td  class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.nombre"/>
						
							
						</td>

						<td >
							<html:text  property="solicitanteNombre" maxlength="100" styleClass="box"   style="width:160px" />
						</td>
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.apellidos"/>
							
						</td>
						<td >
							<div class="labelText">
								<html:text property="solicitanteApellido1" maxlength="100" styleClass="box"  style="width:120px" />
							</div>
						</td>
						<td>			
								<html:text property="solicitanteApellido2" maxlength="100" styleClass="box"style="width:120px" />
						</td>
						<td>
						</td>
					</tr>
				</table>
			
			<siga:ConjCampos leyenda="gratuita.personaJG.literal.direccion">
				<table width="100%" cellpadding="2" cellspacing="0" border="0">
					<tr>
						<td class="labelText" width="107px">
							<siga:Idioma key="gratuita.personaJG.literal.tipovia"/>
						</td>			

			
						<td>	
							<siga:Select queryId="getTiposVia" id="solicitanteTipoVia"/>
						 
						
						</td>
			
						<td class="labelText" width="82px">
							<siga:Idioma key="gratuita.personaJG.literal.direccion"/>
							
						</td>						
						<td width="200px">		
							<html:text property="solicitanteDireccion" maxlength="100" styleClass="box" style="width:190px" />
						</td>
						
						<td class="labelText" width="25px">
							<siga:Idioma key="gratuita.personaJG.literal.numdir"/>
						</td>						
						<td width="35px">		
							<html:text  property="solicitanteNumero" styleClass="box"  style="width:30px" maxlength="5" />
						</td>
						
						<td class="labelText" width="30px">
							<siga:Idioma key="gratuita.personaJG.literal.escdir"/>
						</td>						
						<td width="45px">		
							<html:text  property="solicitanteEscalera"  styleClass="box"  style="width:40px" maxlength="10" />
						</td>
						
						<td class="labelText" width="30px">
							<siga:Idioma key="gratuita.personaJG.literal.pisodir"/>
						</td>
						<td width="35px">		
							<html:text property="solicitantePiso" styleClass="box" style="width:30px" maxlength="5" />
						</td>
						
						<td class="labelText" width="45px">
							<siga:Idioma key="gratuita.personaJG.literal.puertadir"/>
						</td>
						<td width="35px">		
							<html:text  property="solicitantePuerta" styleClass="box"  style="width:30px" maxlength="5" />
						</td>																							
					</tr>				
					
					<tr>	
						
						
						<td class="labelText">
							<siga:Idioma key="censo.datosDireccion.literal.pais2" />&nbsp;
						</td>
						
						<td >
							<siga:Select queryId="getPaises" id="solicitantePais"/>
						</td>
						

						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.provincia"/>	

						</td>
						<td>
			
		  
						<siga:Select id="solicitanteProvincia" 
										queryParamId="idprovincia" 
										queryId="getProvincias"
										childrenIds="solicitantePoblacion"/>
		 
						</td>
						
						<td colspan="6">
							<table width="100%" cellpadding="0" cellspacing="0" border="0">			
								<tr>						
									<td class="labelText">
	 									<siga:Idioma key="gratuita.personaJG.literal.poblacion"/>	
									</td>
									<td>
		   
									<td id="poblacionEspanola" style="display: none;" width="20px">
										<siga:Select id="solicitantePoblacion"
											queryParamId="idpoblacion"
											queryId="getPoblacionesDeProvincia"
											parentQueryParamIds="idprovincia"/>
									</td>
									<td style="display: none" id="poblacionExtranjera" width="20px">
										<html:text		 property="solicitantePoblacionExt" size="25"
											styleClass="box"></html:text>
									</td>
		   
									
								</tr>
							</table>
						</td>
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.cp"/>	
									
						</td>
						<td>
							<html:text  property="solicitanteCodPostal" size="5" maxlength="5" styleClass="box"  />
						</td>
					</tr>
					
				</table>
			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.personaJG.literal.Contacto">
					<table width="100%" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td class="labelText" >
								<siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>						
							</td>										
							<td class="labelTextValor">													
								<html:text  property="solicitanteCorreoElectronico" maxlength="50" style="width:310px"  styleClass="box"  />
							</td>	
							<td >
								
							</td>							
							<td class="labelText" align="center">
								<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.numeroTelefono"/>
							</td>
							<td class="labelTextValor" >
								<html:text property="solicitanteTelefono" maxlength="20" style="width:150px" styleClass="box" />								
							</td>	
										
						
							<td class="labelText" align="center">
								<siga:Idioma key="censo.preferente.fax"/>								
							</td>
							<td class="labelTextValor" >
								<html:text  property="solicitanteFax" maxlength="20" style="width:150px" styleClass="box" />								
							</td>	
						</tr>
					</table>
				</siga:ConjCampos>   
			
			</siga:ConjCampos>
			<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"  />
		</c:when>
		<c:when test="${SolicitudAceptadaCentralitaForm.idEstado=='1'}">
			<siga:Table 		   
		   name="listadoInicial"
		   border="1"
		   columnNames="sjcs.solicitudaceptadacentralita.asistencia, gratuita.seleccionColegiadoJG.literal.colegiado,gratuita.busquedaAsistencias.literal.guardia,
		   sjcs.solicitudaceptadacentralita.literal.fechaGuardia,gratuita.busquedaAsistencias.literal.asistido,"
		   columnSizes="8,22,22,10,22,12">

			<c:choose>
				<c:when test="${empty solicitudesAceptadasCentralita}">
					<tr class="notFound">
			   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				</c:when>
				<c:otherwise>
					
					<c:forEach items="${solicitudesAceptadasCentralita}" var="solicitudAceptadaCentralita" varStatus="status">
						<siga:FilaConIconos	fila='${status.count}'
			  				botones="C,E" 
			  				pintarEspacio="no"
			  				visibleConsulta="si"
			  				visibleEdicion = "si"
			  				visibleBorrado = "no"
			  				clase="listaNonEdit">
		 					<td align='left'>
		 						<input type="hidden" id="idSolicitudAceptada_${status.count}" value="${solicitudAceptadaCentralita.idSolicitudAceptada}">
		 						<input type="hidden" id="asistenciaAnio_${status.count}" value="${solicitudAceptadaCentralita.asistenciaAnio}">
		 						<input type="hidden" id="asistenciaNumero_${status.count}" value="${solicitudAceptadaCentralita.asistenciaNumero}">
								<input type="hidden" id="idInstitucion_${status.count}" value="${solicitudAceptadaCentralita.idInstitucion}">
								
								<c:out value="${solicitudAceptadaCentralita.asistenciaDescripcion}"></c:out>
								
							</td>
							<td align='left'>
								
								<c:out value="${solicitudAceptadaCentralita.descripcionColegiado}"></c:out>
								
							</td>
							<td align='left'><c:out value="${solicitudAceptadaCentralita.nombreGuardia}"></c:out></td>
							<td align='center'><c:out value="${solicitudAceptadaCentralita.fechaGuardia}"></c:out></td>
							<td align='left'><c:out value="${solicitudAceptadaCentralita.solicitanteDescripcion}"></c:out></td>
			
							
						</siga:FilaConIconos>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			
			
			</siga:Table>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		</c:when>
		<c:when test="${SolicitudAceptadaCentralitaForm.idEstado=='2'}">
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		</c:when>
		<c:otherwise>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		</c:otherwise>
		
	</c:choose>
	
</html:form>
<html:form action="/JGR_Asistencia.do" method="post" target="mainWorArea"  style="display:none">
	<html:hidden property="modo"/>
	<html:hidden property="esFichaColegial" id="esFichaColegial"  value="false" />
	<html:hidden property="anio"/>
	<html:hidden property="numero"/>
	<html:hidden property="idInstitucion"/>
	<html:hidden property="solicIdentCentralita"/>
	<html:hidden property="jsonVolver" value="${SolicitudAceptadaCentralitaForm.jsonVolver}"/>
	
</html:form>


<script type="text/javascript">
	
	function consultar(fila,id){
		var asistenciaAnio = 'asistenciaAnio_'+fila;
		var asistenciaNumero = 'asistenciaNumero_'+fila;
		var idInstitucion = 'idInstitucion_'+fila;
		var idSolicitudAceptada = 'idSolicitudAceptada_'+fila;
		
		document.forms['AsistenciasForm'].anio.value =  document.getElementById(asistenciaAnio).value;
		document.forms['AsistenciasForm'].numero.value = document.getElementById(asistenciaNumero).value;
		document.forms['AsistenciasForm'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['AsistenciasForm'].solicIdentCentralita.value = document.getElementById(idSolicitudAceptada).value;
		document.forms['AsistenciasForm'].modo.value = "ver";
		document.forms['AsistenciasForm'].target = "mainWorkArea";
		document.forms['AsistenciasForm'].submit();
	}

	function editar(fila,id){
		var asistenciaAnio = 'asistenciaAnio_'+fila;
		var asistenciaNumero = 'asistenciaNumero_'+fila;
		var idInstitucion = 'idInstitucion_'+fila;
		var idSolicitudAceptada = 'idSolicitudAceptada_'+fila;
		document.forms['AsistenciasForm'].anio.value = document.getElementById(asistenciaAnio).value;
		document.forms['AsistenciasForm'].numero.value = document.getElementById(asistenciaNumero).value;
		document.forms['AsistenciasForm'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['AsistenciasForm'].solicIdentCentralita.value = document.getElementById(idSolicitudAceptada).value;
		document.forms['AsistenciasForm'].modo.value = "editar";
		document.forms['AsistenciasForm'].target = "mainWorkArea";
		document.forms['AsistenciasForm'].submit();
	}
</script>
	 	
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />

	
</body>
</html>
