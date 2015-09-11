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
		
		

<script type="text/javascript">
	
	function refrescarLocal(){
		buscarSolicitudesAceptadas();
	}

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
	
	
	 function onChangeTipoIdentificacion(valor){
						
		// Solo se genera el NIF o CIF de la persona
		if(valor== "10"){
			document.getElementById("textoInformativo").style.display="none";
		} else if ((valor== "20") || (valor== "40")){
			document.getElementById("textoInformativo").style.display="none";

		} else{
			document.getElementById("textoInformativo").style.display="block";
		}			
	}	
	function accionVolver() 
	{		
		document.forms['SolicitudAceptadaCentralitaForm'].modo.value="volver";
		if(jQuery('#fichaColegial').val()=='0') 
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainWorkArea';
		else
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainPestanas';
		
		document.forms['SolicitudAceptadaCentralitaForm'].submit();
	}
	function accionGuardar() 
	{
		sub();
		if (!validateSolicitudAceptadaCentralitaForm(document.SolicitudAceptadaCentralitaForm)){
			fin();
			return false;
		}
		
		if((document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNombre.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteApellido1.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteApellido2.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteTipoVia.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteDireccion.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitantePoblacion.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteProvincia.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteNumero.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteEscalera.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitantePiso.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitantePuerta.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCodPostal.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteCorreoElectronico.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteTelefono.value!="")||
		  (document.forms['SolicitudAceptadaCentralitaForm'].solicitanteFax.value!="")){
			
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
		
		if(jQuery('#fichaColegial').val()=='0') 
			document.forms['SolicitudAceptadaCentralitaForm'].target.value="submitArea";
		else
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainPestanas';
		
		document.forms['SolicitudAceptadaCentralitaForm'].modo.value="guardarSolicitudAceptada";
		
		document.getElementById('idTurno').disabled ='';
		document.getElementById('idGuardia').disabled='';
		document.getElementById('idTipoAsistenciaColegio').disabled ='';
		document.getElementById('idComisaria').disabled ='';
		document.getElementById('idJuzgado').disabled ='';
		document.getElementById('idPersona').disabled ='';
		
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
	function accionCalendario() {
		
		// Abrimos el calendario
		if (document.getElementById('fechaGuardia').value!='') {
			document.forms['SolicitudAceptadaCentralitaForm'].fechaGuardia.value = document.getElementById('fechaGuardia').value; 
			var fechaGuardia = document.getElementById('fechaGuardia').value;
			reloadSelect("idTurno", undefined, [{key:'fechaGuardia', value: fechaGuardia}]);
	 	}else{
			//Si da a reset no viene nada por lo que actualizamos. si viene con fecha
			//es que ha cerrado desde el aspa, lo dejamos como estuviera(no hacemos nada) 		 		
			if(document.getElementById('fechaGuardia').value==''){
			 	//document.getElementById('fechaGuardia').onchange();
				jQuery("#idTurno").val("");
				jQuery("#idGuardia").val("");
				jQuery("#idPersona").val("");
				
			}
		} 
	}
	
</script>
</head>
<c:choose> 
	<c:when test="${fichaColegial=='0'}">
		<siga:Titulo  titulo="${tituloLocalizacion}" localizacion="${localizacion}"/>
		<table class="tablaTitulo" cellspacing="0" height="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="${titulo}"/>
					
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td class="titulitosDatos"><siga:Idioma
						key="sjcs.solicitudaceptadacentralita.preasistencias" /> <c:out
						value="${SolicitudAceptadaCentralitaForm.descripcionColegiado}"></c:out>
					
				</td>
			</tr>
		</table>
	</c:otherwise>

</c:choose>
<body onload="inicio();">

	<c:set var="parametrosComboComisaria" value="{\"idcomisaria\":\"-1\"}"/>
	<c:set var="readonlyText" value="true" />
	<c:set var="disabledSelect" value="true" />
	<c:set var="displaynone" value="display:none" />
	<c:set var="estiloText" value="boxConsulta" />
	<c:set var="estiloSelect" value="boxComboConsulta" />
	<c:set var="botonesListadoAsistencias" value="" />
	<input type="hidden" id="mensajeSuccess" value="${mensajeSuccess}"/>
	<input type="hidden" id="fichaColegial" value="${fichaColegial}"/>
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:javascript formName="SolicitudAceptadaCentralitaForm" staticJavascript="false" />
	<html:form action="${path}"  method="POST" target="mainWorkArea">
		<input type="hidden" id ="idColegiadoGuardiaSeleccionado" name="idColegiadoGuardiaSeleccionado" value="${idColegiadoGuardiaSeleccionado}">
		<input type="hidden" id ="nombreColegiadoGuardiaSeleccionado" name="nombreColegiadoGuardiaSeleccionado" value="${nombreColegiadoGuardiaSeleccionado}">
		
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idSolicitudAceptada"/>
		<html:hidden property="idLlamada"/>
		<html:hidden property="jsonVolver"/>
		<html:hidden property="solicitantePais" value="191"/>
		<html:hidden property="fechaLlamadaHoras" value="${SolicitudAceptadaCentralitaForm.fechaLlamadaHoras}"/>
		
		<c:if	test="${fichaColegial=='0'}">
			<c:set var="estiloText" value="box" />
			<c:set var="estiloSelect" value="boxCombo" />
			<c:set var="displaynone" value="" />
			<c:set var="readonlyText" value="" />
			<c:set var="disabledSelect" value="" />
			<c:set var="botonesListadoAsistencias" value="C,E" />
			
					
		</c:if>
		
		
		

		<siga:ConjCampos leyenda="gratuita.gestionInscripciones.datosSolicitud.leyenda">
			<table width="100%" border="0">
				<tr>
					<td width="15"></td>
					<td width="35"></td>
					<td width="15"></td>
					<td width="35"></td>
					
					
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.idAvisoCentralita"/>
					</td>
					<td class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.numAvisoCV}"/>
							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
					</td>
					<td class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.descripcionEstado}"/>
							
					</td>
					</tr>
			<tr>
					<td class="labelText" >
						<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.fechaLlamada"/>
					</td>
					<td class="labelTextValor"> 
						<c:out value="${SolicitudAceptadaCentralitaForm.fechaLlamadaHoras}"/>
						
							
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
					<td class="labelTextValor" >
						<c:out value="${SolicitudAceptadaCentralitaForm.colegiadoNumero}"/>&nbsp;<c:out value="${SolicitudAceptadaCentralitaForm.colegiadoNombre}"/>
						
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia" />
					</td >
					<td colspan="2" class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.idGuardia}"/>&nbsp;<c:out value="${SolicitudAceptadaCentralitaForm.nombreGuardia}"/>
						
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.centroDetencion" />
					</td>
											
					<td colspan="3" class="labelTextValor">
						<c:out value="${SolicitudAceptadaCentralitaForm.idCentroDetencion}"/>&nbsp;<c:out value="${SolicitudAceptadaCentralitaForm.nombreCentroDetencion}"/>
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
							<siga:Idioma key='gratuita.busquedaAsistencias.literal.fechaAsistencia'/>&nbsp;(*)
						</td>
						<td colspan = "3">
							<siga:Fecha styleId="fechaGuardia" nombreCampo="fechaGuardia" valorInicial="${SolicitudAceptadaCentralitaForm.fechaGuardia}" disabled="${disabledSelect}" postFunction="accionCalendario();"/>
						</td>
					</tr>
				
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno" />&nbsp;(*)
						</td>
						<td >
							<siga:Select queryId="getTurnosConColegGuardia" id="idTurno" queryParamId="idturno" params="${paramsTurnos}"  childrenIds="idGuardia" selectedIds="${idTurnoSelected}"  required="true" width="300" cssClass="${estiloSelect}" disabled="${disabledSelect}" />
							
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia" />&nbsp;(*)
						</td>
						<td>
							<siga:Select queryId="getGuardiasConColegGuardia" id="idGuardia"  parentQueryParamIds="idturno" queryParamId="idGuardia" params="${paramsGuardiasDeTurno}" selectedIds="${idGuardiaSelected}" required="true" width="300" childrenIds="idPersona" cssClass="${estiloSelect}" disabled="${disabledSelect}"/>
						</td>
					</tr>

					<tr style="${displaynone}">
						<td class="labelText">
							<siga:Idioma key="gratuita.volantesExpres.literal.letradosGuardia" />(*)&nbsp;
						</td>
						<td colspan = "3" >
							<siga:Select queryId="getColegiadosGuardia" id="idPersona" parentQueryParamIds="idGuardia" width="350" params="${parametrosComboColegiadosGuardia}" selectedIds="${idColegiadoGuardiaSelected}" required="true" cssClass="${estiloSelect}" disabled="${disabledSelect}"/>
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
						<td class="labelText" >
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
							<siga:Idioma key="gratuita.personaJG.literal.provincia"/>	

						</td>
						<td>
			
						<siga:Select id="solicitanteProvincia" 
										queryParamId="idprovincia" 
										queryId="getProvincias"
										childrenIds="solicitantePoblacion"/>
		 
						</td>
						
						
								
											
						<td class="labelText" width="82px">
								<siga:Idioma key="gratuita.personaJG.literal.poblacion"/>	
						</td>
						
  
						<td width="200px">
							<siga:Select id="solicitantePoblacion"
								queryParamId="idpoblacion"
								queryId="getPoblacionesDeProvincia"
								parentQueryParamIds="idprovincia"/>
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
			  				botones="${botonesListadoAsistencias}" 
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
			
			
		<table class="botonesDetalle" align="center">
				<tr>
				<td class="tdBotones">
					<input type="button" alt="Volver"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="Volver">
				</td>	
				<td style="width: 900px;">
						&nbsp;
					</td>
					
					<c:if	test="${SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada'}">
						<td class="tdBotones">
							<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>"	id="idValidarSolicitudAceptada" onclick="return validarSolicitudAceptada();" class="button"   name="idButton" value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>" />
						</td>
					</c:if>
					
				</tr>
			</table>
		</c:when>
		<c:when test="${SolicitudAceptadaCentralitaForm.idEstado=='2'}">
			<table class="botonesDetalle" align="center">
				<tr>
					<td class="tdBotones">
						<input type="button" alt="Volver"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="Volver">
					</td>	
					<td style="width: 900px;">
						&nbsp;
					</td>
					
					<c:if	test="${SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada'}">
						<td class="tdBotones">
							<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.activarSolicitud'/>"  id="idActivarSolicitudAceptadaDenegada" onclick="return activarSolicitudAceptada();" class="button" name="idButton"    value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.activarSolicitud'/>">
						</td>
					</c:if>
						
					
				</tr>
			</table>
			
		</c:when>
		<c:otherwise>
			<table class="botonesDetalle" align="center">
				<tr>
					<td class="tdBotones">
						<input type="button" alt="Volver"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="Volver">
					</td>	
					<td style="width: 900px;">
						&nbsp;
					</td>
					
					<c:if test="${SolicitudAceptadaCentralitaForm.modo!='consultarSolicitudAceptada'}">
						<td class="tdBotones">
							<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.denegarSolicitud'/>"  id="idDenegarSolicitudAceptada" onclick="return denegarSolicitudAceptada();" class="button" name="idButton"   value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.denegarSolicitud'/>">
						</td>
						<td class="tdBotones">
							<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>"	id="idValidarSolicitudAceptada" onclick="return validarSolicitudAceptada();" class="button"   name="idButton" value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>" />
						</td>
					</c:if>
					
				</tr>
			</table>
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
	function activarSolicitudAceptada() {	
	 	document.forms['SolicitudAceptadaCentralitaForm'].modo.value = "activarSolicitudAceptadaDenegadaVolver";
	 	if(jQuery('#fichaColegial').val()=='1') 
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainPestanas';
	 	document.forms['SolicitudAceptadaCentralitaForm'].submit();
	}
	function denegarSolicitudAceptada() {	
		if (!confirm('<siga:Idioma key="sjcs.solicitudaceptadacentralita.mensaje.denegar"/>'))
			return false;
	 	document.forms['SolicitudAceptadaCentralitaForm'].modo.value = "denegarSolicitudAceptadaVolver";
	 	if(jQuery('#fichaColegial').val()=='1') 
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainPestanas';
	 	
	 	document.forms['SolicitudAceptadaCentralitaForm'].submit();
	}
	function validarSolicitudAceptada() {	
	 	document.forms['SolicitudAceptadaCentralitaForm'].modo.value = "validarSolicitudAceptada";
	 	if(jQuery('#fichaColegial').val()=='1') 
			document.forms['SolicitudAceptadaCentralitaForm'].target = 'mainPestanas';
	 	document.forms['SolicitudAceptadaCentralitaForm'].submit();
	}
	
	
	function inicio() {
		if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
			alert(document.getElementById("mensajeSuccess").value,'success');
		}
		idpersonaseleccionadacombo = jQuery("#idPersona").val();
		idpersonaaniadircombo = jQuery("#idColegiadoGuardiaSeleccionado").val();
		
		
		if(idpersonaseleccionadacombo!=idpersonaaniadircombo){
			nombreColegiadoGuardiaSeleccionado = '<siga:Idioma key="gratuita.literal.letrado.refuerzo"/>'+' '+jQuery("#nombreColegiadoGuardiaSeleccionado").val();
			jQuery("#idPersona").append(jQuery('<option>', {
			    value: idpersonaaniadircombo,
			    text: nombreColegiadoGuardiaSeleccionado,
			    selected:true
			}));
			
		}
		
	}	
	
</script>
	 	
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />

	
</body>
</html>
