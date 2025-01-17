<!DOCTYPE html>
<html>
<head>
<!-- consultaDatosBancarios.jsp -->

<!-- Historico modificaciones:
		miguel.villegas: implementacion boton volver	1-2-2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.siga.beans.CenComponentesBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();

	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	String idUsr = Long.toString(usr.getIdPersona());
	String institucion = String.valueOf((Integer) request.getAttribute("idInstitucion"));
	String nombre = (String) request.getAttribute("nombrePersona");
	String numero = (String) request.getAttribute("numero");
	String idPersona = String.valueOf((Long) request.getAttribute("idPersona"));
	Vector<Hashtable<String, Object>> vDatos = (Vector<Hashtable<String, Object>>) request.getAttribute("vDatos");
	String estadoColegial = (String) request.getAttribute("estadoColegial");
	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;	
	String accion = (String) request.getAttribute("accion");
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String) request.getAttribute("bIncluirRegistrosConBajaLogica"));
	String sTipo = request.getParameter("tipo");

	// Gestion de Volver
	String botones = "N";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null || usr.isLetrado()) {
		busquedaVolver = "volverNo";
	} else {
		botones = "V,N";
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">	
		function solicitar(fila) {
			preparaDatos(fila, 'tablaDatos');
			document.cuentasBancariasForm.modo.value = "solicitarModificacion";
	    	ventaModalGeneral(document.cuentasBancariasForm.name, "M"); 		
		}
	
		function accionNuevo() {		
			document.cuentasBancariasForm.modo.value = "nuevo";
			document.cuentasBancariasForm.submit();
		}
	
		function refrescarLocal() {
	    	document.cuentasBancariasForm.modo.value = "abrir";
			document.cuentasBancariasForm.submit();
		}
			
		function incluirRegBajaLogica(o) {
			if (o.checked) {
				document.cuentasBancariasForm.incluirRegistrosConBajaLogica.value = "s";
			} else {
				document.cuentasBancariasForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.cuentasBancariasForm.modo.value = "abrir";
			document.cuentasBancariasForm.submit();
		}		
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->

	<%
		if (sTipo != null && sTipo.equals("LETRADO")) {
	%>
		<siga:Titulo titulo="censo.fichaCliente.bancos.cabecera" localizacion="censo.fichaLetrado.localizacion" />
	<%
		} else {
	%>
		<siga:TituloExt titulo="censo.fichaCliente.bancos.cabecera" localizacion="censo.fichaCliente.bancos.localizacion" />
	<%
		}
	%>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	<table class="tablaTitulo" align="center" cellspacing=0>
		<!-- Formulario de la lista de detalle multiregistro -->
		<form id="cuentasBancariasForm" name="cuentasBancariasForm" method="post" action="<%=app%>/CEN_CuentasBancarias.do">
			<!-- Campo obligatorio -->
			<html:hidden property="modo" styleId="modo"  value="" />
			<input type="hidden" name="nombreUsuario" id="nombreUsuario" value="<%=nombre%>" /> 
			<input type="hidden" name="numeroUsuario" id="numeroUsuario" value="<%=numero%>" /> 
			<input type='hidden' name="idPersona" id="idPersona" value="<%=idPersona%>" /> 
			<input type='hidden' name="idInstitucion" id="idInstitucion" value="<%=institucion%>" /> 
			<input type='hidden' name="accion"  id="accion" value="<%=String.valueOf(request.getAttribute("accion"))%>" />
			<input type="hidden" name="incluirRegistrosConBajaLogica" id="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>" />
			<input type="hidden" id="actionModal" name="actionModal" value="">
		</form>
		
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1" /> 
				&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>
				&nbsp;&nbsp; 
<% 
				if (!numero.equalsIgnoreCase("")) {
 					if (estadoColegial != null && !estadoColegial.equals("")) {
%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado" /> 
						<%=UtilidadesString.mostrarDatoJSP(numero)%>
						&nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>) 
<%
					} else {
%>
						(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial" />)
<%
					}
 				} else {
%> 
					<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" />
<%
				}
%>
			</td>
		</tr>
	</table>

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

	<siga:Table 
		name="tablaDatos" 
		border="1"
		columnNames="censo.consultaDatosBancarios.literal.titular,
			censo.consultaDatosBancarios.literal.tipoCuenta,
			censo.datosCuentaBancaria.literal.abonoSJCS,
			censo.consultaDatosBancarios.literal.sociedad,
			censo.consultaDatosBancarios.literal.cuenta,
			censo.consultaDatosBancarios.literal.fechaBaja,"
		columnSizes="28,9,7,7,25,8,12">

<%
		if (vDatos == null || vDatos.size() < 1) {
%>
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else {
			Enumeration en = vDatos.elements();
			int i = 0;

			while (en.hasMoreElements()) {
				Hashtable<String, Object> htData = (Hashtable<String, Object>) en.nextElement();
				if (htData == null)
					continue;
				String accionBanco = "";
				i++;
				FilaExtElement[] elems = new FilaExtElement[1];
				if (((String) htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona)) {
					accionBanco = accion;
				} else {
					accionBanco = "ver";
				}

				if ((idPersona.equals(idUsr)) && (((String) htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona)) && usr.isLetrado()) {
					elems[0] = new FilaExtElement("solicitar", "solicitar", SIGAConstants.ACCESS_READ);
				}

				String sociedad = "";
				String sociedadLiteral = "";
				if (((String) htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona)) {
					sociedad = DB_FALSE;
					sociedadLiteral = UtilidadesString.getMensajeIdioma(usr, "general.no");
				} else {
					sociedad = DB_TRUE;
					sociedadLiteral = UtilidadesString.getMensajeIdioma(usr, "general.yes");
				}

				String tipoCuenta = "";
				if (((String) htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_ABONO)) {
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr, "censo.tipoCuenta.abono");
				} else if (((String) htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_ABONO_CARGO)) {
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr, "censo.tipoCuenta.abonoCargo");
				} else if (((String) htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_CARGO)) {
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr, "censo.tipoCuenta.cargo");
				}

				String abonosjcs = "";
				if (((String) htData.get(CenCuentasBancariasBean.C_ABONOSJCS)).equals(DB_FALSE)) {
					abonosjcs = UtilidadesString.getMensajeIdioma(usr, "general.no");
				} else {
					abonosjcs = UtilidadesString.getMensajeIdioma(usr, "general.yes");
				}

				String numeroCuentaCompleto = (String) htData.get(CenCuentasBancariasBean.C_IBAN);
				String fechaBaja = (String) htData.get(CenCuentasBancariasBean.C_FECHABAJA);

				if (fechaBaja == null || "".equals(fechaBaja)) {
					fechaBaja = "";
				} else {
					fechaBaja = UtilidadesString.formatoFecha(fechaBaja, "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy");
				}

				String iconos = "C";
				String f = (String) htData.get(CenCuentasBancariasBean.C_FECHABAJA);
				if ((f == null) || (f.equals(""))) {
					iconos += ",E,B";
				}						
%>
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' elementos='<%=elems%>' modo="<%=accionBanco%>" clase="listaNonEdit">
					<td>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenCuentasBancariasBean.C_IDCUENTA)%>'>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(CenCuentasBancariasBean.C_IDPERSONA)%>'>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_3' name='oculto<%=String.valueOf(i)%>_3' value='<%=htData.get(CenCuentasBancariasBean.C_IDINSTITUCION)%>'>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_4' name='oculto<%=String.valueOf(i)%>_4' value='<%=sociedad%>'> 
						<%=UtilidadesString.mostrarDatoJSP(htData.get(CenCuentasBancariasBean.C_TITULAR))%>
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(tipoCuenta)%></td>
					<td align="center"><%=UtilidadesString.mostrarDatoJSP(abonosjcs)%></td>
					<td align="center"><%=UtilidadesString.mostrarDatoJSP(sociedadLiteral)%></td>
					<td><%=UtilidadesString.mostrarIBANConAsteriscos(numeroCuentaCompleto)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fechaBaja)%></td>
				</siga:FilaConIconos>
<%
			}
		} // While
%>
	</siga:Table>

<%if (!usr.isLetrado()) {%>
		<div style="position: absolute; left: 400px; width:200px; bottom: 0px; z-index: 99;">
			<table align="center" border="0" class="botonesSeguido">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal" /> 
						<%if (bIncluirBajaLogica) {%>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked> 
						<%} else {%>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);"> 
						<%}%>
 					</td>
				</tr>
			</table>
		</div>
<%}%>

	<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=accion%>" clase="botonesDetalle" />

	<%@ include file="/html/jsp/censo/includeVolver.jspf"%>
	<!-- FIN: BOTONES BUSQUEDA -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>