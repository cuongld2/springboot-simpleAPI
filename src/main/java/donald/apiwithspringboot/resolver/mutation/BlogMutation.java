package donald.apiwithspringboot.resolver.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import donald.apiwithspringboot.model.Blog;
import donald.apiwithspringboot.model.mutation.CreateBlog;
import donald.apiwithspringboot.repository.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@AllArgsConstructor
public class BlogMutation implements GraphQLMutationResolver {

    private BlogRepository blogRepository;


    @Transactional
    public Blog createBlog(CreateBlog body){
        String title = body.getTitle();
        String content = body.getContent();
        return blogRepository.save(new Blog(title, content));
    }


}
