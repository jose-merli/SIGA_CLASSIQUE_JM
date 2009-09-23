<!-- destinatariosIndividuales.jsp -->
<!-- VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
     Utilizando tags pinta una lista con cabeceras fijas
     VERSIONES:
	yolanda.garcia 22-12-2004 Creación
-->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="com.siga.beans.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
		
	Vector vDatosDInd = (Vector)request.getAttribute("datosDInd");
	request.removeAttribute("datosDInd");	

	String editable = (String)ses.getAttribute("editable");
	boolean bEditable = true;
	
	if (editable!=null && !editable.equals(""))
	{
		bEditable = editable.equals("1");
	}
%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DestinatariosIndividualesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionConceptos.destIndividuales.cabecera" 
			localizacion="facturacion.destinatariosIndividuales.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<script>
			function refrescarLocal()
			{
				document.forms[0].target="_self";
			 	document.forms[0].modo.value="abrir";
			 	document.forms[0].submit();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_DestinatariosIndividuales.do" method="POST" style="display:none">
			<html:hidden property="modo" value=""/>
			
			<html:hidden property="idPersona" value=""/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
			
			
			<%
			if (vDatosDInd==null || vDatosDInd.size()==0)
			{
			%>
					<siga:TablaCabecerasFijas 
	   					nombre="tabladatos"
	   					borde="1"
	   					clase="tabletitle"
	   					nombreCol="facturacion.destinatariosIndividuales.literal.clientes"
	   					tamanoCol="100"
		   			alto="100%"
		   			ajusteBotonera="true"		
					>
			<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>			
					</siga:TablaCabecerasFijas>
			<%
			}
			else
			{
				if (!bEditable)
				{
				%>
					<siga:TablaCabecerasFijas 
	   					nombre="tabladatos"
	   					borde="1"
	   					clase="tabletitle"
	   					nombreCol="facturacion.destinatariosIndividuales.literal.clientes"
	   					tamanoCol="100"
		   			alto="100%"
		   			ajusteBotonera="true"		
					>
	   					
	   					<!-- INICIO: ZONA DE REGISTROS -->
						<!-- Aqui se iteran los diferentes registros de la lista -->
				
						<%
						for (int i=0; i<vDatosDInd.size(); i++)
			   			{
							Hashtable miHash = (Hashtable)vDatosDInd.elementAt(i);
							%>
					
							<tr class="listaNonEdit">
								<td>
									<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDPERSONA")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("IDSERIEFACTURACION")%>">
									<%=miHash.get("NOMBRECLIENTE")+" "+miHash.get("APELLIDOS1")+" "+miHash.get("APELLIDOS2")%>
								</td>
							</tr>
						<%}%>
						
						<!-- FIN REGISTRO -->
						<!-- FIN: ZONA DE REGISTROS -->	
					</siga:TablaCabecerasFijas>
				<%} 
				else 
				{
				%>
					<siga:TablaCabecerasFijas 
		   				nombre="tabladatos"
	   					borde="1"
	   					estilo=""
	   					clase="tabletitle"
	   					nombreCol="facturacion.destinatariosIndividuales.literal.clientes,"
	   					tamanoCol="90,10"
		   			alto="100%"
		   			ajusteBotonera="true"		
						>
	   					
	   					<!-- INICIO: ZONA DE REGISTROS -->
						<!-- Aqui se iteran los diferentes registros de la lista -->
				
						<%
						for (int i=0; i<vDatosDInd.size(); i++)
			   			{
							Hashtable miHash = (Hashtable)vDatosDInd.elementAt(i);
							%>
						
							<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="B" clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" >
								<td>
									<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDPERSONA")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("IDSERIEFACTURACION")%>">
									<%=miHash.get("NOMBRECLIENTE")+" "+miHash.get("APELLIDOS1")+" "+miHash.get("APELLIDOS2")%>
								</td>
							</siga:FilaConIconos>
							<%}%>
						
						<!-- FIN REGISTRO -->
						<!-- FIN: ZONA DE REGISTROS -->	
					</siga:TablaCabecerasFijas>
				<%}
			}%>
								

		
		<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
   			<input type="hidden" name="actionModal" value="">
   			<input type="hidden" name="modo" value="abrirBusquedaModal">
			<input type="hidden" name="clientes"	value="1">
		</html:form>
		
		<!-- FIN: LISTA DE VALORES -->
		
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->
	
		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->

			<%if (!bEditable){%>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
			<%} else {%>
				<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" />
			<%}%>

		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{	
				var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
				if (resultado!=undefined && resultado[0]!=undefined ){
					document.forms[0].idPersona.value = resultado[0];
					document.forms[0].modo.value = "insertar";
					document.forms[0].target = "submitArea";
					document.forms[0].submit();
				}
			}

			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
