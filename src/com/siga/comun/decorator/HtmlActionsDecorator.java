package com.siga.comun.decorator;

import java.util.List;

import org.displaytag.decorator.TableDecorator;

import com.atos.utils.AccessConstants;
import com.atos.utils.ClsConstants;
import com.atos.utils.ModoConstants;
import com.atos.utils.RowButton;
import com.atos.utils.RowButtonsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public abstract class HtmlActionsDecorator extends TableDecorator {


	public abstract String getActions();

	protected String printButtons(String id, List<RowButton> botones ){

		StringBuffer strBuff=new StringBuffer();
		UsrBean usrbean = getUser();

		for (RowButton rb: botones){
			switch(rb.getEstado()){
			case ON:
				strBuff.append("<img src=\"");
				strBuff.append(rb.getIconOn());
				strBuff.append("\" ");
				strBuff.append("style=\"cursor:hand;\" ");
				strBuff.append("alt=\"");
				strBuff.append(UtilidadesString.getMensajeIdioma(usrbean,rb.getLabel()));
				strBuff.append("\" ");
				strBuff.append("border=\"0\" ");
				strBuff.append("onClick=\"selectRow('");
				strBuff.append(tableModel.getId());
				strBuff.append("','");
				strBuff.append(id);
				strBuff.append("','0');");
				strBuff.append(rb.getAction());
				strBuff.append("('");
				strBuff.append(id);
				strBuff.append("',");				
				strBuff.append("getParentForm(this).name");
				strBuff.append(");\">");
				break;
			case DISABLED:
				strBuff.append("<img src=\"");
				strBuff.append(rb.getIconDisabled());
				strBuff.append("\" ");
				strBuff.append("alt=\"");
				strBuff.append(UtilidadesString.getMensajeIdioma(usrbean,rb.getLabel()));
				strBuff.append("\" ");
				strBuff.append("border=\"0\"\">");
			}
		}

		return strBuff.toString();
	}


	public void setBotones(List<RowButton> botones, ModoConstants modo, AccessConstants tipoAcceso) {
		for(RowButton rb: botones){
			switch(checkAccess(tipoAcceso, rb.getAccess())){
			case ACCESS_NONE: 
			case ACCESS_DENY: 
				rb.setEstado(RowButton.Estado.DISABLED);
				break;
			case ACCESS_READ: // Con ACCESO de lectura no se puede editar o borrar
				if (rb.getButton().equals(RowButtonsConstants.EDITAR) || 
						rb.getButton().equals(RowButtonsConstants.BORRAR) ){
					rb.setEstado(RowButton.Estado.DISABLED);
				}
				break;
			case ACCESS_FULL: // Con MODO de consulta no se puede editar o borrar
				if (ModoConstants.CONSULTA.equals(modo) &&
						(rb.getButton().equals(RowButtonsConstants.EDITAR) || 
								rb.getButton().equals(RowButtonsConstants.BORRAR)) ){
					rb.setEstado(RowButton.Estado.DISABLED);
				}
			default: break;
			}
		}
	}

	private AccessConstants checkAccess(AccessConstants accesoUsuario, AccessConstants accesoBoton) {
		if (accesoUsuario.getPriority() < accesoBoton.getPriority()){
			return AccessConstants.ACCESS_NONE; 
		}
		return accesoUsuario;
	}

	/**
	 * 
	 * @param usrBean
	 * @param idInstitucion
	 * @return <code>true</code> si la institucion del usuario conectado es igual a <code>idInstitucion</code>,
	 *         <code>false</code> en caso contrario.
	 */
	protected boolean ckeckInstitucionConectada(String idInstitucion){
		return getUser().getLocation().matches(idInstitucion);
	}

	/**
	 *  
	 * @param idInstitucion
	 * @return <code>true</code> si la institucion del usuario conectado es un consejo, <code>false</code> en caso contrario.
	 */
	protected boolean checkConsejo(String idInstitucion) {
		return idInstitucion.equals("2000") || idInstitucion.startsWith("3");
	}


	protected UsrBean getUser() {
		return (UsrBean)getPageContext().getSession().getAttribute(ClsConstants.USERBEAN);
	}

}
