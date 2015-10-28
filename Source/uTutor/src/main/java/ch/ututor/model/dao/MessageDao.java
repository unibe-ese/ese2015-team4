package ch.ututor.model.dao;
 
import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
 
import ch.ututor.model.Message;
import ch.ututor.model.User;
 
public interface MessageDao extends CrudRepository<Message, Long> {
     
    public List<Message> findBySenderAndSenderDeletedOrderByDateTimeDesc( User sender, boolean deleted );
     
    public List<Message> findByReceiverAndReceiverDeletedOrderByDateTimeDesc( User receiver, boolean deleted );
    
    public List<Message> findBySenderOrReceiverOrderByDateTimeDesc( User user );
    
}