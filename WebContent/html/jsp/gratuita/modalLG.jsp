<!-- modalLG.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	String idlista = request.getAttribute("IDLISTA")==null?"":(String)request.getAttribute("IDLISTA");		
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

</head>

<body onLoad="ajusteAltoMain('resultado','100');">

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.busquedaLG.literal.titulo"/>
	</td>
</tr>
</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposGrande" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="submitArea">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden property = "accion" value = "modal"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idLista" value = "<%=idlista%>"/>
		<html:hidden property = "idTurno" value = ""/>
		<html:hidden property = "idGuardia" value = ""/>		
		<html:hidden property = "ordenOriginal" value = ""/>		
		<html:hidden property = "orden" value = ""/>		
		<html:hidden property = "hiddenFrame" value = "1"/>		
		
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
		<!-- SUBCONJUNTO DE DATOS -->
			<table class="tablaCampos" align="center">
				<tr>
					<td class="labelText" width="110">	
						<siga:Idioma key="gratuita.modalLG.literal.abreviatura"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirListaGuardiasForm" property="abreviatura" size="30" maxlength="30" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText" width="90">	
						<siga:Idioma key="gratuita.modalLG.literal.turno"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirListaGuardiasForm" property="turno" size="30" maxlength="100" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
				<tr>
					<td class="labelText">	
						<siga:Idioma key="gratuita.modalLG.literal.zona"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirListaGuardiasForm" property="zona" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText">	
						<siga:Idioma key="gratuita.modalLG.literal.subzona"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirListaGuardiasForm" property="subzona" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
				<tr>
					<td class="labelText">	
						<siga:Idioma key="gratuita.modalLG.literal.area"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirListaGuardiasForm" property="area" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText">	
						<siga:Idioma key="gratuita.modalLG.literal.materia"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirListaGuardiasForm" property="materia" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
		</table>
	</td>
	</tr>
	</html:form>	
	</table>
	

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesBusqueda botones="B" titulo="gratuita.modalLG.literal.aviso1" />
	<!-- FIN: BOTONES BUSQUEDA -->
	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			sub();
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		
		
	</script>    

	<div style="width:100%;position:absolute;bottom:30px;">
		<table>
		<tr>
			<td class="labelText" colspan="2">
				<siga:Idioma key="gratuita.modalLG.literal.aviso2"/>
			</td>
		</tr>
		<tr>
			<td class="labelText" width="740">
				<siga:Idioma key="gratuita.modalLG.literal.aviso3"/>&nbsp;
				<input type="text" id="ordenAux" name="ordenAux" size="30" class="box" maxlength="100" value="" disabled >
			</td>		
		</tr>
		</table>
	</div>

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" 
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
		<siga:ConjBotonesAccion botones="Y,C" clase="botonesDetalle" modal="G"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() 
		{
			sub();	
			if (isNaN(document.getElementById('ordenAux').value) || document.getElementById('ordenAux').value == ""){
				alert('<siga:Idioma key="gratuita.modalLG.literal.aviso5"/>');
				fin();
				return false;
			}else{
				document.forms[0].target = "submitArea";			
				document.forms[0].accion.value = "modal";
				document.forms[0].modo.value = "insertar";
				document.forms[0].orden.value=document.getElementById('ordenAux').value;	
				document.forms[0].submit();	
				window.returnValue="MODIFICADO";								
			}
		}

		//Asociada al boton Cerrar
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>