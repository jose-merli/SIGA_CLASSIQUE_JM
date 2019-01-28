CREATE OR REPLACE TRIGGER PYS_PRODUCTOSSOLICITADOS_AI
AFTER INSERT
ON pys_productossolicitados
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Declare
  v_Escertificado          Number;
  v_FechaCobro     Date;
Begin

  --Si hay observaciones significa que esta pagado porque contienen el numero de pago
  IF(:New.Observaciones IS NOT NULL) THEN
    IF (Length(:New.Observaciones) > 0) THEN
      SELECT SYSDATE INTO v_FechaCobro FROM DUAL;
    END IF;
  END IF;

  --Si el producto es de Tipo Certificado insertamos un registro en CER_SOLICITUDCERTIFICADOS:
  Select Count(*)
    Into v_Escertificado
    From Pys_Productosinstitucion Pi
   Where Pi.Idinstitucion = :New.Idinstitucion
     And Pi.Idtipoproducto = :New.Idtipoproducto
     And Pi.Idproducto = :New.Idproducto
     And Pi.Idproductoinstitucion = :New.Idproductoinstitucion
     And Pi.Tipocertificado = 'C';

  If (v_Escertificado > 0) Then

    For i In 1 .. :New.Cantidad Loop
      Insert Into Cer_Solicitudcertificados
        (Idinstitucion, Idsolicitud, Fechasolicitud, Idestadosolicitudcertificado,
         Idinstitucion_Sol, Idinstitucionorigen, Idinstituciondestino, Idinstitucioncolegiacion,
         Idpersona_Des, Idpersona_Dir, Iddireccion_Dir, Idtipoenvios, Ppn_Idtipoproducto,
         Ppn_Idproductoinstitucion, Ppn_Idproducto, Idestadocertificado, Fechaestado,
         Fechaemisioncertificado, Fechamodificacion, Usumodificacion, Idpeticionproducto,
         Idmetodosolicitud, Aceptacesionmutualidad, Fechacreacion, Usucreacion, Comentario,
         Fechacobro, Idcensodatos)
      Values
        (:New.Idinstitucion, Seq_Solicitudcertificados.Nextval, :New.Fecharecepcionsolicitud, 1,
         :New.Idinstitucion, :New.Idinstitucionorigen, :New.Idinstitucioncolegiacion,
         :New.Idinstitucioncolegiacion, :New.Idpersona, :New.Idpersona, :New.Iddireccion,
         :New.Idtipoenvios, :New.Idtipoproducto, :New.Idproductoinstitucion, :New.Idproducto, 1,
         Sysdate, Null, :New.Fechamodificacion, :New.Usumodificacion, :New.Idpeticion,
         :New.Metodorecepcionsolicitud, NVL(:New.Aceptacesionmutualidad,'0'), Sysdate, :New.Usumodificacion, :New.Observaciones, 
         v_FechaCobro, :New.Idcensodatos);
    End Loop;

  End If;
End Pys_Productossolicitados_Ai;
/
