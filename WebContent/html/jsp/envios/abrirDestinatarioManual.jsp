<!-- abrirDestinatarioManual.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Vector vDatos = (Vector)request.getAttribute("datos");
	// para saber hacia donde volver
	
	
	//Recupero el nombre y tipo del envio
	
	String botonesFila;
	request.removeAttribute("datos");	
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"> </script>

		<%--<siga:Titulo titulo="expedientes.tiposexpedientes.configuracion.literal.titulo" localizacion="expedientes.tiposexpedientes.configuracion.literal.localizacion"/>--%>
		
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		

				  var cliente = ventaModalGeneral("busquedaClientesModalForm","G");			
				  	
					if (cliente!=undefined && cliente[0]!=undefined ){
						document.RemitentesForm.modo.value="nuevo";
						document.RemitentesForm.idPersona.value=cliente[0];
						document.RemitentesForm.idInstitucion.value=cliente[1];
						document.RemitentesForm.numColegiado.value=cliente[2];
						document.RemitentesForm.nifcif.value=cliente[3];
						document.RemitentesForm.nombre.value=cliente[4];
						document.RemitentesForm.apellidos1.value=cliente[5];
						document.RemitentesForm.apellidos2.value=cliente[6];
						
						var result = ventaModalGeneral("RemitentesForm","G");
						if (result == "MODIFICADO"){
							document.location.reload();
						}			  		
					}	
			}
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
		</script>
		
		
		<!-- FIN: SCRIPTS BOTONES -->

		<siga:Titulo
			titulo="envios.definirEnvios.destIndividual.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
				
	</head>
	
	<body>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="busquedaVolver" name="busquedaVolver" scope="request"/>

			
<table class="tablaTitulo" align="center" cellspacing="0">			
	<tr>
		<td id="titulo" class="titulitosDatos">
			<c:out value="${titulo}"/> 				    
		</td>
	</tr>
</table>


<html:form  action="${path}" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersona" />
	<html:hidden property = "idInstitucion" />
	<html:hidden property = "numColegiado" />
	<html:hidden property = "nifcif" />
	<html:hidden property = "nombre" />
	<html:hidden property = "apellidos1" />
	<html:hidden property = "apellidos2" />
	<html:hidden property = "idEnvio" value="${idEnvio}"/>
	<html:hidden property = "idTipoEnvio" value="${idTipoEnvio}"/>
	<html:hidden property = "idTipoExpediente" value="${idTipoExpediente}"/>
	<input type="hidden" name="tablaDatosDinamicosD">
	<input type="hidden" name="actionModal" value="">
</html:form>
	
				
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="envios.listas.literal.nombreyapellidos,
		   		  					 censo.fichaCliente.literal.colegiado,
		   		  					 censo.busquedaClientes.literal.nif,"
		   		  tamanoCol="60,15,15,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  modal="g"
		   		  >

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}

				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row)vDatos.elementAt(i);
%>
			<% if(fila.getString("TIPODESTINATARIO").equalsIgnoreCase("CEN_PERSONA")){
				botonesFila="C,E,B"; 
			}else{
				botonesFila="C,B";
			}
			
			%>
	  		
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botonesFila %>" clase="listaNonEdit" visibleConsulta="no">
					<input type="hidden" name="idPersona_<%=""+(i+1)%>" value="<%=fila.getString("IDPERSONA")%>"/>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBREYAPELLIDOS"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NCOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NIFCIF"))%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>


		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"  />
		
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	
	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

	<script type="text/javascript">
		function selectRow(fila) {
		   document.getElementByName('filaSelD').value = fila;
		   var tabla;
		   tabla = document.getElementById('tablaDatos');
		   for (var i=0; i<tabla.rows.length; i++) {
		     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
		     else          tabla.rows[i].className = 'filaTablaImpar';
		   }
		   tabla.rows[fila].className = 'listaNonEditSelected';
		}
		 
		 function consultar(fila) {
		   document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
		   document.RemitentesForm.modo.value = "Ver";
		   ventaModalGeneral(document.RemitentesForm.name,"G");
		 }
		 
		 function editar(fila) {
		  
		   document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
		   document.RemitentesForm.modo.value = "Editar";
		   var resultado = ventaModalGeneral(document.RemitentesForm.name,"G");
		   if (resultado) {
		  	 	if (resultado[0]) {
		   		refrescarLocalArray(resultado);
		   	} else 
		   	if (resultado=="MODIFICADO")
		   	{
		      		refrescarLocal();
		   	}
		   }
		 }
		 
		 function borrar(fila) {
		   var datos;
			if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
		   	
		   	var auxTarget = document.RemitentesForm.target;
		   	document.RemitentesForm.target="submitArea";
		   	document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
		   	document.RemitentesForm.modo.value = "Borrar";
		   	document.RemitentesForm.submit();
		   	document.RemitentesForm.target=auxTarget;
		 	}
		 }
		</script>







	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		