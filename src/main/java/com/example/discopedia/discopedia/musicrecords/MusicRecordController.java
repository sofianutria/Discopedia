package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
