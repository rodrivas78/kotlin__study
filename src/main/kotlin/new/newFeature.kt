import android.content.Context
import android.content.SharedPreferences

//Primeiro, crie uma classe utilitária para gerenciar SharedPreferences:

object PreferencesHelper {

    private const val PREFS_NAME = "client_prefs"
    private const val KEY_NOTIFICATION_ENABLED = "notification_enabled_"
    private const val KEY_LAST_NOTIFICATION_TIME = "last_notification_time_"

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
}

//Crie uma função que verifica se o cliente está habilitado para receber notificações e exibe um Toast se 
//estiver habilitado e 10 dias se passaram desde a última notificação:

import android.content.Context
import android.widget.Toast

fun checkAndShowNotification(context: Context, clientId: String) {
    if (PreferencesHelper.isNotificationEnabled(context, clientId)) {
        val lastNotificationTime = PreferencesHelper.getLastNotificationTime(context, clientId)
        val currentTime = System.currentTimeMillis()
        val tenDaysInMillis = 10 * 24 * 60 * 60 * 1000L

        if (currentTime - lastNotificationTime >= tenDaysInMillis) {
            Toast.makeText(context, "Notificação para Cliente $clientId", Toast.LENGTH_SHORT).show()
            PreferencesHelper.setLastNotificationTime(context, clientId, currentTime)
        }
    }
}


//Crie uma função para habilitar ou desabilitar notificações para um cliente:

fun setClientNotificationEnabled(context: Context, clientId: String, enable: Boolean) {
    PreferencesHelper.setNotificationEnabled(context, clientId, enable)
    if (enable) {
        PreferencesHelper.setLastNotificationTime(context, clientId, System.currentTimeMillis())
    }
}

//Para habilitar ou desabilitar notificações e verificar/exibir notificações:

// Habilitar notificações para o cliente 123456789
setClientNotificationEnabled(this, "123456789", true)

// Desabilitar notificações para o cliente 123456789
setClientNotificationEnabled(this, "123456789", false)

// Verificar e mostrar notificação se apropriado
checkAndShowNotification(this, "123456789")

//Para verificar e exibir notificações periodicamente (por exemplo, a cada vez que o usuário 
//abre o aplicativo), você pode chamar checkAndShowNotification no método onResume da sua Activity principal:

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        // Verificar e mostrar notificação para cada cliente
        checkAndShowNotification(this, "123456789")
        // Adicione mais chamadas se houver mais clientes
        // checkAndShowNotification(this, "987654321")
    }
}

