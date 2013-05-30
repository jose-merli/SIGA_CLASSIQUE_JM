<!-- facturasMoroso.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>



<%@ page import="java.util.*"%>

<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.Row"%>




<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	//Vector vDatos = (Vector)request.getAttribute("datos");
	Vector vLineasFactura = (Vector) request
			.getAttribute("lineasFactura");
	Row filaFactura = (Row) request.getAttribute("filaFactura");
	//Strings recuperados de los registros

	String fechaFactura = filaFactura.getString("FECHAEMISION");
	String nFactura = filaFactura.getString("NUMEROFACTURA");

	String netoFactura = filaFactura.getString("NETO");

	String totalIvaFactura = filaFactura.getString("TOTALIVA");

	String totalFactura = filaFactura.getString("TOTAL");

	String	pagadoFactura = filaFactura.getString("PAGADO");

	String deudaFactura = filaFactura.getString("DEUDA");

	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso = user.getAccessType();

	//Recupero datos del moroso
	String nombreApellidos = (String) request
			.getAttribute("nombreApellidos");
	String numColegiado = (String) request.getAttribute("numColegiado");
	String fechaDesde = (String) request.getAttribute("fechaDesde");
	String fechaHasta = (String) request.getAttribute("fechaHasta");

	//Sumas de los resultados

	double importeTotal = 0, ivaTotal = 0,sumaTotal = 0, sumaAnticipado = 0;

	

	request.removeAttribute("datos");
%>

<%@page import="com.atos.utils.UsrBean"%>
<%@page import="com.atos.utils.GstDate"%>
<%@page import="com.siga.tlds.FilaExtElement"%>
<%@page import="com.siga.administracion.SIGAConstants"%>
<html>
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">	
	
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
			function MM_swapImgRestore(){
				//meto esto para machacar el error de metod de Siga.js
			}
				
			function download(fila)
			{
				
				fila = fila-1;
				var envioDoc = "idEnvioDoc"+fila;
				var institucion = "idInstitucion"+fila;
				var documento = "idDocumento"+fila;
				var path = "pathDocumento"+fila;
				
				var idEnvioDoc = document.getElementById(envioDoc);
				var idInstitucion = document.getElementById(institucion);
				var idDocumento = document.getElementById(documento);
				var pathDocumento = document.getElementById(path);
				
				document.ConsultaMorososForm.idEnvioDoc.value = idEnvioDoc.value;
				document.ConsultaMorososForm.idInstitucion.value = idInstitucion.value; 
				document.ConsultaMorososForm.idDocumento.value = idDocumento.value;
				document.ConsultaMorososForm.pathDocumento.value = pathDocumento.value;
				document.ConsultaMorososForm.modo.value = "download";
				
				document.ConsultaMorososForm.submit();
				
				
				return false;
				
				
					
			}
			
		</script>

<!-- FIN: SCRIPTS BOTONES -->

</head>

<body>

<html:form action="/FAC_ConsultaMorosos.do" method="POST" target="modal" style="display:none">			
		    <html:hidden property = "modo" value = "download"/>
			
			<html:hidden property = "idEnvioDoc" value = ""/>
			<html:hidden property = "idInstitucion" value = ""/>
			<html:hidden property = "idDocumento" value = ""/>
			<html:hidden property = "pathDocumento" value = ""/>
			
		</html:form>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
			key="facturacion.consultamorosos.literal.titulo" /></td>
	</tr>
</table>


<table class="tablaCentralCampos" align="center">
	<tr>
		<td><siga:ConjCampos
			leyenda="facturacion.consultamorosos.literal.titulo">

			<table class="tablaCampos" align="center">
				<tr>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.deudasde" /></td>
					<td><html:text name="ConsultaMorososForm" property="nombre"
						styleClass="boxConsulta" readonly="true" /></td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.ncolegiado" /> :&nbsp;</td>
					<td><html:text name="ConsultaMorososForm"
						property="numColegiado" styleClass="boxConsulta" readonly="true" />
					</td>
					<logic:notEmpty name="ConsultaMorososForm" property="fechaDesde">
						<td class="labelText"><siga:Idioma
							key="facturacion.consultamorosos.literal.desde" />: &nbsp;</td>
						<td><html:text name="ConsultaMorososForm"
							property="fechaDesde" styleClass="boxConsulta" readonly="true" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="ConsultaMorososForm" property="fechaHasta">
						<td class="labelText"><siga:Idioma
							key="facturacion.consultamorosos.literal.hasta" /> : &nbsp;</td>
						<td><html:text name="ConsultaMorososForm"
							property="fechaHasta" styleClass="boxConsulta" readonly="true" />
						</td>
					</logic:notEmpty>
				</tr>


			</table>
		</siga:ConjCampos></td>
	</tr>
	<tr>
		<td><siga:ConjCampos leyenda="Detalle de factura">
			<table>
				<tr>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.fecha" />:</td>
					
					<td class="labelTextValue"><%=fechaFactura%>
					</td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.nfactura" /></td>
					<td class="labelTextValue"><%=nFactura%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.totalneto" />:</td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero
								.redondea(netoFactura, 2))%>&nbsp;&euro;</td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.totaliva" />:</td>
					<td class="labelTextValue" ><%=UtilidadesNumero.formatoCampo(UtilidadesNumero
								.redondea(totalIvaFactura, 2))%>&nbsp;&euro;</td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.total" />:</td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero
								.redondea(totalFactura, 2))%>&nbsp;&euro;</td>
				</tr>
				<tr>

					<td></td>
					<td></td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.facturapagado" /></td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero
								.redondea(pagadoFactura, 2))%>&nbsp;&euro;</td>
					<td class="labelText"><siga:Idioma
						key="facturacion.consultamorosos.literal.pendientepago" /></td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero
								.redondea(deudaFactura, 2))%>&nbsp;&euro;</td>
				</tr>
			</table>

		</siga:ConjCampos></td>
	</tr>
</table>


<siga:Table 
	name="tablaResultados" 
	border="2"
	columnNames="facturacion.lineasFactura.literal.Orden,
			   						facturacion.lineasFactura.literal.Descripcion,
			   						facturacion.lineasFactura.literal.Cantidad,
			   						facturacion.lineasFactura.literal.Precio,
			   						facturacion.lineasFactura.literal.IVA,
										facturacion.lineasFactura.literal.Total,
			   						facturacion.lineasFactura.literal.Anticipado,"
	columnSizes="9,40,7,9,7,9,9" 
	modal="M"
	fixedHeight="250">

	<%
					if ((vLineasFactura != null) && (vLineasFactura.size() > 0)) {
							int i =1;
							for ( i = 1; i <= vLineasFactura.size(); i++) {
							%>
<siga:FilaConIconosExtExt nombreTablaPadre="tablaResultados" pintarEspacio='no' fila="<%=String.valueOf(i)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
		<%
							
							
								FacLineaFacturaBean linea = (FacLineaFacturaBean) vLineasFactura
										.get(i - 1);
								if (linea != null) {
									Double importe = linea.getPrecioUnitario();
									Integer cantidad = linea.getCantidad();
									Float iva = linea.getIva();
									Double anticipado = linea.getImporteAnticipado();
									double imp = cantidad.intValue()* importe.doubleValue();
									double ivaImporte = imp * iva.floatValue()/100;
									double aux = imp
											* (1 + iva.floatValue() / 100);
									Double total = new Double(UtilidadesNumero
											.redondea(aux, 2));

									importeTotal = importeTotal + imp;
									ivaTotal = ivaTotal + ivaImporte;
									sumaTotal = sumaTotal + total.doubleValue();
									sumaAnticipado = sumaAnticipado
											+ anticipado.doubleValue();
				%>


		<td><%=UtilidadesString.mostrarDatoJSP(linea
											.getNumeroOrden())%></td>
		<td><%=UtilidadesString.mostrarDatoJSP(linea
											.getDescripcion())%></td>
		<td align="right"><%=UtilidadesString
													.mostrarDatoJSP(cantidad)%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(importe
															.doubleValue()))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(ivaImporte
															))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(total
															.doubleValue()))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(anticipado
															.doubleValue()))%></td>

		<%
														 					} // if
														 					%>
	</siga:FilaConIconosExtExt>
	<% 
														 							} //for%>
	<siga:FilaConIconosExtExt nombreTablaPadre="tablaResultados" pintarEspacio='no' fila="<%=String.valueOf(i+1)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">

		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(importeTotal
															))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(ivaTotal
															))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(sumaTotal
															))%></td>
		<td align="right"><%=UtilidadesString
											.mostrarDatoJSP(UtilidadesNumero
													.formatoCampo(sumaAnticipado
															))%></td>
	</siga:FilaConIconosExtExt>

	<%
	}
	%>

</siga:Table>
	
	

<div style="position:absolute;bottom:20px;t;width:100%;height:160px;margin:0px">
<% FilaExtElement[] elems = new FilaExtElement[1];
			  
elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_NONE);

	%>
<siga:Table name="tablaResultados2" 
	border="2"
	columnNames="Envio,Fecha Envio,facturacion.consultamorosos.literal.comunicaciones.realizadas,"
	columnSizes="10,20,50,10"
	modal="M"
	fixedHeight="250">
	<logic:empty name="ConsultaMorososForm" property="lineasComunicaciones">
		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	</logic:empty>
	
		
	<logic:notEmpty name="ConsultaMorososForm"
		property="lineasComunicaciones">
		
		<logic:iterate name="ConsultaMorososForm" property="lineasComunicaciones" id="lineaComunicacion" indexId="index">

			<siga:FilaConIconos 
	  				fila='<%=String.valueOf(index.intValue()+1)%>'

	  				botones="" 
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				elementos='<%=elems%>' 
	  				clase="listaNonEdit">
				<%-- fila <bean:write name='index'/> escribe la informacion de la linea de comunicacion--%>
				
				
				<input type="hidden" name="idEnvioDoc<bean:write name='index'/>" value="<bean:write name="lineaComunicacion" property="IDENVIODOC" />"/>
				<input type="hidden" name="idDocumento<bean:write name='index'/>" value="<bean:write name="lineaComunicacion" property="IDDOCUMENTO" />"/>
				<input type="hidden" name="idInstitucion<bean:write name='index'/>" value="<bean:write name="lineaComunicacion" property="IDINSTITUCION" />"/>
				<input type="hidden" name="pathDocumento<bean:write name='index'/>" value="<bean:write name="lineaComunicacion" property="PATHDOCUMENTO" />"/>
				<td><bean:write name="lineaComunicacion" property="IDENVIO" /></td>
					<bean:define id="fecha" name="lineaComunicacion" property="FECHA_ENVIO"/>
				
				<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateMedium(user.getLanguage(),fecha))%></td>
				<td><bean:write name="lineaComunicacion" property="DESCRIPCION" /></td>
				
			</siga:FilaConIconos>
			
		</logic:iterate>
		
	</logic:notEmpty>




</siga:Table>
</div>
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
</body>
</html>

