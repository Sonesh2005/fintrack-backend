package com.sonesh.finance.service;

import com.sonesh.finance.model.Income;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.IncomeRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ✅ Add income
    public Income addIncomeByEmail(String email, Income income) {
        User user = getUserByEmail(email);
        income.setUser(user);
        return incomeRepository.save(income);
    }

    // ✅ Get all incomes
    public List<Income> getAllByEmail(String email) {
        User user = getUserByEmail(email);
        return incomeRepository.findByUserId(user.getId());
    }

    // ✅ Total income
    public Double getTotalByEmail(String email) {
        User user = getUserByEmail(email);
        return incomeRepository.getTotalByUser(user.getId());
    }

    // ✅ Monthly income
    public Double getMonthlyTotalByEmail(String email, int year, int month) {
        User user = getUserByEmail(email);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return incomeRepository.getTotalByUserBetweenDates(user.getId(), start, end);
    }

    // ✅ Category filter
    public List<Income> getByCategoryByEmail(String email, String category) {
        User user = getUserByEmail(email);
        return incomeRepository.findByUserIdAndCategory(user.getId(), category);
    }

    // ✅ Update (only if income belongs to user)
    public Income updateIncomeByEmail(String email, Long id, Income updated) {
        User user = getUserByEmail(email);

        Income existing = incomeRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Income not found"));

        existing.setSource(updated.getSource());
        existing.setAmount(updated.getAmount());
        existing.setCategory(updated.getCategory());
        existing.setDate(updated.getDate());

        return incomeRepository.save(existing);
    }

    // ✅ Delete (only if income belongs to user)
    public void deleteIncome(Long id) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        incomeRepository.delete(income);
    }
}