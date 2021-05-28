package com.laksana.githubuser.widget

/*
class FavoriteAppWidgetConfig: AppCompatActivity() {
    private val SHARED_PREFS = "prefs"
    private val KEY_BUTTON_TEXT = "keyButtonText"
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    //private var editTextButton: EditText? = null
    private var listView: ListView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.git_favorite_widget)
        val configIntent = intent
        val extras = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
        }
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
        listView = findViewById(R.id.stack_view)
    }

    fun confirmConfiguration(v: View) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val buttonIntent = Intent(this, MainActivity::class.java)
        val buttonPendingIntent = PendingIntent.getActivity(this,
                0, buttonIntent, 0)
        //val buttonText = editTextButton!!.text.toString()

        val serviceIntent = Intent(this, FavoriteWidgetService::class.java)
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
        val clickIntent = Intent(this, GitFavoriteWidget::class.java)
        clickIntent.action = ACTION_REFRESH
        val clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0)
        val views = RemoteViews(this.packageName, R.layout.widget_item)
        views.setOnClickPendingIntent(R.id.widgetUsername, buttonPendingIntent)
        //views.setCharSequence(R.id.example_widget_button, "setText", buttonText)
        views.setRemoteAdapter(R.id.stack_view, serviceIntent)
        //views.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view)
        views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
        val prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        //editor.putString(KEY_BUTTON_TEXT + appWidgetId, buttonText)
        editor.apply()
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

 */