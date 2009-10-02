<!-- permisosConsulta.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 11-03-200 Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
	String editable = (String)request.getParameter("editable");		
	boolean bEditable = editable.equals("1")?true:false;
	String botones = bEditable?"V,G,R":"V";
	String idConsulta = (String)request.getParameter("idConsulta");		
	String idInstitucion = (String)request.getParameter("idInstitucion");		
	String nombreConsulta = (String)request.getAttribute("nombreConsulta");
	String tipoConsulta = (String)request.getAttribute("tipoConsulta");
	tipoConsulta=!tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_GEN)?"&tipoConsulta=listas":"";
	String buscar = (String)request.getParameter("buscar");
	if (buscar!=null){
		buscar="&buscar=true";
	}else{
		buscar="";
	}
	
	Vector vGrupos = (Vector)request.getAttribute("datos");
	
	Vector vGruposNO = new Vector();
	Vector vGruposSI = new Vector();
	
	String gruposAntiguos="";
	for (int i=0; i<vGrupos.size(); i++)
	{
		Vector vAux = new Vector();
		
		
		vAux.add(((Row)vGrupos.elementAt(i)).getString("IDPERFIL"));
		vAux.add(((Row)vGrupos.elementAt(i)).getString("DESCRIPCION"));
		
		if (((Row)vGrupos.elementAt(i)).getString("ACCESO").equals("S")){
			vGruposSI.add(vAux);
			gruposAntiguos += ((Row)vGrupos.elementAt(i)).getString("IDPERFIL") + ","; 
		}else{
			vGruposNO.add(vAux);
		}
	}
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="consultas.consultasRecuperar.consulta.cabecera" 
		localizacion=""/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
	
	</head>

	<body class="detallePestanas">
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="consultas.recuperarconsulta.literal.consulta"/>: &nbsp;<%=nombreConsulta%>
				</td>
			</tr>
		</table>
	
		<div id="camposRegistro" align="center">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<table align="center">
			 
		<html:form action="/CON_PermisosConsulta.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden name="PermisosConsultaForm" property="grupos"/>
			<html:hidden name="PermisosConsultaForm" property="gruposAntiguos"/>
			<html:hidden name="PermisosConsultaForm" property="idConsulta"/>
			<html:hidden name="PermisosConsultaForm" property="idInstitucion_Consulta"/>
		
		<tr><td colspan="3">&nbsp;</td></tr>
		<tr>
			<td class="labelText" align="right"><siga:Idioma key="consultas.recuperarconsulta.literal.conacceso"/></td>
			<td>&nbsp;</td>
<%
		if (bEditable){
%>
			<td class="labelText"><siga:Idioma key="consultas.recuperarconsulta.literal.sinacceso"/></td>
<%		}%>
		</tr>
		<tr>
			<td class="labelText" align="right">
				<select size="8" class="boxCombo" id="gruposCON" multiple>
<%
			for (int i=0; i<vGruposSI.size(); i++)
			{
				String myId = (String)((Vector)vGruposSI.elementAt(i)).elementAt(0);
				String myDesc = (String)((Vector)vGruposSI.elementAt(i)).elementAt(1);
%>
					<option value="<%=myId%>"><%=myDesc%></option>
<%
			}
%>
				</select>
			</td>
			<!--<script>document.getElementById("gruposCON").style.width="200";</script>-->
			<td>
<%
		if (bEditable){
%>
				<img src="<%=app%>/html/imagenes/flecha_izquierda.gif" onClick="ponerPerfil();" style="cursor:hand;">&nbsp;&nbsp;&nbsp;<img src="<%=app%>/html/imagenes/flecha_derecha.gif" onClick="quitarPerfil();" style="cursor:hand;">
			</td>
			<td class="labelText">
				<select size="8" class="boxCombo" id="gruposSIN" multiple>
<%
			for (int j=0; j<vGruposNO.size(); j++){
				String myId = (String)((Vector)vGruposNO.elementAt(j)).elementAt(0);
				String myDesc = (String)((Vector)vGruposNO.elementAt(j)).elementAt(1);
%>
					<option value="<%=myId%>"><%=myDesc%></option>
<%
		}
%>
				</select>
			</td>
			<!--<script>document.getElementById("gruposSIN").style.width="200";</script>-->
<%
		}else{
%>
			</td>
<%
		}
%>
												
		</tr>
		</table>
			
		</html:form>
		<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function ponerPerfil()
		{
			var gruposC = document.getElementById("gruposCON");
			var gruposS = document.getElementById("gruposSIN");
			
			while (gruposS.selectedIndex!=-1)
			{
				var codigo = gruposS.options[gruposS.selectedIndex].value;
				var descripcion = gruposS.options[gruposS.selectedIndex].text;
				
				gruposC.options[gruposC.options.length] = new Option(descripcion, codigo);
				gruposS.remove(gruposS.selectedIndex);
			}
		}
		
		function quitarPerfil()
		{
			var gruposC = document.getElementById("gruposCON");
			var gruposS = document.getElementById("gruposSIN");
			
			while (gruposC.selectedIndex!=-1)
			{
				var codigo = gruposC.options[gruposC.selectedIndex].value;
				var descripcion = gruposC.options[gruposC.selectedIndex].text;
				
				gruposS.options[gruposS.options.length] = new Option(descripcion, codigo);
				gruposC.remove(gruposC.selectedIndex);
			}
		}
	
		function refrescarLocal()
		{			
			document.location.reload();			
		}


		//Asociada al boton Volver
		function accionVolver() 
		{		
			
			document.forms[1].modo.value="abrir";
			document.forms[1].action=document.forms[1].action+"<%=buscar%>"+"<%=tipoConsulta%>";
			document.forms[1].submit();				
		}
		
					
		function accionRestablecer() 
		{		
			oCON = document.getElementById("gruposCON");
			oSIN = document.getElementById("gruposSIN");
			
			var tamC = oCON.length;
			
			for (i=0; i<tamC; i++)
			{
				oCON.remove(0);
			}
				
<%
			for (int i=0; i<vGruposSI.size(); i++)
			{
%>			
				var codigo = "<%=((Vector)vGruposSI.elementAt(i)).elementAt(0)%>";
				var descripcion = "<%=((Vector)vGruposSI.elementAt(i)).elementAt(1)%>";
					
				oCON.options[oCON.options.length] = new Option(descripcion, codigo);
<%
			}
%>
	
			var tamS = oSIN.length;
			
			for (i=0; i<tamS; i++)
			{
				oSIN.remove(0);
			}
	
<%
			for (int i=0; i<vGruposNO.size(); i++)
			{
%>			
				var codigo = "<%=((Vector)vGruposNO.elementAt(i)).elementAt(0)%>";
				var descripcion = "<%=((Vector)vGruposNO.elementAt(i)).elementAt(1)%>";
					
				oSIN.options[oSIN.options.length] = new Option(descripcion, codigo);
<%
			}
%>
		}
		
		//Asociada al boton Guardar
		function accionGuardar()  {
			sub();
			PermisosConsultaForm.grupos.value="";
			PermisosConsultaForm.modo.value="modificar";
			PermisosConsultaForm.idConsulta.value="<%=idConsulta%>";
			PermisosConsultaForm.idInstitucion_Consulta.value="<%=idInstitucion%>";
			
			var gruposC = document.getElementById("gruposCON");
			
			for (i=0; i<gruposC.length; i++) {
				document.PermisosConsultaForm.grupos.value += gruposC.options[i].value + ",";
			}

			document.PermisosConsultaForm.gruposAntiguos.value="<%=gruposAntiguos%>";
			document.PermisosConsultaForm.submit();
		}
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<html:form action="/CON_RecuperarConsultas.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
		</html:form>
	
		</div>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>