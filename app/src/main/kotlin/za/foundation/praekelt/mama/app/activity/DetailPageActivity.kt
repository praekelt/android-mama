package za.foundation.praekelt.mama.app.activity

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
public class DetailPageActivity(): AppCompatActivity(), AnkoLogger{
    companion object{
        val TAG: String = "DetailPAgeActivity"
    }

    object argsKeys{
        val uuidKey = "pageUuid"
    }
}
