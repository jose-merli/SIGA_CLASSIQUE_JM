<!-- solicitudModificacionDirecciones.jsp -->
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
<%@ page import = "com.siga.beans.CenDireccionTipoDireccionBean"%>
<%@ page import = "com.siga.beans.CenTipoDireccionBean"%>
<%@ page import = "com.siga.beans.CenDireccionesBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	//UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	String pais="";
	String poblacionExt="";
//	Hashtable htData=(Hashtable)request.getAttribute("hDatos");		
	Hashtable htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	

	ArrayList idPais      = new ArrayList();
	ArrayList idProvincia = new ArrayList();
	ArrayList idPoblacion = new ArrayList();			
	idPais.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPAIS)));
	idProvincia.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPROVINCIA)));
	idPoblacion.add(String.valueOf(htData.get(CenDireccionesBean.C_IDPOBLACION)));
	
	poblacionExt = String.valueOf(htData.get("POBLACIONEXTRANJERA"));
	
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->
<head>
<style>
.ocultar {display:none}
</style>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="consultaDireccionesSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Volver -->

		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		
		function accionCerrar(){ 			
			window.top.close();
		}	
		
	    function selPais(valor) {                                                                   
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
		   	  	jQuery("#provincia").attr("disabled","disabled");
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				jQuery("#provincia").removeAttr("disabled");
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
	       }
	    }
		
		function selPaisInicio() {
			var p = document.getElementById("pais");
			selPais(p.value);
		}	    
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer(){	
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.consultaDireccionesSolicForm.reset();	
				rellenarCampos();
				}						
		}	
		
		<!-- Actualiza las direcciones preferentes -->
		function actualizar(){
	    
	     
	     document.forms[0].modificarPreferencias.value="1";
	     document.forms[0].submit();
		 document.forms[0].modificarPreferencias.value="0";
	    }		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {	
			sub();
			// RGG 01-03-2005 cambio de validacion
			if((document.consultaDireccionesSolicForm.preferenteMail.checked) && 
				 (trim(document.consultaDireccionesSolicForm.correoElectronico.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			if((document.consultaDireccionesSolicForm.preferenteCorreo.checked) && 
				 (trim(document.consultaDireccionesSolicForm.domicilio.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			// RGG 25/04/2005
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") &&
				(trim(document.consultaDireccionesSolicForm.codigoPostal.value)=="")) {
				
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") && (trim(document.consultaDireccionesSolicForm.pais.value)==idEspana ||trim(document.consultaDireccionesSolicForm.pais.value)=="" ) && 
					(trim(document.consultaDireccionesSolicForm.provincia.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.provincia"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
				}
			if(trim(document.consultaDireccionesSolicForm.provincia.value)!=="" && (trim(document.consultaDireccionesSolicForm.poblacion.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
			}
				
			if((trim(document.consultaDireccionesSolicForm.domicilio.value)!="") && (trim(document.consultaDireccionesSolicForm.pais.value)!=idEspana && trim(document.consultaDireccionesSolicForm.pais.value)!="") && 
					(trim(document.consultaDireccionesSolicForm.poblacionExt.value)=="")) {
					
	 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	 				 alert (mensaje);
	 				 fin();
					 return false;
			}			
			if((document.consultaDireccionesSolicForm.preferenteFax.checked) && 
				 (trim(document.consultaDireccionesSolicForm.fax1.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
            if((document.consultaDireccionesSolicForm.preferenteSms.checked) && 
				 (trim(document.consultaDireccionesSolicForm.movil.value)=="")) {
 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/><siga:Idioma key="messages.campoObligatorio.error"/>";
 				 alert (mensaje);
 				 fin();
				 return false;
			}
			if (validateConsultaDireccionesSolicForm(document.consultaDireccionesSolicForm)){			
					document.all.consultaDireccionesSolicForm.modo.value="insertarModificacion";							
					document.all.consultaDireccionesSolicForm.submit();					
				}
			else{
				fin();
			}				
		}	
	
		<!-- Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable -->
		function rellenarCampos(){
			f=document.all.consultaDireccionesSolicForm;
			
			// Inicializo
			f.preferenteMail.checked=false;
			f.preferenteCorreo.checked=false;
			f.preferenteFax.checked=false;
			f.preferenteSms.checked=false;
		
			// Campo Preferente
			preferente="<%=String.valueOf(htData.get(CenDireccionesBean.C_PREFERENTE))%>"	
			if(preferente.indexOf("E")!=-1)  	f.preferenteMail.checked=true;		  
 		  	if(preferente.indexOf("C")!=-1)  	f.preferenteCorreo.checked=true;
		  	if(preferente.indexOf("F")!=-1)  	f.preferenteFax.checked=true;
            if(preferente.indexOf("S")!=-1)  	f.preferenteSms.checked=true;		 		
		  	// Campo provincia	
		 	document.getElementById("provincia").onchange();	  	
		 
		}
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onLoad="selPaisInicio();">
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDirecciones.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
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

<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->
	
	<html:form action="/CEN_SolicitudDirecciones.do" method="POST" target="submitArea">
		<html:hidden property="modo" value="cerrar"/>
		<input type='hidden' name = "idPersona" value="<%=htData.get(CenDireccionesBean.C_IDPERSONA)%>"/>	
		<input type='hidden' name = "idInstitucion" value="<%=htData.get(CenDireccionesBean.C_IDINSTITUCION)%>"/>
		<input type='hidden' name = "idDireccion" value="<%=htData.get(CenDireccionesBean.C_IDDIRECCION)%>">
		<input type="hidden" name = "modificarPreferencias" value="">
		<input type="hidden" name = "idDireccionesPreferentes" value = ""/>
		<input type="hidden" name = "idDireccionesCensoWeb" value = ""/>
		<table class="tablaCentralCamposGrande" align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
						<table class="tablaCampos" align="center">							
							<!-- FILA -->
							<tr>		
								<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.direccion"/>
								</td>
								<td>
									<html:textarea cols="70" rows="2" name="consultaDireccionesSolicForm" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" property="domicilio" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_DOMICILIO))%>" maxlength="100"  styleClass="box"></html:textarea>
								</td>						
								<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.cp"/>
								</td>	
								<td>
									<html:text name="consultaDireccionesSolicForm" property="codigoPostal" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_CODIGOPOSTAL))%>" size="5" maxlength="5" styleClass="box"></html:text>
								</td>							
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.pais2"/>
								</td>
								<td>									
									<siga:ComboBD nombre="pais" tipo="pais" clase="boxCombo" obligatorio="false" elementoSel="<%=idPais%>" accion="selPais(this.value);"/>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.provincia"/>					
								</td>
								<td id="provinciaEspanola">
									<siga:ComboBD nombre="provincia" tipo="provincia" clase="boxCombo" obligatorio="false" elementoSel="<%=idProvincia%>" accion="Hijo:poblacion"/>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>
								</td>
								<td  id="poblacionEspanola">									
									<siga:ComboBD nombre="poblacion" tipo="poblacion" clase="boxCombo" elementoSel="<%=idPoblacion%>" hijo="t"/> 		
								</td>
								<td  class="ocultar"  id="poblacionExtranjera">
										<html:text name="consultaDireccionesForm" property="poblacionExt" value='<%=poblacionExt%>' size="30" styleClass="box" readOnly="false"></html:text>
								</td>
								
							</tr>
		   				<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="telefono1" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO1))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>			   	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.telefono2"/>&nbsp
								</td>				
		   					<td>
		   	 					<html:text name="consultaDireccionesSolicForm" property="telefono2" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_TELEFONO2))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>	
		  				</tr>
		  					
		   				<!-- FILA -->
							<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.movil"/>&nbsp
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="movil" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_MOVIL))%>" size="20" maxlength="20" styleClass="box"></html:text>
		   					</td>	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.fax1"/>&nbsp
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="fax1" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_FAX1))%>" size="20" maxlength="20" styleClass="box"></html:text>		   					
		   					</td>
		   					
		 					</tr>
		 					<!-- FILA -->
		  				<tr>
		  				 	<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.fax2"/>&nbsp
								</td>				
		   					<td colspan="3">
		   						<html:text name="consultaDireccionesSolicForm" property="fax2" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_FAX2))%>" size="20" maxlength="20" styleClass="box"></html:text>		   					
			   	 		   	</td>
		  				</tr>
		  				<!-- FILA -->
		 					<tr>	
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.correo"/>&nbsp										
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="correoElectronico" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_CORREOELECTRONICO))%>" size="50" maxlength="100" styleClass="box"></html:text>					
		   			   	</td>	
		  					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.paginaWeb"/>&nbsp
								</td>				
		   					<td>
		   						<html:text name="consultaDireccionesSolicForm" property="paginaWeb" value="<%=String.valueOf(htData.get(CenDireccionesBean.C_PAGINAWEB))%>" size="25" maxlength="100" styleClass="box"></html:text>					
		  				 	</td>			   	
		  				</tr>
		  			 	 <tr>
				  			<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.preferente"/></td>
						  	<td colspan="4">
							  	<table>
							  		<tr>
					   					<td class="labelText"><siga:Idioma key="censo.preferente.mail"/> 
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteMail"> </html:checkbox> </td>
					   					<td class="labelText"><siga:Idioma key="censo.preferente.correo"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteCorreo"> </html:checkbox> </td>					   					
					   					<td class="labelText"><siga:Idioma key="censo.preferente.fax"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteFax"> </html:checkbox> </td>
					   						<td class="labelText"><siga:Idioma key="censo.preferente.sms"/>
					   						<html:checkbox name="consultaDireccionesSolicForm" property="preferenteSms"> </html:checkbox> </td>
					   											   					 
							  		</tr>
							  	</table>
						  	</td>
		  				</tr>					  
							<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosDireccion.literal.motivo"/>&nbsp;(*)
							</td>											
			   				<td colspan="3">
		   						<textarea name="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" class="box" cols="88" rows="2"></textarea> 
							</td>		   					
		  				</tr>		  			 			 
		   			</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</html:form>
		</table>
		<script>rellenarCampos()</script>
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="C,Y,R" modal="G"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
