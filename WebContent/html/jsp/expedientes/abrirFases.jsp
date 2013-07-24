<!DOCTYPE html>
<html>
<head>
<!-- abrirFases.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	
	//Recupero el nombre del expediente
	String nombreExp = (String)request.getAttribute("nombreExp");
	
	request.removeAttribute("datos");	
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				   document.forms[0].modo.value = "nuevo";
   				   var resultado=ventaModalGeneral(document.forms[0].name,"P");
   				  if(resultado=='MODIFICADO') parent.buscar();
			}
	
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
		</script>
		
		<!-- FIN: SCRIPTS BOTONES -->
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.tipoExpedientes.configuracion.fases.cabecera" 
			localizacion="expedientes.tipoExpedientes.configuracion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->		
	</head>
	
	<body>
	
	<%--<table class="tablaTitulo" cellspacing="0" heigth="32">--%>
	<table class="tablaTitulo" align="center" cellspacing="0">
			<html:form  action="/EXP_TiposExpedientes_Fases.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property="idTipoExpediente"/>
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="expedientes.literal.tiposexpedientes"/> :&nbsp;<%=nombreExp%> 				    
			</td>
		</tr>
		</html:form>
		
	</table>

		
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="expedientes.auditoria.literal.fase,expedientes.auditoria.literal.diasVencimiento,expedientes.auditoria.literal.diasAntelacion,"
		   		  columnSizes="60,15,15,10"
		   		  modal="p">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}

				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		ExpFasesBean bean = (ExpFasesBean)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="E,B" clase="listaNonEdit" visibleConsulta="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdFase()%>">						
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoExpediente()%>">
						
						
						
						<%=UtilidadesString.mostrarDatoJSP(bean.getNombre())%>
					</td>					
					<td align="center">
						<%=(bean.getDiasVencimiento()!=null)?bean.getDiasVencimiento().toString():"&nbsp;"%>
					</td>					
					<td align="center">
						<%=(bean.getDiasAntelacion()!=null)?bean.getDiasAntelacion().toString():"&nbsp;"%>
					</td>					
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>


			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<%	if (tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)){ %>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } else{ %>
				<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"  />
	<% } %>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		