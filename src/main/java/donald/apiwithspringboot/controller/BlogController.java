package donald.apiwithspringboot.controller;


import donald.apiwithspringboot.model.Blog;
import donald.apiwithspringboot.repository.BlogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Blog", description = "Blog controller")
public class BlogController {

    final
    private BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Operation(summary = "Find all blogs", description = "All blogs", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)))) })
    @GetMapping("/blog")
    public List<Blog> index(){
        return blogRepository.findAll();
    }



    @Operation(summary = "Find blog by id", description = "Find blog by id", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Blog.class))) })
    @GetMapping("/blog/{id}")
    public Blog show(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        return blogRepository.findById(blogId).orElse(new Blog());
    }

    @PostMapping("/blog/search")
    @Operation(summary = "search blog by text in title", description = "Find blog by text in title", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)))) })
    public List<Blog> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return blogRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }


    @Operation(summary = "Create a new blog", description = "Create a new blog", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Blog.class))) })
    @PostMapping("/blog")
    public Blog create(@RequestBody Map<String, String> body){
        String title = body.get("title");
        String content = body.get("content");
        return blogRepository.save(new Blog(title, content));
    }


    @Operation(summary = "Update a new blog", description = "Update a new blog", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Blog.class))) })
    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(id);
        // getting blog
        Blog blog = blogRepository.findById(blogId).orElse(new Blog());
        blog.setTitle(body.get("title"));
        blog.setContent(body.get("content"));
        return blogRepository.save(blog);
    }


    @Operation(summary = "Delete a  blog", description = "Delete a blog", tags = { "blog" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation =  Boolean.class))) })
    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        blogRepository.deleteById(blogId);
        return true;
    }

}
