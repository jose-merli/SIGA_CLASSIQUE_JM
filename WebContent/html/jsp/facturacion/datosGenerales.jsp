<!-- datosGenerales.jsp -->
<!-- VENTANA DE DETALLE DE UN REGISTRO -->
<!-- Contiene un posible titulo del mantenimiento, ademas de la zona de campos
     a mantener, que utilizara conjuntos de datos si fuera necesario.
     Bajo esta zona y sin salirse del tamanho estandar de la ventana existira
     una zona de botones de acciones sobre el registro.
     VERSIONES:
	yolanda.garcia 22-12-2004 Creación
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
	String idInstitucion = user.getLocation();
	
	Vector datosSerie = (Vector)request.getAttribute("datosSerie");
	Vector datosContador = (Vector)request.getAttribute("datosContador"); 

	request.removeAttribute("datosSerie");
	
	String editable = (String)ses.getAttribute("editable");
	boolean bEditable = true;
	
	if (editable!=null && !editable.equals(""))
	{
		bEditable = editable.equals("1");
	}
	
	String accion = (String)ses.getAttribute("accion");
	
	String sAbreviatura="";
	String sDescripcion="";
	Integer iPlantilla=new Integer(-1);
	String sPlantilla="";
	String idSerieFacturacion="";
	String enviarFacturas = "";
	String generarPDF = "";
	String observaciones="";
	// Valores para combos setElement
	ArrayList vPlantilla = new ArrayList(); // valor original Plantilla
	String idTipoEnvioCorreoElectronico = ""+EnvEnviosAdm.TIPO_CORREO_ELECTRONICO;
	String parametrosCmbPlantillaEnvios[] = {user.getLocation(),idTipoEnvioCorreoElectronico};
	String parametrosPlantillasMail [] = {"-1",user.getLocation(),"1"};
	ArrayList plantillaEnviosSeleccionada = new ArrayList();
	ArrayList plantillaSeleccionada = new ArrayList();

	FacSerieFacturacionBean beanSerie = null;

	if (datosSerie!=null && datosSerie.size()>0) {
		beanSerie = (FacSerieFacturacionBean)datosSerie.elementAt(0);
		sAbreviatura = beanSerie.getNombreAbreviado();
		sDescripcion = beanSerie.getDescripcion();
		iPlantilla = beanSerie.getIdPlantilla();
		idSerieFacturacion = String.valueOf(beanSerie.getIdSerieFacturacion());
		enviarFacturas = beanSerie.getEnvioFactura();
		generarPDF = beanSerie.getGenerarPDF();
		observaciones = beanSerie.getObservaciones();
		
		String sWhere = " where ";
		sWhere += FacPlantillaFacturacionBean.T_NOMBRETABLA+"."+ FacPlantillaFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
			  " and "+
			  FacPlantillaFacturacionBean.T_NOMBRETABLA+"."+ FacPlantillaFacturacionBean.C_IDPLANTILLA+"="+iPlantilla;
		
		FacPlantillaFacturacionAdm admPlantilla = new FacPlantillaFacturacionAdm(user);
		Vector datosPlantilla = admPlantilla.select(sWhere);
		FacPlantillaFacturacionBean beanPlantilla = (FacPlantillaFacturacionBean)datosPlantilla.elementAt(0);
		sPlantilla = beanPlantilla.getDescripcion();
		
		if(beanSerie.getIdTipoPlantillaMail() != null && !beanSerie.getIdTipoPlantillaMail().equals("")){	
			plantillaEnviosSeleccionada.add(beanSerie.getIdTipoPlantillaMail()+","+user.getLocation() +",1");
		}	
	
	}

	String idContador="";
	String prefijo="";
	String contador="";
	String sufijo="";
	String prefijo_nuevo="";
	String contador_nuevo="";
	String sufijo_nuevo="";
	
	// datos seleccionados Combo
	ArrayList contadorSel = new ArrayList();
	
	if (datosContador!=null && datosContador.size()>0)
	{
		AdmContadorBean beanContador = (AdmContadorBean)datosContador.elementAt(0);
		contadorSel.add(beanContador.getIdContador());
		idContador = beanContador.getIdContador();
		prefijo = beanContador.getPrefijo();
		contador = beanContador.getContador().toString();
		sufijo = beanContador.getSufijo();
	}
	
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	

%>

<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DatosGeneralesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionConceptos.datosGenerales.cabecera" 
			localizacion="facturacion.datosGenerales.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->


		<script language="JavaScript">
	
			function actualiza() 
			{
				if (document.forms[0].envioFacturas.checked==true) {
					document.forms[0].generarPDF.checked=true;
					document.forms[0].generarPDF.disabled=true;
					document.forms[0].idTipoPlantillaMail.disabled=false;
				} else {
					document.forms[0].idTipoPlantillaMail.disabled=true;
					document.forms[0].generarPDF.disabled=false;
				}
				return false;
			}

			function configuraContador() 
			{
				var con = document.getElementById("conta")
				if (document.forms[0].configurarContador.checked==true) {
					con.style.display='block';
					comprobarNuevo();
				} else {
					con.style.display='none';
				}
				return false;
			}

			function comprobarNuevo() {
				if (document.forms[0].contadorExistente.value=="") {
					document.forms[0].prefijo_nuevo.disabled=false;
					document.forms[0].contador_nuevo.disabled=false;
					document.forms[0].sufijo_nuevo.disabled=false;
				} else {
					document.forms[0].prefijo_nuevo.disabled=true;
					document.forms[0].contador_nuevo.disabled=true;
					document.forms[0].sufijo_nuevo.disabled=true;
				}					
			}
			
			function refrescarLocal() {
				document.location = document.location;
			}
			
			
		</script>
	</head>

	<body  class="tablaCentralCampos" onLoad="configuraContador();actualiza();">
	
		<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->

		<!-- INICIO: CAMPOS DEL REGISTRO -->

		<!-- Comienzo del formulario con los campos -->
		<table class="tablaCentralCampos">
			<html:form action="/FAC_DatosGenerales.do" method="POST" focus="nombreAbreviado" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
				<html:hidden property="accion" value="<%=accion%>"/>
				<html:hidden property="ids" value=""/>

				<tr>
					<td>		
						<!-- SUBCONJUNTO DE DATOS -->
						<!-- Conjunto de campos recuadrado y con titulo -->
						<siga:ConjCampos leyenda="facturacion.serios.literal.seriesFacturacion">
							<table align="center" border="0">

								<tr>
									<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.nombreAbreviado"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm" property="nombreAbreviado" size="20" maxlength="20" styleClass="boxConsulta" value="<%=sAbreviatura%>" readonly="true"></html:text>
										<%} else {%>
											<html:text name="DatosGeneralesForm" property="nombreAbreviado" size="20" maxlength="20" styleClass="boxMayuscula" value="<%=sAbreviatura%>" readonly="false"></html:text>
										<%}%>
									</td>
								</tr>
													
								<tr> 
        								<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.descripcion"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm" property="descripcion" size="100" maxlength="100" styleClass="boxConsulta" value="<%=sDescripcion%>" readonly="true"></html:text>
										<%} else {%>
											<html:text name="DatosGeneralesForm" property="descripcion" size="100" maxlength="100" styleClass="boxCombo" value="<%=sDescripcion%>" readonly="false"></html:text>
										<%}%>
									</td>
								</tr>
														
								<tr> 
        								<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.plantilla"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm" property="plantilla" size="100" maxlength="100" styleClass="boxComboConsulta" value="<%=sPlantilla%>" readonly="true"></html:text>
										<%} else {
											String dato[] = new String[1];
											dato[0] = idInstitucion;
											vPlantilla.add(iPlantilla.toString());
										%>
											<siga:ComboBD nombre = "idPlantilla" tipo="cmbPlantilla" elementoSel ="<%=vPlantilla%>" clase="boxCombo" parametro="<%=dato%>"/>
										<%}%>
									</td>
								</tr>
								</table>
								<table border="0">
									<tr>
										<td class="labelText" style="text-align:left" >
											<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>&nbsp;&nbsp;
											<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
													<input type="checkbox" name="generarPDF" checked disabled>
											<% } else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
													<input type="checkbox" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> checked>
											<% } else { %>
													<input type="checkbox" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> >
											<% } %>
													&nbsp;&nbsp;&nbsp;
											<siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/>&nbsp;&nbsp;
											<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
													<input type="checkbox" name="envioFacturas" onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> checked>
											<% } else { %>
													<input type="checkbox" name="envioFacturas" onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> >
											<% } %>
										</td>
										
										<td id="titulo" class="labelText">
											<siga:Idioma key="envios.plantillas.literal.plantilla"/> 
										</td>
										<td>
											<siga:ComboBD nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="300" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>"/>
										</td>									
									</tr>
								</table>
								<table align="center" border="0">
								<tr>
								    <td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.observaciones"/>
									</td>
									<td colspan="4">
										<%if (!bEditable){%>
											<html:textarea name="DatosGeneralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" property="observaciones" style="width:630px;"  rows="3" onkeydown="cuenta(this,4000);" styleClass="boxConsulta" value="<%=observaciones%>" readonly="true"/>
										<%} else {%>
											<html:textarea name="DatosGeneralesForm" property="observaciones"  style="width:630px;"  rows="3" onkeydown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value="<%=observaciones%>" readonly="false"/>
										<%}%>
									</td>
								
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
		</table>
		<!-- FIN: CAMPOS DEL REGISTRO -->

<% if (accion.equals("nuevo")) {  %>

		<table class="tablaCentralCampos" style="display:none">
	
<% } else {  %>
		<table class="tablaCentralCampos">

<% }  %>

				<tr>
					<td>		
	
						<siga:ConjCampos leyenda="facturacion.serios.literal.contador">

							<table align="left" border="0">
								<tr>
									<td class="labelText" style="width:150px">
										<siga:Idioma key="facturacion.datosGenerales.literal.nombreContador"/>&nbsp;
									</td>
									<td  class="labelTextValue" style="width:250px">
										<html:text name="DatosGeneralesForm" property="idContador" size="20" maxlength="20" styleClass="boxConsulta" value="<%=idContador%>" readonly="true"></html:text>
									</td>
									<td class="labelText" style="width:150px">
										<siga:Idioma key="facturacion.datosGenerales.literal.contadorGenerico"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<html:text name="DatosGeneralesForm" property="prefijo" size="8" maxlength="10" styleClass="box" value="<%=prefijo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" property="contador" size="15" maxlength="15" styleClass="box" value="<%=contador%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" property="sufijo" size="8" maxlength="10" styleClass="box" value="<%=sufijo%>"  disabled="true"></html:text>
									</td>
								</tr>
<% if (accion.equals("ver")) { %>
	<input type="hidden" name="configurarContador" value="off">
<% } else { %>

								<tr>
									<td class="labelText"  style="text-align:left;width:150px">
										<siga:Idioma key="facturacion.datosGenerales.literal.configurarContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="checkbox" name="configurarContador" onclick="configuraContador();">
									</td>
								</tr>
<% }  %>
								<tr id="conta">
									<td class="labelText" style="text-align:left;width:150px">
										<siga:Idioma key="facturacion.datosGenerales.literal.existentes"/>&nbsp;
									</td>
									<td class="labelText">
										<siga:ComboBD nombre="contadorExistente" tipo="cmbContadorFacturacion" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=contadorSel %>" accion="comprobarNuevo()" />
									</td>
									<td class="labelText" style="width:150px">
										<siga:Idioma key="facturacion.datosGenerales.literal.nuevoContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<html:text name="DatosGeneralesForm" property="prefijo_nuevo" size="8" maxlength="10" styleClass="box" value="<%=prefijo_nuevo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" property="contador_nuevo" size="15" maxlength="15" styleClass="box" value="<%=contador_nuevo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" property="sufijo_nuevo" size="8" maxlength="10" styleClass="box" value="<%=sufijo_nuevo%>" disabled="true"></html:text>
									</td>
								</tr>
							</table>

						</siga:ConjCampos>
					</td>
				</tr>
		</table>

			</html:form>


		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
			<%if (!bEditable){%>
				<siga:ConjBotonesAccion botones="V" />
			<%} else {%>
				<siga:ConjBotonesAccion botones="V,G,R" />
			<%}%>
		<!-- FIN: BOTONES REGISTRO -->
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
			//Asociada al boton Volver
			function accionVolver() 
			{
				document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";	
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();				
			}

			//Asociada al boton Restablecer
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
		
			//Asociada al boton Guardar
			function accionGuardar() 
			{
				if (validateDatosGeneralesForm(document.DatosGeneralesForm)){		
					if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')) 
					{
						if (document.forms[0].accion.value=="editar")
						{
							document.forms[0].modo.value="modificar";
						}
						else if (document.forms[0].accion.value=="nuevo")
						{
							document.forms[0].target="submitArea";
							document.forms[0].modo.value="insertar";
						}	
										
						var datos = "";
						var chk = document.getElementsByName("chk");
						if (chk !="checkbox") {
							for (i = 0; i < chk.length; i++){
								if (chk[i].checked==1){
									datos=datos + chk[i].value + "%";						
								}	
							}		
						} else {
						   if (chk.checked==1){
								datos=datos + chk.value + "%";						
							}	
						}
						if (datos=="") {
							alert('<siga:Idioma key="Facturacion.mensajes.obligatoriaCuenta"/>');
							return false;
						}
						
						// Validacion de contadores
						if (document.forms[0].configurarContador.checked==1 &&
							document.forms[0].contadorExistente.value=="") {
							
							
							// se trata de un nuevo contador
							if (document.forms[0].prefijo_nuevo.length>8) {
								alert('<siga:Idioma key="Facturacion.mensajes.longitud.prefijo"/>');
								return false;
							}	
							if (document.forms[0].sufijo_nuevo.length>8) {
								alert('<siga:Idioma key="Facturacion.mensajes.longitud.sufijo"/>');
								return false;
							}	
		                    if (isNaN(document.forms[0].contador_nuevo.value)) {
								alert('<siga:Idioma key="Facturacion.mensajes.noNumerico.contador"/>');
								return false;
							}	
							if (document.forms[0].contador_nuevo.value=="") {
							
							    alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.contador"/>');
								return false;
							}	
						}

						if(document.DatosGeneralesForm.envioFacturas.checked){
							if(document.DatosGeneralesForm.idTipoPlantillaMail.value == ""){
							    alert('Debe rellenar el tipo de plantilla para el envío');
								return false;
							}
						}

						document.forms[0].ids.value = datos;
						document.forms[0].submit();
					 }
				}
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


		<!-- INICIO: LISTA RESULTADOS -->


			<% //if ((accion.equalsIgnoreCase("editar"))||(accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("insertar"))||(accion.equalsIgnoreCase("modificar"))){ %>
			<siga:TablaCabecerasFijas 
				   nombre="tablaResultados"
				   borde="1"
				   clase="tableTitle"				   
				   nombreCol="facturacion.devolucionManual.seleccion,facturacion.ficheroBancarioAbonos.literal.banco,censo.consultaDatosBancarios.literal.cuentaBancaria,Facturacion.bancos.comisionPropia,Facturacion.bancos.comisionAjena"
				   tamanoCol="5,55,20,10,10"
				   alto="100%"
				   modal="P">
				   				   
				<%
	    		if (request.getAttribute("bancosInstitucion") == null || ((Vector)request.getAttribute("bancosInstitucion")).size() < 1 )
		    	{
				%>
					<br><br>
					<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
					<br><br>
				<%
		    	}	    
			    else
		    	{ 
		    		Enumeration en = ((Vector)request.getAttribute("bancosInstitucion")).elements();					
					int recordNumber=1;
					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); 
	            		String sel = row.getString("SELECCIONADO");
	            		boolean bsel=false;
	            		if (sel!=null && !sel.equals("0")) {
	            			bsel=true;
	            		}
	            		
					%>
	            		
						<siga:FilaConIconos
							  fila='<%=String.valueOf(recordNumber)%>'
							  botones=''
							  modo='<%=accion%>'							  
							  visibleConsulta='no'
							  visibleEdicion='no'
							  visibleBorrado='no'
							  clase="listaNonEdit"
							  pintarespacio='no'
							  >
						  		
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString("IDINSTITUCION")%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString("COD_BANCO")%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString("IDSERIEFACTURACION")%>">
							<td>
								
								<input type="checkbox" value="<%=row.getString("BANCOS_CODIGO")%>" name="chk" <%=(accion.equals("nuevo") || bsel)?"checked":"" %> <%=(accion.equals("ver"))?"disabled":"" %> >
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("BANCO"))%>							
							</td>  	
							<td align="right">
								<%=row.getString("CUENTACONTABLE")%>							
							</td>  	
							<td align="right">
								<%=row.getString("COMISIONPROPIA")%>							
							</td>  								
							<td align="right">
								<%=row.getString("COMISIONAJENA")%>							
							</td>  								
						</siga:FilaConIconos>
					<% 
					recordNumber++;
					} 
				} %>
			</siga:TablaCabecerasFijas>												

	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>

</html>
