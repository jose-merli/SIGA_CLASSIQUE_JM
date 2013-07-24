<!DOCTYPE html>
<html>
<head>
<!-- insertarRemesa.jsp -->
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
	if (modo.equals("0")) {
	
		estilocaja = "boxConsulta";
		
	} else {
	
		estilocaja = "box";
		
	}
	
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="DefinicionRemesas_CAJG_Form" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
  		
	<script type="text/javascript">
		function busquedaAutomatica ()
		{
			document.forms[1].idTurno.value 	= document.forms[2].identificador.value;
			document.forms[1].idGuardia.value 	= document.forms[2].identificador2.value;
			document.forms[1].modo.value		= "buscarPor";
			document.forms[1].action			= "<%=app%>"+"/JGR_MantenimientoAsistencia.do";
			if(document.forms[2].identificador.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
				return false;
			}
			if(document.forms[2].identificador2.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert4' />");
				return false;
			}
			var resultado = ventaModalGeneral(document.forms[1].name,"M");
			if (resultado != null && resultado[0]!=null)
				document.forms[2].NColegiado.value = resultado[0]; 
			else
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert5' />");
		}
		//Fin Modif Carlos -->
		function actualizarFecha(){
		  document.forms[1].fechaAperturaEJG.value=DefinirEJGForm.fechaApertura.value;
		}
	</script> 
	
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.datos"/>
		</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	
	
	<html:form action="/JGR_E-Comunicaciones_Gestion.do" method="POST" target="submitArea" type="">
	
	<!-- Para seleccion automatica -->
	
	
	<!-- ************************ //-->
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "modoContador" />
	
	
	
				
			<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.BusquedaRemesas_CAJG.literal.datos">
			
		<table class="tablaCampos" align="center">
				
			</tr>
				
			<tr>	
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.nRegistro"/>	
				</td>
				
				<td class="labelText">	
					<html:text property="prefijo"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:55px" readonly="true" ></html:text>
					<html:text property="numero"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:55px" readonly="true"></html:text>
					<html:text property="sufijo"  size="5" maxlength="10" styleClass="<%=estilocaja%>" style="width:55px" readonly="true" ></html:text>	
				</td>
			</tr>
			<tr>
				
				<td class="labelText">
					<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.Descripcion"/>
				</td>
				
				<td class="labelText">	
					<html:text property="descripcion"  size="40" maxlength="200" styleClass="box"  readonly="false" ></html:text>
				</td>
						
			</tr>
			
				
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fGeneracion"/>&nbsp;(*)
				</td>
				<td class="labelText">
					<siga:Fecha nombreCampo="fechaGeneracion"  posicionX="10" posicionY="10" valorInicial="" />					
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
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() {
			var f = document.getElementById("DefinicionRemesas_CAJG_Form");
			sub();
  			if (f && validateDefinicionRemesas_CAJG_Form(f)) {			
				document.forms[0].modo.value="Insertar";				
				document.forms[0].submit();		
				window.top.returnValue="MODIFICADO";
			}else{
			
				fin();
				return false;
			
			}
		}
		
		//Asociada al boton Cerrar -->
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
