package com.example.sample.controller;

import com.example.sample.entity.Portfolio;
import com.example.sample.entity.User;
import com.example.sample.service.PortfolioService;
import com.example.sample.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@SuppressWarnings("unused")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserService userService;

    @SuppressWarnings("unused")
    public PortfolioController(PortfolioService portfolioService, UserService userService) {
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @GetMapping
    @SuppressWarnings("unused")
    public ResponseEntity<List<Portfolio>> getUserPortfolios(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            List<Portfolio> portfolios = portfolioService.getUserPortfolios(user);
            return ResponseEntity.ok(portfolios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @SuppressWarnings("unused")
    public ResponseEntity<Portfolio> createPortfolio(
            @RequestBody Portfolio portfolio,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            portfolio.setUser(user);
            Portfolio savedPortfolio = portfolioService.createPortfolio(portfolio);
            return ResponseEntity.ok(savedPortfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}