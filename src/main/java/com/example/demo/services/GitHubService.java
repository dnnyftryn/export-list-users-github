package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {
   private final RestTemplate restTemplate = new RestTemplate();

      public List<Map<String, Object>> getGithubUsers() {
        String url = "https://api.github.com/users";
        return restTemplate.getForObject(url, List.class);
    }
}
