package com.portfolio;

import org.junit.jupiter.api.Test;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static com.portfolio.helper.TestingPortfolioBuilder.anEmptyPortFolio;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PortfolioFailsTest {

    @Test
    public void when_the_file_contains_no_assets()
    {
        var portfolio = anEmptyPortFolio()
                .onDate("2025/01/01")
                .build();

        assertThrows(IndexOutOfBoundsException.class, portfolio::computePortfolioValue);
    }

    @Test
    public void when_an_asset_has_an_invalid_date()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Another asset").fromDate("wrong date").withValue(0))
                .onDate("2025/01/01")
                .build();

        assertThrows(UncheckedParseException.class, portfolio::computePortfolioValue);
    }
}
