<!DOCTYPE html>
<html>
<head>
<!-- datosGruposFijos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld"  	prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.siga.censo.form.MantenimientoGruposFijosForm"%>
<%@page import="org.redabogacia.sigaservices.app.vo.cen.CenGruposFicherosVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	
	String estilo="box";
	boolean desactivado = true;
	String accion = "";
	String modo = (String)request.getAttribute("modo");	
	String parametro[] = new String[1];
	String botones="";
	
	// Formulario
	MantenimientoGruposFijosForm formulario = (MantenimientoGruposFijosForm) request.getAttribute("MantenimientoGruposFijosForm");
	if (modo.equalsIgnoreCase("EDITAR")) {
		desactivado  = false;
		estilo = "box";
		accion = "modificar";
		botones="V,G";
	} else {
		if (modo.equalsIgnoreCase("NUEVO")) {
				desactivado = false;
				accion = "insertar";
			    botones="V,G";
		} else { //MODO=VER
				desactivado  = true;
				estilo = "boxConsulta";
				accion = "ver";
				botones="V";
		}
	}

%>



<!-- HEAD -->

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoGruposFijosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<siga:Titulo titulo="censo.datosGruposFijos.datosgenerales" localizacion="censo.GruposFijos.localizacion"/>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function accionVolver(){ 
			sub();
			document.MantenimientoGruposFijosForm.target="mainWorkArea";
			document.MantenimientoGruposFijosForm.modo.value = "abrirAlvolver";
			document.MantenimientoGruposFijosForm.submit();
			fin();
		}	
		
		function refrescarLocal(){
			sub();
			document.MantenimientoGruposFijosForm.target="mainWorkArea";
			document.MantenimientoGruposFijosForm.modo.value = "editar";
			document.MantenimientoGruposFijosForm.submit();
			fin();
			
		}

		function accionGuardar(){
			sub();
			if (validateMantenimientoGruposFijosForm(document.getElementById("MantenimientoGruposFijosForm"))) {
				document.getElementById("MantenimientoGruposFijosForm").modo.value = "<%=accion%>";
				document.getElementById("MantenimientoGruposFijosForm").submit();
			} else {
				fin();
				return false;
			}
		}		
		
		function accionProcesar() {
			if(document.forms[0].fichero.value != "") {
				sub();
				var mensaje = "<siga:Idioma key='censo.mantenimientoGruposFijos.confirmarCarga'/> ";
				if (confirm(mensaje)){
					document.forms[0].modo.value="procesarFichero";	
					var alerta = "<siga:Idioma key='censo.mantenimientoGruposFijos.procesandoFichero'/> ";
					alert(alerta);
					document.forms[0].submit();	
				} else{
					fin();
				}	
				
			}else{
				var mensaje = "<siga:Idioma key='censo.mantenimientoGruposFijos.seleccionarFichero'/>";
				alert(mensaje);
			}
		}
		
		function accionDescargarPlant(){
			sub();
			document.MantenimientoGruposFijosForm.modo.value = "generarPlantilla";
			document.MantenimientoGruposFijosForm.submit();
			fin();
		}
		
		function download(fila)
		{
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
  			var aux1 = 'oculto' + fila + '_1';
   			var oculto1 = document.getElementById(aux1);
  			var aux2 = 'oculto' + fila + '_2';
   			var oculto2 = document.getElementById(aux2);
   			sub();
   			document.MantenimientoGruposFijosForm.directorio.value = oculto1.value;
   			document.MantenimientoGruposFijosForm.nombrefichero.value = oculto2.value;
   			document.MantenimientoGruposFijosForm.modo.value = "download";
   			document.MantenimientoGruposFijosForm.submit();
			fin();
			
		}
		
		function descargaLog(fila)
		{
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
  			var aux1 = 'oculto' + fila + '_1';
   			var oculto1 = document.getElementById(aux1);
  			var aux2 = 'oculto' + fila + '_3';
   			var oculto2 = document.getElementById(aux2);
   			sub();
   			document.forms[0].directorio.value = oculto1.value;
   			document.forms[0].nombrefichero.value = oculto2.value;
   			document.forms[0].modo.value = "download";
   			document.forms[0].submit();
   			fin();
		}
		
	</script>	
</head>

<body class="tablaCentralCampos" >
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/CEN_MantenimientoGruposFijos.do" method="POST" target="submitArea" styleId="MantenimientoGruposFijosForm" enctype="multipart/form-data">
		<html:hidden property = "modo" />
		<html:hidden property = "directorio" />
		<html:hidden property = "nombrefichero" />
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.datosGruposFijos.literal.GruposFijos">
						<table class="tablaCampos" border="0" width="100%">
							<tr>
								<td class="labelText"><siga:Idioma
										key="gratuita.mantenimientoTablasMaestra.literal.nombre" />&nbsp;(*)
								</td>
								<td><html:text name="MantenimientoGruposFijosForm"
										property="nombre" size="80" maxlength="100"
										readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<%if (!modo.equalsIgnoreCase("NUEVO")) { %>
							<tr>
								<td class="labelText"><siga:Idioma key="censo.gestion.grupos.literal.identificador" /></td>
								<td><html:text name="MantenimientoGruposFijosForm" property="idGrupo" size="80" maxlength="100" readonly="true"	styleClass="boxConsulta"></html:text></td>
							</tr>
							<%}%>
						</table>
					</siga:ConjCampos>
						
					<%if (!modo.equalsIgnoreCase("NUEVO")&&(!modo.equalsIgnoreCase("VER"))) { %>
						<siga:ConjCampos leyenda="censo.gestion.grupos.literal.importar">
							<table class="tablaCampos" border="0" width="100%">
								<tr>
									<td class="labelText">
										<siga:Idioma key="administracion.informes.literal.archivo" />&nbsp;
									</td>
									<td>
										<html:file property="fichero" styleClass="boxCombo" style="width:400px;" />
									</td>
									<td class="tdBotones">
										<input  type="button" alt="<siga:Idioma key="general.boton.procesaFichero" />"  onclick="return accionProcesar();"
											class="button" name="idButton" value="<siga:Idioma key="general.boton.procesaFichero" />"></input>
									</td>
									<td class="tdBotones">
										<input type="button" alt='<siga:Idioma key="general.boton.descargaFicheroModelo" />'  onClick="accionDescargarPlant()" class="button" name="idButtonDescPlant" value="<siga:Idioma key="general.boton.descargaFicheroModelo" />"/>
									</td>
								</tr>							
							</table>
						</siga:ConjCampos>
					<%}%>
				</td>
			</tr>
		</table>
		
		<c:if test="${ficherosRel.size()>0}">
			<siga:ConjCampos leyenda="censo.datosGruposFijos.literal.ficheros">
				<div align="center" >
					<table width="100%"  border="0" ><tr><td>
			    		<siga:Table 
							name="tablaDatos"
							border="1"
							columnNames="administracion.informes.literal.archivo.fecha,administracion.informes.literal.archivo.usuario,administracion.informes.literal.archivo.nombre,"
							columnSizes="15,15,30,10"
							fixedHeight="600">
							<c:forEach items="${ficherosRel}" var="ficheros" varStatus="status">								
								<%  FilaExtElement[] elems=new FilaExtElement[2];
									elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ); 		
									elems[1]=new FilaExtElement("descargaLog","descargaLog",SIGAConstants.ACCESS_READ); %>		
								<siga:FilaConIconos fila="${status.count}"			    
						  			pintarEspacio="no"
						  			visibleBorrado="N"
						  			visibleEdicion="N"
						  			visibleConsulta="N"
						  			clase="listaNonEdit" elementos="<%=elems%>" botones="">
									<td><input type="hidden" name="oculto${status.count}_1" value="${ficheros.directorio}">
										<input type="hidden" name="oculto${status.count}_2" value="${ficheros.nombrefichero}">
										<input type="hidden" name="oculto${status.count}_3" value="${ficheros.nombreficherolog}">
									<fmt:formatDate value="${ficheros.fechamodificacion}" var="fechaFormat" type="date" pattern="dd/MM/yyyy HH:mm:ss"/><c:out value="${fechaFormat}"></c:out></td>
									<td><c:out value="${ficheros.nombreUsuario}"></c:out></td>
									<td><c:out value="${ficheros.nombrefichero}"></c:out></td>
								</siga:FilaConIconos>	
						   </c:forEach>
					  	 </siga:Table>
					 </td></tr></table>
				</div>
			</siga:ConjCampos>
		</c:if>
		</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=accion%>" clase="botonesDetalle" />



<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>