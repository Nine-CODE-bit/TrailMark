import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trek_session")
data class TrekSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val startTime: Long,
    val endTime: Long
)