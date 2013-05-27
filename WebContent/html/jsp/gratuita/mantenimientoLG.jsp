<!-- mantenimientoLG.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Properties"%>
<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:	
	String accion = request.getAttribute("accion") == null?"":(String)request.getAttribute("accion");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");
	String comunicacion = request.getAttribute("comunicacion")==null?"":(String)request.getAttribute("comunicacion");	

	boolean desactivado = false;
	String clase = "box";
	String valida="";
	if (modo.equalsIgnoreCase("ver")) { desactivado=true; clase="boxConsulta"; valida="disabled";}
	
	String nombre = request.getAttribute("NOMBRE")==null?"":(String)request.getAttribute("NOMBRE");
	String lugar = "";
	if(request.getAttribute("LUGAR") != null && !((String)request.getAttribute("LUGAR")).equals("&nbsp;")){
			lugar = (String)request.getAttribute("LUGAR");
	}
	String observaciones = request.getAttribute("OBSERVACIONES")==null?"":(String)request.getAttribute("OBSERVACIONES");
	String idlista = request.getAttribute("IDLISTA")==null?"":(String)request.getAttribute("IDLISTA");
	String idinstitucion = request.getAttribute("IDINSTITUCION")==null?"":(String)request.getAttribute("IDINSTITUCION");
	
	String localizacion = "gratuita.mantenimientoLG.literal.localizacion";
	String titulo="";
	if (modo.toUpperCase().equals("VER"))
	{
		localizacion = "gratuita.mantenimientoLG.literal.localizacion.ver";
		titulo = "gratuita.busquedaLG.literal.titulo";
	}
	else {
		if (modo.toUpperCase().equals("EDITAR")) {
			if (accion.toUpperCase().equals("NUEVO"))
			{
				localizacion = "gratuita.mantenimientoLG.literal.localizacion.nuevo";
				titulo = "gratuita.busquedaLG.nuevaLista";
			}
			else
			{
				titulo = "gratuita.busquedaLG.literal.titulo";
				localizacion = "gratuita.mantenimientoLG.literal.localizacion.editar";
			}		
		}
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DefinirListaGuardiasForm" staticJavascript="false" />   
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="<%=titulo%>" 
		localizacion="gratuita.busquedaLG.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="ajusteAltoBotones('resultado');buscarGuardias()">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->	
	<fieldset>
	<table class="tablaCentralCampos" align="center">
		<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="submitArea">	
		<html:hidden property = "usuMod" value = ""/>
		<html:hidden property = "modo" value = "<%=modo%>"/>
		<html:hidden property = "accion" value = "<%=accion%>"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idLista" value = "<%=idlista%>"/>
		<html:hidden property = "idInstitucion" value = "<%=idinstitucion%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "comunicacion"/>			
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.mantenimientoLG.literal.nombre"/>&nbsp;(*)
		</td>	
		<td class="labelText">	
			<% if (modo.equalsIgnoreCase("EDITAR")) {%>
				<html:text name="DefinirListaGuardiasForm" property="listaGuardias" size="50" maxlength="100" styleClass="<%=clase%>" value="<%=nombre%>" readOnly="<%=desactivado%>"></html:text>
			<% } else { %>
				<html:text name="DefinirListaGuardiasForm" property="listaGuardias" size="50" maxlength="100" styleClass="<%=clase%>" value="<%=nombre%>" readOnly="<%=desactivado%>"></html:text>			
			<% } %>
		</td>
	</tr>
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.mantenimientoLG.literal.lugar"/>
		</td>	
		<td class="labelText">	
		    <% if (modo.equalsIgnoreCase("EDITAR")) {%>
				<html:text name="DefinirListaGuardiasForm" property="lugar" size="50" maxlength="100" styleClass="<%=clase%>" value="<%=lugar%>" readOnly="<%=desactivado%>" ></html:text>
			<% } else { %>
				<html:text name="DefinirListaGuardiasForm" property="lugar" size="50" maxlength="100" styleClass="<%=clase%>" value="<%=lugar%>" readOnly="<%=desactivado%>" ></html:text>			
			<% } %>
		</td>
	</tr>
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.mantenimientoLG.literal.observaciones"/>
		</td>
		<td class="labelText">	
			<textarea onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" <%=valida%> name="observaciones" cols="191" rows="3" class="box" style="overflow:auto"><%=observaciones%></textarea>
		</td>	
	</tr>	
	</html:form>	
	</table>	
	</fieldset>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->

     <% if(accion.equals("nuevo")) { %> 
        <siga:ConjBotonesAccion botones="G,R,COM" clase="botonesSeguido" modo="<%=modo%>" />
    <% } else {%>
	<% if (modo.equalsIgnoreCase("EDITAR")) {
		if(comunicacion.equalsIgnoreCase("true")){%>
		<siga:ConjBotonesAccion botones="G,R,COMC" clase="botonesSeguido" modo="<%=modo%>" />
		<% } else {%>
		<siga:ConjBotonesAccion botones="G,R,GM" clase="botonesSeguido" modo="<%=modo%>" />
	<% } } else {%>
		<siga:ConjBotonesAccion botones="" clase="botonesSeguido" modo="<%=modo%>" />
	<% } 
	   }%>
		
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		
		//Funcion asociada a la recarga del iframe de la tabla. -->
		function buscarGuardias() {
			if (document.forms[0].accion.value != "nuevo")
			{
				document.forms[0].accion.value=document.forms[0].modo.value	;			
				document.forms[0].modo.value="buscar";		
				document.forms[0].target="resultado";
				document.forms[0].submit();
			}
		}
		
		//Funcion asociada a la recarga de la pagina cuando se pulsa en Guardar -->
		function refrescarLocal() 
		{				
			if (document.forms[0].accion.value != "nuevo")
			{
				document.forms[0].modo.value="buscar";		
				document.forms[0].target="resultado";
				document.forms[0].submit();
			}
			//Si entro desde nuevo para insertar y he refrescado actualizo el campo accion para que modifique en vez de insertar.
			else {
				//Volvemos a editar
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].accion.value= "editarNuevo";
				document.forms[0].submit();
			}
		}		

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
				document.forms[0].reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			sub();
			if (validateDefinirListaGuardiasForm(document.DefinirListaGuardiasForm)){
				if (document.forms[0].accion.value == "nuevo")	
					document.forms[0].modo.value = "insertar";	
				else
					document.forms[0].modo.value = "modificar";	
				document.forms[0].target = "submitArea";							
				document.forms[0].submit();
			}else{
				fin();
				return false;
			}
		}
		
		function accionGenerarInforme() 
		{ 
			document.forms[0].modo.value	= "generarInforme";
			document.forms[0].comunicacion.value = "";
	   		var resultado = ventaModalGeneral(document.forms[0].name,"P");
			
	   		if(resultado == "MODIFICADO"){
	   			buscar();
	   		}else if (resultado == ""){
	   			alert('<siga:Idioma key="messages.listaGuardias.definirListaGuardias.generarInforme.sinGuardias.error"/>');
	   		}
	
		}
		//Asociada al boton Guardar -->
		function accionComunicar() 
		{	
			document.forms[0].modo.value	= "generarInforme";
			document.forms[0].comunicacion.value = "true";
	   		var resultado = ventaModalGeneral(document.forms[0].name,"P");
	   		if(resultado == "MODIFICADO"){
	   			buscar();
	   		}else if (resultado == ""){
	   			alert('<siga:Idioma key="messages.listaGuardias.definirListaGuardias.generarInforme.sinGuardias.error"/>');
	   		}
		}
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


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
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<% if(accion.equals("nuevo")) { %>
	<siga:ConjBotonesAccion botones="V"  clase="botonesDetalle" modo="<%=modo%>"/>
	<% } else { %>
	<siga:ConjBotonesAccion botones="V,N"  clase="botonesDetalle" modo="<%=modo%>"/>	
	<% } %>
	<!-- FIN: BOTONES BUSQUEDA -->
		
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton volver -->
		function accionVolver() 
		{
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}		
			
		//Funcion asociada a boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			document.forms[0].accion.value = "modal";			

			var salida = ventaModalGeneral(document.forms[0].name,"G"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		



<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
</body>
</html>