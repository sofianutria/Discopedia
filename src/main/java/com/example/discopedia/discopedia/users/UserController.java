package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MusicRecordService musicRecordService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail){
        UserResponse user = userService.getOwnUser(userDetail.getId());
        return ResponseEntity.ok(user);
    }
}
