<!-- modalPJ.jsp-->
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
<table class="tablaTitulo" cellspacing="0" heigth="30">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.partidosJudiciales.literal.seleccionPoblacion"/>
	</td>
</tr>
</table>
	


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
<table class="tablaCentralCamposPeque" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_MantenimientoPartidosJudiciales.do" method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "modificar"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idPartidoSeleccionado" value = ""/>								
		<html:hidden property = "cambiar" value = "modal"/>										
		<html:hidden property = "idPartido"/>

	<tr>
	<td colspan="2">
		<br>
	</td>
	</tr>
	<!-- INICIO: COMBOS DE SELECCIÓN PROVINCIA Y POBLACIONES -->
	<tr>
		<td class="labelText" >	
			<siga:Idioma key="gratuita.partidosJudiciales.literal.provincia"/>&nbsp;(*)
		</td>			
		<td>
			<siga:ComboBDExt nombre = "comboProvincias" tipo="provincia" clase="boxCombo"  obligatorio="true" accion="Hijo:comboPoblaciones"/> 
		</td>
	</tr>
	<tr>
		<td class="labelText" >	
			<siga:Idioma key="gratuita.partidosJudiciales.literal.poblaciones"/>&nbsp;(*)
		</td>			
		<td>
			<siga:ComboBDExt nombre = "comboPoblaciones" tipo="poblacionPartido" filasMostrar = "5" clase="boxCombo" obligatorio="true" seleccionMultiple="true" hijo="t"/> 
		</td>
	</tr>
	</html:form>			
</table>
	
	
 
	<siga:ConjBotonesAccion botones="Y,C" modal="P"/>
	
	
		
	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	
		
	
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
			<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			var indice = document.forms[0].comboProvincias.selectedIndex;
			sub();
			if (indice == 0){
				alert('<siga:Idioma key="gratuita.modalPJ.literal.introducirProvincia"/>');
				fin();
				return false;
				}
			else {
					document.forms[0].modo.value = "anadirPoblacion";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
					window.returnValue="MODIFICADO";
				}				
						
		}
		
		 
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar44() 
		{		
			
			alert("top.frames[0].document="+top.frames[0].document);
			var frameCombo=top.frames[0].document.getElementById("combo_poblacionesFrame");
			alert("frame="+top.frames[0]);
			var comboPoblacion=document.forms[0].combo_poblaciones;
			alert("combo ="+comboPoblacion);
			
			//alert("comboOptions ="+comboPoblacion.options);
			alert("comboOptionsLength ="+comboPoblacion.options.length);
			var indice = document.forms[0].combo_provincias.selectedIndex;
			if (indice == 0)
				confirm('<siga:Idioma key="gratuita.modalPJ.literal.introducir"/>');
			else if (contar(comboPoblacion) == 0)
					alert("seleccione al menos una poblacion");
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
		
		function contar(objetoSelect){ 
				alert("xx="+objetoSelect.options);
				var seleccionadas=0; 

				for (var i=0;i < objetoSelect.options.length;i++){ 

					if (objetoSelect.options[i].selected) 

					seleccionadas++; 

				} 
				alert("seleccionadas="+seleccionadas);
				return seleccionadas; 

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