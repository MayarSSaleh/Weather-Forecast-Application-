import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SearchRepository(private val lifecycleScope: LifecycleCoroutineScope) {
    private val _searchResults = MutableSharedFlow<List<String>>()
    val searchResults: SharedFlow<List<String>> = _searchResults

    fun search(query: String, countries: List<Pair<String, Pair<Double, Double>>>) {
        // Filter countries based on the query
        val filteredCountries = countries.filter { it.first.contains(query, true) }
            .map { it.first }

        lifecycleScope.launch {
            _searchResults.emit(filteredCountries)
        }
    }
}

