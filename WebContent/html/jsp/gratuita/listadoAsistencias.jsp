<!DOCTYPE html>
<html>
<head>
<!-- listadoAsistencias.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	//Vector obj = (Vector) request.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String idioma=usr.getLanguage().toUpperCase();
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	 if (ses.getAttribute("DATAPAGINADOR")!=null){
		 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
		
		
		 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
		  resultado = (Vector)hm.get("datos");
		  
		    PaginadorBind paginador = (PaginadorBind)hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
		
		 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
		
		 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
		  
		
		 	
		 }else{
		  resultado =new Vector();
		  paginaSeleccionada = "0";
		
		 	totalRegistros = "0";
		
		 	registrosPorPagina = "0";
		 }
	}else{
	      resultado =new Vector();
		  paginaSeleccionada = "0";
		
		 	totalRegistros = "0";
		
		 	registrosPorPagina = "0";
	}	 
	String action=app+"/JGR_Asistencia.do?noReset=true";
    /**************/
	
	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null) && (sEsFichaColegial.equalsIgnoreCase("1"))) {
		esFichaColegial = true;
	}
	
	//Modo de las pestanhas:
	String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null || busquedaVolver.equals("") ) {
		busquedaVolver = "volverNo";
	}
%>
	
	<title><!--  <"listarAsistencias.title">--></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script language="JavaScript">
	//Funcion asociada a boton nuevo
		function anticiparImporte(fila, id) 
		{	
			document.movimientosVarios.modo.value="nuevo";
			document.movimientosVarios.target="submitArea";
			
			document.movimientosVarios.idInstitucion.value=jQuery("#oculto"+fila+"_3").val(); 
			document.movimientosVarios.numero.value = jQuery("#oculto"+fila+"_2").val();
			document.movimientosVarios.anio.value = jQuery("#oculto"+fila+"_1").val();
			var resultado=ventaModalGeneral(document.movimientosVarios.name,"M");
			//if (resultado=="MODIFICADO")buscar2();
		}
	 
	</script>


</head>

<body class="tablaCentralCampos" >

<%
	//Entrada desde el menu de Censo:
	if (esFichaColegial) { 
		Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");		
		String nombrePestanha = "";
		String numeroPestanha = "";
		if (datosColegiado != null) {
			nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
			numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
		}
%>
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.asistencias.pestana.titulito"/>&nbsp;&nbsp;
					<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
		</table>
<% 
	} 

	String nC="";
	String tC="";
	String botones="C,E,B";
	String alto="243";
  	nC="gratuita.listadoAsistencias.literal.turno,gratuita.listadoAsistencias.literal.guardia,gratuita.listadoAsistencias.literal.anio,gratuita.listadoAsistencias.literal.numero,gratuita.busquedaAsistencias.literal.fechaAsistencia,gratuita.mantAsistencias.literal.letrado,gratuita.busquedaAsistencias.literal.asistido,gratuita.mantAsistencias.literal.estado,gratuita.busquedaAsistencias.literal.validada,";
	tC="13,14,4,6,7,17,16,8,4,11";

	String target = "mainWorkArea";
	if (esFichaColegial) { 
		target = "";
	} 
	
	String ajuste=(!busquedaVolver.equals("volverNo"))?"true":"false";
%>

	<html:form action="/JGR_Asistencia.do" method="post" target="<%=target%>"  style="display:none">
		<input type="hidden" name="modo"  id="modo" />
		<input type="hidden" name="esFichaColegial"  id="esFichaColegial"  value="<%=sEsFichaColegial%>" />
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
	</html:form>
	
	 <html:form action="/JGR_MovimientosVariosLetrado?noReset=true" method="POST" target="submitArea" styleId="movimientosVarios">
			<html:hidden name="MantenimientoMovimientosForm" property = "modo" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property = "actionModal" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property="checkHistorico" value=""/>
			<html:hidden name="MantenimientoMovimientosForm" property="idPersona" value=""/>
			<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<input type="hidden" name="botonBuscarPulsado" value="">
			<input type="hidden" name="mostrarMovimientos" value="">
			
			<html:hidden property = "idInstitucion" />
			<html:hidden property = "anio" />	
			<html:hidden property = "numero"  />
			<html:hidden property = "origen" value="ASISTENCIAS"  />
			
		</html:form>	
		
	<!-- campos a pasar -->
	<siga:Table 
	   name="listarAsistencias"
	   border="2"
	   columnNames="<%=nC%>"
	   columnSizes="<%=tC%>">
		   
<%
		if (resultado.size()>0) {
			String fecha = "";
			String nTurno = "";
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	ScsAsistenciasAdm scsAsistenciasAdm = new ScsAsistenciasAdm(usr);
			while (recordNumber-1 < resultado.size()) {	 
				Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();
				
				String idFacturacion = (String)registro.get("IDFACTURACION");
				
				if (usr.isLetrado()) {
					botones = "E";
				} else { //Como administrador
					botones = "C,E,B";
				}
				
				nTurno = ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),(String)registro.get("IDTURNO"));	
%>

			<%
			 	FilaExtElement[] elems = null;
			 	elems = new FilaExtElement[1];
			 	if(idFacturacion != null && !"".equalsIgnoreCase(idFacturacion)){
			 		elems[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
			 	}
				 	
			 %>

				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" elementos="<%=elems%>">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' id='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=registro.get("ANIO")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' id='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=registro.get("NUMERO")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=registro.get("IDINSTITUCION")%>'> 
						<%=nTurno%>
					</td>
					<td><%=ScsGuardiasTurnoAdm.getNombreGuardiaJSP(usr.getLocation(),(String)registro.get("IDTURNO"),(String)registro.get("IDGUARDIA")) %></td>
					<td><%=registro.get("ANIO")%></td>
					<td><%=registro.get("NUMERO")%></td>
					<%
						// Formateamos la fecha
						fecha = GstDate.getFormatedDateShort(usr.getLanguage(),registro.get("FECHAHORA"));
					%>
					<td><%=fecha%></td>
					<td><%=CenPersonaAdm.obtenerNombreApellidosJSP((String)registro.get("IDPERSONA")) %>&nbsp;</td>
					<td><%=registro.get("NOMBRE")%>&nbsp;</td>
					<td>
						<%ArrayList estadoSel    = new ArrayList();	
						estadoSel.add(registro.get("ESTADO"));%>
						<siga:ComboBD nombre="estado" tipo="cmbEstadosAsistencia" obligatorio="false" accion="" elementoSel="<%=estadoSel%>" clase="boxComboEnTabla" ReadOnly="true" obligatorioSinTextoSeleccionar="no"/>&nbsp;
					</td>
					<td><%=ScsAsistenciasAdm.obtenerActuacionesPendientesValidarJSP(usr.getLocation(),(String)registro.get("ANIO"),(String)registro.get("NUMERO")) %> &nbsp;</td>
				</siga:FilaConIconos>
<% 
				recordNumber++;
			} 
		} else { 
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="gratuita.retenciones.noResultados"/></td>
			</tr>
<%
		}
%>
	</siga:Table>
	
<%
	if ( hm.get("datos")!=null && !hm.get("datos").equals("")) {
%>	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
			registrosPorPagina="<%=registrosPorPagina%>" 
			paginaSeleccionada="<%=paginaSeleccionada%>" 
			idioma="<%=idioma%>"
			modo="buscarPor"								
			clase="paginator" 
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
			distanciaPaginas=""
			action="<%=action%>" />															
<%
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
%>
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
<% 
	} 
%>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>	
</body>
</html>