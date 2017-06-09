CREATE OR REPLACE VIEW V_SIGA_SJCS_FACTURACION AS
SELECT FAC.IDINSTITUCION,
       FAC.IDFACTURACION,
       FAC.NOMBRE AS FACTURACION,
       TRUNC(FAC.FECHAMODIFICACION) AS FECHAMODIFICACION,
       TO_CHAR(FAC.IDFACTURACION) || '/' || TO_CHAR(DET.IDPERSONA) AS DOCUMENTO,
       DET.IDPERSONA,
       COL.NCOLEGIADO AS NUMCOL,
       PER.APELLIDOS1,
       PER.APELLIDOS2,
       PER.NOMBRE,
       IMPORTE,
       COL.CUENTACONTABLESJCS
  FROM FCS_FACTURACIONJG FAC
       INNER JOIN (SELECT IDINSTITUCION,
                          IDFACTURACION,
                          IDPERSONA,
                          SUM(PRECIO) AS IMPORTE
                     FROM (SELECT IDINSTITUCION,
                                  IDFACTURACION,
                                  IDPERSONA,
                                  PRECIOAPLICADO + PRECIOCOSTESFIJOS AS PRECIO
                             FROM FCS_FACT_APUNTE
                          UNION ALL
                           SELECT IDINSTITUCION,
                                  IDFACTURACION,
                                  IDPERSONA,
                                  Importefacturado AS PRECIO
                             FROM FCS_FACT_ACTUACIONDESIGNA
                          UNION ALL
                           SELECT IDINSTITUCION,
                                  IDFACTURACION,
                                  IDPERSONA,
                                  PRECIOAPLICADO AS PRECIO
                             FROM FCS_FACT_EJG
                          UNION ALL
                           SELECT IDINSTITUCION,
                                  IDFACTURACION,
                                  IDPERSONA,
                                  PRECIOAPLICADO AS PRECIO
                             FROM FCS_FACT_SOJ)
                    GROUP BY IDINSTITUCION, IDFACTURACION, IDPERSONA) DET
          ON FAC.IDINSTITUCION = DET.IDINSTITUCION
         AND FAC.IDFACTURACION = DET.IDFACTURACION
             INNER JOIN CEN_COLEGIADO COL
                ON DET.IDINSTITUCION = COL.IDINSTITUCION
               AND DET.IDPERSONA     = COL.IDPERSONA
             INNER JOIN CEN_PERSONA PER
                ON DET.IDPERSONA = PER.IDPERSONA

 
