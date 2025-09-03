package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/me/cd")
    public ResponseEntity <List<MusicRecordResponse>> getMyCds(@AuthenticationPrincipal CustomUserDetail userDetail){
        List<MusicRecordResponse> list = musicRecordService.getCdsByUserUsername(userDetail.getUser().getUsername());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid UserRegisterRequest userRegisterRequest){
        UserResponse updatedUser = userService.updateOwnUser(userDetail.getId(), userRegisterRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail){
        String message = userService.deleteOwnUser(userDetail.getId());
        return ResponseEntity.ok(message);
    }
}
