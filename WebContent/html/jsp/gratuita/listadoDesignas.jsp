<!-- listadoDesignas.jsp -->
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

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.ScsDesignaAdm"%>


<bean:define id="registrosSeleccionados" name="BuscarDesignasForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="BuscarDesignasForm" property="datosPaginador" type="java.util.HashMap"/>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idInstitucionLocation = usr.getLocation();
	String idioma=usr.getLanguage().toUpperCase();
	//Vector obj = (Vector)request.getAttribute("resultado");
	String estado = "";
	ses.removeAttribute("resultado");
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	
	String valorCheckPersona = "";
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
		String action=app+"/JGR_Designas.do?noReset=true";
    /**************/
	ScsDesignaAdm desigAdm = new ScsDesignaAdm(usr);
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body onload="cargarChecks();checkTodos()" class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_MantenimientoDesignas.do" method="post" target="mainWorkArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>
			<html:hidden property="registrosSeleccionados" />
			<html:hidden property="datosPaginador" />
			<html:hidden property="seleccionarTodos" />
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
		<html:form action="/JGR_Designas" method="POST" target="resultado" style="display:none">			
		    <html:hidden property = "modo" value = ""/>
		    <html:hidden property="registrosSeleccionados" />
			<html:hidden property="datosPaginador" />
			<html:hidden property="seleccionarTodos" />
		</html:form>	
		
			<siga:TablaCabecerasFijas 
			   nombre="tablaDatos"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/> ,
			   gratuita.listarGuardias.literal.turno,facturacion.ano,gratuita.busquedaDesignas.literal.codigo,gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.estado,gratuita.listarDesignasTurno.literal.nColegiado,expedientes.auditoria.literal.nombreyapellidos,pestana.justiciagratuitaejg.interesado,gratuita.busquedaDesignas.literal.validada,"
			   tamanoCol="5,11,4,8,7,5,7,13,14,6,9" 
			   alto="100%" 
		       ajustePaginador="true"
		       activarFilaSel="true" >
				
		<%if (resultado.size()<1){%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
		<%}else{%>
			  <%
		    	String anioAnt="", numeroAnt="", turnoAnt="";
				String turnoDesig="";
				String actNoValida="";
				String defendidos="";
				String letradoDesig="";
				String IDletradoDesig="";
				String fechaEntrada="";
				String nColegiado="";
		    	
				for (int recordNumber = 1,contadorFila=1; recordNumber-1 < resultado.size(); recordNumber++)
				{	
					Row fila = (Row)resultado.elementAt(recordNumber-1);
					Hashtable registro = (Hashtable) fila.getRow();
					
					
						String idInstitucion = (String) registro.get("IDINSTITUCION"); 
						String anioNow   = (String) registro.get("ANIO");
						String numeroNow = (String) registro.get("NUMERO");
						String turnoNow  = (String) registro.get("IDTURNO");
						//String idPersona = (String) registro.get("IDPERSONA");
						
						
						
				     
						
					
					
					
					turnoDesig =  desigAdm.getNombreTurnoDes(usr.getLocation(), (String) registro.get("IDTURNO"));
					 actNoValida =  desigAdm.getActDesig_NoValidar(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO"));
					 defendidos =  desigAdm.getDefendidosDesigna(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO"),"1");
					 letradoDesig =  desigAdm.getLetradoDesig(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO"));
					 IDletradoDesig =  desigAdm.getIDLetradoDesig(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO"));
					 fechaEntrada = GstDate.getFormatedDateShort("",desigAdm.getFechaEntrada(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO")));
					 if (IDletradoDesig==null || IDletradoDesig.equals("")){
					  IDletradoDesig=" ";
					 } 
					 nColegiado =  desigAdm.getNColegiadoDesig(usr.getLocation(), (String) registro.get("IDTURNO"),(String) registro.get("ANIO"),(String) registro.get("NUMERO"));
					 if (nColegiado==null || nColegiado.equals("")){
					  nColegiado=" ";
					 } 
					
					
					
					estado = (String) registro.get("ESTADO");
					if (estado!=null){
						if (estado.equalsIgnoreCase("V")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.abierto");
						else if (estado.equalsIgnoreCase("F")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.finalizado");
							 else if(estado.equalsIgnoreCase("A")) estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.designa.estado.anulado");
							 	  else estado="";
								 
					}
					
					
			 	%>	
				  	<siga:FilaConIconos fila='<%=String.valueOf(contadorFila)%>' botones="E,C,B" clase="listaNonEdit">
						
						<td align="center">
					<%
					String valorCheck = idInstitucion+"||"+anioNow+"||"+turnoNow+"||"+numeroNow;
					
							boolean isChecked = false;
							

							for (int z = 0; z < registrosSeleccionados.size(); z++) {
									
								Hashtable clavesRegistro = (Hashtable) registrosSeleccionados
										.get(z);
								String clave = (String)clavesRegistro.get("CLAVE");
								
								if (valorCheck.equals(clave)) {
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
						<td>
							<input type='hidden' name='oculto<%=String.valueOf(contadorFila)%>_1' value='<%= registro.get("IDTURNO")%>'>
							<input type='hidden' name='oculto<%=String.valueOf(contadorFila)%>_2' value='<%=IDletradoDesig%>'>
							<input type='hidden' name='oculto<%=String.valueOf(contadorFila)%>_3' value='<%=registro.get("NUMERO")%>'>
							
							<input type='hidden' name='datosCarta' value='idinstitucion==<%=usr.getLocation()%>##idturno==<%=registro.get("IDTURNO")%>##anio==<%=registro.get("ANIO")%>##numero==<%=registro.get("NUMERO")%>##ncolegiado==<%=nColegiado%>##codigo==<%=registro.get("CODIGO")%>'>
							<input type='hidden' name='oculto<%=String.valueOf(contadorFila)%>_4' value='<%=registro.get("ANIO")%>'>
							<%=turnoDesig%>&nbsp;
						</td>
						<td><%=registro.get("ANIO")%>&nbsp;</td>
				<!--	<td>< %=registro.get("NUMERO")%>&nbsp;</td> -->
				        <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
						<td><%=registro.get("CODIGO")%>-<%=registro.get("SUFIJO")%>&nbsp;</td>
						<%}else{%>
						<td><%=registro.get("CODIGO")%>&nbsp;</td>
						<% }%>
						<!-- td><%if (registro.get("FECHAENTRADA")!=null){out.print (GstDate.getFormatedDateShort("",registro.get("FECHAENTRADA").toString()));}%>&nbsp;</td-->
						<td><%=fechaEntrada%>&nbsp;</td>
						<td><%=estado%></td>
						<td><%=nColegiado%>&nbsp;</td>
						<td><%=letradoDesig%>&nbsp;</td>
						<td><%=UtilidadesString.mostrarDatoJSP(defendidos)%>&nbsp;</td>
						<td><%=actNoValida%>&nbsp;</td>
					</siga:FilaConIconos>	
					
				<% contadorFila++;
				} // for%>	
		<%}%>
	</siga:TablaCabecerasFijas>
	<%
	String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0
			: registrosSeleccionados.size()));
	
	if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								registrosSeleccionados="<%=regSeleccionados%>"
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	


<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
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
	   		 	
		   		Hashtable clavesEJG= (Hashtable) registrosSeleccionados.get(p);
		   		
		   		
				valorCheckPersona=(String)clavesEJG.get("CLAVE");
				
						
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
  		var conf = confirm("<siga:Idioma key="paginador.message.marcarDesmarcar"/>"); 
	   	 
	   	if (conf){
			ObjArray = new Array();
		   	if (o.checked){
		   		parent.seleccionarTodos('<%=paginaSeleccionada%>');
		   	 		
				
			}else{
				ObjArray1= new Array();
			 	ObjArray=ObjArray1;
			 	seleccionados1=ObjArray;
			 	if(seleccionados1){
				document.forms[0].registrosSeleccionados.value=seleccionados1;
				var ele = document.getElementsByName("chkPersona");
					
				for (i = 0; i < ele.length; i++) {
					if(!ele[i].disabled)	
						ele[i].checked = false; 
						
						
				}
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
	   	 if (document.getElementById('registrosSeleccionadosPaginador')){ 		 
		  document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		 }
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
	function refrescarLocal(){
		parent.buscar();
	}
	function accionComunicar()
		{
		
			sub();
			datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			anio  = idRegistros.substring(0,index);

			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idTurno  = idRegistros.substring(0,index);
			
			numero = idRegistros.substring(index+2);
			
 		   	datos = datos +"idInstitucion=="+idInstitucion +"##anio=="+anio + "##idTurno==" +idTurno+"##numero==" +numero+"%%%";
			
			
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
			formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucionLocation%>'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='OFICI'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
			if(numElementosSeleccionados>50){
				formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='0'>"));
			}
			else{
				
				formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
			}
			
			document.appendChild(formu);
			formu.datosInforme.value=datos;
			formu.submit();
			
			
			
      	    					
					
				
		}		
		function accionCerrar() {
		
		}
</script>
	</body>
</html>
		  
		
