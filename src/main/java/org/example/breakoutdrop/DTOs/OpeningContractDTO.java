package org.example.breakoutdrop.DTOs;

import java.util.List;

public record OpeningContractDTO(
        Long userId,
        List<Long> skinId
) { }