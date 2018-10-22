<!DOCTYPE html>
<html>
<head>
<!-- busquedaHistorico.jsp -->
<!-- 
	 Muestra el formulario de busqueda en el historial
	 VERSIONES:
	 miguel.villegas 21-12-2004 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	String botonesAccion="N";
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// Datos del cliente a visualizar
	Long idPersona=(Long)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	String accion=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona
	String estadoColegial=(String)request.getAttribute("ESTADOCOLEGIAL"); // Obtengo el estado colegial de la persona
	String idInstitucion=(String)request.getSession().getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion	
	
	Vector<Hashtable<String, Object>> vTiposAuditoria = (Vector<Hashtable<String, Object>>)request.getAttribute("vTiposAuditoria"); // Obtengo la lista de tipos de cambio para las auditorias

	// Institucion del usuario de la aplicacion
	String idInstUsuario=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion

    // Botones a mostrar
	String botones = "B";
		
	String sTipo =request.getParameter("tipo");
	
	if (sTipo!=null){
		
		ses.setAttribute("tipoR",sTipo);
	}else{
		
		if(ses.getAttribute("tipoR")!=null){
			sTipo=ses.getAttribute("tipoR").toString();
		
		}
		else
			sTipo=null;
	}
	
	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botonesAccion="V,N";
	}
			
%>	


	<!-- HEAD -->
		<link rel="stylesheet" type="text/css" href="<html:rewrite page='/html/dropdownchecklist/smoothness-1.8.13/jquery-ui-1.8.13.custom.css'/>">
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>		
		
		<!-- Incluido jquery en siga.js -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="HistoricoForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->


		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
		 <% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.historico.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.historico.cabecera" 
			localizacion="censo.fichaCliente.historico.localizacion"/>
		<%}%>
		
		<!-- FIN: TITULO Y LOCALIZACION -->		
 	
	</head>

	<body onLoad="ajusteAltoBotones('resultado');" class="tablaCentralCampos">

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->

	    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="censo.busquedaHistorico.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
		    <%if(!numero.equalsIgnoreCase("")){%>
				<%if (estadoColegial!=null && !estadoColegial.equals("")){%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
					 <%= UtilidadesString.mostrarDatoJSP(numero)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
				 <%}else{%> 
				 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
				 <%}%>
			<%} 
			else {%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<%}%>
		</td>
		</tr>
		</table>
		
		<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->

		<table class="tablaCentralCampos" align="center">
				
			<html:form action="/CEN_Historico.do" method="POST" target="resultado">

				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "actionModal" value=""/>
				<html:hidden property = "jsonVolver" />
				<html:hidden property = "idsTipoCambio" />
				
				<tr>				
					<td class="labelText">
						<siga:Idioma key="censo.consultaHistorico.literal.tipo"/>
					</td>				
					<td>
						<select id="listaIdTipoCambio" multiple="multiple" styleClass="boxCombo" style="width:250px;">
							<option value=""><siga:Idioma key="censo.consultaHistorico.literal.tipo.todasNinguna"/></option>
<% 
							for (int i = 0; i < vTiposAuditoria.size(); i++) {
								Hashtable<String, Object> hTipoAuditoria = (Hashtable<String, Object>)vTiposAuditoria.get(i);
								String sIdTipoAuditoria = (String) hTipoAuditoria.get("ID");
								String sDescripcionTipoAuditoria = (String) hTipoAuditoria.get("DESCRIPCION");
%>
								<option value="<%=sIdTipoAuditoria%>"><%=sDescripcionTipoAuditoria%></option>
<%
							} 
%>	
						</select>
					</td>
					
					<td class="labelText"><siga:Idioma key="censo.consultaHistorico.literal.motivo"/></td>				
					<td><html:text property="motivo" size="20" maxlength="50" styleClass="box" value = "" /></td>
					
					<td class="labelText" nowrap><siga:Idioma key="censo.busquedaHistorico.literal.fechaEfectivaInicio"/></td>					
					<td><siga:Fecha nombreCampo="fechaInicio"/></td>
										
					<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>					
					<td><siga:Fecha nombreCampo="fechaFin"/></td>
				</tr>
			</html:form>
		</table>
		<!-- FIN: CAMPOS DE BUSQUEDA-->


		<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->

        <siga:ConjBotonesBusqueda botones="<%=botones%>" modo = "<%=accion%>"  titulo="" />
		

		<!-- FIN: BOTONES BUSQUEDA -->
	
		<!-- INICIO: IFRAME LISTA RESULTADOS -->
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0"				 
						class="frameGeneral">
		</iframe>
		<!-- FIN: IFRAME LISTA RESULTADOS -->
	
     <!-- BOTONES -->
	 <siga:ConjBotonesAccion botones="<%=botonesAccion%>"   modo="<%=accion%>" clase="botonesDetalle"/>		

	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">
		jQuery.noConflict();
		jQuery(document).ready(function() {
	        
	        
	        jQuery('#ddcl-listaIdTipoCambio').click(function(){
	        	jQueryTop("#ddcl-listaIdTipoCambio-ddw", window.document).show();
	        });
	        
	        if (document.forms[0].jsonVolver.value!='') {
		        var jSonVolverObject =  jQuery.parseJSON(document.forms[0].jsonVolver.value);
				if (jSonVolverObject.nombreFormulario == 'HistoricoForm') {
					
					var listaIdsTipoCambio = jSonVolverObject.listaIdTipoCambio;
					var arrayIdsTipoCambio = listaIdsTipoCambio.split(",");
					
					for (var i=0; i<arrayIdsTipoCambio.length; i++) {
						jQuery("#listaIdTipoCambio option[value='" + arrayIdsTipoCambio[i] + "']").attr("selected", "selected");
					}
					
					document.forms['HistoricoForm'].fechaInicio.value = jSonVolverObject.fechaInicio;
					document.forms['HistoricoForm'].fechaFin.value = jSonVolverObject.fechaFin;
					document.forms['HistoricoForm'].motivo.value = jSonVolverObject.motivo;
					buscar();
				}
	        }
	  	});		
		
		// Funcion asociada a boton buscar
		function buscar() {		
			sub();
			if (compararFecha (document.forms[0].fechaInicio, document.forms[0].fechaFin) == 1) {
				var mensaje='<siga:Idioma key="messages.fechas.rangoFechas"/>';
				alert(mensaje);
				fin();
				return false;
			} else {
				var listaIdTipoCambio = jQuery("#listaIdTipoCambio").val();
				if (listaIdTipoCambio) {
					if (listaIdTipoCambio.toString().substring(0,1)==',') {
						listaIdTipoCambio = listaIdTipoCambio.toString().substring(1);
					}
					document.forms[0].idsTipoCambio.value = listaIdTipoCambio;
				} else {
					document.forms[0].idsTipoCambio.value = "";
				}
				
				jQueryTop("#ddcl-listaIdTipoCambio-ddw", window.document).hide();
				
				document.forms[0].modo.value='buscarPor';
				document.forms[0].target='resultado';				
				document.forms[0].submit();
			}	
		}
	
		// Funcion asociada a boton busqueda avanzada
		function buscarAvanzada() { }
	
		// Funcion asociada a boton busqueda simple
		function buscarSimple() { }
	
		// Funcion asociada a boton limpiar
		function limpiar() {		
			document.forms[0].modo.value='abrirAvanzada';
			document.forms[0].target='';			
			document.forms[0].submit();	
		}
		
		// Funcion asociada a boton Nuevo
		function nuevo() {		
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target= "_self";
			document.forms[0].submit();
			fin();
		}	

		function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target= "_self";
			document.forms[0].submit();
			fin();
		}			
  
		function refrescarLocal() {
			buscar();
		}
	</script>

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
			

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	</body>
</html>
