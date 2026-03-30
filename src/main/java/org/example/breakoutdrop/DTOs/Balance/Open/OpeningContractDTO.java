package org.example.breakoutdrop.DTOs.Balance.Open;

import java.util.List;

public record OpeningContractDTO(
        Long userId,
        List<Long> inventoryIds
) { }
