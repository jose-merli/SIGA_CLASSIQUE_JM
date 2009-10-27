<!-- resolucion.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
-->

<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.*,java.util.*"%>
<%@ page import="com.siga.expedientes.form.ExpResolucionForm"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	String editable = (String)request.getParameter("editable");		
	String soloSeguimiento = (String)request.getParameter("soloSeguimiento");	
	boolean bEditable=false;
	if (soloSeguimiento.equals("true")){
		bEditable=false;
	}else{
		bEditable = editable.equals("1")? true : false;
	}
	
	//String bEditable = (String)request.getParameter("editable");
	//boolean editable = bEditable.equals("1")? true : false;
	String boxStyle = bEditable? "box" : "boxConsulta";
	String comboStyle = bEditable? "boxCombo" : "boxComboConsulta";
	String soloLectura="true";
	if (bEditable){
		soloLectura="false";
	}
	String denunciado = (String)request.getAttribute("denunciado");
	String nombreTipoExpediente = (String)request.getParameter("nombreTipoExpediente");
	String numExpediente = (String)request.getParameter("numeroExpediente");
	String anioExpediente = (String)request.getParameter("anioExpediente");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");	
	
	String mostrarResultadoInforme = (String)request.getAttribute("mostrarResultadoInforme");
	ArrayList resultadoInformeSel = new ArrayList();
	String resultadoInforme = (String)request.getAttribute("resultadoInforme");
	if (resultadoInforme != null)
	{
		resultadoInformeSel.add(resultadoInforme);
	}
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
    String[] dato = {usr.getLocation()};
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.resolucion.cabecera" 
			localizacion="expedientes.auditoria.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
			<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
		<%--
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				   document.forms[0].modo.value = "nuevo";
   				   var resultado=ventaModalGeneral(document.forms[0].name,"P");
   				  if(resultado=='MODIFICADO') parent.buscar();
			}
		--%>
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				<% if (busquedaVolver.equals("AB")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% } else if (busquedaVolver.equals("NB")){ %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } else if(busquedaVolver.equals("AV")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% }  else if (busquedaVolver.equals("Al")){%>
					document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } %>
					document.forms[1].submit();				
			}
	
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				//var elemento=parent.document.getElementById('pestana.auditoriaexp.resolucion');
				//parent.pulsar(elemento,'mainPestanas') 
				document.forms[0].reset();
				
			}
			
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{		
				document.forms[0].modo.value="modificar";
				document.forms[0].target="submitArea";	
				document.forms[0].submit();	
			}
			
						
			function refrescarLocal()
			{			
				document.location.reload();
			
			}

		</script>
		
		<!-- FIN: SCRIPTS BOTONES -->
		
	
</head>

<body class="detallePestanas">

	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<% ExpResolucionForm f = (ExpResolucionForm)request.getAttribute("expResolucionForm"); %>
				<%=f.getTituloVentana()%>
			</td>
		</tr>
	</table>
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<div id="camposRegistro">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/EXP_Auditoria_Resolucion.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "hiddenFrame" value = "1"/>
	
	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.resolucion">

	<table class="tablaCampos" align="center" border="0">

	<!-- FILA -->
		<tr>		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.descripcion"/>
			</td>
			<td colspan="3">
			
				<html:textarea cols="10" rows="4" property="resDescripcion" 
					onkeydown="cuenta(this,4000)" onchange="cuenta(this,4000)" 
					styleClass="<%=boxStyle%>" style="width:690px;" readonly="<%=!bEditable%>"></html:textarea> 
			</td>				
		</tr>
	
		<tr>		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.sancionado"/>
			</td>				
			<td>				
				<html:checkbox name="expResolucionForm" property="sancionado" disabled="<%=!bEditable%>"></html:checkbox>
			</td>			
			<td colspan="2"></td>
		</tr>	
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.sancionprescrita"/>
			</td>			
			<td>
				<html:text name="expResolucionForm" property="sancionPrescrita" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
				</html:text>
				<% if (bEditable){%>
				<a href='javascript://'onClick="return showCalendarGeneral(sancionPrescrita);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>
			<td class="labelText" style="text-align: right">
				<siga:Idioma key="expedientes.auditoria.literal.sancionfinalizada"/>
			</td>
			<td>
				&nbsp&nbsp&nbsp&nbsp&nbsp
				<html:text name="expResolucionForm" property="sancionFinalizada" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
				</html:text>
				<% if (bEditable){%>
				<a href='javascript://'onClick="return showCalendarGeneral(sancionFinalizada);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>				
		</tr>
		
		<tr>		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.actuacionesprescritas"/>
			</td>				
			<td>
				<html:text name="expResolucionForm" property="actuacionesPrescritas" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
				</html:text>
				<% if (bEditable){%>
				<a href='javascript://'onClick="return showCalendarGeneral(actuacionesPrescritas);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>			
		
			<td class="labelText" style="text-align: right">
				<siga:Idioma key="expedientes.auditoria.literal.anotcanceladas"/>
			</td>				
			<td>
				&nbsp&nbsp&nbsp&nbsp&nbsp
				<html:text name="expResolucionForm" property="anotacionesCanceladas" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true">
				</html:text>
				<% if (bEditable){%>
				<a href='javascript://'onClick="return showCalendarGeneral(anotacionesCanceladas);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>			
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fechaResolucion"/>
			</td>
			<td>
				<html:text name="expResolucionForm" property="fechaResolucion" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true"></html:text>
				<% if (bEditable){%>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaResolucion);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<% } %>
			</td>
			
<% if (mostrarResultadoInforme != null && mostrarResultadoInforme.equalsIgnoreCase("S")) {%>
			<td class="labelText" style="text-align: right">
				<siga:Idioma key="expedientes.auditoria.literal.resultadoInforme"/>
			</td>
			<td>
				&nbsp&nbsp&nbsp&nbsp&nbsp
				<siga:ComboBD nombre="resultadoInforme" tipo="resultadoInforme" clase="<%=comboStyle%>" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=resultadoInformeSel%>" readonly="<%=soloLectura%>"/>
				
			</td>
<% } %>
		</tr>
		<tr>		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.visible"/>
			</td>				
			<td>
				<html:checkbox name="expResolucionForm" property="visible" disabled="<%=!bEditable%>"></html:checkbox>
			</td>			
		
			<td class="labelText" style="text-align: right">
				<siga:Idioma key="expedientes.auditoria.literal.visibleenficha"/>
			</td>				
			<td>
				&nbsp&nbsp&nbsp&nbsp&nbsp
				<html:checkbox name="expResolucionForm" property="visibleEnFicha" disabled="<%=!bEditable%>"></html:checkbox>
			</td>
			<td colspan="2"></td>
		</tr>	
	
	</table>
		
	</siga:ConjCampos>	
	
			
	</td>
	</tr>
	</html:form>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<%	if (bEditable){%>
		<siga:ConjBotonesAccion botones="V,R,G" clase="botonesDetalle" />
	<% } else{%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } %>	
	
</div>	

<% if (!busquedaVolver.equals("volverNo")) { %>
		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>
<% } %>	

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
