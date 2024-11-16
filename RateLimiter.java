import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;
public class RateLimiter {
    public static final int noOfRequests = 100;
    public static final long allowedTimeWindow = 8;
    private static final ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<>(); 
    
    public static void main(String[] args) {
        String userId = "A";
        
        for (int i = 0; i < 105; i++) {
            if (rateLimiter(userId)) {
                System.out.println("Request " + (i + 1) + " is allowed");
            } else {
                System.out.println("Request " + (i + 1) + " is denied due to rate limiting");
            }
        }
    }
    
    public static boolean rateLimiter(String userId){
        boolean allowed = true;
        int count = 0;
        if(map.containsKey(userId)){
            UserInfo ui = map.get(userId);
            if(Instant.now().getEpochSecond()-ui.windowStartTime <allowedTimeWindow){
                if(ui.getCount()>=noOfRequests){
                return false;
                }
                ui.setCount(ui.getCount()+1);
                
            }else{
              ui.setCount(1);
              ui.windowStartTime = Instant.now().getEpochSecond();  
            }
            
        }else{
        UserInfo uin = new UserInfo(userId);
        map.put(userId,uin);
        }
        return allowed;
    }
}
class UserInfo{
    int requetsCount;
    String userId;
    long windowStartTime;
    
    public UserInfo(String userId){
        this.userId = userId;
        this.requetsCount = 1;
        this.windowStartTime = Instant.now().getEpochSecond();
    }
    
    public int getCount(){
        return requetsCount;
    }
    
    public void setCount(int count){
        this.requetsCount = count;
    }
    
}
