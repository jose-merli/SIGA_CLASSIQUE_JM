<!-- anticiposEditar.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %> 

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("AnticiposClienteForm");

	//recogemos los datos
	Hashtable resultado = (Hashtable)request.getAttribute("resultado");
	Vector v = (Vector)request.getAttribute("serviciosAnticipo");

	//variables que se van a mostrar en la jsp
	String descripcion="", importeAnticipado="", importeRestante="", idPersona="", idAnticipo="", fecha="" ;

	//inicializamos los valores
	try{
		descripcion = (String)resultado.get(PysAnticipoLetradoBean.C_DESCRIPCION);
		importeAnticipado = (String)resultado.get(PysAnticipoLetradoBean.C_IMPORTEINICIAL);
		importeRestante = (String)resultado.get("IMPORTERESTANTE");
		idPersona = (String)resultado.get(PysAnticipoLetradoBean.C_IDPERSONA);
		idAnticipo = (String)resultado.get(PysAnticipoLetradoBean.C_IDANTICIPO);
		formulario.setCtaContable((String)resultado.get(PysAnticipoLetradoBean.C_CTACONTABLE));
		fecha = GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(PysAnticipoLetradoBean.C_FECHA));
	}catch(Exception e){
		idPersona = formulario.getIdPersona();
	}

	
	//recuperamos el modo de acceso
	String modo ="";
	String funcionOnLoad="";
	if ((descripcion==null)||(descripcion.equals(""))) { 
		modo="insertar";
	} else {
		modo ="modificar";
		funcionOnLoad="validarAncho_tablaResultados()";
		
	}

	String botonesAccion ="Y";
	String botonesRegistro ="";
	String estilo="box";
	String estiloNumber="boxNumber";
	boolean readOnly=false;
	boolean readOnlyImporte=false;
	if (formulario.getAccion().equals("Ver")) {
		estilo="boxConsulta";
		estiloNumber="boxConsultaNumber";
		readOnly=true;
		readOnlyImporte=true;
		
		botonesAccion ="";
		botonesRegistro ="";
		
	} else if (formulario.getAccion().equals("Editar")) {
		botonesAccion ="C,Y";
		estiloNumber="boxConsultaNumber";
		readOnlyImporte=true;
	}

%>	
<html>

<!-- HEAD -->
<head>

	<script language="JavaScript">

	function accionNuevo() {
										
    	document.AnticiposClienteForm.modo.value = "abrirServicios";
		// Abro ventana modal y refresco si necesario
		var resultado = ventaModalGeneral(document.AnticiposClienteForm.name,"M");
		
		if (resultado=='MODIFICADO') {
			parent.cargaContenidoModal();	
		}
		
	}
	
	function refrescarLocal() {
			parent.cargaContenidoModal();		
	}
	</script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<html:javascript formName="AnticiposClienteForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="censo.anticipos.mantenimiento.cabecera"/>
		</td>
	</tr>
	</table>



	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">

	<html:form action="/CEN_AnticiposCliente.do" method="POST" target="submitArea">
	
	<html:hidden name="AnticiposClienteForm" property = "modo" value = "<%=modo %>"/>
	<html:hidden name="AnticiposClienteForm" property = "idInstitucion" value="<%=usr.getLocation() %>" />
	<html:hidden name="AnticiposClienteForm" property = "idPersona"  value="<%=idPersona%>"/>
	<html:hidden name="AnticiposClienteForm" property = "idAnticipo"   value="<%=idAnticipo%>"/>
	
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.anticipos.leyenda">

		<table class="tablaCampos" align="center" border="0" width="100%">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.descripcion"/>&nbsp;(*)
		</td>				
		<td class="labelText" colspan="3">
			<html:text name="AnticiposClienteForm" property="descripcion" styleId="descripcion" size="54" maxlength="200" styleClass="<%=estilo %>" readonly="<%=readOnly %>"  value="<%=descripcion%>"/>
		</td>
		</tr>

		</tr>				

<% if (!modo.equals("insertar"))  { %>
		<tr>				



		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.fecha"/>
		</td>				
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="fecha" styleId="fecha" size="12" maxlength="10" styleClass="boxConsulta" readonly="true"  value="<%=fecha%>"/>
		</td>
		</tr>

<% } %>
	<tr>
		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.ctaContable"/>
		</td>				
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="ctaContable" styleId="ctaContable" size="12" maxlength="20" styleClass="<%=estilo %>" readonly="<%=readOnly %>"/>
		</td>
		</tr>

		</tr>				



		<tr>
		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.importeAnticipado"/>&nbsp;(*)
		</td>
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="importeAnticipado" styleId="importeAnticipado" size="11" maxlength="11" styleClass="<%=estiloNumber %>" readonly="<%=readOnlyImporte %>" value="<%=UtilidadesNumero.formatoCampo(importeAnticipado)%>"/>&nbsp;&euro; 
		</td>
<% if (!modo.equals("insertar"))  { %>

		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.importeRestante"/>
		</td>
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="importeRestante" styleId="importeRestante" size="11" maxlength="11" styleClass="boxConsultaNumber" readonly="true" value="<%=UtilidadesNumero.formatoCampo(importeRestante)%>"/>&nbsp;&euro; 
		</td>
<% } %>

		</tr>
				
		</table>

	</siga:ConjCampos>

	</td> 
	</tr>
			
			<!-- RGG: cambio a formularios ligeros -->

		</html:form>	
	</table>
	
<% if (modo.equals("insertar"))  { %>
	 	<siga:ConjBotonesAccion botones="C,Y" modal="M" />
<% } else  { %>	

<siga:ConjBotonesAccion botones="<%=botonesAccion %>" clase="botonesSeguido" modal="M" titulo="censo.anticipos.literal.serviciosConfigurados"/>

	<siga:TablaCabecerasFijas 
			   nombre="tablaResultados"
			   borde="1"
			   clase="tableTitle"				   
			   nombreCol="censo.anticipos.literal.nombreServicio,"
			   tamanoCol="90,10"
			   
			   modal="M"
			   scrollModal = "true"
			   
			   >

<% if ((v != null) && (v.size() > 0)) {
		for (int i = 0; i < v.size(); i++) { 
			Hashtable hash = (Hashtable)v.get(i);
			if (hash != null) {
				String idInstitucionReg = UtilidadesHash.getString (hash, PysServicioAnticipoBean.C_IDINSTITUCION);
				String idPersonaReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDPERSONA);
				String idAnticipoReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDANTICIPO);
				String idTipoServicioReg =  UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDTIPOSERVICIOS);
				String  idServicioReg =UtilidadesHash.getString (hash, PysServicioAnticipoBean.C_IDSERVICIO);
				String  idServicioInstitucionReg =UtilidadesHash.getString (hash, PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION);
				String  descripcionServicioReg =UtilidadesHash.getString (hash, "DESCRIPCION");
				FilaExtElement[] elems=new FilaExtElement[1];
				
%>


<% if (formulario.getAccion().equals("Editar")){
	elems[0]=new FilaExtElement("borrarServicios","borrarServicios",SIGAConstants.ACCESS_READ);
}%>
				<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' visibleConsulta="no" visibleBorrado="no" visibleEdicion="no" pintarEspacio="no" botones="" elementos='<%=elems%>'  modo='<%=modo%>' clase="listaNonEdit">
					<td>
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_1" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idInstitucionReg %>">
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_2" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idPersonaReg %>">
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_3" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idAnticipoReg %>">
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_4" name="oculto<%=String.valueOf(i+1)%>_4" value="<%=idTipoServicioReg %>">
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_5" name="oculto<%=String.valueOf(i+1)%>_5" value="<%=idServicioReg %>">
						<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_6" name="oculto<%=String.valueOf(i+1)%>_6" value="<%=idServicioInstitucionReg %>">
						
						<%=UtilidadesString.mostrarDatoJSP(descripcionServicioReg)%></td>
						
				</siga:FilaConIconos>				
<%
			}
		}
	}
	else { // No hay registros 
	%>
		<table align="center">
			<tr>
				<td>
					<br>
					<font class="labelText" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></font>
				</td>
			</tr>
			</table>
<% } %>

	</siga:TablaCabecerasFijas>
	 
	<% if (!modo.equalsIgnoreCase("insertar")) {%>
		<% if (formulario.getAccion().equals("Editar")) {%>
	 		<siga:ConjBotonesAccion botones="N" modal="M" />
	 	<% } else { %>
	 		<siga:ConjBotonesAccion botones="C" modal="M" />
	 	<% } %>
	<% } %>
	
<% } %>	




	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script type="text/javascript" >

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub();		
			document.forms[0].importeAnticipado.value=document.forms[0].importeAnticipado.value.replace(/,/,".");
			if (validateAnticiposClienteForm(document.AnticiposClienteForm)){
				document.forms[0].modo.value="<%=modo%>";
				document.forms[0].submit();					
			} else {
				fin();
				return false;
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {
			window.top.returnValue="MODIFICADO";
			window.parent.close();
		}
		
		function borrarServicios(fila) {		
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;
			j = 1;
			while (flag) {
				var aux = 'oculto' + fila + '_' + j;
				var oculto = document.getElementById(aux);
				if (oculto == null)  { 
					flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
			  	j++;
			}
			datos.value = datos.value + "%"
									
	    	document.AnticiposClienteForm.modo.value = "borrarServicios";
			document.AnticiposClienteForm.submit();
		}
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
