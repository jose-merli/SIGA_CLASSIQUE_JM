
/********************************************* Validaciones de formatos ****************************************************/
var errMessages;
var errNumber;
var err_msg = new Array();
err_msg [0]=' Comprobando...';
err_msg [1]=' no puede ser vacío.';
err_msg [2]=' no está en el formato adecuado.';
err_msg [3]=' debe tener 4 digitos en el año.';
err_msg [4]=' (año)';
err_msg [5]=' tiene un dia incorrecto.';
err_msg [6]=' tiene un mes incorrecto.';
err_msg [7]=' su parte entera es más grande de lo esperado.';
err_msg [8]=' su parte decimal es más grande de lo esperado.';
err_msg [9]=' es más grande de lo esperado';
err_msg [10]=' error/es encontrados.';
err_msg [11]=' No se encontraron errores.';
err_msg [12]=' Demasiados errores encontrados, validación abortada.';
err_msg [13]=' es más largo de lo esperado.';
err_msg [14]=' :no puede ser mayor que la fecha de finalización';
err_msg [15]=' :intervalo incorrecto';
err_msg [16]=' no es un número válido';
err_msg [17]=' (mes)';
err_msg [18]=' (dia)';
err_msg [19]=' debe ser positivo.';
err_msg [20]=' La cantidad diaria introducida debe de ser menor';
err_msg [21]=' minutos no válidos';
err_msg [22]=' hora no válida';

function addMessages (message){errMessages+=(message+"\n");errNumber++;}
function validateInit (){errMessages="";errNumber=0;}
function val_msg (index){return (err_msg[index]);}
function getMessages (){return (errMessages);}
function getNumberOfErrors (){return (errNumber);}
function checkDate (obj, notNull, id){
 var elem;
 var out;
 obj=trim(obj);
 if (obj=='') {
  if (notNull) {addMessages ('- '+id+ val_msg(1)); return(false);}
  else return;
 }
 elem=obj.split ("/");
 if (elem.length!=3) {addMessages ('- '+id+ val_msg(2));return (false);}
 elem[0]=trim (elem[0]);
 elem[1]=trim (elem[1]);
 elem[2]=trim (elem[2]);
 if (!isAInteger (elem[2], id+val_msg(4))) return (false);
 if (elem[2].length!=4) {addMessages ('- '+id+	val_msg (3));return (false);}
 if (!isValidDate (id, elem[0], elem[1], elem[2])) return (false);
 if (elem[0].length==1) out="0"+elem[0]+"/";
 else out=elem[0]+"/";
 if (elem[1].length==1) out=out+"0"+elem[1]+"/"+elem[2];
 else out=out+elem[1]+"/"+elem[2];
 obj=out;
 return (true);
}
function checkHour(obj, notNull, id) {
 var elem;
 var out;
 obj=trim(obj);
 if (obj=='') {
  if (notNull) {addMessages ('- '+id+ val_msg(1)); return(false);}
  else return;
 }
 elem=obj.split (":");
 if (elem.length!=2) {addMessages ('- '+id+ val_msg(2));return (false);}
 if (!isAInteger (elem[1], id+val_msg(16))) return (false);
 if (!isAInteger (elem[0], id+val_msg(16))) return (false);
 elem[0]=trim (elem[0]);
 elem[1]=trim (elem[1]);
 if (!isValidHour (id, elem[0], elem[1])) return (false);
 if (elem[0].length==1) out="0"+elem[0]+":";
 else out=elem[0]+":";
 if (elem[1].length==1) out=out+"0"+elem[1];
 else out=out+elem[1];
 obj=out;
 return (true);
}
function isValidHour (id, horas, mins) {
 if (mins<0 || mins>59) {addMessages('- '+id+'.'+val_msg(21));return (false);}
 if (horas<0 || horas>23){addMessages('- '+id+'.'+val_msg(21));return (false);}
 return (true);
}
function isValidDate (id, day, month, year) {
 if (!isAInteger (month, id+val_msg(17)) || !isAInteger(day, id+val_msg(18))) return (false);
 if (month<1 || month>12) {addMessages('- '+id+'.'+val_msg(6));return (false);}
 if (day<1 || day>31 ||
    ((month==4 || month==6 || month==9 || month==11) && day>30) ||
	   (month==2 && (day>29 || (!((year%4==0 && year%100!=0) || year%400==0) && day>28)))){
   addMessages('- '+id+'.'+val_msg(5));
   return (false);
 }
 return (true);
}
function checkString (obj, notNull, id, length) {
 obj=trim(obj.toUpperCase());
 if (notNull && obj=='') {addMessages ('- '+id+ val_msg(1)); return (false);}
 if (!isNaN(length) && obj.length>length) {addMessages ('- '+id+val_msg(13)); return (false);}
 return true;
}
function checkPositiveFloat (obj, notNull, id, maxIntegerLength, maxFloatLength) {
 if (checkFloat (obj, notNull, id, maxIntegerLength, maxFloatLength)) {
  if (parseFloat (obj)<0 ) {
   addMessages ('- '+id+val_msg(19));
   return (false); 
}}
 else return (false);
 return (true);
}
function checkFloat (obj, notNull, id, maxIntegerLength, maxFloatLength){
 var dotPosition;
 obj=trim(obj);
 if (obj==''){
 	if(notNull) {addMessages ('- '+id + val_msg(1));return (false);}
	else return(true);
 }
 if (isANumber (obj, id)) {
  obj=String(parseFloat(obj));
  dotPosition = obj.indexOf ('.');	
  if (dotPosition==-1) dotPosition=obj.length;
  var ch=obj.charAt (0);
  if (ch=="+" || ch=="-") maxIntegerLength++;
  if (!isNaN (maxIntegerLength) && obj.substring (0,dotPosition).length>maxIntegerLength) {addMessages('- '+id + ','+val_msg(7));return (false);}
  if (!isNaN (maxFloatLength) && obj.substring (dotPosition+1).length>maxFloatLength){addMessages('- '+id + ', '+val_msg(8));return (false);}
 }
 else return (false);
 return (true);
}

function checkPositiveInteger (obj, notNull, id, maxIntegerLength) {
	var returnValue=true;
 	if (checkInteger (obj, notNull, id, maxIntegerLength)) {
	 if (parseInt (obj)<0) {addMessages ('- '+id+val_msg(19)); returnValue=false;}
	}
	else returnValue=false;
	return (returnValue);
}


function checkInteger (obj, notNull, id, maxIntegerLength) {
 obj=trim(obj);
 if (obj==''){
  if(notNull) {addMessages ('- '+id+val_msg(1));return (false);}
  else return(true);
 }
 if (isAInteger (obj, id)) {
  obj=String(parseFloat(obj));
  var ch=obj.charAt (0);
  if (ch=="+" || ch=="-") maxIntegerLength++;
  if (!isNaN(maxIntegerLength) && obj.length>maxIntegerLength) {addMessages ('- '+id+val_msg(9));return (false);}
 }
 else return (false);
 return (true);
}
function isAInteger (number, id){return (isANumber (number, id) && number.indexOf (".")==-1);}
function isANumber (number, id){
 var hasError=isNaN (number);
 if (hasError) addMessages ('- '+id + val_msg(16));
 return (!hasError);
}
function isAfter (date1, date2) {
 var dat1A=date1.split("/");
 var dat2A=date2.split("/");
 var dat1D=new Date (parseFloat(dat1A[2]), parseFloat(dat1A[1])-1, parseFloat(dat1A [0]));
 var dat2D=new Date (parseFloat(dat2A[2]), parseFloat(dat2A[1])-1, parseFloat(dat2A [0]));
 return (dat1D>dat2D);
}
function isEquals (date1, date2) {
 var dat1A=date1.split("/");
 var dat2A=date2.split("/");
 var dat1D=new Date (parseFloat(dat1A[2]), parseFloat(dat1A[1])-1, parseFloat(dat1A [0]));
 var dat2D=new Date (parseFloat(dat2A[2]), parseFloat(dat2A[1])-1, parseFloat(dat2A [0]));
 return (dat1D.getTime()==dat2D.getTime())
}
function trim (inputString) {
 var aux=inputString.length;
 var begin=0;
 var end=aux;
 if (inputString.indexOf (" ")==0)for (begin=0; begin<aux && inputString.charAt (begin)==" "; begin++);
 if (inputString.lastIndexOf (" ")==aux-1){
  for (end=aux-1; end>0 && end>begin && inputString.charAt (end)==" "; end--);
  end++;
 }
 return (inputString.substring (begin, end));
}
function getNewDate () {
	var kk;
	if (window.dialogArguments)	kk=window.dialogArguments.top.frame_der.frame_sup.document.clock.getFullDate();
	else if (window.opener)	kk=window.opener.top.frame_der.frame_sup.document.clock.getFullDate();
	else kk=top.frame_der.frame_sup.document.clock.getFullDate();
	return (eval ("new Date ("+kk+")"));
}

function addKChar(){
	for(var cnt=0; cnt<document.formulario.getElementsByTagName("input").length; cnt++){
		var obj = document.formulario.getElementsByTagName("input")[cnt];
		if(obj.type != 'hidden' && obj.value!="" && obj.value!=" " ){
			 if(isANumber(obj.value,"valor")) obj.value = putQttySeparator(trim(obj.value));
		}
	} 
}

function addKCharTable(tab){
	for(var cnt=0; cnt<tab.getElementsByTagName("input").length; cnt++){
		var obj = tab.getElementsByTagName("input")[cnt];
		if(obj.type != 'hidden' && obj.value!="" ){
			 if(isANumber(obj.value,"valor")) obj.value = putQttySeparator(obj.value);
		}
	} 
}

function addKCharRow(tab,fila,inicio){
	for(var cnt=inicio; cnt< tab.rows(0).cells.length; cnt++){
			var obj = tab.rows(fila).cells(cnt).getElementsByTagName("input")(0);
		if(obj.type != 'hidden' && obj.value!="" ){
			 if(isANumber(obj.value,"valor")) obj.value = putQttySeparator(obj.value);
		}
	} 
}

function delKChar(){
	for(var cnt=0; cnt<document.formulario.getElementsByTagName("input").length; cnt++){
		var obj = document.formulario.getElementsByTagName("input")[cnt];
		if(obj.type != 'hidden' && obj.value!="" )
			if(isANumber(objnum=obj.value.replace(/\,+/g,""),"valor")) obj.value=objnum;
	} 
}

function delKCharTable(tab){
	for(var cnt=0; cnt<tab.getElementsByTagName("input").length; cnt++){
		var obj = tab.getElementsByTagName("input")[cnt];
		if(obj.type != 'hidden' && obj.value!="" )
			if(isANumber(objnum=obj.value.replace(/\,+/g,""),"valor")) obj.value=objnum;
	} 
}
function delKCharRow(tab,fila,inicio){
	for(var cnt=inicio; cnt< tab.rows(0).cells.length; cnt++){
		var obj =tab.rows(fila).cells(cnt).getElementsByTagName('input')(0);
		if(obj.type != 'hidden' && obj.value!="" )
		if(isANumber(objnum=obj.value.replace(/\,+/g,""),"valor")) obj.value=objnum;
	} 
}
function putQttySeparator(data) {
	var returnValue="";
	var decimalPart = "";
	var negativ="";
	if(data.substring(0,1) =="-") {
		negativ=data.substring(0,1);
		data = data.substring(1);
	}
	if (data.indexOf(".") != -1) {
		decimalPart = data.substring(data.indexOf("."));
		data = data.substring(0,data.indexOf("."));
	}
	var endLoop = Math.floor((data.length-1)/3);
	for (i=0;i<endLoop+1;i++) {
		returnValue = data.substring(data.length-(3*i)-3,data.length-(3*i)) + "," + returnValue;
	}
	return negativ+returnValue.substring(0,returnValue.length-1) + decimalPart;
}

var decimalSeparatorsAllowed = ["."]  // array literal
var currentDecs;

function admitDecs(objectData,ndecs) {
	var separator = "";
	var maxIntReached = false;
	var data = eval(objectData).value;
	for (i=0;i<decimalSeparatorsAllowed.length;i++) {
		if (data.indexOf(decimalSeparatorsAllowed[i]) != -1) {
			separator = decimalSeparatorsAllowed[i];
			break;
		}
	}
	if (objectData.maxLength != null && data.length == (objectData.maxLength - ndecs - 1) 
			&& event.keyCode != 46 && event.keyCode != 44 && data.indexOf(".") == -1) event.keyCode = null;
	
	if (separator != "") { // si aún no hay separador, no hago nada
		currentDecs = data.substring(data.indexOf(separator)+1).length + 1;
		if (currentDecs > ndecs) event.keyCode = null;  // flush casero del buffer de teclado
	}
}

/*
 * This function limits enter chars that aren´t númerics, "." or "-"
 *
 * f: numeric field
 * SN_negative: True or false depending on the value can be negative or not
 * SN_decimal: True or false depending on the value can be decimal or not
 */

function filterChars(f, SN_negative, SN_decimal) {
	var point = '.';
	var key = event.keyCode;
	if (key == null)
		return true;
	var keyChar = String.fromCharCode(key);
	if (keyChar == null)
		return true;
	if (keyChar == ','){
            event.keyCode=0;
			return false;
	}
	if (SN_decimal) {
   		if (event.keyCode == point.charCodeAt(0)) {
   				if(f.value != '')
   					if(f.value.indexOf('.') != -1){
                        event.keyCode=0;
   						return false;
					}
   				return true;
   			}
   		} else {
   			if (keyChar == '.' || keyChar == ','){
                event.keyCode=0;
   				return false;
			}
   		}

	if (SN_negative && keyChar == '-') {
		if (f.value == '') 
			return true;
		
		if (f.value.indexOf('-') != -1){
            event.keyCode=0;
			return false;
		}
		
		return false;
 	}

	if (isNaN(keyChar) || keyChar == ' '){
   		event.keyCode=0;
	   	return false;
	}

  	return true;
}

/*
 * This function limits enter chars that aren´t númerics, "." or "-" or ","
 *
 * f: numeric field
 * SN_negative: True or false depending on the value can be negative or not
 * SN_decimal: True or false depending on the value can be decimal or not
 */

function filterCharsNumberEs(f, SN_negative, SN_decimal) {
	var point = '.';
	var coma = ',';
	var key = event.keyCode;
	if (key == null)
		return true;

	var keyChar = String.fromCharCode(key);

	if (keyChar == null)
		return true;

	if (SN_decimal) {
   		if (event.keyCode == point.charCodeAt(0)) {
   			if(f.value != ''){
   				if(f.value.indexOf(point) != -1){
   					event.keyCode=0;
   					return false;
   				}else if (f.value.indexOf(coma) != -1){
   					event.keyCode=0;
   					return false;
   				}
   			}   			
   			return true;
   			
   		} else if (event.keyCode == coma.charCodeAt(0)) {
   			if(f.value != ''){
   				if(f.value.indexOf(point) != -1){
   					event.keyCode=0;
   					return false;
   				}else if (f.value.indexOf(coma) != -1){
   					event.keyCode=0;
   					return false;
   				}
   			}
   			return true;
		}
   		
   	} else {
   		
   		if (keyChar == '.' || keyChar == ','){
                event.keyCode=0;
   				return false;
   		}
   	}

	if (SN_negative && keyChar == '-') {
		if (f.value == '') 
			return true;
		if (f.value.indexOf('-') != -1){
            event.keyCode=0;
			return false;
		}
		
		return false;
	}

	if (isNaN(keyChar) || keyChar == ' '){
   		event.keyCode=0;
	   	return false;
	}
  		
	return true;
}

/*
 * This function removes "-" chars when it's at an inappropiated place
 * Also adds a '0' when a '.' is inserted alone
 * it is called on onkeyup event for negative numbers
 *
 * f: numeric field
 */

function filterCharsUp(f){
  var i=f.value.indexOf('-');
  if (i>0){
  	var s=f.value.replace('-','');
  	f.value=s;
  }
  var j=f.value.indexOf('.');
  if(j==0){
  	f.value='0'+f.value;
  }
  if(j==1 && i==0){
  	f.value='-0'+f.value.substring(1);
  }
}

/*
 * This function removes "-" and "." chars when are alone in a field
 * Also removes '.' when there are no decimal numbers
 * And removes '-' before a '0' alone
 * it is called on onblur event for numbers
 *
 * f: numeric field
 */

function filterCharsNaN(f){

  var s=trim(f.value);
  s=s.replace(',','.');

  if (isANumber(s, f.name)) {
      if (s=='-0'){
      	f.value='0';
      }else if (s=='-' || s=='.'){
      	f.value='';
      }
      var i=f.value.indexOf('.');
      if(i==f.value.length-1){
      	f.value=f.value.substring(0,f.value.length-1);
      }else if(i!=-1){
	  	var kp=new String(f.onkeypress);
		var j=kp.indexOf("filterChars(");
		if(j!=-1){
		  kp=kp.substring(j+12)
		  kp=kp.substring(0,kp.indexOf(")"));
		  var v=kp.split(',');
		  if(v.length>2){
			if(trim(v[2])=='false'){
			  f.value='';
	    	  //alert ('-'+f.name+' '+val_msg(26)); 
       		  //f.focus();
       		  //f.select();
			}
		  }  
		}	
	  }
   } 
   else {
	   //f.value='';
	   alert (val_msg(16));
       f.focus();
       //f.select();
   }
}

/*****************************************Otras Validaciones****************************************************************/
/*
 * This function filters that inserted data is not a " nor a \
 */
function filterTextChars() {
	var charEsp = [34,92]; // Filter (34) -> '"' , (92) ->	'\'
	return filterListTextChars(charEsp);
} // filterTextChars

function filterTextCharsExt() {
	var charEsp = [34,35,92]; // Filter (34) -> '"' , (35) -> '#' , (92) ->	'\'
	return filterListTextChars(charEsp);
} // filterTextChars


function filterListTextChars(charEsp) {
 	var key = event.keyCode;
	if (key == null)
		return true;

	var keyChar = String.fromCharCode(key);
	if (keyChar == null)
		return true;

	var conta = charEsp.length - 1;
	while(conta>=0){
		if(key == charEsp[conta]){
			event.keyCode=0;
			return false;
		}
		conta--;
	}
	return true;
} // filterTextChars

/*
 * This function removes "-" and "." chars when are alone in a field
 * Also removes '.' when there are no decimal numbers
 * And removes '-' before a '0' alone
 * it is called on onblur event for numbers
 *
 * f: numeric field
 */

function filterTextCharsOnBlur(f){
  // Filter (34) -> '"' , (35) -> '#' , (92) ->	'\'
  var rest=trim(f.value);
  var result="";
  while(rest.length>0){
  	auxChar=rest.charAt(0);
	ok=(auxChar!='"')&&(auxChar!='\\');
	if(ok) result+=rest.substr(0,1);
	rest=rest.substr(1);
  }
  f.value=result;
}

function filterTextCharsExtOnBlur(f){
  // Filter (34) -> '"' , (35) -> '#' , (92) ->	'\'
  var rest=trim(f.value);
  var result="";
  while(rest.length>0){
  	auxChar=rest.charAt(0);
	ok=(auxChar!='"')&&(auxChar!='#')&&(auxChar!='\\');
	if(ok) result+=rest.substr(0,1);
	rest=rest.substr(1);
  }
  f.value=result;
}

/*****************************************Otras Validaciones****************************************************************/
/*
 * This function filters that inserted data is not a " nor a \
 */
function filterNumberChars() {
	var charEsp = [48,49,50,51,52,53,54,55,56,57]; // Filter (34) -> '"' , (92) ->	'\'

 	var key = event.keyCode;
	if (key == null)
		return true;

	var keyChar = String.fromCharCode(key);
	if (keyChar == null)
		return true;

	var conta = charEsp.length - 1;
	while(conta>=0){
		if(key == charEsp[conta]){
			return true;
		}
		conta--;
	}
	event.keyCode=0;
	return false;
} // filterTextChars


/**************************************************** Campos Nulos **********************************************************/
/*
 * It checks that in a the form "formName" , the fields "notNullsFields" come with no value
 * In case they come empty, "notNullsText" is the messages that will be shown
 */
var notNullsFields = new Array();
var notNullsText = new Array;

function validateNotNull(formName,notNullsFields,notNullsText){

//formName=name of the form
//notNullsFields= arrays with the name of the fields that can not be null
//notNullsText = arrays with the labels of the fields that can not be null
  var auxfields;
  for(var i=0;i<notNullsFields.length;i++) {
		auxfields =eval(formName+"."+notNullsFields[i]+".value");
		auxfields=trim(auxfields);
		if(auxfields=="") {
		alert(notNullsText[i] + ": "+val_msg(1));
			return false;
		}
  }
   return true;
}


/*
 * It checks if there is a option checked: return false
 * or if there is no option checked: return true
 */
function radioOptionsAreNull(radio){
 var allNull=true;
 if (radio==null) return true;
 for (var i=0;i<radio.length;i++)
  if (radio[i].checked) {
   allNull=false;
   break;
  }
 return allNull;
}

/********************************************* Detectar Cambios en el formulario ********************************************/

/*
 * Keep the values in arrays for detecting changes
 */
 function saveValuesOfFields(form){
	form = form.name;
 	var f_size=eval(form+".elements.length;");
 	for(i=0; i<f_size; i++){
	 	var element=eval("document."+form+".elements[i]");
    	if(element.type != 'hidden' && element.type != 'button'){
          if(element.type =="checkbox" || element.type =="radio"){
		 	//For checkBox o radiobuttons elements
            valuesOfFields[i]=element.checked;
          }else if(element.type=="select-multiple"){
			//For select multiple
		   	valuesOfFields[i]=getMultiValue(element); //en utils.js
  		  }else{ 
		   	//For text elements
			valuesOfFields[i]=element.value;
		  }
        }// fin if
	}// fin for
 }

 /*
  * It compares the values of the arrays.
  * if the arrays are equals, there are no changes
  * else there are changes in the form
  */
 var valuesOfFields = new Array();
 function checkElementsForChanges(form){
 	form= form.name;
    var f_size=eval(form+".elements.length");
    for(i=0; i<f_size; i++){
		var element=eval("document."+form+".elements[i]");
	 	if(element.type != 'hidden' && element.type != 'button'){
           if(element.type=="checkbox" || element.type=="radio"){
		 	 // For checkBox o radiobuttons elements
             if(valuesOfFields[i]!=element.checked){
			   return true;               //return true if there are changes in the form and false otherwise
			 }
           }else if(element.type=="select-multiple"){
		 	 // For checkBox o radiobuttons elements
             if(valuesOfFields[i]!=getMultiValue(element)){
			   return true;               //return true if there are changes in the form and false otherwise
			 }
           }else {	
		  	 //For text elements
             if(valuesOfFields[i]!=element.value) {
               return true;               //return true if there are changes in the form and false otherwise
			 }
           }
		}
    }
	return false;	 // No changes
 }

//Busca solo los que se llaman "valores...."
  function searchChangedValues(form){
	var valuesChanged='';
 	form= form.name;
    var f_size=eval("document."+form+".elements.length");
    for(i=0; i<f_size; i++){
	  var element=eval("document."+form+".elements[i]");
      if(element.type != 'hidden' && element.type != 'button' &&
	     element.name!=null && element.name.indexOf("valores")==0) {
		 
	  	var ref=element.name.indexOf("(");
		var elmName=element.name.substr(ref+1,element.name.length-2-ref);
		//alert(element.name+" <:P> "+elmName);
        if(element.type=="checkbox" || element.type=="radio"){
          /* No se evaluan
		   if(valuesOfFields[i]!=element.checked){
		    	valuesChanged+=";"+elmName;
		   }*/
        }else if(element.type=="select-multiple"){
           if(valuesOfFields[i]!=getMultiValue(element)) {
                valuesChanged+=";"+elmName+(valuesOfFields[i]==null || valuesOfFields[i].length==0?"N":"C");
		   }
        }else{//varios de texto
           if(valuesOfFields[i]!=element.value) {
                valuesChanged+=";"+elmName+(valuesOfFields[i]==null ||valuesOfFields[i].length==0?"N":"C");
		   }
        }
     }
   }
   return valuesChanged.substr(1);
 }

 function validarSiNumPositivo(valor){ 
    //Compruebo si es un valor numérico 
    if (isNaN(valor)) { 
			 //entonces (no es numero) devuelvo el valor cadena vacia 
       return "" 
    }else{ 
				if (valor< 0){
					return ""
				}
		   //En caso contrario (Si era un número) devuelvo el valor 
       return valor 
    } 
} 

function compruebaNumeroPositivo(obj,texto){ 
		var result = true;
		if(obj.value!=""){
	     enteroValidado = validarSiNumPositivo(obj.value) 
		   if (enteroValidado == ""){ 
	       //si era la cadena vacía es que no era válido. Lo aviso 
		       alert ("Debe escribir un numero positivo en el campo: " + texto) 
	        obj.value="";
					result= false;  			 
	     }
		}
		return result;
} 


function validarFecha(fecha){
	var objRegExp = /^\d{1,2}(\-|\/|\.)\d{1,2}\1\d{4}$/
	if (fecha==""){
		return true;
	}else {
		if (!objRegExp.test(fecha)){
			return false; // No cumple con el formato
		}else{
			return true; // Cumple con el formato
		}
	}
}

/*
 * Valida que el valor del elemento año es un valor numerico o vacío.
 * Además hace un trim() del valor.
 */
function validarObjetoAnio(objAnio){
	objAnio.value = trim(objAnio.value);
	return validarValorAnio(objAnio.value);
}

/*
 * Valida que el año es un valor numerico o vacío.
 */
function validarValorAnio(anio){
	if (!isAllDigits(anio) &&
		!anio == ""){
		return false;
	}
	return true;
}

/*
 * Valida que solo haya valores AlfaNuméricos y guiónes -,_
*/
function validarAlfaNumericoYGuiones(texto){
    var objRegExp=/^[\w,\-,_]+$/      
   if(texto == "" || objRegExp.test(texto))	   	   	
	   	return true;	   
   else	   	   
	   return false;	   
}

/*
 * Método que solo valida digitos y no deja que se escriba otros caracteres. 
*/
function soloDigitos(event)
{			
 	var key;
 	if(window.event) // para navegadores IE
 	{
  		key = event.keyCode;
 	}
 	else if(event.which) // para navegadores Firefox/Opera/Netscape
 	{
  		key = event.which;
 	}
 	if (key < 48 || key > 57) 
 	{
    	 return false;
 	}
 	return true;
}




