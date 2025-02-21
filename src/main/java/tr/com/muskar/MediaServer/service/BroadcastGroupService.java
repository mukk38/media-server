package tr.com.muskar.MediaServer.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.BroadcastGroupSettings;
import tr.com.muskar.MediaServer.entity.dto.BroadcastGroupDto;
import tr.com.muskar.MediaServer.repository.BroadcastGroupRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BroadcastGroupService {
    private final BroadcastGroupRepository broadcastGroupRepository;

    public void save(BroadcastGroupDto broadcastGroupDto) {
        broadcastGroupRepository.save(new BroadcastGroup(broadcastGroupDto));
    }

    public void delete(String id) {
        broadcastGroupRepository.deleteById(new ObjectId(id));
    }

    public void changeSettings(String id, BroadcastGroupSettings broadcastGroupSettings) {
        Optional<BroadcastGroup> broadcastGroupOptional = broadcastGroupRepository.findById(new ObjectId(id));
        if (broadcastGroupOptional.isPresent()) {
            BroadcastGroup broadcastGroup = broadcastGroupOptional.get();
            broadcastGroup.updateSettings(broadcastGroupSettings);
            broadcastGroupRepository.save(broadcastGroup);
        }
    }
}
