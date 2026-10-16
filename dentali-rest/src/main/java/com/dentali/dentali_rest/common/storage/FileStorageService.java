package com.dentali.dentali_rest.common.storage;


import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dentali.dentali_rest.common.exception.ReglaNegocioException;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
public class FileStorageService {

    private static final Map<String, String> IMAGENES_PERMITIDAS = Map.of(
        "image/jpeg","jpg",
        "image/png","png",
        "image/webp","webp"
    );

    private final Path rutaBase;
    
    public FileStorageService(StorageProperties propiedades){
        this .rutaBase = Paths.get(propiedades.rutaBase()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(rutaBase);

        }catch(IOException e){
            throw new IllegalStateException("No se pudo crear el directorio de almacenamiento: "+rutaBase, e);
        }

        log.info("Almacenamiento de archivos en: {}", rutaBase);
    }

    /**
     * Guarda una imagen y devuelve su ruta RELATIVA (la que se persiste en la base de datos).
     */
    public String guardarImagen(MultipartFile archivo, String carpeta){

        if(archivo == null || archivo.isEmpty()){
            throw new ReglaNegocioException("El archivo está vacío.");
        }

        String extension = IMAGENES_PERMITIDAS.get(archivo.getContentType());
        if(extension == null){
            throw new ReglaNegocioException("Formato no permitido. Usa JPG, PNG o WEBP");
        }

        String rutaRelativa = carpeta + "/"+ UUID.randomUUID()+"."+extension;
        Path destino = resolverSeguro(rutaRelativa);

        try{
            Files.createDirectories(destino.getParent());
            try(InputStream entrada = archivo.getInputStream()){
                Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(IOException e){
            throw new UncheckedIOException("No se pudo guaradar el archivo.", e);
        }

        return rutaRelativa;

    }

    public Optional<Resource> cargar (String rutaRelativa){
        Path archivo = resolverSeguro(rutaRelativa);
        return Files.exists(archivo)? Optional.of(new FileSystemResource(archivo)) : Optional.empty();
    }

    public void eliminar(String rutaRelativa){
        if(rutaRelativa == null){
            return;
        }
        try{
            Files.deleteIfExists(resolverSeguro(rutaRelativa));
        }catch(IOException e){
            log.warn("No se pudo eliminar el archivo {}", rutaRelativa, e);
        }
        
    }

    private Path resolverSeguro(String rutaRelativa){
        Path destino = rutaBase.resolve(rutaRelativa).normalize();
        if(!destino.startsWith(rutaBase)){
            throw new ReglaNegocioException("Ruta de archivo inválida");
        }
        return destino;
    }

}
