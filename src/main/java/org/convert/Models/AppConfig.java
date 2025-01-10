package org.convert.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AppConfig  {
    private String pendientesDir;
    private String procesadosDir;
    private String csvDir;
    private String erroresDir;
    private String nameBank;


    public AppConfig(){}

    @Override
    public String toString() {
        return "Pendientes: " + pendientesDir + "\n" +
                "Procesados: " + procesadosDir + "\n" +
                "CSV: " + csvDir + "\n" +
                "Errores: " + erroresDir + "\n" +
                "Banco: " + nameBank;
    }
}
