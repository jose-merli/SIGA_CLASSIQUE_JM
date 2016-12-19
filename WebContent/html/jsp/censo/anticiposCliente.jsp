<!DOCTYPE html>
<html>
<head>
<!-- anticiposCliente.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import="com.siga.beans.PysLineaAnticipoAdm"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>

<!-- JSP -->
<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usrbean = (UsrBean) ses.getAttribute(ClsConstants.USERBEAN);
 
	// locales
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("AnticiposClienteForm");
	String modo = formulario.getAccion();
	String idPersona = formulario.getIdPersona();
	if(idPersona==null){
		idPersona=String.valueOf(usrbean.getIdPersona());
	}
	String idInstitucion = usrbean.getLocation();

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usrbean.isLetrado())) {
	 	busquedaVolver = "volverNo";
	}

	String nombreApellidos = (String) request.getAttribute("CenDatosGeneralesNombreApellidos");
	if (nombreApellidos==null) {
		nombreApellidos="";
	} 
	
	String numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
	boolean bColegiado = false;
	if (numeroColegiado!=null) {
		bColegiado=true;
	} else  {
		numeroColegiado="";
	}
	
	String busc = UtilidadesString.getMensajeIdioma(usrbean,"cen.consultaAnticipos.titulo1") + "&nbsp;" + nombreApellidos + "&nbsp;";
	if (bColegiado) { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.colegiado") + " " + numeroColegiado;
	} else { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.NoColegiado");
	}  
  	String sTipo = request.getParameter("tipoCliente");
  	
    /** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada = "0";
	String totalRegistros = "0";
	String registrosPorPagina = "0";
	HashMap hm = new HashMap();
	String atributoPaginador = (String) request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador)!=null) {
		hm = (HashMap) ses.getAttribute(atributoPaginador);
		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");
			PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
		}
	}
	String sBajaLogica = (String)request.getAttribute("bIncluirRegistrosConBajaLogica");
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean(sBajaLogica);
	String actionPag = app + "/CEN_AnticiposCliente.do?bIncluirRegistrosConBajaLogica=" + sBajaLogica.toString() + "&idPersona=" + idPersona + "&idInstitucion=" + idInstitucion + "&accion=" + modo;
	String idioma = usrbean.getLanguage().toUpperCase();
	/** PAGINADOR ***/
%>

	<!-- HEAD -->
	<script language="JavaScript">
		function abrirFacturas(fila) {
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var flag = true;
			var j = 1;
			while (flag) {
				var aux = 'oculto' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
			  	if (oculto == null)  { 
					flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
			  	j++;
			}
			datos.value = datos.value + "%";
									
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
		
		function refrescarLocal() {
			// document.location.reload();
			parent.pulsarId("pestana.fichaCliente.datosAnticipos","mainPestanas");
		}
		
		function darBaja(fila, id) {			
			if(confirm('<siga:Idioma key="censo.fichaCliente.facturacion.anticipos.literal.darBaja"/>')) { 
				sub();				
				if (typeof id == 'undefined')
					id='tabladatos';
				preparaDatos(fila,id);
				
				var auxTarget = document.AnticiposClienteForm.target;
				document.AnticiposClienteForm.target = "submitArea";
				document.AnticiposClienteForm.modo.value = "darbaja";
				document.AnticiposClienteForm.submit();
				document.AnticiposClienteForm.target = auxTarget;
				fin();
			}			
		}		
	</script>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
<% 
	if (sTipo!=null && sTipo.equals("LETRADO")) {
%>
		 <siga:Titulo titulo="censo.fichaCliente.facturacion.anticipos.cabecera" localizacion="censo.fichaLetrado.facturacion.localizacion"/>
<%
	} else {
%>
		<siga:TituloExt titulo="censo.fichaCliente.facturacion.anticipos.cabecera" localizacion="censo.fichaCliente.facturacion.anticipos.localizacion"/>
<%
	}
%>
</head>

<body class="tablaCentralCampos">
	<table class="tablaTitulo" align="center" cellspacing="0">
		<html:form action="/CEN_AnticiposCliente.do" method="POST" target="_self">
			<html:hidden styleId="modo" property="modo" value="" />
			<html:hidden styleId="accion" property="accion" value="<%=modo%>" />
			<html:hidden styleId="idPersona" name="AnticiposClienteForm" property="idPersona" value="<%=idPersona%>" />
			<html:hidden styleId="idInstitucion" name="AnticiposClienteForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<input type="hidden" id="incluirRegistrosConBajaLogica" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
		</html:form>

		<tr>
			<td class="titulitosDatos">
				<%=busc%>
			</td>
		</tr>
	</table>
<%
	String tamanosCol="10,45,15,15,15";
	String nombresCol="cen.consultaAnticipos.literal.fecha,cen.consultaAnticipos.literal.descripcion,cen.consultaAnticipos.literal.importe,cen.consultaAnticipos.literal.importeRestante,";
%>
	<siga:Table 
	   name="tabladatos"
	   border="1"
	   columnNames="<%=nombresCol%>"
	   columnSizes="<%=tamanosCol%>"
	   modal="M">
<%	
		if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%	
		} else { 
			for (int i=0;i<resultado.size();i++) {
			 	Row fila = (Row) resultado.get(i);
             	Hashtable anticipo = fila.getRow();
			 	if (anticipo!=null) { 
					
					String cont = new Integer(i+1).toString();

					if (idInstitucion==null){
						idInstitucion = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDINSTITUCION);
					}
					idPersona = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDPERSONA);
			
					String fecha = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_FECHA);
					if (fecha==null || fecha.equals("")) {
						fecha = "&nbsp;";
					} else {
						fecha = GstDate.getFormatedDateShort(usrbean.getLanguage(), fecha);
					}
					
					String idAnticipo = UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDANTICIPO);
					double precioDouble = UtilidadesHash.getDouble(anticipo, PysAnticipoLetradoBean.C_IMPORTEINICIAL).doubleValue();
					String precioInicial = UtilidadesNumero.formatoCampo(precioDouble).replaceAll(",",".");
					double gastadoDouble = PysLineaAnticipoAdm.getGastadoLineasAnticipo(idInstitucion, idPersona, idAnticipo);
					double restanteDouble = UtilidadesNumero.redondea(precioDouble - gastadoDouble, 2);
					String precioRestante = UtilidadesNumero.formatoCampo(restanteDouble).replaceAll(",",".");

					String botones = (gastadoDouble>0 ? (restanteDouble>0 ? "C,E" : "C") : "C,E,B");
					
					FilaExtElement[] elems=new FilaExtElement[2];
					elems[0] = new FilaExtElement("abrirFacturas","abrirFacturas",SIGAConstants.ACCESS_READ);
					if (gastadoDouble>0 && restanteDouble>0) {
						String tituloIcono = UtilidadesMultidioma.getDatoMaestroIdioma("censo.fichaCliente.facturacion.anticipos.icono.darBaja", usrbean);
	   					elems[1]=new FilaExtElement("solicitarbaja", "darBaja", tituloIcono, SIGAConstants.ACCESS_READ);
					}
			%>	 
			
					<siga:FilaConIconos fila="<%=cont%>" botones="<%=botones%>" modo="<%=modo %>" elementos="<%=elems%>" clase="listaNonEdit">			
						<td>
							<input type="hidden" id="oculto<%=cont%>_1" name="oculto<%=cont%>_1" value="<%=idInstitucion%>"/>
							<input type="hidden" id="oculto<%=cont%>_2" name="oculto<%=cont%>_2" value="<%=UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDPERSONA)%>"/>
							<input type="hidden" id="oculto<%=cont%>_3" name="oculto<%=cont%>_3" value="<%=UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_IDANTICIPO)%>"/>
							<input type="hidden" id="oculto<%=cont%>_4" name="oculto<%=cont%>_3" value="<%=restanteDouble%>"/>
							<%=fecha%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(anticipo, PysAnticipoLetradoBean.C_DESCRIPCION))%></td>
						<td align="right"><%=UtilidadesString.formatoImporte(precioInicial)%>&nbsp;&euro;</td>
						<td align="right"><%=UtilidadesString.formatoImporte(precioRestante)%>&nbsp;&euro;</td>
					</siga:FilaConIconos>		
<%			
				} // if
		 	} // for
		} // else 
%>			
	</siga:Table>
		
<%
	if (hm.get("datos")!=null && !hm.get("datos").equals("")) { 
%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						idioma="<%=idioma%>"
						modo="abrirOtra"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
						distanciaPaginas=""
						action="<%=actionPag%>" />
<%
	}
%>

	<div style="position:absolute; left:400px;bottom:0px;z-index:99;">
		<table align="center" border="0">
			<tr>
				<td class="botonesDetalle">
					<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
<% 
					if (bIncluirBajaLogica) { 
%>
						<input  type="checkbox" id="bajaLogica" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
<% 
					} else { 
%>
						<input type="checkbox" id="bajaLogica"  name="bajaLogica" onclick="incluirRegBajaLogica(this);">
<% 
					} 
%>
				</td>
			</tr>
		</table>
	</div>

<% 
	if (!busquedaVolver.equals("volverNo")) { 
		if (!usrbean.isLetrado()) { 
%>
			<siga:ConjBotonesAccion botones="V,N"  clase="botonesDetalle" modo="<%=modo%>"/>
<%
		} else {
%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<%
		}
	} else {
		if (!usrbean.isLetrado()) { 
%>
			<siga:ConjBotonesAccion botones="N"  clase="botonesDetalle" modo="<%=modo%>"/>
<%
		} else {
%>
			<siga:ConjBotonesAccion botones="botonesDetalle" />
<%
		}
	}
%>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>