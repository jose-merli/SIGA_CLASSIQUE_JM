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
	boolean esNuevo = accion.equalsIgnoreCase("nuevo");
	boolean esConsulta = accion.equalsIgnoreCase("consulta");
	boolean esEdicion = accion.equalsIgnoreCase("edicion");
	boolean esVerEditar = esEdicion || esConsulta;
	boolean estadoAbierto = idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_ABIERTO);
	String scriptOnLoad = "ocultarConceptos();";
	if (esVerEditar) {
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
		
		importeOficio = pagosBean.getImporteOficio()==null?"":pagosBean.getImporteOficio().toString();
		importeGuardias = pagosBean.getImporteGuardia()==null?"":pagosBean.getImporteGuardia().toString();
		importeEJG = pagosBean.getImporteEJG()==null?"":pagosBean.getImporteEJG().toString();
		importeSOJ = pagosBean.getImporteSOJ()==null?"":pagosBean.getImporteSOJ().toString();
		
		cantidad = pagosBean.getImporteRepartir()==null?"":pagosBean.getImporteRepartir().toString();
		importePagado = pagosBean.getImportePagado()==null?"":pagosBean.getImportePagado().toString();

		Hashtable conceptos = (Hashtable)request.getAttribute("CONCEPTOS");

		porcentajeOficio = (String)conceptos.get("PORCENTAJEOFICIO");
		porcentajeGuardias = (String)conceptos.get("PORCENTAJEGUARDIAS");
		porcentajeEJG = (String)conceptos.get("PORCENTAJEEJG");
		porcentajeSOJ = (String)conceptos.get("PORCENTAJESOJ");

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
		
		estadoAbierto = idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_ABIERTO);
		scriptOnLoad = "inicializarVerEditar();";
		if (esConsulta || !estadoAbierto){
			scriptOnLoad += " ocultarRestante();";
		}

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
	if (esNuevo) {
		nombreEstado="";
		fechaEstado="";
		b_lectura = false;
		estilo = "box";
		estiloNumber="boxNumber";
	} else {
		if (esEdicion) {			
			estilo = "box";
			estiloNumber="boxNumber";			
			if (idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_EJECUTADO) || idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_CERRADO)) {
				b_lectura = true;
				estilo = "boxConsulta";
				estiloNumber="boxConsultaNumber";
			}
		} else if (esConsulta) {
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
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

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

		var conceptos = new Array(4);
		conceptos[0] = 'Oficio';
		conceptos[1] = 'Guardias';
		conceptos[2] = 'EJG';
		conceptos[3] = 'SOJ';
	
	</script>
	
	<script>
		function convertirAFormato(n){
			if (n.toString().indexOf(".", 0) != -1  && n.toString().indexOf(",", 0) != -1){
				var d = n.replace(".","");
				for (;d.toString().indexOf(".", 0) != -1;)
				{
					d = d.replace(".","");
				}				
				d = d.replace(",",".");
				d = new Number(d);
				d = Number(d.toFixed(2));
				d = d.toString();
			}else{
				var d = n.replace(",",".");
				d = new Number(d);
				d = Number(d.toFixed(2));
				d = d.toLocaleString();
				//d = d.replace(".","");
				if(String(d).indexOf(',') < 0){
					d += ',00'; // aqui puede variar segun la cantidad de decimales que desees;
				}				
			}
			return d;	
		}

		function convertirANumero2(n){
			return convertirAFormato(n).replace(",",".");
		}


		function convertirANumero(n)
		{
			numero = convertirAFormato(n);
			if (numero.toString().indexOf(".", 0) != -1  && numero.toString().indexOf(",", 0) != -1){
				for (;numero.toString().indexOf(".", 0) != -1;)
				{
					numero = numero.replace(".","");
				}
				numero = numero.replace(",",".");
				return numero;
			}else{
				numero = numero.replace(",",".");
				return numero;
			}
		}

		

		function convertirANumeroGuardar(n)
		{
			numero = convertirAFormato(n);
		
			for (;numero.toString().indexOf(".", 0) != -1;)
			{			
				numero = numero.replace(".","");					
			}											
			numero = numero.replace(",",".");
			return numero;			
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
			// Al cargar una nueva facturacion hay que borrar los importes
			// a pagar que se hubieran introducido para otra facturacion
			borrarAPagar();
		}

		/**
		* Borra los campos "a pagar" de cada concepto
		*/
		function borrarAPagar(){
			//Obtener la suma del importe a repartir 
			var importeRepartirTotal = 0;
			for (i=0;i<4;i++){			
				//inicializa el valor de los elementos
				document.getElementById("importe"+conceptos[i]).value = "0,00";
				document.getElementById("porcentaje"+conceptos[i]).value = "0,00";
				//inicializa el aspecto de los elementos
				document.getElementById("porcentaje"+conceptos[i]).className  = "<%=estiloNumber%>";	
				document.getElementById("porcentaje"+conceptos[i]).readOnly = false;
				document.getElementById("importe"+conceptos[i]).readOnly = true;
				document.getElementById("importe"+conceptos[i]).className  = "boxConsultaNumber";
				document.getElementById("radioImporte"+conceptos[i]).checked  = false;
				document.getElementById("radioPorcentaje"+conceptos[i]).checked  = true;
			}
		}

		/**
		* Inicializa campos "a pagar" de cada concepto
		*/
		function inicializaAPagar(){
			for (i=0;i<4;i++){			
				//inicializa el valor de los elementos
				document.getElementById("importe"+conceptos[i]).value = convertirAFormato(eval("importe"+conceptos[i])+"");
				actualizaConcepto(conceptos[i], eval("total"+conceptos[i]), eval("importePend"+conceptos[i]), 0);
				document.getElementsByName("radioAPagar"+conceptos[i])[1].checked=true;
				cambiar(conceptos[i]);
			}
		}
		
		 
		/**
		 * Calcula los importes y porcentajes pendientes de cada concepto
		 */
		function calcularPendiente(){
			// actualiza las variables javascript de importe pendiente y porcentaje pendiente 
			// para cada concepto
			
			/*alert(totalOficio + " - " + importePagadoOficio + " - " + porcentajePagadoOficio + " - " + 
				    totalGuardias + " - " + importePagadoGuardias + " - " + porcentajePendGuardias + " - " + 
				    totalEJG + " - " + importePagadoEJG + " - " + porcentajePagadoEJG + " - " + 
			        totalSOJ + " - " + importePagadoSOJ + " - " + porcentajePagadoSOJ);*/
			
			importePendOficio = roundNumber(parseFloat(totalOficio) - parseFloat(importePagadoOficio), 2);
			porcentajePendOficio = roundNumber(parseFloat(100) - parseFloat(porcentajePagadoOficio), 2);
			
			importePendGuardias = roundNumber(parseFloat(totalGuardias) - parseFloat(importePagadoGuardias), 2);
			porcentajePendGuardias = roundNumber(parseFloat(100) - parseFloat(porcentajePagadoGuardias), 2);

			importePendEJG = roundNumber(parseFloat(totalEJG) - parseFloat(importePagadoEJG), 2);
			porcentajePendEJG = roundNumber(parseFloat(100) - parseFloat(porcentajePagadoEJG), 2);

			importePendSOJ = roundNumber(parseFloat(totalSOJ) - parseFloat(importePagadoSOJ), 2);
			porcentajePendSOJ = roundNumber(parseFloat(100) - parseFloat(porcentajePagadoSOJ), 2);
		}
		
		/**
		 * Inicializa los datos cuando la accion es ver o editar
		 */
		function inicializarVerEditar(){
			calcularPendiente();
			inicializaConceptos();
			<% if (esEdicion && estadoAbierto){%>
			inicializaAPagar();
			<% } %>			
			/*alert(importePendOficio + " - " + porcentajePendOficio + " - " + 
			  importePendGuardias + " - " + porcentajePendGuardias + " - " + 
			  importePendEJG + " - " + porcentajePendEJG + " - " + 
			  importePendSOJ + " - " + porcentajePendSOJ);*/
		}
		
		/**
		 * Oculta las fila de cada concepto de la tabla de reparto
		 */
		function ocultarConceptos(){
			for (i=0;i<4;i++){					
				document.getElementById("fila"+conceptos[i]).style.display="none";
			}
		}

		/**
		 * Oculta la columna con los importes restantes 
		 */
		function ocultarRestante(){
			  fila=document.getElementById('tablaConceptos').getElementsByTagName('tr');
			  for(i=0;i<fila.length;i++)
			    fila[i].getElementsByTagName('td')[4].style.display="none";
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

<body  onload="<%=scriptOnLoad%>">

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
			<% if (esNuevo) { %>
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
			<table id="tablaConceptos" border="1" width="100%" cellspacing='0' cellpadding='0'>	
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
						<input name="txtTotalOficio" id="txtTotalOficio" size="18" class="boxConsultaNumber" readonly="true" />
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteOficio" id="txtPendienteOficio" size="18" class="boxConsultaNumber" readonly="true" />	
					</td>
					<td class="labelTextNum" >
						<% if (esNuevo || (esEdicion && estadoAbierto)){ %>
							<input name="radioAPagarOficio" id="radioImporteOficio" value="importe" type="radio" onclick="cambiar('Oficio');" Checked/>
							<input name="importeOficio" id="importeOficio" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('Oficio', totalOficio, importePendOficio, porcentajePendOficio);" onfocus="backup('importeOficio')" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioApagarOficio" id="radioPorcentajeOficio" value="porcentaje" type="radio" onclick="cambiar('Oficio');" />
							<input name="porcentajeOficio" id="porcentajeOficio" style="width:15%" maxlength="6" class="boxConsultaNumber" readonly="true" onblur="actualizaConcepto('Oficio', totalOficio, importePendOficio, porcentajePendOficio);"	onfocus="backup('porcentajeOficio');" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarOficio" id="txtAPagarOficio" style="width:100%" class="boxConsultaNumber" readonly="true"/>
							<input type="hidden" name="importeOficio" id="importeOficio"/>
							<input type="hidden" name="porcentajeOficio" id="porcentajeOficio"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteOficio" id="txtRestanteOficio" style="width:100%" class="boxConsultaNumber" readonly="true" />
					</td>
				</tr>
				<!-- GUARDIAS -->
				<tr id="filaGuardias" style="display:block">				
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.Guardias"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalGuardias" id="txtTotalGuardias" size="18" class="boxConsultaNumber" readonly="true"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteGuardias" id="txtPendienteGuardias" size="18" class="boxConsultaNumber" readonly="true"/>	
					</td>
					<td class="labelTextNum" >
						<% if (esNuevo || (esEdicion && estadoAbierto)){ %>
							<input name="radioAPagarGuardias" id="radioImporteGuardias" value="importe" type="radio" onclick="cambiar('Guardias');" Checked/>
							<input name="importeGuardias" id="importeGuardias" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('Guardias', totalGuardias, importePendGuardias, porcentajePendGuardias);" onfocus="backup('importeGuardias');" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarGuardias" id="radioPorcentajeGuardias" value="porcentaje" type="radio" onclick="cambiar('Guardias');"/>
							<input name="porcentajeGuardias" id="porcentajeGuardias" style="width:15%" maxlength="6" class="boxConsultaNumber" readonly="true"  									
										onblur="actualizaConcepto('Guardias', totalGuardias, importePendGuardias, porcentajePendGuardias);"
										onfocus="backup('porcentajeGuardias');" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarGuardias" id="txtAPagarGuardias" size="18" class="boxConsultaNumber" readonly="true"/>
							<input type="hidden" name="importeGuardias" id="importeGuardias"/>
							<input type="hidden" name="porcentajeGuardias" id="porcentajeGuardias"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteGuardias" id="txtRestanteGuardias" size="18" class="boxConsultaNumber" readonly="true"/>
					</td>
				</tr>
				<!-- EJG -->
				<tr id="filaEJG" style="display:block">				
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.EJG"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalEJG" id="txtTotalEJG" size="18" class="boxConsultaNumber" readonly="true"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteEJG" id="txtPendienteEJG" size="18" class="boxConsultaNumber" readonly="true"/>	
					</td>
					<td class="labelTextNum" >
						<% if (esNuevo || (esEdicion && estadoAbierto)){ %>
							<input name="radioAPagarEJG" id="radioImporteEJG" value="importe" type="radio" onclick="cambiar('EJG');" Checked/>
							<input name="importeEJG" id="importeEJG" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('EJG', totalEJG, importePendEJG, porcentajePendEJG);" onfocus="backup('importeEJG');" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarEJG" id="radioPorcentajeEJG" value="porcentaje" type="radio" onclick="cambiar('EJG');"/>
							<input name="porcentajeEJG" id="porcentajeEJG" style="width:15%" maxlength="6" class="boxConsultaNumber" readonly="true" onblur="actualizaConcepto('EJG', totalEJG, importePendEJG, porcentajePendEJG);" onfocus="backup('porcentajeEJG');" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarEJG" id="txtAPagarEJG" size="18" class="boxConsultaNumber" readonly="true"/>
							<input type="hidden" name="importeEJG" id="importeEJG"/>
							<input type="hidden" name="porcentajeEJG" id="porcentajeEJG"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteEJG" id="txtRestanteEJG" size="18" class="boxConsultaNumber" readonly="true"/>
					</td>
				</tr>
				<!-- SOJ -->
				<tr id="filaSOJ" style="display:block">	
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosPagos.literal.SOJ"/>
					</td>				
					<td class="labelTextNum">
						<input name="txtTotalSOJ" id="txtTotalSOJ" size="18" class="boxConsultaNumber" readonly="true"/>
					</td>
					<td class="labelTextNum">
						<input name="txtPendienteSOJ" id="txtPendienteSOJ" size="18" class="boxConsultaNumber" readonly="true"/>	
					</td>
					<td class="labelTextNum" >
						<% if (esNuevo || (esEdicion && estadoAbierto)){ %>
							<input name="radioAPagarSOJ" id="radioImporteSOJ" value="importe" type="radio" onclick="cambiar('SOJ');" Checked/>
							<input name="importeSOJ" id="importeSOJ" style="width:50%" class="<%=estiloNumber%>" onblur="actualizaConcepto('SOJ', totalSOJ, importePendSOJ, porcentajePendSOJ);" onfocus="backup('importeSOJ');" />						
							<span style="vertical-align:40%">&euro;	&nbsp;</span>
						  	<input name="radioAPagarSOJ" id="radioPorcentajeSOJ" value="porcentaje" type="radio" onclick="cambiar('SOJ');"/>
							<input name="porcentajeSOJ" id="porcentajeSOJ" style="width:15%" maxlength="6" class="boxConsultaNumber" readonly="true" onblur="actualizaConcepto('SOJ', totalSOJ, importePendSOJ, porcentajePendSOJ);" onfocus="backup('porcentajeSOJ');" />						
							<span style="vertical-align:40%">&#37;</span>
						<% } else {%>
							<input name="txtAPagarSOJ" id="txtAPagarSOJ" size="18" class="boxConsultaNumber" readonly="true"/>
							<input type="hidden" name="importeSOJ" id="importeSOJ"/>
							<input type="hidden" name="porcentajeSOJ" id="porcentajeSOJ"/>
						<% } %>
					</td>
					<td class="labelTextNum">
						<input name="txtRestanteSOJ" id="txtRestanteSOJ" size="18" class="boxConsultaNumber" readonly="true"/>
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
			botones = "V,G";
		else {
			if (accion.equalsIgnoreCase("edicion")) {
				if (idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_ABIERTO))) botones += ",EF,G";
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
		
		/**
		 * Convierte a formato java los campos de tipo precio
		 */
		function actualizarCamposPrecio(){
			document.forms[0].importeRepartir.value=document.forms[0].importeRepartir.value.replace(/,/,".");
			document.forms[0].importePagado.value=document.forms[0].importePagado.value.replace(/,/,".");
			document.forms[0].importeFacturado.value=document.forms[0].importeFacturado.value.replace(/,/,".");
			
			//Convierte los importes y porcentajes de cada concepto
			for (i=0;i<4;i++){
				var objPorcentaje = document.getElementById("porcentaje"+conceptos[i]);
				var objImporte = document.getElementById("importe"+conceptos[i]);
				if (objImporte.value.toString().indexOf(".", 0) != -1  && objImporte.value.toString().indexOf(",", 0) != -1){			
					objImporte.value = convertirAFormato(document.getElementById("importe"+conceptos[i]).value);
					objPorcentaje.value = objPorcentaje.value.replace(/,/,".");
				}else{
					objImporte.value = objImporte.value.replace(/,/,".");
					objPorcentaje.value = objPorcentaje.value.replace(/,/,".");
				}
			}
		}
		
		function formatearCamposprecio(){
			document.forms[0].importeRepartir.value = convertirAFormato(document.forms[0].importeRepartir.value);
			document.forms[0].importePagado.value = convertirAFormato(document.forms[0].importePagado.value);
			document.forms[0].importeFacturado.value = convertirAFormato(document.forms[0].importeFacturado.value);
			for (i=0;i<4;i++){			
				document.getElementById("importe"+conceptos[i]).value = convertirAFormato(document.getElementById("importe"+conceptos[i]).value);
				document.getElementById("porcentaje"+conceptos[i]).value = convertirAFormato(document.getElementById("porcentaje"+conceptos[i]).value);
			}
		}

		
		/**
		 *
		 */
		function inicializaConceptos(){
			for (i=0;i<4;i++){	
				if (eval("total"+conceptos[i])==0)
					document.getElementById("fila"+conceptos[i]).style.display = "none";
				else{
					document.getElementById("fila"+conceptos[i]).style.display = "inline";				
					document.getElementById("txtTotal"+conceptos[i]).value = convertirAFormato(eval("total"+conceptos[i])+"") + '\u20AC';
					document.getElementById("txtPendiente"+conceptos[i]).value = convertirAFormato(eval("importePend"+conceptos[i])+"") + '\u20AC (' + convertirAFormato(eval("porcentajePend"+conceptos[i])+"") + '%)';
					document.getElementById("txtRestante"+conceptos[i]).value = convertirAFormato(eval("importePend"+conceptos[i])+"") + '\u20AC (' + convertirAFormato(eval("porcentajePend"+conceptos[i])+"") + '%)';
					<% if (esConsulta || esEdicion && !estadoAbierto){%>
					document.getElementById("txtAPagar"+conceptos[i]).value = convertirAFormato(eval("importe"+conceptos[i])+"") + '\u20AC (' + convertirAFormato(eval("porcentaje"+conceptos[i])+"") + '%)';					
					document.getElementById("importe"+conceptos[i]).value = convertirAFormato(eval("importe"+conceptos[i])+"");
					document.getElementById("porcentaje"+conceptos[i]).value = convertirAFormato(eval("porcentaje"+conceptos[i])+"");;
					<% }%>
				}
			}
		}


		/**
		 * Guarda el valor de un input en una variable.
		 * Esta funcion se llama cuando se entra en cada uno de los inputs "a pagar"
		 */
		function backup(valor){
			backupAPagar = document.getElementById(valor).value;
		}

		/**
		 * Cambia el valor
		 */
		function cambiar(concepto){
			if (document.getElementsByName("radioAPagar"+concepto)[0].checked){
				document.getElementById("porcentaje"+concepto).className  = "boxConsultaNumber";	
				document.getElementById("porcentaje"+concepto).readOnly = true;
				document.getElementById("importe"+concepto).readOnly = false;
				document.getElementById("importe"+concepto).className  = "<%=estiloNumber%>";
			}
			else{			
				document.getElementById("importe"+concepto).className  = "boxConsultaNumber";
				document.getElementById("importe"+concepto).readOnly = true;
				document.getElementById("porcentaje"+concepto).readOnly = false;
				document.getElementById("porcentaje"+concepto).className  = "<%=estiloNumber%>";				
			}
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
			//por el importe pendiente y se actualiza el importe restante
			if ( porcentajeRestante < 1 && porcentajeRestante > 0 || importeRestante < 0){				
				document.getElementById("importe"+concepto).value = convertirAFormato(""+importePendiente);	
				document.getElementById("porcentaje"+concepto).value = porcentajePendiente;	
				importeRestante = 0;
				porcentajeRestante = 0;	
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
				window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoFacturacion';				
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

			var restantes = new Array(4);
			
			for (i=0;i<4;i++){
				var objImporte = document.getElementById("importe"+conceptos[i]);
				var importe = convertirANumeroGuardar(objImporte.value);
				
				//Copia del importe restante para recuperarla tras enviar el formulario
				restantes[i] = document.getElementById("txtRestante"+conceptos[i]).value;
				
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
			// Guarda los valores de los importes a repartir y pagado
			// para recuperarlos una vez enviados al guardar si 
			// se esta editando el pago en el estado ABIERTO
			var iportePagadoAux = f.importePagado.value;

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

				<% if (esEdicion && estadoAbierto){%>
				// Se recuperan los datos del importe Pagado si se edita el pago con estado ABIERTO
				f.importePagado.value = iportePagadoAux;
				for (i=0;i<4;i++){
					document.getElementById("txtRestante"+conceptos[i]).value = restantes[i];
				}
				<% } %>	

				
				
				formatearCamposprecio();
			}else{
				fin();
				formatearCamposprecio();				
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
			window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoPago';
			<%}else{%>
				f.target="submitArea";
				var resultado=ventaModalGeneral(fname,"G");
				if(resultado=='MODIFICADO') refrescarLocal();
			<%}%>
			
			//Dejo el formato con los 2 decimales y la coma:
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
