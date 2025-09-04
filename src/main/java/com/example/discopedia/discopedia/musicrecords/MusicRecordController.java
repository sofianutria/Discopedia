package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordRequest;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/cds")
@RequiredArgsConstructor
public class MusicRecordController {
    private final MusicRecordService musicRecordService;

    @GetMapping
    public ResponseEntity<List<MusicRecordResponseShort>> getAllMusicRecords() {
        List<MusicRecordResponseShort> list = musicRecordService.getAllMusicRecords();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/auth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MusicRecordResponseShort>> getAllMusicRecordsUser(
            @AuthenticationPrincipal CustomUserDetail customUserDetail) {
        List<MusicRecordResponseShort> list = musicRecordService.getAllMusicRecordsUser(customUserDetail.getUsername());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MusicRecordResponse>> getFilteredMusicRecords(@RequestParam(required = false) String title, @RequestParam (required = false) String artist)  {
        List<MusicRecordResponse> list = musicRecordService.getFilteredMusicRecord(title, artist);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user")
    public ResponseEntity<List<MusicRecordResponse>> getMusicRecordByUserUsername(@RequestParam String username) {
        List<MusicRecordResponse> list = musicRecordService.getMusicRecordsByUserUsername(username);
        return ResponseEntity.ok(list);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MusicRecordResponse> addMusicRecord(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestBody @Valid MusicRecordRequest request) {
        MusicRecordResponse created = musicRecordService.addMusicRecord(request, customUserDetail.getUser());
        return ResponseEntity.ok(created);
    }
}
