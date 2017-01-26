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
  PROCEDURE PROC_FCS_HISTORICO_HITOFACT(P_IDINSTITUCION IN NUMBER,
                                            P_IDFACTURACION IN NUMBER,
                                            P_IDTURNO       IN NUMBER,
                                            P_IDGUARDIA     IN NUMBER,
                                            P_IDHITO        IN NUMBER,
                                            P_CODRETORNO    OUT VARCHAR2,
                                            P_DATOSERROR    OUT VARCHAR2);
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
                                            
  /****************************************************************************************************************
    Nombre: PROC_FCS_HISTORICO_HITOFACT
    Descripcion: Procedimiento realiza el historico de los hitos de las guardias de un grupo de facturacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER    
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_IDHITO - IN - Identificador del hito - NUMBER
    - P_IDGRUPOFACTURACION - Identificador del grupo de facturacion - NUMBER    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/      
    PROCEDURE PROC_FCS_HISTO_HITOFACT_GUA(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDGRUPOFACTURACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);
                                            

end PKG_SIGA_FCS_HISTORICO;
/
CREATE OR REPLACE package body PKG_SIGA_FCS_HISTORICO is

    PROCEDURE PROC_FCS_HISTORICO_HITOFACT(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDTURNO       IN NUMBER,
        P_IDGUARDIA     IN NUMBER,
        P_IDHITO        IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN 
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
        P_DATOSERROR    OUT VARCHAR2
    ) IS

    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS: Realiza insercion en FCS_HISTORICO_TIPOASISTCOLEGIO';
        INSERT INTO FCS_HISTORICO_TIPOASISTCOLEGIO (
                IDINSTITUCION, IDTIPOASISTENCIACOLEGIO, IDFACTURACION, 
                DESCRIPCION, FECHACREACION, FECHAMODIFICACION, 
                USUMODIFICACION, IMPORTE, IMPORTEMAXIMO) 
            SELECT P_IDINSTITUCION, -- IDINSTITUCION 
                IDTIPOASISTENCIACOLEGIO, -- IDTIPOASISTENCIACOLEGIO
                P_IDFACTURACION, -- IDFACTURACION
                DESCRIPCION, -- DESCRIPCION 
                SYSDATE, -- FECHACREACION 
                FECHAMODIFICACION, -- FECHAMODIFICACION
                USUMODIFICACION, -- USUMODIFICACION 
                IMPORTE, -- IMPORTE 
                IMPORTEMAXIMO -- IMPORTEMAXIMO
            FROM SCS_TIPOASISTENCIACOLEGIO
            WHERE IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS: Realiza insercion en FCS_HISTORICO_TIPOACTUACION';
        INSERT INTO FCS_HISTORICO_TIPOACTUACION (
                IDINSTITUCION, IDTIPOASISTENCIA, IDTIPOACTUACION, 
                IDFACTURACION, DESCRIPCION, IMPORTE, 
                FECHACREACION, FECHAMODIFICACION, IMPORTEMAXIMO, 
                USUMODIFICACION) 
            SELECT P_IDINSTITUCION, -- IDINSTITUCION
                IDTIPOASISTENCIA, -- IDTIPOASISTENCIA
                IDTIPOACTUACION, -- IDTIPOACTUACION
                P_IDFACTURACION, -- IDFACTURACION
                DESCRIPCION, -- DESCRIPCION
                IMPORTE, -- IMPORTE
                SYSDATE, -- FECHACREACION
                FECHAMODIFICACION, -- FECHAMODIFICACION
                IMPORTEMAXIMO, -- IMPORTEMAXIMO
                USUMODIFICACION -- USUMODIFICACION
            FROM SCS_TIPOACTUACION
            WHERE IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS: Realiza insercion en FCS_HISTO_TIPOACTCOSTEFIJO';
        INSERT INTO FCS_HISTO_TIPOACTCOSTEFIJO (
                IDINSTITUCION, IDTIPOASISTENCIA, IDTIPOACTUACION, 
                IDCOSTEFIJO, IDFACTURACION, IMPORTE, 
                FECHACREACION, FECHAMODIFICACION, USUMODIFICACION) 
            SELECT P_IDINSTITUCION, -- IDINSTITUCION
                IDTIPOASISTENCIA, -- IDTIPOASISTENCIA
                IDTIPOACTUACION, -- IDTIPOACTUACION
                IDCOSTEFIJO, -- IDCOSTEFIJO
                P_IDFACTURACION, -- IDFACTURACION
                IMPORTE, -- IMPORTE
                SYSDATE,  -- FECHACREACION
                FECHAMODIFICACION, -- FECHAMODIFICACION
                USUMODIFICACION -- USUMODIFICACION
            FROM SCS_TIPOACTUACIONCOSTEFIJO
            WHERE IDINSTITUCION = P_IDINSTITUCION;

        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTORICOS_GUARDIAS: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
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
  
  /****************************************************************************************************************
    Nombre: PROC_FCS_HISTO_HITOFACT_GUA
    Descripcion: Procedimiento realiza el historico de los hitos de las guardias de un grupo de facturacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER    
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_IDGRUPOFACTURACION - Identificador del grupo de facturacion - NUMBER  
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/      
    PROCEDURE PROC_FCS_HISTO_HITOFACT_GUA(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDGRUPOFACTURACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTO_HITOFACT_GUA: Realiza insercion en FCS_HISTORICO_HITOFACT';
        -- JPT: Guarda el historico de la configuracion de todas las guardias del grupo de facturacion para esa facturacion
        INSERT INTO FCS_HISTORICO_HITOFACT (
                IDINSTITUCION, IDFACTURACION, IDTURNO, 
                IDGUARDIA, IDHITO, PRECIOHITO, 
                FECHACREACION, FECHAMODIFICACION, USUMODIFICACION, 
                DIASAPLICABLES, AGRUPAR) 
            SELECT 
                HFG.IDINSTITUCION, -- IDINSTITUCION
                P_IDFACTURACION, -- IDFACTURACION                
                HFG.IDTURNO, -- IDTURNO
                HFG.IDGUARDIA, -- IDGUARDIA
                HFG.IDHITO, -- IDHITO
                HFG.PRECIOHITO, -- PRECIOHITO 
                SYSDATE, -- FECHACREACION
                HFG.FECHAMODIFICACION, -- FECHAMODIFICACION
                HFG.USUMODIFICACION, -- USUMODIFICACION
                HFG.DIASAPLICABLES, -- DIASAPLICABLES
                HFG.AGRUPAR -- AGRUPAR
            FROM SCS_HITOFACTURABLEGUARDIA HFG, SCS_TURNO TUR, SCS_GUARDIASTURNO GUA
            WHERE TUR.IDINSTITUCION = P_IDINSTITUCION
                AND TUR.IDGRUPOFACTURACION = P_IDGRUPOFACTURACION
                AND GUA.IDINSTITUCION = TUR.IDINSTITUCION
                AND GUA.IDTURNO = TUR.IDTURNO
                AND HFG.IDINSTITUCION = GUA.IDINSTITUCION
                AND HFG.IDTURNO = GUA.IDTURNO
                AND HFG.IDGUARDIA = GUA.IDGUARDIA
                AND HFG.IDHITO NOT IN (HITO_EJG, HITO_SOJ);                                                          

        P_DATOSERROR := 'PROCEDURE PROC_FCS_HISTO_HITOFACT_GUA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_HISTO_HITOFACT_GUA;  
end PKG_SIGA_FCS_HISTORICO;
/
