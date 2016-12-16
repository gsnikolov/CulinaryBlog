package culinaryblog.contoller.admin;


import com.sun.javafx.sg.prism.NGShape;
import culinaryblog.bindigModel.CategoryBindingModel;
import culinaryblog.entity.Category;
import culinaryblog.entity.Comment;
import culinaryblog.entity.Recipe;
import culinaryblog.repository.CategoryRepository;
import culinaryblog.repository.CommentRepository;
import culinaryblog.repository.RecipeRepository;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/")
    public String list(Model model) {

        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        model.addAttribute("categories", categories);
        model.addAttribute("view", "/admin/category/list");

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("view", "admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel) {
        if (StringUtils.isEmpty(categoryBindingModel.getName())) {
            return "redirect:/admin/categories/create";
        }

        Category category = new Category(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";

    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {

        if (!this.categoryRepository.exists(id)) {

            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");

        return "base-layout";

    }

    @PostMapping("/edit/{id}")
    public String editProcess(CategoryBindingModel categoryBindingModel, @PathVariable Integer id) {

        if (!this.categoryRepository.exists(id)) {

            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        category.setName(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";


    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){

        if (!this.categoryRepository.exists(id)) {
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "/admin/category/delete");

        return "base-layout";

    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if (!this.categoryRepository.exists(id)) {
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        for (Recipe recipe: category.getRecipes()) {

            for (Comment comment : recipe.getComments()) {

                this.commentRepository.delete(comment);
            }

            this.recipeRepository.delete(recipe);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";


    }

}
