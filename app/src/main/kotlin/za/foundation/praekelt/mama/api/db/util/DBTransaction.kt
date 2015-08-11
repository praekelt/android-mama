package za.foundation.praekelt.mama.api.db.util

import com.raizlabs.android.dbflow.runtime.DBTransactionInfo
import com.raizlabs.android.dbflow.runtime.TransactionManager
import com.raizlabs.android.dbflow.runtime.transaction.process.DeleteModelListTransaction
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction
import org.jetbrains.anko.AnkoLogger
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Localisation
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoPull

/**
 * DBTransactions
 * Contains common DB transactions for easy of use
 */
public object DBTransaction: AnkoLogger{
    fun saveLocales(locales: List<Localisation>): Boolean {
        info("saving locales info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(locales)))
        return true
    }

    fun saveCategories(categories: List<Category>): Boolean {
        info("saving categories info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(categories)))
        return true
    }

    fun savePages(pages: List<Page>): Boolean {
        info("saving pages info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(pages)))
        return true
    }

    private fun saveAll(categories: List<Category>, locales: List<Localisation>, pages: List<Page>): Boolean{
        return  saveCategories(categories) && saveLocales(locales) && savePages(pages)
    }

    fun saveRepo(repo: Repo): Boolean{
        return saveAll(repo.categories, repo.locales, repo.pages)
    }

    fun saveRepoPull(pull: RepoPull): Boolean{
        return saveAll(pull.categories, pull.locales, pull.pages)
                && deleteAll(pull.categories, pull.locales, pull.pages)
    }

    private fun deleteAll(categories: List<Category>, locales: List<Localisation>, pages: List<Page>): Boolean{
        return  deleteCategories(categories) && deleteLocales(locales) && deletePages(pages)
    }

    fun deleteLocales(locales: List<Localisation>): Boolean {
        info("deleting locales info")
        TransactionManager.getInstance()
                .addTransaction(DeleteModelListTransaction(ProcessModelInfo.withModels(locales)))
        return true
    }

    fun deleteCategories(categories: List<Category>): Boolean {
        info("deleting categories info")
        TransactionManager.getInstance()
                .addTransaction(DeleteModelListTransaction(ProcessModelInfo.withModels(categories)))
        return true
    }

    fun deletePages(pages: List<Page>): Boolean {
        info("deleting pages info")
        TransactionManager.getInstance()
                .addTransaction(DeleteModelListTransaction(ProcessModelInfo.withModels(pages)))
        return true
    }
}