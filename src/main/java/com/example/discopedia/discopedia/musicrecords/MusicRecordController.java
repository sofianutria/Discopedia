package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
