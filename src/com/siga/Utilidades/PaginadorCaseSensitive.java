/*
 * Created on 28-mar-2005
 *
 */
package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

 

/**
 * @author Ana.Combarros
 * 
 */

/**
 * Paginador  Sensitivo a mayusculas/minusculas y acentos. 
 * @author Ana.Combarros
 *
 *  
 */
@Deprecated
public class PaginadorCaseSensitive extends PaginadorSQL 
{
	 
	public PaginadorCaseSensitive (String query) throws ClsExceptions{
		
		try {
		 
			inicializar(query);
			
			//Inicializo la query
			String columnas = query.substring(6,query.toUpperCase().indexOf("FROM"));
			
			/*this.query = "SELECT "+columnas+" FROM (SELECT a.*, rownum r FROM ("+
						 query+") a WHERE rownum<=rowmax) WHERE r>=rowmin";*/
			
			this.query = "SELECT  /*+ no_merge */ * FROM (SELECT /*+ no_merge */ a.*, rownum r FROM ("+
			 query+") a WHERE rownum<=rowmax) WHERE r>=rowmin";
			
			
			
		} catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	}
	public PaginadorCaseSensitive(String query1, String query2) throws ClsExceptions{
		
		try {
			
			inicializar(query2);
			
			//Inicializo la query
			String columnas = query1.substring(6,query1.toUpperCase().indexOf("FROM"));
			
			/*this.query = "SELECT "+columnas+" FROM (SELECT a.*, rownum r FROM ("+
						 query+") a WHERE rownum<=rowmax) WHERE r>=rowmin";*/
			
			this.query = "SELECT * FROM (SELECT a.*, rownum r FROM ("+
			 query1+") a WHERE rownum<=rowmax) WHERE r>=rowmin";
			
			
			
		} catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	}
	public PaginadorCaseSensitive(String query, String[] cabeceras) throws ClsExceptions{
		
		try {
			inicializar(query);
			this.cabeceras = cabeceras;
			String columnas="";
			
			//Inicializo la query
			for (int i=0;i<cabeceras.length;i++){
				columnas += "\""+cabeceras[i]+"\""+",";
			}
			columnas = columnas.substring(0,columnas.length()-1);
			
			this.query = "SELECT "+columnas+" FROM (SELECT a.*, rownum r FROM ("+
						 query+") a WHERE rownum<=rowmax) WHERE r>=rowmin";
			
		} catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	}
	
	private Vector obtenerPaginaCache(int pagina, boolean ultimaPag){
		
		Vector retorno=new Vector();
		
		if (ultimaPag){
			int resto = numeroTotalRegistros%numeroRegistrosPagina;
			resto = resto>0?resto:numeroRegistrosPagina;
			for (int i=0;i<resto;i++){
				retorno.add(cache.get(numeroRegistrosPagina*(pagina-1)+i));
			}
		}else{
			for (int i=0;(i<numeroRegistrosPagina);i++){
				retorno.add(cache.get(numeroRegistrosPagina*(pagina-1)+i));
			}
		}
		
		return retorno;
		
	} 
	
	private void inicializar(String query) throws ClsExceptions{
			
		try {
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties properties=new ReadProperties("SIGA.properties");
			String numRegistros = properties.returnProperty("paginador.registrosPorPagina",true);
			this.numeroRegistrosPagina = numRegistros!=null? Integer.parseInt(numRegistros):NUM_REGISTROS;
			String pagCache = properties.returnProperty("paginador.distanciaPaginasCache",true);
			this.distanciaPagCache = pagCache!=null? Integer.parseInt(pagCache):DISTANCIA_PAGINAS_CACHE;
			this.queryOriginal = query;
///////////////////////////////////////////////////////////////////////////////////////////////////
			//Viendo que el order by consume recursos y que no afecta en nada
			//para el count lo quitamos
			int endIndex = queryOriginal.toLowerCase().lastIndexOf("order by");
			String queryCountSinOrder ;
			if(endIndex!=-1)
				queryCountSinOrder = queryOriginal.substring(0,endIndex);
			else
				queryCountSinOrder = queryOriginal;

			// Inicializar numeroTotalRegistros y ultimaPagina
			RowsContainer rc = new RowsContainer();
			ArrayList queryList= UtilidadesBDAdm.quitaCamposQuery(new String(queryCountSinOrder),null);
			String queryCountSinOrderSinCampos = (String)queryList.get(0);
			
			String count = null;
			//Ha fallado el quitar los ordenes a los campos
			count = "SELECT  /*+ no_merge */ COUNT(*) AS NUMREGISTROS FROM (" + queryCountSinOrderSinCampos + ")";
			rc.query(count);
			 
///////////////////////////////////////////////////////////////////////////////////////////
			
			
			Row row = (Row)rc.get(0);
			
			this.numeroTotalRegistros=Integer.parseInt(row.getString("NUMREGISTROS"));
			
			if (this.numeroTotalRegistros>0){
				if ((this.numeroTotalRegistros/this.numeroRegistrosPagina)>0){
					int paginaDePico = (this.numeroTotalRegistros%this.numeroRegistrosPagina)>0?1:0;
					this.ultimaPagina=(this.numeroTotalRegistros/this.numeroRegistrosPagina)+paginaDePico;
				}else{
					this.ultimaPagina=1;
				}
			}else{
				this.ultimaPagina=0;
				
			}
		} catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	}
	

	public Vector obtenerPagina(int pagina) throws ClsExceptions{
		
		Vector retorno=null;
		try{
			
			// Comprobamos si está en Caché
			if(pagina>=this.paginaInicialCache&&pagina<=this.paginaFinalCache){
				retorno=obtenerPaginaCache(pagina-this.paginaInicialCache+1,pagina==ultimaPagina);
				paginaActual=pagina;
			}else{
				int rowmax=(pagina+DISTANCIA_PAGINAS_CACHE)*this.numeroRegistrosPagina;
				int rowmin=(pagina-DISTANCIA_PAGINAS_CACHE-1)*this.numeroRegistrosPagina+1;
				
				if(rowmin<0){
					rowmin=1;
					this.paginaInicialCache=1;
				}else{				
					this.paginaInicialCache=pagina-DISTANCIA_PAGINAS_CACHE;
				}
				
				if (rowmax>numeroTotalRegistros){
					rowmax=numeroTotalRegistros;
					this.paginaFinalCache=ultimaPagina;
				}else{
					this.paginaFinalCache=pagina+DISTANCIA_PAGINAS_CACHE;
				}
				
				//hago la select para actualizar la caché
				setCache(rowmin,rowmax);
				retorno = obtenerPaginaCache(pagina-this.paginaInicialCache+1,pagina==ultimaPagina);
				paginaActual=pagina;
			}
		}catch(Exception e){
			throw new ClsExceptions (e, e.getMessage());
		}
		
		return retorno;
	}
	
 
	private void setCache(int rowmin, int rowmax) throws ClsExceptions{
		
		try {
			String queryTrasSustituir = this.query;
			Pattern pattern = Pattern.compile("rowmin");
	        Matcher matcher = pattern.matcher( queryTrasSustituir );
	        queryTrasSustituir = matcher.replaceAll( String.valueOf(rowmin));
			pattern = Pattern.compile("rowmax");
	        matcher = pattern.matcher( queryTrasSustituir );
	        queryTrasSustituir = matcher.replaceAll( String.valueOf(rowmax));
	        
			RowsContainer rc = new RowsContainer();
			rc.query(queryTrasSustituir);
			if (rc!=null) {
			    this.cache=rc.getAll();
			}
			
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	}

	public String getQueryOriginal () {
		return this.queryOriginal;
	}
}
