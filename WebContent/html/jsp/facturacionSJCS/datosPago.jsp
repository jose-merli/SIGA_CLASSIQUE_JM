<!-- datosPago.jsp -->
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

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.facturacionSJCS.form.DatosGeneralesPagoForm"%>
<%@ page import = "com.atos.utils.ClsConstants"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.siga.beans.FcsPagosJGBean"%>
<%@ page import = "com.atos.utils.GstDate"%>
<%@ page import = "java.util.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	DatosGeneralesPagoForm formulario = (DatosGeneralesPagoForm)request.getAttribute("formularioPagos");
	String modo = (String)request.getAttribute("modo");
	String accion = (String)request.getAttribute("accion");
	String accionPrevia = accion;

	//NOMBRE INSTITUCION PESTAÑA:
	String nombreInstitucion = request.getAttribute("nombreInstitucion")==null?"":(String)request.getAttribute("nombreInstitucion"); 
	
	//DATOS DEL BEAN PAGOSJG:
	String cantidad="", porcentajeOficio="0", porcentajeGuardias="0", porcentajeSOJ="0", porcentajeEJG="0",
						   importeOficio="0",    importeGuardias="0",    importeSOJ="0",    importeEJG="0";
	String idInstitucion="", idPagosJG="", nombre="", fechaDesde="", fechaHasta="", abreviatura="", idFacturacion="", criterioPagoTurno="";
	String importePagado="";
	String totalImportePagado = "0";
	//Datos Oficio
	String totalOficio = "0", importePendienteOficio = "0", porcentajePendienteOficio = "0", importePagadoOficio = "0", porcentajePagadoOficio = "0";
	//Datos Guardias
	String totalGuardias = "0", importePendienteGuardias = "0", porcentajePendienteGuardias = "0", importePagadoGuardias = "0", porcentajePagadoGuardias = "0";
	//Datos EJG
	String totalEJG = "0", importePendienteEJG = "0", porcentajePendienteEJG = "0", importePagadoEJG = "0", porcentajePagadoEJG = "0";
	//Datos SOJ
	String totalSOJ = "0", importePendienteSOJ = "0", porcentajePendienteSOJ = "0", importePagadoSOJ = "0", porcentajePagadoSOJ = "0";

	//DATOS DEL ESTADO:
	String nombreEstado="", idEstadoPagosJG="", fechaEstado="";
	
	//DATOS FACTURACION:
	String idEstadoFacturacion="", nombreFacturacion="", importeFacturado="";

	//COBRO AUTOMATICO
	String cobroAutomatico ="";

	Long importePendienteDePago = null;

	//DATOS CUANDO VENIMOS DE EDITAR/VER:
	boolean esConsulta = accion.equalsIgnoreCase("consulta");
	boolean esVerEditar = accion.equalsIgnoreCase("edicion") || esConsulta;
	String scriptOnLoad = "ocultarConceptos();";
	if (esVerEditar) {
		//Funcion javascript que se llama en el onload 
		scriptOnLoad = "inicializarVerEditar();";
		
		//Datos del Bean:
		FcsPagosJGBean pagosBean = (FcsPagosJGBean)request.getAttribute("PAGOSBEAN");
		idInstitucion = pagosBean.getIdInstitucion().toString();//Propia del registro
		idPagosJG = pagosBean.getIdPagosJG().toString();
		idFacturacion = pagosBean.getIdFacturacion().toString();
		nombre = pagosBean.getNombre()==null?"":pagosBean.getNombre();
		abreviatura = pagosBean.getAbreviatura()==null?"":pagosBean.getAbreviatura();
		fechaDesde = pagosBean.getFechaDesde()==null?"":GstDate.getFormatedDateShort(usrbean.getLanguage(),pagosBean.getFechaDesde());
		fechaHasta = pagosBean.getFechaHasta()==null?"":GstDate.getFormatedDateShort(usrbean.getLanguage(),pagosBean.getFechaHasta());
		criterioPagoTurno = pagosBean.getCriterioPagoTurno()==null?"":pagosBean.getCriterioPagoTurno();
		porcentajeOficio = pagosBean.getPorcentajeOficio()==null?"":pagosBean.getPorcentajeOficio().toString();
		porcentajeGuardias = pagosBean.getPorcentajeGuardias()==null?"":pagosBean.getPorcentajeGuardias().toString();
		porcentajeEJG = pagosBean.getPorcentajeEJG()==null?"":pagosBean.getPorcentajeEJG().toString();
		porcentajeSOJ = pagosBean.getPorcentajeSOJ()==null?"":pagosBean.getPorcentajeSOJ().toString();
		importeOficio = pagosBean.getImporteOficio()==null?"":pagosBean.getImporteOficio().toString();
		importeGuardias = pagosBean.getImporteGuardia()==null?"":pagosBean.getImporteGuardia().toString();
		importeEJG = pagosBean.getImporteEJG()==null?"":pagosBean.getImporteEJG().toString();
		importeSOJ = pagosBean.getImporteSOJ()==null?"":pagosBean.getImporteSOJ().toString();
		cantidad = pagosBean.getImporteRepartir()==null?"":pagosBean.getImporteRepartir().toString();
		importePagado = pagosBean.getImportePagado()==null?"":pagosBean.getImportePagado().toString();

		Hashtable conceptos = (Hashtable)request.getAttribute("CONCEPTOS");

		importePagado = (String)conceptos.get("TOTALIMPORTEPAGADO");
		//Datos Oficio
		totalOficio = (String)conceptos.get("TOTALOFICIO");
		importePagadoOficio = (String)conceptos.get("TOTALIMPORTEPAGADOOFICIO");
		porcentajePagadoOficio = (String)conceptos.get("TOTALPORCENTAJEPAGADOOFICIO");
		//Datos Guardias
		totalGuardias = (String)conceptos.get("TOTALGUARDIA");
		importePagadoGuardias = (String)conceptos.get("TOTALIMPORTEPAGADOGUARDIA");
		porcentajePagadoGuardias = (String)conceptos.get("TOTALPORCENTAJEPAGADOGUARDIA");
		//Datos EJG
		totalEJG = (String)conceptos.get("TOTALEJG");
		importePagadoEJG = (String)conceptos.get("TOTALIMPORTEPAGADOEJG");
		porcentajePagadoEJG = (String)conceptos.get("TOTALPORCENTAJEPAGADOEJG");
		//Datos SOJ
		totalSOJ = (String)conceptos.get("TOTALSOJ");
		importePagadoSOJ = (String)conceptos.get("TOTALIMPORTEPAGADOSOJ");
		porcentajePagadoSOJ = (String)conceptos.get("TOTALPORCENTAJEPAGADOSOJ");
		
		//Resto de Datos:
		cobroAutomatico = (String)request.getAttribute("cobroAutomatico");
		nombreEstado = (String)request.getAttribute("nombreEstado");
		fechaEstado = GstDate.getFormatedDateShort(usrbean.getLanguage(),(String)request.getAttribute("fechaEstado"));
		idEstadoPagosJG = request.getAttribute("idEstadoPagosJG")==null?"":(String)request.getAttribute("idEstadoPagosJG");
		idEstadoFacturacion = request.getAttribute("idEstadoFacturacion")==null?"":(String)request.getAttribute("idEstadoFacturacion");
		nombreFacturacion = request.getAttribute("nombreFacturacion")==null?"":(String)request.getAttribute("nombreFacturacion");
		importeFacturado = request.getAttribute("importeFacturado")==null?"":(String)request.getAttribute("importeFacturado");
	} 
	else { //Venimos de Nuevo
		idInstitucion = request.getAttribute("idInstitucionRegistro")==null?"":(String)request.getAttribute("idInstitucionRegistro");//Propia del registro
	}
	
	importePendienteDePago = request.getAttribute("importePendienteDePago")==null?null:(Long)request.getAttribute("importePendienteDePago");
	
	//Accion Previa para ver la modal en modo edicion o consulta:
	if (idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_EJECUTADO) || idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_CERRADO))
		accionPrevia = "consulta";
	
	//VISIBILIDAD:
	boolean b_lectura=false;
	String estilo="";
	String estiloNumber="boxConsultaNumber";
	if (accion.equalsIgnoreCase("nuevo")) {
		nombreEstado="";
		fechaEstado="";
		b_lectura = false;
		estilo = "box";
		estiloNumber="boxNumber";
	} else {
		if (accion.equalsIgnoreCase("edicion")) {			
			estilo = "box";
			if (idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_EJECUTADO) || idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_CERRADO)) {
				b_lectura = true;
				estilo = "boxConsulta";
				estiloNumber="boxConsultaNumber";
			}
		} else if (accion.equalsIgnoreCase("consulta")) {
					b_lectura = true;
					estilo = "boxConsulta";
					estiloNumber="boxConsultaNumber";
					
				}
	}
	
	//COMBO FACTURACION:
	String facturacionParams[] = new String[2];
	facturacionParams[0] = usrbean.getLocation();
	facturacionParams[1] = String.valueOf(ClsConstants.ESTADO_FACTURACION_LISTA_CONSEJO);
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.pagos.datosGenerales" 
		localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosGeneralesPagoForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
	<script>
		var totalImportePagado = <%=totalImportePagado%>;
		var totalOficio = <%=totalOficio%>;
		var importePendOficio = <%=importePendienteOficio%>;
		var porcentajePendOficio = <%=porcentajePendienteOficio%>;
		var	importeOficio = <%=importeOficio%>;
		var	porcentajeOficio = <%=porcentajeOficio%>;		
		var	importePagadoOficio = <%=importePagadoOficio%>;
		var	porcentajePagadoOficio = <%=porcentajePagadoOficio%>;		

		var totalGuardias = <%=totalGuardias%>;
		var importePendGuardias = <%=importePendienteGuardias%>;
		var porcentajePendGuardias = <%=porcentajePendienteGuardias%>;
		var	importeGuardias = <%=importeGuardias%>;
		var	porcentajeGuardias = <%=porcentajeGuardias%>;		
		var	importePagadoGuardias = <%=importePagadoGuardias%>;
		var	porcentajePagadoGuardias = <%=porcentajePagadoGuardias%>;		

		var totalEJG = <%=totalEJG%>;
		var importePendEJG = <%=importePendienteEJG%>;
		var porcentajePendEJG = <%=porcentajePendienteEJG%>;
		var	importeEJG = <%=importeEJG%>;
		var	porcentajeEJG = <%=porcentajeEJG%>;		
		var	importePagadoEJG = <%=importePagadoEJG%>;
		var	porcentajePagadoEJG = <%=porcentajePagadoEJG%>;		

		var totalSOJ = <%=totalSOJ%>;
		var importePendSOJ = <%=importePendienteSOJ%>;
		var porcentajePendSOJ = <%=porcentajePendienteSOJ%>;
		var	importeSOJ = <%=importeSOJ%>;
		var	porcentajeSOJ = <%=porcentajeSOJ%>;		
		var	importePagadoSOJ = <%=importePagadoSOJ%>;
		var	porcentajePagadoSOJ = <%=porcentajePagadoSOJ%>;		
		
		var backupAPagar;
	</script>
	
	<script>
		function convertirAFormato(n){
			var d = n.replace(",",".");
			d = new Number(d);
			d = Number(d.toFixed(2));
			d = d.toLocaleString();
			d = d.replace(".","");
			if(String(d).indexOf(',') < 0){
				d += '.00'; // aqui puede variar segun la cantidad de decimales que desees;
			}	
			return d.replace(".",",");	
		}

		function convertirANumero(n){
			return convertirAFormato(n).replace(",",".");
		}
				
		/*	NOTA:
			El combo me devuelve separado por ~ y en este orden:			
			0.IDFACTURACION
			1.FECHADESDE
			2.FECHAHASTA
			3.TOTALPAGADO
			4.TOTALFACTURADO 
			N. mirar el codigo de abajo para el resto de campos
		*/
		function actualizarFechas(obj){
			var seleccionado = obj.options[obj.selectedIndex].value;
			var vseleccionado = seleccionado.split('~');

			if (seleccionado == 0){
				accionRestablecer();
				return;
			}
					
			//IDFACTURACION:
			var id = vseleccionado[0];
			
			//FECHADESDE:
			var fechaDesde = vseleccionado[1];
			
			//FECHAHASTA:
			var fechaHasta = vseleccionado[2];

			//TOTALPAGADO:
			var importePagado = vseleccionado[3];

			//TOTALPAGADOOFICIO:
			importePagadoOficio = vseleccionado[4];
			
			//TOTALPAGADOGUARDIA:
			importePagadoGuardias = vseleccionado[5];
			
			//TOTALPAGADOEJG:
			importePagadoEJG = vseleccionado[6];
			
			//TOTALPAGADOSOJ:
			importePagadoSOJ = vseleccionado[7];

			//TOTALPORCENTAJEPAGADOOFICIO:
			porcentajePagadoOficio = vseleccionado[8];
			
			//TOTALPORCENTAJEPAGADOGUARDIA:
			porcentajePagadoGuardias = vseleccionado[9];
			
			//TOTALPORCENTAJEPAGADOEJG:
			porcentajePagadoEJG = vseleccionado[10];
			
			//TOTALPORCENTAJEPAGADOSOJ:
			porcentajePagadoSOJ = vseleccionado[11];
			
			//TOTALFACTURADO:
			var importeFacturado = vseleccionado[12];

			//TOTALFACTURADOOFICIO:
			totalOficio = vseleccionado[13];

			//TOTALFACTURADOGUARDIA:
			totalGuardias = vseleccionado[14];

			//TOTALFACTURADOEJG:
			totalEJG = vseleccionado[15];

			//TOTALFACTURADOSOJ:
			totalSOJ = vseleccionado[16];
			
			
			//Actualizo los campos:		
			f = document.getElementById("datosGeneralesPagoForm");
			f.idFacturacion.value = id;
			f.fechaDesde.value = fechaDesde;
			f.fechaHasta.value = fechaHasta;			
			f.importePagado.value = importePagado;
			f.importeFacturado.value = importeFacturado;

			calcularPendiente();
					
			inicializaConceptos();
			
		}

		/**
		 * Calcula los importes y porcentajes pendientes de cada concepto
		 */
		function calcularPendiente(){
			// actualizo las variables javascript de importe pendiente y porcentaje pendiente 
			// para cada concepto
			
			/*alert(totalOficio + " - " + importePagadoOficio + " - " + porcentajePagadoOficio + " - " + 
				    totalGuardias + " - " + importePagadoGuardias + " - " + porcentajePendGuardias + " - " + 
				    totalEJG + " - " + importePagadoEJG + " - " + porcentajePagadoEJG + " - " + 
			        totalSOJ + " - " + importePagadoSOJ + " - " + porcentajePagadoSOJ);*/
			
			importePendOficio = parseFloat(totalOficio) - parseFloat(importePagadoOficio);
			porcentajePendOficio = parseFloat(100) - parseFloat(porcentajePagadoOficio);

			importePendGuardias = parseFloat(totalGuardias) - parseFloat(importePagadoGuardias);
			porcentajePendGuardias = parseFloat(100) - parseFloat(porcentajePagadoGuardias);

			importePendEJG = parseFloat(totalEJG) - parseFloat(importePagadoEJG);
			porcentajePendEJG = parseFloat(100) - parseFloat(porcentajePagadoEJG);

			importePendSOJ = parseFloat(totalSOJ) - parseFloat(importePagadoSOJ);
			porcentajePendSOJ = parseFloat(100) - parseFloat(porcentajePagadoSOJ);
		}
		
		/**
		 * Inicializa los datos cuando la accion es ver o editar
		 */
		function inicializarVerEditar(){
			calcularPendiente();
			inicializaConceptos();
			/*alert(importePendOficio + " - " + porcentajePendOficio + " - " + 
			  importePendGuardias + " - " + porcentajePendGuardias + " - " + 
			  importePendEJG + " - " + porcentajePendEJG + " - " + 
			  importePendSOJ + " - " + porcentajePendSOJ);*/
		}
		
		/**
		 * Oculta las fila de cada concepto de la tabla de reparto
		 */
		function ocultarConceptos(){
			//Ocultar las filas de cada concepto
			document.getElementById("filaOficio").style.display = "none";
			document.getElementById("filaGuardias").style.display = "none";
			document.getElementById("filaEJG").style.display = "none";
			document.getElementById("filaSOJ").style.display = "none";
		}
		
		//Selecciona por texto el option adecuado segun el nombreFacturacion
		function inicio(){
			f = document.getElementById("datosGeneralesPagoForm").comboFacturacion;
			for (var i=0; i< f.options.length; i++){
				if (f.options[i].text == "<%=nombreFacturacion%>") {
					f.options[i].selected = true;					
					var seleccionado = f.options[i].value;
					var id = seleccionado.substring(0,seleccionado.indexOf(","))
					document.getElementById("datosGeneralesPagoForm").idFacturacion.value = id;
				}
			}
		}
				
	</script>

</head>

<body onload="<%=scriptOnLoad%>">

<!-- ******* BOTONES Y CAMPOS ****** -->
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosPagos.titulo1"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos" cellpadding="0" cellspacing="0" align="center">

	<html:form action="/FCS_DatosGeneralesPago.do?noReset=true" method="POST" target="submitArea">
	<html:hidden name="datosGeneralesPagoForm" property="modo" value="<%=modo%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idFacturacion" value="<%=idFacturacion%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idEstadoPagosJG" value="<%=idEstadoPagosJG%>" />
	<html:hidden name="datosGeneralesPagoForm" property="cobroAutomatico" value="<%=cobroAutomatico%>" />
	<html:hidden name="datosGeneralesPagoForm" property="actionModal" value=""/>
	<html:hidden name="datosGeneralesPagoForm" property="accionPrevia" value="<%=accionPrevia%>"/>
	<html:hidden name="datosGeneralesPagoForm" property="criterioPagoTurno" value="<%=ClsConstants.CRITERIOS_PAGO_FACTURACION%>" />
	<html:hidden name="datosGeneralesPagoForm" property="importeRepartir" value="<%=cantidad%>" />
	<tr>				
	<td>
	<siga:ConjCampos leyenda="factSJCS.datosPagos.leyenda1">
	<table class="tablaCampos" align="center" border="0">

	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.nombre"/>&nbsp;(*)
		</td>
		<td class="labelText" >			
			<html:text name="datosGeneralesPagoForm" property="nombre" style="width:325" styleClass="<%=estilo%>" readonly="<%=b_lectura%>" value="<%=nombre%>" />
		</td>
		<td class="labelText" colspan="2">
			<siga:Idioma key="factSJCS.datosPagos.literal.abreviatura"/>&nbsp;(*)
			&nbsp;
			<html:text name="datosGeneralesPagoForm" property="abreviatura" style="width:325" styleClass="<%=estilo%>" readonly="<%=b_lectura%>"  value="<%=abreviatura%>" />
		</td>
	</tr>

	<!-- FILA -->
	<tr>
		<td class="labelText" >
			<siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/>&nbsp;(*)
		</td>
		<td class="labelText" >		
			<% if (accion.equalsIgnoreCase("nuevo")) { %>
				<siga:ComboBD nombre="comboFacturacion" tipo="cmb_facturacionPagosTodos" estilo="width:325" parametro="<%=facturacionParams %>" clase="boxCombo" obligatorio="false" accion="actualizarFechas(this);"  />
			<% } else { %>
				<html:text name="datosGeneralesPagoForm" property="comboFacturacion" style="width:325" value='<%=nombreFacturacion%>' styleClass="boxConsulta" readOnly="true"></html:text>
			<% } %>
		</td>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.estado"/>
			&nbsp;
			<html:text name="datosGeneralesPagoForm" property="nombreEstado" value='<%=nombreEstado%>' styleClass="boxConsulta" readOnly="true"></html:text>	
		</td>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.fechaEstado"/>
			&nbsp;
			<html:text name="datosGeneralesPagoForm" property="fechaEstado" value='<%=fechaEstado%>' styleClass="boxConsulta" readOnly="true"></html:text>	
		</td>
	</tr>


	<!-- FILA -->
	<!-- DATOS DE LA FACTURACION -->
	<tr>				
	  <td colspan="4">
		<siga:ConjCampos leyenda="factSJCS.datosGenerales.cabecera">
		<table class="tablaCampos" align="center" border="0">
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosPagos.literal.importeFacturado"/>
			</td>
			<td class="labelText">
				<html:text name="datosGeneralesPagoForm" property="importeFacturado" value="<%=UtilidadesNumero.formatoCampo(importeFacturado)%>" size="20" styleClass="boxConsultaNumber" readOnly="true"></html:text>
				&nbsp;&euro;
			</td>				
			
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosPagos.literal.fechaInicio"/>
			</td>
			<td class="labelText">
				<html:text name="datosGeneralesPagoForm" property="fechaDesde" value='<%=fechaDesde%>' size="10" styleClass="boxConsulta" readOnly="true" />
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosFacturacion.literal.totalPagado"/>
			</td>
			<td class="labelText">
				<html:text name="datosGeneralesPagoForm" property="importePagado" value="<%=UtilidadesNumero.formatoCampo(importePagado)%>" size="20" styleClass="boxConsultaNumber" readonly="<%=b_lectura%>" />
				&nbsp;&euro;
			</td>
		
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosPagos.literal.fechaFin"/>
			</td>
			<td class="labelText">
				<html:text name="datosGeneralesPagoForm" property="fechaHasta" value='<%=fechaHasta%>' size="10" styleClass="boxConsulta" readOnly="true"></html:text>
			</td>				
		</tr>
		</table>
		</siga:ConjCampos>
	  </td>
	</tr>
	
	<tr><td>&nbsp;</td></tr>
	
	<!-- FILA -->
	<!-- Datos sobre el reparto -->
	<tr>
		<td colspan="4" id="titulo" class="titulitosDatos">
			<siga:Idioma key="factSJCS.datosPagos.titulo.reparto"/>
		</td>
	</tr>
	<tr>
	  <td colspan="4">
			<table border="1" width="100%" cellspacing='0' cellpadding='0'>	
				<tr>	
					<td width="13%" class="tableTitle"><siga:Idioma key="factSJCS.datosPagos.literal.concepto"/></td>
					<td width="19%" class="tableTitle"><siga:Idioma key="factSJCS.datosPagos.literal.total"/></td>
					<td width="19%" class="tableTitle"><siga:Idioma key="factSJCS.datosPagos.literal.pendiente"/></td>
					<td width="30%" class="tableTitle"><siga:Idioma key="factSJCS.datosPagos.literal.aPagar"/></td>
					<td width="19%" class="tableTitle"><siga:Idioma key="factSJCS.datosPagos.literal.restante"/></td>
				</tr>
				
				<!-- OFICIO -->
				<tr id="filaOficio" style="display:block">				
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.Oficio"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalOficio" id="txtTotalOficio" size="18" class="boxConsultaNumber"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteOficio" id="txtPendienteOficio" size="18" class="boxConsultaNumber"/>	
					</td>
					<td class="labelTextNum" >
						<% if (!esVerEditar){ %>
							<input name="radioAPagarOficio" value="importe" type="radio" Checked/>
							<input name="importeOficio" id="importeOficio" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('Oficio', totalOficio, importePendOficio, porcentajePendOficio);" onfocus="backup('Oficio', 0)" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioApagarOficio" value="porcentaje" type="radio" />
							<input name="porcentajeOficio" id="porcentajeOficio" style="width:15%" maxlength="5" class="<%=estiloNumber%>" onblur="actualizaConcepto('Oficio', totalOficio, importePendOficio, porcentajePendOficio);"	onfocus="backup('Oficio', 1);" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarOficio" id="txtAPagarOficio" style="width:100%" class="boxConsultaNumber"/>
							<input type="hidden" name="importeOficio" id="importeOficio"/>
							<input type="hidden" name="porcentajeOficio" id="porcentajeOficio"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteOficio" id="txtRestanteOficio" style="width:100%" class="boxConsultaNumber"/>
					</td>
				</tr>
				<!-- GUARDIAS -->
				<tr id="filaGuardias" style="display:block">				
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.Guardias"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalGuardias" id="txtTotalGuardias" size="18" class="boxConsultaNumber"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteGuardias" id="txtPendienteGuardias" size="18" class="boxConsultaNumber"/>	
					</td>
					<td class="labelTextNum" >
						<% if (!esVerEditar){ %>
							<input name="radioAPagarGuardias" value="importe" type="radio" Checked/>
							<input name="importeGuardias" id="importeGuardias" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('Guardias', totalGuardias, importePendGuardias, porcentajePendGuardias);" onfocus="backup('Guardias', 0);" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarGuardias" value="porcentaje" type="radio" />
							<input name="porcentajeGuardias" id="porcentajeGuardias" style="width:15%" maxlength="5" class="<%=estiloNumber%>"  									
										onblur="actualizaConcepto('Guardias', totalGuardias, importePendGuardias, porcentajePendGuardias);"
										onfocus="backup('Guardias', 1);" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarGuardias" id="txtAPagarGuardias" size="18" class="boxConsultaNumber"/>
							<input type="hidden" name="importeGuardias" id="importeGuardias"/>
							<input type="hidden" name="porcentajeGuardias" id="porcentajeGuardias"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteGuardias" id="txtRestanteGuardias" size="18" class="boxConsultaNumber"/>
					</td>
				</tr>
				<!-- EJG -->
				<tr id="filaEJG" style="display:block">				
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.EJG"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalEJG" id="txtTotalEJG" size="18" class="boxConsultaNumber"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteEJG" id="txtPendienteEJG" size="18" class="boxConsultaNumber"/>	
					</td>
					<td class="labelTextNum" >
						<% if (!esVerEditar){ %>
							<input name="radioAPagarEJG" value="importe" type="radio" Checked/>
							<input name="importeEJG" id="importeEJG" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('EJG', totalEJG, importePendEJG, porcentajePendEJG);" onfocus="backup('EJG', 0);" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarEJG" value="porcentaje" type="radio" />
							<input name="porcentajeEJG" id="porcentajeEJG" style="width:15%" maxlength="5" class="<%=estiloNumber%>" onblur="actualizaConcepto('EJG', totalEJG, importePendEJG, porcentajePendEJG);" onfocus="backup('EJG', 1);" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarEJG" id="txtAPagarEJG" size="18" class="boxConsultaNumber"/>
							<input type="hidden" name="importeEJG" id="importeEJG"/>
							<input type="hidden" name="porcentajeEJG" id="porcentajeEJG"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteEJG" id="txtRestanteEJG" size="18" class="boxConsultaNumber"/>
					</td>
				</tr>
				<!-- SOJ -->
				<tr id="filaSOJ" style="display:block">	
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.SOJ"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalSOJ" id="txtTotalSOJ" size="18" class="boxConsultaNumber"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteSOJ" id="txtPendienteSOJ" size="18" class="boxConsultaNumber"/>	
					</td>
					<td class="labelTextNum" >
						<% if (!esVerEditar){ %>
							<input name="radioAPagarSOJ" value="importe" type="radio" Checked/>
							<input name="importeSOJ" id="importeSOJ" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('SOJ', totalSOJ, importePendSOJ, porcentajePendSOJ);" onfocus="backup('SOJ', 0);" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarSOJ" value="porcentaje" type="radio" />
							<input name="porcentajeSOJ" id="porcentajeSOJ" style="width:15%" maxlength="5" class="<%=estiloNumber%>" onblur="actualizaConcepto('SOJ', totalSOJ, importePendSOJ, porcentajePendSOJ);" onfocus="backup('SOJ', 1);" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarSOJ" id="txtAPagarSOJ" size="18" class="boxConsultaNumber"/>
							<input type="hidden" name="importeSOJ" id="importeSOJ"/>
							<input type="hidden" name="porcentajeSOJ" id="porcentajeSOJ"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteSOJ" id="txtRestanteSOJ" size="18" class="boxConsultaNumber"/>
					</td>
				</tr>
				
			</table>			
		</td>

	</tr>

	<!-- FILA -->	
	</table>
	</siga:ConjCampos>

	</html:form>

	</table>
	<!-- FIN: CAMPOS-->

	<%
		String botones = "V";
		if (accion.equalsIgnoreCase("nuevo")) 
			botones = "V,G,R";
		else {
			if (accion.equalsIgnoreCase("edicion")) {
				if (idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_ABIERTO))) botones += ",EF,G,R";
				if (idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_EJECUTADO))) botones += ",CP";
			}
			botones += ",VC";
		}
	%>


	<siga:ConjBotonesAccion botones="<%=botones%>"/>

	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="subPestanas">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
			<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<html:hidden name="mantenimientoPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	</html:form>
	
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">		
		//Validador de porcentajes. Suma <= 100
		function validarPorcentajes(f){
			if (f.porcentajeOficio.value!="" && f.porcentajeGuardias.value!="" && f.porcentajeEJG.value!="" && f.porcentajeSOJ.value!="") {
				var suma = parseInt(f.porcentajeOficio.value) + parseInt(f.porcentajeGuardias.value) + parseInt(f.porcentajeEJG.value) + parseInt(f.porcentajeSOJ.value);
				return (suma <= 100);
			} else
				return false;
		}
	
		// Asociada al boton Volver
		function accionVolver() 
		{		
			var f = document.getElementById("mantenimientoPagoForm");
			f.target = "mainPestanas";
			f.submit();
		}
		
		//Convierte a formato java los campos de tipo precio
		function actualizarCamposPrecio(){
			document.forms[0].importeRepartir.value=document.forms[0].importeRepartir.value.replace(/,/,".");
			document.forms[0].importePagado.value=document.forms[0].importePagado.value.replace(/,/,".");
			document.forms[0].importeFacturado.value=document.forms[0].importeFacturado.value.replace(/,/,".");

			//Convierte los importes y porcentajes de cada concepto
			var conceptos = new Array(4)
			conceptos[0] = 'Oficio'; conceptos[1] = 'Guardias';
			conceptos[2] = 'EJG'; conceptos[3] = 'SOJ';
			for (i=0;i<4;i++){
				var objImporte = document.getElementById("importe"+conceptos[i]);
				objImporte.value = objImporte.value.replace(/,/,".");
				var objPorcentaje = document.getElementById("porcentaje"+conceptos[i]);
				objPorcentaje.value = objPorcentaje.value.replace(/,/,".");
			}
		}
		
		function formatearCamposprecio(){
			document.forms[0].importeRepartir.value = convertirAFormato(document.forms[0].importeRepartir.value);
			document.forms[0].importePagado.value = convertirAFormato(document.forms[0].importePagado.value);
			document.forms[0].importeFacturado.value = convertirAFormato(document.forms[0].importeFacturado.value);								
		}

		
		/**
		 *
		 */
		function inicializaConceptos(){
			//Oficio
			if (totalOficio==0)
				document.getElementById("filaOficio").style.display = "none";
			else{
				document.getElementById("filaOficio").style.display = "inline";				
				document.forms[0].txtTotalOficio.value = convertirAFormato(totalOficio+"") + '\u20AC';
				document.forms[0].txtPendienteOficio.value = convertirAFormato(importePendOficio+"") + '\u20AC (' + convertirAFormato(porcentajePendOficio+"") + '%)';
				document.forms[0].txtRestanteOficio.value = convertirAFormato(importePendOficio+"") + '\u20AC (' + convertirAFormato(porcentajePendOficio+"") + '%)';
				<% if (esVerEditar){%>
				document.forms[0].txtAPagarOficio.value = convertirAFormato(importeOficio+"") + '\u20AC (' + convertirAFormato(porcentajeOficio+"") + '%)';
				document.forms[0].importeOficio.value = convertirAFormato(importeOficio+"");
				document.forms[0].porcentajeOficio.value = convertirAFormato(porcentajeOficio+"");;
				<% }%>
			}
			//Guardias
			if (totalGuardias==0)
				document.getElementById("filaGuardias").style.display = "none";
			else{
				document.getElementById("filaGuardias").style.display = "inline";				
				document.forms[0].txtTotalGuardias.value = convertirAFormato(totalGuardias+"") + '\u20AC';
				document.forms[0].txtPendienteGuardias.value = convertirAFormato(importePendGuardias+"") + '\u20AC (' + convertirAFormato(porcentajePendGuardias+"") + '%)';
				document.forms[0].txtRestanteGuardias.value = convertirAFormato(importePendGuardias+"") + '\u20AC (' + convertirAFormato(porcentajePendGuardias+"") + '%)';
				<% if (esVerEditar){%>
				document.forms[0].txtAPagarGuardias.value = convertirAFormato(importeGuardias+"") + '\u20AC (' + convertirAFormato(porcentajeGuardias+"") + '%)';
				document.forms[0].importeGuardias.value = convertirAFormato(importeGuardias+"");
				document.forms[0].porcentajeGuardias.value = convertirAFormato(porcentajeGuardias+"");;
				<% }%>
			}
			//EJG
			if (totalEJG==0)
				document.getElementById("filaEJG").style.display = "none";
			else{
				document.getElementById("filaEJG").style.display = "inline";				
				document.forms[0].txtTotalEJG.value = convertirAFormato(totalEJG+"") + '\u20AC';
				document.forms[0].txtPendienteEJG.value = convertirAFormato(importePendEJG+"") + '\u20AC (' + convertirAFormato(porcentajePendEJG+"") + '%)';
				document.forms[0].txtRestanteEJG.value = convertirAFormato(importePendEJG+"") + '\u20AC (' + convertirAFormato(porcentajePendEJG+"") + '%)';
				<% if (esVerEditar){%>
				document.forms[0].txtAPagarEJG.value = convertirAFormato(importeEJG+"") + '\u20AC (' + convertirAFormato(porcentajeEJG+"") + '%)';
				document.forms[0].importeEJG.value = convertirAFormato(importeEJG+"");
				document.forms[0].porcentajeEJG.value = convertirAFormato(porcentajeEJG+"");;
				<% }%>
			}
			//SOJ
			if (totalSOJ==0)
				document.getElementById("filaSOJ").style.display = "none";
			else{
				document.getElementById("filaSOJ").style.display = "inline";				
				document.forms[0].txtTotalSOJ.value = convertirAFormato(totalSOJ+"") + '\u20AC';
				document.forms[0].txtPendienteSOJ.value = convertirAFormato(importePendSOJ+"") + '\u20AC (' + convertirAFormato(porcentajePendSOJ+"") + '%)';
				document.forms[0].txtRestanteSOJ.value = convertirAFormato(importePendSOJ+"") + '\u20AC (' + convertirAFormato(porcentajePendSOJ+"") + '%)';
				<% if (esVerEditar){%>
				document.forms[0].txtAPagarSOJ.value = convertirAFormato(importeSOJ+"") + '\u20AC (' + convertirAFormato(porcentajeSOJ+"") + '%)';
				document.forms[0].importeSOJ.value = convertirAFormato(importeSOJ+"");
				document.forms[0].porcentajeSOJ.value = convertirAFormato(porcentajeSOJ+"");;
			<% }%>
			}
		}


		/**
		 * Guarda el valor de un input en una variable.
		 * Esta funcion se llama cuando se entra en cada uno de los inputs "a pagar"
		 * concepto (Oficio, Turno, EJG, SOJ)
		 * tipo (importe, porcentaje)
		 */
		function backup(concepto, tipo){
			var radio = document.getElementsByName("radioAPagar"+concepto)[tipo];
			if (radio.checked)
				backupAPagar = document.getElementById("importe"+concepto).value;
		}

		
		/**
		 * Actualiza el campo "restante" y los campos "porcentaje a pagar" e "importe a pagar" 
		 * en funcion de cual de los dos se haya rellenado.
		 */
		function actualizaConcepto(concepto, total, importePendiente, porcentajePendiente){
			var importe = 0;
			var porcentaje = 0;
			var importeRestante = 0;
			var porcentajeRestante = 0;
			//alert( total + " - " + importePendiente + " - " + porcentajePendiente);
			//calcula el importe y el porcenaje restante
			if (document.getElementsByName("radioAPagar"+concepto)[0].checked){
				importe = convertirANumero(document.getElementById("importe"+concepto).value);
				//si no se ha introducido un valor correcto no se actualiza nada
				if (isNaN(importe) || importe < 0 || importe > importePendiente){
					document.getElementById("importe"+concepto).value = backupAPagar;				
					return;		
				}	
				porcentaje = parseFloat(importe * 100 / total);							
			}
			else{
				porcentaje = convertirANumero(document.getElementById("porcentaje"+concepto).value);
				//si no se ha introducido un valor correcto no se actualiza nada
				if (isNaN(porcentaje) || porcentaje < 0 || porcentaje > porcentajePendiente){
					document.getElementById("porcentaje"+concepto).value = backupAPagar;	
					return;
				}
				importe = parseFloat(porcentaje * total / 100);
			}

			//actualiza los input por si se ha corregido una coma o el redondeo
			document.getElementById("importe"+concepto).value = convertirAFormato(""+importe);				
			document.getElementById("porcentaje"+concepto).value = convertirAFormato(""+porcentaje);	
			
			importeRestante = parseFloat(importePendiente) - parseFloat(importe);
			porcentajeRestante = parseFloat(importeRestante * 100 / total);	

			//si el porcentaje restante es menor que 1, se sustituye el importe a pagar 
			//por el importe pendiente y se recalcula todo	
			if (porcentajeRestante < 1 && porcentajeRestante > 0){
				document.getElementById("importe"+concepto).value = importePendiente;	
				document.getElementById("porcentaje"+concepto).value = porcentajePendiente;							
				actualizaConcepto(concepto, total, importePendiente, porcentajePendiente);
				return;
			}	
			
			document.getElementById("txtRestante"+concepto).value = convertirAFormato(new String(importeRestante)) + "\u20AC (" + convertirAFormato(new String(porcentajeRestante)) + "%)";
		}

		
		function accionEjecutaFacturacion() 
		{		
				//Convierte a formato java los campos de tipo precio
				actualizarCamposPrecio();
				
				var f=document.getElementById("datosGeneralesPagoForm");
				f.modo.value = "ejecutarPago";

				var fname = document.getElementById("datosGeneralesPagoForm").name;
				// con pantalla de espera
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoFacturacion';				
		}

		/**
		 * Envía los datos al action previa validacion 	
		 * de los datos de importe y porcentaje a pagar.
		 */
		function accionGuardar() 
		{						
			var f=document.getElementById("datosGeneralesPagoForm");

			//Obtener la suma del importe a repartir 
			var importeRepartirTotal = 0;
			var conceptos = new Array(4)
			conceptos[0] = 'Oficio';
			conceptos[1] = 'Guardias';
			conceptos[2] = 'EJG';
			conceptos[3] = 'SOJ';

			for (i=0;i<4;i++){
				var objImporte = document.getElementById("importe"+conceptos[i]);
				var importe = convertirANumero(objImporte.value);

				if (isNaN(importe) || importe == 0){
					objImporte.value = importe;
					document.getElementById("porcentaje"+conceptos[i]).value = 0;
				}
				else{
					var objPorcentaje = document.getElementById("porcentaje"+conceptos[i]);
					var porcentaje = convertirANumero(objPorcentaje.value);
					importeRepartirTotal = parseFloat(importeRepartirTotal) + parseFloat(importe);
				}
			} 
			document.getElementById("importeRepartir").value = importeRepartirTotal;
			
			f.target = "submitArea";
			sub();	
	
			if (validateDatosGeneralesPagoForm(f)) {	
				//Calculo lo que le queda por pagar:
				var importeFacturado = 0;
				var importePagado = 0;				
				var total = 0;

				if (f.importeFacturado.value!='' && f.importePagado.value!='') {
					var importeFacturado = parseFloat(f.importeFacturado.value.replace(/,/,"."));
					var importePagado = parseFloat(f.importePagado.value.replace(/,/,"."));	
					//actualiza el importe pagado 
					f.importePagado.value = importeRepartirTotal;	
				}
											
				//Convierte a formato java los campos numericos con decimales
				actualizarCamposPrecio();

				f.submit();

				//Dejo el formato con los 2 decimales y la coma:
				inicializaConceptos();
				formatearCamposprecio();
			}else{
				fin();
				return false;
			}
		}
		

		/**
		 * Borra los datos del formulario
		 * Oculta las fila de cada concepto de la tabla de reparto
		 */
		function accionRestablecer() 
		{		
			document.getElementById("datosGeneralesPagoForm").reset();	
			ocultarConceptos();
		}
		
		function accionCerrarPago() 
		{		
			//Convierte a formato java los campos de tipo precio
			actualizarCamposPrecio();
		
			var f=document.getElementById("datosGeneralesPagoForm");
			<%if (cobroAutomatico.equalsIgnoreCase("si")){%>
				f.modo.value = "cerrarPago";
			<%}else{%>
				f.modo.value = "mostrarColegiadosAPagar";
			<%}%>

			var fname = document.getElementById("datosGeneralesPagoForm").name;
			// con pantalla de espera
			<%if (cobroAutomatico.equalsIgnoreCase("si")){%>
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoPago';
			<%}else{%>
				f.target="submitArea";
				var resultado=ventaModalGeneral(fname,"G");
				if(resultado=='MODIFICADO') refrescarLocal();
			<%}%>
			
			//Dejoel formato con los 2 decimales y la coma:
			formatearCamposprecio();
		}
		
		function accionDefinirCriterio(){
			var f=document.getElementById("datosGeneralesPagoForm");
			f.modo.value = "abrirModal";		
			var salida = ventaModalGeneral(f.name,"M"); 			
			f.modo.value = "modificarPago";
		}
		
		function accionVisualizarCriterios(){
			var f=document.getElementById("datosGeneralesPagoForm");
			f.modo.value = "abrirModal";		
			var salida = ventaModalGeneral(f.name,"M"); 			
			f.modo.value = "modificarPago";
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->
	
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>