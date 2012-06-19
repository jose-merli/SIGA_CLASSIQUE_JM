<!-- datosServiciosSolicitados.jsp -->
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
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	boolean bOcultarHistorico = usrbean.getOcultarHistorico();

	String pos=(String)request.getAttribute("POS");
	String idTipoServicio = (String)request.getAttribute("CenDatosIdTipo");
	String idServicio = (String)request.getAttribute("CenDatosId");
	String idServicioInstitucion = (String)request.getAttribute("CenDatosIdPSInstitucion");
	String idPeticion = (String)request.getAttribute("CenDatosIdPeticion");
	
	String fechaEfectiva = (String)request.getAttribute("CenFechaEfectiva");
	if(fechaEfectiva==null){
		fechaEfectiva=UtilidadesBDAdm.getFechaBD("");
	}
	
	String numeroColegiado = "";
	boolean bColegiado = false;
	String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
	String cliente = UtilidadesString.getMensajeIdioma(usrbean, colegiado);

	numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
	if (numeroColegiado!=null) {
		bColegiado=true;
	} 

	String nombreApellidos = (String) request.getAttribute("CenDatosGeneralesNombreApellidos");
	if (nombreApellidos==null) {
		nombreApellidos="";
	} 

	String busc = "";

	busc = UtilidadesString.getMensajeIdioma(usrbean,"cen.consultaServicios.titulo1");
	busc += "&nbsp;" + nombreApellidos + "&nbsp;";
	if (bColegiado) { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.colegiado") + " " + numeroColegiado;
	} else { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.NoColegiado");
	}  

%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	


	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosFacturacionForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<%=busc %>
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

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/CEN_Facturacion.do" method="POST" target="submitArea">
	<html:hidden name="datosFacturacionForm"  property = "modo" value = ""/>
	<html:hidden name="datosFacturacionForm"  property = "idInstitucion" />
	<html:hidden name="datosFacturacionForm"  property = "idPersona" />
	<input type="hidden" name="pos" value="<%=pos %>">

	<!-- datos modificacion -->
	<html:hidden  name="datosFacturacionForm" property="motivo"/>
	<input type="hidden" name="idPeticionSel" value="<%=idPeticion%>" >
	<input type="hidden" name="idTipoServicioSel" value="<%=idTipoServicio%>" >
	<input type="hidden" name="idServicioSel" value="<%=idServicio%>" >
	<input type="hidden" name="idServicioInstitucionSel" value="<%=idServicioInstitucion%>" >
	<input type="hidden" name="idPeticion" value="<%=idPeticion%>" >

	<!-- FILA -->
	<tr>				
	<td>

	<fieldset>

	<table class="tablaCampos" align="center">

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="pys.solicitarBaja.literal.fechaEfectiva"/> (*)
	</td>				
	<td>
			<siga:Fecha  nombreCampo= "fechaEfectiva" valorInicial="<%=fechaEfectiva %>" posicionX="50" posicionY="10"/>
	</td>
	

	</tr>

	</table>

	</fieldset>


	</td>
	</tr>

	</html:form>
	
	</table>



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
			if (document.forms[0].fechaEfectiva.value=="") {
				var msg="<siga:Idioma key="messages.servicios.fechaEfectivaObligatoria"/>";
				alert(msg);
				fin();
				return false;
			} else {
				document.forms[0].modo.value="grabarCambiarFecha";
				document.forms[0].submit();
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

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
