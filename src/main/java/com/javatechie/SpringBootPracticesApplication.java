package com.javatechie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.dto.ChatGPTRequest;
import com.javatechie.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class SpringBootPracticesApplication {

    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate template;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) throws JsonProcessingException {
        // create a request
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        // call the API
        ChatGPTResponse response = template.postForObject(apiUrl, request, ChatGPTResponse.class);
        System.out.println("Response : "+new ObjectMapper().writeValueAsString(response));
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/greetings")
    public String greetings() {
        return "Hi John Welcome to javatechie 1st shots video";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootPracticesApplication.class, args);
    }

}
