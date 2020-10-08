package donald.apiwithspringboot;


import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import donald.apiwithspringboot.controller.BlogServiceClient;
import donald.apiwithspringboot.model.Blog;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "blog-service.base-url:http://localhost:8082")
public class BlogConsumerTest {

    private static final String TITLE = "Testing";
    private static final LocalDateTime LAST_LOGIN = LocalDateTime.of(2018, 10, 16, 12, 34, 12);

    @Rule
    public PactProviderRuleMk2 provider = new PactProviderRuleMk2("user-service", null,
            8082, this);

    @Rule
    public ExpectedException expandException = ExpectedException.none();

    @Autowired
    private BlogServiceClient blogServiceClient;


    @Pact(consumer = "front-end")
    public RequestResponsePact blogById(PactDslWithProvider builder) {

        // See https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-consumer-junit#dsl-matching-methods
        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .numberType("id", 1)
                .stringType("title","Testing")
                .stringType("content","Testing")
        ).build();

        return builder
                .uponReceiving("Get blog by ID")
                .headers("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdW9uZ2xkMiIsImV4cCI6MTYwMjE3MDQzNSwiaWF0IjoxNjAyMTUyNDM1fQ.QYY5bBIRaDTn4M2LL8FbrIHI63PG2XgQGnRJtuf-iQHcQHx6zsKEEjk0zvIUPuyaXtZZu3BE5-Des8EMTNPCPw")
                .path("/blog/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @PactVerification(fragment = "blogById")
    @Test
    public void test_blog_by_id() {
        final Blog blog = blogServiceClient.getBlog("1");
        assertThat(blog.getTitle()).isEqualTo(TITLE);
    }

}
