<!-- modificacionCargoJunta.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.atos.utils.UsrBean"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript"
	src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
  
</head>
<body>
	<table class="tablaCampos" id='cargostabla' border='1' width='100%' cellspacing='0' cellpadding='0'>
	  <logic:notEmpty name="BusquedaComisionesForm"	property="comisiones">
		<logic:iterate name="BusquedaComisionesForm" id="comision"   property="comisiones" indexId="index">
				

				<c:choose>
					<c:when test="${index%2==0}">
						<tr id="fila_<bean:write name='index'/>" class="filaTablaPar">
					</c:when>
					<c:otherwise>
						<tr id="fila_<bean:write name='index'/>" class="filaTablaImpar">
					</c:otherwise>
				</c:choose>
				<td  align='center' width='10%'>
				<bean:write name="comision" property="fechaInicio" />
					<input type="hidden" name="IDCV_<bean:write name='index'/>" id="IDCV_<bean:write name='index'/>" value="<bean:write name="comision" property="idCV" />">
					<input type="hidden" name="idPersona_<bean:write name='index'/>" id="idPersona_<bean:write name='index'/>" value="<bean:write name="comision" property="idPersona" />">
					<input type="hidden" name="namecolegiado_<bean:write name='index'/>" id="namecolegiado_<bean:write name='index'/>" value="<bean:write name="comision" property="apellidos" />  <bean:write name="comision" property="nombre" />">
					<input type="hidden" name="ncolegiado_<bean:write name='index'/>" id="ncolegiado_<bean:write name='index'/>" value="<bean:write name="comision" property="numcolegiado" />">
				</td>
				<td  align='center' width='15%'>
					<bean:write name="comision" property="cargo" />
				</td>
				<td  align='center' width='15%'>
					<bean:write name="comision" property="numcolegiado" />
				</td>
				<td  align='center' width='30%'>
					<table> 
					<c:choose>
						<c:when test="${index%2==0}">
							<tr class="filaTablaPar">
						</c:when>
						<c:otherwise>
							<tr class="filaTablaImpar">
						</c:otherwise>
					</c:choose>
						<td width='90%'>
						<bean:write name="comision" property="apellidos" />
						<bean:write name="comision" property="nombre" /></td>
						<td  width='10%'>
						<img id="info_existe_<bean:write name='index'/>" src="/SIGA/html/imagenes/encontrado.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esYaExistentePersonaJG"/>"/>
						</td>
						</tr>
					</table>
				</td>
				<td  align='center' width='20%'>
					<c:if test="${comision.fechaFin == ''}">
				    <input type="checkbox" name="editar_<bean:write name='index'/>" id="editar_<bean:write name='index'/>">Finalizar cargo</td>
				    </c:if>
				    <c:if test="${comision.fechaFin != ''}">
				    <bean:write name="comision" property="fechaFin" />
				    </c:if>
				</td>
				<td align='center' width='10%'>
				<table><tr>
				<td>
					<img name="consultar_<bean:write name='index'/>" 
					       id="consultar_<bean:write name='index'/>"  
					      src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>" 
					      style="cursor:hand;" alt="Consultar"  border="0" 
					      onClick="selectRow(<bean:write name='index'/>);consultar(<bean:write name='index'/>);">
					<img name="editaCargo_<bean:write name='index'/>" id="edita_<bean:write name='index'/>" 
					src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>" style="cursor:hand;" alt="Editar" border="0" 
					onClick="selectRow(<bean:write name='index'/>);editar(<bean:write name='index'/>);">
					<img src="<html:rewrite page='/html/imagenes/bborrar_off.gif'/>" style="cursor:hand;" alt="<siga:Idioma key='general.boton.borrar'/>" 
					name="borradoLogico_<bean:write name='index'/>"   border="0" id="borradoLogico_<bean:write name='index'/>" border="0" 
					onclick="borradoLogicoFila(<bean:write name='index'/>,1)">
				</td></tr>
				</table>
			
				</td>														
			</tr>
		</logic:iterate>
		</logic:notEmpty>
		 <logic:empty name="BusquedaComisionesForm"	property="comisiones">
					<div id="vacio">
					<br><br>
			   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>
			 		</div>
		</logic:empty>	 
</table>

</body>
	<script type="text/javascript">
		var messageError='${BusquedaComisionesForm.msgError}';
		var messageAviso='${BusquedaComisionesForm.msgAviso}';
		
	</script>

</html>