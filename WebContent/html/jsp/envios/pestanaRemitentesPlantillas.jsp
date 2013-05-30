<!-- pestanaRemitentesPlantillas.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%  
	String app = request.getContextPath();
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvDefinirPlantilla");
	if (busquedaVolver==null) busquedaVolver="";
	
	//Recupero el nombre y tipo del envio
	String nombrePlantilla   = (String)request.getAttribute("nombrePlantilla");
	String tipo              = (String)request.getAttribute("tipo");
	String idTipoEnvio       = (String)request.getAttribute("idTipoEnvio");
	String idPlantillaEnvios = (String)request.getAttribute("idPlantillaEnvios");
	String acuseRecibo = (String)request.getAttribute("acuseRecibo");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	
	request.removeAttribute("datos");	
	
	
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				  var cliente = ventaModalGeneral("busquedaClientesModalForm","G");			
				  if (cliente!=undefined && cliente[0]!=undefined ){
						document.forms[0].modo.value="nuevo";
						document.forms[0].idPersona.value=cliente[0];
						document.forms[0].idInstitucion.value=cliente[1];
						document.forms[0].numColegiado.value=cliente[2];
						document.forms[0].nifcif.value=cliente[3];
						document.forms[0].nombre.value=cliente[4];
						document.forms[0].apellidos1.value=cliente[5];
						document.forms[0].apellidos2.value=cliente[6];
						
						var result = ventaModalGeneral("RemitentesPlantillasForm","G");
						if (result == "MODIFICADO"){
							refrescarLocal();
						}
					}	
			}
			function accionGuardar() 
			{		
				if(document.getElementById("idCheckAcuseRecibo").checked)
					document.DefinirPlantillasEdicion.acuseRecibo.value = "1";
				else
					document.DefinirPlantillasEdicion.acuseRecibo.value = "0";
						
				document.DefinirPlantillasEdicion.submit();
	
			}
			function inicio(){
				if(document.getElementById("valorAcuseRecibo").value=="1")
					document.getElementById("idCheckAcuseRecibo").checked = "checked";
				else
					document.getElementById("idCheckAcuseRecibo").checked = "";
					
			}
	

			function refrescarLocal()
			{			
				document.location.reload();			
			}
		</script>
		
		
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo titulo="envios.definirEnvios.remitentes.cabecera"  
					localizacion="envios.definirTiposPlantillas.localizacion" />
		
	</head>
	
	<body onload="inicio();">
	
	<table class="tablaTitulo" align="center" cellspacing="0">
			<html:form  action="/ENV_RemitentesPlantilla.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "accion" value = ""/>
				
				<html:hidden property = "idPlantillaEnvios" value = "<%=idPlantillaEnvios%>"/>
				<html:hidden property = "idTipoEnvios" value = "<%=idTipoEnvio%>"/>
				<html:hidden property = "idPersona" value = ""/>
				<html:hidden property = "idInstitucion" value = ""/>
				<html:hidden property = "numColegiado" value = ""/>
				<html:hidden property = "nifcif" value = ""/>
				<html:hidden property = "nombre" value = ""/>
				<html:hidden property = "apellidos1" value = ""/>
				<html:hidden property = "apellidos2" value = ""/>
				<html:hidden property = "descripcion" value = ""/>
				
				<html:hidden property = "domicilio" value = ""/>
				<html:hidden property = "cp" value = ""/>
				<html:hidden property = "idPoblacion" value = ""/>
				<html:hidden property = "idProvincia" value = ""/>
				<html:hidden property = "idPais" value = ""/>
				<html:hidden property = "fax1" value = ""/>
				<html:hidden property = "fax2" value = ""/>
				<html:hidden property = "correoElectronico" value = ""/>

		</html:form>

				
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.plantillas.literal.plantilla"/> :&nbsp;<%=nombrePlantilla%> 				    
			</td>
		</tr>
		
		
	</table>
	<table>
	
	<tr>
			<td class="labelText"><siga:Idioma key="envios.definir.literal.acuseRecibo"/></td>
			<td >
				 <input type="checkbox" id="idCheckAcuseRecibo">				    
				 <input type="hidden" id="valorAcuseRecibo" value=<%=acuseRecibo%>>
			</td>
		</tr>
	</table>
				
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.listas.literal.nombreyapellidos,
		   		  					 gratuita.retencionesIRPF.literal.descripcion,
		   		  					 censo.fichaCliente.literal.colegiado,
		   		  					 censo.busquedaClientes.literal.nif,"
		   		  columnSizes="30,40,10,10,10"
		   		  modal="g">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}

				else
				{  String sBotones = bEditable ? "C,E,B" : "C";
				for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" clase="listaNonEdit" >
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDPERSONA")%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idPlantillaEnvios%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=idTipoEnvio%>"/>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBREYAPELLIDOS"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("DESCRIPCION"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NCOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NIFCIF"))%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>


		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="G,V,N" clase="botonesDetalle"  />
		
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	<html:form action="/ENV_DefinirPlantillas" name="DefinirPlantillasEdicion" type="com.siga.envios.form.SIGAPlantillasEnviosForm" method="POST" target="submitArea">
		<html:hidden property="modo" value="modificar"/>
		<html:hidden property="idPlantillaEnvios" value = "<%=idPlantillaEnvios%>"/>
		<html:hidden property="idTipoEnvios" value = "<%=idTipoEnvio%>"/>
		<html:hidden property="acuseRecibo" value=""/>
		
		
	</html:form>
	
	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		