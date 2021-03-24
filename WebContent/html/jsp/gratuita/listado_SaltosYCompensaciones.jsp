<!DOCTYPE html>
<html>
<head>
<!-- listadoAsistencias.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultado");
	
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
	String action=app+"/JGR_SaltosYCompensaciones.do?noReset=true";
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

 	<script>
	 	//Refresco del iframe 	
	 	function refrescarLocal() {
			parent.buscar()
		}
 	</script> 	
</head>

<body class="tablaCentralCampos" >

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo"  styleId = "modo" value = ""/>
	</html:form>	
		
	<siga:Table 		   
	   name="listado"
	   border="2"
	   columnNames="gratuita.inicio_SaltosYCompensaciones.literal.turno,gratuita.inicio_SaltosYCompensaciones.literal.guardia,gratuita.inicio_SaltosYCompensaciones.literal.nColegiado,gratuita.inicio_SaltosYCompensaciones.literal.letrado,gratuita.inicio_SaltosYCompensaciones.literal.tipo,gratuita.inicio_SaltosYCompensaciones.literal.fecha,gratuita.inicio_SaltosYCompensaciones.literal.fechaUso,"
	   columnSizes="18,18,7,20,11,8,8,10"
	   modal="P">
		   
<%
		if (resultado.size()>0) {
			//Datos ocultos:
			String idInstitucion="", idTurno="", idSaltosTurno="", idGuardia="", idGrupoGuardia = "";
			String saltoOCompensacion="", fecha="", fechaCumplimiento="", tipoManual="";

			//Datos visibles:
			String nombreTurno="", nombreGuardia="", datosLetrado="", nCol="&nbsp;", idSaltoCompensacionGrupo="";
			
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
			while (recordNumber-1 < resultado.size()) {	 
				Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();
				
				String permisos = "C";
				//if(fechaCumplimiento==null || fechaCumplimiento.trim().equals("") || tipoManual.equals("1")){
					permisos += ",E,B";			
				//}
				if (registro.get("NOMBREGUARDIA")!=null && ((String)registro.get("NOMBREGUARDIA")).equals(""))
					nombreGuardia = " ";
				else
					nombreGuardia = (String)registro.get("NOMBREGUARDIA");
				
				if (registro.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)!=null && (registro.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)).equals(""))
					fechaCumplimiento = " ";
				else
					fechaCumplimiento = (String)registro.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
				
				saltoOCompensacion = registro.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION)==null?"&nbsp;":(String)registro.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
%>

				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=permisos%>" clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=registro.get("IDINSTITUCION")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' id='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=registro.get("IDTURNO")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' id='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=registro.get("IDSALTOSTURNO")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=registro.get("IDGRUPOGUARDIA")%>'> 
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=registro.get("IDSALTOCOMPENSACIONGRUPO")%>'> 
						<%=registro.get("NOMBRETURNO")%>
					</td>
					<td><% if(nombreGuardia.equals(" ")) { %>
							&nbsp;
						<% } else { %>
							<%=nombreGuardia%>
						<% } %>
					</td>
					<td align="center"><%=registro.get("NUMERO")%></td>
					<td><%=registro.get("LETRADO")%></td>
					<td align="center">
						<% if (saltoOCompensacion.equalsIgnoreCase("S")) { %>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.salto"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("C")) {  %>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.compensacion"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("CG")) {  %>
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacionGrupo"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("SG")) {  %>
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.saltoGrupo"/>												
						<% } %>
					</td>
					<%
						// Formateamos la fecha
						fecha = GstDate.getFormatedDateShort(usr.getLanguage(),registro.get("FECHA"));
					%>
					<td align="center"><%=fecha%></td>
					<td>
						<% if(fechaCumplimiento.equals(" ")) { %>
							&nbsp;
						<% } else { %>
							<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaCumplimiento)%>
						<% } %>
					</td>
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
			divStyle="position:absolute; width:100%; height:20; z-index:0; bottom:0px; left: 0px"
			distanciaPaginas=""
			action="<%=action%>" />															
<%
	}
%>

</body>
</html>