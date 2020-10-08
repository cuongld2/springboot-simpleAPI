package donald.apiwithspringboot.controller;

import donald.apiwithspringboot.model.Blog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class BlogServiceClient {

    private final RestTemplate restTemplate;

    public BlogServiceClient(@Value("${blog-service.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplateBuilder().rootUri(baseUrl).build();
    }

    public Blog getBlog(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdW9uZ2xkMiIsImV4cCI6MTYwMjE3MDQzNSwiaWF0IjoxNjAyMTUyNDM1fQ.QYY5bBIRaDTn4M2LL8FbrIHI63PG2XgQGnRJtuf-iQHcQHx6zsKEEjk0zvIUPuyaXtZZu3BE5-Des8EMTNPCPw");

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<Blog> response = restTemplate.exchange("/blog/"+id, HttpMethod.GET,entity,Blog.class);
        return response.getBody();
    }

}
