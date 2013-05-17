<!-- modalEditarCambioLetradoDesigna.jsp -->
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
<%@ taglib uri = "c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<%	String app=request.getContextPath();
	String accion =(String) request.getAttribute("accion");	%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="CambiosLetradosDesignasForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.modalCambioLetradoDesigna.tituloConsulta"/>
		</td>
	</tr>
	</table>


<!-- INICIO -->
<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	
	<html:form action="/JGR_CambiosLetradosDesigna.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "modificar"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "anio"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "numero"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "idTurno"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "idPersona"/>
	<table  class="tablaCentralCamposMedia"  align="center">
	<tr>				
	<td>

	<!-- INICIO: CAMPOS -->
	
	<siga:ConjCampos leyenda="gratuita.cambioLetrados.literal.letradoActual">
	<table  width="100%">
	
	<tr>				
		<td class="labelText" width="140">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
		</td>
		<td colspan="3"class="boxConsulta">
			<c:if test="${CambiosLetradosDesignasForm.nInstitucionOrigen==null || CambiosLetradosDesignasForm.nInstitucionOrigen == ''}"> 
				    <bean:write name="CambiosLetradosDesignasForm" property="NColegiadoActual"/>
			</c:if>
			<c:if test="${CambiosLetradosDesignasForm.nInstitucionOrigen!=null && CambiosLetradosDesignasForm.nInstitucionOrigen != ''}"> 
				    <bean:write name="CambiosLetradosDesignasForm" property="nColegiadoOrigen"/>
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="labelText" >
			<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
		</td>
		<td colspan="3" class="boxConsulta">
			<bean:write name="CambiosLetradosDesignasForm" property="nombreActual"/>&nbsp;
			<bean:write name="CambiosLetradosDesignasForm" property="apellido1Actual"/>&nbsp;
			<bean:write name="CambiosLetradosDesignasForm" property="apellido2Actual"/>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	
	<table width="100%">	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna"/>
		</td>
		<td>
			<html:text name="CambiosLetradosDesignasForm" property="fechaDesigna" size="10" maxlength="10" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenunciaSolicita"/>
		</td>
		<td>		
			<siga:Datepicker nombreCampo="fechaRenunciaSolicita" <% if (accion.equalsIgnoreCase("ver")) { %> disabled="true" <%}%>></siga:Datepicker>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenuncia"/>
		</td>
		<td>
			<html:text name="CambiosLetradosDesignasForm" property="fechaRenuncia" size="10" maxlength="10" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.tipoMotivo"/>&nbsp;(*)
		</td>
		<td colspan="3">
		    <bean:define id="vIdTipoMotivo" name="CambiosLetradosDesignasForm" property="idTipoMotivo" type="java.lang.String"/>
		<%ArrayList idTipoMotivoValue=new ArrayList();
		  idTipoMotivoValue.add(vIdTipoMotivo);%>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ComboBD pestana="true" nombre="idTipoMotivo" tipo="tipoMotivo" estilo="true" clase="boxConsulta" readonly="true" filasMostrar="1" elementoSel="<%=idTipoMotivoValue%>" seleccionMultiple="false" obligatorio="true"/>
		<%} else {%>
			<siga:ComboBD pestana="true" nombre="idTipoMotivo" tipo="tipoMotivo" estilo="true" clase="box" filasMostrar="1" elementoSel="<%=idTipoMotivoValue%>" seleccionMultiple="false" obligatorio="true"/>
		<%}%>
		</td>
	</tr>
	<tr>
		<td class="labelText" colspan="4">
			<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.observaciones"/><br>
			<!--textarea scroll="none"/-->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:textarea name="CambiosLetradosDesignasForm" rows="5" cols="150" styleClass="boxConsulta" property="observaciones" readonly="true" style="overflow-y: hidden;"/>
		<%} else {%>
			<html:textarea name="CambiosLetradosDesignasForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="5" cols="150" styleClass="box" property="observaciones"/>
		<%}%>
		</td>
	</tr>
	</table>
	</td>
	</tr>
	</table>
	
	
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
	<%if (accion.equalsIgnoreCase("ver")){%>
	<siga:ConjBotonesAccion botones="C" modal="M" />
	<%} else {%>
	<siga:ConjBotonesAccion botones="C,R,Y" modal="M"/>
	<%}%>
	<!-- FIN: BOTONES REGISTRO -->
	
	</html:form>


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			var f=document.forms[0]; 
			sub();
			if (validateCambiosLetradosDesignasForm(f)) 		
			{	
				if(f.fechaRenunciaSolicita.value=="" || isAfter(f.fechaRenunciaSolicita.value,f.fechaDesigna.value))
				{
					f.modo.value="modificar";
					f.submit();
				}else
				{
					alert("<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenunciaSolicita"/>"+
					  	  " <siga:Idioma key="gratuita.cambiosProcuradoresDesigna.alert1"/>"+f.fechaDesigna.value);
					  	  fin();
					  	  return false;
				}
			}else{
			
				fin();
				return false;
			}
		}
		
		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
