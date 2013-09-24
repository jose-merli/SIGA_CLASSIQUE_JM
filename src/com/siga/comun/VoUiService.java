package com.siga.comun;

import java.util.List;
/**
 * 
 * @author jorgeta 
 * @date   24/09/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 * @param <L>
 * @param <J>
 */

public interface VoUiService<L, J> {

	/**
	 * Metodo que transforma una lista de Objetos db a objetos Form
	 * @param dbList
	 * @return
	 * @throws SIGAException
	 */
//	public List<L> getDb2FormList(List<K> dbList)throws SIGAException;
	/**
	 * Metodo que transforma una lista de Objetos vo a objetos Form
	 * @param voList
	 * @return
	 * @throws SIGAException
	 */
	public List<L> getVo2FormList(List<J> voList);
	/**
	 * Metodo que transforma un objeto Form a un tipo de objeto Vo
	 * @param objectVo
	 * @return
	 */
	public J getForm2Vo(L objectForm );
	/**
	 * Metodo que transforma un objeto Vo a un tipo de objeto form
	 * @param objectVo
	 * @return
	 */
	public L getVo2Form(J objectVo );
	

}
