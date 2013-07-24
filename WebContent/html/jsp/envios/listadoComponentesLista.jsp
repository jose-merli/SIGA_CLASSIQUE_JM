<!DOCTYPE html>
<html>
<head>
<!-- listadoComponentesLista.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL GRANDE -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

	Hashtable dataBackup = (Hashtable)request.getSession().getAttribute("DATABACKUP");	
	String editable = (String)dataBackup.get("editable");
	boolean bEditable = editable.equals("1")? true : false;
	String estilo,lectura,botonGuardar,botonNuevo;
	if (bEditable){
		estilo = "box"; lectura = "false";	botonGuardar = "G"; botonNuevo = "V,N";
	} else {
		estilo = "boxConsulta"; lectura = "true";	botonGuardar = ""; botonNuevo = "V";
	}
		
	
	
	
	String modo=request.getParameter("modo");
	
	
	request.removeAttribute("datos");	
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="ListaCorreosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="envios.listas.literal.titulo" 
		localizacion="envios.listas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad = "ajusteAltoBotones('resultado');cargarListado();">

	<!-- INICIO: DATOS GENERALES-->	
	

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">

	<table  class="tablaCampos"  align="center">

	<html:form action="/ENV_ListaCorreos.do?" method="GET" target="resultado">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "actionModal" value = ""/>
	
	<html:hidden property = "campoLista" value = ""/>
	<html:hidden property = "campoDinamica" value = ""/>
	<html:hidden property = "buscarListas" value = ""/>
	
	<html:hidden property = "idPersona" value = ""/>	
	<html:hidden property = "numColegiado" value = ""/>	
	<html:hidden property = "nif" value = ""/>	
	<html:hidden property = "nombreCliente" value = ""/>	
	<html:hidden property = "primerApellido" value = ""/>	
	<html:hidden property = "segundoApellido" value = ""/>	
	
	
				
		<tr>				
			<td class="labelText" width="7%">
				<siga:Idioma key="envios.listas.literal.lista"/>&nbsp;(*)
			</td>				
			<td width="30%">
				<html:text name="ListaCorreosForm" property="nombre" size="30" maxlength="30" styleClass="<%=estilo%>"></html:text>
			</td>		
		
			<td class="labelText"  width="7%">
				<siga:Idioma key="envios.listas.literal.descripcion"/>					
			</td>
			<td>
				<html:text name="ListaCorreosForm" property="descripcion" size="60" maxlength="100" styleClass="<%=estilo%>"></html:text>
			</td>
		</tr>		
	</html:form>
	</table>

	</siga:ConjCampos>
		

	<!-- FIN: DATOS GENERALES -->	

		<siga:ConjBotonesAccion botones="<%=botonGuardar%>"  clase="botonesSeguido"/>
		

	<!-- INICIO: SCRIPT BOTON GUARDAR -->
	<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		
		<!-- Asociada al boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado!=null&&resultado.indexOf('%')!=-1) {
				ListaCorreosForm.tablaDatosDinamicosD.value=resultado;
				ListaCorreosForm.modo.value="editar";
				ListaCorreosForm.submit();
			}
		}
			
	</script>
	<!-- FIN: SCRIPT BOTON GUARDAR -->

	<!-- INICIO: IFRAME LISTA COMPONENTES -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA COMPONENTES -->

<!-- INICIO: BOTONES REGISTRO -->


		<siga:ConjBotonesAccion botones="<%=botonNuevo%>" clase="botonesDetalle" />
	<!-- FIN: BOTONES REGISTRO -->
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function refrescarLocal()
		{	
			cargarListado();
		}

	<!-- Asociada al boton Volver -->
		function accionVolver() 
		{				
			//Recupero los datos de b�squeda de sesion(DataBackup)
			<%
			String campoLista="", campoDinamica="";
			Hashtable datosBusq = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			if (datosBusq!=null){
				campoLista = (String)datosBusq.get("campoLista");
				campoDinamica = (String)datosBusq.get("campoDinamica");
			}
			%>
			document.forms[0].campoLista.value="<%=campoLista%>";
			document.forms[0].campoDinamica.value="<%=campoDinamica%>";				
			document.forms[0].action = "<%=app%>/ENV_ListaCorreos.do";
			document.forms[0].modo.value="abrir";
			document.forms[0].buscarListas.value="true";			
			document.forms[0].target = "mainWorkArea";
			document.forms[0].submit();
		}
		
	<!-- Asociada al boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].modo.value = "insertarComponente";			
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");			
			
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value=resultado[0];
				document.forms[0].numColegiado.value=resultado[2];
				document.forms[0].nif.value=resultado[3];
				document.forms[0].nombreCliente.value=resultado[4];
				document.forms[0].primerApellido.value=resultado[5];
				document.forms[0].segundoApellido.value=resultado[6];
				
				var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;			   	
			}
			
		}		
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{			
			sub();
			if (validateListaCorreosForm(document.ListaCorreosForm)){
				var aux = document.forms[0].target;
				document.forms[0].target="submitArea";
				document.forms[0].modo.value="modificar";
				document.forms[0].submit();			
				document.forms[0].target=aux;
			}else{
				fin();
			}
		}
		
		<!-- Funcion que carga la lista de componentes -->
		function cargarListado() 
		{
			document.forms[0].modo.value="listadoComponentes";
			document.forms[0].modo.target="resultado";
			document.forms[0].submit();
		}
		
	</script>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
