package com.orderrideservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orderrideservice.dto.RouteDto;
import com.orderrideservice.entity.Route;
import com.orderrideservice.mapper.RouteMapper;
import com.orderrideservice.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private static Logger log = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    public RouteService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public RouteDto createRoute(RouteDto routeDto) {
        Route route = routeMapper.toEntity(routeDto);
        Route savedRoute = routeRepository.save(route);
        return routeMapper.toDto(savedRoute);
    }

    public List<RouteDto> getRoutes() {
        List<Route> routes = routeRepository.findAll();
        if (routes.isEmpty()) {
            log.info("No routes found");
            return null;
        } else {
            return routes.stream()
                    .map(routeMapper::toDto)
                    .toList();
        }
    }
}
