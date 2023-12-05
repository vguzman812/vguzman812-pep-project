package Service;

import DAO.MessageDao;

public class MessageService {
    public Message getMessageById(int id){
        return MessageDao.get(id);
    }
    
}
