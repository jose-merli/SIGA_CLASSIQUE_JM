<!DOCTYPE html>
<html>
<head>
<!-- busquedaTipoAsistenciaColegio.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
<siga:TituloExt	titulo="menu.sjcs.tiposAsistencia"	localizacion="sjcs.maestros.localizacion"/>

</head>
<body onLoad="ajusteAlto('resultado');buscar();" >
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
	<html:form action="${path}" method="POST" target="resultado">
		<html:hidden property="idTipoAsistenciaColegio"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="modo"/>
		<table class="tablaCentralCampos" align="center">
			<tr>
			<td>
			 <siga:ConjCampos leyenda="Criterios de búsqueda">
		  		<table class="tablaCentralCampos" align="center">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.descripcion"/>
					</td>
					<td class="labelText">
						<html:text name="TipoAsistenciaColegioForm" property="descripcion" size="60" maxlength="200" styleClass="box"  ></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.turno.guardia.literal.tipoGuardia"/>
					</td>
					
					<td class="labelText">
						<siga:Select id="tipoGuardia" selectedIds="${tiposGuardia}"
							queryId="getTiposGuardias"
							width="200"
							/>
					</td>
				
					<td class="labelText">
					<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil"/>
					</td>
					<td class="labelText" >
						<html:select property="visibleMovil"  styleClass="boxCombo" style="width:60px;">
							<html:option value="">&nbsp;</html:option>
							<html:option value="1"><siga:Idioma key="general.yes"/></html:option>
							<html:option value="0"><siga:Idioma key="general.no"/></html:option>
						</html:select>							
					</td>
					
				</tr>
				</table>
				</siga:ConjCampos>
              </td>
			</tr>
			
		</table>
	</html:form>

	<siga:ConjBotonesBusqueda botones="N,B"  titulo="menu.sjcs.tiposAsistencia" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
	
		<!-- Funcion asociada a boton buscar -->
	function buscar(){
		if(document.forms['TipoAsistenciaColegioForm'].modo.value!=''){
			sub();		
			document.forms['TipoAsistenciaColegioForm'].target = "resultado";
			document.forms['TipoAsistenciaColegioForm'].submit();
		}else{
			document.forms['TipoAsistenciaColegioForm'].modo.value ='buscar';
		}
	}		
		
	function nuevo(){
		document.forms[0].target="mainWorkArea";
		document.forms[0].modo.value = "nuevo";
		document.forms[0].submit();
	}
	
		
	</script>
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"	 
					class="frameGeneral"/>
	

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"/>
<!-- FIN: SUBMIT AREA -->

</body>
</html>