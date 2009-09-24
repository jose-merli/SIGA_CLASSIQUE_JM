<!-- datosLineaAbono.jsp -->
<!-- 
	 Muestra el formulario de edicion de lines de abonos
	 VERSIONES:
		 miguel.villegas 11/03/2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.FacLineaAbonoBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String nextModo = request.getAttribute("modelo").toString(); // Obtengo la operacion (modificar o insertar)a realizar

	String idAbono="";
	String idInstitucion="";
	String idFactura="";
	String totalAbono="0";
	String totalFactura="0";
	String numeroLinea="";
	Vector entradas=new Vector();
	Row registro=new Row();
	String impNeto="0";
	String impIva="0";
	String impTotal="0";

	idAbono=(String)request.getAttribute("IDPERSONA"); // Obtengo el id de la persona
	idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el id de la institucion a la que pertenece

	if (!((String)request.getAttribute("TOTAL_ABONO")).equalsIgnoreCase("")){
		totalAbono=(String)request.getAttribute("TOTAL_ABONO"); // Obtengo el total a abonar hasta el momento
	}	

	
	idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el id de la factura
	if (!idFactura.equalsIgnoreCase("")){
		totalFactura=(String)request.getAttribute("TOTAL_FACTURA"); // Obtengo el total (posible) de la factura a abonar
	}	

	// MAV 26/8/2005 Resolucion de incidencias relacionada con la insercion de un combo para el iva
	ArrayList valorIva = new ArrayList(); // valores originales iva
	
	String estiloBox="box";
	String estiloCombo="boxCombo";
	String estiloBoxNumber="boxNumber";
	String readonly="false";
	boolean breadonly=false;
	
	// Obtener informacion para rellenar en caso de modificacion o consulta
	if (nextModo.equalsIgnoreCase("modificar") || nextModo.equalsIgnoreCase("ver")){
		entradas=(Vector)request.getAttribute("container"); // Obtengo el vector
		registro=(Row)entradas.firstElement();
		
		// MAV 26/8/2005 Resolucion de incidencias relacionada con la insercion de un combo para el iva
        valorIva.add(registro.getString(FacLineaAbonoBean.C_IVA));
		
		impNeto=(new Double(UtilidadesNumero.redondea((new Double(registro.getString(FacLineaAbonoBean.C_CANTIDAD))).doubleValue()*(new Double(registro.getString(FacLineaAbonoBean.C_PRECIOUNITARIO))).doubleValue(),2))).toString();
		impIva=(new Double(UtilidadesNumero.redondea(((new Double(impNeto)).doubleValue()*(new Double(registro.getString(FacLineaAbonoBean.C_IVA))).doubleValue())/(new Double("100").doubleValue()),2))).toString();
		impTotal= new Double(UtilidadesNumero.redondea(new Double(impIva).doubleValue() + new Double(impNeto).doubleValue(),2)).toString();
		numeroLinea=registro.getString(FacLineaAbonoBean.C_NUMEROLINEA);
	}			

	String	botones="";
	if (nextModo.equalsIgnoreCase("modificar")){
		botones = "C,Y,R";
	} else {
		botones = "C";
		estiloBox="boxConsulta";
		estiloCombo="boxConsulta";
		estiloBoxNumber="boxConsultaNumber";
		readonly="true";
		breadonly=true;
	}
	
%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="AbonosLineasForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			  titulo="facturacion.busquedaAbonos.literal.cabecera" 
			  localizacion="facturacion.busquedaAbonos.ruta"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>


	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="facturacion.mantenimientoLineas.literal.cabecera"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposMedia"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.mantenimientoLineas.literal.datosLinea">
						<table class="tablaCampos" align="center">
							<html:form action="/FAC_AbonosLineas.do" method="POST" target="submitArea">
								<html:hidden property = "modo" value = "<%=nextModo%>"/>
								<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
								<html:hidden property = "idAbono" value = "<%=idAbono%>"/>
								<html:hidden property = "numeroLinea" value = "<%=numeroLinea%>"/>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.mantenimientoLineas.literal.contabilizado"/>&nbsp;(*)
									</td>
									<td class="labelTextValue">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<html:textarea cols="90" rows="2" property="descripcion" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" readonly="<%=breadonly%>"  styleClass="<%=estiloBox%>"></html:textarea> 
										<% } else { %>
											<html:textarea cols="90" rows="2" property="descripcion" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" styleClass="<%=estiloBox%>" readonly="<%=breadonly%>"  value="<%=registro.getString(FacLineaAbonoBean.C_DESCRIPCIONLINEA)%>"></html:textarea> 
							  	    	<% } %>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.lineasAbonos.literal.cantidad"/>&nbsp;(*)
									</td>	
									<td class="labelTextValue">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<html:text property="cantidad" size="5" maxlength="10" styleClass="<%=estiloBoxNumber%>" readonly="<%=breadonly%>"  value="" onchange="calcularNeto();"></html:text>
										<% } else { %>
											<html:text property="cantidad" size="5" maxlength="10" styleClass="<%=estiloBoxNumber%>" readonly="<%=breadonly%>"  value="<%=registro.getString(FacLineaAbonoBean.C_CANTIDAD)%>" onchange="calcularNeto();"></html:text>
							  	    	<% } %>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.lineasAbonos.literal.precioUnit"/>&nbsp;(*)
									</td>	
									<td class="labelTextValue">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<html:text property="precio" size="13" maxlength="11" styleClass="<%=estiloBoxNumber%>" readonly="<%=breadonly%>"  value="" onchange="calcularNeto();"></html:text>&nbsp;&euro;
										<% } else { %>
											<html:text property="precio" size="13" maxlength="11" styleClass="<%=estiloBoxNumber%>" readonly="<%=breadonly%>"  value="<%=UtilidadesNumero.formatoCampo(registro.getString(FacLineaAbonoBean.C_PRECIOUNITARIO))%>" onchange="calcularNeto();"></html:text>&nbsp;&euro;
							  	    	<% } %>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.importeNeto"/>
									</td>	
									<td class="labelTextValue">
										<html:text property="impNeto" size="13" maxlength="13" styleClass="boxConsultaNumber" readonly="<%=breadonly%>" value="<%=UtilidadesNumero.formatoCampo(impNeto)%>"></html:text>&nbsp;&euro;
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.lineasAbonos.literal.iva"/>&nbsp;(*)
									</td>	
									<td class="labelTextValue">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<!--html:text property="iva" size="5" maxlength="5" styleClass="<%=estiloBox%>" value="" onchange="calcularIva();"!--><!--/html:text--><!--&nbsp;&nbsp;%-->
											<siga:ComboBD nombre = "iva" tipo="porcentajeIva" readonly="<%=readonly%>"  clase="<%=estiloCombo%>" obligatorio="true" accion="calcularIva();"/>
										<% } else { %>
											<!--html:text property="iva" size="5" maxlength="5" styleClass="<%=estiloBox%>" value="<%=registro.getString(FacLineaAbonoBean.C_IVA)%>" onchange="calcularIva();"--><!--/html:text--><!--&nbsp;&nbsp;%-->
											<siga:ComboBD nombre = "iva" tipo="porcentajeIva" readonly="<%=readonly%>"  clase="<%=estiloCombo%>" elementoSel="<%=valorIva%>" obligatorio="true" accion="calcularIva();"/>
							  	    	<% } %>
									</td>
								</tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.importeIva"/>
									</td>	
									<td class="labelTextValue">
										<html:text property="impIva" size="13" maxlength="12" styleClass="boxConsultaNumber" readonly="<%=breadonly%>" value="<%=UtilidadesNumero.formatoCampo(impIva)%>"></html:text>&nbsp;&euro;
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.mantenimientoLineas.literal.totalConcepto"/>
									</td>	
									<td class="labelTextValue">
										<html:text property="impTotal" size="13" maxlength="12" styleClass="boxConsultaNumber" readonly="<%=breadonly%>" value="<%=UtilidadesNumero.formatoCampo(impTotal)%>"></html:text>&nbsp;&euro;
									</td>
								</tr>
							</html:form>	
						</table>
						</siga:ConjCampos>
					</td>
				</tr>
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
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modal="M"/>
			
			<!-- FIN: BOTONES REGISTRO -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">
	
			function validarPrecios(){
				document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
				document.forms[0].impNeto.value=document.forms[0].impNeto.value.replace(/,/,".");
				document.forms[0].impIva.value=document.forms[0].impIva.value.replace(/,/,".");			
				document.forms[0].impTotal.value=document.forms[0].impTotal.value.replace(/,/,".");
			}
		
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				var totalFactura=<%=totalFactura%>;
				var totalAbono=<%=totalAbono%>;
				var evaluacion=<%=!idFactura.equalsIgnoreCase("")%>;
				if (validateAbonosLineasForm(document.AbonosLineasForm)){
					validarPrecios();
					document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
					document.forms[0].iva.value=document.forms[0].iva.value.replace(/,/,".");
					//alert("Se evalua?->"+evaluacion+" entonces "+document.forms[0].impTotal.value+"+"+totalAbono+">"+totalFactura+"?");
					if ((evaluacion)&&(eval("("+document.forms[0].impTotal.value+"+"+totalAbono+")>"+totalFactura))){
						var mensaje='<siga:Idioma key="facturacion.lineasAbonos.literal.errorFacturaCantidad"/>';
						alert(mensaje);
					}else{
						document.forms[0].submit();							
					}	
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
				document.forms[0].reset();
			}
					
			function  convertirAFormato(n){
				var d = n.replace(/,/,".");
				d = new Number(n);
				d = Number(d.toFixed(2));
				d = d.toLocaleString();
				if(String(d).indexOf(',') < 0){
					d += '.00'; // aqui puede variar segun la cantidad de decimales que desees;
				}	
				return d.replace(".","");	
			}
			
			<!-- Recalcula el importe neto, iva y total,  -->
			function calcularNeto() 
			{
				if ((document.forms[0].cantidad.value!="")&&(document.forms[0].precio.value!="")){
					document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
					if (!isNaN(document.forms[0].cantidad.value)&&!isNaN(document.forms[0].precio.value)){
						document.forms[0].impNeto.value=eval(document.forms[0].cantidad.value + "*" + document.forms[0].precio.value);
						// Redondeo
						//document.forms[0].impNeto.value=parseInt(document.forms[0].impNeto.value*100)/100;
						document.forms[0].impNeto.value=Math.round(document.forms[0].impNeto.value*100)/100;
						if (document.forms[0].iva.value!=""){
							document.forms[0].iva.value=document.forms[0].iva.value.replace(/,/,".");
							if (!isNaN(document.forms[0].iva.value)){
								document.forms[0].impIva.value=eval((document.forms[0].impNeto.value + "*" + document.forms[0].iva.value) + "/" + 100);
								document.forms[0].impTotal.value=eval(document.forms[0].impNeto.value + "+" + document.forms[0].impIva.value);
								// Redondeo
								//document.forms[0].impIva.value=(parseInt(document.forms[0].impIva.value*100)/100;
								document.forms[0].impIva.value=Math.round(document.forms[0].impIva.value*100)/100;
								//document.forms[0].impTotal.value=parseInt(document.forms[0].impTotal.value*100)/100;
								document.forms[0].impTotal.value=Math.round(document.forms[0].impTotal.value*100)/100;

							//Formato:
							document.forms[0].impNeto.value = convertirAFormato(document.forms[0].impNeto.value);
							document.forms[0].impTotal.value = convertirAFormato(document.forms[0].impTotal.value);
							document.forms[0].impIva.value = convertirAFormato(document.forms[0].impIva.value);
							}
						}
						else{
							document.forms[0].impIva.value="0,00";
							document.forms[0].impTotal.value="0,00";
						}
					}
				}
				else{
					document.forms[0].impNeto.value="0,00";
					document.forms[0].impIva.value="0,00";
					document.forms[0].impTotal.value="0,00";
				}
			}
			
			<!-- Recalcula el importe iva y total,  -->
			function calcularIva() 
			{					
				if ((document.forms[0].cantidad.value!="")&&(document.forms[0].precio.value!="")&&(document.forms[0].iva.value!="")){
					document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
					document.forms[0].iva.value=document.forms[0].iva.value.replace(/,/,".");
					if (!isNaN(document.forms[0].cantidad.value)&&!isNaN(document.forms[0].precio.value)&&!isNaN(document.forms[0].iva.value)){
						document.forms[0].impIva.value=eval(eval(document.forms[0].impNeto.value + "*" +document.forms[0].iva.value)+"/"+"100");
						document.forms[0].impTotal.value=eval(document.forms[0].impNeto.value + "+" +document.forms[0].impIva.value);
						// Redondeo
						//document.forms[0].impIva.value=parseInt(document.forms[0].impIva.value*100)/100;
						document.forms[0].impIva.value=Math.round(document.forms[0].impIva.value*100)/100;
						//document.forms[0].impTotal.value=parseInt(document.forms[0].impTotal.value*100)/100;
						document.forms[0].impTotal.value=Math.round(document.forms[0].impTotal.value*100)/100;

						//Formato:
						document.forms[0].impTotal.value = convertirAFormato(document.forms[0].impTotal.value);
						document.forms[0].impIva.value = convertirAFormato(document.forms[0].impIva.value);
					}
				}
				else{
					document.forms[0].impIva.value="0,00";
					document.forms[0].impTotal.value="0,00";
				}
			}
			
			</script>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>