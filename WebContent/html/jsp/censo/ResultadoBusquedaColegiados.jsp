<logic:empty property="table" name="BusquedaColegiadosForm">
	<logic:equal property="accion" value="buscar" name="BusquedaColegiadosForm">	
		<div class="labelText" id="empty" style="font-weight:bold; text-align:center; vertical-align:bottom; height:150px; margin-top:50px">
			<siga:Idioma key="messages.noRecordFound" />
		</div>
	</logic:equal>
	<logic:equal property="accion" value="buscarAvanzada" name="BusquedaColegiadosForm">	
		<div class="labelText" id="empty" style="font-weight:bold; text-align:center; vertical-align:bottom; height:150px; margin-top:50px;">
			<siga:Idioma key="messages.noRecordFound" />
		</div>
	</logic:equal>
</logic:empty>

<logic:notEmpty property="table" name="BusquedaColegiadosForm">
<div class="displayTag">
  <cb:checkboxDecorator
	formBean="${BusquedaColegiadosForm}" formName="displ"
	submitUrl="/SIGA/CEN_BusquedaColegiados.do"
	selectAllAjaxUrl="/SIGA/CEN_BusquedaColegiados.do?accion=getJQueryAllSearchedPKs"
	decoratorName="checkboxDecorator" checkboxName="_chk"
	selectAllName="${BusquedaColegiadosForm.selectAllName}"
	selectAllPagesName="${BusquedaColegiadosForm.selectAllPagesName}"
	backupSelectedName="${BusquedaColegiadosForm.backupSelectedName}"
	message="paginador.message.marcarDesmarcar">
	<dts:css height="0" />
	<display:table name="BusquedaColegiadosForm.table"
		uid="${BusquedaColegiadosForm.tableName}"
		id="${BusquedaColegiadosForm.tableName}" export="false" defaultsort="2"
		sort="external" defaultorder="ascending"
		pagesize="${BusquedaColegiadosForm.pageSize}"
		size="BusquedaColegiadosForm.totalTableSize" partialList="true"
		requestURI="/CEN_BusquedaColegiados.do" form="displ"
		excludedParams="${BusquedaColegiadosForm.selectParameterName} ${BusquedaColegiadosForm.selectAllName} ${BusquedaColegiadosForm.selectAllPagesName} ${BusquedaColegiadosForm.backupSelectedName} page deleteForm"
		class="dataScroll" >
		
		<display:setProperty name="decorator.media.html" value="com.siga.censo.decorator.ColegiadoDecorator" />
		
		<display:column property="id" class="hidden" headerClass="hidden" />
		
		<display:column property="id" title="<input type='checkbox' name='${BusquedaColegiadosForm.selectAllName}' id='${BusquedaColegiadosForm.selectAllName}'/>"
			decorator="checkboxDecorator" />
		
		<display:column property="nif" titleKey="censo.busquedaClientesAvanzada.literal.nif" 
				sortable="true" nulls="false" />
		
		<display:column property="nColegiado" titleKey="censo.busquedaClientesAvanzada.literal.nColegiado" 
				sortable="true" nulls="false" />
		
		<display:column property="apellidos" titleKey="gratuita.turnos.literal.apellidosSolo" 
				sortable="true" nulls="false" maxLength="40" />
		
		<display:column property="nombre" titleKey="censo.busquedaClientesAvanzada.literal.nombre" 
				sortable="true" nulls="false" maxLength="20"/>
		
		<display:column property="fechaIncorporacionDesde" titleKey="censo.busquedaClientesAvanzada.literal.fechaIngreso" 
				sortable="true" nulls="false" decorator="com.siga.comun.decorator.ShortDateDecorator" />
		
		<!-- Si la lista de instituciones esta vacia significa que estamos en un colegio -->
		<logic:empty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="nombreInstitucion" class="hidden" headerClass="hidden" />
		</logic:empty>
		<logic:notEmpty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="nombreInstitucion" titleKey="censo.busquedaClientes.literal.institucion" 
				sortable="true" nulls="false" />
		</logic:notEmpty>
		
		<display:column property="estadoFechaColegial" titleKey="censo.busquedaClientesAvanzada.literal.estadoColegial" 
			sortable="false" nulls="false" />
		
		<display:column property="residente" titleKey="censo.busquedaClientesAvanzada.literal.residente" 
				sortable="false" nulls="false" decorator="com.siga.comun.decorator.SiNoDecorator" />/>
		
		<logic:empty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="fechaNacimiento" class="hidden" headerClass="hidden" />
			<display:column property="telefonoFijoMovil" titleKey="censo.busquedaClientesAvanzada.literal.tlfn1movil" 
				sortable="false" nulls="false" />
		</logic:empty>
		<logic:notEmpty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="fechaNacimiento" titleKey="censo.busquedaClientesAvanzada.literal.fechaNacimiento" 
				sortable="true" nulls="false" decorator="com.siga.comun.decorator.ShortDateDecorator" />
			<display:column property="telefonoFijoMovil" class="hidden" headerClass="hidden" />
		</logic:notEmpty>
		
		
		<display:column property="actions" title="" nulls="false" style="white-space: nowrap; text-align:left;" />
	</display:table>
  </cb:checkboxDecorator>
</div>


</logic:notEmpty>


<iframe name="submitArea" src="/SIGA/html/jsp/general/blank.jsp" style="display: none"></iframe>

