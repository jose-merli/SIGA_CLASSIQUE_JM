<!-- modal_IncompatibilidadesGuardia.jsp -->
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
	String idinstitucionpestanha = request.getAttribute("IDINSTITUCIONPESTAÑA")==null?"":(String)request.getAttribute("IDINSTITUCIONPESTAÑA");
	String idturnopestanha = request.getAttribute("IDTURNOPESTAÑA")==null?"":(String)request.getAttribute("IDTURNOPESTAÑA");
	String idguardiapestanha = request.getAttribute("IDGUARDIAPESTAÑA")==null?"":(String)request.getAttribute("IDGUARDIAPESTAÑA");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<script>
		function refrescarlocal() {
			parent.refrescarlocal();
		}
	</script>
	
</head>

<body onLoad="ajusteAltoMain('resultado','100');">

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulosPeq">
		<siga:Idioma key="gratuita.IncompatibilidadesGuardia.literal.titulo"/>
	</td>
</tr>
</table>
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposGrande" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_IncompatibilidadesGuardia.do" method="post" target="submitArea">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idInstitucionPestanha" value = "<%=idinstitucionpestanha%>"/>
		<html:hidden property = "idTurnoPestanha" value = "<%=idturnopestanha%>"/>
		<html:hidden property = "idGuardiaPestanha" value = "<%=idguardiapestanha%>"/>
		<html:hidden property = "idGuardiaIncompatible" value = ""/>
		<html:hidden property = "idTurnoIncompatible" value = ""/>		
		<html:hidden property = "motivos" value = ""/>		

		<html:hidden property = "hiddenFrame" value = "1"/>		
		<input type="hidden" name="datosModificados" value="">
		
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>		
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.aviso1" />:
		</td>
	</tr>
	<tr>
	<td>
		<fieldset>
		<!-- SUBCONJUNTO DE DATOS -->
			<table class="tablaCampos" align="center">
				<tr>
					<td class="labelText" width="150">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.abreviatura"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="abreviatura" size="30" maxlength="30" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText" width="150">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.turno"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="turno" size="30" maxlength="100" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
				<tr>
					<td class="labelText">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.zona"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="zona" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.subzona"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="subzona" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
				<tr>
					<td class="labelText">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.area"/>	
					</td>	
					<td class="labelText">	
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="area" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
					<td class="labelText">	
						<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.materia"/>
					</td>	
					<td class="labelText">
						<html:text name="DefinirIncompatibilidadesGuardiaForm" property="materia" size="30" maxlength="60" styleClass="box" value=""></html:text>		
					</td>	
				</tr>
		</table>
		</fieldset>
	</td>
	</tr>
	</table>
	</html:form>	
	

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesBusqueda botones="B" />
	<!-- FIN: BOTONES BUSQUEDA -->
	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		
		//Funcion asociada al boton buscar -->
		function buscar() 
		{
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		

	</script>


	<div style="position:absolute;left:0px;bottom:30px;width:100%;">
		<table>
			<tr>
				<td class="labelText" width="80">
					<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.motivos"/>&nbsp;(*)
				</td>
				<td class="labelText">
						<textarea  onkeydown="cuenta(this,255)" onChange="cuenta(this,255)" name="motivosAux" cols="180" rows="3" class="box" style="overflow:auto" disabled></textarea>
				</td>		
			</tr>
		</table>
	</div>

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->



	
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
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			if (document.getElementById('motivosAux').value == ""){// si no se introduce motivo
				confirm('<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.aviso3"/>');
			}else{
			  var datos = "";
			  var encontrarCheck="0";
			  var ele =  window.frames.resultado.document.getElementsByName("chkGuardia");
				
				for (i = 0; i < ele.length; i++) {
					if (ele[i].checked) {
					
					  datos = datos +  window.frames.resultado.document.getElementById("oculto" + ele[i].value + "_1").value + "#_#" + 	// idTurno Incompatible
					  		  window.frames.resultado.document.getElementById("oculto" + ele[i].value + "_2").value + "#;#" ;		// idGuardia Incompatible
					  encontrarCheck="1";				  
					}
					
				}
				if (encontrarCheck=="0"){// si no se selecciona ninguna guardia de la lista
				 confirm('<siga:Idioma key="gratuita.modal_IncompatibilidadesGuardia.literal.aviso3"/>');
				 return;
				}
				document.forms[0].target = "submitArea";			
				document.forms[0].modo.value = "insertar";
				document.forms[0].motivos.value = document.getElementById('motivosAux').value;
				document.forms[0].datosModificados.value=datos;
				document.forms[0].submit();	
				window.top.returnValue="MODIFICADO";
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