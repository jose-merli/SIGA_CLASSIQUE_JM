<!-- consultaHistorico.jsp -->
<!-- 
	 Muestra los resultados de la busqueda en el historial
	 VERSIONES:
	 miguel.villegas 21-12-2004 
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// Institucion a la que pertenece el cliente
	String accion=(String)request.getSession().getAttribute("ACCION"); // Obtengo la accion anterior
	String idInstitucion=(String)request.getSession().getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion	

	// Institucion del usuario de la aplicacion
	String idInstUsuario=usr.getLocation();	 // Obtengo el identificador de la institucion
	String	botones="V,N";

%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="HistoricoForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal() {
				parent.buscar();
			}		
		
		</script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.historico.cabecera" 
			localizacion="censo.fichaCliente.historico.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->	
		
	</head>

	<body class="tablaCentralCampos">

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_Historico.do" method="POST" style="display:none" styleId="HistoricoForm">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = ""/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
			<input type="hidden" id="actionModal" name="actionModal" value="">
			<input type="hidden" id="filaSelD" name="filaSelD">	
		</html:form>
		
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		
			<siga:TablaCabecerasFijas 
		  	      nombre="tablaDatos"
				  borde="1"
		   		  clase="tableTitle"
				  nombreCol="censo.consultaHistorico.literal.tipo,censo.consultaHistorico.literal.fechaEntrada,censo.consultaHistorico.literal.fechaEfectiva,censo.consultaHistorico.literal.tipoApunte,censo.consultaHistorico.literal.motivo,"
				  tamanoCol="16,12,12,10,22,8"
				  alto="100%"
				  modal="M">


				<%
		    	 if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 ){
				%>
			 		<br><br>
			   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>	 		
				<%
	    		 }	    
		    	 else { 
		    		Enumeration en = ((Vector)request.getAttribute("container")).elements();
					int recordNumber=1;
					String botonesMostrados="C,E";	
					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); %>
	            		
						<%	
							// Evaluacion de los iconos a mostrar
							String modoFila=accion;
							if(row.getString(CenHistoricoBean.C_USUMODIFICACION).equalsIgnoreCase(Integer.toString(ClsConstants.USUMODIFICACION_AUTOMATICO))){ 
								modoFila="ver";
							}
						%>
	            		
						<siga:FilaConIconos
							  fila='<%=String.valueOf(recordNumber)%>'
							  botones='<%=botonesMostrados%>'
							  modo='<%=modoFila%>'
							  visibleBorrado='no'
							  clase="listaNonEdit">
							<td>
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenHistoricoBean.C_IDPERSONA)%>">
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenHistoricoBean.C_IDINSTITUCION)%>">
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenHistoricoBean.C_IDHISTORICO)%>">
								<%=UtilidadesString.mostrarDatoJSP(row.getString(CenTipoCambioBean.C_DESCRIPCION))%>		
							</td>
							<td>
								<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString(CenHistoricoBean.C_FECHAENTRADA)))%>
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString(CenHistoricoBean.C_FECHAEFECTIVA)))%>
							</td>  	
							<td>
								<%if (row.getString(CenHistoricoBean.C_USUMODIFICACION).equalsIgnoreCase(Integer.toString(ClsConstants.USUMODIFICACION_AUTOMATICO))){%>
									<siga:Idioma key="censo.tipoApunteHistorico.automatico"/>
								<% }else{ %>
									<siga:Idioma key="censo.tipoApunteHistorico.manual"/>
								<% } %>								
							</td>  								
							<td>							
								<%=UtilidadesString.mostrarDatoJSP(row.getString(CenHistoricoBean.C_MOTIVO))%>
							</td>  	
														
						</siga:FilaConIconos>
						<% recordNumber++;
					} 
				 } %>		
			</siga:TablaCabecerasFijas>
	

	 <!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->


	</body>
</html>
