<!DOCTYPE html>
<html>
<head>
<!-- listadoRemesas.jsp -->
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
<%@ page import="com.siga.ws.CajgConfiguracion"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gratuita.action.DefinirRemesasCAJGAction"%>

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
		String action=app+"/JGR_E-Comunicaciones_Gestion.do?noReset=true";
    /**************/

    
    Integer idInstitucion = new Integer(usr.getLocation());
	int cajgConfig = CajgConfiguracion.getTipoCAJG(idInstitucion);
	
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title></title>
	<script type="text/javascript">
		
		var descarga = false;
		
		<%String idRemesa = null;
		
		  if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
				Hashtable datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
				idRemesa = (String)datosFormulario.get("IDREMESA");
		  }
		%>
		
		function inicio() {
			<%if (idRemesa != null && !idRemesa.trim().equals("")){%>
				descargaFichero(<%=idRemesa%>)
			<%}%>
		}
		
		function descargaFichero(idRemesa) {
			document.DefinicionRemesas_CAJG_Form.idRemesa.value = idRemesa
			document.DefinicionRemesas_CAJG_Form.modo.value="descargar";	
		   	//document.DefinicionRemesas_CAJG_Form.target="submitArea";
		   	document.DefinicionRemesas_CAJG_Form.target="mainWorkArea";			   	
			document.DefinicionRemesas_CAJG_Form.submit();
		}
		
		
		function refrescarLocal() {
			if (descarga) {
				parent.buscar(document.DefinicionRemesas_CAJG_Form.idRemesa.value);
			} else {
				parent.buscar();
			}
			descarga = false;
		}
		
		function borrarRemesa(fila){
			if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')) { 
				var auxRemesa = 'oculto' + fila + '_1';
		    	var idReme = document.getElementById(auxRemesa);			          		
		    
		    	document.DefinicionRemesas_CAJG_Form.idRemesa.value=idReme.value;
				document.DefinicionRemesas_CAJG_Form.modo.value='borrarRemesa';		   	
		   
		   		document.DefinicionRemesas_CAJG_Form.target="submitArea";
				document.DefinicionRemesas_CAJG_Form.submit();
			}		
		}
		
		function descargar(fila){
			descarga = true;
			var auxRemesa = 'oculto' + fila + '_1';
			var ocultoIdEstado = 'oculto' + fila + '_3';
			
	    	var idReme = document.getElementById(auxRemesa);			          		
	    	var idEstado = document.getElementById(ocultoIdEstado);	    	
	        
	    	document.DefinicionRemesas_CAJG_Form.idRemesa.value=idReme.value;
	    	document.DefinicionRemesas_CAJG_Form.idEstado.value=idEstado.value;
	    	
	    	if (idEstado.value == 2) {// si ya esta marcada como enviada...
	    		descargaFichero(idReme.value)
	    	} else {
		    	document.DefinicionRemesas_CAJG_Form.modo.value="marcarEnviada";
		   		document.DefinicionRemesas_CAJG_Form.target="submitArea";
				document.DefinicionRemesas_CAJG_Form.submit();
	    	}
	    	
			
			
		}
		
	</script>
</head>

<body onload="inicio()">

	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" id="modo" value="">
		<input type="hidden" name="actionModal"  id="actionModal" value="">
		<input type="hidden" name="idRemesa" id="idRemesa" value="">
		<input type="hidden" name="idEstado" id="idEstado" value="">
	</html:form>	
		
		<siga:Table 		   
		   name="listadoEJG"
		   border="1"
		   columnNames="gratuita.BusquedaRemesas_CAJG.literal.nRegistro, gratuita.BusquedaRemesas_CAJG.literal.Descripcion, gratuita.BusquedaRemesas_CAJG.literal.fGeneracion, gratuita.BusquedaRemesas_CAJG.literal.fEnvio, gratuita.BusquedaRemesas_CAJG.literal.fRecepcion,gratuita.BusquedaRemesas_CAJG.literal.estado,gratuita.BusquedaRemesas_CAJG.literal.incidencias,"
		   columnSizes="7,13,4,4,4,3,3,6">

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
			
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado de la remesa
				
			CajgRemesaEstadosAdm admBean =  new CajgRemesaEstadosAdm(usr);
			//int ultimo_estado=admBean.UltimoEstadoRemesa(usr.getLocation(), (String)registro.get("IDREMESA"));
			
	    	
	    	botones="C,E";
	    	FilaExtElement[] elems = new FilaExtElement[2];
	    	
	    	if (Integer.parseInt((String)registro.get("IDESTADO"))<2){
	    		elems[0] =new FilaExtElement("borrar", "borrarRemesa", SIGAConstants.ACCESS_FULL);
	    	}	    		
	    	
	    	String prefijo = (String)registro.get("PREFIJO");
    		String sufijo = (String)registro.get("SUFIJO");
    		String numRemesa = (prefijo!=null && !prefijo.trim().equals(""))?(prefijo):"";
				numRemesa+=(String)registro.get("NUMERO");
				numRemesa+=(sufijo!=null && !sufijo.trim().equals(""))?(sufijo):"";
				
	    	
	    	if (cajgConfig != 0 && cajgConfig < 2 && DefinirRemesasCAJGAction.getFichero(usr.getLocation(), (String)registro.get("IDREMESA")) != null) {
	    		elems[1]=new FilaExtElement("download", "descargar", SIGAConstants.ACCESS_FULL);
	    	}
	    		    	
	    	String incidencias = "";
	    	if (!((String)registro.get("CUENTA_INCIDENCIAS")).trim().equals("")) {
	    		incidencias = registro.get("CUENTA_INCIDENCIAS") + " / " + registro.get("CUENTA_EXPEDIENTES");
	    	}
			%>
			
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' elementos="<%=elems%>" visibleBorrado="false" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					<td><%=numRemesa%>&nbsp;</td>
					<td><%=UtilidadesString.mostrarDatoJSP((String)registro.get("DESCRIPCION_REMESA"))%></td>
					<td style="text-align: center;">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get("IDREMESA")%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get("IDESTADO")%>">
					
					<%=UtilidadesString.mostrarDatoJSP(registro.get("FECHAGENERACION"))%></td>
					<td style="text-align: center;"><%=UtilidadesString.mostrarDatoJSP(registro.get("FECHAENVIO"))%></td>
					<td style="text-align: center;"><%=UtilidadesString.mostrarDatoJSP(registro.get("FECHARECEPCION"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(registro.get("ESTADO"))%></td>
					<td style="text-align: right;"><%=UtilidadesString.mostrarDatoJSP(incidencias)%></td>
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
	 
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	