<!DOCTYPE html>
<html>
<head>
<!-- busquedaSolicitudesResultados.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@taglib uri="struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="struts-bean.tld" prefix="bean"%>
<%@taglib uri="struts-html.tld" prefix="html"%>
<%@taglib uri="libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.productos.form.GestionSolicitudesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
	String app = request.getContextPath(); 
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	/** PAGINADOR ***/
	String idioma=usr.getLanguage().toUpperCase();
	Vector<Hashtable<String,Object>> resultado = new Vector<Hashtable<String,Object>>();
	String paginaSeleccionada = "0";
	String totalRegistros = "0";
	String registrosPorPagina = "0";
	HashMap hm = new HashMap();
	String idPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	
	if (ses.getAttribute(idPaginador)!=null){
		hm = (HashMap)ses.getAttribute(idPaginador);
	
		if (hm.get("datos")!=null && !hm.get("datos").equals("")){
			resultado = (Vector<Hashtable<String,Object>>)hm.get("datos");
			PaginadorBind paginador = (PaginadorBind)hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
		}
	}
	
	String action=app+"/PYS_GestionarSolicitudes.do?noReset=true";
	request.getSession().setAttribute("EnvEdicionEnvio","GPS");
	
	// Textos del dialog
	String sDialogError= UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "messages.general.error"));
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.guardarCerrar")); 	
	
	GestionSolicitudesForm formGestionSolicitudes = (GestionSolicitudesForm) ses.getAttribute("GestionSolicitudesForm");
	String fechaDesde="", fechaHasta="";
	if (formGestionSolicitudes != null) {
		fechaDesde = formGestionSolicitudes.getBuscarFechaDesde();
		if (fechaDesde != null) {
			fechaDesde = GstDate.getFormatedDateShort("", fechaDesde);
		}
		
		fechaHasta = formGestionSolicitudes.getBuscarFechaHasta();
		if (fechaHasta!=null) {
			fechaHasta = GstDate.getFormatedDateShort("", fechaHasta);
		}
	}
%>

	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>
	<html:form action="/PYS_GestionarSolicitudes.do" method="POST">
		<html:hidden property="modo" value=""/>
		<input type="hidden" name="concepto" id="concepto" value=""> 
    	<input type="hidden" name="idSolicitud" id="idSolicitud" value="">
		<input type="hidden" name="idProducto" id="idProducto" value="">
		<input type="hidden" name="idProductoInstitucion" id="idProductoInstitucion" value="">
		<input type="hidden" name="idTipoProducto" id="idTipoProducto" value="">
		<input type="hidden" name="actionModal" id="actionModal" value="">
		
		<html:hidden name="GestionSolicitudesForm" property="buscarTipoPeticion"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarIdPeticionCompra"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarEstadoPeticion"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarFechaDesde" value="<%=fechaDesde%>"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarFechaHasta" value="<%=fechaHasta%>"/>
		<html:hidden name="GestionSolicitudesForm" property="facturada"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarNColegiado"/>		
		<html:hidden name="GestionSolicitudesForm" property="buscarNifcif"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarNombre"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarApellido1"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarApellido2"/>
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
		name = "tablaResultados"
		border  = "1"
		columnNames="pys.gestionSolicitudes.literal.fecha,
			pys.gestionSolicitudes.literal.idPeticion,
			pys.gestionSolicitudes.literal.tipo,
			pys.gestionSolicitudes.literal.cliente,
			pys.gestionSolicitudes.literal.estadoPeticion,"
		columnSizes = "12,15,19,23,19,12">

<% 
		if (resultado == null || resultado.size()==0) {
%>
	 		<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
			
<% 
		} else { 	    	
			for (int i = 1; i <= resultado.size(); i++)	{	
				Hashtable<String,Object> hPeticionCompraSuscripcion = (Hashtable<String,Object>)resultado.get(i-1);
							 
				if (hPeticionCompraSuscripcion != null) { 
					Long idPeticion = UtilidadesHash.getLong (hPeticionCompraSuscripcion, PysPeticionCompraSuscripcionBean.C_IDPETICION);
					Integer idInstitucion = UtilidadesHash.getInteger (hPeticionCompraSuscripcion, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
					Long idPersona = UtilidadesHash.getLong (hPeticionCompraSuscripcion, CenPersonaBean.C_IDPERSONA);

					String nombreCliente = "";
					nombreCliente = UtilidadesHash.getString(hPeticionCompraSuscripcion, CenPersonaBean.C_NOMBRE) + 
									" " + UtilidadesHash.getString(hPeticionCompraSuscripcion, CenPersonaBean.C_APELLIDOS1) + 
									" " + UtilidadesHash.getString(hPeticionCompraSuscripcion, CenPersonaBean.C_APELLIDOS2);
					String fecha = UtilidadesHash.getString(hPeticionCompraSuscripcion, PysPeticionCompraSuscripcionBean.C_FECHA);					
					String sTipoIcono = UtilidadesHash.getString(hPeticionCompraSuscripcion, "TIPO_ICONO"); // 0:SinIcono; 1:Descarga; 2:FacturacionRapida
			 		String tipoSol = UtilidadesHash.getString(hPeticionCompraSuscripcion, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			 		String tipoSolTexto = "";
			 		String estadoSol = UtilidadesHash.getString(hPeticionCompraSuscripcion, "DESCRIPCION_ESTADO");
			 		String idEstadoSol = UtilidadesHash.getString(hPeticionCompraSuscripcion, "IDESTADOPETICION");

			 		if (fecha == null || fecha.equals("")) {       
			 			fecha = "&nbsp;"; 
			 		} else {
			 			fecha = GstDate.getFormatedDateShort("", fecha);
			 		}
			 		
			 		if (nombreCliente == null || nombreCliente.equals("")) 
			 			nombreCliente = "&nbsp;";
			 			
			 		if (estadoSol == null || estadoSol.equals(""))     
			 			estadoSol = "&nbsp;";
			 			
			 		if (tipoSol == null || tipoSol.equals("")) {     
			 			tipoSol = "&nbsp;";	
			 		} else {
				 		if (tipoSol.equalsIgnoreCase(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {
				 			tipoSolTexto = "pys.tipoPeticion.alta";
				 		} else {
				 			tipoSolTexto = "pys.tipoPeticion.baja";
				 		}
					}

					// 1. Compruebo que no tenga la peticion de compra de baja
					// 2. Compruebo que tenga la solicitud aceptada
					// 3. Compruebo que no sea facturacion programada
					// 4. Compruebo que no tenga certificados
					// 5. Compruebo que no tenga servicios
					// 6. Compruebo que tiene productos facturables
					FilaExtElement[] elems = new FilaExtElement[3];
					elems[0]=new FilaExtElement("editarConCertificado", "editarConCertificado", SIGAConstants.ACCESS_FULL);
					elems[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
					if (idEstadoSol.trim().equals(String.valueOf(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA)) && tipoSol.trim().equals("A")) {
						if (sTipoIcono!=null && sTipoIcono.equals("2")) {
							elems[2]=new FilaExtElement("facturacionrapida", "facturacionrapida", SIGAConstants.ACCESS_READ);		
						} else {
							if (sTipoIcono!=null && sTipoIcono.equals("1")) {
								elems[2]=new FilaExtElement("download", "facturacionrapida", SIGAConstants.ACCESS_READ);
							}
						}
					}
%>
					<siga:FilaConIconos fila='<%=""+i%>' 
						botones="" 
						visibleConsulta="false" 
						visibleBorrado="false" 
						visibleEdicion="false"
						pintarEspacio="false"
						elementos="<%=elems%>" 
						pintarEspacio="no" 							  					  							  
						clase="listaNonEdit"> 
											
						<td><!-- Datos ocultos tabla -->
							<input type="hidden" id="oculto<%=i%>_1" value="<%=idPeticion%>">
							<input type="hidden" id="oculto<%=i%>_2" value="<%=idInstitucion%>">
							<input type="hidden" id="oculto<%=i%>_3" value="<%=tipoSol%>">
							<input type="hidden" id="oculto<%=i%>_4" value="<%=idPersona%>">
							<input type="hidden" id="oculto<%=i%>_5" value="<%=sTipoIcono%>">
							 
							<!-- ENVIOS 1 idSolicitud, 4 idPersona, 5 descripcion -->
							<input type="hidden" name="oculto<%=""+(i)%>_5" value="<%=UtilidadesString.getMensajeIdioma(usr.getLanguage(),tipoSolTexto)%>">
											
							<%=UtilidadesString.mostrarDatoJSP(fecha)%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
						<td><siga:Idioma key="<%=tipoSolTexto%>"/></td> 
						<td><%=UtilidadesString.mostrarDatoJSP(nombreCliente)%></td> 
						<td><%=UtilidadesString.mostrarDatoJSP(estadoSol)%></td> 
					</siga:FilaConIconos>
<%	 		       
				} // if
			}  // for  
		}//else
%>
	</siga:Table>
<%
	if ( hm.get("datos")!=null && !hm.get("datos").equals("")) {
%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
			registrosPorPagina="<%=registrosPorPagina%>" 
			paginaSeleccionada="<%=paginaSeleccionada%>" 
			idioma="<%=idioma%>"
			modo="buscarPor"								
			clase="paginator" 
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
			distanciaPaginas=""
			action="<%=action%>" />
<%
	}
%>	
	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
		<html:hidden styleId = "actionModal"  property = "actionModal" value = ""/>
		<html:hidden styleId = "modo"  property = "modo" value = ""/>
		<html:hidden styleId = "idSolicitud"  property = "idSolicitud" value = ""/>
		<html:hidden styleId = "idPersona"  property = "idPersona" value = ""/>
		<html:hidden styleId = "descEnvio"  property = "descEnvio" value = ""/>
	</html:form>

	<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="_blank" type=""> 
		<input type="hidden" name="modo" value=""> 
		<input type="hidden" name="actionModal" value=""> 
		<html:hidden styleId = "tablaDatosDinamicosD"  property = "tablaDatosDinamicosD" value=""/>
	</html:form>
	
	<script>
		// Refrescar
		function refrescarLocal(){ 		
			parent.buscar();
		}	
	
		function enviar(fila) {
		   	
		   	var auxSol = 'oculto' + fila + '_1';
		    var idSolic = document.getElementById(auxSol);			          		
		   				   	
		   	var auxPers = 'oculto' + fila + '_4';
		    var idPers = document.getElementById(auxPers);	
		    	    
		    var auxDesc = 'oculto' + fila + '_5';
		    var desc = document.getElementById(auxDesc);			    
		    
		    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
		   	document.DefinirEnviosForm.idPersona.value=idPers.value;
		   	document.DefinirEnviosForm.descEnvio.value="Solicitud "+desc.value;
		   	
		   	document.DefinirEnviosForm.modo.value='envioModal';		   	
		   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
			
		   	if (resultado==undefined || resultado[0]==undefined ||resultado[0]=="M"){			   		
		   	} else {
		   		var idEnvio = resultado[0];
			    var idTipoEnvio = resultado[1];
			    var nombreEnvio = resultado[2];				    
			    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
		   	}
		}
		
	 	function editarConCertificado(fila) {
	   		var datos;
	   		datos = document.getElementById('tablaDatosDinamicosD');
	   		datos.value = ""; 
	   		preparaDatos(fila, 'tablaResultados',datos);
	   		
	   		document.forms[0].target = "mainWorkArea";
	   		document.forms[0].modo.value = "Editar";
	   		document.forms[0].submit();
	 	}
	 	
		function facturacionrapida(fila) {
			sub();
			
			var idPeticion = document.getElementById('oculto' + fila + '_1');
		    var idInstitucion = document.getElementById('oculto' + fila + '_2');
		    var tipoIcono = document.getElementById('oculto' + fila + '_5');
		    
		    jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/PYS_GenerarSolicitudes.do?modo=getAjaxSeleccionSerieFacturacion",				
				data: "idInstitucion=" + idInstitucion.value + "&idPeticion=" + idPeticion.value,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){							
						
					// Recupera el identificador de la serie de facturacion
					var idSerieFacturacion = json.idSerieFacturacion;		
					
					if (tipoIcono.value == "2" && (idSerieFacturacion==null || idSerieFacturacion=='')) {
						//jQuery("#selectSeleccionSerieFacturacion")[0].innerHTML = json.aOptionsSeriesFacturacion[0];
						jQuery("#selectSeleccionSerieFacturacion").find("option").detach();
						jQuery("#selectSeleccionSerieFacturacion").append(json.aOptionsSeriesFacturacion[0]);
						
						fin();
						jQuery("#divSeleccionSerieFacturacion").dialog(
							{
								height: 220,
								width: 550,
								modal: true,
								resizable: false,
								buttons: {
									"<%=sDialogBotonGuardarCerrar%>": function() {
										sub();
										idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
										if (idSerieFacturacion==null || idSerieFacturacion=='') {
											alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
											fin();
											
										} else {
											jQuery(this).dialog("close");
											document.solicitudCompraForm.target = "submitArea";
											document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;
											document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
											document.solicitudCompraForm.submit();
										}
										fin();
									},
									"<%=sDialogBotonCerrar%>": function() {
										jQuery(this).dialog("close");
									}
								}
							}
						);
						jQuery(".ui-widget-overlay").css("opacity","0");														
						
					} else {
						document.solicitudCompraForm.target = "submitArea";
						document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;	
						document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
						document.solicitudCompraForm.submit();
					}											
				},
				
				error: function(e){
					alert("<%=sDialogError%>");
					fin();
				}
			});	
		    fin();
		}            	
	</script>	
</body>
</html>