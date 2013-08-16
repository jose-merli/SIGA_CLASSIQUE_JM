<!DOCTYPE html>
<html>
<head>

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
<%@ page import="com.siga.tlds.*"%>
<%@ page import="org.redabogacia.sigaservices.app.util.*"%>
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
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) accion="ver";

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
	if(bPestana.equals("true")&&conceptoE!=null&&conceptoE.equals("EJG"))
		bPestana = "2";
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
		//Para el solicitante: campos sexo, estado civil y todos los datos de direcci�n (sin opci�n de marcar sin domicilio).
	    //Para la unidad familiar: obligatorio el parentesco.
	    //Para unidad familiar y contrarios:

	       // Si se marca el check sin domicilio no es obligatorio ning�n campo.
	       // Si se desmarca el check son obligatorios todos los campos de direcci�n.

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

<!-- HEAD -->

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="PersonaJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
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
					//tmp2.onchange();
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
				desabilitarDomicilio (document.forms[0].existeDom);
				document.getElementById("desaparece").style.display="none";
				document.getElementById("desaparecePr").style.display="none";
				document.getElementById("desapareceCp").style.display="none";
				document.getElementById("desaparecePo").style.display="none";
			<%}else{ %>
			document.forms[0].existeDom.checked=false;
			desabilitarDomicilio (document.forms[0].existeDom);
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
 			document.PersonaJGForm.numeroDir.value = "";
 			document.PersonaJGForm.escaleraDir.value = "";
 			document.PersonaJGForm.pisoDir.value = "";
 			document.PersonaJGForm.puertaDir.value = "";
			 			
			jQuery("#provincia").val("");
			jQuery("#provincia").change();
			
			jQuery("#direccion").attr("disabled","disabled");
			jQuery("#cp").attr("disabled","disabled");
			jQuery("#bisResolucion").removeAttr("disabled");
			jQuery("#sociedadesCliente").attr("disabled","disabled");

			jQuery("#provincia").attr("disabled","disabled");
			jQuery("#poblacion").attr("disabled","disabled");
			jQuery("#tipoVia").val("");
			jQuery("#tipoVia").attr("disabled","disabled");
			
 			document.PersonaJGForm.numeroDir.disabled = "disabled";
 			document.PersonaJGForm.escaleraDir.disabled = "disabled";
 			document.PersonaJGForm.pisoDir.disabled = "disabled";
 			document.PersonaJGForm.puertaDir.disabled = "disabled";		
 			document.PersonaJGForm.direccion.disabled = "disabled";
 			document.PersonaJGForm.cp.disabled = "disabled";	 			
			
		}else {
			document.getElementById("desaparece").style.display="inline";
			document.getElementById("desaparecePr").style.display="inline";
			document.getElementById("desapareceCp").style.display="inline";
			document.getElementById("desaparecePo").style.display="inline";
			jQuery("#provincia").removeAttr("disabled");
			jQuery("#poblacion").removeAttr("disabled");
			jQuery("#tipoVia").removeAttr("disabled");
 			document.forms[0].existeDom.checked = false;
 			document.PersonaJGForm.direccion.disabled = "";	
 			document.PersonaJGForm.cp.disabled = "";
 			document.PersonaJGForm.provincia.disabled = "";	
 			document.PersonaJGForm.puertaDir.disabled = "";	
 			document.PersonaJGForm.numeroDir.disabled = "";
 			document.PersonaJGForm.escaleraDir.disabled = "";
 			document.PersonaJGForm.pisoDir.disabled = "";
 			document.PersonaJGForm.puertaDir.disabled = "";	
 			document.PersonaJGForm.direccion.disabled = "";
 			document.PersonaJGForm.cp.disabled = "";	
		}
	}

	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="<%=titulo %>" 
		localizacion="<%=localizacion %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<script type="text/javascript">
	
			function validacionFecha (dia, mes, anio) {
 				if (mes<1 || mes>12 || day<1 || dia>31 || (
 						(mes==4 || mes==6 || mes==9 || mes==11) && dia>30) ||
	   					(mes==2 && (
	   						dia>29 || (!((anio%4==0 && anio%100!=0)||anio%400==0) && dia>28)
	   					)
	   				)
	   			)
   					return (false);
 				
 				return true;
			}
	
			function comprobarFecha(sFecha){
				var sFechaNac = sFecha;	
			
				<%if (!obligatorioFechaNac) {%>
					if (sFechaNac=="")
						return true;
				<%}%>
			
				if ((sFechaNac=="")||(!validarFecha2(sFechaNac)))
					return false;
								
				sFechaNac=sFechaNac.replace('.','/');
				sFechaNac=sFechaNac.replace('-','/');										
				var diaFechaNac = parseInt(sFechaNac.substring(0, sFechaNac.indexOf('/')),10);
				var auxFecha = sFechaNac.substring(sFechaNac.indexOf('/')+1);					
				var mesFechaNac = parseInt(auxFecha.substring(0, auxFecha.indexOf('/')),10);
				var anioFechaNac = parseInt(auxFecha.substring(auxFecha.indexOf('/')+1),10);
				
				if (anioFechaNac<1000)
					anioFechaNac=anioFechaNac+2000;
				
				if (!validacionFecha(diaFechaNac, mesFechaNac, anioFechaNac))
					return false;					
				
				var dFechaActual = new Date();
				var diaFechaActual = dFechaActual.getDate();
				var mesFechaActual = dFechaActual.getMonth()+1;
				var anioFechaActual = dFechaActual.getFullYear();
				
				if (anioFechaNac>anioFechaActual) {
					return false;
				}
				else {
					if ((anioFechaNac==anioFechaActual)&&(mesFechaNac>mesFechaActual)) {
						return false;
					}
					else {
						if ((anioFechaNac==anioFechaActual)&&(mesFechaNac==mesFechaActual)&&(diaFechaNac>diaFechaActual)) {
							return false;
						}
					}
				}
				return true;
			}							
	
			function proFechaNac(){
				var sFechaNac = document.forms[0].fechaNac.value;
				
				if (sFechaNac==undefined)
					sFechaNac = document.getElementById("fechaNac").value;										
				
				if(!validarFecha2(sFechaNac))
					return false;
			
				if (sFechaNac!="") {										
					sFechaNac=sFechaNac.replace('.','/');
					sFechaNac=sFechaNac.replace('-','/');										
					var diaFechaNac = parseInt(sFechaNac.substring(0, sFechaNac.indexOf('/')),10);
					var auxFecha = sFechaNac.substring(sFechaNac.indexOf('/')+1);					
					var mesFechaNac = parseInt(auxFecha.substring(0, auxFecha.indexOf('/')),10);
					var anioFechaNac = parseInt(auxFecha.substring(auxFecha.indexOf('/')+1),10);
					
					if (anioFechaNac<1000)
						anioFechaNac=anioFechaNac+2000;
						
					if (!validacionFecha(diaFechaNac, mesFechaNac, anioFechaNac))
						return false;
				
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
						<%if (readonly) {%>									
							document.forms[0].edad.className="boxConsulta";
							document.forms[0].edad.readOnly=true;							
						<%} else {%>
							document.forms[0].edad.className="box";
							document.forms[0].edad.readOnly=false;
						<%}%>		
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
				  if (bNuevo=="1"){// s�lo cargamos los datos de la persona si esta ya estaba dada de alta en personaJG					  
					if (resultado[1]!="null" && trim(resultado[1])!="") {						
						jQuery("#tipoId").val(resultado[22]);
						jQuery("#tipoId").change();
						//recuperamos el valor del tipoIdentificacion.											
						var funcionRetardo = 'retarda('+resultado[0]+')';
						window.setTimeout(funcionRetardo,150,"Javascript");						 															
					}else
					if(resultado[0]!=null && resultado[2]!=null  && trim(resultado[0])!=""  && trim(resultado[2])!=""){						
						jQuery("#tipoId").val(resultado[0]);
						jQuery("#tipoId").change();
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
				jQuery("#nacionalidad").val(resultado[8]);
				jQuery("#nacionalidad").change();

				//Provincia:
				jQuery("#provincia").val(resultado[9]);
				jQuery("#provincia").change();				

				//Profesion:
				if(document.forms[0].profesion){
					document.forms[0].profesion.value = resultado[14];
					jQuery("#profesion").change();
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
				
				//DATOS DE DOMICILIO
				document.forms[0].numeroDir.value=resultado[26];
				document.forms[0].escaleraDir.value=resultado[27];
				document.forms[0].pisoDir.value=resultado[28];
				document.forms[0].puertaDir.value=resultado[29];
				jQuery("#tipoVia").val(resultado[30]);

				//existeDomicilio
				document.forms[0].existeDomicilio.value = resultado[25];
				<%if(pcajgActivo == 4){ 
					if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
							|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)){
					%> 
					if(resultado[25]!=null && resultado[25]=="N"){
						document.forms[0].existeDom.checked=true;
						desabilitarDomicilio (document.forms[0].existeDom);
						document.getElementById("desaparece").style.display="inline";
						document.getElementById("desaparecePr").style.display="inline";
						document.getElementById("desapareceCp").style.display="inline";
						document.getElementById("desaparecePo").style.display="inline";
					}else{
						document.forms[0].existeDom.checked=false;
						desabilitarDomicilio (document.forms[0].existeDom);
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
				jQuery("#poblacion").data("set-id-value", resultado[10]);
				jQuery("#provincia").change();
					/*
				poblacionSeleccionada = resultado[10];
				//alert("="+poblacionSeleccionada);
				//A los 1000 milisegundos (tiempo para recargar el combo provincias) se selecciona la poblacion:
				window.setTimeout("recargarComboHijo()",1000,"Javascript");
				*/

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
			jQuery("#poblacion").val(poblacionSeleccionada);
			/*
			var acceso = poblacionFrame.document.getElementsByTagName("select");
			acceso[0].value = poblacionSeleccionada;
			document.forms[0].poblacion.value = poblacionSeleccionada;
			*/
		}

		// Comprueba el tipo de persona que se elegi en el combo FISICA O JURIDICA 
		function comprobarTipoPersona ()
		{
			//Falla al dar el boton ver, yo pondr�a esto 			
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
			alert("ver identificaci�n");
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
			//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y �ste se encarga de todo :)
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
			var nif = document.forms[0].NIdentificacion.value;
			
			while (nif.indexOf(" ") != -1) {
				nif = nif.replace(" ","");
			}

			while (nif.length<9){
				nif="0"+nif;
			}						

			if (new RegExp('^[0-9]{8}[A-Za-z]$').test(nif)) {
				var letraNif = nif.substring(8,9).toUpperCase();								
				
				var numero = nif.substring(0,8);
				var posicion = numero % 23;
				var letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letraNif) {
					errorNIF=true;
				} else {
					document.forms[0].NIdentificacion.value = nif;
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
 			  idprovActual = jQuery("#provincia").val(); 
 			  if(idprovActual!=idProvincia){
	 			  jQuery("#provincia").val(idProvincia);  				  
	 			  rellenarCampos();
 			  }

  	}
		function rellenarCampos(){
			jQuery("#provincia").change();
	} 
			
		var poblacionSeleccionada;
		function recargarComboHijo() {
			jQuery("#poblacion").val(poblacionSeleccionada);
			/*
			var acceso = poblacionFrame.document.getElementsByTagName("select");
			acceso[0].value = poblacionSeleccionada;
			document.PersonaJGForm.poblacion.value = poblacionSeleccionada;
			*/
		}

		function postAccionBusquedaNIF(){
			jQuery("#poblacion").data("set-id-value", document.PersonaJGForm.poblacion.value);
			jQuery("#provincia").change();
		}

		function preAccionExisteNIF(){
			if(document.PersonaJGForm.NIdentificacion.value == null || document.PersonaJGForm.NIdentificacion.value == ''){
				return 'cancel';
			}
		}

		function postAccionExisteNIF(){
			if(document.PersonaJGForm.existeNIF.value == '1'){
			    var type =  'Ya existe una persona con la Identificaci�n introducida - ' + document.PersonaJGForm.NIdentificacion.value + '. �Desea obtener los datos del otro registro existente y sobreescribir los actuales (recomendado)?';
				if (confirm(type)){
					//document.PersonaJGForm.forzarAjax.onchange();   -> Busqueda por ajax
					rellenarFormulario();// -> B�squeda antigua
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
	// BOTONES PARA PESTA�AS
	if (pantalla.equals("P")) {
%>
		<body class="tablaCentralCampos">
			<script type="text/javascript">
				jQuery(document).ready(function(){
					recargar();
					comprobarTipoPersona();
					comprobarTipoIdent();
				});
			</script>
			<!-- capa principal -->
			<div id="camposRegistro"  align="center">

<%
	} else {
		// BOTONES PARA MODAL
%>
	<body class="tablaCentralCampos">
		<script type="text/javascript">
			jQuery(document).ready(function(){
				ajusteAlto('resultado');
				recargar();
				comprobarTipoPersona();
				proFechaNac();
			});
		</script>

		<!-- TITULO -->
		<table class="tablaTitulo" heigth="38" cellpadding="0" cellspacing="0" border="0">
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
<table align="center" width="100%" class="tablaCentralCampos" cellpadding="0" cellspacing="0" border="0">
	<html:form action="<%=actionE%>" method="POST" target="mainPestanas" styleId="PersonaJGForm">		
		<html:hidden styleId = "modo"  property = "modo" />
		<html:hidden styleId = "nuevo" property = "nuevo" value="<%=nuevo%>" />
		<html:hidden name="PersonaJGForm" property = "accionE" styleId="accionE"/>
		<html:hidden name="PersonaJGForm" property = "pantalla" styleId = "pantalla"/>
		<html:hidden name="PersonaJGForm" property = "localizacionE" styleId = "localizacionE" />
		<html:hidden name="PersonaJGForm" property = "tituloE" styleId = "tituloE" />
		<html:hidden name="PersonaJGForm" property = "conceptoE" styleId = "conceptoE" />
		<html:hidden name="PersonaJGForm" property = "existeNIF"  styleId = "existeNIF" />
		<html:hidden name="PersonaJGForm" property = "forzarAjax" styleId = "forzarAjax"  />
		<html:hidden name="PersonaJGForm" property = "lNumerosTelefonos" styleId = "lNumerosTelefonos" />
		<html:hidden property = "idTipoenCalidad"  styleId = "idTipoenCalidad" value="<%=idcalidad%>"/>
		<html:hidden property = "calidadIdinstitucion" styleId = "calidadIdinstitucion" value="<%=calidadIdinstitucion%>"/>
		<html:hidden name="PersonaJGForm" property = "tipoDir" styleId="tipoDir"/>	
	
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
	} else if (conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)
		|| conceptoE.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
		|| conceptoE.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
%>
		<html:hidden name="PersonaJGForm" styleId = "idInstitucionASI"  property = "idInstitucionASI" />
		<html:hidden name="PersonaJGForm" styleId = "anioASI" property = "anioASI" />
		<html:hidden name="PersonaJGForm" styleId = "numeroASI"  property = "numeroASI" />
<%
	} else if (conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)
		|| conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)
		|| conceptoE.equals(PersonaJGAction.DESIGNACION_REPRESENTANTE)) {
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
				|| conceptoE.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
				|| conceptoE.equals(PersonaJGAction.ASISTENCIA_CONTRARIOS)) {
%>
				<table class="tablaTitulo" heigth="38" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td id="titulo" class="titulitosDatos">	
<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
							ScsAsistenciasAdm adm = new ScsAsistenciasAdm(usr);
							Hashtable hTitulo = adm.getTituloPantallaAsistencia(miform.getIdInstitucionASI(), miform.getAnioASI(), miform.getNumeroASI());
							if (hTitulo != null) {
								t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
								t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
								t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
								t_anio = (String) hTitulo.get(ScsAsistenciasBean.C_ANIO);
								t_numero = (String) hTitulo.get(ScsAsistenciasBean.C_NUMERO);										
							}
%>
							<%=UtilidadesString.mostrarDatoJSP(t_anio)%>
							/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
							- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
						</td>
					</tr>
				</table>
<%
			} else if (conceptoE.equals(PersonaJGAction.SOJ)) {
%>
				<table class="tablaTitulo" heigth="38" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoSOJ = "";
							ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(usr);
							Hashtable hTitulo = adm.getTituloPantallaSOJ(miform.getIdInstitucionSOJ(), miform.getAnioSOJ(), miform.getNumeroSOJ(), miform.getIdTipoSOJ());
							if (hTitulo != null) {
								t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
								t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
								t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
								t_anio = (String) hTitulo.get(ScsSOJBean.C_ANIO);
								t_numero = (String) hTitulo.get(ScsSOJBean.C_NUMSOJ);
								t_tipoSOJ = (String) hTitulo.get("TIPOSOJ");
							}
%>
							<%=UtilidadesString.mostrarDatoJSP(t_anio)%>
							/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>  
							<%=UtilidadesString.mostrarDatoJSP(t_tipoSOJ)%>
							- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
						</td>
					</tr>
				</table>
<%
			} else if (conceptoE.equals(PersonaJGAction.EJG)) {
%>
				<table class="tablaTitulo" heigth="38" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";;
							ScsEJGAdm adm = new ScsEJGAdm(usr);

							Hashtable hTitulo = adm.getTituloPantallaEJG(miform.getIdInstitucionEJG(), miform.getAnioEJG(), miform.getNumeroEJG(), miform.getIdTipoEJG());

							if (hTitulo != null) {
								t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
								t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
								t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
								t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
								t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
								t_tipoEJG = (String) hTitulo.get("TIPOEJG");
							}
%>
							<%=UtilidadesString.mostrarDatoJSP(t_anio)%>
							/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>  
							<%=UtilidadesString.mostrarDatoJSP(t_tipoEJG)%>
							- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> 
							<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
						</td>
					</tr>
				</table>
<%
			}
%>

			<!-- SUBCONJUNTO DE DATOS -->
			<siga:ConjCampos leyenda="gratuita.personaJG.literal.datosGenerales">
				<table width="100%" cellpadding="2" cellspacing="0" border="0">
					<tr>
        				<td class="labelText">
<%
							if (conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
%> 
								<siga:Idioma key="gratuita.busquedaSOJ.literal.solicitante"/>&nbsp;	
		 						<html:checkbox  name="PersonaJGForm" property="solicitante" disabled="<%=scheck%>" />
<%
							} else {
%>
								&nbsp;
<%			
							}
%>
						</td>
	
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.tipo"/>		
						</td>	
						<td>
<%
							if (accion.equalsIgnoreCase("ver")) {
								String tip = "";
								if (miform.getIdTipoPersona() != null && miform.getIdTipoPersona().equalsIgnoreCase("F")) {
									tip = UtilidadesString.getMensajeIdioma(usr, "gratuita.personaJG.literal.tipoFisica");
								} else if (miform.getIdTipoPersona() != null) {
									tip = UtilidadesString.getMensajeIdioma(usr, "gratuita.personaJG.literal.tipoJuridica");
								}
%>
								<html:text property="tipo" value="<%=tip%>" size="5" styleClass="boxConsulta" readonly="true" />
<%
							} else {
%>			
								<html:select styleId="idTipoPersona"   styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" property="idTipoPersona">				
									<bean:define id="tipos" name="PersonaJGForm" property="tipos" type="java.util.Collection" />
									<html:optionsCollection name="tipos" value="idTipo" label="descripcion" />					
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
							
							if (!accion.equalsIgnoreCase("ver")) {
								String tipoIdent = (String) request.getAttribute("identificacion");
%>
								<script type="text/javascript">
									jQuery(function(){
										jQuery("#tipoId").on("change", comprobarTipoIdent());
									});
								</script>
								<siga:Select queryId="getTiposIdentificacion" id="tipoId" selectedIds="<%=tipoIdentificacionSel%>" readOnly="<%=sreadonly%>"/>
<%
		 					} else {
%>
		   						<html:select styleId="identificadores"  name="PersonaJGForm"  styleClass="boxCombo"  readOnly="false" property="tipoId" onchange="comprobarTipoIdent();"  >
									<bean:define id="identificadores" name="PersonaJGForm" property="identificadores" type="java.util.Collection" />
									<html:optionsCollection name="identificadores" value="idTipoIdentificacion" label="descripcion" />
								</html:select>
<%
		   					}
%>
						</td>
						<td class="labelText">
							<html:text name="PersonaJGForm" property="NIdentificacion" size="10" maxlength="20" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" />
						</td>

						<td class="labelText" colspan="3">
							<div class="labelText" id="textoInformativo">
								<siga:Idioma key="gratuita.personaJG.literal.requiereNifCif"/>
							</div>
						</td>
					</tr>

					<tr>
						<td  align="left" colspan="2">
							<div class="labelText" id="perFisica">
								<siga:Idioma key="gratuita.personaJG.literal.nombre(*)"/>
							</div>		
							<div class="labelText" id="perJuridica">
								<siga:Idioma key="gratuita.personaJG.literal.nombreDenoApe1"/>
							</div>		
						</td>

						<td colspan="2">
							<html:text name="PersonaJGForm" property="nombre" maxlength="100" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" style="width:160px" />
						</td>

						<td class="labelText" colspan="1">
							<div class="labelText" id="perFisica1">
								<siga:Idioma key="gratuita.personaJG.literal.apellidos(*)"/>
							</div>
							<div class="labelText" id="perJuridica1">
								<siga:Idioma key="gratuita.personaJG.literal.abreviatura(*)"/>
							</div>										
						</td>


						<td colspan="1">
							<div class="labelText">
								<html:text name="PersonaJGForm" property="apellido1" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:150px" />
							</div>
						</td>
						
						<td>			
							<div class="labelText" id="apelli2">
								<html:text name="PersonaJGForm" property="apellido2" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:150px" />
							</div>
						</td>
				
						<td>
<%
							if (!accion.equalsIgnoreCase("ver")) {
%>
								<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.buscar"/>" name="idButton"  onclick="return buscar();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.buscar"/>">
<%
							} else {
%> 
							&nbsp;
<%
							}
%>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
	
			<siga:ConjCampos leyenda="gratuita.personaJG.literal.direccion">
				<table width="100%" cellpadding="2" cellspacing="0" border="0">
					<tr>
						<td class="labelText" width="107px">
							<siga:Idioma key="gratuita.personaJG.literal.tipovia"/>
						</td>			
<%
						ArrayList selTipoVia = new ArrayList();
						if (miform.getTipoVia() != null)
							selTipoVia.add(miform.getTipoVia());
						String paramTipoVia[] = { usr.getLocation() };
%>
			
						<td width="135px">
							<siga:Select queryId="getTiposVia" id="tipoVia" selectedIds="<%=selTipoVia%>" readOnly="<%=sreadonly%>" width="123" />	
						</td>
			
						<td class="labelText" width="82px">
							<siga:Idioma key="gratuita.personaJG.literal.direccion"/>
							
<% 
							if(opcionDireccion) { 
%>				
								<div id="desaparece">
									<%=asterisco%> 
								</div>
								
<% 
							} else if (obligatorioDireccion) { 
%>
								<%=asterisco%> 				
<% 
							} 
%>							
						</td>						
						<td width="200px">		
							<html:text name="PersonaJGForm" property="direccion" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:190px" />
						</td>
						
						<td class="labelText" width="25px">
							<siga:Idioma key="gratuita.personaJG.literal.numdir"/>
						</td>						
						<td width="35px">		
							<html:text name="PersonaJGForm" property="numeroDir" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:30px" maxlength="5" />
						</td>
						
						<td class="labelText" width="30px">
							<siga:Idioma key="gratuita.personaJG.literal.escdir"/>
						</td>						
						<td width="45px">		
							<html:text name="PersonaJGForm" property="escaleraDir"  styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:40px" maxlength="10" />
						</td>
						
						<td class="labelText" width="30px">
							<siga:Idioma key="gratuita.personaJG.literal.pisodir"/>
						</td>
						<td width="35px">		
							<html:text name="PersonaJGForm" property="pisoDir" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:30px" maxlength="5" />
						</td>
						
						<td class="labelText" width="45px">
							<siga:Idioma key="gratuita.personaJG.literal.puertadir"/>
						</td>
						<td width="35px">		
							<html:text name="PersonaJGForm" property="puertaDir" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" style="width:30px" maxlength="5" />
						</td>																							
					</tr>				

					<tr>	
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.cp"/>	
<% 
							if(opcionDireccion) {	
%>
								<div id="desapareceCp">
									<%=asterisco%> 
								</div>
			
<% 
							} else if (obligatorioCodigoPostal) {	
%>
								<%=asterisco%> 
<% 
							} 
%>												
						</td>
						<td>
							<html:text name="PersonaJGForm" property="cp" size="5" maxlength="5" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"  onChange="createProvince()" />
						</td>

						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.provincia"/>	
<% 
							if(opcionDireccion) {
%>
								<div id="desaparecePr">
									<%=asterisco%> 
								</div>
<%
							} else if (obligatorioPoblacion) {
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
		  					<siga:Select queryId="getProvincias" queryParamId="idprovincia" id="provincia" selectedIds="<%=selProvincia %>" required="true" readOnly="<%=sreadonly%>" childrenIds="poblacion" width="190"/>		 
						</td>
						
						<td colspan="8">
							<table width="100%" cellpadding="0" cellspacing="0" border="0">			
								<tr>						
									<td class="labelText">
	 									<siga:Idioma key="gratuita.personaJG.literal.poblacion"/>	
<% 
										if(opcionDireccion) {
%>
											<div id="desaparecePo">
												<%=asterisco%> 
											</div>	
<%
										} else if (obligatorioPoblacion) {
%>
											<%=asterisco%> 
<%		
 										}
%>	
									</td>
									<td>
<%
										ArrayList selPoblacion = new ArrayList();
										if (miform.getPoblacion() != null)
											selPoblacion.add(miform.getPoblacion());
		
										if (accion.equalsIgnoreCase("ver")) {
											String poblacion = (String) request.getAttribute("poblacion");
%>
				   							<html:text property="poblacion" value="<%=poblacion%>" maxlength="100" styleClass="boxConsulta" readonly="true" />
<%
				   						} else {
				   							String idPoblacionParamsJSON = "";
				   							if (miform.getProvincia() != null)
				   								idPoblacionParamsJSON = UtilidadesString.createJsonString("idprovincia", miform.getProvincia());
%>
								   			<siga:Select queryId="getPoblacionesDeProvincia" id="poblacion" parentQueryParamIds="idprovincia" params="<%=idPoblacionParamsJSON%>" selectedIds="<%=selPoblacion%>" required="true" readOnly="<%=sreadonly%>" width="260"/>
<%
				   						}
%>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
					<tr>
						<td class="labelText" colspan="6" >
							<html:hidden name="PersonaJGForm" property = "existeDomicilio" value="S" />

<%
	     					if(pcajgActivo == 4) {
	 							if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS) || conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS) 
										|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
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
				<table width="100%" align="center" cellpadding="2" cellspacing="0" border="0">
					<tr>
						<td class="labelText" width="85px">
							<siga:Idioma key="gratuita.personaJG.literal.nacionalidad"/>	
<%
							if (obligatorioNacionalidad) {
%>
								<%=asterisco%> 
<%
 							}
%>		
						</td>
						<td width="215px">
<%
							ArrayList selPais = new ArrayList();
							if (miform.getNacionalidad() != null) {
								selPais.add(miform.getNacionalidad());
							}/* else {// Aqu� se quita la obligatoriedad al campo idPais y si no se selecciona ninguno NO se pone Espa�a por defecto
																																		// RGG seleccion autom�tica de Espa�a
																																		selPais.add(ClsConstants.ID_PAIS_ESPANA);
																																	}*/
%>
							<siga:Select queryId="getPaises" id="nacionalidad" selectedIds="<%=selPais%>" readOnly="<%=sreadonly%>" width="200"/>
						</td>
						
						<td class="labelText" width="140px">
							<siga:Idioma key="gratuita.personaJG.literal.fechaNac"/>	
<%
							if (obligatorioFechaNac) {
%>
								<%=asterisco%> 
<%
 							}
%>		
						</td>
						<td width="180px">
<%
		   					String fechaNac="";
						   	Boolean bFechaNac=false;
							if ((miform.getFechaNac() != null) && (!miform.getFechaNac().equalsIgnoreCase(""))) {
								 fechaNac = miform.getFechaNac();
								 bFechaNac=true;
							}

							if (!accion.equalsIgnoreCase("ver")) {
%>
			 					<siga:Fecha  nombreCampo= "fechaNac" valorInicial="<%=fechaNac%>" postFunction="proFechaNac();" />
<%
							} else {
%>
			 					<siga:Fecha  nombreCampo= "fechaNac"  valorInicial="<%=fechaNac%>" disabled="true"/>
<%
							}
%>
						</td>
						
						<td class="labelText" width="100px">
							<siga:Idioma key="gratuita.busquedaSOJ.literal.edad"/>	
						</td>
<%
						if ((miform.getEdad() != null) && (!miform.getEdad().equalsIgnoreCase(""))) {
							edad = miform.getEdad();
						}
%>
						<td width="165px">
<%
							if ((obligatorioFechaNac)||(accion.equalsIgnoreCase("ver"))) {
%>
								<html:text name="PersonaJGForm" value ="<%=edad %>" property="edad" size="3" styleClass="boxConsulta" readOnly="true"/>
<%
							} else {
%>
						 		<html:text name="PersonaJGForm" maxlength="3" onkeypress="return soloDigitos(event)" value ="<%=edad %>" property="edad" size="3" styleClass="<%=estiloBox %>" readonly="<%=bFechaNac%>"/>
<%
							}
%>										
						</td>
					</tr>
	
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.estadoCivil"/>	
<%
							if (obligatorioEstadoCivil) {
%>
								<%=asterisco%> 
<%
							}
%>			 
						</td>
						<td>
<%
							ArrayList selEstadoCiv = new ArrayList();
							if (miform.getEstadoCivil() != null)
								selEstadoCiv.add(miform.getEstadoCivil());
%>
							<siga:Select queryId="getEstadosCiviles" id="estadoCivil" selectedIds="<%=selEstadoCiv%>" readOnly="<%=sreadonly%>" width="200"/>
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.regimenConyugal"/>	
<%
							if (obligatorioRegimenConyuge) {
%>
								<%=asterisco%> 
<%
							}
%>	
						</td>
						<td>
<%
							if (accion.equalsIgnoreCase("ver")) {
								String regimen = "";
								if (miform.getRegimenConyugal() != null) {
									if (miform.getRegimenConyugal().equalsIgnoreCase("G")) {
										regimen = UtilidadesString.getMensajeIdioma(usr,"gratuita.personaJG.regimen.literal.gananciales");
									}
									
									if (miform.getRegimenConyugal().equalsIgnoreCase("I")) {
										regimen = UtilidadesString.getMensajeIdioma(usr,"gratuita.personaJG.regimen.literal.indeterminado");
									}
									
									if (miform.getRegimenConyugal().equalsIgnoreCase("S")) {
										regimen = UtilidadesString.getMensajeIdioma(usr,"gratuita.personaJG.regimen.literal.separacion");
									}
								}						
%>
								<html:text name="PersonaJGForm" property="regimenConyugal" value="<%=regimen%>" size="18" styleClass="boxConsulta" readonly="true" />
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

<%	
						if (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
%>
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
						    	<siga:Select queryId="getTiposGrupoLaboral" id="tipoGrupoLaboral" selectedIds="<%=selTipoGrupoLaboral%>" readOnly="<%=sreadonly%>" width="150"/>	       
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
								<siga:Select queryId="getIdiomasInstitucion" id="idioma" selectedIds="<%=selIdioma%>" readOnly="<%=sreadonly%>" width="200"/>
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
								<siga:Select queryId="getProfesiones" id="profesion" selectedIds="<%=selProfe%>" readOnly="<%=sreadonly%>"/>
							</td>

     						<html:hidden name="PersonaJGForm" value ="<%=nHijos %>" property="hijos"/>
<%
						} else if (conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO) || conceptoE.equals(PersonaJGAction.SOJ)) {

							if ((miform.getHijos() != null) && (!miform.getHijos().equalsIgnoreCase(""))) {
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

						if (conceptoE.equals(PersonaJGAction.ASISTENCIA_REPRESENTANTE)
	 						|| conceptoE.equals(PersonaJGAction.DESIGNACION_REPRESENTANTE)
	 						|| conceptoE.equals(PersonaJGAction.EJG_REPRESENTANTE)
	 						|| conceptoE.equals(PersonaJGAction.SOJ_REPRESENTANTE)) {
%>
							<td class="labelText">
								<siga:Idioma key="gratuita.personaJG.literal.enCalidadDe"/>		
							</td>
							<td>
								<siga:Select queryId="getEnCalidadDe" id="enCalidadDe" readOnly="<%=sreadonly%>" width="150" />
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
							<td style="display:none">
								<html:text name="PersonaJGForm" property="enCalidadDeLibre" size="10" maxlength="20" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
						
							<td>
<%
								ArrayList selParentesco = new ArrayList();
								if (miform.getParentesco() != null)
									selParentesco.add(miform.getParentesco());
								String paramParentesco[] = {usr.getLocation()};
%>
								<siga:Select queryId="getParentescos" id="parentesco" selectedIds="<%=selParentesco%>" readOnly="<%=sreadonly%>" width="150"/>
							</td>
						
<%
						} else if (conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)) {
%>
							<td class="labelText">
								<siga:Idioma key="gratuita.personaJG.literal.calidad"/>&nbsp;			
							</td>		
							<td>
								<siga:Select queryId="getTiposCalidades" id="calidad2" selectedIds="<%=calidadSel%>" readOnly="<%=sreadonly%>" width="150"/>				
							</td>
<%
						}
%>
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.personaJG.literal.sexo"/>	
<%
								if (obligatorioSexo) {
%>
									<%=asterisco%> 
<%
								}
%>		
						</td>
						<td>
<%
							String ssexo = "";
							if (miform.getSexo() != null) {
								sexo = miform.getSexo();
							} else {
								sexo = "";
							}
							
							if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE))
								ssexo = UtilidadesString.getMensajeIdioma(usr,"censo.sexo.hombre");
							
							if (sexo.equals(ClsConstants.TIPO_SEXO_MUJER))
								ssexo = UtilidadesString.getMensajeIdioma(usr,"censo.sexo.mujer");
		
							if (!accion.equalsIgnoreCase("ver")) {
%>
			    				<html:select name="PersonaJGForm" property="sexo" styleClass="<%=estiloBox%>" value="<%=sexo%>">
									<html:option value="" >&nbsp;</html:option>
									<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
									<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
								</html:select>		
<%
							} else {
%>
								<html:hidden  name="PersonaJGForm" property="sexo" value="<%=sexo %>"/>
								<html:text name="PersonaJGForm" property="ssexo" readonly="true" size="20" styleClass="<%=estiloBox %>" value="<%=ssexo %>" />				
<%
							}
%>								
						</td>
					
		 				<td class="labelText">
							<siga:Idioma key="gratuita.busquedaSOJ.literal.minusvalia"/>	
<%
							if (obligatorioMinusvalia) {
%>
								<%=asterisco%> 
<%	
							}
%>	
						</td>
						<td>
<%
							ArrayList selMinus = new ArrayList();
							if (miform.getMinusvalia() != null) {
								selMinus.add(miform.getMinusvalia());
							} else {
								if(minusDefecto!=null)
									selMinus.add(minusDefecto);
							}	
%>
							<siga:Select queryId="getMinusvalias" id="minusvalia" selectedIds="<%=selMinus%>" required="<%=String.valueOf(obligatorioMinusvalia)%>" readOnly="<%=sreadonly%>"/>
						</td>
		
<% 
						if (conceptoE.equals(PersonaJGAction.SOJ)) { 
%>
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
								<siga:Select queryId="getIdiomasInstitucion" id="idioma" selectedIds="<%=selIdioma%>" readOnly="<%=sreadonly%>"/>
							</td>		
<% 
						} 
%>	
					</tr>
				</table>
			</siga:ConjCampos>

<%
			if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
%> 
				<!-- REPRESENTANTE LEGAL -->
				<script>
		
					function limpiarRepresentante() {
						document.PersonaJGForm.idPersonaRepresentante.value ="";
						document.PersonaJGForm.ncolegiadoRepresentante.value="";
						document.PersonaJGForm.representante.value="";					
						return false;
					}
					
					function buscarRepresentante() {
						
						document.representanteTutor.idPersonaJG.value=document.PersonaJGForm.idPersonaRepresentante.value;
						document.representanteTutor.idPersonaPER.value=document.PersonaJGForm.idPersonaJG.value;
						document.representanteTutor.idInstitucionPER.value=document.PersonaJGForm.idInstitucionJG.value;
						var resultado = ventaModalGeneral("representanteTutor","G");			
						if (resultado != null && resultado[0]!=null  && resultado[1]!=null) {
							document.PersonaJGForm.idPersonaRepresentante.value = resultado[0];
							document.PersonaJGForm.representante.value = resultado[1];	
						}		
					}
	
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
						if (resultado != null && resultado[0]!=null) {			
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
					<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.personaJG.literal.representanteLegal"/>		
							</td>
							<td>
	 							<html:hidden  name="PersonaJGForm" property="ncolegiadoRepresentante"  styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" /> 	
			 			 		<html:hidden  name="PersonaJGForm" property="idPersonaRepresentante" styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" />
			 			 		<html:text  name="PersonaJGForm" property="representante" size="70" maxlength="200" styleClass="boxConsulta" readOnly="true" />
							</td>
			
							<td width="100px">
<%
								if (!accion.equalsIgnoreCase("ver")) {
%>			
									<input type="button" class="button" id="idButton" name="Buscar" value="<siga:Idioma key="general.boton.search" />" onClick="buscarRepresentante();">
<%
								}
%>
							</td>
							<td width="100px">
<%
								if (!accion.equalsIgnoreCase("ver")) {
%>
									<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarRepresentante();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
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
								<html:hidden  name="PersonaJGForm" property="ncolegiadoContrario" styleClass="boxConsulta"  readOnly="false" size="10" maxlength="10" /> 	
			 					<html:hidden  name="PersonaJGForm" property="idPersonaContrario" styleClass="box"  readOnly="true" size="10" maxlength="10" />
			 					<html:text  name="PersonaJGForm" property="abogadoContrario" size="70" maxlength="200" styleClass="boxConsulta" readOnly="true" />
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
<%
							if (conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
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
									<siga:Select queryId="getProcuradores" id="idProcurador" selectedIds="<%=selProcu%>" readOnly="<%=sreadonly%>" width="500"/>
								</td>
<%
							}
%>
						</tr>
					</table>
				</siga:ConjCampos>
			
<%
			} else {
	 			if (!conceptoE.equals(PersonaJGAction.PERSONAJG)) {
	 				String nomRep = (String) request.getAttribute("nombreRepresentanteJG");
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
							if (resultado != null && resultado[0]!=null && resultado[1]!=null) {
								document.forms[0].idRepresentanteJG.value = resultado[0];
								document.forms[0].representante.value = resultado[1];	
								
							}		
						}
	
						function buscarEJGPersonaContrario() {				
							var resultado = ventaModalGeneral("busquedaClientesModalForm","G");	
										
							if (resultado != null && resultado[0]!=null) {			
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
	  	 				<siga:ConjCampos leyenda="gratuita.personaJG.literal.Contacto">
							<table width="100%" cellpadding="2" cellspacing="0" border="0">
								<tr>
									<td class="labelText" >
										<siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>						
									</td>										
									<td class="labelTextValor">													
										<html:text name="PersonaJGForm" property="correoElectronico" maxlength="50" style="width:310px"  styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
									</td>	
									<td rowspan=2>
										<siga:ConjCampos leyenda="gratuita.personaJG.literal.telefonos">
											<iframe align="top" 
											src="<%=app%>/JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersona%>&idInstitucion=<%=usr.getLocation()%>&esFichaColegial=<%=sEsFichaColegial%>"
											id="resultado" name="resultado" scrolling="no" frameborder="0"
											marginheight="0" marginwidth="0"
											style="width:500px; height:85px;" ></iframe>
										</siga:ConjCampos>
									</td>							
								</tr>
												
								<tr>
									<td class="labelText" align="center">
										<siga:Idioma key="censo.preferente.fax"/>								
									</td>
									<td class="labelTextValor" >
										<html:text name="PersonaJGForm" property="fax" maxlength="20" style="width:150px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />								
									</td>	
								</tr>
							</table>
						</siga:ConjCampos>   
		
<%
   					}
%>

					<siga:ConjCampos leyenda="gratuita.personaJG.literal.representantes">
						<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.personaJG.literal.representanteLegal"/>		
								</td>
								<td>
									<html:text name="PersonaJGForm" property="representante" maxlength="200" styleClass="boxConsulta"  readOnly="true" value="<%= nomRep %>" style="width:600px" />
									<html:hidden name="PersonaJGForm" property="idRepresentanteJG"></html:hidden>
								</td>
								
								<td width="100px">
<%
									if (!accion.equalsIgnoreCase("ver")) {
%>
										<input type="button" alt="<siga:Idioma key="general.boton.search"/>" name="idButton" onclick="return buscarTutor();" class="button" value="<siga:Idioma key="general.boton.search"/>">
<%
									}
%>
								</td>
								<td width="100px">
<%
									if (!accion.equalsIgnoreCase("ver")) {
%>
										<input type="button" alt="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>" name="idButton"  onclick="return limpiarTutor();" class="button" value="<siga:Idioma key="gratuita.personaJG.literal.limpiar"/>">
<%
									}
%>
								</td>
							</tr>
						
<%
							if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)) { 
%>
								<tr>
									<td class="labelText">
										<siga:Idioma key="envios.etiquetas.tipoCliente.abogado"/>	
									</td>
									<td>	
								    	<html:hidden  name="PersonaJGForm" property="ncolegiadoContrario"  /> 	
								 		<html:hidden  name="PersonaJGForm" property="idAbogadoContrarioEJG" />
								 		<html:text  name="PersonaJGForm" property="abogadoContrarioEJG" size="70" maxlength="200" styleClass="boxConsulta"  readOnly="true" />
									</td>		
									
									<td> 
<%	
										if (!accion.equalsIgnoreCase("ver")) {
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
										<siga:Select queryId="getProcuradores" id="idProcurador" selectedIds="<%=selProcu%>" readOnly="<%=sreadonly%>" width="500"/>					
									</td>							
								</tr>		
<%
							}
%>	
						</table>
					</siga:ConjCampos>
<%
				}
			}
%>
		</td>
	</tr>
</table>

<table class="tablaCentralCampos" width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="top">
<%
			if (conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO)
				|| conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
%> 
				<!-- para observaciones -->
				<siga:ConjCampos leyenda="gratuita.personaJG.literal.observaciones">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td height="156px">
								<html:textarea name="PersonaJGForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" property="observaciones" cols="60" rows="7" style="width:500px" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" />
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
				
<%
			} else if (conceptoE.equals(PersonaJGAction.EJG)
				|| conceptoE.equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {

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
					<table width="100%" cellpadding="2" cellspacing="0" border="0">
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
								<siga:Select queryId="getTiposIngreso" id="tipoIngreso" selectedIds="<%=selTipoIngreso%>" readOnly="<%=sreadonly%>"/>		
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
								<html:text name="PersonaJGForm" property="ingresosAnuales" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
							<td class="labelTextValor">
								<html:text name="PersonaJGForm" property="importeIngresosAnuales" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeIngresosAnuales)%>" />&nbsp;&euro;
							</td>
						</tr>
						
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.operarInteresado.literal.bienesInmuebles"/>
							</td>
							<td>
								<html:text name="PersonaJGForm" property="bienesInmuebles" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
							<td class="labelTextValor">
								<html:text name="PersonaJGForm" property="importeBienesInmuebles" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeBienesInmuebles)%>" />&nbsp;&euro;
							</td>
						</tr>
	
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.operarInteresado.literal.bienesMobiliarios"/>
							</td>
							<td>
								<html:text name="PersonaJGForm" property="bienesMuebles" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
							<td class="labelTextValor">
								<html:text name="PersonaJGForm" property="importeBienesMuebles" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>"  readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeBienesMuebles)%>" />&nbsp;&euro;
							</td>
						</tr>
	
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.operarInteresado.literal.otrosBienes"/>
							</td>
							<td>
								<html:text name="PersonaJGForm" property="otrosBienes" maxlength="1024" style="width:270px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
							<td class="labelTextValor">
								<html:text name="PersonaJGForm" property="importeOtrosBienes" maxlength="11" style="width:80px" styleClass="<%=estiloBoxNumber%>" readOnly="<%=readonly%>" value="<%=UtilidadesNumero.formatoCampo(importeOtrosBienes)%>" />&nbsp;&euro;
							</td>
						</tr>
						
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.operarInteresado.literal.finanzas.observaciones"/>
							</td>
							<td colspan="2">
								<html:text name="PersonaJGForm" property="unidadObservaciones" maxlength="1024" style="width:376px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
<%
			}
%>
		</td>

<%
		if (!(conceptoE.equals(PersonaJGAction.SOJ))) {
%>
			<td valign="top">
				<!-- para Telefonos --> 
				<siga:ConjCampos leyenda="gratuita.personaJG.literal.Contacto">
					<table width="100%" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td class="labelText" >
								<siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>								
							</td>	
													
							<td class="labelTextValor">													
								<html:text name="PersonaJGForm" property="correoElectronico" maxlength="50" style="width:250px"  styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />
							</td>
									
						</tr>
				
						<tr>
							<td class="labelText" >
								<siga:Idioma key="censo.preferente.fax"/>								
							</td >
							<td class="labelTextValor" >
								<html:text name="PersonaJGForm" property="fax" maxlength="20" style="width:150px" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" />								
							</td>					
						</tr>
			
						<tr>
							<td colspan="2">
								<siga:ConjCampos leyenda="gratuita.personaJG.literal.telefonos">
									<iframe src="<%=app%>/JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersona%>&idInstitucion=<%=usr.getLocation()%>&esFichaColegial=<%=sEsFichaColegial%>"
										id="resultado" name="resultado" scrolling="no" frameborder="0"
										marginheight="0" marginwidth="0"
										style="width:400px; height:90px;" ></iframe>
								</siga:ConjCampos>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td>
		
<%
		} else {
%>
		 
			<td> 
		   		<siga:ConjCampos leyenda="gratuita.personaJG.literal.estadisticaSOJ">
    				<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0">
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
								<siga:Select queryId="getTiposConoce" id="tipoConoce" selectedIds="<%=selTipoConoce%>" readOnly="<%=sreadonly%>"/>	
							</td>
							
							<td class="labelText" width="30%">
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
								tipoGrupoLaboral = miform.getTipoGrupoLaboral();
							} else {
								tipoGrupoLaboral = "";
							}
							ArrayList selTipoGrupoLaboral = new ArrayList();
							selTipoGrupoLaboral.add(tipoGrupoLaboral);
%>		
							<td>
								<siga:Select queryId="getTiposGrupoLaboral" id="tipoGrupoLaboral" selectedIds="<%=selTipoGrupoLaboral%>" readOnly="<%=sreadonly%>"/>	  
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
								// cmbNumVecesSOJ
								//select 1 as id, f_siga_getrecurso_etiqueta('gratuita.SOJ.literal.una',@idioma@) as descripcion from dual union select 2 as id , f_siga_getrecurso_etiqueta('gratuita.SOJ.literal.dos',@idioma@) as descripcion from dual union select 3 as id , f_siga_getrecurso_etiqueta('gratuita.SOJ.literal.mas',@idioma@) as descripcion from dual
								HashMap<String, List<KeyValue>> hmNumVecesSOJ = new HashMap<String, List<KeyValue>>();
								ArrayList<KeyValue> arlOptions = new ArrayList<KeyValue>();
								arlOptions.add(new KeyValue("1", UtilidadesString.getMensajeIdioma(usr, "gratuita.SOJ.literal.una")));
								arlOptions.add(new KeyValue("2", UtilidadesString.getMensajeIdioma(usr, "gratuita.SOJ.literal.dos")));
								arlOptions.add(new KeyValue("3", UtilidadesString.getMensajeIdioma(usr, "gratuita.SOJ.literal.mas")));
								hmNumVecesSOJ.put(TagSelect.DATA_JSON_OPTION_KEY,arlOptions);
								String sNumVecesSOJJSON = UtilidadesString.createJsonString(hmNumVecesSOJ);
%>		
								<siga:Select queryId="JSONDATA" id="numVecesSOJ" dataJSON="<%=sNumVecesSOJJSON%>" selectedIds="<%=selNumVecesSOJ%>"/>	
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td>
<%
		}
%>	
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

	//	if (conceptoE.equals(PersonaJGAction.EJG) || conceptoE.equals(PersonaJGAction.SOJ) || conceptoE.equals(PersonaJGAction.DESIGNACION_INTERESADO) || conceptoE.equals(PersonaJGAction.ASISTENCIA_ASISTIDO)) { 
	// BOTONES PARA PESTA�AS
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
			
			if(document.forms[0].NIdentificacion.value=="") 
				jQuery("#tipoId").val("");
			
			if(!comprobarFecha(document.getElementById('fechaNac').value)){			
				if (<%=obligatorioFechaNac%>&&document.getElementById('fechaNac').value=="")
					alert("<siga:Idioma key='fecha.error.campo.necesario'/>");
					
				else 
					alert("<siga:Idioma key='fecha.error.valida'/>");
				fin();
				return false;
			}
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
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
			
			if(document.forms[0].NIdentificacion.value=="")
				jQuery("#tipoId").val("");
				
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				if (<%=obligatorioFechaNac%>&&document.getElementById('fechaNac').value=="")
					alert("<siga:Idioma key='fecha.error.campo.necesario'/>");
					
				else 
					alert("<siga:Idioma key='fecha.error.valida'/>");		
				fin();
				return false;
			}
						
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
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
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
			if(document.forms[0].NIdentificacion.value=="") 
				jQuery("#tipoId").val("");
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}				
			
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
			
			if(document.forms[0].NIdentificacion.value=="")
				jQuery("#tipoId").val("");
				
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}
				
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
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
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
			if(document.forms[0].NIdentificacion.value=="")
				jQuery("#tipoId").val("");
			 	
			 if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}

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
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
			if(document.forms[0].NIdentificacion.value=="") 
				jQuery("#tipoId").val("");
				
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}
			
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
			
			if(document.forms[0].NIdentificacion.value=="")
				jQuery("#tipoId").val("");
				
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
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
			
			if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
				document.forms[0].tipoDir.value = 'COMUNICA';
			}			
			
			if(document.forms[0].NIdentificacion.value=="")
				jQuery("#tipoId").val("");
				
			if(!comprobarFecha(document.getElementById('fechaNac').value)){
				alert("<siga:Idioma key='fecha.error.valida'/>");				
				fin();
				return false;
			}

			var error = "";
			if (document.getElementById('calidad2')){				 
				document.forms[0].idTipoenCalidad.value	=	document.getElementById("calidad2").value;
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
	
	if(document.forms[0].NIdentificacion.value=="") 
		jQuery("#tipoId").val("");
		
	if(!comprobarFecha(document.getElementById('fechaNac').value)){
		alert("<siga:Idioma key='fecha.error.valida'/>");				
		fin();
		return false;
	}
	
	if (document.forms[0].tipoDir.value == '' || document.forms[0].tipoDir.value == null){
		document.forms[0].tipoDir.value = 'COMUNICA';
	}	

	var error = "";
	if (document.getElementById('calidad2')){				 
		document.forms[0].idTipoenCalidad.value	=	document.getElementById("calidad2").value;
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
		proFechaNac();
	}
}
//funci�n para obtener los valores de los telefonos para una persona
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


<%if (conceptoE.equals(PersonaJGAction.EJG_CONTRARIOS)||conceptoE.equals(PersonaJGAction.DESIGNACION_CONTRARIOS)) {
	%> 
	<!-- formulario para seleccionar representante Legal, del censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	<!-- FIN formulario para seleccionar representante Legal, del censo -->
<%}
	
	
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


	<!-- formulario para buscar personaJG-->
	<html:form action="/JGR_BusquedaPersonaJG.do" method="POST" target="submitArea" styleId="BusquedaPersonaJGForm">
		<input type="hidden" id="actionModal" name="actionModal" value="">
		<input type="hidden" id="modo" name="modo" value="abrir">
		<input type="hidden" id="conceptoE" name="conceptoE" value="<%=conceptoE%>">
	</html:form>
	<!-- FIN formulario para buscar personaJG-->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp"  style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
