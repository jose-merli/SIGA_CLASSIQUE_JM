<!-- comboAutoComplete.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% 
	String app=request.getContextPath();

	String nombre = request.getParameter("nombre");
	if (nombre == null) 
		nombre = "nombre";
	
	String ancho = request.getParameter("ancho");
	if (ancho == null) 
		ancho = "300";	
	
	String valorCombo = request.getParameter("valorCombo");
	if (valorCombo == null) 
		valorCombo = "";	
	
	String condicion = request.getParameter("condicion");
	if (condicion == null) 
		condicion = "false";	
	
	String idPadre = request.getParameter("idPadre");
	if (idPadre == null) 
		idPadre = "";
	
	String numeroGuiones = request.getParameter("numeroGuiones");
	if (numeroGuiones == null) 
		numeroGuiones = "0";
	
	String numeroLineasCombo = request.getParameter("numeroLineasCombo");
	if (numeroLineasCombo == null) 
		numeroLineasCombo = "0";
	
	String numeroMaximoOpciones = request.getParameter("numeroMaximoOpciones");
	if (numeroMaximoOpciones == null) 
		numeroMaximoOpciones = "0";
	
	String url = request.getParameter("url");
	if (url == null) 
		url = "";	
%>

<html>
	<head>
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>		
		
		<script type="text/javascript" src="<%=app%>/html/js/SIGA.js" ></script>
		<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
		<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	</head>

	<body style="background-color: transparent;">
		<input class="box" name="<%=nombre%>_input" type="text" style="width:<%=ancho%>px;" value="" 
			onblur="onBlurFiltro();" onkeydown="onKeyFiltro(event);" onkeyup="onKeyUpFiltro();" 
			onfocus="onFocusFiltro();">
		<div id="<%=nombre%>_div">
			<select class="box" style="width:<%=ancho%>px" id="<%=nombre%>_select" 
				onblur="onBlurCombo();" onchange="seleccionaCombo();" onclick="onClickCombo();" 
				onkeypress="onKeyPressCombo(event);" onkeydown="onKeyCombo(event);" onmousedown="onMouseCombo();">																		
			</select>					
		</div>				
	</body>
</html>

<script>
	var bControl=false;
	var bControlKey=false;
	var bFocoCombo=false;
	var bFocoFiltro=true;
	var controlFiltro=cteControl; 
	var cteControl="controlFiltro";
	var cteSeleccionar="<siga:Idioma key='general.combo.seleccionar'/>";
	var msgControl="";
	var numOpciones=1;
	var txtTabulador="";
	
	var idPadre="";
	var numeroLineasCombo=20;
	var numeroMaximoOpciones=1000;
	var url="";
	var valorGuiones="";	
	
	// Funcion para cargar el autocomplete 
	function cargaInicial (valorCombo, condicion, idPadreAux, numeroLineasComboAux, numeroMaximoOpcionesAux, urlAux, numeroGuiones) {		
		//msgControl=msgControl+"cargaInicial("+valorCombo+","+condicion+","+idPadreAux+","+numeroLineasComboAux+","+numeroMaximoOpcionesAux+","+urlAux+","+numeroGuiones+")\n";alert(msgControl);
		
		if (idPadreAux!="")
			idPadre=idPadreAux;
		
		if (numeroLineasComboAux>0)
			numeroLineasCombo=numeroLineasComboAux;		
		
		if (numeroMaximoOpcionesAux>0)
			numeroMaximoOpciones=numeroMaximoOpcionesAux;			
		
		if (urlAux!="")
			url=urlAux;
		
		if (numeroGuiones>0) {
			valorGuiones="";
			while(valorGuiones.length<numeroGuiones) {
				valorGuiones=valorGuiones+"-";
			}
		}
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var elementoDiv = jQuery("#<%=nombre%>_div");
		var elementoCombo = jQuery("#<%=nombre%>_select");

		elementoDiv.hide();  		

		// Carga datos y actualizo el combo segun el contenido del filtro
		if (condicion) {
			elementoFiltro.value=valorCombo;
			actualizarCombo(valorCombo, false);
		}				
	}	
	
	// Funcion para recuperar el valor del combo en el hidden
	function recuperaValorCombo() {
		//msgControl=msgControl+"recuperaValorCombo()\n";alert(msgControl);
		
		//Obtiene los elementos que vamos a utilizar
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		// Comprueba si tiene algun elemento selecionado y lo carga en hidden
		var optionSelected=elementoCombo[0].selectedIndex;		
			
		if (optionSelected<0)
			return "";
		else
			return elementoCombo.val();									
	}	
	
	// Selecciona un elemento combo padre
	function seleccionaComboPadre() {
		//msgControl=msgControl+"seleccionaComboPadre()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		// Recarga los datos del autocomplete
		elementoFiltro.value=cteSeleccionar;
		elementoCombo[0].innerHTML="";
		controlFiltro=cteControl;
	}	   		
	
	// Selecciona un elemento del combo
	function seleccionaCombo() {
		//msgControl=msgControl+"seleccionaCombo()\n";alert(msgControl);	
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		// Vamos a mostrar el elemento seleccionado del combo en el filtro
		var optionSelected=elementoCombo[0].selectedIndex;
		
		// 0: Si ha seleccionado el elemento "--Seleccionar"
		if (optionSelected<0) // -1: Si no hay nada seleccionado
			elementoFiltro.value=cteSeleccionar;
			
		else {
			var textoCombo=elementoCombo[0].options[optionSelected].text;
			if (textoCombo==valorGuiones)
				textoCombo=cteSeleccionar;	
			
			if (elementoFiltro.value!=textoCombo)
				elementoFiltro.value=textoCombo;						
		}		
	}
	
	// Pulsa una opcion del combo
	function onClickCombo() {
		//msgControl=msgControl+"onClickCombo()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		
		// un control necesario y pongo el foco en el filtro
		bControl=true;
		elementoFiltro.focus(); //Invoca onBlurCombo() + onFocusFiltro()		 
	}
	
	// Este método es necesario para cuando va a realizar una accion en el combo sin tener el foco
	function onMouseCombo() {
		//msgControl=msgControl+"onMouseCombo()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		if (!bFocoCombo) {			
			bFocoCombo=true;		
			bControl=true;
			elementoCombo.focus(); //Invoca onBlurFiltro()
		}
	}		
	
	// Se realiza cuando abandona el foco del combo
	function onBlurCombo() {	
		//msgControl=msgControl+"onBlurCombo()\n";alert(msgControl);		
		
		// Obtiene los elementos que vamos a utilizar
		var elementoDiv = jQuery("#<%=nombre%>_div");
		
		// Oculto el combo y recalculo el valor del filtro
		bFocoCombo=false;
		elementoDiv.hide();
		seleccionaCombo();
	}	
	
	// Se realiza cuando pulsa una letra en el combo
	function onKeyCombo(e) {
		var tecla = (document.all) ? e.keyCode : e.which;
		//msgControl=msgControl+"onKeyCombo("+tecla+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		switch(tecla) {		
	  		case 8: //BACKSPACE: Anulamos la acción del retroceso en el combo
	  			e.returnValue = false; 
	  			e.cancelBubble = true; 
	  			return false;		  		
	  			break;
		
			case 13: //INTRO: Paso el foco al filtro
				bControl=true;				
				elementoFiltro.focus(); //Invoca onBlurCombo() + onFocusFiltro()
				break;
				
			case 27: //ESC: Paso el foco al filtro
				elementoCombo.val([]); // Elimino los elementos seleccionados del combo
				
				bControl=true;				
				elementoFiltro.focus(); //Invoca onBlurCombo() + onFocusFiltro()
				break;							
  		}		
	}
	
	// Necesario para firefox
	function onKeyPressCombo(e) {
		var tecla = (document.all) ? e.keyCode : e.which; 
		//msgControl=msgControl+"onKeyCombo("+tecla+")\n";alert(msgControl);
		
		switch(tecla) {			
		  	case 8: //BACKSPACE: Anulamos la acción del retroceso en el combo
		  		e.returnValue = false; 
		  		e.cancelBubble = true; 
		  		return false;		  		
  				break;
  		}		
	}									
	
	// Se realiza cuando obtiene el foco del filtro
	function onFocusFiltro() {
		//msgControl=msgControl+"onFocusFiltro()\n";alert(msgControl);
		bFocoFiltro=true;
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
				
		// Si tengo el texto de selección, lo pongo en blanco
		if (elementoFiltro.value==cteSeleccionar)
			elementoFiltro.value="";
		
		// Un control y actualizo el combo segun el contenido del filtro
		if (bControl)
			bControl=false;
		else
			actualizarCombo(elementoFiltro.value, true);
	}			
		
	// Se realiza cuando abandona el foco del filtro
	function onBlurFiltro() {
		//msgControl=msgControl+"onBlurFiltro()\n";alert(msgControl);
		bFocoFiltro=false;
		
		// Obtiene los elementos que vamos a utilizar
		var elementoDiv = jQuery("#<%=nombre%>_div");
		
		// Un control, ocultro el filtro y calculo el valor final del filtro
		if (bControl)
			bControl=false;
			
		else {
			elementoDiv.hide();
			seleccionaCombo();						
		}
	}
	
	// Se realiza cuando termina de cargar la letra en el filtro
	function onKeyUpFiltro() {
		//msgControl=msgControl+"onKeyUpFiltro()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var valorFiltro = elementoFiltro.value;
		
		// Un control y actualizo el combo segun el contenido del filtro
		if (bControlKey) {
			bControlKey=false;
			setTimeout(function(){controlarFiltro(valorFiltro);},1000,"Javascript");
		}
	}	
	
	function onKeyFiltro(e) {		
		var tecla = (document.all) ? e.keyCode : e.which;
		//msgControl=msgControl+"onKeyFiltro("+tecla+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		var elementoDiv = jQuery("#<%=nombre%>_div");
		var elementoCombo = jQuery("#<%=nombre%>_select");
		
		txtTabulador="";
		
		switch(tecla) {		  				
  			case 9: //TABULADOR
  				txtTabulador=elementoFiltro.value;
  				elementoDiv.hide();   				
  				break;  
  				
			case 13: //INTRO: Paso el foco al filtro
				elementoDiv.hide();
				break;  				
  				
			case 27: //ESC: Paso el foco al filtro
				elementoCombo.val([]); // Elimino los elementos seleccionados del combo
				elementoDiv.hide();
				elementoFiltro.value="";
				break;  
				
			case 38: //CURSOR ARRIBA
			case 40: //CURSOR ABAJO
				actualizarCombo(elementoFiltro.value, true);
				if (numOpciones>1) {					
					bFocoCombo=true;		
					bControl=true;
					elementoCombo.focus(); //Invoca onBlurFiltro()
				}
  				break;					
  				
  			default:
  				bControlKey=true;
  				break;				
  		}
	}		
	
	function controlarFiltro(filtroAntiguo) {
		//msgControl=msgControl+"controlFiltro("+filtroAntiguo+")\n";alert(msgControl);

		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById("<%=nombre%>_input");
		
		if (filtroAntiguo==elementoFiltro.value || filtroAntiguo==txtTabulador) {
			actualizarCombo(filtroAntiguo, true);
		}
	}		
			 	
	// Actualizo el combo segun el contenido del filtro
	function actualizarCombo(valorFiltro, bMuestraDiv){
		//msgControl=msgControl+"actualizarCombo("+valorFiltro+","+bMuestraDiv+")\n";alert(msgControl);
		
		// Controlo si tiene padre
		if (idPadre!="") {
			var elementoPadre = parent.document.getElementById(idPadre);
		 	var codigoPadre = elementoPadre.value;		 	
		}		
		
		if (controlFiltro!=valorFiltro) {			
			controlFiltro=valorFiltro;								
	 		
			jQuery.ajax({ 
				type: "POST",
				url: url,				
				data: "codigoPadre="+codigoPadre+"&valorFiltro="+valorFiltro+"&valorGuiones="+valorGuiones+"&numeroMaximoOpciones="+numeroMaximoOpciones,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){					
						
					// Con esta comparacion y por hacer AJAX, nos evitamos hacer codigo
					if (valorFiltro==controlFiltro) {					
						//msgControl=msgControl+"Paso01("+valorFiltro+","+controlFiltro+","+elementoFiltro.value+")\n";alert(msgControl);
						
						// Recupera el numero de optiones
						numOpciones = json.numOptions;	
						
						var elementoDiv = jQuery("#<%=nombre%>_div");						
						
						// Pinta el combo con el contenido devuelto por ajax
						var htmlFinal=elementoDiv[0].innerHTML;
						htmlFinal.replace("\"","'");
						htmlFinal=htmlFinal.substring(0, htmlFinal.indexOf('>')+1);
						elementoDiv[0].innerHTML=htmlFinal+json.htmlOptions[0]+"</SELECT>";
						
						var elementoCombo = jQuery("#<%=nombre%>_select");
    	        	    
			   	    	// Si solo tiene el elemento de seleccion, oculta el combo
		        		if (numOpciones<2) {
		        			elementoDiv.hide();
	        	
		       			} else {    	    		      
		       				// Cargo el numero de lineas que tiene que mostrar el combo
    	        			if (numOpciones>numeroLineasCombo)
    	        				elementoCombo[0].size=numeroLineasCombo;
    	        			else
    	        				elementoCombo[0].size=numOpciones;    	        				
		    			
    	        			// Si solo hay un elemento, quitando el de seleccion, lo selecciono
		    				if (numOpciones==2)
		    					elementoCombo[0].options[1].selected=true;		    					
		    				
		    				// Control de si se debe mostrar el combo
	    					if (bMuestraDiv&&bFocoFiltro)
	    						elementoDiv.show();
    					}   		    	
		        		
						// Controlo que haya perdido el foltro el filtro y no sea la carga inicial
						if (!bFocoFiltro&&bMuestraDiv)
							seleccionaCombo();
					}				    	       		       				       				    		
					fin();
				}
			});
			
		// Si no ha cambiado el filtro, lo muestro cuando tenga elementos
		} else {
			if (numOpciones>1&&bMuestraDiv) {
				var elementoDiv = jQuery("#<%=nombre%>_div");	
				elementoDiv.show();
			}
		} 
	}		
	
	cargaInicial ("<%=valorCombo%>", <%=condicion%>, "<%=idPadre%>", <%=numeroLineasCombo%>, <%=numeroMaximoOpciones%>, "<%=url%>", <%=numeroGuiones%>);
</script>
