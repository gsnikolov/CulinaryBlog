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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;


    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model, @PathVariable Integer id, CommentBindingModel commentBindingModel){

        Recipe recipeId = this.recipeRepository.findOne(id);

        createProcess(commentBindingModel, recipeId);

        model.addAttribute("view", "comment/create");

        return "base-layout";

    }

    @PostMapping("/recipe/details/")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(CommentBindingModel commentBindingModel, Recipe recipeId){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());



        Comment commentEntity = new Comment(
                commentBindingModel.getContent(),
                userEntity,
                recipeId
        );


        this.commentRepository.saveAndFlush(commentEntity);

        return "redirect:/";

    }






}
