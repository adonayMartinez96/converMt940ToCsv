package org.convert.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transactions {
    private String fecha;
    private String folderName;
    private String cuenta;
    private String referenciaBancaria;
    private String codigoTransaccion;
    private String monto;
    private String marcaDebitoCredito;
    private String saldoInicial;
    private String detalleTransaccion;
    private String informacionTransaccion;

    public Transactions(String fecha, String folderName, String cuenta, String referenciaBancaria, String codigoTransaccion,
                       String monto, String marcaDebitoCredito, String saldoInicial, String detalleTransaccion, String informacionTransaccion) {
        this.fecha = fecha;
        this.folderName = folderName;
        this.cuenta = cuenta;
        this.referenciaBancaria = referenciaBancaria;
        this.codigoTransaccion = codigoTransaccion;
        this.monto = monto;
        this.marcaDebitoCredito = marcaDebitoCredito;
        this.saldoInicial = saldoInicial;
        this.detalleTransaccion = detalleTransaccion;
        this.informacionTransaccion = informacionTransaccion;
    }

    public String toCSVString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                formatFecha(fecha), folderName, cuenta, referenciaBancaria, codigoTransaccion,
                monto, marcaDebitoCredito, saldoInicial, detalleTransaccion, informacionTransaccion);
    }

    private String formatFecha(String fecha) {
        // Implement your logic to format the date
        return fecha;
    }
}
