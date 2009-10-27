<!-- informeJustificacionMasivaLetradoResultados.jsp -->

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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=usr.getLanguage().toUpperCase();
	//Vector v = (Vector)request.getAttribute("resultado");
	
	
	String letrado = ((String)request.getAttribute("letrado")==null)?"":(String)request.getAttribute("letrado");
	String fecha   = ((String)request.getAttribute("fecha")  ==null)?"":(String)request.getAttribute("fecha");
	
	String modoPestana = (String)request.getAttribute("MODOPESTANA");
	
	
	boolean checkreadonly = false; // para el check
	if (modoPestana!=null && modoPestana.equals("ver")) {
		checkreadonly = true;
	} else {
	
		checkreadonly = false;
		
	}
	boolean isPermitidoGuardar = false;	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	String atributoPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador)!=null){
		hm = (HashMap)ses.getAttribute(atributoPaginador);
	
	
	
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
	String action=app+"/JGR_PestanaDesignas.do?noReset=true";
	
	
	
%>	

<%@page import="java.util.HashMap"%>
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	
</head>

<body onload="deshabilitarCabecera()">

		<html:form action="/JGR_PestanaDesignas.do" method="post" target="submitArea" >
			<html:hidden property="modo"    value=""/>
			
			<html:hidden property="letrado" value="<%=letrado%>"/>

			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			
			
			
			</html:form>
			
		
			
		
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.informeJustificacionMasiva.literal.orden,gratuita.informeJustificacionMasiva.literal.expedienteCausa,gratuita.informeJustificacionMasiva.literal.juzgado,gratuita.informeJustificacionMasiva.literal.fechaSalida,gratuita.informeJustificacionMasiva.literal.asunto,gratuita.informeJustificacionMasiva.literal.cliente,gratuita.informeJustificacionMasiva.literal.categoria,gratuita.informeJustificacionMasiva.literal.fechaInicio,gratuita.informeJustificacionMasiva.literal.finalizacion,gratuita.informeJustificacionMasiva.literal.baja"
		   tamanoCol="7,10,15,8,10,20,4,11,11,4" 
		   ajusteBotonera="true" 
		   ajustePaginador="true">
				
		<% if (resultado == null || resultado.size()==0){%>
	 		<br>
	   			 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
		<% } else { %>
		
			  <%
		    	
					

				for (int i = 0; i < resultado.size(); i++)
				{	
					Row fila = (Row)resultado.get(i);
					
					Hashtable hash = (Hashtable) fila.getRow();

					String clase = "";
					String idPersona = "";
					String numJustificaciones = "";
					String idInstitucion = " ", idTurno = " ", anio = " ", numero = " ", idJuzgado = " ",idInstJuzgado=" ", 
					       fDesigna=" ",fFinAct=" ", fIniAct=" ", estado=" ", cliente=" ",expedientes=" ", 
					       codigoDesigna=" ", asunto=" ", categoria=" ",idJurisdiccion="",idProcedimiento=" ",descJuzgado = " ", validarJustificaciones="",
					       fechaActuacionIni="", fechaActuacionFin="";;
					boolean puedeLetradoJustificar  = false;
					       

					ArrayList juzgadoSel = new ArrayList();
					
					try {
						anio = UtilidadesHash.getString(hash, "ANIO");
						numero = UtilidadesHash.getString(hash, "NUMERO");
						idTurno = UtilidadesHash.getString(hash, "IDTURNO");
						idInstitucion = UtilidadesHash.getString(hash, "IDINSTITUCION");
						idJuzgado = UtilidadesHash.getString(hash, "IDJUZGADO");
						descJuzgado = UtilidadesHash.getString(hash, "DESC_JUZGADO");
						idInstJuzgado = UtilidadesHash.getString(hash, "IDINSTITUCION_JUZG");
						fDesigna = UtilidadesHash.getString(hash, "FECHADESIGNA");
						fFinAct = UtilidadesHash.getString(hash, "ACREDITACION_FIN");
						fIniAct = UtilidadesHash.getString(hash, "ACREDITACION_INI");
						fechaActuacionIni = UtilidadesHash.getString(hash, "FECHAACTUACIONINI");
						fechaActuacionFin = UtilidadesHash.getString(hash, "FECHAACTUACIONFIN");
						estado = UtilidadesHash.getString(hash, "ESTADO");
						expedientes = UtilidadesHash.getString(hash, "EXPEDIENTES");
						cliente = UtilidadesHash.getString(hash, "CLIENTE");
						codigoDesigna = UtilidadesHash.getString(hash, "CODIGO");
						asunto = UtilidadesHash.getString(hash, "ASUNTO");
						categoria = UtilidadesHash.getString(hash, "CATEGORIA");
						idJurisdiccion = UtilidadesHash.getString(hash, "IDJURISDICCION");
						idProcedimiento = UtilidadesHash.getString(hash, "IDPROCEDIMIENTO");
						idPersona =  UtilidadesHash.getString(hash, "IDPERSONA");
						numJustificaciones = UtilidadesHash.getString(hash, "NUMJUSTIFICACIONES");
						validarJustificaciones = UtilidadesHash.getString(hash, "VALIDARJUSTIFICACIONES");
						String letradoActuaciones = UtilidadesHash.getString(hash, "LETRADOACTUACIONES");  
						puedeLetradoJustificar = letradoActuaciones!=null && letradoActuaciones.equalsIgnoreCase("1");

						juzgadoSel.add(0,idJuzgado + "," + idInstitucion);	
					}
					catch(Exception e) {}

					if ((i%2) == 0)
						clase = "filaTablaPar";
					else 
						clase = "filaTablaImpar";
						
			 	%>	

		  		<tr class="<%=clase%>">
					<td>
						<input type="hidden" name="modificado<%=i%>"   value="false">
						<input type="hidden" name="idInstitucion<%=i%>" value="<%=idInstitucion%>">
						<input type="hidden" name="idTurno<%=i%>"      value="<%=idTurno%>">
						<input type="hidden" name="anio<%=i%>"         value="<%=anio%>">
						<input type="hidden" name="numero<%=i%>"       value="<%=numero%>">
						<input type="hidden" name="idProcedimiento<%=i%>"  value="<%=idProcedimiento%>">
						<input type="hidden" name="idJuzgado<%=i%>"  value="<%=idJuzgado%>">
						<input type="hidden" name="idInstJuzgado<%=i%>"  value="<%=idInstJuzgado%>">
						<input type="hidden" name="fIniAct<%=i%>" value="<%=GstDate.getFormatedDateShort("",fIniAct)%>">
						<input type="hidden" name="idJurisdiccion<%=i%>"  value="<%=idJurisdiccion%>">
						<input type="hidden" name="idPersona<%=i%>"  value="<%=idPersona%>">
						<input type="hidden" name="codigoDesigna<%=i%>"  value="<%=codigoDesigna%>">
						<input type="hidden" name="numJustificaciones<%=i%>"  value="<%=numJustificaciones%>">
						<input type="hidden" name="validarJustificaciones<%=i%>"  value="<%=validarJustificaciones%>">
						
						<%=UtilidadesString.mostrarDatoJSP(codigoDesigna)%>
					</td>
					
					<td><%=UtilidadesString.mostrarDatoJSP(expedientes)%>
					</td>
					
					<td >
					  
					
						<%=UtilidadesString.mostrarDatoJSP(descJuzgado)%>
						
					</td>				
					
					<td align="center">
					
						<%=GstDate.getFormatedDateShort("",fDesigna)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(asunto)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(cliente)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(categoria)%>
					</td>
					
					<% if (idJuzgado == null ||  idJuzgado.equals("")) {%>
						<td colspan="2" align="center">
							<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.sinJuzgado"/>
						</td>
					<%	}else  if (idProcedimiento == null ||  idProcedimiento.equals("")) {%>
							<td colspan="2" align="center">
							<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.sinModulo"/>
						</td>
					 
					<%	}else {%> 
					<td align="center">
						<% if (fIniAct != null && !fIniAct.equals("")) { %> 
							&nbsp;<%=GstDate.getFormatedDateShort("",fIniAct)%>
						<% }  else { 
						     if (usr.isLetrado() && (validarJustificaciones!=null && validarJustificaciones.equals("S")) && (fechaActuacionIni!=null && !fechaActuacionIni.equals("")) ){%>
						    	<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.actPendVal"/>
						    	<input style="display:none" type="checkBox" name="checkFechaIni<%=i%>" value="checkFechaIni" onClick="accionCheckInicio('<%=i%>');"  <% if (estado != null && estado.equals("F")) out.print("disabled "); else{ if(!puedeLetradoJustificar && checkreadonly)out.print("disabled ");}%>> 
						    	 
						     <%}else{%>
						
						
						
							<input type="checkBox" name="checkFechaIni<%=i%>" value="checkFechaIni" onClick="accionCheckInicio('<%=i%>');"  <% if (estado != null && estado.equals("F")) out.print("disabled "); else{ if(!puedeLetradoJustificar && checkreadonly)out.print("disabled ");}%>>
							<% }}%>
					</td>

					<td align="center" >
						<% if (fFinAct != null && !fFinAct.equals("")) { %> 
							&nbsp;<%=GstDate.getFormatedDateShort("",fFinAct)%>
						<% }  else {
							if (usr.isLetrado() && (validarJustificaciones!=null && validarJustificaciones.equals("S")) && (fechaActuacionFin!=null && !fechaActuacionFin.equals("")) ){%>
					    	<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.actPendVal"/>
					    	<input style="display:none" type="checkBox" name="checkFechaFin<%=i%>" value="checkFechaFin" onClick="accionCheckFin('<%=i%>');"   <% if (estado != null && estado.equals("F")) out.print("disabled "); else{ if(!puedeLetradoJustificar && checkreadonly)out.print("disabled ");}%>> 
					     <% }else{%>
							<input type="checkBox" name="checkFechaFin<%=i%>" value="checkFechaFin" onClick="accionCheckFin('<%=i%>');"   <% if (estado != null && estado.equals("F")) out.print("disabled "); else{ if(!puedeLetradoJustificar && checkreadonly)out.print("disabled ");}%>>
						<%  }
						  }%>
					</td>
					<%}
					if(!isPermitidoGuardar){
						
						if((estado != null && !estado.equals("F"))&&(puedeLetradoJustificar||!checkreadonly))
							isPermitidoGuardar = true;
					}
					
					%>
					<td align="center">
						<input type="checkBox" name="baja<%=i%>" value="baja"  onclick="accionCheckBaja('<%=i%>');" <% if (estado != null && estado.equals("F")) out.print("checked disabled "); else{ if(!puedeLetradoJustificar && checkreadonly)out.print("disabled ");}%>>
					</td>
				</tr>
					
			<% 	} // for%>	
		<%}%>

	</siga:TablaCabecerasFijas>
	<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															
	
	 <%}%>	

	
	

	

<!-- FIN: LISTA DE VALORES -->
		

	<script language="JavaScript">


		function deshabilitarCabecera () 
		{
/*			<% if (resultado != null && resultado.size() > 0) { %>
				parent.document.InformeJustificacionMasivaForm.numeroNifTagBusquedaPersonas.disabled = true;
				parent.document.InformeJustificacionMasivaForm.nombrePersona.disabled = true;
				parent.document.InformeJustificacionMasivaForm.buscarCliente.onclick = "";
				parent.document.InformeJustificacionMasivaForm.limpiar.onclick = "";
				parent.document.InformeJustificacionMasivaForm.fecha.disabled = true;
				parent.document.getElementById("iconoCalendarioA").onclick = "";
			<% } %>
*/		}
	
		function refrescarLocal() 
		{
			parent.buscar();
		} 
		function accionCheckFin(fila){
			
	
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);
			
			objModificado.value = objCheckFechaFin.checked;
			if(objCheckFechaIni!=null){
				objCheckFechaIni.checked = objCheckFechaFin.checked;
				
			}
			if(objCheckFechaFin.checked==true)
					objBaja.checked = false;
			
			objBaja.disabled = objCheckFechaFin.checked;
		}
		function accionCheckInicio(fila){
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);

			objModificado.value =objCheckFechaIni.checked;

			
			if(objCheckFechaIni.checked==true)
					objBaja.checked = false;
			else
				objCheckFechaFin.checked=false;
			
			objBaja.disabled = objCheckFechaIni.checked || objCheckFechaFin.checked;

		}
		
		
		function accionCheckBaja(fila)
		{
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);
		
		
			objModificado.value = objBaja.checked;
			
			if(objCheckFechaIni!=null){
				if(objBaja.checked==true)
					objCheckFechaIni.checked = false;
				objCheckFechaIni.disabled = objBaja.checked;
					
				
			}
			if(objCheckFechaFin!=null){
				if(objBaja.checked==true)
						objCheckFechaFin.checked = false;
						objCheckFechaFin.disabled = objBaja.checked;
						
					
				
			}
		}
		
		function accionNuevo () 
		{
		
			filas = 0;
			<% if (resultado!=null) { %>
				filas = eval("<%=resultado.size()%>");
			<% } %>

			flag = false;			
			for (i = 0; i < filas && flag == false; i++) {
				modificado = document.getElementById ("modificado"+i).value;
				if (modificado == "false") 
					continue;
				else 
					flag = true;
			}
		
			if (!flag || confirm("<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.informeJustificacionMasiva.mensaje.nuevoImpreso")%>")) {
				document.InformeJustificacionMasivaForm.target = "mainWorkArea";
				document.InformeJustificacionMasivaForm.modo.value = "nuevo";
				document.InformeJustificacionMasivaForm.submit();
			}
		}

		function accionGuardar () 
		{

			sub();
			filas = 0;
			<% if (resultado!=null) { %>
				filas = eval("<%=resultado.size()%>");
			<% } %>
			
			datos = "";
						
			for (i = 0; i < filas; i++) {

				modificado = document.getElementById ("modificado"+i).value;
				objCheckFechaIni  = document.getElementById ("checkFechaIni"+i);
				objFechaIni  = document.getElementById ("fIniAct"+i);
				objCheckFechaFin  = document.getElementById ("checkFechaFin"+i);
				
				if (modificado == "false") 
					continue;
				
				idInstitucion = document.getElementById ("idInstitucion"+i).value;
				
				idTurno = document.getElementById ("idTurno"+i).value;
				
				anio = document.getElementById ("anio"+i).value;
				
				numero = document.getElementById ("numero"+i).value;
				
				idProcedimiento = document.getElementById ("idProcedimiento"+i).value;
				idJuzgado = document.getElementById ("idJuzgado"+i).value;
				idInstJuzgado = document.getElementById ("idInstJuzgado"+i).value;
				idJurisdiccion = document.getElementById ("idJurisdiccion"+i).value;
				idPersona = document.getElementById ("idPersona"+i).value;
				codigoDesigna = document.getElementById ("codigoDesigna"+i).value;
				numJustificaciones = document.getElementById ("numJustificaciones"+i).value;
				validarJustificaciones = document.getElementById ("validarJustificaciones"+i).value;
				
				fIniAct = " ";
				if(objFechaIni!=null)
					fIniAct = document.getElementById ("fIniAct"+i).value;
				
				
				var isCheckFechaIni = "false";
				var isCheckFechaFin = "false";
				if(idProcedimiento ==""){
					idProcedimiento= " ";
					isCheckFechaIni = "false";
					isCheckFechaFin = "false"
				}else{
				
					if(objCheckFechaIni!=null)
						isCheckFechaIni= document.getElementById ("checkFechaIni"+i).checked;
					
					if(objCheckFechaFin!=null)
						isCheckFechaFin= document.getElementById ("checkFechaFin"+i).checked;
					
					
				
				}
				
				
				
				if(document.getElementById ("baja"+i).checked) 
					baja = "si";
				else 
					baja = "no";
			    
			    if(idJuzgado=="")
				    idJuzgado = " ";
				   
				if(idInstJuzgado=="")
				    idInstJuzgado = " ";
				 if(fIniAct=="")
				    fIniAct = " ";
				 if(idJurisdiccion=="")
				    idJurisdiccion = " ";
				    
				    
				datos = datos + anio + "," + 
								numero + "," + 
								idInstitucion + "," + 
								idTurno + "," + 
								idJuzgado + "," + 
								idInstJuzgado + "," + 
								isCheckFechaIni + "," + 
								isCheckFechaFin + "," + 
								idProcedimiento + "," + 
								fIniAct + "," + 
								idJurisdiccion + "," + 
								idPersona + "," +
								codigoDesigna + "," +
								numJustificaciones + "," +
								baja + "," +
								validarJustificaciones+ "#";
				
				
				
			
								 
			}
			
			
			if (datos == "") {
				alert ("<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.noDatosGuardar"/>");
				fin();
				return;
			}
			
			document.InformeJustificacionMasivaForm.tablaDatosDinamicosD.value = datos;
			document.InformeJustificacionMasivaForm.modo.value = "modificar";
			document.InformeJustificacionMasivaForm.submit();
		}
		
	</script>		
	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
		<% 
		
		if(isPermitidoGuardar){%>
		<siga:ConjBotonesAccion botones="g" clase="botonesDetalle"/>
		<%}else{%>
		<siga:ConjBotonesAccion botones="g" modo="<%=modoPestana%>" clase="botonesDetalle"/>
		<%}%>
	<!-- FIN: BOTONES BUSQUEDA -->

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>

</html>
		  
		
