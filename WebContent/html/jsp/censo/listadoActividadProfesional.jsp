<!DOCTYPE html>
<html>
<head>
<!-- listadoActividadProfesional.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.GruposClienteClienteForm"%>
<%@ page import="com.siga.censo.form.ActividadProfesionalForm"%>
<%@ page import="com.siga.beans.CenGruposClienteBean"%>
<%@ page import="com.siga.beans.CenActividadProfesionalBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector vGrupos = (Vector)request.getAttribute("ACTIVIDADES");
	
	ActividadProfesionalForm miform = (ActividadProfesionalForm)request.getAttribute("ActividadProfesionalForm");
	
	String modoAnterior = miform.getModoAnterior();
	String iconos = "B";
	String botones = "N";
	String alto = "70";
	
	if (modoAnterior==null || modoAnterior.equalsIgnoreCase("nuevo") || modoAnterior.equalsIgnoreCase("ver") || modoAnterior.equalsIgnoreCase("nuevaSociedad")) {
		botones = "";
		iconos = "";
		if (vGrupos==null || vGrupos.isEmpty())
			alto = "100";
	}
%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
	<script>

		function refrescarLocal() {
			parent.refrescarLocal();
			document.forms[0].target="_self";
			document.forms[0].modo.value="buscar";
			document.forms[0].submit();				
		}
		
		<!-- Asociada al boton Nuevo -->
		function nuevo() {	
			document.forms[0].modo.value="nuevo";
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado) {
				refrescarLocal();
			}
		}	
		
		function validaTabla(){
		  	if (document.getElementById("tablaDatos").clientHeight < document.getElementById("tablaDatosDiv").clientHeight) {
		   		document.getElementById("tablaDatosCabeceras").width='100%';
			} else {
				document.getElementById("tablaDatosCabeceras").width='95%';
			}
		}
	</script>
	
</head>

<body>

	<!-- Comienzo del formulario con los campos -->
	<html:form action="/CEN_ActividadProfesional.do" method="POST" target="submitArea" style="display:none">
		<html:hidden styleId="modo"  property="modo" value="insertar"/>
		<html:hidden styleId="idPersona"  property="idPersona" />
		<html:hidden styleId="idInstitucion"  property="idInstitucion" />
		<html:hidden styleId="modoAnterior"  property="modoAnterior" />
		<!-- RGG: cambio a formularios ligeros -->
	</html:form>

	<!-- INICIO TABLA DE GRUPOS -->
	<siga:Table 
		   	name="tablaDatos"
		   	border="0"
		  	columnNames="censo.fichaCliente.literal.actprofesional,"
		  	columnSizes="80,20"
		    modal="P">  

 	<% if(vGrupos==null || vGrupos.isEmpty())	{ %>
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
	<% } else {	 
 			Enumeration en = vGrupos.elements();		
 			int i=0;  									
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
	%> 				
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' modo='<%=modoAnterior%>' clase="listaNonEdit" visibleEdicion="no" visibleConsulta="no" pintarEspacio="false" >
				<td>
					<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=(String)htData.get(CenActividadProfesionalBean.C_IDACTIVIDADPROFESIONAL)%>'>	
					<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=(String)htData.get("IDINSTITUCION_GRUPO")%>'>	
  					<%=UtilidadesString.mostrarDatoJSP(htData.get("NOMBRE"))%>
  				</td>
				</siga:FilaConIconos>

    <% } } %>  			
  		</siga:Table>
 	<!-- FIN TABLA DE GRUPOS -->
	<table class="botonesDetalle">
		<tr>
			<td class="tdBotones">					
		<% if (modoAnterior!=null && (modoAnterior.equalsIgnoreCase("ver") || modoAnterior.equalsIgnoreCase("nuevo") || modoAnterior.equalsIgnoreCase("nuevaSociedad"))) { %>
				&nbsp;
		<% } else { %>
				<input type='button' id="botonNuevo" onclick='return nuevo();' value='<siga:Idioma key="general.boton.new" />' alt='<siga:Idioma key="general.boton.new" />' class='button' />
		<% } %>
			</td>
		</tr>
	</table>

	<!-- FIN: CAMPOS DEL REGISTRO -->


	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>