<!-- mantenimientoProductos.jsp -->
<!-- 
	 Permite mostrar/editar los distintos productos
	 VERSIONES:
	 miguel.villegas 2-02-2005 actualizacion y adecuacion a los nuevos aires
	 david.sanchezp / 27-10-2005: anhadido el combo concepto y maquetado.
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

<!-- IMPORTS -->
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.productos.form.MantenimientoProductosForm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		
	String remitente = request.getAttribute("modelo").toString(); // Obtengo la operacion (consulta,modificar o insertar)a realizar

	// Valor tarjeta, Domiciliacion Bancaria
	String valorMomentoCargo[] = {""+ClsConstants.TIPO_FORMAPAGO_TARJETA,""+ClsConstants.TIPO_FORMAPAGO_FACTURA};   

	String valorModo = "";
	
	// Declaraciones y gestion de la informacion recogida y de los valores a dar a los diferentes combos del mantenimiento
	Row row = new Row();
	Row rowTemp = new Row();	
	Enumeration enumTemp= null;
	int hayInt = 0;	
	int haySec = 0;
	int hayAmb = 0;
	
	// Valores para combos setElement
	ArrayList vTipoProducto = new ArrayList(); // valor original TipoProducto
	ArrayList vProducto = new ArrayList(); // valor original Producto         
	ArrayList vInt = new ArrayList(); // valores originales formas pago internet
	ArrayList vSec = new ArrayList(); // valores originales formas pago secretaria
	ArrayList vIva = new ArrayList(); // valores originales iva
	ArrayList sufijoSel = new ArrayList(); // valores originales sufijo
	
	String estiloCombo = "boxCombo";
	boolean lectura = false;
	if (remitente.equalsIgnoreCase("consulta")) {
		estiloCombo = "boxConsulta";
		lectura = true;
	}

	double precio = 0.00;
	String sPrecio = null;

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if ((remitente.equalsIgnoreCase("modificar"))||(remitente.equalsIgnoreCase("consulta"))){
		enumTemp = ((Vector)request.getAttribute("container")).elements();
		// Entrada a mostrar o modificar
		if (enumTemp.hasMoreElements()){
          	row = (Row) enumTemp.nextElement(); 			              	
        } 
        
         
		//Precio
		sPrecio = row.getString(PysProductosInstitucionBean.C_VALOR);
		try { 
			if (sPrecio!=null)
				precio = Double.parseDouble(sPrecio);
		} catch(NumberFormatException e){
			precio = 0.00;
		}	         

		
		// Cargo valor IVA
        vIva.add(row.getString(PysProductosInstitucionBean.C_PORCENTAJEIVA));
        
        // Cargo el valor seleccionado del Sufijo:
        sufijoSel.add(row.getString(PysProductosInstitucionBean.C_SUFIJO));
        
		// Informacion sobre formas de pago internet
		enumTemp = ((Vector)request.getAttribute("container_I")).elements();
		if (enumTemp.hasMoreElements()){
          	hayInt = 1;
          	while(enumTemp.hasMoreElements()){		    			
              	rowTemp = (Row) enumTemp.nextElement(); 
				vInt.add(rowTemp.getString(PysFormaPagoProductoBean.C_IDFORMAPAGO));
          	}
        }
		// Informacion sobre formas de pago secretaria
		enumTemp = ((Vector)request.getAttribute("container_S")).elements();
		if (enumTemp.hasMoreElements()){		    			
          	haySec = 1;			              	
          	while(enumTemp.hasMoreElements()) {
              	rowTemp = (Row) enumTemp.nextElement(); 
				vSec.add(rowTemp.getString(PysFormaPagoProductoBean.C_IDFORMAPAGO));
          	}
        }  	
		// Informacion sobre ambas formas de pago
		enumTemp = ((Vector)request.getAttribute("container_A")).elements();
		if (enumTemp.hasMoreElements()){
          	hayAmb = 1;			              	
          	while(enumTemp.hasMoreElements()) {
              	rowTemp = (Row) enumTemp.nextElement(); 
				vInt.add(rowTemp.getString(PysFormaPagoProductoBean.C_IDFORMAPAGO));
				vSec.add(rowTemp.getString(PysFormaPagoProductoBean.C_IDFORMAPAGO));
          	}
        }  	
	}
	String	botones = "C,Y,R";
	String tipoCertificado = "";
	tipoCertificado = row.getString(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
	String TIPO_CERTIFICADO_CERTIFICADO      = PysProductosInstitucionAdm.TIPO_CERTIFICADO_CERTIFICADO;      // "C";
	String TIPO_CERTIFICADO_COMUNICACION     = PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION;     // "M";
	String TIPO_CERTIFICADO_DILIGENCIA       = PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA;       // "D";
	String TIPO_CERTIFICADO_COMISIONBANCARIA = PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMISIONBANCARIA; // "B";
	String TIPO_CERTIFICADO_GRATUITO         = PysProductosInstitucionAdm.TIPO_CERTIFICADO_GRATUITO;         // "G";

	String fechaBaja = row.getString(PysProductosInstitucionBean.C_FECHABAJA);
	if (fechaBaja!=null && fechaBaja.trim().equals("")) {
		fechaBaja = null;
	}

   //Parametro para la busqueda:
   String [] parametroCombo = {usrbean.getLocation()};
   String [] institucionParam = {usrbean.getLocation()};
   
   String delimitador = " " + (String)request.getAttribute("DELIMITADOR") + " ";
%>


<html>
<!-- HEAD -->
	<head>


		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoProductosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script language="JavaScript">
			function irA(opcion){

				var envio=1;	
							
				//Comprobar combo tipoProducto
				if (document.forms[0].cmbTipoProducto.value==""){
					alert("<siga:Idioma key="messages.pys.Tipo.error"/>");
					envio=-1;
				}				
				//Comprobar combo Producto
				if (document.forms[0].cmbProducto.value==""){
					alert("<siga:Idioma key="messages.pys.Producto.error"/>");
					envio=-1;
				}								
				//Comprobar nombre
				if (document.forms[0].nombre.value==""){
					alert("<siga:Idioma key="messages.pys.Nombre.error"/>");
					envio=-1;
				}				
				//Comprobar precio
				if (document.forms[0].precio.value==""){
					alert("<siga:Idioma key="messages.pys.Precio.error"/>");
					envio=-1;
				}				
				if (document.forms[0].precio.value<0){
					alert("<siga:Idioma key="messages.pys.PrecioValido.error"/>");
					envio=-1;
				}												
				if (isNaN(document.forms[0].precio.value)){
					alert("<siga:Idioma key="messages.pys.PrecioCaracteres.error"/>");
					envio=-1;
				}								
				//Comprobar IVA
				if (document.forms[0].iva.value==""){
					alert("<siga:Idioma key="messages.pys.Iva.error"/>");
					envio=-1;
				}				
				if ((document.forms[0].iva.value>100)||(document.forms[0].iva.value<0)){
					alert("<siga:Idioma key="messages.pys.IvaValor.error"/>");
					envio=-1;
				}								
				if (isNaN(document.forms[0].iva.value)){
					alert("<siga:Idioma key="messages.pys.IvaAplicado.error"/>");
					envio=-1;
				}															
				//Comprobar seleccion forma de pago cmbFormaPagoInternet y cmbFormaPagoSecretaria
				if ((document.forms[0].cmbFormaPagoInternet.value=="")&&(document.forms[0].cmbFormaPagoSecretaria.value=="")){
					alert("<siga:Idioma key="messages.pys.pago.error"/>");
					envio=-1;
				}								
				if (envio==1){
					document.forms[0].modo.value=opcion;
					document.forms[0].submit();
				}	
				
			}	

			function validacionPosterior(){

				var envio=true;	
				var i;
				var mensaje="";

				//Comprobar existencia de producto
				if (document.forms[0].producto.value==""){
					mensaje+='<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.categoria"/> <siga:Idioma key="messages.campoObligatorio.error"/>\n';
					envio=false;
				}

				//Comprobar relacion momento de cargo al solicitarlo y forma de pago seleccionada
				if(!document.forms[0].noFacturable.checked){
					if (document.forms[0].cargo[0].checked){
						i=0;
						while ((envio)&&(i<document.forms[0].formaPagoInternet.length)){
							//if ((document.forms[0].formaPagoInternet[i].selected)&&((document.forms[0].formaPagoInternet[i].value!="10")&&(document.forms[0].formaPagoInternet[i].value!=""))) {
							if ((document.forms[0].formaPagoInternet[i].selected)&&(document.forms[0].formaPagoInternet[i].value=="20")) {
								mensaje+='<siga:Idioma key="messages.pys.momentoPago.error"/>\n';
								//mensaje+="cargo.messages.pys.momentoPago.error";
								envio=false;
							}
							i++;
						}
						j=0;
						while ((envio)&&(j<document.forms[0].formaPagoSecretaria.length)){
							//if ((document.forms[0].formaPagoInternet[i].selected)&&((document.forms[0].formaPagoInternet[i].value!="10")&&(document.forms[0].formaPagoInternet[i].value!=""))) {
							if ((document.forms[0].formaPagoSecretaria[j].selected)&&(document.forms[0].formaPagoSecretaria[j].value=="20")) {
								mensaje+='<siga:Idioma key="messages.pys.momentoPago.error"/>\n';
								//mensaje+="cargo.messages.pys.momentoPago.error";
								envio=false;
							}
							j++;
						}
					}
				}
				
				//Comprobar relacion momento de cargo proxima factura y forma de pago seleccionada
				if(!document.forms[0].noFacturable.checked){
					if (document.forms[0].cargo[1].checked){
						i=0;
						while ((envio)&&(i<document.forms[0].formaPagoInternet.length)){
							//if ((document.forms[0].formaPagoInternet[i].selected)&&((document.forms[0].formaPagoInternet[i].value!="10")&&(document.forms[0].formaPagoInternet[i].value!=""))) {
							if ((document.forms[0].formaPagoInternet[i].selected)&&(document.forms[0].formaPagoInternet[i].value=="10")) {
								mensaje+='<siga:Idioma key="messages.pys.momentoPago.error"/>\n';
								//mensaje+="cargo.messages.pys.momentoPago.error";
								envio=false;
							}
							i++;
						}
						j=0;
						while ((envio)&&(j<document.forms[0].formaPagoSecretaria.length)){
							//if ((document.forms[0].formaPagoInternet[i].selected)&&((document.forms[0].formaPagoInternet[i].value!="10")&&(document.forms[0].formaPagoInternet[i].value!=""))) {
							if ((document.forms[0].formaPagoSecretaria[j].selected)&&(document.forms[0].formaPagoSecretaria[j].value=="10")) {
								mensaje+='<siga:Idioma key="messages.pys.momentoPago.error"/>\n';
								//mensaje+="cargo.messages.pys.momentoPago.error";
								envio=false;
							}
							j++;
						}
					}
				}
				
				//Comprobar relacion solicitar alta por Internet y forma de pago internet
				if(!document.forms[0].noFacturable.checked){
					if ((document.forms[0].altaInternet.checked==1)&&(document.forms[0].formaPagoInternet.value=="")){
						mensaje+='<siga:Idioma key="messages.pys.altaInternet.error"/>\n';
						//mensaje+="messages.pys.altaInternet.error";
						envio=false;
					}
				}
				
				//Comprobar existencia de un al menos una forma de pago
				//if ((document.forms[0].formaPagoInternet.value=="")&&(document.forms[0].formaPagoSecretaria.value=="")){
				if(!document.forms[0].noFacturable.checked){
					if (document.forms[0].formaPagoSecretaria.value==""){
						mensaje+='<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.formaPago"/> <siga:Idioma key="productos.mantenimientoProductos.literal.secretaria"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
						envio=false;
					}
				}

				if (!envio){
					alert(mensaje);
				}
				
				return envio;
				
			}	
			
			function cargaCombo(modo)
			{
				<% if (remitente.equalsIgnoreCase("modificar")){%>
					document.getElementById("tipoProducto").onchange();
				<% } %>	
			}							

			var momentoCargoInicial, resetComboPagoInternet, resetComboPagoSecretaria;

			function limpiarCombo(nombre) 
			{
				iframeCombo = top.frames[0].document.getElementById (nombre + "Frame");
				cadenaInicial = iframeCombo.src;
				
				if (cadenaInicial.indexOf("&elementoSel=[0]") > 1)  {
					return;
				}
				
				var ini = cadenaInicial.indexOf('&elementoSel=');
				if (ini < 1) 
					return;
				
				cadenaFinal = cadenaInicial.substring(0,ini) + "&elementoSel=[0]";
				
				var fin = cadenaInicial.indexOf('&', ini+2);
				if (fin > 1) {	
					cadenaFinal = cadenaFinal + cadenaInicial.substring(fin);
				}

				iframeCombo.src = cadenaFinal;
			}
	
			function cambiarCombosMomentoPago (o) 
			{
				combo = document.getElementById("momentoCargo");
				if (o.value == 'S' && o.checked) {
					combo.options(0).selected = true;   // Al solicitarlo
				}
				else {
					combo.options(1).selected = true;	// Proximo periodo
				}
				combo.onchange();

				if (momentoCargoInicial != o.value ) {
					limpiarCombo("formaPagoInternet");
					limpiarCombo("formaPagoSecretaria");
				}
			}
			
			function inicio (o) {
				combo = document.getElementById("momentoCargo");
				if (o.value == 'S' && o.checked) {
					combo.options(0).selected = true;   // Al solicitarlo
					momentoCargoInicial = "S";
				}
				else {
					combo.options(1).selected = true;	// Proximo periodo
					momentoCargoInicial = "P";
				}
				combo.onchange();

				resetComboPagoInternet = document.getElementById ("formaPagoInternetFrame").src;
				resetComboPagoSecretaria = document.getElementById ("formaPagoSecretariaFrame").src;

				 var cont=document.getElementById("contador");
				// Mostramos el valor del contador cuando está relleno el combo Tipo Certificado 
				<% if (tipoCertificado!=null && !tipoCertificado.equals("") && !tipoCertificado.equals("B")){%>
				    cont.style.display="block";
				<%}else{%>
				    cont.style.display="none"; 
				<%}%>
			}
			
		</script>		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body onload="inicio(document.getElementById('cargo'))">

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="pys.busquedaProductos.cabecera"/>
				</td>
			</tr>
		</table>
		
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalGrande" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->
			<table  class="tablaCentralCamposGrande" align="center"><tr><td>
				<siga:ConjCampos leyenda="pys.busquedaProductos.cabecera">					
				 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<html:form action="/PYS_MantenimientoProductos.do" method="POST" target="submitArea">
						<html:hidden property="modo" value=""/>					
						<html:hidden property="idContador" value="<%=row.getString(PysProductosInstitucionBean.C_IDCONTADOR)%>"/>					
							<html:hidden property="idProdInst" value="<%=row.getString(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION)%>"/>&nbsp;
							<tr>							
								<td width="19%" class="labelText">
									<siga:Idioma key="pys.resultadoBusquedaProductos.literal.tipo"/>&nbsp;(*)
								</td>
								<td width="29%" class="labelText">
									<% if (remitente=="insertar"){%>
										<siga:ComboBD nombre = "tipoProducto" tipo="cmbTipoProducto" ancho="200" clase="boxCombo" obligatorio="true" accion="Hijo:producto" ancho="300"/>
									<% }else{ %>
										<% vTipoProducto.add(row.getString(PysProductosInstitucionBean.C_IDTIPOPRODUCTO)); %>
										<% if (remitente=="modificar"){ %>
											<html:hidden property="producto" value="<%=row.getString(PysProductosInstitucionBean.C_IDPRODUCTO)%>"/>
											<siga:ComboBD nombre = "tipoProducto"   tipo="cmbTipoProducto" clase="boxConsulta" obligatorio="true" elementoSel="<%=vTipoProducto%>" accion="Hijo:producto" readOnly="true" ancho="300"/>
										<%}else{%>
											<siga:ComboBD nombre = "tipoProducto"  tipo="cmbTipoProducto" clase="boxConsulta" obligatorio="true" elementoSel="<%=vTipoProducto%>" accion="Hijo:producto" readOnly="true" ancho="300"/>
										<% } %>	
									<% } %>
								</td>
								<td width="18%" class="labelText">
									<siga:Idioma key="pys.resultadoBusquedaProductos.literal.precio"/>&nbsp;(*)
								</td>
								<td width="29%" class="labelText"> 
									<% if (remitente=="insertar"){%>
							  			<html:text property="precio" styleClass="boxNumber" size="10" maxlength="11" value=""></html:text>&nbsp;&euro;
							  		<% } else { %>
										<% if (remitente=="modificar"){ %>
											<html:text property="precio" styleClass="boxNumber" size="10" maxlength="11" value="<%=UtilidadesNumero.formatoCampo(precio)%>"></html:text>&nbsp;&euro;
										<%}else{%>
											<html:text property="precio" styleClass="boxConsultaNumber" size="10" value="<%=UtilidadesNumero.formatoCampo(precio)%>" readOnly="true"></html:text>&nbsp;&euro;
										<% } %>								  		
							  	<% } %>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.categoria"/>&nbsp;(*)
								</td>
								<td class="labelText">										
									<% if (remitente=="insertar"){%>
										<siga:ComboBD nombre = "producto" tipo="cmbProducto"  parametro="<%=parametroCombo%>" clase="boxCombo" obligatorio="true" hijo="t" ancho="300"/>
									<% } else { %>
										<% vProducto.add(row.getString(PysProductosInstitucionBean.C_IDPRODUCTO)); %>
										<% if (remitente=="modificar"){ %>
											<html:text property="productoC" styleClass="boxConsulta"  style="width:300"  value='<%=row.getString("CATEGORIA")%>' readOnly="true"></html:text>
										<%}else{%>
											<html:text property="productoC" styleClass="boxConsulta"  style="width:300" value='<%=row.getString("CATEGORIA")%>' readOnly="true"></html:text>
										<% } %>											
									<% } %>
								</td>
								<td class="labelText">
									<siga:Idioma key="pys.resultadoBusquedaProductos.literal.IVA"/>&nbsp;(*)
								</td>
								<td class="labelText"> 					
									<% if (remitente=="insertar"){%>
										<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="boxCombo" obligatorio="true"/>
							  		<% } else { %>
										<% if (remitente=="modificar"){ %>
											<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="box" elementoSel="<%=vIva%>" obligatorio="true"/>
										<%}else{%>
											<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="boxConsulta" elementoSel="<%=vIva%>" obligatorio="true" readOnly="true"/>
										<% } %>
							  		<% } %>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.producto"/>&nbsp;(*)
								</td>
								<td class="labelText"> 					
									<% if (remitente=="insertar"){%>
							  			<html:textarea property="nombre" styleClass="box" cols="25" rows="4" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)"/>
							  		<% } else { %>
										<% if (remitente=="modificar"){ %>
											<html:textarea property="nombre" styleClass="box" cols="25" rows="4" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" value="<%=row.getString(PysProductosInstitucionBean.C_DESCRIPCION)%>"/>
										<%}else{%>
											<html:textarea property="nombre" styleClass="boxConsulta" cols="25" rows="4"  maxlength="100" value="<%=row.getString(PysProductosInstitucionBean.C_DESCRIPCION)%>" readOnly="true"/>
										<% } %>
							  	<% } %>
							  	</td>	
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.cuenta"/>&nbsp;&nbsp;
									</td>
								<td class="labelText"> 					
									<% if (remitente=="insertar"){%>
							  			<html:text property="cuentaContable" styleClass="box" size="20"  maxlength="20" value=""></html:text>
							  		<% } else { %>
										<% if (remitente=="modificar"){ %>
											<html:text property="cuentaContable" styleClass="box" size="20"  maxlength="20"  value="<%=row.getString(PysProductosInstitucionBean.C_CUENTACONTABLE)%>"></html:text>
										<%}else{%>
											<html:text property="cuentaContable" styleClass="boxConsulta"  maxlength="20" size="20" value="<%=row.getString(PysProductosInstitucionBean.C_CUENTACONTABLE)%>" readOnly="true"></html:text>
										<% } %>							  						  		
							  	<% } %>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									Identificador&nbsp;&nbsp;
								</td>
								<td class="labelTextValue" title="Categoria:Tipo:Producto">
									<% if (!remitente.equalsIgnoreCase("insertar")){%>
										<%=row.getString(PysProductosInstitucionBean.C_IDTIPOPRODUCTO) + delimitador + row.getString(PysProductosInstitucionBean.C_IDPRODUCTO) + delimitador +row.getString(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION)%>
									<%} %>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="certificados.mantenimiento.literal.tipoCertificado"/>&nbsp;&nbsp;
								</td>
								<td class="labelText">
									<% if (remitente=="insertar"){%>
										<html:select name="MantenimientoProductosForm" property="tipoCertificado" styleClass="boxCombo" value="">
											<html:option value=""></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_CERTIFICADO  %>"> <siga:Idioma key="certificados.tipocertificado.literal.certificado"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION %>"> <siga:Idioma key="certificados.tipocertificado.literal.comunicacion"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA   %>"> <siga:Idioma key="certificados.tipocertificado.literal.diligencia"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_GRATUITO      %>"> <siga:Idioma key="certificados.tipocertificado.literal.gratuito"/></html:option>
										</html:select>											
									<% } else { %>									
									<% tipoCertificado = row.getString(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
											if (remitente=="modificar"){ %>
										<html:select name="MantenimientoProductosForm" property="tipoCertificado" styleClass="boxCombo" value="<%=tipoCertificado%>">
											<html:option value=""></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_CERTIFICADO  %>"> <siga:Idioma key="certificados.tipocertificado.literal.certificado"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION %>"> <siga:Idioma key="certificados.tipocertificado.literal.comunicacion"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA   %>"> <siga:Idioma key="certificados.tipocertificado.literal.diligencia"/></html:option>
											<html:option value="<%=PysProductosInstitucionAdm.TIPO_CERTIFICADO_GRATUITO      %>"> <siga:Idioma key="certificados.tipocertificado.literal.gratuito"/></html:option>
										</html:select>	
										<%}else{ %>
										<div class="boxConsulta">	
											<% if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_CERTIFICADO)){%>
												<siga:Idioma key="certificados.tipocertificado.literal.certificado"/>
											<%}else if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_COMUNICACION)){%>
												<siga:Idioma key="certificados.tipocertificado.literal.comunicacion"/>
											<%}else if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_DILIGENCIA)){%>
												<siga:Idioma key="certificados.tipocertificado.literal.diligencia"/>
											<%}else if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_GRATUITO)){%>
												<siga:Idioma key="certificados.tipocertificado.literal.gratuito"/>
											<%}%>							
										</div>										 		
										<% } %>											
									<% } %>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.sufijos.literal.concepto"/>
								</td>
								<td class="labelText">
									<siga:ComboBD nombre = "sufijo" tipo="cmbSufijos" ancho="175" clase="<%=estiloCombo%>" elementoSel="<%=sufijoSel%>" obligatorio="false" readonly="<%=String.valueOf(lectura)%>" parametro="<%=institucionParam %>" />
								</td>									
							</tr>	
							<tr>
							<!-- INICIO: FILA DE LOS CHECKBOX-->
								<td class="labelText" colspan="2">
									<!-- ALTA -->
									<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.alta"/>
									&nbsp;
									<% if (remitente=="insertar"){%>
										<input type="checkbox" name="altaInternet" value="1" style="margin-left: -5px;">
							  		<% } else { %>
											<% if (remitente=="modificar"){ %>
								  				<% if (row.getString(PysProductosInstitucionBean.C_SOLICITARALTA).equals(ClsConstants.DB_TRUE)){%>	
								  					<input type="checkbox" name="altaInternet" value="1" checked  style="margin-left: -5px;">
								  				<% } else { %>			  		
													<input type="checkbox" name="altaInternet" value="1"  style="margin-left: -5px;">
								  				<% } %>
								  			<% }else{ %>
								  				<% if (row.getString(PysProductosInstitucionBean.C_SOLICITARALTA).equals(ClsConstants.DB_TRUE)){%>	
								  					<input type="checkbox" name="altaInternet" value="1" checked disabled >
								  				<% } else { %>			  		
													<input type="checkbox" name="altaInternet" value="1" disabled >
								  				<% } %>
								  			<% } %>
							  		<% } %>		

							  		<!-- BAJA -->
									<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.baja"/>
									&nbsp;
									<% if (remitente=="insertar"){%>
										<input type="checkbox" name="bajaInternet" value="1"  style="margin-left: -5px;">								
							  		<% } else { %>
											<% if (remitente=="modificar"){ %>
								  				<% if (row.getString(PysProductosInstitucionBean.C_SOLICITARBAJA).equals(ClsConstants.DB_TRUE)){%>	
								  					<input type="checkbox" name="bajaInternet"  value="1" checked  style="margin-left: -5px;">
								  				<% } else { %>			  		
													<input type="checkbox" name="bajaInternet" value="1"  style="margin-left: -5px;">
								  				<% } %>
								  			<% }else{ %>
								  				<% if (row.getString(PysProductosInstitucionBean.C_SOLICITARBAJA).equals(ClsConstants.DB_TRUE)){%>	
								  					<input type="checkbox" name="bajaInternet"  value="1" checked disabled >
								  				<% } else { %>			  		
													<input type="checkbox" name="bajaInternet"  value="1" disabled >
								  				<% } %>
								  			<% } %>						  				
							  		<% } %>	

							 </td>
							 <td class="labelText" colspan="2">
							 
								<!-- COMISION BANCARIA -->
								<siga:Idioma key="certificados.tipocertificado.literal.comisionBancaria"/>
								&nbsp;							
								<% if (remitente=="insertar"){%>
										<input type="checkbox" name="tipoCertificadoComision" value="1"  style="margin-left: -5px;">								
							  		<% } else { %>
											<% tipoCertificado = row.getString(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
											   if (remitente=="modificar"){	 %>
								  				<% 
												if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_COMISIONBANCARIA)){%>	
								  					<input type="checkbox" name="tipoCertificadoComision"  value="1" checked  style="margin-left: -5px;">
								  				<% } else { %>			  		
													<input type="checkbox" name="tipoCertificadoComision" value="1"  style="margin-left: -5px;">
								  				<% } %>
								  			<% }else{ %>
								  				<% if(tipoCertificado.equalsIgnoreCase(TIPO_CERTIFICADO_COMISIONBANCARIA)){%>	
								  					<input type="checkbox" name="tipoCertificadoComision"  value="1" checked disabled >
								  				<% } else { %>			  		
													<input type="checkbox" name="tipoCertificadoComision"  value="1" disabled >
								  				<% } %>
								  			<% } %>						  				
							  		<% } %>							
							
							
									
								
								<!-- NO FACTURABLE -->
								<siga:Idioma key="pys.mantenimientoProductos.literal.noFacturable"/> 
								&nbsp;	

								<% if (remitente=="insertar"){%>
						  			<input type="checkbox" name="noFacturable"  value="1"  style="margin-left: -5px;">
							  	<% } 
							  	else { 
							  			String nofacturable=row.getString(PysProductosInstitucionBean.C_NOFACTURABLE);
							  			if (remitente=="modificar"){ %>
							  				<%if(nofacturable.equals(ClsConstants.DB_TRUE)){%>	
												<input type="checkbox" name="noFacturable"  value="1" checked style="margin-left: -5px;">
											<%}else{%>
												<input type="checkbox" name="noFacturable"  value="1" style="margin-left: -5px;">
											<% } %>	
										<%}else{%>
											<%if(nofacturable.equals(ClsConstants.DB_TRUE)){%>	
												<input type="checkbox" name="noFacturable"  value="1" checked disabled >
											<%}else{%>
												<input type="checkbox" name="noFacturable"  value="1" disabled >
											<% } %>	
										<% } %>
							  	<%  } %>
							
							
								</td>		
												
							<!-- FIN: FILA DE LOS CHECKBOX-->
							</tr>				
							
							<tr id="contador">
							 <td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.contador"/>
							 </td>
							 <td class="labelText" colspan="3"><html:text property="cuentaContable" styleClass="boxConsulta" size="20"  maxlength="20" value="<%=row.getString(PysProductosInstitucionBean.C_IDCONTADOR)%>"></html:text></td>
							</tr>					
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="5%" class="labelText">&nbsp;</td>
								<td width="45%" valign="top">
												<siga:ConjCampos leyenda="pys.mantenimientoBusquedaProductos.literal.cargo">					
													<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
														<tr>
															<td class="labelText">

															<siga:ComboBD nombre = "momentoCargo" tipo="momentoCargo" obligatorioSinTextoSeleccionar="true" parametro="<%=valorMomentoCargo%>" accion="Hijo:formaPagoSecretaria,Hijo:formaPagoInternet" estilo="display:none"/>

															<% if (remitente=="insertar"){%>
																<input type="radio" name="cargo" value="S" checked onclick="cambiarCombosMomentoPago(this);" ><siga:Idioma key="productos.mantenimientoProductos.literal.alSolicitarlo"/>
							  								<% } else { 
							  										if (row.getString(PysProductosInstitucionBean.C_MOMENTOCARGO).equals(ClsConstants.MOMENTO_CARGO_SOLICITUD)){%>	
																		<% if (remitente=="modificar"){ %>
																			<input type="radio" name="cargo" value="S" checked onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.alSolicitarlo"/>
																		<%}else{%>
																			<input type="radio" name="cargo" value="S" checked disabled onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.alSolicitarlo"/>
																		<% } %>
							  										<% } else { %>
																		<% if (remitente=="modificar"){ %>
																			<input type="radio" name="cargo" value="S" onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.alSolicitarlo"/>
																		<%}else{%>
																			<input type="radio" name="cargo" value="S" disabled onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.alSolicitarlo"/>
																		<% } %>							  																												
							  										<% } %>
							  								<% } %>																											
															</td>
														</tr>
														<tr>
															<td class="labelText">
																<% if (remitente=="insertar"){%>
																	<input type="radio" name="cargo" value="P" onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.proximoPeriodo"/></td>
								  								<% } else { 
								  										if (row.getString(PysProductosInstitucionBean.C_MOMENTOCARGO).equals(ClsConstants.MOMENTO_CARGO_PROXFACTURA)){%>
																			<% if (remitente=="modificar"){ %>
																				<input type="radio" name="cargo" value="P" checked onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.proximoPeriodo"/>
																			<%}else{%>
																				<input type="radio" name="cargo" value="P" checked disabled onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.proximoPeriodo"/>
																			<% } %>
								  										<% } else { %>
																			<% if (remitente=="modificar"){ %>
																				<input type="radio" name="cargo" value="P" onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.proximoPeriodo"/>
																			<%}else{%>
																				<input type="radio" name="cargo" value="P" disabled onclick="cambiarCombosMomentoPago(this);"><siga:Idioma key="productos.mantenimientoProductos.literal.proximoPeriodo"/>
																			<% } %>																		
								  										<% } %>
								  								<% } %>
															</td>
														</tr>
													</table>
												</siga:ConjCampos>
												<siga:ConjCampos leyenda="productos.mantenimientoProductos.literal.estado">					
													<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
														<tr>
															<td class="labelText" width="30%">
																<siga:Idioma key="productos.mantenimientoProductos.literal.bajaLogica"/>
															</td>
															<td class="labelText" align="left">
																<% if (remitente=="insertar") {	%>
																	<input type="checkbox" name="bajaLogica" value="1" disabled>
																<% } else if (remitente=="modificar") {
																		if (fechaBaja!=null) {%>
																		<input type="checkbox" name="bajaLogica" value="1" checked>
																<% 		} else { %>
																		<input type="checkbox" name="bajaLogica" value="1">
																<%		}
																   } else { 
																		if (fechaBaja!=null) {%>
																		<input type="checkbox" name="bajaLogica" value="1" checked disabled>
																<% 		} else { %>
																		<input type="checkbox" name="bajaLogica" value="1" disabled>
																<% 		}
																   } 
																%>
															</td>
														</tr>
													</table>
												</siga:ConjCampos>
								</td>
								<td width="45%">
									<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>&nbsp;</td>
											<td valign="top">
									<siga:ConjCampos leyenda="pys.mantenimientoBusquedaProductos.literal.formaPago">					
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
									  		<tr>
												<td width="20%" class="labelText" align="left">
													<siga:Idioma key="productos.mantenimientoProductos.literal.internet"/>&nbsp;&nbsp;
												</td>
												<td width="80%" class="labelText" align="left">
													<% if (remitente=="insertar"){int i=0;%>
														<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternetProductos" clase="boxCombo" filasMostrar="2" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" 	hijo="t"/>
													<%} else { %> 
														<%	if (vInt.isEmpty()){int i=0;%>
															<% if (remitente=="modificar"){ %>
																<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternetProductos" clase="box" filasMostrar="2" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" 	hijo="t"/>
															<%}else{%>
																<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternetProductos" clase="boxConsulta" filasMostrar="2" seleccionMultiple="true" obligatorio="true" hijo="t" readOnly="true"/>
															<% } %>																
														<% } else { %>
															<% if (remitente=="modificar"){ %>
																<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternetProductos" clase="box" filasMostrar="2" seleccionMultiple="true" elementoSel="<%=vInt%>" obligatorio="true" hijo="t" />
															<%}else{%>
																<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternetProductos" clase="boxConsulta" filasMostrar="2" seleccionMultiple="true" elementoSel="<%=vInt%>" obligatorio="true" hijo="t" readOnly="true"/>
															<% } %>																																		
														<% } %>
													<% } %>
												</td>
												<td>&nbsp;</td>
									  		</tr>
										  	<tr>
												<td width="20%" class="labelText" align="left">
													<siga:Idioma key="productos.mantenimientoProductos.literal.secretaria"/>&nbsp;(*)
												</td>
												<td width="80%" class="labelText" align="left">
																<% if (remitente=="insertar"){int i=0;%>
																	<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretariaProductos" clase="boxCombo" filasMostrar="7" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" hijo="t" />
																<% } else { %>													
																	<% if (vSec.isEmpty()){int i=0;%>
																		<% if (remitente=="modificar"){ %>
																			<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretariaProductos" clase="box" filasMostrar="7" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" hijo="t"/>
																		<%}else{%>
																			<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretariaProductos" clase="boxConsulta" filasMostrar="7" seleccionMultiple="true" obligatorio="true" hijo="t" readOnly="true"/>
																		<% } %>																																			
																	<% } else { 
																	
																		ArrayList elementoSel = new ArrayList();
																		for (int kk = 0; kk < vSec.size(); kk ++) {
																			elementoSel.add(vSec.get(kk));	
																		}
																		%>
																		<% if (remitente=="modificar"){ %>
																			<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretariaProductos" clase="box" filasMostrar="7" seleccionMultiple="true" elementoSel="<%=elementoSel%>" obligatorio="true" hijo="t"/>
																		<%}else{%>
																			<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretariaProductos" clase="boxConsulta" filasMostrar="7" seleccionMultiple="true" elementoSel="<%=elementoSel%>" obligatorio="true" hijo="t" readOnly="true"/>
																		<% } %>																			
																	<% } %>																													
																<% } %>																																				
												</td>
										  	</tr>	
										</table>
									</siga:ConjCampos>					
									<br>
											</td>
										</tr>
									</table>
								</td>	
								<td width="5%" class="labelText">
									&nbsp;
								</td>				
							</tr>
						</html:form>
						</table>	
				</siga:ConjCampos>	
			</table>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=remitente%>'  modal="G"/>
			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">


			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{	
				sub();				
				if (validateMantenimientoProductosForm(document.MantenimientoProductosForm)){
					if (validacionPosterior()){				
						<% if (remitente.equalsIgnoreCase("modificar")){ %>
							document.forms[0].modo.value="modificar";
						<% }else{ %>
							document.forms[0].modo.value="insertar";
						<% } %>	
						document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
						if (document.forms[0].noFacturable.checked){
						  document.forms[0].noFacturable.value="1";
						}else{
						   document.forms[0].noFacturable.value="0";
						}
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}	
				}else{
					fin();
					return false;
				}	
			}

			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.getElementById ("formaPagoInternetFrame").src = resetComboPagoInternet;
				document.getElementById ("formaPagoSecretariaFrame").src = resetComboPagoSecretaria;
				document.forms[0].reset();
			}

			</script>
			<!-- FIN: SCRIPTS BOTONES -->
	
			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

		</div>
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>