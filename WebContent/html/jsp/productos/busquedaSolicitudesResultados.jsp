<!-- busquedaSolicitudesResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.util.Vector"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>
<%@page import="com.atos.utils.Row"%>
<%@page import="java.util.Hashtable"%>

<%
	String app = request.getContextPath(); 
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	/** PAGINADOR ***/
	String idioma=usr.getLanguage().toUpperCase();
	
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	String idPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(idPaginador)!=null){
		hm = (HashMap)ses.getAttribute(idPaginador);
	
	
	
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
	String action=app+"/PYS_GestionarSolicitudes.do?noReset=true";

	request.getSession().setAttribute("EnvEdicionEnvio","GPS");

%>


<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
</head>

<script>
	<!-- Refrescar -->
	function refrescarLocal(){ 		
			parent.buscar();
	}	

	function enviar(fila)
	{
	   	
	   	var auxSol = 'oculto' + fila + '_1';
	    var idSolic = document.getElementById(auxSol);			          		
	   				   	
	   	var auxPers = 'oculto' + fila + '_4';
	    var idPers = document.getElementById(auxPers);	
	    	    
	    var auxDesc = 'oculto' + fila + '_5';
	    var desc = document.getElementById(auxDesc);			    
	    
	    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
	   	document.DefinirEnviosForm.idPersona.value=idPers.value;
	   	document.DefinirEnviosForm.descEnvio.value="Solicitud "+desc.value;
	   	
	   	document.DefinirEnviosForm.modo.value='envioModal';		   	
	   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
		
	   	if (resultado==undefined||resultado[0]==undefined ||resultado[0]=="M"){			   		
	   	} else {
	   		
	   		var idEnvio = resultado[0];
		    var idTipoEnvio = resultado[1];
		    var nombreEnvio = resultado[2];				    
		    
		   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
		   	document.DefinirEnviosForm.modo.value='editar';
		   	document.DefinirEnviosForm.submit();
	   	}
	}
 function editarConCertificado(fila) {
   var datos;
   datos = document.getElementById('tablaDatosDinamicosD');
   datos.value = ""; 
   var i, j;
   for (i = 0; i < 5; i++) {
      var tabla;
      tabla = document.getElementById('tablaResultados');
      if (i == 0) {
        var flag = true;
        j = 1;
        while (flag) {
          var aux = 'oculto' + fila + '_' + j;
          var oculto = document.getElementById(aux);
          if (oculto == null)  { flag = false; }
          else { datos.value = datos.value + oculto.value + ','; }
          j++;
        }
        datos.value = datos.value + "%"
      } else { j = 2; }
      if ((tabla.rows[fila].cells)[i].innerHTML == "") {
        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
      } else {
        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
      }
   }
   document.forms[0].modo.value = "Editar";
   
   var resultado = ventaModalGeneral(document.forms[0].name,"G");

   if (resultado) {
  	 	if (resultado[0] && resultado[0]!="@") {
  	 		
   		refrescarLocalArray(resultado);
   } else 
    	if (resultado=="MODIFICADO")
   	    {
      		refrescarLocal();
   	    }else{
		  var listaValores = resultado.split("#@#");//nombreCertificado #@# idproducto #@# idptoductoInstitucion #@# idTipoProducto
			  document.forms[0].concepto.value=listaValores[1];
			  document.forms[0].idProducto.value=listaValores[2];
			  document.forms[0].idProductoInstitucion.value=listaValores[3];
			  document.forms[0].idTipoProducto.value=listaValores[4];
		
		
		 var aux = 'oculto' + fila + '_1'; 
	     var oculto = document.getElementById(aux);
		 document.forms[0].idSolicitud.value=oculto.value;
		 document.forms[0].action="<%=app %>/CER_GestionSolicitudes.do?buscar=true";
	     document.forms[0].target = "mainWorkArea";
	     document.forms[0].modo.value="abrir";
	     document.forms[0].submit();
	}
   }
 }

	function facturacionrapida(fila)
	{
		subicono('iconoboton_facturacionrapida'+fila);
		
		var auxPet = 'oculto' + fila + '_1';
	    var idPet = document.getElementById(auxPet);			          		
		
		document.solicitudCompraForm.idPeticionParametro.value=idPet.value;		

		document.solicitudCompraForm.target="submitArea";
		document.solicitudCompraForm.modo.value="facturacionRapidaCompra";

	   	document.solicitudCompraForm.submit();		
	   	
	   	finsubicono('iconoboton_facturacionrapida'+fila);
	   	window.setTimeout("fin()",5000,"Javascript");
	}

</script>

<body>
		<html:form action="/PYS_GestionarSolicitudes.do" method="POST">
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" name="concepto"  id="concepto" value=""> 
     		<input type="hidden" name="idSolicitud"  id="idSolicitud" value="">
			<input type="hidden" name="idProducto"  id="idProducto" value="">
			<input type="hidden" name="idProductoInstitucion"  id="idProductoInstitucion" value="">
			<input type="hidden" name="idTipoProducto"  id="idTipoProducto" value="">
			<input type="hidden" name="actionModal"  id="actionModal" value="">
		</html:form>	
		
			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "1"
			   clase  = "tableTitle"
			   nombreCol="pys.gestionSolicitudes.literal.fecha,pys.gestionSolicitudes.literal.idPeticion,
			              pys.gestionSolicitudes.literal.tipo,pys.gestionSolicitudes.literal.cliente,
			              pys.gestionSolicitudes.literal.estadoPeticion,"
			   tamanoCol = "12,15,19,23,19,12"
			   alto  = "250"
			ajustePaginador="true"   
			   activarFilaSel="true" 
			    
		   
			   >

		<% if (resultado == null || resultado.size()==0){%>
	 		<br>
	   			 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
		<% } else { 	    	
					

				for (int i = 1; i <= resultado.size(); i++)
				{	
					Row fila = (Row)resultado.get(i-1);
					
					Hashtable peticion = (Hashtable) fila.getRow();

				
							 
							 if (peticion != null){ 

									Long idPeticion = UtilidadesHash.getLong (peticion, PysPeticionCompraSuscripcionBean.C_IDPETICION);
									Integer idInstitucion = UtilidadesHash.getInteger (peticion, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
									Long idPersona = UtilidadesHash.getLong (peticion, CenPersonaBean.C_IDPERSONA);
 
									String nombreCliente = "";
									nombreCliente = UtilidadesHash.getString (peticion, CenPersonaBean.C_NOMBRE);
									nombreCliente = nombreCliente + " " + UtilidadesHash.getString (peticion, CenPersonaBean.C_APELLIDOS1);
									nombreCliente = nombreCliente + " " + UtilidadesHash.getString (peticion, CenPersonaBean.C_APELLIDOS2);
									String fecha   = UtilidadesHash.getString (peticion, PysPeticionCompraSuscripcionBean.C_FECHA);
									int numSolicitudes   = new Integer(UtilidadesHash.getString (peticion, "NUM_PENDIENTE")).intValue();
									int numCertificados  = new Integer(UtilidadesHash.getString (peticion, "NUM_CERTIFICADOS")).intValue();
									int hayServicios     = new Integer(UtilidadesHash.getString (peticion, "HAY_SERVICIOS")).intValue();
							 		String tipoSol = UtilidadesHash.getString (peticion, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
							 		String tipoSolTexto ="";
							 		String estadoSol = UtilidadesHash.getString (peticion, "DESCRIPCION_ESTADO");
							 		String idEstadoSol = UtilidadesHash.getString (peticion, "IDESTADOPETICION");

							 		if ((fecha == null)         || fecha.equals("")) {       fecha         = "&nbsp;"; }
							 		else {
							 		fecha = GstDate.getFormatedDateShort("", fecha);
							 		}
							 		
							 		if ((nombreCliente == null) || nombreCliente.equals("")) nombreCliente = "&nbsp;";
							 		if ((estadoSol == null)     || estadoSol.equals(""))     estadoSol     = "&nbsp;";
							 		if ((tipoSol == null)       || tipoSol.equals("")) {     tipoSol       = "&nbsp;";	}
									else {
								 		if (tipoSol.equalsIgnoreCase(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {
								 			tipoSolTexto = "pys.tipoPeticion.alta";
								 		}
								 		else {
								 			tipoSolTexto = "pys.tipoPeticion.baja";
								 		}
									}
				
									// boton de envios
									FilaExtElement[] elems = new FilaExtElement[2];
									if ((idEstadoSol.trim().equals("10") || idEstadoSol.trim().equals("20")) && tipoSol.trim().equals("A") && numSolicitudes>0 && numCertificados==0) {
										elems = new FilaExtElement[3];
									}
									
									elems[0]=new FilaExtElement("editarConCertificado", "editarConCertificado", SIGAConstants.ACCESS_FULL);
									elems[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
									
									if ((idEstadoSol.trim().equals("10") || idEstadoSol.trim().equals("20")) && tipoSol.trim().equals("A") && numSolicitudes>0  && numCertificados==0 && hayServicios==0) {
										elems[2]=new FilaExtElement("facturacionrapida", "facturacionrapida", SIGAConstants.ACCESS_READ);
									}
				
   	 		%>
							 		
									<siga:FilaConIconos fila='<%=""+i%>' 
											botones="" 
											visibleConsulta="false" 
											visibleBorrado="false" 
											visibleEdicion="false"
											pintarEspacio="false"
											elementos="<%=elems%>" 
											pintarEspacio="no" 							  					  							  
											clase="listaNonEdit"> 
											
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idPeticion%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idInstitucion%>">
											<input type="hidden" id="oculto<%=i%>_3" value="<%=tipoSol%>">
											<input type="hidden" id="oculto<%=i%>_4" value="<%=idPersona%>">
											 
											
											<!-- ENVIOS 1 idSolicitud, 4 idPersona, 5 descripcion -->
											<input type="hidden" name="oculto<%=""+(i)%>_5" value="<%=UtilidadesString.getMensajeIdioma(usr.getLanguage(),tipoSolTexto)%>">
											
											<%=UtilidadesString.mostrarDatoJSP(fecha)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
									<td><siga:Idioma key="<%=tipoSolTexto%>"/></td> 
									<td><%=UtilidadesString.mostrarDatoJSP(nombreCliente)%></td> 
									<td><%=UtilidadesString.mostrarDatoJSP(estadoSol)%></td> 
									</siga:FilaConIconos>
							 		
			<%	 		       } // if
				 	 }  // for  
				  }//else%>
	

	

			</siga:TablaCabecerasFijas>
			<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	


		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
			<html:hidden styleId = "actionModal"  property = "actionModal" value = ""/>
			<html:hidden styleId = "modo"  property = "modo" value = ""/>
			<html:hidden styleId = "idSolicitud"  property = "idSolicitud" value = ""/>
			<html:hidden styleId = "idPersona"  property = "idPersona" value = ""/>
			<html:hidden styleId = "descEnvio"  property = "descEnvio" value = ""/>
			
		</html:form>


		<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="_blank" type=""> 
			<input type="hidden" name="modo" value=""> 
			<input type="hidden" name="actionModal" value=""> 
			<html:hidden property = "idPeticionParametro" value = ""/>
		</html:form>

</body>