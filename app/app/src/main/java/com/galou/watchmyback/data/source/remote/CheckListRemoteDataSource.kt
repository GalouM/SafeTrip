package com.galou.watchmyback.data.source.remote

import com.galou.watchmyback.data.entity.CheckList
import com.galou.watchmyback.data.entity.CheckListWithItems
import com.galou.watchmyback.data.entity.ItemCheckList
import com.galou.watchmyback.data.source.CheckListDataSource
import com.galou.watchmyback.utils.CHECKLIST_COLLECTION_NAME
import com.galou.watchmyback.utils.ITEM_COLLECTION_NAME
import com.galou.watchmyback.utils.Result
import com.galou.watchmyback.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author galou
 * 2019-11-19
 */
class CheckListRemoteDataSource(
    remoteDB: FirebaseFirestore
) : CheckListDataSource {

    private val ioDispatcher = Dispatchers.IO
    private val checkListCollection = remoteDB.collection(CHECKLIST_COLLECTION_NAME)
    private val itemCollection = remoteDB.collection(ITEM_COLLECTION_NAME)

    override suspend fun fetchUserCheckLists(userId: String): Result<List<CheckListWithItems>> = withContext(ioDispatcher) {
        return@withContext when(val fetchCheckLists = checkListCollection
                .whereEqualTo("userId", userId)
                .get().await()){
                is Result.Success -> {
                    val checkListWithItems = mutableListOf<CheckListWithItems>()
                    fetchCheckLists.data.toObjects(CheckList::class.java).forEach { checkList ->
                        checkListWithItems.add(CheckListWithItems(
                            checkList = checkList,
                            items = fetchItemsCheckList(checkList) ?: mutableListOf()
                        ))
                    }
                    Result.Success(checkListWithItems)

                }
                is Result.Error -> Result.Error(fetchCheckLists.exception)
                is Result.Canceled -> Result.Canceled(fetchCheckLists.exception)
            }
    }

    override suspend fun fetchCheckList(checkList: CheckList): Result<CheckListWithItems?> = withContext(ioDispatcher) {
        return@withContext when(val items = fetchItemsCheckList(checkList)){
            null  -> Result.Error(Exception("Error while fetching list's items"))
            else -> Result.Success(CheckListWithItems(
                checkList = checkList,
                items = items
            ))
        }
    }

    override suspend fun createCheckList(
        vararg checkLists: CheckListWithItems
    ): Result<Void?> = withContext(ioDispatcher) {
        return@withContext try {
            var error = false
            coroutineScope {
                checkLists.forEach { checkList ->
                    launch {
                        when(checkListCollection.document(checkList.checkList.id).set(checkList).await()){
                            is Result.Error, is Result.Canceled -> error = true
                        }
                        when (createItemsCheckList(checkList.items)){
                            is Result.Error, is Result.Canceled -> error = true
                        }
                    }
                }
            }
            when(error){
                true -> Result.Error(Exception("Error while creating checklist"))
                false -> Result.Success(null)
            }
        } catch (e: Exception){
            Result.Error(e)
        }

    }

    override suspend fun updateCheckList(
        checkList: CheckList,
        items: List<ItemCheckList>
    ): Result<Void?> = withContext(ioDispatcher) {
        return@withContext try {
            var error = false
            coroutineScope {
                launch {
                    when(checkListCollection.document(checkList.id)
                        .update("tripType", checkList.tripType,
                            "name", checkList.name).await()){
                        is Result.Error, is Result.Canceled -> error = true
                    }
                }
                launch {
                    when(deleteItemsCheckList(checkList)){
                        is Result.Error, is Result.Canceled -> error = true
                        is Result.Success -> {
                            when(createItemsCheckList(items)){
                                is Result.Error, is Result.Canceled -> error = true
                            }
                        }
                    }
                }
            }
            when(error){
                true -> Result.Error(Exception("Error while updating checklist"))
                false -> Result.Success(null)
            }
        } catch (e: Exception){
            Result.Error(e)
        }

    }

    override suspend fun deleteCheckList(checkList: CheckList): Result<Void?> = withContext(ioDispatcher) {
        return@withContext try {
            var error = false
            coroutineScope {
                launch {
                    when(checkListCollection.document(checkList.id).delete().await()){
                        is Result.Error, is Result.Canceled -> error = true
                    }
                }
                launch {
                    when(deleteItemsCheckList(checkList)){
                        is Result.Error, is Result.Canceled -> error = true
                    }
                }
            }
            when(error){
                true -> Result.Error(Exception("Error while creating checklist"))
                false -> Result.Success(null)
            }
        } catch (e: Exception){
            Result.Error(e)
        }

    }

    private suspend fun fetchItemsCheckList(checkList: CheckList): List<ItemCheckList>? = withContext(ioDispatcher){
        return@withContext when(val items = itemCollection
            .whereEqualTo("listId", checkList.id)
            .get().await()){
            is Result.Success -> items.data.toObjects(ItemCheckList::class.java)
            is Result.Error, is Result.Canceled  -> null
        }

    }

    private suspend fun deleteItemsCheckList(checkList: CheckList): Result<Void?> = withContext(ioDispatcher) {
        var error = false
        fetchItemsCheckList(checkList)?.let { items ->
            items.forEach {
                coroutineScope {
                    launch {
                        when(itemCollection.document(it.id).delete().await()) {
                            is Result.Error, is Result.Canceled -> error = true
                        }
                    }
                    return@coroutineScope when(error) {
                        true -> Result.Error(Exception("Error while deleting items"))
                        false -> Result.Success(null)
                    }
                }
            }
        }

        return@withContext Result.Error(Exception("Error while deleting items"))

    }

    private suspend fun createItemsCheckList(items: List<ItemCheckList>): Result<Void?> = withContext(ioDispatcher){
        var error = false
        items.forEach {
            coroutineScope {
                launch {
                    when(itemCollection.document(it.id).set(it).await()) {
                        is Result.Error, is Result.Canceled -> error = true
                    }
                }
            }
        }
        return@withContext when(error) {
            true -> Result.Error(Exception("Error while creating items"))
            false -> Result.Success(null)
        }
    }
}