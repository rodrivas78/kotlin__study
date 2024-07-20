//Vamos adicionar uma nova chave para salvar o intervalo de notificação:

object PreferencesHelper {

    private const val PREFS_NAME = "client_prefs"
    private const val KEY_NOTIFICATION_ENABLED = "notification_enabled_"
    private const val KEY_LAST_NOTIFICATION_TIME = "last_notification_time_"
    private const val KEY_NOTIFICATION_INTERVAL = "notification_interval_" // Nova chave

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setNotificationEnabled(context: Context, clientId: String, enabled: Boolean) {
        val prefs = getPreferences(context)
        prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED + clientId, enabled).apply()
    }

    fun isNotificationEnabled(context: Context, clientId: String): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_NOTIFICATION_ENABLED + clientId, false)
    }

    fun setLastNotificationTime(context: Context, clientId: String, time: Long) {
        val prefs = getPreferences(context)
        prefs.edit().putLong(KEY_LAST_NOTIFICATION_TIME + clientId, time).apply()
    }

    fun getLastNotificationTime(context: Context, clientId: String): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(KEY_LAST_NOTIFICATION_TIME + clientId, 0)
    }

    fun setNotificationInterval(context: Context, clientId: String, interval: Long) {
        val prefs = getPreferences(context)
        prefs.edit().putLong(KEY_NOTIFICATION_INTERVAL + clientId, interval).apply()
    }

    fun getNotificationInterval(context: Context, clientId: String): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(KEY_NOTIFICATION_INTERVAL + clientId, 10 * 24 * 60 * 60 * 1000L)
    }
}

//Vamos usar o intervalo de notificação salvo em SharedPreferences:

fun checkAndShowNotification(context: Context, clientId: String) {
    if (PreferencesHelper.isNotificationEnabled(context, clientId)) {
        val lastNotificationTime = PreferencesHelper.getLastNotificationTime(context, clientId)
        val currentTime = System.currentTimeMillis()
        val notificationInterval = PreferencesHelper.getNotificationInterval(context, clientId)

        if (currentTime - lastNotificationTime >= notificationInterval) {
            Toast.makeText(context, "Notificação para Cliente $clientId", Toast.LENGTH_SHORT).show()
            PreferencesHelper.setLastNotificationTime(context, clientId, currentTime)
        }
    }
}

//Adicione a configuração do intervalo de notificação:

fun setClientNotificationEnabled(context: Context, clientId: String, enable: Boolean, intervalInDays: Int) {
    PreferencesHelper.setNotificationEnabled(context, clientId, enable)
    if (enable) {
        PreferencesHelper.setLastNotificationTime(context, clientId, System.currentTimeMillis())
        val intervalInMillis = intervalInDays * 24 * 60 * 60 * 1000L
        PreferencesHelper.setNotificationInterval(context, clientId, intervalInMillis)
    }
}

//Atualize a MainActivity para lidar com o estado do Switch:

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var switchNotificationInterval: Switch
    private val clientId = "123456789" // Exemplo de ID do cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchNotificationInterval = findViewById(R.id.switchNotificationInterval)

        // Inicializar o estado do switch com base nas preferências salvas
        val isEnabled = PreferencesHelper.isNotificationEnabled(this, clientId)
        val interval = PreferencesHelper.getNotificationInterval(this, clientId)
        switchNotificationInterval.isChecked = interval == 30 * 24 * 60 * 60 * 1000L

        // Configurar listener para o switch
        switchNotificationInterval.setOnCheckedChangeListener { _, isChecked ->
            val intervalInDays = if (isChecked) 30 else 10
            setClientNotificationEnabled(this, clientId, true, intervalInDays)
        }
    }

    override fun onResume() {
        super.onResume()
        // Verificar e mostrar notificação para o cliente
        checkAndShowNotification(this, clientId)
    }
}
