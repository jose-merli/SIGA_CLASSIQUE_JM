<!---------- busquedaIncompatibilidadesGuardias.jsp ---------->

<!---------- CABECERA JSP ---------->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!---------- TAGLIBS ---------->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!---------- IMPORTS ---------->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<!---------- PRE-JAVASCRIPT ---------->
<%
    //Controles globales
	String app      = request.getContextPath (); 
	HttpSession ses = request.getSession (true);
	UsrBean usr     =(UsrBean) ses.getAttribute ("USRBEAN");
	Properties src  =(Properties) ses.getAttribute (SIGAConstants.STYLESHEET_REF);
	String[] dato = {usr.getLocation ()};
%>


<html>


<!---------- HEAD ---------->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.incompatibilidadesGuardias.cabecera" 
		localizacion="gratuita.incompatibilidadesGuardias.localizacion" />
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>


<!---------- BODY ---------->
<body onload="ajusteAltoBotones('resultado');">

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<siga:ConjCampos leyenda="gratuita.incompatibilidadesGuardias.criteriosBusqueda">
	<html:form action="/JGR_MantenimientoIncompatibilidadesGuardias.do"
	           method="POST" target="resultado">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idTurno" value = ""/>
	<html:hidden property = "idGuardia" value = ""/>
	<html:hidden property = "idTurno_incompatible" value = ""/>
	<html:hidden property = "idGuardia_incompatible" value = ""/>
	<html:hidden property = "diasseparacionguardias" value = ""/>
	<html:hidden property = "motivos" value = ""/>
	<html:hidden property = "incompParaAnyadir" value = ""/>
	<html:hidden property = "incompParaQuitar" value = ""/>
	<html:hidden property = "soloIncompatibilidades" value = ""/>
	<html:hidden property = "idInstitucion" value = "<%usr.getLocation ();%>" />
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
    
    <table align="left" width="100%">
      <tr>
        <td class="labelText">
          <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.turno1" />
        </td>
        <td>
          <siga:ComboBD nombre="turno1" tipo="turnos" estilo="true"
                        clase="boxCombo" filasMostrar="1"
                        seleccionMultiple="false" obligatorio="false"
                        parametro="<%=dato%>" accion="Hijo:guardia1" ancho="300" />
        </td>
        <td class="labelText">
          <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.turno2" />
        </td>
        <td>
          <siga:ComboBD nombre="turno2" tipo="turnos" estilo="true"
                        clase="boxCombo" filasMostrar="1"
                        seleccionMultiple="false" obligatorio="false"
                        parametro="<%=dato%>" accion="Hijo:guardia2" ancho="300" />
        </td>
               
        <td class="labelText" rowspan="2">
          <br>
          <input type="checkbox" name="soloIncomp" />
          <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.soloIncompatibilidades" />
        </td>
      </tr>
      <tr>
      	 <td class="labelText">
          <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.guardia1" />
        </td>
        <td>
          <siga:ComboBD nombre="guardia1" tipo="cmbGuardias" estilo="true"
                        clase="boxCombo" filasMostrar="1"
                        seleccionMultiple="false" obligatorio="false"
                        parametro="<%=dato%>" hijo="t" ancho="300" />
        </td>
        <td class="labelText">
          <siga:Idioma key="gratuita.incompatibilidadesGuardias.literal.guardia2" />
        </td>
        <td>
          <siga:ComboBD nombre="guardia2" tipo="cmbGuardias" estilo="true"
                        clase="boxCombo" filasMostrar="1"
                        seleccionMultiple="false" obligatorio="false"
                        parametro="<%=dato%>" hijo="t" ancho="300" />
        </td>
      </tr>
    </table>

	</html:form>
	</siga:ConjCampos>
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	
	
	<!-- INICIO: BOTONES DE BUSQUEDA-->
	<siga:ConjBotonesBusqueda botones = "B"
	    titulo="gratuita.incompatibilidadesGuardias.literal.titulo" />
	<!-- FIN: BOTONES DE BUSQUEDA-->
    
    
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
	
	
	<!-- INICIO: BOTONES DE ACCION -->
	<siga:ConjBotonesAccion botones="MT,DT,G" clase="botonesDetalle" />
	<!-- FIN: BOTONES DE ACCION -->
	
	
	<!-- INICIO: SCRIPT -->
	<script language="JavaScript">
		<!-- Funcion asociada a boton buscar -->
		function buscar () 
		{
			//obtenemos el idturno y el idguardia
			sub();
			document.forms[0].idGuardia.value  =
			    document.forms[0].guardia1.value;
			document.forms[0].idTurno.value    =
			    document.forms[0].turno1.value.substr
			        (document.forms[0].turno1.value.indexOf (",") + 1);
			document.forms[0].idGuardia_incompatible.value  =
			    document.forms[0].guardia2.value;
			document.forms[0].idTurno_incompatible.value    =
			    document.forms[0].turno2.value.substr
			        (document.forms[0].turno2.value.indexOf (",") + 1);
			document.forms[0].soloIncompatibilidades.value = 
				document.forms[0].soloIncomp.checked ? "1" : "0";
			document.forms[0].target           = "resultado";
			document.forms[0].modo.value       = "buscarPor";
			document.forms[0].submit ();
		}
		
		<!-- Asociada al boton MarcarTodos -->
		function accionMarcarTodos ()
		{		
			var checks = document.resultado.document.getElementsByName ("chkInc");
			if (checks.type != 'checkbox') {
				for (i = 0; i < checks.length; i++){
					checks[i].checked=1;		
				}	
			}
			else {
				checks.checked=1;		
			}
		} //accionMarcarTodos ()
		
		<!-- Asociada al boton DesmarcarTodos -->
		function accionDesmarcarTodos ()
		{		
			var checks = document.resultado.document.getElementsByName ("chkInc");
			if (checks.type != 'checkbox') {
				for (i = 0; i < checks.length; i++){
					checks[i].checked=0;		
				}
			}
			else {
			  checks.checked=0;
			}
		} //accionDesmarcarTodos ()
		
		<!-- Funcion asociada a boton guardar -->
		function accionGuardar ()
		{
			//solicitando numero de dias
			var datos = showModalDialog ("/SIGA/html/jsp/gratuita/" +
			    "modalDiasSeparacionIncompatibilidad.jsp", "",
			    "dialogHeight:250px;dialogWidth:500px;" +
			    "help:no;scroll:no;status:no;");
			window.top.focus();
			if (datos == null) 
				return;
			if (datos[0] != 1)
				return;
			//Boton Guardar
				document.forms[0].diasseparacionguardias.value = datos[1];
				document.forms[0].motivos.value = datos[2];
			
		    //deshabilitando botones
			sub ();
			
			//obteniendo los checks
			var oCheck = document.frames.resultado.document.getElementsByName
			    ("chkInc");
			
			//separando checks activados de desactivados
			document.forms[0].incompParaAnyadir.value = "";
			document.forms[0].incompParaQuitar.value = "";
			for(i=0; i<oCheck.length; i++)
			{
				if (oCheck[i].checked) {
					document.forms[0].incompParaAnyadir.value += ";" +
					    oCheck[i].value;
				}
				else {
					document.forms[0].incompParaQuitar.value += ";" +
					    oCheck[i].value;
				}
			}
			
		    //habilitando botones
			fin();
			
			//mandando guardar las incompatibilidades
		   	document.forms[0].modo.value = 'editar';
			var f = document.forms[0].name;	
			document.forms[0].target = "submitArea2";
		    document.frames.submitArea2.location = 
		        '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' +
		        f + '&msg=messages.wait';
		    
		} //accionGuardar ()
		
		function refrescarLocal ()
		{
		    buscar ();
		}
	</script>
	<!-- FIN: SCRIPT -->
	
	
    <!-- INICIO: SUBMIT AREA -->
    <!-- Obligatoria en todas las páginas -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	        style="display:none" />
	<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp"
	        style="display:none" />
    <!-- FIN: SUBMIT AREA -->

</body>
</html>