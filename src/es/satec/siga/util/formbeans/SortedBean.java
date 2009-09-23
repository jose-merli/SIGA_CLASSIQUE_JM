package es.satec.siga.util.formbeans;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.siga.general.MasterForm;

import es.satec.siga.util.vos.SortedVo;

public class SortedBean extends MasterForm {
	private static final long serialVersionUID = 8632345372808728830L;
	private static final String DEFAULT_SORT_DIRECTION="2";
	private static final String DEFAULT_SORT_COLUMN="1";
	private static final String DEFAULT_TABLE_NAME="tabla";
	
	private boolean ascendente=true;
	private String sortColumn=DEFAULT_SORT_COLUMN;
	private String tableName=DEFAULT_TABLE_NAME;
	protected List columnTranslation=null;

	public SortedBean(){
		super();
	}
	
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
			column=DEFAULT_SORT_COLUMN;

		if (columnTranslation!=null){
			try {
				int posicion=Integer.parseInt(column)-1;
				column=(String)columnTranslation.get(posicion);
			} catch (NumberFormatException nfe){
			}
		}
		
		sortColumn=column;
	}
	
	public String getSortColumn(){
		return sortColumn;
	}
	
	public void setTableName(String tableName){
		this.tableName=tableName;
	}

	public String getTableName(){
		return tableName;
	}

	public void setRequest(HttpServletRequest request){
		ParamEncoder encoder=new ParamEncoder(getTableName());

		setSortColumn(request.getParameter(encoder.encodeParameterName(TableTagParameters.PARAMETER_SORT)));
		setOrderDirection(request.getParameter(encoder.encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
	}

	public SortedVo getSortedVo(){
		SortedVo ordenacion=new SortedVo();
		
		ordenacion.setCriteria(this.getSortColumn(), this.getOrderDirection());
		
		return ordenacion;
	}
}
