<!-- listadoSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsSOJBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsDefinirSOJAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>


<%@ page import="com.atos.utils.Row"%>
 

<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<bean:define id="registrosSeleccionados" name="DefinirSOJForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="DefinirSOJForm" property="datosPaginador" type="java.util.HashMap"/>
<% 

	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idioma=usr.getLanguage().toUpperCase();
	String idInstitucion = usr.getLocation();
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
		String action=app+"/JGR_ExpedientesSOJ.do?noReset=true";
    /**************/
String informeUnico =(String) request.getAttribute("informeUnico");
	
%>

<%@page import="com.siga.Utilidades.paginadores.PaginadorBind"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<title><siga:Idioma key="gratuita.busquedaSOJ.literal.expedientesSOJ"/></title>
	
	<script type="text/javascript">
		function refrescarLocal() 
		{
			parent.buscar();
		}
	</script>
</head>

<body onload="cargarChecks();checkTodos();">
	<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<html:form action="/JGR_ExpedientesSOJ.do" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados" />
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		<input type="hidden" name="actionModal"  id="actionModal" value="">
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoSOJ"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/> ,gratuita.busquedaSOJ.literal.apertura, gratuita.busquedaSOJ.literal.turno, gratuita.busquedaSOJ.literal.guardia, gratuita.busquedaSOJ.literal.anyo, gratuita.busquedaSOJ.literal.codigo, gratuita.busquedaSOJ.literal.tipoSOJ, gratuita.busquedaSOJ.literal.SOJColegio,gratuita.busquedaSOJ.literal.estadoSOJ, gratuita.busquedaSOJ.literal.nombreSolicitante,"
		   tamanoCol="5,7,13,13,4,5,9,9,8,15"
		   alto="258" 
		   activarFilaSel="true" 
		   ajustePaginador="true">

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String botones = "";
	    	String select = "";
	    	Vector v = null;
	    	ScsDefinirSOJAdm scsDefinirSOJAdm = new ScsDefinirSOJAdm(usr);
			while (recordNumber-1 < resultado.size())
			{	
				 Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();		
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				// Comprobamos el estado del idfacturacion
				String idFacturacion =  (String)registro.get("IDFACTURACION");;
				String nombre=  (String)registro.get("NOMBRE");
				if(idFacturacion==null||idFacturacion.equals("")) botones = "C,E,B";
				else botones = "C,B";
				
				String IDPERSONAJG=null;
				if(registro.get(ScsSOJBean.C_IDPERSONAJG)==null||registro.get(ScsSOJBean.C_IDPERSONAJG).equals(""))
					IDPERSONAJG=" ";
				else
					IDPERSONAJG=(String)registro.get(ScsSOJBean.C_IDPERSONAJG);
					
				String CODIGO=null;
				if(registro.get(ScsSOJBean.C_NUMSOJ)==null||registro.get(ScsSOJBean.C_NUMSOJ).equals("")){
					CODIGO="&nbsp;";
				}else{
					if(registro.get(ScsSOJBean.C_SUFIJO)!=null && !registro.get(ScsSOJBean.C_SUFIJO).equals("")){
					  CODIGO=(String)registro.get(ScsSOJBean.C_NUMSOJ)+'-'+(String)registro.get(ScsSOJBean.C_SUFIJO);
					}else{
					  CODIGO=(String)registro.get(ScsSOJBean.C_NUMSOJ);	
					}
				}	
					
				
				String nombreTurno=ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),registro.get(ScsSOJBean.C_IDTURNO).toString());
				
				
				String nombreGuardia=ScsGuardiasColegiadoAdm.getNombreGuardia(usr.getLocation(),registro.get(ScsSOJBean.C_IDTURNO).toString(),registro.get(ScsSOJBean.C_IDGUARDIA).toString());
				
				
				String tipoSoj=ScsDefinirSOJAdm.getNombreTipoSOJ(registro.get(ScsSOJBean.C_IDTIPOSOJ).toString());
				String descTipoSoj="";
				String descTipoSojColegio="";
				if (tipoSoj!=null &&	!tipoSoj.equals("")){
				   descTipoSoj=UtilidadesMultidioma.getDatoMaestroIdioma(tipoSoj.toString(),usr);
				}
				   
				
				String tipoSojColegio=ScsDefinirSOJAdm.getNombreTipoSOJColegio(usr.getLocation().toString(),registro.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString());
				if (tipoSojColegio!=null &&	!tipoSojColegio.equals("")){
				   descTipoSojColegio=UtilidadesMultidioma.getDatoMaestroIdioma(tipoSojColegio.toString(),usr);
				}
				
				
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td align="center">
					<%String valorCheck = registro.get("IDINSTITUCION")+"||"+registro.get("IDTIPOSOJ")+"||"+registro.get("ANIO")+"||"+registro.get("NUMERO");
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
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsSOJBean.C_IDTIPOSOJ)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=IDPERSONAJG%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsSOJBean.C_NUMERO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=registro.get(ScsSOJBean.C_ANIO)%>">
					<%=GstDate.getFormatedDateShort("",registro.get(ScsSOJBean.C_FECHAAPERTURA).toString())%>&nbsp;</td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombreTurno)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombreGuardia)%></td>					
					<td><%=UtilidadesString.mostrarDatoJSP(registro.get(ScsSOJBean.C_ANIO))%></td>
					<td><%=CODIGO%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(descTipoSoj)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(descTipoSojColegio)%></td>
					<td><%if (registro.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("A")) {%><siga:Idioma key="gratuita.SOJ.estado.abierto"/><%} else if (registro.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("P")){%><siga:Idioma key="gratuita.SOJ.estado.pendiente"/><%} else {%><siga:Idioma key="gratuita.SOJ.estado.cerrado"/><%}%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombre)%></td>					
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
			
	<%
	}else {
	%>
	<br>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>
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
<!-- INICIO: SUBMIT AREA -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="SOJ"/>
	<html:hidden property="enviar" value = "0"/>
	<html:hidden property="descargar" value = "1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>


	<iframe name="submitArea21" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
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
			
 		   	datos = datos +"idinstitucion=="+idInstitucion + "##idtipo==" +idTipo+"##anio=="+anio +"##numero==" +numero+"%%%";
			
			
		}
		return datos;
   	}
   	
   	function accionGenerarCarta()
		{
		sub();
		datos =  getDatosSeleccionados();
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
		
		numElementosSeleccionados =  ObjArray.length; 
		
		confirmar = '';
		confirmar += "<siga:Idioma key='general.confirmar.demora' arg0='"+numElementosSeleccionados+"'/>";
		if(numElementosSeleccionados<=50 ||confirm(confirmar)){
			document.InformesGenericosForm.datosInforme.value=datos;
			if(document.getElementById("informeUnico").value=='1'){
				document.InformesGenericosForm.submit();
			}else{
			
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
				  fin(); 		
			   	} 
			   	else {
			   		fin();
			   	}
			}
			
		}else {
			fin();
		}
	}
		
	
	</script>
	
</body>	
</html>
	