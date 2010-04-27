<logic:empty property="table" name="BusquedaColegiadosForm">	
	<div id="empty" style="font-weight:bold; text-align:center; vertical-align:bottom; height:50px;">
		<siga:Idioma key="messages.noRecordFound" />
	</div>
</logic:empty>

<logic:notEmpty property="table" name="BusquedaColegiadosForm">
<div class="displayTag">
  <cb:checkboxDecorator
	formBean="${BusquedaColegiadosForm}" formName="displ"
	submitUrl="/SIGA/CEN_BusquedaColegiados.do"
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
			decorator="checkboxDecorator" style="width:15px;" />
		
		<display:column property="nif" titleKey="censo.busquedaClientesAvanzada.literal.nif" 
				sortable="true" nulls="false" style="width:115px;" />
		
		<display:column property="nColegiado" titleKey="censo.busquedaClientesAvanzada.literal.nColegiado" 
				sortable="true" nulls="false" style="width:80px;" />
		
		<display:column property="apellidos" titleKey="gratuita.turnos.literal.apellidosSolo" 
				sortable="true" nulls="false" style="width:125px; white-space: nowrap;" />
		
		<display:column property="nombre" titleKey="censo.busquedaClientesAvanzada.literal.nombre" 
				sortable="true" nulls="false" style="width:110px; white-space: nowrap;" />
		
		<display:column property="fechaIncorporacionDesde" titleKey="censo.busquedaClientesAvanzada.literal.fechaIngreso" 
				sortable="true" nulls="false" style="width:90px; white-space: nowrap;" 
				decorator="com.siga.comun.decorator.ShortDateDecorator" />
		
		<!-- Si la lista de instituciones esta vacia significa que estamos en un colegio -->
		<logic:empty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="nombreInstitucion" class="hidden" headerClass="hidden" />
		</logic:empty>
		<logic:notEmpty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="nombreInstitucion" titleKey="censo.busquedaClientes.literal.institucion" 
				sortable="true" nulls="false" style="width:115px; white-space: nowrap;" />
		</logic:notEmpty>
		
		<display:column property="estadoFechaColegial" titleKey="censo.busquedaClientesAvanzada.literal.estadoColegial" 
			sortable="true" nulls="false" style="width:95px; white-space: nowrap;" />
		
		<display:column property="residente" titleKey="censo.busquedaClientesAvanzada.literal.residente" 
				sortable="false" nulls="false" style="width:70px; white-space: nowrap;" 
				decorator="com.siga.comun.decorator.SiNoDecorator" />/>
		
		<logic:empty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="fechaNacimiento" class="hidden" headerClass="hidden" />
			<display:column property="telefonoFijoMovil" titleKey="censo.busquedaClientesAvanzada.literal.tlfn1movil" 
				sortable="false" nulls="false" style="width:180px; white-space: nowrap;" />
		</logic:empty>
		<logic:notEmpty property="instituciones" name="BusquedaColegiadosForm">
			<display:column property="fechaNacimiento" titleKey="censo.busquedaClientesAvanzada.literal.fechaNacimiento" 
				sortable="true" nulls="false" style="width:90px; white-space: nowrap;" 
				decorator="com.siga.comun.decorator.ShortDateDecorator" />
			<display:column property="telefonoFijoMovil" class="hidden" headerClass="hidden" />
		</logic:notEmpty>
		
		
		<display:column property="actions" title="" nulls="false" style="text-align:left;" />
	</display:table>
  </cb:checkboxDecorator>
</div>


</logic:notEmpty>


<iframe name="submitArea" src="/SIGA/html/jsp/general/blank.jsp" style="display: none"></iframe>

