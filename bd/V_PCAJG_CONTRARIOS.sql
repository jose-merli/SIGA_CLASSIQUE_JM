CREATE OR REPLACE VIEW V_PCAJG_CONTRARIOS AS
SELECT CON.IDINSTITUCION -- PARA FILTRAR POR EJG
        , CON.IDTIPOEJG  -- PARA FILTRAR POR EJG
        , CON.ANIO       -- PARA FILTRAR POR EJG
        , CON.NUMERO     -- PARA FILTRAR POR EJG
        , LEN.IDLENGUAJE
        , ER.IDREMESA, VDP.TIPOPERSONA_CDA AS C_C_DP_TIPOPERSONA_CDA
        , VDP.TIPOIDENTIFICACION_CDA AS C_C_DP_TIPOIDENTIFICACION_CDA
        , VDP.TIPOIDENTIFICACION_EJIS AS C_C_DP_TIPOIDENTIFICACION_EJIS
        , VDP.IDENTIFICACION AS C_C_DP_IDENTIFICACION
		, VDP.TIPODOMICILIO_EJIS AS C_C_DD_TIPODOMICILIO_EJIS
        , VDP.TIPOVIA_CDA AS C_C_DD_TIPOVIA_CDA
        , VDP.NOMBREVIA AS C_C_DD_NOMBRE_VIA
        , VDP.NUMERODIRECCION  AS C_C_DD_NUMERO_DIRECCION
        , VDP.ESCALERADIRECCION AS C_C_DD_ESCALERA_DIRECCION
        , VDP.PISODIRECCION AS C_C_DD_PISO_DIRECCION
        , VDP.PUERTADIRECCION AS C_C_DD_PUERTA_DIRECCION
        , VDP.PAIS_CDA AS C_C_DD_PAIS_CDA
        , VDP.PROVINCIA_CDA AS C_C_DD_PROVINCIA_CDA
        , VDP.MUNICIPIO_CDA AS C_C_DD_MUNICIPIO_CDA
		, VDP.MUNICIPIO_EJIS AS C_C_DD_MUNICIPIO_EJIS
        , VDP.SUBCODIGOMUNICIPIO AS C_C_DD_SUBCODIGO_MUNICIPIO
        , VDP.CODIGOPOSTAL AS C_C_DD_CODIGO_POSTAL
        , VDP.NOMBRE AS C_C_DP_NOMBRE
        , VDP.PRIMERAPELLIDO AS C_C_DP_PRIMERAPELLIDO
        , VDP.SEGUNDOAPELLIDO AS C_C_DP_SEGUNDOAPELLIDO
        , VDP.RAZONSOCIAL AS C_C_DP_RAZONSOCIAL
        , VDP.FECHANACIMIENTO AS C_C_DP_FECHANACIMIENTO
        , VDP.NACIONALIDAD_CDA AS C_C_DP_NACIONALIDAD_CDA
        , NULL AS C_C_DP_SITUACIONLABORAL_CDA --NO EXISTE EN SIGA ESTA INFORMACION
        , VDP.PROFESION_CDA AS C_C_DP_PROFESION_CDA
        , VDP.SEXO AS C_C_DP_SEXO
        , VDP.SEXO_EJIS AS C_C_DP_SEXO_EJIS
        , VDP.IDIOMACOMUNICACION AS C_C_DP_IDIOMACOMUNICACION
        , VDP.ESTADOCIVIL_CDA AS C_C_DP_ESTADOCIVIL_CDA
		, VDP.ESTADOCIVIL_EJIS AS C_C_DP_ESTADOCIVIL_EJIS
        , VDP.REGIMENECONOMICO_CDA AS C_C_DP_REGIMENECONOMICO_CDA
        , VDP.REGIMENECONOMICO_EJIS AS C_C_DP_REGIMENECONOMICO_EJIS
        , VDP.NUMHIJOS AS C_C_DP_NUMHIJOS
        , VDP.FECHAFORMALIZACION AS C_C_DP_FECHAFORMALIZACION
        , VDP.TIPOPERSONAJURIDICA_CDA AS C_C_DP_TIPOPERSONAJURIDICA_CDA
        --Datos procurador
        , (SELECT P.CODIGO FROM PCAJG_ICAPROCURADOR P, PCAJG_ICAPROC_CENINSTI PS
                                    WHERE P.IDENTIFICADOR = PS.IDENTIFICADOR
                                    AND P.IDINSTITUCION = PS.IDINSTITUCION_PCAJG
                                    AND PS.IDINSTITUCION_SIGA = PRO.IDINSTITUCION
                                    AND P.IDINSTITUCION = ER.IDINSTITUCION) AS C_C_DP_COLEGIOPROCURADOR--error 20101013: antes se enviaba el idinstitucion
        , PRO.IDCOLPROCURADOR AS C_C_DP_COLPROCURADOR_EJIS
        , PRO.NCOLEGIADO AS C_C_DP_NUMCOLEGIADOPROCURADOR
        , PRO.NOMBRE || ' ' || PRO.APELLIDOS1 || ' ' || PRO.APELLIDOS2 AS C_C_DP_NOMBRECOMPLETOPROCURADO
        , PRO.NOMBRE AS C_C_DP_NOMBRE_PROCURADOR
        , PRO.APELLIDOS1 AS C_C_DP_APELLIDO1_PROCURADOR
        , PRO.APELLIDOS2 AS C_C_DP_APELLIDO2_PROCURADOR
        , NULL AS C_C_DP_NIF_PROCURADOR
        --Datos abogado
        , (SELECT I.CODIGOEXT FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION = CON.IDINSTITUCION AND PER.IDPERSONA IS NOT NULL) AS C_C_DA_COLEGIO_CDA
        , NVL(COL.NCOLEGIADO, COL.NCOMUNITARIO) AS C_C_DA_NUMCOLABO
        , PER.NOMBRE AS C_C_DA_NOMBRE_ABO
        , PER.APELLIDOS1 AS C_C_DA_APELLIDO1_ABO
        , PER.APELLIDOS2 AS C_C_DA_APELLIDO2_ABO
        , PER.NIFCIF AS C_C_DA_NIFCIF_ABO
        --Datos representante
        , VDP_REP.TIPOPERSONA_CDA AS C_R_TIPOPERSONA_CDA
        , VDP_REP.TIPOIDENTIFICACION_CDA AS C_R_TIPOIDENTIFICACION_CDA
        , VDP_REP.TIPOIDENTIFICACION_EJIS AS C_R_TIPOIDENTIFICACION_EJIS
        , VDP_REP.IDENTIFICACION AS C_R_IDENTIFICACION
        , VDP_REP.NOMBRE AS C_R_NOMBRE
        , VDP_REP.PRIMERAPELLIDO AS C_R_PRIMERAPELLIDO
        , VDP_REP.SEGUNDOAPELLIDO AS C_R_SEGUNDOAPELLIDO
        , VDP_REP.RAZONSOCIAL AS C_R_RAZONSOCIAL
        , VDP_REP.FECHANACIMIENTO AS C_R_FECHANACIMIENTO
        , VDP_REP.NACIONALIDAD_CDA AS C_R_NACIONALIDAD_CDA
        , NULL AS C_R_SITUACIONLABORAL_CDA -- NO HAY INFORMACION EN SIGA
        , VDP_REP.PROFESION_CDA AS C_R_PROFESION_CDA
        , VDP_REP.SEXO AS C_R_SEXO
        , VDP_REP.SEXO_EJIS AS C_R_SEXO_EJIS
        , VDP_REP.IDIOMACOMUNICACION AS C_R_IDIOMACOMUNICACION
        , VDP_REP.ESTADOCIVIL_CDA AS C_R_ESTADOCIVIL_CDA
		, VDP_REP.ESTADOCIVIL_EJIS AS C_R_ESTADOCIVIL_EJIS
        , VDP_REP.REGIMENECONOMICO_CDA AS C_R_REGIMENECONOMICO_CDA
        , VDP_REP.REGIMENECONOMICO_EJIS AS C_R_REGIMENECONOMICO_EJIS
        , VDP_REP.NUMHIJOS AS C_R_NUMHIJOS
        , VDP_REP.FECHAFORMALIZACION AS C_R_FECHAFORMALIZACION
        , VDP_REP.TIPOPERSONAJURIDICA_CDA AS C_R_TIPOPERSONAJURIDICA_CDA
        , VDP_REP.TIPODOMICILIO_CDA AS C_R_TIPODOMICILIO_CDA
        , VDP_REP.TIPODOMICILIO_EJIS AS C_R_TIPODOMICILIO_EJIS
        , VDP_REP.TIPOVIA_CDA AS C_R_DD_TIPOVIA_CDA
        , VDP_REP.NOMBREVIA AS C_R_DD_NOMBREVIA
        , VDP_REP.NUMERODIRECCION AS C_R_DD_NUMERODIRECCION
        , VDP_REP.ESCALERADIRECCION AS C_R_DD_ESCALERADIRECCION
        , VDP_REP.PISODIRECCION AS C_R_DD_PISODIRECCION
        , VDP_REP.PUERTADIRECCION AS C_R_DD_PUERTADIRECCION
        , VDP_REP.PAIS_CDA AS C_R_DD_PAIS_CDA
        , VDP_REP.PROVINCIA_CDA AS C_R_DD_PROVINCIA_CDA
        , VDP_REP.MUNICIPIO_CDA AS C_R_DD_M_MUNICIPIO_CDA
		, VDP_REP.MUNICIPIO_EJIS AS C_R_DD_M_MUNICIPIO_EJIS
        , VDP_REP.SUBCODIGOMUNICIPIO AS C_R_DD_M_SUBCODIGOMUNICIPIO
        , VDP_REP.CODIGOPOSTAL AS C_R_DD_CODIGOPOSTAL
        , VDP_REP.NUMEROTELEFONO1 AS C_R_DC_NUMEROTELEFONO1
        , VDP_REP.NUMEROTELEFONO2 AS C_R_DC_NUMEROTELEFONO2
        , VDP_REP.EMAIL AS C_R_DC_EMAIL
FROM CAJG_EJGREMESA ER
     , SCS_CONTRARIOSEJG CON
     , V_PCAJG_M_DPERSONA VDP
     , SCS_PROCURADOR PRO
     , ADM_LENGUAJES LEN
     , CEN_COLEGIADO COL
     , CEN_PERSONA PER
     , V_PCAJG_M_DPERSONA VDP_REP
WHERE ER.IDINSTITUCION = CON.IDINSTITUCION
      AND ER.ANIO = CON.ANIO
      AND ER.NUMERO = CON.NUMERO
      AND ER.IDTIPOEJG = CON.IDTIPOEJG
      AND CON.IDINSTITUCION = VDP.IDINSTITUCION
      AND CON.IDPERSONA = VDP.IDPERSONA
      AND LEN.IDLENGUAJE = VDP.IDLENGUAJE
      AND CON.IDINSTITUCION_PROCU = PRO.IDINSTITUCION(+)
      AND CON.IDPROCURADOR = PRO.IDPROCURADOR(+)
      AND CON.IDINSTITUCION = COL.IDINSTITUCION(+)
      AND CON.IDABOGADOCONTRARIOEJG = COL.IDPERSONA(+)
      AND COL.IDPERSONA = PER.IDPERSONA(+)
      AND CON.IDINSTITUCION = VDP_REP.IDINSTITUCION(+)
      AND CON.IDREPRESENTANTEEJG = VDP_REP.IDPERSONA(+)
      AND (VDP_REP.IDPERSONA IS NULL OR LEN.IDLENGUAJE = VDP_REP.IDLENGUAJE);
 
