package org.convert.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Transactions {
    private String fecha;
    private String cuenta;
    private String referenciaBancaria;
    private String descripcion;
    private String monto;
    private String marcaDebitoCredito;
    private String saldoInicial;
    private String detalleTransaccion;
    private String informacionTransaccion;

    public Transactions(){

    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                fecha, "Banco", cuenta, referenciaBancaria, descripcion, monto,
                marcaDebitoCredito, saldoInicial, detalleTransaccion, informacionTransaccion);
    }
}
