package org.example.breakoutdrop.DTOs.Delete;

import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;

public record DeleteInventoryDTO(
        User user,
        Skin skin
) { }
