package ch.ututor.model.dao;
 
import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
 
import ch.ututor.model.Message;
import ch.ututor.model.User;
 
public interface MessageDao extends CrudRepository<Message, Long> {
     
    public List<Message> findBySenderAndSenderDeletedOrderByDateAndTimeDesc( User sender, boolean senderDeleted );    
    public List<Message> findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( User receiver, boolean receiverDeleted );    
    public List<Message> findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( User sender, boolean senderDeleted, User receiver, boolean userDeleted );
    public Message findById( Long id );
    public Long countByReceiverAndIsRead( User receiver, boolean isRead );
}