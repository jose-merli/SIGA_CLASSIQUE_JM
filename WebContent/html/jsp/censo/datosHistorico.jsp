<!DOCTYPE html>
<html>
<head>
<!-- datosHistorico.jsp -->
<!-- 
	 Permite mostrar/editar las entradas del historial
	 VERSIONES:
	 miguel.villegas 22-12-2004 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	String remitente = request.getAttribute("modelo").toString(); // Obtengo la operacion (consulta,modificar o insertar)a realizar

	// Datos del usuario
	String nombre=(String)request.getSession().getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getSession().getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona		

	ArrayList vSel = new ArrayList(); // 
    Row row = new Row(); // 
    Row row_cons = new Row(); // 

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if ((remitente.equalsIgnoreCase("modificar"))||(remitente.equalsIgnoreCase("consulta"))){
		// Informacion sobre registro CEN_HISTORICO
		Enumeration def = ((Vector)request.getAttribute("container")).elements();
		// Tiene que existir y solo uno además, sino no es posible modificacion o consulta
		if (def.hasMoreElements()){
			row = (Row) def.nextElement(); 			              	
			vSel.add(row.getString(CenHistoricoBean.C_IDTIPOCAMBIO));				              	
		}  	
		if (remitente.equalsIgnoreCase("consulta")){
			Enumeration def_cons = ((Vector)request.getAttribute("container_desc")).elements();
			// Tiene que existir y solo uno además, sino no es posible modificacion o consulta
			if (def_cons.hasMoreElements()){
				row_cons = (Row) def_cons.nextElement(); 			              	
			}  			
		}
	}			
	String	botones="C,Y,R";

	String nombreUsuMod = "";
	try { 	
		if (remitente.equalsIgnoreCase("insertar")) {
			UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
			Hashtable h = userBean.getDatosUsuario ();
			if (h != null) {
				nombreUsuMod = (String)h.get("NOMBRE_USUARIO");		
			}
		}
		else {
			Vector v = (Vector)request.getAttribute("container");
			nombreUsuMod = ((Row)v.get(0)).getString("NOMBRE_USU_MOD");
			if (nombreUsuMod == null) nombreUsuMod = new String ("");
		}
	}
	catch (Exception e) {}
%>	


<!-- HEAD -->
	

			<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="HistoricoForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	</head>

	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.busquedaHistorico.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>
				    <%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalMedia" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposMedia"  align="center">

				<html:form action="/CEN_Historico.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = "<%=remitente%>"/>

					<tr>				
						<td>
						<siga:ConjCampos leyenda="censo.consultaHistorico.cabecera">

							<table class="tablaCampos" align="center"  border="0">
	
								<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.consultaHistorico.literal.nombreUsu"/>
										</td>	
										<td class="boxConsulta" colspan="3">
											<%=nombreUsuMod%>
										</td>	
									</tr>	
								<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.consultaHistorico.literal.tipo"/>&nbsp;(*)
										</td>				
										<td colspan="3">
											<% if (remitente.equalsIgnoreCase("insertar")){%>
												<siga:ComboBD nombre = "cmbCambioHistorico" tipo="cmbCambioHistorico" clase="boxCombo" obligatorio="true"/>
											<% } else { %>
												 <% if (remitente.equalsIgnoreCase("modificar")){ %>
													<% if (vSel.isEmpty()){int i=0;%>
														<siga:ComboBD nombre = "cmbCambioHistorico" tipo="cmbCambioHistorico" clase="boxConsulta" elementoSel="<%=i%>" obligatorio="true" readOnly="true"/>
													<% } else {%>
														<siga:ComboBD nombre = "cmbCambioHistorico" tipo="cmbCambioHistorico"  clase="boxConsulta" ancho="500" elementoSel="<%=vSel%>" obligatorio="true" readOnly="true"/>
													<% } %>											
												 <% } else{ %>	
													<html:text property="tipoCambioDesc" size="75" styleClass="boxConsulta" value="<%=row_cons.getString(CenTipoCambioBean.C_DESCRIPCION)%>" readOnly="true"></html:text>
												 <% } %>
											<% } %>								
										</td>	
									</tr>	
									
								<!-- FILA -->
									<tr>														
										<td class="labelText" width="25%">
											<siga:Idioma key="censo.consultaHistorico.literal.fechaEntrada"/>&nbsp;(*)
										</td>
										<td width="25%">							
											<% if (remitente.equalsIgnoreCase("insertar")){ %>
												<siga:Fecha  nombreCampo= "fechaEntrada"  posicionX="150" posicionY="50"/>
											<% } else { %>
					  							<% if (remitente.equalsIgnoreCase("modificar")){ %>
			  										<% String fecha=GstDate.getFormatedDateShort("",row.getString(CenHistoricoBean.C_FECHAENTRADA)); %>
			  										<siga:Fecha  nombreCampo= "fechaEntrada" valorInicial="<%=fecha%>"  posicionX="150" posicionY="50"/>
			  															
						  					 	<% } else { %>
											 		<% String fecha=GstDate.getFormatedDateShort("",row.getString(CenHistoricoBean.C_FECHAENTRADA));%>	
													<siga:Fecha  nombreCampo= "fechaEntrada" valorInicial="<%=fecha%>" disabled="true"  posicionX="150" posicionY="50"/>
											 	<% } %>				  					 
							  				<% } %>														
										</td>
									</tr>														
									<tr>														
										<td class="labelText" width="25%">
											<siga:Idioma key="censo.consultaHistorico.literal.fechaEfectiva"/>&nbsp;(*)
										</td>
										<td width="25%">							
											<% if (remitente.equalsIgnoreCase("insertar")){%>
												<siga:Fecha  nombreCampo= "fechaEfectiva"  posicionX="150" posicionY="50"/>
																		
						  					<% } else { %>
			  									<% if (remitente.equalsIgnoreCase("modificar")){ %>
			  										<% String fecha=GstDate.getFormatedDateShort("",row.getString(CenHistoricoBean.C_FECHAEFECTIVA));%>
			  										<siga:Fecha  nombreCampo= "fechaEfectiva" valorInicial="<%=fecha%>"  posicionX="150" posicionY="50"/>
			  																
				  							 	<% }else{ %>
									 				<% String fecha=GstDate.getFormatedDateShort("",row.getString(CenHistoricoBean.C_FECHAEFECTIVA));%>	
													<siga:Fecha  nombreCampo= "fechaEfectiva" valorInicial="<%=fecha%>" disabled="true"  posicionX="150" posicionY="50"/>
												 <% } %>				  					 				  					 
				  							<% } %>														
										</td>
									</tr>					
									<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.consultaHistorico.literal.motivo"/>&nbsp;(*)
										</td>				
										<td colspan="3">	
										
										<% if (remitente.equalsIgnoreCase("insertar")){ %>
 												<html:textarea property="motivo"
 													onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
 													style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;" 
 												 	styleClass="box"></html:textarea> 
										<% } else { %>
					  						<% if (remitente.equalsIgnoreCase("modificar")){%>
			  						        	<html:textarea property="motivo"
													onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"  
													style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;"
													styleClass="box" value="<%=row.getString(CenHistoricoBean.C_MOTIVO)%>"></html:textarea> 
			  							 	<% } else{ %>
  				  							   	<html:textarea property="motivo"
  				  							   		style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;" 
   				  							 	   	styleClass="boxConsulta" value="<%=row.getString(CenHistoricoBean.C_MOTIVO)%>" readOnly="true" ></html:textarea> 
												<% } %>				  					 				  					 				  					 
							  	    	<% } %>				
																						
						  				</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.consultaHistorico.literal.descripcion"/>
										</td>
										<td colspan="3">	
											<textArea 
												onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"
												style="overflow-y:auto; overflow-x:hidden; width:500px; height:80px; resize:none;"
												class="boxConsulta"  readonly=""><% if(row.getString(CenHistoricoBean.C_DESCRIPCION) != null) out.print(row.getString(CenHistoricoBean.C_DESCRIPCION));%></textarea>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>	
			</table>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=remitente%>'  modal="M"/>
			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">


			//Asociada al boton GuardarCerrar
			function accionGuardarCerrar() 
			{					
				sub();
				if (validateHistoricoForm(document.HistoricoForm)){
					if (compararFecha(document.forms[0].fechaEntrada, document.forms[0].fechaEfectiva) == 1) {
						alert ("mensaje.error.Fechas = Fecha efectiva no puede ser menor que la fecha de entrada");
						fin();
						return false;
					}
					else{
						document.forms[0].submit();		
					}
				}else{
					fin();
					return false;
				}	
			}

			//Asociada al boton Cerrar
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			//Asociada al boton Restablecer
			function accionRestablecer() 
			{		
				document.forms[0].reset();
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

