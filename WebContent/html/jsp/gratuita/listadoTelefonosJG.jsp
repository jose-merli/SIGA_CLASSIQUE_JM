<!-- listadoTelefonosJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.gratuita.form.DefinirTelefonosJGForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Accion de la que venimos:
	DefinirTelefonosJGForm formulario = (DefinirTelefonosJGForm)request.getAttribute("DefinirTelefonosJGForm");
	String accion = formulario.getAccion();
	
	//Calculo los telefonos si la accion no es nuevo:
	Vector  vTelefonosJG = (Vector) request.getAttribute("VTELEFONOS");
	
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>


	<style type="text/css">
		
		.tdBotones {
			text-align: center;			
			padding-left: 0px;
			padding-right: 0px;
			padding-top: 0px;
			padding-bottom: 0px;
		}
		
	</style>
		
	<script type="text/javascript">	
		function refrescarLocal(){
			buscarTelefonos();
		}		
	</script>
	
</head>

<body>
	
	<!-- CAMPOS DEL REGISTRO -->
	<html:form action="/JGR_TelefonosPersonasJG" method="POST" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		
		<html:hidden property = "idPersona" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "accion" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
 <div style="position:absolute;top:0px;z-index:3;left:0px;width:330px;height:130px;" >	
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoTelefonos"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.operarDatosBeneficiario.literal.telefonoUso, gratuita.operarDatosBeneficiario.literal.numeroTelefono,"
		   tamanoCol="57,30,13"
		   	alto="100%"  
		   	ajusteBotonera="true"		
  	    >  	    
  			<%
	    	int recordNumber=1;
	    	ScsTelefonosPersonaJGAdm adm = new ScsTelefonosPersonaJGAdm(usr);
	    	Hashtable miHash = new Hashtable();
	    	while (vTelefonosJG!=null && recordNumber-1 < vTelefonosJG.size()) {
					miHash = (Hashtable) vTelefonosJG.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="B" pintarEspacio="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit" modo="<%=accion%>">
						<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDTELEFONO)%>">
								<%=miHash.get(ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO)%>
						</td>
						<td><%=miHash.get(ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO)%></td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}
			%>
	</siga:TablaCabecerasFijas>	
	
	</div>

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		function buscarTelefonos() {		
			document.forms[0].target = "_self";
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}

		
		<!-- Asociada al boton Guardar -->
		function accionNuevo()	{
		 
			<%	
			if (!accion.equalsIgnoreCase("nuevo") && formulario.getIdPersona()!=null && !formulario.getIdPersona().trim().equals("")) { %>
				document.forms[0].modo.value = "nuevo";
				var resultado=ventaModalGeneral(document.forms[0].name,"P");
				if (resultado=='MODIFICADO') 
					buscarTelefonos();
			<% } else {
				%>
				alert('<siga:Idioma key="gratuita.insertarTelefono.literal.errorIntroducirDatosPrevios"/>');
			<% } %>
		}			
		
		
	</script>
	   <!-- FIN: SCRIPTS BOTONES -->
	<% if (!accion.equalsIgnoreCase("Ver")) { %>
		<div style="position:absolute;bottom:50px;z-index:3;right:5px">		
			<input type="button" class="button" value="<siga:Idioma key="general.boton.new"/>" onClick="accionNuevo()">
		</div>
		
	<% } %>
	
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	

</body>
</html>