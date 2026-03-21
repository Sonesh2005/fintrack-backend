package com.sonesh.finance.service;

import com.sonesh.finance.dto.CategoryAnalyticsResponse;
import com.sonesh.finance.model.Expense;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.ExpenseRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ✅ Add expense for logged-in user (email from JWT)
    public Expense addExpenseByEmail(String email, Expense expense) {
        User user = getUserByEmail(email);
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    // ✅ Get all expenses of logged-in user
    public List<Expense> getAllByEmail(String email) {
        User user = getUserByEmail(email);
        return expenseRepository.findByUserId(user.getId());
    }

    // ✅ Total spending
    public Double getTotalByEmail(String email) {
        User user = getUserByEmail(email);
        return expenseRepository.getTotalByUser(user.getId());
    }

    // ✅ Monthly total
    public Double getMonthlyTotalByEmail(String email, int year, int month) {
        User user = getUserByEmail(email);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository.getTotalByUserBetweenDates(user.getId(), start, end);
    }

    // ✅ Category filter
    public List<Expense> getByCategoryByEmail(String email, String category) {
        User user = getUserByEmail(email);
        return expenseRepository.findByUserIdAndCategory(user.getId(), category);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // ✅ update fields
        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setDate(updatedExpense.getDate());

        return expenseRepository.save(existing);
    }
    public List<CategoryAnalyticsResponse> getCategoryAnalyticsByEmail(String email) {

        User user = getUserByEmail(email);

        List<Object[]> results = expenseRepository.getCategoryTotals(user.getId());

        List<CategoryAnalyticsResponse> list = new ArrayList<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            Double total = (Double) row[1];

            list.add(new CategoryAnalyticsResponse(category, total));
        }

        return list;
    }
}
