<!-- datosRetencionesJudiciales.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();	
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	String accion = request.getAttribute("accion").toString();
	
	String  fechaInicio="", fechaFin="", tipoRetencion="", importe="",descDestinatario="", nombre="", ncolegiado="", observaciones="",obligatorio="", estilo="",estiloCalendario="", estiloFechaFin="", estiloNum="", estiloCombo="", idPersona="", idRetencion="", fechaAlta="",esDeTurno="";
	ArrayList  destinatario= new ArrayList();
	String activar=ClsConstants.DB_FALSE;
	boolean lectura = false;
	String aplicaRetencion=(String)request.getAttribute("APLICADARETENCION");
	
	try {
		Hashtable miHash = (Hashtable) request.getAttribute("DATABACKUP");		
		fechaAlta = (String) miHash.get("FECHAALTA");
		idRetencion = (String) miHash.get("IDRETENCION");
		idPersona = (String) miHash.get("IDPERSONA");
		fechaInicio = GstDate.getFormatedDateShort("",miHash.get("FECHAINICIO").toString());
		fechaFin = GstDate.getFormatedDateShort("",miHash.get("FECHAFIN").toString());		
		importe = (String) miHash.get("IMPORTE");
		nombre = (String) miHash.get("NOMBRE");
		
		ncolegiado = (String) miHash.get("NCOLEGIADO");
		tipoRetencion = (String) miHash.get("TIPORETENCION");
		descDestinatario = (String) miHash.get("DESCDESTINATARIO");
		esDeTurno = (String) miHash.get("ESDETURNO");
		
		
		if (miHash.containsKey("OBSERVACIONES")) observaciones = (String) miHash.get("OBSERVACIONES");		
		
		destinatario.add((String)miHash.get("IDDESTINATARIO"));
	} catch (Exception e){};
	
	// La fecha de alta no se modifica, por tanto si entramos en modificación recuperamos la de la base de datos, y si es inserción se pondrá "sysdate"
	if (fechaAlta.equals("")) fechaAlta="sysdate";
		
	if (accion.equalsIgnoreCase("ver"))	{
	 
		estilo = "boxConsulta";
		estiloNum= "boxConsultaNumber";
		estiloCombo = "boxConsulta";
		lectura=true;
		estiloFechaFin="boxConsulta";
		estiloCalendario="boxConsulta";
	} else {
	  if (aplicaRetencion!=null && aplicaRetencion.equalsIgnoreCase("1")){
	    estilo = "boxConsulta";
		estiloNum= "boxConsultaNumber";
		estiloCombo = "boxConsulta";
		lectura=true;
		estiloFechaFin="box";
		estiloCalendario="boxConsulta";
	  
	  }else{
	  
	  
		estilo = "box";
		estiloNum= "boxNumber";
		estiloCombo = "boxCombo";
		lectura=false;
		estiloFechaFin="box";
		estiloCalendario="box";
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
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<html:javascript formName="MantenimientoRetencionesJudicialesForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			var nombre = "";
			// Se recuperan los valores que nos devuelvel a ventana modal (vienen en un array)
			if (resultado[0]!=null) document.forms[1].idPersona.value=resultado[0];
			if (resultado[2]!=null) document.forms[1].ncolegiado.value=resultado[2];
			// Se comprueba si el letrado tiene un nombre, apellido1 y apellido 2 y se va concatenando
			if (resultado[4]!=null) nombre = resultado[4];
			if (resultado[5]!=null) nombre += " " + resultado[5];
			if (resultado[6]!=null) nombre += " " + resultado[6];
			// Ahora se rellena el html:text (que está en modo consulta) con el nombre completo del letrado
			document.forms[1].nombre.value=nombre;
		}
		
		function activarLetrado(valorCheck){
		 
		 if (!valorCheck.checked){
		   document.getElementById("busquedaLetrado").style.display="block";
		 }else{
		   document.getElementById("busquedaLetrado").style.display="none";
		   limpiarPersona();
		 }
		}
		
		function limpiarPersona() {
			document.getElementById("idPersona").value = "";			
			document.getElementById(numeroNifTagBusquedaPersonas).value = "";
			document.getElementById('nombrePersona').value = "";
			
		}
		function cargarCheck(){
		  
		  <%
		  if (esDeTurno!=null && esDeTurno.equals("1")){%>
		  
		      
			 document.getElementById("checkEsDeTurno").checked=true;
			  <%if (!accion.equalsIgnoreCase("ver")&&!accion.equalsIgnoreCase("modificar")){%>
			  
			  document.getElementById("busquedaLetrado").style.display="none";
		      limpiarPersona();
			  <%}else{%>
			  document.getElementById("checkEsDeTurno").disabled=true;
			  <%}%>
		  <%}else{%>
		     document.getElementById("checkEsDeTurno").checked=false;
			 <%if (!accion.equalsIgnoreCase("ver")&&!accion.equalsIgnoreCase("modificar")){%>
			 document.getElementById("busquedaLetrado").style.display="block";
			 <%}else{%>
			  document.getElementById("checkEsDeTurno").disabled=true;
			  <%}%>
		  <%}%>
		  <%if (!accion.equalsIgnoreCase("ver")&&!accion.equalsIgnoreCase("modificar")){%>
		    document.getElementById('nombrePersona').value = "<%=nombre%>";
		    document.getElementById('numeroNifTagBusquedaPersonas').value = "<%=ncolegiado%>";
		 
		   <%}%>  
		   obtenerCuenta();
		}	
		
		function obtenerCuenta() 
		{ 
			  if (document.getElementById("idDestinatario").value!=""){
               document.MantenimientoDestinatariosRetencionesForm.nombreObjetoDestino.value="";	
			   document.MantenimientoDestinatariosRetencionesForm.idDestinatario.value=document.getElementById("idDestinatario").value;
			   document.MantenimientoDestinatariosRetencionesForm.submit();		
			  
			  }
		}
		
		function traspasoDatos(resultado){
		  document.getElementById("cuentaContable").value=resultado[0]
		}		
	</script>
</head>

<body onload="cargarCheck();">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="FactSJCS.mantRetencionesJ.cabeceraExt"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposMedia" align="center" cellspacing="0" >
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<html:form action="/FCS_RetencionesJudiciales.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idRetencion" value = "<%=idRetencion%>"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "fechaAlta" value = "<%=fechaAlta%>"/>
	

	<tr>		
	<td>	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="FactSJCS.mantRetencionesJ.leyendaRetenciones">	
	<table class="tablaCampos" align="center" border="0">
	<tr>
	<tr>
	<td class="labelText" colspan="4">
	<html:checkbox  property="checkEsDeTurno"   onclick="activarLetrado(this);" />&nbsp;&nbsp;<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.aplicableLetrados"/>
	</td>
	
	</tr>
	<%if (!accion.equalsIgnoreCase("ver")&&!accion.equalsIgnoreCase("modificar")){%>
	<td  id="busquedaLetrado" style="display:true">
    <siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="idPersona" >
	</siga:BusquedaPersona>
	</td>
	<%}else{%>
	<td class="labelText" width="155">
	<siga:Idioma key="gratuita.busquedaEJG.literal.colegiado"/>
	</td>
	<td colspan="3" >
	<input type="text" name="numeroColegiado" style="width: 250px;." class="boxConsulta" readonly="true" value="<%=ncolegiado%> - <%=nombre%>">
	</td>
	
	<%}%>
	</tr>
	<tr>
	</tr>
	<table class="tablaCampos" align="center" border="0">
	<tr>
	<td class="labelText" width="155">
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramoLec"/>&nbsp;(*)
	</td>
	
	<td class="labelText" >
		<%if (accion.equalsIgnoreCase("ver")||(!accion.equalsIgnoreCase("ver") && (aplicaRetencion!=null && aplicaRetencion.equalsIgnoreCase("1")))){%>
			<select name="tipoRetencion" class="boxCombo" disabled="disabled" >
				<option  value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>" <%if(tipoRetencion.equalsIgnoreCase("P")){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual"/></option>
				<option  value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>" <%if(tipoRetencion.equalsIgnoreCase("F")){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo"/></option>
				<option  value="<%=ClsConstants.TIPO_RETENCION_LEC%>" <%if(tipoRetencion.equalsIgnoreCase(ClsConstants.TIPO_RETENCION_LEC)){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramosLEC"/></option>
									</option>
				
			</select>
		<%} else  {%>
			<select name="tipoRetencion" class="boxCombo" >
				<option  value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>" <%if(tipoRetencion.equalsIgnoreCase("P")){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual"/></option>
				<option  value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>" <%if(tipoRetencion.equalsIgnoreCase("F")){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo"/></option>
				<option  value="<%=ClsConstants.TIPO_RETENCION_LEC%>" <%if(tipoRetencion.equalsIgnoreCase(ClsConstants.TIPO_RETENCION_LEC)){ %>selected<%}%>><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramosLEC"/></option>
				</option>
			</select>
		<%}%>	
		</td>
	<td class="labelText" style="vertical-align:left" colspan="2">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="importe" size="10" maxlength="10" styleClass="<%=estiloNum%>" value= "<%=UtilidadesNumero.formatoCampo(importe)%>" readonly="<%=lectura%>" ></html:text>
	</td>
	
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaInicioRJ"/>&nbsp;(*)
	</td>				
	<td class="labelText">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="fechaInicio" size="10" maxlength="10" styleClass="<%=estiloCalendario%>" value="<%=fechaInicio%>" readonly="true"></html:text>
		<%  // Si se entra en modo consulta, los botones del calendario desaparecen
			if (!accion.equalsIgnoreCase("ver")&&!accion.equalsIgnoreCase("modificar")){%>
			&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaInicio);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.listadoCalendario.literal.seleccionarFecha")%>'  border="0"></a>
		<%}%>
	</td>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaFin"/>
	</td>
	<td class="labelText">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="fechaFin" size="10" styleClass="<%=estiloFechaFin%>" value="<%=fechaFin%>" readonly="true"></html:text>
		<%   // Si se entra en modo consulta, los botones del calendario desaparecen
			if (!accion.equalsIgnoreCase("ver")){%>
			&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaFin);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.listadoCalendario.literal.seleccionarFecha")%>'  border="0"></a>
		<%}%>
	</td>	
	</tr>
	<tr>
	<td class="labelText" >
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.destinatario"/>&nbsp;(*)
	</td>
	
	<td class="labelText" >
		<%if (accion.equalsIgnoreCase("ver")||(!accion.equalsIgnoreCase("ver") && (aplicaRetencion!=null && aplicaRetencion.equalsIgnoreCase("1")))){%>
			<siga:ComboBD nombre="idDestinatario" tipo="destinatariosRetencionesFCS" ancho="150" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" ElementoSel="<%=destinatario%>" readonly="true"/>
		<%} else {%>
			<siga:ComboBD nombre="idDestinatario" tipo="destinatariosRetencionesFCS" ancho="150" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" ElementoSel="<%=destinatario%>"  accion="obtenerCuenta();"/>
		<%}%>
		
	</td>
	<td colspan="2">
	 <html:text name="MantenimientoRetencionesJudicialesForm" property="descDestinatario" size="50" maxlength="100" styleClass="<%=estilo%>" value="<%=descDestinatario%>" readonly="<%=lectura%>"></html:text>
	</td>
	</tr>
	<tr>
	<td class="labelText">
	 <siga:Idioma key="FactSJCS.mantRetencionesJ.literal.cuentaContable"/>
	</td>
	<td class="labelText">
	 <input type="text" name="cuentaContable"  class="boxConsulta" readonly="true">
	</td>
	
	</tr>

	<tr>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.observaciones"/>
	</td>
	<td class="labelText" colspan="4">
	<%if (!accion.equalsIgnoreCase("ver")){%>
     	<html:textarea cols="95" rows="4" property="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  styleclass="<%=estilo%>" value="<%=observaciones%>" readonly ="<%=lectura%>"></html:textarea> 
	<%} else {%>
		<html:textarea cols="500" rows="4" property="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  styleclass="<%=estilo%>" value="<%=observaciones%>" readonly ="true"></html:textarea> 
	<%}%>
	</td>
	</tr>
	</siga:ConjCampos>
	</td>
	</tr>
	</html:form>
	
	<html:form action = "/JGR_MantenimientoDestinatariosRetenciones.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarCuenta">
		<html:hidden property = "idDestinatario" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	</table>


<%
	String bot = "C";
	if (!accion.equalsIgnoreCase("ver"))	{
		bot += ",Y,R";
	}	
%>
	<siga:ConjBotonesAccion botones="<%=bot%>" modal="M"  />

	<script language="JavaScript">	

		
		

		<!-- Validaciones locales -->
		
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			document.MantenimientoRetencionesJudicialesForm.importe.value=document.MantenimientoRetencionesJudicialesForm.importe.value.replace(/,/,".");
			var res = compararFecha(document.forms[1].fechaFin,document.forms[1].fechaInicio);
			sub();
		  	if (validateMantenimientoRetencionesJudicialesForm(document.MantenimientoRetencionesJudicialesForm)){ 
			
				if (res != 2) {
			 		if (!document.getElementById("checkEsDeTurno").checked && document.getElementById("idPersona").value== "" ){
			  			alert('<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.checkAplicable"/>');
			  			fin();
			  			return false;
			 		}
			 		if ((MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='P' || MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='I')&&MantenimientoRetencionesJudicialesForm.importe.value==''){
			 			msg = "<siga:Idioma key='errors.required' arg0='FactSJCS.mantRetencionesJ.literal.importe'/>";
						alert(msg);
						fin();
						return false;

			  			
			 		}
			 		
			 		if (MantenimientoRetencionesJudicialesForm.tipoRetencion.value=='P' && MantenimientoRetencionesJudicialesForm.importe.value>100){
			  			alert('<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.avisoPorcentaje"/>');
			  			fin();
			  			return false;
			 		}else{
			 			document.forms[1].checkEsDeTurno.value = document.getElementById("checkEsDeTurno").checked;
						document.forms[1].target = "submitArea";
						document.forms[1].modo.value = '<%=accion%>';
						document.forms[1].submit();				            
			 		}   
				}else{
			 		alert('<siga:Idioma key="messages.fechas.rangoFechas"/>');
			 		fin();
			 		return false;
				}
		  }else{
		   	fin();
		  }	
	}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");
		}
		
		
	</script>
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe></body>
</html>