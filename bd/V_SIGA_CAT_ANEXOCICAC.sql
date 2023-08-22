CREATE OR REPLACE VIEW V_SIGA_CAT_ANEXOCICAC AS
SELECT
       IDINSTITUCION,
       IDPERIODO||'T'||ANIO  TRIMESTRE,
       IDINSTITUCION CODIGOICA,
       'CICAC' ORIGENDATOS,
       IMPORTE_DEVOLUCIONES IMPORTEDEVOLUCIONES,
       '' CODIGOINCIDENCIA,
        MODULO,
       IMPORTE IMPORTEMODULO,
       IMPORTEMODULO IMPORTE,
       CANTIDAD



  FROM (SELECT
       INT.IDINSTITUCION,
         INT.ANIO,
               INT.IDPERIODO,
               3001 ICA,
               'ICA' ORIGENDATOS,
               SUM(ANE.IMPORTE_DEVOLUCIONES) IMPORTE_DEVOLUCIONES,
               ANE.MODULO,
               SUM(ANE.IMPORTEMODULO) IMPORTEMODULO,
               SUM(ANE.CANTIDAD) CANTIDAD,
               ANE.IMPORTE IMPORTE
          FROM FCS_JE_INTERCAMBIOS INT
         INNER JOIN FCS_JE_CERTIFICACIONANEXO CAN ON CAN.IDINTERCAMBIO =
                                                     INT.IDINTERCAMBIO
         INNER JOIN FCS_JE_CERTANEXO_ESTADO EST ON EST.IDCERTIFICACIONANEXO =
                                                   CAN.IDCERTIFICACIONANEXO
         INNER JOIN JE_CERTIFICACION_ANEXO ANE ON ANE.IDJEINTERCAMBIO =
                                                  EST.IDJEINTERCAMBIO
         WHERE INT.IDESTADO = 95
           AND EST.IDESTADO = 30

         GROUP BY INT.IDINSTITUCION,
                  INT.ANIO,
                  INT.IDPERIODO,
                  ANE.MODULO,
                   ANE.IMPORTE
)