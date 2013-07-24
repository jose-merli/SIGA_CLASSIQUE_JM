<!DOCTYPE html>
<html>
<head>
<!-- consultaAbonosLineas.jsp -->
<!-- 
	 Muestra el desglose de un abono
	 VERSIONES:
	 miguel.villegas 11-03-2005 
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
		

	// Datos del cliente a visualizar
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono
	String modo=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	
	String contabilizado=(String)request.getAttribute("contabilizado"); // Obtengo si esta o no contabilizado
	String idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo si esta o no contabilizado
	Vector datosDesglose= new Vector();
	String iconos="";

	// Muestro o no las opciones de edición o borrado
	if (contabilizado.equalsIgnoreCase(ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA)){
		// RGG 25/11/2007 no permitimos modificar ni crear 
		// iconos="E,B";
		iconos="C";
	}
	
	double sumatorioNeto=0;
	double sumatorioIva=0;
	double sumatorioTotal=0;

	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	// Manejadores de BBDD
	ConsPLFacturacion cpl = new ConsPLFacturacion(usr);
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);

	// Obtencion de la informacon relacionada con el abono
	if (request.getAttribute("container") != null){	
		datosDesglose = (Vector)request.getAttribute("container");	
	}

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	String botonesAccion = "";
	
	// Muestro o no las opciones de nueva entrada
	if (contabilizado.equalsIgnoreCase(ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA)){
		// RGG 25/11/2007 no permitimos modificar ni crear 
		// botonesAccion="N";
	}
	
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	
	if (volver.equalsIgnoreCase("SI") && !busquedaVolver.equals("volverNo")) { 
		if (botonesAccion.equalsIgnoreCase("")){
			botonesAccion="V";
		}
		else{
			botonesAccion+=",V";
		}
	}

%>	

<%@page import="org.apache.struts.action.ActionMapping"%>


	<!-- HEAD -->
	

		<style type="text/css">

		</style>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="AbonosLineasForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		 	<% 	if (usr.getStrutsTrans().equals("FAC_GenerarAbonos")) {%>
						<siga:Titulo titulo="facturacion.pagos.lineas.cabecera"	localizacion="facturacion.abonos.localizacion"/>
			<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
					<siga:Titulo titulo="facturacion.pagos.lineas.cabecera"	localizacion="censo.facturacion.abonos.localizacion"/>
			<% }%>
		<!-- FIN: TITULO Y LOCALIZACION -->

	
	</head>

	<body class="tablaCentralCampos">
		
		<html:form action="<%=path%>" method="POST" target="submitArea" style="display:none">
				<html:hidden property ="modo"  styleId ="modo" value = ""/>
				<html:hidden property="idAbono"  styleId="idAbono"  value="<%=idAbono%>"/> 				
				<html:hidden property="idInstitucion"  styleId="idInstitucion"  value="<%=idInstitucion%>"/>
				<html:hidden property="idFactura"  styleId="idFactura"  value="<%=idFactura%>"/>	
			<!-- RGG: cambio a formularios ligeros -->
		</html:form>

						<siga:Table 
						   name="tablaDatos"
						   border="1"
						   columnNames="facturacion.lineasAbonos.literal.cantidad,facturacion.lineasAbonos.literal.Descripcion,
						   			  facturacion.lineasAbonos.literal.precioUnit,facturacion.datosGeneralesAbonos.literal.importeNeto,
						   			  facturacion.lineasAbonos.literal.iva,facturacion.datosGeneralesAbonos.literal.importeIva,
						   			  facturacion.lineasAbonos.literal.importeTotal,"
						   columnSizes="10,30,10,10,10,10,10,10"
						   modal="M">
						<%
				    	if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
					    {
						%>
							<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
						<%
				    	}	    
					    else
					    { %>
				    		<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
							int recordNumber=1;
							while (en.hasMoreElements())
							{
				            	Row row = (Row) en.nextElement();
				            	String impNeto="";
				            	String impIva="";
				            	String impTotal=""; %>
				            	
						        <%impNeto=(new Double(UtilidadesNumero.redondea((new Double(row.getString(FacLineaAbonoBean.C_CANTIDAD))).doubleValue()*(new Double(row.getString(FacLineaAbonoBean.C_PRECIOUNITARIO))).doubleValue(),2))).toString();%>
				            	<%impIva=(new Double(UtilidadesNumero.redondea(((new Double(impNeto)).doubleValue()*(new Double(row.getString(FacLineaAbonoBean.C_IVA))).doubleValue())/(new Double("100").doubleValue()),2))).toString();%>
				            	<%impTotal=new Double(UtilidadesNumero.redondea(new Double(impNeto).doubleValue() + new Double(impIva).doubleValue(),2)).toString();%>
						        <%sumatorioNeto+=new Double(impNeto).doubleValue();%>
				            	<%sumatorioIva+=new Double(impIva).doubleValue();%>
				            	<%sumatorioTotal+=new Double(impTotal).doubleValue();%>
						        
								<siga:FilaConIconos
									  fila='<%=String.valueOf(recordNumber)%>'
									  botones='<%=iconos%>'
									  visibleConsulta="no"
									  modo='<%=modo%>'
									  clase="listaNonEdit">
									  
									<td>
										<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idAbono%>">
										<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=idInstitucion%>">
										<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(FacLineaAbonoBean.C_NUMEROLINEA)%>">
										<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=idFactura%>">
										<%=UtilidadesString.mostrarDatoJSP(row.getString(FacLineaAbonoBean.C_CANTIDAD))%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(row.getString(FacLineaAbonoBean.C_DESCRIPCIONLINEA))%>
									</td>
									<td align="right">
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(row.getString(FacLineaAbonoBean.C_PRECIOUNITARIO)))%>&nbsp;&euro;
									</td>
									<td align="right">
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(impNeto))%>&nbsp;&euro;
									</td>
									<td align="right">
										<%=UtilidadesString.mostrarDatoJSP(row.getString(FacLineaAbonoBean.C_IVA))%>
									</td>
									<td align="right">
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(impIva))%>&nbsp;&euro;
									</td>
									<td align="right">
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(impTotal))%>&nbsp;&euro;
									</td>
								</siga:FilaConIconos>
								<% recordNumber++;%>
							<%	} %>
							<tr class="listaNonEditSelected" style="height:30px">
								<td>
									&nbsp;
								</td>
								<td align="right">
									<b><siga:Idioma key="facturacion.lineasAbonos.literal.total"/></b>
								</td>
								<td>
									&nbsp;
								</td>
								<td align="right">
									<b><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(new Double(UtilidadesNumero.redondea (sumatorioNeto, 2)).toString()))%>&nbsp;&euro;</b>
								</td>
								<td>
									&nbsp;
								</td>
								<td align="right">
									<b><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(new Double(UtilidadesNumero.redondea (sumatorioIva, 2)).toString()))%>&nbsp;&euro;</b>
								</td>
								<td align="right">
									<html:hidden property="importeTotal" value="<%=new Double(UtilidadesNumero.redondea (sumatorioTotal, 2)).toString()%>"/>	
									<b><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(new Double(UtilidadesNumero.redondea (sumatorioTotal, 2)).toString()))%>&nbsp;&euro;</b>
								</td>
								<td>
									&nbsp;
								</td>
							</tr>	
						<%	} %>
						</siga:Table>

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
			
		<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modo="<%=modo%>" clase="botonesDetalle"/>
			
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
		
			//Funcion asociada a boton nuevo -->
			function accionNuevo() 
			{							
				document.forms[0].modo.value='nuevo';
				var resultado = ventaModalGeneral("AbonosLineasForm","M");
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
				//document.forms[0].submit();
			}

			function refrescarLocal() 
			{
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
