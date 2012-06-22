<!--- resultadoBusquedaServicios.jsp -->
<!-- 
	 Muestra los resultados de la busqueda de servicios
	 VERSIONES:
	 miguel.villegas 4-02-2005 Creacion
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<style type="text/css">

			.especif
			{
				background-color : #<%=src.get("color.titleBar.BG")%>;
				position:absolute; width:964; height:35; z-index:2; top: 285px; left: 0px
			}

		</style>
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}			
		
		</script>

	</head>

	<body class="tablaCentralCampos">
		<html:form action="/PYS_MantenimientoServicios.do" method="post" target="">
			<html:hidden property="modo"  styleId="modo"  value=""/>		
			<input type="hidden" name="actionModal"  id="actionModal"  value="">
		</html:form>	
		
			<siga:TablaCabecerasFijas 
			   nombre="tablaDatos"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="pys.resultadoBusquedaServicios.literal.tipo,pys.resultadoBusquedaServicios.literal.categoria,
			   			  pys.resultadoBusquedaServicios.literal.concepto,pys.resultadoBusquedaServicios.literal.Estado,"
			   tamanoCol="20,20,30,20,10"
			   alto="100%"
			   modal="G"
			   activarFilaSel="true" >
			   
			<%
	    	if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
		    {
			%>
				<br><br><br>
				<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
			<%
	    	}	    
		    else
		    { %>
	    		<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
				int recordNumber=1;
				while (en.hasMoreElements())
				{
	            	Row row = (Row) en.nextElement(); %>
					<siga:FilaConIconos
						  fila='<%=String.valueOf(recordNumber)%>'
						  botones="C,E,B"
						  modo="edicion"
						  clase="listaNonEdit">
						  
						<td>
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(PysServiciosInstitucionBean.C_IDINSTITUCION)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIO)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION)%>">
						  	<!--input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString(PysServiciosInstitucionBean.C_DESCRIPCION)%>"-->
							<%=UtilidadesString.mostrarDatoJSP(row.getString("TIPO"))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString("CATEGORIA"))%>
						</td>  	
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(PysServiciosInstitucionBean.C_DESCRIPCION))%>
						</td>  	
						<td>
							<!-- DCG ini -->
							<% 
								String a = row.getString(PysServiciosInstitucionBean.C_FECHABAJA);
								if (a.trim().equals("")){	
									String auto = row.getString(PysServiciosInstitucionBean.C_AUTOMATICO);
									if (auto.trim().equals("0")) { %>
										<siga:Idioma key="pys.busquedaServicio.estado.manual"/>	
									<%}
									else { %>
										<siga:Idioma key="pys.busquedaServicio.estado.suscripcionAutomatica"/>	
									<%}
								} 
								else {%>
									<siga:Idioma key="pys.busquedaservicio.estado.baja"/>
							<%}	%>
							<!-- DCG fin -->
						</td>  																			
					</siga:FilaConIconos>
					<% recordNumber++;
				} 
			} %>
			</siga:TablaCabecerasFijas>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
</html>