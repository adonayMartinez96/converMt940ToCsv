package org.convert.Core;

import org.convert.ConvertToMt940.WriteCsv;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessFolder {
    public  void readFolder(String pathPendientes, String pathProcesado,String pathErrores, String pathCsv){
        MoveFIle moveFile = new MoveFIle();
        WriteCsv writeCsv  = new WriteCsv();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathPendientes))){
            for(Path file: stream){
                if(!Files.isDirectory(file)){
                    System.out.println("procesando archivo: " + file.getFileName() );
                    System.out.println("ruta encontrada----->"+file.getParent( ));

                    String fileNameiIteracion = file.getParent() + "\\"+ file.getFileName();
                    System.out.println("file name final "+fileNameiIteracion);

                    System.out.println("el archivo sera guardado en--------->" + pathCsv);

                    if (writeCsv.extractMt940(fileNameiIteracion,pathCsv)){
                        moveFile.moveFile(file, Paths.get(pathProcesado));
                    }else{
                        moveFile.moveFile(file, Paths.get(pathErrores));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("error al procesar la carpeta " + e.getMessage());
        }
    }
}
