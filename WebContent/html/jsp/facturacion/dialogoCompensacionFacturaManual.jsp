<!-- dialogoCompensacionFacturaManual.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>

<!-- JSP -->


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// Institucion, fecha e idAbono provisional
	String idAbono=(String)request.getAttribute(FacAbonoBean.C_IDABONO); // Obtengo el identificador del abono
	String idInstitucion=(String)request.getAttribute(FacAbonoBean.C_IDINSTITUCION); // Obtengo el identificador de la institucion	
	String fechaAbono = (String)request.getAttribute(FacAbonoBean.C_FECHA);
	String importePendiente=(String)request.getAttribute("PAGOPENDIENTE"); // Obtengo el importe pendiente	
	String idPersona=(String)request.getAttribute(FacAbonoBean.C_IDPERSONA); // Obtengo el identifiador de persona
	String cliente = (String)request.getAttribute("CLIENTE"); // Obtengo el identifiador de persona
	String numeroAbono=(String)request.getAttribute(FacAbonoBean.C_NUMEROABONO); // Obtengo el numero del abono provisional
	
    // Botones a mostrar
	String botones = "Y,C";
%>

<%@page import="java.util.Properties"%>
<%@page import="com.siga.beans.FacAbonoBean"%>
<%@page import="com.siga.beans.FacFacturaBean"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.Utilidades.UtilidadesNumero"%>
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="compensacionAbonosPagosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	
	</head>


	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="facturacion.abonos.compensacionManual.titulo"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalMedia" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->
			<html:form action="/FAC_AbonosPagos.do" method="POST" target="submitArea">
			<table  class="tablaCentralCamposMedia"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.abonos.compensacionManual.datosAbono">
						<table class="tablaCampos" align="center">
							
							
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = ""/>
								<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
								<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
								<html:hidden property = "idFacturaCompensadora" value = ""/>
								<html:hidden property = "idAbono" value ="<%=idAbono%>"/>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.numeroAbono"/>
									</td>
									<td class="labelTextValor">
									
										<%=numeroAbono %>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.fechaAbono"/>
									</td>	
									<td class="labelTextValor">
									
										<%=GstDate.getFormatedDateShort("",fechaAbono) %>
									</td>
								</tr>
								
																
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.clienteAbono"/>
									</td>
									<td class="labelTextValor" colspan ="3">
										<%=cliente %>
									</td>
									
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.importePendienteAbono"/>
										
									</td>
									<td class="labelTextValor" colspan ="3">
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importePendiente)) %>
									</td>
									
								</tr>
								
								
								
									
						</table>
						</siga:ConjCampos>
						<siga:ConjCampos leyenda="facturacion.abonos.compensacionManual.datosFactura">
						<table class="tablaCampos" align="center">
							<tr>
									<td  width="50%">
										
									</td>
									<td  width="15%">
										
									</td>
									<td width="20%">
										
									</td>
									<td width="15%">
										
									</td>
									
								</tr>
							<tr>
									<td class="labelText" width="50%">
										<siga:Idioma key="facturacion.abonos.compensacionManual.numeroFacturaCompensadora"/>
										(*)
									</td>
									<td class="labelTextValor" width="15%">
										<html:text property="numFacturaCompensadora" styleClass="box" size="10" maxlength="20" value="" onchange="return validarFacturaCompensacionManual();"></html:text>
									</td>
									<td class="labelText" width="20%">
										<siga:Idioma key="facturacion.abonos.compensacionManual.fechaEmisionFacturaCompensadora"/>
									</td>
									<td class="labelTextValor" width="15%">
										<html:text property="fechaFacturaCompensadora" styleClass="boxConsulta" size="10" maxlength="20" value="" readOnly="true"></html:text>
									</td>
									
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.importeFacturaCompensadora"/>
										
									</td>
									<td class="labelTextValor">
										
										<html:text property="importeFacturaCompensadora" size="30" maxlength="10" styleClass="boxConsulta" readOnly="true" value=""></html:text>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.abonos.compensacionManual.estadoFacturaCompensadora"/>
									</td>
									<td class="labelTextValor">
										
										<html:text property="estadoFacturaCompensadora" size="30" maxlength="10" styleClass="boxConsulta" readOnly="true" value=""></html:text>
									</td>
									
								</tr>
								
								
							
						</table>
						</siga:ConjCampos>						
					</td>
				</tr>
			</table>
			</html:form>
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
	
				<!-- Asociada al boton GuardarCerrar -->
				function accionGuardarCerrar() 
				{
				
					sub();					
					if (validateCompensacionAbonosPagosForm(document.AbonosPagosForm)){
							document.AbonosPagosForm.modo.value="compensarFacturaManual";
							document.AbonosPagosForm.submit();
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
				
				
				
				<!-- Asociada al evento onchange numFactura -->
				function validarFacturaCompensacionManual() {
					sub();
					if (document.AbonosPagosForm.numFacturaCompensadora.value!=undefined){
						document.AbonosPagosForm.numFacturaCompensadora.value = trim(document.AbonosPagosForm.numFacturaCompensadora.value);
						document.AbonosPagosForm.target="submitArea";
						document.AbonosPagosForm.modo.value="validarFacturaCompensacionManual";
						document.AbonosPagosForm.submit();
					}else
						fin();
				}
				function refrescarLocal(){
					
					top.cierraConParametros("NORMAL");
				
				}
			
			</script>
		</div>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
