package culinaryblog.contoller;

import culinaryblog.bindigModel.CommentBindingModel;
import culinaryblog.entity.Comment;
import culinaryblog.entity.Recipe;
import culinaryblog.entity.User;
import culinaryblog.repository.CommentRepository;
import culinaryblog.repository.RecipeRepository;
import culinaryblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("/recipe/comment/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String comment(Model model, @PathVariable Integer id){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }

        Recipe recipe = this.recipeRepository.findOne(id);


        model.addAttribute("recipe", recipe);
        model.addAttribute("view", "recipe/comment/create");

        return "base-layout";
    }

    @PostMapping("/recipe/comment/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String commentProcess(CommentBindingModel commentBindingModel, @PathVariable Integer id){

        if (!this.recipeRepository.exists(id)){
            return "redirect:/";
        }

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Recipe recipe = this.recipeRepository.findOne(id);


        Comment commentEntity = new Comment(

                commentBindingModel.getContent(),
                userEntity,
                recipe
        );

        this.commentRepository.saveAndFlush(commentEntity);

        return "redirect:/recipe/" + id;
    }

    @GetMapping("/recipe/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id){

        if ( !this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment = commentRepository.findOne(id);
        Integer recipe = comment.getRecipe().getId();


        if (!isUserAuthorOrAdminComments(comment)){

            return "redirect:/recipe/" + comment.getRecipe().getId();

        }


        model.addAttribute("recipe", recipe);
        model.addAttribute("comment", comment);
        model.addAttribute("view", "recipe/comment/edit");

        return "base-layout";
    }

    @PostMapping("/recipe/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, CommentBindingModel commentBindingModel ){

        if ( !this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdminComments(comment)){

            return "redirect:/recipe/" + comment.getRecipe().getId();

        }

        comment.setContent(commentBindingModel.getContent());

        this.commentRepository.saveAndFlush(comment);

        return "redirect:/recipe/" + comment.getRecipe().getId();

    }

    @GetMapping("/recipe/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){

        if ( !this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment = commentRepository.findOne(id);

        Integer recipe = comment.getRecipe().getId();

        if (!isUserAuthorOrAdminComments(comment)){

            return "redirect:/recipe/" + id;

        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("comment", comment);
        model.addAttribute("view", "recipe/comment/delete");

        return "base-layout";
    }

    @PostMapping("/recipe/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if (!this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment = commentRepository.findOne(id);

        if (!isUserAuthorOrAdminComments(comment)){

            return "redirect:/recipe/" + comment.getRecipe().getId();

        }

        this.commentRepository.delete(comment);

        return "redirect:/recipe/" + comment.getRecipe().getId();
    }

    private  boolean isUserAuthorOrAdminComments(Comment comment){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthorComment(comment);

    }

}
