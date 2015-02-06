<!DOCTYPE html>
<html>
<head>
<!-- busquedaDireccionesModal.jsp -->
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
<%@ page import="com.siga.envios.form.RemitentesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
 <%@ page import="java.util.Properties"%>
 <%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	
	RemitentesForm form = (RemitentesForm)request.getAttribute("RemitentesForm");
	
	String idTipoEnvio = form.getIdTipoEnvio();
	
	
	String funcionBuscar="";
	String botonBuscar="B";
	String lectura="false";
	String clase="boxCombo";
	int preseleccion=1;
		
	ArrayList aTipo = new ArrayList();
	if (idTipoEnvio!=null&&!idTipoEnvio.equals("")){
		funcionBuscar="buscar()";
		botonBuscar="";
		lectura="true";
		clase="boxConsulta";
		aTipo.add(idTipoEnvio);
	} else {
		aTipo.add("");
	}
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Validaciones en Cliente -->
	<html:javascript formName="RemitentesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


</head>

<body onload="ajusteAlto('resultadoModal');<%=funcionBuscar%>">
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="envios.certificados.literal.elegirDireccion"/>
				</td>
			</tr>
		</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

	<html:form action="/ENV_Remitentes.do" method="POST" target="resultadoModal">
	<html:hidden property="modo" value="buscarDirecciones"/>
	<html:hidden property = "idInstitucion"/>
	<html:hidden property = "idPersona"/>

	<% if ((idTipoEnvio == null) || (idTipoEnvio != null && !idTipoEnvio.equalsIgnoreCase("-1"))){ %>

		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCampos"  align="center">
			<tr>				
				<td>
					<siga:ConjCampos>
						<table class="tablaCampos" align="center">
							<tr>
								<td class="labelText" width="70">
									<siga:Idioma key="envios.definir.literal.tipoenvio"/>
								</td>
								<td>
									<siga:ComboBD nombre = "idTipoEnvio" tipo="cmbTipoEnvios" ElementoSel="<%=aTipo%>"  clase="<%=clase%>" obligatorioSinTextoSeleccionar="true" readonly="<%=lectura%>"/>			
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

		<!-- FIN: CAMPOS DE BUSQUEDA-->

	<% } else { %>
	
		<!-- Nos traemos todas las direcciones sin filtar por tiipo -->
		<html:hidden property = "idTipoEnvio" value="-1" />
	<% } %>

	</html:form>


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

	<% if ((idTipoEnvio == null)|| (idTipoEnvio != null && !idTipoEnvio.equalsIgnoreCase("-1"))){ %>
		<siga:ConjBotonesBusqueda botones="<%=botonBuscar%>"  modal="G" />
	<% } %>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 		
		{		
			//if (validateRemitentesForm(document.RemitentesForm)){
			if (document.RemitentesForm.idTipoEnvio){
			  if (document.RemitentesForm.idTipoEnvio.value==''){// validacion de obligatoriedad del campo tipo envio
				 alert('<siga:Idioma key="messages.consultas.error.tipoEnvio"/>');
				 return;
			  }
			}  	
			document.forms[0].submit();
			//}			
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoModal"
					name="resultadoModal" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="C" modal="G" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
