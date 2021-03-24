<!DOCTYPE html>

<html>
<head>
<!-- listadoEstadosEJG.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants.PARAMETRO"%>
<%@page import="com.siga.tlds.FilaExtElement"%>

<!-- TAGLIBS -->
<%@taglib uri =	"struts-bean.tld" 	prefix="bean"%>
<%@taglib uri = "struts-html.tld" 	prefix="html"%>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");	
		
	String botonesPie="", botones="",anio= "", numero="", idTipoEJG = "",idInstitucion = "";
	
	if (accion.equalsIgnoreCase("ver")){
		botonesPie = "V";
	}
	else {
		botonesPie = "V,N";
	}
	boolean booVerHistorico =  UtilidadesString.stringToBoolean((String)request.getAttribute("verHistorico"));
	Hashtable fila = new Hashtable();
	
	try {		
		anio = request.getParameter("ANIO").toString();
		numero = request.getParameter("NUMERO").toString();
		idTipoEJG = request.getParameter("IDTIPOEJG").toString();
		idInstitucion = request.getParameter("IDINSTITUCION").toString();
	} catch(Exception e){
		Hashtable miHash = (Hashtable)request.getAttribute("DATOSEJG");
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		idInstitucion =  miHash.get("IDINSTITUCION").toString();
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.listadoSubzonas.literal.listadoSubzonas"/></title>
	<script type="text/javascript">
		function refrescarLocal() {
			buscar();
		}
	</script>
	<siga:Titulo titulo="gratuita.EJG.estados" localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body class="tablaCentralCampos">
	
	<html:form action="/JGR_EstadosEJG" method="post" target="mainPestanas" style="display:none">
		<input type="hidden" name="modo" value="<%=accion%>">
		<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG%>">
		<input type="hidden" name="anio" value="<%=anio%>">
		<input type="hidden" name="numero" value="<%=numero%>">
		<input type="hidden" name="idInstitucion" value="<%=idInstitucion%>">
		<input type="hidden" name="verHistorico" >
		<input type="hidden" name="idEstadoPorEJG" >
		<html:hidden name="DefinirEstadosEJGForm"  styleId="jsonVolver" property = "jsonVolver"  />
		
	</html:form>
		
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
<%  		
				String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
				ScsEJGAdm adm = new ScsEJGAdm (usr);
					
				Hashtable hTitulo = adm.getTituloPantallaEJG(idInstitucion, anio, numero,idTipoEJG,(String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString()));

				if (hTitulo != null) {
					t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
					t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
					t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
					t_anio      = (String)hTitulo.get(ScsEJGBean.C_ANIO);
					t_numero    = (String)hTitulo.get(ScsEJGBean.C_NUMEJG);
					t_tipoEJG   = (String)hTitulo.get("TIPOEJG");
				}
				
				if((t_anio==null) ||(t_numero==null)){
					botonesPie="V";
				}
					
%>
				<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>
	<%
	String columnNames = "";
	String columnSize = "";
	if(booVerHistorico){
		columnNames = "gratuita.operarEJG.literal.fecha,general.baja,certificados.solicitudes.literal.fechaEstado, pestana.justiciagratuitaejg.estados, pestana.justiciagratuitaejg.observaciones, pestana.justiciagratuitaejg.automatico, Prop.,";
		columnSize ="12,5,10,28,25,5,5,10";
	}else{
		columnNames = "certificados.solicitudes.literal.fechaEstado, pestana.justiciagratuitaejg.estados, pestana.justiciagratuitaejg.observaciones, pestana.justiciagratuitaejg.automatico,  Prop.,";
		columnSize ="22,30,28,5,5,10";
	}
	
	%>
	<siga:Table 		   
	   name="listadoDocumentacion"
	   border="2"
	   columnNames="<%=columnNames %>"
	   columnSizes="<%=columnSize %>"
	   modal="P">
	   
<%
		if (obj.size()>0) {
	    	int recordNumber=1;
			boolean blPropietarioComision = false;
			String stPropietario;
			boolean blAutomatico = false;
			
			while (recordNumber-1 < obj.size()) {			
				fila = (Hashtable)obj.get(recordNumber-1);
				String automatico=(String)fila.get("AUTOMATICO");
				if(fila.get("PROPIETARIOCOMISION")!=null && fila.get("PROPIETARIOCOMISION").toString().equalsIgnoreCase("1")){
					blPropietarioComision=true;
					stPropietario="CAJG";
				} else {
					blPropietarioComision=false;
					stPropietario="ICA";
				}

				if (automatico.equals("1")){
					automatico="Si";
					//stPropietario="";
					blAutomatico=true;
				} else {
					automatico="No";
					blAutomatico=false;
				}
				
				if((usr.isComision()&&blPropietarioComision)||(!usr.isComision()&&!blPropietarioComision)){
					if (!blAutomatico && (fila.get("FECHABAJA")==null ||fila.get("FECHABAJA").equals("")) ) {
					 	botones="C,E,B";
					} else {
					 	botones="C";
					}
				} else {
					botones="C";
				}
				FilaExtElement[] elems = new FilaExtElement[1];
				if(fila.get("botonEnvio") != null && ((String)fila.get("botonEnvio")).equals("1")){
					elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
				}
				
%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' pintarEspacio="false"  visibleConsulta="false" botones="<%=botones%>" elementos="<%=elems%>"   clase="listaNonEdit" modo="<%=accion%>">
					
					<%if(booVerHistorico){%>
						<td>
							<%=GstDate.getFormatedDateMedium("",fila.get("FECHAMODIFICACION").toString())%>&nbsp;
						</td>
						<td>
							<%=fila.get("FECHABAJA")!=null &&!fila.get("FECHABAJA").equals("")?"Si":"No" %>&nbsp;
						</td>
						
					<%}%>
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDESTADOPOREJG")%>">
					    <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("AUTOMATICO")%>">
						<%=GstDate.getFormatedDateShort("",fila.get("FECHAINICIO").toString())%>&nbsp;
					</td>
					<td><%=fila.get("DESCRIPCION")%>&nbsp;</td>
					<td><%=fila.get("OBSERVACIONES")%>&nbsp;</td>
					<td align="center"><%=automatico%>&nbsp;</td>
					<td align="center"><%=stPropietario%>&nbsp; </td>
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
	
	<!-- FIN: SUBMIT AREA -->	
	<div style="width:200px; position:absolute; left:400px;bottom:0px;z-index:99;">
				<table align="center" border="0" class="botonesSeguido">
					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
							
							<%if (booVerHistorico) { %>
								<input type="checkbox" id="verHistorico" name="verHistorico" onclick="verHistorico(this);" checked/>
							<%} else {%>
								<input type="checkbox" id="verHistorico" name="verHistorico" onclick="verHistorico(this);"/>
							<%}%>
							
								
						</td>							
					</tr>
				</table>
			</div>
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="<%=botonesPie %>" clase="botonesDetalle" modo="<%=accion%>"/>
	
	
	
	<script type="text/javascript">		
	
		function verHistorico(o) {
			if (o.checked) {
				document.DefinirEstadosEJGForm.verHistorico.value = "1";
			} else {
				document.DefinirEstadosEJGForm.verHistorico.value = "0";
			}
			document.DefinirEstadosEJGForm.modo.value = "abrir";
			document.DefinirEstadosEJGForm.submit();
		}
		function refrescarLocal() {
			return buscar();
			
		}
		//Asociada al boton Cerrar
		function accionVolver() {
			
			
			if(document.DefinirEstadosEJGForm.jsonVolver && document.DefinirEstadosEJGForm.jsonVolver.value!=''){
				
				jSonVolverValue = document.DefinirEstadosEJGForm.jsonVolver.value;
				jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
				var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
				nombreFormulario = jSonVolverObject.nombreformulario;
				if(nombreFormulario != ''){
					parent.document.forms[nombreFormulario].idRemesa.value =  jSonVolverObject.idremesa;
					parent.document.forms[nombreFormulario].idinstitucion.value = jSonVolverObject.idinstitucion;
					parent.document.forms[nombreFormulario].modo.value="editar";
					parent.document.forms[nombreFormulario].target = "mainWorkArea";
					parent.document.forms[nombreFormulario].submit();
					
				}
			}else{
			
				document.forms[0].idInstitucion.value = "<%=usr.getLocation()%>";
				document.forms[0].action="./JGR_EJG.do";	
				document.forms[0].modo.value="buscar";
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit(); 
			}
		}
		
		function accionNuevo() {
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		
		function buscar() {
				document.forms[0].modo.value = "abrir";
				document.forms[0].submit();
		}
		function enviar(fila)
		{
			var idOculto= 'oculto'+fila+'_1';
			var idEstadoPorEJG = document.getElementById(idOculto).value;
			document.forms[0].idEstadoPorEJG.value = idEstadoPorEJG;
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value = "enviarPericles";
			document.forms[0].submit();
		    
		}
	</script>
</body>	
</html>	