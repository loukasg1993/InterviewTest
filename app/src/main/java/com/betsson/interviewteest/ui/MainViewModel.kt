package com.betsson.interviewteest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betsson.interviewteest.api.Repository
import kotlinx.coroutines.launch
import com.betsson.interviewteest.data.Bet

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val bets = arrayListOf<Bet>()
    private val _betResults = MutableLiveData<List<Bet>>()
    val betResults: LiveData<List<Bet>> get() = _betResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchData() {
        viewModelScope.launch {
            try {
                //This would happen in an actual API call
//                val response = repository.getBets()
//                val sortedResponse = response.sortedBy { it.sellIn}
//                _betResults.value = sortedResponse
                val list = getItemsFromNetwork()
                val sortedList = list.sortedBy { it.sellIn }
                _betResults.value = sortedList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    private fun calculateOdds() {
        for (i in bets.indices) {
            if (bets[i].odds > 0 && bets[i].type != "First goal scorer"
                && bets[i].type != "Total score" && bets[i].type != "Number of fouls") {
                bets[i].odds = bets[i].odds - 1
            } else {
                if (bets[i].odds < 50) {
                    bets[i].odds = bets[i].odds + 1

                    if (bets[i].type == "Number of fouls") {
                        if (bets[i].sellIn < 11 && bets[i].odds < 50) {
                            bets[i].odds = bets[i].odds + 1
                        }
                        if (bets[i].sellIn < 6 && bets[i].odds < 50) {
                            bets[i].odds = bets[i].odds + 1
                        }
                    }
                }
            }

            if (bets[i].type != "First goal scorer") {
                bets[i].sellIn = bets[i].sellIn - 1
            }

            if (bets[i].sellIn < 0) {
                if (bets[i].type != "Total score") {
                    if (bets[i].type != "Number of fouls") {
                        if (bets[i].odds > 0 && bets[i].type != "First goal scorer") {
                            bets[i].odds = bets[i].odds - 1
                        }
                    } else {
                        bets[i].odds = bets[i].odds - bets[i].odds
                    }
                } else {
                    if (bets[i].odds < 50) {
                        bets[i].odds = bets[i].odds + 1
                    }
                }
            }
        }
    }

    fun updateOdds() {
        calculateOdds()
        val updatedSortedList = bets.sortedBy { it.sellIn }
        _betResults.value = updatedSortedList
    }

    private fun getItemsFromNetwork(): ArrayList<Bet> {
        bets.add(Bet("Winning team", 10, 20, "https://i.imgur.com/mx66SBD.jpeg"))
        bets.add(Bet("Total score", 2, 0, "https://i.imgur.com/VnPRqcv.jpeg"))
        bets.add(Bet("Player performance", 5, 7, "https://i.imgur.com/Urpc00H.jpeg"))
        bets.add(Bet("First goal scorer", 0, 80, "https://i.imgur.com/Wy94Tt7.jpeg"))
        bets.add(Bet("Number of fouls", 5, 49, "https://i.imgur.com/NMLpcKj.jpeg"))
        bets.add(Bet("Corner kicks", 3, 6, "https://i.imgur.com/TiJ8y5l.jpeg"))
        return bets
    }
}