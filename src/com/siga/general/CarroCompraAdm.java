/*
 * Created on 01-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.UsrBean;


/**
 * @author daniel.campos
 *
 */

/**
 * Clase CarroCompraAdm
 * <BR>
 * Administra el manejo de los carros de la compra
 * 
 */

public class CarroCompraAdm 
{
	static public final String nombreCarro = "_CARRO_COMPRA_";

	/**
	 * getCarroCompra
	 * Obtiene el carro de la compra del usuario
	 * @param idPersona que realiza la compra
	 * @param idInstitucion que realiza la compra
	 * @param request donde se almacena el carro
	 * @return el carro de la compra del usuario
	 * @exception SIGAException
	 */
	static public CarroCompra getCarroCompra (Long idPersona, Integer idInstitucion, Integer idInstitucionPresentador, HttpServletRequest request) throws SIGAException {
		try {
			CarroCompra carro = (CarroCompra) request.getSession().getAttribute(CarroCompraAdm.nombreCarro);
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			if (carro == null) {
				carro = new CarroCompra(idPersona, idInstitucion, idInstitucionPresentador, user);
				CarroCompraAdm.setCarroCompra(carro, request);
			}
			else {
				// Si solicita el carro la misma persona, se le devuelve su carro
				if (idPersona.longValue() == carro.getIdPersona().longValue()) {
					carro.actualizarInstitucionPresentacion(idInstitucionPresentador);
					return carro;
				}
				// Si no, se crea uno nuevo para la nueva persona y se borra el antiguo
				else {
					carro = new CarroCompra(idPersona, idInstitucion,idInstitucionPresentador,user);
					CarroCompraAdm.setCarroCompra(carro, request);
				}
			}
			return carro;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al recuperar el carro asociado", e);
		}
	}
	
	/**
	 * setCarroCompra
	 * Almacena el carro de la compra del usuario
	 * @param carro a guardar
	 * @param request donde se almacena el carro
	 * @exception SIGAException
	 */
	static public void setCarroCompra (CarroCompra carro, HttpServletRequest request) throws SIGAException {
		try {
			request.getSession().setAttribute(CarroCompraAdm.nombreCarro, carro);
		}
		catch (Exception e) {
			throw new SIGAException ("Error al almacenar el carro asociado", e);
		}
	}
	
	/**
	 * eliminarCarroCompra
	 * Elimina el carro de la session
	 * @param request donde se almacena el carro
	 * @throws SIGAException
	 */
	static public void eliminarCarroCompra (HttpServletRequest request) throws SIGAException {
		CarroCompraAdm.setCarroCompra(null, request);
	}
}
