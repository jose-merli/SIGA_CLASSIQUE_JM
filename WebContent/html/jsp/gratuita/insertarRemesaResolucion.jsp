<!-- insertarRemesaResolucion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	String modo=(String)request.getAttribute("modoContador");
	String estilocaja="";
	String idTipoRemesa = request.getParameter("idTipoRemesa");
	
	if (modo.equals("0")) {	
		estilocaja = "boxConsulta";		
	} else {	
		estilocaja = "box";		
	}		
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
	<html:javascript formName="DefinicionRemesaResolucionesCAJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
  		
	<script type="text/javascript">
		
	</script> 
	
	
</head>

<body onload="eliminaPreSuf()">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="gratuita.busquedaResolucionesCAJG.literal.datos"/>
		</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	
	
	<html:form action="/JGR_E-Comunicaciones_RemesaResolucion.do" method="POST" target="submitArea"  enctype="multipart/form-data">
	
	<!-- Para seleccion automatica -->
	
	
	<!-- ************************ //-->
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoRemesa" value ="<%=idTipoRemesa%>"/>
	
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	
	
	
				
			<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaResolucionesCAJG.literal.datos">
			
		<table class="tablaCampos" align="center">
				
			</tr>
				
			<tr>	
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.numRegistro"/>	
				</td>
				
				<td class="labelText">						
					<html:text  property="prefijo"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:80px" readonly="true" ></html:text>					
					<html:text  property="numero"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:80px" readonly="true"></html:text>
					<html:text  property="sufijo"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:80px" readonly="true" ></html:text>	
				</td>
			</tr>
				
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCarga"/>&nbsp;(*)
				</td>
				<td class="labelText">
					<html:text   property="fechaCarga" size="10" maxlength="10" styleClass="<%=estilocaja%>"  value="<%=fecha%>" readOnly="true"></html:text>					
				</td>
		
			</tr>
						
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fResolucion"/>&nbsp;(*)
				</td>
				<td class="labelText">
					<html:text   property="fechaResolucion" size="10" maxlength="10" styleClass="box"  value="<%=fecha%>" readOnly="true"></html:text>
					<a onClick="return showCalendarGeneral(fechaResolucion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
				</td>
		
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroResoluciones"/>&nbsp;(*)
				</td>				
				<td class="labelText">	
					<html:file property="file" size="50" styleClass="box" accept="image/gif,image/jpg"></html:file>
				</td>						
			</tr>
			
			
		</table>
	</siga:ConjCampos>	
	
	
	</html:form>


	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		function eliminaPreSuf() {
			if (!document.forms[0].prefijo.value) {
				document.forms[0].prefijo.style.display='none';
			}
			if (!document.forms[0].sufijo.value) {
				document.forms[0].sufijo.style.display='none';
			}
		}
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() {
			var f = document.getElementById("DefinicionRemesaResolucionesCAJGForm");			
			sub();		
			
			
				
  			if (f && validateDefinicionRemesaResolucionesCAJGForm(f)) {
  			
  				if (!document.forms[0].file.value) {
  					var campo = '<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroResoluciones"/>'
  					var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
					alert (msg);
  					fin();
					return false;
  				}
  				

				f.modo.value="Insertar";								
				//document.forms[0].submit();				
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.gratuita.actualizadonResoluciones';
				
				window.top.returnValue="MODIFICADO";
				
			} else {			
				fin();
				return false;
			
			}
		}
		
		function refrescarLocal(){		 
			parent.buscar();
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {
			top.cierraConParametros("NORMAL");			
		}
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
