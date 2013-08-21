<!DOCTYPE html>
<html>
<head>
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
<%@ page import = "org.redabogacia.sigaservices.app.util.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

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
		catalogo = catalogo.trim();
		if (deCertificado != null && deCertificado.equals("1")) {
			valorCatalog = "C";
			elementoSel0.add(valorCatalog);
		} else {
			valorCatalog = catalogo;
			elementoSel0.add(valorCatalog);
		}
	} else {
		valorCatalog = "";
		catalogo = "P";
		if (deCertificado != null && deCertificado.equals("1")) {
			valorCatalog = "C";
			elementoSel0.add(valorCatalog);
		} else {
			valorCatalog = "P";
			elementoSel0.add(valorCatalog);
		}

	}
	valorCatalog = valorCatalog.trim();

	//PRODUCTOS:
	String sIdTipoProducto = "0";
	if (request.getAttribute("tipoProducto") != null){
		sIdTipoProducto = ((Integer) request.getAttribute("tipoProducto")).toString();
		if (sIdTipoProducto == null || "".equals(sIdTipoProducto))
			sIdTipoProducto = "0";
	}

	String sIdProducto = "0";	
	if (request.getAttribute("categoriaProducto") != null){
		sIdProducto = (String) request.getAttribute("categoriaProducto");
		if (sIdProducto == null || "".equals(sIdProducto))
			sIdProducto = "0";		
	}

	String sIdProductoInstitucion = "0";
	if (request.getAttribute("producto") != null){
		sIdProductoInstitucion =((Long) request.getAttribute("producto")).toString();
		if (sIdProductoInstitucion == null || "".equals(sIdProductoInstitucion))
			sIdProductoInstitucion = "0";
	}

	//SERVICIOS:
	String sIdTipoServicio = "0";
	if (request.getAttribute("tipoServicio") != null){
		sIdTipoServicio = ((Integer) request.getAttribute("tipoServicio")).toString();
		if (sIdTipoServicio == null || "".equals(sIdTipoServicio))
			sIdTipoServicio = "0";
	}

	String sIdServicio = "0";
	if (request.getAttribute("categoriaServicio") != null){
		sIdServicio = (String) request.getAttribute("categoriaServicio");
		if (sIdServicio == null || "".equals(sIdServicio))
			sIdServicio = "0";
	}

	String sIdServicioInstitucion = "0";
	if (request.getAttribute("servicio") != null){
		sIdServicioInstitucion = ((Long) request.getAttribute("servicio")).toString();
		if (sIdServicioInstitucion == null || "".equals(sIdServicioInstitucion))
			sIdServicioInstitucion = "0";		
	}

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


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
		function disableEnterKey(e){
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
		
	    function mostrarProducto(){
	    	jQuery("div.servicio").hide();
			jQuery("div.producto").show();
		}
	    
		function mostrarServicio(){
			jQuery("div.producto").hide();
	    	jQuery("div.servicio").show();			
		}

		function actualizarCliente() {
				document.solicitudCompraForm.target = "mainWorkArea";
				document.solicitudCompraForm.modo.value = "actualizarCliente";
		}		  
	
		function borrarCarrito (mostrarMensaje){
				if (mostrarMensaje) {
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.cambioInstitucion"/>";
					if(confirm(mensaje)) {						
							document.solicitudCompraForm.idInstitucion.value = document.busquedaClientesModalForm.idInstitucion.value;
							document.solicitudCompraForm.target = "mainWorkArea";
							document.solicitudCompraForm.modo.value = "borrarCarrito";						
						    document.solicitudCompraForm.submit();
					}
					else {
						document.busquedaClientesModalForm.reset();
					}
				} 
				else {
						document.solicitudCompraForm.idInstitucion.value = <%=user.getLocation()%>;
						document.solicitudCompraForm.target = "mainWorkArea";
						document.solicitudCompraForm.modo.value = "borrarCarrito";						
					    document.solicitudCompraForm.submit();
				}
		}		  
		  
		function buscarCliente() {
		
		 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
		 	//document.busquedaClientesModalForm.submit();
		 	// Si he recuperado datos y el nuevo idpersona es distinto de la anterior persona
			if((resultado    != undefined) && 
				 (resultado[0] != undefined) && 
				 (resultado[0] != document.solicitudCompraForm.idPersona.value)) {
				
					
					if(document.solicitudCompraForm.idPersona.value != resultado[0]) {
						var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.vaciarCarrito"/>";
						
						if (document.solicitudCompraForm.idPersona.value != "") {
                            
							if(confirm(mensaje)) {						

									document.solicitudCompraForm.idPersona.value = resultado[0];
									document.solicitudCompraForm.numeroColegiado.value = resultado[2];
									document.solicitudCompraForm.nif.value = resultado[3];
									document.solicitudCompraForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];
									if (resultado[3]!="") document.solicitudCompraForm.nombrePersona.value = document.solicitudCompraForm.nombrePersona.value + " - "+resultado[3]+"";
									if (resultado[2]!="") document.solicitudCompraForm.nombrePersona.value = "Num. Col: "+resultado[2]+" - " + document.solicitudCompraForm.nombrePersona.value;

								 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
								 	document.busquedaClientesModalForm.nif.value = resultado[3];
								 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	
									if (resultado[3]!="") document.busquedaClientesModalForm.nombrePersona.value = document.busquedaClientesModalForm.nombrePersona.value + " - "+resultado[3]+"";
									if (resultado[2]!="") document.busquedaClientesModalForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.busquedaClientesModalForm.nombrePersona.value;

									// copiamos el valor del idInstitucionPresentador
									document.solicitudCompraForm.idInstitucionPresentador.value = document.busquedaClientesModalForm.idInstitucionPresentador.value;
									
								 	//Borramos el carro ya que hemos cambiado de Cliente:
									borrarCarrito(false);							
									
							}						

						} else {
							document.solicitudCompraForm.idPersona.value = resultado[0];
							document.solicitudCompraForm.numeroColegiado.value = resultado[2];
							document.solicitudCompraForm.nif.value = resultado[3];
							document.solicitudCompraForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
							if (resultado[3]!="") document.solicitudCompraForm.nombrePersona.value = document.solicitudCompraForm.nombrePersona.value + " - "+resultado[3]+"";
							if (resultado[2]!="") document.solicitudCompraForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.solicitudCompraForm.nombrePersona.value;

						 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
						 	document.busquedaClientesModalForm.nif.value = resultado[3];
						 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	
							if (resultado[3]!="") document.busquedaClientesModalForm.nombrePersona.value = document.busquedaClientesModalForm.nombrePersona.value + " - "+resultado[3]+"";
							if (resultado[2]!="") document.busquedaClientesModalForm.nombrePersona.value =  "Num. Col: "+resultado[2]+" - " + document.busquedaClientesModalForm.nombrePersona.value;

							//Institucion:
							document.solicitudCompraForm.idInstitucion.value = resultado[1];

							// copiamos el valor del idInstitucionPresentador
							document.solicitudCompraForm.idInstitucionPresentador.value = document.busquedaClientesModalForm.idInstitucionPresentador.value;
						
							actualizarCliente();
							
							
						}
				}					
			}			 	
		}

		function actualizarInstitucionPresentador(objeto) {
			document.solicitudCompraForm.idInstitucionPresentador.value = objeto.value;
			actualizarCliente();
		}						
		
		function buscarProducto() {
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
		
		//aalg: INC_09651. Se pierde el interesado al volver atrás en una solicitud de compra
		jQuery("#numeroNifTagBusquedaPersonas").val('<%=numero%>');
		jQuery("#nombrePersona").val('<%=nombre%>');

		function mostrarColegio() {
			<%
				String stylePresentador = "display:none;";
				String styleCampoBlanco = "display:block;";
			if (esConsejo && user.getStrutsTrans().equals("PYS_SolicitarCertificado")){%>
				<%if (request.getSession().getAttribute("volver") != null && request.getSession().getAttribute("volver").equals("s")) {
					stylePresentador = "display:block;";
					styleCampoBlanco = "display:none;";
					request.getSession().setAttribute("volver","");
				%>
				//jQuery("#catalogo").attr("disabled","disabled");
				jQuery("#idInstitucionPresentador").attr("disabled","disabled");
				<%}		
			  }else{
				if (request.getSession().getAttribute("volver") != null && request.getSession().getAttribute("volver").equals("s")) {%>
					//jQuery("#catalogo").attr("disabled","disabled");
				<%}%>
			<%request.getSession().setAttribute("volver","");}%>
		}
		
		function mostrarPresentador(mostrar){
			if (mostrar){
				document.getElementById("campoBlanco").style.display="none";
				document.getElementById("campoBlancoPresentador").style.display="block";
				document.getElementById("comboPresentador").style.display="block";
				document.getElementById("presentador").style.display="block";
			} else {
				document.getElementById("campoBlanco").style.display="block";
				document.getElementById("campoBlancoPresentador").style.display="none";
				document.getElementById("comboPresentador").style.display="none";
				document.getElementById("presentador").style.display="none";
			}
		}
		
		jQuery(function(){			
			
			jQuery("#catalogo").on("change", function(){
				console.debug("#catalogo.change");
				jQuery("#tipoProducto").val("");
				jQuery("#tipoProducto").change();
				jQuery("#tipoServicio").val("");
				jQuery("#tipoServicio").change();
				jQuery("#categoriaProducto").val("");
				jQuery("#categoriaProducto").change();
				jQuery("#categoriaServicio").val("");
				jQuery("#categoriaServicio").change();
				jQuery("#producto").val("");
				jQuery("#producto").change();
				jQuery("#servicio").val("");
				jQuery("#servicio").change();
				
				var institucion = <%=idInstitucionP%>;
				var catalogoVal = jQuery(this).val();
				if (catalogoVal=='C' && (institucion == 2000 || institucion>=3000))
					mostrarPresentador(true);						
				else
					mostrarPresentador(false);
				
			 	document.solicitudCompraForm.nombreProducto.value='';
				if (catalogoVal=='P'||catalogoVal==''||catalogoVal=='C'){
				    if (catalogoVal=='P'){
				   	<%if(esLetrado)	{%>
				  		jQuery("#producto").data("queryid", "getProductosInstitucionLetrado");
					<%}else{%>
					  	jQuery("#producto").data("queryid", "getProductosInstitucion");
					<%}%>
					}else{
					  	<%if(esLetrado)	{%>
					         jQuery("#producto").data("queryid", "getCertificadosInstitucionLetrado");
						<%}else{%>
							jQuery("#producto").data("queryid", "getCertificadosInstitucion");					 
						<%}%>
					}
				    jQuery("#producto").change();
				    console.debug("#catalogo.change Productos ('P','C','')");
				    mostrarProducto();
				}else{
					console.debug("#catalogo.change Servicios !('P','C','')");
					mostrarServicio();
				}
			});
			
			jQuery("#idInstitucionPresentador").on("change", function(){
				console.debug("#idInstitucionPresentador.change");
				document.solicitudCompraForm.idInstitucionPresentador.value = jQuery(this).val();
				actualizarCliente();
			});
			
			jQuery("#btnSolicitar").on("click", function(){
				console.debug("#btnSolicitar.click");
				sub();
				f = document.solicitudCompraForm;
				if(f.idPersona.value == ""){
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionCliente"/>";
					alert (mensaje);
					fin();					
					return false;
				}
				
				var tipo = "producto";
				if(jQuery("#producto").is(":visible")){
					tipo = "producto";
					if(f.tipoProducto.value == "" || f.categoriaProducto.value == "" || f.producto.value == ""){
						var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionProducto"/>";
						alert (mensaje);
						fin();			
						return false;
					}					
				} else if(jQuery("#servicio").is(":visible")){
					tipo = "servicio";
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
			});
			
			jQuery("#catalogo").change();
			
			mostrarColegio();
		});

	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
					<siga:ConjCampos leyenda="pys.solicitudCompra.leyenda.datosSolicitud">		
						<table class="tablaCampos" align="center" border=0>
						<html:form action="/CEN_BusquedaClientesModal" method="POST" target="submitArea" type="">
		  				<input type="hidden" id="actionModal"  name="actionModal" value="">
		  				<input type="hidden" id="modo" name="modo" value="abrirBusquedaModal">
		  				<input type="hidden" id="ventana"  name="ventana" 		value="solicitud">
						<input type="hidden" id="deCertificado" name="deCertificado"	value="<%=deCertificado%>">
						<input type="hidden" id="clientes" name="clientes"	value="1">
						<html:hidden name="busquedaClientesModalForm" property="numeroColegiado" styleId="numeroColegiado"  value="<%=numero%>" size="10" ></html:hidden>
						<html:hidden name="busquedaClientesModalForm" property="nif" value="<%=nif%>" styleId="nif"></html:hidden>
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
	    <input type="hidden" id="deCertificado" name="deCertificado"	value="<%=deCertificado%>">
		<tr>
			<td>

			<table class="tablaCampos" align="center" border="0">
				<!-- FILA -->
				<tr>
					<td class="labelText">
						<siga:Idioma key="pys.solicitudCompra.literal.catalogo" />&nbsp;(*)
					</td>
					<td id="catalogo_td">
						<%
						//cmbCatalogo= Select trim('P') AS ID, 'PRODUCTO' AS DESCRIPCION FROM DUAL UNION select trim('S') AS ID,'SERVICIO' AS DESCRIPCION FROM DUAL UNION select trim('C') AS ID,'CERTIFICADO' AS DESCRIPCION FROM DUAL
						// CAMBIAMOS ESTE COMBO POR JSON (NO HACE FALTA IR A BASE DE DATOS)
						List<KeyValue> catalogoOptions = new ArrayList<KeyValue>();
						catalogoOptions.add(new KeyValue("C", "CERTIFICADO"));
						catalogoOptions.add(new KeyValue("P", "PRODUCTO"));
						catalogoOptions.add(new KeyValue("S", "SERVICIO"));
						
						String catalogoDisabled = "false";
						String catalogoSelected = valorCatalog;
						if (deCertificado.equals("1")){
							catalogoDisabled = "true";
							catalogoSelected = "C";
						}
						String productoStyle = "";
						String servicioStyle = "display:none;";
						if (catalogoSelected.equals("S")){
							productoStyle = "display:none;";
							servicioStyle = "";
						}
						%>
						<siga:Select id="catalogo"
									queryId="JSON" 
									dataJSON="<%=UtilidadesString.createTagSelectDataJson(catalogoOptions)%>"
									selectedIds="<%=catalogoSelected%>"
									required="true"
									disabled="<%=catalogoDisabled%>"
									width="118"/>						
					</td>
					
					<td><p class="labelText">></p></td>
					<td id="tipo_td">
						<div class="producto" style="<%=productoStyle%>" style="<%=productoStyle%>">
						<siga:Select id="tipoProducto"
									queryId="getTiposProductos"
									queryParamId="idtipoproducto"
									childrenIds="categoriaProducto"
									selectedIds="<%=sIdTipoProducto%>"
									width="120"/>
						</div>				
						<div class="servicio" style="<%=servicioStyle%>">
						<siga:Select id="tipoServicio" 
									queryId="getTiposServicios"
									queryParamId="idtiposervicio"
									childrenIds="categoriaServicio"
									selectedIds="<%=sIdTipoServicio%>"
									width="120"/>
						</div>					
					</td>
					<td><p class="labelText">></p></td>
					<td id="categoria_td">
						<div class="producto" style="<%=productoStyle%>">
						<siga:Select id="categoriaProducto"
									queryId="getProductosDeTipo"
									queryParamId="idproducto"
									parentQueryParamIds="idtipoproducto"
									params='<%=UtilidadesString.createJsonString("idtipoproducto", sIdTipoProducto) %>'
									childrenIds="producto"
									selectedIds="<%=sIdProducto%>"
									width="120"/>
						</div>						
						<div class="servicio" style="<%=servicioStyle%>">
						<siga:Select id="categoriaServicio"
									queryId="getServiciosDeTipo"
									queryParamId="idservicio"
									parentQueryParamIds="idtiposervicio"
									params='<%=UtilidadesString.createJsonString("idtiposervicio", sIdTipoServicio) %>'
									childrenIds="servicio"
									selectedIds="<%=sIdServicio%>"
									width="120"/>
						</div>				
					</td>					
					<td><p class="labelText">></p></td>
					<td id="producto_servicio_td">
						<%
						HashMap<String, String> hmProductoParams = new HashMap<String, String>();
						hmProductoParams.put("idtipoproducto", sIdTipoProducto);
						hmProductoParams.put("idproducto", sIdProducto);
						
						HashMap<String, String> hmServicioParams = new HashMap<String, String>();
						hmServicioParams.put("idtiposervicio", sIdTipoServicio);
						hmServicioParams.put("idservicio", sIdServicio);
						
						String productoQueryId = "getProductosInstitucion";
						String servicioQueryId = "getServiciosInstitucion";
						if (catalogoSelected.equals("C") || catalogoSelected.equals("")){
							productoQueryId = "getCertificadosInstitucion";
						}
						hmServicioParams.put("solicitaralta", DB_FALSE);
						hmServicioParams.put("automatico", DB_TRUE);
						if (esLetrado) {
								dato[1] = DB_TRUE;
								dato[2] = DB_FALSE;
								
								productoQueryId += "Letrado";
	 							servicioQueryId += "Letrado";
						} else {
 							dato[1] = DB_FALSE;
 							hmServicioParams.put("automatico", DB_FALSE);
						}
								
						%>							
							<div class="producto" style="<%=productoStyle%>">
							<siga:Select id="producto"
										queryId="<%=productoQueryId %>"
										queryParamId="idproductoinstitucion"
										parentQueryParamIds="idtipoproducto,idproducto"
										params="<%=UtilidadesString.createJsonString(hmProductoParams) %>"
										selectedIds="<%=sIdProductoInstitucion %>"
										width="250"/>
							</div>
							<div class="servicio" style="<%=servicioStyle%>">
							<siga:Select id="servicio"
										queryId="<%=servicioQueryId %>"
										queryParamId="idservicioinstitucion"
										parentQueryParamIds="idtiposervicio,idservicio"
										params="<%=UtilidadesString.createJsonString(hmServicioParams) %>"
										selectedIds="<%=sIdServicioInstitucion %>"
										width="250"/>
							</div>
					</td>

					<td>
						<html:button property="idButton" styleId="btnSolicitar" styleClass="button">
							<siga:Idioma key="general.boton.solicitarCompra" />
						</html:button>
					</td>					

				</tr>
				<tr>																					
					<td id ="presentador" class="labelText" width="50px" style="<%=stylePresentador%>">
							<siga:Idioma key="pys.solicitudCompra.literal.presentador"/>
					</td>
					<td id = "comboPresentador" colspan="7" style="<%=stylePresentador%>">
						<siga:Select id="idInstitucionPresentador"
									queryId="getInstitucionesAbreviadas"
									selectedIds="<%=idInstitucionPresentador%>"
									readOnly="<%=soloLectura%>"
									width="240"/>
					</td>
										
					<td id="campoBlancoPresentador" style="<%=stylePresentador%>">&nbsp;</td>
					<td id="campoBlanco" colspan="6" style="<%=styleCampoBlanco%>">&nbsp;</td>
																																																							
					<td align=left id="nombreProducto" colspan="2">
						<html:text name="solicitudCompraForm" property="nombreProducto" style="width:300px" maxlength="100"
						  styleClass="box" readonly="false" onKeyPress="return disableEnterKey(event)"/>
					</td>					
					<td align=left id="solicitarServicio1">
						<html:button property="idButton" onclick="return buscarProducto();" styleClass="button">
							<siga:Idioma key="general.boton.search" />
						</html:button>
					</td>
				</tr>
		</table>	
			<input type="hidden" id="solicitaralta" value="<%=hmServicioParams.get("solicitaralta")%>" style="display:none"></input>
			<input type="hidden" id="automatico" value="<%=hmServicioParams.get("automatico")%>" style="display:none"/></input>	
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
			style="position:relative;height:38%;width:100%;"			 
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
			marginwidth="0"					 
			style="position:relative;height:40%;width:100%;"
			>					
		</iframe>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>
