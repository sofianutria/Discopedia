package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.common.SecuredBaseController;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends SecuredBaseController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal CustomUserDetail userDetail
            ){
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable @Positive(message="User id must be a positive number")
            Long id){
        UserResponse user = userService.getUserByIdAdmin(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable @Positive (message="User id must be a positive number")
            Long id){
        String message = userService.deleteUserByIdAdmin(id);
        return ResponseEntity.ok(message);
    }
}
