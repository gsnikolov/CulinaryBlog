package culinaryblog.contoller;


import culinaryblog.bindigModel.CommentBindingModel;
import culinaryblog.bindigModel.RecipeBindingModel;
import culinaryblog.entity.Category;
import culinaryblog.entity.Comment;
import culinaryblog.entity.Recipe;
import culinaryblog.entity.User;
import culinaryblog.repository.CategoryRepository;
import culinaryblog.repository.CommentRepository;
import culinaryblog.repository.RecipeRepository;
import culinaryblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;



@Controller
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/recipe/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("view", "recipe/create");

        return "base-layout";
    }

    @PostMapping("/recipe/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(RecipeBindingModel recipeBindingModel){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(recipeBindingModel.getCategoryId());

        Recipe recipeEntity = new Recipe(
                recipeBindingModel.getTitle(),
                recipeBindingModel.getContent(),
                userEntity,
                category
        );

        this.recipeRepository.saveAndFlush(recipeEntity);

        return "redirect:/";
    }

    @GetMapping("/recipe/{id}")
    public String details(Model model, @PathVariable Integer id){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication()
                     instanceof AnonymousAuthenticationToken)){

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User entityUser = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", entityUser);
        }

        Recipe recipe = this.recipeRepository.findOne(id);

        List<Comment> allComments = this.commentRepository.findAll();

        ArrayList<Comment> comments = new ArrayList<>();

        for ( Comment comment : allComments) {

            if (comment.getRecipe() == recipe){

                comments.add(comment);
            }
        }


        model.addAttribute("recipe", recipe);
        model.addAttribute("comments", comments);
        model.addAttribute("view", "recipe/details");

        return "base-layout";
    }

    @GetMapping("/recipe/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }


        Recipe recipe = this.recipeRepository.findOne(id);

        if (!isUserAuthorOrAdmin(recipe)){

            return "redirect:/recipe/" + id;

        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("view", "recipe/edit");

        return "base-layout";

    }

    @PostMapping("/recipe/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, RecipeBindingModel recipeBindingModel){

        if ( !this.recipeRepository.exists(id)){
            return "redirect:/";
        }

        Recipe recipe = this.recipeRepository.findOne(id);

        if (!isUserAuthorOrAdmin(recipe)){

            return "redirect:/recipe/" + id;

        }

        recipe.setContent(recipeBindingModel.getContent());
        recipe.setTitle(recipeBindingModel.getTitle());

        this.recipeRepository.saveAndFlush(recipe);

        return "redirect:/recipe/" + recipe.getId();

    }

    @GetMapping("/recipe/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }

        Recipe recipe = this.recipeRepository.findOne(id);

        if (!isUserAuthorOrAdmin(recipe)){

            return "redirect:/recipe/" + id;

        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("view", "recipe/delete");

        return "base-layout";
    }

    @PostMapping("/recipe/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }


        Recipe recipe = this.recipeRepository.findOne(id);

        if (!isUserAuthorOrAdmin(recipe)){

            return "redirect:/recipe/" + id;

        }

        for (Comment comment : recipe.getComments()) {

            this.commentRepository.delete(comment);
        }

        this.recipeRepository.delete(recipe);

        return "redirect:/";

    }


    private  boolean isUserAuthorOrAdmin(Recipe recipe){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(recipe);

    }








}
