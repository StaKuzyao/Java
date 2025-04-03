package com.example.openweather.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.openweather.Service.RequestCounterService;

@RestController
public class RequestCounterController {

    private final RequestCounterService requestCounterService;

    @Autowired
    public RequestCounterController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping("/request-count")
    public int getRequestCount() {
        return requestCounterService.getCount();
    }
}
