<!DOCTYPE html>
<html>
<head>
<!-- programacion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="fmt.tld" prefix="fmt"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.envios.form.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String idInstitucion[] = {user.getLocation()};
	
	String  pathFTPDescarga = (String)request.getAttribute("pathFTPDescarga");
	if (pathFTPDescarga==null) pathFTPDescarga="";
	
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";
	
	//Recupero el nombre y tipo del envio
	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo =  UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),user);
	String idTipoEnvio = (String)request.getAttribute("idTipoEnvio");
	String idEnvio = (String)request.getParameter("idEnvio");
	String fechaOrig= (String)request.getAttribute("fecha");
	String horasOrig = (String)request.getAttribute("horas");
	String minutosOrig = (String)request.getAttribute("minutos");

	String editable = (String)request.getAttribute("editable");
	boolean bEditable = editable.equals("true");
	
	String boxStyle = bEditable?"box":"boxConsulta";
	
	String fecha = UtilidadesString.getMensajeIdioma(user,"envios.definir.literal.fechaprogramada");
	String obligatorio = UtilidadesString.getMensajeIdioma(user,"messages.campoObligatorio.error");
	String descargar = UtilidadesString.getMensajeIdioma(user,"general.boton.download");
	
	boolean impObligatoria = false;
	if (idTipoEnvio.equals(String.valueOf(EnvEnviosAdm.TIPO_CORREO_ORDINARIO))) impObligatoria=true;
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		
		
		<!-- Validaciones en Cliente -->
	<html:javascript formName="ProgramacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
		<style type="text/css">
		.colIzq{clear:left;}
		
		.col50{width:49%;float:left;display:block;}
		.col25{width:24%;float:left;display:block;}
		.col33{width:32%;float:left;display:block;}
		
		.ahueca{padding:30px;}
		
		.col100px{width:100px;min-height:24px;float:left;display:block;}
		.col120px{width:120px;min-height:24px;float:left;display:block;}
		.col180px{width:180px;min-height:24px;float:left;display:block;}
		.col300px{width:300px;min-height:24px;float:left;display:block;}
		.col400px{width:400px;min-height:24px;float:left;display:block;}
		.col420px{width:420px;min-height:24px;float:left;display:block;}
		
		.scrollClass{clear:left;display:block;overflow-x:hidden;overflow-y:auto; }
		
		</style>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{		
				sub();
				var fechaProgramada = document.ProgramacionForm.fechaProgramada;
								
				if (document.getElementById("automatico").checked && fechaProgramada.value=="") {
					
					alert("<%=fecha%>"+" "+"<%=obligatorio%>");
					fin();
					return false;
				} else if (fechaProgramada.value!="") {
						//Para la validacion no tengo en cuenta si empieza por 0 y tiene 2 digitos (tanto hora como minuto)
						/*var horas = document.ProgramacionForm.horas.value;
						var minutos = document.ProgramacionForm.minutos.value;
						if (horas.length==2 && horas.charAt(0)=='0') {
							document.ProgramacionForm.horas.value = horas.charAt(1);
						}
						if (minutos.length==2 && minutos.charAt(0)=='0') {
							document.ProgramacionForm.minutos.value = minutos.charAt(1);
						}*/

						if (validateProgramacionForm(document.ProgramacionForm)) {
								//document.getElementById("modo").value="modificar";
								ProgramacionForm.modo.value="modificar";
								ProgramacionForm.submit();
						}else{
							fin();
						}
				} else {
						ProgramacionForm.submit();
				}
			}
	
			<!-- Asociada al boton Volver -->
			/*function accionVolver() 
			{		
				document.forms[0].action = "<%=app%>/ENV_DefinirEnvios.do?buscar=true";
				document.forms[0].modo.value="abrir";
				document.forms[0].target = "mainWorkArea";
				document.forms[0].submit();
			}*/
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
			
			function accionRestablecer() 
			{		
				ProgramacionForm.reset();
			}
			function descargar() 
			{
				sub();
				ProgramacionForm.modo.value = "descargar";
				var fname = document.getElementById("ProgramacionForm").name;
				window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.wait';
				
			   	//ProgramacionForm.submit();
			   	//ProgramacionForm.modo.value = "modificar";
			}
			function descargar()
			{
				
			   	var urlFTP='<%=pathFTPDescarga %>';
			   	window.open(urlFTP,'FTPdownload','height=600,width=800,status=yes,toolbar=yes,menubar=no,location=no');
			}
			
			function accionGenerarEtiquetas() 
			{
				sub();
				ProgramacionForm.modo.value = "generarEtiquetas";
				var fname = document.getElementById("ProgramacionForm").name;
				window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.wait';

			}
			
		</script>
		
		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>
		
		<!-- FIN: SCRIPTS BOTONES -->
		
		<script language="JavaScript">
		jQuery.noConflict();
		function validarCheck()
		{
		  
			if (!ProgramacionForm.automatico.checked)	
			{


				jQuery("#divfechaProgramada").hide();
				ProgramacionForm.horas.readOnly=true;
				ProgramacionForm.minutos.readOnly=true;
				ProgramacionForm.fechaProgramada.readOnly=true;
			
				ProgramacionForm.fechaProgramada.value="";
				ProgramacionForm.horas.value="";
				ProgramacionForm.minutos.value="";
			}
			else
			{ 

				jQuery("#divfechaProgramada").show();
				ProgramacionForm.horas.readOnly=false;
				ProgramacionForm.minutos.readOnly=false;
				ProgramacionForm.fechaProgramada.readOnly=false;

				var fFecha = new Date();
				
				var dia=fFecha.getDate();
				var mes=fFecha.getMonth()+1;
				var yea=fFecha.getYear();
				
				var hora=fFecha.getHours();
				var minuto=fFecha.getMinutes();

				ProgramacionForm.fechaProgramada.value=getFechaActualDDMMYYYY();
				//ProgramacionForm.fechaProgramada.value=dia+"/"+mes+"/"+yea;
				if(hora<10) ProgramacionForm.horas.value="0"+hora;
				else ProgramacionForm.horas.value=hora;
				
				if(minuto<10) ProgramacionForm.minutos.value="0"+minuto;
				else ProgramacionForm.minutos.value=minuto;
			 
			}
		}
		
		function validarCheckInit()
		{  
	       <%if (bEditable){%>
			if (!ProgramacionForm.automatico.checked)	
			{

				ProgramacionForm.horas.readOnly=true;
				ProgramacionForm.minutos.readOnly=true;
			
				ProgramacionForm.fechaProgramada.value="";
				ProgramacionForm.horas.value="";
				ProgramacionForm.minutos.value="";
			}
			else
			{ 
			<% if (fechaOrig==null || fechaOrig.equals("")){%>

			
				ProgramacionForm.horas.readOnly=false;
				ProgramacionForm.minutos.readOnly=false;

				var fFecha = new Date();
				
				var dia=fFecha.getDate();
				var mes=fFecha.getMonth()+1;
				var yea=fFecha.getYear();
				
				var hora=fFecha.getHours();
				var minuto=fFecha.getMinutes();
								
				ProgramacionForm.fechaProgramada.value=dia+"/"+mes+"/"+yea;
				if(hora<10) ProgramacionForm.horas.value="0"+hora;
				else ProgramacionForm.horas.value=hora;
				
				if(minuto<10) ProgramacionForm.minutos.value="0"+minuto;
				else ProgramacionForm.minutos.value=minuto;
			  <%}else{%>
			  
			    ProgramacionForm.fechaProgramada.value='<%=fechaOrig%>';
				ProgramacionForm.horas.value=<%=horasOrig%>;
				ProgramacionForm.minutos.value=<%=minutosOrig%>;
			  <%}%>	
			}
			<%}%>
		}
		function accionEnviar(){
		   sub();	
		   ProgramacionForm.target="submitArea";	   	
      	   ProgramacionForm.modo.value='procesarEnvio';
		   
		   ProgramacionForm.submit();
		  
		}
		
		function descargaLog() {		
			ProgramacionForm.target="submitArea";	   	
      	   	ProgramacionForm.modo.value='descargarLogErrores';
		   	ProgramacionForm.submit();
		}
		
		jQuery(document).ready(function(){
			jQuery('#scrollDiv').height(window.parent.jQuery('#mainPestanas').height()-jQuery('#programacion').height()-150);
		});
		
		
		</script>
		
		<siga:Titulo
			titulo="envios.definirEnvios.programacion.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>
	
	<body>
	
	
	
		<html:form  action="/ENV_Programacion.do" method="POST" target="submitArea">
				<html:hidden property = "modo" styleId = "modo" value = "modificar"/>
				<html:hidden property = "hiddenFrame" styleId = "hiddenFrame"  value = "1"/>
				<html:hidden property = "idEnvio" styleId = "idEnvio"  value = "<%=idEnvio%>"/>
				<html:hidden property = "idTipoEnvio"  styleId = "idTipoEnvio" value = "<%=idTipoEnvio%>"/>
		
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%> 				    
				&nbsp;&nbsp;&nbsp;
				<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
			</td>
		</tr>
	</table>
		

		<c:if test="${idTipoEnvio!=2}">
		<div id="programacion" class="colIzq col33 ahueca">
		<siga:ConjCampos leyenda="envios.definirEnvios.programacion.cabecera">
		<div style="height:100px;">	
			<div class="colIzq col100px labelText">
				<siga:Idioma key="envios.definir.literal.automatico"/>
			</div>
			<div class="col120px labelTextValue">
				<html:checkbox name="ProgramacionForm" property="automatico" value="true" disabled="<%=!bEditable%>" onclick="validarCheck();" />				
			</div>
			
			<div id="divfechaProgramada">
			<div class="colIzq col100px labelText">
				<siga:Idioma key="envios.definir.literal.fechaenvio"/> (*)
			</div>
			<div class="col120px labelTextValue">
					<% if (bEditable){%>
						<siga:Fecha nombreCampo="fechaProgramada" valorInicial="<%=fechaOrig%>" />
					
					<%}else{%>
						<html:text name="ProgramacionForm" property="fechaProgramada" styleClass="boxConsulta" value="<%=fechaOrig%>" size="10" maxlength="10" readonly="<%=!bEditable%>"/>
					<%}%>
			</div>
			
			<div class="colIzq col100px labelText">
					<siga:Idioma key="envios.definir.literal.horaenvio"/> (*)
			</div>
			<div class="col120px boxConsulta labelTextValue">
				<%if (bEditable){%>
					<html:text name="ProgramacionForm" property="horas" size="2" maxlength="2" styleClass="box" readonly="<%=!bEditable%>" value="<%=horasOrig%>" style="text-align:center"></html:text>					
					:
					<html:text name="ProgramacionForm" property="minutos"  size="2" maxlength="2" styleClass="box" readonly="<%=!bEditable%>" value="<%=minutosOrig%>" style="text-align:center"></html:text>	
				<% } else {%>
					<bean:write name="ProgramacionForm" property="horas"/>:<bean:write name="ProgramacionForm" property="minutos"/>
				<%}%>
			</div>
			</div>
		</div>
		</siga:ConjCampos>		
		</div>
		</c:if>
		
		
		<% if (impObligatoria) { %>
		<div class="col33 ahueca">
		<siga:ConjCampos leyenda="envios.definir.literal.etiquetas">	
			<div style="height:100px;">						
			<table align="left" cellspacing="0" border="0" width="100%">
				<tr>
					<td align="center">
						<html:radio styleId="radioNoImprimir" name="ProgramacionForm" property="imprimirEtiquetas" disabled = "<%=!bEditable%>" value="<%=EnvEnviosAdm.NO_GENERAR%>"/>
					</td>
					<td id="titulo" class="labelText" colspan="2">
						<label for="radioNoImprimir">
						<siga:Idioma key="envios.definir.literal.nogenerar"/>			    
						</label>
					</td>	
				</tr>
				<tr>
					<td align="center">
						<html:radio styleId="radioImprimir"  name="ProgramacionForm" property="imprimirEtiquetas" disabled = "<%=!bEditable%>" value="<%=EnvEnviosAdm.GENERAR_ETIQUETAS%>"/>
					</td>
					<td id="titulo" class="labelText" colspan="2">
						<label for="radioImprimir">
						<siga:Idioma key="envios.definir.literal.generaretiquetas"/>
						</label>	    
					</td>	
				</tr>
				<tr>
					<td colspan="3" align="right">
						<input type="button" class="button" alt="<%=descargar%>" name="idButton"  onclick="return descargar();" value="<%=descargar%>"/>
					</td>
				</tr>
				<tr>
					<td colspan="3" align="right">
						&nbsp;
					</td>
				</tr>
			</table>
			</div>
		</siga:ConjCampos>
		</div>
		<% } %>

		
		
		<c:if test="${!empty requestScope.estados}">
		<div class="colIzq">
			<div style='margin-bottom:12px;display:block;padding-left:8px; padding-bottom:8px'>
				<div class='tableTitle' style='float:left;width:100%;padding:2px;font-weight: bold;'>
				
					<div class='colIzq col300px '>ESTADO</div>
					<div class='col180px '>FECHA</div>
					<div class='col400px '>USUARIO</div>
					
				</div>
				<br>
				<div id='scrollDiv' class='scrollClass'>
				<c:forEach items="${requestScope.estados}" var="estado" varStatus="status">
					<div class='${status.index % 2 == 0 ? "filaTablaImpar": "filaTablaPar"}' style='float:left;width:100%;padding:2px;'>
						<div class='${status.index % 2 == 0 ? "filaTablaImpar": "filaTablaPar"} colIzq col300px labelTextValue' style='vertical-align: middle;'>	
							<c:out value="${estado.ESTADO}"></c:out>
							<c:if test="${estado.IDESTADO==3 && status.last}">
								<IMG onclick="descargaLog();" title="Descargar informe" 
								style="CURSOR: pointer;vertical-align: middle" border=0 alt="Descargar informe" src="/SIGA/html/imagenes/bdescargaLog_off.gif">
							</c:if>
						</div>
						<div class='${status.index % 2 == 0 ? "filaTablaImpar": "filaTablaPar"} col180px labelTextValue'><c:out value="${estado.FECHACAMBIOESTADO}"></c:out></div>
						<div class='${status.index % 2 == 0 ? "filaTablaImpar": "filaTablaPar"} col400px labelTextValue'><c:out value="${estado.USUARIO}"></c:out></div>
					</div>
				</c:forEach>
				</div>
				</br>
			</div>
		</div>
		</c:if>
		
		</html:form>

<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>	
		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="V,GET,R,G,EN" clase="botonesDetalle"  />
		
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		