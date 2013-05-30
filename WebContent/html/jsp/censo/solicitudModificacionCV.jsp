<!-- solicitudModificacionCV.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
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
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	String modo=(String)request.getAttribute("modoConsulta");	
	boolean solicitarNuevo=((modo != null) && (modo.equalsIgnoreCase("solicitarNuevo")));
	ArrayList lista=new ArrayList();
	ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();
	String idCV="";
	String fechaInicio="";
	String fechaFin="";
	String descripcion="";
	if(!solicitarNuevo){
		Hashtable htData=(Hashtable)request.getAttribute("hDatos");	
		idCV=(String)htData.get(CenDatosCVBean.C_IDCV);
		lista.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV)));
		idSubtipo1.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT1)));
		idSubtipo2.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT2)));
		fechaInicio=GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAINICIO));
		fechaFin=GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAFIN));
		descripcion=String.valueOf(htData.get(CenDatosCVBean.C_DESCRIPCION));
		}	
		
  String parametro[] = new String[1];
  parametro[0] = (String)usr.getLocation();
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosCVSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 			
			window.top.close();
		}	
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer(){	
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.datosCVSolicForm.reset();				
				}						
		}			
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {	
			sub();				
			if (!validateDatosCVSolicForm(document.datosCVSolicForm)){	
				fin();
				return false;
			}		
			
		   
		  
		    if((!v_subTipo1.disabled && !v_subTipo2.disabled) && (document.datosCVSolicForm.tipoApunte.value=="" ||document.datosCVSolicForm.idTipoCVSubtipo1.value=="" || document.datosCVSolicForm.idTipoCVSubtipo2.value=="")){
		        aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
				alert(aux);
				fin();
				return false;
		   }else{
					 if((!v_subTipo1.disabled && v_subTipo2.disabled) && (document.datosCVSolicForm.tipoApunte.value=="" || document.datosCVSolicForm.idTipoCVSubtipo1.value=="")){
					 aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
					 alert(aux);
   		     		 fin();
				     return false;
		  
				 }else{
				 		if((!v_subTipo2.disabled && v_subTipo1.disabled) && (document.datosCVSolicForm.tipoApunte.value=="" || document.datosCVSolicForm.idTipoCVSubtipo2.value=="")){
				 		aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
				 		 alert(aux);
		     		   	 fin();
				       	 return false;
			       		}else{
		      if (document.datosCVSolicForm.tipoApunte.value==""){
			     aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte1"/>'
				alert(aux);
				fin();
				return false;
			  
			  }
		   }	
				 	}
				}
		    
			if (compararFecha (document.datosCVSolicForm.fechaInicio, document.datosCVSolicForm.fechaFin) == 1) {
				alert ("<siga:Idioma key="messages.fechas.rangoFechas"/>");
				fin();
				return false;
			}	
			document.all.datosCVSolicForm.modo.value = "insertarModificacion";					
			document.all.datosCVSolicForm.submit();						
		}			
        
		function deshabilitarCombos(o){
		  v_subTipo1=o; 
		  if (o.options.length > 1) {
			 o.disabled = false;
		  }
		  else {
			 o.disabled = true;
		  }
		 
		}
		function deshabilitarCombos2(o){
		  v_subTipo2=o; 
		  if (o.options.length > 1) {
			 o.disabled = false;
		  }
		  else {
			 o.disabled = true;
		  }
		 
		}
		
								
		
		var tipoCurriculum;
		function init(){
		 
		  var cmb1 = document.getElementsByName("tipoApunte");
		  var cmb2 = cmb1[0]; 
		  
		  
		  tipoCurriculum=<%=lista%>;
		
		
		  cmb2.onchange();
		
		
		  
		}
		
		function recargarCombos(tipo){
		   if (tipo){
		    if (tipoCurriculum!=tipo.value){
			  limpiarCombo("idTipoCVSubtipo1");
     		  limpiarCombo("idTipoCVSubtipo2");
		    }
		   }
		
		}
		function limpiarCombo(nombre){
		   iframeCombo = window.top.frames[0].document.getElementById (nombre + "Frame");
				cadenaInicial = iframeCombo.src;
				
				if (cadenaInicial.indexOf("&elementoSel=[0]") > 1)  {
					return;
				}
				
				var ini = cadenaInicial.indexOf('&elementoSel=');
				if (ini < 1) 
					return;
				
				cadenaFinal = cadenaInicial.substring(0,ini) + "&elementoSel=[0]";
				
				var fin = cadenaInicial.indexOf('&', ini+2);
				if (fin > 1) {	
					cadenaFinal = cadenaFinal + cadenaInicial.substring(fin);
				}

				iframeCombo.src = cadenaFinal;
		}
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onload="init();">
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDatosCV.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				     <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	<html:form action="/CEN_SolicitudCV.do" method="POST" target="submitArea">
	
		<div id="camposRegistro" class="posicionModalMedia" align="center">

		<!-- INICIO: CAMPOS -->
		<html:hidden property="modo" value="cerrar"/>
		<input type='hidden' name="idPersona" value="<%=String.valueOf((Long)request.getAttribute("idPersona"))%>"/>	
		<input type='hidden' name="idInstitucion" value="<%=String.valueOf((Integer)request.getAttribute("idInstitucion"))%>"/>
		<input type='hidden' name="idCV" value="<%=idCV%>">
		<table class="tablaCentralCamposMedia" align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
						<table class="tablaCampos" align="center">	
							<!-- FILA -->
							<tr>		
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.tipo"/> &nbsp;(*)
								</td>
								<td >
									<siga:ComboBD nombre="tipoApunte" tipo="curriculum" clase="boxCombo" elementoSel="<%=lista%>" obligatorio="true" accion="Hijo:idTipoCVSubtipo1,Hijo:idTipoCVSubtipo2;recargarCombos(this);" /></td>
								
								</td>	
								<td >
									<siga:ComboBD nombre="idTipoCVSubtipo1" tipo="cmbComision1"  parametro="<%=parametro%>" clase="boxCombo"  obligatorio="true" elementoSel = "<%=idSubtipo1%>" hijo="t" accion="parent.deshabilitarCombos(this);"/>
								</td>		
								<td >
									
									<siga:ComboBD nombre="idTipoCVSubtipo2" tipo="cmbCargos1" parametro="<%=parametro%>" clase="boxCombo" obligatorio="true" elementoSel = "<%=idSubtipo2%>" hijo="t" accion="parent.deshabilitarCombos2(this);"/>
									
								</td>									
							</tr>
							<tr><td>&nbsp</td></tr>		
								<!-- FILA -->
							<tr>					
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.fechaInicio"/> &nbsp;(*)
								</td>
								<td>
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>"/>
								</td>						
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.fechaFin"/> 
								</td>	
								<td>
									<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>"/>
								<br>
								</td>									
							</tr>
							<tr><td>&nbsp</td></tr>		
		   				<!-- FILA -->
		  				<tr>						
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.descripcion"/>&nbsp;(*)		 			
								</td>							
								<td COLSPAN=3>
										<html:textarea name="datosCVSolicForm" property="descripcion" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value='<%=descripcion%>' maxlength="255" cols="80" rows="3"/>
								<br>
								</td>								
							</tr>
							<tr><td>&nbsp</td></tr>		
		   				<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.motivo"/>&nbsp;(*)
								</td>											
		   					<td COLSPAN=3>
		   						<html:textarea name="datosCVSolicForm" property="motivo" styleClass="box" value="" cols="80" rows="3"/> 
								</td>		   					
		  				</tr>
		   			</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>		
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="C,Y,R" modal="M"/>


	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</html:form>
</body>
</html>
