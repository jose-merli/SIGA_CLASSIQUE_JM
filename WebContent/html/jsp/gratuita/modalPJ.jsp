<!-- modalPJ.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	String idpartido = request.getAttribute("idPartido") == null?"":((String) request.getAttribute("idPartido"));
	String idpoblacion = request.getAttribute("idPoblacion") == null?"":((String) request.getAttribute("idPoblacion"));
	String idprovincia = request.getAttribute("idProvincia") == null?"":((String) request.getAttribute("idProvincia"));
	String poblacion = request.getAttribute("poblacion") == null?"":((String) request.getAttribute("poblacion"));
	String dato[] = {idprovincia};
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.busquedaPJ.literal.titulo"/>
	</td>
</tr>
</table>
	
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_MantenimientoPartidosJudiciales.do" method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "modificar"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idPartido" value = "<%=idpartido%>"/>
		<html:hidden property = "idProvincia" value = "<%=idprovincia%>"/>
		<html:hidden property = "idPoblacion" value = "<%=idpoblacion%>"/>
		<html:hidden property = "poblacion" value = "<%=poblacion%>"/>
		<html:hidden property = "idPartidoSeleccionado" value = ""/>								
		<html:hidden property = "cambiar" value = "modal"/>										

	<tr>
	<td>
		<br>
	</td>
	</tr>
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaPJ.literal.partido">
		<br>
		<table class="tablaCampos" align="center">
		<tr>
			<td>
				<br>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaPJ.literal.partido"/>
			</td>
			<td>
				<br>
			</td>
			<td>
				<!--Combo de Partidos Judiciales de una provincia y de los que no estan asignados a ninguna provincia.-->		
				<siga:ComboBD nombre="combo_partidos" tipo="partidosProvincia" estilo="true" clase="boxCombo" obligatorio="false" parametro="<%=dato%>"/>
			</td>
			<td>
				<br>
			</td>
		</tr>
		</table>
		<br>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
	<td>
		<br>
		<br>
		<br>
	</td>
	</tr>
	</table>

	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
		<siga:ConjBotonesAccion botones="Y,C" modal="P"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			var indice = document.forms[0].combo_partidos.selectedIndex;
			if (indice == 0)
				confirm('<siga:Idioma key="gratuita.modalPJ.literal.introducir"/>');
			else {
				document.forms[0].idPartidoSeleccionado.value = document.forms[0].combo_partidos.options[indice].value;
				document.forms[0].modo.value = "modificar";
				document.forms[0].cambiar.value = "modal";
				document.forms[0].target = "submitArea";							
				document.forms[0].submit();	
				window.returnValue="MODIFICADO";				
			}			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>