<!DOCTYPE html>
<html>
<head>
<!--editarTipoAsistenciaColegio.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="menu.sjcs.tiposAsistencia" 
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="onLoad();">
<html:javascript formName="TipoAsistenciaColegioForm" staticJavascript="false" />
<c:set var="disabled" value="false"/>

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
	<html:form action="${path}" name="formEdicion" type="com.siga.gratuita.form.TipoAsistenciaColegioForm" method="POST" target="submitArea">
	
	<c:if test="${formEdicion.modo=='ver'}">
		<c:set var="disabled" value="true"/>
	</c:if>
	
	<html:hidden name="formEdicion" property = "modo"/>
	<html:hidden name="formEdicion" property = "idInstitucion"/>	
	<html:hidden name="formEdicion"  property = "idTipoAsistenciaColegio"/>
	<html:hidden name="formEdicion"  property = "tipoGuardia"/>
	
	
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="menu.sjcs.tiposAsistencia">
			<table align="center" width="100%" >
			<tr>
				<td width="10%"></td>
				<td width="20%"></td>
				<td width="10%"></td>
				<td width="20%"></td>
				<td width="10%"></td>
				<td width="30%"></td>
			</tr>
			
				<tr>				
					<td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.descripcion"/>(*)
					</td>
					<td colspan="5">		
	  					<html:text name="formEdicion" styleId="descripcion" property="descripcion" size="100" styleClass="box" disabled="${disabled}" ></html:text>						
					</td>	
				</tr>
				<tr>	
					
					<td class="labelText">
						<siga:Idioma key="gratuita.confGuardia.asistencia.importe"/>(*)
					</td>
					<td >	
						<html:text name="formEdicion" styleId="importe" property="importe" styleClass="box" disabled="${disabled}"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.confGuardia.asistencia.importeMaximo"/>
					</td>
					<td >
						<html:text name="formEdicion" styleId="importeMaximo"  property="importeMaximo"  styleClass="box" disabled="${disabled}"></html:text>	
					</td>
				    <td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil"/>(*)
					</td>
					<td>
						<html:select name="formEdicion" styleId="visibleMovil" property="visibleMovil"  styleClass="boxCombo" disabled="${disabled}" style="width:100px;">
							<html:option value=""><siga:Idioma key="general.boton.seleccionar"/></html:option>
							<html:option value="1"><siga:Idioma key="general.yes"/></html:option>
							<html:option value="0"><siga:Idioma key="general.no"/></html:option>
						</html:select>							
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</siga:ConjCampos>
		<siga:ConjCampos leyenda="gratuita.turno.guardia.literal.tipoGuardia">		
			<table align="center" width="100%">
				<tr>
				<td width="10%"></td>
				<td width="20%"></td>
				<td width="10%"></td>
				<td width="20%"></td>
				<td width="10%"></td>
				<td width="30%"></td>
			</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.turno.guardia.literal.tipoGuardia"/>
					</td>
					<td>
						<siga:Select id="selectTiposGuardia"
							queryId="getTiposGuardias"
							selectedIds="${tiposGuardia}"
							multiple="true" 
							lines="10"
							width="200"
							required="true"
							disabled="${disabled}"
							/>
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</table>		
		</siga:ConjCampos>
	</html:form>
<html:form action="${path}" method="POST" target="submitArea">
	<html:hidden property = "modo"/>
	<html:hidden property = "tipoGuardia"/>
	<html:hidden property = "descripcion" />
	<html:hidden property = "visibleMovil" />
	<html:hidden property = "idInstitucion" value="${formEdicion.idInstitucion}"/>
	<html:hidden property = "idTipoAsistenciaColegio" value="${formEdicion.idTipoAsistenciaColegio}"/>
	
	
</html:form>
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ***** -->
<c:choose>		
	<c:when test="${formEdicion.modo=='ver'}">
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>
	</c:when>
	<c:otherwise>	
		<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle"/>
	</c:otherwise>
</c:choose>
	
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		//Refresco
		function refrescarLocal(){
			
			
			document.forms[1].target="mainWorkArea";
			document.forms[1].modo.value = "editar";
			document.forms[1].submit();
		}

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms['formEdicion'].reset();
		}
		
		//Asociada al boton Guardar -->
		
		function accionGuardar() 
		{
			
			document.forms.formEdicion.importeMaximo.value = replaceAll(document.forms.formEdicion.importeMaximo.value,'.',',');
			document.forms['formEdicion'].importeMaximo.value = replaceAll(document.forms['formEdicion'].importeMaximo.value,',','.');
			document.forms['formEdicion'].importe.value = replaceAll(document.forms['formEdicion'].importe.value,'.',',');
			document.forms['formEdicion'].importe.value = replaceAll(document.forms['formEdicion'].importe.value,',','.');
			error = ''
			if(document.forms['formEdicion'].importe.value.split('.').length>2){
				error += '<siga:Idioma key="errors.formato" arg0="gratuita.confGuardia.asistencia.importe" />'+'\n'; 
				
				
			}
			if(document.forms['formEdicion'].importeMaximo.value.split('.').length>2){
				error += '<siga:Idioma key="errors.formato" arg0="gratuita.confGuardia.asistencia.importeMaximo" />'+'\n';
			}
			if(error!=''){
				alert(error);
				return false;
			}
			sub();
			if (!validateTipoAsistenciaColegioForm(document.forms['formEdicion'])){
			   	 fin();	 
			     return false;
			}
			document.forms['formEdicion'].importeMaximo.value = replaceAll(document.forms['formEdicion'].importeMaximo.value,'.',',');
			document.forms['formEdicion'].importe.value = replaceAll(document.forms['formEdicion'].importe.value,'.',',');
			var tiposGuardia = document.getElementById("selectTiposGuardia").options;
			tiposGuardiaSeleccionados = "";
			for ( var i = 0; i < tiposGuardia.length; i++) {
				var tipoGuardia = tiposGuardia[i];
				if(tipoGuardia.selected){
					tiposGuardiaSeleccionados += tipoGuardia.value+'##';						
				}					
			}
			document.forms['formEdicion'].tipoGuardia.value= tiposGuardiaSeleccionados;
			document.forms['formEdicion'].submit();

		}			
		function accionVolver() 
		{	
			document.forms[1].modo.value="volver";
			document.forms[1].target="mainWorkArea"; 
			document.forms[1].submit();
		}
		function onLoad() 
		{
			jQuery('#selectTiposGuardia option[value=""]').remove();
			
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"/>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
