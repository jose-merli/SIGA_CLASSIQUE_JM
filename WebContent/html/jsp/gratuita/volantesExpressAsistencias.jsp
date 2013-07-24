<!DOCTYPE html>
<html>
<head>
<!-- volantesExpressAsistencias.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.atos.utils.UsrBean"%>




<!-- HEAD -->

  
</head>

<body>
	<table class="tablaCampos" id='asistencias' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<logic:notEmpty name="VolantesExpressForm"	property="asistencias">
		<logic:iterate name="VolantesExpressForm" property="asistencias" id="asistencia" indexId="index">
				<input type="hidden" id="claveAnio_<bean:write name='index'/>" value="<bean:write name="asistencia" property="anio" />">  
				<input type="hidden" id="claveNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="numero" />">
				<input type="hidden" id="claveIdInstitucion_<bean:write name='index'/>" value="<bean:write name="asistencia" property="idInstitucion" />">
				<input type="hidden" id="ejgNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgNumero" />">
				<input type="hidden" id="ejgAnio_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgAnio" />">
				<input type="hidden" id="ejgTipo_<bean:write name='index'/>" value="<bean:write name="asistencia" property="ejgIdTipoEjg" />">
				<input type="hidden" id="designaNumero_<bean:write name='index'/>" value="<bean:write name="asistencia" property="designaNumero" />">
				<input type="hidden" id="delitosImputados_<bean:write name='index'/>" value="<bean:write name="asistencia" property="delitosImputados" />">
				
			<tr id="fila_<bean:write name='index'/>">
				<td align='center' width='5%'>
					<input type="text" id="hora_<bean:write name='index'/>" class="box" style="width:20;margin-top:4px;text-align:center;" maxLength="2" value="<bean:write name="asistencia" property="hora" />" onBlur="validaHora(this);" />
					<input type="text" id="minuto_<bean:write name='index'/>" class="box" style="width:20;margin-top:4px;text-align:center;" maxLength="2" value="<bean:write name="asistencia" property="minuto" />" onBlur="validaMinuto(this);" />
				</td>
				
				<td align='center' width='26%'>
				
					<c:if test="${VolantesExpressForm.lugar == 'centro'}">
						<table>
							<tr>
								<td>	
									<input type="text" id="codComisaria_<bean:write name='index'/>" class="box" size="8"  style="width:20;margin-top:2px;" maxlength="10" onBlur="obtenerComisaria(<bean:write name='index'/>);" />
								</td> 			
								<td>
									<select class="boxCombo" id="comisaria_<bean:write name='index'/>" style="width:220px;margin-top:2px;" name="comisaria_<bean:write name='index'/>" onchange="cambiarComisaria(<bean:write name='index'/>);"> 
										<bean:define id="comisarias" name="VolantesExpressForm" property="comisarias" type="java.util.List" />
											<logic:iterate id="comisaria" name="comisarias">
												<option value='<bean:write name="comisaria" property="idComisaria"/>' >
													<bean:write name="comisaria" property="nombre"/>
												</option>					
											</logic:iterate> 
									</select>
								</td>
							</tr>
						</table>
						
						<script>
							if(${asistencia.comisaria!=null} && ${asistencia.comisaria!='-1'}) {
								document.getElementById("comisaria_<bean:write name='index'/>").value ='<bean:write name="asistencia" property="comisaria"/>';
								cambiarComisaria(<bean:write name='index'/>);
							} 						
						</script>
					</c:if>
					
					<c:if test="${VolantesExpressForm.lugar == 'juzgado'}">
			      		<table>
							<tr>
								<td>	
			      					<input type="text" id="codJuzgado_<bean:write name='index'/>" class="box" size="8" style="width:20;margin-top:2px;" maxlength="10" onBlur="obtenerJuzgado(<bean:write name='index'/>);"/> 			
								</td>
								 			
								<td>
									<select class="boxCombo" id="juzgado_<bean:write name='index'/>" style="width:220px;margin-top:2px;" name="juzgado_<bean:write name='index'/>" onchange="cambiarJuzgado(<bean:write name='index'/>);"> 
										<bean:define id="juzgados" name="VolantesExpressForm" property="juzgados" type="java.util.List" />
										<logic:iterate id="juzgado" name="juzgados">
											<option value='<bean:write name="juzgado" property="idJuzgado"/>' >
												<bean:write name="juzgado" property="nombre"/>
											</option>					
										</logic:iterate> 								
									</select>
								</td>
							</tr>
						</table>
						
						<script>
						 	if(${asistencia.juzgado!=null} && ${asistencia.juzgado!='-1'}) {
								document.getElementById("juzgado_<bean:write name='index'/>").value ='<bean:write name="asistencia" property="juzgado"/>';
								cambiarJuzgado(<bean:write name='index'/>);
							} 
						</script>			
					</c:if>	
										
				
				 </td>
				<td align='center' width='36%'>
						<table >
							<tr>
								<td><input type="text" id="dni_<bean:write name='index'/>" class="box" style="width:70;margin-top:2px;margin-rigth:1px;" value="<bean:write name="asistencia" property="asistidoNif" />" maxlength="20" onBlur="obtenerPersona(<bean:write name='index'/>);"/></td>
								<td>-</td>
								<td><input type="text" id="nombre_<bean:write name='index'/>" class="box" style="width:80;margin-top:2px;margin-rigth:1px;" value="<bean:write name="asistencia" property="asistidoNombre" />" maxlength="80"/></td>
			        			<td><input type="text" id="apellido1_<bean:write name='index'/>" class="box" style="width:80;margin-top:2px;margin-rigth:1px;" value="<bean:write name="asistencia" property="asistidoApellido1" />" maxlength="80"/></td>
			        			<td><input type="text" id="apellido2_<bean:write name='index'/>" class="box" style="width:80;margin-top:2px;" value="<bean:write name="asistencia" property="asistidoApellido2" />" maxlength="80"/></td>
			        			<td><img id="info_existe_<bean:write name='index'/>" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/></td>
			        			<td><input type="hidden" id="idPersona_<bean:write name='index'/>" class="box" value="<bean:write name="asistencia" property="idPersonaJG" />"/></td>
			        		</tr>
			        	</table>
			   </td>     	
			      	<script>
			      		if(document.getElementById("dni_<bean:write name='index'/>")&&document.getElementById("dni_<bean:write name='index'/>").value!=''){
			      			ponerIconoIdentPersona (<bean:write name='index'/>, true);
			      		}
			      	</script>
				<c:if test="${VolantesExpressForm.lugar == 'centro'}">
				<td align='center' width='8%'>
					<input type="text" id="diligencia_<bean:write name='index'/>" class="box" maxlength="20" style="width:70;margin-top:2px;" value="<bean:write name="asistencia" property="numeroDiligencia" />"/>
				</td>
				</c:if>
				<c:if test="${VolantesExpressForm.lugar == 'juzgado'}">
				<td align='center' width='8%'>
					<input type="text" id="diligencia_<bean:write name='index'/>" class="box" maxlength="20" style="width:70;margin-top:2px;" value="<bean:write name="asistencia" property="numeroProcedimiento" />"/>
				</td>
				</c:if>
				<td align='center' width='14%'>
				
				<c:if test="${VolantesExpressForm.delito==true}">
				<table><tr><td>
					<input type="hidden" id="observaciones_<bean:write name='index'/>" value="">
					
					<select class="boxCombo" id="idDelito_<bean:write name='index'/>" style="width:130px;margin-top:2px;" name="idDelito_<bean:write name='index'/>" > 
						<bean:define id="delitos" name="VolantesExpressForm" property="delitos" type="java.util.List" />
							<logic:iterate id="delito" name="delitos">
								<option value='<bean:write name="delito" property="idDelito"/>' >
									<bean:write name="delito" property="descripcion"/>
								</option>					
								</logic:iterate> 
								
					</select>
						
						
					<script>
					
						if(${asistencia.idDelito!=null})
							document.getElementById("idDelito_<bean:write name='index'/>").value ='<bean:write name="asistencia" property="idDelito"/>'; 
						</script>
					</td></tr></table>
				</c:if>
				
				<c:if test="${VolantesExpressForm.delito==false}">
			
					<input type="text" id="observaciones_<bean:write name='index'/>" class="box" style="width:130;margin-top:2px;" value="<bean:write name="asistencia" property="observaciones" />"/>
					<input type="hidden" id="idDelito_<bean:write name='index'/>" value="">
				</c:if>
				
				
				
				</td>
				<td align='left' width='11%'>
					<table>
						<tr>
							<td id="consultar_<bean:write name='index'/>"><img  src="/SIGA/html/imagenes/bconsultar_on.gif" style="cursor:hand;" alt="<siga:Idioma key="general.boton.consultar"/>" name="" border="0" onclick="accionConsultaAsistencia(<bean:write name="asistencia" property="anio" />,<bean:write name="asistencia" property="numero" />,<bean:write name="asistencia" property="idInstitucion" />,<bean:write name='index'/>);"/></td>
							<td id="nuevaActuacion_<bean:write name='index'/>"><img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" onclick="accionNuevaActuacion(<bean:write name="asistencia" property="anio" />,<bean:write name="asistencia" property="numero" />,<bean:write name="asistencia" property="idInstitucion" />)"></td>
							<td id="borrarActuacion_<bean:write name='index'/>"><img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt='<siga:Idioma key="general.boton.borrar"/>' name="" border="0" onclick="borrarFila('fila_<bean:write name='index'/>')"></td>
							<td id="nuevoEjg_<bean:write name='index'/>"><img  src="/SIGA/html/imagenes/binsertarestado_on.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.asociarEjg"/>" name="" border="0" onclick="accionCrearEJG(<bean:write name="asistencia" property="anio" />,<bean:write name="asistencia" property="numero" />,<bean:write name="asistencia" property="idInstitucion" />,<bean:write name='index'/>);"/></td>
							<!-- aalg. INC_09396_SIGA -->
							<td id="numEjg_<bean:write name='index'/>" style="display:none"><input type="text" id="ejgNumEjg_<bean:write name='index'/>" size="6" style="font-size:8.5px" title="<bean:write name="asistencia" property="ejgNumEjg"/>"  value="<bean:write name="asistencia" property="ejgNumEjg"/>" readOnly="readonly" class="box" /></td>
						</tr>
					</table>	
					<script>
					//si no esta creada la asistencia no mostramos nada
					if(${asistencia.anio!=null} && ${asistencia.anio!='-1'}){
						if(${asistencia.ejgAnio!=null} && ${asistencia.ejgAnio!='-1'}){
							var nuevoEjg = 'nuevoEjg_'+<bean:write name='index'/>;
							document.getElementById(nuevoEjg).style.display="none";
							// aalg. INC_09396_SIGA
							var numEjg = 'numEjg_'+<bean:write name='index'/>;
							document.getElementById(numEjg).style.display="block";
							document.getElementById("borrarActuacion_<bean:write name='index'/>").style.display="none";
						}else if(${asistencia.designaNumero!=null} && ${asistencia.designaNumero!='-1'}){ 
							document.getElementById("borrarActuacion_<bean:write name='index'/>").style.display="none";
						}
					}else{
						document.getElementById("nuevaActuacion_<bean:write name='index'/>").style.display="none";
						document.getElementById("consultar_<bean:write name='index'/>").style.display="none";
						document.getElementById("nuevoEjg_<bean:write name='index'/>").style.display="none";
					}

					 
					
					
					
					</script>
					
					
				</td>	
					
				
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
	<script type="text/javascript">
		var messageError='${VolantesExpressForm.msgError}';
		var messageAviso='${VolantesExpressForm.msgAviso}';
		if (messageAviso)
			alert(messageAviso);
		if (messageError)
			alert(messageError);
		
	</script>
</body>
<script>
</script>


</html>