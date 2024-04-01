import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SearchRepository(private val coroutineScope: CoroutineScope) {
    private val _searchResults = MutableSharedFlow<List<String>>()
    val searchResults: SharedFlow<List<String>> = _searchResults

    fun search() {
        val results = listOf("Afghanistan","Albania","Australia","Bahrain","Brazil","Egypt","Morocco","Qatar",
            "Romania","Russia","Tunisia","Turkey")

        coroutineScope.launch {
            _searchResults.emit(results)
        }
    }
}
