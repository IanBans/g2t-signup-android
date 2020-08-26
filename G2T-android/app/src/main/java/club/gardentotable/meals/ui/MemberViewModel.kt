package club.gardentotable.meals.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import club.gardentotable.meals.db.Member
import club.gardentotable.meals.db.MemberRepository
import club.gardentotable.meals.db.MemberRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberViewModel(app: Application): AndroidViewModel(app) {
    private val repository: MemberRepository

    val allMembers : LiveData<List<Member>>


    init {
        val membersDAO = MemberRoomDatabase.getDatabase(app, viewModelScope).memberDAO()
        repository = MemberRepository(membersDAO)
        allMembers = membersDAO.getAllOrderedLast()

    }

    fun insert(context: Context, member: Member) = viewModelScope.launch {
        repository.insert(member)

        launch(Dispatchers.IO) {
            repository.addMoreMembers()
            repository.exportToJSON(context)

        }



    }

}