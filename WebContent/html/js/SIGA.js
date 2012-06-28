/*  METODO OBLIGATORIO EN EVENTO onLoad DE TODAS LAS PÁGINAS
	Inicia estilos para todas las páginas que importen este fichero
    Es necesario definir la hoja de estilos SIGA.css con el atributo ID="default" */


//Version nueva para los alrets

/* Alert para la nueva funcionalidad comentado temporalmente hasta que este depurado */
/*
function alert(message, title, acceptText) {
	var returnValue = true;
	if(message){
		if(!title){
			title = "Atención";
		}
		if(!acceptText){
			acceptText = "Aceptar";
		}
		var args = new Array(message,title, acceptText);
		returnValue = showModalDialog("/SIGA/html/jsp/general/alert.jsp",
				args,
				"dialogHeight:110px;dialogWidth:500px;dialogLeft:250px;dialogTop:250px;help:no;scroll:no;status:no;resizable:no;location:no");
		if(!returnValue){
			returnValue = true;
		}
		window.top.focus();
	}
	return returnValue;
}
*/

function alert(message, estilo) {
	var returnValue = true;
	var windowTop=window.top;
	message = message.replace(/\r\n|\r|\n/g, "<br>");
	windowTop.growl(message,estilo);
	return returnValue;
}
/*
function confirm(message, title, acceptText, cancelText) {
	var returnValue = false;
	if(message){
		if(!title){
			title = "Confirmación";
		}
		if(!acceptText){
			acceptText = "Aceptar";
		}
		if(!cancelText){
			cancelText = "Cancelar";
		}
		var args = new Array(message, title, acceptText, cancelText);
		returnValue = showModalDialog("/SIGA/html/jsp/general/confirm.jsp",
				args,
				"dialogHeight:110px;dialogWidth:500px;dialogLeft:250px;dialogTop:250px;help:no;scroll:no;status:no;resizable:no;location:no");
		window.top.focus();
	}
	return returnValue;
}
*/
/*  Hack for allowing correct typing in modal dialogs in safari. */
try {
	if(jQuery.browser.msie || jQuery.browser.mozilla){
		jQuery.browser.safari = false;
	} else {
		jQuery.browser.safari = ( jQuery.browser.webkit && /chrome/.test(navigator.userAgent.toLowerCase()) ) ? false : true;
	}
	if( (jQuery.browser.safari) && (window.top.dialogArguments) ){
		// Only applies to Windows Safari browsers
		if(/win32/.test(navigator.platform.toLowerCase())){
			jQuery(document).keydown(function(event) {
				// Backspace key works fine
				if(event.which == 8){
					return true;
				} else {
					return false;
				}
			});
		}
	}
} catch(e){}


//Disable right mouse click Script
var message = "Función Deshabilitada!";

var semaforoPestana = true;

function cuenta(obj, len) {
	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	if ((keycode != 8) && (keycode != 46)) {
		if (obj.value.length > len - 1)
			obj.value = obj.value.substring(0, len - 1);
	}
}

// /////////////////////////////////
function clickIE4() {
	if (event.button == 2) {
		// alert(message);
		return false;
	}
}

function clickNS4(e) {
	if (document.layers || document.getElementById && !document.all) {
		if (e.which == 2 || e.which == 3) {
			// alert(message);
			return false;
		}
	}
}

if (document.layers) {
	document.captureEvents(Event.MOUSEDOWN);
	document.onmousedown = clickNS4;
} else if (document.all && !document.getElementById) {
	document.onmousedown = clickIE4;
}

// document.oncontextmenu=new Function("alert(message);return false")

// -->*/

function initStyles() {
	var defStyle = document.styleSheets[0].href;
	var logo = document.getElementById("logoImg");

	var liob = 0;
	idx = -1;
	while ((idx = defStyle.indexOf('/', idx + 1)) != -1) {
		liob = idx;
	}
	var stylesPath = defStyle.substring(0, liob + 1);

	if (logo) {
		var imagesPath = logo.src;
		liob = 0;
		idx = -1;
		while ((idx = imagesPath.indexOf('/', idx + 1)) != -1) {
			liob = idx;
		}
		imagesPath = imagesPath.substring(0, liob + 1);
	}

	if (top.loc == 'MURCIA') {
		document.styleSheets[0].href = stylesPath + "murcia.css";
		if (logo) {
			logoImg.src = imagesPath + 'logoMurcia.gif';
		}
	} else if (top.loc == 'GIJON') {
		document.styleSheets[0].href = stylesPath + "gijon.css";
		if (logo) {
			logoImg.src = imagesPath + 'logoGijon.gif';
		}
	} else if (top.loc == 'MALAGA') {
		document.styleSheets[0].href = stylesPath + "malaga.css";
		if (logo) {
			logoImg.src = imagesPath + 'logoMalaga.gif';
		}
	} else if (top.loc == 'ZARAGOZA') {
		document.styleSheets[0].href = stylesPath + "zaragoza.css";
		if (logo) {
			logoImg.src = imagesPath + 'logoZaragoza.gif';
		}
	} else if (top.loc == 'MELILLA') {
		document.styleSheets[0].href = stylesPath + "melilla.css";
		if (logo) {
			logoImg.src = imagesPath + 'logoMelilla.gif';
		}
	}
}

/*
 * Al usar Javascript y referencias directas entre páginas HTML, el prototipo
 * necesita que los formularios sean de tipo GET, para poder realizar el paso de
 * parámetros entre distintos formularios abiertos en el mismo target. Este
 * método implementa la funcionalidad de obtener un array de dos dimensiones
 * (pares nombre/valor) conteniendo los parámetros que se pasan en una peticion
 * get.
 */
function getFormParameters() {
	var allArgs = new Array();
	var counter = 0;
	// el primer caracter es '?', y lo sustituímos por '&' para la busqueda de
	// parametros
	var argsString = '&' + document.location.search.substring(1);
	var idx = -1;
	while ((idx = argsString.indexOf('&', idx + 1)) != -1) {
		var endBlock = argsString.indexOf('&', idx + 1);
		var param = '';
		if (endBlock != -1)
			param = argsString.substring(idx + 1, endBlock);
		else
			param = argsString.substring(idx + 1);
		var paramName = param.substring(0, param.indexOf('='));
		var paramValue = param.substring(param.indexOf('=') + 1);
		allArgs[counter] = new Array();
		allArgs[counter][0] = paramName;
		allArgs[counter][1] = paramValue;
		counter++;
	}
	return allArgs;
}

function getParameterValue(sName) {
	var allParams = getFormParameters();
	var toReturn = '';
	for (i = 0; i < allParams.length; i++) {
		if (allParams[i][0] == sName) {
			toReturn = allParams[i][1];
			break;
		}
	}
	return toReturn;
}

/* Metodos para imagenes */

/*
 * TNF Función para dejar una imagen asociada a un link activada tras pulsar
 * sobre éste. El link no debe tener atributo class, y cualquier link en la
 * misma página que no lleve imagenes, debe tener algún valor para ese atributo
 */
function activateLink(objLink) {
	var links = document.getElementsByTagName('a');
	var linksToSwap = new Array();
	var j = 0;
	for (i = 0; i < links.length; i++) {
		var lnk = links[i];
		if (lnk.className)
			continue;
		if (lnk == objLink) {
			lnk.onmouseout = '';
		} else {
			lnk.onmouseout = new Function('MM_swapImgRestore()');
			linksToSwap[j] = lnk;
			j++;
		}
	}

	for (i = 0; i < linksToSwap.length; i++) {
		linksToSwap[i].onmouseover();
		linksToSwap[i].onmouseout();
	}
	objLink.onmouseover();
}

/**
 * Rellena el atributo value con los
 */
function rellenarFichaCliente(codCliente) {
	var cliente = clientes[new Number(codCliente)];
	if (document.all.numero)
		numero.value = cliente[1];
	if (document.all.nombre)
		nombre.value = cliente[2];
	if (document.all.apel1)
		apel1.value = cliente[3];
	if (document.all.apel2)
		apel2.value = cliente[4];
	if (document.all.NIF)
		NIF.value = cliente[5];
	if (document.all.genero)
		genero.value = cliente[10];
	if (document.all.fecNac)
		fecNac.value = cliente[11];
	if (document.all.lugarNac)
		lugarNac.value = cliente[12];
	if (document.all.NSS)
		NSS.value = cliente[13];
	if (document.all.EC)
		EC.value = cliente[14];
	if (document.all.fecPresenta)
		fecPresenta.value = cliente[16];
	if (document.all.fecIncorp)
		fecIncorp.value = cliente[17];
	if (document.all.fecJura)
		fecJura.value = cliente[16];
	if (document.all.diasColeg)
		diasColeg.value = cliente[17];
	if (document.all.tipoCuota)
		tipoCuota.value = cliente[16];

}

function MM_swapImgRestore() { // v3.0
	var i, x, a = document.MM_sr;
	for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
		x.src = x.oSrc;
}

function MM_preloadImages() { // v3.0
	var d = document;
	if (d.images) {
		if (!d.MM_p)
			d.MM_p = new Array();
		var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
		for (i = 0; i < a.length; i++)
			if (a[i].indexOf("#") != 0) {
				d.MM_p[j] = new Image;
				d.MM_p[j++].src = a[i];
			}
	}
}

function MM_findObj(n, d) { // v4.01
	var p, i, x;
	if (!d)
		d = document;
	if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if (!(x = d[n]) && d.all)
		x = d.all[n];
	for (i = 0; !x && i < d.forms.length; i++)
		x = d.forms[i][n];
	for (i = 0; !x && d.layers && i < d.layers.length; i++)
		x = MM_findObj(n, d.layers[i].document);
	if (!x && d.getElementById)
		x = d.getElementById(n);
	return x;
}

function MM_swapImage() { // v3.0
	var i, j = 0, x, a = MM_swapImage.arguments;
	document.MM_sr = new Array;
	for (i = 0; i < (a.length - 2); i += 3)
		if ((x = MM_findObj(a[i])) != null) {
			document.MM_sr[j++] = x;
			if (!x.oSrc)
				x.oSrc = x.src;
			x.src = a[i + 2];
		}
}

// METODOS PARA CAMBIAR EL TITULO
// Una o varias funciones aplicadas en cada página seran las encargadas
// de modificar la linea de navegacion y el titulo en la barra de titulo
// de la cabecera de las paginas. Esto es, en la pagina main de la aplicacion

function setTitulo(preTitulo, tit) {
	var titulo = tit;
	if (preTitulo != "") {
		titulo = preTitulo + " - " + tit;
	}
	top.document.title = titulo;

	var tdtitulo = top.document.getElementById("titulo");
	if (tdtitulo) {
		tdtitulo.innerHTML = tit;
	} else {
		// VENTANA MODAL
		// esto no funciona, pero no da error. Queda por desarrollar
		tdtitulo = window.top.document.getElementsByTagName("title");
		window.name.value = tit;
		if (tdtitulo) {
			tdtitulo.innerHTML = tit;
		}
	}
}

function setLocalizacion(localizacion) {
	var tdlocalizacion = top.document.getElementById("barraNavegacion");
	if (tdlocalizacion) {
		tdlocalizacion.value = localizacion;
	}
}

// VENTANAS MODALES
// Abre una ventana modal con la pagina ventanaModal.jsp, que recibe
// como parametros el nombre de la transaccion y los parametros que necesita
// para poder abrir esta transaccion en un iframe que contiene ventanaModal.jsp
// ESTE PROCEDIMIENTO ES NECESARIO PARA EL CORRECTO FUNCIONAMIENTO
// DE AQUELLO QUE SE ABRE DENTRO DE UNA VENTANA MODAL.

function ventaModalGeneral(nombreFormulario, tamano, recursoMsg) {
	//alert("ventaModalGeneral : fromulario "+nombreFormulario);
	var formulario = document.getElementById(nombreFormulario);
	// FIX: Find form by name in case it was not found by id
	if(!formulario){
		var formularios = document.getElementsByName(nombreFormulario);
		if(formularios.length > 0){
			formulario = formularios[0];
		}
	}
	var returnValue;
	var msg = '';
	if (recursoMsg)
		msg = '?msg=' + recursoMsg;
	// cargo el action en un campo que se llama siempre actionModal
	formulario.actionModal.value = formulario.getAttribute("action");
	// envio los campos del formulario como parametro con el action incluido en
	// ellos
	if (tamano == "G") {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:650px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	} else if (tamano == "M") {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:440px;dialogWidth:700px;help:no;scroll:no;status:no;");
	} else if (tamano == "P") {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:340px;dialogWidth:500px;help:no;scroll:no;status:no;");
	} else if (tamano == "0") {
		returnValue = showModalDialog(
				"/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHide:yes;dialogHeight:120px;dialogWidth:70px;help:no;scroll:no;status:no;dialogLeft:500;dialogTop:450");
	} else {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:590px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	}
	//alert("Ventana Modal: returnValue = "+returnValue);	
	window.top.focus();
	return returnValue;
}

function cierraConParametros(valores) {
	window.top.returnValue = valores;
	window.top.close();
}

function ventaModalGeneralScrollAuto(nombreFormulario, tamano) {
	//alert("ventaModalGeneralScrollAuto : fromulario "+nombreFormulario);
	var formulario = document.getElementById(nombreFormulario);
	// FIX: Find form by name in case it was not found by id
	if(!formulario){
		var formularios = document.getElementsByName(nombreFormulario);
		if(formularios.length > 0){
			formulario = formularios[0];
		}
	}
	var returnValue;
	// cargo el action en un campo que se llama siempre actionModal
	formulario.actionModal.value = formulario.getAttribute("action");
	// envio los campos del formulario como parametro con el action incluido en
	// ellos
	if (tamano == "G") {
		returnValue = showModalDialog(
				"/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1", formulario,
				"dialogHeight:575px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	} else if (tamano == "M") {
		returnValue = showModalDialog(
				"/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1", formulario,
				"dialogHeight:425px;dialogWidth:700px;help:no;scroll:no;status:no;");
	} else if (tamano == "P") {
		returnValue = showModalDialog(
				"/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1", formulario,
				"dialogHeight:325px;dialogWidth:500px;help:no;scroll:no;status:no;");
	} else {
		returnValue = showModalDialog(
				"/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1", formulario,
				"dialogHeight:575px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	}
	window.top.focus();
	return returnValue;
}

var iconoinhabilidado = '';

function subicono(identificador) {
	// if (top.document.getElementById("velo")) {
	// top.document.getElementById("velo").style.visibility="visible";
	// }

	iconoinhabilidado = identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);
	for (ii = 0; ii < iconos.length; ii++) {
		iconos[ii].disabled = true;
	}

}
function finsubicono(identificador) {
	// if (top.document.getElementById("velo")) {
	// top.document.getElementById("velo").style.visibility="hidden";
	// }

	iconoinhabilidado = identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);

	for (ii = 0; ii < iconos.length; ii++) {
		iconos[ii].disabled = false;
	}

}
/*
 * function deshabilitariconos(identificador) { iconoinhabilidado=identificador;
 * var iconos = document.getElementsByName(iconoinhabilidado); for (ii=0;ii<iconos.length;ii++) {
 * iconos[ii].disabled=true; }
 *  } function habilitariconos(identificador) { iconoinhabilidado=identificador;
 * var iconos = document.getElementsByName(iconoinhabilidado); for (ii=0;ii<iconos.length;ii++) {
 * iconos[ii].disabled=false; }
 *  }
 */

function sub(w){
	var windowTop=window.top;
	windowTop.mainSub();
	return true;
}

function fin(w){
	var windowTop=window.top;
	if(windowTop.bloqueado)
		windowTop.mainFin();
	return true;
}

function trim(s) {
	return s.replace(/^\s*/, "").replace(/\s*$/, "");
}

function ajusteAlto(nObj) {
	ajusteAltoMain(nObj, 0);
}

function ajusteAltoBotones(nObj) {
	ajusteAltoMain(nObj, 32);
}

function ajusteAltoBotonesPaginador(nObj) {
	ajusteAltoMain(nObj, 52);
}

function ajusteAltoPaginador(nObj) {
	ajusteAltoMain(nObj, 20);
}

function ajusteAltoMain(nObj, menos) {
	//alert("ajusteAlto obj: "+nObj+"; menos: "+menos);
	if(!jQuery || !jQuery.height || !jQuery.offset){
		jQuery=window.top.jQuery;
	}
	var obj = document.getElementById(nObj);
	if (obj) {
		var cont = obj.parentElement;
		var hCont = obj.offsetParent.clientHeight;
		//alert("obj = "+obj.id + " : " + obj.nodeName + " : " + obj.offsetTop);
		if (obj.nodeName == 'IFRAME') {
			if (nObj == 'mainWorkArea') {
				hCont = jQuery(window).height() - jQuery('#posicionTitulo', window.top.document).height() - jQuery('#posicionTitulo', window.top.document).offset().top;
				//alert("final iframe mainWorkArea : "+hCont);
				obj.style.height = hCont;
		        obj.style.pixelHeight = hCont;
			} else {				
				hCont = jQuery(window).height() - jQuery(obj, this.parent.document).offset().top - menos;
				//hCont = jQuery.height(window) - jQuery.offset(obj, this.parent.document).top - menos;
				//alert("final iframe other : "+hCont);
				obj.style.height = hCont;
				obj.style.pixelHeight = hCont;
			}			
		} else {
			if (cont.nodeName == 'FORM') {
				hCont = cont.parentElement.offsetHeight;
			}
			//alert("Padre body = "+cont.id + " : " + cont.nodeName + " : " + hCont);
			var hij = cont.children;
			for (var x = 0; x < hij.length; x++) {
				var hijo = hij[x];
				if (hijo.nodeName != 'SCRIPT' && hijo.nodeName != 'INPUT' && hijo.nodeName != '#comment' && hijo.offsetHeight != 0) {
					if (hijo.id != nObj) {
						//alert("hijo menos="+hijo.id+" : "+hijo.nodeName + " : " +hijo.offsetHeight );
						hCont = hCont - hijo.offsetHeight;
					}
				}
			}
			hCont = hCont - menos;
			//alert("final height = "+hCont);
			obj.style.height = hCont;
			obj.style.pixelHeight = hCont;
		}
	}
}

function pintaToolTipDac(ver, id) {
	o = document.getElementById(id);
	if (ver == 'on') {
		// o.style.width = 100;
		// o.style.height = 100;
		// o.style.zIndex = 0;
		o.style.display = "block";
	} else {
		// o.style.height = 1;
		// o.style.width = 1;
		// o.style.zIndex = 5;
		o.style.display = "none";
	}
	return;
}

function pintaToolTipDacDegradado(onOff, texto, colorFondo, colorTexto,
		tipoLetra) {
	if (onOff == 'on') {
		posX = window.event.x;
		posY = window.event.y;

		if (posX < 0)
			posX = 0;
		posX += 5;

		if (posY < 0)
			posY = 0;
		posY += 5;

		var oPopup = window.createPopup();
		var oPopBody = oPopup.document.body;

		colorFinal = parseInt("0x" + colorFondo, "0x"); // - 0x181818;
		colorFinal = colorFinal.toString(16);

		texto = "<div style='position:absolute; top:0; left:0; width:350px; height:80px; padding:5px;"
				+ "filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1,startColorstr=#"
				+ colorFondo
				+ ", endColorstr=#"
				+ colorFinal
				+ ")'>"
				+ texto
				+ "</div>";

		oPopBody.style.backgroundColor = colorFondo;
		oPopBody.style.border = "solid 1px #999999";
		oPopBody.style.fontSize = "12";
		oPopBody.style.fontFamily = tipoLetra;
		oPopBody.style.color = colorTexto;
		oPopBody.innerHTML = texto;
		oPopup.show(posX, posY, 350, 80, document.body);
	} else {
		var oPopup = window.createPopup();
		oPopup.show(0, 0, 0, 0, document.body);
		oPopup.hide();
	}
}

function submitConTeclaEnter() {
	var keycode;
	if (window.event) {
		keycode = window.event.keyCode;
	}
	if (keycode == 13) {
		buscar();
		return false;
	}
}

function registrarEnterFormularios() {
	for (i = 0; i < document.forms.length; i++) {
		elementos = document.forms[i].elements;
		for (j = 0; j < elementos.length; j++) {

			if (elementos[j].type == "hidden" || elementos[j].readOnly)
				continue;

			elementos[j].onkeypress = submitConTeclaEnter;
		}
	}
	setFocusFormularios();
}

function setFocusFormularios() {
	for (i = 0; i < document.forms.length; i++) {
		elementos = document.forms[i].elements;
		for (j = 0; j < elementos.length; j++) {

			if (elementos[j].type == "hidden" || elementos[j].readOnly)
				continue;

			if (elementos[j].type == "text" || elementos[j].type == "checkbox"
					|| elementos[j].type == "select-one") {
				try {
					elementos[j].focus();
				} catch (err) {
					continue;
				}
				return false;
			}
		}
	}
}

/*
 * Para limpiar la seleccion en los tagCombo. idCombo = nombre del combo (p.e.
 * name="pais" --> idCombo="pais") valor = id del elemento a seleccionar. Si es ""
 * no se selecciona ningun elemento
 */
function seleccionComboSiga(idCombo, valor) {

	combo = top.frames[0].document.frames[0].document.getElementById(idCombo
			+ 'Frame');

	if (!combo) {
		combo = top.frames[0].document.getElementById(idCombo + 'Frame');
	}
	if (!combo)
		return;
	var cadena = combo.src;

	var ini = cadena.indexOf('&elementoSel=[');
	if (ini == -1) {
		return;
	}

	var fin = cadena.indexOf('&', ini + 1);
	if (fin == -1) {
		combo.src = cadena.substring(0, ini) + "&elementoSel=";
		return;
	}

	valor = "[" + valor + "]";
	combo.src = cadena.substring(0, ini) + "&elementoSel=" + valor
			+ cadena.substring(fin);
}

var numb = '0123456789';
var lwr = 'abcdefghijklmnopqrstuvwxyz';
var upr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

function isValid(parm, val) {
	if (parm == "")
		return true;
	for (i = 0; i < parm.length; i++) {
		if (val.indexOf(parm.charAt(i), 0) == -1)
			return false;
	}
	return true;
}

function isNumero(parm) {
	return isValid(parm, numb);
}
function isMinuscula(parm) {
	return isValid(parm, lwr);
}
function isMayuscula(parm) {
	return isValid(parm, upr);
}
function isAlfa(parm) {
	return isValid(parm, lwr + upr);
}
function isAlphanum(parm) {
	return isValid(parm, lwr + upr + numb);
}
function replaceAll(text, busca, reemplaza) {
	while (text.toString().indexOf(busca) != -1) {
		text = text.toString().replace(busca, reemplaza);
	}
	return text;
}



/**
 * Redondea num con dec posiciones decimales
 * 
 * @param num
 *            numero a redondear
 * @param dec
 *            posiciones decimales que se quieren obtener
 * @return num redondeado con dec posiciones decimales
 */
function roundNumber(num, dec) {
	var result = Math.round(Math.round(num * Math.pow(10, dec + 1))
			/ Math.pow(10, 1))
			/ Math.pow(10, dec);
	return result;
}

/**
 * Devuelve el objeto padre mas cercano de tipo <code>type</code> del elemento
 * <code>element</code>.
 * 
 * @param element
 *            Elemento del cual se quiere obtener su padre.
 * @param type
 *            Tipo del elemento padre.
 * @return el elemento padre de tipo <code>type</code> mas cercano a
 *         <code>element</code> o <code>undefined</code> si no lo encuentra.
 */
function getParentOfType(element, type) {
	var parent = element.parentNode;
	while (parent != null && parent != undefined
			&& parent.nodeName.toUpperCase() != type.toUpperCase()) {
		parent = parent.parentNode;
	}

	return parent;
}

/**
 * Devuelve el form que contiene al elemento.
 * 
 * @param element
 *            elemento del cual se quiere obtener el form que lo contiene.
 */
function getParentForm(element) {
	return getParentOfType(element, "FORM");
}

/**
 * Devuelve la celda <code>cellId</code> de la fila con identificador/PK igual
 * a <code>idValue</code> de la tabla <code>tableId</code> donde
 * <code>idIndex</code> es la columna que contiene el identificador.
 * 
 * @param tableId
 *            Identificador de la tabla.
 * @param idValue
 *            Valor del identificador del registro que se desea seleccionar.
 * @param idIndex
 *            Columna de la tabla que contiene el identificador.
 * @param cellId
 *            Indice de la celda, dentro de la fila, que se quiere devolver.
 */

function getCellValue(tableId, idValue, IdIndex, cellId) {
	return getRowById(tableId, idValue, IdIndex).cells[cellId].innerHTML;
}

/**
 * Devuelve la fila con identificador/PK igual a <code>idValue</code> en la
 * tabla <code>tableId</code> donde <code>idIndex</code> es la columna que
 * contiene el identificador.
 * 
 * @param tableId
 *            Identificador de la tabla.
 * @param idValue
 *            Valor del identificador del registro que se desea seleccionar.
 * @param idIndex
 *            Columna de la tabla que contiene el identificador.
 */
function getRowById(tableId, idValue, idIndex) {
	try {
		var table = document.getElementById(tableId);
		var rowCount = table.rows.length;

		for ( var i = 0; i < rowCount; i++) {
			var row = table.rows[i];

			var cellCount = row.cells.length;
			for ( var j = 0; j < cellCount; j++) {
				var cell = row.cells[idIndex];
				if (cell.innerHTML == idValue) {
					return row;
				}
			}
		}
	} catch (e) {
		alert(e);
	}
}

/**
 * Selecciona la fila con identificador/PK igual a <code>idValue</code> en la
 * tabla <code>tableId</code>. <code>idIndex</code> es la columna que
 * contiene el identificador.
 * 
 * @param tableId
 *            Identificador de la tabla.
 * @param idValue
 *            Valor del identificador del registro que se desea seleccionar.
 * @param idIndex
 *            Columna de la tabla que contiene el identificador.
 */
function selectRow(tableId, idValue, idIndex) {
	var table = document.getElementById(tableId);
	if (table == null)
		return;

	var rowCount = table.rows.length;

	for ( var i = 0; i < rowCount; i++) {
		var row = table.rows[i];
		var cell = row.cells[idIndex];
		if (cell.innerHTML == idValue) {
			row.style.backgroundColor = getStyle('backgroundColor',
					'listaNonEditSelected', 'stylesheet2');
		} else {
			if (i % 2 == 0) {
				row.style.backgroundColor = getStyle('backgroundColor', 'even',
						'stylesheet2');
			} else {
				row.style.backgroundColor = getStyle('backgroundColor', 'odd',
						'stylesheet2');
			}
		}
	}
}

/**
 * Comprueba si existe algun registro seleccionado en el formulario
 * <code>formName</code>
 * 
 * @param formName
 *            nombre del formulario sobre el que se va a comprobar si existe
 *            algun registro seleccionado.
 * @return <code>true</code> si existe algun registro seleccionado,
 *         <code>false</code> en caso contrario.
 */
function existsSelected(formName) {
	var elements = document.getElementById(formName).elements;
	for (i = 0; i < elements.length; i++) {
		if (elements[i].type == "checkbox" && elements[i].checked)
			return true;
	}
	return false;
}

/**
 * Funcion generica para llamar al metodo de un action pasandole una accion a
 * realizar
 * 
 * @param formName
 *            Nombre del formulario
 * @param id
 *            PK del registro sobre el que se va a realizar la accion
 * @param accion
 *            Nombre del metodo del action
 * @return
 */
function submitItemAction(id, formName, accion) {
	var form = document.getElementById(formName);
	form.id.value = id;
	form.accion.value = accion;
	form.submit();
}

/**
 * Funcion generica de consulta de un registro
 * 
 * @param formName
 *            Nombre del formulario
 * @param id
 *            PK del registro sobre el que se va a realizar la accion
 */
function consultar(id, formName) {
	submitItemAction(id, formName, "ver");
}

/**
 * Funcion generica de edicion de un registro
 * 
 * @param formName
 *            Nombre del formulario
 * @param id
 *            PK del registro sobre el que se va a realizar la accion
 */
function editar(id, formName) {
	submitItemAction(id, formName, "editar");
}

/**
 * Funcion generica de consulta de un registro
 * 
 * @param formName
 *            Nombre del formulario
 * @param id
 *            PK del registro sobre el que se va a realizar la accion
 */
function consultar(id, formName) {
	submitItemAction(id, formName, "ver");
}

/**
 * Funcion generica de edicion de un registro
 * 
 * @param formName
 *            Nombre del formulario
 * @param id
 *            PK del registro sobre el que se va a realizar la accion
 */
function informacionLetrado(id, formName) {
	submitItemAction(id, formName, "informacionLetrado");
}

/**
 * Obtiene el valor de un artibuto de una clase de una hoja de estilo
 */
function getStyle(atributo, clase, css) {
	var theRules = new Array();
	var theCss;
	var theStyle;

	for (i = 0; i < document.styleSheets.length; i++) {
		if (document.styleSheets[i].href.match(css)) {
			theCss = document.styleSheets[i];
			break;
		}
	}

	if (theCss.cssRules) // Comprobación de reglas de Estilos en Firefox
	{
		theRules = theCss.cssRules;
	} else {
		if (theCss.rules) // Comprobación de reglas de Estilos en Internet
							// Explorer
		{
			theRules = theCss.rules;
		}
	}

	for (elem in theRules) // Recorro las reglas de estilos CSS
	{
		if (typeof theRules[elem] == "object") // si el elemento que estoy
												// recorriendo es un Objeto..
		{
			for (elem2 in theRules[elem]) // Recorro el objeto
			{
				if (theRules[elem].selectorText == "." + clase) // Si la clase
																// que estoy
																// recorriendo
																// es la que
																// deseo
																// buscar...
				{
					if (elem2 == "style") // Si dentro de la clase estoy en el
											// objeto STYLE
					{
						foundedClass = true;
						theStyle = theRules[elem][elem2];
					}
				}
			}
		}
	}
	return eval("theStyle." + atributo);
}

/**
 * Ajusta el height de un elemento al maximo del espacio que quede por ocupar
 * 
 * @param nObj
 */
function ajusteAltoDisplayTag(nObj) {
	var obj = document.getElementById(nObj);
	if (obj) {
		var height;
		if (window.name == "mainWorkArea") {
			height = window.document.documentElement.offsetHeight;
		} else if (window.parent.name != "mainWorkArea") {
			height = window.parent.document.getElementById("MainWorkArea").offsetHeight;
		}
		var padre = obj.parentNode;
		var hermanos = obj.parentNode.childNodes;
		var contHermanos = 0;
		// alert(ventana.offsetHeight +" "+ obj.offsetTop);
		for (x = 0; x < hermanos.length; x++) {
			// alert(hermanos(x).id + " : " + hermanos(x).nodeName + " : " +
			// hermanos(x).offsetHeight );
			if (hermanos(x).nodeName != 'SCRIPT'
					&& hermanos(x).nodeName != '#comment'
					&& hermanos(x).offsetHeight != 0 && hermanos(x) != obj
					&& hermanos(x).offsetHeight) {
				contHermanos = contHermanos + hermanos(x).offsetHeight;
			}
		}

		// el 5 es un ajuste que me saco de la manga, mas o menos, habrá que
		// terminar de calcular bien el tamanyo, teneiendo en cuenta
		// los margenes, paddings y demas
		var cont = height - obj.offsetTop - (2 * contHermanos) - 5;
		// alert(ventana.offsetHeight +" "+ obj.offsetTop +" "+ contHermanos + "
		// " + cont);
		obj.style.pixelHeight = cont;
	}
}

/**
 * Borra los valores de los elementos input y select de un formulario
 * 
 * @param form
 */
function limpiarForm(form) {
	var frm_elements = form.elements;
	for (i = 0; i < frm_elements.length; i++) {

		field_type = frm_elements[i].type;
		if (field_type) {
			field_type = field_type.toLowerCase();
			switch (field_type) {

			case "text":
			case "password":
			case "textarea":
			case "hidden":

				frm_elements[i].value = "";
				break;

			case "radio":
			case "checkbox":

				if (frm_elements[i].checked) {

					frm_elements[i].checked = false;

				}
				break;

			case "select-one":
			case "select-multi":

				frm_elements[i].selectedIndex = -1;
				break;

			default:
				break;

			}
		}

	}
}
function isAllDigits(argvalue) {
	argvalue = argvalue.toString();
	var validChars = "0123456789";
	var startFrom = 0;
	if (argvalue.substring(0, 2) == "0x") {
		validChars = "0123456789abcdefABCDEF";
		startFrom = 2;
	} else if (argvalue.charAt(0) == "0") {
		validChars = "01234567";
		startFrom = 1;
	} else if (argvalue.charAt(0) == "-") {
		startFrom = 1;
	}

	for ( var n = startFrom; n < argvalue.length; n++) {
		if (validChars.indexOf(argvalue.substring(n, n + 1)) == -1)
			return false;
	}
	return true;
}

/**
 * Muestra un DIV o lo oculta
 * 
 * @param ident
 *            El identificador del DIV que vamos a ocultar/mostrar
 * @return
 */
function ocultarDIV(ident) {
	if (document.getElementById(ident).style.display == "none") {
		document.getElementById(ident).style.display = "inline";
		document.getElementById(ident).parentElement.className = 'legend';
		if (document.getElementById(ident + "ImMas"))
			document.getElementById(ident + "ImMas").style.display = "none"
		if (document.getElementById(ident + "ImMenos"))
			document.getElementById(ident + "ImMenos").style.display = "inline-block"
	} else {
		document.getElementById(ident).style.display = "none";
		document.getElementById(ident).parentElement.className = 'legendNoBorder';
		if (document.getElementById(ident + "ImMenos"))
			document.getElementById(ident + "ImMenos").style.display = "none"
		if (document.getElementById(ident + "ImMas"))
			document.getElementById(ident + "ImMas").style.display = "inline-block"
	}
	ajusteAltoPaginador('resultado');
	ajusteAlto('resultado');
	ajusteAlto('mainWorkarea');

	return true;
}
function getFechaActualDDMMYYYY() {
	var date = new Date();
	iDay = date.getDate();
	iMonth = date.getMonth() + 1;
	if (iMonth < 10)
		iMonth = '0' + iMonth;
	if (iDay < 10)
		iDay = '0' + iDay;
	iYear = date.getFullYear();
	sDisplayDate = iDay + "/" + iMonth + "/" + iYear;

	return sDisplayDate;
}
function findPosY(obj) {
	var curtop = 0;
	if (obj.offsetParent)
		while (1) {
			curtop += obj.offsetTop;
			if (!obj.offsetParent)
				break;
			obj = obj.offsetParent;
		}
	else if (obj.y)
		curtop += obj.y;
	return curtop;
}
function findPosX(obj) {
	var curleft = 0;
	if (obj.offsetParent)
		while (1) {
			curleft += obj.offsetLeft;
			if (!obj.offsetParent)
				break;
			obj = obj.offsetParent;
		}
	else if (obj.x)
		curleft += obj.x;
	return curleft;
}
function obtenerDigitoControl(valor) {
	valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
	control = 0;
	for (i = 0; i <= 9; i++)
		control += parseInt(valor.charAt(i)) * valores[i];
	control = 11 - (control % 11);
	if (control == 11)
		control = 0;
	else if (control == 10)
		control = 1;
	return control;
}

function isNumeroIdentificacionValido(a){

	var a = trim(a);
	var temp=a.toUpperCase();
	var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
	if (temp!==''){
		//si no tiene un formato valido devuelve error
		if ((!/^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$/.test(temp) && !/^[T]{1}[A-Z0-9]{8}$/.test(temp)) && !/^[0-9]{8}[A-Z]{1}$/.test(temp)){
			return 0;
		}
 
		//comprobacion de NIFs estandar
		if (/^[0-9]{8}[A-Z]{1}$/.test(temp)){
			posicion = a.substring(8,0) % 23;
			letra = cadenadni.charAt(posicion);
			var letradni=temp.charAt(8);
			if (letra == letradni){
			   	return 1;
			}else{
				return -1;
			}
		}
 
		//algoritmo para comprobacion de codigos tipo CIF
		suma = parseInt(a[2])+parseInt(a[4])+parseInt(a[6]);
		for (i = 1; i < 8; i += 2){
			temp1 = 2 * parseInt(a[i]);
			temp1 += '';
			temp1 = temp1.substring(0,1);
			temp2 = 2 * parseInt(a[i]);
			temp2 += '';
			temp2 = temp2.substring(1,2);
			if (temp2 == ''){
				temp2 = '0';
			}
			suma += (parseInt(temp1) + parseInt(temp2));
		}
		suma += '';
		n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
 
		//comprobacion de NIFs especiales (se calculan como CIFs)
		if (/^[KLM]{1}/.test(temp)){
			if (a[8] == String.fromCharCode(64 + n)){
				return 1;
			}else{
				return -1;
			}
		}
 
		//comprobacion de CIFs
		if (/^[ABCDEFGHJNPQRSUVW]{1}/.test(temp)){
			temp = n + '';
			if (a[8] == String.fromCharCode(64 + n) || a[8] == parseInt(temp.substring(temp.length-1, temp.length))){
				return 2;
			}else{
				return -2;
			}
		}
 
		//comprobacion de NIEs
		//T
		if (/^[T]{1}/.test(temp)){
			if (a[8] == /^[T]{1}[A-Z0-9]{8}$/.test(temp)){
				return 3;
			}else{
				return -3;
			}
		}
 
		//XYZ
		if (/^[XYZ]{1}/.test(temp)){
			pos = str_replace(['X', 'Y', 'Z'], ['0','1','2'], temp).substring(0, 8) % 23;
			if (a[8] == cadenadni.substring(pos, pos + 1)){
				return 3;
			}else{
				return -3;
			}
		}
	}
	return 0;
}

function isIBANValido(value) {

    function replaceChars(val) {
        var replaced = '',
            char, code;
        for (var i = 0, m = val.length; i < m; i++) {
            char = val.charAt(i);
            code = char.charCodeAt(0);
            replaced += (code >= 65 && code <= 90) ? new String(code - 55) : char;
        }
        return replaced;
    }

    function mod97(num) {
        var mod = 0,
            digit;
        for (var i = 0, m = num.length; i < m; i++) {
            digit = parseInt(num.charAt(i), 10);
            mod = ((mod * 10) + digit) % 97;
        }
        return mod;
    }

    value = value.toUpperCase();
    if (false === (new RegExp('^[A-Z]{2}[0-9]{2}[A-Z0-9]+$')).test(value)) {
        return false;
    }

    var ibanPrefix = value.substr(0, 4),
        ibanRearranged = value.substr(4) + ibanPrefix,
        ibanAsNumber = replaceChars(ibanRearranged);

    if (mod97(ibanAsNumber) !== 1) {
        return false;
    }

    return true;
}

function isSWIFTValido(swift){
	var regSWIFT = /^([a-zA-Z]){4}([a-zA-Z]){2}([0-9a-zA-Z]){2}([0-9a-zA-Z]{3})?$/;
	if(regSWIFT.test(swift) == false){
		return false;
	}
	return true; 
}

document._oldGetElementById = document.getElementById;
document.getElementById = function(elemIdOrName) {
    var result = document._oldGetElementById(elemIdOrName);
    if (! result) {
        var elems = document.getElementsByName(elemIdOrName); 
        if (elems && elems.length > 0) {
            result = elems[0];
        }
    }

    return result;
};

fin();