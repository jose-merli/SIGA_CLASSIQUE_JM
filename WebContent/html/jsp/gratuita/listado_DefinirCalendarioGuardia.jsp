<!DOCTYPE html>
<html>
<head>
<!-- listado_DefinirCalendarioGuardia.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="java.io.File" %>
<%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties" %>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences" %>
<%@ page import="com.siga.gratuita.action.DefinirCalendarioGuardiaAction"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();
	
	String modopestanha = request.getSession().getAttribute("modo")==null?"":(String)request.getSession().getAttribute("modo");
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");	
%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		function refrescarLocal(){
			parent.refrescarLocal();
		}
	</script>
</head>

<body  class="tablaCentralCampos">

    <table width="100%" cellspacing="0"><tr class="titulitosDatos">
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.turno"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBRETURNO") %>
      </td>
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.guardia"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBREGUARDIA") %>
      </td>
    </tr></table>
    
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="resultado" style="display:none">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "accion" value = ""/>
	<html:hidden property = "modoPestanha" value = "<%=modopestanha%>"/>
			<!-- RGG: cambio a formularios ligeros -->
		</html:form>	
		

		<siga:Table 		   
			   	name="tablaDatos"
			   	border="1"
			   	columnNames="gratuita.listado_DefinirCalendarioGuardia.literal.fechaInicio,
			   			   gratuita.listado_DefinirCalendarioGuardia.literal.fechaFin,
			   			   gratuita.listado_DefinirCalendarioGuardia.literal.generado,
			   			   gratuita.listado_DefinirCalendarioGuardia.literal.observaciones,"
			   	columnSizes="15,15,10,40,12"
			   	modal="G">
		
	<!-- INICIO: RESULTADO -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
				<%
				int recordNumber=1;
				String fechaInicio="", fechaFin="", observaciones="", idcalendarioguardias="", idturno="", idguardia="",numGuardias ="",generado="",idinstitucion="";
				ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String sFicheroLog;
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDCALENDARIOGUARDIAS
				2- IDTURNO
				3- IDGUARDIA
				4- IDINSTITUCION
			-->
			<!-- Campos visibles por cada fila:
				1- FECHAINICIO=FECHADESDE para mi formulario
				2- FECHAFIN=FECHAHASTA para mi formulario
				3- OBSERVACIONES
			-->
			<%
				FilaExtElement[] elems=new FilaExtElement[1];
				fechaInicio = ((String)hash.get("FECHAINICIO")).equals("")?"&nbsp;":(String)hash.get("FECHAINICIO");
				fechaFin = ((String)hash.get("FECHAFIN")).equals("")?"&nbsp;":(String)hash.get("FECHAFIN");
				observaciones = ((String)hash.get("OBSERVACIONES")).equals("")?"&nbsp;":(String)hash.get("OBSERVACIONES");
				idcalendarioguardias = ((String)hash.get("IDCALENDARIOGUARDIAS")).equals("")?"&nbsp;":(String)hash.get("IDCALENDARIOGUARDIAS");
				idturno = ((String)hash.get("IDTURNO")).equals("")?"&nbsp;":(String)hash.get("IDTURNO");
				idguardia = ((String)hash.get("IDGUARDIA")).equals("")?"&nbsp;":(String)hash.get("IDGUARDIA");
				idinstitucion = ((String)hash.get("IDINSTITUCION")).equals("")?"&nbsp;":(String)hash.get("IDINSTITUCION");
				numGuardias = DefinirCalendarioGuardiaAction.getNumGuardias(idinstitucion,idturno,idguardia,idcalendarioguardias,usr);
				sFicheroLog = rp.returnProperty("sjcs.directorioFisicoGeneracionCalendarios")
					+ File.separator + idinstitucion+File.separator+ idturno + "." + idguardia + "." + idcalendarioguardias + "-"
					+ GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio).replace('/', '.') 
					+ "-" + GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin).replace('/', '.') + ".log.xls";
				
				File fichero = new File(sFicheroLog);
				if(fichero!=null && fichero.exists()){
					//Boton de descarga del envio:
					elems[0]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
				} else {
					elems[0] = null;
				}
				
				boolean tieneGuardias = numGuardias!=null && !numGuardias.equalsIgnoreCase("0");
				if (tieneGuardias)generado="Si";else generado="No";
			%>
		       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="E,C,B" elementos="<%=elems%>"  clase="listaNonEdit" modo="<%=modopestanha%>"  >
				<td align="center">
				<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_1' id='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idcalendarioguardias%>' />
				<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_2' id='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idturno%>' />
				<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_3' id='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idguardia%>' />
				<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_4' id='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idinstitucion%>' />
				<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%></td>
				<td align="center"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%></td>
				<td align="center"><%=generado%></td>
				<td align="center"><%=observaciones%></td>
			</siga:FilaConIconos>
				<% 		recordNumber++; %>
				<% } %>
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<% } %>
	</siga:Table>

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<script language="JavaScript">
	
		function descargaLog(fila)
			{

			   var datos;
			   datos = document.getElementById('tablaDatosDinamicosD');
			   datos.value = ""; 
			   var i, j;
			   for (i = 0; i < 5; i++) {
			      var tabla;
			      tabla = document.getElementById('tablaDatos');
			      if (i == 0) {
			        var flag = true;
			        j = 1;
			        while (flag) {
			          var aux = 'oculto' + fila + '_' + j;
			          var oculto = document.getElementById(aux);
			          if (oculto == null)  { flag = false; }
			          else { datos.value = datos.value + oculto.value + ','; }
			          j++;
			        }
			        datos.value = datos.value + "%"
			      }
			   }
				   
				document.forms[0].target="submitArea";
				document.forms[0].modo.value="descargarLog";
				document.forms[0].submit();
			}

	</script>
	
</body>	
</html>