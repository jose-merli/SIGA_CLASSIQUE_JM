package com.siga.test;

/**
 * @author ASD
 *
 * Clase a Eliminar en Produccion
 */
class SIGAThread implements Runnable {
		// This method is called when the thread runs
		public void run() {
			try {
				// 5 segundos
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				com.atos.utils.ClsLogging.writeFileLogError(e.toString(), e, 3);
			}
			com.atos.utils.ClsLogging.writeFileLog("Fin del metodo run", 10);		
		}
	}
