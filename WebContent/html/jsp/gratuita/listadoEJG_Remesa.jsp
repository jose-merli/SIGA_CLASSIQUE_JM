<!DOCTYPE html>

<html>
<head>
<!-- listadoEJG_Remesa.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="org.redabogacia.sigaservices.app.helper.AsignaVeredaHelper.ASIGNA_VERSION"%>
<%@ page import="org.redabogacia.sigaservices.app.helper.AsignaVeredaHelper"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorBind"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
<%@ page import="com.siga.ws.CajgConfiguracion"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.CajgEJGRemesaBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.ws.SIGAWSListener"%>
<%@ page import="com.siga.ws.SIGAWSClientAbstract"%>
<%@ page import="com.siga.gratuita.action.DefinirRemesasCAJGAction"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<!-- TAGLIBS -->
<%@taglib uri =	"struts-bean.tld" 	prefix="bean" %>
<%@taglib uri = "struts-html.tld" 	prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga" %>
<%@taglib uri =	"struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<!-- JSP -->

<bean:define id="registrosSeleccionados" name="DefinicionRemesas_CAJG_Form" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="DefinicionRemesas_CAJG_Form" property="datosPaginador" type="java.util.HashMap"/>
<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />

<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idioma=usr.getLanguage().toUpperCase();
	
	ses.removeAttribute("resultado");
	ASIGNA_VERSION versionAsignaVereda = AsignaVeredaHelper.getAsignaVersion(Short.valueOf(usr.getLocation()));
	
	String idremesa=(String)request.getAttribute("idremesa");
	
	
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheckPersona = "";
	if (datosPaginador!=null) {
	 if ( datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  	resultado = (Vector)datosPaginador.get("datos");
	    PaginadorBind paginador = (PaginadorBind)datosPaginador.get("paginador");
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
	String parametrosAction=app+path+".do?noReset=true&idRemesa=" + idremesa;
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
		
		if(path.equals("/JGR_E-Comunicaciones_InfEconomico") ){
			buttons+="g,ae,val,ws";//envio WebService
		}else{
			if (cajgConfig != 0) {
				buttons="g,ae";//guardar y añadir expedientes
				if (isPCajgTXT) {
					buttons+=",val,gf";//generar fichero txt
					if (cajgConfig == 5)buttons+=",r";
				} else if (cajgConfig == 2) {				
					buttons+=",val,ftp";//validar remesa, envio ftp
				} else if (cajgConfig == 3) {
					if (tipoPCAJGGeneral == 1) {
						buttons+=",ws";//envio WebService
					} else {
						buttons+=",val,gxml";//generar XML	
					}
				} else if (cajgConfig == 4) {//PAMPLONA
					buttons+=",val,ws";//envio WebService
				} else if (cajgConfig == 6) {
					if(versionAsignaVereda!=null && versionAsignaVereda.getVersion().equals(ASIGNA_VERSION.VERSION_2.getVersion())){
						buttons+=",val,ws";//generar XML
					}else
						buttons+=",val,gxml";//generar XML			
				} else if (cajgConfig == 7) {
					buttons+=",val,ws";//envio WebService GVasco
				} else if (cajgConfig == 8) {
					buttons+=",val,ws";//envio WebService GenValenciana
				} else if (cajgConfig == 9 ) {
					if (tipoPCAJGGeneral == 1) {
						buttons+=",val,ws";//envio WebService EJIS
						
					} else {
						buttons+=",gf";//genera fichero
							
					}
					//ELIMINAR ,gf CUANDO LA INTEGRACION DE ANDALUCIA SEA COMPLETA
					
				}else if (cajgConfig == 10 ) {
					buttons+=",val,gxml";//envio WebService EJIS de Canarias
						//buttons+=",val,ws";//envio WebService EJIS de Canarias
						//ELIMINAR ,gxml CUANDO LA INTEGRACION DE CANARIAS SEA COMPLETA
				}
			}
		}
	} else if (idEstado == 1) {//GENERADA
		if(path.equals("/JGR_E-Comunicaciones_InfEconomico") ){
			buttons="g";//guardar
		}else{
			if (cajgConfig != 0) {
				buttons="g";//guardar
				if (isPCajgTXT || cajgConfig == 2) {//QUITAR EL == 2 CUANDO SEA DEFINITIVO EL ENVIO XML
					buttons+=",d";//descargar
				} else if (cajgConfig == 3 && tipoPCAJGGeneral != 1) {
					buttons+=",d";//descargar
				} else if (cajgConfig == 6) {
					if(versionAsignaVereda==null || !versionAsignaVereda.getVersion().equals(ASIGNA_VERSION.VERSION_2.getVersion()))
						buttons+=",d";//descargar
				}else if (cajgConfig == 9) {//ELIMINAR CUANDO LA INTEGRACION DE ANDALUCIA SEA COMPLETA
					if (tipoPCAJGGeneral == 0) {
						buttons+=",d";//descarga envio
							
					}
				}else if (cajgConfig == 10) {//DESCARGAR XML DE CANARIAS
					buttons+=",ftp";//descarga envio
				}
			}
		}
	} else if (idEstado == 2) {//enviada
		if(path.equals("/JGR_E-Comunicaciones_InfEconomico") ){
			buttons+="g";//hA HABIDO UN ERROR EN LA RESPUESTA
		}else{
			if (cajgConfig != 0) {
				buttons="g";//guardar
				if (cajgConfig == 5) {
					buttons+=",d,mri";//descargar//marcar como respondidos con errores
				}else if (isPCajgTXT) {
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
					if(versionAsignaVereda==null || !versionAsignaVereda.getVersion().equals(ASIGNA_VERSION.VERSION_2.getVersion()))
						buttons+=",d";//descargar
				}else if (cajgConfig == 9) {//ELIMINAR CUANDO LA INTEGRACION DE ANDALUCIA SEA COMPLETA
					if (tipoPCAJGGeneral == 0) {
						buttons+=",d";//descarga envio
							
					}
				}else if (cajgConfig == 10) {//DESCARGAR XML DE CANARIAS
					//buttons+=",d";//descarga envio
				}
			}
		}
		
	}else if (idEstado == 3) {
		buttons="g";//guardar
		if (cajgConfig == 5) {
			if(!path.equals("/JGR_E-Comunicaciones_InfEconomico") )
				buttons+=",d,mri";
		}else if (cajgConfig == 10) {//DESCARGAR XML DE CANARIAS
			buttons+=",d";//descarga envio
		}
		
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	
	
<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
<style type="text/css">
	.ui-dialog-titlebar-close {
		  visibility: hidden;
	}
	td{
		padding-top: .3em;
		height: 27px;
	}
</style>
	<script type="text/javascript">
	
		function refrescarLocal() {
			parent.filtrado();
		}
		
		function accionVolver(){
			sub();	
			
			var miForm = document.forms[0];
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

		function validarRemesa(obj) {
			sub();
			deshabilitaTodos();						
			parent.validarRemesa();	
		}
		
		function generaXML(){	
			deshabilitaTodos();		
			parent.generaXML();	
		}

		function envioFTP(obj) {
			if (confirm('<siga:Idioma key="gratuita.cajg.info.envioFTP"/>')) {
				sub();
				deshabilitaTodos();						
				parent.envioFTP();
			}	
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
			deshabilita(document.getElementById('idButtonGuardar'));
			deshabilita(document.getElementById('idButtonEnvioFTP'));
			deshabilita(document.getElementById('idButtonValidarRemesa'));
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

<body onload="inicio();cargarChecks();checkTodos();">


	<html:form action="${path}?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="actionModal" value="">
		<html:hidden property="idRemesa" />
		<html:hidden property="idInstitucion" />
		<input type="hidden" name="idTipoEJG" value="">
		<input type="hidden" name="anio" value="">
		<input type="hidden" name="numero" value="">
		<input type="hidden" name="idEjgRemesa" value="">
		<html:hidden property="jsonVolver"/>
		<input type="hidden" name="volver" value="">		
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		<html:hidden property="datosSolicInformeEconomico"  styleId="datosSolicInformeEconomico" />
		<html:hidden property = "idTipoRemesa" />
		
		
	</html:form>	
	
	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property="modo"          value = ""/>
		<html:hidden property="anio"          value = ""/>
		<html:hidden property="numero"        value = ""/>
		<html:hidden property="idTipoEJG"     value = ""/>
		<html:hidden property="idInstitucion" value = ""/>
	    <html:hidden property="jsonVolver" value="${DefinicionRemesas_CAJG_Form.jsonVolver}"/>
		<!-- Campo obligatorio -->
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="actionModal" value="">
	</html:form>
		
	<siga:ConjBotonesAccion botones="<%= buttons %>" clase="botonesSeguido" titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa"/>	
	
	<c:set var="columnNames" value="gratuita.busquedaEJG.literal.turnoGuardiaEJG,
			gratuita.busquedaEJG.literal.anyo, 
			gratuita.busquedaEJG.literal.codigo, 
			gratuita.listadoActuacionesAsistencia.literal.fecha, 
			gratuita.busquedaEJG.literal.estadoEJG, 
			gratuita.busquedaEJG.literal.solicitante, 
			gratuita.pcajg.listadoEJGremesa.enNuevaRemesa," />
	<c:set var="columnSizes" value="22,5,5,8,20,22,8,10" />
	<%	 if((idEstado==0 || idEstado==2 ||idEstado==3)&& cajgConfig==5 ){%>
		<c:set var="columnNames" value="<input type='checkbox' name='chkGeneral' id='chkGeneral' onclick='cargarChecksTodos(this);'/>,
			gratuita.busquedaEJG.literal.turnoGuardiaEJG,
			gratuita.busquedaEJG.literal.anyo, 
			gratuita.busquedaEJG.literal.codigo, 
			gratuita.listadoActuacionesAsistencia.literal.fecha, 
			gratuita.busquedaEJG.literal.estadoEJG, 
			gratuita.busquedaEJG.literal.solicitante, 
			gratuita.pcajg.listadoEJGremesa.enNuevaRemesa," />
	<c:set var="columnSizes" value="5,17,5,5,8,20,22,8,10" />
	<%}%>
	
		
	<siga:Table 		   
		name="tablaDatos"
		border="1"
		columnNames="${columnNames}"
		columnSizes="${columnSizes}">		   

<%
		if (resultado.size()>0) {
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
			while (recordNumber-1 < resultado.size()) {			
			  
		    	Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();
				String idEstadoEJGRemesa = registro.get("IDESTADOEJGREMESA")!=null?(String)registro.get("IDESTADOEJGREMESA"):""; 
				 String permitirSolInfEconomico = registro.get("PERMITIRSOLINFECONOMICO")!=null?(String)registro.get("PERMITIRSOLINFECONOMICO"):"0";
				//String permitirSolInfEconomico = "1";
				String registroError = registro.get("ERRORES")!=null&&!registro.get("ERRORES").toString().equals("")?registro.get("ERRORES").toString():"0"; 
				String deshabilitarCheck = "disabled";
				if(idEstado==2 ||idEstado==3)
					deshabilitarCheck = registroError.equals("0")?"":"disabled='disabled'";
				else if(idEstado==0)
					deshabilitarCheck = "";
					
				
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado del idfacturacion
	    		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usr);
			
	    		FilaExtElement[] elems = new FilaExtElement[1];
				if (!registroError.equals("0")) {
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
					
					<%
					 if((idEstado==0 ||idEstado==2 ||idEstado==3)&& cajgConfig==5){%>
						
						<td align="center">
						<%String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("IDTIPOEJG")+"||"+registro.get("ANIO")+"||"+registro.get("NUMERO")+"||"+registro.get("IDEJGREMESA")+"||"+registro.get("NUMEROINTERCAMBIO")+"||"+registro.get("PERMITIRSOLINFECONOMICO");
						boolean isChecked = false;
						for (int z = 0; z < registrosSeleccionados.size(); z++) {
							Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
							String clave = (String)clavesRegistro.get("CLAVE");
							if (valorCheck.equals(clave) && registroError.equals("0")) {
								isChecked = true;
								break;
							}
						}if (isChecked) {%>
								<input type="checkbox" value="<%=valorCheck%>" id="<%=valorCheck%>" name="chkPersona"  checked <%=deshabilitarCheck%> onclick="pulsarCheck(this)">
							<%} else {%>
								<input type="checkbox" value="<%=valorCheck%>" id="<%=valorCheck%>" name="chkPersona" <%=deshabilitarCheck%> onclick="pulsarCheck(this)" >
						<%}%>
					
					
					
					</td>	
					
					<%}%>
					
					
					
					
					

					<td><%=(String)registro.get("TURNO")%>&nbsp;<%=(String)registro.get("GUARDIA")%>&nbsp;</td>
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=registro.get(ScsEJGBean.C_IDINSTITUCION)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=registro.get(CajgEJGRemesaBean.C_IDEJGREMESA)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=registro.get(ScsEJGBean.C_NUMEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=registro.get("NUMEROINTERCAMBIO")%>">
					
					
					
					<%=registro.get(ScsEJGBean.C_ANIO)%></td>
					<td><%=CODIGO%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=(String)registro.get("ESTADO")%>&nbsp;</td>
					<td><%=(String)registro.get(ScsPersonaJGBean.C_NOMBRE) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO1) + " " + (String)registro.get(ScsPersonaJGBean.C_APELLIDO2)%>&nbsp;</td>
					<td><%=enNuevaRemesa%></td>
					
					
				</siga:FilaConIconos>		
<% 	
				recordNumber++;
			} 
		} else {
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		}
%>
	</siga:Table>

<%
String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
		: registrosSeleccionados.size()));	
if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>

		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						registrosSeleccionados="<%=regSeleccionados%>"
						idioma="<%=idioma%>"
						modo="buscarPorEJG"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:28px; left: 0px"
						distanciaPaginas=""
						action="<%=parametrosAction%>" />
<%
	}
%>
	
	 <siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	 	
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
<div id="dialogo"  title='Error manual de envio ' style="display:none">
<div>&nbsp;</div>
	<div>
		<div class="labelText">
   			<label for="errorManual"  style="width:90px;float:left;color: black">Descripción </label><textarea  id="errorManual" name="errorManual"
			                	onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
			                	style="overflow-y:auto; overflow-x:hidden; width:350px; height:70px; resize:none;" 
			                	class="box"></textarea>  
		</div>
	</div>
</div>
	
	<!-- FIN: SUBMIT AREA -->		
</body>	

<script type="text/javascript">

ObjArray = new Array();

Array.prototype.indexOf = function(s) {
for (var x=0;x<this.length;x++) if(this[x] == s) return x;
	return false;
}

   
function pulsarCheck(obj){
	if (!obj.checked ){
	   		
		ObjArray.splice(ObjArray.indexOf(obj.value),1);
		seleccionados1=ObjArray;
	}else{
		ObjArray.push(obj.value);
	   	seleccionados1=ObjArray;
	}
	  	
	  	
	document.forms[0].registrosSeleccionados.value=seleccionados1;
	
	document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	checkTodos();
	
	   
}
function cargarChecks(){
	<%if (registrosSeleccionados!=null){
   		for (int p=0;p<registrosSeleccionados.size();p++){
	   		Hashtable clavesEJG= (Hashtable) registrosSeleccionados.get(p);
			valorCheckPersona=(String)clavesEJG.get("CLAVE");
			String[] claves =  valorCheckPersona.split("\\|\\|");
			String permitirSolInfEconomico = claves[6];
			//Con estos dos comentarios probablemte no funciona el check masivo del informe economico 
			//if (permitirSolInfEconomico.equals("1")) { %>
				ObjArray.push('<%=valorCheckPersona%>');
			<%
			//}
		} 
   	}%>
	ObjArray.toString();
	seleccionados1=ObjArray;
		
	document.forms[0].registrosSeleccionados.value=seleccionados1;
	
	if(document.getElementById('registrosSeleccionadosPaginador'))
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		
}
function cargarChecksTodos(o){
	   if (document.getElementById('registrosSeleccionadosPaginador')){ 	
		var conf = confirm("<siga:Idioma key='paginador.message.marcarDesmarcar'/>"); 
   	 
   	if (conf){
		ObjArray = new Array();
	   	if (o.checked){
	   		parent.seleccionarTodos('<%=paginaSeleccionada%>');
	   	 		
			
		}else{
			ObjArray1= new Array();
		 	ObjArray=ObjArray1;
		 	seleccionados1=ObjArray;
		 	if(seleccionados1){
				document.forms[0].registrosSeleccionados.value=seleccionados1;
				var ele = document.getElementsByName("chkPersona");
				for (i = 0; i < ele.length; i++) {
					if(!ele[i].disabled)	
						ele[i].checked = false; 
						
						
				}
			}

		 }
   	  
   	  }else{
   	  	if (!o.checked ){
	   	  		var ele = document.getElementsByName("chkPersona");
					
			  	for (i = 0; i < ele.length; i++) {
			  		if(!ele[i].disabled){
			  			if(ele[i].checked){	
	     					ele[i].checked = false;
	     				
							ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
						}
					}
			   	}
			   	
			   	seleccionados1=ObjArray;
		   }else{
			   	var ele = document.getElementsByName("chkPersona");
						
			  	for (i = 0; i < ele.length; i++) {
			  		if(!ele[i].disabled){
						if(!ele[i].checked){				  		
		    				ele[i].checked = true;
							ObjArray.push(ele[i].value);
						}
					}
			   	}
			   		
		   		seleccionados1=ObjArray;
		   }
		   document.forms[0].registrosSeleccionados.value=seleccionados1;
	   		
   	  }
   	 if (document.getElementById('registrosSeleccionadosPaginador')){ 		 
	  document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	 }
	} 
 }
   
	function checkTodos(){
	 	var ele = document.getElementsByName("chkPersona");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
	  			if(!ele[i].checked && !ele[i].disabled){
					todos=0;
					break;
				} 
			}
	  	if(document.getElementById("chkGeneral")){
		   	if (todos==1){
				document.getElementById("chkGeneral").checked=true;
			}else{
				document.getElementById("chkGeneral").checked=false;
			}
	  	}
	}

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
	function accionComunicar() {
		sub();
		datos = "";
		//String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("IDTIPOEJG")+"||"+registro.get("ANIO")+"||"+registro.get("NUMERO")+"||"+registro.get("IDEJGREMESA")+"||"+registro.get("NUMEROINTERCAMBIO")+"||"+registro.get("PERMITIRSOLINFECONOMICO");
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			
			claves =  idRegistros.split("||");
			idInstitucion  = claves[0];
			idTipoEJG  = claves[1];
			anio  = claves[2];
			numero  = claves[3];
			idEjgRemesa  = claves[4];
			permitirSolInfo  = claves[6];
		//if(permitirSolInfo=='1')
 		   		datos = datos +idInstitucion + "##" +idTipoEJG+"##" +anio+"##" +numero+"##" +idEjgRemesa+"%%%";
		}
		if (datos.length==0){
    		alert("<siga:Idioma key='general.message.seleccionar'/>");
	    	fin();
	    	return false;
		}
		document.forms[0].datosSolicInformeEconomico.value = datos;
		document.forms[0].modo.value = "comunicarInfEconomico";
		document.forms[0].submit();
		
	}
	function closeDialog(){
		jQuery("#dialogo").dialog("close");
	}
	function openDialog(){
		jQuery("#dialogo").dialog(
			{
				height: 250,
			   	width: 525,
				modal: true,
				resizable: false,
				
				buttons: {
				    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ marcaRespuestaIncorrecta(jQuery("#errorManual").val()); }},
				          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog();}}
				}
			}
		);
		
		jQuery(".ui-widget-overlay").css("opacity","0");
		

	}
	
	function marcaRespuestaIncorrecta(respuestaErronea) {
		sub();
		datos = "";
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			
			claves =  idRegistros.split("||");
			idInstitucion  = claves[0];
			idTipoEJG  = claves[1];
			anio  = claves[2];
			numero  = claves[3];
			idejgremesa  = claves[4];
			numeroIntercambio  = claves[5];
			permitirSolInfo  = claves[6];
			var idCheck = idInstitucion+"||"+idTipoEJG+"||"+anio+"||"+numero+"||"+idejgremesa+"||"+numeroIntercambio+"||"+permitirSolInfo;
			if(document.getElementById(idCheck).checked){
		   			datos = datos +idInstitucion + "##" +idTipoEJG+"##" +anio+"##" +numero+"##"+idejgremesa+"##" +numeroIntercambio+"##" +respuestaErronea+"%%%";
			}
		}
		parent.marcarRespuestaIncorrecta(datos);
	}
	function accionRestablecer() {
		var conf = confirm("Se va a eliminar toda relación con envios a la comisión de los elementos seleccionados. La proxima vez que se genere una remesa con estos expedientes se enviara como nuevo traslado, NO como actualización.¿Desea continuar?"); 
	   	if (conf){
	   		sub();
			datos = "";
			for (i = 0; i < ObjArray.length; i++) {
				var idRegistros = ObjArray[i];
				
				claves =  idRegistros.split("||");
				idInstitucion  = claves[0];
				idTipoEJG  = claves[1];
				anio  = claves[2];
				numero  = claves[3];
				idejgremesa  = claves[4];
				numeroIntercambio  = claves[5];
				permitirSolInfo  = claves[6];
				var idCheck = idInstitucion+"||"+idTipoEJG+"||"+anio+"||"+numero+"||"+idejgremesa+"||"+numeroIntercambio+"||"+permitirSolInfo;
				if(document.getElementById(idCheck).checked){
			   			datos = datos +idInstitucion + "##" +idTipoEJG+"##" +anio+"##" +numero+"##"+idejgremesa+"##" +numeroIntercambio+"%%%";
				}
			}
	   		
			parent.limpiarReferenciasEnvioCAJG(datos);
	   	}
	}
	
	
	function marcarRespuestaIncorrecta() {
		var conf = confirm("<siga:Idioma key='e_comunicaciones.confirmar.marcarRespuestaIncorrecta'/>"); 
		//String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("IDTIPOEJG")+"||"+registro.get("ANIO")+"||"+registro.get("NUMERO")+"||"+registro.get("IDEJGREMESA")+"||"+registro.get("NUMEROINTERCAMBIO")+"||"+registro.get("PERMITIRSOLINFECONOMICO");
		 
	   	if (conf){
	   		openDialog();	   		
	   	}
		
	}
	
	
</script>

</html>	