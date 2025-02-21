package tr.com.muskar.MediaServer.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;

@Repository
public interface BroadcastGroupRepository extends MongoRepository<BroadcastGroup, ObjectId> {
}
