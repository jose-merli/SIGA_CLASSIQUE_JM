<!-- buscarListadoSolicitudes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>

<%
	Boolean isPermitirFacturaCertificado = (Boolean)request.getAttribute("isPermitirFacturaCertificado");
	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();
	
	String sCGAE = (String)request.getAttribute("esCGAE"); 
	boolean esCGAE = sCGAE.equalsIgnoreCase("true");
	
	request.getSession().setAttribute("CenBusquedaClientesTipo","GS");
	
	//parámetro para la vuelta al listado de solicitudes desde la edición de envío
	request.getSession().setAttribute("EnvEdicionEnvio","GS");
	
	
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute("DATAPAGINADOR")!=null){
	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	
	
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
		String action=app+"/CER_GestionSolicitudes.do?noReset=true";
    /**************/
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}

			function anular(fila){
			 var datos;
				
				subicono('iconoboton_anular'+fila);
			   	datos = document.getElementById('tablaDatosDinamicosD');
			    datos.value = ""; 
			   	var i, j;
			   	
			   	for (i = 0; i < 7; i++)
			   	{
					var tabla;
			      	tabla = document.getElementById('tablaDatos');
			      	
			      	if (i == 0)
			      	{
			        	var flag = true;
			        	j = 1;
			        	
			        	while (flag)
			        	{
			          		var aux = 'oculto' + fila + '_' + j;
			          		var oculto = document.getElementById(aux);
			          		
			          		if (oculto == null)
			          		{
			          			flag = false;
			          		}
			          		
			          		else
			          		{
			          			datos.value = datos.value + oculto.value + ',';
			          		}
			          		
			          		j++;
						}
			        	
			        	datos.value = datos.value + "%"
			      	}
			      	
			      	else
			      	{
			      		j = 2;
			      	}
			      	
			      	if ((tabla.rows[fila].cells)[i].innerText == "")
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			        }
			      	
			      	else
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
			        }
			   	}
			   	
			   	var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "anular";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;
			   	finsubicono('iconoboton_anular'+fila);
			}
			function generar(fila)
			{
				var datos;
				subicono('iconoboton_generar'+fila);
			   	datos = document.getElementById('tablaDatosDinamicosD');
			    datos.value = ""; 
			   	var i, j;
			   	
			   	for (i = 0; i < 1; i++)
			   	{
					var tabla;
			      	tabla = document.getElementById('tablaDatos');
			      	
			      	if (i == 0)
			      	{
			        	var flag = true;
			        	j = 1;
			        	
			        	while (flag)
			        	{
			          		var aux = 'oculto' + fila + '_' + j;
			          		var oculto = document.getElementById(aux);
			          		
			          		if (oculto == null)
			          		{
			          			flag = false;
			          		}
			          		
			          		else
			          		{
			          			datos.value = datos.value + oculto.value + ',';
			          		}
			          		
			          		j++;
						}
			        	
			        	datos.value = datos.value + "%"
			      	}
			      	
			      	else
			      	{
			      		j = 2;
			      	}
			      	
			      	if ((tabla.rows[fila].cells)[i].innerText == "")
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			        }
			      	
			      	else
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
			        }
			   	}

				var oCheck = document.getElementsByName("chkPDF");
				SolicitudesCertificadosForm.idsTemp.value=oCheck[fila-1].value;
				// PDM Ahora se hace una comprobacion previa, si solo existe una plantilla no se muestra la ventana modal y se genera
				// directamente el certificado, si hay mas de una se muestra la ventana modal para elegir una plantilla.
				SolicitudesCertificadosForm.modo.value="comprobarNumPlantillas";
				SolicitudesCertificadosForm.target="submitArea";
				SolicitudesCertificadosForm.submit();
				finsubicono('iconoboton_generar'+fila);
				
			}

			function enviar(fila)
			{	
				
				document.forms[2].filaSelD.value = fila;
			
			   	var auxSol = 'oculto' + fila + '_2';
			    var idSolic = document.getElementById(auxSol);			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_4';
			    var idPers = document.getElementById(auxPers);	
			    	    
			    var auxDesc = 'oculto' + fila + '_5';
			    var desc = document.getElementById(auxDesc);			    
			    
			    document.forms[2].idSolicitud.value=idSolic.value;
			   	document.forms[2].idPersona.value=idPers.value;
			   	document.forms[2].descEnvio.value=desc.value;
			   	
			   	document.forms[2].modo.value='envioModal';		   	
			   	document.forms[2].subModo.value='SolicitudCertificado';		   	
			   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
			   	if (resultado==undefined||resultado[0]==undefined){			   		
			   		refrescarLocal();
			   	} else {
			   		var idEnvio = resultado[0];
				    var idTipoEnvio = resultado[1];
				    var nombreEnvio = resultado[2];				    
				   	document.forms[2].tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.forms[2].modo.value='editar';
				   	document.forms[2].submit();
			   	}
			}

			function denegar(fila)
			{
				var datos;
				subicono('iconoboton_denegar'+fila);

			   	datos = document.getElementById('tablaDatosDinamicosD');
			    datos.value = ""; 
			   	var i, j;
			   	
			   	for (i = 0; i < 7; i++)
			   	{
					var tabla;
			      	tabla = document.getElementById('tablaDatos');
			      	
			      	if (i == 0)
			      	{
			        	var flag = true;
			        	j = 1;
			        	
			        	while (flag)
			        	{
			          		var aux = 'oculto' + fila + '_' + j;
			          		var oculto = document.getElementById(aux);
			          		
			          		if (oculto == null)
			          		{
			          			flag = false;
			          		}
			          		
			          		else
			          		{
			          			datos.value = datos.value + oculto.value + ',';
			          		}
			          		
			          		j++;
						}
			        	
			        	datos.value = datos.value + "%"
			      	}
			      	
			      	else
			      	{
			      		j = 2;
			      	}
			      	
			      	if ((tabla.rows[fila].cells)[i].innerText == "")
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			        }
			      	
			      	else
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
			        }
			   	}
			   	
			   	var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "denegar";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;
			   	finsubicono('iconoboton_denegar'+fila);
			}

			function finalizar(fila)
			{
				var datos;
				subicono('iconoboton_finalizar'+fila);
			   	datos = document.getElementById('tablaDatosDinamicosD');
			    datos.value = ""; 
			   	var i, j;
			   	
			   	for (i = 0; i < 7; i++)
			   	{
					var tabla;
			      	tabla = document.getElementById('tablaDatos');
			      	
			      	if (i == 0)
			      	{
			        	var flag = true;
			        	j = 1;
			        	
			        	while (flag)
			        	{
			          		var aux = 'oculto' + fila + '_' + j;
			          		var oculto = document.getElementById(aux);
			          		
			          		if (oculto == null)
			          		{
			          			flag = false;
			          		}
			          		
			          		else
			          		{
			          			datos.value = datos.value + oculto.value + ',';
			          		}
			          		
			          		j++;
						}
			        	
			        	datos.value = datos.value + "%"
			      	}
			      	
			      	else
			      	{
			      		j = 2;
			      	}
			      	
			      	if ((tabla.rows[fila].cells)[i].innerText == "")
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			        }
			      	
			      	else
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
			        }
			   	}
			   	
			   	var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "finalizar";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;
			   	finsubicono('iconoboton_finalizar'+fila);
			}

			function download(fila)
			{
				var datos;
				
			   	datos = document.getElementById('tablaDatosDinamicosD');
			    datos.value = ""; 
			   	var i, j;
			   	
			   	for (i = 0; i < 7; i++)
			   	{
					var tabla;
			      	tabla = document.getElementById('tablaDatos');
			      	
			      	if (i == 0)
			      	{
			        	var flag = true;
			        	j = 1;
			        	
			        	while (flag)
			        	{
			          		var aux = 'oculto' + fila + '_' + j;
			          		var oculto = document.getElementById(aux);
			          		
			          		if (oculto == null)
			          		{
			          			flag = false;
			          		}
			          		
			          		else
			          		{
			          			datos.value = datos.value + oculto.value + ',';
			          		}
			          		
			          		j++;
						}
			        	
			        	datos.value = datos.value + "%"
			      	}
			      	
			      	else
			      	{
			      		j = 2;
			      	}
			      	
			      	if ((tabla.rows[fila].cells)[i].innerText == "")
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			        }
			      	
			      	else
			      	{
			        	datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
			        }
			   	}
			   	
			   	//var auxTarget = document.forms[0].target;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "descargar";
			   	document.forms[0].submit();
			   	//document.forms[0].target=auxTarget;
			}
			
			
			function informacionLetrado(fila)
			{
				document.forms[1].filaSelD.value = fila;
				
				var auxIns = 'oculto' + fila + '_1';
			    var idInst = document.getElementById(auxIns);			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_4';
			    var idPers = document.getElementById(auxPers);			    

			   	var auxLetrado = 'oculto' + fila + '_11';
			    var idLetrado = document.getElementById(auxLetrado);			    
				
				if (idLetrado.value=='1') {				    
					document.forms[1].tablaDatosDinamicosD.value=idPers.value + ',' + idInst.value + ',LETRADO' + '%';		
				 } else {
					document.forms[1].tablaDatosDinamicosD.value=idPers.value + ',' + idInst.value + '%';		
				 }

			   	document.forms[1].submit();			   	
			}
			
			function facturacionrapida(fila)
			{
				subicono('iconoboton_facturacionrapida'+fila);
				var auxIns = 'oculto' + fila + '_1';
			    var idInst = document.getElementById(auxIns);			          		
			   				   	
			   	var auxSol = 'oculto' + fila + '_2';
			    var idSol = document.getElementById(auxSol);			    
				document.forms[0].target="submitArea";
				document.forms[0].tablaDatosDinamicosD.value=idInst.value + ',' + idSol.value + '%';		
				document.forms[0].modo.value="facturacionRapida";

			   	document.forms[0].submit();		
			   	finsubicono('iconoboton_facturacionrapida'+fila);
			   	window.setTimeout("fin()",5000,"Javascript");
			   	   	
 			}
			
			
           function versolicitud(fila){
		   var aux = 'oculto' + fila + '_6'; 
	       var oculto = document.getElementById(aux);
		   document.forms[0].idPeticion.value=oculto.value;
		       aux='oculto' + fila + '_7';
	      oculto=document.getElementById(aux);
	      document.forms[0].idProducto.value=oculto.value;
		  
	       aux='oculto' + fila + '_8';
	      oculto=document.getElementById(aux);
	      document.forms[0].idTipoProducto.value=oculto.value;
		  
		  aux='oculto' + fila + '_9';
	      oculto=document.getElementById(aux);
	      document.forms[0].idProductoInstitucion.value=oculto.value;
		  
		      document.forms[0].action="<%=app %>/PYS_GestionarSolicitudes.do?buscar=true";
	          document.forms[0].target = "mainWorkArea";
	          document.forms[0].modo.value="";
	          document.forms[0].submit();
		   }
		</script>
	</head>

	<body class="tablaCentralCampos">
	
	
		<html:form action="/CER_GestionSolicitudes.do?noReset=true" method="POST" target="resultado">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>

			<input type="hidden" name="idsParaGenerarFicherosPDF" value="">
			<input type="hidden" name="idsTemp" value="">
			<input type="hidden" name="validado" value="0">
			<input type="hidden" name="idPeticion" value="">
			<input type="hidden" name="idProducto" value="">
			<input type="hidden" name="idTipoProducto" value="">
			<input type="hidden" name="idProductoInstitucion" value="">

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

		<!-- Formulario para la búsqueda de clientes -->
		<html:form action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = "ver"/>
			<html:hidden property = "filaSelD" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>		
		</html:form>
		
		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "subModo" value = ""/>
			<html:hidden property = "filaSelD" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			
			<html:hidden property = "idSolicitud" value = ""/>
			<html:hidden property = "idPersona" value = ""/>
			<html:hidden property = "descEnvio" value = ""/>
		</html:form>
			<siga:TablaCabecerasFijas
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="&nbsp;,
		   		  			certificados.solicitudes.literal.idsolicitud,
		   		  			certificados.solicitudes.literal.estadosolicitud,
		   		  			certificados.solicitudes.literal.apellidosynombre,
		   		  			certificados.mantenimiento.literal.certificado,
		   		  			certificados.solicitudes.literal.institucionOrigenLista,
		   		  			certificados.solicitudes.literal.institucionDestino,
		   		  			certificados.solicitudes.literal.estadocertificado,
		   		  			certificados.solicitudes.literal.fechaEmision,"
		   		   tamanoCol="3,7,8,11,10,10,10,7,7,27"
		   		  alto="100%"
		   		  modal="G"
	   		  	  activarFilaSel="true" 
				  ajustePaginador="true">
<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}

				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
			   			String botones = "C,E,B";
				  		//Row htDatos = (Row)vDatos.elementAt(i);
						
						Row fila = (Row)resultado.elementAt(i);
			            

				  		// RGG obtengo los datos en la jsp
				  		Hashtable datosFactura = CerSolicitudCertificadosAdm.getDatosFacturaAsociada(userBean.getLocation(),fila.getString("PPN_IDTIPOPRODUCTO"),fila.getString("PPN_IDPRODUCTO"),fila.getString("PPN_IDPRODUCTOINSTITUCION"),fila.getString("IDPETICION"));
				  		String extIdFacturaCompra = (datosFactura.get("IDFACTURACOMPRA")!=null)?(String)datosFactura.get("IDFACTURACOMPRA"):null;
				  		String extNumeroFactura = (datosFactura.get("NUMEROFACTURA")!=null)?(String)datosFactura.get("NUMEROFACTURA"):null;
				  		String extFechaConfirmacion = (datosFactura.get("FECHA_CONFIRMACION")!=null)?(String)datosFactura.get("FECHA_CONFIRMACION"):null;
				  		String extEstadoPDF = (datosFactura.get("ESTADO_PDF")!=null)?(String)datosFactura.get("ESTADO_PDF"):null;
				  		
				  		
				  		String idEstadoSolicitud = fila.getString(CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
				  		
				  		boolean cobrado = (fila.getString(CerSolicitudCertificadosBean.C_FECHACOBRO)==null || fila.getString(CerSolicitudCertificadosBean.C_FECHACOBRO).equals(""))?false:true;
				  		String idInstitucionSol = fila.getString("IDINSTITUCION_SOL");
				  		boolean isSolicitudColegio = idInstitucionSol!=null && !idInstitucionSol.equals(CerSolicitudCertificadosAdm.IDCGAE) && !idInstitucionSol.substring(0,2).equals("30");
				  					  		
						String letrado=CenClienteAdm.getEsLetrado(fila.getString("IDPERSONA"),userBean.getLocation());
						
						
						
						String tipoCertificado2=" ";
						if (fila.getString("TIPOCERTIFICADO2")!=null && !fila.getString("TIPOCERTIFICADO2").equals("")){
						  tipoCertificado2 = fila.getString("TIPOCERTIFICADO2");
						}
				  		
				  		String idEstadoCertificado = fila.getString(CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO);
				  		boolean esCliente = CenClienteAdm.getEsCliente(fila.getString("IDPERSONA"),userBean.getLocation()).equalsIgnoreCase("S")?true:false;
						String idPeticionAux=" ";
						
						FilaExtElement[] elems = new FilaExtElement[8];
						if (fila.getString("IDPETICION")!=null && !fila.getString("IDPETICION").equals("")){
						 elems[6]=new FilaExtElement("versolicitud", "versolicitud", SIGAConstants.ACCESS_READ);
						 idPeticionAux=fila.getString("IDPETICION");
						}
						
						// RGG 25/11/2007 boton de facturacion rapida
						
						
						if(isSolicitudColegio){
							if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)
									&& fila.getString("TIPOCERTIFICADO2").equals("C")
									&& (extIdFacturaCompra == null               || 
		                                extIdFacturaCompra.trim().equals("null") || 
		                                extIdFacturaCompra.trim().equals("")     || 
		                                
		                                extFechaConfirmacion == null               || 
		                                extFechaConfirmacion.trim().equals("null") || 
		                                extFechaConfirmacion.trim().equals("")     ||
		                                
		                                extEstadoPDF == null                       ||
		                               !extEstadoPDF.trim().equals(FacEstadoConfirmFactBean.PDF_FINALIZADA.toString())||
									    extEstadoPDF.trim().equals(FacEstadoConfirmFactBean.PDF_FINALIZADA.toString()))
									&& cobrado 
									&&(isPermitirFacturaCertificado!=null &&isPermitirFacturaCertificado.booleanValue()))
								{
									 elems[7]=new FilaExtElement("facturacionrapida", "facturacionrapida", SIGAConstants.ACCESS_READ);
								}
						}else{
							if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)
								&& fila.getString("TIPOCERTIFICADO2").equals("C")
								&& (extIdFacturaCompra == null               || 
	                                extIdFacturaCompra.trim().equals("null") || 
	                                extIdFacturaCompra.trim().equals("")     || 
	                                
	                                extFechaConfirmacion == null               || 
	                                extFechaConfirmacion.trim().equals("null") || 
	                                extFechaConfirmacion.trim().equals("")     ||
	                                
	                                extEstadoPDF == null                       ||
	                               !extEstadoPDF.trim().equals(FacEstadoConfirmFactBean.PDF_FINALIZADA.toString())||
								    extEstadoPDF.trim().equals(FacEstadoConfirmFactBean.PDF_FINALIZADA.toString()))
	
								&& userBean.getLocation().equals("2000")
								&& cobrado
								&&(isPermitirFacturaCertificado!=null &&isPermitirFacturaCertificado.booleanValue()))
							{
								 elems[7]=new FilaExtElement("facturacionrapida", "facturacionrapida", SIGAConstants.ACCESS_READ);
							}
						}
						
						
//(esCliente||esCGAE) esto es lo que habia
						if (fila.getString("TIPOCERTIFICADO")!=null && 
							!fila.getString("TIPOCERTIFICADO").trim().equals("") &&
							//!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) &&
						    !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) &&
						    !idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)
						    )
						{
							elems[0]=new FilaExtElement("generar", "generar", SIGAConstants.ACCESS_READ);
						}
						if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO))
						{
							if (extNumeroFactura == null || extNumeroFactura.trim().equals("")) {
								elems[1]=new FilaExtElement("anular", "anular", SIGAConstants.ACCESS_READ);
							}
						}
						if (!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
							(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) ||
							 idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)))
						{
						   if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)){
							elems[2]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);
						   }else{
						    elems[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);
						   } 	
						}

						if (fila.getString("TIPOCERTIFICADO")!=null && 
							!fila.getString("TIPOCERTIFICADO").trim().equals("") &&
							idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND))
						{
							elems[2]=new FilaExtElement("denegar", "denegar", SIGAConstants.ACCESS_READ);
						}

						if (fila.getString("TIPOCERTIFICADO")!=null && 
							!fila.getString("TIPOCERTIFICADO").trim().equals("") &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO) &&
							!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
							(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) ||
							 idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)) &&
						    (esCliente||esCGAE))
						{
							elems[3]=new FilaExtElement("finalizar", "finalizar", SIGAConstants.ACCESS_READ);
						}

						if (fila.getString("TIPOCERTIFICADO")!=null && 
							!fila.getString("TIPOCERTIFICADO").trim().equals("") &&
							(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) ||
							 idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)) &&
						    (esCliente||esCGAE))
						{
						   elems[4]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
						}

						if (esCliente) {
							// MAV 26/8/2005 Resolucion incidencias cabiar tooltip seleccionar por informacion letrado							
							//elems[5]=new FilaExtElement("seleccionar", "seleccionar", SIGAConstants.ACCESS_READ);
							elems[5]=new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
						}
						
						// RGG 24-06-2005 si es digilencia la institucion es la de la solicitid (institucionOrigen)
						// 				  si es comunicacion la institucion es la del userbean
						String insti = "";
						if (fila.getString("TIPOCERTIFICADO2")!=null && 
							fila.getString("TIPOCERTIFICADO2").trim().equals(PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION))
						{
							// comunicacion 
							insti = userBean.getLocation();
						} else
						if (fila.getString("TIPOCERTIFICADO2")!=null && 
							fila.getString("TIPOCERTIFICADO2").trim().equals(PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA))
						{
							// diligencia
							insti = fila.getString(CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
						} else
						{
							// RGG 04/07/2005 para el resto de casos
							// certificado
							insti = userBean.getLocation();
						}	
						
						if (extNumeroFactura != null && !extNumeroFactura.trim().equals("")) {
							botones = "E";
						}						  

%>

<script>

</script>

	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" elementos="<%=elems%>"  visibleConsulta="false" pintarEspacio="no" clase="listaNonEdit">
					<td>
						
						
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString(CerSolicitudCertificadosBean.C_IDINSTITUCION)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString(CerSolicitudCertificadosBean.C_IDSOLICITUD)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("FECHA")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString(CenPersonaBean.C_IDPERSONA)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=fila.getString("TIPOCERTIFICADO")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=idPeticionAux%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=fila.getString(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_9" value="<%=fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=idEstadoSolicitud %>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=letrado %>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_12" value="<%=tipoCertificado2%>">
						
<%
/* esta condicion ya no vale
					if (htDatos.getString("TIPOCERTIFICADO")!=null && 
						!htDatos.getString("TIPOCERTIFICADO").trim().equals("") &&
						!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) &&
						!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)
						)
					{
*/

					// RGG : CAMBIO PARA USAR LOS CHECK PARA ENVIOS. SUS PERMISOS VAN COMO EL BOTON DE ENVIOS.
					if (!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND) &&
						!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) &&
						!idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) &&
						(idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) ||
						 idEstadoCertificado.equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO)))
					{

%>
						<input type="checkbox" value="<%=fila.getString("FECHA") + "%%" + fila.getString(CerSolicitudCertificadosBean.C_IDSOLICITUD) + "%%" + fila.getString("TIPOCERTIFICADO") + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_IDINSTITUCION) + "%%" + fila.getString(CenPersonaBean.C_IDPERSONA) + "%%" + insti%>" name="chkPDF">
<%
					}
					else
					{
%>
						<input type="checkbox" value="<%=fila.getString("FECHA") + "%%" + fila.getString(CerSolicitudCertificadosBean.C_IDSOLICITUD) + "%%" + fila.getString("TIPOCERTIFICADO") + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION) + "%%" + fila.getString(CerSolicitudCertificadosBean.C_IDINSTITUCION) + "%%" + fila.getString(CenPersonaBean.C_IDPERSONA) + "%%" + insti%>" name="chkPDF" disabled>
<%
					}
%>
					</td>
					<td><%=fila.getString("IDSOLICITUD")%></td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(fila.getString("ESTADOSOLICITUD"),userBean)%>(<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(userBean.getLanguage(), fila.getString("FECHAESTADO")))%>)</td>
					<td><%=fila.getString("CLIENTE")%></td>
					<td><%=fila.getString("TIPOCERTIFICADO")%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("INSTITUCIONORIGEN"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("INSTITUCIONDESTINO"))%></td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(fila.getString("ESTADOCERTIFICADO"),userBean)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(userBean.getLanguage(), fila.getString("FECHAEMISION")))%></td>

				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>
			
	
	  
	  						
	<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscar"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	

		
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>