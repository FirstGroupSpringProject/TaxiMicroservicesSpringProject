package com.userdriverservice.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private UUID userId;
    private String name;
    private String phone;

}