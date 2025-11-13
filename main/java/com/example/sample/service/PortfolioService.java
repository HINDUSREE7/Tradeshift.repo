package com.example.sample.service;

import com.example.sample.entity.Portfolio;
import com.example.sample.entity.User;
import com.example.sample.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public List<Portfolio> getUserPortfolios(User user) {
        return portfolioRepository.findByUser(user);
    }

    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolioById(Long id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }
}