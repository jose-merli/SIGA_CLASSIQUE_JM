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
	String modo = null;
	String busquedaVolver = "";

	ArrayList gruposSel = new ArrayList();	
	String cliente = "";
	String numeroColegiado = "";
	boolean bColegiado = false;
	boolean bfCertificado=true;
	String fechaCertificado = "";
	Vector resultado = null;
	String consultaPersona = null;
	boolean bConsultaPersona = false;
	String colegiado = null;

	String estiloCaja="", estiloCombo="";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	
	String [] institucionParam = new String[1];
	
	// seleccion de combos
	ArrayList tratamientoSel = new ArrayList();
	//ArrayList tipoIdentificacionSel = new ArrayList();
	String tipoIdentificacionSel = "20";
	ArrayList estadoCivilSel = new ArrayList();
	ArrayList idiomaSel = new ArrayList();
	ArrayList caracterSel = new ArrayList();
	ArrayList actividadSel = new ArrayList();
	
	Hashtable hDatosNoColegiado=(Hashtable)request.getSession().getAttribute("hashNoColegiadoOriginal");
	String valorSociedadSJ=UtilidadesHash.getString(hDatosNoColegiado,CenNoColegiadoBean.C_SOCIEDADSJ);
	String valorSociedadSP=UtilidadesHash.getString(hDatosNoColegiado,CenNoColegiadoBean.C_SOCIEDADSP);
	String app = null;
	HttpSession ses = null;
	Properties src = null;
	UsrBean user = null;
	DatosGeneralesForm formulario = null;
	boolean bOcultarHistorico = false;
	boolean tipoIdentif=false;
	try {
		app=request.getContextPath();
		ses=request.getSession();
		src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
		user = (UsrBean) ses.getAttribute("USRBEAN");
		bOcultarHistorico = user.getOcultarHistorico();
	
		formulario = (DatosGeneralesForm)request.getAttribute("datosGeneralesForm");
//		modo = formulario.getModo();
		modo = (String)request.getAttribute("modoPestanha");
	
		// para saber hacia donde volver
		busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
		if (busquedaVolver==null) {
			busquedaVolver = "volverNo";
		}
	
		// RGG 24-06-2005 cambio para controlar acceso a datos persona otra institucion que la creadora
		consultaPersona = (String) request.getAttribute("CenDatosPersonalesOtraInstitucion");

		// Solo se podra modificar el tipo de identificacion si el no colegiado es la propia institucion y nos encontramos en dicha institucion o si el no colegiado
		// ha sido dado de alta por la institucion donde nos encontramos.
		
		            CenInstitucionAdm insAdm= new CenInstitucionAdm(user);
					Hashtable hashIns= new Hashtable();
					hashIns.put(CenInstitucionBean.C_IDINSTITUCION,user.getLocation());
					Vector v2 = insAdm.selectByPK(hashIns);
					String idPersonaInstitucionUserBean="";
					if (v2!=null && v2.size()>0) {
						CenInstitucionBean insBean = (CenInstitucionBean) v2.get(0);
						idPersonaInstitucionUserBean=insBean.getIdPersona().toString();
					}	
					
		
		if (consultaPersona!=null && consultaPersona.equals(ClsConstants.DB_TRUE) && ((!idPersonaInstitucionUserBean.equals(formulario.getIdPersona()) && formulario.getIdPersona().length()<4)) ) {
			bConsultaPersona = true;
		}



		
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
		
			colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
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
		
		
		//  tratamiento de readonly
		estiloCaja = "";
		readonly = "false";  // para el combo
		breadonly = false;  // para lo que no es combo
		checkreadonly = " "; // para el check
		
		// caso de accion 
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
		
		//Toma los de la institucion:
		if (formulario.getIdInstitucion()!= null && !formulario.getIdInstitucion().equals(""))
	   		institucionParam[0] = formulario.getIdInstitucion();
	   	else
		   	institucionParam[0] = user.getLocation();
	} catch (Exception e){
	
	}

	// No estamos en nuevo: recuperamos los datos
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
	
	//TIPO:
	String tipo = formulario.getTipo();
	
	String [] tipoParam = new String[1];
	tipoParam[0] = user.getLanguage();
	ArrayList tipoSel = new ArrayList();
	tipoSel.add(tipo);
	
	
//	String salidaTipo = "";
//	if (tipo!=null && !tipo.equals("")) {
//		if (tipo.equals(ClsConstants.COMBO_TIPO_SOCIEDAD_CIVIL))
//			salidaTipo = "censo.general.literal.SociedadCivil";
//		else if(tipo.equals(ClsConstants.COMBO_TIPO_SOCIEDAD_LIMITADA))
//					salidaTipo = "censo.general.literal.SociedadLimitada";
//		else if(tipo.equals(ClsConstants.COMBO_TIPO_SOCIEDAD_ANONIMA))
//					salidaTipo = "censo.general.literal.SociedadAnonima";
//		else if(tipo.equals(ClsConstants.COMBO_TIPO_PERSONAL))
//					salidaTipo = "censo.general.literal.Personal";
//		else if(tipo.equals(ClsConstants.COMBO_TIPO_OTROS))
//					salidaTipo = "censo.general.literal.Otros";
//	}
	
	// Numero de Registro:
	/*String numeroRegistro = "";
	if (formulario.getNumeroRegistro()!=null && !formulario.getNumeroRegistro().equals(""))
		numeroRegistro = formulario.getNumeroRegistro();*/

	boolean breadonlyNif = false;
	String estiloCajaNif = "box";
	boolean breadonlyNombreApellidos = false;
	String estiloCajaNombreApellidos = "box";
		
	if (formulario.getAccion().equals("ver")) {
		breadonlyNif = true;
		estiloCajaNif = "boxConsulta";
		breadonlyNombreApellidos = true;
		estiloCajaNombreApellidos = "boxConsulta";
	} else {
	
		if (idPersona.length()>4) {
			if (!idPersona.substring(0,4).equals(user.getLocation())) {
				breadonlyNif = true;
				estiloCajaNif = "boxConsulta";
				breadonlyNombreApellidos = true;
				estiloCajaNombreApellidos = "boxConsulta";
				tipoIdentif=true;
			}   
		} 
	}



// CONTROL DE TIPO	
String tipoCliente=ClsConstants.TIPO_CLIENTE_NOCOLEGIADO;
String [] caracterParam = new String[1];
caracterParam[0] = tipoCliente;
	

%>


<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.io.File"%>
<html>

<!-- HEAD -->
<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		

		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="datosGeneralesNoColegiadoForm" staticJavascript="false" />  
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
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
			
			function deshabilitarCheckSociedad (){
			
			  
			 <%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>  
			  <% if(valorSociedadSP.equals("1")){%>
			     document.forms[0].sociedadSP.checked=true;
			
					  document.getElementById("sociedadSJ").disabled=true;
					  document.getElementById("sociedadSP").disabled=true;
					  document.getElementById("contadorSP").style.display="block";
					  document.getElementById("contadorSJ").style.display="none";
			    
			  <% }
			  }%>
			  if (datosGeneralesForm.tipoIdentificacion.disabled==true){
			  	datosGeneralesForm.tipoIdentificacion.focus();
			  }
			}
			
			function presentaContador(obj)
			{  
				
				<%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>//En modo edicion no se permite modificar el check de SJ y SP
			   		if (document.getElementById("sociedadSP").checked || document.getElementById("sociedadSJ").checked){
			   			document.getElementById("nombrenumregistro").style.display="block";
			   			if (document.getElementById("sociedadSP").checked){
			   				document.getElementById("sociedadSJ").checked=false;
			   			}
			   			document.getElementById("sociedadSJ").disabled=true;
			   			
			   			
			   		}
					<%if (modo.equalsIgnoreCase("VER")) {%>
			   				document.getElementById("sociedadSP").disabled=true;
			   				document.getElementById("sociedadSJ").disabled=true;
			   		<%}%>
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
						     numeroRegistro.style.display="block";
							 datosGeneralesForm.prefijoNumRegSP.disabled=true;
							 datosGeneralesForm.sufijoNumRegSP.disabled=true;
							 datosGeneralesForm.contadorNumRegSP.disabled=true;
							 etiquetaNumReg.style.display="block";
						
				 		
			  		}else{// si el modo de sociedad es Histórico
							document.getElementById("contadorSJ").style.display="none";
							document.getElementById("contadorSP").style.display="block";
						    numeroRegistro.style.display="block";
							etiquetaNumReg.style.display="block";
				    }
				 }else{
				 	document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="none";
				 	
				 	if (document.getElementById("sociedadSJ").checked) { 
						if (document.datosGeneralesForm.modoSociedadSJ.value==0){// si el modo de sociedad es Registro
			    		
						     document.getElementById("contadorSJ").style.display="block";
							 document.getElementById("contadorSP").style.display="none";
						     numeroRegistro.style.display="block";
							 datosGeneralesForm.prefijoNumRegSJ.disabled=true;
							 datosGeneralesForm.sufijoNumRegSJ.disabled=true;
							 datosGeneralesForm.contadorNumRegSJ.disabled=true;
							 etiquetaNumReg.style.display="block";
						
				 		
			  			}else{// si el modo de sociedad es Histórico
							document.getElementById("contadorSJ").style.display="block";
							document.getElementById("contadorSP").style.display="none";
						    numeroRegistro.style.display="block";
							etiquetaNumReg.style.display="block";
				   		}
				 	}else{
					 	document.getElementById("contadorSJ").style.display="none";
						document.getElementById("contadorSP").style.display="none";
				 	}
				 }
				 if (datosGeneralesForm.sociedadSP.disabled==false){
			  		datosGeneralesForm.sociedadSP.focus();
			  	}
				
			}
			
			function presentaContadorAux(obj)
			{  
				
				<%if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {%>//En modo edicion no se permite modificar el check de SJ y SP
			   		if (document.getElementById("sociedadSP").checked || document.getElementById("sociedadSJ").checked){
			   			document.getElementById("nombrenumregistro").style.display="block";
			   			//document.getElementById("sociedadSJ").disabled=true;
			   			
			   			<%if (modo.equalsIgnoreCase("VER")) {%>
			   				document.getElementById("sociedadSP").disabled=true;
			   				document.getElementById("sociedadSJ").disabled=true;
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
						     numeroRegistro.style.display="block";
							 datosGeneralesForm.prefijoNumRegSP.disabled=true;
							 datosGeneralesForm.sufijoNumRegSP.disabled=true;
							 datosGeneralesForm.contadorNumRegSP.disabled=true;
							 etiquetaNumReg.style.display="block";
						
				 		
			  		}else{// si el modo de sociedad es Histórico
							document.getElementById("contadorSJ").style.display="none";
							document.getElementById("contadorSP").style.display="block";
						    numeroRegistro.style.display="block";
							etiquetaNumReg.style.display="block";
				    }
				 }else{
				 	document.getElementById("contadorSJ").style.display="none";
					document.getElementById("contadorSP").style.display="none";
				 	
				 	if (document.getElementById("sociedadSJ").checked) { 
						if (document.datosGeneralesForm.modoSociedadSJ.value==0){// si el modo de sociedad es Registro
			    		
						     document.getElementById("contadorSJ").style.display="block";
							 document.getElementById("contadorSP").style.display="none";
						     numeroRegistro.style.display="block";
							 datosGeneralesForm.prefijoNumRegSJ.disabled=true;
							 datosGeneralesForm.sufijoNumRegSJ.disabled=true;
							 datosGeneralesForm.contadorNumRegSJ.disabled=true;
							 etiquetaNumReg.style.display="block";
						
				 		
			  			}else{// si el modo de sociedad es Histórico
							document.getElementById("contadorSJ").style.display="block";
							document.getElementById("contadorSP").style.display="none";
						    numeroRegistro.style.display="block";
							etiquetaNumReg.style.display="block";
				   		}
				 	}else{
					 	document.getElementById("contadorSJ").style.display="none";
						document.getElementById("contadorSP").style.display="none";
				 	}
				 }
				 if (datosGeneralesForm.sociedadSP.disabled==false){
			  		datosGeneralesForm.sociedadSP.focus();
			  	}
				
			}
			
			
			
			
		function adaptaTamanoFoto () 
		{
			widthMax = 180;
			heightMax = 240;
			foto = document.getElementById ("fotoNueva");
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



<body class="tablaCentralCampos"  onload="adaptaTamanoFoto();buscarGrupos();presentaContador();deshabilitarCheckSociedad();">

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
			<html:hidden name="datosGeneralesForm" property="abono" value="B"/>
			<html:hidden name="datosGeneralesForm" property="cargo"  value="B"/>
			<html:hidden name="datosGeneralesForm" property="idTratamiento"  value="1"/>
			<html:hidden name="datosGeneralesForm" property="modoSociedadSJ"/>
			<html:hidden name="datosGeneralesForm" property="modoSociedadSP"/>
			<html:hidden name="datosGeneralesForm" property="longitudSP"/>
			<html:hidden name="datosGeneralesForm" property="continuarAprobacion" value = ""/>
			
		
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
							<html:file name="datosGeneralesForm" property="foto" size="8" styleClass="<%=estiloCaja %>" accept="image/gif,image/jpg" ></html:file>		
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
										<td class="labelText" width="10%">
											<!-- Fecha Alta -->
											<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaAlta"/>
										</td>
										<td>	
											<html:text name="datosGeneralesForm" property="fechaAlta" style="width:70" styleClass="boxConsulta" readonly="true" value="<%=fechaAlta%>" />
										</td>
										<!-- NUMERO IDENTIFICACION NIF/CIF -->
										<% String CIF=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_CIF);
										   String OTRO=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_OTRO);
										%>
										<td class="labelText" colspan="2">
											<siga:Idioma key="censo.consultaDatosGenerales.literal.tipoIdentificacion"/>&nbsp;(*)

											 <%if (bConsultaPersona || modo.equalsIgnoreCase("VER")||tipoIdentif) { %>
											  <html:hidden name="datosGeneralesForm" property="tipoIdentificacion" value="<%=tipoIdentificacionSel%>"/>
												<% if (tipoIdentificacionSel.equals(CIF)) { %>
												<siga:Idioma key="censo.fichaCliente.literal.cif"/>
												<% } else if (tipoIdentificacionSel.equals(OTRO)) { %>
												<siga:Idioma key="censo.fichaCliente.literal.otro"/>
												<% } %>
										
											<% } else { %>						
												<html:select name="datosGeneralesForm" property="tipoIdentificacion" value="<%=tipoIdentificacionSel%>" styleClass="boxCombo">
														<html:option value="<%=OTRO%>"> <siga:Idioma key="censo.fichaCliente.literal.otro"/></html:option>	
														<html:option value="<%=CIF%>" > <siga:Idioma key="censo.fichaCliente.literal.cif"/></html:option>
												</html:select>
											<% }  %>	
												
											<% if (bConsultaPersona) { %>
													<html:text name="datosGeneralesForm" property="numIdentificacion" size="11" style="width:70px" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" ></html:text>
											<% } else { %>
													<html:text name="datosGeneralesForm" property="numIdentificacion" size="11" style="width:70px" styleClass="<%=estiloCajaNif %>" value="<%=nIdentificacion %>" readonly="<%=breadonlyNif %>" ></html:text>
											<% }  %>
										</td>
										<td class="labelText" width="40%">
											<%
												String tipoEstilo="boxCombo";
												String tipoReadOnly="false";
												
												if (modo.equalsIgnoreCase("EDITAR") || modo.equalsIgnoreCase("VER")) {
													tipoEstilo="boxConsulta";
													tipoReadOnly="true";
												}
											%>  
											<siga:Idioma key="censo.general.literal.tipoRegistro"/>&nbsp;(*) 
											<siga:ComboBD nombre = "tipo" tipo="cmbTipoSociedadAlta" clase="<%=tipoEstilo%>" obligatorio="true" readOnly="<%=tipoReadOnly%>" elementoSel="<%=tipoSel%>" parametro="<%=tipoParam %>" />
										
										</td>
									</tr>
									<tr>
										<td class="labelText" colspan="2">
											<!-- CheckBox de Sociedad SJ -->
											<siga:Idioma key="censo.general.literal.sociedadSJ"/>&nbsp;
											<html:checkbox name="datosGeneralesForm" property="sociedadSJ" onclick="presentaContadorAux(this)" value="<%=ClsConstants.DB_TRUE%>"    ></html:checkbox>
										</td>	
										<td class="labelText" colspan="2">
											<!-- CheckBox de Sociedad Profesional -->
											<siga:Idioma key="censo.general.literal.sociedadProfesional"/>&nbsp;
											<html:checkbox name="datosGeneralesForm" property="sociedadSP" onclick="presentaContadorAux(this)" value="<%=ClsConstants.DB_TRUE%>"   ></html:checkbox> 
										</td>
		<span id="etiquetaNumReg" > 
										<td id="nombrenumregistro" class="labelText" >
											<!-- NUMERO REGISTRO -->
<span id="numeroRegistro" >	
<span id="contadorSP" style="display:none"> <siga:Idioma key="censo.general.literal.numRegistro"/>&nbsp;(*) <html:text name="datosGeneralesForm" property="prefijoNumRegSP"  size="5" maxlength="10" styleClass="<%=estiloCaja%>" style="width:55px" readonly="<%=breadonly%>" ></html:text><html:text name="datosGeneralesForm" property="contadorNumRegSP" size="5" maxlength="8" style="width:58px" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text><html:text name="datosGeneralesForm" property="sufijoNumRegSP" style="width:58px" size="5" maxlength="10" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text> </span>
<span id="contadorSJ" style="display:none">	<siga:Idioma key="censo.general.literal.numRegistro"/>&nbsp;(*) <html:text name="datosGeneralesForm" property="prefijoNumRegSJ"  size="5" maxlength="10" styleClass="<%=estiloCaja%>" style="width:55px" readonly="<%=breadonly%>" ></html:text><html:text name="datosGeneralesForm" property="contadorNumRegSJ" size="5" maxlength="8" style="width:58px" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text><html:text name="datosGeneralesForm" property="sufijoNumRegSJ" style="width:58px" size="5" maxlength="10" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text></span>
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
											<html:text name="datosGeneralesForm" property="denominacion" size="40" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" readonly="<%=breadonlyNombreApellidos %>" ></html:text>
										</td>
										<td class="labelText">
											<!-- Abreviatura -->
											<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;
										</td>
										<td  class="labelText">
											<html:text name="datosGeneralesForm" property="abreviatura" size="15" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" readonly="<%=breadonlyNombreApellidos %>" ></html:text>
										</td>
									</tr>
									<tr>
										<!-- FECHA CONSTITUCION 
										<td class="labelText">
											
											<siga:Idioma key="censo.general.literal.FechaConstitucion"/>&nbsp;(*)
										</td>
										<td class="labelText">
											<html:text name="datosGeneralesForm" property="fechaConstitucion" size="10" styleClass="<%=estiloCaja %>" readonly="true"></html:text>&nbsp;
											<% if (!breadonly) { %>
											<a href='javascript://'onClick="return showCalendarGeneral(fechaConstitucion);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
											<% } %>
										</td>
										-->
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
														<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="<%=estiloCombo%>" obligatorio="true" elementoSel="<%=idiomaSel %>"  readonly="<%=readonly %>" obligatorioSinTextoSeleccionar="true" />
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
															<input type="checkbox" name="guiaJudicial" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="guiaJudicial"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
														<% } %>
														&nbsp;
														<!-- PUBLICIDAD -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.publicidad"/>
														<% if (publicidad.equals(ClsConstants.DB_TRUE)) { %>
															<input type="checkbox" name="publicidad"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="publicidad" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
														<% } %>
														&nbsp;
									
														<!-- COMISIONES -->
														<siga:Idioma key="censo.consultaDatosGenerales.literal.comisiones"/>
														&nbsp;
														<% if (comisiones.equals(ClsConstants.DB_TRUE)) { %>
															<input type="checkbox" name="comisiones"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
														<% } else { %>
															<input type="checkbox" name="comisiones" value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
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
																<html:text name="datosGeneralesForm" property="cuentaContable" size="10" styleClass="<%=estiloCaja %>" readonly="<%=breadonly %>" value="<%=cuentaContable %>"></html:text>
														<% } else { %>
																<html:hidden name="datosGeneralesForm" property="cuentaContable" value="<%=cuentaContable %>"></html:hidden>
														<% }  %>
													</td>
												</tr>
												<tr>
													<td class="labelText" style="width:100px">
														<siga:Idioma key="censo.general.literal.anotaciones"/>
													</td>
													<td style="width:320px">
														<html:textarea name="datosGeneralesForm" property="anotaciones"  onKeyDown="cuenta(this,2000)" onChange="cuenta(this,2000)" rows="2" style="width:300px" styleClass="<%=estiloCaja %>" readonly="<%=breadonly %>" />
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
		<input type="hidden" name="prefijoOld" value="">
   		<input type="hidden" name="sufijoOld" value="">
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

	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="resultado">
		<html:hidden name="GruposClienteClienteForm" property="modo" value="buscar"/>
		<html:hidden name="GruposClienteClienteForm" property="idPersona" />
		<html:hidden name="GruposClienteClienteForm" property="idInstitucion" />
		<html:hidden name="GruposClienteClienteForm" property="modoAnterior" />
	</html:form>


	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton SolicitarModificacion -->
		function accionSolicitarModificacion() 
		{		
			document.forms[0].modo.value="abrirSolicitud";
			ventaModalGeneral(document.forms[0].name,'P');
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();	
		}
		
		function validarFormulario() {
		
			//Valido el formulario:
			if (validateDatosGeneralesNoColegiadoForm(document.forms[0])) {
			//if(true){
			   
			
			<% 	if (!(bConsultaPersona || modo.equalsIgnoreCase("VER")) ) {  
			    %>
			       
					// El tipo de identificacion debe ser CIF:
					if (document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>") {
					
								//Validamos el formato del CIF:
								
								
										var tipo = document.forms[0].tipo.value.charAt(0);
										var numIdentificacion = document.forms[0].numIdentificacion.value.charAt(0);
										
										
										//El tipo debe ser igual a la primera letra del cif salvo para tipo Otro 'O' que no importa:
										if  ( tipo=="<%=ClsConstants.COMBO_TIPO_OTROS%>" || 
											  (tipo!="<%=ClsConstants.COMBO_TIPO_OTROS%>" && (tipo.toUpperCase()==numIdentificacion.toUpperCase()) )) {
												return true;
										} else {
												alert ('<siga:Idioma key="censo.fichaCliente.literal.errorCIF"/>');
												return false;
										
								} //Fin validar el CIF
					} else {
					
							if (document.forms[0].tipoIdentificacion.value != "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") {
							 
								alert ('<siga:Idioma key="censo.fichaCliente.literal.avisoCIF"/>');
								return false;
							} else{
							
								return true;
							}	
					}
				<% } // if VER  %>
				return true;
			} else
						return false;
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		
			//Valido el formulario:
			
			sub();
			if (document.datosGeneralesForm.longitudSP.value < document.datosGeneralesForm.contadorNumRegSP.value.length){
			  alert('<siga:Idioma key="messages.contador.error.longitudSuperada"/>');
			  fin();
			  return false;
			}
			
			
			if (document.datosGeneralesForm.contadorNumRegSP.value=="" && document.datosGeneralesForm.sociedadSP.checked){
				var mensaje='<siga:Idioma key="messages.contador.error.contadorObligatorio"/>';
				 alert (mensaje);
				 fin();
				 return false;
			}
			
			
			if (document.datosGeneralesForm.contadorNumRegSJ.value=="" && document.datosGeneralesForm.sociedadSJ.checked){
				var mensaje='<siga:Idioma key="messages.contador.error.contadorObligatorio"/>';
				 alert (mensaje);
				 fin();
				 return false;
			}
		
			
			if (validarFormulario()) {
			
			<%	
			  if (!modo.equalsIgnoreCase("NUEVASOCIEDAD")) { %>
			
			
					<% if (!bOcultarHistorico) { %>
							var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
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
						if (datosGeneralesForm.sociedadSP.checked){
						datosGeneralesForm.prefijoNumRegSP.disabled=false;
	 					datosGeneralesForm.sufijoNumRegSP.disabled=false;
          			    datosGeneralesForm.contadorNumRegSP.disabled=false;
	                    datosGeneralesForm.sociedadSP.disabled=false;
						}
						if (datosGeneralesForm.sociedadSJ.checked){
	                    datosGeneralesForm.prefijoNumRegSJ.disabled=false;
	 					datosGeneralesForm.sufijoNumRegSJ.disabled=false;
          			    datosGeneralesForm.contadorNumRegSJ.disabled=false;
	                    datosGeneralesForm.sociedadSJ.disabled=false;
						}
						document.forms[0].submit();	
					}else{
						fin();
					 	return false;
					}
				
			<%	} else { %>
			
			   if (datosGeneralesForm.sociedadSP){
				    if (document.datosGeneralesForm.modoSociedadSP.value==0){// cuando estamos en modo registro antes de enviar los datos los inicializamos a blanco porque 
																		   // si no se produce un error de inserccion.
					  
					    datosGeneralesForm.prefijoNumRegSP.disabled=false;
					    datosGeneralesForm.sufijoNumRegSP.disabled=false;
					 	datosGeneralesForm.contadorNumRegSP.disabled=false;
						
					 	
					 }
				} 
				 if (datosGeneralesForm.sociedadSJ){
					  if (document.datosGeneralesForm.modoSociedadSJ.value==0){// cuando estamos en modo registro antes de enviar los datos los inicializamos a blanco porque 
																		   // si no se produce un error de inserccion.
					  
					    datosGeneralesForm.prefijoNumRegSJ.disabled=false;
					    datosGeneralesForm.sufijoNumRegSJ.disabled=false;
					 	datosGeneralesForm.contadorNumRegSJ.disabled=false;
						
					 	
					  }		
				 }
								
					document.forms[0].target="submitArea2";
					document.forms[0].modo.value="insertarSociedad";
					document.forms[0].submit();	
			<%	}  %>
			}else{
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