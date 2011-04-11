<!-- datosGenerales.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*,com.siga.expedientes.form.ExpDatosGeneralesForm,java.util.*"%>
<%@ page import="com.siga.beans.ExpExpedienteBean"%>
<%@ page import="com.siga.expedientes.form.ExpDatosGeneralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));

	String mostrarMinuta = (String)request.getAttribute("mostarMinuta");
	String mostrarMinutaFinal = (String)request.getAttribute("mostarMinutaFinal");
	String derechosColegiales = (String)request.getAttribute("derechosColegiales");
	
	String totalMinuta = (String) request.getAttribute("totalMinuta");
	if (totalMinuta == null) totalMinuta = new String("");
	
	ArrayList juzgadoSel   = new ArrayList();
	ArrayList materiaSel   = new ArrayList();
	ArrayList pretensionSel   = new ArrayList();
	String idJuzgado="", idInstitucionJuzgado="", idArea="", idMateria="", idPretension="";
	// Datos del Juzgado seleccionado:
	ExpDatosGeneralesForm form = (ExpDatosGeneralesForm) request.getAttribute("ExpDatosGeneralesForm");
	if (form.getJuzgado()!=null) idJuzgado = form.getJuzgado();
	if (form.getIdInstitucionJuzgado()!=null) idInstitucionJuzgado	=  form.getIdInstitucionJuzgado();
	if (form.getIdPretension()!=null) idPretension	=  form.getIdPretension();
	if (form.getIdArea()!=null) idArea		=  form.getIdArea();
	if (form.getIdMateria()!=null) idMateria =  form.getIdMateria();
	if (idMateria!=null) {
		materiaSel.add(0,userBean.getLocation()+","+idArea+","+idMateria);	
	}
	if (idJuzgado!=null && idInstitucionJuzgado!=null) {
		juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado);	
	}
	if (idPretension!=null) {
		pretensionSel.add(0,idPretension);	
	}
	String[] datosJuzgado={userBean.getLocation(),idArea, idMateria};		
	String[] datosMateria={userBean.getLocation()};		
	
	//recupero los campos visibles para mostrar o no ciertas leyendas
	Hashtable camposVisibles = (Hashtable)request.getAttribute("camposVisibles");
	String sNumExpDisc = (String)camposVisibles.get("2");
	String sEstado = (String)camposVisibles.get("3");
	String sInstitucion = (String)camposVisibles.get("4");
	String sAsuntoJud = (String)camposVisibles.get("5");	
	boolean bNumExpDisc = (sNumExpDisc!=null && sNumExpDisc.equals("S"));
	boolean bEstado = (sEstado!=null && sEstado.equals("S"));
	String recargarCombos = bEstado?"recargarCombos()":"";
	boolean bInstitucion = (sInstitucion!=null && sInstitucion.equals("S"));
	boolean bAsuntoJud = (sAsuntoJud!=null && sAsuntoJud.equals("S"));	
	
	String idinst_idtipo_idfase = "";
	String idinst_idtipo_idfase_idestado = "";
	String idclasificacion = "";
	ArrayList vFase = new ArrayList();
	ArrayList vEstado = new ArrayList();
	ArrayList vClasif = new ArrayList();
	
	String accion = (String)request.getAttribute("accion");
	if (!accion.equals("nuevo")){	//pestanhas:edicion o consulta
		idinst_idtipo_idfase = (String)request.getAttribute("idinst_idtipo_idfase");
		idinst_idtipo_idfase_idestado = (String)request.getAttribute("idinst_idtipo_idfase_idestado");
		idclasificacion = (String)request.getAttribute("idclasificacion");
		vFase.add(idinst_idtipo_idfase);
		vEstado.add(idinst_idtipo_idfase_idestado);
		vClasif.add(idclasificacion);
	}
	
	String idinstitucion_tipoexpediente = (String)request.getParameter("idInstitucion_TipoExpediente");
	String tipoExp = (String)request.getParameter("idTipoExpediente");
	String editable = (String)request.getParameter("editable");		
	String soloSeguimiento = (String)request.getParameter("soloSeguimiento");	
	boolean bEditable=false;
	if (soloSeguimiento.equals("true")){
		bEditable=false;
	}else{
		bEditable = editable.equals("1")? true : false;
	}
	//boolean bEditable = editable.equals("1")? true : false;
	String boxStyle = bEditable? "box" : "boxConsulta";
	String boxNumero = bEditable? "boxNumber" : "boxConsultaNumber"; 
	// Estilo de los combos:
	
	String estiloCombo=null, readOnlyCombo=null;

	if (!bEditable){
		estiloCombo = "boxConsulta";
		readOnlyCombo = "true";
	} else {
		estiloCombo = "boxCombo";
		readOnlyCombo = "false";
	}
	
	String dato[] = {idinstitucion_tipoexpediente,tipoExp};
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
	
	String seleccionarPersona = UtilidadesString.getMensajeIdioma(userBean, "general.boton.seleccionar");
	String nuevoNoCol = UtilidadesString.getMensajeIdioma(userBean, "general.new");
	String plazo = UtilidadesString.getMensajeIdioma(userBean, "general.boton.consultarplazo");
	
	ArrayList tipoIVASel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	String[] paramJuz = {userBean.getLocation()};
	
	String idTipoIVA = (String)request.getAttribute("idTipoIVA");
	String idProcedimiento=(String)request.getAttribute("idProcedimiento");
	String idInstitucionProcedimiento=(String)request.getAttribute("idInstitucionProcedimiento");

	String[] paramPro = new String[2];
	paramPro[0]=idJuzgado;
	paramPro[1]=idInstitucionJuzgado;

	String[] paramPretension = {userBean.getLocation()};


	if (idProcedimiento!=null && idInstitucionProcedimiento!=null)
	{
		procedimientoSel.add(0,idProcedimiento+","+idInstitucionProcedimiento);
	}
	
	if (idTipoIVA != null)
	{
		tipoIVASel.add(idTipoIVA);
	}
	String tituloDenunciado = (String)request.getAttribute("tituloDenunciado");
	
	String tituloDenunciante = (String)request.getAttribute("tituloDenunciante");

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="expedientes.auditoria.datosgenerales.literal.titulo" 
		localizacion="expedientes.auditoria.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpDatosGeneralesForm" staticJavascript="false" />  
<% if (bEstado) { %>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	
<% } else { %>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
<% } %>
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script language="JavaScript" src="<%=app%>/html/js/validation.js" type="text/jscript"></script>
	
	
			<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		var jsEstadoViejo="";
		var jsEstadoNuevo="";
		 
		function traspasoDatos(resultado){
		 seleccionComboSiga("juzgado",resultado[0]);
		}	
		
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			<% if (busquedaVolver.equals("AB")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% } else if(busquedaVolver.equals("NB")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<% } else if(busquedaVolver.equals("AV")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% } else if(busquedaVolver.equals("N")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="abrir";	
			<% } else if(busquedaVolver.equals("A")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="abrirAvanzada";	 
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% }  else if (busquedaVolver.equals("Al")){%>
				document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<%}%>
			document.forms[1].submit();	
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			var elemento=parent.document.getElementById('pestana.auditoriaexp.datosgenerales');
			parent.pulsar(elemento,'mainPestanas') 
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			sub();					
			if (validateExpDatosGeneralesForm(document.ExpDatosGeneralesForm)){
				if (document.ExpDatosGeneralesForm.idPersona.value == ""){
						alert('<siga:Idioma key="expedientes.auditoria.literal.denunciado"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
						fin();
						return false;
				}
				else {

						if ((document.ExpDatosGeneralesForm.fechaInicial && document.ExpDatosGeneralesForm.fechaInicial.value != '') || (document.ExpDatosGeneralesForm.fechaFinal.value && document.ExpDatosGeneralesForm.fechaFinal.value!='')) {
							if (compararFecha (document.ExpDatosGeneralesForm.fechaInicial, document.ExpDatosGeneralesForm.fechaFinal) == 1) {
								mensaje = '<siga:Idioma key="messages.expediente.rangoFechasIniFin"/>'
								alert(mensaje);
								fin();
								return false;
							}
						}
						if ((document.ExpDatosGeneralesForm.fechaInicial && document.ExpDatosGeneralesForm.fechaInicial.value != '') || (document.ExpDatosGeneralesForm.fechaProrroga.value && document.ExpDatosGeneralesForm.fechaProrroga.value!='')) {
							if (compararFecha (document.ExpDatosGeneralesForm.fechaInicial, document.ExpDatosGeneralesForm.fechaProrroga) == 1) {
								mensaje = '<siga:Idioma key="messages.expediente.rangoFechasIniPro"/>'
								alert(mensaje);
								fin();
								return false;
							}
						}				
				
						if ((document.ExpDatosGeneralesForm.fechaFinal && document.ExpDatosGeneralesForm.fechaFinal.value != '') || (document.ExpDatosGeneralesForm.fechaProrroga.value && document.ExpDatosGeneralesForm.fechaProrroga.value!='')) {
							if (compararFecha (document.ExpDatosGeneralesForm.fechaFinal, document.ExpDatosGeneralesForm.fechaProrroga) == 1) {
								mensaje = '<siga:Idioma key="messages.expediente.rangoFechasFinPro"/>'
								alert(mensaje);
								fin();
								return false;
							}
						}
						if (document.forms[0].minuta){
							  document.forms[0].minuta.value = document.forms[0].minuta.value.replace(/,/,".");
						}
						if (document.forms[0].importeTotal){
							  document.forms[0].importeTotal.value = document.forms[0].importeTotal.value.replace(/,/,".");
						}
						if (document.forms[0].importeIVA){
							  document.forms[0].importeIVA.value = document.forms[0].importeIVA.value.replace(/,/,".");
						}
						if (document.forms[0].minutaFinal){
							  document.forms[0].minutaFinal.value = document.forms[0].minutaFinal.value.replace(/,/,".");
						}
						if (document.forms[0].importeTotalFinal){
							  document.forms[0].importeTotalFinal.value = document.forms[0].importeTotalFinal.value.replace(/,/,".");
						}
						if (document.forms[0].importeIVAFinal){
							  document.forms[0].importeIVAFinal.value = document.forms[0].importeIVAFinal.value.replace(/,/,".");
						}
						if (document.forms[0].porcentajeIVA){							
							  if(document.forms[0].porcentajeIVA.value.replace(/,/,".") == ""){
								 document.forms[0].porcentajeIVA.value = 0;
							  }else{
								  document.forms[0].porcentajeIVA.value = document.forms[0].porcentajeIVA.value.replace(/,/,".");
							  }
						}

						if (document.forms[0].derechosColegiales){
							  document.forms[0].derechosColegiales.value = document.forms[0].derechosColegiales.value.replace(/,/,".");
						}
										
						<%if (accion.equals("nuevo")){%>
							document.forms[0].modo.value="insertar";
						<%}else{%>
							document.forms[0].modo.value="modificar";
						<%}%>
						document.forms[0].target="submitArea";	
						document.forms[0].submit();	
				}
			}else{
				fin();
				return false;
			}
		}
		
		function recargarComboJuzgado()
		{
			<% if (bEditable){%> 
				<%if (bAsuntoJud){%>
				document.getElementById("juzgado").value='<%=idJuzgado+","+idInstitucionJuzgado %>';
				<%}%>
			<%}%>
		}		
		
		function recargarCombos()
		{
			<%if (bEditable){%> 
				<%if (bAsuntoJud){%>
					document.getElementById("idMateria").value='<%=userBean.getLocation()+","+idArea+","+idMateria %>';
					document.getElementById("idMateria").onchange();
					window.setTimeout('recargarComboJuzgado()',1000,"JavaScript");
					
				<%}%>
			
				var tmp1 = document.getElementsByName("comboFases");
				var tmp2 = tmp1[0];			 
				tmp2.onchange();
				if(document.forms[0].idTipoIVA){// solo aparece el tipo IVA cuando está el campo minuta
				  document.getElementById("idTipoIVA").onchange();
				}
			<%}%>
		}
		
		function refrescarLocal()
		{	
			<%if (accion.equals("nuevo")){%>
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();
			<%}else{%>
				document.location.reload();
			<%}%>
		
		}
		function altaPersona()
		{					
			var resultado=ventaModalGeneral("datosGeneralesForm","G");
			
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value=resultado[0];
				document.forms[0].numColegiado.value=resultado[2];
				document.forms[0].nif.value=resultado[3];
				document.forms[0].nombre.value=resultado[4];
				document.forms[0].primerApellido.value=resultado[5];
				document.forms[0].segundoApellido.value=resultado[6];
				document.forms[0].idDireccion.value=resultado[7];

			}
		}
		
		function seleccionarPersona()
		{	
			document.ExpDenunciadoForm.idPersona.value = document.forms[0].idPersona.value;
			document.ExpDenunciadoForm.idDireccion.value = document.forms[0].idDireccion.value;
			document.ExpDenunciadoForm.modal.value = "seleccion";
			if(document.forms[0].idPersona.value)
				document.ExpDenunciadoForm.modo.value = "editarDenunciado";
			else
				document.ExpDenunciadoForm.modo.value = "nuevo";
				
			var resultado=ventaModalGeneral("ExpDenunciadoForm","M");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value=resultado[0];
				document.forms[0].numColegiado.value=resultado[2];
				document.forms[0].nif.value=resultado[3];
				document.forms[0].nombre.value=resultado[4];
				document.forms[0].primerApellido.value=resultado[5];
				document.forms[0].segundoApellido.value=resultado[6];
				document.forms[0].idDireccion.value=resultado[7];
			
				
				
			}
		}
		
	 	function obtenerJuzgado() 
		{ 
		  	if (document.getElementById("codigoExtJuzgado").value!=""){
			 	document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="";	
			   	document.MantenimientoJuzgadoForm.codigoExt.value=document.getElementById("codigoExtJuzgado").value;
				document.MantenimientoJuzgadoForm.submit();		
		 	}
		}
	
		
		function getPlazo(){
			if (validateExpDatosGeneralesForm(document.ExpDatosGeneralesForm)){
				document.forms[0].modo.value="abrirConParametros";
				document.forms[0].target="submitArea";	
				document.forms[0].submit();
			}
		}
		
		function calcularTotalMinuta () 
		{
			if(document.getElementById("minuta") != null){
				if( document.getElementById("minuta").value != ""){	
					iva = document.getElementById("porcentajeIVA").value.replace(/,/,".");
					if (!iva) {
						iva = 0;
					} 
					minuta = document.getElementById("minuta").value.replace(/,/,".");
					minuta = formatNumber(minuta);
		
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;
			
					document.getElementById("importeIVA").value = b.toFixed(2).toString().replace(".",",");
					document.getElementById("importeTotal").value = a.toFixed(2).toString().replace(".",",");
					document.getElementById("minuta").value = minuta.replace(".",",");
				}
			}
		}

		function formatNumber(value){
			if (!value) {
				value = 0;
			} else if (value.toString().indexOf(".", 0) == -1){
				value = value + ".00";
			} else if (value.toString().charAt(value.toString().length -1) == "."){
				value = value + "00";
			} else if (value.toString().charAt(value.toString().length -2) == "."){
				value = value + "0";
			}

			return value;
		}
		
		function calcularTotalMinuta2 () 
		{
			if(document.getElementById("minuta") != null){
				if( document.getElementById("minuta").value != ""){	
					iva = document.getElementById("porcentajeIVA").value.replace(/,/,".");
					iva = formatNumber(iva);
					minuta = document.getElementById("minuta").value.replace(/,/,".");
					minuta = formatNumber(minuta);
					
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;
					document.getElementById("porcentajeIVA").value = iva.replace(".",",");
					document.getElementById("porcentajeIVAFinal").value = iva.replace(".",",");
					document.getElementById("importeIVA").value = b.toFixed(2).toString().replace(".",",");
					document.getElementById("importeTotal").value = a.toFixed(2).toString().replace(".",",");
					document.getElementById("minuta").value = minuta.replace(".",",");
					calcularTotalMinutaFinal ();
				}
			}
		}

		function calcularTotalMinutaFinal () 
		{			
			if(document.getElementById("minutaFinal") != null){
				if( document.getElementById("minutaFinal").value != ""){				
					iva = document.getElementById("porcentajeIVAFinal").value.replace(/,/,".");
					if (!iva) {
						iva = 0;
					} 
					minuta = document.getElementById("minutaFinal").value.replace(/,/,".");
					minuta = formatNumber(minuta);
					
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;			
					document.getElementById("importeIVAFinal").value = b.toFixed(2).toString().replace(".",",");
					document.getElementById("importeTotalFinal").value = a.toFixed(2).toString().replace(".",",");
					document.getElementById("minutaFinal").value = minuta.replace(".",",");
				}
			}
		}

		function formateoDerechos () 
		{			
			if(document.getElementById("derechosColegiales") != null){
				if( document.getElementById("derechosColegiales").value != ""){				
					derechos = document.getElementById("derechosColegiales").value.replace(/,/,".");
					derechos = formatNumber(derechos);					
					document.getElementById("derechosColegiales").value = derechos.replace(".",",");
				}
			}
		}

		function accionComunicar()
		{
			
			idPersona = '<%=form.getIdPersona()%>';
			idInstitucion= '<%=userBean.getLocation()%>';
			idInstitucionTipoExp= '<%=idinstitucion_tipoexpediente%>';
			idTipoExpediente= '<%=tipoExp%>';
			anioExpediente= '<%=form.getAnioExpediente()%>';
			numeroExpediente= '<%=form.getNumExpediente()%>';

		 
		   	datos = "idInstitucion=="+idInstitucion +"##idInstitucionTipoExp=="+idInstitucionTipoExp +
 		   		 "##idTipoExp==" +idTipoExpediente+"##anioExpediente=="+anioExpediente 
 		   		 +"##numeroExpediente=="+numeroExpediente +"##idPersona=="+idPersona +"%%%";
			
		
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
				formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=userBean.getLocation()%>'>"));
				formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
				formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='EXP'>"));
				formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
				formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
				formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
				formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
			
				document.appendChild(formu);
				formu.datosInforme.value=datos;
				formu.submit();
				
		}
		
		function limpiarFechas(){
			if(jsEstadoViejo==("")){
				jsEstadoViejo = document.ExpDatosGeneralesForm.comboEstados.value;
			}else{
				jsEstadoNuevo = document.ExpDatosGeneralesForm.comboEstados.value;
				if(jsEstadoNuevo!=jsEstadoViejo){
					document.getElementById("fechaInicial").value="";
					document.getElementById("fechaFinal").value="";
					jsEstadoViejo = document.ExpDatosGeneralesForm.comboEstados.value;
				}
			}
		}
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->
		
	
</head>

<body class="detallePestanas" onload="<%=recargarCombos%>, calcularTotalMinuta (), calcularTotalMinutaFinal ()">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<div id="camposRegistro">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center" >
	
	<html:form action="/EXP_Auditoria_DatosGenerales" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "hiddenFrame" value = "1"/>
	
	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">

	<table class="tablaCampos" align="left" border="0">

	<!-- FILA -->
	
		<tr>				
	
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nexpediente"/>
			</td>
			
			<td class="labelTextValue">
				<bean:write name="ExpDatosGeneralesForm" property="numExpediente"/>				
				/
				<bean:write name="ExpDatosGeneralesForm" property="anioExpediente"/>
				<html:hidden name="ExpDatosGeneralesForm" property = "numExpediente"/>
				<html:hidden name="ExpDatosGeneralesForm" property = "anioExpediente"/>			
			</td>
<% if (bNumExpDisc){%>								
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nexpdisciplinario"/>
			</td>		
			<%if (!bEditable){%>
			<td class="labelTextValue" styleClass="box" style= "text-align:right" >
				<bean:write name="ExpDatosGeneralesForm" property="numExpDisciplinario"></bean:write>
				/
				<bean:write name="ExpDatosGeneralesForm" property="anioExpDisciplinario"></bean:write>
			</td>
			<%}else{%>	
			<td class="labelTextValue">			
				<html:text name="ExpDatosGeneralesForm" property="numExpDisciplinario" size="6" maxlength="6" styleClass="box" style="text-align:right;"></html:text>
				/
				<html:text name="ExpDatosGeneralesForm" property="anioExpDisciplinario" size="4" maxlength="4" styleClass="box" style="text-align:right;"></html:text>
			</td>
			<%}%>
<%}else{%>						
			<html:hidden name="ExpDatosGeneralesForm" property="numExpDisciplinario"/>
			<html:hidden name="ExpDatosGeneralesForm" property="anioExpDisciplinario"/>			

<%}%>		
			
			<td  class="labelText">
				<siga:Idioma key="expedientes.gestionarExpedientes.fechaApertura"/>
			</td>
			<td>
				<%if (accion.equals("nuevo")){%>
					<html:text name="ExpDatosGeneralesForm" property="fecha" styleClass="box" readonly="true" size="10"></html:text>
					<a href='javascript://'onClick="return showCalendarGeneral(fecha);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}else{%>
					<html:text name="ExpDatosGeneralesForm" property="fecha" styleClass="boxConsulta" readonly="true"></html:text>
				<%}%>
			</td>

		</tr>						
	
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.tipo"/>
			</td>
			<td>				
				<html:text name="ExpDatosGeneralesForm" property="tipoExpediente" size="25" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
<% if (bEstado){%>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.clasificacion"/>&nbsp(*)
			</td>	
			<td colspan="3">	
				<%if (bEditable){%>		
				<siga:ComboBD nombre = "clasificacion" tipo="cmbClasificacion" clase="boxCombo" obligatorio="false" ElementoSel="<%=vClasif%>" parametro="<%=dato%>"/>
				<%}else{%>
				<html:text name="ExpDatosGeneralesForm" property="clasificacionSel"  styleClass="boxConsulta" readonly="true"></html:text>
				<%}%>
			</td>
<% } else { %>
			<html:hidden name="ExpDatosGeneralesForm" property="clasificacion"  ></html:hidden>

<% } %>
			
			
<% if (bInstitucion){%>
			<td class="labelText"  style="display:none">
				<siga:Idioma key="expedientes.auditoria.literal.institucion"/>
			</td>
			<td colspan="5" align="left" class="labelTextValue" style="display:none">
				<!--<html:text name="ExpDatosGeneralesForm" property="institucion" styleClass="boxConsulta" readonly="true"></html:text>-->
				<bean:write name="ExpDatosGeneralesForm" property="institucion"/>
			</td>
<%}else{%>
			<td colspan="6" style="display:none"></td>
<%}%>	
		</tr>
		<tr>
		<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.fechaCaducidad"/>
			</td>
			<td  valign="top">
					
					<%if (bEditable){%>	
						<siga:Fecha nombreCampo="fechaCaducidad" valorInicial="<%=form.getFechaCaducidad()%>"/>
						<a href='javascript://'onClick="return showCalendarGeneral(fechaCaducidad);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
					<% }else{ %>
						<html:text name="ExpDatosGeneralesForm" property="fechaCaducidad" size="10" maxlength="10" styleClass="<%=boxStyle%>" readonly="true"></html:text>							
					<%}%>
				</td>
				<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.asunto"/>&nbsp(*)
			</td>
			<td colspan="3">
				<html:text name="ExpDatosGeneralesForm" property="asunto" size="78" maxlength="100" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>"></html:text>
			</td>
			</tr>

			<tr>
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.observaciones"/>
				</td>				
				<td colspan="5">
					<html:textarea cols="60" rows="4" property="observaciones" style="width: 815px" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  styleclass="<%=boxStyle%>" ></html:textarea> 
				</td>	
			</tr>

	</table>

	</siga:ConjCampos>
	
	<!-- Esto antes estaba abajo del todo (INICIO)-->
<% if (bEstado){%>
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.estado">
	<table class="tablaCampos" align="center">
	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fase"/>&nbsp(*)
			</td>				
			<td>
				<% if (bEditable) {
					String comboHijo = "";
					if (bEstado)
						comboHijo = "Hijo:comboEstados";
				%>
				<siga:ComboBD nombre = "comboFases" tipo="cmbFases"  clase="boxCombo" obligatorio="true" ElementoSel="<%=vFase%>" parametro="<%=dato%>" accion="<%=comboHijo%>" pestana="t"/>
				<% } else { %>
				<html:text name="ExpDatosGeneralesForm" property="faseSel"  styleClass="boxConsulta" readonly="true"></html:text>
				<% } %>
			</td>

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.estadoyfechainicial"/>&nbsp(*)
			</td>
			
			<td colspan="2">
				<%if (bEditable){%>		
				<siga:ComboBD  nombre = "comboEstados" tipo="cmbEstados" ancho="400" clase="boxCombo" obligatorio="true" accion="parent.limpiarFechas();" ElementoSel="<%=vEstado%>" hijo="t" pestana="t"/>						
				<%}else{%>
				<html:text name="ExpDatosGeneralesForm" property="estadoSel"  styleClass="boxConsulta" readonly="true"></html:text>
				<%}%>
 			</td>			
			
			<td>
				<% if (bEditable){%>
					<siga:Fecha nombreCampo="fechaInicial" valorInicial="<%=form.getFechaInicial()%>"/>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaInicial);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}else{%>
					<html:text name="ExpDatosGeneralesForm" property="fechaInicial" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
					</html:text>
				<%}%>
			</td>			
		</tr>

		<tr>					
				
		<% if (bEditable){%>
			<td class="labelText" colspan="2">
				<input type="button" class="button" alt="<%=plazo%>" id="searchDeadline"  name = "idButton" onclick="return getPlazo();" value="<%=plazo%>"/>&nbsp;
			</td>	
		<%}else{%>
			<td colspan="2">&nbsp;</td>
		<%}%>	
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fechafinal"/>
			</td>
			<td>
				<% if (bEditable){%>
					<siga:Fecha nombreCampo="fechaFinal" valorInicial="<%=form.getFechaFinal()%>"/>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaFinal);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}else {%>	
					<html:text name="ExpDatosGeneralesForm" property="fechaFinal" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
					</html:text>
				<%}%>
			</td>		
			
			<td class="labelText" style="text-align: right; width: 180"'>
				<siga:Idioma key="expedientes.auditoria.literal.fechaprorroga"/>
			</td>
			<td>
				<% if (bEditable){%>
					<siga:Fecha nombreCampo="fechaProrroga" valorInicial="<%=form.getFechaProrroga()%>"/>
					<a href="javascript://" onClick="return showCalendarGeneral(fechaProrroga);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}else{%>
					<html:text name="ExpDatosGeneralesForm" property="fechaProrroga" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true"></html:text>
				<%} %>
			</td>	
			<td>&nbsp;</td> <!-- Vacio para cuadrar el interfaz -->		
		</tr>

	</table>
		
	</siga:ConjCampos>
<%}else{%>	
	<html:hidden name="ExpDatosGeneralesForm" property="comboEstados"/>
	<html:hidden name="ExpDatosGeneralesForm" property="comboFases"/>
	<html:hidden name="ExpDatosGeneralesForm" property="estadoSel"/>
	<html:hidden name="ExpDatosGeneralesForm" property="faseSel"/>
	<html:hidden name="ExpDatosGeneralesForm" property="fechaInicial"/>
	<html:hidden name="ExpDatosGeneralesForm" property="fechaFinal"/>
	<html:hidden name="ExpDatosGeneralesForm" property="fechaProrroga"/>
<%}%>
<!-- Esto antes estaba abajo del todo (FIN)-->
	
<% if (mostrarMinuta != null && mostrarMinuta.equalsIgnoreCase("S")) {%>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.minuta">
		<table class="tablaCampos" border="0">
			<tr>
				<td class="labelText" width="150">
					<siga:Idioma key="expedientes.auditoria.literal.minuta"/>
				</td>
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="minuta" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinuta();" ></html:text> &euro;
				</td>
				
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.porcentajeIVA"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="porcentajeIVA" size="6" maxlength="6" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterCharsNumberEs(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinuta2();" ></html:text> %
				</td>
				
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.importeIVA"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="importeIVA" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true"  onblur="filterCharsNaN(this);" ></html:text> &euro;
				</td>
				
				<td class="labelText" width="10%">
					<siga:Idioma key="expedientes.auditoria.literal.totalMinuta"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="importeTotal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true" onblur="filterCharsNaN(this);" ></html:text> &euro;
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	
<% } %>

<% if (mostrarMinutaFinal != null && mostrarMinutaFinal.equalsIgnoreCase("S")) {%>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.minutafinal">
		<table class="tablaCampos" border="0">
			<tr>
				<td class="labelText" width="150">
					<siga:Idioma key="expedientes.auditoria.literal.minutafinal"/>
				</td>
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="minutaFinal" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinutaFinal();" ></html:text> &euro;
				</td>
				
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.porcentajeIVA"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="porcentajeIVAFinal" size="6" maxlength="6" styleClass="boxConsultaNumber" readonly="true"  onblur="filterCharsNaN(this);"></html:text> %
				</td>
				
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.importeIVA"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="importeIVAFinal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true"  onblur="filterCharsNaN(this);" ></html:text> &euro;
				</td>
				
				<td class="labelText" width="10%">
					<siga:Idioma key="expedientes.auditoria.literal.totalMinuta"/>
				</td>				
				<td class="labelTextValue">
					<html:text name="ExpDatosGeneralesForm" property="importeTotalFinal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true"  onblur="filterCharsNaN(this);" ></html:text> &euro;
				</td>
			</tr>
			</table>
			<% if (derechosColegiales != null && derechosColegiales.equalsIgnoreCase("S")) {%>			
				<table>
					<td class="labelText" width="150">
						<siga:Idioma key="expedientes.auditoria.literal.derechoscolegiales"/>
					</td>
					<td class="labelTextValue" >
						<html:text name="ExpDatosGeneralesForm" property="derechosColegiales" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterCharsNumberEs(this,false,true);" onkeyup="filterCharsUp(this);" onblur="formateoDerechos();"></html:text> &euro;
					</td>
				</table>
			<% } %>
		
	</siga:ConjCampos>
	
<% } %>
	
	<siga:ConjCampos leyenda="<%=tituloDenunciado%>">

	<table class="tablaCampos" align="center">

	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<html:hidden name="ExpDatosGeneralesForm" property = "idPersona"/>
				<html:hidden name="ExpDatosGeneralesForm" property = "idDireccion"/>
				
				<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp(*)
			</td>				
			<td>
				<html:text name="ExpDatosGeneralesForm" property="nombre" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="primerApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="segundoApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>	 
		</tr>
	
	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nif"/>&nbsp(*)
			</td>				
			<td>				
				<html:text name="ExpDatosGeneralesForm" property="nif" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.ncolegiado"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="numColegiado" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
<% if (bEditable){%>			
			<td colspan="2" align="right">
				
				<input type="button" class="button" alt="<%=seleccionarPersona%>" id="newPerson" name = "idButton"  onclick="return seleccionarPersona();" value="<%=seleccionarPersona%>"/>
				&nbsp;
				<input type="button" class="button" alt="<%=nuevoNoCol%>" id="newPerson" name = "idButton"  onclick="return altaPersona();" value="<%=nuevoNoCol%>"/>
			</td>	
<%}else{%>
			<td colspan="2"></td>
<%}%>			
		</tr>
	
	</table>
		
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="<%=tituloDenunciante%>">

	<table class="tablaCampos" align="center">

	<!-- FILA -->
		<tr>					
			<td class="labelText">			
				<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp
			</td>				
			<td>
				<html:text name="ExpDatosGeneralesForm" property="nombreDenunciante" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>&nbsp
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="primerApellidoDenunciante" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>&nbsp
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="segundoApellidoDenunciante" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>	 
		</tr>
	
	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nif"/>&nbsp
			</td>				
			<td>				
				<html:text name="ExpDatosGeneralesForm" property="nifDenunciante" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>


<%if (bAsuntoJud){%>
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.asuntojudicial">

	<table class="tablaCampos" align="center">

	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.materia"/>
			</td>				
			<td>
				<%if(bEditable){%>
				 	  <siga:ComboBD nombre="idMateria" tipo="materiaarea" ancho="250" clase="<%=estiloCombo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosMateria%>" elementoSel="<%=materiaSel%>" accion="Hijo:juzgado" readonly="false"/>           	   
				<%}else{%>
					  <siga:ComboBD nombre="idMateria" tipo="materiaarea" ancho="250" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosMateria%>" elementoSel="<%=materiaSel%>"  accion="Hijo:juzgado" readonly="true"/>           	   
				<%}%>							
				
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.juzgado"/>
			</td>				
			<td COLSPAN="3">
				<%if(bEditable){%>
				 	  <input type="text" name="codigoExtJuzgado" class="box" size="3"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />
				 	  <siga:ComboBD nombre="juzgado" tipo="comboJuzgadosMateria" ancho="330" clase="<%=estiloCombo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuzgado%>" elementoSel="<%=juzgadoSel%>" hijo="t" accion="Hijo:procedimiento" readonly="false"/>           	   
				<%}else{%>
						<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosMateria" ancho="330" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuzgado%>" elementoSel="<%=juzgadoSel%>" hijo="t" accion="Hijo:procedimiento" readonly="true"/>           	   
				<%}%>							
				
			</td>
		</tr>					
		<tr>					
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.procedimiento"/>
			</td>
			<td>
				<siga:ComboBD nombre="procedimiento" tipo="comboProcedimientos" estilo="true" clase="<%=estiloCombo%>" ancho="250" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" hijo="t" parametro="<%=paramPro%>" elementoSel="<%=procedimientoSel%>" pestana="true"/>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nasunto"/>
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="numAsunto" size="10" maxlength="20" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>"></html:text>
			</td>	
		</tr>

		<tr>					
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.pretensiones"/>
			</td>
			<td>
				<siga:ComboBD nombre="idPretension" tipo="comboPretensiones" estilo="true" clase="<%=estiloCombo%>" ancho="250" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" parametro="<%=paramPretension%>" elementoSel="<%=pretensionSel%>" pestana="true"/>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.otrasPretensiones"/>
			</td>
			<td>
				<html:text name="ExpDatosGeneralesForm" property="otrasPretensiones" size="50" maxlength="500" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>"></html:text>
			</td>	
		</tr>
	
		
	
	</table>
		
	</siga:ConjCampos>
<%}else{%>	
	<html:hidden name="ExpDatosGeneralesForm" property="idMateria"/>
	<html:hidden name="ExpDatosGeneralesForm" property="juzgado"/>
	<html:hidden name="ExpDatosGeneralesForm" property="procedimiento"/>
	<html:hidden name="ExpDatosGeneralesForm" property="numAsunto"/>
<%}%>
	

	
			
	</td>
	</tr>
	</html:form>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<%if (accion.equals("nuevo")){%>
		<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle" />
	<% } else if (bEditable){%>
		<siga:ConjBotonesAccion botones="V,R,G,COM" clase="botonesDetalle" />
	<% } else{%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } %>	
	
</div>	

	<html:form action="/EXP_AuditoriaExpedientes" method="POST" target="mainWorkArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "avanzada" value = ""/>		
	</html:form>

	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
	</html:form>
		
	<html:form action="/EXP_Auditoria_Denunciado" method="POST" target="mainWorkArea">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = "altaNoColegiado"/>
		<html:hidden property = "modal" value = "seleccion"/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idDireccion"/>
		<html:hidden property = "idInstitucion"/>
		
		
		
		
	</html:form>

	<html:form action = "/JGR_MantenimientoJuzgados" method="POST" target="submitArea33">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	<html:form action="/CEN_DatosGenerales" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="1">
		<input type="hidden" name="modo" value="altaNoColegiado">
	</html:form>
	
		
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea33" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>