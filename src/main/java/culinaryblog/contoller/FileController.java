package culinaryblog.contoller;

import culinaryblog.bindigModel.FileBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    @GetMapping("/file/upload")
    public String file(Model model){

        model.addAttribute("view", "file/upload");

        return "base-layout";
    }

    @PostMapping("/file/upload")
    public String UploadFile(FileBindingModel fileBindingModel)
    {
        MultipartFile file = fileBindingModel.getPicture();

        if ( file != null){

            String originalFileName = file.getOriginalFilename();

            File imageFile = new File(
                    "C:\\Users\\GN\\Desktop\\CulinaryBlog\\CulinaryBlog\\src\\main\\resources\\img",
                    originalFileName);
            try {
                file.transferTo(imageFile);
                System.out.println(imageFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }


}
