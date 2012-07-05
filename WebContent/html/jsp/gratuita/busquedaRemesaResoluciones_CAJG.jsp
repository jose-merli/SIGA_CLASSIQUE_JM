<!-- busquedaRemesaResoluciones_CAJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>



<!-- IMPORTS -->

<%@ page import="com.atos.utils.UsrBean"%>
<%@page import="com.siga.ws.CajgConfiguracion"%>
<%@ page import="com.siga.beans.ConModuloBean"%>

<!-- JSP -->

<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	

	String anio="", numero="",numEJG="", fechaApertura="", estado="", busquedaRealizada="";

	
	String volver=(String)request.getAttribute("VOLVER");

	ses.removeAttribute("DATOSFORMULARIO");	
	String idTipoRemesa = request.getParameter("idTipoRemesa");
	
	String titulo = "";
	String localizacion = "gratuita.BusquedaRemesas.localizacion";
	String botones = "N,B,L,CON";
	
	if (idTipoRemesa != null) {
		if (idTipoRemesa.equals("1")) {
			titulo = "menu.justiciaGratuita.e_comunicaciones.Resoluciones"; 
		} else if(idTipoRemesa.equals("2")) {
			titulo = "menu.justiciaGratuita.e_comunicaciones.cargaDesignaProcurador";
		}
	}
	
	int pcajgActivo = 0;
	
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute("pcajgActivo").toString());
	}
	
	
	
	if (pcajgActivo == CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA) {
		if (idTipoRemesa != null) {
			if (idTipoRemesa.equals("1")) {
				botones = "OR,B,L,CON";
			} else if(idTipoRemesa.equals("2")) {
				botones = "ODP,B,L,CON";
			}
		}
	}
	
	
	
	
%>


<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
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

		function obtenerResoluciones() {
			sub();
			document.forms[0].modo.value="obtenerResoluciones";
			//document.forms[0].target="resultado";
			document.forms[0].submit();
		}
		
		function consultas() 
		{		
			document.RecuperarConsultasForm.submit();
			
		}

		function obtenerDesignaProcurador() {
			sub();
			document.forms[0].modo.value="obtenerDesignaProcurador";
			document.forms[0].submit();
		}
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="<%=titulo%>"
		localizacion="<%=localizacion%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body  onload="inicio();ajusteAlto('resultado');inicio2();">

	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="/JGR_E-Comunicaciones_RemesaResolucion.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "idTipoRemesa" value = "<%=idTipoRemesa%>"/>
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
				<siga:Fecha nombreCampo="fechaResolucion"/>

				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fResolucionHasta"/>
				<siga:Fecha nombreCampo="fechaResolucionHasta"/>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCargaDesde"/>
			</td>
			<td class="labelText">
				<siga:Fecha nombreCampo="fechaCarga"/>

				<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCargaHasta"/>
				<siga:Fecha nombreCampo="fechaCargaHasta"/>
			</td>
			
		</tr>	
		

	</table>
	</fieldset>
	
	</html:form>	
	
	
	
	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="<%=botones%>"  titulo="gratuita.busquedaResolucionesCAJG.literal.Resoluciones" />
		<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
			<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_SJCS %>"/>
			<html:hidden property="modo" value="inicio"/>
			<html:hidden property="accionAnterior" value="${path}"/>		
		</html:form>
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