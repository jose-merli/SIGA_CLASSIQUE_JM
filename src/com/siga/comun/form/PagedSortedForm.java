package com.siga.comun.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.decorator.CheckboxDecorator;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortDirection;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;

/**
 * Contiene los metodos necesarios para realizar la paginacion y ordenacion en una jsp. 
 *
 */
public abstract class PagedSortedForm extends BaseForm{

	private static final long serialVersionUID = 8632345372808728830L;
	
	private static final String DEFAULT_SORT_DIRECTION="2";
	private static final String DEFAULT_SORT_COLUMN="1";
	private static final String DEFAULT_TABLE_NAME="tabla";
	private static final String DEFAULT_SELECT_PARAMETER_NAME="_chk";
	private static final String DEFAULT_MSG_IR_A_PAGINA = "Ir a página";
	private static final String RECURSO_MSG_IR_A_PAGINA = "messages.irApagina";
	private static final String DEFAULT_MSG_PRIMERO = "Primero";
	private static final String RECURSO_MSG_PRIMERO = "messages.primero";
	private static final String DEFAULT_MSG_ULTIMO = "Último";
	private static final String RECURSO_MSG_ULTIMO = "messages.ultimo";
	private static final String DEFAULT_MSG_SIGUIENTE = "Siguiente";
	private static final String RECURSO_MSG_SIGUIENTE = "messages.siguiente";
	private static final String DEFAULT_MSG_ANTERIOR = "Anterior";
	private static final String RECURSO_MSG_ANTERIOR = "messages.anterior";
	private static final String DEFAULT_MSG_ITEM_NAME = "elemento";
	private static final String RECURSO_MSG_ITEM_NAME = "messages.itemName";
	private static final String DEFAULT_MSG_ITEMS_NAME = "elementos";
	private static final String RECURSO_MSG_ITEMS_NAME = "messages.itemsName";
	private static final String DEFAULT_MSG_NO_ENCONTRADO = "No se han encontrado";
	private static final String RECURSO_MSG_NO_ENCONTRADO = "messages.noEncontrado";
	private static final String DEFAULT_MSG_ENCONTRADO_UNO = "Encontrado un";
	private static final String RECURSO_MSG_ENCONTRADO_UNO = "messages.encontradoUno";
	private static final String DEFAULT_MSG_MOSTRANDO_DEL = "encontrados, mostrando del";
	private static final String RECURSO_MSG_MOSTRANDO_DEL = "messages.mostrandoDel";
	private static final String DEFAULT_MSG_AL = "al";
	private static final String RECURSO_MSG_AL = "messages.al";
	private static final String DEFAULT_MSG_MOSTRANDO_TODOS = "encontrados, mostrando todos los";
	private static final String RECURSO_MSG_MOSTRANDO_TODOS = "messages.mostrandoTodos";
	public static final String SELECT_ALL_TRUE="on";
	private static final String SELECT_ALL_PAGES_TRUE="on";
	
	private boolean ascendente=true;
	private String sortColumn=DEFAULT_SORT_COLUMN;
	private String tableName=DEFAULT_TABLE_NAME;
	private String selectParameterName=DEFAULT_SELECT_PARAMETER_NAME;
	private String msgIrApagina = DEFAULT_MSG_IR_A_PAGINA;
	private String msgPrimero = DEFAULT_MSG_PRIMERO;
	private String msgUltimo = DEFAULT_MSG_ULTIMO;
	private String msgAnterior = DEFAULT_MSG_ANTERIOR;
	private String msgSiguiente = DEFAULT_MSG_SIGUIENTE;
	private String msgElemento = DEFAULT_MSG_ITEM_NAME;
	private String msgElementos = DEFAULT_MSG_ITEMS_NAME;
	private String msgNoEncontrado = DEFAULT_MSG_NO_ENCONTRADO;
	private String msgEncontradoUno = DEFAULT_MSG_ENCONTRADO_UNO;
	private String msgMostrandoDel = DEFAULT_MSG_MOSTRANDO_DEL;
	private String msgAl = DEFAULT_MSG_AL;
	private String msgMostrandoTodos = DEFAULT_MSG_MOSTRANDO_TODOS;
	
	private List<String> columnTranslation=null;
	private List<String> selectedElements=new ArrayList<String>();

	private static final Integer DEFAULT_PAGE_SIZE = new Integer(10);
	private static final Integer DEFAULT_PAGE=new Integer(1);
	private Integer pageSize=DEFAULT_PAGE_SIZE;
	private Integer page=DEFAULT_PAGE;
	private Integer totalTableSize=null;
	private static final String SELECT_ALL_NAME="selectAll";
	private String selectAllName = SELECT_ALL_NAME;
	private String selectAll;
	private static final String SELECT_ALL_PAGES_NAME="selectAllPages";
	private String selectAllPagesName = SELECT_ALL_PAGES_NAME;
	private String selectAllPages;
	private static final String DEFAULT_BACKUP_SELECTED_NAME="backupSelected";
	private String backupSelectedName=DEFAULT_BACKUP_SELECTED_NAME;
	private String backupSelected;	
	
	
	/**
	 * Lista donde se almacenan los resultados de las busquedas
	 */
	private List<Vo> table;
	
	/**
	 * Este método carga los recursos de BD para los literales que se muestran
	 * en la paginación del displayTag (por defecto está en displaytag.properties en castellano).
	 * Para que estos mensajes surtan efecto se deben setear como propiedades en cada displaytag:table
	 * Hay una página creada con todos /html/jsp/general/mensajesDisplayTag.jsp
	 * @param usrbean
	 */
	public void updateMsg(UsrBean usrbean){
		this.msgAl = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_AL);
		if (RECURSO_MSG_AL.equals(this.msgAl)){
			this.msgAl = DEFAULT_MSG_AL;
		}
		this.msgAnterior = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_ANTERIOR);
		if (RECURSO_MSG_ANTERIOR.equals(this.msgAnterior)){
			this.msgAnterior = DEFAULT_MSG_ANTERIOR;
		}
		this.msgElemento = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_ITEM_NAME);
		if (RECURSO_MSG_ITEM_NAME.equals(this.msgElemento)){
			this.msgElemento = DEFAULT_MSG_ITEM_NAME;
		}
		this.msgElementos = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_ITEMS_NAME);
		if (RECURSO_MSG_ITEMS_NAME.equals(this.msgElementos)){
			this.msgElementos = DEFAULT_MSG_ITEMS_NAME;
		}
		this.msgEncontradoUno = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_ENCONTRADO_UNO);
		if (RECURSO_MSG_ENCONTRADO_UNO.equals(this.msgEncontradoUno)){
			this.msgEncontradoUno = DEFAULT_MSG_ENCONTRADO_UNO;
		}
		this.msgIrApagina = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_IR_A_PAGINA);
		if (RECURSO_MSG_IR_A_PAGINA.equals(this.msgIrApagina)){
			this.msgIrApagina = DEFAULT_MSG_IR_A_PAGINA;
		}
		this.msgMostrandoDel = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_MOSTRANDO_DEL);
		if (RECURSO_MSG_MOSTRANDO_DEL.equals(this.msgMostrandoDel)){
			this.msgMostrandoDel = DEFAULT_MSG_MOSTRANDO_DEL;
		}
		this.msgMostrandoTodos = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_MOSTRANDO_TODOS);
		if (RECURSO_MSG_MOSTRANDO_TODOS.equals(this.msgMostrandoTodos)){
			this.msgMostrandoTodos = DEFAULT_MSG_MOSTRANDO_TODOS;
		}
		this.msgNoEncontrado = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_NO_ENCONTRADO);
		if (RECURSO_MSG_NO_ENCONTRADO.equals(this.msgNoEncontrado)){
			this.msgNoEncontrado = DEFAULT_MSG_NO_ENCONTRADO;
		}
		this.msgPrimero = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_PRIMERO);
		if (RECURSO_MSG_PRIMERO.equals(this.msgPrimero)){
			this.msgPrimero = DEFAULT_MSG_PRIMERO;
		}
		this.msgSiguiente = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_SIGUIENTE);
		if (RECURSO_MSG_SIGUIENTE.equals(this.msgSiguiente)){
			this.msgSiguiente = DEFAULT_MSG_SIGUIENTE;
		}
		this.msgUltimo = UtilidadesString.getMensajeIdioma(usrbean, RECURSO_MSG_ULTIMO);
		if (RECURSO_MSG_ULTIMO.equals(this.msgUltimo)){
			this.msgUltimo = DEFAULT_MSG_ULTIMO;
		}		
	}
	
	public DisplaytagColumnDecorator getDecorator(String checkboxName){
		return new CheckboxDecorator(checkboxName, selectedElements, 
				SELECT_ALL_TRUE.equals(selectAll)&&SELECT_ALL_PAGES_TRUE.equals(selectAllPages));
	}
	
	public void setTable(List<Vo> table){
		this.table=table;
	}
	
	public List<Vo> getTable(){
		return this.table;
	}
	
	//Metodos para la ordenacion
	
	public void setOrderDirection(String direccion){
		if (StringUtils.isEmpty(direccion))
			direccion=DEFAULT_SORT_DIRECTION;
		if (SortOrderEnum.fromCode(Integer.parseInt(direccion)).equals(SortOrderEnum.ASCENDING))
			ascendente=true;
		else
			ascendente=false;
	}
	
	public boolean getOrderDirection(){
		return ascendente;
	}
	
	public boolean isAscendente(){
		return ascendente;
	}
	
	public boolean isDescendente(){
		return !ascendente;
	}
	
	public void setSortColumn(String column){
		if (StringUtils.isEmpty(column))
			column=getDefaultSortColumn();

		if (columnTranslation!=null){
			try {
				int posicion=Integer.parseInt(column);
				column=(String)columnTranslation.get(posicion);
			} catch (NumberFormatException nfe){
			}
		}
		
		sortColumn=column;
	}
	
	public abstract String getDefaultSortColumn();
	
	public String getSortColumn(){
		return sortColumn;
	}
	
	public void setTableName(String tableName){
		this.tableName=tableName;
	}

	public String getTableName(){
		return this.getClass().getSimpleName()+getAccion()+tableName;
	}

	public void setSelectParameterName(String selectParameterName){
		this.selectParameterName=selectParameterName;
	}

	public String getSelectParameterName(){
		return selectParameterName;
	}

	
	public void setRequest (HttpServletRequest request){
		ParamEncoder encoder=new ParamEncoder(getTableName());

		setPage(getParameterAttribute(request,encoder.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		setSortColumn(getParameterAttribute(request,encoder.encodeParameterName(TableTagParameters.PARAMETER_SORT)));
		setOrderDirection(getParameterAttribute(request,encoder.encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
	}

	private String getParameterAttribute(HttpServletRequest request,
			String encodeParameterName) {
		String aux = request.getParameter(encodeParameterName);
		if (aux == null)
			aux = (String) request.getAttribute(encodeParameterName);
		return aux;
	}

	public void updateRequest (HttpServletRequest request){
		if (!SELECT_ALL_TRUE.equals(selectAll)){
			setSelectedElements(request.getParameterValues(selectParameterName));
		}
	}
	
	public void setSelectedElements(String[] parameters) {
		if (null != backupSelected && !"".equals(backupSelected)){
			String [] aux = backupSelected.split(",");
			selectedElements.addAll(Arrays.asList(aux));
		}
		if (parameters!=null && parameters.length!=0){
			selectedElements.addAll(Arrays.asList(parameters));
		}
		//BEGIN BNS ELIMINA DUPLICADOS POR SI ACASO
		Set<String> mySet = new HashSet<String>(selectedElements);
		selectedElements = new ArrayList<String>(mySet);
		//END BNS
	}

	public List<String> getSelectedElements() {
		return selectedElements;
	}

	
	public void fromSortedVo(SortedVo vo){
		for (Map.Entry<String,SortDirection> entrada:vo.getCriteria()){
			this.sortColumn=entrada.getKey();
			this.ascendente=entrada.getValue().equals(SortDirection.ASCENDING);
		}
	}
	
	public SortedVo toSortedVo(){
		SortedVo vo=new SortedVo();
		String[] multiColumnSort = this.getSortColumn().split(",");
		if (multiColumnSort.length > 1){
			for(String column : multiColumnSort){
				vo.setCriteria(column.trim(), this.getOrderDirection());
			}
		} else
			vo.setCriteria(this.getSortColumn(), this.getOrderDirection());
		for(String columna : getColumnTranslation()){
			if (columna != null && !columna.equals("") && !columna.equals(this.getSortColumn())){
				vo.setCriteria(columna, this.getOrderDirection());
			}
		}
		
		return vo;
	}

	
	public void setColumnTranslation(List<String> lista){
		columnTranslation = lista;
	}

	public void setColumnTranslation(String ... columns) {
		columnTranslation = new ArrayList<String>();
		for (String column: columns){
			columnTranslation.add(column);
		}
	}
	
	public List<String> getColumnTranslation(){
		return columnTranslation;
	}



	//Metodos para la paginacion
	
	public void setPage(String page){
		if (StringUtils.isEmpty(page))
			this.page=DEFAULT_PAGE;
		else
			this.page=new Integer(page);
	}
	
	public Integer getPage(){
		return page;
	}
	
	public void setTotalTableSize(int totalTableSize){
		this.totalTableSize=new Integer(totalTableSize);
	}
	
	public void setTotalTableSize(Integer totalTableSize){
		this.totalTableSize=totalTableSize;
	}
	
	public void setTotalTableSize(String totalTableSize){
		if (!StringUtils.isEmpty(totalTableSize))
			this.totalTableSize=new Integer(totalTableSize);
	}
	
	public Integer getTotalTableSize(){
		if (totalTableSize==null)
			return new Integer(0);
		return totalTableSize;
	}

	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize==null)
			this.pageSize=DEFAULT_PAGE;
		else
			this.pageSize = pageSize;
	}

	public void fromPagedVo(PagedVo vo){
		setTotalTableSize(vo.getTotalListSize());
	}
	
	public PagedVo toPagedVo(){
		PagedVo vo=new PagedVo();
		vo.setPage(getPage());
		vo.setTotalListSize(getTotalTableSize());
		vo.setPageSize(getPageSize().intValue());
		return vo;
	}

	public String getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}

	public String getSelectAllPages() {
		return selectAllPages;
	}

	public void setSelectAllPages(String selectAllPages) {
		this.selectAllPages = selectAllPages;
	}
	public String getSelectAllName() {
		return selectAllName;
	}
	
	public void setSelectAllName(String selectAllName) {
		this.selectAllName = selectAllName;
	}
	public String getSelectAllPagesName() {
		return selectAllPagesName;
	}
	
	public void setSelectAllPagesName(String selectAllPagesName) {
		this.selectAllPagesName = selectAllPagesName;
	}

	public void setBackupSelectedName(String backupSelectedName) {
		this.backupSelectedName = backupSelectedName;
	}

	public String getBackupSelectedName() {
		return backupSelectedName;
	}

	public void setBackupSelected(String backupSelected) {
		this.backupSelected = backupSelected;
	}

	public String getBackupSelected() {
		return backupSelected;
	}

	public String getMsgIrApagina() {
		return msgIrApagina;
	}

	public void setMsgIrApagina(String msgIrApagina) {
		this.msgIrApagina = msgIrApagina;
	}

	public String getMsgPrimero() {
		return msgPrimero;
	}

	public void setMsgPrimero(String msgPrimero) {
		this.msgPrimero = msgPrimero;
	}

	public String getMsgUltimo() {
		return msgUltimo;
	}

	public void setMsgUltimo(String msgUltimo) {
		this.msgUltimo = msgUltimo;
	}

	public String getMsgAnterior() {
		return msgAnterior;
	}

	public void setMsgAnterior(String msgAnterior) {
		this.msgAnterior = msgAnterior;
	}

	public String getMsgSiguiente() {
		return msgSiguiente;
	}

	public void setMsgSiguiente(String msgSiguiente) {
		this.msgSiguiente = msgSiguiente;
	}

	public String getMsgElemento() {
		return msgElemento;
	}

	public void setMsgElemento(String msgElemento) {
		this.msgElemento = msgElemento;
	}

	public String getMsgElementos() {
		return msgElementos;
	}

	public void setMsgElementos(String msgElementos) {
		this.msgElementos = msgElementos;
	}

	public String getMsgNoEncontrado() {
		return msgNoEncontrado;
	}

	public void setMsgNoEncontrado(String msgNoEncontrado) {
		this.msgNoEncontrado = msgNoEncontrado;
	}

	public String getMsgEncontradoUno() {
		return msgEncontradoUno;
	}

	public void setMsgEncontradoUno(String msgEncontradoUno) {
		this.msgEncontradoUno = msgEncontradoUno;
	}

	public String getMsgMostrandoDel() {
		return msgMostrandoDel;
	}

	public void setMsgMostrandoDel(String msgMostrandoDel) {
		this.msgMostrandoDel = msgMostrandoDel;
	}

	public String getMsgAl() {
		return msgAl;
	}

	public void setMsgAl(String msgAl) {
		this.msgAl = msgAl;
	}

	public String getMsgMostrandoTodos() {
		return msgMostrandoTodos;
	}

	public void setMsgMostrandoTodos(String msgMostrandoTodos) {
		this.msgMostrandoTodos = msgMostrandoTodos;
	}
	
}
	