package com.siga.Utilidades.paginadores;

import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.siga.general.SIGAException;

public interface IPaginador<E> {
	public static final int NUM_REGISTROS = 100;
	public static final int DISTANCIA_PAGINAS_CACHE = 2;
	
	public Vector<E> obtenerPagina(int pagina) throws ClsExceptions, SIGAException;
	public Vector<E> obtenerSiguientes() throws ClsExceptions, SIGAException;
	public Vector<E> obtenerAnteriores() throws ClsExceptions, SIGAException;

	public int getNumeroPaginas();
	public int getNumeroTotalRegistros();
	public int getNumeroRegistrosPorPagina();
	public int getPaginaActual();

	public String getQueryInicio();
}