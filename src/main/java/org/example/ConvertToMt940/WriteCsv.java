package org.example.ConvertToMt940;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WriteCsv {

    private String filePath;
    private String outputPath;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public WriteCsv(String filePath, String outputPath) {
        this.filePath = filePath;
        this.outputPath = outputPath;
    }

    public WriteCsv() {
    }


    public  void extractMt940(String filePath,String outputPath)throws IOException{
        //file output
        String nameFile = outputPath + generateName();

        //lectura de nombre de carpeta
        String foldername = Paths.get(filePath).getParent().getFileName().toString();

        //almacen de lineas mt940
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        //Variables constantes
        String cuenta = "", saldoInicial = "", detalleTransaccion = "", informacionTransaccion = "";

            //extraer cabeceras
            for(String line: lines){
                System.out.println("extraccion de las lineas: "+ line);

                if(line.startsWith(":25:")){
                    if (validarLongitud(line, 5)){
                        cuenta = line.substring(4).trim();//numero de cuenta
                    }else{
                        System.err.println("Error: Línea :25: demasiado corta. Asignando valor predeterminado.");
                        cuenta = "N/A"; // Asignar valor predeterminado
                    }
                } else if(line.startsWith(":60F:")){
                    if(validarLongitud(line,15  )) {
                        saldoInicial = line.substring(15).replace(",", ".");
                        ;// saldo inicial
                        System.out.println("saldo inicial ------> " + saldoInicial);
                    }else{
                        System.err.println("Error: Línea :60F: demasiado corta. Asignando valor predeterminado.");
                        cuenta = "N/A"; // Asignar valor predeterminado
                    }
                }
            }

        System.out.println("el archivo a escribir es: " +outputPath);
        //creacion de archivo CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile))) {
            //escritura de los encabezados
            writer.write("Fecha,Banco,Cuenta,Referencia,Descripcion,Monto,Credito_Debito,Saldo_inicial,Detalle_Transaccion,Informacion_transaccion\n");

            //procesamiento de las transacciones
            for(int i = 0; i<lines.size(); i++){
                String line = lines.get(i);

                if(line.startsWith(":61:")){

                    //extraccion de los datos de la linea :61:
                    String fecha = line.substring(4,10); //AAMMDD fecha
                    System.out.println("fecha: "+ fecha);

                    String marcaDebitoCredito = line.substring(14,15);
                    System.out.println("marca debito credito -----> "+ marcaDebitoCredito);

                    String monto = line.substring(15, line.indexOf('N')).replace(",",".");
                    System.out.println("monto -----> "+ monto);

                    String codigoTransaccion = line.substring(line.indexOf('N')+1,line.indexOf('N')+4);
                    String referenciaBancaria = line.substring(line.indexOf("//")+2).trim();

                    detalleTransaccion = line.substring(4).replace(",",".");


                    //extraccion de informacion complementaria
                    if(i+1 <lines.size() && lines.get(i+1).startsWith(":86:")){
                        informacionTransaccion = lines.get(i+1).substring(4).trim();
                    }else {
                        informacionTransaccion = "";
                    }

                    //escribir en el archivo CSV
                        Path path = Paths.get(filePath);
                        String folderName = path.getParent().getFileName().toString();
                        writer.write(String.format(
                            "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            formatFecha(fecha), folderName, cuenta, referenciaBancaria, codigoTransaccion,
                            monto, marcaDebitoCredito, saldoInicial, detalleTransaccion, informacionTransaccion
                    ));
                }
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }


    private static String formatFecha(String fecha){
        String year ="20" + fecha.substring(0,2); //anio en 20xx
        String month = fecha.substring(2,4);
        String day=fecha.substring(4,6);
        System.out.println("format date: "+ day+"/"+month+"/"+year);
        return day+"/"+month+"/"+year;
    }

    private static String generateName(){
        // Obtener la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        // Definir el formato deseado para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

        // Formatear la fecha con extension
        String formattedDateTime = now.format(formatter);
        return formattedDateTime + ".csv";
    }

    private boolean validarLongitud(String line, int longitudRequerida){
        return line.length() > longitudRequerida;
    }
}
