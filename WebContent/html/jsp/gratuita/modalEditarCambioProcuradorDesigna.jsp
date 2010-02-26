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

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>

<!-- JSP -->
<%	String app=request.getContextPath();
	String accion =(String) request.getAttribute("accion");	%>

<%	/*Lo del pcajg*/
	String asterisco = "&nbsp(*)&nbsp";
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	boolean obligatorioDesignacion = false;
	if (pcajgActivo==5){
		obligatorioDesignacion = true;
	}
	%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
 	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="CambiosProcuradoresDesignasForm" staticJavascript="false" />  
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
	
	<html:form action="/JGR_CambioProcuradorDesigna.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "modificar"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "anio"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "numero"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "idTurno"/>
	<html:hidden name="CambiosProcuradoresDesignasForm" property = "idProcurador"/>
	<table  class="tablaCentralCamposMedia"  align="center" border="0">
	<tr>				
	<td>

	<!-- INICIO: CAMPOS -->
	
	<siga:ConjCampos leyenda="gratuita.cambiosProcuradoresDesigna.literal.procuradorActual">
	<table  width="100%">
	
	<tr>				
		<td class="labelText" width="140">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
		</td>
		<td colspan="3" class="boxConsulta">
			<bean:write name="CambiosProcuradoresDesignasForm" property="NColegiadoActual"/>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
		</td>
		<td colspan="3" class="boxConsulta">
			<bean:write name="CambiosProcuradoresDesignasForm" property="nombreActual"/>&nbsp;
			<bean:write name="CambiosProcuradoresDesignasForm" property="apellido1Actual"/>&nbsp;
			<bean:write name="CambiosProcuradoresDesignasForm" property="apellido2Actual"/>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>

	<table width="100%" >
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.numeroDesigna"/>
			<% if (obligatorioDesignacion){ %>
				<%= asterisco %>
			<%} %>
		</td>
		<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="CambiosProcuradoresDesignasForm" property="numeroDesigna" size="13" maxlength="10" styleClass="boxConsulta" readonly="true"></html:text>
		<%} else {%>
			<html:text name="CambiosProcuradoresDesignasForm" property="numeroDesigna" size="13" maxlength="10" styleClass="box"></html:text>
		<%}%>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna"/>
		</td>
		<td>
			<html:text name="CambiosProcuradoresDesignasForm" property="fechaDesigna" size="10" maxlength="10" styleClass="boxConsulta" readonly="true" readonly="true"></html:text>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenunciaSolicita"/>
		</td>
		<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="CambiosProcuradoresDesignasForm" property="fechaRenunciaSolicita" size="13" maxlength="10" styleClass="boxConsulta" readonly="true" readonly="true" ></html:text>
		<%} else {%>
	<logic:notEmpty name="CambiosProcuradoresDesignasForm" property="fechaRenunciaSolicita">
			<html:text name="CambiosProcuradoresDesignasForm" property="fechaRenunciaSolicita" size="13" maxlength="13" styleClass="boxConsulta" readonly="true"></html:text>

	</logic:notEmpty>
	<logic:empty name="CambiosProcuradoresDesignasForm" property="fechaRenunciaSolicita">
			<html:text name="CambiosProcuradoresDesignasForm" property="fechaRenunciaSolicita" size="13" maxlength="15" styleClass="box" readonly="true"></html:text>
			&nbsp;<a onClick="return showCalendarGeneral(fechaRenunciaSolicita);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	</logic:empty>
		<%}%>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenuncia"/>
		</td>
		<td>
			<html:text name="CambiosProcuradoresDesignasForm" property="fechaRenuncia" size="10" maxlength="10" styleClass="boxConsulta" readonly="true" ></html:text>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.tipoMotivo"/>
		</td>
		<td colspan="3">
		    <bean:define id="vIdTipoMotivo" name="CambiosProcuradoresDesignasForm" property="idTipoMotivo" type="java.lang.String"/>
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
			<html:textarea name="CambiosProcuradoresDesignasForm" rows="5" cols="150" styleClass="boxConsulta" style="width=660;height=170" onkeydown="cuenta(this,1024);" property="observaciones" readonly="true" style="overflow-y: hidden;"/>
		<%} else {%>
			<html:textarea name="CambiosProcuradoresDesignasForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="5" cols="150" styleClass="box" style="width=660;height=170" onkeydown="cuenta(this,1024);" property="observaciones"/>
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
			// jbd 01-02-2010 Añadida comprobacion de numero desginacion inc-6788
			<%if (pcajgActivo>0){%>
			var error = "";
	   		if (<%= obligatorioDesignacion %> && document.getElementById("numeroDesigna").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.cambiosProcuradoresDesigna.literal.numeroDesigna'/>"+ '\n';
			if(error!=""){
				alert(error);
				fin();
				return false;
			}
	 		<%}%> 
			var f=document.forms[0];
			sub();
			if (validateCambiosProcuradoresDesignasForm(f)) 		
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
