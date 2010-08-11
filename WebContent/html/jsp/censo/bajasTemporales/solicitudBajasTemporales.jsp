<!-- solicitudBajasTemporales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
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
<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	
	
</head>

<body>

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="censo.bajastemporales.solicitud.titulo"/>
		</td>
	</tr>
</table>


<html:javascript formName="BajasTemporalesForm" staticJavascript="true" />
<html:form action="/JGR_BajasTemporales" name="BajasTemporalesForm" type="com.siga.censo.form.BajasTemporalesForm">
	<html:hidden property="modo" />
	<html:hidden property="idInstitucion" />
	<html:hidden property="idPersona" />
	<html:hidden property="fechaAlta" />
	<html:hidden property="colegiadoNumero" />
	<html:hidden property="colegiadoNombre" />
	<html:hidden property="fichaColegial" />
	
	
	<html:hidden property="datosSeleccionados"/>
	<input type="hidden" name="actionModal" />

	<table width="100%" border="0">
		<tr>
			<td width="25%"></td>
			<td width="25%"></td>
			<td width="15%"></td>
			<td width="25%"></td>
			
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.bajastemporales.solicitud.letrado"/></td>

			<c:choose>
				<c:when test="${BajasTemporalesForm.fichaColegial==false&&BajasTemporalesForm.modo=='insertarNuevaSolicitud'}">
					<td colspan="3" class="labelText">Solicitud Masiva</td>

				</c:when>
				<c:otherwise>
					<td class="labelText"><c:out
						value="${BajasTemporalesForm.colegiadoNumero}"></c:out></td>
					<td colspan="2" class="labelText"><c:out
						value="${BajasTemporalesForm.colegiadoNombre}"></c:out></td>
				</c:otherwise>
			</c:choose>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			
		</tr>
		<tr>

			<c:choose>
				<c:when test="${BajasTemporalesForm.modo=='insertarNuevaSolicitud'}">
					<td class="labelText"  style="align:left"><siga:Idioma key="censo.bajastemporales.fechaInicio"/></td>
					<td><html:text property="fechaDesde" size="10" readonly="true"
						styleClass="box" />&nbsp; <a href='javascript://'
						onClick="return showCalendarGeneral(fechaDesde)"> <img
						src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
						border="0"> </a></td>
					<td class="labelText"><siga:Idioma key="censo.bajastemporales.fechaFin"/></td>
					<td><html:text property="fechaHasta" size="10" readonly="true"
						styleClass="box" />&nbsp; <a href='javascript://'
						onClick="return showCalendarGeneral(fechaHasta)"> <img
						src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
						border="0"> </a></td>

				</c:when>
				<c:otherwise>
					<td class="labelText"><siga:Idioma key="censo.bajastemporales.fechaInicio"/></td>
					<td><html:text property="fechaDesde" size="10" readonly="true"
						styleClass="box" /></td>
					<td class="labelText"><siga:Idioma key="censo.bajastemporales.fechaFin"/></td>
					<td><html:text property="fechaHasta" size="10" readonly="true"
						styleClass="box" /></td>
				</c:otherwise>
			</c:choose>

		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.bajastemporales.tipo"/></td>
			<td  align="left"><html:select property="tipo"
				styleClass="boxCombo"><html:option value="">
					<siga:Idioma key="general.combo.seleccionar" />
				</html:option>
				<html:option value="V">
					<siga:Idioma key="censo.bajastemporales.tipo.vacaciones" />
				</html:option>
				<html:option value="B">
					<siga:Idioma key="censo.bajastemporales.tipo.baja" />
				</html:option>
				<html:option value="M"><siga:Idioma key="censo.bajastemporales.tipo.maternidad"/></html:option>
				<html:option value="S"><siga:Idioma key="censo.bajastemporales.tipo.suspension"/></html:option>
			</html:select></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.bajastemporales.descripcion"/></td>
			<td align="left" colspan="3">
			
			
			<html:textarea name="BajasTemporalesForm" property="descripcion"  onchange="cuenta(this,1024)" cols="65" rows="2" style="overflow=auto;width=400;height=80" onkeydown="cuenta(this,1024);" styleClass="boxCombo"  readonly="false"></html:textarea>
			
			
			</td>
		</tr>
	</table>

</html:form>
<c:choose>
	<c:when test="${BajasTemporalesForm.modo=='consultarSolicitud'}">
		<siga:ConjBotonesAccion botones="C" modal="P" />

	</c:when>
	<c:otherwise>
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	</c:otherwise>
</c:choose>




<script type="text/javascript">
<!-- Asociada al boton GuardarCerrar -->
	
	function accionGuardarCerrar() 
		{		
			sub();
			if (validateBajasTemporalesForm(document.BajasTemporalesForm)){
				document.BajasTemporalesForm.submit();
			}else{
				fin();
			}
		}
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.BajasTemporalesForm.reset();
		}
</script>

</body>

</html>