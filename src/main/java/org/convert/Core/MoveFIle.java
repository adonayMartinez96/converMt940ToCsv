package org.convert.Core;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
@NoArgsConstructor
public class MoveFIle {

    public void moveFile(Path origen, Path carptetaDestino){
        try{
            Path destino = carptetaDestino.resolve(origen.getFileName());
            Files.move(origen,destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo movido a carpeta destino----> "+ destino);
        }catch (IOException e){
            System.err.println("Error moviendo el archivo: "+e.getMessage());
        }
    }

    public Boolean testFuncion(){
        return true;
    }
}
