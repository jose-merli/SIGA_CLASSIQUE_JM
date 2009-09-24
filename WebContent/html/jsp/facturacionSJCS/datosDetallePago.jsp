<!-- datosDetallePago.jsp -->
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
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>

<!-- JSP -->
<% 
	//elementos para las tablas, botones
	FilaExtElement[] elems=new FilaExtElement[1];
	elems[0]=new FilaExtElement("editar","editar",SIGAConstants.ACCESS_FULL);

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	//variables a mostrar en la .jsp
	String turno="", asunto="", fecha="", importe="", importeTotal="", guardia="", fechaInicio="", motivo="",
			fechaFin="", asistencia="", cliente="", descripcion="", tipo="", nExpediente="", importeIrpf="",
			importeTotalIrpf="", nombre="", numeroAsunto ="", idCalendario="", idPersona="", anioNumero="",
			idInstitucion ="", idTurno="", idGuardia="", anio="", numero="", idActuacion="", idTipoSoj="", 
			idTipoEjg="", idMovimiento="", idRetencion="", porcentajeIRPF="", diasACobrar="";
	String salidaPorcentajeIrpf = "";
	
	//Modo de consulta original:
	String modoOriginal = request.getAttribute("modoOriginal")==null?"CONSULTA":(String)request.getAttribute("modoOriginal");
	
	//contador
	double total=0, totalFinal=0, totalIrpf=0, totalFinalIrpf=0;

	// vector resultado
	Vector resultado = (Vector)request.getAttribute("resultado");

	//recogemos el estado del pago
	boolean estadoMayorQueEjecutado=false;
	String estado = (String)request.getAttribute("estado");
	try {
		//Deshabilito el boton de Edicion si el estado es mayor a ejecutado o estamos en modo consulta:
		if ( (Integer.parseInt(estado) > Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) || (modoOriginal.equalsIgnoreCase("CONSULTA"))) {
			estadoMayorQueEjecutado = true;
			elems = null;
		}
	} catch(Exception e){
			estadoMayorQueEjecutado = false;
			elems = null;
	}

	// Campos de la factura a mostrar en la jsp
	Vector turnosOficio = new Vector();
	Vector guardiasPresenciales = new Vector();
	Vector asistencias = new Vector();
	Vector actuaciones = new Vector();
	Vector expSoj = new Vector();
	Vector expEjg = new Vector();
	Vector movimientos = new Vector();
	Vector retenciones = new Vector();
	try{
		turnosOficio = (Vector)resultado.get(0);
	}catch(Exception e){turnosOficio=null;}
	try{
		guardiasPresenciales = (Vector)resultado.get(1);
	}catch(Exception e){guardiasPresenciales=null;}
	try{
		asistencias = (Vector)resultado.get(2);
	}catch(Exception e){asistencias=null;}
	try{
		actuaciones = (Vector)resultado.get(3);
	}catch(Exception e){actuaciones=null;}
	try{
		expSoj = (Vector)resultado.get(4);
	}catch(Exception e){expSoj=null;}
	try{
		expEjg = (Vector)resultado.get(5);
	}catch(Exception e){expEjg=null;}
	try{
		movimientos = (Vector)resultado.get(6);
	}catch(Exception e){movimientos=null;}

	if (resultado.size()>7){
		try{
			retenciones = (Vector)resultado.get(7);
		}catch(Exception e){retenciones=null;}
	}

	//recogemos el nombre del colegiado
	String nombreColegiado = (String)request.getAttribute("nombreColegiado");

	//recogemos el número del colegiado
	String numeroColegiado = (String)request.getAttribute("numeroColegiado");

	//recogemos el nombre del colegio
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");

	//variables para mostrar los resultados totales parciales y final
	String valorFinalTotal="", valorFinal="", valorFinalTotalIrpf="", valorFinalIrpf="";
	
	//el idPAgo
	String idPago = (String)request.getAttribute("idPago");
	String idLetrado = (String)request.getAttribute("idLetrado");
	idInstitucion = (String)usr.getLocation();
	
	//Altura para las tablas:
	long alturaFila=26;
	if (!estadoMayorQueEjecutado)
		alturaFila=38;
	long alturaSinFilas=21;
	long alturaTabla = alturaSinFilas;
%>	
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="DatosDetallePagoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<script>
		function refrescarLocal(){
			document.forms[0].target="_self";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}
		
		function accionCerrar(){
			top.cierraConParametros("NORMAL");
		}
		
		function validarAnchoTablas(){
			validarAncho_tablaDatos1();
			validarAncho_tablaDatos2();
			validarAncho_tablaDatos3();
			validarAncho_tablaDatos4();
			validarAncho_tablaDatos5();
			validarAncho_tablaDatos6();
			validarAncho_tablaDatos7();
			//Si el estado es cerrado visualizamos las retenciones:
			<% if (estadoMayorQueEjecutado) { %>
			validarAncho_tablaDatos8();
			<% } %>
		}
	</script>
</head>

<body onload="validarAnchoTablas()">
		<!-- TITULO -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="factSJCS.datosPagos.titulo2"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreColegiado)%>
					&nbsp;
					<siga:Idioma key="factSJCS.datosFacturacion.nColegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroColegiado)%>
				</td>
			</tr>
		</table>

	
		<table  class="tablaCentralCampos"  align="center" width="960px">
	
		<html:form action="/FCS_DetallePago.do" method="POST" target="submitArea">
		
		<html:hidden property="modo" value=""/>
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property="idPago" value="<%=idPago%>"/>
		<html:hidden property="idPersona" value="<%=idLetrado%>"/>
		<html:hidden property="nombreColegiado" value="<%=nombreColegiado%>"/>
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modoOriginal" value="<%=modoOriginal%>"/>
		

<!-- FILA TURNOS DE OFICIO -->		
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloTurnos"/>
				</p>
			</td>
		</tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (turnosOficio!=null && turnosOficio.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<turnosOficio.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos1"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.detalleFacturacion.literal.turno,factSJCS.detalleFacturacion.literal.referencia,factSJCS.detalleFacturacion.literal.asunto,factSJCS.detalleFacturacion.literal.fecha,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="20,15,20,10,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	if (turnosOficio==null || turnosOficio.size()==0) { %>
			<td colspan="8">
		   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 	</td>
<%
	} else { %>
<%				for (int cont=1;cont<=turnosOficio.size();cont++) {
					Hashtable fila = (Hashtable) turnosOficio.get(cont-1);

					turno = UtilidadesString.mostrarDatoJSP(fila.get(ScsTurnoBean.C_NOMBRE));
					idTurno = (String)fila.get(ScsTurnoBean.C_IDTURNO);
					idInstitucion = (String)fila.get(ScsTurnoBean.C_IDINSTITUCION);
					asunto = UtilidadesString.mostrarDatoJSP(fila.get(ScsDesignaBean.C_RESUMENASUNTO));
					fecha = UtilidadesString.mostrarDatoJSP(fila.get(ScsActuacionDesignaBean.C_FECHA));
					idPago = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionDesignaBean.C_IDPAGOSJG));
					anio = UtilidadesString.mostrarDatoJSP(fila.get(ScsActuacionDesignaBean.C_ANIO));
					numero = UtilidadesString.mostrarDatoJSP(fila.get(ScsActuacionDesignaBean.C_NUMERO));
					numeroAsunto = UtilidadesString.mostrarDatoJSP(fila.get(ScsActuacionDesignaBean.C_NUMEROASUNTO));
					anioNumero =  anio+"/"+numero;
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO));
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionDesignaBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionDesignaBean.C_PORCENTAJEIRPF));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
%>
			<siga:FilaConIconosExtExt elementos='<%=elems%>' nombreTablaPadre="tablaDatos1" fila="<%=String.valueOf(cont)%>" pintarEspacio='no' botones="" clase="listaNonEdit" visibleConsulta="false" visibleBorrado="false" visibleEdicion="false">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos1" value="TURNO">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos1" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos1" value="<%=idPago%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos1" value="<%=idTurno%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos1" value="<%=anio%>">
					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos1" value="<%=numero%>">
					<input type="hidden" name="oculto<%=cont %>_7_tablaDatos1" value="<%=numeroAsunto%>">
					<input type="hidden" name="oculto<%=cont %>_8_tablaDatos1" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_9_tablaDatos1" value="<%=importe%>">
					<input type="hidden" name="oculto<%=cont %>_10_tablaDatos1" value="<%=porcentajeIRPF%>">
					<%=turno%>
				</td>
				<td><%=anioNumero%></td>
				<td><%=asunto%></td>
				<td><%=GstDate.getFormatedDateShort("",fecha)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>

<%				} // del for 
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
			</siga:TablaMultipleModal>
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA TURNOS -->
		
<!-- FILA GUARDIAS PRESENCIALES -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloGuardias"/>
				</p>
			</td>
		</tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (guardiasPresenciales!=null && guardiasPresenciales.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<guardiasPresenciales.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos2"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.detalleFacturacion.literal.turno,factSJCS.detalleFacturacion.literal.guardia,factSJCS.detalleFacturacion.literal.diasCobrar,factSJCS.detalleFacturacion.literal.fechaInicio,factSJCS.detalleFacturacion.literal.fechaFin,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="17,17,11,10,10,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	total=0;
	totalIrpf=0;
	if (guardiasPresenciales==null || guardiasPresenciales.size()==0) { %>			
	<td colspan="9">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=guardiasPresenciales.size();cont++) {
					Hashtable fila = (Hashtable) guardiasPresenciales.get(cont-1);
		
					turno = UtilidadesString.mostrarDatoJSP(fila.get(ScsTurnoBean.C_NOMBRE));
					guardia = UtilidadesString.mostrarDatoJSP(fila.get("GUARDIAS"));
					idTurno = (String)fila.get(ScsGuardiasTurnoBean.C_IDTURNO);
					idGuardia = (String)fila.get(ScsGuardiasTurnoBean.C_IDGUARDIA);
					idPago = (String)fila.get(FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG);
					
					idCalendario = (String)fila.get(FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);
					idPersona = (String)fila.get(FcsPagoGuardiasColegiadoBean.C_IDPERSONA);
					
					idInstitucion = (String)fila.get(ScsGuardiasTurnoBean.C_IDINSTITUCION);
					fechaInicio = UtilidadesString.mostrarDatoJSP(fila.get(ScsGuardiasColegiadoBean.C_FECHAINICIO));
					fechaFin = UtilidadesString.mostrarDatoJSP(fila.get(ScsGuardiasColegiadoBean.C_FECHAFIN));
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoGuardiasColegiadoBean.C_PORCENTAJEIRPF));
					diasACobrar = UtilidadesString.mostrarDatoJSP(fila.get(ScsGuardiasColegiadoBean.C_DIASACOBRAR));
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
%>
			<siga:FilaConIconosExtExt elementos='<%=elems%>' nombreTablaPadre="tablaDatos2" pintarEspacio='no' fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos2" value="GUARDIA">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos2" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos2" value="<%=idPago%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos2" value="<%=idTurno%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos2" value="<%=idGuardia%>">
					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos2" value="<%=idCalendario%>">
					<input type="hidden" name="oculto<%=cont %>_7_tablaDatos2" value="<%=idPersona%>">
					<input type="hidden" name="oculto<%=cont %>_8_tablaDatos2" value="<%=fechaInicio%>">
					<input type="hidden" name="oculto<%=cont %>_9_tablaDatos2" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_10_tablaDatos2" value="<%=importe%>">
					<input type="hidden" name="oculto<%=cont %>_11_tablaDatos2" value="<%=porcentajeIRPF%>">
					<%=turno%>
				</td>
				<td><%=guardia%></td>
				<td><%=diasACobrar%></td>				
				<td><%=GstDate.getFormatedDateShort("",fechaInicio)%></td>
				<td><%=GstDate.getFormatedDateShort("",fechaFin)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>

<%				} // del for %>
<% } // del if 
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
			</siga:TablaMultipleModal>
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>

<!-- FIN FILA DE GUARDIAS -->

<!-- FILA ASISTENCIAS -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloAsistencias"/>
				</p>
			</td>
		</tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (asistencias!=null && asistencias.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<asistencias.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos4"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.detalleFacturacion.literal.turno,factSJCS.detalleFacturacion.literal.guardia,factSJCS.detalleFacturacion.literal.asistencia,factSJCS.detalleFacturacion.literal.fecha,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="20,20,15,10,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	total=0;
	totalIrpf=0;
	if (asistencias==null || asistencias.size()==0) { %>			
	<td colspan="8">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=asistencias.size();cont++) {
					Hashtable fila = (Hashtable) asistencias.get(cont-1);
		
					turno = UtilidadesString.mostrarDatoJSP(fila.get(ScsTurnoBean.C_NOMBRE));
					guardia = UtilidadesString.mostrarDatoJSP(fila.get("ASISTENCIAS"));
					asistencia = UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_ANIO))+ "/" +UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_NUMERO));
					idInstitucion = (String)fila.get(ScsAsistenciasBean.C_IDINSTITUCION);
					anio = (String)fila.get(ScsAsistenciasBean.C_ANIO);
					idPago = (String)fila.get(FcsPagoAsistenciaBean.C_IDPAGOSJG);
					numero = (String)fila.get(ScsAsistenciasBean.C_NUMERO);
					fecha = UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_FECHAHORA));
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoAsistenciaBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoAsistenciaBean.C_PORCENTAJEIRPF));
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoAsistenciaBean.C_IMPORTEPAGADO));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
%>
			<siga:FilaConIconosExtExt pintarEspacio='no' nombreTablaPadre="tablaDatos4" elementos='<%=elems%>' fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos4" value="ASISTENCIA">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos4" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos4" value="<%=idPago%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos4" value="<%=anio%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos4" value="<%=numero%>">
					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos4" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_7_tablaDatos4" value="<%=importe%>">
					<input type="hidden" name="oculto<%=cont %>_8_tablaDatos4" value="<%=porcentajeIRPF%>">
					<%=turno%>
				</td>
				<td><%=fila.get("GUARDIAS")%></td>
				<td><%=asistencia%></td>
				<td><%=GstDate.getFormatedDateShort("",fecha)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>
			
<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
				</siga:TablaMultipleModal>
				
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA ASISTENCIAS -->

<!-- FILA DE ACTUACIONES -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloActuaciones"/>
				</p>
			</td>
		</tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (actuaciones!=null && actuaciones.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<actuaciones.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos3"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.detalleFacturacion.literal.turno,factSJCS.detalleFacturacion.literal.guardia,factSJCS.detalleFacturacion.literal.asistencia,factSJCS.detalleFacturacion.literal.descripcion,factSJCS.detalleFacturacion.literal.fecha,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="15,15,15,10,10,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	total=0;
	totalIrpf=0;
	if (actuaciones==null || actuaciones.size()==0) { %>			
	<td colspan="9">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=actuaciones.size();cont++) {
					Hashtable fila = (Hashtable) actuaciones.get(cont-1);
		
					turno = UtilidadesString.mostrarDatoJSP(fila.get(ScsTurnoBean.C_NOMBRE));
					guardia = UtilidadesString.mostrarDatoJSP(fila.get("ACTUACIONES"));
					asistencia = UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_ANIO))+ "/" +UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_NUMERO));
					idInstitucion = (String)fila.get(ScsAsistenciasBean.C_IDINSTITUCION);
					anio = (String)fila.get(ScsAsistenciasBean.C_ANIO);
					numero = (String)fila.get(ScsAsistenciasBean.C_NUMERO);
					idPago = (String)fila.get(FcsPagoActuacionDesignaBean.C_IDPAGOSJG);
					descripcion = UtilidadesString.mostrarDatoJSP(fila.get(ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE));
					fecha = UtilidadesString.mostrarDatoJSP(fila.get(ScsAsistenciasBean.C_FECHAHORA));
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionAsistBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionAsistBean.C_PORCENTAJEIRPF));
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionAsistBean.C_IMPORTEPAGADO));
					idActuacion = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoActuacionAsistBean.C_IDACTUACION));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
%>
			<siga:FilaConIconosExtExt pintarEspacio='no' nombreTablaPadre="tablaDatos3" elementos='<%=elems%>' fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="false" visibleBorrado="false" visibleEdicion="false">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos3" value="ACTUACION">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos3" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos3" value="<%=idPago%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos3" value="<%=anio%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos3" value="<%=numero%>">
					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos3" value="<%=idActuacion%>">
					<input type="hidden" name="oculto<%=cont %>_7_tablaDatos3" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_8_tablaDatos3" value="<%=importe%>">
					<input type="hidden" name="oculto<%=cont %>_9_tablaDatos3" value="<%=porcentajeIRPF%>">
					<%=turno%>
				</td>
				<td><%=guardia%></td>
				<td><%=asistencia%></td>
				<td><%=descripcion%></td>
				<td><%=GstDate.getFormatedDateShort("",fecha)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>

<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
				</siga:TablaMultipleModal>
				
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA ACTUACIONES -->
		
<!-- FILA SOJ -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloSOJ"/>
				</p>
			</td>
		</tr>
		<tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (expSoj!=null && expSoj.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<expSoj.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos5"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.detalleFacturacion.literal.tipoExpediente,factSJCS.detalleFacturacion.literal.nExpediente,factSJCS.detalleFacturacion.literal.fecha,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
  				   tamanoCol="25,20,20,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	total=0;
	totalIrpf=0;
	if (expSoj==null || expSoj.size()==0) { %>			
	<td colspan="7">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=expSoj.size();cont++) {
					Hashtable fila = (Hashtable) expSoj.get(cont-1);
		
					tipo = UtilidadesString.mostrarDatoJSP(fila.get(ScsTipoSOJ.C_DESCRIPCION));
					nExpediente = UtilidadesString.mostrarDatoJSP(fila.get(ScsSOJBean.C_ANIO))+"/"+UtilidadesString.mostrarDatoJSP(fila.get(ScsSOJBean.C_NUMERO));
					idInstitucion = (String)fila.get(ScsSOJBean.C_IDINSTITUCION);
					idTipoSoj = (String)fila.get(ScsSOJBean.C_IDTIPOSOJ);
					anio = (String)fila.get(ScsSOJBean.C_ANIO);
					idPago = (String)fila.get(FcsPagoSojBean.C_IDPAGOSJG);
					numero = (String)fila.get(ScsSOJBean.C_NUMERO);
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoSojBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoSojBean.C_PORCENTAJEIRPF));
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoSojBean.C_IMPORTEPAGADO));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
					fecha = UtilidadesString.mostrarDatoJSP(fila.get(ScsSOJBean.C_FECHAAPERTURA));
%>
			<siga:FilaConIconosExtExt pintarEspacio='no' elementos='<%=elems%>' nombreTablaPadre="tablaDatos5" fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
				<td ><input type="hidden" name="oculto<%=cont %>_1_tablaDatos5" value="SOJ">
 					 <input type="hidden" name="oculto<%=cont %>_2_tablaDatos5" value="<%=idInstitucion%>">
 					 <input type="hidden" name="oculto<%=cont %>_3_tablaDatos5" value="<%=idPago%>">
					 <input type="hidden" name="oculto<%=cont %>_4_tablaDatos5" value="<%=idTipoSoj%>">
					 <input type="hidden" name="oculto<%=cont %>_5_tablaDatos5" value="<%=anio%>">
					 <input type="hidden" name="oculto<%=cont %>_6_tablaDatos5" value="<%=numero%>">
					 <input type="hidden" name="oculto<%=cont %>_7_tablaDatos5" value="<%=importeIrpf%>">
					 <input type="hidden" name="oculto<%=cont %>_8_tablaDatos5" value="<%=importe%>">
 					 <input type="hidden" name="oculto<%=cont %>_9_tablaDatos5" value="<%=porcentajeIRPF%>">
					<%=tipo%>
				</td>
				<td><%=nExpediente%></td>
				<td><%=GstDate.getFormatedDateShort("",fecha)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>
			
<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
				</siga:TablaMultipleModal>
				
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA SOJ -->		

<!-- FILA EJG -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.detalleFacturacion.literal.tituloEJG"/>
				</p>
			</td>
		</tr>
		<tr>

		<tr>
		
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (expEjg!=null && expEjg.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<expEjg.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
			<siga:TablaMultipleModal
	  			   nombre="tablaDatos6"
	  			   borde="1"
	  			   estilo=""
			   	   clase="tableTitle"
	  			   nombreCol="factSJCS.detalleFacturacion.literal.tipoExpediente,factSJCS.detalleFacturacion.literal.nExpediente,factSJCS.detalleFacturacion.literal.fecha,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
  				   tamanoCol="25,20,20,10,10,10,5"
				   alto = "<%=String.valueOf(alturaTabla)%>"
				   variasTablasEnLaMismaPagina="true"
				   modal="P"
		 	>
<%	total=0;
	totalIrpf=0;
	if (expEjg==null || expEjg.size()==0) { %>
	<td colspan="7">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=expEjg.size();cont++) {
					Hashtable fila = (Hashtable) expEjg.get(cont-1);
		
					tipo = UtilidadesString.mostrarDatoJSP(fila.get(ScsTipoEJGBean.C_DESCRIPCION));
					nExpediente = UtilidadesString.mostrarDatoJSP(fila.get(ScsEJGBean.C_ANIO))+"/"+UtilidadesString.mostrarDatoJSP(fila.get(ScsEJGBean.C_NUMERO));
					idInstitucion = (String)fila.get(ScsEJGBean.C_IDINSTITUCION);
					idTipoEjg = (String)fila.get(ScsEJGBean.C_IDTIPOEJG);
					anio = (String)fila.get(ScsEJGBean.C_ANIO);
					idPago = (String)fila.get(FcsPagoEjgBean.C_IDPAGOSJG);
					numero = (String)fila.get(ScsEJGBean.C_NUMERO);
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoEjgBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoEjgBean.C_PORCENTAJEIRPF));
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsPagoEjgBean.C_IMPORTEPAGADO));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
					fecha = UtilidadesString.mostrarDatoJSP(fila.get(ScsEJGBean.C_FECHAAPERTURA));
%>
			<siga:FilaConIconosExtExt fila='<%=String.valueOf(cont)%>' botones='' visibleConsulta='false' visibleBorrado='false' visibleEdicion='false' pintarEspacio='no' clase='listaNonEdit' nombreTablaPadre='tablaDatos6' elementos='<%=elems%>'>
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos6" value="EJG">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos6" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos6" value="<%=idPago%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos6" value="<%=idTipoEjg%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos6" value="<%=anio%>">
					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos6" value="<%=numero%>">
					<input type="hidden" name="oculto<%=cont %>_7_tablaDatos6" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_8_tablaDatos6" value="<%=importe%>">
 					<input type="hidden" name="oculto<%=cont %>_9_tablaDatos6" value="<%=porcentajeIRPF%>">
					<%=tipo%>
				</td>
				<td><%=nExpediente%></td>
				<td><%=GstDate.getFormatedDateShort("",fecha)%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>

<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
			</siga:TablaMultipleModal>
				
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA EJG -->

<!-- FILA MOVIMIENTOS VARIOS -->
		<tr>
			<td colspan="2">
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="factSJCS.datosMovimientos.titulo"/>
				</p>
			</td>
		</tr>
		<tr>
		<tr>
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (movimientos!=null && movimientos.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<movimientos.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal
				   nombre="tablaDatos7"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="factSJCS.datosMovimientos.literal.descripcion,factSJCS.datosMovimientos.literal.motivo,factSJCS.detalleFacturacion.literal.porcentajeIRPF,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="30,35,10,10,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				   variasTablasEnLaMismaPagina="true"
				>
<%	total=0;
	totalIrpf=0;
	if (movimientos==null || movimientos.size()==0) { %>
	<td colspan="6">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=movimientos.size();cont++) {
					Hashtable fila = (Hashtable) movimientos.get(cont-1);
		
					motivo = UtilidadesString.mostrarDatoJSP(fila.get(FcsMovimientosVariosBean.C_MOTIVO));
					descripcion = UtilidadesString.mostrarDatoJSP(fila.get(FcsMovimientosVariosBean.C_DESCRIPCION));
					idInstitucion = (String)fila.get(FcsMovimientosVariosBean.C_IDINSTITUCION);
					idMovimiento = (String)fila.get(FcsMovimientosVariosBean.C_IDMOVIMIENTO);
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsMovimientosVariosBean.C_CANTIDAD));
					importeIrpf = UtilidadesString.mostrarDatoJSP(fila.get(FcsMovimientosVariosBean.C_IMPORTEIRPF));
					porcentajeIRPF = UtilidadesString.mostrarDatoJSP(fila.get(FcsMovimientosVariosBean.C_PORCENTAJEIRPF));
					total += Double.parseDouble(importe);
					totalIrpf += Double.parseDouble(importeIrpf);
%>
			<siga:FilaConIconosExtExt pintarEspacio='no' elementos='<%=elems%>' nombreTablaPadre="tablaDatos7" fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos7" value="MOVIMIENTO">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos7" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos7" value="<%=idMovimiento%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos7" value="<%=importeIrpf%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos7" value="<%=importe%>">
 					<input type="hidden" name="oculto<%=cont %>_6_tablaDatos7" value="<%=porcentajeIRPF%>">
				 	<%=descripcion%>
				</td>
				<td><%=motivo%></td>
				<td>
				<% if (porcentajeIRPF.equals("-1")) { %>
					<siga:Idioma key="messages.factSJCS.error.IRPF"/>
				<% } else { %>
					<%=porcentajeIRPF%>&nbsp;&#37;
				<% } %>
				</td>
				<td><%=importeIrpf%>&nbsp;&euro;</td>
				<td><%=importe%> &euro;</td>
			</siga:FilaConIconosExtExt>
			
<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
	valorFinalIrpf = String.valueOf(UtilidadesNumero.redondea(totalIrpf,2));
	totalFinalIrpf += totalIrpf;
%>
				</siga:TablaMultipleModal>
				
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="670px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						<siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/> &nbsp;&nbsp;= &nbsp;<%=valorFinalIrpf%>&nbsp;&euro;
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA MOVIMIENTOS VARIOS -->

<!-- FILA RETENCIONES JUDICIALES -->
		<tr>
			<td colspan="2">
			
<%	//Si el estado es mayor que Ejecutado visualizamos las Retenciones:
	if (estadoMayorQueEjecutado){ %>
				<p class="titulitos" style="text-align:left" >
					<siga:Idioma key="menu.facturacionSJCS.retencionesJudiciales"/>
				</p>
			</td>
		</tr>
		<tr>

		<tr>
		
			<td colspan="2">
<%  //Calculo la altura de la tabla segun el numero de filas que tenga y hasta un maximo de 10:
	if (retenciones!=null && retenciones.size() > 0) {
		alturaTabla = 0;
		for (int i=0; i<retenciones.size() && i<10; i++) {
			alturaTabla += alturaFila;
		}
	} else
		alturaTabla = alturaSinFilas;
%>
				<siga:TablaMultipleModal 
				   nombre="tablaDatos8"
				   variasTablasEnLaMismaPagina="true"
				   borde="1"
				   clase="tableTitle"
				   nombreCol="FactSJCS.listadoRetencionesJ.literal.destinatario,FactSJCS.mantRetencionesJ.literal.tipoRetencion,factSJCS.detalleFacturacion.literal.importe,"
				   tamanoCol="65,20,10,5"
				   alto="<%=String.valueOf(alturaTabla)%>"
				   modal="P"
				  >
<%	total=0;
	totalIrpf=0;
	if (retenciones==null || retenciones.size()==0) { %>
	<td colspan="5">
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	</td>
<%	
	} else { 
				for (int cont=1;cont<=retenciones.size();cont++) {
					Hashtable fila = (Hashtable) retenciones.get(cont-1);
		
					nombre = UtilidadesString.mostrarDatoJSP(fila.get(FcsDestinatariosRetencionesBean.C_NOMBRE));
					descripcion = UtilidadesString.mostrarDatoJSP(fila.get(FcsRetencionesJudicialesBean.C_TIPORETENCION));
					if (descripcion.equalsIgnoreCase(ClsConstants.TIPO_RETENCION_LEC))descripcion="LEC";
					else descripcion = "OTRAS";
					idInstitucion = (String)fila.get(FcsRetencionesJudicialesBean.C_IDINSTITUCION);
					idPersona = (String)fila.get(FcsRetencionesJudicialesBean.C_IDPERSONA);
					idRetencion = (String)fila.get(FcsRetencionesJudicialesBean.C_IDRETENCION);
					importe = UtilidadesString.mostrarDatoJSP(fila.get(FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO));
					total += Double.parseDouble(importe);
%>
			<siga:FilaConIconosExtExt pintarEspacio='no' elementos='<%=elems%>' nombreTablaPadre="tablaDatos8" fila="<%=String.valueOf(cont)%>" botones="" clase="listaNonEdit" visibleConsulta="no" visibleBorrado="no" visibleEdicion="no">
				<td><input type="hidden" name="oculto<%=cont %>_1_tablaDatos8" value="RETENCION">
					<input type="hidden" name="oculto<%=cont %>_2_tablaDatos8" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3_tablaDatos8" value="<%=idPersona%>">
					<input type="hidden" name="oculto<%=cont %>_4_tablaDatos8" value="<%=idRetencion%>">
					<input type="hidden" name="oculto<%=cont %>_5_tablaDatos8" value="<%=importe%>">
					<%=nombre%>
				</td>
				<td><%=descripcion%></td>
				<td><%=importe%> &euro;</td>
		</siga:FilaConIconosExtExt>
			
<%				} // del for
	} // del if
	valorFinal = String.valueOf(UtilidadesNumero.redondea(total,2));
	totalFinal += total;
%>
				</siga:TablaMultipleModal>
			</td>
		</tr>
		<tr height="<%=String.valueOf(alturaTabla)%>px">
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellspacing="0" cellpadding="0">
				<tr>
					<td width="755px" class="labelText" align="right">
						&nbsp;
					</td>
					<td class="labelText">
						&nbsp;&nbsp;
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/> &nbsp;&nbsp;= &nbsp;<%=valorFinal%> &euro;
					</td>
				</tr>
				</table>
			</td>
		</tr>
<!-- FIN FILA RETENCIONES JUDICIALES -->

<%}
	valorFinalTotal = String.valueOf(UtilidadesNumero.redondea(totalFinal,2));
	valorFinalTotalIrpf = String.valueOf(UtilidadesNumero.redondea(totalFinalIrpf,2));
%>

<!-- FILA TOTALES -->
		<tr>
			<td>
			<table border="1" align="center" width="800px">
			<tr>
				<td class="listaEdit">
					<p class="titulitos" style="text-align:center" ><siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/>&nbsp;=&nbsp;<%=valorFinalTotalIrpf%>&nbsp;&euro;</p>
				</td>
				<td class="listaEdit">
					<p class="titulitos" style="text-align:center" ><siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;=&nbsp;<%=valorFinalTotal%>&nbsp;&euro;</p>
				</td>
			</tr>
			</table>
			</td>
		</tr>
<!-- FIN FILA TOTALES -->

		</table>
	</html:form>
	
	<siga:ConjBotonesAccion botones="C"/>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>