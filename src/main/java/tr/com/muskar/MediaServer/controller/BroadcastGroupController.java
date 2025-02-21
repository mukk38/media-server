package tr.com.muskar.MediaServer.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.muskar.MediaServer.entity.BroadcastGroupSettings;
import tr.com.muskar.MediaServer.entity.dto.BroadcastGroupDto;
import tr.com.muskar.MediaServer.service.BroadcastGroupService;

@RestController
@RequestMapping("/broadcast-group")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BroadcastGroupController {

    private final BroadcastGroupService broadcastGroupService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createBroadcastGroup(@RequestBody BroadcastGroupDto broadcastGroupDto) {
        broadcastGroupService.save(broadcastGroupDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteBroadcastGroup(@RequestParam String id) {
        broadcastGroupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/change-settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> changeSettings(@RequestBody BroadcastGroupSettings broadcastGroupSettings, @RequestParam String id) {
        broadcastGroupService.changeSettings(id, broadcastGroupSettings);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
