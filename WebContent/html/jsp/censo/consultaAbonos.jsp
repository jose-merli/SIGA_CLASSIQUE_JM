<!-- consultaAbonos.jsp -->
<!-- 
	 Muestra los abonos asociados a un determinado cliente
	 VERSIONES:
	 miguel.villegas 21-03-2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacAbonoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>
<%@page import="com.atos.utils.Row"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String botonesAccion="N";
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

	// Datos del cliente a visualizar
	Long idPersona=(Long)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	String accion=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	String idInstitucion=(String)request.getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion
	String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();
	
	
	Long SidPersona = (Long) request.getAttribute("idPersona");
	if (SidPersona == null){
		SidPersona = (Long) request.getSession().getAttribute("idPersona");
		if (SidPersona == null){
			SidPersona = new Long (request.getParameter("idPersona"));
		}
	}
	Integer SidInstitucion = (Integer) request.getAttribute("idInstitucion");
	if(SidInstitucion == null){
		SidInstitucion = (Integer) request.getSession().getAttribute("idInstitucion");
		if(SidInstitucion == null){
			SidInstitucion = new Integer(request.getParameter("idInstitucion"));	
		
		}
	
	}
		
	
	Vector abonos=new Vector();

	// Institucion del usuario de la aplicacion
	String idInstUsuario=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion

	// Obtencion de la informacon relacionada con el abono
	if (request.getAttribute("container") != null){	
		abonos = (Vector)request.getAttribute("container");	
	}

	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botonesAccion="V";
	}
	 String sTipo = request.getParameter("tipoCliente");		
	 
	 
	 
	/** PAGINADOR ***/
	String idioma = usr.getLanguage().toUpperCase();
	
	Vector resultado = null;
	String paginaSeleccionada = "";

	String totalRegistros = "";

	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	String atributoPaginador = (String) request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador) != null) {
		hm = (HashMap) ses.getAttribute(atributoPaginador);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");
			PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";
			totalRegistros = "0";
			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}
	/** FIN PAGINADOR ***/	 
	
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica")); 
	String action=app+"/CEN_AbonosCliente.do?noReset=true&bIncluirRegistrosConBajaLogica="+(String)request.getAttribute("bIncluirRegistrosConBajaLogica") +
		 "&nombre="+nombre+"&idInstitucion="+idInstitucion+"&idPersona="+idPersona.toString()+"&numero="+numero+
		 "&idInstitucion="+idInstitucion+"&idInstUsuario="+idInstUsuario+"&accion="+accion;
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="AbonosClienteForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<script>
			function incluirRegBajaLogica(o) {
			if (o.checked) {
			document.AbonosClienteForm.incluirRegistrosConBajaLogica.value = "s";
			} else {
				document.AbonosClienteForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.AbonosClienteForm.modo.value = "abrir";
			
			document.AbonosClienteForm.submit();
		}		
		</script>
	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
			
		<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.facturacion.abonos.cabecera"
			localizacion="censo.fichaLetrado.facturacion.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.facturacion.abonos.cabecera" 
			localizacion="censo.fichaCliente.facturacion.abonos.localizacion"/>
		<%}%>
		<!-- FIN: TITULO Y LOCALIZACION -->
			
	</head>

	<body class="tablaCentralCampos">

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->

	    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="facturacion.abonosClientes.literal.cabecera"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
		    <%if(!numero.equalsIgnoreCase("")){%>
					<siga:Idioma key="facturacion.abonosClientes.literal.nColegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
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

	<table cellspacing="0" cellpadding="0" width="100%">

			<html:form action="/CEN_AbonosCliente.do" method="POST" target="_self">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idPersona" value="<%=idPersona.toString()%>"/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="accion" value="<%=accion%>"/>
				
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
		</html:form>
				<tr>				
						<siga:TablaCabecerasFijas 
						   nombre="tablaDatos"
						   borde="1"
						   clase="tableTitle"
						   nombreCol="facturacion.busquedaAbonos.literal.fecha,facturacion.busquedaAbonos.literal.numeroAbono,
						   			  facturacion.altaAbonos.literal.motivos,facturacion.datosGeneralesAbonos.literal.importeNeto,
						   			  facturacion.datosGeneralesAbonos.literal.importeIva,facturacion.abonosPagos.literal.importeTotalAbono,
						   			  facturacion.abonosPagos.literal.totalAbonado,"
						   tamanoCol="10,10,30,10,10,10,10,10"
						   alto="350"
						   ajuste="50"
						   ajusteBotonera="true">
						<%
				    	if (resultado == null || resultado.size() < 1 )
					    {
						%>
						 		<br>
						   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
						 		<br>						<%
				    	}	    
					    else
					    { %>
				    		<%Enumeration en = resultado.elements();
							int recordNumber=1;
							while (en.hasMoreElements())
							{
				            	Row row = (Row) en.nextElement();%>
						        
								<siga:FilaConIconos
									  fila='<%=String.valueOf(recordNumber)%>'
									  botones="C"
									  visibleEdicion="no"
									  visibleBorrado="no"
									  modo='<%=accion%>'
									  clase="listaNonEdit"
									  >
									  
									<td>
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(FacAbonoBean.C_IDABONO)%>">
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=idInstitucion%>">
										<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(FacAbonoBean.C_FECHA)))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_NUMEROABONO))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_MOTIVOS))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea (new Double(row.getString("TOTALNETO")).doubleValue(), 2)).toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea (new Double(row.getString("TOTALIVA")).doubleValue(), 2)).toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea (new Double(row.getString("TOTAL")).doubleValue(), 2)).toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea (new Double(row.getString("TOTALABONADO")).doubleValue(), 2)).toString())%>&nbsp;&euro;
									</td>
								</siga:FilaConIconos>
								<% recordNumber++;%>
							<%	} %>
						<%	} %>
						</siga:TablaCabecerasFijas>

		<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
							registrosPorPagina="<%=registrosPorPagina%>" 
							paginaSeleccionada="<%=paginaSeleccionada%>" 
							idioma="<%=idioma%>"
							modo="abrirAbonosPaginados"								
							clase="paginator" 
							divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
							distanciaPaginas=""
							action="<%=action%>" />
		 <%}%>
								
				</tr>
				
			<div style="position:absolute; left:200px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
						
						<% if (bIncluirBajaLogica) { %>
							<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);" checked>
						<% } else { %>
							<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);">
						<% } %>
					</td>
				</tr>
			</table>
		</div>


								
			<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
			<siga:ConjBotonesAccion botones="<%=botonesAccion%>" clase="botonesDetalle"/>
		<!-- FIN: BOTONES REGISTRO -->


		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript"></script>
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

			</table>		
	</body>
</html>
