package com.siga.censo.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.decorator.DisplaytagColumnDecorator;

import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.vos.ColegiadoVO;
import com.siga.comun.decorator.CheckboxDecorator;
import com.siga.comun.form.PagedSortedForm;
import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.comun.vos.Vo;

public class BusquedaColegiadosForm extends PagedSortedForm{

	private static final long serialVersionUID = 1986996569563065514L;
	/**
	 * Identificador de la tabla en la pagina html
	 */
	private static final String TABLE_NAME = "tablaColegiados";
	private static final Integer DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_SORT_COLUMN = "3";
	
	private String tipoBusqueda;	
	
	//Campos de la busqueda simple
	private List<InstitucionVO> instituciones;
	private String idInstitucion;
	private String nombreInstitucion;

	private String nColegiado;
	private String apellidos1;
	private String apellidos2;
	private String nif;
	private String nombre;
	private boolean busquedaExacta;

	//Campos de la busqueda avanzada
	private String factFechaAltaHasta;
	private String factFechaAltaDesde;
	private String fechaNacimientoDesde;
	private String fechaNacimientoHasta;
	private String codigoPostal;
	private String fax;
	private String tipoApunteCV;
	private List<ValueKeyVO> listaTipoApunteCV;
	private String tipoApunteCVSubtipo1;
	private List<ValueKeyVO> listaTipoApunteCVSubtipo1 = new ArrayList<ValueKeyVO>();
	private String tipoApunteCVSubtipo2;
	private List<ValueKeyVO> listaTipoApunteCVSubtipo2 = new ArrayList<ValueKeyVO>();
	private String domicilio;
	private String sexo;
	private String correoElectronico;
	private String telefono;
	private String movil;
	private String residente;
	private String ejerciente;
	private String comunitario;
	private String fechaIncorporacionDesde;
	private String fechaIncorporacionHasta;
	private String fechaEstadoColegial;
	private String gruposFijos;
	private List<ValueKeyVO> listaGruposFijos;
	private String tipoColegiacion;
	private List<ValueKeyVO> listaTipoColegiacion;
	
	public BusquedaColegiadosForm(){
		setPageSize(DEFAULT_PAGE_SIZE);
		//mapeo de las columnas por las cuales se puede ordenar la lista de resultados
		setColumnTranslation("","","nifCif","nColegiado","apellidos","nombre","fechaIncorporacion",
				"idInstitucion","","","fechaNacimiento");
		//id de la tabla en la pagina html
		setTableName(TABLE_NAME);
	}
	
	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public void updateRequest (HttpServletRequest request){
		setSelectedElements(request.getParameterValues(getSelectParameterName()));
	}
	
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}
	
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}
	
	public String getIdInstitucion() {
		return idInstitucion;
	}
	
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public List<InstitucionVO> getInstituciones() {
		return instituciones;
	}

	public void setInstituciones(List<InstitucionVO> instituciones) {
		this.instituciones = instituciones;
	}
	
	public String getnColegiado() {
		return nColegiado;
	}

	public void setnColegiado(String nColegiado) {
		this.nColegiado = nColegiado;
	}

	public String getApellidos1() {
		return apellidos1;
	}

	public void setApellidos1(String apellido1) {
		this.apellidos1 = apellido1;
	}

	public String getApellidos2() {
		return apellidos2;
	}

	public void setApellidos2(String apellido2) {
		this.apellidos2 = apellido2;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}

	public boolean isBusquedaExacta() {
		return busquedaExacta;
	}

	public void setBusquedaExacta(boolean busquedaExacta) {
		this.busquedaExacta = busquedaExacta;
	}
	

	public String getFactFechaAltaHasta() {
		return factFechaAltaHasta;
	}

	public void setFactFechaAltaHasta(String factFechaAltaHasta) {
		this.factFechaAltaHasta = factFechaAltaHasta;
	}

	public String getFactFechaAltaDesde() {
		return factFechaAltaDesde;
	}

	public void setFactFechaAltaDesde(String factFechaAltaDesde) {
		this.factFechaAltaDesde = factFechaAltaDesde;
	}

	public String getFechaNacimientoDesde() {
		return fechaNacimientoDesde;
	}

	public void setFechaNacimientoDesde(String fechaNacimientoDesde) {
		this.fechaNacimientoDesde = fechaNacimientoDesde;
	}

	public String getFechaNacimientoHasta() {
		return fechaNacimientoHasta;
	}

	public void setFechaNacimientoHasta(String fechaNacimientoHasta) {
		this.fechaNacimientoHasta = fechaNacimientoHasta;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTipoApunteCV() {
		return tipoApunteCV;
	}

	public void setTipoApunteCV(String tipoApunteCV) {
		this.tipoApunteCV = tipoApunteCV;
	}

	public String getTipoApunteCVSubtipo1() {
		return tipoApunteCVSubtipo1;
	}

	public void setTipoApunteCVSubtipo1(String tipoApunteCVSubtipo1) {
		this.tipoApunteCVSubtipo1 = tipoApunteCVSubtipo1;
	}

	public String getTipoApunteCVSubtipo2() {
		return tipoApunteCVSubtipo2;
	}

	public void setTipoApunteCVSubtipo2(String tipoApunteCVSubtipo2) {
		this.tipoApunteCVSubtipo2 = tipoApunteCVSubtipo2;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMovil() {
		return movil;
	}
	public String getTelefonoFijoMovil() {
		String telefonoFijoMovil = getMovil();
		if (telefonoFijoMovil!=null){
			if (getTelefono()!=null){
				telefonoFijoMovil = getTelefono() + " - " + telefonoFijoMovil;
			}
		}
		return telefonoFijoMovil;
	}
	
	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getResidente() {
		return residente;
	}

	public void setResidente(String residente) {
		this.residente = residente;
	}

	public String getEjerciente() {
		return ejerciente;
	}

	public void setEjerciente(String ejerciente) {
		this.ejerciente = ejerciente;
	}

	public String getComunitario() {
		return comunitario;
	}

	public void setComunitario(String comunitario) {
		this.comunitario = comunitario;
	}

	public String getFechaIncorporacionDesde() {
		return fechaIncorporacionDesde;
	}

	public void setFechaIncorporacionDesde(String fechaIncorporacionDesde) {
		this.fechaIncorporacionDesde = fechaIncorporacionDesde;
	}

	public String getFechaIncorporacionHasta() {
		return fechaIncorporacionHasta;
	}

	public void setFechaIncorporacionHasta(String fechaIncorporacionHasta) {
		this.fechaIncorporacionHasta = fechaIncorporacionHasta;
	}

	public String getFechaEstadoColegial() {
		return fechaEstadoColegial;
	}

	public void setFechaEstadoColegial(String fechaEstadoColegial) {
		this.fechaEstadoColegial = fechaEstadoColegial;
	}

	public String getGruposFijos() {
		return gruposFijos;
	}

	public void setGruposFijos(String gruposFijos) {
		this.gruposFijos = gruposFijos;
	}

	public List<ValueKeyVO> getListaGruposFijos() {
		return listaGruposFijos;
	}

	public void setListaGruposFijos(List<ValueKeyVO> listaGruposFijos) {
		this.listaGruposFijos = listaGruposFijos;
	}

	public String getTipoColegiacion() {
		return tipoColegiacion;
	}
	
	public void setTipoColegiacion(String tipoColegiacion) {
		this.tipoColegiacion = tipoColegiacion;
	}
	
	public List<ValueKeyVO> getListaTipoColegiacion() {
		return listaTipoColegiacion;
	}
	
	public void setListaTipoColegiacion(List<ValueKeyVO> listaTipoColegiacion) {
		this.listaTipoColegiacion = listaTipoColegiacion;
	}

	public List<ValueKeyVO> getListaTipoApunteCV() {
		return listaTipoApunteCV;
	}

	public void setListaTipoApunteCV(List<ValueKeyVO> listaTipoApunteCV) {
		this.listaTipoApunteCV = listaTipoApunteCV;
	}

	public List<ValueKeyVO> getListaTipoApunteCVSubtipo1() {
		return listaTipoApunteCVSubtipo1;
	}

	public void setListaTipoApunteCVSubtipo1(
			List<ValueKeyVO> listaTipoApunteCVSubtipo1) {
		this.listaTipoApunteCVSubtipo1 = listaTipoApunteCVSubtipo1;
	}

	public List<ValueKeyVO> getListaTipoApunteCVSubtipo2() {
		return listaTipoApunteCVSubtipo2;
	}

	public void setListaTipoApunteCVSubtipo2(
			List<ValueKeyVO> listaTipoApunteCVSubtipo2) {
		this.listaTipoApunteCVSubtipo2 = listaTipoApunteCVSubtipo2;
	}

	public Vo toVO(){
		ColegiadoVO vo = new ColegiadoVO();
		vo.setIdInstitucion(idInstitucion);
		vo.setNombre(nombre);
		vo.setApellidos1(apellidos1);
		vo.setApellidos2(apellidos2);
		vo.setNif(nif);
		vo.setnColegiado(nColegiado);
		vo.setBusquedaExacta(busquedaExacta);
		
		vo.setCodigoPostal(codigoPostal);
		vo.setComunitario(comunitario);
		vo.setCorreoElectronico(correoElectronico);
		vo.setDomicilio(domicilio);
		vo.setEjerciente(ejerciente);
		vo.setFactFechaAltaDesde(UtilidadesFecha.getDate(factFechaAltaDesde,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFactFechaAltaHasta(UtilidadesFecha.getDate(factFechaAltaHasta,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFax(fax);
		vo.setFechaIncorporacionDesde(UtilidadesFecha.getDate(fechaIncorporacionDesde,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFechaIncorporacionHasta(UtilidadesFecha.getDate(fechaIncorporacionHasta,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFechaNacimientoDesde(UtilidadesFecha.getDate(fechaNacimientoDesde,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFechaNacimientoHasta(UtilidadesFecha.getDate(fechaNacimientoHasta,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setFechaEstadoColegial(UtilidadesFecha.getDate(fechaEstadoColegial,UtilidadesFecha.FORMATO_FECHA_ES,null));
		vo.setResidente(residente);
		vo.setSexo(sexo);
		vo.setTelefono(telefono);
		if (gruposFijos!=null && !"".equals(gruposFijos)){
			String[] sGruposFijos= gruposFijos.split(Vo.PK_SEPARATOR);
			vo.setGruposFijosIdGrupo((UtilidadesNumero.parseInt(sGruposFijos[0])));
			vo.setGruposFijosIdInstitucion((UtilidadesNumero.parseInt(sGruposFijos[1])));
		}
		vo.setTipoApunteCV(UtilidadesNumero.parseInt(tipoApunteCV));
		if (tipoApunteCVSubtipo1!=null && !"".equals(tipoApunteCVSubtipo1)){
			String[] sTipoApunteCVSubtipo1 = tipoApunteCVSubtipo1.split(Vo.PK_SEPARATOR);
			vo.setIdTipoApunteCVSubtipo1((UtilidadesNumero.parseInt(sTipoApunteCVSubtipo1[0])));
			vo.setTipoApunteCVSubtipo1IdInstitucion((UtilidadesNumero.parseInt(sTipoApunteCVSubtipo1[1])));
		}
		if (tipoApunteCVSubtipo2!=null && !"".equals(tipoApunteCVSubtipo2)){
			String[] sTipoApunteCVSubtipo2 = tipoApunteCVSubtipo2.split(Vo.PK_SEPARATOR);
			vo.setIdTipoApunteCVSubtipo2((UtilidadesNumero.parseInt(sTipoApunteCVSubtipo2[0])));
			vo.setTipoApunteCVSubtipo2IdInstitucion((UtilidadesNumero.parseInt(sTipoApunteCVSubtipo2[1])));
		}
		vo.setEstadoColegial(UtilidadesNumero.parseInt(tipoColegiacion));
		return  vo;
	}

	public void fromVO(Vo vo) {
		ColegiadoVO colegiado = (ColegiadoVO) vo;
		setIdInstitucion(colegiado.getIdInstitucion());
		setNombre(colegiado.getNombre());
		setApellidos1(colegiado.getApellidos1());
		setApellidos2(colegiado.getApellidos2());
		setNif(colegiado.getNif());
		setnColegiado(colegiado.getnColegiado());

		setCodigoPostal(colegiado.getCodigoPostal());
		setComunitario(colegiado.getComunitario());
		setCorreoElectronico(colegiado.getCorreoElectronico());
		setDomicilio(colegiado.getDomicilio());
		setEjerciente(colegiado.getEjerciente());
		setFactFechaAltaDesde(UtilidadesString.formatoFecha(colegiado.getFactFechaAltaDesde(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFactFechaAltaHasta(UtilidadesString.formatoFecha(colegiado.getFactFechaAltaHasta(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFax(colegiado.getFax());
		setFechaIncorporacionDesde(UtilidadesString.formatoFecha(colegiado.getFechaIncorporacionDesde(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFechaIncorporacionHasta(UtilidadesString.formatoFecha(colegiado.getFechaIncorporacionHasta(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFechaNacimientoDesde(UtilidadesString.formatoFecha(colegiado.getFechaNacimientoDesde(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFechaNacimientoHasta(UtilidadesString.formatoFecha(colegiado.getFechaNacimientoHasta(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setFechaEstadoColegial(UtilidadesString.formatoFecha(colegiado.getFechaEstadoColegial(),UtilidadesFecha.FORMATO_FECHA_ES,null));
		setResidente(colegiado.getResidente());
		setSexo(colegiado.getSexo());
		setTelefono(colegiado.getTelefono());
		setGruposFijos(
				String.valueOf(colegiado.getGruposFijosIdGrupo()) + 
				Vo.PK_SEPARATOR + 
				String.valueOf(colegiado.getGruposFijosIdInstitucion()));
		setTipoApunteCV(String.valueOf(colegiado.getTipoApunteCV()));
		setTipoApunteCVSubtipo1(
				String.valueOf(colegiado.getIdTipoApunteCVSubtipo1()) + 
				Vo.PK_SEPARATOR + 
				String.valueOf(colegiado.getTipoApunteCVSubtipo1IdInstitucion()));
		setTipoApunteCVSubtipo2(
				String.valueOf(colegiado.getIdTipoApunteCVSubtipo2()) + 
				Vo.PK_SEPARATOR + 
				String.valueOf(colegiado.getTipoApunteCVSubtipo2IdInstitucion()));
		setTipoColegiacion(String.valueOf(colegiado.getEstadoColegial()));
	}
	
	@Override
	public String getDefaultSortColumn() {
		return DEFAULT_SORT_COLUMN;
	}

	public DisplaytagColumnDecorator getDecorator(String checkboxName){
		CheckboxDecorator decorator = (CheckboxDecorator) super.getDecorator(checkboxName);
		List<String> disabled = new ArrayList<String>();
		for (Vo vo: getTable()){
			ColegiadoVO colegiado = (ColegiadoVO) vo;
			if (UtilidadesString.stringToBoolean(colegiado.getNoAparecerRedAbogacia()))
				disabled.add(colegiado.getId());
		}
		decorator.setDisabled(disabled);
		return decorator;
	}

}
