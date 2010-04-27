package com.siga.censo.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.FastDateFormat;

import com.atos.utils.AccessConstants;
import com.atos.utils.ClsConstants;
import com.atos.utils.ModoConstants;
import com.atos.utils.RowButton;
import com.atos.utils.RowButtonsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.vos.ColegiadoVO;
import com.siga.comun.decorator.HtmlActionsDecorator;
import com.siga.comun.vos.Vo;

public class ColegiadoDecorator extends HtmlActionsDecorator {

	private static String SIN_ESTADO = "censo.busquedaClientes.literal.sinEstadoColegial";
	private static FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");

	public String getActions(){
		ColegiadoVO vo = (ColegiadoVO)getCurrentRowObject();
		String id = vo.getId();

		String modo = (String) this.getPageContext().getAttribute("modo");
		String accessType = (String) this.getPageContext().getAttribute("accessType");
		ModoConstants modoConstant = ModoConstants.getEnum(modo);
		AccessConstants accessConstant = AccessConstants.getEnum(accessType);

		//Si el usuario conectado no se corresponde con la institucion del colegiado, 
		//se cambia el modo a CONSULTA 
		if (!ckeckInstitucionConectada(vo.getIdInstitucion()))
			modoConstant = ModoConstants.CONSULTA;

		List<RowButton> botones = getBotones(vo,modoConstant);
		setBotones(botones, modoConstant, accessConstant);
		return printButtons(id, botones);
	}

	public String getEstadoFechaColegial(){
		ColegiadoVO colegiado = (ColegiadoVO)getCurrentRowObject();
		String value = null;
		HttpSession session = getPageContext().getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		if (null == colegiado.getDescEstadoColegial()){
			return UtilidadesString.getMensajeIdioma(usrbean.getLanguage(), SIN_ESTADO);
		}
		else{
			value = colegiado.getDescEstadoColegial();
	        if (null == colegiado.getFechaEstadoColegial())
	        	return value;
	        else
	        	return value += "(" + dateFormat.format(colegiado.getFechaEstadoColegial()) + ")";
		}
	}

	public String getTelefonoFijoMovil(){
		ColegiadoVO colegiado = (ColegiadoVO)getCurrentRowObject();
		String value = colegiado.getTelefono();
		if (null == value ){
			if(colegiado.getMovil() != null){
				return colegiado.getMovil() + "(M)";
			}
		}
		else if(colegiado.getMovil() != null) {
			value += " - " + colegiado.getMovil() + "(M)";
		}
		return value;
	}
	
	private List<RowButton> getBotones(Vo vo, ModoConstants modo) {
		List<RowButton> botones = new ArrayList<RowButton> ();
		ColegiadoVO colegiado = (ColegiadoVO) vo;

		botones.add(new RowButton(RowButtonsConstants.CONSULTAR));
		botones.add(new RowButton(RowButtonsConstants.EDITAR));

		// Si estamos en un consejo
		if (checkConsejo(getUser().getLocation())){
			// Si aplica la LOPD
			if (UtilidadesString.stringToBoolean(colegiado.getNoAparecerRedAbogacia())) {
				// si el colegiado es letrado
				if (UtilidadesString.stringToBoolean(colegiado.getLetrado())){
					botones.add(new RowButton(RowButtonsConstants.INFORMACIONLETRADO,AccessConstants.ACCESS_READ));
				}
				else{
					botones.add(new RowButton(RowButtonsConstants.INFORMACIONLETRADO,AccessConstants.ACCESS_SIGAENPRODUCCION));
				}
				botones.add((new RowButton(RowButtonsConstants.LOPD))); 
			}
			else{
				if (UtilidadesString.stringToBoolean(colegiado.getLetrado())){
					botones.add(new RowButton(RowButtonsConstants.INFORMACIONLETRADO,AccessConstants.ACCESS_READ));
					botones.add(new RowButton(RowButtonsConstants.COMUNICAR,AccessConstants.ACCESS_READ));
				}
				else{
					botones.add(new RowButton(RowButtonsConstants.INFORMACIONLETRADO,AccessConstants.ACCESS_SIGAENPRODUCCION));
					botones.add(new RowButton(RowButtonsConstants.COMUNICAR,AccessConstants.ACCESS_SIGAENPRODUCCION));
				}
			}
		}
		else{
			if (UtilidadesString.stringToBoolean(colegiado.getNoAparecerRedAbogacia())) {
				botones.add((new RowButton(RowButtonsConstants.LOPD))); 
			}
			else{
				botones.add(new RowButton(RowButtonsConstants.COMUNICAR));
			}
		}

		return botones;
	}

}
