<!-- informeRemesa.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-html.tld" 	prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"	%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.FacSufijoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<%
	String app = request.getContextPath(); 

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");

	Hashtable h = (Hashtable) request.getAttribute("datosImpreso");
	
	String importeTotal         = UtilidadesHash.getString(h,"IMPORTETOTAL")==null? "":UtilidadesHash.getString(h,"IMPORTETOTAL");
	String numOrdenes           = UtilidadesHash.getString(h,"NUMORDENES")==null? "":UtilidadesHash.getString(h,"NUMORDENES");
	String numRegistros         = UtilidadesHash.getString(h,"NUMREGISTROS")==null? "":UtilidadesHash.getString(h,"NUMREGISTROS");
	String fechaCreacionFichero = UtilidadesHash.getString(h,"FECHACREACIONFICHERO")==null? "":UtilidadesHash.getString(h,"FECHACREACIONFICHERO");
	String fechaEmisionOrdenes  = UtilidadesHash.getString(h,"FECHAEMISIONORDENES")==null? "":UtilidadesHash.getString(h,"FECHAEMISIONORDENES");
	String nombreInstitucion    = UtilidadesHash.getString(h,"NOMBREINSTITUCION")==null? "":UtilidadesHash.getString(h,"NOMBREINSTITUCION");
	String codigoOrdenante      = UtilidadesHash.getString(h,"CODIGOORDENANTE")==null? "":UtilidadesHash.getString(h,"CODIGOORDENANTE");
	String cuentaAbono          = UtilidadesHash.getString(h,"CUENTAABONO")==null? "":UtilidadesHash.getString(h,"CUENTAABONO");
	
	boolean abono;
	String ficheroAbono =  (String)request.getAttribute("abonos");
	if(( ficheroAbono!=null)&&(ficheroAbono.equals("1"))){
		abono = true;
	}else{
		abono=false;
	}


%>

<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
	<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionCerrar() {
				window.top.close();
			}		

	</script>	

</head>

<body>
	<table class="tablaTitulo">		
		<!-- Campo obligatorio -->
		<tr>		
			<td class="titulitosDatos">
			<% if (abono){ %>
				<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.tituloPago"/>
			<% }else{ %>
				<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.tirulo"/>
			<% } %>
			</td>				
		</tr>
	</table>	


	<table class="tablaCentralCamposPeque" align="center" >
		<tr>
			<td style="padding:'15';padding-top:'25'">
				<fieldset>
					<table class="tablaCampos" border="0">	
						<tr>
							<td class="boxConsulta">
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.importeToral"/>:
							</td>
							<td class="boxConsultaNumber">
								<%=importeTotal%> &nbsp;&euro;
							</td>
						</tr>
						<tr>
							<td class="boxConsulta">
							<% if(abono){%>
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.numOrdenesPago"/>:
							<% } else { %>
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.numOrdenes"/>:
							<% }%>
							</td>
							<td class="boxConsultaNumber">
								<%=numOrdenes%>
							</td>
						</tr>
						<tr>
							<td class="boxConsulta">
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.numRegistros"/>:
							</td>
							<td class="boxConsultaNumber">
								<%=numRegistros%>
							</td>
						</tr>
						<tr>
							<td class="boxConsulta">
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.fechaCreacionFichero"/>:
							</td>
							<td class="boxConsultaNumber">
								<%=GstDate.getFormatedDateShort(userBean.getLanguage(), fechaCreacionFichero)%>
							</td>
						</tr>
						<% if (!abono){ %>
							<tr>
								<td class="boxConsulta">
									<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.fechaEmisionOrdenes"/>:
								</td>
								<td class="boxConsultaNumber">
									<%=GstDate.getFormatedDateShort(userBean.getLanguage(), fechaEmisionOrdenes)%>
								</td>
							</tr>
						<% } %>
						<tr>
							<td class="boxConsulta" colspan="2">
								<hr align="left" width="100%" size="1" class="boxConsulta">
							</td>
						</tr>

						<tr>
							<td class="boxConsulta" colspan="2">
								<%=nombreInstitucion%>
							</td>
						</tr>
						<tr>
							<td class="boxConsulta">
								<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.codigoOrdenante"/>:
							</td>
							<td class="boxConsultaNumber">
								<%=codigoOrdenante%>
							</td>
						</tr>
						<tr>
							<td class="boxConsulta">
								<% if (abono){ %>
									<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.cuentaCargo"/>:
								<% }else{ %>
									<siga:Idioma key="facturacion.ficheroBancarioPagos.informeRemesa.literal.cuentaAbono"/>:
								<% } %>
							</td>
							<td class="boxConsultaNumber">
								<%=UtilidadesString.mostrarNumeroCuentaConAsteriscos(cuentaAbono)%>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>

	</table>

	<siga:ConjBotonesAccion botones='C' modo='' modal="P" />
	

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>