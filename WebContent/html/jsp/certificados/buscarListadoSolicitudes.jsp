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
	
	//par�metro para la vuelta al listado de solicitudes desde la edici�n de env�o
	request.getSession().setAttribute("EnvEdicionEnvio","GS");
	
	Vector<Hashtable<String,Object>> resultado = new Vector<Hashtable<String,Object>>();
	String paginaSeleccionada = "0";
	String totalRegistros = "0";
	String registrosPorPagina = "0";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute("DATAPAGINADOR")!=null){
	 	hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
	 	if (hm.get("datos")!=null && !hm.get("datos").equals("")) {
	  		resultado = (Vector<Hashtable<String,Object>>) hm.get("datos");	  
	    	PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());	
	 		totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());	
	 		registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 	}
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

		function anular(fila) {
			subicono('iconoboton_anular'+fila);
			var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);
			   	
		   	var auxTarget = document.forms[0].target;
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "anular";
		   	document.forms[0].submit();
		   	document.forms[0].target=auxTarget;
		   	finsubicono('iconoboton_anular'+fila);
		}
		
		function generar(fila) {
			subicono('iconoboton_generar'+fila);
		   	var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);

			var oCheck = document.getElementsByName("chkPDF");
			SolicitudesCertificadosForm.idsTemp.value=oCheck[fila-1].value;
			// PDM Ahora se hace una comprobacion previa, si solo existe una plantilla no se muestra la ventana modal y se genera directamente el certificado, si hay mas de una se muestra la ventana modal para elegir una plantilla.
			SolicitudesCertificadosForm.modo.value="comprobarNumPlantillas";
			SolicitudesCertificadosForm.target="submitArea";
			SolicitudesCertificadosForm.submit();
			finsubicono('iconoboton_generar'+fila);
		}

		function enviar(fila) {	
			document.forms[2].filaSelD.value = fila;
			
		   	var auxSol = 'oculto' + fila + '_2';
		    var idSolic = document.getElementById(auxSol);			          		
			   				   	
		   	var auxPers = 'oculto' + fila + '_4';
		    var idPers = document.getElementById(auxPers);	
			    	    
		    var auxDesc = 'oculto' + fila + '_5';
		    var desc = document.getElementById(auxDesc);			    
		    
		    document.forms[2].idSolicitud.value=idSolic.value;
		   	document.forms[2].idPersona.value=idPers.value;
		   	document.forms[2].descEnvio.value=desc.value;
		   	
		   	document.forms[2].modo.value='envioModal';		   	
		   	document.forms[2].subModo.value='SolicitudCertificado';		   	
		   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
		   	if (resultado==undefined||resultado[0]==undefined){			   		
		   		refrescarLocal();
		   	} else {
		   		var idEnvio = resultado[0];
			    var idTipoEnvio = resultado[1];
			    var nombreEnvio = resultado[2];				    
			   	document.forms[2].tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.forms[2].modo.value='editar';
			   	document.forms[2].submit();
		   	}
		}

		function denegar(fila) {
			subicono('iconoboton_denegar'+fila);

		   	var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);
		   	
		   	var auxTarget = document.forms[0].target;
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "denegar";
		   	document.forms[0].submit();
		   	document.forms[0].target=auxTarget;
		   	finsubicono('iconoboton_denegar'+fila);
		}

		function finalizar(fila) {
			subicono('iconoboton_finalizar'+fila);
		   	var datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila,'tablaDatos', datos);
		   	
		   	var auxTarget = document.forms[0].target;
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "finalizar";
		   	document.forms[0].submit();
		   	document.forms[0].target=auxTarget;
		   	finsubicono('iconoboton_finalizar'+fila);
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
			
		function versolicitud(fila){
			var aux = 'oculto' + fila + '_6'; 
	       	var oculto = document.getElementById(aux);
		   	document.forms[0].idPeticion.value=oculto.value;
		    aux='oculto' + fila + '_7';
	      	oculto=document.getElementById(aux);
	      	document.forms[0].idProducto.value=oculto.value;
		  
	       	aux='oculto' + fila + '_8';
	      	oculto=document.getElementById(aux);
	      	document.forms[0].idTipoProducto.value=oculto.value;
		  
		  	aux='oculto' + fila + '_9';
	      	oculto=document.getElementById(aux);
	      	document.forms[0].idProductoInstitucion.value=oculto.value;
		  
			document.forms[0].action="<%=app %>/PYS_GestionarSolicitudes.do?buscar=true";
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
           
		function facturacionrapida(fila) {
			sub();
			
		    var idInstitucion = document.getElementById('oculto' + fila + '_1');			          		
		    var idSolicitud = document.getElementById('oculto' + fila + '_2');
		    var idPersona = document.getElementById('oculto' + fila + '_4');
		    var idProducto = document.getElementById('oculto' + fila + '_7');
		    var idTipoProducto = document.getElementById('oculto' + fila + '_8');		    
		    
		    jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/CER_GestionSolicitudes.do?modo=getAjaxSeleccionSerieFacturacion",				
				data: "idInstitucion=" + idInstitucion.value + "&idTipoProducto=" + idTipoProducto.value + "&idProducto=" + idProducto.value + "&idSolicitud=" + idSolicitud.value + "&idPersona=" + idPersona.value,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){							
						
					// Recupera el identificador de la serie de facturacion
					var idSerieFacturacion = json.idSerieFacturacion;		
					
					if (idSerieFacturacion==null || idSerieFacturacion=='') {
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
									"<%=sBotonGuardarCerrar%>": function() {
										sub();
										idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
										if (idSerieFacturacion==null || idSerieFacturacion=='') {
											alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
											fin();
											
										} else {
											jQuery(this).dialog("close");
											document.forms[0].target = "submitArea";
											document.forms[0].tablaDatosDinamicosD.value = idInstitucion.value + ',' + idSolicitud.value + ',' + idSerieFacturacion;
											document.forms[0].modo.value = "facturacionRapida";
										   	document.forms[0].submit();	
										   	window.setTimeout("fin()",5000,"Javascript");
										}
									},
									"<%=sBotonCerrar%>": function() {
										jQuery(this).dialog("close");
									}
								}
							}
						);
						jQuery(".ui-widget-overlay").css("opacity","0");														
						
					} else {
						document.forms[0].target = "submitArea";
						document.forms[0].tablaDatosDinamicosD.value = idInstitucion.value + ',' + idSolicitud.value + ',' + idSerieFacturacion;	
						document.forms[0].modo.value = "facturacionRapida";
					   	document.forms[0].submit();		
					   	window.setTimeout("fin()",5000,"Javascript");
					}							
				},
				
				error: function(e){
					alert("<%=sError%>");
					fin();
				}
			});			    
			}           
	</script>
</head>

<body class="tablaCentralCampos">
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
	</html:form>

	<!-- Formulario para la b�squeda de clientes -->
	<html:form action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea">
		<html:hidden styleId="modo" property="modo" value="ver"/>
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
	
	<siga:Table
		name="tablaDatos"
		border="1"
	   	columnNames="&nbsp;,
  		  			certificados.solicitudes.literal.idsolicitud,
  		  			certificados.solicitudes.literal.estadosolicitud,
  		  			certificados.solicitudes.literal.apellidosynombre,
  		  			certificados.mantenimiento.literal.certificado,
  		  			certificados.solicitudes.literal.institucionOrigenLista,
  		  			certificados.solicitudes.literal.institucionDestino,
  		  			certificados.solicitudes.literal.estadocertificado,
  		  			certificados.solicitudes.literal.fechaEmision,"
	  	columnSizes="3,7,8,11,10,9,9,7,7,27"
		modal="G">
<%
		if (resultado==null || resultado.size()==0) {
%>
			<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else {
		 	for (int i=0; i<resultado.size(); i++) {
	   			String botones = "C,E,B";
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
		  		
		  		String sCliente = UtilidadesHash.getString(hDatos, "CLIENTE");
		  		boolean esCliente = sCliente!=null && sCliente.equalsIgnoreCase("S");
		  		
				FilaExtElement[] elems = new FilaExtElement[8];					
				if (tipoCertificado!=null && !tipoCertificado.trim().equals("") && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)) {
					elems[0]=new FilaExtElement("generar", "generar", SIGAConstants.ACCESS_READ);
				}
				
				if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO) && (extNumeroFactura==null || extNumeroFactura.trim().equals(""))) {
					elems[1]=new FilaExtElement("anular", "anular", SIGAConstants.ACCESS_READ);
				}
				
				if (tipoCertificado!=null && !tipoCertificado.trim().equals("") && idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)) {
					elems[2]=new FilaExtElement("denegar", "denegar", SIGAConstants.ACCESS_READ);
				}					
				
				if (!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
						(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) || idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO))) {
				   
					if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)) {
						elems[2]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);
				   	} else {
				    	elems[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);
				   	} 	
				}	
				
				if (tipoCertificado!=null && !tipoCertificado.trim().equals("") &&
						!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO) && !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
						(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) || idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)) &&
					    (esCliente || esCGAE)) {
					elems[3]=new FilaExtElement("finalizar", "finalizar", SIGAConstants.ACCESS_READ);
				}
				
				if (tipoCertificado!=null && !tipoCertificado.trim().equals("") &&
					(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) || idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)) &&
				    (esCliente||esCGAE)) {
				   elems[4]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
				}		
				
				if (esCliente) {
					elems[5]=new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
				}					
				
				String idPeticionAux=" ";
				if (idPeticion!=null && !idPeticion.equals("")) {
				 	elems[6]=new FilaExtElement("versolicitud", "versolicitud", SIGAConstants.ACCESS_READ);
				 	idPeticionAux = idPeticion;
				}
					
				if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO) && tipoCertificado2.equals("C") && bCobrado && isPermitirFacturaCertificado!=null && isPermitirFacturaCertificado.booleanValue() &&
					(bSolicitudColegio || idInstitucion.equals("2000"))) {
					String sTipoIcono = UtilidadesHash.getString(hDatos, "TIPO_ICONO"); // 0:SinIcono; 1:Descarga; 2:FacturacionRapida
					if (sTipoIcono!=null && sTipoIcono.equals("2")) {
						elems[7]=new FilaExtElement("facturacionrapida", "facturacionrapida", SIGAConstants.ACCESS_READ);
					} else if (sTipoIcono!=null && sTipoIcono.equals("1")) {
						elems[7]=new FilaExtElement("download", "facturacionrapida", "Descargar Factura", SIGAConstants.ACCESS_READ);
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
					botones = "E";
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
%>

  				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" elementos="<%=elems%>"  visibleConsulta="false" pintarEspacio="no" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=idInstitucionCertificado%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fechaSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=idPersona%>"> 
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=tipoCertificado%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=idPeticionAux%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=idProducto%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=idTipoProducto%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_9" value="<%=idProductoInstitucion%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=idEstadoSolicitud%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=letrado%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_12" value="<%=tipoCertificado2%>">
<%

						// RGG : CAMBIO PARA USAR LOS CHECK PARA ENVIOS. SUS PERMISOS VAN COMO EL BOTON DE ENVIOS.
						if (!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) && 
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
							(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) || idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO))) {
%>
							<input type="checkbox" value="<%=fechaSolicitud + "%%" + idSolicitud + "%%" + tipoCertificado + "%%" + idTipoProducto + "%%" + idProducto + "%%" + idProductoInstitucion + "%%" + idInstitucionCertificado + "%%" + idPersona + "%%" + institucionFinal%>" name="chkPDF">
<%
						} else {
%>
							<input type="checkbox" value="<%=fechaSolicitud + "%%" + idSolicitud + "%%" + tipoCertificado + "%%" + idTipoProducto + "%%" + idProducto + "%%" + idProductoInstitucion + "%%" + idInstitucionCertificado + "%%" + idPersona + "%%" + institucionFinal%>" name="chkPDF" disabled>
<%
						}
%>
					</td>
					<td><%=idSolicitud%></td>
					<td><%=estadoSolicitud%>(<%=UtilidadesString.mostrarDatoJSP(fechaEstado)%>)</td>
					<td><%=cliente%></td>
					<td><%=tipoCertificado%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(institucionOrigen)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(institucionDestino)%></td>
					<td><%=estadoCertificado%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fechaEmision)%></td>
				</siga:FilaConIconos>
<%
			}
		}
%>
	</siga:Table>
 						
<%
	if (hm.get("datos")!=null && !hm.get("datos").equals("")) {
%> 
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
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
</body>
</html>