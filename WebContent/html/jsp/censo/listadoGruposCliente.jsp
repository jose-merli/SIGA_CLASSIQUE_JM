<!-- listadoGruposCliente.jsp -->
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
<%@ page import="com.siga.beans.CenGruposClienteBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector vGrupos = (Vector)request.getAttribute("GRUPOSFIJOS");
	
	GruposClienteClienteForm miform = (GruposClienteClienteForm)request.getAttribute("GruposClienteClienteForm");
	
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

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	
	<script>

		function refrescarLocal() {
			document.forms[0].target="_self";
			document.forms[0].modo.value="buscar";
			document.forms[0].submit();				
		}
		
		//Asociada al boton Nuevo
		function nuevo() {	
			document.forms[0].modo.value="nuevo";
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado) {
				refrescarLocal();
			}
		}	
		
		function validaTabla(){
		  if (document.all.tablaDatos.clientHeight < document.all.tablaDatosDiv.clientHeight) {
		   document.all.tablaDatosCabeceras.width='100%';
		  }
		  else {
		   document.all.tablaDatosCabeceras.width='95%';
		  }
		}
	</script>
	
</head>

<body onload="validaTabla();">

	<!-- Comienzo del formulario con los campos -->
	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="submitArea" style="display:none">
		<html:hidden property="modo" value="insertar"/>
		<html:hidden property="idPersona" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="modoAnterior" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

	<!-- INICIO TABLA DE GRUPOS -->
		<siga:TablaCabecerasFijas 
		   	nombre="tablaDatos"
		   	borde="0"
   			estilo=""
		   	clase="tableTitle"
		  	nombreCol="censo.busquedaClientesAvanzada.literal.grupoCliente,"
		  	tamanoCol="80,20"
		   alto="100%"
		   ajusteBotonera="true"
		     modal="P">  

 	<% if(vGrupos==null || vGrupos.isEmpty())	{ %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } else {	 
 			Enumeration en = vGrupos.elements();		
 			int i=0;  									
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
	%> 				
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' modo='<%=modoAnterior%>' clase="listaNonEdit" visibleEdicion="no" visibleConsulta="no" pintarEspacio="false" >
				<td><input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=(String)htData.get(CenGruposClienteBean.C_IDGRUPO)%>'>	
					<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=(String)htData.get("IDINSTITUCION_GRUPO")%>'>	
  					<%=UtilidadesString.mostrarDatoJSP(htData.get(CenGruposClienteBean.C_NOMBRE))%>
  				</td>
				</siga:FilaConIconos>

    <% } } %>  			
  		</siga:TablaCabecerasFijas>
 	<!-- FIN TABLA DE GRUPOS -->		
  			


	<table class="botonesDetalle">
		<tr>
			<td class="tdBotones">					
		<% if (modoAnterior!=null && (modoAnterior.equalsIgnoreCase("ver") || modoAnterior.equalsIgnoreCase("nuevo") || modoAnterior.equalsIgnoreCase("nuevaSociedad"))) { %>
		&nbsp;
		<% } else { %>
		<input type='button' name="idButton"  id="botonNuevo" onclick='return nuevo();' value='<siga:Idioma key="general.boton.new" />' alt='<siga:Idioma key="general.boton.new" />' class='button' />
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