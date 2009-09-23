<!-- busquedaRemesaResoluciones_CAJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGColegioBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->

<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	

	String anio="", numero="",numEJG="", fechaApertura="", estado="", busquedaRealizada="";

	
	String volver=(String)request.getAttribute("VOLVER");

	Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
	ses.removeAttribute("DATOSFORMULARIO");	
	
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script type="text/javascript">	
	
		function refrescarLocal() {		
			buscar();
		}		
		
		function buscarCliente () {
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");	
			if (resultado != null && resultado[2]!=null)
			{
				document.forms[1].idPersona.value=resultado[2];
			}
		}
		function inicio(){
		<% 
		
		 if (busquedaRealizada.equals("1")) {%>
		      buscarPaginador();
		<%}%>
		}
		function inicio2(){
		
		<%  if (volver.equals("1")) {%>
		      buscar();
		 <%}%>
		
		}
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="menu.justiciaGratuita.e_comunicaciones.Resoluciones"
		localizacion="gratuita.BusquedaRemesas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body  onload="inicio();ajusteAlto('resultado');inicio2();">

	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="/JGR_E-Comunicaciones_RemesaResolucion.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idRemesaResolucion" value = ""/>
		<html:hidden property = "paginaSeleccionada" value = ""/>
	
	

	<fieldset name="fieldset1" id="fieldset1">
	<legend>
		<span  class="boxConsulta">
			<siga:Idioma key="gratuita.busquedaResolucionesCAJG.literal.datos"/>
		</span>
	</legend>
	
	<table align="center" width="100%" border="0">
		<tr>	
			<td class="labelText" >
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.numRegistro"/>	
			</td>
			
			<td class="labelText">	
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="prefijo"  size="5" maxlength="10" styleClass="box" style="width:80px"></html:text>
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="numero"  size="5" maxlength="10" styleClass="box" style="width:80px"></html:text>
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="sufijo"  size="5" maxlength="10" styleClass="box" style="width:80px"></html:text>	
			</td>	
			<td class="labelText" >
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.nombreFichero"/>	
			</td>
			
			<td class="labelText">	
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="nombreFichero"  size="100" maxlength="10" styleClass="box" style="width:250px"></html:text>					
			</td>
		</tr>
		
		<tr>
		
			<td class="labelText">
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fResolucionDesde"/>
			</td>
			<td class="labelText">
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="fechaResolucion" size="10" maxlength="10" styleClass="box"   readOnly="true"></html:text>
				<a onClick="return showCalendarGeneral(fechaResolucion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
			
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fResolucionHasta"/>
			
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="fechaResolucionHasta" size="10" maxlength="10" styleClass="box"   readOnly="true"></html:text>
				<a onClick="return showCalendarGeneral(fechaResolucionHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCargaDesde"/>
			</td>
			<td class="labelText">
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="fechaCarga" size="10" maxlength="10" styleClass="box"   readOnly="true"></html:text>
				<a onClick="return showCalendarGeneral(fechaCarga);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
			
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCargaHasta"/>
			
				<html:text name="DefinicionRemesaResolucionesCAJGForm" property="fechaCargaHasta" size="10" maxlength="10" styleClass="box"   readOnly="true"></html:text>
				<a onClick="return showCalendarGeneral(fechaCargaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
			</td>
			
		</tr>	
		

	</table>
	</fieldset>
	
	</html:form>	
	
	
	
	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B,L"  titulo="gratuita.busquedaResolucionesCAJG.literal.Resoluciones" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	

		<!-- Funcion asociada a boton buscar -->
		
				
		
		function buscar () {
				
			sub();
			document.forms[0].modo.value = "buscarInit";
			document.forms[0].submit();
		}	
		
		function buscarPaginado(paginaSeleccionada) {
			if (!isNaN(paginaSeleccionada)) {
				document.forms[0].paginaSeleccionada.value=paginaSeleccionada
			}
			
			buscar();
			document.forms[0].paginaSeleccionada.value=''
		}
		
			
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() {				
			document.forms[0].reset();
		}
		
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() {
			document.forms[0].modo.value = "nuevo";			
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			buscar();			
			if(resultado && resultado[0]=="MODIFICADO"){
			
				with(document.DefinicionRemesaResolucionesCAJGForm){			
						
					document.DefinicionRemesaResolucionesCAJGForm.idRemesaResolucion.value = resultado[1];
					document.DefinicionRemesaResolucionesCAJGForm.idInstitucion.value = resultado[2];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}

		 
		


		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>