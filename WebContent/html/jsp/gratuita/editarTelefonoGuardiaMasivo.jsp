<!-- altaTurno_2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<% 	
	String app		= request.getContextPath();
	HttpSession ses	= request.getSession();
	UsrBean usr		= (UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src	= (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole 	= (Colegio)ses.getAttribute("colegio");
	Vector obj 		= (Vector) request.getAttribute("resultado");
	String clase   = "box";
	boolean desactivado = false;
	// Obtenemos los datos del request.
	String paso						= String.valueOf(request.getAttribute("paso"));
  	String titulo					= String.valueOf(request.getAttribute("titulo"));
	String botones					= String.valueOf(request.getAttribute("botones"));
  	String action 					= String.valueOf(request.getAttribute("action"));
  	String origen 					= String.valueOf(request.getAttribute("origen"));
	String idretencion 				= String.valueOf(request.getAttribute("idretencion"));
	String guardias 				= String.valueOf(request.getAttribute("GUARDIAS"));
	String idpersona				= String.valueOf(request.getAttribute("IDPERSONA"));
	String idturno 					= String.valueOf(request.getAttribute("idturno"));
	String idinstitucion			= String.valueOf(request.getAttribute("IDINSTITUCION"));
	String fechasolicitud 			= String.valueOf(request.getAttribute("FECHASOLICITUD"));
	String observacionessolicitud 	= String.valueOf(request.getAttribute("OBSERVACIONESSOLICITUD"));
	String fechasolicitudbaja 		= String.valueOf(request.getAttribute("FECHASOLICITUDBAJA"));
	String observacionesbaja 		= String.valueOf(request.getAttribute("OBSERVACIONESBAJA"));
	String fechavalidacion 			= String.valueOf(request.getAttribute("FECHAVALIDACION"));
	String observacionesvalidacion 	= String.valueOf(request.getAttribute("OBSERVACIONESVALIDACION"));
	String validarinscripciones 	= String.valueOf(request.getAttribute("VALIDARINSCRIPCIONES"));
	String tieneYaDirecGuardia		= (String)request.getAttribute("tieneYaDirecGuardia");
	String guardiasSel		= (String)request.getAttribute("GUARDIASSEL");
	String idDireccion				= request.getAttribute("idDireccion") == null?"":(String)request.getAttribute("idDireccion");
%>

<html>
	<head>
	<title><script>alert(top.title)</script></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<html:javascript formName="AltaTurnosGuardiasForm" staticJavascript="false" />  
		 <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
</head>

<body>
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.consultaTurnoMasivo.literal.title2"/>
		</td>
	</tr>
	</table>



	<html:form action="JGR_AltaTurnosGuardias.do" method="post" target="submitArea">
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<html:hidden property="paso" value="0"/>
		<html:hidden property="origen" value="altaTurnoMasivo"/>
		<html:hidden property="modo" value=""/>
		<html:hidden property="guardias" />
		<html:hidden property="idPersona" />
		<html:hidden property="idRetencion" />
		<html:hidden property="idTurno" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="fechaSolicitud" />
		<html:hidden property="observacionesSolicitud" />
		<html:hidden property="fechaSolicitudBaja" />
		<html:hidden property="fechaBaja" />
		<html:hidden property="observacionesBaja" />
		<html:hidden property="fechaValidacion" />
		<html:hidden property="observacionesValidacion" />
		<html:hidden property="validarInscripciones" />
		
		<input type="hidden" name="guardiasSel" value="<%=guardiasSel%>">
		<!--campo para saber si el cliente tiene ya una dirección de guardia//-->
		<input type="hidden" name="tieneYaDirecGuardia" value="<%=tieneYaDirecGuardia%>">
		<!-- Campo para guardar el indicador de direccion //-->
		<input type="hidden" name="idDireccion" value="<%=idDireccion%>">
		<input type="hidden" name="idTurnoSel" value="<%=idturno%>">

		<table class="tablaCentralCamposGrande" align="center" border="0" >
			<tr>				
				<td>
						<siga:ConjCampos leyenda="gratuita.editartelefonosGuardia.literal.telefonosGuardia">
					
						<table class="tablaCampos" align="center">	

						<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp(*)</td>				
		   					<td><html:text name="AltaTurnosGuardiasForm" property="telefono1"  maxlength="20" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>

		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.telefono2"/>&nbsp</td>				
		   					<td><html:text name="AltaTurnosGuardiasForm" property="telefono2"  maxlength="20" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>	
		 					</tr>

		 					<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.movil"/>&nbsp</td>				
		   					<td><html:text name="AltaTurnosGuardiasForm" property="movil"  maxlength="20" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>	
		 					</tr>

		 					<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.fax1"/>&nbsp</td>
		   					<td><html:text name="AltaTurnosGuardiasForm" property="fax1"  maxlength="20" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
		  				 	
		  				 	<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.fax2"/>&nbsp</td>				
		   					<td><html:text name="AltaTurnosGuardiasForm" property="fax2"  maxlength="20" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
		  				</tr>
		   			</table>

					</siga:ConjCampos>

				</td>
			</tr>
		
		</table>	
		</html:form>

		<siga:ConjBotonesAccion botones="<%=botones%>" />	


	<!-- ******* BOTONES DE ACCIONES ****** -->

	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() 
		{		
			top.volver();
		}

		function accionCerrar() 
		{		
			window.close();
		}
		
		function accionRestablecer() 
		{		
	
		}
		
		function accionGuardar() 
		{		
	
		}
		
		function accionGuardarCerrar() 
		{		
	
		}
		
		<!-- Asociada al boton Siguiente -->
		function accionSiguiente() 
		{		
			//Submitimos en el propio frame:
			document.forms[0].target="_self";
			document.forms[0].action="<%=app%><%=action%>.do";
			document.forms[0].paso.value="<%=paso%>";
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}
		
		<!-- Asociada al boton Finalizar -->
		function accionFinalizar() 
		{		
			sub();
			/* if(trim(document.forms[0].telefono1.value) =="")
			{
				var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.telefono1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert(mensaje);
				return false;
			}*/
			  document.AltaTurnosGuardiasForm.telefono1.value=eliminarBlancos(trim(document.AltaTurnosGuardiasForm.telefono1.value));
		  document.AltaTurnosGuardiasForm.telefono2.value=eliminarBlancos(trim(document.AltaTurnosGuardiasForm.telefono2.value));
		  document.AltaTurnosGuardiasForm.fax1.value=eliminarBlancos(trim(document.AltaTurnosGuardiasForm.fax1.value));
		  document.AltaTurnosGuardiasForm.fax2.value=eliminarBlancos(trim(document.AltaTurnosGuardiasForm.fax2.value));
		  document.AltaTurnosGuardiasForm.movil.value=eliminarBlancos(trim(document.AltaTurnosGuardiasForm.movil.value));
			if (!validateAltaTurnosGuardiasForm(document.AltaTurnosGuardiasForm)) { 
			  	fin();
				return false;
			}else{  
				document.forms[0].modo.value="insertarMasivo";
				document.forms[0].submit();
				window.returnValue="INSERTADO";			
			}
		}

	</script>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
	</body>
</html>
