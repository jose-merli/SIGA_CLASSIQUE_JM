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
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
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
  
  UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
  String entrada=(String)request.getSession().getAttribute("entrada");
  String acceso=usr.getAccessType();
  String accion="/JGR_DefinirHitosFacturables.do";
  String existenHitos=(String)request.getAttribute("EXISTENHITOS");
  String checkControlado=(String)request.getAttribute("checkControlado");
  String importeControlado=(String)request.getAttribute("importeControlado");
  String minimoControlado=(String)request.getAttribute("minimoControlado");
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
  		// Transforma la coma en punto, comprueba que es un numero y muestra dos decimales
		function  convertirAFormato(numero){
			var numeroFormateado = numero.replace(",", ".");
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
	    	var valorControlado = jQuery("input[id=checkControlado]:checked").val();
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
						aplicaGuardias();
					}
					if (document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = false;
						habilitarMinAsist();
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
						aplicaGuardias();
					}
					if (!document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = true;
						habilitarMinAsist();
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
						aplicaGuardias();
					}		
					if (!document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = true;
						habilitarMinAsist();
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
						aplicaGuardias();
					}
					if (document.getElementById("chMinAsist").checked) {
						document.getElementById("chMinAsist").checked = false;
						habilitarMinAsist();
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
	      	if (DefinirHitosFacturablesGuardiasForm.radioA[0].checked) {
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
	      	elem.className="box";
	      	elem.disabled=false;
	    } //habilitarCuadroTexto ()
	    
	    function inhabilitarCuadroTexto (elem) {
	      	elem.value=0;
	      	elem.className="boxDisabled";
	      	elem.disabled=true;
	    } //inhabilitarCuadroTexto ()
	    
	    function inhabilitarCuadroTextoSinCero (elem) {
	      	elem.className="boxDisabled";
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
	      	initC ();		      	
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
	      	//Dias que se paga por guardia Y
	      	//Dias que no se paga por guardia
	      	initDiasSemana ();
	      
      		//Habilitando segun B solo
	      	if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
	        	//Check de paga guardia por dia
	        	habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia);
	        	//Precio por dia de guardia presencial
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[1]"));
	        
	      	} else {
		        //Check de paga guardia por dia
	    	    inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaPorDia);
	        	//Precio por dia de guardia presencial
		        inhabilitarCuadroTexto (document.getElementById("hitoPrecio[1]"));
	      	}
	      
	      	if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
		        //Check de No Paga Guardia
	    	    habilitarCheck (DefinirHitosFacturablesGuardiasForm.chGuardias);
		        //Check de no paga guardia por dia
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia);
	        
		    } else {
		        //Check de No Paga Guardia
	    	    inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chGuardias);
	        	//Check de no paga guardia por dia
       			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaPorDia);
	      	}      
      
      		//Habilitando segun A y B
      		if (DefinirHitosFacturablesGuardiasForm.radioA[0].checked) {
        		if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
          			//Precio por guardia simple doblada por asistencias
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[2]"));
          			//Maximo asistencias para doblar
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[45]"));
          			
        		} else //inhabilitando
          			inhabilitarDependientesA0B1 ();
        
        		if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
          			//Check de Maximo por Asistencias
          			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chAsist);
          			//Check de Minimo por Asistencias
          			habilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinAsist);
          			//Precio de asistencias
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[5]"));
          			//Boton de tipos para precio de asistencias
          			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
            			habilitarBotonLupa (document.getElementById("consultar_10"));
          			else //inhabilitando
            			inhabilitarBotonLupa (document.getElementById("consultar_10"));
          
          			//Precio maximo de asistencias Y
          			//Boton de tipos para precio maximo de asistencias
          			if (DefinirHitosFacturablesGuardiasForm.chAsist.checked) {
            			habilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
            			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
              				habilitarBotonLupa (document.getElementById("consultar_11"));
            			else //inhabilitando
              			inhabilitarBotonLupa (document.getElementById("consultar_11"));
            			
          			} else { //inhabilitando
            			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
            			inhabilitarBotonLupa (document.getElementById("consultar_11"));
          			}
          
          			//Precio minimo de asistencias
          			if (DefinirHitosFacturablesGuardiasForm.chMinAsist.checked)
            			habilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
          			else //inhabilitando
            			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
          		
        		} else //inhabilitando
          			inhabilitarDependientesA0B2 ();
        
        		//inhabilitando
        		inhabilitarDependientesA1B1 ();
        		inhabilitarDependientesA1B2 ();
        		inhabilitarDependientesA1C ();
        		
      		} else { //radioA[1]
        		if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
          			//Precio por guardia simple doblada por actuaciones
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[4]"));
          			//Maximo actuaciones para doblar
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[46]"));
          			
        		} else //inhabilitando
          			inhabilitarDependientesA1B1 ();
        
        		if (DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
					//Check de Maximo por Actuaciones
		          	habilitarCheck (DefinirHitosFacturablesGuardiasForm.chActuacion);
		          	//Check de Minimo por Actuaciones
		          	habilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinActuacion);
		          	//Precio de actuaciones
		          	habilitarCuadroTexto (document.getElementById("hitoPrecio[7]"));
		          	//Boton de tipos para precio de actuaciones
		          	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
		            	habilitarBotonLupa (document.getElementById("consultar_20"));
		          	else //inhabilitando
		            	inhabilitarBotonLupa (document.getElementById("consultar_20"));
          
		          	//Precio maximo de actuaciones Y
		          	//Boton de tipos para precio maximo de actuaciones
		          	if (DefinirHitosFacturablesGuardiasForm.chActuacion.checked) {
            			habilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
            			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
              				habilitarBotonLupa (document.getElementById("consultar_21"));
            			else //inhabilitando
              				inhabilitarBotonLupa (document.getElementById("consultar_21"));
            			
          			} else { //inhabilitando
            			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
            			inhabilitarBotonLupa (document.getElementById("consultar_21"));
          			}
          
          			//Precio minimo de actuaciones
          			if (DefinirHitosFacturablesGuardiasForm.chMinActuacion.checked)
            			habilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
          			else //inhabilitando
            			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
          			
        		} else //inhabilitando
          			inhabilitarDependientesA1B2 ();
        
        		//Precio de actuaciones fuera de guardia Y
        		//Boton de tipos para precio de actuaciones fuera de guardia
        		if (DefinirHitosFacturablesGuardiasForm.checkC.checked) {
          			habilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
          			if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
            			habilitarBotonLupa (document.getElementById("consultar_30"));
          			else //inhabilitando
            			inhabilitarBotonLupa (document.getElementById("consultar_30"));
          			
        		} else //inhabilitando
          			inhabilitarDependientesA1C ();
        
        		//inhabilitando
        		inhabilitarDependientesA0B1 ();
        		inhabilitarDependientesA0B2 ();
    		}
    	} //initAB ()
    
	    /** 
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Dias que se paga por guardia
	     * Dias que no se paga por guardia
	     */
	    function initDiasSemana () {
			if (DefinirHitosFacturablesGuardiasForm.checkB1.checked) {
				//habilitando Dias que se paga por guardia
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
				habilitarCheck (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
	        
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
        		//habilitando Dias que no se paga por guardia
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
		        habilitarCheck (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
		        
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
      
      		//activando solo todos los Dias que se paga por guardia
      		if (DefinirHitosFacturablesGuardiasForm.checkB1.checked && !DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaLunes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMartes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaMiercoles);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaJueves);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaViernes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaSabado);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chPagaGuardiaDomingo);
      		
      		
	   		//activando solo todos los Dias que no se paga por guardia
      		} else if (!DefinirHitosFacturablesGuardiasForm.checkB1.checked && DefinirHitosFacturablesGuardiasForm.checkB2.checked) {
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaLunes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMartes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaMiercoles);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaJueves);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaViernes);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaSabado);
		        inhabilitarCheckActivo (DefinirHitosFacturablesGuardiasForm.chNoPagaGuardiaDomingo);
	      	}
    	} //initDiasSemana ()
    
	    function compruebaPagasGuardia () {
	    	//Este metodo comprueba si hay seleccionado algun dia de paga guardia 
	    	//o no paga guardia. en el caso que no haya ningun dia en cualquiera de los dos el check
	    	// de paga guardia o no paga guardia se deshabilitara
	    	with (DefinirHitosFacturablesGuardiasForm) {
	    	
		    	hayDiaPagaGuardiaChecked = chPagaGuardiaLunes.checked ||
		    	chPagaGuardiaMartes.checked ||
		    	chPagaGuardiaMiercoles.checked ||
		    	chPagaGuardiaJueves.checked ||
		    	chPagaGuardiaViernes.checked ||
		    	chPagaGuardiaSabado.checked ||
		    	chPagaGuardiaDomingo.checked;
		    	
		    	//Si no hay ninguno seleccionado y el check esta seleccionado lo deseleccionamos
		    	if(!hayDiaPagaGuardiaChecked && checkB1.checked){
		    		checkB1.checked = false;
		    		cambiarCheckB1();
		    		
		    	} else {		    	
		    		//Si no es el caso puede ser el otro
		    		hayDiaNoPagaGuardiaChecked = chNoPagaGuardiaLunes.checked ||
			    	chNoPagaGuardiaMartes.checked ||
			    	chNoPagaGuardiaMiercoles.checked ||
			    	chNoPagaGuardiaJueves.checked ||
			    	chNoPagaGuardiaViernes.checked ||
			    	chNoPagaGuardiaSabado.checked ||
			    	chNoPagaGuardiaDomingo.checked;
			    	//Si no hay ninguno seleccionado y el check esta seleccionado lo deseleccionamos
			    	
			    	if(!hayDiaNoPagaGuardiaChecked && checkB2.checked){
			    		checkB2.checked = false;
			    		cambiarCheckB2();
			    	}		    	
		    	}	    	 
	    	}
	    
	    }
    	
    	function inhabilitarDependientesA0B1 () {
      		//Precio por guardia simple doblada por asistencias
      		inhabilitarCuadroTexto (document.getElementById("hitoPrecio[2]"));
      		//Maximo asistencias para doblar
      		inhabilitarCuadroTextoSinCero (document.getElementById("hitoPrecio[45]"));
    	} //inhabilitarDependientesA0B1 ()
    
    	function inhabilitarDependientesA0B2 () {
			//Check de Maximo por Asistencias
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chAsist);
			//Check de Minimo por Asistencias
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinAsist);
			//Precio de asistencias
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[5]"));
			//Boton de tipos para precio de asistencias
			inhabilitarBotonLupa (document.getElementById("consultar_10"));
			//Precio maximo de asistencias Y
			//Boton de tipos para precio maximo de asistencias
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
			inhabilitarBotonLupa (document.getElementById("consultar_11"));
			//Precio minimo de asistencias
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
    	} //inhabilitarDependientesA0B2 ()
    
    	function inhabilitarDependientesA1B1 () {
			//Precio por guardia simple doblada por actuaciones
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[4]"));
			//Maximo actuaciones para doblar
			inhabilitarCuadroTextoSinCero (document.getElementById("hitoPrecio[46]"));
	    } //inhabilitarDependientesA1B1 ()
    
    	function inhabilitarDependientesA1B2 () {
			//Check de Maximo por Actuaciones
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chActuacion);
			//Check de Minimo por Actuaciones
			inhabilitarCheck (DefinirHitosFacturablesGuardiasForm.chMinActuacion);
			//Precio de actuaciones
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[7]"));
			//Boton de tipos para precio de actuaciones
			inhabilitarBotonLupa (document.getElementById("consultar_20"));
			//Precio maximo de actuaciones Y
			//Boton de tipos para precio maximo de actuaciones
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
			inhabilitarBotonLupa (document.getElementById("consultar_21"));
			//Precio minimo de actuaciones
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
    	} //inhabilitarDependientesA1B2 ()
    
    	function inhabilitarDependientesA1C () {
			//Precio de actuaciones fuera de guardia
			inhabilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
			//Boton de tipos para precio de actuaciones fuera de guardia
			inhabilitarBotonLupa (document.getElementById("consultar_30"));
    	} //inhabilitarDependientesA1C ()
    
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
    
	    /** 
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Check de Aplicar tipos de Fuera de Guardia
	     * Check de Maximos de actuaciones fuera de guardia
	     * 
	     * Precio de actuaciones fuera de guardia
	     * Boton de tipos para precio de actuaciones fuera de guardia
	     * 
	     * Precio maximo de actuaciones fuera de guardia
	     * Boton de tipos para precio maximo de actuaciones fuera de guardia
	     */
	    function initC () {
	      	//checkC activo
	      	if (DefinirHitosFacturablesGuardiasForm.checkC.checked) {
     			//Check de Aplicar tipos
				jQuery("#chNoGuardias").removeAttr("disabled");
	        	//Check de Maximos de actuaciones fuera de guardia
	        	jQuery("#chActFG").removeAttr("disabled");	
	        
	        	//Si se paga por actuaciones
		        if (DefinirHitosFacturablesGuardiasForm.radioA[1].checked) {
		          	//Precio de actuaciones fuera de guardia
		          	habilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
		          	//Boton de tipos para precio de actuaciones fuera de guardia
		          	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
		            	habilitarBotonLupa (document.getElementById("consultar_30"));
		          	else
		            	inhabilitarBotonLupa (document.getElementById("consultar_30"));
		          	
		        } else {
		          	//Precio de actuaciones fuera de guardia
		          	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
		          	//Boton de tipos para precio de actuaciones fuera de guardia
		          	inhabilitarBotonLupa (document.getElementById("consultar_30"));
		        }
		        
		        //Si hay maximo de actuaciones fuera de guardia
		        if (DefinirHitosFacturablesGuardiasForm.chActFG.checked) {
		          	//Precio maximo de actuaciones fuera de guardia
		          	habilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
		          	//Boton de tipos para precio maximo de actuaciones fuera de guardia
		          	habilitarBotonLupa (document.getElementById("consultar_31"));
		          	
		        } else {
		          	//Precio maximo de actuaciones fuera de guardia
		          	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
		          	//Boton de tipos para precio maximo de actuaciones fuera de guardia
		          	inhabilitarBotonLupa (document.getElementById("consultar_31"));
	        	}
		        
      		} else {//checkC inactivo
				//Check de Aplicar tipos
				DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked=false;
				jQuery("#chNoGuardias").attr("disabled","disabled");
				//Check de Maximos de actuaciones fuera de guardia
				DefinirHitosFacturablesGuardiasForm.chActFG.checked=false;
				jQuery("#chActFG").attr("disabled","disabled");
				//Precio actuaciones fuera de guardia
				inhabilitarCuadroTexto (document.getElementById("hitoPrecio[9]"));
				//Boton de tipos para precio de actuaciones fuera de guardia
				inhabilitarBotonLupa (document.getElementById("consultar_30"));
				//Precio maximo de actuaciones fuera de guardia
				inhabilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
				//Boton de tipos para preciom maximo de actuaciones fuera de guardia
				inhabilitarBotonLupa (document.getElementById("consultar_31"));
	      	}  
	    } //initC ()
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
                DefinirHitosFacturablesGuardiasForm.checkB1)
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
	    function aplicaGuardias () {
			if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked && DefinirHitosFacturablesGuardiasForm.radioA[0].checked) {
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
	      
	      	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked && DefinirHitosFacturablesGuardiasForm.radioA[1].checked) {
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
    	} //aplicaGuardias ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Boton de tipos para precio de actuaciones fuera de guardia
	     * Boton de tipos para precio maximo de actuaciones fuera de guardia
	     * 
	     * LLAMADO POR:
	     * Check de Aplicar tipos de Fuera de Guardia
	     */
	    function aplicaFueraGuardias () {
	      	//Boton de tipos para precio de actuaciones fuera de guardia
	      	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked && DefinirHitosFacturablesGuardiasForm.radioA[1].checked)
	        	habilitarBotonLupa (document.getElementById("consultar_30"));
	      	else
	        	inhabilitarBotonLupa (document.getElementById("consultar_30"));
	      
	      	//Boton de tipos para precio maximo de actuaciones fuera de guardia
	      	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked && DefinirHitosFacturablesGuardiasForm.chActFG.checked)
	        	habilitarBotonLupa (document.getElementById("consultar_31"));
	      	else
	        	inhabilitarBotonLupa (document.getElementById("consultar_31"));
    	} //aplicaFueraGuardias ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Precio maximo de actuaciones fuera de guardia
	     * Boton de tipos para precio maximo de actuaciones fuera de guardia
	     * 
	     * LLAMADO POR:
	     * Check de maximo de actuaciones fuera de guardia
	     */
	    function habilitarFG () {
	      	if (DefinirHitosFacturablesGuardiasForm.chActFG.checked) {
	        	//Precio maximo de actuaciones fuera de guardia
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
	        	//Boton de tipos para precio maximo de actuaciones fuera de guardia
	        	if (DefinirHitosFacturablesGuardiasForm.chNoGuardias.checked)
	          		habilitarBotonLupa (document.getElementById("consultar_31"));
	        	else
	          		inhabilitarBotonLupa (document.getElementById("consultar_31"));
	        	
	      	} else {
	        	//Precio maximo de actuaciones fuera de guardia
	        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[6]"));
	        	//Boton de tipos para precio maximo de actuaciones fuera de guardia
	        	inhabilitarBotonLupa (document.getElementById("consultar_31"));
	      	}
	    } //habilitarFG ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Precio maximo de asistencias
	     * Boton de tipos para precio maximo de asistencias
	     * 
	     * LLAMADO POR:
	     * Check de maximo de asistencias
	     */
	    function habilitarAsist () {
	      	if (DefinirHitosFacturablesGuardiasForm.chAsist.checked) {
	        	//Precio maximo de asistencias
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
	        	//Boton de tipos para precio maximo de asistencias
	        	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
	          		habilitarBotonLupa (document.getElementById("consultar_11"));
	        	else
	          		inhabilitarBotonLupa (document.getElementById("consultar_11"));
	        	
	      	} else {
	        	//Precio maximo de asistencias
	        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[3]"));
	        	//Boton de tipos para precio maximo de asistencias
	        	inhabilitarBotonLupa (document.getElementById("consultar_11"));
	      	}
	    } //habilitarAsist ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Precio minimo de asistencias
	     * 
	     * LLAMADO POR:
	     * Check de minimo de asistencias
	     */
	    function habilitarMinAsist () {
	      	//Precio minimo de asistencias
	      	if (DefinirHitosFacturablesGuardiasForm.chMinAsist.checked)
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
	      	else
		        inhabilitarCuadroTexto (document.getElementById("hitoPrecio[10]"));
	    } //habilitarMinAsist ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Precio maximo de actuaciones
	     * Boton de tipos para precio maximo de actuaciones
	     * 
	     * LLAMADO POR:
	     * Check de maximo de actuaciones
	     */
	    function habilitarAct () {
	      	if (DefinirHitosFacturablesGuardiasForm.chActuacion.checked) {
	        	//Precio maximo de actuaciones
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
	        	//Boton de tipos para precio maximo de actuaciones
	        	if (DefinirHitosFacturablesGuardiasForm.chGuardias.checked)
	          		habilitarBotonLupa (document.getElementById("consultar_21"));
	        	else
	          		inhabilitarBotonLupa (document.getElementById("consultar_21"));
	        	
	      	} else {
	        	//Precio maximo de actuaciones
	        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[8]"));
	        	//Boton de tipos para precio maximo de actuaciones
	        	inhabilitarBotonLupa (document.getElementById("consultar_21"));
	      	}
	    } //habilitarAct ()
    
	    /**
	     * CONTROLES QUE SE COMPRUEBAN EN ESTA FUNCION:
	     *
	     * Precio minimo de actuaciones
	     * 
	     * LLAMADO POR:
	     * Check de minimo de actuaciones
	     */
	    function habilitarMinAct () {
	      	//Precio minimo de actuaciones
	      	if (DefinirHitosFacturablesGuardiasForm.chMinActuacion.checked)
	        	habilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
	      	else
	        	inhabilitarCuadroTexto (document.getElementById("hitoPrecio[19]"));
	    } //habilitarMinAct ()
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
    
    	<div id="scrollValores" style="height:100%; width:100%; overflow-y: auto; overflow-x: hidden; border: white">    	
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
      
      		<table width="100%" border="1" cellspacing="0" cellpadding="0">
        		<!-- Titulos de las columnas -->
        		<tr>
          			<td colspan="2" rowspan="4">&nbsp;</td>
          			
          			<td colspan="2" class="labelText" style="text-align:center" nowrap>
          				<siga:Idioma key="gratuita.confGuardia.literal.guardia"/>
					</td>          			
          			
          			<td rowspan="4">	
            			<table width="100%" border="0" cellspacing="0" cellpadding="0">
              				<tr>
              					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
                					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkC" property="checkC" onclick="initC();"/>
                					<siga:Idioma key="gratuita.confGuardia.literal.fueraguardia"/>
              					</td>
              				</tr>
              				<tr>
              					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>                      				
                					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chNoGuardias" property="chNoGuardias" onclick="aplicaFueraGuardias()"/>
                					<siga:Idioma key="gratuita.confGuardia.literal.aplicartipos"/>
              					</td>
              				</tr>
            			</table>
          			</td>
        		</tr>
        		
				<tr>		
					<td class="labelText" style="text-align:left; vertical-align:top" nowrap> 
     					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkB1" property="checkB1" value="0" onclick="cambiarCheckB1 ();" />
     					<siga:Idioma key="gratuita.confGuardia.literal.pagaguardia"/>
   					</td>
   					   
   					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
    						<tr>
			         			<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
									<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="checkB2" property="checkB2" value="1" onclick="cambiarCheckB2 ();" />
		                 			<siga:Idioma key="gratuita.confGuardia.literal.nopagaguardia"/>
		                 		</td>
		                 	</tr>
		                 	<tr>
		                 		<td class="labelText" style="text-align:left; vertical-align:top" nowrap>                      						
		               				<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chGuardias" property="chGuardias" onclick="aplicaGuardias()"/>
		                			<siga:Idioma key="gratuita.confGuardia.literal.aplicartipos"/>
		                		</td>
		                	</tr>
		                </table>
                    </td>
				</tr>
                   
                <tr>
                	<td>
	                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Lunes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Martes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Miercoles"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Jueves"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Viernes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Sabado"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Domingo"/></td>
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
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Lunes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Martes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Miercoles"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Jueves"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Viernes"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Sabado"/></td>
								<td class="labelText" style="text-align:center"><siga:Idioma key="gratuita.checkbox.literal.Domingo"/></td>
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
                
                <tr>
                	<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
                  		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chPagaGuardiaPorDia" property="chPagaGuardiaPorDia"/>
                  		<siga:Idioma key="fcs.criteriosFacturacion.guardia.porDia"/>
                	</td>          

					<td class="labelText" style="text-align:left; vertical-align:top" nowrap>
      					<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chNoPagaGuardiaPorDia"/>
      					<siga:Idioma key="fcs.criteriosFacturacion.guardia.porDia"/>
    				</td>                    		
          		</tr>
       
        		<!-- Primera linea -->
        		<tr>
          			<td width="80px">
            			&nbsp;
          			</td>
          			<td class="labelText" style="text-align:left; vertical-align:middle" width="100%">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.diaGuardia"/>
          			</td>		
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<html:text name="DefinirHitosFacturablesGuardiasForm" styleId="hitoPrecio[1]"
	                       property="hitoPrecio[1]" maxlength="10" size="10"
	                       onkeypress="filterChars(this,false,true);"
	                       onkeyup="filterCharsUp(this);"
	                       onblur="filterCharsNaN(this);" styleClass="box" />
            			&euro;
          			</td>
          			
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
          			</td>
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
          			</td>
        		</tr>
        
	        	<!-- Segunda linea -->
	        	<tr>
	          		<td class="labelText" style="text-align:left" rowspan="5">
	            		<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioA" styleId="radioA" value="0" onclick="initAB();"/>
	            		<br>
	            		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.porAsistencias"/>
	          		</td>
	          		
	                <td class="labelText" style="text-align:left; vertical-align:middle">
	                  <siga:Idioma key="fcs.criteriosFacturacion.asistencia.guardiaSimpleDoblada"/>
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<html:text name="DefinirHitosFacturablesGuardiasForm"
							property="hitoPrecio[2]" styleId="hitoPrecio[2]" maxlength="10" size="10"
							onkeypress="filterChars(this,false,true);"
							onkeyup="filterCharsUp(this);"
							onblur="filterCharsNaN(this);" styleClass="box" />
	                  	&euro;
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	           		</td>
				</tr>
              
				<!-- Tercera linea -->
	            <tr>
					<td class="labelText" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<html:text name="DefinirHitosFacturablesGuardiasForm"
		                    property="hitoPrecio[5]" styleId="hitoPrecio[5]" maxlength="10" size="10"
		                    onkeypress="filterChars(this,false,true);"
		                    onkeyup="filterCharsUp(this);"
		                    onblur="filterCharsNaN(this);" styleClass="box" />
		                  &euro;&nbsp;
	                  <img src="/SIGA/html/imagenes/bconsultar_disable.gif"
	                       name="consultar_1" id="consultar_10" alt="Consultar"
	                       onclick="consultarAsist(0)" onMouseOut="" onMouseOver=""
	                       border="0" style="cursor:default; vertical-align:middle"
	                       disabled>
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	       		</tr>
              
	            <!-- Cuarta linea -->
	            <tr>
	            	<td class="labelText" style="text-align:left; vertical-align:middle">
	                	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.guardiaDobladaPorAsist"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  >
	                  <html:text name="DefinirHitosFacturablesGuardiasForm"
		                  property="hitoPrecio[45]" maxlength="3" size="3"
		                  onkeypress="filterChars(this,false,true);"
		                  onkeyup="filterCharsUp(this);"
		                  onblur="filterCharsNaN(this);"
		                  styleClass="box"/>
	                </td>
	                
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
				</tr>
              
	            <!-- Quinta linea -->
	            <tr>
	            	<td class="labelText" style="text-align:left; vertical-align:middle">
	                	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.maxAsistencia"/>
	                  	&nbsp;
	                  	<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chAsist" onclick="habilitarAsist();"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<html:text name="DefinirHitosFacturablesGuardiasForm"
		                   	property="hitoPrecio[3]" maxlength="10" size="10"
		                   	onkeypress="filterChars(this,false,true);"
		                   	onkeyup="filterCharsUp(this);"
							onblur="filterCharsNaN(this);" styleClass="box" />
	                  	&euro;&nbsp;
	                  	<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
		                  	name="consultar_1" id="consultar_11" alt="Consultar"
		                  	onclick="consultarAsist(1)" onMouseOut="" onMouseOver=""
		                  	border="0" style="cursor:default; vertical-align:middle"
		                  	disabled>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
				</tr>
              
	            <!-- Sexta linea -->
	            <tr>
	            	<td class="labelText" style="text-align:left; vertical-align:middle">
	                	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.minAsist"/>
	                  	&nbsp;
	                  	<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chMinAsist" onclick="habilitarMinAsist();"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  <siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
						<html:text name="DefinirHitosFacturablesGuardiasForm"
							property="hitoPrecio[10]" maxlength="10" size="10"
							onkeypress="filterChars(this,false,true);"
							onkeyup="filterCharsUp(this);"
							onblur="filterCharsNaN(this);" styleClass="box" />
	                  	&euro;
	                </td>
	                <td class="labelTextValue" style="text-align:left; vertical-align:middle">
	                  	<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
	                </td>
				</tr>
        
        		<!-- Septima linea -->
        		<tr>
          			<td class="labelText" style="text-align:left" rowspan="5">
            			<html:radio name="DefinirHitosFacturablesGuardiasForm" property="radioA" value="1" onclick="initAB();"/>
            			<br>
            			<siga:Idioma key="fcs.criteriosFacturacion.actuacion.porActuaciones"/>
          			</td>
                	<td class="labelText" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.guardiaSimpleDoblada"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[4]" maxlength="10" size="10"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);" styleClass="box" />
                  		&euro;
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
               		</td>
              	</tr>
              
              	<!-- Octava linea -->
              	<tr>
                	<td class="labelText" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.actuaciones"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[7]" maxlength="10" size="10"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);" styleClass="box" />
                  		&euro;&nbsp;
                  		<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
                       		name="consultar_1" id="consultar_20" alt="Consultar"
                       		onclick="consultarAct(0)" onMouseOut="" onMouseOver=""
                       		border="0" style="cursor:default; vertical-align:middle"
                       		disabled>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[9]" maxlength="10" size="10"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);" styleClass="box" />
                  		&euro;&nbsp;
                  		<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
                       		name="consultar_1" id="consultar_30" alt="Consultar"
                       		onclick="consultarAct(0)" onMouseOut="" onMouseOver=""
                       		border="0" style="cursor:default; vertical-align:middle"
                       		disabled>
                	</td>
              	</tr>
              
              	<!-- Novena linea -->
              	<tr>
                	<td class="labelText" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.guardiaDobladaPorAct"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		>
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[46]" maxlength="3" size="3"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);"
                             styleClass="box"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
              	</tr>
              
              	<!-- Decima linea -->
              	<tr>
                	<td class="labelText" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.maxActuacion"/>
                    	&nbsp;
                    	<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chActuacion" onclick="habilitarAct();"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[8]" maxlength="10" size="10"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);" styleClass="box" />
                  		&euro;&nbsp;
                  		<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
                       		name="consultar_1" id="consultar_21" alt="Consultar"
                       		onclick="consultarAct(1)" onMouseOut="" onMouseOver=""
                       		border="0" style="cursor:default; vertical-align:middle"
                       		disabled>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
              	</tr>
              
              	<!-- Undecima linea -->
              	<tr>
                	<td class="labelText" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.minAct"/>
                  		&nbsp;
                  		<html:checkbox name="DefinirHitosFacturablesGuardiasForm" property="chMinActuacion" onclick="habilitarMinAct();"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<html:text name="DefinirHitosFacturablesGuardiasForm"
                             property="hitoPrecio[19]" maxlength="10" size="10"
                             onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"
                             onblur="filterCharsNaN(this);" styleClass="box" />
                  		&euro;
                	</td>
                	<td class="labelTextValue" style="text-align:left; vertical-align:middle">
                  		<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
                	</td>
              	</tr>
        
        		<!-- Duodecima linea -->
        		<tr>
          			<td>
            			&nbsp;
          			</td>
          			<td class="labelText" style="text-align:left; vertical-align:middle">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.maxActuacionFueraGuardia"/>
            			&nbsp;
            			<html:checkbox name="DefinirHitosFacturablesGuardiasForm" styleId="chActFG" property="chActFG" onclick="habilitarFG();"/>
          			</td>
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
          			</td>
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<siga:Idioma key="fcs.criteriosFacturacion.asistencia.noAplica"/>
          			</td>
          			<td class="labelTextValue" style="text-align:left; vertical-align:middle">
            			<html:text name="DefinirHitosFacturablesGuardiasForm"
                       		property="hitoPrecio[6]" maxlength="10" size="10"
                       		onkeypress="filterChars(this,false,true);"
                       		onkeyup="filterCharsUp(this);"
                       		onblur="filterCharsNaN(this);" styleClass="box" />
            			&euro;&nbsp;
            			<img src="/SIGA/html/imagenes/bconsultar_disable.gif"
                 			name="consultar_1" id="consultar_31" alt="Consultar"
                 			onclick="consultarAct(1)" onMouseOut="" onMouseOver=""
                 			border="0" style="cursor:default; vertical-align:middle"
                 			disabled>
          			</td>
        		</tr>
      		</table>
   		</div>  
   		
   		<div id="divFacturacionControlada">
   			<siga:ConjCampos leyenda="fcs.criteriosFacturacion.facturacionGuardias">
	   			<table width="100%" border="0" cellspacing="0" cellpadding="0">   			
					<tr class="filaTablaImpar">
						<td class="labelText">
							<input id="checkControlado" name="checkControlado" TYPE="radio" VALUE="1" onchange="gestionarControlado(this.value, false);" <%if(checkControlado.equals("1")){%>checked<%}%>>
							<siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAs"/>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsImporte" class="box" type="text" size="10" value="" 
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;
						</td>
					</tr>
					
					
					<tr class="filaTablaPar">
						<td class="labelText" rowspan="2">
							<input id="checkControlado" name="checkControlado" TYPE="radio" VALUE="2" onchange="gestionarControlado(this.value);" <%if(checkControlado.equals("2")){%>checked<%}%>>
							<siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAsMin"/>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsMinImporte" class="box" type="text" size="10" value="" 
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;
						</td>
					</tr>
					<tr class="filaTablaPar">
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.minAsist"/></td>
						<td class="labelTextValue">
							<input id="AsMinMinimo" class="box" type="text" size="10" value="" 
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;
						</td>				
					</tr>			
					
					
					<tr class="filaTablaImpar">
						<td class="labelText" rowspan="2">
							<input id="checkControlado" name="checkControlado" TYPE="radio" VALUE="3" onchange="gestionarControlado(this.value);" <%if(checkControlado.equals("3")){%>checked<%}%>>
							<siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaAsTpMin"/>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.asistencias"/></td>
						<td class="labelTextValue">
							<input id="AsTpImporte" class="box" type="text" size="10" value="" 
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;&nbsp;
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
							<input id="AsTpMinimo" class="box" type="text" size="10" value="" 
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;
						</td>
					</tr>		
					
					
					<tr class="filaTablaPar">
						<td class="labelText">
							<input id="checkControlado" name="checkControlado" TYPE="radio" VALUE="4" onchange="gestionarControlado(this.value);" <%if(checkControlado.equals("4")){%>checked<%}%>>
							<siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.controladaGAs"/>
						</td>
						<td class="labelText"><siga:Idioma key="fcs.criteriosFacturacion.asistencia.diaGuardia"/></td>
						<td class="labelTextValue">
							<input id="GAsImporte" class="box" type="text" size="10" value=""
								onkeypress="filterChars(this,false,true);"
	                       		onkeyup="filterCharsUp(this);"
	                       		onblur="filterCharsNaN(this);"/>&euro;
						</td>
					</tr>
										
										
					<tr class="filaTablaImpar">
						<td class="labelText" colspan="3">							
							<input id="checkControlado" name="checkControlado" TYPE="radio" VALUE="5" onchange="gestionarControlado(this.value);" <%if(checkControlado.equals("5")){%>checked<%}%>>
							<siga:Idioma key="fcs.criteriosFacturacion.facturacionGuardias.noControlada"/>
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
						<html:text name="DefinirHitosFacturablesGuardiasForm"
							property="hitoPrecio[13]" maxlength="10" size="10"
							onkeypress="filterChars(this,false,true);"
							onkeyup="filterCharsUp(this);"
							onblur="filterCharsNaN(this);" styleClass="box" />&euro;
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="fcs.criteriosFacturacion.asistencia.expedienteSOJ"/>
					</td>
					
					<td class="labelTextValue">	
            			<html:text name="DefinirHitosFacturablesGuardiasForm"
                       		property="hitoPrecio[12]" maxlength="10" size="10"
                       		onkeypress="filterChars(this,false,true);"
                       		onkeyup="filterCharsUp(this);"
                       		onblur="filterCharsNaN(this);" styleClass="box" />&euro;
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