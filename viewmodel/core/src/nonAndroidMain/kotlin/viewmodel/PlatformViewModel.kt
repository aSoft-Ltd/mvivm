package viewmodel

import kotlin.js.JsExport

@JsExport
actual abstract class PlatformViewModel actual constructor() {
    protected actual open fun onCleared() {}
}