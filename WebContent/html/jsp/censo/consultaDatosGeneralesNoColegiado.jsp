<!-- consultaDatosGeneralesNoColegiado.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.siga.beans.CenInstitucionBean"%>
<%@ page import="com.siga.beans.CenNoColegiadoBean"%>
<%@ page import="com.atos.utils.GstDate"%>




<!-- JSP -->
<% 
	// Controles generales
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	
	// Datos formulario y sesion
	DatosGeneralesForm formulario = (DatosGeneralesForm)request.getAttribute("datosGeneralesForm");
	String modo = (String)request.getAttribute("modoPestanha");
	boolean bOcultarHistorico = user.getOcultarHistorico();
	String mostrarMensaje = (String) request.getSession().getAttribute("MOSTRARMENSAJE");
	String mostrarMensajeNifCif = (String) request.getSession().getAttribute("MOSTRARMENSAJECIFNIF");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	
	// Obteniendo varias cosas
	ArrayList gruposSel = new ArrayList();
	String cliente = "";
	String numeroColegiado = "";
	boolean bColegiado = false;
	boolean bfCertificado=true;
	String fechaCertificado = "";
	Vector resultado = null;
	String [] institucionParam = new String[1];
	String primeraLetraCif = "";
	
	try {
		primeraLetraCif = (String)request.getAttribute("primeraLetraCif");

		if (!formulario.getIdPersona().equals("")) {
			// modo != nuevo	
			// atributos
			resultado = (Vector) request.getAttribute("CenResultadoDatosGenerales");
		
			String[] arrayGruposSel = formulario.getGrupos();
			if (arrayGruposSel!=null) {
				for (int j=0;j<arrayGruposSel.length;j++) {
					gruposSel.add(arrayGruposSel[j]);
				}
			}
		
			String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
			cliente = UtilidadesString.getMensajeIdioma(user, colegiado);
	
			numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
			if (numeroColegiado!=null) {
				bColegiado=true;
			} 
			fechaCertificado = (String) request.getAttribute("CenDatosGeneralesCertificado");
			if (fechaCertificado==null || fechaCertificado.equals("")) {
				fechaCertificado=UtilidadesString.getMensajeIdioma(user, "censo.ConsultaDatosGenerales.mensaje.NoCertificado");
				bfCertificado=false;
			}
	
		} // del if de nuevo
		
		// Obteniendo idinstitucion
		if (formulario.getIdInstitucion()!= null && !formulario.getIdInstitucion().equals(""))
	   		institucionParam[0] = formulario.getIdInstitucion();
	   	else
		   	institucionParam[0] = user.getLocation();
	} catch (Exception e){
	
	}

	// Definicion de variables a usar en la JSP
	String nuevoNumRegistro = null;
	String idInstitucion = "";
	String idPersona = "";
	String fotografia = "";
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	String nIdentificacion = "";
	String fechaNacimiento = "";
	String fechaAlta = "";
	String nacido = "";
	String estad = "";
	String publicidad = "";
	String guiaJudicial = "";
	String comisiones = "";
	String cuentaContable = ""; 
	String sexo = "";
	String idioma = "";
	String estadoCivil = "";
	String caracter = "";
	String tipoi = "";
	String tipoOriginal = "";
	String tipoDisabled = "false";
	// seleccion de combos
	ArrayList tratamientoSel = new ArrayList();
	String tipoIdentificacionSel = "20";
	ArrayList estadoCivilSel = new ArrayList();
	ArrayList idiomaSel = new ArrayList();
	ArrayList caracterSel = new ArrayList();
	
	// Si no es un registro nuevo, todos los datos anteriores se obtienen del formulario
	if (!modo.equalsIgnoreCase("NUEVASOCIEDAD")) {
		// SOLO VA A TENER UN REGISTRO 
		Hashtable registro = (Hashtable) resultado.get(0);

		// calculo de campos
		idInstitucion=formulario.getIdInstitucion();
		if (idInstitucion==null) idInstitucion=""; 
		idPersona = (String) registro.get(CenPersonaBean.C_IDPERSONA);
		if (idPersona==null) idPersona=""; 
		// fotografia con path virtual
		fotografia = (String) registro.get(CenClienteBean.C_FOTOGRAFIA);
		if (fotografia!=null && !fotografia.equals("")) {
			fotografia = File.separatorChar+ClsConstants.PATH_DOMAIN+
							  File.separatorChar+ClsConstants.RELATIVE_PATH_FOTOS+
							  File.separatorChar+idInstitucion+
							  File.separatorChar+fotografia;
		}
		nombre = (String) registro.get(CenPersonaBean.C_NOMBRE);
		if (nombre==null) nombre=""; 
		apellido1 = (String) registro.get(CenPersonaBean.C_APELLIDOS1);
		if (apellido1==null) apellido1=""; 
		apellido2 = (String) registro.get(CenPersonaBean.C_APELLIDOS2);
		if (apellido2==null) apellido2=""; 
		nIdentificacion = (String) registro.get(CenPersonaBean.C_NIFCIF);
		if (nIdentificacion==null) nIdentificacion=""; 
		fechaNacimiento = registro.get(CenPersonaBean.C_FECHANACIMIENTO)==null?"":GstDate.getFormatedDateShort(user.getLanguage(),(String)registro.get(CenPersonaBean.C_FECHANACIMIENTO));	
		if (fechaNacimiento==null) fechaNacimiento=""; 
		fechaAlta = registro.get(CenClienteBean.C_FECHAALTA)==null?"":GstDate.getFormatedDateShort(user.getLanguage(),(String)registro.get(CenClienteBean.C_FECHAALTA));	
		if (fechaAlta==null) fechaAlta=""; 
		nacido = (String) registro.get(CenPersonaBean.C_NATURALDE);
		if (nacido==null) nacido=""; 
		estad = (String) registro.get(CenPersonaBean.C_NIFCIF);
		if (estad==null) estad=""; 
		publicidad = (String) registro.get(CenClienteBean.C_PUBLICIDAD);
		if (publicidad==null) publicidad=""; 
		guiaJudicial = (String) registro.get(CenClienteBean.C_GUIAJUDICIAL);
		if (guiaJudicial==null) guiaJudicial=""; 
		comisiones = (String) registro.get(CenClienteBean.C_COMISIONES);
		if (comisiones==null) comisiones=""; 
		cuentaContable = (String) registro.get(CenClienteBean.C_ASIENTOCONTABLE);
		if (cuentaContable==null) cuentaContable=""; 
		sexo = (String) registro.get(CenPersonaBean.C_SEXO);
		if (sexo==null) sexo=""; 
		idioma = (String) registro.get(CenClienteBean.C_IDLENGUAJE);
		if (idioma==null) idioma=""; 
		estadoCivil = (String) registro.get(CenPersonaBean.C_IDESTADOCIVIL);
		if (estadoCivil==null) estadoCivil=""; 
		caracter = (String) registro.get(CenClienteBean.C_CARACTER);
		if (caracter==null) caracter=""; 
		tipoi = (String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION);
		if (tipoi==null) tipoi=""; 

		// combos
		tratamientoSel.add((String) registro.get(CenClienteBean.C_IDTRATAMIENTO));
		tipoIdentificacionSel=(String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION);
		estadoCivilSel.add((String) registro.get(CenPersonaBean.C_IDESTADOCIVIL));
		idiomaSel.add((String) registro.get(CenClienteBean.C_IDLENGUAJE));
		caracterSel.add((String) registro.get(CenClienteBean.C_CARACTER));
		
	} // Fin de recuperar datos si no estamos en Nuevo
	
	// Obteniendo si es sociedad SJ o SP
	Hashtable hDatosNoColegiado=(Hashtable)request.getSession().getAttribute("hashNoColegiadoOriginal");
	String valorSociedadSJ=UtilidadesHash.getString(hDatosNoColegiado,CenNoColegiadoBean.C_SOCIEDADSJ);
	String valorSociedadSP=UtilidadesHash.getString(hDatosNoColegiado,CenNoColegiadoBean.C_SOCIEDADSP);
	
	// Obteniendo tipo
	String tipo = formulario.getTipo();
	String tipoJY = formulario.getTipo();
	if(tipo == null)	{
		tipo ="";
		tipoJY ="";
	}
	String [] tipoParam = new String[1];
	tipoParam[0] = user.getLanguage();
	ArrayList tipoSel = new ArrayList();
	tipoSel.add(tipo);
	tipoOriginal = tipo;
	String tipoCliente=ClsConstants.TIPO_CLIENTE_NOCOLEGIADO;
	String [] caracterParam = new String[1];
	caracterParam[0] = tipoCliente;
	

	// Calculando estilos generales en funcion de Ver o Editar
	String estiloCaja="", estiloCombo="";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	if (formulario.getAccion().equalsIgnoreCase("VER") ) {
		// caso de consulta
		estiloCaja = "boxConsulta";
		estiloCombo = "boxConsulta";
		readonly = "true";
		breadonly = true;
		checkreadonly = " disabled ";
	} else {
		estiloCaja = "box";
		estiloCombo = "boxCombo";
		readonly = "false";
		breadonly = false;
		checkreadonly = " ";
	}
	
	// Calculando estilos para controlar edicion de datos generales de persona
	boolean bDatosGeneralesEditables = ((String) request.getAttribute("BDATOSGENERALESEDITABLES")).equals("true") ? true : false;

%>


<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.io.File"%>
<html>

<!-- HEAD -->
<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="datosGeneralesNoColegiadoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		  	
	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
		
		
	<!--Step 3 -->
	<!-- defaults for Autocomplete and displaytag -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
		  	
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

	<script>		
		<!-- Funcion asociada a buscarGrupos() -->
		function buscarGrupos(){
			document.GruposClienteClienteForm.modo.value="buscar";
			document.GruposClienteClienteForm.modoAnterior.value=document.forms[0].accion.value;				
			document.GruposClienteClienteForm.idPersona.value=document.forms[0].idPersona.value;	
			document.GruposClienteClienteForm.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.GruposClienteClienteForm.target="resultado";	
			document.GruposClienteClienteForm.submit();	
		}	
		
		function refrescarLocal() {
			<% if (modo.equalsIgnoreCase("NUEVASOCIEDAD")) { %>
			document.forms[0].accion.value="editar";
			<% } %>
			document.forms[0].modo.value="abrir";
			
			document.forms[0].submit();
				//Refresco el iframe de grupos:
			buscarGrupos();
		}
		
		function deshabilitarCheckSociedad () {			  
		 <%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>  
		  <% if(valorSociedadSP.equals("1")){%>
		    document.forms[0].sociedadSP.checked=true;
			jQuery("#sociedadSJ").attr("disabled","disabled");
			jQuery("#sociedadSP").attr("disabled","disabled");
			document.getElementById("contadorSP").style.display="block";
			document.getElementById("contadorSJ").style.display="none";			    
		  <% }
		  }%>
			if (document.getElementById("tipoIdentificacion").disabled==true){
		  		document.getElementById("tipoIdentificacion").focus();
		  	}
		}
		
		function refrescarLocal() {
			<% if (modo.equalsIgnoreCase("NUEVASOCIEDAD")) { %>
				document.forms[0].accion.value="editar";
			<% } %>
			document.forms[0].modo.value="abrir";
			document.forms[0].submit();
			buscarGrupos();
		}
			
			function deshabilitarCheckSociedad (){
			 <%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {  
			   	if(valorSociedadSP.equals("1")){%>
			     	document.forms[0].sociedadSP.checked=true;
					jQuery("#sociedadSJ").attr("disabled","disabled");
					jQuery("#sociedadSP").attr("disabled","disabled");
					  document.getElementById("contadorSP").style.display="block";
					  document.getElementById("contadorSJ").style.display="none";			    
			 <%}
			  }%>
			}
			
			function presentaContador(obj){
				<%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>//En modo edicion no se permite modificar el check de SJ y SP
			   		if (document.getElementById("sociedadSP").checked || document.getElementById("sociedadSJ").checked){
			   			document.getElementById("nombrenumregistro").style.display="block";
			   			if (document.getElementById("sociedadSP").checked){
			   				document.getElementById("sociedadSJ").checked=false;
			   			}
						jQuery("#sociedadSJ").attr("disabled","disabled");
			   		}
					<%if (modo.equalsIgnoreCase("VER")) {%>
						jQuery("#sociedadSJ").attr("disabled","disabled");
						jQuery("#sociedadSP").attr("disabled","disabled");
			   		<%}%>
				<%}%>
				
				if (obj){
					if (obj.checked){
						document.getElementById("sociedadSP").checked = false;
						
						if(document.getElementById("sociedadSJ").disabled==false){
							document.getElementById("sociedadSJ").checked = false;
				    	}
						obj.checked = true;
					}
				}
			
			if (document.getElementById("sociedadSP").checked) { 
				if (document.datosGeneralesForm.modoSociedadSP.value==0){// si el modo de sociedad es Registro			    		
					     document.getElementById("contadorSJ").style.display="none";
						 document.getElementById("contadorSP").style.display="block";
						 document.getElementById("numeroRegistro").style.display="block";
						 jQuery("#prefijoNumRegSP").attr("disabled","disabled");
						 jQuery("#sufijoNumRegSP").attr("disabled","disabled");
						 jQuery("#contadorNumRegSP").attr("disabled","disabled");
						 document.getElementById("etiquetaNumReg").style.display="block";			 		
		  		} else {// si el modo de sociedad es Histórico
						document.getElementById("contadorSJ").style.display="none";
						document.getElementById("contadorSP").style.display="block";
						document.getElementById("numeroRegistro").style.display="block";
						document.getElementById("etiquetaNumReg").style.display="block";
			    }
			} else {
			 	document.getElementById("contadorSJ").style.display="none";
				document.getElementById("contadorSP").style.display="none";				 	
			 	if (document.getElementById("sociedadSJ").checked) { 
					if (document.getElementById("modoSociedadSJ").value==0){// si el modo de sociedad es Registro			    		
					     document.getElementById("contadorSJ").style.display="block";
						 document.getElementById("contadorSP").style.display="none";
						 document.getElementById("numeroRegistro").style.display="block";
						 jQuery("#prefijoNumRegSP").attr("disabled","disabled");
						 jQuery("#sufijoNumRegSP").attr("disabled","disabled");
						 jQuery("#contadorNumRegSP").attr("disabled","disabled");
						 document.getElementById("etiquetaNumReg").style.display="block";				 		
		  			} else {// si el modo de sociedad es Histórico
						document.getElementById("contadorSJ").style.display="block";
						document.getElementById("contadorSP").style.display="none";
						document.getElementById("numeroRegistro").style.display="block";
						document.getElementById("etiquetaNumReg").style.display="block";
			   		}
			 	} else {
				 	document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="none";
			 	}
			}
			if (document.getElementById("sociedadSP").disabled==false){
		  		document.getElementById("sociedadSP").focus();
		  	}				
		}
		
		function presentaContadorAux(obj) {				
			<%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>//En modo edicion no se permite modificar el check de SJ y SP
		   		if (document.getElementById("sociedadSP").checked || document.getElementById("sociedadSJ").checked){
		   			document.getElementById("nombrenumregistro").style.display="block";
		   			//document.getElementById("sociedadSJ").disabled=true;			   			
		   			<%if (modo.equalsIgnoreCase("VER")) {%>
		   			   jQuery("#sociedadSP").attr("disabled","disabled");
					   jQuery("#sociedadSJ").attr("disabled","disabled");
					<%}%>
		   		}					
			<%}%>
			
			if (obj) {
				if (obj.checked) {
					document.getElementById("sociedadSP").checked = false;						
					if(document.getElementById("sociedadSJ").disabled==false){	
						document.getElementById("sociedadSJ").checked = false;
				    }
					obj.checked = true;
				}
			}
			
			if (document.getElementById("sociedadSP").checked) { 
				if (document.datosGeneralesForm.modoSociedadSP.value==0){// si el modo de sociedad es Registro			    		
					document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="block";
					document.getElementById("numeroRegistro").style.display="block";
					 jQuery("#prefijoNumRegSP").attr("disabled","disabled");
					 jQuery("#sufijoNumRegSP").attr("disabled","disabled");
					 jQuery("#contadorNumRegSP").attr("disabled","disabled");
					document.getElementById("etiquetaNumReg").style.display="block";			 		
		  		} else {// si el modo de sociedad es Histórico
					document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="block";
					document.getElementById("numeroRegistro").style.display="block";
					document.getElementById("etiquetaNumReg").style.display="block";
			    }
			} else {
			 	document.getElementById("contadorSJ").style.display="none";
				document.getElementById("contadorSP").style.display="none";				 	
			 	if (document.getElementById("sociedadSJ").checked) { 
					if (document.getElementById("modoSociedadSJ").value==0){// si el modo de sociedad es Registro			    		
					    document.getElementById("contadorSJ").style.display="block";
						document.getElementById("contadorSP").style.display="none";
						document.getElementById("numeroRegistro").style.display="block";
						 jQuery("#prefijoNumRegSP").attr("disabled","disabled");
						 jQuery("#sufijoNumRegSP").attr("disabled","disabled");
						 jQuery("#contadorNumRegSP").attr("disabled","disabled");
						document.getElementById("etiquetaNumReg").style.display="block";			 		
		  			} else {// si el modo de sociedad es Histórico
						document.getElementById("contadorSJ").style.display="block";
						document.getElementById("contadorSP").style.display="none";
						document.getElementById("numeroRegistro").style.display="block";
						document.getElementById("etiquetaNumReg").style.display="block";
			   		}
			 	} else {
				 	document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="none";
			 	}
			}
		}
			
									
			//Funcion que quita blancos a derecha e izquierda de la cadena
			function fTrim(Str)
			{				 
				Str = Str.replace(/(^\s*)|(\s*$)/g,"");			
				return Str;
			}
			
			

			function cambioTipo()
			{																									
				if (document.forms[0].modo.value == "nuevaSociedad" || document.forms[0].modo.value == "editar")
				{		
					document.forms[0].numIdentificacion.value = fTrim(document.forms[0].numIdentificacion.value);			
					if(document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "A"
						|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "B" 
						|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "F"
						|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "G"
						|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "J"
						) 
					{
							document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>";

							if (!(validarCIF(document.forms[0].numIdentificacion.value)))
							{
								//Ocultamos el select cuyo id es soloDos
								document.getElementById("soloDos").style.display="none";

								//mostramos el select cuyo id es todas	
								 document.getElementById("todas").style.display="block";
								
								document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
								document.forms[0].tipo.value = "0";
								return;
							}

							if(document.forms[0].modo.value == "nuevaSociedad" || document.forms[0].modo.value == "editar")
							{
								if (document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "A")
								{
									document.forms[0].tipo.value = "A";
									//Ocultamos el select cuyo id es soloDos	
									 document.getElementById("soloDos").style.display="none";
									//mostramos el select cuyo id es todas	
									 document.getElementById("todas").style.display="block";									
								}
								else if (document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "B")
								{
									document.forms[0].tipo.value = "B";
									//Ocultamos el select cuyo id es soloDos	
									 document.getElementById("soloDos").style.display="none";
									//mostramos el select cuyo id es todas	
									 document.getElementById("todas").style.display="block";
								}
								else if (document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "F")
								{
									document.forms[0].tipo.value = "F";
									//Ocultamos el select cuyo id es soloDos	
									 document.getElementById("soloDos").style.display="none";
									//mostramos el select cuyo id es todas	
									 document.getElementById("todas").style.display="block";
								}
								else if (document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "G")
								{
									document.forms[0].tipo.value = "G";
									//Ocultamos el select cuyo id es soloDos	
									 document.getElementById("soloDos").style.display="none";
									//mostramos el select cuyo id es todas	
									 document.getElementById("todas").style.display="block";
								}
								else if (document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "J")
								{
									 <%if (tipo.equals("J")){%>
									 	document.forms[0].tipoJY.value = "J";
									 	document.forms[0].tipo.value = "J";
									 <%}else{%>
									 	document.forms[0].tipoJY.value = "Y";
									 	document.forms[0].tipo.value = "Y";
									 <%}%>
									 //mostramos el select cuyo id es soloDos
									 document.getElementById("soloDos").style.display="block";
									//Ocultamos el select cuyo id es todas	
									 document.getElementById("todas").style.display="none";
								}
							}
					}
					else 
					{
						//Ocultamos el select cuyo id es soloDos
						document.getElementById("soloDos").style.display="none";
						//mostramos el select cuyo id es soloDos
						 document.getElementById("todas").style.display="block";

						document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
						document.forms[0].tipo.value = "0";
					}					
				}									
			}
			

		function adaptaTamanoFoto () 
		{
			widthMax = 180;
			heightMax = 240;
			foto = document.getElementById("fotoNueva");
			if (foto) {
				ratio = foto.height / foto.width;
	
				if (foto.width  > widthMax ) {
					foto.width  = widthMax;
					foto.height = widthMax * ratio;
				}
	
				if (foto.height > heightMax) { 
					foto.height = heightMax;
					foto.widtht = heightMax / ratio;
				}
			}
			return;
		}
				
		</script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.datosGenerales.cabecera" 
			localizacion="censo.fichaCliente.datosGenerales.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
</head>



<body class="tablaCentralCampos"  onload="adaptaTamanoFoto();buscarGrupos();presentaContador();cambioTipo();deshabilitarCheckSociedad();">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center"  cellspacing="0">
	<tr>
		<td class="titulitosDatos">
		<%	if (!formulario.getAccion().equalsIgnoreCase("NUEVASOCIEDAD")) { %>
					<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1"/>
					 &nbsp; 		
					 <% String nombreaux="";
					 	nombreaux=nombre;
					 	if (!apellido1.equals("#NA")){
					 		nombreaux=nombre+" "+ apellido1;
					 	}
					 	if (!apellido2.equals("#NA")){
					 		nombreaux=nombre+" "+ apellido2;
					 	}%>
					 	
					 <%=UtilidadesString.mostrarDatoJSP(nombreaux) %>
					 &nbsp; 		
			<% if (bColegiado) { %>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
					 <%= UtilidadesString.mostrarDatoJSP(numeroColegiado) %>
			<% } else { %>
					<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<% } %>
		<%	} else {  %>
					<siga:Idioma key="censo.altaDatosGenerales.cabecera"/>
		<%	}  %>
		</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->


	<!------------------------------->
	<!-- INICIO Tabla principal -->
	<!------------------------------->

<!------------------------------->
<!-- Tabla central principal   -->
<!------------------------------->
	<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" border="0">
		<html:form  action="/CEN_DatosGenerales.do" method="POST" target="mainPestanas"  enctype="multipart/form-data">
			<html:hidden name="datosGeneralesForm" property="modo"/>
			<html:hidden name="datosGeneralesForm" property="idInstitucion"/>
			<html:hidden name="datosGeneralesForm" property="idPersona"/>
			<html:hidden name="datosGeneralesForm" property="accion"/>
			<html:hidden name="datosGeneralesForm" property="motivo"/>
			<html:hidden name="datosGeneralesForm" property="actionModal" value=""/>
			<html:hidden name="datosGeneralesForm" property="abono" styleId="abono" value="B"/>
			<html:hidden name="datosGeneralesForm" property="cargo" styleId="cargo" value="B"/>
			<html:hidden name="datosGeneralesForm" property="idTratamiento"  value="1"/>
			<html:hidden name="datosGeneralesForm" property="modoSociedadSJ" styleId="modoSociedadSJ"/>
			<html:hidden name="datosGeneralesForm" property="modoSociedadSP" styleId="modoSociedadSP"/>
			<html:hidden name="datosGeneralesForm" property="longitudSP" styleId="longitudSP"/>
			<html:hidden name="datosGeneralesForm" property="continuarAprobacion" styleId="continuarAprobacion" value = ""/>
			
		<tr>
			<!-- COLUMNA: FOTO -->	
			<td valign="top" style="width:200px">
				<siga:ConjCampos leyenda="censo.consultaDatosGenerales.literal.imagencorporativa">
					<br>
					<% 	if (fotografia == null || fotografia.equals("")) { 
							fotografia = app + "/html/imagenes/usuarioDesconocido.gif";
						}
					%>
					<div style='height:245px; width:184px; overflow-x:auto; overflow-y:auto'>
						<center>
							<img id="fotoNueva" border="0" src="<%=fotografia%>" >	
						</center>
					</div>
					<br>&nbsp;		
					
					<% if (!breadonly) { %>
							<html:file name="datosGeneralesForm" styleId="foto" property="foto" size="8" styleClass="<%=estiloCaja %>" accept="image/gif,image/jpg" ></html:file>		
							<br>&nbsp;		
					<% } %>
				</siga:ConjCampos>
			</td>
			<!-- COLUMNA PRINCIPAL -->
			<td>
				<table border="0">
					<!-- FILA CONJUNTO1 -->
					<tr>
						<td>
							<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
								<table border=0 width="100%">
									<tr>
										
										<!-- NUMERO IDENTIFICACION NIF/CIF -->
										<% String CIF=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_CIF);
										   String OTRO=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_OTRO);
										%>
										<td class="labelText" colspan="2" width="40%">
											<siga:Idioma key="censo.fichaCliente.literal.identificacion"/>&nbsp;(*)

											<% if (!breadonly && bDatosGeneralesEditables) { %>
												<input type="hidden" name="tipoIdentificacionBloqueada" value="<%=tipoIdentificacionSel%>"/>
												<html:text name="datosGeneralesForm" property="numIdentificacion" styleId="numIdentificacion" size="11" style="width:70px" styleClass="box" value="<%=nIdentificacion %>" onblur="cambioTipo();"></html:text>
											<% } else { %>
												<html:text name="datosGeneralesForm" property="numIdentificacion" styleId="numIdentificacion" size="11" style="width:70px" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" ></html:text>
											<% }  %>
												
											<% if (!breadonly && bDatosGeneralesEditables) { %>
												<html:select name="datosGeneralesForm" property="tipoIdentificacion" value="<%=tipoIdentificacionSel%>" styleId="tipoIdentificacion"  styleClass="boxCombo" disabled="true">													
													<html:option value="<%=OTRO%>"> <siga:Idioma key="censo.fichaCliente.literal.otro"/></html:option>	
													<html:option value="<%=CIF%>" > <siga:Idioma key="censo.fichaCliente.literal.cif"/></html:option>
												</html:select>
											<% } else { %>
												<html:hidden name="datosGeneralesForm" property="tipoIdentificacion" styleId="tipoIdentificacion" value="<%=tipoIdentificacionSel%>"/>
												<% if (tipoIdentificacionSel.equals(CIF)) { %>
												<siga:Idioma key="censo.fichaCliente.literal.cif"/>
												<% } else if (tipoIdentificacionSel.equals(OTRO)) { %>
												<siga:Idioma key="censo.fichaCliente.literal.otro"/>
												<% } %>
											<% }  %>
												
											
										</td>


										<td class="labelText" width="40%" id="todas">
											<%
												 String tipoEstilo="boxCombo";
												 String tipoReadOnly="false";
												 String tipoCombo="cmbTipoSociedadAlta";
												 tipoDisabled = "true";
												
												if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER") || modo.equalsIgnoreCase("nuevaSociedad")) {												
													if(tipo.equalsIgnoreCase("J") || tipo.equalsIgnoreCase("Y")){
														tipoEstilo="boxCombo";
														tipoReadOnly="false";
														tipoCombo="cmbTipoSociedadJ";														
														//tipoDisabled = "true";
													}else{
														tipoEstilo="boxConsulta";
														tipoReadOnly="true";
														tipoCombo="cmbTipoSociedadAlta";														
														//tipoDisabled = "true";
													}
												}
											%>  
											<siga:Idioma key="censo.general.literal.tipoRegistro"/>&nbsp;(*) 
											<input type="hidden" id="tipoOriginal"  name="tipoOriginal" value="<%=tipoOriginal%>">

											
											<% if (tipoDisabled.equals("false")) {%>
												<html:select styleId="tipos" styleClass="boxCombo" style="width:200px;" property="tipo" disabled="false">
													<bean:define id="tipos" name="datosGeneralesForm" property="tipos" type="java.util.Collection" />
													<html:optionsCollection name="tipos" value="letraCif" label="descripcion" />
												</html:select>
											
											<% } else {%>
												<html:select styleId="tipos" styleClass="boxCombo" style="width:200px;" property="tipo" disabled="true">
													<bean:define id="tipos" name="datosGeneralesForm" property="tipos" type="java.util.Collection" />
													<html:optionsCollection name="tipos" value="letraCif" label="descripcion" />
												</html:select>
											<% } %>
										
										</td>
										
										<!-- Combo que muestra solo Soc.Civil (J) y  Soc.Civil (J) IRPF Reducido (7%)-->										
										<td class="labelText" width="40%" id="soloDos">
											
											<siga:Idioma key="censo.general.literal.tipoRegistro"/>&nbsp;(*) 
											<input type="hidden" name="tipoOriginalJY" value="<%=tipoOriginal%>">
											 
											 <html:select styleId="tiposJY" styleClass="boxCombo" style="width:200px;" property="tipoJY" disabled="false">
													<bean:define id="tiposJY" name="datosGeneralesForm" property="tiposJY" type="java.util.Collection" />
													<html:optionsCollection name="tiposJY" value="letraCif" label="descripcion" />
											 </html:select>																																
										</td>
										
																																					
										<td class="labelText" width="10%">
											<!-- Fecha Alta -->
											<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaAlta"/>
										</td>
										<td>	
											<html:text name="datosGeneralesForm" property="fechaAlta" styleId="fechaAlta" style="width:70" styleClass="boxConsulta" readonly="true" value="<%=fechaAlta%>" />
										</td>
									</tr>
									</table>
									<table border=0 width="100%">
									<tr>
										<td class="labelText" colspan="1" width="20%">
											<!-- CheckBox de Sociedad SJ -->
											<siga:Idioma key="censo.general.literal.sociedadSJ"/>&nbsp;
											<html:checkbox name="datosGeneralesForm" property="sociedadSJ" styleId="sociedadSJ" onclick="presentaContadorAux(this)" value="<%=ClsConstants.DB_TRUE%>"    ></html:checkbox>
										</td>	
										<td class="labelText" colspan="2" width="40%">
											<!-- CheckBox de Sociedad Profesional -->
											<siga:Idioma key="censo.general.literal.sociedadProfesional"/>&nbsp;
											<html:checkbox name="datosGeneralesForm" property="sociedadSP" styleId="sociedadSP" onclick="presentaContadorAux(this)" value="<%=ClsConstants.DB_TRUE%>"   ></html:checkbox> 
										</td>
										<span id="etiquetaNumReg" > 
										<td id="nombrenumregistro" class="labelText" colspan="2">
											<!-- NUMERO REGISTRO -->
											<span id="numeroRegistro" >	
												<span id="contadorSP" style="display:none">
													<siga:Idioma key="censo.general.literal.numRegistro"/>&nbsp;(*) 
													<html:text name="datosGeneralesForm" property="prefijoNumRegSP" styleId="prefijoNumRegSP" size="5" maxlength="8" styleClass="<%=estiloCaja%>" style="width:55px" readonly="<%=breadonly%>" ></html:text>
													<html:text name="datosGeneralesForm" property="contadorNumRegSP" styleId="contadorNumRegSP" size="5" maxlength="8" style="width:58px" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
													<html:text name="datosGeneralesForm" property="sufijoNumRegSP" styleId="sufijoNumRegSP" style="width:58px" size="5" maxlength="8" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
												</span>
												<span id="contadorSJ" style="display:none">
													<siga:Idioma key="censo.general.literal.numRegistro"/>&nbsp;(*) 
													<html:text name="datosGeneralesForm" property="prefijoNumRegSJ" styleId="prefijoNumRegSJ" size="5" maxlength="8" styleClass="<%=estiloCaja%>" style="width:55px" readonly="<%=breadonly%>" ></html:text>
													<html:text name="datosGeneralesForm" property="contadorNumRegSJ" styleId="contadorNumRegSJ" size="5" maxlength="8" style="width:58px" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
													<html:text name="datosGeneralesForm" property="sufijoNumRegSJ" styleId="sufijoNumRegSJ" style="width:58px" size="5" maxlength="8" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
												</span>
											</span>
						     			</td>
										</span>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
					<!-- FILA CONJUNTO2 -->
					<tr>
						<td>
							<siga:ConjCampos leyenda="censo.general.literal.informacionOrganizacion">
								<table class="tablaCampos" align="center" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td class="labelText" width="90px">
											<!-- DENOMINACION -->
											<siga:Idioma key="censo.consultaDatosGenerales.literal.denominacion"/>&nbsp;(*)
										</td>
										<td style="width:240px">
											<% if (!breadonly && bDatosGeneralesEditables) { %>
												<html:text name="datosGeneralesForm" property="denominacion" styleId="denominacion" size="40" maxlength="100" styleClass="box"></html:text>
											<% } else { %>
												<html:text name="datosGeneralesForm" property="denominacion" styleId="denominacion" size="40" maxlength="100" styleClass="boxConsulta" readonly="true" ></html:text>
											<% }  %>
										</td>
										<td class="labelText">
											<!-- Abreviatura -->
											<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;
										</td>
										<td  class="labelText">
											<% if (!breadonly && bDatosGeneralesEditables) { %>
												<html:text name="datosGeneralesForm" property="abreviatura" styleId="abreviatura" size="15" maxlength="100" styleClass="box"></html:text>
											<% } else { %>
												<html:text name="datosGeneralesForm" property="abreviatura" styleId="abreviatura" size="15" maxlength="100" styleClass="boxConsulta" readonly="true" ></html:text>
											<% }  %>
										</td>
										<td class="labelText">
											<% if (!bDatosGeneralesEditables) { %> 
												<img src="<%=app%>/html/imagenes/help.gif" 
													alt="<siga:Idioma key='censo.consultaDatosGenerales.mensaje.noEditable'/>" 
													onclick="alertaNoEditable();"
													style="cursor: hand;"/> 
											<% } %>
										</td>
									</tr>
									<tr>

									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
					<!-- INFORMACION ADICIONAL -->
					<tr>
						<td>
							<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
								<tr>
									<td>
										<siga:ConjCampos leyenda="censo.general.literal.informacionAdicional">
											<table class="tablaCampos" cellpadding="0" cellpadding="0">
												<tr>
													<td class="labelText" style="width:100px">
														<!-- IDIOMA -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.idioma"/>&nbsp;(*)
													</td>
													<td style="width:320px">
														<siga:ComboBD nombre = "idioma" tipo="cmbIdiomaInstitucion" parametro="<%=institucionParam%>" clase="<%=estiloCombo%>" obligatorio="true" elementoSel="<%=idiomaSel %>"  readonly="<%=readonly %>" obligatorioSinTextoSeleccionar="true" />
														&nbsp;
													</td>
												</tr>
												<tr>
													<td class="labelText" style="width:100px">				
														<!-- Restriccion Visibilidad o caracter -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.caracter"/>&nbsp;
													</td>
													<td style="width:320px">
														<siga:ComboBD nombre = "caracter" tipo="cmbCaracter" clase="boxConsulta" obligatorio="false" parametro="<%=caracterParam %>" elementoSel="<%=caracterSel %>" readonly="true" obligatorioSinTextoSeleccionar="true" />
													</td>
												</tr>
												<tr>
													<td class="labelText" colspan="2">
														<!-- GUIA JUDICIAL -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.guiaJudicial"/>
														&nbsp;
														<% if (guiaJudicial.equals(ClsConstants.DB_TRUE)) { %>
															<input type="checkbox" name="guiaJudicial" id="guiaJudicial" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="guiaJudicial" id="guiaJudicial" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
														<% } %>
														&nbsp;
														<!-- PUBLICIDAD -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.publicidad"/>
														<% if (publicidad.equals(ClsConstants.DB_TRUE)) { %>
															<input type="checkbox" name="publicidad" id="publicidad" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="publicidad" id="publicidad" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
														<% } %>
														&nbsp;
									
														<!-- COMISIONES -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.comisiones"/>
														&nbsp;
														<% if (comisiones.equals(ClsConstants.DB_TRUE)) { %>
															<input type="checkbox" name="comisiones" id="comisiones" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="comisiones" id="comisiones" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
														<% } %>
													</td>
												</tr>
												<tr>
													<td class="labelText" style="width:100px">
														<!-- CUENTA CONTABLE -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.cuentaContable"/>
													</td>
													<td style="width:320px">
														<% if (!user.isLetrado()) { %>
																<html:text name="datosGeneralesForm" property="cuentaContable" styleId="cuentaContable" size="10" styleClass="<%=estiloCaja %>" readonly="<%=breadonly %>" value="<%=cuentaContable %>"></html:text>
														<% } else { %>
																<html:hidden name="datosGeneralesForm" property="cuentaContable" styleId="cuentaContable" value="<%=cuentaContable %>"></html:hidden>
														<% }  %>
													</td>
												</tr>
												<tr>
													<td class="labelText" style="width:100px">
														<siga:Idioma key="censo.general.literal.anotaciones"/>
													</td>
													<td style="width:320px">
														<html:textarea name="datosGeneralesForm" property="anotaciones" styleId="anotaciones" onKeyDown="cuenta(this,2000)" onChange="cuenta(this,2000)" rows="2" style="width:300px" styleClass="<%=estiloCaja %>" readonly="<%=breadonly %>" />
													</td>
												</tr>
											</table>
										</siga:ConjCampos>		
									</td>
									<td  style="width:300px">
											<!-- INICIO: IFRAME LISTA RESULTADOS -->
											<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
															id="resultado"
															name="resultado" 
															scrolling="no"
															frameborder="1"
															marginheight="0"
															marginwidth="0";					 
															style="width:100%; height:100%;">
											</iframe>
											<!-- FIN: IFRAME LISTA RESULTADOS -->
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table><!-- Tabla columna principal -->	
			</td>
		</tr>
		<input type="hidden" name="prefijoOld" id="prefijoOld" value="">
   		<input type="hidden" name="sufijoOld" id="sufijoOld" value="">
   		<input type="hidden" name="tipoBloqueado" value="">
   		   		
		</html:form>
	</table>

<% 
	// BOTONES ACCION
	String botonesAccion = "V";
	if (modo.equalsIgnoreCase("NUEVASOCIEDAD") || modo.equalsIgnoreCase("EDITAR")) {
		botonesAccion+=",G,R";
	}
%>

	<siga:ConjBotonesAccion botones="<%=botonesAccion%>"  modo="<%=modo%>"  clase="botonesDetalle" />

	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="resultado" >
		<html:hidden name="GruposClienteClienteForm" property="modo" value="buscar"/>
		<html:hidden name="GruposClienteClienteForm" property="idPersona" />
		<html:hidden name="GruposClienteClienteForm" property="idInstitucion" />
		<html:hidden name="GruposClienteClienteForm" property="modoAnterior" />
	</html:form>


	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script type="text/javascript">

		// Muestra alerta de no editables los datos generales
		function alertaNoEditable() {
			alert('<siga:Idioma key="censo.consultaDatosGenerales.mensaje.noEditable"/>', 'info');
		}
		
		//Método que valida el CIF de una nueva sociedad
		function validarCIF(cif)
		{
        	var pares = 0; 
        	var impares = 0; 
        	var suma; 
        	var ultima; 
        	var unumero;
        	var letraCif = new Array("J", "A", "B", "X","X","X","F", "G","X","X");  
        	//var letraCif = new Array("J", "A", "B", "C","D","E","F", "G","H","I");  
        	var cadenaCif; 
         
        	cif = cif.toUpperCase(); 
         
        	var regular = new RegExp(/^[ABCDEFGHJKLMNPQS]\d\d\d\d\d\d\d[0-9,A-J]$/g); 
           	if (!regular.exec(cif)) 
               	return false; 
                
           ultima = cif.substr(8,1); 
           
			
           for (var cont = 1 ; cont < 7 ; cont ++) { 
        	   cadenaCif = (2 * parseInt(cif.substr(cont++,1))).toString() + "0"; 
               impares += parseInt(cadenaCif.substr(0,1)) + parseInt(cadenaCif.substr(1,1)); 
               pares += parseInt(cif.substr(cont,1)); 
           } 
           cadenaCif = (2 * parseInt(cif.substr(cont,1))).toString() + "0"; 
           impares += parseInt(cadenaCif.substr(0,1)) + parseInt(cadenaCif.substr(1,1)); 
            
           suma = (pares + impares).toString(); 
           unumero = parseInt(suma.substr(suma.length - 1, 1)); 
           unumero = (10 - unumero).toString(); 
           if(unumero == 10) 
               unumero = 0; 
           
           if ((ultima == unumero) ||  (ultima == letraCif[unumero]))
           {
          // if (ultima == letraCif[unumero])
           //{
         	if(document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "A"
					|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "B" 
					|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "F"
					|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "G"
					|| document.forms[0].numIdentificacion.value.charAt(0).toUpperCase()== "J"
					) 
				{
        	   	return true;
				}
           		else
        		   	return false;
           } 
           else 
           {
               return false;
           } 
    	}//fin método 
			

		// Asociada al boton SolicitarModificacion
		function accionSolicitarModificacion() {		
			document.forms[0].modo.value="abrirSolicitud";
			ventaModalGeneral(document.forms[0].name,'P');
		}

		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();	
		}
		
		function validarFormulario() {			
			//Valido el formulario:
			if (validateDatosGeneralesNoColegiadoForm(document.forms[0])) {
						   			
				<% if (!breadonly && bDatosGeneralesEditables) { %>
					// Se compruba si el Cif s valido para poner el tipo de identificación a CIF o Otros según corresponda
					if ((validarCIF(document.forms[0].numIdentificacion.value)))
					{
						// Si el CIF no es valido en el campo tipoIdentificacion se pone OTROS
						document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>";
						document.forms[0].tipoIdentificacionBloqueada.value = "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>";
						var letraNumIdentificacion = document.forms[0].numIdentificacion.value.charAt(0);
						document.forms[0].tipoBloqueado.value = letraNumIdentificacion;
					}
			    	else
			    	{
			    		document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
			    		document.forms[0].tipo.value = "0";
			    	}
					// El tipo de identificacion debe ser CIF:
					if (document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>") 
					{
						if (!(validarCIF(document.forms[0].numIdentificacion.value)))
						{
							// Si el CIF no es valido en el campo tipoIdentificacion se pone OTROS
							document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
							document.forms[0].tipo.value = "0";

							document.forms[0].tipoIdentificacionBloqueada.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
				    		document.forms[0].tipoBloqueado.value = "0";
				    		
							return true;
						}
						else
						{
							//Validamos el formato del CIF:
							var tipo = document.forms[0].tipos.value;
							var tipoJY = document.forms[0].tiposJY.value;								
							var numIdentificacion = document.forms[0].numIdentificacion.value.charAt(0);
							
							//Esta validacion comprueba que el identificador pertenece al Tipo Identificador en este caso de CIF
							if(numIdentificacion.toUpperCase() !='A' && numIdentificacion.toUpperCase() !='B' 
								&& numIdentificacion.toUpperCase() !='F' && numIdentificacion.toUpperCase() !='G'
									&& numIdentificacion.toUpperCase() !='J')
							{
								alert('<siga:Idioma key="censo.fichaCliente.literal.errorTipoIdent"/>');									
								return false;
							}
						
							<% if (tipoDisabled.equals("false")) { %>
							if((numIdentificacion.toUpperCase() != tipo))
							{
								if(!((numIdentificacion.toUpperCase()=='J') && (tipo == 'J' || tipo == 'Y')))
								{
									alert('<siga:Idioma key="censo.fichaCliente.literal.errorTipoIdent"/>');
									return false;
								}
							}
							<%}%>
																							
							//si el usuario ha elegido el tipo de CIF otros, se debe comprobar que el cif no se corresponde con
							//ninguno de los otros tipos definidos en la lista
							if ( tipo=="<%=ClsConstants.COMBO_TIPO_OTROS%>")
							{
								var i = 0;
								var esTipoPredefinido = false;
								while ((i < document.forms[0].tipos.length) && (esTipoPredefinido == false))
								{
									if (numIdentificacion == document.forms[0].tipos[i].value)
									{
										esTipoPredefinido = true;
									}
									i++;
								} 
								if (esTipoPredefinido)
								{
									alert('<siga:Idioma key="censo.fichaCliente.literal.avisoOtrosCIF"/>');
									return false;
								}
								else
									return true;
							}			
																	
							//El tipo debe ser igual a la primera letra del cif 
							else if  (tipo!="<%=ClsConstants.COMBO_TIPO_OTROS%>" && (tipo.toUpperCase()==numIdentificacion.toUpperCase()) ) 
							{
								return true;
							} 
							else 
							{
								if(!document.forms[0].modo.value == "editar")
								{
									alert ('<siga:Idioma key="censo.fichaCliente.literal.errorCIF"/>');
									return false;
								}									
								document.forms[0].tipoOriginal.value=document.forms[0].numIdentificacion.value.charAt(0).toUpperCase();									  									
								return true;
							}
						}
					} 
					else // Si el tipoIdentificacion es OTROS
					{
						if ((validarCIF(document.forms[0].numIdentificacion.value)))
						{
							// Si el CIF no es valido en el campo tipoIdentificacion se pone OTROS
							document.forms[0].tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>";
							document.forms[0].tipoIdentificacionBloqueada.value = "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>";

							return true;
						}
																

						
						if (document.forms[0].tipoIdentificacion.value != "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") 
						{
							alert ('<siga:Idioma key="censo.fichaCliente.literal.avisoCIF"/>');
							return false;
						} 
						else
						{
							document.forms[0].tipoIdentificacionBloqueada.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
				    		document.forms[0].tipoBloqueado.value = "0";
							return true;
						}	
					}
				<% } // if EDITAR  %>				  
				return true;
			} else {
				return false;
			}
		}
		
		// Asociada al boton Guardar
		function accionGuardar() {		
			//Valido el formulario:			
			sub();
			if (document.getElementById("longitudSP").value < document.getElementById("contadorNumRegSP").value.length){
			  alert('<siga:Idioma key="messages.contador.error.longitudSuperada"/>');
			  fin();
			  return false;
			}			
			
			if (document.getElementById("contadorNumRegSP").value=="" && document.getElementById("sociedadSP").checked){
				var mensaje='<siga:Idioma key="messages.contador.error.contadorObligatorio"/>';
				 alert (mensaje);
				 fin();
				 return false;
			}			
			
			if (document.getElementById("contadorNumRegSJ").value=="" && document.getElementById("sociedadSJ").checked){
				var mensaje='<siga:Idioma key="messages.contador.error.contadorObligatorio"/>';
				 alert (mensaje);
				 fin();
				 return false;
			}		
			
			if (validarFormulario()) {
			<% if (!modo.equalsIgnoreCase("NUEVASOCIEDAD")) { %>		
					<% if (!bOcultarHistorico) { %>
						var datos = showModalDialog("<%=app%>/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
						window.top.focus();
					<% } else { %>
							var datos = new Array();
							datos[0] = 1;
							datos[1] = "";
					<% } %>					
					if (datos[0] == 1) { // Boton Guardar
					    // document.forms[0].tipoIdentificacion.value=< %=tipoIdentificacionSel%>;					
						document.forms[0].motivo.value = datos[1];
						document.forms[0].target="submitArea2";
						document.forms[0].modo.value="modificarSociedad";
						//document.forms[0].tipo.value = document.forms[0].tipos.value;
						if (document.getElementById("sociedadSP").checked) {
							 jQuery("#prefijoNumRegSP").removeAttr("disabled");
							 jQuery("#sufijoNumRegSP").removeAttr("disabled");
							 jQuery("#contadorNumRegSP").removeAttr("disabled");
							 jQuery("#sociedadSP").removeAttr("disabled");
						}
						if (document.getElementById("sociedadSJ").checked) {
							 jQuery("#prefijoNumRegSP").removeAttr("disabled");
							 jQuery("#sufijoNumRegSP").removeAttr("disabled");
							 jQuery("#contadorNumRegSP").removeAttr("disabled");
							 jQuery("#sociedadSP").removeAttr("disabled");
						}						
						document.forms[0].submit();	
					} else {
						fin();
					 	return false;
					}				
			<%	} else { %>			
					if (document.getElementById("sociedadSP")){
					    if (document.datosGeneralesForm.modoSociedadSP.value==0){// cuando estamos en modo registro antes de enviar los datos los inicializamos a blanco porque 
																			   // si no se produce un error de inserccion.		
							 jQuery("#prefijoNumRegSP").removeAttr("disabled");
							 jQuery("#sufijoNumRegSP").removeAttr("disabled");
							 jQuery("#contadorNumRegSP").removeAttr("disabled");																			   
		 	
						 }
					} 
					if (document.getElementById("sociedadSJ")){
					  	if (document.getElementById("modoSociedadSJ").value==0){// cuando estamos en modo registro antes de enviar los datos los inicializamos a blanco porque 
																		   // si no se produce un error de inserccion.
							 jQuery("#prefijoNumRegSP").removeAttr("disabled");
							 jQuery("#sufijoNumRegSP").removeAttr("disabled");
							 jQuery("#contadorNumRegSP").removeAttr("disabled");																			   
					  	}		
				 	}					
					document.forms[0].target="submitArea2";
					document.forms[0].modo.value="insertarSociedad";
					document.forms[0].submit();	
			<%	}  %>
			} else {
				fin();
				return false;
			}  // Fin de validar el formulario
		}		
	</script>


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
