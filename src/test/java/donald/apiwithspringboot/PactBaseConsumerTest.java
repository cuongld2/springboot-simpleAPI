package donald.apiwithspringboot;

import static org.assertj.core.api.Assertions.assertThat;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import donald.apiwithspringboot.controller.BlogController;
import donald.apiwithspringboot.model.Blog;
import donald.apiwithspringboot.repository.BlogRepository;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PactBaseConsumerTest {

    @MockBean(name="blogRepository")
    BlogRepository blogRepository;

    @Autowired
    private BlogController blogController;

    @Pact(consumer = "blog_by_id")
    public RequestResponsePact pactBlogById(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .numberType("id", 1)
                .stringType("title","Testing")
                .stringType("content","Testing")
                ).build();

        return builder
                .uponReceiving("Get blog by ID")
                .path("/blog/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @PactVerification(fragment = "pactBlogById")
    @Test
    public void blogById() {

        Blog blog = new Blog();
        blog.setTitle("Testing");
        blog.setContent("Testing");
        blog.setId(1);
        Mockito.when(blogRepository.findById(blog.getId())).thenReturn(Optional.of(blog));
        Optional<Blog> blog_service_abc = Optional.ofNullable(blogController.show("1"));
        assertThat(blog.getTitle()).isEqualTo(blog_service_abc.get().getTitle());
    }

}
