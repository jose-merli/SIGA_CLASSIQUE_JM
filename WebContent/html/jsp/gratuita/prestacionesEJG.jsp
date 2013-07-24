<!DOCTYPE html>
<html>
<head>
<!-- operarDictamenEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>
<!-- IMPORTS -->

<!-- JSP -->



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
</head>
<body onload="inicio();">
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}" target="mainPestanas">
	<html:hidden property="modo"  />
	<html:hidden property="ejgIdInstitucion" />
	<html:hidden property="ejgIdTipo" />
	<html:hidden property="ejgAnio" />
	<html:hidden property="ejgIdTipo" />
	<html:hidden property="ejgNumero" />
	<html:hidden property="solicitante" />
	<html:hidden property="ejgNumEjg" />
	<html:hidden property="idsInsertarRechazadas" value = '' />
	<html:hidden property="idsBorrarRechazadas" value = ''/>

<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<c:out value="${PrestacionRechazadaEjgForm.ejgAnio}"/>/<c:out
			 	value="${PrestacionRechazadaEjgForm.ejgNumEjg}"/>&nbsp;-&nbsp;<c:out 
			 	value="${PrestacionRechazadaEjgForm.solicitante}"/>
		</td>
	</tr>
</table>
<c:forEach items="${prestacionesRechazadas}" var="prestacionRechazada" varStatus="status">
	<input type="hidden" id='${prestacionRechazada.prestacionId}' name='prestacionesRechazadasEJG'/>
</c:forEach>
<div id="campos" align="center" style="display: none;">

	<table align="center" width="100%" height="430"
		class="tablaCentralCampos">
		<tr>
			<td valign="top">
				<siga:ConjCampos		leyenda="Pretaciones solicitadas">

					<table align="center" width="100%" border="0">
						<c:choose>
   							<c:when test="${empty prestaciones}">
								<br>
	   		 						<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 							<br>
   							</c:when>
   							<c:otherwise>
   								<tr>
									<td width='10%'></td>
									<td width='80%'></td>
									<td width='10%'></td>
								</tr>
									<c:forEach items="${prestaciones}" var="prestacionEJG" varStatus="status">
										<c:set var="disabledPorConfiguracion" value="${prestacionEJG.habilitado=='0'?'disabled=disabled':'disabledPorConfiguracion' }"> </c:set>
									<tr>
										<td width='10%'></td>
										<td class="labelText">
											
											<input type='checkbox'  	id='prestacion_${prestacionEJG.idprestacion}' name='prestacion'  checked="checked" ${disabledPorConfiguracion} >
											<label for="prestacion_${prestacionEJG.idprestacion}"><c:out value="${prestacionEJG.descripcion}"/></label>
											
											</input>
										</td>
									</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
					</table>
			</siga:ConjCampos></td>
		</tr>
	</table>
<siga:ConjBotonesAccion botones="R,G" clase="botonesDetalle" />
</div>
</html:form>
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
<script type="text/javascript">

function refrescarLocal()
{
	document.PrestacionRechazadaEjgForm.target = 'mainPestanas';
	document.PrestacionRechazadaEjgForm.modo.value="abrir";
	document.PrestacionRechazadaEjgForm.submit();
}

function inicio()
{
	var prestRechazadas = document.getElementsByName('prestacionesRechazadasEJG');
	for ( i= 0; i < prestRechazadas.length; i++) {
		idPrestacionEJG = prestRechazadas[i].id.toString();
		
		document.getElementById("prestacion_"+idPrestacionEJG).checked="";
	}
}
function accionRestablecer() 
{	
	document.PrestacionRechazadaEjgForm.target = 'mainPestanas';
	document.PrestacionRechazadaEjgForm.modo.value="abrir";
	document.PrestacionRechazadaEjgForm.submit();
	
}
function accionGuardar()
{
	var idsInsertarRechazadas ='';
	var idsBorrarRechazadas ='';
	var prestARechazar = document.getElementsByName('prestacion');
	for ( i= 0; i < prestARechazar.length; i++) {
		idPrestacionEJG = prestARechazar[i].id;
		//el idPRestacion es prestacion_id por lo que hacemos split y cogemos el 2º[1]
		id =  prestARechazar[i].id.toString().split("_")[1];
		elementARechazar = document.getElementById(id);
		//Si exitia la prestacion rechazada y ahora esta aceptada la tenemos que borrar de la tabal de rechazadas
		if(document.getElementById(id)){
			if(document.getElementById(idPrestacionEJG).checked){
				idsBorrarRechazadas += id+",";
			}
		//si no existia la prestacion rechazada y ahora si se quiere rechazar hay aue insertarla en la tabla	
		}else{
			if(!document.getElementById(idPrestacionEJG).checked){
				idsInsertarRechazadas += id+",";
			}
		}
		
	}
	//borramos la ultima ,
	if(idsBorrarRechazadas!='')
		idsBorrarRechazadas = idsBorrarRechazadas.substring(0,idsBorrarRechazadas.length-1);
	if(idsInsertarRechazadas!='')
		idsInsertarRechazadas = idsInsertarRechazadas.substring(0,idsInsertarRechazadas.length-1);
	document.PrestacionRechazadaEjgForm.target = 'submitArea';
	document.PrestacionRechazadaEjgForm.idsInsertarRechazadas.value = idsInsertarRechazadas;
	document.PrestacionRechazadaEjgForm.idsBorrarRechazadas.value = idsBorrarRechazadas;
	document.PrestacionRechazadaEjgForm.submit();

}
jQuery(function(){
	jQuery("#campos").show();
});
</script>
</body>
</html>