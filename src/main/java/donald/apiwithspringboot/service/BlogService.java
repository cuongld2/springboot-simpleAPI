package donald.apiwithspringboot.service;


import donald.apiwithspringboot.model.Blog;
import donald.apiwithspringboot.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(final BlogRepository blogRepository){this.blogRepository = blogRepository;}

    @Transactional(readOnly = true)
    public List<Blog> getAllBlogs(final int count){
        return this.blogRepository.findAll().stream().limit(count).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Blog> getBlog(final int id){return this.blogRepository.findById(id);}
}
