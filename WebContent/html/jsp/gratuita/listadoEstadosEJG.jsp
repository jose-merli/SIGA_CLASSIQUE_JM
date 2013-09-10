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


<!-- TAGLIBS -->
<%@taglib uri =	"struts-bean.tld" 	prefix="bean"%>
<%@taglib uri = "struts-html.tld" 	prefix="html"%>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@taglib uri = "struts-logic.tld" 	prefix="logic"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	Vector obj = (Vector) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");	
		
	String botonesPie="", botones="",anio= "", numero="", idTipoEJG = "" ;
	
	if (accion.equalsIgnoreCase("ver")){
		botonesPie = "V";
	}
	else {
		botonesPie = "V,N";
	}
	
	Hashtable fila = new Hashtable();
	
	try {		
		anio = request.getParameter("ANIO").toString();
		numero = request.getParameter("NUMERO").toString();
		idTipoEJG = request.getParameter("IDTIPOEJG").toString();
	} catch(Exception e){
		Hashtable miHash = (Hashtable)request.getAttribute("DATOSEJG");
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
	</html:form>
		
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
<%  		
				String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
				ScsEJGAdm adm = new ScsEJGAdm (usr);
					
				Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero,idTipoEJG);

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
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>
	
	<siga:Table 		   
	   name="listadoDocumentacion"
	   border="2"
	   columnNames="gratuita.operarEJG.literal.fecha,
				   pestana.justiciagratuitaejg.estados,
				   pestana.justiciagratuitaejg.observaciones,
				   pestana.justiciagratuitaejg.automatico,
				   Propietario,"
	   columnSizes="10,25,35,10,10,10"
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
					stPropietario="";
					blAutomatico=true;
				} else {
					automatico="No";
					blAutomatico=false;
				}
				
				if((usr.isComision()&&blPropietarioComision)||(!usr.isComision()&&!blPropietarioComision)){
					if (!blAutomatico) {
					 	botones="C,E,B";
					} else {
					 	botones="C";
					}
				} else {
					botones="C";
				}
%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' pintarespacio="false"  visibleConsulta="false" botones="<%=botones%>" clase="listaNonEdit" modo="<%=accion%>">
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDESTADOPOREJG")%>">
					    <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("AUTOMATICO")%>"> 
						<%=GstDate.getFormatedDateShort("",fila.get("FECHAINICIO").toString())%>&nbsp;
					</td>
					<td><%=fila.get("DESCRIPCION")%>&nbsp;</td>
					<td><%=fila.get("OBSERVACIONES")%>&nbsp;</td>
					<td><%=automatico%>&nbsp;</td>
					<td><%=stPropietario%>&nbsp; </td>
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

	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="<%=botonesPie %>" clase="botonesDetalle" modo="<%=accion%>"/>
	
	<script type="text/javascript">			
		//Asociada al boton Cerrar
		function accionVolver() {
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
		function accionNuevo() {
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		
		function buscar() {
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();
		}
	</script>
</body>	
</html>	