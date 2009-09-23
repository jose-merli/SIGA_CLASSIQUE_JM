<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="displaytag.tld" prefix="display" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		<link rel="stylesheet" href="/SIGA/html/css/displaytag.css" type="text/css"/>
		<html:base/>
	</head>
	<body>
		<html:errors/>
		<div>
	   		<html:form action="/CEN_BusquedaLetradosPruebas.do" method="POST">
	    		<html:hidden property="modo" value="busqueda"/>
				<table>
					<tr>
	  					<td class="labelText">
							Residencia
	  					</td>
	  					<td align="left">
							<select name="residente" class="boxCombo">
								<option value="0" >&nbsp;</option>
								<option value="S" >Sin Residencia</option>
								<option value="M" >Residencia Múltiple</option>
							</select>
	  					</td>
	   					<td class="labelText">
		  					Búsqueda exacta
						</td>
						<td>
							<html:checkbox property="busquedaExacta"/>
						</td>
					</tr>
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
						</td>
						<td>
							<html:text property="nifCif" name="CensoBusquedaLetradosBean" size="15" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
						</td>				
						<td>
							<html:text property="nombre" name="CensoBusquedaLetradosBean" size="30" styleClass="box"/>
						</td>
					</tr>
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
						</td>
						<td>
							<html:text property="apellidos1" name="CensoBusquedaLetradosBean" size="30" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
						</td>
						<td>
							<html:text property="apellidos2" name="CensoBusquedaLetradosBean" size="30" styleClass="box"/>
						</td>
					</tr>
				</table>
				<html:submit value="buscar"/>
	   		</html:form>
		</div>
		
		<table style="border:0;">
			<tr>
				<td>
					<display:table name="${CensoBusquedaLetradosBean.table}" uid="${CensoBusquedaLetradosBean.tableName}" id="${CensoBusquedaLetradosBean.tableName}" export="true" defaultsort="2" sort="external" 
									defaultorder="ascending" pagesize="${CensoBusquedaLetradosBean.pageSize}" size="${CensoBusquedaLetradosBean.totalTableSize}" 
									partialList="true" requestURI="/CEN_BusquedaLetradosPruebas.do">
						<display:setProperty name="decorator.media.html"  value="es.satec.siga.util.decorators.censo.HtmlActionsDecorator"/>
						<display:setProperty name="decorator.media.csv"   value="es.satec.siga.util.decorators.CsvActionsDecorator"/>
						<display:setProperty name="decorator.media.excel" value="es.satec.siga.util.decorators.ExcelActionsDecorator"/>
						<display:setProperty name="decorator.media.xml"   value="es.satec.siga.util.decorators.XmlActionsDecorator"/>
						<display:setProperty name="decorator.media.pdf"   value="es.satec.siga.util.decorators.PdfActionsDecorator"/>
						<display:setProperty name="decorator.media.rtf"   value="es.satec.siga.util.decorators.RtfActionsDecorator"/>
						<display:column property="idPersona" title="Id"/>
						<display:column property="nombre" title="Nombre" sortable="true" nulls="false" maxLength="20" style="white-space: nowrap;"/>
						<display:column property="apellidos1" title="1º Apellido" sortable="true" nulls="false" maxLength="20" style="white-space: nowrap;"/>
						<display:column property="apellidos2" title="2º Apellido" sortable="true" nulls="false" maxLength="20" style="white-space: nowrap;"/>
						<display:column property="nifCif" title="NIF / CIF" sortable="true" nulls="false"/>
						<display:column property="fechaNacimiento" title="Fecha de Nacimiento" sortable="true" nulls="false" format="{0,date,dd-MM-yyyy}"/>
						<display:column property="idInstitucion" title="Institución" sortable="true" nulls="false"/>
						<display:column property="noAparecerRedAbogacia" title="Aparecer" nulls="false"/>
					</display:table>
				</td>
			</tr>
		</table>
	</body>
</html:html>