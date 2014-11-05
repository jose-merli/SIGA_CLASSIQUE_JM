<!DOCTYPE html>
<html>
<head>
<!-- listadoEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>  
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.ScsTurnoAdm"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>

<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<bean:define id="registrosSeleccionados" name="DefinirEJGForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="DefinirEJGForm" property="datosPaginador" type="java.util.HashMap"/>
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idInstitucion = usr.getLocation();
	boolean esComision = usr.isComision();
	boolean esComisionMultiple = usr.getInstitucionesComision()!=null &&usr.getInstitucionesComision().length>1;
	
	String idioma=usr.getLanguage().toUpperCase();
	
	//Vector obj = (Vector) ses.getAttribute("resultado");
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
		String action=app+"/JGR_EJG.do?noReset=true";
    /**************/

	
%>

<%@page import="com.siga.Utilidades.PaginadorBind"%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	
</head>

<body onload="cargarChecks();checkTodos();">

	<html:form action="/JGR_EJG.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
	</html:form>
	<%
		
		StringBuffer  nombreColumnas = new StringBuffer("<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>");
		StringBuffer tamanioColumnas =  new StringBuffer("5");
		if(esComisionMultiple){
			nombreColumnas.append(",ICA");
			tamanioColumnas.append(",6");
			tamanioColumnas.append(",12,13,5,6,13,9,9,12,10");
			
		}else{
			tamanioColumnas.append(",13,13,5,6,15,9,10,14,10");
			
		}
		nombreColumnas.append(" ,gratuita.busquedaEJG.literal.turnoGuardiaEJG, gratuita.busquedaEJG.literal.turnoDesignacion, gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.letradoDesignacion, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante,");
		
	%>


	
		
	
		
	<siga:Table 		   
		   name="listadoEJG"
		   border="1"
		   columnNames="<%=nombreColumnas.toString() %>"
		   columnSizes="<%=tamanioColumnas.toString() %>">

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	String botones = "";
	    	String fRatificacion = "";
			while (recordNumber-1 < resultado.size())
			{			
			  
		    Row fila = (Row)resultado.elementAt(recordNumber-1);
			Hashtable registro = (Hashtable) fila.getRow();
			
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado del idfacturacion
	    	ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usr);
				
			// Creamos el Turno/Guardia EJG
			String turno = ScsTurnoAdm.getNombreTurnoJSP((String)registro.get("IDINSTITUCION"),(String)registro.get("GUARDIATURNO_IDTURNO"));
			String guardia = ScsGuardiasTurnoAdm.getNombreGuardiaJSP((String)registro.get("IDINSTITUCION"),(String)registro.get("GUARDIATURNO_IDTURNO"),(String)registro.get("GUARDIATURNO_IDGUARDIA")) ;
			String turnoGuardia = " ";
			if ((turno!="")||(guardia!="")){
				turnoGuardia = turno + "/ " + guardia ;
			}
			

	    	fRatificacion = (String)registro.get("FECHARATIFICACION");

	    	
	   		String idFacturacion =  (String)registro.get("IDFACTURACION");
			
	    	boolean isModificable = ((idFacturacion==null||idFacturacion.equals("")) ||(idFacturacion!=null &&(fRatificacion==null||fRatificacion.equals(""))));
	    	if(esComision){
	    		if(isModificable ) botones = "C,E";
				else botones = "C";
	    	}else{
				if(isModificable ) botones = "C,E,B";
				else botones = "C,B";
	    	}
			String CODIGO=null;
			if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
				CODIGO="&nbsp;";
			else
				CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);

			%>
			
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td align="center">
						<%String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("IDTIPOEJG")+"||"+registro.get("ANIO")+"||"+registro.get("NUMERO");
						boolean isChecked = false;
						for (int z = 0; z < registrosSeleccionados.size(); z++) {
							Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
							String clave = (String)clavesRegistro.get("CLAVE");
							if (valorCheck.equals(clave)) {
								isChecked = true;
								break;
							}
						}if (isChecked) {%>
								<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" checked onclick="pulsarCheck(this)">
							<%} else {%>
								<input type="checkbox" value="<%=valorCheck%>"  name="chkPersona" onclick="pulsarCheck(this)" >
						<%}%>
						</td>
						<% if(esComisionMultiple){%>
							<td><%=(String)registro.get("INST_ABREV")%></td>
						<%}%>
					<td><%=turnoGuardia%>&nbsp;</td>
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" id="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" id="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=(String)registro.get(ScsEJGBean.C_IDINSTITUCION)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" id="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" id="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					<input type='hidden' name='datosCarta' value='idinstitucion==<%=(String)registro.get(ScsEJGBean.C_IDINSTITUCION)%>##idtipo==<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>##anio==<%=registro.get(ScsEJGBean.C_ANIO)%>##numero==<%=registro.get(ScsEJGBean.C_NUMERO)%>'><%=registro.get("TURNODESIGNA")%></td>
					
					<td><%=registro.get(ScsEJGBean.C_ANIO)%></td>
					 <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
						<td><%=CODIGO%>-<%=(String)registro.get(ScsEJGBean.C_SUFIJO)%></td>
						<%}else{%>
						<td><%=CODIGO%></td>
					<% }%>
					
					<td><%=registro.get("LETRADODESIGNA")%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("DESC_ESTADO"), usr) %>&nbsp;</td>
					<td><%=ScsEJGAdm.getUnidadEJG((String)registro.get(ScsEJGBean.C_IDINSTITUCION),(String)registro.get(ScsEJGBean.C_IDTIPOEJG),(String)registro.get(ScsEJGBean.C_ANIO),(String)registro.get(ScsEJGBean.C_NUMERO)) %>&nbsp;</td>
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
	<%
	}else {
	%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
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
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value='<%= usr.isComision() ?"CAJG":"EJG"%>'/>
	<html:hidden property="enviar" value = "1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>

<html:form action="/JGR_ActasComision" method="POST" target="submitArea">
	<input type='hidden' name='idInstitucion' value='<%=usr.getLocation()%>'>
	<input type='hidden' name='modo' value=''>
	<input type='hidden' name='accion' value=''>
	<input type='hidden' name='seleccionados' value=''>
	<input type='hidden' name='actionModal'>
</html:form>	

<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>
	 
<!-- INICIO: SUBMIT AREA -->
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
  		var conf = confirm("<siga:Idioma key='paginador.message.marcarDesmarcar'/>"); 
	   	 
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
   	function getDatosSeleccionados(){
   		datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			
			

			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idTipo  = idRegistros.substring(0,index);
			
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			anio  = idRegistros.substring(0,index);
			
			numero = idRegistros.substring(index+2);
			
 		   	datos = datos +"idinstitucion=="+idInstitucion + "##idtipo==" +idTipo+"##anio=="+anio +"##numero==" +numero+"##idTipoInforme==EJG%%%";
			
			
		}
		return datos;
   	}
   	function refrescarLocal(){
		parent.buscar();
	}
   	function accionGenerarCarta()
		{
		
		//sub();
		datos =  getDatosSeleccionados();
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
		
		numElementosSeleccionados =  ObjArray.length; 
		
		
		if(numElementosSeleccionados>50){
			document.InformesGenericosForm.descargar.value = '0';
		}
		else{
			document.InformesGenericosForm.descargar.value = '1';
			
		}
		document.InformesGenericosForm.datosInforme.value=datos;
		
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
		
   	function accionEditarSeleccionados(){
	
		sub();
		seleccionados =  getDatosSeleccionados();
		if (seleccionados == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}

		document.ActaComisionForm.seleccionados.value=seleccionados;
		document.ActaComisionForm.modo.value='edicionMasiva';
		var resultado = ventaModalGeneral("ActaComisionForm","M");	
		fin();
	}
	</script>
</body>	
</html>
	