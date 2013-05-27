<!-- consultaGuardiasInscripcion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
</head>

<body onload="activarDesactivarCheck();">
<script>
jQuery.noConflict();
	function accionSiguiente() {	
		sub();
		var guardiasAlta="";
	    var ele = document.getElementsByName("chkGuardia");
		var guardia="";		
		for (i = 0; i < ele.length; i++) {
			if(ele[i].checked) {				 
				guardia=document.getElementById("guardia_"+i);					
		   		guardiasAlta+=guardia.value+"@";
		   	}
		}		
		document.InscripcionTGForm.guardiasSel.value=guardiasAlta;
		document.InscripcionTGForm.target="_self";
		document.InscripcionTGForm.submit();				
	}
	
	function accionCancelar() {	
		window.top.close();	
	}
	
	function marcarDesmarcarTodos(o,deshabilitarSinRestriccion) { 
		if(document.getElementById("guardiasTodos")){
			document.getElementById("guardiasTodos").checked=o.checked;
			document.getElementById("guardiasNinguno").checked=!o.checked;
	  	}
		
		var ele = document.getElementsByName("chkGuardia");
		for (i = 0; i < ele.length; i++) {
			ele[i].checked = o.checked;
			
				
		}
		if(deshabilitarSinRestriccion){
		   	jQuery("input[name=chkGuardia]").attr("disabled","disabled");
		} else {
			if(document.InscripcionTGForm.tipoGuardias.value==0|| document.InscripcionTGForm.tipoGuardias.value==1){ 
			  jQuery("input[name=chkGuardia]").attr("disabled","disabled");
			} else {
				jQuery("input[name=chkGuardia]").removeAttr("disabled");				 
			}
		}	
		
		
		
		
		
		
	}
	
   	function marcarTodos(){
    	var eleGeneral = document.getElementById("chkGeneral");
		eleGeneral.checked=true;
		if(document.getElementById("guardiasTodos")){
			document.getElementById("guardiasTodos").checked=true;
			document.getElementById("guardiasNinguno").checked=false;
		}
	  marcarDesmarcarTodos(eleGeneral);		
	}
   	
  	function desmarcarTodos(habilitar){
	     var eleGeneral = document.getElementById("chkGeneral");
		 eleGeneral.checked=false;
		 if(document.getElementById("guardiasTodos")){
			 document.getElementById("guardiasTodos").checked=false;
		  	 document.getElementById("guardiasNinguno").checked=true;
	  	 }		 
		 marcarDesmarcarTodos(eleGeneral);
	}
  	
	function activarDesactivarCheck(){
   		if((!document.InscripcionTGForm.fechaSolicitud || document.InscripcionTGForm.fechaSolicitud.value=='')&&document.InscripcionTGForm.idGuardia.value==''){//alta
   			if(document.InscripcionTGForm.tipoGuardias.value==0){//obligatorias
   				jQuery("#chkGeneral").attr("disabled","disabled");
 	  			marcarTodos();
   			} else if(document.InscripcionTGForm.tipoGuardias.value==1) {
   				jQuery("#chkGeneral").removeAttr("disabled");				 
   				if(document.getElementById("guardiasTodos")){
   					jQuery("#guardiasTodos").removeAttr("disabled");				 
   					jQuery("#guardiasNinguno").removeAttr("disabled");				 
	  			}
				desmarcarTodos();   			
   			} else if(document.InscripcionTGForm.tipoGuardias.value==2){
   				jQuery("#chkGeneral").removeAttr("disabled");				 
   				desmarcarTodos();
   			}
 		} else {
 			jQuery("#chkGeneral").attr("disabled","disabled");
 			if(document.getElementById("guardiasTodos")){
 				jQuery("#guardiasTodos").attr("disabled","disabled");
 				jQuery("#guardiasNinguno").attr("disabled","disabled");
	  		}
 			var eleGeneral = document.getElementById("chkGeneral");
	  		eleGeneral.checked=true;
	  		marcarDesmarcarTodos(eleGeneral,true);
   		}
   	}

</script>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}" method="POST"	target="_top">
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
		<c:if test="${InscripcionTGForm.modo=='sitDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.turno.solicitud.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sigDatos'}">
		<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitud.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vitDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.turno.validar.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vigDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.guardia.validar.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sbtDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.turno.solicitudBaja.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sbgDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitudBaja.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vbtDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.turno.validarBaja.guardias' />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vbgDatos'}">
			<siga:Idioma key='gratuita.gestionInscripciones.paso.inscripcion.guardia.validarBaja.guardias' />
		</c:if>
		</td>
	</tr>
</table>

<html:hidden property="modo"/>
<html:hidden property="tipoGuardias"/>
<html:hidden property="fechaSolicitud"/>
<html:hidden property="idGuardia"/>
<html:hidden property="porGrupos"/>
<html:hidden property="guardiasSel"/>

	<siga:Table 
			name="altaTurno" 
			border="2"
			columnNames="<input type='checkbox' id='chkGeneral' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>,gratuita.altaTurnos_2.literal.nombre,gratuita.altaTurnos_2.literal.nletrados,gratuita.altaTurnos_2.literal.tipodias,gratuita.altaTurnos_2.literal.diasguardia,Acciones"
			columnSizes="5,30,15,20,15,20">

			<logic:notEmpty name="InscripcionTGForm"
				property="inscripcionesGuardia">
				<logic:iterate name="InscripcionTGForm"
					property="inscripcionesGuardia" id="inscripcionGuardia"
					indexId="index">
					
					<tr>
					<siga:FilaConIconos fila='<%=String.valueOf(index.intValue())%>'
						botones=""  clase="listaNonEdit" pintarEspacio="false">
						<td align="center">
							<input type="hidden" id="guardia_<%=String.valueOf(index)%>" name="guardia_<%=String.valueOf(index)%>" value="${inscripcionGuardia.guardia.idGuardia}" />
					   		<input type="checkbox" disabled="" value="<%=String.valueOf(index)%>"  name="chkGuardia" /> 						
 						</td>
						<td  ><c:out
							value="${inscripcionGuardia.guardia.nombre}"></c:out></td>
						<td ><c:out
							value="${inscripcionGuardia.guardia.numeroLetradosGuardia}"></c:out></td>
						<td ><c:out
							value="${inscripcionGuardia.guardia.seleccionTiposDia}"></c:out>
						</td>
						<td><c:out
							value="${inscripcionGuardia.guardia.diasGuardia}"></c:out>&nbsp;<siga:Idioma
							key="${inscripcionGuardia.guardia.descripcionTipoDiasGuardia}" />
							
						</td>

					</siga:FilaConIconos>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</siga:Table>
</html:form>
<div style="position: absolute; bottom: 5px; left: 0px; width: 100%; z-index: 99;">
<table width="100%">
	<tr>

		<c:choose>
			<c:when test="${InscripcionTGForm.tipoGuardias==0}">
				<td class="labelText">
					<input type="radio" id="guardias" name="guardias" checked> 
					<siga:Idioma key="gratuita.maestroTurnos.literal.guardias.obligatorias" />
				</td>
			</c:when>
			<c:when test="${InscripcionTGForm.tipoGuardias==1}">
				<td class="labelText">
					<input type="radio" id="guardiasTodos" name="guardiasTodos" value="0" onClick="marcarTodos()"> 
					<siga:Idioma key="gratuita.altaTurnos_2.literal.todas" /> &nbsp; 
					
					<input type="radio" id="guardiasNinguno" name="guardiasNinguno" value="1" checked onClick="desmarcarTodos()"> 
					<siga:Idioma key="gratuita.altaTurnos_2.literal.ninguna" /></td>
				</td>
			</c:when>
			<c:otherwise>
				<td class="labelText">
					<input type="radio" id="guardias" name="guardias" checked > 
					<siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion" />
				</td>
			</c:otherwise>
		</c:choose>

	</tr>
</table>
</div>



<div style="position:absolute;bottom:10px;left: 0px;width:100%;">

		<c:choose>
			<c:when test="${InscripcionTGForm.modo=='sitDatos'||InscripcionTGForm.modo=='sigDatos'||InscripcionTGForm.modo=='vitDatos'||InscripcionTGForm.modo=='vigDatos'}">
				<p class="labelText" style="text-align:center">
					<siga:Idioma key="gratuita.gestionInscripciones.literal.guardias.alta" />
				</p>
			</c:when>
			<c:when test="${InscripcionTGForm.modo=='sbtDatos'||InscripcionTGForm.modo=='sbgDatos'||InscripcionTGForm.modo=='vbtDatos'||InscripcionTGForm.modo=='vbgDatos'}">
				<p class="labelText" style="text-align:center">
					<siga:Idioma key="gratuita.gestionInscripciones.literal.guardias.baja" />
				</p>
			</c:when>
		</c:choose>		
			
		
</div>
		<siga:ConjBotonesAccion ordenar="false" botones="X,S"  />	

</body>

</html>


