package sa.vrtx.android

import android.app.Activity
import sa.vrtx.sdk.Sdk
import sa.vrtx.sdk.SdkConfig

object Vrtx {
    suspend fun init(
        activity: Activity,
        config: SdkConfig,
        onExit: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        Sdk.init(
            activity = activity,
            config = config,
            onExit = onExit,
            onError = onError,
        )
    }
}
