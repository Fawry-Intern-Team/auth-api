package com.example.auth_service.dto;

import com.example.auth_service.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class UserDTO {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<String> roles;
}
