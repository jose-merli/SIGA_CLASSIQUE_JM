<!-- listadoEJG_Remesa.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsMaestroEstadosEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
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
	
	ses.removeAttribute("resultado");
	
	String idremesa=(String)request.getAttribute("idremesa");
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
		    Paginador paginador = (Paginador)hm.get("paginador");
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
	String action=app+"/JGR_E-Comunicaciones_Gestion.do?noReset=true&idRemesa=" + idremesa;
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	CajgRemesaEstadosAdm admBean =  new CajgRemesaEstadosAdm(usr);	
	int idEstado = admBean.UltimoEstadoRemesa(usr.getLocation(), idremesa);
	
	
	String buttons="";
	
	
	if (modo.equals("consultar")) {
		buttons="";			
	} else if (idEstado == 0) {
		buttons="g,ae,gf,gxml";
	} else if (idEstado > 0) {
		buttons="g,d,gxml";
	} 
	
	
    /**************/

	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
	
		function refrescarLocal() {
			parent.refrescarLocal();
		}
		
		function accionVolver(){
			sub();			
			document.forms[0].action="./JGR_E-Comunicaciones_Gestion.do";	
			document.forms[0].modo.value="abrir";
			document.forms[0].volver.value="SI";
			document.forms[0].idRemesa.value=<%=idremesa%>;
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
		
				
		function aniadirExpedientes(){
			sub();
			parent.aniadirExpedientes();
		}
		
		function generarFichero(){
			sub();
			parent.generarFichero();	
		}
		
		function generaXML(){			
			parent.generaXML();	
		}
		
		function accionDownload(){
			sub();
			parent.accionDownload();
		}
		function accionGuardar() {
			sub();
			parent.accionGuardar();
		}
	</script>
</head>

<body>

	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idRemesa" value="">
		<input type="hidden" name="volver" value="">
	</html:form>	
	
	<siga:ConjBotonesAccion botones="<%= buttons %>" clase="botonesSeguido" titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa"/>
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoEJG"
		   borde="1"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaEJG.literal.turno, gratuita.busquedaEJG.literal.guardia, gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.tipoEJG, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante,"
		   tamanoCol="15,15,5,6,15,9,10,15,10"
		   alto="100%" 
		   ajustePaginador="true"
		   ajusteBotonera="true"		
		   			 >

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	String botones = "";
	    	
	    	if (!modo.equals("consultar") && (idEstado == 0)) {
	    		botones = "B";
	    	}
	    	
	    	String fRatificacion = "";
			while (recordNumber-1 < resultado.size())
			{			
			  
		    Row fila = (Row)resultado.elementAt(recordNumber-1);
			Hashtable registro = (Hashtable) fila.getRow();
			
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado del idfacturacion
	    	ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usr);

			

			String CODIGO=null;
			if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
				CODIGO="&nbsp;";
			else
				CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);

			%>
			
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" visibleconsulta="false" visibleEdicion="false" pintarespacio="false" clase="listaNonEdit">
					<td><%=(String)registro.get("TURNO")%>&nbsp;</td>
					<td><%=(String)registro.get("GUARDIA")%>&nbsp;</td>
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=idremesa%>">
					<input type='hidden' name='datosCarta' value='idinstitucion==<%=usr.getLocation()%>##idtipo==<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>##anio==<%=registro.get(ScsEJGBean.C_ANIO)%>##numero==<%=registro.get(ScsEJGBean.C_NUMERO)%>'>
					<%=registro.get(ScsEJGBean.C_ANIO)%></td>
					<td><%=CODIGO%></td>
					<td><%=registro.get("TIPOEJG")%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=(String)registro.get("ESTADO")%>&nbsp;</td>
					<td><%=(String)registro.get(ScsPersonaJGBean.C_NOMBRE) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO1) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO2)%>&nbsp;</td>
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
	<%
	}else {
	%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>
	</siga:TablaCabecerasFijas>

     <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPorEJG"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:28px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	
	 <siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	