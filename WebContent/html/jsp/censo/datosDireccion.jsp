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
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");
	boolean bOcultarHistorico = usr.getOcultarHistorico();

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;

	boolean desactivado = false;
	String clase = "box";
	String botones = "C,Y,R";

	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;

	String nombreUsu = (String) request.getAttribute("nombrePersona");
	String numero = (String) request.getAttribute("numero");
	Vector vTipos = (Vector) request.getAttribute("vTipos");
	Hashtable hTiposDir = (Hashtable) request
			.getAttribute("TipoDirecciones");

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
	String ididPais = "";

	String modo = (String) request.getAttribute("modoConsulta");
	if (modo != null && modo.equals("editar")) {
		botones += ",GAH";
	}
	if (modo.equals("ver") || modo.equals("editar")) {
		htData = (Hashtable) request.getSession().getAttribute(
				"DATABACKUP");
		if (htData != null) {
			domicilio = String.valueOf(htData
					.get(CenDireccionesBean.C_DOMICILIO));
			codigoPostal = String.valueOf(htData
					.get(CenDireccionesBean.C_CODIGOPOSTAL));

			idTipoDireccion = String.valueOf(htData
					.get(CenTipoDireccionBean.C_DESCRIPCION));
			provincia = String.valueOf(htData.get("PROVINCIA"));
			pais = String.valueOf(htData.get("PAIS"));
			poblacion = String.valueOf(htData.get("POBLACION"));
			poblacionExt = String.valueOf(htData
					.get("POBLACIONEXTRANJERA"));
			telefono1 = String.valueOf(htData
					.get(CenDireccionesBean.C_TELEFONO1));
			telefono2 = String.valueOf(htData
					.get(CenDireccionesBean.C_TELEFONO2));
			movil = String.valueOf(htData
					.get(CenDireccionesBean.C_MOVIL));
			fax1 = String
					.valueOf(htData.get(CenDireccionesBean.C_FAX1));
			fax2 = String
					.valueOf(htData.get(CenDireccionesBean.C_FAX2));
			mail = String.valueOf(htData
					.get(CenDireccionesBean.C_CORREOELECTRONICO));
			paginaWEB = String.valueOf(htData
					.get(CenDireccionesBean.C_PAGINAWEB));
			idDireccion = String.valueOf(htData
					.get(CenDireccionesBean.C_IDDIRECCION));
			fechaModificacion = String.valueOf(htData
					.get(CenDireccionesBean.C_FECHAMODIFICACION));
			if (fechaModificacion != null)
				fechaModificacion = UtilidadesString
						.mostrarDatoJSP(GstDate.getFormatedDateShort(
								"", fechaModificacion));
			else
				fechaModificacion = "";

			fechaBaja = String.valueOf(htData
					.get(CenDireccionesBean.C_FECHABAJA));
			if ((fechaBaja != null) && !fechaBaja.equals(""))
				fechaBaja = UtilidadesString.mostrarDatoJSP(GstDate
						.getFormatedDateShort("", fechaBaja));
			else
				fechaBaja = "";

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

			Vector vTiposDirecciones = (Vector) htData
					.get(CenTipoDireccionBean.C_IDTIPODIRECCION);
			if (vTiposDirecciones != null) {
				for (int i = 0; i < vTiposDirecciones.size(); i++) {
					CenDireccionTipoDireccionBean tipoDirBean = (CenDireccionTipoDireccionBean) vTiposDirecciones
							.get(i);
					if (tipoDirBean != null) {
						idTipoDireccionArrayList.add(String
								.valueOf(tipoDirBean
										.getIdTipoDireccion()));
					}
				}
			}

			if (modo.equals("editar")) {
				desactivado = false;
				editarCampos = true;
			} else { // Ver
				desactivado = true;
				clase = "boxConsulta";
				desactivarCheckTipos = "disabled";
			}
			idPersona = String.valueOf((Long) request
					.getAttribute("idPersona"));
			idInstitucion = String.valueOf(htData
					.get(CenDireccionesBean.C_IDINSTITUCION));

			idProvincia.add(String.valueOf(htData
					.get(CenDireccionesBean.C_IDPROVINCIA)));
			sIdprovincia = String.valueOf(htData.get(CenDireccionesBean.C_IDPROVINCIA));
			idPoblacion.add(String.valueOf(htData
					.get(CenDireccionesBean.C_IDPOBLACION)));
			ididPais = (htData.get(CenDireccionesBean.C_IDPAIS) == null || String
					.valueOf(
							htData.get(CenDireccionesBean.C_IDPAIS))
					.equals(""))
					? ClsConstants.ID_PAIS_ESPANA
					: String.valueOf(htData
							.get(CenDireccionesBean.C_IDPAIS));
			idPais.add(ididPais);
		}
	} else {
		if (modo.equals("nuevo")) {
			idPais.add("");
			editarCampos = true;
			desactivado = false;
			idPersona = String.valueOf((Long) request
					.getAttribute("idPersona"));
			idInstitucion = String.valueOf((Integer) request
					.getAttribute("idInstitucion"));
		}
	}

	//Se ha añadido para crear bien la dirección del enlace la pagina web
	String EnlaceWEb = "";
	String quitar;
	String lista = "";

	if (!paginaWEB.equals("") && paginaWEB.length() >= 7) {

		lista = paginaWEB.substring(0, 7);
		if (lista.equalsIgnoreCase("http://")) {
			EnlaceWEb = paginaWEB;
		}

		if (!lista.equalsIgnoreCase("http://")
				&& (!lista.equalsIgnoreCase("http:\\\\"))) {
			EnlaceWEb = "http://" + paginaWEB;
		}

		if (lista.equalsIgnoreCase("http:\\\\")) {
			quitar = paginaWEB.substring(7);
			EnlaceWEb = "http://" + quitar;
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
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
		
		jQuery(function(){
			jQuery("#pais").on("change", function(){
				selPais(jQuery(this).val());
			});
			jQuery("#provincia").on("change", function(){
				if (trim(document.consultaDireccionesForm.pais.value) == idEspana) {
					jQuery("#codigoPostal").val(jQuery(this).val() + "000");
				}
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
			}						
		}			
	
	    function validateConsultaDireccionesFormAux(form) {                                                                   
	       	return validateMaxLength(form) && validateMask(form); 
	    }
	    
	    function actualizar(){	     
		    document.forms[0].modificarPreferencias.value="1";
		    document.forms[0].modificarDireccionesCensoWeb.value="0";
		    document.forms[0].control.value="0";   
		    document.forms[0].submit();
			document.forms[0].modificarPreferencias.value="0";
	    }

	    function actualizarcenso(){
	    	document.forms[0].modificarDireccionesCensoWeb.value="1";
	    	document.forms[0].modificarPreferencias.value="1";	 
	    	document.forms[0].control.value="1";   	
		    document.forms[0].submit();
			document.forms[0].modificarDireccionesCensoWeb.value="0";
			document.forms[0].modificarPreferencias.value="0";
	    }	   
	    
	    function selPais(valor) {                                                                   
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
			   	jQuery("#provincia").attr("disabled","disabled");
			   	//aalg: se quita la marca de obligatoriedad
			   	document.getElementById("provinciaSinAsterisco").className="labelText";
				document.getElementById("provinciaConAsterisco").className="ocultar";
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				jQuery("#provincia").removeAttr("disabled");
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
				//aalg: se restaura la marca de obligatoriedad si es pertinente
				comprobarTelefonoAsterico();
	       }
	    }
		
		function selPaisInicio() {
			var valor = document.getElementById("pais").value;
			if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
			   	//aalg: se quita la marca de obligatoriedad
			   	document.getElementById("provinciaSinAsterisco").className="labelText";
				document.getElementById("provinciaConAsterisco").className="ocultar";
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
				//aalg: se restaura la marca de obligatoriedad si es pertinente
				comprobarTelefonoAsterico();
	       }
		}
		
		function comprobarTelefonoAsterico() {
			var checkGuardia = false;
			var checkPostal = false;
			var oCheck = document.getElementsByName("checkTipoDireccion");
			
			for(i=0; i<oCheck.length; i++){			 
				if (oCheck[i].checked){
					  var tipoDir = oCheck[i].value;
					  if (tipoDir==6){
			            checkGuardia = true;
			          }
			          if (tipoDir==3||tipoDir==2||tipoDir==5||tipoDir==8){					  
					    checkPostal=true;						
			          }
				}
			}		
					
			
			if(checkGuardia) {
				document.getElementById("telefonoSinAsterisco").className="ocultar";
				document.getElementById("telefonoConAsterisco").className="labelText";
			}
			else {
				document.getElementById("telefonoSinAsterisco").className="labelText";
				document.getElementById("telefonoConAsterisco").className="ocultar";
	    	}
			if(checkPostal) {
				document.getElementById("direccionSinAsterisco").className="ocultar";
				document.getElementById("direccionConAsterisco").className="labelText";
				document.getElementById("cpSinAsterisco").className="ocultar";
				document.getElementById("cpConAsterisco").className="labelText";
				document.getElementById("provinciaSinAsterisco").className="ocultar";
				document.getElementById("provinciaConAsterisco").className="labelText";
				document.getElementById("poblacionSinAsterisco").className="ocultar";
				document.getElementById("poblacionConAsterisco").className="labelText";
				document.getElementById("paisSinAsterisco").className="ocultar";
				document.getElementById("paisConAsterisco").className="labelText";
			}
			else {
				document.getElementById("direccionSinAsterisco").className="labelText";
				document.getElementById("direccionConAsterisco").className="ocultar";
				document.getElementById("cpSinAsterisco").className="labelText";
				document.getElementById("cpConAsterisco").className="ocultar";
				document.getElementById("provinciaSinAsterisco").className="labelText";
				document.getElementById("provinciaConAsterisco").className="ocultar";
				document.getElementById("poblacionSinAsterisco").className="labelText";
				document.getElementById("poblacionConAsterisco").className="ocultar";
				document.getElementById("paisSinAsterisco").className="labelText";
				document.getElementById("paisConAsterisco").className="ocultar";
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
						|| tipoDir == 5) {
						
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
					if(tipoDir == 3 || tipoDir == 2	|| tipoDir == 5) {						
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
				document.consultaDireccionesForm.submit();
			} else {
				fin();
			}
	}

	//Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
	function rellenarCampos() {
		//document.getElementById("provincia").onchange();
	}

	function createProvince() {
		if (trim(document.consultaDireccionesForm.pais.value) == idEspana) {
			var Primary = document.consultaDireccionesForm.codigoPostal.value;
			if ((Primary == null) || (Primary == 0)) {
				return;
			}
			while(Primary.length<5){
				Primary="0"+Primary;
			}	  
			var idProvincia	= Primary.substring(0,2);
			if (jQuery("#provincia").val() != idProvincia){
				if (jQuery("#provincia").find("option[value='"+idProvincia+"']").exists()){
					jQuery("#provincia").val(idProvincia);
					jQuery("#provincia").change();
				} else {
					alert("No se ha encontrado provincia para el código postal: " + document.consultaDireccionesForm.codigoPostal.value);
				}
			} 
		}
	}       
	</script>
</head>

<body onload="comprobarTelefonoAsterico();selPaisInicio();">

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma
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
			<input type="hidden" id="idDireccionesPreferentes"
				name="idDireccionesPreferentes" value="" />
			<input type="hidden" id="idDireccionesCensoWeb"
				name="idDireccionesCensoWeb" value="" />
			<html:hidden property="vieneDe" styleId="vieneDe" />

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
						<siga:ConjCampos leyenda="censo.consultaDirecciones.cabecera">
							<table class="tablaCampos" align="center" border="0">
								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.tipoDireccion" />&nbsp(*)
									</td>
									<td class="labelText" colspan="2">
										<div style='height: 200px; width: 250px; overflow-x: auto; overflow-y: auto'>
											<table align="left" border="0" width="100%">
												<tr>
													<td class="labelText" align="left">
														<%
															String valorCheck = "";

																	if ((vTipos != null) && (vTipos.size() > 0)) {
																		for (int i = 1; i <= vTipos.size(); i++) {
																			String activarCheck = "";
																			CenTipoDireccionBean recurso = (CenTipoDireccionBean) vTipos
																					.get(i - 1);
																			Integer idTipoDireccion1 = (Integer) recurso
																					.getIdTipoDireccion();
																			String descripcion = (String) recurso
																					.getDescripcion();

																			if (modo.equals("editar") || modo.equals("ver")) {
																				valorCheck = (String) hTiposDir.get(recurso
																						.getIdTipoDireccion());
																				if (valorCheck.equals("S")) {
																					activarCheck = "checked";
																				}
																			}
														%> 
														<input type=checkbox name="checkTipoDireccion" id="checkTipoDireccion" value="<%=idTipoDireccion1%>"
															<%=activarCheck%> onclick="comprobarTelefonoAsterico()"
															<%=desactivarCheckTipos%> /> 
														<%=UtilidadesString.mostrarDatoJSP(descripcion)%>
														<br> <%
 	}
 			}
 %>

													</td>
													<td class="labelText"/>
												</tr>
											</table>
										</div>
									</td>
									<td valign="top">
										<table>
											<tr>
												<td class="labelText">
													<p align="right">
														<siga:Idioma key="censo.datosDireccion.literal.fechaModificacion" />
														&nbsp&nbsp<%=fechaModificacion%>
													</p>
												</td>
											</tr>
											<%
												if (!fechaBaja.equals("")) {
											%>
											<tr>
												<td class="labelText">
													<p align="right">
														<siga:Idioma key="censo.consultaDatos.literal.fechaBaja" />
														&nbsp;&nbsp;<%=fechaBaja%>
													</p>
												</td>
											</tr>
											<%
												}
											%>
										</table>
									</td>
								</tr>

								<tr>
									<td class="labelText" width="180px" id="direccionSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.direccion" />&nbsp
									</td>
									<td class="ocultar" width="180px" id="direccionConAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.direccion" />&nbsp(*)
									</td>
									<td>
										<html:textarea name="consultaDireccionesForm" property="domicilio" styleId="domicilio"
											onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)"
											style="overflow-y:auto; overflow-x:hidden; width:350px; height:50px; resize:none;"
											value="<%=domicilio%>" styleClass="<%=clase%>"
											readOnly="<%=desactivado%>"></html:textarea>
									</td>
									<td class="labelText" width="180px" id="cpSinAsterisco" nowrap>
										<siga:Idioma key="censo.datosDireccion.literal.cp" />&nbsp
									</td>
									<td class="ocultar" width="180px" id="cpConAsterisco" nowrap>
										<siga:Idioma key="censo.datosDireccion.literal.cp" />&nbsp(*)
									</td>
									<td>
										<%
											if (editarCampos) {
										%>
										<html:text name="consultaDireccionesForm" styleId="codigoPostal"
											property="codigoPostal" value="<%=codigoPostal%>"
											maxlength="5" size="5" styleClass="<%=clase%>"
											onChange="createProvince()"></html:text> 
										<%
 											} else {
 										%> 
 										<html:text name="consultaDireccionesForm" styleId="codigoPostal"
											property="codigoPostal" value="<%=codigoPostal%>"
											maxlength="5" size="5" styleClass="<%=clase%>"
											readOnly="<%=desactivado%>"></html:text> 
										<%
 											}
	 									%>
									</td>
								</tr>

								<tr>
									<td class="labelText" width="180px" id="paisSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.pais2" />&nbsp
									</td>
									<td class="ocultar" width="180px" id="paisConAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.pais2" />&nbsp
									</td>
									<td>
										<siga:Select id="pais" 
													queryParamId="idpais" 
													queryId="getPaises"
													disabled="<%=String.valueOf(!editarCampos)%>"
													selectedIds="<%=idPais%>"/>										
									</td>
								</tr>

								<tr>
									<td class="labelText" id="provinciaSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.provincia" />&nbsp
									</td>
									<td class="ocultar" id="provinciaConAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.provincia" />&nbsp(*)
									</td>
									<td id="provinciaEspanola">
										<siga:Select id="provincia" 
													queryParamId="idprovincia"
													queryId="getProvincias"
													childrenIds="poblacion"
													selectedIds="<%=idProvincia%>"
													disabled="<%=String.valueOf(!editarCampos)%>"/>										
									</td>
									<td class="labelText" id="poblacionSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.poblacion" />&nbsp
									</td>
									<td class="ocultar" id="poblacionConAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.poblacion" />&nbsp(*)
									</td>
									<td id="poblacionEspanola">
										<%
										String idProvinciaJSON = "";
										if (sIdprovincia != null && !"".equals(sIdprovincia)){
											idProvinciaJSON = "{\"idprovincia\":\""+sIdprovincia+"\"}";
										}
										%>
										<siga:Select id="poblacion"
													queryId="getPoblacionesDeProvincia"
													parentQueryParamIds="idprovincia"
													params="<%=idProvinciaJSON%>"
													selectedIds="<%=idPoblacion%>"
													disabled="<%=String.valueOf(!editarCampos)%>"/>										
									</td>
									<td class="ocultar" id="poblacionExtranjera">
										<html:text name="consultaDireccionesForm" property="poblacionExt" styleId="poblacionExt"
												value='<%=poblacionExt%>' size="30" styleClass="<%=clase%>"
												readOnly="<%=desactivado%>">
										</html:text>
									</td>
								</tr>

								<tr>
									<td class="labelText" id="telefonoSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.telefono1" />&nbsp
									</td>
									<td class="ocultar" id="telefonoConAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.telefono1" />&nbsp(*)
									</td>
									<td>
										<html:text name="consultaDireccionesForm" styleId="telefono1"
											property="telefono1" value="<%=telefono1%>" maxlength="20"
											size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>

									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.telefono2" />&nbsp
									</td>
									<td>
										<html:text name="consultaDireccionesForm" styleId="telefono2"
											property="telefono2" value="<%=telefono2%>" maxlength="20"
											size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.movil" />&nbsp
									</td>
									<td>
										<html:text name="consultaDireccionesForm" styleId="movil"
											property="movil" value="<%=movil%>" maxlength="20" size="10"
											styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.fax1" />&nbsp
									</td>
									<td>
										<html:text name="consultaDireccionesForm" styleId="fax1"
											property="fax1" value="<%=fax1%>" maxlength="20" size="10"
											styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.fax2" />&nbsp
									</td>
									<td>
										<html:text name="consultaDireccionesForm" styleId="fax2"
											property="fax2" value="<%=fax2%>" maxlength="20" size="10"
											styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.correo" />&nbsp
									</td>
									<%
										if (!modo.equals("editar")) {
									%>
									<%
										if (!mail.equalsIgnoreCase("")) {
									%>
									<td nowrap>
										<a href="mailto:<%=mail%>">
											<html:text
												name="consultaDireccionesForm" styleId="correoElectronico"
												style="cursor:hand;color:blue;" property="correoElectronico"
												value="<%=mail%>" maxlength="100" size="50"
												styleClass="<%=clase%>" readOnly="<%=desactivado%>">
											</html:text>
										</a>
									</td>
									<%
										} else {
									%>
									<td nowrap>
										<html:text name="consultaDireccionesForm" styleId="correoElectronico"
											property="correoElectronico" value="<%=mail%>"
											maxlength="100" size="50" styleClass="<%=clase%>"
											readOnly="<%=desactivado%>">
										</html:text>
									</td>
									<%
										}
									%>
									<%
										} else {
									%>
									<td nowrap>
										<html:text name="consultaDireccionesForm" styleId="correoElectronico"
											property="correoElectronico" value="<%=mail%>"
											maxlength="100" size="50" styleClass="<%=clase%>"
											readOnly="<%=desactivado%>">
										</html:text>
									</td>
									<%
										}
									%>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.paginaWeb" />&nbsp
									</td>
									<%
										if (!modo.equals("editar")) {
									%>
									<%
										if (!EnlaceWEb.equalsIgnoreCase("")) {
									%>
									<td>
										<a href="<%=EnlaceWEb%>" target="_blank">
											<html:text name="consultaDireccionesForm" styleId="paginaWeb"
												style="cursor:hand;color:blue;" property="paginaWeb"
												value="<%=paginaWEB%>" maxlength="100" size="25"
												styleClass="<%=clase%>" readOnly="<%=desactivado%>">
											</html:text>
										</a>
									</td>
									<%
										} else {
									%>
									<td>
										<html:text name="consultaDireccionesForm" styleId="paginaWeb"
											property="paginaWeb" value="<%=paginaWEB%>" maxlength="100"
											size="25" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
									<%
										}
									%>
									<%
										} else {
									%>
									<td>
										<html:text name="consultaDireccionesForm" styleId="paginaWeb"
											property="paginaWeb" value="<%=paginaWEB%>" maxlength="100"
											size="25" styleClass="<%=clase%>" readOnly="<%=desactivado%>">
										</html:text>
									</td>
									<%
										}
									%>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosDireccion.literal.preferente" />
									</td>
									<td colspan="2" class="labelText">
										<siga:Idioma key="censo.preferente.mail" />
										<input type="checkbox" name="preferenteMail" id="preferenteMail"
										<%if (desactivado)
										out.print("disabled");%>
										<%if(preferenteMail)
										out.println("checked");%>>&nbsp;&nbsp;&nbsp;
										<siga:Idioma key="censo.preferente.correo" />
										<input type="checkbox" name="preferenteCorreo" id="preferenteCorreo"
										<%if (desactivado)
										out.print("disabled");%>
										<%if (preferenteCorreo)
										out.print(" checked");%>>&nbsp;&nbsp;&nbsp;
										<siga:Idioma key="censo.preferente.fax" />
										<input type="checkbox" name="preferenteFax" id="preferenteFax"
										<%if (desactivado)
										out.print("disabled");%>
										<%if (preferenteFax)
										out.print(" checked");%>>&nbsp;&nbsp;&nbsp;
										<siga:Idioma key="censo.preferente.sms" />
										<input type="checkbox" name="preferenteSms" id="preferenteSms"
										<%if (desactivado)
										out.print("disabled");%>
										<%if (preferenteSms)
										out.print(" checked");%>>
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
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
