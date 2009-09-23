<!-- resultBusqAlerta.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 03-02-2005 Versión inicial
-->

<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.expedientes.ExpPermisosTiposExpedientes"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	Vector vDatos = (Vector)request.getAttribute("datos");	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	request.removeAttribute("datos");
	String idInstitucion = userBean.getLocation();
	String botones = "";
	ExpPermisosTiposExpedientes perm=(ExpPermisosTiposExpedientes)request.getAttribute("permisos");

%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.alertas.cabecera" 
			localizacion="expedientes.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/EXP_Consultas.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>


			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.auditoria.literal.fecha,
		   		  	expedientes.auditoria.literal.tipo,
		   		  	expedientes.auditoria.literal.fase,
		   		  	expedientes.auditoria.literal.estado,
		   		  	expedientes.auditoria.literal.nexpediente,
		   		  	expedientes.auditoria.literal.alerta,"
		   		  tamanoCol="13,15,12,12,8,25,10"
		   		  alto="358" 
		   		  activarFilaSel="true">
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row)vDatos.elementAt(i);	
						if (fila.getString("IDINSTITUCION").equals(idInstitucion)){	
				  			botones="C,E,B";
				  		}else{
				  			botones="C,E";
				  		}
				  		botones=perm.getBotones(fila.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila.getString("IDTIPOEXPEDIENTE"),botones);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDINSTITUCION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDINSTITUCION_TIPOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("IDTIPOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString("NUMEROEXPEDIENTE")%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=fila.getString("ANIOEXPEDIENTE")%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRETIPOEXPEDIENTE"))%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=fila.getString("IDALERTA")%>">	
											
					<td><%=fila.getString("FECHAALERTA")%></td>
					<td><%=fila.getString("NOMBRETIPOEXPEDIENTE")%></td>
					<td><%=fila.getString("FAS_NOMBRE")%></td>
					<td><%=fila.getString("EST_NOMBRE")%></td>
					<td><%=fila.getString("NUMEROEXPEDIENTE")%>/<%=fila.getString("ANIOEXPEDIENTE")%></td>
					<td><%=fila.getString("TEXTO")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			

		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		
		function refrescarLocal()
		{			
			parent.buscar() ;			
		}
		

	</script>
	
		
	</body>
</html>