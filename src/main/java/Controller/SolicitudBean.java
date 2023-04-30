package Controller;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;


import java.io.File;

@Data
@Named
@ViewScoped
public class SolicitudBean {
    String aux= "";
//    public void uploadFile() throws IOException {
//        UploadedFile uploadedFile = file;
//        String fileName = uploadedFile.getFileName();
//        InputStream input = uploadedFile.getInputstream();
//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/pdfcarpeta/");
//        File file = new File(path + fileName);
//        OutputStream output = new FileOutputStream(file);
//
//        try {
//            IOUtils.copy(input, output);
//        } finally {
//            IOUtils.closeQuietly(input);
//            IOUtils.closeQuietly(output);
//        }
//    }
}
