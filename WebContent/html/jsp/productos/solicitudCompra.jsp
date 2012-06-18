<!-- solicitudCompra.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS --> 
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user = (UsrBean) request.getSession().getAttribute(
			"USRBEAN");

	boolean esLetrado = user.isLetrado();
	boolean esConsejo=false;
	int idConsejo = new Integer(user.getLocation()).intValue();
	if(idConsejo==2000 || idConsejo>=3000)
		esConsejo=true;
	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;
	String estiloComboInstitucionPresentador = "boxCombo";

	String nombre = (String) request.getAttribute("nombrePersona");
	String numero = (String) request.getAttribute("numero");
	String nif = (String) request.getAttribute("nif");
	Long lIdPersona = (Long) request.getAttribute("idPersona");
	String deCertificado = (String) request.getSession().getAttribute(
			"deCertificado");
	String idPersona = "";
	if (lIdPersona != null) {
		idPersona = String.valueOf(lIdPersona);
	}

	String existeCarro = (String) request.getAttribute("existeCarro");

	// Para el combo de institucion
	String idPersonaCombo[] = new String[1];
	idPersonaCombo[0] = idPersona;

	ArrayList idInstitucion = new ArrayList();
	ArrayList idInstitucionPresentador = new ArrayList();

	String idInstitucionP = user.getLocation();
	String soloLectura = "false";
	Integer aux = new Integer(3);
	if (request.getAttribute("idInstitucion") != null) {
		aux = (Integer) request.getAttribute("idInstitucion");
		idInstitucion.add(aux.toString());
	} else {
		idInstitucion.add(user.getLocation());
	}

	if (request.getAttribute("idInstitucionPresentador") != null) {// si se ha seleccionado colegio presentador

		if (new Integer(idInstitucionP.substring(0, 1)).intValue() >= 3
				|| idInstitucionP.equals("2000")) {// Si es CGAE o Consejos, se permite editar 
			// el combo de colegio presentador
			aux = (Integer) request
					.getAttribute("idInstitucionPresentador");
			idInstitucionPresentador.add(aux.toString());

		} else {// Si es un colegio, el combo del colegio presentador no es editable y por defecto es el propio Colegio

			idInstitucionPresentador.add(user.getLocation());
		}

	} else {// si no se ha seleccionado colegio presentador

		if (new Integer(idInstitucionP.substring(0, 1)).intValue() >= 3
				|| idInstitucionP.equals("2000")) {// Si es CGAE o Consejos, se permite editar 
			// el combo de colegio presentador

			idInstitucionPresentador.add("");

		} else {// Si es un colegio, el combo del colegio presentador no es editable y por defecto es el propio Colegio

			idInstitucionPresentador.add(user.getLocation());

		}

	}

	String dato[] = new String[3];
	dato[0] = String.valueOf(idInstitucion.get(0));

	//Parametro para la busqueda:
	String[] parametroCombo = { String.valueOf(idInstitucion.get(0)) };

	//
	//Datos de los Productos y Servicios seleccionados en los combos:
	//
	//CATALOGO
	ArrayList elementoSel0 = new ArrayList();
	String catalogo = "";
	String valorCatalog = "";
	if (request.getAttribute("catalogo") != null) {
		catalogo = (String) request.getAttribute("catalogo");
		if (deCertificado != null && deCertificado.equals("1")) {
			valorCatalog = "C";
			elementoSel0.add(valorCatalog.trim());
		} else {
			elementoSel0.add((request.getAttribute("catalogo"))
					.toString());
		}
	} else {
		valorCatalog = "";
		catalogo = "P";
		if (deCertificado != null && deCertificado.equals("1")) {
			valorCatalog = "C";
			elementoSel0.add(valorCatalog.trim());
		} else {
			valorCatalog = "P";
			elementoSel0.add(valorCatalog.trim());
		}

	}

	//PRODUCTOS:
	ArrayList elementoSel1 = new ArrayList();
	if (request.getAttribute("tipoProducto") != null)
		elementoSel1.add(((Integer) request
				.getAttribute("tipoProducto")).toString());
	else
		elementoSel1.add("0");

	ArrayList elementoSel2 = new ArrayList();
	if (request.getAttribute("categoriaProducto") != null)
		elementoSel2.add((String) request
				.getAttribute("categoriaProducto"));
	else
		elementoSel2.add("0");

	ArrayList elementoSel3 = new ArrayList();
	if (request.getAttribute("producto") != null)
		elementoSel3.add(((Long) request.getAttribute("producto"))
				.toString());
	else
		elementoSel3.add("0");

	//SERVICIOS:
	ArrayList elementoSel4 = new ArrayList();
	if (request.getAttribute("tipoServicio") != null)
		elementoSel4.add(((Integer) request
				.getAttribute("tipoServicio")).toString());
	else
		elementoSel4.add("0");

	ArrayList elementoSel5 = new ArrayList();
	if (request.getAttribute("categoriaServicio") != null)
		elementoSel5.add((String) request
				.getAttribute("categoriaServicio"));
	else
		elementoSel5.add("0");

	ArrayList elementoSel6 = new ArrayList();
	if (request.getAttribute("servicio") != null)
		elementoSel6.add(((Long) request.getAttribute("servicio"))
				.toString());
	else
		elementoSel6.add("0");

	//Controlamos que si el usuario es letrado muestre el combo.
	//Si es agente/administrador aparece el combo en modo lectura seleccionando la institucion del userbean del letrado buscado.
	String lecturaComboInstitucion = "true", estiloComboInstitucion = "boxConsulta";
	if (esLetrado) {
		lecturaComboInstitucion = "false";
		estiloComboInstitucion = "boxCombo";
	}
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	
	<% if(user.getStrutsTrans().equals("PYS_SolicitarCertificado")){ %>
		<siga:Titulo titulo="certificados.solicitudCertificado.literal.titulo"	localizacion="certificados.solicitudes.literal.localizacion"/>
	<%}else{ %>
		<siga:Titulo titulo="pys.solicitudCompra.cabecera"	localizacion="pys.solicitudCompra.ruta"/>
	<% } %>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

			function disableEnterKey(e)
			{
				
			     if(window.event.keyCode == 13){
			    	 if (document.getElementById("nombreProducto").value!=""){
			    		 buscarProducto();
			    	 }else{
			          	return false;
			    	 }
			     }else{
			          return true;				
				}
			}
			
		    function ocultarCombosProducto(){
			    document.getElementById("tipoProducto1").style.display="none";
				document.getElementById("categoriaProducto1").style.display="none";
				document.getElementById("producto1").style.display="none";
				document.getElementById("solicitarProducto1").style.display="none";
				document.getElementById("tipoServicio1").style.display="block";
				document.getElementById("categoriaServicio1").style.display="block";
				document.getElementById("servicio1").style.display="block";
				document.getElementById("solicitarServicio1").style.display="block";
				document.getElementById("filaNaranja").focus();	// Por problemas de repintado de pantalla
			}
			function ocultarCombosServicio(){
			    document.getElementById("tipoProducto1").style.display="block";
				document.getElementById("categoriaProducto1").style.display="block";
				document.getElementById("producto1").style.display="block";
				document.getElementById("solicitarProducto1").style.display="block";
				document.getElementById("tipoServicio1").style.display="none";
				document.getElementById("categoriaServicio1").style.display="none";
				document.getElementById("servicio1").style.display="none";
				document.getElementById("solicitarServicio1").style.display="none";
				document.getElementById("filaNaranja").focus();	// Por problemas de repintado de pantalla
			}
			function cargarCombos() {
			 
			   
				<% 
				  //se inicializan estos valores porque cuando cambiamos el tipo de catalogo, cambiamos el combo de productos que debe mostrarse
				  // y la consulta se ejecuta sin tener todos los valores que necesita, como no necesitamos a priori que muestren nada, los 
				  // inicializamos a -1.
				  
				   dato[1] = "-1";
				   dato[2] = "-1";
				
				if (deCertificado.equals("1") ){
				   
				%>
				     
				   document.getElementById("catalogo").disabled=true;
				   <%if(esLetrado)	{%>//Si venimos de la solicitud de certificados cambiamos el tipo del combo producto para que solo filtre por certificados
				 
			            cambiarTipoComboSiga('producto','cmbCertificadoInstitucionLetrado');
					<%}else{%>
					
					   cambiarTipoComboSiga('producto','cmbCertificadoInstitucion');
					 
					<%}%>	
				 
				<%}else{
				
				     if (catalogo.equals("C")){ 
					     if(esLetrado)	{%>// si venimos de la solicitud de productos y servicios y despues de recuperar el cliente estaba seleccionado 'certificados'
						                   // debemos cambiar el tipo del combo producto  para que solo filtre por certificados
			            	cambiarTipoComboSiga('producto','cmbCertificadoInstitucionLetrado');
							
						<%}else{%>
					   		cambiarTipoComboSiga('producto','cmbCertificadoInstitucion');
							
					 
						<%}
					}%>
					document.getElementById("catalogo").disabled=false;
			   <%}	%>	
					
				   
				  
				  
				
				 
				
				//Seleccion de los combos de Productos:
			 if (document.solicitudCompraForm.catalogo.value=='P'||document.solicitudCompraForm.catalogo.value==''||document.solicitudCompraForm.catalogo.value=='C'){	
			    ocultarCombosServicio();
			    
			 
				var cmb1 = document.getElementsByName("tipoProducto");
				var cmb2 = cmb1[0]; 
				cmb2.onchange();
				
				
				
			}else{	

			   
			    ocultarCombosProducto();
				var cmbs1 = document.getElementsByName("tipoServicio");
				var cmbs2 = cmbs1[0]; 
							
				
				cmbs2.onchange();
								
				
				
				
				
			}	
				
				
				//Seleccion de los combos de Servicios:			
				//var tmp1 = document.getElementsByName("tipoServicio");
				//var tmp2 = tmp1[0]; 
				//tmp2.onchange();*/
			}

			function actualizarCliente() {
			     
					document.all.solicitudCompraForm.target = "mainWorkArea";
					document.all.solicitudCompraForm.modo.value = "actualizarCliente";
					//document.all.solicitudCompraForm.submit();
								   		   
			}		  
		
			function borrarCarrito (mostrarMensaje){
					if (mostrarMensaje) {
						var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.cambioInstitucion"/>";
						if(confirm(mensaje)) {						
								document.all.solicitudCompraForm.idInstitucion.value = document.busquedaClientesModalForm.idInstitucion.value;
								document.all.solicitudCompraForm.target = "mainWorkArea";
								document.all.solicitudCompraForm.modo.value = "borrarCarrito";						
							    document.all.solicitudCompraForm.submit();
						}
						else {
							document.busquedaClientesModalForm.reset();
						}
					} 
					else {
							document.all.solicitudCompraForm.idInstitucion.value = <%=user.getLocation()%>;
							document.all.solicitudCompraForm.target = "mainWorkArea";
							document.all.solicitudCompraForm.modo.value = "borrarCarrito";						
						    document.all.solicitudCompraForm.submit();
					}
			}		  
		  
			function buscarCliente() {
			
			 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
			 	//document.all.busquedaClientesModalForm.submit();
			 	// Si he recuperado datos y el nuevo idpersona es distinto de la anterior persona
				if((resultado    != undefined) && 
					 (resultado[0] != undefined) && 
					 (resultado[0] != document.all.solicitudCompraForm.idPersona.value)) {
					
						
						if(document.all.solicitudCompraForm.idPersona.value != resultado[0]) {
							var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.vaciarCarrito"/>";
							
							if (document.all.solicitudCompraForm.idPersona.value != "") {
                             
								if(confirm(mensaje)) {						

										document.all.solicitudCompraForm.idPersona.value = resultado[0];
										document.all.solicitudCompraForm.numeroColegiado.value = resultado[2];
										document.all.solicitudCompraForm.nif.value = resultado[3];
										document.all.solicitudCompraForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];
										if (resultado[3]!="") document.all.solicitudCompraForm.nombrePersona.value = document.all.solicitudCompraForm.nombrePersona.value + " - "+resultado[3]+"";
										if (resultado[2]!="") document.all.solicitudCompraForm.nombrePersona.value = "Num. Col: "+resultado[2]+" - " + document.all.solicitudCompraForm.nombrePersona.value;
	
									 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
									 	document.busquedaClientesModalForm.nif.value = resultado[3];
									 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	
										if (resultado[3]!="") document.all.busquedaClientesModalForm.nombrePersona.value = document.all.busquedaClientesModalForm.nombrePersona.value + " - "+resultado[3]+"";
										if (resultado[2]!="") document.all.busquedaClientesModalForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.all.busquedaClientesModalForm.nombrePersona.value;

										// copiamos el valor del idInstitucionPresentador
										document.all.solicitudCompraForm.idInstitucionPresentador.value = document.all.busquedaClientesModalForm.idInstitucionPresentador.value;
										
									 	//Borramos el carro ya que hemos cambiado de Cliente:
										borrarCarrito(false);							
										
								}						

							} else {
								document.all.solicitudCompraForm.idPersona.value = resultado[0];
								document.all.solicitudCompraForm.numeroColegiado.value = resultado[2];
								document.all.solicitudCompraForm.nif.value = resultado[3];
								document.all.solicitudCompraForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
								if (resultado[3]!="") document.all.solicitudCompraForm.nombrePersona.value = document.all.solicitudCompraForm.nombrePersona.value + " - "+resultado[3]+"";
								if (resultado[2]!="") document.all.solicitudCompraForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.all.solicitudCompraForm.nombrePersona.value;

							 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
							 	document.busquedaClientesModalForm.nif.value = resultado[3];
							 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	
								if (resultado[3]!="") document.all.busquedaClientesModalForm.nombrePersona.value = document.all.busquedaClientesModalForm.nombrePersona.value + " - "+resultado[3]+"";
								if (resultado[2]!="") document.all.busquedaClientesModalForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.all.busquedaClientesModalForm.nombrePersona.value;

								//Institucion:
								document.all.solicitudCompraForm.idInstitucion.value = resultado[1];

								// copiamos el valor del idInstitucionPresentador
								document.all.solicitudCompraForm.idInstitucionPresentador.value = document.all.busquedaClientesModalForm.idInstitucionPresentador.value;
							
								actualizarCliente();
								
								
							}
					}					
				}			 	
			}

			function actualizarInstitucionPresentador(objeto) {
				document.all.solicitudCompraForm.idInstitucionPresentador.value = objeto.value;

				actualizarCliente();
			}
			
			
			function solicitar(tipo){
				sub();
				f = document.solicitudCompraForm;
				if(f.idPersona.value == ""){
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionCliente"/>";
					alert (mensaje);
					fin();					
					return false;
					}
				if(tipo=="Producto"){
					if(f.tipoProducto.value == "" || f.categoriaProducto.value == "" || f.producto.value == ""){
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionProducto"/>";
					alert (mensaje);
					fin();			
						return false;
					}					
				}
				else if(tipo=="Servicio"){
					if(f.tipoServicio.value == "" || f.categoriaServicio.value == "" || f.servicio.value == ""){
						var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionProducto"/>";
						alert (mensaje);	
						fin();		
						return false;
					}
				}
   			    document.solicitudCompraForm.target="resultado";
				document.solicitudCompraForm.modo.value = "solicitar";
				document.solicitudCompraForm.concepto.value = tipo;
				document.solicitudCompraForm.submit();
			}
			
			
			
			function cambiarTipoComboSiga(idCombo,tipoNew){// se utliza para cambiar el tipo del combo productos segun seleccionemos en el combo catalogo productos o 
			                                               // certificados

  				var cadena = top.frames[0].document.getElementById(idCombo+'Frame').src;
				var ini = cadena.indexOf('cmb');
				if (ini == -1) {
					return;
				}
				var fin = cadena.indexOf('&', ini+1);
				if (fin == -1) { 
					top.frames[0].document.getElementById(idCombo+'Frame').src = cadena.substring(0,ini) + tipoNew;
				return;
				}
				top.frames[0].document.getElementById(idCombo+'Frame').src = cadena.substring(0,ini) + tipoNew + cadena.substring(fin);
				
			}
			
			
			function mostrarCombos(){
				var institucion = <%=idInstitucionP%>;				
				if (document.solicitudCompraForm.catalogo.value=='C' && (institucion == 2000 || institucion>=3000))
				{
					document.getElementById("comboPresentador").style.display="block";
					document.getElementById("presentador").style.display="block";								
				}
				else
				{
					document.getElementById("comboPresentador").style.display="none";
					document.getElementById("presentador").style.display="none";										
				}
					
			  document.solicitudCompraForm.nombreProducto.value='';

			  if (document.solicitudCompraForm.catalogo.value=='P'||document.solicitudCompraForm.catalogo.value==''||document.solicitudCompraForm.catalogo.value=='C'){
			      if (document.solicitudCompraForm.catalogo.value=='P'){
				    <%if(esLetrado)	{%>
			            cambiarTipoComboSiga('producto','cmbProductoInstitucionLetrado');
					<%}else{%>
					   cambiarTipoComboSiga('producto','cmbProductoInstitucion');
					 
					<%}%>	
				  }else{
				    <%if(esLetrado)	{%>
			            cambiarTipoComboSiga('producto','cmbCertificadoInstitucionLetrado');
					<%}else{%>
					   cambiarTipoComboSiga('producto','cmbCertificadoInstitucion');
					 
					<%}%>	
				  }
//			    Cada vez que cambiemos el combo de catalogo, se limpia todos los demas combos
				var cmb1 = document.getElementsByName("tipoProducto");
				var a = cmb1[0].options;
				a[0].selected=true;
				cmb1[0].onchange();
//	
			    ocultarCombosServicio();
				
			  }else{
			    ocultarCombosProducto();
				
//			    Cada vez que cambiemos el combo de catalogo, se limpia todos los demas combos				
				var cmbs1 = document.getElementsByName("tipoServicio");
				var a = cmbs1[0].options;
				a[0].selected = true;
				cmbs1[0].onchange();
//								
			  }
			}
			
			
			function buscarProducto()
			{
			 	sub();
 			 	if (document.solicitudCompraForm.catalogo.value==''){
			    	alert ("<siga:Idioma key="producto.campo.catalogo"/>");
			    	fin();
					return;
			 }else{ 			 
				  if (document.solicitudCompraForm.catalogo.value=='S'){
			   			document.solicitudCompraForm.concepto.value='Servicio';
			  	  }else if(document.solicitudCompraForm.catalogo.value=='P'){
      		   			document.solicitudCompraForm.concepto.value='Producto';			
			  		}else{
			   			document.solicitudCompraForm.concepto.value='Certificado';
			  		}			
			  	document.solicitudCompraForm.target="resultado1";
   		      	document.solicitudCompraForm.modo.value = "buscarProducto";
			  	document.solicitudCompraForm.submit();							 			 
			 }	
			 fin();
			}
			
		function desactivar(valor){
		
		   var valorAuxp=valor.value;
		   if (document.getElementById("producto1").style.display=="block"  && valor.name=='productoSel'){
		    
		    if (valorAuxp==''||valorAuxp==0){
			  document.solicitudCompraForm.nombreProducto.disabled=false;
			}else{
			  document.solicitudCompraForm.nombreProducto.value='';
			  document.solicitudCompraForm.nombreProducto.disabled=true;
			}
		  }	
		  
		  if (document.getElementById("servicio1").style.display=="block" && valor.name=='servicioSel'){
		    
		    if (valorAuxp==''||valorAuxp==0){
			  document.solicitudCompraForm.nombreProducto.disabled=false;
			}else{
			  document.solicitudCompraForm.nombreProducto.value='';
			  document.solicitudCompraForm.nombreProducto.disabled=true;
			}
		  }	
		}
		

								
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onload="cargarCombos();ajusteAlto('resultado');mostrarColegio();">

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
					<siga:ConjCampos leyenda="pys.solicitudCompra.leyenda.datosSolicitud">		
						<table class="tablaCampos" align="center" border=0>
						<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type="">
		  				<input type="hidden" name="actionModal" value="">
		  				<input type="hidden" name="modo" value="abrirBusquedaModal">
		  				<input type="hidden" name="ventana" 		value="solicitud">
						<input type="hidden" name="deCertificado"	value="<%=deCertificado%>">
						<input type="hidden" name="clientes"	value="1">
						<html:hidden name="busquedaClientesModalForm" property="numeroColegiado" value="<%=numero%>" size="10" ></html:hidden>
						<html:hidden name="busquedaClientesModalForm" property="nif" value="<%=nif%>"></html:hidden>
						 <tr>	
							<%
							if (esConsejo){
						 		
						 	%>	
								
								<td>
								<siga:BusquedaPersona tipo="personas" idPersona="idPersona" ></siga:BusquedaPersona>
								</td>

							<%
							 	}else if (!esLetrado){ 
							%>
							 
								<td>
								<siga:BusquedaPersona tipo="colegiado" idPersona="idPersona"></siga:BusquedaPersona>
								</td>

	
							<%
							 	}else{ 
							%>

							<!-- FILA -->
								
									<td class="labelText" width="180">
										<siga:Idioma key="pys.solicitudCompra.literal.interesado"/>&nbsp;(*)
									</td>
									<td  width="150">
										<html:text name="busquedaClientesModalForm" property="nombrePersona" value="<%=nombre%>" size="40" styleClass="boxConsulta" readOnly="true"></html:text>
									</td>
							<%}%>
									
								</tr>	

							</html:form>
							</table>
						</siga:ConjCampos>
						


<table class="tablaCampos" align="center" border="0">
	<html:form action="/PYS_GenerarSolicitudes.do" method="POST"
		target="resultado">

		<html:hidden name="solicitudCompraForm" property="modo" value="" />
		<html:hidden name="solicitudCompraForm" property="concepto" value="" />
		<html:hidden name="solicitudCompraForm" property="idPersona"
			value="<%=idPersona%>" />
		<html:hidden name="solicitudCompraForm" property="numeroColegiado"
			value="<%=numero%>" />
		<html:hidden name="solicitudCompraForm" property="nombrePersona"
			value="<%=nombre%>" />
		<html:hidden name="solicitudCompraForm" property="nif"
			value="<%=nif%>" />
		<html:hidden name="solicitudCompraForm" property="idInstitucion"
			value="<%=String.valueOf(idInstitucion.get(0))%>" />								 
		<input type="hidden" name="deCertificado" value="<%=deCertificado%>">
		<tr>
			<td>

			<table class="tablaCampos" align="center" border="0">
				<!-- FILA -->
				<tr>
					<td class="labelText" valign="top"><siga:Idioma
						key="pys.solicitudCompra.literal.catalogo" />&nbsp;(*)</td>
					<td><siga:ComboBD nombre="catalogo" tipo="cmbCatalogo"
						clase="boxCombo" obligatorio="true" ancho="95"
						accion="mostrarCombos();" elementoSel="<%=elementoSel0%>" />
					</td>
					<td class="labelText" id="filaNaranja"> <p>></p> </td>
					<td id="tipoProducto1"><siga:ComboBD nombre="tipoProducto"
						tipo="cmbTipoProducto" clase="boxCombo" obligatorio="false"
						accion="Hijo:categoriaProducto;compruebaComboSigaPadre();"
						elementoSel="<%=elementoSel1%>" ancho="120" />
					</td>
					<td id="tipoServicio1" style="display: none"><siga:ComboBD
						nombre="tipoServicio" tipo="cmbTipoServicio_1" clase="boxCombo"
						obligatorio="false"
						accion="Hijo:categoriaServicio;compruebaComboSigaPadre()"
						elementoSel="<%=elementoSel4%>" ancho="120" />

					</td>

					<td class="labelText"><p>></p></td>
					<td id="categoriaProducto1"><siga:ComboBD
						nombre="categoriaProducto" tipo="cmbProducto_1" clase="boxCombo"
						accion="Hijo:producto;" hijo="t" elementoSel="<%=elementoSel2%>"
						parametro="<%=parametroCombo%>" ancho="130" /></td>
					<td id="categoriaServicio1" style="display: none"><siga:ComboBD
						nombre="categoriaServicio" tipo="cmbServicio_1" clase="boxCombo"
						accion="Hijo:servicio;" hijo="t" elementoSel="<%=elementoSel5%>"
						parametro="<%=parametroCombo%>" ancho="130" /></td>


					<td class="labelText"><p>></p></td>

					<td id="producto1">
					<%
						if (esLetrado) {
								dato[1] = DB_TRUE;
								dato[2] = DB_FALSE;
					%> <siga:ComboBD nombre="producto"
						tipo="cmbProductoInstitucionLetrado" clase="boxCombo"
						parametro="<%=dato%>" hijo="t" elementoSel="<%=elementoSel3%>"
						accion="parent.desactivar(this);" ancho="220" /> <%
 	} else {
 			dato[1] = DB_FALSE;
 %> <siga:ComboBD nombre="producto" tipo="cmbProductoInstitucion"
						clase="boxCombo" parametro="<%=dato%>" hijo="t"
						elementoSel="<%=elementoSel3%>" accion="parent.desactivar(this);"
						ancho="220" /> <%
 	}
 %>
					</td>

					<td id="servicio1" style="display: none">
					<%
						if (esLetrado) {
								dato[1] = DB_TRUE;
								dato[2] = DB_FALSE;
					%> <siga:ComboBD nombre="servicio"
						tipo="cmbServicioInstitucionLetrado" clase="boxCombo"
						obligatorio="false" parametro="<%=dato%>" hijo="t"
						elementoSel="<%=elementoSel6%>" accion="parent.desactivar(this);"
						ancho="130" /> <%
 	} else {
 			dato[1] = DB_FALSE;
 %> <siga:ComboBD nombre="servicio" tipo="cmbServicioInstitucion"
						clase="boxCombo" obligatorio="false" parametro="<%=dato%>"
						hijo="t" elementoSel="<%=elementoSel6%>"
						accion="parent.desactivar(this);" ancho="130" /> <%
 	}
 %>
					</td>



					<td align=right id="solicitarProducto1"><html:button
						property="idButton" onclick="return solicitar('Producto');"
						styleClass="button">
						<siga:Idioma key="general.boton.solicitarCompra" />
					</html:button></td>
					<td align=left id="solicitarServicio1" style="display: none">
					<html:button property="idButton"
						onclick="return solicitar('Servicio');" style="align:right"
						styleClass="button">
						<siga:Idioma key="general.boton.solicitarCompra" />
					</html:button></td>

				</tr>
		</table>
		<table class="tablaCampos" align="center" border="0">
			<tr>																					
					<td id ="presentador" class="labelText">
							<siga:Idioma key="pys.solicitudCompra.literal.presentador"/>
					</td>
					<td id = "comboPresentador">									
						<siga:ComboBD nombre="idInstitucionPresentador" 
								tipo="cmbInstitucionesAbreviadas" 
								elementoSel ="<%=idInstitucionPresentador%>"
								clase="<%=estiloComboInstitucionPresentador%>"
								readonly="<%=soloLectura%>"
								accion="actualizarInstitucionPresentador(this);"												
						/>																			
					</td>
					<td id="campoBlanco">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
																																																									
					<td align=right id="nombreProducto"><html:text name="solicitudCompraForm"
						property="nombreProducto" size="20" maxlength="100"
						styleClass="box" readonly="false" onKeyPress="return disableEnterKey(event)"/></td>
					<td align=right id="solicitarServicio1"><html:button
						property="idButton" onclick="return buscarProducto();"
						styleClass="button">
						<siga:Idioma key="general.boton.search" />
						</html:button>
					</td>
			</tr>
		</table>
			</td>
			
		</tr>
		<!-- FILA -->
	</html:form>
</table>


<!-- INICIO: IFRAME LISTA RESULTADOS BUSQUEDA -->
		<iframe align="center" src="<%=app%>/html/jsp/productos/productosEncontrados.jsp"
						id="resultado1"
						name="resultado1" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0"
						style="position:relative;height:45%;width:100%;"			 
						>					
		</iframe>

		<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- INICIO: IFRAME LISTA ELEMENTOS SOLICITADOS -->
		<iframe align="center" src="<%=app%>/html/jsp/productos/productosSolicitados.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0";					 
						style="position:relative;width:100%;"
						>					
		</iframe>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->



<script>

function compruebaComboSigaPadre () 
{   
<%	String aux1 = "" + (Integer)request.getAttribute("tipoProducto");
    if (aux1 == null) aux1 = new String ("");

     if (aux1 != null) { %>
          if (document.solicitudCompraForm.tipoProducto.value!='<%=aux1%>'){
		    limpiarSeleccionComboSiga ('categoriaProducto');
			limpiarSeleccionComboSiga ('producto');
		  }
 <%  } 

  	String aux2 = "" + (Integer)request.getAttribute("tipoServicio");
    if (aux2 == null) aux2 = new String ("");

     if (aux2 != null) { %>
          if (document.solicitudCompraForm.tipoServicio.value!='<%=aux2%>'){
		    limpiarSeleccionComboSiga ('categoriaServicio');
			limpiarSeleccionComboSiga ('servicio');
		  }
  <% } %>
}

function compruebaComboSigaHijo (valor, tipo) 

{   
  if (tipo=='P'){ 
    <%	String aux11 = (String)request.getAttribute("categoriaProducto");
	    
	   
    if (aux11 == null) aux11 = new String ("");

     if (aux11 != null) { %>
	  var aux111='<%=aux11%>';
	  if (aux111.indexOf(',')!=-1){
	   aux111=aux111.substring((aux111.indexOf(','))+1);
	  } 
	  
	  if (valor.indexOf(',')!=-1){
	   valor=valor.substring((valor.indexOf(','))+1);
	  }
          if (valor!=aux111){
			limpiarSeleccionComboSiga ('producto');
		  }
 <%  } %>
  }else{
  	/*< %String aux22 =  (String)request.getAttribute("categoriaServicio");
    if (aux22== null) aux22 = new String ("");

     if (aux22 != null) { %>
          if (valor!='< %=aux22%>'){
		    limpiarSeleccionComboSiga ('servicio');
		  }
  < % } %>*/
      
  }

}

function limpiarSeleccionComboSiga (idCombo) 
{
	var cadena = top.frames[0].document.getElementById(idCombo+'Frame').src;
	var ini = cadena.indexOf('&elementoSel=[');
	if (ini == -1) {
		return;
	}
	var fin = cadena.indexOf('&', ini+1);
	if (fin == -1) { 
		top.frames[0].document.getElementById(idCombo+'Frame').src = cadena.substring(0,ini) + "&elementoSel=";
		return;
	}
	top.frames[0].document.getElementById(idCombo+'Frame').src = cadena.substring(0,ini) + "&elementoSel=" + cadena.substring(fin);

}

function oculta(id)
{         
	var elDiv = document.getElementById(id); 
	//se define la variable "elDiv" igual a nuestro div         
	elDiv.style.display='none'; 
	//damos un atributo display:none que oculta el div            
}
function muestra(id)
{       
	var elDiv = document.getElementById(id); 
	//se define la variable "elDiv" igual a nuestro div  
	elDiv.style.display='block';//damos un atributo display:block que  el div
}

function mostrarColegio()
{		
	<%
	if (esConsejo && user.getStrutsTrans().equals("PYS_SolicitarCertificado")){	%>
		document.getElementById("comboPresentador").style.display="block";
		document.getElementById("presentador").style.display="block";
		<%if (request.getSession().getAttribute("volver") != null && request.getSession().getAttribute("volver").equals("s")) {%>
		document.solicitudCompraForm.catalogo.disabled=true;
		document.solicitudCompraForm.idInstitucionPresentador.disabled=true;
		
		<%request.getSession().setAttribute("volver","");}%>		
	<%}else
	{
	%>	
	document.getElementById("campoBlanco").style.display="block";
		document.getElementById("comboPresentador").style.display="none";
		document.getElementById("presentador").style.display="none";
		<%if (request.getSession().getAttribute("volver") != null && request.getSession().getAttribute("volver").equals("s")) {%>
			document.solicitudCompraForm.catalogo.disabled=true;
		<%}%>
	<%request.getSession().setAttribute("volver","");}%>
	
}


</script>



</body>
</html>
