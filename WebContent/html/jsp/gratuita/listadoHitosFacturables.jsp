<!DOCTYPE html>
<html>
<head>
<!-- listadoHitosFacturables.jsp -->

<!--
  Contiene la zona de detalle multiregistro, sin botones de acciones, 
  y sin campos de filtro o busqueda
  
  VERSIONES:
  raul.ggonzalez - 16-12-2004:
    * Modificacion de formularios para validacion por struts de campos
  adrian.ayala - 01-07-2008:
    * Cambio de radiobutton B1 y B2 a checkbox
    * Adicion de seleccion por dias de la semana en cabeceras
    * Adicion de checkboxs en cabeceras
-->

<!------------------------------------------------------------------------->
<!-------------------- CABECERA JSP Y TAGLIBS - INICIO -------------------->
<!------------------------------------------------------------------------->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gratuita.form.DefinirHitosFacturablesGuardiasForm"%>
<!---------------------------------------------------------------------->
<!-------------------- CABECERA JSP Y TAGLIBS - FIN -------------------->
<!---------------------------------------------------------------------->


<!--------------------------------------------------------------->
<!-------------------- PRE-BODY JSP - INICIO -------------------->
<!--------------------------------------------------------------->
<% 
  String app=request.getContextPath();
  HttpSession ses=request.getSession();
  
  request.getSession().getAttribute("DATABACKUPHITO");
  //eliminamos esta variable de sesion
  //se volverá a crear en caso de que se elija el boton de editar
  
  //Modo de la pestanha:
  String modopestanha = request.getSession().getAttribute("modo")==null ? "" : (String)request.getSession().getAttribute("modo");
  
  DefinirHitosFacturablesGuardiasForm form = (DefinirHitosFacturablesGuardiasForm) request.getAttribute("DefinirHitosFacturablesGuardiasForm");
  String sBuscarFacturacionSJCS = form.getBuscarFacturacionSJCS();
  if (sBuscarFacturacionSJCS!=null && !sBuscarFacturacionSJCS.equals("")) {
	  modopestanha = "VER";
  }
  
  UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
  String entrada=(String)request.getSession().getAttribute("entrada");
  String acceso=usr.getAccessType();
  String accion="/JGR_DefinirHitosFacturables.do";
  String existenHitos=(String)request.getAttribute("EXISTENHITOS");
  String checkControlado=(String)request.getAttribute("checkControlado");
  String importeControlado=(String)request.getAttribute("importeControlado");
  String minimoControlado=(String)request.getAttribute("minimoControlado");
  
  String parametroPaqueteFacturacionSJCS=(String)request.getAttribute("parametroPaqueteFacturacionSJCS");
  String estiloPaqueteFacturacionSJCS="";
  String estiloGuardiasInactivas="";
  if (parametroPaqueteFacturacionSJCS==null || !parametroPaqueteFacturacionSJCS.equalsIgnoreCase("S")) {
	  estiloPaqueteFacturacionSJCS="style='display:none'";
	  Integer iConsejo=(Integer)request.getAttribute("iConsejo");
	  if (iConsejo==null || !iConsejo.equals(3001)) {
		  estiloGuardiasInactivas="style='display:none'";
	  }
  }   
  
  Vector<Hashtable<String,Object>> vFcsHistoricoHitoFact = (Vector<Hashtable<String,Object>>) request.getAttribute("vFcsHistoricoHitoFact");
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
  
  	<!-------------------- TITULO Y LOCALIZACION - INICIO -------------------->
  	<siga:TituloExt titulo="gratuita.listadoHitosFacturables.literal.titulo" localizacion="gratuita.listadoHitosFacturables.literal.localizacion"/>
  	<!-------------------- TITULO Y LOCALIZACION - FIN -------------------->
  
  	<!-------------------- FUNCIONES SCRIPT - INICIO -------------------->
  	<script>
  		// JPT Transforma la coma en punto, comprueba que es un numero y muestra dos decimales
		function convertirAFormato(numero){
  			var numeroFormateado = numero.replace(",", ".");
			while (numeroFormateado.toString().indexOf(".", 0) > 0 && numeroFormateado.toString().length - numeroFormateado.toString().indexOf(".", 0) > 3) {
				numeroFormateado = numeroFormateado.replace(".", "");
			}  			
  			
			var numeroNumber = new Number(numeroFormateado);
			
			if (isNaN(numeroNumber)) {
				return "";
			}
			
			numeroNumber = Number(numeroNumber.toFixed(2));
			
			return numeroNumber;	
		}		  		  		
    
	    //////////////////// FUNCIONES DE BOTONES - INI ////////////////////
	    function accionGuardar () {
	    	// Requisitos necesarios para la facturación controlada
	    	var valorControlado = jQuery("input[name=checkControlado]:checked").val();
	    	if (valorControlado != "5") {	 
		    	var errores = "";
		    	var importe = "";
		    	var importeMinimo = "";
		    	
		    	if (valorControlado == "1") {	    		
		    		if (jQuery("#AsImporte").val()=="") {
		    			errores += "<siga:Idioma key='errors.required' arg0='fcs.criteriosFacturacion.asistencia.asistencias'/>"+ '\n';
		    		} else {
		    			importe = convertirAFormato(jQuery("#AsImporte").val());
		    			if (importe=="0" || importe=="0.0" || importe=="0.00") {
		    				errores += "<siga:Idioma key='errors.invalid' arg0='fcs.criteriosFacturacion.asistencia.asistencias'/>"+ '\n';
		    			}
		    		}
		    		
	    		} else if (valorControlado == "2") {	    		
		    		if (jQuery("#AsMinImporte").val()=="") {
		    			errores += "<siga:Idioma key='errors.required' arg0='fcs.criteriosFacturacion.asistencia.asistencias'/>"+ '\n';
		    		} else {
		    			importe = convertirAFormato(jQuery("#AsMinImporte").val());
		    			if (importe=="0" || importe=="0.0" || importe=="0.00") {
		    				errores += "<siga:Idioma key='errors.invalid' arg0='fcs.criteriosFacturacion.asistencia.asistencias'/>"+ '\n';
		    			}			
		    		}
		    		
		    		if (jQuery("#AsMinMinimo").val()=="") {
		    			errores += "<siga:Idioma key='errors.required' arg0='fcs.criteriosFacturacion.asistencia.minAsist'/>"+ '\n';
		    		} else {
		    			importeMinimo = convertirAFormato(jQuery("#AsMinMinimo").val());
		    			if (importeMinimo=="0" || importeMinimo=="0.0" || importeMinimo=="0.00") {
		    				errores += "<siga:Idioma key='errors.invalid' arg0='fcs.criteriosFacturacion.asistencia.minAsist'/>"+ '\n';
		    			}			
		    		}	
		    		
	    		} else if (valorControlado == "3") {
	    			importe = convertirAFormato(jQuery("#AsTpImporte").val());
	    			
		    		if (jQuery("#AsTpMinimo").val()=="") {
		    			errores += "<siga:Idioma key='errors.required' arg0='fcs.criteriosFacturacion.asistencia.minAsist'/>"+ '\n';
		    		} else {
		    			importeMinimo = convertirAFormato(jQuery("#AsTpMinimo").val());
		    			if (importeMinimo=="0" || importeMinimo=="0.0" || importeMinimo=="0.00") {
		    				errores += "<siga:Idioma key='errors.invalid' arg0='fcs.criteriosFacturacion.asistencia.minAsist'/>"+ '\n';
		    			}			
		    		}
		    		
	    		} else  if (valorControlado == "4") {	    		    		
		    		if (jQuery("#GAsImporte").val()=="") {
		    			errores += "<siga:Idioma key='errors.required' arg0='fcs.criteriosFacturacion.asistencia.diaGuardia'/>"+ '\n';
		    		} else {
		    			importe = convertirAFormato(jQuery("#GAsImporte").val());
		    			if (importe=="0" || importe=="0.0" || importe=="0.00") {
		    				errores += "<siga:Idioma key='errors.invalid' arg0='fcs.criteriosFacturacion.asistencia.diaGuardia'/>"+ '\n';
		    			}			
		    		}	    		
	    		}	    	
	
				if (errores != "") {
					alert(errores);
					fin();
					return false;					
				}
				
				if (valorControlado == "1") {		
					if (document.getElementById("checkB1").checked) {
						document.getElementById("checkB1").checked = false;
						cambiarCheckB1();
					}
					if (!document.getElementById("checkB2").checked) {
						document.getElementById("checkB2").checked = true;
						cambiarCheckB2();
					}					
					if (document.getElementById("chGuardias").checked) {
						document.getElementById("chGuardias").checked = false;
						aplicaTipos();
					}
					if (document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = false;
						controlAsMin();
					}					
					document.getElementById("hitoPrecio[1]").value = "";
					document.getElementById("hitoPrecio[2]").value = "";
					document.getElementById("hitoPrecio[5]").value = importe;
					document.getElementById("hitoPrecio[10]").value = "";
					document.getElementById("hitoPrecio[45]").value = "";
					
				} else if (valorControlado == "2") {
					if (document.getElementById("checkB1").checked) {
						document.getElementById("checkB1").checked = false;
						cambiarCheckB1();
					}
					if (!document.getElementById("checkB2").checked) {
						document.getElementById("checkB2").checked = true;
						cambiarCheckB2();
					}	
					if (document.getElementById("chGuardias").checked) {
						document.getElementById("chGuardias").checked = false;
						aplicaTipos();
					}
					if (!document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = true;
						controlAsMin();
					}					
					document.getElementById("hitoPrecio[1]").value = "";
					document.getElementById("hitoPrecio[2]").value = "";
					document.getElementById("hitoPrecio[5]").value = importe;
					document.getElementById("hitoPrecio[10]").value = importeMinimo;
					document.getElementById("hitoPrecio[45]").value = "";
					
				} else if (valorControlado == "3") {
					if (document.getElementById("checkB1").checked) {
						document.getElementById("checkB1").checked = false;
						cambiarCheckB1();
					}
					if (!document.getElementById("checkB2").checked) {
						document.getElementById("checkB2").checked = true;
						cambiarCheckB2();
					}	
					if (!document.getElementById("chGuardias").checked) {
						document.getElementById("chGuardias").checked = true;
						aplicaTipos();
					}		
					if (!document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = true;
						controlAsMin();
					}					
					document.getElementById("hitoPrecio[1]").value = "";
					document.getElementById("hitoPrecio[2]").value = "";
					document.getElementById("hitoPrecio[5]").value = importe;
					document.getElementById("hitoPrecio[10]").value = importeMinimo;
					document.getElementById("hitoPrecio[45]").value = "";
					
				} else if (valorControlado == "4") {
					if (!document.getElementById("checkB1").checked) {
						document.getElementById("checkB1").checked = true;
						cambiarCheckB1();
					}						
					if (document.getElementById("checkB2").checked) {
						document.getElementById("checkB2").checked = false;
						cambiarCheckB2();
					}	
					if (document.getElementById("chGuardias").checked) {
						document.getElementById("chGuardias").checked = false;
						aplicaTipos();
					}
					if (document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = false;
						controlAsMin();
					}
					document.getElementById("hitoPrecio[1]").value = importe;
					if (document.getElementById("hitoPrecio[2]").value != importe) {
						document.getElementById("hitoPrecio[2]").value = "0";
						document.getElementById("hitoPrecio[45]").value = "0";
					}
					document.getElementById("hitoPrecio[5]").value = "";
					document.getElementById("hitoPrecio[10]").value = "";
									
				}
	    	}				
	    		    	
	      	//activando selecciones de dias para poder leer desde JAVA
	   		compruebaPagasGuardia();
	      	activarDiasSemana ();
	      
	      	//para que solo inserte guardia doblada por actuacion o por asistencia
	      	//segun este el campo activo
	      	if (DefinirHitosFacturablesGuardiasForm.radioPG[0].checked) {
	        	document.getElementById("hitoPrecio[4]").value="0";
	        	jQuery("#hitoPrecio[4]").attr("disabled","disabled");
	      	} else {
		        document.getElementById("hitoPrecio[2]").value="0";
	    	    jQuery("#hitoPrecio[2]").attr("disabled","disabled");
	      	}
	      	
	      	with (DefinirHitosFacturablesGuardiasForm) {
	        	target = "submitArea";
	        	modo.value = "modificar";
	        	submit ();
	      	}
	    } //accionGuardar ()
	    
	    function accionVolver () {
	      	DefinirHitosFacturablesGuardiasForm.action="JGR_DefinirTurnos.do";
	      	DefinirHitosFacturablesGuardiasForm.target="mainWorkArea";
	      	DefinirHitosFacturablesGuardiasForm.modo.value="abrirAvanzada";
	      	DefinirHitosFacturablesGuardiasForm.submit();
	    } //accionVolver ()
	    
	    function refrescarLocal () {
	      	buscar ();
	    } //refrescarLocal ()
	    
	    function buscar () {
	      	DefinirHitosFacturablesGuardiasForm.target="mainPestanas";
	      	DefinirHitosFacturablesGuardiasForm.modo.value="abrir";
	      	DefinirHitosFacturablesGuardiasForm.submit();
	    } //buscar ()
	    
	    function consultarAct (importeMax) {
	      	DefinirHitosFacturablesGuardiasForm.modo.value="consultar";
	      	DefinirHitosFacturablesGuardiasForm.tipoConsulta.value="actuacion";
	      	DefinirHitosFacturablesGuardiasForm.importeMax.value=importeMax;
	      	ventaModalGeneral("DefinirHitosFacturablesGuardiasForm","P");
	    } //consultarAct ()
	    
	    function consultarAsist (importeMax) {
	      	DefinirHitosFacturablesGuardiasForm.modo.value="consultar";
	      	DefinirHitosFacturablesGuardiasForm.tipoConsulta.value="asistencia";
	      	DefinirHitosFacturablesGuardiasForm.importeMax.value=importeMax;
	      	ventaModalGeneral("DefinirHitosFacturablesGuardiasForm","P");
	    } //consultarAsist ()
	    //////////////////// FUNCIONES DE BOTONES - FIN ////////////////////
	    
	    //////////////////// FUNCIONES UTILES - INI ////////////////////
	    function habilitarCuadroTexto (elem) {
	      	elem.className="boxNumber";
	      	elem.disabled=false;
	    } //habilitarCuadroTexto ()
	    
	    function inhabilitarCuadroTexto (elem) {
	      	elem.value=0;
	      	elem.className="boxConsultaNumber";
	      	elem.disabled=true;
	    } //inhabilitarCuadroTexto ()
	    
	    function inhabilitarCuadroTextoSinCero (elem) {
	      	elem.className="boxConsultaNumber";
	      	elem.disabled=true;
	    } //inhabilitarCuadroTextoSinCero ()
	    
	    function habilitarCheck (elem) {
	      	elem.disabled=false;
	    } //habilitarCheck ()
	    
	    function inhabilitarCheck (elem) {
	      	elem.checked=false;
	      	elem.disabled=true;
	    } //inhabilitarCheck ()
	    
	    function inhabilitarCheckActivo (elem) {
	      	elem.checked=true;
	      	elem.disabled=true;
	    } //inhabilitarCheckActivo ()
	    
	    function habilitarBotonLupa (elem) {
	      	elem.src="/SIGA/html/imagenes/bconsultar_on.gif";
	      	elem.style.cursor="hand";
	      	elem.disabled=false;
	    } //habilitarBotonLupa ()
	    
	    function inhabilitarBotonLupa (elem) {
	      	elem.src="/SIGA/html/imagenes/bconsultar_disable.gif";
	      	elem.style.cursor="default";
	      	elem.disabled=true;
	    } //inhabilitarBotonLupa ()
	    //////////////////// FUNCIONES UTILES - FIN ////////////////////
    
	    //////////////////// FUNCIONES DE CHECKS GENERALES - INI ////////////////////
	    function init () {	 
	      	initAB ();	      	
	      	gestionarControlado(<%=checkControlado%>, true);
	    } //init ()
	    
	    // Funcion que calcula la altura de la pagina de un div con los botones del final
	    function calcularAltura(controlado) {		
			if (document.getElementById("idBotonesAccion")) {
				var tablaBotones = jQuery('#idBotonesAccion')[0];
				
				var divDatos;
				if (controlado)
					divDatos = jQuery('#divFacturacionControlada')[0];
				else
					divDatos = jQuery('#divFacturacionNoControlada')[0];
			
				var posTablaBotones = tablaBotones.offsetTop;
				var posDivDatos = divDatos.offsetTop;
			
				jQuery('#scrollValores').height(posTablaBotones - posDivDatos);
			}		
		}	
	    
  		function gestionarControlado(valorControlado, inicio) {
			if (valorControlado == 1) {
				habilitarCuadroTexto (document.getElementById("AsImporte"));				
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinImporte"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinMinimo"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpImporte"));
				inhabilitarBotonLupa (document.getElementById("AsTpTipos"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpMinimo"));
				inhabilitarCuadroTextoSinCero (document.getElementById("GAsImporte"));				
				if (inicio) {
					jQuery("#AsImporte").val("<%=importeControlado%>");
					jQuery("#divFacturacionNoControlada").hide();
					calcularAltura(true);
				}

			} else if (valorControlado == 2) {
				inhabilitarCuadroTextoSinCero (document.getElementById("AsImporte"));
				habilitarCuadroTexto (document.getElementById("AsMinImporte"));				
				habilitarCuadroTexto (document.getElementById("AsMinMinimo"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpImporte"));
				inhabilitarBotonLupa (document.getElementById("AsTpTipos"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpMinimo"));				
				inhabilitarCuadroTextoSinCero (document.getElementById("GAsImporte"));				
				if (inicio) {
					jQuery("#AsMinImporte").val("<%=importeControlado%>");
					jQuery("#AsMinMinimo").val("<%=minimoControlado%>");
					jQuery("#divFacturacionNoControlada").hide();
					calcularAltura(true);
				}
			
			} else if (valorControlado == 3) {
				inhabilitarCuadroTextoSinCero (document.getElementById("AsImporte"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinImporte"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinMinimo"));
				habilitarCuadroTexto (document.getElementById("AsTpImporte"));				
				habilitarBotonLupa (document.getElementById("AsTpTipos"));
				habilitarCuadroTexto (document.getElementById("AsTpMinimo"));				
				inhabilitarCuadroTextoSinCero (document.getElementById("GAsImporte"));
				if (inicio) {
					jQuery("#AsTpImporte").val("<%=importeControlado%>");
					jQuery("#AsTpMinimo").val("<%=minimoControlado%>");
					jQuery("#divFacturacionNoControlada").hide();
					calcularAltura(true);
				}				
			
			} else if (valorControlado == 4) {
				inhabilitarCuadroTextoSinCero (document.getElementById("AsImporte"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinImporte"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsMinMinimo"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpImporte"));
				inhabilitarBotonLupa (document.getElementById("AsTpTipos"));
				inhabilitarCuadroTextoSinCero (document.getElementById("AsTpMinimo"));				
				habilitarCuadroTexto (document.getElementById("GAsImporte"));							
				if (inicio) {
					jQuery("#GAsImporte").val("<%=importeControlado%>");
					jQuery("#divFacturacionNoControlada").hide();
					calcularAltura(true);
				}					
			
			} else if (valorControlado == 5) {
				jQuery("#divFacturacionControlada").hide();
				jQuery("#divFacturacionNoControlada").show();	
				calcularAltura(false);
			}
		}	  	    
    
	    /** 
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Check de paga guardia por dia
	     * Check de no paga guardia por dia
	     * 
	     * Precio por dia de guardia presencial
	     * Check de No Paga Guardia
	     * Dias que se paga por guardia
	     * Dias que no se paga por guardia
	     * 
	     * Precio de actuaciones fuera de guardia
	     * Boton de tipos para precio de actuaciones fuera de guardia
	     * 
	     * Precio por guardia simple doblada por asistencias
	     * Maximo asistencias para doblar
	     * Check de Maximo por Asistencias
	     * Check de Minimo por Asistencias
	     * Precio de asistencias
	     * Boton de tipos para precio de asistencias
	     * Precio maximo de asistencias
	     * Boton de tipos para precio maximo de asistencias
	     * Precio minimo de asistencias
	     * 
	     * Precio por guardia simple doblada por actuaciones
	     * Maximo actuaciones para doblar
	     * Check de Maximo por Actuaciones
	     * Check de Minimo por Actuaciones
	     * Precio de actuaciones
	     * Boton de tipos para precio de actuaciones
	     * Precio maximo de actuaciones
	     * Boton de tipos para precio maximo de actuaciones
	     * Precio minimo de actuaciones
	     */
	    function initAB () {
	      	initDiasSemana ();	      	      	   
      		
      		controlPagaGuardia();
      		
      		controlNoPagaGuardia();
      		
      		controlAcFg();
    	} //initAB ()
    
	    /** 
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Dias que se paga por guardia
	     * Dias que no se paga por guardia
	     */
	    function initDiasSemana () {
			if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
				
				if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
					//habilitando Dias que se paga por guardia
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
					habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
					
				} else {
					//activando solo todos los Dias que se paga por guardia
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
			        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
				}
	        
			} else {
				//deshabilitando Dias que se paga por guardia
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
				inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
			}
      
      		if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
      			
      			if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
	        		//habilitando Dias que no se paga por guardia
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
			        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
			        
      			} else {
      			//activando solo todos los Dias que no se paga por guardia
      				inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
    		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
      			}
		        
      		} else {
		        //deshabilitando Dias que no se paga por guardia
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
		        inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
      		}
    	} //initDiasSemana ()
    
	    function compruebaPagasGuardia () {
	    	//Este metodo comprueba si hay seleccionado algun dia de paga guardia 
	    	//o no paga guardia. en el caso que no haya ningun dia en cualquiera de los dos el check
	    	// de paga guardia o no paga guardia se deshabilitara
	    	with (DefinirHitosFacturablesGuardiasForm) {
	    	
		    	var hayDiaPagaGuardiaChecked = chPagaGuardiaLunes.checked ||
			    	chPagaGuardiaMartes.checked ||
			    	chPagaGuardiaMiercoles.checked ||
			    	chPagaGuardiaJueves.checked ||
			    	chPagaGuardiaViernes.checked ||
			    	chPagaGuardiaSabado.checked ||
			    	chPagaGuardiaDomingo.checked;
		    	
		    	//Si no hay ninguno seleccionado y el check esta seleccionado lo deseleccionamos
		    	if (!hayDiaPagaGuardiaChecked && checkB1.checked){
		    		checkB1.checked = false;
		    		cambiarCheckB1();
		    	}
		    	
	    		var hayDiaNoPagaGuardiaChecked = chNoPagaGuardiaLunes.checked ||
			    	chNoPagaGuardiaMartes.checked ||
			    	chNoPagaGuardiaMiercoles.checked ||
			    	chNoPagaGuardiaJueves.checked ||
			    	chNoPagaGuardiaViernes.checked ||
			    	chNoPagaGuardiaSabado.checked ||
			    	chNoPagaGuardiaDomingo.checked;
	    		
		    	//Si no hay ninguno seleccionado y el check esta seleccionado lo deseleccionamos			    	
		    	if (!hayDiaNoPagaGuardiaChecked && checkB2.checked){
		    		checkB2.checked = false;
		    		cambiarCheckB2();
		    	}			    	
	    	}
	    }
    
	    /**
	     * Activa todos los checks de los dias de la semana.
	     * Se utiliza fundamentalmente para poder leer si estan checkeados o no
	     * desde JAVA
	     */
	    function activarDiasSemana () {
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
			
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
	    } //activarDiasSemana ()
	    //////////////////// FUNCIONES DE CHECKS GENERALES - FIN ////////////////////
    
    
    	//////////////////// FUNCIONES DE CHECKS CONCRETOS - INI ////////////////////
    	function cambiarCheckB1 () {
      		if (!DefinirHitosFacturablesGuardiasForm.checkB1.checked && !DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
        		DefinirHitosFacturablesGuardiasForm.checkB2.checked=true;
        		DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia.checked=true;
      		}
      		
      		if (DefinirHitosFacturablesGuardiasForm.checkB1.checked)
        		DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia.checked=true;
      
      		initAB ();
    	} //cambiarCheckB1 ()
    	
    	function cambiarCheckB2 () {
      		if (!DefinirHitosFacturablesGuardiasForm.checkB1.checked && !DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
        		DefinirHitosFacturablesGuardiasForm.checkB1.checked=true;
        		DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia.checked=true;
      		}
      		
      		if (DefinirHitosFacturablesGuardiasForm.checkB2.checked)
        		DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia.checked=true;
      		
      		initAB ();
    	} //cambiarCheckB2 ()
    
    	function pagaGuardiaGeneral (elemPaga, elemNoPaga, elemRadio) {
      		if (elemPaga.checked)    
      			elemNoPaga.checked=false;
      		else {
        		if (elemRadio.checked) 
        			elemNoPaga.checked=true;
        		else                   
        			elemPaga.checked=true;
      		}
    	} //pagaGuardiaGeneral ()
    	
    	function pagaGuardiaLunes () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaLunes ()
    	
    	function pagaGuardiaMartes () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaMartes ()
    	
    	function pagaGuardiaMiercoles () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaMiercoles ()
    	
    	function pagaGuardiaJueves () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaJueves ()
    	
    	function pagaGuardiaViernes () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaViernes ()
    	
    	function pagaGuardiaSabado () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaSabado ()
    	
    	function pagaGuardiaDomingo () {
      		pagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo,
                DefinirHitosFacturablesGuardiasForm.checkB2);
    	} //pagaGuardiaDomingo ()
    
    	function noPagaGuardiaGeneral (elemPaga, elemNoPaga, elemRadio) {
      		if (elemNoPaga.checked)  
      			elemPaga.checked=false;
      		else {
        		if (elemRadio.checked) 
        			elemPaga.checked=true;
        		else                   
        			elemNoPaga.checked=true;
      		}
    	} //noPagaGuardiaGeneral ()
    	
    	function noPagaGuardiaLunes () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaLunes ()
    	
    	function noPagaGuardiaMartes () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaMartes ()
    	
    	function noPagaGuardiaMiercoles () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaMiercoles ()
    	
    	function noPagaGuardiaJueves () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaJueves ()
    	
    	function noPagaGuardiaViernes () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaViernes ()
    	
    	function noPagaGuardiaSabado () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaSabado ()
    	
    	function noPagaGuardiaDomingo () {
      		noPagaGuardiaGeneral (
      			DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo,
                DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo,
                DefinirHitosFacturablesGuardiasForm.checkB1);
    	} //noPagaGuardiaDomingo ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Boton de tipos para precio de asistencias
	     * Boton de tipos para precio maximo de asistencias
	     * Boton de tipos para precio de actuaciones
	     * Boton de tipos para precio maximo de actuaciones
	     * 
	     * LLAMADO POR:
	     * Check de Aplicar tipos
	     */
	    function aplicaTipos () {
	    	// chGuardias=AplicaTipos; radioNPG[0]=Asistencias
			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked && DefinirHitosFacturablesGuardiasForm.radioNPG[0].checked) {
				
	        	//Boton de tipos para precio de asistencias
	        	habilitarBotonLupa (document.getElementById("consultar_10"));
	        	
	        	//Boton de tipos para precio maximo de asistencias
	        	if (DefinirHitosFacturablesGuardiasForm.chAsist.checked)
	          		habilitarBotonLupa (document.getElementById("consultar_11"));
	        	else
	          		inhabilitarBotonLupa (document.getElementById("consultar_11"));
	        	
	      	} else {
	        	//Boton de tipos para precio de asistencias
	        	inhabilitarBotonLupa (document.getElementById("consultar_10"));
	        	
	        	//Boton de tipos para precio maximo de asistencias
	        	inhabilitarBotonLupa (document.getElementById("consultar_11"));
	      	}
	      
			// chGuardias=AplicaTipos; radioNPG[1]=Actuaciones
	      	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked && DefinirHitosFacturablesGuardiasForm.radioNPG[1].checked) {
	      		
	        	//Boton de tipos para precio de actuaciones
	        	habilitarBotonLupa (document.getElementById("consultar_20"));
	        	
	        	//Boton de tipos para precio maximo de actuaciones
	        	if (DefinirHitosFacturablesGuardiasForm.chActuacion.checked)
	          		habilitarBotonLupa (document.getElementById("consultar_21"));
	        	else
	          		inhabilitarBotonLupa (document.getElementById("consultar_21"));
	        	
	      	} else {
	        	//Boton de tipos para precio de actuaciones
	        	inhabilitarBotonLupa (document.getElementById("consultar_20"));
	        	
	        	//Boton de tipos para precio maximo de actuaciones
	        	inhabilitarBotonLupa (document.getElementById("consultar_21"));
	      	}
    	} //aplicaTipos ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Boton de tipos para precio de actuaciones fuera de guardia
	     * Boton de tipos para precio maximo de actuaciones fuera de guardia
	     * 
	     * LLAMADO POR:
	     * Check de Aplicar tipos de Fuera de Guardia
	     */
	    function aplicaTiposFG () {
	    	// chNoGuardias=AplicaTiposFG
	      	//Boton de tipos para precio de actuaciones fuera de guardia
	      	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
	        	habilitarBotonLupa (document.getElementById("consultar_30"));
	      	else
	        	inhabilitarBotonLupa (document.getElementById("consultar_30"));
	      
	     	// chNoGuardias=AplicaTiposFG; chActFG=MaxActFG
	      	//Boton de tipos para precio maximo de actuaciones fuera de guardia
	      	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked && DefinirHitosFacturablesGuardiasForm.chActFG.checked)
	        	habilitarBotonLupa (document.getElementById("consultar_31"));
	      	else
	        	inhabilitarBotonLupa (document.getElementById("consultar_31"));
    	} //aplicaTiposFG ()
	    
    	/** Datos:
		 * - Check Aplicar Max-Min por Dia (chPagaGuardiaPorDia)
    	 *
    	 * - Div Importe por guardia simple por asistencias o actuaciones (DivGAs + DivGAc + DivGAsMin + DivGAcMin)
	     * - Importe por guardia simple por asistencias o actuaciones (hitoPrecio[1])
    	 *
    	 * - Tr Importe por guardia doblada por asistencias (TrGDAs1 + TrGDAs2)
	     * - Importe por guardia doblada por asistencias (hitoPrecio[2])
	     * - Numero de asistencias para ser considerada guardia doblada (hitoPrecio[45])
    	 *
    	 * - Tr Importe por guardia doblada por actuaciones (TrGDAc1 + TrGDAc2)
	     * - Importe por guardia doblada por actuaciones (hitoPrecio[4])
	     * - Numero de actuaciones para ser considerada guardia doblada (hitoPrecio[46])
	     */
	    function controlPagaGuardia() {
	    	// Si paga guardias (checkB1)
	      	if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
	      		habilitarGAx();
	      		if (DefinirHitosFacturablesGuardiasForm.radioPG[0].checked) { // Por Asistencia
	      			habilitarGAs();
	      			inhabilitarGAc();
	      			habilitarGDAs();
	      			inhabilitarGDAc();
	      		} else { // Por Actuaciones
	      			inhabilitarGAs();
	      			habilitarGAc();
	      			inhabilitarGDAs();
	      			habilitarGDAc();
	      		} 
	      		
	      	} else {	
	      		inhabilitarGAx();
	      		inhabilitarGAs();
      			inhabilitarGAc();
	      		inhabilitarGDAs();
	      		inhabilitarGDAc();
	      	}
	    }
	    
		function habilitarGAx() {		
   			jQuery("#TabGDAx").show();
   			
   			// Aplicar Max-Min por Dia 
   			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia);
    	}
		
		function inhabilitarGAx() {		
   			jQuery("#TabGDAx").hide();
   			
   			// Aplicar Max-Min por Dia 
   			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia);
    	}	
		
		function habilitarGAs() {		
			// Importe por guardia simple As
 			habilitarCuadroTexto (document.getElementById("hitoPrecio[1]"));
 			jQuery("#DivGAs").show();
   		
 			// Importe por guardia inactiva As			
			habilitarCuadroTexto (document.getElementById("hitoPrecio[53]"));
			jQuery("#DivGAsMin").show();
    	}
		
		function habilitarGAc() {		
			// Importe por guardia simple Ac
 			habilitarCuadroTexto (document.getElementById("hitoPrecio[44]"));
 			jQuery("#DivGAc").show();
   		
 			// Importe por guardia inactiva Ac			
			habilitarCuadroTexto (document.getElementById("hitoPrecio[54]"));
			jQuery("#DivGAcMin").show();
    	}
		
		function inhabilitarGAs() {		
			// Importe por guardia simple As
 			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[1]"));
 			jQuery("#DivGAs").hide();
   		
 			// Importe por guardia inactiva As			
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[53]"));
			jQuery("#DivGAsMin").hide();
    	}
		
		function inhabilitarGAc() {		
			// Importe por guardia simple Ac
 			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[44]"));
 			jQuery("#DivGAc").hide();
   		
 			// Importe por guardia inactiva Ac			
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[54]"));
			jQuery("#DivGAcMin").hide();
    	}		
    	
		function habilitarGDAs() {		
			// Importe por guardia doblada por asistencias
   			habilitarCuadroTexto (document.getElementById("hitoPrecio[2]"));
			
   			// Numero de asistencias para ser considerada guardia doblada
   			habilitarCuadroTexto (document.getElementById("hitoPrecio[45]"));
   		
   			// Muestra tr TrGDAs1 y TrGDAs2
   			jQuery("#TrGDAs1").show();
   			jQuery("#TrGDAs2").show();
    	}
		
		function inhabilitarGDAs() {		
			// Importe por guardia doblada por asistencias
   			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[2]"));
			
   			// Numero de asistencias para ser considerada guardia doblada
   			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[45]"));
   		
   			// Oculta tr TrGDAs1 y TrGDAs2
   			jQuery("#TrGDAs1").hide();
   			jQuery("#TrGDAs2").hide();
    	}
		
		function habilitarGDAc() {		
			// Importe por guardia doblada por actuaciones
   			habilitarCuadroTexto (document.getElementById("hitoPrecio[4]"));
			
   			// Numero de actuaciones para ser considerar guardia doblada
   			habilitarCuadroTexto (document.getElementById("hitoPrecio[46]"));
   		
   			// Muestra tr TrGDAc1 y TrGDAc2
   			jQuery("#TrGDAc1").show();
   			jQuery("#TrGDAc2").show();
    	}
		
		function inhabilitarGDAc() {		
			// Importe por guardia doblada por actuaciones
   			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[4]"));
			
   			// Numero de actuaciones para ser considerar guardia doblada
   			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[46]"));
   		
   			// Oculta tr TrGDAc1 y TrGDAc2
   			jQuery("#TrGDAc1").hide();
   			jQuery("#TrGDAc2").hide();
    	}
		
    	/** Datos:
    	 * - Check Aplicar tipos (chGuardias)
    	 *
    	 * - Check Aplicar Max-Min por Dia  (chNoPagaGuardiaPorDia)
    	 *     	     	 
    	 * - Div de Imorte de Asistencias (DivAs)
	     * - Importe de asistencias (hitoPrecio[5])
	     * - Boton de tipos para importe de asistencias (consultar_10)
		 *
		 * - Div de Maximo por Asistencias (DivAsMax)
    	 * - Check de Maximo por Asistencias (chAsist)		
	     * - Importe maximo de asistencias (hitoPrecio[3])
	     * - Boton de tipos para importe maximo de asistencias (consultar_11)
		 *
		 * - Div de Minimo por Asistencias (DivAsMin)
    	 * - Check de Minimo por Asistencias (chMinAsist)		
	     * - Importe minimo de asistencias (hitoPrecio[10])
    	 *
    	 * - Div de Importe de actuaciones (DivAc)
	     * - Importe de actuaciones (hitoPrecio[7])
	     * - Boton de tipos para importe de actuaciones (consultar_20)
	     *
	     * - Div de Maximo por actuaciones (DivAcMax)
	     * - Check de Maximo por actuaciones (chActuacion)
	     * - Importe maximo de actuaciones (hitoPrecio[8])
	     * - Boton de tipos para importe maximo de actuaciones (consultar_21)
	     *
	     * - Div de Minimo por actuaciones (DivAcMin)  
    	 * - Check de Minimo por actuaciones (chMinActuacion)	    
	     * - Importe minimo de actuaciones (hitoPrecio[19])
	     */		
		function controlNoPagaGuardia() {
			
			if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
				// Check Aplicar tipos  
	   			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chGuardias);
				// Check Aplicar Max-Min por Dia 
	   			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia);
	   			// Radio por Asistencias o Actuaciones  
	   			habilitarCheck (DefinirHitosFacturablesGuardiasForm.radioNPG);
	   			
			} else {
				// Check Aplicar tipos  
	   			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chGuardias);
				// Check Aplicar Max-Min por Dia 
	   			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia);
	   			// Radio por Asistencias o Actuaciones  
	   			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.radioNPG);
			}		
			
			controlAs();
			
			controlAc();
		}
    	
    	/** Datos:
   		 * - Div de Imorte de Asistencias (DivAs)
	     * - Importe de asistencias (hitoPrecio[5])
	     * - Boton de tipos para importe de asistencias (consultar_10)
		 *
		 * - Div de Maximo por Asistencias (DivAsMax)
    	 * - Check de Maximo por Asistencias (chAsist)		
	     * - Importe maximo de asistencias (hitoPrecio[3])
	     * - Boton de tipos para importe maximo de asistencias (consultar_11)
		 *
		 * - Div de Minimo por Asistencias (DivAsMin)
    	 * - Check de Minimo por Asistencias (chMinAsist)		
	     * - Importe minimo de asistencias (hitoPrecio[10])
	     */
	    function controlAs() {
    		// Si NO paga guardias (checkB2) y por asistencias (radioNPG[0])
	      	if (DefinirHitosFacturablesGuardiasForm.checkB2.checked && DefinirHitosFacturablesGuardiasForm.radioNPG[0].checked) {
	      		habilitarAs();
      			
	      		controlAsMax();
      
	      		controlAsMin();
	      		
	      	} else {	      
	      		inhabilitarAs();
	      		
	      		inhabilitarAsMax();
	      		
	      		inhabilitarAsMin();
	      	}
	    } 
    	
    	function habilitarAs() {
    		// Check de Maximo por Asistencias
  			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chAsist);
  			jQuery("#DivAsMax").show();
  			
      		// Check de Minimo por Asistencias
  			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinAsist);
  			jQuery("#DivAsMin").show();
    		
    		// Importe de asistencias
  			habilitarCuadroTexto (document.getElementById("hitoPrecio[5]"));
  		
 			// Boton de tipos para importe de asistencias
  			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
    			habilitarBotonLupa (document.getElementById("consultar_10"));
  			else
    			inhabilitarBotonLupa (document.getElementById("consultar_10"));
 			
  			jQuery("#DivAs").show();
    	}
    	
    	function inhabilitarAs() {    		
    		// Check de Maximo por Asistencias
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chAsist);
			jQuery("#DivAsMax").hide();
    		
			// Check de Minimo por Asistencias
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinAsist);
			jQuery("#DivAsMin").hide();
    		
			// Importe de asistencias
			jQuery("#DivAs").hide();
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[5]"));
			
			// Boton de tipos para importe de asistencias
			inhabilitarBotonLupa (document.getElementById("consultar_10"));
    	}
    	
	    /** Datos:
	     * - Importe minimo de asistencias (hitoPrecio[10])
	     */
	    function controlAsMin() {
	      	//Precio minimo de asistencias
	      	if (DefinirHitosFacturablesGuardiasForm.chMinAsist.checked) {
	      		habilitarAsMin();
	      	} else {
	      		inhabilitarAsMin();	        
	      	}
	    }
    	
	    function habilitarAsMin() {
	    	// Importe minimo de asistencias
	    	habilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
	    }
	    
	    function inhabilitarAsMin() {
	    	// Importe minimo de asistencias
	        inhabilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));	
	    }
    	
	    /** Datos:
	     * - Importe maximo de asistencias (hitoPrecio[3])
	     * - Boton de tipos para importe maximo de asistencias (consultar_11)
	     */
	    function controlAsMax() {
	      	if (DefinirHitosFacturablesGuardiasForm.chAsist.checked) {
	      		habilitarAsMax();	        	
	      	} else {	      		
	      		inhabilitarAsMax();
	      	}
	    } 
	    
   	    function habilitarAsMax() {
   	  		// Importe maximo de asistencias
        	habilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));	 
        	
        	// Boton de tipos para importe maximo de asistencias
        	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
          		habilitarBotonLupa (document.getElementById("consultar_11"));
        	else
          		inhabilitarBotonLupa (document.getElementById("consultar_11"));
	    } 	   
	    
		function inhabilitarAsMax() {
	   		// Importe maximo de asistencias        	
        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
        	
        	// Boton de tipos para importe maximo de asistencias
        	inhabilitarBotonLupa (document.getElementById("consultar_11"));
	    }
		
		/** Datos:
		 * - Div de Importe de actuaciones (DivAc)
	     * - Importe de actuaciones (hitoPrecio[7])
	     * - Boton de tipos para importe de actuaciones (consultar_20)
	     *
	     * - Div de Maximo por actuaciones (DivAcMax)
	     * - Check de Maximo por actuaciones (chActuacion)
	     * - Importe maximo de actuaciones (hitoPrecio[8])
	     * - Boton de tipos para importe maximo de actuaciones (consultar_21)
	     *
	     * - Div de Minimo por actuaciones (DivAcMin)  
    	 * - Check de Minimo por actuaciones (chMinActuacion)	    
	     * - Importe minimo de actuaciones (hitoPrecio[19])
	     */
	    function controlAc() {
	    	// Si NO paga guardias (checkB2) y por actuaciones (radioNPG[1])
	      	if (DefinirHitosFacturablesGuardiasForm.checkB2.checked && DefinirHitosFacturablesGuardiasForm.radioNPG[1].checked) {
	      		habilitarAc();
      			
	      		controlAcMax();
      
	      		controlAcMin();
	      		
	      	} else {
	      		inhabilitarAc();
	      		
	      		inhabilitarAcMax();
	      		
	      		inhabilitarAcMin();
	      	}
	    } 
    	
    	function habilitarAc() {
    		// Check de Maximo por actuaciones
  			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chActuacion);
  			jQuery("#DivAcMax").show();
  			
      		// Check de Minimo por actuaciones
  			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinActuacion);
  			jQuery("#DivAcMin").show();
    		
    		// Importe de actuaciones
  			habilitarCuadroTexto (document.getElementById("hitoPrecio[7]"));
  		
 			// Boton de tipos para importe de actuaciones
  			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
    			habilitarBotonLupa (document.getElementById("consultar_20"));
  			else
    			inhabilitarBotonLupa (document.getElementById("consultar_20"));
 			
  			jQuery("#DivAc").show();
    	}
    	
    	function inhabilitarAc() {  		    		
    		// Check de Maximo por actuaciones
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chActuacion);
			jQuery("#DivAcMax").hide();
    		
			// Check de Minimo por actuaciones
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinActuacion);
			jQuery("#DivAcMin").hide();
    		
			// Importe de actuaciones	
			jQuery("#DivAc").hide();
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[7]"));
			
			// Boton de tipos para importe de actuaciones
			inhabilitarBotonLupa (document.getElementById("consultar_20"));
    	}
    	
	    /** Datos:
	     * - Importe minimo de actuaciones (hitoPrecio[19])
	     */
	    function controlAcMin() {
	      	//Precio minimo de actuaciones
	      	if (DefinirHitosFacturablesGuardiasForm.chMinActuacion.checked) {
	      		habilitarAcMin();
	      	} else {
	      		inhabilitarAcMin();	        
	      	}
	    }
    	
	    function habilitarAcMin() {
	    	// Importe minimo de actuaciones
	    	habilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
	    }
	    
	    function inhabilitarAcMin() {
	    	// Importe minimo de actuaciones
	        inhabilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));	
	    }
    	
	    /** Datos:
	     * - Importe maximo de actuaciones (hitoPrecio[8])
	     * - Boton de tipos para importe maximo de actuaciones (consultar_21)
	     */
	    function controlAcMax() {
	      	if (DefinirHitosFacturablesGuardiasForm.chActuacion.checked) {
	      		habilitarAcMax();	        	
	      	} else {	      		
	      		inhabilitarAcMax();
	      	}
	    } 
	    
   	    function habilitarAcMax() {
   	  		// Importe maximo de actuaciones
        	habilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));	 
        	
        	// Boton de tipos para importe maximo de actuaciones
        	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
          		habilitarBotonLupa (document.getElementById("consultar_21"));
        	else
          		inhabilitarBotonLupa (document.getElementById("consultar_21"));
	    } 	   
	    
		function inhabilitarAcMax() {
	   		// Importe maximo de actuaciones        	
        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
        	
        	// Boton de tipos para importe maximo de actuaciones
        	inhabilitarBotonLupa (document.getElementById("consultar_21"));
	    }
		
		/** Datos:
		 * - Check Aplica tipo de actuaciones fuera de guardia (checkC)			
		 * 			
		 * - Div de Importe de actuaciones fuera de guardia (DivAcFg) 			
	     * - Importe de actuaciones fuera de guardia (hitoPrecio[9])
	     * - Boton de tipos para importe de actuaciones fuera de guardia (consultar_30)
	     *
	     * - Div de Maximo por actuaciones fuera de guardia (DivAcFgMax)
	     * - Check de Maximo por actuaciones fuera de guardia (chActFG)
	     * - Importe maximo de actuaciones fuera de guardia (hitoPrecio[6])
	     * - Boton de tipos para importe maximo de actuaciones fuera de guardia (consultar_31)
	     */
	    function controlAcFg() {
      		habilitarCheck (DefinirHitosFacturablesGuardiasForm.checkC);
			
	    	// Si fuera de guardia (checkC)
	      	if (DefinirHitosFacturablesGuardiasForm.checkC.checked) {
	      		habilitarAcFg();
      			
	      		controlAcFgMax();      
	      		
	      	} else {
	      		inhabilitarAcFg();
	      		
	      		inhabilitarAcFgMax();
	      	}
	    } 
    	
    	function habilitarAcFg() {
    		// Check Aplicar tipos  
   			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoGuardias);
    		
    		// Check de Maximo por actuaciones fuera de guardia
  			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chActFG);
  			jQuery("#DivAcFgMax").show();
    		
    		// Importe de actuaciones fuera de guardia
  			habilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
  		
 			// Boton de tipos para importe de actuaciones fuera de guardia
 			if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
    			habilitarBotonLupa (document.getElementById("consultar_30"));
  			else
    			inhabilitarBotonLupa (document.getElementById("consultar_30"));
 			
  			jQuery("#DivAcFg").show();
    	}
    	
    	function inhabilitarAcFg() {
    		// Check Aplicar tipos  
   			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoGuardias);
    		
    		// Check de Maximo por actuaciones fuera de guardia
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chActFG);
			jQuery("#DivAcFgMax").hide();
    		
			// Importe de actuaciones fuera de guardia
			jQuery("#DivAcFg").hide();
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
			
			// Boton de tipos para importe de actuaciones fuera de guardia
			inhabilitarBotonLupa (document.getElementById("consultar_30"));
    	}
    	
	    /** Datos:
	     * - Importe maximo de actuaciones fuera de guardia (hitoPrecio[6])
	     * - Boton de tipos para importe maximo de actuaciones fuera de guardia (consultar_31)
	     */
	    function controlAcFgMax() {
	      	if (DefinirHitosFacturablesGuardiasForm.chActFG.checked) {
	      		habilitarAcFgMax();	        	
	      	} else {	      		
	      		inhabilitarAcFgMax();
	      	}
	    } 
	    
   	    function habilitarAcFgMax() {
   	  		// Importe maximo de actuaciones fuera de guardia
        	habilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));	 
        	
        	// Boton de tipos para importe maximo de actuaciones fuera de guardia
        	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
          		habilitarBotonLupa (document.getElementById("consultar_31"));
        	else
          		inhabilitarBotonLupa (document.getElementById("consultar_31"));
	    } 	   
	    
		function inhabilitarAcFgMax() {
	   		// Importe maximo de actuaciones fuera de guardia        	
        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
        	
        	// Boton de tipos para importe maximo de actuaciones fuera de guardia
        	inhabilitarBotonLupa (document.getElementById("consultar_31"));
	    }		
	    
	    //////////////////// FUNCIONES DE CHECKS CONCRETOS - INI ////////////////////    
  </script>
  <!-------------------- FUNCIONES SCRIPT - FIN -------------------->  
</head>

<body onload="init()" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr class="titulitosDatos">
    		<td width="50%">
      			<siga:Idioma key="gratuita.confGuardia.literal.turno"/>:&nbsp;
      			<%= (String)request.getAttribute ("NOMBRETURNO") %>
    		</td>
    		<td width="50%">
      			<siga:Idioma key="gratuita.confGuardia.literal.guardia"/>:&nbsp;
      			<%= (String)request.getAttribute ("NOMBREGUARDIA") %>
    		</td>
  		</tr>
  	</table>
  
  	<html:form action="<%=accion%>" method="POST" target="mainPestanas">
    	<html:hidden property = "modo" value = ""/>
    	<html:hidden property = "tipoConsulta" value = ""/>
    	<html:hidden property = "importeMax" value = ""/>
    	<html:hidden property = "actionModal" value = ""/>
    
    	<div id="scrollValores" style="height:100%; width:100%; overflow-y: auto; overflow-x: hidden">    	
    	<div id="divFacturacionNoControlada">
<%
        	if (existenHitos.equals ("0")) {
%>
      			<table class="tablaTitulo" width="100%" border="1" top="0" cellspacing="0" cellpadding="0">
        			<tr>
        				<td class="labelText" style="text-align:center">
          					<siga:Idioma key="gratuita.confGuardia.mensaje1"/>
        				</td>
        			</tr>
      			</table>
<%
        	}
%>

			<div class="labelText">
   				<siga:Idioma key="gratuita.confGuardia.literal.configuracionHistorica"/>
				&nbsp;	
				<html:select name="DefinirHitosFacturablesGuardiasForm" property="buscarFacturacionSJCS" styleId="buscarFacturacionSJCS" styleClass="boxCombo" onchange="buscar()">
					<html:option value=""><siga:Idioma key="gratuita.confGuardia.literal.configuracionHistorica.actual"/></html:option>
<%
					if (vFcsHistoricoHitoFact!=null && vFcsHistoricoHitoFact.size()>0) {
						for (int i=1; i<=vFcsHistoricoHitoFact.size(); i++) {
							Hashtable<String,Object> hFcsHistoricoHitoFact = (Hashtable<String,Object>) vFcsHistoricoHitoFact.get(i-1);
							if (hFcsHistoricoHitoFact!= null) {
								String sIdFacturacion = UtilidadesHash.getString(hFcsHistoricoHitoFact, FcsFacturacionJGBean.C_IDFACTURACION);
								String sNombre = UtilidadesHash.getString(hFcsHistoricoHitoFact, FcsFacturacionJGBean.C_NOMBRE);
								String sFechaDesde = UtilidadesHash.getString(hFcsHistoricoHitoFact, FcsFacturacionJGBean.C_FECHADESDE);
								sFechaDesde = GstDate.getFormatedDateShort("", sFechaDesde);
								String sFechaHasta = UtilidadesHash.getString(hFcsHistoricoHitoFact, FcsFacturacionJGBean.C_FECHAHASTA);							
								sFechaHasta = GstDate.getFormatedDateShort("", sFechaHasta);
%>
								<html:option value="<%=sIdFacturacion%>"><%=sFechaDesde%>-<%=sFechaHasta%> - <%=sNombre%></html:option>
<%							
							}
						}
					}
%>				
				</html:select>
			</div> 
      
      		<table width="100%" border="1" cellspacing="0" cellpadding="0">
        		<!-- Titulos de las columnas -->
        		<tr>
        			<td colspan="2">	
            			<table border="0" cellspacing="0" cellpadding="0">
              				<tr>        			
          						<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
          							<siga:Idioma key="gratuita.confGuardia.literal.guardia"/>
								</td>
							</tr>
							
							<tr <%=estiloPaqueteFacturacionSJCS%>>
			          			<td class="labelText" style="text-align:left; vertical-align:top" nowrap
									 title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosGuardia.titulo.info").replaceAll("\\\\n", ""))%>'>
			          				<siga:Idioma key="gratuita.confGuardia.asuntosAntiguosGuardia.titulo.literal"/>
								</td>

        						<td class="labelText" style="text-align:left; vertical-align:top" nowrap
        							title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosGuardia.actual.info").replaceAll("\\\\n", ""))%>'>
    								<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioConfig" styleId="radioConfig0" value="0"/>
    								<label for="radioConfig0"><siga:Idioma key="gratuita.confGuardia.asuntosAntiguosGuardia.actual.literal"/></label>
    							</td>
								        		
			        			<td class="labelText" style="text-align:left; vertical-align:top" nowrap
			        				title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosGuardia.antigua.info").replaceAll("\\\\n", ""))%>'>
			        				<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioConfig" styleId="radioConfig1" value="1"/>
			    					<label for="radioConfig1"><siga:Idioma key="gratuita.confGuardia.asuntosAntiguosGuardia.antigua.literal"/></label>
			    				</td>
    						</tr>
    					</table>
    				</td> 
          			
          			<td rowspan="3">	
            			<table width="100%" border="0" cellspacing="0" cellpadding="0">
              				<tr>
              					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
                					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkC" property="checkC" onclick="controlAcFg();"/>
                					<label for="checkC"><siga:Idioma key="gratuita.confGuardia.literal.fueraguardia"/></label>
              					</td>
              				</tr>
              				
			        		<tr <%=estiloPaqueteFacturacionSJCS%>>
			          			<td class="labelText" style="text-align:left; vertical-align:top" nowrap
									title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.info").replaceAll("\\\\n", ""))%>'>
			          				<siga:Idioma key="gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.literal"/>
								</td>       
							</tr>
							
			        		<tr <%=estiloPaqueteFacturacionSJCS%>>			
			        			<td class="labelText" style="text-align:left; vertical-align:top" nowrap
			        				 title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.info").replaceAll("\\\\n", ""))%>'>
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioConfigFg" styleId="radioConfigFg0" value="0"/>
			    					<label for="radioConfigFg0"><siga:Idioma key="gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.literal"/></label>
			    				</td>
			    			</tr>                				
							
			        		<tr <%=estiloPaqueteFacturacionSJCS%>>			    				
			    				<td class="labelText" style="text-align:left; vertical-align:top" nowrap
			    					 title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.info").replaceAll("\\\\n", ""))%>'>
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioConfigFg" styleId="radioConfigFg1" value="1"/>
			    					<label for="radioConfigFg1"><siga:Idioma key="gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.literal"/></label>
			    				</td>
			    			</tr>   							
			        		
			        		<tr <%=estiloPaqueteFacturacionSJCS%>>
			        			<td class="labelText" style="text-align:left; vertical-align:top" nowrap
			        				 title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (usr, "gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.info").replaceAll("\\\\n", ""))%>'>
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioConfigFg" styleId="radioConfigFg2" value="2"/>
			    					<label for="radioConfigFg2"><siga:Idioma key="gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.literal"/></label>
			    				</td>
			    			</tr>   		
              				
              				<tr>
              					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>                      				
                					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoGuardias" property="chNoGuardias" onclick="aplicaTiposFG()"/>
                					<label for="chNoGuardias"><siga:Idioma key="gratuita.confGuardia.literal.aplicartipos"/></label>
              					</td>
              				</tr>              				              				
            			</table>
          			</td>
        		</tr>
        		
				<tr>	
					<td>	
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
    						<tr>
			         			<td class="labelText" style="text-align:left; vertical-align:top" nowrap> 
     								<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkB1" property="checkB1" value="0" onclick="cambiarCheckB1();" />
     								<label for="checkB1"><siga:Idioma key="gratuita.confGuardia.literal.pagaguardia"/></label>
   								</td>
   							</tr>
   							
		                	<tr>
			                	<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
			                  		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaPorDia" property="chPagaGuardiaPorDia"/>
			                  		<label for="chPagaGuardiaPorDia"><siga:Idioma key="fcs.criteriosFacturacion.guardia.porDia"/></label>
			    				</td>
			    			</tr>
			    			
			        		<tr>
			        			<td colspan="2">
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioPG" styleId="radioPGAs" value="0" onclick="initAB();"/>          							
			    					<label for="radioPGAs"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.porAsistencias"/></label>
			    					&nbsp;
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioPG" styleId="radioPGAc" value="1" onclick="initAB();"/>            						
			      					<label for="radioPGAc"><siga:Idioma key="fcs.criteriosFacturacion.actuacion.porActuaciones"/></label>
			    				</td>
			    			</tr> 			    			
			    		</table>
			    	</td>   							   						
   					   
   					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
    						<tr>
			         			<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
									<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkB2" property="checkB2" value="1" onclick="cambiarCheckB2 ();" />
		                 			<label for="checkB2"><siga:Idioma key="gratuita.confGuardia.literal.nopagaguardia"/></label>
		                 		</td>
		                 	</tr>
		                 	
		                 	<tr>
               					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>                      						
          							<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chGuardias" property="chGuardias" onclick="aplicaTipos()"/>
              						<label for="chGuardias"><siga:Idioma key="gratuita.confGuardia.literal.aplicartipos"/></label>
               					</td>
		                	</tr>
		                	
		                	<tr>
			                	<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
			      					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaPorDia" property="chNoPagaGuardiaPorDia"/>
			      					<label for="chNoPagaGuardiaPorDia"><siga:Idioma key="fcs.criteriosFacturacion.guardia.porDia"/></label>
			    				</td>
			    			</tr>
			    			
			        		<tr>
			        			<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioNPG" styleId="radioNPGAs" value="0" onclick="initAB();"/>          							
			    					<label for="radioNPGAs"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.porAsistencias"/></label>
			    					&nbsp;
			    					<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioNPG" styleId="radioNPGAc" value="1" onclick="initAB();"/>            						
			      					<label for="radioNPGAc"><siga:Idioma key="fcs.criteriosFacturacion.actuacion.porActuaciones"/></label>
			    				</td>
			    			</tr> 					    			
		                </table>
                    </td>
				</tr>  				
                   
                <tr>
                	<td>
	                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaLunes"><siga:Idioma key="gratuita.checkbox.literal.Lunes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaMartes"><siga:Idioma key="gratuita.checkbox.literal.Martes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaMiercoles"><siga:Idioma key="gratuita.checkbox.literal.Miercoles"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaJueves"><siga:Idioma key="gratuita.checkbox.literal.Jueves"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaViernes"><siga:Idioma key="gratuita.checkbox.literal.Viernes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaSabado"><siga:Idioma key="gratuita.checkbox.literal.Sabado"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chPagaGuardiaDomingo"><siga:Idioma key="gratuita.checkbox.literal.Domingo"/></label></td>
							</tr>	 
							
							<tr>
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaLunes" property="chPagaGuardiaLunes" onclick="pagaGuardiaLunes()" />
                				</td>             				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaMartes" property="chPagaGuardiaMartes" onclick="pagaGuardiaMartes()" />
                				</td>             				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaMiercoles" property="chPagaGuardiaMiercoles" onclick="pagaGuardiaMiercoles()" />
                				</td>               				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaJueves" property="chPagaGuardiaJueves" onclick="pagaGuardiaJueves()" />
                				</td>                				
                				<td align="center"> 
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaViernes" property="chPagaGuardiaViernes" onclick="pagaGuardiaViernes()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaSabado" property="chPagaGuardiaSabado" onclick="pagaGuardiaSabado()" />
                				</td>              				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaDomingo" property="chPagaGuardiaDomingo" onclick="pagaGuardiaDomingo()" />
                				</td>	
                			</tr>	               		
	                	</table>
	                </td>                   

                	<td>
	                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaLunes"><siga:Idioma key="gratuita.checkbox.literal.Lunes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaMartes"><siga:Idioma key="gratuita.checkbox.literal.Martes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaMiercoles"><siga:Idioma key="gratuita.checkbox.literal.Miercoles"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaJueves"><siga:Idioma key="gratuita.checkbox.literal.Jueves"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaViernes"><siga:Idioma key="gratuita.checkbox.literal.Viernes"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaSabado"><siga:Idioma key="gratuita.checkbox.literal.Sabado"/></label></td>
								<td class="labelText" style="text-align:center"><label for="chNoPagaGuardiaDomingo"><siga:Idioma key="gratuita.checkbox.literal.Domingo"/></label></td>
							</tr>	 
							
							<tr>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaLunes" property="chNoPagaGuardiaLunes" onclick="noPagaGuardiaLunes()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaMartes" property="chNoPagaGuardiaMartes" onclick="noPagaGuardiaMartes()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaMiercoles" property="chNoPagaGuardiaMiercoles" onclick="noPagaGuardiaMiercoles()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaJueves" property="chNoPagaGuardiaJueves" onclick="noPagaGuardiaJueves()" />
                				</td>                					
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaViernes" property="chNoPagaGuardiaViernes" onclick="noPagaGuardiaViernes()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaSabado" property="chNoPagaGuardiaSabado" onclick="noPagaGuardiaSabado()" />
                				</td>                				
                				<td align="center">
                  					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoPagaGuardiaDomingo" property="chNoPagaGuardiaDomingo" onclick="noPagaGuardiaDomingo()" />
                				</td>
              				</tr>        							               		
	                	</table>
	                </td>                    
                </tr>
                
				<!-- Linea Minimo -->
	            <tr>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                	<div id="DivGAsMin">
	                		<table border="0" cellspacing="0" cellpadding="0" <%=estiloGuardiasInactivas%>>
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.asistencia.importeInactiva"/>
									</td>
									<td>
			                			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[53]"
				                       		property="hitoPrecio[53]" maxlength="10" size="10"
					                       	onkeypress="filterCharsNumberEs(this,false,true);"
					                       	onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
			                       	</td>
		                       	</tr>
	                       	</table>
                       </div>
                       
						<div id="DivGAcMin">
	                		<table border="0" cellspacing="0" cellpadding="0" <%=estiloGuardiasInactivas%>>
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacion.importeInactiva"/>
									</td>
									<td>
			                			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[54]"
				                       		property="hitoPrecio[54]" maxlength="10" size="10"
					                       	onkeypress="filterCharsNumberEs(this,false,true);"
					                       	onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
			                       	</td>
		                       	</tr>
	                       	</table>
                       </div>&nbsp;&nbsp;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                	<div id="DivAsMin">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.asistencia.importeMinimo"/>
									</td>
									<td>		                	
				                		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chMinAsist" styleId="chMinAsist" onclick="controlAsMin();"/>
				                		&nbsp;
										<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[10]"
											property="hitoPrecio[10]" maxlength="10" size="10"
											onkeypress="filterCharsNumberEs(this,false,true);"
											onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
				                    </td>
		                       	</tr>
	                       	</table>											
						</div>
						
						<div id="DivAcMin">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacion.importeMinimo"/>
									</td>
									<td>							
										<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chMinActuacion" styleId="chMinActuacion" onclick="controlAcMin();"/>
										&nbsp;
										<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[19]"
			                             property="hitoPrecio[19]" maxlength="10" size="10"
			                             onkeypress="filterCharsNumberEs(this,false,true);"
			                             onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
				                    </td>
		                       	</tr>
	                       	</table>			                             
						</div>&nbsp;
	                </td>
	                
	                <td>
	                  	&nbsp;
	                </td>
				</tr>	
              
				<!-- Linea Importe -->
	            <tr>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle" width="33%">
	                	<div id="DivGAs">
	                		<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.asistencia.importeGuardia"/>
									</td>
									<td>
			                			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[1]"
				                       		property="hitoPrecio[1]" maxlength="10" size="10"
					                       	onkeypress="filterCharsNumberEs(this,false,true);"
					                       	onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
			                       	</td>
		                       	</tr>
	                       	</table>
                       </div>
                       
                       <div id="DivGAc">
	                		<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacion.importeGuardia"/>
									</td>
									<td>
			                			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[44]"
				                       		property="hitoPrecio[44]" maxlength="10" size="10"
					                       	onkeypress="filterCharsNumberEs(this,false,true);"
					                       	onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
			                       	</td>
		                       	</tr>
	                       	</table>
                       </div>&nbsp;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle" width="33%">
                		<div id="DivAs">
	                		<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.asistencia.importe"/>
									</td>
									<td>
					                	<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[5]"
											property="hitoPrecio[5]" maxlength="10" size="10"
						                    onkeypress="filterCharsNumberEs(this,false,true);"
						                    onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
										<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
					                       	name="consultar_1" id="consultar_10" alt="Consultar"
					                       	onclick="consultarAsist(0)" onMouseOut="" onMouseOver=""
					                       	border="0" style="cursor:default; vertical-align:middle"
					                       	disabled>
			                       	</td>
		                       	</tr>
	                       	</table>					                       	
                       	</div>
                       	
						<div id="DivAc">
	                		<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacion.importe"/>
									</td>
									<td>
			                  			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[7]"
			                             	property="hitoPrecio[7]" maxlength="10" size="10"
			                             	onkeypress="filterCharsNumberEs(this,false,true);"
			                             	onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
			                  			<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
			                       			name="consultar_1" id="consultar_20" alt="Consultar"
			                       			onclick="consultarAct(0)" onMouseOut="" onMouseOver=""
			                       			border="0" style="cursor:default; vertical-align:middle"
			                       			disabled>
			                       	</td>
		                       	</tr>
	                       	</table>			                       			
                     	</div>&nbsp;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle" width="33%">
	                	<div id="DivAcFg">
	                		<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacionFueraGuardia.importe"/>
									</td>
									<td>
					                  	<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[9]"
				                             property="hitoPrecio[9]" maxlength="10" size="10"
				                             onkeypress="filterCharsNumberEs(this,false,true);"
				                             onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
				                  		<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
				                       		name="consultar_1" id="consultar_30" alt="Consultar"
				                       		onclick="consultarAct(0)" onMouseOut="" onMouseOver=""
				                       		border="0" style="cursor:default; vertical-align:middle"
				                       		disabled>
			                       	</td>
		                       	</tr>
	                       	</table>				                       		
                     	</div>&nbsp;
	                </td>
	       		</tr>	       	       	
              
	            <!-- Linea Maximo -->
	            <tr>
	            	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
	            		<table border="0" cellspacing="0" cellpadding="0" id="TabGDAx">
							<tr id="TrGDAs1">
								<td class="labelText" style="text-align:left; vertical-align:middle">
									<siga:Idioma key="gratuita.confGuardia.asistencia.doblada.numero"/>
								</td>
								<td>
				                  	>&nbsp;
				                  	<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[45]"
					                  property="hitoPrecio[45]" maxlength="3" size="3"
					                  onkeypress="filterCharsNumberEs(this,false,true);"
					                  onblur="filterCharsNaN(this);"
					                  styleClass="boxNumber"/>
				                </td>
			               </tr>
				               
							<tr id="TrGDAs2">
								<td class="labelText" style="text-align:left; vertical-align:middle">
									<siga:Idioma key="gratuita.confGuardia.asistencia.doblada.importe"/>
								</td>
								<td>				               
		               				<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[2]"
										property="hitoPrecio[2]" maxlength="10" size="10"
										onkeypress="filterCharsNumberEs(this,false,true);"
										onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
		                       	</td>
	                       	</tr>																		
						
							<tr id="TrGDAc1">
								<td class="labelText" style="text-align:left; vertical-align:middle">
									<siga:Idioma key="gratuita.confGuardia.actuacion.doblada.numero"/>
								</td>
								<td>
			                  		>&nbsp;
			                  		<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[46]"
			                             property="hitoPrecio[46]" maxlength="3" size="3"
			                             onkeypress="filterCharsNumberEs(this,false,true);"
			                             onblur="filterCharsNaN(this);"
			                             styleClass="boxNumber"/>
				                </td>
			               </tr>
			               
							<tr id="TrGDAc2">
								<td class="labelText" style="text-align:left; vertical-align:middle">
									<siga:Idioma key="gratuita.confGuardia.actuacion.doblada.importe"/>
								</td>
								<td>				                             
									<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[4]"
			                             property="hitoPrecio[4]" maxlength="10" size="10"
			                             onkeypress="filterCharsNumberEs(this,false,true);"
			                             onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
			                    </td>
	                       	</tr>	                       	
                        </table>&nbsp;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                	<div id="DivAsMax">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.asistencia.importeMaximo"/>
									</td>
									<td>	                	
				                		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chAsist" styleId="chAsist" onclick="controlAsMax();"/>
				                		&nbsp;
					                  	<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[3]"
						                   	property="hitoPrecio[3]" maxlength="10" size="10"
						                   	onkeypress="filterCharsNumberEs(this,false,true);"
											onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
					                  	<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
						                  	name="consultar_1" id="consultar_11" alt="Consultar"
						                  	onclick="consultarAsist(1)" onMouseOut="" onMouseOver=""
						                  	border="0" style="cursor:default; vertical-align:middle"
						                  	disabled>
				                    </td>
		                       	</tr>
	                       	</table>						                  	
	                  	</div>
	                  	
	                  	<div id="DivAcMax">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacion.importeMaximo"/>
									</td>
									<td>	                  	
				                  		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chActuacion" styleId="chActuacion" onclick="controlAcMax();"/>
				                  		&nbsp;
						                <html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[8]"
				                             property="hitoPrecio[8]" maxlength="10" size="10"
				                             onkeypress="filterCharsNumberEs(this,false,true);"
				                             onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
				                  		<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
				                       		name="consultar_1" id="consultar_21" alt="Consultar"
				                       		onclick="consultarAct(1)" onMouseOut="" onMouseOver=""
				                       		border="0" style="cursor:default; vertical-align:middle"
				                       		disabled>
				                    </td>
		                       	</tr>
	                       	</table>				                       		
	                  	</div>&nbsp;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                	<div id="DivAcFgMax">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="labelText" style="text-align:left; vertical-align:middle">
										<siga:Idioma key="gratuita.confGuardia.actuacionFueraGuardia.importeMaximo"/>
									</td>
									<td>	                	
				                		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chActFG" property="chActFG" onclick="controlAcFgMax();"/>
				                		&nbsp;
										<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[6]"
				                       		property="hitoPrecio[6]" maxlength="10" size="10"
				                       		onkeypress="filterCharsNumberEs(this,false,true);"
				                       		onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;&nbsp;
				            			<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
				                 			name="consultar_1" id="consultar_31" alt="Consultar"
				                 			onclick="consultarAct(1)" onMouseOut="" onMouseOver=""
				                 			border="0" style="cursor:default; vertical-align:middle"
				                 			disabled>
				                    </td>
		                       	</tr>
	                       	</table>				                 			
               			</div>&nbsp;
	                </td>
				</tr>             
      		</table>
   		</div>  
   		
   		<div id="divFacturacionControlada">
   			<siga:ConjCampos leyenda="fcs.criteriosFacturacion.facturacionGuardias">
	   			<table width="100%" border="0" cellspacing="0" cellpadding="0">   			
					<tr class="filaTablaImpar">
						<td class="labelText">
							<input id="checkControlado1" name="checkControlado" TYPE="radio" VALUE="1" onclick="gestionarControlado(this.value, false);" <%if(checkControlado.equals("1")){%>checked<%}%>>
							<label for="checkControlado1"><siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAs"/></label>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsImporte" class="boxNumber" type="text" maxlength="10" size="10" value="" 
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;
						</td>
					</tr>
					
					
					<tr class="filaTablaPar">
						<td class="labelText" rowspan="2">
							<input id="checkControlado2" name="checkControlado" TYPE="radio" VALUE="2" onclick="gestionarControlado(this.value);" <%if(checkControlado.equals("2")){%>checked<%}%>>
							<label for="checkControlado2"><siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAsMin"/></label>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsMinImporte" class="boxNumber" type="text" maxlength="10" size="10" value="" 
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;
						</td>
					</tr>
					<tr class="filaTablaPar">
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.minAsist"/></td>
						<td class="labelTextValue">
							<input id="AsMinMinimo" class="boxNumber" type="text" maxlength="10" size="10" value="" 
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;
						</td>				
					</tr>			
					
					
					<tr class="filaTablaImpar">
						<td class="labelText" rowspan="2">
							<input id="checkControlado3" name="checkControlado" TYPE="radio" VALUE="3" onclick="gestionarControlado(this.value);" <%if(checkControlado.equals("3")){%>checked<%}%>>
							<label for="checkControlado3"><siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAsTpMin"/></label>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsTpImporte" class="boxNumber" type="text" maxlength="10" size="10" value="" 
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;&nbsp;
	              			<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
	                   			name="AsTpTipos" id="AsTpTipos" alt="Consultar"
	                   			onclick="consultarAsist(0)" onMouseOut="" onMouseOver=""
								border="0" style="cursor:default; vertical-align:middle"
								disabled>
						</td>
					</tr>
					<tr class="filaTablaImpar">	
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.minAsist"/></td>
						<td class="labelTextValue">
							<input id="AsTpMinimo" class="boxNumber" type="text" maxlength="10" size="10" value="" 
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;
						</td>
					</tr>		
					
					
					<tr class="filaTablaPar">
						<td class="labelText">
							<input id="checkControlado4" name="checkControlado" TYPE="radio" VALUE="4" onclick="gestionarControlado(this.value);" <%if(checkControlado.equals("4")){%>checked<%}%>>
							<label for="checkControlado4"><siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaGAs"/></label>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.diaGuardia"/></td>
						<td class="labelTextValue">
							<input id="GAsImporte" class="boxNumber" type="text" maxlength="10" size="10" value=""
								onkeypress="filterCharsNumberEs(this,false,true);"
	                       		onblur="filterCharsNaN(this);"/>&nbsp;&euro;
						</td>
					</tr>
										
										
					<tr class="filaTablaImpar">
						<td class="labelText" colspan="3">							
							<input id="checkControlado5" name="checkControlado" TYPE="radio" VALUE="5" onclick="gestionarControlado(this.value);" <%if(checkControlado.equals("5")){%>checked<%}%>>
							<label for="checkControlado5"><siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.noControlada"/></label>
						</td>	
					</tr>   							   							   							   			
	      		</table>
	      	</siga:ConjCampos>
   		</div>
   		
   		<siga:ConjCampos leyenda="fcs.criteriosFacturacion.facturacionExpedientes">
			<table align="center" border="0" width="100%">
				<tr>
					<td class="labelText">
						<siga:Idioma key="fcs.criteriosFacturacion.asistencia.expedienteEJG"/>
					</td>
					
					<td class="labelTextValue">	
						<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[13]"
							property="hitoPrecio[13]" maxlength="10" size="10"
							onkeypress="filterCharsNumberEs(this,false,true);"
							onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="fcs.criteriosFacturacion.asistencia.expedienteSOJ"/>
					</td>
					
					<td class="labelTextValue">	
            			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[12]"
                       		property="hitoPrecio[12]" maxlength="10" size="10"
                       		onkeypress="filterCharsNumberEs(this,false,true);"
                       		onblur="filterCharsNaN(this);" styleClass="boxNumber" />&nbsp;&euro;
					</td>
				</tr>
			</table>
   		</siga:ConjCampos>    
   	</div>	   		    		
  </html:form>
  
  <siga:ConjBotonesAccion botones="G,V" modo="<%=modopestanha%>" clase="botonesDetalle"/>
  <iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
  
</body>
<!-------------------- CUERPO - FIN -------------------->
</html>