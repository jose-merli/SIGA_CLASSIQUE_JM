/*  METODO OBLIGATORIO EN EVENTO onLoad DE TODAS LAS P�GINAS
	Inicia estilos para todas las p�ginas que importen este fichero
    Es necesario definir la hoja de estilos SIGA.css con el atributo ID="default" */
    
//Disable right mouse click Script

var message="Funci�n Deshabilitada!";

var semaforoPestana=true;

function cuenta(obj, len){
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		if((keycode!=8)&&(keycode!=46)) {	 
			if(obj.value.length > len-1) obj.value = obj.value.substring(0,len-1);
		}
}


///////////////////////////////////
function clickIE4(){
if (event.button==2){
//alert(message);
return false;
}
}

function clickNS4(e){
if (document.layers||document.getElementById&&!document.all){
if (e.which==2||e.which==3){
//alert(message);
return false;
}
}
}

if (document.layers){
document.captureEvents(Event.MOUSEDOWN);
document.onmousedown=clickNS4;
}
else if (document.all&&!document.getElementById){
document.onmousedown=clickIE4;
}

//document.oncontextmenu=new Function("alert(message);return false")

// -->*/       
    
 function initStyles() {
 	var defStyle=document.styleSheets('default').href;
	var logo=document.all.logoImg;

	var liob=0;idx=-1;
	while((idx=defStyle.indexOf('/', idx+1))!=-1) {
		liob=idx;
	}
	var stylesPath=defStyle.substring(0, liob+1);

	if(logo) {
		var imagesPath=logo.src;
		liob=0;idx=-1;
		while((idx=imagesPath.indexOf('/', idx+1))!=-1) {
			liob=idx;
		}
		imagesPath=imagesPath.substring(0, liob+1);
	}
	
	if(top.loc=='MURCIA') {
		document.styleSheets('default').href=stylesPath+"murcia.css";
		if(logo) {
			logoImg.src=imagesPath+'logoMurcia.gif';
		}
	} else if(top.loc=='GIJON') {
		document.styleSheets('default').href=stylesPath+"gijon.css";
		if(logo) {
			logoImg.src=imagesPath+'logoGijon.gif';
		}
	} else if(top.loc=='MALAGA') {
		document.styleSheets('default').href=stylesPath+"malaga.css";
		if(logo) {
			logoImg.src=imagesPath+'logoMalaga.gif';
		}
	} else if(top.loc=='ZARAGOZA') {
		document.styleSheets('default').href=stylesPath+"zaragoza.css";
		if(logo) {
			logoImg.src=imagesPath+'logoZaragoza.gif';
		}
	} else if(top.loc=='MELILLA') {
		document.styleSheets('default').href=stylesPath+"melilla.css";
		if(logo) {
			logoImg.src=imagesPath+'logoMelilla.gif';
		}
	}
 }
   


/*
	Al usar Javascript y referencias directas entre p�ginas HTML,
	el prototipo necesita que los formularios sean de tipo GET,
	para poder realizar el paso de par�metros entre distintos formularios
	abiertos en el mismo target.
	Este m�todo implementa la funcionalidad de obtener un array de dos dimensiones
	(pares nombre/valor) conteniendo los par�metros que se pasan en una peticion get.
*/
function getFormParameters() {
	var allArgs=new Array();
	var counter=0;
	//el primer caracter es '?', y lo sustitu�mos por '&' para la busqueda de parametros
	var argsString='&'+document.location.search.substring(1);
	var idx=-1;
	while((idx=argsString.indexOf('&',idx+1))!=-1) {
		var endBlock=argsString.indexOf('&',idx+1);
		var param='';
		if(endBlock!=-1) 
			param=argsString.substring(idx+1,endBlock);
		else 
			param=argsString.substring(idx+1);
		var paramName=param.substring(0, param.indexOf('='));
		var paramValue=param.substring(param.indexOf('=')+1);
		allArgs[counter]=new Array();
		allArgs[counter][0]=paramName;
		allArgs[counter][1]=paramValue;
		counter++;
	}
	return allArgs;
}

function getParameterValue(sName) {
	var allParams=getFormParameters();
	var toReturn='';
	for (i=0;i<allParams.length;i++) {
		if(allParams[i][0]==sName) {
			toReturn=allParams[i][1];
			break;
		}
	}
	return toReturn;
}

/* Metodos para imagenes */

/* TNF 
	Funci�n para dejar una imagen asociada a un link activada tras pulsar sobre �ste.
	El link no debe tener atributo class, y cualquier link en la misma p�gina que no lleve
	imagenes, debe tener alg�n valor para ese atributo
*/
function activateLink(objLink) {
	var links=document.getElementsByTagName('a');
	var linksToSwap=new Array();
	var j=0;
	for(i=0; i<links.length;i++) {
		var lnk=links[i];
		if(lnk.className) continue;
		if(lnk==objLink) {
			lnk.onmouseout='';
		} else {
			lnk.onmouseout=new Function('MM_swapImgRestore()');
			linksToSwap[j]=lnk;
			j++;
		}
	}
	
	for(i=0;i<linksToSwap.length;i++) {
		linksToSwap[i].onmouseover();
		linksToSwap[i].onmouseout();
	}
	objLink.onmouseover();
}

/**
 * Rellena el atributo value con los 
 */
function rellenarFichaCliente(codCliente) {
	var cliente=clientes[new Number(codCliente)];
	if(document.all.numero) 	numero.value=cliente[1];
	if(document.all.nombre) 	nombre.value=cliente[2];
	if(document.all.apel1)  	apel1.value=cliente[3];
	if(document.all.apel2)  	apel2.value=cliente[4];
	if(document.all.NIF)    	NIF.value=cliente[5];
	if(document.all.genero) 	genero.value=cliente[10];
	if(document.all.fecNac) 	fecNac.value=cliente[11];
	if(document.all.lugarNac) 	lugarNac.value=cliente[12];
	if(document.all.NSS)		NSS.value=cliente[13];
	if(document.all.EC)			EC.value=cliente[14];	
	if(document.all.fecPresenta)fecPresenta.value=cliente[16];
	if(document.all.fecIncorp) 	fecIncorp.value=cliente[17];	
	if(document.all.fecJura) 	fecJura.value=cliente[16];
	if(document.all.diasColeg) 	diasColeg.value=cliente[17];	
	if(document.all.tipoCuota)	tipoCuota.value=cliente[16];

}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


//	METODOS PARA CAMBIAR EL TITULO
//	Una o varias funciones aplicadas en cada p�gina seran las encargadas
//	de modificar la linea de navegacion y el titulo en la barra de titulo 
//	de la cabecera de las paginas. Esto es, en la pagina main de la aplicacion

function setTitulo(preTitulo, tit) 
{
	var titulo = tit
	if (preTitulo != "") {
		titulo = preTitulo + " - " + tit;
	}
	document.title = titulo;
	
	var tdtitulo = document.getElementById("titulo");
	if (tdtitulo) {
		tdtitulo.innerText=tit;	
	} else {
		//  VENTANA MODAL
		// esto no funciona, pero no da error. Queda por desarrollar
		tdtitulo = window.document.all.tags("title");
		window.name.value=tit;
		if (tdtitulo) {
			tdtitulo.innerText=tit;
		}
	}
}

function setLocalizacion(localizacion) {
	var tdlocalizacion = document.getElementById("barraNavegacion");
	if (tdlocalizacion) {
		tdlocalizacion.value=localizacion;	
	}
}

// VENTANAS MODALES
//	 Abre una ventana modal con la pagina ventanaModal.jsp, que recibe 
//	 como parametros el nombre de la transaccion y los parametros que necesita
//	 para poder abrir esta transaccion en un iframe que contiene ventanaModal.jsp
//	 ESTE PROCEDIMIENTO ES NECESARIO PARA EL CORRECTO FUNCIONAMIENTO
//	 DE AQUELLO QUE SE ABRE DENTRO DE UNA VENTANA MODAL.

function ventaModalGeneral(nombreFormulario,tamano){
	var formulario = document.getElementById(nombreFormulario);
	
	// cargo el action en un campo que se llama siempre actionModal
	formulario.actionModal.value=formulario.action;
	// envio los campos del formulario como parametro con el action incluido en ellos
	if (tamano=="G") {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp",formulario,"dialogHeight:650px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	} else 
	if (tamano=="M") {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp",formulario,"dialogHeight:440px;dialogWidth:700px;help:no;scroll:no;status:no;");
	} else 
	if (tamano=="P") {
	  
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp",formulario,"dialogHeight:340px;dialogWidth:500px;help:no;scroll:no;status:no;");
		
	} else {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp",formulario,"dialogHeight:590px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	}
}	

function cierraConParametros(valores) {
	window.returnValue=valores;
	window.close();
}

function ventaModalGeneralScrollAuto(nombreFormulario,tamano){
	var formulario = document.getElementById(nombreFormulario);
	
	// cargo el action en un campo que se llama siempre actionModal
	formulario.actionModal.value=formulario.action;
	// envio los campos del formulario como parametro con el action incluido en ellos
	if (tamano=="G") {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1",formulario,"dialogHeight:575px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	} else 
	if (tamano=="M") {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1",formulario,"dialogHeight:425px;dialogWidth:700px;help:no;scroll:no;status:no;");
	} else 
	if (tamano=="P") {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1",formulario,"dialogHeight:325px;dialogWidth:500px;help:no;scroll:no;status:no;");
	} else {
		return showModalDialog("/SIGA/html/jsp/general/ventanaModal.jsp?scroll=1",formulario,"dialogHeight:575px;dialogWidth:1000px;help:no;scroll:no;status:no;");
	}
}	

var iconoinhabilidado='';

function subicono(identificador) {
	//if (top.document.getElementById("velo")) {
	//	top.document.getElementById("velo").style.visibility="visible";
	//}

	iconoinhabilidado=identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);
		for (ii=0;ii<iconos.length;ii++) {
		iconos[ii].disabled=true;
	}
	
}
function finsubicono(identificador) { 
	//if (top.document.getElementById("velo")) {
	//	top.document.getElementById("velo").style.visibility="hidden";
	//}

	iconoinhabilidado=identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);
	
	for (ii=0;ii<iconos.length;ii++) {
		iconos[ii].disabled=false;
	}
	
}
/*
function deshabilitariconos(identificador) {
	iconoinhabilidado=identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);
	for (ii=0;ii<iconos.length;ii++) {
		iconos[ii].disabled=true;
	}
	
}
function habilitariconos(identificador) {
	iconoinhabilidado=identificador;
	var iconos = document.getElementsByName(iconoinhabilidado);
	for (ii=0;ii<iconos.length;ii++) {
		iconos[ii].disabled=false;
	}
	
}
*/



function sub(doc) {
	//alert("sub= "+window.semaforoPestana + " " + window.name);
	if(doc == undefined) {
		doc = document;
	}
	
	
	//if (top.doc.getElementById("velo")) {
	//	top.doc.getElementById("velo").style.visibility="visible";
	//}
	
	var iconos = doc.getElementsByName("iconoboton");
	for (ii=0;ii<iconos.length;ii++) {
		iconos[ii].disabled=true;
	}
	
	
	
	
	var pestana = doc.getElementsByName("pestana");
	
	
	for (p=0;p<pestana.length;p++) {
		pestana[p].disabled=true;
		
	}
	
	var pestanaExt = doc.getElementsByName("pestanaExt");
	for (pe=0;pe<pestanaExt.length;pe++) {
		pestanaExt[pe].disabled=true;
	}
	
	var buts = doc.getElementsByTagName("input");
	
//var buts = doc.getElementsByName("idButton");
	for (ii=0;ii<buts.length;ii++) {
		if (buts[ii].type=='button') {
			buts[ii].disabled=true;
		}
	}
	// por cada frame hijo
	var framess = doc.frames;
	for (j=0;j<framess.length;j++) {
		var buts2=framess[j].document.getElementsByName("idButton");
		for (k=0;k<buts2.length;k++) {
			buts2[k].disabled=true;
		}
		var iconos2 = framess[j].document.getElementsByName("iconoboton");
		for (ii=0;ii<iconos2.length;ii++) {
			iconos2[ii].disabled=true;
		}
		var pestana2 = framess[j].document.getElementsByName("pestana");
	
	
		for (p2=0;p2<pestana2.length;p2++) {
			pestana2[p2].disabled=true;
		}
		
		var pestanaExt2 = framess[j].document.getElementsByName("pestanaExt");
		for (pe2=0;pe2<pestanaExt2.length;pe2++) {
			pestanaExt2[pe2].disabled=true;
		}
		
		
		
	}
	// por el padre
	var framePadre = window.parent;
	
	if (framePadre) {
		var buts3=framePadre.document.getElementsByName("idButton");
		for (k=0;k<buts3.length;k++) {
			buts3[k].disabled=true;
		}
		var pestana3 = framePadre.document.getElementsByName("pestana");
	
	
		for (p3=0;p3<pestana3.length;p3++) {
			pestana3[p3].disabled=true;
		}
		
		var pestanaExt3 =framePadre.document.getElementsByName("pestanaExt");
		for (pe3=0;pe3<pestanaExt3.length;pe3++) {
			pestanaExt3[pe3].disabled=true;
		}
		var frameAbuelo = framePadre.parent;
		if(frameAbuelo){
			var butsAbuelo=frameAbuelo.document.getElementsByName("idButton");
			for (k=0;k<butsAbuelo.length;k++) {
				butsAbuelo[k].disabled=true;
			}
			var pestanaAbuelo = frameAbuelo.document.getElementsByName("pestana");
		
		
			for (p3=0;p3<pestanaAbuelo.length;p3++) {
				pestanaAbuelo[p3].disabled=true;
			}
			
			var pestanaAbueloExt =frameAbuelo.document.getElementsByName("pestanaExt");
			for (pe3=0;pe3<pestanaAbueloExt.length;pe3++) {
				pestanaAbueloExt[pe3].disabled=true;
			}
			
			//Para los Bisabuelos
			var frameBisabuelo = frameAbuelo.parent;
			if(frameBisabuelo){
				var butsBisabuelo=frameBisabuelo.document.getElementsByName("idButton");
				for (k=0;k<butsBisabuelo.length;k++) {
					butsBisabuelo[k].disabled=true;
				}
				var pestanaBisabuelo = frameBisabuelo.document.getElementsByName("pestana");
			
			
				for (p3=0;p3<pestanaBisabuelo.length;p3++) {
					pestanaBisabuelo[p3].disabled=true;
				}
				
				var pestanaBisabueloExt =frameBisabuelo.document.getElementsByName("pestanaExt");
				for (pe3=0;pe3<pestanaBisabueloExt.length;pe3++) {
					pestanaBisabueloExt[pe3].disabled=true;
				}
				
			}
			
		}
		
		

	}
	//alert("sub()");
	
	
	return true;
}

function fin(doc) {
	if(doc == undefined) {
		doc = document;
	}
	
	
	//if (top.document.getElementById("velo")) {
	//	top.document.getElementById("velo").style.visibility="hidden";
	//}

	if (iconoinhabilidado!='') {
		var iconos = doc.getElementsByName(iconoinhabilidado);
		for (ii=0;ii<iconos.length;ii++) {
			iconos[ii].disabled=false;
		}
	}
	
	var pestana = doc.getElementsByName("pestana");
	window.semaforoPestana=true;
	//alert("fin window "+window.name + " "+ window.semaforoPestana);
	for (p=0;p<pestana.length;p++) {
		pestana[p].disabled=false;
	}
	
	var pestanaExt = doc.getElementsByName("pestanaExt");
	for (pe=0;pe<pestanaExt.length;pe++) {
		pestanaExt[pe].disabled=false;
	}
	
	
/*
	var buts = document.getElementsByTagName("input");
	for (ii=0;ii<buts.length;ii++) {
		if (buts[ii].type=='button') {
			buts[ii].disabled=false;
		}
	}
	*/
	var buts = doc.getElementsByName("idButton");
	for (ii=0;ii<buts.length;ii++) {
		buts[ii].disabled=false;
	}
	buts = parent.document.getElementsByName("idButton");
	for (ii=0;ii<buts.length;ii++) {
		buts[ii].disabled=false;
	}


	// por cada frame hijo
	var framess = doc.frames;
	for (j=0;j<framess.length;j++) {
		var buts2=framess[j].document.getElementsByName("idButton");
		for (k=0;k<buts2.length;k++) {
			buts2[k].disabled=false;
		}
		var pestana2 = framess[j].document.getElementsByName("pestana");
		framess[j].semaforoPestana=true;
		//alert("fin hijo "+framess[j].name + " "+ framess[j].semaforoPestana);
		
		for (p2=0;p2<pestana2.length;p2++) {
			pestana2[p2].disabled=false;
		}
		
		var pestanaExt2 = framess[j].document.getElementsByName("pestanaExt");
		for (pe2=0;pe2<pestanaExt2.length;pe2++) {
			pestanaExt2[pe2].disabled=false;
		}
		
	}
	// por el padre
	var framePadre = window.parent;
	if (framePadre) {
		var buts3=framePadre.document.getElementsByName("idButton");
		for (k=0;k<buts3.length;k++) {
			buts3[k].disabled=false;
		}
		var pestana3 = framePadre.document.getElementsByName("pestana");
		framePadre.semaforoPestana=true;
		//alert("fin padre "+framePadre.name + " "+ framePadre.semaforoPestana);
		for (p3=0;p3<pestana3.length;p3++) {
			pestana3[p3].disabled=false;
		}
		
		var pestanaExt3 =framePadre.document.getElementsByName("pestanaExt");
		for (pe3=0;pe3<pestanaExt3.length;pe3++) {
			pestanaExt3[pe3].disabled=false;
		}
		//Para los abuelos
		var frameAbuelo = framePadre.parent;
		if(frameAbuelo){
			var butsAbuelo=frameAbuelo.document.getElementsByName("idButton");
			for (k=0;k<butsAbuelo.length;k++) {
				butsAbuelo[k].disabled=false;
			}
			var pestanaAbuelo = frameAbuelo.document.getElementsByName("pestana");
			frameAbuelo.semaforoPestana=true;
			//alert("fin abuelo "+frameAbuelo.name + " "+ frameAbuelo.semaforoPestana);
			
			for (p3=0;p3<pestanaAbuelo.length;p3++) {
				pestanaAbuelo[p3].disabled=false;
			}
			
			var pestanaAbueloExt =frameAbuelo.document.getElementsByName("pestanaExt");
			for (pe3=0;pe3<pestanaAbueloExt.length;pe3++) {
				pestanaAbueloExt[pe3].disabled=false;
			}
			
			//Para los Bisabuelos
			var frameBisabuelo = frameAbuelo.parent;
			if(frameBisabuelo){
				var butsBisabuelo=frameBisabuelo.document.getElementsByName("idButton");
				for (k=0;k<butsBisabuelo.length;k++) {
					butsBisabuelo[k].disabled=false;
				}
				var pestanaBisabuelo = frameBisabuelo.document.getElementsByName("pestana");
				frameBisabuelo.semaforoPestana=true;
				//alert("fin bisabuelo "+frameBisabuelo.name + " "+ frameBisabuelo.semaforoPestana);
			
				for (p3=0;p3<pestanaBisabuelo.length;p3++) {
					pestanaBisabuelo[p3].disabled=false;
				}
				
				var pestanaBisabueloExt =frameBisabuelo.document.getElementsByName("pestanaExt");
				for (pe3=0;pe3<pestanaBisabueloExt.length;pe3++) {
					pestanaBisabueloExt[pe3].disabled=false;
				}
				
			}
			
		}
		
		
	}
		
		
		

	
}

  function trim(s) {
      return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }



function ajusteAlto(nObj)
{
	ajusteAltoMain(nObj, 0);
}

function ajusteAltoBotones(nObj)
{
	ajusteAltoMain(nObj, 32);
}

function ajusteAltoBotonesPaginador(nObj)
{
	ajusteAltoMain(nObj, 52);
}

function ajusteAltoPaginador(nObj)
{
	ajusteAltoMain(nObj, 20);
}

function ajusteAltoMain(nObj, menos)
{
	//alert("ajusteAlto "+menos);
	//alert("ajusteAlto "+nObj);
	var obj=document.getElementById(nObj);
	if (obj) {
		var cont = obj.parentElement;
		var hCont = cont.offsetHeight;
		//alert("YO="+obj.id + " : " + obj.nodeName + " : " + obj.offsetTop);
		if (obj.nodeName=='IFRAME') {
			hCont = cont.parentElement.offsetHeight - obj.offsetTop;
			//alert("final iframe="+hCont);
			if (nObj=='mainWorkArea') {
				hCont=hCont-5;
			}
			obj.style.height=hCont-menos;
		} else {
			if (cont.nodeName=='FORM') {
				hCont = cont.parentElement.offsetHeight;
			}
			//alert("Padre body="+cont.id + " : " + cont.nodeName + " : " + hCont);
			var hij=cont.children;
			for(x=0;x<hij.length;x++){
				if (hij(x).nodeName!='SCRIPT' && hij(x).nodeName!='INPUT' && hij(x).nodeName!='#comment' && hij(x).offsetHeight!=0) {
				    if (hij(x).id!=nObj ) {
						//alert("hijo menos="+hij(x).id+" : "+hij(x).nodeName + " : " +hij(x).offsetHeight );
						hCont = hCont - hij(x).offsetHeight;
					} 
				}
			}
			//alert("final tabla="+hCont);
			obj.style.pixelHeight=hCont-menos;
		}
	}
}

function pintaToolTipDac(ver, id) 
{
	o = document.getElementById(id);
	if (ver == 'on') {
//			o.style.width  = 100;
//			o.style.height = 100;
//			o.style.zIndex  = 0;
			o.style.display  = "block";
	}
	else {
//			o.style.height = 1;
//			o.style.width  = 1;
//			o.style.zIndex  = 5;
			o.style.display = "none";
	}
	return;
}


function pintaToolTipDacDegradado (onOff, texto, colorFondo, colorTexto, tipoLetra) 
{
	if (onOff == 'on') {
		posX = window.event.x;
		posY = window.event.y;
		
		if (posX < 0) posX = 0;
		posX += 5;
	
		if (posY < 0) posY = 0;
		posY += 5;

		var oPopup   = window.createPopup();
		var oPopBody = oPopup.document.body;

		colorFinal = parseInt("0x"+ colorFondo, "0x"); //- 0x181818;
		colorFinal = colorFinal.toString(16);

		texto = "<div style='position:absolute; top:0; left:0; width:350px; height:80px; padding:5px;" +
	                 "filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1,startColorstr=#"+ colorFondo + ", endColorstr=#" + colorFinal + ")'>" + 
	                     texto +  
	             "</div>";

		oPopBody.style.backgroundColor = colorFondo;
		oPopBody.style.border = "solid 1px #999999";
		oPopBody.style.fontSize = "12";
		oPopBody.style.fontFamily = tipoLetra;
		oPopBody.style.color = colorTexto;
		oPopBody.innerHTML = texto;
		oPopup.show(posX, posY, 350, 80, document.body);
	}
	else {
		var oPopup   = window.createPopup();
		oPopup.show(0, 0, 0, 0, document.body);
		oPopup.hide();
	}
}


function submitConTeclaEnter()
{
	var keycode;
	if (window.event)  {
		keycode = window.event.keyCode;
	}
	if (keycode == 13) {
		buscar();return false;
	}
}

function registrarEnterFormularios () 
{
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

function setFocusFormularios () 
{
	for (i = 0; i < document.forms.length; i++) {
		elementos = document.forms[i].elements;
		for (j = 0; j < elementos.length; j++) {

			if (elementos[j].type == "hidden" || elementos[j].readOnly) 
				continue;

			if (elementos[j].type == "text" || elementos[j].type == "checkbox" || elementos[j].type == "select-one") {
				try
				{
					elementos[j].focus();
				}
				catch(err)
				{
					continue;
				}
				return false; 
			}
		} 
	}
}


/* Para limpiar la seleccion en los tagCombo.
   idCombo = nombre del combo (p.e. name="pais" --> idCombo="pais")
   valor = id del elemento a seleccionar. Si es "" no se selecciona ningun elemento
*/
function seleccionComboSiga (idCombo, valor) 
{
  

	combo = top.frames[0].document.frames[0].document.getElementById(idCombo+'Frame');
	
	if (!combo) {
		combo = top.frames[0].document.getElementById(idCombo+'Frame');
	}
  	if(!combo)
  		return;
	var cadena = combo.src; 

	var ini = cadena.indexOf('&elementoSel=[');
	if (ini == -1) {
		return;
	}

	var fin = cadena.indexOf('&', ini+1);
	if (fin == -1) { 
		combo.src = cadena.substring(0,ini) + "&elementoSel=";
		return;
	}

	valor = "[" + valor + "]";
	combo.src = cadena.substring(0,ini) + "&elementoSel=" + valor + cadena.substring(fin);
}



var numb = '0123456789';
var lwr = 'abcdefghijklmnopqrstuvwxyz';
var upr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

function isValid(parm,val) {
	if (parm == "") return true;
	for (i=0; i<parm.length; i++) {
		if (val.indexOf(parm.charAt(i),0) == -1) return false;
	}
	return true;
}

function isNumero(parm) {return isValid(parm,numb);}
function isMinuscula(parm) {return isValid(parm,lwr);}
function isMayuscula(parm) {return isValid(parm,upr);}
function isAlfa(parm) {return isValid(parm,lwr+upr);}
function isAlphanum(parm) {return isValid(parm,lwr+upr+numb);} 
function replaceAll( text, busca, reemplaza ){  
	while (text.toString().indexOf(busca) != -1){  
    	text = text.toString().replace(busca,reemplaza);  
     }  
     return text;  
}  


fin();


/**
 * Redondea num con dec posiciones decimales
 * @param num numero a redondear
 * @param dec posiciones decimales que se quieren obtener
 * @return num redondeado con dec posiciones decimales
 */
function roundNumber(num, dec) {
	var result = Math.round( Math.round( num * Math.pow( 10, dec + 1 ) ) / Math.pow( 10, 1 ) ) / Math.pow(10,dec);
	return result;
}

/**
 * Devuelve el objeto padre mas cercano de tipo <code>type</code> del elemento <code>element</code>. 
 * @param element Elemento del cual se quiere obtener su padre. 
 * @param type Tipo del elemento padre.
 * @return el elemento padre de tipo <code>type</code> mas cercano a <code>element</code> o <code>undefined</code> 
 * si no lo encuentra.
 */
function getParentOfType(element, type){
	var parent = element.parentNode;
	while (parent!=null && parent!=undefined && 
		   parent.nodeName.toUpperCase() != type.toUpperCase()){
		parent = parent.parentNode;
	}

	return parent;
}

/**
 * Devuelve el form que contiene al elemento.
 * @param element elemento del cual se quiere obtener el form que lo contiene.
 */
function getParentForm(element){
	return getParentOfType(element, "FORM");
}

/**
 * Devuelve la celda <code>cellId</code> de la fila con identificador/PK igual a <code>idValue</code> 
 * de la tabla <code>tableId</code> donde <code>idIndex</code> es la columna que contiene el identificador.
 * @param tableId Identificador de la tabla.
 * @param idValue Valor del identificador del registro que se desea seleccionar.
 * @param idIndex Columna de la tabla que contiene el identificador.
 * @param cellId  Indice de la celda, dentro de la fila, que se quiere devolver.
 */

function getCellValue(tableId, idValue, IdIndex, cellId){
	return getRowById(tableId, idValue, IdIndex).cells[cellId].innerHTML;
}

/**
 * Devuelve la fila con identificador/PK igual a <code>idValue</code> en la tabla <code>tableId</code> donde 
 * <code>idIndex</code> es la columna que contiene el identificador.
 * @param tableId Identificador de la tabla.
 * @param idValue Valor del identificador del registro que se desea seleccionar.
 * @param idIndex Columna de la tabla que contiene el identificador.
 */
function getRowById(tableId, idValue, idIndex){
	try {
		var table = document.getElementById(tableId);
		var rowCount = table.rows.length;

		for(var i=0; i<rowCount; i++) {
			var row = table.rows[i];

			var cellCount = row.cells.length;
			for(var j=0; j<cellCount; j++) {
				var cell = row.cells[idIndex];
				if (cell.innerHTML == idValue){
					return row;
				}
			}
		}
	}catch(e) {
		alert(e);
	}
}

/**
 * Selecciona la fila con identificador/PK igual a <code>idValue</code> en la tabla <code>tableId</code>.
 * <code>idIndex</code> es la columna que contiene el identificador.
 * @param tableId Identificador de la tabla.
 * @param idValue Valor del identificador del registro que se desea seleccionar.
 * @param idIndex Columna de la tabla que contiene el identificador.
 */
function selectRow(tableId, idValue, idIndex) {
	var table = document.getElementById(tableId);
	if(table==null)
		return;

	var rowCount = table.rows.length;

	for(var i=0; i<rowCount; i++) {
		var row = table.rows[i];
		var cell = row.cells[idIndex];
		if (cell.innerHTML == idValue){
			row.style.backgroundColor =  getStyle('backgroundColor', 'listaNonEditSelected', 'stylesheet2');
		}
		else{
			if (i%2 == 0){ 
				row.style.backgroundColor =  getStyle('backgroundColor', 'even', 'stylesheet2');
			}
			else{
				row.style.backgroundColor =  getStyle('backgroundColor', 'odd', 'stylesheet2');
			}
		}
	}
}

/**
 * Comprueba si existe algun registro seleccionado en el formulario <code>formName</code>
 * @param formName nombre del formulario sobre el que se va a comprobar si existe algun registro seleccionado.
 * @return <code>true</code> si existe algun registro seleccionado, <code>false</code> en caso contrario.
 */
function existsSelected(formName){
	var elements = document.getElementById(formName).elements;
	for (i=0; i<elements.length; i++){
		if(elements[i].type == "checkbox" && elements[i].checked)
			return true;
	}
	return false;
}

/**
 * Funcion generica para llamar al metodo de un action pasandole una accion a realizar
 * @param formName Nombre del formulario  
 * @param id PK del registro sobre el que se va a realizar la accion
 * @param accion Nombre del metodo del action
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
 * @param formName Nombre del formulario  
 * @param id PK del registro sobre el que se va a realizar la accion
 */
function consultar(id, formName) {
	submitItemAction(id, formName, "ver");
}

/**
 * Funcion generica de edicion de un registro 
 * @param formName Nombre del formulario  
 * @param id PK del registro sobre el que se va a realizar la accion
*/
function editar(id, formName) {
	submitItemAction(id, formName, "editar");
}

/**
 * Funcion generica de consulta de un registro
 * @param formName Nombre del formulario  
 * @param id PK del registro sobre el que se va a realizar la accion
 */
function consultar(id, formName) {
	submitItemAction(id, formName, "ver");
}

/**
 * Funcion generica de edicion de un registro 
 * @param formName Nombre del formulario  
 * @param id PK del registro sobre el que se va a realizar la accion
*/
function informacionLetrado(id, formName) {
	submitItemAction(id, formName, "informacionLetrado");
}

/**
 * Obtiene el valor de un artibuto de una clase de una hoja de estilo
 */
function getStyle(atributo, clase, css){
	var theRules = new Array();
	var theCss;
	var theStyle;
	
	for(i=0; i<document.styleSheets.length; i++){
		if(document.styleSheets[i].href.match(css)){
			theCss = document.styleSheets[i];
			break;
		}
	}
	
	if (theCss.cssRules) // Comprobaci�n de reglas de Estilos en Firefox
	{
		theRules = theCss.cssRules;
	}
	else
	{
		if (theCss.rules) // Comprobaci�n de reglas de Estilos en Internet Explorer
		{
			theRules = theCss.rules;
		}
	}

	for(elem in theRules) // Recorro las reglas de estilos CSS
	{
		if(typeof theRules[elem] == "object") // si el elemento que estoy recorriendo es un Objeto..
		{
			for(elem2 in theRules[elem]) // Recorro el objeto
			{
				if(theRules[elem].selectorText == "." + clase) // Si la clase que estoy recorriendo es la que deseo buscar... 
				{
					if(elem2 == "style") // Si dentro de la clase estoy en el objeto STYLE
					{
						foundedClass = true;
						theStyle = theRules[elem][elem2];
					}
				}
			}
		}
	}
	return eval("theStyle."+atributo);
}

/**
 * Ajusta el height de un elemento al maximo del espacio que quede por ocupar 
 * @param nObj
 */
function ajusteAltoDisplayTag(nObj)
{
	var obj=document.getElementById(nObj);
	if (obj) {
		var height;
		if (window.name == "mainWorkArea"){
			height = window.document.documentElement.offsetHeight;
		}
		else if (window.parent.name != "mainWorkArea"){
			height = window.parent.document.getElementById("MainWorkArea").offsetHeight;
		}
		var padre = obj.parentNode;
		var hermanos = obj.parentNode.childNodes;
		var contHermanos = 0;
//		alert(ventana.offsetHeight +" "+ obj.offsetTop);
		for(x=0;x<hermanos.length;x++){
//			alert(hermanos(x).id + " : " + hermanos(x).nodeName + " : " + hermanos(x).offsetHeight );
			if (hermanos(x).nodeName!='SCRIPT' &&  
				hermanos(x).nodeName!='#comment' && hermanos(x).offsetHeight!=0 &&
					hermanos(x)!=obj && hermanos(x).offsetHeight) {
				contHermanos = contHermanos + hermanos(x).offsetHeight;
			}
		}

		//el 5 es un ajuste que me saco de la manga, mas o menos, habr� que
		//terminar de calcular bien el tamanyo, teneiendo en cuenta
		//los margenes, paddings y demas
		var cont = height - obj.offsetTop - (2*contHermanos) - 5;
//		alert(ventana.offsetHeight +" "+ obj.offsetTop +" "+ contHermanos + " " + cont);
		obj.style.pixelHeight=cont;
	}
}

/**
 * Borra los valores de los elementos input y select de un formulario
 * @param form
 */
function limpiarForm(form){
	var inputs = form.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++){
		inputs[i].value = "";
	}
	var selects = form.getElementsByTagName("select");
	for(var i=0;i<selects.length;i++){
		selects[i].value = "";
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
         
         for (var n = startFrom; n < argvalue.length; n++) {
             if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
         }
         return true;
}


/**
 * Muestra un DIV o lo oculta
 * @param ident El identificador del DIV que vamos a ocultar/mostrar
 * @return
 */
function ocultarDIV(ident){
	if(document.getElementById(ident).style.display == "none" ) {
		document.getElementById(ident).style.display = "inline";
		document.getElementById(ident).parentElement.className='legend';
		document.getElementById(ident+"ImMas").style.display = "none"
		document.getElementById(ident+"ImMenos").style.display = "inline-block"
	}else {
		document.getElementById(ident).style.display = "none";
		document.getElementById(ident).parentElement.className='legendNoBorder';
		document.getElementById(ident+"ImMenos").style.display = "none"
		document.getElementById(ident+"ImMas").style.display = "inline-block"
	}
	ajusteAltoPaginador('resultado');
	//ajusteAlto();
	ajusteAlto('resultado');
	ajusteAlto('mainWorkarea');
	return true;
}
