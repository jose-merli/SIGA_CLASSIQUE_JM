<!-- modalCambioLetradoDesigna.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%  String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String[] parametros = new String[]{usr.getLocation()};
	String fecha = UtilidadesBDAdm.getFechaBD("");
	%>
<%  ArrayList idCeroValue=new ArrayList();
	idCeroValue.add("0");%>
	
<%	/*Lo del pcajg*/
	String asterisco = "&nbsp(*)&nbsp";
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	boolean obligatorioDesignacion = false;
	if (pcajgActivo==5){
		obligatorioDesignacion = true;
	}
	
	boolean nuevoProcurador = (Boolean) request.getAttribute("NUEVOPROC");
	
	%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="CambiosProcuradoresDesignasForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	var fechaActual=null;
	var seleccionadoProc = false;
	
		
	//Eliminamos la opcion del procurador actual
	function borraActual(){
	<logic:notEmpty name="CambiosProcuradoresDesignasForm" property="nombreActual">
		var actual='<bean:write name="CambiosProcuradoresDesignasForm" property="idProcurador"/>';
		with(document.CambiosProcuradoresDesignasForm){
			
			idProcurador.value=actual;
			if (idProcurador.selectedIndex) {
				idProcurador.remove(idProcurador.selectedIndex);
			}
			idProcurador.selectedIndex=0;
			/*fechaDesigna.value="";*/
		}
		fechaActual='<bean:write name="CambiosProcuradoresDesignasForm" property="fechaDesigna"/>';
	</logic:notEmpty>
	}


	function buscarProcurador() {
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
		if((resultado != undefined) && (resultado[0] != undefined) && (resultado[1] != undefined)) {
			document.all.CambiosProcuradoresDesignasForm.idProcurador.value   = resultado[0];
			document.all.CambiosProcuradoresDesignasForm.nombreCompleto.value = resultado[1];
			document.all.CambiosProcuradoresDesignasForm.nColegiado.value     = resultado[2];
			seleccionadoProc = true;
		}			 	
	}

	</script>
</head>

<body onload="borraActual()">

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.nuevoProcurador"/>
		</td>
	</tr>
	</table>

<!-- INICIO -->
<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!--formulario para la busqueda avanzada del letrado segun el turno-->
	
	<html:form action="/JGR_CambioProcuradorDesigna.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "anio"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "numero"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "idTurno"/>
	
	<html:hidden property = "cambioMismoDia" value=""/>

	<table  class="tablaCentralCamposMedia"  align="center">
	<tr>				
	<td>

	<!-- INICIO: CAMPOS -->
	<logic:notEmpty name="CambiosProcuradoresDesignasForm" property="nombreActual">
	<siga:ConjCampos leyenda="gratuita.cambiosProcuradoresDesigna.literal.procuradorActual">
	<table  width="100%">
	<tr>				
		<td class="labelText" width="200">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
		</td>
		<td colspan="3" class="boxConsulta">
			<bean:write name="CambiosProcuradoresDesignasForm" property="NColegiadoActual"/>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
		</td>
		<td colspan="3" class="boxConsulta">
			<bean:write name="CambiosProcuradoresDesignasForm" property="nombreActual"/>&nbsp;
			<bean:write name="CambiosProcuradoresDesignasForm" property="apellido1Actual"/>&nbsp;
			<bean:write name="CambiosProcuradoresDesignasForm" property="apellido2Actual"/>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</logic:notEmpty>

	<table width="100%" border="0">
	<tr>
		<td colspan="4">
		<siga:ConjCampos leyenda="gratuita.cambiosProcuradoresDesigna.literal.nuevoProcurador">
			<table  width="100%" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/> (*)
					</td>
					<td colspan="1">
						<input type="text" name="nColegiado" id="nColegiado" size="50" maxlength="100" class="boxConsulta" />
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
					</td>
					<td colspan="2">
						<input type="text" name="nombreCompleto" id="nombreCompleto" size="50" maxlength="100" class="boxConsulta" />
						<html:hidden name="CambiosProcuradoresDesignasForm" property = "idProcurador"/>
					</td>
		
					<td>
						<html:button property="buscar" onclick="return buscarProcurador();" styleClass="button"><siga:Idioma key="general.boton.search"/></html:button> 			
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.numeroDesigna"/>
			<% if (obligatorioDesignacion){ %>
				<%= asterisco %>
			<%} %>
		</td>
		<td>
			<html:text name="CambiosProcuradoresDesignasForm" property="numeroDesigna" size="13" maxlength="10" styleClass="box"></html:text>
		</td>
		<td class="labelText">
			<!--siga:Idioma key="gratuita.modalCambioLetradoDesigna.literal.fechaCambio"/-->
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna"/>&nbsp;(*)
		</td>
		<td>
			<siga:Datepicker nombreCampo="fechaDesigna" valorInicial="<%=fecha%>"></siga:Datepicker>
		</td>
	</tr>
	<% if (!nuevoProcurador){ %>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.tipoMotivo"/>
			</td>
			<td colspan="3">
				<siga:ComboBD pestana="true" nombre="idTipoMotivo" tipo="tipoMotivo" estilo="true" clase="box" filasMostrar="1" elementoSel="<%=idCeroValue%>" seleccionMultiple="false" obligatorio="true"/>
			</td>
		</tr>
	<%} %>
	<tr>
		<td class="labelText" colspan="4">
			<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.observaciones"/><br>
			<html:textarea name="CambiosProcuradoresDesignasForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="5" cols="150" styleClass="box" style="width=665;height=120" onkeydown="cuenta(this,1024);" property="observaciones"/>
		</td>
	</tr>
	</table>

	</td>
	</tr>
	</table>

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,R,C" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	</html:form>
	
	<!-- Para la busqueda del procurador -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrirBusquedaProcuradorModal">
	</html:form>

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	function cuenta (obj){
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		if((keycode!=8)&&(keycode!=46)){
		if (obj.value.length >=1024){
				obj.blur();
		}
	  }	
	}

		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
			// jbd 01-02-2010 Añadida comprobacion de numero desginacion inc-6788
			<%if (pcajgActivo>0){%>
			var error = "";
	   		if (<%= obligatorioDesignacion %> && document.getElementById("numeroDesigna").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.cambiosProcuradoresDesigna.literal.numeroDesigna'/>"+ '\n';
			if(error!=""){
				alert(error);
				fin();
				return false;
			}
	 		<%}%> 
			// JBD 23-1-2009 Añadida comprobacion de seleccionadoProcurador INC-5643
			if(seleccionadoProc){	
				sub();
				if (validateCambiosProcuradoresDesignasForm(document.forms[0])){
					if(fechaActual!=''){
						if(fechaActual == null){
							document.forms[0].modo.value="insertar";
							document.forms[0].submit();
						}else if (isEquals(document.forms[0].fechaDesigna.value,fechaActual)){
						    if (confirm("<siga:Idioma key='messages.designa.confirmacion.igualdadFechas.procurador' />")) {	
						    	document.CambiosProcuradoresDesignasForm.cambioMismoDia.value="1";						 
							  	document.forms[0].modo.value="insertar";
							  	document.forms[0].submit();
							}else{
								document.CambiosProcuradoresDesignasForm.cambioMismoDia.value="0";
							 	fin();
							    return false;
							}
					
							}else{
							  if (!isAfter(document.forms[0].fechaDesigna.value,fechaActual)){
									alert("<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna"/>"+
									  "<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.alert1"/>"+fechaActual);
									 fin();
									 return false;
							  }else{
							    	document.forms[0].modo.value="insertar";
									document.forms[0].submit();								
								}
							}				
					}else{				
						fin();
						return false;
					}
					
				}else{
					alert("<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.alertProcurador"/>");
				  	return false;
				}
			}
		}
		
		//Asociada al boton Cerrar
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
