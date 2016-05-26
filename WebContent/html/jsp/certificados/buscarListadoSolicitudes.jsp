<!DOCTYPE html>
<html>
<head>
<!-- buscarListadoSolicitudes.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CerEstadoCertificadoBean"%> 
<%@ page import="com.siga.beans.CerSolicitudCertificadosBean"%>
<%@ page import="com.siga.beans.CerSolicitudCertificadosAdm"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.PysProductosInstitucionAdm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Vector"%>

<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>
<%@ page import="java.io.File"%>

<!-- JSP -->
<bean:define id="registrosSeleccionados" name="SolicitudesCertificadosForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="SolicitudesCertificadosForm" property="datosPaginador" type="java.util.HashMap"/>

<%
	Boolean isPermitirFacturaCertificado = (Boolean)request.getAttribute("isPermitirFacturaCertificado");
	
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	
	//Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean userBean = (UsrBean)ses.getAttribute("USRBEAN");
	String idioma = userBean.getLanguage().toUpperCase();
	
	String sCGAE = (String)request.getAttribute("esCGAE"); 
	boolean esCGAE = sCGAE.equalsIgnoreCase("true");
	
	request.getSession().setAttribute("CenBusquedaClientesTipo","GS");
	
	//parámetro para la vuelta al listado de solicitudes desde la edición de envío
	request.getSession().setAttribute("EnvEdicionEnvio","GS");
	
	/** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada="0", totalRegistros="0", registrosPorPagina="0", valorCheckFactura="";
	if (datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  	resultado = (Vector)datosPaginador.get("datos");
	    PaginadorBind paginador = (PaginadorBind)datosPaginador.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	}	
	
	 
	String action=app+"/CER_GestionSolicitudes.do?noReset=true";
    
	String sError= UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(userBean, "messages.general.error"));
	String sBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(userBean, "general.boton.close"));
	String sBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(userBean, "general.boton.guardarCerrar")); 
%>
	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
		
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>	  	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

	<script>					
		function refrescarLocal() {
			parent.buscar();
		}
		
		function descargaLog(fila) {
			var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);
		   	
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "descargarLogError";
		   	document.forms[0].submit();
		}	
		
		function download(fila) {
			var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);
		   	
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "descargar";
		   	document.forms[0].submit();
		}
			
			
		function informacionLetrado(fila) {
			document.forms[1].filaSelD.value = fila;
			
			var auxIns = 'oculto' + fila + '_1';
		    var idInst = document.getElementById(auxIns);			          		
		   				   	
		   	var auxPers = 'oculto' + fila + '_4';
		    var idPers = document.getElementById(auxPers);			    

		   	var auxLetrado = 'oculto' + fila + '_11';
		    var idLetrado = document.getElementById(auxLetrado);			    
			
			if (idLetrado.value=='1') {				    
				document.forms[1].tablaDatosDinamicosD.value=idPers.value + ',' + idInst.value + ',LETRADO' + '%';		
			 } else {
				document.forms[1].tablaDatosDinamicosD.value=idPers.value + ',' + idInst.value + '%';		
			 }

		   	document.forms[1].submit();			   	
		}
			
		function descargarFactura(fila) { // PIDE CONFIRMACION
			sub();
			
		    var idInstitucion = document.getElementById('oculto' + fila + '_1');			          		
		    var idSolicitud = document.getElementById('oculto' + fila + '_2');
		    
			document.forms[0].target = "submitArea";
			document.forms[0].tablaDatosDinamicosD.value = idInstitucion.value + ',' + idSolicitud.value + ',Facturado';	
			document.forms[0].modo.value = "facturacionRapida";
		   	document.forms[0].submit();		
		   	window.setTimeout("fin()",5000,"Javascript");
		}
		 
	</script>
</head>

<body class="tablaCentralCampos"   onload="cargarChecks();checkTodos()">
	<html:form action="/CER_GestionSolicitudes.do?noReset=true" method="POST" target="resultado">
		<html:hidden styleId="modo" property="modo" value=""/>
		<html:hidden styleId="hiddenFrame" property="hiddenFrame" value="1"/>
		<input type="hidden" id="idsParaGenerarFicherosPDF" name="idsParaGenerarFicherosPDF" value="">
		<input type="hidden" id="idsTemp" name="idsTemp" value="">
		<input type="hidden" id="validado" name="validado" value="0">
		<input type="hidden" id="idPeticion" name="idPeticion" value="">
		<input type="hidden" id="idProducto" name="idProducto" value="">
		<input type="hidden" id="idTipoProducto" name="idTipoProducto" value="">
		<input type="hidden" id="idProductoInstitucion" name="idProductoInstitucion" value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
	</html:form>
	
	<!-- Formulario para la búsqueda de clientes -->
	<html:form action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea">
		<html:hidden styleId="modo" property="modo" value="editar"/>
		<html:hidden styleId="filaSelD" property="filaSelD"/>
		<html:hidden styleId="tablaDatosDinamicosD" property="tablaDatosDinamicosD" value="ver"/>
	</html:form>
	
	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
		<html:hidden styleId="actionModal" property="actionModal" value=""/>
		<html:hidden styleId="modo" property="modo" value=""/>
		<html:hidden styleId="subModo" property="subModo" value=""/>
		<html:hidden styleId="idSolicitud" property="idSolicitud" value=""/>
		<html:hidden styleId="idPersona" property="idPersona" value=""/>
		<html:hidden styleId="descEnvio" property="descEnvio" value=""/>
		<html:hidden styleId="filaSelD" property="filaSelD" value="ver"/>
	</html:form>	
	
	<div id="divSeleccionSerieFacturacion" title="<siga:Idioma key='facturacion.seleccionSerie.titulo'/>" style="display:none">
		<table align="left">
			<tr>		
				<td class="labelText" nowrap>
					<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.serieFacturacion"/>&nbsp;(*)
				</td>
				<td>
					<select class='box' style='width:270px' id='selectSeleccionSerieFacturacion'>
					</select>
				</td>
			</tr>				
							
			<tr>
				<td class="labelTextValue" colspan="2">
					<siga:Idioma key="pys.gestionSolicitudes.aviso.seriesFacturacionMostradas"/>
				</td>
			</tr>					
		</table>			
	</div>

	
	<siga:Table
		name="tablaDatos"
		border="1"
	    		columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,
  		  			certificados.solicitudes.literal.idsolicitud,
  		  			certificados.solicitudes.literal.estadosolicitud,
  		  			certificados.solicitudes.literal.apellidosynombre,
  		  			certificados.mantenimiento.literal.certificado,
  		  			certificados.solicitudes.literal.institucionOrigenLista,
  		  			certificados.solicitudes.literal.fechaEmision,"
	  	columnSizes="3,7,16,23,13,10,10,18">
<%
		if (resultado==null || resultado.size()==0) {
%>
			<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else {
		 	for (int i=0; i<resultado.size(); i++) {
	   			String botones = "C,E";
	   			Hashtable<String,Object> hDatos = (Hashtable<String,Object>) resultado.elementAt(i);
				
		  		// RGG obtengo los datos en la jsp
		  		String extNumeroFactura = UtilidadesHash.getString(hDatos, FacFacturaBean.C_NUMEROFACTURA);
		  		String idEstadoSolicitud = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
				String tipoCertificado = UtilidadesHash.getString(hDatos, "TIPOCERTIFICADO");
				String tipoCertificado2 = UtilidadesHash.getString(hDatos, "TIPOCERTIFICADO2");	
		  		String idEstadoCertificado = UtilidadesHash.getString(hDatos, CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO);
		  		String idPeticion = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO);
		  		String idInstitucion = userBean.getLocation();	
		  		
			  		
		  		String sFechaCobro = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHACOBRO);
		  		boolean bCobrado = sFechaCobro!=null && !sFechaCobro.equals("");
		  		
		  		String idInstitucionSol = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL);
		  		boolean bSolicitudColegio = idInstitucionSol!=null && !idInstitucionSol.equals(CerSolicitudCertificadosAdm.IDCGAE) && !idInstitucionSol.substring(0,2).equals("30");
		  		
		  		String sCliente = UtilidadesHash.getString(hDatos, "ESCLIENTE");
		  		boolean esCliente = sCliente!=null && sCliente.equalsIgnoreCase("S");
		  		
				FilaExtElement[] elems = new FilaExtElement[8];					
				
				if (tipoCertificado!=null && !tipoCertificado.trim().equals("") && (idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) 
					|| idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)) && (esCliente||esCGAE)) {
				   elems[1]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
				}		
				
				if (esCliente) {
					elems[2]=new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
				}					
				
				String idPeticionAux=" ";
				if (idPeticion!=null && !idPeticion.equals("")) {
				 	idPeticionAux = idPeticion;
				}
					
				if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO) && tipoCertificado2.equals("C") && bCobrado && isPermitirFacturaCertificado!=null
					&& isPermitirFacturaCertificado.booleanValue() && (bSolicitudColegio || idInstitucion.equals("2000"))) {
					String sTipoIcono = UtilidadesHash.getString(hDatos, "TIPO_ICONO");
					if (sTipoIcono!=null && sTipoIcono.equals("1")) {
						elems[3]=new FilaExtElement("download", "descargarFactura", "Descargar Factura", SIGAConstants.ACCESS_READ);
					}										
				}				
					
				String institucionFinal = "";
				if (tipoCertificado2!=null && tipoCertificado2.trim().equals(PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION)) { // comunicacion							 
					institucionFinal = idInstitucion;
				} else if (tipoCertificado2!=null && tipoCertificado2.trim().equals(PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA)) { // diligencia							
					institucionFinal = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
				} else { // certificado
					institucionFinal = idInstitucion;
				}	
					
				if (extNumeroFactura!=null && !extNumeroFactura.trim().equals("")) {
					botones = "C,E";
				}		
				
				
				String rutaFichero = UtilidadesHash.getString(hDatos, "FICHERO_LOG_ERROR");
				StringBuffer sFicheroLog = new StringBuffer(rutaFichero);
				sFicheroLog.append("-");
				sFicheroLog.append("LogError");
				sFicheroLog.append(".log.xls");
				
				File fichero = new File(sFicheroLog.toString());
				FilaExtElement[] elementosFila=new FilaExtElement[1];
				if(fichero!=null && fichero.exists()){
					elems[4]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
				}	
				
				if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)){
					botones += ",B";
				}
				
				String fechaSolicitud = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHASOLICITUD);
				String idProducto = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
				String idTipoProducto = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
				String idProductoInstitucion = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
				String idInstitucionCertificado = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCION);
				String idPersona = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPERSONA_DES);
				String idSolicitud = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDSOLICITUD);
				String fechaEmision = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO);
				fechaEmision = GstDate.getFormatedDateShort(userBean.getLanguage(), fechaEmision);
				String fechaEstado = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHAESTADO);									
				fechaEstado = GstDate.getFormatedDateShort(userBean.getLanguage(), fechaEstado);
				String letrado = UtilidadesHash.getString(hDatos, "LETRADO");
				String cliente = UtilidadesHash.getString(hDatos, "CLIENTE");
				String institucionOrigen = UtilidadesHash.getString(hDatos, "INSTITUCIONORIGEN");
				String institucionDestino = UtilidadesHash.getString(hDatos, "INSTITUCIONDESTINO");
				String estadoSolicitud = UtilidadesHash.getString(hDatos, "DESCRIPCION_ESTADOSOLICITUD");
				String estadoCertificado = UtilidadesHash.getString(hDatos, "DESCRIPCION_ESTADOCERTIFICADO");
				String idPeticionProducto = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO);
				String idInstitucionOrigen = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
				
%>

  				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" elementos="<%=elems%>"  visibleConsulta="false" pintarEspacio="no" clase="listaNonEdit" visibleBorrado="false">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=idInstitucionCertificado%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fechaSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=idPersona%>"> 
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=tipoCertificado.replaceAll(",", ".")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=idPeticionAux%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=idProducto%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=idTipoProducto%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_9" value="<%=idProductoInstitucion%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=idEstadoSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=letrado%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_12" value="<%=tipoCertificado2%>">
<%

						//String valorCheck = idSolicitud  +"||"+ idInstitucionCertificado;
						//String valorCheck = fechaSolicitud + "||" + idSolicitud + "||" + idTipoProducto + "||" + idProducto + "||" + idProductoInstitucion + "||" 
						//+ idInstitucionCertificado + "||" + idPersona;
						
						String valorCheck = fechaSolicitud + "||" + idSolicitud + "||" + tipoCertificado.trim() + "||" +  idTipoProducto + "||" + idProducto + "||" 
						+ idProductoInstitucion + "||" + idInstitucionCertificado + "||" + idPersona + "||" + idInstitucionOrigen +"||"+idEstadoSolicitud;

							boolean isChecked = false;
							for (int z = 0; z < registrosSeleccionados.size(); z++) {
								Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
								String clave = (String)clavesRegistro.get("CLAVE");
								if (valorCheck.equalsIgnoreCase(clave)) {
									isChecked = true;
									break;
								}
							}
							
							if (isChecked) {
%>
								<input type="checkbox" value="<%=valorCheck%>" checked name="chkPDF" onclick="pulsarCheck(this)">
<%
							} else {
%>					
									<input type="checkbox" value="<%=valorCheck%>" name="chkPDF" onclick="pulsarCheck(this)">
<%
							}
							
					
%>

					</td>
					<td><%=idSolicitud%></td>
					<td><%=estadoSolicitud%>&nbsp;(<%=UtilidadesString.mostrarDatoJSP(fechaEstado)%>)</td>
					<td><%=cliente%></td>
					<td><%=tipoCertificado%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(institucionOrigen)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fechaEmision)%></td>
				</siga:FilaConIconos>
<%
			}
		}
%>
	</siga:Table>
 			
		<script type="text/javascript">
		function editar(fila, id) {
			if (typeof id == 'undefined')
				id='tablaDatos';
			preparaDatos(fila, id);
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();		
		 }
		
		function consultar(fila, id) {
			if (typeof id == 'undefined')
				id='tablaDatos';
			preparaDatos(fila, id);
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "ver";
			document.forms[0].submit();		
		 }
		 
	</script>		
				
<%
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));

	if (datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")) {
%>		
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						registrosSeleccionados="<%=regSeleccionados%>"
						idioma="<%=idioma%>"
						modo="buscar"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
						distanciaPaginas=""
						action="<%=action%>" />
<%
	}
%>	

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>	
	<script language="javascript">
	ObjArray = new Array();
	var maximoRegistrosSeleccionables=1000;

	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}

	function pulsarCheck(obj){
		if (!obj.checked ){
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		} else {
			if(document.getElementById('registrosSeleccionadosPaginador').value<maximoRegistrosSeleccionables){
				ObjArray.push(obj.value);
	   			seleccionados1=ObjArray;
			}else{ 
				alert('<siga:Idioma key="messages.seleccionarMilRegistros"/>');
				$('input:checkbox[value="'+obj.value+'"]').prop("checked", "");	
	   		}
	   			
		}
		document.forms[0].registrosSeleccionados.value=seleccionados1;

		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		
		checkTodos();
	
	}
	
	function cargarChecks(){
		<%
			Integer maxSeleccionable=1000;
			Integer tam=0;
					if (registrosSeleccionados!=null){
						if(registrosSeleccionados.size() >= maxSeleccionable){
							tam = maxSeleccionable;
						}else{
							tam = registrosSeleccionados.size();
						}
		   				for (int p=0;p<tam;p++){
			   				Hashtable clavesFac= (Hashtable) registrosSeleccionados.get(p);
							valorCheckFactura=(String)clavesFac.get("CLAVE");				
		%>
							var aux='<%=valorCheckFactura%>';
							ObjArray.push(aux);
		<%
						} 
		   			}
		%>
					ObjArray.toString();
					seleccionados1=ObjArray;
				
					document.forms[0].registrosSeleccionados.value=seleccionados1;
			
					if(document.getElementById('registrosSeleccionadosPaginador'))
						document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	}
	
	function cargarChecksTodos(o){  
		if(document.getElementById('registrosSeleccionadosPaginador').value<1000){
		if (document.getElementById('registrosSeleccionadosPaginador')){			
			var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>'); 
		   	if (conf){
				ObjArray = new Array();
			   	if (o.checked){	
			   		
			   		if(<%=totalRegistros%> > 1000){
			   			alert('<siga:Idioma key="messages.numeroRegistrosSeleccionados"/>'+<%=totalRegistros%> +'<siga:Idioma key="messages.numeroRegistrosSeleccionadosMasDeMil"/>'); 
			   			document.getElementById("chkGeneral").checked=false;
			   		}else{
			   			parent.seleccionarTodos('<%=paginaSeleccionada%>');		
			   		}			
			   					
				} else {
					
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;
				 	seleccionados1=ObjArray;				 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPDF");						
					for (var i = 0; i < ele.length; i++) {
						if(!ele[i].disabled){
							ele[i].checked = false; 
						}							
					}		
				}	
			} else {
		   		if (!o.checked) {
		   	  		var ele = document.getElementsByName("chkPDF");							
				  	for (var i = 0; i < ele.length; i++) {
				  		if(!ele[i].disabled){
				  			if(ele[i].checked){	
		     					ele[i].checked = false;
								ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
							}
						}
				   	}					   	
				   	seleccionados1=ObjArray;
				   	
				} else {
					
					var ele = document.getElementsByName("chkPDF");	
					var registrosSeleccionadosEnLaPaginacion= document.getElementById('registrosSeleccionadosPaginador').value;
					if((maximoRegistrosSeleccionables-registrosSeleccionadosEnLaPaginacion)>20){
						for (var i = 0; i < ele.length; i++) {
							if(!ele[i].disabled){
								if(!ele[i].checked){				  		
					    			ele[i].checked = true;
									ObjArray.push(ele[i].value);
								}
							}
						}					   		
					   	seleccionados1=ObjArray;
					}else{
						var numeroRegistrosASeleccionar = (maximoRegistrosSeleccionables-registrosSeleccionadosEnLaPaginacion);
						for (var i = 0; i < numeroRegistrosASeleccionar; i++) {
							if(!ele[i].disabled){
								if(!ele[i].checked){				  		
					    			ele[i].checked = true;
									ObjArray.push(ele[i].value);
								}
							}
						}					   		
					   	seleccionados1=ObjArray;
					   	alert('<siga:Idioma key="messages.seleccionarMilRegistros"/>'); 
					}
				}
				document.forms[0].registrosSeleccionados.value=seleccionados1;			   		
			}
		   	
		   	if(document.getElementById('registrosSeleccionadosPaginador')) {
		   		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		   	}
		}
		}else{
			alert('<siga:Idioma key="messages.seleccionarMilRegistros"/>'); 
		}
	 }
	
	
	
	function checkTodos() {
		var ele = document.getElementsByName("chkPDF");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
   			if(!ele[i].checked && !ele[i].disabled){
   				todos=0;
   				break;
   			} 
   		}	   
	    if (todos==1){	   		
			document.getElementById("chkGeneral").checked=true;
		} else {
			document.getElementById("chkGeneral").checked=false;
		}			
   	}	
	
	</script>
</body>
</html>