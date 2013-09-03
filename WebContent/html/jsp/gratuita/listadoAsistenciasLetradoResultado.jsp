<!DOCTYPE html>
<html>
<head>
<!-- listadoAsistenciasLetradoResultado.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>

<% 	 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	//Vector obj = (Vector) request.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String idioma=usr.getLanguage().toUpperCase();
	
	String idPersona = (String)request.getParameter("idPersonaPestanha");
	String idInstitucion = (String)request.getParameter("idInstitucionPestanha");
	Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");		
	String nombrePestanha = "";
	String numeroPestanha = "";
	
	if (datosColegiado != null) {
		nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
	}
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	String atributoPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador)!=null){
		hm = (HashMap)ses.getAttribute(atributoPaginador);
	
		if (hm.get("datos")!=null && !hm.get("datos").equals("")) {
			resultado = (Vector)hm.get("datos");
			PaginadorBind paginador = (PaginadorBind)hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());	
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());	
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	
		} else {
			resultado =new Vector();
			paginaSeleccionada = "0";	
			totalRegistros = "0";	
			registrosPorPagina = "0";
		}
		
	} else {
		resultado =new Vector();
		paginaSeleccionada = "0";	
		totalRegistros = "0";	
		registrosPorPagina = "0";
	}	 
	
	String action=app+"/JGR_AsistenciasLetrado.do?noReset=true&nColegiado="+numeroPestanha;
	/**************/
	
	boolean esFichaColegial = true;
	
	//Modo de las pestanhas:
	String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	String botonNuevo = "";
	if (usr.isLetrado()) {						
		botonNuevo = "N";
	} else { //Como administrador						
		if (esFichaColegial && modoPestanha!=null && modoPestanha.equalsIgnoreCase("ver")){
			botonNuevo = "";
		}else{
			botonNuevo = "N";
		}
	}		
%>

	<title><"listarAsistencias.title"></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script type="text/javascript">
		function accionNuevo() {
			document.forms[0].modo.value = "nuevo";
	   		var resultado = ventaModalGeneral(document.forms[0].name,"M");
		   	if(resultado = "MODIFICADO") {
		   		refrescarLocal();
		   	}
		}
		
		function refrescarLocal() {
			parent.refrescarLocal();
		} 

		function borrarSobreescrito(fila) {
		   var datos;
		   if (confirm('<%= UtilidadesString.getMensajeIdioma(usr,"messages.deleteConfirmation")%>')){
		   	datos = document.getElementById('tablaDatosDinamicosD');
		    datos.value = ""; 
		    preparaDatos(fila, 'listarAsistencias', datos);
		   	
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "Borrar";
		   	document.forms[0].submit();
		 	}
		 }
		
		function borrar(fila) {
			borrarSobreescrito(fila);
		}			
	</script>
</head>

<body class="tablaCentralCampos">

	<% 		
		String nC="";
		String tC="";
		String botones="C,E,B";
		String alto="243";
	  	nC="gratuita.listadoAsistencias.literal.turno,gratuita.listadoAsistencias.literal.guardia,gratuita.listadoAsistencias.literal.anio,gratuita.listadoAsistencias.literal.numero,gratuita.busquedaAsistencias.literal.fechaAsistencia,gratuita.busquedaAsistencias.literal.asistido,gratuita.mantAsistencias.literal.estado,gratuita.busquedaAsistencias.literal.validada,";
		tC="20,12,4,6,8,20,6,10,";

		String target = "mainWorkArea";
		if (esFichaColegial) { 
			target = "_parent";					  
		}
		
		String ajuste="true";
	 %>

	<html:form action="/JGR_AsistenciasLetrado.do" method="post" target="<%=target%>" style="display:none">
		<input type="hidden" name="modo" id="modo" />
		<input type="hidden" name="idPersona" id="idPersona" value="<%=idPersona%>" />
		<input type="hidden" name="nColegiado"  id="nColegiado" value="<%=numeroPestanha%>">
		<input type="hidden" name="actionModal"  id="actionModal" value="">
		<input type="hidden" name="esFichaColegial" id="esFichaColegial" value="<%=esFichaColegial%>">
	</html:form>
	<!-- campos a pasar -->

	<siga:Table 
		name="listarAsistencias" 
		border="2"
		columnNames="<%=nC%>" 
		columnSizes="<%=tC%>">
		
<%		
		if (resultado.size()>0){
			String fecha = "";
			String nTurno = "";
	    	int recordNumber=1;
	    	String select = "";
	    	
	    	Vector v = null;
	    	ScsAsistenciasAdm scsAsistenciasAdm = new ScsAsistenciasAdm(usr);
	    	
			while (recordNumber-1 < resultado.size()) {	 
				Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();
				//String esModificable=ScsAsistenciasAdm.esModificableAsistenciaJSP(registro.get("IDFACTURACION").toString());
				String idFacturacion = (String)registro.get("IDFACTURACION");
				if (usr.isLetrado()) {						
					botones = "C,E";
				} else { //Como administrador						
					if (esFichaColegial && modoPestanha!=null && modoPestanha.equalsIgnoreCase("ver")){
						botones = "C";
					} else {
						botones = "C,E,B";
					}
				}

				// Verificamos si el turno permite la modificacion de la asistencia
				if (!UtilidadesString.stringToBoolean((String)registro.get(ScsTurnoBean.C_LETRADOASISTENCIAS))) {
					botones = "C";
				}				
				
				nTurno = ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),(String)registro.get("IDTURNO"));									
%>

				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=registro.get("ANIO")%>'> 
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=registro.get("NUMERO")%>'> 
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
					<td><%=registro.get("NOMBRE")%>&nbsp;</td>
					<td>
<%
						ArrayList estadoSel    = new ArrayList();	
						estadoSel.add(registro.get("ESTADO"));
%> 
						<siga:ComboBD
							nombre="estado" tipo="cmbEstadosAsistencia" obligatorio="false"
							accion="" elementoSel="<%=estadoSel%>" clase="boxComboEnTabla"
							ReadOnly="true" obligatorioSinTextoSeleccionar="no" />&nbsp;
					</td>
					<td><%=ScsAsistenciasAdm.obtenerActuacionesPendientesValidarJSP(usr.getLocation(),(String)registro.get("ANIO"),(String)registro.get("NUMERO")) %>&nbsp;</td>
				</siga:FilaConIconos>
		
<% 
				recordNumber++;
			} // while
		} else {
%>
			<tr class="notFound">
				<td class="titulitos" colspan="10">
						<siga:Idioma key="gratuita.retenciones.noResultados" />
				</td>
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
			paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
			modo="buscarPor" clase="paginator"
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
			distanciaPaginas="" action="<%=action%>" />
<%
	}
 
	if (!busquedaVolver.equals("volverNo")) { 
		if (botonNuevo.equals("")) { 
%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<% 
		} else { 
%>
			<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" />
<% 
		} 
 	} else { 
%>
		<siga:ConjBotonesAccion botones="<%=botonNuevo%>" clase="botonesDetalle" />	
<% 
	} 
%>

	<%@ include file="/html/jsp/censo/includeVolver.jspf"%>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
</body>

	<script type="text/javascript">
		function editar(fila) {
			
			if (typeof id == 'undefined')
				id='listarAsistencias';
			
			preparaDatos(fila, id);
			
			document.forms[0].modo.value = "Editar";
			document.forms[0].submit();
		}
	</script>	 
</html>