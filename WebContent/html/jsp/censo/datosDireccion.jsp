<!DOCTYPE html>
<html>
<head>
<!-- datosDireccion.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL Grande -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.CenDireccionTipoDireccionBean"%>
<%@ page import="com.siga.beans.CenTipoDireccionBean"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="com.siga.beans.CenPoblacionesBean"%>
<%@ page import="com.siga.beans.CenProvinciaBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = usr.getOcultarHistorico();

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;

	boolean desactivado = false;
	String clase = "box";
	String botones = "C,Y,R";

	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;
	int tam_max_codigo_postal = ClsConstants.TAM_MAX_CODIGO_POSTAL;
	int tam_min_codigo_postal = ClsConstants.TAM_MIN_CODIGO_POSTAL;

	String nombreUsu = (String) request.getAttribute("nombrePersona");
	String numero = (String) request.getAttribute("numero");
	Vector vTipos = (Vector) request.getAttribute("vTipos");
	String tipoCliente = (String) request.getAttribute("tipoCliente");
	Hashtable hTiposDir = (Hashtable) request.getAttribute("TipoDirecciones");

	Hashtable htData = null;
	String preferente = "";
	String domicilio = "";
	String codigoPostal = "";
	String idTipoDireccion = "";
	String provincia = "";
	String pais = "";
	String poblacion = "";
	String poblacionExt = "";
	String telefono1 = "";
	String telefono2 = "";
	String movil = "";
	String fax1 = "";
	String fax2 = "";
	String mail = "";
	String paginaWEB = "";
	String desactivarCheckTipos = "";
	String otraProvinciaString="";
	boolean preferenteMail = false;
	boolean preferenteCorreo = false;
	boolean preferenteFax = false;
	boolean preferenteSms = false;
	String idPersona = "";
	String idInstitucion = "";
	String idDireccion = "";
	String sIdprovincia = "";
	ArrayList idProvincia = new ArrayList();
	ArrayList idPais = new ArrayList();
	ArrayList idPoblacion = new ArrayList();
	ArrayList idTipoDireccionArrayList = new ArrayList();
	String fechaModificacion = "", fechaBaja = "";
	String colegioOrigen = "";
	String ididPais = "";

	String modo = (String) request.getAttribute("modoConsulta");
	if (modo != null && modo.equals("editar")) {
		botones += ",GAH";
	}
	if (modo.equalsIgnoreCase("ver") || modo.equalsIgnoreCase("editar") || modo.equalsIgnoreCase("duplicar")) {
		htData = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		if (htData != null) {
			domicilio = String.valueOf(htData.get(CenDireccionesBean.C_DOMICILIO));
			codigoPostal = String.valueOf(htData.get(CenDireccionesBean.C_CODIGOPOSTAL));

			idTipoDireccion = String.valueOf(htData.get(CenTipoDireccionBean.C_DESCRIPCION));
			provincia = String.valueOf(htData.get("PROVINCIA"));
			pais = String.valueOf(htData.get("PAIS"));
			poblacion = String.valueOf(htData.get("POBLACION"));
			poblacionExt = String.valueOf(htData.get("POBLACIONEXTRANJERA"));
			telefono1 = String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO1));
			telefono2 = String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO2));
			movil = String.valueOf(htData.get(CenDireccionesBean.C_MOVIL));
			fax1 = String.valueOf(htData.get(CenDireccionesBean.C_FAX1));
			fax2 = String.valueOf(htData.get(CenDireccionesBean.C_FAX2));
			mail = String.valueOf(htData.get(CenDireccionesBean.C_CORREOELECTRONICO));
			paginaWEB = String.valueOf(htData.get(CenDireccionesBean.C_PAGINAWEB));
			idDireccion = String.valueOf(htData.get(CenDireccionesBean.C_IDDIRECCION));
			fechaModificacion = String.valueOf(htData.get(CenDireccionesBean.C_FECHAMODIFICACION));
			otraProvinciaString =String.valueOf( htData.get(CenDireccionesBean.C_OTRAPROVINCIA));
			if (fechaModificacion != null)
				fechaModificacion = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaModificacion));
			else
				fechaModificacion = "";

			fechaBaja = String.valueOf(htData.get(CenDireccionesBean.C_FECHABAJA));
			if ((fechaBaja != null) && !fechaBaja.equals(""))
				fechaBaja = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaBaja));
			else
				fechaBaja = "";
			
			colegioOrigen = String.valueOf(htData.get("COLEGIOORIGEN"));

			String aux = "";
			aux = (String) request.getAttribute("preferenteFax");
			if ((aux != null) && (aux.equals("true")))
				preferenteFax = true;
			aux = (String) request.getAttribute("preferenteMail");
			if ((aux != null) && (aux.equals("true")))
				preferenteMail = true;
			aux = (String) request.getAttribute("preferenteCorreo");
			if ((aux != null) && (aux.equals("true")))
				preferenteCorreo = true;
			aux = (String) request.getAttribute("preferenteSms");
			if ((aux != null) && (aux.equals("true")))
				preferenteSms = true;

			if (modo.equalsIgnoreCase("editar") || modo.equalsIgnoreCase("duplicar")) {
				desactivado = false;
				editarCampos = true;
			} else { // Ver
				desactivado = true;
				clase = "boxConsulta";
				desactivarCheckTipos = "disabled";
			}
			idPersona = String.valueOf((Long) request.getAttribute("idPersona"));
			idInstitucion = String.valueOf(htData.get(CenDireccionesBean.C_IDINSTITUCION));

			idProvincia.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPROVINCIA)));
			sIdprovincia = String.valueOf(htData.get(CenDireccionesBean.C_IDPROVINCIA));
			idPoblacion.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPOBLACION)));
			ididPais = String.valueOf(htData.get(CenDireccionesBean.C_IDPAIS));
			idPais.add(ididPais);
		}
	} else {
		if (modo.equalsIgnoreCase("nuevo")) {
			idPais.add("");
			editarCampos = true;
			desactivado = false;
			idPersona = String.valueOf((Long) request.getAttribute("idPersona"));
			idInstitucion = String.valueOf((Integer) request.getAttribute("idInstitucion"));
		}
	}

	//Se ha a�adido para crear bien la direcci�n del enlace la pagina web
	String enlaceWEB = "";
	String quitar;
	String lista = "";

	if (!paginaWEB.equals("") && paginaWEB.length() >= 7) {

		lista = paginaWEB.substring(0, 7);
		if (lista.equalsIgnoreCase("http://")) {
			enlaceWEB = paginaWEB;
		}

		if (!lista.equalsIgnoreCase("http://") && (!lista.equalsIgnoreCase("http:\\\\"))) {
			enlaceWEB = "http://" + paginaWEB;
		}

		if (lista.equalsIgnoreCase("http:\\\\")) {
			quitar = paginaWEB.substring(7);
			enlaceWEB = "http://" + quitar;
		}

	}
%>

<style>
.ocultar {
	display: none
}
</style>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<!-- Validaciones en Cliente -->
<html:javascript formName="consultaDireccionesForm"
	staticJavascript="false" />
<script src="<%=app%>/html/js/validacionStruts.js"
	type="text/javascript"></script>
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"
	type="text/javascript"></script>

<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
<script language="JavaScript">
	
		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		var paisGlobal='<%=ididPais%>';
		jQuery(function(){
			jQuery("#pais").on("change", function(){
				selPais(jQuery(this).val());
				
				if(trim(document.consultaDireccionesForm.codigoPostal.value) != "")
					if(trim(document.consultaDireccionesForm.pais.value)!=idEspana && trim(document.consultaDireccionesForm.pais.value) != "")
						jQuery("#codigoPostal").val("");
			});
		});
		
		//Asociada al boton Volver -->
		function accionCerrar(){ 		
			window.top.close();
		}	
		//Asociada al boton Restablecer -->
		function accionRestablecer(){
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.consultaDireccionesForm.reset();
				comprobarAsterico();
				selPais(<%=ididPais%>);
				jQuery("#provincia").change();
				
				<%if(ididPais.equals(ClsConstants.ID_PAIS_ESPANA) || "".equals(ididPais)){%>	
						document.consultaDireccionesForm.codigoPostal.value='<%=codigoPostal%>';
						<%if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")){%>
						  	jQuery("#provinciaDiv").show()
						    jQuery("#provinciaText").hide();
						    jQuery("#otraProvinciaCheck").attr('checked','checked');
						<%}else{%>
							jQuery("#provinciaText").show();
						<%}%>
				<%}else{%>
					var codpostal = '<%=codigoPostal%>';
					var poblacionExtAux = '<%=poblacionExt%>';
					if(codpostal == null){
						document.consultaDireccionesForm.codigoPostal.value="";
					}else{
						document.consultaDireccionesForm.codigoPostal.value=codpostal;
					}
					document.getElementById("poblacionExt").value=poblacionExtAux;
					createProvince();
				<%}%>
			}		
		}
	
	    function validateConsultaDireccionesFormAux(form) {                                                                   
	       	return validateMaxLength(form) && validateMask(form); 
	    }
	    
	    function actualizar(){			
		    document.forms[0].modificarPreferencias.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="0";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="0";
		    document.forms[0].modificarDireccionesFacturacion.value="0";
		    document.forms[0].control.value="0";   
		    document.forms[0].submit();
			document.forms[0].modificarPreferencias.value="0";
	    }

	    function actualizarCenso(){
	    	document.forms[0].modificarDireccionesFacturacion.value="0";
		    document.forms[0].modificarDireccionesCensoWeb.value="1";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="0";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			
	    }
	    function actualizarFacturacion(){
	    	document.forms[0].modificarDireccionesFacturacion.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="0";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="0";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesFacturacion.value="0";
			document.forms[0].modificarPreferencias.value="0";
			
	    }
	    function actualizarCensoFacturacion(){
	    	document.forms[0].modificarDireccionesFacturacion.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="1";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="0";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			document.forms[0].modificarDireccionesFacturacion.value="0";
			
	    }
	    function actualizarCensoTraspasoOJFacturacion(){
	    	document.forms[0].modificarDireccionesFacturacion.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="1";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="1";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			document.forms[0].modificarDireccionesFacturacion.value="0";
	    }
		function actualizarTraspasoOJFacturacion(){
			document.forms[0].modificarDireccionesFacturacion.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="0";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="1";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			document.forms[0].modificarDireccionesFacturacion.value="0";
	    }
		function actualizarCensoWebTraspasoOJ(){
			document.forms[0].modificarDireccionesFacturacion.value="0";
		    document.forms[0].modificarDireccionesCensoWeb.value="1";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="1";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			document.forms[0].modificarDireccionesFacturacion.value="0";
	    }
		function actualizarTraspasoOJ(){
			document.forms[0].modificarDireccionesFacturacion.value="0";
		    document.forms[0].modificarDireccionesCensoWeb.value="0";
		    document.forms[0].modificarDireccionesTraspasoOJ.value="1";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarDireccionesTraspasoOJ.value="0";
			document.forms[0].modificarPreferencias.value="0";
			document.forms[0].modificarDireccionesFacturacion.value="0";
		}
	    
	    
		function selPais(valor) {
			if (valor!=null && valor!="" && valor!=idEspana) {
				// Pais distinto a Espana. Hay que mostrar el texto de poblacion extranjera y ocultar la seleccion de provincia y poblacion espanolas
				
				if((paisGlobal != "") && (paisGlobal ==idEspana)) { //no se borra el codigo postal al inicio
					jQuery("#codigoPostal").val("");
				}
				
				jQuery("#provinciaLabel").hide();
				
				jQuery("#otraProvinciaCheck").removeAttr('checked');
				jQuery("#tdOtraProvincia").hide();
				jQuery("#provinciaText").val("");
				jQuery("#provinciaText").hide();
				jQuery("#provincia").val(jQuery("#provincia option:first").val());
				jQuery("#provincia").val("");
				jQuery("#provincia").change();
				jQuery("#provinciaDiv").hide();
				
				jQuery("#poblacion").val("");
				jQuery("#poblacionEspanola").hide();
				jQuery("#poblacionExtranjera").show();
				
			} else {
				// �ESTO ES ESPANAAAAA!. Hay que ocultar el texto de poblacion extranjera y mostrar la seleccion de provincia y poblacion espanolas
				
				if((paisGlobal != "") && (paisGlobal !=idEspana)) { //no se borra el codigo postal al inicio
					jQuery("#codigoPostal").val("");
				}
				
				jQuery("#provinciaLabel").show();
				
				jQuery("#tdOtraProvincia").show();
				if(jQuery("#otraProvinciaCheck").is(':checked') && jQuery("#otraProvinciaCheck").is(':visible')){
					jQuery("#provinciaText").hide();
					jQuery("#provinciaDiv").show();
				} else {
					jQuery("#otraProvinciaCheck").removeAttr('checked');
					jQuery("#provinciaDiv").hide();
				}
				
				jQuery("#poblacionExt").val("");
				jQuery("#poblacionExtranjera").hide();
				jQuery("#poblacionEspanola").show();
				
				comprobarAsterico();
			}
			paisGlobal=jQuery("#pais").val();
		}
		
		function selPaisInicio() {
			var valor = document.getElementById("pais").value;
			selPais(valor);
		}
		
		function comprobarAsterico() {
			var checkGuardia = false;
			var checkPostal = false;
			var oCheck = document.getElementsByName("checkTipoDireccion");
			
			for (i=0; i < oCheck.length; i++) {			 
				if (oCheck[i].checked) {
					var tipoDir = oCheck[i].value;
					if (tipoDir==6) {
						checkGuardia = true;
					} else if (tipoDir==3||tipoDir==2||tipoDir==5||tipoDir==8||tipoDir==9) {
						checkPostal = true;
					}
				}
			}		
			
			if (checkGuardia) {
				$('#telefonoSinAsterisco').hide();
				$('#telefonoConAsterisco').show();
			} else {
				$('#telefonoSinAsterisco').show();
				$('#telefonoConAsterisco').hide();
	    	}
			
			if (checkPostal) {
				$('#direccionSinAsterisco').hide();
				$('#direccionConAsterisco').show();
				$('#cpSinAsterisco').hide();
				$('#cpConAsterisco').show();
				$('#poblacionSinAsterisco').hide();
				$('#poblacionConAsterisco').show();
				$('#paisSinAsterisco').hide();
				$('#paisConAsterisco').show();
			} else {
				$('#direccionSinAsterisco').show();
				$('#direccionConAsterisco').hide();
				$('#cpSinAsterisco').show();
				$('#cpConAsterisco').hide();
				$('#poblacionSinAsterisco').show();
				$('#poblacionConAsterisco').hide();
				$('#paisSinAsterisco').show();
				$('#paisConAsterisco').hide();
	    	}
	    } 

//Asociada al boton GuardarCerrar -->
		function accionGuardarAnyadirHistorico() {          
           sub();
           document.consultaDireccionesForm.telefono1.value=eliminarBlancos(trim(document.consultaDireccionesForm.telefono1.value));
		   document.consultaDireccionesForm.telefono2.value=eliminarBlancos(trim(document.consultaDireccionesForm.telefono2.value));
			// Validamos los errores ///////////
			// Campos Tipo de Direccion obligatorio -> no se usa validacion Struts pq es multiple seleccion
			
			var oCheck = document.getElementsByName("checkTipoDireccion");
			var checkActivo=false;
			var datosTipoDir="";
			for(i=0; i<oCheck.length; i++) {
			 	if (oCheck[i].checked){
				   checkActivo=true;
				}
			}
			if (!checkActivo) {
			     var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.tipoDireccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}			
			
			//var long=document.consultaDireccionesForm.idTipoDireccion.options.length;
						
			var cont=0;
			var checkGuardia=false;
			var checkPostal=false;
			
			for(i=0; i<oCheck.length; i++) {  			
				if (oCheck[i].checked){
				
					cont++;
					 var tipoDir = oCheck[i].value;
					 datosTipoDir=datosTipoDir+tipoDir+",";
					if(tipoDir == 6) {
						checkGuardia=true;
					}

					// si es de tipo correo, despacho o guia lo apunto
					if(tipoDir == 3
						|| tipoDir == 2
						|| tipoDir == 5|| tipoDir == 9) {
						
						checkPostal=true;
					}
						
				}
			}

           document.consultaDireccionesForm.idTipoDireccionNew.value=datosTipoDir;
		  
		   
			// VALIDAMOS QUE SOLO HAYAN INTRODUCIDO EN EL COMBO DE TIPO DE DIRECCION DIRECCION DE GUARDIA.
			// SI ES ASI SOLO REALIZAMOS LA VALIDACION DEL CAMPO TELEFONO 1. EN CASO CONTRARIO SE REALIZAN
			// EL RESTO DE VALIDACIONES TAMBIEN NECESARIAS PARA LOS OTROS TIPOS DE DIRECCION
			
			
		    
			// RGG 01-03-2005 cambio de validacion
				
				
				for(i=0; i<oCheck.length; i++) {
					if (oCheck[i].checked)
					{  var tipoDir = oCheck[i].value;
						if(tipoDir == 6)
							if(trim(document.consultaDireccionesForm.telefono1.value)=="")
							{
								var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.telefono1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
								alert(mensaje);
								fin();
								return false;
							}
							
						}
					}	
				
			//VALIDACIONES GENERALES A TODOS LOS TIPOS DE DIRECCION SELECCIONADOS
		  
		   	if (!validateConsultaDireccionesForm(document.consultaDireccionesForm)){
				fin();
				return false;
			}
		   
		   if((document.consultaDireccionesForm.preferenteMail.checked) && 
					 (trim(document.consultaDireccionesForm.correoElectronico.value)=="")) {
				
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
				 
					 return false;
				}
				if((document.consultaDireccionesForm.preferenteCorreo.checked) && 
					 (trim(document.consultaDireccionesForm.domicilio.value)=="")) {
					 
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
					 fin();
					 return false;
				}
				// RGG 25/04/2005
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") &&
					(trim(document.consultaDireccionesForm.codigoPostal.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				var value = trim(document.consultaDireccionesForm.codigoPostal.value);
				// Si es obligatorio el codigo postal e introduce menos de 5
				
				if((jQuery("#cpConAsterisco").attr("class") != "ocultar")  &&
					(value.length<<%=tam_max_codigo_postal%>)) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				
				//Si no es obligatorio pero introduce campo tampoco puede ser menos de 5. 
				if((jQuery("#cpConAsterisco").attr("class") == "ocultar") &&
						((value.length>=<%=tam_min_codigo_postal%>)&&(value.length < <%=tam_max_codigo_postal%>))) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)==idEspana ||trim(document.consultaDireccionesForm.pais.value)=="" ) && 
					(trim(document.consultaDireccionesForm.provincia.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.provincia"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)==idEspana ||trim(document.consultaDireccionesForm.pais.value)=="") && 
					(trim(document.consultaDireccionesForm.poblacion.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)!=idEspana && trim(document.consultaDireccionesForm.pais.value)!="") && 
					(trim(document.consultaDireccionesForm.poblacionExt.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((document.consultaDireccionesForm.preferenteFax.checked) && 
					 (trim(document.consultaDireccionesForm.fax1.value)=="")) {
					 
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				// jbd 04/06/2009 opcion sms
				if((document.consultaDireccionesForm.preferenteSms.checked) && 
						 (trim(document.consultaDireccionesForm.movil.value)=="")) {
						 
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 fin();
						 return false;
					}
	         

			if(checkPostal) {
 				if((trim(document.consultaDireccionesForm.domicilio.value)=="") ||
					((trim(document.consultaDireccionesForm.pais.value)==idEspana) && (trim(document.consultaDireccionesForm.provincia.value)=="")) ||
					((trim(document.consultaDireccionesForm.pais.value)==idEspana) && (trim(document.consultaDireccionesForm.poblacion.value)=="")) ||
					(trim(document.consultaDireccionesForm.codigoPostal.value)=="")) {
				
					var mensaje = "<siga:Idioma key="messages.obligatorioPostal.error"/>";
					alert(mensaje);
					fin();
					return false;
				}
			}else{
				
				//Cuando no es obligatoria la direcci�n postal pero se introduce.
				var Primary = document.consultaDireccionesForm.codigoPostal.value;
				if(Primary != null && Primary != "" && (Primary.length<5)){
					 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}else{
					//Si es vacio o espa�a se comprueba que sea un codigo postal v�lido, sino se admite cualquier valor
					if(trim(document.consultaDireccionesForm.pais.value)==idEspana || trim(document.consultaDireccionesForm.pais.value)==""){
						var idProvincia	= Primary.substring(0,2);
						//Comprobamos que exista los dos primeros d�gitos del c.p con alg�n elemento de la lista de provincia
						if (jQuery("#provincia").val() != idProvincia){
							if (!jQuery("#provincia").find("option[value='"+idProvincia+"']").exists()){
								var mensaje = "<siga:Idioma key="censo.datosDireccion.noSeEncuentraProvincia"/>";
				 				alert (mensaje+document.consultaDireccionesForm.codigoPostal.value);
			 				 	fin();
							 	return false;
							} 
						} 
					}
				}
				
			}


			////////////////////////////////////

			<%if (!bOcultarHistorico) {%>
				var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
			<%} else {%>
					var datos = new Array();
					datos[0] = 1;
					datos[1] = "";
			<%}%>

			if (datos == null){ 
				fin();
				return false;
			}
			
			if (datos[0] == 1) { // Boton Guardar
				document.consultaDireccionesForm.motivo.value = datos[1];
				<%if (modo.equals("editar")) {%>
					document.consultaDireccionesForm.modo.value = "guardarInsertarHistorico";
				<%}%>	
				document.consultaDireccionesForm.target = "submitArea";
				document.consultaDireccionesForm.idProvinciaHidden.value=jQuery("#provincia").val();
				if(jQuery("#otraProvinciaCheck").is(':checked') && jQuery("#otraProvinciaCheck").is(':visible')){
					document.forms[0].otraProvincia.value = "1";
				} else{
					document.forms[0].otraProvincia.value= "0";
				}
				document.consultaDireccionesForm.submit();
			}else{
				fin();
			}
		}

		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
           sub();
           document.consultaDireccionesForm.telefono1.value=eliminarBlancos(trim(document.consultaDireccionesForm.telefono1.value));
		   document.consultaDireccionesForm.telefono2.value=eliminarBlancos(trim(document.consultaDireccionesForm.telefono2.value));
			// Validamos los errores ///////////
			// Campos Tipo de Direccion obligatorio -> no se usa validacion Struts pq es multiple seleccion
			
			var oCheck = document.getElementsByName("checkTipoDireccion");
			var checkActivo=false;
			var datosTipoDir="";
			for(i=0; i<oCheck.length; i++){
			 	if (oCheck[i].checked){
				   checkActivo=true;
				}
			}
			if (!checkActivo){
			     var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.tipoDireccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}			
			
			//var long=document.consultaDireccionesForm.idTipoDireccion.options.length;
						
			var cont=0;
			var checkGuardia=false;
			var checkPostal=false;
			
			for(i=0; i<oCheck.length; i++) {  			
				if (oCheck[i].checked){				
					cont++;
					 var tipoDir = oCheck[i].value;
					 datosTipoDir=datosTipoDir+tipoDir+",";
					if(tipoDir == 6) {
						checkGuardia=true;
					}
					// si es de tipo correo, despacho o guia lo apunto
					if(tipoDir == 3 || tipoDir == 2	|| tipoDir == 5 || tipoDir == 8|| tipoDir == 9) {						
						checkPostal=true;
					}						
				}
			}

           document.consultaDireccionesForm.idTipoDireccionNew.value=datosTipoDir;
		  
		   
			// VALIDAMOS QUE SOLO HAYAN INTRODUCIDO EN EL COMBO DE TIPO DE DIRECCION DIRECCION DE GUARDIA.
			// SI ES ASI SOLO REALIZAMOS LA VALIDACION DEL CAMPO TELEFONO 1. EN CASO CONTRARIO SE REALIZAN
			// EL RESTO DE VALIDACIONES TAMBIEN NECESARIAS PARA LOS OTROS TIPOS DE DIRECCION
			
			
		    
			// RGG 01-03-2005 cambio de validacion
			for(i=0; i<oCheck.length; i++) {
					if (oCheck[i].checked) {  
						var tipoDir = oCheck[i].value;
						if(tipoDir == 6)
							if(trim(document.consultaDireccionesForm.telefono1.value)=="") {
								var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.telefono1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
								alert(mensaje);
								fin();
								return false;
							}							
						}
					}	
				
			//VALIDACIONES GENERALES A TODOS LOS TIPOS DE DIRECCION SELECCIONADOS
		  
		   if (!validateConsultaDireccionesForm(document.consultaDireccionesForm)){
					fin();
					return false;
			}
			
		 if((document.consultaDireccionesForm.preferenteMail.checked) && 
					 (trim(document.consultaDireccionesForm.correoElectronico.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
				 
					 return false;
				}
			if((document.consultaDireccionesForm.preferenteCorreo.checked) && 
					 (trim(document.consultaDireccionesForm.domicilio.value)=="")) {
					 
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
					 fin();
					 return false;
				}
				// RGG 25/04/2005
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") &&
					(trim(document.consultaDireccionesForm.codigoPostal.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				var value = trim(document.consultaDireccionesForm.codigoPostal.value);
				//Si es obligatorio el codigo postal e introduce menos de 5
				
				if((jQuery("#cpConAsterisco").attr("class") != "ocultar")  &&
					(value.length<<%=tam_max_codigo_postal%>)) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				
				//Si no es obligatorio pero introduce campo tampoco puede ser menos de 5. 
				if((jQuery("#cpConAsterisco").attr("class") == "ocultar") &&
						((value.length>=<%=tam_min_codigo_postal%>)&&(value.length<<%=tam_max_codigo_postal%>))) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)==idEspana ||trim(document.consultaDireccionesForm.pais.value)=="" ) && 
					(trim(document.consultaDireccionesForm.provincia.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.provincia"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)==idEspana ||trim(document.consultaDireccionesForm.pais.value)=="") && 
					(trim(document.consultaDireccionesForm.poblacion.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				
				if((trim(document.consultaDireccionesForm.domicilio.value)!="") && (trim(document.consultaDireccionesForm.pais.value)!=idEspana && trim(document.consultaDireccionesForm.pais.value)!="") && 
					(trim(document.consultaDireccionesForm.poblacionExt.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				if((document.consultaDireccionesForm.preferenteFax.checked) && 
					 (trim(document.consultaDireccionesForm.fax1.value)=="")) {
					 
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
				// jbd 04/06/2009 opcion sms
				if((document.consultaDireccionesForm.preferenteSms.checked) && 
						 (trim(document.consultaDireccionesForm.movil.value)=="")) {
						 
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 fin();
						 return false;
					}
	         
			
			if(checkPostal) {	
				// valido que sea una direccion postal
				if((trim(document.consultaDireccionesForm.domicilio.value)=="") ||
					((trim(document.consultaDireccionesForm.pais.value)==idEspana) && (trim(document.consultaDireccionesForm.provincia.value)=="")) ||
					((trim(document.consultaDireccionesForm.pais.value)==idEspana) && (trim(document.consultaDireccionesForm.poblacion.value)=="")) ||
					(trim(document.consultaDireccionesForm.codigoPostal.value)=="")) {
					
				
					var mensaje = "<siga:Idioma key="messages.obligatorioPostal.error"/>";
					alert(mensaje);
					fin();
					return false;
				}
			}else{
				//Cuando no es obligatorio la direcci�n postal pero se introduce.
				var Primary = document.consultaDireccionesForm.codigoPostal.value;
				if(Primary != null && Primary != "" && (Primary.length<5)){
					var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}else{
					//Si es vacio o espa�a se comprueba que sea un codigo postal v�lido, sino se admite cualquier valor
					if(trim(document.consultaDireccionesForm.pais.value)==idEspana || trim(document.consultaDireccionesForm.pais.value)==""){
						var idProvincia	= Primary.substring(0,2);
						//Comprobamos que exista los dos primeros d�gitos del c.p con alg�n elemento de la lista de provincia
						if (jQuery("#provincia").val() != idProvincia){
							if (!jQuery("#provincia").find("option[value='"+idProvincia+"']").exists()){
								var mensaje = "<siga:Idioma key="censo.datosDireccion.noSeEncuentraProvincia"/>";
				 				alert (mensaje+document.consultaDireccionesForm.codigoPostal.value);
			 				 	fin();
							 	return false;
							} 
						} 
					}
				}
				
				
			}	


			////////////////////////////////////

			<%if (!bOcultarHistorico) {%>
					var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
					window.top.focus();
			<%} else {%>
					var datos = new Array();
					datos[0] = 1;
					datos[1] = "";
			<%}%>

			if (datos == null){ 
				fin();
				return false;
			}
		
			if (datos[0] == 1) { // Boton Guardar
				document.consultaDireccionesForm.motivo.value = datos[1];
				<%if (modo.equals("editar")) {%>
				document.consultaDireccionesForm.modo.value = "modificar";
				<%} else {%>
				document.consultaDireccionesForm.modo.value = "insertar";
				<%}%>
				document.consultaDireccionesForm.target = "submitArea";
				document.consultaDireccionesForm.idProvinciaHidden.value=jQuery("#provincia").val();
				if(jQuery("#otraProvinciaCheck").is(':checked') && jQuery("#otraProvinciaCheck").is(':visible')){
					document.forms[0].otraProvincia.value = "1";
				} else{
					document.forms[0].otraProvincia.value= "0";
				}
				document.consultaDireccionesForm.submit();
			} else {
				fin();
			}
	}

	//Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
	function rellenarCampos() {
		document.getElementById("provincia").onchange();
	}

	function createProvince() {
		
		if (trim(document.consultaDireccionesForm.pais.value) == idEspana || trim(document.consultaDireccionesForm.pais.value) == "" ) {
			
			var Primary = document.consultaDireccionesForm.codigoPostal.value;
			if ((Primary == null) || (Primary == "")) {
				//Inicializamos todo
				jQuery("#provincia").val(jQuery("#provincia option:first").val());
				jQuery("#provinciaText").val("");
				jQuery("#provincia").change();
				jQuery("#otraProvinciaCheck").removeAttr('checked');
				jQuery("#provinciaDiv").hide();
			    jQuery("#provinciaText").show();
				return;
			} 
			if(Primary.length<5){
				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}else{
				var idProvincia	= Primary.substring(0,2);
				//Comprobamos que exista los dos primeros d�gitos del c.p con alg�n elemento de la lista de provincia
				if (jQuery("#provincia").val() != idProvincia){
					if (jQuery("#provincia").find("option[value='"+idProvincia+"']").exists()){
						jQuery("#provincia").val(idProvincia);
						jQuery("#provinciaText").val( jQuery("#provincia option:selected").text());
						document.consultaDireccionesForm.idProvinciaHidden.value=idProvincia;
						jQuery("#poblacion").val(jQuery("#poblacion option:first").val());
					 	jQuery("#provincia").change();
					 	
						jQuery("#otraProvinciaCheck").removeAttr('checked');
						jQuery("#provinciaDiv").hide();
					    jQuery("#provinciaText").show();
						
					 	
					} else {
						var mensaje = "<siga:Idioma key="censo.datosDireccion.noSeEncuentraProvincia"/>";
		 				alert (mensaje+document.consultaDireccionesForm.codigoPostal.value);
						//Inicializamos todo
						jQuery("#provincia").val(jQuery("#provincia option:first").val());
						jQuery("#provinciaText").val("");
						jQuery("#provincia").change();
	 				 	fin();
					 	return false;
					}
				} 
			}
		
		}
	}   
	
	function otraProvinciaFuction(valor){
		createProvince();
		if(valor.checked ) {
		   //Si est� seleccionado
		  	jQuery("#provinciaDiv").show();
		    jQuery("#provinciaText").hide();
		}else{
			jQuery("#provinciaDiv").hide();
		    jQuery("#provinciaText").show();
		}
	}
	</script>
</head>

<body onload="comprobarAsterico();selPaisInicio();">

	<!-- TITULO -->
	<table class=titulitosDatos cellspacing="0">
		<tr>
			<td id="titulitos" class="titulosPeq"><siga:Idioma
					key="censo.consultaDirecciones.literal.titulo1" /> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>
				&nbsp;&nbsp; <%
 	if ((numero != null) && (!numero.equalsIgnoreCase(""))) {
 %>
				<siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%
					} else {
				%> <siga:Idioma key="censo.fichaCliente.literal.NoColegiado" />
				<%
					}
				%></td>
		</tr>
	</table>


	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<div id="camposRegistro" class="posicionModalGrande" align="center">

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->

		<html:form action="/CEN_ConsultasDirecciones" method="POST"
			target="resultado">
			<html:hidden property="modo" value="cerrar" />
			<input type="hidden" id="idTipoDireccionNew"
				name="idTipoDireccionNew" value="">
			<input type="hidden" id="modificarPreferencias"
				name="modificarPreferencias" value="">
			<input type="hidden" id="control" name="control" value="">
			<input type="hidden" id="modificarDireccionesCensoWeb"
				name="modificarDireccionesCensoWeb" value="">
				<input type="hidden" id="modificarDireccionesTraspasoOJ"
				name="modificarDireccionesTraspasoOJ" value="">
				<input type="hidden" id="modificarDireccionesFacturacion"
				name="modificarDireccionesFacturacion" value="">
			<input type="hidden" id="idDireccionesPreferentes"
				name="idDireccionesPreferentes" value="" />
			<input type="hidden" id="idDireccionesCensoWeb"
				name="idDireccionesCensoWeb" value="" />
			<input type="hidden" id="idDireccionesTraspasoOJ"
				name="idDireccionesTraspasoOJ" value="" />
			<input type="hidden" id="idDireccionesFacturacion"
				name="idDireccionesFacturacion" value="" />	
			<html:hidden property="vieneDe" styleId="vieneDe" />
			<html:hidden styleId="tipoAcceso"  property="tipoAcceso" />
			<html:hidden styleId="idProvinciaHidden"  property="idProvinciaHidden" />
			<input type="hidden" id="otraProvincia" name="otraProvincia" value="">
			

			<%
				if (editarCampos) {
			%>
			<html:hidden property="idPersona" styleId="idPersona"
				value="<%=idPersona%>" />
			<html:hidden property="idDireccion" styleId="idDireccion"
				value="<%=idDireccion%>" />
			<html:hidden property="idInstitucion" styleId="idInstitucion"
				value="<%=idInstitucion%>" />
			<html:hidden property="idTipoDireccionAntes"
				styleId="idTipoDireccionAntes" value="<%=idTipoDireccion%>" />
			<html:hidden property="motivo" styleId="motivo" value="" />
			<%
				}
			%>

			<table class="tablaCentralCamposGrande" align="center" border="0">
			<tr>
			<td>
				<siga:ConjCampos leyenda="censo.datosDireccion.literal.tipoDireccion">
					<table class="tablaCampos" align="center">

						<!-- Tipos de direccion -->
						<tr>
						<%
							String valorCheck = "";
							if (vTipos == null) vTipos = new Vector();
							for (int i = 1; i <= vTipos.size(); i++) {
								CenTipoDireccionBean recurso = (CenTipoDireccionBean) vTipos.get(i - 1);
								String descripcion = (String) recurso.getDescripcion();
								Integer idTipoDireccion1 = (Integer) recurso.getIdTipoDireccion();
								String activarCheck = "";

								if (modo.equals("editar") || modo.equals("ver")) {
									valorCheck = (String) hTiposDir.get(idTipoDireccion1);
									if (valorCheck.equals("S")) {
										activarCheck = "checked";
									}
								}
								
								if(! tipoCliente.equals("1") || 
								   ! (idTipoDireccion1 == ClsConstants.TIPO_DIRECCION_CENSOWEB || idTipoDireccion1 == ClsConstants.TIPO_DIRECCION_TRASPASO_OJ))
								{
									//NO SE PINTA EL CHECK DE TIPO DIRECCION CENSO WEB ni de traspaso a organos judiciales PARA NO COLEGIADOS TIPO PERSONAL (1)
						%> 
									<td valign="middle" width="1%">
										<input type=checkbox name="checkTipoDireccion" id="checkTipoDireccion<%=idTipoDireccion1%>" value="<%=idTipoDireccion1%>"
											<%=activarCheck%> onclick="comprobarAsterico()"
											<%=desactivarCheckTipos%>
									</td>
									<td class="labelText" valign="middle">
										<label for="checkTipoDireccion<%=idTipoDireccion1%>"><%=UtilidadesString.mostrarDatoJSP(descripcion)%></label>
									</td>
						<%
								}
							}
						%>
						</tr>
					</table>
				</siga:ConjCampos>
					
				<siga:ConjCampos leyenda="censo.consultaDirecciones.cabecera">
					<table class="tablaCampos" align="center">
					
						<!-- Fechas de control y colegio de origen -->
						<tr>
						<%	if (fechaBaja.equals("")) { %>
							<td class="labelText"">
								<siga:Idioma key="censo.datosDireccion.literal.fechaModificacion" />
							</td>
							<td class="labelText">
								<%=fechaModificacion%>
							</td>
						<%	} else { %>
							<td class="labelText">
								<siga:Idioma key="censo.consultaDatos.literal.fechaBaja" />
							</td>
							<td class="labelText">
								<%=fechaBaja%>
							</td>
						<%	} %>
						</tr>
						
						<%	if (!colegioOrigen.equals("")) { %>
						<tr>
							<td class="labelText">
								<siga:Idioma key="envios.definir.literal.institucionOrigen" />
							</td>
							<td class="labelText">
								<%=colegioOrigen%>
							</td>
						</tr>
						<%	} %>
						
						<!-- Domicilio -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.direccion" />
								<div style="display:inline" id="direccionSinAsterisco">&nbsp;</div>
								<div style="display:inline" id="direccionConAsterisco">(*)</div>
							</td>
							<td colspan="3">
								<html:textarea name="consultaDireccionesForm" property="domicilio" styleId="domicilio"
									onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)"
									style="overflow-y:auto; overflow-x:hidden; width:95%; height:50px; resize:none;"
									value="<%=domicilio%>" styleClass="<%=clase%>"
									readOnly="<%=desactivado%>"></html:textarea>
							</td>
						</tr>

						<!-- Pais y Otra provincia -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.pais2" />
								<div style="display:inline" id="paisSinAsterisco">&nbsp;</div>
								<div style="display:inline" id="paisConAsterisco">(*)</div>
							</td>
							<td>
							<%	if (editarCampos) { %> 
								<siga:ComboBD nombre="pais" tipo="pais"
									clase="boxCombo" obligatorio="false"
									elementoSel="<%=idPais%>" accion="selPais(this.value);" /> 
							<%	} else { %>
								<html:hidden property="pais" styleId="pais" value='<%=ididPais%>'></html:hidden>
								<html:text name="consultaDireccionesForm" property="pais2" styleId="pais2"
									value='<%=pais%>' size="40" styleClass="<%=clase%>"
									readOnly="<%=desactivado%>"></html:text>
							<%	} %>
							</td>
							
							<td class="labelText" nowrap="nowrap" id="tdOtraProvincia">
								<siga:Idioma key="censo.datosDireccion.literal.otraProvincia" />
								
							<%	if (editarCampos) { %>
							<%		if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")) { %>
								<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck" checked="checked"  onclick="otraProvinciaFuction(this);"> &nbsp;
							<%		} else { %>
								<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck" onclick="otraProvinciaFuction(this);"> &nbsp;
							<%		} %>
							<%	} else {%>
							<%		if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")) { %>
								<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck" checked="checked" disabled="disabled" onclick="otraProvinciaFuction(this);"> &nbsp;
							<%		} else { %>
								<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck" disabled="disabled" onclick="otraProvinciaFuction(this);"> &nbsp;
							<%		} %>
							<%	} %> 
							</td>
						</tr>

						<!-- Codigo postal y Provincia -->
						<tr>
							<td class="labelText" nowrap>
								<siga:Idioma key="censo.datosDireccion.literal.cp"/>
								<div style="display:inline" id="cpSinAsterisco">&nbsp;</div>
								<div style="display:inline" id="cpConAsterisco">(*)</div>
							</td>
							<td >
							<% if (editarCampos) { %>
								<html:text name="consultaDireccionesForm" styleId="codigoPostal"
									property="codigoPostal" value="<%=codigoPostal%>"
									maxlength="5" size="5" styleClass="<%=clase%>" 
									onChange="createProvince()" /> 
							<% } else { %> 
									<html:text name="consultaDireccionesForm" styleId="codigoPostal"
									property="codigoPostal" value="<%=codigoPostal%>"
									maxlength="5" size="5" styleClass="<%=clase%>"
									readOnly="<%=desactivado%>" /> 
							<% } %>
							</td>
							
							<td id="provinciaLabel" class="labelText" nowrap="nowrap">
								<siga:Idioma key="censo.datosDireccion.literal.provincia" />
							</td>
							
							<td class="labelText" nowrap="nowrap">
							<%
								if (editarCampos) {
							%>
								<div id="provinciaDiv" style="display:none;">
									<siga:ComboBD nombre="provincia" tipo="provincia" clase="boxCombo" obligatorio="false" elementoSel="<%=idProvincia%>" accion="Hijo:poblacion" />
								</div> 
							<%
								}
							%>
								<input id="provinciaText" class="boxConsulta" type="text" value="<%=provincia%>" readonly="readonly" tabindex="-1" style="width: 200px" />
							</td>
						</tr>
						
						<!-- Poblacion -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.poblacion" />
								<div style="display:inline" id="poblacionSinAsterisco">&nbsp;</div>
								<div style="display:inline" id="poblacionConAsterisco">(*)</div>
							</td>
							<td id="poblacionEspanola">
							<%
								if (editarCampos) {
							%> 
								<siga:ComboBD nombre="poblacion" tipo="poblacion" clase="boxCombo" elementoSel="<%=idPoblacion%>" hijo="t" />
							<%
								} else {
							%> 
								<html:text property="poblacion" value="<%=poblacion%>" styleId="poblacion" size="40" styleClass="<%=clase%>" readOnly="<%=desactivado%>"/>
							<%
								}
							%>
							</td>
							<td class="ocultar" id="poblacionExtranjera">
								<html:text name="consultaDireccionesForm" property="poblacionExt" styleId="poblacionExt"
										value='<%=poblacionExt%>' size="30" styleClass="<%=clase%>"
										readOnly="<%=desactivado%>">
								</html:text>
							</td>
						</tr>
						
						<!-- Telefonos -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.telefono1" />
								<div style="display:inline" id="telefonoSinAsterisco">&nbsp;</div>
								<div style="display:inline" id="telefonoConAsterisco">(*)</div>
							</td>
							<td>
								<html:text name="consultaDireccionesForm" styleId="telefono1"
									property="telefono1" value="<%=telefono1%>" maxlength="20"
									size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							</td>

							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.telefono2" />&nbsp;
							</td>
							<td>
								<html:text name="consultaDireccionesForm" styleId="telefono2"
									property="telefono2" value="<%=telefono2%>" maxlength="20"
									size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							</td>
						</tr>

						<!-- Movil -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.movil" />&nbsp;
							</td>
							<td>
								<html:text name="consultaDireccionesForm" styleId="movil"
									property="movil" value="<%=movil%>" maxlength="20" size="10"
									styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							</td>
						</tr>

						<!-- Faxes -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.fax1" />&nbsp;
							</td>
							<td>
								<html:text name="consultaDireccionesForm" styleId="fax1"
									property="fax1" value="<%=fax1%>" maxlength="20" size="10"
									styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							</td>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.fax2" />&nbsp;
							</td>
							<td>
								<html:text name="consultaDireccionesForm" styleId="fax2"
									property="fax2" value="<%=fax2%>" maxlength="20" size="10"
									styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							</td>
						</tr>

						<!-- Email y Pagina web -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.correo" />&nbsp;
							</td>
							<td>
							<%
								if (modo.equals("editar") || modo.equals("duplicar") || mail.equalsIgnoreCase("")) {
							%>
								<html:text name="consultaDireccionesForm" styleId="correoElectronico"
									property="correoElectronico" value="<%=mail%>"
									maxlength="100" size="50" styleClass="<%=clase%>"
									readOnly="<%=desactivado%>">
								</html:text>
							<%
								} else {
							%>
								<a href="mailto:<%=mail%>">
									<html:text
										name="consultaDireccionesForm" styleId="correoElectronico"
										style="cursor:hand;color:blue;" property="correoElectronico"
										value="<%=mail%>" maxlength="100" size="50"
										styleClass="<%=clase%>" readOnly="<%=desactivado%>">
									</html:text>
								</a>
							<%
								}
							%>
							</td>
							
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.paginaWeb" />&nbsp;
							</td>
							<td>
							<%
								if (modo.equals("editar") || modo.equals("duplicar") || enlaceWEB.equalsIgnoreCase("")) {
							%>
								<html:text name="consultaDireccionesForm" styleId="paginaWeb"
									property="paginaWeb" value="<%=paginaWEB%>" maxlength="100"
									size="25" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
								</html:text>
							<%
								} else {
							%>
								<a href="<%=enlaceWEB%>" target="_blank">
									<html:text name="consultaDireccionesForm" styleId="paginaWeb"
										style="cursor:hand;color:blue;" property="paginaWeb"
										value="<%=paginaWEB%>" maxlength="100" size="25"
										styleClass="<%=clase%>" readOnly="<%=desactivado%>">
									</html:text>
								</a>
							<%
								}
							%>
							</td>
						</tr>

						<!-- Preferencias -->
						<tr>
							<td class="labelText">
								<siga:Idioma key="censo.datosDireccion.literal.preferente" />
							</td>
							<td class="labelText">
								<siga:Idioma key="censo.preferente.mail" />
								<input type="checkbox" name="preferenteMail" id="preferenteMail"
								<%if (desactivado)		out.print("disabled");%>
								<%if (preferenteMail)	out.println("checked");%>>&nbsp;&nbsp;&nbsp;
								
								<siga:Idioma key="censo.preferente.correo" />
								<input type="checkbox" name="preferenteCorreo" id="preferenteCorreo"
								<%if (desactivado)		out.print("disabled");%>
								<%if (preferenteCorreo)	out.print(" checked");%>>&nbsp;&nbsp;&nbsp;
								
								<siga:Idioma key="censo.preferente.fax" />
								<input type="checkbox" name="preferenteFax" id="preferenteFax"
								<%if (desactivado)		out.print("disabled");%>
								<%if (preferenteFax)	out.print(" checked");%>>&nbsp;&nbsp;&nbsp;
								
								<siga:Idioma key="censo.preferente.sms" />
								<input type="checkbox" name="preferenteSms" id="preferenteSms"
								<%if (desactivado)		out.print("disabled");%>
								<%if (preferenteSms)	out.print(" checked");%>>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td>
			</tr>
			</table>
		</html:form>
		<script>	
	<%if (editarCampos) {%>
		rellenarCampos();	
	<%}%>
			
		</script>

		<!-- FIN: CAMPOS -->

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>'
			modal="G" />
		<!-- FIN: BOTONES REGISTRO -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
