package com.example.sample.repository;

import com.example.sample.entity.Asset;
import com.example.sample.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByPortfolio(Portfolio portfolio);
    List<Asset> findByPortfolioId(Long portfolioId);
}