Carpeta con script SIGA_126_002, aunque ahora es para la 126_003, porque hubo una entrega urgente anteriormente.

-- Ejecutados en Integracion por AAG el 17/07/2017 a las 11:35

UPDATE ECOM_CEN_EX_COLUMN C SET C.NORMALIZACION = 'DOÑA.\s|D\.\s|Dº\s|D\u00aa\s|\p{Punct}|^(\u00A0)|(\u00A0)$' WHERE C.NOM_COL = 'NOMBRE';
UPDATE ECOM_CEN_EX_COLUMN C SET C.NORMALIZACION = '[[\p{Punct}]&&[-]&&[.]]|^(\u00A0)|(\u00A0)$' WHERE C.NOM_COL IN ('APELLIDO1', 'APELLIDO2');

-- Ejecutados en Integracion por AAG el 17/07/2017 a las 15:35


f_Siga_Asuntoasociado_MV

Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set descripcion = 'Aplicat en Pagament' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago#EU' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago#GL' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 4;

