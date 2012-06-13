<!DOCTYPE html>

<!-- alterMutuaConsulta.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page pageEncoding="ISO-8859-15"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="fmt.tld" prefix="fmt"%>

<html>
<head>

<%String app=request.getContextPath(); 
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	%>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/css/jquery-ui.css"/>">

<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery-ui.js'/>" type="text/javascript"></script>

<style media="screen" type="text/css">
	.ocultar { display: none }
	
	.red { color: "red" }
	
    fieldset{
    	padding-bottom:10px;
    }
    legend{
    	border:1 black;
    	padding-left:10px;
    	padding-right:10px;
    }
    .defaultText {}
    .defaultTextActive { color: #a1a1a1; font-style: italic; }
    .requiredText {}
    .requiredTextActive { color: #8a8a8a; font-weight:bold;}
    .obligatorio {background: #F7D5E3;}
        
</style>

<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.solicitudIncorporacion.localizacion"/>
	<%if(path.toString().contains("Ficha")){ %>
		<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.fichaCliente.localizacion"/>
	<%}%>
<!-- FIN: TITULO Y LOCALIZACION -->

</head>


<body class="tablaCentralCampos">

<div id="datosSolicitud" >
	<fmt:setLocale value="es_ES"/>
	<html:form action="/CEN_AlterMutua" method="POST">
		<html:hidden property="apellidos"/>
		<html:hidden property="codigoPostal"/>
		<html:hidden property="comunicacionPreferente"/>
		<html:hidden property="domicilio"/>
		<html:hidden property="email"/>
		<html:hidden property="estadoCivil"/>
		<html:hidden property="fax"/>
		<html:hidden property="fechaNacimiento"/>
		<html:hidden property="identificador"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idPaquete"/>
		<html:hidden property="movil"/>
		<html:hidden property="msgRespuesta"/>
		<html:hidden property="nombre"/>
		<html:hidden property="poblacion"/>
		<html:hidden property="sexo"/>
		<html:hidden property="idSexo"/>
		<html:hidden property="telefono1"/>
		<html:hidden property="telefono2"/>
		<html:hidden property="tipoEjercicio"/>
		<html:hidden property="idTipoIdentificacion"/>
		<html:hidden property="idSolicitudalter"/>
		<html:hidden property="identificador"/>
		<html:hidden property="titular"/>
		<html:hidden property="herederos"/>
		<html:hidden property="familiares"/>
		<html:hidden property="requiereBeneficiarios"/>
		<html:hidden property="requiereFamiliares"/>
		<html:hidden property="modo"/>
		<html:hidden property="brevePropuesta"/>
		<html:hidden property="tarifaPropuesta"/>
		<html:hidden property="codigoInstitucion"/>
		<html:hidden property="idSolicitud"/>
		<html:hidden property="idTipoEjercicio"/>
		<html:hidden property="propuesta"/>
		<html:hidden property="numeroPropuestas"/>

		<input type="hidden" name="actionModal" value="">
		
		<c:set var="estiloText" value="box" />
		<c:set var="estiloCombo" value="boxCombo" />
		
		<fieldset id="estadoColegiado" ><legend><siga:Idioma key="censo.alterMutua.literal.estadoColegiado"/></legend>
		<table>
		<tr>
			<td style="width:120px">&nbsp;</td>
			<td><input type="button" alt=""  id="idButton" onclick="return consultarEstadoColegiado();" class="button" name="idButton" value="<siga:Idioma key='censo.alterMutua.literal.consultarEstado'/>"></td>
		</tr>
		</table>
		</fieldset>
		
		<!--/c:if-->
			
		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.datosPersonales"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.identificador"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.tipoIdentificacion}"/>&nbsp;<c:out value="${AlterMutuaForm.identificador}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.nombre"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.nombre}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.apellidos"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.apellidos}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.fechaNacimiento"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.fechaNacimiento}"/></td>
				</tr>
				<tr>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.sexo"/></td>
					<td class="labelTextValor"> <c:out value="${AlterMutuaForm.sexo}" /> </td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.estadoCivil"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.estadoCivil}" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.colegio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.colegio}" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.situacionEjercicio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoEjercicio}" /></td>
				</tr>
			</table>
		</fieldset>
			
	<siga:ConjBotonesAccion botones="V"  clase="botonesDetalle"  />
	
	</html:form>

	<form name="busquedaClientesForm" method="POST" action="/SIGA/CEN_BusquedaClientes.do" target="mainWorkArea">
			<input type="hidden" name="modo" value="Editar">
			<input type="hidden" name="avanzada" value="">
	</form>
	<form name="SolicitudIncorporacionForm" method="POST" action="/SIGA/CEN_SolicitudesIncorporacion.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
	</form>		
	<div id="dialog-message" title="SIGA" style="vertical-align: top;"></div>

	<script>
	<%if(path.toString().contains("Ficha")){ %>
		function accionVolver() 
		{
			document.busquedaClientesForm.action = "/SIGA/CEN_BusquedaClientes.do" + "?noReset=true&buscar=true";
			document.busquedaClientesForm.modo.value="abrirConParametros";
			document.busquedaClientesForm.submit();	
		}
	<%}else{%>
		function accionVolver() {		
			document.SolicitudIncorporacionForm.action="./CEN_SolicitudesIncorporacion.do";	
			document.SolicitudIncorporacionForm.target="mainWorkArea";
			document.SolicitudIncorporacionForm.submit();
		}
	<%}%>
	
	function refrescarLocal(){
		window.location.reload(true);
	}

	function consultarEstadoColegiado(){
		sub();
        $.ajax({
            type: "POST",
            url: "/SIGA/CEN_AlterMutua.do?modo=getEstadoColegiado",
            data: $('form').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            success: function(json){
           		jAlert(json.mensaje,400,300);
		        fin();
            },
            error: function(e){
                alert('Error de comunicación: ' + e);
		        fin();
            }
        });
	}
	
	function jAlert(texto, ancho, alto){
		$("#dialog-message").html(texto);
		$("#dialog-message").height(alto);
		$("#dialog-message").dialog({
			modal: true,
			resizable: false,
			width: ancho,
			height: alto,
			buttons: { "Ok": function() { $(this).dialog("close"); $(this).dialog("destroy"); } }
		});
		$("#dialog-message").scrollTop(0);
	}

 	</script>
	
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
</body>

</html>



