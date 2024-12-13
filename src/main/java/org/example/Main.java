package org.example;

import org.example.ConvertToMt940.WriteCsv;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Ruta "quemada" para pruebas rápidas
        String intputFilePath = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/MT940.txt";
        String outputFilePath = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/";

        // Verificar si se recibió un argumento en la línea de comandos
        String filePathParameter;
        String outputPathParameter;

        if (args.length > 0) {
            filePathParameter = args[0];
            outputPathParameter = args[1];

            System.out.println("Archivo a procesar recibido como argumento: " + filePathParameter);
            System.out.println("Archivo a procesar recibido como argumento: " + outputPathParameter);
        } else {
            // Usar la ruta por defecto si no se pasa un parámetro
            filePathParameter = intputFilePath;
            outputPathParameter = outputFilePath;

            System.out.println("No se proporcionó un argumento, usando ruta quemada: " + filePathParameter);
            System.out.println("El archivo se guardara en ----> "+  outputPathParameter);
        }

        try {
            WriteCsv write = new WriteCsv();
            write.extractMt940(filePathParameter,outputPathParameter);
        }catch (IOException e){
            System.err.println("error procesando el archivo "+e.getMessage());
        }

    }

}