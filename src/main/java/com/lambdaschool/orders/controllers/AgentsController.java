package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agents")
public class AgentsController {
    @Autowired
    private AgentService agentService;

    // http://localhost:2019/agents/agent/{id}
    @GetMapping(value = "/agent/{id}", produces = "application/json")
    public ResponseEntity<?> getAgentWithId(@PathVariable long id) {
        Agent agent = agentService.findAgentById(id);
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }
}
