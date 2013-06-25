<!-- datosConsulta.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 04-04-2005 Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	String app_imagen = app+"/html/imagenes/";
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");	
		
	String y = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.y");
	String o = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.o");
	String no = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.no");
	String subir = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.subir");
	String bajar = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.bajar");
	String mas = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.mas");
	String menos = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.menos");
	String help = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.help");
	String tablaPri = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.tablaprioritaria");
	String abreP = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.abrirparentesis");
	String cierraP = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.cerrarparentesis");
	String errorTabla = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.TablaPrioritaria");
	String errorNumerico = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.CriteriosNumericos");
	String errorEnteros = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.enteros");
	String errorDecimales = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.decimales");
	String errorNoDecimales = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.noDecimales");
	String errorPunto = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.punto");
	String errorDescripcion = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.descripcion");
	
	Vector vcs = (Vector)request.getAttribute("camposSalida");
	Vector vco = (Vector)request.getAttribute("camposOrdenacion");
	Vector vca = (Vector)request.getAttribute("camposAgregacion");
	Vector vc = (Vector)request.getAttribute("criteriosConsulta");
	Vector vcd = (Vector)request.getAttribute("criteriosDinamicos");
	
	String idTablaPri = (String)request.getAttribute("idTablaPri");
	String descTablaPri = (String)request.getAttribute("descTablaPri");
	ArrayList moduloSel = new ArrayList();
	String idModulo = (String)request.getAttribute("idModulo");
	if (idModulo!=null && !idModulo.equals("")){
		moduloSel.add(idModulo);
	}
	
	String editable = (String)request.getParameter("editable");		
	String idInstitucion = (String)request.getParameter("idInstitucion");
	editable = idInstitucion.equals(user.getLocation())?editable:"0";	
	boolean bEditable = editable.equals("1")?true:false;
	String botones = bEditable?"V,G,GE":"V";
	String boxStyle = bEditable?"box":"boxConsulta";
	String accion = (String)request.getParameter("accion");
	
	String cgae = (String)request.getAttribute("esCGAE");
	boolean esCGAE = (cgae!=null && cgae.equals("true"))?true:false;	
	
	String tipoConsultaGeneral = ConConsultaAdm.TIPO_CONSULTA_GEN;
	String tipoConsultaEnvios = ConConsultaAdm.TIPO_CONSULTA_ENV;
	String tipoConsultaFact = ConConsultaAdm.TIPO_CONSULTA_FAC;
	
	String parametros = "";
	
	String noReset = !accion.equals("nuevo")?"noReset=true":"";
	parametros = noReset.equals("")?"":"?"+noReset;

	String tipoConsulta = (String)request.getAttribute("tipoConsulta");
	String sTipoConsultaCombo = tipoConsulta;
	tipoConsulta=!tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_GEN)?"tipoConsulta=listas":"";
	if (!tipoConsulta.equals("")){
		if (parametros.equals("")){
			parametros="?"+tipoConsulta;
		}else{
			parametros+="&"+tipoConsulta;
		}
	}
	
	String buscar = (String)request.getParameter("buscar");
	buscar = buscar!=null?"buscar=true":"";
	if (!buscar.equals("")){
		if (parametros.equals("")){
			parametros="?"+buscar;
		}else{
			parametros+="&"+buscar;
		}
	}
	
	//Calculo los values d elos simbolos esta vacio SQL:
	ConOperacionConsultaAdm opConsulta = new ConOperacionConsultaAdm(user);
	String valuesEstaVacio = opConsulta.getvaluesSimboloEstaIgual();

	String tipoConsultaParaElTitulo = (String)request.getAttribute("tipoConsulta");

%>	


<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<!-- Validaciones en Cliente -->
		<html:javascript formName="EditarConsultaForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		  //Funcion que dice si valor es uno de los simbolos EstaVacio:	
		  function esUnSimboloEstaVacio(valor) {
			  	var values = "<%=valuesEstaVacio%>";
			  	var listaValues = values.split("-");
			  	
			  	for (var i=0; i<listaValues.length; i++) {
			  		if (listaValues[i] == valor)
			  			return true;
			  	}
			  	
			  	return false;
		  }
	
		  function bajar(ref) {
		    v=ref.selectedIndex;
		    nElemDes=ref.length;
		    if (v<=nElemDes){
		      if (ref.selectedIndex+1<nElemDes){
		        auxText=ref(v).text;
		        auxValue=ref(v).value;
		        ref(v).text=ref(v+1).text;
		        ref(v).value=ref(v+1).value;
		        ref(v+1).text=auxText;
		        ref(v+1).value=auxValue;
		        ref.selectedIndex=ref.selectedIndex+1;
		      }
		    }
		  }
		  
		  function subir(ref) {
		    v=ref.selectedIndex;
		   if (v!=-1){
		     if (ref.selectedIndex-1>-1){
		       auxText=ref(v).text;
		       auxValue=ref(v).value;
		       ref(v).text=ref(v-1).text;
		       ref(v).value=ref(v-1).value;
		       ref(v-1).text=auxText;
		       ref(v-1).value=auxValue;
		       ref.selectedIndex=ref.selectedIndex-1;
		     }
		   }
		  }

		  function poner(refSelect,conCabecera) {
		  	//var select = document.frames("campoFrame").document.all.item("campoSel");	
		  
		  	var tema=document.forms[0].tema;
		  	var tipoCampo= document.getElementById("tipoCampoFrame").contentWindow.document.all.item("tipoCampoSel");
		    var campo= document.getElementById("campoFrame").contentWindow.document.all.item("campoSel");	
		    var cabecera=document.forms[0].cabecera.value;
		    
		    tipoCampoT = tipoCampo.options[tipoCampo.selectedIndex].text;
		    tipoCampoV = tipoCampo.options[tipoCampo.selectedIndex].value;
		    campoT = campo.options[campo.selectedIndex].text;
		    campoV = campo.options[campo.selectedIndex].value;
		    
		    if (tipoCampoV.length==0)
		      return;
		    if (cabecera.length==0)
		      return;
		      
		    nElementos=refSelect.options.length;
		    elementoRepetido=false;
		    refSelect.multiple=false;
		    aliasRepetido=false;
		    
		    if (conCabecera==1){
			    for (i=0; i<nElementos; i++) {
			      	if (refSelect.options[i].text.toUpperCase()==(tipoCampoT+'.'+campoT+'['+cabecera.toUpperCase()+']').toUpperCase()) {
			          elementoRepetido=true;
			          break;
			        }
			        
			        // Modificacion MAV 4/7/05
			        inicioAlias=refSelect.options[i].text.indexOf('[');
			        finAlias=refSelect.options[i].text.indexOf(']');
			        if ((inicioAlias!=-1)&&(finAlias!=-1)){
			        	alias=refSelect.options[i].text.substring(inicioAlias+1,finAlias);
			      		if (alias.toUpperCase()==cabecera.toUpperCase()) {
			      			aliasRepetido=true;
			          		break;
			        	}
			        }
			        // Fin modificacion
			        
			    }
			    if (elementoRepetido)
		      		return;
		      
		      	// Modificacion MAV 4/7/05
			    if (aliasRepetido){
			    	alert('<%=UtilidadesString.getMensajeIdioma(user,"consultas.nuevaConsulta.errorAlias")%>');
		      		return;
		      	}
		      	// Fin modificacion
		      
			    //new Option(text,value)
			    refSelect.add(new Option(tipoCampoT+'.'+campoT+'['+cabecera.toUpperCase()+']',campoV+'#'+cabecera.toUpperCase()));		       			    
			}else{
				for (i=0; i<nElementos; i++) {
			      	if (refSelect.options[i].text.toUpperCase()==(tipoCampoT+'.'+campoT).toUpperCase()) {
			          elementoRepetido=true;
			          break;
			        }
			    }
			    if (elementoRepetido)
			      	return;
			      
			    //new Option(text,value)
			    refSelect.add(new Option(tipoCampoT+'.'+campoT,campoV));		
			}

			//esto lo hacemos para que se vea el último insertado
		    refSelect.focus();
		    refSelect.selectedIndex=refSelect.options.length-1;
		    refSelect.multiple=true;		
		    
	    
		  }
		
		  function quitar(refSelect) {		    
		    
		    while (refSelect.selectedIndex!=-1)
			{				
				refSelect.remove(refSelect.selectedIndex);
			}
		  }
		  
		  function trim(s) {
			  while (s.substring(0,1) == ' ') {
			    s = s.substring(1,s.length);
			  }
			  while (s.substring(s.length-1,s.length) == ' ') {
			    s = s.substring(0,s.length-1);
			  }
			  return s;
		  }
		  
		  function checkFloat (obj, maxIntegerLength, maxFloatLength){
		  	var dotPosition;
			obj=trim(obj);
			 
			if (isANumber (obj)) {
			  punto = false;
			  if (obj.indexOf('.')!=-1) punto=true;
			  if (obj.indexOf('.')==0){
			  	alert('<%=errorPunto%>');
			  	return (false);
			  }
			  obj=String(parseFloat(obj));
			  dotPosition = obj.indexOf ('.');
			  if (dotPosition==-1){
			  	dotPosition=obj.length;
			  }
			  var ch=obj.charAt (0);
			  if (maxFloatLength!=0){
			  	if (ch=="+" || ch=="-") maxIntegerLength++;
			  }
			  if (!isNaN (maxIntegerLength) && obj.substring (0,dotPosition).length>maxIntegerLength) {
			  	alert('<%=errorEnteros%>');
			  	return (false);
			  }			  
			  
			  if (!isNaN (maxFloatLength) && obj.substring (dotPosition+1).length>maxFloatLength){
			  	if (maxFloatLength>0){
			  		alert('<%=errorDecimales%>');
			  	}else{
			  		alert('<%=errorNoDecimales%>');
			  	}
			  	return (false);
			  }
			  decimales = obj.substring (dotPosition+1).length;
			  if (decimales==0 && punto){
			  	alert('<%=errorPunto%>');
			  	return (false);
			  }
			}else{
			  alert('<%=errorNumerico%>');
			  return (false);
			}
			return (true);
		  }
		  
		  function isANumber (number){
			 var hasError=isNaN (number);
			 return (!hasError);
	 	  }
		  
		  
		  function ponerCriterio(refSelect) {
			  
		  	var operacion =  document.getElementById("frameOperacionValor").contentWindow.document.all.item("operacion");
		  	if (operacion.length==0){
		  		return;
		  	}
		  	operacionT = operacion.options[operacion.selectedIndex].text;
		  	operacionV = operacion.options[operacion.selectedIndex].value;	
		  	
		  	var valor =  document.getElementById("frameOperacionValor").contentWindow.document.all.item("valor");

		  	//Si valor es un combo:
		  	if (valor.tagName=="SELECT"){
			  	// Si hemos seleccionado el chequeo de esta vacio ponemos a vacio el valor:
		  		if (!esUnSimboloEstaVacio(operacion.value) && valor.length==0) {
			  		return;
			  	}

			  	// Si hemos seleccionado el chequeo de esta vacio ponemos a vacio el valor:
			  	if (esUnSimboloEstaVacio(operacion.value)) { 
					valor.options[valor.selectedIndex].text='';
					valor.options[valor.selectedIndex].value='0';
			  		valorT = '';
			  		valorV = '';
			   } else {
			  		valorT = valor.options[valor.selectedIndex].text;
			  		valorV = valor.options[valor.selectedIndex].value;	
		  		}
		  	//Si valor NO es un combo:
		  	} else {
		  		valortrim = trim(valor.value);
		  		//Si la operacion es esta vacio no compruebo que el valor sea vacio:
		  		if (!esUnSimboloEstaVacio(operacion.value) && valortrim=='') {
		  			return;
			  	}
			  	
			  	// Si hemos seleccionado el chequeo de esta vacio ponemos a vacio el valor:
			  	if (esUnSimboloEstaVacio(operacion.value)) { 
					  	valor.value='';
					  	valorT = '';
				  		valorV = '';
			   } else {
				  	valorT = valor.value;
			  		valorV = valor.value;
				}		  		
			  	var numerico =  document.getElementById("frameOperacionValor").contentWindow.document.all.item("numerico");
			  	if (numerico.value=="true"){	  				  	
			  		var enteros =  document.getElementById("frameOperacionValor").contentWindow.document.all.item("enteros");
			  		var decimales =  document.getElementById("frameOperacionValor").contentWindow.document.all.item("decimales");
		  			if(valorT!='' && !checkFloat(valorT,enteros.value,decimales.value)){
						return;
		  			}
			  	}
		  	}		  	
		    
		    var tipoCampo= document.getElementById("tipoCampoFrame").contentWindow.document.all.item("tipoCampoSel");
		    var campo= document.getElementById("campoFrame").contentWindow.document.all.item("campoSel");	
		    
		    tipoCampoT = tipoCampo.options[tipoCampo.selectedIndex].text;
		    tipoCampoV = tipoCampo.options[tipoCampo.selectedIndex].value;
		    campoT = campo.options[campo.selectedIndex].text;
		    campoV = campo.options[campo.selectedIndex].value;
		    
		    text = tipoCampoT+'.'+campoT+' '+operacionT+' '+valorT;
		    value = '#'+campoV+'#'+operacionV+'#'+valorV+'#';
		    refSelect.add(new Option(text,value));
		    
		    refSelect.focus();
		    refSelect.selectedIndex=refSelect.options.length-1;
		    		    		    
		  }
		
		  function addLogico(refSelect, oper) {
		    iSelDes=refSelect.selectedIndex;
		    if (iSelDes<0)
		      return;
		    if (refSelect.length==0) {
		      return;
		    }
		    var tmp=refSelect.options[iSelDes].text;
		    tmp=reemplazaSubStr(tmp, ' .O.', '');
		    tmp=reemplazaSubStr(tmp, ' .Y.', '');
		    tmp=reemplazaSubStr(tmp, ' .NO.', '');
		    if(document.forms[0].negar.checked)
		      refSelect.options[iSelDes].text=tmp;
		    else
		      refSelect.options[iSelDes].text=tmp+' .'+oper+'.';
		      
		    tmp=refSelect.options[iSelDes].value;
		    tmp=reemplazaSubStr(tmp, ' .O.', '');
		    tmp=reemplazaSubStr(tmp, ' .Y.', '');
		    tmp=reemplazaSubStr(tmp, ' .NO.', '');
		    if(document.forms[0].negar.checked)
		      refSelect.options[iSelDes].value=tmp;
		    else
		      refSelect.options[iSelDes].value=tmp+' .'+oper+'.';
		  }		  
		  
		  function eliminarParentesis(refSelect, parentesis, insPrin) {
		    iSelDes=refSelect.selectedIndex;
		    var tmpT=refSelect.options[iSelDes].text;
		    var tmpV=refSelect.options[iSelDes].value;
		    if (insPrin) {
		      if (tmpT.substring(0, 1)=='(') {
		        refSelect.options[iSelDes].value=tmpV.substring(1, tmpV.length);
		        refSelect.options[iSelDes].text=tmpT.substring(1, tmpT.length);
		      }
		    } else {
		      var pos=tmpT.indexOf(')', 0);
		      if (pos>-1) {
		        refSelect.options[iSelDes].text=tmpT.substring(0, pos)+tmpT.substring(pos+1, tmpT.length);
		        var pos=tmpV.indexOf(')', 0);
		        if (pos>-1)
		          refSelect.options[iSelDes].value=tmpV.substring(0, pos)+tmpV.substring(pos+1, tmpV.length);
		      }
		    }
		  }
		  function addParentesis(refSelect, parentesis, insPrin) {
		    iSelDes=refSelect.selectedIndex;
		    if (iSelDes<0)
		      return;
		    if(document.forms[0].negar.checked) {
		      eliminarParentesis(refSelect, parentesis, insPrin);
		      return;
		    }
		    var tmp=refSelect.options[iSelDes].text;
		    var pos=-1, lg='';
		    pos=tmp.indexOf(' .O.',0);
		    if (pos==-1) {
		      pos=tmp.indexOf(' .Y.',0);
		      if (pos==-1) {
		        pos=tmp.indexOf(' .NO.',0);
		        if (pos>-1) {
		          tmp=reemplazaSubStr(tmp, ' .NO.', '');
		          lg=' .NO.';
		        }
		      }
		      else {
		        tmp=reemplazaSubStr(tmp, ' .Y.', '');
		        lg=' .Y.';
		      }
		    }
		    else {
		      tmp=reemplazaSubStr(tmp, ' .O.', '');
		      lg=' .O.';
		    }
		    var tmpV=refSelect.options[iSelDes].value;
		    var posV=-1, lgV='';
		    posV=tmpV.indexOf(' .O.',0);
		    if (posV==-1) {
		      posV=tmpV.indexOf(' .Y.',0);
		      if (posV==-1) {
		        posV=tmpV.indexOf(' .NO.',0);
		        if (posV>-1) {
		          tmpV=reemplazaSubStr(tmpV, ' .NO.', '');
		          lgV=' .NO.';
		        }
		      }
		      else {
		        tmpV=reemplazaSubStr(tmpV, ' .Y.', '');
		        lgV=' .Y.';
		      }
		    }
		    else {
		      tmpV=reemplazaSubStr(tmpV, ' .O.', '');
		      lgV=' .O.';
		    }
		    if (insPrin) {
		      refSelect.options[iSelDes].text=parentesis+tmp+lg;
		      refSelect.options[iSelDes].value=parentesis+refSelect.options[iSelDes].value;
		    } else {
		      refSelect.options[iSelDes].text=tmp+parentesis+lg;
		      refSelect.options[iSelDes].value=tmpV+parentesis+lgV;
		    }		
		  }		  
		  
		 function reemplazaSubStr(cadena, find, reemplace) {
			  cadenaProcesada="";
			  pos = -1, lengthFind = 0, lengthReemplace = 0;
			
			  lengthFind      = find.length; // longitud de la cadena a reemplazar
			  lengthReemplace = reemplace.length; // longitud de la cadena por la que vamos a reemplazar
			  pos             = cadena.indexOf(find,0); // posicion de la cadena a reemplazar
			
			  while(pos > -1) {
			  // desde el inicio hasta la posicion de la primera ocurrencia
			  // cadena a reemplazar
			  // posicion de cadena sumandole la posicion de la primera ocurrencia mas la
			  // longitud de la cadena que va a ser reemplazada hasta el final
			
			    cadenaProcesada = cadena.substring(0, pos) + reemplace + cadena.substring(pos+lengthFind, cadena.length);
			    cadena = cadenaProcesada;
			  // posicion posible de la siguiente ocurrencia
			    pos = cadena.indexOf(find, pos+lengthReemplace);
			  }
			  return(cadena);
		}
	
		function refrescarLocal()
		{			
			document.location.reload();			
		}


		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			if(parent.document.getElementById("accionAnterior")&&parent.document.getElementById("accionAnterior").value!=""){

				document.forms[1].accionAnterior.value=parent.document.getElementById("accionAnterior").value;
				document.forms[1].idModulo.value=parent.document.getElementById("idModulo").value;
				document.forms[1].modo.value="inicio";
			}else{
				document.forms[1].modo.value="abrir";
			}
			document.forms[1].action=document.forms[1].action+"<%=parametros%>";
			document.forms[1].submit();				
		}
		
		function extractCampos(ref,conCabecera){
			for (i=0; i<ref.length; i++) {
				var opcion = ref[i].value;		
				var opcion_array=opcion.split("#");
				
				elemento = document.getElementById(ref.id+'['+i+']tc');
				if (elemento!=null){
					elemento.value=opcion_array[0];
				}else{
					var input1 = document.createElement('INPUT');
					input1.type = "hidden";
	        		input1.name = ref.id+"["+i+"].tc";
	        		input1.value = opcion_array[0];
	        		input1.id = ref.id+"["+i+']tc';
					document.forms[0].appendChild(input1);
				}
				
				elemento = document.getElementById(ref.id+'['+i+']idC');
				if (elemento!=null){
					elemento.value=opcion_array[1];
				}else{
					var input2 = document.createElement('INPUT');
					input2.type = "hidden";
					input2.name = ref.id+"["+i+"].idC";
					input2.value = opcion_array[1];
					input2.id = ref.id+"["+i+']idC';
					document.forms[0].appendChild(input2);
				}
				
				if(conCabecera==1){
					elemento = document.getElementById(ref.id+'['+i+']cab');
					if (elemento!=null){
						elemento.value=opcion_array[2];
					}else{
						var input3 = document.createElement('INPUT');
						input3.type = "hidden";
						input3.name = ref.id+"["+i+"].cab";
						input3.value = opcion_array[2];
						input3.id = ref.id+"["+i+']cab';
						document.forms[0].appendChild(input3);
					}
				}
			}
			for (j=i;j<<%=SIGAConstants.TAMANYO_ARRAY_CONSULTA%>;j++){
        		elemento = document.getElementById(ref.id+'['+j+']tc');
        		if(elemento!=null){
        			elemento.value=null;
        		}else{
        			break;
        		}
        	}
		}
		
		function extractCriterios(ref){		
			for (i=0; i<ref.length; i++) {
				nombre = ref.id+"["+i+"]";
				var criterio = ref[i].value;
				elemento = document.getElementById(ref.id+'['+i+']');
				if (elemento!=null){
					elemento.value=criterio;
				}else{
					var input = document.createElement('INPUT');
					input.type = "hidden";
	        		input.name = ref.id+"["+i+"]";
	        		input.value = criterio;
	        		input.id = ref.id+"["+i+']';
	        		document.forms[0].appendChild(input)
	        	}        		
        	}
        	for (j=i;j<<%=SIGAConstants.TAMANYO_ARRAY_CONSULTA%>;j++){
        		elemento = document.getElementById('criterios['+j+']');
        		if(elemento!=null){
        			elemento.value=null;
        		}else{
        			break;
        		}
        	}
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			sub();
			tipoConsultaEnvios="<%=tipoConsultaEnvios%>";
			tipoConsultaFact="<%=tipoConsultaFact%>";
			if (document.forms[0].tipoConsulta.value==tipoConsultaEnvios || document.forms[0].tipoConsulta.value==tipoConsultaFact){
				guardarListas();
			}else{
				extractCampos(document.forms[0].camposSalida,1);
				if (validateEditarConsultaForm(document.EditarConsultaForm)){
					extractCampos(document.forms[0].camposOrden,0);
					extractCampos(document.forms[0].camposAgregacion,0);
					extractCampos(document.forms[0].criteriosDinamicos,0);
					extractCriterios(document.forms[0].criterios);
					<%if (accion.equals("nuevo")){%>
						document.forms[0].modo.value="insertarSolo";
					<%}else{%>
						document.forms[0].modo.value="modificarSolo";					
					<%}%>
					document.forms[0].submit();
				}else{
					fin();
					return false;
				}
			}	
		}
		
		function guardarListas() 
		{	
			if (document.forms[0].descripcion==null || document.forms[0].descripcion.value==""){
				alert('<%=errorDescripcion%>');
				fin();
				return false;
			}else{
				extractCriterios(document.forms[0].criterios);
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertarSolo";
				<%}else{%>
					document.forms[0].modo.value="modificarSolo";					
				<%}%>
				document.forms[0].submit();
			}
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardarEjecutar() 
		{	
			sub();
			tipoConsultaEnvios="<%=tipoConsultaEnvios%>";
			tipoConsultaFact="<%=tipoConsultaFact%>";
			if (document.forms[0].tipoConsulta.value==tipoConsultaEnvios || document.forms[0].tipoConsulta.value==tipoConsultaFact){
				guardarEjecutarListas();
			}else{
				extractCampos(document.forms[0].camposSalida,1);
				if (validateEditarConsultaForm(document.EditarConsultaForm)){
				
					if (comprobarIgualdadDeListas (document.forms[0].camposSalida, document.forms[0].camposAgregacion) == false) {
				    	alert('<%=UtilidadesString.getMensajeIdioma(user,"consultas.camposAgregacion.error")%>');
						fin();

						return false;
					}
				
					extractCampos(document.forms[0].camposOrden,0);
					extractCampos(document.forms[0].camposAgregacion,0);
					extractCampos(document.forms[0].criteriosDinamicos,0);
					extractCriterios(document.forms[0].criterios);
					<%if (accion.equals("nuevo")){%>
						document.forms[0].modo.value="insertarEjecutar";
					<%}else{%>
						document.forms[0].modo.value="modificarEjecutar";					
					<%}%>
					document.forms[0].submit();
				}else{
					fin();
					return false;
				}
			}	
		}
		
		function comprobarIgualdadDeListas (lista1, lista2) 
		{		
			if (lista2.length == 0) 			return true;
			if (lista1.length != lista2.length) return false;
			
			v1 = lista1.options;
			v2 = lista2.options;
						
			for (i = 0; i < v1.length; i++) {
			
				o1  = v1[i].value;
				id1 = (o1.split("#"))[1];

				j = 0;
				for (j = 0; j < v2.length; j++) {
					o2 = v2[j].value;
					id2 = (o2.split("#"))[1];
					
					if (id1 == id2)
						break;
				}
				if (j >= v2.length) return false;
			}
			return true;
		}
		
		function guardarEjecutarListas() 
		{	
		
			sub();
			if (document.forms[0].descripcion==null || document.forms[0].descripcion.value==""){
				alert('<%=errorDescripcion%>');
				fin();
				return false;
			}else{
				extractCriterios(document.forms[0].criterios);
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertarEjecutar";
				<%}else{%>
					document.forms[0].modo.value="modificarEjecutar";					
				<%}%>
				document.forms[0].submit();
			}
		}
					
		function setCabecera() 
		{		
			var select =  document.getElementById("campoFrame").contentWindow.document.all.item("campoSel");	
			document.forms[0].campo.value=select.options[select.selectedIndex].value;
			document.forms[0].cabecera.value=select.options[select.selectedIndex].text;
			if(document.forms[0].campo.value.length!=0){
				setOperacionValor();				
			}
		}
		
		function setOperacionValor() 
		{		
			var select =  document.getElementById("campoFrame").contentWindow.document.all.item("campoSel");	
			document.forms[0].campo.value=select.options[select.selectedIndex].value;
			if(document.forms[0].campo.value.length!=0){
				document.forms[0].target="frameOperacionValor";
				document.forms[0].modo.value="operacionValor";
				document.forms[0].submit();				
				document.forms[0].target="submitArea";	
			}
		}				
		
		function selectTablaPri(ref){
		
			tablas="";
			for (i=0; i<ref.length; i++) {
				var opcionV = ref.options[i].value;	
				var opcion_arrayV=opcionV.split("#");
				var opcionT = ref.options[i].text;	
				var opcion_arrayT=opcionT.split(".");
				tablaV = opcion_arrayV[0];
				tablaT = opcion_arrayT[0];
				tablas=tablas+tablaV+","+tablaT+"#";
			}
			if (tablas==""){
				error = "<%=errorTabla%>";
				alert(error);
				return;
			}
			document.forms[0].modo.value="tablaPri";
			document.forms[0].tablas.value=tablas;
			
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			
			
			if (resultado!=undefined){
				select = document.forms[0].tablaPrioritaria;
				if (select.length>0){
					select.remove(1);
				}else{
					select.add(new Option("",""));
				}
				select.add(new Option(resultado[0],resultado[1]));
				select.selectedIndex=1;
			}
		}
		
		function editarCriterio(ref){
			if (ref.selectedIndex>-1){
				criterio = ref.options[ref.selectedIndex];
				document.forms[0].criterioModif.value=criterio.value;
				document.forms[0].modo.value="cambiarCriterio";
				var resultado=ventaModalGeneral(document.forms[0].name,"M");
				if (resultado!=undefined){
					ref.options[ref.selectedIndex].text=resultado[0];
					ref.options[ref.selectedIndex].value=resultado[1];
				}
			}
			
		}
				
	</script>
	<!-- FIN: SCRIPTS BOTONES -->	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	
<% 	if (accion!=null && accion.equalsIgnoreCase("NUEVO")) { %>

		<% if (tipoConsultaParaElTitulo != null && tipoConsultaParaElTitulo.equalsIgnoreCase("listas")) { %>
				<siga:TituloExt titulo="consultas.consultasRecuperar.consulta.cabecera"  localizacion="consultas.nuevaConsultaListas.localizacion"/>
		<% } else { %>
				<siga:TituloExt titulo="consultas.consultasRecuperar.consulta.cabecera"  localizacion="consultas.nuevaConsulta.localizacion"/>
		<% } %>	
	
	
	<!-- FIN: TITULO Y LOCALIZACION -->	
<% } else { %>
	<siga:TituloExt 
		titulo="consultas.consultasRecuperar.consulta.cabecera" 
		localizacion="consultas.consultasRecuperar.editar.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
<% } %>
	</head>

	<body>
	
		<html:form action="/CON_EditarConsulta.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>	
			<html:hidden property = "idConsulta" value=""/>	
			<html:hidden property = "tablas"/>
			<html:hidden property = "actionModal" value=""/>
			<html:hidden property = "criterioModif" value=""/>
			<html:hidden property = "esExperta" value="0"/>
			<input type="Hidden"  name="experta" value="0">

<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">			
			<html:hidden property = "tipoConsulta"/>
</logic:equal>
<logic:notEqual name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
<%if (!accion.equals("nuevo")){%>		
			<html:hidden property = "tipoConsulta"/>
<%}%>			
</logic:notEqual>			
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos" width=120px>
					<siga:Idioma key="consultas.recuperarconsulta.literal.descripcion"/>&nbsp(*) &nbsp;&nbsp;
					<html:text name="EditarConsultaForm" property="descripcion" size="80" maxlength="100" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>"></html:text>
				</td>
			</tr>
		</table>
	
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<table  class="tablaCentralCampos"  align="center">			 		
		
		<tr>				
		<td>	
		
		<table class="tablaCampos" align="center">
<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">		
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.modulo"/>&nbsp(*)
			</td>
			<td>
			<%if (bEditable){%>				
				<siga:ComboBD nombre = "modulo" tipo="cmbModuloConsulta" clase="boxCombo" obligatorio="false" pestana="t" ElementoSel="<%=moduloSel%>"/>
			<%}else{%>
				<html:text name="EditarConsultaForm" property="moduloSel"  styleClass="boxConsulta" readonly="true"></html:text>
			<%}%>
			</td>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.general"/>
			</td>
			<td>				
				<html:checkbox name="EditarConsultaForm" property="general" disabled="<%=(!bEditable||!esCGAE)%>"></html:checkbox>
			</td>													
		</tr>
</logic:equal>

<%if (bEditable){%>		
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tema"/>
			</td>
			<td>				
				<siga:ComboBD nombre = "tema" tipo="cmbBaseConsulta" clase="boxCombo"  obligatorio="false" accion="Hijo:tipoCampo" pestana="t"/>
			</td>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tipocampo"/>
			</td>
			<td>				
				<siga:ComboBD nombre = "tipoCampo" tipo="cmbTipoCampoConsulta"  ancho="300"  clase="boxCombo"  obligatorio="false" accion="Hijo:campo" hijo="t" pestana="t"/>
			</td>												
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.campo"/>
			</td>
			<td>	
<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">						
				<siga:ComboBD nombre = "campo" tipo="cmbCampoConsulta" ancho="220" clase="boxCombo" obligatorio="false" hijo="t" pestana="t" accion="parent.setCabecera()"/>
</logic:equal>
<logic:notEqual name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">						
				<siga:ComboBD nombre = "campo" tipo="cmbCampoConsulta" ancho="220" clase="boxCombo" obligatorio="false" hijo="t" pestana="t" accion="parent.setOperacionValor()"/>
</logic:notEqual>				
			</td>
<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">			
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.cabecera"/>
			</td>
			<td>				
				<html:text name="EditarConsultaForm" property="cabecera" size="30" maxlength="32" styleClass="box"></html:text>
			</td>
</logic:equal>	
<logic:notEqual name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
<%if (accion.equals("nuevo")){%>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tipoConsulta"/>
			</td>
			<td>				
				<select name="tipoConsulta" class="boxCombo" id="tipoConsulta" >
					<option value="<%=tipoConsultaEnvios%>"><siga:Idioma key="modulo.envios"/></option>
					<option value="<%=tipoConsultaFact%>"><siga:Idioma key="modulo.facturacion"/></option>
				</select>
			</td>
<%} else {%>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tipoConsulta"/>
			</td>
			<td>		
<%			if (sTipoConsultaCombo.equals(tipoConsultaEnvios)) {%>
				<input type="text" value="<siga:Idioma key="modulo.envios"/>" class="boxConsulta">
<%			} else {%>
				<input type="text" value="<siga:Idioma key="modulo.facturacion"/>" class="boxConsulta">
<%			}%>
			</td>
<%}%>
</logic:notEqual>
		</tr>
<%} else {%>
		</table><table align="center" width="30%"><tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tipoConsulta"/>
			</td>
			<td>		
<%			if (sTipoConsultaCombo.equals(tipoConsultaEnvios)) {%>
				<input type="text" value="<siga:Idioma key="modulo.envios"/>" class="boxConsulta">
<%			} else {%>
				<input type="text" value="<siga:Idioma key="modulo.facturacion"/>" class="boxConsulta">
<%			}%>
			</td>
		</tr>
<%}%>
		</table>

<%
String altura = "430px";
if (!bEditable){
	altura="460px";
}
%>
<div id="capaPrincipal" style="position:relative; width:100%; height:<%=altura%>; overflow-y:auto;	margin:5px; border: 2px solid #<%=src.get("color.button.border")%>;">

<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.campossalida">
		<table class="tablaCampos" align="center">
	
		<tr>
			<td colspan="3" align="center" width="100%">
				<table align="center">
				<tr>			
					<td valign="middle" align="right">
						<%if (bEditable){%><a HREF="javascript:selectTablaPri(document.forms[0].camposSalida);"><%}%>
						<IMG border=0 src="<%=app_imagen%>iconot.gif" alt="<%=tablaPri%>">
					</td>
					<td>
<%if (bEditable){%>
						<select name="tablaPrioritaria" class="boxCombo" id="tablaPrioritaria" >
<%if (idTablaPri!=null && !idTablaPri.equals("")){%>
							<option value=""></option>
							<option value="<%=idTablaPri%>" selected><%=descTablaPri%></option>
<%}%>					
						</select>	
<%}else{
	if (descTablaPri!=null && !descTablaPri.equals("")){%>							
						<input type="text" class="boxConsulta" readonly="true" value="<%=descTablaPri%>"></input>
<%}else{%>
						<input type="text" class="box" readonly="true" size="20" value=""></input>
<%}
}%>						
					</td>
				</tr>		
				</table>
			</td>
		</tr>
		<tr>	
			<td valign="middle" align="right" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:poner(document.forms[0].camposSalida,1);"><IMG border=0 src="<%=app_imagen%>icono+.gif" alt="<%=mas%>"></a><br><br>
				<a HREF="javascript:quitar(document.forms[0].camposSalida);"><IMG border=0 src="<%=app_imagen%>icono-.gif" alt="<%=menos%>"></a>
			<%}%>
			</td>
			<td align="center" width="50%">
				<select size="4" class="boxCombo" id="camposSalida" name="campoSalida" multiple>
<%
			if(vcs!=null && !vcs.isEmpty()){
				for (int i=0; i<vcs.size(); i++)
				{
					Row fila = (Row)vcs.elementAt(i);
					String text = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+".";
					text += fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA)+"[";
					text += fila.getString(ConCamposSalidaBean.C_CABECERA)+"]";
					
					//String value = fila.getString(ConTipoCampoConsultaBean.C_IDTIPOCAMPO)+"#";
					String value = fila.getString(ConCampoConsultaBean.C_IDTABLA)+"#";
					value += fila.getString(ConCamposSalidaBean.C_IDCAMPO)+"#";
					value += fila.getString(ConCamposSalidaBean.C_CABECERA);
%>
					<option value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
				</select>								
			</td>
			<script>document.getElementById("camposSalida").style.width="400";</script>
			<td valign="middle" align="left" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:subir(document.forms[0].camposSalida);"><IMG border=0 src="<%=app_imagen%>iconoarriba.gif" alt="<%=subir%>"></a><br><br>
				<a HREF="javascript:bajar(document.forms[0].camposSalida);"><IMG border=0 src="<%=app_imagen%>iconoabajo.gif" alt="<%=bajar%>"></a>
			<%}%>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
	
		</table>		
		</siga:ConjCampos>	
</logic:equal>
		
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.criterios">
		<table class="tablaCampos" align="center">
	
		<tr>
		<%if (bEditable){%>
			<td colspan="3" align="center" width="100%">
				<iframe src="<%=app%>/html/jsp/consultas/operacionValor.jsp"
							id="frameOperacionValor"
							name="frameOperacionValor" 
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
							style="width:100%; height:30; z-index:2;left: 0px">
				</iframe>
			
			</td>
		</tr>
		<tr>
		<%}%>
			<td valign="middle" align="right" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:ponerCriterio(document.forms[0].criterios);"><IMG border=0 src="<%=app_imagen%>icono+.gif"  alt="<%=mas%>"></a><br><br>
				<a HREF="javascript:quitar(document.forms[0].criterios);"><IMG border=0 src="<%=app_imagen%>icono-.gif" alt="<%=menos%>"></a>
			<%}%>
			</td>
			<td align="center" width="50%">
			<%if (bEditable){%>
				<select size="4" class="boxCombo" id="criterios" ondblclick="editarCriterio(document.forms[0].criterios);">
			<%}else{%>
				<select size="4" class="boxCombo" id="criterios">
			<%}%>
<%
			if(vc!=null && !vc.isEmpty()){
				for (int i=0; i<vc.size(); i++)
				{
					Row fila = (Row)vc.elementAt(i);
										
					String text = fila.getString(ConCriterioConsultaBean.C_SEPARADORINICIO);
					text += fila.getString("DESCRIPCIONTIPOCAMPO")+".";
					text += fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);
					text += " "+fila.getString("DESCRIPCIONOPERACION");
					text += " "+fila.getString("VALOR");
					text += fila.getString(ConCriterioConsultaBean.C_SEPARADORFIN);
					
					String operador = fila.getString(ConCriterioConsultaBean.C_OPERADOR);
					String operadorTraducido="";
					if (operador.equals(ConConsultaAdm.CONS_Y)){
						operadorTraducido = " .Y.";
					}else if(operador.equals(ConConsultaAdm.CONS_O)){
						operadorTraducido = " .O.";
					}else if(operador.equals(ConConsultaAdm.CONS_NO)){
						operadorTraducido = " .NO.";
					}
					text += operadorTraducido;
					
					String value = fila.getString(ConCriterioConsultaBean.C_SEPARADORINICIO)+"#";
					value += fila.getString(ConTipoCampoConsultaBean.C_IDTIPOCAMPO)+"#";
					value += fila.getString(ConCriterioConsultaBean.C_IDCAMPO)+"#";
					value += fila.getString(ConCriterioConsultaBean.C_IDOPERACION)+"#";
					value += fila.getString(ConCriterioConsultaBean.C_VALOR)+"#";
					value += fila.getString(ConCriterioConsultaBean.C_SEPARADORFIN);
					value += operadorTraducido;
					
					//String id="criterios["+i+"]";
					
%>
					<option value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
				</select>				
			</td>
			<script>document.getElementById("criterios").style.width="400";</script>
			<td valign="middle" align="left"  width="50%">
			<%if (bEditable){%>
				<a HREF="javascript:addParentesis(document.forms[0].criterios, '(', true);"><IMG border=0 src="<%=app_imagen%>icono(.gif" alt="<%=abreP%>"></a>&nbsp;				
				<a HREF="javascript:addLogico(document.forms[0].criterios, 'Y');"><IMG border=0 src="<%=app_imagen%>iconoy.gif"  alt="<%=y%>"></a>&nbsp;
				<a HREF="javascript:addLogico(document.forms[0].criterios, 'O');"><IMG border=0 src="<%=app_imagen%>iconoo.gif" alt="<%=o%>"></a>&nbsp;
				<a HREF="javascript:addLogico(document.forms[0].criterios, 'NO');"><IMG border=0 src="<%=app_imagen%>iconono.gif" alt="<%=no%>"></a>&nbsp;
				<a HREF="javascript:addParentesis(document.forms[0].criterios, ')', false);"><IMG border=0 src="<%=app_imagen%>icono).gif" alt="<%=cierraP%>"></a>
				<input type="checkbox" name="negar"/>
				<IMG border=0 src="<%=app_imagen%>help.gif"  alt="<%=help%>">
			<%}%>
			</td>
		</tr>
		<%if (bEditable){%><tr><td>&nbsp;</td></tr><%}%>
	
		</table>		
		</siga:ConjCampos>	

<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">		
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.criteriosdinamicos">
		<table class="tablaCampos" align="center">
	
		<tr>
			<td valign="middle" align="right" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:poner(document.forms[0].criteriosDinamicos,0);"><IMG border=0 src="<%=app_imagen%>icono+.gif" alt="<%=mas%>"></a><br><br>
				<a HREF="javascript:quitar(document.forms[0].criteriosDinamicos);"><IMG border=0 src="<%=app_imagen%>icono-.gif" alt="<%=menos%>"></a>
			<%}%>	
			</td>
			<td align="center" width="50%">
				<select size="4" class="boxCombo" id="criteriosDinamicos" multiple>
<%
			if(vcd!=null && !vcd.isEmpty()){
				for (int i=0; i<vcd.size(); i++)
				{
					Row fila = (Row)vcd.elementAt(i);
					String text = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+".";
					text += fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);
					
					String value = fila.getString(ConTipoCampoConsultaBean.C_IDTIPOCAMPO)+"#";
					value += fila.getString(ConCriteriosDinamicosBean.C_IDCAMPO);
					
%>
					<option value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
				</select>					
			</td>
			<script>document.getElementById("criteriosDinamicos").style.width="400";</script>
			<td valign="middle" align="left"></td>
		</tr>
	
		</table>		
		</siga:ConjCampos>	
		
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.camposagregacion">		
		<table class="tablaCampos" align="center">
	
		<tr>
			<td valign="middle" align="right" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:poner(document.forms[0].camposAgregacion,0);"><IMG border=0 src="<%=app_imagen%>icono+.gif" alt="<%=mas%>"></a><br><br>
				<a HREF="javascript:quitar(document.forms[0].camposAgregacion);"><IMG border=0 src="<%=app_imagen%>icono-.gif" alt="<%=menos%>"></a>
			<%}%>
			<td align="center" width="50%">
				<select size="4" class="boxCombo" id="camposAgregacion" multiple>
<%
			if(vca!=null && !vca.isEmpty()){
				for (int i=0; i<vca.size(); i++)
				{
					Row fila = (Row)vca.elementAt(i);
					String text = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+".";
					text += fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);
					
					String value = fila.getString(ConTipoCampoConsultaBean.C_IDTIPOCAMPO)+"#";
					value += fila.getString(ConCamposAgregacionBean.C_IDCAMPO);
					
%>
					<option value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
			</select>						
			</td>
			<script>document.getElementById("camposAgregacion").style.width="400";</script>
			<td valign="middle" align="left" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:subir(document.forms[0].camposAgregacion);"><IMG border=0 src="<%=app_imagen%>iconoarriba.gif" alt="<%=subir%>"></a><br><br>
				<a HREF="javascript:bajar(document.forms[0].camposAgregacion);"><IMG border=0 src="<%=app_imagen%>iconoabajo.gif" alt="<%=bajar%>"></a>
			<%}%>
			</td>
		</tr>
	
		</table>
		</siga:ConjCampos>					
		
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.camposordenacion">
		<table class="tablaCampos" align="center">
	
		<tr>
			<td valign="middle" align="right" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:poner(document.forms[0].camposOrden,0);"><IMG border=0 src="<%=app_imagen%>icono+.gif" alt="<%=mas%>"></a><br><br>
				<a HREF="javascript:quitar(document.forms[0].camposOrden);"><IMG border=0 src="<%=app_imagen%>icono-.gif" alt="<%=menos%>"></a>
			<%}%>
			</td>
			<td align="center" width="50%">
				<select size="4" class="boxCombo" id="camposOrden" multiple>
<%
			if(vco!=null && !vco.isEmpty()){
				for (int i=0; i<vco.size(); i++)
				{
					Row fila = (Row)vco.elementAt(i);
					String text = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+".";
					text += fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);
					
					String value = fila.getString(ConTipoCampoConsultaBean.C_IDTIPOCAMPO)+"#";
					value += fila.getString(ConCamposOrdenacionBean.C_IDCAMPO);
					
%>
					<option value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
				</select>				
			</td>
			<script>document.getElementById("camposOrden").style.width="400";</script>
			<td valign="middle" align="left" width="25%">
			<%if (bEditable){%>
				<a HREF="javascript:subir(document.forms[0].camposOrden);"><IMG border=0 src="<%=app_imagen%>iconoarriba.gif" alt="<%=subir%>"></a><br><br>
				<a HREF="javascript:bajar(document.forms[0].camposOrden);"><IMG border=0 src="<%=app_imagen%>iconoabajo.gif" alt="<%=bajar%>"></a>
			<%}%>
			</td>
		</tr>
	
		</table>		
		</siga:ConjCampos>	
</logic:equal>					
</div>		
		
		</td>
		</tr>
		</table>
			
		</html:form>
		<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<%if (tipoConsulta != null && tipoConsulta.equals("tipoConsulta=listas")){%>				
		<html:form action="/CON_RecuperarConsultasDinamicas.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior"/>
			<html:hidden property = "idModulo"/>
		</html:form>
		
	<%}else{%>
		<html:form action="/CON_RecuperarConsultas.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior"/>
			<html:hidden property = "idModulo"/>
		</html:form>
	<%}%>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>	
</html>