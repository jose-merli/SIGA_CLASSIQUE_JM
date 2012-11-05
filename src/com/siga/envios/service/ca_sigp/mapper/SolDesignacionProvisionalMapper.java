package com.siga.envios.service.ca_sigp.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisional;
import com.siga.envios.service.ca_sigp.vos.TipoDesignaLetradoProcurador;

public interface SolDesignacionProvisionalMapper {
	
	public EcomSoldesignaprovisional getDatosSolDesignaProvisional(Map<String, Object> parametrosMap) throws SQLException;
	public List<TipoDesignaLetradoProcurador> getDesignasEJGRelacionadas(Map<String, Object> parametrosMap) throws SQLException;
	public TipoDesignaLetradoProcurador getDesignaSeleccionada(Map<String, Object> parametrosMap) throws SQLException;
	
}
