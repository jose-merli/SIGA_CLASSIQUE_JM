<!DOCTYPE html>
<html>
<head>
<!-- ficheroBancarioAbonos.jsp -->

<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 29-03-2005 - Inicio
-->		
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.FacDisqueteAbonosBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	boolean abonosSJCS = false;
	String sjcs = request.getParameter("sjcs");
	if ((sjcs!=null) && (sjcs.equals("1"))){
		abonosSJCS = true;
	}
	
	/** INICIO PAGINADOR ***/
	String idioma = usrbean.getLanguage().toUpperCase();
	String paginaSeleccionada = "", totalRegistros = "", registrosPorPagina = "";
	Vector resultado = null;
	
    if (ses.getAttribute("DATAPAGINADOR")!=null) {
	 	HashMap hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
	 	if (hm.get("datos")!=null && !hm.get("datos").equals("")){
	  		resultado = (Vector) hm.get("datos");
	  		PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)hm.get("paginador");
	
	 		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 	} else {
	  		resultado = new Vector();
	  		paginaSeleccionada = "0";	
	 		totalRegistros = "0";	
	 		registrosPorPagina = "0";
	 	}
   	} else {
      	resultado = new Vector();
	  	paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
   	}
    
    String action=app+"/FAC_EnvioAbonosABanco.do?noReset=true";
    /** FIN PAGINADOR ***/	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<% if(abonosSJCS){ %>
		<siga:Titulo titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" localizacion="factSJCS.Pagos.localizacion"/>
	<% } else { %>
		<siga:Titulo titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" localizacion="facturacion.localizacion"/>
	<% } %>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		
		function download(fila) {
			if (!confirm('<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.confirmarDescargaFichero"/>')) {
				return false;
			}				
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;
			j = 1;
			while (flag) {
				var aux = 'oculto' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
				if (oculto == null)  { 
					flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
				j++;
			}
			datos.value = datos.value + "%";
			document.all.ficheroBancarioAbonosForm.modo.value = "download";
		   	document.ficheroBancarioAbonosForm.submit();
		}
				
		// Informe remesa
		function versolicitud(fila) {
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;
			j = 1;
			while (flag) {
			  	var aux = 'oculto' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
			  	if (oculto == null)  { 
			  		flag = false; 
			  	} else { 
			  		datos.value = datos.value + oculto.value + ','; 
			  	}
			  j++;
			}
			datos.value = datos.value + "%";
			document.ficheroBancarioAbonosForm.modo.value = "informeRemesa";
			ventaModalGeneral(document.ficheroBancarioAbonosForm.name,"P");
		}
		
		function refrescarLocal() {
			parent.buscar();
		}
	</script>
</head>

<body>	
	<table class="tablaTitulo">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.cabecera"/>				    
			</td>				
		</tr>
	</table>		

	<html:form action="/FAC_EnvioAbonosABanco.do?noReset=true" method="POST" target="submitArea" style="display:none">		
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = ""/>		
		<html:hidden styleId="idInstitucion" 	property="idInstitucion" />
		<html:hidden styleId="codigoBanco" 		property="codigoBanco" />
		<html:hidden styleId="fechaDesde" 		property="fechaDesde" />
		<html:hidden styleId="fechaHasta" 		property="fechaHasta" />
		<html:hidden styleId="abonosDesde" 		property="abonosDesde" />
		<html:hidden styleId="abonosHasta" 		property="abonosHasta" />
		<html:hidden styleId="importesDesde" 	property="importesDesde" />
		<html:hidden styleId="importesHasta" 	property="importesHasta" />				
		<% if (abonosSJCS){ %>
			<input type="hidden" name="sjcs" value="<%=sjcs%>">
		<%} else { %>
			<input type="hidden" name="sjcs" value="">
		<%} %>
	</html:form>
			
	<siga:Table 
	   	name="tablaDatos"
	   	border="1"
	 	columnNames="facturacion.ficheroBancarioAbonos.literal.fecha,
	 				facturacion.ficheroBancarioAbonos.literal.nAbonos.ultimoPagoIncluido,
	 				facturacion.ficheroBancarioAbonos.literal.banco,
	 				facturacion.ficheroBancarioAbonos.literal.nAbonos,
	 				facturacion.ficheroBancarioPagos.literal.importeTotalRemesa,"
		columnSizes="10,20,40,8,12,10"
		modal="M">
		
<%
		if (resultado == null || resultado.size() < 1) { 
%>	   
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else { 
			for (int i = 0; i < resultado.size(); i++) { 				
				Row row = (Row)resultado.elementAt(i);
				
				FilaExtElement[] elems = new FilaExtElement[2];
				elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);
				elems[1]=new FilaExtElement("versolicitud", "versolicitud", "Informe remesa", SIGAConstants.ACCESS_READ);
				
				int recordNumber = i + 1;
								
				String fecha = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", row.getString(FacDisqueteAbonosBean.C_FECHA)));								
				String descripcion = UtilidadesString.mostrarDatoJSP(row.getString("NOMBRE"));
				String banco = UtilidadesString.mostrarDatoJSP(row.getString("BANCO"));
				String recibos = UtilidadesString.mostrarDatoJSP(row.getString("NUMRECIBOS"));	
				String importe = UtilidadesString.mostrarDatoJSP(row.getString("IMPORTE"));
%> 							
				<siga:FilaConIconos 
					fila='<%=String.valueOf(recordNumber)%>' 
					botones='' 
					visibleConsulta='false' 
					visibleEdicion='false' 
					visibleBorrado='false' 
					elementos='<%=elems%>' 
					pintarEspacio="no" 
					clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=row.getString(FacDisqueteAbonosBean.C_IDDISQUETEABONO)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=row.getString(FacDisqueteAbonosBean.C_NOMBREFICHERO)%>'>	
						<%=fecha%>
					</td>
					<td><%=descripcion%></td> 
					<td><%=banco%></td> 
					<td align="right"><%=recibos%></td>
					<td align="right"><%=UtilidadesString.formatoImporte(importe)%>&nbsp;&euro;</td> 								
				</siga:FilaConIconos>
<%			
			}
	 	}
%>  			
	</siga:Table>  			
	
<!-- Metemos la paginación-->		
<%
	if (resultado != null && resultado.size() > 0) { 
%>	
	<siga:Paginador totalRegistros="<%=totalRegistros%>" 
		registrosPorPagina="<%=registrosPorPagina%>" 
		paginaSeleccionada="<%=paginaSeleccionada%>" 
		idioma="<%=idioma%>"
		modo="buscar"								
		clase="paginator" 
		divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 32px; left: 0px"
		distanciaPaginas=""
		action="<%=action%>" />
<%
	}
%>			

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->		
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>
