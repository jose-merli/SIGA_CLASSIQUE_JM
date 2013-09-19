(function() {
	// Para los navegadores sin console
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
    
    // Para los navegadores que no soportan la función trim
    if(typeof String.prototype.trim !== 'function') {
    	  String.prototype.trim = function() {
    	    return this.replace(/^\s+|\s+$/g, ''); 
    	  };
	}
}());

if (window == window.top && window.jQuery){
	var jQueryTop = window.jQuery;
} else {
	var jQueryTop = window.top.jQueryTop;
}

if (typeof String.prototype.endsWith !== 'function') {
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
}

var jqueryFileUri = 	"/SIGA/html/js/jquery.js";
var jqueryUIfileUri = 	"/SIGA/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js";
var jqueryUICSSfileUri = "/SIGA/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css";

var defaultDateFormat = "dd/mm/yy";//FORMATO DATEPICKER SE CORRESPONDE CON dd/mm/yyyy: http://api.jqueryui.com/datepicker/#utility-formatDate 

var optionCargando = 	"<option>Cargando...</option>";

if (typeof jQuery == "undefined"){
	// Tratamos de usar jQuery de window.top
	try{
		var windowTop = window.top;
		if (typeof windowTop != "undefined" && typeof windowTop.jQuery != "undefined"){
				jQuery = windowTop.jQuery;	
		} /*else {
			alert("jquery no encontrado para "+window.location);
		}*/
	} catch(msg){
		//alert("CATCH ERROR: " + msg.message);
	}
}

if (typeof jQuery == "undefined"){
	// No se ha encontrado jQuery en top
	// Cargamos jQuery y esperamos a que este listo	(CALLBACK jQueryLoaded)
	var jqueryScript = document.createElement("script");
	jqueryScript.type = "text/javascript";
	jqueryScript.src = jqueryFileUri;
	var headElement = document.getElementsByTagName("head")[0];
	if (headElement.firstChild !== undefined){
		headElement.insertBefore(jqueryScript, headElement.firstChild);
	} else {
		headElement.appendChild(jqueryScript);
	}
	// Se establecen un número máximo de intentos
	// El primer intento espera un segundo y luego hace pool cada 500ms
	var maxIntentos = 38; // unos 20 segundos como máximo para dar error
	var intento = 0;
	var waitForLoad = function () {
	    if (typeof jQuery != "undefined") {
	    	// Inicializa script
	    	jQueryLoaded();
	    } else if (intento < maxIntentos){
	    	intento++;
	        window.setTimeout(waitForLoad, 500);
	    } else {
	    	alert("Se ha producido un error al cargar la página, por favor, intentelo de nuevo más tarde...");
	    }
	};
	window.setTimeout(waitForLoad, 1000);
} else {
	// Se ha encontrado jQuery en top. Inicializa script
	jQueryLoaded();
}


//JSON FOR COMPATIBILITY MODE ON IE
//http://cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.min.js
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();


/**
*	Inicialización una vez cargado jquery en el iframe
*	Esta función ya incluye un método de onload de jquery
*	por lo que cualquier cosa que se tenga que añadir en el
*	onload debería estár aquí
*	
**/
function jQueryLoaded(){
	console.debug("[jQueryLoaded] inicializando contexto jQuery...");
	// Inicialización de jquery para el contexto actual
	window.top.jQueryContext(window);
	// Con este método se inicializa otra vez jquery UI dentro del jquery local de esta página pero
	// esto no evita los memory leaks de jquery UI por lo que voy a intentar cargarlo solo en el TOP
	// y usarlo desde allí.
	//window.top.jQueryUiContext(window.jQuery);
	
	// *** BEGIN PLUGINS *** //
	// EXISTS FUNCTION
	jQuery.fn.exists = function(){return this.length>0;};	
	
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
	*	@version    $Id: SIGA.js,v 1.100 2013-09-19 15:55:47 carlos Exp $
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
	/*
	(function(e){function t(){var e=document.createElement("input"),t="onpaste";return e.setAttribute(t,""),"function"==typeof e[t]?"paste":"input"}var n,a=t()+".mask",r=navigator.userAgent,i=/iphone/i.test(r),o=/android/i.test(r);e.mask={definitions:{9:"[0-9]",a:"[A-Za-z]","*":"[A-Za-z0-9]"},dataName:"rawMaskFn",placeholder:"_"},e.fn.extend({caret:function(e,t){var n;if(0!==this.length&&!this.is(":hidden"))return"number"==typeof e?(t="number"==typeof t?t:e,this.each(function(){this.setSelectionRange?this.setSelectionRange(e,t):this.createTextRange&&(n=this.createTextRange(),n.collapse(!0),n.moveEnd("character",t),n.moveStart("character",e),n.select())})):(this[0].setSelectionRange?(e=this[0].selectionStart,t=this[0].selectionEnd):document.selection&&document.selection.createRange&&(n=document.selection.createRange(),e=0-n.duplicate().moveStart("character",-1e5),t=e+n.text.length),{begin:e,end:t})},unmask:function(){return this.trigger("unmask")},mask:function(t,r){var c,l,s,u,f,h;return!t&&this.length>0?(c=e(this[0]),c.data(e.mask.dataName)()):(r=e.extend({placeholder:e.mask.placeholder,completed:null},r),l=e.mask.definitions,s=[],u=h=t.length,f=null,e.each(t.split(""),function(e,t){"?"==t?(h--,u=e):l[t]?(s.push(RegExp(l[t])),null===f&&(f=s.length-1)):s.push(null)}),this.trigger("unmask").each(function(){function c(e){for(;h>++e&&!s[e];);return e}function d(e){for(;--e>=0&&!s[e];);return e}function m(e,t){var n,a;if(!(0>e)){for(n=e,a=c(t);h>n;n++)if(s[n]){if(!(h>a&&s[n].test(R[a])))break;R[n]=R[a],R[a]=r.placeholder,a=c(a)}b(),x.caret(Math.max(f,e))}}function p(e){var t,n,a,i;for(t=e,n=r.placeholder;h>t;t++)if(s[t]){if(a=c(t),i=R[t],R[t]=n,!(h>a&&s[a].test(i)))break;n=i}}function g(e){var t,n,a,r=e.which;8===r||46===r||i&&127===r?(t=x.caret(),n=t.begin,a=t.end,0===a-n&&(n=46!==r?d(n):a=c(n-1),a=46===r?c(a):a),k(n,a),m(n,a-1),e.preventDefault()):27==r&&(x.val(S),x.caret(0,y()),e.preventDefault())}function v(t){var n,a,i,l=t.which,u=x.caret();t.ctrlKey||t.altKey||t.metaKey||32>l||l&&(0!==u.end-u.begin&&(k(u.begin,u.end),m(u.begin,u.end-1)),n=c(u.begin-1),h>n&&(a=String.fromCharCode(l),s[n].test(a)&&(p(n),R[n]=a,b(),i=c(n),o?setTimeout(e.proxy(e.fn.caret,x,i),0):x.caret(i),r.completed&&i>=h&&r.completed.call(x))),t.preventDefault())}function k(e,t){var n;for(n=e;t>n&&h>n;n++)s[n]&&(R[n]=r.placeholder)}function b(){x.val(R.join(""))}function y(e){var t,n,a=x.val(),i=-1;for(t=0,pos=0;h>t;t++)if(s[t]){for(R[t]=r.placeholder;pos++<a.length;)if(n=a.charAt(pos-1),s[t].test(n)){R[t]=n,i=t;break}if(pos>a.length)break}else R[t]===a.charAt(pos)&&t!==u&&(pos++,i=t);return e?b():u>i+1?(x.val(""),k(0,h)):(b(),x.val(x.val().substring(0,i+1))),u?t:f}var x=e(this),R=e.map(t.split(""),function(e){return"?"!=e?l[e]?r.placeholder:e:void 0}),S=x.val();x.data(e.mask.dataName,function(){return e.map(R,function(e,t){return s[t]&&e!=r.placeholder?e:null}).join("")}),x.attr("readonly")||x.one("unmask",function(){x.unbind(".mask").removeData(e.mask.dataName)}).bind("focus.mask",function(){clearTimeout(n);var e;S=x.val(),e=y(),n=setTimeout(function(){b(),e==t.length?x.caret(0,e):x.caret(e)},10)}).bind("blur.mask",function(){y(),x.val()!=S&&x.change()}).bind("keydown.mask",g).bind("keypress.mask",v).bind(a,function(){setTimeout(function(){var e=y(!0);x.caret(e),r.completed&&e==x.val().length&&r.completed.call(x)},0)}),y()}))}})})(jQuery);		
	*/
	// jQuery Mask Plugin v1.1.2
	// github.com/igorescobar/jQuery-Mask-Plugin
	(function(b){var u=function(a,g,f){var c=this;a=b(a);var l;g="function"==typeof g?g(a.val(),f):g;c.init=function(){f=f||{};c.byPassKeys=[8,9,37,38,39,40,46];c.translation={0:{pattern:/\d/},9:{pattern:/\d/,optional:!0},A:{pattern:/[a-zA-Z0-9]/},S:{pattern:/[a-zA-Z]/}};c.translation=b.extend({},c.translation,f.translation);c=b.extend(!0,{},c,f);a.each(function(){a.attr("maxlength",g.length).attr("autocomplete","off");d.destroyEvents();d.events();d.isInput()||d.val(d.getMasked())})};var d={events:function(){a.on("keydown.mask",
	function(){l=d.val()});a.on("keyup.mask",d.behaviour);a.on("paste.mask",function(){setTimeout(function(){a.keydown().keyup()},100)})},destroyEvents:function(){a.off("keydown.mask").off("keyup.mask").off("paste.mask")},isInput:function(){return"input"===a.get(0).tagName.toLowerCase()},val:function(s){return 0<arguments.length?d.isInput()?a.val(s):a.text(s):d.isInput()?a.val():a.text()},behaviour:function(a){a=a||window.event;if(-1===b.inArray(a.keyCode||a.which,c.byPassKeys))return d.val(d.getMasked()),
	d.callbacks(a)},getMasked:function(){var a=[],b=d.val(),e=0,t=g.length,h=0,l=b.length,k=1,m="push",n;f.reverse?(m="unshift",k=-1,e=t-1,h=l-1,n=function(){return-1<e&&-1<h}):n=function(){return e<t&&h<l};for(;n();){var p=g.charAt(e),q=c.translation[p],r=b.charAt(h);q?(r.match(q.pattern)?(a[m](r),e+=k):!0==q.optional&&(e+=k,h-=k),h+=k):(a[m](p),r==p&&(h+=k),e+=k)}return a.join("")},callbacks:function(b){var c=d.val(),e=d.val()!==l;if(!0===e&&"function"==typeof f.onChange)f.onChange(c,b,a,f);if(!0===
	e&&"function"==typeof f.onKeyPress)f.onKeyPress(c,b,a,f);if("function"===typeof f.onComplete&&c.length===g.length)f.onComplete(c,b,a,f)}};c.remove=function(){d.destroyEvents();d.val(c.getCleanVal()).removeAttr("maxlength")};c.getCleanVal=function(){for(var a=[],b=d.val(),e=0,f=g.length;e<f;e++)c.translation[g.charAt(e)]&&a.push(b.charAt(e));return a.join("")};c.init()};b.fn.mask=function(a,g){return this.each(function(){b(this).data("mask",new u(this,a,g))})};b.fn.unmask=function(){return this.each(function(){b(this).data("mask").remove()})};
	b("input[data-mask]").each(function(){b(this).mask(b(this).attr("data-mask"))})})(window.jQuery||window.Zepto);
	
	/*
	A simple jQuery function that can add listeners on attribute change.
	http://meetselva.github.io/attrchange/

	About License:
	Copyright (C) 2013 Selvakumar Arumugam
	You may use attrchange plugin under the terms of the MIT Licese.
	https://github.com/meetselva/attrchange/blob/master/MIT-License.txt
	*/
	(function($) {
	   function isDOMAttrModifiedSupported() {
			var p = document.createElement('p');
			var flag = false;
			
			if (p.addEventListener) p.addEventListener('DOMAttrModified', function() {
				flag = true;
			}, false);
			else if (p.attachEvent) p.attachEvent('onDOMAttrModified', function() {
				flag = true;
			});
			else return false;
			
			p.setAttribute('id', 'target');
			
			return flag;
	   }
	   
	   function checkAttributes(chkAttr, e) {
			if (chkAttr) {
				var attributes = this.data('attr-old-value');
				
				if (e.attributeName.indexOf('style') >= 0) {
					if (!attributes['style']) attributes['style'] = {}; //initialize
					var keys = e.attributeName.split('.');
					e.attributeName = keys[0];
					e.oldValue = attributes['style'][keys[1]]; //old value
					e.newValue = keys[1] + ':' + this.prop("style")[$.camelCase(keys[1])]; //new value
					attributes['style'][keys[1]] = e.newValue;
				} else {
					e.oldValue = attributes[e.attributeName];
					e.newValue = this.attr(e.attributeName);
					attributes[e.attributeName] = e.newValue; 
				}
				
				this.data('attr-old-value', attributes); //update the old value object
			}	   
	   }

	   //initialize Mutation Observer
	   var MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

	   $.fn.attrchange = function(o) {
		   
			var cfg = {
				trackValues: false,
				callback: $.noop
			};
			
			//for backward compatibility
			if (typeof o === "function" ) { 
				cfg.callback = o; 
			} else { 
				$.extend(cfg, o); 
			}

		    if (cfg.trackValues) { //get attributes old value
		    	$(this).each(function (i, el) {
		    		var attributes = {};
		    		for (var attr, i=0, attrs=el.attributes, l=attrs.length; i<l; i++){
		    		    attr = attrs.item(i);
		    		    attributes[attr.nodeName] = attr.value;
		    		}
		    		
		    		$(this).data('attr-old-value', attributes);
		    	});
		    }
		   
			if (MutationObserver) { //Modern Browsers supporting MutationObserver
				/*
				   Mutation Observer is still new and not supported by all browsers. 
				   http://lists.w3.org/Archives/Public/public-webapps/2011JulSep/1622.html
				*/
				var mOptions = {
					subtree: false,
					attributes: true,
					attributeOldValue: cfg.trackValues
				};
		
				var observer = new MutationObserver(function(mutations) {
					mutations.forEach(function(e) {
						var _this = e.target;
						
						//get new value if trackValues is true
						if (cfg.trackValues) {
							/**
							 * @KNOWN_ISSUE: The new value is buggy for STYLE attribute as we don't have 
							 * any additional information on which style is getting updated. 
							 * */
							e.newValue = $(_this).attr(e.attributeName);
						}
						
						cfg.callback.call(_this, e);
					});
				});
		
				return this.each(function() {
					observer.observe(this, mOptions);
				});
			} else if (isDOMAttrModifiedSupported()) { //Opera
				//Good old Mutation Events but the performance is no good
				//http://hacks.mozilla.org/2012/05/dom-mutationobserver-reacting-to-dom-changes-without-killing-browser-performance/
				return this.on('DOMAttrModified', function(event) {
					if (event.originalEvent) event = event.originalEvent; //jQuery normalization is not required for us 
					event.attributeName = event.attrName; //property names to be consistent with MutationObserver
					event.oldValue = event.prevValue; //property names to be consistent with MutationObserver 
					cfg.callback.call(this, event);
				});
			} else if ('onpropertychange' in document.body) { //works only in IE		
				return this.on('propertychange', function(e) {
					if (window.event && window.event.propertyName)
						e.attributeName = window.event.propertyName;
					//to set the attr old value
					checkAttributes.call($(this), cfg.trackValues , e);
					cfg.callback.call(this, e);
				});
			}

			return this;
	    };
	})(jQuery);
	
	// SERIALIZE
	jQuery.fn.serializeObject = function() {
	    var o = {};
	    // Para poder enviar los elementos sin name pero con ID. Si no tienen ni name ni ID se envía un array 'desconocido'
	    this.each(function(){
	    	if (typeof jQuery(this).attr("name") == "undefined" || jQuery(this).attr("name") == ""){
	    		if (typeof jQuery(this).attr("id") !== "undefined" && jQuery(this).attr("id") !== ""){
	    			jQuery(this).attr("name", jQuery(this).attr("id"));
	    		} else {
	    			jQuery(this).attr("name", "desconocido");
	    		}
	    	}
		});
	    var a = this.serializeArray();
	    jQuery.each(a, function() {
	    	var key = undefined;
	    	if (typeof this.name !== "undefined" && this.name !== "")
	    		key = this.name.toLowerCase();
	    	else if (typeof this.id !== "undefined" && this.id !== "")
    			key = this.id.toLowerCase();
	    	if (typeof key == "undefined")
	    		key = "desconocido";
	    	if (o[this.name] !== undefined){
	    		var old_value = o[this.name];
	    		delete o[this.name];
	    		o[key] = old_value;
	    	}
	    	var value = o[key];
	    	if (value !== undefined && value != "" && value != " " && value != "-1" && new String(value).trim() != ""){
	            if (!o[key].push) {
	                o[key] = [value];
	            }
	            o[key].push(this.value || '');
	        } else {
	            o[key] = this.value || '';
	        }
	    });
	    return o;
	};		
	
	//*** END PLUGINS *** //
	
	if (window == window.top){
		// NO LO CARGAMOS TIENE QUE ESTAR CARGADO EN EL WINDOW TOP!
		//cargarJqueryUI(window);
		jQueryUILoaded();
	}
	
	/*  Hack for allowing correct typing in modal dialogs in safari. */
	/* jQuery.browser DEPRECATED
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
	*/
	
	//*** ONLOAD ***//
	
	jQuery(document).ready(function(){
		if (jQuery("table.tablaLineaPestanasArriba").length>0){
			jQuery("table.tablaLineaPestanasArriba").css("float", "left");
		}
		if (jQuery("table.pest").length>0){
			jQuery("table.pest").css("float", "left");
		}
		if(jQuery.fn.mask){
			// Descomentar para no permitir la edición de fechas por texto
			//jQuery("input.datepicker").attr("readonly", "readonly");
			// Comentar para no permitir la edición de fechas por texto	
			//jQuery("input.datepicker").not(".boxConsulta").mask('99/99/9999',{completed:function(){datepickerMaskValueChanged(jQueryTop(this, window.document));}});
			jQuery("input.datepicker").not(".boxConsulta").each(function(){
				//var inputDatepicker = jQueryTop(this, window.document);
				var inputDatepicker = jQuery(this);
				inputDatepicker.mask('YY/YY/YYYY',{'translation': {            
	                Y: {pattern: /[0-9]/}
	              },
	              onComplete: function(cep){datepickerMaskValueChanged(inputDatepicker);}
	              });
				inputDatepicker.on("blur", function(){datepickerMaskValueChanged(jQuery(this));});
			});
		}
		jQuery("input.datepicker").each(function(){
			if (jQuery(this).data("cargarfechadesde")){
				jQuery(this).val(jQuery('#'+jQuery(this).data("cargarfechadesde")).val());
			}
			if (jQuery(this).hasClass("editable")){
				//console.debug("DATEPICKER: " + jQuery(this).attr("id") + " VALUE: " + jQuery(this).val());
				if (false && window == window.top){//DESACTIVAMOS EL DATEPICKER NORMAL PARA QUE TODOS FUNCINEN IGUAL
					//console.debug("DATEPICKER: Está en el top, construimos datepicker normal");
					jQueryTop(this, this.ownerDocument).datepicker({
						dateFormat: jQuery(this).data("datepickerformat"),
						regional: jQuery(this).data("regional"),
						onSelect: function(dateText, datePickerInstance){
							var fireOnChange = false;
							if (datepickerInput.val() !== dateText){
								fireOnChange = true;
							}
							datepickerInput.val(dateText);
							if (fireOnChange)
								datepickerInput.change();
						},
						'beforeShow': function(input, inst) {
							jQueryTop('.menu-overlay').height(jQueryTop(document).height());
							jQueryTop('.menu-overlay').toggle();
							},
						'onClose': function(dateText, inst) {
							jQueryTop('.menu-overlay').toggle();
						}
					}).keydown(function(e) {
						if(e.keyCode == 8 || e.keyCode == 46) {
							jQueryTop.datepicker._clearDate(this);
							return false;
						}
					});
				} else {
					//console.debug("DATEPICKER: NO está en el top, construimos datepicker dialog");
					jQuery(this).after('<img id="'+jQuery(this).attr("id")+'-datepicker-trigger" class="siga-datepicker-trigger" style="cursor:pointer;" src="/SIGA/html/imagenes/calendar.gif" alt="..." title="...">');
					var datepickerInput = jQueryTop(this, this.ownerDocument);
					var self = jQuery(this);
					/*
					datepickerInput.keydown(function(e) {
						if(e.keyCode == 8 || e.keyCode == 46) {
							jQueryTop.datepicker._clearDate(this);
							return false;
						}
					});
					*/
					jQuery("#"+jQuery(this).attr("id")+'-datepicker-trigger').on("click", function(e){
						var options = jQueryTop.datepicker.regional[datepickerInput.data("regional")];
						options.dateFormat = datepickerInput.data("datepickerformat");
						options.beforeShow = function(input, inst) {
							//console.debug("[DATEPICKER] beforeShow");							
							jQueryTop('#main_overlay').show();
							jQueryTop("input[id^=dp]").hide();
						};
						options.onClose = function(dateText, inst) {
							//console.debug("[DATEPICKER] onClose");
							jQueryTop('#main_overlay').hide();
						};
						options.regional = datepickerInput.data("regional");
						options.draggable = true;
						if (typeof jQueryTop.datepicker._curInst != "undefined" && jQueryTop.datepicker._curInst != null){
							jQueryTop.datepicker._destroyDatepicker(jQueryTop.datepicker._curInst);
						}
						datepickerInput.datepicker("dialog",
								formatDate(datepickerInput.val(),datepickerInput.data("datepickerformat")),
								function(dateText, datePickerInstance){
									var fireOnChange = false;
									if (datepickerInput.val() !== dateText){
										fireOnChange = true;
									}
									datepickerInput.val(dateText);
									if (fireOnChange)
										datepickerInput.change();
								},
								options);
						var vContainment = "#mainWorkArea";
						if (jQueryTop(vContainment).length <= 0){
						   if (jQueryTop("#modal").length > 0)
							   vContainment = "#modal";
						   else
							   vContainment = "#main_overlay";
						}
						/*
						vContainment = [];
						vContainment.push(0);//x1
						vContainment.push(0);//y1
						var winW = 630, winH = 460;
						if (document.body && document.body.offsetWidth) {
						 winW = document.body.offsetWidth;
						 winH = document.body.offsetHeight;
						}
						if (document.compatMode=='CSS1Compat' &&
						    document.documentElement &&
						    document.documentElement.offsetWidth ) {
						 winW = document.documentElement.offsetWidth;
						 winH = document.documentElement.offsetHeight;
						}
						if (window.innerWidth && window.innerHeight) {
						 winW = window.innerWidth;
						 winH = window.innerHeight;
						}
						vContainment.push(winW);//x2
						vContainment.push(winH);//y2
						alert("vContainment: "+vContainment);
						*/
						jQueryTop("#ui-datepicker-div").draggable({ containment: jQueryTop(vContainment).contents().find("html"), scroll: false, snap: true });
						jQueryTop("#main_overlay").on("click", function(e){
							datepickerInput.datepicker("destroy");
						});
					});
				}
			}
		});
		if (window == window.top){
		// MouseWheel
		   jQueryTop(document).on("mousewheel", "#ui-datepicker-div", function(e){
			   try{
				   if (typeof jQueryTop.datepicker._curInst != "undefined" && jQueryTop.datepicker._curInst != null){
					   var currentDatepicker = jQueryTop.datepicker._curInst;
					   var offset = 0;
					   var type = "M";
			   			if(e.wheelDelta/120 > 0) {
			   	            //prev
			   				offset = -1;		   
			   	        } else{
			   	            //next
			   	        	offset = 1;
			   	        }
			   			if (jQueryTop(this).find("#datepicker-change-year:focus").length > 0)
			   				type = "Y";
			   			if (offset != 0){
				   			jQueryTop.datepicker._adjustInstDate(currentDatepicker, offset, type);
				   			jQueryTop.datepicker._updateDatepicker(currentDatepicker);
				   			if (type == "Y")
				   				jQueryTop(this).find("#datepicker-change-year").focus();
			   			}
				   }
			   } catch(e){}
	   		});
		}
		// iFRAMEs POST
		/*
		jQuery("iframe").each(function(){
			console.debug("[REEMPLAZO IFRAMEs] id: "+jQuery(this).attr("id") + "; name: " + jQuery(this).attr("name"));
			var iFrame = jQuery(this);
			var iFrameId = iFrame.attr("id");
			var iFrameName = iFrame.attr("name");
			var iFrameSrc = jQuery(this).attr("src");
			iFrame.replaceWith("<div data-iframe='true' id="+iFrameId+" name="+iFrameName+" class="+iFrame.attr("class")+"></div>");
			if (iFrameSrc){
				console.debug("[REEMPLAZO IFRAMEs] Frame("+iFrameId + "; " + iFrameName+"): URL=" + iFrameSrc);
				var divIframeSelector;
				if (iFrameId){
					divIframeSelector = "div#"+iFrameId;
				} else if (iFrameName){
					divIframeSelector = "div[name='"+iFrameId+"']";
				}
				if (divIframeSelector)
					jQuery(divIframeSelector).load(iFrameSrc);
			}
		});
		jQuery(document).on("submit", "form", function(event){
			var submit = true;
			var form = jQuery(this);
			console.debug("[FORM SUBMIT] " + form.attr("id") + " to " + form.action + " in " + form.attr("target"));
			if (form.attr("target")){
				var target = form.attr("target");
				var iFrame = jQuery("iframe#"+target+",div#"+target+", iframe[name='"+target+"'], div[name='"+target+"']").filter(function(){
					return jQuery(this).is("iframe") || jQuery(this).data("iframe");
				});
				if (iFrame.exists()){
					ajaxSubmit(form, iFrame);
					submit = false;
				}
			}
			if (submit)
				form.submit();
			else {
				event.preventDefault();
				return false;
			}
		});		
		*/
		// TAG SELECT BEGIN
		//console.debug("div.tagSelectDiv: " + jQuery("div.tagSelectDiv").length + " EN " + window.location);
		jQuery("div.tagSelectDiv").each(function(){
			//console.debug("--- BEGIN tagSelectDiv ---");			
			var tagSelect_select = jQuery(this).find("select");
			if (tagSelect_select.exists()){
				var tagSelect_input = jQuery(this).find("input.tagSelect_disabled");
				var tagSelect_searchBox = jQuery(this).find("input.tagSelect_searchBox");
				// INPUT PARA ESTADO DISABLED/READONLY
				if (tagSelect_input.exists()){
					tagSelect_select.on("change",function(){
						var optionSelected = jQuery(this).find('option:selected');
						if (optionSelected.val() != "")
							jQuery("#"+jQuery(this).attr("id")+"_disabled").val(optionSelected.text());
					});
					
					//OJO: NO FUNCIONA EN IE AL AÑADIR EL ATTRIBUTO DISABLED/READONLY PERO SI AL ELIMINARLO POR ELLO
					jQuery(this).attrchange({
						callback:function(event){
							tagSelectDivAttrChangeCallback(event, jQuery(this));
						}
					});
					tagSelect_select.attrchange({
						callback:function(event){
									tagSelectAttrChangeCallback(event, jQuery(this));
								}
					});		
				}
				
				//PAGINACION
				if (tagSelect_select.data("paginated")){
					console.debug("[tagSelect] construyendo paginación...");					
					var paginatedDiv = jQuery(this).find("div.tagSelectPaginatedDiv");					
					var page = tagSelect_select.data("page");
					var pageSize = tagSelect_select.data("pagesize");
					
					//posicionar
					var winW = 630, winH = 460;
					if (document.body && document.body.offsetWidth) {
					 winW = document.body.offsetWidth;
					 winH = document.body.offsetHeight;
					}
					if (document.compatMode=='CSS1Compat' &&
					    document.documentElement &&
					    document.documentElement.offsetWidth ) {
					 winW = document.documentElement.offsetWidth;
					 winH = document.documentElement.offsetHeight;
					}
					if (window.innerWidth && window.innerHeight) {
					 winW = window.innerWidth;
					 winH = window.innerHeight;
					}
					//Ajuste por barra scroll
					paginatedDiv.width(paginatedDiv.width() + 25 + "px");
					console.debug("[tagSelect] Dimensiones window width: " + winW + "px; height: " + winH + "px");
					console.debug("[tagSelect] Dimensiones paginatedDiv con left: " + paginatedDiv.offset().left + "px; width: " + paginatedDiv.width() + "px; top: " +paginatedDiv.offset().top+ "px; height: " +paginatedDiv.height()+ "px");
					if (paginatedDiv.offset().left + paginatedDiv.width() > (winW + 20)){
						var ajusteW = paginatedDiv.offset().left + paginatedDiv.width() - winW;
						if (paginatedDiv.width() > winW || paginatedDiv.offset().left < ajusteW){
							var widthMaxPercent = winW - (winW * 70 / 100);
							paginatedDiv.css("left","0px");
							paginatedDiv.height(widthMaxPercent+"px");
							console.debug("[tagSelect] Ajustando paginatedDiv a left: 0px y height: " + widthMaxPercent + "px");
						} else {
							var ajusteLeft = paginatedDiv.offset().left - ajusteW;
							paginatedDiv.css("left",ajusteLeft + "px");
							console.debug("[tagSelect] Ajustando paginatedDiv a left: " + ajusteLeft + "px");
						}
					}
					if (paginatedDiv.offset().top + paginatedDiv.height() > (winH + 20)){
						var ajusteH = paginatedDiv.offset().top + paginatedDiv.height() - winH;
						if (paginatedDiv.height() > winH || paginatedDiv.offset().top < ajusteH){
							var heightMaxPercent = winH - (winH * 70 / 100);
							paginatedDiv.css("top","0px");							
							paginatedDiv.height(heightMaxPercent+"px");
							console.debug("[tagSelect] Ajustando paginatedDiv a top: 0px y height: " + heightMaxPercent + "px");
						} else {
							var ajusteTop = paginatedDiv.offset().top - ajusteH;
							paginatedDiv.css("top",ajusteTop + "px");
							console.debug("[tagSelect] Ajustando paginatedDiv a top: " + ajusteTop + "px");
						}
					}
					
					jQueryTop("#"+tagSelect_select.attr("id")+"_paginated_div", window.document).position({
						"my": "center",
						"at": "center",
						"of": jQueryTop("#"+tagSelect_select.attr("id")+"_tagSelectDiv", window.document),
						"collision": "flipfit"
					});
					
					jQuery(document).on("hover", "span.selectOptionText", function(){
						jQuery("span.selectOptionText").css("background-color", "white");
						jQuery(this).css("background-color", "blue");
					});
					paginatedDiv.on("click", "li.selectOption", function(){
						console.debug("[tagSelect] Option click");
						var tagSelectSelect = jQuery(this).parent().parent().parent().find("select");						
						jQuery(this).parent().parent().find("li.selected").each(function(){
							jQuery(this).removeClass("selected");
							jQuery(this).addClass("notSelected");
						});
						jQuery(this).addClass("selected");
						jQuery(this).removeClass("notSelected");
						if (tagSelectSelect.find("option[value='"+jQuery(this).data("key")+"']").length <= 0){
							tagSelectSelect.prepend("<option value='"+jQuery(this).data("key")+"'>"+jQuery(this).find("span").text()+"</option>");
						}
						tagSelectSelect.val(jQuery(this).data("key"));
						console.debug("[tagSelect] selectOption ID: " + tagSelectSelect.attr("id") + " KEY: " + jQuery(this).data("key") + " VAL: " + tagSelectSelect.val());
						tagSelectMostrarDesplegable(tagSelectSelect.attr("id"), false);	
						return false;
					});
					
					tagSelect_select.on("mousedown", function(e){
						console.debug("[tagSelect] Select mousedown: " + jQuery(this).attr("id"));
						tagSelectMostrarDesplegable(jQuery(this).attr("id"), true);
						return false;
					});
					
					jQuery(document).on("click", function(){
						console.debug("document.click...");
						jQuery("div.tagSelectPaginatedDiv.open").each(function(){
							var id = jQuery(this).parent().find("select").attr("id");
							console.debug("[tagSelect] div.tagSelectPaginatedDiv.open OCULTAR POR CLICK EN DOCUMENT ID: " + id);							
							tagSelectMostrarDesplegable(id, false);					
						});
					});
					paginatedDiv.bind('scroll', function() {
                              if(jQuery(this).scrollTop() + jQuery(this).innerHeight() >= jQuery(this)[0].scrollHeight) {
                            	  tagSelectLoadNextPage(jQuery(this).parent().find("select"));
                              }
                    });					
				}
				
				// INPUT PARA BUSCAR/FILTRAR EL SELECT
				if (tagSelect_searchBox.exists()){		
					tagSelect_select.on("change",function(){
						jQuery("#"+jQuery(this).attr("id")+"_searchBox").val(jQuery(this).find('option:selected').data("searchkey"));
					});
					var searchBoxKeyupTimeOut = false;
					tagSelect_searchBox.on("keyup",function(){
						console.debug("[searchBox] KEYUP ENVENT");						
						if (searchBoxKeyupTimeOut)
							clearTimeout(searchBoxKeyupTimeOut);
						var el = jQuery(this);
						searchBoxKeyupTimeOut = setTimeout(function(){
							tagSelect_searchBox_keyup(el);
							}, 1000);
						/*
						var searchBox_select = jQuery(this).parent().find("select.tagSelect");
						var old_selected_value = searchBox_select.find("option:selected").val();
						
						tagSelect_search(searchBox_select, jQuery(this));
						
						var current_selectedValue = searchBox_select.find("option:selected").val();
						console.debug(searchBox_select.attr("id") + " keyup selected value OLD: " + old_selected_value + " NEW: "+ current_selectedValue);
						if (old_selected_value != current_selectedValue && typeof searchBox_select.data("childrenids") != "undefined"){
							console.debug(searchBox_select.attr("id") + " load children...");
							var reloadIds = searchBox_select.data("childrenids").split(",");
							jQuery.each(reloadIds, function(index, childrenId){
								if (typeof current_selectedValue != "undefined" && current_selectedValue != "-1" && current_selectedValue != ""){
									loadSelect(searchBox_select,childrenId);
								} else {
									//console.debug("OnChange empty parent value");
									jQuery("#"+childrenId).html("");
									if (typeof jQuery("#"+childrenId).data("selectparentmsg") != "undefined"){
										jQuery("#"+childrenId).prepend("<option value='' selected>"+jQuery("#"+childrenId).data("selectparentmsg")+"</option>");
									} else if (typeof jQuery("#"+childrenId).data("required") == "undefined" && jQuery("#"+childrenId).data("required") != "true") {
										jQuery("#"+childrenId).prepend("<option value='' selected></option>");
									}
								}
							});
						}
						*/
					}).on("change",function(){
						console.debug("[searchBox] CHANGE ENVENT");
						if (searchBoxKeyupTimeOut)
							clearTimeout(searchBoxKeyupTimeOut);
						tagSelect_search(jQuery(this).parent().find("select.tagSelect"), jQuery(this));
					}).on("blur", function(){
						console.debug("[searchBox] BLUR ENVENT");
						//var selected_value = tagSelect_select.find("option:selected").val();
						//if (typeof selected_value != "undefined" && selected_value != "" && selected_value != "-1" && selected_value != null)
						if (searchBoxKeyupTimeOut)
							clearTimeout(searchBoxKeyupTimeOut);
						jQuery(this).parent().find("select.tagSelect").change();
					});
					if (tagSelect_select.find("option:selected").exists()){
						tagSelect_searchBox.val(tagSelect_select.find('option:selected').data("searchkey"));
					}
				}
				
				if (typeof tagSelect_select.data("childrenids") != "undefined"){
					// TIENE HIJOS, ES UN SELECT ANIDADO
					//console.debug(tagSelect_select.attr("id") + " TIENE HIJOS: "+ childrenIds);
					tagSelect_select.on("change",function(){
						var parentSelect = jQuery(this);
						var parentValue = jQuery(this).find("option:selected").val();
						jQuery.each(jQuery(this).data("childrenids").split(","), function(index, childrenId){
							if (typeof parentValue != "undefined" && parentValue != "-1" && parentValue != ""){
								loadSelect(parentSelect,childrenId);
							} else {
								//console.debug("OnChange empty parent value");
								jQuery("#"+childrenId).html("");
								if (typeof jQuery("#"+childrenId).data("selectparentmsg") != "undefined"){
									jQuery("#"+childrenId).prepend("<option value='' selected>"+jQuery("#"+childrenId).data("selectparentmsg")+"</option>");
								} else if (typeof jQuery("#"+childrenId).data("required") == "undefined" && jQuery("#"+childrenId).data("required") != "true") {
									jQuery("#"+childrenId).prepend("<option value='' selected></option>");
								}
								jQuery("#"+childrenId).change();
							}
							if (jQuery("#"+childrenId).parent().find("input.tagSelect_searchBox").exists()){
								if (jQuery("#"+childrenId).find('option:selected').exists())
									jQuery("#"+childrenId).parent().find("input.tagSelect_searchBox").val(jQuery("#"+childrenId).find('option:selected').data("searchkey"));
								else
									jQuery("#"+childrenId).parent().find("input.tagSelect_searchBox").val("");
							}
						});
					});
				} else {
					//console.debug(tagSelect_select.attr("id") + " NO TIENE HIJOS!");
				}
				// Inicializa hijos si hay valor en los padres y no estaba
				// ya inicializado por TAGSELECT con los parámetros de la request
				if (tagSelect_select.children().length <= 0){
					//console.debug(tagSelect_select.attr("id")+" NO TIENE OPTIONS CARGADAS, COMPROBANDO SI HAY QUE INICIALIZARLO...");
					var parentSelects = jQuery("select[data-childrenIds*='" + tagSelect_select.attr("id")+"']");
					if (parentSelects.length > 0 && 
							parentSelects.filter(function() { return jQuery(this).val() == "" || typeof jQuery(this).val() == "undefined" || jQuery(this).val() == "-1"; }).length <= 0){
						//console.debug("ES UN HIJO! INICIALIZANDOLO...");
						//Tiene padres y todos tienen valor
						loadSelect(parentSelects, tagSelect_select.attr("id"), true);
					} else {
						//console.debug("NO ES UN HIJO!");
					}
				}
			}
			setTagselectDivWidth(jQuery(this));
		});
		// TAG SELECT END
	}); // READY
	
} // FIN JQUERY LOADED

function tagSelectMostrarDesplegable(id, mostrar){
	//var tagSelectDiv = jQuery(this).parent().parent();
	var tagSelectPaginatedDiv = jQuery("#"+id+"_paginated_div");
	var tagSelectSelect = jQuery("#"+id);
	var tagSelectDisabled = jQuery("#"+id+"_disabled");
	var keyupHandler = function(e){
		console.debug("[tagSelect] KEYUP en paginated div");
		jQueryTop.blockUI();
		if (tagSelectPaginatedDiv.is(".open")){
	        var searchValue = tagSelectPaginatedDiv.data("search");
	        var regex = new RegExp("^[a-zA-Z0-9]+$");
	        var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	        if (regex.test(str)) {
	        	if (tagSelectPaginatedDiv.data("waitsearch")){
	        		console.debug("[tagSelect]tagSelectMostrarDesplegable wait search: " + searchValue + str);
	        		tagSelectPaginatedDiv.data("search", searchValue + str);
	        	} else {
	        		tagSelectPaginatedDiv.data("search", str);
	        		tagSelectPaginatedDiv.data("waitsearch", true);
	        		console.debug("[tagSelect]tagSelectMostrarDesplegable keyup search text: " + str);
	        		searchPaginatedSelect(tagSelectPaginatedDiv);
	        		/* NO HAY ESPERA, ACEPTAMOS SOLO UNA LETRA PARA FILTRAR
	        		window.setTimeout(function(){
	        			searchPaginatedSelect(tagSelectPaginatedDiv);
	        		}, 100);
	        		*/
	        	}	        
	        }
		}
	};
	if (typeof mostrar == "undefined" && tagSelectPaginatedDiv.is(":visible") || 
			typeof mostrar != "undefined" && !mostrar){
		console.debug("[tagSelect] OCULTANDO OPCIONES ID: " + id + " por condiciones mostrar: " + mostrar + "; "+tagSelectPaginatedDiv.attr("id")+"_visible: " + tagSelectPaginatedDiv.is(":visible"));
		tagSelectPaginatedDiv.css("visibility", "hidden");
		tagSelectPaginatedDiv.removeClass("open");
		tagSelectSelect.show();
		tagSelectDisabled.hide();
		jQuery(document).unbind("keyup");
		console.debug("[tagSelect] UN-BIND KEY UP");
	} else {
		console.debug("[tagSelect] MOSTRANDO OPCIONES ID: " + id + " por condiciones mostrar: " + mostrar + "; "+tagSelectPaginatedDiv.attr("id")+"_visible: " + tagSelectPaginatedDiv.is(":visible"));
		tagSelectPaginatedDiv.css("visibility", "visible");
		tagSelectPaginatedDiv.addClass("open");
		tagSelectSelect.hide();
		tagSelectDisabled.show();
		if (tagSelectPaginatedDiv.find("li.selected").length > 0){
			var scrollToSelected = tagSelectPaginatedDiv.find("li.selected").offset().top - tagSelectPaginatedDiv.offset().top + tagSelectPaginatedDiv.scrollTop();
			if (scrollToSelected - (tagSelectPaginatedDiv.width() / 2) > 0){
				scrollToSelected = scrollToSelected - (tagSelectPaginatedDiv.width() / 2);
			}
			var ajusteScroll = (tagSelectPaginatedDiv.find("li.selected").height() * 2);
			if (scrollToSelected > (tagSelectPaginatedDiv.width() / 2)){
				scrollToSelected = scrollToSelected + ajusteScroll;
			}
			tagSelectPaginatedDiv.scrollTop(scrollToSelected);
		}
		console.debug("[tagSelect] BIND KEY UP");
		jQuery(document).bind("keyup",keyupHandler);
	}
}

function searchPaginatedSelect(paginatedDiv, lastPage, loadNextPageUrl){	
	paginatedDiv.data("waitsearch", false);
	var searchText = paginatedDiv.data("search").toLowerCase();
	//console.debug("[tagSelect] searchPaginatedSelect: " + searchText);
	var optionsFound = undefined;
	if (lastPage){
		optionsFound = paginatedDiv.find("ul:last").find("span[data-nav='"+searchText+"']");
		/*
		optionsFound = paginatedDiv.find("ul:last").find("span.selectOptionText").filter(function(){
			return jQuery(this).text().toLowerCase().indexOf(searchText) == 0;
		});
		*/
	} else {
		optionsFound = paginatedDiv.find("span[data-nav='"+searchText+"']");
		/*
		optionsFound = paginatedDiv.find("span.selectOptionText").filter(function(){
			return jQuery(this).text().toLowerCase().indexOf(searchText) == 0;
		});
		*/
	}
	if (optionsFound.exists()){
		var optionFound = optionsFound.first().parent();
		//console.debug("[tagSelect] searchPaginatedSelect: " + searchText + " FOUND ON DIV: " + optionsFound.first().text());
		var scrollToSelected = optionFound.offset().top - paginatedDiv.offset().top + paginatedDiv.scrollTop();
		if (scrollToSelected - (paginatedDiv.width() / 2) > 0){
			scrollToSelected = scrollToSelected - (paginatedDiv.width() / 2);
		}
		var ajusteScroll = (optionFound.height() * 2);
		if (scrollToSelected > (paginatedDiv.width() / 2)){
			scrollToSelected = scrollToSelected + ajusteScroll;
		}
		paginatedDiv.scrollTop(scrollToSelected);
		jQueryTop.unblockUI();
	} else {
		var tagSelectSelect = paginatedDiv.parent().find("select");
		if (!tagSelectSelect.data("allPagesLoaded")){
			var nextPage = parseInt(tagSelectSelect.data("page")) + 1;
			//console.debug("[tagSelect] searchPaginatedSelect: " + searchText + " NOT FOUND ON DIV LOADING NEXT PAGE: " + nextPage);
			if (typeof loadNextPageUrl != "undefined"){
				loadNextPageUrl = loadNextPageUrl.replace("&page="+tagSelectSelect.data("page"), "&page="+nextPage);
				tagSelectLoadNextPage(tagSelectSelect, nextPage, true, loadNextPageUrl);
			} else {
				tagSelectLoadNextPage(tagSelectSelect, nextPage, true);
			}
		} else {
			jQueryTop.unblockUI();
			alert("Ningún elemento encontrado por: " + searchText);
		}
	}
}

function tagSelect_searchBox_keyup(el){
	console.debug("[searchBox] tagSelect_searchBox_keyup BEGIN");
	var searchBox_select = el.parent().find("select.tagSelect");
	var old_selected_value = searchBox_select.find("option:selected").val();
	
	tagSelect_search(searchBox_select, el);
	
	var current_selectedValue = searchBox_select.find("option:selected").val();
	console.debug(searchBox_select.attr("id") + " keyup selected value OLD: " + old_selected_value + " NEW: "+ current_selectedValue);
	if (old_selected_value != current_selectedValue && typeof searchBox_select.data("childrenids") != "undefined"){
		console.debug(searchBox_select.attr("id") + " load children...");
		var reloadIds = searchBox_select.data("childrenids").split(",");
		jQuery.each(reloadIds, function(index, childrenId){
			if (typeof current_selectedValue != "undefined" && current_selectedValue != "-1" && current_selectedValue != ""){
				loadSelect(searchBox_select,childrenId);
			} else {
				//console.debug("OnChange empty parent value");
				jQuery("#"+childrenId).html("");
				if (typeof jQuery("#"+childrenId).data("selectparentmsg") != "undefined"){
					jQuery("#"+childrenId).prepend("<option value='' selected>"+jQuery("#"+childrenId).data("selectparentmsg")+"</option>");
				} else if (typeof jQuery("#"+childrenId).data("required") == "undefined" && jQuery("#"+childrenId).data("required") != "true") {
					jQuery("#"+childrenId).prepend("<option value='' selected></option>");
				}
			}
		});
	}
}

function cargarJqueryUI(){
	console.debug("[cargarJqueryUI] BEGIN");
	if (typeof window.jQuery.ui == "undefined"){
		console.debug("[cargarJqueryUI] jQuery UI no encontrado, Cargandolo...");
		// Cargamos jQuery UI y esperamos a que este listo	(CALLBACK jQueryUILoaded)
		var jqueryScript = document.createElement("script");
		jqueryScript.type = "text/javascript";
		jqueryScript.src = jqueryUIfileUri;
		var headElement = document.getElementsByTagName("head")[0];
		headElement.appendChild(jqueryScript);
		// Se establecen un número máximo de intentos
		// El primer intento espera un segundo y luego hace pool cada 500ms
		var maxIntentos = 38; // unos 20 segundos como máximo para dar error
		var intento = 0;
		var waitForLoad = function () {
			if (typeof window.jQuery.ui != "undefined") {
		    	// Inicializa script
		    	jQueryUILoaded();
		    } else if (intento < maxIntentos){
		    	intento++;
		        window.setTimeout(waitForLoad, 500);
		    } else {
		    	alert("Se ha producido un error al cargar la página, por favor, intentelo de nuevo más tarde...");
		    }
		};
		window.setTimeout(waitForLoad, 1000);
	} else {
		// Se ha encontrado jQuery UI en top. Inicializa script
		jQueryUILoaded();
	}
}

function jQueryUILoaded(){
	console.debug("[jQueryUILoaded] BEGIN");
	//***CSS***//
	var css = document.styleSheets;
	var bFound = false;
	var i = 0;
    while (!bFound && i < css.length) {
    	var cssHref = css[i].href;	    	
        if (cssHref &&  css[i].href.endsWith(jqueryUICSSfileUri))
        	bFound = true;
        else
        	i++;
    }
    if (!bFound){
    	//JQUERY UI CSS NO ECONTRADA, LA CARGAMOS
    	console.debug("[jQueryUILoaded] JQUERY UI CSS NO ECONTRADA, LA CARGAMOS");
    	var link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = jqueryUICSSfileUri;

        document.getElementsByTagName("head")[0].appendChild(link);
    }
    // CONFIGURACIÓN POR DEFECTO DE DATEPICKER
	if (jQueryTop!=null&&jQueryTop.fn.datepicker){
		console.debug("[jQueryUILoaded] JQUERY datepicker INI");		
		
		jQueryTop.datepicker.setDefaults(jQueryTop.datepicker.regional['es']);
		jQueryTop.datepicker.setDefaults({
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			showOn: 'both',
			buttonImage: 'button',
			buttonImage: '/SIGA/html/imagenes/calendar.gif',
			buttonImageOnly: true,
			yearRange: "c-100:c+10"
		});
	   
		// Botón Hoy
	   jQueryTop.datepicker._gotoTodayOriginal = jQueryTop.datepicker._gotoToday;
	   jQueryTop.datepicker._gotoToday = function(id) {
		   jQueryTop.datepicker._gotoTodayOriginal.apply(this, [id]);
		   jQueryTop.datepicker._selectDate.apply(this, [id]);
	    };	    	    
	    
	   // UI
	   var old_fn = jQueryTop.datepicker._updateDatepicker;
	   jQueryTop.datepicker._updateDatepicker = function(inst) {
		   var currentDatepicker = jQueryTop(this);		   
		   // Botón Borrar
		   old_fn.call(this, inst);
		   var buttonPane = jQueryTop(this).datepicker("widget").find(".ui-datepicker-buttonpane");
		   var clearText = "Borrar";		   
		   if (inst.settings.regional == "ca")
			   clearText = "Esborrar";
		   else if (inst.settings.regional == "eu")
			   clearText = "Ezabatu";
		   jQueryTop("<button type='button' class='ui-datepicker-clean ui-state-default ui-priority-primary ui-corner-all'>"+clearText+"</button>").appendTo(buttonPane).click(function(ev) {
			   jQueryTop.datepicker._clearDate(inst.input);
	   		});
		   
		   // Botones sig/ant año
		   var header = jQueryTop(this).datepicker("widget").find(".ui-datepicker-header");
		   var next = jQueryTop(this).datepicker("widget").find(".ui-datepicker-next");
		   var prev = jQueryTop(this).datepicker("widget").find(".ui-datepicker-prev");
		   var nextYear = next.clone();
		   var prevYear = prev.clone();
		   nextYear.attr("title", "Sig>>");
		   //nextYear.removeClass("ui-datepicker-next");
		   nextYear.addClass("ui-datepicker-year-next");
		   nextYear.removeAttr("data-handler");
		   nextYear.removeAttr("data-event");
		   nextYear.css("float", "right");
		   nextYear.removeData();
		   prevYear.attr("title", "<<Ant");
		   //prevYear.removeClass("ui-datepicker-prev");
		   prevYear.addClass("ui-datepicker-year-prev");
		   prevYear.removeAttr("data-handler");
		   prevYear.removeAttr("data-event");
		   prevYear.css("float", "left");
		   prevYear.removeData();
		   
		   var header_title_month = jQueryTop(this).datepicker("widget").find(".ui-datepicker-title");
		   var header_title_year = header_title_month.clone(true, true);
		   
		   header_title_month.find("select.ui-datepicker-year").remove();
		   header_title_year.appendTo(header);
		   header_title_year.find("select.ui-datepicker-month").remove();
		   
		   header_title_month.find("select.ui-datepicker-month").css("width", "100%");
		   
		   header_title_year.find("select.ui-datepicker-year").css("width", "40%");
		   header_title_year.prepend("<input type='text' id='datepicker-change-year' style='text-align: right;' value='"+ inst.selectedYear +"'/>");
		   header_title_year.find("#datepicker-change-year").css("width", "40%");
		   //No se quiere el combo, lo ocultamos
		   header_title_year.find("select.ui-datepicker-year").remove();
		   header_title_year.find("#datepicker-change-year").css("width", "30%");
		   
		   header_title_year.find("#datepicker-change-year").on("change", function(){
			   console.debug("[datepicker-change-year] Change value: " + jQuery(this).val());
			   var year = jQuery(this).val();
			   var currentYear = inst.selectedYear;
			   if (year != ""){
				   if (year.length == 2)
					   year = currentYear.toString().substring(0,2) + year;
				   if (year.length == 4){
					   try{
						   var iYear = parseInt(year);						   
						   var offsetYear = iYear - currentYear;
						   console.debug("[datepicker-change-year] Change currentYear: "+currentYear+" iYear: "+iYear+" offsetYear: " + offsetYear);
						   jQueryTop.datepicker._adjustInstDate(inst, offsetYear, "Y");
						   jQueryTop.datepicker._updateDatepicker(inst);
					   } catch (e){
						   //select_year.val("");
						   jQuery(this).val(inst.selectedYear);
					   }
				   }
			   }
		   });
		   
		   prevYear.appendTo(header).click(function(e){
			   jQueryTop.datepicker._adjustInstDate(inst, -1, "Y");
			   jQueryTop.datepicker._updateDatepicker(inst);
		   });
		   nextYear.appendTo(header).click(function(e){
			   jQueryTop.datepicker._adjustInstDate(inst, 1, "Y");
			   jQueryTop.datepicker._updateDatepicker(inst);
		   });
		   		   
   		};   		
	}
}


function datepickerMaskValueChanged(datepickerInput){
	var dateFormat = defaultDateFormat;
	if (datepickerInput.data("datepickerformat") && datepickerInput.data("datepickerformat") != "")
		dateFormat = datepickerInput.data("datepickerformat");
	var dateValue = datepickerInput.val();
	if (dateValue != ""){
		var date = formatDate(dateValue, dateFormat);
		if (!date){
			datepickerInput.val("");
			datepickerInput.blur();
			alert("La fecha "+dateValue+" no es válida. Introduzca una fecha válida: " + datepickerInput.data("format"));		
		}
	}
}

function formatDate(dateValue, dateFormat){
	var date;
	try{
		if (!dateFormat || dateFormat == "")
			dateFormat = defaultDateFormat;
		//console.debug("[formatDate] dateValue="+dateValue+"; dateFormat="+dateFormat);
		date = jQueryTop.datepicker.parseDate(dateFormat, dateValue);
		//console.debug("[formatDate] OK date="+date);
	} catch (e){
		date = "";
		console.debug("[formatDate] ERROR llamada formatDate("+dateValue+","+dateFormat+")");
	}
	return date;
}

function ajaxSubmit(form, iFrame){	
	jQuery.ajax({
		url: form.attr("action"),
		dataType: "html",
		data: form.serialize()
	}).done(function(data, textStatus, jqXHR){
		console.debug("[ajaxSubmit] Reemplazando iFrame por div...");
		iFrame.replaceWith("<div data-iframe='true' id="+iFrame.attr("id")+" name="+iFrame.attr("name")+" class="+iFrame.attr("class")+">" + data + "</div>");
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error("Se ha producido un error al enviar el formulario: " + errorThrown);
		alert("Se ha producido un error, por favor, inténtelo de nuevo más tarde.");
	}).always(function(data_jqXHR, textStatus, jqXHR_errorThrown) {
		
	});
};

function tagSelectDivAttrChangeCallback(event, element){
	var modifiedAttributeName=event.attributeName;
	console.debug("[tagSelectDivAttrChangeCallback] Modified "+modifiedAttributeName+" for id: "+element.attr("id") + " current class Value: "+element.attr("class"));
	if (modifiedAttributeName == "class" || modifiedAttributeName == "forceCheckAttr"){
		tagSelectState(element.find("select").attr("id"), element.hasClass("disabled"));
	}
}

function tagSelectAttrChangeCallback(event, element){
	var modifiedAttributeName=event.attributeName;
	console.debug("[tagSelectAttrChangeCallback] Modified "+modifiedAttributeName+" for id: "+element.attr("id"));
	if (!jQuery("#"+element.attr("id")+"_tagSelectDiv").data("hideifnooptions")){
		if (modifiedAttributeName == "forceCheckAttr"){
			if (element.hasClass("disabled") || element.is(":disabled") || element.is("[readonly='readonly']")){
				if (!jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
					console.debug("[tagSelectAttrChangeCallback] ADD CLASS DISABLED TO "+element.attr("id")+"_tagSelectDiv");
					jQuery("#"+element.attr("id")+"_tagSelectDiv").addClass("disabled");
					if (window.MutationObserver || window.WebKitMutationObserver){
						// No hacer nada, se detectará el cambio y se ejecutará tagSelectAttrChangeCallback
					} else {
						var e = {};
						e.attributeName = "forceCheckAttr";
						tagSelectDivAttrChangeCallback(e, jQuery("#"+element.attr("id")+"_tagSelectDiv"));
					}
				}
			} else if (jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
				console.debug("[tagSelectAttrChangeCallback] REMOVE CLASS DISABLED FROM "+element.attr("id")+"_tagSelectDiv");
				jQuery("#"+element.attr("id")+"_tagSelectDiv").removeClass("disabled");
				if (window.MutationObserver || window.WebKitMutationObserver){
					// No hacer nada, se detectará el cambio y se ejecutará tagSelectAttrChangeCallback
				} else {
					var e = {};
					e.attributeName = "forceCheckAttr";
					tagSelectDivAttrChangeCallback(e, jQuery("#"+element.attr("id")+"_tagSelectDiv"));
				}
			}
		}else if (modifiedAttributeName == "class"){
			if (element.hasClass("disabled")){
				if (!jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
					console.debug("[tagSelectAttrChangeCallback] ADD CLASS DISABLED TO "+element.attr("id")+"_tagSelectDiv");
					jQuery("#"+element.attr("id")+"_tagSelectDiv").addClass("disabled");
				}
			} else {
				if (jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
					console.debug("[tagSelectAttrChangeCallback] REMOVE CLASS DISABLED FROM "+element.attr("id")+"_tagSelectDiv");
					jQuery("#"+element.attr("id")+"_tagSelectDiv").removeClass("disabled");
				}
			}											
		} else if (modifiedAttributeName == "disabled" || modifiedAttributeName == "readonly"){
			if (element.is(":disabled") || element.is("[readonly='readonly']")){
				if (!jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
					console.debug("[tagSelectAttrChangeCallback] ADD CLASS DISABLED TO "+element.attr("id")+"_tagSelectDiv");
					jQuery("#"+element.attr("id")+"_tagSelectDiv").addClass("disabled");
				}
			} else {
				if (jQuery("#"+element.attr("id")+"_tagSelectDiv").hasClass("disabled")){
					console.debug("[tagSelectAttrChangeCallback] REMOVE CLASS DISABLED FROM "+element.attr("id")+"_tagSelectDiv");
					jQuery("#"+element.attr("id")+"_tagSelectDiv").removeClass("disabled");
				}
			}
		} else {
			console.debug("[tagSelectAttrChangeCallback] NO ACTION");
		}
	} else {
		jQuery("#"+element.attr("id")+"_tagSelectDiv").removeData("hideifnooptions");
	}
}

function disableTagSelects(oSelects){
	if (typeof oSelects != "undefined"){
		oSelects.each(function(){
			var oSelect = jQuery(this);
			oSelect.attr("disabled", true);
			if (window.MutationObserver || window.WebKitMutationObserver){
				// No hacer nada, se detectará el cambio y se ejecutará tagSelectAttrChangeCallback
			} else {
				var tagSelectDiv = jQuery("#"+oSelect.attr("id")+"_tagSelectDiv");
				tagSelectDiv.addClass("disabled");
				var e = {};
				e.attributeName = "forceCheckAttr";
				tagSelectDivAttrChangeCallback(e, tagSelectDiv);
			}
		});
	}
}

function enableTagSelects(oSelects){
	if (typeof oSelects != "undefined"){
		oSelects.each(function(){
			var oSelect = jQuery(this);
			oSelect.removeAttr("disabled");
			if (window.MutationObserver || window.WebKitMutationObserver){
				// No hacer nada, se detectará el cambio y se ejecutará tagSelectAttrChangeCallback
			} else {
				var tagSelectDiv = jQuery("#"+oSelect.attr("id")+"_tagSelectDiv");
				tagSelectDiv.removeClass("disabled");
				var e = {};
				e.attributeName = "forceCheckAttr";
				tagSelectDivAttrChangeCallback(e, tagSelectDiv);
			}
		});
	}
}

function tagSelectState(id, disabled){
	console.debug("[tagSelectState] ID: " + id + " disabled: " + disabled);
	if (disabled == undefined){
		disabled = jQuery("#"+id).hasClass("disabled");
	}
	if (id && jQuery("#"+id).exists()){
		var selectTag = jQuery("#"+id);
		var divTag = jQuery("#"+id+"_tagSelectDiv");
		if (!disabled){
			console.debug("[tagSelectState] MOSTRANDO SELECT " + id);
			selectTag.show();
			if (jQuery("#"+selectTag.attr("id") + "_searchBox").exists())
				jQuery("#"+selectTag.attr("id") + "_searchBox").show();
			jQuery("#" + selectTag.attr("id")+"_disabled").hide();			
		} else {
			console.debug("[tagSelectState] OCULTANDO SELECT " + id);
			selectTag.hide();
			if (jQuery("#"+selectTag.attr("id") + "_searchBox").exists())
				jQuery("#"+selectTag.attr("id") + "_searchBox").hide();
			jQuery("#" + selectTag.attr("id")+"_disabled").show();			
		}
		if (disabled != jQuery("#"+id).hasClass("disabled")){
			if (disabled){
				console.debug("[tagSelectState] UPDATING " + id + " ADDING DISABLED CLASS...");
				jQuery("#"+id).addClass("disabled");
			} else {
				console.debug("[tagSelectState] UPDATING " + id + " REMOVING DISABLED CLASS...");
				jQuery("#"+id).removeClass("disabled");
			}
		}
		
		if (disabled != divTag.hasClass("disabled")){
			if (disabled){
				console.debug("[tagSelectState] UPDATING " + divTag.attr("id") + " ADDING DISABLED CLASS...");
				divTag.addClass("disabled");
			} else {
				console.debug("[tagSelectState] UPDATING " + divTag.attr("id") + " REMOVING DISABLED CLASS...");
				divTag.removeClass("disabled");
			}
		}
	}
}

function setTagselectDivWidth(tagSelectDiv){
	/* NO HACE FALTA CON EL INLINE
	if (tagSelectDiv.data("width")){
		tagSelectDiv.width(tagSelectDiv.data("width"));
	} else {
		var width = 10;
		if (tagSelectDiv.find("select:visible").exists()){
			width = tagSelectDiv.find("select:visible").width();
			if(tagSelectDiv.find("input.tagSelect_searchBox").exists()){
				width += tagSelectDiv.find("input.tagSelect_searchBox").width();
			}
		} else if (tagSelectDiv.find("input.tagSelect_disabled:visible").exists()){
			width = tagSelectDiv.find("input.tagSelect_disabled:visible").width();
		}
		if (width != 10){
			console.debug("[setTagselectDivWidth] width: " + width);
			tagSelectDiv.width(width);
		}
	}
	*/
}

function tagSelect_search(tagSelect_select, tagSelect_searchBox){
	if (typeof tagSelect_searchBox != "undefined" && typeof tagSelect_select != "undefined" && tagSelect_searchBox.exists()) {
		
		tagSelect_select.find("option").each(function() {
			if (jQuery(this).parent().is("span")){
				jQuery(this).unwrap();
			}
			jQuery(this).show();
		});
		
		var searchValue = tagSelect_searchBox.val();
		if (searchValue != ""){
			tagSelect_select.find("option").wrap("<span>").hide();
			var optionsFound = tagSelect_select.find("option").filter(function() {
				var vBoolean = this.value == "";					
				if (!vBoolean)
					vBoolean = String(jQuery(this).data("searchkey")).toLowerCase().indexOf(String(searchValue).toLowerCase()) != "-1";
				return vBoolean;
			});
			
			if (optionsFound.length > 1){
				/* Esto soluciona el problema de que se borre el filtro introducido al 
					perder el foco, seleccionando el primer elemento no vacío como estaba
					pero es que no se quiere que se seleccione (falta análisis)
				var vSelected = false;
				optionsFound.each(function() {
					if (jQuery(this).parent().is("span")){							
						jQuery(this).unwrap();
					}
					
					if (jQuery(this).attr("data-searchKey") == searchValue){
					    jQuery(this).attr("selected", true);
					    if (vSelected){
					    	vSelected.removeAttr("selected");
					    }
					} else if (!vSelected && jQuery(this).val() !== ""){
				    	jQuery(this).attr("selected", true);
				    	vSelected = jQuery(this);
				    } else {
				    	jQuery(this).removeAttr("selected");
				    }
				});								
				optionsFound.show();
				 */
				optionsFound.each(function() {
					if (jQuery(this).parent().is("span")){							
						jQuery(this).unwrap();
					}
					
					if (jQuery(this).attr("data-searchKey") == searchValue)
					    jQuery(this).attr("selected", true);
				    else
				    	jQuery(this).removeAttr("selected");
				});								
				optionsFound.show();

			} else {
				tagSelect_select.find("option").removeAttr("selected");
				tagSelect_select.find("option[value='']").attr("selected", true);
				
				tagSelect_select.find("option").each(function() {
					if (jQuery(this).parent().is("span")){
						jQuery(this).unwrap();
					}
					jQuery(this).show();
				});
			}
			
		} else {
			tagSelect_select.find("option").removeAttr("selected");
			tagSelect_select.find("option[value='']").attr("selected", true);
			
			tagSelect_select.find("option").each(function() {
				if (jQuery(this).parent().is("span")){
					jQuery(this).unwrap();
				}
				jQuery(this).show();
			});
		}
	}
}

function reloadSelect(childrenId, setValue, params){
	if (setValue)
		jQuery("#"+childrenId).data("set-id-value", setValue);
	loadSelect(undefined, childrenId, undefined, params);
}
function tagSelectLoadNextPage(select, page, callSearch, loadNextPageUrl){
	if (select.exists()){
		if (!select.data("loadingpage") && !select.data("allPagesLoaded")){
			select.data("loadingpage", true);
			var tagSelectPaginatedDiv = select.parent().find("div.tagSelectPaginatedDiv");
			tagSelectPaginatedDiv.find(".loading").remove();
			tagSelectPaginatedDiv.find("ul").append("<li class='selectOption loading'><span class='selectOptionText'><img id='"+select.attr("id")+"_loading_page' src='html/imagenes/loading-spinner.gif' alt='Cargando...' style='margin: 0px auto;' /></span></li>");
			var incPage = 0;
			if (typeof page == "undefined"){
				page = select.data("page");
				if (typeof page == "undefined")
					page = 1;
				else
					incPage = 1;
			}
			try{
				page = parseInt(page);
			} catch(e){
				page = 1;
			}
			page = page + incPage;
			
			var finalURL = "selectData.do?queryId="+select.data("queryid");
			if (typeof loadNextPageUrl == "undefined"){
				// parámetros de carga
				var dataObject = {};
				dataObject = jQuery("select, input").not("*[data-queryparamid], input.tagSelect_disabled, input.tagSelect_searchBox").serializeObject();
				jQuery("*[data-queryparamid]").each(function(){
					var key = jQuery(this).data("queryparamid");
					var value = jQuery(this).val();
					if (value == "" && jQuery(this).is("select"))
						value = jQuery(this).find("option:selected").val();				
					dataObject[key] = value || '';
				});
				
				if (params){
					jQuery.each(params, function(index, param){
						try{
							dataObject[param.key] = param.value || '';
						} catch (e){
							console.error("Error al incluir algún parámetro en la carga del select");
						}
					});
				}
				
				var data = JSON.stringify(dataObject);
				var params = "";
				if (typeof data != "undefined"){
					params = "&paginated=true&page="+page+"&pagesize="+select.data("pagesize")+"&params="+encodeURIComponent(data);
				}
				finalURL += params;
			} else {
				finalURL = loadNextPageUrl;
			}
			
			//Llamada al servidor
			var jqxhr = jQuery.ajax({
				url:finalURL
			}).done(function(data, textStatus, jqXHR){
				console.debug("[tagSelect]" + select.attr("id")+" load page "+page+" DONE!");
				tagSelectPaginatedDiv.find(".loading").remove();
				tagSelectPaginatedDiv.append("<ul class='selectOptions' data-page='"+page+"'></ul>");
				tagSelectPaginatedDiv.find("ul:last")[0].innerHTML = data;
				//tagSelectPaginatedDiv.find("ul").append(data);
				select.data("page", page);				
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.debug("[tagSelect]" + select.attr("id")+" load page "+page+" FAIL! ERROR: " + errorThrown);
				//alert("Se ha producido un error al cargar los datos");
				tagSelectPaginatedDiv.find(".loading").remove();
				tagSelectPaginatedDiv.find("ul").append("<li class='selectOption loading'><span class='selectOptionText'>Error, inténtelo de nuevo</span></li>");
			}).always(function(data_jqXHR, textStatus, jqXHR_errorThrown) {
				select.data("loadingpage", false);
				if (tagSelectPaginatedDiv.find("li.notFound").exists()){
					select.data("allPagesLoaded", true);
				}
				if (callSearch){
					searchPaginatedSelect(tagSelectPaginatedDiv, true, finalURL);
				}
			});
		}
	}
}

function loadSelect(parentSelects, childrenId, setInitialValue, params){
	var blockParentSelects = undefined;
	if (parentSelects){
		blockParentSelects = parentSelects.not(":disabled").not('[readonly="readonly"]').not(".disabled");
		disableTagSelects(blockParentSelects);
	}
	var childrenSelect = jQuery("#"+childrenId);
	console.debug("loadSelect " + childrenId);
	if (childrenSelect.exists()){
		var loadingId = childrenSelect.attr("id") + "_loading";
		if (!jQuery("#"+loadingId).exists()){
			childrenSelect.after("<img id='"+loadingId+"' src='html/imagenes/loading-spinner.gif' alt='Cargando...' style='visibility:hidden' />");			
		}
		jQuery("#"+loadingId).css('visibility', 'visible');
		var dataObject = {};
		dataObject = jQuery("select, input").not("*[data-queryparamid], input.tagSelect_disabled, input.tagSelect_searchBox").serializeObject();
		jQuery("*[data-queryparamid]").each(function(){
			var key = jQuery(this).data("queryparamid");
			var value = jQuery(this).val();
			if (value == "" && jQuery(this).is("select"))
				value = jQuery(this).find("option:selected").val();
			/*
			if (dataObject[key] !== undefined && dataObject[key] != "" && dataObject[key] != " " && dataObject[key] != "-1") {
				if (!dataObject[key].push) {
					dataObject[key] = [dataObject[key]];
	            }
				dataObject[key].push(value || '');
			} else {
				dataObject[key] = value || '';
			}
			*/
			// Sobreescribimos el valor siempre aunque exista ya para evitar errores en páginas donde hay más de un formulario y se repiten atributos
			// atributos
			dataObject[key] = value || '';
			console.debug("ADDED queryparamid: " + key + " = " + dataObject[key]);
		});
		if (params){
			jQuery.each(params, function(index, param){
				try{
					dataObject[param.key] = param.value || '';
					console.debug("ADDED param "+ param.key + " = " + dataObject[param.key]);
				} catch (e){
					console.error("Error al incluir algún parámetro en la carga del select");
				}
			});
		}
		var data = JSON.stringify(dataObject);
		var params = "";
		if (typeof data != "undefined"){
			params = "&params="+encodeURIComponent(data);
		}
		var selectedIds = "";
		if(setInitialValue && typeof childrenSelect.data("inival") != "undefined"){
			selectedIds = "&selectedIds="+childrenSelect.data("inival");
		}
		if (typeof childrenSelect.data("set-id-value") != "undefined"){
			selectedIds = "&selectedIds="+childrenSelect.data("set-id-value");
			childrenSelect.removeData("set-id-value");
		}
		
		var required = "";
		if(typeof childrenSelect.data("required") != "undefined"){
			required = "&required="+childrenSelect.data("required");
		}
		var sShowsearchbox = "";
		if (typeof childrenSelect.data("showsearchbox") != "undefined"){
			sShowsearchbox ="&showsearchbox=true";
		}
		// Funciona mucho mejor en IE que .html("");
		childrenSelect.each(function(){this.innerHTML="";});
		childrenSelect.html(optionCargando);
		childrenSelect.each(function(){
			var currentChild = this;
			if (jQuery(currentChild).data("hideifnooptions") == "true"){
				jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv").show();
			}
			var jqxhr = jQuery.ajax({
				url:"selectData.do?queryId="+jQuery(currentChild).data("queryid")+params+selectedIds+required+sShowsearchbox
			}).done(function(data, textStatus, jqXHR){
				console.debug(jQuery(currentChild).attr("id")+" load DONE!");
				//var start = +new Date();
				var currentChildId = jQuery(currentChild).attr("id");
				jQuery(currentChild).html(data);
				/* FUNCIONA MUCHO MÁS RÁPIDO EN IE PERO SE PIERDEN LOS EVENTOS					
				jQuery(currentChild).empty();
				var currentChild_loader = jQuery("#"+jQuery(currentChild).attr("id")+"_loader")[0];
				
				var events = jQuery(currentChild).data('events');
				var newSelect = jQuery(currentChild).clone(true, true);
				newSelect[0].innerHTML = data;
				alert("options: " + newSelect[0].options.length);
				var newSelectDiv = document.createElement('div');
				newSelectDiv.appendChild(newSelect[0]);
				currentChild_loader.innerHTML = newSelectDiv.innerHTML;
				
				console.debug(currentChildId + ": " + jQuery("#"+currentChildId).length);
				$.each(events, function() {
				  $.each(this, function() {
					  console.debug(currentChildId+ " bind " + this.type + " " + this.handler);
					  jQuery("#"+currentChildId).bind(this.type, this.handler);
				  });
				});
				*/								
				
				/*
				var end =  +new Date();
				var diff = end - start;
				console.debug(jQuery(currentChild).attr("id")+" Inserted on DOM on: "+diff);
				*/
				
				if(typeof jQuery(currentChild).data("onloadcallback") != "undefined"){
					try{
						console.debug("Ejecutando callback de " + jQuery(currentChild).attr("id"));
						var callbackFnVarName = jQuery(currentChild).data("onloadcallback");
						console.debug("calling " + jQuery(currentChild).attr("id") + " callback: " + callbackFnVarName);
						var callbackFnVar = window[callbackFnVarName];
						var callbacks = jQuery.Callbacks('once unique');
						callbacks.add(callbackFnVar);
						callbacks.fireWith(jQuery(currentChild).context);
					} catch (e){
						console.error("Error al ejecutar callback de " + jQuery(currentChild).attr("id"));
					}					
				}
				
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.debug(jQuery(currentChild).attr("id")+" load FAIL! ERROR: " + errorThrown);
				//alert("Se ha producido un error al cargar los datos");
				jQuery(currentChild).empty();
			}).always(function(data_jqXHR, textStatus, jqXHR_errorThrown) {
				//if (jQuery("#"+currentChildId).val() !== "undefined" && jQuery("#"+currentChildId).val() != "" && jQuery("#"+currentChildId).val() != "-1"){
					jQuery(currentChild).change();
				//}
				if (jQuery(currentChild).data("hideifnooptions")){
					if (jQuery(currentChild).find("option").length == 0 || (jQuery(currentChild).find("option").length == 1 && jQuery(currentChild).find("option").val() == "")){
						jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv").data("hideifnooptions", "true");
						jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv").hide();
					} else {
						jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv").data("hideifnooptions", "true");
						jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv").show();
						setTagselectDivWidth(jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv"));
					}
				} else {
					setTagselectDivWidth(jQuery("#"+jQuery(currentChild).attr("id")+"_tagSelectDiv"));
				}
				
				if (jQuery("#"+loadingId).exists())
					jQuery("#"+loadingId).css('visibility', 'hidden');
				enableTagSelects(blockParentSelects);
			});
		});		
	} else {
		enableTagSelects(blockParentSelects);
	}
}

var ie = (function(){
    var undef, v = 3, div = document.createElement('div');

    while (
        div.innerHTML = '<!--[if gt IE '+(++v)+']><i></i><![endif]-->',
        div.getElementsByTagName('i')[0]
    );

    return v> 4 ? v : undef;
}());

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
};
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
	} else if (tamano == "S") {
		returnValue = showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp" + msg,
				formulario,
				"dialogHeight:300px;dialogWidth:300px;help:no;scroll:no;status:no;");
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
	//var jQuery = window.top.jQuery;
	if(w == undefined) {
		w = window.top;
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
	
	//var jQuery = window.top.jQuery;
	if(w == undefined) {
		w = window.top;
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
			var AlturaIframeOriginal = jQuery(obj).height();
			var windowHeight = jQuery(window).height();
			if (windowHeight == null || windowHeight == undefined || windowHeight <= 0){
				windowHeight = document.body.offsetHeight - 5;
			}
			
			if (nObj == 'mainWorkArea') {
				//alert("ajusteAltoMain mainWorkArea " +  document.body.offsetHeight + " - " + jQuery('#posicionTitulo', window.top.document).height() + " - " + jQuery('#posicionTitulo', window.top.document).offset().top);				
				hCont =  windowHeight - jQuery('#posicionTitulo', window.top.document).height() - jQuery('#posicionTitulo', window.top.document).offset().top;
				//alert("final iframe mainWorkArea : "+hCont);
				if (hCont > 0){
					jQuery(obj).height(hCont);
				}
				
			} else {
				//alert("ajusteAltoMain NOT mainWorkArea " + window.outerHeight + " - " + jQuery(obj, this.parent.document).offset().top + " - " + menos);
				hCont = windowHeight - jQuery(obj, this.parent.document).offset().top - menos;
				//hCont = jQuery.height(window) - jQuery.offset(obj, this.parent.document).top - menos;
				//alert("final iframe other : "+hCont);
				if (hCont > 0){
					jQuery(obj).height(hCont);
				}
			}
			var innerFixedHeadertables = jQuery(obj).contents().find("table.fixedHeaderTable");
			if (innerFixedHeadertables.exists()){
				var menosAplicadoAlIframe = menos;
				if (AlturaIframeOriginal >= jQuery(obj).height()){
					menosAplicadoAlIframe = AlturaIframeOriginal - jQuery(obj).height();
				} else if (AlturaIframeOriginal < jQuery(obj).height()) {
					menosAplicadoAlIframe = jQuery(obj).height() - AlturaIframeOriginal;
					menosAplicadoAlIframe = menosAplicadoAlIframe * -1;
				}
				console.debug("ajusteAltoMain nodeName=IFRAMEID:"+jQuery(obj).attr("id")+" fixedHeaderTable: "+jQuery(obj).contents().find("table.fixedHeaderTable").attr("id"));
				innerFixedHeadertables.parent().filter("div[id$='_BodyDiv']").each(function(){
					var tableBodyDiv = jQuery(this);
					console.debug("ajusteAltoMain ajustando FixedHeadertableID="+tableBodyDiv.attr("id")+ " con altura:"+tableBodyDiv.height()+" restando " + menosAplicadoAlIframe);
					tableBodyDiv.height(tableBodyDiv.height() - menosAplicadoAlIframe);
					console.debug("ajusteAltoMain ajustando FixedHeadertable resultado: " + tableBodyDiv.height());
				});
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
				jQuery(obj).height(hCont)
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

	combo = top.frames[0].document.frames[0].document.getElementById(idCombo + 'Frame');

	if (!combo) {
		combo = top.frames[0].document.getElementById(idCombo + 'Frame');
	}
	
	if (!combo) {
		combo = document.getElementById(idCombo + 'Frame');
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
	while (parent != null && parent !== undefined
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
			document.getElementById(ident + "ImMas").style.display = "none";
		if (document.getElementById(ident + "ImMenos"))
			document.getElementById(ident + "ImMenos").style.display = "inline-block";
	} else {
		document.getElementById(ident).style.display = "none";
		document.getElementById(ident).parentElement.className = 'legendNoBorder';
		if (document.getElementById(ident + "ImMenos"))
			document.getElementById(ident + "ImMenos").style.display = "none";
		if (document.getElementById(ident + "ImMas"))
			document.getElementById(ident + "ImMas").style.display = "inline-block";
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
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30;}
		if (i==2) {this[i] = 29;}
   } 
   return this;
}

function validarFechaRegExp(dtStr){
	var daysInMonth = DaysArray(12);
	var pos1=dtStr.indexOf(dtCh);
	var pos2=dtStr.indexOf(dtCh,pos1+1);
	var strDay=dtStr.substring(0,pos1);
	var strMonth=dtStr.substring(pos1+1,pos2);
	var strYear=dtStr.substring(pos2+1);
	strYr=strYear;
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
	}
	month=parseInt(strMonth);
	day=parseInt(strDay);
	year=parseInt(strYr);
	if (pos1==-1 || pos2==-1){
		//alert("The date format should be : dd/mm/yyyy")
		return false;
	}
	if (strMonth.length<1 || month<1 || month>12){
		//alert("Please enter a valid month")
		return false;
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		//alert("Please enter a valid day")
		return false;
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		//alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false;
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		//alert("Please enter a valid date")
		return false;
	}
return true;
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

// JBD // ¿Por que has quitado esto BNS? No funciona nada en chrome si lo quitas porque el tag html:text no crea id, solo name 
// BNS porque daba errores de llenado de pila de llamadas en chrome e IE y como esta funcionalidad te la da jquery sin sobrescribir un método de document creía que ya no se estaba usando...
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


// **
	
	function iniInputSelect(frames){
		var selects = undefined;
		if (frames){
			selects = frames.find("select:not(.inputSelect, .tagSelect)");
			frames.addClass("inputSelect");
		} else
			selects = jQuery("select:not(.inputSelect, .tagSelect)");
		
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
		//console.debug("scrolify TableId"+tblAsJQueryObject.attr("id")+" height: " + height);
		/* CONTROL DE NÚMERO DE CABECERAS/COLUMNAS DATOS
		var numRows = parseInt(tblAsJQueryObject.find('tbody tr').length);
		var numHeaders = parseInt(tblAsJQueryObject.find('thead th').length);
		var numCells = parseInt(tblAsJQueryObject.find('tbody td').length);
		//console.log("scrolify numRows: " + numRows + "; numHeaders: "+numHeaders+"; numCells:"+numCells);
		if (numRows * numHeaders < numCells){
			//console.log("scrolify NUM CABECERAS MENOR A NUM COLUMNAS DATOS");
			var addHeader = 1;
			while((numRows * (numHeaders + addHeader)) < numCells){
				addHeader++;
			}
			//console.log("scrolify AÑADO "+addHeader+" CABECERAS VACÍAS");
			for (var i = 0; i < addHeader; i++)
				tblAsJQueryObject.find('thead').append("<th></th>");
		}		
		*/
		if (height && !isNaN(height) && height < 20){
			height = 100;
		}
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
        //console.debug(tblId+"_BodyDiv height: " + height);
        //BNS: NO TENÍA EN CUENTA LA ALTURA DE LA CABECERA PARA EL CÁLCULO DE LA ALTURA DEL CUERPO
        oTblDiv.css('height', height - oTbl.find("thead").height());
        oTblDiv.css('overflow-y', 'auto');
        oTblDiv.css('overflow-x', 'hidden');
        oTblDiv.css('white-space','break-word');
        oTblDiv.css('text-overflow','ellipsis');
        oTblDiv.css('width','100%');
        oTbl.wrap(oTblDiv);

        // save original width
        oTbl.attr("data-item-original-width", oTbl.width());
        oTbl.find('thead tr th').each(function () {
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
        //oTblDiv.css('height', oTblDiv.height() - newTbl.height());
        //oTblDiv.height(oTblDiv.height() - newTbl.height());
        //console.debug("oTblDiv height["+tblId+"]: " + oTblDiv.height());
    }
	function fixCellBorders (table){
		var tableId = table.attr("id");
		if (jQuery("#"+tableId+"_tblFxHeadr").find("tr.notFound").length <= 0){
			jQuery.each( jQuery("#"+tableId+"_tblFxHeadr").find("th"), function(columnIndex, value){
				var headerCell = jQuery(value);
				var dataColumn = jQuery("#"+tableId+"_tblFxHeadr").find("td:eq("+columnIndex+")");
				/*
				if (ie === undefined){
					dataColumn.width(headerCell.width());
				} else {
					dataColumn.width(headerCell.outerWidth(true));
				}
				*/
				if (ie === undefined){
					//headerCell.width(dataColumn.width());
				} else {
					headerCell.width(dataColumn.outerWidth(true));
				}
				//dataColumn.css("width", headerCell.css("width"));
				/*
				var headerColumnOuterWidth = jQuery(value).outerWidth();
				var dataColumn = jQuery("#"+tableId+"_tblFxHeadr").find("td:eq("+columnIndex+")");
				var dataColumnMPB = dataColumn.outerWidth() - dataColumn.width();
				dataColumn.width(headerColumnOuterWidth - dataColumnMPB);
				//console.log("fixCellBorders >> SETEO A " + (headerColumnOuterWidth - dataColumnMPB));
				//console.log("fixCellBorders >> DESPUES: " + jQuery(value).outerWidth() + " === " + dataColumn.outerWidth());
				*/
				//jQuery("#"+tableId+"_tblFxHeadr").find("td:eq("+columnIndex+")").width(width);
			});
		}
	}

	function loadFixedHeaderTables (tableId, fixedHeight) {
		//console.debug(">>> loadFixedHeaderTables("+tableId+", "+fixedHeight+") BEGIN");
		var oTable = jQuery('#'+tableId+'.fixedHeaderTable');
    	if (oTable.length > 0){
    		//console.log("loadFixedHeaderTables table found");
    		sub(window);
    		if (fixedHeight !== undefined && !isNaN(fixedHeight)){
    			//console.log("loadFixedHeaderTables fixedHeight: " + fixedHeight);
    			if (parseInt(fixedHeight) > 0){
    				//console.debug("loadFixedHeaderTables fixedHeight: " + fixedHeight);
    				scrolify(oTable, fixedHeight);
    			}
    		} else {
	    		//console.debug("loadFixedHeaderTables calculando altura..");
	    		var fixedHeaderTableHeight = 0;
	    		if (oTable.offset().top > 0){
	    			fixedHeaderTableHeight -= oTable.offset().top;
	    		}
				var tableContainer = getCurrentIFrame();
				if (tableContainer == null){
					tableContainer = oTable;
				} else {
					tableContainer = jQuery(tableContainer);
				}
				// En ventanaModal debe de cargarse el iframe después de la inicialización por lo que
				// el tamaño del conteneder (iframe id="modal") es de 5 cosa que luego no se cumple...
				// como estos casos son una tabla cuyo padre directamente es el iframe modal podemos
				// tomar como altura el tamaño de la ventana modal.
				if (tableContainer != null && tableContainer.attr("id") == "modal" && tableContainer.height() < 20){
					console.debug("[loadFixedHeaderTables] AJUSTE MODAL (window height)");
					var winW = 630, winH = 460;
					if (document.body && document.body.offsetWidth) {
					 winW = document.body.offsetWidth;
					 winH = document.body.offsetHeight;
					}
					if (document.compatMode=='CSS1Compat' &&
					    document.documentElement &&
					    document.documentElement.offsetWidth ) {
					 winW = document.documentElement.offsetWidth;
					 winH = document.documentElement.offsetHeight;
					}
					if (window.innerWidth && window.innerHeight) {
					 winW = window.innerWidth;
					 winH = window.innerHeight;
					}
					fixedHeaderTableHeight += winH;
				} else {
					fixedHeaderTableHeight += tableContainer.height();
				}
				
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
				if (tablaPaginacion !== undefined){
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
				if (tablaBotones !== undefined){
					//alert("tiene tablaBotones");
					//console.debug("Tiene tablaBotones");
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
				
				if (tablaPaginacionLocal !== undefined){
					fixedHeaderTableHeight -= tablaPaginacionLocal.outerHeight(true);
				}
				if (tablaBotonesLocal !== undefined){
					//alert("ContainerId: "+tableContainer.attr("id")+"; ContainerHeight: "+tableContainer.height()+"; fixedHeaderTableHeight: " + fixedHeaderTableHeight + " - " + tablaBotonesLocal.outerHeight(true));
					fixedHeaderTableHeight -= tablaBotonesLocal.outerHeight(true);
				}
				if (fixedHeight !== undefined){
					var addMargin = true;
					try{
						if (fixedHeight !== undefined && fixedHeight.substr(fixedHeight.length - 1) == "%"){
							//console.log("loadFixedHeaderTables fixedHeight x%");
							var percent = fixedHeight.substring(0, fixedHeight.length - 1);
							if (!isNaN(percent)){
								//console.log("loadFixedHeaderTables fixedHeight "+fixedHeaderTableHeight+"px >> "+percent+"%");
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
	            if (tablaPaginacion !== undefined && tablaBotones !== undefined && 
	            		tablaPaginacion.length>0 && tablaPaginacion[0] !== undefined && 
	            		tablaBotones.length>0 && tablaBotones[0] !== undefined){
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
				} else if (tablaPaginacion !== undefined && tablaPaginacion.length>0 && tablaPaginacion[0] !== undefined){					
					nextUpperElement = tablaPaginacion;
					nextUpperElementPosition = getElementAbsolutePos(tablaPaginacion[0]);
					//alert("recalculo con tablaPaginacionPosition x:" + nextUpperElementPosition.x + " y:" + nextUpperElementPosition.y);
				} else if (tablaBotones !== undefined && tablaBotones.length>0 && tablaBotones[0] !== undefined){
					nextUpperElement = tablaBotones;
					//alert("calculando posicion de tablaBotones");
					nextUpperElementPosition = getElementAbsolutePos(tablaBotones[0]);
					//alert("posicion de tablaBotones x:"+nextUpperElementPosition.x+" y:"+nextUpperElementPosition.y);
					//console.debug("posicion de tablaBotones x:"+nextUpperElementPosition.x+" y:"+nextUpperElementPosition.y);
					//alert("recalculo con tablaBotonesPosition x:" + nextUpperElementPosition.x + " y:" + nextUpperElementPosition.y);
				}
	            //alert("comprobamos si recalculamos...");
	            //alert("tblPosition.y: " + tblPosition.y + " outerHeight: " + jQuery('#'+tableId+'_BodyDiv').outerHeight(true) + " nextUpperElementPosition.y: " + nextUpperElementPosition.y);
	            if (nextUpperElementPosition !== undefined && (tblPosition.y + jQuery('#'+tableId+'_BodyDiv').outerHeight(true)) > nextUpperElementPosition.y){
	            	//console.debug("tblPosition.y: " + tblPosition.y + " outerHeight: " + jQuery('#'+tableId+'_BodyDiv').outerHeight(true) + " nextUpperElementPosition.y: " + nextUpperElementPosition.y);
	            	//alert("reajusto a " + (parseInt(nextUpperElementPosition.y) - parseInt(tblPosition.y)));
	            	//console.debug("reajusto a " + (parseInt(nextUpperElementPosition.y) - parseInt(tblPosition.y)));
	            	jQuery('#'+tableId+'_BodyDiv').height(parseInt(nextUpperElementPosition.y) - parseInt(tblPosition.y));
	            }
    		}
    		//fixCellBorders(oTable);
    		//alert("hago visible la tabla");
    		if (jQuery("#"+tableId+"_tblFxHeadr").length > 0) {
    			jQuery("#"+tableId+"_tblFxHeadr").find("table").css("visibility","visible");
    		} else if (jQuery('#'+tableId+'.fixedHeaderTable').length > 0){
    			jQuery('#'+tableId+'.fixedHeaderTable').css("visibility","visible");
    		}
    		fin(window);
    	}
    	//console.log(">>> loadFixedHeaderTables("+tableId+", "+fixedHeight+") END");
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

fin();