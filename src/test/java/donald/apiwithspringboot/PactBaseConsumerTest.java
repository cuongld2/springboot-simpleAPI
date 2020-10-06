package donald.apiwithspringboot;

import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import donald.apiwithspringboot.controller.AuthController;
import org.junit.runner.RunWith;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.PactTestExecutionContext;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.ConsumerPactTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PactBaseConsumerTest extends ConsumerPactTest{

    @Autowired
    AuthController providerService;

    @Override
    @Pact(provider="ExampleProvider", consumer="BaseConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .given("")
                .uponReceiving("Pact JVM example Pact interaction")
                .path("/authenticate")
                .body("{\n" +
                        "\t\"username\": \"cuongld2\",\n" +
                        "\t\"password\":\"12345\"\n" +
                        "}")
                .method("POST")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\n" +
                        "    \"token\": \"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdW9uZ2xkMiIsImV4cCI6MTYwMTk3MzQ5MiwiaWF0IjoxNjAxOTU1NDkyfQ.9m1NrYXcRpBknJq0qkvSaXL-cMeGOrmCAsMjmMyJlWZ4hWQMLx2o4sc12jtx3y2KEj7d-T30W0iS4HsGRxcL3A\"\n" +
                        "}")
                .toPact();
    }

    @Override
    protected String providerName() {
        return "ExampleProvider";
    }

    @Override
    protected String consumerName() {
        return "BaseConsumer";
    }

    @Override
    protected void runTest(MockServer mockServer, PactTestExecutionContext pactTestExecutionContext) throws IOException {

        System.out.println("Pact test");
    }


}
