<!-- estados_Edit.jsp -->
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
<%@ page import="com.siga.beans.ExpEstadosBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector datos = (Vector)request.getAttribute("datos");
	Vector vPlazos = (Vector)request.getAttribute("vPlazos");
	String editable = (String)request.getAttribute("editable");
	boolean bEditable = editable.equals("1")? true : false;
	String modo=request.getParameter("modo");
	String fase =(String) request.getAttribute("fase");
	String estado =(String) request.getAttribute("estado");
	String estadoSiguiente =(String) request.getAttribute("estadoSiguiente");
	String[] parametros;
	request.removeAttribute("datos");	 
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="EstadosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script>
	
		function validarActivarAlarmas() {
			var auxMensaje=document.getElementById("mensaje");
			var auxDiasAntelacion=document.getElementById("diasAntelacion");
			var auxActivarAlertas=document.getElementById("activarAlertas");
			if (auxActivarAlertas.checked) {
				auxDiasAntelacion.readonly=false;
				jQuery("#diasAntelacion").removeAttr("disabled");				
				auxMensaje.readonly=false;
				jQuery("#mensaje").removeAttr("disabled");						
				auxMensaje.className="box";
				auxDiasAntelacion.className="box";
			} else {
				auxDiasAntelacion.readonly=true;
			   	jQuery("#diasAntelacion").attr("disabled","disabled");
				auxMensaje.readonly=true;
			   	jQuery("#mensaje").attr("disabled","disabled");
				auxMensaje.value="";
				auxDiasAntelacion.value="";
				auxMensaje.className="boxConsulta";
				auxDiasAntelacion.className="boxConsulta";
			}
		}
	
	</script>

</head>

<body onload="validarAncho_tablaDatos();validarActivarAlarmas();">

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.auditoria.literal.datosgenerales"/>
			</td>
		</tr>
	</table>	
	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralGrande" 
-->
<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposGrande"  align="center">

	<html:form action="/EXP_TiposExpedientes_Estados.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>

<% if(modo.equalsIgnoreCase("Nuevo"))  { %>
	<html:hidden property = "modo" value = "Insertar"/>

<% } else {%>
	<html:hidden property = "modo" value = "Modificar"/>

<% }%>
<%
		ExpEstadosBean bean = (ExpEstadosBean) datos.elementAt(0);
%>


<!-- ******************* Datos Generales ******************* -->

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">

		<table class="tablaCampos" align="center" border="0">
	
		<!-- FILA -->
		<tr>				
			<td class="labelText" >
				<siga:Idioma key="expedientes.auditoria.literal.fase"/>&nbsp(*)
			</td>				
			<td colspan="3">
				<% 
						parametros = new String[2];
					 	parametros[0] = bean.getIdInstitucion().toString();
					 	parametros[1] =bean.getIdTipoExpediente().toString();
											 	
						if(modo.equalsIgnoreCase("Nuevo")){ 	
				%>				 	
					<siga:ComboBD nombre = "idInst_idExp_idFase" tipo="cmbFases" clase="boxCombo" parametro="<%=parametros%>" ancho="300" obligatorio="true"/>
				<% } else { 
					    ArrayList faseSel =null;
						faseSel = new ArrayList ();
						String s_faseSel=parametros[0]+","+parametros[1]+","+bean.getIdFase().toString();
						faseSel.add(s_faseSel);
				%>
					<html:text name="EstadosForm" property="no_utilizado" size="30" maxlength="30" styleClass="boxConsulta" readonly="true" value="<%=fase%>"></html:text>
					<input type="hidden" name="idInst_idExp_idFase" value="<%=s_faseSel%>"/>
				<% } %>			
				<input type="hidden" name="idTipoExpediente" value="<%=bean.getIdTipoExpediente()%>"/>
				<input type="hidden" name="idInstitución" value="<%=bean.getIdInstitucion()%>"/>
				
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.estado"/>&nbsp(*)
			</td>				
			<td colspan="5">
				<% if (bEditable){ %>
					<html:text name="EstadosForm" property="estado" size="70" maxlength="60" styleClass="box" value="<%=estado%>"></html:text>
				<% } else { %>
					<html:text name="EstadosForm" property="estado" size="70" maxlength="60" styleClass="boxConsulta" readonly="true" value="<%=estado%>"></html:text>
				<% } %>
			</td>
		</tr>
		<tr>
			<td class="labelText" >
				<siga:Idioma key="expedientes.tiposexpedientes.literal.automatico"/>
			</td>
			<td >
				<html:checkbox name="EstadosForm" property="automatico" value="true" disabled="<%=!bEditable%>"/>
			</td>
			<td class="labelText" style="text-align: right">
				<siga:Idioma key="expedientes.auditoria.literal.ejecucionsancion"/>
			</td>
			<td >
				<html:checkbox name="EstadosForm" property="ejecucionSancion" value="true" disabled="<%=!bEditable%>"/>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.tiposexpedientes.literal.siguienteestado"/>
			</td>	
			<td>
			<% 
				parametros = new String[4];
			 	parametros[0] = bean.getIdInstitucion().toString();
			 	parametros[1] = bean.getIdTipoExpediente().toString();
			 	String siguienteestado="",siguientefase="";
			 	if(bean.getIdEstadoSiguiente()!=null) siguienteestado=bean.getIdEstadoSiguiente().toString();
			 	if(bean.getIdFaseSiguiente()!=null) siguientefase=bean.getIdFaseSiguiente().toString();
			 	
				ArrayList estadoSel =null;
				if(!modo.equalsIgnoreCase("Nuevo")){
					parametros[2] = bean.getIdEstado().toString();
					parametros[3] = bean.getIdFase().toString();
				 	estadoSel = new ArrayList ();
				 	estadoSel.add(parametros[0]+","+parametros[1]+","+siguientefase+","+siguienteestado); 
				 	
				 	String readOnly = "true", clase = "boxConsulta";
				 	if (bEditable) {
				 		readOnly = "false";
				 		clase = "boxCombo";
				 	} 
			%>
				 	<siga:ComboBD nombre = "idInst_idExp_idFase_idEstadoSig" tipo="cmbEstadosSiguientes" elementoSel="<%=estadoSel%>" clase="<%=clase%>" ancho="450" parametro="<%=parametros%>" obligatorio="false" readonly="<%=readOnly%>" />

			 <% } else { %>
					<siga:ComboBD nombre = "idInst_idExp_idFase_idEstadoSig" tipo="cmbEstadosSiguientesTodos" clase="boxCombo" parametro="<%=parametros%>" ancho="450" obligatorio="false"/>
			 <% } %>				
				
		</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.estadoFinal"/>
			</td>
			<td >
				<html:checkbox name="EstadosForm" property="estadoFinal" value="true" disabled="<%=!bEditable%>"/>
			</td>
		</tr>
		
		</table>
	</siga:ConjCampos>

	</td>
	</tr>
	

<!-- ******************* Plazos Clasificación  ******************* -->
	
	<tr >
	<td >
	
		<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.plazosclasificacion">
			
			
			<table width="60%" align="center" >
				<tr>
				<td  > 
	
			
			<!-- 1. Pintamos la cabecera de la tabla con los contenidos -->
			<table id='tablaDatosCabeceras' border='1' width='97.00%' cellspacing='0' cellpadding='0'>
			<tr class = 'tableTitle'>
				<td align='center' width='50%'>
					<siga:Idioma key="expedientes.auditoria.literal.clasificacion"/>
				</td>
				<td align='center' width='50%'>
					<siga:Idioma key="expedientes.auditoria.literal.plazo"/>
				</td>
			</tr>
			</table>
		
	
		<!-- 2. Pintamos el contenido de la tabla -->
		<div id='tablaDatosDiv' style='height:79px; width:100%; overflow-y:auto'>
		<table id='tablaPlazos' border='1'  width='100%' cellspacing='0' cellpadding='0'>
			<tr>
				<td align='center' width='50%'></td>
				<td align='center' width='50%'></td>				
			</tr>			
			
			<%
				if (vPlazos==null || vPlazos.size()==0)
				{
%>		<tr><td colspan="2" style="background-color:transparent; text-align:center">
		   		<p class="titulitos" ><siga:Idioma key="messages.noRecordFound"/></p>
			</td></tr>
<%
				} else {
			 		for (int i=0; i<vPlazos.size(); i++)
			   		{
			   			Row fila = (Row) vPlazos.elementAt(i);
				  		String clasificacion = fila.getString("CLASIFICACION");
				  		String plazo = fila.getString("PLAZO").trim();
				  		
				  		if(plazo.equalsIgnoreCase("NULO")) {
				  			plazo="0";
				  		}
				  		String idclasificacion=fila.getString("IDCLASIFICACION");
%>
					<tr>
						<td class="labelTextValue">
							<%=clasificacion%>
						</td>
						<td  align="center" >
		<% 			if (bEditable){ %>
							<input type='text'  name='idclasificacion_<%=idclasificacion%>'  size="6" maxlength="6" class="boxNumber" value="<%=plazo%>"></input>							
					<% } else { %>
							<html:text name="plazosForm" property="no_utilizado" size="6" maxlength="6" styleClass="boxConsulta" readonly="true" value="<%=plazo%>"></html:text>				
					<% } %>
						</td>
					</tr>
			   	<%}/*for*/%>
			   	
			<%}//else		%>	
			
		</table>
		</div>
	
	</td >
	
</tr >
	
<table><tr><td class="labelText"><siga:Idioma key="expedientes.tiposexpedientes.literal.notaclasificaciones"/></td></tr></table>
</siga:ConjCampos>


<!-- ******************* Entrada Estado ******************* -->

	<tr>
	<td>
		
		<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.entradaestado">
		<table  width="100%">
			<tr>
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.sancionado"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="sancionado"	size="1" value="<%=bean.getPreSancionado()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPreSancionado().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPreSancionado().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.visible"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
						<html:select property="preVisible"	size="1" value="<%=bean.getPreVisible()%>" styleClass="boxCombo">
							<html:option value="" key=""/>
							<html:option value="S" key="general.yes"/>
							<html:option value="N" key="general.no"/>
						</html:select>	
<% 				} else if (bean.getPreVisible().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPreVisible().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
						
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.visibleenficha"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="preVisibleFicha"	size="1" value="<%=bean.getPreVisibleFicha()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPreVisibleFicha().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPreVisibleFicha().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
				</td>
			</tr>
		</table>
		</siga:ConjCampos>
		
	</td>
	</tr>
	
	
	<!-- ******************* Salida Estado ******************* -->

	<tr>
	<td>
	
		<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.salidaestado">
		<table width="100%">
			<tr>
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.actuacionesprescritas"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="actPrescritas" size="1" value="<%=bean.getPostActPrescritas()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPostActPrescritas().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostActPrescritas().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.sancionprescrita"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="sancionPrescrita"	size="1" value="<%=bean.getPostSancionPrescrita()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPostSancionPrescrita().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostSancionPrescrita().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>	
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.sancionfinalizada"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="sancionFinalizada"	size="1" value="<%=bean.getPostSancionFinalizada()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPostSancionFinalizada().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostSancionFinalizada().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>											
				</td>
			</tr>
			<tr>
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.anotcanceladas"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="anotCanceladas" size="1" value="<%=bean.getPostAnotCanceladas()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPostAnotCanceladas().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostAnotCanceladas().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.visible"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="postVisible" size="1" value="<%=bean.getPostVisible()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
<% 				} else if (bean.getPostVisible().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostVisible().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
				</td>
				
				<td class="labelText" width="20%">
					<siga:Idioma key="expedientes.auditoria.literal.visibleenficha"/>
				</td>
				<td class="labelTextValue" width="8%">
<% 				if (bEditable){ %>
					<html:select property="postVisibleFicha" size="1" value="<%=bean.getPostVisibleFicha()%>" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>				
				</td>
<% 				} else if (bean.getPostVisibleFicha().equals("S")){  %>
						<siga:Idioma  key="general.yes"/>
<%              } else if (bean.getPostVisibleFicha().equals("N")){  %>
						<siga:Idioma  key="general.no"/>
<%				} else {%>
						-
<%				}%>
			</tr>
		</table>
		</siga:ConjCampos>
		
	</td>
	</tr>
	
	<tr>
	<td>
	
		<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.alertas">
		<table width="100%">
			<tr>
				<td class="labelText">
					<siga:Idioma key="expedientes.auditoria.literal.activarAlarma"/>
				</td>
				<td >
					<html:checkbox name="EstadosForm" property="activarAlertas" value="true" disabled="<%=!bEditable%>" onClick="validarActivarAlarmas();"/>
				</td>
				<td colspan="6">
			</tr>
			<tr>
				
				<td class="labelText" width="15%">
					<siga:Idioma key="expedientes.auditoria.literal.diasAntelacion"/>
				</td>	
				<td>
					<% 
					   if (bEditable){ %>
						<html:text name="EstadosForm" property="diasAntelacion" size="3" maxlength="3" styleClass="box"></html:text>
					<% } else { %>
						<html:text name="EstadosForm" property="diasAntelacion" size="3" maxlength="3" styleClass="boxConsulta" disabled="true"></html:text>
					<% } %>
				</td>	
				<td class="labelText" >
					<siga:Idioma key="expedientes.tiposexpedientes.literal.mensaje"/>
				</td>
				<td>
				<% if (bEditable){ %>
					<html:text name="EstadosForm" property="mensaje" size="100" maxlength="100" styleClass="box" value="<%=bean.getMensaje()%>"></html:text>
				<% } else { %>
					<html:text name="EstadosForm" property="mensaje" size="100" maxlength="100" styleClass="boxConsulta"   readonly ="true" value="<%=bean.getMensaje()%>"></html:text>
				<% } %>
				</td>
				<td colspan="4"></td>
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
	<% String botones = (bEditable?"Y,R,C":"C");%>
		<siga:ConjBotonesAccion botones="<%=botones%>" modal="G" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
				sub();						
				if (validateEstadosForm(document.EstadosForm)){
					EstadosForm.submit();			
					window.top.returnValue="MODIFICADO";
				}else{
					
					fin();
					return false;
				
				}				
			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{				
			document.forms[0].reset();
		}
	
	

 function validarAncho_tablaDatos() {
  if (document.getElementById("tablaPlazos").clientHeight < document.getElementById("tablaDatosDiv").clientHeight) {
	  document.getElementById("tablaDatosCabeceras").width='100%';
  }

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
