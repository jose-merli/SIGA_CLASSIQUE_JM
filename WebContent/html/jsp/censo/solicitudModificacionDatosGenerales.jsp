<!-- solicitudModificacionDatosGenerales.jsp -->
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
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	DatosGeneralesForm miForm = (DatosGeneralesForm) request.getAttribute("datosGeneralesForm");
	String idPersona = miForm.getIdPersona();
	String idInstitucion = miForm.getIdInstitucion();
	
	Vector resultado = (Vector) request.getAttribute("CenSolititudDatosGeneralesResultados");
	Vector resultadoPersona = (Vector) request.getAttribute("CenSolititudDatosGeneralesPersonaResultados");
	
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	String numeroColegiado = "";
	boolean bColegiado = false;
	String publicidad = "";
	String guiaJudicial = "";
	String abono = "";
	String cargo = "";
	String idioma = "";
	ArrayList idiomaSel = new ArrayList();
	
	if (resultado!=null && resultado.size()>0) {

		// SOLO VA A TENER UN REGISTRO 
		CenClienteBean registro = (CenClienteBean) resultado.get(0);
	
		if (resultadoPersona!=null && resultadoPersona.size()>0) {
			CenPersonaBean registroPersona = (CenPersonaBean) resultadoPersona.get(0);
			nombre = (String) registroPersona.getNombre();
			if (nombre==null) nombre=""; 
			apellido1 = (String) registroPersona.getApellido1();
			if (apellido1==null) apellido1=""; 
			apellido2 = (String) registroPersona.getApellido2();
			if (apellido2==null) apellido2=""; 
		}

		numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
		bColegiado = false;
		if (numeroColegiado!=null) {
			bColegiado=true;
		} 

		publicidad = (String) registro.getPublicidad();
		if (publicidad==null) publicidad=""; 
		guiaJudicial = (String) registro.getGuiaJudicial();
		if (guiaJudicial==null) guiaJudicial=""; 
		abono = (String) registro.getAbonosBanco();
		if (abono==null) abono=""; 
		cargo = (String) registro.getCargosBanco();
		if (cargo==null) cargo=""; 
		idioma = (String) registro.getIdLenguaje();
		if (idioma==null) idioma=""; 

		idiomaSel.add((String) registro.getIdLenguaje());

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

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.solicitudModificacion.literal.titulo"
		localizacion="censo.solicitudModificacion.literal.titulo" />
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosGeneralesSolicForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>

<body>

<%	if (resultado!=null && resultado.size()>0) { %>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="40">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1"/>	
			 <%=UtilidadesString.mostrarDatoJSP(nombre) + " " + UtilidadesString.mostrarDatoJSP(apellido1) + " " + UtilidadesString.mostrarDatoJSP(apellido2) %>
			 &nbsp; 		
<% if (bColegiado) { %>
			<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
			 <%= UtilidadesString.mostrarDatoJSP(numeroColegiado) %>
<% } else { %>
			<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<% }  %>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->


	<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">

	<table class="tablaCampos" align="center">
	<html:form action="/CEN_SolicitudDatosGenerales" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idInstitucion" value = "<%=idInstitucion %>"/>
	<html:hidden property = "idPersona" value = "<%=idPersona %>"/>

	<tr>
	<td class="labelText">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.publicidad"/>
	</td>				
	<td>
	<% 
	   if (publicidad.equals(ClsConstants.DB_TRUE)) { 
	%>
		<input type="checkbox" name="publicidad" value="<%=ClsConstants.DB_TRUE %>"   checked />
	<% } else { %>
		<input type="checkbox" name="publicidad"  value="<%=ClsConstants.DB_TRUE %>"  />
	<% } %>
	</td>

	<td class="labelText">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.guiaJudicial"/>
	</td>				
	<td>
	<% 
	   if (guiaJudicial.equals(ClsConstants.DB_TRUE)) { 
	%>
		<input type="checkbox" name="guiaJudicial"  value="<%=ClsConstants.DB_TRUE %>"   checked />
	<% } else { %>
		<input type="checkbox" name="guiaJudicial"  value="<%=ClsConstants.DB_TRUE %>"  />
	<% } %>
	</td>

	</tr>

	<!-- FILA -->
	<tr>				

	<td class="labelText" style="display:none">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.abono"/>
	</td>				

	<td style="display:none">
		<!-- option select -->
		<html:select name="datosGeneralesSolicForm" property="abono" style = "null" styleClass = "box" value="<%=abono %>" >
			<html:option value="<%=ClsConstants.TIPO_CARGO_BANCO %>" ><siga:Idioma key="censo.tipoAbono.banco"/></html:option>
			<html:option value="<%=ClsConstants.TIPO_CARGO_CAJA %>" ><siga:Idioma key="censo.tipoAbono.caja"/></html:option>
		</html:select>						
	</td>

	<td class="labelText" style="display:none">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.cargo"/>
	</td>				
	<td style="display:none">
		<!-- option select -->
		<html:select name="datosGeneralesSolicForm" property="cargo" style = "null" styleClass = "box" value="<%=cargo %>"  >
			<html:option value="<%=ClsConstants.TIPO_CARGO_BANCO %>" ><siga:Idioma key="censo.tipoAbono.banco"/></html:option>
			<html:option value="<%=ClsConstants.TIPO_CARGO_CAJA %>" ><siga:Idioma key="censo.tipoAbono.caja"/></html:option>
		</html:select>						
	</td>
	
	</tr>

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.idioma"/>  &nbsp;(*)
	</td>				
	<td colspan="3">
		<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="box" obligatorio="true" elementoSel="<%=idiomaSel %>" obligatorioSinTextoSeleccionar="true"/>
	</td>

	</tr>

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.consultaDatosGenerales.literal.motivo"/>  &nbsp;(*)
	</td>				
	<td colspan="5" >
		<textarea cols="50" rows="4" name="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" class="box" style="overflow:hidden"></textarea>	
	</td>

	</tr>
	</html:form>
	
	</table>

	</siga:ConjCampos>






	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

 
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			sub();
			if (validateDatosGeneralesSolicForm(document.forms[0])) 
			{
				document.forms[0].modo.value="insertarSolicitud";
				document.forms[0].submit();
			}else{
				fin();
			}
			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- FIN ******* CAPA DE PRESENTACION ****** -->

<%	}  %>
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>