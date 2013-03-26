<!-- resultadoBusqExp.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
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
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.expedientes.ExpPermisosTiposExpedientes"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<bean:define id="registrosSeleccionados" name="busquedaExpedientesForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="busquedaExpedientesForm" property="datosPaginador" type="java.util.HashMap"/>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	boolean isBusquedaAvanzada = request.getAttribute("isBusquedaAvanzada")!=null;
	 
	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idioma=userBean.getLanguage().toUpperCase();
	String idInstitucion = userBean.getLocation();
	boolean isInstitucion = Integer.parseInt(idInstitucion)>2000 && Integer.parseInt(idInstitucion)<3000;
	
	String nombreCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,expedientes.auditoria.literal.institucion, 	expedientes.auditoria.literal.tipo,expedientes.auditoria.literal.nexpediente,expedientes.auditoria.literal.numyanioejg,	expedientes.auditoria.literal.fase, expedientes.tiposexpedientes.literal.estado,expedientes.auditoria.literal.fecha, 	expedientes.auditoria.literal.nombreyapellidos,";
	String tamanoCol="3,8,8,8,8,8,8,8,18,12";
	if(isInstitucion){
		nombreCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,expedientes.auditoria.literal.tipo,expedientes.auditoria.literal.nexpediente,expedientes.auditoria.literal.numyanioejg,	expedientes.auditoria.literal.fase, expedientes.tiposexpedientes.literal.estado,expedientes.gestionarExpedientes.fechaApertura,	expedientes.auditoria.literal.nombreyapellidos,";
		  tamanoCol="3,15,10,10,10,10,8,18,12";
		
	}
	
	
	
	String botones = "";
	ExpPermisosTiposExpedientes perm=(ExpPermisosTiposExpedientes)request.getAttribute("permisos");
	
	request.removeAttribute("datos");
	/** PAGINADOR ***/
	String paginaSeleccionada = "";
	
	String totalRegistros = "";

	String registrosPorPagina = "";
	Vector resultado = null;
	String valorCheckPersona = "";
	if (datosPaginador!=null) {
	

		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");
			
				Paginador paginador = (Paginador) datosPaginador
						.get("paginador");
				paginaSeleccionada = String.valueOf(paginador
						.getPaginaActual());

				totalRegistros = String.valueOf(paginador
						.getNumeroTotalRegistros());

				registrosPorPagina = String.valueOf(paginador
						.getNumeroRegistrosPorPagina());
			
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";

		totalRegistros = "0";

		registrosPorPagina = "0";
	}

	String action = app + "/EXP_AuditoriaExpedientes.do?noReset=true";
	
	
	
	
	
%>	


<%@page import="com.siga.Utilidades.Paginador"%>
<%@page import="com.siga.tlds.FilaExtElement"%>
<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.literal.titulo" 
			localizacion="expedientes.auditoria.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body onload="cargarChecks();checkTodos()"  class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/EXP_AuditoriaExpedientes.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
			
		    <html:hidden styleId = "modo" property = "modo" value = ""/>
			<html:hidden styleId = "hiddenFrame" property = "hiddenFrame" value = "1"/>
			<html:hidden styleId="registrosSeleccionados" property="registrosSeleccionados" />
			<html:hidden styleId="datosPaginador"  property="datosPaginador" />
			<html:hidden styleId="seleccionarTodos"  property="seleccionarTodos" />
			<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>
		

			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="<%=nombreCol%>"
		   		  tamanoCol="<%=tamanoCol%>"
		   		  alto="100%"
		   		  ajusteBotonera="true"
		   		  activarFilaSel="true"	
				  ajustePaginador="true" >
	   		     		    		  
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br>
<%
				}
				
				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
			 			FilaExtElement[] elemento=new FilaExtElement[1];
			 			elemento[0]=new FilaExtElement("enviar","comunicar",SIGAConstants.ACCESS_READ);
				  		Row fila = (Row)resultado.elementAt(i);
				  		String idInstitucionRow = fila.getString("IDINSTITUCION");
				  		String idInstitucionTipoExpRow = fila.getString("IDINSTITUCION_TIPOEXPEDIENTE");
				  		String idTipoExpedienteRow = fila.getString("IDTIPOEXPEDIENTE");
				  		String anioExpedienteRow = fila.getString("ANIOEXPEDIENTE");
				  		String numeroExpedienteRow = fila.getString("NUMEROEXPEDIENTE");
				  		
				  		if (idInstitucionRow.equals(idInstitucion)){	
				  			botones="C,E,B";
				  		}else{
				  			botones="C,E";
				  		}
				  		botones=perm.getBotones(idInstitucionTipoExpRow,idTipoExpedienteRow,botones);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit" elementos='<%=elemento%>'>
	  			<td align="center">
					<%
					
					String valorCheck =idInstitucionRow+"||"+idInstitucionTipoExpRow+
						"||"+idTipoExpedienteRow+"||"+anioExpedienteRow+"||"+numeroExpedienteRow;
					
							boolean isChecked = false;
							

							for (int z = 0; z < registrosSeleccionados.size(); z++) {
									
								Hashtable clavesRegistro = (Hashtable) registrosSeleccionados
										.get(z);
								
								if (valorCheck.equals((String)clavesRegistro.get("CLAVE"))) {
									
									isChecked = true;
									break;
								} 
								

							}
							
								if (isChecked) {
			%>
								
									<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" checked onclick="pulsarCheck(this)">
								<%
									} else {
								%>
									<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" onclick="pulsarCheck(this)" >
							<%
								}
							
							%>
					</td>
					<%if(!isInstitucion){ %>
						<td><%=fila.getString("ABREVIATURA")%></td>
					<%} %>
					
						<td>
						
							
							<input type="hidden" name="idInstitucion<%=""+(i+1)%>" value="<%=idInstitucionRow%>">
							<input type="hidden" name="idInstitucionTipoExp<%=""+(i+1)%>" value="<%=idInstitucionTipoExpRow%>">
							<input type="hidden" name="idTipoExpediente<%=""+(i+1)%>" value="<%=idTipoExpedienteRow%>">
							<input type="hidden" name="numeroExpediente<%=""+(i+1)%>" value="<%=numeroExpedienteRow%>">
							<input type="hidden" name="anioExpediente<%=""+(i+1)%>" value="<%=anioExpedienteRow%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=idInstitucionRow%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idInstitucionTipoExpRow%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=idTipoExpedienteRow%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=numeroExpedienteRow%>">
							<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=anioExpedienteRow%>">
							
							
							<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRETIPOEXPEDIENTE"))%>">
							
							<%=fila.getString("NOMBRETIPOEXPEDIENTE")%>
						</td>
					
					<td><%=fila.getString("ANIOEXPEDIENTE")+" / "+fila.getString("NUMEROEXPEDIENTE")%></td>
					<td><%=("".equals(fila.getString("EXPRELACIONADO")))?"&nbsp;":fila.getString("EXPRELACIONADO")%></td>					
					<td><%=fila.getString("NOMBREFASE").equals("")?"&nbsp;":fila.getString("NOMBREFASE") %></td>
					<td><%=fila.getString("NOMBREESTADO").equals("")?"&nbsp;":fila.getString("NOMBREESTADO") %></td>
					<td><%=GstDate.getFormatedDateShort("",fila.getString("FECHA"))%></td>
					<td><%=fila.getString("DENUNCIADO")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			<%if(isBusquedaAvanzada){%>
				<siga:ConjBotonesAccion botones="V,GX,COM" />
			<%}else{%>
				<siga:ConjBotonesAccion botones="GX,COM" />	
			<%} %>
			
			
  
			

<%if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
			: registrosSeleccionados.size()));
%>
	  
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								registrosSeleccionados="<%=regSeleccionados%>"
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20;  z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      <%}%>
			

		<!-- FIN: LISTA DE VALORES -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="EXP"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>	
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		ObjArray = new Array();
		
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}
 
	   
	function pulsarCheck(obj){
		
		if (!obj.checked ){
		   		
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
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
	   		 	
		   		Hashtable claves= (Hashtable) registrosSeleccionados.get(p);
		   		
		   		
				valorCheckPersona=(String)claves.get("CLAVE");
				
						
				%>
					var aux='<%=valorCheckPersona%>';
					ObjArray.push(aux);
				<%
			} 
	   	}%>
	   	
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
			   		<%if(isBusquedaAvanzada){%>
			   		document.forms[0].target = "mainWorkArea";
			   		<%}else{ %>
			   	 	document.forms[0].target = "resultado";
			   	 	<%}%>
			   	 	
			   	 	
					document.forms[0].modo.value = "buscarPor";
					document.forms[0].seleccionarTodos.value = "<%=paginaSeleccionada%>";
					document.forms[0].submit();
					
				}else{
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;
				 	
				 	seleccionados1=ObjArray;
				 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPersona");
						
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled)	
							ele[i].checked = false; 
							
					}
		
				 }
		   	  
		   	  }else{
		   	  	if (!o.checked ){
			   	  		var ele = document.getElementsByName("chkPersona");
							
					  	for (i = 0; i < ele.length; i++) {
					  		if(!ele[i].disabled){
					  			if(ele[i].checked){	
			     					ele[i].checked = false;
			     				
									ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
								}
							}
					   	}
					   	
					   	seleccionados1=ObjArray;
				   }else{
					   	var ele = document.getElementsByName("chkPersona");
								
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
		   	  		 
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		}
	 }
	   
	function checkTodos(){
	
	 	var ele = document.getElementsByName("chkPersona");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
   			if(!ele[i].checked){
   				todos=0;
   				break;
   			} 
   		}
	   
	   if (todos==1){
			document.getElementById("chkGeneral").checked=true;
		}else{
			document.getElementById("chkGeneral").checked=false;
		}
	   
				
			
			
   	}
   	
   
		
	
	
		function comunicar(fila)
		{
		
			
			
			idInstitucion= document.getElementById ('idInstitucion'+fila).value;
			idInstitucionTipoExp= document.getElementById ('idInstitucionTipoExp'+fila).value;
			idTipoExpediente= document.getElementById ('idTipoExpediente'+fila).value;
			anioExpediente= document.getElementById ('anioExpediente'+fila).value;
			numeroExpediente= document.getElementById ('numeroExpediente'+fila).value;

		 
			
			
		   	datos = "idInstitucion=="+idInstitucion +"##idInstitucionTipoExp=="+idInstitucionTipoExp +
 		   		 "##idTipoExp==" +idTipoExpediente+"##anioExpediente=="+anioExpediente 
 		   		 +"##numeroExpediente=="+numeroExpediente +"##idTipoInforme==EXP%%%";
		  //"+"##idPersona=="+idPersona QUITO EL DENUNCIADO PRINCIPAL YA QUE solo sirve a efectos de interfaz de
			//la caratula del expediente, no de la gestion de informes y envios
			document.InformesGenericosForm.datosInforme.value =datos;

			
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
			
					
				
		}
		
			
				
			
	
   	
	function accionComunicar()
		{
		
			datos = "";
		
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idInstitucionTipoExp  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idTipoExp  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			anioExpediente  = idRegistros.substring(0,index);
			numeroExpediente = idRegistros.substring(index+2);
			
			
			
			
 		   	datos = datos +"idInstitucion=="+idInstitucion +"##idInstitucionTipoExp=="+idInstitucionTipoExp +
 		   		 "##idTipoExp==" +idTipoExp+"##anioExpediente=="+anioExpediente 
 		   		 +"##numeroExpediente=="+numeroExpediente  +"##idTipoInforme==EXP%%%";
 		 //"+"##idPersona=="+idPersona QUITO EL DENUNCIADO PRINCIPAL YA QUE solo sirve a efectos de interfaz de
			//la caratula del expediente, no de la gestion de informes y envios
			
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
			if(numElementosSeleccionados>50){
				document.InformesGenericosForm.descargar.value ='0';
			}
			else{
				document.InformesGenericosForm.descargar.value ='1';
			}
			document.InformesGenericosForm.datosInforme.value =datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
					
		}
		function accionGenerarExcels(){
   		sub();
			
			datos = "";
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idInstitucionTipoExp  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idTipoExp  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			anioExpediente  = idRegistros.substring(0,index);
			numeroExpediente = idRegistros.substring(index+2);
 		   	 
 		   	datos = datos +idInstitucion +","+idInstitucionTipoExp +
 		   		 "," +idTipoExp+","+anioExpediente 
 		   		 +","+numeroExpediente  +"#";
 		   	
 		   	
 		   		   	
 		   	
 		   	
			
			
		}
		if (datos == '') {
			
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
		document.forms[0].tablaDatosDinamicosD.value = datos;
		document.forms[0].modo.value ='generaExcel';
			
			
		//alert("datosPaginador:"+document.forms[0].datosPaginador )
		//alert("registrosSeleccionados:"+document.forms[0].registrosSeleccionados)
		//alert("si:"+document.forms[0].datosPaginador)
		document.forms[0].submit();

		
		fin();
			
   	}
   	
   	
   	
		function refrescarLocal()
		{			
			if(parent.buscar)
				parent.buscar() ;
			else
				accionVolver();
			
		}
		function accionCerrar() 
	{		
		window.top.close();
	}
	function accionVolver() 
	{		
		document.forms[0].modo.value="abrirAvanzada";
		document.forms[0].target="mainWorkArea";	
		document.forms[0].submit();	
	}
	 
		

	</script>
	
		
	</body>
</html>