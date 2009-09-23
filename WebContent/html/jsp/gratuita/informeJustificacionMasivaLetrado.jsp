<!-- informeJustificacionMasivaLetrado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<% 

	UsrBean usr=(UsrBean)session.getAttribute("USRBEAN");
	String letrado = ((String)request.getAttribute("letrado")==null)?"":(String)request.getAttribute("letrado");
	//String modoPestana = (String)request.getAttribute("MODOPESTANA");
	
	String anio = UtilidadesBDAdm.getYearBD("");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/js/validation.js"/>" type="text/javascript"></script>

	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.informeJustificacionMasiva.literal.titulo"  localizacion="gratuita.informeJustificacionMasiva.literal.localizacion"/>
	
</head>

<body onLoad="ajusteAlto('resultado');buscar();">

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->	
	<html:form action = "/JGR_PestanaDesignas.do" method="POST" target="resultado">

		<html:hidden property="modo" />
		

		<html:hidden property="letrado" value="<%=letrado%>"/>
		
		
		<table width="100%" border="0">
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.literal.fechasDesigna">
						<table>
							<tr>
								
								<td class="labelText">

									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fechaDesde"/>

								</td>
								<td>
									<html:text property="fechaDesde"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
						
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fechaHasta"/>
								</td>
								<td>
									<html:text property="fechaHasta"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.anio"/>
								</td>
								<td>
									<html:text property="anio"  size="4" styleClass="box" readOnly="false" value="<%=anio%>" maxlength="4"/>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.mostrarHistorico"/>
								</td>
								<td>	
								<html:checkbox property="mostrarTodas" onclick="buscar();"/>
									
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
				<tr>
					<td>
					<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.literal.cliente">
							<table width="100%">
							<tr>
								
								
								<td class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
								</td>
								<td>	
									<html:text  property="interesadoNombre" size="15" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.apellidos"/>
								</td>
								<td >	
									<html:text  property="interesadoApellidos" size="30" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td width="50%">
								&nbsp;
								</td>
		
							</tr>
							</table>
						</siga:ConjCampos>
					
					</td>
				
				</tr>
						
						


		</table>
		
		
	</html:form>

	<br>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- Formularios auxiliares para la busqueda de persona-->
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modo" value="abrirBusquedaModal"/>
		
	</html:form>


	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function buscar ()
		{

			if ( !validarObjetoAnio(document.getElementById("anio")) ){
				alert("<siga:Idioma key="fecha.error.anio"/>");
				return false;
			}
			
			sub();
			
			document.InformeJustificacionMasivaForm.modo.value = "buscarInit";
			document.InformeJustificacionMasivaForm.submit();
			
		}
		function buscarPaginador() 
		{
			document.forms[1].target			= "resultado";
			document.forms[1].modo.value 		= "buscarPor";
			document.forms[1].submit();
				
		}
	
	   function refrescarLocal(){
			
			buscar();
		}		

	</script>

	<siga:ConjBotonesBusqueda   botones="B"  titulo=""/>
	<iframe align="center" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	

	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
	
	
	
  </body>
  
</html>