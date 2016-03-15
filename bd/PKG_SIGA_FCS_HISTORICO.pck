CREATE OR REPLACE package PKG_SIGA_FCS_HISTORICO is

  -- Author  : ANAAG
  -- Created : 21/05/2013 16:34:28
  -- Purpose :

  -- Public variable declarations
  E_ERROR2 EXCEPTION;
  V_DATOSERROR2 VARCHAR2(2000) := NULL;
  
    -- Public constant declarations        
    HITO_GAs CONSTANT NUMBER := 1;
    HITO_SOJ CONSTANT NUMBER := 12;
    HITO_EJG CONSTANT NUMBER := 13;
    HITO_GAc CONSTANT NUMBER := 44;
    HITO_GAsMin CONSTANT NUMBER := 53;
    HITO_GAcMin CONSTANT NUMBER := 54;

  -- Public function and procedure declarations
  
  /****************************************************************************************************************
    Nombre: PROC_FCS_HISTORICO_HITOFACT
    Descripcion: Procedimiento para catalanes que carga V_CONFIG_GUARDIA 

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER    
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDHITO - IN - Identificador del hito - NUMBER
    - P_IDGRUPOFACTURACION - Identificador del grupo de facturacion - NUMBER
    - P_PRECIOHITO - IN - Precio usado por el hito - NUMBER    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/      
    PROCEDURE PROC_FCS_HISTORICO_HITOFACT(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDHITO IN NUMBER,
        P_IDGRUPOFACTURACION IN NUMBER,
        P_PRECIOHITO IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);
        
  PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2);
  PROCEDURE PROC_FCS_HISTORICOS_TURNOS(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2);

  PROCEDURE PROC_FCS_HISTO_BORRAR_FACT(P_IDINSTITUCION IN NUMBER,
                                          P_IDFACTURACION IN NUMBER,
                                          P_CODRETORNO    OUT VARCHAR2,
                                          P_DATOSERROR    OUT VARCHAR2);

  PROCEDURE PROC_FCS_HISTO_HITOFACT_REGU(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2);

end PKG_SIGA_FCS_HISTORICO;
/
CREATE OR REPLACE package body PKG_SIGA_FCS_HISTORICO is

  /****************************************************************************************************************
    Nombre: PROC_FCS_HISTORICO_HITOFACT
    Descripcion: Procedimiento para catalanes que carga V_CONFIG_GUARDIA 

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER    
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDHITO - IN - Identificador del hito - NUMBER
    - P_IDGRUPOFACTURACION - Identificador del grupo de facturacion - NUMBER
    - P_PRECIOHITO - IN - Precio usado por el hito - NUMBER    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/      
    PROCEDURE PROC_FCS_HISTORICO_HITOFACT(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDHITO IN NUMBER,
        P_IDGRUPOFACTURACION IN NUMBER,
        P_PRECIOHITO IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        IF (P_IDGRUPOFACTURACION IS NULL) THEN    
            INSERT INTO FCS_HISTORICO_HITOFACT
                SELECT 
                    P_IDFACTURACION, 
                    P_IDINSTITUCION, 
                    IDTURNO, 
                    IDGUARDIA,
                    IDHITO, 
                    PRECIOHITO, 
                    SYSDATE,
                    FECHAMODIFICACION, 
                    USUMODIFICACION, 
                    DIASAPLICABLES,
                    AGRUPAR
                FROM SCS_HITOFACTURABLEGUARDIA
                WHERE IDINSTITUCION = P_IDINSTITUCION 
                    AND IDTURNO = P_IDTURNO 
                    AND IDGUARDIA = P_IDGUARDIA 
                    AND (
                        (
                            DECODE(P_IDHITO, 0, 0, P_IDHITO)=0  
                            AND IDHITO NOT IN (HITO_EJG,HITO_SOJ)
                        ) 
                        OR IDHITO = P_IDHITO
                    );
                    
        ELSE
            -- JPT: Guarda el historico de la configuracion de todas las guardias del grupo de facturacion para esa facturacion
            INSERT INTO FCS_HISTORICO_HITOFACT
                SELECT 
                    P_IDFACTURACION, 
                    HFG.IDINSTITUCION, 
                    HFG.IDTURNO, 
                    HFG.IDGUARDIA,
                    HFG.IDHITO, 
                    HFG.PRECIOHITO, 
                    SYSDATE,
                    HFG.FECHAMODIFICACION, 
                    HFG.USUMODIFICACION, 
                    HFG.DIASAPLICABLES,
                    HFG.AGRUPAR
                FROM SCS_HITOFACTURABLEGUARDIA HFG, SCS_TURNO TUR, SCS_GUARDIASTURNO GUA
                WHERE TUR.IDINSTITUCION = P_IDINSTITUCION
                    AND TUR.IDGRUPOFACTURACION = P_IDGRUPOFACTURACION
                    AND GUA.IDINSTITUCION = TUR.IDINSTITUCION
                    AND GUA.IDTURNO = TUR.IDTURNO
                    AND HFG.IDINSTITUCION = GUA.IDINSTITUCION
                    AND HFG.IDTURNO = GUA.IDTURNO
                    AND HFG.IDGUARDIA = GUA.IDGUARDIA
                    AND HFG.IDHITO NOT IN (HITO_EJG, HITO_SOJ);
                    
            -- JPT: Guardamos el precio de GAsMin y GAcMin        
            IF (P_PRECIOHITO IS NOT NULL) THEN
                -- JPT: Guarda el historico de la configuracion de las guardias inactivas
                INSERT INTO FCS_HISTORICO_HITOFACT
                    SELECT 
                        P_IDFACTURACION, 
                        HFG.IDINSTITUCION, 
                        HFG.IDTURNO, 
                        HFG.IDGUARDIA,
                        DECODE(HFG.IDHITO,HITO_GAs,HITO_GAsMin,HITO_GAc,HITO_GAcMin,NULL), 
                        P_PRECIOHITO, -- Precio puesto en constante que se pasa como parametro 
                        SYSDATE,
                        HFG.FECHAMODIFICACION, 
                        HFG.USUMODIFICACION, 
                        HFG.DIASAPLICABLES,
                        HFG.AGRUPAR
                    FROM SCS_HITOFACTURABLEGUARDIA HFG, SCS_TURNO TUR, SCS_GUARDIASTURNO GUA
                    WHERE TUR.IDINSTITUCION = P_IDINSTITUCION
                        AND TUR.IDGRUPOFACTURACION = P_IDGRUPOFACTURACION
                        AND GUA.IDINSTITUCION = TUR.IDINSTITUCION
                        AND GUA.IDTURNO = TUR.IDTURNO
                        AND HFG.IDINSTITUCION = GUA.IDINSTITUCION
                        AND HFG.IDTURNO = GUA.IDTURNO
                        AND HFG.IDGUARDIA = GUA.IDGUARDIA
                        AND HFG.IDHITO IN (HITO_GAs, HITO_GAc);
            END IF;                        
        END IF;                                    

        --Actualizo para saber que el procedimiento ha finalizado correctamente:
        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICO_HITOFACT: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            when e_error2 then
                P_CODRETORNO := TO_CHAR(-1);
                P_DATOSERROR := V_DATOSERROR2 || ' ' || SQLERRM;
                    
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_HISTORICO_HITOFACT;

    PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        BEGIN
            INSERT INTO FCS_HISTORICO_TIPOASISTCOLEGIO
                SELECT P_IDINSTITUCION, 
                    IDTIPOASISTENCIACOLEGIO, 
                    P_IDFACTURACION,
                    DESCRIPCION, 
                    SYSDATE, 
                    FECHAMODIFICACION, 
                    USUMODIFICACION, 
                    IMPORTE, 
                    IMPORTEMAXIMO
                FROM SCS_TIPOASISTENCIACOLEGIO
                WHERE IDINSTITUCION = P_IDINSTITUCION;

            P_DATOSERROR := 'Ejecutada la insercion en FCS_HISTORICO_TIPOASISTCOLEGIO';

            INSERT INTO FCS_HISTORICO_TIPOACTUACION
                SELECT P_IDINSTITUCION, 
                    IDTIPOASISTENCIA, 
                    IDTIPOACTUACION,
                    P_IDFACTURACION, 
                    DESCRIPCION, 
                    IMPORTE, 
                    SYSDATE,
                    FECHAMODIFICACION, 
                    IMPORTEMAXIMO, 
                    USUMODIFICACION
                FROM SCS_TIPOACTUACION
                WHERE IDINSTITUCION = P_IDINSTITUCION;

            P_DATOSERROR := 'Ejecutada la insercion en FCS_HISTORICO_TIPOACTUACION';

            INSERT INTO FCS_HISTO_TIPOACTCOSTEFIJO
                SELECT P_IDINSTITUCION, 
                    IDTIPOASISTENCIA, 
                    IDTIPOACTUACION, 
                    IDCOSTEFIJO,
                    P_IDFACTURACION, 
                    IMPORTE, 
                    SYSDATE, 
                    FECHAMODIFICACION, 
                    USUMODIFICACION
                FROM SCS_TIPOACTUACIONCOSTEFIJO
                WHERE IDINSTITUCION = P_IDINSTITUCION;

            --Actualizo para saber que el procedimiento ha finalizado correctamente:
            P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS: ha finalizado correctamente.';
            P_CODRETORNO := TO_CHAR(0);

            EXCEPTION
                when e_error2 then
                    P_CODRETORNO := TO_CHAR(-1);
                    P_DATOSERROR := V_DATOSERROR2 || ' ' || SQLERRM;
                    
                WHEN OTHERS THEN
                    P_CODRETORNO := TO_CHAR(SQLCODE);
                    P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_HISTORICOS_GUARDIAS;

  PROCEDURE PROC_FCS_HISTORICOS_TURNOS(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN

      INSERT INTO FCS_HISTORICO_ACREDITACION
        SELECT IDACREDITACION, P_IDFACTURACION, P_IDINSTITUCION,
          DESCRIPCION,  SYSDATE, FECHAMODIFICACION,
          USUMODIFICACION, IDTIPOACREDITACION
        FROM SCS_ACREDITACION;

      P_DATOSERROR := 'Ejecutada la insercion en FCS_HISTORICO_ACREDITACION';

      INSERT INTO FCS_HISTORICO_PROCEDIMIENTOS
        SELECT P_IDINSTITUCION, IDPROCEDIMIENTO, P_IDFACTURACION,
          NOMBRE, SYSDATE, FECHAMODIFICACION, USUMODIFICACION,
          PRECIO, IDJURISDICCION, CODIGO, COMPLEMENTO,
          VIGENTE, ORDEN, CODIGOEXT, PERMITIRANIADIRLETRADO,
          FECHADESDEVIGOR, FECHAHASTAVIGOR
        FROM SCS_PROCEDIMIENTOS
        WHERE IDINSTITUCION = P_IDINSTITUCION;

      P_DATOSERROR := 'Ejecutada la insercion en FCS_HISTORICO_PROCEDIMIENTOS';

      INSERT INTO FCS_HISTO_ACREDITACIONPROC
        SELECT P_IDINSTITUCION, IDPROCEDIMIENTO, IDACREDITACION,
          P_IDFACTURACION, PORCENTAJE, USUMODIFICACION,
          SYSDATE, FECHAMODIFICACION, CODIGOEXT
        FROM SCS_ACREDITACIONPROCEDIMIENTO
        WHERE IDINSTITUCION = P_IDINSTITUCION;

      --Actualizo para saber que el procedimiento ha finalizado correctamente:
      P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_TURNOS: ha finalizado correctamente.';
      P_CODRETORNO := TO_CHAR(0);

    EXCEPTION
      when e_error2 then
        P_CODRETORNO := TO_CHAR(-1);
        P_DATOSERROR := V_DATOSERROR2 || ' ' || SQLERRM;
      WHEN OTHERS THEN
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
  END;

  PROCEDURE PROC_FCS_HISTO_BORRAR_FACT(P_IDINSTITUCION IN NUMBER,
                                          P_IDFACTURACION IN NUMBER,
                                          P_CODRETORNO    OUT VARCHAR2,
                                          P_DATOSERROR    OUT VARCHAR2) IS
      BEGIN

       DELETE FROM FCS_HISTORICO_HITOFACT
           WHERE IDFACTURACION = P_IDFACTURACION
             AND IDINSTITUCION = P_IDINSTITUCION;

       P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTORICO_HITOFACT';

          DELETE FROM FCS_HISTORICO_TIPOASISTCOLEGIO
           WHERE IDFACTURACION = P_IDFACTURACION
             AND IDINSTITUCION = P_IDINSTITUCION;

       P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTORICO_TIPOASISTCOLEGIO';

          DELETE FROM FCS_HISTORICO_TIPOACTUACION
           WHERE IDFACTURACION = P_IDFACTURACION
             AND IDINSTITUCION = P_IDINSTITUCION;

       P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTORICO_TIPOACTUACION';

          DELETE FROM FCS_HISTO_TIPOACTCOSTEFIJO
           WHERE IDFACTURACION = P_IDFACTURACION
             AND IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTO_TIPOACTCOSTEFIJO';

          DELETE FROM FCS_HISTO_ACREDITACIONPROC
          WHERE IDFACTURACION = P_IDFACTURACION
            AND IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTO_ACREDITACIONPROC';

          DELETE FROM FCS_HISTORICO_ACREDITACION
          WHERE IDFACTURACION = P_IDFACTURACION
            AND IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTORICO_ACREDITACION';

          DELETE FROM FCS_HISTORICO_PROCEDIMIENTOS
          WHERE IDFACTURACION = P_IDFACTURACION
            AND IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'Ejecutada el borrado de la factura en FCS_HISTORICO_PROCEDIMIENTOS';

        --Actualizo para saber que el procedimiento ha finalizado correctamente:
        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_TURNOS: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

      EXCEPTION
        when e_error2 then
          P_CODRETORNO := TO_CHAR(-1);
          P_DATOSERROR := V_DATOSERROR2 || ' ' || SQLERRM;
        WHEN OTHERS THEN
          P_CODRETORNO := TO_CHAR(SQLCODE);
          P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
  END;

  PROCEDURE PROC_FCS_HISTO_HITOFACT_REGU(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN

      INSERT INTO FCS_HISTORICO_HITOFACT
             SELECT P_IDFACTURACION, P_IDINSTITUCION, IDTURNO, IDGUARDIA,
                    IDHITO, PRECIOHITO, SYSDATE,FECHAMODIFICACION, USUMODIFICACION, DIASAPLICABLES,
                    AGRUPAR
             FROM SCS_HITOFACTURABLEGUARDIA
             WHERE IDINSTITUCION = P_IDINSTITUCION AND
                   (IDTURNO, IDGUARDIA) IN (SELECT  DISTINCT IDTURNO, IDGUARDIA
                                            FROM FCS_FACT_APUNTE
                                            WHERE IDINSTITUCION = P_IDINSTITUCION
                                                  AND IDFACTURACION = P_IDFACTURACION)
                    AND IDHITO NOT IN (HITO_EJG,HITO_SOJ);

      --Actualizo para saber que el procedimiento ha finalizado correctamente:
      P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTO_HITOFACT_REGU: ha finalizado correctamente.';
      P_CODRETORNO := TO_CHAR(0);

    EXCEPTION
      when e_error2 then
        P_CODRETORNO := TO_CHAR(-1);
        P_DATOSERROR := V_DATOSERROR2 || ' ' || SQLERRM;
      WHEN OTHERS THEN
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
  END;
end PKG_SIGA_FCS_HISTORICO;
/
