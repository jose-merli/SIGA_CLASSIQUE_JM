-- Generado por Oracle SQL Developer Data Modeler 17.3.0.261.1529
--   en:        2018-02-14 12:43:00 CET
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



CREATE TABLE fcs_certificacion (
    idcertificacion       NUMBER(10) NOT NULL,
    idtipocertificacion   NUMBER(1) NOT NULL,
    descripcion           VARCHAR2(100) NOT NULL,
    idjustificacion       NUMBER(10) NOT NULL,
    fechamodificacion     DATE NOT NULL,
    usumodificacion       NUMBER(5) NOT NULL
);

ALTER TABLE fcs_certificacion ADD CONSTRAINT fcs_certificacion_pk PRIMARY KEY ( idcertificacion );

CREATE TABLE fcs_je_cert_estado (
    idcertestado      NUMBER(10) NOT NULL,
    idcertificacion   NUMBER(10) NOT NULL,
    idestado          NUMBER(2) NOT NULL,
    idjeintercambio   NUMBER(10)
);

ALTER TABLE fcs_je_cert_estado ADD CONSTRAINT fcs_je_cert_estado_pk PRIMARY KEY ( idcertestado );

CREATE TABLE fcs_je_dev_estado (
    iddevestado       NUMBER(10) NOT NULL,
    iddevolucion      NUMBER(10) NOT NULL,
    idestado          NUMBER(2) NOT NULL,
    idjeintercambio   NUMBER(10)
);

ALTER TABLE fcs_je_dev_estado ADD CONSTRAINT pk_fcs_je_dev_estado PRIMARY KEY ( iddevestado );

CREATE TABLE fcs_je_devol_mov_vario (
    idjedevmovvario     NUMBER(10) NOT NULL,
    iddevolucion        NUMBER(10) NOT NULL,
    idinstitucion       NUMBER(4) NOT NULL,
    idmovimiento        NUMBER(10) NOT NULL,
    fechamodificacion   DATE NOT NULL,
    usumodificacion     NUMBER(5) NOT NULL
);

ALTER TABLE fcs_je_devol_mov_vario ADD CONSTRAINT fcs_je_devol_mov_vario_pk PRIMARY KEY ( idjedevmovvario );

CREATE TABLE fcs_je_devolucion (
    iddevolucion        NUMBER(10) NOT NULL,
    idjustificacion     NUMBER(10) NOT NULL,
    descripcion         VARCHAR2(100 BYTE) NOT NULL,
    fechamodificacion   DATE NOT NULL,
    usumodificacion     NUMBER(5) NOT NULL
);

CREATE UNIQUE INDEX pk_fcs_je_devolucion ON
    fcs_je_devolucion ( iddevolucion ASC );

ALTER TABLE fcs_je_devolucion ADD CONSTRAINT pk_fcs_je_devolucionv1 PRIMARY KEY ( iddevolucion );

CREATE TABLE fcs_je_just_estado (
    idjustestado        NUMBER(10) NOT NULL,
    idjustificacion     NUMBER(10) NOT NULL,
    idestado            NUMBER(2) NOT NULL,
    idjeintercambio     NUMBER(10),
    fechamodificacion   DATE NOT NULL,
    usumodificacion     NUMBER(5) NOT NULL
);

CREATE UNIQUE INDEX pk_fcs_je_just_estado ON
    fcs_je_just_estado ( idjustestado ASC );

CREATE UNIQUE INDEX uk_fcs_je_just_estado ON
    fcs_je_just_estado ( idjustificacion ASC,
    idestado ASC );

ALTER TABLE fcs_je_just_estado ADD CONSTRAINT pk_fcs_je_just_estado PRIMARY KEY ( idjustestado );

ALTER TABLE fcs_je_just_estado ADD CONSTRAINT uk_fcs_je_just_estado UNIQUE ( idjustificacion,
idestado );

CREATE TABLE fcs_je_justifica_facturacionjg (
    idjustfacturacionjg   NUMBER(10) NOT NULL,
    idjustificacion       NUMBER(10) NOT NULL,
    idinstitucion         NUMBER(4) NOT NULL,
    idfacturacion         NUMBER(5) NOT NULL
);

CREATE UNIQUE INDEX pk_fcs_je_justifica_facturacjg ON
    fcs_je_justifica_facturacionjg ( idjustfacturacionjg ASC );

ALTER TABLE fcs_je_justifica_facturacionjg ADD CONSTRAINT pk_fcs_je_justifica_facturacjg PRIMARY KEY ( idjustfacturacionjg );

CREATE TABLE fcs_je_justificacion (
    idjustificacion     NUMBER(10) NOT NULL,
    idinstitucion       NUMBER(4) NOT NULL,
    descripcion         VARCHAR2(100 BYTE) NOT NULL,
    idperiodo           NUMBER(3) NOT NULL,
    anio                NUMBER(4) NOT NULL,
    fechamodificacion   DATE NOT NULL,
    usumodificacion     NUMBER(5) NOT NULL
);

CREATE UNIQUE INDEX uk_fcs_je_justificacion ON
    fcs_je_justificacion ( idinstitucion ASC,
    idjustificacion ASC );

ALTER TABLE fcs_je_justificacion ADD CONSTRAINT pk_fcs_je_justificacion PRIMARY KEY ( idjustificacion );

CREATE TABLE fcs_je_maestroestados (
    idestado            NUMBER(2) NOT NULL,
    descripcion         VARCHAR2(100 BYTE) NOT NULL,
    codigoext           VARCHAR2(10 BYTE),
    fechamodificacion   DATE NOT NULL,
    usumodificacion     NUMBER(5) NOT NULL
);

CREATE UNIQUE INDEX pk_fcs_je_maestroestados ON
    fcs_je_maestroestados ( idestado ASC );

ALTER TABLE fcs_je_maestroestados ADD CONSTRAINT pk_fcs_je_maestroestados PRIMARY KEY ( idestado );

CREATE TABLE fcs_je_tipocertificacion (
    idtipocertificacion   NUMBER(1) NOT NULL,
    nombre                VARCHAR2(50) NOT NULL
);

ALTER TABLE fcs_je_tipocertificacion ADD CONSTRAINT fcs_je_tipocertificacion_pk PRIMARY KEY ( idtipocertificacion );

CREATE TABLE je_cabecera (
    idjecabecera                NUMBER(10) NOT NULL,
    cod_origen_intercambio      VARCHAR2(50) NOT NULL,
    desc_origen_intercambio     VARCHAR2(80),
    cod_destino_intercambio     VARCHAR2(50) NOT NULL,
    desc_destino_intercambio    VARCHAR2(80),
    identificador_intercambio   NUMBER(10) NOT NULL,
    fecha_intercambio           DATE NOT NULL
);


ALTER TABLE je_cabecera ADD CONSTRAINT pk_je_cabecera PRIMARY KEY ( idjecabecera );

CREATE TABLE je_cert_ica_guardia (
    idjecerticaguardia   NUMBER(10) NOT NULL,
    guardia_inactivo     NUMBER(5) NOT NULL,
    guardia_activo       NUMBER(5) NOT NULL,
    guardia_mes5         NUMBER(5) NOT NULL,
    dispensa_guardia     NUMBER(5) NOT NULL,
    complemento_turno    NUMBER(5) NOT NULL,
    asistencia_organ     NUMBER(5) NOT NULL
);

ALTER TABLE je_cert_ica_guardia ADD CONSTRAINT je_cert_ica_guardia_pk PRIMARY KEY ( idjecerticaguardia );

CREATE TABLE je_certificacion_anexo (
    idjecertificacioncicac   NUMBER(10) NOT NULL,
    idjeintercambio          NUMBER(10) NOT NULL,
    idjetrimestre            NUMBER(10) NOT NULL,
    ica                      VARCHAR2(50),
    origen_datos             VARCHAR2(5),
    importe_devoluciones     NUMBER(15,3)
);

ALTER TABLE je_certificacion_anexo ADD CONSTRAINT je_certificacion_cicacv1_pk PRIMARY KEY ( idjecertificacioncicac );

CREATE TABLE je_certificacion_cicac (
    idjecertificacioncicac       NUMBER(10) NOT NULL,
    idjeintercambio              NUMBER(10) NOT NULL,
    idjetrimestre                NUMBER(10) NOT NULL,
    asuntos_importe              NUMBER(15,3) NOT NULL,
    asuntos_cantidad             NUMBER(5) NOT NULL,
    importe_devoluciones         NUMBER(15,3) NOT NULL,
    importe_interes              NUMBER(15,3) NOT NULL,
    importe_final                NUMBER(15,3) NOT NULL,
    importe_bestreta             NUMBER(15,3) NOT NULL,
    importe_acumulado_anterior   NUMBER(15,3) NOT NULL,
    importe_acumulado_actual     NUMBER(15,3) NOT NULL,
    nombre_pdf                   VARCHAR2(200)
);

ALTER TABLE je_certificacion_cicac ADD CONSTRAINT je_certificacion_icav1_pk PRIMARY KEY ( idjecertificacioncicac );

CREATE TABLE je_certificacion_ica (
    idjecertificacionica      NUMBER(10) NOT NULL,
    idjeintercambio           NUMBER(10) NOT NULL,
    idjetrimestre             NUMBER(10) NOT NULL,
    cod_ica                   VARCHAR2(50) NOT NULL,
    importe_devoluciones      NUMBER(15,3) NOT NULL,
    asuntos_importe           NUMBER(15,3) NOT NULL,
    asuntos_cantidad          NUMBER(5) NOT NULL,
    act_proces_importe        NUMBER(15,3) NOT NULL,
    act_proces_cantidad       NUMBER(5) NOT NULL,
    asis_penal_importe        NUMBER(15,3) NOT NULL,
    asis_penal_cantidad       NUMBER(5) NOT NULL,
    asis_vido_importe         NUMBER(15,3) NOT NULL,
    asis_vido_cantidad        NUMBER(5) NOT NULL,
    asistencias_importe       NUMBER(15,3) NOT NULL,
    asistencias_cantidad      NUMBER(5) NOT NULL,
    idjecerticaguardia_det    NUMBER(10) NOT NULL,
    idjecerticaguardia_vido   NUMBER(10) NOT NULL,
    nombre_pdf                VARCHAR2(200) NOT NULL
);

ALTER TABLE je_certificacion_ica ADD CONSTRAINT je_certificacion_ica_pk PRIMARY KEY ( idjecertificacionica );

CREATE TABLE je_codigoexpediente (
    idcodigoexpediente   NUMBER(10) NOT NULL,
    colegio_expediente   VARCHAR2(50) NOT NULL,
    num_expediente       VARCHAR2(50) NOT NULL,
    anio_expediente      NUMBER(4) NOT NULL
);

ALTER TABLE je_codigoexpediente ADD CONSTRAINT je_codigoexpediente_pk PRIMARY KEY ( idcodigoexpediente );

CREATE TABLE je_designa (
    idjedesigna                 NUMBER(10) NOT NULL,
    ica_colegiado               VARCHAR2(50) NOT NULL,
    num_colegiado_abogado       VARCHAR2(50) NOT NULL,
    nombre_abogado              VARCHAR2(100) NOT NULL,
    primer_apellido_abogado     VARCHAR2(100) NOT NULL,
    segundo_apellido_abogado    VARCHAR2(100),
    asistencia_defensa          VARCHAR2(100),
    turno                       VARCHAR2(100),
    area_partido_judicial       VARCHAR2(100),
    tipo_iniciacion             VARCHAR2(50),
    num_designacion_abogado     NUMBER(6) NOT NULL,
    fecha_designacion_abogado   DATE
);

ALTER TABLE je_designa ADD CONSTRAINT je_designa_pk PRIMARY KEY ( idjedesigna );

CREATE TABLE je_detalle_error (
    idjedetalleerror   NUMBER(10) NOT NULL,
    idjeerror          NUMBER(10) NOT NULL,
    campo_error        VARCHAR2(100) NOT NULL,
    desc_error         VARCHAR2(512) NOT NULL
);

ALTER TABLE je_detalle_error ADD CONSTRAINT je_detalle_error_pk PRIMARY KEY ( idjedetalleerror );

CREATE TABLE je_dev_valerroneo (
    idjedevvalerroneo   NUMBER(10) NOT NULL,
    iddevestado         NUMBER(10) NOT NULL,
    descerrorgeneral    CLOB
);

ALTER TABLE je_dev_valerroneo ADD CONSTRAINT pk_je_dev_valerroneo PRIMARY KEY ( idjedevvalerroneo );

CREATE TABLE je_devolucion (
    idjedevolucion           NUMBER(10) NOT NULL,
    idjeintercambio          NUMBER(10) NOT NULL,
    cod_actuacion            VARCHAR2(50) NOT NULL,
    idcodigoexpediente       NUMBER(10) NOT NULL,
    idjetrimestre            NUMBER(10) NOT NULL,
    idjetrimestre_justific   NUMBER(10) NOT NULL,
    idjedesigna              NUMBER(10) NOT NULL,
    idjejusticiable          NUMBER(10) NOT NULL,
    fecha_actuacion          DATE,
    fecha_devolucion         DATE,
    tipologia                VARCHAR2(10),
    motivo_movimiento        VARCHAR2(512),
    modulo_justificado       VARCHAR2(50) NOT NULL,
    modulo_cambio            VARCHAR2(50) NOT NULL,
    importe                  NUMBER(15,3) NOT NULL
);

ALTER TABLE je_devolucion ADD CONSTRAINT je_justificacionv1_pk PRIMARY KEY ( idjedevolucion );

CREATE TABLE je_error (
    idjeerror                NUMBER(10) NOT NULL,
    idjeintercambioerroneo   NUMBER(10) NOT NULL,
    idcodigoexpediente       NUMBER(10) ,
    cod_actuacion_error      VARCHAR2(50),
    origendades              VARCHAR2(50),
    ica                      VARCHAR2(50),
    modul                    VARCHAR2(100)
);

ALTER TABLE je_error ADD CONSTRAINT je_error_pk PRIMARY KEY ( idjeerror );

CREATE TABLE je_importe_modulo (
    idjeimportemodulo        NUMBER(10) NOT NULL,
    idjecertificacioncicac   NUMBER(10) NOT NULL,
    modulo                   VARCHAR2(200) NOT NULL,
    importe_modulo           NUMBER(15,3) NOT NULL,
    asunto_importe           NUMBER(15,3) NOT NULL,
    asunto_cantidad          NUMBER(5)
);

ALTER TABLE je_importe_modulo ADD CONSTRAINT je_importe_modulo_pk PRIMARY KEY ( idjeimportemodulo );

CREATE TABLE je_intercambio (
    idjeintercambio      NUMBER(10) NOT NULL,
    idinstitucion        NUMBER(4) NOT NULL,
    tipo_intercambio     VARCHAR2(50) NOT NULL,
    idjecabecera_cicac   NUMBER(10),
    idjecabecera_gen     NUMBER(10),
    numero_detalles      NUMBER(5) NOT NULL,
    version              VARCHAR2(50)
);

ALTER TABLE je_intercambio ADD CONSTRAINT je_intercambio_pk PRIMARY KEY ( idjeintercambio );

CREATE TABLE je_intercambioerroneo (
    idjeintercambioerroneo   NUMBER(10) NOT NULL,
    idjeintercambio          NUMBER(10) NOT NULL,
    idjeintercambioorigen    NUMBER(10) NOT NULL,
    descerrorgeneral         VARCHAR2(512)
);

ALTER TABLE je_intercambioerroneo ADD CONSTRAINT je_intercambioerroneo_pk PRIMARY KEY ( idjeintercambioerroneo );

CREATE TABLE je_jus_valerroneo (
    idjejustvalerroneo   NUMBER(10) NOT NULL,
    idjustestado         NUMBER(10) NOT NULL,
    descerrorgenera      CLOB
);

ALTER TABLE je_jus_valerroneo ADD CONSTRAINT pk_je_jus_valerroneo PRIMARY KEY ( idjejustvalerroneo );

CREATE TABLE je_justiciable (
    idjejusticiable                NUMBER NOT NULL,
    cod_tipo_identificacion        VARCHAR2(50) NOT NULL,
    identificacion                 VARCHAR2(50),
    nombre_justiciable             VARCHAR2(100) NOT NULL,
    primer_apellido_justiciable    VARCHAR2(100),
    segundo_apellido_justiciable   VARCHAR2(100),
    sexo_justiciable               VARCHAR2(10)
);

ALTER TABLE je_justiciable ADD CONSTRAINT je_justiciable_pk PRIMARY KEY ( idjejusticiable );
 
CREATE TABLE je_justificacion (
    idjejustificacion               NUMBER(10) NOT NULL,
    idjeintercambio                 NUMBER(10) NOT NULL,
    cod_actuacion                   VARCHAR2(50) NOT NULL,
    idcodigoexpediente              NUMBER(10),
    idjetrimestre                   NUMBER(10) NOT NULL,
    idjedesigna                     NUMBER(10) NOT NULL,
    idjejusticiable                 NUMBER(10),
    fecha_actuacion                 DATE,
    fecha_justificacion_actuacion   DATE,
    festivo                         VARCHAR2(10),
    fuera_plazo                     VARCHAR2(50),
    causa_nueva_designa             VARCHAR2(50),
    tipo_delito                     VARCHAR2(50),
    tipo_procedimiento              VARCHAR2(50),
    centro_deten_org_judicial       VARCHAR2(50),
    modulo                          VARCHAR2(50) NOT NULL,
    importe                         NUMBER(15,3) NOT NULL
);

ALTER TABLE je_justificacion ADD CONSTRAINT je_justificacion_pk PRIMARY KEY ( idjejustificacion );

CREATE TABLE je_trimestre (
    idjetrimestre      NUMBER(10) NOT NULL,
    numero_trimestre   VARCHAR2(10) NOT NULL,
    anio_trimestre     NUMBER(10) NOT NULL
);

ALTER TABLE je_trimestre ADD CONSTRAINT je_trimestre_pk PRIMARY KEY ( idjetrimestre );

ALTER TABLE fcs_je_justificacion
    ADD CONSTRAINT fk_fcs_je_justi_gen_periodo FOREIGN KEY ( idperiodo )
        REFERENCES gen_periodo ( idperiodo );

ALTER TABLE fcs_certificacion
    ADD CONSTRAINT fk_fcscert_fcsjejust FOREIGN KEY ( idjustificacion )
        REFERENCES fcs_je_justificacion ( idjustificacion );

ALTER TABLE fcs_certificacion
    ADD CONSTRAINT fk_fcscert_fcsjetipocert FOREIGN KEY ( idtipocertificacion )
        REFERENCES fcs_je_tipocertificacion ( idtipocertificacion );

ALTER TABLE fcs_je_cert_estado
    ADD CONSTRAINT fk_fcsjecertest_fcscert FOREIGN KEY ( idcertificacion )
        REFERENCES fcs_certificacion ( idcertificacion );

ALTER TABLE fcs_je_cert_estado
    ADD CONSTRAINT fk_fcsjecertest_fcsjemaestest FOREIGN KEY ( idestado )
        REFERENCES fcs_je_maestroestados ( idestado );

ALTER TABLE fcs_je_cert_estado
    ADD CONSTRAINT fk_fcsjecertest_jeinterc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE fcs_je_devolucion
    ADD CONSTRAINT fk_fcsjedev_fcsjejust FOREIGN KEY ( idjustificacion )
        REFERENCES fcs_je_justificacion ( idjustificacion );

ALTER TABLE fcs_je_dev_estado
    ADD CONSTRAINT fk_fcsjedevest_fcsjedev FOREIGN KEY ( iddevolucion )
        REFERENCES fcs_je_devolucion ( iddevolucion );

ALTER TABLE fcs_je_dev_estado
    ADD CONSTRAINT fk_fcsjedevest_fcsjemaestest FOREIGN KEY ( idestado )
        REFERENCES fcs_je_maestroestados ( idestado );

ALTER TABLE fcs_je_dev_estado
    ADD CONSTRAINT fk_fcsjedevest_jeinterc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE fcs_je_devol_mov_vario
    ADD CONSTRAINT fk_fcsjedevmovvar_fcsjedev FOREIGN KEY ( iddevolucion )
        REFERENCES fcs_je_devolucion ( iddevolucion );

ALTER TABLE fcs_je_devol_mov_vario
    ADD CONSTRAINT fk_fcsjedevmovvar_fcsmovvar FOREIGN KEY ( idinstitucion,
    idmovimiento )
        REFERENCES fcs_movimientosvarios ( idinstitucion,
        idmovimiento );

ALTER TABLE fcs_je_just_estado
    ADD CONSTRAINT fk_fcsjejustest_fcsjejust FOREIGN KEY ( idjustificacion )
        REFERENCES fcs_je_justificacion ( idjustificacion );

ALTER TABLE fcs_je_just_estado
    ADD CONSTRAINT fk_fcsjejustest_fcsjemaestest FOREIGN KEY ( idestado )
        REFERENCES fcs_je_maestroestados ( idestado );

ALTER TABLE fcs_je_just_estado
    ADD CONSTRAINT fk_fcsjejustest_jeinterc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE fcs_je_justifica_facturacionjg
    ADD CONSTRAINT fk_fcsjejustfactjg_fcsfactjg FOREIGN KEY ( idinstitucion,
    idfacturacion )
        REFERENCES fcs_facturacionjg ( idinstitucion,
        idfacturacion );

ALTER TABLE fcs_je_justifica_facturacionjg
    ADD CONSTRAINT fk_fcsjejustfactjg_fcsjejust FOREIGN KEY ( idjustificacion )
        REFERENCES fcs_je_justificacion ( idjustificacion );

ALTER TABLE je_dev_valerroneo
    ADD CONSTRAINT fk_je_dev_valerroneo FOREIGN KEY ( iddevestado )
        REFERENCES fcs_je_dev_estado ( iddevestado );

ALTER TABLE je_error
    ADD CONSTRAINT fk_je_error_intercerr FOREIGN KEY ( idjeintercambioerroneo )
        REFERENCES je_intercambioerroneo ( idjeintercambioerroneo );

ALTER TABLE je_jus_valerroneo
    ADD CONSTRAINT fk_je_jus_valerroneo FOREIGN KEY ( idjustestado )
        REFERENCES fcs_je_just_estado ( idjustestado );

ALTER TABLE je_certificacion_cicac
    ADD CONSTRAINT fk_jecertcicac_interc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_certificacion_anexo
    ADD CONSTRAINT fk_jecertcicac_trime FOREIGN KEY ( idjetrimestre )
        REFERENCES je_trimestre ( idjetrimestre );

ALTER TABLE je_certificacion_ica
    ADD CONSTRAINT fk_jecertica_certguadet FOREIGN KEY ( idjecerticaguardia_det )
        REFERENCES je_cert_ica_guardia ( idjecerticaguardia );

ALTER TABLE je_certificacion_ica
    ADD CONSTRAINT fk_jecertica_certguavido FOREIGN KEY ( idjecerticaguardia_vido )
        REFERENCES je_cert_ica_guardia ( idjecerticaguardia );

ALTER TABLE je_certificacion_ica
    ADD CONSTRAINT fk_jecertica_codexp FOREIGN KEY ( idjetrimestre )
        REFERENCES je_trimestre ( idjetrimestre );

ALTER TABLE je_certificacion_ica
    ADD CONSTRAINT fk_jecertica_interc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_certificacion_cicac
    ADD CONSTRAINT fk_jecertica_trime FOREIGN KEY ( idjetrimestre )
        REFERENCES je_trimestre ( idjetrimestre );

ALTER TABLE je_certificacion_anexo
    ADD CONSTRAINT fk_jecertifcicac_int FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_detalle_error
    ADD CONSTRAINT fk_jedeterror_error FOREIGN KEY ( idjeerror )
        REFERENCES je_error ( idjeerror );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_codexp FOREIGN KEY ( idcodigoexpediente )
        REFERENCES je_codigoexpediente ( idcodigoexpediente );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_designa FOREIGN KEY ( idjedesigna )
        REFERENCES je_designa ( idjedesigna );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_interc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_justic FOREIGN KEY ( idjejusticiable )
        REFERENCES je_justiciable ( idjejusticiable );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_trimes FOREIGN KEY ( idjetrimestre )
        REFERENCES je_trimestre ( idjetrimestre );

ALTER TABLE je_devolucion
    ADD CONSTRAINT fk_jedev_trimes_just FOREIGN KEY ( idjetrimestre_justific )
        REFERENCES je_trimestre ( idjetrimestre );

ALTER TABLE je_error
    ADD CONSTRAINT fk_jeerror_codexp FOREIGN KEY ( idcodigoexpediente )
        REFERENCES je_codigoexpediente ( idcodigoexpediente );

ALTER TABLE je_importe_modulo
    ADD CONSTRAINT fk_jeimportemod_certificanex FOREIGN KEY ( idjecertificacioncicac )
        REFERENCES je_certificacion_anexo ( idjecertificacioncicac );

ALTER TABLE je_intercambio
    ADD CONSTRAINT fk_jeinterc_jecabcicac FOREIGN KEY ( idjecabecera_cicac )
        REFERENCES je_cabecera ( idjecabecera );

ALTER TABLE je_intercambio
    ADD CONSTRAINT fk_jeinterc_jecabgen FOREIGN KEY ( idjecabecera_gen )
        REFERENCES je_cabecera ( idjecabecera );

ALTER TABLE je_intercambioerroneo
    ADD CONSTRAINT fk_jeintercerr_interc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_intercambioerroneo
    ADD CONSTRAINT fk_jeintererr_int_origen FOREIGN KEY ( idjeintercambioorigen )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_justificacion
    ADD CONSTRAINT fk_jejust_codexp FOREIGN KEY ( idcodigoexpediente )
        REFERENCES je_codigoexpediente ( idcodigoexpediente );

ALTER TABLE je_justificacion
    ADD CONSTRAINT fk_jejust_designa FOREIGN KEY ( idjedesigna )
        REFERENCES je_designa ( idjedesigna );

ALTER TABLE je_justificacion
    ADD CONSTRAINT fk_jejust_interc FOREIGN KEY ( idjeintercambio )
        REFERENCES je_intercambio ( idjeintercambio );

ALTER TABLE je_justificacion
    ADD CONSTRAINT fk_jejust_justic FOREIGN KEY ( idjejusticiable )
        REFERENCES je_justiciable ( idjejusticiable );

ALTER TABLE je_justificacion
    ADD CONSTRAINT fk_jejust_trimes FOREIGN KEY ( idjetrimestre )
        REFERENCES je_trimestre ( idjetrimestre );

CREATE SEQUENCE seq_fcs_certificacion START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_fcs_je_devol_mov_vario START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_fcs_je_devolucion START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_fcs_je_just_estado START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_fcs_je_justifica_facturaci START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_fcs_je_justificacion START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_cabecera START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_cert_ica_guardia START WITH 1 MINVALUE 1 MAXVALUE 999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_certificacion_anexo START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_certificacion_cicac START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_certificacion_ica START WITH 1 MINVALUE 1 MAXVALUE 999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_codigoexpediente START WITH 1 MINVALUE 1 MAXVALUE 999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_designa START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_detalle_error START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_devolucion START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_error START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_importe_modulo START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_intercambio START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_intercambioerroneo START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE Seq_JE_JUS_VALERRONEO START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_justiciable START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_justificacion START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_trimestre START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            28
-- CREATE INDEX                             6
-- ALTER TABLE                             75
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                         22
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0


CREATE SEQUENCE Seq_FCS_JE_DEV_ESTADO START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;
CREATE SEQUENCE Seq_FCS_JE_CER_ESTADO START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;

CREATE SEQUENCE seq_je_jus_codActuacion START WITH 1 MINVALUE 1 MAXVALUE 999999999999 NOCACHE ORDER;
CREATE SEQUENCE seq_je_cab_idIntercambio START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;
CREATE SEQUENCE seq_je_cab_idIntercambioCA START WITH 1 MINVALUE 1 MAXVALUE 9999999999 NOCACHE ORDER;




