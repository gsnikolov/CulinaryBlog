package culinaryblog.bindigModel;


import culinaryblog.entity.Recipe;

import javax.validation.constraints.NotNull;

public class CommentBindingModel {


    private String content;



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
