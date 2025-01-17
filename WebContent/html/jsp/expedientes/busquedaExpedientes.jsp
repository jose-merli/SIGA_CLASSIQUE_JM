<!DOCTYPE html>
<html>
<head>
<!-- busquedaExpedientes.jsp -->

<!-- CABECERA JSP -->

<%@page import="com.siga.beans.ConModuloBean"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*,com.siga.expedientes.form.BusquedaExpedientesForm,java.util.*,com.siga.Utilidades.UtilidadesString,com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();

	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String tipoacceso = userBean.getAccessType();
	String idinstitucion = userBean.getLocation();
	
	//aalg. incidencia del 18 de mayo de 2012. para ver si hay permisos para ese perfil para crear nuevos expedientes
	String accesoNuevo = userBean.getAccessForProcessName("EXP_NuevoExpediente");
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String) request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar != null) {
		//aalg: inc_10284. Se a�ade opci�n buscarVolver para refrescar la b�squeda cuando vuelve
		funcionBuscar = "recargarCombos();buscar('buscarVolver')";
	}

	ArrayList vTipoExp = new ArrayList();
	String sTipoExp = "";
	ArrayList vInst = new ArrayList();
	String sInst = "";
	String general = "N";

	Object[] aPerfiles = new Object[2];
	aPerfiles[0] = userBean.getProfile();
	aPerfiles[1] = userBean.getProfile();

	String datoTipoExp[] = new String[2];
	datoTipoExp[0] = idinstitucion;
	datoTipoExp[1] = idinstitucion;

	BusquedaExpedientesForm form = (BusquedaExpedientesForm) session.getAttribute("busquedaExpedientesForm");
	try {
		
		if (form == null) {
			vTipoExp.add("");
			vInst.add("");
		} else {
			sTipoExp = form.getComboTipoExpediente();
			vTipoExp.add(sTipoExp);

			sInst = form.getInstitucion();
			vInst.add(sInst);
			if (form.getEsGeneral().equals("S")) {
				general = "S";
			} else {
				general = "N";
			}
		}

	} catch (Exception e) {
		vTipoExp.add("");
		vInst.add("");
	}
	String[] datosJuzgado = { userBean.getLocation(), "","", "-1" };
	String[] datosMateria = { "-1", userBean.getLocation() };
	String[] paramPro = {"",userBean.getLocation()};
	String[] paramPretension = { userBean.getLocation(), "-1" };
	String txtbuscar = UtilidadesString.getMensajeIdioma(userBean, "general.search");
	
	String anio = "";	
	if (ses.getAttribute("DATOSFORMULARIO") instanceof Hashtable) {
		Hashtable<String,Object> miHash = (Hashtable<String,Object>) ses.getAttribute("DATOSFORMULARIO");
		ses.removeAttribute("DATOSFORMULARIO");   
		
		if (miHash.get("AnioExpediente")!=null) {
			anio = miHash.get("AnioExpediente").toString();
		}
		
	} else {
		anio = UtilidadesBDAdm.getYearBD("");
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="expedientes.auditoria.literal.titulo" localizacion="expedientes.auditoria.literal.localizacion" />
	<!-- FIN: TITULO Y LOCALIZACION -->

	<script>
		function buscarPersona() {				
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].nombre.value=resultado[4];
				document.forms[0].primerApellido.value=resultado[5];
				document.forms[0].segundoApellido.value=resultado[6];
			}
		}
		
		function mostarNombre(obj) {
			var indice = obj.selectedIndex;
			document.forms[0].nombreInstLargo.value=document.forms[0].comboInstitucionLargo.options[indice].text;
		}
		
		function buscarPersonaDenunciante() {				
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].nombreDenunciante.value=resultado[4];
				document.forms[0].primerApellidoDenunciante.value=resultado[5];
				document.forms[0].segundoApellidoDenunciante.value=resultado[6];
			}
		}
		
		function obtenerJuzgado() { 
		  	if (document.getElementById("codigoExtJuzgado").value!=""){
			 	document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
			   	document.MantenimientoJuzgadoForm.codigoExt2.value=document.getElementById("codigoExtJuzgado").value;
				document.MantenimientoJuzgadoForm.submit();		
		 	} 
		 	else
		 		seleccionComboSiga("juzgado",-1);
		}		

		function traspasoDatos(resultado) {
			if (resultado[0]==undefined) {
				seleccionComboSiga("juzgado",-1);
				document.getElementById("codigoExtJuzgado").value = "";
			} 
			else
				seleccionComboSiga("juzgado",resultado[0]);				 
		}			
		
		function cambiarJuzgado(comboJuzgado) {
			if(comboJuzgado.value!=""){
				jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
		   			type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado2",
					data: "idCombo="+comboJuzgado.value,
					dataType: "json",
					success: function(json){		
			       		document.getElementById("codigoExtJuzgado").value = json.codigoExt2;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicaci�n: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById("codigoExtJuzgado").value = "";
		}
	</script>
</head>

<body onload="ajusteAlto('resultado');<%=funcionBuscar%>">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<html:form action="/EXP_AuditoriaExpedientes.do?noReset=true" method="POST" target="resultado">
		<html:hidden property="modo" value="" />
		<html:hidden property="avanzada" value="" />
		<html:hidden property="esGeneral" value="<%=general%>" />
		<html:hidden property="hiddenFrame" value="1" />
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
 		<html:hidden property="seleccionarTodos" />
	
		<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">
			<table class="tablaCampos" align="center" border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="labelText" width="250x">
						<siga:Idioma key="expedientes.auditoria.literal.tipo" />
					</td>
					<td>						
						<siga:Select id="comboTipoExpediente" queryId="getTiposExpedientesLocalOgeneralConPermiso" selectedIds="<%=vTipoExp%>" width="200"/>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.otrainstitucion" /> 
						<input type="checkbox" name="checkGeneral" value="S" <%if (general.equals("S")) {%> checked <%}%> onclick="javascript:marked()" />
					</td>

					<td class="labelText">
						<siga:Idioma key="expedientes.literal.orden" />
					</td>
					<td>
						<html:select onchange="buscar();" name="busquedaExpedientesForm" property="orden" styleClass="boxCombo">
							<html:option value="1" key="expedientes.auditoria.literal.fecha"></html:option>
							<html:option value="2" key="expedientes.auditoria.orden.apellidosNombre"></html:option>
							<html:option value="3" key="expedientes.auditoria.literal.nexpediente"></html:option>
						</html:select>
					</td>
					<td>
						<html:select onchange="buscar();" name="busquedaExpedientesForm" property="tipoOrden" styleClass="boxCombo">
							<html:option value="1" key="orden.literal.ascendente"></html:option>
							<html:option value="2" key="orden.literal.descendente"></html:option>
						</html:select>
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.nexpediente" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="anioExpediente" style="width:40px;" maxlength="4" styleClass="box" value="<%=anio%>" />
						&nbsp;/&nbsp;
						<html:text name="busquedaExpedientesForm" property="numeroExpediente" style="width:60px;" maxlength="6" styleClass="box" />
					</td>
					
					<td class="labelText" colspan="2">
						<siga:Idioma key="expedientes.auditoria.literal.nexpdisciplinarioejg" />
					</td>
					<td colspan="3">
						<html:text name="busquedaExpedientesForm" property="anioExpDisciplinario" style="width:40px;" maxlength="4" styleClass="box" />
						&nbsp;/&nbsp;
						<html:text name="busquedaExpedientesForm" property="numeroExpDisciplinario" style="width:60px;" maxlength="6" styleClass="box" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.gestionarExpedientes.fechaApertura" />
					</td>
					<td>
						<siga:Fecha nombreCampo="fecha" valorInicial="<%=form.getFecha()%>" />
					</td> 
					
					<td class="labelText" colspan="2">
						<siga:Idioma key="expedientes.gestionarExpedientes.fechaAperturaHasta" />
					</td>
					<td colspan="3">
						<siga:Fecha nombreCampo="fechaHasta" valorInicial="<%=form.getFechaHasta()%>" />
					</td>																
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.asunto" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="asunto" size="40" maxlength="70" styleClass="box" />
					</td>
					
					<td class="labelText" colspan="2">
						<siga:Idioma key="expedientes.gestionarExpedientes.campoConfigurado" />
					</td>
					<td colspan="3">
						<html:text name="busquedaExpedientesForm" property="campoConfigurado" size="42" maxlength="40" styleClass="box" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.observaciones" />
					</td>
					<td colspan="6">
						<html:text name="busquedaExpedientesForm" property="observaciones" size="128" maxlength="4000" styleClass="box" />
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="expedientes.auditoria.literal.impugnadoydenunciado" desplegable="true" oculto="true">
			<table class="tablaCampos" align="center" border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.nombre" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="nombre" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="primerApellido" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="segundoApellido" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td>
						<input type="button" class="button" name="idButton" alt="<%=txtbuscar%>" id="searchPerson" onclick="return buscarPersona();" value="<%=txtbuscar%>" />&nbsp;
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="expedientes.auditoria.literal.impugnanteydenunciante" desplegable="true" oculto="true">
			<table class="tablaCampos" align="center" border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.nombre" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="nombreDenunciante" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="primerApellidoDenunciante" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2" />
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="segundoApellidoDenunciante" maxlength="100" styleClass="box" style="width:200px" />
					</td>

					<td>
						<input type="button" class="button" name="idButton" alt="<%=txtbuscar%>" id="searchPerson" onclick="return buscarPersonaDenunciante();" value="<%=txtbuscar%>" />&nbsp;
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="expedientes.auditoria.literal.asuntojudicial" desplegable="true" oculto="true">
			<table class="tablaCampos" align="center" border="0" cellpadding="5" cellspacing="0">
				<tr>					
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.materia"/>
					</td>				
					<td>
						<siga:Select id="idMateria" queryParamId="idmateria" queryId="getMateriaAreaExpediente" childrenIds="juzgado" width="250"/>				
					</td>
					
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.juzgado"/>
					</td>				
					<td>
						<siga:Select id="juzgado" 
							queryParamId="idjuzgado" 
							queryId="getJuzgadosMateria" 
							parentQueryParamIds="idmateria,idarea" 
							childrenIds="idProcedimiento" 
							showSearchBox="true" 
							searchkey="CODIGOEXT2" 
							searchBoxMaxLength="10" 
							searchBoxWidth="9" 
							width="245" />	
					</td>				
				</tr>
								
				<tr>					
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.procedimiento"/>
					</td>
					<td>
						<siga:Select queryId="getProcedimientos" id="idProcedimiento" parentQueryParamIds="idjuzgado" width="250"/>
					</td>
				
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.nasunto"/>
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="numAsunto" size="9" maxlength="20" styleClass="boxCombo" readonly="false" />
					</td>	
				</tr>

				<tr>									
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.pretensiones"/>
					</td>
					<td>
						<siga:Select id="idPretension" queryId="getPretensiones" width="250"/>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="expedientes.auditoria.literal.otrasPretensiones"/>
					</td>
					<td>
						<html:text name="busquedaExpedientesForm" property="otrasPretensiones" size="50" maxlength="500" styleClass="boxCombo" readonly="false"></html:text>
					</td>	
				</tr>
			</table>		
		</siga:ConjCampos>
	</html:form>

<%
	//aalg: controlar si tiene acceso al Nuevo
		if (tipoacceso.equalsIgnoreCase(SIGAConstants.ACCESS_READ) || !accesoNuevo.equalsIgnoreCase(SIGAConstants.ACCESS_FULL)) {
%>
			<siga:ConjBotonesBusqueda botones="B,A,CON" />
<%
		} else {
%>
			<siga:ConjBotonesBusqueda botones="N,B,A,CON" />
<%
		}
%>

	<html:form action="/EXP_NuevoExpediente.do" method="POST"target="submitArea" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property="modo" value="" />
		<html:hidden property="idInstitucion_TipoExpediente" value="" />
		<html:hidden property="idTipoExpediente" value="" />
	</html:form>
	
	<html:form action = "/JGR_MantenimientoJuzgados" method="POST" target="submitArea33">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function initCheck() {
			if (document.forms[0].esGeneral.value=="S"){
				document.forms[0].checkGeneral.checked=true;
			}else{
				document.forms[0].checkGeneral.checked=false;
			}
		}
	
		// Funcion asociada al check		
		function marked() {
			if (document.forms[0].checkGeneral.checked){
				document.forms[0].esGeneral.value="S";
				reloadSelect("comboTipoExpediente", undefined, [{key:'idinstitucion', value: '2000'}]);
				/*
				var tipo='cmbTipoExpedienteGeneral';				
				var destino_esGeneral0=(document.getElementById('comboTipoExpedienteFrame')).src;		
				var tam_esGeneral0 = destino_esGeneral0.indexOf('&id=');
				if(tam_esGeneral0==-1)
				{
					tam_esGeneral0=destino_esGeneral0.length;
				}	
				destino_esGeneral0=destino_esGeneral0.substring(0,tam_esGeneral0)+'&id='+<=idinstitucion%>+'&tipoalt='+tipo;

				(document.getElementById('comboTipoExpedienteFrame')).src=destino_esGeneral0;
				*/
				
			}else {			
				document.forms[0].esGeneral.value="N";
				reloadSelect("comboTipoExpediente");
				/*
				var tipo='cmbTipoExpedienteLocaloGeneralPermisos';
				var destino_esGeneral0=(document.getElementById('comboTipoExpedienteFrame')).src;		
				var tam_esGeneral0 = destino_esGeneral0.indexOf('&id=');
				if(tam_esGeneral0==-1)
				{
					tam_esGeneral0=destino_esGeneral0.length;
				}	
				destino_esGeneral0=destino_esGeneral0.substring(0,tam_esGeneral0);
				(document.getElementById('comboTipoExpedienteFrame')).src=destino_esGeneral0;
				*/
			}			
		}
		
		
		// Funcion asociada a boton buscar
		function buscar(modo) {				
			sub();	
			if(modo)
				document.forms[0].modo.value = modo;
			else			
				document.forms[0].modo.value="buscarInit";

			var numero = document.getElementById('numeroExpediente').value;
			var anio = document.getElementById('anioExpediente').value;
			var numeroExpDis = document.getElementById('numeroExpDisciplinario').value;
			var anioExpDis = document.getElementById('anioExpDisciplinario').value;
			
			if (((numero !="") && isNaN(numero))||((anio !="") && isNaN(anio))) {
				alert('<siga:Idioma key="expedientes.busquedaExpedientes.literal.errorNumeroExpediente"/>');
				fin();
				return;
			}

			if (((numeroExpDis !="") && isNaN(numeroExpDis)) || ((anioExpDis !="") && isNaN(anioExpDis))) {
				alert('<siga:Idioma key="expedientes.busquedaExpedientes.literal.errorNumeroExpDisciplinario"/>');	
				fin();
				return;				
			}

			document.forms[0].avanzada.value="<%=ClsConstants.DB_FALSE%>";
			document.forms[0].target="resultado";
			document.forms[0].submit();					
		}
		
		function seleccionarTodos(pagina) {
			document.forms[0].seleccionarTodos.value = pagina;
			buscar('buscarPor');
				
		}				
		
		// Funcion asociada a boton busqueda avanzada
		function buscarAvanzada() {				
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].avanzada.value="<%=ClsConstants.DB_TRUE%>";			
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
			
		// Funcion asociada a boton Nuevo
		function nuevo() {					
			document.forms[1].submit();
		}
		
		function recargarCombos() {			
		}
		
		function consultas() {		
			document.RecuperarConsultasForm.submit();		
		}			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_EXPEDIENTES%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>	
	</html:form>
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="middle" src="<%=app%>/html/jsp/general/blank.jsp"
		id="resultado" 
		name="resultado" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"		
		class="frameGeneral"></iframe>
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea33" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>	
	<!-- FIN: SUBMIT AREA -->
</body>
</html>