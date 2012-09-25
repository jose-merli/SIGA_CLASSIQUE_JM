<!-- listadoEJG_RemesaListos.jsp -->
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
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsMaestroEstadosEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@page import="com.siga.beans.ScsTurnoAdm"%>
<%@page import="com.siga.beans.ScsGuardiasTurnoAdm"%>

<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>

<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idremesa=(String)request.getAttribute("idremesa");
	String idioma=usr.getLanguage().toUpperCase();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String valor="";
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	Vector ejgSeleccionados=null;
	
 if (ses.getAttribute("DATAPAGINADOR")!=null){
 
	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	 ejgSeleccionados=(Vector)ses.getAttribute("EJG_SELECCIONADOS");
	
	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  	resultado = (Vector)hm.get("datos");
		
	  	PaginadorBind paginador = (PaginadorBind)hm.get("paginador");
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
		String action=app+"/JGR_E-Comunicaciones_Gestion.do?noReset=true";
    /**************/

	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
		function refrescarLocal()
		{
			
			parent.buscar();
		}
	</script>
</head>

<body onload="cargarChecks();checkTodos()">

	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<!-- RGG: cambio a formularios ligeros -->
		
		<input type="hidden" name="actionModal" value="a">
		<input type="hidden" name="selDefinitivo" value="">
		<input type="hidden" name="idRemesa" value="">
		
		
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoEJG"
		   borde="1"
		   clase="tableTitle"		   
		   nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,gratuita.busquedaEJG.literal.turno, gratuita.busquedaEJG.literal.guardia, gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.tipoEJG, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante"
		   tamanoCol="5,15,15,5,6,15,9,10"
		   alto="100%" 
		   ajustePaginador="true"
		   ajusteBotonera="true" >
			
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

	    	
			String CODIGO=null;
			if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
				CODIGO="&nbsp;";
			else
				CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);

			%>
			<tr class=<% if (recordNumber % 2 == 0) { %>'filaTablaPar'
					  <% } else { %>'filaTablaImpar'
					<% } %>
			>
					<td>
						<%
							String valorCheck="";
							valorCheck=registro.get(ScsEJGBean.C_ANIO)+"||"+registro.get(ScsEJGBean.C_NUMERO)+"||"+registro.get(ScsEJGBean.C_IDTIPOEJG);
							boolean encontrado=false;
							int z=0;
							
							while (z<ejgSeleccionados.size() && !encontrado){
								Hashtable clavesEJG= (Hashtable) ejgSeleccionados.get(z);
								
								if ((registro.get(ScsEJGBean.C_ANIO).equals(clavesEJG.get(ScsEJGBean.C_ANIO)))&& (registro.get(ScsEJGBean.C_NUMERO).equals(clavesEJG.get(ScsEJGBean.C_NUMERO))) && (registro.get(ScsEJGBean.C_IDTIPOEJG).equals(clavesEJG.get(ScsEJGBean.C_IDTIPOEJG)))){
									if (clavesEJG.get("SELECCIONADO").equals("1")){
										encontrado=true;
									}else{
										encontrado=false;
									}
									break;
								}else{
									encontrado=false;
								}
								z++;
								
								
							}
							
							if (encontrado){%>
							
								<input type="checkbox" value="<%=valorCheck%>" name="chkGuardia" checked="true" onclick="pulsarCheck(this)">
							<%}else{%>
								<input type="checkbox" value="<%=valorCheck%>" name="chkGuardia" onclick="pulsarCheck(this)" >
							<%}
						%>
						
					</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO")),usr) %>&nbsp;</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(ScsGuardiasTurnoAdm.getNombreGuardiaJSP(usr.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO"),(String)registro.get("GUARDIATURNO_IDGUARDIA")),usr) %>&nbsp;</td>
					<td>
					
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					<input type='hidden' name='datosCarta' value='idinstitucion==<%=usr.getLocation()%>##idtipo==<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>##anio==<%=registro.get(ScsEJGBean.C_ANIO)%>##numero==<%=registro.get(ScsEJGBean.C_NUMERO)%>'>
					
					<%=registro.get(ScsEJGBean.C_ANIO)%></td>
					<td><%=CODIGO%></td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("TIPOEJG"),usr)%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("DESC_ESTADO"), usr) %>&nbsp;</td>
					<td><%=ScsEJGAdm.getUnidadEJG(usr.getLocation(),(String)registro.get(ScsEJGBean.C_IDTIPOEJG),(String)registro.get(ScsEJGBean.C_ANIO),(String)registro.get(ScsEJGBean.C_NUMERO)) %>&nbsp;</td>
			</tr>
			<% 	recordNumber++;		   
			} %>
	<%
	}else {
	%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	
	%>
		
	</siga:TablaCabecerasFijas>

     <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarListos"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:28px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	
	 
	 
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
		  	
		  	
		   document.DefinicionRemesas_CAJG_Form.selDefinitivo.value=seleccionados1;
	   }
	   function cargarChecks(){
	   		
	   		
	   		 <% 
	   		 if (ejgSeleccionados!=null){
	   		 	for (int p=0;p<ejgSeleccionados.size();p++){
	   		 	
		   			Hashtable clavesEJG= (Hashtable) ejgSeleccionados.get(p);
					valor=clavesEJG.get(ScsEJGBean.C_ANIO)+"||"+clavesEJG.get(ScsEJGBean.C_NUMERO)+"||"+clavesEJG.get(ScsEJGBean.C_IDTIPOEJG);
					if (clavesEJG.get("SELECCIONADO").equals("1")){%>
						var aux='<%=valor%>';
						ObjArray.push(aux);
					<%}%>
			<%} }%>
			ObjArray.toString();
			seleccionados1=ObjArray;
			document.DefinicionRemesas_CAJG_Form.selDefinitivo.value=seleccionados1;
	   }
	   
	   function cargarChecksTodos(o){
	   	
	   	  if (o.checked){
	   		 <% 
	   		 if (ejgSeleccionados!=null){
	   		 	for (int p=0;p<ejgSeleccionados.size();p++){
	   		 	
		   			Hashtable clavesEJG= (Hashtable) ejgSeleccionados.get(p);
					valor=clavesEJG.get(ScsEJGBean.C_ANIO)+"||"+clavesEJG.get(ScsEJGBean.C_NUMERO)+"||"+clavesEJG.get(ScsEJGBean.C_IDTIPOEJG);%>
					
						var aux='<%=valor%>';
						ObjArray.push(aux);
					
			<%} }%>
			ObjArray.toString();
			seleccionados1=ObjArray;
			document.DefinicionRemesas_CAJG_Form.selDefinitivo.value=seleccionados1;
			
			var ele = document.getElementsByName("chkGuardia");
				
				for (i = 0; i < ele.length; i++) {
					
					ele[i].checked = true;
					
				}
			
		 }else{
		 	ObjArray1= new Array();
		 	ObjArray=ObjArray1;
		 	seleccionados1=ObjArray;
			document.DefinicionRemesas_CAJG_Form.selDefinitivo.value=seleccionados1;
			var ele = document.getElementsByName("chkGuardia");
				
				for (i = 0; i < ele.length; i++) {
					
					ele[i].checked = false;
					
				}

		 }
		
	   }
	   function checkTodos(){
	   	var todos=1;
	   	
	   
	   <% 
	   		 if (ejgSeleccionados!=null){
	   		 	
	   		 	for (int p=0;p<ejgSeleccionados.size();p++){
	   		 	
		   			Hashtable clavesEJG= (Hashtable) ejgSeleccionados.get(p);
					valor=clavesEJG.get(ScsEJGBean.C_ANIO)+"||"+clavesEJG.get(ScsEJGBean.C_NUMERO)+"||"+clavesEJG.get(ScsEJGBean.C_IDTIPOEJG);
					if (!clavesEJG.get("SELECCIONADO").equals("1")){%>
						todos=0;
						
					<%}%>
			<%} }%>
				
			if (todos==1){
				document.getElementById("chkGeneral").checked=true;
			}else{
				document.getElementById("chkGeneral").checked=false;
			}
			
	   }
	   
	   
	   
	   
	  
			</script>
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	