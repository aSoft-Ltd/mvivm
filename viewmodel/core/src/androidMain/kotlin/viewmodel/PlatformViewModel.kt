package viewmodel

actual abstract class PlatformViewModel : androidx.lifecycle.ViewModel() {
    protected actual open override fun onCleared() {}
}