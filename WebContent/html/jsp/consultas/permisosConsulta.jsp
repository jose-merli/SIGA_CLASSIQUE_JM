<!DOCTYPE html>
<html>
<head>
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
<%@ taglib uri="c.tld" prefix="c"%>

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



<!-- HEAD -->
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="consultas.consultasRecuperar.permisos" 
		localizacion="consultas.consultasRecuperar.consulta.cabecera"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
	
	</head>

	<body class="detallePestanas">
	<bean:define id="listadoInformes" name="listadoInformes" scope="request"/>
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
			<html:hidden name="PermisosConsultaForm" property="nombreConsulta" value="<%=nombreConsulta %>"/>
		
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
		<tr><td colspan="3">&nbsp;</td></tr>
		</table>
			
		</html:form>
		
<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos" style="width:95%;">
			<siga:Idioma key="menu.administracion.informes"/>
		</td>
		
		<td class="tdBotones" style="vertical-align: left">
		
			<input type="button" alt="<siga:Idioma key="general.boton.new"/>"  id="idButton" onclick="return accionNuevo();" class="button" name="idButton" value="<siga:Idioma key="general.boton.new"/>">
		</td>
	</tr>
</table>		
	<table id='listadoArchivosCab' style="table-layout:fixed" border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class ='tableTitle'>
			<td align='left' width='80%'><siga:Idioma key="administracion.informes.literal.descripcion"/></td>
			<td align='center' width='10%'><siga:Idioma key="administracion.informes.literal.formato"/></td>
			<td width='10%'></td>
		</tr>
		<c:choose>
			<c:when test="${listadoInformes==null}">
			<tr>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="text-align:center" colspan = "3">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		</td>
	 			</tr>
				
   			</c:when>
   			<c:when test="${empty listadoInformes}">
   				<tr>
					<td></td>
					<td></td>
					<td></td>
					
					
				</tr>
   				
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="text-align:center" colspan = "3">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		
	   		 		</td>
	 			</tr>
   			</c:when>
   			<c:otherwise>
		
				<tr>
					<td></td>
					<td></td>
					<td></td>
					
				</tr>
				<c:forEach items="${listadoInformes}" var="informe" varStatus="status">                 
					<siga:FilaConIconos	fila='${status.count}'
		  				pintarEspacio="no"
		  				visibleConsulta="no"
		  				visibleEdicion = "no"
		  				visibleBorrado = "si"
		  				clase="listaNonEdit"
		  				id="filaInforme_${status.count}"
		  				>
		  				<input type="hidden" name="idPlantilla_${status.count}" value="${informe.idPlantilla}">
						<input type="hidden" name="idInstitucion_${status.count}" value="${informe.idInstitucion}">
						<input type="hidden" name="claseTipoInforme_${status.count}" value="${informe.claseTipoInforme}">
						
						<td align='left'><c:out value="${informe.descripcion}"></c:out></td>
						<td align='left'>
							<c:choose>
							<c:when test="${informe.tipoFormato=='W'}">
								<siga:Idioma key="administracion.informes.formato.word"/>
							</c:when>
							<c:when test="${informe.tipoFormato=='E'}">
							<siga:Idioma key="administracion.informes.formato.excel"/>
							</c:when>
							<c:otherwise>
								<c:out value="${informe.tipoFormato}"></c:out>
							</c:otherwise>
							</c:choose>
							
						 </td>
					
					</siga:FilaConIconos>
				</c:forEach>
				
				
			</c:otherwise>
	</c:choose>

</table>

		
		
		
		
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
			if(parent.document.getElementById("accionAnterior")&&parent.document.getElementById("accionAnterior").value!=""){

				document.forms[1].accionAnterior.value=parent.document.getElementById("accionAnterior").value;
				document.forms[1].idModulo.value=parent.document.getElementById("idModulo").value;
				document.forms[1].modo.value="inicio";
			}else{
				document.forms[1].modo.value="abrir";
			}
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
		function accionNuevo() 
		{		
			document.InformeForm.modo.value = "nuevoInformeConsulta";
			document.InformeForm.alias.value = PermisosConsultaForm.nombreConsulta.value;
			document.InformeForm.idConsulta.value="<%=idConsulta%>";
			document.InformeForm.idInstitucionConsulta.value="<%=idInstitucion%>";
			var resultado = ventaModalGeneral(document.InformeForm.name,"G");
			refrescarLocal();
			
		}
		function consultar(fila){
			
			var idPlantillaFila = 'idPlantilla_'+fila;
			var idInstitucionFila = 'idInstitucion_'+fila;
			var claseTipoInformeFila = 'claseTipoInforme_'+fila;
			document.InformeForm.idPlantilla.value = document.getElementById(idPlantillaFila).value;
			document.InformeForm.idInstitucion.value = document.getElementById(idInstitucionFila).value;
			document.InformeForm.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
			document.InformeForm.idConsulta.value="<%=idConsulta%>";
			document.InformeForm.idInstitucionConsulta.value="<%=idInstitucion%>";
			document.InformeForm.modo.value = "consultarInformeConsulta";
			var resultado = ventaModalGeneral(document.InformeForm.name,"G");
			
		}
		function editar(fila){
			var idPlantillaFila = 'idPlantilla_'+fila;
			var idInstitucionFila = 'idInstitucion_'+fila;
			var claseTipoInformeFila = 'claseTipoInforme_'+fila;
			document.InformeForm.idPlantilla.value = document.getElementById(idPlantillaFila).value;
			document.InformeForm.idInstitucion.value = document.getElementById(idInstitucionFila).value;
			document.InformeForm.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
			document.InformeForm.idConsulta.value="<%=idConsulta%>";
			document.InformeForm.idInstitucionConsulta.value="<%=idInstitucion%>";
			document.InformeForm.modo.value = "editarInformeConsulta";
			
			var resultado = ventaModalGeneral(document.InformeForm.name,"G");
			refrescarLocal();
			 

		}
		function borrar(fila){
			if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')) { 
				
				var idPlantillaFila = 'idPlantilla_'+fila;
				var idInstitucionFila = 'idInstitucion_'+fila;
				var claseTipoInformeFila = 'claseTipoInforme_'+fila;
				document.InformeForm.idPlantilla.value = document.getElementById(idPlantillaFila).value;
				document.InformeForm.idInstitucion.value = document.getElementById(idInstitucionFila).value;
				document.InformeForm.claseTipoInforme.value = document.getElementById(claseTipoInformeFila).value;
				document.InformeForm.idConsulta.value="<%=idConsulta%>";
				document.InformeForm.idInstitucionConsulta.value="<%=idInstitucion%>";
				document.InformeForm.target = "submitArea";
				document.InformeForm.modo.value = "borrarConsultaInforme";
				document.InformeForm.submit();
					
			}
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<%if (tipoConsulta != null && tipoConsulta.equals("&tipoConsulta=listas")){%>				
		<html:form action="/CON_RecuperarConsultasDinamicas.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior"/>
			<html:hidden property = "idModulo"/>
		</html:form>
		
	<%}else{%>
		<html:form action="/CON_RecuperarConsultas.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior"/>
			<html:hidden property = "idModulo"/>
		</html:form>
	<%}%>	

		<html:form action="/ADM_GestionInformes" method="POST" target="mainWorkArea">
			<html:hidden property = "modo"/>
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "claseTipoInforme" value = "P"/>
			<html:hidden property = "idTipoInforme" value = "CON"/>
			<html:hidden property = "alias"/>
			<html:hidden property = "idPlantilla"/>
			<html:hidden property = "idInstitucion"/>
			<html:hidden property = "claseTipoInforme"/>
			<html:hidden property = "idConsulta"/>
			<html:hidden property = "idInstitucionConsulta"/>
	
			
		</html:form>
		</div>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>