<!DOCTYPE html>
<html>
<head>
<!-- listadoTelefonos.jsp -->
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String accion = (String)ses.getAttribute("accion");
	String idPersona = "";
	try {					
		idPersona = (String)((Hashtable)request.getSession().getAttribute("DATOSSOJ")).get("IDPERSONA");
	}	
	catch (Exception e) {};
	
	Vector obj = (Vector) ses.getAttribute("resultadoTelefonos");	
	String idPers = (String) request.getParameter("idPers");
	if ((obj==null)||(obj.size()==0)){	
		try{			
			ScsTelefonosPersonaJGAdm telefonosAdm =  new ScsTelefonosPersonaJGAdm(usr);
			String condicion = " SELECT * FROM SCS_TELEFONOSPERSONA WHERE idpersona = "+idPers+" AND idinstitucion = "+ usr.getLocation()+ " ";			
			obj = (Vector)telefonosAdm.selectGenerico(condicion);
		}catch(Exception e){}
	}
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


		
	<script type="text/javascript">	
		function refrescarLocal(){
			buscar();
		}		
	</script>
	
</head>

<body class="tablaCentralCampos">
	
	<!-- CAMPOS DEL REGISTRO -->
	<html:form action="/JGR_PestanaSOJBeneficiarios" method="POST" target="submitArea" style="display:none">	
	<table align="center" width="400">	
	<html:hidden property = "modo" value = ""/>
	
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
	
	<siga:Table 		   
		   name="listadoTelefonos"
		   border="2"
		   columnNames="gratuita.operarDatosBeneficiario.literal.telefonoUso, gratuita.operarDatosBeneficiario.literal.numeroTelefono,"
		   columnSizes="40,35,25">  	    
  			<%
	    	int recordNumber=1;
	    	ScsTelefonosPersonaJGAdm adm = new ScsTelefonosPersonaJGAdm(usr);
	    	Hashtable miHash = new Hashtable();
			while (recordNumber-1 < obj.size()) {
				miHash = (Hashtable) obj.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="B" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit" modo="<%=accion%>">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION)%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA)%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDTELEFONO)%>"><%=miHash.get(ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO)%></td>
				<td><%=miHash.get(ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO)%></td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}
			%>
	</siga:Table>	
	

	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		
		function buscar() 
		{		
			document.forms[0].target = "_self";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}

		
		//Asociada al boton Guardar -->
		function accionNuevo()	{
			<%if ((idPersona != "") && (idPersona != null)) {%>
				document.forms[0].modo.value = "nuevo";
				var resultado=ventaModalGeneral(document.forms[0].name,"P");
				if(resultado=='MODIFICADO') buscar();
			<%} else {%>
				alert('<siga:Idioma key="gratuita.insertarTelefono.literal.errorIntroducirDatosPrevios"/>');
			<%}%>
		}			
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<%if (!accion.equalsIgnoreCase("Ver")) {%>
		<div style="position:absolute;top:85;z-index:3;left:300">		
			<input type="button" class="button" value="Nuevo Teléfono" onClick="accionNuevo()">
		</div>
	<%}%>
	
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
