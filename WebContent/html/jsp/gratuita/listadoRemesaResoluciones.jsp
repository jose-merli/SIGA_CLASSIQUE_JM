<!-- listadoRemesaResoluciones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="java.lang.Integer"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsMaestroEstadosEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="com.siga.gratuita.action.DefinirRemesasCAJGAction"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>

<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idioma=usr.getLanguage().toUpperCase();
	
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	
	
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
	    PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)hm.get("paginador");
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
		
	String idTipoRemesa = request.getParameter("idTipoRemesa");
	String action=app+"/JGR_E-Comunicaciones_RemesaResolucion.do?noReset=true&idTipoRemesa="+idTipoRemesa;
	
	String mensajeUsuario = (String)request.getAttribute("mensajeUsuario");
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title></title>
	<script type="text/javascript">
		
		var descarga = false;
		
		<%String idRemesaResolucion = null;
		
		  if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
				Hashtable datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
				idRemesaResolucion = (String)datosFormulario.get("IDREMESARESOLUCION");
		  }
		%>
		
		function inicio() {
			<%if (idRemesaResolucion != null && !idRemesaResolucion.trim().equals("")){%>
				descargaFichero(<%=idRemesaResolucion%>)
			<%}%>
		}
				
		function refrescarLocal() {		
			parent.buscarPaginado(<%=paginaSeleccionada%>);			
		}
		
		function descargar(fila){						
	    	descargarFichero(fila, "descargar");
		}
		
		function descargarLog(fila){						
	    	descargarFichero(fila, "descargarLog");
		}
		
		function descargarFichero(fila, tipo) {
			var idRemesaReso = document.getElementById('oculto' + fila + '_1');
	    	document.DefinicionRemesaResolucionesCAJGForm.idRemesaResolucion.value=idRemesaReso.value	    	
			document.DefinicionRemesaResolucionesCAJGForm.modo.value=tipo;	
		   	document.DefinicionRemesaResolucionesCAJGForm.target="submitArea";
		   	//document.DefinicionRemesaResolucionesCAJGForm.target="mainWorkArea";			   	
			document.DefinicionRemesaResolucionesCAJGForm.submit();
		}
		
		
		
	</script>
</head>

<body onload="inicio()">

	<html:form action="/JGR_E-Comunicaciones_RemesaResolucion.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="idRemesaResolucion"  id="idRemesaResolucion" value="">
		<html:hidden property = "idTipoRemesa"  styleId = "idTipoRemesa"  value = "<%=idTipoRemesa%>"/>
				
	</html:form>	
		
		<siga:Table 		   
		   name="listadoRemesaResoluciones"
		   border="1"
		   columnNames="gratuita.BusquedaResolucionCAJG.literal.numRegistro, gratuita.BusquedaResolucionCAJG.literal.fCarga, gratuita.BusquedaResolucionCAJG.literal.fResolucion, gratuita.BusquedaResolucionCAJG.literal.nombreFichero, gratuita.BusquedaResolucionCAJG.literal.observaciones, "
		   columnSizes="9,5,5,16,16,8"
		   modal="M">

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	String botones = "";
	    	String fRatificacion = "";
			while (recordNumber-1 < resultado.size())
			{			
			  
		    Row fila = (Row)resultado.elementAt(recordNumber-1);
			Hashtable registro = (Hashtable) fila.getRow();
				
			CajgRemesaEstadosAdm admBean =  new CajgRemesaEstadosAdm(usr);
			//int ultimo_estado=admBean.UltimoEstadoRemesa(usr.getLocation(), (String)registro.get("IDREMESA"));
			
	    	
	    	botones="C,E";
	    	FilaExtElement[] elems = new FilaExtElement[2];
	    	
	    	String prefijo = (String)registro.get("PREFIJO");
    		String sufijo = (String)registro.get("SUFIJO");
    		String numRemesa = (prefijo!=null && !prefijo.trim().equals(""))?(prefijo):"";
				numRemesa+=(String)registro.get("NUMERO");
				numRemesa+=(sufijo!=null && !sufijo.trim().equals(""))?(sufijo):"";
			
	    	
	    	elems[0]=new FilaExtElement("download", "descargar", "gratuita.BusquedaResolucionCAJG.literal.FicheroResoluciones", SIGAConstants.ACCESS_FULL);
	    	if (registro.get("LOGGENERADO") != null && registro.get("LOGGENERADO").equals("1") ) {
	    		elems[1]=new FilaExtElement("descargaLog", "descargarLog", "gratuita.BusquedaResolucionCAJG.literal.FicheroLog", SIGAConstants.ACCESS_FULL);
	    	}
	    	
	    	String observaciones = (String)registro.get("OBSERVACIONES");
	    	int limite = 35;
	    	if (observaciones != null && observaciones.length() > limite) {
	    		observaciones = observaciones.substring(0, limite);
	    		observaciones += "...";
	    	}
	    	
	    	String nombreFichero = (String)registro.get("NOMBREFICHERO"); 
	    	if (nombreFichero != null && nombreFichero.length() > limite) {
	    		nombreFichero = nombreFichero.substring(0, limite);
	    		nombreFichero += "...";
	    	}
	    	
			%>
			
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' elementos="<%=elems%>" visibleBorrado="false" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					<td align="center"><%=numRemesa%>&nbsp;</td>
					<td align="center">
					<%=registro.get("FECHACARGA").equals("")?"&nbsp;":GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(registro.get("FECHACARGA")))%></td>
					</td>
					<td align="center">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get("IDREMESARESOLUCION")%>">
					<%=registro.get("FECHARESOLUCION").equals("")?"&nbsp;":GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(registro.get("FECHARESOLUCION")))%></td>	
					<td><%=nombreFichero%></td>
					<td><%=observaciones%>&nbsp;</td>								
					
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
	<%
	}else {
	%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<%
	}
	%>
	</siga:Table>

     <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	
	 
	 
	<%if (mensajeUsuario != null && !mensajeUsuario.trim().equals("")) {%>
		<script language="JavaScript">
			alert('<%=mensajeUsuario%>');
		</script>
	<%}%>
	 
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	