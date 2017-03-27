<!DOCTYPE html>
<html>
<head>
<!-- solicitudModificacionDirecciones.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDireccionTipoDireccionBean"%>
<%@ page import = "com.siga.beans.CenTipoDireccionBean"%>
<%@ page import = "com.siga.beans.CenDireccionesBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "java.util.Arrays"%>
<%@ page import = "java.util.List"%>
<%@ page import = "java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	//UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	String pais="";
	String poblacionExt="";
	String codigoPostal="";
	String otraProvinciaString="";
//	Hashtable htData=(Hashtable)request.getAttribute("hDatos");		
	Hashtable htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	

	ArrayList idPais      = new ArrayList();
	ArrayList idProvincia = new ArrayList();
	ArrayList idPoblacion = new ArrayList();			
	idPais.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPAIS)));
	idProvincia.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPROVINCIA)));
	idPoblacion.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPOBLACION)));
	codigoPostal = String.valueOf(htData.get(CenDireccionesBean.C_CODIGOPOSTAL));
	otraProvinciaString =String.valueOf( htData.get(CenDireccionesBean.C_OTRAPROVINCIA));
	
	poblacionExt = String.valueOf(htData.get("POBLACIONEXTRANJERA"));
	
	//NUEVO.27012016
	String idTipoDirecciones = "";
	idTipoDirecciones = (String.valueOf(htData.get("IDTIPODIRECCION")));
	List<String> listaDirecciones = null;
	String descripcionDirecciones = "";
	if(idTipoDirecciones != null && !"".equalsIgnoreCase(idTipoDirecciones)){
		listaDirecciones = new ArrayList<String>(Arrays.asList(idTipoDirecciones.split(",")));
		descripcionDirecciones = String.valueOf(htData.get("CEN_TIPODIRECCION.DESCRIPCION"));
	}
	String ididPais = String.valueOf(htData.get(CenDireccionesBean.C_IDPAIS));
	idPais.add(ididPais);
	String provincia = String.valueOf(htData.get("PROVINCIA"));
	
	
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>


<!-- HEAD -->

<style>
.ocultar {display:none}
</style>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="consultaDireccionesSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Volver -->

		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		var paisGlobal='<%=ididPais%>';
		jQuery(function(){
			jQuery("#pais").on("change", function(){
				selPais(jQuery(this).val());
				
				if(trim(document.consultaDireccionesSolicForm.codigoPostal.value) != "")
					if(trim(document.consultaDireccionesSolicForm.pais.value)!=idEspana && trim(document.consultaDireccionesSolicForm.pais.value) != "")
						jQuery("#codigoPostal").val("");
			});
		});
		
		function accionCerrar(){ 			
			window.top.close();
		}	
		/*
	    function selPais(valor) {                                                                   
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
		   	  	jQuery("#provincia").attr("disabled","disabled");
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				jQuery("#provincia").removeAttr("disabled");
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
	       }
	    }
		
		function selPaisInicio() {
			var p = document.getElementById("pais");
			selPais(p.value);
		}	    
		*/
		 function selPais(valor) {
			   if (valor!=null && valor!="" && valor!=idEspana) {
			   		document.getElementById("poblacion").value="";
			   		document.getElementById("provincia").value="";
					//Ocultamos la provincia
					jQuery("#provinciaSinAsterisco").hide();
					jQuery("#provinciaText").hide();
					jQuery("#otraProvinciaCheck").removeAttr('checked');
					jQuery("#tdOtraProvincia").hide();
					jQuery("#codigoPostal").val("");	
					jQuery("#provincia").val(jQuery("#provincia option:first").val());
					jQuery("#provinciaText").val("");
					jQuery("#provincia").change();	
				   	//aalg: se quita la marca de obligatoriedad
				   	document.getElementById("provinciaSinAsterisco").className="labelText";
					document.getElementById("poblacionEspanola").className="ocultar";
					document.getElementById("poblacionExtranjera").className="";
					document.getElementById("poblacionExt").value="";
		       } else {
		    	   document.getElementById("poblacionExt").value="";
					//Mostramos la provincia
					jQuery("#provinciaSinAsterisco").show();
					jQuery("#provinciaText").show();
					jQuery("#tdOtraProvincia").show();
					//Para el caso de que venga null, sino se pone mostrarÃ¡ por pantalla en el campo codigo postal undefined
					if((paisGlobal != "") && (paisGlobal !=idEspana))
					jQuery("#codigoPostal").val("");				
					document.getElementById("poblacionEspanola").className="";
					document.getElementById("poblacionExtranjera").className="ocultar";
					//aalg: se restaura la marca de obligatoriedad si es pertinente
					comprobarAsterico();
					
		       }
			   paisGlobal=jQuery("#pais").val();
		    }
			
			function selPaisInicio() {
				var valor = document.getElementById("pais").value;
				if (valor!=null && valor!="" && valor!=idEspana) {
			   		document.getElementById("poblacion").value="";
			   		document.getElementById("provincia").value="";
			   		//Ocultamos la provincia
			   		jQuery("#provinciaSinAsterisco").hide();
					jQuery("#provinciaText").hide();
				   	//aalg: se quita la marca de obligatoriedad
				   	document.getElementById("provinciaSinAsterisco").className="labelText";
					document.getElementById("poblacionEspanola").className="ocultar";
					document.getElementById("poblacionExtranjera").className="";
					jQuery("#tdOtraProvincia").hide();
		       } else {
		    	   
			   		document.getElementById("poblacionExt").value="";
					document.getElementById("poblacionEspanola").className="";
					document.getElementById("poblacionExtranjera").className="ocultar";		
					//Mostramos la provincia
					<%if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")){%>
					  	jQuery("#provinciaEspanola").css("display","inline");
					    jQuery("#provinciaText").hide();
					<%}else{%>
						jQuery("#provinciaText").show();
					<%}%>
					//aalg: se restaura la marca de obligatoriedad si es pertinente
					comprobarAsterico();
		       }
			}
		<!-- Asociada al boton Restablecer -->
		//Asociada al boton Restablecer -->
		function accionRestablecer(){
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.consultaDireccionesSolicForm.reset();
				comprobarAsterico();
				selPais(<%=ididPais%>);
				jQuery("#provincia").change();
				
				<%if(ididPais.equals(ClsConstants.ID_PAIS_ESPANA) || "".equals(ididPais)){%>	
						document.consultaDireccionesSolicForm.codigoPostal.value='<%=codigoPostal%>';
						<%if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")){%>
						  	jQuery("#provinciaEspanola").css("display","inline");
						    jQuery("#provinciaText").hide();
						    jQuery("#otraProvinciaCheck").attr('checked','checked');
						<%}else{%>
							jQuery("#provinciaText").show();
						<%}%>
				<%}else{%>
					var codpostal = '<%=codigoPostal%>';
					var poblacionExtAux = '<%=poblacionExt%>';
					if(codpostal == null){
						document.consultaDireccionesSolicForm.codigoPostal.value="";
					}else{
						document.consultaDireccionesSolicForm.codigoPostal.value=codpostal;
					}
					document.getElementById("poblacionExt").value=poblacionExtAux;
					createProvince();
				<%}%>
			}		
		}
		
		
		<!-- Actualiza las direcciones preferentes -->
		function actualizar(){
	    
	     
	     document.forms[0].modificarPreferencias.value="1";
	     document.forms[0].submit();
		 document.forms[0].modificarPreferencias.value="0";
	    }		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {	
			sub();
			if(document.getElementById("telefonoConAsterisco").className =="labelText"){
				if(trim(document.consultaDireccionesSolicForm.telefono1.value)=="")
				{
					var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.telefono1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert(mensaje);
					fin();
					return false;
				}		
			}	
			
			// RGG 01-03-2005 cambio de validacion
			if((document.consultaDireccionesSolicForm.preferenteMail.checked) && 
				 (trim(document.consultaDireccionesSolicForm.correoElectronico.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			if((document.consultaDireccionesSolicForm.preferenteCorreo.checked) && 
				 (trim(document.consultaDireccionesSolicForm.domicilio.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			// RGG 25/04/2005
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") &&
				(trim(document.consultaDireccionesSolicForm.codigoPostal.value)=="")) {
				
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") && (trim(document.consultaDireccionesSolicForm.pais.value)==idEspana ||trim(document.consultaDireccionesSolicForm.pais.value)=="" ) && 
					(trim(document.consultaDireccionesSolicForm.provincia.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.provincia"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
			if(trim(document.consultaDireccionesSolicForm.provincia.value)!=="" && (trim(document.consultaDireccionesSolicForm.poblacion.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
			}
				
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") && (trim(document.consultaDireccionesSolicForm.pais.value)!=idEspana && trim(document.consultaDireccionesSolicForm.pais.value)!="") && 
					(trim(document.consultaDireccionesSolicForm.poblacionExt.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
			}			
			if((document.consultaDireccionesSolicForm.preferenteFax.checked) && 
				 (trim(document.consultaDireccionesSolicForm.fax1.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
            if((document.consultaDireccionesSolicForm.preferenteSms.checked) && 
				 (trim(document.consultaDireccionesSolicForm.movil.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
            if (validateConsultaDireccionesSolicForm(document.consultaDireccionesSolicForm)){			
				document.all.consultaDireccionesSolicForm.modo.value="insertarModificacion";	
				if(jQuery("#otraProvinciaCheck").is(':checked') && jQuery("#otraProvinciaCheck").is(':visible')){
					document.forms[0].otraProvincia.value = "1";
				} else{
					document.forms[0].otraProvincia.value= "0";
				}
				document.all.consultaDireccionesSolicForm.submit();					
			}
			else{
				fin();
			}			
		}	
	
		<!-- Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable -->
		function rellenarCampos(){
			f=document.all.consultaDireccionesSolicForm;
			
			// Inicializo
			f.preferenteMail.checked=false;
			f.preferenteCorreo.checked=false;
			f.preferenteFax.checked=false;
			f.preferenteSms.checked=false;
		
			// Campo Preferente
			preferente="<%=String.valueOf(htData.get(CenDireccionesBean.C_PREFERENTE))%>"	
			if(preferente.indexOf("E")!=-1)  	f.preferenteMail.checked=true;		  
 		  	if(preferente.indexOf("C")!=-1)  	f.preferenteCorreo.checked=true;
		  	if(preferente.indexOf("F")!=-1)  	f.preferenteFax.checked=true;
            if(preferente.indexOf("S")!=-1)  	f.preferenteSms.checked=true;		 		
		  	// Campo provincia	
		 	document.getElementById("provincia").onchange();	  	
		 
		}
		function comprobarAsterico() {
			var checkGuardia = false;
			var checkPostal = false;
			var oCheck =<%=listaDirecciones%>
			for(i=0; i<oCheck.length; i++){		
				if (oCheck[i]==6){
	            checkGuardia = true;
	          }
	          if (oCheck[i]==3||oCheck[i]==2||oCheck[i]==5||oCheck[i]==8||oCheck[i]==9){					  
			    checkPostal=true;						
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
				document.getElementById("poblacionSinAsterisco").className="labelText";
				document.getElementById("poblacionConAsterisco").className="ocultar";
				document.getElementById("paisSinAsterisco").className="labelText";
				document.getElementById("paisConAsterisco").className="ocultar";
			
	    	}
			
	    } 
		
		function createProvince() {
			if (trim(document.consultaDireccionesSolicForm.pais.value) == idEspana || trim(document.consultaDireccionesSolicForm.pais.value) == "" ) {
				
				var Primary = document.consultaDireccionesSolicForm.codigoPostal.value;
				if ((Primary == null) || (Primary == "")) {
					//Inicializamos todo
					jQuery("#provincia").val(jQuery("#provincia option:first").val());
					jQuery("#provinciaText").val("");
					jQuery("#provincia").change();
					return;
				} 
				if(Primary.length<5){
					 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.cp.tamanyo"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}else{
					var idProvincia	= Primary.substring(0,2);
					//Comprobamos que exista los dos primeros dÃ­gitos del c.p con algÃºn elemento de la lista de provincia
					if (jQuery("#provincia").val() != idProvincia){
						if (jQuery("#provincia").find("option[value='"+idProvincia+"']").exists()){
							jQuery("#provincia").val(idProvincia);
							jQuery("#provinciaText").val( jQuery("#provincia option:selected").text());
							//document.consultaDireccionesSolicForm.idProvinciaHidden.value=idProvincia;
							jQuery("#poblacion").val(jQuery("#poblacion option:first").val());
						 	jQuery("#provincia").change();
						 	
						} else {
							var mensaje = "<siga:Idioma key="censo.datosDireccion.noSeEncuentraProvincia"/>";
			 				alert (mensaje+document.consultaDireccionesSolicForm.codigoPostal.value);
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
			if(valor.checked ) {
			   //Si está seleccionado
			  	jQuery("#provinciaEspanola").css("display","inline");
			    jQuery("#provinciaText").hide();
			}else{
				createProvince();
				jQuery("#provinciaEspanola").hide();
			    jQuery("#provinciaText").css("display","inline");
			}
		}
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onLoad="comprobarAsterico();selPaisInicio();">
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDirecciones.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				      <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>
	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->
	
	<html:form action="/CEN_SolicitudDirecciones.do" method="POST" target="submitArea">
		<html:hidden property="modo" value="cerrar"/>
		<input type='hidden' name = "idPersona" value="<%=htData.get(CenDireccionesBean.C_IDPERSONA)%>"/>	
		<input type='hidden' name = "idInstitucion" value="<%=htData.get(CenDireccionesBean.C_IDINSTITUCION)%>"/>
		<input type='hidden' name = "idDireccion" value="<%=htData.get(CenDireccionesBean.C_IDDIRECCION)%>">
		<input type="hidden" name = "modificarPreferencias" value="">
		<input type="hidden" name = "idDireccionesPreferentes" value = ""/>
		<input type="hidden" name = "idDireccionesCensoWeb" value = ""/>
		<input type="hidden" id="otraProvincia" name="otraProvincia" value="">
		<table class="tablaCentralCamposGrande" align="center">			
			<tr>				
					<td>
					<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
						<table class="tablaCampos" align="center">							
							<!-- FILA -->
							<tr>	
								<td class="labelText" width="180px" id="tipoDireccion">
										<siga:Idioma key="censo.datosDireccion.literal.tipoDireccion"/>&nbsp;
								</td>	
								<td>
									<%=descripcionDirecciones%>
								</td>		
							</tr>
							<tr>	
							
								<td class="labelText" width="180px" id="direccionSinAsterisco">
										<siga:Idioma key="censo.datosDireccion.literal.direccion"/>&nbsp;
								</td>
								<td class="ocultar" width="180px" id="direccionConAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.direccion"/>&nbsp;(*)
								</td>
								
								<td>
									<html:textarea cols="70" rows="2" name="consultaDireccionesSolicForm" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" property="domicilio" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_DOMICILIO))%>" maxlength="100"  styleClass="box"></html:textarea>
								</td>						
													
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText" width="180px" id="paisSinAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.pais2"/>&nbsp;
								</td>
								<td class="ocultar" width="180px" id="paisConAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.pais2"/>&nbsp;(*)
								</td>
								<td>									
									<siga:ComboBD nombre="pais" tipo="pais" clase="boxCombo" obligatorio="false" elementoSel="<%=idPais%>" accion="selPais(this.value);"/>
								</td>
							</tr>
							<tr>
								<td class="labelText" width="180px" id="cpSinAsterisco" nowrap>
										<siga:Idioma key="censo.datosDireccion.literal.cp"/>&nbsp;
								</td>
								<td class="ocultar" width="180px" id="cpConAsterisco" nowrap>
									<siga:Idioma key="censo.datosDireccion.literal.cp"/>&nbsp;(*)
								</td>					
								<td>
									<html:text name="consultaDireccionesSolicForm" styleId="codigoPostal" property="codigoPostal" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_CODIGOPOSTAL))%>" size="5" maxlength="5" styleClass="box" onChange="createProvince()"></html:text>
								</td>		
								<td nowrap="nowrap" id="tdOtraProvincia"> 
										<siga:Idioma key="censo.datosDireccion.literal.otraProvincia" />
											<%if(otraProvinciaString != null && !"".equalsIgnoreCase(otraProvinciaString) && otraProvinciaString.equalsIgnoreCase("1")){ %>
												<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck" checked="checked"  onclick="otraProvinciaFuction(this);"> &nbsp;
											<%}else{ %>
												<input type="checkbox" id="otraProvinciaCheck" name="otraProvinciaCheck"  onclick="otraProvinciaFuction(this);"> &nbsp;
											<%} %>
								</td>
								<td class="labelText" id="provinciaSinAsterisco" nowrap="nowrap">
								
									<siga:Idioma key="censo.datosDireccion.literal.provincia" />&nbsp;
								
								
									<div id="provinciaEspanola" style="display: none;">
										
											<siga:ComboBD nombre="provincia" 
												tipo="provincia" clase="boxCombo" obligatorio="false"
												elementoSel="<%=idProvincia%>" accion="Hijo:poblacion"  /> 
										
									</div>
									<input id="provinciaText" class="boxConsulta" type="text" value="<%=provincia%>" readonly="readonly" tabindex="-1" style="width: 200px" />
								</td>							
							</tr>
							<tr>
								<td class="labelText" id="poblacionSinAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>&nbsp;
								</td>
								<td class="ocultar" id="poblacionConAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>&nbsp;(*)
								</td>
								<td  id="poblacionEspanola">								
									<siga:ComboBD nombre="poblacion" id="poblacion" tipo="poblacion" clase="boxCombo" elementoSel="<%=idPoblacion%>" hijo="t"/> 		
								</td>
								<td  class="ocultar"  id="poblacionExtranjera">
										<html:text name="consultaDireccionesSolicForm" property="poblacionExt" value='<%=poblacionExt%>' size="30" styleClass="box" readOnly="false"></html:text>
								</td>
								
							</tr>
		   				<!-- FILA -->
		  				<tr>
		   					<td class="labelText" id="telefonoSinAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp;
							</td>				
		   					<td class="ocultar" id="telefonoConAsterisco">
									<siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp;(*)
							</td>					
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="telefono1" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO1))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>			   	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.telefono2"/>&nbsp;
								</td>				
		   					<td>
		   	 					<html:text name="consultaDireccionesSolicForm" property="telefono2" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO2))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>	
		  				</tr>
		  					
		   				<!-- FILA -->
							<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.movil"/>&nbsp;
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="movil" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_MOVIL))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.fax1"/>&nbsp;
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="fax1" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_FAX1))%>" size="20" maxlength="20" styleClass="box"></html:text>		   					
		   					</td>
		   					
		 					</tr>
		 					<!-- FILA -->
		  				<tr>
		  				 	<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.fax2"/>&nbsp;
								</td>				
		   					<td colspan="3">
		   						<html:text name="consultaDireccionesSolicForm" property="fax2" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_FAX2))%>" size="20" maxlength="20" styleClass="box"></html:text>		   					
			   	 		   	</td>
		  				</tr>
		  				<!-- FILA -->
		 					<tr>	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.correo"/>&nbsp;										
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="correoElectronico" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_CORREOELECTRONICO))%>" size="50" maxlength="100" styleClass="box"></html:text>					
		   			   	</td>	
		  					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.paginaWeb"/>&nbsp;
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="paginaWeb" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_PAGINAWEB))%>" size="25" maxlength="100" styleClass="box"></html:text>					
		  				 	</td>			   	
		  				</tr>
		  			 	 <tr>
				  			<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.preferente"/></td>
						  	<td colspan="4">
							  	<table>
							  		<tr>
					   					<td class="labelText"><siga:Idioma key="censo.preferente.mail"/> 
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteMail"> </html:checkbox> </td>
					   					<td class="labelText"><siga:Idioma key="censo.preferente.correo"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteCorreo"> </html:checkbox> </td>					   					
					   					<td class="labelText"><siga:Idioma key="censo.preferente.fax"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteFax"> </html:checkbox> </td>
					   						<td class="labelText"><siga:Idioma key="censo.preferente.sms"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteSms"> </html:checkbox> </td>
					   											   					 
							  		</tr>
							  	</table>
						  	</td>
		  				</tr>					  
							<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.motivo"/>&nbsp;(*)
							</td>											
			   				<td colspan="3">
		   						<textarea name="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" class="box" cols="88" rows="2"></textarea> 
							</td>		   					
		  				</tr>		  			 			 
		   			</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</html:form>
		</table>
		<script>rellenarCampos()</script>
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="C,Y,R" modal="G"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
