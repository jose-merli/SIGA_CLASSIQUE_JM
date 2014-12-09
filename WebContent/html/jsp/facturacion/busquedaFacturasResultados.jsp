<!DOCTYPE html>
<%@page import="com.siga.Utilidades.PaginadorBind"%>
<html>
<head>
<!-- busquedaFacturasResultados.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri="struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@taglib uri="libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<bean:define id="registrosSeleccionados" name="BusquedaFacturaForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="BusquedaFacturaForm" property="datosPaginador" type="java.util.HashMap"/>
<%
	String app=request.getContextPath(); 
	request.getSession().setAttribute("EnvEdicionEnvio", "FAC");
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();

	/** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheckFactura = "";
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
	
	

	String action = app + "/FAC_BusquedaFactura.do?noReset=true";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<title><siga:Idioma key="pys.gestionSolicitudes.titulo" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page='${sessionScope.SKIN}'/>" />


<!-- Incluido jquery en siga.js -->

<script type="text/javascript"
	src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<script>
	// Refrescar
	function refrescarLocal() { 		
		parent.buscar();
	}	
	
	function enviar(fila) {
	   	var auxFac = 'oculto' + fila + '_2';
	    var idFac = document.getElementById(auxFac);			          		
	    
	    document.DefinirEnviosForm.idFactura.value=idFac.value;
	   	
	   	document.DefinirEnviosForm.modo.value='envioModal';		   	
	   	document.DefinirEnviosForm.subModo.value='envioFactura';		   	
	   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
	   	if (resultado==undefined || resultado[0]==undefined || resultado[0]=="M"){
	   				   		
	   	} else {
	   		var idEnvio = resultado[0];
		    var idTipoEnvio = resultado[1];
		    var nombreEnvio = resultado[2];				    
		    
		   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
		   	document.DefinirEnviosForm.modo.value='editar';
		   	document.DefinirEnviosForm.submit();
	   	}
	}
</script>

<body onload="cargarChecks();checkTodos()">
	<html:form action="/FAC_BusquedaFactura.do?noReset=true" method="POST"
		target="mainWorkArea">
		<html:hidden property="modo" styleId="modo" value="" />
		<input type="hidden" id="actionModal" name="actionModal" value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
	</html:form>
	<siga:Table name="tablaResultados" border="1"
		columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,
	   				facturacion.buscarFactura.literal.Cliente,
			 		facturacion.buscarFactura.literal.Fecha,
	 				facturacion.buscarFactura.literal.NumeroFactura,
	   				facturacion.buscarFactura.literal.descripcion,			   							   					
	   				facturacion.buscarFactura.literal.Total,
	   				facturacion.buscarFactura.literal.Pendiente,
	   				facturacion.buscarFactura.literal.Estado,"
		columnSizes="5,18,8,13,18,8,8,12,10">

		<%
			if (resultado != null && resultado.size() > 0) {
					for (int i = 1; i <= resultado.size(); i++) {
						Row fila = (Row) resultado.elementAt(i - 1);
						Hashtable factura = (Hashtable) fila.getRow();

						if (factura != null) {
							Integer idInstitucion = UtilidadesHash.getInteger(factura, FacFacturaBean.C_IDINSTITUCION);
							String idFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_IDFACTURA);
							String numFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_NUMEROFACTURA);
							Long idPersona = UtilidadesHash.getLong(factura, FacFacturaBean.C_IDPERSONA);
							String nombreCompleto = UtilidadesHash.getString(factura, CenPersonaBean.C_NOMBRE) + " "
									+ UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS1) + " "
									+ UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS2);
							Double total = UtilidadesHash.getDouble(factura, FacFacturaBean.C_IMPTOTAL);
							Double pendiente = UtilidadesHash.getDouble(factura, FacFacturaBean.C_IMPTOTALPORPAGAR);
							String sEstado = UtilidadesHash.getString(factura, "DESCRIPCION_ESTADO");
							String fecha = UtilidadesHash.getString(factura, FacFacturaBean.C_FECHAEMISION);
							String descripcion = UtilidadesHash.getString(factura, FacFacturacionProgramadaBean.C_DESCRIPCION);
							fecha = GstDate.getFormatedDateShort("", fecha);

							// boton de envios
							FilaExtElement[] elems = new FilaExtElement[1];
							if (numFactura != null && !numFactura.trim().equals("")) {
								// si esta confirmada
								elems[0] = new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
							}
		%>
		<siga:FilaConIconos fila='<%="" + i%>' elementos="<%=elems%>"
			botones="E,C" visibleBorrado="false" pintarEspacio="no"
			clase="listaNonEdit">			
			<td align="center">
			<%String valorCheck = idFactura+"||"+numFactura;
			boolean isChecked = false;
			for (int z = 0; z < registrosSeleccionados.size(); z++) {
				Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
				String clave = (String)clavesRegistro.get("CLAVE");
				if (valorCheck.equals(clave)) {
					isChecked = true;
					break;
				}
			}if (isChecked) {%>
					<input type="checkbox" value="<%=valorCheck%>"  name="sel" checked onclick="pulsarCheck(this)">
			<%} else {%>
					<input type="checkbox" value="<%=valorCheck%>"  name="sel" onclick="pulsarCheck(this)" >
			<%}%>
			</td>
			
			<td>
				<!-- Datos ocultos tabla --> <input type="hidden"
				id="oculto<%=i%>_1" value="<%=idInstitucion%>"> <input
				type="hidden" id="oculto<%=i%>_2" value="<%=idFactura%>"> <input
				type="hidden" id="oculto<%=i%>_3" value="<%=idPersona%>"> <%=UtilidadesString.mostrarDatoJSP(nombreCompleto)%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(numFactura)%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(descripcion)%></td>
			<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(total.doubleValue()))%>&nbsp;&euro;</td>
			<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(pendiente.doubleValue()))%>&nbsp;&euro;</td>
			<td><%=sEstado%></td>
		</siga:FilaConIconos>
		<%
			} // if
					} // for

				} else {
		%>
		<tr class="notFound">
			<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
		</tr>
		<%
			}
		%>
	</siga:Table>

	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST"
		target="mainWorkArea">
		<html:hidden property="actionModal" value="" />
		<html:hidden property="modo" value="" />
		<html:hidden property="tablaDatosDinamicosD" value="" />
		<html:hidden property="subModo" value="" />
		<html:hidden property="idFactura" value="" />
	</html:form>

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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
<script language="javascript">
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
   		 	
	   		Hashtable clavesFac= (Hashtable) registrosSeleccionados.get(p);
	   		
	   		
			valorCheckFactura=(String)clavesFac.get("CLAVE");
			
					
			%>
				var aux='<%=valorCheckFactura%>';
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
			var ele = document.getElementsByName("sel");
				
			for (i = 0; i < ele.length; i++) {
				if(!ele[i].disabled)	
					ele[i].checked = false; 
					
					
			}
			}

		 }
   	  
   	  }else{
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
		   }else{
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
   	 if (document.getElementById('registrosSeleccionadosPaginador')){ 		 
	  document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	 }
	} 
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
   
   if (todos==1){
		document.getElementById("chkGeneral").checked=true;
	}else{
		document.getElementById("chkGeneral").checked=false;
	}
   
			
		
		
	}

</script>
</body>
</html>