package tr.com.muskar.MediaServer.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastDash.BroadcastDash;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.BroadcastHls.BroadcastHls;
import tr.com.muskar.MediaServer.entity.broadcast.BroadcastDto;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.entity.enums.BroadcastStatus;
import tr.com.muskar.MediaServer.exception.BroadcastGroupNotFoundException;
import tr.com.muskar.MediaServer.exception.BroadcastNotFoundException;
import tr.com.muskar.MediaServer.mapper.BroadcastMapper;
import tr.com.muskar.MediaServer.repository.BroadcastGroupRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final BroadcastGroupRepository broadcastGroupRepository;
    private final BroadcastMapper broadcastMapper;
    private final BroadcastThreadService broadcastThreadService;

    public void save(BroadcastDto broadcastDto) {
        Optional<BroadcastGroup> broadcastGroupOptional = broadcastGroupRepository.findById(new ObjectId(broadcastDto.broadcastGroupId()));
        if (broadcastGroupOptional.isPresent()) {
            BroadcastGroup broadcastGroup = broadcastGroupOptional.get();
            if (Objects.nonNull(broadcastDto.streamId())) {
                ObjectId streamId = new ObjectId(broadcastDto.streamId());
                List<Broadcast> broadcastList = broadcastGroup.getBroadcastList();
                broadcastList.forEach(broadcast -> {
                    if (broadcast.getId().equals(streamId)) {
                        broadcastMapper.updateBroadcastWithDto(broadcast, broadcastDto);
                    }
                });
                broadcastGroupRepository.save(broadcastGroup);
            } else {
                broadcastGroup.getBroadcastList().add(new Broadcast(broadcastDto));
                broadcastGroupRepository.save(broadcastGroup);
            }
        } else {
            throw new BroadcastGroupNotFoundException(broadcastDto.broadcastGroupId());
        }
    }

    public void deleteBroadcast(String broadcastGroupId, String broadcastId) {
        Optional<BroadcastGroup> broadcastGroupOptional = broadcastGroupRepository.findById(new ObjectId(broadcastGroupId));
        if (broadcastGroupOptional.isPresent()) {
            BroadcastGroup broadcastGroup = broadcastGroupOptional.get();
            List<Broadcast> broadcastList = broadcastGroup.getBroadcastList();
            List<Broadcast> broadcastListNew = broadcastList.stream().filter(broadcast -> broadcast.getId().equals(new ObjectId(broadcastId))).toList();
            broadcastGroup.setBroadcastList(broadcastListNew);
            broadcastGroupRepository.save(broadcastGroup);
        } else {
            throw new BroadcastGroupNotFoundException(broadcastGroupId);
        }
    }

    public void startBroadcast(String broadcastGroupId, String broadcastId, BroadcastType type) {
        BroadcastGroup broadcastGroup = findBroadcastGroup(broadcastGroupId);
        Broadcast broadcast = findBroadcast(broadcastGroup, broadcastId);
        broadcastThreadService.startBroadcast(broadcastGroup, broadcast, type);
        updateBroadcastGroupStreamStatus(broadcastGroup, broadcastId, type, true);
    }

    private BroadcastGroup findBroadcastGroup(String broadcastGroupId) {
        Optional<BroadcastGroup> broadcastGroupOptional = broadcastGroupRepository.findById(new ObjectId(broadcastGroupId));
        if (broadcastGroupOptional.isPresent()) {
            return broadcastGroupOptional.get();
        } else {
            throw new BroadcastGroupNotFoundException(broadcastGroupId);
        }
    }

    private Broadcast findBroadcast(BroadcastGroup broadcastGroup, String broadcastId) {
        List<Broadcast> broadcastList = broadcastGroup.getBroadcastList();
        Optional<Broadcast> broadcast = broadcastList.stream().filter(b -> b.getId().equals(new ObjectId(broadcastId))).findAny();
        if (broadcast.isPresent()) {
            return broadcast.get();
        } else {
            throw new BroadcastNotFoundException(broadcastId + " id of broadcast not found");
        }
    }

    public void stopBroadcast(String broadcastGroupId, String broadcastId, BroadcastType type) {
        BroadcastGroup broadcastGroup = findBroadcastGroup(broadcastGroupId);
        Broadcast broadcast = findBroadcast(broadcastGroup, broadcastId);
        broadcastThreadService.stopBroadcast(broadcastGroup, broadcast, type);
        updateBroadcastGroupStreamStatus(broadcastGroup, broadcastId, type, false);
    }

    private void updateBroadcastGroupStreamStatus(BroadcastGroup broadcastGroup, String broadcastId, BroadcastType type, boolean isOpened) {
        for (Broadcast broadcast : broadcastGroup.getBroadcastList()) {
            if (broadcast.getId().equals(new ObjectId(broadcastId))) {
                if (type == BroadcastType.DASH) {
                    BroadcastDash broadcastDash = broadcast.getBroadcastDash();
                    broadcastDash.setDashIsOpened(isOpened);
                } else if (type == BroadcastType.HLS) {
                    BroadcastHls broadcastHls = broadcast.getBroadcastHls();
                    broadcastHls.setHlsIsOpened(isOpened);
                }
            }
        }
        broadcastGroupRepository.save(broadcastGroup);
    }
}
