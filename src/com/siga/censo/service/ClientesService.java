package com.siga.censo.service;


import java.util.List;

import com.siga.censo.vos.NombreVO;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;

/**
 */
public interface ClientesService extends BusinessService{

	/**
	 * 
	 * @param nif
	 */
	public abstract List<String> getNifList(String nif);

	/**
	 * 
	 * @param cp
	 */
	public abstract List<String> getCodigoPostal(int cp);

	/**
	 * 
	 * @param telefono
	 */
	public abstract List<String> getTelefono(int telefono);

	/**
	 * 
	 * @param email
	 */
	public abstract List<String> getCorreoElectronico(String email);

	/**
	 * 
	 * @param cliente
	 */
	public  List<Vo> buscar(Vo cliente);

		
	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public List<NombreVO> getNombre(NombreVO apellido2);

	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public List<NombreVO> getApellido1(NombreVO apellido2);

	/**
	 * 
	 * @param apellido2
	 * @param apellido1
	 * @param nombre
	 */
	public List<NombreVO> getApellido2(NombreVO apellido2);

	
	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo) throws SIGAException;
	public List<Vo> buscar(Vo vo, String idioma, String idInstitucion) throws SIGAException;
	
}
