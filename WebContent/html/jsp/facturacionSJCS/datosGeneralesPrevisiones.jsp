<!DOCTYPE html>
<html>
<head>
<!-- datosGeneralesPrevisiones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	request.setAttribute("nuevoCriterio","si");
	
	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	FcsFacturacionJGBean facturaBean = (FcsFacturacionJGBean)request.getAttribute("datosFactura");
	
	String regularizacion = (String) request.getAttribute("FcsRegularizacion");
	
	// Para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("SJCSBusquedaPrevisionesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}

	// meto en session el indicador de que estamos en prevision
	request.getSession().setAttribute("prevision","S");
		
	// Campos de la factura a mostrar en la jsp
	String nombreInstitucion = "", nombre="", fechaInicio="", fechaFin="", idFacturacion="";
	String estado="", idEstado ="", botones = "G",botonesAbajo="V", fechaEstado="", destino="mainWorkArea";
	String idInstitucion="", modo="";

	//string que dirá cual es el modo en el que se envie el form
	String accion="Insertar";
	
	//para los tipos
	String clase = "box", desactivado="false";
	boolean consulta = false;
	boolean readonly = false;
	
	//para seleccionar los combos
	Vector vHito = new Vector();
	Vector gruposF = new Vector();
	String[] dato1 = {usr.getLocation()};
	ArrayList vPartidaP = new ArrayList();
	try
	{
		modo = (String)request.getAttribute("modo");
		idInstitucion = (String)request.getAttribute("idInstitucion");
		idFacturacion = (String)request.getAttribute("idFacturacion");
		nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
		if (!modo.equalsIgnoreCase("nuevo"))
		{
			accion="Modificar";
			destino = "submitArea2";
			if (facturaBean!=null){
				nombre = (String)facturaBean.getNombre();
				idFacturacion = ((Integer)facturaBean.getIdFacturacion()).toString();
				fechaInicio = GstDate.getFormatedDateShort("",(String)facturaBean.getFechaDesde());
				fechaFin = GstDate.getFormatedDateShort("",(String)facturaBean.getFechaHasta());
			}
		}
	}
	catch(Exception e){}
	
	//Para los modos de presentar los objetos de html
	if (modo.equalsIgnoreCase("consulta")){
		consulta = true;
		desactivado = "true";
		readonly = true;
		clase = "boxConsulta";
	}

	if (modo.equalsIgnoreCase("nuevo")){
		botonesAbajo = "V";
		botones = "G";
	} 
	else
		if (modo.equalsIgnoreCase("consulta")){
			botonesAbajo = "V";
			botones = "DE,EF";
		} 
		else {
			botonesAbajo = "V,N";
			botones = "DE,G,R,EF";
		}
%>



<!-- HEAD -->

	<siga:Titulo 
		titulo="factSJCS.previsiones.cabecera"
		localizacion="factSJCS.previsiones.ruta"/>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="mantenimientoPrevisionesNuevaForm" staticJavascript="false" />

	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer(){		
			document.all.mantenimientoPrevisionesForm.reset();
		}			
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardar() 
		{
			sub();
			if (validateMantenimientoPrevisionesNuevaForm(document.mantenimientoPrevisionesForm))
			{
					/*if (compararFecha(document.forms[0].fechaHasta, document.forms[0].fechaHoy) ==1) {
						alert('<siga:Idioma key="factSJCS.datosFacturacion.fechas.errorPosteriorActual"/>');
						return;
					}*/
			
					if (compararFecha(document.forms[0].fechaDesde,document.forms[0].fechaHasta)==1) {
						mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
						alert(mensaje);
						fin();
						return false;
					} else {
						document.forms[0].action = "<%=app %>/FCS_MantenimientoPrevisiones.do?noReset=true";
						document.forms[0].modo.value = "<%=accion%>";
						document.forms[0].target = "<%=destino%>";
						document.forms[0].submit();
					}
			}else{
				fin();
				return false;
			}
		}		
		
		function accionNuevo()
		{
			document.forms[0].action = "<%=app %>/FCS_DatosGeneralesFacturacion.do?prevision=S";
			document.forms[0].modo.value = "nuevoCriterio";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscarCriterios();
		}
		
		function buscarCriterios()
		{	
			document.forms[0].action = "<%=app %>/FCS_DatosGeneralesFacturacion.do";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].target = "resultado";
			document.forms[0].submit();
		}
		
		function refrescarLocal(){
			document.forms[0].action = "<%=app %>/FCS_MantenimientoPrevisiones.do?noReset=true";
			document.forms[0].modo.value = "editar";
			document.forms[0].target = "_self"; //submitArea2";
			document.forms[0].submit();
		}

		function accionEjecutaFacturacion()
		{
			document.forms[0].action = "<%=app %>/FCS_MantenimientoPrevisiones.do?noReset=true";
			document.forms[0].action = "<%=app %>/FCS_MantenimientoPrevisiones.do?noReset=true";
			document.forms[0].modo.value="ejecutar";
			document.forms[0].target = "submitArea2";
			var f = document.forms[0].name;
				 
			// con pantalla de espera
			 window.frames.submitArea2.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=messages.factSJCS.procesandoPrevision';
		}

		function accionDescargas()
		{
			// primero recojo si en un fichero o varios
			document.forms[0].action = "<%=app %>/FCS_MantenimientoPrevisiones.do?noReset=true";
			document.forms[0].modo.value="descargas";
			var pepe = ventaModalGeneral(document.forms[0].name,"P");
		}
		
		</script>	
</head>

<body onLoad="ajusteAltoBotones('resultado');">



	
		<html:form action="/FCS_MantenimientoPrevisiones.do?noReset=true" method="POST" target="submitArea2">
		<html:hidden property="modo" value=""/>
		<html:hidden property ="actionModal" value = ""/>
		<html:hidden property ="idFacturacion" value = "<%=idFacturacion%>"/>
		<html:hidden property ="idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property ="fechaHoy"			 value = '<%=UtilidadesBDAdm.getFechaBD("")%>'/>

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos" height="22">
				<siga:Idioma key="factSJCS.previsiones.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>
			
					<siga:ConjCampos leyenda="factSJCS.previsiones.cabecera">
				
					<table class="tablaCampos" align="center">	
						<tr>		
							<td class="labelText" ><siga:Idioma key="factSJCS.datosFacturacion.literal.nombre"/>&nbsp(*)</td>
							<td >
								<html:text name="mantenimientoPrevisionesForm" property="nombre" value='<%=nombre%>' size="60" maxlength="100" styleClass="<%=clase%>" readOnly="<%=readonly%>"></html:text>
								<html:hidden name="mantenimientoPrevisionesForm" property="nombreB"></html:hidden>
							</td>
							<td class="labelText" ><siga:Idioma key="factSJCS.datosFacturacion.literal.fechaInicio"/>&nbsp(*)</td>
							<td>
								<siga:Fecha nombreCampo="fechaDesde" valorInicial="<%=fechaInicio%>" disabled="<%=String.valueOf(consulta)%>"></siga:Fecha>								
							</td>
							<td class="labelText" ><siga:Idioma key="factSJCS.datosFacturacion.literal.fechaFin"/>&nbsp(*)</td>	
							<td >								
								<siga:Fecha nombreCampo="fechaHasta" valorInicial="<%=fechaFin%>" disabled="<%=String.valueOf(consulta)%>"></siga:Fecha>
							</td>	
						</tr>
					</table>
					</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion clase="botonesSeguido" botones='<%=botones%>' modo='<%=modo%>'/>


	<siga:ConjBotonesAccion botones='<%=botonesAbajo%>' modo='<%=modo%>'/>


<!-- FIN ******* CAPA DE PRESENTACION ****** -->

<iframe align="center" src="<%=app%>/html/jsp/facturacionSJCS/consultaCriteriosFacturacion.jsp?idInstitucion=<%=idInstitucion%>&idFacturacion=<%=idFacturacion%>&modo=<%=modo%>&prevision=S"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
</iframe>

<!-- PARA LA FUNCION VOLVER -->
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea2" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
