<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
 
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	//Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	String[] dato = {usr.getLocation()};

%>	
	
<%  
	// locales
	//ValidarVolantesGuardiasForm formulario = (ValidarVolantesGuardiasForm)request.getAttribute("ValidarVolantesGuardiasForm");
		
	String titu = "censo.busquedaVolantesGuardias.literal.titulo";
	String busc = "censo.busquedaVolantesGuardias.literal.titulo";

%>	


<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<siga:Titulo titulo="<%=titu%>" localizacion="<%=titu%>"/>

	<html:javascript formName="ValidarVolantesGuardiasForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	

</head>

<body onLoad="ajusteAltoBotones('resultadoModal');">

	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titu %>"/>
		</td>
	</tr>
	</table>

	<siga:ConjCampos leyenda="censo.busquedaVolantesGuardias.literal.titulo1">

	<table class="tablaCampos" align="center">

	<html:form action="/JGR_ValidarVolantesGuardias.do" method="POST" target="resultadoModal">
	<html:hidden name="ValidarVolantesGuardiasForm" property = "modo" value = ""/>
	<html:hidden name="ValidarVolantesGuardiasForm" property = "datosValidar" value = ""/>
	<html:hidden name="ValidarVolantesGuardiasForm" property = "datosBorrar" value = ""/>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno"/>
		</td>
		<td>
			<siga:ComboBD nombre ="idTurno" tipo ="turnos" ancho="340" clase="boxCombo" obligatorio="false" accion="Hijo:idGuardia" parametro="<%=dato%>"  />
		</td>	
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia"/>
		</td>
		<td colspan="3">
			<siga:ComboBD nombre = "idGuardia" tipo="guardias" ancho="340" clase="boxCombo" hijo="t" parametro="<%=dato%>"  />
		</td>	
	</tr>

	<!-- FILA -->
	<tr>				
	   <td class="labelText" style="width:150px;">
		<siga:Idioma key="censo.busquedaVolantesGuardias.literal.ncolegiado"/>
	   </td>		 
	  <td>
	    <html:text name="ValidarVolantesGuardiasForm" property="numColegiado" onChange="buscarColegiado();" maxlength="15" size="10" styleClass="box"></html:text>
		<input type="text" name="nomColegiado" value="" class="boxConsulta">
	    <html:hidden name="ValidarVolantesGuardiasForm" property="idPersona"></html:hidden>
	  </td>
	  	<td width="140" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaDesde"/></td>
	  	<td width="140"><html:text styleClass="box" property="buscarFechaDesde" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
	  	<td width="120" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaHasta"/></td>
	  	<td><html:text styleClass="box" property="buscarFechaHasta" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
	 </tr>				

	<!-- FILA -->
	<tr>				
      <td class="labelText">
		<siga:Idioma key="censo.busquedaVolantesGuardias.literal.pendientevalidar"/> 
	  </td>
      <td class="labelText">
	    <input type="checkbox" name="pendienteValidar" class="box" checked>
	  </td>
	</tr>				
	</html:form>
	</table>

	</siga:ConjCampos>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="<%=busc%>" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscarColegiado() 
		{	
			if 	(trim(document.forms[0].numColegiado.value)!="") {
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="submitArea3";
				document.forms[0].submit();	
			} else {
				document.forms[0].nomColegiado.value="";
				document.forms[0].idPersona.value="";
			}
		}

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
			sub();
			document.forms[0].target="resultadoModal";
			if (trim(document.forms[0].numColegiado.value)=="" && 
				trim(document.forms[0].idGuardia.value)=="" && 
				trim(document.forms[0].idTurno.value)==""){
				alert("<siga:Idioma key="gratuita.busquedaVolantesGuardias.literal.criteriosObligatorios"/> ");	
				fin();
				return false;
			}
			if (trim(document.forms[0].numColegiado.value)!="" && 
				trim(document.forms[0].idPersona.value)=="") {
				fin();
				return false;
			}
			
						
			document.forms[0].modo.value="buscar";
			document.forms[0].submit();	
	
		}
		
				
		
		

	</script>

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoModal"
					name="resultadoModal" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	

	<siga:ConjBotonesAccion botones="C,G" modal="G" />

	<script language="JavaScript">
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}
		
		function refrescarLocal() 
		{		
			buscar();
		}
				
		function accionGuardar() 
		{		
			sub();
			var datosvalidar="";
			var datosborrar="";
			var checks = document.resultadoModal.document.getElementsByName("chkVal");
			if (checks.type != 'checkbox') {
				for (i = 0; i < checks.length; i++){
					if (checks[i].disabled==false && checks[i].checked==true) {
						datosvalidar += checks[i].value + "##";		
					}
					if (checks[i].disabled==false && checks[i].checked==false) {
						datosborrar += checks[i].value + "##";		
					}
				}	
			}
			if (datosvalidar.length>2) datosvalidar=datosvalidar.substring(0,datosvalidar.length-2);
			if (datosborrar.length>2) datosborrar=datosborrar.substring(0,datosborrar.length-2);
			if (trim(datosvalidar)!="" || trim(datosborrar)!="") {
				document.forms[0].datosValidar.value=datosvalidar;
				document.forms[0].datosBorrar.value=datosborrar;
				document.forms[0].modo.value="insertar";
				document.forms[0].target="submitArea3";
				document.forms[0].submit();			
			} else {
				fin();
			}
		}
		
		<!-- Asociada al boton MarcarTodos -->
		function accionMarcarTodos() 
		{		
			var checks = document.resultadoModal.document.getElementsByName("chkVal");
			if (checks.type != 'checkbox') {
				for (i = 0; i < checks.length; i++){
					if (checks[i].disabled==false) {
						checks[i].checked=1;		
					}
				}	
			}
			else{
				if (checks.disabled==false) {
					checks.checked=1;		
				}
			}
		}
	
		<!-- Asociada al boton DesmarcarTodos -->
		function accionDesmarcarTodos() 
		{		
			var checks = document.resultadoModal.document.getElementsByName("chkVal");
			if (checks.type != 'checkbox') {
				for (i = 0; i < checks.length; i++){
					if (checks[i].disabled==false) {
						checks[i].checked=0;		
					}
				}
			}
			else{
			  	if (checks[i].disabled==false) {
			  		checks.checked=0;
			  	}
			}
		}
	</script>

	<iframe name="submitArea3" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>
