<!-- listadoTelefonosContrarios.jsp -->
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
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String accion = (String)ses.getAttribute("accion");
	if (accion==null)accion="insertar";
	String idPersona = "";
	try {					
		idPersona = (String)((Hashtable)request.getSession().getAttribute("DATABACKUP")).get("IDPERSONA");
	}	
	catch (Exception e) { idPersona = "";}
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) ses.getAttribute("resultadoTelefonos");	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.busquedaSOJ.literal.expedientesSOJ" 
		localizacion="SJCS > Maestros > Expedientes SOJ > Mantenimiento"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<style type="text/css">
		.tablaCentral {		
			width: 10;
			background-color: #<%=src.get("color.tableTitle.BG")%>;
		}
		
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
			buscar();
		}		
	</script>
	
</head>

<body class="tablaCentralCampos">
	
	<!-- CAMPOS DEL REGISTRO -->
	<html:form action="/JGR_ContrariosDesignas.do" method="POST" target="submitArea" style="display:none">	
	<table align="center" width="400">	
	<html:hidden property = "modo" value = ""/>
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
	
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoTelefonos"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.operarDatosBeneficiario.literal.telefonoUso, gratuita.operarDatosBeneficiario.literal.numeroTelefono,"
		   tamanoCol="40,40,20"
		   			alto="100%"
		   			ajusteBotonera="true"		

  	    >  	    
  			<%
	    	int recordNumber=1;
	    	ScsTelefonosPersonaJGAdm adm = new ScsTelefonosPersonaJGAdm(usr);
	    	Hashtable miHash = new Hashtable();
			if (obj!=null){
			while (recordNumber-1 < obj.size()) {
				miHash = (Hashtable) obj.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="B" clase="listaNonEdit" modo="<%=accion%>">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION)%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA)%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=miHash.get(ScsTelefonosPersonaJGBean.C_IDTELEFONO)%>"><%=miHash.get(ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO)%></td>
				<td><%=miHash.get(ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO)%></td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}}
			%>
	</siga:TablaCabecerasFijas>	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		
		function buscar() 
		{		
			document.forms[0].target = "_self";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}

		
		//Asociada al boton Guardar
		function accionNuevo()	{
			<%if ((idPersona != "") && (idPersona != null)) {%>
				document.forms[0].modo.value = "nuevoTelefono";
				var resultado=ventaModalGeneral(document.forms[0].name,"P");
				if(resultado=='MODIFICADO') buscar();
			<%} else {%>
				alert('Debe insertar primero los datos del beneficiario');
			<%}%>
		}			
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<%if ((idPersona!=null)&&(idPersona !="")&&(!accion.equalsIgnoreCase("ver"))){%>
		<div style="position:absolute;top:160;z-index:3;left:175">		
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
