<!-- consultaAbonos.jsp -->
<!-- 
	 Muestra los abonos asociados a un determinado cliente
	 VERSIONES:
	 miguel.villegas 21-03-2005
-->

<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesHash"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

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
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<!-- JSP -->

<html>

<!-- HEAD -->




<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	String botonesAccion = "N";
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");

	// Datos del cliente a visualizar
	Long idPersona = (Long) request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	String accion = (String) request.getAttribute("ACCION"); // Obtengo la accion anterior
	String nombre = (String) request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero = (String) request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	String idInstitucion = (String) request
			.getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion
	String idInstitucionPersona = Integer.valueOf(
			request.getParameter("idInstitucion")).toString();

	int destinatarioAbono = ((Integer)request.getAttribute("destinatarioAbono")).intValue();
	Long SidPersona = (Long) request.getAttribute("idPersona");
	if (SidPersona == null) {
		SidPersona = (Long) request.getSession().getAttribute(
				"idPersona");
		if (SidPersona == null) {
			SidPersona = new Long(request.getParameter("idPersona"));
		}
	}
	Integer SidInstitucion = (Integer) request
			.getAttribute("idInstitucion");
	if (SidInstitucion == null) {
		SidInstitucion = (Integer) request.getSession().getAttribute(
				"idInstitucion");
		if (SidInstitucion == null) {
			SidInstitucion = new Integer(
					request.getParameter("idInstitucion"));

		}

	}

	Vector abonos = new Vector();

	// Institucion del usuario de la aplicacion
	String idInstUsuario = (String) request
			.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion

	// Obtencion de la informacon relacionada con el abono
	if (request.getAttribute("container") != null) {
		abonos = (Vector) request.getAttribute("container");
	}

	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute(
			"CenBusquedaClientesTipo");
	if ((busquedaVolver == null) || (usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}

	if (!busquedaVolver.equals("volverNo")) {
		botonesAccion = "V,i";
	}
	String sTipo = request.getParameter("tipoCliente");

	/** PAGINADOR ***/
	String idioma = usr.getLanguage().toUpperCase();

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
	/** FIN PAGINADOR ***/
	String informeUnico =(String) request.getAttribute("informeUnico");
	
	boolean bIncluirBajaLogica = UtilidadesString
			.stringToBoolean((String) request
					.getAttribute("bIncluirRegistrosConBajaLogica"));
	String action = app
			+ path
			+ ".do?noReset=true&bIncluirRegistrosConBajaLogica="
			+ (String) request
					.getAttribute("bIncluirRegistrosConBajaLogica")
			+ "&nombre=" + nombre + "&idInstitucion=" + idInstitucion
			+ "&idPersona=" + idPersona.toString() + "&numero="
			+ numero + "&idInstitucion=" + idInstitucion
			+ "&idInstUsuario=" + idInstUsuario + "&accion=" + accion;
%>
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp" />
<link rel="stylesheet"
	href="<%=app%>/html/js/themes/base/jquery.ui.all.css" />


<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
	
		<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>	
	
	
	
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="AbonosClienteForm" staticJavascript="false" />  

<%
								if (sTipo != null && sTipo.equals("LETRADO")) {
							%>
<siga:Titulo titulo="censo.fichaCliente.facturacion.abonos.cabecera"
	localizacion="censo.fichaLetrado.facturacion.localizacion" />
<%
			} else if (path.equals("/JGR_AbonosClienteSJCS")) {
		%>
<siga:Titulo titulo="pestana.fichaCliente.justiciagratuita.pagos"
	localizacion="censo.fichaCliente.sjcs.to.facturacion.localizacion" />
<%
			} else {
		%>
<siga:TituloExt titulo="censo.fichaCliente.facturacion.abonos.cabecera"
	localizacion="censo.fichaCliente.facturacion.abonos.localizacion" />
<%
			}
		%>
<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">
	<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="facturacion.abonosClientes.literal.cabecera"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
		    <%
		    	if (!numero.equalsIgnoreCase("")) {
		    %>
					<siga:Idioma key="facturacion.abonosClientes.literal.nColegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
			<%
				} else {
			%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<%
				}
			%>
		</td>
		</tr>
		</table>
		
		<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
		
		
		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->

	
	<table cellspacing="0" cellpadding="0" width="100%">

			<html:form action="${path}" method="POST" target="_self">
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
						   			  facturacion.datosGenerales.literal.observaciones,facturacion.datosGeneralesAbonos.literal.importeNeto,
						   			  facturacion.datosGeneralesAbonos.literal.importeIva,facturacion.abonosPagos.literal.importeTotalAbono,
						   			  facturacion.abonosPagos.literal.totalAbonado,Destino,"
						   tamanoCol="8,10,30,9,9,9,9,9,7"
						   alto="350"
						   ajuste="50"
						   ajusteBotonera="true">
						<%
							if (resultado == null || resultado.size() < 1) {
						%>
						 		<br>
						   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
						 		<br>						<%
							} else {
						%>
				    		<%
				    			Enumeration en = resultado.elements();
				    					int recordNumber = 1;
				    					while (en.hasMoreElements()) {

				    						Row row = (Row) en.nextElement();
				    						String s = row.getString(FacAbonoBean.C_IDPAGOSJG);
				    						String abono = row.getString(FacAbonoBean.C_IDABONO);
				    						FilaExtElement[] elemento = null;
				    						if ((s != null && !s.trim().equals("") )||( abono != null && !abono.trim().equals(""))) {
				    							elemento = new FilaExtElement[1];
				    							elemento[0] = new FilaExtElement("download",
				    									"download", SIGAConstants.ACCESS_READ);
				    						}
				    		%>

						        
								<siga:FilaConIconos
									  fila='<%=String.valueOf(recordNumber)%>'
									  botones="C" 
									  visibleEdicion="no"
									  visibleBorrado="no"
									  pintarEspacio="no"
									  elementos='<%=elemento%>'
									  modo='<%=accion%>'
									  clase="listaNonEdit"
									  >
									  
									  
									 <input type="hidden" name="idPersona<%="" + String.valueOf(recordNumber)%>"	
									 		value="<%=row.getString(FacAbonoBean.C_IDPERSONA)%>">
									 <input type="hidden" name="idPersonaOrigen<%="" + String.valueOf(recordNumber)%>"	
									 		value="<%=row.getString("IDPERORIGEN")%>">
			
									<input type="hidden" name="idPago<%="" + String.valueOf(recordNumber)%>"
										value="<%=row.getString(FacAbonoBean.C_IDPAGOSJG)%>"> 
									<td>
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(FacAbonoBean.C_IDABONO)%>">
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=idInstitucion%>">
										<%=UtilidadesString.mostrarDatoJSP(GstDate
									.getFormatedDateShort("",
											row.getString(FacAbonoBean.C_FECHA)))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(row
									.getString(FacAbonoBean.C_NUMEROABONO))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(row
									.getString(FacAbonoBean.C_OBSERVACIONES))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero
									.formatoCampo(
											UtilidadesNumero.redondea(
													new Double(
															row.getString("TOTALNETO"))
															.doubleValue(), 2))
									.toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero
									.formatoCampo(
											UtilidadesNumero.redondea(
													new Double(
															row.getString("TOTALIVA"))
															.doubleValue(), 2))
									.toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero
									.formatoCampo(
											UtilidadesNumero.redondea(
													new Double(row
															.getString("TOTAL"))
															.doubleValue(), 2))
									.toString())%>&nbsp;&euro;
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero
									.formatoCampo(
											UtilidadesNumero.redondea(
													new Double(
															row.getString("TOTALABONADO"))
															.doubleValue(), 2))
									.toString())%>&nbsp;&euro;
									</td>
									<td>
										<%
											if (UtilidadesHash.getString(row.getRow(),"IDPERORIGEN").equals(UtilidadesHash.getString(row.getRow(),"IDPERDESTINO"))) {
												if(destinatarioAbono==0){%>
													<siga:Idioma key="facturacion.abonos.destino.sociedad"/>
													<%}else{%>
														<siga:Idioma key="facturacion.abonos.destino.letrado"/>		
												<%
												}										
											
										
											} else {
										if(destinatarioAbono==0){%>
													<siga:Idioma key="facturacion.abonos.destino.letrado"/>
													<%}else{%>
														<siga:Idioma key="facturacion.abonos.destino.sociedad"/>		
												<%
												}	
											}
										%>
									</td>
								</siga:FilaConIconos>
								<%
									recordNumber++;
								%>
							<%
								}
							%>
						<%
							}
						%>
						</siga:TablaCabecerasFijas>

		<%
			if (hm.get("datos") != null && !hm.get("datos").equals("")) {
		%>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
							registrosPorPagina="<%=registrosPorPagina%>" 
							paginaSeleccionada="<%=paginaSeleccionada%>" 
							idioma="<%=idioma%>"
							modo="abrirAbonosPaginados"								
							clase="paginator" 
							divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
							distanciaPaginas=""
							action="<%=action%>" />
		 <%
		 	}
		 %>
								
				</tr>
				
			<div style="position:absolute; left:200px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
						
						<%
													if (bIncluirBajaLogica) {
												%>
							<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);" checked>
						<%
							} else {
						%>
							<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);">
						<%
							}
						%>
					</td>
				</tr>
			</table>
		</div>

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>


		<%@ include file="/html/jsp/censo/includeVolver.jspf"%>

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
		<siga:ConjBotonesAccion botones="<%=botonesAccion%>"
			clase="botonesDetalle" />
		<!-- FIN: BOTONES REGISTRO -->


		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript"></script>

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
			style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</table>

	<script>
		function refrescarLocal(){
			parent.pulsarId("pestana.fichaCliente.datosAbonos","mainPestanas");
		}
		
		function incluirRegBajaLogica(o) {
			if (o.checked) {
			document.AbonosClienteForm.incluirRegistrosConBajaLogica.value = "s";
			} else {
				document.AbonosClienteForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.AbonosClienteForm.modo.value = "abrir";
			
			document.AbonosClienteForm.submit();
		}
		function accionImprimir() {		
	
			document.AbonosClienteForm.modo.value = 'imprimir';
			document.AbonosClienteForm.target = "submitArea";
			document.AbonosClienteForm.submit();
		}	
	 
		function creaFormInformeFacturaRectificativa() {
			
		}	
	
		function download(fila) {
			sub();
			var idPago = "idPago"+fila;
			
			if(document.getElementById(idPago).value!=''){
				var idPers = "idPersonaOrigen"+fila;
			
				var idInst = "oculto"+fila+"_2";
				idPersona = document.getElementById(idPers).value;
				idPago = document.getElementById(idPago).value;
				idInstitucion =  document.getElementById(idInst).value;
				datos = +idInstitucion +","+idPago+","+idPersona +"#";
			   	
				var formu = document.createElement('form');
				formu.setAttribute('name', 'InformesGenericosForm');
				formu.setAttribute('method', 'POST');
				formu.setAttribute('action', '/SIGA/INF_InformesGenericos.do');
				formu.setAttribute('target', 'submitArea');
				formu.setAttribute('method', 'POST');
				
				var myinput = document.createElement('input');
				myinput.setAttribute('type', 'hidden');
				myinput.setAttribute('name', 'idInstitucion');
				myinput.setAttribute('value', '<%=idInstitucion %>');
				formu.appendChild(myinput);
				
				var myinput2 = document.createElement('input');
				myinput2.setAttribute('type', 'hidden');
				myinput2.setAttribute('name', 'idTipoInforme');
				myinput2.setAttribute('value', 'CPAGO');
				formu.appendChild(myinput2);
			   	
				var myinput3 = document.createElement('input');
				myinput3.setAttribute('type', 'hidden');
				myinput3.setAttribute('name', 'datosInforme');
				myinput3.setAttribute('value', '');
				formu.appendChild(myinput3);
				
				var myinput4 = document.createElement('input');
				myinput4.setAttribute('type', 'hidden');
				myinput4.setAttribute('name', 'tablaDatosDinamicosD');
				myinput4.setAttribute('value', '');
				formu.appendChild(myinput4);
				
				var myinput5 = document.createElement('input');
				myinput5.setAttribute('type', 'hidden');
				myinput5.setAttribute('name', 'seleccionados');
				myinput5.setAttribute('value', '0');
				formu.appendChild(myinput5);
			   	
				//formu.datosInforme.value = datos;
				document.body.appendChild(formu);
			   	formu.submit();
			   	
			}else{
				
				// EJEMPLO: var dat="idAbono==25##idinstitucion==2040%%%idAbono==26##idinstitucion==2040%%%idAbono==27##idinstitucion==2040%%%idAbono==28##idinstitucion==2040";
				
				var formu = document.createElement('form');
				formu.setAttribute('name', 'InformesGenericosForm');
				formu.setAttribute('method', 'POST');
				formu.setAttribute('action', '/SIGA/INF_InformesGenericos.do');
				formu.setAttribute('target', 'submitArea');
				formu.setAttribute('method', 'POST');
				
				var myinput = document.createElement('input');
				myinput.setAttribute('type', 'hidden');
				myinput.setAttribute('name', 'idInstitucion');
				myinput.setAttribute('value', '<%=idInstitucion %>');
				formu.appendChild(myinput);
				
				var myinput2 = document.createElement('input');
				myinput2.setAttribute('type', 'hidden');
				myinput2.setAttribute('name', 'idInforme');
				myinput2.setAttribute('value', '');
				formu.appendChild(myinput2);
				
				var myinput3 = document.createElement('input');
				myinput3.setAttribute('type', 'hidden');
				myinput3.setAttribute('name', 'idTipoInforme');
				myinput3.setAttribute('value', 'ABONO');
				formu.appendChild(myinput3);
				
				var myinput4 = document.createElement('input');
				myinput4.setAttribute('type', 'hidden');
				myinput4.setAttribute('name', 'datosInforme');
				myinput4.setAttribute('value', '');
				formu.appendChild(myinput4);
				
				var myinput5 = document.createElement('input');
				myinput5.setAttribute('type', 'hidden');
				myinput5.setAttribute('name', 'seleccionados');
				myinput5.setAttribute('value', '0');
				formu.appendChild(myinput5);
				
				var idInst = "oculto" + fila + "_2";
				var idAbono = "oculto" + fila + "_1";
				datos = 'idAbono=='+ document.getElementById(idAbono).value+ "##idinstitucion=="+ document.getElementById(idInst).value + "%%%";
				
				document.body.appendChild(formu);
				formu.submit();
			}
	
	function download(fila)
	{
		var idPago = "idPago"+fila;
		
		if(document.getElementById(idPago).value!=''){
			var idPers = "idPersonaOrigen"+fila;
		
			var idInst = "oculto"+fila+"_2";
			idPersona = document.getElementById(idPers).value;
			idPago = document.getElementById(idPago).value;
			idInstitucion =  document.getElementById(idInst).value;
			
			datos = "idPersona=="+idPersona + "##idPago==" +idPago + "##idInstitucion==" +idInstitucion + "##idTipoInforme==CPAGO%%%";
			
			document.InformesGenericosForm.datosInforme.value =datos;
			document.InformesGenericosForm.idTipoInforme.value = 'CPAGO';
			
			
			
		}else{
			// EJEMPLO: var dat="idAbono==25##idinstitucion==2040%%%idAbono==26##idinstitucion==2040%%%idAbono==27##idinstitucion==2040%%%idAbono==28##idinstitucion==2040";
			
		
		

			var idInst = "oculto" + fila + "_2";
			var idAbono = "oculto" + fila + "_1";
			datos = 'idAbono=='+ document.getElementById(idAbono).value+ "##idinstitucion=="+ document.getElementById(idInst).value + "##idTipoInforme==ABONO%%%";
			
			document.InformesGenericosForm.datosInforme.value =datos;
			document.InformesGenericosForm.idTipoInforme.value = 'ABONO';
		}
		if(document.getElementById("informeUnico").value=='1'){
			document.InformesGenericosForm.submit();
		}else{
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
		}

	}
			</script>
			
	</body>
</html>
