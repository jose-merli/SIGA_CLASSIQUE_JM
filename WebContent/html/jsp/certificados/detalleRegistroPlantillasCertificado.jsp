<!DOCTYPE html>
<html>
<head>
<!-- detalleRegistroPlantillasCertificado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>



<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector datos = (Vector)request.getAttribute("datos");
	
	
	Vector vDatos = (Vector)request.getAttribute("datosPlantillasRelaciondas");
	String realizarAsociacion = (String)request.getAttribute("asociacion");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Hashtable htDatos = (Hashtable)datos.elementAt(0);
	
	boolean bEditable = ((String)htDatos.get("editable")).equals("1");
	boolean bNuevo = ((String)htDatos.get("nuevo")).equals("1");
	
	String sIdPlantilla=(String)htDatos.get("idPlantilla");
	String sDescripcion=(String)htDatos.get("descripcion");
	String sPorDefecto=(String)htDatos.get("porDefecto");

	String sIdInstitucion=(String)htDatos.get("idInstitucion");
	String sIdTipoProducto=(String)htDatos.get("idTipoProducto");
	String sIdProducto=(String)htDatos.get("idProducto");
	String sIdProductoInstitucion=(String)htDatos.get("idProductoInstitucion");
	String sCertificado=(String)htDatos.get("certificado");
	
	
	
	
	// Textos del dialog
	String sDialogError= "Error";
	String sDialogBotonCerrar = "Cerrar";
	String sDialogBotonGuardarCerrar = "Guardar";
	
	
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<siga:Titulo titulo="certificados.mantenimiento.literal.plantilla" localizacion="certificados.mantenimientoCertificados.plantillas.plantilla.localizacion"/>	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton GuardarCerrar
			
			function refrescarLocal() {
				document.formulario.target = "_self";
				document.formulario.modo.value="Editar";	
				document.formulario.descripcion.value=document.formulario2.descripcion.value;
				document.formulario.recarga.value="SI";
				document.formulario.submit();
				
			}			
			
			function accionVolver()
			{
				formulario2.action = "/SIGA/CER_MantenimientoCertificados.do";
				formulario2.modo.value="abrirConParametros";
				formulario2.target="mainWorkArea";
				formulario2.submit();
			}
			
			function accionNuevo(){
				   jQuery.ajax({ 
						type: "POST",
						url: "/SIGA/CER_Plantillas.do?modo=getAjaxSeleccionPlantillas",				
						data: "idPlantilla=" + <%=sIdPlantilla%> + "&idInstitucion=" + <%=sIdInstitucion%>+ "&idTipoProducto=" + <%=sIdTipoProducto%>+ "&idProducto=" + <%=sIdProducto%>+ "&idProductoInstitucion=" + <%=sIdProductoInstitucion%>,
						dataType: "json",
						contentType: "application/x-www-form-urlencoded;charset=UTF-8",
						success: function(json){
							fin(); // finaliza el sub de accionFacturacionRapida()			
								jQuery("#selectSeleccionSerieFacturacion").find("option").detach();
								jQuery("#selectSeleccionSerieFacturacion").append(json.aOptionsSeriesFacturacion[0]);
								
								jQuery("#divSeleccionSerieFacturacion").dialog(
										{
											height: 220,
											width: 550,
											modal: true,
											resizable: false,
											buttons: {
												"<%=sDialogBotonGuardarCerrar%>": function() {
													
													sub();
													var idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
													if (idSerieFacturacion==null || idSerieFacturacion=='') {
														alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
														fin();
														
													} else {
														jQuery(this).dialog("close");
														document.formulario.target = "_self";
														document.formulario.modo.value = "Editar";
														document.formulario.recarga.value="SI";
														//Nueva relacion
														document.formulario.relacion.value="SI";
														document.formulario.idRelacion.value= $("#selectSeleccionSerieFacturacion option:selected").attr("value");
														document.formulario.submit();	
													}
												},
												"<%=sDialogBotonCerrar%>": function() {
													jQuery(this).dialog("close");
												}
											}
										}
									);
								jQuery(".ui-widget-overlay").css("opacity","0");														
											
						},
						
						error: function(e){
							fin(); // finaliza el sub de accionFacturacionRapida()
							alert("<%=sDialogError%>");					
						}
					});			    
			}
			
			function accionGuardar() 
			{  
				//sub();
				if(formulario2.descripcion.value=="")
				{
					var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	
					alert (mensaje);
					//fin();
					return false;
				}
				if (!TestFileType(document.formulario2.theFile.value, ['ZIP', 'FO','DOC'])){
					//fin();
					return false;
				}
				if (<%=bNuevo%>)
				{ 
					
					if(document.formulario2.theFile.value=="")
					{
						var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.archivo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";

						alert (mensaje);
						//fin();
						return false;
					}
				}
			
				//LMS 22/08/2006
				//Ventana de espera con las tuercas.		
				formulario2.submit();
				
				
			}
			
			
			function borrarRelacion(fila)
			{
				var idInstitucion = document.formulario.elements['oculto' + fila + '_1'].value;
				var idTipoProducto = document.formulario.elements['oculto' + fila + '_2'].value;
				var idProducto = document.formulario.elements['oculto' + fila + '_3'].value;
				var idProductoInstitucion = document.formulario.elements['oculto' + fila + '_4'].value;
				var idPlantilla = document.formulario.elements['oculto' + fila + '_5'].value;
				
				document.formulario.idInstitucion.value = idInstitucion; 
				document.formulario.idTipoProducto.value = idTipoProducto;
				document.formulario.idProducto.value = idProducto;
				document.formulario.idProductoInstitucion.value = idProductoInstitucion;
				document.formulario.idPlantilla.value = idPlantilla;
				document.formulario.descripcion.value=document.formulario2.descripcion.value;
				
				document.formulario.modo.value = "borrarRelacion";
				document.formulario.submit();
			}
			
			function accionDownload(){
				document.formulario.idPlantilla.value = document.formulario2.idPlantilla.value;
				document.formulario.modo.value = "download";
				document.formulario.submit();
			}
	
			function download(fila)
			{
				var idInstitucion = document.formulario.elements['oculto' + fila + '_1'].value;
				var idTipoProducto = document.formulario.elements['oculto' + fila + '_2'].value;
				var idProducto = document.formulario.elements['oculto' + fila + '_3'].value;
				var idProductoInstitucion = document.formulario.elements['oculto' + fila + '_4'].value;
				var idPlantilla = document.formulario.elements['oculto' + fila + '_5'].value;
				
				document.formulario.idInstitucion.value = idInstitucion; 
				document.formulario.idTipoProducto.value = idTipoProducto;
				document.formulario.idProducto.value = idProducto;
				document.formulario.idProductoInstitucion.value = idProductoInstitucion;
				document.formulario.idPlantilla.value = idPlantilla;
			
				
				document.formulario.modo.value = "download";
				document.formulario.submit();
			
			}
		
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body onload="ajusteAlto('mainPestanasRelacionPlantillas');">
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="certificados.plantillas.editar.literal"/>
				</td>
			</tr>
		</table>
			
		<div id="camposRegistro"  align="center">
			<html:form action="/CER_Plantillas.do" method="POST" target="submitArea" enctype="multipart/form-data" styleId="formulario2">

<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "recarga"/>
				<html:hidden property = "relacion"/>
				<html:hidden property = "idRelacion"/>
				
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
				<html:hidden property="idProducto" value="<%=sIdProducto%>"/>
				<html:hidden property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
				
				<html:hidden property="idPlantilla" value="<%=sIdPlantilla%>"/>
				<html:hidden styleId="certificado"  property="certificado" value="<%=sCertificado%>"/>

				<table class="tablaCentralCampos" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="certificados.mantenimiento.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/>&nbsp;(*)
										</td>
										<td>
											<html:text property="descripcion" value="<%=sDescripcion%>" styleClass="box" size="50" maxlength="50"/>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.archivo"/>&nbsp;(*)
										</td>				
										<td>
											<html:file name="MantenimientoCertificadosPlantillasForm" property="theFile" styleClass="box" size="50"/>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.pordefecto"/>
										</td>				
										<td>
											<input type="checkbox" name="porDefecto" value="1" styleClass="boxCombo" <%if (sPorDefecto.equals("S")) {%>checked<%}%>/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>	
			<html:form action="/CER_Plantillas.do" method="POST" target="submitArea" styleId="formulario">
					<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
						String miModo = bNuevo ? "Insertar" : "Modificar";
		%>
						<html:hidden property = "modo" value = "<%=miModo%>"/>
						<html:hidden property = "hiddenFrame" value = "1"/>
						<html:hidden property = "recarga"/>
						<html:hidden property = "relacion" value="NO"/>
						<html:hidden property = "idRelacion" value="<%=sIdPlantilla%>"/>
						<html:hidden property="idInstitucion" />
						<html:hidden property="idTipoProducto" />
						<html:hidden property="idProducto" />
						<html:hidden property="idProductoInstitucion" />
						<html:hidden property="idPlantilla" value="<%=sIdPlantilla%>"/>
						<html:hidden property="descripcion"/>
						<html:hidden styleId="certificado"  property="certificado"/>
		
		
		<%
					String botones = bEditable ? "V,G" : "V";
			
					if (sIdPlantilla != null && !"".equalsIgnoreCase(sIdPlantilla)){
		%>
						<!-- Si estamos en editar -->
						
							<siga:ConjBotonesAccion botones="D,G" clase="botonesSeguido"/>
							
							<%
							
								if (realizarAsociacion != null && realizarAsociacion.equalsIgnoreCase("NO")){
							%>
								<siga:ConjBotonesAccion botones="V"/>
							<%
							
								}else{
							%>
								<siga:ConjBotonesAccion botones="V,N"/>
							<%
							
								}
							%>
							
							
							<siga:Table 
					   	      name="tablaDatos"
					   		  border="1"
					   		  columnNames="certificados.mantenimiento.literal.plantilla,"
					   		  columnSizes="93,7"
					   		>
						<%
							if (vDatos==null || vDatos.size()==0)
							{
								if (realizarAsociacion != null && realizarAsociacion.equalsIgnoreCase("NO")){
						%>
								<tr class="notFound">
							   		<td class="titulitos"><siga:Idioma key="messages.noAsociacion"/></td>
								</tr>
								<%
								}else{
								%>
								<tr class="notFound">
							   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
								</tr>
								<%
								}
								%>
						<%
						}
						
						else
						{
							String sBotones ="";
							
					 		for (int i=0; i<vDatos.size(); i++)
					   		{
					 			FilaExtElement[] elems=new FilaExtElement[2];
								elems[0]=new FilaExtElement("borrar","borrarRelacion",SIGAConstants.ACCESS_READ);
						  		CerPlantillasBean bean = (CerPlantillasBean)vDatos.elementAt(i);
								elems[1]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);
		%>
			  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='' elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no" pintarEspacio="false">
							<td>
								<input type="hidden" id="oculto<%=""+(i+1)%>_1" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_2" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoProducto()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_3" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdProducto()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_4" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdProductoInstitucion()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_5" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getIdPlantilla()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_6" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getDescripcion()%>">
								<input type="hidden" id="oculto<%=""+(i+1)%>_7" name="oculto<%=""+(i+1)%>_7" value="<%=bean.getPorDefecto()%>">
								
								<%=bean.getDescripcion()%>
							</td>
						</siga:FilaConIconos>
		<%
							}
						}
		%>
							</siga:Table>
					<%}else{%>
						<siga:ConjBotonesAccion botones="<%=botones%>"/>
						
					<%} %>
					
				
			
			
			
			
			<div id="divSeleccionSerieFacturacion" title="<siga:Idioma key='certificados.mantenimiento.literal.plantilla'/>" style="display:none">
			<table align="left">
				<tr>		
					<td >
						<siga:Idioma key="certificados.mantenimiento.literal.plantilla" />&nbsp;(*)
						
					</td>
					<td>
						<select class='box' style='width:270px' id='selectSeleccionSerieFacturacion'>
						</select>
					</td>
				</tr>		
			</table>			
		</div>
		</html:form>
			
		</div>	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>