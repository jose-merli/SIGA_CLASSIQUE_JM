<!-- listadoMorosos.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 21-03-2005 Versión inicial
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitiveBind"%>


<bean:define id="registrosSeleccionados" name="ConsultaMorososForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="ConsultaMorososForm" property="datosPaginador" type="java.util.HashMap"/>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idioma=userBean.getLanguage().toUpperCase();
	String idInstitucion = userBean.getLocation();
	
	/** PAGINADOR ***/
	String paginaSeleccionada ="";
	String valorCheckPersona = "";
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	
	Vector resultado=null;
	
	
	if (datosPaginador!=null) {
		
	 

	
	
	 if ( datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  resultado = (Vector)datosPaginador.get("datos");
	  
	    PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)datosPaginador.get("paginador");
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
	
	
	
	String action=app+"/FAC_ConsultaMorosos.do";
    /**************/
	
	
%>	

<style>
.tiraAbajo {
	background-color : #<%=src.get("color.titleBar.BG")%>;
	position:absolute; width:964; height:35; z-index:0; top: 365px; left: 0px
}

.botonesAbajo {	
	position:absolute; width:964; height:35; z-index:1; top: 369px; left: 0px
}

</style>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="facturacion.consultamorosos.literal.titulo" 
			localizacion="facturacion.consultamorosos.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body onload="cargarChecks();checkTodos()"  class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/FAC_ConsultaMorosos.do" method="POST" target="submitArea" style="display:none">			
		    <html:hidden property = "modo" value = ""/>
		    
		    <html:hidden property="registrosSeleccionados" />
			<html:hidden property="datosPaginador" />
			<html:hidden property="seleccionarTodos" />
		    
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "idPersona" value = ""/>
			<html:hidden property = "fechaDesde" value = ""/>
			<html:hidden property = "fechaHasta" value = ""/>
			<html:hidden property = "numColegiado" value = ""/>
			<html:hidden property = "nombre" value = ""/>
			<html:hidden property = "modelo" value = ""/>
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD" value = "">
			<input type="hidden" name="actionModal" value="">
			
			
		</html:form>
		
	
		<html:form action="/FAC_Devoluciones" target="submitArea">
			<html:hidden property="modo" value="renegociarCobrosRecobros" />
			<html:hidden property="datosFacturas" />
			<input type="hidden" name="actionModal" value="">
			
		</html:form>
		
				
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol=" <input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/> ,facturacion.consultamorosos.literal.ncolegiado,
				   facturacion.consultamorosos.literal.nombreyapellidos,
		   		  	facturacion.consultamorosos.literal.fecha,
		   		  	facturacion.consultamorosos.literal.factura,
		   		  	facturacion.consultamorosos.literal.estadoFactura,
		   		  	facturacion.consultamorosos.literal.pendientepago,facturacion.consultamorosos.literal.comunicaciones, "
		   		  tamanoCol="5,10,20,8,12,18,8,10,7"
		   		  alto="70%"
		   		  ajusteBotonera="true"
		   		  activarFilaSel="true"	
				  ajustePaginador="true">
	   		     		    		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
				  		Row fila = (Row)resultado.elementAt(i);
						FilaExtElement[] elemento=new FilaExtElement[2];
				  		elemento[0]=new FilaExtElement("consultar","consultar",SIGAConstants.ACCESS_READ);
				  		elemento[1]=new FilaExtElement("enviar","comunicar",SIGAConstants.ACCESS_READ);
				  		String idFactura = (String)fila.getString(FacFacturaBean.C_IDFACTURA);
				  		String idPersona = (String)fila.getString(FacFacturaBean.C_IDPERSONA);
				  		
						
%>
	  			<siga:FilaConIconos 
	  				fila='<%=""+(i+1)%>'
	  				botones=""
	  				visibleConsulta = "no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				pintarEspacio="false"
	  				elementos='<%=elemento%>' 
					
	  				clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=idPersona%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString(CenColegiadoBean.C_NCOLEGIADO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("NOMBRE")%>">
						<input type="hidden" name="idPersona<%=""+(i+1)%>" value="<%=idPersona%>">
						<input type="hidden" name="numeroFactura<%=""+(i+1)%>" value="<%=fila.getString(""+FacFacturaBean.C_NUMEROFACTURA+"")%>">
						<input type="hidden" name="idFactura<%=""+(i+1)%>" value="<%=idFactura%>">
						<input type="hidden" name="idInstitucion<%=""+(i+1)%>" value="<%=fila.getString(""+FacFacturaBean.C_IDINSTITUCION+"")%>">
						
					<td align="center">
					<%
					String valorCheck =idFactura+"||"+idPersona+"||"+idInstitucion;
					
							boolean isChecked = false;
							

							for (int z = 0; z < registrosSeleccionados.size(); z++) {
									
								Hashtable clavesRegistro = (Hashtable) registrosSeleccionados
										.get(z);
								String clave = (String)clavesRegistro.get("CLAVE");
								
								if (valorCheck.equals(clave)) {
									isChecked = true;
									break;
								}
								

							}
							
								if (isChecked) {
			%>
								
									<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" checked onclick="pulsarCheck(this)">
								<%
									} else {
								%>
									<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" onclick="pulsarCheck(this)" >
							<%
								}
							
							%>
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(CenColegiadoBean.C_NCOLEGIADO))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRE"))%></td>
					<td align="center"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(userBean.getLanguage(),fila.getString(""+FacFacturaBean.C_FECHAEMISION+"")))%></td>
					<td align="right"><%=UtilidadesString.mostrarDatoJSP(fila.getString(""+FacFacturaBean.C_NUMEROFACTURA+""))%></td>
					<td align="left"><%=UtilidadesString.mostrarDatoJSP(fila.getString("ESTADO_FACTURA"))%></td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("DEUDA"),2))%>&nbsp;&euro;</td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("COMUNICACIONES"))%></td>
					
				</siga:FilaConIconos>
<%
					}
				}
%>

           
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			<siga:ConjBotonesAccion botones="GX,COM,RN" />
			
			
  
			

<%if ( datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
			: registrosSeleccionados.size()));
%>
	  
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								registrosSeleccionados="<%=regSeleccionados%>"
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      <%}%>
		<!-- FIN: LISTA DE VALORES -->
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="COBRO"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>	
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
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
			
	  		var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>');  
	   	   	
		   	if (conf){
				ObjArray = new Array();
			   	if (o.checked){
			   		parent.seleccionarTodos('<%=paginaSeleccionada%>');
			   	 	
					
				}else{
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;
				 	
				 	seleccionados1=ObjArray;
				 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPersona");
						
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled)	
							ele[i].checked = false; 
							
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
		   	  		 
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
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
   	
   	function comunicar(fila)
	{
		var auxPers = 'idPersona' + fila;
		var idPersona = document.getElementById(auxPers).value;
		var auxInst = 'idInstitucion' + fila ;
		var idInstPersona = document.getElementById(auxInst).value;
		var auxIdFactura = 'idFactura' + fila ;
		var idFactura = document.getElementById(auxIdFactura).value;		
	   	datos = "idFactura=="+idFactura + "##idPersona=="+idPersona + "##idInstitucion==" +idInstPersona+"##idTipoInforme==COBRO%%%" ; 
		
				
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
   	
	function accionComunicar()
		{
		
			//sub();
			var datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			
			idFactura  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idPersona  = idRegistros.substring(0,index);
			idInstitucion = idRegistros.substring(index+2);
 		   	datos = datos +"idFactura=="+idFactura +"##idPersona=="+idPersona + "##idInstitucion==" +idInstitucion+"##idTipoInforme==COBRO%%%";
			
			
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
			if(numElementosSeleccionados>50){
				document.InformesGenericosForm.descargar.value ='0';
			}
			else{
				document.InformesGenericosForm.descargar.value ='1';
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
		function accionGenerarExcels(){
	   		sub();
			
			datos = "";
			for (i = 0; i < ObjArray.length; i++) {
				var idRegistros = ObjArray[i];
				index = idRegistros.indexOf('||');
				//alert("index"+index);
				idFactura  = idRegistros.substring(0,index);
			
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				idPersona  = idRegistros.substring(0,index);
				idInstitucion = idRegistros.substring(index+2);
 		   		datos = datos +	idPersona + "," +idFactura + "#"; 
			
			}
		if (datos == '') {
			
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
		document.forms[0].tablaDatosDinamicosD.value = datos;
		document.forms[0].modo.value ='generaExcel';
			
			
		//alert("datosPaginador:"+document.forms[0].datosPaginador )
		//alert("registrosSeleccionados:"+document.forms[0].registrosSeleccionados)
		//alert("si:"+document.forms[0].datosPaginador)
		document.forms[0].submit();

		
		fin();
			
   	}
   	
   	
   	
   	
   	
		
		function refrescarLocal()
		{			
			parent.buscar() ;			
		}

		
		
		//Guardo los campos seleccionados
		function seleccionarFila(fila){
		    var idpersona = 'oculto' + fila + '_' + 1;		    
		    var ncolegiado = 'oculto' + fila + '_' + 2;
		    var nombreyapellidos = 'oculto' + fila + '_' +3;
			//Datos del elemento seleccionado:
			document.forms[0].idPersona.value = document.getElementById(idpersona).value;
			document.forms[0].numColegiado.value = document.getElementById(ncolegiado).value;
			document.forms[0].nombre.value = document.getElementById(nombreyapellidos).value;
			idPersona = document.getElementById ("idPersona"+fila).value;
			idFactura = document.getElementById ("idFactura"+fila).value;
			datos = idPersona + "," +idFactura + "#"; 
			document.forms[0].tablaDatosDinamicosD.value = datos;
			
			
		}
		

		<!-- Funcion asociada al boton Consultar -->
		function consultar(fila) 
		{		
			//Datos del elemento seleccionado:
			
			seleccionarFila(fila)			
			
			//Submito
			document.forms[0].modo.value = "consultaMoroso";
			var salida = ventaModalGeneral(document.forms[0].name,"G"); 			
		}
				
		
		
		
		
		
		
	
	function accionCerrar() 
	{		
		window.top.close();
	}
	
	function renegociar() 
	{
		var isConfirmado = confirm('<siga:Idioma key="facturacion.consultamorosos.mensaje.confirmaRenegociar"/>');
		if(!isConfirmado)
			return;
		datos = "";
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			//alert("index"+index);
			idFactura  = idRegistros.substring(0,index);
			
			
 		   	datos = datos +	idFactura + "##"; 
			
			
		}
		if (datos == '') {
			
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
		document.DevolucionesForm.datosFacturas.value = datos;
		
		var resultado = ventaModalGeneral("DevolucionesForm","P");
		if (resultado=="MODIFICADO")
		{
		}
	
	
	}
			
		
			

	</script>
	
	</body>
</html>