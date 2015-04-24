<!DOCTYPE html>
<%@page import="com.siga.Utilidades.paginadores.PaginadorBind"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.Utilidades.UtilidadesHash"%>
<html>
<head>
<!-- confCostesFijos.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>

<bean:define id="registrosSeleccionados" name="listadoTablasMaestrasForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="listadoTablasMaestrasForm" property="datosPaginador" type="java.util.HashMap"/>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
    boolean bEditable=true;
        
    /** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheck = "";
	if (datosPaginador!=null) {
	 if ( datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  	resultado = (Vector)datosPaginador.get("datos");
	    PaginadorBind paginador = (PaginadorBind)datosPaginador.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }else{
		resultado =new Vector();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	 }
	}else{
      	resultado =new Vector();
	  	paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}	 
    
    String bot="";
    Integer modoC=Integer.parseInt(request.getAttribute("modoConsulta").toString());
	
    String sIncluirBajaLogica="false";
	if(request.getAttribute("bIncluirRegistrosConBajaLogica")!=null){
		sIncluirBajaLogica=(String) request.getAttribute("bIncluirRegistrosConBajaLogica");
	}
    
    if(modoC!=1)
		bot ="V,G";
	else
		bot = "V";

    String action=app+"/ADM_GestionarTablasMaestras.do";
    String modo=request.getAttribute("modo").toString();
	
%>	

		
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		<!-- Incluido jquery en siga.js -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<siga:Titulo titulo="administracion.catalogos.maestros.configuracion.costes" localizacion="menu.administracion"/>
	
		<script language="JavaScript">
		
		<%  
		String mensaje = (String)request.getAttribute("mensaje");	
		if (mensaje!=null){
				String msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),mensaje));
				String estilo="notice";
				if(mensaje.contains("error")) {
					estilo="error";
				} else if(mensaje.contains("success")||mensaje.contains("updated")) {
					estilo="success";
				} 
		%>
					alert(unescape("<%=msg%>"),"<%=estilo%>");
					
		<%  
				} 
		%>
			// Asociada al boton Volver
			function accionVolver(){		
				sub();
				listadoTablasMaestrasForm.target="mainWorkArea";
				<%if(modoC!=1){ %>
					listadoTablasMaestrasForm.modo.value="Editar";
				<%}else{%>
					listadoTablasMaestrasForm.modo.value="Ver";
				<%}%>
				
				listadoTablasMaestrasForm.submit(); 
				fin();
			}
			function accionGuardar(){		
				
				
					var registrosList="";
					
					var checks = document.getElementsByName("sel");
				  	for (i = 0; i < checks.length; i++) {
				  			
						if(checks[i].checked){
							
							var importe="";
							
							importe=convertirAFormato(document.getElementById("importeCosteFijo_"+checks[i].id).value.replace(" ",""));
							
							if(importe<=0){
								fin();
								var mensaje = '<siga:Idioma key="administracion.catalogos.costesFijos.error.importe"/>';
								alert(mensaje);
								return false;
							}
								
							registrosList = registrosList + checks[i].value+"||"+importe+";";		
						} 
					}
					if(registrosList.length>0){
						sub();
						listadoTablasMaestrasForm.target="mainWorkArea";
						listadoTablasMaestrasForm.modo.value="insetarAsistencia";
						listadoTablasMaestrasForm.datosConf.value=registrosList;
						listadoTablasMaestrasForm.submit(); 
						fin();
					}else{
						fin();
						var mensaje = '<siga:Idioma key="general.message.seleccionar"/>';
						alert(mensaje);
						return false;
					}
				
			}
			
			function convertirAFormato(numero){
	  			var numeroFormateado = numero.replace(",", ".");
				while (numeroFormateado.toString().indexOf(".", 0) > 0 && numeroFormateado.toString().length - numeroFormateado.toString().indexOf(".", 0) > 3) {
					numeroFormateado = numeroFormateado.replace(".", "");
				}  			
	  			
				var numeroNumber = new Number(numeroFormateado);
				
				if (isNaN(numeroNumber)) {
					return "";
				}
				
				numeroNumber = Number(numeroNumber.toFixed(2));
				
				return numeroNumber;	
			}	
		</script>
	
	
	</head>

	<body onload="cargarChecks();checkTodos();ajusteAltoBotones('mainWorkArea')">
	<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="mainWorkArea">
		<html:hidden property="modo" styleId="modo" />
		<input type="hidden" id="actionModal" name="actionModal" value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		<html:hidden property="codigoRegistro"  styleId="codigoRegistro" />
		<html:hidden property="datosConf"/>
		<input type="hidden" property="id" styleId="id"   >
		<input type="hidden" id="regBajaLogica"  name="regBajaLogica" value="<%=sIncluirBajaLogica%>">
	</html:form>
	<table class="tablaTitulo" align="center">
	<tr>
	<td class="titulitos">
		<siga:Idioma key="general.boton.aniadirTipoAsistencia"/>
	</td>
	</tr>
</table>
	
	<siga:Table name="tablaResultados" border="1"
		columnNames=",
	   				administracion.catalogos.maestros.literal.tipos.asistencias,
	   				administracion.catalogos.maestros.literal.tipo.actuaciones,
	   				administracion.catalogos.costesFijos.importe"
		columnSizes="5,32,33,15">

	<%
		
		if (resultado != null && resultado.size() > 0) {
			for (int i = 1; i <= resultado.size(); i++) {
				Row fila = (Row) resultado.elementAt(i - 1);
				Hashtable asistAct = (Hashtable) fila.getRow();
				
				if (asistAct != null) {
					
					Integer idInstitucion   = UtilidadesHash.getInteger(asistAct, ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION);
					Integer idTipoAsist     = UtilidadesHash.getInteger(asistAct, ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA);
					Integer idTipoAct       = UtilidadesHash.getInteger(asistAct, ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION);
					String  dsTipoAct       = UtilidadesHash.getString(asistAct,  "DSTIPOACTUACION");
					String  dsTipoAsist     = UtilidadesHash.getString(asistAct,  "DSTIPOASISTENCIA");
					Integer seleccionado    = UtilidadesHash.getInteger(asistAct, "SELECCIONADO");
					String importeCosteFijo = UtilidadesHash.getString(asistAct, "IMPORTECOSTE");

				%>		
				
				<siga:FilaConIconos fila='<%="" + i%>' botones="false" visibleBorrado="false" visibleConsulta="false" visibleEdicion="false" pintarEspacio="no" clase="listaNonEdit">			
				<td align="center">
				<%String vCheck = idTipoAsist+"||"+idTipoAct;
				boolean isChecked = false;
				for (int z = 0; z < registrosSeleccionados.size(); z++) {
					Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
					String clave = (String)clavesRegistro.get("CLAVE");
					if (vCheck.equals(clave)) {
						isChecked = true;
						break;
					}
				}
				//para que en el paginador aparezca correctamente el número de seleccionados
				if(seleccionado>0){
					isChecked = true;
					Hashtable clavesSel=new Hashtable();
					clavesSel.put("CLAVE", vCheck);
					registrosSeleccionados.add(clavesSel);
				}
				
				if (isChecked) {
						if(modoC!=1){%>
							<input type="checkbox" value="<%=vCheck%>" id="<%="" + i%>" name="sel" checked onclick="pulsarCheck(this,<%="" + i%>)">
						<%}else{%>
							<input type="checkbox" value="<%=vCheck%>" id="<%="" + i%>" name="sel" checked disabled >
						<%}%>
				<%} else {
						if(modoC!=1){%>
							<input type="checkbox" value="<%=vCheck%>" id="<%="" + i%>" name="sel" onclick="pulsarCheck(this,<%="" + i%>)" >
						<%}else{%>
							<input type="checkbox" value="<%=vCheck%>" id="<%="" + i%>" name="sel" disabled >
						<%}%>
				<%}%>
			
				</td>
			
			<td>
				<!-- Datos ocultos tabla --> 
				<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucion%>"> 
				 <%=UtilidadesString.mostrarDatoJSP(dsTipoAsist)%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(dsTipoAct)%></td>
			
			<%if(modoC!=1){%>				
				<td align="center"><input style="text-align:right" type="text"  name="importeCosteFijo" id="importeCosteFijo_<%="" + i%>"  value="<%=UtilidadesString.mostrarDatoJSP(importeCosteFijo)%>"  class="box" />
			<%}else{%>
				<td align="right"><%=UtilidadesString.mostrarDatoJSP(importeCosteFijo)%></td>
			<%}%>
		</siga:FilaConIconos>	
			<%} // if
			}//for		

		} else {
		%>
		<tr class="notFound">
			<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
		</tr>
		<%
			}
		%>
	</siga:Table>
	
	<%
	
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
			: registrosSeleccionados.size()));
	
	if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>
	  
	  						
				<siga:Paginador totalRegistros="<%=totalRegistros%>" 
				registrosPorPagina="<%=registrosPorPagina%>" 
				paginaSeleccionada="<%=paginaSeleccionada%>" 
				registrosSeleccionados="<%=regSeleccionados%>"
				idioma="<%=idioma%>"
				modo="configuracionCosteFijoBuscarPor"								
				clase="paginator" 
				divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
				distanciaPaginas=""
				action="<%=action%>" 
				preFunction="preFunction" />
				
															
	
	 <%}%>	

 	 <siga:ConjBotonesAccion botones="<%=bot%>" modal=""/> 
	 <!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	<script language="javascript">
	ObjArray = new Array();
	
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}
	
	function pulsarCheck(obj,fila){
				
		
		if (!obj.checked ){
			jQuery("#importeCosteFijo_"+fila).hide();   		
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			jQuery("#importeCosteFijo_"+fila).show();
			ObjArray.push(obj.value);
		   	seleccionados1=ObjArray;
		}
		  	
		  	
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		checkTodos();
		
		   
	}
	
	function cargarChecks(){
   		
   	 	
		<%if (registrosSeleccionados!=null){
			
	   		for (int p=0;p<registrosSeleccionados.size();p++){
	   		 	
		   		Hashtable clavesReg= (Hashtable) registrosSeleccionados.get(p);
				valorCheck=(String)clavesReg.get("CLAVE");				
				%>
					
					var aux='<%=valorCheck%>';
					ObjArray.push(aux);
				<%

			} 
	   	}%>
	   	
		jQuery("input[name=sel]:not(:checked)").each(function(){
			jQuery("#importeCosteFijo_" + this.id).hide();
			
	   	})

		ObjArray.toString();
		seleccionados1=ObjArray;
			
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		if(document.getElementById('registrosSeleccionadosPaginador'))
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	
	}
	function cargarChecksTodos(o){  		
		if (document.getElementById('registrosSeleccionadosPaginador')){			
	  		var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>'); 	   	   	
		   	if (conf){
				ObjArray = new Array();
			   	if (o.checked){				   				
<%-- 					parent.seleccionarTodos('<%=paginaSeleccionada%>');					 --%>

					sub();
						listadoTablasMaestrasForm.seleccionarTodos.value = <%=paginaSeleccionada%>;		
					listadoTablasMaestrasForm.modo.value = "configuracionCosteFijoBuscarPor";
						listadoTablasMaestrasForm.target="mainWorkArea";
						listadoTablasMaestrasForm.submit();
						fin();
	
	
				} else {					
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;
				 	seleccionados1=ObjArray;				 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("sel");						
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled){
							ele[i].checked = false; 
						}							
					}		
				 }		   	  
		   	  } else {
		   	  	if (!o.checked ){		   	  			
			   	  		var ele = document.getElementsByName("sel");							
					  	for (i = 0; i < ele.length; i++) {
					  		if(!ele[i].disabled){
					  			if(ele[i].checked){	
			     					ele[i].checked = false;
									ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
								}
							}
					   	}					   	
					   	seleccionados1=ObjArray;
				   } else {				   	
					   	var ele = document.getElementsByName("sel");								
					  	for (i = 0; i < ele.length; i++) {
					  		if(!ele[i].disabled){
								if(!ele[i].checked){				  		
				    				ele[i].checked = true;
									ObjArray.push(ele[i].value);
								}
							}
					   	}					   		
				   		seleccionados1=ObjArray;
				   }
				   document.forms[0].registrosSeleccionados.value=seleccionados1;			   		
		   	  }
		   	if(document.getElementById('registrosSeleccionadosPaginador')) {
		   		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		   	}
		}
		
		jQuery("input[name=sel]:not(:checked)").each(function(){
			jQuery("#importeCosteFijo_" + this.id).hide();
	   	})
	 }
	 
	function checkTodos(){

	 	var ele = document.getElementsByName("sel");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
	  			
				if(!ele[i].checked){
					todos=0;
					break;
				} 
			}
	   
	  

	}
	function preFunction(pagina){
		
		var isModificado = false;
		
		var checks = document.getElementsByName("sel");
	  	for (i = 0; i < checks.length; i++) {
			if(checks[i].checked){
				isModificado =true;
				continue;
			} 
		}
		 if(isModificado){
			if (confirm('<siga:Idioma key="gratuita.informeJustificacionMasiva.confirmar.guardarAlCambiarPagina"/>')){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
		

	}
	</script>
	
	</body>
</html>