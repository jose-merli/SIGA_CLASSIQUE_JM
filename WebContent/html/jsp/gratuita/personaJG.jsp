<!-- personaJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<!-- IMPORTS -->
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@ page import="com.siga.gratuita.form.PersonaJGForm"%>
<%@ page import="com.siga.beans.*"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	String dato[] = {(String) usr.getLocation()};
	boolean esFichaColegial = false;

	
	String sEsFichaColegial = (String) request
			.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null)
			&& ((sEsFichaColegial.equalsIgnoreCase("1")) || (sEsFichaColegial
					.equalsIgnoreCase("true")))) {
		esFichaColegial = true;
	}

	int pcajgActivo = 0;
	String minusDefecto=null;
	/*String sEsPcajgActivo = (String)request.getAttribute("pcajgActivo");
	if ((sEsPcajgActivo!=null) && (!sEsPcajgActivo.equalsIgnoreCase(""))){
		pcajgActivo = Integer.parseInt(sEsPcajgActivo);
	}*/
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute("pcajgActivo").toString());
	}
	// RGG 23-03-2006  cambios de personaJG

	PersonaJGForm miform = (PersonaJGForm) request
			.getAttribute("PersonaJGForm");
	String importeBienesInmuebles = miform.getImporteBienesInmuebles();
	String importeOtrosBienes = miform.getImporteOtrosBienes();
	String importeIngresosAnuales = miform.getImporteIngresosAnuales();
	String importeBienesMuebles = miform.getImporteBienesMuebles();
	
	String estiloBox = "box";
	String estiloBoxNumber = "boxNumber";
	String classCombo = "box";
	boolean readonly = false;
	String sreadonly = "false";
	boolean scheck = false;
	String accion = miform.getAccionE();
	if(usr.isComision()) accion="ver";

	if (accion.equals("ver")) {
		estiloBox = "boxConsulta";
		estiloBoxNumber = "boxConsultaNumber";
		classCombo = "boxConsulta";
		readonly = true;
		sreadonly = "true";
		scheck = true;
	} else {
		estiloBox = "box";
		estiloBoxNumber = "boxNumber";
		classCombo = "box";
		readonly = false;
		sreadonly = "false";
		scheck = false;
	}
	String titulo = miform.getTituloE();
	String leyenda = miform.getTituloE();
	String localizacion = miform.getLocalizacionE();
	String conceptoE = miform.getConceptoE();

	String pantalla = miform.getPantalla();
	String idPersona = miform.getIdPersonaJG();
	String actionE = miform.getActionE();
	String bPestana = (pantalla != null && pantalla.equals("P"))
			? "true"
			: "false";
	String nuevo = miform.getNuevo();
	String sexo = "";
	String idioma = "";
	String nHijos = "";
	String edad = "";
	if ((idPersona==null || idPersona.trim().equals("")) && request.getAttribute("minusvaliaDefecto") != null) {
		minusDefecto = (String)request.getAttribute("minusvaliaDefecto");
	}

	String tipoConoce = "", tipoGrupoLaboral = "", numVecesSOJ = "";
	/*idTipoConoce.add(miHash.get(ScsSOJBean.C_IDTIPOCONOCE).toString());
	idTipoGrupoLaboral.add(miHash.get(ScsSOJBean.C_IDTIPOGRUPOLABORAL).toString());*/

	String pideJG = miform.getChkPideJG();
	String solicitaInfoJG = miform.getChkSolicitaInfoJG();
	String checkSolicitante = miform.getSolicitante();
	String existeDomicilio = miform.getExisteDomicilio();
	String asterisco = "&nbsp(*)&nbsp";

	// Ponemos astericos en los campos obligatorios para el pcajg activo
	// jbd 19/01/2010 Hay que cambiar esto porque ahora pcajgActivo es un numero en vez de boolean
	/*if ((pcajgActivo) && 
			(( conceptoE.equals(PersonaJGAction.EJG)
			|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR) 
			|| conceptoE.equals(PersonaJGAction.PERSONAJG) ))){
		asterisco="&nbsp(*)&nbsp";
	}*/
	/*  Creamos una variable booleana por cada campo que dependa del tipo de pcjag del colegio.
		Estas variables luego nos serviran para colocar el asterisco en el campo y validar que tenga valor. */
	boolean obligatorioParentesco = false;
	boolean obligatorioDireccion = false;
	boolean obligatorioPoblacion = false;
	boolean obligatorioCodigoPostal = false;
	boolean obligatorioTipoIdentificador = false;
	boolean obligatorioIdentificador = false;
	boolean obligatorioNacionalidad = false;
	boolean obligatorioTipoIngreso = false;
	boolean obligatorioEstadoCivil= false;
	boolean obligatorioRegimenConyuge= false;
	boolean obligatorioIngreso = false;
	boolean obligatorioFechaNac = false;
	boolean obligatorioSexo = false;
	boolean obligatorioMinusvalia = false;
	boolean opcionDireccion = false;	
	
	if ((pcajgActivo == 1)
			&& ((conceptoE.equals(PersonaJGAction.EJG) || conceptoE
					.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)))) {
		// Nada de momento
	} else if ((pcajgActivo == 2)
			&& ((conceptoE.equals(PersonaJGAction.EJG) || conceptoE
					.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)))) {			
		if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR))
			obligatorioParentesco = true;
	} else if ((pcajgActivo == 3)
			&& ((conceptoE.equals(PersonaJGAction.EJG) || conceptoE
					.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)))) {				
		obligatorioIdentificador = true;		
		if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR))
			obligatorioParentesco = true;
		//Para el solicitante: campos sexo, estado civil y todos los datos de dirección (sin opción de marcar sin domicilio).
	    //Para la unidad familiar: obligatorio el parentesco.
	    //Para unidad familiar y contrarios:

	       // Si se marca el check sin domicilio no es obligatorio ningún campo.
	       // Si se desmarca el check son obligatorios todos los campos de dirección.

	} else if ((pcajgActivo == 4) && (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR) ||
				conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)  || conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || 
				conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS))) {
			
		if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
				|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
				 opcionDireccion=true;
				 obligatorioDireccion = true;
				 obligatorioPoblacion = true;
				 obligatorioCodigoPostal = true;
			
			if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR))
				obligatorioParentesco = true;
		}else if (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)){
			 obligatorioSexo = true;
			 obligatorioEstadoCivil= true;
			 obligatorioDireccion = true;
			 obligatorioPoblacion = true;
			 obligatorioCodigoPostal = true;
		}
				
				/*if (conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)){
					 obligatorioEstadoCivil= true;
					 obligatorioSexo = true;
				     obligatorioRegimenConyuge=true;
				}else if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
						|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR) || conceptoE.equals(PersonaJGAction.EJG)){
					obligatorioNacionalidad=false;
				}else{
					obligatorioDireccion = true;
					obligatorioPoblacion = true;
					obligatorioCodigoPostal = true;
					obligatorioIngreso= true;
				    obligatorioRegimenConyuge=true;
				    obligatorioNacionalidad=true;
				    obligatorioEstadoCivil= true;
				    obligatorioSexo = true;
					if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
						obligatorioParentesco = true;
					    obligatorioEstadoCivil= true;
					    obligatorioIngreso= true;
					    obligatorioSexo = false;
					}
				 }*/
	} else if ((pcajgActivo == 5)
			&& ((conceptoE.equals(PersonaJGAction.EJG) || conceptoE
					.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)))) {
		obligatorioDireccion = true;
		obligatorioPoblacion = true;
		obligatorioParentesco = true;
		obligatorioNacionalidad = true;
		obligatorioCodigoPostal = true;
	
	}else if ((pcajgActivo == 6) && ((conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)))){
		//Campos solicitante
		obligatorioDireccion = true;
		obligatorioPoblacion = true;		
		obligatorioCodigoPostal = true;
		
		//Campos UF
		obligatorioParentesco = true;
		obligatorioFechaNac = true;
		
	} else if (pcajgActivo == 7) {
		obligatorioMinusvalia = true;
	
	}

	ArrayList calidadSel = new ArrayList();
	String[] datos2= {usr.getLocation(),usr.getLanguage()};
	String idcalidad = miform.getIdTipoenCalidad();
	if (idcalidad!=null&&!idcalidad.equals("")){
		calidadSel.add(0,idcalidad);
	}
	
	String calidadIdinstitucion=miform.getCalidadIdinstitucion();
%>


<html>

<!-- HEAD -->
<head>
	<html:javascript formName="PersonaJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
		
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	
	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


	<!--Step 3 -->
  	<!-- defaults for Autocomplete and displaytag -->
  	<link type="text/css" rel="stylesheet" href="<%=app%>/html/css/ajaxtags.css" />
  	
	<script type="text/javascript">
	

	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g,"");
	}
		
		function validarCampoPoblacion()	{				
			if(document.forms[0].poblacion.value == "") {
				var msg = "<siga:Idioma key='errors.required'  arg0="" />" + "<siga:Idioma key='gratuita.personaJG.literal.poblacion'/>";
				alert (msg);
				return false;
			}
			return true;
		}
		
	function recargar(){
			<%if (!accion.equalsIgnoreCase("ver")) {%>
/*< %				if (esFichaColegial) {%>
			  < %} else { %>*/
				var tmp1 = document.getElementsByName("provincia");
				var tmp2 = tmp1[0];
				if (tmp2) {
					tmp2.onchange();
				} 
/*< %			   }%>*/
		 <%}%>
<%if (conceptoE.equals(PersonaJGAction.DESIGNACION_REPRESENTANTE)) {%>			
		var nombre = document.forms[0].representante;
		if (document.forms[0].idPersonaRepresentante.value=="") {
			// se puede modificar
			nombre.className="box";
			nombre.readOnly=false;
		} else {
			// no se puede modificar
			nombre.className="boxConsulta";
			nombre.readOnly=true;
		}
<%}%>			

  <%if (conceptoE.equals(PersonaJGAction.SOJ)) {
		if (pideJG != null && pideJG.equals("1")) {%>
		  document.forms[0].chkPideJG.checked=true;
	     
	  <%} else {%>
	      document.forms[0].chkPideJG.checked=false;
	  <%}

		if (solicitaInfoJG != null && solicitaInfoJG.equals("1")) {%>
			  document.forms[0].chkSolicitaInfoJG.checked=true;
	   <%} else {%>
	      document.forms[0].chkSolicitaInfoJG.checked=false;
	   <%}%>
	   
	   
	 <%}%>
	 
	 <% 	
	 	if(pcajgActivo == 4){
	 		if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
					|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
	 		if (existeDomicilio != null && existeDomicilio.equals("N")) {%>

				document.forms[0].existeDom.checked=true;
	 			document.PersonaJGForm.direccion.disabled = "disabled";
	 			document.PersonaJGForm.cp.disabled = "disabled";
				document.getElementById("desaparece").style.display="none";
				document.getElementById("desaparecePr").style.display="none";
				document.getElementById("desapareceCp").style.display="none";
				document.getElementById("desaparecePo").style.display="none";
			<%}else{ %>
			document.forms[0].existeDom.checked=false;
			document.getElementById("desaparece").style.display="inline";
			document.getElementById("desaparecePr").style.display="inline";
			document.getElementById("desapareceCp").style.display="inline";
			document.getElementById("desaparecePo").style.display="inline";
			<%} 
	 		}
	 	}	
		%>
	<%if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {%> 
	<%if (checkSolicitante != null && checkSolicitante.equals("1")) {%>
		 
		    document.forms[0].solicitante.checked=true;
	     
	   <%} else {%>	    
	        document.forms[0].solicitante.checked=false;
	   <%}
			}%>
		}
	
		function desabilitarDomicilio (o) {	
 			if (o.checked) {
				document.getElementById("desaparece").style.display="none";
				document.getElementById("desaparecePr").style.display="none";
				document.getElementById("desapareceCp").style.display="none";
				document.getElementById("desaparecePo").style.display="none";
				document.forms[0].existeDom.checked = true;
	 			document.PersonaJGForm.existeDomicilio.value = "N";
	 			document.PersonaJGForm.direccion.value = "";
	 			document.PersonaJGForm.cp.value = "";
				document.forms[0].provincia.value = "";
				document.forms[0].provincia.onchange();
	 			document.PersonaJGForm.direccion.disabled = "disabled";
	 			document.PersonaJGForm.cp.disabled = "disabled";
				document.forms[0].provincia.disabled = "disabled";
				document.forms[0].poblacion.disabled = "disabled";
			}else {
				document.getElementById("desaparece").style.display="inline";
				document.getElementById("desaparecePr").style.display="inline";
				document.getElementById("desapareceCp").style.display="inline";
				document.getElementById("desaparecePo").style.display="inline";
	 			document.PersonaJGForm.direccion.disabled = "";
	 			document.PersonaJGForm.cp.disabled = "";
				document.forms[0].provincia.disabled = "";
				document.forms[0].poblacion.disabled = "";
	 			document.forms[0].existeDom.checked = false;
	 			document.PersonaJGForm.existeDomicilio.value = "S";
			}
 		}
	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titulo %>" 
		localizacion="<%=localizacion %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<script type="text/javascript">
	
			function comprobarFecha(sFecha){
			
				<%if (!obligatorioFechaNac) {%>
					if (sFecha==""){
						return true;
					}
				<%}%>
			
				if ((sFecha=="")||(!validarFecha2(sFecha))){
					return false;
				}							
								
				var diaFecha = parseInt(sFecha.substring(0,2),10);
				var mesFecha = parseInt(sFecha.substring(3,5),10);
				var anioFecha = parseInt(sFecha.substring(6,10),10);
				
				var dFechaActual = new Date();
				var diaFechaActual = dFechaActual.getDate();
				var mesFechaActual = dFechaActual.getMonth()+1;
				var anioFechaActual = dFechaActual.getFullYear();
				
				if (anioFecha>anioFechaActual) {
					return false;
				}
				else {
					if ((anioFecha==anioFechaActual)&&(mesFecha>mesFechaActual)) {
						return false;
					}
					else {
						if ((anioFecha==anioFechaActual)&&(mesFecha==mesFechaActual)&&(diaFecha>diaFechaActual)) {
							return false;
						}
					}
				}
				return true;
			}							
	
			function proFechaNac(){
				var sFechaNac = document.forms[0].fechaNac.value;				
				
				if(!validarFecha2(sFechaNac)){
					sFechaNac = "";
				}
			
				if (sFechaNac!="") {							
					var diaFechaNac = parseInt(sFechaNac.substring(0,2),10);
					var mesFechaNac = parseInt(sFechaNac.substring(3,5),10);
					var anioFechaNac = parseInt(sFechaNac.substring(6,10),10);
				
					var dFechaActual = new Date();
					var diaFechaActual = dFechaActual.getDate();
					var mesFechaActual = dFechaActual.getMonth()+1;
					var anioFechaActual = dFechaActual.getFullYear();
					
					var numEdad = anioFechaActual - anioFechaNac;
					if ((mesFechaActual<mesFechaNac)||(mesFechaActual==mesFechaNac&&diaFechaActual<diaFechaNac)){
						numEdad = numEdad - 1;							
					}
					
					if (numEdad<0) {
						numEdad="";
					}
				}

				<%if (obligatorioFechaNac) {%>
					if (sFechaNac=="") {
						document.forms[0].edad.value = "";
					}
					else {
						document.forms[0].edad.value = numEdad;			
					}
					
				<%} else {%>
					if (sFechaNac=="") {
						document.forms[0].edad.className="box";
						document.forms[0].edad.readOnly=false;
					}
					else {
						document.forms[0].edad.value = numEdad;		
						document.forms[0].edad.className="boxConsulta";
						document.forms[0].edad.readOnly=true;	
					}						
				<%}%>																
			}

			function retarda(tipoId){
				document.PersonaJGForm.tipoId.value = tipoId;
				comprobarTipoIdent();
			}

				function traspasoDatos(resultado,bNuevo) 
				{

					document.forms[0].nuevo.value = bNuevo;					
				  if (bNuevo=="1"){// sólo cargamos los datos de la persona si esta ya estaba dada de alta en personaJG					  
					if (resultado[1]!="null" && trim(resultado[1])!="") {					
						document.forms[0].idTipoPersona.value=resultado[22];						
						document.forms[0].idTipoPersona.onchange();
						//recuperamos el valor del tipoIdentificacion.											
						var funcionRetardo = 'retarda('+resultado[0]+')';
						window.setTimeout(funcionRetardo,150,"Javascript");						 															
					}else
					if(resultado[0]!=null && resultado[2]!=null  && trim(resultado[0])!=""  && trim(resultado[2])!=""){						
						document.forms[0].tipoId.value = resultado[0];
						document.forms[0].NIdentificacion.value = resultado[2]; 
						
						//document.forms[0].tipoId.value=	resultado[0];
					}
					document.forms[0].idPersonaJG.value = resultado[1]; 
					// RGG 15-03-2006 para no pisar el dni
					if (trim(resultado[1])!="") {
						document.forms[0].NIdentificacion.value = resultado[2]; 
					}
					document.forms[0].nombre.value = resultado[3]; 
					document.forms[0].apellido1.value = resultado[4]; 
					document.forms[0].apellido2.value = resultado[5]; 
					document.forms[0].direccion.value = resultado[6]; 
					document.forms[0].cp.value = resultado[7]; 

				//Estado Civil:
				document.forms[0].estadoCivil.value = resultado[11];
				
				//Regimen Conyugal:
				document.forms[0].regimenConyugal.value = resultado[12];				
				
				//Fecha Nacimiento:
				document.forms[0].fechaNac.value = resultado[13]; 
								
				//Pais:
				document.forms[0].nacionalidad.value = resultado[8];
				document.forms[0].nacionalidad.onchange();

				//Provincia:
				document.forms[0].provincia.value = resultado[9];
				document.forms[0].provincia.onchange();

				//Profesion:
				if(document.forms[0].profesion){
					document.forms[0].profesion.value = resultado[14];
					document.forms[0].profesion.onchange();
				}

				//Sexo:
				document.forms[0].sexo.value = resultado[19];

				//Fax	
				document.forms[0].fax.value = resultado[20];
				//correoElectronico	
				document.forms[0].correoElectronico.value = resultado[21];

				//minusvalia	
				document.forms[0].minusvalia.value = resultado[23];

				//edad	
				document.forms[0].edad.value = resultado[24];

				//existeDomicilio
				document.forms[0].existeDomicilio.value = resultado[25];
				<%if(pcajgActivo == 4){ 
					if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
							|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
					%> 
					if(resultado[25]!=null && resultado[25]=="N"){
						document.forms[0].existeDom.checked=true;
						document.getElementById("desaparece").style.display="inline";
						document.getElementById("desaparecePr").style.display="inline";
						document.getElementById("desapareceCp").style.display="inline";
						document.getElementById("desaparecePo").style.display="inline";
					}else{
						document.forms[0].existeDom.checked=false;
						document.getElementById("desaparece").style.display="none";
						document.getElementById("desaparecePr").style.display="none";
						document.getElementById("desapareceCp").style.display="none";
						document.getElementById("desaparecePo").style.display="none";
					}	
				<%} }%>
         <%if (conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)
					|| conceptoE.equals(PersonaJGAction.SOJ)) {%> 
				if (resultado[18] != null && trim(resultado[18])!="" && trim(resultado[18])!="null") {
            		document.forms[0].hijos.value = resultado[18]; 
				} else {
            		document.forms[0].hijos.value = ""; 
				}
         <%}%>

<%if (!conceptoE.equals(PersonaJGAction.PERSONAJG)
					&& !conceptoE
							.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {%> 
	
				//Representante Tutor:
				document.forms[0].idRepresentanteJG.value = resultado[15];
				
				document.forms[0].representante.value=resultado[16];
				
<%}%>

<%if (conceptoE.equals(PersonaJGAction.EJG)
					|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {%>
				document.forms[0].ingresosAnuales.value = "";
				document.forms[0].importeIngresosAnuales.value = "";
				document.forms[0].bienesMuebles.value = "";
				document.forms[0].importeBienesMuebles.value = "";
				document.forms[0].bienesInmuebles.value = "";
				document.forms[0].importeBienesInmuebles.value = "";
				document.forms[0].otrosBienes.value = "";
				document.forms[0].importeOtrosBienes.value = "";
				document.forms[0].unidadObservaciones.value = "";
<%}%>

				//Poblacion:				
				poblacionSeleccionada = resultado[10];
				//alert("="+poblacionSeleccionada);
				//A los 1000 milisegundos (tiempo para recargar el combo provincias) se selecciona la poblacion:
				window.setTimeout("recargarComboHijo()",1000,"Javascript");

				if (resultado!=null && resultado[1]!="") {
					var p1 = document.getElementById("resultado");					
					p1.src = "JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona="+resultado[1]+"&idInstitucion=<%=usr.getLocation()%>";
					document.forms[0].target="mainPestanas";				
					document.forms[0].modo.value="editar";
				} else {
					var p1 = document.getElementById("resultado");					
					p1.src = "JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=&idInstitucion=<%=usr.getLocation()%>";
					document.forms[0].target="mainPestanas";				
					document.forms[0].modo.value="editar";
				}
 			}
			 comprobarTipoIdent();
		 }	
		function recargarComboHijo() {
			var acceso = poblacionFrame.document.getElementsByTagName("select");
			acceso[0].value = poblacionSeleccionada;
			document.forms[0].poblacion.value = poblacionSeleccionada;
		}

		// Comprueba el tipo de persona que se elegi en el combo FISICA O JURIDICA 
		function comprobarTipoPersona ()
		{
			//Falla al dar el boton ver, yo pondría esto 			
			<%if (!accion.equalsIgnoreCase("ver")) {%>	
			if(document.PersonaJGForm.idTipoPersona.value == 'F')
			{
				//alert("document.PersonaJGForm.tipos.value FISICA: "+document.PersonaJGForm.tipos.value);
				document.getElementById("apelli2").style.display="block";
				document.getElementById("perJuridica").style.display="none";
				document.getElementById("perJuridica1").style.display="none";
				document.getElementById("perFisica").style.display="block";
				document.getElementById("perFisica1").style.display="block";					
			}
			else
			{
				//alert("document.PersonaJGForm.tipos.value JURIDICA: "+document.PersonaJGForm.tipos.value);
				document.getElementById("perFisica").style.display="none";
				document.getElementById("perFisica1").style.display="none"; 
				document.getElementById("apelli2").style.display="none";				
				document.getElementById("perJuridica").style.display="block";
				document.getElementById("perJuridica1").style.display="block";					
			}
			//document.getElementById("textoInformativo").style.display="none";		
			<%}%>		
		}
				
		function comprobarIdentificador ()
		{
			alert("ver identificación");
			alert("document.PersonaJGForm.tipoId.value"+document.PersonaJGForm.tipoId.value);
			//document.solicitudCompraForm.tipoId.value=='P'
		}
		
		// Comprueba el tipo de ident y pinta el boton de generar letra nif si fuese necesario
		 
		 function comprobarTipoIdent(){
			<%if (!accion.equalsIgnoreCase("ver")) {%>			
			// Solo se genera el NIF o CIF de la persona
			if((document.forms[0].tipoId.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")||
				(document.forms[0].tipoId.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>")
				|| (document.forms[0].tipoId.value== "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>")){
				//document.getElementById("idButtonNif").style.visibility="visible";
				document.getElementById("textoInformativo").style.display="none";
				//document.getElementById("textoInformativoEnBlanco").style.display="block";								
			}	else{
				//document.getElementById("idButtonNif").style.visibility="hidden";
				document.getElementById("textoInformativo").style.display="block";
				//document.getElementById("textoInformativoEnBlanco").style.display="none";											
			}			
			<%}%>
		}	
		
		
		function rellenarFormulario(){
			if(document.forms[0].NIdentificacion.value!="")
			{
				document.forms[0].modo.value="buscarNIF";
				document.forms[0].target="submitArea2";
				document.forms[0].submit();
			}
		}

		function obtenerNif(){
			PersonaJGForm.NIdentificacion.value=PersonaJGForm.NIdentificacion.value.toUpperCase();
			//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y éste se encarga de todo :)
			if(generarLetra()){
				rellenarFormulario();
				comprobarTipoIdent();
		  	}
		}
		
		function formateaNIFLocal(valorX) {
			return valorX.toUpperCase(); 
		}

		
		function formateaNIELocal(valorX) {
			return valorX.toUpperCase(); 
		}

	
		
	function generarLetra() {
		var numId = PersonaJGForm.NIdentificacion.value;
		var tipoIdentificacion = PersonaJGForm.tipoId.value;
	  	var letra='TRWAGMYFPDXBNJZSQVHLCKET';
		if(numId.length==0){
			return false;		
		}
		if( (tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")){
			if(numId.length==8){
				if(isNumero(numId)==true){
				 	numero = numId;
				 	numero = numero % 23;
				 	letra=letra.substring(numero,numero+1);
				 	PersonaJGForm.NIdentificacion.value =numId+letra;
				}else{
					return false;
				}
			}else{
				rc = validaNumeroIdentificacion(tipoIdentificacion, numId);
				if(rc==false){
				    return rc;
				}	
			}
		} else	if((tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){
			if(numId.length==8){
				var dnie = document.forms[0].NIdentificacion.value;
				letIni = numId.substring(0,1);
				primeraLetra = letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni+numId.substring(1,8);
				if(primeraLetra.match('[X|Y|Z]') && isNumero(num)){
					var posicion = num % 23;
					letras='TRWAGMYFPDXBNJZSQVHLCKET';
					var letra=letras.substring(posicion,posicion+1);
					numero = dnie + letra;
					PersonaJGForm.NIdentificacion.value = numero;
				}else{
					return false;
				}	
			}else{
				rc = validaNumeroIdentificacion(tipoIdentificacion, numId);
				if(rc==undefined){
					return rc;
				}else if(rc==false){
					return rc;
				}	
			}
			
		}
		// Caso1: Se han realizado las modificaciones necesarias sin encontrar errores 
		// Caso2: no es nif ni nie no hay generacion de letra

		return true;
	}

	function validaNumeroIdentificacion (){
		var errorNIE = false;
		var errorNIF = false;
		var errorDatos = false;
		var valido = true;

		if(document.forms[0].tipoId.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			var numero = document.forms[0].NIdentificacion.value;
			if(numero.length==9){
				letIn = numero.substring(8,9);
				num = numero.substring(0,8);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letIn) {
					errorNIF=true;
				}
			}else{
				errorNIF=true;
			}
		}else if(document.forms[0].tipoId.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
			var dnie = document.forms[0].NIdentificacion.value;
			if(dnie.length==9){
				letIni = dnie.substring(0,1);
				primera=letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni + dnie.substring(1,8);
				letFin = dnie.substring(8,9);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (!primera.match('[X|Y|Z]')||letra!=letFin) {
					errorNIE=true;
				}
			}else{
				errorNIE=true;
			}
		}else{
			var numId = document.forms[0].NIdentificacion.value;
			var tipoId = document.forms[0].tipoId.value;
			if(numId!="" && tipoId==""){
				errorDatos=true;
			}
		}
		if (errorNIF){
			valido = false;
			alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
		}
		if (errorDatos){
			valido = false;
			alert("<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>");;
		}
		return valido;
	}
		

			function validaImportes(){
				var ingAnuales = document.forms[0].importeIngresosAnuales.value;
				var bienInm = document.forms[0].importeBienesInmuebles.value;
				var impInm = document.forms[0].importeBienesMuebles.value;
				var impOtros = document.forms[0].importeOtrosBienes.value;
				//if (ingAnuales > 99999999.99)
					
			}
					
			function addOption(combo,text, value) {
				var comboBox = document.getElementById(combo);
				var newOption = new Option(text, value);
				comboBox.options[comboBox.options.length] = newOption;
			}
			
	
             function sigueRecarga(resultado){
			 

               for(var x=0;x<document.forms[0].provincia.length;x++)
               {
                   var laProvincia = resultado[8];
                   while(laProvincia.length < 1) laProvincia = "0"+laProvincia;
                   if(document.forms[0].provincia.options[x].value == laProvincia)
                      document.forms[0].provincia.options[x].selected = 'true';
               }

               for(var x=0;x<document.forms[0].poblacion.length;x++)
               {
                   var laPoblacion = resultado[9];
                   while(laPoblacion.length < 5) laPoblacion = "0"+laPoblacion;
                   if(document.forms[0].poblacion.options[x].value == laPoblacion)
                      document.forms[0].poblacion.options[x].selected = 'true';
               }
               for(var x=0;x<document.forms[0].idEstadoCivil.length;x++)
                   if(document.forms[0].idEstadoCivil.options[x].value == resultado[10])
                      document.forms[0].idEstadoCivil.options[x].selected = 'true';
                              if(resultado[11]=='G')
               {
                  document.forms[0].regimenConyugal.options[1].selected = 'false';
                  document.forms[0].regimenConyugal.options[0].selected = 'true';
               }
               else if(resultado[11] == 'S')
               {
                  document.forms[0].regimenConyugal.options[0].selected = 'false';
                  document.forms[0].regimenConyugal.options[1].selected = 'true';
               }
       }

     function createProvince() {

  			  var Primary = document.forms[0].cp.value;
  			  if ((Primary == null) || (Primary == 0)) return;
  			
  			  while(Primary.length<5){
  				  Primary="0"+Primary;
  			  }	  
  			  var idProvincia	= Primary.substring(0,2);
  			  document.forms[0].provincia.value=idProvincia;  				  
  			  rellenarCampos();

  	}
		function rellenarCampos(){
			document.forms[0].provincia.onchange();
	} 


		function accionCalendario() 
		{
			// Abrimos el calendario 
			if (document.getElementById('fechaNac').value!='') {

				rellenaEdad(document.getElementById('fechaNac').value,'S');
		 	}else{
				//Si da a reset no viene nada por lo que actualizamos. si viene con fecha
				//es que ha cerrado desde el aspa, lo dejamos como estuviera(no hacemos nada) 		 		
		 		 
				 if( document.getElementById('fechaNac').value=='')
					 document.getElementById('fechaNac').value='';
			} 
		}
	
		function rellenaEdad(fecha, Actualiza){
			//calculo la fecha de hoy 
		    hoy=new Date() 
		    //alert(hoy) 

		    //calculo la fecha que recibo 
		    //La descompongo en un array 
		    var array_fecha = fecha.split("/") 
		    //si el array no tiene tres partes, la fecha es incorrecta 
		    if (array_fecha.length!=3) 
		       return false 

		    //compruebo que los ano, mes, dia son correctos 
		    var ano 
		    ano = parseInt(array_fecha[2]); 
		    if (isNaN(ano)) 
		       return false 
			var mes 
		    mes = parseInt(array_fecha[1]); 
		    if (isNaN(mes)) 
		       return false 

		    var dia 
		    dia = parseInt(array_fecha[0]); 
		    if (isNaN(dia)) 
		       return false 

		    //si el año de la fecha que recibo solo tiene 2 cifras hay que cambiarlo a 4 
		    if (ano<=99) 
		       ano +=1900 

		    //resto los años de las dos fechas 
		    edad=hoy.getYear()- ano - 1; //-1 porque no se si ha cumplido años ya este año 
			var edadReal=edad;
		    //si resto los meses y me da menor que 0 entonces no ha cumplido años. Si da mayor si ha cumplido 
		    if (hoy.getMonth() + 1 - mes < 0){ //+ 1 porque los meses empiezan en 0 
		    	edadReal= edad;
		    }else	
		    if (hoy.getMonth() + 1 - mes > 0){ 
		    	edadReal= edad+1;
			}else	
		    //entonces es que eran iguales. miro los dias 
		    //si resto los dias y me da menor que 0 entonces no ha cumplido años. Si da mayor o igual si ha cumplido 
		    if (hoy.getMonth() + 1 - mes == 0){ 
			    if (hoy.getUTCDate() - dia >= 0) {
			    	edadReal= edad + 1;
			    }else
			    	edadReal= edad;
		    }    		    	
		    if(document.PersonaJGForm.edad.value==undefined)
				document.PersonaJGForm.edad.value="";
			
		    if(Actualiza=='S'){   
				if(edadReal!=undefined){
					document.PersonaJGForm.edad.value=edadReal;
				}
		    }else
			    return edadReal;
	} 


		function 	validaEdad(){
			if(document.PersonaJGForm.edad.value!=''){
				var edad = rellenaEdad(document.getElementById('fechaNac').value,'N');
				if(document.PersonaJGForm.edad.value==edad)
					return true;
				else 
					return false;
			}else 
				return true;
		}	
		var poblacionSeleccionada;
		function recargarComboHijo() {
			var acceso = poblacionFrame.document.getElementsByTagName("select");
			acceso[0].value = poblacionSeleccionada;
			document.PersonaJGForm.poblacion.value = poblacionSeleccionada;
		}

		function postAccionBusquedaNIF(){
			poblacionSeleccionada = document.getElementById("poblacion").value;
			document.getElementById("provincia").onchange();
			window.setTimeout("recargarComboHijo()",500,"Javascript");	
			document.getElementById("poblacion").value = document.PersonaJGForm.poblacion.value;		
		}

		function preAccionExisteNIF(){
			if(document.PersonaJGForm.NIdentificacion.value == null || document.PersonaJGForm.NIdentificacion.value == ''){
				return 'cancel';
			}
		}

		function postAccionExisteNIF(){
			if(document.PersonaJGForm.existeNIF.value == '1'){
			    var type =  'Ya existe una persona con la Identificación introducida - ' + document.PersonaJGForm.NIdentificacion.value + '. ¿Desea obtener los datos del otro registro existente y sobreescribir los actuales (recomendado)?';
				if (confirm(type)){
					//document.PersonaJGForm.forzarAjax.onchange();   -> Busqueda por ajax
					rellenarFormulario();// -> Búsqueda antigua
				}
				
			}else if(document.PersonaJGForm.existeNIF.value == '0'){
				//No existe
			}
		}
		
		function preAccionBusquedaNIF(){
			document.PersonaJGForm.tipoId.value = "";
			document.PersonaJGForm.nombre.value = ""; 
			document.PersonaJGForm.apellido1.value = "";
			document.PersonaJGForm.apellido2.value = "";

			document.PersonaJGForm.direccion.value = "";
			document.PersonaJGForm.cp.value = "";
			document.PersonaJGForm.provincia.value = "";
			document.PersonaJGForm.poblacion.value = "";
			document.PersonaJGForm.existeDomicilio.value = "";

			document.PersonaJGForm.nacionalidad.value = "";
			document.PersonaJGForm.fechaNac.value = "";
			document.PersonaJGForm.edad.value = "";
		
			document.PersonaJGForm.estadoCivil.value = "";
			document.PersonaJGForm.regimenConyugal.value = "";

			if(document.PersonaJGForm.tipoGrupoLaboral){
				document.PersonaJGForm.tipoGrupoLaboral.value = "";
			}
			
			if(document.PersonaJGForm.profesion){
				document.PersonaJGForm.profesion.value = "";
			}

			if(document.PersonaJGForm.calidad2){
				document.PersonaJGForm.calidad2.value = "";
			}

			if(document.PersonaJGForm.enCalidadDe){
				document.PersonaJGForm.enCalidadDe.value = "";
			}

			document.PersonaJGForm.sexo.value = "";
			document.PersonaJGForm.minusvalia.value = "";
			
			document.PersonaJGForm.fax.value = "";
			document.PersonaJGForm.correoElectronico.value = "";
		}
               
		</script>
</head>


<%
	// BOTONES PARA PESTAÑAS
	if (pantalla.equals("P")) {
%>

<body class="tablaCentralCampos" onload="recargar();comprobarTipoPersona();comprobarTipoIdent();">

<!-- capa principal -->
<div id="camposRegistro"  align="center">

<%
	} else {
		// BOTONES PARA MODAL
%>




<body class="tablaCentralCampos" onload="ajusteAlto('resultado');recargar();comprobarTipoPersona();">

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titulo %>"/>
		</td>
	</tr>
	</table>

<!-- capa principal -->
<div style="height:430px;" id="camposRegistro" class="posicionModalGrande" align="center">
<%
	}
%>
	
	
	
<!-- CAMPOS DEL REGISTRO -->
<table align="center"  width="100%" class="tablaCentralCampos" cellpadding="0" cellspacing="0">
<html:form action="<%=actionE%>" method="POST" target="mainPestanas" styleId="PersonaJGForm">	

	<html:hidden styleId = "modo"  property = "modo" />
	<html:hidden styleId = "nuevo" property = "nuevo" value="<%=nuevo%>" />

	<html:hidden name="PersonaJGForm" property = "accionE" styleId="accionE"/>
	<html:hidden name="PersonaJGForm" property = "pantalla" styleId = "pantalla"/>
	<html:hidden name="PersonaJGForm" property = "localizacionE" styleId = "localizacionE" />
	<html:hidden name="PersonaJGForm" property = "tituloE" styleId = "tituloE" />
	<html:hidden name="PersonaJGForm" property = "conceptoE" styleId = "conceptoE" />
	<html:hidden name="PersonaJGForm" property = "existeNIF"  styleId = "existeNIF" />
	<html:hidden name="PersonaJGForm" property = "forzarAjax" styleId = "existeNIF"  />
	<html:hidden name="PersonaJGForm" property = "lNumerosTelefonos" styleId = "lNumerosTelefonos" />
	<html:hidden property = "idTipoenCalidad"  styleId = "idTipoenCalidad" value="<%=idcalidad%>"/>
	<html:hidden property = "calidadIdinstitucion" styleId = "calidadIdinstitucion" value="<%=calidadIdinstitucion%>"/>
	
	
	
<%
		if (conceptoE.equals(PersonaJGAction.EJG)
					|| conceptoE.equals(PersonaJGAction.EJG_REPRESENTANTE)
					|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)
					|| conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)) {
	%>
	<html:hidden name="PersonaJGForm" styleId = "idInstitucionEJG" property = "idInstitucionEJG" />
	<html:hidden name="PersonaJGForm" styleId = "idTipoEJG"  property = "idTipoEJG" />
	<html:hidden name="PersonaJGForm" styleId = "anioEJG" property = "anioEJG" />
	<html:hidden name="PersonaJGForm" styleId = "numeroEJG" property = "numeroEJG" />
	<html:hidden property = "actionModal" styleId = "actionModal"  value = ""/>
<%
	} else if (conceptoE.equals(PersonaJGAction.SOJ)
				|| conceptoE.equals(PersonaJGAction.SOJ_REPRESENTANTE)) {
%>
	<html:hidden name="PersonaJGForm" styleId = "idInstitucionSOJ"  property = "idInstitucionSOJ" />
	<html:hidden name="PersonaJGForm" styleId = "idTipoSOJ"  property = "idTipoSOJ" />
	<html:hidden name="PersonaJGForm" styleId = "anioSOJ" property = "anioSOJ" />
	<html:hidden name="PersonaJGForm" styleId = "numeroSOJ" property = "numeroSOJ" />
<%
	} else if (conceptoE
				.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)
				|| conceptoE
						.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
				|| conceptoE
						.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
%>
	<html:hidden name="PersonaJGForm" styleId = "idInstitucionASI"  property = "idInstitucionASI" />
	<html:hidden name="PersonaJGForm" styleId = "anioASI" property = "anioASI" />
	<html:hidden name="PersonaJGForm" styleId = "numeroASI"  property = "numeroASI" />
<%
	} else if (conceptoE
				.equals(PersonaJGAction.DESIGNACION_INTERESADO)
				|| conceptoE
						.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)
				|| conceptoE
						.equals(PersonaJGAction.DESIGNACION_REPRESENTANTE)) {
%>
	<html:hidden name="PersonaJGForm" styleId = "idInstitucionDES" property = "idInstitucionDES" />
	<html:hidden name="PersonaJGForm" styleId = "idTurnoDES" property = "idTurnoDES" />
	<html:hidden name="PersonaJGForm" styleId = "anioDES"  property = "anioDES" />
	<html:hidden name="PersonaJGForm" styleId = "numeroDES" property = "numeroDES" />	
	
<%
	} else if (conceptoE.equals(PersonaJGAction.PERSONAJG)) {
%>
	<html:hidden name="PersonaJGForm" styleId = "idInstitucionPER" property = "idInstitucionPER" />
	<html:hidden name="PersonaJGForm" styleId = "idPersonaPER"  property = "idPersonaPER" />
<%
	}
%>

	<html:hidden name="PersonaJGForm" styleId = "idPersonaJG" property = "idPersonaJG" />
 	<html:hidden name="PersonaJGForm" styleId = "idInstitucionJG" property = "idInstitucionJG"/>
 	

<tr>				
<td  align="center" valign="top"> 

	<!-- TITULO -->

	<%
		if (conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)
					|| conceptoE
							.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
					|| conceptoE
							.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
	%>
		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
									ScsAsistenciasAdm adm = new ScsAsistenciasAdm(usr);
									Hashtable hTitulo = adm.getTituloPantallaAsistencia(miform
											.getIdInstitucionASI(), miform.getAnioASI(), miform
											.getNumeroASI());
									if (hTitulo != null) {
										t_nombre = (String) hTitulo
												.get(ScsPersonaJGBean.C_NOMBRE);
										t_apellido1 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO1);
										t_apellido2 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO2);
										t_anio = (String) hTitulo
												.get(ScsAsistenciasBean.C_ANIO);
										t_numero = (String) hTitulo
												.get(ScsAsistenciasBean.C_NUMERO);										
									}
						%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	<%
		}
	%>
	<%
		if (conceptoE.equals(PersonaJGAction.SOJ)) {
	%>
		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoSOJ = "";
									ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(usr);
									Hashtable hTitulo = adm.getTituloPantallaSOJ(miform
											.getIdInstitucionSOJ(), miform.getAnioSOJ(), miform
											.getNumeroSOJ(), miform.getIdTipoSOJ());
									if (hTitulo != null) {
										t_nombre = (String) hTitulo
												.get(ScsPersonaJGBean.C_NOMBRE);
										t_apellido1 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO1);
										t_apellido2 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO2);
										t_anio = (String) hTitulo.get(ScsSOJBean.C_ANIO);
										t_numero = (String) hTitulo.get(ScsSOJBean.C_NUMSOJ);
										t_tipoSOJ = (String) hTitulo.get("TIPOSOJ");
									}
						%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>  <%=UtilidadesString.mostrarDatoJSP(t_tipoSOJ)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	<%
		}
	%>
	<%
		if (conceptoE.equals(PersonaJGAction.EJG)) {
	%>
		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";;
									ScsEJGAdm adm = new ScsEJGAdm(usr);

									Hashtable hTitulo = adm.getTituloPantallaEJG(miform
											.getIdInstitucionEJG(), miform.getAnioEJG(), miform
											.getNumeroEJG(), miform.getIdTipoEJG());

									if (hTitulo != null) {
										t_nombre = (String) hTitulo
												.get(ScsPersonaJGBean.C_NOMBRE);
										t_apellido1 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO1);
										t_apellido2 = (String) hTitulo
												.get(ScsPersonaJGBean.C_APELLIDO2);
										t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
										t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
										t_tipoEJG = (String) hTitulo.get("TIPOEJG");
									}
						%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>  <%=UtilidadesString.mostrarDatoJSP(t_tipoEJG)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString
											.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	<%
		}
	%>
	
	

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.personaJG.literal.datosGenerales">

	<table  border="0" width="100%">
	<tr >
	
        <td class="labelText">
	<%
			if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
		%> 
			<siga:Idioma key="gratuita.busquedaSOJ.literal.solicitante"/>&nbsp;	
		 	<html:checkbox  name="PersonaJGForm" property="solicitante" disabled="<%=scheck%>" />
	<%
		}else{
	%>
	&nbsp;
	<%}%>
		</td>
	
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.tipo"/>		
		</td>

	
		<td>
			<%
				if (accion.equalsIgnoreCase("ver")) {
							String tip = "";
							if (miform.getIdTipoPersona() != null
									&& miform.getIdTipoPersona().equalsIgnoreCase("F")) {
								tip = UtilidadesString.getMensajeIdioma(usr,
										"gratuita.personaJG.literal.tipoFisica");
							} else if (miform.getIdTipoPersona() != null) {
								tip = UtilidadesString.getMensajeIdioma(usr,
										"gratuita.personaJG.literal.tipoJuridica");
							}
			%>
			<html:text property="tipo" value="<%=tip%>" size="5" styleClass="boxConsulta" readonly="true"></html:text>
			<%
				} else {
			%>
			
			<html:select styleId="idTipoPersona"   styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" property="idTipoPersona">				
				<bean:define id="tipos" name="PersonaJGForm"
						property="tipos" type="java.util.Collection" />
				<html:optionsCollection name="tipos" value="idTipo"
						label="descripcion" />					
			</html:select>		
			
			
			<%
				}
			%>
		</td>

		<td>			
		<%
			ArrayList tipoIdentificacionSel = new ArrayList();
					if (miform.getNIdentificacion() != null) {
						tipoIdentificacionSel.add(miform.getTipoId());
					}
			if (accion.equalsIgnoreCase("ver")) {
				String tipoIdent = (String) request.getAttribute("identificacion");
		%>			
				<siga:ComboBD nombre = "tipoId" tipo="cmbTipoIdentificacionConCIF" elementoSel="<%=tipoIdentificacionSel%>" clase="boxConsulta" obligatorio="true"  readonly="<%=sreadonly%>"  accion="comprobarTipoIdent();"/>
	    <%
		 	} else {
		  %>
		   		<html:select styleId="identificadores"  name="PersonaJGForm"  styleClass="boxCombo"  readOnly="false" property="tipoId" onchange="comprobarTipoIdent();"  >
					<bean:define id="identificadores" name="PersonaJGForm"
						property="identificadores" type="java.util.Collection" />
					<html:optionsCollection name="identificadores" value="idTipoIdentificacion"
						label="descripcion" />
				</html:select>
		   <%
		   	}
		   %>
		</td>
		<td class="labelText">
			<html:text name="PersonaJGForm" property="NIdentificacion" size="10" maxlength="20" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>"></html:text>
		</td>

		<td class="labelText" colspan="3">
			<div class="labelText" id="textoInformativo">
				<siga:Idioma key="gratuita.personaJG.literal.requiereNifCif"/>
			</div>
		</td>
	</tr>

	<tr>
		<td  align="left" colspan="2">
			<div class="labelText" id="perFisica"><siga:Idioma key="gratuita.personaJG.literal.nombre(*)"/></div>		
			<div class="labelText" id="perJuridica"><siga:Idioma key="gratuita.personaJG.literal.nombreDenoApe1"/></div>		
		</td>

		<td  colspan=2>
			<html:text name="PersonaJGForm" property="nombre" maxlength="100" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" style="width:160"></html:text>
		</td>

		<td class="labelText" colspan="1">
			<div class="labelText" id="perFisica1"><siga:Idioma key="gratuita.personaJG.literal.apellidos(*)"/></div>
			<div class="labelText" id="perJuridica1"><siga:Idioma key="gratuita.personaJG.literal.abreviatura(*)"/></div>										
		</td>


		<td  colspan="1" >
			<div class="labelText"><html:text name="PersonaJGForm" property="apellido1" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:150"></html:text></div>
		</td>
		
		<td>
			
			<div class="labelText" id="apelli2"><html:text name="PersonaJGForm" property="apellido2" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:150"></html:text></div>
		</td>
				
		<td>
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
				<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.buscar"/>" name="idButton"  onclick="return buscar();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.buscar"/>">
			<%}else{%> 
			&nbsp;
			<%}%>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.personaJG.literal.direccion">

	<table  align="center" width="100%">
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.direccion"/>
			
			<% if(opcionDireccion){
				%>
				<div id="desaparece">
				<%=asterisco%> 
				</div>
				<%
				
			}else	
				if (obligatorioDireccion) {
			%>
				<%=asterisco%> 
			<%
 				}
 			%>
		</td>
		<td>		
			<html:text name="PersonaJGForm" property="direccion" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:340" ></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.cp"/>	
			<% if(opcionDireccion){
				%>
				<div id="desapareceCp">
				<%=asterisco%> 
				</div>
				<%
				
			}else
					if (obligatorioCodigoPostal) {
				%>
				<%=asterisco%> 
			<%
 				}
 			%>		
		</td>
		<td colspan="2">
			<html:text name="PersonaJGForm" property="cp" size="5" maxlength="5" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"  onChange="createProvince()"></html:text>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.provincia"/>	
			<% if(opcionDireccion){
				%>
				<div id="desaparecePr">
				<%=asterisco%> 
				</div>
				<%
				
			}else
					if (obligatorioPoblacion) {
				%>
				<%=asterisco%> 
			<%
 				}
 			%>		
		</td>
		<td>
			<%
				ArrayList selProvincia = new ArrayList();
						if (miform.getProvincia() != null)
							selProvincia.add(miform.getProvincia());
			%>
<%
	//LMS 13/09/2006
			//Hack para cargar combos anidados dentro de 3 niveles de pestañas (Asistencias en Ficha Colegial).
			String sHack = "";
			if (esFichaColegial && bPestana.equals("true")) {
				//sHack = "top.frames[0].document.frames[0].document.frames[0].document.frames[0].document.getElementById('poblacionFrame').src";

				sHack += "var destino_provincia0=(document.getElementById('poblacionFrame')).src;";
				sHack += "var tam_provincia0 = destino_provincia0.indexOf('&id=');";
				sHack += "if(tam_provincia0==-1)";
				sHack += "{";
				sHack += "	tam_provincia0=destino_provincia0.length;";
				sHack += "}";
				sHack += "destino_provincia0=destino_provincia0.substring(0,tam_provincia0)+'&id='+provincia.value;";
				sHack += "(document.getElementById('poblacionFrame')).src=destino_provincia0;";

			} else {
				sHack = "Hijo:poblacion";
			}
%>
			
		  
			<siga:ComboBD pestana="<%=bPestana%>" nombre = "provincia" tipo="provincia" elementoSel="<%=selProvincia %>" clase="<%=classCombo %>" obligatorio="false" accion="<%=sHack%>" readOnly="<%=sreadonly%>" obligatorioSinTextoSeleccionar="false"/>
		 
		</td>
		<td class="labelText">
 			<siga:Idioma key="gratuita.personaJG.literal.poblacion"/>	
 			<% if(opcionDireccion){
				%>
				<div id="desaparecePo">
				<%=asterisco%> 
				</div>
				<%
				
			}else
	 				if (obligatorioPoblacion) {
	 			%>
				<%=asterisco%> 
			<%
 				}
 			%>	
		</td>
		<td colspan="2">
			<%
				ArrayList selPoblacion = new ArrayList();
						if (miform.getPoblacion() != null)
							selPoblacion.add(miform.getPoblacion());

						if (accion.equalsIgnoreCase("ver")) {
							String poblacion = (String) request
									.getAttribute("poblacion");
			%>
		   		<html:text property="poblacion" value="<%=poblacion%>" maxlength="100" styleClass="boxConsulta" readonly="true" style="width:220"></html:text>
		   <%
		   	} else {
		   %>
				<siga:ComboBD pestana="<%=bPestana%>" nombre="poblacion" tipo="poblacion" elementoSel="<%=selPoblacion%>" clase="<%=classCombo%>" obligatorio="true" hijo="t" readOnly="<%=sreadonly%>" obligatorioSinTextoSeleccionar="false" />
		   <%
		   	}
		   %>
		</td>
	</tr>
	<tr>
		<td class="labelText" width="30%" colspan="5" >
		<html:hidden name="PersonaJGForm" property = "existeDomicilio" value="S" />

	 <%
	     if(pcajgActivo == 4){
	 		if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
					|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
	 		if (!accion.equalsIgnoreCase("ver")) {
	 %>
	 		<siga:Idioma key="gratuita.busquedaSOJ.literal.solicitaObligaDir"/>
	 
			<input type="checkbox" id="existeDom" onclick="desabilitarDomicilio(this);">
		  <%
		  		} else {
		  %>
	 		<siga:Idioma key="gratuita.busquedaSOJ.literal.solicitaObligaDir"/>
	  <input type="checkbox" id="existeDom" onclick="desabilitarDomicilio(this);" disabled="disabled">		  
	  <%
		  		}
	     	}
	     }
		  %>
	</td>
	</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.personaJG.literal.inforAdicional">
	<table   align="center" width="100%" border="0">
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.nacionalidad"/>	
			<%
					if (obligatorioNacionalidad) {
				%>
				<%=asterisco%> 
			<%
 				}
 			%>		
		</td>
		<td width="20%">
			<%
				ArrayList selPais = new ArrayList();
						if (miform.getNacionalidad() != null) {
							selPais.add(miform.getNacionalidad());
						}/* else {// Aquí se quita la obligatoriedad al campo idPais y si no se selecciona ninguno NO se pone España por defecto
																																		// RGG seleccion automática de España
																																		selPais.add(ClsConstants.ID_PAIS_ESPANA);
																																	}*/
			%>
			<siga:ComboBD pestana="<%=bPestana%>" elementoSel="<%=selPais %>" nombre = "nacionalidad" tipo="pais" ancho="200" clase="<%=classCombo %>" readOnly="<%=sreadonly%>"/>
		</td>
		<td class="labelText" width="140">
			<siga:Idioma key="gratuita.personaJG.literal.fechaNac"/>	
			<%
				if (obligatorioFechaNac) {
			%>
				<%=asterisco%> 
			<%
 				}
 			%>		
		</td>
		<td >
		   <%
		   String fechaNac="";
			if ((miform.getFechaNac() != null)
								&& (!miform.getFechaNac().equalsIgnoreCase(""))) {
				 fechaNac = miform.getFechaNac();
						}
		%>
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
			 <siga:Fecha  nombreCampo= "fechaNac" valorInicial="<%=fechaNac%>" postFunction="proFechaNac();" />
			<%
				}else{
			%>
			 <siga:Fecha  nombreCampo= "fechaNac"  valorInicial="<%=fechaNac%>" disabled="true"/>
			 <%
				}
			%>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.edad"/>	
		</td>
				<%
			if ((miform.getEdad() != null)
								&& (!miform.getEdad().equalsIgnoreCase(""))) {
					edad = miform.getEdad();
						}
		%>
		<td>
			<%
				if ((obligatorioFechaNac)||(accion.equalsIgnoreCase("ver"))||(fechaNac!="")) {
			%>
				<html:text name="PersonaJGForm" value ="<%=edad %>" property="edad" size="3" styleClass="boxConsulta" readOnly="true"/>
			<%
				}else{
			%>
			 	<html:text name="PersonaJGForm" onkeypress="return soloDigitos(event)" value ="<%=edad %>" property="edad" size="3" styleClass="<%=estiloBox %>"/>
			<%
				}
			%>	
					
		</td>

	</tr>
	
	<tr>
			<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.estadoCivil"/>	
			<%if (obligatorioEstadoCivil){%>
					<%=asterisco%> 
				<%}%>			 
		</td>
		<td>
			<%
				ArrayList selEstadoCiv = new ArrayList();
						if (miform.getEstadoCivil() != null)
							selEstadoCiv.add(miform.getEstadoCivil());
			%>
			<siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="<%=classCombo%>" elementoSel="<%=selEstadoCiv%>" readOnly="<%=sreadonly%>"/>
		</td>
				<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.regimenConyugal"/>	
			<%if (obligatorioRegimenConyuge){%>
					<%=asterisco%> 
				<%}%>	
			</td>
		<td>
			<%
				if (accion.equalsIgnoreCase("ver")) {
							String regimen = "";
							if (miform.getRegimenConyugal() != null) {
								if (miform.getRegimenConyugal().equalsIgnoreCase(
										"G")) {
									regimen = UtilidadesString
											.getMensajeIdioma(usr,
													"gratuita.personaJG.regimen.literal.gananciales");
								}
								if (miform.getRegimenConyugal().equalsIgnoreCase(
										"I")) {
									regimen = UtilidadesString
											.getMensajeIdioma(usr,
													"gratuita.personaJG.regimen.literal.indeterminado");
								}
								if (miform.getRegimenConyugal().equalsIgnoreCase(
										"S")) {
									regimen = UtilidadesString
											.getMensajeIdioma(usr,
													"gratuita.personaJG.regimen.literal.separacion");
								}
							}						
			%>
				<html:text name="PersonaJGForm" property="regimenConyugal" value="<%=regimen%>" size="18" styleClass="boxConsulta" readonly="true"></html:text>
			<%
				} else {
							String reg = miform.getRegimenConyugal();
							if (reg == null)
								reg = new String("");
			%>
				<html:select styleClass="boxCombo" name="PersonaJGForm" value="<%=reg%>" property="regimenConyugal" readOnly="false">
					<html:option value=""></html:option>
					<html:option value="G"><siga:Idioma key="gratuita.personaJG.regimen.literal.gananciales"/></html:option>
					<html:option value="S"><siga:Idioma key="gratuita.personaJG.regimen.literal.separacion"/></html:option>
					<html:option value="I"><siga:Idioma key="gratuita.personaJG.regimen.literal.indeterminado"/></html:option>	
				</html:select>
			<%
				}
			%>
		</td>
		

		<%	if (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) { %>
		 <td class="labelText">
		 <siga:Idioma key="gratuita.busquedaSOJ.literal.grupoLaboral"/>
	     </td>		
	    <%
			    	if (miform.getTipoGrupoLaboral() != null) {
			    					tipoGrupoLaboral = miform.getTipoGrupoLaboral();
			    				} else {
			    					tipoGrupoLaboral = "";
			    				}
			    				ArrayList selTipoGrupoLaboral = new ArrayList();
			    				selTipoGrupoLaboral.add(tipoGrupoLaboral);
			    %>		
	    <td>
	       <%
	       	if (!accion.equalsIgnoreCase("ver")) {
	       %>
		       <siga:ComboBD nombre = "tipoGrupoLaboral" tipo="cmbTipoGrupoLaboral"  ancho="150" clase="boxCombo"  parametro="<%=dato%>" elementoSel="<%=selTipoGrupoLaboral%>"   />	
	       <%
		       	} else {
		       %>	
	           <siga:ComboBD nombre = "tipoGrupoLaboral" tipo="cmbTipoGrupoLaboral" ancho="150" readonly="true" clase="boxComboConsulta"  parametro="<%=dato%>" elementoSel="<%=selTipoGrupoLaboral%>" />	
  	       <%
	  	       	}
	  	       %>	
     	</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.idioma"/>		
		</td>
		<td>
			<%
				if (miform.getSexo() != null) {
							idioma = miform.getIdioma();
						} else {
							idioma = "";
						}
						ArrayList selIdioma = new ArrayList();
						selIdioma.add(idioma);
			%>
			<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="<%=classCombo%>"  elementoSel="<%=selIdioma%>" readOnly="<%=sreadonly%>"/>
		</td>
			<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.profesion"/>		
		</td>
		<td>
			<%
				ArrayList selProfe = new ArrayList();
						if (miform.getProfesion() != null)
							selProfe.add(miform.getProfesion());
			%>
			<siga:ComboBD nombre = "profesion" tipo="cmbProfesion" clase="<%=classCombo%>" elementoSel="<%=selProfe%>" readOnly="<%=sreadonly%>"/>
		</td>

     	<html:hidden name="PersonaJGForm" value ="<%=nHijos %>" property="hijos"/>
		<%
			} else if (conceptoE
							.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)
							|| conceptoE.equals(PersonaJGAction.SOJ)) {
		%>
		<%
			if ((miform.getHijos() != null)
								&& (!miform.getHijos().equalsIgnoreCase(""))) {
							nHijos = miform.getHijos();
						}
		%>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.nHijos"/>	
		</td>
		<td>
			<html:text name="PersonaJGForm" value ="<%=nHijos %>" property="hijos" size="3" styleClass="<%=estiloBox %>"/>
		</td>
		<%
			} else {
		%>
			<html:hidden name="PersonaJGForm" value ="<%=nHijos %>" property="hijos"/>
		<%
			}
		%>
	
<%
	 	if (conceptoE.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
	 					|| conceptoE
	 							.equals(PersonaJGAction.DESIGNACION_REPRESENTANTE)
	 					|| conceptoE
	 							.equals(PersonaJGAction.EJG_REPRESENTANTE)
	 					|| conceptoE
	 							.equals(PersonaJGAction.SOJ_REPRESENTANTE)) {
	 %>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.enCalidadDe"/>		
		</td>
		<td>
			<siga:ComboBD nombre = "enCalidadDe" tipo="cmbEnCalidadDe" clase="<%=estiloBox%>" readOnly="<%=sreadonly%>"/>
		</td>
<%
	} else if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
%>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.parentescoNormalizado"/>
			<%
				if (obligatorioParentesco) {
			%>
				<%=asterisco%> 
			<%
 				}
 			%>			
		</td>
		<td  style="display:none">
			<html:text name="PersonaJGForm" property="enCalidadDeLibre" size="10" maxlength="20" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>
		<td  >
		<%
			ArrayList selParentesco = new ArrayList();
						if (miform.getParentesco() != null)
							selParentesco.add(miform.getParentesco());
						String paramParentesco[] = {usr.getLocation()};
		%>
			<siga:ComboBD  nombre="parentesco" tipo="cmbParentesco" elementoSel="<%=selParentesco %>" parametro="<%=paramParentesco%>" clase="<%=classCombo %>" obligatorio="false" readOnly="<%=sreadonly%>" obligatorioSinTextoSeleccionar="false"/>
		</td>
<%
	} else if (conceptoE
					.equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
%>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.calidad"/>&nbsp;(*)			
		</td>		
			<td>
				<%if(!accion.equalsIgnoreCase("ver")){%>
						<siga:ComboBD nombre="calidad2" tipo="ComboCalidades" ancho="200" clase="boxCombo" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="true"  obligatorioSinTextoSeleccionar="true" parametro="<%=datos2%>" elementoSel="<%=calidadSel%>" readonly="false"/>           	   
					<%}else{%>
						<siga:ComboBD nombre="calidad2" tipo="ComboCalidades" ancho="200" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=calidadSel%>" readonly="true"/>           	   
			<%}%>
		</td>
<%
	}
%>
	</tr>
	<tr>
			<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.sexo"/>	
			<%if (obligatorioSexo){%>
				<%=asterisco%> 
			<%}%>		
		</td>
		<td >
		<%
			String ssexo = "";
					if (miform.getSexo() != null) {
						sexo = miform.getSexo();
					} else {
						sexo = "";
					}
					if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE))
						ssexo = UtilidadesString.getMensajeIdioma(usr,
								"censo.sexo.hombre");
					if (sexo.equals(ClsConstants.TIPO_SEXO_MUJER))
						ssexo = UtilidadesString.getMensajeIdioma(usr,
								"censo.sexo.mujer");

					if (!accion.equalsIgnoreCase("ver")) {
		%>
		    <html:select name="PersonaJGForm" property="sexo"  styleClass = "<%=estiloBox %>" value="<%=sexo%>"   >
			<html:option value="0" >&nbsp;</html:option>
			<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
			<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
			</html:select>		
		<%
					} else {
				%>
			<html:hidden  name="PersonaJGForm" property="sexo" value="<%=sexo %>"/>
			<html:text name="PersonaJGForm" property="ssexo" size="20" styleClass="<%=estiloBox %>" value="<%=ssexo %>"  ></html:text>				
		<%
							}
						%>					

			
		</td>
		 <td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.minusvalia"/>	
			<%if (obligatorioMinusvalia){%>
					<%=asterisco%> 
				<%}%>	
		</td>
		<td>
		<%
				ArrayList selMinus = new ArrayList();
						if (miform.getMinusvalia() != null){
							selMinus.add(miform.getMinusvalia());
						}else{
							if(minusDefecto!=null)
							selMinus.add(minusDefecto);
						}	
			%>
			<siga:ComboBD nombre = "minusvalia" tipo="cmbMinusvalia" clase="<%=classCombo%>"  parametro="<%=dato%>"  elementoSel="<%=selMinus%>" readOnly="<%=sreadonly%>"/>
			
		</td>
		
		<% if (conceptoE.equals(PersonaJGAction.SOJ)) { %>
			<td class="labelText">
				<siga:Idioma key="gratuita.personaJG.literal.idioma"/>		
			</td>
			
			<td>
				<%	if (miform.getSexo() != null) {
						idioma = miform.getIdioma();
					} else {
						idioma = "";
					}
					
					ArrayList selIdioma = new ArrayList();
					selIdioma.add(idioma);
					
				%>
				<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="<%=classCombo%>"  elementoSel="<%=selIdioma%>" readOnly="<%=sreadonly%>"/>
			</td>		
		<% } %>	
	</tr>
	</table>
	</siga:ConjCampos>

<%
	if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
%> 
	<!-- REPRESENTANTE LEGAL -->
<script>
	
	function limpiarPersona() {
		document.PersonaJGForm.idPersonaRepresentante.value ="";
		document.PersonaJGForm.ncolegiadoRepresentante.value="";
		document.PersonaJGForm.representante.value="";
		return false;		
	}
		
	
	
function buscarPersona() {		
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
		if (resultado != null && resultado[0]!=null){		
			document.PersonaJGForm.idPersonaRepresentante.value       = resultado[0];
			document.PersonaJGForm.ncolegiadoRepresentante.value    = resultado[2];
			document.PersonaJGForm.representante.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];			 
	   }
		
	}

	

function buscarPersonaContrario() {		
		
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
		if (resultado != null && resultado[0]!=null)
		{			
			document.PersonaJGForm.idPersonaContrario.value       = resultado[0];
			document.PersonaJGForm.ncolegiadoContrario.value    = resultado[2];
			document.PersonaJGForm.abogadoContrario.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
	   }
	}

	
function limpiarPersonaContrario() {
	document.PersonaJGForm.idPersonaContrario.value="";
	document.PersonaJGForm.ncolegiadoContrario.value="";
	document.PersonaJGForm.abogadoContrario.value="";
	
	return false;		
}


	
</script>
	<siga:ConjCampos leyenda="gratuita.personaJG.literal.representantes">
	<table   align="center" width="100%">
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.representanteLegal"/>		
		</td>
		<td>
 			
 					
 					<html:hidden  name="PersonaJGForm" property="ncolegiadoRepresentante"  styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" ></html:hidden> 	
		 			 <html:hidden  name="PersonaJGForm" property="idPersonaRepresentante" styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10"></html:hidden>
		 			 <html:text  name="PersonaJGForm" property="representante" size="70" maxlength="200" styleClass="boxConsulta"  readOnly="false"></html:text>
 			         		 			 
			
			
		</td>
		
		<td width="100">
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>			
			<input type="button" class="button" id="idButton" name="Buscar" value="<siga:Idioma key="general.boton.search" />" onClick="buscarPersona();">
			<%
				}
			%>
		</td>
		<td width="100">
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
			<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarPersona();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
			<%
				}
			%>						
		</td>		
		
		
	</tr>
	
	
	
	<tr>
	
	<td class="labelText">
			<siga:Idioma key="envios.etiquetas.tipoCliente.abogado"/>	
		</td>
		<td>	
		       
					<html:hidden  name="PersonaJGForm" property="ncolegiadoContrario" styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" ></html:hidden> 	
		 			<html:hidden  name="PersonaJGForm" property="idPersonaContrario" styleClass="box"  readOnly="true" size="10" maxlength="10"></html:hidden>
		 			<html:text  name="PersonaJGForm" property="abogadoContrario" size="70" maxlength="200" styleClass="boxConsulta"  readOnly="false"></html:text>
					
							
		</td>		
		<td> 
		<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
		  	<input type="button" class="button" id="idButton" name="Buscar" value="<siga:Idioma key="general.boton.search" />" onClick="buscarPersonaContrario();">
			<%
				}
			%>
			
		</td>
		<td>	
		<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
		 	  <input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarPersonaContrario();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
			<%
				}
			%>
		 
		</td>
				
		
	
	</tr>
	
	
	<tr>
	<%if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
         %>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.procurador"/>		
		</td>
		<td>
			<%
				ArrayList selProcu = new ArrayList();
							if (miform.getIdProcurador() != null)
								selProcu.add(miform.getIdProcurador());
							String paramProcu[] = { usr.getLocation() };
			%>
			
			<%
				if (!accion.equalsIgnoreCase("ver")) {%>
					<siga:ComboBD nombre = "idProcurador" tipo="comboProcuradores"  clase="<%=classCombo%>" elementoSel="<%=selProcu%>" parametro="<%=paramProcu%>" readOnly="<%=sreadonly%>"/>
			<%}else{%>		
			        <siga:ComboBD nombre = "idProcurador"  ancho="500" tipo="comboProcuradores"  clase="<%=classCombo%>" elementoSel="<%=selProcu%>" parametro="<%=paramProcu%>" readOnly="<%=sreadonly%>"/>
			<%}%>			
		</td>
		<%}%>
	</tr>
	</table>
	</siga:ConjCampos>
<%
	} else {
%> 
<%
 	if (!conceptoE.equals(PersonaJGAction.PERSONAJG)) {

 				String nomRep = (String) request
 						.getAttribute("nombreRepresentanteJG");
 				if (nomRep == null)
 					nomRep = "";
 %> 
	<!-- REPRESENTANTE TUTOR -->
<script>
	function limpiarTutor() {
		document.forms[0].idRepresentanteJG.value="";
		document.forms[0].representante.value="";
		return false;
	}
	function buscarTutor() {
		document.representanteTutor.idPersonaJG.value=document.forms[0].idRepresentanteJG.value;
		document.representanteTutor.idPersonaPER.value=document.forms[0].idPersonaJG.value;
		document.representanteTutor.idInstitucionPER.value=document.forms[0].idInstitucionJG.value;
		var resultado = ventaModalGeneral("representanteTutor","G");			
		if (resultado != null && resultado[0]!=null  && resultado[1]!=null)
		{
			document.forms[0].idRepresentanteJG.value = resultado[0];
			document.forms[0].representante.value = resultado[1];	
			
		}		
	}

	function buscarEJGPersonaContrario() {	
			
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");	
					
		if (resultado != null && resultado[0]!=null)
		{			
			document.PersonaJGForm.idAbogadoContrarioEJG.value = resultado[0];
			document.PersonaJGForm.ncolegiadoContrario.value   = resultado[2];
			document.PersonaJGForm.abogadoContrarioEJG.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
	   }
	}



	function limpiarAbogadoEjg() {
		document.forms[0].ncolegiadoContrario.value="";
		document.forms[0].abogadoContrarioEJG.value="";
		document.PersonaJGForm.idAbogadoContrarioEJG.value="";
		return false;		
	}

	
	
	
	
</script>
   <%
   	if (conceptoE.equals(PersonaJGAction.SOJ)) {
   %>
  	 <siga:ConjCampos
			leyenda="gratuita.personaJG.literal.Contacto">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr >
							<td class="labelText" >
								<siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>								
							</td>										
							<td class="labelTextValor">													
								<html:text name="PersonaJGForm" property="correoElectronico" maxlength="50" style="width:310px"  styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
							</td>	
							<td rowspan=2><siga:ConjCampos
								leyenda="gratuita.personaJG.literal.telefonos"><iframe align="top"
								src="<%=app%>/JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersona%>&idInstitucion=<%=usr.getLocation()%>&esFichaColegial=<%=sEsFichaColegial%>"
								id="resultado" name="resultado" scrolling="no" frameborder="0"
								marginheight="0" marginwidth="0"
								style="width: 500px; height:85px;"> </iframe></siga:ConjCampos></td>							
						</tr>				
						<tr >
							<td class="labelText" colspan="1" align="center">
								<siga:Idioma key="censo.preferente.fax"/>								
							</td >
							<td class="labelTextValor" >
								<html:text name="PersonaJGForm" property="fax" maxlength="20" style="width:150px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>								
							</td>	
						</tr>
						
						
											
								
			</table>
		</siga:ConjCampos>   
		
   <%
   	}
   %>
	<siga:ConjCampos leyenda="gratuita.personaJG.literal.representantes">
	<table  align="center" width="100%">
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.representanteLegal"/>		
		</td>
		<td>
			<html:text name="PersonaJGForm" property="representante" maxlength="200" styleClass="boxConsulta"  readOnly="true" value="<%= nomRep %>" style="width:600"></html:text>
			<html:hidden name="PersonaJGForm" property="idRepresentanteJG"></html:hidden>
		</td>
		<td width="100">
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
			<input type="button" alt="<siga:Idioma key="general.boton.search"/>" name="idButton" onclick="return buscarTutor();" class="button" value="<siga:Idioma key="general.boton.search"/>">
			<%
				}
			%>
		</td>
		<td width="100">
			<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
			<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarTutor();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
			<%
				}
			%>
		</td>
	</tr>
	<%if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)) { %>
	<tr>
		<td class="labelText">
			<siga:Idioma key="envios.etiquetas.tipoCliente.abogado"/>	
		</td>
		<td>	
		    	<html:hidden  name="PersonaJGForm" property="ncolegiadoContrario" styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" ></html:hidden> 	
		 		<html:hidden  name="PersonaJGForm" property="idAbogadoContrarioEJG" styleClass="box"  readOnly="false" size="10" maxlength="10"></html:hidden>
		 		<html:text  name="PersonaJGForm" property="abogadoContrarioEJG" size="70" maxlength="200" styleClass="boxConsulta"  readOnly="false"></html:text>
					
			 			
		</td>		
		
		<td> 
		<%	if (!accion.equalsIgnoreCase("ver")) {
			%>
		  	<input type="button" alt="<siga:Idioma key="general.boton.search"/>" name="idButton" onclick="return buscarEJGPersonaContrario();" class="button" value="<siga:Idioma key="general.boton.search"/>">
			<%
				}
			%>
			
		</td>
		<td>	
		<%
				if (!accion.equalsIgnoreCase("ver")) {
			%>
		 	  <input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarAbogadoEjg();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
			<%
				}
			%>
		 
		</td>
											
		</tr>	
	<tr>
	
	<td class="labelText">
			<siga:Idioma key="gratuita.personaJG.literal.procurador"/>		
		</td>
		<td>
			<%
				ArrayList selProcu = new ArrayList();
							if (miform.getIdProcurador() != null)
								selProcu.add(miform.getIdProcurador());
							String paramProcu[] = { usr.getLocation() };
			%>	
			<%
				if (!accion.equalsIgnoreCase("ver")) {%>
					<siga:ComboBD nombre = "idProcurador" tipo="comboProcuradores"  clase="<%=classCombo%>" elementoSel="<%=selProcu%>" parametro="<%=paramProcu%>" readOnly="<%=sreadonly%>"/>
			<%}else{%>		
			        <siga:ComboBD nombre = "idProcurador"  ancho="500" tipo="comboProcuradores"  clase="<%=classCombo%>" elementoSel="<%=selProcu%>" parametro="<%=paramProcu%>" readOnly="<%=sreadonly%>"/>
			<%}%>			
		</td>
	
	</tr>
	
	
	<%}%>
	
	</table>
	</siga:ConjCampos>

<%
	}
		}
%>

</td>
</tr>
</table>


<table class="tablaCentralCampos" width="100%" border=0 cellpadding=0 cellspacing=0>
<tr>
<td valign="top">
<%
	if (conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)
				|| conceptoE
						.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
%> 
<!-- para observaciones -->
	<siga:ConjCampos leyenda="gratuita.personaJG.literal.observaciones">
	<table width="100%" >
	<tr>
		<td height="156px">
			<html:textarea name="PersonaJGForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" property="observaciones" cols="60" rows="7" style="width:500px" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>"></html:textarea>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
<%
	} else if (conceptoE.equals(PersonaJGAction.EJG)
				|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
%> 

	<%
 		String tipoIngreso;
 				if (miform.getTipoIngreso() != null) {
 					tipoIngreso = miform.getTipoIngreso();
 				} else {
 					tipoIngreso = "";
 				}
 				ArrayList selTipoIngreso = new ArrayList();
 				selTipoIngreso.add(tipoIngreso);
 	%>
<!-- para datos financieros -->
 	<siga:ConjCampos leyenda="gratuita.personaJG.literal.datosFinancieros">
	<table width="100%" >
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.tipoIngresos"/>
			<%
				if (obligatorioTipoIngreso) {
			%>
				<%=asterisco%> 
			<%
 				}
 			%>
		</td>

		<td>
		<%
			if (!accion.equalsIgnoreCase("ver")) {
		%>
			<siga:ComboBD nombre = "tipoIngreso" tipo="tipoIngreso" clase="boxCombo" elementoSel="<%=selTipoIngreso%>"/>
		<%
			} else {
		%>
			<siga:ComboBD nombre = "tipoIngreso" tipo="tipoIngreso" clase="boxComboConsulta" elementoSel="<%=selTipoIngreso%>" readonly="true"/>
		<%
			}
		%>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.ingresos"/>
			<%
				if (obligatorioIngreso) {
			%>
				<%=asterisco%> 
			<%
 				}
 			%>
		</td>

		<td>
			<html:text name="PersonaJGForm" property="ingresosAnuales" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>

		<td class="labelTextValor">
			<html:text name="PersonaJGForm" property="importeIngresosAnuales" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeIngresosAnuales)%>"></html:text>&nbsp;&nbsp;&euro;
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.bienesInmuebles"/>
		</td>
		<td>
			<html:text name="PersonaJGForm" property="bienesInmuebles" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>
		<td class="labelTextValor">
			<html:text name="PersonaJGForm" property="importeBienesInmuebles" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeBienesInmuebles)%>"></html:text>&nbsp;&nbsp;&euro;
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.bienesMobiliarios"/>
		</td>
		<td>
			<html:text name="PersonaJGForm" property="bienesMuebles" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>
		<td class="labelTextValor">
			<html:text name="PersonaJGForm" property="importeBienesMuebles" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>"  readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeBienesMuebles)%>"></html:text>&nbsp;&nbsp;&euro;
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.otrosBienes"/>
		</td>
		<td>
			<html:text name="PersonaJGForm" property="otrosBienes" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>
		<td class="labelTextValor">
			<html:text name="PersonaJGForm" property="importeOtrosBienes" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeOtrosBienes)%>"></html:text>&nbsp;&nbsp;&euro;
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarInteresado.literal.finanzas.observaciones"/>
		</td>
		<td colspan="2">
			<html:text name="PersonaJGForm" property="unidadObservaciones" maxlength="1024" style="width:376px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
<%
	}

%>

</td>

	<%if (!(conceptoE.equals(PersonaJGAction.SOJ))) {%>
		<td valign="top"><!-- para Telefonos --> <siga:ConjCampos
			leyenda="gratuita.personaJG.literal.Contacto">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr height="10px">
							<td class="labelText" >
								<siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>								
							</td>	
													
							<td class="labelTextValor">													
								<html:text name="PersonaJGForm" property="correoElectronico" maxlength="50" style="width:310px"  styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
							</td>
									
						</tr>
				
						<tr height="10px">
							<td class="labelText" >
								<siga:Idioma key="censo.preferente.fax"/>								
							</td >
							<td class="labelTextValor" >
								<html:text name="PersonaJGForm" property="fax" maxlength="20" style="width:150px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
								
							</td>
					
						</tr>
			
				<tr>
					<td colspan="2"><siga:ConjCampos
						leyenda="gratuita.personaJG.literal.telefonos">
					<iframe 
						src="<%=app%>/JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersona%>&idInstitucion=<%=usr.getLocation()%>&esFichaColegial=<%=sEsFichaColegial%>"
						id="resultado" name="resultado" scrolling="no" frameborder="0"
						marginheight="0" marginwidth="0"
						style="width: 400px; height:90px;"> </iframe></siga:ConjCampos></td>
				</tr>
			</table>
		</siga:ConjCampos>
		    
								
		</td>
		
		 <%} else{%>
		 
	<td> 
		   <siga:ConjCampos leyenda="gratuita.personaJG.literal.estadisticaSOJ">
    <table   align="center" width="100%" border="0">
     <tr>	
	<td class="labelText" >
		<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoConoce"/>
	</td>			
	<%
					if (miform.getTipoConoce() != null) {
											tipoConoce = miform.getTipoConoce();
										} else {
											tipoConoce = "";
										}
										ArrayList selTipoConoce = new ArrayList();
										selTipoConoce.add(tipoConoce);
				%>
			   
	<td width="30%">
	<%
		if (!accion.equalsIgnoreCase("ver")) {
	%>
		<siga:ComboBD nombre = "tipoConoce" tipo="cmbTipoConoce" clase="boxCombo"  parametro="<%=dato%>" elementoSel="<%=selTipoConoce%>" />
	<%
		} else {
	%>
	   <siga:ComboBD nombre = "tipoConoce" tipo="cmbTipoConoce" clase="boxComboConsulta"  readonly="true" parametro="<%=dato%>" elementoSel="<%=selTipoConoce%>" />
	<%
		}
	%>	
	</td>
	<td class="labelText"width="30%">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.solicitaInfoJG"/>
	</td>				
	<td align="left">
	 <%
	 	if (!accion.equalsIgnoreCase("ver")) {
	 %>
	  <html:checkbox  name="PersonaJGForm" property="chkSolicitaInfoJG"  />	
	  <%
		  	} else {
		  %>
	   <html:checkbox  name="PersonaJGForm" disabled="true" property="chkSolicitaInfoJG"  />	
	  <%
		  	}
		  %>	
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.grupoLaboral"/>
	</td>		
	<%
				if (miform.getTipoGrupoLaboral() != null) {
										tipoGrupoLaboral = miform
												.getTipoGrupoLaboral();
									} else {
										tipoGrupoLaboral = "";
									}
									ArrayList selTipoGrupoLaboral = new ArrayList();
									selTipoGrupoLaboral.add(tipoGrupoLaboral);
			%>		
	<td>
	  <%
	  	if (!accion.equalsIgnoreCase("ver")) {
	  %>
		<siga:ComboBD nombre = "tipoGrupoLaboral" tipo="cmbTipoGrupoLaboral" clase="boxCombo"   parametro="<%=dato%>" elementoSel="<%=selTipoGrupoLaboral%>" />	
	 <%
		 	} else {
		 %>	
	   <siga:ComboBD nombre = "tipoGrupoLaboral" tipo="cmbTipoGrupoLaboral" readonly="true" clase="boxComboConsulta"  parametro="<%=dato%>" elementoSel="<%=selTipoGrupoLaboral%>" ancho="150"/>	
	 <%
		 	}
		 %>	
	</td>
	<td class="labelText">
	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.pideJG" />
	
	</td>				
	<td align="left">
	 <%
	 	if (!accion.equalsIgnoreCase("ver")) {
	 %>
	   <html:checkbox  name="PersonaJGForm" property="chkPideJG"  />	
	  <%
		  	} else {
		  %>	
	   <html:checkbox  name="PersonaJGForm" disabled="true" property="chkPideJG"  />
	  <%
	  	}
	  %>	 	
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.NsolicitaInfoJG"/>
	</td>			
	<%
					if (miform.getNumVecesSOJ() != null) {
											numVecesSOJ = miform.getNumVecesSOJ();
										} else {
											numVecesSOJ = "";
										}
										ArrayList selNumVecesSOJ = new ArrayList();
										selNumVecesSOJ.add(numVecesSOJ);
				%>		
	<td colspan="3">
	 <%
	 	if (!accion.equalsIgnoreCase("ver")) {
	 %>
		<siga:ComboBD nombre = "numVecesSOJ" tipo="cmbNumVecesSOJ" clase="boxCombo"  parametro="<%=dato%>" elementoSel="<%=selNumVecesSOJ%>" />	
	<%
			} else {
		%>		
	   <siga:ComboBD nombre = "numVecesSOJ" tipo="cmbNumVecesSOJ" clase="boxComboConsulta" readonly="true" parametro="<%=dato%>" elementoSel="<%=selNumVecesSOJ%>" />	
	<%
			}
		%>	 	
	</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</td>
		<%}%>	
	</tr>
	<!-- Ajax -->
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxTipoIdentificacion"
	source="idTipoPersona" target="identificadores"		
	parameters="idTipoPersona={idTipoPersona}" postFunction="comprobarTipoPersona"
	/>
	
<ajax:updateFieldFromSelect
	baseUrl="/SIGA${path}.do?modo=getAjaxBusquedaNIF"
	source="forzarAjax" target="NIdentificacion,idInstitucionJG,idPersonaJG,nombre,apellido1,apellido2,direccion,cp,fechaNac,minusvalia,provincia,poblacion,estadoCivil,regimenConyugal,idTipoPersona,identificadores,representante,nacionalidad,sexo,edad,fax,correoElectronico,idioma,profesion,existeDomicilio,enCalidadDe,hijos"
	parameters="NIdentificacion={NIdentificacion}, conceptoE={conceptoE}"
	postFunction="postAccionBusquedaNIF"
	preFunction="preAccionBusquedaNIF"
/>
<ajax:updateFieldFromSelect
	baseUrl="/SIGA${path}.do?modo=getAjaxExisteNIF"
	source="NIdentificacion" target="existeNIF" 
	parameters="NIdentificacion={NIdentificacion}"
	preFunction="preAccionExisteNIF"
	postFunction="postAccionExisteNIF" />
	
</html:form>
</table>


<%
	String sClasePestanas = esFichaColegial
			? "botonesDetalle3"
			: "botonesDetalle";
%>

<%
	//	if (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.SOJ) || conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO) || conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) { 
	// BOTONES PARA PESTAÑAS
	String sBoton = null;
	if (pantalla.equals("P")) {

		if (conceptoE.equals(PersonaJGAction.EJG))
			sBoton = esFichaColegial ? "G,R" : "V,G,R";
		else
			sBoton = esFichaColegial ? "G,R" : "V,G,R";

	} else {
		// BOTONES PARA MODAL

		if (conceptoE.equals(PersonaJGAction.EJG))
			sBoton = "Y,C";
		else
			sBoton = "Y,C";

	}
%>
<siga:ConjBotonesAccion botones="<%=sBoton%>" modo="<%=accion%>" clase="botonesDetalle"/>

</div>

	
<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
<script language="JavaScript">


	
<%// VOLVER PARA CADA CASO
			if (conceptoE.equals(PersonaJGAction.EJG)) {%>
	
		//Asociada al boton Volver -->
		function accionVolver()   
		{	
		 	document.forms[0].action="<%=app%>/JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar(){	

 			document.PersonaJGForm.existeDomicilio.value = "S";
 		
			var lNumerosTelefonos=getDatos();				
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}			
			sub();
			var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;		
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
			
			//if(!validaEdad()){
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				//alert("<siga:Idioma key='gratuita.personaJG.messages.EdadErronea'/>");
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}
							
			document.forms[0].importeIngresosAnuales.value=document.forms[0].importeIngresosAnuales.value.replace(/,/,".").trim();
			document.forms[0].importeBienesInmuebles.value=document.forms[0].importeBienesInmuebles.value.replace(/,/,".").trim();
			document.forms[0].importeBienesMuebles.value=document.forms[0].importeBienesMuebles.value.replace(/,/,".").trim();
			document.forms[0].importeOtrosBienes.value=document.forms[0].importeOtrosBienes.value.replace(/,/,".").trim();
		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarEJG';
			document.forms[0].target="submitArea2";		
			var tipo = document.PersonaJGForm.idTipoPersona.value;		
			var tipoId = document.PersonaJGForm.tipoId.value;	
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";


			var envio=1;
				if (isNaN(document.forms[0].importeIngresosAnuales.value)||
						document.forms[0].importeIngresosAnuales.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.IngresosAnuales'/>");
					envio=-1;
					fin();
					return false;
					
				}
				if (isNaN(document.forms[0].importeBienesInmuebles.value)||
						document.forms[0].importeBienesInmuebles.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.BienesInmuebles'/>");
					envio=-1;
					fin();
					return false;
					
				}
					if (isNaN(document.forms[0].importeBienesMuebles.value)||
							document.forms[0].importeBienesMuebles.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.BienesMuebles'/>");
					envio=-1;
					fin();
					return false;
					
				}
				if (isNaN(document.forms[0].importeOtrosBienes.value)||
						document.forms[0].importeOtrosBienes.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.OtrosBienes'/>");
					envio=-1;
					fin();
					return false;
					
				}
					
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else if (validatePersonaJGForm(document.forms[0])){
								
					// jbd: comprobaciones adicionales para el pcajg
					if(<%=pcajgActivo > 0%>){
						var error = "";
						if (<%=obligatorioTipoIdentificador%>){
							if( document.forms[0].tipoId.value=="")
								error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>"+ '\n';
							if ((document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>" || 
								document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>" ||
								document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&
								document.forms[0].NIdentificacion.value=="")
								error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nIdentificacion'/>"+ '\n';
						}
						if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1)
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
						if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
						if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
						if (<%=obligatorioNacionalidad%> && document.forms[0].nacionalidad.value =="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nacionalidad'/>"+ '\n';						
						if (<%=obligatorioIngreso%> && document.forms[0].importeIngresosAnuales.value =="")
								error += "<siga:Idioma key='errors.required' arg0='gratuita.operarInteresado.literal.ingresos'/>"+ '\n';
						if(<%=pcajgActivo == 4%>){
							if (<%=obligatorioSexo%> && document.forms[0].sexo.value=="0")
								error += "<siga:Idioma key='errors.required' arg0='Sexo'/>"+ '\n';
							if (<%=obligatorioEstadoCivil%> && document.forms[0].estadoCivil.value=="")
								error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.estadoCivil'/>"+ '\n';										
						}
						if(error!=""){
							alert(error);
							fin();
							return false;
						}
					}
				    	document.forms[0].submit();
					}else{
						fin();
						return false;
					}		
			}		
		}			
		function refrescarLocal() {
			window.location=window.location;
		}

<%} else
			// VOLVER PARA CADA CASO
			if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {%>
	
		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	{


			if (document.forms[0].existeDom!=null && document.forms[0].existeDom.checked) {
	 			document.PersonaJGForm.existeDomicilio.value = "N";
			}else {
	 			document.PersonaJGForm.existeDomicilio.value = "S";
			}
		
			
			var lNumerosTelefonos=getDatos();			
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}		
				
		   	sub();

		   	var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;	
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
						
			document.forms[0].importeIngresosAnuales.value=document.forms[0].importeIngresosAnuales.value.replace(/,/,".").trim();
			document.forms[0].importeBienesInmuebles.value=document.forms[0].importeBienesInmuebles.value.replace(/,/,".").trim();
			document.forms[0].importeBienesMuebles.value=document.forms[0].importeBienesMuebles.value.replace(/,/,".").trim();
			document.forms[0].importeOtrosBienes.value=document.forms[0].importeOtrosBienes.value.replace(/,/,".").trim();
			document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarEJG';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
		  	  				

			
				var envio=1;
				if (isNaN(document.forms[0].importeIngresosAnuales.value)||
						document.forms[0].importeIngresosAnuales.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.IngresosAnuales'/>");
					envio=-1;
					fin();
					return false;
				}
				if (isNaN(document.forms[0].importeBienesInmuebles.value)||
						document.forms[0].importeBienesInmuebles.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.BienesInmuebles'/>");
					envio=-1;
					fin();
					return false;
				}
					if (isNaN(document.forms[0].importeBienesMuebles.value)||
							document.forms[0].importeBienesMuebles.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.BienesMuebles'/>");
					envio=-1;
					fin();
					return false;
				}
				if (isNaN(document.forms[0].importeOtrosBienes.value)||
						document.forms[0].importeOtrosBienes.value>99999999.99){
					alert("<siga:Idioma key='gratuita.personaJG.messages.OtrosBienes'/>");
					envio=-1;
					fin();
					return false;
				}
												
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
			  
				alert(msg1);
				fin();
				return false;
			} else if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
			  
				alert(msg2);
				fin();
				return false;
			} else if (validatePersonaJGForm(document.forms[0])){
			
				// jbd: comprobaciones adicionales para el pcajg
				if(<%=pcajgActivo > 0%>){
					var error = "";
					if (<%=obligatorioTipoIdentificador%>){
						if( document.forms[0].tipoId.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>"+ '\n';
						if ((document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>" || 
							document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&
							document.forms[0].NIdentificacion.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nIdentificacion'/>"+ '\n';
					}

					if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1  && document.PersonaJGForm.existeDomicilio.value!= "N")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
					if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
					if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
					if (<%=obligatorioNacionalidad%> && document.forms[0].nacionalidad.value =="")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nacionalidad'/>"+ '\n';						
					if (<%=obligatorioEstadoCivil%> && document.forms[0].estadoCivil.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.estadoCivil'/>"+ '\n';								
					if (<%=obligatorioRegimenConyuge%> && document.forms[0].regimenConyugal.value=="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.regimenConyugal'/>"+ '\n';
					if (<%=obligatorioParentesco%> && document.forms[0].parentesco.value=="")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.parentescoNormalizado'/>"+ '\n';						
					if (<%=obligatorioTipoIngreso%> && document.forms[0].tipoIngreso.value =="")
						error += "<siga:Idioma key='errors.required' arg0='gratuita.operarInteresado.literal.tipoIngresos'/>"+ '\n';
					if (<%=obligatorioIngreso%> && document.forms[0].importeIngresosAnuales.value =="")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.operarInteresado.literal.ingresos'/>"+ '\n';
					if (<%=obligatorioFechaNac%> && document.forms[0].fechaNac.value=="" && document.forms[0].parentesco.value!="<%=ClsConstants.TIPO_CONYUGE%>")
							error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.fechaNac'/>"+ '\n';
					if(error!=""){
						alert(error);
						fin();
						return false;
					}
				}
			   
				//angelcorral: enviamos el formulario si es nuevo o no ha cambiado de persona o confirma la pregunta
				if (<%=idPersona == null%> || (document.PersonaJGForm.idPersonaJG.value == '<%=idPersona%>') || confirm('<siga:Idioma key="gratuita.personaJG.messages.cambioPersona"/>')) {
					document.forms[0].submit();			
				}else{
					fin();
					return false;
				}
				
			}else{
				fin();
				return false;
			}				
		}			
		//Asociada al boton Cerrar -->
		function accionCerrar() {	
			window.top.close();
		}
		
		function refrescarLocal() {
			window.location=window.location;
		}
<%} else if (conceptoE.equals(PersonaJGAction.SOJ)) {%>

		//Asociada al boton Volver -->
		function accionVolver()   
		{	
		 	document.forms[0].action="<%=app%>/JGR_ExpedientesSOJ.do";	
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		//Asociada al boton Guardar -->
		function accionGuardar()	{

	 		document.PersonaJGForm.existeDomicilio.value = "S";
			
			var lNumerosTelefonos=getDatos();	
			
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}		
			sub();
			var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;

			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
			
			document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarSOJ';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;
			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else{
					if (validatePersonaJGForm(document.forms[0]) ){
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}	
				}
			}
		}
						
		function refrescarLocal() {
			window.location=window.location;
		}
		
<%} else if (conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) {%>

		function accionVolver()
		{
<%String sAction = esFichaColegial
						? "JGR_AsistenciasLetrado.do"
						: "JGR_Asistencia.do";%>
			<%// indicamos que es boton volver
				ses.setAttribute("esVolver", "1");%>
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].action 	= "<%=sAction%>";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
		

		//Asociada al boton Guardar -->
		function accionGuardar()	{	


 			document.PersonaJGForm.existeDomicilio.value = "S";
			
			var lNumerosTelefonos=getDatos();	
					
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}		
			sub();
			var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;
			
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarAsistencia';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else {
					if (validatePersonaJGForm(document.forms[0]) ){
						document.forms[0].submit();
						
					}else{
						fin();
						return false;
					}				
				}
			}
		}
		function refrescarLocal() {
			window.location=window.location;
		}			
<%} else if (conceptoE.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {%>

		//Asociada al boton Cerrar -->
		function accionCerrar() {	
			window.top.close();
		}

		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	{	


			if (document.forms[0].existeDom!=null && document.forms[0].existeDom.checked) {
	 			document.PersonaJGForm.existeDomicilio.value = "N";
			}else {
	 			document.PersonaJGForm.existeDomicilio.value = "S";
			}
			if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
			if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
			if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
			
			var lNumerosTelefonos=getDatos();						
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}				
					
            sub();
            var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;
            
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";

		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarAsistencia';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else {
					if (validatePersonaJGForm(document.forms[0]) ){
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}				
				}
			}
		}
		
		function refrescarLocal() {
			window.location=window.location;
		}
					
<%} else if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)) {%>
		
		// Asociada al boton Cerrar -->
		function accionCerrar()   
		{	
			window.top.close();
		}

		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	{	


			if (document.forms[0].existeDom!=null && document.forms[0].existeDom.checked) {
	 			document.PersonaJGForm.existeDomicilio.value = "N";
			}else {
	 			document.PersonaJGForm.existeDomicilio.value = "S";
			}				
			if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
			if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
			if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
				
			var lNumerosTelefonos=getDatos();						
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}				
						
            sub();
            var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;
			
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
			
		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarContrariosEjg';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else {
					if (validatePersonaJGForm(document.forms[0]) ){
						if(<%=pcajgActivo == 4%>){
							var error = "";
							if (<%=obligatorioNacionalidad%> && document.forms[0].nacionalidad.value =="")
								error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nacionalidad'/>"+ '\n';	

							if(error!=""){
								alert(error);
								fin();
								return false;
							}
						}
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}				
				}
			}
		}
		
		function refrescarLocal() {
			window.location=window.location;
		}
					
<%} else if (conceptoE.equals(PersonaJGAction.PERSONAJG)) {%>

		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	{	

 			document.PersonaJGForm.existeDomicilio.value = "S";
			
			var lNumerosTelefonos=getDatos();					
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}				

			
			sub();
			var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;		
					
						
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";
			
		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarPersona';
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else{
					if (validatePersonaJGForm(document.forms[0])){
						// jbd: comprobaciones adicionales para el pcajg
						if(<%=pcajgActivo > 0%>){
							var error = "";
							if (<%=obligatorioTipoIdentificador%>){
								if( document.forms[0].tipoId.value=="")
									error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.tipoIdentificacion'/>"+ '\n';
								if ((document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>" || 
									document.forms[0].tipoId.value=="<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&
									document.forms[0].NIdentificacion.value=="")
									error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nIdentificacion'/>"+ '\n';
							}
							if(error!=""){
								alert(error);
								fin();
								return false;
							}
						}
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}		
				}	
			}	
		}			

		//Asociada al boton Cerrar -->
		function accionCerrar()   
		{	
			window.top.close();
		}

		function refrescarLocal() {
			window.location=window.location;
		}

<%} else if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {%>
		//Asociada al boton Cerrar -->
		function accionCerrar()   
		{	
			window.top.close();
		}

		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	{	

			if (document.forms[0].existeDom!=null && document.forms[0].existeDom.checked) {
	 			document.PersonaJGForm.existeDomicilio.value = "N";
			}else {
	 			document.PersonaJGForm.existeDomicilio.value = "S";
			}
			
			var lNumerosTelefonos=getDatos();					
			if (!lNumerosTelefonos){
                 fin();
                 return false;
			}				

			
			sub();
			var tipoIdent=document.forms[0].tipoId.value;
			var numId=document.forms[0].NIdentificacion.value;		
					
						
			if (!validaNumeroIdentificacion()) {
				fin();
				return false;
			}
			if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";

			var error = "";
			if (document.getElementById('calidad2')){				 
				document.forms[0].idTipoenCalidad.value	=	document.getElementById("calidad2").value;
				var calidad=document.forms[0].idTipoenCalidad.value;   
				if (calidad==""){			
					error+="<siga:Idioma key='gratuita.personaJG.literal.mensajecalidad'/>";				
				}				
			}			
			if (<%=obligatorioEstadoCivil%> && document.forms[0].estadoCivil.value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.estadoCivil'/>"+ '\n';
			if (<%=obligatorioRegimenConyuge%> && document.forms[0].regimenConyugal.value=="")
			    error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.regimenConyugal'/>"+ '\n';
			if (<%=obligatorioNacionalidad%> && document.forms[0].nacionalidad.value =="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.nacionalidad'/>"+ '\n';	
			if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
			if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
			if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value==""  && document.PersonaJGForm.existeDomicilio.value!= "N")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
			    
			if(error!=""){
			  alert(error);
			  fin();
			  return false;
			 }	
			
		 	document.forms[0].action="<%=app + actionE%>";	
			document.forms[0].modo.value='guardarDesigna';
			
			document.forms[0].target="submitArea2";
			//var tipo = document.forms[0].tipo.value;
			var tipo = document.forms[0].idTipoPersona.value;			
			var tipoId = document.forms[0].tipoId.value;
			var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
			var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
			
			if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
				alert(msg1);
				fin();
				return false;
			} else{
				if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
					alert(msg2);
					fin();
					return false;
				} else {
					if (validatePersonaJGForm(document.forms[0]) ){						
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}
				}
			}

		
			
		}
<%} else if(conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)){ %>
//Asociada al boton Cerrar -->
function accionCerrar()   
{	
	window.top.close();
}

//Asociada al boton Guardar -->
function accionGuardarCerrar()	{	

	document.PersonaJGForm.existeDomicilio.value = "S";
	
	var lNumerosTelefonos=getDatos();					
	if (!lNumerosTelefonos){
         fin();
         return false;
	}				

	
	sub();
	var tipoIdent=document.forms[0].tipoId.value;
	var numId=document.forms[0].NIdentificacion.value;		
			
				
	if (!validaNumeroIdentificacion()) {
		fin();
		return false;
	}
	if(document.forms[0].NIdentificacion.value=="") document.forms[0].tipoId.value = "";

	var error = "";
	if (document.getElementById('calidad2')){				 
		document.forms[0].idTipoenCalidad.value	=	document.getElementById("calidad2").value;
		var calidad=document.forms[0].idTipoenCalidad.value;   
		if (calidad==""){			
			error+="<siga:Idioma key='gratuita.personaJG.literal.mensajecalidad'/>";				
		}				
	}
	if (<%=obligatorioDireccion%> && document.forms[0].direccion.value.length<1)
		error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.direccion'/>"+ '\n';
	if (<%=obligatorioCodigoPostal%> && document.forms[0].cp.value=="")
		error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.cp'/>"+ '\n';
	if (<%=obligatorioPoblacion%> && document.forms[0].poblacion.value=="")
		error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.poblacion'/>"+ '\n';
				
	if (<%=obligatorioEstadoCivil%> && document.forms[0].estadoCivil.value=="")
		error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.estadoCivil'/>"+ '\n';
	if (<%=obligatorioRegimenConyuge%> && document.forms[0].regimenConyugal.value=="")
	    error += "<siga:Idioma key='errors.required' arg0='gratuita.personaJG.literal.regimenConyugal'/>"+ '\n';
	if(<%=pcajgActivo == 4%>){
		if (<%=obligatorioSexo%> && document.forms[0].sexo.value=="0")
			error += "<siga:Idioma key='errors.required' arg0='Sexo'/>"+ '\n';								
	}	    
	    
	if(error!=""){
	  alert(error);
	  fin();
	  return false;
	 }	
	
 	document.forms[0].action="<%=app + actionE%>";	
	document.forms[0].modo.value='guardarDesigna';
	
	document.forms[0].target="submitArea2";
	//var tipo = document.forms[0].tipo.value;
	var tipo = document.forms[0].idTipoPersona.value;			
	var tipoId = document.forms[0].tipoId.value;
	var msg1="<siga:Idioma key="gratuita.personaJG.messages.alertTipo1"/>";
	var msg2="<siga:Idioma key="gratuita.personaJG.messages.alertTipo2"/>";
	
	if (tipo=="F" && (tipoId!="" && tipoId!="50" && tipoId!="10" && tipoId!="30" && tipoId!="40")) {
		alert(msg1);
		fin();
		return false;
	} else{
		if (tipo=="J" && (tipoId!="" && tipoId!="20" && tipoId!="50")) {
			alert(msg2);
			fin();
			return false;
		} else {
			if (validatePersonaJGForm(document.forms[0]) ){						
				document.forms[0].submit();
			}else{
				fin();
				return false;
			}
		}
	}


	
}
<%}%>


function buscar() {		
	var resultado = ventaModalGeneral("BusquedaPersonaJGForm","G");			
	if (resultado != null && resultado[1]!=null) {
		traspasoDatos(resultado,resultado[17]);
	}
}
//función para obtener los valores de los telefonos para una persona
function getDatos() {
	table = resultado.document.getElementById("tablaTelefono");
	filas = table.rows.length;
	// Datos Lista de Telefonos.	
	
	var datos = "";
	var accion = "";

	 if(filas!=0){  
			
		for (a = 0; a < filas ; a++) {

			i = table.rows[a].id.split("_")[1];

			var validado = validarDatosFila (i);            
			if (!validado) {			
				fin();				
				return false;
			} 
			
			nombreTelefonoJG = resultado.document.getElementById("nombreTelefonoJG_" + i).value;
			if(nombreTelefonoJG=='-1')
				nombreTelefonoJG ="";				
			datos += 'nombreTelefonoJG='+nombreTelefonoJG;
			datos += '$$~';
			
			numeroTelefonoJG = resultado.document.getElementById("numeroTelefonoJG_" + i).value;
			if(numeroTelefonoJG=='-1')
				numeroTelefonoJG ="";			
			datos += 'numeroTelefonoJG='+numeroTelefonoJG;
			datos += '$$~';
			
			preferenteSms= resultado.document.getElementById("preferenteSms_" + i).value;
			if(preferenteSms=='-1')
				preferenteSms ="";		
			datos += 'preferenteSms='+preferenteSms;		
			datos += "%%%";
			
					
		}
	 }else return true;
	document.PersonaJGForm.lNumerosTelefonos.value = datos;	 	
	
	return datos;
}



function validarDatosFila (fila)  
{
	var campo = "";
	var obligatorio = "<siga:Idioma key='messages.campoObligatorio.error'/>";
	
	   
	    if (resultado.document.getElementById("numeroTelefonoJG_"+fila).value=='-1' || resultado.document.getElementById("numeroTelefonoJG_"+fila).value=='') {
			campo = "<siga:Idioma key='gratuita.personaJG.literal.numeroTelefono'/>" ;
			alert ( campo + " "+ obligatorio);			
			return false;
		}    
	    valor=validartelefono(resultado.document.getElementById("numeroTelefonoJG_"+fila).value);	 
	    if(!valor){
	    	campo = "<siga:Idioma key='gratuita.personaJG.literal.errors.telefono'/>" ;
			alert (campo);			
		 return false;
		}
	 
		  
 return true;	    
}

function validartelefono(valor){
	if((/^\+\d+$/.test(valor)))					
		return true;
	if((/^\d+$/.test(valor)))					
		return true;	
	//otherwise
	return false;
}

function valorprimerdigito(valor){	
	var premerdigito = valor.substring(0, 1);	
	if(premerdigito!="6")			
		return false;
	else return true;		
}

function comprobarmovil(valor){	

	var contar=valor.length;	
 	if(contar=="9"){	
 		digito=valorprimerdigito(valor);
 		if(!digito){
 			return false;
 	 	}else return true;
 		 		 
 	 }else if (contar==12){
	    	cadena = valor.substring(3);
	    	digito=valorprimerdigito(cadena);
	 		if(!digito){
	 			return false;
	 	 	}else return true;	    	
	     }else if(contar==13){
		    	cadena = valor.substring(4);
		    	digito=valorprimerdigito(cadena);
		 		if(!digito){
		 			return false;
		 	 	}else return true;
		       }			
}



//Asociada al boton Restablecer -->
function accionRestablecer() 
{		
	document.forms[0].reset();
	// inc7269 // Refrescamos la pagina para que cargue tambien los telefonos
	window.location.reload();
}
		
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->


<%if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)) {
	%> 
	<!-- formulario para seleccionar representante Legal, del censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	<!-- FIN formulario para seleccionar representante Legal, del censo -->
<%}
	
	if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {%> 
	<!-- formulario para seleccionar representante Legal, del censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	<!-- FIN formulario para seleccionar representante Legal, del censo -->
<%
	} else {
%>
	<!-- formulario para seleccionar representante -->
	<form id="representanteTutor" name="representanteTutor" action="<%=app + actionE%>" method="post">
		<input type="hidden" id="actionModal"      name="actionModal" value="">
		<input type="hidden" id="modo"      name="modo" value="abrirPestana">
		<input type="hidden" id="idInstitucionJG"     name="idInstitucionJG" value="<%=usr.getLocation()%>">
		<input type="hidden" id="idPersonaJG"      name="idPersonaJG" value="<%=miform.getIdRepresentanteJG()%>">
		<input type="hidden" id="idInstitucionPER"     name="idInstitucionPER" value="<%=usr.getLocation()%>">
		<input type="hidden" id="idPersonaPER"     name="idPersonaPER" value="<%=miform.getIdPersonaJG()%>">
		<input type="hidden" id="conceptoE"      name="conceptoE" value="<%=PersonaJGAction.PERSONAJG%>">
		<input type="hidden" id="tituloE"      name="tituloE" value="gratuita.personaJG.literal.representante">
		<input type="hidden" id="localizacionE"     name="localizacionE" value="">
		<input type="hidden" id="accionE"      name="accionE" value="editar">
		<input type="hidden" id="actionE"      name="actionE" value="<%=actionE%>">
		<input type="hidden" id="pantallaE"      name="pantallaE" value="M">
		<input type="hidden" id="repPCAJG"      name="repPCAJG" value="<%=pcajgActivo%>">
	</form>
	<!-- FIN formulario para seleccionar representante -->
<%
	}
%> 


	<!-- formulario para buscar personaJG-->
	<html:form action="/JGR_BusquedaPersonaJG.do" method="POST" target="submitArea" styleId="BusquedaPersonaJGForm">
		<input type="hidden" id="actionModal" name="actionModal" value="">
		<input type="hidden" id="modo" name="modo" value="abrir">
		<input type="hidden" id="conceptoE" name="conceptoE" value="<%=conceptoE%>">
	</html:form>
	<!-- FIN formulario para buscar personaJG-->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp"  style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
