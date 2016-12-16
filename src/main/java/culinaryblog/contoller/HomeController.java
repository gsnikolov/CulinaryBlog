package culinaryblog.contoller;

import culinaryblog.entity.Category;
import culinaryblog.entity.Recipe;
import culinaryblog.repository.CategoryRepository;
import culinaryblog.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;



    @GetMapping("/")
    public String index(Model model) {

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";

    }

    @GetMapping("/error/403")
    public String accessDenied(Model model){

        model.addAttribute("view", "error/403");

        return "base-layout";

    }

    @GetMapping("/category/{id}")
    public String listRecipes(Model model, @PathVariable Integer id){

        if (!this.categoryRepository.exists(id)){
            return "redirect:/";
        }

        Category category =this.categoryRepository.findOne(id);
        Set<Recipe> recipes = category.getRecipes();

        model.addAttribute("recipes", recipes);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-recipes");

        return "base-layout";

    }



}