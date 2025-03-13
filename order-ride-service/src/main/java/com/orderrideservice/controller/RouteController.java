package com.orderrideservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.orderrideservice.dto.RouteDto;
import com.orderrideservice.service.RouteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;
    private static final Logger LOG = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<RouteDto> createRoute(@RequestBody RouteDto routeDto) {
        RouteDto createdRoute = routeService.createRoute(routeDto);
        return ResponseEntity.ok(createdRoute);
    }

    @GetMapping
    public ResponseEntity<List<RouteDto>> getRoutes() {
        List<RouteDto> routeDtoList = routeService.getRoutes();
        if (routeDtoList.isEmpty()) {
            LOG.info("routeDtoList if empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            LOG.info("routeDtoList is {}", routeDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(routeDtoList);
        }
    }
}
