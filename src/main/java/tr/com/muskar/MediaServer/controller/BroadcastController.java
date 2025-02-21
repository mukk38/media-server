package tr.com.muskar.MediaServer.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.muskar.MediaServer.entity.broadcast.BroadcastDto;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.service.BroadcastService;

@RestController
@RequestMapping("/broadcast")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BroadcastController {

    private final BroadcastService broadcastService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createBroadcast(@RequestBody BroadcastDto broadcastDto) {
        broadcastService.save(broadcastDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteBroadcast(@RequestParam String broadcastGroupId, @RequestParam String broadcastId) {
        broadcastService.deleteBroadcast(broadcastGroupId, broadcastId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/stop-broadcast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> startBroadcast(@RequestParam String broadcastGroupId, @RequestParam String broadcastId, @RequestParam BroadcastType type) {
        broadcastService.startBroadcast(broadcastGroupId, broadcastId, type);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/start-broadcast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> stopBroadcast(@RequestParam String broadcastGroupId, @RequestParam String broadcastId, @RequestParam BroadcastType type) {
        broadcastService.stopBroadcast(broadcastGroupId, broadcastId, type);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
