package com.sonesh.finance.service;

import com.sonesh.finance.dto.GoalRequest;
import com.sonesh.finance.model.Goal;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.GoalRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Goal createGoal(String email, GoalRequest request) {
        User user = getUser(email);

        Goal goal = new Goal();
        goal.setTitle(request.getTitle());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setSavedAmount(
                request.getSavedAmount() != null ? request.getSavedAmount() : BigDecimal.ZERO
        );
        goal.setCategory(request.getCategory());
        goal.setUser(user);

        return goalRepository.save(goal);
    }

    public List<Goal> getGoals(String email) {
        User user = getUser(email);
        return goalRepository.findByUserId(user.getId());
    }

    public Goal updateGoal(Long id, String email, GoalRequest request) {
        User user = getUser(email);

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        goal.setTitle(request.getTitle());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setSavedAmount(request.getSavedAmount() != null ? request.getSavedAmount() : BigDecimal.ZERO);
        goal.setCategory(request.getCategory());

        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id, String email) {
        User user = getUser(email);

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        goalRepository.delete(goal);
    }
}