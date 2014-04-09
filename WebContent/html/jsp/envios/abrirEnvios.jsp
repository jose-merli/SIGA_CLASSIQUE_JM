<!DOCTYPE html>
<html>
<head>
<!-- abrirEnvios.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
-->

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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*, com.siga.envios.form.DefinirEnviosForm, java.util.*"%>
<%@ page import="com.siga.beans.EnvEnviosAdm"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = (UsrBean)ses.getAttribute(("USRBEAN"));
	//boolean isComision = request.getAttribute("isComision");
	
//	String buscar = (String) request.getAttribute("buscarEnvios");
	String buscar = (String) request.getAttribute("buscar");
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String funcionBuscar = "";
	if (buscar!=null && buscar.equals("true")) {
		funcionBuscar = "buscarPaginador()";
	}
	 
	ArrayList aEstado = new ArrayList();
	ArrayList aTipoEnvio = new ArrayList();
	
	try {
// 		DefinirEnviosForm	form = (DefinirEnviosForm) ses.getAttribute("DATABACKUP"); 
 		DefinirEnviosForm	form = (DefinirEnviosForm) ses.getAttribute("DefinirEnviosForm"); 
		if(form==null){
			aEstado.add("");	
			aTipoEnvio.add("");
		}else
		{
			aEstado.add(form.getIdEstado());
			aTipoEnvio.add(form.getIdTipoEnvio());
		}

	} catch (Exception e) {
		aEstado.add("");
		aTipoEnvio.add("");
	}
	
%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.definir.literal.titulo" 
		localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	
</head>

<body onload="ajustarAltoCondicional();<%=funcionBuscar%>">
<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<%--<html:form action="/EXP_Consultas.do?noReset=true" method="POST" target="resultado">--%>
	<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="resultado">	
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "tablaDatosDinamicosD" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden name="DefinirEnviosForm" property="conArchivados" value=""/>
	<tr>				
		<td>
	
	<siga:ConjCampos leyenda="Datos Envios">
	
	<table class="tablaCampos" align="center">
		<tr>
			<td width="18%" class="labelText">
				<html:radio  name="DefinirEnviosForm" property="tipoFecha" value="<%=EnvEnviosAdm.FECHA_CREACION%>"/>			
				<siga:Idioma key="envios.definir.literal.fechacreacion"/>			    
			</td>
			<td width="18%" class="labelText">
				<html:radio  name="DefinirEnviosForm" property="tipoFecha" value="<%=EnvEnviosAdm.FECHA_PROGRAMADA%>"/>			
				<siga:Idioma key="envios.definir.literal.fechaprogramada"/>			    
			</td>
			<td>&nbsp;</td>

			<td class="labelText" width="12%">
				<siga:Idioma key="envios.definir.literal.fechadesde"/>
			</td>				
			<td>
				<siga:Fecha nombreCampo="fechaDesde" readonly="true"></siga:Fecha>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta"/>
			</td>
			<td>
				<siga:Fecha nombreCampo="fechaHasta" readonly="true"></siga:Fecha>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="envios.definir.literal.verArchivados"/>
			</td>
			<td>
				<input type="checkbox" id="idCheckArchivados"/>	
			</td>			
		</tr>	
	</table>

	<table>
		<tr>	
			<td class="labelText" width="10%">
				<siga:Idioma key="envios.definir.literal.identificador"/>
			</td>
			<td width="7%">
				<html:text name="DefinirEnviosForm" property="idEnvioBuscar" size="8" maxlength="12" styleClass="box"></html:text>
			</td>
			
			<td class="labelText" width="10%">
				<siga:Idioma key="envios.definir.literal.nombre"/>
			</td>
			<td width="10%">
				<html:text name="DefinirEnviosForm" property="nombre" size="20" maxlength="100" styleClass="box"></html:text>
			</td>
			
			<td class="labelText" width="8%">
				<siga:Idioma key="envios.definir.literal.estado"/>
			</td>
			<td width="20%">	
				<siga:ComboBD nombre = "idEstado" tipo="cmbEstadoEnvio" ElementoSel="<%=aEstado%>"  clase="boxCombo" obligatorio="true"/>
			</td>		
			
			<td class="labelText">
					<siga:Idioma key="envios.definir.literal.tipoenvio"/>
			</td>
			<td>	
				<siga:ComboBD nombre = "idTipoEnvio" tipo="cmbTipoEnvios" ElementoSel="<%=aTipoEnvio%>"  clase="boxCombo" obligatorio="true"/>				
			</td>	
		</tr>
	
	</table>
	</siga:ConjCampos>
			
	</td>
	</tr>
	</html:form>
	</table>
<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_ENVIOS%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		<c:set var="botones" value="B,N,CON"/>
		<c:if test="${isComision}">
			<c:set var="botones" value="B"/>
			
		</c:if>
		<siga:ConjBotonesBusqueda botones="${botones}"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		function accionInformeEnvios() 
		{		
			document.forms[0].modo.value="descargarEstadistica";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		
		
		function buscarPaginador() 
		{		
//			if (validateBusquedaClientesForm(document.forms[0])) 
			{  
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
			
		}
		function buscar() 
		{
			sub();
			
			if(jQuery("#idCheckArchivados").is(':checked')){
				document.forms[0].conArchivados.value = "1";
			} else{
				document.forms[0].conArchivados.value= "0";
			}
			
			if(document.forms[0].idEnvioBuscar.value!=''){ 
				if (!isAllDigits(document.forms[0].idEnvioBuscar.value)) {
					var mensaje = "<siga:Idioma key="envios.definir.literal.identificador"/> <siga:Idioma key="messages.campoNumerico.error"/>";
			 		alert (mensaje);
			 		fin();
			 		return;
					
		        }	
	        }
			
			document.forms[0].modo.value="buscarInicio";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		
		<!-- Asociada al boton Nuevo -->
			function nuevo() 
			{		
				document.forms[0].modo.value = "nuevo";
   				var datosTabla = ventaModalGeneral(document.forms[0].name,"P");
   				
   				if(datosTabla!=null&&datosTabla.indexOf('%')!=-1) {
   					DefinirEnviosForm.tablaDatosDinamicosD.value=datosTabla;   				
   					DefinirEnviosForm.modo.value="editar";
   					DefinirEnviosForm.target="mainWorkArea";
					DefinirEnviosForm.submit();
					
					//var auxTarget = DefinirEnviosForm.target;
				   	/*DefinirEnviosForm.target="submitArea";
				   	DefinirEnviosForm.submit();
				   	DefinirEnviosForm.target="mainWorkArea";*/
				}
			}	
			function consultas() 
			{		
				document.RecuperarConsultasForm.submit();
			
			}
			function ajustarAltoCondicional()
			{		
				if(document.getElementById("idBotonesAccion"))
					ajusteAltoBotones('resultado');
				else{
					ajusteAlto('resultado');
				}
			}
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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
	<c:if test="${!isComision}">
		<siga:ConjBotonesAccion botones="IE" clase="botonesDetalle"  />
	</c:if>
		 

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
