<!DOCTYPE html>
<html>
<head>
<!-- listadoDesignas.jsp -->
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

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.ScsDesignaAdm"%>


<bean:define id="registrosSeleccionados" name="BuscarDesignasForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="BuscarDesignasForm" property="datosPaginador" type="java.util.HashMap"/>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idInstitucionLocation = usr.getLocation();
	String idioma=usr.getLanguage().toUpperCase();
	//Vector obj = (Vector)request.getAttribute("resultado");
	
	ses.removeAttribute("resultado");
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheckPersona = "";
	if (datosPaginador!=null) {
	 if ( datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  	resultado = (Vector)datosPaginador.get("datos");
	    PaginadorBind paginador = (PaginadorBind)datosPaginador.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }else{
		resultado =new Vector();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	 }
	}else{
      	resultado =new Vector();
	  	paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}	 
	String action=app+"/JGR_Designas.do?noReset=true";
    /**************/
    
	// Chapuza para el Colegio de Badajoz: al perfil "Procuradores" no hay que permitirle borrar las designaciones
	// Se ha de hacer asi porque en SIGA no existe el permiso de borrado separado del de edicion
    String botonesLinea = "E,C" + ((usr.getLocation().equals("2010") && Arrays.asList(usr.getProfile()).contains("PRO")) ? "" : ",B");
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body onload="cargarChecks();checkTodos()" class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_MantenimientoDesignas.do" method="post" target="mainWorkArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo"  styleId = "modo" value = ""/>
			<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados" />
			<html:hidden property="datosPaginador"  styleId="datosPaginador" />
			<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
			<input type="hidden" id="actionModal" name="actionModal" value="">
			<html:hidden property="compensar" styleId="compensar"  value="" />
		</html:form>
		
		<html:form action="/JGR_Designas" method="POST" target="resultado" style="display:none">			
		    <html:hidden property = "modo"  styleId = "modo" value = ""/>
		    <html:hidden property="registrosSeleccionados" styleId="registrosSeleccionados" />
			<html:hidden property="datosPaginador"  styleId="datosPaginador" />
			<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		</html:form>	
		
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/> ,
			   gratuita.listarGuardias.literal.turno,facturacion.ano,gratuita.busquedaDesignas.literal.codigo,gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.estado,gratuita.listarDesignasTurno.literal.nColegiado,expedientes.auditoria.literal.nombreyapellidos,pestana.justiciagratuitaejg.interesado,gratuita.busquedaDesignas.literal.validada,"
			   columnSizes="5,11,4,5,7,5,10,13,14,6,9" >
				
		<%if (resultado.size()<1){%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		<%}else{%>
			  <%
		    	
				String defendidos="";
		    	String estado = "";
				for (int recordNumber = 1,contadorFila=1; recordNumber-1 < resultado.size(); recordNumber++)
				{	
					Row fila = (Row)resultado.elementAt(recordNumber-1);
					Hashtable registro = (Hashtable) fila.getRow();

					defendidos =    (String) registro.get("DEFENDIDOS");
					
					estado = (String) registro.get("ESTADO");
					if (estado!=null){
						if (estado.equalsIgnoreCase("V")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.abierto");
						else if (estado.equalsIgnoreCase("F")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.finalizado");
					 	else if(estado.equalsIgnoreCase("A")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.anulado");
						else estado="";
					}
					
					
			 	%>	
				  	<siga:FilaConIconos fila='<%=String.valueOf(contadorFila)%>' botones="<%=String.valueOf(botonesLinea)%>" clase="listaNonEdit">
						
						<td align="center">
						<%String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("ANIO")+"||"+registro.get("IDTURNO")+"||"+registro.get("NUMERO");
						boolean isChecked = false;
						for (int z = 0; z < registrosSeleccionados.size(); z++) {
							Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
							String clave = (String)clavesRegistro.get("CLAVE");
							if (valorCheck.equals(clave)) {
								isChecked = true;
								break;
							}
						}if (isChecked) {%>
								<input type="checkbox" value="<%=valorCheck%>"  id="chkPersona"  name="chkPersona" checked onclick="pulsarCheck(this)">
							<%} else {%>
								<input type="checkbox" value="<%=valorCheck%>"  id="chkPersona"  name="chkPersona" onclick="pulsarCheck(this)" >
						<%}%>
						</td>
						<td>
							<input type='hidden' id='oculto<%=String.valueOf(contadorFila)%>_1' name='oculto<%=String.valueOf(contadorFila)%>_1' value='<%=registro.get("IDTURNO")%>'>
							<input type='hidden' id='oculto<%=String.valueOf(contadorFila)%>_2' name='oculto<%=String.valueOf(contadorFila)%>_2' value='<%=registro.get("IDLETRADODESIG")%>'>
							<input type='hidden' id='oculto<%=String.valueOf(contadorFila)%>_3' name='oculto<%=String.valueOf(contadorFila)%>_3' value='<%=registro.get("NUMERO")%>'>
							
							<input type='hidden' id='datosCarta' name='datosCarta' value='idinstitucion==<%=usr.getLocation()%>##idturno==<%=registro.get("IDTURNO")%>##anio==<%=registro.get("ANIO")%>##numero==<%=registro.get("NUMERO")%>##ncolegiado==<%=registro.get("NCOLEGIADO")%>##codigo==<%=registro.get("CODIGO")%>'>
							<input type='hidden' id='oculto<%=String.valueOf(contadorFila)%>_4'  name='oculto<%=String.valueOf(contadorFila)%>_4' value='<%=registro.get("ANIO")%>'>
							<%=registro.get("TURNODESIG")%>&nbsp;
						</td>
						<td><%=registro.get("ANIO")%>&nbsp;</td>
				<!--	<td>< %=registro.get("NUMERO")%>&nbsp;</td> -->
				        <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
						<td><%=registro.get("CODIGO")%>-<%=registro.get("SUFIJO")%>&nbsp;</td>
						<%}else{%>
						<td><%=registro.get("CODIGO")%>&nbsp;</td>
						<% }%>
						<td><%=registro.get("FECHAENTRADA")%>&nbsp;</td>
						<td><%=estado%></td>
						<td><%=registro.get("NCOLEGIADO")%>&nbsp;</td>
						<td><%=registro.get("LETRADODESIG")%>&nbsp;</td>
						<td><%=UtilidadesString.mostrarDatoJSP(defendidos)%>&nbsp;</td>
						<td><%=registro.get("ACTNOVALIDA")%>&nbsp;</td>
					</siga:FilaConIconos>	
					
				<% contadorFila++;
				} // for%>	
		<%}%>
	</siga:Table>
	<%
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
			: registrosSeleccionados.size()));
	
	if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								registrosSeleccionados="<%=regSeleccionados%>"
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	


<!-- FIN: LISTA DE VALORES -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden styleId="idInstitucion"  property="idInstitucion"  value = "<%=idInstitucionLocation%>"/>
	<html:hidden styleId="idTipoInforme" property="idTipoInforme"  value="OFICI"/>
	<html:hidden styleId="enviar" property="enviar"  value="1"/>
	<html:hidden styleId="descargar" property="descargar" value="1"/>
	<html:hidden styleId="datosInforme" property="datosInforme"/>
	<html:hidden styleId="modo" property="modo"  value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal' id='actionModal'>
</html:form>
	<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo"  id="modo"  value="">
	<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD"  value="">
</form>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
<script language="JavaScript">
			ObjArray = new Array();
		
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}
 
	   
	function pulsarCheck(obj){
		
		if (!obj.checked ){
		   		
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			ObjArray.push(obj.value);
		   	seleccionados1=ObjArray;
		}
		  	
		  	
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		checkTodos();
		
		   
	}
	function cargarChecks(){
	   		
	   	 	
		<%if (registrosSeleccionados!=null){
			
	   		for (int p=0;p<registrosSeleccionados.size();p++){
	   		 	
		   		Hashtable clavesEJG= (Hashtable) registrosSeleccionados.get(p);
		   		
		   		
				valorCheckPersona=(String)clavesEJG.get("CLAVE");
				
						
				%>
					var aux='<%=valorCheckPersona%>';
					ObjArray.push(aux);
				<%
			} 
	   	}%>
	   	
		ObjArray.toString();
		seleccionados1=ObjArray;
			
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		if(document.getElementById('registrosSeleccionadosPaginador'))
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			
	}
	function cargarChecksTodos(o){
  	   if (document.getElementById('registrosSeleccionadosPaginador')){ 	
  		var conf = confirm("<siga:Idioma key='paginador.message.marcarDesmarcar'/>"); 
	   	 
	   	if (conf){
			ObjArray = new Array();
		   	if (o.checked){
		   		parent.seleccionarTodos('<%=paginaSeleccionada%>');
		   	 		
				
			}else{
				ObjArray1= new Array();
			 	ObjArray=ObjArray1;
			 	seleccionados1=ObjArray;
			 	if(seleccionados1){
				document.forms[0].registrosSeleccionados.value=seleccionados1;
				var ele = document.getElementsByName("chkPersona");
					
				for (i = 0; i < ele.length; i++) {
					if(!ele[i].disabled)	
						ele[i].checked = false; 
						
						
				}
				}
	
			 }
	   	  
	   	  }else{
	   	  	if (!o.checked ){
		   	  		var ele = document.getElementsByName("chkPersona");
						
				  	for (i = 0; i < ele.length; i++) {
				  		if(!ele[i].disabled){
				  			if(ele[i].checked){	
		     					ele[i].checked = false;
		     				
								ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
							}
						}
				   	}
				   	
				   	seleccionados1=ObjArray;
			   }else{
				   	var ele = document.getElementsByName("chkPersona");
							
				  	for (i = 0; i < ele.length; i++) {
				  		if(!ele[i].disabled){
							if(!ele[i].checked){				  		
			    				ele[i].checked = true;
								ObjArray.push(ele[i].value);
							}
						}
				   	}
				   		
			   		seleccionados1=ObjArray;
			   }
			   document.forms[0].registrosSeleccionados.value=seleccionados1;
		   		
	   	  }
	   	 if (document.getElementById('registrosSeleccionadosPaginador')){ 		 
		  document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		 }
		} 
	 }
	   
	function checkTodos(){
	
	 	var ele = document.getElementsByName("chkPersona");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
   			if(!ele[i].checked){
   				todos=0;
   				break;
   			} 
   		}
	   
	   if (todos==1){
			document.getElementById("chkGeneral").checked=true;
		}else{
			document.getElementById("chkGeneral").checked=false;
		}
	   
				
			
			
   	}
	function refrescarLocal(){
		parent.buscar();
	}
	function accionComunicar()
		{
		
			//sub();
		datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			anio  = idRegistros.substring(0,index);

			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idTurno  = idRegistros.substring(0,index);
			
			numero = idRegistros.substring(index+2);
			
 		   	datos = datos +"idInstitucion=="+idInstitucion +"##anio=="+anio + "##idTurno==" +idTurno+"##numero==" +numero+"##idTipoInforme==OFICI%%%";
			
			
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
						
			if(numElementosSeleccionados>50){
				document.InformesGenericosForm.descargar.value = '0';

			}
			else{
				document.InformesGenericosForm.descargar.value = '1';
			}
			
			
			document.InformesGenericosForm.datosInforme.value=datos;
			
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
			
			
      	    					
					
				
		}
			
		function accionCerrar() {		
		}


		
		function borrar(fila) {
			   var datos;
			   if (confirm('¿Está seguro de que desea eliminar el registro?')){
				   if (confirm("<siga:Idioma key='gratuita.compensacion.confirmacion'/>")) // y desea compensar al letrado ...
					{
							document.forms[0].compensar.value = "1";
						}else{
							document.forms[0].compensar.value = "0";
						}
				   preparaDatos(fila,'tablaDatos',document.getElementById('tablaDatosDinamicosD'));
				   
			   	datos = document.getElementById('tablaDatosDinamicosD');
			   	/*
			       datos.value = ""; 
			   	var i, j;
			   	for (i = 0; i < 10; i++) {
			      		var tabla;
			      		tabla = document.getElementById('tablaDatos');
			      		if (i == 0) {
			        		var flag = true;
			        		j = 1;
			        		while (flag) {
			          			var aux = 'oculto' + fila + '_' + j;
			          			var oculto = document.getElementById(aux);
			          			if (oculto == null)  { flag = false; }
			          else { 
			          if(oculto.value=='')       		oculto.value=' ';
						datos.value = datos.value + oculto.value + ','; }
			          			j++;
			        		}
			        		datos.value = datos.value + "%"
			      		} else { j = 2; }
			      		if ((tabla.rows[fila].cells)[i].innerHTML == "")
			        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			      		else
			        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
			   	}
			   	*/
			   	var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "Borrar";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;
			 	}
			 }
		
		
</script>
	</body>
</html>
		  
		
