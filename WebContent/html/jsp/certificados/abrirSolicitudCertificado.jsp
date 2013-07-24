<!DOCTYPE html>
<html>
<head>
<!-- abrirSolicitudCertificado.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 19-04-2005 Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.UsrBean,java.util.*,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	
	// MAV 26/8/2005 Resolucion de incidencia relacionada con no aparicion de datos si se trata de un letrado
	String nombre = (String)request.getAttribute("nombre");
	String apellido1 = (String)request.getAttribute("apellido1");
	String apellido2 = (String)request.getAttribute("apellido2");
	String nif = (String)request.getAttribute("nif");		
	String idPersona = (String)request.getAttribute("idPersona");
	int institucion = Integer.parseInt(idInstitucion);
	
	boolean bLectura = userBean.getAccessType().equals(SIGAConstants.ACCESS_READ);
	
	ArrayList aInstitucion = new ArrayList();
	aInstitucion.add(idInstitucion);

	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(3);
	
	String buscar = UtilidadesString.getMensajeIdioma(userBean, "general.search");

	String fechaSolicitud = UtilidadesBDAdm.getFechaBD("");
	String [] parametro = {userBean.getLocation(),userBean.getLocation()};
	
	String consultaOrigen, consultaDestino = "";

	if (institucion == 2000){ // General
		consultaOrigen = "cmbColegiosAbreviados";
		consultaDestino = "cmbColegiosAbreviados";
	}else if (institucion > 3000){  // Consejo
		consultaOrigen = "cmbColegiosConsejo";
		consultaDestino = "cmbColegiosConsejoRelacionados";
	}else{ // Colegio
		consultaOrigen = "cmbColegiosConsejo";
		consultaDestino = "cmbColegiosAbreviados";
	}
	

%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- Validaciones en Cliente -->
	<html:javascript formName="SolicitudCertificadoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="certificados.comunicacionydiligencia.literal.titulo" 
		localizacion="certificados.solicitudes.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
		
</head>

<body>

	<% if (bLectura) {%>
	<br><br>
	<table align="center"><tr><td class="labelText">
		<siga:Idioma key="messages.certificados.error.acceso"/>
	</td></tr></table>
	<%} else{ %>


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	
	<html:form action="/CER_SolicitudCertificado" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden property = "idPersona" value="<%=idPersona%>"/>
	<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
	
	<siga:ConjCampos leyenda="certificados.solicitudes.literal.solicitante">

	<table class="tablaCampos" align="center">
	
	<tr>		
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>&nbsp(*)
		</td>				
		<td>
			<html:text name="SolicitudCertificadoForm" property="nombre" styleClass="boxConsulta" readonly="true" value="<%=nombre%>"></html:text>
		</td>			
	
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>&nbsp(*)
		</td>				
		<td>		
			<html:text name="SolicitudCertificadoForm" property="apellido1" styleClass="boxConsulta" readonly="true" value="<%=apellido1%>"></html:text>
		</td>
		
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>&nbsp(*)
		</td>				
		<td>		
			<html:text name="SolicitudCertificadoForm" property="apellido2" styleClass="boxConsulta" readonly="true" value="<%=apellido2%>"></html:text>
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.nif"/>&nbsp(*)
		</td>
		<td>
			<html:text name="SolicitudCertificadoForm" property="nif" styleClass="boxConsulta" readonly="true" value="<%=nif%>"></html:text>
		</td>
		<td colspan="3">
			&nbsp;
		</td>
		
	 <% if (!userBean.isLetrado())	{ %>
			<td align="right">
				<input type="button" class="button" name="idButton" id="idButton" alt="<%=buscar%>" id="searchPerson"  onclick="return buscarPersona();" value="<%=buscar%>"/>&nbsp;
			</td>
	 <% } %>
	</tr>
		
	</table>

	</siga:ConjCampos>
	
	
	<siga:ConjCampos leyenda="certificados.solicitudes.literal.instituciones">

	<table class="tablaCampos" align="center">


	
	<tr>
		<td class="labelText">
			<siga:Idioma key="certificados.solicitudes.literal.institucionorigen"/>&nbsp(*)
		</td>				
		<td>
			<siga:ComboBD nombre="idInstitucionOrigen" tipo="<%=consultaOrigen%>" obligatorio="true" parametro="<%=parametro%>" ElementoSel="<%=aInstitucion%>" clase="boxCombo"/>
		</td>
	
		<td class="labelText">
			<siga:Idioma key="certificados.solicitudes.literal.institucionDestino2"/>&nbsp(*)
		</td>				
		<td>
			<siga:ComboBD nombre = "idInstitucionDestino" tipo="<%=consultaDestino%>" parametro="<%=parametro%>" obligatorio="true" clase="boxCombo"/>
		</td>
	</tr>
	
	</table>
		
	</siga:ConjCampos>
	<br>
	<siga:ConjCampos>

	<table class="tablaCampos" align="center">

	<tr>	
		<td class="labelText" width="55">
			<siga:Idioma key="certificados.solicitudes.literal.descripcion"/>
		</td>				
		<td>
			<html:text name="SolicitudCertificadoForm" property="descripcion" styleClass="box" size="130" maxlength="200"></html:text>
		</td>
	</tr>
	
	</table>
		
	<table class="tablaCampos" align="center">

	<tr>	
		<td class="labelText" width="120">
			<siga:Idioma key="certificados.solicitudes.literal.fechaSolicitud"/>
		</td>				
		<td>
			<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSolicitud%>"></siga:Fecha>
		</td>
		<td class="labelText" width="120">
			<siga:Idioma key="certificados.solicitudes.literal.metodoSolicitud"/>
		</td>				
		<td>
			<siga:ComboBD nombre="metodoSolicitud" tipo="comboMetodoSolicitud" obligatorio="false" parametro="<%=parametro%>" ElementoSel="<%=aMetodoSol%>" clase="boxCombo"/>
		</td>
	</tr>
	
	</table>
		
	</siga:ConjCampos>
	
	</html:form>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
		<siga:ConjBotonesAccion botones="G" clase="botonesDetalle" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea">
			<input type="hidden" name="actionModal" value="">
			<html:hidden property = "modo" value = "abrirBuscarPersona"/>			
	</html:form>	


	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscarPersona() 
		{				
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value=resultado[0];
				document.forms[0].idInstitucion.value=resultado[1];
				document.forms[0].nif.value=resultado[2];
				document.forms[0].nombre.value=resultado[3];
				document.forms[0].apellido1.value=resultado[4];
				document.forms[0].apellido2.value=resultado[5];
			}
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{			
			sub();
			if (validateSolicitudCertificadoForm(document.SolicitudCertificadoForm)){
				SolicitudCertificadoForm.modo.value='confirmar';
				SolicitudCertificadoForm.submit();
			}else{
				fin();
			}
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<%}%>
</body>
</html>
