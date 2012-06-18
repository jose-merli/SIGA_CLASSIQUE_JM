<!-- abrirTipoExpediente.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.general.CenVisibilidad"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	request.removeAttribute("datos");

%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<siga:Titulo titulo="expedientes.tiposexpedientes.literal.titulo" localizacion="expedientes.tiposexpedientes.literal.localizacion"/>
		
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				TipoExpedienteForm.modo.value="buscar";
				TipoExpedienteForm.submit();
			}
	

		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				document.forms[0].modo.value = "nuevo";
   				var resultado=ventaModalGeneral(document.forms[0].name,"P");
   				
   				if(resultado!=null&&resultado.indexOf('%')!=-1) {
   					TipoExpedienteForm.tablaDatosDinamicosD.value=resultado;
   					TipoExpedienteForm.modo.value="editar";
					TipoExpedienteForm.submit();
				}
			}	

		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>
	
	<body onload="buscar();">
		
			<html:form action="/EXP_MantenerTiposExpedientes.do" method="POST" target="submitArea" style="display:none">
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
			   		  nombreCol="expedientes.literal.tiposexpedientes,expedientes.tiposexpedientes.literal.esgeneral,"
			   		  tamanoCol="80,10,10"
		   			alto="100%"
		   			ajusteBotonera="true"	
		   			activarFilaSel="true" >
<%
			if (vDatos==null || vDatos.size()==0)
			{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
			} else {
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		ExpTipoExpedienteBean bean = (ExpTipoExpedienteBean)vDatos.elementAt(i);
				  		String botones;
				  		if (Integer.valueOf(user.getLocation()).equals(bean.getIdInstitucion())){
				  			botones="C,E,B";
				  		} else {
				  			botones="C,E";
				  		}				  		
%>
			  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
							<td>						
								<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdTipoExpediente()%>">					
								<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdInstitucion()%>">								
								<%=bean.getNombre()%>
							</td>

							<td>
<%
								if (bean.getEsGeneral().equals("S"))
								{
%>
									<siga:Idioma key="general.yes"/>
<%
								} else {
%>
									<siga:Idioma key="general.no"/>
<%
								}
%>
							</td>

						</siga:FilaConIconos>
<%
					} //for
				} // if vdatos
%>			

			</siga:TablaCabecerasFijas>

			
			
			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="N" clase="botonesDetalle"/>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		