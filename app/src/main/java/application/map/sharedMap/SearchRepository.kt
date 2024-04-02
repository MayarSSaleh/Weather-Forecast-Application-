package application.map.sharedMap

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
/*
countries.filter { it.first.contains(query, true) }:
This line filters the list of pairs (countries) based on whether the first element of each pair (the country name)
contains the specified query string. The contains function is used with true as the second argument, which means the search is case insensitive.

.map { it.first }:
After filtering, this line maps each filtered pair to its first element, which is the country name.
It effectively transforms the list of pairs into a list of country names that match the search query
 */
