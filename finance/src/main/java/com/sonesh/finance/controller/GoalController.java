package com.sonesh.finance.controller;

import com.sonesh.finance.dto.GoalRequest;
import com.sonesh.finance.model.Goal;
import com.sonesh.finance.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getGoals(Authentication authentication) {
        return ResponseEntity.ok(goalService.getGoals(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(
            @RequestBody GoalRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(goalService.createGoal(authentication.getName(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(
            @PathVariable Long id,
            @RequestBody GoalRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(goalService.updateGoal(id, authentication.getName(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoal(
            @PathVariable Long id,
            Authentication authentication
    ) {
        goalService.deleteGoal(id, authentication.getName());
        return ResponseEntity.ok("Goal deleted successfully");
    }
}