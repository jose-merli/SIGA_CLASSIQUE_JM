<!DOCTYPE html>
<html>
<head>
<!-- consultaAbonos.jsp -->
<!-- 
	 Muestra los resultados de la busqueda de abonos
	 VERSIONES:
	 miguel.villegas 07-03-2005 Creacion
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorBind"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<bean:define id="registrosSeleccionados" name="GenerarAbonosForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="GenerarAbonosForm" property="datosPaginador" type="java.util.HashMap"/>

<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
			
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idioma=usr.getLanguage().toUpperCase();
	
	ConsPLFacturacion cpl = new ConsPLFacturacion(usr);
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
	
	String botonesFila = "";
	//String maximo=(String)request.getAttribute("maximo");

	request.getSession().setAttribute("EnvEdicionEnvio","GAF");   
    /** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheckAbono = "";
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
    
    
	String action=app+"/FAC_GenerarAbonos.do?noReset=true";

%>	


<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		


		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}			
			
					
			
			function enviar(fila)
			{
				var idPago = "idPago"+fila;
				
				if(document.getElementById(idPago).value!=''){
					var auxPers = 'oculto' + fila + '_3';
				    var idPersona = document.getElementById(auxPers).value;
					
					
					
					var idInst = "oculto"+fila+"_2";
					idPago = document.getElementById(idPago).value;
					idInstitucion =  document.getElementById(idInst).value;
					
					datos = "idPersona=="+idPersona + "##idPago==" +idPago + "##idInstitucion==" +document.InformesGenericosForm.idInstitucion.value + "##idTipoInforme==CPAGO%%%";
					
					document.InformesGenericosForm.datosInforme.value =datos;
					document.InformesGenericosForm.idTipoInforme.value = 'CPAGO';
					
					
					
				}else{
					// EJEMPLO: var dat="idAbono==25##idinstitucion==2040%%%idAbono==26##idinstitucion==2040%%%idAbono==27##idinstitucion==2040%%%idAbono==28##idinstitucion==2040";
					
				
				
					var auxPers = 'oculto' + fila + '_3';
				    var idPersona = document.getElementById(auxPers).value;
					var idAbono = "oculto" + fila + "_1";
					datos = "idPersona=="+idPersona +"##idAbono=="+ document.getElementById(idAbono).value+ "##idInstitucion=="+ document.InformesGenericosForm.idInstitucion.value + "##idTipoInforme==ABONO%%%";
					
					document.InformesGenericosForm.datosInforme.value =datos;
					document.InformesGenericosForm.idTipoInforme.value = 'ABONO';
				}
//				if(document.getElementById("informeUnico").value=='1'){
	//				document.InformesGenericosForm.submit();
		//		}else{
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
				// }

			}
			
		</script>

	</head>

	<body class="tablaCentralCampos" onload="cargarChecks();checkTodos()">
		<html:form action="/FAC_GenerarAbonos.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
			<html:hidden property="modo" value=""/>				
			<input type="hidden" name="actionModal"  name="actionModal" value="">
			<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
			<html:hidden property="datosPaginador"  styleId="datosPaginador" />
			<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		</html:form>
		
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,
			   			  facturacion.busquedaAbonos.literal.numAbono,facturacion.busquedaAbonos.literal.fecha,
			   			  facturacion.busquedaAbonos.literal.cliente,gratuita.modalRegistro_DefinirCalendarioGuardia.literal.observaciones,facturacion.busquedaAbonos.literal.estado,
			   			  facturacion.busquedaAbonos.literal.totalAbono,facturacion.busquedaAbonos.literal.numFacturaAsociada,"
			   columnSizes="4,10,8,15,22,12,7,9,14">		       
			<%
	    	if (resultado == null || resultado.size() < 1 )
		    {
			%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
			<%
	    	}	    
		    else
		    { %>
	    		<%for (int i = 0; i < resultado.size(); i++) { 
							
					Row row = (Row)resultado.elementAt(i);
			        
	            	String total=row.getString("TOTAL");
	            	Boolean isPermitidoBorrar =(Boolean) row.getRow().get("ESPERMITIDOBORRAR");
	            	// BNS NO SE PERMITE BORRAR ABONOS SJCS
	            	if (isPermitidoBorrar.booleanValue()){
		            	String IDPAGOSJG = (String) row.getRow().get("IDPAGOSJG");
		            	if (IDPAGOSJG != null && !IDPAGOSJG.equals("")){
		            		isPermitidoBorrar = false;
		            	}
	            	}
					total=new Double(UtilidadesNumero.redondea(new Double(row.getString("TOTAL")).doubleValue(),2)).toString();
	            	botonesFila="C";
	            	// Modificada MAV 12/7/05 a instancias JG para no permitir editable cuando el abono esté contabilizado y pagado
	            	// if (row.getString(FacAbonoBean.C_CONTABILIZADA).equalsIgnoreCase(ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA)){
	            	Object estado=cpl.obtenerEstadoFacAbo(new Integer(row.getString(FacAbonoBean.C_IDINSTITUCION)).intValue(),new Long(row.getString(FacAbonoBean.C_IDABONO)).longValue(),ConsPLFacturacion.ABONO);
	            	if (!(row.getString(FacAbonoBean.C_CONTABILIZADA).equalsIgnoreCase(ClsConstants.FACTURA_ABONO_CONTABILIZADA)&&
	            	      estado.toString().equalsIgnoreCase("Pagado"))){
		            	botonesFila+=",E";
	            	}
					//if (row.getString(FacAbonoBean.C_IDABONO).equalsIgnoreCase(maximo)){
					if (isPermitidoBorrar.booleanValue()){
		            	botonesFila+=",B";
		            	
	            	}
	            	

					// boton de envios
					FilaExtElement[] elems = new FilaExtElement[1];
					elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
	            	
	            	%>
					<siga:FilaConIconos
						  fila='<%=""+(i+1)%>'
						  botones="<%=botonesFila%>"
						  modo="edicion"
 						  elementos="<%=elems%>" 
						  pintarEspacio="no" 							  					  							  
						  clase="listaNonEdit">
						 
						 
						<td align="center">
						<%String valorCheck = row.getString(FacAbonoBean.C_IDABONO);
						boolean isChecked = false;
						for (int z = 0; z < registrosSeleccionados.size(); z++) {
							Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
							String clave = (String)clavesRegistro.get("CLAVE");
							if (valorCheck.equals(clave)) {
								isChecked = true;
								break;
							}
						}
						
						if (isChecked) {%>
								<input type="checkbox" value="<%=valorCheck%>"  name="sel" checked onclick="pulsarCheck(this)">
						<%} else {%>
								<input type="checkbox" value="<%=valorCheck%>"   name="sel" onclick="pulsarCheck(this)" >
						<%}%>
						</td>
						<td>
							<input type="hidden" name="oculto<%=(i+1)%>_1" value="<%=row.getString(FacAbonoBean.C_IDABONO)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_2" value="<%=row.getString(FacAbonoBean.C_IDINSTITUCION)%>">

							<!-- ENVIOS 1 idAbono, 3 idPersona, 4 descripcion -->
							<input type="hidden" name="oculto<%=(i+1)%>_3" value="<%=row.getString(FacAbonoBean.C_IDPERSONA)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_4" value="<%=row.getString(FacAbonoBean.C_IDFACTURA)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_5" value=" ">
							<input type="hidden" name="oculto<%=(i+1)%>_6" value="<%=row.getString(FacAbonoBean.C_IDPAGOSJG)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_7" value="<%=row.getString(FacAbonoBean.C_IDPERSONA)%>">
							<input type="hidden" name="idPago<%=(i+1)%>" id="idPago<%=(i+1)%>"	value="<%=row.getString(FacAbonoBean.C_IDPAGOSJG)%>"> 
							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_NUMEROABONO))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(FacAbonoBean.C_FECHA)))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(personaAdm.obtenerNombreApellidos(row.getString(FacAbonoBean.C_IDPERSONA)))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_OBSERVACIONES))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(estado)%>
  						</td>
						<td align="right">
							<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(total))%>&nbsp;&euro;
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacFacturaBean.C_NUMEROFACTURA))%>
						</td>
					</siga:FilaConIconos>
					<% 
				} 
			} %>
			</siga:Table>
			
			<!-- Metemos la paginación-->		    
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
    	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>


	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "tablaDatosDinamicosD" value = ""/>
		<html:hidden property = "filaSelD" value = ""/>		
		<html:hidden property = "idSolicitud" value = ""/>
		<html:hidden property = "subModo" value = "abono"/>
		<html:hidden property = "idPersona" value = ""/>
		<html:hidden property = "descEnvio" value = ""/>
		
	</html:form>
	<html:form action="/INF_InformesGenericos" method="post"  target="submitArea">
		<html:hidden property="idInstitucion" styleId="idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property="idTipoInforme"  styleId="idTipoInforme"/>
		<html:hidden property="enviar"   styleId="enviar" value="1"/>
		<html:hidden property="descargar"  styleId="descargar" value="1"/>
		<html:hidden property="datosInforme"  styleId="datosInforme"/>
		<html:hidden property="modo"  styleId="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal' id='actionModal'>
	</html:form>
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
   		 	
	   		Hashtable clavesAb= (Hashtable) registrosSeleccionados.get(p);
	   		
	   		
			valorCheckAbono=(String)clavesAb.get("CLAVE");
			
					
			%>
				var aux='<%=valorCheckAbono%>';
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