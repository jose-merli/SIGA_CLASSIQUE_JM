<!-- anticiposCliente.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 07-02-2004 creacion
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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	// locales
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("AnticiposClienteForm");
	String modo = formulario.getAccion();
	String idPersona = formulario.getIdPersona();
	if(idPersona==null){
		idPersona=String.valueOf(usrbean.getIdPersona());
	}
	String idInstitucion = usrbean.getLocation();
	// Vector resultado = (Vector) request.getAttribute("PysAnticipoLetradoResultados");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usrbean.isLetrado())) {
	 	busquedaVolver = "volverNo";
	}

	// PARA LA CABECERA
	String numeroColegiado = "";
	boolean bColegiado = false;
	//String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
	//String cliente = UtilidadesString.getMensajeIdioma(usrbean, colegiado);
	//numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
	if (numeroColegiado!=null) {
		bColegiado=true;
	} 
	String nombreApellidos = (String) request.getAttribute("CenDatosGeneralesNombreApellidos");
	if (nombreApellidos==null) {
		nombreApellidos="";
	} 
	numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
	if (numeroColegiado==null) {
		numeroColegiado="";
	} 
	String loca = "censo.fichaCliente.localizacion";
	String titu = "censo.fichaCliente.cabecera";
	String busc = "";

	busc = UtilidadesString.getMensajeIdioma(usrbean,"cen.consultaAnticipos.titulo1");
	busc += "&nbsp;" + nombreApellidos + "&nbsp;";
	if (bColegiado) { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.colegiado") + " " + numeroColegiado;
	} else { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.NoColegiado");
	}  
  	String sTipo = request.getParameter("tipoCliente");
  	
 
    /** PAGINADOR ***/
	Vector resultado = null;
	String paginaSeleccionada = "";

	String totalRegistros = "";

	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	String atributoPaginador = (String) request
			.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador) != null) {
		hm = (HashMap) ses.getAttribute(atributoPaginador);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");

			PaginadorBind paginador = (PaginadorBind) hm
					.get("paginador");
			paginaSeleccionada = String.valueOf(paginador
					.getPaginaActual());

			totalRegistros = String.valueOf(paginador
					.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador
					.getNumeroRegistrosPorPagina());

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
	String sBajaLogica = (String)request.getAttribute("bIncluirRegistrosConBajaLogica");
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean(sBajaLogica);
	String actionPag=app+"/CEN_AnticiposCliente.do?bIncluirRegistrosConBajaLogica="+sBajaLogica.toString() +
	       "&idPersona="+idPersona+"&idInstitucion="+idInstitucion+"&accion="+modo;
	String idioma = usrbean.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	
%>

<html>
<!-- HEAD -->
<head>
	
	<script language="JavaScript">
	function abrirFacturas(fila) {
		var datos;
		datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = ""; 
		var j;
		var tabla;
		tabla = document.getElementById('tablaDatos');
		var flag = true;
		j = 1;
		while (flag) {
		  var aux = 'oculto' + fila + '_' + j;
		  var oculto = document.getElementById(aux);
		  if (oculto == null)  { flag = false; }
		  else { datos.value = datos.value + oculto.value + ','; }
		  j++;
		}
		datos.value = datos.value + "%"
								
    	document.AnticiposClienteForm.modo.value = "abrirFacturas";
		// Abro ventana modal y refresco si necesario
		var resultado = ventaModalGeneral(document.AnticiposClienteForm.name,"M");
		
		if (resultado=='MODIFICADO') {
			document.location.reload();		
		}
		
	}
	function accionNuevo() {

    	document.AnticiposClienteForm.modo.value = "nuevo";

		// Abro ventana modal y refresco si necesario
		var resultado = ventaModalGeneral(document.AnticiposClienteForm.name,"M");
		
		if (resultado=='MODIFICADO') {
			refrescarLocal();		
		}
		
	}
	
	
	function incluirRegBajaLogica(o) {
		if (o.checked) {
			document.AnticiposClienteForm.incluirRegistrosConBajaLogica.value = "s";
		} else {
			document.AnticiposClienteForm.incluirRegistrosConBajaLogica.value = "n";
		}
		document.AnticiposClienteForm.modo.value = "abrir";
		
		document.AnticiposClienteForm.submit();
	}
	
	
	
	</script>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	<script>	
		function refrescarLocal() {
			// document.location.reload();
			parent.pulsarId("pestana.fichaCliente.datosAnticipos","mainPestanas");
		}
	</script>	

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		 <% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.facturacion.anticipos.cabecera"
			localizacion="censo.fichaLetrado.facturacion.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.facturacion.anticipos.cabecera" 
			localizacion="censo.fichaCliente.facturacion.anticipos.localizacion"/>
		<%}%>
		
		<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<!-- Esto es muy util para el caso de ventanas modales, ya que no
	     vamos a disponer en estos casos de la cabecera de la pagina.
	     Nos serviria para tener conciencia de donde estamos ya que desde una
	     ventana modal no se puede actualizar la barra de titulo y ademas queda detras -->

		<table class="tablaTitulo" align="center" cellspacing="0">

			<!-- Formulario de la lista de detalle multiregistro -->
			<html:form action="/CEN_AnticiposCliente.do" method="POST" target="_self">
			<!-- Campo obligatorio -->
			<html:hidden styleId="modo" property = "modo" value = "" />
			<html:hidden styleId="accion" property = "accion" value = "<%=modo%>" />
			<html:hidden styleId="idPersona" name="AnticiposClienteForm" property = "idPersona" value = "<%=idPersona %>" />
			<html:hidden styleId="idInstitucion" name="AnticiposClienteForm" property = "idInstitucion" value = "<%=idInstitucion %>" />

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
			<input type="hidden" id="incluirRegistrosConBajaLogica" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
			</html:form>


		<tr>
		<td class="titulitosDatos">
			<%=busc%>
		</td>
		</tr>
		</table>

	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->



		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

	<!-- NO HAY FORMULARIO -->

<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="10,45,15,15,15";
		nombresCol="cen.consultaAnticipos.literal.fecha,cen.consultaAnticipos.literal.descripcion,cen.consultaAnticipos.literal.importe,cen.consultaAnticipos.literal.importeRestante,";
%>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol %>"
		   columnSizes="<%=tamanosCol %>"
		   modal="M">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
		</tr>
<%	
	} else { 
		
		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			 Row fila = (Row) resultado.get(i);
             Hashtable anticipo = fila.getRow();
			 if (anticipo != null) { 
				 
			FilaExtElement[] elems=new FilaExtElement[1];
			//PysAnticipoLetradoBean registro = (PysAnticipoLetradoBean) resultado.get(i);
			String cont = new Integer(i+1).toString();

			// calculo de campos
			// String fecha = registro.getFecha();
			String fecha = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_FECHA);
			if (idInstitucion==null){
				idInstitucion = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDINSTITUCION);
			}
			idPersona = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDPERSONA);
			
			if (fecha==null || fecha.equals("")) {
				fecha = "&nbsp;";
			} else {
				// formateo
				fecha = GstDate.getFormatedDateShort(usrbean.getLanguage(),fecha);
			}
			double precioDouble;
			double gastadoDouble;
			double restanteDouble;
			String idAnticipo; // = registro.getIdAnticipo().toString();
			idAnticipo= UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDANTICIPO);
			
			// precioDouble = UtilidadesNumero.redondea(registro.getImporteInicial().doubleValue(),2);
			precioDouble = UtilidadesHash.getDouble(anticipo, PysAnticipoLetradoBean.C_IMPORTEINICIAL).doubleValue();
			String precioInicial = UtilidadesNumero.formatoCampo(precioDouble);
			gastadoDouble = PysLineaAnticipoAdm.getGastadoLineasAnticipo(idInstitucion,idPersona,idAnticipo);
			restanteDouble = UtilidadesNumero.redondea(precioDouble - gastadoDouble ,2);
			String precioRestante = UtilidadesNumero.formatoCampo(restanteDouble);
			elems[0]=new FilaExtElement("abrirFacturas","abrirFacturas",SIGAConstants.ACCESS_READ);
			
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
				 
				 
			
			<%
				String botones = "";			
				
				
				boolean lineas = (gastadoDouble>0)?true:false;
				
				if(lineas) {
					botones = "C,E";	
				} else{
					botones= "C,E,B";
				}
				
			%>	 
			
			<siga:FilaConIconos fila="<%=cont %>" botones="<%=botones%>" modo="<%=modo %>" elementos="<%=elems%>" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=idInstitucion  %>"/>
					<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDPERSONA)  %>"/>
					<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDANTICIPO)  %>"/>

					<%=fecha %>
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_DESCRIPCION)) %>
				</td>
				<td align="right">
					<%=precioInicial %>
				</td>
				<td align="right">
					<%=precioRestante %>
				</td>

			</siga:FilaConIconos>		
<%			} // del if interno %>		

			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>
		
		
		<!-- Pie del paginador -->
		<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){ %>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="abrirOtra"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=actionPag%>" />

	 	<%}%>
	 	
	 	
								
		<!-- FIN: LISTA DE VALORES -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->


		<div style="position:absolute; left:300px;bottom:5px;z-index:99;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
						
						<% if (bIncluirBajaLogica) { %>
							<input type="checkbox" id="bajaLogica" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
						<% } else { %>
							<input type="checkbox" id="bajaLogica"  name="bajaLogica" onclick="incluirRegBajaLogica(this);">
						<% } %>
					</td>
				</tr>
			</table>
		</div>




<% 
	String bot ="";
	if (!busquedaVolver.equals("volverNo")) { 
		if (!usrbean.isLetrado()) { 
%>
		<siga:ConjBotonesAccion botones="V,N"  clase="botonesDetalle" modo="<%=modo%>"/>
<%
		} else{
%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<%
		}
	} else {
		if (!usrbean.isLetrado()) { 
%>
		<siga:ConjBotonesAccion botones="N"  clase="botonesDetalle" modo="<%=modo%>"/>
<%
		} else{
%>
		<siga:ConjBotonesAccion botones="botonesDetalle" />
<%
		}
	}
%>


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>


		<!-- INICIO: SUBMIT AREA -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>