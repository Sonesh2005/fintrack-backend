package com.sonesh.finance.service;

import com.sonesh.finance.model.RecurringExpense;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.RecurringExpenseRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final UserRepository userRepository;

    public RecurringExpenseService(RecurringExpenseRepository recurringExpenseRepository,
                                   UserRepository userRepository) {
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public RecurringExpense create(String email, RecurringExpense expense) {

        User user = getUser(email);

        expense.setUser(user);

        return recurringExpenseRepository.save(expense);
    }

    public List<RecurringExpense> getAll(String email) {

        User user = getUser(email);

        return recurringExpenseRepository.findByUserId(user.getId());
    }

    public void delete(Long id) {
        recurringExpenseRepository.deleteById(id);
    }
    public RecurringExpense updateRecurringExpense(Long id, RecurringExpense updatedExpense) {
        RecurringExpense existing = recurringExpenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurring expense not found"));

        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setFrequency(updatedExpense.getFrequency());
        existing.setStartDate(updatedExpense.getStartDate());

        return recurringExpenseRepository.save(existing);
    }
}