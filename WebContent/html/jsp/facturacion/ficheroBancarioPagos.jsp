<!DOCTYPE html>
<html>
<head>
<!-- ficheroBancarioPagos.jsp -->

<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 18-03-2005 - Inicio
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
<%@ page import="com.siga.beans.FacDisqueteCargosBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String tieneFechaCargo = request.getAttribute("TIENEFECHACARGO")==null?"NO":(String)request.getAttribute("TIENEFECHACARGO");
	
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
    
    String action=app+"/FAC_DisqueteCargos.do?noReset=true";
    /** FIN PAGINADOR ***/	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="facturacion.ficheroBancarioPagos.literal.cabecera" localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		
		function download(fila) {		
			if (!confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarDescargaFichero"/>')) {
				return false;
			}
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
			document.all.ficheroBancarioPagosForm.modo.value = "download";
	   		document.ficheroBancarioPagosForm.submit();
	 	}
				
		function generarFichero() {	
		    sub();	
			var ok = true;
			var aviso = false;
			var fechaCargo = "<%=tieneFechaCargo%>"

			//Si debo ir a la modal para obtener la fecha de cargo:
			if (fechaCargo == 'SI') {
				//Abro la modal para calcular la fecha de cargo:
				var resultado = ventaModalGeneral(document.all.confirmarFacturacionForm.name,"P");

				// Compruebo que si necesito la fecha de cargo la he obtenido de la modal:
				
				if (resultado==undefined || (resultado!=undefined && resultado=='')) {				
					fin(document);
					ok = false;		
								
				} else {
					// Almaceno la fecha de Cargo de la modal:
					document.all.ficheroBancarioPagosForm.fechaCargo.value = resultado;
				}
			
			} else {
			 	fin();
			} 

			// Si todo ha ido bien y Acepto genero el fichero:
			if (ok && confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarFicheroRenegociaciones"/>')) {			
					document.all.ficheroBancarioPagosForm.modo.value = "generarFichero";
					var f = document.all.ficheroBancarioPagosForm.name;	
					window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioPagos.mensaje.generandoFicheros';
					
			} else {		
			 	fin();
			} 
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
			document.ficheroBancarioPagosForm.modo.value = "informeRemesa";
			ventaModalGeneral(document.ficheroBancarioPagosForm.name,"P");
		}
		
		function refrescarLocal() {
			document.location.reload();
		}
	</script>
</head>

<body>	
	<table class="tablaTitulo">		
		<!-- Campo obligatorio -->
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.cabecera"/>				    
			</td>				
		</tr>
	</table>		


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/FAC_DisqueteCargos.do?noReset=true" method="POST" target="submitArea" style="display:none">		
		<html:hidden property = "modo" value = ""/>
		<html:hidden name="ficheroBancarioPagosForm" property="fechaCargo" value = ""/>
	</html:form>

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->	
	<siga:Table 
	   	name="tablaDatos"
	   	border="1"
	  	columnNames= "facturacion.ficheroBancarioPagos.literal.fecha,
  					  facturacion.ficheroBancarioPagos.literal.descripcion,
					  facturacion.ficheroBancarioPagos.literal.banco,
					  facturacion.ficheroBancarioPagos.literal.nRecibos,
					  facturacion.ficheroBancarioPagos.literal.Origen,
					  facturacion.ficheroBancarioPagos.literal.importeTotalRemesa,"			  			
		columnSizes="9,19,23,8,23,10,10"
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
				elems[0]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ); 	
				elems[1]=new FilaExtElement("versolicitud", "versolicitud", "Informe remesa", SIGAConstants.ACCESS_READ);
				
				int recordNumber = i + 1;
				
				String fecha = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", row.getString(FacDisqueteCargosBean.C_FECHACREACION))); 
				String banco = UtilidadesString.mostrarDatoJSP(row.getString("BANCO"));	
				String recibos = UtilidadesString.mostrarDatoJSP(row.getString("NUMRECIBOS"));
				String origen = UtilidadesString.mostrarDatoJSP(row.getString("NOMBREABREVIADO"));	
				String descripcion = UtilidadesString.mostrarDatoJSP(row.getString("DESCRIPCION_PROGRAMACION"));
				String totalRemesa = UtilidadesString.mostrarDatoJSP(row.getString("TOTAL_REMESA"));						
%> 							
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=row.getString(FacDisqueteCargosBean.C_IDDISQUETECARGOS)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=row.getString(FacDisqueteCargosBean.C_NOMBREFICHERO)%>'>											
						<%=fecha%>
					</td>
					<td><%=descripcion%></td> 
					<td><%=banco%></td> 
					<td align="right"><%=recibos%></td> 
					<td>
<%	
						if(origen == null || origen.equals("")){  
%>
							&nbsp;<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.renegociacion"/>
<%	
						} else {
%>	
							&nbsp;<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.facturacion"/>
<%	
						} 
%>
						<%=origen%>
					</td>
					<td align="right"><%=UtilidadesString.formatoImporte(totalRemesa)%>&nbsp;&euro;</td> 									
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
		modo="abrir"								
		clase="paginator" 
		divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 32px; left: 0px"
		distanciaPaginas=""
		action="<%=action%>" />
<%
	}
%>		
		
	<table class="botonesDetalle" align="center" border="0">
		<tr>
			<td class="tdBotones">					
				<html:button property="renegociaciones" property="idButton" onclick="return generarFichero();" styleClass="button" >
					<siga:Idioma key="facturacion.ficheroBancarioPagos.boton.renegociacion"/>
				</html:button>
			</td>	
		</tr>
	</table>
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->		

	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">		
		<html:hidden name="confirmarFacturacionForm" property="modo" value = "nuevo"/>
		<html:hidden name="confirmarFacturacionForm" property="actionModal" value = ""/>
	</html:form>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>