<!DOCTYPE html>
<html>
<head>
<!-- mantenimientoServicios.jsp -->

<!-- 
	 Permite mostrar/editar los distintos productos
	 VERSIONES:
	 miguel.villegas 2-02-2005 actualizacion a adecuacion a los nuevos aires
 	 david.sanchezp / 27-10-2005: anhadido el combo concepto y maquetado.
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.productos.form.MantenimientoServiciosForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<%  
	HttpSession ses=request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
	
	String modo = request.getAttribute("modelo").toString(); // Obtengo la operacion (consulta,modificar o insertar)a realizar

	// Valores que debere tener en cuenta para los pagos por internet y los pagos por secretaria
	String valorInternet[] = {ClsConstants.TIPO_PAGO_INTERNET,ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA};   // Valores de Internet y Sercretaria Internet
	String valorSecretaria[] = {ClsConstants.TIPO_PAGO_SECRETARIA,ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA}; // Valores de Secretaria y Sercretaria Internet

	String valorModo = "";

	// declaraciones y gestion de la informacion recogida y de los valores a dar a los diferentes combos del mantenimiento
	Row row = new Row();
	Row rowTemp = new Row();	
	Enumeration enumTemp= null;
	int hayInt = 0;	
	int haySec = 0;
	int hayAmb = 0;      

	String estiloCombo = "boxCombo";
	boolean lectura = false;
	if (modo.equalsIgnoreCase("consulta")) {
		estiloCombo = "boxConsulta";
		lectura = true;
	}

	// Valores para combos setElement
	ArrayList vTipoServicios = new ArrayList(); // valor original TipoProducto
	ArrayList vServicios = new ArrayList(); // valor original Producto         
	ArrayList vInt = new ArrayList(); // valores originales formas pago internet
	ArrayList vSec = new ArrayList(); // valores originales formas pago secretaria
	ArrayList vIva = new ArrayList(); // valores originales iva
	Vector precios = new Vector(); // precios asociados
	ArrayList sufijoSel = new ArrayList(); // valores originales sufijo

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if ((modo.equalsIgnoreCase("modificar"))||(modo.equalsIgnoreCase("consulta"))){
		enumTemp = ((Vector)request.getAttribute("container")).elements();
		// Entrada a mostrar o modificar
		if (enumTemp.hasMoreElements()){
          	row = (Row) enumTemp.nextElement(); 			              	
        }  	        
		
		// cambioSituacion valor IVA
        vIva.add(row.getString(PysServiciosInstitucionBean.C_PORCENTAJEIVA));
        
        // Cargo el valor seleccionado del Sufijo:
        sufijoSel.add(row.getString(PysServiciosInstitucionBean.C_SUFIJO));
        
		// Informacion sobre formas de pago internet
		enumTemp = ((Vector)request.getAttribute("container_I")).elements();
		if (enumTemp.hasMoreElements()){
          	hayInt = 1;
          	while(enumTemp.hasMoreElements()){		    			
              	rowTemp = (Row) enumTemp.nextElement(); 
				vInt.add(rowTemp.getString(PysFormaPagoServiciosBean.C_IDFORMAPAGO));				              	
          	}
        }
		// Informacion sobre formas de pago secretaria
		enumTemp = ((Vector)request.getAttribute("container_S")).elements();
		if (enumTemp.hasMoreElements()){		    			
          	haySec = 1;			              	
          	while(enumTemp.hasMoreElements()){		    			
              	rowTemp = (Row) enumTemp.nextElement(); 
				vSec.add(rowTemp.getString(PysFormaPagoServiciosBean.C_IDFORMAPAGO));				              	
          	}			              	
        }  	
		// Informacion sobre ambas formas de pago
		enumTemp = ((Vector)request.getAttribute("container_A")).elements();
		if (enumTemp.hasMoreElements()){
          	hayAmb = 1;			              	
          	while(enumTemp.hasMoreElements()){		    			
              	rowTemp = (Row) enumTemp.nextElement(); 
				vInt.add(rowTemp.getString(PysFormaPagoServiciosBean.C_IDFORMAPAGO));				              	
				vSec.add(rowTemp.getString(PysFormaPagoServiciosBean.C_IDFORMAPAGO));				              	
          	}			              	
        }  	
	}	
	
	String	botones="G,R";
	String	botonesPrecio="N,C";	
	boolean bFechaBaja= true;
	boolean bAutomatico= false;

	String fechaBaja = row.getString(PysServiciosInstitucionBean.C_FECHABAJA);
	String noPondera = row.getString(PysServiciosInstitucionBean.C_FACTURACIONPONDERADA);
	if (fechaBaja!=null && fechaBaja.trim().equals("")) {
		fechaBaja = null;
		bFechaBaja = false;
	}
	
	if (row.getString(PysServiciosInstitucionBean.C_AUTOMATICO)!=null && row.getString(PysServiciosInstitucionBean.C_AUTOMATICO).equals(ClsConstants.DB_TRUE)) {
		bAutomatico=true;
	}
	
   //Parametro para la busqueda:
   String [] parametroCombo = {usr.getLocation()};
   String [] institucionParam = {usr.getLocation()};

   String  checkPonderar="";
   if (modo=="consulta"){ 
    	checkPonderar="disabled";
   }
%>



	<!-- HEAD -->
			
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  		
		
		<script language="JavaScript">

			var resultadoVentanaCondicion = "";		
		
			function validacionPosterior() {
				var envio=true;	
				var i;
				var mensaje="";

				//Comprobar categoria (oculto por ser combo hijo)
				if (document.forms[0].servicio.value==""){
					mensaje+='<siga:Idioma key="pys.mantenimientoServicios.literal.categoria"/> <siga:Idioma key="messages.campoObligatorio.error"/>\n';
					envio=false;
				}
				
				//Comprobar relacion solicitar alta por Internet y forma de pago internet
				if ((document.forms[0].altaInternet.checked==1)&&(document.forms[0].formaPagoInternet.value=="")){
					mensaje+='<siga:Idioma key="messages.pys.altaInternet.error"/>\n';
					envio=false;
				}

				//Comprobar existencia de un al menos una forma de pago
				if (document.forms[0].formaPagoSecretaria.value==""){
					mensaje+='<siga:Idioma key="pys.mantenimientoBusquedaProductos.literal.formaPago"/> <siga:Idioma key="productos.mantenimientoProductos.literal.secretaria"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					envio=false;
				}

				//LMS 04/09/2006
				//Si se ha marcado como automático y como única forma de pago domiciliación bancaria, se muestra un mensaje de aviso.
				if (document.forms[0].automatico.checked){
					var iii;
					var jjj=0;

					for (iii=0; iii<document.forms[0].formaPagoSecretaria.options.length; iii++){
						if(document.forms[0].formaPagoSecretaria.options[iii].selected){
							jjj++;
						}
					}

					if (jjj==1 && document.forms[0].formaPagoSecretaria.value==<%=ClsConstants.TIPO_FORMAPAGO_FACTURA%>){
						//alert('<siga:Idioma key="pys.mantenimientoServicios.mensaje.automaticoDomiciliacionBancaria"/>');
					  	men = '<siga:Idioma key="pys.mantenimientoServicios.mensaje.automaticoDomiciliacionBancaria"/>';
    					if (!confirm(men)) {	
						 	return;
						}
					}
				}			
				
				if (!envio){
					alert(mensaje);
				}
				
				return envio;				
			}	
			
			function refrescarLocal(){
					document.MantenimientoServiciosForm.target="modal";
					document.MantenimientoServiciosForm.refresco.value="refresco";
					document.MantenimientoServiciosForm.modo.value="editar";
					document.MantenimientoServiciosForm.submit();
			}							

			function nuevaCondicionSuscripcionAutomatica(){		
					document.forms[0].comprobarCondicion.value = document.forms[0].automatico.checked;
					document.forms[0].modo.value = 'condicionSuscripcionAutomatica' + '<%=modo%>';
					document.forms[0].target='submitArea';
					resultadoVentanaCondicion = ventaModalGeneral(document.forms[0].name, "G");
					return;
//					resultadoVentanaCondicion = resultadoVentanaCondicion;
//					alert (resultadoVentanaCondicion);
			}
			
			function comprobarSiExisteCondicion() {		
				<% 
					String idConsulta = (String) request.getAttribute("idConsulta");
					if ((idConsulta != null) && (!idConsulta.equals(""))) {	
						
					} else {
				%>
					if (resultadoVentanaCondicion != "SI_HAY_CONDICION") {
						if (document.forms[0].automatico.checked) {
							alert("<siga:Idioma key="pys.mantenimientoServicios.suscripcionAutomatica.mensaje.errorSinCondicion"/>");
							document.forms[0].automatico.checked = false;
							return;
						}
					}
				<%}%>
			}
			
			function eliminarSuscripcionAutomatica() {
			  document.forms[0].modo.value = "configurarEliminacion";
			   document.forms[0].target='submitArea';
			  ventaModalGeneral(document.forms[0].name,"P");
			  return;
			}
		</script>		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS --> 	
	</head>

	<body>
			
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="pys.busquedaServicios.cabecera"/>
				</td>
			</tr>
		</table>
		
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCamposGrande"  align="center" border="0">
			<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="">
				<html:hidden property="modo" value="<%=modo%>"/>
				<html:hidden property="precio" value=""/>
				<html:hidden property="periodicidad" value=""/>
				<html:hidden property="refresco" value=""/>
				<html:hidden property="comprobarCondicion" value=""/>
				<html:hidden property="comprobarCondicionBaja" value="<%=new Boolean(bFechaBaja).toString() %>"/>
				<% if (modo.equalsIgnoreCase("insertar")){ %>
					<html:hidden property="idInstitucion" value="<%=usr.getLocation()%>"/>
					<html:hidden property="idTipoServicios" value=""/>
					<html:hidden property="idServicio" value=""/>
					<html:hidden property="idServiciosInstitucion" value=""/>

				<% } else { %>
					<html:hidden property="idInstitucion" value="<%=row.getString(PysServiciosInstitucionBean.C_IDINSTITUCION)%>"/>
					<html:hidden property="idTipoServicios" value="<%=row.getString(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS)%>"/>
					<html:hidden property="idServicio" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIO)%>"/>
					<html:hidden property="idServiciosInstitucion" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION)%>"/>
				<% } %>

				<input type="hidden" name="fechaEfectiva"	value="">
				<tr>				
					<td  height="300">
						<siga:ConjCampos leyenda="pys.busquedaServicios.leyenda">					
					 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
								<tr>							
									<html:hidden property="idServInst" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION)%>"/>
									
									<td class="labelText"  >
										<siga:Idioma key="pys.mantenimientoServicios.literal.tipo"/>&nbsp;(*)
									</td>
									<td class="labelText">
										<% if (modo=="insertar"){%>
											<siga:ComboBD nombre = "tipoServicio" tipo="tipoServicio" clase="boxCombo" obligatorio="true" accion="Hijo:servicio"/>
										<% } else { %>  
											<% vTipoServicios.add(row.getString(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS)); %>  
											<%if (modo=="modificar"){%> 										
												<html:hidden property="servicio" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIO)%>"/>
												<siga:ComboBD nombre = "tipoServicio" tipo="tipoServicio" clase="boxConsulta" obligatorio="true" elementoSel="<%=vTipoServicios%>" accion="Hijo:servicio" readOnly="true"/>
											
											<% } else { %>
												<siga:ComboBD nombre = "tipoServicio" tipo="tipoServicio" clase="boxConsulta" obligatorio="true" elementoSel="<%=vTipoServicios%>" accion="Hijo:servicio" readOnly="true"/>
											<% } %>	
										 <% } %>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="pys.mantenimientoServicios.literal.IVA"/>&nbsp;(*)
									</td>
									<td class="labelText"> 
										<% if (modo=="insertar") {%>
											<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="boxCombo" obligatorio="true"/>
											
								 		<% } else { %>  
								 			<%if (modo=="modificar"){%> 
												<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="boxCombo" elementoSel="<%=vIva%>" obligatorio="true"/>
												
											<% } else { %>
												<siga:ComboBD nombre = "iva" tipo="porcentajeIva" clase="boxConsulta" elementoSel="<%=vIva%>" obligatorio="true" readOnly="true"/>
											<% } %>	
										 <% } %>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="facturacion.sufijos.literal.concepto"/>
									</td>
									<td class="labelText">
										<siga:ComboBD nombre = "sufijo" ancho="175" tipo="cmbSufijos" clase="<%=estiloCombo%>" elementoSel="<%=sufijoSel%>" obligatorio="false" readonly="<%=String.valueOf(lectura)%>"  parametro="<%=institucionParam %>" />
									</td>									
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="pys.mantenimientoServicios.literal.categoria"/>&nbsp;(*)
									</td>
									<td class="labelText">										
										<% if (modo=="insertar"){%>
											<siga:ComboBD nombre = "servicio" ancho="200" tipo="categoriaServicio" parametro="<%=parametroCombo%>" clase="boxCombo" obligatorio="true" hijo="t"/>
										<% } else { %>
											<% vServicios.add(row.getString(PysServiciosInstitucionBean.C_IDSERVICIO)); %>
											<% if (modo=="modificar"){ %>
												<html:text property="servicioC" styleClass="boxConsulta" size="30" value='<%=row.getString("CATEGORIA")%>' readOnly="true" />
												
											<% } else { %>
												<html:text property="servicioC" styleClass="boxConsulta" size="30" value='<%=row.getString("CATEGORIA")%>' readOnly="true" />
											<% } %>											
										<% } %>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="pys.mantenimientoServicios.literal.cuenta"/>&nbsp;&nbsp;
									</td>
									<td class="labelText" colspan="3">
										<% if (modo=="insertar"){%>
											<html:text property="cuentaContable" styleClass="box" size="20" value="" />
										<% } else { %>
											<% if (modo=="modificar"){ %>
												<html:text property="cuentaContable" styleClass="box" size="20" value="<%=row.getString(PysServiciosInstitucionBean.C_CUENTACONTABLE)%>" />
												
											<% } else { %>
												<html:text property="cuentaContable" styleClass="boxConsulta" size="20" value="<%=row.getString(PysServiciosInstitucionBean.C_CUENTACONTABLE)%>" readOnly="true" />
											<% } %>							  						  		
										<% } %>
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="pys.mantenimientoServicios.literal.servicio"/>&nbsp;(*)
										</td>
										<td class="labelText">
											<% if (modo=="insertar"){%>
									  			<html:text property="descripcion" styleClass="box" size="30" />
									  			
									  		<% } else { %>
												<% if (modo=="modificar"){ %>
													<html:text property="descripcion" styleClass="box" size="30" value="<%=row.getString(PysServiciosInstitucionBean.C_DESCRIPCION)%>" />
													
												<% } else { %>
													<html:text property="descripcion" styleClass="boxConsulta" size="30" value="<%=row.getString(PysServiciosInstitucionBean.C_DESCRIPCION)%>" readOnly="true" />
												<% } %>
									  		<% } %>
									  	</td>
									  	
										<td class="labelText" colspan="2">
											<% if (modo=="insertar"){%>
												<siga:Idioma key="pys.mantenimientoServicios.literal.baja"/>&nbsp;&nbsp;
												<input type="checkbox" name="bajaInternet" value="1"> 
												
									  		<% } else { %>
													<% if (modo=="modificar"){ %>
										  				<% if (row.getString(PysServiciosInstitucionBean.C_SOLICITARBAJA).equals(ClsConstants.DB_TRUE)){%>	
										  					<siga:Idioma key="pys.mantenimientoServicios.literal.baja"/>&nbsp;&nbsp;
										  					<input type="checkbox" name="bajaInternet"  value="1" checked>
										  					
										  				<% } else { %>			  		
															<siga:Idioma key="pys.mantenimientoServicios.literal.baja"/>&nbsp;&nbsp;
															<input type="checkbox" name="bajaInternet" value="1">
										  				<% } %>
										  				
										  			<% } else { %>
										  				<% if (row.getString(PysServiciosInstitucionBean.C_SOLICITARBAJA).equals(ClsConstants.DB_TRUE)){%>	
										  					<siga:Idioma key="pys.mantenimientoServicios.literal.baja"/>&nbsp;&nbsp;
										  					<input type="checkbox" name="bajaInternet"  value="1" checked disabled>
										  					
										  				<% } else { %>			  		
															<siga:Idioma key="pys.mantenimientoServicios.literal.baja"/>&nbsp;&nbsp;
															<input type="checkbox" name="bajaInternet"  value="1" disabled>
										  				<% } %>
										  			<% } %>						  				
									  		<% } %>							
										</td>
										
										<td class="labelText" colspan="2">
											<% if (modo=="insertar"){%>
												<siga:Idioma key="pys.mantenimientoServicios.literal.alta"/>&nbsp;&nbsp;
												<input type="checkbox" name="altaInternet" value="1">
									  		<% } else { %>
													<% if (modo=="modificar"){ %>
										  				<% if (row.getString(PysServiciosInstitucionBean.C_SOLICITARALTA).equals(ClsConstants.DB_TRUE)){%>	
										  					<siga:Idioma key="pys.mantenimientoServicios.literal.alta"/>&nbsp;&nbsp;
										  					<input type="checkbox" name="altaInternet" value="1" checked>
										  					
										  				<% } else { %>			  		
															<siga:Idioma key="pys.mantenimientoServicios.literal.alta"/>&nbsp;&nbsp;
															<input type="checkbox" name="altaInternet" value="1">
										  				<% } %>
										  				
										  			<% } else { %>
										  				<% if (row.getString(PysServiciosInstitucionBean.C_SOLICITARALTA).equals(ClsConstants.DB_TRUE)){%>	
										  					<siga:Idioma key="pys.mantenimientoServicios.literal.alta"/>&nbsp;&nbsp;
										  					<input type="checkbox" name="altaInternet" value="1" checked disabled>
										  					
										  				<% } else { %>			  		
															<siga:Idioma key="pys.mantenimientoServicios.literal.alta"/>&nbsp;&nbsp;
															<input type="checkbox" name="altaInternet" value="1" disabled>
										  				<% } %>
										  			<% } %>
									  		<% } %>											  		
										</td>
									</tr>
								</table>
								
								<table width="100%" border="0">
									<tr>
										<td width="40%"  valign="top">
											<siga:ConjCampos leyenda="pys.mantenimientoServicios.literal.formaPago">					
												<table border="0">
											  		<tr>
														<td width="10%" class="labelText" align="left">
															<siga:Idioma key="productos.mantenimientoProductos.literal.internet"/>&nbsp;&nbsp;
														</td>
														<td width="40%" class="labelText" align="left">																						
															<% if (modo=="insertar"){int i=0;%>
																<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternet" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" parametro="<%=valorInternet%>"/>
																
															<% } else { %>
																<%	if (vInt.isEmpty()){int i=0;%>
																	<% if (modo=="modificar"){ %>
																		<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternet" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" parametro="<%=valorInternet%>"/>
																		
																	<% } else { %>
																		<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternet" clase="boxConsulta" filasMostrar="4" seleccionMultiple="true" obligatorio="true" parametro="<%=valorInternet%>" readOnly="true"/>
																	<% } %>		
																															
																<% } else { %>
																	<% if (modo=="modificar"){ %>
																		<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternet" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vInt%>" obligatorio="true" parametro="<%=valorInternet%>"/>
																		
																	<% } else { %>
																		<siga:ComboBD nombre = "formaPagoInternet" tipo="cmbFormaPagoInternet" clase="boxConsulta" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vInt%>" obligatorio="true" parametro="<%=valorInternet%>" readOnly="true"/>
																	<% } %>																																		
																<% } %>
															<% } %>
														</td>
														
														<td width="10%" class="labelText" align="left">
															<siga:Idioma key="productos.mantenimientoProductos.literal.secretaria"/>&nbsp;(*)
														</td>
														<td width="40%"class="labelText" align="left">
															<% if (modo=="insertar"){int i=0;%>
																<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretaria" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" parametro="<%=valorSecretaria%>"/>
																
															<% } else { %>													
																<% if (vSec.isEmpty()){int i=0;%>
																	<% if (modo=="modificar"){ %>
																		<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretaria" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=i%>" obligatorio="true" parametro="<%=valorSecretaria%>"/>
																		
																	<% } else { %>
																		<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretaria" clase="boxConsulta" filasMostrar="4" seleccionMultiple="true" obligatorio="true" parametro="<%=valorSecretaria%>" readOnly="true"/>
																	<% } %>	
																																																			
																<% } else { %>
																	<% if (modo=="modificar"){ %>
																		<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretaria" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vSec%>" obligatorio="true" parametro="<%=valorSecretaria%>"/>
																		
																	<% } else { %>
																		<siga:ComboBD nombre = "formaPagoSecretaria" tipo="cmbFormaPagoSecretaria" clase="boxConsulta" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vSec%>" obligatorio="true" parametro="<%=valorSecretaria%>" readOnly="true"/>
																	<% } %>																			
																<% } %>																													
															<% } %>
														</td>														
											  		</tr>
												</table>
											</siga:ConjCampos>					
												
											<table width="100%" border="0">
												<tr>
													<td width="35%">
														<siga:ConjCampos leyenda="productos.mantenimientoProductos.literal.estado">					
															<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
																<tr>
																	<td class="labelText" width="100%">
																		<siga:Idioma key="productos.mantenimientoProductos.literal.bajaLogica"/>&nbsp;&nbsp;
																		<% if (modo=="insertar") {	%>
																			<input type="checkbox" name="bajaLogica" id="bajaLogica" value="1" disabled >
																		<% } else {%>
																			<% if (fechaBaja!=null) {%>
																				<input type="checkbox" name="bajaLogica" id="bajaLogica" value="1" checked>																			
																			<% } else { %>
																				<input type="checkbox" name="bajaLogica" id="bajaLogica" value="1">
																			<% } %>
																		<% } %>
																	</td>
																</tr>
															</table>
														</siga:ConjCampos>
													</td>
													
													<td width="65%">
													<!-- DCG ini --> 												
														<siga:ConjCampos leyenda="productos.mantenimientoServicios.literal.asginacion">					
															<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
																<tr>
																	<td class="labelText">
																		<siga:Idioma key="productos.mantenimientoServicios.literal.SuscripcionAutomatica"/>&nbsp;&nbsp;
																		<% if (modo=="insertar"){%>
																			<input type="checkbox" name="automatico" value="1" disabled >
																			
													  					<% } else { %>
																			<% if (modo=="modificar"){ %>
														  						<% if (row.getString(PysServiciosInstitucionBean.C_AUTOMATICO).equals(ClsConstants.DB_TRUE)){%>	
														  							<input type="checkbox" name="automatico" value="1" checked onClick="return comprobarSiExisteCondicion();">
														  							
														  						<% } else { %>			  		
																					<input type="checkbox" name="automatico" value="1" onClick="return comprobarSiExisteCondicion();">
																  				<% } %>
																  				
															  				<% } else { %>
														  						<% if (row.getString(PysServiciosInstitucionBean.C_AUTOMATICO).equals(ClsConstants.DB_TRUE)){%>	
														  							<input type="checkbox" name="automatico" value="1" checked disabled >
														  							
														  						<% } else { %>			  		
																					<input type="checkbox" name="automatico" value="1" disabled >
														  						<% } %>
														  					<% } %>						  				
													  					<% } %>
													  				</td>
														
													  				<td align="center">
																		<% if (modo != "insertar") {	%>
													  						<input type="button" 
													  					 		value="<siga:Idioma key="pys.mantenimientoServicios.boton.condicion"/>" 
													  					 		alt="Poner recurso condicion" 
													  					 		id="idButton"  
													  					 		onclick="return nuevaCondicionSuscripcionAutomatica();" 
													  					 		class="button" >
													  					<% } %>													  		
																	</td>
																</tr>										
															</table>
														</siga:ConjCampos>
													<!-- DCG ini --> 
													</td>
												</tr>
											</table>												
										</td>
										
										<td width="50%" valign="top">
											<siga:ConjCampos leyenda="pys.mantenimientoServicios.literal.ponderacion">	
												<table width="100%" valign="top" border="0">												
													<tr>
														<td class="labelText">
															<% if (modo=="insertar"){%>
																<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_PONDERADO%>" checked>
																<siga:Idioma key="pys.cambioSituacion.ponderado"/>
							  								<% } else { 
							  										if (row.getString(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO).equals(ClsConstants.INICIOFINALPONDERADO_PONDERADO)){%>	
																		<% if (modo=="modificar"){ %>
																			<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_PONDERADO%>" checked>
																			<siga:Idioma key="pys.cambioSituacion.ponderado"/>
																			
																		<% } else { %>
																			<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_PONDERADO%>" checked disabled>
																			<siga:Idioma key="pys.cambioSituacion.ponderado"/>
																		<% } %>
																		
							  										<% } else { %>
																		<% if (modo=="modificar"){ %>
																			<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_PONDERADO%>">
																			<siga:Idioma key="pys.cambioSituacion.ponderado"/>
																			
																		<% } else { %>
																			<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_PONDERADO%>" disabled>
																			<siga:Idioma key="pys.cambioSituacion.ponderado"/>
																		<% } %>
							  										<% } %>
							  								<% } %>																											
														</td>
													</tr>
													
													<tr>
														<td class="labelText">
															<% if (modo=="insertar"){%>
																<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_INICIO%>">
																<siga:Idioma key="pys.cambioSituacion.inicioPonderado"/>
																
							  								<% } else {%> 
							  									<% if (row.getString(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO).equals(ClsConstants.INICIOFINALPONDERADO_INICIO)){%>
																	<% if (modo=="modificar"){ %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_INICIO%>" checked>
																		<siga:Idioma key="pys.cambioSituacion.inicioPonderado"/>
																			
																	<% } else { %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_INICIO%>" checked disabled>
																		<siga:Idioma key="pys.cambioSituacion.inicioPonderado"/>
																	<% } %>
																		
							  									<% } else { %>
																	<% if (modo=="modificar"){ %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_INICIO%>">
																		<siga:Idioma key="pys.cambioSituacion.inicioPonderado"/>
																			
																	<% } else { %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_INICIO%>" disabled>
																		<siga:Idioma key="pys.cambioSituacion.inicioPonderado"/>
																	<% } %>																		
							  									<% } %>
							  								<% } %>
														</td>
													</tr>
													
													<tr>
														<td class="labelText">
															<% if (modo=="insertar"){%>
																<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_FINAL%>">
																<siga:Idioma key="pys.cambioSituacion.finalPeriodo"/>
																
							  								<% } else {%> 
							  									<% if (row.getString(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO).equals(ClsConstants.INICIOFINALPONDERADO_FINAL)){%>
																	<% if (modo=="modificar"){ %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_FINAL%>" checked>
																		<siga:Idioma key="pys.cambioSituacion.finalPeriodo"/>
																		
																	<% } else { %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_FINAL%>" checked disabled>
																		<siga:Idioma key="pys.cambioSituacion.finalPeriodo"/>
																	<% } %>
																	
							  									<% } else { %>
																	<% if (modo=="modificar"){ %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_FINAL%>">
																		<siga:Idioma key="pys.cambioSituacion.finalPeriodo"/>
																		
																	<% } else { %>
																		<input type="radio" name="cambioSituacion" value="<%=ClsConstants.INICIOFINALPONDERADO_FINAL%>" disabled>
																		<siga:Idioma key="pys.cambioSituacion.finalPeriodo"/>
																	<% } %>																		
							  									<% } %>
							  								<% } %>
														</td>
													</tr>	
												</table>
											</siga:ConjCampos>	
													
											<siga:ConjCampos leyenda="productos.mantenimientoProductos.literal.facturacionPonderada">					
												<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="labelText" width="100%">
															<siga:Idioma key="productos.mantenimientoProductos.literal.noPonderar"/>&nbsp;&nbsp;
															<% if ((noPondera!=null && noPondera.equals("0")) || noPondera==null) {%>
																<input type="checkbox" name="noPondera" value="0" <%=checkPonderar%>>
																
															<% } else { %>
																<input type="checkbox" name="noPondera" value="1" checked <%=checkPonderar%>>
															<% } %>
														</td>
													</tr>
												</table>
											</siga:ConjCampos>									    	
										</td>											
									</tr>

									<tr>
										<td>	
											<table width="100%" cellpadding="3" cellspacing="3" border="0">
											  	<tr>
											  		<td width="35%">
											  			&nbsp;
											  		</td>											  
											    	<td  width="100%"  align="left"  >
														<% if (modo != "insertar" && modo != "consulta") { %>
												  			<input type="button" 
												  				value="<siga:Idioma key="pys.mantenimientoServicios.boton.eliminarSuscripcion"/>" 
												  				alt="Poner recurso condicion" 
												  				id="idButton"  
												  				onclick="return eliminarSuscripcionAutomatica();" 
												  				class="button" >
												  		<% } %>
													</td>
											 	</tr>															
										    </table>
										</td>	
									</tr>
								</table>	
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>						
			</table>

			<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>'  modal="G" clase="botonesSeguido"/>
		
			<% if ((modo.equalsIgnoreCase("editar"))||(modo.equalsIgnoreCase("edicion"))||(modo.equalsIgnoreCase("insertar"))||(modo.equalsIgnoreCase("modificar"))){ %>
				<siga:Table 
				   name="tablaResultados"
				   border="1"
				   columnNames="pys.mantenimientoServicios.literal.precio,pys.mantenimientoServicios.literal.periodicidad,pys.mantenimientoCategorias.literal.descripcion,productos.mantenimientoProductos.literal.precioDefecto,"
				   columnSizes="20,20,20,20,20"
				   modal="G">
						   				   
					<% if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 ) { %>									
						<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
						
					<% } else { %>
						<%
				    		// RGG 05-10-2005 Primera pasada para saber cuantos hay por defecto.
				    		Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();
							int contDefecto=0;
		            		while (en.hasMoreElements()) {
			            		Row rowPrecios = (Row) en.nextElement();
			            		if (rowPrecios.getString(PysPreciosServiciosBean.C_PORDEFECTO).equalsIgnoreCase(ClsConstants.DB_TRUE)){
			            			contDefecto ++;
								}
			            	}			            	

		            		en = ((Vector)request.getAttribute("DATESTADO")).elements();
							int recordNumber=1;
		            		String iconosFila="";
							while (en.hasMoreElements()) {
			            		Row rowPrecios = (Row) en.nextElement();
			            		if (rowPrecios.getString(PysPreciosServiciosBean.C_PORDEFECTO).equalsIgnoreCase(ClsConstants.DB_TRUE)){
				            		if (contDefecto==1) {
					            		iconosFila="C,E";
					            	} else {
						            	iconosFila="C,E,B";
					            	}
			            		}else{
				            		iconosFila="C,E,B";
			            		}
			            		
			            		//Precio:
			            		double precio = 0.00;
								String sPrecio = rowPrecios.getString(PysPreciosServiciosBean.C_VALOR);

								try { 
									if (sPrecio!=null)
										precio = Double.parseDouble(sPrecio);
									
								} catch(NumberFormatException e){
									precio = 0.00;
								}	
			            %>
						            		
							<siga:FilaConIconos
							  	fila='<%=String.valueOf(recordNumber)%>'
							  	botones='<%=iconosFila%>'
							  	modo='<%=modo%>'
							  	clase="listaNonEdit">		
								  										  
								<td align = "right">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDINSTITUCION)%>">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDTIPOSERVICIOS)%>">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDSERVICIO)%>">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION)%>">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDPERIODICIDAD)%>">
									<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=rowPrecios.getString(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS)%>">
									<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formato(precio))%>&nbsp;&euro;
								</td>
								
								<td align = "right">
									<%=UtilidadesString.mostrarDatoJSP(rowPrecios.getString("PERIODICIDAD"))%>
								</td>  	
								
								<td align = "right">
									<%=UtilidadesString.mostrarDatoJSP(rowPrecios.getString("DESCRIPCION"))%>
								</td>  																				
								
								<td align = "right">
									<% if (rowPrecios.getString(PysPreciosServiciosBean.C_PORDEFECTO).equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
										<siga:Idioma key="general.yes"/>
										
									<% } else { %>
										<siga:Idioma key="general.no"/>
									<% } %>
								</td>  								
							</siga:FilaConIconos>
							
							<% recordNumber++;%>
						<% } // WHILE %>
					<% } // ELSE %>					
				</siga:Table>		
					
			<% } else {	%>								
				<siga:Table 
					name="tablaResultados"
					border="1"
					columnNames="pys.mantenimientoServicios.literal.precio,pys.mantenimientoServicios.literal.periodicidad,pys.mantenimientoCategorias.literal.descripcion,productos.mantenimientoProductos.literal.precioDefecto"
					columnSizes="30,20,30,20"
					modal="G">

					<%if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 ){ %>
						<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
							
					<% } else {
					   	Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();								    	
						while (en.hasMoreElements()) {
					    	Row rowPrecios = (Row) en.nextElement();
					            		
					        //Precio:
					        double precio = 0.00;
							String sPrecio = rowPrecios.getString(PysPreciosServiciosBean.C_VALOR);

							try { 
								if (sPrecio!=null)
									precio = Double.parseDouble(sPrecio);
							} catch(NumberFormatException e){
								precio = 0.00;
							}	
					%>
							<tr>						            							  
								<td align = "right" class="listaNonEdit" nowrap>
									<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(precio))%>&nbsp;&euro;
								</td>
								<td class="listaNonEdit" nowrap>
									<%=UtilidadesString.mostrarDatoJSP(rowPrecios.getString("PERIODICIDAD"))%>
								</td>
								
								<td class="listaNonEdit" nowrap>
									<%=UtilidadesString.mostrarDatoJSP(rowPrecios.getString("DESCRIPCION"))%>
								</td>													
								<td class="listaNonEdit" nowrap>
									<% if (rowPrecios.getString(PysPreciosServiciosBean.C_PORDEFECTO).equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
										<siga:Idioma key="general.yes"/>
										
									<% } else { %>
										<siga:Idioma key="general.no"/>
									<% } %>
								</td>  																	
							</tr>	
						<% } // WHILE %>
					<% } // ELSE %>
				</siga:Table> 
			<% } // ELSE %>
			
			<siga:ConjBotonesAccion botones='<%=botonesPrecio%>' modo='<%=modo%>' clase="botonesDetalle" modal="G"/>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">
				// DCG  ini
				<% if (modo.equalsIgnoreCase("Consulta"))	{%>
			    	jQuery("#bajaLogica").attr("disabled","disabled");
				<% } %>
				// DCG  fin			
			
			//Asociada al boton Guardar
			function accionGuardar() {	
				sub();
				
				var antesAutomatico = <%= new Boolean(bAutomatico).toString()%>;
				var antesBaja = <%= new Boolean(bFechaBaja).toString()%>;
				
				if ((document.forms[0].automatico.checked && !antesAutomatico) || (document.forms[0].bajaLogica.checked && !antesBaja)){		
			      	var fecha = showModalDialog("/SIGA/html/jsp/productos/ventanaFechaEfectiva.jsp","","dialogHeight:200px;dialogWidth:400px;help:no;scroll:no;status:no;");
			      	window.top.focus();
			      	if (fecha!=null) {				
						document.forms[0].fechaEfectiva.value=fecha;
     				  	
				  	} else {				 
				  		fin();
				    	return false;
				  	}	  
				}
			    			
				if (validateMantenimientoServiciosForm(document.MantenimientoServiciosForm)){				
					if (validacionPosterior()){					
						<% if (modo.equalsIgnoreCase("modificar")){ %>
							document.forms[0].modo.value="modificar";
							document.forms[0].target='submitArea';
							
						<% } else { %>						    										
							// Inserto el precio por defecto
							var datos = showModalDialog("/SIGA/html/jsp/productos/ventanaPrecioPorDefecto.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
							window.top.focus(); 
							if (datos) {							  
								if (datos[0] == 1) { // Recibo precio y periodicidad
									// Asigno valores asociados al servicio en creacion
									document.forms[0].idTipoServicios.value=document.forms[0].tipoServicio.value;
									document.forms[0].idServicio.value=document.forms[0].servicio.value;
									// Solo asigno un cero para saber de donde viene
									document.forms[0].idServiciosInstitucion.value="0";
	
									// Asigno el precio y la periodicidad obtenidos en la ventana modal
									document.forms[0].precio.value=datos[1];
									document.forms[0].periodicidad.value=datos[2];
									
									// Redirigimos al lugar adecuado
									document.forms[0].modo.value="insertar";
									document.forms[0].target='submitArea';
									
								} else {
									// por aqui pasa cuando le damos al boton de cerrar sin meter ningún precio.
								 	fin();
								 	return false;
								}
								
							} else {
								//por aqui pasa cuando le damos al aspa para cerrar la ventana y no se ha metido precio.							
							  	fin();
								return false;
							}	
						<% } %>													

						// RGG compruebo que antes no estaba checked
						if(document.forms[0].bajaLogica.checked && !antesBaja) {							
							men = '<siga:Idioma key="pys.mantenimientoServicios.mensaje.realizarBajaLogica"/>';
							if (confirm(men)) {
								// Abro la ventana de las tuercas:
								var f = document.forms[0].name;											  
								var m = "pys.mantenimientoServicios.cabecera.tuercas.realizarBajaLogica";
								window.frames.submitArea.location = '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
							} 
							
							fin();
							return false;								

							
						} else {			
							// RGG comprubo que antes no estaba checked
							if(document.forms[0].automatico.checked && !document.forms[0].bajaLogica.checked && !antesAutomatico) {							
								document.forms[0].comprobarCondicion.value = document.forms[0].automatico.checked;
								men = '<siga:Idioma key="pys.mantenimientoServicios.mensaje.suscripcionAutomaticaATodos"/>';
							 	if (confirm(men)) {									 						  
									// Abro la ventana de las tuercas:
									var f = document.forms[0].name;	
									var m = "pys.mantenimientoServicios.cabecera.tuercas.suscripcionAutomaticaATodos";
									window.frames.submitArea.location = '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
								}
								
								fin();
								return false;
							}
						}
						
						if (document.forms[0].noPondera.checked) {
						 	document.forms[0].noPondera.value="1";
						 	
						} else {
						 	document.forms[0].noPondera.value="0";
						}
						
 						document.forms[0].submit();
 						
					} else {					
						fin();
					    return false;
					}	
					
				} else {				
					fin();
				    return false;
				}	
			}

			// Asociada al boton Restablecer
			function accionRestablecer() {		
				document.forms[0].reset();
			}

			// Asociada al boton Restablecer
			function accionNuevo() {					
				if ((document.forms[0].idTipoServicios.value!="")&&(document.forms[0].idServicio.value!="")){
					document.forms[0].modo.value='nuevoCriterio';					
					var resultado = ventaModalGeneral(document.forms[0].name,"G");					
					refrescarLocal();
					
				} else {					
					var mensaje='<siga:Idioma key="messages.pys.errorServicioAntesPrecio"/>';
					alert(mensaje);
				}		
			}
			
			// Asociada al boton Cerrar
			function accionCerrar() {		
				top.cierraConParametros("MODIFICADO");
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>