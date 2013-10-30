<!DOCTYPE html>
<html>
<head>
<!-- consultaDatosGeneralesAbonos.jsp -->
<!-- 
	 Muestra los datos generales de un abono
	 VERSIONES:
	 miguel.villegas 10-03-2005 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
String path = actionMapping.getPath();
	String volver = request.getAttribute("volver")==null?"NO":(String)request.getAttribute("volver");

	String fecha="";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	// Datos a visualizar
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono
	String numeroAbono=(String)request.getAttribute("NUMEROABONO"); // Obtengo el número del abono
	String modo=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion
	String idFactura = (String)request.getAttribute("IDFACTURA"); // Obtengo el identificador de la factura
	String idPagoJG = (String)request.getAttribute("idPagoJG"); // Obtengo el identificador del pago
	Hashtable datosAbono= new Hashtable();

	
	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	// Manejadores de BBDD
	ConsPLFacturacion cpl = new ConsPLFacturacion(usr);
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);

	String totalNeto="";
	String totalIva="";
	String total="";

	// Obtencion de la informacon relacionada con el abono
	if (request.getAttribute("container") != null){	
		datosAbono = (Hashtable)request.getAttribute("container");	
		totalNeto=new Double(UtilidadesNumero.redondea(new Double((String)datosAbono.get("TOTALNETO")).doubleValue(),2)).toString();
		totalIva=new Double(UtilidadesNumero.redondea(new Double((String)datosAbono.get("TOTALIVA")).doubleValue(),2)).toString();
		total=new Double(UtilidadesNumero.redondea(new Double((String)datosAbono.get("TOTAL")).doubleValue(),2)).toString();
	}

	//String idFactura = (String)datosAbono.get(FacAbonoBean.C_IDFACTURA);

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	String botonesAccion = "G,R";
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	
	if ((volver!=null && volver.equalsIgnoreCase("SI")) && !busquedaVolver.equals("volverNo")) { 
		botonesAccion="V,G,R";
	}
	String informeUnico =(String) request.getAttribute("informeUnico");
%>	

<%@page import="org.apache.struts.action.ActionMapping"%>


	<!-- HEAD -->
	
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="AbonosDatosGeneralesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->

 			<% 	if (usr.getStrutsTrans().equals("FAC_GenerarAbonos")) {%>
 					<siga:Titulo titulo="facturacion.pagos.datosGenerales.cabecera"	localizacion="facturacion.abonos.localizacion"/>
			<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
					<siga:Titulo titulo="facturacion.pagos.datosGenerales.cabecera"	localizacion="censo.facturacion.abonos.localizacion"/>
			<% }%>

		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
<input type="hidden" id = "idPagoJG" value ="<%=idPagoJG%>" />
		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	
		<!-- CAMPOS DEL REGISTRO -->
		<table class="tablaCentralCampos" align="center">		

			<html:form action="<%=path%>" method="POST">

				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idAbono" value="<%=idAbono%>"/> 				
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>	
				<html:hidden property="idPersona" value="<%=(String)datosAbono.get(FacAbonoBean.C_IDPERSONA)%>"/>
				<html:hidden property="idFactura" value="<%=(String)datosAbono.get(FacAbonoBean.C_IDFACTURA)%>"/>		

				<tr>				
					<td width="100%" align="center">

						<!-- SUBCONJUNTO DE DATOS -->
						<siga:ConjCampos leyenda="facturacion.datosGeneralesAbonos.literal.cabecera">

						<% if (request.getAttribute("container") == null){%>
							<div class="notFound">
			<br><br>
	   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
			</div>
						<% } else { %>						
							<table align="center" width="100%">
								<tr>				
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.numeroAbono"/>&nbsp;
									</td>				
									<td>
										<html:text property="numeroAbono" size="10" styleClass="boxConsulta" value="<%=(String)datosAbono.get(FacAbonoBean.C_NUMEROABONO)%>" readOnly="true"></html:text>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>&nbsp;
									</td>
									<td>
									 	<% fecha=GstDate.getFormatedDateShort("",(String)datosAbono.get(FacAbonoBean.C_FECHA));%>
										<% if (fecha.equalsIgnoreCase("")){ %>									 	
											<html:text property="fecha" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
										<% }else{ %>	
											<html:text property="fecha" size="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>
										<% } %>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.contabilizado"/>&nbsp;
									</td>
									<td>									
										<% if (((String)datosAbono.get(FacAbonoBean.C_CONTABILIZADA)).equalsIgnoreCase(ClsConstants.FACTURA_ABONO_CONTABILIZADA)){ %>
												<input type="checkbox" name="contabilizada" value="<%=ClsConstants.FACTURA_ABONO_CONTABILIZADA%>" checked disabled>
										<% }else{ %>
												<input type="checkbox" name="contabilizada" value="<%=ClsConstants.FACTURA_ABONO_CONTABILIZADA%>" disabled>
										<% } %>
									</td>									
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.estado"/>
									</td>
									<td>							
										<html:text property="estado" size="40" styleClass="boxConsulta" value="<%=cpl.obtenerEstadoFacAbo(new Integer(idInstitucion).intValue(),new Long(idAbono).longValue(),ConsPLFacturacion.ABONO)%>" readOnly="true"></html:text>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.cliente"/>&nbsp;
									</td>
									<td colspan="7">
										<html:text property="cliente" size="60" styleClass="boxConsulta" value="<%=personaAdm.obtenerNombreApellidos((String)datosAbono.get(FacAbonoBean.C_IDPERSONA))%>" readOnly="true"></html:text>
									</td>
								</tr>
							</table>								
						<% } %>																				
						</siga:ConjCampos>
					</td>
				</tr>
				<tr>				
					<td width="100%" align="center">

						<!-- SUBCONJUNTO DE DATOS -->
						<siga:ConjCampos leyenda="facturacion.altaAbonos.literal.motivos">

						<% if (request.getAttribute("container") == null){%>
							<div class="notFound">
			<br><br>
	   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
			</div>
						<% } else { %>						
							<table align="center" width="100%">
								<tr>				
									<td class="labelText" width="150">
										<siga:Idioma key="facturacion.altaAbonos.literal.motivos"/>&nbsp;(*)
									</td>				
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<html:textarea property="motivos"
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;" 
												styleClass="boxConsulta" value="<%=(String)datosAbono.get(FacAbonoBean.C_MOTIVOS)%>" readOnly="true"></html:textarea>
										<% } else { %>
											<html:textarea property="motivos" 
												onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"												
												styleClass="box" value="<%=(String)datosAbono.get(FacAbonoBean.C_MOTIVOS)%>"></html:textarea>
										<% } %>
									</td>
									<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Observaciones"/></td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<html:textarea property="observaciones" 
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"
												styleClass="boxConsulta" value="<%=(String)datosAbono.get(FacAbonoBean.C_OBSERVACIONES)%>"/>
										<% } else { %>
											<html:textarea property="observaciones" 
												onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"
												styleClass="box" value="<%=(String)datosAbono.get(FacAbonoBean.C_OBSERVACIONES)%>"/>
										<% } %>
									</td>
								</tr>
							</table>								
						<% } %>																				
						</siga:ConjCampos>
					</td>
				</tr>
				
				<tr>				
					<td width="100%" align="center">

						<!-- SUBCONJUNTO DE DATOS -->
						<siga:ConjCampos leyenda="facturacion.datosGeneralesAbonos.literal.totales">

						<% if (request.getAttribute("container") == null){%>
							<div class="notFound">
			<br><br>
	   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
			</div>
						<% } else { %>						
							<table align="center" width="100%">
								<tr>				
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.importeNeto"/>&nbsp;
									</td>				
									<td class="labelTextValue">
										<html:text property="importeNeto" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(totalNeto)%>' readOnly="true"></html:text>&nbsp;&euro;
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.importeIva"/>&nbsp;
									</td>
									<td class="labelTextValue">
										<html:text property="importeIva" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(totalIva)%>' readOnly="true"></html:text>&nbsp;&euro;
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGeneralesAbonos.literal.importeAbono"/>&nbsp;
									</td>
									<td class="labelTextValue">
										<html:text property="importeIva" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(total)%>' readOnly="true"></html:text>&nbsp;&euro;
									</td>									
								</tr>
							</table>								
						<% } %>																				
						</siga:ConjCampos>
					</td>
				</tr>

			</html:form>		
			<tr  width="100%">
				<td>
					<br>&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" name="idButton" id ="idButton" value="<siga:Idioma key="general.boton.download"/>" onclick="download();" class="button">	
				</td>
			</tr>
			</table>		

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="ABONO"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>

		<siga:ConjBotonesAccion clase="botonesDetalle" botones="<%=botonesAccion%>" modo="<%=modo%>" />

		<script language="JavaScript">
		
		
		
		
		
		
		
		
		
		function download()
		{
			sub();
			
			if(document.getElementById("idPagoJG").value!=''){
				
			
				
				idPago = document.getElementById("idPagoJG").value;
				idPersona = document.AbonosDatosGeneralesForm.idPersona.value;
				var idInstitucion = document.AbonosDatosGeneralesForm.idInstitucion.value;
				
				datos = "idPersona=="+idPersona+"##idPago==" +idPago + "##idInstitucion==" +idInstitucion + "##idTipoInforme==CPAGO%%%";
				
				document.InformesGenericosForm.datosInforme.value =datos;
				document.InformesGenericosForm.idTipoInforme.value = 'CPAGO';
				
				
				
			}else{
				var idInst = document.AbonosDatosGeneralesForm.idInstitucion.value;
				var idAbono = document.AbonosDatosGeneralesForm.idAbono.value;
				datos = 'idAbono=='+ idAbono+ "##idinstitucion=="+ idInst + "##idTipoInforme==ABONO%%%";
				
				document.InformesGenericosForm.datosInforme.value =datos;
				document.InformesGenericosForm.idTipoInforme.value = 'ABONO';
			
				
			}
			if(document.getElementById("informeUnico").value=='1'){
				document.InformesGenericosForm.submit();
			}else{
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
				   	fin();
			   	}else {
			   		fin();
			   	}
			   	
			}
		

		}
			
		
			//Asociada al boton Restablecer
			function accionRestablecer() 
			{		
				document.forms[0].reset();	
			}
		
			//Asociada al boton Guardar -->
			function accionGuardar() 
			{
				sub();		
				if (document.AbonosDatosGeneralesForm.observaciones.value.length > 255) {
					var mensaje = "<siga:Idioma key="messages.censo.historico.motivoLargo"/>";
					alert (mensaje);
					fin();
					return false;
				}
			
				if (validateAbonosDatosGeneralesForm(document.AbonosDatosGeneralesForm)){
					document.forms[0].modo.value = "modificar";
					document.forms[0].target = "submitArea";
					document.forms[0].submit();
				}else{
				
					fin();
					return false;
				
				}	
			}

			function refrescarLocal () {
				document.location.reload();
			}
			
			
		</script>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>		

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
