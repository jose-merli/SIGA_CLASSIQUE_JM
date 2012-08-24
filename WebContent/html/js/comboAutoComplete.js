	
	var bControl=false;
	var bControlKey=false;
	var bFocoCombo=false;
	var bFocoFiltro=true;
	var controlFiltro=cteControl; 
	var cteControl="controlFiltro";
	var cteSeleccionar="--Seleccionar";
	var msgControl="";
	var numOpciones=1;
	
	var idFiltro=""; 
	var idDiv=""; 
	var idCombo=""; 
	var idHidden=""; 
	var idPadre="";
	var idNext="";
	var numeroLineasCombo=1;
	var numeroMaximoOpciones=1;
	var url="";
	var valorGuiones="";
	
	// Carga los id´s del autocomplete
	function loadAutocomplete(idFiltroAux, idDivAux, idComboAux, idHiddenAux, idPadreAux, idNextAux, urlAux, numeroLineasComboAux, numeroMaximoOpcionesAux, numeroGuiones, cteSeleccionarAux) {
		//msgControl=msgControl+"loadAutocomplete("+idFiltroAux+","+idDivAux+","+idComboAux+","+idHiddenAux+","+idPadreAux+","+idNextAux+","+urlAux+","+numeroLineasComboAux+","+numeroMaximoOpcionesAux+","+numeroGuiones+","+cteSeleccionarAux+")\n";alert(msgControl);
		
		if (idFiltroAux!=null&&idFiltroAux!="")
			idFiltro=idFiltroAux;
		
		if (idDivAux!=null&&idDivAux!="")
			idDiv=idDivAux;
		
		if (idComboAux!=null&&idComboAux!="")
			idCombo=idComboAux;
		
		if (idHiddenAux!=null&&idHiddenAux!="")
			idHidden=idHiddenAux;
		
		if (idPadreAux!=null&&idPadreAux!="")
			idPadre=idPadreAux;
		
		if (idNextAux!=null&&idNextAux!="")
			idNext=idNextAux;
		
		if (urlAux!=null&&urlAux!="")
			url=urlAux;
		
		if (numeroLineasComboAux!=null&&numeroLineasComboAux>0)
			numeroLineasCombo=numeroLineasComboAux;		
		
		if (numeroMaximoOpcionesAux!=null&&numeroMaximoOpcionesAux>0)
			numeroMaximoOpciones=numeroMaximoOpcionesAux;	
		
		if (numeroGuiones!=null&&numeroGuiones>0) {
			valorGuiones="";
			while(valorGuiones.length<numeroGuiones) {
				valorGuiones=valorGuiones+"-";
			}
		}
		
		if (cteSeleccionarAux!=null&&cteSeleccionarAux!="")
			cteSeleccionar=cteSeleccionarAux;
	}
	
	// Funcion para cargar el autocomplete 
	function cargaInicial (valorCombo, condicion) {		
		//msgControl=msgControl+"cargaInicial("+valorCombo+","+condicion+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoDiv = jQuery("#"+idDiv);
		var elementoCombo = jQuery("#"+idCombo);

		// Posiciona el combo y el div
		var pos = elementoCombo.offset();             
		elementoCombo.css("position", "absolute");             
		elementoCombo.css("zIndex", 9999);             
		elementoCombo.offset(pos); 	
		elementoDiv.hide();  		

		// Carga datos y actualizo el combo segun el contenido del filtro
		if (condicion) {
			elementoFiltro.value=valorCombo;
			actualizarCombo(elementoFiltro.value, false);
		}				
	}	
	
	// Funcion para recuperar el valor del combo en el hidden
	function recuperaValorCombo() {
		//msgControl=msgControl+"recuperaValorCombo()\n";alert(msgControl);	
		
		//Obtiene los elementos que vamos a utilizar
		var elementoCombo = jQuery("#"+idCombo);
		var elementoOculto = document.getElementById(idHidden);
		
		// Comprueba si tiene algun elemento selecionado y lo carga en hidden
		var optionSelected=elementoCombo[0].selectedIndex;		
			
		if (optionSelected<0)
			elementoOculto.value="";
		else
			elementoOculto.value = elementoCombo.val();									
	}	
	
	// Selecciona un elemento combo padre
	function seleccionaComboPadre() {
		//msgControl=msgControl+"seleccionaComboPadre()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoCombo = jQuery("#"+idCombo);
		
		// Recarga los datos del autocomplete
		elementoFiltro.value=cteSeleccionar;
		elementoCombo[0].innerHTML="";
		controlFiltro=cteControl;
	}	   		
	
	// Selecciona un elemento del combo
	function seleccionaCombo() {
		//msgControl=msgControl+"seleccionaCombo()\n";alert(msgControl);	
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoCombo = jQuery("#"+idCombo);
		
		// Vamos a mostrar el elemento seleccionado del combo en el filtro
		var optionSelected=elementoCombo[0].selectedIndex;//var optionSelected=elementoCombo.prop("selectedIndex");
		
		// 0: Si ha seleccionado el elemento "--Seleccionar"
		if (optionSelected<0) // -1: Si no hay nada seleccionado
			elementoFiltro.value=cteSeleccionar;
			
		else {
			var textoCombo=elementoCombo[0].options[optionSelected].text;//var textoCombo=jQuery("#"+idCombo+" option:selected").text();
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
		var elementoFiltro = document.getElementById(idFiltro);
		
		// un control necesario y pongo el foco en el filtro
		bControl=true;
		elementoFiltro.focus(); //Invoca onBlurCombo() + onFocusFiltro()		 
	}
	
	// Este método es necesario para cuando va a realizar una accion en el combo sin tener el foco
	function onMouseCombo() {
		//msgControl=msgControl+"onMouseCombo()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoCombo = jQuery("#"+idCombo);
		
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
		var elementoDiv = jQuery("#"+idDiv);
		
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
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoCombo = jQuery("#"+idCombo);
		
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
		var elementoFiltro = document.getElementById(idFiltro);
				
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
		var elementoDiv = jQuery("#"+idDiv);
		
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
		var elementoFiltro = document.getElementById(idFiltro);
		
		// Un control y actualizo el combo segun el contenido del filtro
		if (bControlKey) {
			bControlKey=false;
			actualizarCombo(elementoFiltro.value, true);
		}
	}		
	
	function onKeyFiltro(e) {		
		var tecla = (document.all) ? e.keyCode : e.which;
		//msgControl=msgControl+"onKeyFiltro("+tecla+")\n";alert(msgControl);	
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoDiv = jQuery("#"+idDiv);
		var elementoCombo = jQuery("#"+idCombo);
		var elementoNext = jQuery("#"+idNext);
		
		switch(tecla) {		  				
  			case 9: //TABULADOR
  				elementoDiv.hide();
  				elementoNext.focus(); //Invoca onBlurFiltro()  				
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
			 	
	// Actualizo el combo segun el contenido del filtro
	function actualizarCombo(valorFiltro, bMuestraDiv){
		//msgControl=msgControl+"actualizarCombo("+valorFiltro+","+bMuestraDiv+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoFiltro = document.getElementById(idFiltro);
		var elementoDiv = jQuery("#"+idDiv);		
		
		// Controlo si tiene padre
		if (idPadre!="") {
			var elementoPadre = document.getElementById(idPadre);
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
						
						// Pinta el combo con el contenido devuelto por ajax
						var htmlFinal=elementoDiv[0].innerHTML;
						htmlFinal=htmlFinal.substring(0, htmlFinal.indexOf('>')+1);
						elementoDiv[0].innerHTML=htmlFinal+json.htmlOptions[0]+"</SELECT>";		
	                	        		
						// Recupera el numero de optiones
						numOpciones = json.numOptions;						
						
						var elementoCombo = jQuery("#"+idCombo);
    	        	    
			   	    	// Si solo tiene el elemento de seleccion, oculta el combo
		        		if (numOpciones<2) {
		        			elementoDiv.hide();
	        	
		       			} else {    	    		      
		       				// Cargo el numero de lineas que tiene que mostrar el combo
    	        			if (numOpciones>numeroLineasCombo)
    	        				elementoCombo[0].size=numeroLineasCombo;
    	        			else
    	        				elementoCombo[0].size=numOpciones;//elementoCombo.attr("size", numOpciones);    	        				
		    			
    	        			// Si solo hay un elemento, quitando el de seleccion, lo selecciono
		    				if (numOpciones==2)
		    					elementoCombo[0].options[1].selected=true;//jQuery("#"+idCombo+" option").eq(1).attr('selected', 'selected');		    					
		    				
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
			if (numOpciones>1&&bMuestraDiv)
				elementoDiv.show();
		} 
	}	