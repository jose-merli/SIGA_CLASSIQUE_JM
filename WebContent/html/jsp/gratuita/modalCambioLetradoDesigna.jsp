<!DOCTYPE html>
<html>
<head>
<!-- modalCambioLetradoDesigna.jsp -->
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
<%@ taglib uri = "c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.form.CambiosLetradosDesignasForm"%>

<!-- JSP -->
<%  String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String[] parametros = new String[]{usr.getLocation()};
	String accion = (String) request.getAttribute("accion");
	
	// Formulario
	CambiosLetradosDesignasForm formulario = (CambiosLetradosDesignasForm) request.getAttribute("CambiosLetradosDesignasForm");

%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="CambiosLetradosDesignasForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	var personaActual='<bean:write name="CambiosLetradosDesignasForm" property="idPersona"/>';
	var fechaActual='<bean:write name="CambiosLetradosDesignasForm" property="fechaDesigna"/>';

	function cuenta(obj){ 
	if(obj.value.length >1023) {
		      obj.value = obj.value.substring(0, 1021);
		      //obj.blur();
	       }
		} 

	//Eliminamos la persona actual
	function borraActual(){
		with(document.CambiosLetradosDesignasForm){
			//idPersona.value="";
			//fechaDesigna.value="";
		}
	}

	</script>
</head>

<body onload="borraActual()">

	<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.modalCambioLetradoDesigna.titulo"/>
		</td>
	</tr>
	</table>

<!-- INICIO -->
<div id="camposRegistro" class="posicionModalMedia" align="center">


	<html:form action="/JGR_CambiosLetradosDesigna.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "anio"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "numero"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "idTurno"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "codigo"/>
	<html:hidden name="CambiosLetradosDesignasForm" property = "sufijo"/>
	<html:hidden property = "idPersona" value = ""/>
	
	<html:hidden property = "flagSalto" value=""/>
	<html:hidden property = "flagCompensacion" value=""/>
	<html:hidden property = "cambioMismoDia" value=""/>

	<table  style="width:100%;vertical-align: center" class="tablaCentralCamposMedia"   >
	<tr>				
		<td width="15%"></td>
		<td width="22%"></td>
		<td width="63%"></td>
		
	</tr>
	
	<tr>				
	<td colspan="3">

	<!-- INICIO: CAMPOS -->
	<logic:notEmpty name="CambiosLetradosDesignasForm" property="nombreActual">
		<siga:ConjCampos leyenda="gratuita.cambioLetrados.literal.letradoActual">
			<table  width="100%">
				<!-- FILA -->
				<tr>				
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
					</td>
					<td class="boxConsulta">
						<bean:write name="CambiosLetradosDesignasForm" property="NColegiadoActual"/>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
					</td>
					<td class="boxConsulta">
						<bean:write name="CambiosLetradosDesignasForm" property="nombreActual"/>&nbsp;
						<bean:write name="CambiosLetradosDesignasForm" property="apellido1Actual"/>&nbsp;
						<bean:write name="CambiosLetradosDesignasForm" property="apellido2Actual"/>			
					</td>		
				</tr>
				
				<!-- FILA -->
				<tr>				
					<td class="labelText">
						<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesignado"/>
					</td>
					<td class="boxConsulta">
						<input type="text" id="fechaAntigua" value="<%=formulario.getFechaDesigna()%>" style="width:80px" class="boxConsulta" readonly />
					</td>
				</tr>	
				
				<!-- FILA -->
				<tr>
					<td class="labelText" width="170px">
						<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenunciaSolicita"/>
					</td>
					<td>		
						<%if (accion.equalsIgnoreCase("ver")) {%>
							<bean:write name="CambiosLetradosDesignasForm" property="fechaRenunciaSolicita"/>
						<%} else { %>
							<siga:Fecha nombreCampo="fechaRenunciaSolicita" valorInicial="<%=formulario.getFechaRenunciaSolicita()%>"></siga:Fecha>
						<%} %>
					</td>				
				
					<td class="labelText">
						<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.tipoMotivo"/>&nbsp;<%if (accion.equalsIgnoreCase("nuevo")) {%>(*)<%}%>
					</td>
					<td colspan="3">
					    <bean:define id="vIdTipoMotivo" name="CambiosLetradosDesignasForm" property="idTipoMotivo" type="java.lang.String"/>
						<%ArrayList idTipoMotivoValue=new ArrayList();
							if (vIdTipoMotivo!=null)
								idTipoMotivoValue.add(vIdTipoMotivo);
						 %>
						<%if (accion.equalsIgnoreCase("ver")){%>
							<siga:ComboBD pestana="true" nombre="idTipoMotivo" tipo="tipoMotivo" estilo="true" clase="boxConsulta" readonly="true" filasMostrar="1" elementoSel="<%=idTipoMotivoValue%>" seleccionMultiple="false" obligatorio="true"/>
						<%} else {%>
							<siga:ComboBD pestana="true" nombre="idTipoMotivo" tipo="tipoMotivo" estilo="true" clase="box" filasMostrar="1" elementoSel="<%=idTipoMotivoValue%>" seleccionMultiple="false" obligatorio="true" ancho="240"/>
						<%}%>	
					</td>
				</tr>				
							
				
				<!-- FILA -->
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenuncia"/>
					</td>
					<td>
						<html:text name="CambiosLetradosDesignasForm" styleId="fechaRenuncia" property="fechaRenuncia" size="10" maxlength="10" styleClass="boxConsulta" readonly="true"></html:text>
					</td>
					<%if (accion.equalsIgnoreCase("nuevo")) {%>				
						<td class="labelText" colspan="2">
							<siga:Idioma key='gratuita.busquedaSJCS.literal.incluirCompensacion'/>
							&nbsp;&nbsp;&nbsp;
							<input type="Checkbox" id="compensacionActual" name="compensacionActual">
						</td>
					<%} %>
				</tr>
				
				<!-- FILA -->
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.observaciones"/>
					</td>		
					
					<td colspan="3">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:textarea name="CambiosLetradosDesignasForm" rows="4" cols="150" styleClass="boxConsulta" property="observaciones" style="overflow-y:auto; overflow-x:hidden;" />
						<%} else {%>
							<html:textarea name="CambiosLetradosDesignasForm" onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)" rows="4" cols="150" styleClass="box" property="observaciones"/>
						<%}%>						
					</td>
				</tr>				
			</table>
		</siga:ConjCampos>
	</logic:notEmpty>
	</td>
	</tr>
	
	<%if (accion.equalsIgnoreCase("nuevo")){%>

		<!-- FECHA DESIGNACION-->
		<tr>
			<td class="labelText" nowrap>
				<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna"/>&nbsp;(*)
			</td>
			<td>			
				<siga:Fecha nombreCampo="fechaDesigna" postFunction="rellenarFechaRenunciaEfectiva(this)"></siga:Fecha>
			</td>
			
			<td class="labelText">
				&nbsp;
			</td>
		</tr>
		
		<tr>
			<td class="labelText" colspan="3">	
				<siga:Idioma key="gratuita.designa.designacionAutomatica"/>
			</td>		
		</tr>
		<tr>
		<td colspan="3">
		
		
		<!-- SELECCION DE LETRADO -->
		<table border="0" width="100%">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.titulo"> 
						<table class="tablaCampos" border="0" width="100%">		
							<tr>
								<td colspan="5">
									<siga:BusquedaSJCS propiedad="buscaLetrado" concepto="designacion" operacion="sustitucion" nombre="CambiosLetradosDesignasForm" botones="M"
											campoTurno="idTurno" campoFecha="fechaDesigna" campoPersona="idPersona" campoColegiado="NColegiado" campoNombreColegiado="nomColegiado" mostrarNColegiado="true"
											mostrarNombreColegiado="true" campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" modo="nuevo"  campoSalto="checkSalto" campoCompensacion="checkCompensacion"
										/>		
								</td>	
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
								</td>		
								<td>
									<input type="text" name="NColegiado" class="boxConsulta" readOnly value="" style="width:'100px';">
								</td>
								<td class="labelText">
									<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
								</td>
								<td colspan="2">
									<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" style="width:'240px';">
								</td>			
							</tr>	
						</table>
				    </siga:ConjCampos> 
				</td>
			</tr>
		</table>   
	
	<%}%>

	</td>
	</tr>
	</table>

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ConjBotonesAccion botones="C" modal="M" />
	<%} else {%>
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M"/>
	<%}%>

	<!-- FIN: BOTONES REGISTRO -->

	</html:form>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton GuardarCerrar
		
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			
			<%if (accion.equalsIgnoreCase("editar")){%>			
				var f=document.forms[0]; 
				sub();
				if (validateCambiosLetradosDesignasForm(document.forms[0]))		
				{	
					if(f.fechaRenunciaSolicita.value=="" || isAfter(f.fechaRenunciaSolicita.value,jQuery("#fechaAntigua").val()))
					{
						f.modo.value="modificar";
						f.submit();
					}else
					{
						alert("<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.literal.fechaRenunciaSolicita"/>"+
						  	  " <siga:Idioma key="gratuita.cambiosProcuradoresDesigna.alert1"/>"+jQuery("#fechaAntigua").val());
						  	  fin();
						  	  return false;
					}
				}else{
				
					fin();
					return false;
				}
			
			<%} else if (accion.equalsIgnoreCase("nuevo")) {%>	
				sub();
				
				if(personaActual!='' && personaActual==document.forms[0].idPersona.value)
				{	//ha seleccionado al mismo
					alert("<siga:Idioma key='gratuita.busquedaSJCS.alert2'/>");
					fin();
				    return false;
				}else if (validateCambiosLetradosDesignasForm(document.forms[0])) {
					
					//Se saca fuera de la validacion por struts ya que esta validacion no se hace al modificar
					if(document.forms[0].idTipoMotivo.value == ''){
						alert("<siga:Idioma key='errors.required' arg0='gratuita.cambiosProcuradoresDesigna.literal.tipoMotivo'/>"); 
						fin();
						return false;
					}
					
					//Se saca fuera de la validacion por struts ya que esta validacion no se hace al modificar
					if(document.forms[0].fechaDesigna.value == ''){
						alert("<siga:Idioma key='errors.required' arg0='gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna'/>"); 
						fin();
						return false;
					}
					
					
					if(fechaActual!=''){
						if (isEquals(document.forms[0].fechaDesigna.value,fechaActual)){
						    if (confirm("<siga:Idioma key='messages.designa.confirmacion.igualdadFechas' />")) {
							 
							  document.CambiosLetradosDesignasForm.cambioMismoDia.value="1";
							  document.forms[0].modo.value="insertar";
							  document.forms[0].submit();
							}else{
							      document.CambiosLetradosDesignasForm.cambioMismoDia.value="0";
							      fin();
							      return false;
							}
					    
						}else{
						  if (!isAfter(document.forms[0].fechaDesigna.value,fechaActual)){
						       alert("<siga:Idioma key='gratuita.cambiosProcuradoresDesigna.literal.fechaDesigna'/> "+
							  	  "<siga:Idioma key='gratuita.cambiosProcuradoresDesigna.alert2'/>"+fechaActual);
							  	  fin();
							  	  return false;
						  }else{
						   	if (document.forms[0].NColegiado.value || confirm("<siga:Idioma key='messages.designa.confirmacion.seleccionAutomaticaLetrado' />")){
						    	document.forms[0].modo.value="insertar";
								document.forms[0].submit();
							}else{
								fin();
								return false;
							}
						  }
						}	
					}
					
				}else{
				
					fin();
					return false;
				}
			<%}%>	
		}
		
		function refrescarLocal() 
		{		
			fin();
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
		
		function rellenarFechaRenunciaEfectiva(o) 
		{		
			jQuery('#fechaRenuncia').val(o.value);
		}
		
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
