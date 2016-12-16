package culinaryblog.contoller;

import culinaryblog.entity.Recipe;
import culinaryblog.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.AssertTrue;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RecipeRepository recipeRepository;



    @GetMapping("/")
    public String index(Model model) {

        List<Recipe> recipes = this.recipeRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("recipes", recipes);

        return "base-layout";

    }

    @GetMapping("/error/403")
    public String accessDenied(Model model){

        model.addAttribute("view", "error/403");

        return "base-layout";

    }
}