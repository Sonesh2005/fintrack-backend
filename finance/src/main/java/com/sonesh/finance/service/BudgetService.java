package com.sonesh.finance.service;

import com.sonesh.finance.dto.BudgetAlertResponse;
import com.sonesh.finance.model.Budget;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.BudgetRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpenseService expenseService;
    private final NotificationService notificationService;

    public BudgetService(BudgetRepository budgetRepository,
                         UserRepository userRepository,
                         ExpenseService expenseService,
                         NotificationService notificationService) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expenseService = expenseService;
        this.notificationService = notificationService;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public Budget saveBudget(String email, Double monthlyBudget) {
        User user = getUserByEmail(email);

        Budget budget = budgetRepository.findByUserId(user.getId())
                .orElse(new Budget());

        budget.setUser(user);
        budget.setMonthlyBudget(monthlyBudget);

        return budgetRepository.save(budget);
    }

    public Double getMonthlyBudget(String email) {
        User user = getUserByEmail(email);

        return budgetRepository.findByUserId(user.getId())
                .map(Budget::getMonthlyBudget)
                .orElse(0.0);
    }

    public BudgetAlertResponse getBudgetAlert(String email) {
        User user = getUserByEmail(email);
        double budget = getMonthlyBudget(email);

        LocalDate now = LocalDate.now();

        double spent = expenseService.getMonthlyTotalByEmail(
                email,
                now.getYear(),
                now.getMonthValue()
        );

        int percentage = (budget == 0) ? 0 : (int) ((spent / budget) * 100);

        String status;
        String message;

        if (budget == 0) {
            status = "NO_BUDGET";
            message = "No monthly budget is set.";
        } else if (percentage < 70) {
            status = "SAFE";
            message = "Your spending is within the safe range.";
        } else if (percentage < 90) {
            status = "WARNING";
            message = "You have used " + percentage + "% of your budget.";

            notificationService.createNotification(
                    user,
                    "Budget warning",
                    "You have used " + percentage + "% of your monthly budget.",
                    "warning"
            );

        } else if (percentage <= 100) {
            status = "DANGER";
            message = "You are close to exceeding your budget!";

            notificationService.createNotification(
                    user,
                    "Budget danger",
                    "You are very close to exceeding your monthly budget.",
                    "warning"
            );

        } else {
            status = "EXCEEDED";
            message = "You exceeded your budget by ₹" + (spent - budget);

            notificationService.createNotification(
                    user,
                    "Budget exceeded",
                    "You exceeded your budget by ₹" + (spent - budget),
                    "danger"
            );
        }

        return new BudgetAlertResponse(budget, spent, percentage, status, message);
    }
}