package com.example.Projekt;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class UserRequestIndex {
    private String name;
    private Integer noOfBots;
    private Integer choosenAction;
    private int valueRiseBet;
}
