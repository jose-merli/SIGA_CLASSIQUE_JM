<!-- validarInscripcion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- JSP -->


<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaTurnos.literal.title" /></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	
	<script type="text/javascript">		
		function accionCerrar() {		
			window.top.close();
		}		
	</script>
</head>

<body>


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.gestionInscripciones.consulta.titulo" />
		</td>
	</tr>
</table>


<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
<!-- INICIO: CAMPOS -->
<!-- Zona de campos de busqueda o filtro -->


<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudAlta">

	<table width="100%" border="0">
		<tr>
			<td width="25%"></td>
			<td width="74%"></td>



		</tr>
		<tr>
			<!-- obtenemos los campos para el alta de turnos -->
			<td class="labelText"><siga:Idioma
				key="gratuita.altaTurnos.literal.fsolicitud" /></td>
			<td class="labelText"><c:out
				value="${InscripcionTGForm.fechaSolicitudJsp}">
			</c:out></td>

		</tr>
		<tr>


			<td class="labelText"><siga:Idioma
				key="gratuita.altaTurnos.literal.osolicitud" /></td>
			<td><html:textarea name="InscripcionTGForm"
				property="observacionesSolicitud" cols="65" rows="2"
				onkeydown="cuenta(this,1024);" styleClass="box"
				style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
			</td>
		</tr>
		<c:if test="${InscripcionTGForm.fechaValidacion!=null}">
		
		
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.altaTurnos.literal.fvalidacion" /></td>
				<td class="labelText"><c:out
					value="${InscripcionTGForm.fechaValidacionJsp}">
				</c:out></td>
			</tr>
			<tr>
		
	
				<td class="labelText"><siga:Idioma
					key="gratuita.altaTurnos.literal.ovalidacion" /></td>
				<td><html:textarea name="InscripcionTGForm"
					property="observacionesValidacion" onChange="cuenta(this,1024)"
					cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box"
					style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
				</td>
	
			</tr>
		</c:if>
		<c:if test="${InscripcionTGForm.fechaDenegacion!=null&&InscripcionTGForm.fechaSolicitudBaja==null}">
		
		
			<tr>
				<td class="labelText">Fecha Denegacion</td>
				<td class="labelText"><c:out
					value="${InscripcionTGForm.fechaDenegacionJsp}">
				</c:out></td>
			</tr>
			<tr>
		
	
				<td class="labelText">Obs. denegacion</td>
				<td><html:textarea name="InscripcionTGForm"
					property="observacionesDenegacion" onChange="cuenta(this,1024)"
					cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box"
					style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
				</td>
	
			</tr>
		</c:if>
	</table>

</siga:ConjCampos>
<c:if test="${InscripcionTGForm.fechaSolicitudBaja!=null&&InscripcionTGForm.fechaSolicitudBaja!=''}">
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudBaja">
		<table width="100%" border="0">
			<tr>
				<td width="25%"></td>
				<td width="75%"></td>
	
			</tr>
			<tr>
				<!-- obtenemos los campos para el alta de turnos -->
				<td class="labelText"><siga:Idioma
					key="gratuita.altaTurnos.literal.fsolicitud" /></td>
				<td class="labelText"><c:out
					value="${InscripcionTGForm.fechaSolicitudBajaJsp}"></c:out></td>
	
	
			</tr>
			<tr>
	
				<td class="labelText"><siga:Idioma
					key="gratuita.altaTurnos.literal.mbaja" /></td>
				<td><html:textarea name="InscripcionTGForm"
					property="observacionesBaja" onChange="cuenta(this,1024)" cols="65"
					rows="2" onkeydown="cuenta(this,1024);" styleClass="box"
					style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
				</td>
			</tr>
			<c:if test="${InscripcionTGForm.fechaDenegacion!=null&&InscripcionTGForm.fechaSolicitudBaja!=null}">
		
		
			<tr>
				<td class="labelText">Fecha Denegacion</td>
				<td class="labelText"><c:out
					value="${InscripcionTGForm.fechaDenegacionJsp}">
				</c:out></td>
			</tr>
			<tr>
		
	
				<td class="labelText">Obs. denegacion</td>
				<td><html:textarea name="InscripcionTGForm"
					property="observacionesDenegacion" onChange="cuenta(this,1024)"
					cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box"
					style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
				</td>
	
			</tr>
		</c:if>
			
			<c:if test="${InscripcionTGForm.fechaBaja!=null}">
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.altaTurnos.literal.fabaja" /></td>
				<td class="labelText"><c:out
					value="${InscripcionTGForm.fechaBajaJsp}">
				</c:out></td>
	
			</tr>
			<tr>
		
	
				<td class="labelText">Obs. Baja</td>
				<td><html:textarea name="InscripcionTGForm"
					property="observacionesValBaja" onChange="cuenta(this,1024)"
					cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box"
					style="overflow:auto;width=400;height=58" readonly="true"></html:textarea>
				</td>
	
			</tr>
			</c:if>
	
		</table>
	
	</siga:ConjCampos>
</c:if>
<siga:ConjBotonesAccion botones="C"/>
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea"
	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
