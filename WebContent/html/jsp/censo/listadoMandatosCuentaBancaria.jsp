<!DOCTYPE html>
<html>
<head>
<!-- listadoMandatosCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "java.util.Vector"%>
<%@ page import = "com.siga.beans.CenMandatosCuentasBancariasBean"%>

<!-- JSP -->
<% 
	UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String nombrePersona = String.valueOf(request.getParameter("nombrePersona"));
	String numero = String.valueOf(request.getParameter("numero"));
	
	String modo = (String) request.getAttribute("modoListadoMandatos");	
	Vector<CenMandatosCuentasBancariasBean> vMandatos = (Vector<CenMandatosCuentasBancariasBean>) request.getAttribute("vListadoMandatos");		
	
	// JPT: Gestion de Volver y botones
	String botones = "";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null || usuario.isLetrado()) {
		busquedaVolver = "volverNo";
	} else {
		botones = "V";
	}
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<siga:TituloExt titulo="pestana.fichaCliente.datosBancarios.listadoMandatos" localizacion="censo.fichaCliente.bancos.mandatos.localizacion" />
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" height="32px">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;

<%
				if ((numero != null) && (!numero.equalsIgnoreCase(""))) {
%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
<%
				} else {
%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<%
				}
%>
			</td>
		</tr>
	</table>
	
	<form id="MandatosCuentasBancariasForm" name="MandatosCuentasBancariasForm" method="post" action="/SIGA/CEN_MandatosCuentasBancarias.do">
		<html:hidden property="modo" styleId="modo"  value="" />
		<input type='hidden' name="idInstitucion" id="idInstitucion" value="" />
		<input type='hidden' name="idPersona" id="idPersona" value="" />
		<input type='hidden' name="idCuenta" id="idCuenta" value="" />
		<input type='hidden' name="idMandato" id="idMandato" value="" />
		<input type="hidden" name="nombrePersona" id="nombrePersona" value="" /> 
		<input type="hidden" name="numero" id="numero" value="" />
		<input type="hidden" name="modoMandato" id="modoMandato" value="" /> 
	</form>	
	
	<siga:Table 
	   	name="listadoMandatos"
	   	border="2"
	   	columnNames="censo.fichaCliente.bancos.mandatos.nombreMandato, 
	   		censo.fichaCliente.bancos.mandatos.idMandato, 
	   		censo.fichaCliente.bancos.mandatos.fechaCreacion, 
	   		censo.fichaCliente.bancos.mandatos.fechaFirma, 
	   		censo.fichaCliente.bancos.mandatos.fechaUso,"
	   	columnSizes="15,30,15,15,15,10">	

<%
		if (vMandatos==null || vMandatos.size()==0) {
%>
	 		<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>			
	 		
<%
		} else {		
			for (int j=0; j < vMandatos.size(); j++) {
				int i=j+1;
				CenMandatosCuentasBancariasBean mandatoBean = (CenMandatosCuentasBancariasBean)vMandatos.get(j);
				
				// JPT: 
				// - Si tiene fecha de uso, pero no tiene fecha de firma:
				// 		a. No se puede modificar la informacion del mandato
				//		b. Se pueden modificar los ficheros de la firma de mandatos y anexos
				// - Si tiene fecha de firma
				// 		a. No se puede modificar la informacion del mandato
				//		b. No se puede modificar los ficheros de la firma de mandatos y anexos que ya estan firmados y tienen un documento asignado
				//		c. Solo Se puede modificar el documento asignado (no se puede moficiar la fecha, lugar, origen y descripcion de la firma), para los ficheros firmados sin documento asignado
				//		d. Se pueden modificar todos los datos de los ficheros de mandatos y anexos que no estan firmados				
				String botonesMandato = "C";
				if (modo.equals("editar"))
					botonesMandato += ",E";
				
				String sNombreMandato = UtilidadesString.getMensajeIdioma(usuario, "censo.fichaCliente.bancos.mandatos.nombreServicios");
				if (mandatoBean.getTipoMandato().equals("1")) 
					sNombreMandato = UtilidadesString.getMensajeIdioma(usuario, "censo.fichaCliente.bancos.mandatos.nombreProductos");
				
				String sFirmaFecha = "";
				if (mandatoBean.getFirmaFecha()!=null && !mandatoBean.getFirmaFecha().equals("") &&
					mandatoBean.getFirmaFechaHora()!=null && !mandatoBean.getFirmaFechaHora().equals("") &&
					mandatoBean.getFirmaFechaMinutos()!=null && !mandatoBean.getFirmaFechaMinutos().equals("")) {
					sFirmaFecha = mandatoBean.getFirmaFecha() + " " + mandatoBean.getFirmaFechaHora() + ":" + mandatoBean.getFirmaFechaMinutos();
				}
%>
				<siga:FilaConIconos 
					fila="<%=String.valueOf(i)%>" 
					botones="<%=botonesMandato%>"
					visibleBorrado="no"
					pintarEspacio="no"
					clase="listaNonEdit">
					
					<td>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=mandatoBean.getIdInstitucion()%>'/>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=mandatoBean.getIdPersona()%>'/>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_3' name='oculto<%=String.valueOf(i)%>_3' value='<%=mandatoBean.getIdCuenta()%>'/>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_4' name='oculto<%=String.valueOf(i)%>_4' value='<%=mandatoBean.getIdMandato()%>'/>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_5' name='oculto<%=String.valueOf(i)%>_5' value='<%=nombrePersona%>'/>
						<input type='hidden' id='oculto<%=String.valueOf(i)%>_6' name='oculto<%=String.valueOf(i)%>_6' value='<%=numero%>'/>
						<%=sNombreMandato%>						
					</td>		
					<td><%=UtilidadesString.mostrarDatoJSP(mandatoBean.getIdMandatoSepa())%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(mandatoBean.getFechaCreacion())%></td>
					<td><%=sFirmaFecha%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(mandatoBean.getFechaUso())%></td>		
				</siga:FilaConIconos>
<%												
			}
		}
%>
	</siga:Table>		
	
	<!-- SCRIPTS LOCALES -->
	<script>
		function editar(fila) {
		   	document.MandatosCuentasBancariasForm.idInstitucion.value = document.getElementById('oculto' + fila + '_' + 1).value;
		   	document.MandatosCuentasBancariasForm.idPersona.value = document.getElementById('oculto' + fila + '_' + 2).value;
		   	document.MandatosCuentasBancariasForm.idCuenta.value = document.getElementById('oculto' + fila + '_' + 3).value;
		   	document.MandatosCuentasBancariasForm.idMandato.value = document.getElementById('oculto' + fila + '_' + 4).value;
		   	document.MandatosCuentasBancariasForm.nombrePersona.value = document.getElementById('oculto' + fila + '_' + 5).value;
		   	document.MandatosCuentasBancariasForm.numero.value = document.getElementById('oculto' + fila + '_' + 6).value;
		   	document.MandatosCuentasBancariasForm.modoMandato.value = "editar";		   				   	
		   	document.MandatosCuentasBancariasForm.modo.value = "gestionarMandatoCuentaBancaria";
		    document.MandatosCuentasBancariasForm.submit();
		}
		
		function consultar(fila) {
		   	document.MandatosCuentasBancariasForm.idInstitucion.value = document.getElementById('oculto' + fila + '_' + 1).value;
		   	document.MandatosCuentasBancariasForm.idPersona.value = document.getElementById('oculto' + fila + '_' + 2).value;
		   	document.MandatosCuentasBancariasForm.idCuenta.value = document.getElementById('oculto' + fila + '_' + 3).value;
		   	document.MandatosCuentasBancariasForm.idMandato.value = document.getElementById('oculto' + fila + '_' + 4).value;
		   	document.MandatosCuentasBancariasForm.nombrePersona.value = document.getElementById('oculto' + fila + '_' + 5).value;
		   	document.MandatosCuentasBancariasForm.numero.value = document.getElementById('oculto' + fila + '_' + 6).value;
		   	document.MandatosCuentasBancariasForm.modoMandato.value = "ver";		   				   	
		   	document.MandatosCuentasBancariasForm.modo.value = "gestionarMandatoCuentaBancaria";
		    document.MandatosCuentasBancariasForm.submit();
		}
	</script>
	
	<siga:ConjBotonesAccion botones='<%=botones%>'/>
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>