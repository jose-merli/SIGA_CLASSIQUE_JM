<!-- busquedaSeguimientoResultados.jsp -->

<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.expedientes.form.ExpSeguimientoForm"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Vector vDatos = (Vector)request.getAttribute("datos");
	String botones = "";
	ExpSeguimientoForm form = (ExpSeguimientoForm)request.getAttribute("ExpSeguimientoForm");
	Boolean editable = form.getEditable();
%>	

<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript">
			function refrescarLocal() {	
				parent.refrescarLocal();
			}
		</script>				
	</head>

	<body>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
			 
		<html:form action="/EXP_Auditoria_Seguimiento.do?noReset=true" method="POST" target="submitArea"  style="display:none">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "idInstitucion" />
			<html:hidden property = "idInstitucionTipoExpediente" />
			<html:hidden property = "idTipoExpediente" />
			<html:hidden property = "numeroExpediente" />
			<html:hidden property = "anioExpediente" />
			<html:hidden property = "editable" />
		</html:form>
		
		<siga:Table 
	   	 	name="tablaDatos"
	   		border="1"
	   		columnNames="expedientes.auditoria.literal.fechaAnotacion,
				expedientes.auditoria.literal.usuario,
				expedientes.auditoria.literal.fase,
				expedientes.auditoria.literal.fechaInicioEstado,
				expedientes.auditoria.literal.estado,
				expedientes.auditoria.literal.automatico,
				expedientes.auditoria.literal.descripcion,"
   		  	columnSizes="8,17,8,8,8,18,23,10"
   		  	modal="m">
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
			<%if (vDatos==null || vDatos.size()==0)	{%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				
			<%} else {
		 		for (int i=0; i<vDatos.size(); i++) {
				  	Row fila = (Row)vDatos.elementAt(i);
				  	if (editable.booleanValue() && fila.getString("IDUSUARIO").equals(userBean.getUserName()) && fila.getString("IDINSTITUCION_USUARIO").equals(userBean.getLocation())){
				  		String tipoAnotacion = fila.getString("IDTIPOANOTACION");
				  		if (!tipoAnotacion.equals(ExpTiposAnotacionesAdm.codigoTipoComunicacion.toString())){
					  		botones="C,E,B";					  		
					  	} else {
					  		botones="C,E";
					  	}
				  						  		
					} else {
					  	if (editable.booleanValue()) {
					  		botones="C,E";
					  	} else {
					  		botones="C";
					  	}
					}
					String sAutomatico="";
					if (fila.getValue("AUTOMATICO")!=null) {
						sAutomatico=(fila.getString("AUTOMATICO").trim().equals("S"))?UtilidadesString.getMensajeIdioma(userBean,"messages.si"):UtilidadesString.getMensajeIdioma(userBean,"general.no");
					}
			%>
	  				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
						<td>
							<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDINSTITUCION")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDINSTITUCION_TIPOEXPEDIENTE")%>">						
							<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("IDTIPOEXPEDIENTE")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString("NUMEROEXPEDIENTE")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=fila.getString("ANIOEXPEDIENTE")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=fila.getString("IDANOTACION")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("FASE"))%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("ESTADO"))%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_9" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("USUARIO"))%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("TIPO"))%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=fila.getString("IDFASE")%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_12" value="<%=fila.getString("IDESTADO")%>">
							<%=GstDate.getFormatedDateShort("",fila.getString("FECHAANOTACION"))%>
						</td>
						<td><%=fila.getString("USUARIO").equals("")?"&nbsp;":UtilidadesString.mostrarDatoJSP(fila.getString("USUARIO"))%></td>
						<td><%=fila.getString("FASE").equals("")?"&nbsp;":fila.getString("FASE")%></td>
						<td><%=(fila.getString("FECHAINICIOESTADO")==null || fila.getString("FECHAINICIOESTADO").equals(""))?"&nbsp;":GstDate.getFormatedDateShort("",fila.getString("FECHAINICIOESTADO"))%></td>
						<td><%=fila.getString("ESTADO").equals("")?"&nbsp;":fila.getString("ESTADO")%></td>
						<td align="center"><%=sAutomatico%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila.getRow(), "DESCRIPCION"),100)%></td>
					</siga:FilaConIconos>
			<%	}
			}%>
			
		<!-- FIN: ZONA DE REGISTROS -->
		</siga:Table>
		<!-- FIN: LISTA DE VALORES -->
		
		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>
		
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
	</body>
</html>