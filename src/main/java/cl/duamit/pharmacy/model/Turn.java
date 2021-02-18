package cl.duamit.pharmacy.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Turn {
    private String dayName;
    private LocalDateTime open;
    private LocalDateTime close;
}
