<!-- consultaPagosAbonos.jsp -->
<!-- 
	 Muestra la pestranha de pagos de los abonos
	 VERSIONES:
	 miguel.villegas 16-03-2005 
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
<%@ page import="com.siga.tlds.FilaExtElement"%>
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
	String idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el identificador de la institucion
	
	Vector datosPagos= new Vector();
	Hashtable datosTotales= new Hashtable();
	
	String total="0";
	String totalAbonado="0";
	String pendiente="0";

	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	// Manejadores de BBDD
	ConsPLFacturacion cpl = new ConsPLFacturacion(usr);
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);

	// Obtencion de la informacon relacionada con los datos de pago
	if (request.getAttribute("container") != null){	
		datosPagos = (Vector)request.getAttribute("container");	
	}
	
	// Obtencion de la informacon relacionada con los totales
	if (request.getAttribute("totales") != null){	
		datosTotales = ((Row)((Vector)request.getAttribute("totales")).firstElement()).getRow();		
		total=new Double(UtilidadesNumero.redondea(new Double((String)datosTotales.get("TOTAL")).doubleValue(),2)).toString();
		totalAbonado=new Double(UtilidadesNumero.redondea(new Double((String)datosTotales.get("TOTALABONADO")).doubleValue(),2)).toString();
		pendiente=new Double(UtilidadesNumero.redondea(new Double((String)datosTotales.get("PENDIENTE")).doubleValue(),2)).toString();
	}

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	String botonesAccion = "";
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	
	if (volver.equalsIgnoreCase("SI") && !busquedaVolver.equals("volverNo")) { 
		botonesAccion="V";
	}

%>	

<%@page import="org.apache.struts.action.ActionMapping"%>
<html>

	<!-- HEAD -->
	<head>

		</style>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="AbonosPagosForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}
			
			<!-- Asociada al icono ModificarDatos -->						
			 function datosImpresion(fila) {
			   var datos;
			   datos = document.getElementById('tablaDatosDinamicosD');
			   datos.value = ""; 
			   preparaDatos(fila, 'tabladatos', datos);
			   
			   document.forms[0].modo.value = "datosImpresion";
			   ventaModalGeneral(document.forms[0].name,"P");

			 }					
		
		</script>
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		 	<% 	if (usr.getStrutsTrans().equals("FAC_GenerarAbonos")) {%>
						<siga:Titulo titulo="facturacion.pagos.pagos.cabecera"	localizacion="facturacion.abonos.localizacion"/>
			<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
					<siga:Titulo titulo="facturacion.pagos.pagos.cabecera"	localizacion="censo.facturacion.abonos.localizacion"/>
			<% }%>	
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	
		<!-- CAMPOS DEL REGISTRO -->
			<html:form action="<%=path%>" method="POST" target="submitArea" style="display:none">
				<html:hidden property ="modo" value = ""/>
				<html:hidden property="idAbono" value="<%=idAbono%>"/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="pagoPendiente" value="<%=pendiente%>"/>
				<html:hidden property="idFactura" value="<%=idFactura%>"/>

		</html:form>
		

							
						<siga:Table 
						   name="tablaDatos"
						   border="1"
						   columnNames="facturacion.abonosPagos.literal.fecha,facturacion.abonosPagos.literal.medioPago,
						   			  facturacion.abonosPagos.literal.importe,"
						   columnSizes="10,60,20,10"
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
							String botonesMostrados="";	
							while (en.hasMoreElements())
							{
				            	Row row = (Row) en.nextElement();
								FilaExtElement[] elementos=new FilaExtElement[1];
								if (row.getString("MODO").equalsIgnoreCase("CAJA")){
		  			 				elementos[0]=new FilaExtElement("datosImpresion","datosImpresion",SIGAConstants.ACCESS_FULL);
		  			 			}
				            	%>

								<siga:FilaConIconos
									  fila='<%=String.valueOf(recordNumber)%>'
									  botones='<%=botonesMostrados%>'
									  elementos='<%=elementos%>'
									  modo='editar'
									  visibleConsulta='no'
									  visibleEdicion='no'
									  visibleBorrado='no'
									  pintarEspacio='no'
									  clase="listaNonEdit">
									  
									<td align="left">
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idAbono%>">
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=idInstitucion%>">
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value='<%=row.getString("IDENTIFICADOR")%>'>
										<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=idFactura%>">
									 	<% fecha=GstDate.getFormatedDateShort("",row.getString("FECHA"));%>
										<%=UtilidadesString.mostrarDatoJSP(fecha)%>
									</td>
									<td align="left">
										<%=UtilidadesString.mostrarDatoJSP(row.getString("MODO"))%>
									</td>
									<td align="right">
										<% String valorMostrado=new Double(UtilidadesNumero.redondea(new Double(row.getString("IMPORTE")).doubleValue(),2)).toString();%>
										<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(valorMostrado))%>&nbsp;&euro;
									</td>
								</siga:FilaConIconos>
								<% recordNumber++;%>
							<%	} %>
						<%	} %>
						</siga:Table>

					<div id='tablaImportes' style='position:absolute;width:360px;left:545px;height:100px; bottom: 60px;'>
						<fieldset>
						<table width="100%" border="0">
							<tr>
								<td class="labelText"  width="25%">
									<siga:Idioma key="facturacion.abonosPagos.literal.importeTotalAbono"/>&nbsp;
								</td>				
								<td class="labelTextNum" width="10%">
									<html:text property="importeAbono" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(total)%>' readOnly="true"></html:text>&nbsp;&euro;
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.abonosPagos.literal.totalAbonado"/>&nbsp;
								</td>				
								<td class="labelTextNum">
									<html:text property="importeAbonado" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(totalAbonado)%>' readOnly="true"></html:text>&nbsp;&euro;
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.abonosPagos.literal.pendienteAbonado"/>&nbsp;
								</td>				
								<td class="labelTextNum">
									<html:text property="importePendiente" size="13" styleClass="boxConsultaNumber" value='<%=UtilidadesNumero.formatoCampo(pendiente)%>' readOnly="true"></html:text>&nbsp;&euro;
								</td>
							</tr>
						</table>
						</fieldset>
					</div>

					<div id='tablaImportes' style='position:absolute;width:100%; height:30px; bottom: 30px;'>
						
						<table align="center">
							<tr>
							<% if ((new Double(pendiente).doubleValue()>0)&&(modo.equalsIgnoreCase("editar"))){ %>
								<td class="misBotones" width="10%">
									<input type="button" alt="compensarFactura"  id="idButton" onclick="return compensacionFacturaManual();" class="button" value='<siga:Idioma key="facturacion.abonosPagos.boton.compensacionManual"/>'>
								</td>
								<td class="misBotones" width="10%">
									<input type="button" alt="compensarFactura"  id="idButton" onclick="return compensarFactura();" class="button" value='<siga:Idioma key="facturacion.abonosPagos.boton.compensacion"/>'>
								</td>
								<td class="misBotones" width="10%">
									<input type="button" alt="pagarCaja"  id="idButton" onclick="return pagarCaja();" class="button" value='<siga:Idioma key="facturacion.abonosPagos.boton.pagoCaja"/>'>
								</td>
								<td class="misBotones" width="30%">
								<% if (!((String)datosTotales.get(FacAbonoBean.C_IDCUENTA)).equalsIgnoreCase("")){ %>
									<input type="button" alt="pagarBanco"  id="idButton" onclick="return pagarBanco();" class="button" value='<siga:Idioma key="facturacion.abonosPagos.boton.cambioBanco"/>'>
								<% } else { %>
									<input type="button" alt="pagarBanco"  id="idButton" onclick="return pagarBanco();" class="button" value='<siga:Idioma key="facturacion.abonosPagos.boton.pagoBanco"/>'>
								<% } %>
								</td>
						
								
						<% } else { %>
							<td class="misBotones">
								&nbsp;
							</td>
						<% } %>
							
							</tr>
						</table>
					</div>			

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
		
			<!-- Funcion asociada a boton pagarCaja -->
			function pagarCaja() 
			{			
				sub();				
				document.forms[0].modo.value='pagarCaja';
				var resultado = ventaModalGeneral("AbonosPagosForm","P");
				fin();
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
			}
			
			<!-- Funcion asociada a boton compensarFactura -->
			function compensarFactura() 
			{	
				sub();						
				document.forms[0].modo.value='compensarFactura';
				document.forms[0].submit();
			}
			function compensacionFacturaManual() 
			{
				sub();							
				document.forms[0].modo.value='compensacionFacturaManual';
				// Abro ventana modal y refresco si necesario
				var resultado = ventaModalGeneral(document.AbonosPagosForm.name,"M");
				fin();
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
	
			}
			
			
			<!-- Funcion asociada a boton pagarCaja -->
			function pagarBanco() 
			{						
				sub();	
				document.forms[0].modo.value='pagarBanco';
				var resultado = ventaModalGeneral("AbonosPagosForm","P");
				fin();
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
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
