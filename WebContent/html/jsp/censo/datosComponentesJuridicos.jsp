<!DOCTYPE html>
<html>
<head>
<!-- datosComponentesJuridicos.jsp -->
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
<%@ page import	=	"com.siga.beans.CenComponentesBean"%>
<%@ page import	=	"com.siga.beans.CenPersonaBean"%>
<%@ page import	=	"com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr=(String)usr.getUserName();
	boolean bOcultarHistorico = usr.getOcultarHistorico();
	

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	
	String clase = "box";
	String claseDatosIdentif = "box";
	String claseCombo = "boxCombo";
	String lectura="false";
	String lecturaTipoCol1="false";
	boolean desactivado = false;
	String botones="C,Y,R";

	String nombreUsu =(String)request.getAttribute("nombrePersona");
	String numero = 	(String)request.getAttribute("numero");
	String modo = 		(String)request.getAttribute("modoConsulta");
	Vector colegios = 		(Vector)request.getAttribute("colegios");

	Hashtable htData = null;
	String sociedad = "";
	String nifcif = "";
	String nombreCliente = "";
	String apellidos1Cliente="";
	String apellidos2Cliente="";
	String cargo = "";
	String fechaCargo = "";
	String numeroColegiado = "";
	String idPersona = "";
	String idClientePersona = "";
	String idInstitucion ="";
	String tipo="";
	String capitalSocial="";
	String fechaBaja="";
	
	ArrayList idBanco = new ArrayList();
	ArrayList idtipocolegio = new ArrayList();
	ArrayList idtipoprovincia = new ArrayList();
	ArrayList idInstitucionCli = new ArrayList();
	ArrayList idcargo = new ArrayList();
	String idtipocolegioaux="";
	if (modo.equals("ver") || modo.equals("editar")){
		htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
		if (htData != null) {
			sociedad = (String)htData.get(CenComponentesBean.C_SOCIEDAD);
			nifcif = String.valueOf(htData.get(CenPersonaBean.C_NIFCIF));
			cargo = String.valueOf(htData.get(CenComponentesBean.C_CARGO));
			fechaCargo = GstDate.getFormatedDateShort("",(String)htData.get(CenComponentesBean.C_FECHACARGO));					
			numeroColegiado = (String)htData.get("_NUMERO_COLEGIADO_");
			nombreCliente = (String)htData.get(CenPersonaBean.C_NOMBRE);
			apellidos1Cliente = (String)htData.get(CenPersonaBean.C_APELLIDOS1);
			apellidos2Cliente = (String)htData.get(CenPersonaBean.C_APELLIDOS2);
			idPersona 		= String.valueOf(htData.get(CenComponentesBean.C_IDPERSONA));
			idClientePersona = String.valueOf(htData.get(CenComponentesBean.C_CEN_CLIENTE_IDPERSONA));
			idInstitucion = String.valueOf(htData.get(CenComponentesBean.C_IDINSTITUCION));
			idBanco.add (String.valueOf(htData.get(CenComponentesBean.C_IDCUENTA)));
			idtipocolegio.add (String.valueOf(htData.get(CenComponentesBean.C_IDTIPOCOLEGIO)));
			idtipocolegioaux=String.valueOf(htData.get(CenComponentesBean.C_IDTIPOCOLEGIO));
			idtipoprovincia.add (String.valueOf(htData.get(CenComponentesBean.C_IDPROVINCIA)));
			idInstitucionCli.add (String.valueOf(htData.get(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION)));
			idcargo.add (String.valueOf(htData.get(CenComponentesBean.C_IDCARGO)));
			tipo=String.valueOf(htData.get(CenComponentesBean.C_IDTIPOCOLEGIO));
			capitalSocial=String.valueOf(htData.get(CenComponentesBean.C_CAPITALSOCIAL));
			fechaBaja = GstDate.getFormatedDateShort("",(String)htData.get(CenComponentesBean.C_FECHABAJA));
			
		}
		
		if (modo.equals("ver")){
			desactivado = true;
			clase = "boxConsulta";
			claseCombo="boxConsulta";
			lectura="true";
			lecturaTipoCol1="true";
			botones = "C";
		}
		
		if (modo.equals("editar")){
			lecturaTipoCol1="true";
		}		
		
		if (!modo.equals("nuevo")){			
			claseDatosIdentif = "boxConsulta";			
		}	
		
	} else {
		if (modo.equals("nuevo")) {
			desactivado  = false;
			idPersona		  = String.valueOf((Long)request.getAttribute("idPersona"));
			idInstitucion = String.valueOf((Integer)request.getAttribute("idInstitucion"));
		}
	}
	//String[] param = {idInstitucion, idInstitucion};	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<%=app%>/html/js/validation.js"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>
	<script type="text/javascript" src="<%=app%>/html/js/validacionStruts.js"></script>	

	<!-- Validaciones en Cliente -->
	<html:javascript formName="componentesJuridicosForm" staticJavascript="false" />  
	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Volver
		function accionCerrar(){ 		
			window.top.close();
		}	
		
		//Asociada al boton Restablecer
		function accionRestablecer(){		
			<%if (modo.equals("nuevo")) {%>
				document.componentesJuridicosForm.idTipoColegio1.readOnly=false;
				document.componentesJuridicosForm.idTipoColegio1.disabled=false;
				document.componentesJuridicosForm.numColegiado.readOnly=false;
				document.componentesJuridicosForm.numColegiado.disabled=false;
			<%}%>
			document.getElementsByName("componentesJuridicosForm")[0].reset();
			rellenarCampos();
		}			
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
            sub();
            //habilita los posibles botones deshabilitados para que sí se envíen en el formulario
            jQuery.removeAttr(document.getElementById("nombre"),"disabled");
            jQuery.removeAttr(document.getElementById("apellidos1"),"disabled");
            jQuery.removeAttr(document.getElementById("apellidos2"),"disabled");
            
			// Validamos los errores ///////////
			if ((document.getElementById("sociedad").checked) && (document.getElementById("idCuenta").value == "")) {
				alert ("<siga:Idioma key="messages.censo.componentes.errorCuentaObligatoria"/>");
				fin();
				return false;
			}
			
			if (!validateComponentesJuridicosForm(document.getElementsByName("componentesJuridicosForm")[0])){
				fin();
				return false;
			}
			////////////////////////////////////

			<% if (!bOcultarHistorico) { %>
				var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
			<% } else { %>
					var datos = new Array();
					datos[0] = 1;
					datos[1] = "";
			<% } %>

			if (datos == null){
				fin();
				return false;
			}
			
			if (datos[0] == 1) { // Boton Guardar
				document.getElementById("motivo").value = datos[1];
				<%if (modo.equals("editar")) {%>
					document.getElementsByName("componentesJuridicosForm")[0].modo.value = "modificar";
				<%}	else { %>
					document.getElementsByName("componentesJuridicosForm")[0].modo.value = "insertar";
				<%}%>
				document.getElementById("capitalSocial").value = document.getElementById("capitalSocial").value.replace(/,/,".");
				document.getElementsByName("componentesJuridicosForm")[0].target = "submitArea";
				
				if(jQuery("#colegiadoabogacia").is(":visible")){
					document.getElementById("idTipoColegio").value = document.getElementById("idTipoColegio1").value;
				}				
				if(jQuery("#colegiadonoabogacia").is(":visible")){
					if(document.getElementById("idTipoColegio2").value == ""){
						alert("Debe seleccionar una profesión")
						fin();
						return false;
					}
					
					document.getElementById("idTipoColegio").value =document.getElementById("idTipoColegio2").value;
				}				

				document.getElementsByName("componentesJuridicosForm")[0].submit();

			} else {
				fin();
				return false;
			}
		}		
		
		//Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
		function rellenarCampos(){	
			// Obtenemos los valores para el check sociedad.
			var sociedad = "<%=sociedad%>"	;
		  	if(sociedad == "<%=DB_TRUE%>"){
		  		document.getElementById("sociedad").checked=true;
				if (document.getElementById("idCuenta")){
		  	 		jQuery.removeAttr(document.getElementById("idCuenta"),"disabled");
				}
		  	} else {
			    if (document.getElementById("idCuenta")){
			    	jQuery.attr(document.getElementById("idCuenta"),"disabled","disabled");
				}
		  	}
		}
		
		function buscarCliente() {	
			var datos = new Array();
			datos[0] = ""; 						// idpersona
			datos[1] = ""; 						// idInstitucion
			datos[2] = "nColegiado"; 			// Numero Colegiado
			datos[3] = "nifcif"; 					// NIF
			datos[4] = "Nombre"; 					// Nombre
			datos[5] = "Apellido1"; 			// Apellido1
			datos[6] = "Apellido2"; 			// Apellido2

			var datos = ventaModalGeneral("busquedaClientesModalForm","G");

			if (datos == null || datos[0] == undefined) {
				return false;
			}
			document.getElementById("clienteIdPersona").value = datos[0];
			document.getElementById("clienteIdInstitucion").value = datos[1];
			document.getElementById("nombre").value = datos[4];
			document.getElementById("apellidos1").value=datos[5];
			document.getElementById("apellidos2").value=datos[6];
			document.getElementById("nifcif").value = datos[3];
			document.getElementById("numColegiado").value = datos[2];
			document.getElementById("profesional").checked=true;
			jQuery("#colegiado").show();
			jQuery("#colegiadoabogacia").show();
			jQuery("#colegiadonoabogacia").hide();
			var lista_tipo = document.getElementById("idTipoColegio1").options;
			for (i = 0; i < lista_tipo.length; i++) {
				if (lista_tipo.options[i].value == "1" ) {
					lista_tipo.options[i].selected = true;
					document.getElementById("idTipoColegio").value=lista_tipo.options[i].value;
					break;
				}				
			}			
			
			document.componentesJuridicosForm.clienteIdInstitucion.readOnly=true;
			document.componentesJuridicosForm.clienteIdInstitucion.disabled=true;			
			document.componentesJuridicosForm.idTipoColegio1.readOnly=true;
			document.componentesJuridicosForm.idTipoColegio1.disabled=true;
			document.componentesJuridicosForm.numColegiado.readOnly=true;
			document.componentesJuridicosForm.numColegiado.disabled=true;
			
			jQuery("#sjcs").show();
			jQuery("#colegio1").show();
			jQuery("#colegio2").show();			
		}
		
		function obtenerNif() {
			if (document.componentesJuridicosForm.nifcif.value!="")  {
				var sNIF = document.getElementById("nifcif").value;
			     //document.componentesJuridicosForm.nifcif.value = formateaNIF(sNIF);
			} 
				
		   	var nif = (document.getElementById("nifcif").value);
				
 			if (nif!="") {
				//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y éste se encarga de todo :)
				document.forms[0].modo.value="buscarNIF";
				document.forms[0].target="submitArea";
				document.forms[0].submit();
			}				
		}
		
		function profesionalCol(){
			if (document.getElementById("profesional").checked){
				jQuery("#colegiado").show();
				jQuery("#provincia1").show();
				jQuery("#provincia2").show();
				<%if (tipo.equals("1")) {%>
				jQuery("#colegiadoabogacia").show();
				jQuery("#colegiadonoabogacia").hide();
				if (document.getElementById("sociedad").checked==false){
					jQuery("#asteriscoCuenta").hide();
				} else {
					jQuery("#asteriscoCuenta").show();
				}
				<%}	else { %>
				jQuery("#colegiadoabogacia").hide();
				jQuery("#colegiadonoabogacia").show();
				<%}	 %>
			} else {
				jQuery("#colegiado").hide();
				jQuery("#provincia1").hide();
				jQuery("#provincia2").hide();
			}
		}
		
		function cargar() {
			<% if (tipo.equals("")) {%>
				jQuery("#colegiado").hide();
				jQuery("#provincia1").hide();
				jQuery("#provincia2").hide();
			<%}	else { %>
				document.getElementById("profesional").checked=true;
				<%if (tipo.equals("1")) {%>
					jQuery("#colegiado").show();
					jQuery("#colegio1").show();
					jQuery("#colegio2").show();
					jQuery("#colegiadoabogacia").show();
					jQuery("#colegiadonoabogacia").hide();
					jQuery("#sjcs").show();
					if (document.getElementById("sociedad").checked==false){
						jQuery("#asteriscoCuenta").hide();
						jQuery("#sinasteriscoCuenta").show();
					} else {
						jQuery("#sinasteriscoCuenta").hide();
						jQuery("#asteriscoCuenta").show();
					}
					
					document.componentesJuridicosForm.numColegiado.readOnly=true;
					document.componentesJuridicosForm.numColegiado.disabled=true;
				<%}	else { %>
					jQuery("#colegiado").show();
					jQuery("#provincia1").show();
					jQuery("#provincia2").show();
					jQuery("#colegiadoabogacia").hide();
					jQuery("#colegiadonoabogacia").show();
				<%}%>
			<%}%>
		
			<%if(!fechaBaja.equalsIgnoreCase("")){%>
				jQuery("#divBaja").show();
			<%}%>
		}
		
		function cambiar(){
			var idtipoColegioAux = document.getElementById("idTipoColegio1").value.split("#");		 
			if (idtipoColegioAux[0]!=1){			 
				jQuery("#colegio1").hide();
				jQuery("#provincia1").show();
				jQuery("#colegio2").hide();
				jQuery("#provincia2").show();
				jQuery("#sjcs").hide();
			} else {
			   	jQuery("#colegio1").show();
				jQuery("#provincia1").hide();
				jQuery("#colegio2").show();
				jQuery("#provincia2").hide();
				jQuery("#sjcs").show();
			}
		}
		
		function cuenta(){
			if (document.getElementById("sociedad").checked==true){
				jQuery("#sinasteriscoCuenta").hide();
				jQuery("#asteriscoCuenta").show();
				jQuery("#idCuenta").removeAttr("disabled");
			} else {
				jQuery("#asteriscoCuenta").hide();
				jQuery("#sinasteriscoCuenta").show();
			   	jQuery("#idCuenta").attr("disabled","disabled");
				
			}
		}
		
		function traeDatos() {
			if (document.getElementById("sociedad").checked==true){
				var idclienteper = document.getElementById("clienteIdPersona").value;
				var idper = document.getElementById("idPersona").value;
				var idinsti = document.getElementById("clienteIdInstitucion").value;

				$.ajax({ //Comunicación jQuery hacia JSP  
			           type: "POST",
			           url: "/SIGA/CEN_ComponentesJuridicos.do?modo=existeOtraSociedad",
			           data: "idInstitucion="+idinsti+"&idPersona="+idper+"&idClientePersona="+idclienteper,
			           //contentType: "application/json; charset=utf-8",
			           dataType: "json",
			           success:  function(json) {
				           		if(json.exite=="S")
				           			if(!confirm('<siga:Idioma key="messages.censo.componentes.errorExisteCliente"/> '+json.nifSociedad +' <siga:Idioma key="messages.censo.componentes.cambiarCliente"/>')){
				           				document.getElementById("sociedad").checked=false;
				           				cuenta();
				           			}
			           },
			           error: function(xml,msg){
			        	   //alert("Error1: "+xml);//$("span#ap").text(" Error");
			        	   alert("Error: "+msg);//$("span#ap").text(" Error");
			           }
			    });						
			}			
		}	
		
		function getNumeroColegiado() {
				var idper = document.getElementById("clienteIdPersona").value;
				var idinsti = document.getElementById("clienteIdInstitucion").value;

				$.ajax({ //Comunicación jQuery hacia JSP  
			           type: "POST",
			           url: "/SIGA/CEN_ComponentesJuridicos.do?modo=getNumeroColegiado",
			           data: "idInstitucion="+idinsti+"&idPersona="+idper,
			           //contentType: "application/json; charset=utf-8",
			           dataType: "json",
			           success:  function(json) {
			   						document.getElementById("numColegiado").value = json.numColegiado;	
		   							document.componentesJuridicosForm.numColegiado.readOnly=true;
		   							document.componentesJuridicosForm.numColegiado.disabled=true;
			           },
			           error: function(xml,msg){
			        	   //alert("Error1: "+xml);//$("span#ap").text(" Error");
			        	   alert("Error: "+msg);//$("span#ap").text(" Error");
			           }
			    });						
		}
	</script>	

<!-- INICIO: TITULO Y LOCALIZACION 	-->	
</head>
<body onload="cargar();">
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaComponentesJuridicos.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%> &nbsp;&nbsp;
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
<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/CEN_ComponentesJuridicos.do" method="POST" styleId="componentesJuridicosForm" target="resultado">

		<html:hidden styleId="modo" property="modo" value="cerrar"/>
		<html:hidden styleId="idPersona" property="idPersona" value="<%=idPersona%>"/>
		<html:hidden styleId="idInstitucion" property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden styleId="clienteIdPersona" property="clienteIdPersona" value="<%=idClientePersona%>"/> 
		<html:hidden styleId="motivo" property="motivo" value=""/> 
		<html:hidden styleId="nuevo" property="nuevo" value="1"/> 
		<html:hidden styleId="idTipoColegio" name="componentesJuridicosForm" property="idTipoColegio" value="<%=tipo%>"/>
		<input type="hidden" id="actionModal" name="actionModal" value="" />

		<siga:ConjCampos leyenda="censo.busquedaClientes.literal.datosIdentificacion">
			<table class="tablaCampos" align="center" border="0">
				<tr>		
					<td class="labelText">
						<siga:Idioma key="censo.consultaComponentesJuridicos.literal.nifcif"/>&nbsp;(*)
					</td>
					<td>
						<html:text name="componentesJuridicosForm"  styleId="nifcif" property="nifcif" value='<%=nifcif%>' size="20" maxlength ="20" styleClass="<%=claseDatosIdentif%>" readOnly="<%=desactivado%>" onBlur="obtenerNif();" />	
					</td>
					
					<td class="labelText">
						<siga:Idioma key="censo.consultaComponentesJuridicos.literal.nombre"/>&nbsp;(*)
					</td>
					<td>
						<html:text name="componentesJuridicosForm" styleId="nombre" property="nombre" value='<%=nombreCliente%>' size="20" maxlength ="60" styleClass="<%=claseDatosIdentif%>" readOnly="<%=desactivado%>" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaComponentesJuridicos.literal.apellido1"/>&nbsp;(*)
					</td>
					<td>
						<html:text name="componentesJuridicosForm" styleId="apellidos1" property="apellidos1" value='<%=apellidos1Cliente%>' size="20" maxlength ="60" styleClass="<%=claseDatosIdentif%>" readOnly="<%=desactivado%>" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="censo.consultaComponentesJuridicos.literal.apellido2"/>&nbsp;
					</td>
					<td>
						<html:text name="componentesJuridicosForm" styleId="apellidos2" property="apellidos2" value='<%=apellidos2Cliente%>' size="20" maxlength ="60" styleClass="<%=claseDatosIdentif%>" readOnly="<%=desactivado%>" />
					</td>
				</tr>
			</table>	
		</siga:ConjCampos>
	
		<table class="tablaCampos" align="center" border="0">
			<tr>			
				<td width="20px">
					<html:checkbox name="componentesJuridicosForm" styleId="profesional" property="profesional" disabled="<%=desactivado%>" onclick="profesionalCol()"/>
				</td>
				<td class="labelText" >
					<siga:Idioma key="censo.consultaComponentesJuridicos.literal.esProfesionalColegiado"/>
				</td>
				<%if (modo.equals("nuevo")) {%>
					<td class="tdBotones">
						<input type="button" alt="<siga:Idioma key="general.boton.search"/>"  id="buscar" onclick="return buscarCliente();" class="button" value="<siga:Idioma key="general.boton.search"/>">
					</td>
				<% } %>
			</tr>
		</table>
	
	 
		<table id="colegiado" style="display:none" class="tablaCampos" align="center" border="0">
		  	<tr>
		  		<td>
					<siga:ConjCampos leyenda="censo.busquedaClientes.literal.datosColegiacion">
						<table class="tablaCampos" width="100%" border="0">
							<tr>			
								<td class="labelText" >
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.tipocolegio"/>
								</td>
	<% 
								ArrayList elementoSelTipoColegio = new ArrayList(); 
								elementoSelTipoColegio.add(idtipocolegioaux);
	%>
								<td id="colegiadoabogacia" style="display:none">
									<siga:ComboBD nombre="idTipoColegio1" tipo="cmbActividadProfesional" clase="<%=claseCombo%>" obligatorioSinTextoSeleccionar="true" seleccionMultiple="false" readonly="<%=lecturaTipoCol1%>" elementoSel="<%=elementoSelTipoColegio%>" accion="cambiar()"  /> 								
								</td>
								<td id="colegiadonoabogacia" style="display:none">
									<siga:ComboBD nombre="idTipoColegio2" tipo="cmbActividadProfesionalNoColegiado" clase="<%=claseCombo%>" obligatorio="true" seleccionMultiple="false" readonly="<%=lectura%>" elementoSel="<%=elementoSelTipoColegio%>" />								
								</td>							
								
								<td id="provincia1" style="display:none" class="labelText">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.provincia"/>
								</td>	
								<td id="provincia2" style="display:none">
									<siga:ComboBD nombre = "idProvincia" tipo="provincia" obligatorio="true"  clase="<%=claseCombo%>" readonly="<%=lectura%>"  elementoSel="<%=idtipoprovincia%>"/>
								</td>
					
								<td id="colegio1" style="display:none" class="labelText">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.colegio"/>
								</td>	
								<%if (modo.equals("editar")) {
									Object[] paramColegios = colegios.toArray();
								%>
									<td id="colegio2" style="display:none">
										<siga:ComboBD nombre = "clienteIdInstitucion" tipo="cmbColegiosAbreviadosIn" obligatorioSinTextoSeleccionar="true"  clase="<%=claseCombo%>" readonly="<%=lectura%>"  elementoSel="<%=idInstitucionCli%>" parametrosIn="<%=paramColegios%>" accion="getNumeroColegiado();" />
									</td>
								<%}	else { %>
									<td id="colegio2" style="display:none">
										<siga:ComboBD nombre = "clienteIdInstitucion" tipo="cmbColegiosAbreviados" obligatorioSinTextoSeleccionar="true"  clase="<%=claseCombo%>" readonly="<%=lectura%>"  elementoSel="<%=idInstitucionCli%>" accion="getNumeroColegiado();"/>
									</td>
								<% } %>
								
							</tr>
				
							<tr>
								<td class="labelText" >
									<siga:Idioma key="censo.consultaDatosColegiales.literal.colegiado"/>
								</td>
								<td>
									<html:text name="componentesJuridicosForm" styleId="numColegiado" property="numColegiado" value='<%=numeroColegiado%>' size="10" styleClass="<%=clase%>" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
		  		</td>
		  	</tr>
		</table>
			
						
		<table class="tablaCampos" align="center" border="0">	
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.busquedaClientes.literal.cargo">
						<table class="tablaCampos" align="center" border="0">	
							<tr>
								<td class="labelText" width="185px" >
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaCargo"/>&nbsp;
								</td>						
								<td width="140px" colspan="2">							
									<%if (!desactivado) {%>
										<siga:Fecha nombreCampo= "fechaCargo" valorInicial="<%=fechaCargo%>"/>
									<%}else{%>
										<siga:Fecha  nombreCampo= "fechaCargo"  valorInicial="<%=fechaCargo%>" disabled="true"/>
									<%}%>								
								</td>
							
								<td class="labelText" >
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.cargo"/>&nbsp;
								</td>	
	<% 
								String parametro1[] = new String[2];
					   			parametro1[0] = idPersona;
					   			parametro1[1] = idInstitucion; 
	%>
								<td>
									<siga:ComboBD nombre="idCargo" tipo="cmbCenCargos" obligatorio="false"  clase="<%=claseCombo%>" readonly="<%=lectura%>"  elementoSel="<%=idcargo%>"/>
								</td>	
							</tr>
							
							<tr>
								<td class="labelText" width="185px">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.descripcioncargo"/>&nbsp;
								</td>	
								<td colspan="2">
									<html:text name="
									componentesJuridicosForm" property="cargo" styleId="cargo" value='<%=cargo%>' size="50" maxlength ="255" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
								</td>
								
								<td class="labelText" width="185px">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.ParticipacionSociedad"/>&nbsp;
								</td>	
								<td >
									<html:text name="componentesJuridicosForm" property="capitalSocial" styleId="capitalSocial" value='<%=capitalSocial%>' onkeypress="filterChars(this,false,true);" 
										onkeyup="filterCharsUp(this);" onblur="filterCharsNaN(this);" size="7" maxlength ="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
								</td>							
							</tr>
						</table>
						<div id="divBaja" style="left:2px;display:none">
							<div style="padding:3px">
								<span class="labelText" style="width:175px;display: inline-block;"><siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaBaja"/></span>
								<span class="labelTextValue"><%=fechaBaja%></span>
							</div>
						</div>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	
		<table id="sjcs" class="tablaCampos" style="display:none" width="100%" border="0" cellpadding="0" cellspacing="0">	
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.busquedaClientes.literal.liquidacionSJCS">	
						<table class="tablaCampos" width="100%" border="0" cellpadding="5" cellspacing="0">	
							<tr>						
								<td class="labelText" width="200px">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.liquidarSJCS"/>
								</td>
								<td>
									<html:checkbox name="componentesJuridicosForm" property="sociedad" styleId="sociedad" disabled="<%=desactivado%>" onclick="cuenta();traeDatos()"/>
								</td>
							</tr>
							
							<tr>
								<td id="asteriscoCuenta" class="labelText">
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.cuenta"/> &nbsp;(*)
								</td>
								<td id="sinasteriscoCuenta" class="labelText" style="display:none" >
									<siga:Idioma key="censo.consultaComponentesJuridicos.literal.cuenta"/> 
								</td>
								<td>
	<% 
									String parametro[] = new String[2];
							 		parametro[0] = idPersona;
									parametro[1] = idInstitucion; 
	%>
							    	<siga:ComboBD nombre="idCuenta" tipo="cuentaSJCS" parametro="<%=parametro%>" clase="<%=clase%>" obligatorio="false" elementoSel="<%=idBanco%>" readonly="<%=String.valueOf(desactivado)%>"/>									
								</td>
							</tr>						
			   			</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>   		
		
		<script>
			rellenarCampos();
		</script>	
	</html:form>
	
	<!-- FORMULARIO PARA RECOGER LOS DATOS DE LA BUSQUEDA -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
 		<input type="hidden" id="actionModal"  name="actionModal" value="">
  		<input type="hidden" id="modo" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->		
		<siga:ConjBotonesAccion botones='<%=botones%>' modal="G"/>
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