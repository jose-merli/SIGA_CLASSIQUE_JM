<!-- busquedaEstadosResultados.jsp -->
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page
	import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	
	//Recupero el nombre del expediente
	String nombreExp = (String)request.getAttribute("nombreExp");
	
	request.removeAttribute("datos");
	String idinstitucion_tipoexpediente = (String)request.getAttribute("idInstitucion_TipoExpediente");
	String tipoExp = (String)request.getParameter("idTipoExpediente");
	String dato[] = {idinstitucion_tipoexpediente,tipoExp};
	
%>

<html>
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->

<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->


<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">
		
			<!-- Refresco -->
			<!-- esta función es llamada desde exito.jsp tras mostrar el mensaje de éxito --!
			function refrescarLocal() 
			{		
				
				parent.refrescarLocal();
			}
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				   document.forms[0].modo.value = "nuevo";
   				   var resultado=ventaModalGeneral(document.forms[0].name,"G");
   				  if(resultado=='MODIFICADO') parent.buscar();
			}
	
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				<%request.getSession().removeAttribute("nombreExp");%>
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}
			function accionBuscar() 
			{		
				var auxTarget = document.forms[0].target;
				document.forms[0].modo.value="buscar";
				document.forms[0].target="submitArea";	
				document.forms[0].submit();
				document.forms[0].target= auxTarget ;	
		
			}
		
		</script>

<!-- FIN: SCRIPTS BOTONES -->

</head>

<body class="detallePestanas" >

<html:form action="/EXP_TiposExpedientes_Estados.do" method="POST"
	target="submitArea">
	<html:hidden property="modo" value="" />
	<html:hidden property="hiddenFrame" value="1" />
	<html:hidden property="idTipoExpediente" />

</html:form>





<siga:Table 
	name="tablaDatos" 
	border="1"
	columnNames="expedientes.auditoria.literal.fase, expedientes.auditoria.literal.estado, expedientes.tiposexpedientes.literal.siguienteestado, expedientes.auditoria.literal.automatico, expedientes.tiposexpedientes.literal.mensaje,"
	columnSizes="15,15,15,5,30,10" 
	modal="g">

	<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
	<br>
	<br>
	<p class="titulitos" style="text-align: center"><siga:Idioma
		key="messages.noRecordFound" /></p>
	<br>
	<br>
	<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row)vDatos.elementAt(i);	
				  		
				  		String sAutomatico="";
					  	if (fila.getValue("AUTOMATICO")!=null) {
					  		sAutomatico=(fila.getString("AUTOMATICO").trim().equals("S"))?"<b>"+UtilidadesString.getMensajeIdioma(user,"messages.si")+"</b>":UtilidadesString.getMensajeIdioma(user,"general.no");
					  	}
				  		
%>
	<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B"
		clase="listaNonEdit">
		<input type="hidden" name="oculto<%=""+(i+1)%>_1"
			value="<%=fila.getString("IDESTADO")%>">
		<input type="hidden" name="oculto<%=""+(i+1)%>_2"
			value="<%=fila.getString("IDFASE")%>">
		<input type="hidden" name="oculto<%=""+(i+1)%>_3"
			value="<%=fila.getString("IDINSTITUCION")%>">
		<input type="hidden" name="oculto<%=""+(i+1)%>_4"
			value="<%=fila.getString("IDTIPOEXPEDIENTE")%>">

		<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("FASE"))%></td>
		<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("ESTADO"))%></td>
		<td><%=fila.getString("SIGUIENTE").equals("")?"&nbsp;":fila.getString("SIGUIENTE")%></td>
		<td align="center"><%=sAutomatico%></td>
		<td><%=fila.getString("MENSAJE").equals("")?"&nbsp;":fila.getString("MENSAJE")%></td>
	</siga:FilaConIconos>
	<%	
					}
				}
%>
</siga:Table>




<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
</body>
</html>

