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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
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

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

 	 	<style type="text/css">
	 	.boxConsultaNegro {
			font-family: <%=src.get("font.style")%>;
			font-weight: normal;
			margin: auto;
			padding-left: 5px;
			vertical-align: top;
			text-align: left;
			padding-top: 3px;
			padding-bottom: 3px;
			border:none;
			background-color:transparent;
			color:#000000;
			width:100%;
		}
	</style>
 	
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
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>	

			<%
				String tamanosCol = "45,45,10";
				String nombresCol = "factSJCS.datosFacturacion.literal.gruposFacturacion," +
									"factSJCS.datosFacturacion.literal.hitos,";
				if (resultado==null || resultado.size()==0) { %>			
			   		 <div class="notFound">
			<br><br>
	   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
			</div>
			 <% } else { %>
				<siga:Table 
				   name="tablaDatos"
				   border="1"
				   columnNames="<%=nombresCol %>"
				   columnSizes="<%=tamanosCol %>">
							<logic:iterate id="criterio" name="datosGeneralesPagoForm" property="criterios">
							<tr class="listaNonEdit">
								<html:hidden name="criterio" property="idGrupoFacturacion" indexed="true" />
								<html:hidden name="criterio" property="idHitoGeneral" indexed="true" />
								<html:hidden name="criterio" property="idPagosJG" indexed="true" />
								<td align="center">
									<html:text name="criterio" property="grupoFacturacion" indexed="true" readonly="true" styleClass="boxConsultaNegro"/>
								</td>
								<td align="center">
									<html:text name="criterio" property="hitoGeneral" indexed="true" readonly="true" styleClass="boxConsultaNegro" />
								</td>
								<td align="center">
									<html:checkbox name="criterio" property="checkCriterio" indexed="true" value="SI" disabled="<%=disable%>"/>
								</td>								
							</tr>
							</logic:iterate>
							<!-- FIN REGISTRO -->
							<!-- FIN: ZONA DE REGISTROS -->
			</siga:Table>
			<% } //fin del else%>


		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle" modal="P" modo="<%=modo%>"/>
		
	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		<!-- Asociada al boton MarcarTodos -->
		function accionMarcarTodos() 
		{		
			if (document.getElementById("criterio[0].checkCriterio")!=null){
				for (i = 0; i < <%=resultado.size()%>; i++){
					
					seleccionado = document.getElementById('criterio['+i+'].checkCriterio');
					seleccionado.checked="SI";		
				}	
			}	
		}
	
		<!-- Asociada al boton DesmarcarTodos -->
		function accionDesmarcarTodos() 
		{		
			if (document.getElementById("criterio[0].checkCriterio")!=null){
				for (i = 0; i < <%=resultado.size()%>; i++){
					seleccionado = document.getElementById('criterio['+i+'].checkCriterio');
					seleccionado.checked="";		
				}
			}	
		}
				
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
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
