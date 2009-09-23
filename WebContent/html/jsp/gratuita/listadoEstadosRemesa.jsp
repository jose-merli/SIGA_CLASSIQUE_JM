<!-- listadoEstadosRemesa.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.gratuita.form.EstadosRemesaForm"%>
<%@ page import="com.siga.beans.CenGruposClienteBean"%>
<%@ page import="com.siga.beans.CenActividadProfesionalBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector vGrupos = (Vector)request.getAttribute("ESTADOS");
	
	EstadosRemesaForm miform = (EstadosRemesaForm)request.getAttribute("EstadosRemesaForm");
	
	String modoAnterior = miform.getModoAnterior();

	String iconos = "B,E";
	String botones = "N";
	String alto = "70";
	
	String idEstado = "";
	
	if (modoAnterior==null || modoAnterior.equalsIgnoreCase("consultar") || modoAnterior.equalsIgnoreCase("ver") || modoAnterior.equalsIgnoreCase("nuevaSociedad")) {
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
			parent.refrescarLocal();				
		}
		
		<!-- Asociada al boton Nuevo -->
		function nuevo(idEstado) {	
			document.forms[0].modo.value="nuevo";
			document.forms[0].idEstado.value = idEstado;
			
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

<body>

	<!-- Comienzo del formulario con los campos -->
	<html:form action="/JGR_EstadosRemesa.do" method="POST" target="submitArea" style="display:none">
		<html:hidden property="modo" value="insertar"/>
		<html:hidden property="idRemesa" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="modoAnterior" />
		<html:hidden property="idEstado" />
			
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

	<!-- INICIO TABLA DE GRUPOS -->
		<siga:TablaCabecerasFijas 
		   	nombre="tablaDatos"
		   	borde="0"
   			estilo=""
		   	clase="tableTitle"
		  	nombreCol="gratuita.BusquedaRemesas_CAJG.literal.fechaEstado,gratuita.BusquedaRemesas_CAJG.literal.estado,"
		  	tamanoCol="30,50,20"
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
				idEstado = (String)htData.get("IDESTADO");
				//if (htData == null) continue;
				i++;
	%> 				
				
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=(i>1 && vGrupos.size()==i)?iconos:""%>' modo='<%=true?"consulta":modoAnterior%>' clase="listaNonEdit" visibleConsulta="no" pintarEspacio="false" >
				<td>
				
					<%=GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(htData.get("FECHAREMESA")))%>
				</td>
				<td>
					<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=(String)htData.get("IDREMESA")%>'>	
					<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=(String)htData.get("IDINSTITUCION")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(i)%>_3' value='<%=(String)htData.get("IDESTADO")%>'>	
					<input type='hidden' name='oculto<%=String.valueOf(i)%>_4' value='<%=GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(htData.get("FECHAREMESA")))%>'>	
  					<%=UtilidadesString.mostrarDatoJSP(htData.get("DESCRIPCION"))%>
  				</td>
  				
				</siga:FilaConIconos>

    <% } } %>  			
  		</siga:TablaCabecerasFijas>
 	<!-- FIN TABLA DE GRUPOS -->		
  			

	<!-- 
	<table class="botonesDetalle">
		<tr>
			<td class="tdBotones">					
		<% if ((modoAnterior!=null && (modoAnterior.equalsIgnoreCase("consultar") || modoAnterior.equalsIgnoreCase("nuevo") )) || idEstado.equals("0") || idEstado.equals("3")) { %>
		&nbsp;
		<% } else { %>
		<input type='button' id="botonNuevo" onclick='return nuevo("<%=idEstado%>");' value='<siga:Idioma key="general.boton.new" />' alt='<siga:Idioma key="general.boton.new" />' class='button' />
		<% } %>
		</td>
		</tr>
	</table>
 -->
	<!-- FIN: CAMPOS DEL REGISTRO -->


	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>