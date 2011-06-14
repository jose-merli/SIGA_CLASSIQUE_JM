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
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
<%@ page import="com.siga.ws.CajgConfiguracion"%>
<%@ page import="com.atos.utils.*"%>
<%@page import="com.siga.beans.CajgEJGRemesaBean"%>
<%@page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.ws.SIGAWSListener"%>
<%@ page import="com.siga.ws.SIGAWSClientAbstract"%>
<%@ page import="com.siga.gratuita.action.DefinirRemesasCAJGAction"%>



<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

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
	
	Integer idInstitucion = new Integer(usr.getLocation());
	int cajgConfig = CajgConfiguracion.getTipoCAJG(idInstitucion);	
	
	int tipoPCAJGGeneral = CajgConfiguracion.getPCAJGGeneralTipo(idInstitucion);
	
	String buttons="";	
	
	boolean isPCajgTXT = cajgConfig < 2 || cajgConfig == 5;
	boolean tieneTXT = DefinirRemesasCAJGAction.getFichero(usr.getLocation(), idremesa) != null;
	boolean ejecutandose = SIGAWSListener.isEjecutandose(idInstitucion.intValue(), Integer.parseInt(idremesa));
	
	
	if (modo.equals("consultar")) {
		buttons="";			
	} else if (idEstado == 0) {//INICIADA
		if (cajgConfig != 0) {
			buttons="g,ae";//guardar y añadir expedientes
			if (isPCajgTXT) {
				buttons+=",gf";//generar fichero txt
			} else if (cajgConfig == 2) {
				if (ejecutandose) {
					buttons+=",ftp";//genera fichero txt, envio ftp
				} else {
					buttons+=",gf,ftp";//genera fichero txt, envio ftp
				}
			} else if (cajgConfig == 3) {
				if (tipoPCAJGGeneral == 1) {
					buttons+=",ws";//envio WebService
				} else {
					buttons+=",gxml";//generar XML	
				}
			} else if (cajgConfig == 4) {
				buttons+=",ws";//envio WebService
			} else if (cajgConfig == 6) {
				buttons+=",gxml";//generar XML
			}
		}
	} else if (idEstado == 1) {//GENERADA
		if (cajgConfig != 0) {
			buttons="g";//guardar
			if (isPCajgTXT || cajgConfig == 2) {//QUITAR EL == 2 CUANDO SEA DEFINITIVO EL ENVIO XML
				buttons+=",d";//descargar
			} else if (cajgConfig == 3 && tipoPCAJGGeneral != 1) {
				buttons+=",d";//descargar
			} else if (cajgConfig == 6) {
				buttons+=",d";//descargar
			}
		}
	} else if (idEstado == 2) {//enviada
		if (cajgConfig != 0) {
			buttons="g";//guardar
			if (isPCajgTXT) {
				buttons+=",d";//descargar
			} else if (cajgConfig == 2 && !SIGAWSClientAbstract.isRespondida(idInstitucion, Integer.parseInt(idremesa))) {
				if (tieneTXT){
					buttons+=",d";//descargar //QUITAR EL d y el tieneTXT CUANDO SEA DEFINITIVO EL ENVIO XML	
				} else {
					buttons+=",respFTP";//obtener respuesta
				}				
			} else if (cajgConfig == 2 && SIGAWSClientAbstract.isRespondida(idInstitucion, Integer.parseInt(idremesa))) {
				if (tieneTXT){
					buttons+=",d";//descargar //QUITAR EL d y el tieneTXT CUANDO SEA DEFINITIVO EL ENVIO XML	
				} else {
					buttons+=",resolucionFTP";//obtener resoluciones
				}
			} else if (cajgConfig == 3 && tipoPCAJGGeneral != 1) {
				buttons+=",d";//descargar
			} else if (cajgConfig == 6) {
				buttons+=",d";//descargar
			}
		}
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
			var miForm = document.forms[0];
			miForm.action="./JGR_E-Comunicaciones_Gestion.do";	
			miForm.modo.value="abrir";
			miForm.volver.value="SI";
			miForm.idRemesa.value=<%=idremesa%>;
			miForm.numero.value = "";
			miForm.target="mainWorkArea"; 
			miForm.submit(); 
		}
		
		
				
		function aniadirExpedientes(){
			sub();
			parent.aniadirExpedientes();
		}
		
		function generarFichero(){
			sub();
			deshabilitaTodos();
			parent.generarFichero();	
		}
		
		function generaXML(){	
			deshabilitaTodos();		
			parent.generaXML();	
		}

		function envioFTP(obj) {
			sub();
			deshabilitaTodos();						
			parent.envioFTP();	
		}
		
		function envioWS(obj){		
			deshabilitaTodos();
			parent.envioWS();	
		}
		
		function accionDownload(){
			sub();
			parent.accionDownload();
		}
		function accionGuardar() {
			sub();
			parent.accionGuardar();
		}
		function respuestaFTP() {
			deshabilitaTodos();			
			parent.respuestaFTP();
		}
		
		function resolucionFTP() {
			respuestaFTP();//llama al mismo metodo
		}
		
		function descargarLog(fila) {
	    	var idEjgRemesa = document.getElementById('oculto' + fila + '_5');
	    	
	    	document.DefinicionRemesas_CAJG_Form.modo.value="erroresResultadoCAJG";	    	
	    	document.DefinicionRemesas_CAJG_Form.idEjgRemesa.value=idEjgRemesa.value
		   	
			ventaModalGeneral(document.DefinicionRemesas_CAJG_Form.name,'M')
		}
		
		function deshabilita(button) {
			if (button) {
				button.disabled=true;
			}
		}
		
		function deshabilitaTodos() {
			deshabilita(document.getElementById('idButtonEnvioFTP'));
			deshabilita(document.getElementById('idButtonGeneraXML'));			
			deshabilita(document.getElementById('idButtonEnvioWS'));
			deshabilita(document.getElementById('idButtonRespuestaFTP'));
			deshabilita(document.getElementById('idButtonResolucionFTP'));			
			deshabilita(document.getElementById('idButtonAniadirExpedientes'));
		}
		
		function inicio() {
			<%if (ejecutandose) {%>
				deshabilitaTodos();				
			<%}%>			
		}

	</script>
</head>

<body onload="inicio()">

	

	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idRemesa" value="">
		<html:hidden property="idInstitucion" value = ""/>
		<input type="hidden" name="idTipoEJG" value="">
		<input type="hidden" name="anio" value="">
		<input type="hidden" name="numero" value="">
		<input type="hidden" name="idEjgRemesa" value="">		
		<input type="hidden" name="volver" value="">
		
	</html:form>	
	
	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property="modo"          value = ""/>
		<html:hidden property="anio"          value = ""/>
		<html:hidden property="numero"        value = ""/>
		<html:hidden property="idTipoEJG"     value = ""/>
		<html:hidden property="idInstitucion" value = ""/>

		<!-- Campo obligatorio -->
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>
	
	
		
	<siga:ConjBotonesAccion botones="<%= buttons %>" clase="botonesSeguido" titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa"/>	
		
	<siga:TablaCabecerasFijas 		   
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaEJG.literal.turno, gratuita.busquedaEJG.literal.guardia, gratuita.busquedaEJG.literal.anyo, 
		   	gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.tipoEJG, gratuita.listadoActuacionesAsistencia.literal.fecha, 
		   	gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante, gratuita.pcajg.listadoEJGremesa.enNuevaRemesa,"
		   tamanoCol="13,12,4,5,15,9,8,13,7,13"
		   alto="100%" 
		   ajustePaginador="true"
		   ajusteBotonera="true"	
		   activarFilaSel="true">		   			
		   

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	String botones = "C";
	    	
	    	if (!modo.equals("consultar")){
	    		botones += ",E";
	    		if (idEstado == 0) {
	    			botones += ",B";	
	    		}
	    		
	    	}
	    	
	    	String fRatificacion = "";
			while (recordNumber-1 < resultado.size())
			{			
			  
		    Row fila = (Row)resultado.elementAt(recordNumber-1);
			Hashtable registro = (Hashtable) fila.getRow();
			
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado del idfacturacion
	    	ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usr);
			
	    	FilaExtElement[] elems = new FilaExtElement[1];
			if (!registro.get("ERRORES").equals("0")) {
	    		elems[0]=new FilaExtElement("descargaLog", "descargarLog", "gratuita.BusquedaRemesas_CAJG.literal.IncidenciasEnvio", SIGAConstants.ACCESS_FULL);
			}
			String CODIGO=null;
			if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
				CODIGO="&nbsp;";
			else
				CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);
			
			String enNuevaRemesa = "Si";
			if (((String)registro.get("EN_NUEVA_REMESA")).trim().equals("0")) {
				enNuevaRemesa = "No";
			}
			

			%>
				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' elementos="<%=elems%>" botones="<%=botones%>" visibleconsulta="false" visibleEdicion="false" pintarespacio="false" clase="listaNonEdit" modo="<%=modo%>">
					<td><%=(String)registro.get("TURNO")%>&nbsp;</td>
					<td><%=(String)registro.get("GUARDIA")%>&nbsp;</td>
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=registro.get(ScsEJGBean.C_IDINSTITUCION)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=registro.get(CajgEJGRemesaBean.C_IDEJGREMESA)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=registro.get(ScsEJGBean.C_NUMEJG)%>">
					
					<%=registro.get(ScsEJGBean.C_ANIO)%></td>
					<td><%=CODIGO%></td>
					<td><%=registro.get("TIPOEJG")%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=(String)registro.get("ESTADO")%>&nbsp;</td>
					<td><%=(String)registro.get(ScsPersonaJGBean.C_NOMBRE) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO1) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO2)%>&nbsp;</td>
					<td><%=enNuevaRemesa%></td>
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

	<script type="text/javascript">
		function consultar(fila) {			
			document.forms[0].modo.value = "Ver";
			consultaEdita(fila);
		}
		
		function editar(fila) {
			document.forms[0].modo.value = "Editar";
			consultaEdita(fila)
		}

		function consultaEdita(fila) {
			document.forms[0].target = document.forms[1].target;			
			document.forms[0].action = document.forms[1].action;		
			
			document.forms[0].tablaDatosDinamicosD.value = document.forms[1].tablaDatosDinamicosD.value
			document.forms[0].actionModal.value = document.forms[0].actionModal.value

			var idTipoEJG = document.getElementById('oculto' + fila + '_1');
			var idInstitucion = document.getElementById('oculto' + fila + '_2');
			var anio = document.getElementById('oculto' + fila + '_3');
			var numero = document.getElementById('oculto' + fila + '_4');
			var idEjgRemesa = document.getElementById('oculto' + fila + '_5');
			
			document.forms[0].idTipoEJG.value = idTipoEJG.value;				
			document.forms[0].idInstitucion.value = idInstitucion.value;
			document.forms[0].anio.value = anio.value;				
			document.forms[0].numero.value = numero.value;
			document.forms[0].idEjgRemesa.value = idEjgRemesa.value;
			
			document.forms[0].submit();
		}
	
		

	</script>
</html>
	