<!-- listadoUnidadFamiliarEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.ScsUnidadFamiliarEJGBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@page import="com.siga.tlds.FilaExtElement"%>


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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) request.getAttribute("resultado");
	Hashtable valores = (Hashtable)request.getAttribute("sumavalores");
	String idSolicitante = request.getAttribute("idSolicitante").toString();
	String accion = (String)request.getSession().getAttribute("accion");
	Hashtable solicitante = new Hashtable();
		
	String anio = "", numero = "", idTipoEJG = "", botones ="", botonVolverNuevo = "V,N";
	
	if (accion.equalsIgnoreCase("ver")){
		botones = "";
	}
	else {
		botones = "C,E,B";
	}
	
	Hashtable fila = new Hashtable();
	
	try {
		
		anio = request.getParameter("ANIO").toString();
		numero = request.getParameter("NUMERO").toString();
		idTipoEJG = request.getParameter("IDTIPOEJG").toString();
	}	
	catch(Exception e){
		Hashtable miHash = (Hashtable) request.getAttribute("DATOSEJG");
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
	}	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.operarUnidadFamiliar.literal.unidadFamiliar"/></title>
	
	<script type="text/javascript">
		function refrescarLocal()
		{	
			buscar();
		}
	</script>
	
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.unidadFamiliar" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body class="tablaCentralCampos" >	
	
	
	<html:form action="/JGR_UnidadFamiliarPerJG.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="abrirPestana">

		<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idPersonaJG" value="">

		<input type="hidden" name="idInstitucionEJG" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG %>">
		<input type="hidden" name="anioEJG" value="<%=anio %>">
		<input type="hidden" name="numeroEJG" value="<%=numero %>">

		<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.EJG_UNIDADFAMILIAR %>">
		<input type="hidden" name="tituloE" value="gratuita.personaJG.literal.unidadFamiliar">
		<input type="hidden" name="localizacionE" value="">
		<input type="hidden" name="accionE" value="nuevo">
		<input type="hidden" name="actionE" value="/JGR_UnidadFamiliarPerJG.do">
		<input type="hidden" name="pantallaE" value="M">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
			

		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";
							;
							ScsEJGAdm adm = new ScsEJGAdm(usr);

							Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(),
									anio, numero, idTipoEJG);

							if (hTitulo != null) {
								t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
								t_apellido1 = (String) hTitulo
										.get(ScsPersonaJGBean.C_APELLIDO1);
								t_apellido2 = (String) hTitulo
										.get(ScsPersonaJGBean.C_APELLIDO2);
								t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
								t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
								t_tipoEJG = (String) hTitulo.get("TIPOEJG");
							}
						%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoUnidadFamiliar"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.personaJG.literal.parentescoNormalizado,gratuita.busquedaEJG.literal.nif,gratuita.busquedaEJG.literal.nombre,gratuita.operarInteresado.literal.ingresosAnuales,gratuita.operarInteresado.literal.bienesMobiliarios,gratuita.operarInteresado.literal.bienesInmuebles,gratuita.operarInteresado.literal.otrosBienes,"
		   tamanoCol="10,10,26,10,10,10,10,14"
		   alto="100%"
		   ajusteBotonera="true"		
		   mensajeBorrado="gratuita.ejg.unidadFamiliar.borrado"
		   modal="G"
		  >
	<%
		if ((obj != null) && (obj.size() > 0)) {

				//preparamos los importes para visualizarlos.
				java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols();
				dfs.setDecimalSeparator(',');
				dfs.setGroupingSeparator('.');
				java.text.DecimalFormat dfEuro = new java.text.DecimalFormat(
						"#,##0.00", dfs);
				FilaExtElement[] elems = null;

				if (!accion.equalsIgnoreCase("ver")) {
					elems = new FilaExtElement[4];
					elems[3] = new FilaExtElement("comunicar", "comunicar",
							SIGAConstants.ACCESS_READ);
				} else {
					elems = new FilaExtElement[3];
				}
				int recordNumber = 0;
				int recordNumber2 = 1;
				while (obj.size() > recordNumber) {
					fila = (Hashtable) obj.get(recordNumber);
					if (fila.get("IDPERSONA").equals(idSolicitante)) {
						solicitante = fila;
					} else {
	%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber2)%>' botones="<%=botones%>" elementos="<%=elems%>" clase="listaNonEdit" modo="<%=accion%>">
					<td>

					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_1" value="<%=PersonaJGAction.EJG_UNIDADFAMILIAR%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_2" value="gratuita.personaJG.literal.unidadFamiliar">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_3" value="gratuita.personaJG.literal.unidadFamiliar">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_4" value="editar">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_5" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_6" value="<%=fila.get("IDPERSONA")%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_7" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_8" value="<%=idTipoEJG%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_9" value="<%=anio%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber2)%>_10" value="<%=numero%>">
					<%
						if (fila.get("SOLICITANTE").equals(
													ClsConstants.DB_TRUE)) {
					%>
					   <siga:Idioma key="gratuita.busquedaEJG.literal.solicitante"/>
					<%
						} else {
					%>
					   <%=fila.get("PARENTESCO")%>&nbsp;
					<%
						}
					%>
					
					</td>
					<td><%=fila.get("NIF")%>&nbsp;</td>
					<td><%=fila.get("NOMBRE") + " "
										+ fila.get("APELLIDO1") + " "
										+ fila.get("APELLIDO2")%>&nbsp;</td>
					<td align="right"><%
						if (fila.get("IMPORTEINGRESOSANUALES") != null
													&& !fila.get("IMPORTEINGRESOSANUALES")
															.equals("")) {
					%><%=dfEuro
															.format(Double
																	.valueOf((String) fila
																			.get("IMPORTEINGRESOSANUALES")))%>&euro;<%
						} else {
					%>&nbsp;<%
						}
					%></td>
					<td align="right"><%
						if (fila.get("IMPORTEBIENESMUEBLES") != null
													&& !fila.get("IMPORTEBIENESMUEBLES")
															.equals("")) {
					%><%=dfEuro
															.format(Double
																	.valueOf((String) fila
																			.get("IMPORTEBIENESMUEBLES")))%>&euro;<%
						} else {
					%>&nbsp;<%
						}
					%></td>
					<td align="right"><%
						if (fila.get("IMPORTEBIENESINMUEBLES") != null
													&& !fila.get("IMPORTEBIENESINMUEBLES")
															.equals("")) {
					%><%=dfEuro
															.format(Double
																	.valueOf((String) fila
																			.get("IMPORTEBIENESINMUEBLES")))%>&euro;<%
						} else {
					%>&nbsp;<%
						}
					%></td>
					<td align="right"><%
						if (fila.get("IMPORTEOTROSBIENES") != null
													&& !fila.get("IMPORTEOTROSBIENES")
															.equals("")) {
					%><%=dfEuro
															.format(Double
																	.valueOf((String) fila
																			.get("IMPORTEOTROSBIENES")))%>&euro;<%
						} else {
					%>&nbsp;<%
						}
					%></td>
					</siga:FilaConIconos>					
		<%
								recordNumber2++;
											}
											recordNumber++;
										}

										String importeIngresosAnualesFormat = "";
										String importeMueblesFormat = "";
										String importeInmueblesFormat = "";
										String importeOtrosBienesFormat = "";

										if (!solicitante.isEmpty()) {
											importeIngresosAnualesFormat = (String) solicitante
													.get("IMPORTEINGRESOSANUALES");
											importeMueblesFormat = (String) solicitante
													.get("IMPORTEBIENESMUEBLES");
											importeInmueblesFormat = (String) solicitante
													.get("IMPORTEBIENESINMUEBLES");
											importeOtrosBienesFormat = (String) solicitante
													.get("IMPORTEOTROSBIENES");

											if (importeIngresosAnualesFormat != null
													&& !importeIngresosAnualesFormat.equals(""))
												importeIngresosAnualesFormat = dfEuro.format(Double
														.valueOf(importeIngresosAnualesFormat));
											else
												importeIngresosAnualesFormat = "0";
											if (importeMueblesFormat != null
													&& !importeMueblesFormat.equals(""))
												importeMueblesFormat = dfEuro.format(Double
														.valueOf(importeMueblesFormat));
											else
												importeMueblesFormat = "0";
											if (importeInmueblesFormat != null
													&& !importeInmueblesFormat.equals(""))
												importeInmueblesFormat = dfEuro.format(Double
														.valueOf(importeInmueblesFormat));
											else
												importeInmueblesFormat = "0";
											if (importeOtrosBienesFormat != null
													&& !importeOtrosBienesFormat.equals(""))
												importeOtrosBienesFormat = dfEuro.format(Double
														.valueOf(importeOtrosBienesFormat));
											else
												importeOtrosBienesFormat = "0";
							%>

	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber2+1)%>'
		botones="" visibleBorrado="false" visibleEdicion="false"
		visibleConsulta="false" clase="listaNonEdit" modo="<%=accion%>">

		<td width="10%"><siga:Idioma
			key="gratuita.busquedaEJG.literal.solicitante" /></td>
		<td width="10%"><%=UtilidadesString
											.mostrarDatoJSP(solicitante
													.get("NIF"))%></td>
		<td width="30%"><%=solicitante.get("NOMBRE") + " "
									+ solicitante.get("APELLIDO1") + " "
									+ solicitante.get("APELLIDO2")%>&nbsp;</td>
		<td width="10%" align="right"><%=importeIngresosAnualesFormat%>&euro;</td>
		<td width="10%" align="right"><%=importeMueblesFormat%>&euro;</td>
		<td width="10%" align="right"><%=importeInmueblesFormat%>&euro;</td>
		<td width="10%" align="right"><%=importeOtrosBienesFormat%>&euro;</td>

	</siga:FilaConIconos>

	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber2+2)%>' botones="" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false" clase="listaNonEdit" modo="<%=accion%>">
			   <td width="50%"  colspan="3" align="right"><b><siga:Idioma key="gratuita.listadoUnidadFamiliar.literal.totalUnidadFamiliar"/></b></td>
			   <td width="10%" align="right" ><%=(valores.get("SUMAINGRESOS") == null || valores
													.get("SUMAINGRESOS")
													.equals("")) ? "0"
											: dfEuro
													.format(Double
															.valueOf((String) valores
																	.get("SUMAINGRESOS")))%>&euro;</td>
			   <td width="10%" align="right"><%=(valores.get("SUMAMUEBLES") == null || valores
													.get("SUMAMUEBLES").equals(
															"")) ? "0"
											: dfEuro
													.format(Double
															.valueOf((String) valores
																	.get("SUMAMUEBLES")))%>&euro;</td>
			   <td width="10%" align="right"><%=(valores.get("SUMAINMUEBLES") == null || valores
													.get("SUMAINMUEBLES")
													.equals("")) ? "0"
											: dfEuro
													.format(Double
															.valueOf((String) valores
																	.get("SUMAINMUEBLES")))%>&euro;</td>
			   <td width="10%" align="right"><%=(valores.get("SUMAOTROS") == null || valores
													.get("SUMAOTROS")
													.equals("")) ? "0" : dfEuro
											.format(Double
													.valueOf((String) valores
															.get("SUMAOTROS")))%>&euro;</td>
				</siga:FilaConIconos>					

		   
		   <%
							   		   	}
							   		   %>	   	
	<%
	   			} else {
	   		%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
		}
	%>	
		   </siga:TablaCabecerasFijas>		
			
	

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="<%=botonVolverNuevo%>" modo="<%=accion%>" clase="botonesDetalle" />
	
	<script type="text/javascript">
				
	//Asociada al boton Cerrar
	function accionVolver()
	{
		document.forms[1].action="./JGR_EJG.do";	
		document.forms[1].modo.value="buscar";
		document.forms[1].target="mainWorkArea"; 
		document.forms[1].submit(); 

	}
	
	function accionNuevo()
	{
		document.forms[0].target = "submitArea";
		document.forms[0].modo.value = "abrirPestana";
		document.forms[0].accionE.value = "nuevo";
		var resultado=ventaModalGeneral(document.forms[0].name,"G");
		if (resultado=="MODIFICADO") buscar();
		
	}
	function buscar()
	{
		document.forms[1].modo.value = "abrir";										
		document.forms[1].target = "mainPestanas";
		document.forms[1].submit();
	}
	function comunicar(fila) {

		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
			
	   	datos = idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + '#';
	   				 
		
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=usr.getLocation() %>'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='EJG'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='asolicitantes' value='S'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='tablaDatosDinamicosD' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		document.appendChild(formu);
		formu.tablaDatosDinamicosD.value = datos;
		formu.submit();
		
		
	   
	} 	
	
	</script>
	




	<html:form action="/JGR_UnidadFamiliarEJG" method="post" target="submitArea">
		<input type="hidden" name="modo" value="<%=accion%>">
		<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG%>">
		<input type="hidden" name="anio" value="<%=anio%>">
		<input type="hidden" name="numero" value="<%=numero%>">
	</html:form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
</body>	
</html>