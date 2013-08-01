<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
<!-- datosPagoResultados.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 david.sanchez 31-03-2005 creacion
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
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	Vector resultado = (Vector) request.getAttribute("resultadosCriteriosPago");
	String modo = (String) request.getAttribute("modo");
	boolean disable = false;
	String botones = "MT,DT,Y,C";
	String left = "-15";//Atributo left del div de los botones en modo Edicion.
	
	if (modo.equalsIgnoreCase("consulta")) {
		disable = true;
		botones = "C";
		left = "0";
	}
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

 	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.datosPagos.literal.criteriosPagos"/>
		</td>
	</tr>
	</table>


		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FCS_DatosGeneralesPago.do?noReset=true" method="POST" target="submitArea" style="display:none">
		<html:hidden name="datosGeneralesPagoForm" property="modo" value="<%=modo%>" />
		<html:hidden name="datosGeneralesPagoForm" property="idPagosJG" />
		<html:hidden name="datosGeneralesPagoForm" property="idFacturacion" />
		<html:hidden name="datosGeneralesPagoForm" property="idInstitucion" />
		<html:hidden name="datosGeneralesPagoForm" property="cadenaCriteriosPago" />
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>
		<logic:empty name="datosGeneralesPagoForm"	property="criterios">
				<br>
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 			<br>
			</logic:empty>
			<logic:notEmpty name="datosGeneralesPagoForm"	property="criterios">
			
				<siga:Table 
				   name="tablaDatos"
				   border="1"
				   columnNames="factSJCS.datosFacturacion.literal.gruposFacturacion,factSJCS.datosFacturacion.literal.hitos,"
				   columnSizes="45,45,10">
				
				
			
						
							<logic:iterate id="criterio" name="datosGeneralesPagoForm" property="criterios" indexId="index">
								<input type="hidden" name="criterios" id="${index}" value="${index}" />
								<input type="hidden" id="idGrupoFacturacion_${index}" value="${criterio.idGrupoFacturacion}" />
								<input type="hidden" id="idHitoGeneral_${index}" value="${criterio.idHitoGeneral}" />
							<c:set var="disabledCriterio" value="" />
							<c:set var="checkedCriterio" value="" />
							<c:if test="${criterio.checkCriterio=='SI'}">
								<c:set var="checkedCriterio" value="checked" />
							</c:if>
							<c:if	test="${modo=='consulta'}">
								<c:set var="disabledCriterio" value="disabled" />
							</c:if>

							<c:choose>
								<c:when test="${index%2==0}">
									<tr id="fila_<bean:write name='index'/>" class="filaTablaPar">
								</c:when>
								<c:otherwise>
								<tr id="fila_<bean:write name='index'/>" class="filaTablaImpar">
								</c:otherwise>
							</c:choose>
								
								<td >
									<c:out value="${criterio.grupoFacturacion}"/>
								</td>
								<td>
									<c:out value="${criterio.hitoGeneral}"/>
								</td>
								<td align="center">
									
										<input type="checkbox"   id="checkCriterio_${index}" ${disabledCriterio} ${checkedCriterio}/>
									
								
									 
								</td>								
							</tr>
							</logic:iterate>
							<!-- FIN REGISTRO -->
							<!-- FIN: ZONA DE REGISTROS -->
			</siga:Table>
			</logic:notEmpty>	

			


		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle" modal="P" modo="<%=modo%>"/>
		
	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	function accionMarcarTodos() 
	{		
		
		criteriosjta = document.getElementsByName("criterios");
		for ( var i = 0; i < criteriosjta.length; i++) {
			var array_element = criteriosjta[i];
			var checkCriterio=document.getElementById("checkCriterio_"+array_element.id);
			checkCriterio.checked = "checked";
		}
		
			
	}

	<!-- Asociada al boton DesmarcarTodos -->
	function accionDesmarcarTodos() 
	{		
		criteriosjta = document.getElementsByName("criterios");
		for ( var i = 0; i < criteriosjta.length; i++) {
			var array_element = criteriosjta[i];
			var checkCriterio=document.getElementById("checkCriterio_"+array_element.id);
			checkCriterio.checked = "";
		}
	}
		
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			///<input type="hidden" id="idGrupoFacturacion_${index}" value="${criterio.idGrupoFacturacion}" />
			///<input type="hidden" id="idHitoGeneral_${index}" value="${criterio.idHitoGeneral}" />
			///<input type="hidden" id="iidPagosJG_${index}" value="${criterio.idPagosJG}" />
			
			cadena = "";
			criteriosjta = document.getElementsByName("criterios");
			for ( var i = 0; i < criteriosjta.length; i++) {
				var array_element = criteriosjta[i];
				var checkCriterio=document.getElementById("checkCriterio_"+array_element.id);
				idGrupoFacturacion = document.getElementById("idGrupoFacturacion_"+array_element.id).value;
				idHitoGeneral = document.getElementById("idHitoGeneral_"+array_element.id).value;
				cadena+= idGrupoFacturacion+","+idHitoGeneral+","+checkCriterio.checked+"##";
				
				
			}
			document.datosGeneralesPagoForm.cadenaCriteriosPago.value = cadena;
			
			var f=document.getElementById("datosGeneralesPagoForm");
			f.target = "submitArea";			
			f.modo.value = "insertarCriteriosPagos";
			f.submit();	
			window.top.returnValue="MODIFICADO";
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
