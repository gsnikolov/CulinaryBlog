package culinaryblog.contoller;

import culinaryblog.entity.Tag;
import culinaryblog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag/{name}")
    public String recipeWithTag(Model model, @PathVariable String name){

        Tag tag  = this.tagRepository.findByName(name);

        if (tag == null){

            return "redirect:/";
        }
        model.addAttribute("tag", tag);
        model.addAttribute("view", "tag/recipes");

        return "base-layout";

    }



}
