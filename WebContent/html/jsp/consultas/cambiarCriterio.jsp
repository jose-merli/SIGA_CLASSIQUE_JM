<!-- cambiarCriterio.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");	
	
	String errorNumerico = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.CriteriosNumericos");
	String errorEnteros = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.enteros");
	String errorDecimales = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.decimales");
	String errorNoDecimales = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.noDecimales");
	String errorPunto = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.punto");
	String errorOperacion = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.operacion");
	String errorValor = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.valor");
		

	String tipocampo = (String)request.getAttribute("tipocampo");
	String dato[] = {tipocampo};
	int max = 10;
	Integer longitud = (Integer)request.getAttribute("longitud");
	if (longitud!=null){
		// +1 para dejar que introduzcan signo
		max = longitud.intValue()+1;		
	}
	Integer escala = (Integer)request.getAttribute("escala");
	if (escala!=null){
		String sEscala = String.valueOf(escala);
		if (!sEscala.equals("0")){
			// +2 para dejar que introduzcan signo y punto decimal
			max = longitud.intValue()+escala.intValue()+2;
		}
	}
	
	boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE))?true:false;
	boolean numerico = (tipocampo!=null && (tipocampo.equals(SIGAConstants.TYPE_NUMERIC)|| tipocampo.equals(SIGAConstants.TYPE_LONG)))?true:false;
	boolean selected = false;
	
	if (tipocampo!=null && !tipocampo.equals("")){
		if (tipocampo.equals(SIGAConstants.TYPE_NUMERIC) || tipocampo.equals(SIGAConstants.TYPE_LONG)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valornumerico");
		}else if (tipocampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
		}else if (tipocampo.equals(SIGAConstants.TYPE_DATE)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valorfecha");
		}
	}else{
		tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
	}	
	Vector datosValor = (Vector)request.getAttribute("datosValor");
	Vector datosOperacion = (Vector)request.getAttribute("datosOperacion");
	String operacion = (String)request.getAttribute("operacion");
	ArrayList operacionSel = new ArrayList();
	operacionSel.add(operacion);	
	String valor = (String)request.getAttribute("valor");
	String separadorIni = (String)request.getAttribute("separadorIni");
	String separadorFin = (String)request.getAttribute("separadorFin");
	String nombreCampo = (String)request.getAttribute("nombreCampo");
	String idCampo = (String)request.getAttribute("idCampo");	
	String idTabla = (String)request.getAttribute("idTabla");	
%>


<html>
	<head>
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	    
	    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	    
	   	<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		
	</head>

	<body>

	
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	<div id="camposRegistro" class="posicionModalMedia" align="center">
	
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
	
		<table  class="tablaCentralCamposMedia"  align="center">
			<tr>				
				<td>

				<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.criterios">
				
	<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.campo"/>
			</td>
			<td  class="labelTextValue">
				<%=nombreCampo%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.operacion"/>
			</td>
			<td>			
				<siga:ComboBD nombre = "operacion" tipo="cmbOperacion" clase="boxCombo" obligatorio="true" ElementoSel="<%=operacionSel%>" parametro="<%=dato%>"/>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<%=tipocampo%>
			</td>		
			<td>
			<% if (datosValor!=null){%>
				<select name = "valor" id="valor"  class = "boxCombo" >
					<%
						for (int i=0; i<datosValor.size(); i++){
							Row fila = (Row)datosValor.elementAt(i);
							String id2 = fila.getString("ID");
							String desc2 = fila.getString("DESCRIPCION");	
							selected = id2.equals(valor)?true:false;
								
						%>
						<option value="<%=id2%>" <%if (selected){%>selected<%}%>><%=desc2%></option>
					<%	}%>					
				</select>	
			<%}else{%>		
			<%if (fecha){%>
				<input type="text" name="valor" class="box" readonly="true" value="<%=valor%>"></input>				
				<a href='javascript://'onClick="return showCalendarGeneral(valor);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<input type="hidden" name="numerico" value="false"></input>
			<%}else if (numerico){%>
				<input type="text" name="valor" class="box" maxlength="<%=max%>" value="<%=valor%>"></input>
				<input type="hidden" name="numerico" value="true"></input>
				<input type="hidden" name="enteros" value="<%=longitud%>"></input>
				<input type="hidden" name="decimales" value="<%=escala%>"></input>
			<%}else{%>
				<input type="text" name="valor" class="box" maxlength="<%=max%>" value="<%=valor%>"></input>	
				<input type="hidden" name="numerico" value="false"></input>			
			<%}%>
			<%}%>
			</td>
		</tr>
	</table>	
	
	</siga:ConjCampos>

	</td>
</tr>

</table>

<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="A,X" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->
	
	<script language="JavaScript">

		function trim(s) {
			  while (s.substring(0,1) == ' ') {
			    s = s.substring(1,s.length);
			  }
			  while (s.substring(s.length-1,s.length) == ' ') {
			    s = s.substring(0,s.length-1);
			  }
			  return s;
		}

		  //Funcion que dice si valor es uno de los simbolos EstaVacio:	
		  function esUnSimboloEstaVacio(valor) {
			  	var values = "20-21-22-23-";
			  	var listaValues = values.split("-");
			  	
			  	for (var i=0; i<listaValues.length; i++) {
			  		if (listaValues[i] == valor)
			  			return true;
			  	}
			  	
			  	return false;
		  }
			
		function isANumber (number){
			 var hasError=isNaN (number);
			 return (!hasError);
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
			  dec = obj.substring (dotPosition+1).length;
			  if (dec==0 && punto){
			  	alert('<%=errorPunto%>');
			  	return (false);
			  }
			}else{
			  alert('<%=errorNumerico%>');
			  return (false);
			}
			return (true);
		  }
		  
		  
		
		<!-- Asociada al boton Aceptar -->
		function accionAceptar() 
		{		
			var operacion = document.getElementById("operacion");
			
			if (operacion.value==""){
				alert('<%=errorOperacion%>');
		  		return;
		  	}
		  	operacionT = operacion.options[operacion.selectedIndex].text;
		  	operacionV = operacion.options[operacion.selectedIndex].value;	
		  	
		  	var valor = document.getElementById("valor");
		  	if (valor.tagName=="SELECT"){
		  		if (!esUnSimboloEstaVacio(valor.value) && valor.value==""){
			  		alert('<%=errorValor%>');
			  		return;
			  	}
		  		valorT = valor.options[valor.selectedIndex].text;
		  		valorV = valor.options[valor.selectedIndex].value;	
		  	}else{
		  		valortrim = trim(valor.value);
		  		if (!esUnSimboloEstaVacio(valortrim) && valortrim==""){
		  			alert('<%=errorValor%>');
		  			return;
			  	}
			  	valorT = valor.value;
		  		valorV = valor.value;
		  		
			  	var numerico = document.getElementById("numerico");
			  	if (numerico.value=="true"){	  				  	
			  		var enteros = document.getElementById("enteros");
			  		var decimales = document.getElementById("decimales");
			  		if(!checkFloat(valorT,enteros.value,decimales.value)){
						return;
		  			}
			  	}
		  	}		  	
		    
		    text = '<%=separadorIni%>'+'<%=nombreCampo%>'+' '+operacionT+' '+valorT+'<%=separadorFin%>';
		    value = '<%=separadorIni%>'+'#'+'<%=idTabla%>'+'#'+'<%=idCampo%>'+'#'+operacionV+'#'+valorV+'#'+'<%=separadorFin%>';
		    
		    var aux = new Array();
				aux[0]=text;
				aux[1]=value;
			
			top.cierraConParametros(aux);
		}
	
		<!-- Asociada al boton Cancelar -->
		function accionCancelar() 
		{		
			window.close();
		}	

	</script>
			
	</body>
</html>