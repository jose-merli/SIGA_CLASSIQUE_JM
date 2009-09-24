<!-- BusquedaClientesAvanzada.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
%>	
	
<%  
	// locales
	BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
	if (formulario==null) {
		formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
	}
	// datos seleccionados Combo
	
	ArrayList tipoColeg = new ArrayList();
	
	if (formulario.getTipoColegiacion()==null||formulario.getTipoColegiacion().equals("")){
	 tipoColeg.add(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA));
	}else{
	 tipoColeg.add(formulario.getTipoColegiacion());
	} 
	ArrayList tipoApunte = new ArrayList();
	
	if (formulario.getTipoApunte()==null||formulario.getTipoApunte().equals("")){
	 tipoApunte.add("");
	}else{
	 tipoApunte.add(formulario.getTipoApunte());
	} 
	ArrayList comision = new ArrayList();
	
	if (formulario.getComision()==null||formulario.getComision().equals("")){
	 comision.add("");
	}else{
	 comision.add(formulario.getComision());
	} 
	ArrayList colegioSel = new ArrayList();
	colegioSel.add(formulario.getNombreInstitucion());
	ArrayList grupoClienteSel = new ArrayList();
	grupoClienteSel.add(formulario.getGrupoClientes());

	// colegiado
	String colegiado = formulario.getColegiado();
	if (colegiado==null) colegiado="";

	String sexo = formulario.getSexo();
	if (sexo==null) sexo="";
	String residente = formulario.getResidente();
	if (residente==null) residente="";
	String comunitario = formulario.getComunitario();
	if (comunitario==null) comunitario="";
	String ejerciente = formulario.getEjerciente();
	if (ejerciente==null) ejerciente="";
	String tipoCliente = formulario.getTipoCliente();
	if (tipoCliente==null) tipoCliente="";

	String titu = "";
	String loca = "censo.busquedaClientes.localizacion";

	if (colegiado.equals(ClsConstants.DB_TRUE)) {
		//colegiados
		titu = "censo.busquedaClientesAvanzada.colegiados.titulo";
	} else {
		//no colegiados
		titu = "censo.busquedaClientesAvanzada.noColegiados.titulo";
	}


	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("CenInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	/*String parametro[] = new String[1];
	parametro[0] = institucionesVisibles;*/
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	// MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion
	// Obtengo el UserBean y en consecuencia la institucion a la que pertenece y su nombre
	
	String institucionAcceso=user.getLocation();
	String nombreInstitucionAcceso="";
	if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){
		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
		nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(institucionAcceso);
	}
	
   String [] institucionParam = {user.getLocation()};
  
   ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();
	if (formulario.getIdTipoCVSubtipo1()==null||formulario.getIdTipoCVSubtipo1().equals("")){
	 idSubtipo1.add("");
	 
	}else{
	 idSubtipo1.add(formulario.getIdTipoCVSubtipo1());
	 
	} 
	if (formulario.getIdTipoCVSubtipo2()==null||formulario.getIdTipoCVSubtipo2().equals("")){
	 idSubtipo2.add("");
	 
	}else{
	 idSubtipo2.add(formulario.getIdTipoCVSubtipo2());
	 
	} 
	
	
%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="<%=titu %>" 
		localizacion="<%=loca %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="busquedaClientesAvanzadaForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>


<body onload="init();">
<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="/CEN_BusquedaClientesAvanzada.do?noReset=true" method="POST" target="mainWorkArea">
		<html:hidden  name="busquedaClientesAvanzadaForm" property="modo"/>
		<!-- parametro para colegiados o no -->
		<html:hidden name="busquedaClientesAvanzadaForm" property = "colegiado" value="<%=colegiado%>"/>
		<html:hidden name="busquedaClientesAvanzadaForm" property = "avanzada"/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo1">

	<table class="tablaCampos" align="center" border="0">
	
	<!-- FILA -->
	<tr>				

		<td class="labelText" width="19%">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.colegio"/>
		</td>				
		<td colspan="5">
			<!-- MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion -->
			<% if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){ %>
				<html:hidden name="busquedaClientesForm" property = "nombreInstitucion" value = "<%=institucionAcceso%>"/>
				<html:text property="" styleClass="boxConsulta" size="80" value='<%=nombreInstitucionAcceso%>' readOnly="true"></html:text>
			<% }else{ %>
			<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>
		     	  <siga:ComboBD nombre = "nombreInstitucion" tipo="cmbNombreColegiosTodos" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=colegioSel %>"/>
				<% }else{ %>
					<siga:ComboBD nombre = "nombreInstitucion" tipo="cmbInstitucion" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=colegioSel %>"/>	
				<% } %>
			<% } %>
		</td>
	
	</tr>
	<!-- FILA -->
	<tr>				

<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>
		<!-- -->
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
		</td>
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box"></html:text>
		</td>
		<td class="labelText" >
			&nbsp;
		</td>
		<td  colspan="3">
			<input type="hidden" name="tipoCliente">
		</td>
<% } else { %>

	<td class="labelText" >
		<siga:Idioma key="censo.busquedaClientes.literal.tipoCliente"/>
	</td>
	<td >
		<!-- TIPO -->
		<siga:ComboBD nombre = "tipo" tipo="cmbTiposNoColegiadoBusqueda" clase="boxCombo" obligatorio="false" />
<!--
		<html:select name="busquedaClientesForm" property="tipo" styleClass="boxCombo">
				<html:option value=""></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_SOCIEDAD_SERVICIOS_JURIDICOS%>" key="censo.general.literal.sociedadSJ"></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_SOCIEDAD_CIVIL%>" key="censo.general.literal.SociedadCivil"></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_SOCIEDAD_LIMITADA%>" key="censo.general.literal.SociedadLimitada"></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_SOCIEDAD_ANONIMA%>" key="censo.general.literal.SociedadAnonima"></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_PERSONAL%>" key="censo.general.literal.Personal"></html:option>
				<html:option value="<%=ClsConstants.COMBO_TIPO_OTROS%>" key="censo.general.literal.Otros"></html:option>
		</html:select>
-->
	</td>
		<!-- -->
		<td class="labelText" >
			&nbsp;
		</td>
		<td >
			<input type="hidden" name="numeroColegiado">
		</td>
<% } %>

	
	</tr>
	<!-- FILA -->
	<tr>				

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nif"/>
		</td>
		<td>
			<html:text name="busquedaClientesAvanzadaForm" property="nif" size="15" styleClass="box"></html:text>
		</td>
	

		<!-- -->
		<td class="labelText" width="20%">
			<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nombre"/>
			<% } else { %>
			<siga:Idioma key="censo.busquedaClientes.literal.nombreDenominacion" />
			<% } %>
		</td>				
		<td colspan="3">
			<html:text name="busquedaClientesAvanzadaForm" property="nombrePersona" size="30" styleClass="box"></html:text>
		</td>
	
	</tr>
	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.apellido1"/>
		</td>
		<td>
			<html:text name="busquedaClientesAvanzadaForm" property="apellido1" size="30" styleClass="box"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.apellido2"/>
		</td>
		<td colspan="3">
			<html:text name="busquedaClientesAvanzadaForm" property="apellido2" size="30" styleClass="box"></html:text>
		</td>
	
	</tr>
	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoDesde"/>
			</td>				
			<td>
			<html:text styleClass="box" name="busquedaClientesAvanzadaForm" property="fechaNacimientoDesde" size="10"  readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimientoDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>
        <td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoHasta"/>
			</td>				
			<td colspan="2">
			<html:text styleClass="box" name="busquedaClientesAvanzadaForm" property="fechaNacimientoHasta" size="10"  readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimientoHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>
	</tr>
	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.sexo"/>
		</td>
		<td >
			<!-- option select -->
			<html:select name="busquedaClientesAvanzadaForm" property="sexo" style = "null" styleClass = "boxCombo" value="<%=sexo %>">
				<html:option value="" > </html:option>
				<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
				<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
			</html:select>						
		</td>
	
		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.domicilio"/>
		</td>
		<td colspan="3">
			<html:text name="busquedaClientesAvanzadaForm" property="domicilio" size="40" styleClass="box"></html:text>
		</td>
	
	</tr>
	<!-- FILA -->
	<tr>				

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.cp"/>
		</td>
		<td>
			<html:text name="busquedaClientesAvanzadaForm" property="codigoPostal" size="5" styleClass="box"></html:text>	
		</td>

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.telefono"/>
		</td>				
		<td colspan="3">
			<html:text name="busquedaClientesAvanzadaForm" property="telefono" size="20" styleClass="box"></html:text>
		</td>
	
	</tr>
	<!-- FILA -->
	<tr>				
		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fax"/>
		</td>
		<td>
			<html:text name="busquedaClientesAvanzadaForm" property="fax" size="20" styleClass="box"></html:text>
		</td>

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.correo"/>
		</td>
		<td colspan="3">
			<html:text name="busquedaClientesAvanzadaForm" property="correo" size="30" styleClass="box"></html:text>
		</td>
	
	</tr>
	<!-- FILA -->
	 <tr>
   <td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.grupoCliente"/>
		</td>
		<td colspan="4">
			<siga:ComboBD nombre = "grupoClientes" tipo="cmbGruposCliente_1" clase="boxCombo" obligatorio="false" elementoSel="<%=grupoClienteSel %>" parametro="<%=institucionParam%>" />
		</td>
	</tr>
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoApunteCV"/>
		</td>
		<td>
			<siga:ComboBD nombre = "tipoApunte" tipo="curriculum" clase="boxCombo" obligatorio="false" elementoSel="<%=tipoApunte %>" parametro="<%=institucionParam%>" accion="Hijo:idTipoCVSubtipo1,Hijo:idTipoCVSubtipo2;recargarCombos(this);" />
		</td>
		<td >
		<siga:ComboBD nombre="idTipoCVSubtipo1" tipo="cmbComision1" parametro="<%=parametro%>" clase="boxCombo" obligatorio="true"  elementoSel = "<%=idSubtipo1%>" hijo="t" accion="parent.deshabilitarCombos(this);"/>
		</td>		
		<td colspan="3">
		<siga:ComboBD nombre="idTipoCVSubtipo2" tipo="cmbCargos1" parametro="<%=parametro%>" clase="boxCombo"  obligatorio="true"  elementoSel = "<%=idSubtipo2%>" hijo="t" accion="parent.deshabilitarCombos(this);"/>
		</td>
   </tr>
  	</table>

	</siga:ConjCampos>

<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>

	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo2">

		<table class="tablaCampos" align="center" width="100%" border="0">
	
		<!-- FILA -->
		<tr>				
	
		 <td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoColegiacion"/>
		 </td>				
		 <td   colspan="5">
			<siga:ComboBD nombre = "tipoColegiacion" tipo="cmbTipoColegiacion" ancho="5" clase="boxCombo" obligatorio="false" elementoSel="<%=tipoColeg %>"/>				
		 </td>
		</tr>
		<tr>				
	
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.situacion"/>
		</td>				
		<td >
			<!-- option select -->
			<html:select name="busquedaClientesAvanzadaForm" property="ejerciente" style = "null" styleClass = "boxCombo" value="<%=ejerciente %>">
				<html:option value="" > </html:option>
				<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
				<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
			</html:select>						
		</td>
	
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionDesde"/>
		</td>
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="fechaIncorporacionDesde" styleClass="box" readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaIncorporacionDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionHasta"/>
		</td>
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="fechaIncorporacionHasta" styleClass="box" readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaIncorporacionHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>
	
		</tr>				

		<!-- FILA -->
		<tr>				

		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.residente"/>
		</td>
		<td >
			<!-- option select -->
			<html:select name="busquedaClientesAvanzadaForm" property="residente" style = "null" styleClass = "boxCombo" value="<%=residente %>">
				<html:option value="" > </html:option>
				<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
				<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
			</html:select>						
		</td>
	
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.comunitario"/>
		</td>
		<td colspan="3">
			<!-- option select -->
			<html:select name="busquedaClientesAvanzadaForm" property="comunitario" style = "null" styleClass = "boxCombo" value="<%=comunitario %>">
				<html:option value="" > </html:option>
				<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
				<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
			</html:select>						
		</td>

		</tr>				

		</table>
		
	</siga:ConjCampos>

<% 
	} else { 
		// campos de validacion que no hacen falta
%>
		<input type="hidden" name="ejerciente">
		<input type="hidden" name="residente">
		<input type="hidden" name="fechaIncorporacion">
<% 	} %>

	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo3">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				
	
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaDesde"/>
		</td>
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="fechaAltaDesde" styleClass="box" readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaAltaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaHasta"/>
		</td>
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="fechaAltaHasta" styleClass="box" readonly="true">
			</html:text>
			<a href='javascript://'onClick="return showCalendarGeneral(fechaAltaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
		</td>

		<td colspan="2" class="labelText" >
		&nbsp;
		</td>		
		<!-- 
		<td class="labelText" >
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.concepto"/>
		</td>				
		<td >
			<html:text name="busquedaClientesAvanzadaForm" property="concepto" size="30" styleClass="box"></html:text>
		</td>
 -->
		</tr>
	
		</table>

	</siga:ConjCampos>

	</td>
	</tr>

	</html:form>
	
	</table>



	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

<%  
	String botones = "B,S";
	if (!colegiado.equals(ClsConstants.DB_TRUE)) {
		botones += ",N,NS";
	} 
%>


		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
//			if (validateBusquedaClientesAvanzadaForm(document.forms[0])) 
			{
				sub();
				document.forms[0].modo.value="buscarInit";
				document.forms[0].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
		}

		<!-- Funcion asociada a boton busqueda simple -->
		function buscarSimple() 
		{		
			document.forms[0].modo.value="abrir";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}

		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}

		<!-- Funcion asociada a boton Nueva Sociedad -->
		function nuevaSociedad() 
		{		
			document.forms[0].modo.value="nuevaSociedad";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
		var tipoCurriculum;
		
		function init(){
		 
		  var cmb1 = document.getElementsByName("tipoApunte");
		  var cmb2 = cmb1[0]; 
		   tipoCurriculum=<%=tipoApunte%>;
		  cmb2.onchange();
		
		}
		function recargarCombos(tipo){
		   if (tipo){
		    if (tipoCurriculum!=tipo.value){
			  limpiarCombo("idTipoCVSubtipo1");
     		  limpiarCombo("idTipoCVSubtipo2");
		    }
		   }
		
		}
		function limpiarCombo(nombre){
		   iframeCombo = top.frames[0].document.getElementById (nombre + "Frame");
				cadenaInicial = iframeCombo.src;
				
				if (cadenaInicial.indexOf("&elementoSel=[0]") > 1)  {
					return;
				}
				
				var ini = cadenaInicial.indexOf('&elementoSel=');
				if (ini < 1) 
					return;
				
				cadenaFinal = cadenaInicial.substring(0,ini) + "&elementoSel=[0]";
				
				var fin = cadenaInicial.indexOf('&', ini+2);
				if (fin > 1) {	
					cadenaFinal = cadenaFinal + cadenaInicial.substring(fin);
				}

				iframeCombo.src = cadenaFinal;
		}
						
		function deshabilitarCombos(o){
		  v_subTipo1=o; 
		  if (o.options.length > 1) {
			 o.disabled = false;
		  }
		  else {
			 o.disabled = true;
		  }
		 
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

</div>
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
