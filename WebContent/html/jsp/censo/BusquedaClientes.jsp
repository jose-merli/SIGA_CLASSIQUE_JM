<!-- BusquedaClientes.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
 
<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>	
	
<%  
	// locales
	BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
	formulario.setChkBusqueda("on");
	// datos seleccionados Combo
	ArrayList colegioSel = new ArrayList();
	colegioSel.add(formulario.getNombreInstitucion());

	ArrayList tipoSel = new ArrayList();
	if (formulario!=null && formulario.getTipo()!=null && !formulario.getTipo().equals(""))
		tipoSel.add(formulario.getTipo());

	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	
	/*String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscarPaginador()";
	}*/
	
	// colegiado
	String colegiado = formulario.getColegiado();
	if (colegiado==null) colegiado="";
	String titu = "";
	String loca = "censo.busquedaClientes.localizacion";
	String busc = "";
	if (colegiado.equals(ClsConstants.DB_TRUE)) {
		//colegiados
		titu = "censo.busquedaClientes.colegiados.titulo";
		busc = "censo.busquedaClientes.colegiados.titulo";
	} else {
	   if (colegiado.equals(ClsConstants.DB_FALSE)){
		//no colegiados
		titu = "censo.busquedaClientes.noColegiados.titulo";
		busc = "censo.busquedaClientes.noColegiados.titulo";
	   }else{
	    //letrados
	    titu = "censo.busquedaClientes.letrados.titulo";
		busc = "censo.busquedaClientes.letrados.titulo"; 
	   }	
	}

	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("CenInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	/*String parametro[] = new String[1];
	parametro[0] = institucionesVisibles;*/
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();

	// MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion
	// Obtengo el UserBean y en consecuencia la institucion a la que pertenece y su nombre
	
	String institucionAcceso=user.getLocation();
	String nombreInstitucionAcceso="";
	if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){
		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
		nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(institucionAcceso);
	}
	
	String nColegiado=(String)request.getAttribute("nColegiado");
	String idInstitucion=(String)request.getAttribute("idInstitucion");
	String nifcif=(String)request.getAttribute("nifcif");

%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=loca%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="busquedaClientesForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="inicio();ajusteAlto('resultado');">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">

	<table class="tablaCampos" align="center">

	<html:form action="/CEN_BusquedaClientes.do?noReset=true" method="POST" target="resultado">
	<html:hidden name="busquedaClientesForm" property = "modo" value = ""/>
	<html:hidden property="seleccionarTodos" />
	<input type="hidden" name="limpiarFilaSeleccionada" value="">

	<!-- parametro para colegiados o no -->
	<html:hidden name="busquedaClientesForm" property = "colegiado"  value="<%=colegiado%>"/>
	<html:hidden name="busquedaClientesForm" property = "avanzada" />
	<html:hidden name="busquedaClientesForm" property = "valorCheck" />
<% if (!colegiado.equals("2")) { %>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
		
	</td>				
	<td colspan="4">
		<!-- MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion -->
		<% if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){ 
		    
			%>
			<html:hidden name="busquedaClientesForm" property = "nombreInstitucion" value = "<%=institucionAcceso%>"/>
			<html:text property="" styleClass="boxConsulta" size="80" value='<%=nombreInstitucionAcceso%>' readOnly="true"></html:text>
		<% }else{ 
		   
		%>
		     	<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>
		     	  <siga:ComboBD nombre = "nombreInstitucion" tipo="cmbNombreColegiosTodos" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=colegioSel %>"/>
				<% }else{ %>
					<siga:ComboBD nombre = "nombreInstitucion" tipo="cmbInstitucion" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=colegioSel %>"/>	
				<% } %>
			
		<% } %>
	</td>
	</tr>
	<!-- FILA -->
	<tr>				
<%}%>

<% if (colegiado.equals(ClsConstants.DB_TRUE)) { %>
		<!-- -->
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
		</td>
		<td>
			<html:text name="busquedaClientesForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box"></html:text>
		</td>
		<td class="labelText">
		  <siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda"/>
		</td>
		<td>
		 
		 <html:checkbox  name="busquedaClientesForm" property="chkBusqueda"  />
		
		</td>
		<td class="labelText">
			&nbsp;
		</td>
		<td>
			<input type="hidden" name="tipoCliente">
		</td>
<% } else { 
      if (colegiado.equals(ClsConstants.DB_FALSE)){%>

	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.tipoCliente"/>
	</td>
	<td>
		<!-- TIPO -->
		<siga:ComboBD nombre = "tipo" tipo="cmbTiposNoColegiadoBusqueda" clase="boxCombo" obligatorio="false" elementoSel="<%=tipoSel %>"/>
<!--
		<html:select name="busquedaClientesForm" property="tipo" styleClass="boxCombo">
				<html:option value=""></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_SOCIEDAD_SERVICIOS_JURIDICOS%>" key="censo.general.literal.sociedadSJ"></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_SOCIEDAD_CIVIL%>" key="censo.general.literal.SociedadCivil"></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_SOCIEDAD_LIMITADA%>" key="censo.general.literal.SociedadLimitada"></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_SOCIEDAD_ANONIMA%>" key="censo.general.literal.SociedadAnonima"></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_PERSONAL%>" key="censo.general.literal.Personal"></html:option>
				<html:option value="< %=ClsConstants.COMBO_TIPO_OTROS%>" key="censo.general.literal.Otros"></html:option>
		</html:select>
-->
	</td>
		 <td class="labelText">
		  <siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda"/>
		</td>
		<td>
		 
		 <html:checkbox name="busquedaClientesForm" property="chkBusqueda"  />
		
		</td>
		<!-- -->
		<td class="labelText">
			&nbsp;
		</td>
		<td>
			<input type="hidden" name="numeroColegiado">
		</td>
<%  } 
 }%>


	</tr>
	<!-- FILA -->
	<%if (colegiado.equals("2")){%>	
	<tr>
	  <td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.noResidente"/>
	  </td>
	  <td align="left">
		<html:select name="busquedaClientesForm" property="residente" styleClass="boxCombo">
			<html:option value="0">&nbsp;</html:option>
			<html:option value="<%=ClsConstants.COMBO_SIN_RESIDENCIA%>" key="censo.busquedaClientes.sinResidencia"></html:option>
			<html:option value="<%=ClsConstants.COMBO_RESIDENCIA_MULTIPLE%>" key="censo.busquedaClientes.residenciaMultiple"></html:option>
		</html:select>
	  </td>
	   <td class="labelText">
		  <siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda"/>
		</td>
		<td>
		 <html:checkbox  name="busquedaClientesForm" property="chkBusqueda"  />
		
		</td>

	</tr>
<%}%>	
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
	</td>
	<td>
		<html:text name="busquedaClientesForm" property="nif" size="15" styleClass="box"></html:text>
	</td>

	<td class="labelText">
		<% if (colegiado.equals(ClsConstants.DB_TRUE)||colegiado.equals("2")) { %>
		<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
		<% } else { %>
		<siga:Idioma key="censo.busquedaClientes.literal.nombreDenominacion" />
		<% } %>
	</td>				
	<td>
		<html:text name="busquedaClientesForm" property="nombrePersona" size="30" styleClass="box"></html:text>
	</td>

	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
	</td>
	<td>
		<html:text name="busquedaClientesForm" property="apellido1" size="30" styleClass="box"></html:text>
	</td>


	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
	</td>
	<td>
		<html:text name="busquedaClientesForm" property="apellido2" size="30" styleClass="box"></html:text>
	</td>


	</tr>


	</html:form>
	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
<%  
	String botones = "B";
	if (colegiado.equals(ClsConstants.DB_FALSE)) {
		botones += ",A,N,NS";
		//botones += ",N,NS";
	}else{
	  if (colegiado.equals(ClsConstants.DB_TRUE)) {
	    botones +=",L,A";

	  }
	} 
%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="<%=busc%>" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscarPaginador() 
		{		
//			if (validateBusquedaClientesForm(document.forms[0])) 
			{  
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
		}
		function buscar(modo) 
		
		{
				sub();
				if (document.forms[0].chkBusqueda.checked){
		           document.forms[0].chkBusqueda.value="1";
				   document.forms[0].valorCheck.value="1";
		        }else{
				   document.forms[0].chkBusqueda.value="0";
				    document.forms[0].valorCheck.value="0";
				}
				if(modo)
					document.forms[0].modo.value = modo;
				else
		        	document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			
		}
		function seleccionarTodos(pagina) 
		{
			document.forms[0].seleccionarTodos.value = pagina;
			buscar('buscarPor');
				
		}		

		<!-- Funcion asociada a boton busqueda avanzada -->
		function buscarAvanzada() 
		{	
			sub();		
			document.forms[0].action="<%=app%>/CEN_BusquedaClientesAvanzada.do";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].chkBusqueda.checked=true;
			document.forms[0].nombrePersona.value="";
			document.forms[0].apellido1.value="";
			document.forms[0].apellido2.value="";
			document.forms[0].numeroColegiado.value="";
			document.forms[0].nif.value="";
			document.forms[0].modo.value="abrir";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}

		<!-- Funcion asociada a boton Nueva Sociedad -->
		function nuevaSociedad() 
		{		
			document.forms[0].modo.value="nuevaSociedad";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}		
		function inicio(){
		  
		  // Cuando venimos de las colegiaciones de letrados introducimos los siguientes criterios de busqueda por defecto:
			  <% if (nColegiado!=null && !nColegiado.equals("")) {%>
			     
			        busquedaClientesForm.numeroColegiado.value=<%=nColegiado%>;
				 
			   <%}%>
			
			   <% if (nifcif!=null && !nifcif.equals("")) {%>
			      
			       busquedaClientesForm.nif.value='<%=nifcif%>';
				 
			   <%}%>
			
			   <% if (idInstitucion!=null && !idInstitucion.equals("")) {%>
			          
			             busquedaClientesForm.nombreInstitucion.value=<%=idInstitucion%>;
						 
					 
					    
				 
			   <%}
			   
			  if (request.getParameter("buscar")!=null && request.getParameter("buscar").equals("1")){%>
// Cuando venimos de las colegiaciones de letrados
            
			    buscar();
			  <%}else if(request.getParameter("buscar")!=null && request.getParameter("buscar").equals("true")){%>
			  
			  
// Cuando volvemos a la paginaci�n
// Recuperamos si el check de busqueda estaba activado o no  
               if ( document.forms[0].valorCheck.value=="1"){
				     busquedaClientesForm.chkBusqueda.checked=true;
				 }else{
				      busquedaClientesForm.chkBusqueda.checked=false;
				 } 
			         buscarPaginador();
			  <%}%>	
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<% if (colegiado.equals("2")) {%>
	  <iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
<!--					style="position:absolute; width:964; height:350; z-index:2; top: 150px; left: 0px">-->
	  </iframe>
	<%}else{%>
	   <iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
<!--					style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
	  </iframe>
	<%}%>  
	

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->



</div>	

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>