var jQuery = false;
try{
	if (window.jQuery){
		jQuery = window.jQuery;
	} else if (window.$){
		jQuery = window.$;
	} else if (window.parent && window.parent.jQuery){
		jQuery = window.parent.jQuery;
	} else if (window.parent && window.parent.$){
		jQuery = window.parent.$;
	} else if (window.top && window.top.jQuery){
		jQuery = window.top.jQuery;
	} else if (window.top && window.top.$)
		jQuery = window.top.$;
} catch(msg){
	jQuery = false;
}
if (!jQuery)
	document.write('<script src="/SIGA/html/js/jquery.ui/js/jquery-1.8.3.js"><\/script>');

// *** BEGIN JQUERY PLUGINS *** //
/**
*	jQuery.noticeAdd() and jQuery.noticeRemove()
*	These functions create and remove growl-like notices
*		
*   Copyright (c) 2009 Tim Benniks
*
*	Permission is hereby granted, free of charge, to any person obtaining a copy
*	of this software and associated documentation files (the "Software"), to deal
*	in the Software without restriction, including without limitation the rights
*	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*	copies of the Software, and to permit persons to whom the Software is
*	furnished to do so, subject to the following conditions:
*
*	The above copyright notice and this permission notice shall be included in
*	all copies or substantial portions of the Software.
*
*	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
*	THE SOFTWARE.
*	
*	@author 	Tim Benniks <tim@timbenniks.com>
* 	@copyright  2009 timbenniks.com
*	@version    $Id: SIGA.js,v 1.44 2013-05-28 12:29:52 tf2 Exp $
**/
(function(jQuery)
{
	jQuery.extend({			
		noticeAdd: function(options)
		{	
			var defaults = {
				inEffect: 			{opacity: 'show'},	// in effect
				inEffectDuration: 	600,				// in effect duration in miliseconds
				stayTime: 			15000,				// time in miliseconds before the item has to disappear
				text: 				'',					// content of the item
				stay: 				true,				// should the notice item stay or not?
				type: 				'notice' 			// could also be error, succes
			}
			
			// declare varaibles
			var options, noticeWrapAll, noticeItemOuter, noticeItemInner, noticeItemClose;
								
			options 		= jQuery.extend({}, defaults, options);
			noticeWrapAll	= (!jQuery('.notice-wrap').length) ? jQuery('<div></div>').addClass('notice-wrap').appendTo('body') : jQuery('.notice-wrap');
			noticeItemOuter	= jQuery('<div></div>').addClass('notice-item-wrapper').hover(function() { jQuery.noticeRemove(noticeItemInner) });
			noticeItemInner	= jQuery('<div></div>').hide().addClass('notice-item ' + options.type).appendTo(noticeWrapAll).html(options.text).animate(options.inEffect, options.inEffectDuration).wrap(noticeItemOuter);
			//noticeItemClose	= jQuery('<div></div>').addClass('notice-item-close').prependTo(noticeItemInner).html('x').hover(function() { jQuery.noticeRemove(noticeItemInner) });
			noticeItemClose	= jQuery('<div></div>').addClass('notice-item-close').prependTo(noticeItemInner);
			
			// hmmmz, zucht
			if(navigator.userAgent.match(/MSIE 6/i)) 
			{
		    	noticeWrapAll.css({top: document.documentElement.scrollTop});
		    }
			
			if(!options.stay)
			{
				setTimeout(function()
				{
					jQuery.noticeRemove(noticeItemInner);
				},
				options.stayTime);
			}
		},
		
		noticeRemove: function(obj)
		{
			obj.animate({opacity: '0'}, 600, function()
			{
				obj.parent().animate({height: '0px'}, 600, function()
				{
					obj.parent().remove();
				});
			});
		}
	});
})(jQuery);

/*
Masked Input plugin for jQuery
Copyright (c) 2007-2013 Josh Bush (digitalbush.com)
Licensed under the MIT license (http://digitalbush.com/projects/masked-input-plugin/#license)
Version: 1.3.1
*/
(function(e){function t(){var e=document.createElement("input"),t="onpaste";return e.setAttribute(t,""),"function"==typeof e[t]?"paste":"input"}var n,a=t()+".mask",r=navigator.userAgent,i=/iphone/i.test(r),o=/android/i.test(r);e.mask={definitions:{9:"[0-9]",a:"[A-Za-z]","*":"[A-Za-z0-9]"},dataName:"rawMaskFn",placeholder:"_"},e.fn.extend({caret:function(e,t){var n;if(0!==this.length&&!this.is(":hidden"))return"number"==typeof e?(t="number"==typeof t?t:e,this.each(function(){this.setSelectionRange?this.setSelectionRange(e,t):this.createTextRange&&(n=this.createTextRange(),n.collapse(!0),n.moveEnd("character",t),n.moveStart("character",e),n.select())})):(this[0].setSelectionRange?(e=this[0].selectionStart,t=this[0].selectionEnd):document.selection&&document.selection.createRange&&(n=document.selection.createRange(),e=0-n.duplicate().moveStart("character",-1e5),t=e+n.text.length),{begin:e,end:t})},unmask:function(){return this.trigger("unmask")},mask:function(t,r){var c,l,s,u,f,h;return!t&&this.length>0?(c=e(this[0]),c.data(e.mask.dataName)()):(r=e.extend({placeholder:e.mask.placeholder,completed:null},r),l=e.mask.definitions,s=[],u=h=t.length,f=null,e.each(t.split(""),function(e,t){"?"==t?(h--,u=e):l[t]?(s.push(RegExp(l[t])),null===f&&(f=s.length-1)):s.push(null)}),this.trigger("unmask").each(function(){function c(e){for(;h>++e&&!s[e];);return e}function d(e){for(;--e>=0&&!s[e];);return e}function m(e,t){var n,a;if(!(0>e)){for(n=e,a=c(t);h>n;n++)if(s[n]){if(!(h>a&&s[n].test(R[a])))break;R[n]=R[a],R[a]=r.placeholder,a=c(a)}b(),x.caret(Math.max(f,e))}}function p(e){var t,n,a,i;for(t=e,n=r.placeholder;h>t;t++)if(s[t]){if(a=c(t),i=R[t],R[t]=n,!(h>a&&s[a].test(i)))break;n=i}}function g(e){var t,n,a,r=e.which;8===r||46===r||i&&127===r?(t=x.caret(),n=t.begin,a=t.end,0===a-n&&(n=46!==r?d(n):a=c(n-1),a=46===r?c(a):a),k(n,a),m(n,a-1),e.preventDefault()):27==r&&(x.val(S),x.caret(0,y()),e.preventDefault())}function v(t){var n,a,i,l=t.which,u=x.caret();t.ctrlKey||t.altKey||t.metaKey||32>l||l&&(0!==u.end-u.begin&&(k(u.begin,u.end),m(u.begin,u.end-1)),n=c(u.begin-1),h>n&&(a=String.fromCharCode(l),s[n].test(a)&&(p(n),R[n]=a,b(),i=c(n),o?setTimeout(e.proxy(e.fn.caret,x,i),0):x.caret(i),r.completed&&i>=h&&r.completed.call(x))),t.preventDefault())}function k(e,t){var n;for(n=e;t>n&&h>n;n++)s[n]&&(R[n]=r.placeholder)}function b(){x.val(R.join(""))}function y(e){var t,n,a=x.val(),i=-1;for(t=0,pos=0;h>t;t++)if(s[t]){for(R[t]=r.placeholder;pos++<a.length;)if(n=a.charAt(pos-1),s[t].test(n)){R[t]=n,i=t;break}if(pos>a.length)break}else R[t]===a.charAt(pos)&&t!==u&&(pos++,i=t);return e?b():u>i+1?(x.val(""),k(0,h)):(b(),x.val(x.val().substring(0,i+1))),u?t:f}var x=e(this),R=e.map(t.split(""),function(e){return"?"!=e?l[e]?r.placeholder:e:void 0}),S=x.val();x.data(e.mask.dataName,function(){return e.map(R,function(e,t){return s[t]&&e!=r.placeholder?e:null}).join("")}),x.attr("readonly")||x.one("unmask",function(){x.unbind(".mask").removeData(e.mask.dataName)}).bind("focus.mask",function(){clearTimeout(n);var e;S=x.val(),e=y(),n=setTimeout(function(){b(),e==t.length?x.caret(0,e):x.caret(e)},10)}).bind("blur.mask",function(){y(),x.val()!=S&&x.change()}).bind("keydown.mask",g).bind("keypress.mask",v).bind(a,function(){setTimeout(function(){var e=y(!0);x.caret(e),r.completed&&e==x.val().length&&r.completed.call(x)},0)}),y()}))}})})(jQuery);

jQuery(document).ready(function(){
	if(jQuery.fn.mask){
		// Descomentar para no permitir la edición de fechas por texto
		//jQuery("input.datepicker").attr("readonly", "readonly");
		// Comentar para no permitir la edición de fechas por texto	
		jQuery("input.datepicker").not(".boxConsulta").mask('99/99/9999',{completed:function(){datepickerMaskValueChanged(jQuery(this));}});
	}
});

//*** END JQUERY PLUGINS *** //

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
var alertStop = window.alert;

window.alert=function (message, estilo) {
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
	if($.browser.msie || $.browser.mozilla){
		$.browser.safari = false;
	} else {
		$.browser.safari = ( $.browser.webkit && /chrome/.test(navigator.userAgent.toLowerCase()) ) ? false : true;
	}
	if( ($.browser.safari) && (window.top.dialogArguments) ){
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
	} else if (tamano == "XS") {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:140px;dialogWidth:300px;help:no;scroll:no;status:no;");
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
	var jQuery = window.top.jQuery;
	if(w == undefined) {
		w = this.top;
	}
	try {
		// disable links in current frame
		var imgs = w.document.getElementsByTagName("img");
		for ( var l = 0; l < imgs.length; l++) {
			if (!imgs(links[l]).hasClass("disabled")) {
				imgs(links[l]).addClass("disabled");
			}
		}
	} catch (e) {
		// TODO: handle exception
	}
	try {
		// disable links in current frame
		var links = w.document.getElementsByTagName("a");
		for ( var l = 0; l < links.length; l++) {
			if (!jQuery(links[l]).hasClass("disabled")) {
				jQuery(links[l]).addClass("disabled");
			}
		}
	} catch (e) {
		// TODO: handle exception
	}
	try {
		// disable buttons in current frame
		var buts = w.document.getElementsByTagName("input");
		for ( var b = 0; b < buts.length; b++) {
			if (buts[b].type == 'button') {
				jQuery(buts[b]).attr("disabled", "disabled");
			}
		}
	} catch (e) {
		// TODO: handle exception
	}
	try {
		// Go to child frames
		var framess = w.frames;
		for(var f=0; f < framess.length; f++){
			sub(framess[f]);
		}
	} catch (e) {
		// TODO: handle exception
	}
	var windowTop=window.top;
	windowTop.mainSub();
	return true;
}

function fin(w){
	
	var jQuery = window.top.jQuery;
	if(w == undefined) {
		w = this.top;
	}
	try {
		// enable links and buttons in current frame
		var links = w.document.getElementsByTagName("a");
		for ( var l = 0; l < links.length; l++) {
			if (jQuery(links[l]).hasClass("disabled")) {
				jQuery(links[l]).removeClass("disabled");
			}
		}
	} catch (e) {
		// TODO: handle exception
		//alert("Error habilitando links. Continuo.");
	}
	try {
		var buts = w.document.getElementsByTagName("input");
		for ( var b = 0; b < buts.length; b++) {
			if (buts[b].type == 'button') {
				jQuery(buts[b]).removeAttr("disabled");
			}
		}
	} catch (e) {
		// TODO: handle exception
		//alert("Error habilitando botones. Continuo.");
	}
	try {
			// Go to child frames
		var framess = w.frames;
		for(var f=0; f < framess.length; f++){
			fin(framess[f]);
		}
	} catch (e) {
		// TODO: handle exception
		//alert("Error procesando frame. Continuo.");
	}
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
	if(!jQuery){
		jQuery=window.top.jQuery;
	}
	var obj = document.getElementById(nObj);
	if (obj) {
		var cont = obj.parentElement;
		var hCont = obj.offsetParent.clientHeight;
		//alert("obj = "+obj.id + " : " + obj.nodeName + " : " + obj.offsetTop);
		if (obj.nodeName == 'IFRAME') {
			var windowHeight = jQuery(window).height();
			if (windowHeight == null || windowHeight == undefined || windowHeight <= 0){
				windowHeight = document.body.offsetHeight - 5;
			}
			
			if (nObj == 'mainWorkArea') {
				//alert("ajusteAltoMain mainWorkArea " +  document.body.offsetHeight + " - " + jQuery('#posicionTitulo', window.top.document).height() + " - " + jQuery('#posicionTitulo', window.top.document).offset().top);				
				hCont =  windowHeight - jQuery('#posicionTitulo', window.top.document).height() - jQuery('#posicionTitulo', window.top.document).offset().top;
				//alert("final iframe mainWorkArea : "+hCont);
				if (hCont > 0){
					obj.style.height = hCont;
			        obj.style.pixelHeight = hCont;
				}
				
			} else {
				//alert("ajusteAltoMain NOT mainWorkArea " + window.outerHeight + " - " + jQuery(obj, this.parent.document).offset().top + " - " + menos);
				hCont = windowHeight - jQuery(obj, this.parent.document).offset().top - menos;
				//hCont = jQuery.height(window) - jQuery.offset(obj, this.parent.document).top - menos;
				//alert("final iframe other : "+hCont);
				if (hCont > 0){
					obj.style.height = hCont;
					obj.style.pixelHeight = hCont;
				}
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
			if (hCont > 0){
			//alert("final height = "+hCont);
			obj.style.height = hCont;
			obj.style.pixelHeight = hCont;
			}
		}
	}
}

function pintaToolTipDac(ver, id) {
	o = document.getElementById(id);
	if (ver == 'on') {
		// o.style.width = 100;
		// o.style.height = 100;
		// o.style.zIndex = 0;
		o.style.display = "inline";
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

//aalg. valida float con coma, no con punto
function validaFloat(numero){
  if (!/^([0-9])*[,]?[0-9]*$/.test(numero))
  	return false;
  else 
	return true;
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
	alert("aayy"+formName);
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


/**
 * DHTML date validation script for dd/mm/yyyy. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/datevalidation.asp)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function validarFechaRegExp(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		//alert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		//alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		//alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		//alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		//alert("Please enter a valid date")
		return false
	}
return true
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

//BNS
if (jQuery){
	(function(){
		if(!jQuery.fn.watch){
			jQuery.fn.watch = function(props, callback, timeout){
			    if(!timeout)
			        timeout = 10;
			    return this.each(function(){
			        var el         = jQuery(this),
			            func     = function(){ __check.call(this, el); },
			            data     = {    props:     props.split(","),
			                        func:     callback,
			                        vals:     [] };
			            jQuery.each(data.props, function(i) { data.vals[i] = el.attr(data.props[i]); });
			        el.data(data);
			        if (typeof (this.onpropertychange) == "object"){
			            el.bind("propertychange", callback);
			        } else if (jQuery.browser.mozilla){
			            el.bind("DOMAttrModified", callback);
			        } else {
			            setInterval(func, timeout);
			        }
			    });
			    function __check(el) {
			        var data     = el.data(),
			            changed = false,
			            temp    = "";
			        for(var i=0;i < data.props.length; i++) {
			            temp = el.attr(data.props[i]);
			            if(data.vals[i] != temp){
			                data.vals[i] = temp;
			                changed = true;
			                break;
			            }
			        }
			        if(changed && data.func) {
			            data.func.call(el, data);
			        }
			    }
			};
			var original_removeAttr = jQuery.fn.removeAttr;
			jQuery.fn.removeAttr = function(){
				original_removeAttr.apply(this, arguments);
				//alert(jQuery(this).attr("id")+".removeAttr("+arguments+")");
				jQuery(this).each(function(){
					if (jQuery(this).is("select"))
						disableSelect(jQuery(this));
				});
			};
			/*
			var original_attr = jQuery.fn.attr;
			jQuery.fn.attr = function(attrname, attrvalue){
				original_attr.apply(this,arguments);
				//alert(jQuery(this).attr("id")+".attr("+attrname+")");
				jQuery(this).each(function(){
					if (jQuery(this).is("select"))
						disableSelect(jQuery(this));
				});
			};
			*/
		}
	})();
	//var log = "";
	if (typeof modo != "undefined" && modo.toUpperCase() === "VER"){
		jQuery(document).ready(function() {	
		    jQuery(window).load(function (){
		    	iniInputSelect(false);
		    	//alert("entro");
	//	    	if (log != "")
	//	    		alert(log);
		    });
		});
	}
	
	function iniInputSelect(frames){
		var selects = undefined;
		if (frames){
			selects = frames.find("select").not(".inputSelect");
			frames.addClass("inputSelect");
		} else
			selects = jQuery("select").not(".inputSelect");
		
		selects.each(function(){
	    	   var select = jQuery(this);
	    	   select.addClass("inputSelect");
	    	   //select.change(function(event){disableSelect(jQuery(this));});
	    	   if (typeof (this.onpropertychange) == "object"){
					//alert("voy por onpropertychange");
					select.bind("propertychange", function(event){disableSelect(jQuery(this));});
				} else{
					try{
						select.watch('disabled', function(){
							disableSelect(jQuery(this));											
						});
						//alert("voy por watch");
					} catch(msg) {
						if (this.addEventListener){ 
				    		   select.bind("DOMAttrModified", function(event){disableSelect(jQuery(this));});
				    		   //alert("voy por DOMAttrModified");
			    	   } else {
			    		   // No hay forma de detectar la modificación del atributo
			    	   }
					}
				}	            
				disableSelect(select);
	    });
		if (frames){
			if (frames.find("iframe").not("inputSelect").length > 0)
				iniInputSelect(frames.find("iframe").not("inputSelect").contents());			
		} else if (jQuery("iframe").not("inputSelect").length > 0)
				iniInputSelect(jQuery("iframe").not("inputSelect").contents());
	}
		
	var selectId = 0;
	function disableSelect(select){
		//alert("BEGIN disableSelect");
		if (select.length > 0 && select.is("select")){
			if (select.length > 1){
				select.each(function(){
					disableSelect(jQuery(this));
				});
			} else {
				if (select.attr("id") == undefined || select.attr("id") === ""){
					if (select.attr("name") == undefined || select.attr("name") === ""){				
						select.attr("id", "select_"+selectId);
						select.attr("name", "select_"+selectId);
						selectId++;
					} else {
						select.attr("id", select.attr("name"));
					}
				}
				if (select.parent().find("#"+select.attr("id")+"_inputDisabled").length > 0){
					select.parent().find("#"+select.attr("id")+"_inputDisabled").remove();
				}
				if (select.is(":disabled") || select.is("[readonly]") || select.hasClass("boxConsulta")){
					
					var inputDisabled = document.createElement("input");
					inputDisabled.setAttribute("type", "text");
					if (select.val() != "-1")
						inputDisabled.setAttribute("value", select.find("option[value="+select.val()+"]").text());					
					inputDisabled.setAttribute("id", select.attr("id")+"_inputDisabled");
					if (select.attr("name") == undefined || select.attr("name") === ""){
						inputDisabled.setAttribute("name", select.attr("name")+"_inputDisabled");
					} else {
						inputDisabled.setAttribute("name", select.attr("id")+"_inputDisabled");
					}

					select.after(jQuery(inputDisabled));
					select.change(function(){
						jQuery(jQuery(this).attr("id")+"_inputDisabled").val(select.find("option[value="+jQuery(this).val()+"]").text());
					});
					jQuery("#"+select.attr("id")+"_inputDisabled").addClass("boxConsulta");
					jQuery("#"+select.attr("id")+"_inputDisabled").addClass("inputDisabled");					
					jQuery("#"+select.attr("id")+"_inputDisabled").attr("readonly","readonly");
					select.hide();
				} else {
					select.show();			
				}
				//alert("ENCONTRADO: " + select.attr("id"));
				//log += select.attr("id") + "; ";
			}
		}
	}
	
	function scrolify(tblAsJQueryObject, height) {
    	var oTbl;
        var newTbl;        
        oTbl = tblAsJQueryObject;
        oTbl.css("border-collapse","collapse");
        var tblId = oTbl.attr("id");
        var oDiv = jQuery("<div id='"+tblId+"_tblFxHeadr'/>");
        oDiv.css('white-space','break-word');
        oTbl.css('white-space','break-word');
        //oDiv.css('text-overflow','ellipsis');
        oTbl.wrap(oDiv);
        // for very large tables you can remove the four lines below
        // and wrap the table with <div> in the mark-up and assign
        // height and overflow property  
        var oTblDiv = jQuery("<div id='"+tblId+"_BodyDiv'/>");
        oTblDiv.css('height', height);
        oTblDiv.css('overflow-y', 'auto');
        oTblDiv.css('overflow-x', 'hidden');
        oTblDiv.css('white-space','break-word');
        oTblDiv.css('text-overflow','ellipsis');
        oTblDiv.css('width','100%');
        oTbl.wrap(oTblDiv);

        // save original width
        oTbl.attr("data-item-original-width", oTbl.width());
        oTbl.find('thead tr td').each(function () {
            jQuery(this).attr("data-item-original-width", jQuery(this).width());
        });
        oTbl.find('tbody tr:eq(0) td').each(function () {
            jQuery(this).attr("data-item-original-width", jQuery(this).width());
        });

        // clone the original table
        newTbl = oTbl.clone();
        
        newTbl.find("div.notFound").empty();
        newTbl.find("div.notFound").remove();
        
        // remove table header from original table
        oTbl.find('thead tr').remove();
        // remove table body from new table
        newTbl.find('tbody tr').remove();

        oTbl.parent().before(newTbl);
        newTbl.wrap("<div id='"+tblId+"_HeaderDiv'/>");

        // replace ORIGINAL COLUMN width                
        newTbl.width(newTbl.attr('data-item-original-width'));        
        newTbl.find('thead tr th').each(function () {
            jQuery(this).width(jQuery(this).attr("data-item-original-width"));
        });
        oTbl.width(oTbl.attr('data-item-original-width'));
        oTbl.find('tbody tr:eq(0) td').each(function () {
            jQuery(this).width(jQuery(this).attr("data-item-original-width"));
        });
        oTbl.find('tbody tr').each(function () {
            jQuery(this).addClass("tableTitle");
        });
        
    }
	function setBodyTdsWidthLikeHeaderTds (table){
		jQuery.each( table.find("thead th"), function(columnIndex, value){
			console.log("columnIndex: " + columnIndex + " set width from "+table.find("tbody td:eq("+columnIndex+")").width() +" to "+jQuery(value).width());
			table.find("tbody td:eq("+columnIndex+")").width(jQuery(value).width());
		});		
	}
	//BNS: Scroll de tablas
	function loadFixedHeaderTables (tableId, fixedHeight) {
		var oTable = jQuery('#'+tableId+'.fixedHeaderTable');
    	if (oTable.length > 0){
    		//setBodyTdsWidthLikeHeaderTds(oTable);
    		if (fixedHeight != undefined && !isNaN(fixedHeight)){
    			scrolify(oTable, fixedHeight);
    		} else {
	    		//alert("fixedHeaderTable BEGIN");
	    		var fixedHeaderTableHeight = 0;
				var tableContainer = getCurrentIFrame();
				if (tableContainer == null){
					tableContainer = oTable;
				} else {
					tableContainer = jQuery(tableContainer);
				}
				fixedHeaderTableHeight += tableContainer.height();			
				
	    		var clonarTablaBotones = true;
	        	var tablaBotones = undefined;
	        	if (jQuery('table.botonesDetalle').length > 0) {
	        		tablaBotones = jQuery('table.botonesDetalle');
	        		clonarTablaBotones = false;
	        	} else if (parent && parent.document && parent.document.body){
	        		//alert("buscando botonesDetalle en parents...");
					var parentElement = parent;
					var bKeepLooking = true;
					while (bKeepLooking && parentElement && parentElement.document && parentElement.document.body && tablaBotones == undefined){					
						if (jQuery('table.botonesDetalle', parentElement.document.body).length > 0) {
							tablaBotones = jQuery('table.botonesDetalle', parent.document.body);
						} else if (window.top.document != parentElement.document){
							parentElement = parentElement.parent;
						} else {
							bKeepLooking = false;
						}
						//alert("buscando en parent..." + parentElement);
					}				
				}
	        	// Buscamos paginación
				//alert("Buscamos paginación ...");
				var clonarTablaPaginacion = true;
	        	var tablaPaginacion = undefined;
				if (jQuery('div.tPaginatorDiv').length > 0){
					//alert("Encontrado al mismo nivel...");
					clonarTablaPaginacion = false;
					tablaPaginacion = jQuery('div.tPaginatorDiv');
				} else {
					parentElement = parent;
					bKeepLooking = true;
					while (bKeepLooking && parentElement && parentElement.document && parentElement.document.body && tablaBotones == undefined){					
						if (jQuery('div.tPaginatorDiv', parentElement.document.body).length > 0) {
							tablaPaginacion = jQuery('div.tPaginatorDiv', parent.document.body);
						} else if (window.top.document != parentElement.document){
							parentElement = parentElement.parent;
						} else {
							bKeepLooking = false;
						}
						//alert("buscando en parent..." + parentElement);
					}				
				}
				
				var tablaPaginacionLocal = tablaPaginacion;
				var tablaPaginacionLocalId = undefined;
				if (tablaPaginacion != undefined){
					//alert("tiene tablaPaginacion " + tablaPaginacion.offset().top);
					tablaPaginacionLocalId = tablaPaginacionLocal.attr("id");
		        	if (tablaPaginacionLocalId == undefined){
		        		tablaPaginacionLocalId = "tablaPaginacionId";
		        		tablaPaginacionLocal.attr("id", tablaPaginacionLocalId);
		        	}
					if (clonarTablaPaginacion){
						tablaPaginacionLocal = tablaPaginacion.clone();
						tablaPaginacionLocalId = "borrarTablaPaginacion";
		        		tablaPaginacionLocal.attr("id", tablaPaginacionLocalId);
		        		jQuery('#'+tableId+'_BodyDiv').after(tablaPaginacionLocal);
		        		//alert("tablaPaginacion clonada" + tablaPaginacionLocal.offset().top);
					}
				}
				var tablaBotonesLocal = tablaBotones;
				var tablaBotonesLocalId = undefined;
				if (tablaBotones != undefined){
					//alert("tiene tablaBotones");
					tablaBotonesLocalId = tablaBotonesLocal.attr("id");
		        	if (tablaBotonesLocalId == undefined){
		        		tablaBotonesLocalId = "tablaBotonesId";
		        		tablaBotonesLocal.attr("id", tablaBotonesLocalId);
		        	}
					if (clonarTablaBotones){
						tablaBotonesLocal = tablaBotones.clone();
		        		tablaBotonesLocalId = "borrarTablaBotones";
		        		tablaBotonesLocal.attr("id", tablaBotonesLocalId);
		        		jQuery('#'+tableId+'_BodyDiv').after(tablaBotonesLocal);
					}
				}									
				
				if (tablaPaginacionLocal != undefined){
					fixedHeaderTableHeight -= tablaPaginacionLocal.outerHeight(true);
				}
				if (tablaBotonesLocal != undefined){
					fixedHeaderTableHeight -= tablaBotonesLocal.outerHeight(true);
				}
				if (tablaPaginacionLocal == undefined && tablaBotonesLocal == undefined){
					var addMargin = true;
					try{
						if (fixedHeight != undefined && fixedHeight.substr(fixedHeight.length - 1) == "%"){
							var percent = fixedHeight.substring(0, fixedHeight.length - 1);
							if (!isNaN(percent)){							
								fixedHeaderTableHeight = fixedHeaderTableHeight * percent / 100;
								addMargin = false;
							}
						}
					} catch(e){}
					if (addMargin)
					fixedHeaderTableHeight -= 20;
				}
				//alert("scrolify con " + fixedHeaderTableHeight);
	            scrolify(oTable, fixedHeaderTableHeight);
	            if (clonarTablaPaginacion)
            		jQuery("#"+tablaPaginacionLocalId).remove();
            	if (clonarTablaBotones)
            		jQuery("#"+tablaBotonesLocalId).remove();
	            //var td = jQuery("#BodyDiv").find('tbody tr:eq(0) td');
	            //var th = jQuery("#HeaderDiv").find('thead tr th');
	            //alert("reajuste");
	            //alert(tableId + " despues scrolify...");
	            var nextUpperElement = undefined;
	            //alert("calculando posicion de body de tabla...");
	            var tblPosition = getElementAbsolutePos(jQuery('#'+tableId+'_BodyDiv')[0]);
	            //alert("posicion de body de tabla x:"+tblPosition.x+" y:"+tblPosition.y);
	            var nextUpperElementPosition = undefined;
	            if (tablaPaginacion != undefined && tablaBotones != undefined){
	            	var tablaPaginacionPosition = getElementAbsolutePos(tablaPaginacion[0]);
	            	var tablaBotonesPosition = getElementAbsolutePos(tablaBotones[0]);
	            	if (tablaPaginacionPosition.y < tablaBotonesPosition.y){
	            		//alert("recalculo con tablaPaginacionPosition x:" + tablaPaginacionPosition.x + " y:" + tablaPaginacionPosition.y);
	            		nextUpperElement = tablaPaginacion;
	            		nextUpperElementPosition = tablaPaginacionPosition;
	            	} else {
	            		//alert("recalculo con tablaBotonesPosition x:" + tablaBotonesPosition.x + " y:" + tablaBotonesPosition.y);
	            		nextUpperElement = tablaBotones;
	            		nextUpperElementPosition = tablaBotonesPosition;
	            	}					
				} else if (tablaPaginacion != undefined){					
					nextUpperElement = tablaPaginacion;
					nextUpperElementPosition = getElementAbsolutePos(tablaPaginacion[0]);
					//alert("recalculo con tablaPaginacionPosition x:" + nextUpperElementPosition.x + " y:" + nextUpperElementPosition.y);
				} else if (tablaBotones != undefined){
					nextUpperElement = tablaBotones;
					//alert("calculando posicion de tablaBotones");
					nextUpperElementPosition = getElementAbsolutePos(tablaBotones[0]);
					//alert("posicion de tablaBotones x:"+nextUpperElementPosition.x+" y:"+nextUpperElementPosition.y);
					//alert("recalculo con tablaBotonesPosition x:" + nextUpperElementPosition.x + " y:" + nextUpperElementPosition.y);
				}
	            //alert("comprobamos si recalculamos...");
	            //alert("tblPosition.y: " + tblPosition.y + " outerHeight: " + jQuery('#'+tableId+'_BodyDiv').outerHeight(true) + " nextUpperElementPosition.y: " + nextUpperElementPosition.y);
	            if (nextUpperElementPosition != undefined && (tblPosition.y + jQuery('#'+tableId+'_BodyDiv').outerHeight(true)) > nextUpperElementPosition.y){
	            	//alert("reajusto a " + (parseInt(nextUpperElementPosition.y) - parseInt(tblPosition.y)));
	            	jQuery('#'+tableId+'_BodyDiv').height(parseInt(nextUpperElementPosition.y) - parseInt(tblPosition.y));
	            }
    		}    		
    	}
    }
}

function getCurrentIFrame() {
    var iframes= parent.document.getElementsByTagName('iframe');
    for (var i= iframes.length; i-->0;) {
        var iframe= iframes[i];
        try {
            var idoc= 'contentDocument' in iframe? iframe.contentDocument : iframe.contentWindow.document;
        } catch (e) {
            continue;
        }
        if (idoc===document)
            return iframe;
    }
    return null;
}

function getElementIFrame(element) {
	var doc= element.ownerDocument;
	var win= 'defaultView' in doc? doc.defaultView : doc.parentWindow;
    var iframes= win.parent.document.getElementsByTagName('iframe');
    for (var i= iframes.length; i-->0;) {
        var iframe= iframes[i];
        try {
            var idoc= 'contentDocument' in iframe? iframe.contentDocument : iframe.contentWindow.document;
        } catch (e) {
            continue;
        }
        if (idoc===win.document)
            return iframe;
    }
    return null;
}

function __getIEVersion() {
    var rv = -1; // Return value assumes failure.
    if (navigator.appName == 'Microsoft Internet Explorer') {
        var ua = navigator.userAgent;
        var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
        if (re.exec(ua) != null)
            rv = parseFloat(RegExp.$1);
    }
    return rv;
}

function __getOperaVersion() {
    var rv = 0; // Default value
    if (window.opera) {
        var sver = window.opera.version();
        rv = parseFloat(sver);
    }
    return rv;
}

var __userAgent = navigator.userAgent;
var __isIE =  navigator.appVersion.match(/MSIE/) != null;
var __IEVersion = __getIEVersion();
var __isIENew = __isIE && __IEVersion >= 8;
var __isIEOld = __isIE && !__isIENew;

var __isFireFox = __userAgent.match(/firefox/i) != null;
var __isFireFoxOld = __isFireFox && ((__userAgent.match(/firefox\/2./i) != null) || (__userAgent.match(/firefox\/1./i) != null));
var __isFireFoxNew = __isFireFox && !__isFireFoxOld;

var __isWebKit =  navigator.appVersion.match(/WebKit/) != null;
var __isChrome =  navigator.appVersion.match(/Chrome/) != null;
var __isOpera =  window.opera != null;
var __operaVersion = __getOperaVersion();
var __isOperaOld = __isOpera && (__operaVersion < 10);

function __parseBorderWidth(width) {
    var res = 0;
    if (typeof(width) == "string" && width != null && width != "" ) {
        var p = width.indexOf("px");
        if (p >= 0) {
            res = parseInt(width.substring(0, p));
        }
        else {
     		//do not know how to calculate other values (such as 0.5em or 0.1cm) correctly now
    		//so just set the width to 1 pixel
            res = 1; 
        }
    }
    return res;
}


//returns border width for some element
function __getBorderWidth(element) {
	var res = new Object();
	res.left = 0; res.top = 0; res.right = 0; res.bottom = 0;
	if (window.getComputedStyle) {
		//for Firefox
		var elStyle = window.getComputedStyle(element, null);
		res.left = parseInt(elStyle.borderLeftWidth.slice(0, -2));  
		res.top = parseInt(elStyle.borderTopWidth.slice(0, -2));  
		res.right = parseInt(elStyle.borderRightWidth.slice(0, -2));  
		res.bottom = parseInt(elStyle.borderBottomWidth.slice(0, -2));  
	}
	else {
		//for other browsers
		res.left = __parseBorderWidth(element.style.borderLeftWidth);
		res.top = __parseBorderWidth(element.style.borderTopWidth);
		res.right = __parseBorderWidth(element.style.borderRightWidth);
		res.bottom = __parseBorderWidth(element.style.borderBottomWidth);
	}
   
	return res;
}


//returns the absolute position of some element within document
function getElementAbsolutePos(element) {
	var res = {};
	res.x = 0; res.y = 0;
	if (element !== null && element !== undefined) { 
		//use getBoundingClientRect function available in new browsers
		if (element.getBoundingClientRect) {
 	        var box = element.getBoundingClientRect();
		    
 		    var body = document.body;
 		    var docElem = document.documentElement;
 		    
 		    var scrollTop = window.pageYOffset || docElem.scrollTop || body.scrollTop;
 		    var scrollLeft = window.pageXOffset || docElem.scrollLeft || body.scrollLeft;
 		    
 		    var clientTop = docElem.clientTop || body.clientTop || 0;
 		    var clientLeft = docElem.clientLeft || body.clientLeft || 0;
   	    

		    res.x = Math.round(box.left + scrollLeft - clientLeft);
		    res.y = Math.round(box.top + scrollTop - clientTop);
		    
		}
		else { //for old browsers
			res.x = element.offsetLeft;
			res.y = element.offsetTop;
			
			var parentNode = element.parentNode;
			var borderWidth = null;

			while (offsetParent != null) {
				res.x += offsetParent.offsetLeft;
				res.y += offsetParent.offsetTop;
				
				var parentTagName = offsetParent.tagName.toLowerCase();	

				if ((__isIEOld && parentTagName != "table") || ((__isFireFoxNew || __isChrome) && parentTagName == "td")) {		    
					borderWidth = __getBorderWidth(offsetParent);
					res.x += borderWidth.left;
					res.y += borderWidth.top;
				}
				
				if (offsetParent != document.body && offsetParent != document.documentElement) {
					res.x -= offsetParent.scrollLeft;
					res.y -= offsetParent.scrollTop;
				}


				//next lines are necessary to fix the problem with offsetParent
				if (!__isIE && !__isOperaOld || __isIENew) {
					while (offsetParent != parentNode && parentNode !== null) {
						res.x -= parentNode.scrollLeft;
						res.y -= parentNode.scrollTop;
						if (__isFireFoxOld || __isWebKit) {
							borderWidth = __getBorderWidth(parentNode);
							res.x += borderWidth.left;
							res.y += borderWidth.top;
						}
						parentNode = parentNode.parentNode;
					}    
				}

				parentNode = offsetParent.parentNode;
				offsetParent = offsetParent.offsetParent;
			}
		}
		var frame = getElementIFrame(element);
		//alert(element.id + " x: "+res.x+" y:"+res.y);
		if (frame != null) {			
			var fp = getElementAbsolutePos(frame);
			//alert("sumando frame x:"+fp.x+ " y:"+fp.y);
			res.x += fp.x; 
			res.y += fp.y; 
		}
	}
	
    return res;
}
// BNS
// Máscara de datepicker
function datepickerMaskValueChanged(datepickerInput){
	var dateFormat = "dd/mm/yyyy";
	try{
		var dateFormat = datepickerInput.datepicker( "option", "dateFormat" );
		jQuery.datepicker.parseDate(dateFormat, datepickerInput.val(), null);
	} catch (e){
		datepickerInput.val("");
		alert("Introduzca una fecha válida: " + dateFormat);
	}
}

(function(jQuery){
	if (jQuery.datepicker){
		//TODO: DEFINIR EL RESTO DE LOS IDIOMAS
	   jQuery.datepicker.regional['es'] = {
	      closeText: 'Cerrar',
	      prevText: '<Ant',
	      nextText: 'Sig>',
	      currentText: 'Hoy',
	      monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	      monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
	      dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
	      dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
	      dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],
	      weekHeader: 'Sm',
	      dateFormat: 'dd/mm/yy',
	      firstDay: 1,
	      isRTL: false,
	      showMonthAfterYear: false,
	      yearSuffix: ''};
	   jQuery.datepicker.setDefaults(jQuery.datepicker.regional['es']);
	   
	   var clearText = "Borrar";
	   var old_fn = jQuery.datepicker._updateDatepicker;
	   jQuery.datepicker._updateDatepicker = function(inst) {
		   old_fn.call(this, inst);
		   var buttonPane = jQuery(this).datepicker("widget").find(".ui-datepicker-buttonpane");
		   jQuery("<button type='button' class='ui-datepicker-clean ui-state-default ui-priority-primary ui-corner-all'>"+clearText+"</button>").appendTo(buttonPane).click(function(ev) {
		   jQuery.datepicker._clearDate(inst.input);
		   		}) ;
	   		};
	}
})(jQuery);
var datepickerBtn = false;
function setDatepickerClearBtn(btnText){
	if (!datepickerBtn){
	var old_fn = jQuery.datepicker._updateDatepicker;
	jQuery.datepicker._updateDatepicker = function(inst) {
		   old_fn.call(this, inst);
		   var buttonPane = jQuery(this).datepicker("widget").find(".ui-datepicker-buttonpane");
		   if (buttonPane.find(".ui-datepicker-clean").lenght <= 0){
			   jQuery("<button type='button' class='ui-datepicker-clean ui-state-default ui-priority-primary ui-corner-all'>"+btnText+"</button>").appendTo(buttonPane).click(function(ev) {
			   jQuery.datepicker._clearDate(inst.input);
			   		}) ;
		   } else {
			   buttonPane.find(".ui-datepicker-clean").text(btnText);
		   }
	   	};
	   	datepickerBtn = true;
	}
}
fin();