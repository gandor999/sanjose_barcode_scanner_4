package database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.models.Item
import java.sql.Connection


object Database {
    var connection: Connection? = null

    suspend fun isItemInDatabaseById(id: Long): Boolean {
        check(connection != null) { "Walay connected database" }
        val query = connection?.prepareStatement("SELECT COUNT(*) FROM public.\"Item\" WHERE id = $id")

        return withContext(Dispatchers.IO) {
            query?.executeQuery().use { rs ->
                rs?.next() == true && rs.getInt(1) > 0
            }
        }
    }

    suspend fun getItemById(id: Long): Item {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(id)) { "Walay item na naay barcode na $id sa database" }
        val query = connection?.prepareStatement("SELECT * FROM public.\"Item\" WHERE id = $id")

        return withContext(Dispatchers.IO) {
            query?.executeQuery().use { rs ->
                check(rs?.next() == true || rs != null) { "Walay na kit.an" }

                Item(
                    id = rs!!.getLong("id"),
                    price = rs.getDouble("price"),
                    name = rs.getString("name")
                )
            }
        }
    }

    suspend fun insertItem(item: Item) {
        check(connection != null) { "Walay connected database" }
        check(!isItemInDatabaseById(item.id)) { "Naa nay item barcode na ${item.id} sa database" }

        val query =
            connection?.prepareStatement("INSERT INTO public.\"Item\" (id, name, price) VALUES (${item.id}, \'${item.name}\', ${item.price})")

        withContext(Dispatchers.IO) {
            query?.executeUpdate()
        }
    }

    suspend fun updateAnItem(item: Item): Boolean {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(item.id)) { "Walay item na naay barcode na ${item.id} sa database" }

        val query =
            connection?.prepareStatement("UPDATE public.\"Item\" SET name = \'${item.name}\', price = ${item.price} WHERE id = ${item.id}")

        return withContext(Dispatchers.IO) {
            query?.executeUpdate()
            true
        }
    }

    suspend fun deleteItemById(id: Long): Item {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(id)) { "Walay item na naay barcode na $id sa database" }

        val itemToBeDeleted = getItemById(id)

        val query = connection?.prepareStatement("DELETE FROM public.\"Item\" WHERE id = $id")

        return withContext(Dispatchers.IO) {
            query?.executeUpdate()
            itemToBeDeleted
        }
    }
}