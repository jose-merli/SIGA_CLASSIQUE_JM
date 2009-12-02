<!-- volantesExpress.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
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
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
</head>

<body>
	<table id='asistencias' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<logic:notEmpty name="VolantesExpressForm"	property="asistencias">
		
		<logic:iterate name="VolantesExpressForm" property="asistencias" id="asistencia" indexId="index">
				<input type="hidden" id="claveAnio_<bean:write name='index'/>" value="<bean:write name="asistencia" property="anio" />">  
				<input type="hidden" id="claveNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="numero" />">
				<input type="hidden" id="claveIdInstitucion_<bean:write name='index'/>" value="<bean:write name="asistencia" property="idInstitucion" />">
				<input type="hidden" id="ejgNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgNumero" />">
				<input type="hidden" id="ejgAnio_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgAnio" />">
				<input type="hidden" id="ejgTipo_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgIdTipoEjg" />">
				<input type="hidden" id="designaNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="designaNumero" />">
				
			<tr>
			
			
				<td align='center' width='6%'>
					<input type="text" id="hora_<bean:write name='index'/>" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="<bean:write name="asistencia" property="hora" />" onBlur="validaHora(this);" />
					:
					<input type="text" id="minuto_<bean:write name='index'/>" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="<bean:write name="asistencia" property="minuto" />" onBlur="validaMinuto(this);" />
				</td>
				
				<td align='center' width='27%'>
				
					<c:if test="${VolantesExpressForm.lugar == 'centro'}">
						<input type="text" id="codComisaria_<bean:write name='index'/>" class="box" size="8"  style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerComisaria(<bean:write name='index'/>);" /> 
				  		<iframe ID="comisaria_<bean:write name='index'/>Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=comisaria_<bean:write name='index'/>&tipo=comboComisariasTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<bean:write name="VolantesExpressForm" property="idTurno" />&id=<bean:write name="VolantesExpressForm" property="idInstitucion" />&<bean:write name="VolantesExpressForm" property="idTurno" />&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe>  
			      		<input type="hidden" id="comisaria_<bean:write name='index'/>" value="<bean:write name="asistencia" property="comisaria" />">
			      		<script>
			      			seleccionComboSiga ("comisaria_<bean:write name='index'/>", <bean:write name="asistencia" property="comisaria" />+','+<bean:write name="asistencia" property="idInstitucion" />);
			      		</script>
					</c:if>
					<c:if test="${VolantesExpressForm.lugar == 'juzgado'}">
			      		<input type="text" id="codJuzgado_<bean:write name='index'/>" class="box" size="8" style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerJuzgado(<bean:write name='index'/>);"/> 
				  		<iframe ID="juzgado_<bean:write name='index'/>Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=juzgado_<bean:write name='index'/>&tipo=comboJuzgadosTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<bean:write name="VolantesExpressForm" property="idTurno" />&id=<bean:write name="VolantesExpressForm" property="idInstitucion" />&<bean:write name="VolantesExpressForm" property="idTurno" />&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> 
			      		<input type="hidden" id="juzgado_<bean:write name='index'/>" value="<bean:write name="asistencia" property="juzgado" />">
			      		<script>
			      			seleccionComboSiga ("juzgado_<bean:write name='index'/>", <bean:write name="asistencia" property="juzgado" />+','+<bean:write name="asistencia" property="idInstitucion" />);
			      		</script>
			      						
					</c:if>	
										
				
				 </td>
				<td align='center' width='36%'>
					<input type="text" id="dni_<bean:write name='index'/>" class="box" style="width:70;margin-top:3px;" value="<bean:write name="asistencia" property="asistidoNif" />" maxlength="20" onBlur="obtenerPersona(<bean:write name='index'/>);" /> 
			        <input type="text" id="nombre_<bean:write name='index'/>" class="box" style="width:80;margin-top:3px;" value="<bean:write name="asistencia" property="asistidoNombre" />" maxlength="80"/>&nbsp;
			        <input type="text" id="apellido1_<bean:write name='index'/>" class="box" style="width:80;margin-top:3px;" value="<bean:write name="asistencia" property="asistidoApellido1" />" maxlength="80"/>&nbsp;
			        <input type="text" id="apellido2_<bean:write name='index'/>" class="box" style="width:80;margin-top:3px;" value="<bean:write name="asistencia" property="asistidoApellido2" />" maxlength="80"/>
			        <img id="info_existe_<bean:write name='index'/>" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>
			        <input type="hidden" id="idPersona_<bean:write name='index'/>" class="box" value="<bean:write name="asistencia" property="idPersonaJG" />"/>
			      	<script>
			      		ponerIconoIdentPersona (<bean:write name='index'/>, true);
			      	</script>
			        
				</td>
				<td align='center' width='9%'>
					<input type="text" id="diligencia_<bean:write name='index'/>" class="box" maxlength="20" style="width:70;margin-top:3px;" value="<bean:write name="asistencia" property="numeroDiligencia" />"/>
				</td>
				<td align='center' width='14%'>
				
				
				<c:if test="${VolantesExpressForm.delitos==true}">
								
					
					<iframe ID="idDelito_<bean:write name='index'/>Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=idDelito_<bean:write name='index'/>&tipo=comboDelitos&clase=boxCombo&estilo=width:215px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<bean:write name="VolantesExpressForm" property="idInstitucion" />&id=<bean:write name="VolantesExpressForm" property="idInstitucion" />&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe>   
					<input type="hidden" id="idDelito_<bean:write name='index'/>" value="<bean:write name="asistencia" property="delitosImputados" />">
					<script>
			      			seleccionComboSiga ("idDelito_<bean:write name='index'/>", '<bean:write name="asistencia" property="delitosImputados" />');
			      		</script>
					<input type="hidden" id="observaciones_<bean:write name='index'/>" value="">
				</c:if>
				
				<c:if test="${VolantesExpressForm.delitos==false}">
			
					<input type="text" id="observaciones_<bean:write name='index'/>" class="box" style="width:130;margin-top:3px;" value="<bean:write name="asistencia" property="observaciones" />"/>
					<input type="hidden" id="idDelito_<bean:write name='index'/>" value="">
				</c:if>
				
				
				</td>
				<td align='center' width='8%'>
					
					<img src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt='<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>' name="" border="0" onclick="accionNuevaActuacion(<bean:write name="asistencia" property="anio" />,<bean:write name="asistencia" property="numero" />,<bean:write name="asistencia" property="idInstitucion" />)">
					<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt='<siga:Idioma key="general.boton.borrar"/>' name="" border="0" onclick="borrarFila('<bean:write name='index'/>')">						
					
					<c:if test="${asistencia.ejgAnio!=null}">
						<input type="text" id="ejgNumEjg_<bean:write name='index'/>" size="10"  value="<bean:write name="asistencia" property="ejgNumEjg"/>" readOnly="readonly" class="box" />
					</c:if>
					<c:if test="${asistencia.ejgAnio==null}">
						<input type="button"  id = "idButton" value="EJG" alt='<siga:Idioma key="gratuita.volantesExpres.asociarEjg"/>'  class="buttonEnTabla" onclick="accionCrearEJG(<bean:write name="asistencia" property="anio" />,<bean:write name="asistencia" property="numero" />,<bean:write name="asistencia" property="idInstitucion" />,<bean:write name='index'/>);"/>
					</c:if>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
	<script type="text/javascript">
		var messageError="${VolantesExpressForm.msgError}";
		var messageAviso="${VolantesExpressForm.msgAviso}";
		if (messageAviso)
			alert(messageAviso);
		if (messageError)
			alert(messageError);
		
	</script>
</body>
<script>
</script>


</html>