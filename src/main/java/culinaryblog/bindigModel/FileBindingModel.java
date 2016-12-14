package culinaryblog.bindigModel;


import org.springframework.web.multipart.MultipartFile;

public class FileBindingModel {

    private MultipartFile picture;

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
